<template>
	<div>
		<my-navigation></my-navigation>
		<!-- header背景图 -->
		<div style="height:100px;background-image: url(static/image/header-bg.png);background-position: center;">
		</div>
		<div class="tabs-wrapper">
			<img src="static/image/happy.jpeg" />
			<div class="tabs-container">
				<router-link to="/" class="no-underline tabs-item-link">
					<i class="el-icon-s-home" style="color: red;"></i>首页
				</router-link>
				<el-divider direction="vertical"></el-divider>
				<router-link to="/forum" class="no-underline tabs-item-link">
					<i class="el-icon-s-home" style="color: red;"></i>论坛
				</router-link>
				<el-divider direction="vertical"></el-divider>
				<router-link to="/publish" class="no-underline tabs-item-link">
					<i class="el-icon-s-promotion" style="color: red;"></i>分享
				</router-link>
				<el-divider direction="vertical"></el-divider>
				<router-link to="/" class="no-underline tabs-item-link">
					<i class="el-icon-search" style="color: red;"></i>搜索
				</router-link>
				<el-divider direction="vertical"></el-divider>
				<router-link to="/SearchImage" class="no-underline tabs-item-link">
					<i class="el-icon-search" style="color: red;"></i>识图
				</router-link>
			</div>
			<!-- <span style="color:red;">HOT</span> -->
			<img src="static/image/sad.jpeg" />
		</div>
	</div>
</template>

<script>
	import MyNavigation from './MyNavigation/MyNavigation.vue'
	export default {
		name: 'MyHeader',
		components: {
			MyNavigation
		},
	}
</script>

<style src="./MyHeader.css" scoped>
</style>
