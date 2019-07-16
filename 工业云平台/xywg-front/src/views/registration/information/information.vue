<!--
 * @Author: zhangzhenya 
 * @CreateTime: 2019-02-22 14:28:05 
 * @Desc: 基本信息认证 
-->
<template>
  <div class="information">
    <div class="information__top">
      <h3 class="information__top-title">基本信息</h3>
      <Steps :current="0" class="information__top-steps" size="small">
        <Step title="基本信息"></Step>
        <Step title="企业信息"></Step>
        <Step title="人工审核"></Step>
        <Step title="认证结果"></Step>
      </Steps>
      <div class="information__top-tips" v-if="auditStatus !== 0 && auditStatus !== 3">
        <p>请完善以下信息,方便我们更好的服务</p>
      </div>
      <div class="information__top-tips3" v-if="auditStatus == 3">
        <p>根据提示修改基本信息，若没有提示继续修改企业信息</p>
      </div>
      <!-- <div class="information__top-tips2" v-if="auditStatus === 0" @click="changeToFixAuditStatus">
        <p>修改</p>
      </div>-->
    </div>
    <Form ref="inforForm" :model="inforForm" :label-width="120" :rules="inforFormValidate">
      <div class="information__titleBlock">
        <span>基本信息</span>
      </div>
      <Row type="flex" justify="start" class="information__form">
        <Col span="11">
          <FormItem label="上传身份证" prop="identityPhoto">
            <Tooltip placement="right" :always="true" :disabled="isIdentityError">
              <Upload
                ref="upload"
                :default-file-list="identityPhotoDefaultList1"
                :show-upload-list="false"
                :before-upload="handleFrontBeforeUpload"
                :on-success="frontHandleSuccess"
                :format="['jpg','jpeg','png']"
                :headers="accessToken"
                :max-size="10240"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                type="drag"
                action="/business/fileUpload/uploadPhoto"
                :data="otherPhotoData"
                name="uploadPhoto"
                style="display:inline-block;width:104px;"
              >
                <div class="information__frontUpload" v-if="frontUploadList.length == 0"></div>
                <div
                  class="information__uploadList"
                  v-for="(item,index) in frontUploadList"
                  :key="index"
                  else
                >
                  <template v-if="item.status === 'finished'">
                    <img :src="staticPath + item.response" class="information__uploadList-img">
                    <div class="information__uploadList-cover">
                      <Icon type="ios-eye-outline" @click.stop="handleView(item.response)"></Icon>
                      <Icon type="ios-trash-outline" @click.stop="frontHandleRemove(item)"></Icon>
                    </div>
                  </template>
                  <template v-else>
                    <i-progress v-if="item.showProgress" :percent="item.percentage" hide-info></i-progress>
                  </template>
                </div>
              </Upload>
              <Upload
                ref="upload"
                :default-file-list="identityPhotoDefaultList2"
                :show-upload-list="false"
                :before-upload="handleBackBeforeUpload"
                :on-success="backHandleSuccess"
                :format="['jpg','jpeg','png']"
                :headers="accessToken"
                :max-size="10240"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                type="drag"
                action="/business/fileUpload/uploadPhoto"
                :data="otherPhotoData"
                name="uploadPhoto"
                style="display:inline-block;width:104px;"
              >
                <div class="information__backUpload" v-if="backUploadList.length == 0"></div>
                <div
                  class="information__uploadList"
                  v-for="(item,index) in backUploadList"
                  :key="index"
                  else
                >
                  <template v-if="item.status === 'finished'">
                    <img :src="staticPath + item.response" class="information__uploadList-img">
                    <div class="information__uploadList-cover">
                      <Icon type="ios-eye-outline" @click.stop="handleView(item.response)"></Icon>
                      <Icon type="ios-trash-outline" @click.stop="backHandleRemove(item)"></Icon>
                    </div>
                  </template>
                  <template v-else>
                    <i-progress v-if="item.showProgress" :percent="item.percentage" hide-info></i-progress>
                  </template>
                </div>
              </Upload>
              <div slot="content" style="white-space: normal;">
                <p>{{this.identityError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="真实姓名" prop="name">
            <Input v-model="inforForm.name" :maxlength="20"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isNameError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.nameError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="身份证号" prop="identityNo">
            <Input v-model="inforForm.identityNo" :maxlength="85"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isIdentityNoError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.identityNoError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="职务" prop="staff">
            <Input v-model="inforForm.staff" :maxlength="50"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isStaffError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.staffError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="来源" prop="source">
            <Select v-model="inforForm.source">
              <Option :value="1">周围人推荐</Option>
              <Option :value="2">已建立合作</Option>
              <Option :value="3">新闻报道</Option>
              <Option :value="4">搜索引擎</Option>
              <Option :value="5">会议会展</Option>
              <Option :value="6">其他</Option>
            </Select>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isSourceError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.sourceError}}</p>
              </div>
            </Tooltip>
          </FormItem>
        </Col>
      </Row>
      <div class="information__titleBlock">
        <span>联系信息</span>
      </div>
      <Row type="flex" justify="start" class="information__form">
        <Col span="11">
          <FormItem label="所在地区" prop="area">
            <Row type="flex" justify="space-between">
              <Col span="7">
                <FormItem prop="province">
                  <!-- <Select
                    v-model="inforForm.province"
                    @on-open-change="provinceOpen"
                    @on-change="provinceChange"
                  >-->
                  <Select v-model="inforForm.province" @on-change="provinceChange">
                    <Option
                      :value="item.id"
                      v-for="(item,index) in provinceList"
                      :key="index"
                    >{{item.areaName}}</Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="7">
                <FormItem prop="city">
                  <Select
                    v-model="inforForm.city"
                    prop="city"
                    @on-open-change="cityOpen"
                    @on-change="cityChange"
                  >
                    <Option
                      :value="item.id"
                      v-for="(item,index) in cityList"
                      :key="index"
                    >{{item.areaName}}</Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="9">
                <FormItem prop="district">
                  <Select
                    v-model="inforForm.district"
                    prop="district"
                    @on-open-change="districtOpen"
                    @on-change="districtChange"
                  >
                    <Option
                      :value="item.id"
                      v-for="(item,index) in districtList"
                      :key="index"
                    >{{item.areaName}}</Option>
                  </Select>
                </FormItem>
              </Col>
            </Row>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isAreaError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.areaError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="街道地址" prop="street">
            <Input v-model="inforForm.street" :maxlength="50"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isStreetError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.streetError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="联系电话" prop="tel">
            <Input v-model="inforForm.tel" :maxlength="13" placeholder="请填写联系电话或手机号"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isTelError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.telError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="电子邮件" prop="email">
            <Input v-model="inforForm.email" :maxlength="50"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isEmailError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.emailError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="传真" prop="fax">
            <Input v-model="inforForm.fax" :maxlength="12"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isFaxError"
              class="information__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.faxError}}</p>
              </div>
            </Tooltip>
          </FormItem>
        </Col>
      </Row>
      <Button
        v-if="auditStatus != 3"
        @click="handleSubmit"
        :loading="submitLoading"
        type="primary"
        shape="circle"
        class="information__submit"
      >暂存,继续填写企业信息</Button>
      <!-- <Button
        v-if="auditStatus === ''"
        @click="handleEdit"
        :loading="submitLoading"
        type="primary"
        shape="circle"
        class="information__submit"
      >修改基本信息</Button>-->
      <Button
        v-if="auditStatus == 3"
        @click="handleToEnterprise"
        :loading="submitLoading"
        type="error"
        shape="circle"
        class="information__submit"
      >继续修改企业信息</Button>
    </Form>
    <!-- <Form
      :label-width="120"
      :model="indInforForm"
      v-show="auditStatus === 0"
      ref="indInforForm"
      :rules="indInforFormValidate"
    >
      <div class="result__titleBlock">
        <span>基本信息</span>
      </div>
      <Row type="flex" justify="start" class="result__form">
        <Col span="11">
          <FormItem label="真实姓名" prop="name">
            <p class="result__form-content">{{indInforForm.name}}</p>
          </FormItem>
          <FormItem label="身份证号">
            <p class="result__form-content">{{indInforForm.identityNo}}</p>
          </FormItem>
          <FormItem label="职务">
            <p class="result__form-content">{{indInforForm.staff}}</p>
          </FormItem>
          <FormItem label="来源">
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
          <FormItem label="所在地区">
            <p class="result__form-content">
              <span>{{indInforForm.provinceName?indInforForm.provinceName:''}}</span>
              <span>{{indInforForm.cityName?indInforForm.cityName:''}}</span>
              <span>{{indInforForm.districtName?indInforForm.districtName:''}}</span>
            </p>
          </FormItem>
          <FormItem label="街道地址">
            <p class="result__form-content">{{indInforForm.street?indInforForm.street:''}}</p>
          </FormItem>
          <FormItem label="联系电话">
            <p class="result__form-content">{{indInforForm.tel?indInforForm.tel:''}}</p>
          </FormItem>
          <FormItem label="电子邮件">
            <p class="result__form-content">{{indInforForm.email?indInforForm.email:""}}</p>
          </FormItem>
          <FormItem label="传真">
            <p class="result__form-content">{{indInforForm.fax?indInforForm.fax:""}}</p>
          </FormItem>
        </Col>
      </Row>
      <Button
        @click="routerToEnterprise"
        :loading="submitLoading"
        type="primary"
        shape="circle"
        class="information__submit"
      >下一步</Button>
    </Form>-->
    <modal title="预览" v-model="imgVisible">
      <img :src="staticPath + imgName" v-if="imgVisible" style="width: 100%">
    </modal>
  </div>
</template>

<script>
import { getStore, setStore } from "../../../utils/storage";
import { staticPath } from "../../../../src/libs/base.js";
import fileToBase64 from "fileToBase64";
import qs from "qs";
export default {
  data() {
    const validateConfirmTel = (rule, value, callback) => {
      // let reg1 = /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/;
      // let reg2 = /^1[0-9]{10}$/;
      let reg = /^((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/;
      if (!this.isDigit(value)) {
        callback(new Error("联系电话只能填数字"));
      } else if (!reg.test(value)) {
        callback(new Error("请输入正确的号码:区号-电话号码/手机号"));
      } else {
        callback();
      }
    };
    //自定义校验
    const validateIdentityNo = (rule, value, callback) => {
      let reg = /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}$)/;
      if (value == "") {
        callback(new Error("身份证号码不为空"));
      } else if (!reg.test(value)) {
        callback(new Error("请输入正确的身份证号码"));
      } else {
        callback();
      }
    };
    const validateArea = (rule, value, callback) => {
      if (
        !this.inforForm.province ||
        !this.inforForm.city ||
        !this.inforForm.district ||
        this.inforForm.province == "" ||
        this.inforForm.city == "" ||
        this.inforForm.district == ""
      ) {
        callback(new Error("请选择正确的地址"));
      } else if (
        this.inforForm.province &&
        this.inforForm.city &&
        this.inforForm.district &&
        this.inforForm.province != "" &&
        this.inforForm.city != "" &&
        this.inforForm.district != ""
      ) {
        callback();
      }
    };
    const validateIdentityPhoto = (rule, value, callback) => {
      if (
        this.frontUploadList.length > 0 &&
        this.backUploadList.length > 0 &&
        this.hasFront &&
        this.hasBack
      ) {
        callback();
      }
      // else if (
      //   this.inforForm.name !== this.trueName ||
      //   this.inforForm.identityNo !== this.trueIdentifyNo
      // ) {
      //   console.log(this.identityPhotoList);
      //   this.frontUploadList = [];
      //   this.identityPhotoList[0] = "";
      //   callback(new Error("实名信息有误,请修改实名信息后重新上传"));
      // }
      else {
        callback(new Error("请上传完整的身份证信息"));
      }
    };
    return {
      inforForm: {
        // type: "", //会员身份
        name: "", //真实姓名
        // productName: "", //行业应用
        // majorBusiness: "", //主营业务
        // website: "", //网址
        identityNo: "", //身份证号
        identityPhoto: "",
        staff: "", //职务
        source: "", //来源
        province: "", //省份
        city: "", //城市
        district: "", //区
        street: "", //街道
        tel: "",
        email: "",
        fax: "", //传真
        userId: JSON.parse(this.cookies.get("userInfo")).id, //用户id
        createUser: JSON.parse(this.cookies.get("userInfo")).id, //创建人id
        createUserName: JSON.parse(this.cookies.get("userInfo")).username //创建人名称
      },
      identityPhotoDefaultList1: [],
      identityPhotoDefaultList2: [],
      indInforFormValidate: {
        name: [
          {
            required: false
          }
        ]
      },
      inforFormValidate: {
        name: [
          {
            required: true,
            message: "真实姓名不为空"
          }
        ],
        identityNo: [
          {
            required: true,
            validator: validateIdentityNo
          }
        ],
        area: [{ required: true, validator: validateArea }],
        identityPhoto: [
          {
            required: true,
            validator: validateIdentityPhoto,
            trigger: "blur"
          }
        ],
        street: [
          {
            required: true,
            message: "街道不为空",
            trigger: "blur"
          }
        ],
        tel: [
          {
            required: true,
            message: "联系电话不为空",
            trigger: "blur"
          },
          { validator: validateConfirmTel, trigger: "blur" }
        ],
        email: [
          {
            required: true,
            message: "邮箱不为空",
            trigger: "blur"
          },
          { type: "email", message: "邮箱格式有误", trigger: "blur" }
        ]
      },
      provinceList: [],
      cityList: [],
      districtList: [],
      productTypeList: [],
      submitLoading: false,
      accessToken: {
        accessToken: getStore("accessToken")
      },
      frontUploadList: [],
      backUploadList: [],
      otherPhotoData: {
        type: "enterprise",
        userName: JSON.parse(this.cookies.get("userInfo")).username
      },
      staticPath: staticPath,
      imgVisible: false,
      imgName: "",
      identityPhotoList: [],
      hasFront: false,
      hasBack: false,
      status: null,
      comInforForm: {},
      indInforForm: {},
      auditStatus: "",
      isIdentityError: true,
      identityError: "",
      isNameError: true,
      nameError: "",
      isIdentityNoError: true,
      identityNoError: "",
      isStaffError: true,
      staffError: "",
      isSourceError: true,
      sourceError: "",
      isAreaError: true,
      areaError: "",
      isStreetError: true,
      streetError: "",
      isTelError: true,
      telError: "",
      isEmailError: true,
      emailError: "",
      isFaxError: true,
      faxError: ""
      // trueName: "",
      // trueIdentifyNo: ""
    };
  },
  methods: {
    isDigit(s) {
      var patrn = /^[0-9//-]{1,20}$/;
      if (!patrn.exec(s)) {
        return false;
      }
      return true;
    },
    //获取省市区
    // getAreas(id) {
    //   this.postBusRequest("/areas/getAreas", { parentId: id }).then(res => {
    //     this.areaList = res;
    //   });
    // },
    //获取省份
    provinceOpen() {
      this.provinceList = [];
      this.postBusRequest("/areas/getAreas", { parentId: 0 }).then(res => {
        this.provinceList = res;
      });
    },
    //获取城市
    cityOpen(open) {
      this.cityList = [];
      if (this.inforForm.province) {
        this.postBusRequest("/areas/getAreas", {
          parentId: this.inforForm.province
        }).then(res => {
          this.cityList = res;
        });
      }
    },
    //获取区县
    districtOpen(open) {
      this.districtList = [];
      if (this.inforForm.city) {
        this.postBusRequest("/areas/getAreas", {
          parentId: this.inforForm.city
        }).then(res => {
          this.districtList = res;
        });
      }
    },
    //选择省份
    provinceChange(value) {
      console.log(value);
      this.inforForm.city = "";
      this.inforForm.district = "";
      this.$refs.inforForm.validateField("area");
      // this.areaList = [];
    },
    //选择城市
    cityChange(value) {
      console.log(value);
      this.inforForm.district = "";
      this.$refs.inforForm.validateField("area");
      // this.areaList = [];
    },
    //获取区县
    districtChange(value) {
      console.log(value);
      this.$refs.inforForm.validateField("area");
    },
    //提交基本信息
    insertEnterpriseCertification() {
      // this.inforForm
      if (this.hasFront && this.hasBack) {
        this.inforForm.identityPhoto = this.identityPhotoList.join();
        console.log(this.inforForm);
        this.postBusRequest(
          "/accountInfoIndividual/insert",
          this.inforForm
        ).then(res => {
          console.log(res);
          // this.operateSystemList = res;
          if (res.code == 200) {
            this.$router.push({
              name: "authenticate",
              query: { route: "enterprise" }
            });
          }
        });
      }
    },
    //修改基本信息
    editInformation() {
      if (this.hasFront && this.hasBack) {
        this.inforForm.identityPhoto = this.identityPhotoList.join();
        this.postBusRequest(
          "/accountInfoIndividual/updateBaseInfo",
          this.inforForm
        ).then(res => {
          console.log(res);
          // this.operateSystemList = res;
          if (res.code == 200) {
            this.$router.push({
              name: "authenticate",
              query: { route: "enterprise" }
            });
          }
        });
      }
    },
    //触发提交基本信息
    handleSubmit() {
      this.$refs.inforForm.validate(valid => {
        if (valid) {
          if (this.auditStatus === "") {
            this.insertEnterpriseCertification();
          } else if (this.auditStatus === 0) {
            this.editInformation();
          }
        }
      });
    },
    //触发修改基本信息
    // handleEdit() {
    //   this.$refs.inforForm.validate(valid => {
    //     if (valid) {
    //       this.editInformation();
    //     }
    //   });
    // },
    //上传格式错误校验
    handleFormatError(file) {
      this.$Notice.warning({
        title: "文件格式出错",
        desc: file.name + "的文件格式不正确, 请选择png、jpg或者jpeg."
      });
    },
    //文件尺寸过大校验
    handleMaxSize(file) {
      this.$Notice.warning({
        title: "文件尺寸过大",
        desc: "文件" + file.name + " 超过了10M."
      });
    },
    //身份证正面移除
    frontHandleRemove(file) {
      this.frontUploadList = [];
      this.hasFront = false;
    },
    //身份证背面移除
    backHandleRemove(file) {
      this.backUploadList = [];
      this.hasBack = false;
    },
    //身份证正面上传
    handleFrontBeforeUpload(file) {
      let _this = this;
      fileToBase64(file, base64 => {
        let params = {
          detect_direction: true,
          id_card_side: "front",
          image: base64,
          detect_risk: true
        };
        _this.getBaiduToken().then(res => {
          _this
            .postBaiduIDcard(res.access_token, qs.stringify(params))
            .then(_res => {
              console.log(_res);
              if (_res.image_status == "normal") {
                _this.hasFront = true;
                _this.inforForm.name = _res.words_result["姓名"].words;
                _this.inforForm.identityNo =
                  _res.words_result["公民身份号码"].words;
                // _this.trueName = _res.words_result["姓名"].words;
                // _this.trueIdentifyNo = _res.words_result["公民身份号码"].words;
                _this.$refs.inforForm.validateField("identityPhoto");
              } else if (_res.image_status != "normal") {
                _this.hasFront = false;
                _this.frontUploadList = [];
                _this.identityPhotoList[0] = "";
                _this.$Message.error("提交了错误的身份证信息");
                _this.$refs.inforForm.validateField("identityPhoto");
                return false;
              }
            });
        });
      });
    },
    //身份证背面上传
    handleBackBeforeUpload(file) {
      let _this = this;
      fileToBase64(file, base64 => {
        let params = {
          detect_direction: true,
          id_card_side: "back",
          image: base64,
          detect_risk: true
        };
        _this.getBaiduToken().then(res => {
          _this
            .postBaiduIDcard(res.access_token, qs.stringify(params))
            .then(_res => {
              console.log(_res);
              if (_res.image_status == "normal") {
                _this.hasBack = true;
                _this.$refs.inforForm.validateField("identityPhoto");
              } else if (_res.image_status != "normal") {
                _this.hasBack = false;
                _this.backUploadList = [];
                _this.identityPhotoList[1] = "";
                _this.$Message.error("提交了错误的身份证信息");
                _this.$refs.inforForm.validateField("identityPhoto");
                return false;
              }
            });
        });
      });
    },
    //身份证正面上传成功回调
    frontHandleSuccess(res, file, fileList) {
      console.log(res);
      this.frontUploadList = [];
      this.frontUploadList.push(file);
      this.identityPhotoList[0] = res;
      this.$refs.inforForm.validateField("identityPhoto");
      this.$refs.inforForm.validateField("name");
      this.$refs.inforForm.validateField("identityNo");
    },
    //身份证背面上传成功回调
    backHandleSuccess(res, file, fileList) {
      console.log(res);
      this.backUploadList = [];
      this.backUploadList.push(file);
      this.identityPhotoList[1] = res;
      this.$refs.inforForm.validateField("identityPhoto");
    },
    //身份证预览
    handleView(name) {
      this.imgName = name;
      this.imgVisible = true;
    },
    //获取认证状态
    getAuditStatus() {
      this.getBusRequest("/accountInfoIndividual/selectByUserId", {
        userId: JSON.parse(this.cookies.get("userInfo")).id
      }).then(_res => {
        console.log(_res);
        if (_res.code == 200 || _res.code == "200") {
          if (!_res.data) {
          } else if (_res.data) {
            this.auditStatus = _res.data.auditStatus;
            if (_res.data.auditStatus == 0) {
              this.auditStatus = 0;
              // this.indInforFormValidate = {};
              // this.indInforForm = _res.data;
              this.$store.commit("removeTag", "authenticate");
              this.$router.push({
                name: "authenticate",
                query: { router: "information" }
              });
              this.inforForm = {
                id: _res.data.id,
                name: _res.data.name, //真实姓名
                identityNo: _res.data.identityNo,
                identityPhoto: _res.data.identityPhoto,
                staff: _res.data.staff, //职务
                source: _res.data.source, //来源
                province: _res.data.province, //省份
                city: _res.data.city, //城市
                district: _res.data.district, //区
                street: _res.data.street, //街道
                tel: _res.data.tel,
                email: _res.data.email,
                fax: _res.data.fax, //传真
                userId: JSON.parse(this.cookies.get("userInfo")).id, //用户id
                createUser: JSON.parse(this.cookies.get("userInfo")).id, //创建人id
                createUserName: JSON.parse(this.cookies.get("userInfo"))
                  .username //创建人名称
              };
              console.log("222", this.inforForm);
              let arr = this.inforForm.identityPhoto.split(",");
              this.identityPhotoDefaultList1 = [
                {
                  name: "identityPhoto1",
                  url: this.staticPath + arr[0]
                }
              ];
              this.frontUploadList = [
                {
                  response: arr[0],
                  status: "finished"
                }
              ];
              this.identityPhotoDefaultList2 = [
                {
                  name: "identityPhoto2",
                  url: this.staticPath + arr[1]
                }
              ];
              this.backUploadList = [
                {
                  response: arr[1],
                  status: "finished"
                }
              ];
              this.identityPhotoList = this.inforForm.identityPhoto.split(",");
              this.hasFront = true;
              this.hasBack = true;
              this.cityOpen();
              this.districtOpen();
              console.log("111", this.inforForm);
            } else if (
              _res.data.auditStatus &&
              _res.data.auditStatus != 0 &&
              !this.$store.state.authenticate.isFix
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
            } else if (
              this.auditStatus === 3 &&
              this.$store.state.authenticate.isFix
            ) {
              this.getBusRequest(
                "/accountInfoCompany/getEnterpriseCertification",
                {
                  userId: JSON.parse(this.cookies.get("userInfo")).id
                }
              ).then(res => {
                console.log(res.data);
                if (res.code == 200 || res.code == "200") {
                  this.inforForm = {
                    id: res.data.individual.id,
                    name: res.data.individual.name, //真实姓名
                    identityNo: res.data.individual.identityNo,
                    identityPhoto: res.data.individual.identityPhoto,
                    staff: res.data.individual.staff, //职务
                    source: res.data.individual.source, //来源
                    province: res.data.individual.province, //省份
                    city: res.data.individual.city, //城市
                    district: res.data.individual.district, //区
                    street: res.data.individual.street, //街道
                    tel: res.data.individual.tel,
                    email: res.data.individual.email,
                    fax: res.data.individual.fax, //传真
                    userId: JSON.parse(this.cookies.get("userInfo")).id, //用户id
                    createUser: JSON.parse(this.cookies.get("userInfo")).id, //创建人id
                    createUserName: JSON.parse(this.cookies.get("userInfo"))
                      .username //创建人名称
                  };
                  console.log(this.inforForm);
                  let arr = this.inforForm.identityPhoto.split(",");
                  console.log(arr);
                  this.identityPhotoDefaultList1 = [
                    {
                      name: "identityPhoto1",
                      url: this.staticPath + arr[0]
                    }
                  ];
                  this.frontUploadList = [
                    {
                      response: arr[0],
                      status: "finished"
                    }
                  ];
                  this.identityPhotoDefaultList2 = [
                    {
                      name: "identityPhoto2",
                      url: this.staticPath + arr[1]
                    }
                  ];
                  this.backUploadList = [
                    {
                      response: arr[1],
                      status: "finished"
                    }
                  ];
                  this.identityPhotoList = this.inforForm.identityPhoto.split(
                    ","
                  );
                  this.hasFront = true;
                  this.hasBack = true;
                  this.cityOpen();
                  this.districtOpen();
                  var arr = Object.keys(
                    JSON.parse(res.data.company.rejectReason)
                  );
                  var obj = JSON.parse(res.data.company.rejectReason);
                  arr.forEach(el => {
                    if (el == "name") {
                      this.isNameError = false;
                      this.nameError = obj[el];
                    } else if (el == "identityPhoto") {
                      this.isIdentityError = false;
                      this.identityError = obj[el];
                    } else if (el == "identityNo") {
                      this.isIdentityNoError = false;
                      this.identityNoError = obj[el];
                    } else if (el == "staff") {
                      this.isStaffError = false;
                      this.staffError = obj[el];
                    } else if (el == "source") {
                      this.isSourceError = false;
                      this.sourceError = obj[el];
                    } else if (el == "province") {
                      this.isAreaError = false;
                      this.areaError = obj[el];
                    } else if (el == "street") {
                      this.isStreetError = false;
                      this.streetError = obj[el];
                    } else if (el == "tel") {
                      this.isTelError = false;
                      this.telError = obj[el];
                    } else if (el == "email") {
                      this.isEmailError = false;
                      this.emailError = obj[el];
                    } else if (el == "fax") {
                      this.isFaxError = false;
                      this.faxError = obj[el];
                    }
                  });
                }
              });
            }
          }
        }
      });
    },
    //路由跳转到企业信息
    routerToEnterprise() {
      this.$router.push({
        name: "authenticate",
        query: { route: "enterprise" }
      });
    },
    //修改时校验触发路由跳转到企业信息
    handleToEnterprise() {
      this.$refs.inforForm.validate(valid => {
        if (valid) {
          console.log(this.inforForm);
          if (this.hasFront && this.hasBack) {
            this.inforForm.identityPhoto = this.identityPhotoList.join();
          }
          this.postBusRequest(
            "/accountInfoIndividual/updateBaseInfo",
            this.inforForm
          ).then(res => {
            if (res.code == 200) {
              if (
                this.inforForm.identityPhoto &&
                this.inforForm.identityPhoto !== ""
              ) {
                this.$router.push({
                  name: "authenticate",
                  query: { route: "enterprise" }
                });
              }
            }
          });
          this.cookies.set("informationForm", this.inforForm);
          // console.log(this.cookies.get("informationForm"));
        }
      });
    }
    // changeToFixAuditStatus() {
    //   this.auditStatus = "";
    // }
  },
  mounted() {
    // this.$store.state.authenticate.isFix = false;
    this.provinceOpen();
    this.getAuditStatus();
  }
};
</script>

<style lang="less" scoped>
@import "./information.less";
@import "../result/result.less";
</style>
