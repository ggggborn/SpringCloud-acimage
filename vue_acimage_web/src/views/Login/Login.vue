<template>
	<div class="login-wrapper" style="background-image: url(static/image/login-bg5.jpeg); ">
		<div class="login-main">
			<div class="login-container" :style="{height:loginBoxHeight+'px'}">
				<div style="margin-top: 0px;display: inline-block;">
					<!-- logo -->
					<img src="static/image/logo.png" />

					<!-- 登录框tabs -->
					<div class="login-tabs-container">
						<el-menu mode="horizontal" style="background-color: rgba(0,0,0,0);">
							<el-menu-item index="1" @click="isRegisterMode=false" class="login-tabs-content">登录
							</el-menu-item>
							<el-menu-item index="2" @click="isRegisterMode=true" class="login-tabs-content">注册
							</el-menu-item>
						</el-menu>
					</div>

					<div class="hint-container">
						<span class="hint-content">{{hint}}</span>
					</div>

					<el-row>
						<el-input placeholder="请输入账号" v-model="username" @change="onUsernameChange" maxlength="12"
							prefix-icon="el-icon-user" size="samll" style="width:250px;" clearable>
						</el-input>
					</el-row>

					<el-row style="margin-top: 10px;">
						<el-input placeholder="请输入密码" v-model="password" maxlength="16" prefix-icon="el-icon-lock"
							style="width:250px;" show-password>
						</el-input>
					</el-row>

					<el-row style="margin-top: 10px;" v-show="isRegisterMode">
						<el-input placeholder="请输入邮箱" v-model="email" maxlength="32" prefix-icon="el-icon-message"
							style="width:250px;" clearable>
						</el-input>
					</el-row>

					<el-row style="margin-top:10px;">
						<el-button v-if="isRegisterMode" type="primary" @click="register" style="width: 250px;">
							注册
						</el-button>
						<el-button v-else type="primary" @click="login" style="width: 250px;">
							登录
						</el-button>
					</el-row>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import { doLogin, doRegister, getPublicKey } from '@/api/login.js'
	import { queryIsUsernameExist } from '@/api/user.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	import { JSEncrypt } from 'jsencrypt'
	export default {
		name: 'LoginA',
		data() {
			return {
				username: 'wk',
				password: 'wk123456',
				email: 'wk@qq.com',
				isRegisterMode: false,
				isUsernameExist: false,
			};
		},
		computed: {
			hint() {
				if (this.isRegisterMode && this.isUsernameExist) {
					return '*用户名已存在';
				}
				return '';
			},
			loginBoxHeight() {
				return this.isRegisterMode ? '370' : '320';
			}

		},
		methods: {
			hasNull() {
				for (let item of arguments) {
					if (CommonUtils.isEmpty(item)) {
						return true;
					}
				}
				return false;
			},
			register() {
				if (this.hasNull(this.username, this.password, this.email) == true) {
					MessageUtils.notice("有选项为空");
					return false;
				}
				let _this = this;
				getPublicKey().then(result => {
					// 向服务器获取公钥
					return result.data;
				}).then(publicKey => {
					let encryptor = new JSEncrypt();
					encryptor.setPublicKey(publicKey);
					//加密
					let passwordEncrypt = encryptor.encrypt(_this.password);
					console.log(passwordEncrypt);
					let user = {
						"username": this.username,
						"password": passwordEncrypt,
						"email": this.email
					};
					doRegister(user).then(result => {
						if (result.code == Code.OK) {
							_this.$store.commit('setToken',result.data);
							MessageUtils.success("注册成功", 1);
							setTimeout(()=> {_this.$router.push({ path: '/' })}, 500);
						}
					})
				})
			},
			login() {
				if (this.hasNull(this.username, this.password, this.email) == true) {
					MessageUtils.notice('有选项为空');
					return false;
				}

				let _this = this;
				getPublicKey().then(result => {
					//获取公钥
					return result.data;
				}).then(publicKey => {
					console.log('公钥：' + publicKey);
					let encryptor = new JSEncrypt();
					encryptor.setPublicKey(publicKey);
					//加密
					let passwordEncrypt = encryptor.encrypt(_this.password);
					let user = {
						"username": this.username,
						"password": passwordEncrypt,
					};
					doLogin(user)
						.then(result => {
							if (result.code == Code.OK) {
								_this.$store.commit('setToken',result.data);
								MessageUtils.success('登录成功', 1);
								setTimeout(() => { _this.$router.push({ path: '/' }); }, 500);
							}
						})
				})

			},
			onUsernameChange() {
				//检查用户名是否存在
				if (this.isRegisterMode && this.username != '') {
					let _this = this;
					queryIsUsernameExist(_this.username)
						.then(result => {
							if (result.code == Code.OK) {
								_this.isUsernameExist = result.data;
							}
						})
				}
			}
		}
	}
</script>

<style src="./Login.css" scoped>
</style>
