import { defineStore } from 'pinia';
import { queryAllCategories } from '@/api/category'
import { queryAllRoles } from '@/api/role';
import { Code } from '@/utils/result'

interface Category {
	id: number;
	label: string;
	createTime: string;
	updateTime: string;
}

import { Role } from '@/views/role/role.vue'

export const useStore = defineStore('store', {
	state: () => {
		return {
			categoryList: <Category[]>[],
			roleList: <Role[]>[]
		};
	},
	getters: {
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
		roleName(state) {
			return (id) => {
				for (let item of state.roleList) {
					if (item.id == id) {
						return item.roleName;
					}
				}
				return null;
			}
		}
	},
	actions: {
		init() {
			let _this = this;
			if (this.categoryList.length == 0) {
				queryAllCategories().then((res: any) => {
					if (res.code == Code.OK) {
						_this.categoryList = res.data;
					}
				});
			}
			if (this.roleList.length == 0) {
				queryAllRoles().then((res: any) => {
					if (res.code == Code.OK) {
						this.roleList = res.data;
					}
				})
			}
		}
	}
});
