<template>
	<div class="nav">
		<el-row>
			<el-col>
				<el-menu mode="horizontal" style="border: 0px;height:52px;margin-top:-12px;">
					<el-menu-item index="0">
						<router-link to="/" class="no-underline">
							<i class="el-icon-house" style="color:orange"></i>主页
						</router-link>
					</el-menu-item>
					<el-menu-item index="1">
						<router-link to="/MyActivity" class="no-underline">
							<i class="el-icon-magic-stick" style="color:orange"></i>我的动态
						</router-link>
					</el-menu-item>

					<el-menu-item index="2">
						<el-link href="https://element.eleme.io" target="_blank">
							<i class="el-icon-bell" style="color:red"></i>消息
						</el-link>
					</el-menu-item>

					<el-submenu index="3">
						<template slot="title"><i class="el-icon-user-solid" style="color:orange"></i>个人中心</template>
						<el-menu-item index="3-1"><i class="el-icon-bell" style="color:red"></i>消息中心
						</el-menu-item>
						<el-menu-item index="3-2"><i class="el-icon-info" style="color:orange"></i>
							<router-link to="/profile" class="no-underline router-link-item">
								我的信息
							</router-link>
						</el-menu-item>
						<el-menu-item index="3-3" @click="logout">
							<i class="el-icon-upload2" style="color:orange"></i>登出
						</el-menu-item>
					</el-submenu>
					<el-menu-item index="-1">
						<el-button @click="onClickLogin" type="danger" style="position:fixed;right:80px;top: 0px;">登录
						</el-button>
						<!-- <image style="width: 60px; height: 60px;border-radius:50%; overflow:hidden;" :src="PHOTO_URL" ></el-image> -->
					</el-menu-item>

					<el-avatar :src="$store.getters.truePhotoUrl" :size="50" style="margin-top:15px;position:fixed;right:10px;">
					</el-avatar>
				</el-menu>

			</el-col>
		</el-row>
	</div>
</template>

<script>
	import { doLogout } from '@/api/login.js'

	import { Code } from '@/utils/result.js'
	import MessageUtils from '@/utils/MessageUtils.js'
	export default {
		name: 'MyNavigation',
		methods: {
			logout() {
				let _this = this;
				doLogout()
					.then(result => {
						if (result.code == Code.OK) {
							_this.$store.commit('removeToken');
							MessageUtils.success('登出成功', 1);
							setTimeout(() => { _this.$router.push({ path: '/login' }); }, 500)
						}
					})
			},
			onClickLogin(){
				let _this=this;
				MessageUtils.confirm('确定跳转到登录界面？').then(()=>{
					_this.$router.push({ path: '/login' });
				})
			}

		}
	}
</script>

<style scoped>
	.nav {
		width: 100%;
		/* height:75px; */
		height: 40px;
		position: fixed;
		top: 0px;
		left: 0px;
		/* box-shadow:1px 1px 3px #A7A796; */
		overflow: hidden;
		z-index: 100;
		opacity: 0.85;
	}
</style>
