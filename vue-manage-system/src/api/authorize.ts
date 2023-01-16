import request from '@/utils/request'

//查
export function queryRoleAuthorize(roleId) {
	return request.get("/api/admin/authorizes/roleId/"+roleId);
}

//写
export function addAuthorize(formData) {
	let config={params:formData}
	return request.post("/api/admin/authorizes",formData,config);
}

export function deleteAuthorize(roleId,permissionId) {
	return request.delete("/api/admin/authorizes/"+roleId+'/'+permissionId);
}
