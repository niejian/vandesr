import lazyLoading from './lazyLoading'
export default (routers, data) => {
    // 
    generateMenu(routers, data)
}

// 根据后端返回生成对应的路由信息
/**
 * 
 * @param  routers 将后台请求返回信息包装
 * @param  data 后台请求返回数据信息
 */
function generateMenu(routers, data) {
    data.forEach((item) => {
        
        let menu = Object.assign({}, item);
        let path = menu.path;
        // if (path && path.indexOf('/') >= 0){
        //     path = path.substring(1)
        // }
        menu.component = lazyLoading(path);
        if(!item.leaf) {
            menu.children = []
            generateMenu(menu.children, item.children) 
        }

        routers.push(menu);
    });
}


function lazyLoading2(name) {
    //import(`@/components/${name}.vue`)
}