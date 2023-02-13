<template>
	<div class="home-wrapper">
		<my-header></my-header>
		<div class="home-main">
			<div style="display: inline-block;width:598px;">
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
						<div class="float-image-container">
							<float-image :title="topic.title" :star="topic.starCount" :page-view="topic.pageView"
								:username="topic.user.username" :create-time="topic.createTime"
								:photo-url="$global.getPhotoUrl(topic.user.photoUrl)" :image-url="topic.coverImageUrl">
							</float-image>
						</div>
					</router-link>
				</template>
			</base-container>
			
			<base-container title="番剧茶馆" icon="el-icon-milk-tea" iconColor="#FFA131" style="margin-top: 20px;">
				<el-skeleton v-if="loading" :rows="8" animated />
				<template v-else>
					<router-link v-for="topic in animationTopis" :key="topic.id" :to="$global.getTopicUrl(topic.id)">
						<div class="float-image-container">
							<float-image :title="topic.title" :star="topic.starCount" :page-view="topic.pageView"
								:username="topic.user.username" :create-time="topic.createTime"
								:photo-url="$global.getPhotoUrl(topic.user.photoUrl)" :image-url="topic.coverImageUrl">
							</float-image>
						</div>
					</router-link>
				</template>
			</base-container>

			<base-container title="最新发表" style="margin-top: 20px;">
				<el-skeleton v-if="loading" :rows="8" animated />
				<template v-else>
					<router-link v-for="topic in latestPublicTopics" :key="topic.id" :to="$global.getTopicUrl(topic.id)">
						<div class="float-image-container">
							<float-image :title="topic.title" :star="topic.starCount" :page-view="topic.pageView"
								:username="topic.user.username" :create-time="topic.createTime"
								:photo-url="$global.getPhotoUrl(topic.user.photoUrl)" :image-url="topic.coverImageUrl">
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
		pageByCategoryId,
		pageBySort
	} from '@/api/topic.js'
	import { pageActiveTopics } from '@/api/topic.js'
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
				queryCategory: {
					pageNo: 1,
					pageSize: 10,
					categoryId: null
				},
				//美图分享话题
				pictureTopics: [],
				//番剧茶馆话题
				animationTopis:[],
				//最新发表话题
				latestPublicTopics:[],
				recommendTopics: []
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
			this.queryCategory.categoryId = 4;
			pageByCategoryId(this.queryCategory).then(res => {
				if (res.code == Code.OK) {
					_this.pictureTopics = res.data.dataList;
					_this.loading = false;
				}
			})
			//番剧茶馆id
			this.queryCategory.categoryId = 1;
			pageByCategoryId(this.queryCategory).then(res => {
				if (res.code == Code.OK) {
					_this.animationTopis = res.data.dataList;
					_this.loading = false;
				}
			})
			
			pageBySort({pageNo:1,pageSize:10,sortMode:'CREATE_TIME'}).then(res=>{
				if(res.code==Code.OK){
					_this.latestPublicTopics=res.data.dataList
				}
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
