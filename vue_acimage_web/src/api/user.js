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
		if (resp.data.code == Code.ok) {
			return resp.data;
		}
	});
}

export let queryProfile = function() {
	return request.get('/api/user/users/me');

}
