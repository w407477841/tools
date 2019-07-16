<style lang="less" >
  @import "config.less";
</style>
<style>
  .ivu-page.mini .ivu-page-options-elevator input {
    height: 22px;
  }

</style>
<template>
  <div class="search">
    <Card>
      <Tabs value="name1" type="card" @on-click="tabDataTables" v-model="tabModelValue">
        <TabPane label="远程配置" name="name1">
          <Form inline :label-width="70" class="search-form">
            <Form-item label="">
              <Select v-model="productId" filterable @on-change="serachGetDeviceList" clearable placeholder="所属产品">
                <Option v-for="item in productList" :value="item.id" :key="item.id">{{ item.name }}</Option>
              </Select>
            </Form-item>
            <Form-item label="">
              <Input type="text" v-model="keyWord" @on-change="serachGetDeviceList" clearable placeholder="设备名称" style="width: 200px"/>
            </Form-item>
            <Form-item class="common-margin-left">
              <Button @click="serachGetDeviceList" type="primary">搜索</Button>
            </Form-item>
            <Form-item class="common-margin-left">
              <Button @click="buttonRemoteConfig" type="primary" :disabled="selectCount === 0?true:false">远程配置</Button>
            </Form-item>
          </Form>
          <Row class="margin-top-10 searchable-table-con1">
            <Table :loading="loading" border :columns="columns" :data="data" ref="table" sortable="custom"
                   @on-sort-change="changeSort" @on-selection-change="changeSelect"></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page :current="this.pageNumber" :total="total" :page-size="this.pageSize" @on-change="changePage"
                  @on-page-size-change="changePageSize" :page-size-opts="[10,20,50,100]" size="small" show-total
                  show-elevator show-sizer></Page>
          </Row>
          <!--配置编辑弹出层-->
          <Modal title="配置模板" v-model="configEditModalVisible" :mask-closable='false' :width="800">
            <vue-json-editor v-model="jsonData" mode='code' :showBtns="false" ref="jsonDataRef"
                             @json-change="onJsonChange"></vue-json-editor>
            <div slot="footer">
              <Button type="text" @click="configEditModalCancel">取消</Button>
              <Button type="primary" @click="batchUpdate" :disabled="available">批量更新</Button>
            </div>
          </Modal>
          <!--设备配置履历弹出层-->
          <Modal title="配置履历" v-model="configRecordModalVisible" :mask-closable='false' :width="1200">
            <Row class="margin-top-10 searchable-table-con1">
              <Table :loading="loading" border :columns="columnsRecord" :data="dataRecord" ref="tableRecord"
                     sortable="custom"
                     @on-sort-change="changeSortRecord" @on-selection-change="changeSelectRecord"></Table>
            </Row>
            <Row type="flex" justify="end" class="code-row-bg page">
              <Page :current="this.pageNumberRecord" :total="totalRecord" :page-size="this.pageSizeRecord"
                    @on-change="changePageRecord"
                    @on-page-size-change="changePageSizeRecord" :page-size-opts="[10,20,50,100]" size="small" show-total
                    show-elevator show-sizer></Page>
            </Row>
            <div slot="footer"></div>
          </Modal>
        </TabPane>

        <!--  ------------------------------------------------------配置恢复----------------------------------------------------------- -->
        <!--配置列表-->
        <TabPane label="配置恢复" name="name2">
          <Row class="margin-top-10 searchable-table-con1">
            <Table :loading="loading" border :columns="columnsConfig" :data="dataConfig" ref="tableConfig"
                   sortable="custom"
                   @on-sort-change="changeSortConfig" @on-selection-change="changeSelectConfig"></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page :current="this.pageNumberConfig" :total="totalConfig" :page-size="this.pageSizeConfig"
                  @on-change="changePageConfig"
                  @on-page-size-change="changePageSizeConfig" :page-size-opts="[10,20,50,100]" size="small" show-total
                  show-elevator show-sizer></Page>
          </Row>

          <!--查看受影响的设备弹出层-->
          <Modal title="受影响的设备" v-model="influencedDeviceModalVisible" :mask-closable='false' :width="1200">
            <Row class="margin-top-10 searchable-table-con1">
              <Table :loading="loading" border :columns="columnsInfluencedDevice" :data="dataInfluencedDevice"
                     ref="tableInfluencedDevice"
                     sortable="custom"
                     @on-sort-change="changeSortInfluencedDevice"
                     @on-selection-change="changeSelectInfluencedDevice"></Table>
            </Row>
            <Row type="flex" justify="end" class="code-row-bg page">
              <Page :current="this.pageNumberInfluencedDevice" :total="totalInfluencedDevice"
                    :page-size="this.pageSizeInfluencedDevice"
                    @on-change="changePageInfluencedDevice"
                    @on-page-size-change="changePageSizeInfluencedDevice" :page-size-opts="[10,20,50,100]" size="small"
                    show-total
                    show-elevator show-sizer></Page>
            </Row>
            <div slot="footer"></div>
          </Modal>


        </TabPane>
        <!--  ------------------------------------------------------配置恢复----------------------------------------------------------- -->
      </Tabs>
    </Card>
  </div>
</template>
<script>
  import vueJsonEditor from 'vue-json-editor';

  export default {
    name: "dict-manage",
    components: {
      vueJsonEditor,
    },
    data() {
      return {
        loading: true,
        selectList: [],
        selectCount: 0,
        selectListRecord: [],
        selectCountRecord: 0,
        deviceList: [],
        productList: [],
        keyWord: "",
        productId: "",
        tabModelValue: "name1",
        configEditModalVisible: false,
        configRecordModalVisible: false,
        influencedDeviceModalVisible: false,
        userInfo: JSON.parse(this.cookies.get('userInfo')),
        sortColumn: "createTime",
        sortType: "desc",
        sortColumnRecord: "createTime",
        sortTypeRecord: "desc",
        available: false,
        jsonData: {},
        data: [],
        pageNumber: 1,
        ids: [],
        pageSize: 10,
        total: 0,
        dataRecord: [],
        pageNumberRecord: 1,
        pageSizeRecord: 10,
        totalRecord: 0,

        dataInfluencedDevice: [],
        selectListInfluencedDevice: [],
        selectCountInfluencedDevice: 0,
        sortColumnInfluencedDevice: "createTime",
        sortTypeInfluencedDevice: "desc",
        pageNumberInfluencedDevice: 1,
        pageSizeInfluencedDevice: 10,
        totalInfluencedDevice: 0,

        selectListConfig: [],
        selectCountConfig: 0,
        sortColumnConfig: "createTime",
        sortTypeConfig: "desc",
        dataConfig: [],
        pageNumberConfig: 1,
        pageSizeConfig: 10,
        totalConfig: 0,
        columnsConfig: [{type: "index", width: 60, align: "center"},
          /*{type: "selection", width: 60, align: "center"},*/
          {title: "操作时间", key: "createTime", width: 150, align: "center", ellipsis: true},
          {
            title: "产品名称",
            key: "productName",
            align: "center",
            ellipsis: true,
            minWidth: 120,
            maxWidth: 200,
            tooltip: true,
          },
          {
            title: "配置内容", key: "jsonString", align: "center", ellipsis: true, tooltip: true,
            /*render: (h, params) => {
              return h('Tooltip', {
                props: {placement: 'top', transfer: true}
              }, [
                params.row.jsonString,
                h('span', {
                  slot: 'content',
                  style: {whiteSpace: 'normal', wordBreak: 'break-all'}
                }, params.row.jsonString)
              ])
            }*/
          },
          {
            title: "操作", key: "action", align: "center", fixed: "right", width: 140,
            render: (h, params) => {
              return h("div", [
                h("Button", {
                    props: {size: "small"},
                    style: {marginRight: "5px"},
                    on: {
                      click: () => {
                        this.getDeviceConfigConfig(params.row);
                      }
                    }
                  }, "查看"
                ),
                h("Button", {
                    props: {type: "primary", size: "small"}, style: {marginRight: "5px"},
                    on: {
                      click: () => {
                        this.batchDeviceConfigRecover(params.row);
                      }
                    }
                  }, "恢复"
                ),
              ]);
            }
          }
        ],
        columns: [{type: "index", width: 60, align: "center"},
          {type: "selection", width: 60, align: "center"},
          {title: "产品名称", key: "productName", align: "center", ellipsis: true, tooltip: true,},
          {title: "设备编号", key: "deviceNo", align: "center", ellipsis: true, tooltip: true,},
          {title: "设备名称", key: "name", align: "center", ellipsis: true, tooltip: true,},
          {
            title: "接入方式", key: "linkTypeName", tooltip: true, align: "center"/*, render: (h, params) => {
              let re = params.row.linkTypeName;
              if (re === 0) {
                return h("div", "不接入网关");
              } else if (re === 1) {
                return h("div", "接入网关");
              }
            }*/
          },
          {
            title: "状态", key: "status", align: "center",
            render: (h, params) => {
              let re = params.row.status;
              if (re === 1) {
                return h("div", "未激活");
              } else if (re === 2) {
                return h("div", "离线");
              } else if (re === 3) {
                return h("div", "在线");
              }
            }
          },
          {
            title: "最后上线时间", key: "lastTime", width: 150, align: "center",
            render: (h, params) => {
              let re = params.row.lastTime;
              if (re == null || re == undefined || re == '') {
                return h("div", '');
              }
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            }
          },
          {title: "备注", key: "comments", align: "center", ellipsis: true, tooltip: true,},
          {
            title: "操作", key: "action", align: "center", fixed: "right",
            render: (h, params) => {
              return h("div", [
                h("Button", {
                    props: {type: "primary", size: "small"},
                    style: {marginRight: "5px"},
                    on: {
                      click: () => {
                        this.deviceConfigHistory(params.row);
                      }
                    }
                  }, "配置记录"
                ),
              ]);
            }
          }
        ],

        //单个设备编辑的配置履历
        columnsRecord: [{type: "index", width: 60, align: "center"},
          /*{type: "selection", width: 60, align: "center"},*/
          {title: "操作时间", key: "createTime", align: "center", ellipsis: true},
          {title: "产品名称", key: "productName", align: "center", ellipsis: true, tooltip: true,},
          {title: "设备名称", key: "deviceName", align: "center", ellipsis: true, tooltip: true,},
          {title: "设备编号", key: "deviceNo", align: "center", ellipsis: true, tooltip: true,},
          {
            title: "配置内容", key: "jsonString", align: "center", ellipsis: true, tooltip: true,
            /*render: (h, params) => {
              return h('Tooltip', {
                props: {placement: 'top', transfer: true}
              }, [
                params.row.jsonString,
                h('span', {
                  slot: 'content',
                  style: {whiteSpace: 'normal', wordBreak: 'break-all'}
                }, params.row.jsonString)
              ])
            }*/
          },
          {
            title: "操作", key: "action", align: "center", fixed: "right", width: 100,
            render: (h, params) => {
              return h("div", [
                h("Button", {
                    props: {type: "primary", size: "small"},
                    style: {marginRight: "5px"},
                    on: {
                      click: () => {
                        this.deviceConfigRecover(params.row);
                      }
                    }
                  }, "恢复"
                ),
              ]);
            }
          }
        ],
        //受影响的设备列表
        columnsInfluencedDevice: [{type: "index", width: 60, align: "center"},
          /*{type: "selection", width: 60, align: "center"},*/
          {title: "操作时间", key: "createTime", align: "center", ellipsis: true, tooltip: true,},
          {title: "产品名称", key: "productName", align: "center", ellipsis: true, tooltip: true,},
          {title: "设备名称", key: "deviceName", align: "center", ellipsis: true, tooltip: true,},
          {title: "设备编号", key: "deviceNo", align: "center", ellipsis: true, tooltip: true,},
        ],

      };
    },
    methods: {
      init() {
        this.getProductList();
        this.pageNumber = 1;
        this.getDeviceList();

      },
      serachGetDeviceList(){
        this.pageNumber = 1;
        this.getDeviceList();
      },
      changePage(v) {
        this.pageNumber = v;
        this.getDeviceList();
        this.clearSelectAll();
      },
      changePageRecord(v) {
        this.pageNumberRecord = v;
        this.deviceConfigHistory();
        this.clearSelectAllRecord();
      },
      changePageSize(v) {
        this.pageSize = v;
        this.getDeviceList();
      },
      changePageSizeRecord(v) {
        this.pageSizeRecord = v;
        this.deviceConfigHistory();
      },
      getDeviceListChange(){

      },

      getDeviceList() {
        let params = {
          productId: this.productId,
          keyWord: this.keyWord,
          pageNum: this.pageNumber,
          pageSize: this.pageSize
        };
        // 多条件搜索列表
        this.loading = true;
        this.postBusRequest('/remote/selectDevicePageList', params).then(res => {
          this.loading = false;
          this.total = res.total;
          this.data = res.list;
        });

      },
      getProductList() {
        this.postBusRequest('/upgrade/getProductList').then(res => {
          this.productList = res.data;
        });
      },
      clearSelectAll() {
        this.$refs.table.selectAll(false);
      },
      clearSelectAllRecord() {
        this.$refs.tableRecord.selectAll(false);
      },

      changeSelect(e) {
        this.selectList = e;
        this.selectCount = e.length;
      },
      changeSelectRecord(e) {
        this.selectListRecord = e;
        this.selectCountRecord = e.length;
      },
      buttonRemoteConfig() {
        if (this.productId === "" || typeof (this.productId) === 'undefined') {
          this.$Message.warning("请先选择产品");
          return;
        }
        if (this.selectList.length === 0) {
          this.$Message.warning("未选择任何设备");
          return;
        }

        for (let i = 0; i < this.selectList.length; i++) {
          if (this.selectList[i].status === 1) {
            this.$Message.warning("选择的设备中有未激活设备");
            return;
          }
        }
        this.configEditModalVisible = true;
      },
      configEditModalCancel() {
        this.configEditModalVisible = false;
      },
      changeSort(e) {
        this.sortColumn = e.key;
        this.sortType = e.order;
        if (e.order === "normal") {
          this.sortType = "";
        }
        this.getDeviceList();
      },
      changeSortRecord(e) {
        this.sortColumnRecord = e.key;
        this.sortTypeRecord = e.order;
        if (e.order === "normal") {
          this.sortTypeRecord = "";
        }
        this.getDeviceList();
      },
      /**
       * json 编辑框内容
       * */
      onJsonChange(value) {
        //console.log('value:', value)
      },
      /**
       * 批量更新提交
       */
      batchUpdate() {
        if (this.$refs.jsonDataRef.error) {
          this.$Message.warning("编辑的内容不符合JSON格式");
          return;
        }
        if (JSON.stringify(this.jsonData) === "{}") {
          this.$Message.warning("编辑的内容无效");
          return;
        }
        this.$Modal.confirm({
          title: "确认",
          content: "您确认将当前编辑的配置更新于所选的 " + this.selectCount + " 个设备中?",
          onOk: () => {
            let parms = {
              productId: this.productId,
              jsonString: JSON.stringify(this.jsonData),
              deviceList: this.selectList
            };
            this.postBusRequest('/remote/batchUpdateDeviceConfig', parms).then(res => {
              this.$Message.success(res.msg);
              this.configEditModalVisible = false;
              this.selectList = [];
              this.getDeviceList();
            });
          }
        });
      },
      //配置记录
      deviceConfigHistory(row) {
        let params = {
          body: {
            deviceId: row.id,
            productId: row.productId
          },
          pageNum: this.pageNumberRecord,
          pageSize: this.pageSizeRecord
        };
        // 多条件搜索列表
        this.loading = true;
        this.postBusRequest('/remote/getDeviceConfigHistory', params).then(res => {
          this.loading = false;
          this.totalRecord = res.total;
          this.dataRecord = res.list;
        });
        this.configRecordModalVisible = true;
      },
      //单个设备配置恢复
      deviceConfigRecover(row) {
        this.$Modal.confirm({
          title: "确认",
          content: "您确认将该设备恢复至此版本?",
          onOk: () => {
            let rowNew=Object.assign({},row,{createTime : ""});
            let deviceList = [];
            deviceList.push(rowNew);
            let params = {
              productId: row.productId,
              jsonString: row.jsonString,
              deviceList: deviceList
            };
            this.postBusRequest('/remote/batchUpdateDeviceConfig', params).then(res => {
              this.$Message.success(res.msg);
             /* this.selectList = [];
              this.getDeviceList();*/
            });
          }
        });
      },

      //----------------------------------------------批量配置恢复----------------------------------------
      changePageConfig(v) {
        this.pageNumberConfig = v;
        this.getConfigHistory();
        this.clearSelectAllConfig();
      },
      changePageSizeConfig(v) {
        this.pageSizeConfig = v;
        this.getConfigHistory();
      },
      clearSelectAllConfig() {
        this.$refs.tableConfig.selectAll(false);
      },
      changeSelectConfig(e) {
        this.selectListConfig = e;
        this.selectCountConfig = e.length;
      },
      changeSortConfig(e) {
        this.sortColumnConfig = e.key;
        this.sortTypeConfig = e.order;
        if (e.order === "normal") {
          this.sortTypeConfig = "";
        }
      },
      changePageInfluencedDevice(v) {
        this.pageNumberConfig = v;
        this.getConfigHistory();
        this.clearSelectAllConfig();
      },
      changePageSizeInfluencedDevice(v) {
        this.pageSizeConfig = v;
        this.getConfigHistory();
      },
      clearSelectAllInfluencedDevice() {
        this.$refs.tableInfluencedDevice.selectAll(false);
      },
      changeSelectInfluencedDevice(e) {
        this.selectListConfig = e;
        this.selectCountConfig = e.length;
      },
      changeSortInfluencedDevice(e) {
        this.sortColumnConfig = e.key;
        this.sortTypeConfig = e.order;
        if (e.order === "normal") {
          this.sortTypeConfig = "";
        }
      },
      //查询所有的配置履历
      getConfigHistory() {
        let params = {
          pageNum: this.pageNumberConfig,
          pageSize: this.pageSizeConfig
        };
        // 多条件搜索列表
        this.loading = true;
        this.postBusRequest('/remoteConfig/selectConfigPageList', params).then(res => {
          this.loading = false;
          this.totalConfig = res.total;
          this.dataConfig = res.list;
        });

      },
      //查看受影响的设备
      getDeviceConfigConfig(row) {
        let params = {
          id: row.id,
          pageNum: this.pageNumberInfluencedDevice,
          pageSize: this.pageSizeInfluencedDevice
        };
        // 多条件搜索列表
        this.loading = true;
        this.postBusRequest('/remote/getDeviceRemoteConfigRecordList', params).then(res => {
          this.loading = false;
          this.totalInfluencedDevice = res.total;
          this.dataInfluencedDevice = res.list;
          this.influencedDeviceModalVisible = true;
        });
      },
      //批量恢复
      batchDeviceConfigRecover(row) {
        this.$Modal.confirm({
          title: "确认",
          content: "您确认将受影响的设备恢复至此版本?",
          onOk: () => {
            this.postBusRequest('/remote/batchRecoverDeviceConfig', {id: row.id}).then(res => {
              this.$Message.success(res.msg);
              this.getConfigHistory();
            });
          }
        });
      },
      //切换tab 时
      tabDataTables() {
        if (this.tabModelValue === "name1") {
          this.init();
        } else {
          this.getConfigHistory();
        }
      },
    },
    mounted() {
      this.init();
    }
  };
</script>
