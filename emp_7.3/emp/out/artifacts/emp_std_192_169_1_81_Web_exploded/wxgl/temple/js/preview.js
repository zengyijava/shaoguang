function showContent(link){
	if(link.indexOf("weix_imgDetail.hts")!=-1){
		showLink(link+"&tempId="+$("#tempId").val()+"&fromTp=pc");
	}else{
		showLink(link);
	}
}
//模板弹出框的预览
function showLink(link){
	$("iframe[id^='msgPreviewFrame']",parent.document.body).attr("src",link);
}