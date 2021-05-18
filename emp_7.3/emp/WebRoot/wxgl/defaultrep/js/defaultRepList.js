/*默认回复管理-列表页面*/
//搜索
function showAddAccountTmp(showType)
{
	var url = $("#pathUrl").val()+"/weix_defaultReply.htm?method=doAdd&lguserid="
	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType="+showType+"&time="+new Date().getTime();
	doGo(url);
}

//关注时回复预览
function preview(tetId){
	var url = $("#pathUrl").val() + "/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tetId=" + tetId;
	showbox({src:url});
}

//弹出框预览
function modify(t,i)
{
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	dlog = art.dialog.through({
	    content: document.getElementById('modify'),
	    id: 'modify',
	    lock: false
	});
	if(i==1)
	{
		art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_18"));
	}
	else if(i==2)
	{
		art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_19"));
	}
	else if(i==3){
		art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_20"));
	}
}

//全选中
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}

//删除
function del(recordId)
{
	if(recordId=="all"){
		if($("input[type='checkbox'][id!='checkall']:checked").size()==0){
				
			   alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_21"));
			   return;
		}
	}
	if(!confirm(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_22")))
	{
		return;
	}
	var recordIds = "";
	var ids = [];
	//删除多条
	if(recordId=="all"){
		$("input[type='checkbox']:checked").each(function(){
			if($(this).attr("id")!="selectAll"){
				ids.push($(this).attr("value"));
			}
		});
		recordIds = ids.join(","); 
	}else {
		recordIds = recordId;
	}
	
	$.post("weix_defaultReply.htm",
		{method:"delete",lgcorpcode:$("#lgcorpcode").val(),recordIds: recordIds,OpType:"add",isAsync:"yes"},
    	function(result)
    	{
			if(result == "outOfLogin")
	    	{
	    		$("#logoutalert").val(1);
	    		location.href= $("#pathUrl").val() + "/quit";
	    		return;
	    	}
	    	if(result=="fail")
	    	{
    	    	
    	    	alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_23"));
	    	}
	    	else if(result=="error")
	    	{
    	    	alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_11"));
	    	}
	    	else if(result.indexOf("success")!=-1)
	    	{
    	    	alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_24")+result.replace("success","")+getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_25"))
	    	}
	    	else
	    	{
	    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_11"));
	    	}
		submitForm();
	});
}

//点击修改按钮
function toUpdate(tet_id)
{
	var url =$("#pathUrl").val() + "/weix_defaultReply.htm?method=doEdit&lguserid="
	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&tet_id="+tet_id;
	doGo(url);
}

$(function(){
	getLoginInfo("#hiddenValueDiv");
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
		}, function() {
		$(this).removeClass("hoverColor");
	});
	
	//分页
	initPage($("#currentTotalPage").val(),$("#currentPageIndex").val(),$("#currentPageSize").val(),$("#currentTotalRec").val());
	
	$('#search').click(function(){submitForm();});
	
	//表格提示
	$("#pageForm table td").mouseover(function(){
			$(this).attr("title",$(this).find("label").text());
	});
	$("#pageForm table td").mouseout(function(){
			$(this).attr("title","");
	});
});