import Mock from 'mockjs'
import { Code } from '@/utils/result.js'

Mock.mock('/api/community/topics/recentHot', 'get', {
	code: Code.OK,
	'data|4': [{
			id: '@id()',
			'title|5': '@cname()',
			starCount: 888,
			pageView: 88888,
			createTime: '@date()',
			user: {
				username: '@cname()',
				photoUrl: ''
			},
			coverImageUrl: 'test/test1.jpeg',
			categoryId:1,
		},
		{
			id: '@id()',
			'title|15': '@cname()',
			starCount: 888,
			pageView: 88888,
			createTime: '2022-2-22 22:22:22',
			user: {
				username: '@cname()',
				photoUrl: ''
			},
			coverImageUrl: 'topicImage/2022/12/02/1573240094587424768',
			categoryId:1,
		},
	]
})

Mock.mock('/api/community/topics/recommend', 'get', {
	code: Code.OK,
	'data|2': [{
			id: '@integer()',
			'title|5': '@cname()',
			commentCount:123,
			starCount: 888,
			pageView: 88888,
			createTime: '@date()',
			user: {
				username: '@cname()',
				photoUrl: ''
			},
			coverImageUrl: 'test/test1.jpeg',
		},
		{
			id: '@integer()',
			'title|15': '@cname()',
			commentCount:123,
			starCount: 888,
			pageView: 88888,
			createTime: '2022-2-22 22:22:22',
			user: {
				username: '刚刚刚刚刚刚刚刚',
				photoUrl: ''
			},
			coverImageUrl: 'topicImage/2022/12/02/1573240094587424768',
			categoryId:1,
		},
	]
})

Mock.mock('/api/community/topics/pageRecentTopics/1', 'get', {
	code: Code.OK,
	data: {
		'dataList|5': [{
			id: '@integer()',
			userId: 999,
			'title|10': "@cname()",
			'content': `不知道@cparagraph()`,
			activityTime: '2022-2-22 2:22:22',
			starCount: 666,
			pageView: 777,
			commentCount: 888,
			coverImageUrl: 'static/image/user-rank-header.jpg',
			categoryId:1,
			images: [{
				id: 0
			}],
			user: {
				photoUrl: '',
				'username|2': "@cname()"
			}
		}, ],
		totalCount: 10,
	}

})
