import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)


import Home from '@/views/Home/Home.vue'
import Login from '@/views/Login/Login.vue'
import PublishTopic from '@/views/PublishTopic/PublishTopic.vue'
import MyProfile from '@/views/MyProfile/MyProfile.vue'
import MyActivity from '@/views/MyActivity/MyActivity.vue'
import MyMessage from '@/views/MyMessage/MyMessage.vue'
import Forum from '@/views/Forum/Forum.vue'
import About from '@/views/About/About.vue'
import SearchImage from '@/views/SearchImage/SearchImage.vue'
import SearchTopic from '@/views/SearchTopic/SearchTopic.vue'


const router = new VueRouter({
	routes: [
		{
			path: '/',
			component: Home
		},
		{
			path: '/login',
			component: Login
		},
		{
			path: '/publish',
			component: PublishTopic
		},
		{
			path: '/topic/:id',
			component: ()=>import('@/views/TopicInfo/TopicInfo.vue')
		},
		{
			path: '/profile',
			component: MyProfile
		},
		{
			path: '/MyActivity',
			component: MyActivity
		},
		{
			path: '/MyMessage',
			component: MyMessage
		},
		{
			path: '/forum',
			component: Forum
		},
		{
			path: '/SearchTopic',
			component: SearchTopic
		},
		{
			path: '/SearchImage',
			component: SearchImage
		},
		{
			path: '/about',
			component: About
		},

		
	]
})

export default router
