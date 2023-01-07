<template>
	<div class="comment-container">
		<div style="width:70px;">
			<el-avatar :src="photoSrc" :size="60"></el-avatar>
		</div>
		<div class="comment-right">
			<div class="comment-username">
				{{username}}
			</div>
			<div class="comment-content">
				{{content}}
			</div>
			<div class="comment-bottom">
				{{updateTime}}
				<div class="edit-button" v-if="editable">
					<el-button type="text" class="gray-color" size="mini" @click="onClickEdit">编辑</el-button>
					<el-button type="text" class="gray-color" size="mini" @click="onClickDelete">删除</el-button>
				</div>
			</div>
		</div>

		<!-- 修改评论对话框 -->
		<el-dialog title="修改评论" :visible.sync="commentModifyVisible">
			<el-form>
				<el-form-item label="修改评论(2-150字)">
					<el-input v-model="contentModify" maxlength="150" type="textarea" :rows="5" autocomplete="off">
					</el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="commentModifyVisible = false">取 消</el-button>
				<el-button type="primary" @click="onConfirmModify">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import { deleteComment, modifyComment } from '@/api/comment.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'UserComment',
		data() {
			return {
				// isModifyMode: false,
				commentModifyVisible: false,
				contentModify: ''
			};
		},
		props: {
			photoSrc: {},
			commentId: {},
			username: { default: '名字加载中' },
			content: { default: '内容加载中' },
			updateTime: { default: '2022-2-22 22:22:22' },
			editable: { default: false, type: Boolean }
		},
		watch: {
			content: {
				handler(newVal, oldVal) {
					this.contentModify = this.content;
				},
				immediate: true
			}
		},
		methods: {
			onClickEdit() {
				this.commentModifyVisible = true;
			},
			//修改评论
			onConfirmModify() {
				let reqData = { 'id': this.commentId, 'content': this.contentModify };
				let _this = this;
				CommonUtils.popMsgAndRefreshIfOk(modifyComment(reqData), '修改成功', 1);
			},
			//删除评论
			onClickDelete() {
				let _this = this;
				MessageUtils.confirm('确认删除？操作不可逆').then(() => {
					CommonUtils.popMsgAndRefreshIfOk(deleteComment(_this.commentId), '删除成功', 1);
				})

			}
		}
	}
</script>

<style src="./UserComment.css" scoped>
</style>
