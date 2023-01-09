<template>
	<div>
		<my-header></my-header>
		<div class="forum-wrapper">
			<div class="forum-left-wrapper">
				<div class="topics-container">
					
					<div class="topics-header">
						<i class="el-icon-chat-round topics-header-icon" style=""></i>
						<div class="topics-header-title">AC论坛</div>
						<div style="margin-top:-20px;">
							<el-divider direction="horizontal"></el-divider>
						</div>
					</div>
					
					<template v-for="topic in topics">
						<div :key="topic.id">
							<div class="topic-item">
								<el-avatar :src="$global.truePhotoUrl(topic.user.photoUrl)" :size="60"></el-avatar>
								<div class="item-medium">
									<div>
										<div class="topic-title">
											<router-link :to="$global.getTopicUrl(topic.id)"
												class="no-underline router-link-item"
												style="font-size: 17px;margin-top:-5px;">
												{{$global.omitStr(topic.title,15)}}
											</router-link>
										</div>
										<div class="topic-data">
											更新于 {{topic.activityTime}}
											| <i class="el-icon-star-off"></i>{{topic.starCount}}
											| <i class="el-icon-view"> </i>{{topic.pageView}}
											| <i class="el-icon-chat-dot-round"> </i>{{topic.commentCount}}
										</div>
										<router-link :to="$global.getTopicUrl(topic.id)"
											class="no-underline router-link-item"
											style="font-size: 16px;margin-top:-5px;">
											<div class="topic-content">
												{{$global.html2Text(topic.content,80)}}
											</div>
										</router-link>
									</div>
								</div>
								<div class="item-right">
									<el-image :src="$global.trueImageUrl(topic.firstImageUrl)" fit="cover"
										style="height:90px;width:90px;border-radius: 3px">
									</el-image>
								</div>
							</div>
							<el-divider></el-divider>
						</div>
					</template>
					<div style="text-align: center;">
						<el-pagination background layout="prev, pager, next" :total="totalCount"
							:current-page.sync="pageNo" @current-change="getRecentTopicPage" :page-size="10">
						</el-pagination>
					</div>
				</div>
			</div>
<!-- 			<div class="forum-right-wrapper">
				<div class="user-rank-header">
					<i class="el-icon-office-building"></i>用户排行
				</div>
			</div> -->
		</div>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	import { pageRencentTopic } from '@/api/topic.js'

	export default {
		name: 'ForumA',
		components: {
			MyHeader
		},
		data() {
			return {
				pageNo: 1,
				totalCount: 100,
				topics: [{
						id: 405,
						userId: 999,
						title: "这是标题这是标题这是标题这是标题这是标题",
						content: `不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么
						不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么`,
						activityTime: '2022-2-22 2:22:22',
						starCount: 666,
						pageView: 777,
						commentCount: 888,
						firstImageUrl: '',
						images: [{
							id: 0
						}],
						user: {
							photoUrl: ''
						}
					},
					{
						id: 404,
						title: "这是标题这是标题这是标题这是标题这是标题",
						content: `不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道,
						写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么不知道写什么`,
						activityTime: '2022-2-22 2:22:22',
						starCount: 666,
						pageView: 777,
						commentCount: 888,
						firstImageUrl: '',
						images: [{
							id: 0
						}],
						user: {
							photoUrl: ''
						}
					},
				]
			};
		},
		computed:{
			
		},
		mounted() {
			this.getRecentTopicPage();
		},
		methods: {
			getRecentTopicPage() {
				let _this = this;
				pageRencentTopic(_this.pageNo)
					.then(result => {
						if (result.code == Code.OK) {
							_this.topics = result.data.dataList;
							_this.totalCount = result.data.totalCount;
						}
					})

			}

		}
	}
</script>

<style src="./Forum.css" scoped>
</style>
