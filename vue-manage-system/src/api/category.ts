import request from '@/utils/request'

//查
export function queryAllCategories() {
	return request.get("/api/admin/categories/all");
}


