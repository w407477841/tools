<style lang="less">
@import "./own-space.less";
</style>

<template>
  <div class="own-space">
    <Card>
      <p slot="title">
        <Icon type="person"></Icon>个人信息
      </p>
      <div>
        <Form ref="userForm" :model="userForm" :label-width="100" label-position="right">
          <FormItem label="用户头像：">
            <div class="upload-list" v-for="item in uploadList" :key="item.url">
              <template v-if="item.status === 'finished'">
                <img :src="staticPath + item.url">
                <div class="upload-list-cover">
                  <Icon type="ios-eye-outline" @click.native="handleView(item.url)"></Icon>
                  <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
                </div>
              </template>
              <template v-else>
                <Progress v-if="item.showProgress" :percent="item.percentage" hide-info></Progress>
              </template>
            </div>
            <Upload
              ref="upload"
              :show-upload-list="false"
              :default-file-list="defaultList"
              :on-success="handleSuccess"
              :format="['jpg','jpeg','png']"
              :max-size="10240"
              :on-format-error="handleFormatError"
              :on-exceeded-size="handleMaxSize"
              :before-upload="handleBeforeUpload"
              type="drag"
              action="/business/fileUpload/uploadPhoto"
              :headers="accessToken"
              :data="otherPhotoData"
              name="uploadPhoto"
              style="display: inline-block;width:58px;"
            >
              <div style="width: 58px;height:58px;line-height: 58px;">
                <Icon type="camera" size="20"></Icon>
              </div>
            </Upload>
          </FormItem>
          <FormItem label="用户账号：">
            <span>{{userForm.username}}</span>
          </FormItem>
          <FormItem label="性别：">
            <RadioGroup v-model="userForm.sex">
              <Radio :label="1">男</Radio>
              <Radio :label="0">女</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="手机号：">
            <span>{{userForm.mobile}}</span>
            <div style="display:inline-block;margin-left:20px;font-size:13px;">
              <a @click="showChangeMobile">修改手机号</a>
            </div>
          </FormItem>
          <FormItem label="邮箱：">
            <span>{{userForm.email}}</span>
            <div style="display:inline-block;margin-left:20px;font-size:13px;">
              <a @click="showChangeEmail">修改邮箱</a>
            </div>
          </FormItem>
          <FormItem label="地址：">
            <al-cascader
              v-model="userForm.addressArray"
              @on-change="changeAddress"
              data-type="code"
              level="2"
              style="width:250px"
            />
          </FormItem>
          <FormItem label="用户类型：">
            <span>{{ userForm.typeTxt }}</span>
          </FormItem>
          <FormItem label="创建时间：">
            <span>{{ userForm.createTime }}</span>
          </FormItem>
          <FormItem>
            <Button type="primary" style="width: 100px;" :loading="saveLoading" @click="saveEdit">保存</Button>
            <Button type="ghost" @click="cancelEditUserInfo">取消</Button>
          </FormItem>
        </Form>
      </div>
    </Card>
    <Modal
      title="修改手机号"
      v-model="editMobileVisible"
      :closable="false"
      :mask-closable="false"
      :width="500"
    >
      <Form
        ref="mobileEditForm"
        :model="mobileEditForm"
        :label-width="60"
        :rules="mobileEditValidate"
      >
        <FormItem label="手机号" prop="mobile">
          <Input v-model="mobileEditForm.mobile" @on-change="hasChangePhone" placeholder="请输入新手机号"></Input>
        </FormItem>
        <FormItem label="验证码" prop="code" :error="codeError">
          <Input v-model="mobileEditForm.code" placeholder="请输入您收到的短信验证码" style="width:200px;"></Input>
          <div style="display:inline-block;position:relative;">
            <Button
              @click="getIdentifyCode"
              :disabled="canGetIdentifyCode"
            >{{ gettingIdentifyCodeBtnContent }}</Button>
          </div>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelInputCodeBox">取消</Button>
        <Button type="primary" :loading="checkIdentifyCodeLoading" @click="submitCode">提交</Button>
      </div>
    </Modal>
    <Modal
      title="修改邮箱"
      v-model="editEmailVisible"
      :closable="false"
      :mask-closable="false"
      :width="500"
    >
      <Form ref="emailEditForm" :model="emailEditForm" :label-width="90" :rules="emailEditValidate">
        <FormItem label="新邮箱地址" prop="email">
          <Input v-model="emailEditForm.email" @on-change="hasChangeEmail" placeholder="请输入新邮箱地址"></Input>
        </FormItem>
        <FormItem label="验证码" prop="code" :error="codeError">
          <Input v-model="mobileEditForm.code" placeholder="请输入您收到的邮箱验证码" style="width:200px;"></Input>
          <div style="display:inline-block;position:relative;">
            <Button
              @click="sendVerifyEmail"
              :disabled="canSendEditEmail"
            >{{ gettingIdentifyCodeBtnContent }}</Button>
          </div>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelEditEmail">取消</Button>
        <Button
          type="primary"
          :disabled="canSendEditEmail"
          :loading="sendVerifyEmailLoading"
          @click="submitMailCode"
        >提交</Button>
      </div>
    </Modal>
    <Modal title="图片预览" v-model="viewImage">
      <img :src="staticPath + imgUrl" style="width: 80%;margin: 0 auto;display: block;">
    </Modal>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import { getStore } from "../../utils/storage";
import { staticPath } from "../../../src/libs/base.js";
export default {
  name: "ownspace_index",
  data() {
    const validePhone = (rule, value, callback) => {
      var re = /^1[0-9]{10}$/;
      if (!re.test(value)) {
        callback(new Error("请输入正确格式的手机号"));
      } else {
        callback();
      }
    };
    return {
      accessToken: {},
      userForm: {
        id: "",
        avatar: "",
        username: "",
        sex: "",
        mobile: "",
        email: "",
        status: "",
        type: "",
        typeTxt: "",
        address: "",
        addressArray: []
      },
      mobileEditForm: {
        mobile: "",
        code: ""
      },
      emailEditForm: {
        email: ""
      },
      defaultList: [
        {
          url: ""
        }
      ],
      uploadList: [],
      viewImage: false,
      imgUrl: "",
      codeError: "",
      initPhone: "",
      initEmail: "",
      uid: "", // 登录用户的userId
      saveLoading: false,
      identifyError: "", // 验证码错误
      savePassLoading: false,
      oldPassError: "",
      identifyCodeRight: false, // 验证码是否正确
      // hasGetIdentifyCode: false, // 是否点了获取验证码
      canGetIdentifyCode: false, // 是否可点获取验证码
      checkIdentifyCodeLoading: false,
      mobileEditValidate: {
        mobile: [
          { required: true, message: "请输入手机号码" },
          { validator: validePhone }
        ]
      },
      emailEditValidate: {
        email: [
          { required: true, message: "请输入邮箱地址" },
          { type: "email", message: "邮箱格式不正确" }
        ]
      },
      editMobileVisible: false, // 显示填写验证码box
      gettingIdentifyCodeBtnContent: "获取验证码", // “获取验证码”按钮的文字
      editEmailVisible: false,
      sendVerifyEmailLoading: false,
      canSendEditEmail: true,
      otherPhotoData: {
        type: "useravatar",
        userName: JSON.parse(this.cookies.get("userInfo")).username
      },
      staticPath: staticPath
    };
  },
  methods: {
    init() {
      this.accessToken = {
        accessToken: getStore("accessToken")
      };
      let userInfo = JSON.parse(Cookies.get("userInfo"));
      userInfo.addressArray = [];
      this.userForm = userInfo;
      this.initPhone = userInfo.mobile;
      this.mobileEditForm.mobile = userInfo.mobile;
      this.initEmail = userInfo.email;
      this.emailEditForm.email = userInfo.email;
      this.defaultList[0].url = userInfo.avatar;
      if (userInfo.address !== null && userInfo.address !== "") {
        this.userForm.address = userInfo.address;
        this.userForm.addressArray = JSON.parse(userInfo.address);
      }
      if (this.userForm.type === 0) {
        this.userForm.typeTxt = "普通用户";
      } else if (this.userForm.type === 1) {
        this.userForm.typeTxt = "管理员";
      }
    },
    handleView(imgUrl) {
      this.imgUrl = imgUrl;
      this.viewImage = true;
    },
    handleRemove(file) {
      const fileList = this.$refs.upload.fileList;
      this.$refs.upload.fileList.splice(fileList.indexOf(file), 1);
    },
    handleSuccess(res, file) {
      if (res) {
        file.url = res;
        this.userForm.avatar = res;
        this.$refs.upload.fileList.splice(0, 1);
        this.defaultList[0].url = res;
        this.userForm.avatar = res;
      }
    },
    handleFormatError(file) {
      this.$Notice.warning({
        title: "不支持的文件格式",
        desc:
          "所选文件‘ " +
          file.name +
          " ’格式不正确, 请选择 .jpg .jpeg .png格式文件"
      });
    },
    handleMaxSize(file) {
      this.$Notice.warning({
        title: "文件大小过大",
        desc: "所选文件‘ " + file.name + " ’大小过大, 不得超过 10M."
      });
    },
    handleBeforeUpload() {
      const check = this.uploadList.length < 2;
      if (!check) {
        this.$Notice.warning({
          title: "最多只能上传 1 张图片"
        });
      }
      return check;
    },
    showChangeMobile() {
      this.editMobileVisible = true;
    },
    showChangeEmail() {
      this.editEmailVisible = true;
    },
    getIdentifyCode() {
      // this.hasGetIdentifyCode = true;
      this.$refs["mobileEditForm"].validate(valid => {
        if (valid) {
          this.canGetIdentifyCode = false;
          let timeLast = 60;
          let timer = setInterval(() => {
            if (timeLast >= 0) {
              this.gettingIdentifyCodeBtnContent = timeLast + "秒后重试";
              timeLast -= 1;
            } else {
              clearInterval(timer);
              this.gettingIdentifyCodeBtnContent = "获取验证码";
              this.canGetIdentifyCode = false;
            }
          }, 1000);
          this.editMobileVisible = true;
          // you can write ajax request here
          this.postRequest("/common/captcha/smsCode", {
            mobile: this.mobileEditForm.mobile,
            type: 3 //注册是0 登录是1 修改手机号是3
          }).then(res => {
            console.log(res);
            if (res.code == 200) {
              clearInterval(timer);
            }
          });
        }
      });
    },
    cancelEditUserInfo() {
      this.$store.commit("removeTag", "ownspace_index");
      localStorage.pageOpenedList = JSON.stringify(
        this.$store.state.app.pageOpenedList
      );
      let lastPageName = "";
      if (this.$store.state.app.pageOpenedList.length > 1) {
        lastPageName = this.$store.state.app.pageOpenedList[1].name;
      } else {
        lastPageName = this.$store.state.app.pageOpenedList[0].name;
      }
      this.$router.push({
        name: lastPageName
      });
    },
    saveEdit() {
      this.saveLoading = true;
      let params = this.userForm;
      delete params.nickName;
      delete params.description;
      this.postRequest("/user/edit", params).then(res => {
        this.saveLoading = false;
        if (res.success === true) {
          this.$Message.success("保存成功");
          // 更新用户信息
          Cookies.set("userInfo", this.userForm);
          // 更新头像
          this.$store.commit("setAvatarPath", this.userForm.avatar);
        }
      });
    },
    changeAddress() {
      this.userForm.address = JSON.stringify(this.userForm.addressArray);
    },
    cancelInputCodeBox() {
      this.editMobileVisible = false;
      this.userForm.mobile = this.initPhone;
    },
    cancelEditEmail() {
      this.editEmailVisible = false;
      this.emailEditForm.email = this.initEmail;
    },
    submitCode() {
      this.checkIdentifyCodeLoading = true;
      this.$refs["mobileEditForm"].validate(valid => {
        if (valid) {
          if (this.mobileEditForm.code.length === 0) {
            this.codeError = "请填写短信验证码";
            this.checkIdentifyCodeLoading = false;
            return;
          } else {
            this.postRequest("/user/editInfo", {
              newMobile: this.mobileEditForm.mobile,
              code: this.mobileEditForm.code,
              username: this.userForm.username,
              id: this.userForm.id
            }).then(res => {
              console.log(res);
              if (res.code == 200) {
                this.$Message.success("修改成功");
                this.editMobileVisible = false;
                this.checkIdentifyCodeLoading = false;
                this.userForm.mobile = this.mobileEditForm.mobile;
                this.mobileEditForm.code = "";
                this.initPhone = this.mobileEditForm.mobile;
              }
            });
          }
        }
      });
    },
    submitMailCode() {
      this.sendVerifyEmailLoading = true;
      this.$refs["mobileEditForm"].validate(valid => {
        if (valid) {
          if (this.mobileEditForm.code.length === 0) {
            this.codeError = "请填写短信验证码";
            this.sendVerifyEmailLoading = false;
            return;
          } else {
            this.postRequest("/user/editInfo", {
              newMail: this.emailEditForm.email,
              code: this.emailEditForm.code,
              username: this.userForm.username,
              id: this.userForm.id
            }).then(res => {
              console.log(res);
              if (res.code == 200) {
                this.$Message.success("修改成功");
                this.editEmailVisible = false;
                this.sendVerifyEmailLoading = false;
                this.userForm.email = this.emailEditForm.email;
                this.emailEditForm.code = "";
                this.initEmail = this.emailEditForm.email;
              }
            });
          }
        }
      });
    },
    hasChangePhone() {
      if (this.mobileEditForm.mobile === this.initPhone) {
        this.canGetIdentifyCode = true;
      } else {
        this.$refs["mobileEditForm"].validate(valid => {
          if (valid) {
            this.canGetIdentifyCode = false;
          } else {
            this.canGetIdentifyCode = true;
          }
        });
      }
    },
    hasChangeEmail() {
      if (this.emailEditForm.email === this.initEmail) {
        this.canSendEditEmail = true;
      } else {
        this.canSendEditEmail = false;
      }
    },
    sendVerifyEmail() {
      this.$refs["emailEditForm"].validate(valid => {
        if (valid) {
          let timeLast = 60;
          let timer = setInterval(() => {
            if (timeLast >= 0) {
              this.gettingIdentifyCodeBtnContent = timeLast + "秒后重试";
              timeLast -= 1;
            } else {
              clearInterval(timer);
              this.gettingIdentifyCodeBtnContent = "获取验证码";
              this.canSendEditEmail = false;
            }
          }, 1000);
          this.postRequest("/common/captcha/smsMailCode", {
            mail: this.emailEditForm.email,
            type: 0
          }).then(res => {
            console.log(res);
            if (res.code == 200) {
              this.$Message.success("发送验证邮件成功，请注意查收");
            }
          });
        }
      });
    }
  },
  mounted() {
    this.uploadList = this.$refs.upload.fileList;
    this.init();
    this.hasChangePhone();
  }
};
</script>
