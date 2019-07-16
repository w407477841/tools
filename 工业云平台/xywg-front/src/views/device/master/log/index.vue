<style lang="less">
  @import "index.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
      <Card>
        <Form inline :label-width="70" class="search-form">

          <Input type="text" v-model="searchName" clearable placeholder="设备编号/产品类型" style="width: 200px"/>

          <Form-item class="common-margin-left">
            <Button @click="searchGetList" type="primary">搜索</Button>
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
      </Card>
      </Col>
    </Row>
    <!--设备配置履历弹出层-->
    <Modal :title="modalTitle" v-model="logDetail" :mask-closable='false' :width="600"   @on-cancel="dictModalCancel">
      <div style="text-align:left;overflow:auto;"><div>{{logDetailText}}</div></div>
      <div slot="footer"></div>
    </Modal>
  </div>
</template>

<script>
  export default {
    name: "dict-manage",
    data() {
      return {
        loading: true,
        searchName: "",
        modalTitle: "日志详情",
        logDetail: false,
        logDetailText: "",
        selectList: [],
        selectCount: 0,
        userInfo: JSON.parse(this.cookies.get('userInfo')),
        sortColumn: "createTime",
        sortType: "desc",
        columns: [
          {
            type: "selection",
            width: 60,
            align: "center"
          },
          {
            title: "产品类型",
            key: "productType",
            //width: 20,
            align: "center",
            //sortable: true
            tooltip: true,
          },
          {
            title: "产品名称",
            key: "productName",
            //width: 20,
            align: "center",
            //sortable: true
            tooltip: true,
          },
          {
            title: "产品key",
            key: "productKey",
            align: "center",
            tooltip: true,
          },
          {
            title: "设备编号",
            key: "deviceNo",
            align: "center",
            tooltip: true,
            //sortable: true,
            //sortType: "desc",
          },
          {
            title: "设备名称",
            key: "deviceName",
            align: "center",
            tooltip: true,
            //sortable: true,
            //sortType: "desc",
          },
          {
            title: "厂家",
            key: "company",
            //width: 20,
            align: "center",
            tooltip: true,
            /*sortable: true,*/
          },
          {
            title: "日志",
            key: "details",
            //width: 20,
            align: "center",
            tooltip: true,
            /*sortable: true,*/
            render: (h, params) => {
              return h("div", [
                h("div", {
                    props: {type: "primary", size: "small"},
                    style: {marginRight: "5px","text-decoration":'underline',color:'#00F'},
                    on: {
                      click: () => {
                        this.detailLook(params.row);
                      }
                    }
                  }, "查看详情"
                ),
              ]);
            }
          },
          {
            title: "日志时间",
            key: "createTime",
            //width: 20,
            align: "center",
            tooltip: true,
            width: 150,
          },
        ],
        data: [],
        pageNumber: 1,
        ids: [],
        pageSize: 10,
        total: 0,
        startDate: "",
        endDate: "",


      };
    },
    methods: {
      init() {
        this.pageNumber = 1;
        this.getLogList();
      },
      searchGetList() {
        this.pageNumber = 1;
        this.getLogList();
      },
      changePage(v) {
        this.pageNumber = v;
        this.getLogList();
        this.clearSelectAll();
      },
      changePageSize(v) {
        this.pageSize = v;
        this.getLogList();
      },
      selectDateRange(v) {
        if (v) {
          this.startDate = v[0];
          this.endDate = v[1];
        }
      },
      getLogList() {
        this.loading = true;
        let params = {
          keyWord: this.searchName,
          pageNum: this.pageNumber,
          pageSize: this.pageSize
        };
        // 多条件搜索列表
        this.loading = true;
        this.postBusRequest('/deviceLog/selectErrorLogPageList', params).then(res => {
          this.loading = false;
          this.total = res.total;
          this.data = res.list;
        });

      },
      detailLook(row) {
        this.logDetail = true;
        this.logDetailText = row.details +"";
      },
      dictModalCancel() {

      },
      handleReset() {
        this.getLogList();
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
        this.getLogList();
      }
    },
    mounted() {
      this.init();
    }
  };
</script>
