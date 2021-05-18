function check() {
	var flag = true;
	var imgs = $.trim($("#chooseImg").val());
	if (imgs != "") {
		imgs = imgs.toString().substring(imgs.toString().lastIndexOf(".") + 1, imgs.toString().length);
		imgs = imgs.toUpperCase();
	}
	if (imgs == "") {
		alert("请选择要上传的图片文件！");
		flag = false;
	} else if (imgs != 'PNG') {
		alert("不支持的图片格式！");
		flag = false;
	}
	return flag;
}

function doUp() {

	var pathUrl = $("#pathUrl").val();
	if (check()) { //这里判断上传的 图片或者声音是否合法	
		$.ajaxFileUpload({
			url: pathUrl + '/uploadLogo.hts?method=upload&lgcorpcode=' + lgcorpcode,
			//处理上传文件的服务端 
			secureuri: false,
			//是否启用安全提交，默认为false
			fileElementId: 'chooseImg',
			//与页面处理代码中file相对应的ID值
			dataType: 'json',
			//返回数据类型:text，xml，json，html,scritp,jsonp五种
			success: function(data) {
				if (data == true) {
					alert("上传成功！");
				} else if (data == false) {
					alert("上传失败！");
				}
			}
		});
	}
}