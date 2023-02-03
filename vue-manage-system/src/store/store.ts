import { defineStore } from 'pinia';
import Config from '@/config'
import { queryAllCategories } from '@/api/category'
import { Code } from '@/utils/result'

interface Category {
	id: number;
	label: string;
	createTime: string;
	updateTime: string;
}

export const useStore = defineStore('store', {
	state: () => {
		return {
			categoryList: <Category[]>[]
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
		}
	}
});
