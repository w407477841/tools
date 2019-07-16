<style lang="less">
  @import "dictManage.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
      <Card>
        <Form inline :label-width="70" class="search-form">
          <Form-item label="字典名称">
            <Input type="text" v-model="searchDictName" clearable placeholder="请输入字典名称" style="width: 200px"/>
          </Form-item>
          <Form-item style="margin-left:-35px;">
            <Button @click="getDictList"  type="primary" icon="search">搜索</Button>
            <Button @click="handleReset" type="ghost" >重置</Button>
          </Form-item>
          <Form-item style="float: right">
            <Button @click="goBack"  type="primary" icon="back">返回</Button>
          </Form-item>
        </Form>
        <Row class="operation">
          <Button @click="add" type="error" icon="trash-a">新增</Button>
          <Button @click="deletes" type="ghost" icon="refresh">批量删除</Button>
        </Row>
        <Row>
          <Alert show-icon>
            已选择 <span class="select-count">{{selectCount}}</span> 项
            <a class="select-clear" @click="clearSelectAll">清空</a>
          </Alert>
        </Row>
        <Row class="margin-top-10 searchable-table-con1">
          <Table :loading="loading" border :columns="columns" :data="data" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect"></Table>
        </Row>
        <Row type="flex" justify="end" class="code-row-bg page">

          <Page :current="this.pageNumber" :total="total" :page-size="this.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[10,20,50,100]" size="small" show-total show-elevator show-sizer></Page>
        </Row>


      </Card>
      </Col>
    </Row>

    <Modal :title="modalTitle" v-model="dictModalVisible" :mask-closable='false' :width="500">
      <Form ref="dictForm" :model="dictForm" :label-width="80" :rules="dictFormValidate">
        <FormItem label="父字典名称" prop="name">
          <Input v-model="groupName" disabled/>
        </FormItem>
        <FormItem label="字典名称" prop="name">
          <Input v-model="dictForm.name" placeholder="请输入字典名称"/>
        </FormItem>
        <FormItem label="字典编码" prop="code">
          <Input v-model="dictForm.code" placeholder="请输入字典编码"/>
        </FormItem>
        <FormItem label="字典值" prop="code">
          <Input v-model="dictForm.value" placeholder="请输入字典值"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="dictModalCancel">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submit">提交</Button>
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
        searchDictName:"",
        selectList: [],
        selectCount: 0,
        searchKey: "",
        groupCode:this.$route.params.code,
        groupName:this.$route.params.name,
        sortColumn: "createTime",
        sortType: "desc",
        columns: [
          {
            type: "selection",
            width: 60,
            align: "center"
          },
          {
            title: "父字典名称",
            key: "groupName",
            width: 200,
            align: "center",
            sortable: true
          },
          {
            title: "父字典编码",
            key: "groupCode",
            width: 200,
            align: "center",
            sortable: true,
          },  {
            title: "字典名称",
            key: "name",
            width: 200,
            align: "center",
            sortable: true
          },
          {
            title: "字典编码",
            key: "code",
            width: 200,
            align: "center",
            sortable: true,
          },
          {
            title: "值",
            key: "value",
            width: 200,
            align: "center",
            sortable: true,
          },
          {
            title: "操作",
            key: "action",
            align: "center",
             fixed :"right",
            width: 220,
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
                    on: {
                      click: () => {
                        this.delete(params.row.id);
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
        modalTitle:"" ,
        dictFormValidate:{
          name: [{ required: true, message: "字典名称不能为空", trigger: "blur" }],
          code: [{ required: true, message: "字典编码不能为空", trigger: "blur" }]
        } ,
        dictForm:{
          id:"",
          groupCode:this.groupCode,
          groupName:this.groupName,
          name: "",
          code: "",
          value:""
        },
        dictModalVisible:false,
        submitLoading:false
      };
    },
    methods: {
      init() {
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
          searchDictName:this.searchDictName ,
          groupCode: this.groupCode
        };
        this.getRequest("/dict/list", params).then(res => {
          this.loading = false;
          if (res.success === true) {
            this.data = res.result;
            this.total = res.result.length;
          }
        });
      },
      handleReset() {
        this.searchDictName = "";
        this.getDictList();
      },
      add(){
        this.modalType = 0;
        this.modalTitle = "添加字典";
        this.dictForm = {
          id:"",
          groupName:this.groupName,
          groupCode:this.groupCode,
          name: "",
          code: "",
          value:""
        };
        this.dictModalVisible = true;
      },
      edit(row){
        this.modalType = 0;
        this.modalTitle = "修改字典";
        console.log(row);
        this.getRequest("/dict/selectById",{id:row.id}).then(res => {
          if (res.success === true) {
            let data = res.result;
            this.dictForm = {
              id:data.id,
              groupName:data.groupName,
              groupCode:data.groupCode,
              name: data.name,
              code: data.code,
              value:data.value
            };
            this.dictModalVisible = true;
          }
        });
      },
      deletes(){
        if (this.selectCount <= 0) {
          this.$Message.warning("您还未选择要删除的数据");
          return;
        }
        this.$Modal.confirm({
          title: "确认删除",
          content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
          onOk: () => {
            let ids = "";
            this.selectList.forEach(function(e) {
              ids += e.id + ",";
            });
            ids = ids.substring(0, ids.length - 1);
            this.deleteRequest("/dict/deletes", { ids: ids }).then(res => {
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
      delete(ids){
        this.$Modal.confirm({
          title: "确认删除",
          content: "您确认要删除该条数据?",
          onOk: () => {
            this.deleteRequest("/dict/deletes", { ids: ids }).then(res => {
              if (res.success === true) {
                this.$Message.success("删除成功");
                this.init();
              }
            });
          }
        });
      },
      dictModalCancel(){
        this.dictModalVisible = false;
      },
      submit(){
        if(this.dictForm.id === ""){
          //新增
          this.addSubmit();
        }else{
          //修改
          this.editSubmit();
        }
      },
      addSubmit(){
        this.postRequest("/dict/insert", this.dictForm).then(res => {
          if (res.success === true) {
            this.$Message.success("新增成功");
            this.dictModalVisible = false;
            this.init();
          }
        })
      },
      editSubmit(){
        this.postRequest("/dict/update", this.dictForm).then(res => {
          if (res.success === true) {
            this.$Message.success("修改成功");
            this.dictModalVisible = false;
            this.init();
          }
        })
      },
      showDictDetail(){
        this.dictDetail.detailModalTitle = "字典详情",
          this.dictDetail.dictDetailModalVisible = true
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
        this.getDictList();
      }
    },
    mounted() {
      this.init();
    }
  };
</script>
