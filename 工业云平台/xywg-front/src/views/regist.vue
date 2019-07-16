<template>
  <Row type="flex" justify="center" align="middle" class="regist" @keydown.enter="submitRegist">
    <Row class="header">
      <img src="../images/logo.png" width="156px">
    </Row>
    <Col :xs="{span:22}" style="width: 280px;">
      <Alert type="error" show-icon v-if="error">{{errorMsg}}</Alert>
      <Row class="regist-form">
        <Form ref="registForm" :model="form" :rules="rules" class="form">
          <span class="regist-title">欢迎注册</span>
          <FormItem prop="username">
            <Input
              v-model="form.username"
              size="large"
              :maxlength="18"
              clearable
              placeholder="设置用户名"
            />
          </FormItem>
          <FormItem prop="password">
            <Poptip placement="right" width="250">
              <Input
                type="password"
                v-model="form.password"
                @on-change="strengthChange"
                :maxlength="16"
                size="large"
                clearable
                placeholder="设置登录密码"
              />
              <div v-bind:class="tipStyle" slot="content">
                <span class="words">强度 : {{strength}}</span>
                <Slider v-model="strengthValue" :step="33" style="width:95%"></Slider>请至少输入 8 个字符。请不要使
                <br>用容易被猜到的密码。
              </div>
            </Poptip>
          </FormItem>
          <FormItem prop="confirmPass">
            <Input
              type="password"
              v-model="form.confirmPass"
              size="large"
              clearable
              :maxlength="16"
              placeholder="确认登录密码"
            />
          </FormItem>
          <FormItem prop="mobile">
            <Input v-model="form.mobile" size="large" clearable placeholder="请输入手机号">
              <Select v-model="select" slot="prepend" style="width: 70px">
                <Option value="86">+86</Option>
              </Select>
            </Input>
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
              />
              <Button size="large" @click="sendVerify" v-if="!sended" class="send-verify">获取验证码</Button>
              <Button size="large" disabled v-if="sended" class="count-verify">{{countButton}}</Button>
            </Row>
          </FormItem>
          <!-- <FormItem prop="verify">
            <Row type="flex" justify="space-between" class="code-row-bg">
              <Input
                v-model="form.verify"
                size="large"
                clearable
                placeholder="请输入验证码"
                :maxlength="maxLength"
                class="input-verify"
              />
              <img :src="verifyCodeImg" style="width:100px;cursor:pointer" @click="getVerifyCode">
            </Row>
          </FormItem>-->
        </Form>

        <Row type="flex" justify="space-between" class="code-row-bg">
          <Button
            class="regist-btn"
            type="primary"
            size="large"
            :loading="loading"
            @click="submitRegist"
            long
          >
            <span v-if="!loading">同意条款并注册</span>
            <span v-else>注册中...</span>
          </Button>
        </Row>
        <p class="tips">注册即代表同意《星云网格协议》《隐私权政策》</p>
        <p class="register__backLogin">
          <span>已有账号?</span>
          <router-link to="/login">
            <a class="to-login">去登录</a>
          </router-link>
        </p>
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
    </Col>
  </Row>
</template>

<script>
import Cookies from "js-cookie";
//import { imgPath } from "@/libs/base";
export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 8) {
        callback(new Error("密码长度不得小于8位"));
      } else if (value.length > 16) {
        callback(new Error("密码长度不得大于16位"));
      } else {
        callback();
      }
    };
    const validateConfirmPass = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("密码长度不得小于6位"));
      } else if (value !== this.form.password) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    const validateMobile = (rule, value, callback) => {
      var reg = /^1[3456789]\d{9}$/;
      if (!reg.test(value)) {
        callback(new Error("手机号格式错误"));
      } else {
        callback();
      }
    };
    const validateUsername = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error("用户名需要大于4位"));
      } else if (value.length > 18) {
        callback(new Error("用户名需要小于18位"));
      } else if (this.isDigit(value)) {
        callback(new Error("用户名不可纯数字"));
      } else {
        callback();
      }
    };
    return {
      captchaId: "",
      // verifyCodeImg: "",
      error: false,
      errorMsg: "",
      loading: false,
      sended: false,
      count: 60,
      countButton: "60 s",
      maxLength: 4,
      tipStyle: "password-tip-weak",
      strength: "太短",
      select: "86",
      strengthValue: 33,
      errorCode: "",
      form: {
        username: "",
        password: "",
        confirmPass: "",
        mobile: "",
        verifyCode: "",
        verify: ""
      },
      rules: {
        username: [
          { required: true, message: "账号不能为空", trigger: "blur" },
          { validator: validateUsername, trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { validator: validatePassword, trigger: "blur" }
        ],
        confirmPass: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { validator: validateConfirmPass, trigger: "blur" }
        ],
        mobile: [
          { required: true, message: "手机号不能为空", trigger: "blur" },
          { validator: validateMobile, trigger: "blur" }
        ],
        verifyCode: [
          { required: true, message: "验证码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    isDigit(s) {
      var patrn = /^[0-9]{1,20}$/;
      if (!patrn.exec(s)) {
        return false;
      }
      return true;
    },
    showErrorMsg(msg) {
      this.error = true;
      this.errorMsg = msg;
    },
    strengthChange() {
      let length = this.form.password.length;
      if (length < 6) {
        this.tipStyle = "password-tip-weak";
        this.strength = "太短";
        this.strengthValue = 33;
      } else if (length >= 6 && length < 10) {
        this.tipStyle = "password-tip-middle";
        this.strength = "中";
        this.strengthValue = 66;
      } else {
        this.tipStyle = "password-tip-strong";
        this.strength = "强";
        this.strengthValue = 100;
      }
      this.$refs.registForm.validateField("confirmPass");
    },
    sendVerify() {
      this.$refs.registForm.validateField("mobile", res => {
        console.log(res);
        if (!res) {
          this.getRequest("/common/captcha/regist/" + this.form.mobile).then(
            res => {
              console.log(res);
              if (res.code == 200) {
                if (res.data.exist === true) {
                  this.postRequest("/common/captcha/smsCode", {
                    mobile: this.form.mobile,
                    type: 0 //注册是0 登录是1
                  }).then(res => {
                    console.log(res);
                    if (res.code == 200) {
                      this.sended = true;
                      this.countDown();
                    }
                  });
                } else if (res.data.exist === false) {
                  this.$Message.error("手机号已存在，直接登录");
                }
              }
            }
          );
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
    submitRegist() {
      this.$refs.registForm.validate(valid => {
        if (valid) {
          if (this.form.verifyCode === "") {
            this.errorCode = "验证码不能为空";
            return;
          } else {
            this.errorCode = "";
          }
          this.loading = true;
          this.form.captchaId = this.captchaId;
          this.postRequest("/user/regist", this.form).then(res => {
            this.loading = false;
            if (res.success === true) {
              let query = {
                username: this.form.username,
                mobile: this.form.mobile
              };
              this.$router.push({
                name: "regist-result",
                query: query
              });
            }
            // else {
            //   this.getVerifyCode();
            // }
          });
        }
      });
    }
    // getVerifyCode() {
    //   this.getRequest("/common/captcha/init").then(res => {
    //     if (res.success === true) {
    //       this.captchaId = res.result.captchaId;
    //       this.verifyCodeImg =
    //         imgPath + "/api/common/captcha/draw/" + this.captchaId;
    //     }
    //   });
    // }
  },
  mounted() {
    // this.getVerifyCode();
  }
};
</script>

<style lang="less">
@import "./regist.less";
@import "../styles/common.less";
</style>
