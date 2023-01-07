<template>
	<div></div>
</template>

<script>
	export default {
		name:'MyGlobal',
		methods:{
			popupHint(msg) {
				this.$notify({
					title: '提示',
					message: msg,
					type: 'warning'
				});
			}
		}
	}
</script>

<style>
</style>