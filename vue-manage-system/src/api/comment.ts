import request from '@/utils/request'

//查
export function queryCommentsBy(params) {
	return request.get("/api/admin/comments/query/by", { params: params });
}

//操作
export function deleteComment(commentId) {
	return request.delete("/api/admin/comments/operate/" + commentId);
}