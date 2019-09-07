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
                :route="{'path': subItem.routerPath}"
              >
                <template slot="title">{{ subItem.title }}</template>
                <el-menu-item
                  v-for="(threeItem, i) in subItem.children"
                  :key="i"
                  :index="threeItem.routerPath"
                  :route="{'path': threeItem.routerPath}"
                >
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <!-- <el-menu-item v-else :index="subItem.routerPath" :key="subItem.menuId" :router='!subItem.hasChildren'> -->
              <el-menu-item
                v-else
                :index="subItem.routerPath"
                :key="subItem.menuId"
                :route="{'path': subItem.routerPath}"
              >
                {{ subItem.title }}
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <!-- <el-menu-item :index="item.routerPath" :key="item.menuId" :router='!item.hasChildren'> -->
          <el-menu-item :index="item.routerPath" :key="item.menuId" :route="{'path': '/' + item.routerPath}">
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
import {getSessionData} from '@/utils/storeUtil'

export default {
  data() {
    return {
      collapse: false,
      items: []
      
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
    //debugger
    // 从缓存中获取所以菜单信息
    let menus = getSessionData("menuTrees");
    if (menus) {
      this.items = menus;
    }
   
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
