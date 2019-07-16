<!--
 * @Author: zhangzhenya 
 * @CreateTime: 2019-02-22 14:28:55 
 * @Desc: 认证结果页 
-->
<template>
  <div class="result">
    <div class="result__top">
      <h3 class="result__top-title">企业认证</h3>
      <Steps :current="status==1?2:3" class="result__top-steps" size="small">
        <Step title="基本信息"></Step>
        <Step title="企业信息"></Step>
        <Step title="人工审核"></Step>
        <Step title="认证结果"></Step>
      </Steps>
      <div class="result__top-tips" v-if="status == 2">
        <p>完成认证</p>
      </div>
    </div>
    <div v-if="status == 1" class="result__main" :style="{height:mainHeight+'px'}">
      <div class="result__break"></div>
      <div class="result__main-title">
        <img src="~@/images/review.png">
        <h1>人工审核中...</h1>
      </div>
      <div class="result__main-content">
        <p>当前人工审核中,请耐心等待,审核结果会及时通知给您!</p>
      </div>
      <Button type="primary" shape="circle" @click="routerToHome">返回首页</Button>
    </div>
    <div v-if="status == 2">
      <Form :label-width="120">
        <div class="result__titleBlock">
          <span>基本信息</span>
        </div>
        <Row type="flex" justify="start" class="result__form">
          <Col span="11">
            <FormItem label="真实姓名" prop="name">
              <p class="result__form-content">{{indInforForm.name}}</p>
            </FormItem>
            <FormItem label="身份证号" prop="identityNo">
              <p class="result__form-content">{{indInforForm.identityNo}}</p>
            </FormItem>
            <FormItem label="职务" prop="staff">
              <p class="result__form-content">{{indInforForm.staff}}</p>
            </FormItem>
            <FormItem label="来源" prop="source">
              <p class="result__form-content" v-if="indInforForm.source == 1">周围人推荐</p>
              <p class="result__form-content" v-if="indInforForm.source == 2">已建立合作</p>
              <p class="result__form-content" v-if="indInforForm.source == 3">新闻报道</p>
              <p class="result__form-content" v-if="indInforForm.source == 4">搜索引擎</p>
              <p class="result__form-content" v-if="indInforForm.source == 5">会议会展</p>
              <p class="result__form-content" v-if="indInforForm.source == 6">其他</p>
            </FormItem>
          </Col>
        </Row>
        <div class="result__titleBlock">
          <span>联系信息</span>
        </div>
        <Row type="flex" justify="start" class="result__form">
          <Col span="11">
            <FormItem label="所在地区" prop="area">
              <p class="result__form-content">
                <span>{{indInforForm.provinceName?indInforForm.provinceName:''}}</span>
                <span>{{indInforForm.cityName?indInforForm.cityName:''}}</span>
                <span>{{indInforForm.districtName?indInforForm.districtName:''}}</span>
              </p>
            </FormItem>
            <FormItem label="街道地址" prop="street">
              <p class="result__form-content">{{indInforForm.street?indInforForm.street:''}}</p>
            </FormItem>
            <FormItem label="联系电话" prop="tel">
              <p class="result__form-content">{{indInforForm.tel?indInforForm.tel:''}}</p>
            </FormItem>
            <FormItem label="电子邮件" prop="email">
              <p class="result__form-content">{{indInforForm.email?indInforForm.email:""}}</p>
            </FormItem>
            <FormItem label="传真" prop="fax">
              <p class="result__form-content">{{indInforForm.fax?indInforForm.fax:""}}</p>
            </FormItem>
          </Col>
        </Row>
        <div class="result__titleBlock">
          <span>企业基本信息</span>
        </div>
        <Row type="flex" justify="start" class="result__form">
          <Col span="11">
            <FormItem label="企业全称" prop="enterpriseName">
              <p class="result__form-content">{{comInforForm.name}}</p>
            </FormItem>
            <FormItem label="企业简称" prop="shortName">
              <p class="result__form-content">{{comInforForm.shortName}}</p>
            </FormItem>
            <FormItem label="企业官网" prop="website">
              <p class="result__form-content">{{comInforForm.website}}</p>
            </FormItem>
            <FormItem label="统一社会信用代码" prop="creditCode">
              <p class="result__form-content">{{comInforForm.creditCode}}</p>
            </FormItem>
            <FormItem label="企业描述" prop="companyDescription">
              <p class="result__form-textarea">{{comInforForm.companyDescription}}</p>
            </FormItem>
          </Col>
        </Row>
        <div class="result__titleBlock">
          <span>企业背景信息</span>
        </div>
        <Row type="flex" justify="start" class="result__form">
          <Col span="11">
            <FormItem label="年营业额" prop="annualTurnover">
              <p class="result__form-content">{{comInforForm.annualTurnover}}</p>
            </FormItem>
            <FormItem label="软件研发团队" prop="team">
              <p class="result__form-content" v-if="comInforForm.team == 1">公司团队</p>
              <p class="result__form-content" v-if="comInforForm.team == 2">外包团队</p>
              <p class="result__form-content" v-if="comInforForm.team == 3">无团队</p>
            </FormItem>
            <FormItem label="以往明星产品" prop="starProduct">
              <p class="result__form-textarea">{{comInforForm.starProduct}}</p>
            </FormItem>
          </Col>
        </Row>
        <div class="result__titleBlock">
          <span>拟接入产品信息</span>
        </div>
        <Row type="flex" justify="start" class="result__form">
          <Col span="11">
            <FormItem label="产品名称" prop="productName">
              <p class="result__form-content">{{comInforForm.productName}}</p>
            </FormItem>
            <FormItem label="产品类型" prop="productType">
              <p class="result__form-content">{{comInforForm.productTypeName}}</p>
            </FormItem>
            <FormItem label="联网方式" prop="linkType">
              <p class="result__form-content">{{comInforForm.linkTypeName}}</p>
            </FormItem>
            <FormItem label="操作系统" prop="operateSystem">
              <p class="result__form-content">{{comInforForm.operateSystemName}}</p>
            </FormItem>
            <FormItem label="产品描述" prop="productDescription">
              <p class="result__form-textarea">{{comInforForm.productDescription}}</p>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </div>
    <div v-if="status == 3" class="result__main">
      <div class="result__break"></div>
      <div class="result__main-title">
        <img src="~@/images/failed.png">
        <h1>企业认证失败</h1>
      </div>
      <Button type="primary" shape="circle" @click="routerToEnterprise">修改认证信息</Button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      status: "",
      comInforForm: {},
      indInforForm: {},
      mainHeight: ""
    };
  },
  watch: {
    $route: {
      deep: true,
      handler() {}
    }
  },
  methods: {
    //路由跳转到主页
    routerToHome() {
      this.$router.push({ name: "home_index" });
    },
    //路由跳转到企业信息
    routerToEnterprise() {
      this.$store.state.authenticate.isFix = true;
      this.$router.push({
        name: "authenticate",
        query: { route: "information" }
      });
    },
    //获取成功后的详情
    getEnterpriseCertification() {
      this.getBusRequest("/accountInfoCompany/getEnterpriseCertification", {
        userId: JSON.parse(this.cookies.get("userInfo")).id
      }).then(res => {
        console.log(res.data);
        if (res.code == 200 || res.code == "200") {
          this.comInforForm = res.data.company;
          this.indInforForm = res.data.individual;
        }
      });
    },
    //获取认证状态
    getAuditStatus() {
      this.getBusRequest("/accountInfoIndividual/selectByUserId", {
        userId: JSON.parse(this.cookies.get("userInfo")).id
      }).then(_res => {
        console.log(_res.data.auditStatus, !_res.data.auditStatus);
        if (_res.code == 200 || _res.code == "200") {
          this.auditStatus = _res.data.auditStatus;
          console.log(this.auditStatus);
          if (
            this.auditStatus &&
            this.auditStatus !== 0 &&
            this.auditStatus !== 3
          ) {
            this.$store.commit("removeTag", "authenticate");
            this.$router.push({
              name: "result",
              query: { status: _res.data.auditStatus }
            });
          } else if (this.auditStatus === false || this.auditStatus === "") {
            this.$store.commit("removeTag", "authenticate");
            this.$router.push({
              name: "authenticate",
              query: { router: "information" }
            });
          }
        }
      });
    }
  },
  mounted() {
    this.$store.commit("removeTag", "authenticate");
    this.status = this.$route.query.status;
    // this.status = 2;
    if (this.status == 2) {
      this.getEnterpriseCertification();
    } else {
      this.mainHeight = window.innerHeight - 100 - 32 - 20 - 42;
    }
  }
};
</script>

<style lang="less" scoped>
@import "./result.less";
</style>
