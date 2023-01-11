<template>
	<div class="Bread">
		<el-breadcrumb separator="/" style="margin-left: 200px;">
			<!-- to为点击跳转 title为路由中的meta属性定义的标题 -->
			<el-breadcrumb-item v-for="(item, index) in breadList" :key="index" >
				{{ item}}
			</el-breadcrumb-item>

			<el-breadcrumb separator-class="el-icon-arrow-right">
				<el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
				<el-breadcrumb-item>活动管理</el-breadcrumb-item>
				<el-breadcrumb-item>活动列表</el-breadcrumb-item>
				<el-breadcrumb-item>活动详情</el-breadcrumb-item>
			</el-breadcrumb>
		</el-breadcrumb>
	</div>
</template>

<script>
	export default {
		name: "BreadCrumb",
		data() {
			return {
				breadList: [],
			};
		},
		watch: {
			$route() { //监听$route
				this.getBreadcrumb();
			},
		},
		methods: {
			// isHome(route) {
			// 	return route.name === "home";
			// },
			getBreadcrumb() {
				// let matched = this.$route.matched;
				// //如果不是首页
				// if (!this.isHome(matched[0])) {
				// 	matched = [].concat(matched);
				// }
				this.breadList[0] = this.$router.path;
			},
		},
		created() {
			//生命周期中调用获取数据的方法
			this.getBreadcrumb();
		},
	};
</script>

<style>
	.Bread {
		margin-left: 300px;
		height: 30px;
		float: left;
	}
</style>
