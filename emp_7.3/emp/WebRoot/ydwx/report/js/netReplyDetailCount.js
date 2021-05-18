//发送时间
function rtime() {
	var max = "2099-12-31 23:59:59";
	var v = $("#sendtime").attr("value");
	WdatePicker( {
		dateFmt : 'yyyy-MM-dd HH:mm:ss'
	});
}

//接收时间
function stime() {
	var max = "2099-12-31 23:59:59";
	var v = $("#recvtime").attr("value");
	var min = "1900-01-01 00:00:00";
	WdatePicker( {
		dateFmt : 'yyyy-MM-dd HH:mm:ss'
	});
}

//返回
function back()
{
  	location.href=$("#path").val()+"/wx_count.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&skip=true";
}
