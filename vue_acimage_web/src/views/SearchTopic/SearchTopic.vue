<template>
	<div>
		<my-header></my-header>
		<div class="wrapper">
			<div class="wrapper-top">
				<el-form ref="form" label-width="120px" style="width: 900px;" :model="query">
					<el-form-item label="关键词">
						<el-input style="width:300px;" v-model="query.search" prefix-icon="el-icon-edit-outline"
							maxlength="15" clearable></el-input>
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
						<el-tabs tab-position="top" style="width:410px;" @tab-click="handleTabClick">
							<el-tab-pane v-for="(label,index) in sortLabels" :label="label" :key="index"></el-tab-pane>
						</el-tabs>
					</el-form-item>
				</el-form>
				<div style="margin-left: 120px;">
					<div v-for="topic in topics" :key="topic.id" style="margin-bottom: 5px;">
						<topic-card :title="topic.title" :html="topic.content" :updateTime="topic.activityTime"
							:starCount="topic.starCount" :commentCount="topic.commentCount" :pageView="topic.pageView"
							:username="topic.user.username" :photoUrl="topic.user.photoUrl" :to="$global.getTopicUrl(topic.id)"
							:categoryId="topic.categoryId" :tagIds="topic.tagIds" :coverImageUrl="topic.coverImageUrl"
							titleHtml contentHtml>
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
	import { searchTopicsAsPage } from '@/api/TopicSearch'

	let SortBy = {
		NORMAL: 'NORMAL',
		TIME: 'TIME',
		STAR_COUNT: 'STAR_COUNT',
		PAGE_VIEW: 'PAGE_VIEW',
		COMMENT_COUNT: 'COMMENT_COUNT'
	}
	let sortModes = [SortBy.NORMAL, SortBy.TIME, SortBy.STAR_COUNT, SortBy.COMMENT_COUNT, SortBy.PAGE_VIEW];

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
				query: {
					search: '',
					pageNo: 1,
					categoryId: null,
					tagId: null,
					sortBy: SortBy.NORMAL
				},
				sortLabels: ['相关性', '时间', 'star', '评论数', '浏览量'],
				topics: []
			};
		},
		watch: {
			query: {
				handler(newVal, oldVal) {
					console.log('query新值' + this.query.search)
				},
				deep: true
			}
		},
		created() {
			this.getRecentTopicPage();
		},
		methods: {
			handleTabClick(tab) {
				this.query.sortBy = sortModes[tab.index];
			},
			handlePageChange() {
				this.query.pageNo = this.curPage;
				this.submitSearch();
			},
			validateForm() {
				let text = this.$refs['editBoard'].Html.toString();
				// text.replace(new RegExp("</?[^>]+>", "gm"), "");
				// // eslint-disable-next-line
				// text.replace(new RegExp("<a>\\s*|\t|\r|\n</a>", "gm"), "");
				let pureText = StringUtils.html2Text(text);
				console.log(pureText)
				if (pureText.trim().length <= 4) {
					MessageUtils.notice("内容至少要四个字符");
					return false;
				} else if (text.length > 5000) {
					MessageUtils.notice("文本过长，请缩减");
					return false;
				}
				return true;
			},
			submitSearch() {
				let _this = this;
				if (CommonUtils.isEmpty(this.query.categoryId)) {
					this.query.categoryId = null;
				}
				// let searchForm = CommonUtils.getFormData(this.query);
				searchTopicsAsPage(_this.query).then(res => {
					if (res.code == Code.OK) {
						MessageUtils.success("搜索成功", 1);
						_this.topics = res.data.dataList;
						_this.totalCount = res.data.totalCount;
					}
				})
			},
			getRecentTopicPage() {
				let _this = this;
				pageRencentTopic(_this.query.pageNo)
					.then(result => {
						if (result.code == Code.OK) {
							_this.topics = result.data.dataList;
							_this.totalCount = result.data.totalCount;
							_this.loading = false;
						}
					})
			}

		}
	}
</script>

<style src="./SearchTopic.css" scoped>
</style>
