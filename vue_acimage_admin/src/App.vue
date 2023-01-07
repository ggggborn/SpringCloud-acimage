<template>
	<div id="app">
		<admin-navigation></admin-navigation>
		<div class="wrapper-right">
			<router-view></router-view>
		</div>
		
	</div>
</template>

<script>
	import AdminNavigation from '@/components/AdminNavigation/AdminNavigation.vue'

	export default {
		name: 'App',
		components: {
			AdminNavigation
		}
	}
</script>

<style>
/* 	#app {
		font-family: Avenir, Helvetica, Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
		text-align: center;
		color: #2c3e50;
	} */
	
	body{
		margin:0px;
	}
	
	.wrapper-right{
		margin-left:210px;
		display: inline-block;
		vertical-align: top;
	}
	

</style>
