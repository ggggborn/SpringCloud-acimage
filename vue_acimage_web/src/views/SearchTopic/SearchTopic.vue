<template>
	<div>
		<my-header></my-header>
		<div class="wrapper">
			<div class="wrapper-top">
				<el-form ref="form" label-width="120px" style="width: 900px;" :model="query">
					<el-form-item label="关键词">
						<el-input @keydown.native.enter="submitSearch" style="width:300px;" v-model="query.search"
							prefix-icon="el-icon-edit-outline" maxlength="15" clearable></el-input>
						<el-button @click="submitSearch" style="margin-left:30px;" type="primary" plain>提交搜索</el-button>
					</el-form-item>
					<el-form-item label="分类">
						<el-select v-model="query.categoryId" placeholder="选择分类" clearable>
							<el-option v-for="item in $store.state.categoryList" :label="item.label" :value="item.id"
								:key="item.id">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="标签">
						<el-tag v-if="query.tagId!=null" class="hover-pointer mr5" @close="query.tagId=null"
							:type="$global.buttonType(query.tagId)" closable>
							{{$store.getters.tagLabel(query.tagId)}}
						</el-tag>
						<br />
						<div>
							<el-tag v-for="item in $store.state.tagList" :key="item.id"
								:type="$global.buttonType(item.id)" @click="query.tagId=item.id"
								class="hover-pointer mr5">
								{{item.label}}
							</el-tag>
						</div>
					</el-form-item>
					<el-form-item label="排序">
						<el-tabs v-model="query.sortMode" tab-position="top" style="width:380px;"
							@tab-click="handleTabClick">
							<el-tab-pane v-for="(label,index) in sortLabels" :label="label" :name="sortKeys[index]"
								:key="label">
							</el-tab-pane>
						</el-tabs>
					</el-form-item>
				</el-form>

				<div style="margin-left: 120px;">
					<el-skeleton v-if="loading" :rows="6" animated />
					<el-empty v-else-if="$global.isEmpty(topics)" image="static/image/sad.jpeg"
						description="没有你想要的结果 ┑(￣Д ￣)┍"></el-empty>
					<div v-else v-for="topic in topics" :key="topic.id" style="margin-bottom: 5px;">
						<topic-card :title="topic.title" :html="topic.content" :updateTime="topic.activityTime"
							:starCount="topic.starCount" :commentCount="topic.commentCount" :pageView="topic.pageView"
							:username="topic.user.username" :photoUrl="$global.getPhotoUrl(topic.user.photoUrl)"
							:to="$global.getTopicUrl(topic.id)" :categoryId="topic.categoryId" :tagIds="topic.tagIds"
							:coverImageUrl="topic.coverImageUrl" titleHtml contentHtml>
						</topic-card>
					</div>
				</div>
				<div style="text-align: center;">
					<el-pagination background layout="prev, pager, next" :total="totalCount"
						:current-page.sync="curPage" @current-change="handlePageChange" :page-size="10">
					</el-pagination>
				</div>
			</div>

		</div>
	</div>
</template>

<script>
	import MyHeader from '@/components/MyHeader/MyHeader.vue'
	import TopicCard from '@/components/TopicCard/TopicCard.vue'

	import { Code } from '@/utils/result.js'
	import MessageUtils from '@/utils/MessageUtils'
	import StringUtils from '@/utils/StringUtils'
	import CommonUtils from '@/utils/CommonUtils'

	import { pageRencentTopic } from '@/api/topic.js'
	import { searchTopics } from '@/api/TopicSearch'


	let sortModes = {
		NORMAL: '相关性',
		CREATE_TIME: '创建时间',
		STAR_COUNT: 'star',
		COMMENT_COUNT: '评论数',
		PAGE_VIEW: '浏览量',
	};

	export default {
		name: 'SearchTopic',
		components: {
			MyHeader,
			TopicCard
		},
		data() {
			return {
				curPage: 1,
				totalCount: 1,
				loading: false,
				query: {
					search: '',
					pageNo: 1,
					categoryId: null,
					tagId: null,
					sortMode: 'NORMAL'
				},
				topics: []
			};
		},
		computed: {
			sortKeys() {
				return Object.keys(sortModes);
			},
			sortLabels() {
				let keys = this.sortKeys;
				let labels = [];
				for (let key of keys) {
					labels.push(sortModes[key])
				}
				return labels;
			},
		},
		watch: {
			'$route'(to, from) {
				// CommonUtils.copyPropertiesTo(this.$route.query, this.query)
				// if (!CommonUtils.isEmpty(this.query.search.trim()) ||
				// 	this.query.pageNo != 1 ||
				// 	this.query.categoryId != null ||
				// 	this.query.tagId != null ||
				// 	this.query.sortMode != 'NORMAL') {
				// 	if (to.path == from.path) {
				// 		this.toSearch();
				// 	}
				// }
			},
		},
		mounted() {
			CommonUtils.copyPropertiesTo(this.$route.query, this.query)
			if (!CommonUtils.isEmpty(this.query.search.trim()) ||
				this.query.pageNo != 1 ||
				this.query.categoryId != null ||
				this.query.tagId != null ||
				this.query.sortMode != 'NORMAL') {

				this.toSearch();

			}
		},
		methods: {
			handleTabClick(tab) {
				this.query.sortMode = this.sortKeys[tab.index];
			},
			handlePageChange() {
				this.query.pageNo = this.curPage;
				this.toSearch();
			},
			submitSearch() {
				if (JSON.stringify(this.query) != JSON.stringify(this.$route.query)) {
					this.$router.replace({ query: this.query })
					this.toSearch();
				} else {
					MessageUtils.notice("请改变搜索选项")
				}

			},
			toSearch() {
				let _this = this;
				if (CommonUtils.isEmpty(this.query.categoryId)) {
					this.query.categoryId = null;
				}
				this.loading = true;
				// let searchForm = CommonUtils.toFormData(this.query);
				searchTopics(_this.query).then(res => {
					if (res.code == Code.OK) {
						MessageUtils.success("搜索成功", 1);
						_this.topics = res.data.dataList;
						_this.totalCount = res.data.totalCount;
					}
					this.loading = false;
				});
			}
		}
	}
</script>

<style src="./SearchTopic.css" scoped>
</style>
