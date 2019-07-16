// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'babel-polyfill'
import Vue from 'vue'
import iView from '@/libs/iview/src';
import iviewArea from 'iview-area';
import App from './App'
import {
  router
} from './router/index'
import store from './store';
import '@/locale';
import '@/libs/iview/dist/styles/iview.css';
import VueI18n from 'vue-i18n';
import {
  Base64
} from 'js-base64';
import Icon from 'vue-awesome/components/Icon';
import 'vue-awesome/icons/brands/qq';
import 'vue-awesome/icons/brands/weixin';
import 'vue-awesome/icons/brands/weibo';
import {
  getBusRequest,
  postBusRequest,
  getRequest,
  postRequest,
  putRequest,
  deleteRequest,
  uploadFileRequest,
  getBaiduToken,
  postBaiduIDcard
} from '@/utils/api';
import util from '@/libs/util';
import hasPermission from '@/libs/hasPermission';
import * as TreeTable from 'tree-table-vue';
import Moment from "moment";
import BaiduMap from 'vue-baidu-map'
import VueClipboard from 'vue-clipboard2'
import Cookies from "js-cookie";
import echarts from 'echarts';
import '@/styles/common.less'

Vue.config.productionTip = false
Vue.use(VueClipboard);
Vue.use(VueI18n);
Vue.use(iView);
Vue.use(iviewArea);
Vue.component('icon', Icon);
Vue.use(hasPermission);
Vue.use(TreeTable);
Vue.use(Base64);
Vue.prototype.getRequest = getRequest;
Vue.prototype.postRequest = postRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.uploadFileRequest = uploadFileRequest;
Vue.prototype.getBusRequest = getBusRequest;
Vue.prototype.postBusRequest = postBusRequest;
Vue.prototype.getBaiduToken = getBaiduToken;
Vue.prototype.postBaiduIDcard = postBaiduIDcard;
Vue.prototype.moment = Moment;
Vue.prototype.cookies = Cookies;
Vue.prototype.echarts = echarts;
Vue.use(BaiduMap, {
  // ak 是在百度地图开发者平台申请的密钥 详见 http://lbsyun.baidu.com/apiconsole/key */
  ak: 'GqKqAVxobuSmaI14O3auufoZSziltPmG'
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: router,
  store: store,
  render: h => h(App),
  data: {
    currentPageName: ''
  },
  mounted() {
    // 初始化菜单
    util.initRouter(this);
    this.currentPageName = this.$route.name;
    // 显示打开的页面的列表
    this.$store.commit('setOpenedList');
    this.$store.commit('initCachepage');
  }
})
