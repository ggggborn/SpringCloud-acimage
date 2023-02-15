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



// global.trueImageUrl = function(url) {
// 	if (CommonUtils.isEmpty(url) || url == '#') {
// 		return '#';
// 	}
// 	return global.baseImageUrl + url;
// }

//获取头像路径
global.getPhotoUrl = function(url) {
	if (CommonUtils.isEmpty(url) || url == '#') {
		return 'static/image/default-photo.webp';
	}
	return url;
}

//获取分享链接
global.getTopicUrl = function(topicId) {
	return '/topic/' + topicId;
}

global.isEmpty = function(object) {
	return CommonUtils.isEmpty(object);
}

global.buttonType = function(id) {
	let types = ['success', 'primary', 'warning', 'danger'];
	return types[id % 4];
}

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
	let duration = d2.getTime() - d1.getTime();

	let time = parseInt(Math.floor(duration / (1000 * 60 * 60 * 24 * 30)));
	if (time >= 1) {
		return datetime;
	}

	time = parseInt(Math.floor(duration / (1000 * 60 * 60 * 24)));
	if (time == 1) {
		return '昨天';
	} else if (time > 1) {
		return time + '天前';
	}

	time = parseInt(Math.floor(duration / (1000 * 60 * 60)));
	if (time >= 1) {
		return time + '小时前'
	}

	time = parseInt(Math.floor(duration / (1000 * 60)));
	if (time >= 1) {
		return time + '分钟前'
	}

	time = parseInt(Math.floor(duration / (1000)));
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
