<template>
	<div id="app">
		<my-header></my-header>

		<div class="publish-main">
			<div class="publish-header">
				<i class="el-icon-s-promotion publish-header-icon" style=""></i>
				<div class="publish-header-title">快来分享吧</div>
				<div style="margin-top:-20px;">
					<el-divider direction="horizontal"></el-divider>
				</div>
			</div>
			<div style="text-align: center;">
				<el-row>
					<el-input placeholder="标题" v-model="title" maxlength="20" prefix-icon="el-icon-edit-outline"
						size="samll" style="width:250px;" clearable>
					</el-input>
				</el-row>
				<div class="upload-container">
					<el-upload action="#" ref="upload" list-type="picture-card"
						:class="{'upload-plus-disabled':imageList.length>=5}" :on-preview="handlePictureCardPreview"
						:on-remove="handleRemove" :limit="5" :multiple="true" :auto-upload="false" accept="image/*"
						:on-exceed="exceedLimit" :on-change="handleChange">
						<i class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="dialogImageUrl" alt="">
					</el-dialog>
				</div>
				<div class="upload-hint">
					需上传1-5张图片，单张图片大小上限10MB
				</div>
				<edit-board ref="editBoard" margin="160px" width="720px"></edit-board>
				<el-button @click="submitTopic" type="primary" style="margin-top: 20px;">确认分享</el-button>
			</div>

		</div>

	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import EditBoard from '@/components/EditBoard/EditBoard.vue'

	import { addTopic } from '@/api/topic.js'
	import { uploadTopicImages } from '@/api/image.js'

	import { Code } from '@/utils/result.js'
	import MessageUtils from '@/utils/MessageUtils'
	import StringUtils from '@/utils/StringUtils'

	export default {
		name: 'PublishTopic',
		components: {
			MyHeader,
			EditBoard,
		},
		data() {
			return {
				dialogImageUrl: '',
				dialogVisible: false,
				title: '',
				content: '',
				imageList: [],
			};
		},
		methods: {
			test() {
				alert(this.$refs['editBoard'].Html);
			},
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
				MessageUtils.notice('最多上传5张图');
			},
			validateForm() {
				if (this.imageList.length == 0) {
					MessageUtils.notice("请选择至少一张图片");
					return false;
				}

				// if (this.title.length < 4) {
				// 	MessageUtils.notice("标题字数4以上");
				// 	return false;
				// } else if (this.title.length > 30) {
				// 	MessageUtils.notice("标题字数30以内");
				// 	return false;
				// }

				let text = this.$refs['editBoard'].Html.toString();
				// text.replace(new RegExp("</?[^>]+>", "gm"), "");
				// // eslint-disable-next-line
				// text.replace(new RegExp("<a>\\s*|\t|\r|\n</a>", "gm"), "");
				let pureText = StringUtils.html2Text(text);
				console.log(pureText)
				if (pureText.trim().length <= 4) {
					MessageUtils.notice("内容至少要四个字符");
					return false;
				} else if (text.length > 5000) {
					MessageUtils.notice("文本过长，请缩减");
					return false;
				}
				return true;
			},
			submitTopic() {
				if (this.validateForm() == false) {
					return;
				}
				let addTopicForm = new FormData();
				//加图片加入到表单数据中
				for (let image of this.imageList) {
					addTopicForm.append("coverImage", image.raw);
				}
				addTopicForm.append("html", this.$refs['editBoard'].Html);
				addTopicForm.append("title", this.title);

				let addTopicReq = {};
				addTopicReq["html"] = this.$refs['editBoard'].Html;
				addTopicReq["title"] = this.title;

				let _this = this;
				addTopic(addTopicForm).then(res => {
					if (res.code == Code.OK) {
						MessageUtils.success("发表成功", 1);
						let url = '/topic/' + res.data;
						setTimeout(() => { _this.$router.push({ path: url }); }, 500);
					}
				})
				// uploadTopicImages(reqUploadImageData).then(result => {
				// 	if (result.code == Code.OK) {
				// 		let serviceToken = result.data;
				// 		addTopicReq["serviceToken"]=serviceToken;

				// 		addTopic(addTopicReq).then(result => {
				// 			if (result.code == Code.OK) {
				// 				MessageUtils.success("发表成功", 1);
				// 				let url = '/topic/' + result.data;
				// 				setTimeout(() => { _this.$router.push({ path: url }); }, 500);
				// 			}
				// 		})
				// 	}
				// });

			}

		}
	}
</script>

<style src="./PublishTopic.css" scoped>
</style>
