import Vue from 'vue';
import Vuex from 'vuex';
import user from './module/user'
import baseUrl from './module/baseUrl';
Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        user,
        baseUrl
    }
        
    
});
export default store;
