import request from '@/utils/request'

export function searchApis(params) {
	return request.get("/api/admin/apis/query/search",{params:params});
}


export function addApi(data) {
	return request.post("/api/admin/apis/operate",data);
}

export function modifyApi(data) {
	return request.put("/api/admin/apis/operate",data);
}

export function deleteApi(id) {
	return request.delete("/api/admin/apis/operate/"+id);
}





