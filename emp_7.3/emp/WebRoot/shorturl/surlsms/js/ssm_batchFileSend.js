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

// 失去焦点时判断--(只用来统计字数 不清楚首尾空格)
function eblurNoTrim(ob) {
    var $parent = ob.parent();
    $parent.find(".add_error").remove();
    if (ob.is("#contents")) {
        // ob.val($.trim(ob.val()));
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
					if (date1 < date2.getTime()+20*60*1000 && $("input[name=timerStatus]:checked").val() == 1) {
						alert("预发送时间需要大于服务器当前时间20分钟！请合理预定发送时间[EXFV011]");
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
							alert("字数超过"+contentLen+"个，请重新输入");
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							return;
						}
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
						var srcUrl = formObj.netUrlId.value;
						if(srcUrl==null || srcUrl==""){
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
//						var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
//						var objExp= srcUrl.match(Expression);
//						var contents = $("#contents").val(); 
//						var objExp2 = contents.match(Expression);
//						
//						if(objExp==null){
//							alert("插入地址格式不对");
//							$("#subSend").attr("disabled","");
//							$("#qingkong").attr("disabled","");
//							return;
//						}
//						var text = srcUrl.replace(Expression,"");
//						if(text.length>0){
//							alert("插入信息包含地址以外内容");
//							$("#subSend").attr("disabled","");
//							$("#qingkong").attr("disabled","");
//							return;
//						}
//						if(objExp2 != null && objExp2.length>4){
//							alert("短信内容中包含多个地址");
//							$("#subSend").attr("disabled","");
//							$("#qingkong").attr("disabled","");
//							return;
//						}
//						if(contents.indexOf(srcUrl)<=-1){
//							alert("短信内容中没有插入活动地址地址");
//							$("#subSend").attr("disabled","");
//							$("#qingkong").attr("disabled","");
//							return;
//						}
						//活动地址中的&替换成|
						//formObj.srcUrl.value = srcUrl.replace(/\&/g,"|");
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
							alert("请填写正确的短信内容！");
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							return;
						}
						if($("#gateNumberDiv").html() == ""){
							alert("没有可供发送的通道号或拓展尾号！");
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							return ;
						}
						var isReply = $("input:radio[name='isReply']:checked").attr("value");
						//不用回复时,不对通道号位数进行判断
						if(isReply != 0 )
						{
							if($("#gateTLCount").val()>0){
								alert("主通道号+拓展尾号位数超过20位，不支持回复！不允许发送！");
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
						$("#busCode1").val($("#busCode").val());
						$("#spUser1").val($("#spUser").val());
						//关键字检查放到提交表单来处理
						//checkWords();
						//验证是否定时
						checkTimer()
					}
				});
				//----------------------------------------------------
			});
	
	// 点击暂存草稿按钮时
	$("input[id='toDraft']").click(function() {
			$("#toDraft").attr("disabled",true);
			$("#subSend").attr("disabled",true);
			$("#qingkong").attr("disabled",true);
			
			//暂存草稿前判断发送内容以及发送号码是否为空
	        var msg = $.trim($('#contents').val());
	        var len = $('#vss tr:gt(0)').length;
	        if(len == 0){
	            alert('发送号码不能为空！');
	            $("#toDraft").attr("disabled","");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
	            return;
	        }
	        
			$("#phoneStr").val($("#phoneStr1").val()+$("#phoneStr2").val()+$("#inputphone").val());
			
			if ($("#taskname").val() == "不作为短信内容发送") {
				$("#taskname").val("");
			}
			
			var formObj = document.forms[formName];
			if(eval($("#strlen").text())>contentLen){
				alert("字数超过"+contentLen+"个，请重新输入");
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
				title:'加载进度..', 
				height:80,
				resizable :false,
				closeOnEscape: false,
				width:200,
				open: function(){
				$(".ui-dialog-titlebar").hide();
					errorNum=0;
					//dd = window.setInterval("fresh()",3000);
				}
			});
			$("form[name='form2']").attr('action', "urls_previewSMS.htm?method=toDraft&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val()+"&timee="+new Date().getTime());
        	$("form[name='form2']").submit();
        	//换回预览的servlet
			$("form[name='form2']").attr('action', "urls_previewSMS.htm?method=preview&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val());
	});

	//查看
	$("input[id='shows']").click(
			function(){
				
				

				var formObj = document.forms["form2"];
				if (formObj.busCode.value == "") {
					alert("没有可用的业务类型！");
					return;
				}
				if (formObj.spUser.value == "") {
					alert("没有可供发送的SP账号！");
					return;
				}
				
				if(re()){
					
					$("#form2").attr("action", "url_ssendBatchSMS.htm?method=adds");
					
					$("#form2").submit();
				}
				
			});
	$("#tempDiv").dialog({
		autoOpen: false,
		height:600,
		width: 950,
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
				alert("无效号码[EXFV004]：" + result);
				return false;
			} else {
				checkWords();
			}
		});
	} else {
		alert("请输入手机号码！");
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
				//window.location.href='url_ssendBatchSMS.htm';
				//return;
			//}
			if(message.indexOf("@")==-1){
				alert("关键字检验请求响应超时，请刷新页面或稍后重试！[EXFV005]");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
			message=message.substr(message.indexOf("@")+1);
			if (message != "" && message !="error") {
				alert("发送内容包含如下违禁词组：\n     " + message + "\n请检查后重新输入[EXFV006]");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
			} else if (message == "error") {
				alert("检验关键字异常[EXFV007]");
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
			} else {
				//alert("2");
				checkTimer();
				// $('form[name="'+formName+'"]').submit();
			}
		},
		error:function(xrq,textStatus,errorThrown){
			alert("关键字检验请求响应超时，请刷新页面或稍后重试！[EXFV008]");
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			window.location.href='url_ssendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
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
		alert("请填写定时时间！");
		$("#subSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
	} else {
		if($("#vss").find(" tr").length<2)
        {
        	alert("请添加手机号码或上传文件！");
        	$("#subSend").attr("disabled","");
    		$("#qingkong").attr("disabled","");
        }else
        {
	        $("#probar").dialog({
			modal:true,
			title:'加载进度..', 
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
	        $("form[name='form2']").attr("action","urls_previewSMS.htm?method=preview&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val()+"&domainId="+$("#domainId").val()+"&timee="+new Date().getTime());
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
					alert("网络或服务器无法连接，请稍后重试！[EXFV009]");
					window.clearInterval(dd);
					//window.location.href='url_ssendBatchSMS.htm';
					window.location.href='url_ssendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			
			if(errorNum==0)
			{
				errorNum=1;
				alert("网络或服务器无法连接，请稍后重试！[EXFV010]");
			}
			window.clearInterval(dd);
			//window.location.href='url_ssendBatchSMS.htm';
			window.location.href='url_ssendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
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
					//window.location.href='url_ssendBatchSMS.htm';
					window.location.href='url_ssendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
					return;
				}
				result=result.substr(result.indexOf("@")+1);
				if(result=="error")
				{
					alert("获取模板失败！");
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
				alert("网络或服务器无法连接，请稍后重试！[EXFV010]");
				//window.location.href='url_ssendBatchSMS.htm';
				window.location.href='url_ssendBatchSMS.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
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
		if (date1 < date2) {
			alert("预发送时间需要大于服务器当前时间20分钟！请合理预定发送时间[EXFV011]");
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
			alert("文件不存在[EXFV012]");
		else
			alert("出现异常,无法跳转[EXFV013]");
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
    	  alert("发送手机号码不能为空!请选择一种手机号码来源!");
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
	var SendReq=$("#SendReq").val();
	var cpath = $("#cpath").val();
	$("#probar").dialog("close");
	$(".ui-dialog-titlebar").show();
	if(data.indexOf("empex") == 0)
	{
		//关键字检查
		if(data.indexOf("V10016") >= 0)
		{
			alert("发送内容包含如下违禁词组：\n     " + data.substr(data.indexOf("[V10016]")+8) + "\n请检查后重新输入[EXFV006]");
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
				info:'相同内容预览，表单流上传失败。userId:'+ lguserid +'，lgcorpcode:'+ lgcorpcode
			});
		}
		alert("预览失败："+data.substr(5));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	if(data=="error")
	{
		alert("预览异常，操作失败。[EXFV014]");
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data=="overstep")
	{
		alert("文件内有效号码大于100万，系统不支持，请重新选择发送文件！[EXFV015]");
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data=="overSize")
	{
		alert("上传文件过大，单次上传TXT文件或EXCEL文件最大支持"+MAX_SIZE+"兆，ZIP文件最大支持"+ZIP_SIZE+"兆，请重新选择发送文件！[EXFV018]");
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data == "draftnotex")
	{
		alert("草稿箱不可用，可能已被删除，请重新选择！");
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data == "draftnotfile")
	{
		alert("草稿箱文件不存在，可能已被删除，请重新选择！");
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data == "drafterr")
	{
		alert("草稿箱无法使用，请重新选择！");
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
	
	$("#content").empty();
	
	$("#content").append("<thead><tr align='center'><th>编号</th>" +
			"<th><center><div style='width:89px'>手机号码</div></center></th>" +
			"<th>短信内容</th></tr></thead>" );
	var msgContent=$("form[name='" + formName + "']").find("textarea[name='msg']").val();
	msgContent += $("#tailcontents").val();
	if(data=="noPhone")
	{
		alert("没有可发送的有效号码！[EXFV016]");
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
		$("#hidDzUrl").val(array.urlFilePath);
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
	    	$("#content").append("<tbody><tr><td colspan='3'  align='center'>没有有效的号码</td></tr></tbody>");
		    $("#detail_Info").css("display","block");
		    $("#detail_Info").dialog("open");
		    deleteleftline();
		    $(".ui-dialog-titlebar-close").hide();
	    }else
	    {
//			for (var x = 0; x < nums.length; x=x+1) {
//				$("#content").append("<tr align ='center'><td>"+(x+1)+"</td><td>"+nums[x]+"</td>" +"<td><xmp style='word-break: break-all;white-space:normal; '>"
//						+msgContent+"</xmp></td></tr>");
//			}
	    	if(SendReq=="0"){
			    $("#SendReq").val("1");
			    var sendType=$("#sendType").val();
			    $("#content").load(
                    "url_urlSendDiffSMS.htm?method=readSmsContent&url=" + array.viewFilePath + "&sendType=" + sendType,
//			    	"url_ssendDiffSMS.htm?method=readSmsContent&url="+array.viewFilePath+"&sendType="+sendType,
//			    	"url_ssendDiffSMS.htm?method=readSmsContent",
//			    	{url:array.viewFilePath,sendType:sendType},
			    	function(){
			    		$("#detail_Info").css("display","block")
			    		$("#detail_Info").dialog("open");
			    		deleteleftline1();	
			    });
			    $(".ui-dialog-titlebar-close").hide();
	    	}
	    }
//	    $("#detail_Info").css("display","block");
//	    $("#detail_Info").dialog("open");
//	    deleteleftline();
	    //$(".ui-dialog-titlebar-close").hide();
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
		altstr = "获取运营商余额失败，不允许发送";
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
	$("form[name='form2']").attr('action', "urls_previewSMS.htm?method=preview&lguserid="+$("#lguserid").val()+"&taskId="+$("#taskId").val());
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
		alert("文件内号码大于100万，系统不支持，请重新选择发送文件！[EXFV015]");
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
