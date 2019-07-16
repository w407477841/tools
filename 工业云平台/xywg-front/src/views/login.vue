<template>
  <Row
    type="flex"
    justify="center"
    align="middle"
    class="login"
    @keydown.enter.native="submitLogin"
  >
    <Row class="header">
      <img src="../images/logo.png" width="156px">
    </Row>
    <Col :xs="{span:22}" style="width: 280px;">
      <Alert type="error" show-icon v-if="error">{{errorMsg}}</Alert>
      <div class="login__switch">
        <span
          :class="{'login__swich-item':tabName == 'mobile','login__swich-active':tabName == 'username'}"
          @click="swichMove"
        >密码登录</span>
        <span
          :class="{'login__swich-item':tabName == 'username','login__swich-active':tabName == 'mobile'}"
          @click="swichMove"
        >手机号登录</span>
        <div
          :class="{'login__swich-bar':tabName == 'username','login__swich-barS':tabName == 'mobile'}"
        ></div>
      </div>
      <Row class="login-form">
        <Tabs v-model="tabName">
          <TabPane label="密码登录" name="username" icon>
            <Form ref="usernameLoginForm" :model="form" :rules="rules" class="form">
              <FormItem prop="username">
                <Input v-model="form.username" size="large" clearable placeholder="请输入用户名/手机号"></Input>
              </FormItem>
              <FormItem prop="password">
                <Input
                  type="password"
                  v-model="form.password"
                  size="large"
                  clearable
                  placeholder="请输入密码"
                ></Input>
              </FormItem>
            </Form>
            <Row type="flex" justify="space-between" class="code-row-bg">
              <Checkbox v-model="saveLogin" size="large">自动登录</Checkbox>
              <router-link to="/upPassword">
                <a class="forget-pass">忘记密码</a>
              </router-link>
            </Row>
          </TabPane>
          <TabPane label="手机号登录" name="mobile">
            <Form ref="mobileLoginForm" :model="form" :rules="rules" class="form">
              <FormItem prop="mobile">
                <Input v-model="form.mobile" size="large" clearable placeholder="请输入手机号"></Input>
              </FormItem>
              <FormItem prop="verifyCode" :error="errorCode">
                <Row type="flex" justify="space-between" class="code-row-bg">
                  <Input
                    v-model="form.verifyCode"
                    size="large"
                    clearable
                    placeholder="请输入短信验证码"
                    :maxlength="maxLength"
                    class="input-verify"
                  ></Input>
                  <Button size="large" @click="sendVerify" v-if="!sended" class="send-verify">获取验证码</Button>
                  <Button size="large" disabled v-if="sended" class="count-verify">{{countButton}}</Button>
                </Row>
              </FormItem>
            </Form>
          </TabPane>
        </Tabs>

        <!-- <Row type="flex" justify="space-between" class="code-row-bg">
          <Checkbox v-model="saveLogin" size="large">自动登录</Checkbox>
          <a class="forget-pass" @click="showAccount">忘记密码</a>
        </Row>-->
        <Row>
          <Button
            class="login-btn"
            type="primary"
            size="large"
            :loading="loading"
            @click="submitLogin"
            long
          >
            <span v-if="!loading">登录</span>
            <span v-else>登录中...</span>
          </Button>
        </Row>
        <Row type="flex" justify="center" class="code-row-bg other-login">
          <!-- <span class="other-way">其它方式登录
            <icon class="other-icon" name="brands/qq"></icon>
            <icon class="other-icon lower" scale="1.2" name="brands/weixin"></icon>
            <icon class="other-icon lower" scale="1.2" name="brands/weibo"></icon>
          </span>-->
          <span class="login__tips">没有账号?</span>
          <router-link to="/regist">
            <a class="forget-pass">去注册</a>
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
          <a href="#" target="_blank" style="margin:0 5px;">星云网格</a>
        </Row>
      </Row>
    </Col>
  </Row>
</template>

<script>
import Cookies from "js-cookie";
import { setStore } from "@/utils/storage";
import router from "@/router/index";
import util from "@/libs/util.js";
export default {
  data() {
    const validateMobile = (rule, value, callback) => {
      var reg = /^1[3456789]\d{9}$/;
      if (!reg.test(value)) {
        callback(new Error("手机号格式错误"));
      } else {
        callback();
      }
    };
    return {
      error: false,
      errorMsg: "",
      tabName: "username",
      saveLogin: true,
      loading: false,
      sended: false,
      count: 60,
      countButton: "60 s",
      maxLength: 4,
      errorCode: "",
      form: {
        username: "",
        password: "",
        mobile: "",
        verifyCode: ""
      },
      rules: {
        username: [
          { required: true, message: "账号不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        mobile: [
          { required: true, message: "手机号不能为空", trigger: "blur" },
          { validator: validateMobile, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    showErrorMsg(msg) {
      this.error = true;
      this.errorMsg = msg;
    },
    sendVerify() {
      this.$refs.mobileLoginForm.validate(valid => {
        if (valid) {
          this.getRequest("/common/captcha/regist/" + this.form.mobile).then(
            res => {
              console.log(res);
              if (res.code == 200) {
                if (res.data.exist === false) {
                  this.postRequest("/common/captcha/smsCode", {
                    mobile: this.form.mobile,
                    type: 1 //注册是0 登录是1
                  }).then(res => {
                    console.log(res);
                    if (res.code == 200) {
                      this.sended = true;
                      this.countDown();
                    }
                  });
                } else if (res.data.exist === true) {
                  this.$Message.error("手机号不存在，请先注册");
                }
              }
            }
          );
          // this.sended = true;
          // this.countDown();
        }
      });
    },
    countDown() {
      let that = this;
      if (this.count === 0) {
        this.sended = false;
        this.count = 60;
        return;
      } else {
        this.countButton = this.count + " s";
        this.count--;
      }
      setTimeout(function() {
        that.countDown();
      }, 1000);
    },
    submitLogin() {
      if (this.tabName === "username") {
        this.$refs.usernameLoginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            this.postRequest("/login", {
              username: this.form.username,
              password: this.form.password,
              saveLogin: this.saveLogin
            }).then(res => {
              if (res.success === true) {
                setStore("accessToken", res.result);
                // 获取用户信息
                this.getRequest("/user/info").then(res => {
                  if (res.success === true) {
                    // 避免超过大小限制
                    delete res.result.permissions;
                    if (this.saveLogin) {
                      // 保存7天
                      Cookies.set("userInfo", JSON.stringify(res.result), {
                        expires: 7
                      });
                    } else {
                      Cookies.set("userInfo", JSON.stringify(res.result));
                    }
                    console.info("Hi,data");
                    setStore("userInfo", res.result);
                    this.$store.commit("setAvatarPath", res.result.avatar);
                    // 加载菜单
                    util.initRouter(this);
                    this.$router.push({
                      name: "home_index"
                    });
                  } else {
                    this.loading = false;
                  }
                });
              } else {
                this.loading = false;
              }
            });
          }
        });
      } else if (this.tabName === "mobile") {
        this.$refs.mobileLoginForm.validate(valid => {
          if (valid) {
            if (this.form.verifyCode === "") {
              this.errorCode = "验证码不能为空";
              return;
            } else {
              this.errorCode = "";
            }
            this.loading = true;
            this.postRequest("/smsLogin", {
              mobile: this.form.mobile,
              code: this.form.verifyCode
            }).then(res => {
              if (res.success === true) {
                setStore("accessToken", res.result);
                // 获取用户信息
                this.getRequest("/user/info").then(res => {
                  if (res.success === true) {
                    // 避免超过大小限制
                    delete res.result.permissions;
                    Cookies.set("userInfo", JSON.stringify(res.result));
                    setStore("userInfo", res.result);
                    this.$store.commit("setAvatarPath", res.result.avatar);
                    // 加载菜单
                    util.initRouter(this);
                    this.$router.push({
                      name: "home_index"
                    });
                  } else {
                    this.loading = false;
                  }
                });
              } else {
                this.loading = false;
              }
            });
          }
        });
      }
    },
    // showAccount() {
    //   this.$Notice.info({
    //     title: "体验账号密码",
    //     desc:
    //       "账号1：test 密码：123456 <br>账号2：test2 密码：123456 已开放注册！"
    //   });
    // },
    swichMove() {
      if (this.tabName == "username") {
        this.tabName = "mobile";
      } else if (this.tabName == "mobile") {
        this.tabName = "username";
      }
    }
  },
  mounted() {
    // this.showAccount();
  }
};
</script>

<style lang="less">
@import "./login.less";
@import "../styles/common.less";
</style>
