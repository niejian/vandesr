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
        <el-form-item label="登陆账号">
          <el-input v-model="formData.loginAccount" placeholder="登陆账号"></el-input>
        </el-form-item>
        <el-form-item label="用户名称">
          <el-input v-model="formData.userName" placeholder="用户名称"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          <el-button type="default" icon="el-icon-refresh" @click="resetQuery">重置</el-button>

        </el-form-item>
      </el-form>

      <!-- 按钮操作 -->
      <div class="toolbar">
        <el-button type="info" icon="el-icon-search" @click="handleView()">查看</el-button>
        <el-button type="primary" icon="el-icon-edit" @click="handleEdit()">编辑</el-button>
      </div>

       <!-- 数据展示 -->
       <el-table
        data="users"
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
              v-model="scope.row.isDeleted"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-value="0"
              inactive-value="1" >
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

    </div>

  </div>
</template>

<script>
import {getUsers} from '@/api/user'
export default {
  data() {
    return {
      editVisible: false,
      users: [],
      formData : {
        loginAccount: '',
        userName: '',
        email: ''
      }
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
    // 搜索
    search() {
      var page = new Object();
      page.pageNum = this.pageNum;
      page.pageSize = this.pagesize;
      
      let data = {page: page, 
        userCode: this.formData.userCode, 
        userName: this.formData.userCode, 
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
    resetQuery() {
      this.formData = {
        loginAccount: '',
        userName: '',
        email: ''

      };
      this.search()
    }
  },
  created() {
    this.search()
  }
}
</script>

