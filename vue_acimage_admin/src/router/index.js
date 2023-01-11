import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)



const router = new VueRouter({
	routes: [
		{
			path: '/home/carousel',
			component: ()=> import('@/views/HomeCarousel/HomeCarousel.vue')
		},	
		{
			path: '/auth',
			component: ()=> import('@/views/AuthManage/AuthManage.vue')
		},	
	]
})

export default router
