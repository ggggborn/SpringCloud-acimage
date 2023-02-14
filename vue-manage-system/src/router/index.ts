import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { usePermissStore } from '../store/permiss';
import CommonUtils from '@/utils/CommonUtils'
import Home from '../views/home.vue';
import Table from '../views/table.vue'
import HomeCarousel from '../views/HomeCarousel/HomeCarousel.vue'

const routes: RouteRecordRaw[] = [
	{
		path: '/',
		redirect: '/dashboard',
	},
	{
		path: '/',
		name: 'Home',
		component: Home,
		children: [
			{
				path: '/dashboard',
				name: 'dashboard',
				meta: {
					title: '系统首页',
					permiss: '1',
				},
				component: () => import(/* webpackChunkName: "dashboard" */ '../views/dashboard.vue'),
			},
			{
				path: '/HomeCarousel',
				name: 'HomeCarousel',
				meta: {
					title: '走马灯',
					permiss: '2',
				},
				component: () => import(/* webpackChunkName: "HomeCarousel" */ '../views/HomeCarousel/HomeCarousel.vue'),
			},
			{
				path: '/role',
				name: 'role',
				meta: {
					title: '角色',
					permiss: '5',
				},
				component: () => import(/* webpackChunkName: "role" */ '../views/role/role.vue'),
			},
			{
				path: '/permission/:pageNo',
				name: 'permission',
				meta: {
					title: '权限',
					permiss: '1',
				},
				component: () => import(/* webpackChunkName: "permission" */ '../views/permission/permission.vue'),
			},
			{
				path: '/authorize',
				name: 'authorize',
				meta: {
					title: '授权',
					permiss: '1',
				},
				component: () => import(/* webpackChunkName: "authorize" */ '../views/authorize/authorize.vue'),
			},
			{
				path: '/api',
				name: 'api',
				meta: {
					title: '接口',
					permiss: '1',
				},
				component: () => import(/* webpackChunkName: "api" */ '../views/api/api.vue'),
			},
			{
				path: '/topic',
				name: 'topic',
				meta: {
					title: '话题',
					permiss: '1',
				},
				component: () => import(/* webpackChunkName: "topic" */ '../views/topic/topic.vue'),
			},
			{
				path: '/user',
				name: 'user',
				meta: {
					title: '用户',
					permiss: '1',
				},
				component: () => import(/* webpackChunkName: "user" */ '../views/user/user.vue'),
			},
			{
				path: '/comment',
				name: 'comment',
				meta: {
					title: '评论',
					permiss: '1',
				},
				component: () => import(/* webpackChunkName: "comment" */ '../views/comment/comment.vue'),
			},
		],
	},
	{
		path: '/login',
		name: 'Login',
		meta: {
			title: '登录',
		},
		component: () => import(/* webpackChunkName: "login" */ '../views/login.vue'),
	},
	{
		path: '/403',
		name: '403',
		meta: {
			title: '没有权限',
		},
		component: () => import(/* webpackChunkName: "403" */ '../views/403.vue'),
	},
];

const router = createRouter({
	history: createWebHashHistory(),
	routes,
});

router.beforeEach((to, from, next) => {
	document.title = `${to.meta.title} | vue-manage-system`;
	const token = localStorage.getItem('token');

	if (!!CommonUtils.isEmpty(token) && to.path !== '/login') {
		next('/login');
	} /*else if (to.meta.permiss && !permiss.key.includes(to.meta.permiss)) {
        // 如果没有权限，则进入403
        next('/403');
    }*/ else {
		next();
	}
});

export default router;
