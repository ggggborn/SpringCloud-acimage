export let delayRefresh = function(delaySeconds) {
	setTimeout(() => {
		location.reload();
	}, delaySeconds * 1000);
}
