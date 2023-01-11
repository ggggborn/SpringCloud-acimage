import request from '@/utils/request.js'


export let deleteStar=function(topicId){
	return request.delete('/api/community/stars/'+topicId);
}

export let addStar=function(topicId){
	return request.post('/api/community/stars/'+topicId);
}

export let queryIsStar=function(topicId){
	return request.get('/api/community/stars/isStar/'+topicId)
}

export let pageMyStar=function(pageNo){
	return request.get('/api/community/stars/mine/' + pageNo);
}