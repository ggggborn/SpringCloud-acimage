<template>
	<div class="activity-content">
		<template v-for="(star,index) in stars">
			<el-divider v-if="index>0" :key="star.topic.id"></el-divider>
			<div class="activity-content-item" :key="star.id">
				<el-avatar :src="$global.PHOTO_URL()" :size="60"></el-avatar>
				<div class="activity-medium">
					<div>
						<span class="activity-title-label">收藏了</span>
						<router-link :to="$global.getTopicUrl(star.topic.id)"
							class="no-underline router-link-item activity-title">
							{{$global.omitStr(star.topic.title,20)}}
						</router-link>
						<br />
					</div>
					<br />
					<div class="activity-time">
						{{star.createTime}}
					</div>
				</div>
				<div class="activity-right">
					<el-image :src="$global.trueImageUrl(star.topic.firstImageUrl)" fit="cover"
						style="height:70px;border-radius: 3px">
					</el-image>

				</div>
			</div>
		</template>
		<!-- 分页 -->
		<div style="text-align: center;margin-top:20px;">
			<el-pagination background layout="prev, pager, next" :total="totalCount" :current-page.sync="pageNo"
				@current-change="onPageNoChange" :page-size="5">
			</el-pagination>
		</div>
	</div>
</template>

<script>
	import { pageMyStar } from '@/api/star.js'

	import { Code } from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils'
	import MessageUtils from '@/utils/MessageUtils'

	export default {
		name: 'MyTopicActivity',
		data() {
			return {
				pageNo: 1,
				totalCount: 2,
				stars: [{
						topicId: 2,
						createTime: '2022-2-22 22:22:22',
						topic: {
							title: '评论标题',
							createTime: '2022-2-22 22:22:22',
							starCount: 888,
							firstImageUrl: '',
						}
					}

				]
			}
		},
		mounted() {
			this.getStarPage(1);
		},
		methods: {
			getStarPage(pageNo) {
				let _this = this;
				pageMyStar(pageNo).then(result => {
					if (result.code == Code.OK) {
						_this.stars = result.data.dataList;
						_this.totalCount= result.data.totalCount;
					}
				})
			},
			//更新对应页
			onPageNoChange() {
				this.getStarPage(this.pageNo);
			},
		}
	}
</script>

<style src="./MyStarActivity.css" scoped>
</style>
