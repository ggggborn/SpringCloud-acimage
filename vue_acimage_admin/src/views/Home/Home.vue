<template>
	<div class="home-wrapper">
		<my-header></my-header>
		<div class="home-main">
			<div style="display: inline-block;">
				<home-carousel v-loading="false"></home-carousel>
			</div>

			<div class="mask-images-container">
				<template v-for="topic in recommendTopics">
					<router-link :key="topic.id" :to="$global.getTopicUrl(topic.id)" :underline="false">
						<mask-image :image-url="$global.trueImageUrl(topic.firstImageUrl)" :title="topic.title"
							:star-count="topic.starCount" :page-view="topic.pageView" :username="topic.user.username">
						</mask-image>
					</router-link>
				</template>
			</div>

			<div class="hot-container">
				<div class="hot-header">
					<i class="el-icon-discover hot-header-icon"></i>
					<div class="hot-header-title">近期热门</div>
					<div style="margin-top:-20px;">
						<el-divider direction="horizontal"></el-divider>
					</div>
				</div>

				<template v-for="topic in recentHotTopics">
					<router-link :key="topic.id" :to="$global.getTopicUrl(topic.id)" class="no-underline">
						<float-image :title="topic.title" :star="topic.starCount" :page-view="topic.pageView"
							:username="topic.user.username" :create-time="topic.createTime"
							:photo-url="$global.truePhotoUrl(topic.user.photoUrl)"
							:image-url="$global.trueImageUrl(topic.firstImageUrl)">
						</float-image>
					</router-link>
				</template>
			</div>

			<div class="user-rank-container">
				<user-rank></user-rank>
			</div>
		</div>
	</div>
</template>

<script>
	//组件
	import HomeCarousel from './HomeCarousel/HomeCarousel.vue'
	import UserRank from './UserRank/UserRank.vue'
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import MaskImage from '@/components/MaskImage/MaskImage.vue'
	import FloatImage from '@/components/FloatImage/FloatImage.vue'


	import { queryRecentHotTopics, queryRecommendTopics } from '@/api/topic.js'
	import axios from 'axios'

	import { Code } from '@/utils/result.js'

	export default {
		name: 'HomeA',
		components: {
			MyHeader,
			HomeCarousel,
			MaskImage,
			FloatImage,
			UserRank
		},
		data() {
			return {
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
				// 	firstImageUrl: '',
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
					// 	firstImageUrl: '',
					// },
				]
			};

		},
		mounted() {
			let _this = this;
			console.log(process.env.VUE_APP_MOCK);
			queryRecentHotTopics().then(result => {
				if (result.code == Code.OK) {
					_this.recentHotTopics = result.data;
				}
				console.log(result)
			});
			queryRecommendTopics().then(result => {
				if (result.code == Code.OK) {
					_this.recommendTopics = result.data;
				}
			});



		},
		computed: {

		}

	}
</script>

<style src="./Home.css" scoped>
</style>
