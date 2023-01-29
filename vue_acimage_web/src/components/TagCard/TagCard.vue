<template>
	<div>
		<el-card shadow="hover" style="border-width:2px;">
			<div slot="header" class="clearfix">
				<span>标签云</span>
				<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
			</div>
			<div class="tags-container">
				<el-tag class="hover-pointer" type="warning" @click="clickTag(10086)">动画话</el-tag>
				<el-tag class="hover-border-light hover-pointer" type="warning">动画话</el-tag>
				<el-tag class="hover-border-light hover-pointer" type="warning">动画话</el-tag>
				<el-tag class="hover-border-light hover-pointer" type="warning">动画话</el-tag>
				<el-tag class="hover-border-light hover-pointer" type="warning">动画话</el-tag>
			</div>
		</el-card>
	</div>

</template>

<script>
	export default {
		name: 'TagCard',
		props: {
			clickTag: {
				type: Function
			}
		},
		data() {
			return {
				types: ['success', 'primary', 'info', 'warning', 'danger']
			}
		},
		methods: {
			handleClick(tagId) {
				this.clickTag(tagId);
			}
		}
	}
</script>

<style scoped src="./TagCard.css">
</style>
