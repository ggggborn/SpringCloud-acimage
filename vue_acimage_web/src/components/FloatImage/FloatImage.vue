<template>
	<div class="container">
		<el-image :src="imageUrl" fit="cover" class="image" lazy>
		</el-image>
		<div class="title">
			{{$global.omitStr(title,20)}}
			<!-- 			<div style="float:right;">
				<i class="el-icon-view" style="font-size:10px;"></i>
				<div class="scan-in-title">{{pageView}}</div>
			</div> -->
		</div>
		<div class="info">
			<el-avatar :src="photoUrl" :size="20"></el-avatar>

			<div class="username">{{$global.omitStr(username,8)}}</div>
			<div style="margin-top:-5px;">
				<div class="time">
					<i class="el-icon-time time-icon-color"></i>
					<span>{{$global.timeView(createTime)}}</span>
				</div>
				<div class="info-star">
					<div class="info-star-icon">
						<i class="el-icon-star-on" style="font-size:16px;color:orange;"></i>
					</div>
					<div class="info-star-num">
						<span style="font-size:10px;">{{star}}</span>
					</div>
				</div>
			</div>



		</div>
	</div>
</template>

<script>
	export default {
		name: 'FloatImage',
		props: {
			title: {
				default: '加载中'
			},
			username: {
				default: '加载中'
			},
			createTime: {
				default: '2022-9-26 20:22:22'
			},
			star: {
				default: 888
			},
			pageView: {
				defaut: 88888
			},
			photoUrl: {
				default: 'static/image/0.jpeg'
			},
			imageUrl: {
				default: 'static/image/0.jpeg'
			}
		},
		computed: {
			timeFromNow() {
				let d1 = new Date(this.createTime);
				let d2 = new Date();
				let days = d2.getTime() - d1.getTime();
				let time = parseInt(days / (1000 * 60 * 60 * 24));
				if (time >= 1) {
					return time + '天前';
				}

				time = parseInt(days / (1000 * 60 * 60));
				if (time >= 1) {
					return time + '小时前'
				}

				time = parseInt(days / (1000 * 60));
				if (time >= 1) {
					return time + '分钟前'
				}

				time = parseInt(days / (1000));
				if (time >= 1) {
					return time + '秒前'
				}

				return '刚刚'
			}
		}
	}
</script>

<style src="./FloatImage.css" scoped>
</style>
