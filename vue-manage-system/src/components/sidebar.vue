<template>
	<div class="sidebar">
		<el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="sidebar.collapse"
			background-color="#324157" text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
			<template v-for="item in items">
				<template v-if="item.subs">
					<el-sub-menu :index="item.index" :key="item.index" v-permiss="item.permiss">
						<template #title>
							<el-icon>
								<component :is="item.icon"></component>
							</el-icon>
							<span>{{ item.title }}</span>
						</template>
						<template v-for="subItem in item.subs">
							<el-sub-menu v-if="subItem.subs" :index="subItem.index" :key="subItem.index"
								v-permiss="item.permiss">
								<template #title>{{ subItem.title }}</template>
								<el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
									{{ threeItem.title }}
								</el-menu-item>
							</el-sub-menu>
							<el-menu-item v-else :index="subItem.index" v-permiss="item.permiss">
								{{ subItem.title }}
							</el-menu-item>
						</template>
					</el-sub-menu>
				</template>
				<template v-else>
					<el-menu-item :index="item.index" :key="item.index" v-permiss="item.permiss">
						<el-icon>
							<component :is="item.icon"></component>
						</el-icon>
						<template #title>{{ item.title }}</template>
					</el-menu-item>
				</template>
			</template>
		</el-menu>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';
	import { useSidebarStore } from '../store/sidebar';
	import { useRoute } from 'vue-router';


	const items: any = [{
			icon: 'Odometer',
			index: '/dashboard',
			title: '系统首页',
			permiss: '1',
		},
		{
			icon: 'Calendar',
			index: '1',
			title: '首页管理',
			permiss: '2',
			subs: [{
				index: '/HomeCarousel',
			 title: '走马灯',
				permiss: '2',
			}, ],
		},
		{
			icon: 'Calendar',
			index: '2',
			title: '社区管理',
			permiss: '2',
			subs: [{
					index: '/topic',
					title: '话题',
					permiss: '2',
				},
				{
					index: '/comment',
					title: '评论',
					permiss: '2',
				},
				{
					index: '/user',
					title: '用户',
					permiss: '2',
				},
			],
		},
		{
			icon: 'Edit',
			index: '3',
			title: '权限管理',
			permiss: '4',
			subs: [{
					index: '/role',
					title: '角色',
					permiss: '1',
				},
				{
			  index: '/permission/1',
					title: '权限',
					permiss: '1',
				},
				{
					index: '/authorize',
					title: '授权',
					permiss: '1',
				},
			],
		},
		{
			icon: 'Edit',
			index: '34',
			title: '接口管理',
			permiss: '4',
			subs: [{
				index: '/api',
				title: '接口',
				permiss: '1',
			}, ],
		},
	];

	const route = useRoute();
	const onRoutes = computed(() => {
		return route.path;
	});

	const sidebar = useSidebarStore();
</script>

<style scoped>
	.sidebar {
		display: block;
		position: absolute;
		left: 0;
		top: 70px;
		bottom: 0;
		overflow-y: scroll;
	}

	.sidebar::-webkit-scrollbar {
		width: 0;
	}

	.sidebar-el-menu:not(.el-menu--collapse) {
		width: 250px;
	}

	.sidebar>ul {
		height: 100%;
	}
</style>
