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
                    <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                </div>
                <p class="login-tips">Tips : 用户名和密码随便填。</p>
            </el-form>
        </div>
    </div>
</template>

<script>
    import md5 from 'js-md5'
    import httpPost from '@/lib/http';
    import storage from '@/lib/storage';
    export default {
        data: function(){
            return {
                ruleForm: {
                    userName: 'code4fun@qq.com',
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
                    if (valid) {
                        let baseUrl = `${this.URL_PREFIX}`;
                        
                        let url = baseUrl + '/user/login';
                        let requestData = {
                                'email': this.ruleForm.username,
                                //'password': md5(this.ruleForm.password)
                                'password': this.ruleForm.password
                            };

                        this.$axios.post(
                            url,
                            requestData
                        ).then(res => {
                            
                            debugger
                            let responseData = res.data;
                            let isSuccess = responseData.isSuccess;
                            let responseCode = responseData.responseCode;
                            if(isSuccess && 0 == responseCode){

                                //登陆成功
                                this.$message({
                                    type: 'success',
                                    message: '登陆成功',
                                    duration: 1000
                                })
                                let token = responseData.token;
                                //
                                this.$store.commit('setToken', token);
                                this.$store.commit('setLoginAccount', this.ruleForm.username)
                                storage.setItem('login_account', this.ruleForm.username);
                                storage.setItem(this.ruleForm.username + "_token_key", token);
                                //获取用户权限
                                this.getUserInfo(baseUrl + "/user/getUserInfo")
                                
                            }else {
                                //登陆失败
                                this.$message({
                                    type: 'error',
                                    duration: 1000,
                                    message: responseData.responseMsg
                                })
                            }
                        }).catch(err => {
                            this.$message({
                                type: 'error',
                                message: '登陆失败，请重试！'
                            })
                        })
                        
                    
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            getUserInfo(url) {
                this.$axios.post(
                    url,
                    {}
                ).then(res => {
                    debugger
                    let responseData = res.data;
                    let isSuccess = responseData.success;
                    let responseCode = responseData.responseCode;

                    if (isSuccess && 0 == responseCode){
                        let data = responseData.data;
                        if(null != data){
                            //let user = data.user;
                            let roles = data.roles;

                            let email = data.email;
                            let avatar = data.avatar;
                            let userName = data.userName;

                            sessionStorage.setItem('login_user_name', userName);
                            sessionStorage.setItem('login_user_account', email);
                            sessionStorage.setItem('login_user_avatar', avatar);
                            sessionStorage.setItem('login_user_roles', roles);

                            //条状
                            this.$router.push("/")

                        }
                    }

                }).catch(err => {
                    console.log(err)
                })
            }
        }
    }

    
</script>

<style scoped>
    .login-wrap{
        position: relative;
        width:100%;
        height:100%;
        background-image: url(../../assets/login-bg.jpg);
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