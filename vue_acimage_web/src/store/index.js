import Vuex from 'vuex'
import Vue from 'vue'
import Config from '@/config.js'
import CommonUtils from '@/utils/CommonUtils.js'
//应用vuex插件
Vue.use(Vuex)

import jwtDecode from "jwt-decode";
import { queryAllCategories } from "@/api/category.js"
import { queryAllTags } from "@/api/tag.js"
import { Code } from '@/utils/result.js'

export default new Vuex.Store({
	//数据，相当于data
	state: {
		userId: '',
		username: '',
		photoUrl: '',
		token: '',
		categoryList: [],
		tagList: [],
		//elementui五种按钮类型
		types: ['success', 'primary', 'info', 'warning', 'danger']
	},
	getters: {
		truePhotoUrl(state) {
			if (!CommonUtils.isEmpty(state.photoUrl)) {
				return Config.domainOfImages + state.photoUrl;
			}
			return Config.domainOfImages + 'userPhoto/default.jpeg';
		},
		categoryLabel(state) {
			return (id) => {
				for (let item of state.categoryList) {
					if (item.id == id) {
						return item.label;
					}
				}
				return null;
			}
		},
		tagLabel(state) {
			return (id) => {
				for (let item of state.tagList) {
					if (item.id == id) {
						return item.label;
					}
				}
				return null;
			}
		}

	},
	//里面定义方法，操作state方发
	mutations: {
		init(state) {
			state.token = localStorage.getItem("token");
			try {
				state.userId = jwtDecode(state.token).userId;
				state.userName = jwtDecode(state.token).userName;
				state.photoUrl = jwtDecode(state.token).photoUrl;
			} catch (err) {
				state.userId = '';
				state.username = '';
				state.photoUrl = '';
			}
			queryAllCategories().then(res => {
					if (res.code == Code.OK) {
						state.categoryList = res.data;
					}
				}),
				queryAllTags().then(res => {
					state.tagList = res.data;
				});
		},
		setToken(state, token) {
			localStorage.setItem("token", token);
			this.commit('init')
		},
		removeToken(state) {
			localStorage.removeItem("token");
			this.commit('init')
		}
	},
	// 操作异步操作mutation
	actions: {

	},
	modules: {

	},
})
