var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var contentLen = 900;
var baseContentLen = 900;
// 获取1到n-1条英文短信内容的长度
var entotalLen = 153;
var empLangName = $("#empLangName").val();
function hideSelect()
{
	$("#busCode").css("display","none");
	$("#spUser").css("display","none");
	$("#sendType").css("display","none");
	$("#tempSelect").css("display","none");
	$("#priority").css("display","none");
}

function showSelect()
{
	$("#priority").css("display","");
	$("#busCode").css("display","inline");
	$("#spUser").css("display","inline");
	$("#sendType").css("display","inline");
	$("#tempSelect").css("display","inline");
}

//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
// 统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	var smsTailContens="";
    if($('#wx_righttitle').is(':visible')){
    	smsTailContens = $.trim($('#smsTailContens').text())||'';
    }
	var totalLen=parseInt(content.length)+parseInt(smsTailContens.length);
	var fontlen=contentLen-parseInt(smsTailContens.length);
	if(ob.is("#contents")){
		if(totalLen>contentLen)
		{
			$("#contents").val(content.substring(0,fontlen));
		}
	}
	var len = contentLen;
	if(baseContentLen == 620)
	{
		setSmsContentMaxLen(content+smsTailContens);
		content = $("#contents").val();
	}
	if(len != contentLen)
	{
		if(totalLen.length>contentLen)
		{
			$("#contents").val(content.substring(0,fontlen));
			content = $("#contents").val();
		}
		$('#maxLen').html("/"+contentLen);
	}
//	var huiche = content.length - content.replaceAll("\n", "").length;
	$('form[name="' + formName + '"]').find(' #strlen').html(($("#contents").val().length +smsTailContens.length));
};

// 失去焦点时判断
function eblur(ob) {
	var $parent = ob.parent();
	$parent.find(".add_error").remove();
	if (ob.is("#contents")) {
		//ob.val($.trim(ob.val()));
//		var content = $("#contents").val();
//		var huiche = content.length - content.replaceAll("\n", "").length;
//		if (content.length + huiche > contentLen) {
//			$("#contents").val(content.substring(0, contentLen - huiche));
//		}
		if($("#tempSelect").val()=="")
		{
			$("#inputContent").val($("#contents").val());
		}
		len(ob);
		var str=$.trim(ob.val());
        var reg;
        if("zh_HK" === empLangName){
            reg=/{#Param(\d*?)#}/g;
        }else if("zh_TW" === empLangName){
            reg=/{#參數(\d*?)#}/g;
        }else if("zh_CN" === empLangName){
            reg=/{#参数(\d*?)#}/g;
        }
		var msg=str.replace(reg,replacer);
		if ($.trim(ob.val()) == "") {
			$("#errormessage").html(getJsLocaleMessage("ydwx","ydwx_dtwxfs_2"));
			document.forms[formName].isOk.value = 1;
		}
		else if(!/#[Pp]_[1-9][0-9]*#+/g.test(msg)){
			    $("#errormessage").html(getJsLocaleMessage("ydwx","ydwx_dtwxfs_3"));
				document.forms[formName].isOk.value = 1;
		}else if (ob.val().length > contentLen) {
			$("#errormessage").html(getJsLocaleMessage("ydwx","ydwx_dtwxfs_4")+contentLen+getJsLocaleMessage("ydwx","ydwx_dtwxfs_5"));
			document.forms[formName].isOk.value = 1;
		} else {
			$("#errormessage").empty();
			document.forms[formName].isOk.value = 2;
		}
	}
}

// 短信内容键盘事件监听统计字数
function synlen() {
	$("form").find("textarea[name='msg']").keyup(function() {
//		var content = $(this).val();
//		content = $.trim(content);
//		var huiche = content.length - content.replaceAll("\n", "").length;
//		if (content.length > contentLen) {
//			$(this).val(content.substring(0, contentLen ));
//		}
		len($(this));
	});
}

// 设置当前显示的表单名称
var formName = "";
function setFormName(name) {
	formName = name;
	$('#formName').val(formName);
}

$(document).ready(function() {
	synlen();
	var lguserid = $("#lguserid").val();
	var curMsg = "";
	var lgcorpcode = $("#lgcorpcode").val();
	setFormName($("#formName").val());
	dopreview();
	$("input:radio[name='checkrepeat'][value="+checkrepeat+"]").attr("checked",'checked');
});
//点击预览时的方法
function dopreview()
{
	// 点击创建或存草稿按钮时
	$("input[id='subSend'],input[id='creSend']").click(function() {
		//标示判断是否有异常（具体针对内存溢出的异常）
		$("#error").val("0")
		$("form[name='" + formName + "']").find(
				"input[name='subState']").val(2);
		var formObj = document.forms[formName];
		if (formObj.spUser.value == "") {
			/*没有可供发送的SP账号！*/
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_6"));
			return;
		}
		var netid = $("#netId").val();
		var $texta = $("form[name='" + formName + "']").find("textarea[name='msg']");
		var sendType = $("form[name='" + formName + "']").find("#sendType").val();
		if (sendType != 3) {
			$texta.trigger("blur");
			var msgtxt=$texta.val();
		 	var reg;
			if("zh_HK" === empLangName){
				reg=/{#Param(\d*?)#}/g;
			}else if("zh_TW" === empLangName){
                reg=/{#參數(\d*?)#}/g;
			}else if("zh_CN" === empLangName){
                reg=/{#参数(\d*?)#}/g;
            }
			msgtxt=msgtxt.replace(reg,replacer);
			$("#dtMsg").val(msgtxt);
		}
		if (formObj.isOk.value == 1 && sendType != 3) {
			if($.trim($("#contents").val())=="")
			{
				/*请输入短信内容或者选择短信模板！*/
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_8"));
				return;
			}else{
				/*请录入动态模板参数格式！  如:{#参数1#}*/
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_9"));
				return;
			}
		}
		if(netid==null||netid=="" || netid =="undefined")
		{
			/*请选择网讯模板！*/
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_10"));
			return;
		}
		if (checkFile1()) {
			checkWords();
		}
	});
}

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

// 验证上传文件格式
function checkFile1() {
	  if($("#upfilediv").html() == "" || trim($("#upfilediv").html()).length <=0)
	  {
		    alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_11"));
			return false;
	  }	
	  else
	  {
		  return true;
	  }
}

// 验证短信内容是否包含关键字
function checkWords() {
	$("#subSend").attr("disabled",true);
	$("#qingkong").attr("disabled",true);
	var sendType = $("#sendType").val();
	if(sendType == 2)
	{
		var msg = $("form[name='" + formName + "']").find("textarea[name='msg']").val();
	    	msg += $('#smsTailContens').text();
		$.ajax({
			url: "wx_senddsm.htm", 
			type : "POST",
			data: {
				method : "checkBadWord1",
				tmMsg : msg,
				corpCode : $("#lgcorpcode").val(),
				isAsync : "yes"
			},
			success: function(message) {
				if(message == "outOfLogin")
    	    	{
    	    		alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_12"));
    	    		return;
    	    	}
				if(message.indexOf("@")==-1){
					alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_13"));
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
					return;
				}
				message=message.substr(message.indexOf("@")+1);
				if (message != ""&&message!="error") {
					alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_14") + message + getJsLocaleMessage("ydwx","ydwx_dtwxfs_15"));
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
				} else if (message == "error") {
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
					alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_16"));
				} else {
					checkTimer();
				}
			},
			error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_17"));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				window.location.href='wx_senddsm.htm';
			}
		});
	}else
	{
		checkTimer();
	}
}

// 验证是否定时
function checkTimer() {
	var $timer = $("form[name='" + formName + "']").find(
			"input:checked[name='timerStatus']");
	var $timerTime = $("form[name='" + formName + "']").find(
			"input[name='timerTime']");
	if ($timer.val() == 1 && $timerTime.val() == "") {
		alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_18"));
		$("#subSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
	} else {
		var sendTime = $("#timerTime").val();
		var serverTime ;
		$.post("common.htm", {
			method : "getServerTime"
		}, function(msg) {
			serverTime = msg;
			var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
			var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
            date1.setMinutes(date1.getMinutes() - 1, 0, 0);
			if (date1 <= date2) {
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_19"));
				$("#timerTime").val("");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
			$("#probar").dialog({
				modal:true,
				title:getJsLocaleMessage("ydwx","ydwx_dtwxfs_20"), 
				height:70,
				resizable :false,
				closeOnEscape: false,
				width:200,
				open: function(){
				$("#probar").css("height","50px");
				$(".ui-dialog-titlebar").hide();
				errorNum=0;
				dd = window.setInterval("fresh()",3000);
			}
			});
			$("#busCodeHidden").val($("#busCode").val());
			$("#spuserHidden").val($("#spUser").val());
			$("#sendtypeHidden").val($("#sendType").val());
			var checkrepeat = $("input[name=checkrepeat]:checked").val();
			var lguserid =$("#lguserid").val();
			var taskid = $("#taskId").val();
			 var netid = $("#netid").val();
			 $("form[name='form2']").attr("action","wx_senddsm.htm?method=preview&checkrepeat="+checkrepeat+"&lguserid="+lguserid+"&netid="+netid+"&taskid="+taskid);
			$("form[name='" + formName + "']").submit();
		});
	}
}

function fresh()
{
	$.ajax({
		url:"LoadingServlet.htm",
		type:"post",
		dataType:"script",
		success:function(result){
			if( result !="true")
			{
				if(errorNum==0)
				{
					errorNum==1;
					alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_17"));
					window.clearInterval(dd);
					reloadPage();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			
			if(errorNum==0)
			{
				errorNum=1;
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_17"));
			}
			window.clearInterval(dd);
			reloadPage();
		}
	});
}

// 获取表格的值


// 获取短信模板内容
function getTempMsg(tmpOb) {
	var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
	if (tmpOb.val() != "") {
		$("#errormessage").empty();
		var reTitle="wx_senddsm.htm";
		$.ajax({
			url:"wx_senddsm.htm",
			data:{method : "getTmMsg1",tmId : tmpOb.val()},
			type:"post",
			success:function(result){
				if(result.indexOf("@")==-1){
					window.location.href=reTitle;
					return;
				}
				result=result.substr(result.indexOf("@")+1);
				if(result=="error")
				{
					alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_21"));
					return;
				}
				msgob.val(result);
				msgob.attr("readonly","readonly");
				msgob.css("background","#F1F1EB");
				msgob.bind("focus",function(){
					this.blur();
				});
　　　　　　　　　len(msgob);　　　　　　　　
					
			},
			error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_17"));
				window.location.href=reTitle;
			}
		});
	} else {
		msgob.val($("#inputContent").val());
		msgob.attr("readonly","");
		msgob.focus();
		msgob.css("background","#FFFFFF");
		msgob.unbind("focus");
		len(msgob);
	}
}

// 文件发送时改变发送类型调用的方法
function changeSendType(va) {
	var sendtype=$("#sendType").val();
	if(sendtype=="2")
	{
		$("#temDiv").css("display", "");
	}
	else if(sendtype=="3")
	{
		$("#temDiv").css("display", "none");
		$("#smstext").find("textarea[name='msg']").css("background","#FFFFFF");
		$("#smstext").find("textarea[name='msg']").attr("readonly","");
		$("#tempSelect").val("");
	}
	var $texta = $("form[name='" + formName + "']")
			.find("textarea[name='msg']");
	var $textp = $texta.parent();
	var $textspan = $texta.parent().prev("span");
	var isTmShow = $("form[name='" + formName + "']").find("#model").is(":visible");
	$("#downlink").attr("href", "javascript:openTemp('smsTem" + va + ".txt')");
	if (va == 3) {
		$texta.val("");
		$textp.hide();
		$textspan.hide();
		$("#temDiv").css("display", "none");
		$("#preShow").css("display", "none");
		$("form[name='" + formName + "']").find("#model").css("display","none");
	} else {
		$("#temDiv").css("display", "");
		$("#preShow").css("display", "");
		if ($textp.is(":hidden")) {
			$textp.show();
			$textspan.show();
		}
		$texta.val("");
		len($texta);
		if (isTmShow) {
			var tmpSel = $("form[name='" + formName + "']").find("#tempSelect");
			tmpSel.empty();
			var lguserid = $("#lguserid").val();
			tmpSel.load("t_smsTemplate.htm", {
				method : "getTmAsOption",
				lguserid:lguserid,
				dsflag : va - 1
			}, function() {
				});
		}
	}

}

function openTemp(r) {
	var path = $("#cpath").val();
	window.open(path + "/file/fileDownload/" + r, "_blank");
}

// 查看号码文件是否存在
function checkFileIsVisible(url, src) {
	$.post(src + "/common.htm?method=checkFile", {
		url : url
	}, function(result) {
		if (result == "true") {
			window.showModalDialog(src + "/" + url);
		} else if (result == "false")
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_22"));
		else
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_23"));
	});
}


function getBindUserId() {
	$("form[name='" + formName + "']").find("#spUser").load(
			"sms_sendSMS.htm?method=getBindUserId",
			{
				busCode : $("form[name='" + formName + "']").find("#busCode")
						.val()
			});
}
// **********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var app = navigator.appName;
var ieWidth = 350;
var ieHeight = 440;
if (isIE6) {
	ieWidth = 360;
	ieHeight = 470;
}
if (app == "Netscape") {
	ieWidth = 355;
	ieHeight = 447;
}
// ***************

   //重置按纽
	function resets(){
		var ie = (navigator.appVersion.indexOf("MSIE")!=-1);//IE         
		if(ie){   
		            $("#numFile").select();   
		             document.execCommand("delete");   
		       }else{   
		              $("#numFile").val("");    
		      }  

			$("#taskname").attr("value","");
			$("#contents").attr("value","");
			$("#sendNow").attr("checked","checked");
		 //清队查模板
		   var type=$("#sendType").val();
		   $('form[name="' + formName + '"]').find(' #strlen').html((0));
		   if(type=="3"){
		
		    $("#temDiv").css("display", "block");
			$("#smscon").css("display", "block");
			$("#smstext").css("display", "block");
	
		   }
		   $("#model").css("display","none");
		$('#contents').attr('readonly',false);
		   $("#time2").css("display","none");
		}
		
	//预览窗口
	function preSend(data)
	{
		window.clearInterval(dd);
		$("#error").val("1");
		var SendReq=$("#SendReq").val();
		var cpath = $("#cpath").val();
		$("#probar").dialog("close");
		$(".ui-dialog-titlebar").show();

        if(data.indexOf("empex")===0){
            alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_35")+data.substr(5));
            $("#subSend").attr("disabled",true);
            $("#qingkong").attr("disabled",false);
            return;
        }

		if(data=="error")
		{
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_24"));
			$("#subSend").attr("disabled",true);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data=="overstep")
		{
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_25")+MAX_PHONE_NUM+getJsLocaleMessage("ydwx","ydwx_dtwxfs_26"));
			$("#subSend").attr("disabled",true);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data=="overSize")
		{
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_27")+MAX_SIZE+getJsLocaleMessage("ydwx","ydwx_dtwxfs_28")+ZIP_SIZE+getJsLocaleMessage("ydwx","ydwx_dtwxfs_29"));
			$("#subSend").attr("disabled",true);
			$("#qingkong").attr("disabled",false);
			return;
		}
		
		//$("#newtable").empty();
		$("#content").empty();
		$("#preStr").val(data);
		var msgContent=$("form[name='" + formName + "']").find("textarea[name='msg']").val();
		if(data=="noPhone")
		{
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_30"));
			$("#subSend").attr("disabled",true);
            $("#qingkong").attr("disabled",false);
		}
		else
		{
			var array=data.split('&');
			$("#counts").text(array[1]);
		    $("#effs").text(array[2]);
		    $("#ct").text(array[3]);
		    $("#yct").text(array[4]);
		    $("#blacks").text(array[7]);
		    $("#legers").text(array[5]);
		    $("#sames").text(array[6]);
		    $("#keyW").text(array[9]);
		    var wuxiaonum = parseInt(array[7])+parseInt(array[6])+parseInt(array[5])+parseInt(array[9]);
		    $("#alleff").text(wuxiaonum);
		    $("#badurl").attr("value",array[0]);
		    $("#isCharg").attr("value",array[10]);
		 	var spye = array[11];
	    	checkspye(spye);
	    	
	    	// ----增加SP账号检查---
	    	//为了先显示短信等，显示SP账号，显示顺序
	    	var isshow=true;
	    	 //如果余额<发送条数
			if(eval(array[3])<eval(array[4]))
			{
				isshow=false;
		    }
	    	var feeFlag=array[13];
	    	//后付费情况
			 if(feeFlag=="2"){
	    		$("#shospfee").text("");
	    	}
			 //预付费处理
	    	else if(feeFlag=="1")
	    	{
				$("#shospfee").text("");
				$("#shospfee").html(getJsLocaleMessage("ydwx","ydwx_dtwxfs_31")+"<span id='spfee'></span>");
				var spAccount=array[12];
				var resultCount=eval(spAccount-array[4]);
			 	checkSpAccount(isshow,resultCount,spAccount);
			 }else if(feeFlag=="-1"&&isshow){
				 $("#shospfee").text("");
				 $("#messages2 font").text(getJsLocaleMessage("ydwx","ydwx_dtwxfs_32"));
				 $("#btsend").attr("disabled","disabled");
			 }else if(feeFlag=="-2"&&isshow){
				 $("#shospfee").text("");
				 $("#messages2 font").text(getJsLocaleMessage("ydwx","ydwx_dtwxfs_33"));
				 $("#btsend").attr("disabled","disabled");
			 }else if(feeFlag=="-3"&&isshow){
				 $("#shospfee").text("");
				 $("#messages2 font").text(getJsLocaleMessage("ydwx","ydwx_dtwxfs_34"));
				 $("#btsend").attr("disabled","disabled");
			 }
			 
			  //----增加SP账号检查 end---
		    if(wuxiaonum == 0){
		    	 $("#predowninfo").attr("style","display:none");
		    }else{
		    	 $("#predowninfo").attr("style","display:block");
		    }
		    
		    if(array[2]-0==0)
		    {
		    	//$("#newtable").append("<tr align='center'><td style='border: 1px  solid; height: 25px;width:30px'>编号</td>" +
				//		"<td style='border: 1px  solid; height: 25px;'><center><div style='width:60px'>手机号码</div></center></td>" +
				//		"<td style='border: 1px  solid; height: 25px;'>短信内容</td></tr>" );
		    	$("#content").append("<thead><tr align='center'><th>"+getJsLocaleMessage("ydwx","ydwx_dtwxfs_35")+"</th>" +
						"<th><center><div style='width:89px'>"+getJsLocaleMessage("ydwx","ydwx_dtwxfs_36")+"</div></center></th>" +
						"<th>"+getJsLocaleMessage("ydwx","ydwx_dtwxfs_37")+"</th></tr></thead>" );
		    	$("#content").append("<tbody><tr><td colspan='3' align='center'>"+getJsLocaleMessage("ydwx","ydwx_dtwxfs_38")+"</td></tr></tbody>");
		    	$("#detail_Info").css("display","block");
	    		$("#detail_Info").dialog("open");
	    		deleteleftline1();	
		    }else
		    {   	
		    	
		    	//防止预览发出多次异步请求
		    	if(SendReq=="0"){
				    $("#SendReq").val("1");
				    var sendType=$("#sendType").val();
				    var netid = $("#netId").val();
				    var taskId= $("#taskId").val();
				    $("#content").load(
				    	"wx_senddsm.htm?method=readSmsContent",
				    	{url:array[0],sendType:sendType,netid:netid,taskId:taskId},
				    	function(){
				    		$("#detail_Info").css("display","block");
				    		$("#detail_Info").dialog("open");
				    		deleteleftline1();	
				    });
		    	}
		    }
			
		}
	}
	
	// 提示SP账号余额相关信息
function checkSpAccount(isshow,resultCount,spAccount)
{
	var spUser=$("#spUser").val();
	var altstr = "";
	if(resultCount <0&&isshow)
	{
		altstr = spUser+getJsLocaleMessage("ydwx","ydwx_dtwxfs_39");
		$("#messages2 font").text(altstr);
		$("#btsend").attr("disabled","disabled");	 
	}

	$("#spfee").text(spAccount);

}
	
	function checkspye(yeresult)
{
	var altstr = "";
	if(yeresult.indexOf("lessgwfee") == 0)
	{
		altstr = getJsLocaleMessage("ydwx","ydwx_dtwxfs_40")+yeresult.split("=")[1];
		 $("#btsend").attr("disabled","disabled");
	}else if(yeresult=="feefail" || yeresult=="nogwfee")
	{
		altstr = getJsLocaleMessage("ydwx","ydwx_dtwxfs_41");
		 $("#btsend").attr("disabled","disabled");
	}
	$("#messages2 font").text(altstr);
}
	
	function checkError()
	{
		if($("#error").val()=="0")
		{
			window.clearInterval(dd);
			$("#probar").dialog("close");
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_42"));
			$("#subSend").attr("disabled",false);
            $("#qingkong").attr("disabled",false);
		}
	}
	
	function reloadPage(){
        var url = "wx_senddsm.htm";
		var conditionUrl = "";
		if(url.indexOf("?")>-1)
		{
			conditionUrl="&";
		}else
		{
			conditionUrl="?";
		}
		$("#loginparam").find(" input").each(function(){
			conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
		});
	 	window.location.href = url+conditionUrl;
	}	
	//上传附件
	var flag=true;
	var isFile=true;
	var fileCount=1;

	// 验证上传文件格式
	function checkFile_old(name) {
		var fileObj = $("form[name='" + formName + "']").find("input[name='"+name+"']");
		if(fileObj.val() != "") {
			var fileNmae = fileObj.val();
			var index = fileNmae.lastIndexOf(".");
			var fileType = fileNmae.substring(index + 1).toLowerCase();
			if (fileType != "txt" && fileType != "zip") {
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_43"));
				var file = $(":file");   
        		file.after(file.clone().val(""));   
        		file.remove(); 
        		return false;
			} else {
				return true;
			}
		}
		else {
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_44"));
			return false;
		}
	}
	// 验证上传文件格式
	function checkFile(name) {
		var fileObj = $("form[name='" + formName + "']").find("input[name='"+name+"']");
		if(fileObj.val() != "") {
			var fileName = fileObj.val();
			var index =fileName.lastIndexOf(".");
			var fileType =fileName.substring(index + 1).toLowerCase(); 
			var trs = $("#allfilename").html();

            if(trs.indexOf(fileName)>-1)
            {
            	alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_45"));
		    	fileObj.after(fileObj.clone().val(""));   
		    	fileObj.remove(); 
        		return false;
            }
		    
			if (fileType != "txt" && fileType != "zip" && fileType != "rar" && fileType != "xls" && fileType != "xlsx" && fileType != "et") {
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_46"));
        		fileObj.after(fileObj.clone().val(""));   
        		fileObj.remove(); 
        		return false;
			} else {
				return true;
			}
		}
		else {
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_44"));
			return false;
		}
	}
	//删除附件
	function delfile(filename)
	{	
		if(confirm(getJsLocaleMessage("ydwx","ydwx_dtwxfs_47")))
		{ 
		   var trs = $("#allfilename").html();
		   trs = trs.replace($("#numFile"+filename).val()+";","");
		   $("#allfilename").html(trs);
		   $("#tr"+filename).remove();
		   $("#numFile"+filename).remove();
		   if($("#upfilediv").html() == "")
		   {
		      $("#upfilediv").hide();
		   }
		}
	}
	
	function showMenu() {
		var sortSel = $("#groupResouce");
		var sortOffset = $("#groupResouce").offset();
		$("#dropMenu").toggle();
	}
	function hideMenu() {
		$("#dropMenu").hide();
	}
	//检查手机号码数量
	function checkMax(permitMax){
		var num=parseInt(permitMax);
		if(num>1000000){
			parent.$("#counts").text();
			parent.$("#effs").text();
			parent.$("#blacks").text();
			parent.$("#legers").text();
			parent.$("#sames").text();
			parent.$("#keyW").text();						  
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_48")+MAX_PHONE_NUM+getJsLocaleMessage("ydwx","ydwx_dtwxfs_49")+MAX_PHONE_NUM+getJsLocaleMessage("ydwx","ydwx_dtwxfs_50"));
			parent.$("#subSend").attr("disabled",false);
	        parent.$("#qingkong").attr("disabled",false);
			parent.$("#detail_Info").dialog("close");	
			return false;
			}
		return true;
		}
	//检查模板内参数
	function checkTemplate(Template){
		
		if(Template=="false"){
			parent.$("#subSend").attr("disabled",false);
	        parent.$("#qingkong").attr("disabled",false);
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_51"));
			parent.$("#detail_Info").dialog("close");	
			return false;
			}
		return true;
		}
	//检查参数格式
	function checkMatch(match){
		if(match=="false")
		{
			parent.$("#subSend").attr("disabled",false);
	        parent.$("#qingkong").attr("disabled",false);
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_52"));
			parent.$("#detail_Info").dialog("close");
			return false;
		}
		return true;
	}
	//日期控制
	function numberControl(va)
	{
		var pat=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
		if(pat.test(va.val()))
		{
			va.val(va.val().replace(/[^\d]/g,''));
		}
	}
	
	//选择短信模板
	function tempSure()
	{
		var $fo = $("#contentFrame").contents();
		var $ro = $fo.find("input[type='radio']:checked");
		if($ro.val() == undefined)
		{
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_53"));
			return;
		}else
		{
			var message = $ro.next("xmp").text();
			var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
			msgob.attr("readonly","readonly");
			msgob.val(message.replaceAll("#[pP]_(\\d+)#",getJsLocaleMessage("ydwx","ydwx_dtwxfs_92")));
			msgob.css("background-color","#E8E8E8");
		}
		$("#ctem").hide();
		$("#qtem").show();
		len(msgob);
		$("#contentDiv").dialog("close");
		$("#depSign").attr("checked",false);
		$("#nameSign").attr("checked",false);
		$('.showParams').hide();
		$('.para_cg').hide();
	}
	
	//模板不显示
	function tempNoShow()
	{
		var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
		initContents();
		len(msgob);
		$("#depSign").attr("checked",false);
		$("#nameSign").attr("checked",false);
		$("#qtem").hide();
		$("#ctem,.para_cg,.showParams").show();
		msgob.css("background-color","#ffffff");
		
	}
	
	//选择短信内容模板
	function chooseTemp()
	{
		var tpath = $("#cpath").val();
		var frameSrc = $("#tempFrame").attr("src");
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		frameSrc = tpath+"/wx_send.htm?method=getLfTemplateBySms&dsflag=1&lguserid="
			+lguserid+"&lgcorpcode="+lgcorpcode+"&timee="+new Date().getTime();
		$("#contentFrame").attr("src",frameSrc);
		//4.0皮肤重新设置dialog 弹框高宽
		resizeDialog($("#contentDiv"),"ydwxDialogJson","wx_senddsm_contentDiv");
		$("#contentDiv").dialog("open");
	}
	
		//取消层显示
		function tempCancel()
		{
			$("#contentDiv").dialog("close");
		}
		
		//预览网讯内容
		function showContent(netId,taskId,phone,pathurl)
		{
		    //$("#netid").val(netId);
		    $.post('wx_senddsm.htm?method=showNetById',{netId:netId,taskId:taskId,phone:phone,pathurl:pathurl},function(data){
		       data=eval("("+data+")");
		       listpage=data;
		       $(".ym").children().remove();
		       for(var i=0;i<listpage.length;i++){   
		           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
		      }
                // 此处为显示错误页面，避免进入登录页面
                if(listpage[0].content=="notexists"){
                    $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                }else{
                    $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
                }
		      $("#divBox").dialog("open");
	       });
		}
		
	    //显示静态模板列表（层）
	    function staticTemp() {
		var tpath = $("#pathUrl").val();
		var frameSrc = $("#tempFrame").attr("src");
			var lguserid = $("#lguserid").val();
			var lgcorpcode =$("#lgcorpcode").val();
			frameSrc = tpath+"/wx_senddsm.htm?method=getTemplate&dsFlag=1&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&state=1";
			$("#tempFrame").attr("src",frameSrc);
			//$("#tempupfilediv").empty();
			$("#tempDiv").dialog("open");
		}
	    
		//显示动态模板列表（层）
	   function dynamicTemp() {
		var tpath = $("#pathUrl").val();
		var frameSrc = $("#tempFrame").attr("src");
			var lguserid = $("#lguserid").val();
			var lgcorpcode =$("#lgcorpcode").val();
			frameSrc = tpath+"/wx_senddsm.htm?method=getTemplate&dsFlag=1&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&state=2";
			$("#tempFrame").attr("src",frameSrc);
			//$("#tempupfilediv").empty();
			$("#tempDiv").dialog("open");
		}
		//查看
		function Look(netId){
		    //$("#netid").val(netId);
		    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
		       data=eval("("+data+")");
		       listpage=data;
		       $(".ym").children().remove();
		       for(var i=0;i<listpage.length;i++)	{   
		           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
		      }
                // 此处为显示错误页面，避免进入登录页面
                if(listpage[0].content=="notexists"){
                    $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                }else{
                    $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
                }
		      $("#divBox").dialog("open");
	       });
	    }
		
		//删除附件
		function deltempfile(filename)
		{	
			if(confirm(getJsLocaleMessage("ydwx","ydwx_dtwxfs_47")))
			{ 
			   var trs = $("#alltempname").html();
			   trs = trs.replace($("#tempFile"+filename).val()+";","");
			   $("#netId").val("");
			   $("#alltempname").html(trs);
			   $("#temptr"+filename).remove();
			   $("#tempFile"+filename).remove();
			   if($("#tempupfilediv").html() == "")
			   {
			      $("#tempupfilediv").hide();
			   }
			}
		}
		function fileCheck()
		{
	  	  if($("#upfilediv").html() == "" || trim($("#upfilediv").html()).length <=0)
		  {
				return false;
		  }	
		  else
		  {
			  return true;
		  }
		}
		var subcount = 0;
		//初始化相关数据
		$(document).ready(
			function(){
				$('.para_cg').gotoParam({'width':498,'textarea':'#contents'});
				getLoginInfo("#loginparam");
				setFormName('form2');

			    if(findresult=="-1")
			    {
			       alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_54"));	
			       return;			       
			    }	
			    	
				if(message=="file"){
				     parent.$("#probar").dialog("close");
			         parent.$("body").css("background-color","white");
				     alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_55"));
				     parent.$("#subSend").attr("disabled",false);
		             parent.$("#qingkong").attr("disabled",false);
				     return;
		 		 }
		 		 
				$("#detail_Info").dialog({
					autoOpen: false,
					modal:true,
					title:getJsLocaleMessage("ydwx","ydwx_dtwxfs_56"),
					height:520,
					width: 620,
					resizable:false,
					closeOnEscape: false,
					open:function(){
						$("#detail_Info").parent().css("top","20px");
						$("#detail_Info").css("height","481px");//兼容websphere
					    var  hhh=$("#effinfo").height();
						hideSelect();
						//$(".ui-dialog-titlebar-close").hide(); 不显示右上角的关闭按钮												 		 
				 		 var ct = $.trim($("#ct").text());
				 		 var yct =$.trim($("#yct").text());
				 		 var isCharg = $("#isCharg").val();
				 		 if(isCharg == "true")
				 		 {
					 		 if(eval(ct)<eval(yct))
					 		 {
					 		     $("#messages1").show(); 
					 		     $("#btsend").attr("disabled","disabled");
					 		 }				 		 
					 		 else
					 		 {
					 		    $("#messages1").hide(); 
					 		 }
					 		 $("#showyct").show();
				 		 }
				 		 else{
				 		     $("#showyct").hide();
				 		     $("#messages1").hide(); 
				 		 }
				 		 //如果预发送条数为0，则也不允许发送，因为有可能此号码未绑定通道。				 		 
				 		 if(eval(yct) == 0)
				 		 {
				 		    $("#btsend").attr("disabled","disabled");
				 		 }
					},
					 close:function(){
						showSelect();
						$("#effinfo").hide();
						$("#subSend").attr("disabled","");
						$("#qingkong").attr("disabled","");
						$("#SendReq").val("0");
						$(".ui-dialog-titlebar-close").show();
						$("#btsend").attr("disabled","");
						hideSelect();
					}
				});	
				$("#showeffinfo").click(function() {
				    if($("#effinfo").is(":hidden"))
				    {
						$("#effinfo").show();
						$(this).find("> p ").removeClass("unfold");
						$(this).find("> p ").addClass("fold");
	                    if($("#messages1").is(":hidden")){
	                        $("#effinfotable").css("top","62px");
	                    }
	                    else
	                    {
	                       $("#effinfotable").css("top","90px");
	                    }
				    }else
				    {
						$("#effinfo").hide();
						$(this).find("> p ").removeClass("fold");
						$(this).find("> p ").addClass("unfold");
					}
				});
				$("#tempDiv").dialog({
					autoOpen: false,
					height:565,
					width: 620,
					resizable:false,
					modal: true,
					open:function(){
						
					},
					close:function(){
						//$("#addSmsTmpDiv").attr("src","");
					}
				});		
				$("#contentDiv").dialog({
				autoOpen: false,
				height:500,
				width: 630,
				resizable:false,
				modal: true,
				open:function(){
					
				},
				close:function(){
				}
	});
				
				$('#moreSelect').hide();
				$('#u_o_c_explain').toggle(function(){
						$("#foldIcon").removeClass("unfold");
						$("#foldIcon").addClass("fold");
						$('#moreSelect').show();
					},function(){
						$("#foldIcon").removeClass("fold");
						$("#foldIcon").addClass("unfold");
						$('#moreSelect').hide();
				});
				
				//加载上传附件鼠标移动的事件
                filedivHover();				
				//加载格式提示处的悬浮框
				floating("downlinks","infomodel");
				//$("#tempSelect").empSelect();
				//$("#moreSelect select").empSelect({
					//width:225,
					//classStyle: "input_bd"
				//});
			
				$("#divBox").dialog({
					autoOpen: false,
					height:510,
					width: 300,
					modal: true,
					close:function(){
					}
				});
				setContentMaxLen();
				$("#spUser").bind("change", function(){setContentMaxLen();});
				$(".ym").change(function(){
		            var id=$(this).val();
		            for(var i=0;i<listpage.length;i++){
		                  if(id==listpage[i].id){
		                	   if(listpage[i].content=="notexists"){
		   			           	 $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
					           }else{
			                		$("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
			                	}
		                  }
		            }
				});
				
			    //页面初始化的时候，需要加载贴尾
			    setTailInfo();
			    //选择业务类型，SP账号都要查询
			    $("#spUser,#busCode").bind("change", function(){setTailInfo();});

				$("#draftDiv").dialog({
					autoOpen: false,
					height:500,
					width: "zh_HK" == empLangName?920:900,
					resizable:false,
					modal: true,
			        open:function(){

			        },
			        close:function(){
			            //关闭草稿箱选择 页面内容移除
			            $("#draftFrame").attr("src","");
			        }

				});
				
			    $('#toDraft').click(function(){
			        //暂存草稿前判断发送内容以及发送号码是否为空
			        var msg = $.trim($('#contents').val());
			        if(!msg&&!fileCheck()){
			            alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_57"));
			            return false;
			        }
			        if($("#taskname").val()==getJsLocaleMessage("ydwx","ydwx_dtwxfs_58"))
			        {
			            $("#taskname").val("");
			        }
					var $texta = $("form[name='" + formName + "']").find("textarea[name='msg']");
					var msgtxt=$texta.val();
					$("#dtMsg").val(msgtxt);
			        //提交之前置灰暂存草稿按钮
			        $(this).attr('disabled',true);
			        $("form[name='form2']").attr('action', "wx_senddsm.htm?method=toDraft&timee="+new Date().getTime());
			        $("form[name='form2']").submit();
			    });
				
			});
			function infomodelop(){
			$("#infomodel").dialog({
				modal:true,
				title:getJsLocaleMessage("ydwx","ydwx_dtwxfs_59"), 
				height:300,
				closeOnEscape: false,
				width:500,
				close:function(){
				}
				});
	    }
			//发送
			function btsend()
			{
				subcount = subcount+1;
				if(subcount > 1)
				{
					alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_60"));
					$("form[name='" + formName + "']").attr("action","");
					return;
				}
				$("#btsend").attr("disabled",true);
				$("#btcancel").attr("disabled",true);
			    if($("#taskname").val()==getJsLocaleMessage("ydwx","ydwx_dtwxfs_58"))
				{
					$("#taskname").val("");
				}
				var sendTime = $('#timerTime').val();
				var serverTime ;
				$.post("common.htm", {
					method : "getServerTime"
				}, function(msg) {
					serverTime = msg;
					var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
					var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                    date1.setMinutes(date1.getMinutes() - 1, 0, 0);
					if (date1 <= date2) {
						alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_61"));
						$("#timerTime").val("");
						showSelect();
						$("#subSend").attr("disabled","");
						$("#qingkong").attr("disabled","");
						$("#detail_Info").dialog("close");
						$("#btsend").attr("disabled","");
						$("#btcancel").attr("disabled","");
						return;
					}
					else
					{
						if($("#effs").text()=="0")
						{
							alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_62"));
							$("#btsend").attr("disabled","");
							$("#btcancel").attr("disabled","");
						}else
						{
							$("#btsend").attr("disabled",true);
							$("#btcancel").attr("disabled",true);
							$("form[name='" + formName + "']").attr("target","_self");
							var lguserid =$("#lguserid").val();
	                        var lgcorpcode =$("#lgcorpcode").val();
	                        var lgusername =$("#lgusername").val();
	                        var netId =$("#netId").val();
	                        
							//$("form[name='" + formName + "']").attr("action","wx_senddsm.htm?method=send&lguserid="+lguserid+"&lgusername="+lgusername+"&lgcorpcode="+lgcorpcode+"&netId="+netId);
							var encoding="";
							var enctype="";
							if($("form[name='" + formName + "']").attr("encoding"))
							{	
							encoding="application/x-www-form-urlencoded";
								$("form[name='" + formName + "']").attr("encoding","application/x-www-form-urlencoded");
							}else
							{
							enctype="application/x-www-form-urlencoded";
								$("form[name='" + formName + "']").attr("enctype","application/x-www-form-urlencoded");
							}
							// may 此处是新增的，主要目的是解决 后退后，出现之前操作出现的弹出框。
						var preStr =$("#preStr").val();
						var taskId=$("#taskId").val();
						var spUser=$("#spUser").val();
						var taskname=$("#taskname").val();
						var msgob = $("form[name='form2']").find("textarea[name='msg']");
						var msg=msgob.val();
						var reg;
						if("zh_CN" === empLangName){
							reg=/{#参数(.*?)#}/g;
						}else if("zh_TW" === empLangName){
							reg=/{#參數(.*?)#}/g;
						}else if("zh_HK" === empLangName){
							reg=/{#Param(.*?)#}/g;
						}
						msg=msg.replace(reg,replacer);
						var sendType = $("#sendType").val();
						var busCode=$("#busCode").val();
						var timerStatus = $("input:radio[name='timerStatus']:checked").attr("value");
						var timerTime=$("#timerTime").val();
						var lgusername=$("#lgusername").val();
						//贴尾内容
						var smsTail=$("#smsTail").val();
						//草稿id
						var draftId=$("#draftId").val();
				$.post(
				path+"/wx_senddsm.htm",{method:"send",lguserid:lguserid,draftId:draftId,smsTail:smsTail,lgusername:lgusername,msg:msg,timerStatus:timerStatus,timerTime:timerTime,sendType:sendType,busCode:busCode,taskname:taskname,lgusername:lgusername,lgcorpcode:lgcorpcode,netId:netId,preStr:preStr,taskId:taskId,spUser:spUser,encoding:encoding,enctype:enctype},
				function(result){

					if(result!="-1")
					{
						if(result.substr(0,3) == "000"){
							getCt();
							$("#oldTaskId").attr("value",result.substr(4));
							$("#message").dialog({width:300,height:180});
							$("#message").dialog("open");
						}
						else
						{
							if(result=="timerSuccess")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_63"));
								getCt();
							}else if(result=="timerFail")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_64"));
							}else if(result=="createSuccess")
							{ 
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_65"));
								getCt();
							}else if(result=="saveSuccess")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_66"));
								
							}else if(result.indexOf("empex") == 0)
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_67")+result.substr(5));
							}
							else if("nogwfee"==result || "feefail"==result){
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_68"));
							}else if(result.indexOf("lessgwfee")==0){
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_69"));
							}
							else if(result=="error")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_70"));
							}else if(result=="nomoney")
							{
							    alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_71"));
							}else if(result=="false")
							{
							    alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_72"));
							}
							else if(result=="timeError")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_73"));
							}
							else if(result=="spuserfee:-2")
							{
						    	alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_74"));
							}
							else if(result=="spuserfee:-1"||result=="spuserfee:-3")
							{
						    	alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_75"));
							}
							else if(result=="nospnumber")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_76")+smsaccount+getJsLocaleMessage("ydwx","ydwx_dtwxfs_77"));
							}
							else if(result == "subnoFailed"){
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_78"));
							}
							else
							{
								alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_78")+result);
							}
							location.reload();
						}
					}
				});
					}
					}
				});
			}	
			

			
			//取消
			function btcancel1()
			{
			    showSelect();
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				$("#detail_Info").dialog("close");
			}
		      //错误上传内容下载
			function uploadbadFiles()
			{
			    var badurl = $("#badurl").val();
			    badurl = badurl.replace("_view.txt","_bad.txt");
			   	$.post("common.htm?method=checkFile", {
					url : badurl,
					upload : "yes"
				},
					function(result) {
						if (result == "true") {
							download_href(path+"/doExport.hts?u="+badurl);				
						} else if (result == "false") {
                            /*文件不存在*/
                            alert(getJsLocaleMessage("ydwx", "ydwx_dtwxfs_80"));
                        } else {
                            /*出现异常,无法跳转*/
                            alert(getJsLocaleMessage("ydwx", "ydwx_dtwxfs_81"));
                        }
				});
			}
			
			// 该方法通过查询未被使用 需要进一步确认
			function bycheck(type)
			{
			   var sendTypevalue = $("#sendType").val();
			   if(type == '1')
			   {
			      $("#dtsend").removeClass("wjsendclss");
			      $("#wjsend").removeClass("dtsendclass");
			      $("#dtsend").addClass("dtsendclass");
			      $("#wjsend").addClass("wjsendclss");
			      $("#dtsend font").removeClass();
			      $("#wjsend font").removeClass();
			      $("#dtsend font").addClass("send_ac1");
			      $("#wjsend font").addClass("send_ac2");
			      $("#showmessage").show();
			      $("#sendType").attr("value","2");
			      if(sendTypevalue != "2")
			       {
				       fileCount=1;
				      $("#filesdiv").find("> input").remove();
				       $("#filesdiv").find("> a:eq(0)").after("<input id='numFile1' name='numFile1'  type=file onchange='ch();' class='numfileclass2' value=''/>");
				       $("#upfilediv").hide();
				       $("#upfilediv").html("");
				       $("#allfilename").empty();
			       }
			       $("#txtstyle").css("background","url("+inheritPath+"/skin/"+skin+"/images/dtsend.png) no-repeat");
			       $("#xlsstyle").css("background","url("+inheritPath+"/skin/"+skin+"/images/dtsend.png) no-repeat 0px -80px");
			       $("#errormessage").empty();
			   }
			   else
			   {
			       $("#dtsend").removeClass("dtsendclass");
			       $("#wjsend").removeClass("wjsendclss");
			       $("#wjsend").addClass("dtsendclass");
			       $("#dtsend").addClass("wjsendclss");
			       $("#dtsend font").removeClass();
			       $("#wjsend font").removeClass();
			       $("#dtsend font").addClass("send_ac2");
			       $("#wjsend font").addClass("send_ac1");
			       $("#showmessage").hide();
			       $("#sendType").attr("value","3");
			       if(sendTypevalue != "3")
			       {
				       fileCount=1;
				        $("#filesdiv").find("> input").remove();
				        $("#filesdiv").find("> a:eq(0)").after("<input id='numFile1' name='numFile1'  type='file' onchange='ch();' class='numfileclass2' value=''/>");
				       $("#upfilediv").hide();
				       $("#upfilediv").html("");
				       $("#allfilename").empty();
			       }
			       $("#txtstyle").css("background","url("+commonPath+"/common/skin/"+skin+"/images/wjsend.png) no-repeat");

			       $("#xlsstyle").css("background","url("+commonPath+"/common/skin/"+skin+"/images/wjsend.png) no-repeat 0px -80px");
			       $("#errormessage").empty();
			       $("#contents").attr("value","");
			       $("#tempSelect").val("");
			   }		   
	           filedivHover();
			}
			
			//加载上传附件鼠标移动的事件
			function filedivHover()
			{
			   	$("#filesdiv input").hover(function(e){
	               $("#afile").css("text-decoration","underline");
				},function(e){
	                  $("#afile").css("text-decoration","none");
				});
			}
			
			//初始化样式
			function inputTipText(){
				//所有样式名中含有graytext的input
				$("input[class*=graytext]").each(function(){
					var oldVal=$(this).val(); //默认的提示性文本
					$(this)
					.css({'color':'#ccc'}) //灰色
					.focus(function(){
					if($(this).val()!=oldVal)
						{$(this).css({'color':'#000'})}
					else
						{$(this).val('').css({'color':'#ccc'})}
					})
					.blur(function(){
					if($(this).val()=="")
						{$(this).val(oldVal).css({'color':'#ccc'})}
					})
					.keydown(function(){
						$(this).css({'color':'#000'})
					})
				})
			}
			$(function(){
				inputTipText(); //直接调用就OK了
			});
			//浏览按纽事件
			function ch(){
		 		var pathValue="";
		 		pathValue = $("#numFile"+fileCount).val();
				var index = pathValue.lastIndexOf("\\");
				var name =fileName= pathValue.substring(index + 1);
				if(name.length >12)
				{
				   name = name.substring(0,12)+"....";				}
		  		if(checkFile("numFile"+fileCount)){   		     
		  		     if ($("#tr"+fileCount).length == 0){ 
		  		     	var fileType=getFileType(fileName),icon;
					 	 if(fileType=='txt'){
					 	 	icon='txt.gif';
					 	 }else if(fileType=='xls' || fileType=='xlsx' || fileType=='et'){
					 	 	icon='xls.gif';
					 	 }else{
					 	 	icon='fileimg.png';
					 	 }
		  		         $("#upfilediv").append("<div id='tr"+fileCount+"' style='margin-bottom:0px;height:35px;line-height:35px;'><a style='background : url("+commonPath+"/common/img/"+icon+") no-repeat left center;padding:2px 0;margin-left:5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;<label stype='width:160px;'>"+name+"</label>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delfile("+fileCount+")' style='text-decoration: none;color: #2970c0;'>"+getJsLocaleMessage("common","common_delete")+"</a></div>");
		  		         $("#upfilediv").show();        			 
			   			 $("#numFile"+fileCount).css("display","none");
			   			 fileCount++; 
			   			 var ss = $("#filesdiv").html();      			
			   			 var ss1 = "<input type='file' id='numFile"+fileCount+"' name='numFile"+fileCount+"' value='' onchange='ch();' class='numfileclass2'/>";
			   			 $("#afile").after(ss1);
			   			 $("#allfilename").append(pathValue+";");
			   			 filedivHover();
		   			  }
		  		}
			}
			
			//选择模板后事件
			var tempCount=1;
			function choiceTmp(id,name){
				$("#tempDiv").dialog("close");
				$("#netId").val(id);

		 		var pathValue="";
		 		pathValue = $("#tempFile"+tempCount).val();
				if(name.length >12)
				{
				   name = name.substring(0,5)+"....";
				}
		   		     
		  		     if ($("#temptr"+tempCount).length == 0){ 
		  		         $("#tempupfilediv").append("<div id='temptr"+tempCount+"' style='float:left;width: 245px;margin-bottom:0px;height:35px;line-height:35px;'><a style='background : url("+commonPath+"/common/img/fileimg.png) no-repeat left center;padding:2px 0;margin-left:5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;<label stype='width:160px;'>" 
		  		                + name +"</label>"+ "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:Look("+ id +")' style='text-decoration: none;color: #2970c0;'>"+getJsLocaleMessage("common","common_preview")+"</a>"
		  		                +"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deltempfile("+tempCount+")' style='text-decoration: none;color: #2970c0;'>"+getJsLocaleMessage("common","common_delete")+"</a></div>");
		  		         $("#tempupfilediv").show();        			 
			   			 $("#tempFile"+tempCount).css("display","none");
			   			 tempCount++; 
			   			 var ss = $("#eq_sendDiv").html();      			
			   			 var ss1 = "<input type='file' id='tempFile"+tempCount+"' name='tempFile"+tempCount+"' value='' onchange='choiceTmp("+id+","+name+");' class='numfileclass2'/>";
			   			 $("#tempfilesdiv").find("> a:eq(0)").after(ss1);
			   			 $("#alltempname").append(pathValue+";");
			   			 filedivHover();
		   			  }
		  		
			}
			
			//发送成功跳转群发任务查看界面
			 function sendRecord(lguserid, lgcorpcode)
			 {
				closemessage();
				var oldTaskId = $("#oldTaskId").val();
				window.parent.openNewTab('2700-2100',path+"/wx_taskreport.htm?method=find&taskid="+oldTaskId+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
			 }
			 function closemessage()
			 {
				 $("#message").dialog("close");
				 reloadPage();
			 }
			 
			 function setDefault()
			{
				if(confirm(getJsLocaleMessage("ydwx","ydwx_dtwxfs_82"))) {
					var lguserid = $('#lguserid').val();
					var lgcorpcode = $('#lgcorpcode').val();
					var busCode = $("#busCode").val();
					var spUser = $("#spUser").val();
					var repeat = $("input:radio[name='checkrepeat']:checked").attr("value");
					$.post("wx_senddsm.htm?method=setDefault", {
						lguserid: lguserid,
						lgcorpcode:lgcorpcode,
						busCode: busCode,
						spUser: spUser,
						repeat:repeat,
						flag: "8"			
						}, function(result) {
						if (result == "success") {
							/*当前选项设置为默认成功！*/
							alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_83"));
							return;
						} else {
							/*当前选项设置为默认失败！*/
							alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_84"));
							return;
						}
					});
				}
			}
			
			//根据SP账号设置短信最长位数
			function setContentMaxLen()
			{
				var spUser = $('#spUser').val();
				$.post("wx_send.htm",{method : "getSpGateConfig",spUser : spUser},
					function(infoStr){
						if(infoStr !="error" && infoStr.startWith("infos:"))
						{
							var infos = infoStr.replace("infos:","").split("&");
							var interGW = infos[3];
							var interGWs = interGW.split(",");
							entotalLen = interGWs[5];
							//签名前置
							if(interGWs[10] ==1)
							{
								entotalLen = entotalLen - interGWs[7];
							}
							contentLen = infos[4];
							baseContentLen = contentLen;
							$('#maxLen').html("/"+contentLen);
						}
						len($("#contents"));
					}
				);
			}
			//通过短信内容设置短信最长位数
			function setSmsContentMaxLen(content)
			{
				var isChinese = false;
				var enLen = 0;
				var charCode;
				var pattern = /(9[1-4]|12[3-6])/;
				var enMsgShortLen = 0;
				for(var j=0;j<content.length;j++)
				{
					enMsgShortLen += 1;
					enLen += 1;
					charCode = content.charAt(j).charCodeAt();
					if(charCode > 127)
					{
						isChinese = true;
						break;
					}
					if(pattern.test(charCode))
					{
						//长短信边界值
						if(enLen % entotalLen == 0)
						{
							//条数加2
							enLen += 2;
						}
						else
						{
							enLen += 1;
						}
						enMsgShortLen += 1;
					}
					//英文短信长度超过最大短信长度
					if(enLen > contentLen)
					{
						$("#contents").val(content.substring(0,j));
						break;
					}
				}
				
				if(isChinese && contentLen == 620)
				{
					contentLen = 270;
				}
				else if(!isChinese && contentLen == 270 && baseContentLen == 620)
				{
					contentLen = 620;
				}
			}
			
			//定义startWith方法
			String.prototype.startWith=function(str){
				if(str==null||str==""||this.length==0||str.length>this.length)
				return false;
				if(this.substr(0,str.length)==str)
				return true;
				else
				return false;
				return true;
			}
			
			
			//设置贴尾
			function setTailInfo(){
			    //业务类型
			    var busCode = $('#busCode').val();
			    //sp账号
			    var spUser = $("#spUser").val();
			    //返回结果
			    var result = true;
			    //先根据其他贴尾类型查找贴尾 再根据全局贴尾 找到就终止
			    $.ajax({
			        url:'common.htm',
			        data:{method:'setTailInfo',busCode:busCode,spUser:spUser,lgcorpcode:GlobalVars.lgcorpcode},
			        type:'post',
			        dataType:'text',
			        async:false,
			        beforeSend:function(){
			            $('#wx_lefttitle').hide();
			            $('#wx_righttitle').hide();
			            $('#smsTailContens').text('');
			            //传输后台使用
			            $('#smsTail').val('');
			        },
			        success:function(data){
			            data = eval('('+data+')');
			            if(data.status == 1){//找到对应贴尾
			                $('#smsTailContens').text(data.contents);
				            $('#wx_lefttitle').show();
				            $('#wx_righttitle').show();
			                $('#smsTail').val(data.contents);
			            }
			        },
			        error:function(){
			            result = false;
			        },
			        complete:function(){

			        }
			    })
			    return result;
			}
		
			//选择草稿
			function showDraft(){
			    var draftstype = 5;
				var path = $("#cpath").val();
			    var frameSrc = path+"/common.htm?method=getDrafts&draftstype="+draftstype+"&timee="+new Date().getTime();
			    $("#draftFrame").attr("src",frameSrc);
			    $("#draftDiv").dialog("open");
			}
			
			//选择草稿记录
			function draftSure()
			{
			    var $fo = $("#draftFrame").contents();
			    var $ro = $fo.find("input[type='radio']:checked");
			    if(!$ro.val())
			    {
			        alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_85"));
			        return;
			    }

			    //已存在草稿箱 提示覆盖
			    if($('#containDraft').val()){
			        if(confirm(getJsLocaleMessage("ydwx","ydwx_dtwxfs_86"))){
			            $('#containDraft').parents('tr').remove();
			            
			            $('#draft_file').remove();
			            //删除草稿文件后 清空草稿信息
			            $('#draftFile').val('');
			            $('#draftId').val('');
			        }else{
			            $("#draftDiv").dialog("close");
			            return false;
			        }
			    }
			    //如果选择草稿箱，覆盖之前选择的文件
			      fileCount=1;
			      $("#filesdiv").find("> input").remove();
			       $("#filesdiv").find("> a:eq(0)").after("<input id='numFile1' name='numFile1'  type=file onchange='ch();' class='numfileclass2' value=''/>");
			       $("#upfilediv").hide();
			       $("#upfilediv").html("");
			       $("#allfilename").empty();
			       
			       //回填草稿箱内容前 如若存在已选择的模板 则需取消掉模板 
			      tempNoShow();
			       
			    var $tr = $ro.parents('tr');
			    //草稿箱发送文件相对路径
			    var filePath = $tr.find('td:eq(0)').attr('path');
			    var draftId = $ro.val();
			    var taskname = $tr.find('td:eq(2)').find('label').text()||'';
			    var msg = $tr.find('td:eq(3)').find('label').attr('msg');
				var index = filePath.lastIndexOf("\\");
				var fileName= filePath.substring(index + 1);
				var fileType=getFileType(fileName);
			 	 if(fileType=='txt'){
				 	 	icon='txt.gif';
				 	 }else if(fileType=='xls' || fileType=='xlsx' || fileType=='et'){
				 	 	icon='xls.gif';
				 	 }else{
				 	 	icon='fileimg.png';
				 	 }
 		         $("#upfilediv").append("<div id='draft_file' style='margin-bottom:0px;height:35px;line-height:35px;'><input type='hidden' id='containDraft' name='containDraft' value='1'>" +
 		         		"<a style='background : url("+commonPath+"/common/img/"+icon+") no-repeat left center;" +
 		         		"padding:2px 0;margin-left:5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>" +
 		         		"&nbsp;&nbsp;&nbsp;<label stype='width:160px;'>"+getJsLocaleMessage("ydwx","ydwx_dtwxfs_87")+"</label>" +
 		         		"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delRow()' " +
 		         		"style='text-decoration: none;color: #2970c0;'>"+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+"</a></div>");  		     
  		         $("#upfilediv").show();
			    $('#draftFile').val(filePath);
			    $('#draftId').val(draftId);
			    $('#taskname').css('color','').val(taskname);
			    $('#contents').val(msg);
			    len($('#contents'));
			    $("#draftDiv").dialog("close");
			}
			/**
			 * 删除草稿箱文件行
			 * @param obj
			 */
			function delRow(){
		    if(confirm(getJsLocaleMessage("ydwx","ydwx_dtwxfs_47"))){
				   $("#draft_file").remove();
				   if($("#upfilediv").html() == "")
				   {
				      $("#upfilediv").hide();
				   }
		        
		        //删除草稿文件后 清空草稿信息
		        $('#draftFile').val('');
		        $('#draftId').val('');
		    	}
			}
			function draftCancel()
			{
			    $("#draftDiv").dialog("close");
			}
			/**
			 * 保存草稿回调
			 */
			function saveDraft(result){
			    result = eval('('+result+')');
			    if(result.ok == 1){
			        $('#draftFile').val(result.draftpath);
			        $('#draftId').val(result.draftid);
			        alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_88"))
			    }else if(result.ok == -1){
			        alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_89"));
			    }else if(result.ok == -2){
			        alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_90"));
			    }else{
			        alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_91"));
			    }
			    //暂存草稿按钮置为可用
			    $('#toDraft').attr('disabled',false);
			}