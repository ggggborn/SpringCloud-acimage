<template>
	<div>
		<my-header></my-header>
		<div class="wrapper">
			<el-skeleton class="wrapper-left" v-if="loading" :rows="10" animated />
			<div v-else class="wrapper-left">
				<!-- 当前话题的相关信息 -->
				<div class="title">
					{{topic.title}}
				</div>
				<div class="topic-info">
					<i class="el-icon-user"></i>
					{{topic.user.username}}
					<el-divider direction="vertical"></el-divider> {{topic.createTime}}
					<el-divider direction="vertical"></el-divider> {{topic.pageView}}浏览
					<el-divider direction="vertical"></el-divider> {{topic.starCount}}收藏
					<el-divider direction="vertical"></el-divider> {{topic.commentCount}}评论
					<!-- 编辑标题按钮 -->
					<div v-if="$store.state.userId == topic.userId" class="title-edit-button">
						<el-button @click="titleModifyVisible=true" class="gray-color" type="text" size="mini">编辑标题
						</el-button>
					</div>
					<div style="margin-top: 8px;">
						<el-tag :type="$global.buttonType(topic.categoryId)" effect="plain" size="small" :key="-1">
							{{$store.getters.categoryLabel(topic.categoryId)}}
						</el-tag>
						<span class="tags-container">
							<el-tag v-for="tagId in topic.tagIds" :type="$global.buttonType(tagId)" effect="plain"
								size="mini" :key="tagId">
								{{$store.getters.tagLabel(tagId)}}
							</el-tag>
						</span>

					</div>
				</div>
				<!--End 当前话题的相关信息 -->
				<div style="margin-top:-20px;width:90%;margin-left:5%">
					<el-divider direction="horizontal"></el-divider>
				</div>
				<!--话题内容 -->
				<div class="topic-html-container">
					<div v-dompurify-html="topic.html"></div>
				</div>

				<div style="text-align: center;margin-top:20px;">
					<el-button @click="onClickStar" :type=" isStar?'info':'danger' " :disabled="isLoadingStar"
						style="border:none" :icon="isStar?'':'el-icon-star-off'">
						{{isStar?'取消星星':'给TA星星'}}
					</el-button>
					<!-- 					<el-button type="danger" style="border:none" icon="el-icon-download" @click="onClickDownloadImages">
						下载
					</el-button> -->
				</div>

				<div class="time-hint">
					编辑于：{{topic.updateTime}}
					<template v-if="$store.state.userId==topic.userId">
						<div class="content-edit-button">
							<el-button @click="contentModifyVisible=true" type="text" class="gray-color" size="mini">
								编辑内容
							</el-button>
							<el-button @click="onClickDeleteTopic" type="text" class="gray-color" size="mini">删除话题
							</el-button>
						</div>
					</template>
				</div>

				<!-- 分割线 -->
				<div style="width:90%;margin-left:5%">
					<el-divider direction="horizontal"></el-divider>
				</div>

				<!-- 当前用户评论框 -->
				<publish-comment :topic-id="topic.id"></publish-comment>

				<!-- 当前话题的评论 -->
				<div class="comments-container">
					<div class="comments-count">
						共 {{topic.commentCount}} 条评论
					</div>
					<user-comment v-for="comment in topic.comments" :key="comment.id"
						:photo-src="$global.getPhotoUrl(comment.user.photoUrl)" :username="comment.user.username"
						:content="comment.content" :createTime="comment.createTime" :comment-id="comment.id"
						:editable="$store.state.userId==comment.user.id">
					</user-comment>
					<!-- 分页 -->
					<div v-if="topic.comments.length!=0" style="text-align: center;">
						<el-pagination background layout="prev, pager, next" :total="topic.commentCount"
							:current-page.sync="pageNo" @current-change="onPageNoChange" :page-size="10">
						</el-pagination>
					</div>
				</div>
				<!-- End 当前话题的评论 -->
			</div>

			<div class="wrapper-right">
				<!-- 右侧话题主人信息 -->
				<div class="user-container hover-shadow">
					<el-avatar :src="$global.getPhotoUrl(topic.user.photoUrl)" :size="90" style="margin-top:20px;">
					</el-avatar>
					<br />
					<span style="color:#F67B7B;">{{topic.user.username}}</span>
					<div style="font-size:14px;margin-top:5px;color:#999999;">
						<i class="el-icon-star-on" style="color:orange;"></i>{{topic.user.starCount}}
						<i class="el-icon-edit" style="color:skyblue;"></i>{{topic.user.topicCount}}
						<br />
						这是我的个性签名
					</div>
					<div style="margin-top:10px;">
						<el-button type="primary" icon="el-icon-plus" size="mini"
							style="background-color:#EC5555;border:none;">
							关注
						</el-button>
					</div>
				</div>
				<!--End 右侧话题主人信息-->

				<div v-if="!$global.isEmpty(topic.similarTopics)" style="margin-top: 10px;">
					<topic-rank :labels="['相关推荐']" :topics="topic.similarTopics" headerSrc="static/image/similar-topics-header.webp"></topic-rank>
				</div>
			</div>
		</div>

		<!-- 修改标题对话框 -->
		<el-dialog title="修改标题" :visible.sync="titleModifyVisible">
			<el-form>
				<el-form-item label="新的标题(2-30字)">
					<el-input v-model="titleModify" maxlength="30" clearable=""></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="titleModifyVisible = false">取 消</el-button>
				<el-button type="primary" @click="onConfirmModifyTitle">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 修改内容对话框 -->
		<el-dialog title="修改内容" :visible.sync="contentModifyVisible" width="900px">
			<edit-board ref="editBoard" width="820px" :html="topic.html"></edit-board>
			<div slot="footer" class="dialog-footer">
				<el-button @click="contentModifyVisible = false">取 消</el-button>
				<el-button type="primary" @click="onConfirmModifyHtml">确 定</el-button>
			</div>
		</el-dialog>

	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import UserComment from './UserComment/UserComment.vue'
	import PublishComment from './PublishComment/PublishComment.vue'
	import EditBoard from '@/components/EditBoard/EditBoard.vue'
	import TopicRank from '@/components/TopicRank/TopicRank.vue'


	import {
		deleteTopic,
		queryTopicAndFirstCommentPage,
		modifyHtml,
		modifyTitle
	} from '@/api/topic.js'
	import { queryCommentPage } from '@/api/comment.js'
	import { deleteStar, addStar, queryIsStar } from '@/api/star.js'
	import { downloadImages, modifyDescription } from '@/api/image.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'
	export default {
		name: 'TopicInfo',
		components: {
			MyHeader,
			UserComment,
			PublishComment,
			EditBoard,
			TopicRank
		},
		data() {
			return {
				loading: true,
				isLoadingStar: false,
				titleModifyVisible: false,
				titleModify: '',
				contentModifyVisible: false,
				// 当前用户的评论
				comment: {
					content: ''
				},
				//记录修改后的数据
				isModifyTopic: false,
				descriptionsModify: [],
				pageNo: 1,
				//展示话题的相关数据
				isStar: false,
				topic: {
					id: 0,
					title: '这是我的title',
					user: {
						userId: 996,
						username: 'TA996',
						starCount: 996,
						photoUrl: '',
					},
					categoryId: 4,
					createTime: '2022-2-22 22:22:22',
					updateTime: '2022-2-22 22:22:22',
					pageView: 996,
					starCount: 996,
					commentCount: 20,
					html: `这些图片辛苦找来的，大家且看且珍惜`,
					comments: [],
					similarTopics: []
				}
			};
		},
		watch: {
			'$route'(to, from) {
				if (to.params.id != from.params.id) {
					this.init(to.params.id); //重新加载数据
				}
			}
		},
		mounted() {
			//获取参数
			let id = this.$route.params.id;
			this.init(id);
		},
		methods: {
			init(id) {
				if (CommonUtils.isEmpty(id)) {
					return;
				}
				let _this = this;
				queryTopicAndFirstCommentPage(id).then(result => {
					if (result.code == Code.OK) {
						_this.topic = result.data;
						console.log(_this.topic)
						_this.loading = false;
						_this.initDataForModify();
					}
				})
				if (_this.$store.getters.isLogin) {
					queryIsStar(id).then(result => {
						if (result.code == Code.OK) {
							_this.isStar = result.data;
						}
					})
				}

			},
			onClickStar() {
				let _this = this;
				if (!_this.$store.getters.isLogin) {
					MessageUtils.notice("请先登录");
					return;
				}
				if (this.isStar) {
					deleteStar(this.topic.id)
						.then(result => {
							if (result.code == Code.OK) {
								_this.isStar = !_this.isStar;
								MessageUtils.success("取消成功");
							}
						});
				} else {
					addStar(this.topic.id)
						.then(result => {
							if (result.code == Code.OK) {
								_this.isStar = !_this.isStar;
								MessageUtils.success("收藏成功");
							}
						});
				}
			},
			onConfirmModifyTitle() {
				modifyTitle(this.topic.id, this.titleModify).then(result => {
					if (result.code == Code.OK) {
						MessageUtils.success("修改标题成功", 1);
						CommonUtils.delayRefresh(0.5);
					}
				})
			},
			onConfirmModifyHtml() {
				let data = { id: this.topic.id, html: this.$refs['editBoard'].Html };
				modifyHtml(data).then(result => {
					if (result.code == Code.OK) {
						MessageUtils.success("修改内容成功", 1);
						CommonUtils.delayRefresh(0.5);
					}
				})
			},
			initDataForModify() {
				this.isModifyTopic = false;
				this.contentModify = this.topic.content;
				this.titleModify = this.topic.title;
			},
			//删除话题
			onClickDeleteTopic() {
				let _this = this;
				MessageUtils.confirm('确认删除？操作不可逆').then(() => {
					deleteTopic(_this.topic.id).then(res => {
						if (res.code == Code.OK) {
							MessageUtils.success('删除成功')
							setTimeout(function() {
								_this.$router.push({ path: '/' })
							}, 1000);
						}
					})
				})
			},
			//下载话题所有图片
			onClickDownloadImages() {
				let _this = this;
				let imageIds = [];
				for (let image of this.topic.images) {
					imageIds.push(image.id);
				}
				downloadImages(imageIds, this.topic.id);
			},
			//获取对应页评论
			onPageNoChange() {
				let _this = this;
				queryCommentPage(this.topic.id, this.pageNo).then(result => {
					if (result.code == Code.OK) {
						_this.topic.comments = result.data;
					}
				})
			}
		}

	}
</script>

<style src="./TopicInfo.css" scoped>
	.tox-tinymce-aux {
		z-index: 9999 !important;
	}
</style>
