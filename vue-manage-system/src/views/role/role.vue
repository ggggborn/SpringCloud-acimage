<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<!-- 				<el-select v-model="query.address" placeholder="地址" class="handle-select mr10">
					<el-option key="1" label="广东省" value="广东省"></el-option>
					<el-option key="2" label="湖南省" value="湖南省"></el-option>
				</el-select>
				<el-input v-model="query.name" placeholder="用户名" class="handle-input mr10"></el-input>
				<el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button> -->
				<el-button type="primary" :icon="Plus" @click="addRoleVisible=true">新增</el-button>
			</div>
			<el-table :data="roleList" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column label="序号" width="55" align="center">
					<template #default="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>

				<el-table-column prop="roleName" label="角色名"></el-table-column>
				<el-table-column prop="note" label="备注"></el-table-column>
				<el-table-column prop="updateTime" label="创建时间"></el-table-column>
				<el-table-column prop="updateTime" label="修改时间"></el-table-column>

				<el-table-column label="操作" width="220" align="center">
					<template #default="scope">
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)" v-permiss="15">
							编辑
						</el-button>
						<el-button type="primary" size="small" @click="handleCover(scope.row)" v-permiss="16">
							覆盖
						</el-button>
						<el-button type="danger" size="small" @click="handleDelete(scope.$index)" v-permiss="16">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!-- 			<div class="pagination">
				<el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
					:page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
			</div> -->
		</div>

		<!-- 编辑弹出框 -->
		<el-dialog title="编辑" v-model="editVisible">
			<el-form label-width="70px">
				<el-form-item label="新的描述">
					<el-input v-model="editForm.description" maxlength="30"></el-input>
				</el-form-item>
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
		<el-dialog title="新增图片" v-model="addRoleVisible">
			<el-form :model="addRoleForm">
				<el-form-item label="角色名">
					<el-input v-model="addRoleForm.roleName"></el-input>
				</el-form-item>
				<el-form-item label="备注">
					<el-input v-model="addRoleForm.note"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addRoleVisible = false">取 消</el-button>
					<el-button type="primary" @click="addRole">确 定</el-button>
				</span>
			</template>
		</el-dialog>

	</div>
</template>

<script setup lang="ts" name="role">
	import { ref, reactive } from 'vue';
	import { Plus } from '@element-plus/icons-vue';


	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';

	interface Role {
		id: number;
		roleName: string;
		note: string;
		createTime: string;
		updateTime: string;
		
	}
	//查询首页图片
	let roleList = reactive < Role[] > ([{
		id: 1,
		roleName: 'user',
		createTime: '2022-2-22',
		updateTime: '2022-2-22',
		note: '用户',
	}]);

	let addRoleVisible = ref(false);
	let addRoleForm = reactive({
		roleName: '',
		note: '',
	});
	const addRole = () => {

	}

	// const getData = () => {
	// 	queryCurrentHomeCarousel().then((res: any) => {
	// 		if (res.code == Code.OK) {
	// 			homeCarousel = res.data;
	// 		}
	// 	});
	// }
	// getData();

	// //编辑
	// let editForm = reactive({
	// 	id: 0,
	// 	description: ''
	// });
	// let editVisible = ref(false);
	// const handleEdit = (index: number, row: Image) => {
	// 	editForm.id = homeCarousel[index].id;
	// 	editForm.description = row.description;
	// 	editVisible.value = true;
	// };
	// const saveEdit = () => {
	// 	let params = {
	// 		description: editForm.description,
	// 		id: editForm.id
	// 	};
	// 	modifyHomeCarouselDescription(params).then((res: any) => {
	// 		if (res.code == Code.OK) {
	// 			MessageUtils.success("修改成功", 1);
	// 			CommonUtils.delayRefresh(1);
	// 		}
	// 	})
	// };

	// //覆盖
	// let coverVisible = ref(false);

	// let coverId = -1;
	// let coverImageList = ref < UploadFile[] > ([]);

	// const handleCover = (row: Image) => {
	// 	coverVisible.value = true;
	// 	coverId = row.id;

	// };
	// const saveCover = () => {
	// 	if (coverImageList.value.length == 0) {
	// 		MessageUtils.notice("至少选择一张图", 2);
	// 		return;
	// 	}
	// 	let reqData: any = new FormData();
	// 	reqData.append("id", coverId);
	// 	reqData.append("image", coverImageList.value[0].raw);
	// 	coverHomeCarouselImage(reqData).then((res: any) => {
	// 		if (res.code == Code.OK) {
	// 			MessageUtils.success("成功", 1)
	// 		}
	// 	})
	// }

	// //新增
	// let addImageVisible = ref(false);
	// let addImageForm = reactive({
	// 	description: '',
	// });
	// let addImageList = ref < UploadFile[] > ([]);

	// let addImage = () => {
	// 	if (addImageList.value.length == 0) {
	// 		MessageUtils.notice("至少选择一张图", 2);
	// 		return;
	// 	}
	// 	if (addImageForm.description.length < 2 || addImageForm.description.length > 30) {
	// 		MessageUtils.notice("描述需要在2-30之间", 2);
	// 		return;
	// 	}

	// 	let reqData: any = new FormData();
	// 	reqData.append("description", addImageForm.description);
	// 	reqData.append("image", addImageList.value[0].raw);

	// 	addHomeCarouselImage(reqData).then((res: any) => {
	// 		if (res.code == Code.OK) {
	// 			MessageUtils.success("新增成功", 1);
	// 			CommonUtils.delayRefresh(0.5);
	// 		}
	// 	});
	// 	addImageVisible.value = false;
	// };

	// //删除
	// const handleDelete = (index: number) => {
	// 	MessageUtils.confirm("确定删除吗？操作不可逆！").then(() => {
	// 		const deleteId = homeCarousel[index].id;
	// 		deleteHomeCarouselImage(deleteId)
	// 			.then((res: any) => {
	// 				if (res.code == Code.OK) {
	// 					MessageUtils.success("新增成功", 1);
	// 					CommonUtils.delayRefresh(0.5);
	// 				}
	// 			})
	// 	}).catch(e => e);

	// }




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
