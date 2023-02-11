<template>
	<div class="category-card-div">
		<el-card shadow="hover">
			<div slot="header">
				<span style="color:#FF9300">
					<i class="el-icon-c-scale-to-original" />分类
				</span>
			</div>
			<div class="categories-container">
				<el-button :id="'btn'+item.id" v-for="item in $store.state.categoryList" size="small"
					:type="$global.buttonType(item.id)" plain @click="handleClick(item.id)" :key="item.id">
					{{item.label}}
				</el-button>
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
		},
		data() {
			return {
				activeId: null
			}
		},
		methods: {
			handleClick(categoryId) {
				if (this.activeId == categoryId) {
					this.activeId = null;
					document.querySelector('#btn' + categoryId).blur()
					// document.querySelector('#btn'+categoryId).mouseleave()
				} else {
					this.activeId = categoryId
				}


				this.clickCategory(categoryId);
			}
		}
	}
</script>

<style scoped src="./CategoryCard.css">
</style>
