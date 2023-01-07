import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)


import Home from '@/views/Home/Home.vue'
import Login from '@/views/Login/Login.vue'
import PublishTopic from '@/views/PublishTopic/PublishTopic.vue'
import Topic from '@/views/Topic/Topic.vue'
import MyProfile from '@/views/MyProfile/MyProfile.vue'
import MyActivity from '@/views/MyActivity/MyActivity.vue'
import Forum from '@/views/Forum/Forum.vue'
import SearchImage from '@/views/SearchImage/SearchImage.vue'



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
			component: Topic
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
			path: '/forum',
			component: Forum
		},
		{
			path: '/SearchImage',
			component: SearchImage
		},

		
	]
})

export default router
