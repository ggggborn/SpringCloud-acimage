import request from '@/utils/request'

//查
export function queryUsers(params) {
	return request.get("/api/admin/users/query/search", { params: params });
}

//操作



