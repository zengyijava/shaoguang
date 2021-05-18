//检测文件是否存在，各网页有涉及到查看短信号码附件的均需调用此文件
function checkFile(url,src) {
	//var isjq = $("#isCluster").val();
	//if(1==isjq)
	//{
	//	checkFile2(url,$("#httpUrl").val());
	//	return;
	//}
//	$.post(src+"/common.htm?method=checkFile", {
//		url : url
//	},
//		function(result) {
//			if (result == "true") {
//				window.showModalDialog(src + "/" + url +"?Rnd="+ Math.random());
//				//window.open(src + "/" + url,"_blank");
//			} else if (result == "false")
//				alert("文件不存在");
//			else
//				alert("出现异常,无法跳转");
//	});
	$.post(src+"/common.htm?method=checkFile", {
		url : url
	},
		function(result) {
			if (result.indexOf("true") == 0) {
                src = result.substring(4)||src;
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
//	var isjq = $("#isCluster").val();
//	if(1==isjq)
//	{
//		uploadFiles2(url,$("#httpUrl").val());
//		return;
//	}
//	$.post(src+"/common.htm?method=checkFile", {
//		url : url
//	},
//		function(result) {
//			if (result == "true") {
//				location.href=src+"/doExport.hts?u="+url;				
//			} else if (result == "false")
//				alert("文件不存在或该文件无访问权限");
//			else
//				alert("出现异常,无法跳转");
//	});
	
	$.post(src+"/common.htm?method=checkFile", {
		url : url
	},
		function(result) {
			if (result.indexOf("true") == 0) {
                var _src = result.substring(4);
                if(_src.length > 0){
                    uploadFiles2(url,_src);
                }else{
                    download_href(src+"/doExport.hts?u="+url);
                }
			} else if (result == "false")
				alert("文件不存在或该文件无访问权限");
			else
				alert("出现异常,无法跳转");
	});

}

function download_href(url){
    window.IE_UNLOAD  = true;
    window.location.href = url;
}

function checkFile2(url,src) {
	window.showModalDialog(src + "checkFile?url=" + url +"&Rnd="+ Math.random());
}
function uploadFiles2(url,src)
{
	window.open(src + "checkFile?type=downLoad&url=" + url +"&Rnd="+ Math.random());	
}

if(!window.showModalDialog){
	window.showModalDialog = function(url){
		if(iPath.indexOf('frame3.0')!=-1){
			window.parent.open(url,'','height=200,width=540,left=450,top=300');
		}else{
			window.parent.open(url,'','height=450,width=700,left=400,top=200');
		}
	}
}
//function checkFile2(url,src) {
//	window.showModalDialog(src + "checkFile?url=" + url +"&Rnd="+ Math.random());
//}
//function uploadFiles2(url,src)
//{
//	window.open(src + "checkFile?type=downLoad&url=" + url +"&Rnd="+ Math.random());	
//}