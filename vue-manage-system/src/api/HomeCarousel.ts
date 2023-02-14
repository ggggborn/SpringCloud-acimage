import request from '@/utils/request'


// export function addImageIntoHomeCarousel(reqData, _this) {
// 	$.ajax({
// 		url: "/api/admin/SpImages/upload",
// 		type: "post", //请求方式
// 		data: reqData, //请求数据
// 		processData: false, //是否将请求数据转换为对象
// 		contentType: false, //发送数据到服务器时所使用的内容类型，false表示不需要ajax处理
// 		dataType: 'json',
// 		success: function(result) {
// 			if (result.code == Code.OK) {
// 				return result.data;
// 			} else {
// 				_this.$global.popupHint(result.msg);
// 				return undefined;
// 			}
// 		},
// 		error: function() {
// 			_this.$global.popupHint("提交失败");
// 			return 'ssss';
// 		}
// 	});
// }

export function add(formData) {
	const config : any = { 'Content-type': 'multipart/form-data' };
	return request.post("/api/admin/homeCarousels", formData, config);
}

export function deleteById(id) {
	return request.delete("/api/admin/homeCarousels/"+id);
}

export function modifyDescription(params) {
	let config = { params: params };
	return request.put("/api/admin/homeCarousels/descriptionAndLink", {}, config);
}

export function coverImage(reqData) {
	return request.post("/api/admin/homeCarousels/cover", reqData);
}

export function queryCurrent() {
	return request.get("/api/admin/homeCarousels/current");
}
