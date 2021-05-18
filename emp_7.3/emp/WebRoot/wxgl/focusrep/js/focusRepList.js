//搜索
function addFocusRep(beanSize,accountSize)
{
	beanSize = parseInt(beanSize);
	accountSize = parseInt(accountSize);
	if(accountSize==0){
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_14"));
	}else if(beanSize >= accountSize){
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_15"));
	}else {
		var url = $("#pathUrl").val() + "/weix_focusReply.htm?method=doAdd&lguserid="
    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
		doGo(url);
	}
}

//关注时回复预览
function preview(evtId){
	var url = $("#pathUrl").val() + "/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&evtId=" + evtId;
	/*$("#msgPreviewFrame").attr("src",url);
	 dlog = art.dialog.through({
			title: "预览",
		    content: document.getElementById('divBox'),
		    id: 'divBox',
		    lock: true
	  });*/
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
		art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_16"));
	}
	else if(i==2)
	{
		art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_17"));
	}
	else if(i==3){
		art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_18"));
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

//删除js
function del(recordId){
	if(recordId=="all"){
		if($("input[type='checkbox'][id!='selectAll']:checked").size()==0){
			   alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_19"));
			   return;
		}
	}
	
	if(!confirm(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_20"))){
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
	
	$.post("weix_focusReply.htm",{method:"delete",lgcorpcode:$("#lgcorpcode").val(),lguserid:$("#lguserid").val(),recordIds: recordIds,isAsync:"yes"},
	    	function(result)
	    	{
				if(result == "outOfLogin")
    	    	{
    	    		$("#logoutalert").val(1);
    	    		location.href=$("#pathUrl").val() + "/quit";
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
function toUpdate(aId, evt_id)
{
	var url=$("#pathUrl").val() + "/weix_focusReply.htm?method=doEdit&lguserid="
	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&evt_id="+evt_id+"&aId="+aId;
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
	
	$('#search').click(function(){
		var flag=true;
		var title=$("#title").val();
		if(title=="%" || title=="_" || title=="%_" || title=="_%"){
			//alert("%和_必须包括别的字符");
            alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_27"));
            flag=false;
		}
		if(flag){
            submitForm();
		}
	});
});