import request from '@/utils/request.js'

export let doLogin = function(data) {
	return request.post('/api/user/logins/doLogin', data)
}

export let doRegister = function(data) {
	return request.post('/api/user/logins/doRegister', data)
}

export let doLogout = function() {
	return request.post('/api/user/logins/logout');
}

export let sendCodeToEmail = function(email) {
	let param = { email: email }
	return request.post('/api/user/logins/sendCode',{},{ params: param })
}

export let getPublicKey = function() {
	return request.get('/api/user/logins/getPublicKey')
}
