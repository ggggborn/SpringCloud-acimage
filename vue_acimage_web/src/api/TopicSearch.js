import request from '@/utils/request.js'

export let searchTopics = function(formData) {
	let config = { params: formData }
	return request.get('/api/community/topics/search/multiSearch', config)
}
