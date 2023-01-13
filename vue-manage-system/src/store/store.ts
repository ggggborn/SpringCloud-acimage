import { defineStore } from 'pinia';
import Config from '@/config'

interface ListItem {
	name: string;
	path: string;
	title: string;
}

export const useTagsStore = defineStore('tags', {
	state: () => {
		return {
			list: <ListItem[]>[]
		};
	},
	getters: {
		show: state => {
			return state.list.length > 0;
		},
		nameList: state => {
			return state.list.map(item => item.name);
		}
	},
	actions: {

	}
});
