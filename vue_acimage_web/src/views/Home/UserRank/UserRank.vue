<template>
	<div class="user-rank-wrapper">
		<div class="user-rank-header">
			<i class="el-icon-office-building"></i>用户排行
		</div>
		<div class="sort-choice-container">
			<span class="sort-hint">排序方式</span>
			<el-button class="gray-color" type="text" size="mini" @click="onClickSortByStarCount">被收藏数
			</el-button>
			<el-button class="gray-color" type="text" size="mini" @click="onClickSortByTopicCount">话题数
			</el-button>
		</div>
		<template v-for="user in users">
			<div class="user-item-container" :key="user.id">
				<div class="user-item-left">
					<el-avatar :size="40" :src="$global.truePhotoUrl(user.photoUrl)">
						<img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png" />
					</el-avatar>
				</div>
				<div class="user-item-right">
					<div class="user-item-right-header">
						{{user.username}}
					</div>
					<div class="user-item-right-bottom">
						<div>{{user.starCount}}</div> <span>被收藏</span>
						<div>{{user.topicCount}}</div> <span>话题</span>
					</div>
				</div>

			</div>
		</template>
	</div>

</template>

<script>
	import { pageUserRankByTopicCount, pageUserRankByStarCount } from '@/api/UserRank.js'
	import { Code } from '@/utils/result.js'
	let Mode = {
		starCount: 1,
		topicCount: 2
	};
	export default {
		name: 'UserRank',
		data() {
			return {
				sortMode: Mode.starCount,
				users: [{
						id: 0,
						username: 'xlg',
						photoUrl: '',
						starCount: 888,
						topicCount: 888,
					},
					{
						id: 1,
						username: 'xlg',
						photoUrl: '',
						starCount: 666,
						topicCount: 777,
					}
				]
			}
		},
		watch: {
			sortMode: {
				handler(newVal) {
					if (newVal == Mode.starCount) {
						this.getUserPageRankByStarCount(1);
					} else if (newVal == Mode.topicCount) {
						this.getUserPageRankByTopicCount(1);
					}
				}
			}
		},
		mounted() {
			let _this = this;
			this.getUserPageRankByStarCount(1);
		},
		methods: {
			getUserPageRankByStarCount(pageNo) {
				let _this = this;
				pageUserRankByStarCount(pageNo).then(result => {
					if (result.code == Code.OK) {
						_this.users = result.data;
					}
				});
			},
			getUserPageRankByTopicCount(pageNo) {
				let _this = this;
				pageUserRankByTopicCount(pageNo).then(result => {
					if (result.code == Code.OK) {
						_this.users = result.data;
					}
				});
			},
			onClickSortByStarCount(){
				this.sortMode=Mode.starCount;
			},
			onClickSortByTopicCount(){
				this.sortMode=Mode.topicCount;
			},

		}
	}
</script>

<style src="./UserRank.css" scoped>
</style>
