import router from '@/router'
import MenuUtils from './menuUtils'

export function storeLoginRouters () {
  let menuRouters = localStorage.getItem('menuRouters');
  if (!menuRouters || menuRouters.length <= 0) {
    return false;
  }
  let routers = [];
  menuRouters = JSON.parse(menuRouters);
  MenuUtils(routers, menuRouters)

  // routers.push(error_route)
  
  // router.addRoutes(routers);

  
// 系统已配置路由信息
  let sysRoutes = router.options.routes;
  if(sysRoutes && sysRoutes.length > 0) {
    for(let i = 0; i < sysRoutes.length; i++) {
      let rootRoute = router.options.routes[i];
      if(rootRoute.children && rootRoute.children.length > 0 
        && rootRoute.path === '/') {
          if(routers && routers.length > 0) {
            routers.forEach((data) => {
              //router.options.routes.push(data)
              
              router.options.routes[i].children.push(data)
            })
          }
        }
    }
    
  }
  // router.options.routes = router.options.routes.concat(routers)

  router.addRoutes(routers);
  // router.options.routes.push(error_route)

  // router.addRoutes(error_route);

  return routers;
}