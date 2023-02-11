import request from '@/utils/request.js'
import axios from 'axios'
import MessageUtils from '@/utils/MessageUtils'
import global from '@/utils/global.js'


export let uploadPhoto = function(reqData) {
	let config = { 'Content-type': 'multipart/form-data' };
	return request.post('/api/image/photos/operate/upload', reqData, config);
}


