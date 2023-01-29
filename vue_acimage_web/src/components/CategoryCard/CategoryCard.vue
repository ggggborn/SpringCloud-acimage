<template>
	<div class="category-card-div">
		<el-card shadow="hover" >
			<div slot="header">
				<span class="c6">分类</span>
				<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
			</div>
			<div class="categories-container">
				<el-button v-for="item in $store.state.categoryList" size="small" :type="$global.buttonType(item.id)"
					plain @click="handleClick(item.id)" :key="item.id">{{item.label}}</el-button>
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
			},
			// width: {
			// 	type: Number,
			// 	default: 298,
			// }
		},
		// computed: {
		// 	myStyle() {
		// 		return { width: this.width + 'px' };
		// 	}
		// },
		methods: {
			handleClick(categoryId) {
				this.clickCategory(categoryId);
			}
		}
	}
</script>

<style scoped src="./CategoryCard.css">
</style>
