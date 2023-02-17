<template>
	<div>
		<my-header></my-header>
		<div class="wrapper">
			<div class="wrapper-header">
				<i class="el-icon-s-promotion header-icon" style=""></i>
				<div class="header-title">快来分享吧</div>
				<div style="margin-top:-20px;">
					<el-divider direction="horizontal"></el-divider>
				</div>
			</div>
			<el-form ref="form" label-width="160px" style="width: 600px;" :model="addForm">
				<el-form-item label="标题">
					<el-input v-model="addForm.title" prefix-icon="el-icon-edit-outline" maxlength="30"></el-input>
				</el-form-item>
				<el-form-item label="分类">
					<el-select v-model="addForm.categoryId" placeholder="请选择分类" clearable>
						<el-option v-for="item in $store.state.categoryList" :label="item.label" :value="item.id"
							:key="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="标签(1-3个)">
					<el-tag v-for="item in chosenTagList" :key="item.id" class="hover-pointer mr5"
						@close="removeTag(item)" :type="$global.buttonType(item.id)" closable>
						{{item.label}}
					</el-tag>
					<br />
					<div>
						<el-tag v-for="item in $store.state.tagList" :key="item.id" :type="$global.buttonType(item.id)"
							@click="clickTag(item)" class="hover-pointer mr5">
							{{item.label}}
						</el-tag>
					</div>
				</el-form-item>
				<el-form-item label="封面">
					<div class="upload-container">
						<el-upload action="#" ref="upload" list-type="picture-card"
							:class="{'upload-plus-disabled':imageList.length>=1}" :on-preview="handlePictureCardPreview"
							:on-remove="handleRemove" :limit="1" :auto-upload="false" accept="image/*"
							:on-exceed="exceedLimit" :on-change="handleChange">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="dialogImageUrl" alt="">
						</el-dialog>
					</div>
					<div class="upload-hint">
						图片大小上限1MB (支持格式：jpg,png,jpeg,webp)
					</div>

				</el-form-item>
				<el-dialog :visible.sync="cropperVisible">
					<div  class="cropper-container">
						<vueCropper ref="cropper" :img="imageUrl" :outputSize="option.size"
							:outputType="option.outputType" :info="true" :full="option.full" :canMove="option.canMove"
							:canMoveBox="option.canMoveBox" :original="option.original" :autoCrop="option.autoCrop"
							:fixed="option.fixed" :fixedNumber="option.fixedNumber" :centerBox="option.centerBox"
							:infoTrue="option.infoTrue" :fixedBox="option.fixedBox">
						</vueCropper>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button size="mini" @click="handleCancelCropper">取 消</el-button>
						<el-button size="mini" type="primary" @click="confirmCoverImage">确认</el-button>
					</div>
				</el-dialog>
			</el-form>

			<div style="text-align: center;">
				<edit-board ref="editBoard" margin="160px" width="820px"></edit-board>
				<el-button @click="submitTopic" type="primary" style="margin-top: 20px;">提交</el-button>
			</div>
		</div>

	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import EditBoard from '@/components/EditBoard/EditBoard.vue'
	import { VueCropper } from 'vue-cropper'


	import { addTopic } from '@/api/topic.js'
	import { uploadTopicImages } from '@/api/image.js'

	import { Code } from '@/utils/result.js'
	import MessageUtils from '@/utils/MessageUtils'
	import StringUtils from '@/utils/StringUtils'

	import { queryAllCategories } from "@/api/category.js"
	import { queryAllTags } from "@/api/tag.js"

	export default {
		name: 'PublishTopic',
		components: {
			MyHeader,
			EditBoard,
			VueCropper
		},
		data() {
			return {
				dialogImageUrl: '',
				dialogVisible: false,
				cropperVisible: false,
				addForm: {
					title: '',
					categoryId: null,
					coverImage: {},
				},
				imageUrl: '',
				imageBlob: null,
				imageList: [],
				tempFile: null,
				imageType: null,
				chosenTagList: [],

				option: {
					img: '', // 裁剪图片地址，这里可以本地图片或者链接，链接不用require
					outputSize: 1, // 裁剪生成图片质量
					outputType: 'png', // 裁剪生成图片格式
					canScale: true, // 图片是否允许滚轮播放
					autoCrop: true, // 是否默认生成截图框 false
					info: false, // 是否展示截图框信息
					canMoveBox: true, // 截图框是否可以拖动
					fixedNumber: [135, 130],
					fixedBox: false, // 固定截图框的大小
					fixed: true,
					canMove: false, // 上传图片是否可拖动
					centerBox: true, // 截图框限制在图片里面
				},

			};
		},
		methods: {
			test() {
				alert(this.$refs['editBoard'].Html);
			},
			handleRemove(file, fileList) {
				this.imageUrl = '';
				console.log(file, fileList);
			},
			handleCancelCropper() {
				this.cropperVisible = false;
				this.imageUrl = '';
			},
			confirmCoverImage() {
				this.$refs['cropper'].getCropBlob((blob) => {
					this.imageBlob = blob;
				})
				setTimeout(() => {
					this.tempFile.url = window.URL.createObjectURL(this.imageBlob);
					this.imageList.push(this.tempFile);
				}, 100)
				this.cropperVisible = false;

			},
			handleChange(file, fileList) {
				this.cropperVisible = true;
				this.imageList = fileList;
				let raw = file.raw;
				this.imageUrl = window.URL.createObjectURL(raw);
				this.imageType = raw.type;
				this.tempFile = file;
				console.log(file)
				fileList.splice(0, 1);
			},
			handlePictureCardPreview(file) {
				this.dialogImageUrl = file.url;
				this.dialogVisible = true;
			},
			exceedLimit() {
				MessageUtils.notice('最多上传1张图');
			},
			clickTag(tag) {
				//长度限制，超过三个停止
				if(this.chosenTagList.length>=3){
					return;
				}
				//判断重复
				for (let item of this.chosenTagList) {
					if (tag.id == item.id) {
						return;
					}
				}
				this.chosenTagList.push(tag);
			},
			removeTag(tag) {
				for (let i = 0; i < this.chosenTagList.length; i++) {
					let item = this.chosenTagList[i];
					if (tag.id == item.id) {
						this.chosenTagList.splice(i, 1);
						return;
					}
				}
			},
			validateForm() {
				if (this.imageList.length == 0) {
					MessageUtils.notice("请选择封面");
					return false;
				}

				if (this.addForm.title.length < 4) {
					MessageUtils.notice("标题字数4以上");
					return false;
				} else if (this.addForm.title.length > 30) {
					MessageUtils.notice("标题字数30以内");
					return false;
				}

				let text = this.$refs['editBoard'].Html.toString();
				let pureText = StringUtils.html2Text(text);
				console.log(pureText)
				if (pureText.trim().length < 4) {
					MessageUtils.notice("内容至少要四个字符");
					return false;
				} 
				else if (text.length > 4500) {
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

				// addTopicForm.append("coverImage", this.imageList[0].raw);
				addTopicForm.append("html", this.$refs['editBoard'].Html);
				addTopicForm.append("title", this.addForm.title);
				addTopicForm.append("categoryId", this.addForm.categoryId);
				for (let tag of this.chosenTagList) {
					addTopicForm.append("tagIds", tag.id);
				}
				let _this = this;
				//新的文件名
				let filename = this.tempFile.name;
				let index = filename.indexOf(".");
				filename = filename.substring(0, index) + ".png";
				addTopicForm.append("coverImage", new File([this.imageBlob], filename, { type: "image/png" }));
				addTopic(addTopicForm).then(res => {
					if (res.code == Code.OK) {
						MessageUtils.success("发表成功", 1);
						let url = '/topic/' + res.data;
						setTimeout(() => { _this.$router.push({ path: url }); }, 500);
					}
				})
			},
		}
	}
</script>

<style src="./PublishTopic.css" scoped>
</style>
