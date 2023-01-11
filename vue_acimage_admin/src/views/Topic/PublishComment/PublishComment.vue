<template>
	<div class="user-comment-container">
		<div>
			<el-avatar :src="$store.state.photoUrl" :size="60"></el-avatar>
		</div>
		<div>
			<el-input type="textarea" :rows="3" style="width:400px;" maxlength="200" placeholder="快来发表友好的评论吧~"
				v-model="content">
			</el-input>
		</div>
		<div>
			<el-button type="primary" style="height:74px;" @click="submitComment">评论</el-button>
		</div>
	</div>
</template>

<script>
	import { addComment } from '@/api/comment.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils.js'
	import MessageUtils from '@/utils/MessageUtils.js'

	export default {
		name: 'PublishComment',
		data() {
			return {
				content: ''
			}
		},
		props: {
			topicId: {},
		},
		methods: {
			submitComment() {
				let data = {
					'topicId': this.topicId,
					'content': this.content
				};
				CommonUtils.popMsgAndRefreshIfOk(addComment(data), '发表成功', 2);
			}
		}
	}
</script>

<style src="./PublishComment.css" scoped>
</style>
