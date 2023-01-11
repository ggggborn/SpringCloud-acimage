<template>
	<div id="app">
		<router-view></router-view>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'

	export default {
		name: 'App',
		created() {
			// this.$store.commit('init');
			this.$store.commit('setToken',"xxxx");
		}
	}
</script>

<style>
	body {
		margin: 0px;
	}

	/* 解决tinymce弹出框在el-dialog中层级太低被遮盖 */
	.tox-tinymce-aux {
		z-index: 9999 !important;
	}

	/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
} */
</style>
