var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var contentLen = 990;
var baseContentLen = 990;

function hideSelect()
{
	
	$("#priority").css("display","none");
	$("#busCode").css("display","none");
	$("#spUser").css("display","none");
	$("#sendType").css("display","none");
	$("#tempSelect").css("display","none");
}

function showSelect()
{
	$("#priority").css("display","inline");
	$("#busCode").css("display","inline");
	$("#spUser").css("display","inline");
	$("#sendType").css("display","inline");
	$("#tempSelect").css("display","inline");
}

function zhezhao(){
    var width = "100%";
	var height = "100%";
	//setzhezhao("true");
	qdzz();
	var newDiv = document.createElement("div");
	newDiv.id = "loading2";
	newDiv.style.position = "fixed";
	newDiv.style.zIndex = "100";
	newDiv.style.width = width;
	newDiv.style.height = height;
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.background = "#999";
	newDiv.style.filter = "alpha(opacity=60)";
	newDiv.style.opacity = 0.6;
	newDiv.innerHTML = '<iframe style="border:0;position:absolute;top:0;left:0;width:99%;height:99%;filter:alpha(opacity=0);"></iframe>'
	if(isIE6){
		newDiv.style.position = "absolute";
		newDiv.style.width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth) + "px";
		newDiv.style.height = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) + "px";
	}
	document.body.appendChild(newDiv);
	newDiv.style.display="block";
}

function qdzz()
{
	if (null != document.getElementById("loading2"))
	{
		document.body.removeChild(document.getElementById("loading2"));
	}
}
//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
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
// 统计短信内容字数
function len(ob) {
	//统计短信内容字数 增加对贴尾的计算
    var tailcontents = "";
    if($('#tail-area').is(':visible')){
        tailcontents = $('#tailcontents').val()||'';
    }
    var smsContent = $.trim(ob.val());
    //贴尾内容+短信内容，贴尾内容放在前面便于超长短信内容截取
	var content = tailcontents + smsContent;
	var huiche = content.length - content.replaceAll("\n", "").length;
	if(ob.is("#smsContent")){
		if(content.length + huiche > contentLen)
		{
			$("#smsContent").val(content.substring(tailcontents.length,contentLen-huiche));
			//短信内容
			smsContent = $("#smsContent").val();
		    //贴尾内容+短信内容
	 		content = tailcontents + smsContent;
			huiche = content.length - content.replaceAll("\n", "").length;
		}
	}
	var len = contentLen;
	countSMS($("#smsContent").val().length + huiche, tailcontents, content);
	if(len != contentLen)
	{
	    //贴尾内容+短信内容
 		content = tailcontents + $("#smsContent").val();
		//贴尾内容长度+短信内容长度大于短信最大长度
		if(content.length + huiche > contentLen)
		{
			$("#smsContent").val(content.substring(tailcontents.length,contentLen-huiche));
			smsContent = $("#smsContent").val();
			//贴尾内容+短信内容
	 		content = tailcontents + smsContent;
			huiche = content.length - content.replaceAll("\n", "").length;
		}
		$('#maxLen').html("/"+contentLen);
	}
	$('form[name="' + formName + '"]').find(' #strlen').html(($("#smsContent").val().length + huiche + tailcontents.length));
}

//根据账户获取账户绑定的路由信息拆分规则
function setGtInfo(spUser)
{
	$.post("ll_comm.htm?method=getSpGateConfig&spUser="+spUser,{},
		function(infoStr){
			if(infoStr !="error" && infoStr.startWith("infos:"))
			{
				var infos = infoStr.replace("infos:","").split("&");
				$("#gt1").val(infos[0]);
				$("#gt2").val(infos[1]);
				$("#gt3").val(infos[2]);
				$("#gt4").val(infos[3]);
				contentLen = infos[4];
				baseContentLen = contentLen;
				$('#maxLen').html("/"+contentLen);
			}
//			len($("#smsContent"));
		}
	);
}

// 统计条数-短信拆分规则js版
function countTS(s) {
	
	var len ;
	var maxLen;
	var totalLen;
	var lastLen;
	var signLen;
	var count;
	for(var i=1;i<4;i=i+1)
	{
		count = 0;
		if(s>0)
		{
			var gtinfo = $("#gt"+i).val();
			if(gtinfo != "")
			{
				var gtinfos = gtinfo.split(",");
				
				maxLen = gtinfos[0];
				totalLen = gtinfos[1];
				lastLen = gtinfos[2];
				signLen = gtinfos[3];
				//alert("maxLen:"+maxLen);
				//alert("totalLen:"+totalLen);
				//alert("lastLen:"+lastLen);
				//alert("signLen:"+signLen);
				//if (s <= maxLen - signLen)
				//{
					len = s*2;
					if (len <= (totalLen - signLen + 3)*2)
						count = 1;
					else
						count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
				//}
			}
		}
		$("#ft"+i).text(count);
	}
}
// 统计条数-短信拆分规则js版(支持英文短信)
function countSMS(s, tailcontents, content) {
	var len ;
	var enLen;
	var reset;
	var maxLen;
	var totalLen;
	var lastLen;
	var signLen;
	var enmaxLen;
	var entotalLen;
	var enlastLen;
	var ensignLen;
	var gateprivilege;
	var ensinglelen;
	var count;
	var enMsgShortLen;
	var signLocation;
	var longSmsFirstLen;
	//短信内容是否为中文短信,false:英文;true:中文
	var isChinese = false;
	//短信内容
	var smsContent;
	//回车换行的个数
	var huiche;
	//短信长度+贴尾长度
	s += tailcontents.length;
	for(var i=1;i<5;i=i+1)
	{
		count = 0;
		if(s>0)
		{
			enLen = 0;
			enMsgShortLen = 0;
			reset = false;
			var gtinfo = $("#gt"+i).val();
			if(gtinfo != "")
			{
				var gtinfos = gtinfo.split(",");
				
				maxLen = gtinfos[0];
				totalLen = gtinfos[1];
				lastLen = gtinfos[2];
				signLen = gtinfos[3];
				enmaxLen = gtinfos[4];
				entotalLen = gtinfos[5];
				enlastLen = gtinfos[6];
				ensignLen = gtinfos[7];
				gateprivilege = gtinfos[8];
				ensinglelen = gtinfos[9];
				signLocation = gtinfos[10];
				//签名前置
				if(signLocation == 1)
				{
					longSmsFirstLen = entotalLen - ensignLen;
				}
				else
				{
					longSmsFirstLen = entotalLen;
				}
				//支持英文短信
				if(!isChinese && gateprivilege == 1){
					//字符ASCII码
					var charAscii;
					//是否中文短信
					for(var j=0;j<content.length;j++)
					{
						enLen += 1;
						enMsgShortLen += 1;
						charAscii = content.charAt(j).charCodeAt();
						if(charAscii > 127)
						{
							isChinese = true;
						}
						for(var k=0; k<special.length; k++)
						{
							if(special[k] == charAscii)
							{
								//长短信边界值
								if(enLen % longSmsFirstLen == 0)
								{
									//条数加2
									enLen += 2;
								}
								else
								{
									enLen += 1;
								}
								enMsgShortLen += 1;
								break;
							}
						}
						
						//英文短信长度超过最大短信长度
						if(enLen > contentLen)
						{
							$("#smsContent").val(content.substring(tailcontents.length,j-huiche));
							smsContent = $("#smsContent").val();
							//贴尾内容+短信内容
							content = tailcontents + smsContent;
							huiche = content.length - content.replaceAll("\n", "").length;
							s = huiche + content.length;
							//重新计算条数
							reset = true;
							break;
						}
						if(isChinese)
						{
							break;
						}
					}
					//重新计算条数,重新遍历
					if(reset)
					{
						i = 0;
						continue;
					}
					//如果为短短信
					if(enMsgShortLen <= (ensinglelen - ensignLen))
					{
						enLen = enMsgShortLen;
					}
				}
				//短信内容为英文并且支持英文短信，使用英文拆分规则
				if(!isChinese && gateprivilege == 1){
					//国内通道英文短信,特殊字符按1个计算
					if(i != 4)
					{
						enLen = s;
					}
					//条数计算
					if (enLen <= (ensinglelen - ensignLen)){
						count = 1;
					}
					else{
						count = 1 + Math.floor((enLen - enlastLen +  parseInt(entotalLen) -1) / entotalLen);
					}
				}
				//中文短信
				else{
					//处理模板导入,短信内容条数计算
					if(isChinese && contentLen == 700)
					{
						contentLen = 350;
						if(s > contentLen)
						{
							huiche = content.length - content.replaceAll("\n", "").length;
							$("#smsContent").val(content.substring(tailcontents.length,350-huiche));
							smsContent = $("#smsContent").val();
							//贴尾内容+短信内容
							content = tailcontents + smsContent;
							s = huiche + content.length;
							//重新计算条数
							i = 0;
							continue;
						}
					}
					len = s*2;
					if (len <= (totalLen - signLen + 3)*2)
						count = 1;
					else
						count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
				}
			}
		}
		$("#ft"+i).text(count);
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

// 失去焦点时判断
function eblur(ob) {
	var $parent = ob.parent();
	$parent.find(".add_error").remove();
	/*if (ob.is("#taskname")) {
		if ($.trim(ob.val()) == "") {
			var errorMsg = "&nbsp;请输入主题";
			$parent.append("<em class='add_error'>" + errorMsg + "</em>");
			document.forms[formName].isOk.value = 0;
		} else {
			document.forms[formName].isOk.value = 2;
		}
	}
	*/
	if (ob.is("#smsContent")) {
		ob.val($.trim(ob.val()));
		var content = $("#smsContent").val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > contentLen) {
			$("#smsContent").val(content.substring(0, contentLen - huiche));
		}
		if($("#tempSelect").val()=="")
		{
			$("#inputContent").val($("#smsContent").val());
		}
		len(ob);
		if ($.trim(ob.val()) == "") {
			document.forms[formName].isOk.value = 1;
		} else if (ob.val().length > contentLen) {
			document.forms[formName].isOk.value = 1;
		} else {
			document.forms[formName].isOk.value = 2;
		}
	}
}
// 手机号码输入控制
function limit() {
	// 只允许输入数字和';'
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$('#mobileUrl').keypress(
		function(e) {
			var iKeyCode = window.event ? e.keyCode : e.which;
			var vv = !(((iKeyCode >= 48) && (iKeyCode <= 57))
					|| (iKeyCode == 44) || (iKeyCode == 13)
					|| (iKeyCode == 46) || (iKeyCode == 45)
					|| (iKeyCode == 37) || (iKeyCode == 39) || (iKeyCode == 8));
			if (vv) {
				if (isIE) {
					event.returnValue = false;
				} else {
					e.preventDefault();
				}
			}
		}
	);
	// 控制不能由输入法输入其他字符
	$('#mobileUrl').keyup(function() {
		var str = $('#mobileUrl').val();
		// 只能输入0-9或者英文标点","符号、回车换行
		var reg = /[^0-9,\r\n]+/g;
		str = str.replace(reg, "");
		$('#mobileUrl').val(str);
	});
}

// 短信内容键盘事件监听统计字数
function synlen() {
	$("form").find("textarea[name='smsContent']").keyup(function() {
		var content = $(this).val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > contentLen) {
			$(this).val(content.substring(0, contentLen - huiche));
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

$(document).ready(
function() {
	$("#upNumFile").click(function() {
		$(this).parent().next("#upNum").show();
	});
	var curMsg = "";
	$("a[id='addModel'],a[id='addModel2'],a[id='addModel3']")
			.click(
	function() {
		var dsflag = 0;
		if ($(this).attr("id") == "addModel2") {
			dsflag = $(
					"form[name='" + formName
							+ "']").find(
					"#sendType").val() - 1;
		}
		if ($(this).attr("id") == "addModel3") {
			dsflag = 1;
		}
		var mod = $(this).parent().next(
				"#model");
		var tmpSel = mod.find("#tempSelect");
		var lguserid = $("#lguserid").val();
		if ((tmpSel.find("> option").length == 1 && tmpSel
				.val() == "")
				|| $(this).attr("id") == "addModel2") {
			tmpSel.empty();
			tmpSel.load("tem_smsTemplate.htm", {
				method : "getTmAsOption",
				lguserid:lguserid,
				dsflag : dsflag
			}, function() {
				// tmpSel.trigger("change");
				});
		} else {
			tmpSel.find("> option").eq(0).attr(
					"selected", "selected");
		}
		mod.toggle();
		var msgob = $(
				"form[name='" + formName + "']")
				.find("textarea[name='msg']");
		if (mod.is(":visible")) {
			tmpSel.css("display", "block");
			msgob.val(curMsg);
			curMsg = msgob.val();
			msgob.attr("readonly", "readonly");
			len(msgob);
		} else {
			tmpSel.css("display", "none");
			msgob.val(curMsg);
			curMsg = msgob.val();
			msgob.attr("readonly", "");
			len(msgob);
		}
	});
	$("#delUp").click(function() {
		if (navigator.appName == "Netscape") {
			$("#numFile").attr("value", "");
		} else {
			document.getElementById("numFile").select();
			document.selection.clear();
		}
		$("#upNum").toggle();
	});

	synlen();
	limit();
	setFormName($("#formName").val());
	$("form[name='" + formName + "']").find(
			"input[name='reInput']").click(function() {
		reInputs($(this));
	});
	// 点击创建或存草稿按钮时
	$("input[id='subSend'],input[id='creSend']").click(
			function() {
				$("#subSend").attr("disabled",true);
				$("#qingkong").attr("disabled",true);
				
				$("#phoneStr").val($("#phoneStr1").val()+$("#phoneStr2").val()+$("#inputphone").val());
				//判断时间----------------------------------------
				var sendTime = $('#timerTime').val();
				var serverTime ;
				$.post("common.htm?method=getServerTime", {
					
				}, function(msg) {
					serverTime = msg;
					var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
					var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                    date1.setMinutes(date1.getMinutes() - 1, 0, 0);
					if (date1 <= date2) {
						alert(getJsLocaleMessage("qyll","qyll_lldg_alter_1"));
						$("#timerTime").val("");
						$("#subSend").attr("disabled","");
						$("#qingkong").attr("disabled","");
						return;
					}
					else
					{
						//第一步：判断是否选择套餐（至少选择一种）
						var hasSelect = false;
						var ydtc = $("#yd_select").val();
						var lttc = $("#lt_select").val();
						var dxtc = $("#dx_select").val();
						if(ydtc != '-1:-1'){
							hasSelect = true;
						}
						if(lttc != '-1:-1'){
							hasSelect = true;
						}
						if(dxtc != '-1:-1'){
							hasSelect = true;
						}
						if(!hasSelect){
							alert(getJsLocaleMessage("qyll","qyll_lldg_alter_2"));
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							return;
						}
						
						//判断是否需要短信提醒
						var smsContent = $.trim($("#smsContent").val());
						if(smsContent != getJsLocaleMessage("qyll","qyll_lldg_alter_3")){
							//需要短信提醒，判断是否有可发送的sp账号
							if($("#sp_User").val() == ""){
								alert(getJsLocaleMessage("qyll","qyll_lldg_alter_4"));
								$("#subSend").attr("disabled","");
								$("#qingkong").attr("disabled","");
								return ;
							} 
						}else{
							$("#smsContent").val("");
						}
				        $("form[name='form2']").attr("action",$("form[name='form2']").attr("action")+"&lguserid="+$("#lguserid").val());
			        	$("form[name='form2']").submit();
					}
				});
				//----------------------------------------------------
			});
	
	// 点击暂存草稿按钮时

	//查看
	$("input[id='shows']").click(
			function(){
				
				

				var formObj = document.forms["form2"];
				if (formObj.busCode.value == "") {
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_5"));
					return;
				}
				if (formObj.spUser.value == "") {
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_6"));
					return;
				}
				
				if(re()){
					
					$("#form2").attr("action", "ssm_sendBatchSMS.htm?method=adds");
					
					$("#form2").submit();
				}
				
			});
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
	

function checkTxt() {
	var mobile = $("#mobileUrl").val();
	if (mobile != "") {
		/*
		 * if (/^(\d|;|\n)*$/.test(mobile)) { $.post("sms_sendSMS.htm", { mobile :
		 * mobile, separator : ";", method: "checkMoblieLegal" },
		 * function(result) { if (result == 0) {
		 * alert("录入手机号码包含非法号码，请确认后重新录入。"); return false; } else { checkWords(); }
		 * }); } else { alert("录入手机号码包含非允许字符，请确认后重新录入。"); return false; }
		 */
		$.post("mms_sendMMS.htm?method=checkMobile", {
			receiver : mobile
		}, function(result) {
			if (result != null && result != "") {
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_7")+"[EXFV004]：" + result);
				return false;
			} else {
				checkWords();
			}
		});
	} else {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_8"));
		// return checkWords();
	}
}
/*/ 验证上传文件格式
function checkFile() {
	var fileObj = $("form[name='" + formName + "']").find(
			"input[name='numFile']");
	if (fileObj.val() != "") {
		var fileNmae = fileObj.val();
		var index = fileNmae.lastIndexOf(".");
		fileNmae = fileNmae.substring(index + 1).toLowerCase();
		if (fileNmae != "txt" && fileNmae != "txt") {
			alert("上传文件格式错误，请选择txt格式的文件。");
			return false;
		} else {
			return true;
		}
	}
	else {
		alert("请选择上传的文件！");
		return false;
	}
}*/

// 验证短信内容是否包含关键字
function checkWords() {
	
	var msg = $("form[name='" + formName + "']").find("textarea[name='msg']")
			.val();
	$.ajax({
		url: "common.htm?method=checkBadWord1", 
		type : "POST",
		data: {tmMsg : msg,corpCode : $("#lgcorpcode").val()},
		success: function(message) {
			//if(message=="")
			//{
				//alert("网络或服务器无法连接，请稍后重试！");
				//window.location.href='ssm_sendBatchSMS.htm';
				//return;
			//}
			if(message.indexOf("@")==-1){
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_9")+"[EXFV005]");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
			message=message.substr(message.indexOf("@")+1);
			if (message != "" && message !="error") {
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_10")+"：\n     " + message + "\n"+getJsLocaleMessage("qyll","qyll_lldg_alter_11")+"[EXFV006]");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
			} else if (message == "error") {
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_12")+"[EXFV007]");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
			} else {
				//alert("2");
				checkTimer();
				// $('form[name="'+formName+'"]').submit();
			}
		},
		error:function(xrq,textStatus,errorThrown){
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_9")+"关键字检验请求响应超时，请刷新页面或稍后重试！[EXFV008]");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			window.location.href='ssm_sendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
//	 $.ajax({
//	        url:"sms_sendSMS.htm",
//			type:"post",
//			data:{
//		 	method : "checkBadWord",
//			tmMsg : msg
//			}
//	    	,error:function(data, textStatus, XMLHttpRequest) {
//	    		if("error")
//	    	if (data != "") {
//				alert("短信模板内容包含如下违禁词组：\n     " + message + "\n请检查后重新输入。");
//			} else if (data == "error") {
//				alert("过滤关键字失败！");
//			} else {
//				//alert("2");
//				checkTimer();
//				// $('form[name="'+formName+'"]').submit();
//			}
//	    	}});
}

// 验证是否定时
function checkTimer() {
	var $timer = $("form[name='" + formName + "']").find(
			"input:checked[name='timerStatus']");
	var $timerTime = $("form[name='" + formName + "']").find(
			"input[name='timerTime']");
	if ($timer.val() == 1 && $timerTime.val() == "") {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_13"));
		$("#subSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
	} else {
		if($("#vss").find(" tr").length<2)
        {
        	alert(getJsLocaleMessage("qyll","qyll_lldg_alter_14"));
        	$("#subSend").attr("disabled","");
    		$("#qingkong").attr("disabled","");
        }else
        {
	        $("#probar").dialog({
			modal:true,
			title:getJsLocaleMessage("qyll","qyll_lldg_alter_15"), 
			height:80,
			resizable :false,
			closeOnEscape: false,
			width:200,
			open: function(){
			$(".ui-dialog-titlebar").hide();
			errorNum=0;
//			dd = window.setInterval("fresh()",3000);
			//$("body").css("background-color","gray");
	          }
			});
	       //alert($("#numFile").val());
	        $.post('common.htm?method=frontLog',{
					info:'相同内容群发，预览提交参数。userId:'+ $("#lguserid").val()+'，taskId:'+$("#taskId").val()
				});
	        $("form[name='form2']").attr("action",$("form[name='form2']").attr("action")+"&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val());
        	$("form[name='form2']").submit();
        }
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
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_17"));
					window.clearInterval(dd);
					//window.location.href='ssm_sendBatchSMS.htm';
					window.location.href='ssm_sendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			
			if(errorNum==0)
			{
				errorNum=1;
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_17"));
			}
			window.clearInterval(dd);
			//window.location.href='ssm_sendBatchSMS.htm';
			window.location.href='ssm_sendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
}

// 获取表格的值

// 获取短信模板内容
function getTempMsg(tmpOb) {
	var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
	if (tmpOb.val() != "") {
		$.ajax({
			url:"common.htm?method=getTmMsg1",
			data:{tmId : tmpOb.val()},
			type:"post",
			success:function(result){
				if(result.indexOf("@")==-1){
					//alert("网络或服务器无法连接，请刷新页面或稍后重试[EXFV005]");
					//window.location.href='ssm_sendBatchSMS.htm';
					window.location.href='ssm_sendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
					return;
				}
				result=result.substr(result.indexOf("@")+1);
				if(result=="error")
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_18"));
					return;
				}
				msgob.val(result);
				msgob.attr("readonly","readonly");
				msgob.css("background","#E8E8E8");
				msgob.bind("focus",function(){
					this.blur();
				});
				len(msgob);
			},
			error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_9"));
				//window.location.href='ssm_sendBatchSMS.htm';
				window.location.href='ssm_sendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
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

// 获取短信模板内容
function getSql(sqlId) {
	var $sqlContent = $("form[name='" + formName + "']").find(
			"textarea[name='sql']");
	if (sqlId != "") {
		$.post("tem_smsTemplate.htm", {
			method : "getTmMsg",
			tmId : sqlId
		}, function(msg) {
			$sqlContent.val(msg);
		});
	} else {
		$sqlContent.val("");
	}
}

function openTemp(r) {
	var path = $("#cpath").val();
	window.open(path + "/fileUpload/fileDownload/" + r, "_blank");
}
//当前时间与服务器时间对比
function checkServerTime(){
	
	//------------------------------------guodw
	var sendTime = $('#timerTime').val();
	var serverTime ;
	$.post("common.htm?method=getServerTime", {
	
	}, function(msg) {
		serverTime = msg;
		var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
		var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
        date1.setMinutes(date1.getMinutes() - 1, 0, 0);
		if (date1 <= date2) {
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_1"));
			$("#timerTime").val("");
			return;
		}
	});
	
	//----------------------------------------
}

// 查看号码文件是否存在
function checkFileIsVisible(url, src) {
	$.post(src + "/mms_mmsSendBox.htm?method=checkFiles", {
		url : url
	}, function(result) {
		if (result == "true") {
			window.showModalDialog(src + "/" + url);
		} else if (result == "false")
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_20"));
		else
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_21"));
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

//提交前先判断是否有号码存在列表中
function re(){
	//提交前先遍历表格，看看是否有手机号码或者文件存在
	//取出表格中的数据
	 var hen=new Array();
     //合法
        var trs= $("#vss").children().children();           
        $.each(trs,function(i,n){
	       var tds=$(n).children();
	       $.each(tds,function(j,d){
	             if(i!=0&&j==1)
	              {
	                 var ps=$(d).text();
	                 hen[i]=ps;
	              }
	        }); 
	     });
      if(hen.length>0){
    	  return true;
      }else{
    	  alert(getJsLocaleMessage("qyll","qyll_lldg_alter_22"));
    	//  alert("false");
    	  return false;
      }
	
}


function resets(){
	var ie = (navigator.appVersion.indexOf("MSIE")!=-1);//IE         
	if(ie){   
	            $("#numFile").select();   
	             document.execCommand("delete");   
	       }else{   
	              $("#numFile").val("");    
	      }  
	$('form[name="' + formName + '"]').find(' #ft').html((0));
		$("#taskname").attr("value","");
		$("#smsContent").attr("value","");
		$("#sendNow").attr("checked","checked");
	//1清除列表中的数据
	var trs= $("#vss").children().children();           
        $.each(trs,function(i,n){
	      if(i>0){
	        $(n).remove();
	       }
	 });
     $('form[name="' + formName + '"]').find(' #strlen').html((0));
	 //清队查模板
	 $("#model").css("display","none");
	 $('#contents').attr('readonly',false);
	// $("#smsContent").attr("disabled","");
	  $("#time2").css("display","none");
}


//预览窗口
function preSend(data){
	if(data.indexOf("empex") == 0){
		//关键字检查
		if(data.indexOf("V10016") >= 0){
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_10")+"：\n     " + data.substr(data.indexOf("[V10016]")+8) + "\n"+getJsLocaleMessage("qyll","qyll_lldg_alter_21")+"[EXFV006]");
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		//表单流上传失败
		if(data.indexOf("V10003") >= 0){
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $("#lgcorpcode").val();
			$.post('common.htm?method=frontLog',{
				info:'相同内容预览，表单流上传失败。userId:'+ lguserid +'，lgcorpcode:'+ lgcorpcode
			});
		}
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_24")+"："+data.substr(5));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	
	if(data == "noPhoneOrFile"){
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_144"));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	
	if(data=="noPhone"){
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_25")+"[EXFV016]");
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	
	var array=JSON.parse(data);
	
	//流量运营商余额不足
	if(array.llBalance - array.flowSumPrice < 0){
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_26"));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	//定时时间
	$("#hidTimerTime").val(array.timerTime);
	
	//有效号码
	$("#effs").text(array.effCount);
	$("#hidEffCount").val(array.effCount);
	//提交号码
	$("#counts").text(array.subCount);
	$("#hidSubCount").val(array.subCount);
	//无效号码数
	$("#alleff").text(array.subCount - array.effCount);
	
	//黑名单
	$("#blacks").text(array.blackCount);
	//重复
	$("#sames").text(array.repeatCount);
	//格式非法
	$("#legers").text(array.badModeCount);
	//无套餐号码
	$("#noFlowPhone").text(array.noFlowPhone);
	//流量消费金额：	流量运营商余额
	$("#allprice").text(array.flowSumPrice);
	$("#hideSumPrice").val(array.flowSumPrice);
	
	$("#leftprice").text(array.llBalance);
	//短信发送条数
	if(array.needSMS == true){
		$("#smsCount").text(array.effCount);
	}else{
		$("#smsCount").text(0);
	}
	//设置短信预览内容
	$("#smsContentyl").val($("#smsContent").val());
	$("#hidemsg").val($("#smsContent").val());
	//taskId
	$("#taskId").val(array.taskId);
	//url
	$("#hidMobileUrl").val(array.validFilePath);
	//错误号码url
	$("#badurl").val(array.viewFilePath);

	$('#dgxx tr:first td:gt(0)').remove();
	if(typeof(array.depFeeCount) !="undefined" &&  array.depFeeCount != -1 ){
		$('#dgxx tr:first').append('<td align="left">'+getJsLocaleMessage("qyll","qyll_lldg_alter_27")+'<span id="depFeeCount">'+array.depFeeCount+'</span> </td>');
	}
	
	if(typeof(array.spUserFeeCount) !="undefined" &&  array.spUserFeeCount != -1 ){
		$('#dgxx tr:first').append('<td align="left">'+getJsLocaleMessage("qyll","qyll_lldg_alter_145")+'<span id="spUserFeeCount">'+array.spUserFeeCount+'</span> </td>');

	}

	if(array.spUserFeeCount !=-1 && eval(array.spUserFeeCount) < eval(array.effCount)){
		$("#messages2 font").text(getJsLocaleMessage("qyll","qyll_lldg_alter_29"));
		$("#btsend").attr("disabled", "disabled");
	}else if(array.depFeeCount != -1 && eval(array.depFeeCount) < eval(array.effCount)){
		$("#messages2 font").text(getJsLocaleMessage("qyll","qyll_lldg_alter_113"));
		$("#btsend").attr("disabled", "disabled");
	}else{
		$("#messages1").hide();
	}
	//所选套餐IDs
	$("#hideProIds").val(array.proIds);
	
	var productIdStrs = array.productIdStrs.split(":");
	var flowNames = array.flowNames.split(":");
	var oprValidPhone = array.oprValidPhone.split(":");
	var tr = '';
	var count = 1;
	$('#content tr:gt(0)').remove();
	//移动
	if(productIdStrs[0] != '-1'){
		tr = '<tr>'+
				'<td width="35" height="30" class="div_bd" style="border-left:0px;text-align:center;">'+count+'</td>'+
				'<td width="50" class="div_bd" style="border-left:0px;text-align:center;">'+productIdStrs[0]+'</td>'+
				'<td width="135" class="div_bd" style="border-left:0px;text-align:center;">'+flowNames[0]+'</td>'+
				'<td width="80" class="div_bd" style="border-left:0px;text-align:center;">'+getJsLocaleMessage("qyll","qyll_lldg_alter_30")+'</td>'+
				'<td width="70" class="div_bd" style="border-left:0px;text-align:center;">'+oprValidPhone[0]+'</td>'+
			  '</tr>'
		$("#content").append(tr);	
		count++;
	}
	//联通
	if(productIdStrs[1] != '-1'){
		tr = '<tr>'+
				'<td width="35" height="30" class="div_bd" style="border-left:0px;text-align:center;">'+count+'</td>'+
				'<td width="50" class="div_bd" style="border-left:0px;text-align:center;">'+productIdStrs[1]+'</td>'+
				'<td width="135" class="div_bd" style="border-left:0px;text-align:center;">'+flowNames[1]+'</td>'+
				'<td width="80" class="div_bd " style="border-left:0px;text-align:center;">'+getJsLocaleMessage("qyll","qyll_lldg_alter_31")+'</td>'+
				'<td width="70" class="div_bd " style="border-left:0px;text-align:center;">'+oprValidPhone[1]+'</td>'+
			  '</tr>'
		$("#content").append(tr);	
		count++;
	}
	//电信
	if(productIdStrs[2] != '-1'){
		tr = '<tr>'+
				'<td width="35" height="30" class="div_bd" style="border-left:0px;text-align:center;">'+count+'</td>'+
				'<td width="50" class="div_bd" style="border-left:0px;text-align:center;">'+productIdStrs[2]+'</td>'+
				'<td width="135" class="div_bd" style="border-left:0px;text-align:center;">'+flowNames[2]+'</td>'+
				'<td width="80" class="div_bd" style="border-left:0px;text-align:center;">'+getJsLocaleMessage("qyll","qyll_lldg_alter_32")+'</td>'+
				'<td width="70" class="div_bd" style="border-left:0px;text-align:center;">'+oprValidPhone[2]+'</td>'+
			  '</tr>'
		$("#content").append(tr);		
	}
	
	 $("#detail_Info").dialog("open");
}
function checkspye(yeresult)
{
	var altstr = "";
	if(yeresult.indexOf("lessgwfee") == 0)
	{
		altstr = getJsLocaleMessage("qyll","qyll_lldg_alter_33")+yeresult.split("=")[1];
		 $("#btsend").attr("disabled","disabled");
	}else if(yeresult=="feefail" || yeresult=="nogwfee")
	{
		altstr =getJsLocaleMessage("qyll","qyll_lldg_alter_34");
		 $("#btsend").attr("disabled","disabled");
	}
	$("#messages2 font").text(altstr);
}
function selectWorkers(){
	$("#yugong").css("display","block");
    $("#yugong").dialog("open");
}
function checkError()
{
	if($("#error").val()=="0")
	{
//		window.clearInterval(dd);
		$("#probar").dialog("close");
		alert("预览异常，操作失败。[EXFV017]");
		$("#subSend").attr("disabled",false);
        $("#qingkong").attr("disabled",false);
	}
}


function chooseTemp()
{
	var tpath = $("#cpath").val();
	var frameSrc = $("#tempFrame").attr("src");
//	var lguserid = $("#lguserid").val();
//	var lgcorpcode = $("#lgcorpcode").val();
	var lguserid=GlobalVars.lguserid;
	var lgcorpcode=GlobalVars.lgcorpcode;
	frameSrc = tpath+"/common.htm?method=getLfTemplateBySms&dsflag=0&lguserid="
		+lguserid+"&lgcorpcode="+lgcorpcode+"&timee="+new Date().getTime();
	$("#tempFrame").attr("src",frameSrc);
	$("#tempDiv").dialog({width:620,height:460});
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
		alert("未选择短信模板！");
		return;
	}
	else
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
	len(msgob);
	$("#tempDiv").dialog("close");
	$("#depSign").attr("checked",false);
	$("#nameSign").attr("checked",false);
}

function tempNo()
{
	var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
	initContents();
	len(msgob);
	$("#depSign").attr("checked",false);
	$("#nameSign").attr("checked",false);
	$("#qtem").hide();
	$("#ctem").show();
}
var inval;
var mcode;
function openTemDiv(menuCode)
{
	mcode = menuCode;
	var tpath = $("#cpath").val();
	var url = tpath+"/tem_smsTemplate.htm?opentm=0&lguserid="+$("#lguserid").val()
		+"&lgcorpcode="+$("#lgcorpcode").val();
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

/**
 * 保存草稿回调
 */
function saveDraft(data)
{
	//换回预览的servlet
	$("form[name='form2']").attr('action', "ssm_previewSMS.htm?method=preview&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val());
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
				info:'相同内容暂存草稿，表单流上传失败。userId:'+ lguserid +'，lgcorpcode:'+ lgcorpcode
			});
		}
		alert("暂存草稿失败："+data.substr(5));
		//不隐藏按钮
		disabledButton(false);
		return;
	}
	if(data=="error")
	{
		alert("暂存草稿异常，操作失败。[EXFV014]");
		//不隐藏按钮
		disabledButton(false);
		return;
	}
	else if(data=="overstep")
	{
		alert("文件内号码大于"+MAX_PHONE_NUM+"万，系统不支持，请重新选择发送文件！[EXFV015]");
		//不隐藏按钮
		disabledButton(false);
		return;
	}
	else if(data=="overSize")
	{
		alert("上传文件过大，单次上传TXT文件或EXCEL文件最大支持"+MAX_SIZE+"兆，ZIP文件最大支持"+ZIP_SIZE+"兆，请重新选择发送文件！[EXFV018]");
		//不隐藏按钮
		disabledButton(false);
		return;
	}
	else if(data=="noPhone")
	{
		alert("没有可发送的有效号码！[EXFV016]");
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
