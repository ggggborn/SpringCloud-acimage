<template>
	<div>
		<div class="wrapper hover-pointer" @click="toTopic">
			<div class="wrapper-left">
				<el-image :src="coverImageUrl" fit="cover" lazy></el-image>
				<div class="white-gradient">
				</div>
			</div>
			<div class="wrapper-medium">
				<div class="title">
					<div v-if="titleHtml" v-dompurify-html="title"></div>
					<span v-else>{{title}}</span>
					<!-- {{$global.omitStr(title)}} -->
				</div>
				<div class="content" v-if="contentHtml" v-dompurify-html="$global.omitStr(html,contentLimit)"> </div>
				<div class="content" v-else>
					{{$global.omitStr(html,contentLimit)}}
				</div>
				<div class="info">
					<div style="display: inline-block;">
						<el-avatar :size="20" :src="photoUrl">
						</el-avatar>
					</div>
					<div class="username">
						{{username}}
					</div>
					<div class="data">
						{{timeLabel}} {{$global.timeView(updateTime)}}
						<template v-if="starCount != null">
							·
							<i class="el-icon-star-off"></i>
							{{starCount}}
						</template>
						<template v-if="commentCount != null">
							·
							<i class="el-icon-chat-dot-square"></i>
							{{commentCount}}
						</template>
						<template v-if="pageView != null">
							·
							<i class="el-icon-view"></i>
							{{pageView}}
						</template>
					</div>
				</div>
			</div>
			<div class="wrapper-right">
				<div class="category-container">
					<el-tag effect="plain" :type="$global.buttonType(categoryId)" v-if="!$global.isEmpty(categoryId)">
						{{$store.getters.categoryLabel(categoryId)}}
					</el-tag>
				</div>
				<div class="tags-container">
					<template v-for="id in tagIds">
						<el-tag v-if="!$global.isEmpty($store.getters.tagLabel(id))" effect="plain"
							:type="$global.buttonType(id)" :key="id">
							{{$store.getters.tagLabel(id)}}
						</el-tag>
					</template>
				</div>
			</div>
		</div>
	</div>

</template>

<script>
	export default {
		name: "TopicCard",
		props: {
			title: {
				type: String,
				default: 'sadhasdj'
			},
			content: {
				type: String,
			},
			html: {
				type: String,
			},
			contentLimit: {
				type: Number,
				default: 500
			},
			username: {},
			updateTime: {},
			timeLabel:{
				default:'更新于'
			},
			starCount: {},
			commentCount: {},
			pageView: {},
			photoUrl: {},
			coverImageUrl: {},
			tagIds: {
				type: Array
			},
			categoryId: {
				type: Number
			},
			to: {},
			showImage: {
				type: Boolean,
				default: true
			},
			titleHtml: {
				type: Boolean,
				default: false
			},
			contentHtml: {
				type: Boolean,
				default: false
			}

		},
		methods: {
			toTopic() {
				this.$router.push({ path: this.to });
			}
		}
	}
</script>

<style src="./TopicCard.css" scoped>
</style>
