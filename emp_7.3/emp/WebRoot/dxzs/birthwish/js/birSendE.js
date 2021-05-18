var contentLen = 980;
var baseContentLen = 980;
// 获取1到n-1条英文短信内容的长度
var entotalLen = 153;
var sign = "";
var signName = 0;
var isChineseSignName = false;
function setSign(obj)
{
			if(obj.checked)
			{
				$("#signStr").css("display","inline");
				$("#signStr").css("background-color","#E8E8E8");
				$("#signStr").val(getJsLocaleMessage('dxzs','dxzs_ssend_alert_141')+'XXX');
			}else
			{
				$("#signStr").css("display","none");
				$("#signStr").val("");
			}
			eblur($("#contents"));
}

function doOk() {
	$("#isSubmit").val("1");
	var skin = $("#skin").val();
	$(window.frames['flowFrame'].document).find("#left").empty();
	$(window.frames['flowFrame'].document).find("#showPage1").html("");
	$(window.frames['flowFrame'].document).find("#pageIndex2").val("1");

	var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
	if(optionSize > 0){
		$("#haveOne").val(1);
	}

	//右边select内容
	$("#rightSelectedUserOption").val($(window.frames['flowFrame'].document).find("#right").html());
	//操作员Id
	$("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
	//机构Id
	$("#empDepIds").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
	//总人数
	$("#choiceNum").html($(window.frames['flowFrame'].document).find("#manCount").html());

	var empDepName = "";
	var empName = "";
	$(window.frames['flowFrame'].document).find("#right option").each(function () {
		if(skin.indexOf("frame4.0") > -1){
			if($(this).attr("isdep") === "1"){
				//机构
				empDepName += $(this).html() + ",";
			}else if($(this).attr("isdep") === "4"){
				//员工
				empName += $(this).html() + ",";
			}
		}else {
			if($(this).attr("membertype") === "1"){
				//员工
				empName += $(this).html() + ",";
			}else {
				//机构
				empDepName += $(this).html() + ",";
			}
		}
	});
	$("#empDepName").val(empDepName);
	$("#empName").val(empName);

	$("#infoDiv").dialog("close");
	$("#selectWorkerButton").val(getJsLocaleMessage('dxzs','dxzs_ssend_alert_142'));
}

function doNo(){
	var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
	if(optionSize > 0){
		$("#haveOne").val(1);
	}
	$(window.frames['flowFrame'].document).find("#employeeIds").empty();
	$(window.frames['flowFrame'].document).find("#empDepIdsStrs").empty();
	$(window.frames['flowFrame'].document).find("#manCount").html("0");
	$(window.frames['flowFrame'].document).find("#right").empty();
	$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
	$(window.frames['flowFrame'].document).find("#left").empty();
	$(window.frames['flowFrame'].document).find("#showPage1").html("");
	$(window.frames['flowFrame'].document).find("#pageIndex2").val("1");
	$("#infoDiv").dialog("close");
}

function eblur(ob) 
{
       		if (ob.is("#contents")) 
           	{
       			ob.val($.trim(ob.val()));
       			var content = $("#contents").val();
       			var huiche = content.length - content.replaceAll("\n", "").length;
       			if (content.length + huiche > contentLen) {
       				$("#contents").val(content.substring(0, contentLen - huiche));
       			}
       			len(ob);
       			if ($.trim(ob.val()) == "") {
       				document.forms["form2"].isOk.value = 1;
       			} else if (ob.val().length > contentLen) {
       				document.forms["form2"].isOk.value = 1;
       			} else {
       				document.forms["form2"].isOk.value = 2;
       			}
       		}
}

// 统计短信内容字数
function len(ob) 
{
       		var content = $.trim(ob.val());
       		var huiche = content.length - content.replaceAll("\n", "").length;
       		var signLen = 0;
       		var signNameLen=0;
       		if(ob.is("#contents") && $("#isAddName").attr("checked"))
           	{
       			signLen=24;
       			sign = $("#signStr").val();
       		}
       		else
       		{
       			sign = "";
       		}
       		if(ob.is("#contents") && $("#isSignName").attr("checked"))
           	{
       			signNameLen=$.trim($("#signName").val()).length;
       			getSignLen($("#signName").val());
       		}
       		else
       		{
				signName = 0;
				isChineseSignName = false;
       		}
       		if(content.length+signLen+signNameLen>contentLen)
       		{
       			$("#contents").val(content.substring(0,contentLen-signLen-signNameLen));
           	}
       		var len = contentLen;
       		if(baseContentLen == 700)
       		{
	       		setSmsContentMaxLen(content);
	       		content = $("#contents").val();
       		}
			 if(!isChineseSignName && contentLen == 700 && sign == "")
			 {
			 	signNameLen = signName;
			 }
       		if(len != contentLen)
			{
				 if(content.length+signLen+signNameLen>contentLen)
       			{
       				$("#contents").val(content.substring(0,contentLen-signLen-signNameLen));
           		}
				$('#maxLen').html("/"+contentLen);
			}
       		$('#strlen').html(($("#contents").val().length + huiche + signLen + signNameLen));
}

function synlen() {
       		$("#form2").find("textarea[name='msg']").keyup(function() {
       			var content = $(this).val();
       			var huiche = content.length - content.replaceAll("\n", "").length;
       			if (content.length + huiche > contentLen) {
       				$(this).val(content.substring(0, contentLen - huiche));
       			}
       			len($(this));
       		});
       		$("#signStr").keyup(function() {
       			len($("#contents"));
       		});
}
function setDefault()
{
	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_98'))) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var spUser = $("#spUser").val();
		$.post("bir_birthdaySendEMP.htm?method=setDefault", {
			lguserid: lguserid,
			lgcorpcode: lgcorpcode,
			spUser: spUser,
			flag: "5"			
			}, function(result) {
			if (result == "seccuss") {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_99'));
				return;
			} 
			else if(result == "fail"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_100'));
				return;
			}
		});
	}
}

//通过SP账号设置短信最长位数
function setSpUserContentMaxLen()
{
	var spUser = $("#spUser").val();
  //$.post("ssm_comm.htm",{method : "getSpGateConfig",spUser : spUser},
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
				baseContentLen = contentLen;
				$('#maxLen').html("/"+contentLen);
			}
			eblur($("#contents"));
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
	if(sign == "" && !isChineseSignName)
	{
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
	else
	{
		contentLen = 350;
	}
}

//通过短信内容设置短信最长位数
function getSignLen(SignName)
{
	var isChinese = false;
	var signLen = 0;
	var charCode;
	var pattern = /(9[1-4]|12[3-6])/;
	for(var j=0;j<SignName.length;j++)
	{
		signLen += 1;
		charCode = SignName.charAt(j).charCodeAt();
		if(charCode > 127)
		{
			isChineseSignName = true;
			break;
		}
		if(pattern.test(charCode))
		{
			signLen += 1;
		}

	}
	if(isChineseSignName)
	{
		signName = SignName.length;
	}
	else
	{
		signName = signLen;
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

