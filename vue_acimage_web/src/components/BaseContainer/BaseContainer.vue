<template>
	<div>
		<div class="container">
			<div class="header">
				<i :class="iconClass" :style="iconStyle"></i>
				<div class="header-title">{{title}}</div>
				<div style="margin-top:-20px;">
					<el-divider direction="horizontal"></el-divider>
				</div>
			</div>
			<slot></slot>
		</div>
	</div>
</template>

<script>
	export default {
		name: "BaseContainer",
		props: {
			icon: {
				default: 'el-icon-discover'
			},
			title: {},
			iconColor: {
				default: "#E5E544"
			}
		},
		computed: {
			iconClass() {
				return this.icon + ' header-icon'
			},
			iconStyle() {
				return {color:this.iconColor}
			}
		}
	}
</script>

<style src="./BaseContainer.css" scoped>
</style>
