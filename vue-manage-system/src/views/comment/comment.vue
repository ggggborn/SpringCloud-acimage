<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-input placeholder="关键字" class="handle-input mr10" v-model="query.keyword"></el-input>
				<el-input placeholder="话题id" class="handle-input mr10" v-model="query.topicId"></el-input>
				<el-button type="primary" @click="getData">搜索</el-button>
			</div>
			<el-table :data="comments" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column label="序号" width="55" align="center">
					<template #default="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID" width="50" align="center"></el-table-column>
				<el-table-column prop="topicId" label="话题ID" width="100" align="center"></el-table-column>
				<el-table-column prop="userId" label="用户ID" width="100" align="center"></el-table-column>
				<el-table-column prop="content" label="内容" width="200">
					<template #default="scope">
						{{global.omitStr(scope.row.content,200)}}
					</template>
				</el-table-column>
				<el-table-column prop="updateTime" label="更新时间" width="70"></el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="70"></el-table-column>

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

		<!-- 编辑弹出框 -->
		<el-dialog title="编辑" v-model="editVisible">
			<el-form label-width="70px">
				<el-form-item label="标题">
					<el-input v-model="editForm.content" maxlength="30"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveEdit">确 定</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="comment">
	import { ref, reactive } from 'vue';

	import { queryCommentsBy,deleteComment } from '@/api/comment'
	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';
	import global from '@/utils/global'

	
	interface Comment {
		id: number;
		topicId:number;
		userId:number;
		content: string;
		updateTime: string;
		createTime: string;
	}
	//查询话题
	let comments = ref < Comment[] > ([{
		id: 1008610086,
		userId:1054054,
		topicId:546545,
		content: "阿坎吉合肥市拉开收到货拉愧疚但是",
		updateTime: '2016-05-02 22:22:22',
		createTime: '2016-05-02 22:22:22',
	}]);
	
	let totalCount = ref(1);

	let query = reactive({
		keyword: '',
		topicId:-1,
		pageNo: 1,
		pageSize: 10,
	});
	const getData = () => {
		if(query.topicId<0||query.topicId==null||query.topicId.toString().trim().length==0){
			query.topicId=null;
		}
		queryCommentsBy(query).then((res: any) => {
			if (res.code == Code.OK) {
				comments.value = res.data.dataList;
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
			const deleteId = comments.value[index].id;
			deleteComment(deleteId)
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
