<template>
	<div class="carousel-container">
		<!-- 图片走马灯 -->
		<el-carousel trigger="click" height="310px" style="border-radius: 5px;" @change="onCarouselChange">
			<el-carousel-item v-for="image in images" :key="image.id">
				<el-image :src="$global.trueImageUrl(image.url)" fit="cover"
					style="width:500px;height: 310px;border-radius:5px;">
				</el-image>
			</el-carousel-item>
		</el-carousel>
		<!-- 图片走马灯end -->
		<div class="carousel-description">
			<span style="margin-left:10px;">{{currentDescription}}</span>
		</div>
	</div>
</template>

<script>
	import { queryHomeCarousel } from '@/api/HomeCarousel.js'
	import { Code } from '@/utils/result.js'

	export default {
		name: 'HomeCarousel',
		data() {
			return {
				index: 0,
				images: [
					// {
					// 	id: 2,
					// 	url: 'static/image/2.jpeg',
					// 	description: '欢迎来到......'
					// },
				]
			}
		},
		computed:{
			currentDescription(){
				if(this.images.length==0){
					return '';
				}
				return this.$global.omitStr(this.images[this.index].description,20);
			}
		},
		mounted() {
			let _this = this;
			queryHomeCarousel().then(result => {
				if (result.code == Code.OK) {
					console.log(result)
					_this.images = result.data;
					_this.index = 0;
				}
			})
		},
		methods: {
			onCarouselChange() {
				this.index = (this.index + 1) % this.images.length;
			}
		}
	}
</script>

<style scoped>
	.carousel-container {
		width: 500px;
		position: relative;
	}

	.carousel-description {
		display: inline-block;
		position: relative;
		width: 100%;
		height: 60px;
		border-radius: 5px;
		top: -60px;
		z-index: 11;
		color: white;
		font-size: 20px;
		background-image: linear-gradient(to top, rgba(0, 0, 0, 0.7), rgba(99, 99, 99, 0));
	}
	
</style>
