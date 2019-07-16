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
              <FormItem style="margin-left:-70px;">
                <Button @click="addRoot" type="primary">新增根节点</Button>
              </FormItem>
            </Form>
          </Row>
          <Row class="margin-top-10 searchable-table-con1">
            <tree-table
              expand-key="name"
              :expand-type="false"
              :selectable="false"
              :columns="columns"
              :data="data"
              border
            >
              <template slot="action" slot-scope="scope">
                <Button
                  @click="addChild(scope.row)"
                  size="small"
                  style="margin-right:5px"
                  type="primary"
                >新增下级节点</Button>
                <Button
                  @click="edit(scope.row)"
                  size="small"
                  style="margin-right:5px"
                  type="success"
                >编辑</Button>
                <Button
                  @click="remove(scope.row.id)"
                  size="small"
                  style="margin-right:5px"
                  type="error"
                >删除</Button>
              </template>
            </tree-table>
          </Row>
        </Card>
      </Col>
    </Row>
    <Modal :title="modalTitle" v-model="addFormVisible" :mask-closable="false" :width="500">
      <Form ref="addForm" :model="addForm" :label-width="70" :rules="addFormValidate">
        <FormItem label="产品类型" prop="name">
          <Input v-model="addForm.name" :maxlength="20" :disabled="isDis"/>
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
  </div>
</template>
<script>
import { getStore } from "@/utils/storage.js";
import TreeTable from "tree-table-vue";
export default {
  name: "type",
  components: {
    TreeTable
  },
  data() {
    return {
      loading: true,
      selectCount: 0,
      selectList: [],
      addFormVisible: false,
      isDis: false,
      modalTitle: "",
      searchForm: {
        keyword: ""
      },
      addForm: {
        name: "",
        comments: ""
      },
      addFormValidate: {
        name: [{ required: true, message: "请输入产品类型", trigger: "blur" }]
      },
      submitLoading: false,
      columns: [
        {
          title: "产品类型",
          key: "name",
          tooltip: true,
          width: 180
        },
        {
          title: "备注",
          key: "comments",
          tooltip: true,
          width: 180,
          tooltip: true
        },
        {
          title: "创建人",
          key: "createUserName",
          tooltip: true,
          width: 180
        },
        {
          title: "创建时间",
          key: "createTime",
          width: 180
        },
        {
          title: "操作",
          key: "action",
          width: 300,
          type: "template",
          template: "action",
          fixed: "right"
        }
      ],
      data: []
    };
  },
  methods: {
    init() {
      this.accessToken = {
        accessToken: getStore("accessToken")
      };
      this.getList();
    },
    getList() {
      let params = {
        selectColumns: ["id"],
        order: {},
        params: []
      };
      // 多条件搜索用户列表
      this.loading = true;
      this.columns = [];
      this.postBusRequest("/productType/selectProductTypeList", params).then(
        res => {
          this.loading = false;
          this.data = res.data;
          this.columns = [
            {
              title: "产品类型",
              key: "name",
              tooltip: true,
              width: 180
            },
            {
              title: "备注",
              key: "comments",
              tooltip: true,
              width: 180
            },
            {
              title: "创建人",
              key: "createUserName",
              tooltip: true,
              width: 180
            },
            {
              title: "创建时间",
              key: "createTime",
              width: 180
            },
            {
              title: "操作",
              key: "action",
              width: 300,
              type: "template",
              template: "action",
              fixed: "right"
            }
          ];
        }
      );
    },
    handleSearch() {
      this.searchForm.page = 1;
      this.searchForm.limit = 10;
      this.init();
    },
    submitDict() {
      if (this.modalTitle == "新增产品类型") {
        this.$refs.addForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "新增",
              content: "确认保存吗？",
              onOk: () => {
                this.postBusRequest("/productType/insert", this.addForm).then(
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
      if (this.modalTitle == "编辑产品类型") {
        this.$refs.addForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "编辑",
              content: "确认保存吗？",
              onOk: () => {
                this.postBusRequest("/productType/update", this.addForm).then(
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
    },
    cancelDict() {
      if (this.modalTitle == "新增产品类型") {
        this.$Modal.confirm({
          title: "取消",
          content: "新增内容将不被保存，是否确认取消？",
          onOk: () => {
            this.addFormVisible = false;
          }
        });
      } else if (this.modalTitle == "编辑产品类型") {
        this.$Modal.confirm({
          title: "取消",
          content: "修改内容将不被保存，是否确认取消？",
          onOk: () => {
            this.addFormVisible = false;
          }
        });
      } else {
        this.addFormVisible = false;
      }
    },
    addRoot() {
      this.modalTitle = "新增产品类型";
      this.$refs.addForm.resetFields();
      this.addForm.parentId = 0;
      this.addFormVisible = true;
      this.isDis = false;
    },
    addChild: function(row) {
      this.modalTitle = "新增产品类型";
      this.$refs.addForm.resetFields();
      this.addFormVisible = true;
      this.addForm.parentId = row.id;
      this.isDis = false;
    },
    edit(v) {
      this.modalTitle = "编辑产品类型";
      this.isDis = false;
      this.$refs.addForm.resetFields();
      this.postBusRequest("/productType/selectById", { id: v.id }).then(res => {
        this.addForm = res.data;
        this.addFormVisible = true;
      });
    },
    remove: function(v) {
      this.$Modal.confirm({
        title: "删除",
        content: "确认删除选中记录吗？",
        onOk: () => {
          this.postBusRequest("/productType/delete", {
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
    }
  },
  mounted() {
    this.init();
  }
};
</script>
