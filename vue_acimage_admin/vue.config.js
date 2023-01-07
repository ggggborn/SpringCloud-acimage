const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  devServer:{
	  port:8000,
	  proxy: {
	              "/api": {
	                  // 代理名称   凡是使用/api开头的地址都是用此代理
	                  target: "http://localhost:8060/", // 需要代理访问的api地址
	                  changeOrigin: true, // 允许跨域请求
	                  // pathRewrite: {
	                  //     // 重写路径，替换请求地址中的指定路径
	                  //     "^/api": "/", // 将请求地址中的/api替换为空，也就是请求地址中不会包含/api/
	                  // },
	              },
	          },
  }
}

