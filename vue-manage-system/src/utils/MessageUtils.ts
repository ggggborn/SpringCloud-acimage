import {
	ElMessageBox,
	ElNotification
} from 'element-plus';
export default{
	notice(msg,durationSeconds){
		let newDuration=durationSeconds||2;
		ElNotification({
			title: '提示',
			message: msg,
			type: 'warning',
			duration: newDuration*1000
		});
	},
	success(msg,durationSeconds){
		let newDuration=durationSeconds||4;
		ElNotification({
			title: '成功',
			message: msg,
			type: 'success',
			duration: newDuration*1000
		});
	},
	confirm(msg){
		return ElMessageBox.confirm(msg, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		})
	},


}