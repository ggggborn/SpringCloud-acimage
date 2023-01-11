<template>
	<div>
		<my-header></my-header>
		<div class="wrapper">
			<div class="main">
				<div class="profile-header">
					<i class="el-icon-user profile-header-icon"></i>
					<div class="profile-header-title">我的信息</div>
					<div style="margin-top:-20px;">
						<el-divider direction="horizontal"></el-divider>
					</div>
				</div>
				<div class="profile-content">
					<div class="profile-content-left">
						<div style="display: inline-block;text-align: right: 10px;">
							<el-avatar :src="$store.getters.truePhotoUrl" :size="120"></el-avatar>
						</div>

						<br />
						<el-button type="primary" @click="uploadPhotoVisible=true" size="mini">更换头像</el-button>
					</div>

					<div class="profile-content-right">
						<div class="item-edit-btn">
							<el-button type="primary" size="mini" @click="usernameModifyVisible=true">修改
							</el-button>
						</div>
						<el-form label-position="left" label-width="70px">

							<div style="margin-bottom:-20px;">

								<el-form-item label="用户名">
									{{user.username}}
								</el-form-item>
							</div>

							<div style="margin-bottom:-20px;">
								<el-form-item label="邮箱">
									{{user.email}}
								</el-form-item>
							</div>

							<div style="margin-bottom:-20px;">
								<el-form-item label="注册时间">
									{{user.registerTime}}
								</el-form-item>
							</div>

							<div style="margin-bottom:-20px;">
								<el-form-item label="话题">
									{{user.topicCount}}
								</el-form-item>
							</div>

							<div style="margin-bottom:-20px;">
								<el-form-item label="star">
									{{user.starCount}}
								</el-form-item>
							</div>
						</el-form>
						<div style="text-align: center;">
							<el-button type="primary" size="mini" @click="passwordModifyVisible=true">修改密码
							</el-button>
						</div>

						<!-- <div>
							用户名：{{user.username}}
							<div class="item-edit-btn">
								<el-button type="primary" size="mini" @click="usernameModifyVisible=true">修改
								</el-button>
							</div>
	
						</div>
						<div>
							邮箱：{{user.email}}
						</div>
						<div>
							分享数：996
						</div>
						<div>
							star:9
						</div> -->
					</div>
				</div>

			</div>
		</div>
		<!-- 修改用户名对话框 -->
		<el-dialog title="修改用户名" :visible.sync="usernameModifyVisible">
			<el-form>
				<el-form-item label="新的用户名">
					<el-input autocomplete="off" v-model="usernameModify" maxlength="12"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="usernameModifyVisible = false">取 消</el-button>
				<el-button type="primary" @click="onConfirmModifyUsername">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 上传头像对话框 -->
		<el-dialog title="上传头像" :visible.sync="uploadPhotoVisible">
			<el-form>
				<el-form-item>
					<el-upload action="https://jsonplaceholder.typicode.com/posts/" ref="upload"
						:class="{'upload-plus-disabled':imageList.length>=1}" list-type="picture-card"
						:on-remove="handleRemove" :limit="1" :auto-upload="false" accept="image/*"
						:on-exceed="exceedLimit" :on-change="handleChange">
						<i class="el-icon-plus"></i>
					</el-upload>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="uploadPhotoVisible = false">取 消</el-button>
				<el-button type="primary" @click="onConfirmUploadPhoto">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 修改密码对话框 -->
		<el-dialog title="修改密码" :visible.sync="passwordModifyVisible">
			<el-form>
				<el-form-item label="旧密码">
					<el-input autocomplete="off" v-model="oldPassword" maxlength="16" show-password></el-input>
				</el-form-item>
				<el-form-item label="新密码">
					<el-input autocomplete="off" v-model="newPassword" maxlength="16" show-password></el-input>
				</el-form-item>
				<el-form-item label="确认新密码">
					<el-input autocomplete="off" v-model="passwordEnsure" maxlength="16" show-password></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="passwordModifyVisible = false">取 消</el-button>
				<el-button type="primary" @click="passwordModifyVisible = false;">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'

	import { modifyUsername, queryProfile } from '@/api/user.js'
	import { uploadPhoto} from '@/api/photo.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'MyProfile',
		components: {
			MyHeader
		},
		data() {
			return {
				//修改用户名
				usernameModifyVisible: false,
				usernameModify: '',
				//修改密码
				passwordModifyVisible: false,
				oldPassword: '',
				newPassword: '',
				passwordEnsure: '',
				//上传头像
				uploadPhotoVisible: false,
				imageList: [],
				//用户个人信息
				user: {
					username: '无名氏',
					email: 'wyktxdy999@qq.com',
					registerTime: '2022-2-22 22:22:22',
					topicCount: 666,
					starCount: 666
				}
			}
		},
		mounted() {
			let _this = this;
			queryProfile().then(result => {
				_this.user = result.data;
				_this.usernameModify = _this.user.username;
			})

		},
		methods: {
			handleRemove(file, fileList) {
				console.log(file, fileList);
			},
			handleChange(file, fileList) {
				this.imageList = fileList;
				console.log(this.imageList);
			},
			exceedLimit() {
				MessageUtils.notice('只能选择1张图');
			},
			//修改用户名
			onConfirmModifyUsername() {
				let _this = this;
				modifyUsername(this.usernameModify).then(result => {
					if (result.code == Code.OK) {
						_this.$store.commit('setToken',result.data);
						MessageUtils.success("修改成功", 1);
						CommonUtils.delayRefresh(1);
					}
				})
			},
			onConfirmUploadPhoto() {
				if (this.imageList.length == 0) {
					MessageUtils.notice("请选择至少一张图片");
					return;
				}
				//将photo加入到表单数据中
				let reqData = new FormData();
				let photo = this.imageList[0];
				reqData.append("photoFile", photo.raw);

				let _this = this;
				uploadPhoto(reqData).then(result => {
					if (result.code == Code.OK) {
						_this.$store.commit('setToken',result.data);
						MessageUtils.success("上传成功", 1);
						CommonUtils.delayRefresh(1);
					}
				})


				// $.ajax({
				// 	url: "/api/users/uploadPhoto",
				// 	type: "post", //请求方式
				// 	data: reqData, //请求数据
				// 	processData: false, //是否将请求数据转换为对象
				// 	contentType: false, //发送数据到服务器时所使用的内容类型，false表示不需要ajax处理
				// 	dataType: 'json',
				// 	success: function(result) {
				// 		if (result.code == Code.OK) {
				// 			_this.passwordModifyVisible = false;
				// 			_this.popupSuccess("上传成功");
				// 			setTimeout(function() {
				// 				window.location.reload();
				// 			}, 1000);
				// 		} else {
				// 			_this.popupHint(result.msg);
				// 		}
				// 	},
				// 	error: function() {
				// 		_this.popupHint("提交失败");
				// 	}
				// });
			}
		}
	}
</script>

<style src="./MyProfile.css" scoped>
</style>
