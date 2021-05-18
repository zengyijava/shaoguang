//选择模板初始化
function initRadioDiv2()
{
	var message = $("#tempName").val();
	$("#radioDiv2").empty();
	var content = "\""+ message +"\"" + getJsLocaleMessage("weix","weix_qywx_mrhf_text_15") + "<a onclick='javascript:toPreview("+$("#tid").val() + getJsLocaleMessage("weix","weix_qywx_mrhf_text_16");
	var html = getJsLocaleMessage("weix","weix_qywx_mrhf_text_17");
	html = html + "<td colspan=\"3\">"+content+"</td>"
	$("#radioDiv2").html(html);
}

//保存表单提交数据
function save()
{
	var AId=$("#AId").val();
	var tid=$("#tid").val();
	var title=$.trim($("#title").val());
	
	if(AId==null|| AId=="")
	{
		alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_5"));
		return;
	}
	if(title==null|| title=="")
	{
		alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_6"));
		return;
	}
	if(title.length>32)
	{
		alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_7"));
		return;
	}
	var msgText=$("#msgText").val();
	if(msgText==undefined||msgText==null){
		msgText = "";
	}
	if($("#radio1:checked").size()==1){
		tid = "";
		if(msgText.length<=0){
			alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_8"));
			return;
		}
	}

	if($("#radio2:checked").size()==1){
		msgText = "";
		if(tid.length<=0){
			alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_9"));
			return;
		}
	}
	
	if(tid.length<=0 && msgText.length<=0)
	{
		alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_10"));
		return;
	}
	else if(msgText.length>500)
	{
		alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_12"));
		return;
	}
	
	$("input[type='button']").attr("disabled","disabled");
	$.post("cwc_defaultrep.htm",
			{method:"update",lgcorpcode:$("#lgcorpcode").val(),msgText:msgText,AId: AId,title:title,tid:tid,OpType:"edit",tet_id:$("#tet_id").val(),userid:$("#lguserid").val(),isAsync:"yes"},
	    	function(result){
    	    	if(result == "outOfLogin")
    	    	{
    	    		$("#logoutalert").val(1);
    	    		location.href=$("#pathUrl").val() + "/common/logoutEmp.html";
    	    		return;
    	    	}
    	    	if(result=="success")
	    		{
		    		alert(getJsLocaleMessage("common","common_text_1"));
		    		window.location.href=$("#pathUrl").val() + "/cwc_defaultrep.htm?method=find&lguserid="
			    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType=0";
	    		}
    	    	else if(result=="noTemplate")
	    		{
	    			alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_13"));
	    		}
	    		else if(result=="fail")
	    		{
	    			alert(getJsLocaleMessage("common","common_text_2"));
	    		}
	    		else
	    		{
		    		alert(getJsLocaleMessage("common","common_text_3"))
	    		}
    	    	$("input[type='button']").attr("disabled","disabled");
    	    	$("#tid").val("");
    	    	window.location.href=$("#pathUrl").val() + "/cwc_defaultrep.htm?method=find&lguserid="
    	    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType=0";
	});
}

//返回上一级
function doreturn()
{
	window.location.href=$("#pathUrl").val() + "/cwc_defaultrep.htm?method=find&lguserid="
	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
}
//选择模板
function tempSure()
{
	var $fo = $("#tempFrame").contents();
	var $ro = $fo.find("input[type='radio']:checked");
	
	if($ro.val() == undefined)
	{
		alert(getJsLocaleMessage("weix","weix_qywx_mrhf_text_14"));
		return;
	}else
	{
		//将模板id赋值给隐藏域
		$("#tid").val($ro.val());
		var message = $ro.next("xmp").text();
		$("#radioDiv2").empty();
		var content = "\""+ message +"\"" +  getJsLocaleMessage("weix","weix_qywx_mrhf_text_15") +"<a onclick='javascript:toPreview("+$ro.val()+  getJsLocaleMessage("weix","weix_qywx_mrhf_text_16");
		var html =  getJsLocaleMessage("weix","weix_qywx_mrhf_text_17");
		html = html + "<td colspan=\"3\">"+content+"</td>"
		$("#radioDiv2").html(html);
	}
	$("#tempDiv").dialog("close");
}

//点击选择模板
function chooseTemp()
{
	$("#tempDiv").dialog("open");
}

//取消模板
function quxiaoTemp()
{
	if($("#tid").val()!=null && $("#tid").val()!="")
	{
		$("#tid").val("");
	}
}

//关闭弹出框
function tempCancel()
{
	$("#tempDiv").dialog("close");
}

//加载模板选择弹出框
function loadChooseTemp(){
	var tpath = $("#cpath").val();
	var frameSrc = $("#tempFrame").attr("src");
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	frameSrc = tpath+"/cwc_defaultrep.htm?method=getLfTemplateByWeix&dsflag=1&lguserid="
		+lguserid+"&lgcorpcode="+lgcorpcode;
	$("#tempFrame").attr("src",frameSrc);
}

//浏览效果
function toPreview(tempId){
	url = $("#pathUrl").val() + "/cwc_replymanger.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	$("#msgPreviewFrame2").attr("src",url);
	$("#divBox2").dialog("open");
}

//弹出框的预览效果
function preview(tempId){
	url = $("#pathUrl").val() + "/cwc_replymanger.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	$("#msgPreviewFrame2").attr("src",url);
	$("#divBox2").dialog("open");
}

//回复类型选择
function selectTemp(value){
	var tp = parseInt(value);
	if(tp==1){
		$("#radioDiv1").hide();
		$("#radioDiv2").show();
		$("#chooseTemp").show().css("display","inline");
	}else{
		$("#radioDiv1").show();
		$("#radioDiv2").hide();
		$("#chooseTemp").hide();
		if($("#msgText").val().length<=0)
		{
			$("#text_title").show();
		}
	}	
}

//去掉匹配的字符
function secapp(obj,reg){
	var val=$(obj).val();
	if(reg.test(val)){
		$(obj).val($(obj).val().replace(reg,''));
	}
}

$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
	//注册回复模板选择
	$("#tempDiv").dialog({
		autoOpen: false,
		height:520,
		width: 750,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
		}
	});

	//注册预览效果弹出框
	$("#divBox2").dialog({
		autoOpen: false,
		height:510,
		width: 300,
		modal: true,
		bgiframe: true ,
		overlay: {opacity: 1.0, background: "white" ,overflow:'hidden'},
		close:function(){
	      $("#msgPreviewFrame2").attr("src","");
		}
 	});
 	
	//加载模板选择弹出框
	loadChooseTemp();
	
	//选择手工编辑/引用模板
	if($("#radioType").val()=="2"){
		initRadioDiv2();
		//selectTemp(1);
		$("#radio2").click();
		$("#msgText").val("");
	}else{
		//selectTemp(0);
		$("#radio1").click();
		$("#radioDiv2").empty();
	}
	
	$("#msgText").manhuaInputLetter({					       
		len : 500,//限制输入的字符个数				       
		showId : "sid",//显示剩余字符文本标签的ID
		showNum: $("#tempTextSize").val().length
	});
	
	$("#title").bind('keyup blur',function(){
		secapp(this,/[<'">]/g);
	})
});