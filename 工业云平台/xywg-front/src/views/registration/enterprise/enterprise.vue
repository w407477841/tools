 <!--
  * @Author: zhangzhenya 
  * @CreateTime: 2019-02-22 14:27:44 
  * @Desc:  企业认证
 -->
<template>
  <div class="enterprise">
    <div class="enterprise__top">
      <h3 class="enterprise__top-title">企业认证</h3>
      <Steps :current="1" class="enterprise__top-steps" size="small">
        <Step title="基本信息"></Step>
        <Step title="企业信息"></Step>
        <Step title="人工审核"></Step>
        <Step title="认证结果"></Step>
      </Steps>
      <div v-if="auditStatus != 3" class="enterprise__top-tips" @click="routerToInformation">
        <p>返回查看基本信息</p>
      </div>
      <div v-if="auditStatus == 3" class="enterprise__top-tips3" @click="routerToInformation">
        <p>修改基本信息</p>
      </div>
    </div>
    <Form ref="inforForm" :model="inforForm" :label-width="120" :rules="inforFormValidate">
      <div class="enterprise__titleBlock">
        <span>企业基本信息</span>
      </div>
      <Row type="flex" justify="start" class="enterprise__form">
        <Col span="11">
          <FormItem label="企业全称" prop="enterpriseName">
            <Input v-model="inforForm.enterpriseName" :maxlength="20"/>
            <div class="enterprise__form-tips">
              <p>请务必与营业执照上的名称保持一致</p>
            </div>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isEnterpriseNameError"
              class="enterprise__form-tipMax"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.enterpriseNameError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="企业简称" prop="shortName">
            <Input v-model="inforForm.shortName" :maxlength="10"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isShortNameError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.shortNameError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="企业官网" prop="website">
            <Input v-model="inforForm.website" :maxlength="100"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isWebsiteError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.websiteError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="统一社会信用代码" prop="creditCode">
            <Input v-model="inforForm.creditCode" :maxlength="20"/>
            <div class="enterprise__form-tips">
              <p>请务必营业执照上的注册号或统一社会信用代码号一致</p>
            </div>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isCreditCodeError"
              class="enterprise__form-tipMax"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.creditCodeError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="营业执照扫描件" prop="creditPhoto">
            <Tooltip placement="right" :always="true" :disabled="isCreditPhotoError">
              <Upload
                :default-file-list="creditDefaultList"
                ref="upload"
                :show-upload-list="false"
                :on-success="handleSuccess"
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
                <div class="enterprise__uploadBox" v-if="uploadList.length == 0">
                  <Icon type="plus" size="20"></Icon>
                  <p>选择文件</p>
                </div>
                <div
                  else
                  class="enterprise__uploadList"
                  v-for="(item,index) in uploadList"
                  :key="index"
                >
                  <template v-if="item.status === 'finished'">
                    <img :src="staticPath + item.response" class="enterprise__uploadList-img">
                    <div class="enterprise__uploadList-cover">
                      <Icon type="ios-eye-outline" @click.stop="handleView(item.response)"></Icon>
                      <Icon type="ios-trash-outline" @click.stop="handleRemove(item)"></Icon>
                    </div>
                  </template>
                  <template v-else>
                    <i-progress v-if="item.showProgress" :percent="item.percentage" hide-info></i-progress>
                  </template>
                </div>
              </Upload>
              <div slot="content" style="white-space: normal;">
                <p>{{this.creditPhotoError}}</p>
              </div>
            </Tooltip>
            <div class="enterprise__form-tips">
              <p>
                请提供证件的原件照片或彩色扫描件（正副本均可），文字/盖章清晰
                可辨认 格式要求jpg、jpeg、png，不超过10MB
              </p>
            </div>
            <modal title="预览" v-model="imgVisible">
              <img :src="staticPath+imgName" v-if="imgVisible" style="width: 100%">
            </modal>
          </FormItem>
          <FormItem label="企业描述" prop="companyDescription" class="enterprise__form-item">
            <Input
              v-model="inforForm.companyDescription"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              placeholder="请输入企业描述"
              class="enterprise__form-teaxtarea"
              :maxlength="200"
              style="resize:none;"
            ></Input>
            <p class="enterprise__form-counter">{{inforForm.companyDescription.length}}/200</p>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isCompanyDescriptionError"
              class="enterprise__form-tipMax"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.companyDescriptionError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem>
            <Upload
              multiple
              :default-file-list="companyDescriptionFileDefault"
              action="/business/fileUpload/uploadFile"
              :headers="accessToken"
              :data="otherFileData"
              :on-success="getCompanyDescription"
              :on-error="getCompanyDescriptionError"
              :on-remove="removeCompanyDescription"
              name="uploadFile"
              :format="['zip','rar']"
              :on-format-error="handleZipFormatError"
            >
              <Button class="enterprise__form-button" shape="circle">上传企业介绍资料</Button>
            </Upload>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isCompanyDescriptionFileError"
              class="enterprise__upload-tips"
            >
              <span>（.zip/.rar文件，限20MB以内，文件名勿含特殊字符）</span>
              <div slot="content" style="white-space: normal;">
                <p
                  style="width:220px"
                  v-for="(item,index) in this.companyDescriptionFileError"
                  :key="index"
                >{{item}}</p>
              </div>
            </Tooltip>
          </FormItem>
        </Col>
      </Row>
      <div class="enterprise__titleBlock">
        <span>企业背景信息</span>
      </div>
      <Row type="flex" justify="start" class="enterprise__form">
        <Col span="11">
          <FormItem label="年营业额" prop="annualTurnover">
            <Input v-model="inforForm.annualTurnover" :maxlength="10"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isAnnualTurnoverError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.annualTurnoverError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="软件研发团队" prop="team">
            <Select v-model="inforForm.team" prop="team">
              <Option v-for="(item,index) in teamList" :key="index" :value="item.id">{{item.name}}</Option>
              <!-- <Option v-for="(item,index) in teamList" :key="index" :value="index">公司团队</Option> -->
              <!-- <Option :value="2">外包团队</Option>
              <Option :value="3">无团队</Option>-->
            </Select>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isTeamError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.teamError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="以往明星产品" prop="starProduct">
            <Input
              v-model="inforForm.starProduct"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              placeholder="请输入产品描述"
              class="enterprise__from-teaxtarea"
              :maxlength="200"
            ></Input>
            <p class="enterprise__form-counter">{{inforForm.starProduct.length}}/200</p>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isStarProductError"
              class="enterprise__form-tipMax"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.starProductError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem>
            <Upload
              multiple
              :default-file-list="starProductFileDefault"
              action="/business/fileUpload/uploadFile"
              :headers="accessToken"
              :data="otherFileData"
              name="uploadFile"
              :on-success="getStarProductFile"
              :on-error="getStarProductError"
              :on-remove="removeStarProductFile"
              :format="['zip','rar']"
              :on-format-error="handleZipFormatError"
            >
              <Button class="enterprise__form-button" shape="circle">上传产品介绍资料</Button>
            </Upload>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isStarProductFileError"
              class="enterprise__upload-tips"
            >
              <span>（.zip/.rar文件，限20MB以内，文件名勿含特殊字符）</span>
              <div slot="content" style="white-space: normal;">
                <p
                  style="width:220px"
                  v-for="(item,index) in this.starProductFileError"
                  :key="index"
                >{{item}}</p>
              </div>
            </Tooltip>
          </FormItem>
        </Col>
      </Row>
      <div class="enterprise__titleBlock">
        <span>拟接入产品信息</span>
      </div>
      <Row type="flex" justify="start" class="enterprise__form">
        <Col span="11">
          <FormItem label="产品名称" prop="productName">
            <Input v-model="inforForm.productName" :maxlength="20"/>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isProductNameError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.productNameError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="产品类型" prop="productType">
            <Select
              v-model="inforForm.productType"
              prop="productType"
              @on-change="productTypeChange"
            >
              <Option
                :value="item.id"
                v-for="(item,index) in productTypeList"
                :key="index"
              >{{item.name}}</Option>
            </Select>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isProductTypeError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.productTypeError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="联网方式" prop="linkType">
            <Select v-model="inforForm.linkType" prop="linkType" @on-change="linkTypeChange">
              <Option
                :value="item.id"
                v-for="(item,index) in linkTypeList"
                :key="index"
              >{{item.name}}</Option>
            </Select>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isLinkTypeError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.linkTypeError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="操作系统" prop="operateSystem">
            <Select
              v-model="inforForm.operateSystem"
              prop="operateSystem"
              @on-change="operateSystemChange"
            >
              <Option
                :value="item.id"
                v-for="(item,index) in operateSystemList"
                :key="index"
              >{{item.name}}</Option>
            </Select>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isOperateSystemError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.operateSystemError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="产品描述" prop="productDescription">
            <Input
              v-model="inforForm.productDescription"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              placeholder="请输入企业描述"
              class="enterprise__from-teaxtarea"
              :maxlength="200"
            ></Input>
            <p class="enterprise__form-counter">{{inforForm.productDescription.length}}/200</p>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isProductDescriptionError"
              class="enterprise__form-tip"
            >
              <div slot="content" style="white-space: normal;">
                <p style="width:220px;">{{this.productDescriptionError}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem>
            <Upload
              multiple
              :default-file-list="productDescriptionFileDefault"
              action="/business/fileUpload/uploadFile"
              :headers="accessToken"
              :data="otherFileData"
              name="uploadFile"
              :on-success="getProductDescriptionFile"
              :on-error="getProductDescriptionError"
              :format="['zip','rar']"
              :on-format-error="handleZipFormatError"
            >
              <Button class="enterprise__form-button" shape="circle">上传产品介绍资料</Button>
            </Upload>
            <Tooltip
              placement="right"
              :always="true"
              :disabled="isProductDescriptionFileError"
              class="enterprise__upload-tips"
            >
              <span>（.zip/.rar文件，限20MB以内，文件名勿含特殊字符）</span>
              <div slot="content" style="white-space: normal;">
                <p
                  style="width:220px"
                  v-for="(item,index) in this.productDescriptionFileError"
                  :key="index"
                >{{item}}</p>
              </div>
            </Tooltip>
          </FormItem>
        </Col>
      </Row>
      <div class="enterprise__line"></div>
      <Checkbox v-model="single" class="enterprise__radio">我已阅读《星云工业物联网平台合作条款》</Checkbox>
      <Button
        @click="handleSubmit"
        :loading="submitLoading"
        type="primary"
        shape="circle"
        class="enterprise__submit"
        :disabled="!single"
      >提交</Button>
    </Form>
  </div>
</template>
<script>
import { staticPath } from "../../../../src/libs/base.js";
import { getStore, setStore } from "../../../utils/storage";
export default {
  data() {
    // const validateArea = (rule, value, callback) => {
    //   if (
    //     !this.inforForm.province ||
    //     !this.inforForm.city ||
    //     !this.inforForm.district ||
    //     this.inforForm.province == "" ||
    //     this.inforForm.city == "" ||
    //     this.inforForm.district == ""
    //   ) {
    //     callback(new Error("请选择正确的地址"));
    //   } else if (
    //     this.inforForm.province &&
    //     this.inforForm.city &&
    //     this.inforForm.district &&
    //     this.inforForm.province != "" &&
    //     this.inforForm.city != "" &&
    //     this.inforForm.district != ""
    //   ) {
    //     callback();
    //   }
    // };
    return {
      inforForm: {
        individualId: "",
        enterpriseName: "", //企业全称
        shortName: "", //企业简介
        website: "", //企业官网
        creditCode: "",
        creditPhoto: "",
        companyDescription: "", //企业描述
        companyDescriptionFile: [], //企业描述附件
        annualTurnover: "", //营业额
        team: "",
        starProduct: "", //以往明星产品
        starProductFile: [], //以往明星产品文件地址
        productName: "",
        productType: "",
        linkType: "", //联网方式
        operateSystem: "", //操作系统
        productDescription: "",
        productDescriptionFile: [], //产品描述文件
        createUser: JSON.parse(this.cookies.get("userInfo")).id, //创建人id
        createUserName: JSON.parse(this.cookies.get("userInfo")).username //创建人名称
      },
      creditDefaultList: [],
      single: false,
      inforFormValidate: {
        enterpriseName: [
          {
            required: true,
            message: "企业全称不为空",
            trigger: "blur"
          }
        ],
        shortName: [
          {
            required: true,
            message: "企业简称不为空",
            trigger: "blur"
          }
        ],
        street: [
          {
            required: true,
            message: "街道地址不为空",
            trigger: "blur"
          }
        ],
        creditCode: [
          {
            required: true,
            message: "统一社会信用代码不为空",
            trigger: "blur"
          }
        ],
        companyDescription: [
          {
            required: true,
            message: "企业描述不为空",
            trigger: "blur"
          }
        ],
        annualTurnover: [
          {
            required: true,
            message: "年营业额不为空",
            trigger: "blur"
          }
        ],
        team: [
          {
            required: true,
            message: "请选择研发团队"
          }
        ],
        starProduct: [
          {
            required: true,
            message: "产品描述不为空",
            trigger: "blur"
          }
        ]
      },
      submitLoading: false,
      uploadList: [],
      otherFileData: {
        type: "enterprise"
      },
      otherPhotoData: {
        type: "enterprise",
        userName: JSON.parse(this.cookies.get("userInfo")).username
      },
      imgVisible: false,
      areaList: [],
      staticPath: staticPath,
      teamList: [
        { id: 1, name: "公司团队" },
        { id: 2, name: "外包团队" },
        { id: 3, name: "无团队" }
      ],
      productTypeList: [],
      linkTypeList: [],
      operateSystemList: [],
      accessToken: {
        accessToken: getStore("accessToken")
      },
      auditStatus: "",
      companyDescriptionFileDefault: [],
      starProductFileDefault: [],
      productDescriptionFileDefault: [],
      companyDescriptionFileError: [],
      isCompanyDescriptionFileError: true,
      starProductFileError: [],
      isStarProductFileError: true,
      productDescriptionFileError: [],
      isProductDescriptionFileError: true,
      isCreditCodeError: true,
      creditCodeError: "",
      isEnterpriseNameError: true,
      enterpriseNameError: "",
      isShortNameError: true,
      shortNameError: "",
      isWebsiteError: true,
      websiteError: "",
      isCreditPhotoError: true,
      creditPhotoError: "",
      isCompanyDescriptionError: true,
      companyDescriptionError: "",
      isAnnualTurnoverError: true,
      annualTurnoverError: "",
      isTeamError: true,
      teamError: "",
      isStarProductError: true,
      starProductError: "",
      isProductNameError: true,
      productNameError: "",
      isProductTypeError: true,
      productTypeError: "",
      isLinkTypeError: true,
      linkTypeError: "",
      isOperateSystemError: true,
      operateSystemError: "",
      isProductDescriptionError: true,
      productDescriptionError: "",
      informationForm: {}
    };
  },
  methods: {
    //提交
    handleSubmit() {
      console.log(this.inforForm);
      this.$refs.inforForm.validate(valid => {
        if (valid) {
          // this.$Message.success("Success!");
          this.insertEnterpriseCertification();
        }
      });
    },
    //营业执照扫描件上传提交成功返回
    handleSuccess(res, file) {
      console.log(res, file);
      this.uploadList = [];
      this.uploadList.push(file);
      this.inforForm.creditPhoto = file.response;
    },
    //营业执照扫描件格式错误提示
    handleFormatError(file) {
      this.$Notice.error({
        title: "文件格式出错",
        desc: file.name + "的文件格式不正确, 请选择png、jpg或者jpeg."
      });
    },
    //描述附件格式错误提示
    handleZipFormatError(file) {
      this.$Notice.error({
        title: "文件格式出错",
        desc: file.name + "的文件格式不正确, 请选择zip、或者rar."
      });
    },
    //营业执照尺寸过大提示
    handleMaxSize(file) {
      this.$Notice.warning({
        title: "文件尺寸过大",
        desc: "文件" + file.name + " 超过了10M."
      });
    },
    //营业执照扫描件移除
    handleRemove(file) {
      this.uploadList = [];
      this.inforForm.creditPhoto = "";
    },
    //营业执照预览
    handleView(name) {
      this.imgName = name;
      this.imgVisible = true;
    },
    //获取产品类型
    getProductTypeList() {
      this.postBusRequest("/productType/selectList", {
        selectColumns: ["id", "name"]
      }).then(res => {
        console.log(res);
        if (res.code == 200 || res.code == "200") {
          this.productTypeList = res.data;
        }
      });
    },
    //产品类型修改
    productTypeChange(value) {
      console.log(value);
    },
    //获取联网方式
    getLinkTypeList() {
      this.postBusRequest("/dictionary/linkType/selectListNoWhere").then(
        res => {
          console.log(res);
          if (res.code == 200 || res.code == "200") {
            this.linkTypeList = res.data;
          }
        }
      );
    },
    // linkTypeOpen(open) {
    //   if (open) {
    //     this.getLinkTypeList();
    //   }
    // },
    //联网方式改变
    linkTypeChange(value) {
      console.log(value);
    },
    //获取系统列表
    getOperateSystemList() {
      this.postBusRequest("/operateSystem/selectList", {
        selectColumns: ["id", "name"]
      }).then(res => {
        console.log(res);
        if (res.code == 200 || res.code == "200") {
          this.operateSystemList = res.data;
        }
      });
    },
    //系统选项改变
    operateSystemChange(value) {
      console.log(value);
    },
    //获取认证状态
    getAuditStatus() {
      this.getBusRequest("/accountInfoIndividual/selectByUserId", {
        userId: JSON.parse(this.cookies.get("userInfo")).id
      }).then(_res => {
        console.log(_res.data.auditStatus, !_res.data.auditStatus);
        if (_res.code == 200 || _res.code == "200") {
          if (_res.data.auditStatus) {
            this.auditStatus = _res.data.auditStatus;
            console.log(this.auditStatus);
            if (
              this.auditStatus &&
              this.auditStatus !== 0 &&
              this.auditStatus !== 3 &&
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
            } else if (this.auditStatus === 3) {
              this.getBusRequest(
                "/accountInfoCompany/getEnterpriseCertification",
                {
                  userId: JSON.parse(this.cookies.get("userInfo")).id
                }
              ).then(res => {
                console.log(res.data);
                if (res.code == 200 || res.code == "200") {
                  this.inforForm = {
                    individualId: res.data.individual.id,
                    enterpriseName: res.data.company.name, //企业全称
                    shortName: res.data.company.shortName, //企业简介
                    website: res.data.company.website, //企业官网
                    creditCode: res.data.company.creditCode,
                    creditPhoto: res.data.company.creditPhoto,
                    companyDescription: res.data.company.companyDescription, //企业描述
                    companyDescriptionFile: res.data.companyDescriptionFile, //企业描述附件
                    annualTurnover: res.data.company.annualTurnover, //营业额
                    team: res.data.company.team,
                    starProduct: res.data.company.starProduct, //以往明星产品
                    starProductFile: res.data.starProductFile, //以往明星产品文件地址
                    productName: res.data.company.productName,
                    productType: res.data.company.productType,
                    linkType: res.data.company.linkType, //联网方式
                    operateSystem: res.data.company.operateSystem, //操作系统
                    productDescription: res.data.company.productDescription,
                    productDescriptionFile: res.data.productDescriptionFile, //产品描述文件
                    enterpriseId: res.data.company.id,
                    individualName: JSON.parse(
                      this.cookies.get("informationForm")
                    ).name, //姓名
                    identityNo: JSON.parse(this.cookies.get("informationForm"))
                      .identityNo, //姓名
                    identityPhoto: JSON.parse(
                      this.cookies.get("informationForm")
                    ).identityPhoto, //姓名
                    staff: JSON.parse(this.cookies.get("informationForm"))
                      .staff, //职务
                    source: JSON.parse(this.cookies.get("informationForm"))
                      .source,
                    province: JSON.parse(this.cookies.get("informationForm"))
                      .province,
                    city: JSON.parse(this.cookies.get("informationForm")).city,
                    district: JSON.parse(this.cookies.get("informationForm"))
                      .district,
                    street: JSON.parse(this.cookies.get("informationForm"))
                      .street,
                    tel: JSON.parse(this.cookies.get("informationForm")).tel,
                    email: JSON.parse(this.cookies.get("informationForm"))
                      .email,
                    fax: JSON.parse(this.cookies.get("informationForm")).fax,
                    createUser: JSON.parse(this.cookies.get("userInfo")).id, //创建人id
                    createUserName: JSON.parse(this.cookies.get("userInfo"))
                      .username //创建人名称
                  };
                  this.creditDefaultList = [
                    {
                      name: res.data.company.creditCode,
                      url: this.staticPath + res.data.company.creditPhoto
                    }
                  ];
                  this.uploadList = [
                    {
                      response: res.data.company.creditPhoto,
                      status: "finished"
                    }
                  ];
                  this.companyDescriptionFileDefault =
                    res.data.companyDescriptionFile;
                  this.starProductFileDefault = res.data.starProductFile;
                  this.productDescriptionFileDefault =
                    res.data.productDescriptionFile;
                  console.log(JSON.parse(res.data.company.rejectReason));
                  console.log(
                    Object.keys(JSON.parse(res.data.company.rejectReason))
                  );
                  var arr = Object.keys(
                    JSON.parse(res.data.company.rejectReason)
                  );
                  var obj = JSON.parse(res.data.company.rejectReason);
                  this.companyDescriptionFileError = [];
                  this.productDescriptionFileError = [];
                  this.starProductFileError = [];
                  this.isCompanyDescriptionFileError = true;
                  this.isProductDescriptionFileError = true;
                  this.isStarProductFileError = true;
                  arr.forEach(el => {
                    this.companyDescriptionFileDefault.forEach(_el => {
                      if (_el.url == el) {
                        this.isCompanyDescriptionFileError = false;
                        this.companyDescriptionFileError.push(
                          _el.name + obj[el]
                        );
                      }
                    });
                    this.starProductFileDefault.forEach(_el => {
                      if (_el.url == el) {
                        this.isStarProductFileError = false;
                        this.starProductFileError.push(_el.name + obj[el]);
                      }
                    });
                    this.productDescriptionFileDefault.forEach(_el => {
                      if (_el.url == el) {
                        this.isProductDescriptionFileError = false;
                        this.productDescriptionFileError.push(
                          _el.name + obj[el]
                        );
                      }
                    });
                    if (el == "creditPhoto") {
                      this.isCreditPhotoError = false;
                      this.creditPhotoError = obj[el];
                    } else if (el == "enterpriseName") {
                      this.isEnterpriseNameError = false;
                      this.enterpriseNameError = obj[el];
                    } else if (el == "shortName") {
                      this.isShortNameError = false;
                      this.shortNameError = obj[el];
                    } else if (el == "website") {
                      this.isWebsiteError = false;
                      this.websiteError = obj[el];
                    } else if (el == "creditCode") {
                      this.isCreditCodeError = false;
                      this.creditCodeError = obj[el];
                    } else if (el == "companyDescription") {
                      this.isCompanyDescriptionError = false;
                      this.companyDescriptionError = obj[el];
                    } else if (el == "annualTurnover") {
                      this.isAnnualTurnoverError = false;
                      this.annualTurnoverError = obj[el];
                    } else if (el == "team") {
                      this.isTeamError = false;
                      this.teamError = obj[el];
                    } else if (el == "starProduct") {
                      this.isStarProductError = false;
                      this.starProductError = obj[el];
                    } else if (el == "productName") {
                      this.isProductNameError = false;
                      this.productNameError = obj[el];
                    } else if (el == "productType") {
                      this.isProductTypeError = false;
                      this.productTypeError = obj[el];
                    } else if (el == "linkType") {
                      this.isLinkTypeError = false;
                      this.linkTypeError = obj[el];
                    } else if (el == "operateSystem") {
                      this.isOperateSystemError = false;
                      this.operateSystemError = obj[el];
                    } else if (el == "productDescription") {
                      this.isProductDescriptionError = false;
                      this.productDescriptionError = obj[el];
                    }
                  });
                }
              });
            }
          }
        }
      });
    },
    //提交及修改企业信息
    insertEnterpriseCertification() {
      if (this.auditStatus == 3) {
        this.postBusRequest(
          "/accountInfoCompany/updateEnterpriseCertification",
          this.inforForm
        ).then(res => {
          console.log(res);
          if (res.code == 200 || res.code == "200") {
            this.$store.state.authenticate.isFix = false;
            this.getAuditStatus();
          }
        });
      } else if (this.auditStatus == 0) {
        this.postBusRequest(
          "/accountInfoCompany/insertEnterpriseCertification",
          this.inforForm
        ).then(res => {
          console.log(res);
          if (res.code == 200 || res.code == "200") {
            this.getAuditStatus();
          }
        });
      }
    },
    //上传企业介绍资料成功回调
    getCompanyDescription(res, file, fileList) {
      console.log(res, file, fileList);
      this.inforForm.companyDescriptionFile = [];
      if (res.code == 200 || res.code == "200") {
        fileList.forEach(el => {
          if (el.status == "finished") {
            if (el.response) {
              this.inforForm.companyDescriptionFile.push({
                name: el.response.data.fileName,
                url: el.response.data.filePath
              });
            } else {
              this.inforForm.companyDescriptionFile.push({
                name: el.name,
                url: el.url
              });
            }
          }
        });
      }
    },
    //上传企业介绍资料失败
    getCompanyDescriptionError(error, file, fileList) {
      console.log(error);
    },
    // getCompanyDescriptionFormError(error, file, fileList) {
    //   message;
    // },
    //移除企业介绍附件
    removeCompanyDescription(file, fileList) {
      console.log(file, fileList);
      this.inforForm.companyDescriptionFile = [];
      // if (this.fileList.length > 0) {
      fileList.forEach(el => {
        if (el.response) {
          this.inforForm.companyDescriptionFile.push(el.response.data.filePath);
        } else {
          this.inforForm.companyDescriptionFile.push(el.url);
        }
      });
      //}
    },
    //明星产品附件上传成功回调
    getStarProductFile(res, file, fileList) {
      console.log(res, file, fileList);
      this.inforForm.starProductFile = [];
      if (res.code == 200 || res.code == "200") {
        fileList.forEach(el => {
          if (el.status == "finished") {
            if (el.response) {
              this.inforForm.starProductFile.push({
                name: el.response.data.fileName,
                url: el.response.data.filePath
              });
            } else {
              this.inforForm.starProductFile.push({
                name: el.name,
                url: el.url
              });
            }
          }
        });
      }
      console.log(this.inforForm.starProductFile);
    },
    //明星产品附件上传失败
    getStarProductError(error, file, fileList) {
      console.log(error);
    },
    //移除明星产品附件
    removeStarProductFile(file, fileList) {
      console.log(file, fileList);
      this.inforForm.starProductFile = [];
      // if (this.fileList.length > 0) {
      fileList.forEach(el => {
        if (el.response) {
          this.inforForm.starProductFile.push(el.response.data.filePath);
        } else {
          this.inforForm.starProductFile.push(el.url);
        }
      });
      // }
    },
    //产品描述附件上传成功
    getProductDescriptionFile(res, file, fileList) {
      console.log(res, file, fileList);
      this.inforForm.productDescriptionFile = [];
      if (res.code == 200 || res.code == "200") {
        fileList.forEach(el => {
          if (el.status == "finished") {
            if (el.response) {
              this.inforForm.productDescriptionFile.push({
                name: el.response.data.fileName,
                url: el.response.data.filePath
              });
            } else {
              this.inforForm.productDescriptionFile.push({
                name: el.name,
                url: el.url
              });
            }
          }
        });
      }
    },
    //产品描述附件上传失败
    getProductDescriptionError(error, file, fileList) {
      console.log(error);
    },
    //移除产品描述附件
    removeProductDescriptionFile(file, fileList) {
      console.log(file, fileList);
      this.inforForm.productDescriptionFile = [];
      // if (this.fileList.length > 0) {
      fileList.forEach(el => {
        if (el.response) {
          this.inforForm.productDescriptionFile.push(el.response.data.filePath);
        } else {
          this.inforForm.productDescriptionFile.push(el.url);
        }
      });
      // }
    },
    //路由跳转到基本信息
    routerToInformation() {
      this.$router.push({
        name: "authenticate",
        query: { route: "information" }
      });
    }
  },
  mounted() {
    console.log(JSON.parse(this.cookies.get("userInfo")));
    this.getAuditStatus();
    this.getProductTypeList();
    this.getOperateSystemList();
    this.getLinkTypeList();
    console.log(JSON.parse(this.cookies.get("informationForm")));
  }
};
</script>
<style lang="less" scoped>
@import "./enterprise.less";
</style>

