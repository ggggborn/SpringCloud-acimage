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
				<el-button type="primary" :icon="Plus" @click="addVisible=true">新增</el-button>
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
				<el-table-column prop="createTime" label="创建时间"></el-table-column>
				<el-table-column prop="updateTime" label="修改时间"></el-table-column>

				<el-table-column label="操作" width="220" align="center">
					<template #default="scope">
						<el-button size="small" @click="handleEdit(scope.row)" >
							编辑
						</el-button>
						<el-button type="danger" size="small" @click="handleDelete(scope.$index)" >
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
				<el-form-item label="角色名">
					<el-input v-model="editForm.roleName" maxlength="20"></el-input>
				</el-form-item>
			</el-form>
			<el-form label-width="70px">
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
		<!-- 新增角色对话框 -->
		<el-dialog title="新增角色" v-model="addVisible">
			<el-form :model="addForm">
				<el-form-item label="角色名">
					<el-input v-model="addForm.roleName"></el-input>
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

	</div>
</template>

<script setup lang="ts" name="role">
	import { ref, reactive } from 'vue';
	import { Plus } from '@element-plus/icons-vue';
	import { queryAllRoles, addRole, deleteRole, modifyRole } from '@/api/role';

	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';

	export interface Role {
		id: number;
		roleName: string;
		note: string;
		createTime: string;
		updateTime: string;

	}
	//查询首页图片
	let roleList = ref < Role[] > ([{
		id: 1,
		roleName: 'user',
		createTime: '2022-2-22',
		updateTime: '2022-2-22',
		note: '用户',
	}]);
	const getRoleList = () => {
		queryAllRoles().then((res: any) => {
			if (res.code == Code.OK) {
				roleList.value = res.data;
			}
		})
	};
	getRoleList();

	//新增角色
	let addVisible = ref(false);
	let addForm = reactive({
		roleName: '',
		note: '',
	});
	const saveAdd = () => {
		addRole(addForm).then((res: any) => {
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
		id: -1,
		roleName: '',
		note: '',
	});
	const handleEdit=(row : any)=>{
		editForm.id=row.id;
		editForm.roleName=row.roleName;
		editForm.note=row.note;
		editVisible.value=true;
	};
	const saveEdit = () => {
		modifyRole(editForm).then((res: any) => {
			if(res.code=Code.OK){
				MessageUtils.success("修改成功", 1);
				CommonUtils.delayRefresh(1);
				editVisible.value=false;
			}
		});
	};
	
	//删除
	const handleDelete = (index: number) => {
		MessageUtils.confirm("确定删除吗？操作不可逆！").then(() => {
			const deleteId = roleList.value[index].id;
			deleteRole(deleteId)
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
