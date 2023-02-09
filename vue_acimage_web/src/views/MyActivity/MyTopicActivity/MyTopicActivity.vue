<template>
	<div class="activity-content">
		<template v-for="(topic,index) in topics">
			<el-divider v-if="index>0" :key="topic.id"></el-divider>
			<div class="activity-content-item" :key="topic.id">
				<el-avatar :src="$store.getters.truePhotoUrl" :size="60"></el-avatar>
				<div class="activity-medium">
					<div>
						<span class="activity-title-label">分享了</span>
						<router-link :to="$global.getTopicUrl(topic.id)"
							class="no-underline router-link-item activity-title">
							{{$global.omitStr(topic.title,20)}}
						</router-link>
						<br />
						<div class="topic-data">
							<i class="el-icon-star-off">{{topic.starCount}}</i>
							<el-divider direction="vertical"></el-divider>
							<i class="el-icon-chat-dot-square">{{topic.commentCount}}</i>
							<el-divider direction="vertical"></el-divider>
							<i class="el-icon-view">{{topic.pageView}}</i>
						</div>

					</div>
					<div class="activity-time">
						{{topic.createTime}}
					</div>
				</div>
				<div class="activity-right">
					<el-image :src="topic.coverImageUrl" fit="cover"
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
	import { pageMyPublishTopic } from '@/api/topic.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'MyTopicActivity',
		data() {
			return {
				pageNo: 1,
				totalCount: 2,
				topics: [{
						id: 0,
						title: '随便起个名字',
						createTime: '2022-2-22 22:22:22',
						starCount: 888,
						commentCount: 888,
						pageView: 888,
						firstImageUrl: '',
					},
					{
						title: '随便起个名字',
						createTime: '2022-2-22 22:22:22',
						starCount: 888,
						commentCount: 888,
						pageView: 888,
						firstImageUrl: '',
					}
				]
			}
		},
		mounted() {
			this.getTopicPage(1);
		},
		methods: {
			getTopicPage(pageNo) {
				let _this = this;
				pageMyPublishTopic(pageNo).then(result => {
					if (result.code == Code.OK) {
						_this.topics = result.data.dataList;
						_this.totalCount = result.data.totalCount;
					}
				})
			},
			//更新对应页
			onPageNoChange() {
				this.getTopicPage(this.pageNo);
			},
		}
	}
</script>

<style src="./MyTopicActivity.css" scoped>
</style>
