export default {
	html2Text(htmlString) {
		return htmlString.replace(/<(style|script|iframe)[^>]*?>[\s\S]+?<\/\1\s*>/gi, '')
			.replace(/<[^>]+?>/g, '')
			.replace(/\s+/g, ' ')
			.replace(/ /g, ' ')
			.replace(/>/g, ' ')
			.replace(/&nbsp;/gm,' ');
	}
}
