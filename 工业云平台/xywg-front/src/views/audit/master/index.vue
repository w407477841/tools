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
              <Input
                type="text"
                v-model="searchForm.keyword"
                clearable
                placeholder="企业名称"
                style="width: 200px"
              />

              <Form-item style="margin-left:-70px;">
                <Button @click="handleSearch" type="primary">搜索</Button>
              </Form-item>
            </Form>
          </Row>
          <Row class="margin-top-10 searchable-table-con1">
            <Table :loading="loading" border :columns="columns" :data="data" ref="table"></Table>
          </Row>
          <Row type="flex" justify="end" class="code-row-bg page">
            <Page
              :current="this.pageNum"
              :total="total"
              :page-size="this.pageSize"
              @on-change="changePage"
              @on-page-size-change="changePageSize"
              :page-size-opts="[10,20,50,100]"
              size="small"
              show-total
              show-elevator
              show-sizer
            ></Page>
          </Row>
        </Card>
      </Col>
    </Row>
    <Modal :title="modalTitle" v-model="addFormVisible" :mask-closable="false" width="50%">
      <Form
        ref="addForm"
        :model="addForm"
        :label-width="110"
        :rules="addFormValidate"
        :inline="true"
      >
        <div class="information__titleBlock">
          <span>基本信息</span>
        </div>
        <Row type="flex" justify="start" class="information__form">
          <Col span="24">
            <FormItem label="真实姓名" prop="name" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.nameStatus"></Checkbox>
              <Input v-model="addForm.name" :readonly="true" style="width : 90%"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.nameStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.nameReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="身份证号" prop="identityNo" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.identityNoStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.identityNo"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.identityNoStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.identityNoReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="身份证照片" prop="identityPhoto" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.identityPhotoStatus"></Checkbox>
              <a>
                <div name="uploadPhoto" style="display:inline-block;width:104px;">
                  <div class="information__uploadList" @click.stop="handleUpView()">
                    <template>
                      <img :src="staticPath + identityPhotoUp" class="information__uploadList-img">
                    </template>
                  </div>
                </div>
              </a>
              <a>
                <div name="uploadPhoto" style="display:inline-block;width:104px;">
                  <div class="information__uploadList" @click.stop="handleDownView()">
                    <template>
                      <img
                        :src="staticPath + identityPhotoDown"
                        class="information__uploadList-img"
                      >
                    </template>
                  </div>
                </div>
              </a>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.identityPhotoStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.identityPhotoReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem style="width : 100%" label="职务" prop="staff">
              <Checkbox style="width : 5%" v-model="addForm.staffStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.staff"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.staffStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.staffReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="来源" prop="source" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.sourceStatus"></Checkbox>
              <Select v-model="addForm.source" style="width : 90%">
                <Option :value="1" :disabled="true">周围人推荐</Option>
                <Option :value="2" :disabled="true">已建立合作</Option>
                <Option :value="3" :disabled="true">新闻报道</Option>
                <Option :value="4" :disabled="true">搜索引擎</Option>
                <Option :value="5" :disabled="true">会议会展</Option>
                <Option :value="6" :disabled="true">其他</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.sourceStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.sourceReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
        </Row>
        <div class="information__titleBlock">
          <span>联系信息</span>
        </div>
        <Row type="flex" justify="start" class="information__form">
          <Col span="24">
            <FormItem label="所在地区" prop="province" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.provinceStatus"></Checkbox>
              <Select style="width : 30%" v-model="addForm.province">
                <Option
                  :disabled="true"
                  :value="item.id"
                  v-for="(item,index) in provinceList"
                  :key="index"
                >{{item.areaName}}</Option>
              </Select>
              <Select style="width : 30%" v-model="addForm.city" prop="city">
                <Option
                  :disabled="true"
                  :value="item.id"
                  v-for="(item,index) in cityList"
                  :key="index"
                >{{item.areaName}}</Option>
              </Select>
              <Select style="width : 30%" v-model="addForm.district" prop="district">
                <Option
                  :disabled="true"
                  :value="item.id"
                  v-for="(item,index) in districtList"
                  :key="index"
                >{{item.areaName}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.provinceStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.provinceReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="街道地址" prop="street" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.streetStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.street"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.streetStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.streetReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="联系电话" prop="tel" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.telStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.tel"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.telStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.telReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="电子邮件" prop="email" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.emailStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.email"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.emailStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.emailReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="传真" prop="fax" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.faxStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.fax"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.faxStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.faxReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
        </Row>
        <div class="enterprise__titleBlock">
          <span>企业基本信息</span>
        </div>
        <Row type="flex" justify="start" class="enterprise__form">
          <Col span="24">
            <FormItem label="企业全称" prop="enterpriseName" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.enterpriseNameStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.enterpriseName"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.enterpriseNameStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.enterpriseNameReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="企业简称" prop="shortName" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.shortNameStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.shortName"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.shortNameStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.shortNameReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="企业官网" prop="website" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.websiteStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.website"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.websiteStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.websiteReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="统一社会信用代码" prop="creditCode" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.creditCodeStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.creditCode"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.creditCodeStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.creditCodeReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="营业执照扫描件" prop="creditPhoto" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.creditPhotoStatus"></Checkbox>
              <a>
                <div
                  name="uploadPhoto"
                  style="display:inline-block;width:104px;"
                  @click.stop="handleView()"
                >
                  <div class="enterprise__uploadList">
                    <template>
                      <img :src="staticPath+addForm.creditPhoto" class="enterprise__uploadList-img">
                    </template>
                  </div>
                </div>
              </a>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.creditPhotoStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.creditPhotoReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem
              label="企业描述"
              prop="companyDescription"
              class="enterprise__form-item"
              style="width : 100%"
            >
              <Checkbox style="width : 5%" v-model="addForm.companyDescriptionStatus"></Checkbox>
              <Input
                :readonly="true"
                v-model="addForm.companyDescription"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入企业描述"
                class="enterprise__form-teaxtarea"
                :maxlength="200"
                style="resize:none;width:90%"
              ></Input>
              <p class="enterprise__form-counter">{{addForm.companyDescription.length}}/200</p>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.companyDescriptionStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.companyDescriptionReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <div v-for="item in addForm.companyFile" :key="item.name" style="width : 100%">
            <Col span="24">
              <FormItem style="width : 100%">
                <Checkbox style="width : 5%" v-model="item.check"></Checkbox>
                <a :href="staticPath+item.attachment">
                  <Button class="enterprise__form-button" shape="circle">{{item.name}}</Button>
                </a>
              </FormItem>
            </Col>
            <Col span="24" v-if="item.check">
              <FormItem label="问题所在" style="width : 100%">
                <Input
                  style="width : 97%"
                  v-model="item.reason"
                  type="textarea"
                  :autosize="{minRows: 2,maxRows: 5}"
                  placeholder="请输入问题所在"
                  class="enterprise__from-teaxtarea"
                ></Input>
              </FormItem>
            </Col>
          </div>
        </Row>
        <div class="enterprise__titleBlock">
          <span>企业背景信息</span>
        </div>
        <Row type="flex" justify="start" class="enterprise__form">
          <Col span="24">
            <FormItem label="年营业额" prop="annualTurnover" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.annualTurnoverStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.annualTurnover"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.annualTurnoverStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.annualTurnoverReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="软件研发团队" prop="team" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.teamStatus"></Checkbox>
              <Select style="width : 90%" :readonly="true" v-model="addForm.team">
                <Option :disabled="true" :value="1">公司团队</Option>
                <Option :disabled="true" :value="2">外包团队</Option>
                <Option :disabled="true" :value="3">无团队</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.teamStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.teamReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="以往明星产品" prop="starProduct" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.starProductStatus"></Checkbox>
              <Input
                style="width : 90%"
                :readonly="true"
                v-model="addForm.starProduct"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入产品描述"
                class="enterprise__from-teaxtarea"
                :maxlength="200"
              ></Input>
              <p class="enterprise__form-counter">{{addForm.starProduct.length}}/200</p>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.starProductStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.starProductReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <div v-for="item in addForm.starFile" :key="item.name" style="width : 100%">
            <Col span="24">
              <FormItem style="width : 100%">
                <Checkbox style="width : 5%" v-model="item.check"></Checkbox>
                <a :href="staticPath+item.attachment">
                  <Button class="enterprise__form-button" shape="circle">{{item.name}}</Button>
                </a>
              </FormItem>
            </Col>
            <Col span="24" v-if="item.check">
              <FormItem label="问题所在" style="width : 100%">
                <Input
                  style="width : 97%"
                  v-model="item.reason"
                  type="textarea"
                  :autosize="{minRows: 2,maxRows: 5}"
                  placeholder="请输入问题所在"
                  class="enterprise__from-teaxtarea"
                ></Input>
              </FormItem>
            </Col>
          </div>
        </Row>
        <div class="enterprise__titleBlock">
          <span>拟接入产品信息</span>
        </div>
        <Row type="flex" justify="start" class="enterprise__form">
          <Col span="24">
            <FormItem label="产品名称" prop="productName" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.productNameStatus"></Checkbox>
              <Input style="width : 90%" :readonly="true" v-model="addForm.productName"/>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.productNameStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.productNameReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="产品类型" prop="productType" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.productTypeStatus"></Checkbox>
              <Select style="width : 90%" v-model="addForm.productType" prop="productType">
                <Option
                  :disabled="true"
                  :value="item.id"
                  v-for="(item,index) in productTypeList"
                  :key="index"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.productTypeStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.productTypeReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="联网方式" prop="linkType" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.linkTypeStatus"></Checkbox>
              <Select style="width : 90%" v-model="addForm.linkType" prop="linkType">
                <Option
                  :disabled="true"
                  :value="item.id"
                  v-for="(item,index) in linkTypeList"
                  :key="index"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.linkTypeStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.linkTypeReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="操作系统" prop="operateSystem" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.operateSystemStatus"></Checkbox>
              <Select style="width : 90%" v-model="addForm.operateSystem" prop="operateSystem">
                <Option
                  :disabled="true"
                  :value="item.id"
                  v-for="(item,index) in operateSystemList"
                  :key="index"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.operateSystemStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.operateSystemReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="产品描述" prop="productDescription" style="width : 100%">
              <Checkbox style="width : 5%" v-model="addForm.productDescriptionStatus"></Checkbox>
              <Input
                style="width : 90%"
                :readonly="true"
                v-model="addForm.productDescription"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
              <p class="enterprise__form-counter">{{addForm.productDescription.length}}/200</p>
            </FormItem>
          </Col>
          <Col span="24" v-if="addForm.productDescriptionStatus">
            <FormItem label="问题所在" style="width : 100%">
              <Input
                style="width : 97%"
                v-model="addForm.productDescriptionReason"
                type="textarea"
                :autosize="{minRows: 2,maxRows: 5}"
                placeholder="请输入问题所在"
                class="enterprise__from-teaxtarea"
              ></Input>
            </FormItem>
          </Col>
          <div v-for="item in addForm.productFile" :key="item.name" style="width : 100%">
            <Col span="24">
              <FormItem style="width : 100%">
                <Checkbox style="width : 5%" v-model="item.check"></Checkbox>
                <a :href="staticPath+item.attachment">
                  <Button class="enterprise__form-button" shape="circle">{{item.name}}</Button>
                </a>
              </FormItem>
            </Col>
            <Col span="24" v-if="item.check">
              <FormItem label="问题所在" style="width : 100%">
                <Input
                  style="width : 97%"
                  v-model="item.reason"
                  type="textarea"
                  :autosize="{minRows: 2,maxRows: 5}"
                  placeholder="请输入问题所在"
                  class="enterprise__from-teaxtarea"
                ></Input>
              </FormItem>
            </Col>
          </div>
        </Row>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitDict(2)">同意</Button>
        <Button type="error" :loading="submitLoading" @click="submitDict(3)">驳回</Button>
      </div>
    </Modal>
    <Modal title="预览" v-model="imgVisible" :mask-closable="false" :closable="false">
      <img
        :src="staticPath+addForm.creditPhoto"
        v-if="imgVisible"
        style="width: 100%"
        @click="closeImg"
      >
      <div slot="footer"></div>
    </Modal>
    <Modal title="预览" v-model="upVisible" :mask-closable="false" :closable="false">
      <img :src="staticPath+identityPhotoUp" v-if="upVisible" style="width: 100%" @click="closeImg">
      <div slot="footer"></div>
    </Modal>
    <Modal title="预览" v-model="downVisible" :mask-closable="false" :closable="false">
      <img
        :src="staticPath+identityPhotoDown"
        v-if="downVisible"
        style="width: 100%"
        @click="closeImg"
      >
      <div slot="footer"></div>
    </Modal>
  </div>
</template>
<script>
import { staticPath } from "@/libs/base.js";
import { getStore } from "@/utils/storage.js";
import { bill } from "@/components/common/audit.js";
export default {
  name: "device",
  data() {
    return {
      loading: true,
      total: 0,
      addFormVisible: false,
      modalTitle: "",
      pageNum: 1,
      pageSize: 10,
      searchForm: {
        keyword: ""
      },
      addForm: {
        companyDescription: "",
        starProduct: "",
        productDescription: "",
        creditPhoto: "",
        rejectReason: "",
        nameStatus: false,
        identityNoStatus: false,
        identityPhotoStatus: false,
        staffStatus: false,
        sourceStatus: false,
        provinceStatus: false,
        streetStatus: false,
        telStatus: false,
        emailStatus: false,
        faxStatus: false,
        enterpriseNameStatus: false,
        shortNameStatus: false,
        websiteStatus: false,
        creditCodeStatus: false,
        creditPhotoStatus: false,
        companyDescriptionStatus: false,
        annualTurnoverStatus: false,
        teamStatus: false,
        starProductStatus: false,
        productNameStatus: false,
        productTypeStatus: false,
        linkTypeStatus: false,
        operateSystemStatus: false,
        productDescriptionStatus: false,
        companyFileStatus: false,
        starFileStatus: false,
        productFileStatus: false
      },
      addFormValidate: {},
      submitLoading: false,
      columns: [
        {
          type: "index",
          width: 60,
          align: "center"
        },
        {
          title: "企业全称",
          key: "enterpriseName",
          align: "center",
          tooltip: true,
          minWidth: 180,
          maxWidth: 1000
        },
        {
          title: "统一社会信用代码",
          key: "creditCode",
          align: "center",
          tooltip: true,
          minWidth: 180,
          maxWidth: 1000
        },
        {
          title: "申请时间",
          key: "createTime",
          align: "center",
          tooltip: true,
          minWidth: 180,
          maxWidth: 1000
        },
        {
          title: "操作",
          key: "action",
          width: 300,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    size: "small",
                    type: "primary"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.audit(params.row);
                    }
                  }
                },
                "审批"
              )
            ]);
          }
        }
      ],
      data: [],
      provinceList: [],
      cityList: [],
      districtList: [],
      productTypeList: [],
      linkTypeList: [],
      operateSystemList: [],
      identityPhotoUp: "",
      identityPhotoDown: "",
      staticPath: staticPath,
      response: "",
      imgVisible: false,
      upVisible: false,
      downVisible: false
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
        params: [
          {
            name: "name",
            condition: "like",
            value: this.searchForm.keyword
          }
        ],
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      this.columns = [];
      this.postBusRequest("/audit/getAuditList", params).then(res => {
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
            title: "企业全称",
            key: "enterpriseName",
            align: "center",
            tooltip: true,
            minWidth: 180,
            maxWidth: 1000
          },
          {
            title: "统一社会信用代码",
            key: "creditCode",
            align: "center",
            tooltip: true,
            minWidth: 180,
            maxWidth: 1000
          },
          {
            title: "申请时间",
            key: "createTime",
            align: "center",
            tooltip: true,
            minWidth: 180,
            maxWidth: 1000
          },
          {
            title: "操作",
            key: "action",
            width: 300,
            align: "center",
            fixed: "right",
            render: (h, params) => {
              return h("div", [
                h(
                  "Button",
                  {
                    props: {
                      size: "small",
                      type: "primary"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.audit(params.row);
                      }
                    }
                  },
                  "审批"
                )
              ]);
            }
          }
        ];
      });
    },
    //模糊查询
    handleSearch() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.init();
    },
    //审核打开弹窗
    audit(row) {
      this.modalTitle = "人工审核";
      this.$refs.addForm.resetFields();
      this.addForm = JSON.parse(JSON.stringify(row));
      this.addForm.rejectReason = "";
      let identityPhoto = this.addForm.identityPhoto.split(",");
      this.identityPhotoUp = identityPhoto[0];
      this.identityPhotoDown = identityPhoto[1];
      this.getProvincetList(0);
      this.getCityList(this.addForm.province);
      this.getDistrictList(this.addForm.city);
      this.addFormVisible = true;
    },
    //提交
    submitDict(v) {
      //拼接驳回理由
      let reason = "{";
      if (this.addForm.nameStatus) {
        if (
          this.addForm.nameReason != null &&
          this.addForm.nameReason != undefined &&
          this.addForm.nameReason != ""
        ) {
          reason += '"name":"' + this.addForm.nameReason + '",';
        } else {
          reason += '"name":"真是姓名存在问题",';
        }
      }
      if (this.addForm.identityNoStatus) {
        if (
          this.addForm.identityNoReason != null &&
          this.addForm.identityNoReason != undefined &&
          this.addForm.identityNoReason != ""
        ) {
          reason += '"identityNo":"' + this.addForm.identityNoReason + '",';
        } else {
          reason += '"identityNo":"身份证号存在问题",';
        }
      }
      if (this.addForm.identityPhotoStatus) {
        if (
          this.addForm.identityPhotoReason != null &&
          this.addForm.identityPhotoReason != undefined &&
          this.addForm.identityPhotoReason != ""
        ) {
          reason +=
            '"identityPhoto":"' + this.addForm.identityPhotoReason + '",';
        } else {
          reason += '"identityPhoto":"身份证照片存在问题",';
        }
      }
      if (this.addForm.staffStatus) {
        if (
          this.addForm.staffReason != null &&
          this.addForm.staffReason != undefined &&
          this.addForm.staffReason != ""
        ) {
          reason += '"staff":"' + this.addForm.staffReason + '",';
        } else {
          reason += '"staff":"职务存在问题",';
        }
      }
      if (this.addForm.sourceStatus) {
        if (
          this.addForm.sourceReason != null &&
          this.addForm.sourceReason != undefined &&
          this.addForm.sourceReason != ""
        ) {
          reason += '"source":"' + this.addForm.sourceReason + '",';
        } else {
          reason += '"source":"来源存在问题",';
        }
      }
      if (this.addForm.provinceStatus) {
        if (
          this.addForm.provinceReason != null &&
          this.addForm.provinceReason != undefined &&
          this.addForm.provinceReason != ""
        ) {
          reason += '"province":"' + this.addForm.provinceReason + '",';
        } else {
          reason += '"province":"所在地区存在问题",';
        }
      }
      if (this.addForm.streetStatus) {
        if (
          this.addForm.streetReason != null &&
          this.addForm.streetReason != undefined &&
          this.addForm.streetReason != ""
        ) {
          reason += '"street":"' + this.addForm.streetReason + '",';
        } else {
          reason += '"street":"街道地址存在问题",';
        }
      }
      if (this.addForm.telStatus) {
        if (
          this.addForm.telReason != null &&
          this.addForm.telReason != undefined &&
          this.addForm.telReason != ""
        ) {
          reason += '"tel":"' + this.addForm.telReason + '",';
        } else {
          reason += '"tel":"联系电话存在问题",';
        }
      }
      if (this.addForm.emailStatus) {
        if (
          this.addForm.emailReason != null &&
          this.addForm.emailReason != undefined &&
          this.addForm.emailReason != ""
        ) {
          reason += '"email":"' + this.addForm.emailReason + '",';
        } else {
          reason += '"email":"电子邮件存在问题",';
        }
      }
      if (this.addForm.faxStatus) {
        if (
          this.addForm.faxReason != null &&
          this.addForm.faxReason != undefined &&
          this.addForm.faxReason != ""
        ) {
          reason += '"fax":"' + this.addForm.faxReason + '",';
        } else {
          reason += '"fax":"传真存在问题",';
        }
      }
      if (this.addForm.enterpriseNameStatus) {
        if (
          this.addForm.enterpriseNameReason != null &&
          this.addForm.enterpriseNameReason != undefined &&
          this.addForm.enterpriseNameReason != ""
        ) {
          reason +=
            '"enterpriseName":"' + this.addForm.enterpriseNameReason + '",';
        } else {
          reason += '"enterpriseName":"企业全称存在问题",';
        }
      }
      if (this.addForm.shortNameStatus) {
        if (
          this.addForm.shortNameReason != null &&
          this.addForm.shortNameReason != undefined &&
          this.addForm.shortNameReason != ""
        ) {
          reason += '"shortName":"' + this.addForm.shortNameReason + '",';
        } else {
          reason += '"shortName":"企业简称存在问题",';
        }
      }
      if (this.addForm.websiteStatus) {
        if (
          this.addForm.websiteReason != null &&
          this.addForm.websiteReason != undefined &&
          this.addForm.websiteReason != ""
        ) {
          reason += '"website":"' + this.addForm.websiteReason + '",';
        } else {
          reason += '"website":"企业官网存在问题",';
        }
      }
      if (this.addForm.creditCodeStatus) {
        if (
          this.addForm.creditCodeReason != null &&
          this.addForm.creditCodeReason != undefined &&
          this.addForm.creditCodeReason != ""
        ) {
          reason += '"creditCode":"' + this.addForm.creditCodeReason + '",';
        } else {
          reason += '"creditCode":"统一社会信用代码存在问题",';
        }
      }
      if (this.addForm.creditPhotoStatus) {
        if (
          this.addForm.creditPhotoReason != null &&
          this.addForm.creditPhotoReason != undefined &&
          this.addForm.creditPhotoReason != ""
        ) {
          reason += '"creditPhoto":"' + this.addForm.creditPhotoReason + '",';
        } else {
          reason += '"creditPhoto":"营业执照扫描件存在问题",';
        }
      }
      if (this.addForm.companyDescriptionStatus) {
        if (
          this.addForm.companyDescriptionReason != null &&
          this.addForm.companyDescriptionReason != undefined &&
          this.addForm.companyDescriptionReason != ""
        ) {
          reason +=
            '"companyDescription":"' +
            this.addForm.companyDescriptionReason +
            '",';
        } else {
          reason += '"companyDescription":"企业描述存在问题",';
        }
      }
      if (this.addForm.annualTurnoverStatus) {
        if (
          this.addForm.annualTurnoverReason != null &&
          this.addForm.annualTurnoverReason != undefined &&
          this.addForm.annualTurnoverReason != ""
        ) {
          reason +=
            '"annualTurnover":"' + this.addForm.annualTurnoverReason + '",';
        } else {
          reason += '"annualTurnover":"年营业额存在问题",';
        }
      }
      if (this.addForm.teamStatus) {
        if (
          this.addForm.teamReason != null &&
          this.addForm.teamReason != undefined &&
          this.addForm.teamReason != ""
        ) {
          reason += '"team":"' + this.addForm.teamReason + '",';
        } else {
          reason += '"team":"软件研发团队存在问题",';
        }
      }
      if (this.addForm.starProductStatus) {
        if (
          this.addForm.starProductReason != null &&
          this.addForm.starProductReason != undefined &&
          this.addForm.starProductReason != ""
        ) {
          reason += '"starProduct":"' + this.addForm.starProductReason + '",';
        } else {
          reason += '"starProduct":"以往明星产品存在问题",';
        }
      }
      if (this.addForm.productNameStatus) {
        if (
          this.addForm.productNameReason != null &&
          this.addForm.productNameReason != undefined &&
          this.addForm.productNameReason != ""
        ) {
          reason += '"productName":"' + this.addForm.productNameReason + '",';
        } else {
          reason += '"productName":"产品名称存在问题",';
        }
      }
      if (this.addForm.productTypeStatus) {
        if (
          this.addForm.productTypeReason != null &&
          this.addForm.productTypeReason != undefined &&
          this.addForm.productTypeReason != ""
        ) {
          reason += '"productType":"' + this.addForm.productTypeReason + '",';
        } else {
          reason += '"productType":"产品类型存在问题",';
        }
      }
      if (this.addForm.linkTypeStatus) {
        if (
          this.addForm.linkTypeReason != null &&
          this.addForm.linkTypeReason != undefined &&
          this.addForm.linkTypeReason != ""
        ) {
          reason += '"linkType":"' + this.addForm.linkTypeReason + '",';
        } else {
          reason += '"linkType":"联网方式存在问题",';
        }
      }
      if (this.addForm.operateSystemStatus) {
        if (
          this.addForm.operateSystemReason != null &&
          this.addForm.operateSystemReason != undefined &&
          this.addForm.operateSystemReason != ""
        ) {
          reason +=
            '"operateSystem":"' + this.addForm.operateSystemReason + '",';
        } else {
          reason += '"operateSystem":"操作系统存在问题",';
        }
      }
      if (this.addForm.productDescriptionStatus) {
        if (
          this.addForm.productDescriptionReason != null &&
          this.addForm.productDescriptionReason != undefined &&
          this.addForm.productDescriptionReason != ""
        ) {
          reason +=
            '"productDescription":"' +
            this.addForm.productDescriptionReason +
            '",';
        } else {
          reason += '"productDescription":"产品描述存在问题",';
        }
      }
      for (let i = 0; i < this.addForm.companyFile.length; i++) {
        if (this.addForm.companyFile[i].check) {
          if (
            this.addForm.companyFile[i].reason != null &&
            this.addForm.companyFile[i].reason != undefined &&
            this.addForm.companyFile[i].reason != ""
          ) {
            reason +=
              '"' +
              this.addForm.companyFile[i].attachment +
              '":"' +
              this.addForm.companyFile[i].reason +
              '",';
          } else {
            reason +=
              '"' +
              this.addForm.companyFile[i].attachment +
              '":"企业描述附件存在问题",';
          }
        }
      }
      for (let i = 0; i < this.addForm.starFile.length; i++) {
        if (this.addForm.starFile[i].check) {
          if (
            this.addForm.starFile[i].reason != null &&
            this.addForm.starFile[i].reason != undefined &&
            this.addForm.starFile[i].reason != ""
          ) {
            reason +=
              '"' +
              this.addForm.starFile[i].attachment +
              '":"' +
              this.addForm.starFile[i].reason +
              '",';
          } else {
            reason +=
              '"' +
              this.addForm.starFile[i].attachment +
              '":"以往明星产品附件存在问题",';
          }
        }
      }
      for (let i = 0; i < this.addForm.productFile.length; i++) {
        if (this.addForm.productFile[i].check) {
          if (
            this.addForm.productFile[i].reason != null &&
            this.addForm.productFile[i].reason != undefined &&
            this.addForm.productFile[i].reason != ""
          ) {
            reason +=
              '"' +
              this.addForm.productFile[i].attachment +
              '":"' +
              this.addForm.productFile[i].reason +
              '",';
          } else {
            reason +=
              '"' +
              this.addForm.productFile[i].attachment +
              '":"产品描述附件存在问题",';
          }
        }
      }
      if (reason.length > 1) {
        reason = reason.substring(0, reason.length - 1);
      }
      reason += "}";
      this.addForm.rejectReason = reason;
      let params = {
        id: this.addForm.id,
        individualId: this.addForm.individualId,
        auditStatus: v,
        userId: this.addForm.userId,
        rejectReason: this.addForm.rejectReason
      };
      let content = "";
      if (v == 2) {
        content = "<p>确认<b style='color:#2d8cf0;'>同意</b>该申请吗？</p>";
        this.$Modal.confirm({
          title: "同意",
          content: content,
          onOk: () => {
            this.postBusRequest("/audit/audit", params).then(res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.addFormVisible = false;
                this.init();
              } else {
                this.$Message.error(res.msg);
              }
            });
          }
        });
      } else if (v == 3) {
        content = "<p>确认<b style='color:#ed3f14;'>驳回</b>该申请吗？</p>";
        this.$refs.addForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "驳回",
              content: content,
              onOk: () => {
                this.postBusRequest("/audit/audit", params).then(res => {
                  if (res.code == 200) {
                    this.$Message.success(res.msg);
                    this.addFormVisible = false;
                    this.init();
                  } else {
                    this.$Message.error(res.msg);
                  }
                });
              }
            });
          }
        });
      }
    },
    //取消提交
    cancelDict() {
      this.addFormVisible = false;
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
    }, //获取省市区
    getProvincetList(id) {
      this.loading = true;
      this.postBusRequest("/areas/getAreas", { parentId: id }).then(res => {
        this.loading = false;
        this.provinceList = res;
      });
    },
    getCityList(id) {
      this.loading = true;
      this.postBusRequest("/areas/getAreas", { parentId: id }).then(res => {
        this.loading = false;
        this.cityList = res;
      });
    },
    getDistrictList(id) {
      this.loading = true;
      this.postBusRequest("/areas/getAreas", { parentId: id }).then(res => {
        this.loading = false;
        this.districtList = res;
      });
    },
    getProductTypeList() {
      let params = {
        selectColumns: ["id", "name"],
        order: {
          open: true,
          name: "create_time",
          isAsc: false
        }
      };
      this.loading = true;
      this.postBusRequest("/productType/selectList", params).then(res => {
        this.loading = false;
        this.productTypeList = res.data;
      });
    },
    getLinkTypeList() {
      let params = {
        selectColumns: ["id", "name"],
        order: {
          open: true,
          name: "create_time",
          isAsc: false
        }
      };
      this.loading = true;
      this.postBusRequest("/dictionary/linkType/selectList", params).then(
        res => {
          this.loading = false;
          this.linkTypeList = res.data;
        }
      );
    },
    getOperateSystemList() {
      let params = {
        selectColumns: ["id", "name"],
        order: {
          open: true,
          name: "create_time",
          isAsc: false
        }
      };
      this.loading = true;
      this.postBusRequest("/operateSystem/selectList", params).then(res => {
        this.loading = false;
        this.operateSystemList = res.data;
      });
    },
    handleView: function() {
      this.imgVisible = true;
    },
    handleUpView: function() {
      this.upVisible = true;
    },
    handleDownView: function() {
      this.downVisible = true;
    },
    closeImg: function() {
      this.imgVisible = false;
      this.upVisible = false;
      this.downVisible = false;
    }
  },
  mounted() {
    this.init();
    this.getProductTypeList();
    this.getLinkTypeList();
    this.getOperateSystemList();
  }
};
</script>
