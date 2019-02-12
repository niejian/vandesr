/**
 * 通过axios发送post，获取get请求
 */
/**
 * post 请求
 * @param {*} url 请求地址
 * @param {*} data 请求参数
 */
import Vue from 'vue';
import axios from 'axios';

Vue.prototype.$axios = axios;
const httpPost = function(url, data){

    axios.post(
        url,
        data
    ).then(res => {
        debugger
        return res.data;
    }).catch(err => {
        console.log('请求地址：' + url + ", 参数：" + data + ",发生错误，错误信息：" + err);
        return null;
    })
}

export default httpPost