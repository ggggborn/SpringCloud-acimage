<template>
	<div class="login-wrap">
		<div class="ms-login">
			<div class="ms-title">后台管理系统</div>
			<el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
				<el-form-item prop="email">
					<el-input v-model="param.email" placeholder="email">
						<template #prepend>
							<el-button :icon="User"></el-button>
						</template>
					</el-input>
				</el-form-item>
				<el-form-item prop="password">
					<el-input type="password" placeholder="password" v-model="param.password" show-password>
						<template #prepend>
							<el-button :icon="Lock"></el-button>
						</template>
					</el-input>
				</el-form-item>

				<el-input placeholder="验证码" v-model="param.verifyCode" maxlength="8" style="width:130px;" clearable>
				</el-input>
				<br />
				<img alt="单击图片刷新!" src="/api/admin/logins/commonCode"
					onclick="this.src='/api/admin/logins/commonCode?d='+new Date()*1">
				<br />
				<div class="login-btn">
					<el-button type="primary" @click="submitForm(login)">登录</el-button>
				</div>
				<p class="login-tips">验证码过期时间30s</p>
			</el-form>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { ref, reactive } from 'vue';
	import { useTagsStore } from '../store/tags';
	import { usePermissStore } from '../store/permiss';
	import { useRouter } from 'vue-router';
	import { ElMessage } from 'element-plus';
	import type { FormInstance, FormRules } from 'element-plus';
	import { Lock, User } from '@element-plus/icons-vue';
	import { JSEncrypt } from 'jsencrypt'

	import { getPublicKey, doLogin } from '@/api/login'
	import { useStore } from '@/store/store'
	import { Code } from '@/utils/result'
	import MessageUtils from '@/utils/MessageUtils';


	interface LoginInfo {
		email: string;
		password: string;
		verifyCode: string;
	}

	const store = useStore();
	const router = useRouter();
	const param = reactive < LoginInfo > ({
		email: 'xlg@qq.com',
		password: 'xlg123456',
		verifyCode: ''
	});

	const rules: FormRules = {
		email: [{
			required: true,
			message: '请输入邮箱',
			trigger: 'blur'
		}],
		password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
	};
	const permiss = usePermissStore();
	const login = ref < FormInstance > ();
	const submitForm = (formEl: FormInstance | undefined) => {
		if (!formEl) return;
		formEl.validate((valid: boolean) => {
			if (valid) {
				// localStorage.setItem('ms_username', param.username);
				const keys = permiss.defaultList['admin'];
				permiss.handleSet(keys);
				localStorage.setItem('ms_keys', JSON.stringify(keys));
				// router.push('/');
				getPublicKey().then(result => {
					return result.data;
				}).then(publicKey => {
					let encryptor = new JSEncrypt();
					encryptor.setPublicKey(publicKey);
					//加密
					let encryptRes = encryptor.encrypt(param.password);
					if (encryptRes != false) {
						let adminLoginReq = {
							email: param.email,
							password: encryptRes,
							verifyCode: param.verifyCode
						}
						doLogin(adminLoginReq).then((res: any) => {
							if (res.code == Code.OK) {
								store.setToken(res.token)
								MessageUtils.success('登录成功', 1);
								setTimeout(() => { router.push({ path: '/' }); }, 500);
							}
						})
					}
				})
			} else {
				ElMessage.error('失败');
				return false;
			}
		});
	};

	const tags = useTagsStore();
	tags.clearTags();
</script>

<style scoped>
	.login-wrap {
		position: relative;
		width: 100%;
		height: 100%;
		background-image: url(../assets/img/login-bg.jpg);
		background-size: 100%;
	}

	.ms-title {
		width: 100%;
		line-height: 50px;
		text-align: center;
		font-size: 20px;
		color: #fff;
		border-bottom: 1px solid #ddd;
	}

	.ms-login {
		position: absolute;
		left: 50%;
		top: 50%;
		width: 350px;
		margin: -190px 0 0 -175px;
		border-radius: 5px;
		background: rgba(255, 255, 255, 0.3);
		overflow: hidden;
	}

	.ms-content {
		padding: 30px 30px;
	}

	.login-btn {
		text-align: center;
	}

	.login-btn button {
		width: 100%;
		height: 36px;
		margin-bottom: 10px;
	}

	.login-tips {
		font-size: 12px;
		line-height: 30px;
		color: #fff;
	}
</style>
