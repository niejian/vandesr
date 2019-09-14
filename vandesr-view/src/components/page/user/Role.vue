<template>
  <div class="table">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 系统设置/角色管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
     
    <div class="container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="角色编码">
          <el-input v-model="formInline.roleCode" placeholder="角色编码"></el-input>
        </el-form-item>
        <el-form-item label="角色名称">
          <el-input v-model="formInline.roleName" placeholder="角色名称"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          <el-button type="default" icon="el-icon-refresh" @click="resetQuery">重置</el-button>

        </el-form-item>
      </el-form> 
      <!--操作按钮 -->
      <div class="toolbar">
        <el-button type="success" icon="el-icon-plus" @click="handleAdd()">添加</el-button>
        <el-button type="info" icon="el-icon-search" @click="handleView()">查看</el-button>
        <el-button type="primary" icon="el-icon-edit" @click="handleEdit()">编辑</el-button>
        <el-button type="warning" icon="el-icon-menu" @click="handleBindMenu()">绑定菜单</el-button>
        <el-button type="danger" icon="el-icon-delete" class="red" @click="handleDelete()">删除</el-button>
      </div>
      <!-- 数据展示 -->
      <el-table 
        :data="roles" 
        border 
        class="table"
        @row-click="onRowClick"
        highlight-current-row>
        <el-table-column align="center" width="55" label="" >
          <template slot-scope="scope">
            <el-radio class="radio"  v-model="radio"  :label="scope.$index">&nbsp;</el-radio>
          </template>
        </el-table-column>

        <el-table-column label="id" prop="id" v-if="1!=1"></el-table-column>
        <el-table-column label="角色编码" prop="roleCode" align="center"></el-table-column>
        <el-table-column label="角色名称" prop="roleName" align="center"></el-table-column>
        <el-table-column label="创建时间" prop="createDate" align="center"></el-table-column>
        <el-table-column label="创建人" prop="createUserCode" align="center" :formatter='formatCreateUser'></el-table-column>
        <el-table-column label="修改时间" prop="updateDate" align="center"></el-table-column>
        <el-table-column label="修改时间" prop="updateDate" align="center"></el-table-column>
        <el-table-column label="修改人" prop="updateUserCode" align="center" :formatter='formatUpdateUser'></el-table-column>
        <!-- <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="warning" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>
      <div class="block">
        <el-pagination 
          @size-change="handleSizeChange"
          background
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagesize"
          :current-page="cur_page"
          @current-change="handleCurrentChange" 
          layout="total, sizes, prev, pager, next" 
          :total="total">
        </el-pagination>
      </div>

<!--      弹出编辑页面-->
      <el-dialog :title="title" :closeOnClickModal="closeOnClickModal" :visible.sync="editVisible" width="30%">
        <el-form ref="formData" :model="formData" :rules="rules" label-width="150px">
          <el-form-item label="角色编码" prop="roleCode">
            <el-input v-model="formData.roleCode" :readonly="isReadOnly"></el-input>
            <el-input type="hidden" v-model="formData.id"></el-input>
          </el-form-item>
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="formData.roleName" ></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" v-if="isShow" class="dialog-footer">
          <el-button @click="cancelBtn('formData')">取 消</el-button>
          <el-button type="primary" @click="saveEdit('formData')">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 弹出菜单绑定页面 -->
      <el-dialog title="菜单绑定" 
        :closeOnClickModal="closeOnClickModal" 
        @open="open"
        @close="close"
        :visible.sync="bindMenu" 
        width="30%">
        <div>
          <el-tree
            :data="menus"
            show-checkbox
            default-expand-all
            check-on-click-node
            node-key="menuId"
            :default-checked-keys="bindMenuIds"
            ref="tree"
            highlight-current
            :props="defaultProps"
            >
          </el-tree>
          
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="cancelBtn('')">取 消</el-button>
          <el-button type="primary" @click="saveRoleMenu()">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {getSystemMenus, getRoleList, updateRole, getMenuByRoleId, saveRoleMenu} from '@/api/user'
export default {
  name: 'roleManage',
  data: function() {
    return {
      isReadOnly: true,
      closeOnClickModal: false,
      bindMenu: false,
      title: '编辑',
      isShow: false,
      radio: '',
		  selected:{},
      menus: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      bindMenuIds: [],
      // 弹出编辑页面
      editVisible: false,
      roles: [],
      formInline : {
        roleCode: '',
        roleName: '',
        id: ''
      },

      formData: {
        roleCode: '',
        roleName: '',
        id: '',
        type: '',
      },
      cur_page: 1,
      total: 0,
      pageNum: 1,
      pagesize: 10,
      rules: {
        roleCode : [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
          {min: 1, max: 64, message: '长度在64个字符内', trigger: 'blur' }
        ],
        roleName : [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          {min: 1, max: 64, message: '长度在64个字符内', trigger: 'blur' }
        ]
          
      }

    }

  },
  created() {
    this.search();
    // this.getSysMenus({})
  },
  methods: {
    // 每页条数变化
    handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pagesize = val;
        this.search();
    },
    // 页码变化
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.search();
    },
    formatCreateUser(row, column) {
      return row.createUserCode + '\n' + row.createUserName
    },
    formatUpdateUser(row, column) {
      return row.updateUserCode + '\n' + row.updateUserName
    },
    // 单选效果
    onRowClick(row, index) {
      this.radio = this.roles.indexOf(row);
      this.selected= row;
    },
    // 获取菜单树信息
    getSysMenus(data) {
      getSystemMenus(data).then(response => {
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && responseCode === 0) {
          this.menus = response.data
          // console.log(this.menus)
          
        }
        
      })
    },
    search() {
      this.selected = {}
      this.selectedId = ''
      let page = new Object();
      page.pageNum = this.pageNum;
      page.pageSize = this.pagesize;
      let role = new Object();
      role.roleCode = this.formInline.roleCode;
      role.roleName = this.formInline.roleName;

      let data = {page: page, role: role}
      //let data = JSON.stringify(param);
      
      getRoleList(data).then(response => {
        
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && responseCode === 0) {
          let records = response.data.records;
          // 总记录数
          this.total = response.data.total;
          // 当前页数
          this.cur_page = response.data.current;
          if (records && records.length > 0) {
            this.roles = records;
          }
        }
        
      })
    },

    //添加
    handleAdd() {
      this.title = '添加';
      this.editVisible = true;
      this.isShow = true;
      this.isReadOnly = false;
      // 将formData置空
      this.formData = {
        roleCode: '',
        roleName: '',
        id: 0,
        type: 'add',
      }
    },
    // 查看
    handleView() {
      let selectedId = this.selected.id;
      this.isReadOnly = true;
      if(!selectedId) {
        this.$message({
          type: 'warning',
          showClose: true,
          message: '请选择一条数据'
        })
        return false;
      }
      this.isShow = false;
      this.title = '查看';
      this.editVisible = true;
      this.formData.id = this.selected.id;
      this.formData.roleCode = this.selected.roleCode;
      this.formData.roleName = this.selected.roleName;
      this.formData.type = 'edit';
    },

    handleEdit() {
      this.isReadOnly = true;
      let selectedId = this.selected.id;
      if(!selectedId) {
        this.$message({
          type: 'warning',
          showClose: true,
          message: '请选择一条数据'
        })
        return false;
      }
      this.isShow = true;
      this.title = '编辑';
      this.editVisible = true;
      this.formData.id = this.selected.id;
      this.formData.roleCode = this.selected.roleCode;
      this.formData.roleName = this.selected.roleName;
      this.formData.type = 'edit';
    },
    // 后台编辑/添加请求
    saveEdit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          
          updateRole({role: this.formData}).then(response => {
            let responseCode = response.responseCode;
            let responseMsg = response.responseMsg;
            let success = response.success;
            if(success && 0 === responseCode ) {
              let type = this.formData.type;
              let msg = type === 'add' ? '添加成功' : '更新成功';
              this.$message({
                type: 'success',
                showClose: true,
                message: msg
              });
              // 关闭层，并重置弹出层的form
              this.cancelBtn('formData');
              this.selected = {};
              this.search();

            }else {
              this.$message({
                type: 'error',
                showClose: true,
                message: response.responseMsg
              })
            }
          }).catch(err => {
            console.log(err)
          })
        } else {
          console.log('error submit!!');
          return false;
        }
        
      });
      
    },
    // 获取该角色绑定的菜单信息
    getRoleMenu(roleId) {
      getMenuByRoleId({roleId: roleId}).then(res => {
        let responseCode = res.responseCode;
        let responseMsg = res.responseMsg;
        let success = res.success;
        let roleMenus = res.data
        if (success && responseCode === 0 
          && roleMenus && roleMenus.length > 0) {
            let length = roleMenus.length;
            for(let i = 0; i < length; i++) {
              let val = roleMenus[i];
              this.bindMenuIds.push(val.menuId);

            }
                   
        }

      }).catch(err => {
        console.log(err)
      })
    },

    open() {
      this.menus = [];
      this.bindMenuIds = [];
      this.getSysMenus({});
      let selectedId = this.selected.id;
      if(!selectedId) {
        return;
      }
      // 获取菜单树信息
      
    },
    close () {
      this.selected = {}
      this.menus = [];
      this.bindMenu = false;
    },
    
    // 绑定菜单信息      
    handleBindMenu() {
      
      let selectedId = this.selected.id;
      this.isReadOnly = true;
      if(!selectedId) {
        this.$message({
          type: 'warning',
          showClose: true,
          message: '请选择一条数据'
        })
        return false;
      }

      this.bindMenu = true;
      
      this.getRoleMenu(selectedId);
      console.log(this.bindMenuIds)
     
    },

    // 绑定菜单信息
    saveRoleMenu() {
      let checkedNodes = this.$refs.tree.getCheckedNodes();
      //
      if (!checkedNodes || checkedNodes.length <= 0) {
        this.$message({
          type: 'warning',
          showClose: true,
          message: '请选择绑定的菜单信息'
        })
        return false;
      }

      console.log(checkedNodes)
      let data = {roleId: this.selected.id, data: checkedNodes}
      saveRoleMenu(data).then(res => {
        let responseCode = res.responseCode;
        let success = res.success;
        let responseMsg = res.responseMsg;

        if (responseCode === 0 && success) {
          this.$message({
            type: 'success',
            showClose: true,
            message: "保存成功"
          });
          this.bindMenu = false;
        } else {
          this.$message({
            type: 'error',
            showClose: true,
            message: responseMsg
          });
        }

      }).catch(err => {
        console.error(err)
      })
    },

    resetQuery() {
      this.formInline = {
        roleCode: '',
        roleName: '',
        id: ''
      },
      this.selected = {}
      this.selectedId = ''
       //单选效果清空
      this.onRowClick()
    },
    // 删除
    handleDelete() {
      let selectedId = this.selected.id;
      if(!selectedId) {
        this.$message({
          type: 'warning',
          showClose: true,
          message: '请选择一条数据'
        })
        return false;
      }
      // 弹出提示框
     this.$confirm('是否确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then((action) => {
        if (action === 'confirm') {
          this.deleteRole(this.selected);
        }
        
      }).catch((err) => {
        console.log(err)
      })
    },
    // 删除
    deleteRole(row) {
      
      this.formData.id = row.id;
      this.formData.type = 'del';
      // 删除操作
      updateRole({role: this.formData}).then(response => {
        
        let responseCode = response.responseCode;
        let responseMsg = response.responseMsg;
        let success = response.success;
        if(success && 0 === responseCode ) {
          this.$message({
            type: 'success',
            showClose: true,
            message: '删除成功'
          })
          this.selected = {};

          this.search();
        }else {
          this.$message({
            type: 'error',
            showClose: true,
            message: response.responseMsg
          })
        }
      }).catch(err => {
        console.log(error)
      })
    },
    // 取消
    cancelBtn(formName) {
      // this.formInline
      if ('' !== formName) {
        this.$refs[formName].resetFields();
      
        this.editVisible = false;
      }else {
        this.bindMenu = false;
      }
     
    }
  }
}
</script>


<style scoped>
 .handle-box {
   margin-bottom: 20px;
 }
 .toolbar {
   padding-bottom: 15px;
   padding-left: 15px
 }
</style>

