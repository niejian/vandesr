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


let loginAccount = sessionStorage.getItem('login_account');

let token = sessionStorage.getItem(loginAccount + '_token_key');

//为每个请求添加请求头
axios.defaults.headers.common['Authorization'] = token;


getUserRouter();

router.beforeEach((to, from, next) => {
    let token = sessionStorage.getItem(loginAccount + '_token_key');
    console.log('token --->' + token);

    let userMenus = sessionStorage.getItem('login_user_menus');
    if (null != userMenus) {
        userMenus = JSON.parse(userMenus);

    }


    //为每个请求添加请求头
    axios.defaults.headers.common['Authorization'] = token;
    debugger
    
    if (!userMenus && to.path !== '/login') {
        sessionStorage.removeItem('isLoadNodes')
        sessionStorage.removeItem('login_user_menus');
        next({path : '/login'})
    }else {
        if (to.path) {
            next()
          } else {
            next({ path: '/nofound' })
          }
    }
    
});

function addRootChildrens(router, data) {
  router.options.routes.forEach(element => {
    if (element.path == '/' && element.children &&element.children.length == 1) {
      debugger
      for(let i = 0; i < data.length; i++){
        element.children.push(data[i])
      }
     
    }
  });
}

function getUserRouter() {
  // 获取用户菜单信息
  let userMenus = sessionStorage.getItem('login_user_menus');
  if (null != userMenus) {
      userMenus = JSON.parse(userMenus);
      let routes = []
      MenuUtils(routes, userMenus)
      console.log("<--------------->")
      console.log(routes)
      debugger
      //router.addRoutes(routes)
      addRootChildrens(router, routes)
      console.log(router.options)
      console.log("<--------------->")
      window.sessionStorage.removeItem('isLoadNodes')

  }
}


new Vue({
    axios,
    router,
    store,
    render: h => h(App)
}).$mount('#app');