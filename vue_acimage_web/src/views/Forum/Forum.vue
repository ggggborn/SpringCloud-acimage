<template>
	<div class="forum-div">
		<my-header></my-header>
		<div class="wrapper">
			<div class="wrapper-header">
				<div style="display: inline-block;width:560px">
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
				<!-- 				<img src="/static/image/user-rank-header.jpg" width="60%" height="80%" object-fit="cover"/>
				<i class="el-icon-chat-round wrapper-header-icon" style=""></i>
				<div class="wrapper-header-title">AC论坛</div> -->
			</div>
			<div class="wrapper-left">
<!-- 				<div class="hot-container" style="height: 600px;">
					<div class="hot-header">
						<i class="el-icon-discover hot-header-icon"></i>
						<div class="hot-header-title">近期热门</div>
						<div style="margin-top:-20px;">
							<el-divider direction="horizontal"></el-divider>
						</div>
					</div>
					<template v-for="topic in recentHotTopics">
						<router-link :key="topic.id" :to="$global.getTopicUrl(topic.id)" class="no-underline">
							<div style="display: inline-block;margin-right: 10px;width: 150px;">
								<float-image :title="topic.title" :star="topic.starCount" :page-view="topic.pageView"
									:username="topic.user.username" :create-time="topic.createTime"
									:photo-url="$global.truePhotoUrl(topic.user.photoUrl)"
									:image-url="$global.trueImageUrl(topic.coverImageUrl)">
								</float-image>
							</div>
						</router-link>
					</template>
				</div> -->

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
				<div class="mt10">
					<topic-list medalMode label="火热讨论"></topic-list>
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
	import HomeCarousel from '@/views/Home/HomeCarousel/HomeCarousel.vue'
	import MaskImage from '@/components/MaskImage/MaskImage.vue'
	// import FloatImage from '@/components/FloatImage/FloatImage.vue'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	import { pageRencentTopic } from '@/api/topic.js'
	import { queryRecentHotTopics, queryRecommendTopics } from '@/api/topic.js'

	export default {
		name: 'ForumA',
		components: {
			MyHeader,
			TopicCard,
			CategoryCard,
			TagCard,
			TopicList,

			HomeCarousel,
			MaskImage,
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
