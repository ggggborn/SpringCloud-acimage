<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<div class="handle-box">
					<el-button type="primary" :icon="Plus" @click="addVisible=true">新增</el-button>
				</div>
			</div>
			<el-table :data="permissionList" border class="table" ref="multipleTable"
				header-cell-class-name="table-header">
				<el-table-column label="序号" width="55" align="center">
					<template #default="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
				<el-table-column prop="label" label="名称"></el-table-column>
				<el-table-column prop="code" label="权限码" width="120"></el-table-column>
				<el-table-column prop="module" label="是否是模块" width="60"></el-table-column>
				<el-table-column label="父模块"  align="center">
					<template #default="scope">
						{{CommonUtils.isEmpty(scope.row.parent)?'无':scope.row.parent.label}}
					</template>
				</el-table-column>
				<el-table-column prop="note" label="备注"></el-table-column>
				<el-table-column prop="createTime" label="创建时间"></el-table-column>
				<el-table-column prop="updateTime" label="修改时间"></el-table-column>

				<el-table-column label="操作" width="220" align="center">
					<template #default="scope">
						<el-button size="small" @click="handleEdit(scope.row)">
							编辑
						</el-button>
						<el-button type="danger" size="small" @click="handleDelete(scope.$index)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination background layout="total, prev, pager, next" v-model:current-page="query.pageNo"
					:page-size="query.pageSize" :total="totalCount" @current-change="handlePageNoChange">
				</el-pagination>
			</div>


			<div class="mgb20 tree-wrapper">
				权限树
				<el-tree ref="tree" :data="permissionTree" node-key="id" default-expand-all />
			</div>
		</div>
		<!-- 新增权限对话框 -->
		<el-dialog title="新增权限" v-model="addVisible">
			<el-form :model="addForm">
				<el-form-item label="父级模块(可为空)">
					<el-select v-model="addForm.parentId" placeholder="父级模块(可为空)" class="handle-select mr10" clearable>
						<el-option v-for="item in moduleList" :key="item.id" :label="item.label" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="权限码">
					<el-input v-model="addForm.code"></el-input>
				</el-form-item>
				<el-form-item label="名称">
					<el-input v-model="addForm.label"></el-input>
				</el-form-item>
				<el-form-item label="是否是模块">
					<el-select v-model="addForm.module" class="handle-select mr10" clearable>
						<el-option key="1" label="是" :value="true"></el-option>
						<el-option key="1" label="否" :value="false"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="备注">
					<el-input v-model="addForm.note"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveAdd">确 定</el-button>
				</span>
			</template>
		</el-dialog>
		<!-- 编辑弹出框 -->
		<el-dialog title="编辑" v-model="editVisible">
			<el-form label-width="120px">
				<el-form-item label="父级模块(可为空)">
					<el-select v-model="editForm.parentId" placeholder="父级模块(可为空)" class="handle-select mr10" clearable>
						<el-option v-for="item in moduleList" :key="item.id" :label="item.label" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="权限码(慎重修改)">
					<el-input v-model="editForm.code" maxlength="50"></el-input>
				</el-form-item>
				<el-form-item label="名称">
					<el-input v-model="editForm.label" maxlength="20"></el-input>
				</el-form-item>
				<el-form-item label="备注">
					<el-input v-model="editForm.note" maxlength="20"></el-input>
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

<script setup lang="ts" name="authorize">
	import { ref, reactive } from 'vue';
	import { useRoute, useRouter } from "vue-router"
	import { Plus } from '@element-plus/icons-vue';
	import {
		queryModules,
		queryPermissionTree,
		pagePermission,
		addPermission,
		modifyPermission,
		deletePermission
	} from '@/api/permission';

	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';


	export interface Permission {
		id: number;
		parentId: number;
		parent ? : null | Permission;
		code: null | string;
		note: string;
		module: boolean;
		label: string;
		createTime: string;
		updateTime: string;
		children ? : null | Permission[];
	}

	//查询模块
	let moduleList = ref < Permission[] > ([]);
	const getModules = () => {
		queryModules().then((res: any) => {
			if (res.code == Code.OK) {
				moduleList.value = res.data;
			}
		})
	};
	getModules();

	//查询权限树
	let permissionTree = ref < Permission[] > ([{
		id: 1,
		code: 'user:update',
		label: '用户更新信息',
		parentId: -1,
		parent: null,
		module: false,
		note: '用户',
		createTime: '2022-2-22',
		updateTime: '2022-2-22',
		children: []
	}]);
	const combineAsLabel = (node: Permission) => {
		node.label = CommonUtils.isEmpty(node.code) ? node.label : node.code + '|' + node.label;
		for (let item of node.children!) {
			combineAsLabel(item);
		}
	}
	const getPermissionTree = () => {
		queryPermissionTree().then((res: any) => {
			if (res.code == Code.OK) {
				permissionTree.value = res.data;
				for (let item of permissionTree.value!) {
					combineAsLabel(item);
				}
			}
		})
	};
	getPermissionTree();

	//查询权限列表
	const route = useRoute();
	// const router=useRouter();
	let permissionList = ref < Permission[] > ([]);
	let query = ref({
		pageNo: parseInt(route.params.pageNo[0]),
		pageSize: 10
	});
	const handlePageNoChange = () => {
		window.location.href = "/#/permission/" + query.value.pageNo;
		// router.replace({name:"permission",params:{pageNo:queryPage.value.pageNo.toString()}})
		// route.path.replace('pageNo', queryPage.value.pageNo.toString())
		// route.params.pageNo=queryPage.value.pageNo.toString();
		getPermissionPage();
	}
	const totalCount = ref(1);
	const getPermissionPage = () => {
		pagePermission(query.value.pageNo, query.value.pageSize).then((res: any) => {
			if (res.code == Code.OK) {
				permissionList.value = res.data.dataList;
				totalCount.value = res.data.totalCount;
			}
		})
	};
	getPermissionPage();

	//新增权限
	let addVisible = ref(false);
	let addForm = reactive({
		module: false,
		code: '',
		label: '',
		parentId: null,
		note: '',
	});
	const saveAdd = () => {
		addPermission(addForm).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("增加成功", 1);
				CommonUtils.delayRefresh(1);
			}
		});
		addVisible.value = false;
	};

	//编辑
	let editVisible = ref(false);
	let editForm = reactive({
		id: 0,
		code: null,
		label: '',
		parentId: null,
		note: '',
	});
	const handleEdit = (row: any) => {
		editForm.id = row.id;
		editForm.code = row.code;
		editForm.label = row.label;
		editForm.note = row.note;
		editForm.parentId = row.parentId;
		editVisible.value = true;
	};
	const saveEdit = () => {
		modifyPermission(editForm).then((res: any) => {
			if (res.code = Code.OK) {
				MessageUtils.success("修改成功", 1);
				CommonUtils.delayRefresh(1);
				editVisible.value = false;
			}
		});
	};

	//删除
	const handleDelete = (index: number) => {
		MessageUtils.confirm("确定删除吗？操作不可逆！").then(() => {
			const deleteId = permissionList.value[index].id;
			deletePermission(deleteId)
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
		width: 200px;
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
