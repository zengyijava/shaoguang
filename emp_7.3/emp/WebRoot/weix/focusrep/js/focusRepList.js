//搜索
function addFocusRep(beanSize,accountSize)
{
	beanSize = parseInt(beanSize);
	accountSize = parseInt(accountSize);
	if(accountSize==0){
		alert(getJsLocaleMessage("weix","weix_qywx_gzsdhf_text_1"));
	}else if(beanSize >= accountSize){
		alert(getJsLocaleMessage("weix","weix_qywx_gzsdhf_text_2"));
	}else {
		window.location.href= $("#pathUrl").val() + "/cwc_focusrep.htm?method=doAdd&lguserid="
    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
	}
}

//关注时回复预览
function preview(evtId){
	url = $("#pathUrl").val() + "/cwc_replymanger.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&evtId=" + evtId;
	$("#msgPreviewFrame").attr("src",url);
	$("#divBox").dialog("open");
}

//弹出框预览
function modify(t,i)
{
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	if(i==1)
	{
		$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_1"));
	}
	else if(i==2)
	{
		$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_18"));
	}
	else if(i==3){
		$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_4"));
	}
	$('#modify').dialog('open');
}

//全选中
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}

//删除js
function del(recordId){
	if(recordId=="all"){
		if($("input[type='checkbox'][id!='selectAll']:checked").size()==0){
			   alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_19"));
			   return;
		}
	}
	
	if(!confirm(getJsLocaleMessage("weix","weix_qywx_mrhf_text_20"))){
		return;
	}
	var recordIds = "";
	var ids = [];
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
	
	$.post("cwc_focusrep.htm",{method:"delete",lgcorpcode:$("#lgcorpcode").val(),lguserid:$("#lguserid").val(),recordIds: recordIds,isAsync:"yes"},
	    	function(result)
	    	{
				if(result == "outOfLogin")
    	    	{
    	    		$("#logoutalert").val(1);
    	    		location.href=$("#pathUrl").val() + "/common/logoutEmp.html";
    	    		return;
    	    	}
    	    	if(result=="fail")
    	    	{
	    	    	alert(getJsLocaleMessage("common","common_text_5"));
    	    	}
    	    	else if(result=="error")
    	    	{
	    	    	alert(getJsLocaleMessage("common","common_text_3"));
    	    	}
    	    	else if(result.indexOf("success")!=-1)
    	    	{
	    	    	alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_21") + result.replace("success","") + getJsLocaleMessage("weix","weix_qywx_mrhf_text_22"))
    	    	}
    	    	else
    	    	{
    	    		alert(getJsLocaleMessage("common","common_text_3"));
    	    	}
			submitForm();
		});
}

//点击修改按钮
function toUpdate(aId, evt_id)
{
	window.location.href=$("#pathUrl").val() + "/cwc_focusrep.htm?method=doEdit&lguserid="
	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&evt_id="+evt_id+"&aId="+aId;
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
	$('#modify').dialog({
		autoOpen: false,
		width:250,
	    height:200
	});
	$("#divBox").dialog({
		autoOpen: false,
		height:510,
		width: 300,
		modal: true,
		close:function(){
	  $("#msgPreviewFrame").attr("src","");
		}
	});
});