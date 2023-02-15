<template>
	<div>
		<!-- <el-card shadow="hover" style="border-width: 2px;"> -->
		<div class="wrapper">
			<div class="header">
				<div style="height: 100px;">
					<img :src="headerSrc" />
					<div class="white-gradient"></div>
				</div>

				<div :id="'header-item'+index" v-for="(label,index) in labels" :key="index" :style="headerItemStyle"
					@mouseenter="activeIndex=index" class="header-item hover-pointer hover-bg-red">
					<div class="label">{{label}}</div>
				</div>
			</div>
			<div class="body">
				<div v-for="(topic,index) in topics" :key="topic.id" class="item-container">
					<template v-if="index<endIndex">
						<div class="image-container">
							<el-image fit="cover" lazy
								:src="topic.coverImageUrl"></el-image>
							<div class="index image-index">
								{{index+1}}
							</div>
						</div>
						<div class="front-title">
							<el-link :href="'/#/topic/'+topic.id" :underline="false">
								{{$global.omitStr(topic.title,titleLimit+6)}}
							</el-link>
							<br />
							<span style="color:#666666;font-size: 12px;">
								<i class="el-icon-star-off"></i>{{topic.starCount}}
								· <i class="el-icon-chat-dot-square"></i>{{topic.commentCount}}
								· <i class="el-icon-view"></i>{{topic.pageView}}
							</span>
						</div>
					</template>
					<template v-else>
						<el-link :href="'/#/topic/'+topic.id" :underline="false">
							<div class="index" style="background-color:#CCCCCC;display:inline-block;">
								{{index+1}}
							</div>
							{{$global.omitStr(topic.title,titleLimit)}}
						</el-link>
					</template>
				</div>
			</div>
			<!-- </el-card> -->
		</div>
	</div>

</template>

<script>
	export default {
		name: 'TopicList',
		props: {
			handleTabHover:{
				type:Function
			},
			endIndex: {
				default: 3
			},
			headerSrc:{
				default:'static/image/topic-rank.webp'
			},
			showData: {
				type: Boolean,
				default: true
			},
			labels: {
				type: Array,
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
							pageView: 77,
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
		data() {
			return {
				activeIndex: null
			}
		},
		mounted() {
			this.activeIndex = 0
			document.querySelector("#header-item0").style.backgroundColor = "#FF566A"
		},
		watch: {
			activeIndex: {
				handler(newVal, oldVal) {
					for (let i = 0; i < this.labels.length; i++) {
						document.querySelector("#header-item" + i).style.backgroundColor = "#CCCCCC";
					}
					document.querySelector("#header-item" + newVal).style.backgroundColor = "#FF566A";
					this.handleTabHover(this.activeIndex)
				},
				immediate: false
			}
		},
		computed: {
			headerItemStyle() {
				let width = 100 / this.labels.length;
				return { width: width + '%' };
			},
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

<style scoped src="./TopicRank.css">
</style>
