<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-select v-model="query.roleId" placeholder="角色" class="handle-select mr10" @change="getRoleAuthorizeList">
					<el-option v-for="item in roleList" :key="item.id" :label="item.roleName" :value="item.id">
					</el-option>
				</el-select>
				<el-button type="primary" :icon="Search" @click="getRoleAuthorizeList">刷新</el-button>
			</div>

			<div class="mgb20 tree-wrapper">
				权限树
				<el-tree class="leaf-chekabel-only" ref="treeRef" :data="permissionTree" node-key="id"
					default-expand-all check-strictly show-checkbox :default-checked-keys="checkedKeys"
					@check="handleCheck" />
			</div>
		</div>

	</div>
</template>

<script setup lang="ts" name="authorize">
	import { ref, reactive } from 'vue';
	import { ElTree } from 'element-plus'
	import { Role } from '@/views/role/role.vue'
	import { Permission } from '@/views/permission/permission.vue'

	import { queryAllRoles } from '@/api/role';
	import { queryPermissionTree } from '@/api/permission';
	import { queryRoleAuthorize, addAuthorize, deleteAuthorize } from '@/api/authorize';
	import { Search } from '@element-plus/icons-vue';

	import { Code } from '@/utils/result';
	import CommonUtils from '@/utils/CommonUtils';
	import MessageUtils from '@/utils/MessageUtils';

	//查询权限树
	let permissionTree = ref < Permission[] > ([]);
	const getPermissionTree = () => {
		queryPermissionTree().then((res: any) => {
			if (res.code == Code.OK) {
				permissionTree.value = res.data;
			}
		})
	};
	getPermissionTree();

	//查询所有角色
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


	//查询角色权限
	let roleAuthorizeList = ref([]);
	let query = reactive({
		roleId: -1,
	})
	const getRoleAuthorizeList = () => {
		if (query.roleId != -1) {
			queryRoleAuthorize(query.roleId).then((res: any) => {
				if (res.code == Code.OK) {
					roleAuthorizeList.value = res.data;
					initCheckedKeys();
				}
			})
		}
	}

	let checkedKeys = ref < number[] > ([]);
	const initCheckedKeys = () => {
		checkedKeys = ref < number[] > ([]);;
		for (const item of roleAuthorizeList.value) {
			checkedKeys.value.push(item.permissionId);
		}
		treeRef.value!.setCheckedKeys(checkedKeys.value, false);
	}

	//选中节点复选框时触发
	interface Tree {
		id: number
		label: string
		children ? : Tree[]
	}
	const treeRef = ref < InstanceType < typeof ElTree >> ()
	const handleCheck = (node: Permission) => {
		if(node.isModule){
			treeRef.value!.setCheckedKeys(checkedKeys.value, false);
			MessageUtils.notice("不可操作模块",1);
			return false;
		}
		MessageUtils.confirm("是否改变该权限").then(() => {
			if (checkedKeys.value.includes(node.id)) {
				saveDelete(query.roleId, node.id);
				checkedKeys.value.forEach((value, index) => {
					if (value == node.id) {
						checkedKeys.value.slice(index, 1);
					}
				})
			} else {
				saveAdd(query.roleId, node.id);
				checkedKeys.value.push(node.id);
			}
			return false;
		}).catch(() => {
			treeRef.value!.setCheckedKeys(checkedKeys.value, false);
		})

		return true;
	};

	const saveAdd = (roleId: number, permissionId: number) => {
		let formData: FormData = new FormData();
		formData.append("roleId", roleId.toString());
		formData.append("permissionId", permissionId.toString());
		addAuthorize(formData).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("修改成功", 1);
			}
		})
	}

	const saveDelete = (roleId: number, permissionId: number) => {
		deleteAuthorize(roleId, permissionId).then((res: any) => {
			if (res.code == Code.OK) {
				MessageUtils.success("修改成功", 1);
			}
		})
	}
</script>

<style scoped>
	/* 只能选叶子节点 */
	.leaf-chekabel-only :deep() .el-tree-node {

		.is-leaf+.el-checkbox .el-checkbox__inner {

			display: inline-block;

		}

		.el-checkbox__input>.el-checkbox__inner {

			display: none;

		}

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
