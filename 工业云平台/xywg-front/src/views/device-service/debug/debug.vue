<style lang="less" >
  @import "debug.less";
</style>
<style>
  /**
  json 编辑框样式
   */
  .jsoneditor-vue{
    height: 380px;
    width: 99.8%;
  }
  .ace_editor{
    position:static;
  }
</style>
<template>
  <div class="search">
    <Card>
      <Form inline :label-width="70" class="search-form">
        <Form-item label="">
          <Select v-model="productId" filterable clearable @on-change="onProductChange" placeholder="所属产品" class="ivu-select-input">
            <Option v-for="item in productList" :value="item.id" :key="item.id">{{ item.name }}</Option>
          </Select>
        </Form-item>
        <Form-item label="">
          <Select v-model="deviceId" clearable filterable @on-change="onDeviceChange" ref="deviceSelect" placeholder="设备名称">
            <Option v-for="(item,index) in deviceList" :value="item.id" :key="index">{{item.name}}</Option>
          </Select>
        </Form-item>

        <Row>
          <Col span="10">
          <Card>
            <Tabs type="card" value="name1">
              <TabPane label="调试真实设备" name="name1">
                <FormItem label="">
                  <Select v-model="functionId" filterable clearable ref="debugDeviceDown"  placeholder="下发类型">
                    <Option v-for="item in functionList" :value="item.key" :key="item.id">{{ item.name }}</Option>
                  </Select>
                </FormItem>
                <Button @click="sendDebugMessage" type="primary">发送</Button>
                <vueJsonEditor v-model="jsonData" mode='code' :showBtns="false" ref="jsonDataRef"
                               padding="0"></vueJsonEditor>
              </TabPane>
              <TabPane label="虚拟真实设备" name="name2">
                <Button @click="openDevice" type="primary">开启虚拟设备</Button>
                <FormItem label="">
                  <Select v-model="functionIdVirtual" filterable clearable ref="debugDeviceVirtual" placeholder="上报类型">
                    <Option v-for="(item,index) in functionListVirtual" :value="item.key" :key="index">{{ item.name }}
                    </Option>
                  </Select>
                </FormItem>
                <Button @click="sendVirtualDevice" type="primary">发送</Button>
                <vueJsonEditor v-model="jsonDataSimulate" mode='code' :showBtns="false" ref="jsonDataRef2" theme="jsoneditor"
                               padding="0"></vueJsonEditor>
              </TabPane>
            </Tabs>
          </Card>
          </Col>
          <Col span="13" offset="1">
          <Card dis-hover>
            <p slot="title">实时数据<span class="autoRefresh"> (自动刷新)</span>
              <span @click="clearMessage" class="clearMessage">清屏</span>
              <span class="deviceInfoClass">
                {{this.deviceNameRight}}{{this.deviceStatus}}
              </span>
            </p>
            <Table :columns="columns1" :data="data1"></Table>
          </Card>
          </Col>
        </Row>
      </Form>
    </Card>
  </div>
</template>

<script>
  import vueJsonEditor from 'vue-json-editor';
  import Stomp from "stompjs";
  import SockJS from 'sockjs-client';
  import {websocketUrl} from '@/libs/base';
  import {getStore, setStore} from '@/utils/storage';

  export default {
    name: "dict-manage",
    components: {
      vueJsonEditor,
    },
    data() {
      return {
        userInfo: JSON.parse(this.cookies.get('userInfo')),
        headers: {accessToken: getStore('accessToken')},
        productList: [],
        deviceList: [],
        productId: '',
        deviceId: '',
        jsonData: {},
        jsonDataSimulate: {},
        functionId: '',
        functionIdVirtual: '',
        functionList: [],
        functionListVirtual: [],
        openLogin: false,
        clientId: null,
        subscribeId: null,
        deviceInfo: null,
        deviceStatus: "",
        deviceNameRight: "",
        columns1: [
          {title: '时间', align: "center", key: 'date', width: 150},
          {title: '方式', align: "center", key: 'dataMode'},
          {title: '数据', align: "center", key: 'data',tooltip:true,},
        ],
        data1: [],
        //-----------------------------------------
        stompClient: null,
        pk: "",
        dn: ""

      };
    },
    //组件销毁时，断开socket连接，关闭netty的模拟客户端
    beforeDestroy() {
      this.disconnect();
      this.nettyClose();
    },
    methods: {
      init() {
        this.getProductList();
        this.connect();
      },
      getProductList() {
        this.postBusRequest('/upgrade/getProductList').then(res => {
          this.productList = res.data;
        });
      },
      //当产品下拉改变时执行
      onProductChange() {
        this.getDeviceInfo();
        //获取设备下拉数据
        this.getDeviceList();
        //断开虚拟设备连接
        this.nettyClose();
        //取消订阅
        this.unsubscribe();
        //获取产品的方法列表
        this.getFunctionList();
        //获取产品的事件和属性列表
        this.getFunctionListVirtual();
        this.clearMessage();
      },
      //当设备下拉改变时执行
      onDeviceChange() {
        this.getDeviceInfo();
        //断开虚拟设备连接
        this.nettyClose();
        //重新建立websocket 的通道
        this.startSubscribe();
        this.clearMessage();

      },
      //获取设备下拉数据
      getDeviceList() {
        //清空上次选择的下拉值
        this.$refs.deviceSelect.clearSingleSelect();
        if (this.productId === "" || typeof (this.productId) === 'undefined') {
          return;
        }
        let params = {
          productId: this.productId,
        };
        //获取设备列表
        this.postBusRequest('/remote/selectDeviceList', params).then(res => {
          this.deviceList = res.data || [];
          this.$refs.deviceSelect.setQuery("");
        });
      },
      //调试真实设备的发送
      sendDebugMessage() {
        if (!this.commonCheck()) {
          this.$Message.warning("产品或设备未选择");
          return;
        }
        //重新获取设备状态
        this.postBusRequest('/device/selectById', {id: this.deviceId}).then(res => {
          if (res.code === 200 || res.data != null) {
            if (res.data.status !== 3) {
              this.$Message.warning("该设备处于未激活或者离线状态，无法下发数据");
            } else {
              this.deviceStatusConvert(res);
              //下发调试真实设备的数据
              this.sendOder();
            }
          } else {
            this.$Message.error(res.msg);
          }
        });
      },
      //下发调试真实设备的数据
      sendOder() {
        if (this.functionId === "" || typeof (this.functionId) === 'undefined') {
          this.$Message.warning("功能未选择");
          return false;
        }
        if (this.$refs.jsonDataRef.error) {
          this.$Message.warning("编辑的内容不符合JSON格式");
          return false;
        }
        if (JSON.stringify(this.jsonData) === "{}") {
          this.$Message.warning("编辑的内容无效");
          return false;
        }
        let params = {
          productId: this.productId,
          deviceId: this.deviceId,
          functionId: this.functionId,
          jsonString: JSON.stringify(this.jsonData)
        };
        this.postBusRequest('/debug/sendOrder', params).then(res => {
          if (res.code === 200) {
            this.$Message.success(res.msg);
          } else {
            this.$Message.error(res.msg);
          }
        });
      },
      getDeviceInfo() {
        if (this.commonCheck()) {
          //重新获取设备信息
          this.postBusRequest('/remote/getDeviceInfoById', {id: this.deviceId}).then(res => {
                  this.deviceStatusConvert(res);
          });
        }
      },
      deviceStatusConvert (res){
        this.deviceInfo = res.data;
        if (res.data) {
          this.deviceNameRight = res.data.name;
          if (res.data.status === 1) {
            this.deviceStatus = " (未激活)";
          } else if (res.data.status === 2) {
            this.deviceStatus = " (离线)";
          } else if (res.data.status === 3) {
            this.deviceStatus = " (在线)";
          }
        }
      },
      //获取所有的方法列表(下发)
      getFunctionList() {
        this.$refs.debugDeviceDown.clearSingleSelect();
        let params = {
          productId: this.productId
        };
        this.postBusRequest('/debug/getProductMethodDebug', params).then(res => {
          this.functionList = res.data;
          this.functionList.push({"key": "config", "name": "配置"});
        });
      },
      //获取模拟设备的所有属性和事件下拉(上报)
      getFunctionListVirtual() {
        this.$refs.debugDeviceVirtual.clearSingleSelect();
        let params = {
          productId: this.productId
        };
        this.postBusRequest('/debug/getProductPropertyAndEventList', params).then(res => {
          this.functionListVirtual = res.data;
        });
      },
      //开启设备
      openDevice() {
        if (!this.commonCheck()) {
          this.$Message.warning("要开启虚拟设备,必须先选择产品和设备");
          return;
        }
        //重新获取设备信息
        this.postBusRequest('/remote/getDeviceInfoById', {id: this.deviceId}).then(res => {
          if (res.code === 200 && res.data != null) {
            this.deviceStatusConvert(res);
            if (res.data.status === 1) {
              this.$Message.warning("该设备处于未激活状态，无法开启虚拟");
            } else {
              let device = res.data;
              //重启开启通道时，先关闭原来的通道
              this.nettyClose();
              //在开启设备时就初始化 通道id的值，在发送消息的时候使用该值去互相持有通道
              this.clientId = new Date().getTime();
              let param = {
                productKey: device.product.productKey,
                deviceNo: device.deviceNo,
                deviceSecret: device.deviceSecret
              };
              this.sendVirtualDeviceMessage("login", param);
            }
          } else {
            this.$Message.error(res.msg);
          }
        });
      },
      //虚拟设备发送信息
      sendVirtualDevice() {
        if (!this.commonCheck()) {
          this.$Message.warning("产品或设备未选择");
          return;
        }
        if (!this.openLogin) {
          this.$Message.warning("需要先开启虚拟设备");
          return;
        }
        if (this.functionIdVirtual === "" || typeof (this.functionIdVirtual) === 'undefined') {
          this.$Message.warning("功能未选择");
          return;
        }
        if (this.$refs.jsonDataRef2.error) {
          this.$Message.warning("编辑的内容不符合JSON格式");
          return;
        }
        if (JSON.stringify(this.jsonDataSimulate) === "{}") {
          this.$Message.warning("编辑的内容无效");
          return;
        }
        this.sendVirtualDeviceMessage(this.functionIdVirtual, this.jsonDataSimulate);
      },
      //真正虚拟设备发送信息
      sendVirtualDeviceMessage(functionVirtual, param) {
        let params = {
          method: functionVirtual,
          uuid: this.clientId,
          version: 1,
          clientId: this.clientId,
          params: param,
        };
        this.postBusRequest('/debug/action', params).then(res => {
          if (res.code === 200) {
            if (functionVirtual === 'login') {
              //登录成功后修改虚拟设备为已登录状态
              this.openLogin = true;
              //需要刷新设备状态
              //this.getDeviceInfo();
              this.deviceStatus = " (在线)";
            }
            this.$Message.success(res.msg);
          } else {
            this.$Message.error(res.msg);
          }
        });
      },
      //关闭netty 通道
      nettyClose() {
        if (this.clientId != null) {
          this.postBusRequest('/debug/close', {"clientId": this.clientId}).then(res => {
            //关闭通道后 无论成功失败 都把虚拟设备状态设置为未开启
            this.openLogin = false;
            this.clientId = null;
          });
        }
      },
      commonCheck() {
        return !(this.productId === "" || typeof (this.productId) === 'undefined' || this.deviceId === "" || typeof (this.deviceId) === 'undefined');
      },

      //---------------↓---------------↓--------------websocket-------------↓-------------------↓---------------
      //清屏
      clearMessage() {
        this.data1 = [];
      },
      //开始订阅
      startSubscribe() {
        if (!this.commonCheck()) {
          return;
        }
        //先取消订阅
        this.unsubscribe();
        //重新获取设备信息
        this.postBusRequest('/remote/getDeviceInfoById', {id: this.deviceId}).then(res => {
          this.pk = res.data.product.productKey;
          this.dn = res.data.deviceNo;
          this.subscribe();
        });

      },
      connect() {
        this.clearMessage();
        let socket = new SockJS(websocketUrl); //连接SockJS的endpoint名称为"endpointOyzc"
        this.stompClient = Stomp.over(socket);//使用STMOP子协议的WebSocket客户端
        this.stompClient.connect({}, function (frame) {//连接WebSocket服务端
          console.log('Connected:' + frame);
        });
      },
      disconnect() {
        this.clearMessage();
        if (this.stompClient != null) {
          this.stompClient.disconnect();
          console.log("----websocket disconnected----");
        }
      },
      //订阅
      subscribe() {
        let _this = this;
        this.subscribeId = this.stompClient.subscribe('/debug/' + this.pk + "/" + this.dn, function (response) {
          let message = JSON.parse(response.body);
          _this.data1.push(message);
        });
      },
      //取消订阅
      unsubscribe() {
        if (this.subscribeId) {
          this.subscribeId.unsubscribe();
        }
      }
      //-------------↑------------------↑-----------------websocket----------------↑------------------↑-------------
    },
    mounted() {
      this.init();
    }
  };
</script>
