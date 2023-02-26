<template>
	<div class="activity-content">
		<template v-for="(commentMsg,index) in commentMsgs">
			<el-divider v-if="index>0" :key="commentMsg.topicId"></el-divider>
			<div class="activity-content-item" :key="commentMsg.commentId">
				<!-- <el-avatar :src="$store.getters.getPhotoUrl" :size="60"></el-avatar> -->
				<div class="activity-medium">
					<div>
						{{commentMsg.user.username}}
						<span class="activity-title-label">评论了你的话题 </span>
						<router-link :to="$global.getTopicUrl(commentMsg.topicId)"
							class="no-underline router-link-item activity-title">
							{{$global.omitStr(commentMsg.topicTitle,20)}}
						</router-link>
						<br />
					</div>
					<span style="color: #666666;">{{commentMsg.content}}</span>

					<div class="activity-time">
						{{commentMsg.createTime}}
					</div>
				</div>
				<div class="activity-right">
					<!-- 					<el-image :src="comment.topic.coverImageUrl" fit="cover"
						style="height:70px;border-radius: 3px">
					</el-image> -->
				</div>
			</div>
		</template>
		<!-- 分页 -->
		<div style="text-align: center;margin-top:20px;">
			<el-pagination background layout="prev, pager, next" :total="totalCount" :current-page.sync="pageNo"
				@current-change="onPageNoChange" :page-size="5">
			</el-pagination>
		</div>
	</div>
</template>

<script>
	import { pageCommentMessages } from '@/api/message.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'MyTopicActivity',
		data() {
			return {
				pageNo: 1,
				totalCount: 2,
				commentMsgs: [],
			}
		},
		mounted() {
			this.getCommentMsgPage(1);
		},
		methods: {
			getCommentMsgPage(pageNo) {
				let _this = this;
				pageCommentMessages(pageNo, 5).then(result => {
					if (result.code == Code.OK) {
						_this.commentMsgs = result.data.dataList;
						_this.totalCount = result.data.totalCount;
						if (_this.pageNo == 1) {
							_this.$store.state.messageNum = null;
						}
					}
				})
			},
			//更新对应页
			onPageNoChange() {
				this.getCommentMsgPage(this.pageNo);
			},
		}
	}
</script>

<style src="./CommentMessage.css" scoped>
</style>
