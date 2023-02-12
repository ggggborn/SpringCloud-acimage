<template>
	<div class="home-wrapper">
		<my-header></my-header>
		<div class="home-main">
			<div style="display: inline-block;width:598px">
				<home-carousel></home-carousel>
			</div>

			<div class="mask-images-container">
				<div v-for="topic in recommendTopics" :key="topic.id">
					<router-link :key="topic.id" :to="$global.getTopicUrl(topic.id)" :underline="false">
						<mask-image :image-url="$global.trueImageUrl(topic.coverImageUrl)"
							:title="labelWithTitle(topic.categoryId,topic.title)" :star-count="topic.starCount"
							:page-view="topic.pageView" :commentCount="topic.commentCount"
							:username="topic.user.username">
						</mask-image>
					</router-link>
				</div>
			</div>

			<base-container title="美图分享" icon="el-icon-picture" style="margin-top:-40px;">
				<el-skeleton v-if="loading" :rows="8" animated />
				<template v-else>
					<router-link v-for="topic in pictureTopics" :key="topic.id" :to="$global.getTopicUrl(topic.id)">
						<div style="display: inline-block;margin-left: 18px;margin-right:18px;width: 190px;">
							<float-image :title="topic.title" :star="topic.starCount" :page-view="topic.pageView"
								:username="topic.user.username" :create-time="topic.createTime"
								:photo-url="$global.truePhotoUrl(topic.user.photoUrl)" :image-url="topic.coverImageUrl">
							</float-image>
						</div>
					</router-link>
				</template>
			</base-container>

			<base-container title="近期热门">
				<el-skeleton v-if="loading" :rows="8" animated />
				<template v-else>
					<router-link v-for="topic in recentHotTopics" :key="topic.id" :to="$global.getTopicUrl(topic.id)">
						<div style="display: inline-block;margin-left: 18px;margin-right:18px;width: 190px;">
							<float-image :title="topic.title" :star="topic.starCount" :page-view="topic.pageView"
								:username="topic.user.username" :create-time="topic.createTime"
								:photo-url="$global.truePhotoUrl(topic.user.photoUrl)" :image-url="topic.coverImageUrl">
							</float-image>
						</div>
					</router-link>
				</template>
			</base-container>
		</div>
	</div>
</template>

<script>
	//组件
	import HomeCarousel from '@/components/HomeCarousel/HomeCarousel.vue'
	// import UserRank from './UserRank/UserRank.vue'
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import MaskImage from '@/components/MaskImage/MaskImage.vue'
	import FloatImage from '@/components/FloatImage/FloatImage.vue'
	import BaseContainer from '@/components/BaseContainer/BaseContainer.vue'


	import {
		queryRecentHotTopics,
		queryRecommendTopics,
		pageByCategoryId
	} from '@/api/topic.js'
	import { pageRencentTopic } from '@/api/topic.js'
	import axios from 'axios'

	import { Code } from '@/utils/result.js'

	export default {
		name: 'HomeA',
		components: {
			MyHeader,
			HomeCarousel,
			MaskImage,
			FloatImage,
			BaseContainer
		},
		data() {
			return {
				loading: true,
				query:{
					pageNo:1,
					pageSize:10,
					categoryId:null
				},
				//美图分享话题
				pictureTopics:[],
				recentHotTopics: [
					// 	{
					// 	id: 0,
					// 	title: '加载中...',
					// 	starCount: 888,
					// 	pageView: 88888,
					// 	createTime: '2022-2-22 22:22:22',
					// 	user: {
					// 		username: '加载中...',
					// 		photoUrl: ''
					// 	},
					// 	coverImageUrl: '',
					// }, 
				],
				recommendTopics: [
					// 	{
					// 	id: 0,
					// 	title: '加载中...',
					// 	starCount: 888,
					// 	pageView: 88888,
					// 	createTime: '2022-2-22 22:22:22',
					// 	user: {
					// 		username: '加载中...',
					// 		photoUrl: ''
					// 	},
					// 	coverImageUrl: '',
					// },
				]
			};

		},
		computed: {
			labelWithTitle() {
				return (categoryId, title) => {
					return '【' + this.$store.getters.categoryLabel(categoryId) + '】' + title;
				};
			}
		},
		mounted() {
			let _this = this;
			//美图分享id
			this.query.categoryId=4;
			pageByCategoryId(this.query).then(res=>{
				if (res.code == Code.OK) {
					_this.pictureTopics = res.data.dataList;
					_this.loading = false;
				}
			})
			pageRencentTopic(1).then(result => {
				if (result.code == Code.OK) {
					_this.recentHotTopics = result.data.dataList;
					_this.loading = false;
				}
				console.log(result)
			});
			queryRecommendTopics().then(result => {
				if (result.code == Code.OK) {
					_this.recommendTopics = result.data;
					_this.loading = false;
				}
			});
		},
	}
</script>

<style src="./Home.css" scoped>
</style>
