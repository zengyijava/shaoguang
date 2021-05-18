var docUrl = document.URL;
var urls = docUrl.split("/");
docUrl = urls[0] + "//" + urls[2] + "/" + urls[3];
var inheritPath;
var time;
var dep;
var depName;
var depId;
// **********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var isIE = (document.all) ? true : false;
var isIE6 = isIE
		&& (navigator.appVersion.match(/MSIE 6.0/i) == "MSIE 6.0" ? true
				: false);
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

function showTree(departId, tmethod) {
	inheritPath = $('#inheritPath').val();
	time = new Date();
	dep = window.showModalDialog(inheritPath
			+ "/a_clientDepTree1.jsp?treemethod=" + tmethod + "&timer=" + time
			+ "&isReturnDepId=false&departId=" + departId, "", "dialogWidth="
			+ ieWidth + "px;dialogHeight=" + ieHeight
			+ "px;help:no;status:no;center:yes");
	if (dep == null) {
		dep = "&";
		depName = getJsLocaleMessage('employee','employee_alert_35');
		depId = "";
	} else {
		depName = dep.split("&")[0];
		depId = dep.split("&")[1];
	}

}

$(function() {
	$("#employeeNo").bind('keyup blur',function(){
		var reg=/[<'">]/g;
		var val=$(this).val();
		if(reg.test(val)){
			$(this).val($(this).val().replace(reg,''));
		}
	});
	
	$("#all").click(function(e) {
		$("#depNam").val(getJsLocaleMessage('employee','employee_alert_35'));
		$("#depName").val("");
	});
	$("#cdep").click(function(e) {
		var departId = $("#depId").val() == "" ? 0 : $("#depId").val();
		var tmethod = $("#tmethod").val();
		showTree(departId, tmethod);
		$("#depNam").val(depName);
		$("#depId").val(depId);
		$("#domdepName").attr("value", depName);
		$("#domString").attr("value", depId);
	});
	  //名字长度限制
	$('#cName').bind('propertychange input focus',function(){
          var $that =  $(this),
                  limitLen = 60;                            //定义所需字节数
          $that.attr('maxlength',limitLen);
          setTimeout(function(){
              var value =  $that.val(),
                  reg = /[\u4e00-\u9fa5]{1}/g,             //中文
                  notReg = /\w{1}/g;                      //非中文
              var resultCn = value.match(reg);
              var resultEn = value.match(notReg);
              if(resultCn){
                  limitLen = limitLen - (resultCn.length*2);
              }
              if(resultEn){

                  limitLen = limitLen - resultEn.length;
              }
              if(limitLen<=0){
                  var finalLen = value.length+limitLen;
                  value = value.substring(0,finalLen);
                  $that.val(value);
                  $that.attr('maxlength',limitLen);
              }
          },0);

	})
	 
});

var treeWay = "";
var deptree = document.getElementsByName("tinytreeInputName");

// 复选框多选
function checkAll(e, str) {
	var a = document.getElementsByName(str);
	var n = a.length;
	for ( var i = 0; i < n; i++) {
		a[i].checked = e.checked;
	}
}

// 按照机构搜索信息
function getDepInfo(nodeId) {
	location.href = "getInfoByDep.action?depId=" + nodeId;
}

// 获取选中节点的id集合，返回的id集合格式为 id1,id2,id3
function getNodeId() {
	var id = "";
	var result = $('#com_abjust_list')[0].tinytree.getSelecetedNodes();
	$.each(results, function(i, result) {
		ids += result.nodeId + ",";
	});
	ids = ids.substring(0, ids.lastIndexOf(','));
	return id;
}

// 把链接好的字符串分解为数组
function getNodeIdByString(inString) {
	var str = inString.split(",");
	var nodeId = new Array();
	for ( var i = 0; i < str.length; i++) {
		nodeId[i] = parseInt(str[i]);
	}
	return nodeId;
}

// 返回树的结点ID
function returnId() {
	var results = $('#com_abjust_list')[0].tinytree.getSelecetedNodes();
	// alert(results[0].nodeId);
	if (treeWay == "dom") {
		$("#domString").val(results[0].nodeId);
	} else if (treeWay == "priv") {
		$("#privListString").val(results[0].nodeId);
	}
	document.getElementById("com_add_Dom").style.display = "none";
	document.getElementById("iframeshow2").style.display = "none";
}

// 用List的ID拼接字符串
function splitString(list) {
	var idString = "";
	for ( var i = 0; i < list.length; i++) {
		idString += list[i] + ",";
	}
	idString = idString.substring(0, idString.lastIndexOf(','));
	return idString;
}

function doSub() {
	var bookType = $("#bookType").val();
	var cName = $.trim($("#cName").val());
	// 手机号-新增使用mobile，修改使用tempMobile
	var mobile = $.trim($("#tempMobile").attr("value"));
	if(!mobile){
		mobile = $.trim($("#mobile").attr("value"));
	}
	if(!mobile){
		alert(getJsLocaleMessage('employee','employee_alert_41'));
		return;
	}
	var pageIndex=$("#pageI").val();
	var pagesize=$("#pageS").val();

	if (mobile.indexOf("*") > 0) {
		// 如果含有星号
		mobile = $("#mobile").attr("value");
	} else {
		$("#mobile").val(mobile);
	}
	var depId = $("#depId").val();
	var employeeNo = $.trim($("#employeeNo").val());
	var clientNo = $.trim($("#clientCode").val());
	var email = document.getElementById("EMail").value;
	email = email.replace(/(^\s*)|(\s*$)/g, "");// 去除两端空格
	if (email != "") {
		// 对电子邮箱的验证
		var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if (!myreg.test(email)) {
			alert(getJsLocaleMessage('employee','employee_alert_36'));
			$("#EMail").focus();
			return;
		}
	}
	var comment = $("#comments").val();
	if(comment != ""){
		if(comment.length>150){
			alert(getJsLocaleMessage('employee','employee_alert_37'));
			return;
		}
	}
	
	if (cName == "") {
		alert(getJsLocaleMessage('employee','employee_alert_38'));
		$("#cName").focus();
		return;
	} else if (outSpecialChar(cName)) {
		alert(getJsLocaleMessage('employee','employee_alert_39'));
		$("#cName").focus();
		return;
//	} else if (bookType == "employee" && employeeNo == "") {
//		alert("工号不能为空！");
//		$("#employeeNo").focus();
//		return;
	} else if (depId == "") {
		alert(getJsLocaleMessage('employee','employee_alert_40'));
		return;
	} else if (mobile == "") {
		alert(getJsLocaleMessage('employee','employee_alert_41'));
		$("#mobile").focus();
		return;
	}else if(mobile.length<7||mobile.length>21){
		alert(getJsLocaleMessage('employee','employee_alert_42'));
		$("#mobile").focus();
		return;
	}else if (mobile != "" && !asyncCheckPhone(mobile)) {
		alert(getJsLocaleMessage('employee','employee_alert_42'));
		$("#mobile").focus();
		return;
	} else {
		var isUser = $('input[name="isUser"]:checked').val();
		if (isUser == "1") {
			var val = checkUserData();
			var userId = $("#userId").val();
			if (!val) {
				return;
			}else if(!userId){
				var userName = $.trim($("#userName").attr("value"));
				if (userName == "admin" || userName == "sysadmin") {
					alert(getJsLocaleMessage('employee','employee_alert_43') + userName + getJsLocaleMessage('employee','employee_alert_44'));
					$("#userName").val("");
					return ;
				}
				var userCode = $.trim($("#userCode").attr("value"));
				$.ajax( {
					url : 'opt_sysuser.htm',
					data : {
						username : userName,
						userCode:userCode,
						method : "checkUserNameAndCode",
						lgcorpcode : $("#lgcorpcode").val()
					},
					type : 'post',
					beforeSend: function(){$("#button").attr("disabled",true);},
					success : function(data) {
						if(data == "true"){
							subEmploy(mobile,employeeNo,clientNo,cName,depId);
						}else if (data == "codeExist") {
							//$("#zhu").html("");
							$("#button").attr("disabled", "");
							alert(getJsLocaleMessage('employee','employee_alert_45'));
							return ;
						}else if (data == "nameExist") {
							//$("#zhu").html("");
							alert(getJsLocaleMessage('employee','employee_alert_46'));
							$("#button").attr("disabled", "");
							return ;
						}else {
							//$("#zhu").html("");
							alert(getJsLocaleMessage('employee','employee_alert_47'));
							$("#button").attr("disabled", "");
							return ;
						}
					}
				});
			}else{
				subEmploy(mobile,employeeNo,clientNo,cName,depId);
			}
		}else{
			subEmploy(mobile,employeeNo,clientNo,cName,depId);
		}
		
	}
}

function subEmploy(mobile,employeeNo,clientNo,cName,depId){
	//$("#button").attr("disabled", "disabled");
	var checkUrl = $("#checkUrl").val();
	$("#button").attr("disabled",true);
	$.post(checkUrl, {
		mobile : mobile,
		eNo : employeeNo,
		cNo : clientNo,
		name : cName,
		depId : depId,
		bookId : $("#bookId").val(),
		hidOpType : $("#hidOpType").val(),
		depCode : depId
	},
			function(result) {
				if (result.indexOf("html") > 0) {
					alert(getJsLocaleMessage('employee','employee_alert_48'));
					window.location.href = window.location.href;
					return;
				} else if (result != null && result == "numfalse") {
					alert(getJsLocaleMessage('employee','employee_alert_49'));
					$("#button").attr("disabled", "");
					return;
				} else if (result != null && result != "numfalse"
						&& result != "true" && result != "phoneExist"
						&& result != "phoneAndNameExist") {
					$("#button").attr("disabled", "");
					if (result.length > 0) {
						alert(result);
					} else {
						alert(getJsLocaleMessage('employee','employee_alert_11'));
					}
					return;
				} else if (result == "true") {
					$("#addForm").attr(
							"action",
							$("#addForm").attr("action") + "&lguserid="
									+ $("#lguserid").val() + "&lgcorpcode="
									+ $("#lgcorpcode").val());
					$("#addForm").submit();
				} else if (result == "phoneExist"
						|| result == "phoneAndNameExist") {
					var message = "";
					if (result == "phoneAndNameExist") {
						alert(getJsLocaleMessage('employee','employee_alert_50'));
						$("#button").attr("disabled", "");
						return;
					}
					if (result == "phoneExist") {
						message = getJsLocaleMessage('employee','employee_alert_51');
					}

					if (confirm(message)) {

						$("#addForm").attr(
								"action",
								$("#addForm").attr("action") + "&lguserid="
										+ $("#lguserid").val()
										+ "&lgcorpcode="
										+ $("#lgcorpcode").val());
						$("#addForm").submit();
					} else {
						$("#button").attr("disabled", "");
						return;
					}
				}
			});
}
function checkUserData() {
	var haveSubno = $("#haveSubno").val();
	var isGiveSubno = $("#isGiveSubno").val();
	if (isGiveSubno == "1") {
		if (haveSubno == "1") {
			var updateSubno = $("#addSubno").val();
			var userdSubno = $("#subno2").val();
			if (updateSubno == "" || updateSubno.length == 0) {
				alert(getJsLocaleMessage('employee','employee_alert_52'));
				$("#addSubno").val(userdSubno);
				return false;
			}
		}
	} else if (isGiveSubno == "2") {
		if (haveSubno == "1") {
			var updateSubno = $("#addSubno").val();
			var subno2 = $("#subno2").val();
			if (updateSubno == "" || updateSubno.length == 0) {
				alert(getJsLocaleMessage('employee','employee_alert_52'));
				$("#addSubno").val(subno2);
				return false;
			}
		}
	}
	var roleId = document.getElementsByName("roleId");
	for ( var i = 0; i < roleId.length; i = i + 1) {
		if (roleId[i].checked == true) {
			mid = 1;
			break;
		}
		if (i == (roleId.length - 1)) {
			//$("#zhu").html("");
			alert(getJsLocaleMessage('employee','employee_alert_53'));
			openRoleChoose();
			return false;
		}
	}
	var cheRoles = "";
	for ( var i = 0; i < roleId.length; i = i + 1) {
		if (roleId[i].checked == true) {
			cheRoles = cheRoles + roleId[i].value + ",";
		}
	}
	cheRoles = cheRoles.substring(0, cheRoles.length - 1);
	$("#cheRoles").attr("value", cheRoles);
	var userName = $("#userName").val();
	var userCode = $("#userCode").val();

	var userDepId = $("#userDepId").val();
	var domDepId = $("#domDepId").val();
	var domDepId_employ = $("#domDepId_employ").val();
	var domDepId_client = $("#domDepId_client").val();
	var userPerType = $("#userPerType").attr("value");
	var employPerType = $("#employPerType").attr("value");
	var clientPerType = $("#clientPerType").attr("value");
	var iflow = $('input[name="isCheckFlow"]:checked').val();
	var fflow = $("#bindOpeNum").attr("value");
	if (iflow == "n") {
		$("#bindOpeNum").attr("value", "");
	}
	var patrnUserNo=/^(\w){1,15}$/; //字母数字下划线
	var patrnUserCode = /^[0-9a-zA-Z]{1,20}$/;
	
	if (!userName) {
		alert(getJsLocaleMessage('employee','employee_alert_54'));
	}else if(!patrnUserNo.exec(userName)){
		alert(getJsLocaleMessage('employee','employee_alert_55'));
	}else if (!userCode) {
		alert(getJsLocaleMessage('employee','employee_alert_56'));
	} else if(!patrnUserCode.exec(userCode)){
		alert(getJsLocaleMessage('employee','employee_alert_57'));
	}else if (!userDepId) {
		alert(getJsLocaleMessage('employee','employee_alert_58'));
	} else if (userPerType == 2 && !domDepId) {
		alert(getJsLocaleMessage('employee','employee_alert_59'));
	} else if (employPerType == 2 && !domDepId_employ) {
		alert(getJsLocaleMessage('employee','employee_alert_60'));
	} else if (clientPerType == 2 && !domDepId_client) {
		alert(getJsLocaleMessage('employee','employee_alert_61'));
	} else if (iflow == "y" && fflow == "") {
		$("#zhu").html("");
		$("#zhu").html(getJsLocaleMessage('employee','employee_alert_62'));
		$('#flowDiv').dialog("open");
	} else {
		var _userDepId = $('#_userDepId').val();
		var _domDepId = $('#_domDepId').val();
	    if(userPerType == 2 && userDepId !=  domDepId&& (userDepId != _userDepId || domDepId != _domDepId)){
	  	 	var depNode = zTree_user.getNodesByParam("id", userDepId)[0];
	  	 	var domNodes = zTree_user.getNodesByParam("id", domDepId,depNode);
	  	 	if(domNodes.length != 1 || !depNode){
	  	 		alert(getJsLocaleMessage('employee','employee_alert_63'));
	  	 		return false;
	  	 	}
	    }
		return true;
	}
	return false;
}

function doSubClient() {
	// 如果姓名或电话或者机构任何一个修改了 ，此属性就设为true，后台就要进行重复验证，如果都没有变则不需要验证重复性
	var changeFlag = false;
	var bookType = $("#bookType").val();
	var cName = $.trim($("#cName").val());
	var cNameTemp = $.trim($("#cNameTemp").val());
	var mobile = $.trim($("#mobile").val());
	var mobileTemp = $.trim($("#mobileTemp").val());
	var depId = $("#depId").val();
	var depIdTemp = $("#depIdTemp").val();
	var checkUrl = $("#checkUrl").val();
	var employeeNo = $.trim($("#employeeNo").val());
	var clientNo = $.trim($("#clientCode").val());
	var email = document.getElementById("EMail").value;
	email = email.replace(/(^\s*)|(\s*$)/g, "");// 去除两端空格
	if (email != "") {
		// 对电子邮箱的验证
		var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if (!myreg.test(email)) {
			alert(getJsLocaleMessage('employee','employee_alert_36'));
			$("#EMail").focus();
			return;
		}
	}

	if (cName == "") {
		alert(getJsLocaleMessage('employee','employee_alert_38'));
		$("#cName").focus();
		return;
	} else if (outSpecialChar(cName)) {
		alert(getJsLocaleMessage('employee','employee_alert_39'));
		$("#cName").focus();
		return;
	} else if (bookType == "employee" && employeeNo == "") {
		alert(getJsLocaleMessage('employee','employee_alert_64'));
		$("#employeeNo").focus();
		return;
	} else if (depId == "") {
		alert(getJsLocaleMessage('employee','employee_alert_2'));
		return;
	} else if (mobile == "") {
		alert(getJsLocaleMessage('employee','employee_alert_41'));
		$("#mobile").focus();
		return;
	} else if (mobile != "" && mobile.length != 11) {
		alert(getJsLocaleMessage('employee','employee_alert_42'));
		$("#mobile").focus();
		return;
	} else {
		if (mobile.indexOf("*") > -1) {
			$("#mobile").val(mobileTemp);
			mobile = mobileTemp;
		}
		// 如果姓名和电话都没有修改则没有必要验证重复性
		if (cNameTemp != null
				&& mobileTemp != null
				&& depIdTemp != null
				&& (cName != cNameTemp || mobileTemp != mobile || depIdTemp != depId)) {
			changeFlag = true;
		}
		if ($("#actionType").val() == "add") {
			changeFlag = true;
		}
		$("#button").attr("disabled", "disabled");
		$.post(checkUrl, {
			mobile : mobile,
			eNo : employeeNo,
			changeFlag : changeFlag,
			name : cName,
			cNo : clientNo,
			hidOpType : $("#hidOpType").val(),
			depCode : depId
		}, function(result) {
			if (result.indexOf("html") > 0) {
				alert(getJsLocaleMessage('employee','employee_alert_48'));
				window.location.href = window.location.href;
				return;
			} else if (result != null && result == "numfalse") {
				alert(getJsLocaleMessage('employee','employee_alert_49'));
				$("#button").attr("disabled", "");
				return;
			} else if (result != null && result != "numfalse"
					&& result != "phoneNameRepeat" && result != "noRepeat"
					&& result != "true" && result != "phoneRepeat"
					&& result != "phoneExist") {
				$("#button").attr("disabled", "");
				if (result.length > 0) {
					alert(result);
				} else {
					alert(getJsLocaleMessage('employee','employee_alert_11'));
				}
				return;
			} else if (result == "phoneNameRepeat") {
				alert(getJsLocaleMessage('employee','employee_alert_65'));
				$("#button").attr("disabled", "");
				return;
			} else if (result == "true" || result == "phoneRepeat"
					|| result == "noRepeat") {
				var message = getJsLocaleMessage('employee','employee_alert_66');
				if (result == "phoneRepeat") {
					message = getJsLocaleMessage('employee','employee_alert_67');
				}

				if (confirm(message)) {
					$("#addForm").attr(
							"action",
							$("#addForm").attr("action") + "&lguserid="
									+ $("#lguserid").val() + "&lgcorpcode="
									+ $("#lgcorpcode").val());
					$("#addForm").submit();
				} else {
					$("#button").attr("disabled", "");
					return;
				}
			}
		})
	}
}

function changeinfo(type) {
	if (type == '1') {
		$("#addone").removeClass();
		$("#addall").removeClass();
		$("#addone").addClass("infotd1");
		$("#addall").addClass("infotd2");

		$("#addoneDiv").show();
		$("#addallDiv").hide();
	} else {
		$("#addone").removeClass();
		$("#addall").removeClass();
		$("#addall").addClass("infotd1");
		$("#addone").addClass("infotd2");

		$("#addallDiv").show();
		$("#addoneDiv").hide();
	}
}

// 关闭树的下拉框方法（点击别处时关闭）+关闭时显示下拉框
function closeTreeFunSelBook() {
	$(".tree").parent().click(function(e) {
		e.stopPropagation();
	});

	$('html,body').click(function(e) {
		var $obj = $(e.target);
		if ($obj.attr("class").indexOf("treeInput") == -1) {
			$(".tree").parent().css("display", "none");
			$("select").css("visibility", "");
		}
	});
}
/* 检查要添加的操作员登录账号跟操作员编码是否已经存在 */
function checkUser() {
	var userName = $.trim($("#userName").attr("value"));
	if (userName == "") {
		alert(getJsLocaleMessage('employee','employee_alert_54'));
		return false;
	}
	if (userName == "admin" || userName == "sysadmin") {
		alert(getJsLocaleMessage('employee','employee_alert_43') + userNo + getJsLocaleMessage('employee','employee_alert_44'));
		$("#userName").val("");
		return false;
	}
	var userCode = $.trim($("#userCode").attr("value"));
	if (userCode == "") {
		//$("#zhu").html("");
		alert(getJsLocaleMessage('employee','employee_alert_68'));
		return false;
	}
	$.ajax( {
		url : 'opt_sysuser.htm',
		data : {
			username : userName,
			userCode:userCode,
			method : "checkUserNameAndCode",
			lgcorpcode : $("#lgcorpcode").val()
		},
		type : 'post',
		success : function(data) {
			if (data == "true") {
				//$("#zhu").html("");
				//$("#button").attr("disabled", "disabled");
				//$("#Sysuser").submit();
				return true;
			} else if (data == "codeExist") {
				$("#zhu").html("");
				$("#button").attr("disabled", "");
				$("#zhu").html(getJsLocaleMessage('employee','employee_alert_45'));
				return false;
			}else if (data == "nameExist") {
				$("#zhu").html("");
				$("#zhu").html(getJsLocaleMessage('employee','employee_alert_46'));
				$("#button").attr("disabled", "");
				return false;
			}else {
				$("#zhu").html("");
				$("#zhu").html(getJsLocaleMessage('employee','employee_alert_47'));
				$("#button").attr("disabled", "");
				return false;
			}
		}
	});
}
