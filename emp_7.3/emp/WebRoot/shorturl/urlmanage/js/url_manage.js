//新建url地址
function addUrl()
{
	var urlname = $.trim($('#urlNameAdd').val());
	var urlmsg = $.trim($('#urlDescriptionAdd').val());
	var srcurl = $.trim($('#urlCodeAdd').val());
	
	$('#addcancel').attr("disabled",true);
	$('#addsubmit').attr("disabled",true);
	if(urlname == ""){
		alert("长链接名称必填项不允许为空！请重新输入。");
		$('#urlNameAdd').val("");
		$('#urlNameAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}
	if(urlmsg == ""){
		alert("长链接内容描述必填项不允许为空！请重新输入。");
		$('#urlDescriptionAdd').val("");
		$('#urlDescriptionAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}
	if(srcurl == ""){
		alert("长链接必填项不允许为空！请重新输入。");
		$('#urlCodeAdd').val("");
		$('#urlCodeAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}		
	var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
	var objExp= srcurl.match(Expression);
	if(objExp==null){
		alert("长地址格式不正确，请参照标准的URL格式，如：http://www.montnets.com/");
		$('#urlCodeAdd').val("");
		$('#urlCodeAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}
	//地址中的&替换成|
	$('#urlCodeAdd').val(srcurl.replace(/\&/g,"|"));
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $('#lgcorpcode').val();
	$.post("keep_record.htm",
		{
			method:"add",
			urlname:urlname,
			urlmsg:urlmsg,
			srcurl : srcurl	
		},function(result){
			if(result == "true")
			{
				alert("新建成功！");
				doCancel();
				var url = 'keep_record.htm';
				var conditionUrl = "";
				if(url.indexOf("?")>-1)
				{
					conditionUrl="&";
				}else
				{
					conditionUrl="?";
				}
				var pageIndex=$('#txtPage').val();
				var pageSize=$('#pageSize').val();
				conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&isContainsSun=1";
				location.href=url+conditionUrl;
				
			}else {
				alert("新建失败！");
				$('#addcancel').attr("disabled",false);
				$('#addsubmit').attr("disabled",false);
				return;
			}
		}
	);
	
}

//新建url地址
function addUrl1()
{
	var urlname = $.trim($('#urlNameAdd').val());
	var urlmsg = $.trim($('#urlDescriptionAdd').val());
	var srcurl = $.trim($('#urlCodeAdd').val());
	
	$('#addcancel').attr("disabled",true);
	$('#addsubmit').attr("disabled",true);
	if(urlname == ""){
		alert("长链接名称必填项不允许为空！请重新输入。");
		$('#urlNameAdd').val("");
		$('#urlNameAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}
	if(urlmsg == ""){
		alert("长链接内容描述必填项不允许为空！请重新输入。");
		$('#urlDescriptionAdd').val("");
		$('#urlDescriptionAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}
	if(srcurl == ""){
		alert("长链接必填项不允许为空！请重新输入。");
		$('#urlCodeAdd').val("");
		$('#urlCodeAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}		
	var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
	var objExp= srcurl.match(Expression);
	if(objExp==null){
		alert("长地址格式不正确，请参照标准的URL格式，如：http://www.montnets.com/");
		$('#urlCodeAdd').val("");
		$('#urlCodeAdd').select();
		$('#addcancel').attr("disabled",false);
		$('#addsubmit').attr("disabled",false);
		return;
	}
	//地址中的&替换成|
	$('#urlCodeAdd').val(srcurl.replace(/\&/g,"|"));
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $('#lgcorpcode').val();
	$.post("keep_record.htm",
		{
			method:"add",
			urlname:urlname,
			urlmsg:urlmsg,
			srcurl : srcurl	
		},function(result){
			if(result == "true")
			{
				alert("新建成功！");
				 $("#addDiv").dialog("close");
			}else {
				alert("新建失败！");
				$('#addcancel').attr("disabled",false);
				$('#addsubmit').attr("disabled",false);
				return;
			}
		}
	);
	
}


//启用禁用
function changestate(i)
{
	var ks=$.trim($("#evestatue"+i).attr("value"));
	//alert(ks);
	var lgcorpcode =$("#lgcorpcode").val();
	$.post("keep_record.htm?method=changeState",{urlid:i,urlState:ks,lgcorpcode:lgcorpcode},function(result){
		
        if (result == "true") {
        	if(ks == 0) {
        		$("#evestatue"+i+"_txt").html("已启用");
        	} else {
        		$("#evestatue"+i+"_txt").html("已禁用");
        	}
			alert("修改成功！");
		}else{
			alert("修改失败！");
		}		
	});
}

//删除
function del(path, i){
	if(confirm("您确定要删除该条链接吗？"))
	{
		var lgcorpcode=$("#lgcorpcode").val();
		var lguserid=$("#lguserid").val();
		$.post("keep_record.htm?method=delete",{ids:i,lgcorpcode:lgcorpcode,ispass:-1},function(result){
			if(result == "true")
			{
				alert("删除成功！");
				//alert(path+"/tem_smsTemplate.htm?lguserid="+lguserid);
				location.href = path+"/keep_record.htm?lguserid="+lguserid;
			}else{
				alert("删除失败！可能已被删除");
				location.href = path+"/keep_record.htm?lguserid="+lguserid;
			}
		});
	}
}
//删除按钮
function delAll(path){
	var items = "";
	$('input[name="Id"]:checked').each(function(){	
		items += $(this).val()+",";
	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
	}
	if(items==null || items.length == 0){
		alert("请选择您要删除的链接！");
		return;
	}else{
	if(confirm("您确定要删除选择的链接?")==true){
		var lgcorpcode = $("#lgcorpcode").val();
				$.post("keep_record.htm?method=delete",{ids:items,lgcorpcode:lgcorpcode,ispass:-1},function(result){
					if(result == "true")
					{
						alert("删除成功！");
						
						location.href = path+"/keep_record.htm?lguserid="+lguserid;
					}else{
						alert("删除失败！可能已被删除");
						location.href = path+"/keep_record.htm?lguserid="+lguserid;
					}
				});
				
	}
	}
}
//全选
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}
function doCancel()
{
     $("#urlNameAdd").val("");
     $("#urlDescriptionAdd").val("");
     $("#urlCodeAdd").val("");
     
     $("#addDiv").dialog("close");
}



function revtime(){
	var max = "2099-12-31 23:59:59";
    var v = $("#startTime").attr("value");
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max,enableInputMask:false});
};
	
function sedtime(){
	var max = "2099-12-31 23:59:59";
    var v = $("#recvtime").attr("value");
    var min = "1900-01-01 00:00:00";
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:v,enableInputMask:false});
	
};

//显示审批不通过意见
function modify(t)
{
	var txt = $("#remark"+t).val();
	$("#msg").children("xmp").empty();
	$("#msg").children("xmp").text(txt);
	$("#singledetail").dialog("option","title", "审批意见");
	$("#singledetail").dialog("open");
}
//显示禁用原因
function showstop(t)
{
//	alert(t);
	var txt = $("#remark1"+t).val();
//	alert(txt);
	$("#msg1").children("xmp").empty();
	$("#msg1").children("xmp").text(txt);
	$("#stopdetail").dialog("option","title", "禁用意见");
	$("#stopdetail").dialog("open");
}
function showUrl(u)
{
	var ul = $("#toUrl"+u).val();
//	alert(ul);
	//$("#kkuu").attr("src","https://"+ul);
	//$("#showUrl").dialog("open");
	showbox({src:ul , mode:1});

}

function modify2(t)
{
	$('#modify').dialog({
	autoOpen: false,
	width:250,
    height:200
});
$("#msg2").empty();
$("#msg2").text($(t).children("label").children("xmp").text());
$('#modify').dialog('open');
}
