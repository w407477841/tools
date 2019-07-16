<style lang="less" scoped>
@import "package.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
        <Card>
          <Form inline :label-width="70" class="search-form">
            <Input
              type="text"
              v-model="searchName"
              clearable
              placeholder="升级包名称"
              style="width: 200px"
            />

            <Form-item class="common-margin-left">
              <Button @click="searchGetDataList" type="primary">搜索</Button>
            </Form-item>
            <Form-item class="common-margin-left">
              <Button @click="add" type="primary">新增</Button>
            </Form-item>
            <Form-item class="common-margin-left">
              <Button @click="deletes" type="error" :disabled="selectCount === 0?true:false">批量删除</Button>
            </Form-item>
          </Form>
          <Row class="margin-top-10 searchable-table-con1">
            <Table
              :loading="loading"
              border
              :columns="columns"
              :data="data"
              ref="table"
              sortable="custom"
              @on-sort-change="changeSort"
              @on-selection-change="changeSelect"
            ></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page
              :current="this.pageNumber"
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
    <!-- -----------------------------↓---------------------新增-------------------------↓--------------------------------- -->
    <Modal :title="modalTitle" v-model="dictModalVisible" :mask-closable="false" :width="500">
      <Form ref="dictForm" :model="dictForm" :label-width="120" :rules="dictFormValidate">
        <FormItem label="升级包名称" prop="name">
          <Input
            v-model="dictForm.name"
            placeholder
            :disabled="available"
            style="width:300px"
            :maxlength="20"
          />
        </FormItem>
        <FormItem label="版本号" prop="version">
          <Input
            v-model="dictForm.version"
            placeholder
            :disabled="available"
            style="width:300px"
            :maxlength="50"
          />
        </FormItem>
        <FormItem label="所属产品" prop="productId">
          <Select v-model="dictForm.productId" style="width:300px" :disabled="available">
            <Option v-for="item in productList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="上传升级包" prop="path">
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
            v-model="dictForm.path"
          >
            <i-button type="ghost" icon="ios-cloud-upload-outline" :disabled="available">上传文件</i-button>
          </Upload>
        </FormItem>
        <FormItem label="备注" prop="comments">
          <Input
            v-model="dictForm.comments"
            type="textarea"
            :autosize="{minRows: 2,maxRows: 5}"
            placeholder
            :disabled="available"
            style="width:300px"
            :maxlength="200"
          />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="dictModalCancel">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submit" :disabled="available">提交</Button>
      </div>
    </Modal>
    <!-- -----------------------------↑---------------------新增-------------------------↑----------------------------- -->
    <!-- -----------------------------↓---------------------升级详情-------------------------↓----------------------------- -->
    <Modal
      :title="modalUpgradeTitle"
      v-model="dictModalUpgradeVisible"
      :mask-closable="false"
      :width="1000"
    >
      <Tabs @on-click="tabDataTables" type="card" v-model="tabModelValue">
        <TabPane label="待升级" name="tab_0">
          <Form inline :label-width="70" class="search-form">
            <Input
              type="text"
              v-model="keyWord"
              clearable
              placeholder="升级包名称"
              style="width: 200px"
            />

            <Form-item class="common-margin-left">
              <Button @click="searchGetTabsDataList" type="primary">搜索</Button>
            </Form-item>
            <Form-item class="common-margin-left">
              <Button
                @click="toUpgrade"
                type="primary"
                :disabled="selectCount1 === 0?true:false "
              >升级</Button>
            </Form-item>
          </Form>
          <Row class="margin-top-10 searchable-table-con1">
            <Table
              :loading="loading"
              border
              :columns="tabColumns"
              :data="tabData"
              ref="table"
              sortable="custom"
              @on-sort-change="changeSort1"
              @on-selection-change="changeSelect1"
            ></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page
              :current="this.pageNumber1"
              :total="total1"
              :page-size="this.pageSize1"
              @on-change="changePage1"
              @on-page-size-change="changePageSize1"
              :page-size-opts="[10,20,50,100]"
              size="small"
              show-total
              show-elevator
              show-sizer
            ></Page>
          </Row>
        </TabPane>
        <TabPane label="升级中" name="tab_1">
          <Form inline :label-width="70" class="search-form">
            <Input
              type="text"
              v-model="keyWord"
              clearable
              placeholder="升级包名称"
              style="width: 200px"
            />

            <Form-item class="common-margin-left">
              <Button @click="searchGetTabsDataList" type="primary">搜索</Button>
            </Form-item>
          </Form>
          <Row class="margin-top-10 searchable-table-con1">
            <Table
              :loading="loading"
              border
              :columns="tabColumns"
              :data="tabData"
              ref="table"
              sortable="custom"
              @on-sort-change="changeSort1"
              @on-selection-change="changeSelect1"
            ></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page
              :current="this.pageNumber1"
              :total="total1"
              :page-size="this.pageSize1"
              @on-change="changePage1"
              @on-page-size-change="changePageSize1"
              :page-size-opts="[10,20,50,100]"
              size="small"
              show-total
              show-elevator
              show-sizer
            ></Page>
          </Row>
        </TabPane>
        <TabPane label="升级成功" name="tab_2">
          <Form inline :label-width="70" class="search-form">
            <Form-item label>
              <Input
                type="text"
                v-model="keyWord"
                clearable
                placeholder="升级包名称"
                style="width: 200px"
              />
            </Form-item>
            <Form-item class="common-margin-left">
              <Button @click="searchGetTabsDataList" type="primary">搜索</Button>
            </Form-item>
          </Form>
          <Row class="margin-top-10 searchable-table-con1">
            <Table
              :loading="loading"
              border
              :columns="tabColumns"
              :data="tabData"
              ref="table"
              sortable="custom"
              @on-sort-change="changeSort1"
              @on-selection-change="changeSelect1"
            ></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page
              :current="this.pageNumber1"
              :total="total1"
              :page-size="this.pageSize1"
              @on-change="changePage1"
              @on-page-size-change="changePageSize1"
              :page-size-opts="[10,20,50,100]"
              size="small"
              show-total
              show-elevator
              show-sizer
            ></Page>
          </Row>
        </TabPane>
        <TabPane label="升级失败" name="tab_3">
          <Form inline :label-width="70" class="search-form">
            <Form-item label>
              <Input
                type="text"
                v-model="keyWord"
                clearable
                placeholder="升级包名称"
                style="width: 200px"
              />
            </Form-item>
            <Form-item class="common-margin-left">
              <Button @click="searchGetTabsDataList" type="primary">搜索</Button>
            </Form-item>
            <Form-item class="common-margin-left">
              <Button
                @click="toUpgrade"
                type="primary"
                :disabled="selectCount1 === 0?true:false "
              >升级</Button>
            </Form-item>
          </Form>
          <Row class="margin-top-10 searchable-table-con1">
            <Table
              :loading="loading"
              border
              :columns="tabColumns"
              :data="tabData"
              ref="table"
              sortable="custom"
              @on-sort-change="changeSort1"
              @on-selection-change="changeSelect1"
            ></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page
              :current="this.pageNumber1"
              :total="total1"
              :page-size="this.pageSize1"
              @on-change="changePage1"
              @on-page-size-change="changePageSize1"
              :page-size-opts="[10,20,50,100]"
              size="small"
              show-total
              show-elevator
              show-sizer
            ></Page>
          </Row>
        </TabPane>
      </Tabs>
      <div slot="footer"></div>
    </Modal>
    <!-- -----------------------------↑---------------------升级详情-------------------------↑----------------------------- -->
  </div>
</template>

<script>
import { getStore, setStore } from "../../../utils/storage";

export default {
  name: "dict-manage",
  data() {
    return {
      loading: true,
      searchName: "",
      modalUpgradeTitle: "升级详情",
      tabModelValue: "tab_0",
      showuploadlist: true,
      userInfo: JSON.parse(this.cookies.get("userInfo")),
      selectList: [],
      selectCount: 0,
      selectList1: [],
      selectCount1: 0,
      upgradePackageId: "",
      upgradePackageVersion: "",
      searchKey: "",
      keyWord: "",
      sortColumn: "createTime",
      sortColumn1: "modifyTime",
      sortType: "desc",
      available: false,
      headers: { accessToken: getStore("accessToken") },
      productList: [],
      otherFileData: { type: "firmware" },
      data: [],
      tabData: [],
      pageNumber: 1,
      pageNumber1: 1,
      ids: [],
      pageSize: 10,
      pageSize1: 10,
      total: 0,
      total1: 0,
      startDate: "",
      endDate: "",
      modalTitle: "",
      dictFormValidate: {
        productId: [
          {
            required: true,
            message: "所属产品不能为空",
            trigger: "change",
            type: "number"
          }
        ],
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        version: [{ required: true, message: "版本不能为空", trigger: "blur" }],
        path: [{ required: true, message: "升级包不能为空" }]
      },
      dictForm: {
        id: "",
        name: "",
        version: "",
        productId: "",
        path: "",
        fileName: "",
        comments: ""
      },
      dictModalVisible: false,
      dictModalUpgradeVisible: false,
      submitLoading: false,
      selectRow: null,
      columns: [
        { type: "selection", width: 60, align: "center" },
        {
          title: "升级包名称",
          key: "name",
          align: "center",
          tooltip: true /*sortable: true*/
        },
        {
          title: "产品名称",
          key: "productName",
          align: "center",
          tooltip: true /*sortable: true*/
        },
        {
          title: "版本",
          key: "version",
          align: "center",
          tooltip: true /*sortable: true*/
        },
        { title: "状态", key: "status", align: "center" /*sortable: true*/ },
        {
          title: "操作时间",
          key: "createTime",
          align: "center" /*sortable: true,*/ /*sortType: "desc"*/
        },
        {
          title: "操作",
          key: "action",
          fixed: "right",
          align: "center",
          width: 230,
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: { size: "small" },
                  style: { marginRight: "5px" },
                  on: {
                    click: () => {
                      this.showDictDetail(params.row);
                    }
                  }
                },
                "查看"
              ),
              h(
                "Button",
                {
                  props: { type: "success", size: "small" },
                  style: { marginRight: "5px" },
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
                  props: { type: "error", size: "small" },
                  style: { marginRight: "5px" },
                  on: {
                    click: () => {
                      this.delete(params.row.id);
                    }
                  }
                },
                "删除"
              ),
              h(
                "Button",
                {
                  props: { type: "primary", size: "small" },
                  style: { marginRight: "5px" },
                  on: {
                    click: () => {
                      this.showUpgradeDetail(params.row);
                    }
                  }
                },
                "升级"
              )
            ]);
          }
        }
      ],
      tabColumns: [
        { type: "selection", width: 60, align: "center" },
        {
          title: "设备名称",
          key: "name",
          align: "center",
          tooltip: true /*sortable: true*/
        },
        {
          title: "产品名称",
          key: "productName",
          align: "center",
          tooltip: true /*sortable: true*/
        },
        {
          title: "当前版本",
          key: "version",
          align: "center",
          tooltip: true /*sortable: true*/
        },
        {
          title: "状态",
          key: "status",
          align: "center",
          /*sortable: true,*/ render: (h, params) => {
            if (params.row.status === 1) {
              return h("div", "未激活");
            } else if (params.row.status === 2) {
              return h("div", "离线");
            } else if (params.row.status === 3) {
              return h("div", "在线");
            }
          }
        },
        {
          title: "更新时间",
          key: "modifyTime",
          align: "center",
          /*sortable: true,*/ sortType: "desc"
        }
      ],
      defaultFileList: []
    };
  },
  methods: {
    init() {
      this.pageNumber = 1;
      this.pageNumber1 = 1;
      this.getDataList();
      this.getProductList();
    },
    searchGetDataList() {
      this.pageNumber = 1;
      this.getDataList();
    },
    searchGetTabsDataList() {
      this.pageNumber1 = 1;
      this.getTabsDataList();
    },
    changePage(v) {
      this.pageNumber = v;
      this.getDataList();
      this.clearSelectAll();
    },
    changePageSize(v) {
      this.pageSize = v;
      this.getDataList();
    },

    getDataList() {
      let params = {
        body: {
          name: this.searchName
        },
        pageNum: this.pageNumber,
        pageSize: this.pageSize
      };
      // 多条件搜索列表
      this.loading = true;
      this.postBusRequest("/upgrade/selectPageList", params).then(res => {
        console.log(res);
        this.loading = false;
        this.total = res.total;
        this.data = res.list;
      });
    },
    getProductList() {
      let params = {};
      this.postBusRequest("/upgrade/getProductList", params).then(res => {
        this.productList = res.data;
      });
    },
    handleReset() {
      this.searchDictName = "";
      this.getDataList();
    },
    add() {
      this.modalType = 0;
      this.modalTitle = "新增";
      this.dictForm = {
        id: "",
        name: "",
        version: "",
        productId: "",
        path: "",
        fileName: "",
        comments: ""
      };
      this.available = false;
      this.$refs.dictForm.resetFields();
      this.$refs.uploadFile.clearFiles();
      this.dictModalVisible = true;
    },
    //编辑时获取数据
    edit(row) {
      this.modalType = 0;
      this.modalTitle = "编辑";
      this.$refs.uploadFile.clearFiles();
      this.$refs.dictForm.resetFields();
      this.getBusRequest("/upgrade/getById", { id: row.id }).then(res => {
        if (res.code === 200) {
          this.dictForm = {
            id: res.data.id,
            name: res.data.name,
            version: res.data.version,
            productId: res.data.productId,
            path: res.data.path,
            fileName: res.data.fileName,
            comments: res.data.comments
          };
          this.defaultFileList = [];
          this.defaultFileList = [
            {
              name: this.dictForm.fileName,
              url: this.dictForm.path
            }
          ];
          setTimeout(() => {
            console.log(document.querySelector(".ivu-upload-list-remove"));
            document.querySelector(".ivu-upload-list-remove").style.display =
              "block";
          }, 50);
          this.available = false;
          this.dictModalVisible = true;
        }
      });
    },
    deletes() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未选择要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
        onOk: () => {
          let ids = [];
          this.selectList.forEach(function(e) {
            ids.push(e.id);
          });
          this.ids = ids;
          this.executeDelete();
        }
      });
    },
    delete(id) {
      this.ids = [];
      this.ids.push(id);
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除该条数据?",
        onOk: () => {
          this.executeDelete();
        }
      });
    },
    executeDelete() {
      let params = {
        ids: this.ids,
        modifyUser: this.userInfo.id,
        modifyUserName: this.userInfo.username
      };
      this.postBusRequest("/upgrade/deletes", params).then(res => {
        if (res.code === 200) {
          this.$Message.success(res.msg);
          this.selectCount = 0;
          this.selectList = [];
          this.init();
        }
      });
    },
    //关闭
    dictModalCancel() {
      if (this.modalTitle === "新增") {
        this.$Modal.confirm({
          title: "取消",
          content: "新增内容将不被保存，是否确认取消？",
          onOk: () => {
            this.dictModalVisible = false;
            this.dictModalUpgradeVisible = false;
            this.available = false;
          }
        });
      } else if (this.modalTitle === "编辑") {
        this.$Modal.confirm({
          title: "取消",
          content: "修改内容将不被保存，是否确认取消？",
          onOk: () => {
            this.dictModalVisible = false;
            this.dictModalUpgradeVisible = false;
            this.available = false;
          }
        });
      } else {
        this.dictModalVisible = false;
        this.dictModalUpgradeVisible = false;
        this.available = false;
      }
    },
    submit() {
      if (this.dictForm.id === "") {
        //新增
        this.addSubmit();
      } else {
        //修改
        this.editSubmit();
      }
    },
    addSubmit() {
      this.$refs.dictForm.validate(valid => {
        if (valid) {
          this.dictForm.createUser = this.userInfo.id;
          this.dictForm.createUserName = this.userInfo.username;
          this.dictForm.createTime = this.moment().format(
            "YYYY-MM-DD HH:mm:ss"
          );
          this.postBusRequest("/upgrade/insert", this.dictForm).then(res => {
            if (res.code === 200) {
              this.$Message.success(res.msg);
              this.dictModalVisible = false;
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    editSubmit() {
      this.$refs.dictForm.validate(valid => {
        if (valid) {
          this.dictForm.modifyUser = this.userInfo.id;
          this.dictForm.modifyUserName = this.userInfo.username;
          this.postBusRequest("/upgrade/updateById", this.dictForm).then(
            res => {
              if (res.code === 200) {
                this.$Message.success(res.msg);
                this.dictModalVisible = false;
                this.init();
              } else {
                this.$Message.error(res.msg);
              }
            }
          );
        }
      });
    },
    showDictDetail(row) {
      this.modalType = 1;
      this.modalTitle = "查看";
      this.$refs.uploadFile.clearFiles();
      this.$refs.dictForm.resetFields();
      this.getBusRequest("/upgrade/getById", { id: row.id }).then(res => {
        if (res.code === 200) {
          this.dictForm = {
            id: res.data.id,
            name: res.data.name,
            version: res.data.version,
            productId: res.data.productId,
            path: res.data.path,
            fileName: res.data.fileName,
            comments: res.data.comments
          };
          this.defaultFileList = [];
          this.defaultFileList = [
            {
              name: this.dictForm.fileName,
              url: this.dictForm.path
            }
          ];
          setTimeout(() => {
            console.log(document.querySelector(".ivu-upload-list-remove"));
            document.querySelector(".ivu-upload-list-remove").style.display =
              "none";
          }, 50);
          this.available = true;
          this.dictModalVisible = true;
        }
      });
    },

    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    changeSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },
    changeSort(e) {
      this.sortColumn = e.key;
      this.sortType = e.order;
      if (e.order === "normal") {
        this.sortType = "";
      }
      this.getDataList();
    },
    changeSelect1(e) {
      this.selectList1 = e;
      this.selectCount1 = e.length;
    },
    changeSort1(e) {
      this.sortColumn1 = e.key;
      this.sortType1 = e.order;
      if (e.order === "normal") {
        this.sortType1 = "";
      }
      this.getTabsDataList();
    },
    //上传文件的处理
    handleSuccess(res, file) {
      console.log(res);
      this.dictForm.path = res.data.filePath;
      this.dictForm.fileName = res.data.fileName;
      this.defaultFileList = [
        {
          name: this.dictForm.fileName,
          url: this.dictForm.path
        }
      ];
      this.$refs.dictForm.validateField("path");
    },
    beforeUpload() {},

    //升级弹窗
    changePage1(v) {
      this.pageNumber1 = v;
      this.getTabsDataList();
      this.clearSelectAll();
    },
    changePageSize1(v) {
      this.pageSize1 = v;
      this.getTabsDataList();
    },
    //一开始加载的  升级列表处理开始
    showUpgradeDetail(row) {
      this.tabModelValue = "tab_0";
      this.modalTitle = "";
      this.selectRow = row;
      this.dictModalUpgradeVisible = true;
      this.getTabsDataList();
    },
    //切换tab 时
    tabDataTables() {
      this.keyWord = "";
      this.selectList1 = [];
      this.selectCount1 = 0;
      this.pageNumber1 = 1;
      this.getTabsDataList();
    },
    //点击真正的升级时
    toUpgrade() {
      if (this.selectCount1 <= 0) {
        this.$Message.warning("您还未选择要升级的设备");
        return;
      }
      this.$Modal.confirm({
        title: "确认升级",
        content: "您确认要升级所选的 " + this.selectCount1 + " 个设备?",
        onOk: () => {
          this.selectRow.deviceList = this.selectList1;
          let params = this.selectRow;
          //添加升级履历  并发送相关指令
          this.postBusRequest("/iot/upgrade", params).then(res => {
            if (res.code === 200) {
              this.$Message.success(res.msg);
              this.selectCount1 = 0;
              this.selectList1 = [];
              this.getTabsDataList();
            }
          });
        }
      });
    },
    //加载弹窗list
    getTabsDataList() {
      let statusVal = this.tabModelValue.substring(4, 5);
      let params = {
        body: {
          id: this.selectRow.id,
          productId: this.selectRow.productId,
          productVersion: this.selectRow.version,
          status: statusVal
        },
        keyWord: this.keyWord,
        pageNum: this.pageNumber1,
        pageSize: this.pageSize1
      };
      // 多条件搜索列表
      //this.loading = true;
      this.postBusRequest("/upgrade/selectDevicePageList", params).then(res => {
        //this.loading = false;
        this.total1 = res.total;
        this.tabData = res.list;
      });
    }
  },
  mounted() {
    this.init();
  }
};
</script>
