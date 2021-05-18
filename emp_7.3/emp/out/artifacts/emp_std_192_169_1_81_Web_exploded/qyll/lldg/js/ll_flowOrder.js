function initContents() {
}

function setDepSign(signObject) {
	var depSign = base_depSign;
	var nameSign = base_nameSign;
	var contents = $("#smsContent").val();
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
			$("#smsContent").val(depSignLeft + depSign + nameSign + depSignRight + str);
		} else {
			$("#smsContent").val(depSignLeft + depSign + depSignRight + contents);
		}
	} else {
		//var str = contents.substring(contents.indexOf(depSignRight) + 1);
		if ($("#nameSign").attr("checked")) {
			$("#smsContent").val(depSignLeft + nameSign + depSignRight + str);
		} else {
			$("#smsContent").val(str);
		}
	}
	eblur($("#smsContent"));
}

function setNameSign(signObject) {
	var depSign = base_depSign;
	var nameSign = base_nameSign;
	var contents = $("#smsContent").val();
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
			$("#smsContent").val(nameSignLeft + depSign + nameSign + nameSignRight + str);
		} else {
			$("#smsContent").val(nameSignLeft + nameSign + nameSignRight + str);
		}
	} else {
		//var str = contents.substring(contents.indexOf(nameSignRight) + 1);
		if ($("#depSign").attr("checked")) {
			$("#smsContent").val(nameSignLeft + depSign + nameSignRight + str);
		} else {
			$("#smsContent").val(str);
		}
	}
	eblur($("#smsContent"));
}

function doOk() {
	$("#phoneStr1").val("");
	var eIds = "";
	var names = "";
	var mobile = "";
	if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_70"));
		return;
	}

	$(window.frames['flowFrame'].document).find("#right option").each(function() {
		eIds = $(this).val();
		names = $(this).text();
		mobile = $(this).attr("mobile");

		if (mobile != null && mobile != "") {
			$("#phoneStr1").val($("#phoneStr1").val() + mobile + ",");
		}
	});
	$("#ygtxl").remove();  
	$("#vss").append("<tr  class='div_bd' id='ygtxl' style='background-color:#ffffff'><td style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("qyll","qyll_lldg_alter_71")+"</td><td style='border-left:0;border-right:0'  align='center' valign='middle' >" + "<a onclick='javascript:showInfo();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage("qyll","qyll_lldg_alter_72")+"(<label style='color:#0e5ad1' id='manCountTemp'></label>"+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+")</font> </a></td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px'>" + "<a  onclick='javascript:delAddr()'><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	$(window.frames['flowFrame'].document).find("#rightSelectTempAll").html($(window.frames['flowFrame'].document).find("#right").html());

	//改变弹出框状态为1
	$("#hidIsDoOk").val("1");

	$("#infoDiv").dialog("close");
	$("#manCountTemp").html($(window.frames['flowFrame'].document).find("#manCount").html());
	showSelect();
}

function delAddr() {
	if (confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_73"))) {
		$("#depIdStr").val(",");
		$("#ygtxl").remove();
		$("#groupStr").val(",");
		$("#phoneStr1").val("");
		$(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
		$(window.frames['flowFrame'].document).find("#manCount").html("0");
	}
}

function doOk1() {
	var eIds = "";
	var names = "";
	var mobile = "";
	if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_70"));
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
			$("#vss").append("<tr id='yt" + eIds + "' ><td align='center' name='ConR'  valign='middle'>" + names + "<input  type='hidden' value ='" + mobile + "' id='" + mobile + "' name='bts'/></td><td align='center' valign='middle' >" + mobile + "</td><td align='center' valign='middle'><a onclick='abc(\"yt" + eIds + "\"," + mobile + ",\"aa\"" + ")' >删除</a></td></tr>");
			$("#phoneStr").val($("#phoneStr").val() + mobile + ",");
		} else {
			$("#vss").append("<tr id='dep" + eIds + "' ><td align='center' name='ConR'  valign='middle'>" + names + "<input  type='hidden' value ='" + eIds + "' id='" + eIds + "' name='bts'/></td><td align='center' valign='middle' >" + '全部号码' + "</td><td align='center' valign='middle'><a onclick='abc(\"dep" + eIds + "\",\"" + eIds + "\",\"" + names + "\")' >删除</a></td></tr>");
		}
	});
	$(window.frames['flowFrame'].document).find("#right").empty();
	$("#infoDiv").dialog("close");
	showSelect();
	$(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
}

function showInfo() {
	$(window.frames['flowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());
	$(window.frames['flowFrame'].document).find("#getChooseMan").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html().replace(/option/gi, "li").replace(/value/g, "dataval"));

	$("#depIdStrTemp").val($("#depIdStr").val());
	$("#groupStrTemp").val($("#groupStr").val());
	//window.frames['flowFrame'].window.frames['sonFrame'].a();
	$(window.frames['flowFrame'].document).find('#sonFrame')[0].contentWindow.a();
	var type = $(window.frames['flowFrame'].document).find("#chooseType").val();
	if (type == 1) {
		$(window.frames['flowFrame'].document).find("#showUserName").html("当前通讯录：");
	} else {
		$(window.frames['flowFrame'].document).find("#showUserName").html("当前群组：");
	}
	$(window.frames['flowFrame'].document).find("#left").empty();
	$(window.frames['flowFrame'].document).find("#depId").val("");
	hideSelect();
	$(window.frames['flowFrame'].document).find('#pageIndex1,#totalPage1').val(1);
	$(window.frames['flowFrame'].document).find('#showPage1').html('');
	$("#infoDiv").dialog("open");
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
	$("#depIdStr").val($("#depIdStrTemp").val()); //将机构id字符串还原
	$("#depIdStrTemp").val("");
	$("#groupStr").val($("#groupStrTemp").val()); //将群组id字符串还原
	$("#groupStrTemp").val("");
	$(window.frames['flowFrame'].document).find("#right").empty();
	$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
	if ($("#manCountTemp").html() == null) {
		$(window.frames['flowFrame'].document).find("#manCount").html("0");
	} else {
		$(window.frames['flowFrame'].document).find("#manCount").html($("#manCountTemp").html());
	}
	$(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
	//$("#infoDiv").dialog("close");
	showSelect();
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
var subcount = 0;
$(document).ready(
function() {
	initContents();
	floating("downlinks", "infomodel");
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
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_76"));
		return;
	}
	//触发账户change事件
	$("#spUser").trigger("change");
	//短信预览窗口
	$("#detail_Info").dialog({
		autoOpen: false,
		modal: true,
		title: getJsLocaleMessage("qyll","qyll_lldg_alter_77"),
		height: 520,
		width: 500,
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
							$("#messages2 font").text(getJsLocaleMessage("qyll","qyll_lldg_alter_29"));
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
	$("#infoDiv").dialog({
		autoOpen: false,
		height: 540,
		width: 535,
		resizable: false,
		modal: true,
		open: function() {
			//$(".ui-dialog-titlebar-close").hide();
			hideSelect();
		},
		close: function() {
			//0为取消操作；1：为确定操作
			var hidIsDoOk = $("#hidIsDoOk").val();
			if (hidIsDoOk == "0") {
				doNo();
			}
			$("#hidIsDoOk").val("0");
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
	subcount = subcount + 1;
	if (subcount > 1) {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_78"));
		$("form[name='" + formName + "']").attr("action", "");
		return;
	}
	$("#btsend").attr("disabled", true);
	$("#btcancel").attr("disabled", true);
	if ($("#taskname").val() == getJsLocaleMessage("qyll","qyll_lldg_alter_79")) {
		$("#taskname").val("");
	}
	var sendTime = $('#timerTime').val();
	var serverTime;
	$.post("common.htm?method=getServerTime", {
	
	}, function(msg) {
		serverTime = msg;
		var date1 = new Date(Date.parse(sendTime.replaceAll("-", "/")));
		var date2 = new Date(Date.parse(serverTime.replaceAll("-", "/")));
		if (date1 < date2) {
			alert( getJsLocaleMessage("qyll","qyll_lldg_alter_1"));
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
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_80"));
				$("#btsend").attr("disabled", "");
				$("#btcancel").attr("disabled", "");
			} else {
				$("#btsend").attr("disabled", true);
				$("#btcancel").attr("disabled", true);
				$("form[name='" + formName + "']").attr("target", "_self");
				$("form[name='" + formName + "']").attr("action", "ll_flowOrder.htm?method=add");
				if ($("form[name='" + formName + "']").attr("encoding")) {
					$("form[name='" + formName + "']").attr("encoding", "application/x-www-form-urlencoded");
				} else {
					$("form[name='" + formName + "']").attr("enctype", "application/x-www-form-urlencoded");
				}
				isSend = 1;
				$("form[name='" + formName + "']").submit();
			}
		}
	});
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
		title: '模板格式查看',
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
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_82"));
		parent.$("#subSend").attr("disabled", false);
		parent.$("#qingkong").attr("disabled", false);
		parent.$("#detail_Info").dialog("close");
		return false;
	}
	return true;
}

function addph() {
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

					$("#vss").append("<tr  class='div_bd' id='" + phone + "' style='background-color:#ffffff'><td  style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("qyll","qyll_lldg_alter_83")+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px' style='border-right:0px'><a onclick=ddll('" + phone + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
					$("#phoneStr2").val($("#phoneStr2").val() + phone + ",");
					$("#tph").val("");
				} else {}
			}
		});
	}
}

function addph2() {
	$("#fuzhi").val("fdasfasdfasfdds"); //此句没有实际功能，站位
	$("#fuzhi").val("fdasfasdfasfdds"); //此句没有实际功能，站位
	$("#fuzhi").val("fdasfasdfasfdds"); //此句没有实际功能，站位
	var phone = $("#tph").val();
	if (phone != "" && phone != getJsLocaleMessage("qyll","dxzs_ssend_alert_130")) {
		if (phone.length < 7 || phone.length > 21) {
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_85"));
			$("#tph").focus();
		} else {
			$.post("common.htm?method=filterPh", {
			tmp : phone
			}, function(data) {
				if (data == "false") {
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_86")+" "+phone+" "+getJsLocaleMessage("qyll","qyll_lldg_alter_87"));
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
							$("#vss").append("<tr class='div_bd' id='" + "GJ" + phone.substring(1) + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("qyll","qyll_lldg_alter_83")+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=ddll('" + "GJ" + phone.substring(1) + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
						}
						else
						{
							$("#vss").append("<tr class='div_bd' id='" + phone + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("qyll","qyll_lldg_alter_83")+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=ddll('" + phone + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
						}
						$("#phoneStr2").val($("#phoneStr2").val() + phone + ",");
					} else {
						alert(getJsLocaleMessage("qyll","qyll_lldg_alter_88"));
					}
					$("#tph").val("");
				}
			});
		}
	} else {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_89"));
	}
	//$("#tph").val("");
}


function abc(ag, phone, names) {
	if (confirm("确认要删除吗? ")) {
		if (ag.indexOf("yt") > -1) {
			$("#" + ag + "").remove();
			$("#phoneStr2").val($("#phoneStr2").val().replace(phone + ",", ""));
		} else {
			$("#" + ag + "").remove();
			if (names.indexOf("包含子机构") > -1) {
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
		$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='11'><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage("qyll","qyll_lldg_alter_90")+"</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>" + name + "</td><input type='hidden' value='FileTxt'  /><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick='ddll(11)'><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
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
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_91"));
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		}

		if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar"&& fileType != "csv") {
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_92"));
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		} else {
			return true;
		}
	} else {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_93"));
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
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		}

		if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar" && fileType != "csv") {
			alert(getJsLocaleMessage("qyll","qyll_lldg_alter_92"));
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		} else {
			return true;
		}
	} else {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_93"));
		return false;
	}
}
//过滤文件名中的非法字符

function checkForm(name) {
	var patrn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	if (patrn.test(name)) {
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_94"));
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
	if (menuCode == "1050-1000") {
		window.location.href = 'll_flowOrder.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
	} else {
		window.location.href = 'll_flowOrder.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
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
	var spUser = $("#sp_User").val();
	setGtInfo(spUser);
}
var fileCount = 1;

function addFiles_old() {
	fileCount++;
	$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='tr" + fileCount + "'><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage("qyll","qyll_lldg_alter_90")+"</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'><input style='width:200px' type='file' id='file" + fileCount + "' onchange=checkFile('file" + fileCount + "') name='file" + fileCount + "' value=''/></td><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick=ddll('tr" + fileCount + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	$("#chooseFiles").val(getJsLocaleMessage("qyll","qyll_lldg_alter_98"));
}

function addFiles() {
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
			$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='tr" + fileCount + "'><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage("qyll","qyll_lldg_alter_90")+"</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>" + name + "</td><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick=ddll('tr" + fileCount + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
			$("#numFile" + fileCount).css("display", "none");
			fileCount++;
			var ss = $("#filesdiv").html();
			var ss1 = "<input type='file' id='numFile" + fileCount + "' name='numFile" + fileCount + "' value='' onchange='addFiles();' class='numfileclass'";
			if ((base_menuCode == "1050-1000" && base_menu == null) || base_menuCode == "2300-1100") {
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


function ddll(idd) {
	if (idd.indexOf("tr") == 0) {
		if (confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_73"))) {
			$("#" + idd).remove();
			var filename = idd.replace("tr", "numFile");
			var trs = $("#allfilename").html();
			trs = trs.replace($("#" + filename).val() + ";", "");
			$("#" + filename).remove();
			$("#allfilename").html(trs);
		}
	} else {
		if (confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_73"))) {
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
	$(".graytext").each(function() {
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
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_100"));
			else
				alert("出现异常,无法跳转");
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
	 $('#bulkImport_box').dialog({
				autoOpen: false,
				height: 415,
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
	var num=$('#importAreaTemp').attr('data-num');
	num=typeof num=='undefined' ? 0 : num;
	$('#bNum').html(num);
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
	if($('#importArea').val()==''){
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_102"));
		return false;
	}
	var phone=$('#bNum').html();
	//批量输入最大支持20000个号码
	if(20000 - phone < 0)
	{
		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_103"));
		return false;
	}
	$('.bultPhone').remove();
	var str="<tr class='div_bd bultPhone' id='" + phone + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("qyll","qyll_lldg_alter_104")+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td>"+
		"<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+
		"<a onclick='javascript:bulkImport();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage("qyll","qyll_lldg_alter_72")+"(<label style='color:#0e5ad1' id='BatchInput'>" + phone + "</label>"+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+")</font></a></td>"+
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
	if (confirm("确认要删除吗? ")) {
		$("#" + phone).remove();
		$("#inputphone").val("");
		$("#importArea").val("");
		$('#importAreaTemp').val("").attr('data-num',0);
	}
}

function setDefault()
{
	if(confirm("确认是否将当前选项设置为默认？ ")) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var busCode = $("#busCode").val();
		var priority = $("#priority").val();
		var spUser = $("#spUser").val();
		var isReply = $("input:radio[name='isReply']:checked").attr("value");
		$.post("ll_comm.htm?method=setDefault", {
			lguserid: lguserid,
			lgcorpcode: lgcorpcode,
			busCode: busCode,
			priority: priority,
			spUser: spUser,
			isReply: isReply,
			flag: "1"			
			}, function(result) {
			if (result == "seccuss") {
				alert("当前选项设置为默认成功！");
				return;
			} 
			else if(result == "fail"){
				alert("当前选项设置为默认失败！");
				return;
			}
		});
	}
}

//选择草稿箱
function showDraft()
{
    var tpath = $("#cpath").val();
    var draftstype = 0;
    var frameSrc = tpath+"/common.htm?method=getDrafts&draftstype="+draftstype+"&timee="+new Date().getTime();
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
        alert("未选择草稿箱！");
        return;
    }
    //已存在草稿箱 提示覆盖
    if($('#containDraft').val()){
        if(confirm("是否覆盖已有的草稿？")){
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
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' valign='middle' >草稿箱</td>");
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
    if(confirm("确认要删除吗？")){
        $(obj).parents('tr').remove();
        //删除草稿文件后 清空草稿信息
        
        $('#draftFile').val('');
        $('#containDraft').val(null);
    }
}

