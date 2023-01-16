<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-select v-model="query.address" placeholder="地址" class="handle-select mr10">
					<el-option key="1" label="广东省" value="广东省"></el-option>
					<el-option key="2" label="湖南省" value="湖南省"></el-option>
				</el-select>
				<el-input v-model="query.name" placeholder="用户名" class="handle-input mr10"></el-input>
				<el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
			</div>
		
			<div class="mgb20 tree-wrapper">
				权限树
				<el-tree ref="tree" :data="permissionTree" node-key="id" default-expand-all show-checkbox />
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
					<el-select v-model="addForm.isModule" class="handle-select mr10" clearable>
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
					<el-input v-model="editForm.code" maxlength="20"></el-input>
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

<script setup lang="ts" name="permission">
	import { ref, reactive } from 'vue';
	import {Role} from '@/views/role/role.vue'
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


	interface Permission {
		id: number;
		parentId: number;
		parent ? : null | Permission;
		code: null | string;
		note: string;
		isModule: boolean;
		label: string;
		createTime: string;
		updateTime: string;
		children ? : null | Permission[];
	}



	//查询权限树
	let permissionTree = ref < Permission[] > ([{
		id: 1,
		code: 'user:update',
		label: '用户更新信息',
		parentId: -1,
		parent: null,
		isModule: false,
		note: '用户',
		createTime: '2022-2-22',
		updateTime: '2022-2-22',
		children: []
	}]);
	const getPermissionTree = () => {
		queryPermissionTree().then((res: any) => {
			if (res.code == Code.OK) {
				permissionTree.value = res.data;
			}
		})
	};
	getPermissionTree();


	//查询角色权限树
	let queryRolePermission = ref({
		roleId:-1
	});
	const totalCount = ref(1);
	const getPermissionPage = () => {
		pagePermission(queryPage.value.pageNo, queryPage.value.pageSize).then((res: any) => {
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
		isModule: false,
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
