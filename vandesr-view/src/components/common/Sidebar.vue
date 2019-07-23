<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      :default-active="onRoutes"
      :collapse="collapse"
      background-color="#324157"
      text-color="#bfcbd9"
      active-text-color="#20a0ff"
      @select="handleSelect"
      unique-opened
      router
    >
      <template v-for="item in items">
        <template v-if="item.children">
          <el-submenu :index="item.routerPath" :key="item.menuId">
            <template slot="title">
              <i :class="item.icon"></i
              ><span slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.children">
              <el-submenu
                v-if="subItem.hasChildren"
                :index="subItem.routerPath"
                :key="subItem.menuId"
                :route="{'path': '/' + subItem.menuCode}"
              >
                <template slot="title">{{ subItem.title }}</template>
                <el-menu-item
                  v-for="(threeItem, i) in subItem.children"
                  :key="i"
                  :index="threeItem.routerPath"
                  :route="{'path': '/' + threeItem.menuCode}"
                >
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <!-- <el-menu-item v-else :index="subItem.routerPath" :key="subItem.menuId" :router='!subItem.hasChildren'> -->
              <el-menu-item
                v-else
                :index="subItem.routerPath"
                :key="subItem.menuId"
                :route="{'path': '/' + subItem.menuCode}"
              >
                {{ subItem.title }}
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <!-- <el-menu-item :index="item.routerPath" :key="item.menuId" :router='!item.hasChildren'> -->
          <el-menu-item :index="item.routerPath" :key="item.menuId" :route="{'path': '/' + subItem.menuCode}">
            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
  
</template>


<script>
import bus from "../common/bus";
import {storeLoginRouters} from '@/utils/utils'

export default {
  data() {
    return {
      collapse: false,
      items: []
      // items: [
      //     {
      //         icon: 'el-icon-lx-home',
      //         index: 'dashboard',
      //         title: '系统首页'
      //     },
      //     {
      //         icon: 'el-icon-lx-cascades',
      //         index: 'table',
      //         title: '基础表格'
      //     },
      //     {
      //         icon: 'el-icon-lx-copy',
      //         index: 'tabs',
      //         title: 'tab选项卡'
      //     },
      //     {
      //         icon: 'el-icon-lx-calendar',
      //         index: '3',
      //         title: '表单相关',
      //         subs: [
      //             {
      //                 index: 'form',
      //                 title: '基本表单'
      //             },
      //             {
      //                 index: '3-2',
      //                 title: '三级菜单',
      //                 subs: [
      //                     {
      //                         index: 'editor',
      //                         title: '富文本编辑器'
      //                     },
      //                     {
      //                         index: 'markdown',
      //                         title: 'markdown编辑器'
      //                     },
      //                 ]
      //             },
      //             {
      //                 index: 'upload',
      //                 title: '文件上传'
      //             }
      //         ]
      //     },
      //     {
      //         icon: 'el-icon-lx-emoji',
      //         index: 'icon',
      //         title: '自定义图标'
      //     },
      //     {
      //         icon: 'el-icon-pie-chart',
      //         index: 'charts',
      //         title: 'schart图表'
      //     },
      //     {
      //         icon: 'el-icon-rank',
      //         index: '6',
      //         title: '拖拽组件',
      //         subs: [
      //             {
      //                 index: 'drag',
      //                 title: '拖拽列表',
      //             },
      //             {
      //                 index: 'dialog',
      //                 title: '拖拽弹框',
      //             }
      //         ]
      //     },
      //     {
      //         icon: 'el-icon-lx-global',
      //         index: 'i18n',
      //         title: '国际化功能'
      //     },
      //     {
      //         icon: 'el-icon-lx-warn',
      //         index: '7',
      //         title: '错误处理',
      //         subs: [
      //             {
      //                 index: 'permission',
      //                 title: '权限测试'
      //             },
      //             {
      //                 index: '404',
      //                 title: '404页面'
      //             }
      //         ]
      //     }
      //     ,
      //     {
      //         icon: 'el-icon-lx-redpacket_fill',
      //         index: '/donate',
      //         title: '支持作者'
      //     }
      // ]
    };
  },
  computed: {
    onRoutes() {
      // debugger
      
      return this.$route.path.replace("/", "");
    }
  },
  methods: {
    handleSelect(a, b) {
      
      console.log(a)
      console.log(b)
    }
  },
 
  created() {
    console.log('sidebar')
    // 从缓存中获取所以菜单信息
    let menus = window.localStorage.getItem("menuTrees");
    if (menus) {
      menus = JSON.parse(menus);
    }
   

    // for(var i = 0; i < menus.length; i++){
    //   console.log(menus[i])
    // }
    this.items = menus;
    // 通过 Event Bus 进行组件间通信，来折叠侧边栏
    bus.$on("collapse", msg => {
      this.collapse = msg;
    });
  }
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
  width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
  width: 250px;
}
.sidebar > ul {
  height: 100%;
}
</style>
