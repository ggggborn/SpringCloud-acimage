import axios from 'axios';
import { Message, MessageBox, Loading, Notification } from 'element-ui';
import MessageUtils from '@/utils/MessageUtils'
import { Code } from '@/utils/result.js';
import store from '@/store'

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
		config.headers['authorization'] = store.state.token;
		requestNum++;
		if (loading == null) {
			loading = Loading.service({ fullscreen: true, text: '正在努力加载中~', background: 'rgba(250, 250, 250, 0.5)' });
			// setTimeout(() => { loading.close(); }, 1);
		} else if (loading != null && requestNum > 0) {
			loading = Loading.service({ fullscreen: true, text: '正在努力加载中~',background: 'rgba(250, 250, 250, 0.5)'  });
		}
		return config;
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
			MessageUtils.notice(result.msg, 2)
		} else if (result.code == Code.OK) {
			return response.data;
		} else {
			// 出错了直接关闭loading
			requestNum = 0
			loading.close();
		}
	},
	error => {
		//拦截到失败的数据
		console.log('错误码', error)
		if (error.response.status == 401) {
			MessageUtils.notice('(￣_,￣ )请登录或成为认证用户后再操作')
		} else if(error.response.status == 429){
			MessageUtils.notice('系统繁忙┗( T﹏T )┛')
		} else {
			MessageUtils.notice("出错了o(≧口≦)o，状态码"+error.response.status)
		}
		// 出错了直接关闭loading
		requestNum = 0;
		loading.close();
		return Promise.reject(error);
	}
);

export default service;
