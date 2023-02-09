import request from '@/utils/request.js'
import axios from 'axios'
import { Code } from '@/utils/result.js'


//查询
export let queryProfile = function() {
	return request.get('/api/user/users/query/me');
}



//操作
export let modifyUsername = function(newUsername) {
	return request.put('/api/user/users/operate/username/' + newUsername)
}

// export let uploadPhoto = function(reqData) {
// 	let config = { 'Content-type': 'multipart/form-data' };
// 	return request.post('/api/user/users/uploadPhoto', reqData, config);
// }




