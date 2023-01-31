<template>
	<div>
		<div class="wrapper">
			<el-row>
				<el-menu mode="horizontal" router style="border: 0px;height:52px;margin-top:-12px;opacity: 0.85;">
					<el-menu-item index="/">
						<i class="el-icon-house" style="color:orange"></i>主页
					</el-menu-item>
					<el-menu-item index="/MyActivity">
						<i class="el-icon-magic-stick" style="color:orange"></i>我的动态
					</el-menu-item>

					<el-menu-item index="2">
						<router-link to="/MyActivity" class="no-underline">
							<i class="el-icon-bell" style="color:red"></i>消息
						</router-link>
					</el-menu-item>

					<el-submenu index="3">
						<template slot="title"><i class="el-icon-user-solid" style="color:orange"></i>个人中心</template>
						<el-menu-item index="3-1">
							<i class="el-icon-bell" style="color:red"></i>消息中心
						</el-menu-item>
						<el-menu-item index="/profile">
							<i class="el-icon-info" style="color:orange"></i>我的信息
						</el-menu-item>
						<el-menu-item @click="logout">
							<i class="el-icon-upload2" style="color:orange"></i>登出
						</el-menu-item>
					</el-submenu>
					<el-menu-item>
						<el-button @click="onClickLogin" type="danger" style="position:fixed;right:80px;top: 0px;">登录
						</el-button>
					</el-menu-item>
				</el-menu>
				<el-input v-model="search" placeholder="想搜点什么呢..." maxlength="15" prefix-icon="el-icon-edit"
					size="small" clearable>
				</el-input>
				<el-avatar :src="$store.getters.truePhotoUrl" :size="50"
					style="margin-top:15px;position:fixed;right:10px;top:-10px;">
				</el-avatar>

			</el-row>
		</div>
	</div>
</template>

<script>
	import { doLogout } from '@/api/login.js'

	import { Code } from '@/utils/result.js'
	import MessageUtils from '@/utils/MessageUtils.js'
	export default {
		name: 'MyNavigation',
		data() {
			return {
				search: ''
			}
		},
		methods: {
			logout() {
				let _this = this;
				doLogout().then(result => {
					if (result.code == Code.OK) {
						_this.$store.commit('removeToken');
						_this.$router.push({ path: '/login' });
					}
				})
			},
			onClickLogin() {
				let _this = this;
				MessageUtils.confirm('确定跳转到登录界面？').then(() => {
					_this.$router.push({ path: '/login' });
				})
			}

		}
	}
</script>

<style scoped>
	.wrapper {
		width: 100%;
		height: 40px;
		position: fixed;
		top: 0px;
		left: 0px;
		overflow: hidden;
		z-index: 100;
		background-color: raba(255, 255, 255, 0.85);
	}

	.wrapper>>>.el-input {
		width: 200px;
		position: fixed;
		right: 200px;
		top: 4px;
		height: 30px;
		opacity: 0.9;
		transition: 0.7s;
	}

	.wrapper>>>.el-input:hover {
		width: 280px;
		transition: 0.7s;
	}
</style>
