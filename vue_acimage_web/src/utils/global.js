import Vue from 'vue'
import cookie from 'vue-cookie'
import CommonUtils from '@/utils/CommonUtils.js'
import MessageUtils from '@/utils/MessageUtils.js'
import StringUtls from '@/utils/StringUtils.js'

import Config from '@/config.js'
import { Code } from '@/utils/result.js'
import { MessageBox } from 'element-ui'
import jwtDecode from "jwt-decode";
import StringUtils from '@/utils/StringUtils.js'

let global = new Vue();


//消息提示功能
// global.popupHint = function(msg) {
// 	this.$notify({
// 		title: '提示',
// 		message: msg,
// 		type: 'warning'
// 	});
// }

// global.popupSuccess = function(msg) {
// 	this.$notify({
// 		title: '成功',
// 		message: msg,
// 		type: 'success'
// 	});
// }

// global.popupMsgIfOk = function(request, msg) {
// 	request.then(result => {
// 		console.log(result);
// 		if (result.code == Code.OK) {
// 			global.popupSuccess(msg);
// 		}
// 	});
// }

// global.openConfirm = function(msg) {
// 	return MessageBox.confirm(msg, '提示', {
// 		confirmButtonText: '确定',
// 		cancelButtonText: '取消',
// 		type: 'warning'
// 	})
// }


//真实图片链接
global.baseImageUrl = Config.domainOfImages;

global.trueImageUrl = function(url) {
	if (CommonUtils.isEmpty(url) || url == '#') {
		return '#';
	}
	return global.baseImageUrl + url;
}

//获取头像路径
global.truePhotoUrl = function(url) {
	if (CommonUtils.isEmpty(url) || url == '#') {
		return global.baseImageUrl + 'userPhoto/default.jpeg';

	}
	return global.baseImageUrl + url;
}

//获取分享链接
global.getTopicUrl = function(topicId) {
	return '/topic/' + topicId;
}

// //token及token内信息
// global.TOKEN = function() {
// 	let token = localStorage.getItem("token");
// 	if (CommonUtils.isEmpty(token)) {
// 		return "";
// 	} else {
// 		return token;
// 	}
// }

// global.setToken = function(token) {
// 	localStorage.setItem("token", token);
// }

// global.removeToken = function() {
// 	localStorage.removeItem("token");
// }

// global.PHOTO_URL = function() {
// 	let token = global.TOKEN();
// 	if (!CommonUtils.isEmpty(token)) {
// 		return global.truePhotoUrl(jwtDecode(token).photoUrl);
// 	}
// 	return '#';
// }

// global.USERNAME = function() {
// 	let token = global.TOKEN();
// 	if (!CommonUtils.isEmpty(token)) {
// 		return jwtDecode(token).username;
// 	}
// 	return '';
// }

// global.USER_ID = function() {
// 	let token = global.TOKEN();
// 	if (!CommonUtils.isEmpty(token)) {
// 		return jwtDecode(token).userId;
// 	}
// 	return '';
// }

//限制显示长度
global.omitStr = function(content, totalLength) {
	if (content.length > totalLength) {
		return content.slice(0, totalLength - 2) + '...'
	} else {
		return content;
	}
}

global.timeView = function(datetime) {
	let d1 = new Date(datetime);
	let d2 = new Date();
	let days = d2.getTime() - d1.getTime();

	let time = parseInt(days / (1000 * 60 * 60 * 24 * 30));
	if (time >= 1) {
		return time + '个月前';
	}

	time = parseInt(days / (1000 * 60 * 60 * 24));
	if (time == 1) {
		return '昨天';
	} else if (time > 1) {
		return time + '天前';
	}

	time = parseInt(days / (1000 * 60 * 60));
	if (time >= 1) {
		return time + '小时前'
	}

	time = parseInt(days / (1000 * 60));
	if (time >= 1) {
		return time + '分钟前'
	}

	time = parseInt(days / (1000));
	if (time >= 1) {
		return time + '秒前'
	}

	return '刚刚'
}

global.html2Text = function(contentHtml, limitLength) {
	let content = StringUtils.html2Text(contentHtml);
	if (limitLength == undefined) {
		return content;
	}
	if (content.length > limitLength) {
		return content.slice(0, limitLength - 3) + '...'
	} else {
		return content;
	}
}

export default global;
