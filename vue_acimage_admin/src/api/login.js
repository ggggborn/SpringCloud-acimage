import request from '@/utils/request.js'

export let doLogin = function(reqData) {
	return request.post('/api/user/logins/doLogin', reqData)
}

export let doRegister = function(reqData) {
	return request.post('/api/user/logins/doRegister', reqData)
}

export let doLogout=function(){
	return request.post('/api/user/logins/logout');
}

export let getPublicKey=function(){
	return request.get('/api/user/logins/getPublicKey')
}

