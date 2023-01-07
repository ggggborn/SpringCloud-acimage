import {
	Message,
	MessageBox,
	Loading,
	Notification
} from 'element-ui';
export default{
	notice(msg,durationSeconds,type){
		let newType=type||'warning';
		let newDuration=durationSeconds||4;
		Notification({
			title: '提示',
			message: msg,
			type: newType,
			duration: newDuration*1000
		});
	},
	success(msg,durationSeconds){
		let newDuration=durationSeconds||4;
		Notification({
			title: '成功',
			message: msg,
			type: 'success',
			duration: newDuration*1000
		});
	},
	confirm(msg){
		return MessageBox.confirm(msg, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		})
	}

}