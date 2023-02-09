import request from '@/utils/request.js'
import axios from 'axios'
import { Code } from '@/utils/result.js'

export let sendCodeToEmail = function(email) {
	let param = { email: email }
	return request.post('/api/user/logins/sendCode',{},{ params: param })
}

export let getPublicKey = function() {
	return request.get('/api/user/logins/getPublicKey')
}
export let queryIsUsernameExist = function(username) {
	return axios.get('/api/user/users/isExist/' + username).then(resp => {
		let res = resp.data;
		if (res.code == Code.OK) {
			return res;
		}
	});
}




export let doLogin = function(data) {
	return request.post('/api/user/logins/doLogin', data)
}

export let doRegister = function(data) {
	return request.post('/api/user/logins/doRegister', data)
}

export let doLogout = function() {
	return request.post('/api/user/logins/logout');
}


