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
                        <Table :loading="loading" border :columns="columns" :data="data" @on-selection-change="showSelect" ref="table"></Table>
                    </Row>
                    <Row type="flex" justify="end" class="code-row-bg page">
                        <Page :current="this.pageNum" :total="total" :page-size="this.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[10,20,50,100]" size="small" show-total show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>
        <Modal :title="modalTitle" v-model="addFormVisible" :mask-closable='false' :width="550">
            <Form ref="addForm" :model="addForm" :label-width="100" :rules="addFormValidate">
                <FormItem  label="应用名称" prop="appName">
                    <Input v-model="addForm.appName" :maxlength="128" :disabled="isDis"/>
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
        <Modal :title="modalTitle" v-model="editFormVisible" :mask-closable='false' :width="550">
            <Form ref="editForm" :model="editForm" :label-width="100" :rules="editFormValidate">
                <FormItem  label="应用名称" prop="appName">
                    <Input v-model="editForm.appName" :maxlength="128" :disabled="isDis"/>
                </FormItem>
                <FormItem  label="应用标识符" prop="appKey">
                    <span>{{editForm.appKey}}</span><Button style="float : right" v-clipboard:copy="editForm.appKey">复制</Button>
                </FormItem>
                <FormItem  label="授权密钥" prop="accessSecret">
                    <span>{{editForm.accessSecret}}</span><Button style="float : right" v-clipboard:copy="editForm.accessSecret">复制</Button>
                </FormItem>
                <FormItem label="备注信息" prop="comments">
                    <Input v-model="editForm.comments" type="textarea" :maxlength="200" :disabled="isDis"/>
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
    import {getStore} from '@/utils/storage.js';
    export default {
        name: "operate",
        data() {
            return {
                loading: true,
                selectCount: 0,
                total: 0,
                selectList: [],
                addFormVisible: false,
                editFormVisible: false,
                isDis : false,
                modalTitle: "",
                pageNum:1,
                pageSize:10,
                searchForm: {
                    keyword : ''
                },
                addForm: {
                    name: '',
                    comments: ''
                },
                addFormValidate: {
                    appName: [
                        { required: true, message: "请输入应用名称", trigger: "blur" }
                    ]
                },
                editForm: {
                    name: '',
                    comments: ''
                },
                editFormValidate: {
                    appName: [
                        { required: true, message: "请输入应用名称", trigger: "blur" }
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
                        title: "应用名称",
                        key: "appName",
                        tooltip:true,
                        align: "center",
                        width: 180
                    },
                    {
                        title: "应用标识符",
                        key: "appKey",
                        align: "center",
                        tooltip:true,
                        width: 180
                    },
                    {
                        title: "备注",
                        key: "comments",
                        align: "center",
                        tooltip:true,
                        minWidth: 180,
                        maxWidth: 1000
                    },
                    {
                        title: "创建人",
                        key: "createUserName",
                        tooltip:true,
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
                        fixed :"right",
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
            //获取列表
            getList() {
                let params = {
                    selectColumns : ['id','app_name as appName','app_key as appKey','access_secret as accessSecret','comments','create_time as createTime','create_user as createUser','create_user_name as createUserName'],
                    order : {
                        open : true,
                        name : 'create_time',
                        isAsc : false
                    },
                    params : [{
                        name : 'app_name',
                        condition : 'like',
                        value : this.searchForm.keyword
                    }],
                    pageNum : this.pageNum,
                    pageSize : this.pageSize
                }
                this.loading = true;
                this.columns = [];
                this.postBusRequest('/app/selectFilterPage',params).then(res => {
                    this.loading = false;
                    this.data = res.data.list;
                    this.total = res.data.total;
                    this.columns = [
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
                            title: "应用名称",
                            key: "appName",
                            tooltip:true,
                            align: "center",
                            width: 180
                        },
                        {
                            title: "应用标识符",
                            key: "appKey",
                            align: "center",
                            tooltip:true,
                            width: 180
                        },
                        {
                            title: "备注",
                            key: "comments",
                            align: "center",
                            tooltip:true,
                            minWidth: 180,
                            maxWidth: 1000
                        },
                        {
                            title: "创建人",
                            key: "createUserName",
                            tooltip:true,
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
                            fixed :"right",
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
                    ]
                    this.clearSelect();
                });
            },
            //模糊查询
            handleSearch() {
                this.pageNum = 1;
                this.pageSize = 10;
                this.init();
            },
            //提交
            submitDict(){
                if( this.modalTitle == "新增应用"){
                    this.$refs.addForm.validate(valid => {
                        if (valid) {
                            this.$Modal.confirm({
                                title: "新增",
                                content: "确认保存吗？",
                                onOk: () => {
                                    this.postBusRequest('/app/insert',this.addForm).then(res => {
                                        if (res.code == 200) {
                                            this.$Message.success(res.msg);
                                            this.addFormVisible = false;
                                            this.init();
                                        }else {
                                            this.$Message.error(res.msg);
                                        }
                                    })
                                }
                            });
                        }
                    });
                }
                if( this.modalTitle == "编辑应用"){
                    this.$refs.editForm.validate(valid => {
                        if (valid) {
                            this.$Modal.confirm({
                                title: "编辑",
                                content: "确认保存吗？",
                                onOk: () => {
                                    this.postBusRequest('/app/update',this.editForm).then(res => {   
                                        if (res.code == 200) {
                                            this.$Message.success(res.msg);
                                            this.editFormVisible = false;
                                            this.init();
                                        }else {
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
                if(this.modalTitle == "新增应用") {
                    this.$Modal.confirm({
                        title: "取消",
                        content: "新增内容将不被保存，是否确认取消？",
                        onOk: () => {
                            this.addFormVisible = false;
                        }
                    });
                }else if(this.modalTitle == "编辑应用") {
                    this.$Modal.confirm({
                        title: "取消",
                        content: "修改内容将不被保存，是否确认取消？",
                        onOk: () => {
                            this.editFormVisible = false;
                        }
                    });
                }else {
                    this.editFormVisible = false;
                }
            },
            //新增打开弹窗
            addRoot() {
                this.modalTitle = "新增应用";
                this.$refs.addForm.resetFields();
                this.addFormVisible = true;
                this.isDis = false;
            },
            //编辑打开弹窗
            edit(v) {
                this.modalTitle = "编辑应用";
                this.isDis = false;
                this.$refs.editForm.resetFields();
                this.postBusRequest('/app/selectById',{ id: v.id }).then(res => {
                    this.editForm = res.data;
                    this.editFormVisible = true;
                });
            },
            //单条删除
            remove : function(v) {
                this.$Modal.confirm({
                    title: "删除",
                    content: "确认删除选中记录吗？",
                    onOk: () => {
                        this.postBusRequest('/app/delete',{ ids: v.toString().split(",") }).then(res => {
                            if (res.code == 200) {
                                this.$Message.success(res.msg);
                                this.init();
                            }else {
                                this.$Message.error(res.msg);
                            }
                        });
                    }
                });
            },
            //查看打开弹窗
            check(v) {
                this.modalTitle = "查看应用";
                this.isDis = true;
                this.$refs.editForm.resetFields();
                this.postBusRequest('/app/selectById',{ id: v.id }).then(res => {
                    this.editForm = res.data;
                    this.editFormVisible = true;
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
            //批量删除
            delAll : function() {
                if (this.selectCount <= 0) {
                    this.$Message.warning("您还未选择要删除的数据");
                    return;
                }
                this.$Modal.confirm({
                    title: "删除",
                    content: "确认删除选中记录吗？",
                    onOk: () => {
                        this.postBusRequest("/app/delete", { ids: this.selectList.map(item => item.id) }).then(res => {
                            if (res.code == 200) {
                                this.$Message.success(res.msg);
                                this.init();
                            }else {
                                this.$Message.error(res.msg);
                            }
                        });
                    }
                });
            },
            clearSelect : function() {
                this.selectCount = 0;
                this.selectList = [];
            }
        },
        mounted() {
            this.init();
        }
    };
</script>
