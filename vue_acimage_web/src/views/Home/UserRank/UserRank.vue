<template>
	<div>
		<div class="wrapper">
			<div class="wrapper-header">
				<img src="static/image/user-rank-header.webp" />
				<div class="white-gradient">
					<div class="header-title">用户排行</div>
				</div>
			</div>
			<el-tabs type="card" style="margin-top:-5px;" v-model="query.column">
				<el-tab-pane label="星星收割者" name="starCount"></el-tab-pane>
				<el-tab-pane label="话题达人" name="topicCount"></el-tab-pane>
			</el-tabs>

			<el-skeleton v-if="loading" :rows="6" animated style="width: 90%;margin-left:5%" />
			<div v-else v-for="user in users" class="user-item-container" :key="user.id">
				<div class="user-item-left">
					<el-avatar :size="40" :src="$global.getPhotoUrl(user.photoUrl)"></el-avatar>
				</div>
				<div class="user-item-right">
					<div class="user-item-right-header">
						{{user.username}}
					</div>
					<div class="user-item-right-bottom">
						<div>{{user.starCount}}</div> <span>star</span>
						<div>{{user.topicCount}}</div> <span>话题</span>
					</div>
				</div>
			</div>
			<div style="text-align: center;">
				<el-pagination v-if="users.length>0" layout="prev, pager, next" :total="totalCount"
					:current-page.sync="curPage" @current-change="handlePageNoChange" :page-size="10">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	import { pageUserRankBy } from '@/api/UserRank.js'
	import { Code } from '@/utils/result.js'

	export default {
		name: 'UserRank',
		data() {
			return {
				curPage: 1,
				query: {
					pageNo: 1,
					column: 'starCount'
				},
				totalCount: 0,
				loading: true,
				users: []
			}
		},
		watch: {
			query: {
				handler(newVal, oldVal) {
					this.getUserRankPage();
				},
				deep: true
			}
		},
		created() {
			this.getUserRankPage();
		},
		methods: {
			getUserRankPage() {
				let _this = this;
				pageUserRankBy(this.query).then(res => {
					if (res.code == Code.OK) {
						_this.users = res.data.dataList;
						_this.totalCount = res.data.totalCount;
						_this.loading = false;
					}
				});
			},
			handlePageNoChange() {
				this.query.pageNo = this.curPage;
			}
		}
	}
</script>

<style src="./UserRank.css" scoped>
</style>
