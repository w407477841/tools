import axios from 'axios';
import Vue from 'vue';
import {
  getStore,
  setStore
} from './storage';
import {
  router
} from '../router/index';
import {
  Message
} from 'iview';
import 'iview/dist/styles/iview.css';
Vue.prototype.Message = Message;
import Cookies from 'js-cookie';
import Main from '@/views/Main.vue';
// 统一请求路径前缀
let base = '/api';
let busBase = '/business';
let aip = '/aip';
let oauth = '/oauth';
// 超时设定
axios.defaults.timeout = 10000;

axios.interceptors.request.use(config => {
  return config;
}, err => {
  Message.error('请求超时');
  return Promise.resolve(err);
});

// http response 拦截器
axios.interceptors.response.use(response => {
  const data = response.data;

  // 根据返回的code值来做不同的处理(和后端约定)
  switch (data.code) {
    case 401:
      // 未登录 清除已登录状态
      Cookies.set('userInfo', '');
      setStore('accessToken', '');
      router.push('/login');
      break;
    case 4012:
      // 未登录 清除已登录状态
      Cookies.set('userInfo', '');
      setStore('accessToken', '');
      router.push('/login');
      break;
    case 403:
      // 没有权限
      if (data.msg) {
        Message.error(data.msg);
      } else if (data.message) {
        Message.error(data.message)
      } else {
        Message.error("未知错误");
      }
      break;
    case 500:
      // 错误
      if (data.msg) {
        Message.error(data.msg);
      } else if (data.message) {
        Message.error(data.message)
      } else {
        Message.error("未知错误");
      }
      break;
    case 400:
      // 错误
      if (data.msg) {
        Message.error(data.msg);
      } else if (data.message) {
        Message.error(data.message)
      } else {
        Message.error("未知错误");
      }
      break;
    default:
      return data;
  }

  return data;
}, (err) => {
  // 返回状态码不为200时候的错误处理
  Message.error(err.toString());
  return Promise.resolve(err);
});

export const getRequest = (url, params) => {
  let accessToken = getStore('accessToken');
  return axios({
    method: 'get',
    url: `${base}${url}`,
    params: params,
    headers: {
      'accessToken': accessToken
    }
  });
};

export const postRequest = (url, params) => {
  let accessToken = getStore("accessToken");
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
      }
      return ret;
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'accessToken': accessToken
    }
  });
};

export const putRequest = (url, params) => {
  let accessToken = getStore("accessToken");
  return axios({
    method: 'put',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
      }
      return ret;
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'accessToken': accessToken
    }
  });
};

export const deleteRequest = (url, params) => {
  let accessToken = getStore('accessToken');
  return axios({
    method: 'delete',
    url: `${base}${url}`,
    params: params,
    headers: {
      'accessToken': accessToken
    }
  });
};

export const uploadFileRequest = (url, params) => {
  let accessToken = getStore('accessToken');
  return axios({
    method: 'post',
    url: `${base}${url}`,
    params: params,
    headers: {
      'accessToken': accessToken,
      'Language': (localStorage.lang || 'zh_CN').replace("-", "_")
    }
  });
};

export const getBusRequest = (url, params) => {
  let accessToken = getStore('accessToken');
  return axios({
    method: 'get',
    url: `${busBase}${url}`,
    params: params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'accessToken': accessToken,
      'userId': JSON.parse(Cookies.get("userInfo")).id,
      'userName': JSON.parse(Cookies.get("userInfo")).username,
      'Language': (localStorage.lang || 'zh_CN').replace("-", "_")
    }
  });
};

export const postBusRequest = (url, params) => {
  let accessToken = getStore("accessToken");
  return axios({
    method: 'post',
    url: `${busBase}${url}`,
    data: params,
    headers: {
      'Content-Type': 'application/json',
      'accessToken': accessToken,
      'userId': JSON.parse(Cookies.get("userInfo")).id,
      'userName': JSON.parse(Cookies.get("userInfo")).username,
      'Language': (localStorage.lang || 'zh_CN').replace("-", "_")
    }
  });
};

export const getBaiduToken = () => {
  return axios({
    method: 'post',
    // url: `${aip}/oauth/2.0/token`,
    url: `${oauth}/2.0/token`,
    params: {
      grant_type: 'client_credentials',
      client_id: 'eLao7874AArfRLMrbsIsx57s',
      client_secret: 'zPE5e6YXlTW1O23V2okqXwLGMMjuwDle'
    },
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
}

export const postBaiduIDcard = (token, params) => {
  return axios({
    method: 'post',
    url: `${oauth}/rest/2.0/ocr/v1/idcard`,
    data: params,
    params: {
      access_token: token,
    },
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'access_token': token,
    }
  });
}
