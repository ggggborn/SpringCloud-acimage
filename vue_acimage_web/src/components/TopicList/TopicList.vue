<template>
	<div class="topic-list-div">
		<el-card shadow="hover" style="border-width: 2px;">
			<div slot="header">
				<span class="c6">{{label}}</span>
			</div>
			<div>
				<template v-for="(item,index) in topics">
					<div :key="item.id" style="font-size: 14px;position: relative;margin-bottom: 5px;">
						<template v-if="medalMode&&index<3">
							<i :class="medalClass(index)" :style="medalColor(index)"></i>
						</template>
						<el-link :href="'/#/topic/'+item.id">
							<template v-if="!medalMode||index>=3">
								<span class="ml7">
									{{index+1}}
								</span>
							</template>
							{{$global.omitStr(item.title,titleLimit)}}
						</el-link>
						<template v-if="showData">
							<div style="margin-left: 20px;margin-bottom: -5px;font-size: 12px;">
								<i class="el-icon-star-off"></i>{{item.starCount}}
								·<i class="el-icon-chat-dot-square"></i>{{item.commentCount}}
								· <i class="el-icon-view"></i>{{item.pageView}}
							</div>
						</template>
					</div>
				</template>


				<!-- 				<div style="font-size: 14px;position: relative;margin-bottom: 5px;">

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
				</div> -->
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
			},
			medalMode: {
				type: Boolean,
				default: false
			},
			label: {
				type: String,
				default: '热门话题'
			},
			titleLimit: {
				type: Number,
				default: 17
			},
			topics: {
				type: Array,
				default: () => {
					return [{
							id: 1,
							title: '发家史客户端卡技术大家看爱打架深三等奖安徽的',
							commentCount: 55,
							starCount: 66,
							pageView: 77
						},
						{
							id: 2,
							title: '发家史客户端卡技术大家看爱打架深',
							commentCount: 55,
							starCount: 66,
							pageView: 77
						},
						{
							id: 3,
							title: '发家史客户端卡技术大家看爱打架深',
							commentCount: 55,
							starCount: 66,
							pageView: 77
						},
						{
							id: 4,
							title: '发家史客户端卡技术大家看爱打架深',
							commentCount: 55,
							starCount: 66,
							pageView: 77
						}
					]
				}
			}
		},
		computed: {
			medalColor() {
				return (index) => {
					if (index == 0) {
						return { color: 'gold' };
					} else if (index == 1) {
						return { color: 'silver' };
					} else if (index == 2) {
						return { color: '#D38B0D' }
					}
					return {};
				};
			},
			medalClass() {
				return (index) => {
					const commonClass = 'icon-medal';
					if (index == 0) {
						return 'el-icon-medal-1' + ' ' + commonClass;
					} else if (index == 1 || index == 2) {
						return 'el-icon-medal' + ' ' + commonClass;
					}
					return {};
				}
			}

		},
		methods: {
			clickTopicLink(id){
				this.$router.push({path:'/topic/'+id});
			}
		}
	}
</script>

<style scoped src="./TopicList.css">
</style>
