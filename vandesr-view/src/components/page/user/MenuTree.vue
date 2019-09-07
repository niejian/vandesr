// 菜单树组件
<template>
  <div>
    <el-tree
      :data="menus"
      show-checkbox
      default-expand-all
      check-on-click-node
      node-key="menuId"
      ref="tree"
      highlight-current
      :props="defaultProps" @node-click="handleNodeClick">
    </el-tree>
  </div>
  
</template>

<style scoped>

</style>

<script>
import {getSystemMenus} from '@/api/user'
export default {
  data() {
    return {
      menus: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  props:['getSelectNode'],
  methods : {
    // 获取当前系统菜单信息
    getSysMenus(data) {
      getSystemMenus(data).then(response => {
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && responseCode === 0) {
          this.menus = response.data
          console.log(this.menus)
        }
        
      })
    },
    handleNodeClick(data) {
      // console.log(data);
      // let checkedNodes = this.$refs.tree.getCheckedNodes();
      let checkedMenuId = data.menuId;
      // 让树节点只能选中一个
      // 清空所有选中节点
      this.$refs.tree.setCheckedKeys([]);
      // 设置当前选中节点
      this.$refs.tree.setCheckedKeys([checkedMenuId]);
      return data;
    }
  },
  created() {
    // 获取系统菜单信息
    this.getSysMenus({})
  }
}
</script>