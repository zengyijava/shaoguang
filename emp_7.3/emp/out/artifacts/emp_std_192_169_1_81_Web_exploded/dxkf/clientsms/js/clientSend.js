var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var contentLen = 980;
var baseContentLen = 980;
// 获取1到n-1条英文短信内容的长度
var entotalLen = 153;
var skin = $("#skin").val();
function hideSelect()
{
	$("#busCode").css("display","none");
	$("#spUser").css("display","none");
	$("#sendType").css("display","none");
	$("#tempSelect").css("display","none");
	$("#groupSelect").css("display","none");
	$("#priority").css("display","none");
}

function showSelect()
{
	$("#busCode").css("display","inline");
	$("#spUser").css("display","inline");
	$("#sendType").css("display","inline");
	$("#tempSelect").css("display","inline");
	$("#groupSelect").css("display","inline");
	$("#priority").css("display","inline");
}

//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
// 统计短信内容字数
function len(ob) {
    //统计短信内容字数 增加对贴尾的计算
    var tailcontents = "";
    if($('#tail-area').is(':visible')){
        tailcontents = $.trim($('#tailcontents').text())||'';
    }
    //将贴尾内容放置短信内容前面 方便处理
	var content = tailcontents + $.trim(ob.val());
	if(baseContentLen == 700)
	{
		setSmsContentMaxLen(content);
	}
    //是否对短信内容进行过截取
    var isSub = false;
    while(content.replaceAll("\n","**").length > contentLen){
        content = content.substring(0,content.length-1);
        isSub = true;
    }
    var len = content.replaceAll("\n","**").length;
    if(isSub){
        $("#contents").val(content.substring(tailcontents.length,content.length));
    }
    $('#maxLen').html("/"+contentLen);
	$('form[name="' + formName + '"]').find(' #strlen').html(
			len);
};

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
	}*/
	if (ob.is("#contents")) {
		ob.val($.trim(ob.val()));
		//var content = $("#contents").val();
		//var huiche = content.length - content.replaceAll("\n", "").length;
		//if (content.length + huiche > contentLen) {
		//	$("#contents").val(content.substring(0, contentLen - huiche));
		//}
		if($("#tempSelect").val()=="")
		{
			$("#inputContent").val($("#contents").val());
		}
		len(ob);
		if ($.trim(ob.val()) == "") {
			/*var errorMsg = "<br/><br/>请输入短信发送内容<br/>";
			$parent.append("<em class='add_error'>" + errorMsg + "</em>");*/
			document.forms[formName].isOk.value = 1;
		} else if (ob.val().length > contentLen) {
			/*var errorMsg = "<br/><br/>短信发送内容过长，应少于990个字符<br/>";
			$parent.append("<em class='add_error'>" + errorMsg + "</em>");*/
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
	$('#mobileUrl')
			.keypress(
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
					});
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
	$("form").find("textarea[name='msg']").keyup(function() {
		//var content = $(this).val();
		//var huiche = content.length - content.replaceAll("\n", "").length;
		//if (content.length + huiche > contentLen) {
		//	$(this).val(content.substring(0, contentLen - huiche));
		//}
		len($(this));
	});
}

// 设置当前显示的表单名称
var formName = "";
function setFormName(name) {
	formName = name;
	$('#formName').val(formName);
}
var subcount = 0;
$(document)
		.ready(
				function() {
					//短信预览窗口
					$("#detail_Info").dialog({
						autoOpen: false,
						modal:true,
						// title:'预览效果', 
						title:getJsLocaleMessage('dxkf','dxkf_dxqf_client_ylxg'), 
						height:520,
						width:500,
						closeOnEscape: false,
						open:function(){
							var  hhh=$("#effinfo").height();
							hideSelect();
							$(".ui-dialog-titlebar-close").hide();												 		 
					 		 var ct = $.trim($("#ct").text());
					 		 var yct =$.trim($("#yct").text());
					 		 var isCharg = $("#isCharg").val();
					 		 //sp账号余额
					 		 var spf = $.trim($("#spf").text());
					 		 //sp账号付费类型
					 		var spChargetype = $.trim($("#spChargetype").val());

					 		if(isCharg == "true")
					 		 {
						 		 if(eval(ct)<eval(yct))
						 		 {
						 			// $("#messages1 font").text("机构余额不足不允许进行发送!");
						 			$("#messages1 font").text(getJsLocaleMessage('dxkf','dxkf_dxqf_client_yebzbfs'));
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
					 		 
					 		//预付费账号
					 		if(spChargetype == "1")
					 		 {
					 			 //余额不足
						 		 if(eval(spf)<eval(yct))
						 		 {
						 			// $("#messages1 font").text("sp账号余额不足,不允许发送,请联系管理员充值!");
						 			 $("#messages1 font").text(getJsLocaleMessage('dxkf','dxkf_dxqf_client_spyebzbfs'));
						 		     $("#messages1").show(); 
						 		     $("#btsend").attr("disabled","disabled");
						 		 }				 		 
						 		 $("#showspf").show();
					 		 }
					 		//后付费账号
					 		else if(spChargetype == "2"){
								$("#showspf").hide();
					 		}else{
					 			$("#messages1").show(); 
					 		 }
					 		 //如果预发送条数为0，则也不允许发送，因为有可能此号码未绑定通道。				 		 
					 		 if(eval(yct) == 0)
					 		 {
					 		    $("#btsend").attr("disabled","disabled");
					 		 }			
					 		 if($("#messages2").is(":visible")){
					 			$("#messages1").hide();
					 		 }
						}
						,close:function(){
							showSelect();	
							$("#effinfo").hide();
							//$("#arrowhead").attr('src','images/unfold.png');
							//$(".ui-dialog-buttonpane button").first().css("display","");
							$("#btsend").attr("disabled","");
							$("#showyct").show();
						}
					});	
					
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
										if ((tmpSel.find("> option").length == 1 && tmpSel
												.val() == "")
												|| $(this).attr("id") == "addModel2") {
											tmpSel.empty();
											tmpSel.load("tem_smsTemplate.htm", {
												method : "getTmAsOption",
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

					// 加载数据源配置
					$("#dbId")
							.load("p_datasourceConf.htm?method=getDBAsOption");
					// 加载智能抓取模板
					$("#sqlTemp").load("tem_smsTemplate.htm", {
						method : "getTmAsOption",
						dsflag : 2
					});

					synlen();
					limit();
					setFormName($("#formName").val());
					$("form[name='" + formName + "']").find(
							"input[name='reInput']").click(function() {
						reInputs($(this));
					});
					// 点击创建或存草稿按钮时
					$("input[id='preSend']").click(
						
							function() {
								var sendTime = $('#timerTime').val();
								var serverTime ;
								$.post("common.htm?method=getServerTime", {
								
								}, function(msg) {
									serverTime = msg;
									var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
									var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                                    date1.setMinutes(date1.getMinutes() - 1, 0, 0);
									if (date1 <= date2 && $("input[name=timerStatus]:checked").val() == 1) {
										// alert("预发送时间小于服务器当前时间！请合理预定发送时间！");
										alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_sendtimeerror'));
										$("#timerTime").val("");
										return;
									}
									else
									{
									$("form[name='" + formName + "']").find("input[name='subState']").val(2);
	
									var formObj = document.forms[formName];
									if (formObj.busCode.value == "") {
										// alert("没有可用的业务类型！");
										alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_noywle'));
										return;
									}
									if (formObj.spUser.value == "") {
										// alert("没有可供发送的SP账号！");
										alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nosp'));
										return;
									}
									// 判断主题是否填写
									$("form[name='" + formName + "']").find(
											"input[name='taskname']").trigger(
											"blur");
									/*if (formObj.isOk.value == 0) {
										alert("请填入主题！");
										return;
									}*/
									if($("#vss").find(" tr").length<2)
							        {
							        	// alert("请添加发送号码！");
							        	alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_addphone'));
							        	return ;
							        }
									var $texta = $("form[name='" + formName + "']")
											.find("textarea[name='msg']");
									/*if (($("#groupSelect").val() == "2"
											&& $("#bookId").val() == "")) {
										alert("未选择客户通讯录！");
										return;
									}
									else if (($("#groupSelect").val() == "4"
											&& $("#bookId").val() == "")) {
										alert("未选择通讯录群组！");
										return;
									}
									else if(($("#groupSelect").val() == "5"
											&& $("#proIds").val() == "")) {
										alert("未选择客户属性！");
										return;
									}*/
									$texta.trigger("blur");
									if (formObj.isOk.value == 1) {
										// alert("请填写正确的短信内容！");
										alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_smscontenterror'));
										return;
									}
									//不用回复时,不对通道号位数进行判断
									var isReply = $("input:radio[name='isReply']:checked").attr("value");
									if(isReply != 0)
									{
										if($("#subNo").val() == ""){
											// alert("没有可供发送的拓展尾号！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_noweihao'));
											return ;
										}
										var sendFlag = $("#sendFlag").val().split("&");
										if(sendFlag[0] == "false" && sendFlag[1] == "false" && sendFlag[2] == "false"){
											// alert("移动、联通、电信主通道号+拓展尾号位数超过20位，不能发送！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_weihaoerror1'));
											return ;
										}
										if(sendFlag[0] == "false" && sendFlag[1] == "false" ){
											// alert("移动、联通主通道号+拓展尾号位数超过20位，不能发送！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_weihaoerror2'));
											return ;
										}
										if(sendFlag[0] == "false" && sendFlag[2] == "false"){
											// alert("移动、电信主通道号+拓展尾号位数超过20位，不能发送！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_weihaoerror3'));
											return ;
										}
										if(sendFlag[1] == "false" && sendFlag[2] == "false"){
											// alert("联通、电信主通道号+拓展尾号位数超过20位，不能发送！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_weihaoerror4'));
											return ;
										}
										if(sendFlag[0] == "false" ){
											// alert("移动主通道号+拓展尾号位数超过20位，不能发送！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_weihaoerror5'));
											return ;
										}
										if(sendFlag[1] == "false"){
											// alert("联通主通道号+拓展尾号位数超过20位，不能发送！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_weihaoerror6'));
											return ;
										}
										if(sendFlag[2] == "false"){
											// alert("电信主通道号+拓展尾号位数超过20位，不能发送！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_weihaoerror7'));
											return ;
										}
									}
								checkWords();
								}
							});
						});

					// 编辑短信时点击创建或保存按钮时
					$("input[id='subEdit'],input[id='saveEdit']")
							.click(
									function() {
										if ($(this).attr("id") == "subEdit") {
											$("form[name='" + formName + "']")
													.find(
															"input[name='subState']")
													.val(2);
										} else {
											$("form[name='" + formName + "']")
													.find(
															"input[name='subState']")
													.val(1);
										}

										var formObj = document.forms[formName];
										if (formObj.busCode.value == "") {
											//alert("没有可用的业务类型！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_noywle'));
											return;
										}
										if (formObj.spUser.value == "") {
											// alert("没有可供发送的SP账号！");
											alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nosp'));
											return;
										}
										// 判断主题是否填写
										$("form[name='" + formName + "']")
												.find("input[name='taskname']")
												.trigger("blur");
										/*if (formObj.isOk.value == 0) {
											
											alert("请填入主题！");
											return;
										}*/
										//alert("dd");
										var $texta = $(
												"form[name='" + formName + "']")
												.find("textarea[name='msg']");
									var reInputVa = $(
												"form[name='" + formName + "']")
												.find(
														"input[name='reInput']:checked")
												.val();
									//alert(reInputVa);
										// 简单发送时
										if (formName == "form1") {
											$texta.trigger("blur");
											if (formObj.isOk.value == 1) {
												//alert("请填写正确的短信内容！");
												alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_smscontenterror'));
												return;
											}
											if (reInputVa == 1
													|| reInputVa == undefined) {
												checkTxt();
											} else {
												checkWords();
											}
										} else if (formName == "form2")// 文件发送
										{
											//alert("ddd");
											var sendType = $("#sendType").val();
											if (sendType != 3) {
												$texta.trigger("blur");
												$("#dtMsg").val($texta.val());
											}
											if (formObj.isOk.value == 1
													&& sendType != 3) {
												// alert("请填写正确的短信内容！");
												alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_smscontenterror'));
												return;
											}
											if (((reInputVa == undefined || reInputVa == 1) && checkFile())
												|| (reInputVa == 0)) {
												checkWords();
											}
										} else if (formName == "form3")// 通讯录发送
										{
											if ((reInputVa == 1 || reInputVa == undefined)
													&& ($("#groupSelect").val() == 0 || $(
															"#bookId").val() == "")) {
												// alert("未选择通讯录来源！");
												alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nophonebook'));
												return;
											}
											$texta.trigger("blur");
											if (formObj.isOk.value == 1) {
												// alert("请填写正确的短信内容！");
												alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_smscontenterror'));
												return;
											}
											checkWords();
										} else if (formName == "form4") {
											if ($("#dbId").val() == "") {
												// alert("请选择数据源！");
												alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nodatasource'));
												return;
											}
											if ($.trim($(
													"form[name='" + formName
															+ "']").find(
													"textarea[name='sql']")
													.val()) == "") {
												// alert("SQL语句为空，短信任务无法添加！");
												alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nosql'));
												return;
											}
											$texta.trigger("blur");
											if (formObj.isOk.value == 1) {
												// alert("请填写正确的短信内容！");
												alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_smscontenterror'));
												return;
											}
											checkWords();
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
					resizeDialog($("#tempDiv"),"ydkfDialogJson","kfdx_kfqzqf_test1");
				});

//发送
function btsend()
{
	subcount = subcount+1;
	if(subcount > 1)
	{
		// alert("提交信息中，请勿重复提交！");
		alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_resubmit'));
		$("form[name='" + formName + "']").attr("action","");
		return;
	}
	$("#btsend").attr("disabled",true);
	$("#btcancel").attr("disabled",true);
	// 不作为短信内容发送
	if($("#taskname").val()==getJsLocaleMessage('dxkf','dxkf_dxqf_client_cannotsmscontent'))
	{
		$("#taskname").val("");
	}
	var sendTime = $('#timerTime').val();
	var serverTime ;
	$.post("common.htm?method=getServerTime", {
	
	}, function(msg) {
		serverTime = msg;
		var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
		var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
        date1.setMinutes(date1.getMinutes() - 1, 0, 0);
		if (date1 <= date2 && $("input[name=timerStatus]:checked").val() == 1) {
			// alert("预发送时间小于服务器当前时间！请合理预定发送时间[EXFV011]");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_sendtimeerror2'));
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
				// alert("无有效发送号码，无法提交。");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_errorphone'));
				$("#btsend").attr("disabled","");
				$("#btcancel").attr("disabled","");
			}else
			{
				$("#btsend").attr("disabled",true);
				$("#btcancel").attr("disabled",true);
				$("form[name='" + formName + "']").attr("target","_self");
				$("form[name='" + formName + "']").attr('action', "kfs_sendClientSMS.htm?method=add");
				$("form[name='" + formName + "']").submit();
			}
		}
	});
}
//取消
function btcancel1()
{
	showSelect();
	$("#detail_Info").dialog("close");
}

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
				// alert("无效号码：" + result);
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_falsephone') + result);
				return false;
			} else {
				checkWords();
			}
		});
	} else {
		// alert("请输入手机号码！");
		alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_inputphone'));
		// return checkWords();
	}
}
// 验证上传文件格式
function checkFile() {
	//alert("看看看进来没有？");
	var fileObj = $("form[name='" + formName + "']").find(
			"input[name='numFile']");
	if (fileObj.val() != "") {
		var fileNmae = fileObj.val();
		var index = fileNmae.lastIndexOf(".");
		fileNmae = fileNmae.substring(index + 1).toLowerCase();
		if (fileNmae != "txt" && fileNmae != "txt") {
			// alert("上传文件格式错误，请选择txt格式的文件。");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_uploadfileerror'));
			return false;
		} else {
			return true;
		}
	} else {
		// alert("请选择上传的文件！");
		alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nofileupload'));
		return false;
	}
}

// 验证短信内容是否包含关键字
function checkWords() {
	$("#preSend").attr("disabled",true);
    $("#qingkong").attr("disabled",true);
    var strlen = parseInt($("#strlen").html());
    if(strlen>contentLen){
    	// alert("短信发送内容过长，应少于"+contentLen+"个字符！");
    	alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_smslength')+contentLen+getJsLocaleMessage('dxkf','dxkf_dxqf_client_gzf'));
    	$("#preSend").attr("disabled","");
	    $("#qingkong").attr("disabled","");
	    return ;
    }
	var msg = $("form[name='" + formName + "']").find("textarea[name='msg']")
			.val();
    //关键字检查 带上贴尾类容
    msg += $('#tailcontents').text();
	$.ajax({
		url: "kfs_sendClientSMS.htm?method=checkBadWord", 
		type : "POST",
		data: {tmMsg : msg,corpCode : $("#lgcorpcode").val()},
	success: function(message) {
		if(message.indexOf("@")==-1){
			// alert("检查关键字失败！");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_keyvalueerror'));
			$("#preSend").attr("disabled","");
		    $("#qingkong").attr("disabled","");
			return;
		}
		message=message.substr(message.indexOf("@")+1);
		if (message != "" && message !="error") {
			// alert("发送内容包含如下违禁词组：\n     " + message + "\n请检查后重新输入。");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_cannotword')+ message +getJsLocaleMessage('dxkf','dxkf_dxqf_client_reinput'));
			$("#preSend").attr("disabled","");
		    $("#qingkong").attr("disabled","");
		} else if (message == "error") {
			// alert("预览操作失败！[EKFV242]");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_yloptfalse'));
			$("#preSend").attr("disabled","");
		    $("#qingkong").attr("disabled","");
		} else {
			//alert("2");
			if(-1 == msg.indexOf("退订")){
				$("#confirmMsgText").dialog({
				      resizable: false,
				      modal: true,
				      width:500,
				      height:250
			    });
			    $("#confirmMsgText").dialog("open");
			}else{
				//关键字检查放到提交表单来处理
				//checkWords();
				//验证是否定时
				checkTimer();
			}
			// $('form[name="'+formName+'"]').submit();
		}
	},
	error:function(xrq,textStatus,errorThrown){
		// alert("预览失败！");
		alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_ylfalse'));
		$("#preSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
		window.location.href='kfs_sendClientSMS.htm';
		return;
	}
	});
}

// 验证是否定时
function checkTimer() {
	var $timer = $("form[name='" + formName + "']").find(
			"input:checked[name='timerStatus']");
	var $timerTime = $("form[name='" + formName + "']").find(
			"input[name='timerTime']");
	if ($timer.val() == 1 && $timerTime.val() == "") {
		// alert("请填写定时时间！");
		alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_inputsendtime'));
		$("#preSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
	} else {
		
		//alert("3");
		//$("form[name='" + formName + "']").submit();
		var cpath = $("#cpath").val();
        $("form[name='" + formName + "']").attr('action', "kfs_previewSMS.htm?method=preview");
        $("form[name='" + formName + "']").submit();
		
	}
}

function preSend(data){
		data=data.substr(data.indexOf("%")+1);
		$("#preSend").attr("disabled",false);
        $("#qingkong").attr("disabled",false);
        $(".ui-dialog-titlebar").show();
		if(data=="error")
		{
			// alert("预览异常，操作失败！[EKFV241]");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_ylerror'));
			return;
		}else if(data=="nodraft"){
            // alert("草稿箱号码文件不存在！");
            alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nocgxfile'));
            return;
        }
		//处理后台抛出的自定义类型异常
		if(data.indexOf("empex") == 0)
		{
			// alert("预览失败："+data.substr(5));
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_ylfalse')+data.substr(10));
			$("#subSend").attr("disabled",false);
			$("#qingkong").attr("disabled",false);
			return;
		}
		//$("#newtable").empty();
		$("#content").empty();
		//$("#preStr").val(data);
	    //$("#newtable").append("<tr align='center'><td style='border: 1px  solid; height: 25px;width:30px'>编号</td>" +
		//		"<td style='border: 1px  solid; height: 25px;'><center><div style='width:90px'>手机号码</div></center></td>" +
		//		"<td style='border: 1px  solid; height: 25px;'>短信内容</td>" );
    	$("#content").append("<thead><tr align='center'><th>" + getJsLocaleMessage('dxkf','dxkf_dxqf_client_codenum') + "</th>" +
				"<th><center><div style='width:89px'>" + getJsLocaleMessage('dxkf','dxkf_dxqf_client_phoenum') + "</div></center></th>" +
				"<th>"+getJsLocaleMessage('dxkf','dxkf_dxqf_client_yys')+"</th>"+
				"<th>" + getJsLocaleMessage('dxkf','dxkf_dxqf_client_smscontent') + "</th></tr></thead>" );
    	//$("#content").append("<tbody><tr><td colspan='3' style='border: 1px  solid; height: 25px;' align='center'>没有有效的号码</td></tr></tbody>");
    	
		var signStr = $("form[name='" + formName + "']").find("input[name='signStr']").val()
		var msgContent=$("form[name='" + formName + "']").find("textarea[name='msg']").val();
        var tailcontents = $('#tailcontents').text()||'';
		signStr = "";
		msgContent = signStr+msgContent+tailcontents;
		if(data=="noPhone")
		{
			// alert("通讯录中没有包含号码！");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nophonenumatbook'));
		}else
		{
			hideSelect();
			var array=eval("("+data+")");
			
			$("#hidSubCount").val(array.subCount);
			$("#hidEffCount").val(array.effCount);
			$("#hidMobileUrl").val(array.validFilePath);
			$("#hidPreSendCount").val(array.preSendCount);
			
			var nums=array.previewPhone.split(';');
			$("#counts").text(array.subCount);
		    $("#effs").text(array.effCount);
		    $("#blacks").text(array.blackCount);
		    $("#legers").text(array.badModeCount);
		    $("#sames").text(array.repeatCount);
		    $("#ct").text(array.depFeeCount);
			$("#yct").text(array.preSendCount);
			$("#isCharg").attr("value",array.isCharge);
			//sp账号付费类型
			$("#spChargetype").attr("value",array.spChargetype);
			var spye = array.spFeeResult;
			if(spye!="nocheck"){
				checkspye(spye);
			}else{
		 		$("#messages2").hide();
			}
		    //sp账号余额
		    var spFee = array.spFee;
		    if(array.spIsCheck!="nocheck"){
		    	 checkspfee(spFee,array.spChargetype);
		    }
			var wuxiaonum = parseInt(array.subCount)-parseInt(array.effCount);
			$("#alleff").text(wuxiaonum);
			$("#badurl").attr("value",array.validFilePath);
			$("#content").append("<tbody>");
			if(wuxiaonum == 0){
				$("#preinfodown").attr("style","display:none");
			}else{
				$("#preinfodown").attr("style","display:block");
			}
			var ishidephone = $("#ishidephome").val();
			if(nums[0] == ""){
				$("#content").append("<tr><td colspan='4'>"+getJsLocaleMessage('dxkf','dxkf_dxqf_client_nonomalphone')+"</tr>");
			}else{
				for ( var x = 0; x < nums.length-1; x=x+1) {
					//$("#newtable").append("<tr align ='center'><td style='border: 1px  solid; height: 25px;'>"+(x+1)
					//		+"</td><td style='border: 1px  solid; height: 25px;'>"+nums[x]+"</td>" +
					//		"<td style='border: 1px  solid; height: 25px;word-break: break-all;text-align:left;'><xmp style='word-break: break-all;white-space:normal;'>"
					//		+msgContent+"</xmp></td></tr>");
					var phone1 = nums[x].split(',')[0];
					if(ishidephone == 0){
						phone1 = phone1.substring(0,3)+"*****"+phone1.substring(8);
					}
					var tempContentHtml = "<tr><td>"+(x+1)+"</td><td>"+phone1+"</td><td>" + nums[x].split(',')[1]+"</td><td><xmp style='word-break: break-all;white-space:normal;'>";
					if("false"==nums[x].split(',')[3]){
						tempContentHtml += (msgContent + nums[x].split(',')[2]);
					}else{
						tempContentHtml += (nums[x].split(',')[2]+msgContent);
					}
					tempContentHtml += "</xmp></td></tr>";
					$("#content").append(tempContentHtml);
				}
			}
			$("#content").append("</tbody>");
			
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
		    $("#tdSign").empty();
		    $("#tdSign").append(tdSignHtml);
			$("#detail_Info").css("display","block");
		$("#detail_Info").dialog("open");
		deleteleftline1();	
		showSelect();
		$(".ui-dialog-titlebar-close").hide();
		}
}
function checkspye(yeresult)
{
	var altstr = "";
	if(yeresult.indexOf("lessgwfee") == 0)
	{
		// altstr = "运营商余额不足，当前余额："+yeresult.split("=")[1];
		altstr = getJsLocaleMessage('dxkf','dxkf_dxqf_client_yebzdqye')+yeresult.split("=")[1];
		 $("#btsend").attr("disabled","disabled");
	}else if(yeresult=="feefail" || yeresult=="nogwfee")
	{
		// altstr = "获取运营商余额失败，不允许发送";
		altstr = getJsLocaleMessage('dxkf','dxkf_dxqf_client_yebznosend');
		 $("#btsend").attr("disabled","disabled");
	}
	$("#messages2 font").text(altstr);
}

/**
 * 检查sp账号余额
 */
function checkspfee(spFee,spChargetype)
{
	//提示信息
	var tips = "";
	//预付费
	if(spChargetype=="1"){
		//余额大于0
		if(spFee<=0){
			// tips = "sp账号余额不足,不允许发送,请联系管理员充值";
			tips = getJsLocaleMessage('dxkf','dxkf_dxqf_client_spyebzbfs');
			$("#btsend").attr("disabled","disabled");
		}
		$("#showspf").show();
		$("#spf").html(spFee);
	}else if(spChargetype=="2")
	{
		$("#showspf").hide();
		//后付费账号，不作处理
	}else{
		//tips = "获取sp账号余额信息异常";
		tips = getJsLocaleMessage('dxkf','dxkf_dxqf_client_spyeerror');
		$("#btsend").attr("disabled","disabled");
	}
	if(tips!=""){
		$("#messages1 font").text(tips);
	}
}
// 获取表格的值


// 获取短信模板内容
function getTempMsg(tmpOb) {
	var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
	if(tmpOb.val() != ""){
		msgob.attr("readonly", "readonly");
		$.ajax({
			url:"kfs_sendClientSMS.htm?method=getTmMsg1",
			data:{tmId : tmpOb.val()},
			type:"post",
			success:function(result){
				if(result.indexOf("@")==-1){
					window.location.href='kfs_sendClientSMS.htm';
					return;
				}
				result=result.substr(result.indexOf("@")+1);
				if(result=="error")
				{
					// alert("获取模板失败！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_getmbfalse'));
					return;
				}
				msgob.val(result);
				//msgob.trigger("blur");
				msgob.attr("readonly","readonly");
				msgob.css("background","#E8E8E8");
				msgob.bind('focus', function() {
					this.blur();
				}); 
				len(msgob);
			},
			error:function(xrq,textStatus,errorThrown){
				// alert("获取模板异常！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_getmberror'));
				window.location.href='kfs_sendClientSMS.htm';
			}
		});
	} else {
		msgob.val($("#inputContent").val());
		msgob.attr("readonly","");
		msgob.focus();
		msgob.css("background","#FFFFFF");
		msgob.unbind("focus");
		//msgob.val("");
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

// 文件发送时改变发送类型调用的方法
function changeSendType(va) {
	var $texta = $("form[name='" + formName + "']")
			.find("textarea[name='msg']");
	var $textp = $texta.parent();
	var $textspan = $texta.parent().prev("span");
	var isTmShow = $("form[name='" + formName + "']").find("#model").is(
			":visible");
	$("#downlink").attr("href", "javascript:openTemp('smsTem" + va + ".txt')");
	if (va == 3) {
		$textp.hide();
		$textspan.hide();
		$("#temDiv").css("display", "none");
	} else {
		$("#temDiv").css("display", "");
		if ($textp.is(":hidden")) {
			$textp.show();
			$textspan.show();
		}
		$texta.val("");
		len($texta);
		if (isTmShow) {
			var tmpSel = $("form[name='" + formName + "']").find("#tempSelect");
			tmpSel.empty();
			tmpSel.load("tem_smsTemplate.htm", {
				method : "getTmAsOption",
				dsflag : va - 1
			}, function() {
				// tmpSel.trigger("change");
				});
		}
	}

}

function openTemp(r) {
	var path = $("#cpath").val();
	window.open(path + "/fileUpload/fileDownload/" + r, "_blank");
}

// 查看号码文件是否存在
function checkFileIsVisible(url, src) {
	$.post(src + "/kfs_sendClientSMS.htm?method=checkFiles", {
		url : url
	}, function(result) {
		if (result == "true") {
			window.showModalDialog(src + "/" + url);
		} else if (result == "false")
			//alert("文件不存在");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nofile'));
		else
			// alert("出现异常,无法跳转");
			alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_donotjumperror'));
	});
}
// 编辑时是否重新输入号码
function reInputs($obThis) {
	if ($obThis.val() == 1) {
		$obThis.parent().next("#fileDiv").show();
	} else {
		$obThis.parent().next("#fileDiv").hide();
	}
}
// 编辑短信时文件发送时改变发送类型调用的方法
function changeSendType2(va) {
	var curSendType = $("#curSendType").val();
	var $texta = $("form[name='" + formName + "']")
			.find("textarea[name='msg']");
	var $textp = $texta.parent();
	var $textspan = $texta.parent().prev("span");
	var isTmShow = $("form[name='" + formName + "']").find("#model").is(
			":visible");
	$("#downlink").attr("href", "javascript:openTemp('smsTem" + va + ".txt')");
	if (va != curSendType) {
		$("form[name='" + formName + "']").find("input[name='reInput']").eq(1)
				.trigger("click");
		$("form[name='" + formName + "']").find("input[name='reInput']").eq(0)
				.attr("disabled", "disabled");
	} else {
		$("form[name='" + formName + "']").find("input[name='reInput']").eq(0)
				.attr("disabled", "");
	}
	if (va == 3) {
		$textp.hide();
		$textspan.hide();
		$("#temDiv").css("display", "none");
	} else {
		$("#temDiv").css("display", "");
		$("#temDiv a ").css("display", "");
		if ($textp.is(":hidden")) {
			$textp.show();
			$textspan.show();
		}
		$texta.val("");
		len($texta);
		if (isTmShow) {
			var tmpSel = $("form[name='" + formName + "']").find("#tempSelect");
			tmpSel.empty();
			tmpSel.load("tem_smsTemplate.htm", {
				method : "getTmAsOption",
				dsflag : va - 1
			}, function() {
				// tmpSel.trigger("change");
				});
		}
	}

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

// 选择通讯录下拉框时-已废弃
function bookShow(sid) {
	var time = new Date();
	var id = sid.val();
	switch (id) {
	case "0":
		$("#groupResouce").attr("value", "");
		/// $("#groupType").html("　来自机构：");
		$("#groupType").html("　" + getJsLocaleMessage('dxkf','dxkf_dxqf_page_formdept') + "：");
		$("#bookId").attr("value", "");
		$("#bookType").attr("value", "");
		break;
	case "1":
		showTree($('#path').val() + "/u_depTree1.jsp?timer=" + time
				+ "&isReturnDepId=false&type=1");
		depName = dep.split("&")[0];
		depId = dep.split("&")[1];
		$("#groupType").empty();
		// $("#groupType").html("　来自机构：");
		$("#groupType").html("　" + getJsLocaleMessage('dxkf','dxkf_dxqf_page_formdept') + "：");
		$("#groupResouce").attr("value", depName);
		$("#bookId").attr("value", depId);
		$("#bookType").attr("value", "1");
		break;
	case "2":
		showTree($('#path').val() + "/u_depTree1.jsp?timer=" + time
				+ "&isReturnDepId=false&type=0");
		depName = dep.split("&")[0];
		depId = dep.split("&")[1];
		$("#groupType").empty();
		// $("#groupType").html("　来自机构：");
		$("#groupType").html("　" + getJsLocaleMessage('dxkf','dxkf_dxqf_page_formdept') + "：");
		$("#groupResouce").attr("value", depName);
		$("#bookId").attr("value", depId);
		$("#bookType").attr("value", "2");
		break;
	case "3":
		showTree($('#path').val() + "/u_sysuserTree2.jsp?timer=" + time
				+ "&isReturnDepId=false");
		depName = dep.split("&")[1];
		$("#groupType").empty();
		if (dep.split("&")[0] == "3") {
			// $("#groupType").html("　来自机构：");
			$("#groupType").html("　" + getJsLocaleMessage('dxkf','dxkf_dxqf_page_formdept') + "：");
		} else {
			// $("#groupType").html("来自操作员：");
			$("#groupType").html(getJsLocaleMessage('dxkf','dxkf_dxqf_page_formuser') + "：");
		}
		$("#bookId").attr("value", dep.split("&")[2]);
		$("#bookType").attr("value", dep.split("&")[0]);
		$("#groupResouce").attr("value", depName);
		break;
	}
}
function checkError()
{
	/*if($("#error").val()=="0")
	{
		window.clearInterval(dd);
		$("#probar").dialog("close");
		$("#preSend").attr("disabled",false);
        $("#qingkong").attr("disabled",false);
	}*/
}
var dep = "&&";
function showTree(url) {
	var time = new Date();
	dep = window
			.showModalDialog(url, "", "dialogWidth=" + ieWidth
					+ "px;dialogHeight=" + ieHeight
					+ "px;help:no;status:no;center:yes");
	if (dep == null) {
		dep = "&&";
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
		+lguserid+"&lgcorpcode="+lgcorpcode+"&timee="+ new Date().getTime();
	$("#tempFrame").attr("src",frameSrc);
	resizeDialog($("#tempDiv"),"ydkfDialogJson","kfdx_kfqzqf_test1");
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
		// alert("未选择短信模板！");
		alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nochoosesmsmb') );
		return;
	}else
	{
		var message = $ro.next("xmp").text();
		var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
        //模板不可编辑状态
        if(tmpEditorFlag == 0){
            msgob.attr("readonly","readonly");
            msgob.css("background-color","#E8E8E8");
        }
		msgob.val(message);
	}
	$("#ctem").hide();
	$("#qtem").show();
	len(msgob);
	$("#tempDiv").dialog("close");
}

function tempNo()
{
	var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
	msgob.css("background-color","");
	msgob.val("");
	msgob.attr("readonly","");
	len(msgob);
	$("#qtem").hide();
	$("#ctem").show();
}

var inval;
var mcode;
function openTemDiv(menuCode)
{
	mcode = menuCode;
	var tpath = $("#cpath").val();
	var url = tpath+"/tem_smsTemplate.htm?opentm=0&lguserid="
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

function reloadPage(){
	getLoginInfo("#loginUser");
	window.location.href='kfs_sendClientSMS.htm?method=find&lguserid='
		+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()
		+'&lgguid='+$('#lgguid').val();
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

function resets(){
				$("#taskname").attr("value","");
				$("#contents").attr("value","");
				$("#sendNow").attr("checked","checked");
				$("#timerTime").val("");
				$('#time3').hide()
}

function showMenu(tt) {
			$("#sengTypes").css("display","block");
			var bid = $("#bookId").val();
			var gr = $("#groupResouce").val();
			//hideSelect();
			$('#sengTypes').dialog({
				autoOpen: false,
				height: 520,
				width: 530,
				title:tt,
				modal:true,
				resizable:false
			});
			$(".ui-dialog-titlebar-close").hide();
			$("#bookIdTemp").attr("value",bid);
        	$("#groupResouceTemp").val(gr);
			$("#sengTypes").dialog("open");
}

//确认按钮
function btconfirm(){
		  showSelect();
	      if($("#groupSelect").val() == 5)
	      {
                add();
		  }
	      else
	      {
		       $('#sengTypes').dialog('close');
		  }
}

//取消按钮
function btcancel()
{
  		showSelect();
		$("#bookId").attr("value",$("#bookIdTemp").val());
        $("#groupResouce").attr("value",$("#groupResouceTemp").val());
		$('#sengTypes').dialog('close');
}

function hideMenu() {
			$("#dropMenu").hide();
}

function hideSignModel(){
			$("#signModel").css("display","none");
			$("#isSign").css("display","none");
			$("#isSign").attr("checked",false)
			$("#signStr").val("");
			$("#signStr").css("display","none");
			eblur($("#contents"));
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
            $('#tail-area').hide();
            $('#tailcontents').text('');
            $('input[name="tailcontents"]').val('');
        },
        success:function(data){
            data = eval('('+data+')');
            if(data.status == 1){//找到对应贴尾
                $('#tailcontents').text(data.contents);
                $('input[name="tailcontents"]').val(data.contents);
                $('#tail-area').show();
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
function getGateNumber(){
		    var isReply = $("input:radio[name='isReply']:checked").attr("value");
		    var spUser = $("#spUser").val();

		    if(spUser == null || spUser == ""){
		    	return ;
		    }
            //设置贴尾信息
            if(!setTailInfo()){
                return;
            }
		    //设置短信最长位数
   			setContentMaxLen(spUser);
		   //点不用回复，不请求后台
		    if(isReply=="0"){
		    	$("#subno").html("");
				$("#subNo").val("");
				$("#curSubNo").css("display","none");
				return ;
		    }
		   
	    	var circleSubNo = $("#circleSubNo").val();
	    	var taskId = $("#taskId").val();
	    	$.post("common.htm?method=getSubNo", 
	    		{
					spUser: spUser,
					isReply:isReply,
					circleSubNo:circleSubNo,
					taskId:taskId,
					lgcorpcode:$("#lgcorpcode").val(),
					lgguid:$("#lgguid").val()
				}, function(result) {
					if(result == "noSubNo"){
						// alert("拓展尾号获取失败！");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_getweihaofalse'));
						$("#subno").html("");
						$("#subNo").val("");
						return ;
					}else if(result == "error"){
						// alert("通道号获取失败！");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_gettongdaofalse'));
						$("#subno").html("");
						$("#subNo").val("");
						return ;
					}else if(result == "noUsedSubNo"){
						// alert("系统没有可用的拓展尾号！");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_systemnoweihao'));
						$("#subno").html("");
						$("#subNo").val("");
						return ;
					}
					if(isReply != 0){
				    	$("#curSubNo").css("display","inline");
				    }else{
				    	$("#curSubNo").css("display","none");
				    }
				    
				    if(result!=null && result !="")
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

//通过SP账号设置短信最长位数
function setContentMaxLen(spUser)
{
	$.post("kfs_sendClientSMS.htm?method=getSpGateConfig&spUser="+spUser,{},
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
		if(isChinese && contentLen == 700)
		{
			contentLen = 350;
		}
		else if(!isChinese && contentLen == 350 && baseContentLen == 700)
		{
			contentLen = 700;
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
function showAlert()
{
			var sel = $("#groupSelect").val();
			var tt = "";
			switch(sel)
			{
			case "1":
				break;
			case "2":
				// tt="客户通讯录发送";
				tt = getJsLocaleMessage('dxkf','dxkf_dxqf_client_kftxlfs');
				break;
			case "3":
				break;
			case "4":	
				// tt="个人群组发送";
				tt = getJsLocaleMessage('dxkf','dxkf_dxqf_client_grqzfs');
				break;
			case "5":	
				// tt="客户属性发送";
				 tt = getJsLocaleMessage('dxkf','dxkf_dxqf_page_khsxfs');
				break;
			}
			showMenu(tt);
}

//当前时间与服务器时间对比--guodw
function checkServerTime(){
				//----------------------------------
				if($("input[name=timerStatus]:checked").val() == 1){
					var sendTime = $('#timerTime').val();
					var serverTime ;
					$.post("kfs_sendClientSMS.htm", {
						method : "getServerTime"
					}, function(msg) {
						serverTime = msg;
						var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
						var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                        date1.setMinutes(date1.getMinutes() - 1, 0, 0);
						if (date1 <= date2) {
							// alert("预发送时间小于服务器当前时间！请合理预定发送时间");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_sendtimeerror'));
							$("#timerTime").val("");
							return;
						}
					});
				}
}

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

function changeDialogTitleCss(id) {
    var $titleBar = $("#ui-dialog-title-" + id);
    $titleBar.parent().addClass("titleBar");
    $titleBar.addClass("titleBarTxt");
}
function showInfo()
{
    var $iframe = $("#infoDiv").children('iframe');
    if(!$iframe.attr('src')){
        $iframe.attr('src',$iframe.attr('data-src'));
    }

	var commonPath = $("#commonPath").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	$("#signClientIds").val();
	$("#signClientIds").val($("#signClientIds").val()+$(this).val()+",");
	var chooseType="2,5,6,7";
	if(skin.indexOf("frame4.0") != -1) {
		$("#flowFrame").attr("src",commonPath+"/common/selectUserInfo.jsp?lguserid="+lguserid+"&chooseType="+chooseType+"&lgcorpcode="+lgcorpcode);
		changeDialogTitleCss('infoDiv');
		$(".clearBtn").val(getJsLocaleMessage('common','common_clean'));
		$("#ui-dialog-title-infoDiv").next("a.ui-dialog-titlebar-close").show();
	}
	resizeDialog($("#infoDiv"),"ydkfDialogJson","kfdx_kfqzqf_test2");
    $("#infoDiv").dialog("open");
    showSelect();
}

//弹出高级搜索框
function showSearchBox()
{
    var $iframe = $("#advSearch").children('iframe');
    if(!$iframe.attr('src')){
        $iframe.attr('src',$iframe.attr('data-src'));
    }
    $("#advSearch").dialog("open");
}

function closeSearchBox(){
	   $("#advSearch").dialog("close");	
}

// 清空主界面的选择对象的数据
function clearUser(){
    $("#ygtxl").remove();
    // 清空主页面的数据
    $("#rightSelectedUserOption").val("");
    $("#empDepIds").val(",");
    $("#cliDepIds").val(",");
    $("#groupIds").val(",");
    $("#empIds").val(",");
    $("#cliIds").val(",");
    $("#malIds").val(",");
    $("#signClientIds").val(",");
    $("#ydywPhoneStr").val("");
    $("#ydywGroupStr").val(",");
    $("#proIdStr").val(",");
    $("#proValueIdStr").val(",");
    $("#phoneStr1").val("");
}

function delAddr()
{
			if(confirm(getJsLocaleMessage('dxkf','dxkf_dxqf_client_suretodelte')))
			{
                clearUser()
				$("#depIdStr").val(",");
				$("#groupStr").val(",");
				$("#wxphoneStr").val("");
				$("#depIdStrTemp").val(",");
				$("#groupStrTemp").val(",");
				$("#proIdStrTemp").val(",");
				$("#proValueIdStrTemp").val(",");
				$("#ydywGroupStrTemp").val(",");
				$(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
				$(window.frames['flowFrame'].document).find("#manCount").html("0");
				$(window.frames['flowFrame'].document).find("#right").empty();
				$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
			}
}

//高级搜索删除选中人员
function delAddr12()
{
			if(confirm(getJsLocaleMessage('dxkf','dxkf_dxqf_client_suretodelte')))
			{ 	
				$("#ygtxl2").remove();
				$("#phoneStr12").val("");
				$("#selectAllStatus").val(''); 
				$("#selectAllStatusTemp").val(''); 
				$("#unChioceUserIds").val(''); 
				$("#conditionsqlTemp").val('');
				$(window.frames['advSearchFrame'].document).find("table#content input[type='checkbox']").attr('checked',false);
			}
}

//因为机构id字符串是在选择机构按钮事件生成的所以当用户选择机构后点击取消时应该将生成的字符串还原回去
function doNo(){
    if(skin.indexOf("frame4.0") !== -1) {
        var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
        if(optionSize === 0){
            clearUser();
            return;
        }
        //该操作将清空所选择对象,是否执行？
        if(confirm("该操作将清空所选择对象,是否执行？")){
			//代表的是员工机构IDS
            $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
            //代表的是客户机构IDS
            $(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val("");
            //代表的是群组IDS
            $(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
            //代表的是分页索引第一页
            $(window.frames['flowFrame'].document).find("#pageIndex").val("1");
            //代表的是员工IDS
            $(window.frames['flowFrame'].document).find("#employeeIds").val("");
            //代表的是客户IDS
            $(window.frames['flowFrame'].document).find("#clientIds").val("");
            //代表的是外部人员IDS
            $(window.frames['flowFrame'].document).find("#malistIds").val("");
            //签约用户手机号
            $(window.frames['flowFrame'].document).find("#signClientPhoneStr").val("");
            //属性值的IDS
            $(window.frames['flowFrame'].document).find("#cusFieldValueIds").val("");
			//客户属性Ids
            $(window.frames['flowFrame'].document).find("#cusFieldIds").val("");
			//套餐Ids
            $(window.frames['flowFrame'].document).find("#taocanIds").val("");
            //签约用户IDS
            $(window.frames['flowFrame'].document).find("#signClientIds").val("");

            $(window.frames['flowFrame'].document).find("#right").empty();
            $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
            //选择用户对象号码串
            $(window.frames['flowFrame'].document).find("#moblieStrs").val("");
            $(window.frames['flowFrame'].document).find("#manCount").html(0);

            clearUser();
        }
    	return;
    }
		$("#depIdStr").val($("#depIdStrTemp").val());//将机构id字符串还原
		$("#groupStr").val($("#groupStrTemp").val());//将群组id字符串还原
		$("#proIdStr").val($("#proIdStrTemp").val());//将群组id字符串还原
		$("#proValueIdStr").val($("#proValueIdStrTemp").val());//将群组id字符串还原
		$("#ydywGroupStr").val($("#ydywGroupStrTemp").val());
		if($("#manCountTemp").html()==null){
			$(window.frames['flowFrame'].document).find("#manCount").html("0");
		}else{
			$(window.frames['flowFrame'].document).find("#manCount").html($("#manCountTemp").html());
		}
		$(window.frames['flowFrame'].document).find("#right").empty();
		$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
		$(window.frames['flowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());
		$(window.frames['flowFrame'].document).find("#getChooseMan").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html().replace(/option/gi,"li").replace(/value/g,"dataval"));
		$("#infoDiv").dialog("close");
		showSelect();
}

$(function(){
		inputTipText(); //直接调用就OK了
});

function clearHiddenArea(){
			 $(window.parent.document).find("#phoneStr12Temp").val(''); 
			 $(window.parent.document).find("#selectAllStatusTemp").val(''); 
			 $(window.parent.document).find("#unChioceUserIdsTemp").val(''); 
			 //$(window.parent.document).find("#conditionsqlTemp").val(''); 
} 

function includeItem(arys,item){
		    var arys = arys||[];
		    var flag = false;
	    	for(var i=0;i < arys.length;i++) {
	            if(item.toString() == arys[i]){flag = true;return flag;}; 
	        }
	        return flag;
}

function removeItem(arys,item){
	        for(var i=0;i < arys.length;i++) {  
	            if(item == arys[i]) arys.splice(i,1,"");  
	        }  
}

function unique(data){
			data = data || []; 
			var a = {}; 
			for (var i=0; i<data.length; i++) { 
				var v = data[i]; 
				if (v!=""&&typeof(a[v]) == 'undefined'){ 
					a[v] = 1; 
				}
			}; 

			data.length=0; 
			for (var i in a){ 
			  data[data.length] = i; 
			}
			return data; 
}

//高级搜索-重置页面
function doReload(){
			 //所属机构
			 $("#depNam").attr("value", getJsLocaleMessage('dxkf','dxkf_dxqf_page_pleasechoose'));	
			 $("#depId").attr("value","");
			 //客户号
			 $("#clientCode").val("");
			 //姓名
			 $("#cName").val("");
			 //性别
			 $("#sex").val("");
			 //手机
			 $("#mobile").val("");
			 //区域
			 $("#area").val("");
			 //生日
			  $("#d1").val("");
			 //生日
			 $("#d2").val("");
			 //多选
			 $("select[name^='FIELD']").val("");
			 //单选
			 $("input[name^='FIELD']").attr("checked",false);
}

//选择checkbox事件处理
function onclickClient(obj){
           	var status = $(window.parent.document).find("#selectAllStatusTemp").val();
           	if(status==null||""==status||status=="false"){
   			    var tempStr = $(window.parent.document).find("#phoneStr12Temp").val()
   				var tempArray = new Array();
   				
   				//window.console.log(tempStr);
   				//window.console.log(tempArray);
   				//window.console.log($(obj).attr('mobile'));
   				//window.console.log($(obj).attr("checked"));
				if($(obj).attr("checked")){
						tempArray = tempStr.split(",");
						tempArray.push($(obj).attr('mobile'));
 				}else{
 					tempStr = tempStr.replaceAll($(obj).attr('mobile'),"");
 					tempArray = tempStr.split(",");
	 			}			
					
   				unique(tempArray);
   				//window.console.log(tempArray);
   				var newTempStr = tempArray.join(",");
   				
   				//window.console.log(newTempStr);
   				$(window.parent.document).find("#phoneStr12Temp").val(newTempStr);
   			}else{
   			    var tempStr = $(window.parent.document).find("#unChioceUserIdsTemp").val();
   				var tempArray = new Array();
   				
   				//window.console.log(tempStr);
   				//window.console.log(tempArray);
   				//window.console.log($(obj).attr('value'));
   				//window.console.log($(obj).attr("checked"));
				if(!$(obj).attr("checked")){
					tempArray = tempStr.split(",");
					tempArray.push($(obj).attr('value'));
 				}else{
 					tempArray = tempStr.split(",");
 					removeItem(tempArray,$(obj).attr('value'));
	 			}
		 					
				//window.console.log(tempArray);
   				unique(tempArray);
   				var newTempStr = tempArray.join(",");
   				
   				//window.console.log(newTempStr);
   				$(window.parent.document).find("#unChioceUserIdsTemp").val(newTempStr);
   			}
}

//搜索页面reload
function pageLoad(){
			var status = $(window.parent.document).find("#selectAllStatusTemp").val();
			
			if(status==null||""==status||status=="false"){
				$("#selectAll").attr("checked",false);
				$("input[name='clientIds']").attr("checked",$("#selectAll").attr("checked"));
				var tempArray = $(window.parent.document).find("#phoneStr12Temp").val().split(",");
				unique(tempArray);
				
				if(tempArray.length>0){
					$(window.parent.frames['advSearchFrame'].document).find("#content input[name='clientIds']").each(function(index,element){
				    	if(includeItem(tempArray,$(this).attr("mobile"))){
				    		$(this).attr("checked",true);
						}
		            });
				}
			}else{
				$("#selectAll").attr("checked",true);
				$("input[name='clientIds']").attr("checked",$("#selectAll").attr("checked"));
				var tempArray = $(window.parent.document).find("#unChioceUserIdsTemp").val().split(",");
				unique(tempArray);
		
				if(tempArray.length>0){
					 $(window.parent.frames['advSearchFrame'].document).find("#content input[name='clientIds']").each(function(index,element){
					    	if(includeItem(tempArray,$(this).val())){
					    		$(this).attr("checked",false);
							}
			            });
				}
			}		
}

//选中的机构显示文本框
function zTreeOnClickOK2() {
		hideMenu();
}
//发送成功跳转群发任务查看界面
 function sendRecord(menuCode, taskid, lguserid, lgcorpcode)
 {
	closemessage();
	window.parent.openNewTab(menuCode,base_path+"/smt_smsSendedBox2.htm?method=find&taskid="+taskid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
 }
 function closemessage()
 {
	 $("#message").dialog("close");
 }

$(document).ready(function(){

    $('#toDraft').click(function(){
        //暂存草稿前判断发送内容以及发送号码是否为空
        var msg = $.trim($('#contents').val());
        var len = $('#vss tr:gt(0)').length;
        if(!msg && !len){
            // alert('发送内容和发送号码不能全为空！');
            alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_contentorphonenotnull'));
            return false;
        }
        $("form[name='" + formName + "']").attr('action', "kfs_sendClientSMS.htm?method=toDraft&timee="+new Date().getTime());
        // "不作为短信内容发送"
        if($("#taskname").val()==getJsLocaleMessage('dxkf','dxkf_dxqf_client_cannotsmscontent'))
        {
            $("#taskname").val("");
        }
        //提交之前置灰暂存草稿按钮
        $(this).attr('disabled',true);
        $("form[name='" + formName + "']").submit();
    });
    $("#draftDiv").dialog({
        autoOpen: false,
        height:500,
        width: 900,
        resizable:false,
        modal: true,
        open:function(){

        },
        close:function(){
            //关闭草稿箱选择 页面内容移除
            $("#draftFrame").attr("src","");
        }
    });
})

/**
 * 删除草稿箱文件行
 * @param obj
 */
function delRow(obj){
    if(confirm(getJsLocaleMessage('dxkf','dxkf_dxqf_client_suretodelte'))){
        $(obj).parents('tr').remove();
        //删除草稿文件后 清空草稿信息
        $('#draftId').val('');
    }
}

//选择草稿箱
function showDraft()
{
    var tpath = $("#cpath").val();
    var draftstype = 3;
    var frameSrc = tpath+"/common.htm?method=getDrafts&draftstype="+draftstype+"&timee=" + new Date().getTime();
    $("#draftFrame").attr("src",frameSrc);
    $("#draftDiv").dialog("open");
}

function draftCancel()
{
    $("#draftDiv").dialog("close");
}

function draftSure()
{
    var $fo = $("#draftFrame").contents();
    var $ro = $fo.find("input[type='radio']:checked");
    if(!$ro.val())
    {
        // lert("未选择草稿箱！");
    	alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_nochoosecgx'));
        return;
    }
    //已存在草稿箱 提示覆盖
    if($('#containDraft').val()){
    	// "是否覆盖已有的草稿？"
        if(confirm(getJsLocaleMessage('dxkf','dxkf_dxqf_page_isconvertcgx'))){
            $('#containDraft').parents('tr').remove();
            //删除草稿文件后 清空草稿信息
            $('#draftId').val('');
        }else{
            $("#draftDiv").dialog("close");
            return false;
        }
    }
    //回填草稿箱内容前 如若存在已选择的模板 则需取消掉模板
    tempNo();

    var $tr = $ro.parents('tr');
    //草稿箱发送文件相对路径
    var filePath = $tr.find('td:eq(0)').attr('path');
    var draftId = $ro.val();
    var taskname = $tr.find('td:eq(2)').find('label').text()||'';
    var msg = $tr.find('td:eq(3)').find('label').attr('msg');
    var trhtml = [];
    trhtml.push("<tr  class='div_bd' style='background-color:#ffffff'>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage('dxkf','dxkf_dxqf_page_caogaoxiang')+"</td>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>");
    trhtml.push("<label class='draft-label'>"+draftId+" "+taskname+"</label>");
    trhtml.push("<input type='hidden' id='containDraft' name='containDraft' value='"+filePath+"'>");
    trhtml.push("</td>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick='delRow(this);'><img border='0' src='" + $('#cpath').val() + "/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer' /></a></td>");
    trhtml.push("</tr>");
    $("#vss").append(trhtml.join(''));
    $('#draftId').val(draftId);
    $('#taskname').css('color','').val(taskname);
    $('#contents').val(msg);
    len($('#contents'));
    $("#draftDiv").dialog("close");
}

/**
 * 保存草稿回调
 */
function saveDraft(result){
    result = eval('('+result+')');
    if(result.ok == 1){
        $('#draftId').val(result.draftid);
        // alert('暂存草稿成功！')
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_savecgxsuucess'));
    }else if(result.ok == -1){
       // alert('草稿箱号码文件不存在！');
    	alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_cgxnofile'));
    }else if(result.ok == -2){
        //alert('草稿箱可能已被删除，暂存失败！');
    	alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_cgxisdelete'));
    }else{
        //alert('暂存草稿箱失败！');
    	alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_savecgxfalse'));
    }
    //暂存草稿按钮置为可用
    $('#toDraft').attr('disabled',false);
}

function confirmBtn() {
	$("#confirmMsgText").dialog("close");
	checkTimer();
}
function cancelBtn() {
	$("#preSend").attr("disabled","");
    $("#qingkong").attr("disabled","");
	$("#confirmMsgText").dialog("close");
}