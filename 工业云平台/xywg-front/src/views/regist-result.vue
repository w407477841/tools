<template>
  <div style="height:100%;">
    <div class="common__top">
      <img src="../images/logo.png" alt>
      <div class="common__top-right">
        <Icon type="ios-help-outline" size="25" color="#eff2f4"></Icon>
        <span>帮助</span>
        <img class="common__top-avatar" src="https://s1.ax1x.com/2018/05/19/CcdVQP.png">
      </div>
    </div>
    <div
      type="flex"
      justify="center"
      align="middle"
      class="regist__result"
      @keydown.enter="submitRegist"
    >
      <Row class="success">
        <div class="regist__successBox">
          <Icon type="checkmark-circled" color="#52c41a" size="40"></Icon>
          <h1>注册成功</h1>
        </div>
        <div class="regist__content">
          <div class="regist__content-item">
            <span>会员名:</span>
            <span>{{username}}</span>
          </div>
          <div class="regist__content-item">
            <span>绑定手机:</span>
            <span>{{mobile}}</span>
          </div>
        </div>
        <h3>您尚未完成企业认证</h3>
        <p class="success-words">为保证您的账户及资源的所属权安全，建议您优先完成企业认证。</p>
        <Row class="buttons">
          <router-link to="/login">
            <Button type="primary" size="large" shape="circle">登录进行企业认证</Button>
          </router-link>
        </Row>
      </Row>
      <Row class="foot">
        <Row type="flex" justify="space-around" class="code-row-bg help">
          <!-- <a class="item">帮助</a>
          <a class="item">隐私</a>
          <a class="item">条款</a>-->
        </Row>
        <Row type="flex" justify="center" class="code-row-bg copyright">
          Copyright © 2018
          <a href="#" target="_blank" style="margin:0 5px;">星云网格</a> Presents
        </Row>
      </Row>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";
export default {
  data() {
    return {
      username: "",
      mobile: ""
    };
  },
  methods: {},
  mounted() {
    this.username = this.$route.query.username;
    this.mobile = this.$route.query.mobile;
  }
};
</script>

<style lang="less">
@import "./regist-result.less";
</style>
