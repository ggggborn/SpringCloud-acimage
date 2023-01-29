<template>
	<div class="topic-list-div">
		<el-card shadow="hover">
			<div slot="header">
				<span class="c6">热门话题</span>
			</div>
			<div>
				<div style="font-size: 14px;position: relative;margin-bottom: 5px;">

					<i class="el-icon-medal-1" style="color: #99999;font-size: 20px;position: relative;top:4px;"></i>
					<el-link href="/#/publish">
						发家史客户端卡技术大家看爱打架深
					</el-link>
					<template v-if="true">
						<div style="margin-left: 20px;margin-bottom: -5px;">
							<i class="el-icon-chat-dot-square"></i>5
							· <i class="el-icon-star-off"></i>6
							· <i class="el-icon-view"></i>9
						</div>

					</template>
				</div>

				<div style="font-size: 14px;position: relative;">

					<i class="el-icon-medal" style="color: #99999;font-size: 20px;position: relative;top:4px;"></i>
					<el-link href="/#/publish">
						发家史客户端卡技术大家看爱打架深
					</el-link>
					<template v-if="false">
						· <i class="el-icon-chat-dot-square"></i>5
						· <i class="el-icon-star-off"></i>6
						· <i class="el-icon-view"></i>9
					</template>
				</div>
			</div>
		</el-card>
	</div>

</template>

<script>
	export default {
		name: 'TopicList',
		props: {
			showData: {
				type: Boolean,
				default: true
			}
		},
		methods: {

		}
	}
</script>

<style scoped src="./TopicList.css">
</style>
