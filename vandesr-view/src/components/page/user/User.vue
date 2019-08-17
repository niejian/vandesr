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
        width="30%">
        <el-form ref="formData" :model="formData" :rules="rules" label-width="150px">
          <el-input type="hidden" v-model="formData.id"></el-input>

          <el-form-item label="昵称">
            <el-input v-model="formData.loginName" :readyOnly="readyOnly" placeholder="昵称"></el-input>

          </el-form-item>
          <el-form-item label="用户名称">
            <el-input v-model="formData.userName" :readyOnly="readyOnly" placeholder="用户名称"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="formData.email" :readyOnly="readyOnly" placeholder="邮箱"></el-input>
          </el-form-item>
        </el-form>

        <!-- 该用户所绑定的角色信息 -->
        <el-table 
          class="table"
          :data="roles"
          border
        >
          <el-table-column label="id" prop="id" v-if="1!=1"></el-table-column>
          <el-table-column label="角色编码" prop="roleCode" align="center"></el-table-column>
          <el-table-column label="角色名称" prop="roleName" align="center"></el-table-column>
        </el-table>

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
import {getUsers, removeUser, getUserRoleInfo} from '@/api/user'
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
      let data = {userId: userId, deleteFlag: deleteFlag}
      removeUser(data).then(response => {
        let success = response.success;
        let responseCode = response.responseCode;
      
        if (success && responseCode === 0) {
          this.$message({
            type: 'success',
            showClose: true,
            message: '删除成功'
          });
        }else {
          this.$message({
            type: 'error',
            showClose: true,
            message: '删除失败，请重试！'
          });
        }
      })
    },
    resetQuery() {
      this.formData = {
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
        this.$message({
          type: 'warning',
          showClose: true,
          message: '请选择一条数据'
        })
        return false;
      }

      this.editVisible = true;
      this.formData = this.selected;
      let data = {userId: this.selected.id}

      // 查询用户关联的角色信息
      getUserRoleInfo(data).then(response =>{
        let success = response.success;
        let responseCode = response.responseCode;
        if (success && 0 === responseCode) {
          this.roles = response.data;
        }
      })


    },
    // 弹出框点击关闭回调
    closeDialog() {
      
      this.resetQuery() 
    }
  },
  created() {
    this.search()
  }
}
</script>

