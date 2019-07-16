<template>
  <div class="home">
    <div class="home__tabs">
      <span class="home__tabs-item">设备数量</span>
    </div>
    <div class="home__top">
      <div class="home__topleft">
        <div class="home__topleft-item">
          <p>累积设备数</p>
          <h1>{{realTotal}}</h1>
          <!-- <div class="home__compare">
                  <span>
                    <Icon type="chevron-up" color="#28ae64"></Icon>
                    <span style="color:#28ae64">15.2%</span>
                  </span>
                  <span>同比上周</span>
          </div>-->
        </div>
        <div class="home__topleft-item">
          <p>本周新增设备数</p>
          <h1>{{realNewAdd}}</h1>
          <div class="home__compare" v-if="realHuanbi">
            <span class="home__compare-percentage">
              <Icon v-if="realHuanbi>=0" type="arrow-up-b" color="#28ae64"></Icon>
              <Icon else type="arrow-down-b" color="#ff5855"></Icon>
              <span style="color:#ff5855">{{realHuanbi>=0?realHuanbi + "%":-realHuanbi + "%"}}</span>
            </span>
            <span class="home__compare-tips">环比</span>
          </div>
        </div>
      </div>
      <div class="home__topright">
        <div class="home__topright-top">
          <h3>设备数量</h3>
          <div class="home__screening">
            <a
              @click="pickDeviceWeek"
              :class="{'home__picked':deviceWeekPicked,'home__notPicked':!deviceWeekPicked}"
            >本周</a>
            <a
              @click="pickDeviceMonth"
              :class="{'home__picked':deviceMonthPicked,'home__notPicked':!deviceMonthPicked}"
            >本月</a>
            <DatePicker
              v-model="deviceDate"
              type="daterange"
              placement="bottom-end"
              placeholder="选择时间段"
              style="width: 200px"
              @on-change="deviceDateChange"
            ></DatePicker>
          </div>
        </div>
        <gradient-line
          v-if="showDeviceLine"
          class="home__chartsBox"
          :dataSource="deviceLineData.value"
          :xList="deviceLineData.name"
          :color0="attColorEnd"
          :color100="attColorStart"
          :title="deviceLineTitle"
        ></gradient-line>
      </div>
    </div>
    <div class="home__bottom">
      <div class="home__bottomLeft">
        <div class="home__bottomLeft-title">
          <h1>设备使用情况</h1>
        </div>
        <div class="home__bottomLeft-main">
          <home-pie
            v-if="showHomePie"
            class="home__pieBox"
            :dataSourse="deviceAddrData"
            :legendData="pieLegendData"
          ></home-pie>
        </div>
        <div v-if="showHomePie" class="home__pieTotal">
          <h1>{{deviceTotal}}</h1>
          <p>主机(台)</p>
        </div>
      </div>
      <div class="home__bottomRight">
        <div class="home__bottomRight-title">
          <h1>发送到平台的数据量</h1>
        </div>
        <div class="home__bottomRight-main">
          <div class="home__bottomRight-top">
            <div class="home__screening">
              <a
                @click="pickDataWeek"
                :class="{'home__picked':dataWeekPicked,'home__notPicked':!dataWeekPicked}"
              >本周</a>
              <a
                @click="pickDataMonth"
                :class="{'home__picked':dataMonthPicked,'home__notPicked':!dataMonthPicked}"
              >本月</a>
              <DatePicker
                v-model="dataDate"
                type="daterange"
                placement="bottom-end"
                placeholder="选择时间段"
                style="width: 200px"
                @on-change="dataDateChange"
              ></DatePicker>
            </div>
          </div>
          <gradient-line
            v-if="showAccountLine"
            class="home__chartsBox"
            :dataSource="accountLineData.value"
            :xList="accountLineData.name"
            :color0="attColorEnd"
            :color100="attColorStart"
            :title="acconutLineTitle"
          ></gradient-line>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import thousandsth from "thousandsth";
import gradientLine from "../../components/gradientLine";
import homePie from "../../components/homePie";
export default {
  components: {
    gradientLine,
    homePie
  },
  data() {
    return {
      weekBeginDate: "",
      weekEndDate: "",
      monthBeginDate: "",
      monthEndDate: "",
      deviceLineData: {
        name: [],
        value: []
      },
      accountLineData: {
        name: [],
        value: []
      },
      showDeviceLine: false,
      showAccountLine: false,
      deviceLineTitle: "设备数量",
      acconutLineTitle: "数据量",
      attColorEnd: "rgba(0,124,255,1)",
      attColorStart: "rgba(0,124,255,.1)",
      showHomePie: false,
      pieLegendData: [],
      deviceAddrData: [],
      deviceTotal: new Number(),
      realTotal: "",
      realNewAdd: "",
      realHuanbi: "",
      deviceWeekPicked: true,
      deviceMonthPicked: false,
      deviceCustomPicked: false,
      dataWeekPicked: true,
      dataMonthPicked: false,
      dataCustomPicked: false,
      deviceDate: "",
      dataDate: ""
    };
  },
  mounted() {
    this.num1 = thousandsth("2223");
    this.num2 = thousandsth("342");
    this.getThisWeek();
    this.getDeviceTotal();
    this.getDeviceLine(this.weekBeginDate, this.weekEndDate);
    this.getDeviceAddrPie();
    this.getAccountLine(this.weekBeginDate, this.weekEndDate);
  },
  methods: {
    deviceDateChange(date) {
      this.getDeviceLine(date[0], date[1]);
      this.deviceWeekPicked = false;
      this.deviceMonthPicked = false;
    },
    dataDateChange(date) {
      this.getAccountLine(date[0], date[1]);
      this.dataWeekPicked = false;
      this.dataMonthPicked = false;
    },
    pickDeviceWeek() {
      if (!this.deviceWeekPicked) {
        this.getThisWeek();
        this.getDeviceLine(this.weekBeginDate, this.weekEndDate);
        this.deviceWeekPicked = true;
        this.deviceMonthPicked = false;
        this.deviceDate = "";
      }
    },
    pickDeviceMonth() {
      if (!this.deviceMonthPicked) {
        this.getThisMonth();
        this.getDeviceLine(this.monthBeginDate, this.monthEndDate);
        this.deviceWeekPicked = false;
        this.deviceMonthPicked = true;
        this.deviceDate = "";
      }
    },
    pickDataWeek() {
      if (!this.dataWeekPicked) {
        this.getThisWeek();
        this.getAccountLine(this.weekBeginDate, this.weekEndDate);
        this.dataWeekPicked = true;
        this.dataMonthPicked = false;
        this.dataDate = "";
      }
    },
    pickDataMonth() {
      if (!this.dataMonthPicked) {
        this.getThisMonth();
        this.getAccountLine(this.monthBeginDate, this.monthEndDate);
        this.dataWeekPicked = false;
        this.dataMonthPicked = true;
        this.dataDate = "";
      }
    },
    getThisWeek() {
      let date = new Date();
      // 本周一的日期
      date.setDate(date.getDate() - date.getDay() + 1);
      this.weekBeginDate =
        date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
      // 本周日的日期
      date.setDate(date.getDate() + 6);
      this.weekEndDate =
        date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
    },
    getThisMonth() {
      let date = new Date();
      this.monthBeginDate =
        date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + 1;
      this.monthEndDate =
        date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + 31;
    },
    getDeviceLine(beginDate, endDate) {
      this.deviceLineData = {
        name: [],
        value: []
      };
      this.postBusRequest("/homepage/totalDeviceLine", {
        begin: beginDate,
        end: endDate
      }).then(res => {
        if (res.code == 200 || res.code == "200") {
          res.data.forEach(el => {
            this.deviceLineData.name.push(el.reportDate);
            this.deviceLineData.value.push(el.account);
          });
          this.showDeviceLine = true;
        }
      });
    },
    getDeviceAddrPie() {
      this.postBusRequest("/homepage/deviceAddrPie").then(res => {
        if (res.code == 200 || res.code == "200") {
          this.pieLegendData = [];
          this.deviceAddrData = [];
          this.deviceTotal = 0;
          res.data.forEach(el => {
            this.deviceTotal += el.account;
          });
          res.data.forEach(el => {
            this.pieLegendData.push(
              `${el.name}  ${el.account}台  ${
                this.deviceTotal
                  ? parseInt((el.account / this.deviceTotal) * 100)
                  : ""
              }%`
            );
            this.deviceAddrData.push({
              value: el.account,
              name: `${el.name}  ${el.account}台  ${parseInt(
                (el.account / this.deviceTotal) * 100
              )}%`
            });
          });
          this.showHomePie = true;
        }
      });
    },
    getAccountLine(beginDate, endDate) {
      this.accountLineData = {
        name: [],
        value: []
      };
      this.postBusRequest("/homepage/dataAccountLine", {
        begin: beginDate,
        end: endDate
      }).then(res => {
        if (res.code == 200 || res.code == "200") {
          res.data.forEach(el => {
            this.accountLineData.name.push(el.reportDate);
            this.accountLineData.value.push(el.account);
          });
          this.showAccountLine = true;
        }
      });
    },
    getDeviceTotal() {
      this.postBusRequest("/homepage/deviceTotal").then(res => {
        if (res.code == 200 || res.code == "200") {
          this.realTotal = res.data.total;
          this.realNewAdd = res.data.newAdd;
          this.realHuanbi = res.data.huanbi.slice(0, -1);
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
@import "./home.less";
</style>
