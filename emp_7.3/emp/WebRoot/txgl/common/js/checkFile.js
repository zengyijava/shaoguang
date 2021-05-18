//检测文件是否存在，各网页有涉及到查看短信号码附件的均需调用此文件
function checkFile(url,src) {
	var isjq = $("#isCluster").val();
	if(1==isjq)
	{
		checkFile2(url,$("#httpUrl").val());
		return;
	}
	$.post(src+"/common.htm?method=checkFile", {
		url : url
	},
		function(result) {
			if (result == "true") {
				window.showModalDialog(src + "/" + url +"?Rnd="+ Math.random());
				//window.open(src + "/" + url,"_blank");
			} else if (result == "false")
				alert("文件不存在");
			else
				alert("出现异常,无法跳转");
	});
}
//文件下载
function uploadFiles(url,src)
{
	var isjq = $("#isCluster").val();
	if(1==isjq)
	{
		uploadFiles2(url,$("#httpUrl").val());
		return;
	}
	$.post(src+"/common.htm?method=checkFile", {
		url : url
	},
		function(result) {
			if (result == "true") {
				location.href=src+"/doExport.hts?u="+url;				
			} else if (result == "false")
				alert("文件不存在或该文件无访问权限");
			else
				alert("出现异常,无法跳转");
	});

}

function checkFile2(url,src) {
	window.showModalDialog(src + "checkFile?url=" + url +"&Rnd="+ Math.random());
}
function uploadFiles2(url,src)
{
	window.open(src + "checkFile?type=downLoad&url=" + url +"&Rnd="+ Math.random());	
}