var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var contentLen = 980;
var baseContentLen = 980;
// 获取1到n-1条英文短信内容的长度
var entotalLen = 153;

function getContentVal(){
	var cs = getJsLocaleMessage('dxzs','dxzs_ssend_alert_309');
	if(cs == '参数')
	{
		var msg = $("#contents").val(),
		reg=/\{#参数(.*?)#\}/gi;
	}
	else if (cs == '參數')
	{
		var msg = $("#contents").val(),
		reg=/\{#參數(.*?)#\}/gi;
	}
	else
	{
		var msg = $("#contents").val(),
		reg=/\{#param(.*?)#\}/gi;
	}
	
	msg=msg.replace(reg,replacer);
	return msg;
}
function getChangeVal(){
	var msg = $("#contents").val(),
		reg=/#p_(.*?)#/gi;
	msg=msg.replace(reg,replacerChange);
	return msg;
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
            $('.tailcontents').text('');
            $('#tailcontents').val('');
            $('#tailstate').val('0');
            $("#dtailcontents").hide();
            $("#wtailcontents").hide();
        },
        success:function(data){
            data = eval('('+data+')');
            if(data.status == 1){//找到对应贴尾
            	$('#tailstate').val(data.status);
                $('.tailcontents').text(data.contents);
                $('#tailcontents').val(data.contents);
                var sendType = $("#sendType").val();
                if(sendType == 2)
            	{
	                $("#dtailcontents").show();
            	}
                else
            	{
	            	$("#wtailcontents").show();
            	}
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

//获取尾号的方法
function getGateNumber(){
	// $("#subSend").attr("disabled","disabled");
    var isReply = $("input:radio[name='isReply']:checked").attr("value");
    var spUser = $("#spUser").val();
    if(spUser == null || spUser == ""){
    	return ;
    }
    //设置贴尾信息
    if(!setTailInfo())
    {
        return;
    }
    //设置短信最长位数
    setContentMaxLen(spUser);
     //点不用回复，不请求后台
    if(isReply=="0"){
    	$("#subno").html("");
		$("#subNo").val("");
		$("#curSubNo").css("display","none");
		$("#subSend").attr("disabled","");
		return ;
    }
	var circleSubNo = $("#circleSubNo").val();
	var taskId = $("#taskId").val();
	var lgcorpcode =$("#lgcorpcode").val();
    var lgguid =$('#lgguid').val();
    
	$.post("common.htm?method=getSubNo", 
	{
		spUser: spUser,
		isReply:isReply,
		circleSubNo:circleSubNo,
		taskId:taskId,
		lgcorpcode:lgcorpcode,
		lgguid:lgguid					
	}, function(result) {
		$("#subSend").attr("disabled","");
	if(result == "noSubNo"){
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_91'));
		$("#subno").html("");
		$("#subNo").val("");
		return ;
	}else if(result == "error"){
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_93'));
		$("#subno").html("");
		$("#subNo").val("");
		return ;
	}else if(result == "noUsedSubNo"){
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_92'));
		$("#subno").html("");
		$("#subNo").val("");
		return ;
	}
	if(isReply != 0){
		$("#curSubNo").css("display","inline");
	}else{
		$("#curSubNo").css("display","none");
	}
	
	if(result !=null && result !="")
	{
	    var index = result.indexOf("}");
	    result = result.substring(0,index+1);
		var data = eval("("+result+")");
		$("#subNo").val(data.subNo);
		$("#subno").html(data.subNo);
		var sendFlag = (data.sendFlag).split("&");
		if(sendFlag[0] == "false" || sendFlag[1] == "false" || sendFlag[2] == "false"){
			$("#subno").css("color","red");
		}else{
			$("#subno").css("color","blue");
		}
		$("#sendFlag").val(data.sendFlag);
		if(isReply == 1){
			$("#circleSubNo").val(data.subNo);
			}
		}					
	});
}

//根据SP账号设置短信最长位数
function setContentMaxLen(spUser)
{
	$.post("ssm_comm.htm?method=getSpGateConfig&spUser="+spUser,{},
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
				//动态模板发送,参数为中文
				if(contentLen == 700)
				{
					contentLen = 350;
				}
				baseContentLen = contentLen;
				$('#maxLen').html("/"+contentLen);
			}
			eblur($("#contents"));
		}
	);
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

//当前时间与服务器时间对比--guodw
function checkServerTime(){
	
	//----------------------------------

}
//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
// 统计短信内容字数
function len(ob) {
	var tailcontents = $('#tailcontents').val()||'';
	//var smsContent = $.trim(ob.val());
    var smsContent = ob.val();
	//贴尾内容+短信内容，贴尾内容放在前面便于超长短信内容截取
	var content = tailcontents + smsContent;
	var huiche = content.length - content.replaceAll("\n", "").length;
	if(content.length+huiche>contentLen)
	{
		$("#contents").val(content.substring(tailcontents.length,contentLen-huiche));
		//短信内容
		smsContent = $("#contents").val();
		//贴尾内容+短信内容
	 	content = tailcontents + smsContent;
	 	huiche = content.length - content.replaceAll("\n", "").length;
		$('#maxLen').html("/"+contentLen);
	}
	var len = contentLen;
	if(baseContentLen == 700)
	{
		setSmsContentMaxLen(tailcontents, content);
		content = tailcontents + $("#contents").val();
		huiche = content.length - content.replaceAll("\n", "").length;
	}
	if(len != contentLen)
	{
		content = tailcontents + $("#contents").val();
		if(content.val().length>contentLen)
		{
			$("#contents").val(content.substring(tailcontents.length,contentLen));
			smsContent = $("#contents").val();
			huiche = smsContent.length - smsContent.replaceAll("\n", "").length;
		}
		$('#maxLen').html("/"+contentLen);
	}
	$('form[name="' + formName + '"]').find(' #strlen').html(
			($("#contents").val().length+huiche+tailcontents.length));
};

// 失去焦点时判断
function eblur(ob) {
	var $parent = ob.parent();
	$parent.find(".add_error").remove();
	/*if (ob.is("#taskname")) {
		if ($.trim(ob.val()) == "") {
//			var errorMsg = "&nbsp;请输入主题";
//			$parent.append("<em class='add_error'>" + errorMsg + "</em>");
//			document.forms[formName].isOk.value = 0;
		} else {
			document.forms[formName].isOk.value = 2;
		}
	}*/
	if (ob.is("#contents")) {
		var contentsVal=getContentVal();//替换后的文本内容
		var content = $("#contents").val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > contentLen) {
			$("#contents").val(content.substring(0, contentLen - huiche));
		}
		if($("#tempSelect").val()=="")
		{
			$("#inputContent").val(contentsVal);
		}
		len(ob);
		//var str=$.trim(ob.val());
		if ($.trim(ob.val()) == "") {
			$("#errormessage").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_25'));
			document.forms[formName].isOk.value = 1;
		}
		else if(!/#[Pp]_[1-9][0-9]*#+/g.test(contentsVal)){
			    $("#errormessage").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_277'));
				document.forms[formName].isOk.value = 1;
		}else if (ob.val().length > contentLen) {
			$("#errormessage").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_278')+contentLen+getJsLocaleMessage('dxzs','dxzs_ssend_alert_162'));
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
		var content = $(this).val();
		//content = $.trim(content);
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > contentLen) {
			$(this).val(content.substring(0, contentLen));
		}
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
	doAddDraft();
	$("#tempDiv").dialog({
		autoOpen: false,
		height:440,
		width: 620,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			//$("#addSmsTmpDiv").attr("src","");
		}
	});
	resizeDialog($("#tempDiv"),"ydbgDialogJson","dxzhsh_xtnrqf_test1");
});

//点击保存草稿箱时的方法
function doAddDraft()
{
	// 点击暂存草稿按钮时
	$("input[id='toDraft']").click(function() {
		$("#toDraft").attr("disabled",true);
		$("#subSend").attr("disabled",true);
		$("#qingkong").attr("disabled",true);
		
		//暂存草稿前判断发送内容以及发送号码是否为空
        var msg = $.trim($('#contents').val());
        var len = $('#upfilediv li').length;
        //var sendType = $("form[name='" + formName + "']").find("#sendType").val();
        if(len == 0)
        {
        	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_279'));
            $("#toDraft").attr("disabled","");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
            return;
        }
        var nomsgs = getJsLocaleMessage('dxzs','dxzs_ssend_alert_36');
        if($("#taskname").val()==nomsgs)
        {
            $("#taskname").val("");
        }
        
		var formObj = document.forms[formName];
		
		var $texta = $("form[name='" + formName + "']").find("textarea[name='msg']");
		var msgText=getContentVal();
		var sendType = $(
				"form[name='" + formName + "']")
				.find("#sendType").val();
		if (sendType != 3) {
			$texta.trigger("blur");
			$("#dtMsg").val(msgText);
		}
		
			$("#probar").dialog({
				modal:true,
				title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_37'), 
				height:70,
				resizable :false,
				closeOnEscape: false,
				width:200,
				open: function(){
				$("#probar").css("height","50px");
				$(".ui-dialog-titlebar").hide();
				errorNum=0;
				//dd = window.setInterval("fresh()",3000);
				//$("body").css("background-color","gray");
				}
			});
			$("#sendtypeHidden").val($("#sendType").val());
			//$('#contents').val(getContentVal());
			 $("form[name='form2']").attr("action","dsm_previewSMS.htm?method=toDraft&lguserid="+$("#lguserid").val());
			$("form[name='" + formName + "']").submit();
		
	});
}

//点击预览时的方法
function dopreview()
{
	// 点击创建或存草稿按钮时
	$("input[id='subSend'],input[id='creSend']").click(function() {
		$("#subSend").attr("disabled",true);
		$("#qingkong").attr("disabled",true);
		//标示判断是否有异常（具体针对内存溢出的异常）
		$("#error").val("0")
		$("form[name='" + formName + "']").find(
				"input[name='subState']").val(2);

		var formObj = document.forms[formName];
		if (formObj.busCode.value == "") {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_23'));
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			return;
		}
		if (formObj.spUser.value == "") {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_24'));
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			return;
		}
		var $texta = $("form[name='" + formName + "']").find("textarea[name='msg']");
        //处理短信内容前后空白字符并重新统计字数
        $texta.val($.trim($texta.val()));
        len($texta);
		var msgText=getContentVal();
		//alert("---");
		var sendType = $(
				"form[name='" + formName + "']")
				.find("#sendType").val();
		if (sendType != 3) {
			$texta.trigger("blur");
			$("#dtMsg").val(msgText);
		}

		if (formObj.isOk.value == 1
				&& sendType != 3) {
			if($.trim(msgText)=="")
			{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_25'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}else{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_280'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
		}
		var isReply = $("input:radio[name='isReply']:checked").attr("value");
		//不用回复时,不对通道号位数进行判断
		if(isReply != 0)
		{
			if($("#subNo").val() == ""){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_26'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			var sendFlag = $("#sendFlag").val().split("&");
			if(sendFlag[0] == "false" && sendFlag[1] == "false" && sendFlag[2] == "false"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_28'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[0] == "false" && sendFlag[1] == "false" ){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_29'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[0] == "false" && sendFlag[2] == "false"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_30'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[1] == "false" && sendFlag[2] == "false"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_31'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[0] == "false" ){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_32'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[1] == "false"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_33'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[2] == "false"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_34'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
		}
		
		var validContent = $("#dtMsg").val()+$("#dtailcontents .tailcontents").text();
		if($("#sendType").val()&&2==$("#sendType").val()&&-1 == validContent.indexOf("退订")){

			$("#confirmMsgText").dialog({
				autoOpen: false,
				resizable: false,
				modal: true,
				width:500,
				height:250
		    });
		    $("#confirmMsgText").dialog("open");

		}else{
			if (checkFile1()) {
				//关键字检查放到提交表单来处理
				//checkWords();
				//验证是否定时
				checkTimer()
			}
		}	
	});
}
function confirmBtn() {
	$("#confirmMsgText").dialog("close");
	if (checkFile1()) {
		//关键字检查放到提交表单来处理
		//checkWords();
		//验证是否定时
		checkTimer()
	}
}
function cancelBtn() {
	$("#subSend").attr("disabled","");
	$("#qingkong").attr("disabled","");
	$("#confirmMsgText").dialog("close");
}
function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

// 验证上传文件格式
function checkFile1() {
	  if($("#upfilediv").html() == "" || trim($("#upfilediv").html()).length <=0)
	  {
		    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_89'));
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			return false;
	  }	
	  else
	  {
		  return true;
	  }
}

// 验证短信内容是否包含关键字
function checkWords() {
//	$("#subSend").attr("disabled",true);
//	$("#qingkong").attr("disabled",true);
	var sendType = $("#sendType").val();
	if(sendType == 2)
	{
		var msg = getContentVal();
		$.ajax({
			url: "common.htm?method=checkBadWord1", 
			type : "POST",
			data: {
				tmMsg : msg,
				corpCode : $("#lgcorpcode").val(),
				isAsync : "yes"
			},
			success: function(message) {
				if(message == "outOfLogin")
    	    	{
    	    		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_201'));
    	    		return;
    	    	}
				if(message.indexOf("@")==-1){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_40'));
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
					return;
				}
				message=message.substr(message.indexOf("@")+1);
				if (message != ""&&message!="error") {
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+"：\n     " + message + "\n"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
				} 
				else if (message == "error") {
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_43'));
				} else {
					//alert("2");
					checkTimer();
					// $('form[name="'+formName+'"]').submit();
				}
			},
			error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_40'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				window.location.href='dsm_sendDiffSMS.htm';
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
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_45'));
		$("#subSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
	} else {
		var sendTime = $("#timerTime").val();
		var serverTime ;
		$.post("common.htm?method=getServerTime", {
		
		}, function(msg) {
			serverTime = msg;
			var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
			var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
            date1.setMinutes(date1.getMinutes() - 1, 0, 0);
			if (date1 <= date2 && $("input[name=timerStatus]:checked").val() == 1) {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_20'));
				$("#timerTime").val("");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
		//alert("3");
		//$("#form2").attr('action', "eq_sendDiffSMS.htm?method=add");
			$("#probar").dialog({
				modal:true,
				title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_37'), 
				height:70,
				resizable :false,
				closeOnEscape: false,
				width:200,
				open: function(){
				$("#probar").css("height","50px");
				$(".ui-dialog-titlebar").hide();
				errorNum=0;
//				dd = window.setInterval("fresh()",3000);
				//$("body").css("background-color","gray");
			}
			});
			$("#busCodeHidden").val($("#busCode").val());
			$("#spuserHidden").val($("#spUser").val());
			$("#sendtypeHidden").val($("#sendType").val());
			//$('#contents').val(getContentVal());
			var checkrepeat = $("input[name=checkrepeat]:checked").val();
			$.post('common.htm?method=frontLog',{
					info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_283')+ $("#lguserid").val()+'，taskId:'+$("#taskId").val()+'，checkrepeat:'+checkrepeat
				});
			 $("form[name='form2']").attr("action","dsm_previewSMS.htm?method=preview&checkrepeat="+checkrepeat+"&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val());
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
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_284'));
					window.clearInterval(dd);
					//window.location.href='eq_sendDiffSMS.htm';
					reloadPage();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			
			if(errorNum==0)
			{
				errorNum=1;
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_284'));
			}
			window.clearInterval(dd);
			//window.location.href='eq_sendDiffSMS.htm';
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
		var reTitle=$("#reTitle").val();
		$.ajax({
			url:"dsm_sendDiffSMS.htm",
			data:{method : "getTmMsg1",tmId : tmpOb.val()},
			type:"post",
			success:function(result){
				if(result.indexOf("@")==-1){
					//alert("网络或服务器无法连接，请刷新页面或稍后重试[EXFV005]");
					window.location.href=reTitle;
					return;
				}
				result=result.substr(result.indexOf("@")+1);
				if(result=="error")
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_49'));
					return;
				}
				msgob.val(result);
				//msgob.trigger("blur");
				msgob.attr("readonly","readonly");
				//msgob.attr("disabled","disabled");
				msgob.css("background","#F1F1EB");
				msgob.bind("focus",function(){
					this.blur();
				});
				len(msgob);
					
			},
			error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_285'));
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
	//$("#sendtypeHidden").val(sendtype);
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
	var isTmShow = $("form[name='" + formName + "']").find("#model").is(
			":visible");
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
				// tmpSel.trigger("change");
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
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_51'));
		else
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_287'));
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
		//window.clearInterval(dd);
		$("#error").val("1");
		var SendReq=$("#SendReq").val();
		var cpath = $("#cpath").val();
		$("#probar").dialog("close");
		$(".ui-dialog-titlebar").show();
		if(data.indexOf("empex") == 0)
		{
			//关键字检查
			if(data.indexOf("V10016") >= 0)
			{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+"：\n     " + data.substr(data.indexOf("[V10016]")+8) + "\n"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
				$('#contents').val(getChangeVal());
				$("#subSend").attr("disabled",false);
				$("#qingkong").attr("disabled",false);
				return;
			}
			//表单流上传失败
			if(data.indexOf("V10003") >= 0)
			{
				var lguserid = $("#lguserid").val();
				var lgcorpcode = $("#lgcorpcode").val();
				$.post('common.htm?method=frontLog',{
					info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_288')+ lguserid +'，lgcorpcode:'+ lgcorpcode
				});
			}
            $("#probar").dialog("close");
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_50')+data.substr(5));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		if(data=="error")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_54'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data=="overstep")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_55')+MAX_PHONE_NUM+getJsLocaleMessage('dxzs','dxzs_ssend_alert_56'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data=="overSize")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_57')+MAX_SIZE+getJsLocaleMessage('dxzs','dxzs_ssend_alert_58')+ZIP_SIZE+getJsLocaleMessage('dxzs','dxzs_ssend_alert_59'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data == "draftnotex")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_60'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data == "draftnotfile")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_61'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data == "drafterr")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_62'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		
		//$("#newtable").empty();
		$("#content").empty();
		var msgContent=getContentVal();
		if(data=="noPhone")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_66'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
            $("#qingkong").attr("disabled",false);
            $(".ui-dialog-titlebar-close").hide();
		}
		else
		{
			var array=eval("("+data+")");
			
			$("#hidSubCount").val(array.subCount);
			$("#hidEffCount").val(array.effCount);
			$("#hidMobileUrl").val(array.validFilePath);
			$("#hidPreSendCount").val(array.preSendCount);
			
			$("#counts").text(array.subCount);
		    $("#effs").text(array.effCount);
		    $("#ct").text(array.depFeeCount);
		    $("#spbalance").text(array.spUserFeeCount);
		    $("#yct").text(array.preSendCount);
		    $("#blacks").text(array.blackCount);
		    $("#legers").text(array.badModeCount);
		    $("#sames").text(array.repeatCount);
		    $("#keyW").text(array.kwCount);
		    var wuxiaonum = parseInt(array.subCount)-parseInt(array.effCount);
		    $("#alleff").text(wuxiaonum);
		    $("#badurl").attr("value",array.viewFilePath);
		    $("#isCharg").attr("value",array.isCharge);
		    $("#feeFlag").attr("value",array.feeFlag);
		    var spye = array.spFeeResult;
		     $("#gwFee").attr("value",spye);
		    checkspye(spye);
		    if(wuxiaonum == 0){
		    	 $("#predowninfo").attr("style","display:none");
		    }else{
		    	 $("#predowninfo").attr("style","display:block");
		    }
		    
		    var tdSignHtml = "<b>通道签名：</b><br/>";
		    if(array.td0){
		    	tdSignHtml +="<span style='color:green'>移动</span>："+array.td0.replace("[","【").replace("]","】")+"&nbsp;&nbsp;&nbsp;&nbsp;";
		    }
		    if(array.td1){
		    	tdSignHtml +="<span style='color:green'>联通</span>："+array.td1.replace("[","【").replace("]","】")+"&nbsp;&nbsp;&nbsp;&nbsp;";
		    }
		    if(array.td21){
		    	tdSignHtml +="<span style='color:green'>电信</span>："+array.td21.replace("[","【").replace("]","】")+"&nbsp;&nbsp;&nbsp;&nbsp;";
		    }
		    if(array.td5){
		    	tdSignHtml +="<span style='color:green'>国外</span>："+array.td5.replace("[","【").replace("]","】").replace("[","【").replace("]","】")+"<br />";
		    }
		    // 是否分批，若分配则重新调大弹出框
		    if(array.splitFlag){
		    	var batchHtml = "";
		    	batchHtml +="<div><input type='hidden' id='splitFlag' value='true' /><b>分批设置：</b><div id='custBatch'>";
		    	var date = new Date();
		    	date.setMinutes(date.getMinutes()+5, date.getSeconds(), 0);
		    	date = date.Format("yyyy-MM-dd HH:mm");
		    	for(var i =1;i<3;i++){
		    		batchHtml += "<div class='custBatchNode'><div class='custBatchNode-left'>第<span class='nodeInfo'>"+i+"</span>批<a href='javascript:void(0);' onclick='delCustBatchNode($(this))'>删除此批</a></div><div class='custBatchNode-middle'>发送数量：<input type='text' onkeyup='batchNodeValid($(this))' onpaste='phoneInputCtrl($(this))' class='batchNodeValue'/></div><div class='custBatchNode-right'>发送时间：<input class='Wdate div_bd batchNodeTimeValue' readonly='readonly' onclick="+'"'+"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:#{%m+1}',maxDate:'%y-%M-%d 23:59:00',isShowToday:false})"+'"'+" type='text' value='"+date+"'/></div></div>";
		    	}
		    	batchHtml += "</div>";
		    	
		    	batchHtml +="</div><div class='nodeControl'><a href='javascript:void(0);' onclick='showBatchDetail()'>查看批次详情</a><a href='javascript:void(0);' onclick='addCustBatchNode()'>点击增加一级批次</a></div>";
		    	$("#configBatch").empty();
		    	$("#configBatch").append(batchHtml);
		    	$("#detail_Info").dialog("option", "height", "720" );
		    	$("#detail_Info").dialog("option", "width", "520" );
		    }else{
	    		$("#configBatch").empty();
	    		$("#detail_Info").dialog("option", "height", "530" );
		    	$("#detail_Info").dialog("option", "width", "530" );
		    }
		    
		    $("#tdSign").empty();
		    $("#tdSign").append(tdSignHtml);
		    
		    if(array.effCount-0==0)
		    {
		    	//$("#newtable").append("<tr align='center'><td style='border: 1px  solid; height: 25px;width:30px'>编号</td>" +
				//		"<td style='border: 1px  solid; height: 25px;'><center><div style='width:60px'>手机号码</div></center></td>" +
				//		"<td style='border: 1px  solid; height: 25px;'>短信内容</td></tr>" );
		    	$("#content").append("<thead><tr align='center'><th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_63')+"</th>" +
						"<th><center><div style='width:89px'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_64')+"</div></center></th>" +
						"<th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_65')+"</th></tr></thead>" );
		    	$("#content").append("<tbody><tr><td colspan='3' align='center'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_289')+"</td></tr></tbody>");
		    	$("#detail_Info").css("display","block");
	    		$("#detail_Info").dialog("open");
	    		deleteleftline1();	
		    }else
		    {   	
		    	
		    	//防止预览发出多次异步请求
		    	if(SendReq=="0"){
				    $("#SendReq").val("1");
				    var sendType=$("#sendType").val();
				    var spUser=$("#spUser").val();
				    $("#content").load(
				    	"dsm_sendDiffSMS.htm?method=readSmsContent&url="+array.viewFilePath+"&sendType="+sendType+"&spUser="+spUser,
//				    	"dsm_sendDiffSMS.htm?method=readSmsContent",
//				    	{url:array.viewFilePath,sendType:sendType},
				    	function(){
				    		$("#detail_Info").css("display","block");
				    		$("#detail_Info").dialog("open");
				    		deleteleftline1();	
				    		showSelect();
				    });
				    $(".ui-dialog-titlebar-close").hide();
		    	}
		    }
			
		}
	}
	
	/**
	 * 显示批次详情
	 */
	function showBatchDetail(){
		var batchDetailHtml = "<div style='padding:10px;'><table><tr><th class='td-batch-left'>发送批次</th><th class='td-batch-middle'>发送数量</th><th class='td-batch-right'>发送时间</th></tr>";
		var custBatchNodeArr = $(".custBatchNode");
		var len = custBatchNodeArr.length;
		for (var i = 0; i < len; i++) {
			batchDetailHtml +=("<tr><td class='td-batch-left'>第"+(i+1)+"批</td><td class='td-batch-middle'>"+$(custBatchNodeArr[i]).find(".batchNodeValue").val()+"</td><td class='td-batch-right'>"+$(custBatchNodeArr[i]).find(".batchNodeTimeValue").val()+"</td></tr>");
		}
		batchDetailHtml +="</table></div>";
		$("#batchDetial").empty();
		$("#batchDetial").append(batchDetailHtml);
		$("#batchDetial").dialog({
			autoOpen: true,
			title:"批次详情",
			height:550,
			width: 450,
			resizable:false,
			modal: true
		});
	}

	/**
	 * 关闭批次详情
	 */
	function closeBatchDetail(){
		$("#batchDetial").dialog("close");
	}

	/**
	 * 限制用户输入分批数量时候总数不能超过有效号码数
	 * @param obj
	 */
	function batchNodeValid(obj){
		phoneInputCtrl(obj);
		var thisVal = parseInt(obj.val());
		var sumLimit = parseInt($("#effs").text());
		if(thisVal<=0){
			alert("分批设置应该大于0！");
			obj.focus();
			return;
		}
		//输入值不能大于有效号码数
		if(thisVal > sumLimit){
			alert("分批设置不能超过有效号码数！");
			obj.focus();
			return;
		}
		
		// 自动调节分批设置数值
		autoControlNodeValue(obj);
	}

	/**
	 * 自动调节分批设置
	 * @param obj
	 */
	function autoControlNodeValue(obj) {
		$(".currentBatchNodeClass").removeClass("currentBatchNodeClass");
		obj.addClass("currentBatchNodeClass");
		var thisVal = parseInt(obj.val());
		if(isNaN(thisVal)){
			// 点击删除此批时对象处理
			thisVal = 0;
		}
		var sumLimit = parseInt($("#effs").text());
		var batchNodes = $(".batchNodeValue");
		var sum = 0;
		var len = batchNodes.length;
		var isDel = false;
		// 遍历除了当前编辑框的其他分批设置
		var tempNode;
		var batchNodeValue;
		for (var i = 0; i < len; i++) {
			tempNode = batchNodes[i];
			batchNodeValue = tempNode.value;
			// 计算非当前分批的其他分批总和
			if(undefined != batchNodeValue && ""!=$.trim(batchNodeValue)&&(!$(tempNode).hasClass("currentBatchNodeClass"))){
				sum += parseInt(batchNodeValue);
			}else if(!$(tempNode).hasClass("currentBatchNodeClass")&& i!=len-1){
				// 删除无效分批设置，保留最后一个分批设置框(无论是否值为空，便于之后动态赋值)
				tempNode.parentElement.parentElement.remove();
			}
		}
		var otherLimitVal = sumLimit - thisVal;
		if(otherLimitVal == sum){
			// 当前所有分批总数刚好等于有效号码数，且最后一个分批未设置，则删除未设置的分批
			if(undefined == batchNodeValue || "" == $.trim(batchNodeValue)){
				tempNode.parentElement.parentElement.remove();
			}
		}else if(otherLimitVal > sum){
			// 目前设置的所有分批总数小于有效号码数量，则将所有数量加给最后一个非编辑节点上
			if(!$(tempNode).hasClass("currentBatchNodeClass")){
				if(undefined != batchNodeValue && ""!=$.trim(batchNodeValue)){
					tempNode.value = (parseInt(batchNodeValue) + otherLimitVal - sum);
				}else{
					tempNode.value = (otherLimitVal - sum);
				}
			}else{
				addCustBatchNode();
				var newBatchNodes = $(".batchNodeValue");
				newBatchNodes[newBatchNodes.length-1].value = (otherLimitVal - sum);
			}
		}else{
			// 设置的所有分批总数超过有效号码数量，则从最后一个节点开始减少至等于有效号码数
			// 分批设置超出的数量
			var subVal = sum - otherLimitVal;
			for(var i = len-1; i >=0; i--){
				tempNode = batchNodes[i];
				batchNodeValue = tempNode.value;
				// 节点有值且不是当前编辑的值
				if(undefined != batchNodeValue && ""!=$.trim(batchNodeValue)&&(!$(tempNode).hasClass("currentBatchNodeClass"))){
					if(subVal == parseInt(batchNodeValue)){
						// 分批设置超出的数量刚好等于最后一个非编辑节点的值，直接删除此节点
						tempNode.parentElement.parentElement.remove();
						subVal = 0;
					}else if(subVal <= parseInt(batchNodeValue)){
						// 最后一个非编辑节点的值足够扣除分批设置超出的数量
						tempNode.value = (parseInt(batchNodeValue) - subVal);
						subVal = 0;
					}else{
						// 最后一个非编辑节点的值不够扣除分批设置超出的数量
						tempNode.parentElement.parentElement.remove();
						subVal = subVal - parseInt(batchNodeValue);
					}
				}else{
					// 节点没有值且不是当前编辑的值
					if(!$(tempNode).hasClass("currentBatchNodeClass")){
						tempNode.parentElement.parentElement.remove();
					}
				}
			}
		}
		// 分批设置处理完成之后，重新处理节点编号
		resortBatchNode();
	}
	
	/**
	 * 删除分批节点
	 * @param obj
	 */
	function delCustBatchNode(obj){
		obj.parent().parent().remove();
		autoControlNodeValue(obj);
	}

	/**
	 * 删除分批节点之后，分批显示需要重新排序
	 * 比如删除第2批，之前的第3批要显示为第2批
	 */
	function resortBatchNode(){
		var custBatchNodeArr = $(".custBatchNode");
		var len = custBatchNodeArr.length;
		for (var i = 0; i < len; i++) {
			$(custBatchNodeArr[i]).find(".nodeInfo").text(""+(i+1));
		}
	}

	/**
	 * 添加一级批次
	 */
	function addCustBatchNode(){
		var len = $(".custBatchNode").length;
		if(len >=20){
			alert("分批不能超过20批");
			return;
		}
		var i = len+1;
		var date = new Date();
		date.setMinutes(date.getMinutes()+5, date.getSeconds(), 0);
		date = date.Format("yyyy-MM-dd HH:mm");
//		var batchHtml = "<div class='custBatchNode'><div class='custBatchNode-left'>第1批<button onclick='delCustBatchNode($(this))'>删除此批</button></div><div class='custBatchNode-middle'>发送数量：<input type='text' class='batchNodeValue'/></div><div class='custBatchNode-right'>发送时间：<input type='text' value='2018-12-14 17:00:00'/></div></div>";
		var batchHtml = "<div class='custBatchNode'><div class='custBatchNode-left'>第<span class='nodeInfo'>"+i+"</span>批<a href='javascript:void(0);' onclick='delCustBatchNode($(this))'>删除此批</a></div><div class='custBatchNode-middle'>发送数量：<input type='text' onkeyup='batchNodeValid($(this))' onpaste='phoneInputCtrl($(this))' class='batchNodeValue'/></div><div class='custBatchNode-right'>发送时间：<input class='Wdate div_bd batchNodeTimeValue' readonly='readonly' onclick="+'"'+"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:#{%m+1}',maxDate:'%y-%M-%d 23:59:00',isShowToday:false})"+'"'+" type='text' value='"+date+"'/></div></div>";
		$("#custBatch").append(batchHtml);
		resortBatchNode();
	}

	function checkspye(yeresult)
	{
		var altstr = "";
		if(yeresult.indexOf("lessgwfee") == 0)
		{
			altstr = getJsLocaleMessage('dxzs','dxzs_ssend_alert_67')+yeresult.split("=")[1];
			 $("#btsend").attr("disabled","disabled");
		}else if(yeresult=="feefail" || yeresult=="nogwfee")
		{
			altstr = getJsLocaleMessage('dxzs','dxzs_ssend_alert_68');
			 $("#btsend").attr("disabled","disabled");
		}
		$("#messages2 font").text(altstr);
	}
	
	function checkError()
	{
		if($("#error").val()=="0")
		{
			//window.clearInterval(dd);
			$("#probar").dialog("close");
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_290'));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
            $("#qingkong").attr("disabled",false);
		}
	}
	
	function reloadPage(){
		var menuCode = base_menuCode;
		var url="";
		if (menuCode == "1050-1050") {
			url="dsm_ssendDiffSMS.htm";
		}else{
			url="dsm_sendDiffSMS.htm";
		}
        //var url = "dsm_sendDiffSMS.htm";
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
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_291'));
				var file = $(":file");   
        		file.after(file.clone().val(""));   
        		file.remove(); 
        		return false;
			} else {
				return true;
			}
		}
		else {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_89'));
			return false;
		}
	}
	var lastname = '';
	// 验证上传文件格式
	function checkFile(name) {
		if(lastname == name){return false;}
		lastname = name;
		var fileObj = $("form[name='" + formName + "']").find("input[name='"+name+"']");
		if(fileObj.val() != "") {
			var fileName = fileObj.val();
			var index =fileName.lastIndexOf(".");
			var fileType =fileName.substring(index + 1).toLowerCase(); 
			var trs = $("#allfilename").html();
            if(trs.indexOf(fileName+";")>-1)
            {
            	lastname = '';
            	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_87'));
		    	fileObj.after(fileObj.clone().val(""));   
		    	fileObj.remove(); 
        		return false;
            }
		    
			if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar"&& fileType != "csv") {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_88'));
        		fileObj.after(fileObj.clone().val(""));   
        		fileObj.remove(); 
        		lastname = '';
        		return false;
			} else {
				return true;
			}
		}
		else {
			lastname = '';
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_89'));
			return false;
		}
	}
	//删除附件
	function delfile(filename)
	{	
		if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76')))
		{ 
		   var trs = $("#allfilename").html();
		   trs = trs.replace($("#numFile"+filename).val()+";","");
		   $("#allfilename").html(trs);
		   $("#tr"+filename).remove();
		   $("#numFile"+filename).remove();
		   setFileListHeight();//重新计算上传文件列表高度
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
	
	function checkMax(permitMax){
		var num=parseInt(permitMax);
		if(num>1000000){
			parent.$("#counts").text();
			parent.$("#effs").text();
			parent.$("#blacks").text();
			parent.$("#legers").text();
			parent.$("#sames").text();
			parent.$("#keyW").text();						  
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_292')+MAX_PHONE_NUM+getJsLocaleMessage('dxzs','dxzs_ssend_alert_293')+MAX_PHONE_NUM+getJsLocaleMessage('dxzs','dxzs_ssend_alert_294'));
			parent.$("#subSend").attr("disabled",false);
	        parent.$("#qingkong").attr("disabled",false);
			parent.$("#detail_Info").dialog("close");	
			return false;
			}
		return true;
		}
	function checkTemplate(Template){
		
		if(Template=="false"){
			parent.$("#subSend").attr("disabled",false);
	        parent.$("#qingkong").attr("disabled",false);
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_295'));
			parent.$("#detail_Info").dialog("close");	
			return false;
			}
		return true;
		}
	function checkMatch(match){
		if(match=="false")
		{
			parent.$("#subSend").attr("disabled",false);
	        parent.$("#qingkong").attr("disabled",false);
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_296'));
			parent.$("#detail_Info").dialog("close");
			return false;
		}
		return true;
	}

	function numberControl(va)
	{
		var pat=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
		if(pat.test(va.val()))
		{
			va.val(va.val().replace(/[^\d]/g,''));
		}
	}

		
	function chooseTemp()
	{
		var tpath = $("#cpath").val();
		var frameSrc = $("#tempFrame").attr("src");
//		var lguserid = $("#lguserid").val();
//		var lgcorpcode = $("#lgcorpcode").val();
		var lguserid=GlobalVars.lguserid;
		var lgcorpcode=GlobalVars.lgcorpcode;
		frameSrc = tpath+"/common.htm?method=getLfTemplateBySms&dsflag=1&lguserid="
			+lguserid+"&lgcorpcode="+lgcorpcode+"&timee=" + new Date().getTime();
		$("#tempFrame").attr("src",frameSrc);
		$("#tempDiv").dialog({width:620,height:460});
		resizeDialog($("#tempDiv"),"ydbgDialogJson","dxzhsh_xtnrqf_test1");
		$("#tempDiv").dialog("open");
		
	}

	function tempCancel()
	{
		$("#tempDiv").dialog("close");
	}

	function tempSure()
	{
		var $fo = $("#tempFrame").contents();
		var $ro = $fo.find("input[type='radio']:checked");
		if($ro.val() == undefined)
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_69'));
			return;
		}else
		{
			var message = $ro.next("xmp").text();
			var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
			//模块不可编辑
			if(tmpEditorFlag == 0)
			{
				msgob.attr("readonly","readonly");
				msgob.css("background-color","#E8E8E8");
			}
			msgob.val(message);
		}
		$("#ctem").hide();
		$("#qtem").show();
		//模块不可编辑
		if(tmpEditorFlag == 0)
		{
			$('.para_cg').hide().html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_297'));
			$('.showParams').hide();
		}
		len(msgob);
		$("#tempDiv").dialog("close");
	}

	function tempNo()
	{
		var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
		initContents();
		len(msgob);
		$("#qtem").hide();
		$("#ctem").show();
		$('.para_cg,.showParams').show();
	}
	var inval;
	var mcode;
	function openTemDiv(menuCode)
	{
		mcode = menuCode;
		var tpath = $("#cpath").val();
		var url = tpath+"/tem_smsTemplate.htm?opentm=1&lguserid="
			+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		window.parent.openNewTab(menuCode,url);
		//inval=setInterval("showadd()",300);
	}
	function showadd()
	{
		var $obp = $(parent.window.document);
		if($obp.find("#cont"+mcode).length > 0)
		{
			$obp.find("#cont"+mcode)[0].contentWindow.showAddSmsTmp(0);
			clearInterval(inval);
		}
	}
	
	//立即新建模板
	function showAddSmsTmp(showType)
	{
		$("#addSmsTmpDiv").css("display","block");
		$("#addSmsTmpFrame").css("display","block");

		$("#addSmsTmpDiv").dialog({
			autoOpen: false,
			height:520,
			width: 620,
			resizable:false,
			modal: true,
			open:function(){
				
			},
			close:function(){
				$("#addSmsTmpDiv").css("display","none");
				$("#addSmsTmpFrame").css("display","none");
			}
		});
		
		resizeDialog($("#addSmsTmpDiv"),"ydbgDialogJson","dxzhsh_xtnrqf_test2");
		//新增
	    $("#addSmsTmpFrame").attr("src","tem_smsTemplate.htm?method=doAdd&fromState=1&lguserid="
	    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType="+showType+"&time="+ new Date().getTime());
	          
		$("#addSmsTmpDiv").dialog("open");
	}

	function closeAddSmsTmpdiv()
	{
		$("#addSmsTmpDiv").dialog("close");
		$("#addSmsTmpFrame").attr("src","");
		
	}
	//通过短信内容设置短信最长位数
	function setSmsContentMaxLen(tailcontents, content)
	{
		var huiche = content.length - content.replaceAll("\n", "").length;
		var len = huiche + content.length
		var isChinese = false;
		var enLen = 0;
		var charCode;
		var pattern = /(9[1-4]|12[3-6])/;
		var enMsgShortLen = 0;
		for(var j=0;j<len;j++)
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
				$("#contents").val(content.substring(tailcontents.length,j-huiche));
				break;
			}
		}
		
		if(isChinese && contentLen == 700)
		{
			contentLen = 350;
		}
		else if(!isChinese && contentLen == 350 && baseContentLen == 700)
		{
			contentLen = 700;
		}
	}
	
	/**
	 * 保存草稿回调
	 */
	function saveDraft(data)
	{
		//window.clearInterval(dd);
		$("#probar").dialog("close");
		$(".ui-dialog-titlebar").show();
		if(data.indexOf("empex") == 0)
		{
			//表单流上传失败
			if(data.indexOf("V10003") >= 0)
			{
				var lguserid = $("#lguserid").val();
				var lgcorpcode = $("#lgcorpcode").val();
				$.post('common.htm?method=frontLog',{
					info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_298')+ lguserid +'，lgcorpcode:'+ lgcorpcode
				});
			}
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_71')+data.substr(5));
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		if(data=="error")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_72'));
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="overstep")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_55')+MAX_PHONE_NUM+getJsLocaleMessage('dxzs','dxzs_ssend_alert_56'));
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="overSize")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_57')+MAX_SIZE+getJsLocaleMessage('dxzs','dxzs_ssend_alert_58')+ZIP_SIZE+getJsLocaleMessage('dxzs','dxzs_ssend_alert_59'));
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="noPhone")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_66'));
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="draftnotex")
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_60'));
			//不隐藏按钮
			disabledButton(false);
			return;
		}

		//不隐藏按钮
		disabledButton(false);
			
	    result = eval('('+data+')');
	    if(result.ok == 1){
	        $('#draftFile').val(result.draftpath);
	        $('#draftId').val(result.draftid);
	        alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_74'))
	    }else{
	        alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_71'));
	    }
	}
	
	//显示按钮，false为显示
	function disabledButton(isShow)
	{
		$("#toDraft").attr("disabled",isShow);
		$("#subSend").attr("disabled",isShow);
		$("#qingkong").attr("disabled",isShow);
	}
