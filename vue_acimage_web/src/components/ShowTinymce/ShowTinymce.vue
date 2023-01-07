<template>

	<div class="wrapper" :style="myStyle">
		<editor api-key="no-api-key" :init="options" v-model="Html" />
	</div>
</template>

<script>
	import Editor from '@tinymce/tinymce-vue'

	export default {
		name: 'ShowTinymce',
		components: {
			'editor': Editor
		},
		props: {
			width: {
				type: String,
				default: '600px'
			},
			margin: {
				type: String,
				default: '0px'
			},
		},
		computed:{
			myStyle(){
				return {
					width:this.width,
					marginLeft:this.margin
				}
			}
		},
		data() {
			return {
				options: {
					// plugins: 'lists link image table code help wordcount',
					plugins: '',
					language: 'zh-Hans',
					// emoticons_database_url: '../../../tinymce/plugins/emoticons/js/emojis.js',
					toolbar: [],
					menubar:'',
					header:''

				},
				Html: ''
			}
		},
		methods:{
			getHtml(){
				
			}
		}
	}
</script>

<style scoped>
	.wrapper{
		display: block;
	}
</style>
