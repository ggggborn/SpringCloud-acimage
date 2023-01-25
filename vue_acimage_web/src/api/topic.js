import request from '@/utils/request.js'


export let addTopicAndUploadImages = function(formData) {
	let config = { 'Content-type': 'multipart/form-data' };
	return request.post('/api/community/topics/', formData, config);
}

export let addTopic = function(formData) {
	let config = { 'Content-type': 'multipart/form-data' };
	return request.post('/api/community/topics/', formData, config);
}

// export let addTopic = function(data) {
// 	// let urlParams = new URLSearchParams();
// 	// for (let imageId of imageIds) {
// 	// 	urlParams.append("imageIds", imageId);
// 	// }
// 	// let config = {  params: urlParams }
// 	// let config = {
// 	// 	header: { 'Content-Type': 'application/json' }
// 	// };
// 	return request.post('/api/community/topics', data);
// }

export let modifyTitle = function(id, title) {
	// let urlParams = new URLSearchParams();
	// for (let imageId of imageIds) {
	// 	urlParams.append("imageIds", imageId);
	// }
	// let config = {  params: urlParams }
	// let config = {
	// 	header: { 'Content-Type': 'application/json' }
	// };
	return request.put('/api/community/topics/title/' + id + '/' + title);
}

export let modifyContent = function(data) {
	// let urlParams = new URLSearchParams();
	// for (let imageId of imageIds) {
	// 	urlParams.append("imageIds", imageId);
	// }
	// let config = {  params: urlParams }
	// let config = {
	// 	header: { 'Content-Type': 'application/json' }
	// };
	return request.put('/api/community/topics/content', data);
}

export let deleteTopic = function(topicId) {
	return request.delete('/api/community/topics/' + topicId)
}

// export let modifyTopicAndImageDescriptions = function(data) {
// 	return request.put('/api/community/topics', data);
// }

export let queryRecentHotTopics = function() {
	return request.get('/api/community/topics/recentHot');
}

export let queryRecommendTopics = function() {
	return request.get('/api/community/topics/recommend');
}

export let queryTopicAndFirstCommentPage = function(topicId) {
	return request.get('/api/community/topics/info/' + topicId);
}

export let pageMyPublishTopic = function(pageNo) {
	return request.get('/api/community/topics/mine/' + pageNo);
}

export let pageRencentTopic = function(pageNo) {
	return request.get('/api/community/topics/pageRecentTopics/' + pageNo)
}
