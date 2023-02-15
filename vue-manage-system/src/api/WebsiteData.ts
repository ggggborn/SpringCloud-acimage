import request from '@/utils/request'


export function queryWebsiteData() {
	return request.get("/api/admin/websites/accessData");
}




