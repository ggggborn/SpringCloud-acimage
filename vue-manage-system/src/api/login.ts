import request from '@/utils/request'

export function getPublicKey(){
	return request.get("/api/admin/logins/publicKey");
}

export function doLogin(data){
	return request.post("/api/admin/logins/doLogin",data);
}