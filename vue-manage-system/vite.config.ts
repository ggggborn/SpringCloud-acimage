import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import VueSetupExtend from 'vite-plugin-vue-setup-extend';
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import { resolve } from 'path'

export default defineConfig({
	base: './',
	plugins: [
		vue(),
		VueSetupExtend(),
		AutoImport({
			resolvers: [ElementPlusResolver()]
		}),
		Components({
			resolvers: [ElementPlusResolver()]
		})
	],
	resolve: {
		alias: {
			'@': resolve('src')
		}
	},
	optimizeDeps: {
		include: ['schart.js']
	},
	server: {
		port: 7010,
		proxy: {
			"/api": {
				// 代理名称   凡是使用/api开头的地址都是用此代理
				target: "http://127.0.0.1:81/", // 需要代理访问的api地址
				changeOrigin: true, // 允许跨域请求
				// pathRewrite: {
				//     // 重写路径，替换请求地址中的指定路径
				//     "^/api": "/", // 将请求地址中的/api替换为空，也就是请求地址中不会包含/api/
				// },
			},
		},
	}
});
