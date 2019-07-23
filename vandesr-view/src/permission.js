// 权限控制相关
import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken, getMenus} from '@/utils/authUtil'

NProgress.configure({ showSpinner: false }) // NProgress Configuration
const whiteList = ['/login', '/dashboard', '/404', '/403'] // no redirect whitelist
router.beforeEach((to, from, next) => {
    NProgress.start()

    console.log('from:' + from.fullPath + "--->toPath:" + to.fullPath)
    // user authed routers
    let menuRouters = localStorage.getItem('menuRouters');
    let authRouters = [];
    // let routers = [];
    menuRouters = JSON.parse(menuRouters);
    if (menuRouters && menuRouters.length > 0) {
      menuRouters.forEach(data => {
        authRouters.push('/' + data.component)
      })
    }
    debugger
    if (!whiteList.includes(to.path)) {
      if (!authRouters || authRouters <= 0 || !authRouters.includes(to.path)) {
        next({'path': '/403'})
        //return false
      }
    }
    
    const role = localStorage.getItem('ms_username');

    if (!role && to.path !== '/login') {
        next('/login');
        return false
    } else if (to.meta.permission) {
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        role === 'admin' ? next() : next('/403');
    } else {
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        } else {
            next();
        }
    }
})
// done
router.afterEach(() => {
  NProgress.done()
})