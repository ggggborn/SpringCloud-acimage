import request from '@/utils/request.js'

export let addComment = function(data) {
	return request.post('/api/community/comments', data);
}

export let deleteComment=function(commentId){
	return request.delete('/api/community/comments/'+commentId);
}

export let modifyComment=function(data){
	return request.put('/api/community/comments',data);
}

export let queryCommentPage = function(topicId, pageNo) {
	let reqParams = {
		'topicId': topicId,
		'pageNo': pageNo
	};
	let config = { params: reqParams };
	return request.get('/api/community/comments/pageComments', config)
}

export let pageMyComment=function(pageNo){
	return request.get('/api/community/comments/mine/' + pageNo)
}
