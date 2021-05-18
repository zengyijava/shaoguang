var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var contentLen = 980;
var baseContentLen = 980;

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
	if(ob.is("#contents")){
		if(content.length + huiche > contentLen)
		{
			$("#contents").val(content.substring(tailcontents.length,contentLen-huiche));
			//短信内容
			smsContent = $("#contents").val();
		    //贴尾内容+短信内容
	 		content = tailcontents + smsContent;
			huiche = content.length - content.replaceAll("\n", "").length;
		}
	}
	var len = contentLen;
	countSMS($("#contents").val().length + huiche, tailcontents, content);
	if(len != contentLen)
	{
	    //贴尾内容+短信内容
 		content = tailcontents + $("#contents").val();
		//贴尾内容长度+短信内容长度大于短信最大长度
		if(content.length + huiche > contentLen)
		{
			$("#contents").val(content.substring(tailcontents.length,contentLen-huiche));
			smsContent = $("#contents").val();
			//贴尾内容+短信内容
	 		content = tailcontents + smsContent;
			huiche = content.length - content.replaceAll("\n", "").length;
		}
		$('#maxLen').html("/"+contentLen);
	}
	$('form[name="' + formName + '"]').find(' #strlen').html(($("#contents").val().length + huiche + tailcontents.length));
}

//根据账户获取账户绑定的路由信息拆分规则
function setGtInfo(spUser)
{
	$.post("ssm_comm.htm?method=getSpGateConfig&spUser="+spUser,{},
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
			len($("#contents"));
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
							$("#contents").val(content.substring(tailcontents.length,j-huiche));
							smsContent = $("#contents").val();
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
							$("#contents").val(content.substring(tailcontents.length,350-huiche));
							smsContent = $("#contents").val();
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
	if (ob.is("#contents")) {
		ob.val($.trim(ob.val()));
		var content = $("#contents").val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > contentLen) {
			$("#contents").val(content.substring(0, contentLen - huiche));
		}
		if($("#tempSelect").val()=="")
		{
			$("#inputContent").val($("#contents").val());
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
	$("form").find("textarea[name='msg']").keyup(function() {
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
//				var files= $(":file");
//				//判断文件重复
//				var flag=false;
//				$.each(files,function(i,n)
//				{
//		            if($(n).val()=="")
//		            {
//		            	flag=true;
//						return;
//		            }
//			    }); 
//				
//				if(flag)
//				{
//					alert("请选择导入号码文件！");
//					return;
//				}
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
					if (date1 <= date2 && $("input[name=timerStatus]:checked").val() == 1) {
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_20'));
						$("#timerTime").val("");
						$("#subSend").attr("disabled","");
						$("#qingkong").attr("disabled","");
						return;
					}
					else
					{
						$("#error").val("0");
						if ($(this).attr("id") == "subSend") {
							$("form[name='" + formName + "']").find(
							"input[name='subState']").val(2);
						} else {
							$("form[name='" + formName + "']").find(
							"input[name='subState']").val(1);
						}
						
						var formObj = document.forms[formName];
						if(eval($("#strlen").text())>contentLen){
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_21')+contentLen+getJsLocaleMessage('dxzs','dxzs_ssend_alert_22'));
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							return;
						}
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
						// 判断主题是否填写
						$("form[name='" + formName + "']").find(
						"input[name='taskname']").trigger(
						"blur");
						if (formObj.isOk.value == 0) {
							//alert("请填入主题！");
							//return;
						}
						var $texta = $("form[name='" + formName + "']")
						.find("textarea[name='msg']");
						// 简单发送时
						var sendType = $(
								"form[name='" + formName + "']")
								.find("#sendType").val();
						if (sendType != 3) {
							$texta.trigger("blur");
							$("#dtMsg").val($texta.val());
						}
						if (formObj.isOk.value == 1
								&& sendType != 3) {
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_25'));
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							return;
						}
						if($("#gateNumberDiv").html() == ""){
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_26'));
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							return ;
						}
						var isReply = $("input:radio[name='isReply']:checked").attr("value");
						//不用回复时,不对通道号位数进行判断
						if(isReply != 0 )
						{
							if($("#gateTLCount").val()>0){
								alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_27'));
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
						$("#busCode1").val($("#busCode").val());
						$("#spUser1").val($("#spUser").val());
						
						var validContent = $("#dtMsg").val()+$("#showtailcontent").text();
						if(-1 == validContent.indexOf("退订")){
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
					}
				});
				//----------------------------------------------------
			});
	// 点击暂存草稿按钮时
	$("input[id='toDraft']").click(function() {
		var tex=$("#vss").html();
		if(tex.indexOf("delbigfile")>-1){		
			alert("超大文件发送,不能存草稿!");
			return;
		}
			$("#toDraft").attr("disabled",true);
			$("#subSend").attr("disabled",true);
			$("#qingkong").attr("disabled",true);
			
			//暂存草稿前判断发送内容以及发送号码是否为空
	        var msg = $.trim($('#contents').val());
	        var len = $('#vss tr:gt(0)').length;
	        if(len == 0){
	            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_35'));
	            $("#toDraft").attr("disabled","");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
	            return;
	        }
	        
			$("#phoneStr").val($("#phoneStr1").val()+$("#phoneStr2").val()+$("#inputphone").val());
			var nomsg = getJsLocaleMessage('dxzs','dxzs_ssend_alert_36');
			if ($("#taskname").val() == nomsg) {
				$("#taskname").val("");
			}
			
			var formObj = document.forms[formName];
			if(eval($("#strlen").text())>contentLen){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_21')+contentLen+getJsLocaleMessage('dxzs','dxzs_ssend_alert_22'));
				$("#toDraft").attr("disabled","");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
			
			// 判断主题是否填写
			//$("form[name='" + formName + "']").find("input[name='taskname']").trigger("blur");
			
			var $texta = $("form[name='" + formName + "']").find("textarea[name='msg']");
			// 简单发送时
			var sendType = $("form[name='" + formName + "']").find("#sendType").val();
			if (sendType != 3) {
				$texta.trigger("blur");
				$("#dtMsg").val($texta.val());
			}
			
			$("#probar").dialog({
				modal:true,
				title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_37'), 
				height:80,
				resizable :false,
				closeOnEscape: false,
				width:200,
				open: function(){
				$(".ui-dialog-titlebar").hide();
					errorNum=0;
					dd = window.setInterval("fresh()",3000);
				}
			});
			//移动办公--企业短信--相同内容群发--存草稿
			var date = formatDate(new Date());
			//$("form[name='form2']").attr('action', "ssm_previewSMS.htm?method=toDraft&lguserid=" + $("#lguserid").val() + "&taskId=" + $("#taskId").val() + "&timee=" + date);
			$("form[name='form2']").attr('action', "ssm_previewSMS.htm?method=toDraft&lguserid=" + $("#lguserid").val());
        	// $("form[name='form2']").submit();
		    $("form[name='" + formName + "']").submit();
        	//换回预览的servlet
			$("form[name='form2']").attr('action', "ssm_previewSMS.htm?method=preview&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val());
	});

	//查看
	$("input[id='shows']").click(
			function(){
				var formObj = document.forms["form2"];
				if (formObj.busCode.value == "") {
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_23'));
					return;
				}
				if (formObj.spUser.value == "") {
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_24'));
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
	
	$("#bigfileDiv").dialog({
		autoOpen: false,
		height:440,
		width: 850,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
		}
	});
	resizeDialog($("#tempDiv"),"ydbgDialogJson","dxzhsh_xtnrqf_test1");
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
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_38') + result);
				return false;
			} else {
				checkWords();
			}
		});
	} else {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_39'));
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
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_40'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
			message=message.substr(message.indexOf("@")+1);
			if (message != "" && message !="error") {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+"\n     " + message + "\n"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
			} else if (message == "error") {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_43'));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
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
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_45'));
		$("#subSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
	} else {
		if($("#vss").find(" tr").length<2)
        {
        	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_46'));
        	$("#subSend").attr("disabled","");
    		$("#qingkong").attr("disabled","");
        }else
        {
	        $("#probar").dialog({
			modal:true,
			title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_37'), 
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
					info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_47')+ $("#lguserid").val()+'，taskId:'+$("#taskId").val()
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
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_48'));
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
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_48'));
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
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_49'));
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
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_48'));
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
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_20'));
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
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_48'));
		else
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_96'));
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
    	  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_52'));
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
		$("#contents").attr("value","");
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
	// $("#contents").attr("disabled","");
	  $("#time2").css("display","none");
}


//预览窗口
function preSend(data)
{
//	window.clearInterval(dd);
	$("#error").val("1");
	var cpath = $("#cpath").val();
	$("#probar").dialog("close");
	$(".ui-dialog-titlebar").show();
	if(data.indexOf("empex") == 0)
	{
		//关键字检查
		if(data.indexOf("V10016") >= 0)
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+"\n     " + data.substr(data.indexOf("[V10016]")+8) + "\n"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
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
				info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_53')+ lguserid +'，lgcorpcode:'+ lgcorpcode
			});
		}
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_50')+data.substr(5));

		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	if(data=="error")
	{
		alert(getJsLocaleMessage('common','common_js_wxSend_16')+"[EXFV014]");
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data=="overstep")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_55')+MAX_PHONE_NUM+getJsLocaleMessage('dxzs','dxzs_ssend_alert_56'));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data=="overSize")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_57')+MAX_SIZE+getJsLocaleMessage('dxzs','dxzs_ssend_alert_58')+ZIP_SIZE+getJsLocaleMessage('dxzs','dxzs_ssend_alert_59'));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data == "draftnotex")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_60'));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data == "draftnotfile")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_61'));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data == "drafterr")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_62'));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	
	$("#content").empty();
	
	$("#content").append("<thead><tr align='center'><th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_63')+"</th>" +
			"<th><center><div style='width:89px'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_64')+"</div></center></th>" +
			"<th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_yys')+"</th>"+
			"<th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_65')+"</th></tr></thead>" );
	var msgContent=$("form[name='" + formName + "']").find("textarea[name='msg']").val();
	msgContent += $("#tailcontents").val();
	if(data=="noPhone")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_66'));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
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
	    $("#ct").text(array.depFeeCount);
	    $("#spbalance").text(array.spUserFeeCount);
	    $("#yct").text(array.preSendCount);
	    $("#blacks").text(array.blackCount);
	    $("#legers").text(array.badModeCount);
	    $("#sames").text(array.repeatCount);
	    $("#badurl").attr("value",array.viewFilePath);
	    
	    var spye = array.spFeeResult;
	    $("#gwFee").attr("value",spye);
	    checkspye(spye);
	    var wuxiaonum = parseInt(array.subCount)-parseInt(array.effCount);
	    $("#alleff").text(wuxiaonum);
	    if(wuxiaonum == 0)
	    {
	    	$("#preinfonum").attr("style","display:none");
	    }else{
	    	$("#preinfonum").attr("style","display:block");
	    }
	    $("#isCharg").attr("value",array.isCharge);
	    $("#feeFlag").attr("value",array.feeFlag);
	    if(array.effCount == 0)
	    {
	    	$("#content").append("<tbody><tr><td colspan='4'  align='center'>没有有效的号码</td></tr></tbody>");
	    }else
	    {
			for (var x = 0; x < nums.length; x=x+1) {
				var tempContentHtml = "<tr align ='center'><td>"+(x+1)+"</td><td>"+nums[x].split(',')[0]+"</td><td>"+nums[x].split(',')[1] +"</td><td><xmp style='word-break: break-all;white-space:normal; '>";
				if("false"==nums[x].split(',')[3]){
					tempContentHtml += (msgContent + nums[x].split(',')[2]);
				}else{
					tempContentHtml += (nums[x].split(',')[2]+msgContent);
				}
				tempContentHtml +="</xmp></td></tr>";
				$("#content").append(tempContentHtml);
			}
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
	    $("#detail_Info").css("display","block");
	    $("#detail_Info").dialog("open");

	    deleteleftline();
	    //$(".ui-dialog-titlebar-close").hide();
	    showSelect();
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
//	var batchHtml = "<div class='custBatchNode'><div class='custBatchNode-left'>第1批<button onclick='delCustBatchNode($(this))'>删除此批</button></div><div class='custBatchNode-middle'>发送数量：<input type='text' class='batchNodeValue'/></div><div class='custBatchNode-right'>发送时间：<input type='text' value='2018-12-14 17:00:00'/></div></div>";
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
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_54'));
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
		+lguserid+"&lgcorpcode="+lgcorpcode+"&timee="+ new Date().getTime();
	$("#tempFrame").attr("src",frameSrc);
	$("#tempDiv").dialog({width:620,height:460});
	resizeDialog($("#tempDiv"),"ydbgDialogJson","dxzhsh_xtnrqf_test1");
	$("#tempDiv").dialog("open");
}
function choosebigfile()
{
	var splitFlag = $("input[name='splitFlag']:checked").val();
	if(splitFlag == "1"){
		//选择了分批
		alert("超大文件发送不支持分批设置，请重新选择!");
		return;
	}
	var tex=$("#vss").html();
	
	if(tex.indexOf("delbigfile")>-1){		
		alert("超大文件发送,只能单个发送!");
		return;
	}
	
	if(tex.indexOf("delAddr")>-1||tex.indexOf("ddll")>-1||tex.indexOf("delinputphone")>-1||tex.indexOf("delRow")>-1){		
		alert("超大文件发送不能和其他方式混合发送，请重新选择!");
		return;
	}	
	var tpath = $("#cpath").val();
	var frameSrc = $("#bigfileFrame").attr("src");
	var lguserid=GlobalVars.lguserid;
	var lgcorpcode=GlobalVars.lgcorpcode;
	frameSrc = tpath+"/common.htm?method=getLfBigFileBySms&pageSize=10&dsflag=0&lguserid="
		+lguserid+"&lgcorpcode="+lgcorpcode+"&timee="+new Date().getTime();
	$("#bigfileFrame").attr("src",frameSrc);
	$("#bigfileDiv").dialog({width:1210,height:780});
	$("#bigfileDiv").dialog("open");
}
function tempCancel()
{
	$("#tempDiv").dialog("close");
}
function bigfileCancel()
{
	$("#bigfileDiv").dialog("close");
}
function tempSure()
{
	var $fo = $("#tempFrame").contents();
	var $ro = $fo.find("input[type='radio']:checked");
	if($ro.val() == undefined)
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_69'));
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
function bigfileSure()
{
	var $fo = $("#bigfileFrame").contents();
	var $ro = $fo.find("input[type='radio']:checked");
	if($ro.val() == undefined)
	{
		alert("未选择文件批次！");
		return;
	}
	else
	{
		var buscode = $ro.next("xmp").text();
		var num = $ro.next("xmp").next("xmp").text();
		$("#bigfileid").val($ro.val());
		createtaskid(num);
		addbigfile($ro.val(),buscode);
	}
	$("#bigfileDiv").dialog("close");
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
		height:570,
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
    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType="+showType+"&time=" + new Date().getTime());
          
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
				info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_70')+ lguserid +'，lgcorpcode:'+ lgcorpcode
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

function testBigFile() {
	var bigFileNum = $("#vss .bigFileTr").length;
	if(bigFileNum === 1){
		//已经选择超大文件
		var splitFlag = $("input[name='splitFlag']:checked").val();
		if(splitFlag == "1"){
			//选择了分批
			alert("超大文件发送不支持分批设置!");
			$("#splitFlagN").attr("checked",true);
		}
	}
}