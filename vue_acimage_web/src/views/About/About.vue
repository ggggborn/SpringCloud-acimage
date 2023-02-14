<template>
	<div id="app">
		<my-header></my-header>
		<div class="main">
			<div class="header">
				<i class="el-icon-info header-icon" style=""></i>
				<div class="header-title">关于本站</div>
				<div style="margin-top:-20px;">
					<el-divider direction="horizontal"></el-divider>
				</div>
			</div>
			<div style="padding:80px;color:#666666">
				一. 本站GitHub地址
				<br />
				
				<el-link href="https://github.com/ggggborn/SpringCloud-acimage" target="_blank">
					<el-image src="static/image/github.webp" style="width: 20px; height: 20px " fit="cover"></el-image>
					https://github.com/ggggborn/SpringCloud-acimage
				</el-link>
				<br />
				喜欢的话进去来个<i class="el-icon-star-on" style="color:orange"></i>吧 o(￣▽￣)ｄ
				<br />
				欢迎大家加群692992463（进群备注：ac）交流本项目✧(≖ ◡ ≖✿)
				<br />
				<br />
				
				二. 网站规则
				<br />
				为了防止乱发言和被攻击，网站随时会调整访客和用户的权限，有可能导致某些接口不可用┑(￣Д ￣)┍
				<br />
				为了防止恶意刷屏，本站对每天发表话题/评论和修改话题/评论次数都有限制
				<br />
				如果想进一步得到更多权限，请加qq群692992463（进群备注：ac），找群主索要
				<br />
				<br />
				
				三. 请大家文明发言，谢谢
				<br />
				<br />
				
				四. 目前功能比较少，等我有精力会不断完善
				
				
			</div>
		</div>

	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'AboutA',
		components: {
			MyHeader,
		},
		data() {
			return {

			};
		},
		mounted() {

		},
		methods: {

		}


	}
</script>

<style src="./About.css" scoped>
</style>
