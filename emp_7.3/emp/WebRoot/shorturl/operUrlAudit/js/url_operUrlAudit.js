function revtime(){
	var max = "2099-12-31 23:59:59";
    var v = $("#sendtime").attr("value");
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max,enableInputMask:false});
};
	
function sedtime(){
	var max = "2099-12-31 23:59:59";
    var v = $("#recvtime").attr("value");
    var min = "1900-01-01 00:00:00";
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:v,enableInputMask:false});
	
};
function doCancelCheck(){
	$("#checkurlname").val("");
	$("#checksrcurl").val("");
	$("#checksrcu").text("");
	$("#checkurlmsg").val("");
	$("#checkcorpname").val("");
	$("#checkcorpcode").val("");
	$("#checkcrusr").val("");
	$("#checkurlcreatime").val("");
	$("#urlDescriptionAdd").val("");
	$("#checkDiv").dialog("close");
}

//查看
function check(id){
	//公司名称
	var corpname = $("#cppname"+id).val();
	//企业编号
	var corpnum = $("#cppnum"+id).val();
	//链接地址名称
	var urlname = $("#cppurlnam"+id).val();
	//链接地址
	var srcurl = $("#toUrl"+id).val();
	//连接地址描述
	var urlmsg = $("#cppurlmsg"+id).val();
	//创建人
	var createuser  = $("#cppurlcu"+id).val();
	//创建时间
	var ctime = $("#cppurltm"+id).val();
	//审批结果
	var urlipass = $("#urlispass"+id).val();
	//审批意见
	var remarks = $("#remark"+id).val();
	
	$("#checkurlname").val(urlname);
	$("#checksrcurl").val(srcurl);
	if(srcurl.length>50){
		$("#checksrcu").text(srcurl.substring(0,50)+"...");
	}else{
		$("#checksrcu").text(srcurl);
	}
	//$("#checksrcu").text(srcurl);
	
	$("#checkurlmsg").val(urlmsg);
	$("#checkcorpname").val(corpname);
	$("#checkcorpcode").val(corpnum);
	$("#checkcrusr").val(createuser);
	$("#checkurlcreatime").val(ctime);
	$("#urlDescriptionAdd").val(remarks);
	if(urlipass==2){
		$("#checjtg1").attr("checked", "checked");
	}else{
		$("#checjtg2").attr("checked", "checked");
		//禁用意见
		var remarks1 = $("#remark1"+id).val();
		$("#urlDescriptionAdd").val(remarks1);
	}
	//jieguo 
	
	$("#checkDiv").dialog("open");
	
}

//审核
//url id 
var verifyid="";
function verify(id){
	//公司名称
	verifyid = id;
	var corpname2 = $("#cppname"+id).val();
	$("#verifycorpname").val(corpname2);
	
	//企业编号
	var corpnum2 = $("#cppnum"+id).val();
	$("#verifycorpcode").val(corpnum2);
	
	//链接地址名称
	var urlname2 = $("#cppurlnam"+id).val();
	$("#verifyurlname").val(urlname2);
	
	//链接地址
	var srcurl2 = $("#toUrl"+id).val();
	$("#verifysrcurl").val(srcurl2);
	if(srcurl2.length>50){
		$("#verysrcu").text(srcurl2.substring(0,50)+"...");
	}else{
		$("#verysrcu").text(srcurl2);
	}

	
	//连接地址描述
	var urlmsg2 = $("#cppurlmsg"+id).val();
	$("#verifyurlmsg").val(urlmsg2);
	
	//创建人
	var createuser2  = $("#cppurlcu"+id).val();
	$("#verifycrusr").val(createuser2);
	
	//创建时间
	var ctime2 = $("#cppurltm"+id).val();
	$("#verifyurlcreatime").val(ctime2);
	$("#verifyDiv").dialog("open");
}
//审核取消
function doCancelVerify() {
	$("#verifycorpname").val("");
	$("#verifycorpcode").val("");
	$("#verifyurlname").val("");
	$("#verifysrcurl").val("");
	$("#verysrcu").text("");
	$("#verifyurlmsg").val("");
	$("#verifycrusr").val("");
	$("#verifyurlcreatime").val("");
	$("#verifyDescription").val("");
//	$("#verifytg1").attr("checked", "");
//	$("#verifytg2").attr("checked", "checked");
	verifyid="";
	$("#verifyDiv").dialog("close");
}

//新增审核时 意见描述字符数限制
$(function(){
    $("#verifyDescription").keyup(function(){
        var len = $(this).val().length;
        if(len > 99){
            $(this).val($(this).val().substring(0,100));
        }
        // var num = 140 - len;
        // $("#word").text(num);
    });
});


//审核提交
function verifyUrl(){
	var urlremark = $.trim($("#verifyDescription").val());
	var verifyispass =$("input[name='verifytg']:checked").val();
	
	if(urlremark == "" && verifyispass == 3){
		alert("审批不通过，审批意见不能为空！");
		$("#verifyDescription").val("");
		$("#verifyDescription").select();
		$('#verifycancel').attr("disabled",false);
		$('#verifysubmit').attr("disabled",false);
		return;
	}

	if(urlremark.length > 100) {
	    alert("审批意见长度不能超过100字");
	    return;
    }

//	alert(verifyispass);
	
	if(confirm("您确定要提交审核？")==true){
				$.post("surl_audit.htm?method=verifyUrl",{id:verifyid,remarks:urlremark,ispass:verifyispass},function(result){
					if(result == "true")
					{
						alert("审核成功！");
						doCancelVerify();
						var url = 'surl_audit.htm';
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
					}else{
						alert("审核失败！");
						$('#verifycancel').attr("disabled",false);
						$('#verifysubmit').attr("disabled",false);
						return;
					}
				});
				
	}
}


function showUrl(u)
{
	var ul = $("#toUrl"+u).val();
	showbox({src:ul , mode:1});

}
function showCUrl(){
	var ul2 = $("#verifysrcurl").val();
	//alert(ul2);
	showbox({src:ul2 , mode:1});
}
function showresurl(){
	var ul3 = $("#checksrcurl").val();
	//alert(ul2);
	showbox({src:ul3 , mode:1});
}
function showStopurl(){
	var ul4 = $("#stopsrcurl").val();
	//alert(ul2);
	showbox({src:ul4 , mode:1});
}

//禁用
var stopid="";
function toStop(id){
	stopid = id;
//	alert(id);
	//链接地址名称
	var surlname = $("#cppurlnam"+id).val();
	$("#stopurlname").val(surlname);
	
	//链接地址
	var ssrcurl = $("#toUrl"+id).val();
	$("#stopsrcurl").val(ssrcurl);
	
	if(ssrcurl.length>50){
		$("#stopsrcu").text(ssrcurl.substring(0,50)+"...");
	}else{
		$("#stopsrcu").text(ssrcurl);
	}
	
	//连接地址描述
	var surlmsg = $("#cppurlmsg"+id).val();
	$("#stopurlmsg").val(surlmsg);
	
	$("#stopDiv").dialog("open");
}
function doCancelStop(){
	$("#stopurlname").val("");
	$("#stopsrcurl").val("");
	$("#stopsrcu").text("");
	$("#stopurlmsg").val("");
	$("#urlDescriptionStop").val("");
	stopid="";
	
	$("#stopDiv").dialog("close");
}
function modify(t)
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
//禁用链接
function stopUrl(){
	var urlremark1 = $.trim($("#urlDescriptionStop").val());
	var stopispass =$("input[name='stoptg1']:checked").val();
	//alert(stopispass);
	if(stopispass!=-3){
		alert("请勾选状态！");
		$('#stopcancel').attr("disabled",false);
		$('#stopysubmit').attr("disabled",false);
		return;
	}
	
	if(urlremark1 == ""){
		alert("禁用意见不能为空！");
		$("#urlDescriptionStop").val("");
		$("#urlDescriptionStop").select();
		$('#stopcancel').attr("disabled",false);
		$('#stopysubmit').attr("disabled",false);
		return;
	}
	
	if(confirm("禁用以后，企业将无法再使用本链接，同时先前接收的手机用户将浏览不到链接内容，您确定要禁用？")==true){
				$.post("surl_audit.htm?method=stopUrl",{id:stopid,remarks1:urlremark1,ispass:stopispass},function(result){
					if(result == "true")
					{
						alert("禁用成功！");
						doCancelStop();
						var url = 'surl_audit.htm';
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
					}else{
						alert("禁用失败！");
						$('#stopcancel').attr("disabled",false);
						$('#stopysubmit').attr("disabled",false);
						return;
					}
				});
				
	}
	
	
	
}






