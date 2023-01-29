<template>
	<div class="outer-div">
		<my-header></my-header>
		<div class="wrapper">
			<div class="wrapper-left">
				<div>
					我的动态
				</div>
				<template v-for="(tab,index) in tabs">
					<div @click="tabIndex=index" :style="tabIndex==index?'color:#0B8FFF':''" :key="tab">
						{{tab}}
					</div>
				</template>
			</div>
			<div class="wrapper-right">
				<div class="activity-header">
					{{tabs[tabIndex]}}
				</div>
				<template v-if="tabIndex==0">
					<my-topic-activity></my-topic-activity>
				</template>
				<template v-else-if="tabIndex==1">
					<my-comment-activity></my-comment-activity>
				</template>
				<template v-else-if="tabIndex==2">
					<my-star-activity></my-star-activity>
				</template>
			</div>
		</div>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import MyTopicActivity from './MyTopicActivity/MyTopicActivity.vue'
	import MyCommentActivity from './MyCommentActivity/MyCommentActivity.vue'
	import MyStarActivity from './MyStarActivity/MyStarActivity.vue'

	import { pageMyPublishTopic } from '@/api/topic.js'
	import { pageMyComment } from '@/api/comment.js'
	import { pageMyStar } from '@/api/star.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'MyActivity',
		components: {
			MyHeader,
			MyTopicActivity,
			MyCommentActivity,
			MyStarActivity
		},
		data() {
			return {
				tabIndex: 0,
				tabs: ['我的话题', '我的评论', '我的收藏'],
			}
		}
	}
	// 	activityType: ['topic', 'comment', 'star'],
	// 	activityLabel: ['分享了', '评论了', '收藏了'],
	// 	pageNo: [1, 1, 1],
	// 	topicPageNo: 1,
	// 	totalCount: [10, 10, 10],
	// 	t: 10,
	// 	topics: [{
	// 			id: 0,
	// 			title: '随便起个名字',
	// 			createTime: '2022-2-22 22:22:22',
	// 			starCount: 888,
	// 			firstImageUrl: '',
	// 		},
	// 		{
	// 			title: '随便起个名字',
	// 			createTime: '2022-2-22 22:22:22',
	// 			starCount: 888,
	// 			firstImageUrl: '',
	// 		}
	// 	],
	// 	comments: [{
	// 		id: 1,
	// 		content: '这是瞎写的评论',
	// 		createTime: '2020-2-22 22:22:22',
	// 		topic: {
	// 			title: '评论标题',
	// 			createTime: '2021-2-22 22:22:22',
	// 			starCount: 888,
	// 			firstImageUrl: '',
	// 		}
	// 	}],
	// 	stars: [{
	// 			topicId: 2,
	// 			createTime: '2022-2-22 22:22:22',
	// 			topic: {
	// 				title: '评论标题',
	// 				createTime: '2022-2-22 22:22:22',
	// 				starCount: 888,
	// 				firstImageUrl: '',
	// 			}
	// 		}

	// 	]
	// }
	// mounted() {
	// 	this.getTopicPage(1);
	// 	this.getCommentPage(1);
	// 	this.getStarPage(1);
	// },
	// computed: {
	// 	activities() {
	// 		let activitiesInfo = [];
	// 		if (this.activityType[this.tabIndex] == 'topic') {
	// 			for (let item of this.topics) {
	// 				let activity = item;
	// 				activity['time'] = item.createTime;
	// 				activitiesInfo.push(activity);
	// 			}
	// 		} else if (this.activityType[this.tabIndex] == 'comment') {
	// 			for (let item of this.comments) {
	// 				let activity = item.topic;
	// 				activity['content'] = item.content;
	// 				activity['time'] = item.createTime;
	// 				activity['id'] = item.id;
	// 				activitiesInfo.push(activity);
	// 			}
	// 		} else if (this.activityType[this.tabIndex] == 'star') {
	// 			for (let item of this.stars) {
	// 				let activity = item.topic;

	// 				activity['time'] = item.createTime;
	// 				activity['id'] = item.topicId;
	// 				console.log(activity['id'])
	// 				activitiesInfo.push(activity);
	// 			}
	// 		}
	// 		return activitiesInfo;
	// 	}
	// },
	// 	methods: {
	// 		getTopicPage(pageNo) {
	// 			let _this = this;
	// 			pageMyPublishTopic(pageNo).then(result => {
	// 				if (result.code == Code.OK) {
	// 					_this.topics = result.data.dataList;
	// 					_this.totalCount.splice(0, 1, result.data.totalCount);
	// 				}
	// 			})
	// 		},
	// 		getCommentPage(pageNo) {
	// 			let _this = this;
	// 			pageMyComment(pageNo).then(result => {
	// 				if (result.code == Code.OK) {
	// 					_this.comments = result.data.dataList;
	// 					_this.totalCount.splice(1, 1, result.data.totalCount);
	// 				}
	// 			})
	// 		},
	// 		getStarPage(pageNo) {
	// 			let _this = this;
	// 			pageMyStar(pageNo).then(result => {
	// 				if (result.code == Code.OK) {
	// 					_this.stars = result.data.dataList;
	// 					_this.totalCount.splice(2, 1, result.data.totalCount);
	// 				}
	// 			})
	// 		},
	// 		//更新对应页
	// 		onPageNoChange() {
	// 			let type = this.activityType[this.tabIndex];
	// 			let pageNo = this.pageNo[this.tabIndex];
	// 			if (type == 'topic') {
	// 				this.getTopicPage(pageNo);
	// 			} else if (type == 'comment') {
	// 				this.getCommentPage(pageNo);
	// 			} else if (type == 'star') {
	// 				this.getStarPage(pageNo);
	// 			}
	// 		},
	// 	}
	// }
</script>

<style scoped src="./MyActivity.css" >
</style>
