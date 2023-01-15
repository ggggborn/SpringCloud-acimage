<template>
	<div class="container">
		<div class="handle-box">
			<!-- 				<el-select v-model="query.address" placeholder="地址" class="handle-select mr10">
				<el-option key="1" label="广东省" value="广东省"></el-option>
				<el-option key="2" label="湖南省" value="湖南省"></el-option>
			</el-select>
			<el-input v-model="query.name" placeholder="用户名" class="handle-input mr10"></el-input>
			<el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button> -->
			<el-button type="primary" :icon="Plus" @click="addPermissionVisible=true">新增权限</el-button>
		</div>
		<div class="mgb20">
			<span class="label">角色：</span>
			<el-select v-model="role" @change="handleChange">
				<el-option label="超级管理员" value="admin"></el-option>
				<el-option label="普通用户" value="user"></el-option>
			</el-select>
		</div>
		<div class="mgb20 tree-wrapper">
			<el-tree ref="tree" :data="data" node-key="id" default-expand-all show-checkbox
				:default-checked-keys="checkedKeys" />
		</div>
		<el-button type="primary" @click="onSubmit">保存权限</el-button>

		<!-- 新增权限对话框 -->
		<el-dialog title="新增角色" v-model="addPermissionVisible">
			<el-form :model="addPermissionForm">
				<el-form-item label="父节点">
					<el-select v-model="roleList">
						<el-option label="超级管理员" value="admin"></el-option>
						<el-option label="普通用户" value="user"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="权限码">
					<el-input v-model="addPermissionForm.roleName"></el-input>
				</el-form-item>
				<el-form-item label="名称">
					<el-input v-model="addPermissionForm.note"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addPermissionVisible=false">取 消</el-button>
					<el-button type="primary" @click="addPermission">确 定</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="permission">
	import { ref } from 'vue';
	import { ElTree } from 'element-plus';

	const role = ref < string > ('admin');

	interface Tree {
		id: string;
		label: string;
		children ? : Tree[];
	}

	const data: Tree[] = [{
			id: '1',
			label: '系统首页'
		},
		{
			id: '2',
			label: '基础表格',
			children: [{
					id: '15',
					label: '编辑'
				},
				{
					id: '16',
					label: '删除'
				}
			]
		},
		{
			id: '3',
			label: 'tab选项卡'
		},
		{
			id: '4',
			label: '表单相关',
			children: [{
					id: '5',
					label: '基本表单'
				},
				{
					id: '6',
					label: '文件上传'
				},
				{
					id: '7',
					label: '三级菜单',
					children: [{
							id: '8',
							label: '富文本编辑器'
						},
						{
							id: '9',
							label: 'markdown编辑器'
						}
					]
				}
			]
		},
		{
			id: '10',
			label: '自定义图标'
		},
		{
			id: '11',
			label: 'schart图表'
		},

		{
			id: '13',
			label: '权限管理'
		},
		{
			id: '14',
			label: '支持作者'
		}
	];

	// 获取当前权限
	const checkedKeys = ref < string[] > ([]);

</script>

<style scoped>
	.tree-wrapper {
		max-width: 500px;
	}

	.label {
		font-size: 14px;
	}
</style>
