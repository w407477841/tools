<style lang="less">
  @import "deptRelationManage.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
      <Card>
        <Form inline :label-width="70" class="search-form">
          <Form-item label="部门名称">
            <Input type="text" v-model="searchCondition" clearable placeholder="部门名称/社会信用代码" style="width: 200px"/>
          </Form-item>
          <Form-item style="margin-left:-35px;">
            <Button @click="getChildrenListByParnetDeptId" type="primary" icon="search">搜索</Button>
            <Button @click="handleReset" type="ghost">重置</Button>
          </Form-item>
          <Form-item style="float: right">
            <Button @click="goBack"  type="primary" icon="back">返回</Button>
          </Form-item>
        </Form>
        <Row class="operation">
          <Button @click="deletes" type="ghost" icon="refresh">批量删除</Button>
        </Row>
        <Row>
          <Alert show-icon>
            已选择 <span class="select-count">{{selectCount}}</span> 项
            <a class="select-clear" @click="clearSelectAll">清空</a>
          </Alert>
        </Row>
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

  </div>
</template>

<script>
  export default {
    name: "deptRelation-manage",
    data() {
      return {
        loading: true,
        searchCondition: "",
        selectList: [],
        selectCount: 0,
        searchKey: "",
        sortColumn: "createTime",
        sortType: "desc",
        columns: [
          {
            type: "selection",
            width: 60,
            align: "center"
          },
          {
            title: "父部门名称",
            key: "parentName",
            width: 200,
            align: "center",
            sortable: true
          },
          {
            title: "子部门名称",
            key: "childName",
            width: 200,
            align: "center",
            sortable: true,
          },
          {
            title: "子部门全称",
            key: "childName",
            width: 300,
            align: "center",
            sortable: true,
          },
          {
            title: "部门类型",
            key: "type",
            width: 200,
            align: "center",
            sortable: true,
            render:(h, params) => {
              return h("div",params.row.type === 1?"公司":"部门");
            }
          },
          {
            title: "社会信用代码",
            key: "socialCreditNumber",
            width: 200,
            align: "center",
            sortable: true,
          },
          {
            title: "状态",
            key: "status",
            width: 200,
            align: "center",
            sortable: true,
            render:(h, params) => {
              return h("div",params.row.status === 1?"禁用":"启用");
            }
          },
          {
            title: "操作",
            key: "action",
            align: "center",
            fixed :"right",
            width: 200,
            render: (h, params) => {
              return h("div", [
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
                        this.enableSwitch(params.row, params.row.status === 0 ? 1 : 0);
                      }
                    }
                  },
                  params.row.status === 0 ? "禁用" : "启用"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "error",
                      size: "small"
                    },
                    on: {
                      click: () => {
                        console.log(params.row.deptRelationId);
                        this.delete(params.row.deptRelationId);
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
        pageNumber: 1,
        pageSize: 10,
        total: 0,
        startDate: "",
        endDate: "",
        modalTitle: "",
        deptRelationFormValidate: {
          name: [{required: true, message: "字典名称不能为空", trigger: "blur"}],
          code: [{required: true, message: "字典编码不能为空", trigger: "blur"}]
        },
        deptRelationForm: {
          id: "",
          name: "",
          code: ""
        },
        deptRelationModalVisible: false,
        submitLoading: false
      };
    },
    methods: {
      init() {
        this.getChildrenListByParnetDeptId();
      },
      changePage(v) {
        this.pageNumber = v;
        this.getChildrenListByParnetDeptId();
        this.clearSelectAll();
      },
      changePageSize(v) {
        this.pageSize = v;
        this.getChildrenListByParnetDeptId();
      },
      selectDateRange(v) {
        if (v) {
          this.startDate = v[0];
          this.endDate = v[1];
        }
      },
      getChildrenListByParnetDeptId() {
        this.loading = true;
        let params = {
          parentDeptId: this.$route.params.parentDeptId,
          searchCondition: this.searchCondition
        };
        this.getRequest("/deptRelation/getNextChildrenByParentDeptId", params).then(res => {
          this.loading = false;
          if (res.success === true) {
            this.data = res.result;
            this.total = res.result.length;
          }
        });
      },
      handleReset() {
        this.searchCondition = "";
        this.getChildrenListByParnetDeptId();
      },
      add() {
        this.modalType = 0;
        this.modalTitle = "添加字典";
        this.deptRelationForm = {
          id: "",
          name: "",
          code: ""
        };
        this.deptRelationModalVisible = true;
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
            let ids = "";
            this.selectList.forEach(function (e) {
              ids += e.deptRelationId + ",";
            });
            ids = ids.substring(0, ids.length - 1);
            this.deleteRequest("/deptRelation/deletes", {ids: ids}).then(res => {
              if (res.success === true) {
                this.$Message.success("删除成功");
                this.selectCount = 0;
                this.selectList = [];
                this.init();
              }
            });
          }
        });
      },
      delete(ids) {
        this.$Modal.confirm({
          title: "确认删除",
          content: "您确认要删除该条数据?",
          onOk: () => {
            this.deleteRequest("/deptRelation/deletes", {ids: ids}).then(res => {
              if (res.success === true) {
                this.$Message.success("删除成功");
                this.init();
              }
            });
          }
        });
      },
      deptRelationModalCancel() {
        this.deptRelationModalVisible = false;
      },
      //启用禁用切换
      enableSwitch(row, status) {
        console.log(row)
        let params = {
          status: status,
          statusDeptid: 1,
          id: row.deptRelationId
        };
        this.postRequest("/deptRelation/enableSwitch", params).then(res => {
          if (res.success === true) {
            this.$Message.success(status === 1 ? "禁用" : "启用" + "成功");
            this.deptRelationModalVisible = false;
            this.init();
          }
        })
      },
      goBack(){
        this.$router.go(-1);
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
        this.getChildrenListByParnetDeptId();
      }
    },
    mounted() {
      this.init();
    }
  };
</script>
