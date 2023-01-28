<template>
	<div class="wrapper" :style="myStyle">
		<editor api-key="no-api-key" :init="options" v-model="Html" />
		<!-- <div style="width: 200px;" v-dompurify-html="Html"></div> -->
	</div>
</template>

<script>
	import Editor from '@tinymce/tinymce-vue'
	import { uploadTopicImage } from '@/api/image.js'
	import { Code } from '@/utils/result.js'

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
			html: {
				type: String,
				default: ''
			}
		},
		computed: {
			myStyle() {
				return {
					width: this.width,
					marginLeft: this.margin,
				}
			}
		},
		data() {
			return {
				options: {
					// plugins: 'lists link image table code help wordcount',
					plugins: 'lists link code wordcount emoticons image table',
					height:600,
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
					images_file_types: 'jpeg,jpg,png,gif,bmp,webp',
					relative_urls : true,//是否相对地址
					convert_urls: false,//是否转换地址
					// images_upload_url: "/manage/upload", //指定上传图片的后端处理程序的URL。
					// images_upload_base_path: "/demo",
					images_upload_handler: (blobInfo, success, failure) => new Promise((resolve, reject) => {
						const formData = new FormData();
						formData.append("imageFile", blobInfo.blob());
						uploadTopicImage(formData).then((res) => {
							if (res.code == Code.OK) {
								console.log(res);
								resolve(res.data); //将图片展示到编辑器中
							} else {
								reject(res.msg)
							}
						}).catch(() => {
							// resolve("static/image/user-rank-header.jpg");
							reject("上传失败");
						});
					}),
					content_style: `
					  body {font-size: 14pt;}
					  html, body                { height:100%; }
					  img                       { max-width:100%; display:block;height:auto; }
					  a                         { text-decoration: none; }
					  p                         { line-height:1.6; margin: 0px; }
					  table                     { word-wrap:break-word; word-break:break-all;max-width:100%; border:none; border-color:#999; }
					  .mce-object-iframe        { width:100%; box-sizing:border-box; margin:0; padding:0; }
					  ul,ol                     { list-style-position:inside; }
					  `,
					fontsize_formats: "8pt 10pt 12pt 14pt 18pt 24pt 36pt",

				},
				Html: '内容',
			}
		},
		mounted() {
			this.Html = this.html;
		}
	}
</script>

<style scoped>
	.wrapper {
		display: block;
	}
</style>
