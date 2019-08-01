import axios from 'axios';
import {getSessionData, removeSesssionData} from '@/utils/storeUtil'
import router from '@/router'
import {Notification, Col} from 'element-ui'

axios.defaults.timeout = 5000;

let token = getSessionData('token')
// if (token) {
//   token = JSON.parse(token)
// }
axios.defaults.headers.common['Authorization'] = token;

// const service = axios.create({
//     process.env.NODE_ENV === 'development', //来判断是否开发环境
//     baseURL: 'http://localhost:8088',
//     timeout: 5000
// })
let baseURL = 'http://localhost:8088';
// production
if (process.env.NODE_ENV === 'production') {
    
}
axios.baseURL = baseURL;

// 请求拦截
axios.interceptors.request.use( config => {
  //debugger
    let url = config.url;
    if (url.indexOf(baseURL) < 0) {
      config.url = baseURL + url
    }
    // 设置请求头
    if (token) {
      // token = JSON.parse(token)
      config.headers.Authorization = token
    }
    return config;
}, error => {
    console.log(error);
    return Promise.reject();
})

// 响应拦截
axios.interceptors.response.use(response => {
    if(response.status === 200){
      
      return Promise.resolve(response.data);        
    }else{
      return Promise.reject(response.data);        
    }
}, error => {
  
    console.log(error);
    // 根据返回状态码跳转到指定页面
    if (error.response.status) {
      switch (error.response.status) {
        case 401: 
          router.replace({
            path: "/login",
            query: {
              redirect: router.currentRoute.fullPath
            }
          });
          break;
        case 403: 
          Notification({
            title: '403',
            message: '登陆过期',
            type: 'warning'
          });
          // 清除token
          removeSesssionData('token');
          sessionStorage.clear();
          router.replace({
            path: '/login',
            query: {
              redirect: router.currentRoute.fullPath
            }
          })
          break;
        case 404:
          router.replace({
            path: '/404',
            query: {
              redirect: router.currentRoute.fullPath
            }
          })
          break;
        

      }
    }
    return Promise.reject();
})

/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */
export function fetch(url,params={}){
  return new Promise((resolve,reject) => {
    axios.get(url,{
      params:params
    })
    .then(response => {
      resolve(response.data);
    })
    .catch(err => {
      reject(err)
    })
  })
}

/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function post(url,data = {}){
  return new Promise((resolve,reject) => {
    axios.post(url, data)
         .then(response => {
           
           resolve(response.data);
         },err => {
           reject(err)
         })
  })
}

export default axios;