import request from '@/utils/request.js'


export let queryHomeCarousel = function() {
	return request.get('/api/image/homeCarousels/all');
}
