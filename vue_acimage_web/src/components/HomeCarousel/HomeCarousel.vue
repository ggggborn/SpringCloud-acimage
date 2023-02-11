<template>
	<div class="carousel-container" @click="toLink">
		<el-carousel trigger="click" height="350px" @change="onCarouselChange">
			<el-carousel-item v-for="image in images" :key="image.id" class="hover-pointer">
				<el-image :src="image.url" fit="cover">
				</el-image>
			</el-carousel-item>
		</el-carousel>
		<div class="carousel-description">
			<span style="margin-left:10px;">{{currentDescription}}</span>
		</div>
	</div>
</template>

<script>
	import { queryHomeCarousel } from '@/api/HomeCarousel.js'
	import CommonUtils from '@/utils/CommonUtils';
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
		computed: {
			currentDescription() {
				if (this.images.length == 0) {
					return '';
				}
				return this.$global.omitStr(this.images[this.index].description, 20);
			}
		},
		mounted() {
			let _this = this;
			queryHomeCarousel().then(result => {
				if (result.code == Code.OK) {
					_this.images = result.data;
					_this.index = 0;
				}
			})
		},
		methods: {
			onCarouselChange() {
				this.index = (this.index + 1) % this.images.length;
			},
			toLink() {
				let link = this.images[this.index].link;
				// window.open("/#/topic/1575129660344836096");
				if (!CommonUtils.isEmpty(link)) {
					window.location.href = link;
				}
			}
		}
	}
</script>

<style scoped>
	.carousel-container {
		width: 100%;
		position: relative;
	}

	.carousel-container>>>.el-carousel {
		border-radius: 5px;
		width: 100%;
	}

	.carousel-container>>>.el-image {
		width: 100%;
		height: 350px;
		border-radius: 5px;
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
