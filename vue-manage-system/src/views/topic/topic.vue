<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-select v-model="query.column" placeholder="排序字段" class="handle-select mr10" style="width:160px;">
					<el-option key="1" label="createTime" value="createTime"></el-option>
					<el-option key="2" label="updateTime" value="updateTime"></el-option>
					<el-option key="3" label="starCount" value="starCount"></el-option>
					<el-option key="4" label="commentCount" value="commentCount"></el-option>
					<el-option key="5" label="pageView" value="pageView"></el-option>
				</el-select>
				<el-input placeholder="用户id" class="handle-input mr10"></el-input>
				<el-button type="primary" @click="getData">搜索</el-button>
			</div>
			<el-table :data="topics" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column label="序号" width="55" align="center">
					<template #default="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID" width="50" align="center"></el-table-column>
				<el-table-column prop="userId" label="用户ID" width="50" align="center"></el-table-column>
				<el-table-column label="封面" align="center" width="100">
					<template #default="scope">
						<el-image class="table-td-thumb" :src="scope.row.coverImageUrl"
							:z-index="10" :preview-src-list="[scope.row.coverImageUrl]"
							preview-teleported>
						</el-image>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="标题"></el-table-column>
				<el-table-column prop="content" label="内容" width="200">
					<template #default="scope">
						{{global.omitStr(scope.row.content,100)}}
					</template>
				</el-table-column>
				<el-table-column prop="pageView" label="浏览量" width="50"></el-table-column>
				<el-table-column prop="starCount" label="star" width="50"></el-table-column>
				<el-table-column prop="commentCount" label="评论数" width="50"></el-table-column>
				<el-table-column prop="updateTime" label="更新时间" width="70"></el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="70"></el-table-column>
				<el-table-column prop="activityTime" label="活跃时间" width="70"></el-table-column>

				<el-table-column label="操作" width="250" align="center">
					<template #default="scope">
						<el-button type="danger" size="small" @click="handleDelete(scope.$index)" v-permiss="16">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination background layout="total, prev, pager, next" v-model:current-page="query.pageNo"
					:page-size="query.pageSize" :total="totalCount" @current-change="handlePageChange"></el-pagination>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts" name="topic">
	import { ref, reactive, onMounted } from 'vue';
	import { useStore } from '@/store/store';

	import { queryTopicsOrderBy, deleteTopic } from '@/api/topic'
	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';
	import global from '@/utils/global'


	const store = useStore();
	store.init();
	
	interface Topic {
		id: number;
		title: string;
		content: string;
		coverImageUrl: string;
		userId: number;
		starCount: number;
		commentCount: number;
		pageView: number;
		activityTime: string;
		categotyId: number;
		updateTime: string;
		createTime: string;
	}
	//查询话题
	let topics = ref < Topic[] > ([{
		id: 1008610086,
		title: "表他说看得见哈肯德基刷卡单",
		content: "阿坎吉合肥市拉开收到货拉愧疚但是",
		coverImageUrl: 'homeCarousel/2023/01/07/1611730070081744896.jpeg',
		userId: 64645,
		// user: { username: "用户名用户名用户名用户名" },
		starCount: 54564,
		commentCount: 9786,
		pageView: 64546,
		activityTime: '2016-05-02 22:22:22',
		categotyId: 3564,
		updateTime: '2016-05-02 22:22:22',
		createTime: '2016-05-02 22:22:22',
	}]);
	
	let totalCount = ref(1);

	let query = reactive({
		column: 'createTime',
		pageNo: 1,
		pageSize: 10,
	});
	const getData = () => {
		queryTopicsOrderBy(query).then((res: any) => {
			if (res.code == Code.OK) {
				topics.value = res.data.dataList;
				totalCount.value = res.data.totalCount;
			}
		});
	}
	getData();
	const handlePageChange = () => {
		getData();
	};

	//删除
	const handleDelete = (index: number) => {
		MessageUtils.confirm("确定删除吗？操作不可逆！").then(() => {
			const deleteId = topics.value[index].id;
			deleteTopic(deleteId)
				.then((res: any) => {
					if (res.code == Code.OK) {
						MessageUtils.success("删除成功", 1);
						CommonUtils.delayRefresh(1);
					}
				})
		}).catch(e => e);
	}



</script>

<style scoped>
	.hide :deep() .el-upload--picture-card {
		display: none;
	}

	.handle-box {
		margin-bottom: 20px;
	}

	.handle-select {
		width: 120px;
	}

	.handle-input {
		width: 300px;
	}

	.table {
		width: 100%;
		font-size: 14px;
	}

	.red {
		color: #F56C6C;
	}

	.mr10 {
		margin-right: 10px;
	}

	.table-td-thumb {
		display: block;
		margin: auto;
		width: 40px;
		height: 40px;
	}
</style>
