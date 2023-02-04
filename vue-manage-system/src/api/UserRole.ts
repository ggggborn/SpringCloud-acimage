import request from '@/utils/request'

//查
export function addRoleForUser(userId, roleId) {
	let params = { userId: userId, roleId: roleId };
	let config = { params: params }
	return request.post("/api/admin/userRoles/operate", {}, config);
}

export function deleteRoleForUser(userId, roleId) {
	let params = { userId: userId, roleId: roleId };
	let config = { params: params }
	return request.delete("/api/admin/userRoles/operate/"+userId+'/'+roleId);
}

//操作



