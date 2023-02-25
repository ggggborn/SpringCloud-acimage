<template>
	<div class="outer-div">
		<my-header></my-header>
		<div class="wrapper">
			<div class="wrapper-left">
				<div>
					我的消息
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
				<comment-message></comment-message>
			</div>
		</div>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import CommentMessage from './CommentMessage/CommentMessage.vue'

	import { pageMyPublishTopic } from '@/api/topic.js'
	import { pageMyComment } from '@/api/comment.js'
	import { pageMyStar } from '@/api/star.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'MyMessage',
		components: {
			MyHeader,
			CommentMessage,
		},
		data() {
			return {
				tabIndex: 0,
				tabs: ['评论我的'],
			}
		}
	}

</script>

<style scoped src="./MyMessage.css">
</style>
