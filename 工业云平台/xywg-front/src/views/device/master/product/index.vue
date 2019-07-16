<style lang="less">
@import "./index.less";
</style>
<template>
  <div class="search">
    <div>
      <Row>
        <Col>
          <Card>
            <Row class="operation">
              <Form
                ref="searchForm"
                :model="searchForm"
                inline
                :label-width="70"
                class="search-form"
              >
                <Input
                  v-model="searchForm.keyword"
                  clearable
                  placeholder="产品名称"
                  style="width: 200px"
                />
                <Form-item style="margin-left:-70px;">
                  <Button @click="handleSearch" type="primary">搜索</Button>
                </Form-item>
                <FormItem style="margin-left:-70px;">
                  <Button @click="addRoot" type="primary">新增</Button>
                </FormItem>
                <FormItem style="margin-left:-70px;">
                  <Button @click="delAll" type="error" :disabled="selectCount === 0 || isBatch">批量删除</Button>
                </FormItem>
                <FormItem style="margin-left:-70px;">
                  <Button @click="setUse" type="success" :disabled="selectCount === 0 || isUse">启用</Button>
                </FormItem>
                <FormItem style="margin-left:-70px;">
                  <Button @click="setDis" type="error" :disabled="selectCount === 0 || isDisd">禁用</Button>
                </FormItem>
              </Form>
            </Row>
            <Row class="margin-top-10 searchable-table-con1">
              <Table
                :loading="loading"
                @on-row-click="getFunctionList"
                border
                :columns="columns"
                :data="data"
                @on-selection-change="showSelect"
                ref="table"
                highlight-row
              ></Table>
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
    </div>
    <div class="marginTop">
      <Row>
        <Col>
          <Card>
            <Row class="operation">
              <Form ref="searchForm" inline :label-width="70" class="search-form">
                <FormItem style="margin-left:-70px;">
                  <Button @click="addFunction" type="primary">新增</Button>
                </FormItem>
              </Form>
            </Row>
            <Row class="margin-top-10 searchable-table-con1">
              <Table
                :loading="functionLoading"
                border
                :columns="functionColumns"
                :data="functionData"
                ref="table"
              ></Table>
            </Row>
            <Row type="flex" justify="end" class="code-row-bg page">
              <Page
                :current="this.functionPageNum"
                :total="functionTotal"
                :page-size="this.functionPageSize"
                @on-change="functionChangePage"
                @on-page-size-change="functionChangePageSize"
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
    </div>
    <Modal :title="modalTitle" v-model="addFormVisible" :mask-closable="false" :width="500">
      <Form ref="addForm" :model="addForm" :label-width="80" :rules="addFormValidate">
        <FormItem label="产品名称" prop="name">
          <Input v-model="addForm.name" :maxlength="64"/>
        </FormItem>
        <FormItem label="联网方式" prop="linkType">
          <Select v-model="addForm.linkType" filterable clearable>
            <Option v-for="item in linkTypeList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="产品类型" prop="type">
          <Select v-model="addForm.type" filterable clearable>
            <Option
              v-for="(item,index) in typeList"
              :key="index"
              :value="Number(item.id)"
            >{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="版本" prop="version">
          <Input v-model="addForm.version" :maxlength="32"/>
        </FormItem>
        <FormItem label="备注信息" prop="comments">
          <Input v-model="addForm.comments" type="textarea" :maxlength="200"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitDict" v-if="!isDis">提交</Button>
      </div>
    </Modal>
    <Modal :title="modalTitle" v-model="editFormVisible" :mask-closable="false" :width="500">
      <Form ref="editForm" :model="editForm" :label-width="85" :rules="editFormValidate">
        <FormItem label="产品名称" prop="name">
          <Input v-model="editForm.name" :maxlength="64" :disabled="isDis"/>
        </FormItem>
        <FormItem label="联网方式" prop="linkType">
          <Select v-model="editForm.linkType" filterable clearable :disabled="isDis">
            <Option v-for="item in linkTypeList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="产品类型" prop="productType">
          <Select v-model="editForm.type" filterable clearable :disabled="isDis">
            <Option v-for="item in typeList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="版本" prop="version">
          <Input v-model="editForm.version" :maxlength="32" :disabled="isDis"/>
        </FormItem>
        <FormItem label="产品唯一编号" prop="productKey">
          <div>{{editForm.productKey}}</div>
        </FormItem>
        <FormItem label="产品密钥" prop="productSecret">
          <div>{{editForm.productSecret}}</div>
        </FormItem>
        <FormItem label="备注信息" prop="comments">
          <Input v-model="editForm.comments" type="textarea" :maxlength="200" :disabled="isDis"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelDict">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitDict" v-if="!isDis">提交</Button>
      </div>
    </Modal>

    <Modal
      :title="functionModalTitle"
      v-model="functionAddFormVisible"
      :mask-closable="false"
      :closable="false"
      :width="500"
    >
      <Row>
        <Col style="text-align: center;margin-bottom:10px">
          <RadioGroup v-model="radioValue">
            <Radio :label="1" :disabled="functionModalTitle!='新增功能'&&radioValue!=1">事件</Radio>
            <Radio :label="2" :disabled="functionModalTitle!='新增功能'&&radioValue!=2">属性</Radio>
            <Radio :label="3" :disabled="functionModalTitle!='新增功能'&&radioValue!=3">方法</Radio>
          </RadioGroup>
        </Col>
      </Row>
      <!-- 这里是事件页面 开始-->
      <Row v-show="radioValue==1">
        <Form
          ref="eventAddForm"
          :model="eventAddForm"
          :label-width="75"
          :rules="eventAddFormValidate"
        >
          <FormItem label="产品类型" prop="typeId">
            <Select v-model="eventAddForm.typeId" :disabled="true">
              <Option v-for="item in typeList" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="功能名称" prop="eventId" class="spacialFormItem">
            <!-- <Select
              v-model="eventAddForm.eventId"
              @on-change="setEvent"
              :disabled="functionIsDis"
              :filterable="true"
              @on-query-change="getEventName"
            >
              <Option v-for="item in eventNameList" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>-->
            <Input
              v-model="eventAddForm.eventId"
              :maxlength="32"
              @on-focus="showSpacialEventFormBox"
              @input="eventfilter"
              @on-blur="eventConfirm"
              :disabled="functionIsDis"
            />
            <ul class="spacialEventFormBox spacialFormBox">
              <li
                v-for="(item,index) in eventNameList"
                :key="index"
                class="ivu-select-item spacialEventFormBoxItem"
                @click="hideSpacialEventFormBox(item.name,item.id)"
              >{{item.name}}</li>
            </ul>
          </FormItem>
          <FormItem label="标识符" prop="key">
            <Input
              v-model="eventAddForm.key"
              :maxlength="32"
              :readonly="eventAddForm.eventId==''"
              :disabled="functionIsDis"
            />
          </FormItem>
          <FormItem label="事件类型" prop="eventType">
            <RadioGroup v-model="eventAddForm.eventType">
              <Radio :label="1" :disabled="functionIsDis">信息</Radio>
              <Radio :label="2" :disabled="functionIsDis">预警</Radio>
              <Radio :label="3" :disabled="functionIsDis">故障</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="输出参数" prop="list">
            <Button type="primary" @click="addParamButton(1)" :disabled="functionIsDis">增加参数</Button>
            <Tooltip
              placement="top"
              v-for="(item,index) in eventAddForm.list"
              :key="index"
              class="parameterTooltip"
            >
              <div class="ivu-tag ivu-tag-checked parameterTag">
                <span
                  class="ivu-tag-text"
                  @click="!functionIsDis ? editParam(1,index) : viewParam(1,index)"
                >{{ item.name }}</span>
                <Icon
                  type="ios-close-empty"
                  @click.native.stop="!functionIsDis && removeParam(1,index)"
                ></Icon>
              </div>
              <div slot="content">
                <p>功能名称：{{item.name}}</p>
                <p>标识符：{{item.key}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="备注" prop="comments">
            <Input
              type="textarea"
              v-model="eventAddForm.comments"
              :maxlength="200"
              :disabled="functionIsDis"
            />
          </FormItem>
        </Form>
      </Row>
      <!-- 这里是事件页面 结束-->
      <!-- 这里是属性页面 开始-->
      <Row v-show="radioValue==2">
        <Form
          ref="propertyAddForm"
          :model="propertyAddForm"
          :label-width="75"
          :rules="propertyAddFormValidate"
        >
          <FormItem label="产品类型" prop="typeId">
            <Select v-model="propertyAddForm.typeId" :disabled="true">
              <Option v-for="item in typeList" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="功能名称" prop="propertyId">
            <Input
              v-model="propertyAddForm.propertyId"
              :maxlength="32"
              @on-focus="showSpacialPropertyFormBox"
              @input="propertyfilter"
              @on-blur="propertyConfirm"
              :disabled="functionIsDis"
            />
            <ul class="spacialPropertyFormBox spacialFormBox">
              <li
                v-for="(item,index) in propertyNameList"
                :key="index"
                class="ivu-select-item spacialPropertyFormBoxItem"
                @click="hideSpacialPropertyFormBox(item.name,item.id)"
              >{{item.name}}</li>
            </ul>
          </FormItem>
          <FormItem label="标识符" prop="key">
            <Input
              v-model="propertyAddForm.key"
              :maxlength="64"
              :readonly="propertyAddForm.propertyId==''"
              :disabled="functionIsDis"
            />
          </FormItem>
          <FormItem label="数据类型" prop="dataType">
            <Select v-model="propertyAddForm.dataType" :disabled="functionIsDis">
              <Option
                v-for="item in dataTypeList"
                :key="item.value"
                :value="item.value"
              >{{item.label}}</Option>
            </Select>
          </FormItem>
          <!-- <FormItem label="取值范围">
            <Row>
              <Col span="11">
                <FormItem prop="min">
                  <InputNumber
                    v-model="propertyAddForm.min"
                    :maxlength="30"
                    :disabled="functionIsDis"
                  ></InputNumber>
                </FormItem>
              </Col>
              <Col span="2" style="text-align: center">-</Col>
              <Col span="11">
                <FormItem prop="max">
                  <InputNumber
                    v-model="propertyAddForm.max"
                    :maxlength="30"
                    :disabled="functionIsDis"
                  ></InputNumber>
                </FormItem>
              </Col>
            </Row>
          </FormItem>
          <FormItem label="步长" prop="step">
            <InputNumber v-model="propertyAddForm.step" :maxlength="10" :disabled="functionIsDis"></InputNumber>
          </FormItem>-->
          <FormItem label="单位" prop="unit">
            <Select v-model="propertyAddForm.unit" :disabled="functionIsDis">
              <Option v-for="item in unitList" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="读写类型" prop="wrType">
            <RadioGroup v-model="propertyAddForm.wrType">
              <Radio :label="1" :disabled="functionIsDis&&propertyAddForm.wrType!=1">读写</Radio>
              <Radio :label="2" :disabled="functionIsDis&&propertyAddForm.wrType!=2">只读</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="报警表达式" prop="alarmExpression">
            <Input
              v-model="propertyAddForm.alarmExpression"
              :maxlength="128"
              :disabled="functionIsDis"
              style="width : 85%"
            />
            <Tooltip placement="right">
              <Icon style="margin-left : 20px" type="help-circled" color="blue" :size="20"></Icon>
              <div slot="content">
                <p>符号解释：</p>
                <p>self：表示当前参数</p>
                <p>$：可调用已存在的参数</p>
                <p>&&：表示并且</p>
                <p>||：表示或者</p>
                <p>!：表示非</p>
                <p>>：表示大于</p>
                <p><：表示小于</p>
                <p>>=：表示大于等于</p>
                <p><=：表示小于等于</p>
                <p>==：表示等于</p>
                <p>!=：表示不等于</p>
                <p>示例1：self<=20||self>=60</p>
                <p>表示当前参数在20-60之间为假，否则为真</p>
                <p>示例2：$temp!=100</p>
                <p>表示标识符为temp的参数不等于100为真，</p>
                <p>否则为假</p>
                <p>示例3：!($temp==100)</p>
                <p>表示标识符为temp的参数不等于100为真，</p>
                <p>否则为假</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="备注" prop="comments">
            <Input
              type="textarea"
              v-model="propertyAddForm.comments"
              :maxlength="200"
              :disabled="functionIsDis"
            />
          </FormItem>
        </Form>
      </Row>
      <!-- 这里是属性页面 结束-->
      <!-- 这里是方法页面 开始-->
      <Row v-show="radioValue==3">
        <Form
          ref="methodAddForm"
          :model="methodAddForm"
          :label-width="75"
          :rules="methodAddFormValidate"
        >
          <FormItem label="产品类型" prop="typeId">
            <Select v-model="methodAddForm.typeId" :disabled="true">
              <Option v-for="item in typeList" :key="item.id" :value="item.id">{{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="功能名称" prop="methodId">
            <Input
              v-model="methodAddForm.methodId"
              :maxlength="32"
              @on-focus="showSpacialMethodFormBox"
              @input="methodfilter"
              @on-blur="methodConfirm"
              :disabled="functionIsDis"
            />
            <ul class="spacialMethodFormBox spacialFormBox">
              <li
                v-for="(item,index) in methodNameList"
                :key="index"
                class="ivu-select-item spacialMethodFormBoxItem"
                @click="hideSpacialMethodFormBox(item.name,item.id)"
              >{{item.name}}</li>
            </ul>
          </FormItem>
          <FormItem label="标识符" prop="key">
            <Input
              v-model="methodAddForm.key"
              :maxlength="32"
              :readonly="methodAddForm.methodId==''"
              :disabled="functionIsDis"
            />
          </FormItem>
          <FormItem label="调用方式" prop="isSync">
            <RadioGroup v-model="methodAddForm.isSync">
              <Radio :label="1" :disabled="functionIsDis">同步</Radio>
              <Radio :label="0" :disabled="functionIsDis">异步</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="输入参数" prop="inParams">
            <Button type="primary" @click="addParamButton(2)" :disabled="functionIsDis">增加参数</Button>
            <Tooltip
              placement="top"
              v-for="(item,index) in methodAddForm.inList"
              :key="index"
              class="parameterTooltip"
            >
              <!-- <Tag :name="item.name+'#2'" :closable="true" @on-close="removeParam">{{item.name}}</Tag> -->
              <div class="ivu-tag ivu-tag-checked parameterTag">
                <span
                  class="ivu-tag-text"
                  @click="!functionIsDis ?editParam(2,index):viewParam(2, index)"
                >{{ item.name }}</span>
                <Icon
                  type="ios-close-empty"
                  @click.native.stop="!functionIsDis &&removeParam(2,index)"
                ></Icon>
              </div>
              <div slot="content">
                <p>功能名称：{{item.name}}</p>
                <p>标识符：{{item.key}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="输出参数" prop="outParams">
            <Button type="primary" @click="addParamButton(3)" :disabled="functionIsDis">增加参数</Button>
            <Tooltip
              placement="bottom"
              v-for="(item,index) in methodAddForm.outList"
              :key="index"
              class="parameterTooltip"
            >
              <!-- <Tag :name="item.name+'#3'" :closable="true" @on-close="removeParam">{{item.name}}</Tag> -->
              <div class="ivu-tag ivu-tag-checked parameterTag">
                <span
                  class="ivu-tag-text"
                  @click="!functionIsDis ?editParam(3,index):viewParam(3,index)"
                >{{ item.name }}</span>
                <Icon
                  type="ios-close-empty"
                  @click.native.stop="!functionIsDis &&removeParam(3,index)"
                ></Icon>
              </div>
              <div slot="content">
                <p>功能名称：{{item.name}}</p>
                <p>标识符：{{item.key}}</p>
              </div>
            </Tooltip>
          </FormItem>
          <FormItem label="备注" prop="comments">
            <Input
              type="textarea"
              v-model="methodAddForm.comments"
              :maxlength="200"
              :disabled="functionIsDis"
            />
          </FormItem>
        </Form>
      </Row>
      <!-- 这里是方法页面 结束-->
      <div slot="footer">
        <Button type="text" @click="cancelFunction">取消</Button>
        <Button v-if="functionIsDis" type="primary" @click="functionAddFormVisible = false">确认</Button>
        <Button
          v-if="!functionIsDis"
          type="primary"
          :loading="submitLoading"
          @click="submitProperty"
          :disabled="functionIsDis"
        >提交</Button>
      </div>
    </Modal>
    <!-- 参数页面 -->
    <Modal
      :title="paramModalTitle"
      v-model="paramVisible"
      :mask-closable="false"
      :width="500"
      :closable="false"
    >
      <Form
        ref="paramAddForm"
        :model="paramAddForm"
        :label-width="75"
        :rules="paramAddFormValidate"
      >
        <FormItem label="参数名称" prop="name">
          <Input v-model="paramAddForm.name" :maxlength="64" :disabled="functionIsDis"/>
        </FormItem>
        <FormItem label="标识符" prop="key">
          <Input v-model="paramAddForm.key" :maxlength="64" :disabled="functionIsDis"/>
        </FormItem>
        <FormItem label="数据类型" prop="dataType">
          <Select v-model="paramAddForm.dataType" :disabled="functionIsDis">
            <Option
              v-for="item in dataTypeList"
              :key="item.value"
              :value="Number(item.value)"
            >{{item.label}}</Option>
          </Select>
        </FormItem>
        <!-- <FormItem label="取值范围">
          <Row>
            <Col span="11">
              <FormItem prop="min">
                <InputNumber v-model="paramAddForm.min" :maxlength="30"></InputNumber>
              </FormItem>
            </Col>
            <Col span="2" style="text-align: center">-</Col>
            <Col span="11">
              <FormItem prop="max">
                <InputNumber v-model="paramAddForm.max" :maxlength="30"></InputNumber>
              </FormItem>
            </Col>
          </Row>
        </FormItem>
        <FormItem label="步长" prop="step">
          <InputNumber v-model="paramAddForm.step" :maxlength="10"></InputNumber>
        </FormItem>-->
        <FormItem label="单位" prop="unit">
          <Select v-model="paramAddForm.unit" :disabled="functionIsDis">
            <Option v-for="item in unitList" :key="item.id" :value="item.id">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="备注" prop="comments">
          <Input
            type="textarea"
            v-model="paramAddForm.comments"
            :maxlength="200"
            :disabled="functionIsDis"
          />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelParam" v-if="!functionIsDis">取消</Button>
        <Button type="primary" @click="submitParam" v-if="!paramIsDis && !functionIsDis">提交</Button>
        <Button type="primary" @click="paramVisible = false" v-if="functionIsDis">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import { getStore } from "@/utils/storage.js";
import { validateKey } from "@/libs/util.js";
export default {
  name: "product",
  data() {
    let checkKey = (rule, value, callback) => {
      if (!validateKey(value) && value != "") {
        return callback(new Error("标识符只能为英文大小写字母和数字"));
      } else {
        return callback();
      }
    };
    let checkAlarmExpression = (rule, value, callback) => {
      value = value.replace(/\s*/g, "");
      if (value == "") {
        return callback();
      }
      let reg = /^\$[a-zA-Z\d]$/;
      if (value == "self" || reg.test(value)) {
        return callback(new Error("报警表达式格式不对"));
      }
      value = value.replace(/\$[a-zA-Z\d]+|self/g, "1");
      console.log(value);
      let regx = /[a-zA-Z]+/;
      let errorWord = value.match(regx);
      if (errorWord != "" && errorWord != null) {
        return callback(
          new Error(errorWord + "不正确,是否要输入$" + errorWord)
        );
      }
      try {
        eval(value);
        return callback();
      } catch (err) {
        console.log(err);
        return callback(new Error("报警表达式格式不对"));
      }
    };
    return {
      cancelModal: false,
      split: 0.5,
      loading: true,
      selectCount: 0,
      total: 0,
      isUse: true,
      isDisd: true,
      isBatch: true,
      selectList: [],
      addFormVisible: false,
      editFormVisible: false,
      isDis: false,
      modalTitle: "",
      pageNum: 1,
      pageSize: 10,
      searchForm: {
        keyword: ""
      },
      addForm: {},
      addFormValidate: {
        name: [{ required: true, message: "请输入产品名称", trigger: "blur" }],
        type: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        linkType: [
          {
            required: true,
            message: "请选择接入方式"
          }
        ],
        version: [{ required: true, message: "请输入版本", trigger: "blur" }]
      },
      editForm: {},
      editFormValidate: {
        name: [{ required: true, message: "请输入产品名称", trigger: "blur" }],
        productType: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        linkType: [
          {
            required: true,
            message: "请选择接入方式"
          }
        ],
        version: [{ required: true, message: "请输入版本", trigger: "blur" }]
      },
      submitLoading: false,
      columns: [
        {
          type: "index",
          width: 60,
          align: "center"
        },
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "产品类型",
          key: "typeName",
          align: "center",
          tooltip: true,
          width: 180
        },
        {
          title: "产品名称",
          key: "name",
          align: "center",
          tooltip: true,
          width: 180
        },
        {
          title: "产品唯一编号",
          key: "productKey",
          align: "center",
          tooltip: true,
          width: 250
        },
        {
          title: "接入方式",
          key: "linkTypeName",
          tooltip: true,
          align: "center",
          width: 180
        },
        {
          title: "设备数量",
          key: "deviceNum",
          align: "center",
          width: 180
        },
        {
          title: "状态",
          key: "status",
          align: "center",
          render: (h, params) => {
            let re = params.row.status;
            if (re === 0) {
              return h("div", "开发中");
            } else if (re === 1) {
              return h("div", "已发布");
            }
          },
          width: 180
        },
        {
          title: "操作时间",
          key: "modifyTime",
          align: "center",
          render: (h, params) => {
            let re = params.row.modifyTime;
            if (re == undefined || re == null || re == "") {
              return h("div", "");
            }
            re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
            return h("div", re);
          },
          width: 180
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
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.check(params.row);
                    }
                  }
                },
                "查看"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small",
                    disabled: params.row.status == 1
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.edit(params.row);
                    }
                  }
                },
                "编辑"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small",
                    disabled: params.row.status == 1 //true 禁用 ; false启用
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.remove(params.row.id);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      data: [],
      linkTypeList: [],
      typeList: [],
      functionColumns: [
        {
          type: "index",
          width: 60,
          align: "center"
        },
        {
          title: "产品类型",
          key: "productType",
          align: "center",
          ellipsis: true,
          width: 180
        },
        {
          title: "功能类型",
          key: "functionType",
          align: "center",
          ellipsis: true,
          width: 180
        },
        {
          title: "功能名称",
          key: "name",
          align: "center",
          width: 180
        },
        {
          title: "标识符",
          key: "key",
          align: "center",
          ellipsis: true,
          width: 180
        },
        {
          title: "备注",
          key: "comments",
          align: "center",
          minWidth: 180,
          maxWidth: 1000,
          tooltip: true
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
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.checkFunction(params.row);
                    }
                  }
                },
                "查看"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.editFunction(params.row);
                    }
                  }
                },
                "编辑"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.removeFunction(params.row);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      functionTotal: 0,
      functionData: [],
      functionPageNum: 1,
      functionPageSize: 10,
      functionLoading: false,
      functionModalTitle: "",
      functionAddFormVisible: false,
      radioValue: 2,
      functionIsDis: false,
      propertyAddForm: {},

      propertyAddFormValidate: {
        typeId: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        propertyId: [{ required: true, message: "请输入功能名称" }],
        key: [
          { required: true, message: "请输入标识符", trigger: "blur" },
          { validator: checkKey, trigger: "blur" }
        ],
        dataType: [
          {
            required: true,
            message: "请选择数据类型"
          }
        ],
        unit: [
          {
            required: true,
            message: "请选择单位"
          }
        ],
        wrType: [
          {
            required: true,
            message: "请选择读写类型"
          }
        ],
        alarmExpression: [
          { required: true, message: "请输入报警表达式", trigger: "blur" },
          { validator: checkAlarmExpression, trigger: "blur" }
        ]
      },
      eventAddForm: {},
      eventAddFormValidate: {
        typeId: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        eventId: [{ required: true, message: "请输入功能名称" }],
        key: [
          { required: true, message: "请输入标识符", trigger: "blur" },
          { validator: checkKey, trigger: "blur" }
        ],
        eventType: [
          {
            required: true,
            message: "请选择事件类型"
          }
        ]
      },
      methodAddForm: {},
      methodAddFormValidate: {
        typeId: [
          {
            required: true,
            message: "请选择产品类型"
          }
        ],
        methodId: [{ required: true, message: "请输入功能名称" }],
        key: [
          { required: true, message: "请输入标识符", trigger: "blur" },
          { validator: checkKey, trigger: "blur" }
        ],
        isSync: [
          {
            required: true,
            message: "请选择调用方式"
          }
        ]
      },
      dataTypeList: [
        { label: "int", value: 1 },
        { label: "float", value: 2 },
        { label: "double", value: 3 },
        { label: "bool", value: 4 },
        { label: "string", value: 5 },
        { label: "date", value: 6 }
      ],
      unitList: [],
      paramModalTitle: "",
      paramVisible: false,
      paramAddForm: {},
      paramAddFormValidate: {
        name: [{ required: true, message: "请输入参数名称", trigger: "blur" }],
        key: [
          { required: true, message: "请输入标识符", trigger: "blur" },
          { validator: checkKey, trigger: "blur" }
        ],
        dataType: [
          {
            required: true,
            message: "请选择数据类型"
          }
        ],
        unit: [
          {
            required: true,
            message: "请选择单位"
          }
        ]
      },
      paramIsDis: false,
      rowId: "",
      functionId: "",
      paramType: "",
      rowData: "",
      eventNameList: [],
      propertyNameList: [],
      methodNameList: [],
      eventName: "",
      propertyName: "",
      methodName: "",
      paramTempo: {},
      indexTempo: new Number()
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
        selectColumns: [],
        order: {},
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
      this.postBusRequest("/product/selectProduct", params).then(res => {
        if (res.code == 200) {
          this.loading = false;
          this.data = res.data.list;
          console.log(this.data);
          this.data.forEach((el, i) => {
            if (i == 0) {
              el._highlight = true;
            }
          });
          this.columns = [
            {
              type: "index",
              width: 60,
              align: "center"
            },
            {
              type: "selection",
              width: 60,
              align: "center"
            },
            {
              title: "产品类型",
              key: "typeName",
              align: "center",
              tooltip: true,
              width: 180
            },
            {
              title: "产品名称",
              key: "name",
              align: "center",
              tooltip: true,
              width: 180
            },
            {
              title: "产品唯一编号",
              key: "productKey",
              align: "center",
              tooltip: true,
              width: 250
            },
            {
              title: "接入方式",
              key: "linkTypeName",
              tooltip: true,
              align: "center",
              width: 180
            },
            {
              title: "设备数量",
              key: "deviceNum",
              align: "center",
              width: 180
            },
            {
              title: "状态",
              key: "status",
              align: "center",
              render: (h, params) => {
                let re = params.row.status;
                if (re === 0) {
                  return h("div", "开发中");
                } else if (re === 1) {
                  return h("div", "已发布");
                }
              },
              width: 180
            },
            {
              title: "操作时间",
              key: "modifyTime",
              align: "center",
              render: (h, params) => {
                let re = params.row.modifyTime;
                if (re == undefined || re == null || re == "") {
                  return h("div", "");
                }
                re = this.moment(re).format("YYYY-MM-DD HH:mm:ss");
                return h("div", re);
              },
              width: 180
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
                        size: "small"
                      },
                      style: {
                        marginRight: "5px"
                      },
                      on: {
                        click: () => {
                          this.check(params.row);
                        }
                      }
                    },
                    "查看"
                  ),
                  h(
                    "Button",
                    {
                      props: {
                        type: "success",
                        size: "small",
                        disabled: params.row.status == 1
                      },
                      style: {
                        marginRight: "5px"
                      },
                      on: {
                        click: () => {
                          this.edit(params.row);
                        }
                      }
                    },
                    "编辑"
                  ),
                  h(
                    "Button",
                    {
                      props: {
                        type: "error",
                        size: "small",
                        disabled:
                          params.row.status == 1 || params.row.deviceNum > 0
                      },
                      style: {
                        marginRight: "5px"
                      },
                      on: {
                        click: () => {
                          this.remove(params.row.id);
                        }
                      }
                    },
                    "删除"
                  )
                ]);
              }
            }
          ];
          this.total = res.data.total;
          this.clearSelect();
          if (this.data.length > 0) {
            this.rowId = this.data[0].id;
            this.getFunctionList(this.rowId);
          }
          this.getFunctionList(this.data[0]);
        }
      });
    },
    //模糊查询
    handleSearch() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.init();
    },
    //提交
    submitDict() {
      if (this.modalTitle == "新增产品") {
        this.$refs.addForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "新增",
              content: "确认保存吗？",
              onOk: () => {
                this.addForm.status = 0;
                this.postBusRequest("/product/insert", this.addForm).then(
                  res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.addFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  }
                );
              }
            });
          }
        });
      }
      if (this.modalTitle == "编辑产品") {
        this.$refs.editForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "编辑",
              content: "确认保存吗？",
              onOk: () => {
                this.postBusRequest("/product/update", this.editForm).then(
                  res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.editFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  }
                );
              }
            });
          }
        });
      }
    },
    //取消提交
    cancelDict() {
      if (this.modalTitle == "新增产品") {
        this.$Modal.confirm({
          title: "取消",
          content: "新增内容将不被保存，是否确认取消？",
          onOk: () => {
            this.addFormVisible = false;
          }
        });
      } else if (this.modalTitle == "编辑产品") {
        this.$Modal.confirm({
          title: "取消",
          content: "修改内容将不被保存，是否确认取消？",
          onOk: () => {
            this.editFormVisible = false;
          }
        });
      } else {
        this.editFormVisible = false;
      }
    },
    //新增打开弹窗
    addRoot() {
      this.modalTitle = "新增产品";
      this.$refs.addForm.resetFields();
      this.addFormVisible = true;
      this.isDis = false;
    },
    //编辑打开弹窗
    edit(v) {
      this.modalTitle = "编辑产品";
      this.isDis = false;
      this.$refs.editForm.resetFields();
      this.postBusRequest("/product/selectById", { id: v.id }).then(res => {
        this.editForm = res.data;
        this.editFormVisible = true;
      });
    },
    //单条删除
    remove: function(v) {
      this.$Modal.confirm({
        title: "删除",
        content: "确认删除选中记录吗？",
        onOk: () => {
          this.postBusRequest("/product/deleteProduct", {
            ids: v.toString().split(",")
          }).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
              this.functionData = [];
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //查看打开弹窗
    check(v) {
      this.modalTitle = "查看产品";
      this.isDis = true;
      this.$refs.editForm.resetFields();
      this.postBusRequest("/product/selectById", { id: v.id }).then(res => {
        this.editForm = res.data;
        this.editFormVisible = true;
      });
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
    },
    //勾选时给全局变量赋值
    showSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
      //循环判断启用按钮和批量删除按钮是否可用
      // true 置灰； false 可点
      for (let i = 0; i < e.length; i++) {
        if (e[i].status === 0 && e[i].deviceNum < 1) {
          this.isBatch = false;
        } else {
          this.isBatch = true;
          break;
        }
      }
      //循环判断禁用按钮是否可用
      for (let i = 0; i < e.length; i++) {
        if (e[i].status === 1) {
          this.isDisd = false;
        } else {
          this.isDisd = true;

          break;
        }
      }

      this.isUse = !this.isDisd;
    },
    //批量删除
    delAll: function() {
      this.$Modal.confirm({
        title: "删除",
        content: "确认删除选中记录吗？",
        onOk: () => {
          this.postBusRequest("/product/delete", {
            ids: this.selectList.map(item => item.id)
          }).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //启用
    setUse: function() {
      let params = {
        productInfos: []
      };
      for (let i = 0; i < this.selectList.length; i++) {
        params.productInfos[i] = {
          id: this.selectList[i].id,
          status: 1,
          modifyUser: JSON.parse(this.cookies.get("userInfo")).id,
          modifyUserName: JSON.parse(this.cookies.get("userInfo")).username
        };
      }
      this.$Modal.confirm({
        title: "启用",
        content: "确认启用选中记录吗？",
        onOk: () => {
          this.postBusRequest("/product/setUse", params).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //禁用
    setDis: function() {
      let params = {
        productInfos: []
      };
      for (let i = 0; i < this.selectList.length; i++) {
        params.productInfos[i] = {
          id: this.selectList[i].id,
          status: 0,
          modifyUser: JSON.parse(this.cookies.get("userInfo")).id,
          modifyUserName: JSON.parse(this.cookies.get("userInfo")).username
        };
      }
      this.$Modal.confirm({
        title: "禁用",
        content: "确认禁用选中记录吗？",
        onOk: () => {
          this.postBusRequest("/product/setDis", params).then(res => {
            if (res.code == 200) {
              this.$Message.success(res.msg);
              this.init();
            } else {
              this.$Message.error(res.msg);
            }
          });
        }
      });
    },
    //获取类型列表
    getTypeList() {
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
        this.typeList = res.data;
      });
    },
    //功能翻页
    functionChangePage: function(v) {
      this.functionPageNum = v;
      this.getFunctionList(this.rowId);
    },
    //功能改变显示条数
    functionChangePageSize: function(v) {
      this.functionPageSize = v;
      this.getFunctionList(this.rowId);
    },
    //获取功能列表
    getFunctionList: function(v) {
      if ("number" != typeof v) {
        console.log(v);
        this.rowData = v;
        v = v.id;
      }
      console.log(this.rowData);
      let params = {
        params: [
          {
            id: "id",
            condition: "=",
            value: v
          }
        ],
        pageNum: this.functionPageNum,
        pageSize: this.functionPageSize
      };
      this.rowId = v;
      console.log(params);
      this.functionLoading = true;
      this.postBusRequest("/product/getFunctionList", params).then(res => {
        this.functionLoading = false;
        this.functionData = res.data.list;
        this.functionTotal = res.data.total;
      });
    },
    //功能提交
    submitProperty: function() {
      if (this.functionModalTitle == "新增功能") {
        if (this.radioValue == 1) {
          this.$refs.eventAddForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "新增",
                content: "确认保存吗？",
                onOk: () => {
                  this.eventAddForm.productId = this.rowId;
                  this.eventAddForm.name = this.eventAddForm.eventId;
                  this.postBusRequest(
                    "/productEvent/insertProductEvent",
                    this.eventAddForm
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.functionAddFormVisible = false;
                      this.getFunctionList(this.rowId);
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        } else if (this.radioValue == 2) {
          this.$refs.propertyAddForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "新增",
                content: "确认保存吗？",
                onOk: () => {
                  this.propertyAddForm.productId = this.rowId;
                  this.propertyAddForm.name = this.propertyAddForm.propertyId;
                  this.postBusRequest(
                    "/productProperty/insert",
                    this.propertyAddForm
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.functionAddFormVisible = false;
                      this.getFunctionList(this.rowId);
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        } else if (this.radioValue == 3) {
          this.$refs.methodAddForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "新增",
                content: "确认保存吗？",
                onOk: () => {
                  this.methodAddForm.productId = this.rowId;
                  this.methodAddForm.name = this.methodAddForm.methodId;
                  this.postBusRequest(
                    "/productMethod/insertProductMethod",
                    this.methodAddForm
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.functionAddFormVisible = false;
                      this.getFunctionList(this.rowId);
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }
      }
      if (this.functionModalTitle == "编辑功能") {
        if (this.radioValue == 1) {
          this.$refs.eventAddForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "编辑",
                content: "确认保存吗？",
                onOk: () => {
                  this.eventAddForm.productId = this.rowId;
                  this.eventAddForm.name = this.eventAddForm.eventId;
                  this.postBusRequest(
                    "/productEvent/updateProductEvent",
                    this.eventAddForm
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.functionAddFormVisible = false;
                      this.getFunctionList(this.rowId);
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        } else if (this.radioValue == 2) {
          this.$refs.propertyAddForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "编辑",
                content: "确认保存吗？",
                onOk: () => {
                  this.propertyAddForm.productId = this.rowId;
                  this.propertyAddForm.name = this.propertyAddForm.propertyId;
                  this.postBusRequest(
                    "/productProperty/update",
                    this.propertyAddForm
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.functionAddFormVisible = false;
                      this.getFunctionList(this.rowId);
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        } else if (this.radioValue == 3) {
          this.$refs.methodAddForm.validate(valid => {
            if (valid) {
              this.$Modal.confirm({
                title: "编辑",
                content: "确认保存吗？",
                onOk: () => {
                  this.methodAddForm.productId = this.rowId;
                  this.methodAddForm.name = this.methodAddForm.methodId;
                  this.postBusRequest(
                    "/productMethod/updateProductMethod",
                    this.methodAddForm
                  ).then(res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.functionAddFormVisible = false;
                      this.getFunctionList(this.rowId);
                    } else {
                      this.$Message.error(res.msg);
                    }
                  });
                }
              });
            }
          });
        }
      }
    },
    //功能新增打开弹窗
    addFunction: function() {
      if (!this.rowId) {
        this.$Message.error("请先选择要增加功能的产品");
        return;
      }
      this.eventAddForm.list = [];
      this.methodAddForm.inList = [];
      this.methodAddForm.outList = [];
      this.functionModalTitle = "新增功能";
      if (this.radioValue == 1) {
        this.$refs.eventAddForm.resetFields();
        this.eventAddForm = {};
      } else if (this.radioValue == 2) {
        this.$refs.propertyAddForm.resetFields();
        this.propertyAddForm = {};
      } else if (this.radioValue == 3) {
        this.$refs.methodAddForm.resetFields();
        this.methodAddForm = {};
      }
      this.functionAddFormVisible = true;
      this.radioValue = 1;
      this.functionIsDis = false;
      this.eventAddForm.typeId = this.rowData.type;
      this.propertyAddForm.typeId = this.rowData.type;
      this.methodAddForm.typeId = this.rowData.type;
      this.getNameList();
    },
    //功能查看打开弹窗
    checkFunction: function(v) {
      this.getNameList();
      this.functionModalTitle = "查看功能";
      if (this.radioValue == 1) {
        this.$refs.eventAddForm.resetFields();
      } else if (this.radioValue == 2) {
        this.$refs.propertyAddForm.resetFields();
      } else if (this.radioValue == 3) {
        this.$refs.methodAddForm.resetFields();
      }
      this.functionIsDis = true;
      // 转换null为""
      for (let attr in v) {
        if (v[attr] === null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      if (v.functionType == "事件") {
        this.radioValue = 1;
        this.postBusRequest("/productEventParam/selectById", { id: v.id }).then(
          res => {
            this.eventAddForm = res.data;
            this.eventAddForm.typeId = data.typeId;
            this.eventAddForm.eventId = this.eventAddForm.name;
          }
        );
      } else if (v.functionType == "方法") {
        this.radioValue = 3;
        this.postBusRequest("/productMethodParam/selectById", {
          id: v.id
        }).then(res => {
          this.methodAddForm = res.data;
          this.methodAddForm.typeId = data.typeId;
          for (let i = 0; i < this.methodNameList.length; i++) {
            if (this.methodNameList[i].name == res.data.name) {
              this.methodAddForm = Object.assign({}, this.methodAddForm, {
                methodId: this.methodNameList[i].id
              });
            }
          }
        });
      } else if (v.functionType == "属性") {
        this.radioValue = 2;
        this.postBusRequest("/productProperty/selectById", { id: v.id }).then(
          res => {
            this.propertyAddForm = res.data;
            this.propertyAddForm.typeId = data.typeId;
            this.propertyAddForm.propertyId = this.propertyAddForm.name;
            // for (let i = 0; i < this.propertyNameList.length; i++) {
            //   if (this.propertyNameList[i].name == res.data.name) {
            //     this.propertyAddForm.propertyId = this.propertyNameList[i].id;
            //   }
            // }
          }
        );
      }
      this.functionAddFormVisible = true;
    },
    //功能编辑打开弹窗
    editFunction: function(v) {
      this.getNameList();
      this.functionModalTitle = "编辑功能";
      this.functionIsDis = false;
      if (this.radioValue == 1) {
        this.$refs.eventAddForm.resetFields();
      } else if (this.radioValue == 2) {
        this.$refs.propertyAddForm.resetFields();
      } else if (this.radioValue == 3) {
        this.$refs.methodAddForm.resetFields();
      }
      // 转换null为""
      for (let attr in v) {
        if (v[attr] === null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      if (v.functionType == "事件") {
        this.radioValue = 1;
        this.postBusRequest("/productEventParam/selectById", { id: v.id }).then(
          res => {
            console.log(res.data);
            this.eventAddForm = res.data;
            this.eventAddForm.typeId = data.typeId;
            this.eventAddForm.eventId = this.eventAddForm.name;
            // for (let i = 0; i < this.eventNameList.length; i++) {
            //   if (this.eventNameList[i].name == res.data.name) {
            //     this.eventAddForm.eventId = this.eventNameList[i].id;
            //   }
            // }
          }
        );
      } else if (v.functionType == "方法") {
        this.radioValue = 3;
        this.postBusRequest("/productMethodParam/selectById", {
          id: v.id
        }).then(res => {
          this.methodAddForm = res.data;
          this.methodAddForm.typeId = data.typeId;
          this.methodAddForm.methodId = this.methodAddForm.name;
          // for (let i = 0; i < this.methodNameList.length; i++) {
          //   if (this.methodNameList[i].name == res.data.name) {
          //     this.methodAddForm = Object.assign({}, this.methodAddForm, {
          //       methodId: this.methodNameList[i].id
          //     });
          //   }
          // }
        });
      } else if (v.functionType == "属性") {
        this.radioValue = 2;
        this.postBusRequest("/productProperty/selectById", { id: v.id }).then(
          res => {
            this.propertyAddForm = res.data;
            this.propertyAddForm.typeId = data.typeId;
            this.propertyAddForm.propertyId = this.propertyAddForm.name;
            // for (let i = 0; i < this.propertyNameList.length; i++) {
            //   if (this.propertyNameList[i].name == res.data.name) {
            //     this.propertyAddForm.propertyId = this.propertyNameList[i].id;
            //   }
            // }
          }
        );
      }
      this.functionAddFormVisible = true;
    },
    //功能单条删除
    removeFunction: function(v) {
      this.$Modal.confirm({
        title: "删除",
        content: "确认删除选中记录吗？",
        onOk: () => {
          let url = "";
          if (v.functionType == "事件") {
            url = "/productEvent/deleteProductEvent";
          } else if (v.functionType == "属性") {
            url = "/productProperty/delete";
          } else if (v.functionType == "方法") {
            url = "/productMethod/deleteProductMethod";
          }
          this.postBusRequest(url, { ids: v.id.toString().split(",") }).then(
            res => {
              if (res.code == 200) {
                this.$Message.success(res.msg);
                this.getFunctionList(this.rowId);
              } else {
                this.$Message.error(res.msg);
              }
            }
          );
        }
      });
    },
    //获取单位列表
    getUnitList() {
      let params = {
        selectColumns: ["id", "name"],
        order: {
          open: true,
          name: "create_time",
          isAsc: false
        },
        params: []
      };
      this.loading = true;
      this.postBusRequest("/master/unit/selectList", params).then(res => {
        this.unitList = res.data;
      });
    },
    //取消功能提交
    cancelFunction: function() {
      if (this.functionModalTitle == "新增功能") {
        this.$Modal.confirm({
          title: "取消",
          content: "新增内容将不被保存，是否确认取消？",
          onOk: () => {
            this.functionAddFormVisible = false;
          }
        });
      } else if (this.functionModalTitle == "编辑功能") {
        this.$Modal.confirm({
          title: "取消",
          content: "修改内容将不被保存，是否确认取消？",
          onOk: () => {
            this.functionAddFormVisible = false;
          }
        });
      } else {
        this.functionAddFormVisible = false;
      }
    },
    //新增参数打开弹窗
    addParamButton: function(v) {
      this.paramVisible = true;
      this.paramModalTitle = "新增参数";
      this.$refs.paramAddForm.resetFields();
      // this.paramAddForm = {};
      this.paramType = v;
      let list = this.eventAddForm.list;
      let inList = this.methodAddForm.inList;
      let outList = this.methodAddForm.outList;
      if (v == 1 && (list == null || list == undefined || list == "")) {
        this.eventAddForm.list = [];
      } else if (
        v == 2 &&
        (inList == null || inList == undefined || inList == "")
      ) {
        this.paramAddForm = Object.assign({}, this.paramAddForm, {
          paramType: 1
        });

        this.methodAddForm.inList = [];
      } else if (
        v == 3 &&
        (outList == null || outList == undefined || outList == "")
      ) {
        this.paramAddForm = Object.assign({}, this.paramAddForm, {
          paramType: 2
        });
        this.methodAddForm.outList = [];
      }
    },
    //修改参数
    editParam: function(paramType, index) {
      this.paramModalTitle = "修改参数";
      this.paramType = paramType;
      this.indexTempo = index;
      if (this.paramType == 1) {
        this.paramTempo = JSON.parse(
          JSON.stringify(this.eventAddForm.list[index])
        );
        this.paramAddForm = JSON.parse(
          JSON.stringify(this.eventAddForm.list[index])
        );
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 2) {
        this.paramTempo = JSON.parse(
          JSON.stringify(this.methodAddForm.inList[index])
        );
        this.paramAddForm = JSON.parse(
          JSON.stringify(this.methodAddForm.inList[index])
        );
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 3) {
        this.paramTempo = JSON.parse(
          JSON.stringify(this.methodAddForm.outList[index])
        );
        this.paramAddForm = JSON.parse(
          JSON.stringify(this.methodAddForm.outList[index])
        );
        console.log(this.paramAddForm);
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      }
    },
    //查看参数
    viewParam: function(paramType, index) {
      this.paramModalTitle = "查看参数";
      this.paramType = paramType;
      this.indexTempo = index;
      if (this.paramType == 1) {
        this.paramAddForm = JSON.parse(
          JSON.stringify(this.eventAddForm.list[index])
        );
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 2) {
        this.paramAddForm = JSON.parse(
          JSON.stringify(this.methodAddForm.inList[index])
        );
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      } else if (this.paramType == 3) {
        this.paramAddForm = JSON.parse(
          JSON.stringify(this.methodAddForm.outList[index])
        );
        setTimeout(() => {
          this.paramVisible = true;
        }, 50);
      }
    },
    //参数提交
    submitParam: function() {
      if (this.paramModalTitle == "新增参数") {
        this.$refs.paramAddForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "新增",
              content: "确认保存吗？",
              onOk: () => {
                if (this.paramType == 1) {
                  let list = this.eventAddForm.list;
                  this.paramAddForm.paramType = 2;
                  for (let i = 0; i < list.length; i++) {
                    if (list[i].key == this.paramAddForm.key) {
                      this.$Message.error("事件输出参数已存在");
                      return;
                    }
                  }
                  setTimeout(() => {
                    this.$set(
                      this.eventAddForm.list,
                      this.eventAddForm.list.length,
                      Object.assign({}, this.paramAddForm)
                    );
                    this.paramVisible = false;
                  }, 50);
                } else if (this.paramType == 2) {
                  let list = this.methodAddForm.inList;
                  this.paramAddForm.paramType = 1;
                  for (let i = 0; i < list.length; i++) {
                    if (list[i].key == this.paramAddForm.key) {
                      this.$Message.error("方法输入参数已存在");
                      return;
                    }
                  }
                  setTimeout(() => {
                    this.$set(
                      this.methodAddForm.inList,
                      this.methodAddForm.inList.length,
                      Object.assign({}, this.paramAddForm)
                    );
                    this.paramVisible = false;
                  }, 50);
                } else if (this.paramType == 3) {
                  let list = this.methodAddForm.outList;
                  this.paramAddForm.paramType = 2;
                  for (let i = 0; i < list.length; i++) {
                    if (list[i].key == this.paramAddForm.key) {
                      this.$Message.error("方法输出参数已存在");
                      return;
                    }
                  }
                  setTimeout(() => {
                    this.$set(
                      this.methodAddForm.outList,
                      this.methodAddForm.outList.length,
                      Object.assign({}, this.paramAddForm)
                    );
                    this.paramVisible = false;
                  }, 50);
                }
              }
            });
          }
        });
      } else if (this.paramModalTitle == "修改参数") {
        this.$refs.paramAddForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "修改",
              content: "确认保存吗？",
              onOk: () => {
                if (this.paramType == 1) {
                  let list = this.eventAddForm.list;
                  this.paramAddForm.paramType = 2;
                  for (let i = 0; i < list.length; i++) {
                    if (
                      list[i].key == this.paramAddForm.key &&
                      i != this.indexTempo
                    ) {
                      this.$Message.error("事件输出参数已存在");
                      return;
                    }
                  }
                  setTimeout(() => {
                    this.eventAddForm.list[this.indexTempo] = this.paramAddForm;
                    this.paramVisible = false;
                  }, 50);
                } else if (this.paramType == 2) {
                  let list = this.methodAddForm.inList;
                  this.paramAddForm.paramType = 1;
                  for (let i = 0; i < list.length; i++) {
                    if (
                      list[i].key == this.paramAddForm.key &&
                      i != this.indexTempo
                    ) {
                      this.$Message.error("方法输入参数已存在");
                      return;
                    }
                  }
                  setTimeout(() => {
                    this.methodAddForm.inList[
                      this.indexTempo
                    ] = this.paramAddForm;
                    this.paramVisible = false;
                  }, 50);
                } else if (this.paramType == 3) {
                  console.log(this.indexTempo);
                  let list = this.methodAddForm.outList;
                  this.paramAddForm.paramType = 2;
                  for (let i = 0; i < list.length; i++) {
                    if (
                      list[i].key == this.paramAddForm.key &&
                      i != this.indexTempo
                    ) {
                      this.$Message.error("方法输出参数已存在");
                      return;
                    }
                  }
                  setTimeout(() => {
                    this.methodAddForm.outList[
                      this.indexTempo
                    ] = this.paramAddForm;
                    this.paramVisible = false;
                  }, 50);
                }
              }
            });
          }
        });
      } else if (this.paramModalTitle == "编辑产品") {
        this.$refs.paramAddForm.validate(valid => {
          if (valid) {
            this.$Modal.confirm({
              title: "编辑",
              content: "确认保存吗？",
              onOk: () => {
                this.postBusRequest("/product/update", this.editForm).then(
                  res => {
                    if (res.code == 200) {
                      this.$Message.success(res.msg);
                      this.editFormVisible = false;
                      this.init();
                    } else {
                      this.$Message.error(res.msg);
                    }
                  }
                );
              }
            });
          }
        });
      }
    },
    //删除参数
    removeParam(pType, index) {
      let paramType = pType;
      if (paramType === 1) {
        this.$Modal.confirm({
          title: "取消",
          content: "参数将不被保存，是否确认取消？",
          onOk: () => {
            this.$delete(this.eventAddForm.list, index);
            this.$forceUpdate();
          }
        });
      } else if (paramType === 2) {
        this.$Modal.confirm({
          title: "取消",
          content: "参数将不被保存，是否确认取消？",
          onOk: () => {
            this.$delete(this.methodAddForm.inList, index);
            this.$forceUpdate();
          }
        });
      } else if (paramType === 3) {
        this.$Modal.confirm({
          title: "取消",
          content: "参数将不被保存，是否确认取消？",
          onOk: () => {
            this.$delete(this.methodAddForm.outList, index);
            this.$forceUpdate();
          }
        });
      }
    },
    //取消参数提交
    cancelParam: function() {
      if (this.paramModalTitle == "新增参数") {
        this.$Modal.confirm({
          title: "取消",
          content: "新增内容将不被保存，是否确认取消？",
          onOk: () => {
            this.paramVisible = false;
          }
        });
      } else if (this.paramModalTitle == "修改参数") {
        this.$Modal.confirm({
          title: "取消",
          content: "修改内容将不被保存，是否确认取消？",
          onOk: () => {
            this.paramVisible = false;
            console.log(this.paramTempo);
            if (this.paramType == 1) {
              this.eventAddForm.list[this.indexTempo] = this.paramTempo;
            } else if (this.paramType == 2) {
              this.methodAddForm.inList[this.indexTempo] = this.paramTempo;
            } else if (this.paramType == 3) {
              this.methodAddForm.outList[this.indexTempo] = this.paramTempo;
            }
          }
        });
      }
    },
    clearSelect: function() {
      this.selectCount = 0;
      this.selectList = [];
    },
    getNameList: function() {
      if (this.radioValue == 1) {
        let params = {
          selectColumns: [
            "id",
            "type_id",
            "name",
            "`key`",
            "event_type",
            "comments"
          ],
          order: {},
          params: [
            {
              name: "type_id",
              condition: "=",
              value: this.rowData.type
            }
          ]
        };
        this.postBusRequest("/master/event/selectList", params).then(res => {
          if (res.code == 200) {
            this.eventNameList = res.data;
          } else {
            this.$Message.error(res.msg);
          }
        });
      } else if (this.radioValue == 2) {
        let params = {
          selectColumns: [
            "id",
            "type_id",
            "name",
            "`key`",
            "data_type",
            "min",
            "max",
            "step",
            "bool_false",
            "bool_true",
            "data_length",
            "wr_type",
            "unit",
            "comments"
          ],
          order: {},
          params: [
            {
              name: "type_id",
              condition: "=",
              value: this.rowData.type
            }
          ]
        };
        this.postBusRequest("/master/property/selectList", params).then(res => {
          if (res.code == 200) {
            this.propertyNameList = res.data;
          } else {
            this.$Message.error(res.msg);
          }
        });
      } else if (this.radioValue == 3) {
        let params = {
          selectColumns: [
            "id",
            "type_id",
            "name",
            "`key`",
            "is_sync",
            "comments"
          ],
          order: {},
          params: [
            {
              name: "type_id",
              condition: "=",
              value: this.rowData.type
            }
          ]
        };
        this.postBusRequest("/master/method/selectList", params).then(res => {
          if (res.code == 200) {
            this.methodNameList = res.data;
          } else {
            this.$Message.error(res.msg);
          }
        });
      }
    },
    setEvent: function(v) {
      if (v != null && v != "" && v != undefined) {
        this.postBusRequest("/productFunction/selectEventById", { id: v }).then(
          res => {
            if (res.code == 200) {
              this.eventAddForm = Object.assign({}, this.eventAddForm, {
                name: res.data.name,
                eventId: res.data.id,
                key: res.data.key,
                eventType: res.data.eventType,
                list: res.data.list,
                comments: res.data.comments
              });
            } else {
              this.$Message.error(res.msg);
            }
          }
        );
      }
    },
    setProperty: function(v) {
      if (v != null && v != "" && v != undefined) {
        this.postBusRequest("/master/property/selectById", { id: v }).then(
          res => {
            if (res.code == 200) {
              this.propertyAddForm = Object.assign({}, this.propertyAddForm, {
                name: res.data.name,
                propertyId: res.data.id,
                key: res.data.key,
                dataType: res.data.dataType,
                min: Number(res.data.min),
                max: Number(res.data.max),
                step: Number(res.data.step),
                unit: res.data.unit,
                wrType: res.data.wrType,
                comments: res.data.comments
              });
            } else {
              this.$Message.error(res.msg);
            }
          }
        );
      }
    },
    setMethod: function(v) {
      if (v != null && v != "" && v != undefined) {
        this.postBusRequest("/productFunction/selectMethodById", {
          id: v
        }).then(res => {
          if (res.code == 200) {
            this.methodAddForm = Object.assign({}, this.methodAddForm, {
              name: res.data.name,
              methodId: res.data.id,
              key: res.data.key,
              isSync: res.data.isSync,
              inList: res.data.inList,
              outList: res.data.outList,
              comments: res.data.comments
            });
          } else {
            this.$Message.error(res.msg);
          }
        });
      }
    },
    getLinkTypeList: function() {
      this.postBusRequest("/dictionary/linkType/selectList", {}).then(res => {
        if (res.code == 200) {
          this.linkTypeList = res.data;
        } else {
          this.$Message.error(res.msg);
        }
      });
    },
    // getEventName: function(data) {
    //   console.log(data);
    //   this.eventName = data;
    // },
    // getPropertyName: function(data) {
    //   this.propertyName = data;
    // },
    // getMethodName: function(data) {
    //   this.methodName = data;
    // },
    showSpacialEventFormBox: function() {
      document.querySelector(".spacialEventFormBox").style.height = "auto";
    },
    showSpacialPropertyFormBox: function() {
      document.querySelector(".spacialPropertyFormBox").style.height = "auto";
    },
    showSpacialMethodFormBox: function() {
      document.querySelector(".spacialMethodFormBox").style.height = "auto";
    },
    hideSpacialEventFormBox: function(v, id) {
      console.log(v, id);
      this.eventAddForm.eventId = v;
      if (id != null && id != "" && id != undefined) {
        this.postBusRequest("/productFunction/selectEventById", {
          id: id
        }).then(res => {
          if (res.code == 200) {
            this.eventAddForm = Object.assign({}, this.eventAddForm, {
              name: res.data.name,
              eventId: res.data.name,
              key: res.data.key,
              eventType: res.data.eventType,
              list: res.data.list,
              comments: res.data.comments
            });
            this.$refs.eventAddForm.validateField;
          } else {
            this.$Message.error(res.msg);
          }
        });
      }
      this.$forceUpdate();
      document.querySelector(".spacialEventFormBox").style.height = "0";
    },
    hideSpacialPropertyFormBox: function(v, id) {
      console.log(v);
      this.propertyAddForm.propertyId = v;
      if (id != null && id != "" && id != undefined) {
        this.postBusRequest("/master/property/selectById", { id: id }).then(
          res => {
            if (res.code == 200) {
              this.propertyAddForm = Object.assign({}, this.propertyAddForm, {
                name: res.data.name,
                propertyId: res.data.name,
                key: res.data.key,
                dataType: res.data.dataType,
                min: Number(res.data.min),
                max: Number(res.data.max),
                step: Number(res.data.step),
                unit: res.data.unit,
                wrType: res.data.wrType,
                comments: res.data.comments
              });
            } else {
              this.$Message.error(res.msg);
            }
          }
        );
      }
      this.$forceUpdate();
      document.querySelector(".spacialPropertyFormBox").style.height = "0";
    },
    hideSpacialMethodFormBox: function(v, id) {
      console.log(v);
      this.methodAddForm.methodId = v;
      if (id != null && id != "" && id != undefined) {
        this.postBusRequest("/productFunction/selectMethodById", {
          id: id
        }).then(res => {
          if (res.code == 200) {
            this.methodAddForm = Object.assign({}, this.methodAddForm, {
              name: res.data.name,
              methodId: res.data.name,
              key: res.data.key,
              isSync: res.data.isSync,
              inList: res.data.inList,
              outList: res.data.outList,
              comments: res.data.comments
            });
          } else {
            this.$Message.error(res.msg);
          }
        });
      }
      this.$forceUpdate();
      document.querySelector(".spacialMethodFormBox").style.height = "0";
    },
    propertyfilter() {
      document.querySelectorAll(".spacialPropertyFormBoxItem").forEach(el => {
        el.style.display = "none";
      });
      this.propertyNameList.forEach((el, i) => {
        if (el.name.indexOf(this.propertyAddForm.propertyId) == -1) {
          document.querySelectorAll(".spacialPropertyFormBoxItem")[
            i
          ].style.display = "none";
        } else if (el.name.indexOf(this.propertyAddForm.propertyId) != -1) {
          document.querySelectorAll(".spacialPropertyFormBoxItem")[
            i
          ].style.display = "block";
        }
      });
    },
    propertyConfirm() {
      setTimeout(() => {
        if (document.querySelector(".spacialPropertyFormBox")) {
          document.querySelector(".spacialPropertyFormBox").style.height = "0";
        }
      }, 300);
    },
    methodfilter() {
      document.querySelectorAll(".spacialMethodFormBoxItem").forEach(el => {
        el.style.display = "none";
      });
      this.methodNameList.forEach((el, i) => {
        if (el.name.indexOf(this.methodAddForm.propertyId) == -1) {
          document.querySelectorAll(".spacialMethodFormBoxItem")[
            i
          ].style.display = "none";
        } else if (el.name.indexOf(this.methodAddForm.propertyId) != -1) {
          document.querySelectorAll(".spacialMethodFormBoxItem")[
            i
          ].style.display = "block";
        }
      });
    },
    methodConfirm() {
      setTimeout(() => {
        if (document.querySelector(".spacialMethodFormBox")) {
          document.querySelector(".spacialMethodFormBox").style.height = "0";
        }
      }, 300);
    },
    eventfilter() {
      document.querySelectorAll(".spacialEventFormBoxItem").forEach(el => {
        el.style.display = "none";
      });
      this.eventNameList.forEach((el, i) => {
        if (el.name.indexOf(this.eventAddForm.eventId) == -1) {
          document.querySelectorAll(".spacialEventFormBoxItem")[
            i
          ].style.display = "none";
        } else if (el.name.indexOf(this.eventAddForm.eventId) != -1) {
          document.querySelectorAll(".spacialEventFormBoxItem")[
            i
          ].style.display = "block";
        }
      });
    },
    eventConfirm() {
      setTimeout(() => {
        if (document.querySelector(".spacialEventFormBox")) {
          document.querySelector(".spacialEventFormBox").style.height = "0";
        }
      }, 300);
    }
  },
  watch: {
    //清空表单
    radioValue: function(val) {
      let checkKey = (rule, value, callback) => {
        if (!validateKey(value) && value != "") {
          return callback(new Error("标识符只能为英文大小写字母和数字"));
        } else {
          return callback();
        }
      };
      let checkAlarmExpression = (rule, value, callback) => {
        value = value.replace(/\s*/g, "");
        if (value == "") {
          return callback();
        }
        let reg = /^\$[a-zA-Z\d]$/;
        if (value == "self" || reg.test(value)) {
          return callback(new Error("报警表达式格式不对"));
        }
        value = value.replace(/\$[a-zA-Z\d]+|self/g, "1");
        console.log(value);
        let regx = /[a-zA-Z]+/;
        let errorWord = value.match(regx);
        if (errorWord != "" && errorWord != null) {
          return callback(
            new Error(errorWord + "不正确,是否要输入$" + errorWord)
          );
        }
        try {
          eval(value);
          return callback();
        } catch (err) {
          console.log(err);
          return callback(new Error("报警表达式格式不对"));
        }
      };

      document.querySelectorAll(".spacialFormBox").forEach(el => {
        el.style.height = "0";
      });
      if (val === 1) {
        //事件
        this.$nextTick(() => {
          // this.eventAddFormValidate = {};
          // this.eventAddFormValidate = {
          //   typeId: [
          //     {
          //       required: true,
          //       message: "请选择产品类型"
          //     }
          //   ],
          //   eventId: [{ required: true, message: "请输入功能名称" }],
          //   key: [
          //     { required: true, message: "请输入标识符", trigger: "blur" },
          //     { validator: checkKey, trigger: "blur" }
          //   ],
          //   eventType: [
          //     {
          //       required: true,
          //       message: "请选择事件类型"
          //     }
          //   ]
          // };
          this.$refs.eventAddForm.resetFields();
          this.eventAddForm = {};
          this.eventAddForm.typeId = this.rowData.type;
          this.getNameList();
          this.eventName = "";
          this.$forceUpdate();
        });
      } else if (val === 2) {
        //属性
        this.$nextTick(() => {
          // this.propertyAddFormValidate = {};
          // //重置下验证规则
          // this.propertyAddFormValidate = {
          //   typeId: [
          //     {
          //       required: true,
          //       message: "请选择产品类型"
          //     }
          //   ],
          //   propertyId: [{ required: true, message: "请输入功能名称" }],
          //   key: [
          //     { required: true, message: "请输入标识符", trigger: "blur" },
          //     { validator: checkKey, trigger: "blur" }
          //   ],
          //   dataType: [
          //     {
          //       required: true,
          //       message: "请选择数据类型"
          //     }
          //   ],
          //   unit: [
          //     {
          //       required: true,
          //       message: "请选择单位"
          //     }
          //   ],
          //   wrType: [
          //     {
          //       required: true,
          //       message: "请选择读写类型"
          //     }
          //   ],
          //   alarmExpression: [
          //     { required: true, message: "请输入报警表达式", trigger: "blur" },
          //     { validator: checkAlarmExpression, trigger: "blur" }
          //   ]
          // };

          this.$refs.propertyAddForm.resetFields();
          this.propertyAddForm = {};
          this.propertyAddForm.typeId = this.rowData.type;
          this.getNameList();
          this.properytName = "";
          this.$forceUpdate();
        });
      } else if (val === 3) {
        //方法
        this.$nextTick(() => {
          // this.methodAddFormValidate = {};
          // this.methodAddFormValidate = {
          //   typeId: [
          //     {
          //       required: true,
          //       message: "请选择产品类型"
          //     }
          //   ],
          //   methodId: [{ required: true, message: "请输入功能名称" }],
          //   key: [
          //     { required: true, message: "请输入标识符", trigger: "blur" },
          //     { validator: checkKey, trigger: "blur" }
          //   ],
          //   isSync: [
          //     {
          //       required: true,
          //       message: "请选择调用方式"
          //     }
          //   ]
          // };

          this.$refs.methodAddForm.resetFields();
          this.methodAddForm = {};
          this.methodAddForm.typeId = this.rowData.type;
          this.getNameList();
          this.methodName = "";
          this.$forceUpdate();
        });
      }
    }
  },
  mounted() {
    this.init();
    this.getTypeList();
    this.getUnitList();
    this.getLinkTypeList();
  }
};
</script>
