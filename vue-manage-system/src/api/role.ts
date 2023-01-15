import request from '@/utils/request'


export function queryAll() {
	return request.get("/api/admin/roles/all");
}

export function add(data) {
	return request.post("/api/admin/roles", data);
}

export function deleteById(id) {
	return request.delete("/api/admin/roles/"+id);
}

export function modify(data) {
	return request.put("/api/admin/roles", data);
}



