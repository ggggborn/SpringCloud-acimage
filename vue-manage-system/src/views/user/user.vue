<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<!-- 				<el-select placeholder="排序字段" class="handle-select mr10" style="width:160px;">
					<el-option key="1" label="createTime" value="createTime"></el-option>
				</el-select> -->
				<el-input placeholder="关键字" class="handle-input mr10" v-model="query.keyword"></el-input>
				<el-button type="primary" @click="getData">搜索</el-button>
			</div>
			<el-table :data="users" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column label="序号" width="55" align="center">
					<template #default="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID" width="50" align="center"></el-table-column>
				<el-table-column label="头像" align="center" width="100">
					<template #default="scope">
						<el-image class="table-td-thumb" :src="scope.row.photoUrl" :z-index="10"
							:preview-src-list="[scope.row.photoUrl]" preview-teleported>
						</el-image>
					</template>
				</el-table-column>
				<el-table-column prop="username" label="用户名"></el-table-column>
				<el-table-column prop="content" label="角色" width="200">
					<template #default="scope">
						<el-tag v-for="roleId in scope.row.roleIds" tyle="success">
							{{store.roleName(roleId) }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="注册时间" width="70"></el-table-column>

				<el-table-column label="操作" width="250" align="center">
					<template #default="scope">
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">
							编辑角色
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
				<el-form-item label="当前角色">
					<el-tag v-for="id in editForm.roleIds" closable @close="handleDeleteRole(id)">
						{{store.roleName(id) }}
					</el-tag>
				</el-form-item>
				<el-form-item label="所有角色">
					<el-tag v-for="role in store.roleList" type="success" class="hover-pointer"
						style="margin-left: 10px;" size="large" @click="handleAddRole(role.id)">
						{{role.roleName }}
					</el-tag>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">关闭</el-button>
				</span>
			</template>
		</el-dialog>

	</div>
</template>

<script setup lang="ts" name="user">
	import { ref, reactive, onMounted } from 'vue';

	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';
	import global from '@/utils/global'

	import { useStore } from '@/store/store';

	import { queryUsers } from '@/api/user'
	import { addRoleForUser, deleteRoleForUser } from '@/api/UserRole'

	const store = useStore();
	store.init()

	interface User {
		id: number;
		username: string;
		photoUrl: string;
		createTime: string;
		roleIds: any;
	}
	let users = ref < User[] > ([]);
	let totalCount = ref(1);
	let query = reactive({
		keyword: null,
		pageNo: 1,
		pageSize: 10,
	});
	const getData = () => {
		queryUsers(query).then((res: any) => {
			if (res.code == Code.OK) {
				users.value = res.data.dataList;
				totalCount.value = res.data.totalCount;
			}
		});
	}
	getData();
	const handlePageChange = () => {
		getData();
	};


	//编辑角色
	let editForm = reactive({
		userId: 0,
		roleIds: [],
	});
	let editVisible = ref(false);
	const handleEdit = (index: number, row: User) => {
		editForm.userId = users.value[index].id;
		editForm.roleIds = users.value[index].roleIds;
		editVisible.value = true;
	};

	const handleAddRole = (roleId) => {
		MessageUtils.confirm("确定为该用户增加角色吗").then(() => {
			addRoleForUser(editForm.userId, roleId).then((res: any) => {
				if (res.code == Code.OK) {
					MessageUtils.success("增加成功", 2);
					CommonUtils.delayRefresh(1);
				}
			})
		}).catch(e => e)
	}

	const handleDeleteRole = (roleId) => {
		MessageUtils.confirm("确定删除角色吗").then(() => {
			deleteRoleForUser(editForm.userId, roleId).then((res: any) => {
				if (res.code == Code.OK) {
					MessageUtils.success("删除成功", 2);
					CommonUtils.delayRefresh(1)
				}
			})
		}).catch(e => e)

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
