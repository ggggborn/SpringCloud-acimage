import request from '@/utils/request.js'



//查询话题
export let queryRecentHotTopics = function() {
	return request.get('/api/community/topics/query/recentHot');
}

export let queryRecommendTopics = function() {
	return request.get('/api/community/topics/query/recommend');
}

export let queryMostCommentCountTopics = function() {
	return request.get('/api/community/topics/query/most/commentCount');
}

export let queryTopicAndFirstCommentPage = function(topicId) {
	return request.get('/api/community/topics/query/info/' + topicId);
}

export let pageMyPublishTopic = function(pageNo) {
	return request.get('/api/community/topics/query/mine/' + pageNo);
}

export let pageRencentTopic = function(pageNo) {
	return request.get('/api/community/topics/query/pageRecentTopics/' + pageNo)
}



export let pageByCategoryId = function(formData) {
	return request.get('/api/community/topics/query/byCategoryId',{params:formData})
}

export let pageByTagId = function(formData) {
	return request.get('/api/community/topics/query/byTagId',{params:formData})
}





//操作话题
export let addTopic = function(formData) {
	let config = { 'Content-type': 'multipart/form-data' };
	return request.post('/api/community/topics/operate', formData, config);
}

export let modifyTitle = function(id, title) {
	return request.put('/api/community/topics/operate/title/' + id + '/' + title);
}

export let modifyHtml= function(data) {
	return request.put('/api/community/topics/operate/html', data);
}

export let deleteTopic = function(topicId) {
	return request.delete('/api/community/topics/operate' + topicId)
}
