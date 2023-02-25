import request from '@/utils/request.js'



//查询
export let pageCommentMessages = function(pageNo,pageSize) {
	return request.get('/api/user/messages/query/comments/page/'+pageNo+'/'+pageSize)
}


