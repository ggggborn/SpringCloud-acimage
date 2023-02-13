<template>
	<div>
		<el-card shadow="hover" style="border-width:2px;">
			<div slot="header">
				<span class="c6" style="color: rgb(250, 100, 180);">
					<i class="el-icon-collection-tag"></i>标签
				</span>
				<!-- <img src="static/image/user-rank-header.jpg" style="width:100%;height: 80px;"/> -->
			</div>
			<div class="tags-container">
				<el-tag v-for="item in $store.state.tagList" :id="'tag-btn'+item.id" @click="handleClick(item.id)"
					class="hover-pointer" :type="$global.buttonType(item.id)" :key="item.id">{{item.label}}</el-tag>
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
				for(let tag of this.$store.state.tagList){
					document.querySelector('#tag-btn' + tag.id).style.border = ""
				}
				
				if (this.activeId == tagId) {
					this.activeId = null;
				} else {
					this.activeId = tagId
					document.querySelector('#tag-btn' + this.activeId).style.border = "1px solid blue"
				}
				
				this.clickTag(tagId)

			}
		}
	}
</script>

<style scoped src="./TagCard.css">
</style>
