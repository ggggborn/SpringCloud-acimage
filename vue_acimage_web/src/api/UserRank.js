import request from '@/utils/request.js'
import { Code } from '@/utils/result.js'

export let pageUserRankBy = function(params) {
	return request.get('/api/community/users/rank/byColumn', { params: params });
}
