<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-input placeholder="路径关键字" class="handle-input mr10" v-model="query.keyword"></el-input>
				<el-button type="primary" @click="getData">搜索</el-button>
				<el-button type="primary" @click="addVisible=true">新增</el-button>
			</div>
			<el-table :data="apis" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column label="序号" width="55" align="center">
					<template #default="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID" width="50" align="center"></el-table-column>
				<el-table-column prop="path" label="路径" width="200"></el-table-column>
				<el-table-column prop="method" label="方法" width="60"></el-table-column>
				<el-table-column label="权限码" width="160">
					<template #default="scope">
						{{getPermissionCode(scope.row.permissionId)}}
					</template>
				</el-table-column>
				<el-table-column prop="note" label="note"></el-table-column>
				<el-table-column prop="enable" label="是否启用"></el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="70"></el-table-column>

				<el-table-column label="操作" width="150" align="center">
					<template #default="scope">
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">
							编辑
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

		<!-- 新增弹出框 -->
		<el-dialog title="新增" v-model="addVisible">
			<el-form label-width="70px">
				<el-form-item label="路径">
					<el-input v-model="addForm.path" maxlength="200"></el-input>
				</el-form-item>
				<el-form-item label="权限">
					<el-select v-model="addForm.permissionId" placeholder="权限" class="handle-select mr10"
						@change="getRoleAuthorizeList">
						<el-option v-for="permission in permissions" :key="permission.id" :label="permission.code"
							:value="permission.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="备注">
					<el-input v-model="addForm.note" maxlength="100"></el-input>
				</el-form-item>
				<el-form-item label="启用">
					<el-radio-group v-model="addForm.enable">
						<el-radio :label="true">启用</el-radio>
						<el-radio :label="false">禁用</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="请求方法">
					<el-radio-group v-model="addForm.method">
						<el-radio label="GET">GET</el-radio>
						<el-radio label="PUT">PUT</el-radio>
						<el-radio label="POST">POST</el-radio>
						<el-radio label="DELETE">DELTE</el-radio>
						<el-radio label="ALL">ALL</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addVisible = false">关闭</el-button>
					<el-button @click="saveAdd">确认</el-button>
				</span>
			</template>
		</el-dialog>

		<!-- 编辑弹出框 -->
		<el-dialog title="新增" v-model="editVisible">
			<el-form label-width="70px">
				<el-form-item label="路径">
					<el-input v-model="editForm.path" maxlength="200"></el-input>
				</el-form-item>
				<el-form-item label="权限">
					<el-select v-model="editForm.permissionId" placeholder="权限" class="handle-select mr10"
						@change="getRoleAuthorizeList">
						<el-option v-for="permission in permissions" :key="permission.id" :label="permission.code"
							:value="permission.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="备注">
					<el-input v-model="editForm.note" maxlength="100"></el-input>
				</el-form-item>
				<el-form-item label="启用">
					<el-radio-group v-model="editForm.enable">
						<el-radio :label="true">启用</el-radio>
						<el-radio :label="false">禁用</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="请求方法">
					<el-radio-group v-model="editForm.method">
						<el-radio label="GET">GET</el-radio>
						<el-radio label="PUT">PUT</el-radio>
						<el-radio label="POST">POST</el-radio>
						<el-radio label="DELETE">DELETE</el-radio>
						<el-radio label="ALL">ALL</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">关闭</el-button>
					<el-button @click="saveEdit">确认</el-button>
				</span>
			</template>
		</el-dialog>

	</div>
</template>

<script setup lang="ts" name="api">
	import { ref, reactive, onMounted } from 'vue';

	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';
	import global from '@/utils/global'

	import { useStore } from '@/store/store';
	import { Permission } from '@/views/permission/permission.vue'
	import { queryNonModules } from '@/api/permission';

	import { searchApis, addApi, modifyApi, deleteApi } from '@/api/api'

	const store = useStore();
	store.init()

	interface Api {
		id: number;
		path: string;
		method: string;
		note: string;
		enable: boolean;
		permissionId: number;
		createTime: string;
		updateTime: string;
	}
	let apis = ref < Api[] > ([]);

	let permissions = ref < Permission[] > ([]);
	const getNonModules = () => {
		queryNonModules().then((res: any) => {
			if (res.code == Code.OK) {
				permissions.value = res.data;
			}
		})
	};
	getNonModules();
	const getPermissionCode = (permissionId) => {
		for (let permission of permissions.value) {
			if (permission.id == permissionId) {
				return permission.code;
			}
		}
		return null;
	}

	let totalCount = ref(1);
	let query = reactive({
		keyword: null,
		pageNo: 1,
		pageSize: 20,
	});
	const getData = () => {
		searchApis(query).then((res: any) => {
			if (res.code == Code.OK) {
				apis.value = res.data.dataList;
				totalCount.value = res.data.totalCount;
			}
		});
	}
	getData();
	const handlePageChange = () => {
		getData();
	};

	//编辑接口
	let editForm = reactive({
		id: 0,
		path: '',
		note: '',
		permissionId: -1,
		method: '',
		enable: true,
	});
	let editVisible = ref(false);
	const handleEdit = (index: number, row: Api) => {
		editForm.id = apis.value[index].id;
		editForm.path = apis.value[index].path;
		editForm.note = apis.value[index].note;
		editForm.permissionId = apis.value[index].permissionId;
		editForm.method = apis.value[index].method;
		editForm.enable = apis.value[index].enable;
		editVisible.value = true;
	};

	const saveEdit = () => {
		modifyApi(editForm).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("修改成功", 2);
				CommonUtils.delayRefresh(1);
			}
		})
	}

	//新增接口
	let addForm = reactive({
		path: null,
		method: "GET",
		note: null,
		permissionId: null,
		enable: true
	});
	let addVisible = ref(false);

	const saveAdd = () => {
		addApi(addForm).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("增加成功", 2);
				CommonUtils.delayRefresh(1);
			}
		})
	}


	const handleDelete = (index: number) => {
		MessageUtils.confirm("是否确定删除？操作不可逆！").then(() => {
			deleteApi(apis.value[index].id).then((res: any) => {
				if (res.code == Code.OK) {
					MessageUtils.success("修改成功", 2);
					CommonUtils.delayRefresh(1);
				}
			})
		})
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
