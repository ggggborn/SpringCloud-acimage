<template>
	<div class="wrapper" :style="myStyle">
		<editor api-key="no-api-key" :init="options" v-model="Html" />
		<!-- <div style="width: 200px;" v-dompurify-html="Html"></div> -->
	</div>
</template>

<script>
	import Editor from '@tinymce/tinymce-vue'

	export default {
		name: 'EditBoard',
		components: {
			'editor': Editor
		},
		props: {
			width: {
				type: String,
				default: '720px'
			},
			margin: {
				type: String,
				default: '0px'
			},
			html:{
				type: String,
				default: ''
			}
		},
		computed: {
			myStyle() {
				return {
					width: this.width,
					marginLeft: this.margin
				}
			}
		},
		data() {
			return {
				options: {
					// plugins: 'lists link image table code help wordcount',
					plugins: 'lists link code wordcount emoticons',
					language: 'zh-Hans',
					emoticons_database_url: '../../../tinymce/plugins/emoticons/js/emojis.js',
					toolbar: [
						`code undo redo blocks
						| cut copy paste pastetext 
						| forecolor backcolor bold italic underline strikethrough link anchor 
						| alignleft aligncenter alignright alignjustify outdent indent 
						| styleselect formatselect fontselect fontsizeselect | bullist numlist 
						| blockquote subscript superscript removeformat 
						| table image media charmap emoticons hr pagebreak insertdatetime print preview 
						| fullscreen | bdmap indent2em lineheight axupimgs`
					],
				},
				Html: '内容'
				// Html: "<p>asdasd</p></br><p>asdasd</p><div οnclick='alert(123123)'>点我alert</div>"
			}
		},
		mounted() {
			this.Html=this.html;
		},
	}
</script>

<style scoped>
	.wrapper {
		display: block;
	}
</style>
