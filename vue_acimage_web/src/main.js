import Vue from 'vue'
import App from './App.vue'
import router from '@/router'
import store from '@/store'

import axios from 'axios'
Vue.prototype.axios = axios

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

import {Notification} from 'element-ui';
Vue.prototype.$notify = Notification;

import global from '@/utils/global.js';
Vue.prototype.$global=global

//防xss攻击
 import VueDOMPurifyHTML from 'vue-dompurify-html'
Vue.use(VueDOMPurifyHTML)

Vue.config.productionTip = false
if(process.env.VUE_APP_MOCK){
	require('../mock')
}


new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')
