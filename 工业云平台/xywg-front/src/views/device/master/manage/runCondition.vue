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
              <Select
                v-model="searchForm.key"
                @on-change="selectChange"
                style="width:200px"
                placeholder="查询选型"
              >
                <Option
                  v-for="item in keyList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.label }}</Option>
              </Select>

              <Form-item>
                <DatePicker
                  v-model="searchForm.beginDate"
                  type="date"
                  @on-change="searchForm.beginDate=$event"
                  placeholder="开始时间"
                  :disabled="isDateDis"
                  style="width: 200px"
                ></DatePicker>
              </Form-item>
              <Form-item>
                <DatePicker
                  v-model="searchForm.endDate"
                  type="date"
                  @on-change="searchForm.endDate=$event"
                  placeholder="结束时间"
                  :disabled="isDateDis"
                  style="width: 200px"
                ></DatePicker>
              </Form-item>
              <Form-item style="margin-left:-70px;">
                <Button @click="handleSearch" type="primary" icon="search">搜索</Button>
              </Form-item>
              <!-- <Form-item style="margin-left:-70px;">
                                <Button @click="goBack">取消</Button>                              
              </Form-item>-->
            </Form>
          </Row>
          <Tabs value="属性" @on-click="tabChange">
            <TabPane label="属性" name="属性">
              <Row class="margin-top-10 searchable-table-con1">
                <Table :loading="loading" border :columns="columns" :data="data" ref="table"></Table>
              </Row>
              <Row type="flex" justify="end" class="code-row-bg page">
                <Page
                  :current="this.pageNum"
                  :total="total"
                  :page-size="this.pageSize"
                  :page-size-opts="[10,20,50,100]"
                  size="small"
                  show-total
                  show-elevator
                  show-sizer
                  @on-change="changePage"
                  @on-page-size-change="changePageSize"
                ></Page>
              </Row>
            </TabPane>
            <TabPane label="事件" name="事件">
              <Row class="margin-top-10 searchable-table-con1">
                <Table :loading="loading" border :columns="columns" :data="data" ref="table"></Table>
              </Row>
              <Row type="flex" justify="end" class="code-row-bg page">
                <Page
                  :current="this.pageNum"
                  :total="total"
                  :page-size="this.pageSize"
                  :page-size-opts="[10,20,50,100]"
                  size="small"
                  show-total
                  show-elevator
                  show-sizer
                  @on-change="changePage"
                  @on-page-size-change="changePageSize"
                ></Page>
              </Row>
            </TabPane>
            <TabPane label="方法" name="方法">
              <Row class="margin-top-10 searchable-table-con1">
                <Table :loading="loading" border :columns="columns" :data="data" ref="table"></Table>
              </Row>
              <Row type="flex" justify="end" class="code-row-bg page">
                <Page
                  :current="this.pageNum"
                  :total="total"
                  :page-size="this.pageSize"
                  :page-size-opts="[10,20,50,100]"
                  size="small"
                  show-total
                  show-elevator
                  show-sizer
                  @on-change="changePage"
                  @on-page-size-change="changePageSize"
                ></Page>
              </Row>
            </TabPane>
          </Tabs>
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script>
import { getStore } from "@/utils/storage.js";
export default {
  name: "runCondition",
  data() {
    return {
      loading: true,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      submitLoading: false,
      isDateDis: true,
      searchForm: {
        key: "",
        beginDate: null,
        endDate: null
      },
      keyList: [
        {
          value: 1,
          label: "7天内"
        },
        {
          value: 2,
          label: "15天内"
        },
        {
          value: 3,
          label: "30天内"
        },
        {
          value: 4,
          label: "自定义"
        }
      ],
      columns: [
        {
          type: "index",
          width: 60,
          align: "center"
        },
        {
          title: "数据时间",
          key: "collectTime",
          align: "center",
          render: (h, params) => {
            let re = params.row.collectTime;
            re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
            return h("div", re);
          }
        },
        {
          title: "设备编号",
          key: "deviceNo",
          align: "center"
        },
        {
          title: "数据内容",
          key: "dataDetail",
          align: "center",
          render: (h, params) => {
            let datas = params.row.dataDetail.split(",");
            let alarms = params.row.alarmDetail.split(",");
            let childs = [];
            datas.forEach((element, index) => {
              childs.push(
                h(
                  "font",
                  { style: { color: alarms[index] === "0" ? "green" : "red" } },
                  element
                )
              );
              if ((index + 1) % 3 === 0 && index != 0) {
                childs.push(h("br"));
              }
            });
            return h("div", childs);
          }
        }
        // ,
        // {
        //     title: "报警内容",
        //     key: "alarmDetail",
        //     align: "center",
        //      render: (h, params) => {

        //         return  h('div',[h("font",{style:{color:'red'}}, '温度:20,'),h("font",{style:{color:'green'}}, '湿度：80'),h("font",{style:{color:'red'}}, '风速：1.1')])  ;
        //     }
        // }
      ],
      data: [],
      productList: [],
      file: {},
      url: "/devicePropertyRecord/getRunCondition"
    };
  },
  methods: {
    init() {
      this.accessToken = {
        accessToken: getStore("accessToken")
      };
      this.getList("/devicePropertyRecord/getRunCondition");
    },
    //获取列表
    getList(url) {
      let params = {
        id: this.$route.query.id,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        beginDate: this.searchForm.beginDate,
        endDate: this.searchForm.endDate
      };
      this.loading = true;
      this.postBusRequest(url, params).then(res => {
        this.loading = false;
        this.data = res.data.list;
        this.total = res.data.total;
      });
    },
    tabChange(name) {
      console.log(name);
      if (name == "方法") {
        this.url = "/deviceMethodRecord/getRunCondition";
        this.columns = [
          {
            type: "index",
            width: 60,
            align: "center"
          },
          {
            title: "调用时间",
            key: "callTime",
            align: "center",
            render: (h, params) => {
              let re = params.row.callTime;
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            }
          },
          {
            title: "反馈时间",
            key: "receiveTime",
            align: "center",
            render: (h, params) => {
              let re = params.row.receiveTime;
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            }
          },
          {
            title: "设备编号",
            key: "deviceNo",
            align: "center"
          },
          //0:无返回 1:已返回 2:超时
          {
            title: "状态",
            key: "status",
            align: "center",
            render: (h, params) => {
              let status = params.row.status;
              if (status == 0) {
                return h("div", { style: { color: "orange" } }, "无返回");
              } else if (status == 1) {
                return h("div", { style: { color: "green" } }, "已返回");
              } else if (status == 2) {
                return h("div", { style: { color: "red" } }, "超时");
              }
            }
          },
          {
            title: "入参",
            key: "inParams",
            align: "center"
          },
          {
            title: "出参",
            key: "outParams",
            align: "center"
          }
        ];
        this.getList(this.url);
      } else if (name == "属性") {
        this.url = "/devicePropertyRecord/getRunCondition";
        this.columns = [
          {
            type: "index",
            width: 60,
            align: "center"
          },
          {
            title: "数据时间",
            key: "collectTime",
            align: "center",
            render: (h, params) => {
              let re = params.row.collectTime;
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            }
          },
          {
            title: "设备编号",
            key: "deviceNo",
            align: "center"
          },
          {
            title: "数据内容",
            key: "dataDetail",
            align: "center",
            render: (h, params) => {
              let datas = params.row.dataDetail.split(",");
              let alarms = params.row.alarmDetail.split(",");
              let childs = [];
              datas.forEach((element, index) => {
                childs.push(
                  h(
                    "font",
                    {
                      style: { color: alarms[index] === "0" ? "green" : "red" }
                    },
                    element
                  )
                );
                if ((index + 1) % 3 === 0 && index != 0) {
                  childs.push(h("br"));
                }
              });
              return h("div", childs);
            }
          }
        ];
        this.getList(this.url);
      } else if (name == "事件") {
        this.url = "/deviceEventRecord/getRunCondition";
        this.columns = [
          {
            type: "index",
            width: 60,
            align: "center"
          },
          {
            title: "触发时间",
            key: "triggerTime",
            align: "center",
            render: (h, params) => {
              let re = params.row.triggerTime;
              re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
              return h("div", re);
            }
          },
          {
            title: "设备编号",
            key: "deviceNo",
            align: "center"
          },
          //1:信息 2:告警 3:故障
          {
            title: "事件类型",
            key: "eventType",
            align: "center",
            render: (h, params) => {
              let eventType = params.row.eventType;
              if (eventType == 1) {
                return h("div", { style: { color: "green" } }, "信息");
              } else if (eventType == 2) {
                return h("div", { style: { color: "orange" } }, "告警");
              } else if (eventType == 3) {
                return h("div", { style: { color: "red" } }, "故障");
              }
            }
          },
          {
            title: "入参",
            key: "inParams",
            align: "center"
          }
        ];
        this.getList(this.url);
      }
    },
    //模糊查询
    handleSearch() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.getList(this.url);
    },
    //翻页
    changePage(v) {
      this.pageNum = v;
      this.getList(this.url);
    },
    //改变显示条数
    changePageSize(v) {
      this.pageSize = v;
      this.getList(this.url);
    },
    //改变查询条件对应改变日期
    selectChange: function(v) {
      if (v === 4) {
        this.isDateDis = false;
      } else {
        this.isDateDis = true;
        if (v === 1) {
          let begin = new Date(this.moment(new Date()).format("YYYY-MM-DD"));
          this.searchForm.endDate = new Date();
          this.searchForm.beginDate = new Date(
            begin.setDate(begin.getDate() - 6)
          );
        } else if (v === 2) {
          let begin = new Date(this.moment(new Date()).format("YYYY-MM-DD"));
          this.searchForm.endDate = new Date();
          this.searchForm.beginDate = new Date(
            begin.setDate(begin.getDate() - 14)
          );
        } else if (v === 3) {
          let begin = new Date(this.moment(new Date()).format("YYYY-MM-DD"));
          this.searchForm.endDate = new Date();
          this.searchForm.beginDate = new Date(
            begin.setDate(begin.getDate() - 29)
          );
        }
      }
    },
    //返回设备管理页面
    goBack: function() {
      this.$router.push({ path: "/device/manage" });
    }
  },
  mounted() {
    this.init();
  }
};
</script>
