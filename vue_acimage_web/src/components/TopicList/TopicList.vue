<template>
	<div class="topic-list-div">
		<el-card shadow="hover" style="border-width: 2px;">
			<div slot="header">
				<span :style="'color:'+labelColor">{{label}}</span>
			</div>
			<div>
				<div v-for="(item,index) in topics" :key="item.id" class="item-container">
					<i v-if="medalMode&&index<3" :class="medalClass(index)" :style="medalColor(index)"></i>
					<el-link :href="'/#/topic/'+item.id">
						<span v-if="!medalMode||index>=3" class="ml7">
							{{index+1}}
						</span>
						{{$global.omitStr(item.title,titleLimit)}}
					</el-link>
					<div v-if="showData" class="data-container">
						<i class="el-icon-star-off"></i>{{item.starCount}}
						·<i class="el-icon-chat-dot-square"></i>{{item.commentCount}}
						· <i class="el-icon-view"></i>{{item.pageView}}
					</div>
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
			},
			medalMode: {
				type: Boolean,
				default: false
			},
			label: {
				type: String,
				default: '热门话题'
			},
			labelColor:{
				default:'#666666'
			},
			titleLimit: {
				type: Number,
				default: 21
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
			clickTopicLink(id) {
				this.$router.push({ path: '/topic/' + id });
			}
		}
	}
</script>

<style scoped src="./TopicList.css">
</style>
