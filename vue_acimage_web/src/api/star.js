import request from '@/utils/request.js'

//操作
export let deleteStar=function(topicId){
	return request.delete('/api/community/stars/operate/'+topicId);
}

export let addStar=function(topicId){
	return request.post('/api/community/stars/operate/'+topicId);
}


//查询
export let queryIsStar=function(topicId){
	return request.get('/api/community/stars/query/isStar/'+topicId)
}

export let pageMyStar=function(pageNo){
	return request.get('/api/community/stars/query/mine/' + pageNo);
}