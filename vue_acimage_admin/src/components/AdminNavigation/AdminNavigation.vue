<template>
	<div class="admin-navigation">
		<div class="tac">
			<el-menu default-active="1" class="el-menu-vertical-demo" background-color="#666666" text-color="#fff"
				active-text-color="#ffd04b">
				<el-menu-item index="1">
					<i class="el-icon-house"></i>
					<span slot="title">首页管理</span>
				</el-menu-item>
				<el-submenu index="2">
					<template slot="title">
						<i class="el-icon-location"></i>
						<span>首页管理</span>
					</template>
					<el-menu-item-group>
						<router-link to="/home/carsoul" class="no-underline">
							<el-menu-item index="2-1">走马灯图片</el-menu-item>
						</router-link>
						<el-menu-item index="2-2">选项2</el-menu-item>
					</el-menu-item-group>
					<el-menu-item-group title="分组2">
						<el-menu-item index="2-3">选项3</el-menu-item>
					</el-menu-item-group>
					<el-submenu index="2-4">
						<template slot="title">选项4</template>
						<el-menu-item index="2-4-1">选项1</el-menu-item>
					</el-submenu>
				</el-submenu>
				<el-menu-item index="3">
					<i class="el-icon-document"></i>
					<span slot="title">导航三</span>
				</el-menu-item>
				<el-menu-item index="4">
					<i class="el-icon-setting"></i>
					<span slot="title">导航四</span>
				</el-menu-item>
			</el-menu>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'AdminNavigation'
	}
</script>

<style scoped>
	.admin-navigation {
		width: 200px;
		height: calc(100vh);
		position: fixed;
	}

	.tac {
		height: 100% !important;
		background-color: #666666;
	}
</style>
