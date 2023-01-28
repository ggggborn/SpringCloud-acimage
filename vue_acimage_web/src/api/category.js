import request from '@/utils/request.js'
import axios from 'axios'
import MessageUtils from '@/utils/MessageUtils'
import global from '@/utils/global.js'

export let queryAllCategories = function() {
	return request.get('/api/community/categories/all')
}