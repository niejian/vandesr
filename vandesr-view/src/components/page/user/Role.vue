<template>
  <div class="table">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 角色管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="query_box">
        <!-- 搜素条件 -->
        <el-input v-model="roleCode" placeholder="角色编码" ></el-input>
        <el-input v-model="roleName" placeholder="角色名称" ></el-input>
        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>

      </div>
      <!-- 数据展示 -->
      <el-table :data="roles" border class="table" 
        highlight-current-row
        @current-change="handleCurrentChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column label="id" prop="id" v-if="1==1"></el-table-column>
        <el-table-column label="角色编码" prop="roleCode"></el-table-column>
        <el-table-column label="角色名称" prop="roleName"></el-table-column>
        <el-table-column label="创建时间" prop="createDate"></el-table-column>
        <el-table-column label="创建人" prop="createBy"></el-table-column>
        <el-table-column label="修改时间" prop="updateDate"></el-table-column>
        <el-table-column label="修改人" prop="updateBy"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="warning" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background @current-change="handleCurrentChange" layout="prev, pager, next" :total="1000">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'roleManage',
  data() {
    return {
      roles: [],
       cur_page: 1 
    }

  },
  created: {

  },
  methods: {
    getData() {
      
    }
  }
}
</script>


<style scoped>
 .handle-box {
   margin-bottom: 20px;
 }
</style>

