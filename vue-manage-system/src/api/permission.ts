import request from '@/utils/request'

//查
export function queryPermissionTree() {
	return request.get("/api/admin/permissions/tree");
}

export function pagePermission(pageNo,pageSize) {
	return request.get("/api/admin/permissions/page/"+pageNo+'/'+pageSize);
}

export function queryModules() {
	return request.get("/api/admin/permissions/modules");
}

//写
export function addPermission(data) {
	return request.post("/api/admin/permissions",data);
}

export function modifyPermission(data) {
	return request.put("/api/admin/permissions",data);
}

export function deletePermission(id) {
	return request.delete("/api/admin/permissions/"+id);
}
