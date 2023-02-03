import request from '@/utils/request'

//查
export function queryTopicsOrderBy(params) {
	return request.get("/api/admin/topics/query/orderBy", { params: params });
}





export function deleteTopic(topicId) {
	return request.delete("/api/admin/topics/operate/" + topicId);
}


