<style lang="less">
@import "./index.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
        <Card>
          <Row class="operation">
            <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
              <Form-item prop="type">
                <Input
                  type="text"
                  v-model="searchForm.keyword"
                  clearable
                  placeholder="设备编号"
                  style="width: 200px"
                />
              </Form-item>
              <Form-item style="margin-left:-70px;">
                <Button @click="handleSearch" type="primary">搜索</Button>
              </Form-item>
              <FormItem style="margin-left:-70px;">
                <Button @click="addRoot" type="primary">新增</Button>
              </FormItem>
              <FormItem style="margin-left:-70px;">
                <Button @click="inputBatch" type="primary">批量导入</Button>
              </FormItem>
              <FormItem style="margin-left:-70px;">
                <Button
                  @click="delAll"
                  type="error"
                  :disabled="selectList.length === 0 || isBatch"
                >批量删除</Button>
              </FormItem>
              <!-- <FormItem style="margin-left:-70px;">
                                <Button @click="setUse" type="success" :disabled="selectList.length === 0 || isUse">启用</Button>
                            </FormItem>
                            <FormItem style="margin-left:-70px;">
                                <Button @click="setDis" type="error" :disabled="selectList.length === 0 || isDisd">禁用</Button>
              </FormItem>-->
              <FormItem style="margin-left:-70px;">
                <Button
                  @click="activate"
                  type="success"
                  :disabled="selectList.length === 0 || isActive"
                >激活</Button>
              </FormItem>
              <FormItem style="margin-left:-70px;">
                <a href="./static/设备导入模板.xls">
                  <Button type="primary">模板下载</Button>
                </a>
              </FormItem>
            </Form>
          </Row>
          <Row class="margin-top-10 searchable-table-con1">
            <Table
              :loading="loading"
              border
              :columns="columns"
              :data="data"
              @on-selection-change="showSelect"
              ref="table"
            ></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page
              :current="this.pageNum"
              :total="total"
              :page-size="this.pageSize"
              @on-change="changePage"
              @on-page-size-change="changePageSize"
              :page-size-opts="[10,20,50,100]"
              size="small"
              show-total
              show-elevator
              show-sizer
            ></Page>
          </Row>
        </Card>
      </Col>
    </Row>
    <Modal :title="modalTitle" v-model="addFormVisible" :mask-closable="false" :width="500">
      <Form ref="addForm" :model="addForm" :label-width="75" :rules="addFormValidate">
        <FormItem label="产品名称" prop="productId">
          <Select v-model="addForm.productId" filterable clearable ref="addSel">
            <Option v-for="item in productList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="设备编号" prop="deviceNo">
          <Input v-model="addForm.deviceNo" :maxlength="64"/>
        </FormItem>
        <FormItem label="设备名称" prop="name">
          <Input v-model="addForm.name" :maxlength="64"/>
        </FormItem>
        <!-- <FormItem  label="位置" prop="position">
                    <Input v-model="addForm.position" :readonly="true">
                        <Button slot="append" @click="openMap"></Button>
                    </Input>
        </FormItem>-->
        <FormItem label="备注信息" prop="comments">
          <Input v-model="addForm.comments" type="textarea" :maxlength="200"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitDict" v-if="!isDis">提交</Button>
      </div>
    </Modal>
    <Modal :title="modalTitle" v-model="editFormVisible" :mask-closable="false" :width="500">
      <Form ref="editForm" :model="editForm" :label-width="70" :rules="editFormValidate">
        <FormItem label="产品名称" prop="productId">
          <Select v-model="editForm.productId" filterable clearable :disabled="isDis">
            <Option v-for="item in productList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="设备编号" prop="deviceNo">
          <Input v-model="editForm.deviceNo" :maxlength="64" :disabled="isDis"/>
        </FormItem>
        <FormItem label="设备名称" prop="name">
          <Input v-model="editForm.name" :maxlength="64" :disabled="isDis"/>
        </FormItem>
        <FormItem label="ip" prop="ip">
          <Input v-model="editForm.ip" :maxlength="25" :disabled="isDis"/>
        </FormItem>
        <FormItem label="版本号" prop="version">
          <div>{{editForm.version}}</div>
        </FormItem>
        <FormItem label="产品唯一编号" prop="productKey">
          <div>{{editForm.productKey}}</div>
        </FormItem>
        <FormItem label="设备密钥" prop="deviceSecret">
          <div>{{editForm.deviceSecret}}</div>
        </FormItem>
        <FormItem label="接入方式" prop="linkTypeName">
          <div>{{editForm.linkTypeName}}</div>
        </FormItem>
        <FormItem label="状态" prop="statusName">
          <div>{{editForm.statusName}}</div>
        </FormItem>
        <FormItem label="最后一次上线时间" prop="lastTime">
          <div>{{editForm.lastTime}}</div>
        </FormItem>
        <!-- <FormItem  label="位置" prop="position">
                    <Input v-model="editForm.position" :readonly="true" :disabled="isDis">
                        <Button slot="append" @click="openMap"></Button>
                    </Input>
        </FormItem>-->
        <FormItem label="备注信息" prop="comments">
          <Input v-model="editForm.comments" type="textarea" :maxlength="200" :disabled="isDis"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitDict" v-if="!isDis">提交</Button>
      </div>
    </Modal>
    <Modal title="选择位置" v-model="mapFormVisible" :mask-closable="false" :width="1000">
      <MapBaidu :isClose="true" @setShow="setShow"></MapBaidu>
    </Modal>
    <Modal title="批量导入" v-model="inputFormVisible" :mask-closable="false" :width="500">
      <Form ref="inputForm" :model="inputForm" :label-width="75" :rules="inputFormValidate">
        <FormItem label="产品名称" prop="productId">
          <Select v-model="inputForm.productId" filterable clearable>
            <Option v-for="item in productList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="文件" prop="file" style="text-align:left">
          <Upload
            action="/business/fileUpload/uploadFile"
            :show-upload-list="true"
            :before-upload="handleUpload"
            :disabled="isDis"
            v-if="inputFormVisible"
            :on-remove="handleRemove"
          >
            <Button :disabled="this.file!=''">选择文件</Button>
          </Upload>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="inputBatchSubmit" v-if="!isDis">提交</Button>
      </div>
    </Modal>
    <Modal
      :title="setFunctionTitle"
      v-model="setFunctionVisible"
      :mask-closable="false"
      :width="500"
      :closable="false"
    >
      <Tabs value="usefulFunction" class="setFunction">
        <TabPane label="可执行方法" name="usefulFunction">
          <Button
            :type="functionSelected === index?'primary':'ghost'"
            v-for="(item,index) in setFunctionList"
            :key="index"
            @click="selectFunction(item.id,index)"
            class="usefulFunctionButton"
          >{{item.name}}</Button>
        </TabPane>
      </Tabs>
      <Tabs value="paramsArea" class="setFunctionS">
        <TabPane label="参数区" name="paramsArea">
          <div v-if="attributesList.length == 0" class="setFunctionTip">
            <p>暂无参数！</p>
            <p>请选择以上可执行的方法</p>
          </div>
          <Form
            ref="attributesForm"
            :model="attributesParams"
            :label-width="150"
            label-position="left"
            :rules="attributesValidate"
          >
            <FormItem
              :label="item.name"
              :prop="item.key"
              v-for="(item,index) in attributesList"
              :key="index"
            >
              <InputNumber
                v-if="item.type == 1"
                :step="1"
                v-model="attributesParams[item.key]"
                :formatter="intFormatter"
                style="width:100%"
              ></InputNumber>
              <InputNumber
                v-if="item.type == 2 || item.type == 3"
                :step="0.1"
                v-model="attributesParams[item.key]"
                :formatter="floatFormatter"
                style="width:100%"
              ></InputNumber>
              <i-switch v-model="attributesParams[item.key]" v-if="item.type == 4">
                <span slot="open">是</span>
                <span slot="close">否</span>
              </i-switch>
              <Input v-model="attributesParams[item.key]" v-if="item.type == 5"/>
              <DatePicker
                :value="attributesParams[item.key]"
                v-if="item.type == 6"
                type="date"
                @change="dateChange"
                format="yyyy-MM-dd HH:mm:ss.SSS"
                style="width:100%"
              ></DatePicker>
            </FormItem>
          </Form>
        </TabPane>
      </Tabs>
      <div slot="footer">
        <Button type="text" @click="setFunctionCancel">取消</Button>
        <Button type="primary" :loading="setFunctitonLoading" @click="setFunctitonSubmit">提交</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import { getStore } from "@/utils/storage.js";
import MapBaidu from "./mapBaidu.vue";
import { validateIp } from "@/libs/util.js";
export default {
  components: {
    MapBaidu
  },
  name: "device",
  data() {
    let checkIp = (rule, value, callback) => {
      if (!validateIp(value) && value != "") {
        return callback(new Error("请输入正确的ip"));
      } else {
        return callback();
      }
    };
    return {
      setFunctionSelected: false,
      setFunctionTitle: "",
      setFunctionVisible: false,
      setFunctionList: [],
      loading: true,
      selectCount: 0,
      total: 0,
      isUse: true,
      isActive: true,
      isDisd: true,
      isBatch: true,
      selectList: [],
      addFormVisible: false,
      editFormVisible: false,
      mapFormVisible: false,
      inputFormVisible: false,
      isDis: false,
      modalTitle: "",
      pageNum: 1,
      pageSize: 10,
      searchForm: {
        keyword: ""
      },
      addForm: {},
      addFormValidate: {
        productId: [
          {
            required: true,
            message: "请选择产品名称"
          }
        ],
        deviceNo: [
          { required: true, message: "请输入设备编号", trigger: "blur" }
        ],
        name: [{ required: true, message: "请输入设备名称", trigger: "blur" }]
      },
      editForm: {},
      editFormValidate: {
        productId: [
          {
            required: true,
            message: "请选择产品名称"
          }
        ],
        deviceNo: [
          { required: true, message: "请输入设备编号", trigger: "blur" }
        ],
        name: [{ required: true, message: "请输入设备名称", trigger: "blur" }],
        ip: [{ validator: checkIp, trigger: "blur" }]
      },
      inputForm: {},
      inputFormValidate: {
        productId: [
          {
            required: true,
            message: "请选择产品名称"
          }
        ]
      },
      submitLoading: false,
      columns: [
        {
          type: "index",
          width: 60,
          align: "center"
        },
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "产品类型",
          key: "typeName",
          align: "center",
          tooltip: true,
          width: 180
        },
        {
          title: "产品名称",
          key: "productName",
          align: "center",
          tooltip: true,
          width: 180
        },{
          title: "设备名称",
          key: "name",
          align: "center",
          tooltip: true,
          width: 180
        },
        {
          title: "设备编号",
          key: "deviceNo",
          align: "center",
          tooltip: true,
          width: 180
        },
        {
          title: "接入方式",
          key: "linkTypeName",
          tooltip: true,
          align: "center",
          width: 180
        },
        {
          title: "状态",
          key: "status",
          align: "center",
          render: (h, params) => {
            let re = params.row.status;
            if (re === 1) {
              return h("div", "未激活");
            } else if (re === 2) {
              return h("div", "离线");
            } else if (re === 3) {
              return h("div", "在线");
            }
          },
          width: 180
        },
        {
          title: "最后上线时间",
          key: "lastTime",
          align: "center",
          tooltip: true,
          width: 150,
          render: (h, params) => {
            let re = params.row.lastTime;
            if (re == null || re == undefined || re == "") {
              return h("div", "");
            }
            re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
            return h("div", re);
          },
          width: 180
        },
        {
          title: "备注",
          key: "comments",
          align: "center",
          tooltip: true,
          width: 180
        },
        {
          title: "创建人",
          key: "createUserName",
          align: "center",
          tooltip: true,
          width: 180
        },
        {
          title: "创建时间",
          key: "createTime",
          align: "center",
          width: 180,
          render: (h, params) => {
            let re = params.row.createTime;
            re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
            return h("div", re);
          }
        },
        {
          title: "操作",
          key: "action",
          width: 300,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.check(params.row);
                    }
                  }
                },
                "查看"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small",
                    disabled: params.row.status == 3
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.edit(params.row);
                    }
                  }
                },
                "编辑"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small",
                    disabled: params.row.status == 3
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.remove(params.row.id);
                    }
                  }
                },
                "删除"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.runConcition(params.row.id);
                    }
                  }
                },
                "运行情况"
              )
            ]);
          }
        }
      ],
      data: [],
      productList: [],
      file: "",
      runConditionPath: this.$route.path + "/runCondition",
      setFunctitonLoading: false,
      functionSelected: "",
      pkSelected: "",
      snSelected: "",
      methodSelected: "",
      attributesList: [],
      attributesValidate: {}
    };
  },
  computed: {
    attributesParams() {
      let params = {};
      return params;
    }
  },
  methods: {
    //设备重启
    resetDevice(row) {
      console.log(row);
      this.postBusRequest("/iot/rebootDevice", {
        deviceList: [
          {
            deviceNo: row.deviceNo,
            productKey: row.productKey
          }
        ]
      }).then(res => {
        if (res.code == 200) {
          this.$Message.success(res.msg);
        } else if (res.code != 200) {
          this.$Message.error(res.msg);
        }
      });
    },
    //int型格式化
    intFormatter(v) {
      return parseInt(v);
    },
    //float型转化
    floatFormatter(v) {
      return parseFloat(v);
    },
    //关闭设置modal
    setFunctionCancel() {
      this.$Modal.confirm({
        title: "取消",
        content: "确认取消吗？",
        onOk: () => {
          this.pkSelected = "";
          this.snSelected = "";
          this.methodSelected = "";
          this.$refs["attributesForm"].resetFields();
          this.attributesParams = null;
          this.attributesList = [];
          this.setFunctionVisible = false;
        }
      });
    },
    //获取方法属性
    selectFunction(id, index) {
      this.postBusRequest("/device/selectParamsInfoByMethodId", {
        id: id
      }).then(res => {
        this.functionSelected = index;
        this.methodSelected = this.setFunctionList[index].key;
        console.log(res);
        if (res.code == 200) {
          this.attributesList = res.data;
          this.attributesList.forEach(el => {
            this.attributesParams[el.key] =
              el.type == 1 || el.type == 2 || el.type == 3
                ? 0
                : el.type == 4
                ? false
                : el.type == 5
                ? ""
                : el.type == 6
                ? this.moment().format("YYYY-MM-DD HH:mm:ss.SSS")
                : "";
            console.log(this.attributesParams);
            this.attributesValidate[el.key] = [
              {
                required: true,
                message: `请完善${el.name}`
              }
            ];
          });
        }
      });
    },
    //提交设置的方法属性
    setFunctitonSubmit() {
      console.log(this.attributesParams);
      this.$refs.attributesForm.validate(valid => {
        if (valid) {
          this.$Modal.confirm({
            title: "提交",
            content: "确认提交吗？",
            onOk: () => {
              this.postBusRequest(
                `/iot/${this.pkSelected}/${this.snSelected}/method/${
                  this.methodSelected
                }`,
                this.attributesParams
              ).then(res => {
                console.log(res);
                if (res.code == 200) {
                  this.pkSelected = "";
                  this.snSelected = "";
                  this.methodSelected = "";
                  this.$refs["attributesForm"].resetFields();
                  this.attributesParams = null;
                  this.attributesList = [];
                  this.setFunctionVisible = false;
                  this.$Message.success(res.message);
                }
              });
            }
          });
        }
      });
    },
    //获取当前产品的方法列表
    setfunction(id) {
      this.setFunctionVisible = true;
      this.functionSelected = "";
      this.pkSelected = "";
      this.snSelected = "";
      this.postBusRequest("/device/selectMethodInfoByDeviceId", {
        id: id
      }).then(res => {
        console.log(res);
        if (res.code == 200) {
          this.setFunctionList = res.data.productMethods;
          this.pkSelected = res.data.pk;
          this.snSelected = res.data.sn;
        }
      });
    },
    init() {
      this.accessToken = {
        accessToken: getStore("accessToken")
      };
      this.getList();
    },
    //获取列表
    getList() {
      let params = {
        selectColumns: [],
        order: {},
        params: [
          {
            name: "device_no",
            condition: "like",
            value: this.searchForm.keyword
          }
        ],
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      this.columns = [];
      this.postBusRequest("/device/selectDevice", params).then(res => {
        this.loading = false;
        this.data = res.data.list;
        this.total = res.data.total;
        this.columns = [
          {
            type: "index",
            width: 60,
            align: "center"
          },
          {
            type: "selection",
            width: 60,
            align: "center"
          },
          {
            title: "产品类型",
            key: "typeName",
            align: "center",
            tooltip: true,
            width: 180
          },
          {
            title: "产品名称",
            key: "productName",
            align: "center",
            tooltip: true,
            width: 180
          },{
            title: "设备名称",
            key: "name",
            align: "center",
            tooltip: true,
            width: 180
          },
          {
            title: "设备编号",
            key: "deviceNo",
            align: "center",
            tooltip: true,
            width: 180
          },
          {
            title: "接入方式",
            key: "linkTypeName",
            tooltip: true,
            align: "center",
            width: 180
          },
          {
            title: "状态",
            key: "status",
            align: "center",
            render: (h, params) => {
              let re = params.row.status;
              if (re === 1) {
                return h("div", "未激活");
              } else if (re === 2) {
                return h("div", "离线");
              } else if (re === 3) {
                return h("div", "在线");
              }
            },
            width: 180
          },
          {
            title: "最后上线时间",
            key: "lastTime",
            align: "center",
            tooltip: true,
            width: 150,
            render: (h, params) => {
              let re = params.row.lastTime;
              if (re == null || re == undefined || re == "") {
                return h("div", "");
              }
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            },
            width: 180
          },
          {
            title: "备注",
            key: "comments",
            align: "center",
            tooltip: true,
            width: 180
          },
          {
            title: "创建人",
            key: "createUserName",
            align: "center",
            tooltip: true,
            width: 180
          },
          {
            title: "创建时间",
            key: "createTime",
            align: "center",
            width: 180,
            render: (h, params) => {
              let re = params.row.createTime;
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            }
          },
          {
            title: "操作",
            key: "action",
            width: 350,
            align: "center",
            fixed: "right",
            render: (h, params) => {
              return h("div", [
                h(
                  "Button",
                  {
                    props: {
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.check(params.row);
                      }
                    }
                  },
                  "查看"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "success",
                      size: "small",
                      disabled: params.row.status == 3
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.edit(params.row);
                      }
                    }
                  },
                  "编辑"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "error",
                      size: "small",
                      disabled: params.row.status == 3
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.remove(params.row.id);
                      }
                    }
                  },
                  "删除"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "primary",
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.runConcition(params.row.id);
                      }
                    }
                  },
                  "运行情况"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "warning",
                      size: "small",
                      disabled: params.row.status != 3
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.setfunction(params.row.id);
                      }
                    }
                  },
                  "设置"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "error",
                      size: "small",
                      shape: "circle",
                      disabled: params.row.status != 3
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.resetDevice(params.row);
                      }
                    }
                  },
                  "重启"
                )
              ]);
            }
          }
        ];
        this.clearSelect();
      });
    },
    //模糊查询
    handleSearch() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.init();
    },
    //提交
    submitDict() {
      if (this.modalTitle == "新增设备") {
        this.$refs.addForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "新增",
              content: "确认保存吗？",
              onOk: () => {
                this.addForm.status = 1;
                this.postBusRequest("/device/insert", this.addForm).then(
                  res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  }
                );
              }
            });
          }
        });
      }
      if (this.modalTitle == "编辑设备") {
        this.$refs.editForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "编辑",
              content: "确认保存吗？",
              onOk: () => {
                this.postBusRequest("/device/update", this.editForm).then(
                  res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.editFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  }
                );
              }
            });
          }
        });
      }
    },
    //取消提交
    cancelDict() {
      if (this.modalTitle == "新增设备") {
        this.$Modal.confirm({
          title: "取消",
          content: "新增内容将不被保存，是否确认取消？",
          onOk: () => {
            this.addFormVisible = false;
          }
        });
      } else if (this.modalTitle == "编辑设备") {
        this.$Modal.confirm({
          title: "取消",
          content: "修改内容将不被保存，是否确认取消？",
          onOk: () => {
            this.editFormVisible = false;
          }
        });
      } else {
        this.editFormVisible = false;
      }
    },
    //新增打开弹窗
    addRoot() {
      this.modalTitle = "新增设备";
      this.$refs.addForm.resetFields();
      this.$refs.addSel.clearSingleSelect();
      this.addForm.parentId = 0;
      this.addFormVisible = true;
      this.isDis = false;
    },
    //编辑打开弹窗
    edit(v) {
      
      this.modalTitle = "编辑设备";
      this.isDis = false;
      this.$refs.editForm.resetFields();
      this.postBusRequest("/device/selectDeviceById", { id: v.id }).then(
        res => {
          this.editForm = res.data;
          this.editFormVisible = true;
        }
      );
    },
    //单条删除
    remove: function(v) {
      this.$Modal.confirm({
        title: "删除",
        content: "确认删除选中记录吗？",
        onOk: () => {
          this.postBusRequest("/device/delete", {
            ids: v.toString().split(",")
          }).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //查看打开弹窗
    check(v) {
      this.modalTitle = "查看设备";
      this.isDis = true;
      this.$refs.editForm.resetFields();
      this.postBusRequest("/device/selectDeviceById", { id: v.id }).then(
        res => {
          this.editForm = res.data;
          this.editFormVisible = true;
        }
      );
    },
    //翻页
    changePage(v) {
      this.pageNum = v;
      this.init();
    },
    //改变显示条数
    changePageSize(v) {
      this.pageSize = v;
      this.init();
    },
    //勾选时给全局变量赋值
    showSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
      //循环判断激活按钮是否可用
      for (let i = 0; i < e.length; i++) {
        if (e[i].status === 1) {
          this.isActive = false;
        } else {
          this.isActive = true;
          break;
        }
      }
      //循环判断启用按钮是否可用
      for (let i = 0; i < e.length; i++) {
        if (e[i].status === 2) {
          this.isUse = false;
        } else {
          this.isUse = true;
          break;
        }
      }
      //循环判断禁用按钮是否可用
      for (let i = 0; i < e.length; i++) {
        if (e[i].status === 3) {
          this.isDisd = false;
        } else {
          this.isDisd = true;
          break;
        }
      }
      //循环判断批量删除按钮是否可用
      for (let i = 0; i < e.length; i++) {
        if (e[i].status === 1 || e[i].status === 2) {
          this.isBatch = false;
        } else {
          this.isBatch = true;
          break;
        }
      }
    },
    //批量删除
    delAll: function() {
      this.$Modal.confirm({
        title: "删除",
        content: "确认删除选中记录吗？",
        onOk: () => {
          this.postBusRequest("/device/delete", {
            ids: this.selectList.map(item => item.id)
          }).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //批量导入打开弹窗
    inputBatch: function() {
      this.inputForm = {};
      this.file = "";
      this.inputFormVisible = true;
    },
    //启用
    setUse: function() {
      let params = {
        deviceInfos: []
      };
      for (let i = 0; i < this.selectList.length; i++) {
        params.deviceInfos[i] = {
          id: this.selectList[i].id,
          status: 3,
          modifyUser: JSON.parse(this.cookies.get("userInfo")).id,
          modifyUserName: JSON.parse(this.cookies.get("userInfo")).username
        };
      }
      this.$Modal.confirm({
        title: "启用",
        content: "确认启用选中记录吗？",
        onOk: () => {
          this.postBusRequest("/device/setUse", params).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //禁用
    setDis: function() {
      let params = {
        deviceInfos: []
      };
      for (let i = 0; i < this.selectList.length; i++) {
        params.deviceInfos[i] = {
          id: this.selectList[i].id,
          status: 2,
          modifyUser: JSON.parse(this.cookies.get("userInfo")).id,
          modifyUserName: JSON.parse(this.cookies.get("userInfo")).username
        };
      }
      this.$Modal.confirm({
        title: "禁用",
        content: "确认禁用选中记录吗？",
        onOk: () => {
          this.postBusRequest("/device/setDis", params).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //跳转运行数据页面
    runConcition: function(id) {
      this.$router.push({ path: this.runConditionPath, query: { id: id } });
    },
    //获取产品列表
    getProductList() {
      let params = {
        selectColumns: ["id", "name"],
        order: {
          open: true,
          name: "create_time",
          isAsc: false
        }
      };
      this.loading = true;
      this.postBusRequest("/product/selectFilterList", params).then(res => {
        this.loading = false;
        this.productList = res.data;
      });
    },
    //打开地图
    openMap: function() {
      this.mapFormVisible = true;
    },
    //地图页面经纬度赋值给表单
    setShow: function(data) {
      if (data.sign == "关闭地图") {
        this.addForm.position = data.data.point.lng + "," + data.data.point.lat;
        this.editForm.position =
          data.data.point.lng + "," + data.data.point.lat;
        this.mapFormVisible = false;
      }
    },
    //激活
    activate: function() {
      let params = {
        deviceInfos: []
      };
      for (let i = 0; i < this.selectList.length; i++) {
        params.deviceInfos[i] = {
          id: this.selectList[i].id,
          status: 2,
          modifyUser: JSON.parse(this.cookies.get("userInfo")).id,
          modifyUserName: JSON.parse(this.cookies.get("userInfo")).username
        };
      }
      this.$Modal.confirm({
        title: "激活",
        content: "确认激活选中记录吗？",
        onOk: () => {
          this.postBusRequest("/device/setActive", params).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //文件上传获取文件对象
    handleUpload: function(file) {
      this.file = file;
    },
    //excel批量导入
    inputBatchSubmit: function() {
      this.$refs.inputForm.validate(valid => {
        if (valid) {
          let fd = new FormData();
          fd.append("file", this.file);
          fd.append("productId", this.inputForm.productId);
          this.$Modal.confirm({
            title: "批量导入",
            content: "确认批量导入吗？",
            onOk: () => {
              this.postBusRequest("/device/inputBatch", fd).then(res => {
                if (res.code == 200) {
                  this.$Message.success(res.msg);
                  this.inputFormVisible = false;
                  this.init();
                } else {
                  this.$Message.error(res.msg);
                }
              });
            }
          });
        }
      });
    },
    clearSelect: function() {
      this.selectCount = 0;
      this.selectList = [];
    },
    handleRemove: function(file) {
      this.file = "";
    }
  },
  mounted() {
    this.init();
    this.getProductList();
  }
};
</script>
