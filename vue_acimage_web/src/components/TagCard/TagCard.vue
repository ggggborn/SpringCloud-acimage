<template>
	<div>
		<el-card shadow="hover" style="border-width:2px;">
			<div slot="header">
				<span class="c6">标签云</span>
				<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
			</div>
			<div class="tags-container">
				<el-tag :id="'btn'+item.id" v-for="item in $store.state.tagList" @click="handlerClick(item.id)" class="hover-pointer"
					:type="$global.buttonType(item.id)" :key="item.id">{{item.label}}</el-tag>
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
				activeId: null
			}
		},
		methods: {
			handleClick(tagId) {
				// this.clickTag(tagId);
				if (this.activeId == tagId) {
					this.activeId = null;
					document.querySelector('#btn' + tagId).blur()
					// document.querySelector('#btn'+categoryId).mouseleave()
				} else {
					this.activeId = tagId
					
				}
			}
		}
	}
</script>

<style scoped src="./TagCard.css">
</style>
