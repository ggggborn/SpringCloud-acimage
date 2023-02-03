<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-select v-model="query.column" placeholder="排序字段" class="handle-select mr10">
					<el-option key="1" label="createTime" value="createTime"></el-option>
					<el-option key="2" label="updateTime" value="updateTime"></el-option>
					<el-option key="3" label="starCount" value="starCount"></el-option>
					<el-option key="4" label="commentCount" value="commentCount"></el-option>
					<el-option key="5" label="pageView" value="pageView"></el-option>
					<el-option key="6" label="pageView" value="pageView"></el-option>
				</el-select>
				<el-input  placeholder="用户id" class="handle-input mr10"></el-input>
				<el-button type="primary"  @click="getData">搜索</el-button>
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
						<el-image class="table-td-thumb" :src="global.trueImageUrl(scope.row.coverImageUrl)"
							:z-index="10" :preview-src-list="[global.trueImageUrl(scope.row.coverImageUrl)]"
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
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">
							编辑
						</el-button>
						<el-button size="small" @click="handleEditHtml(scope.$index, scope.row)">
							编辑html
						</el-button>
						<el-button type="primary" size="small" @click="handleCover(scope.row)">
							覆盖
						</el-button>
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
				<el-select v-model="edit.categoryId" placeholder="角色" class="handle-select mr10">
					<el-option v-for="item in categoryList" :key="item.id" :label="item.roleName" :value="item.id">
					</el-option>
				</el-select>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveEdit">确 定</el-button>
				</span>
			</template>
		</el-dialog>
		<!-- 覆盖图片对话框 -->
		<el-dialog title="覆盖图片" v-model="coverVisible">
			<el-form>
				<el-form-item>
					<el-upload action="#" ref="cover" :class="{'hide':coverImageList.length >= 1}"
						list-type="picture-card" :limit="1" :auto-upload="false" accept="image/*"
						v-model:file-list="coverImageList">
						<el-icon>
							<Plus />
						</el-icon>
					</el-upload>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="coverVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveCover()">确 定</el-button>
				</span>
			</template>
		</el-dialog>
		<!-- 新增图片对话框 -->
		<el-dialog title="新增图片" v-model="addImageVisible">
			<el-form :model="addImageForm">
				<el-form-item>
					<el-upload action="#" ref="upload" :class="{'hide':addImageList.length>=1}" list-type="picture-card"
						:limit="1" :auto-upload="false" accept="image/*" v-model:file-list="addImageList">
						<el-icon>
							<Plus />
						</el-icon>
					</el-upload>
				</el-form-item>
				<el-form-item label="描述" prop="description">
					<el-input v-model="addImageForm.description"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addImageVisible = false">取 消</el-button>
					<el-button type="primary" @click="addImage">确 定</el-button>
				</span>
			</template>
		</el-dialog>

	</div>
</template>

<script setup lang="ts" name="topic">
	import { ref, reactive, onMounted } from 'vue';
	import { UploadFile } from 'element-plus';
	import { Plus } from '@element-plus/icons-vue';
	import { useStore } from '@/store/store';

	import { queryTopicsOrderBy,deleteTopic } from '@/api/topic'
	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';
	import global from '@/utils/global'


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
	//查询首页图片
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
	const store = useStore();
	store.init();
	let query = reactive({
		column: 'createTime',
		pageNo: 1,
		pageSize: 5,
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
	

	//编辑
	let editForm = reactive({
		id: 0,
		content: '',
		title: '',
		categoryId: null,
	});
	let editVisible = ref(false);
	const handleEdit = (index: number, row: Topic) => {
		editForm.id = topics.value[index].id;
		editVisible.value = true;
	};
	const saveEdit = () => {
		let params = {
			description: editForm.description,
			id: editForm.id
		};
		modifyDescription(params).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("修改成功", 1);
				CommonUtils.delayRefresh(1);
			}
		})
	};

	//覆盖
	let coverVisible = ref(false);

	let coverId = -1;
	let coverImageList = ref < UploadFile[] > ([]);

	const handleCover = (row: Image) => {
		coverVisible.value = true;
		coverId = row.id;

	};
	const saveCover = () => {
		if (coverImageList.value.length == 0) {
			MessageUtils.notice("至少选择一张图", 2);
			return;
		}
		let reqData: any = new FormData();
		reqData.append("id", coverId);
		reqData.append("image", coverImageList.value[0].raw);
		coverImage(reqData).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("成功", 1);
				CommonUtils.delayRefresh(1);
			}
		})
		coverVisible.value = false;
	}

	//新增
	let addImageVisible = ref(false);
	let addImageForm = reactive({
		description: '',
	});
	let addImageList = ref < UploadFile[] > ([]);

	let addImage = () => {
		if (addImageList.value.length == 0) {
			MessageUtils.notice("至少选择一张图", 2);
			return;
		}
		if (addImageForm.description.length < 2 || addImageForm.description.length > 30) {
			MessageUtils.notice("描述需要在2-30之间", 2);
			return;
		}

		let reqData: any = new FormData();
		reqData.append("description", addImageForm.description);
		reqData.append("image", addImageList.value[0].raw);

		add(reqData).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("新增成功", 1);
				CommonUtils.delayRefresh(0.5);
			}
		});
		addImageVisible.value = false;
	};

	//删除





	// let currentModifyId: number = 0;
	// let newDescription = '';

	// interface TableItem {
	// 	id: number;
	// 	name: string;
	// 	money: string;
	// 	state: string;
	// 	date: string;
	// 	address: string;
	// }

	// const query = reactive({
	// 	address: '',
	// 	name: '',
	// 	pageIndex: 1,
	// 	pageSize: 10
	// });
	// const tableData = ref < TableItem[] > ([]);
	// const pageTotal = ref(0);
	// // 获取表格数据
	// // const getData = () => {
	// // 	fetchData().then((res: any) => {
	// // 		tableData.value = res.data.list;
	// // 		pageTotal.value = res.data.pageTotal || 50;
	// // 	});
	// // };
	// // getData();

	// // 查询操作
	// const handleSearch = () => {
	// 	query.pageIndex = 1;
	// 	// getData();
	// };
	// // 分页导航
	// const handlePageChange = (val: number) => {
	// 	query.pageIndex = val;
	// 	// getData();
	// };

	// 删除操作
	// const handleDelete = (index: number) => {
	// 	// 二次确认删除
	// 	ElMessageBox.confirm('确定要删除吗？', '提示', {
	// 			type: 'warning'
	// 		})
	// 		.then(() => {
	// 			ElMessage.success('删除成功');
	// 			tableData.value.splice(index, 1);
	// 		})
	// 		.catch(() => {});
	// };

	// 表格编辑时弹窗和保存
	// let form = reactive({
	// 	name: '',
	// 	address: ''
	// });
	// let idx: number = -1;
	// const handleEdit = (index: number, row: any) => {
	// 	idx = index;
	// 	form.name = row.name;
	// 	form.address = row.address;
	// 	editVisible.value = true;
	// };
	// const saveEdit = () => {
	// 	editVisible.value = false;
	// 	ElMessage.success(`修改第 ${idx + 1} 行成功`);
	// 	tableData.value[idx].name = form.name;
	// 	tableData.value[idx].address = form.address;
	// };
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
