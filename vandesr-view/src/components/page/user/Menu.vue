<template>
  <div class="table">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 系统设置/系统菜单管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <br/>
   
    <div class="" style="height:100%">
    
      <!-- 菜单树信息 -->
      <div style="float:left;width:35%;" >   
        <!-- 显示对应的可编辑的树形菜单信息 -->
        <!-- <MenuTree ref="menuTree" v-bind:getSelectNode="getSelectNode"></MenuTree> -->
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
        
      </div>
      &nbsp;&nbsp;&nbsp;

      <!-- 选中菜单详情 -->
      <div class="menuInfo" style="" >
        <div class="toolbar">
          <el-button type="primary" icon="el-icon-search" @click="handleView()" round>查看</el-button>
          <el-button type="warning" icon="el-icon-circle-plus-outline" @click="add()" round>添加</el-button>
          <el-button type="success" icon="el-icon-edit" @click="edit()" round>编辑</el-button>
          <el-button type="danger" icon="el-icon-edit" @click="del()" round>删除</el-button>
        </div>
        <br/>
        <!-- 菜单详情 -->
        <el-table
          :data="menuDetails"
          border 
          class="table"
          @row-click="onRowClick"
          highlight-current-row>
       
          <el-table-column label="menuId" prop="menuId" v-if="1!=1"></el-table-column>
          <el-table-column label="菜单名称" prop="name" align="center" ></el-table-column>
          <el-table-column label="菜单编码" prop="menuCode" align="center"></el-table-column>
          <el-table-column label="菜单路径" prop="path" align="center"></el-table-column>
          <el-table-column label="访问路由" prop="routerPath" align="center"></el-table-column>
          <el-table-column label="是否叶子节点" prop="hasChildren" :formatter="format" align="center"></el-table-column>
          <!-- <el-table-column label="头像" prop="avatar" align="center"></el-table-column> -->
        </el-table>
      </div>
    </div>
    <el-dialog :title="title" :closeOnClickModal="closeOnClickModal" 
      :visible.sync="visible"
      @close="closeDialog"
      @open="openDialog"
      
      width="50%">
      <el-form ref="viewData" :inline="true" :model="menuDetail" :rules="rules" label-width="150px">
        <el-form-item label="上级菜单名称" >
          <el-input v-model="menuDetail.parentMenuName" disabled></el-input>
  
        </el-form-item>
        <el-form-item label="上级菜单编码" >
          <el-input v-model="menuDetail.parentMenuCode" disabled></el-input>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menuDetail.name" :disabled="disabled"></el-input>
  
        </el-form-item>
        <el-form-item label="菜单编码" prop="menuCode">
          <el-input v-model="menuDetail.menuCode" :disabled="disabled"></el-input>
        </el-form-item>

        <el-form-item label="访问路由" prop="routerPath">
          <el-input v-model="menuDetail.routerPath" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="页面路径" prop="path">
          <el-input v-model="menuDetail.path" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="是否叶子节点" prop="hasChildren">
          <!-- <el-input v-model="menuDetail.hasChildren" :disabled="disabled"></el-input> -->
          <el-select v-model="menuDetail.hasChildren" clearable 
            placeholder="请选择"
            @change="selectChange" 
            :disabled="disabled">
            <el-option
              v-for="item in hasChildren"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单图标" v-show="iconShow">
          <el-input v-model="menuDetail.icon" :disabled="disabled"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" v-if="title !== '查看'" @click="saveEdit">确 定</el-button>
      </span>
    </el-dialog>

  </div>
  
</template>
<style scoped>
.container {
  height: 100%;
}
.menuInfo {
  float:left;
  width:45%;
  padding-left:20px
}
/* .table {
  padding-top: 10px
} */
</style>
<script>
import {getSystemMenus, getMenuInfo, addMenuInfo} from '@/api/user'
// import MenuTree  from '@/components/page/user/MenuTree.vue'

export default {

  data() {
    const isValidMenuName = (rule, value, callback) => {
      let field = rule.field;
      let menuNameRequestData = {menuName: value};
      let menuCodeRequestData = {menuCode: value};
      let requestData = menuNameRequestData;
      let alertMsg = '菜单名称';
      if('menuCode' === field) {
        requestData = menuCodeRequestData;
        alertMsg = '菜单编码';
      }
      // 校验菜单名称是否唯一
      if (value) {

        getMenuInfo(requestData).then(response => {
          let success = response.success;
          let responseCode = response.responseCode;
          let menu = response.data;
          // 能查到对应的menu信息
          if (success && responseCode === 0 && menu) {
             callback(new Error(alertMsg + '重复，请重新输入'))
    
          }else{
             callback()
          }
          
        })
        
      } else {
        callback()
      }
    };
    return {
      hasChildren: [
         {
          value: true,
          label: '否'
        },
         {
          value: false,
          label: '是'
        }
      ],
      iconShow: true,
      visible: false,
      closeOnClickModal: false,
      disabled: true,
      readyOnly:true,
      title: '查看',
      menus: [],
      menuDetails: [],
      menuDetail: {},
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      rules: {
        name: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' },
          {min: 1, max: 64, message: '长度在64个字符内', trigger: 'blur' },
          // 菜单名称是否唯一
          { validator: isValidMenuName, trigger: 'blur' }

        ],
        menuCode: [
          { required: true, message: '请输入菜单编码', trigger: 'blur' },
          {min: 1, max: 64, message: '长度在64个字符内', trigger: 'blur' },
          // 菜单名称是否唯一
          { validator: isValidMenuName, trigger: 'blur' }
        ],
        routerPath: [
          { required: true, message: '请输入访问路由', trigger: 'blur' },
          {min: 1, max: 64, message: '长度在64个字符内', trigger: 'blur' }
        ],
        path: [
         { required: true, message: '请输入页面路径', trigger: 'blur' },
         {min: 1, max: 64, message: '长度在64个字符内', trigger: 'blur' }
        ],
        hasChildren: [
          { required: true, message: '请选择是否是叶子节点', trigger: 'blur' }
        ]
      }
    }
  },
  // components: {
  //   MenuTree
  // },
  methods: {
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
      this.menuDetails = [];
      this.menuDetail = {};
      // console.log(data);
      // let checkedNodes = this.$refs.tree.getCheckedNodes();
      let checkedMenuId = data.menuId;
      // 让树节点只能选中一个
      // 清空所有选中节点
      this.$refs.tree.setCheckedKeys([]);
      // 设置当前选中节点
      this.$refs.tree.setCheckedKeys([checkedMenuId]);
      
      this.menuDetails.push(data);
      this.menuDetail = data;
      this.getMenuByMenuId(data.parentId)
    },
    // 获取父菜单信息
    getMenuByMenuId(menuId) {
      getMenuInfo({id: menuId}).then(response => {
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && responseCode === 0) {
          let menu = response.data;
          
          this.menuDetail.parentMenuName = menu.menuName;
          this.menuDetail.parentMenuCode = menu.menuCode;
         
        }
        
      })
      
    },
    // 查看菜单详细信息
    handleView() {
      let length = this.menuDetails.length;
      if (length <= 0) {
        this.showAlert('warning', '请选择一个菜单信息进行操作')
        return;
      }
     // 
     this.visible = true;
    
    
    },
    add() {
      let length = this.menuDetails.length;
      if (length <= 0) {
        this.showAlert('warning', '请选择一个菜单信息进行操作');
        return;
      }
      //添加操作应该是非叶子节点
      if(!this.menuDetail.hasChildren) {
        this.showAlert('warning', '添加菜单操作应该选择非叶子节点');
        return;
      }
      this.title = '添加菜单';
      this.visible = true;
      this.disabled = false;

      let parentIds = this.menuDetail.parentIds;
      let parentId = this.menuDetail.menuId;
      let parentMenuName = this.menuDetail.parentMenuName;
      let parentMenuCode = this.menuDetail.parentMenuCode;
      this.menuDetail = {};
      this.menuDetail.parentId = parentId;
      this.menuDetail.parentIds = parentIds;
      this.menuDetail.parentMenuName = parentMenuName;
      this.menuDetail.parentMenuCode = parentMenuCode;


    },
    // 添加菜单逻辑处理
    saveEdit() {
      this.$refs['viewData'].validate((valid) => {
        if (valid) {
          addMenuInfo(this.menuDetail).then(response => {
            let success = response.success;
            let responseCode = response.responseCode;
            if (success && responseCode === 0) {

              this.showAlert('success', '菜单添加成功');
              this.closeDialog()
            }else {
              this.showAlert('error', response.responseMsg);
            }
          }).catch(err => {
            console.error('添加菜单失败')
          })
        }
      })
    },
    onRowClick() {},
    //弹出提示信息
    showAlert(type, msg) {
      this.$message({
          type: type,
          showClose: true,
          message: msg
        })
        return false;
    },
    format(row) {
      if(row.hasChildren) {
        return '否'
      }
      return '是'
    },
    closeDialog() {
      this.visible = false
      this.refresh()
    },
    openDialog() {
      
    },
    selectChange(data) {
     //是叶子节点 data = false; 图标不显示
     if (!data) {
      this.iconShow = false;
      this.menuDetail.routerPath = '#'
      this.menuDetail.path = '#'
     }else {
      this.iconShow = true;
      this.menuDetail.routerPath = ''
      this.menuDetail.path = ''
     }
    },
    refresh() {
      this.getSysMenus({});
      this.menuDetail = {};
      // 清空所有选中节点
      this.$refs.tree.setCheckedKeys([]);
      //清空表格信息
      this.menuDetails = [];
      this.title = '查看';
      this.iconShow = true;
    }
   
  },
  created() {
    // 获取系统菜单信息
    this.getSysMenus({})
  }
}
</script>