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
			AdminNavigation,
		},
		created() {
			// this.$store.commit('init');
			this.$store.commit('init');
		}
	}
</script>

<style>
	
	body{
		margin:0px;
	}
	
	.wrapper-right{
		margin-left:145px;
		display: inline-block;
		vertical-align: top;
	}
	

</style>
