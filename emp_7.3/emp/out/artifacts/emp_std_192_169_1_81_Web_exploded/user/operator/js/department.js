var total=0;// 总页数
var pageIndex=0;// 当前页数
var pageSize=0;// 每页记录数
var totalRec=0;// 总记录数


function initPage(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total=total;
	this.pageIndex=pageIndex;
	this.pageSize=pageSize;
	this.totalRec=totalRec;
	
	showPageInfo();
}

//跳到第几页
//表单提交
function goPage(i){
	var page;
	if(i<0){
		page=$("#txtPage").attr("value");
	}else{
		page=i;
	}
	var size=$('#pageSize').attr("value");
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total;
	if(size<1 ||  !checkPage.test(size) ){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_14"));
		return;
	}
	if(size>500)
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_15"));
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm'].elements["pageIndex"].value =1;
		submitForm();
		return;
	}
	if(page-0>pagetotal){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_16"));
		document.forms['pageForm'].elements["pageIndex"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_17"));
		return;
	}
	document.forms['pageForm'].elements["pageIndex"].value=page;
	submitForm();
}

function submitForm(){
	var time = new Date().valueOf();
	$('#tableInfo').load($('#servletUrl').val(),
		{
			method:'getTable',
			time:time,
			depId:$('#depId').val(),
			lguserid:$("#lguserid").val(),
			lgcorpcode:$("#lgcorpcode").val(),
			pageIndex:$('#txtPage').val(),
			pageSize:$('#pageSize').val()
		}
	);
}

function showPageInfo(){
	var $pa = $('#pageInfo');
	var pathUrl = "website";
	$pa.empty();
	$pa.append(getJsLocaleMessage("user","user_xtgl_czygl_text_18")).append(totalRec).append(getJsLocaleMessage("user","user_xtgl_czygl_text_19"))
			.append(pageIndex).append('/').append(total).append(getJsLocaleMessage("user","user_xtgl_czygl_text_20"));
	$pa.append('&nbsp;');
	if(pageIndex<=1){
		$pa.append('<img src="'+pathUrl+'/images/first.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="'+pathUrl+'/images/back.gif" width="26" height="15" />');
	}else{
		$pa.append('<a href="javascript:goPage(1)"><img border="0" src="'+pathUrl+'/images/first.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage('+(pageIndex-1)+')"><img border="0" src="'+pathUrl+'/images/back.gif" width="26" height="15" /></a>');
	}
	$pa.append('&nbsp;');
	if(pageIndex<total){
		$pa.append('<a href="javascript:goPage('+(pageIndex-0+1)+')"><img border="0" src="'+pathUrl+'/images/next.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage('+total+')"><img border="0" src="'+pathUrl+'/images/last.gif" width="26" height="15" /></a>');
	}else{
		$pa.append('<img src="'+pathUrl+'/images/next.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="'+pathUrl+'/images/last.gif" width="26" height="15" />');
	}
	$pa.append('&nbsp;');
	$pa.append(getJsLocaleMessage("user","user_xtgl_czygl_text_21")).append('<input name="pageSize" id="pageSize" type="text" value="'+pageSize+'" size="4" />').append(getJsLocaleMessage("user","user_xtgl_czygl_text_22"));
	$pa.append(getJsLocaleMessage("user","user_xtgl_czygl_text_23")).append('<input name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" size="4" />').append(getJsLocaleMessage("user","user_xtgl_czygl_text_20"));
	$pa.append('&nbsp;');
	$pa.append('<a href="javascript:goPage(-1)"><img src="'+pathUrl+'/images/go.gif" id="goPage" width="26" height="15" /></a>');
}

