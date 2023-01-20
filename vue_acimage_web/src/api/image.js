import request from '@/utils/request.js'
import axios from 'axios'
import MessageUtils from '@/utils/MessageUtils'
import global from '@/utils/global.js'

export let downloadImages = function(imageIds, fileName) {

	let urlParams = new URLSearchParams();
	for (let imageId of imageIds) {
		urlParams.append("imageIds", imageId);
	}
	// let url='/api/files/download/imageFiles?'+urlParams;
	let config = { responseType: 'blob', params: urlParams, headers: { 'authorization': global.TOKEN() } }
	axios.post('/api/image/download/imageFiles', {}, config)
		.then(resp => {
			if (resp.data.type == 'application/json') {
				const reader = new FileReader(); //创建一个FileReader实例
				reader.readAsText(resp.data, 'utf-8'); //读取文件,结果用字符串形式表示
				reader.onload = function() { //读取完成后,**获取reader.result**
					const result = JSON.parse(reader.result);
					MessageUtils.notice(result.msg);
				}
				return;
			}

			let url = window.URL.createObjectURL(new Blob([resp.data]));
			let link = document.createElement('a');
			link.style.display = 'none';
			link.href = url;
			link.setAttribute('download', fileName + ".zip");
			document.body.appendChild(link);
			link.click();
			// 释放URL对象所占资源
			window.URL.revokeObjectURL(url);
			// 用完即删
			document.body.removeChild(link);
		})
}

export let searchImagesByImage = function(reqData) {
	let config = { 'Content-type': 'multipart/form-data' };
	return request.post('/api/image/images/searchByImage', reqData, config);
}

export let uploadTopicImages = function(reqData) {
	let config = { 'Content-type': 'multipart/form-data' };
	return request.post('/api/image/images/upload/topicImages', reqData, config);
}

export let uploadTopicImage = function(reqData) {
	let config = { 'Content-type': 'multipart/form-data' };
	return request.post('/api/image/images/upload/topicImage', reqData, config);
}

export let modifyDescription = function(imageId, description) {
	return request.put('/api/image/images/description/' + imageId + '/' + description);
}


