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
	$("#phoneStr1").val("");
	var eIds = "";
	var names = "";
	var mobile = "";
	if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
		alert("您没有选择任何人员！");
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
	$("#vss").append("<tr  class='div_bd' id='ygtxl' style='background-color:#ffffff'><td style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>员工通讯录</td><td style='border-left:0;border-right:0'  align='center' valign='middle' >" + "<a onclick='javascript:showInfo();' style='cursor:pointer'><font style='color:#0e5ad1'>详情(<label style='color:#0e5ad1' id='manCountTemp'></label>人)</font> </a></td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px'>" + "<a  onclick='javascript:delAddr()'><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	$(window.frames['flowFrame'].document).find("#rightSelectTempAll").html($(window.frames['flowFrame'].document).find("#right").html());

	//改变弹出框状态为1
	$("#hidIsDoOk").val("1");

	$("#infoDiv").dialog("close");
	$("#manCountTemp").html($(window.frames['flowFrame'].document).find("#manCount").html());
	showSelect();
}

function delAddr() {
	if (confirm("确认要删除吗? ")) {
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
		alert("您没有选择任何人员！");
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
		alert("加载页面失败,请检查网络是否正常!");
		return;
	}
	//触发账户change事件
	$("#spUser").trigger("change");
	//短信预览窗口
	$("#detail_Info").dialog({
		autoOpen: false,
		modal: true,
		title: '预览效果',
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
							$("#messages2 font").text("SP账号余额不足，不允许发送，请联系管理员充值。");
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
			$("#SendReq").val("0");
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
		alert("提交信息中，请勿重复提交！");
		$("form[name='" + formName + "']").attr("action", "");
		return;
	}
	$("#btsend").attr("disabled", true);
	$("#btcancel").attr("disabled", true);
	if ($("#taskname").val() == "不作为短信内容发送") {
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
			alert("预发送时间小于服务器当前时间！请合理预定发送时间[EXFV011]");
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
				alert("无有效发送号码，无法提交。[EXFV001]");
				$("#btsend").attr("disabled", "");
				$("#btcancel").attr("disabled", "");
			} else {
				$("#btsend").attr("disabled", true);
				$("#btcancel").attr("disabled", true);
				$("form[name='" + formName + "']").attr("target", "_self");
				$("form[name='" + formName + "']").attr("action", "url_" + base_reTitle + ".htm?method=add");
				if ($("form[name='" + formName + "']").attr("encoding")) {
					$("form[name='" + formName + "']").attr("encoding", "application/x-www-form-urlencoded");
				} else {
					$("form[name='" + formName + "']").attr("enctype", "application/x-www-form-urlencoded");
				}
				$.post('common.htm?method=frontLog',{
					info:'相同内容群发，发送提交参数。userId:'+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
					+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
					+'，spUser:'+$("#spUser").val()
				});
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
		alert("您上传的手机号码数超过100万，请重新上传文件！");
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

					$("#vss").append("<tr  class='div_bd' id='" + phone + "' style='background-color:#ffffff'><td  style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>手动添加<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px' style='border-right:0px'><a onclick=ddll('" + phone + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
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
	if (phone != "" && phone !=" 请输入手机号") {
		if (phone.length < 7 || phone.length > 21) {
			alert("手机号码位数为7-21位");
			$("#tph").focus();
		} else {
			$.post("common.htm?method=filterPh", {
			tmp : phone
			}, function(data) {
				if (data == "false") {
					alert("号码"+phone+"为非法号码！");
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
							$("#vss").append("<tr class='div_bd' id='" + "GJ" + phone.substring(1) + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>手动添加<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=ddll('" + "GJ" + phone.substring(1) + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
						}
						else
						{
							$("#vss").append("<tr class='div_bd' id='" + phone + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>手动添加<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >" + phone + "</td><td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=ddll('" + phone + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
						}
						$("#phoneStr2").val($("#phoneStr2").val() + phone + ",");
					} else {
						alert("不能添加相同号码[EXFV019]");
					}
					$("#tph").val("");
				}
			});
		}
	} else {
		alert("手机号码不能为空！");
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
		$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='11'><td  style='border-left:0;border-right:0' align='center' valign='middle' >号码文件</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>" + name + "</td><input type='hidden' value='FileTxt'  /><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick='ddll(11)'><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
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
			alert("该文件已经存在！");
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		}

		if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar"&& fileType != "csv") {
			alert("上传文件格式错误，请选择txt、zip、rar、xls、xlsx、csv或et格式的文件。");
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		} else {
			return true;
		}
	} else {
		alert("请选择上传的文件！");
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
			alert("该文件已经存在！");
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		}

		if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et" && fileType != "rar" && fileType != "csv") {
			alert("上传文件格式错误，请选择txt、zip、rar、xls、xlsx、csv或et格式的文件。");
			fileObj.after(fileObj.clone().val(""));
			fileObj.remove();
			return false;
		} else {
			return true;
		}
	} else {
		alert("请选择上传的文件！");
		return false;
	}
}
//过滤文件名中的非法字符

function checkForm(name) {
	var patrn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	if (patrn.test(name)) {
		alert("提示信息：所选择的上传文件名称含有非法字符(,.(){})！");
		var file = $(":file");
		file.after(file.clone().val(""));
		file.remove();
		return false;
	}
	return true;
}

function reloadPage() {
    var path = $("#cpath").val();
	getLoginInfo("#hiddenValueDiv");
	var menuCode = base_menuCode;

	// if (menuCode == "1050-1000") {
	// 	window.location.href = path + '/url_urlSendBatchSMS.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
	// } else {
		window.location.href = path + '/url_urlSendBatchSMS.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
	// }
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
	$("#subSend").attr("disabled", "disabled");
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
			alert("拓展尾号获取失败！");
			$("#subno").html("");
			$("#subNo").val("");
			return;
		} else if (result == "noUsedSubNo") {
			alert("系统没有可用的拓展尾号！");
			$("#subno").html("");
			$("#subNo").val("");
			return;
		} else if (result == "error") {
			alert("通道号获取失败！");
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
	fileCount++;
	$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='tr" + fileCount + "'><td  style='border-left:0;border-right:0' align='center' valign='middle' >号码文件</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'><input style='width:200px' type='file' id='file" + fileCount + "' onchange=checkFile('file" + fileCount + "') name='file" + fileCount + "' value=''/></td><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick=ddll('tr" + fileCount + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	$("#chooseFiles").val("继续导入");
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
			$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='tr" + fileCount + "'><td  style='border-left:0;border-right:0' align='center' valign='middle' >号码文件</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>" + name + "</td><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick=ddll('tr" + fileCount + "')><img border='0' src='" + base_commonPath + "/common/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
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
		if (confirm("确认要删除吗? ")) {
			$("#" + idd).remove();
			var filename = idd.replace("tr", "numFile");
			var trs = $("#allfilename").html();
			trs = trs.replace($("#" + filename).val() + ";", "");
			$("#" + filename).remove();
			$("#allfilename").html(trs);
		}
	} else {
		if (confirm("确认要删除吗? ")) {
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
				alert("文件不存在或该文件无访问权限");
			else
				alert("出现异常,无法跳转");
	});
}
//发送成功跳转群发任务查看界面
 function sendRecord(menuCode, taskid, lguserid, lgcorpcode)
 {
	
	closemessage();
	EmpExecutionContext.log(base_path+"/surlTaskRecord.htm?method=find",{taskid:taskid,lguserid:lguserid,lgcorpcode:lgcorpcode}); 
	window.parent.openNewTab(menuCode,base_path+"/surlTaskRecord.htm?method=find&taskID="+taskid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
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
		alert('请输入号码');
		return false;
	}
	var phone=$('#bNum').html();
	//批量输入最大支持20000个号码
	if(20000 - phone < 0)
	{
		alert('批量输入号码个数超出范围，最大只支持20000个号码');
		return false;
	}
	$('.bultPhone').remove();
	var str="<tr class='div_bd bultPhone' id='" + phone + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>批量输入<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td>"+
		"<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+
		"<a onclick='javascript:bulkImport();' style='cursor:pointer'><font style='color:#0e5ad1'>详情(<label style='color:#0e5ad1' id='BatchInput'>" + phone + "</label>人)</font></a></td>"+
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
    var frameSrc = tpath+"/common.htm?method=getDrafts&draftstype="+draftstype+"&shorturl=shorturl"+"&timee="+new Date().getTime();
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
    var domainId = $tr.find('td:eq(0)').find('input[id="domainId"]').attr('value');
    var netUrlId = $tr.find('td:eq(0)').find('input[id="netUrlId"]').attr('value');
    var validays = $tr.find('td:eq(0)').find('input[id="validays"]').attr('value');

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
    $('#netUrlId').val(netUrlId);
    $('#domainId').val(domainId);
    $('#vaildays').val(validays);

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

$(function() {  
    /*  在textarea处插入文本--Start */  
    (function($) {  
        $.fn  
                .extend({  
                    insertContent : function(myValue, t) {  
                        var $t = $(this)[0];  
                        if (document.selection) { // ie  
                            this.focus();  
                            var sel = document.selection.createRange();  
                            sel.text = myValue;  
                           this.focus();  
                            sel.moveStart('character', -l);  
                            var wee = sel.text.length;  
                            if (arguments.length == 2) {  
                                var l = $t.value.length;  
                                sel.moveEnd("character", wee + t);  
                                t <= 0 ? sel.moveStart("character", wee - 2 * t  
                                        - myValue.length) : sel.moveStart(  
                                        "character", wee - t - myValue.length);  
                                sel.select();  
                            }  
                        } else if ($t.selectionStart  
                                || $t.selectionStart == '0') {  
                            var startPos = $t.selectionStart;  
                            var endPos = $t.selectionEnd;  
                            var scrollTop = $t.scrollTop;  
                            $t.value = $t.value.substring(0, startPos)  
                                    + myValue  
                                    + $t.value.substring(endPos,  
                                            $t.value.length);  
                            this.focus();  
                            $t.selectionStart = startPos + myValue.length;  
                            $t.selectionEnd = startPos + myValue.length;  
                            $t.scrollTop = scrollTop;  
                            if (arguments.length == 2) {  
                                $t.setSelectionRange(startPos - t,  
                                        $t.selectionEnd + t);  
                                this.focus();  
                            }  
                        } else {  
                            this.value += myValue;  
                            this.focus();  
                        }  
                    }  
                })  
    })(jQuery);  
    /* 在textarea处插入文本--Ending */  
});  
var cacheUrl
$(document).ready(function() {
	$("#insUrl").click(function() {
		var srcUrl = $("#srcUrl").val();
		if(srcUrl==null || srcUrl=="活动页面地址，如：http://www.wal-martchina.com/"){
			alert("没有可插入地址");
			return;
		}
		var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
		var objExp= srcUrl.match(Expression);
		var contents = $("#contents").val(); 
		var objExp2 = contents.match(Expression);
		
		if(objExp==null){
			alert("插入地址格式不对");
			return;
		}
		var text = srcUrl.replace(Expression,"");
		if(text.length>0){
			alert("插入信息包含地址以外内容");
			return;
		}
		if(objExp2 != null && objExp2.length>=4){
			alert("短信内容中包含多个地址");
			return;
		}
		$("#contents").insertContent(" "+srcUrl+" ",0);
	});
})
//点击插入短链接触发方法
function insertUrl(){
	var skin=$("#skin").val();
	
	
	var tpath = $("#cpath").val();
	var frameSrc = $("#shortUrlFrame").attr("src");
//	var lguserid = $("#lguserid").val();
//	var lgcorpcode = $("#lgcorpcode").val();
	var netUrlId = $("#netUrlId").val();
	var domainId = $("#domainId").val();
	var vaildays = $("#vaildays").val();
	
	frameSrc = tpath+"/urlcommonSMS.htm?method=getDomain&netUrlId="+netUrlId+"&domainId="
		+domainId+"&vaildays="+vaildays+"&timee="+new Date().getTime();
	$("#shortUrlFrame").attr("src",frameSrc);
	if(skin.indexOf("frame4.0") != -1){
		$("#shortUrlDiv").dialog({
				autoOpen: false,
				modal: true, 
				width:1000,
				height:560,
				close:function(){
                  eblurNoTrim($("#contents"));
				  $.ajax({
					  type: "post",
					  url: tpath+"/urlcommonSMS.htm",
					  data: {"method":"getLfDomain"},
					  success:function(data){
						  var domains = eval('(' + data + ')');
						  var str="";
						  for(var i in domains){
							  str+="<input type=\"hidden\" value=\""+domains[i].domain+"\" />";
						  }
						  $("#urllist").html(str);
					  }
				  });
				}
		});
		$("#shortUrlFrame").width(950);
	}else{
		$("#shortUrlDiv").dialog({
			autoOpen: false,
			modal: true, 
			width:827,
			height:560,
			close:function(){
			  eblurNoTrim($("#contents"));
			  $.ajax({
				  type: "post",
				  url: tpath+"/urlcommonSMS.htm",
				  data: {"method":"getLfDomain"},
				  success:function(data){
					  var domains = eval('(' + data + ')');
					  var str="";
					  for(var i in domains){
						  str+="<input type=\"hidden\" value=\""+domains[i].domain+"\" />";
					  }
					  $("#urllist").html(str);
				  }
			  });
			}
		});
	}
	$("#shortUrlDiv").dialog("open");
}

function urlSure()
{
	var $fo = $("#shortUrlFrame").contents();
	var srcUrl =  $fo.find("input[type='radio']:checked").next("span").text();
	var cont = $("#contents").val(); 
	$("#contents").focus();
	$("#contents").selection('insert', {text: " "+srcUrl+" ", mode: 'before'});
	//$("#contents").insertContent(" "+srcUrl+" ",0);
	$("#shortUrlDiv").dialog("close");

}

function urlCancel(){
	$("#shortUrlDiv").dialog("close");
}

function showUrl()
{
	var ul = $("#netUrl").val();
	showbox({src:ul , mode:1});

}