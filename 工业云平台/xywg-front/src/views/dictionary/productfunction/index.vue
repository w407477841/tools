<style lang="less">
@import "./index.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
        <Card>
          <Row class="operation">
            <Form ref="searchForm" :model="searchForm" inline :label-width="75" class="search-form">
              <Input
                type="text"
                v-model="searchForm.keyword"
                clearable
                placeholder="产品类别\功能类型"
                style="width: 200px;"
              />
              <Form-item>
                <Select
                  v-model="searchForm.functionName"
                  placeholder="功能名称"
                  :clearable="true"
                  :filterable="true"
                  style="width: 200px"
                  @on-change="setFunctionName"
                >
                  <Option
                    v-for="item in functionNameList"
                    :key="item.id"
                    :value="item.functionName"
                  >{{item.functionName}}</Option>
                </Select>
              </Form-item>
              <Form-item>
                <Select
                  v-model="searchForm.keyName"
                  placeholder="标识符"
                  :clearable="true"
                  :filterable="true"
                  style="width: 200px"
                  @on-change="setKeyName"
                >
                  <Option v-for="item in keyNameList" :key="item.id" :value="item.key">{{item.key}}</Option>
                </Select>
              </Form-item>
              <Form-item style="margin-left:-70px;">
                <Button @click="handleSearch" type="primary">搜索</Button>
              </Form-item>
              <FormItem style="margin-left:-70px;">
                <Button @click="addButton" type="primary">新增</Button>
              </FormItem>
              <FormItem style="margin-left:-70px;">
                <Button @click="delAll" type="error" :disabled="selectCount === 0">批量删除</Button>
              </FormItem>
            </Form>
          </Row>
          <Row class="margin-top-10 searchable-table-con1">
            <Table
              :loading="loading"
              border
              :columns="columns"
              :data="data"
              sortable="custom"
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

    <Modal
      :title="modalTitle"
      v-model="addFormVisible"
      :mask-closable="false"
      :closable="false"
      :width="500"
    >
      <Row>
        <Col style="text-align: center;margin-bottom:10px">
          <RadioGroup v-model="radioValue" @on-change="radioChange">
            <Radio :label="1" :disabled="modalTitle!='新增'&&radioValue!=1">事件</Radio>
            <Radio :label="2" :disabled="modalTitle!='新增'&&radioValue!=2">属性</Radio>
            <Radio :label="3" :disabled="modalTitle!='新增'&&radioValue!=3">方法</Radio>
          </RadioGroup>
        </Col>
      </Row>

      <!-- 这里是事件页面 开始-->
      <Row v-if="radioValue==1">
        <Form ref="addForm1" :model="addForm1" :label-width="75" :rules="addFormValidate1">
          <FormItem label="产品类型" prop="typeId">
            <Select v-model="addForm1.typeId" :disabled="isDis1">
              <Option
                v-for="item in dict.productTypeList"
                :key="item.id"
                :value="item.id"
              >{{item.name}}</Option>
            </Select>
          </FormItem>

          <FormItem label="功能名称" prop="name">
            <Input v-model="addForm1.name" :maxlength="64" :disabled="isDis1"/>
          </FormItem>

          <FormItem label="标识符" prop="key">
            <Input v-model="addForm1.key" :maxlength="64" :disabled="isDis1"/>
          </FormItem>
          <FormItem label="事件类型" prop="eventType">
            <RadioGroup v-model="addForm1.eventType">
              <Radio :label="1" :disabled="modalTitle=='查看'">信息</Radio>
              <Radio :label="2" :disabled="modalTitle=='查看'">预警</Radio>
              <Radio :label="3" :disabled="modalTitle=='查看'">故障</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="输出参数">
            <Button type="primary" @click="addParamButton(1)" :disabled="modalTitle=='查看'">增加参数</Button>
            <Tooltip
              placement="top"
              v-for="(item,index) in addForm1.list"
              :key="index"
              class="parameterTooltip"
            >
              <!-- <Tag :name="item.name+'#1'" :closable="!isDis1" @on-close="removeParam">{{item.name}}</Tag> -->
              <div class="ivu-tag ivu-tag-checked parameterTag">
                <span
                  class="ivu-tag-text"
                  @click="modalTitle == '查看'?viewParam(1,index):editParam(1,index)"
                >{{ item.name }}</span>
                <Icon
                  type="ios-close-empty"
                  @click.native.stop="modalTitle!='查看' && removeParam(1,index)"
                ></Icon>
              </div>
              <div slot="content">
                <p>功能名称：{{item.name}}</p>
                <p>标识符：{{item.key}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="备注" prop="comments">
            <Input type="textarea" v-model="addForm1.comments" :maxlength="200" :disabled="isDis1"/>
          </FormItem>
        </Form>
      </Row>
      <!-- 这里是事件页面 结束-->
      <!-- 这里是属性页面 开始-->
      <Row v-if="radioValue==2">
        <Form ref="addForm2" :model="addForm2" :label-width="75" :rules="addFormValidate2">
          <FormItem label="产品类型" prop="typeId">
            <Select v-model="addForm2.typeId" :disabled="isDis2">
              <Option
                v-for="item in dict.productTypeList"
                :key="item.id"
                :value="item.id"
              >{{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="功能名称" prop="name">
            <Input v-model="addForm2.name" :maxlength="64" :disabled="isDis2"/>
          </FormItem>
          <FormItem label="标识符" prop="key">
            <Input v-model="addForm2.key" :maxlength="64" :disabled="isDis2"/>
          </FormItem>
          <FormItem label="数据类型" prop="dataType">
            <Select v-model="addForm2.dataType" :disabled="isDis2">
              <Option
                v-for="item in dict.dataType"
                :key="item.value"
                :value="item.value"
              >{{item.key}}</Option>
            </Select>
          </FormItem>

          <!-- <FormItem label="取值范围">
            <Row>
              <Col span="11">
                <FormItem prop="min">
                  <InputNumber v-model="addForm2.min" :maxlength="12" :disabled="isDis2"></InputNumber>
                </FormItem>
              </Col>
              <Col span="2" style="text-align: center">-</Col>
              <Col span="11">
                <FormItem prop="max">
                  <InputNumber v-model="addForm2.max" :maxlength="12" :disabled="isDis2"></InputNumber>
                </FormItem>
              </Col>
            </Row>
          </FormItem>
          <FormItem label="步长" prop="step">
            <InputNumber v-model="addForm2.step" :maxlength="10" :disabled="isDis2"/>
          </FormItem>-->
          <FormItem label="单位" prop="unit">
            <Select v-model="addForm2.unit" :disabled="isDis2">
              <Option v-for="item in dict.unit" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>

          <FormItem label="读写类型" prop="wrType">
            <RadioGroup v-model="addForm2.wrType">
              <Radio :label="1">读写</Radio>
              <Radio :label="2">只读</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="备注" prop="comments">
            <Input type="textarea" v-model="addForm2.comments" :maxlength="200" :disabled="isDis2"/>
          </FormItem>
        </Form>
      </Row>
      <!-- 这里是属性页面 结束-->
      <!-- 这里是方法页面 开始-->
      <Row v-if="radioValue==3">
        <Form ref="addForm3" :model="addForm3" :label-width="75" :rules="addFormValidate3">
          <FormItem label="产品类型" prop="typeId">
            <Select v-model="addForm3.typeId" :disabled="isDis3">
              <Option
                v-for="item in dict.productTypeList"
                :key="item.id"
                :value="item.id"
              >{{item.name}}</Option>
            </Select>
          </FormItem>

          <FormItem label="功能名称" prop="name">
            <Input v-model="addForm3.name" :maxlength="64" :disabled="isDis3"/>
          </FormItem>

          <FormItem label="标识符" prop="key">
            <Input v-model="addForm3.key" :maxlength="64" :disabled="isDis3"/>
          </FormItem>
          <FormItem label="调用方式" prop="isSync">
            <RadioGroup v-model="addForm3.isSync">
              <Radio :label="1" :disabled="isDis3">同步</Radio>
              <Radio :label="2" :disabled="isDis3">异步</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="输入参数" prop="inParams">
            <Button type="primary" @click="addParamButton(2)" :disabled="modalTitle=='查看'">增加参数</Button>
            <Tooltip
              placement="top"
              v-for="(item,index) in addForm3.inList"
              :key="index"
              class="parameterTooltip"
            >
              <!-- <Tag :name="item.name+'#2'" :closable="!isDis3" @on-close="removeParam">{{item.name}}</Tag> -->
              <div class="ivu-tag ivu-tag-checked parameterTag">
                <span
                  class="ivu-tag-text"
                  @click="modalTitle=='查看' ? viewParam(2,index):editParam(2,index)"
                >{{ item.name }}</span>
                <Icon type="ios-close-empty" @click="modalTitle!='查看' && removeParam(2,index)"></Icon>
              </div>
              <div slot="content">
                <p>功能名称：{{item.name}}</p>
                <p>标识符：{{item.key}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="输出参数" prop="outParams">
            <Button type="primary" @click="addParamButton(3)" :disabled="modalTitle=='查看'">增加参数</Button>
            <Tooltip
              placement="bottom"
              v-for="(item,index) in addForm3.outList"
              :key="index"
              class="parameterTooltip"
            >
              <!-- <Tag
                :name="item.name+'#3'"
                :closable="!isDis3"
                @on-close="removeParam"
                class="parameterTag"
              >{{item.name}}</Tag>-->
              <div class="ivu-tag ivu-tag-checked parameterTag">
                <span
                  class="ivu-tag-text"
                  @click="modalTitle=='查看' ?viewParam(3,index):editParam(3,index)"
                >{{ item.name }}</span>
                <Icon
                  type="ios-close-empty"
                  @click.native.stop="modalTitle!='查看' && removeParam(3,index)"
                ></Icon>
              </div>
              <div slot="content">
                <p>功能名称：{{item.name}}</p>
                <p>标识符：{{item.key}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="备注" prop="comments">
            <Input type="textarea" v-model="addForm3.comments" :maxlength="200" :disabled="isDis3"/>
          </FormItem>
        </Form>
      </Row>
      <!-- 这里是方法页面 结束-->
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button
          type="primary"
          :loading="submitLoading"
          @click="submitProperty"
          :disabled="modalTitle=='查看'"
        >提交</Button>
      </div>
    </Modal>
    <!-- 参数页面 -->
    <Modal
      :title="paramModalTitle"
      v-model="paramVisible"
      :mask-closable="false"
      :width="500"
      :closable="false"
    >
      <Form ref="addForm4" :model="addForm4" :label-width="75" :rules="addFormValidate4">
        <FormItem label="参数名称" prop="name">
          <Input v-model="addForm4.name" :maxlength="64" :disabled="isDis4"/>
        </FormItem>
        <FormItem label="标识符" prop="key">
          <Input v-model="addForm4.key" :maxlength="64" :disabled="isDis4"/>
        </FormItem>
        <FormItem label="数据类型" prop="dataType">
          <Select v-model="addForm4.dataType" :disabled="isDis4">
            <Option v-for="item in dict.dataType" :key="item.value" :value="item.value">{{item.key}}</Option>
          </Select>
        </FormItem>

        <!-- <FormItem label="取值范围">
          <Row>
            <Col span="11">
              <FormItem prop="min">
                <InputNumber v-model="addForm4.min" :maxlength="12" :disabled="isDis4"></InputNumber>
              </FormItem>
            </Col>
            <Col span="2" style="text-align: center">-</Col>
            <Col span="11">
              <FormItem prop="max">
                <InputNumber v-model="addForm4.max" :maxlength="12" :disabled="isDis4"></InputNumber>
              </FormItem>
            </Col>
          </Row>
        </FormItem>
        <FormItem label="步长" prop="step">
          <InputNumber v-model="addForm4.step" :maxlength="10" :disabled="isDis4"></InputNumber>
        </FormItem>-->
        <FormItem label="单位" prop="unit">
          <Select v-model="addForm4.unit" :disabled="isDis4">
            <Option v-for="item in dict.unit" :key="item.id" :value="item.id">{{item.name}}</Option>
          </Select>
        </FormItem>

        <FormItem label="备注" prop="comments">
          <Input type="textarea" v-model="addForm4.comments" :maxlength="200" :disabled="isDis4"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelParam" v-if="modalTitle != '查看'">取消</Button>
        <Button type="primary" @click="submitMethod" v-if="modalTitle != '查看' && !isDis4">提交</Button>
        <Button type="primary" @click="paramVisible = false" v-if="modalTitle == '查看'">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import { getStore } from "@/utils/storage.js";
export default {
  name: "dict-manage",
  data() {
    return {
      loading: true,
      radioValue: 1, // 1 事件 2属性 3方法
      selectCount: 0,
      paramType: 1, // 参数类别 1 event ， 2 method.in ,3 method.out
      total: 0,
      selectList: [],
      addFormVisible: false, // 新增/编辑窗口 显示标识
      paramVisible: false, // 新增/编辑窗口 显示标识
      isDis1: false,
      isDis2: false,
      isDis3: false,
      isDis4: false,
      modalTitle: "",
      dict: {
        dataType: [
          { key: "int", value: 1 },
          { key: "float", value: 2 },
          { key: "double", value: 3 },
          { key: "bool", value: 4 },
          { key: "string", value: 5 },
          { key: "date", value: 6 }
        ],
        unit: [],
        productTypeList: []
      },
      pageNum: 1,
      pageSize: 10,
      searchForm: {
        keyword: ""
      },
      addForm1: {
        //事件
        id: null,
        typeId: "",
        name: "",
        key: "",
        eventType: "",
        comments: "",
        list: []
      },
      addForm2: {
        // 属性
        id: null,
        typeId: "",
        name: "",
        key: "",
        dataType: 1,
        min: new Number(),
        max: new Number(),
        step: new Number(),
        unit: "",
        wrType: 1,
        comments: ""
      },
      addForm3: {
        //方法
        id: null,
        typeId: "",
        name: "",
        key: "",
        isSync: "",
        comments: "",
        inList: [],
        outList: []
      },
      addForm4: {
        //参数
        name: "",
        key: "",
        dataType: 1,
        min: new Number(),
        max: new Number(),
        step: new Number(),
        unit: ""
      },
      addFormValidate1: {
        typeId: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        name: [{ required: true, message: "请输入功能名称", trigger: "blur" }],
        key: [{ required: true, message: "请输入标识符", trigger: "blur" }],
        eventType: [
          {
            required: true,
            message: "请选择事件类型"
          }
        ]
      },
      addFormValidate2: {
        typeId: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        name: [{ required: true, message: "请输入功能名称", trigger: "blur" }],
        key: [{ required: true, message: "请输入标识符", trigger: "blur" }],
        dataType: [
          {
            required: true,
            message: "请选择数据类型"
          }
        ],
        unit: [
          {
            required: true,
            message: "请选择单位"
          }
        ],
        wrType: [
          {
            required: true,
            message: "请选择读写类型"
          }
        ]
      },
      addFormValidate3: {
        typeId: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        name: [{ required: true, message: "请输入功能名称", trigger: "blur" }],
        key: [{ required: true, message: "请输入标识符", trigger: "blur" }],
        isSync: [
          {
            required: true,
            message: "请选择调用方式"
          }
        ]
      },
      addFormValidate4: {
        name: [{ required: true, message: "请输入功能名称", trigger: "blur" }],
        key: [{ required: true, message: "请输入标识符", trigger: "blur" }],
        dataType: [
          {
            required: true,
            message: "请选择数据类型"
          }
        ],
        unit: [
          {
            required: true,
            message: "请选择单位"
          }
        ]
      },
      submitLoading: false,

      paramTableColumns: [
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
          title: "参数名称",
          key: "name",
          align: "center"
        },
        {
          title: "标识符",
          key: "key",
          align: "center"
        }
      ],

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
          title: "产品类别",
          key: "productType",
          width: 180,
          tooltip: true,
          align: "center"
        },
        {
          title: "功能类型",
          key: "functionType",
          width: 180,
          tooltip: true,
          align: "center"
        },
        {
          title: "功能名称",
          key: "functionName",
          width: 180,
          tooltip: true,
          align: "center"
        },
        {
          title: "标识符",
          key: "key",
          width: 180,
          tooltip: true,
          align: "center"
        } /**,
        {
          title: "数据类型",
          key: "dataType",
          align: "center",
          render: (h, params) => {
            let re = "";
            this.dict.dataType.forEach((item, index) => {
              if (params.row.dataType === item.value) {
                re = item.key;
                return;
              }
            });
            return h("div", re);
          }
        }*/,
        {
          title: "备注",
          key: "comments",
          tooltip: true,
          minWidth: 160,
          maxWidth: 1000,
          //width:160,
          align: "center"
        },
        {
          title: "创建时间",
          key: "createTime",
          align: "center",
          width: 180,
          tooltip: true,
          render: (h, params) => {
            let re = params.row.createTime;
            re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
            return h("div", re);
          }
        },
        {
          title: "操作",
          key: "action",
          width: 240,
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
                    size: "small"
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
                    size: "small"
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
              )
            ]);
          }
        }
      ],
      data: [],
      functionNameList: [],
      keyNameList: [],
      paramModalTitle: "",
      paramTempo: {},
      indexTempo: new Number()
    };
  },
  methods: {
    init() {
      this.accessToken = {
        accessToken: getStore("accessToken")
      };
      this.getList();
      this.getProductTypeList();
      this.getUnitList();
      this.getFunctionNameList();
      this.getKeyNameList();
    },
    /**
     * 删除标签
     */
    removeParam(pType, index) {
      let paramType = pType;

      if (paramType === 1) {
        this.$Modal.confirm({
          title: "取消",
          content: "参数将不被保存，是否确认取消？",
          onOk: () => {
            this.$delete(this.addForm1.list, index);
            this.$forceUpdate();
          }
        });
      } else if (paramType === 2) {
        this.$Modal.confirm({
          title: "取消",
          content: "参数将不被保存，是否确认取消？",
          onOk: () => {
            this.$delete(this.addForm3.inList, index);
            this.$forceUpdate();
          }
        });
      } else if (paramType === 3) {
        this.$Modal.confirm({
          title: "取消",
          content: "参数将不被保存，是否确认取消？",
          onOk: () => {
            this.$delete(this.addForm3.outList, index);
            this.$forceUpdate();
          }
        });
      }
    },
    /**
     * 查询单位
     *
     */
    getUnitList() {
      let params = {
        selectColumns: ["id", "name"],
        order: {
          open: true,
          name: "create_time",
          isAsc: false
        },
        params: []
      };
      // 多条件搜索用户列表
      this.loading = true;
      this.postBusRequest("/master/unit/selectList", params).then(res => {
        this.dict.unit = res.data;
      });
    },
    /**
     * 查询产品类别
     */
    getProductTypeList() {
      let params = {
        selectColumns: ["id", "name"],
        order: {
          open: true,
          name: "create_time",
          isAsc: false
        },
        params: []
      };
      // 多条件搜索用户列表
      this.loading = true;
      this.postBusRequest("/productType/selectList", params).then(res => {
        this.dict.productTypeList = res.data;
      });
    },
    /**
     *  分页查询列表
     */
    getList() {
      let params = {
        selectColumns: [
          "id",
          "name",
          "comments",
          "create_time as createTime",
          "create_user as createUser",
          "create_user_name as createUserName"
        ],
        order: {
          open: true,
          name: "create_time",
          isAsc: true
        },
        params: [
          {
            name: "name",
            condition: "like",
            value: this.searchForm.keyword
          },
          {
            name: "functionName",
            condition: "like",
            value: this.searchForm.functionName
          },
          {
            name: "keyName",
            condition: "like",
            value: this.searchForm.keyName
          }
        ],
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      // 多条件搜索用户列表
      this.loading = true;
      this.postBusRequest("/productFunction/selectPage", params).then(res => {
        this.loading = false;
        this.data = res.data.list;
        this.total = res.data.total;
        this.selectCount = 0;
        this.selectList = [];
      });
    },
    handleSearch() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.getList();
    },
    /**
      属性提交
    */
    submitProperty() {
      if (this.radioValue == 1) {
        // 事件
        if (this.modalTitle == "新增") {
          this.$refs.addForm1.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "是否确认提交新增内容？",
                onOk: () => {
                  // this.addForm2.createUser = 1;
                  // this.addForm2.createUserName = "admin";
                  this.postBusRequest(
                    "/productFunction/insertEvent",
                    this.addForm1
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }
        if (this.modalTitle == "编辑") {
          this.$refs.addForm1.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "是否确认提交修改内容？",
                onOk: () => {
                  // this.addForm1.modifyUser = 1;
                  // this.addForm1.modifyUserName = "admin";
                  this.postBusRequest(
                    "/productFunction/updateEvent",
                    this.addForm1
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }
      } else if (this.radioValue == 2) {
        // 属性

        if (this.modalTitle == "新增") {
          this.$refs.addForm2.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "是否确认提交新增内容？",
                onOk: () => {
                  // this.addForm2.createUser = 1;
                  // this.addForm2.createUserName = "admin";
                  this.postBusRequest(
                    "/master/property/insert",
                    this.addForm2
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }
        if (this.modalTitle == "编辑") {
          this.$refs.addForm2.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "是否确认提交修改内容？",
                onOk: () => {
                  // this.addForm2.modifyUser = 1;
                  // this.addForm2.modifyUserName = "admin";
                  this.postBusRequest(
                    "/master/property/update",
                    this.addForm2
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }
      } else if (this.radioValue == 3) {
        // 方法

        if (this.modalTitle == "新增") {
          this.$refs.addForm3.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "是否确认提交新增内容？",
                onOk: () => {
                  // this.addForm3.createUser = 1;
                  // this.addForm3.createUserName = "admin";
                  this.postBusRequest(
                    "/productFunction/insertMethod",
                    this.addForm3
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }

        if (this.modalTitle == "编辑") {
          this.$refs.addForm3.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "是否确认提交修改内容？",
                onOk: () => {
                  // this.addForm3.modifyUser = 1;
                  // this.addForm3.modifyUserName = "admin";
                  this.postBusRequest(
                    "/productFunction/updateMethod",
                    this.addForm3
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }
      }
    },
    /**
     * 参数提交按钮
     */
    submitMethod() {
      if (this.paramModalTitle == "新增参数") {
        let form = Object.assign({}, this.addForm4);

        switch (this.paramType) {
          case 1:
            let list = this.addForm1.list;
            for (let i = 0; i < list.length; i++) {
              if (list[i].key == this.addForm4.key && i != this.indexTempo) {
                this.$Message.error("参数标识符已存在");
                return;
              }
            }
            this.addForm1.list.push(form);
            break;
          case 2:
            let inList = this.addForm3.inList;
            for (let i = 0; i < inList.length; i++) {
              if (inList[i].key == this.addForm4.key && i != this.indexTempo) {
                this.$Message.error("参数标识符已存在");
                return;
              }
            }
            this.addForm3.inList.push(form);
            break;
          case 3:
            let outList = this.addForm3.outList;
            for (let i = 0; i < outList.length; i++) {
              if (outList[i].key == this.addForm4.key && i != this.indexTempo) {
                this.$Message.error("参数标识符已存在");
                return;
              }
            }
            this.addForm3.outList.push(form);
            break;
        }

        this.paramVisible = false;
      } else if (this.paramModalTitle == "修改参数") {
        let form = Object.assign({}, this.addForm4);

        switch (this.paramType) {
          case 1:
            let list = this.addForm1.list;
            for (let i = 0; i < list.length; i++) {
              if (list[i].key == this.addForm4.key && i != this.indexTempo) {
                this.$Message.error("参数标识符已存在");
                return;
              }
            }
            this.addForm1.list[this.indexTempo] = form;
            break;
          case 2:
            let inList = this.addForm3.inList;
            for (let i = 0; i < inList.length; i++) {
              if (inList[i].key == this.addForm4.key && i != this.indexTempo) {
                this.$Message.error("参数标识符已存在");
                return;
              }
            }
            this.addForm3.inList[this.indexTempo] = form;
            break;
          case 3:
            let outList = this.addForm3.outList;
            for (let i = 0; i < outList.length; i++) {
              if (outList[i].key == this.addForm4.key && i != this.indexTempo) {
                this.$Message.error("参数标识符已存在");
                return;
              }
            }
            this.addForm3.outList[this.indexTempo] = form;
            break;
        }

        this.paramVisible = false;
      }
    },
    submitDict() {
      if (this.modalTitle == "添加操作系统") {
        this.$refs.addForm2.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "是否确认提交新增内容？",
              onOk: () => {
                // this.addForm2.createUser = 1;
                // this.addForm2.createUserName = "admin";
                this.postBusRequest(
                  "/operateSystem/insert",
                  this.addForm2
                ).then(res => {
                  if (res.code == 200) {
                    this.$Message.success(res.msg);
                    this.addFormVisible = false;
                    this.init();
                  } else {
                    this.$Message.error(res.msg);
                  }
                });
              }
            });
          }
        });
      }
      if (this.modalTitle == "编辑操作系统") {
        this.$refs.addForm2.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "是否确认提交修改内容？",
              onOk: () => {
                // this.addForm2.modifyUser = 1;
                // this.addForm2.modifyUserName = "admin";
                this.postBusRequest(
                  "/operateSystem/update",
                  this.addForm2
                ).then(res => {
                  if (res.code == 200) {
                    this.$Message.success(res.msg);
                    this.addFormVisible = false;
                    this.init();
                  } else {
                    this.$Message.error(res.msg);
                  }
                });
              }
            });
          }
        });
      }
    },
    cancelDict() {
      this.addFormVisible = false;
    },
    cancelParam() {
      this.paramVisible = false;
    },
    /**
     * 新增参数按钮
     */
    addParamButton(i) {
      this.paramType = i;
      this.paramModalTitle = "新增参数";
      this.$refs.addForm4.resetFields();
      this.isDis4 = false;
      this.paramVisible = true;
    },
    //修改参数
    editParam(paramType, index) {
      this.paramType = paramType;
      this.paramModalTitle = "修改参数";
      this.indexTempo = index;
      // this.$refs.addForm4.resetFields();
      this.isDis4 = false;
      if (this.paramType == 1) {
        this.paramTempo = JSON.parse(JSON.stringify(this.addForm1.list[index]));
        this.addForm4 = JSON.parse(JSON.stringify(this.addForm1.list[index]));
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 2) {
        this.paramTempo = JSON.parse(
          JSON.stringify(this.addForm3.inList[index])
        );
        this.addForm4 = JSON.parse(JSON.stringify(this.addForm3.inList[index]));
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 3) {
        this.paramTempo = JSON.parse(
          JSON.stringify(this.addForm3.outList[index])
        );
        this.addForm4 = JSON.parse(
          JSON.stringify(this.addForm3.outList[index])
        );
        console.log(this.addForm4);
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      }
    },
    //查看参数
    viewParam(paramType, index) {
      this.paramType = paramType;
      this.paramModalTitle = "查看参数";
      this.indexTempo = index;
      // this.$refs.addForm4.resetFields();
      this.isDis4 = true;
      if (this.paramType == 1) {
        this.paramTempo = JSON.parse(JSON.stringify(this.addForm1.list[index]));
        this.addForm4 = JSON.parse(JSON.stringify(this.addForm1.list[index]));
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 2) {
        this.paramTempo = JSON.parse(
          JSON.stringify(this.addForm3.inList[index])
        );
        this.addForm4 = JSON.parse(JSON.stringify(this.addForm3.inList[index]));
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 3) {
        this.paramTempo = JSON.parse(
          JSON.stringify(this.addForm3.outList[index])
        );
        this.addForm4 = JSON.parse(
          JSON.stringify(this.addForm3.outList[index])
        );
        console.log(this.addForm4);
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      }
    },
    /**
     * 新增按钮
     */
    addButton() {
      this.modalTitle = "新增";
      this.addForm1.list = [];
      this.addForm3.inList = [];
      this.addForm3.outList = [];
      this.radioValue = 1;
      this.addFormVisible = true;
    },
    /**
     * 点击编辑按钮
     */
    edit(v) {
      this.modalTitle = "编辑";
      let id = Number.parseInt(v.id.split("#")[0]);
      this.radioValue = Number.parseInt(v.id.split("#")[1]);
      this.initForm(id, false);
    },
    remove: function(v) {
      this.$Modal.confirm({
        title: "是否确认删除选中记录",
        onOk: () => {
          this.postBusRequest("/productFunction/deletes", {
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
    check(v) {
      this.modalTitle = "查看";
      let id = Number.parseInt(v.id.split("#")[0]);
      this.radioValue = Number.parseInt(v.id.split("#")[1]);
      this.initForm(id, true);
    },
    initForm(id, isDel) {
      if (this.radioValue == 1) {
        // this.$refs.addForm1.resetFields();
        this.isDis1 = isDel;
        this.postBusRequest("/productFunction/selectEventById", {
          id: id
        }).then(res => {
          if (res.code == 200) {
            this.addForm1 = res.data;
          } else {
            this.$Message.error(res.msg);
          }
        });
      }
      if (this.radioValue == 2) {
        //    this.$refs.addForm2.resetFields();
        this.isDis2 = isDel;
        this.postBusRequest("/master/property/selectById", { id: id }).then(
          res => {
            if (res.code == 200) {
              this.addForm2 = res.data;
            } else {
              this.$Message.error(res.msg);
            }
          }
        );
      }
      if (this.radioValue == 3) {
        //    this.$refs.addForm3.resetFields();
        this.isDis3 = isDel;
        this.postBusRequest("/productFunction/selectMethodById", {
          id: id
        }).then(res => {
          if (res.code == 200) {
            this.addForm3 = res.data;
          } else {
            this.$Message.error(res.msg);
          }
        });
      }

      this.addFormVisible = true;
    },
    changePage(v) {
      this.pageNum = v;
      this.init();
    },
    changePageSize(v) {
      this.pageSize = v;
      this.init();
    },
    showSelect(e) {
      this.exportData = e;
      this.selectList = e;
      this.selectCount = e.length;
    },
    delAll: function() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未选择要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: this.$t("alertDeleteSelectAll"),
        onOk: () => {
          this.postBusRequest("/productFunction/deletes", {
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
    getFunctionNameList: function() {
      this.postBusRequest("/productFunction/selectFunctionList", {}).then(
        res => {
          this.functionNameList = res.data;
        }
      );
    },
    getKeyNameList: function() {
      this.postBusRequest("/productFunction/selectKeyList", {}).then(res => {
        this.keyNameList = res.data;
      });
    },
    setFunctionName: function(data) {
      this.searchForm.functionName = data;
    },
    setKeyName: function(data) {
      this.searchForm.keyName = data;
    },
    radioChange: function() {
      this.addForm1.list = [];
    }
  },
  watch: {
    radioValue: function(val) {
      if (val === 1) {
        //事件
        this.$nextTick(() => {
          this.$refs.addForm1.resetFields();
          this.isDis1 = false;
        });
      } else if (val === 2) {
        //属性
        this.$nextTick(() => {
          this.$refs.addForm2.resetFields();
          this.isDis2 = false;
        });
      } else if (val === 3) {
        //方法
        this.$nextTick(() => {
          this.$refs.addForm3.resetFields();
          this.isDis3 = false;
        });
      }
    }
  },
  mounted() {
    this.init();
  }
};
</script>
