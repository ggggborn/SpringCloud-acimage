import request from '@/utils/request.js'
import axios from 'axios'
import { Code } from '@/utils/result.js'

export let modifyUsername = function(newUsername) {
	return request.put('/api/user/users/username/' + newUsername)
}

// export let uploadPhoto = function(reqData) {
// 	let config = { 'Content-type': 'multipart/form-data' };
// 	return request.post('/api/user/users/uploadPhoto', reqData, config);
// }

export let queryIsUsernameExist = function(username) {
	return axios.get('/api/user/users/isExist/' + username).then(resp => {
		let res = resp.data;
		if (res.code == Code.OK) {
			return res;
		}
	});
}

export let queryProfile = function() {
	return request.get('/api/user/users/me');

}
