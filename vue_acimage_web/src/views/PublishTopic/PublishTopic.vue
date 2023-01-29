<template>
	<div>
		<my-header></my-header>

		<div class="publish-main">

			<div class="publish-header">
				<i class="el-icon-s-promotion publish-header-icon" style=""></i>
				<div class="publish-header-title">快来分享吧</div>
				<div style="margin-top:-20px;">
					<el-divider direction="horizontal"></el-divider>
				</div>
			</div>
			<el-form ref="form" label-width="160px" style="width: 600px;" :model="addForm">
				<el-form-item label="标题">
					<el-input v-model="addForm.title" prefix-icon="el-icon-edit-outline" maxlength="30"></el-input>
				</el-form-item>
				<el-form-item label="分类">
					<el-select v-model="addForm.categoryId" placeholder="请选择分类">
						<el-option v-for="item in $store.state.categoryList" :label="item.label" :value="item.id"
							:key="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="标签(选1-3个)">

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
						图片大小上限2MB
					</div>
				</el-form-item>
			</el-form>

			<div style="text-align: center;">
				<edit-board ref="editBoard" margin="160px" width="720px"></edit-board>
				<el-button @click="submitTopic" type="primary" style="margin-top: 20px;">提交</el-button>
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

	import { queryAllCategories } from "@/api/category.js"
	import { queryAllTags } from "@/api/tag.js"

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
				addForm: {
					title: '',
					categoryId: 1,
					coverImage: {},
				},
				imageList: [],
				// categoryList: [{
				// 	id: 1,
				// 	label: '无'
				// }],
				// tagList: [{
				// 		id: 1,
				// 		label: '冒险'
				// 	},
				// 	{
				// 		id: 2,
				// 		label: '娱乐'
				// 	},
				// ],
				chosenTagList: [],


			};
		},
		created() {
			// let _this = this;
			// queryAllCategories().then(res => {
			// 	if (res.code == Code.OK) {
			// 		_this.categoryList = res.data;
			// 	}
			// })
			// queryAllTags().then(res => {
			// 	_this.tagList = res.data;
			// });
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
			clickTag(tag) {

				//判断重复
				for (let item of this.chosenTagList) {
					if (tag.id == item.id) {
						return;
					}
				}
				this.chosenTagList.push(tag);
				console.log(tag);
				console.log(this.chosenTagList)
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
					MessageUtils.notice("请选择至少一张图片");
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

				addTopicForm.append("coverImage", this.imageList[0].raw);
				addTopicForm.append("html", this.$refs['editBoard'].Html);
				addTopicForm.append("title", this.addForm.title);
				addTopicForm.append("categoryId", this.addForm.categoryId);
				for (let tag of this.chosenTagList) {
					addTopicForm.append("tagIds", tag.id);
				}


				// let addTopicReq = {};
				// addTopicReq["html"] = this.$refs['editBoard'].Html;
				// addTopicReq["title"] = this.title;

				let _this = this;
				addTopic(addTopicForm).then(res => {
					if (res.code == Code.OK) {
						MessageUtils.success("发表成功", 1);
						let url = '/topic/' + res.data;
						setTimeout(() => { _this.$router.push({ path: url }); }, 500);
					}
				})

			}

		}
	}
</script>

<style src="./PublishTopic.css" scoped>
</style>
