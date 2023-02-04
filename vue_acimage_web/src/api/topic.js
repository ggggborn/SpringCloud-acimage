import request from '@/utils/request.js'



//查询话题
export let queryRecentHotTopics = function() {
	return request.get('/api/community/topics/query/recentHot');
}

export let queryRecommendTopics = function() {
	return request.get('/api/community/topics/query/recommend');
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
