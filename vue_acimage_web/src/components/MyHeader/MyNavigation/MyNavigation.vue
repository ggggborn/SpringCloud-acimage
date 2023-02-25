<template>
	<div>
		<div class="wrapper">
			<el-row>
				<el-menu router mode="horizontal" style="border: 0px;height:52px;margin-top:-12px;opacity: 0.85;">
					<el-menu-item index="/">
						<i class="el-icon-house" style="color:orange"></i>主页
					</el-menu-item>
					<el-menu-item index="/MyActivity">
						<i class="el-icon-magic-stick" style="color:orange"></i>我的动态
					</el-menu-item>
					<el-menu-item index="/MyMessage">
						<i class="el-icon-magic-stick" style="color:orange"></i>消息
						<el-badge :value="messageNum" :max="99" style="position: fixed;top: -20px;">
						</el-badge>
					</el-menu-item>

					<el-submenu index="3">
						<template slot="title">
							<i class="el-icon-user-solid" style="color:orange"></i>个人中心
						</template>
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
				<el-input v-model="search" @keyup.enter.native="enterSearch" placeholder=" ( ﹁ ﹁ ) ~→搜什么呢..."
					maxlength="15" prefix-icon="el-icon-edit" size="small" clearable>
				</el-input>
				<el-avatar :src="$store.getters.getPhotoUrl" :size="50"
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
				search: '',
				webSocket: null,
				messageNum: null,
			}
		},
		mounted() {
			let _this = this;
			if (!this.$store.getters.isLogin) {
				return;
			}

			let loc = window.location,
				new_uri;
			if (loc.protocol === "https:") {
				new_uri = "wss:";
			} else {
				new_uri = "ws:";
			}
			new_uri += "//" + loc.host + "/websocket";
			let ws = new WebSocket(new_uri, this.$store.state.token);
			// let ws = new WebSocket("ws:localhost:81/websocket", this.$store.state.token);
			//监听是否连接成功
			ws.onopen = function() {
				console.log('连接成功');
				//连接成功则发送一个数据
				ws.send('test1');
			}
			// 接听服务器发回的信息并处理展示
			ws.onmessage = function(messageEvent) {
				console.log('接收到来自服务器的消息：' + messageEvent.data);
				if (messageEvent.data != 0) {
					_this.messageNum = messageEvent.data;
				}
			}
			// 监听连接关闭事件
			ws.onclose = function() {
				// 监听整个过程中websocket的状态
				console.log('关闭成功');
				ws.close()
			}
			// 监听并处理error事件
			ws.onerror = function(error) {
				console.log(error);
			}
			this.webSocket = ws;

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
			},
			enterSearch() {
				if (this.$router.path != '/SearchTopic') {
					this.$router.push({
						path: '/SearchTopic',
						query: { search: this.search }
					});
				}
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
