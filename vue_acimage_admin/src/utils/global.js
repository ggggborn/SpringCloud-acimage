import Vue from 'vue'
import {Code} from '@/utils/result.js'

let global = new Vue();

global.popupHint = function(msg) {
	this.$notify({
		title: '提示',
		message: msg,
		type: 'warning'
	});
}

global.popupSuccess = function(msg) {
	this.$notify({
		title: '成功',
		message: msg,
		type: 'success'
	});
}

global.popupMsgIfOk = function(request,msg) {
	request.then(result=>{
		console.log(result);
		if (result.code == Code.OK) {
			global.popupSuccess(msg);
		}
	});
}

global.baseImageUrl='http://rlqaxj6tc.hn-bkt.clouddn.com/';

global.trueImageUrl = function(url) {
	return global.baseImageUrl+url;
}



export default global;
// exports.install = function(Vue, options) {
// 	let global=new Vue();
// 	global.popupHint = function(msg) {
// 		this.$notify({
// 			title: '提示',
// 			message: msg,
// 			type: 'warning'
// 		});
// 	}
// 	Vue.prototype.$global=global
// }

