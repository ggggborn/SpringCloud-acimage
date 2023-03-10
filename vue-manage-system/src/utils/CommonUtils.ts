import MessageUtils from '@/utils/MessageUtils'
import { Code } from "@/utils/result"
export default {
	isEmpty(object) {
		if (object == undefined || object == '' || object == null || object == {} || object == []) {
			return true;
		}
		return false;
	},

	popMsgAndRefreshIfOk(request, msg, delaySeconds: void) {
		request.then(result => {
			console.log(result);
			if (result.code == Code.OK) {
				MessageUtils.success(msg, 1);
				if (!this.isEmpty(delaySeconds)) {
					this.delayRefresh(delaySeconds);
				}
			}
		});
	},
	popMsgIfOk(request, msg) {
		this.popMsgAndRefreshIfOk(request, msg);
	},

	delayRefresh(delaySeconds) {
		setTimeout(() => {
			location.reload();
		}, delaySeconds * 1000);
	},

	getUrlParams(url) {
		let firstIndex = url.indexOf('?');
		if (firstIndex == -1) return;
		let argStr = url.slice(firstIndex + 1, url.length);

		let args = argStr.split("&");
		let params = {};
		for (let i = 0; i < args.length; i++) {
			let keyAndValues = args[i].split("=");
			params[keyAndValues[0]] = keyAndValues[1];
		}
		return params;
	},
}
