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
				<el-button type="primary" :icon="Plus" @click="addImageVisible=true">新增</el-button>
			</div>
			<el-table :data="homeCarousel" border class="table" ref="multipleTable"
				header-cell-class-name="table-header">
				<el-table-column label="序号" width="55" align="center">
					<template #default="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID" width="50" align="center"></el-table-column>

				<el-table-column label="图片" align="center">
					<template #default="scope">
						<el-image class="table-td-thumb" :src="scope.row.url" :z-index="10"
							:preview-src-list="[scope.row.url]" preview-teleported>
						</el-image>
					</template>
				</el-table-column>
				<el-table-column prop="description" label="描述"></el-table-column>
				<el-table-column prop="link" label="链接"></el-table-column>
				<el-table-column prop="updateTime" label="更新时间"></el-table-column>
				<el-table-column label="文件大小">
					<template #default="scope">{{ Math.ceil(scope.row.size/1000) }}KB</template>
				</el-table-column>

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
				<el-form-item label="新的链接">
					<el-input v-model="editForm.link" maxlength="100"></el-input>
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
					<el-input v-model="addImageForm.description" maxlength="30"></el-input>
				</el-form-item>
				<el-form-item label="链接" prop="link">
					<el-input v-model="addImageForm.link" maxlength="100"></el-input>
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

<script setup lang="ts" name="HomeCarousel">
	import { ref, reactive, onMounted } from 'vue';
	import { UploadFile } from 'element-plus';
	import { Plus } from '@element-plus/icons-vue';

	import {
		add,
		modifyDescription,
		deleteById,
		coverImage,
		queryCurrent
	} from '@/api/HomeCarousel';
	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';
	import global from '@/utils/global'
	

	interface Image {
		id: number;
		updateTime: string;
		description: string;
		link:string
		url: string;
		size: number;
	}
	//查询首页图片
	let homeCarousel = ref < Image[] > ([{
		id: 1008610086,
		updateTime: '2016-05-02 22:22:22',
		description: '好看的',
		url: 'homeCarousel/2023/01/07/1611730070081744896.jpeg',
		link:'',
		size: 1008610,
	}]);

	const getData = () => {
		queryCurrent().then((res: any) => {
			if (res.code == Code.OK) {
				homeCarousel.value = res.data;
			}
		});
	}
	getData();

	//编辑
	let editForm = reactive({
		id: 0,
		description: '',
		link:''
	});
	let editVisible = ref(false);
	const handleEdit = (index: number, row: Image) => {
		editForm.id = homeCarousel.value[index].id;
		editForm.description = row.description;
		editForm.link = row.link;
		editVisible.value = true;
	};
	const saveEdit = () => {
		modifyDescription(editForm).then((res: any) => {
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
		link:'',
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
		reqData.append("link", addImageForm.link);
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
	const handleDelete = (index: number) => {
		MessageUtils.confirm("确定删除吗？操作不可逆！").then(() => {
			const deleteId = homeCarousel.value[index].id;
			deleteById(deleteId)
				.then((res: any) => {
					if (res.code == Code.OK) {
						MessageUtils.success("删除成功", 1);
						CommonUtils.delayRefresh(1);
					}
				})
		}).catch(e => e);

	}




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
