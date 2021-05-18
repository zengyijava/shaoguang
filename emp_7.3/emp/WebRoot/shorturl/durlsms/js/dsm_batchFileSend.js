var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var contentLen = 990;
var baseContentLen = 990;
// 获取1到n-1条英文短信内容的长度
var entotalLen = 153;

function getContentVal(){
	var msg = $("#contents").val(),
		reg=/\{#参数(.*?)#\}/gi;
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
	$("#subSend").attr("disabled","disabled");
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
		alert("拓展尾号获取失败！");
		$("#subno").html("");
		$("#subNo").val("");
		return ;
	}else if(result == "error"){
		alert("通道号获取失败！");
		$("#subno").html("");
		$("#subNo").val("");
		return ;
	}else if(result == "noUsedSubNo"){
		alert("系统没有可用的拓展尾号！");
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
			$("#errormessage").html("请输入短信发送内容");
			document.forms[formName].isOk.value = 1;
		}
		else if(!/#[Pp]_[1-9][0-9]*#+/g.test(contentsVal)){
			    $("#errormessage").html("请按格式输入参数");
				document.forms[formName].isOk.value = 1;
		}else if (ob.val().length > contentLen) {
			$("#errormessage").html("短信发送内容过长，应少于"+contentLen+"个字符");
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
        	alert('发送文件不能为空！');
            $("#toDraft").attr("disabled","");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
            return;
        }

        if($("#taskname").val()=="不作为短信内容发送")
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
				title:'加载进度..', 
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
			 $("form[name='form2']").attr("action","urld_previewSMS.htm?method=toDraft&lguserid="+$("#lguserid").val());
			$("form[name='" + formName + "']").submit();
		
	});
}

//点击预览时的方法
function dopreview()
{
	// 点击创建或存草稿按钮时
	$("input[id='subSend'],input[id='creSend']").click(function() {
		
		var contents = $("#contents").val();
		var urlcount = 0;
		var duplicateCount = 0;
		if(contents){
			var urllist = $("#urllist input");
			if(urllist != null && urllist.size()>0){
				for (var int = 0; int < urllist.length; int++) {
					var inputObj = urllist[int].value;
					if(contents.indexOf(inputObj) != -1){
						urlcount++;
						var tcount = 0;
						for(var i=0;i<urllist.length;i++){
							if(inputObj.indexOf(urllist[i].value) != -1){
								tcount ++;
							}
						}
						if(tcount > 1){
							duplicateCount++;
						}
						if(contents.substring(1+contents.indexOf(inputObj)).indexOf(inputObj) != -1){
							urlcount++;
						}
					}
				}
			}
		}
		if(urlcount>0){
			urlcount = urlcount - duplicateCount;
		}else{
			alert("没有插入短链接！");
			return;
		}
		if(urlcount>1){
			alert("只能插入一条短链接！");
			return;
		}
		
		$("#subSend").attr("disabled",true);
		$("#qingkong").attr("disabled",true);
		//标示判断是否有异常（具体针对内存溢出的异常）
		$("#error").val("0")
		$("form[name='" + formName + "']").find(
				"input[name='subState']").val(2);

		var formObj = document.forms[formName];
		if (formObj.busCode.value == "") {
			alert("没有可用的业务类型！");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			return;
		}
		if (formObj.spUser.value == "") {
			alert("没有可供发送的SP账号！");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			return;
		}
		//caizg
		var sendType = $(
				"form[name='" + formName + "']")
				.find("#sendType").val();
		var netUrlId = formObj.netUrlId.value;
		if(netUrlId==null || netUrlId=="" ){
			alert("没有插入短链接");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			return;
		}
		var domainId = formObj.domainId.value;
		if(domainId==null || domainId =="" ){
			alert("没有插入短链接");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			return;
		}
//		var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
//		var objExp= srcUrl.match(Expression);
//		var contents = $("#contents").val(); 
//		var objExp2 = contents.match(Expression);
//		
//		if(objExp==null){
//			alert("插入地址格式不对");
//			$("#subSend").attr("disabled","");
//			$("#qingkong").attr("disabled","");
//			return;
//		}
//		var text = srcUrl.replace(Expression,"");
//		if(text.length>0){
//			alert("插入信息包含地址以外内容");
//			$("#subSend").attr("disabled","");
//			$("#qingkong").attr("disabled","");
//			return;
//		}
//		if(objExp2 != null && objExp2.length>4 && sendType != 3){
//			alert("短信内容中包含多个地址");
//			$("#subSend").attr("disabled","");
//			$("#qingkong").attr("disabled","");
//			return;
//		}
//		if(contents.indexOf(srcUrl)<=-1 && sendType != 3){
//			alert("短信内容中没有插入活动地址地址");
//			$("#subSend").attr("disabled","");
//			$("#qingkong").attr("disabled","");
//			return;
//		}
//		
//		//活动地址中的&替换成|
//		formObj.srcUrl.value = srcUrl.replace(/\&/g,"|");
		var $texta = $("form[name='" + formName + "']").find("textarea[name='msg']");
        //处理短信内容前后空白字符并重新统计字数
        $texta.val($.trim($texta.val()));
        len($texta);
		var msgText=getContentVal();
		//alert("---");
		
		if (sendType != 3) {
			$texta.trigger("blur");
			$("#dtMsg").val(msgText);
		}

		if (formObj.isOk.value == 1
				&& sendType != 3) {
			if($.trim(msgText)=="")
			{
				alert("请输入短信发送内容！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}else{
				alert("请录入动态模板参数格式！  如:{#参数1#}");
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
				alert("没有可供发送的拓展尾号！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			var sendFlag = $("#sendFlag").val().split("&");
			if(sendFlag[0] == "false" && sendFlag[1] == "false" && sendFlag[2] == "false"){
				alert("移动、联通、电信主通道号+拓展尾号位数超过20位，不能发送！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[0] == "false" && sendFlag[1] == "false" ){
				alert("移动、联通主通道号+拓展尾号位数超过20位，不能发送！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[0] == "false" && sendFlag[2] == "false"){
				alert("移动、电信主通道号+拓展尾号位数超过20位，不能发送！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[1] == "false" && sendFlag[2] == "false"){
				alert("联通、电信主通道号+拓展尾号位数超过20位，不能发送！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[0] == "false" ){
				alert("移动主通道号+拓展尾号位数超过20位，不能发送！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[1] == "false"){
				alert("联通主通道号+拓展尾号位数超过20位，不能发送！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
			if(sendFlag[2] == "false"){
				alert("电信主通道号+拓展尾号位数超过20位，不能发送！");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return ;
			}
		}
		if (checkFile1()) {
			//关键字检查放到提交表单来处理
			//checkWords();
			//验证是否定时
			checkTimer()
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
		    alert("请选择上传的文件！");
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
    	    		alert("登录超时，请重新登录！");
    	    		return;
    	    	}
				if(message.indexOf("@")==-1){
					alert("关键字检验请求响应超时，请刷新页面或稍后重试！[EBFBV0280]");
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
					return;
				}
				message=message.substr(message.indexOf("@")+1);
				if (message != ""&&message!="error") {
					alert("发送内容包含如下违禁词组：\n     " + message + "\n请检查后重新输入。");
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
				} 
				else if (message == "error") {
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
					alert("检验关键字异常！[EBFBV0280]");
				} else {
					//alert("2");
					checkTimer();
					// $('form[name="'+formName+'"]').submit();
				}
			},
			error:function(xrq,textStatus,errorThrown){
				alert("关键字检验请求响应超时，请刷新页面或稍后重试！[EBFBV0280]");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				window.location.href='url_ssendDiffSMS.htm';
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
		alert("请填写定时时间！");
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
			if (date1 < date2.getTime()+20*60*1000) {
				alert("预发送时间需要大于服务器当前时间20分钟！请合理预定发送时间:[EBFV009]");
				$("#timerTime").val("");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
		//alert("3");
		//$("#form2").attr('action', "eq_sendDiffSMS.htm?method=add");
			$("#probar").dialog({
				modal:true,
				title:'加载进度..', 
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
					info:'不同内容群发，预览提交参数。userId:'+ $("#lguserid").val()+'，taskId:'+$("#taskId").val()+'，checkrepeat:'+checkrepeat
				});
			 $("form[name='form2']").attr("action","urld_previewSMS.htm?method=preview&checkrepeat="+checkrepeat+"&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val()+"&domainId="+$("#domainId").val());
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
					alert("异步请求时，响应超时，请刷新页面或稍后重试！[EBFBV0281]");
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
				alert("异步请求时，响应超时，请刷新页面或稍后重试！[EBFBV0281]");
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
			url:"url_ssendDiffSMS.htm",
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
					alert("获取模板失败！");
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
				alert("获取模板请求响应超时，请刷新页面或稍后重试！[EBFBV0281]");
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
			alert("文件不存在[EBFV0282]");
		else
			alert("号码文件检查请求响应超时，请刷新页面或稍后重试！[EBFBV0283]");
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
				alert("发送内容包含如下违禁词组：\n     " + data.substr(data.indexOf("[V10016]")+8) + "\n请检查后重新输入[EBFBV0280]");
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
					info:'不同内容预览，表单流上传失败。userId:'+ lguserid +'，lgcorpcode:'+ lgcorpcode
				});
			}
			alert("预览失败："+data.substr(5));
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		if(data=="error")
		{
			alert("预览异常，操作失败！[EBFV0284]");
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data=="overstep")
		{
			alert("文件内有效号码大于100万，系统不支持，请重新选择发送文件！[EBFV0285]");
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data=="overSize")
		{
			alert("上传文件过大，单次上传TXT文件或EXCEL文件最大支持"+MAX_SIZE+"兆，ZIP文件最大支持"+ZIP_SIZE+"兆，请重新选择发送文件！[EBFV0286]");
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data == "draftnotex")
		{
			alert("草稿箱不可用，可能已被删除，请重新选择！");
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data == "draftnotfile")
		{
			alert("草稿箱文件不存在，可能已被删除，请重新选择！");
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data == "drafterr")
		{
			alert("草稿箱无法使用，请重新选择！");
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		else if(data == "noShortUrl")
		{
			alert("暂时没有可用短地址，请稍后重试");
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		//$("#newtable").empty();
		$("#content").empty();
		var msgContent=getContentVal();
		if(data=="noPhone")
		{
			alert("文件内没有包含号码！[EBFV0287]");
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
			$("#hidDzUrl").val(array.urlFilePath);
			
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
		    
		    if(array.effCount-0==0)
		    {
		    	//$("#newtable").append("<tr align='center'><td style='border: 1px  solid; height: 25px;width:30px'>编号</td>" +
				//		"<td style='border: 1px  solid; height: 25px;'><center><div style='width:60px'>手机号码</div></center></td>" +
				//		"<td style='border: 1px  solid; height: 25px;'>短信内容</td></tr>" );
		    	$("#content").append("<thead><tr align='center'><th>编号</th>" +
						"<th><center><div style='width:89px'>手机号码</div></center></th>" +
						"<th>短信内容</th></tr></thead>" );
		    	$("#content").append("<tbody><tr><td colspan='3' align='center'>文件内没有有效的号码或短信内容格式不正确</td></tr></tbody>");
		    	$("#detail_Info").css("display","block");
	    		$("#detail_Info").dialog("open");
	    		deleteleftline1();	
		    }else
		    {   	
		    	
		    	//防止预览发出多次异步请求
		    	if(SendReq=="0"){
				    $("#SendReq").val("1");
				    var sendType=$("#sendType").val();
				    $("#content").load(
                        "url_urlSendDiffSMS.htm?method=readSmsContent&url="+array.viewFilePath+"&sendType="+sendType,
//				    	"url_ssendDiffSMS.htm?method=readSmsContent&url="+array.viewFilePath+"&sendType="+sendType,
//				    	"url_ssendDiffSMS.htm?method=readSmsContent",
//				    	{url:array.viewFilePath,sendType:sendType},
				    	function(){
				    		$("#detail_Info").css("display","block");
				    		$("#detail_Info").dialog("open");
				    		deleteleftline1();	
				    });
				    $(".ui-dialog-titlebar-close").hide();
		    	}
		    }
			
		}
	}
	
	function checkspye(yeresult)
	{
		var altstr = "";
		if(yeresult.indexOf("lessgwfee") == 0)
		{
			altstr = "运营商余额不足，当前余额："+yeresult.split("=")[1];
			 $("#btsend").attr("disabled","disabled");
		}else if(yeresult=="feefail" || yeresult=="nogwfee")
		{
			altstr = "获取运营商余额失败，不允许发送!";
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
			alert("预览失败，响应超时，请刷新页面或稍后重试！[EBFV0284]");
			$('#contents').val(getChangeVal());
			$("#subSend").attr("disabled",false);
            $("#qingkong").attr("disabled",false);
		}
	}
	
	function reloadPage(){
		var menuCode = base_menuCode;
		var url="";
		if (menuCode == "1050-1050") {
			url="url_urlSendDiffSMS.htm";
		}else{
			url="url_urlSendDiffSMS.htm";
		}
        //var url = "url_ssendDiffSMS.htm";
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
				alert("上传文件格式错误，请选择txt或zip格式的文件。");
				var file = $(":file");   
        		file.after(file.clone().val(""));   
        		file.remove(); 
        		return false;
			} else {
				return true;
			}
		}
		else {
			alert("请选择上传的文件！");
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
            	alert("该文件已经存在！");
		    	fileObj.after(fileObj.clone().val(""));   
		    	fileObj.remove(); 
        		return false;
            }
		    
			if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar"&& fileType != "csv") {
				alert("上传文件格式错误，请选择txt、zip、rar、xls、xlsx、csv或et格式的文件。");
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
			alert("请选择上传的文件！");
			return false;
		}
	}
	//删除附件
	function delfile(filename)
	{	
		if(confirm( "确认要删除吗? "))
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
			alert("选择的手机号码数超过100万，请选择100万以下的手机号码");
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
			alert("模板内参数错误，请查看上传模板！");
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
			alert("参数格式不匹配，请匹配后再发！");
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
			+lguserid+"&lgcorpcode="+lgcorpcode+"&timee="+new Date().getTime();
		$("#tempFrame").attr("src",frameSrc);
        if(isFrame4_0()) {
            $("#tempDiv").dialog({width:950,height:560});
        } else {
            $("#tempDiv").dialog({width:620,height:460});
        }
		$("#tempDiv").dialog("open");
		
	}

    function isFrame4_0() {
        var skin = $("#skin").val();
        if(skin.indexOf('frame4.0') != -1) {
            return true;
        } else {
            return false;
        }
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
			alert("未选择短信模板！");
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
			$('.para_cg').hide().html('关闭参数');
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
			width: 650,
			resizable:false,
			modal: true,
			open:function(){
				
			},
			close:function(){
				$("#addSmsTmpDiv").css("display","none");
				$("#addSmsTmpFrame").css("display","none");
			}
		});
		//新增
	    $("#addSmsTmpFrame").attr("src","tem_smsTemplate.htm?method=doAdd&fromState=1&lguserid="
	    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType="+showType+"&time="+new Date().getTime());
	          
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
					info:'不同内容暂存草稿，表单流上传失败。userId:'+ lguserid +'，lgcorpcode:'+ lgcorpcode
				});
			}
			alert("暂存草稿失败："+data.substr(5));
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		if(data=="error")
		{
			alert("暂存草稿异常，操作失败！[EBFV0284]");
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="overstep")
		{
			alert("文件内号码大于100万，系统不支持，请重新选择发送文件！[EBFV0285]");
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="overSize")
		{
			alert("上传文件过大，单次上传TXT文件或EXCEL文件最大支持"+MAX_SIZE+"兆，ZIP文件最大支持"+ZIP_SIZE+"兆，请重新选择发送文件！[EBFV0286]");
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="noPhone")
		{
			alert("文件内没有包含号码！[EBFV0287]");
			//不隐藏按钮
			disabledButton(false);
			return;
		}
		else if(data=="draftnotex")
		{
			alert("当前草稿箱不可用，可能已被删除，请重新选择！");
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
	        alert('暂存草稿成功！')
	    }else{
	        alert('暂存草稿失败！');
	    }
	}
	
	//显示按钮，false为显示
	function disabledButton(isShow)
	{
		$("#toDraft").attr("disabled",isShow);
		$("#subSend").attr("disabled",isShow);
		$("#qingkong").attr("disabled",isShow);
	}
