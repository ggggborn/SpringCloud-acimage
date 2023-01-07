import request from '@/utils/request.js'
import { Code } from '@/utils/result.js'



export let pageUserRankByTopicCount = function(pageNo) {
	let url='/api/community/users/rank/topicCount/'+pageNo;
	return request.get(url);
}

export let pageUserRankByStarCount = function(pageNo) {
	let url='/api/community/users/rank/starCount/'+pageNo;
	return request.get(url);
}