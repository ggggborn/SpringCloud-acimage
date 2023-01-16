import request from '@/utils/request'


export function queryAllRoles() {
	return request.get("/api/admin/roles/all");
}

export function addRole(data) {
	return request.post("/api/admin/roles", data);
}

export function deleteRole(id) {
	return request.delete("/api/admin/roles/"+id);
}

export function modifyRole(data) {
	return request.put("/api/admin/roles", data);
}



