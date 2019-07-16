<style lang="less">
  @import "deptManage.less";
</style>
<template>
  <div class="search">
    <Row>
      <Col>
      <Card>
         <Form inline :label-width="70" class="search-form">
           <Form-item label="部门管理">
             <Input type="text" v-model="searchCondition" clearable placeholder="请输入部门名称/社会信用代码" style="width: 200px"/>
           </Form-item>
           <Form-item style="margin-left:-35px;">
             <Button @click="searchDept"  type="primary" icon="search">搜索</Button>
             <Button @click="handleReset" type="ghost" >重置</Button>
           </Form-item>
         </Form>
        <Row class="operation">
          <Button @click="addDeptRelation" type="primary" icon="trash-a">新增</Button>
        </Row>
<!--        <Row>
          <Alert show-icon>
            已选择 <span class="select-count">{{selectCount}}</span> 项
            <a class="select-clear" @click="clearSelectAll">清空</a>
          </Alert>
        </Row>-->
        <Row class="margin-top-10 searchable-table-con1">
          <!--<Table :loading="loading" border :columns="columns" :data="data" ref="table" sortable="custom"-->
          <!--@on-sort-change="changeSort" @on-selection-change="changeSelect"   @on-expand="emitExpand"></Table>-->
          <div id="app">
            <tree-grid
              :items='data'
              :columns='columns'
            ></tree-grid>
          </div>
        </Row>
<!--        <Row type="flex" justify="end" class="code-row-bg page">

          <Page :current="this.pageNumber" :total="total" :page-size="this.pageSize" @on-change="changePage"
                @on-page-size-change="changePageSize" :page-size-opts="[10,20,50,100]" size="small" show-total
                show-elevator show-sizer></Page>
        </Row>-->


      </Card>
      </Col>
    </Row>

    <Modal :title="deptRelationModal.title" v-model="deptRelationModal.visible" :mask-closable='false' :width="500">
      <Form ref="deptRelationModalForm" :model="deptRelationModalForm" :label-width="80" :rules="deptRelationModalFormValidate">
        <FormItem label="父部门" prop="parentDeptName">
          <Input v-model="deptRelationModalForm.parentDeptName" placeholder="请选择父部门" @on-focus="getParentTree" :readonly="true"/>
          <Card style="width:388px" v-show="deptRelationModalForm.parentTreeVisibility" >
              <Tree :data="deptRelationModalForm.parentTree" @on-check-change="changeSelect"
                                   @on-select-change="selectTree" ></Tree>
              <Spin size="large" fix v-if="loading"></Spin>
          </Card>
        </FormItem>
        <FormItem label="子部门" prop="childDeptName">
          <Input v-model="deptRelationModalForm.childDeptName" placeholder="请选择子部门" @on-focus="getChild" :readonly="true"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="deptRelationModalCancel">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="deptRelationSubmit">提交</Button>
      </div>
    </Modal>

    <Modal :title="childModal.title" v-model="childModal.visible" :mask-closable='false' :width="850">
      <Row :gutter="16">
        <Col span="7">
           <Input v-model="childModal.searchCondition" placeholder="请输入部门名称/社会信用代码"/>
        </Col>
        <Col span="8">
           <Button @click="searchChildren" type="primary" icon="search">搜索</Button>
           <Button @click="childModalReset" type="ghost">重置</Button>
           <Button @click="addDept" type="primary" icon="add">新建部门</Button>
        </Col>
      </Row>
      <Row style="margin-top: 24px">
        <Table :loading="loading" border :columns="childModal.columns" :data="childModal.data" ref="table"
               sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect"></Table>
      </Row>
    </Modal>

    <Modal :title="deptModal.title" v-model="deptModal.visible" :mask-closable='false' :width="500">
      <Form ref="deptModalForm" :model="deptModalForm" :label-width="80" :rules="deptModalFormValidate">
        <FormItem label="简称" prop="simpleName">
          <Input v-model="deptModalForm.simpleName" placeholder="简称"/>
        </FormItem>
        <FormItem label="全称" prop="fullName">
          <Input v-model="deptModalForm.fullName" placeholder="全称"/>
        </FormItem>
        <FormItem label="类型" prop="type">
          <RadioGroup v-model="deptModalForm.type" :disabled="updateDisabled">
            <Radio :label="0" :disabled="updateDisabled"> 
              <Icon type="ios-list-outline"></Icon>
              <span>部门</span>
            </Radio>
            <Radio :label="1" :disabled="updateDisabled">
              <Icon type="log-in"></Icon>
              <span>公司</span>
            </Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="超级管理员" prop="superAdmin">
          <RadioGroup v-model="deptModalForm.superAdmin" >
            <Radio :label="0" :disabled="updateDisabled">
              <Icon type="ios-list-outline"></Icon>
              <span>否</span>
            </Radio>
            <Radio :label="1" :disabled="updateDisabled">
              <Icon type="log-in"></Icon>
              <span>是</span>
            </Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="信用代码" prop="socialCreditNumber">
          <Input v-model="deptModalForm.socialCreditNumber" :disabled="updateDisabled" placeholder="请输入社会信用代码"/>
        </FormItem>
        <FormItem label="排序" prop="num">
          <Input v-model="deptModalForm.num" :disabled="updateDisabled" placeholder="排序"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="deptModalCancel">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="add">提交</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  import {getStore} from "../../../utils/storage";
  import TreeGrid from '../../my-components/tree-grid/treeGrid.vue';
  const user = JSON.parse(getStore("userInfo"));

  export default {
    name: "dept-manage",
    data() {
      return {
        loading: true,
        updateDisabled:false,
        searchDictName: "",
        selectList: [],
        selectCount: 0,
        searchKey: "",
        sortColumn: "createTime",
        searchCondition:"",
        sortType: "desc",
        deptRelationModal: {
          title: "",
          visible: false,
        },
        deptRelationModalFormValidate: {
          parentDeptName: [{required: true, message: "父部门不能为空", trigger: "blur"}],
          childDeptName: [{required: true, message: "子部门不能为空", trigger: "blur"}]
        },
        deptRelationModalForm: {
          id: "",
          parentDeptId: "",
          childDeptId: "",
          parentTree: [],
          parentTreeVisibility: false,
          parentDeptName: "",
          childDeptName: ""
        },
        columns: [
          {
            type: "selection",
            width: 60,
            align: "center"
          },
          {
            title: "简称",
            key: "simpleName",
            align: "center",
            width: 200,
            sortable: true
          },
          {
            title: "id",
            key: "id",
            width: 200,
            align: "center",
            sortable: true
          },
          {
            title: "全称",
            key: "fullName",
            width: 300,
            align: "center",
            sortable: true,
          },
          {
            title: "类型",
            key: "type",
            width: 200,
            align: "center",
            sortable: true,
            render: (row) => {
              return row.type === 1 ? "公司" : "部门";
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
            render:(row) => {
              return row.status === 1?"禁用":"启用";
            }
          },
          {
            title: "超级管理员",
            key: "superAdmin",
            width: 200,
            align: "center",
            sortable: true,
            render: (row) => {
              return row.superAdmin === 1 ? "是" : "否";
            }
          },
          {
            title: "操作",
            width: 100,
            type: 'action',
            align: "center",
            fixed :"right",
            actions: [{
              type: 'primary',
              text: '查看',
              actionClick: (item) => {
                this.$router.push({name: "deptRelation", params: {parentDeptId: item.id}});
              },
            },
            {
              type: 'primary',
              text: '编辑',
              actionClick: (row) => {
                this.updateCompany(row)
              },
            }]
          }
        ],
        data: [],
        pageNumber: 1,
        pageSize: 10,
        total: 0,
        startDate: "",
        endDate: "",
        modalTitle: "",
        submitLoading: false,
        childModal: {
          title: "",
          visible: false,
          searchCondition: "",
          columns: [
            {
              title: "简称",
              key: "simpleName",
              width: 150,
              align: "center",
              sortable: true
            },
            {
              title: "全称",
              key: "fullName",
              width: 250,
              align: "center",
              sortable: true,
            },
            {
              title: "类型",
              key: "type",
              width: 150,
              align: "center",
              sortable: true,
              render:(h, params) => {
                return h("div", params.row.type === 1 ? "公司" : "部门");
              }
            },
            {
              title: "社会信用代码",
              key: "socialCreditNumber",
              width: 150,
              align: "center",
              sortable: true,
              
            },
            {
              title: "操作",
              key: "action",
              align: "center",

              width: 150,
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
                          this.deptRelationModalForm.childDeptId = params.row.id;
                          this.deptRelationModalForm.childDeptName = params.row.simpleName;
                          this.childModal.visible = false;
                        }
                      }
                    },
                    "关联"
                  )
                ]);
              }
            }
          ],
          data: []
        },
        deptModal: {
          title: "",
          visible: false,
        },
        deptModalFormValidate: {
          simpleName: [{required: true, message: "简称不能为空", trigger: "blur"}],
          fullName: [{required: true, message: "全称不能为空", trigger: "blur"}]
        },
        deptModalForm: {
          id: "",
          simpleName: "",
          fullName: "",
          type: 0,
          socialCreditNumber: "",
          superAdmin: 0,
          num: 0,
          createDeptId: ""
        }
      };
    },
    components: {
      TreeGrid
    },
    methods: {
              //打开编辑部门模态框
      updateCompany(pap) {
        this.deptModal.title = '编辑部门';
        this.deptModal.visible = true;
        this.updateDisabled =true;
        this.deptModalForm = {
          id: pap.id,
          simpleName: pap.simpleName,
          fullName: pap.fullName,
          type: pap.type,
          socialCreditNumber: pap.socialCreditNumber,
          superAdmin: pap.superAdmin,
          num: pap.num,
          createDeptId: pap.createDeptId,
          deptCode: pap.deptCode,
          page:"company"
        };
      },
      init() {
        this.getDeptRelationList();
      },
      changePage(v) {
        this.pageNumber = v;
        this.getDeptRelationList();
        this.clearSelectAll();
      },
      changePageSize(v) {
        this.pageSize = v;
        this.getDeptRelationList();
      },
      selectDateRange(v) {
        if (v) {
          this.startDate = v[0];
          this.endDate = v[1];
        }
      },
      getDeptRelationList() {
        this.loading = true;
        this.getRequest("/deptRelation/getChildrenByParentDeptId", {parentDeptId: user.deptId}).then(res => {
          if (res.success === true) {
            this.data = res.result;
            this.loading = false;
            console.log(this.data);
          }
        })
      },
      handleReset() {
        this.searchDictName = "";
        this.getDeptRelationList();
      },
      addDeptRelation() {
        this.deptRelationModal.title = '添加部门关系';
        this.deptRelationModalForm = {
          id: "",
          parentDeptId: "",
          childDeptId: "",
          parentTree: [],
          parentTreeVisibility: false,
          parentDeptName: "",
          childDeptName: ""
        };
        this.deptRelationModal.visible = true;
      },
      deptRelationModalCancel() {
        this.deptRelationModal.visible = false;
      },
      deptModalCancel() {
        this.deptModal.visible = false;
      },
      deptRelationSubmit() {
        if (this.deptRelationModalForm.id === "") {
          //新增
          this.addDeptRelationSubmit();
        } else {
          //修改
          this.editDeptRelationSubmit();
        }
      },
      //获取父菜单
      getParentTree() {
        this.getRequest("/deptRelation/getChildrenByParentDeptId", {parentDeptId: user.deptId}).then(res => {
          if (res.success === true) {
            this.deptRelationModalForm.parentTree = res.result;
            this.deptRelationModalForm.parentTreeVisibility = true;
          }
        })
      },
      //打开获取子公司模态框
      getChild() {
        this.childModal.title = "选择子部门";
        this.childModal.searchCondition = "";
        this.getChildren();
        this.childModal.visible = true;
      },
      getChildren() {
        let params = {
          createDeptId: user.deptId,
          searchCondition: this.childModal.searchCondition
        };
        this.getRequest("/dept/selectDeptsByCreateDeptId", params).then(res => {
          if (res.success === true) {
            this.childModal.data = res.result;
          }
        })
      },
      searchChildren() {
        this.getChildren();
      },
      //打开新增部门模态框
      addDept() {
        this.deptModal.title = '添加部门';
        this.deptModal.visible = true;
        this.deptModalForm = {
          id: "",
          simpleName: "",
          fullName: "",
          type: 0,
          socialCreditNumber: "",
          superAdmin: 0,
          num: 0,
          createDeptId: ""
        };
      },
      add(){
        if(this.deptModal.title == "编辑部门"){
          
           this.updateCompanySubmit()
        }
        if(this.deptModal.title == "添加部门"){
          this.addDeptSubmit()
        }},
      //新增部门
      addDeptSubmit() {
        this.deptModalForm.createDeptId = user.deptId;
        this.$refs.deptModalForm.validate(valid => {
          if (valid) {
            this.postRequest("/dept/insert", this.deptModalForm).then(res => {
              if (res.success === true) {
                this.deptModal.visible = false;
                this.getChildren();
              }
            })
          }
        });
      },
         //编辑部门
      updateCompanySubmit() {
        console.log(user);
        let param={
          
           id : this.deptModalForm.id,
           type :this.deptModalForm.type,
           simpleName: this.deptModalForm.simpleName,
           socialCreditNumber: this.deptModalForm.socialCreditNumber,
           fullName: this.deptModalForm.fullName,
           updateUser : user.id
        }
        this.$refs.deptModalForm.validate(valid => {
          if (valid) {
            console.log(param);
            this.postRequest("/deptRelation/update",param).then(res => {
              if (res.success === true) {
                this.$Message.success("编辑成功");
                this.deptModal.visible = false;
                this.init();
              }
            })
          }
        });
      },
      //选择子部门重置
      childModalReset() {
        this.childModal.searchCondition = "";
        this.getChildren();
      },
      addDeptRelationSubmit() {
        this.$refs.deptRelationModalForm.validate(valid => {
          if (valid) {
            this.postRequest("/deptRelation/insert", this.deptRelationModalForm).then(res => {
              if (res.success === true) {
                this.$Message.success("新增成功");
                this.deptRelationModal.visible = false;
                this.init();
              }
            })
          }
        });
      },
      selectTree(nodeList) {
        console.log(nodeList);
        let node = nodeList[0];
        let type = node.type;
        if (type !== 1) {
          this.$Message.warning("只有类型为公司的部门才能添加子部门");
          return;
        }
        this.deptRelationModalForm.parentDeptName = node.simpleName;
        this.deptRelationModalForm.parentDeptId = node.id;
        this.deptRelationModalForm.parentTreeVisibility = false;
      },
      //部门搜索
      searchDept(){
        this.getRequest("/dept/searchDept", {searchCondition:this.searchCondition,parentDeptId:user.deptId}).then(res => {
          if (res.success === true) {
            this.data = res.result;
          }
        })
      },
      emitExpand(row, status) {
        this.$emit('on-expand', row, status)
      },
      clearSelectAll() {
        this.$refs.table.selectAll(false);
      },
      changeSelect(e) {
        this.selectList = e;
        this.selectCount = e.length;
        console.log("changeSelect")
      },
      changeSort(e) {
        this.sortColumn = e.key;
        this.sortType = e.order;
        if (e.order === "normal") {
          this.sortType = "";
        }
        this.getDeptRelationList();
      }
    },
    mounted() {
      this.init();
    }
  };
</script>
