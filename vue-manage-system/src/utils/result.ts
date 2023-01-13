// 状态码
export let Code= {
	OK: 20000,
	ERR: 20001,
	DATA_NOT_EXIST: 20011
}



// //获取图片路径
// Vue.prototype.getImageUrl = function(imageId) {
// 	return "../storage/images/" + imageId + ".jpeg";
// }
// //获取头像路径
// Vue.prototype.getPhotoUrl = function(photoId) {
// 	return "../storage/photos/" + photoId + ".jpeg";
// }

// //获取分享链接
// Vue.prototype.getTopicUrl = function(shareId) {
// 	return '/templates/ShowTopic.html?id=' + shareId
// }

// /*获取存储在浏览器cookie的用户信息*/
// Vue.prototype.PHOTO_URL = function() {
// 	let photoId = $.cookie('photoId');
// 	if (!isEmpty(photoId)) {
// 		return this.getPhotoUrl(photoId);
// 	}
// 	return '#';
// }

// Vue.prototype.USERNAME = function() {
// 	let username = $.cookie('username');
// 	if (!isEmpty(username)) {
// 		return username;
// 	}
// 	return '';
// }

// Vue.prototype.USER_ID = function() {
// 	let userId = $.cookie('userId');
// 	if (!isEmpty(userId)) {
// 		return userId;
// 	}
// 	return '';
// }
// /*End cookie信息*/

// /*消息提示，进一步封装elementUI的消息提示*/
// Vue.prototype.popupHint = function(msg) {
// 	this.$notify({
// 		title: '提示',
// 		message: msg,
// 		type: 'warning'
// 	});
// }

// Vue.prototype.popupSuccess = function(msg) {
// 	this.$notify({
// 		title: '成功',
// 		message: msg,
// 		type: 'success'
// 	});
// }


//控制显示长度
// Vue.prototype.omitStr = function(content, totalLength) {
// 	if (content.length > totalLength) {
// 		return content.slice(0, totalLength - 3) + '...'
// 	} else {
// 		return content;
// 	}
// }


//axios拦截
// axios.interceptors.response.use(
// 	function(response) {
// 		let result = response.data;
// 		if (result.code == Code.ERR) {
// 			Vue.prototype.popupHint(result.msg);
// 			return false;
// 		}
// 		return response;
// 	},
// 	function(error) {
// 		// 对响应错误进行操作
// 		return Promise.reject(error);
// 	}
// );
