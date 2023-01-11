import Vuex from 'vuex'
import Vue from 'vue'
import Config from '@/config.js'
import CommonUtils from '@/utils/CommonUtils.js'
//应用vuex插件
Vue.use(Vuex)

import jwtDecode from "jwt-decode";


export default new Vuex.Store({
	//数据，相当于data
	state: {
		userId: '',
		username: '',
		photoUrl: '',
		token: ''
	},
	getters: {
		truePhotoUrl(state) {
			if (!CommonUtils.isEmpty(state.photoUrl)) {
				return Config.domainOfImages + state.photoUrl;
			}
			return Config.domainOfImages + 'userPhoto/default.jpeg';
			// return ''
		}
	},
	//里面定义方法，操作state方发
	mutations: {
		init(state) {
			state.token = localStorage.getItem("token");
			try{
				state.userId = jwtDecode(state.token).userId;
				state.userName = jwtDecode(state.token).userName;
				state.photoUrl = jwtDecode(state.token).photoUrl;
			}catch(err){
				state.userId='';
				state.username='';
				state.photoUrl='';
			}

		},
		setToken(state, token) {
			localStorage.setItem("token", token);
			this.commit('init')
		},
		removeToken(state) {
			localStorage.removeItem("token");
			state.commit('init')
		}
	},
	// 操作异步操作mutation
	actions: {

	},
	modules: {

	},
})
