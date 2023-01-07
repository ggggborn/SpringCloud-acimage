import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

import HelloWorld from '@/components/HelloWorld.vue'
import HomeCarousel from '@/views/HomeCarousel/HomeCarousel.vue'

const router = new VueRouter({
	routes: [
		{
			path: '/hello',
			component: HelloWorld
		},
		{
			path: '/home/carsoul',
			component: HomeCarousel
		}
	]
})

export default router
