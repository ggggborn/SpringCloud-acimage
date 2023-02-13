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
							:photoUrl="$global.getPhotoUrl(topic.user.photoUrl)" :to="$global.getTopicUrl(topic.id)"
							:categoryId="topic.categoryId" :tagIds="topic.tagIds" :coverImageUrl="topic.coverImageUrl"
							timeLabel="话题活跃于">
						</topic-card>
					</div>
					<div style="text-align: center;">
						<el-pagination v-if="topics.length>0" background layout="prev, pager, next" :total="totalCount"
							:current-page.sync="curPage" @current-change="handlePageNoChange"
							:page-size="query.pageSize">
						</el-pagination>
						<el-empty v-else image="static/image/sad.jpeg" description="没有你想要的结果 ┑(￣Д ￣)┍"></el-empty>
					</div>
				</template>
			</div>
			<div class="wrapper-right">
				<category-card :click-category="clickCategory"></category-card>
				<div class="mt10">
					<tag-card :click-tag="clickTag"></tag-card>
				</div>
				<div style="margin-top:20px;">
					<topic-rank :labels="['火热评论','抢先收藏','引人注目']" :topics="topicsInTab" :showData="false"
					:handleTabHover="handleTabHover">
					</topic-rank>
				</div>
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

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	import { pageActiveTopics } from '@/api/topic.js'
	import {
		queryRecentHotTopics,
		queryRecommendTopics,
		queryMostCommentCountTopics,
		get3HotTopicLists,
		pageByCategoryId,
		pageByTagId,
		pageBySort
	} from '@/api/topic.js'

	export default {
		name: 'ForumA',
		components: {
			MyHeader,
			TopicCard,
			CategoryCard,
			TagCard,
			TopicRank,

			HomeCarousel,
			MaskImage,
			UserRank
		},
		data() {
			return {
				loading: true,
				totalCount: 100,
				recommendTopics: [],
				recentHotTopics: [],
				topicsInTab: [],
				hotTopicLists:[[],[],[]],
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
				tabIndex:0,
				curPage: 1,
				query: {
					tagId: null,
					categoryId: null,
					pageNo: 1,
					pageSize: 12,
					sortMode: 'ACTIVITY_TIME'
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
			this.getTopics();
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
			get3HotTopicLists().then(res=>{
				if (res.code == Code.OK) {
					_this.hotTopicLists = res.data;
					this.topicsInTab=this.hotTopicLists[this.tabIndex];
				}
			});
			queryMostCommentCountTopics().then(res => {
				if (res.code == Code.OK) {
					_this.mostCommentCountTopics = res.data;
				}
			})

		},
		methods: {
			clickCategory(categoryId) {
				this.query.tagId = null;
				if (this.query.categoryId == null) {
					this.query.pageNo = 1;
					this.curPage = 1;
				}
				this.query.categoryId = categoryId;
				let _this = this;
				this.getTopics();
			},
			clickTag(tagId) {
				this.query.categoryId = null;
				if (this.query.tagId == null) {
					this.query.pageNo = 1;
					this.curPage = 1;
				}
				this.query.tagId = tagId;
				this.getTopics();
			},
			handlePageNoChange() {
				this.query.pageNo = this.curPage;
				this.getTopics();
			},
			getTopics() {
				let _this = this;
				if (this.query.categoryId != null) {
					//按分类查
					_this.loading = true;
					pageByCategoryId(this.query).then(res => {
						if (res.code == Code.OK) {
							_this.topics = res.data.dataList;
							_this.totalCount = res.data.totalCount;
							_this.loading = false;
						}
					})
				} else if (this.query.tagId != null) {
					//按标签查
					_this.loading = true;
					pageByTagId(this.query).then(res => {
						if (res.code == Code.OK) {
							_this.topics = res.data.dataList;
							_this.totalCount = res.data.totalCount;
							_this.loading = false;
						}
					})
				} else {
					_this.loading = true;
					// pageBySort(this.query).then(res => {
					// 	if (res.code == Code.OK) {
					// 		_this.topics = res.data.dataList;
					// 		_this.totalCount = res.data.totalCount;
					// 		_this.loading = false;
					// 	}
					// });
					pageActiveTopics(this.query.pageNo, this.query.pageSize)
						.then(result => {
							if (result.code == Code.OK) {
								_this.topics = result.data.dataList;
								_this.totalCount = result.data.totalCount;
								_this.loading = false;
							}
						})
				}

			},
			handleTabHover(tabIndex){
				this.topicsInTab=this.hotTopicLists[tabIndex];
			}
		}
	}
</script>

<style src="./Forum.css" scoped>
</style>
