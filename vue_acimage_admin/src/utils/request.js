import axios from 'axios';
import {
	Message,
	MessageBox,
	Loading,
	Notification
} from 'element-ui';
import {
	Code
} from '@/utils/result.js';

// const service = axios.create({
// 	baseURL: "http://127.0.0.1:8080/projectName",//请求地址前缀
// 	timeout: 0
// });
const service = axios.create();

var requestNum = 0;
var loading = null;

// 请求拦截器
service.interceptors.request.use(
	config => {
		//添加请求头部参数
		// config.headers['arg1'] = "arg1Value";

		//开始loading
		requestNum++;
		if (loading == null) {
			loading = Loading.service({
				fullscreen: true,
				text: '正在努力加载中~'
			});
		} else if (loading != null && requestNum > 0) {
			loading = Loading.service({
				fullscreen: true,
				text: '正在努力加载中~'
			});
		}

		return config

	},
	error => {
		requestNum = 0;
		if (loading) {
			loading.close();
		}
		return Promise.reject(error);
	}
);

// 响应拦截器
service.interceptors.response.use(
	response => {
		//拦截到成功的数据
		requestNum--;
		if (loading == null || requestNum <= 0) {
			loading.close()
		}

		const result = response.data;
		if (result.code == Code.ERR) {
			Notification({
				title: '提示',
				message: result.msg,
				type: 'warning',
				duration: 2 * 1000
			});
		} else if (result.code == Code.OK) {
			return response.data;
		} else {
			// 出错了直接关闭loading
			requestNum = 0
			loading.close();
		}
	},
	error => {

		console.log('错误码', error)
		// 出错了直接关闭loading
		requestNum = 0
		loading.close();
		//拦截到失败的数据
		Notification({
			title: '提示',
			message: error,
			type: 'warning',
			duration: 2 * 1000
		});
		return Promise.reject(error);
	}
);

export default service;
