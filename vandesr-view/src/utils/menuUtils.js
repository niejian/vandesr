
import lazyLoading from './lazyLoading'
export default (routers, data) => {
  //这里之所以要重新遍历一下，是因为，通常我们动态路由的时候，是获取服务端数据，这个component属性是一个字符串，或者可能连字段名都是其他的key
  //所以这里要做一些转换
  generaMenu(routers, data)
}
function generaMenu(routers,data){
  
  data.forEach((item)=>{
    
    let menu = Object.assign({},item)
    // debugger
    // component ===》 menuCode
    menu.name = menu.component;
    let meta = {};
    meta.title = menu.name;
    let path = menu.path;
    let routerPath = path;
    menu.meta = meta;
    
    if(path && path.lastIndexOf('/') > 0){
      let index = path.lastIndexOf('/');
      routerPath = path.substr(index);
      // menu.path = routerPath;
    }
    
    menu.component = lazyLoading('/common/Home')
    //menu.component = lazyLoading(menu.path)
    menu.path = routerPath;
    
    // 非叶子节点
    if(item.hasChildren){
      menu.children = []
      generaMenu(menu.children, item.children)
    }
    // console.log(menu)
    routers.push(menu)
  })

  // console.log(routers)
}