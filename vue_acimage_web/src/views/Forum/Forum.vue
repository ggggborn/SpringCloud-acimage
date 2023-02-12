<template>
	<div class="forum-div">
		<my-header></my-header>
		<div class="wrapper">
			<div class="wrapper-header">
				<div style="display: inline-block;width:598px">
					<home-carousel></home-carousel>
				</div>
				<div class="mask-images-container">
					<div v-for="topic in recommendTopics" :key="topic.id">
						<router-link :key="topic.id" :to="$global.getTopicUrl(topic.id)" :underline="false">
							<mask-image :image-url="$global.trueImageUrl(topic.coverImageUrl)" :title="topic.title"
								:star-count="topic.starCount" :page-view="topic.pageView"
								:commentCount="topic.commentCount" :username="topic.user.username">
							</mask-image>
						</router-link>
					</div>
				</div>
			</div>
			<div class="wrapper-left">
				<el-skeleton v-if="loading" :rows="6" animated />
				<template v-else>
					<div v-for="topic in topics" style="margin-bottom:12px;" :key="topic.id">
						<topic-card :title="topic.title" :html="topic.content" :updateTime="topic.activityTime"
							:username="topic.user.username" :starCount="topic.starCount"
							:commentCount="topic.commentCount" :pageView="topic.pageView"
							:photoUrl="$global.truePhotoUrl(topic.user.photoUrl)" :to="$global.getTopicUrl(topic.id)"
							:categoryId="topic.categoryId" :tagIds="topic.tagIds" :coverImageUrl="topic.coverImageUrl">
						</topic-card>
					</div>
					<div style="text-align: center;">
						<el-pagination background layout="prev, pager, next" :total="totalCount"
							:current-page.sync="curPage" @current-change="handlePageNoChange" :page-size="10">
						</el-pagination>
					</div>
				</template>
			</div>
			<div class="wrapper-right">
				<category-card :click-category="clickCategory"></category-card>
				<div class="mt10">
					<tag-card :click-tag="clickTag"></tag-card>
				</div>
				<div style="margin-top:20px;">
					<topic-rank :labels="['火热评论','抢先收藏','引人注目']" :topics="mostCommentCountTopics" :showData="false"></topic-rank>
				</div>
<!-- 				<div class="mt10">
					<topic-list medalMode label="火热讨论" :topics="mostCommentCountTopics" :showData="false"></topic-list>
				</div> -->
				<div style="margin-top:20px">
					<user-rank></user-rank>
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
	import TopicList from '@/components/TopicList/TopicList.vue'
	import TopicRank from '@/components/TopicRank/TopicRank.vue'
	import HomeCarousel from '@/components/HomeCarousel/HomeCarousel.vue'
	import MaskImage from '@/components/MaskImage/MaskImage.vue'
	import UserRank from '@/views/Home/UserRank/UserRank.vue'
	// import FloatImage from '@/components/FloatImage/FloatImage.vue'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	import { pageRencentTopic } from '@/api/topic.js'
	import { queryRecentHotTopics, queryRecommendTopics, queryMostCommentCountTopics } from '@/api/topic.js'

	export default {
		name: 'ForumA',
		components: {
			MyHeader,
			TopicCard,
			CategoryCard,
			TagCard,
			// TopicList,
			TopicRank,

			HomeCarousel,
			MaskImage,
			UserRank
			// FloatImage
		},
		data() {
			return {
				loading: true,
				totalCount: 100,
				
				recommendTopics: [

				],
				recentHotTopics: [

				],
				mostCommentCountTopics:[],
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
					categoryId: 1,
					tagIds: [],
					user: {
						photoUrl: ''
					}
				}],
				curPage: 1,
				query: {
					tagId: null,
					categoryId: null,
					pageNo: 1,
					pageSize: 1
				}
			};
		},
		watch: {
			query: {
				handler(newval, oldval) {
					console.log('新+' + newval);
					console.log('旧+' + oldval);
				},
				deep: true
			}
		},
		mounted() {
			// console.log(this.$router.query)
			this.getRecentTopicPage();
			let _this = this;
			queryRecommendTopics().then(result => {
				if (result.code == Code.OK) {
					_this.recommendTopics = result.data;
				}
			});
			queryRecentHotTopics().then(result => {
				if (result.code == Code.OK) {
					_this.recentHotTopics = result.data;
				}
			});
			queryMostCommentCountTopics().then(res=>{
				if (res.code == Code.OK) {
					_this.mostCommentCountTopics = res.data;
				}
			})

		},
		methods: {
			clickCategory(categoryId) {
				// alert("父组件收到" + categoryId);
			},
			clickTag(tagId) {
				alert("父组件收到" + tagId);
				this.query.tagId = tagId;
			},
			handlePageNoChange() {
				this.query.pageNo = this.curPage;
				this.getRecentTopicPage();
				console.log(this.query)
			},
			getRecentTopicPage() {
				let _this = this;
				pageRencentTopic(_this.query.pageNo)
					.then(result => {
						if (result.code == Code.OK) {
							_this.topics = result.data.dataList;
							_this.totalCount = result.data.totalCount;
							_this.loading = false;
						}
					})
			}
		}
	}
</script>

<style src="./Forum.css" scoped>
</style>
