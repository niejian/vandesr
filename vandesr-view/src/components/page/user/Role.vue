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
        </el-form-item>
      </el-form> 
      <!-- 数据展示 -->
      <el-table :data="roles" 
        border 
        class="table" 
        highlight-current-row
        @current-change="handleCurrentChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column label="id" prop="id" v-if="1!=1"></el-table-column>
        <el-table-column label="角色编码" prop="roleCode" align="center"></el-table-column>
        <el-table-column label="角色名称" prop="roleName" align="center"></el-table-column>
        <el-table-column label="创建时间" prop="createDate" align="center"></el-table-column>
        <el-table-column label="创建人" prop="createUserCode" align="center" :formatter='formatCreateUser'></el-table-column>
        <el-table-column label="修改时间" prop="updateDate" align="center"></el-table-column>
        <el-table-column label="修改时间" prop="updateDate" align="center"></el-table-column>
        <el-table-column label="修改人" prop="updateUserCode" align="center" :formatter='formatUpdateUser'></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="warning" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
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
      <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
        <el-form ref="formInline" :model="formInline" label-width="150px">
          <el-form-item label="角色编码">
            <el-input v-model="formInline.roleCode"></el-input>
            <el-input type="hidden" v-model="formInline.id"></el-input>
          </el-form-item>
          <el-form-item label="角色名称">
            <el-input v-model="formInline.roleName"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {getRoleList} from '@/api/user'
export default {
  name: 'roleManage',
  data: function() {
    return {
      // 弹出编辑页面
      editVisible: false,
      roles: [],
      formInline : {
        roleCode: '',
        roleName: '',
        id: ''
      },
      cur_page: 1,
      total: 0,
      pageNum: 1,
      pagesize: 10,

    }

  },
  created() {
    this.search();
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
    
    search() {
      var page = new Object();
      page.pageNum = this.pageNum;
      page.pageSize = this.pagesize;
      var role = new Object();
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

    handleEdit(index, row) {
      console.log(index)
      console.log(row)
      this.editVisible = true;
      this.formInline.id = row.id;
      this.formInline.roleCode = row.roleCode;
      this.formInline.roleName = row.roleName;


    }
  }
}
</script>


<style scoped>
 .handle-box {
   margin-bottom: 20px;
 }
</style>

