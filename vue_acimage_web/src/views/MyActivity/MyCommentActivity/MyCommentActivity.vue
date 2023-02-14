<template>
	<div class="activity-content">
		<template v-for="(comment,index) in comments">
			<el-divider v-if="index>0" :key="comment.topic.id"></el-divider>
			<div class="activity-content-item" :key="comment.id">
				<el-avatar :src="$store.getters.getPhotoUrl" :size="60"></el-avatar>
				<div class="activity-medium">
					<div>
						<span class="activity-title-label">评论了</span>
						<router-link :to="$global.getTopicUrl(comment.topic.id)"
							class="no-underline router-link-item activity-title">
							{{$global.omitStr(comment.topic.title,20)}}
						</router-link>
						<br />
					</div>
					<span style="color: #666666;">{{comment.content}}</span>
					
					<div class="activity-time">
						{{comment.createTime}}
					</div>
				</div>
				<div class="activity-right">
					<el-image :src="comment.topic.coverImageUrl" fit="cover"
						style="height:70px;border-radius: 3px">
					</el-image>

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
	import { pageMyComments } from '@/api/comment.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'MyTopicActivity',
		data() {
			return {
				pageNo: 1,
				totalCount: 2,
				comments: [],
			}
		},
		mounted() {
			this.getCommentPage(1);
		},
		methods: {
			getCommentPage(pageNo) {
				let _this = this;
				pageMyComments(pageNo).then(result => {
					if (result.code == Code.OK) {
						_this.comments = result.data.dataList;
						_this.totalCount=result.data.totalCount;
					}
				})
			},
			//更新对应页
			onPageNoChange() {
				this.getCommentPage(this.pageNo);
			},
		}
	}
</script>

<style src="./MyCommentActivity.css" scoped>
</style>
