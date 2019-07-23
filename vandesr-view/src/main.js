import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import ElementUI from 'element-ui';
import VueI18n from 'vue-i18n';
import { messages } from './components/common/i18n';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import '../static/css/theme-green/index.css';       // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import "babel-polyfill";
import './permission'; // 权限控制
// import MenuUtils from '@/utils/menuUtils'
// import {storeLoginRouters} from '@/utils/utils'

import store from './store'

Vue.config.productionTip = false
Vue.use(VueI18n);
Vue.use(ElementUI, {
    size: 'small'
});
Vue.prototype.$axios = axios;

axios.defaults.baseURL = 'http://localhost:8088/';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;

const i18n = new VueI18n({
    locale: 'zh',
    messages
})


// MenuUtils(routers, menuRouters)
// debugger
// router.addRoutes(routers);
// 从缓存中加载路由信息
//storeLoginRouters()

// router.options.routes.push(routers)
// const whiteList = ['/login', '/dashboard', '/404', '/403']
// console.log(router.options.routes)
//使用钩子函数对路由进行权限跳转
// router.beforeEach((to, from, next) => {
//     console.log('from:' + from.fullPath + "--->toPath:" + to.fullPath)
//     // user authed routers
//     let menuRouters = localStorage.getItem('menuRouters');
//     let authRouters = [];
//     // let routers = [];
//     menuRouters = JSON.parse(menuRouters);
//     if (menuRouters && menuRouters.length > 0) {
//       menuRouters.forEach(data => {
//         authRouters.push('/' + data.component)
//       })
//     }
//     debugger
//     if (!whiteList.includes(to.path)) {
//       if (!authRouters || authRouters <= 0 || !authRouters.includes(to.path)) {
//         next({'path': '/403'})
//         //return false
//       }
//     }
    
//     const role = localStorage.getItem('ms_username');

//     if (!role && to.path !== '/login') {
//         next('/login');
//         return false
//     } else if (to.meta.permission) {
//         // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
//         role === 'admin' ? next() : next('/403');
//     } else {
//         // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
//         if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
//             Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
//                 confirmButtonText: '确定'
//             });
//         } else {
//             next();
//         }
//     }
// })


new Vue({
    router,
    store,
    i18n,
    axios,
    render: h => h(App)
}).$mount('#app')