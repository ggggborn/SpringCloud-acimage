import request from '@/utils/request.js'

export let searchTopicsAsPage = function(formData) {
	let config = { params: formData }
	return request.get('/api/community/topics/search/topics', config)
}
