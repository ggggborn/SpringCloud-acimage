<template>
	<div id="app">
		<my-header></my-header>
		<div class="search-main">
			<div class="search-header">
				<i class="el-icon-search search-header-icon" style=""></i>
				<div class="search-header-title">以图搜图</div>
				<div style="margin-top:-20px;">
					<el-divider direction="horizontal"></el-divider>
				</div>
			</div>
			<div style="text-align: center;">
				<div class="upload-container">
					<el-upload action="https://jsonplaceholder.typicode.com/posts/" ref="upload"
						list-type="picture-card" :class="{'upload-plus-disabled':imageList.length>=1}"
						:on-preview="handlePictureCardPreview" :on-remove="handleRemove" :limit="1" :multiple="true"
						:auto-upload="false" accept="image/*" :on-exceed="exceedLimit" :on-change="handleChange">
						<i class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="dialogImageUrl" alt="">
					</el-dialog>
				</div>
				<div class="upload-hint">
					*选择你要识别的图片（10MB以内）
				</div>
				<el-button @click="submitImage" type="primary">提交</el-button>
			</div>

			<template v-if="!haveSearched">
				<el-empty description="快来搜索吧"></el-empty>
			</template>
			<template v-else-if="resultImages==null||resultImages.length==0">
				<el-empty description="没有找到相似图片"></el-empty>
			</template>
			<template v-else>
				<div class="search-result-container">
					<template v-for="image in resultImages">
						<router-link class="no-underline" :to="$global.getTopicUrl(image.topic.id)" :key="image.id">
							<float-image :title="image.topic.title" :star="image.topic.starCount"
								:page-view="image.topic.pageView" :username="image.topic.user.username"
								:create-time="image.topic.createTime"
								:photo-url="$global.truePhotoUrl(image.topic.user.photoUrl)"
								:image-url="$global.trueImageUrl(image.url)">
							</float-image>
						</router-link>
					</template>
				</div>
			</template>
		</div>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import FloatImage from '@/components/FloatImage/FloatImage.vue'

	import { searchImagesByImage } from '@/api/image.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'SearchImage',
		components: {
			MyHeader,
			FloatImage
		},
		data() {
			return {
				dialogImageUrl: '',
				dialogVisible: false,
				imageList: [],

				isShowResult: false,
				haveSearched: false,
				hint: '快来搜索吧',
				resultImages: [{
					id: 0,
					description: '快来搜索吧',
					url: '',
					topic: {
						id: 404,
						title: '快来搜索吧',
						createTime: '2022-2-22 22:22:22',
						starCount: 888,
						pageView: 888,
						user: {
							username: '快来搜索吧',
							photoUrl: ''
						}
					}
				}]
			};
		},
		mounted() {

		},
		methods: {
			handleRemove(file, fileList) {
				console.log(file, fileList);
			},
			handleChange(file, fileList) {
				this.imageList = fileList;
				console.log(this.imageList);
			},
			handlePictureCardPreview(file) {
				this.dialogImageUrl = file.url;
				this.dialogVisible = true;
			},
			exceedLimit() {
				MessageUtils.notice('只能选择1张图');
			},
			submitImage() {
				if (this.imageList.length == 0) {
					MessageUtils.notice("请选择一张图片");
					return;
				}
				let reqData = new FormData();
				let image = this.imageList[0];
				reqData.append("imageFile", image.raw);

				let _this = this;
				searchImagesByImage(reqData).then(result => {
					if (result.code == Code.OK) {
						_this.resultImages = result.data;
						_this.haveSearched = true;
						MessageUtils.success("搜索成功");
					}
				})
				// $.ajax({
				// 	url: "/api/files/images/searchByImage",
				// 	type: "post", //请求方式
				// 	data: reqData, //请求数据
				// 	processData: false, //是否将请求数据转换为对象
				// 	contentType: false, //发送数据到服务器时所使用的内容类型，false表示不需要ajax处理
				// 	dataType: 'json',
				// 	success: function(result) {
				// 		if (result.code == Code.OK) {
				// 			_this.resultImages = result.data;
				// 			_this.popupSuccess("搜索成功");
				// 		} else {
				// 			_this.popupHint(result.msg);
				// 		}
				// 	},
				// 	error: function() {
				// 		_this.popupHint("网络异常，提交失败");
				// 	}
				// });
			}

		}
	}
</script>

<style src="./SearchImage.css" scoped>
</style>
