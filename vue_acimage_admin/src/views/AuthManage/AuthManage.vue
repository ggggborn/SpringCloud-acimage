<template>
	<div class="wrapper">
		<el-table :data="homeCarouselImages" class="table-container">
			<el-table-column label="权限管理" header-align="center">
				<el-table-column label="序号" width="40">
					<template slot-scope="scope">
						{{ scope.$index+1 }}
					</template>
				</el-table-column>
				<el-table-column label="id" width="100">
					<template slot-scope="scope">
						{{ scope.row.id }}
					</template>
				</el-table-column>
				<el-table-column label="权限标识" width="120">
					<template slot-scope="scope">
						<i class="el-icon-time"></i>
						<span>{{ scope.row.updateTime}}</span>
					</template>
				</el-table-column>
				<el-table-column label="角色" width="120">
					<template slot-scope="scope">
						<img :src="$global.trueImageUrl(scope.row.url)" alt="" style="width: 100px;height: 100px">
					</template>
				</el-table-column>
				<el-table-column label="描述" width="180">
					<template slot-scope="scope">
						{{ scope.row.description }}
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
						<el-button size="mini" type="primary" @click="handleCover(scope.$index, scope.row)">覆盖图片
						</el-button>
						<el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除
						</el-button>
						<template v-if="homeCarouselImages.length==scope.$index+1">
							<el-button size="mini" type="primary" @click="addImageVisible=true">新增
							</el-button>
						</template>
					</template>
				</el-table-column>
			</el-table-column>
		</el-table>
		<!-- 修改描述弹出框 -->
		<el-dialog title="修改描述" :visible.sync="descriptionModifyVisible">
			<el-form>
				<el-form-item label="输入新描述">
					<el-input autocomplete="off" v-model="newDescription" maxlength="30"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="descriptionModifyVisible = false">取 消</el-button>
				<el-button type="primary" @click="modifyDescription(currentModifyId)">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 覆盖图片对话框 -->
		<el-dialog title="覆盖图片" :visible.sync="coverImageVisible">
			<el-form>
				<el-form-item>
					<el-upload action="#" ref="cover" :class="{'upload-plus-disabled':coverImageList.length>=1}"
						list-type="picture-card" :limit="1" :auto-upload="false" accept="image/*"
						:on-exceed="exceedLimit" :on-remove="handleRemove" :on-change="handleCoverImageChange">
						<i class="el-icon-plus"></i>
					</el-upload>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="coverImageVisible = false">取 消</el-button>
				<el-button type="primary" @click="coverImage(coverImageId)">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 新增图片对话框 -->
		<el-dialog title="新增图片" :visible.sync="addImageVisible">
			<el-form :model="addImageForm" :rules="rules" ref="addImageForm">
				<el-form-item>
					<el-upload action="#" ref="upload" :class="{'upload-plus-disabled':addImageList.length>=1}"
						list-type="picture-card" :limit="1" :auto-upload="false" accept="image/*"
						:on-exceed="exceedLimit" :on-remove="handleRemove" :on-change="handleAddImageChange">
						<i class="el-icon-plus"></i>
					</el-upload>
				</el-form-item>
				<el-form-item label="描述" prop="description">
					<el-input v-model="addImageForm.description"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="addImageVisible = false">取 消</el-button>
				<el-button type="primary" @click="addImage">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 删除提示框 -->
		<el-dialog title="提示" :visible.sync="deleteConfirmVisible" width="30%">
			<span>确定删除吗？操作不可逆！</span>
			<span slot="footer" class="dialog-footer">
				<el-button @click="deleteConfirmVisible = false">取 消</el-button>
				<el-button type="primary" @click="deleteImage(currentDeleteId)">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import  { 
				addHomeCarouselImage,
				modifyHomeCarouselDescription,
				deleteHomeCarouselImage,
				coverHomeCarouselImage,
				queryCurrentHomeCarousel
	} from '@/api/HomeCarousel.js'
	import {Code} from '@/utils/result.js'
	import CommonUtils from '@/utils/CommonUtils.js'

	export default {
		name: 'AuthManage',
		data() {
			return {
				//修改图片
				descriptionModifyVisible: false,
				currentModifyId:0,
				newDescription: '',
				//删除图片
				deleteConfirmVisible: false,
				currentDeleteId:0,
				//覆盖图片
				coverImageVisible: false,
				coverImageList: [],
				coverImageId:0,

				homeCarouselImages: [
					// {
					// 	id:100861008610086,
					// 	updateTime: '2016-05-02 22:22:22',
					// 	description: '好看的',
					// 	url: '',
					// 	size: 1008610,
					// },
				],
				//新增图片
				addImageList:[],
				addImageVisible:false,
				addImageForm:{
					description:'',
				},
				rules:{
					description: [
						{ required: true, message: '请输入描述', trigger: 'blur' },
						{ min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
					],
				}
				
			};
		},
		mounted() {
			this.getHomeCarouselImages();
		},
		methods: {
			handleRemove(file, fileList){},
			handleCoverImageChange(file, fileList) {
				this.coverImageList = fileList;
				console.log(this.coverImageList);
			},
			handleAddImageChange(file, fileList) {
				this.addImageList = fileList;
				console.log(this.addImageList);
			},
			exceedLimit() {
				this.$global.popupHint('只能上传一张图片');
			},
			handleEdit(index, row) {
				this.newDescription = row.description;
				this.currentModifyId=row.id;
				this.descriptionModifyVisible = true;
			},
			handleCover(index, row) {
				this.coverImageVisible = true;
				this.coverImageId=row.id;
				console.log(index, row);
			},
			handleDelete(index, row) {
				this.deleteConfirmVisible = true;
				this.currentDeleteId=row.id;
			},
			addImage() {
				if(this.addImageList.length==0){
					this.$global.popupHint("请选择至少一张图片");
					return;
				}
				if(this.validateInputOfAddImage('addImageForm')==false){
					return;
				}
				
				let reqData = new FormData();
				reqData.append("description", this.addImageForm.description);
				reqData.append("image",this.addImageList[0].raw);
				
				addHomeCarouselImage(reqData).then(result=>{
					if (result.code == Code.OK) {
						this.$global.popupSuccess('增加成功');
					}
				});
				this.addImageVisible=false;
				
				CommonUtils.delayRefresh(2);
			},
			deleteImage(imageId){
				let params={'id':imageId};
				this.$global.popupMsgIfOk(deleteHomeCarouselImage(params),'删除成功');
				this.deleteConfirmVisible=false;
				
				CommonUtils.delayRefresh(2);
			},
			modifyDescription(imageId){
				let params={'id':imageId,'description':this.newDescription};
				this.$global.popupMsgIfOk(modifyHomeCarouselDescription(params),'修改成功');
				this.descriptionModifyVisible=false;
				
				CommonUtils.delayRefresh(2);
				
			},
			coverImage(imageId){
				if(this.coverImageList.length==0){
					this.$global.popupHint("请选择至少一张图片");
					return;
				}
				
				let reqData = new FormData();
				reqData.append("id", imageId);
				reqData.append("image",this.coverImageList[0].raw);
				this.$global.popupMsgIfOk(coverHomeCarouselImage(reqData),'覆盖成功');
				this.coverImageVisible = false;
				
				CommonUtils.delayRefresh(2);
			},
			getHomeCarouselImages(){
				queryCurrentHomeCarousel().then(result=>{
					if(result.code==Code.OK){
						this.homeCarouselImages=result.data;
					}
				});
				
			},
			validateInputOfAddImage(formRef){
				let returnValue=true;
				this.$refs[formRef].validate((valid) => {
					if (!valid) {
						returnValue= false;
					}
				});
				return returnValue;
			}
		},

	}
</script>

<style scoped>
	.wrapper {
		width: calc(100vw - 200px);
		display: block;
	}

	.table-container {
		width: 100%;
		margin-left: 5%;
	}
	
	/* 	.upload-container {
			width: 800px;
			border: 2px solid skyblue;
			border-radius: 5px;
			margin-top: 20px;
			display: block;
			margin-left: calc((100vw - 200px - 800px) / 2);
		} */
</style>
