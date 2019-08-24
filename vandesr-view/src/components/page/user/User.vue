<template>
  <div class="table">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 系统设置/用户信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 用户信息管理，在此页面给用户添加、删除各种角色信息 -->

    <div class="container">
      <el-form :inline="true" :model="formData" class="demo-form-inline">
        <el-form-item label="昵称">
          <el-input v-model="formData.loginName" placeholder="昵称"></el-input>
        </el-form-item>
        <el-form-item label="用户名称">
          <el-input v-model="formData.userName" placeholder="用户名称"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          <el-button type="default" icon="el-icon-refresh" @click="resetQuery">重置</el-button>

        </el-form-item>
      </el-form>

      <!-- 按钮操作 -->
      <div class="toolbar">
        <el-button type="success" icon="el-icon-search" @click="handleView()">查看</el-button>
        <el-button type="primary" icon="el-icon-edit" @click="handleEdit()">编辑</el-button>
      </div>

       <!-- 数据展示 -->
       <el-table
        :data="users"
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
        <el-table-column label="用户编码" prop="userCode" align="center" ></el-table-column>
        <el-table-column label="用户名称" prop="userName" align="center"></el-table-column>
        <el-table-column label="昵称" prop="loginName" align="center"></el-table-column>
        <el-table-column label="邮箱" prop="email" align="center"></el-table-column>
        <el-table-column label="头像" prop="avatar" align="center"></el-table-column>
        <el-table-column label="是否有效" width="180" align="center">
          <template slot-scope="scope">
            <el-switch 
              v-model="scope.row.deleteFlag"
              inactive-color="#ff4949"
              :active-value=0
              active-text="有效"
              inactive-text	= "无效"
              :inactive-value=1
              @change="changeSwitch(scope.row)" >
            </el-switch>
          </template>
        </el-table-column>

       </el-table>
       <!-- 页脚信息 -->
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

      <!-- 弹出编辑/查看页面 -->
      <el-dialog :title="title" :closeOnClickModal="closeOnClickModal" 
        :visible.sync="editVisible"
        @close="closeDialog"
        @open="openDialog"
        width="30%">
        <el-form ref="viewData" :model="viewData" :rules="rules" label-width="150px">
          <el-input type="hidden" v-model="viewData.id"></el-input>

          <el-form-item label="昵称">
            <el-input v-model="viewData.loginName" :readyOnly="readyOnly" placeholder="昵称"></el-input>

          </el-form-item>
          <el-form-item label="用户名称">
            <el-input v-model="viewData.userName" :readyOnly="readyOnly" placeholder="用户名称"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="viewData.email" :readyOnly="readyOnly" placeholder="邮箱"></el-input>
          </el-form-item>
           <!-- 显示角色信息 -->
          <el-form-item label="用户角色信息">
            <el-checkbox-group v-model="checkedRoles">
              <el-checkbox v-for="role in roles" :label="role.id" :key="role.id">{{role.roleName}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <span slot="footer" v-if="title !== '查看'" class="dialog-footer">
          <el-button @click="cancelBtn('')">取 消</el-button>
          <el-button type="primary" @click="saveEdit()">确 定</el-button>
        </span>

        <!-- 该用户所绑定的角色信息 -->
        <!-- 
        <el-table 
          class="table"
          :data="roles"
          border
        >
          <el-table-column label="id" prop="id" v-if="1!=1"></el-table-column>
          <el-table-column label="角色编码" prop="roleCode" align="center"></el-table-column>
          <el-table-column label="角色名称" prop="roleName" align="center"></el-table-column>
        </el-table>
        -->
       

      </el-dialog>


    </div>

  </div>
</template>
<style scoped>
.toolbar {
  padding-top: 10px;
  padding-bottom: 10px
}
</style>
<script>
import {getUsers, deleteUser, getRoleList, getUserRoleInfo, updateUserRole} from '@/api/user'
export default {
  data() {
    return {
      // 实现单选选中效果
      radio: '', 
      closeOnClickModal: false,
      selected: {},
      editVisible: false,
      title: '查看',
      users: [],
      roles: [],
      checkedRoles:[],
      readyOnly: false,
      cur_page: 1,
      total: 0,
      pageNum: 1,
      pagesize: 10,
      formData : {
        id: '',
        loginName: '',
        userName: '',
        email: ''
      },
      viewData : {
        id: '',
        loginName: '',
        userName: '',
        email: ''
      },
      // 校验字段及规则
      rules: {}
    }
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
    // 单选效果
    onRowClick(row, index) {
      this.radio = this.users.indexOf(row);
      this.selected= row;
    },
    // 搜索
    search() {
      this.onRowClick()
      this.selected = {}
      this.selectedId = ''
      var page = new Object();
      page.pageNum = this.pageNum;
      page.pageSize = this.pagesize;
      
      let data = {page: page, 
        loginName: this.formData.loginName, 
        userName: this.formData.userName, 
        email: this.formData.email
      }
      getUsers(data).then(response => {
        
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && responseCode === 0) {
          let records = response.data.records;
          // 总记录数
          this.total = response.data.total;
          // 当前页数
          this.cur_page = response.data.current;
          if (records && records.length > 0) {
            this.users = records;
          }
        }
        
      })
    },
    changeSwitch(row) {
      let userId = row.id;
      let deleteFlag = row.deleteFlag;
      let data = {id: userId, deleteFlag: deleteFlag}
      deleteUser(data).then(response => {
        let success = response.success;
        let responseCode = response.responseCode;
      
        if (success && responseCode === 0) {
          let msg = deleteFlag == 1 ? '删除成功' : '激活成功'
          this.showAlert('success', msg)
          this.search()
        }else {
          this.showAlert('error', '删除失败，请重试！')
    
        }
      })
    },
    resetQuery() {
      this.formData = {
        loginAccount: '',
        userName: '',
        email: ''

      };
      this.viewData = {
        loginAccount: '',
        userName: '',
        email: ''

      };
      this.selected = {}
      //单选效果清空
      this.onRowClick()
      this.search()
    },
    // 查看用户信息
    handleView() {
      let selectedId = this.selected.id;
      if(!selectedId) {
        this.showAlert('warning', '请先选择一条数据')
        return false;
      }

      this.editVisible = true;
      this.viewData = this.selected;
      let data = {userId: this.selected.id}
      //获取用户绑定的角色信息
      this.getUserRole(data);
      
    },
    // 查询用户关联的角色信息
    getUserRole(data) {
      // 查询用户关联的角色信息
      getUserRoleInfo(data).then(response =>{
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && 0 === responseCode) {
          // this.checkedRoles = response.data;
          for (let i = 0 ; i < response.data.length; i++) {
              this.checkedRoles.push(response.data[i].id)

          }
        }
      })

    },

    // 获取所有角色信息
    getRoles() {
      let page = new Object();
      page.pageNum = this.pageNum;
      page.pageSize = this.pagesize;
      let role = new Object();
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

    // 绑定角色信息
    handleEdit() {
      let selectedId = this.selected.id;
      this.title = '编辑'
      if(!selectedId) {
        this.showAlert('warning', '请先选择一条数据')
        return false;
      }

      //检查用户状态
      let deleteFlag = this.selected.deleteFlag;
      if (1 === deleteFlag) {
        this.showAlert('warning', '请先激活用户再编辑')
        return false;
      }

      this.editVisible = true;
      this.viewData = this.selected;
      //获取用户绑定的角色信息
      this.getUserRole({userId: this.selected.id});
      
    },
    // 保存操作
    saveEdit() {
      //给用户绑定角色信息
      // let roleLength = this.checkedRoles.length;
      // if(!roleLength) {
      //   this.showAlert('error', '请至少给用户绑定一个角色');
      //   return false;
      // }
      
      let data = {userId: this.selected.id, roleIds: this.roleLength};

      updateUserRole(data).then(response => {
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && responseCode === 0) {
          this.showAlert('success', '编辑成功');
          this.cancelBtn()
        }else {
          this.showAlert('error', response.responseMsg);
        }

      }).catch(err => {
        console.log(err)
      })
    },
    // 弹出框点击关闭回调
    closeDialog() {
      this.resetQuery() 
    },
    openDialog() {
      this.getRoles();
    },
    cancelBtn() {
      this.editVisible = false;
      this.search();
    },
    //弹出提示信息
    showAlert(type, msg) {
      this.$message({
          type: type,
          showClose: true,
          message: msg
        })
        return false;
    }
  },
  created() {
    this.search()
  }
}
</script>

