<template>
	<div class="mask-image-container">
		<div style="" class="image-container">
			<img :src="imageUrl" width="250px" height="150px" style="object-fit:cover;object-position:center" />
		</div>
		<div class="image-mask">
			<div class="image-info" style="margin-top:10px;">
				{{$global.omitStr(title,18)}}
			</div>
			<div class="image-info image-info-bottom">
				<el-row style="margin-bottom: 5px;">
					作者：{{$global.omitStr(username,8)}}
				</el-row>
				<el-row>
					<el-col :span="12">
						<i class="el-icon-star-off"></i>{{starCount}}
					</el-col>

					<el-col :span="12">
						<i class="el-icon-view"></i>{{pageView}}
					</el-col>
				</el-row>
			</div>
		</div>
	</div>
</template>

<script>
	export default{
		name:'MaskImage',
		props:{
			title:{
				default:'加载中...'
			},
			username:{
				default:'加载中...'
			},
			createTime:{
				default:'2022-9-26 20:22:22'
			},
			starCount:{
				default:888
			},
			pageView:{
				defaut:88888
			},
			imageUrl:{
				default:'static/image/0.jpeg'
			}
		}
	}
</script>

<style src="./MaskImage.css" scoped>
</style>
