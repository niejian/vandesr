<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="ruleForm.username" placeholder="username">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="password" v-model="ruleForm.password" @keyup.enter.native="submitForm('ruleForm')">
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm('ruleForm')" v-loading="loading" 
                      element-loading-text="登录中....请稍候">登录</el-button>
                </div>
                <p class="login-tips">Tips : 用户名和密码随便填。</p>
            </el-form>
        </div>
    </div>
</template>

<script>
    import MenuUtils from '@/utils/menuUtils'
    import md5 from 'js-md5'
    import {addSessionData} from '@/utils/storeUtil'
    import {login} from  '@/api/user'
    import NProgress from 'nprogress'
    import 'nprogress/nprogress.css'
    var routers = []
    export default {
      
        data: function(){
          
            return {
                loading: false,
                ruleForm: {
                    username: 'code4fun@qq.com',
                    password: 'qq123123'
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                  let menus = [];
                  let token = '';
                  let menuRouters = [];
                    if (valid) {
                      NProgress.start();
                      this.loading = true;
                      login({
                        'email': this.ruleForm.username,
                        'password': md5(this.ruleForm.password)
                       }).then(response => {
                        
                         this.loading = false
                         token = response.data.token;
                          menus = response.data.menusTree;
                          menuRouters = response.data.leafMenus;
                          if(!token) {
                            // this.$message.error(response.data.responseMsg);
                            this.$message({
                              type: 'error',
                              showClose: true,
                              message: response.responseMsg
                            })
                            this.$router.push('/login');
                          }
                          addSessionData('token', token);

                          if(menus && menus.length > 0) {
                            
                            // 登陆请求
                            addSessionData('menuTrees', JSON.stringify(menus))
                            addSessionData('menuRouters',JSON.stringify(menuRouters))
                            addSessionData('ms_username',this.ruleForm.username);
                            
                          }
                          this.$router.push('/');

                          NProgress.done()
                       })


                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .login-wrap{
        position: relative;
        width:100%;
        height:100%;
        background-image: url(../../assets/img/login-bg.jpg);
        background-size: 100%;
    }
    .ms-title{
        width:100%;
        line-height: 50px;
        text-align: center;
        font-size:20px;
        color: #fff;
        border-bottom: 1px solid #ddd;
    }
    .ms-login{
        position: absolute;
        left:50%;
        top:50%;
        width:350px;
        margin:-190px 0 0 -175px;
        border-radius: 5px;
        background: rgba(255,255,255, 0.3);
        overflow: hidden;
    }
    .ms-content{
        padding: 30px 30px;
    }
    .login-btn{
        text-align: center;
    }
    .login-btn button{
        width:100%;
        height:36px;
        margin-bottom: 10px;
    }
    .login-tips{
        font-size:12px;
        line-height:30px;
        color:#fff;
    }
</style>