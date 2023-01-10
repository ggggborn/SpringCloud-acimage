import Mock from 'mockjs'
import { Code } from '@/utils/result.js'

Mock.mock('/api/image/spImages/homeCarousel', 'get', {
	code: Code.OK,
	'data|2': [{
			id: '@id()',
			description: '@cparagraph()',
			url: '/test/test1.jpg',
		},
		{
			id: '@id()',
			description: '@cparagraph()',
			url: '/test/test2.jpeg',
		}
	]
})
