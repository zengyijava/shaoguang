var skin = $("#skin").val();
function initContents() {
	var manualWriter = base_manualWriter;
	var msgob = $("form[name='form2']").find("textarea[name='msg']");
	if (manualWriter == "true") {
		msgob.attr("readonly", "readonly");
		msgob.css("background-color", "#E8E8E8");
		msgob.val("");
	} else {
		msgob.css("background-color", "");
		msgob.val("");
		msgob.attr("readonly", "");
	}
}

function setDepSign(signObject) {
	var depSign = base_depSign;
	var nameSign = base_nameSign;
	var contents = $("#contents").val();
	var str = "";
	if(depSignLeft == depSignRight)
	{
		contents = contents.substring(1);
		str = contents.substring(contents.indexOf(depSignRight) + 1);
	}
	else
	{
		str = contents.substring(contents.indexOf(depSignRight) + 1);
	}
	if (signObject.checked) {
		if ($("#nameSign").attr("checked")) {
			//var str = contents.substring(contents.indexOf(depSignRight) + 1);
			$("#contents").val(depSignLeft + depSign + nameSign + depSignRight + str);
		} else {
			$("#contents").val(depSignLeft + depSign + depSignRight + contents);
		}
	} else {
		//var str = contents.substring(contents.indexOf(depSignRight) + 1);
		if ($("#nameSign").attr("checked")) {
			$("#contents").val(depSignLeft + nameSign + depSignRight + str);
		} else {
			$("#contents").val(str);
		}
	}
	eblur($("#contents"));
}

function setNameSign(signObject) {
	var depSign = base_depSign;
	var nameSign = base_nameSign;
	var contents = $("#contents").val();
	var str = "";
	if(nameSignLeft == nameSignRight)
	{
		contents = contents.substring(1);
		str = contents.substring(contents.indexOf(nameSignRight) + 1);
	}
	else
	{
		str = contents.substring(contents.indexOf(nameSignRight) + 1);
	}
	if (signObject.checked) {
		//var str = contents.substring(contents.indexOf(nameSignRight) + 1);
		if ($("#depSign").attr("checked")) {
			$("#contents").val(nameSignLeft + depSign + nameSign + nameSignRight + str);
		} else {
			$("#contents").val(nameSignLeft + nameSign + nameSignRight + str);
		}
	} else {
		//var str = contents.substring(contents.indexOf(nameSignRight) + 1);
		if ($("#depSign").attr("checked")) {
			$("#contents").val(nameSignLeft + depSign + nameSignRight + str);
		} else {
			$("#contents").val(str);
		}
	}
	eblur($("#contents"));
}

function doOk() {
	var tex=$("#vss").html();
	//如果发超大文件，不允许其他方式发送
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}

	//右边选择的option 用于数据回显
    $("#rightSelectedUserOption").val($(window.frames['flowFrame'].document).find("#right").html());
	$("#phoneStr1").val("");
	var eIds = "";
	var names = "";
	var mobile = "";
	/*if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_75'));
		return;
	}*/
	//客户Id集合
	$("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
	//客户机构Id集合
    $("#depIdStr").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
    //群组Id
	$("#groupIds").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
	//自建Ids
    $("#malIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
	$(window.frames['flowFrame'].document).find("#right option").each(function() {
		eIds = $(this).val();
		names = $(this).text();
		mobile = $(this).attr("mobile");

		if (mobile != null && mobile != "") {
			$("#phoneStr1").val($("#phoneStr1").val() + mobile + ",");
		}
	});
	$("#ygtxl").remove();
    /*客户通讯录 详情 人*/
    if($(window.frames['flowFrame'].document).find("#manCount").html()!=0){
        $("#vss").append("<tr  class='div_bd' id='ygtxl' style='background-color:#ffffff'><td style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_124')+"</td><td style='border-left:0;border-right:0'  align='center' valign='middle' >" + "<a onclick='showInfo();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_125')+"(<label style='color:#0e5ad1' id='choiceNum'></label>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_114')+")</font> </a></td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px'>" + "<a  onclick='delAddr()'><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'/></a></td></tr>");
    }
	$(window.frames['flowFrame'].document).find("#rightSelectTempAll").html($(window.frames['flowFrame'].document).find("#right").html());

	//改变弹出框状态为1
	$("#hidIsDoOk").val("1");

	$("#infoDiv").dialog("close");
	$("#choiceNum").html($(window.frames['flowFrame'].document).find("#manCount").html());
	showSelect();
}

function delAddr() {
	if (confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76'))) {
        $("#ygtxl").remove();
        $("#phoneStr1").val("");
        $("#depIdStr").val(",");
        $("#groupIds").val(",");
		$("#malIds").val(",");
        $("#empIds").val(",");
        $("#rightSelectedUserOption").val("");
		$(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
		$(window.frames['flowFrame'].document).find("#manCount").html("0");
	}
}

function doOk1() {
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}
	var eIds = "";
	var names = "";
	var mobile = "";
	if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_75'));
		return;
	}
	var ops = $(window.frames['flowFrame'].document).find("#right option");
	var buffer = "";
	var trs = $("#vss").children().children();
	//重新生成表格里面的机构
	$.each(trs, function(i, n) {
		var trId = $(n).attr("id");
		var index = trId.indexOf("dep");
		var name = $($(n).children()[0]).text();
		if (index > -1) {
			$(n).remove();
		}
	});
	for (var j = 0; j < ops.length; j = j + 1) {

		eIds = ops[j].value;
		names = ops[j].text;
		mobile = ops[j].mobile;
		if (mobile != '' && $("#phoneStr").val().indexOf("," + $.trim(mobile) + ",") > -1) {
			$(window.frames['flowFrame'].document).find("#right option[mobile='" + mobile + "']").remove();
		}
	}
	$(window.frames['flowFrame'].document).find("#right option").each(function() {
		var trs = $("#vss").children().children();
		var num = trs.length;
		eIds = $(this).val();
		names = $(this).text();
		mobile = $(this).attr("mobile");
		if (mobile != null && mobile != "") {
			$("#vss").append("<tr id='yt" + eIds + "' ><td align='center' name='ConR'  valign='middle'>" + names + "<input  type='hidden' value ='" + mobile + "' id='" + mobile + "' name='bts'/></td><td align='center' valign='middle' >" + mobile + "</td><td align='center' valign='middle'><a onclick='abc(\"yt" + eIds + "\"," + mobile + ",\"aa\"" + ")' >"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_127')+"</a></td></tr>");
			$("#phoneStr").val($("#phoneStr").val() + mobile + ",");
		} else {
			$("#vss").append("<tr id='dep" + eIds + "' ><td align='center' name='ConR'  valign='middle'>" + names + "<input  type='hidden' value ='" + eIds + "' id='" + eIds + "' name='bts'/></td><td align='center' valign='middle' >" + getJsLocaleMessage('dxzs','dxzs_ssend_alert_126') + "</td><td align='center' valign='middle'><a onclick='abc(\"dep" + eIds + "\",\"" + eIds + "\",\"" + names + "\")' >"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_127')+"</a></td></tr>");
		}
	});
	$(window.frames['flowFrame'].document).find("#right").empty();
	$("#infoDiv").dialog("close");
	showSelect();
	$(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
}

function changeDialogTitleCss(id) {
    var $titleBar = $("#ui-dialog-title-" + id);
    $titleBar.parent().addClass("titleBar");
    $titleBar.addClass("titleBarTxt");
}
function showInfo() {
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}
	var commonPath = $("#commonPath").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var chooseType="1,4";
	if(skin.indexOf("frame4.0") != -1) {
		$("#flowFrame").attr("src",commonPath+"/common/selectUserInfo.jsp?lguserid="+lguserid+"&chooseType="+chooseType+"&lgcorpcode="+lgcorpcode);
		changeDialogTitleCss('infoDiv');
		$("#closeBtn").hide();
	}else{
		$(window.frames['flowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());
		$(window.frames['flowFrame'].document).find("#getChooseMan").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html().replace(/option/gi, "li").replace(/value/g, "dataval"));

		$("#depIdStrTemp").val($("#depIdStr").val());
		$("#groupStrTemp").val($("#groupIds").val());
		//window.frames['flowFrame'].window.frames['sonFrame'].a();
		$(window.frames['flowFrame'].document).find('#sonFrame')[0].contentWindow.a();
		var type = $(window.frames['flowFrame'].document).find("#chooseType").val();
		if (type == 1) {
			$(window.frames['flowFrame'].document).find("#showUserName").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_116'));
		} else {
			$(window.frames['flowFrame'].document).find("#showUserName").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_118'));
		}
		$(window.frames['flowFrame'].document).find("#left").empty();
		$(window.frames['flowFrame'].document).find("#depId").val("");
		hideSelect();
		$(window.frames['flowFrame'].document).find('#pageIndex1,#totalPage1').val(1);
		$(window.frames['flowFrame'].document).find('#showPage1').html('');
	}
	$("#infoDiv").dialog("open");
	showSelect();
}
//回填

function optionBack() {
	var trs = $("#vss").children().children();
	$.each(trs, function(i, n) {
		var trId = $(n).attr("id");
		var index = trId.indexOf("dep");
		var name = $($(n).children()[0]).text();
		if (index > -1) {
			var depId = trId.substring(index + 3);
			$(window.frames['flowFrame'].document).find("#right").append("<option value='" + depId + "' mobile=''>" + name + "</option>");
		}
	});
}
//因为机构id字符串是在选择机构按钮事件生成的所以当用户选择机构后点击取消时应该将生成的字符串还原回去

function doNo() {
    if(skin.indexOf("frame4.0") >= 0) {
        var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
        if(optionSize === 0){
            clearUser();
            return;
        }
        //该操作将清空所选择对象,是否执行？
        if(confirm("该操作将清空所选择对象,是否执行？")){
            //代表的是员工机构IDS
            $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
            //代表的是群组IDS
            $(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
            //代表的是分页索引第一页
            $(window.frames['flowFrame'].document).find("#pageIndex").val("1");
            //代表的是员工IDS
            $(window.frames['flowFrame'].document).find("#employeeIds").val("");
            //代表的是外部人员IDS
            $(window.frames['flowFrame'].document).find("#malistIds").val("");

            $(window.frames['flowFrame'].document).find("#right").empty();
            $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
            //选择用户对象号码串
            $(window.frames['flowFrame'].document).find("#moblieStrs").val("");
            $(window.frames['flowFrame'].document).find("#manCount").html(0);

            clearUser();
        }
    	return;
    }
	$("#depIdStr").val($("#depIdStrTemp").val()); //将机构id字符串还原
	$("#depIdStrTemp").val("");
	$("#groupIds").val($("#groupStrTemp").val()); //将群组id字符串还原
	$("#groupStrTemp").val("");
	$(window.frames['flowFrame'].document).find("#right").empty();
	$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
	if ($("#choiceNum").html() == null) {
		$(window.frames['flowFrame'].document).find("#manCount").html("0");
	} else {
		$(window.frames['flowFrame'].document).find("#manCount").html($("#choiceNum").html());
	}
	$(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
	//$("#infoDiv").dialog("close");
	showSelect();
}

// 清空主界面的选择对象（div#sameSendInfo）的数据
function clearUser(){
    $("#ygtxl").remove();
    // 把主页面的数据也清空
    $("#rightSelectedUserOption").val("");
    $("#empIds").val("");
    $("#malIds").val("");
    $("#phoneStr1").val("");
    $("#groupIds").val("");
    $("#depIdStr").val("");
}

function doSelectEClose() {
	$("#infoDiv").dialog("close");
}

function showMenu() {
	var sortSel = $("#groupResouce");
	var sortOffset = $("#groupResouce").offset();
	$("#dropMenu").toggle();
}

function numberControl(va) {
	var pat = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	if (pat.test(va.val())) {
		va.val(va.val().replace(/[^\d]/g, ''));
	}
}
//调用当前页的悬浮框
function floatings(id,id2){
	$("#"+id).hover(
			function(){
				$(this).css("text-decoration","underline");
				$(".ifr").show();
				$("#"+id2).show();
			},function(){
				$("#"+id2).hide();
				$(".ifr").hide();
				$(this).css("text-decoration","none");
			}
	);
	$("#"+id2).hover(
			function(){
				$(".ifr").show();
				$("#"+id2).show();
			},function(){
				$("#"+id2).hide();
				$(".ifr").hide();
			}
	);
}
var subcount = 0;
$(document).ready(
function() {
	initContents();
	if(skin.indexOf("frame4.0") != -1) {
		$("#infomodel").hide();
		floatings("downlinks", "descriptInfo");
	}else{
		floating("downlinks", "infomodel");
	}
	$('#moreSelect').hide();
	$('#u_o_c_explain').toggle(function() {
		$("#foldIcon").removeClass("unfold");
		$("#foldIcon").addClass("fold");
		$('#moreSelect').show();
	}, function() {
		$("#foldIcon").removeClass("fold");
		$("#foldIcon").addClass("unfold");
		$('#moreSelect').hide();
	});

	$("#picTab td").hover(function() {
		//$(this).css("background-color","#edf6ff");
		$(this).removeClass("div_bg");
		$(this).addClass("div_hover_bg");
		$(this).addClass("img_hover");
	}, function() {
		//$(this).css("background-color","#e7effc");
		$(this).removeClass("div_hover_bg");
		$(this).addClass("div_bg");
		$(this).removeClass("img_hover");
	});
	$("#showeffinfo").toggle(function() {
		$("#effinfo").show();
		$("#arrowhead").removeClass("unfold");
		$("#arrowhead").addClass("fold");
		if ($("#messages1").is(":hidden")) {
			$("#effinfotable").css("top", "62px");
		} else {
			$("#effinfotable").css("top", "90px");
		}
	}, function() {
		$("#effinfo").hide();
		$("#arrowhead").removeClass("fold");
		$("#arrowhead").addClass("unfold");
	});
	getLoginInfo("#hiddenValueDiv");
	var findresult = base_findresult;

	if (findresult == "-1") {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_77'));
		return;
	}
	//触发账户change事件
	$("#spUser").trigger("change");
	//短信预览窗口
	$("#detail_Info").dialog({
		autoOpen: false,
		modal: true,
		title: getJsLocaleMessage('dxzs','dxzs_ssend_alert_78'),
		height: 530,
		width: 530,
		closeOnEscape: false,
		open: function() {
			hideSelect();
			//$(".ui-dialog-titlebar-close").hide();
			var ct = $.trim($("#ct").text());
			var yct = $.trim($("#yct").text());
			var isCharg = $("#isCharg").val();
			var spbalance = $.trim($("#spbalance").text());
			var feeFlag = $("#feeFlag").val();
			var gwFee = $("#gwFee").val();
			if(gwFee.substring(0, 9) != "lessgwfee" && gwFee != "feefail" && gwFee != "nogwfee")
			{
				//机构扣费状态，true:正常；false:费用不足
				var depState = true;
				if (isCharg == "true") {
					if (eval(ct) < eval(yct)) {
						depState = false;
						$("#messages1").show();
						$("#btsend").attr("disabled", "disabled");
					} else {
						$("#messages1").hide();
					}
					$("#showyct").show();
				} else {
					$("#showyct").hide();
					$("#messages1").hide();
				}
				//机构扣费正常且SP账号为预付费
				if(feeFlag == 1)
				{
					if(depState){
						//显示SP账号余额
						$("#showyspbalance").show();
						//余额小于发送条新
						if (eval(spbalance) < eval(yct)){
							$("#messages2 font").text(getJsLocaleMessage('dxzs','dxzs_ssend_alert_79'));
							$("#btsend").attr("disabled", "disabled");
						}
					}
				}
				else
				{
					//隐藏SP账号余额
					$("#showyspbalance").hide();
				}
			}
			else
			{
					$("#showyct").hide();
					$("#messages1").hide();
					//隐藏SP账号余额
					$("#showyspbalance").hide();
			}


			//如果预发送条数为0，则也不允许发送，因为有可能此号码未绑定通道。
			if (eval(yct) == 0) {
				$("#btsend").attr("disabled", "disabled");
			}
		},
		close: function() {
			$("#subSend").attr("disabled", "");
			$("#qingkong").attr("disabled", "");
			$(".ui-dialog-titlebar-close").show();
			$("#btsend").attr("disabled", "");
			$("#showyct").show();
			btcancel1();
		}
	});
	var _width;
	var _height;
	if(skin.indexOf("frame4.0") != -1) {
		_width = 690;
		_height = 565;
	}else{
		_width = 535;
		_height = 540;
	}
	$("#infoDiv").dialog({
		autoOpen: false,
		height: _height,
		width: _width,
		resizable: false,
		modal: true,
		open: function() {
			//$(".ui-dialog-titlebar-close").hide();
			hideSelect();
		},
		close: function() {
			if(skin.indexOf("frame4.0") < 0) {
                //0为取消操作；1：为确定操作
                var hidIsDoOk = $("#hidIsDoOk").val();
                if (hidIsDoOk === "0") {
                    doNo();
                }
                $("#hidIsDoOk").val("0");
			}
		}
	});

	$("#draftDiv").dialog({
        autoOpen: false,
        height:500,
        width: 900,
        resizable:false,
        modal: true,
        open:function(){
			//那个等待滚动条会把这个标题栏关掉
			$(".ui-dialog-titlebar").show();
        },
        close:function(){
        }
    });

	setFormName('form2');
});
//发送

function btsend() {

	if($("#splitFlag")&&$("#splitFlag").val()){

		subcount = subcount + 1;
		if (subcount > 1) {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_80'));
			$("form[name='" + formName + "']").attr("action", "");
			return;
		}
		$("#btsend").attr("disabled", true);
		$("#btcancel").attr("disabled", true);
		var nomsg = getJsLocaleMessage('dxzs','dxzs_ssend_alert_36');
		if ($("#taskname").val() == nomsg) {
			$("#taskname").val("");
		}

		var sumLimit = $("#effs").text();
		var sum = 0;
		// 遍历分批设置，验证分批数量是否合理
		var bn = $(".custBatchNode");
		var len = bn.length;
		// 发送短信分批验证失败，清空之前添加到的from表单的分批设置
		$(".hiddenBatchNode").remove();
		for (var i = 0; i < len; i++) {
			var batchNodeValue = $(bn[i]).find(".batchNodeValue").val();
			var batchNodeTimeValue = $(bn[i]).find(".batchNodeTimeValue").val();
			if(undefined != batchNodeValue){
				if(""==$.trim(batchNodeValue)||parseInt(batchNodeValue) == 0){
					alert("分批设置不合理！");
					subcount = 0;
					$("#btsend").attr("disabled", "");
					$("#btcancel").attr("disabled", "");
					return;
				}
				//bug 在预览效果页将发送时间清空，点击‘发送’，提示‘任务创建失败：提交短信任务失败
				if("" == $.trim(batchNodeTimeValue)){
					alert("分批时间设置不合理！");
					subcount = 0;
					$("#btsend").attr("disabled", "");
					$("#btcancel").attr("disabled", "");
					return;
				}
				// if(isNaN(Date.parse(batchNodeTimeValue))){
				// 	alert("分批发送时间设置不合理！");
				// 	subcount = 0;
				// 	$("#btsend").attr("disabled", "");
				// 	$("#btcancel").attr("disabled", "");
				// 	$(bn[i]).find(".batchNodeTimeValue").addClass("WdateFmtErr");
				// 	return;
				// }
				sum += parseInt(batchNodeValue);
				// 将分批设置添加到form表单，便于提交到后台
				$("form[name='" + formName + "']").append("<input class='hiddenBatchNode' name='batchNode" + i + "' type='hidden' value='" + batchNodeValue + "'/>");
				$("form[name='" + formName + "']").append("<input class='hiddenBatchNode' name='batchNodeTimeValue" + i + "' type='hidden' value='" + batchNodeTimeValue + "'/>");
			}
		}
		if(sum>sumLimit){
			alert("分批总数不能超过有效号码数！");
			subcount = 0;
			$("#btsend").attr("disabled", "");
			$("#btcancel").attr("disabled", "");
			return;
		}
		if(sum!=sumLimit){
			alert("分批总数与有效号码数不一致！");
			subcount = 0;
			$("#btsend").attr("disabled", "");
			$("#btcancel").attr("disabled", "");
			return;
		}

		$("form[name='" + formName + "']").append("<input  class='hiddenBatchNode' name='batchNodeNum' type='hidden' value='"+ len +"' />");
		$.post("common.htm?method=getServerTime", {

		}, function(msg) {

			var serverTime = msg;
			var date2 = new Date(Date.parse(serverTime.replaceAll("-", "/")));
			var isExpire = false;
			if($("#splitFlag")&&$("#splitFlag").val()){
				var bn = $(".custBatchNode");
				var len = bn.length;
				var timingDate;
				for (var i = 0; i < len; i++) {
					var bntv = $(bn[i]).find(".batchNodeTimeValue");
					var batchNodeTimeValue = bntv.val();
					timingDate = new Date(Date.parse(batchNodeTimeValue.replaceAll("-", "/")));
					// 定时时间应该在当前时间一分钟之后，将服务器时间减去1分钟之后与定时时间比较做判断
					timingDate.setMinutes(timingDate.getMinutes()-1, 0, 0);
					if(timingDate<=date2){
						isExpire = true;
						bntv.addClass("WdateFmtErr");
					}
				}
			}

			if (isExpire) {
				alert("定时时间不能小于服务器时间，且应在当前时间1分钟之后！");
				subcount = 0;
				showSelect();
				$("#subSend").attr("disabled", "");
				$("#qingkong").attr("disabled", "");
				$("#btsend").attr("disabled", "");
				$("#btcancel").attr("disabled", "");
				return;
			} else {
				$.post("common.htm?method=getSpUserTimePeriod", {
					spUser:$("#spUser").val()
				}, function(spUserTimePeriod) {

					if(spUserTimePeriod.length>0 && ("00:00:00-23:59:59" == spUserTimePeriod)){

					}else{

						var startSpUserTimeDate = new Date();
						var endSpUserTimeDate = new Date();
						var startSpUserTime = spUserTimePeriod.split("-")[0];
						startSpUserTimeDate.setHours(startSpUserTime.split(":")[0], startSpUserTime.split(":")[1], startSpUserTime.split(":")[2], 0);
						var endSpUserTime = spUserTimePeriod.split("-")[1];
						endSpUserTimeDate.setHours(endSpUserTime.split(":")[0], endSpUserTime.split(":")[1], endSpUserTime.split(":")[2], 0);

						if($("#splitFlag")&&$("#splitFlag").val()){
							var bn = $(".custBatchNode");
							var len = bn.length;
							var timingDate;
							for (var i = 0; i < len; i++) {
								var bntv = $(bn[i]).find(".batchNodeTimeValue");
								var batchNodeTimeValue = bntv.val();
								timingDate = new Date(Date.parse(batchNodeTimeValue.replaceAll("-", "/")));
								if(startSpUserTimeDate>timingDate || timingDate>endSpUserTimeDate){
									isExpire = true;
									bntv.addClass("WdateFmtErr");
								}
							}
						}
					}

					if (isExpire) {
						alert("定时时间需要在sp账号发送时间("+spUserTimePeriod+")之内，请重新设置！");
						subcount = 0;
						showSelect();
						$("#subSend").attr("disabled", "");
						$("#qingkong").attr("disabled", "");
						$("#btsend").attr("disabled", "");
						$("#btcancel").attr("disabled", "");
						return;
					} else {
						if ($("#effs").text() == "0") {
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_128'));
							$("#btsend").attr("disabled", "");
							$("#btcancel").attr("disabled", "");
						} else {
							$("#btsend").attr("disabled", true);
							$("#btcancel").attr("disabled", true);
							$("form[name='" + formName + "']").attr("target", "_self");
							$("form[name='" + formName + "']").attr("action", "ssm_" + base_reTitle + ".htm?method=add");
							if ($("form[name='" + formName + "']").attr("encoding")) {
								$("form[name='" + formName + "']").attr("encoding", "application/x-www-form-urlencoded");
							} else {
								$("form[name='" + formName + "']").attr("enctype", "application/x-www-form-urlencoded");
							}
							$.post('common.htm?method=frontLog',{
								info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_47')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val()
								+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
								+'，spUser:'+$("#spUser").val()
							});
							isSend = 1;
							$("form[name='" + formName + "']").submit();
						}
					}

				});
			}

		});

	}else{
		subcount = subcount + 1;
		if (subcount > 1) {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_80'));
			$("form[name='" + formName + "']").attr("action", "");
			return;
		}
		$("#btsend").attr("disabled", true);
		$("#btcancel").attr("disabled", true);
		var nomsg = getJsLocaleMessage('dxzs','dxzs_ssend_alert_36');
		if ($("#taskname").val() == nomsg) {
			$("#taskname").val("");
		}
		var sendTime = $('#timerTime').val();
		var serverTime;
		$.post("common.htm?method=getServerTime", {

		}, function(msg) {
			serverTime = msg;
			var date1 = new Date(Date.parse(sendTime.replaceAll("-", "/")));
			var date2 = new Date(Date.parse(serverTime.replaceAll("-", "/")));
            date1.setMinutes(date1.getMinutes() - 1, 0, 0);
			if (date1 <= date2  && $("input[name=timerStatus]:checked").val() == 1) {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_20'));
				$("#timerTime").val("");
				showSelect();
				$("#subSend").attr("disabled", "");
				$("#qingkong").attr("disabled", "");
				$("#detail_Info").dialog("close");
				$("#btsend").attr("disabled", "");
				$("#btcancel").attr("disabled", "");
				return;
			} else {
				if ($("#effs").text() == "0") {
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_128'));
					$("#btsend").attr("disabled", "");
					$("#btcancel").attr("disabled", "");
				} else {
					$("#btsend").attr("disabled", true);
					$("#btcancel").attr("disabled", true);
					$("form[name='" + formName + "']").attr("target", "_self");
					$("form[name='" + formName + "']").attr("action", "ssm_" + base_reTitle + ".htm?method=add");
					if ($("form[name='" + formName + "']").attr("encoding")) {
						$("form[name='" + formName + "']").attr("encoding", "application/x-www-form-urlencoded");
					} else {
						$("form[name='" + formName + "']").attr("enctype", "application/x-www-form-urlencoded");
					}
					$.post('common.htm?method=frontLog',{
						info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_47')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val()
						+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
						+'，spUser:'+$("#spUser").val()
					});
					isSend = 1;
					$("form[name='" + formName + "']").submit();
				}
			}
		});
	}


}
//取消

function btcancel1() {
	showSelect();
	$("#subSend").attr("disabled", "");
	$("#qingkong").attr("disabled", "");
	//$("#detail_Info").dialog("close");
}

function previewCancel() {
	$("#detail_Info").dialog("close");
}

function infomodelop() {
	$("#infomodel").dialog({
		modal: true,
		title: getJsLocaleMessage('dxzs','dxzs_ssend_alert_81'),
		height: 300,
		closeOnEscape: false,
		width: 500,
		close: function() {}
	});
}

function Pro(u, a) {
	parent.$("#processbar").progressBar(u * 100 / a);
}

function checkMaxNum(maxNum) {
	var maxNums = parseInt(maxNum);
	if (maxNums > 1000000) {
		parent.$("#counts").text();
		parent.$("#effs").text();
		parent.$("#blacks").text();
		parent.$("#legers").text();
		parent.$("#sames").text();
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_82'));
		parent.$("#subSend").attr("disabled", false);
		parent.$("#qingkong").attr("disabled", false);
		parent.$("#detail_Info").dialog("close");
		return false;
	}
	return true;
}

function addph() {
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}

	var phone = $.trim($("#tph").val());
	if (phone.length == 11 && phone.substring(0,1) != "+" && phone.substring(0,2) !="00") {
		$.post("common.htm?method=filterPh&tmp="+phone, {
		}, function(data) {
			if (data == "false") {} else {
				//遍历你窗口已存在的手机号码，并放入一个数组中保存
				var hen = new Array();
				//合法
				var trs = $("#vss").children().children();
				$.each(trs, function(i, n) {
					var tds = $(n).children();
					$.each(tds, function(j, d) {
						if (j == 1) {
							var ps = $(d).text();
							hen[i] = ps;
						}
					});
				});
				var flag = "0";
				for (var g = 1; g < hen.length; g++) {
					// 判断新增的手机号码是否已存在
					if (phone == hen[g]) {
						flag = "1";
						break;
					}
				}
				//看看状态
				if (flag != "1") {

					$("#vss").append("<tr  class='div_bd' id='" + phone + "' style='background-color:#ffffff'><td  style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_129')+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px' style='border-right:0px'><a onclick=ddll('" + phone + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
					$("#phoneStr2").val($("#phoneStr2").val() + phone + ",");
					$("#tph").val("");
				} else {}
			}
		});
	}
}

function addph2() {
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}
	$("#fuzhi").val("fdasfasdfasfdds"); //此句没有实际功能，站位
	$("#fuzhi").val("fdasfasdfasfdds"); //此句没有实际功能，站位
	$("#fuzhi").val("fdasfasdfasfdds"); //此句没有实际功能，站位
	var phone = $("#tph").val();
	var alm = getJsLocaleMessage('dxzs','dxzs_ssend_alert_130');
	if (phone != "" && phone != alm) {
		if (phone.length < 7 || phone.length > 21) {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_131'));
			$("#tph").focus();
		} else {
			$.post("common.htm?method=filterPh", {
			tmp : phone
			}, function(data) {
				if (data == "false") {
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_83')+phone+getJsLocaleMessage('dxzs','dxzs_ssend_alert_84'));
					$("#tph").focus();
				} else {
					//遍历你窗口已存在的手机号码，并放入一个数组中保存
					var hen = new Array();
					//合法
					var trs = $("#vss").children().children();
					$.each(trs, function(i, n) {
						var tds = $(n).children();
						$.each(tds, function(j, d) {
							if (j == 1) {
								var ps = $(d).text();
								hen[i] = ps;
							}
						});
					});
					var flag = "0"
					for (var g = 1; g < hen.length; g++) {
						// 判断新增的手机号码是否已存在
						if (phone == hen[g]) {
							flag = "1";
							break;
						}
					}
					//看看状态
					if (flag != "1") {
						if(phone.substring(0,1) == "+")
						{
							$("#vss").append("<tr class='div_bd' id='" + "GJ" + phone.substring(1) + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_129')+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=ddll('" + "GJ" + phone.substring(1) + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
						}
						else
						{
							$("#vss").append("<tr class='div_bd' id='" + phone + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_129')+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=ddll('" + phone + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
						}
						$("#phoneStr2").val($("#phoneStr2").val() + phone + ",");
					} else {
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_85'));
					}
					$("#tph").val("");
				}
			});
		}
	} else {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_35'));
	}
	//$("#tph").val("");
}


function abc(ag, phone, names) {
	if (confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76'))) {
		if (ag.indexOf("yt") > -1) {
			$("#" + ag + "").remove();
			$("#phoneStr2").val($("#phoneStr2").val().replace(phone + ",", ""));
		} else {
			$("#" + ag + "").remove();
			if (names.indexOf(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76')) > -1) {
				$("#depIdStr").val($("#depIdStr").val().replace("e" + phone + ",", ""));
			} else {
				$("#depIdStr").val($("#depIdStr").val().replace(phone + ",", ""));
			}
		}
	}
}
var flag = true;
var isFile = true;
//浏览按纽事件 暂时不用

function ch() {
	var obj = $("#numFile");
	var pathValue = "";
	pathValue = $("#numFile").val();
	var ip = $("#11").attr('id');
	var index = pathValue.lastIndexOf("\\");
	var name = pathValue.substring(index + 1);
	if (checkFile()) {
		$("#11").remove();
		$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='11'><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_132')+"</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>" + name + "</td><input type='hidden' value='FileTxt'  /><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick='ddll(11)'><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	}
}
// 验证上传文件格式

function checkFile_old(file) {
	var fileObj = $("#" + file);
	if (fileObj.val() != "") {
		var fileName = fileObj.val();
		var index = fileName.lastIndexOf(".");
		var fileType = fileName.substring(index + 1).toLowerCase();

		var trs = $("#vss").children().children();
		//判断文件重复
		var repeatFlag = false;
		var repeatCount = 0;
		$.each(trs, function(i, n) {
			var name = $($(n).children()[1]).html();
			if (name.indexOf(fileName) > -1) {
				repeatCount++;
			}
		});
		if (repeatCount > 1) {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_87'));
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		}

		if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar"&& fileType != "csv") {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_88'));
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		} else {
			return true;
		}
	} else {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_89'));
		return false;
	}
}
// 验证上传文件格式

function checkFile(name) {
	var fileObj = $("form[name='" + formName + "']").find("input[name='" + name + "']");
	if (fileObj.val() != "") {
		var fileName = fileObj.val();
		var index = fileName.lastIndexOf(".");
		var fileType = fileName.substring(index + 1).toLowerCase();
		var trs = $("#allfilename").html();

		if (trs.indexOf(fileName+";") > -1) {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_87'));
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		}

		if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar" && fileType != "csv") {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_88'));
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		} else {
			return true;
		}
	} else {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_89'));
		return false;
	}
}
//过滤文件名中的非法字符

function checkForm(name) {
	var patrn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	if (patrn.test(name)) {
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_90')+"(,.(){})！");
		var file = $(":file");
		file.after(file.clone().val(""));
		file.remove();
		return false;
	}
	return true;
}

function reloadPage() {
	getLoginInfo("#hiddenValueDiv");
	var menuCode = base_menuCode;
	if (menuCode.indexOf("1050-1000") >-1) {
		window.location.href = 'ssm_sendBatchSMS.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
	} else {
		window.location.href = 'ssm_ssendBatchSMS.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
	}
}

//加载机构人员数据

function ok() {
	var a = $("#queryString").val();
	var hen = new Array();
	//合法
	var trs = $("#vss").children().children();
	$.each(trs, function(i, n) {

		var tds = $(n).children();
		$.each(tds, function(j, d) {
			if (j == 1) {
				var ps = $(d).text();
				hen[i] = ps;
			}

		});
	});
	var cc = a.split(',');

	//遍历人员
	for (var x = 0; x < cc.length; x++) {
		var ab = cc[x].toString();
		var qp = ab.split(';');
		//判断是否是机构 qp.length=3是机构,=4是人员
		if (qp.length == 4) {
			var pid = qp[0].split(':');
			var idp = pid[1].toString().substring(1);
			var ge = qp[1].split(':');
			var gs = qp[2].split(':');
			if (gs[1] != "") {
				var flag = "0"
				for (var g = 1; g < hen.length; g++) {
					if (gs[1] == hen[g]) {
						flag = "1";
						break;
					}
				}
				if (flag != "1") {
					$("#vss").append("<tr class='div_bd' id='" + idp + "' ><td align='center' name='ConR'  valign='middle'>" + ge[1] + "<input  type='hidden' value ='" + gs[1] + "' id='" + gs[1] + "' name='bts'/></td><td align='center' valign='middle' >" + gs[1] + "</td><td height='20px' align='center' valign='middle'><a onclick='abc(" + idp + "," + gs[1] + ")' ><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
					$("#phoneStr2").val($("#phoneStr2").val() + gs[1] + ",");
				}
			}
		}
	}
	$("#infoDiv").dialog("close");
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
            $('#showtailcontent').text('');
            $('#tailcontents').val('');
        },
        success:function(data){
            data = eval('('+data+')');
            if(data.status == 1){//找到对应贴尾
                $('#showtailcontent').text(data.contents);
                $('#tailcontents').val(data.contents);
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

function getGateNumber() {
	// $("#subSend").attr("disabled", "disabled");
	var isReply = $("input:radio[name='isReply']:checked").attr("value");
	var spUser = $("#spUser").val();
	setGtInfo(spUser);
	if (spUser == null || spUser == "") {
		return;
	}
    //设置贴尾信息
    if(!setTailInfo())
    {
        return;
    }
	//点不用回复，不请求后台
	if (isReply == "0") {
		$("#subno").html("");
		$("#subNo").val("");
		$("#curSubNo").css("display", "none");
		$("#subSend").attr("disabled", "");
		return;
	}

	var circleSubNo = $("#circleSubNo").val();
	var taskId = $("#taskId").val();
	var lgguid = $("#lgguid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	$.post("common.htm?method=getSubNo", {
		spUser: spUser,
		lgguid: lgguid,
		lgcorpcode: lgcorpcode,
		isReply: isReply,
		circleSubNo: circleSubNo,
		taskId: taskId
	}, function(result) {
		if (result == "noSubNo") {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_91'));
			$("#subno").html("");
			$("#subNo").val("");
			return;
		} else if (result == "noUsedSubNo") {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_92'));
			$("#subno").html("");
			$("#subNo").val("");
			return;
		} else if (result == "error") {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_93'));
			$("#subno").html("");
			$("#subNo").val("");
			return;
		}
		if (isReply != 0) {
			$("#curSubNo").css("display", "inline");
		} else {
			$("#curSubNo").css("display", "none");
		}

		if (result != null && result != "") {
			var index = result.indexOf("}");
			result = result.substring(0, index + 1);
			var data = eval("(" + result + ")");
			$("#subNo").val(data.subNo);
			$("#subno").html(data.subNo);
			var sendFlag = (data.sendFlag).split("&");
			if (sendFlag[0] == "false" || sendFlag[1] == "false" || sendFlag[2] == "false") {
				$("#subno").css("color", "red");
			} else {
				$("#subno").css("color", "blue");
			}
			$("#sendFlag").val(data.sendFlag);
			if (isReply == 1) {
				$("#circleSubNo").val(data.subNo);
			}
		}
		$("#subSend").attr("disabled", "");
	});
}
var fileCount = 1;

function addFiles_old() {
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}
	fileCount++;
	$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='tr" + fileCount + "'><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_132')+"</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'><input style='width:200px' type='file' id='file" + fileCount + "' onchange=checkFile('file" + fileCount + "') name='file" + fileCount + "' value=''/></td><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick=ddll('tr" + fileCount + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	$("#chooseFiles").val(getJsLocaleMessage('dxzs','dxzs_ssend_alert_94'));
}

function checkbigfile(){
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		$("#numFile1").attr('disabled', 'disabled');
		return;
	}
}
function createtaskid(num){
	$.post("common.htm?method=createtaskid", {
		num : num
    },function(result) {
	if(result!=null&&result!='null'&&result!='error'){
		$("#bigtaiskids").val(result);
	}else{
		alert("生成taskid失败");
	}
    });
}
function addFiles() {
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}
	var obj = $("#numFile" + fileCount);
	var pathValue = "";
	pathValue = $("#numFile" + fileCount).val();
	var index = pathValue.lastIndexOf("\\");
	var name = pathValue.substring(index + 1);
	if (name.length > 12) {
		name = name.substring(0, 12) + "....";
	}
	if (checkFile("numFile" + fileCount)) {
		if ($("#tr" + fileCount).length == 0) {
			$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='tr" + fileCount + "'><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_132')+"</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>" + name + "</td><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick=ddll('tr" + fileCount + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
			$("#numFile" + fileCount).css("display", "none");
			fileCount++;
			var ss = $("#filesdiv").html();
			var ss1 = "<input type='file' id='numFile" + fileCount + "' name='numFile" + fileCount + "' value='' onchange='addFiles();' class='numfileclass'";
			if ((base_menuCode.indexOf("1050-1000")>-1 && base_menu == null) || base_menuCode.indexOf("2300-1100")>-1) {
				var style = " style='height:64px;width:64px'";
			} else {
				var style = " style='height:64px;width:64px'";
			}
			ss1 += style + "/>";
			$("#filesdiv").prepend(ss1);
			$("#allfilename").append(pathValue + ";");
		}
	}
}
function delbigfile(id) {

	if (confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76'))) {
		//$("#" + id).remove();
		$(".bigFileTr").remove();
		$("#bigtaiskids").val("");
		$("#bigfileid").val("");
		$("#busCode").attr("disabled", false);
		$("#numFile1").attr('disabled', false);


	}
}

function ddll(idd) {
	if (idd.indexOf("tr") == 0) {
		if (confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76'))) {
			$("#" + idd).remove();
			var filename = idd.replace("tr", "numFile");
			var trs = $("#allfilename").html();
			trs = trs.replace($("#" + filename).val() + ";", "");
			$("#" + filename).remove();
			$("#allfilename").html(trs);
		}
	} else {
		if (confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76'))) {
			$("#" + idd + "").remove();
			if(idd.substring(0,2) == "GJ")
			{
				idd = "+" + idd.substring(2);
			}
			$("#phoneStr2").val($("#phoneStr2").val().replace(idd + ",", ""));
		}
	}
}

function inputTipText() {
	//所有样式名中含有graytext的input
	$("input[class*=graytext]").each(function() {
		var oldVal = $(this).val(); //默认的提示性文本
		$(this).css({
			'color': '#ccc'
		}) //灰色
		.focus(function() {
			if ($(this).val() != oldVal) {
				$(this).css({
					'color': '#000'
				})
			} else {
				$(this).val('').css({
					'color': '#ccc'
				})
			}
		}).blur(function() {
			if ($(this).val() == "") {
				$(this).val(oldVal).css({
					'color': '#ccc'
				})
			}
		}).keydown(function() {
			$(this).css({
				'color': '#000'
			})
		})
	})
}

$(function() {
	inputTipText(); //直接调用就OK了
})
//详情下载

function uploadbadFiles() {
	var badurl = $("#badurl").val();
	badurl = badurl.replace("_view.txt", "_bad.txt");
	$.post("common.htm?method=checkFile", {
				url : badurl,
				upload : "0"
	},
	function(result) {
			if (result == "true") {
                //来源emp节点
                download_href("doExport.hts?u="+badurl);
			} else if (result == "false")
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_95'));
			else
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_96'));
	});
}
//发送成功跳转群发任务查看界面
 function sendRecord(menuCode, taskid, lguserid, lgcorpcode)
 {
	 if(menuCode == "1050-1000")
	 {
	 	menuCode = "1050-1200";
	 }
	 else
	 {
	 	menuCode = "2300-1400";
	 }
	closemessage();
	EmpExecutionContext.log(base_path+"/smt_smsSendedBox.htm?method=find",{taskid:taskid,lguserid:lguserid,lgcorpcode:lgcorpcode});
	window.parent.openNewTab(menuCode,base_path+"/smt_smsSendedBox.htm?method=find&taskid="+taskid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
 }
 function closemessage()
 {
	 $("#message").dialog("close");
 }

 //批量输入
 function bulkImport(){
	 var tex=$("#vss").html();
	 if(tex.indexOf("delbigfile")>-1){
			alert("超大文件发送不能和其他方式混合发送!");
			return;
	 }
	 var _height;
	 if(skin.indexOf("frame4.0") != -1) {
		_height = 435;
	 }else{
		_height = 415;
	 }
	 $('#bulkImport_box').dialog({
				autoOpen: false,
				height: _height,
				width: 542,
				resizable:false,
				modal:true
			});
	$('#bulkImport_box').dialog('open');
/**	if($('.bultPhone').size()==0){
		$('#bNum').html('0');
	}*/
	$('#importArea').blur();
	$('#importArea').html($('#importAreaTemp').html());
	// var num=$('#importAreaTemp').attr('data-num');
	// num=typeof num=='undefined' ? 0 : num;
	// $('#bNum').html(num);
 }
 function bultCancel(){
	 $('#bulkImport_box').dialog('close');
	 $('#importArea').html("");
 }

function formatTelNum(element){

	var str=$.trim($('#importArea').val());

	var reg=/[\s,、\n，]+/g;
	var result=str.replace(reg," ");
	var arr=result.split(" ");
	if(arr[arr.length-1]==""){
		var len=arr.length-1;
	}else{
		var len=arr.length;
	}
	$(element).html(len);
}
function previewTel(){
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}
	if($('#importArea').val()==''){
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_39'));
		return false;
	}
	var phone=$('#bNum').html();
	//批量输入最大支持20000个号码
	if(20000 - phone < 0)
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_97'));
		return false;
	}
	$('.bultPhone').remove();
	var str="<tr class='div_bd bultPhone' id='" + phone + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_133')+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td>"+
		"<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+
		"<a onclick='javascript:bulkImport();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_125')+"(<label style='color:#0e5ad1' id='BatchInput'>" + phone + "</label>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_114')+")</font></a></td>"+
		"<td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=delinputphone('" + phone + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>";
	$("#vss").append(str);

	var inputphone=$('#importArea').val();
	$('#importAreaTemp').html(inputphone).attr('data-num',phone);
	var reg=/[\s,、\n，]+/g;
	var result=inputphone.replace(reg,",");
	if(result.substr(0,1)==",")
	{
		result = result.substr(1);
	}
	$("#inputphone").val(result);
	bultCancel();
}
function display(optionID){
	var all_options = document.getElementById("busCode").options;
	for (i=0; i<all_options.length; i++){
	      if (all_options[i].id == optionID)
	      {
	         all_options[i].selected = true;
	      }
	 }
}
function addbigfile(ids,buscode){
	var tex=$("#vss").html();
	//如果发超大文件，不允许其他方式发送
	//alert(tex);
	//delbigfile
	if(tex.indexOf("delAddr")>-1||tex.indexOf("ddll")>-1||tex.indexOf("delinputphone")>-1||tex.indexOf("delRow")>-1){
		//javascript:delAddr()
		//ddll('tr1')
		//delinputphone('1')
		//ddll('122')
		//delRow(this);
		alert("超大文件发送不能和其他方式混合发送，请重新选择!");
		return;
	}
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送,只能单个发送!");
		return;
	}
	var str="<tr class='div_bd bigFileTr' id='" + ids + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>超大文件<input type='hidden' value ='" + ids + "' id='btbigfile' name='btbigfile'/></td>"+
	"<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+
	"<a onclick='' style='cursor:pointer'><font style='color:#0e5ad1'>"+ids+"</font></a></td>"+
	"<td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=delbigfile('" + ids + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>";
    $("#vss").append(str);
    display(buscode);
    $("#busCode").attr("disabled", true);
}
//数组去重
function distinctArray(arr){
	var tempArr=[],obj={};
	for(var i=0;i<arr.length;i++){
		if(!obj[arr[i]] && arr[i]!==''){
			tempArr.push(arr[i]);
			obj[arr[i]]=true;
		}
	}
	return tempArr;
}

function delinputphone(phone) {
	if (confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76'))) {
		$("#" + phone).remove();
		$("#inputphone").val("");
		$("#importArea").val("");
		$('#importAreaTemp').val("").attr('data-num',0);
	}
}

function setDefault()
{
	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_98'))) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var busCode = $("#busCode").val();
		var priority = $("#priority").val();
		var spUser = $("#spUser").val();
		var isReply = $("input:radio[name='isReply']:checked").attr("value");
		$.post("ssm_comm.htm?method=setDefault", {
			lguserid: lguserid,
			lgcorpcode: lgcorpcode,
			busCode: busCode,
			priority: priority,
			spUser: spUser,
			isReply: isReply,
			flag: "1"
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

//选择草稿箱
function showDraft()
{
	var tex=$("#vss").html();
	if(tex.indexOf("delbigfile")>-1){
		alert("超大文件发送不能和其他方式混合发送!");
		return;
	}
    var tpath = $("#cpath").val();
    var draftstype = 0;
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
        alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_101'));
        return;
    }
    //已存在草稿箱 提示覆盖
    if($('#containDraft').val()){
        if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_102'))){
            $('#containDraft').parents('tr').remove();
            //删除草稿文件后 清空草稿信息
            $('#draftFile').val('');
            $('#draftId').val('');
            $('#draftFileTemp').val('');
        }else{
            $("#draftDiv").dialog("close");
            return false;
        }
    }
    var $tr = $ro.parents('tr');
    //草稿箱发送文件相对路径
    var filePath = $tr.find('td:eq(0)').attr('path');
    var draftId = $ro.val();
    var taskname = $tr.find('td:eq(2)').find('label').text();
    var msg = $tr.find('td:eq(3)').find('label').attr('msg');

    var mobilefileName;
  	if(taskname == null || $.trim(taskname).length == 0)
	{
	    mobilefileName = ""+draftId;
	}
  	else
  	{
  		mobilefileName = draftId + "&nbsp;" + taskname;
  	}
    var trhtml = [];
    trhtml.push("<tr  class='div_bd' style='background-color:#ffffff'>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_103')+"</td>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>");
    trhtml.push("<label>"+mobilefileName+"</label>");
    trhtml.push("<input type='hidden' id='containDraft' name='containDraft' value='1'>");
    trhtml.push("</td>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick='delRow(this);'><img border='0' src='" + $('#cpath').val() + "/common/img/delete.gif' style='cursor:pointer' /></a></td>");
    trhtml.push("</tr>");

    if(filePath != null && $.trim(filePath).length > 0)
    {
    	$("#vss").append(trhtml.join(''));
    }
    $('#draftFile').val(filePath);
    $('#draftFileTemp').val(filePath);
    $('#draftId').val(draftId);
    $('#taskname').css('color','').val(taskname);

    if(msg != null && msg.length > 0)
    {
    	//回填草稿箱内容前 如若存在已选择的模板 则需取消掉模板
    	tempNo();

	    $('#contents').val(msg);
    }
    len($('#contents'));
    $("#draftDiv").dialog("close");
}

/**
 * 删除草稿箱文件行
 * @param obj
 */
function delRow(obj){
    if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76'))){
        $(obj).parents('tr').remove();
        //删除草稿文件后 清空草稿信息

        $('#draftFile').val('');
        $('#containDraft').val(null);
    }
}

function confirmBtn() {
	$("#confirmMsgText").dialog("close");
	checkTimer();
}
function cancelBtn() {
	$("#subSend").attr("disabled","");
	$("#qingkong").attr("disabled","");
	$("#confirmMsgText").dialog("close");
}
