import Vue from 'vue';
import App from './App';
import router from './router';
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
// import '../static/css/theme-green/index.css';       // 浅绿色主题
import '../static/css/icon.css';
import './components/common/directives';
import "babel-polyfill";
import store from '@/store/index'
import {isEmptyArr} from '@/lib/commonFunction'
import isNullStr from './lib/commonFunction';


Vue.use(ElementUI, { size: 'small' });

Vue.prototype.$axios = axios;

//定义全局访问url
if(process.env.NODE_ENV === 'development'){
    //开发环境
    Vue.prototype.URL_PREFIX = 'http://localhost:8088'
}

debugger
    
    
    let loginAccount = store.getters.getLoginAccount;
    
    

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    debugger

    let token = store.getters.getToken;
    if(isNullStr(token)){
        token = store.state.user.token;
    }
    let roles = sessionStorage.getItem('login_user_roles');
    //为每个请求添加请求头
    axios.defaults.headers.common['Authorization'] = token

    if( isEmptyArr(roles) && to.path !== '/login'){
        next('/login');
    }else if(to.meta.permission){
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        roles.includes('ROLE_ADMIN') ? next() : next('/403');
    }else{
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if(navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor'){
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        }else{
            next();
        }
    }
})

new Vue({
    axios,
    router,
    store,
    render: h => h(App)
}).$mount('#app');