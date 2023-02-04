import request from '@/utils/request.js'


//操作
export let addComment = function(data) {
	return request.post('/api/community/comments/operate', data);
}

export let deleteComment = function(commentId) {
	return request.delete('/api/community/comments/operate' + commentId);
}

export let modifyComment = function(data) {
	return request.put('/api/community/comments/operate', data);
}


//查询
export let queryCommentPage = function(topicId, pageNo) {
	let reqParams = {
		topicId: topicId,
		pageNo: pageNo
	};
	let config = { params: reqParams };
	return request.get('/api/community/comments/query/topicComments', config)
}

export let pageMyComments = function(pageNo) {
	return request.get('/api/community/comments/query/mine/' + pageNo)
}
