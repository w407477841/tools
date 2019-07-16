<style lang="less">
  @import "linkType.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
      <Card>
        <Form inline :label-width="70" class="search-form">

          <Input type="text" v-model="searchName" clearable placeholder="联网方式" style="width: 200px"/>

          <Form-item class="common-margin-left">
            <Button @click="getDictList" type="primary">搜索</Button>
          </Form-item>
          <Form-item class="common-margin-left">
            <Button @click="add" type="primary">新增</Button>
          </Form-item>
          <Form-item class="common-margin-left">
            <Button @click="deletes" type="error" :disabled="selectCount === 0?true:false ">批量删除</Button>
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

    <Modal :title="modalTitle" v-model="dictModalVisible" :mask-closable='false' :width="500">
      <Form ref="dictForm" :model="dictForm" :label-width="80" :rules="dictFormValidate">
        <FormItem label="联网方式" prop="name">
          <Input v-model="dictForm.name" placeholder="" :disabled="available" :maxlength="20"/>
        </FormItem>
        <FormItem label="备注" prop="comments">
          <Input v-model="dictForm.comments" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder=""
                 :disabled="available" :maxlength="200"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="dictModalCancel">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submit" :disabled="available">提交</Button>
      </div>
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
        selectList: [],
        selectCount: 0,
        userInfo: JSON.parse(this.cookies.get('userInfo')),
        searchKey: "",
        sortColumn: "createTime",
        sortType: "desc",
        available: false,
        columns: [
          {
            type: "selection",
            width: 60,
            align: "center"
          },
          {
            title: "联网方式",
            key: "name",
            //width: 20,
            align: "center",
            //sortable: true
            tooltip: true,
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
            title: "创建时间",
            key: "createTime",
            align: "center",
            width: 180
            //sortable: true,
            //sortType: "desc",
          },
          {
            title: "创建人",
            key: "createUserName",
            //width: 20,
            align: "center",
            tooltip: true,
            /*sortable: true,*/
          },
          {
            title: "操作",
            key: "action",
            fixed: "right",
            align: "center",
            width: 300,
            render: (h, params) => {
              return h("div", [
                h("Button", {
                    props: {
                      // type: "primary",
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.showDictDetail(params.row);
                      }
                    }
                  }, "查看"
                ),
                h(
                  "Button", {
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
                  }, "编辑"
                ),
                h(
                  "Button", {
                    props: {
                      type: "error",
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.delete(params.row.id);
                      }
                    }
                  }, "删除"
                ),

              ]);
            }
          }
        ],
        data: [],
        pageNumber: 1,
        ids: [],
        pageSize: 10,
        total: 0,
        startDate: "",
        endDate: "",
        modalTitle: "",
        dictFormValidate: {
          name: [{required: true, message: "联网类型不能为空", trigger: "blur"}],
          /*code: [{required: true, message: "字典编码不能为空", trigger: "blur"}]*/
        },
        dictForm: {
          id: "",
          name: "",
          comments: ""
        },
        dictModalVisible: false,
        submitLoading: false
      };
    },
    methods: {
      init() {
        this.pageNumber = 1;
        this.getDictList();
      },
      changePage(v) {
        this.pageNumber = v;
        this.getDictList();
        this.clearSelectAll();
      },
      changePageSize(v) {
        this.pageSize = v;
        this.getDictList();
      },
      selectDateRange(v) {
        if (v) {
          this.startDate = v[0];
          this.endDate = v[1];
        }
      },
      getDictList() {
        this.loading = true;
        let params = {
          params: [{
            name: "name",
            condition: "like",
            value: this.searchName
          }],
          order: {
            open: true,
            name: "createTime",
            isAsc: false
          },
          pageNum: this.pageNumber,
          pageSize: this.pageSize
        };
        // 多条件搜索列表
        this.loading = true;
        this.postBusRequest('/dictionary/linkType/selectPage', params).then(res => {
          this.loading = false;
          this.total = res.data.total;
          this.data = res.data.list;
        });

      },
      handleReset() {
        this.searchDictName = "";
        this.getDictList();
      },
      add() {
        this.modalType = 0;
        this.modalTitle = "添加联网类型";
        this.dictForm = {
          id: "",
          name: "",
          code: ""
        };
        this.dictModalVisible = true;
      },
      edit(row) {
        this.modalType = 0;
        this.modalTitle = "修改联网类型";
        this.postBusRequest("/dictionary/linkType/selectById", {id: row.id}).then(res => {
          if (res.code === 200) {
            this.dictForm = {
              id: res.data.id,
              name: res.data.name,
              comments: res.data.comments
            };
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
            this.selectList.forEach(function (e) {
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
        this.postBusRequest("/dictionary/linkType/delete", {ids: this.ids}).then(res => {
          if (res.code === 200) {
            this.$Message.success(res.msg);
            this.selectCount = 0;
            this.selectList = [];
            this.init();
          }
        });
      },
      dictModalCancel() {
        if (this.modalTitle === "添加联网类型") {
          this.$Modal.confirm({
            title: "取消",
            content: "新增内容将不被保存，是否确认取消？",
            onOk: () => {
              this.dictModalVisible = false;
            }
          });
        } else if (this.modalTitle === "修改联网类型") {
          this.$Modal.confirm({
            title: "取消",
            content: "修改内容将不被保存，是否确认取消？",
            onOk: () => {
              this.dictModalVisible = false;
            }
          });
        } else {
          this.dictModalVisible = false;
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
            this.dictForm.createTime = this.moment().format("YYYY-MM-DD HH:mm:ss");
            this.postBusRequest("/dictionary/linkType/insert", this.dictForm).then(res => {
              if (res.code === 200) {
                this.$Message.success(res.msg);
                this.dictModalVisible = false;
                this.init();
              } else {
                this.$Message.error(res.msg);
              }
            })
          }
        });
      },
      editSubmit() {
        this.$refs.dictForm.validate(valid => {
          if (valid) {
            this.dictForm.modifyUser = this.userInfo.id;
            this.dictForm.modifyUserName = this.userInfo.username;
            this.dictForm.modifyTime = this.moment().format("YYYY-MM-DD HH:mm:ss");
            this.postBusRequest("/dictionary/linkType/update", this.dictForm).then(res => {
              if (res.code === 200) {
                this.$Message.success(res.msg);
                this.dictModalVisible = false;
                this.init();
              } else {
                this.$Message.error(res.msg);
              }
            })
          }
        });
      },
      showDictDetail(row) {
        this.modalType = 1;
        this.modalTitle = "查看联网类型";
        this.postBusRequest("/dictionary/linkType/selectById", {id: row.id}).then(res => {
          if (res.code === 200) {
            this.dictForm = {
              id: res.data.id,
              name: res.data.name,
              comments: res.data.comments
            };
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
        this.getDictList();
      }
    },
    mounted() {
      this.init();
    }
  };
</script>
