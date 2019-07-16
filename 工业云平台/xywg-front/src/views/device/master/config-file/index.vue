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
              <Input type="text" v-model="searchForm.keyword" clearable placeholder="应用名称" style="width: 200px"/>
            </Form-item>
            <Form-item style="margin-left:-70px;">
              <Button @click="handleSearch" type="primary">搜索</Button>
            </Form-item>
            <FormItem style="margin-left:-70px;">
              <Button @click="addRoot" type="primary">新增</Button>
            </FormItem>
            <FormItem style="margin-left:-70px;">
              <Button @click="delAll" type="error" :disabled="this.selectList.length==0">批量删除</Button>
            </FormItem>
          </Form>
        </Row>
        <Row class="margin-top-10 searchable-table-con1">
          <Table :loading="loading" border :columns="columns" :data="data" @on-selection-change="showSelect"
                 ref="table"></Table>
        </Row>
        <Row type="flex" justify="end" class="code-row-bg page">
          <Page :current="this.pageNum" :total="total" :page-size="this.pageSize" @on-change="changePage"
                @on-page-size-change="changePageSize" :page-size-opts="[10,20,50,100]" size="small" show-total
                show-elevator show-sizer></Page>
        </Row>
      </Card>
      </Col>
    </Row>
    <Modal :title="modalTitle" v-model="addFormVisible" :mask-closable='false' :width="550">
      <Form ref="addForm" :model="addForm" :label-width="90" :rules="addFormValidate">
        <FormItem label="所属产品" prop="productId">
          <Select v-model="addForm.productId" style="width:300px" :disabled="isDis">
            <Option v-for="item in productList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="名称" prop="name">
          <Input v-model="addForm.name" :maxlength="128" :disabled="isDis"/>
        </FormItem>
        <FormItem label="版本" prop="version">
          <Input v-model="addForm.version" :maxlength="128" :disabled="isDis"/>
        </FormItem>
        <FormItem label="上传配置文件" prop="path">
          <Upload
            :default-file-list="defaultFileList"
            action="/business/fileUpload/uploadFile"
            :before-upload="beforeUpload"
            :on-success="handleSuccess"
            :headers="headers"
            name="uploadFile"
            ref="uploadFile"
            :data="otherFileData"
            :show-upload-list="showuploadlist"
            v-model="addForm.path"
          >
            <i-button type="ghost" icon="ios-cloud-upload-outline" :disabled="isDis">上传文件</i-button>
          </Upload>
        </FormItem>
        <FormItem label="备注信息" prop="comments">
          <Input v-model="addForm.comments" type="textarea" :maxlength="200" :disabled="isDis"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitDict" v-if="!isDis">提交</Button>
      </div>
    </Modal>

    <Modal title="配置下发" v-model="handleConfigVisible" :mask-closable='false' width="80%">
      <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
        <Form-item prop="type">
          <Input type="text" v-model="keywordConfig" clearable placeholder="编号/名称" style="width: 200px"/>
        </Form-item>
        <Form-item style="margin-left:-70px;">
          <Button @click="handleSearchDevice" type="primary">搜索</Button>
        </Form-item>
        <Form-item style="margin-left:-70px;">
          <Button @click="distributedProcessing" type="primary">下发</Button>
        </Form-item>
      </Form>
      <Row class="margin-top-10 searchable-table-con1">
        <Table :loading="loading" border :columns="configColumns" :data="configData" ref="table" sortable="custom"
               @on-selection-change="changeSelectConfig"></Table>
      </Row>
      <Row type="flex" justify="end" class="code-row-bg page">
        <Page :current="this.pageNumConfig" :total="totalConfig" :page-size="this.pageSizeConfig" @on-change="changePageConfig"
              @on-page-size-change="changePageSizeConfig" :page-size-opts="[10,20,50,100]" size="small" show-total
          show-elevator show-sizer></Page>
      </Row>

    </Modal>

  </div>
</template>
<script>
  import {getStore} from '@/utils/storage.js';

  export default {
    name: "operate",
    data() {
      return {
        headers: {accessToken: getStore("accessToken")},
        loading: true,
        selectCount: 0,
        total: 0,
        selectList: [],
        selectCountConfig: 0,
        totalConfig: 0,
        selectListConfig: [],
        productList: [],
        defaultFileList: [],
        otherFileData: {type: "config"},
        showuploadlist: true,
        addFormVisible: false,
        editFormVisible: false,
        handleConfigVisible:false,
        isDis: false,
        modalTitle: "",
        pageNum: 1,
        pageSize: 10,
        pageNumConfig: 1,
        pageSizeConfig: 10,
        selectConfigRow:null,
        keywordConfig:"",
        searchForm: {
          keyword: '',
        },
        addForm: {
          name: '',
          comments: ''
        },
        addFormValidate: {
          name: [
            {required: true, message: "请输入名称", trigger: "blur"}
          ]
        },
        editForm: {
          name: '',
          comments: ''
        },
        editFormValidate: {
          appName: [
            {required: true, message: "请输入应用名称", trigger: "blur"}
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
            title: "配置文件名",
            key: "name",
            tooltip: true,
            align: "center",
            width: 180
          }, {
            title: "版本",
            key: "version",
            tooltip: true,
            align: "center",
            width: 180
          },
          {
            title: "备注",
            key: "comments",
            align: "center",
            tooltip: true,
            minWidth: 180,
            maxWidth: 1000
          },
          {
            title: "创建人",
            key: "createUserName",
            tooltip: true,
            align: "center",
            width: 180
          },
          {
            title: "创建时间",
            key: "createTime",
            align: "center",
            render: (h, params) => {
              let re = params.row.createTime;
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            },
            width: 180
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
                ),h(
                  "Button",
                  {
                    props: { type: "primary", size: "small" },
                    style: { marginRight: "5px" },
                    on: {
                      click: () => {
                        this.showConfigDetail(params.row);
                      }
                    }
                  },
                  "配置"
                )
              ]);
            }
          },
        ],
        configColumns: [
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
            title: "名称",
            key: "name",
            tooltip: true,
            align: "center",
            //width: 180
          },
          {
            title: "编号",
            key: "deviceNo",
            tooltip: true,
            align: "center",
            //width: 180
          },
          {
            title: "所属产品",
            key: "productName",
            tooltip: true,
            align: "center",
            //width: 180
          },
          {
            title: "状态",
            key: "status",
            tooltip: true,
            align: "center",
            render: (h, params) => {
              if (params.row.status === 1) {
                return h("div", "未激活");
              } else if (params.row.status === 2) {
                return h("div", "离线");
              } else if (params.row.status === 3) {
                return h("div", "在线");
              }
            }
          },
        ],
        data: [],
        configData: []
      };
    },
    methods: {
      init() {
        this.accessToken = {
          accessToken: getStore("accessToken")
        };
        this.getList();
        this.getProductList();
      },
      initConfig() {

      },
      //获取列表
      getList() {
        let params = {
          selectColumns: ['id', 'name as name','product_id as productId','path', 'version', 'file_name as fileName', 'comments', 'create_time as createTime', 'create_user as createUser', 'create_user_name as createUserName'],
          order: {
            open: true,
            name: 'create_time',
            isAsc: false
          },
          params: [{
            name: 'name',
            condition: 'like',
            value: this.searchForm.keyword
          }],
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        this.loading = true;

        this.postBusRequest('/deviceConfigFile/selectFilterPage', params).then(res => {
          this.loading = false;
          this.data = res.data.list;
          this.total = res.data.total;

          this.clearSelect();
        });
      },
      //模糊查询
      handleSearch() {
        this.pageNum = 1;
        this.pageSize = 10;
        this.init();
      },
      handleSearchDevice(){
        this.pageNumConfig = 1;
        this.pageSizeConfig = 10;
        this.getTabsDataList();
      },
      //提交
      submitDict() {
        if (this.modalTitle == "新增配置文件") {
          this.$refs.addForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "新增",
                content: "确认保存吗？",
                onOk: () => {
                  this.postBusRequest('/deviceConfigFile/insert', this.addForm).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  })
                }
              });
            }
          });
        }
        if (this.modalTitle == "编辑配置文件") {
          this.$refs.editForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "编辑",
                content: "确认保存吗？",
                onOk: () => {
                  this.postBusRequest('/app/update', this.editForm).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.editFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  })
                }
              });
            }
          });
        }
      },
      //取消提交
      cancelDict() {
        if (this.modalTitle == "新增配置文件") {
          this.$Modal.confirm({
            title: "取消",
            content: "新增内容将不被保存，是否确认取消？",
            onOk: () => {
              this.addFormVisible = false;
            }
          });
        } else if (this.modalTitle == "编辑配置文件") {
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
        this.modalTitle = "新增配置文件";
        this.defaultFileList = [],
          this.$refs.addForm.resetFields();
        this.addFormVisible = true;
        this.isDis = false;
      },
      //编辑打开弹窗
      edit(v) {
        this.modalTitle = "编辑配置文件";
        this.isDis = false;
        this.$refs.addForm.resetFields();
        this.postBusRequest('/deviceConfigFile/selectById', {id: v.id}).then(res => {
          this.addForm = res.data;
          this.defaultFileList = [
            {
              name: this.addForm.fileName,
              url: this.addForm.path
            }
          ];
          this.addFormVisible = true;
        });
      },
      //单条删除
      remove: function (v) {
        this.$Modal.confirm({
          title: "删除",
          content: "确认删除选中记录吗？",
          onOk: () => {
            this.postBusRequest('/deviceConfigFile/delete', {ids: v.toString().split(",")}).then(res => {
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
        this.modalTitle = "查看配置文件";
        this.isDis = true;
        this.$refs.addForm.resetFields();
        this.postBusRequest('/deviceConfigFile/selectById', {id: v.id}).then(res => {
          this.addForm = res.data;
          this.defaultFileList = [
            {
              name: this.addForm.fileName,
              url: this.addForm.path
            }
          ];
          this.addFormVisible = true;
        });
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
      //勾选后给全局变量赋值
      showSelect(e) {
        this.selectList = e;
        this.selectCount = e.length;
      },
      //翻页
      changePageConfig(v) {
        this.pageNumConfig = v;
        this.initConfig();
      },
      //改变显示条数
      changePageSizeConfig(v) {
        this.pageSizeConfig = v;
        this.initConfig();
      },
      //勾选后给全局变量赋值
      changeSelectConfig(e) {
        this.selectListConfig = e;
        this.selectCountConfig = e.length;
      },
      //批量删除
      delAll: function () {
        if (this.selectCount <= 0) {
          this.$Message.warning("您还未选择要删除的数据");
          return;
        }
        this.$Modal.confirm({
          title: "删除",
          content: "确认删除选中记录吗？",
          onOk: () => {
            this.postBusRequest("/deviceConfigFile/delete", {ids: this.selectList.map(item => item.id)}).then(res => {
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
      clearSelect: function () {
        this.selectCount = 0;
        this.selectList = [];
      },
      // 查询产品列表
      getProductList() {
        let params = {};
        this.postBusRequest("/upgrade/getProductList", params).then(res => {
          this.productList = res.data;
        });
      },
      //上传文件的处理
      handleSuccess(res, file) {
        console.log(res);
        this.addForm.path = res.data.filePath;
        this.addForm.fileName = res.data.fileName;
        this.defaultFileList = [
          {
            name: this.addForm.fileName,
            url: this.addForm.path
          }
        ];
        this.$refs.addForm.validateField("path");
      },
      beforeUpload() {
      },
      //弹出配置设备列表
      showConfigDetail(row) {
        this.keywordConfig="";
        this.selectConfigRow = row;
        this.handleConfigVisible = true;
        this.getTabsDataList();
      },
      getTabsDataList() {
        let params = {
          body: {
            id: this.selectConfigRow.id,
            productId: this.selectConfigRow.productId
          },
          keyWord: this.keywordConfig,
          pageNum: this.pageNumConfig,
          pageSize: this.pageSizeConfig
        };
        // 多条件搜索列表
        this.postBusRequest("/deviceConfigFile/selectDevicePageList", params).then(res => {
          this.totalConfig = res.total;
          this.configData = res.list;
        });
      },
      //下发处理
      distributedProcessing(){
        let prams={
          productId: this.selectConfigRow.productId,
          packageId: this.selectConfigRow.id,
          deviceList: this.selectListConfig,
          path: this.selectConfigRow.path
        };
        this.$Modal.confirm({
          title: "确认",
          content: "确认下发该配置文件到选择的设备吗？",
          onOk: () => {
            this.postBusRequest("/iot/sendConfig", prams).then(res => {
              if (res.code === 200) {
                this.$Message.success(res.msg);
                this.init();
              } else {
                this.$Message.error(res.msg);
              }
            });
          }
        });
      }

    },
    mounted() {
      this.init();
    }
  };
</script>
