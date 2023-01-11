import Mock from 'mockjs'
import { Code } from '@/utils/result.js'

Mock.mock('/api/community/users/rank/starCount/1', 'get', {
	code: Code.OK,
	'data|4': [{
		id: '@id',
		username: '@cname()',
		photoUrl: '',
		starCount: '@integer(0, 10000)',
		topicCount: '@integer(0, 10000)',
	}]
})

Mock.mock('/api/community/users/rank/topicCount/1', 'get', {
	code: Code.OK,
	'data|5': [{
		id: '@id',
		username: '@cname()',
		photoUrl: '',
		starCount: '@integer(0, 10000)',
		topicCount: '@integer(0, 10000)',
	}]
})
