import Vue from 'vue';
import App from './App';
import router from './router';
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
// import '../static/css/theme-green/index.css';       // 浅绿色主题
import '../static/css/icon.css';
import './components/common/directives';
//import "babel-polyfill";
import store from '@/store/index'
import {isEmptyArr} from '@/lib/commonFunction'
import isNullStr from './lib/commonFunction'
//动态加载路由信息
import MenuUtils from '@/utils/menuUtil'


Vue.use(ElementUI, { size: 'small' });

Vue.prototype.$axios = axios;

//定义全局访问url
if(process.env.NODE_ENV === 'development'){
    //开发环境
    Vue.prototype.URL_PREFIX = 'http://localhost:8088'
}

debugger
let loginAccount = sessionStorage.getItem('login_account');

let token = sessionStorage.getItem(loginAccount + '_token_key');

//为每个请求添加请求头
axios.defaults.headers.common['Authorization'] = token;

// 获取用户菜单信息
// let userMenus = sessionStorage.getItem('login_user_menus');
// if (null != userMenus) {
//     userMenus = JSON.parse(userMenus);

// }

// if (userMenus){
//     //这里是防止用户手动刷新页面，整个app要重新加载,动态新增的路由，会消失，所以我们重新add一次
//     let routes = []
//     MenuUtils(routes, userMenus)
//     router.addRoutes(routes)
//     console.log(router)
//     // window.sessionStorage.removeItem('isLoadNodes')
//   }


        
    

//使用钩子函数对路由进行权限跳转
// router.beforeEach((to, from, next) => {
//     debugger

//     let roles = sessionStorage.getItem('login_user_roles');


//     if( isEmptyArr(roles) && to.path !== '/login'){
//         next('/login');
//     }else if(to.meta.permission){
//         // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
//         roles.includes('ROLE_ADMIN') ? next() : next('/403');
//     }else{
//         // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
//         if(navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor'){
//             Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
//                 confirmButtonText: '确定'
//             });
//         }else{
//             next();
//         }
//     }
// })

router.beforeEach((to, from, next) => {
    let token = sessionStorage.getItem(loginAccount + '_token_key');
    console.log('token --->' + token);

    let userMenus = sessionStorage.getItem('login_user_menus');
    if (null != userMenus) {
        userMenus = JSON.parse(userMenus);

    }

    if (userMenus){
        //这里是防止用户手动刷新页面，整个app要重新加载,动态新增的路由，会消失，所以我们重新add一次
        let routes = []
        MenuUtils(routes, userMenus)
        router.addRoutes(routes)
        console.log(router)
        // window.sessionStorage.removeItem('isLoadNodes')
    }


    //为每个请求添加请求头
    axios.defaults.headers.common['Authorization'] = token;
    debugger
    
    if (!userMenus && to.path !== '/login') {
        next({path : '/login'})
    }else {
        if (to.path) {
            next()
          } else {
            next({ path: '/nofound' })
          }
    }
    
})

new Vue({
    axios,
    router,
    store,
    render: h => h(App)
}).$mount('#app');