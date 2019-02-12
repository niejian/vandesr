const user = {
    state: {
        userName: '',
        email: '',
        avatar: '',
        roles: [],
        token: '',
        loginAccount: ''
    },
    
    mutations: {
        setToken(state, token){
            state.token = token;
        },
        setAvatar(state, avatar){
            state.avatar = avatar;
        },
        setUserName(state, userName){
            state.userName = userName;
        },
        setEmail(state, email){
            state.email = email;
        },
        setLoginAccount(state, loginAccount) {
            state.loginAccount = loginAccount;
        }
    },
    getters: {
        getToken: state => {
            if(null == state.token || '' == state.token){
                state.token = sessionStorage.getItem(state.loginAccount + "_token_key");
            }
            return state.token;
        },
        getUserName: state => {
            if(null == state.userName || '' == state.userName){
                state.userName = sessionStorage.getItem('login_user_name');
            }
            return state.userName;
        },
        getEmail: state => {
            if(null == state.email || '' == state.email){
                state.email = sessionStorage.getItem('login_user_email');
            }
            return state.email;
        },
        getAvatar: state => {
            if(null == state.avatar || '' == state.avatar){
                state.avatar = sessionStorage.getItem('login_user_avatar');
            }
            return state.avatar;
        },
        getRoles: state => {
            
            if(null == state.roles || '' == state.roles){
                state.roles = sessionStorage.getItem('login_user_roles');
            }
            return state.roles;
        },
        getLoginAccount: state => {
            if(null == state.loginAccount || '' == state.loginAccount){
                state.loginAccount = sessionStorage.getItem('login_account');
            }
            return state.loginAccount;
        }
    }
}

export default user;