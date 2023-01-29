<template>
	<div class="forum-div">
		<my-header></my-header>
		<div class="wrapper">
			<div class="topics-header">
				<i class="el-icon-chat-round topics-header-icon" style=""></i>
				<div class="topics-header-title">AC论坛</div>
			</div>
			<div class="wrapper-left">
				<template v-if="isLoading" >
					<el-skeleton :rows="6" animated/>
				</template>
				<template v-else>
					<template v-for="topic in topics">
						<div style="margin-bottom:8px;" :key="topic.id">
							<topic-card :title="topic.title" :html="topic.content" :updateTime="topic.activityTime"
								:username="topic.user.username" :starCount="topic.starCount"
								:commentCount="topic.commentCount" :pageView="topic.pageView"
								:photoUrl="$global.truePhotoUrl(topic.user.photoUrl)"
								:to="$global.getTopicUrl(topic.id)"
								:categoryId="topic.categoryId"
								:tagIds="topic.tagIds"
								:coverImageUrl="topic.coverImageUrl">
							</topic-card>
						</div>
					</template>
					<div style="text-align: center;">
						<el-pagination background layout="prev, pager, next" :total="totalCount"
							:current-page.sync="pageNo" @current-change="getRecentTopicPage" :page-size="10">
						</el-pagination>
					</div>
				</template>


			</div>
			<div class="wrapper-right">
				<category-card :click-category="clickCategory"></category-card>
				<div class="mt10">
					<tag-card :click-tag="clickTag"></tag-card>
				</div>

			</div>

		</div>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import TopicCard from '@/components/TopicCard/TopicCard.vue'
	import CategoryCard from '@/components/CategoryCard/CategoryCard.vue'
	import TagCard from '@/components/TagCard/TagCard.vue'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	import { pageRencentTopic } from '@/api/topic.js'

	export default {
		name: 'ForumA',
		components: {
			MyHeader,
			TopicCard,
			CategoryCard,
			TagCard
		},
		data() {
			return {
				isLoading:true,
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
					coverImageUrl: '',
					categoryId:1,
					tagIds:[],
					user: {
						photoUrl: ''
					}
				}]
			};
		},
		computed: {

		},
		mounted() {
			this.getRecentTopicPage();
		},
		methods: {
			clickCategory(categoryId) {
				alert("父组件收到" + categoryId);
			},
			clickTag(tagId) {
				alert("父组件收到" + tagId);
			},
			getRecentTopicPage() {
				let _this = this;
				pageRencentTopic(_this.pageNo)
					.then(result => {
						if (result.code == Code.OK) {
							_this.topics = result.data.dataList;
							_this.totalCount = result.data.totalCount;
							_this.isLoading=false;
						}
					})
			}

		}
	}
</script>

<style src="./Forum.css" scoped>
</style>
