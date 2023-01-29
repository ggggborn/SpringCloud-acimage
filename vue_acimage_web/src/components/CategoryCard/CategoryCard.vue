<template>
	<div>
		<el-card shadow="hover" style="border-width:2px;">
			<div slot="header" class="clearfix">
				<span>分类</span>
				<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
			</div>
			<div class="categories-container">
				<el-button size="small" type="success" plain @click="handleClick(5)">动画话xxx</el-button>
				<el-button size="small">动画话阿法第三方</el-button>
				<el-button size="small">动画话阿达</el-button>
				<el-button size="small">动画话</el-button>
				<el-button size="small">动画话</el-button>
			</div>
		</el-card>
	</div>

</template>

<script>
	export default {
		name: 'CategoryCard',
		props: {
			clickCategory: {
				type: Function
			}
		},
		data() {
			return {
				types: ['success','primary','info','warning','danger']
			}

		},
		methods: {
			handleClick(categoryId) {
				this.clickCategory(categoryId);
			}
		}
	}
</script>

<style scoped src="./CategoryCard.css">
</style>
