var zTree_user;
var setting_user;
var zNodes_user = [];
var setting_employ;
var zTree_employ;
var zNodes_employ = [];
var setting_client;
var zTree_client;
var zNodes_client = [];
var setting5 ;
var zTree5;
var zNodes5 =[];
var pathUrl;
$(document).ready(function() {
	pathUrl = $("#pathUrl").val();
	getLoginInfo("#loginUser");
	$('#roDiv').dialog( {
		autoOpen : false,
		height : 310,
		title : getJsLocaleMessage('employee','employee_alert_77'),
		width : 320,
		close : function() {
			// $("select[name='sex']").show();
			// $("select[name='userState']").show();
			// $("select[name='userPerType']").show();
			fillRoleName();
		},
		modal : true
	});

	setting_user = {
		async : true,
		asyncUrl : pathUrl
				+ "/opt_department.htm?method=createTree2&lguserid="
				+ $("#lguserid").val(), // 获取节点数据的URL地址
		isSimpleData : true,
		rootPID : -1,
		treeNodeKey : "id",
		treeNodeParentKey : "pId",
		asyncParam : [ "depId" ],
		callback : {
			click : zTreeOnClick_user,
			asyncSuccess : function(event, treeId, treeNode, msg) {
				if (!treeNode) {
					var rootNode = zTree_user
							.getNodeByParam("level", 0);
					zTree_user.expandNode(rootNode, true, false);
				}
			}
		}
	};

	setting_employ = {
		checkable : true,
		checkStyle : "checkbox",
		checkType : {
			"Y" : "s",
			"N" : "s"
		},
		async : true,
		asyncUrl : "", // 获取节点数据的URL地址
		isSimpleData : true,
		rootPID : -1,
		treeNodeKey : "id",
		treeNodeParentKey : "pId",
		asyncParam : [ "depId" ],
		callback : {
			change : zTreeOnClick_employ,
			asyncSuccess : function(event, treeId, treeNode, msg) {
				if (!treeNode) { // 判断是 顶级机构就展开,其余的收缩+
					var rootNode2 = zTree_employ.getNodeByParam(
							"level", 0);
					zTree_employ.expandNode(rootNode2, true, false);
				}

			}
		}
	};

	setting_client = {
		checkable : true,
		checkStyle : "checkbox",
		checkType : {
			"Y" : "s",
			"N" : "s"
		},
		async : true,
		asyncUrl : "", // 获取节点数据的URL地址
		isSimpleData : true,
		rootPID : -1,
		treeNodeKey : "id",
		treeNodeParentKey : "pId",
		asyncParam : [ "id" ],
		callback : {
			change : zTreeOnClick_client,
			asyncSuccess : function(event, treeId, treeNode, msg) {
				if (!treeNode) { // 判断是 顶级机构就展开,其余的收缩+
					var rootNode2 = zTree_client.getNodeByParam(
							"level", 0);
					zTree_client.expandNode(rootNode2, true, false);
				}

			}
		}
	};
	setting5 = {
			async : true,
			asyncUrl :pathUrl+"/opt_department.htm?method=createTree2&lguserid="+$("#lguserid").val(), //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			asyncParam: ["depId"],
			callback: {
				click: zTreeOnClick_udep,
			  	asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){
					   var rootNode = zTree5.getNodeByParam("level", 0);
					   zTree5.expandNode(rootNode, true, false);
					}
				}
			}
		};
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
});
function openRoleChoose() {
	// $("select[name='sex']").hide();
	// $("select[name='userState']").hide();
	// $("select[name='userPerType']").hide();
	$('#roDiv').dialog('open');

}
function dorole() {
	$('#roDiv').dialog('close');
	fillRoleName();
}
function fillRoleName() {
	var roleNameStr = "";
	$('input[name="roleId"]:checked').each(function() {
		roleNameStr += $(this).next().html() + ",";
	});
	if (roleNameStr != "") {
		roleNameStr = roleNameStr.substring(0, roleNameStr.lastIndexOf(","));
		$("#roleName").attr("allRoleName", roleNameStr);
		$("#allRoleName").html(roleNameStr);
		var arr = roleNameStr.split(",");
		if (arr.length > 1) {
			roleNameStr = arr[0] + "...";
		}
		$("#roleName").val(
				roleNameStr.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		$("#roleName").removeClass("fontColor");
	} else {
		$("#roleName").val(getJsLocaleMessage('employee','employee_alert_78'));
		$("#roleName").addClass("fontColor");
		$("#roleName").attr("allRoleName", "");
	}
}

function hideMenuById(menu) {
	for ( var i = 0; i < menu.length; i++) {
		$("#" + menu[i]).hide();
	}
}
function showMenuById(menu) {
	for ( var i = 0; i < menu.length; i++) {
		$("#" + menu[i]).show();
	}
}
function zTreeOnClick_user(event, treeId, treeNode) {
	if (treeNode) {
		var sortObj = $("#domdepName");
		sortObj.attr("value", treeNode.name);
		$("#depOption").attr("text", getJsLocaleMessage('employee','employee_alert_79') + treeNode.name);
		$("#domDepId").attr("value", treeNode.id);
		var depId = $("#userDepId").attr("value");
		if (depId != treeNode.id) {
			var depNode = zTree_user.getNodesByParam("id", depId)[0];
			var domNodes = zTree_user.getNodesByParam("id", treeNode.id,
					depNode);
			if (domNodes.length != 1 || !depNode) {
				alert(getJsLocaleMessage('employee','employee_alert_80'));
				return;
			} else {
				$("#dropMenu_user").hide();
			}
		} else {
			$("#dropMenu_user").hide();
		}
	}
	$("#selectDepBtn_user").css("visibility", "visible");
	$("#userState").css("visibility", "visible");
	$("#employPerType").css("visibility","visible");
	$("#clientPerType").css("visibility","visible");
}

function showEmployMenu() {
	// var menu = ["dropMenu","dropMenu_udep","dropMenu2","dropMenu_client"];
	// hideMenuById(menu);
	$("#dropMenu_client").hide();
	var disstate = $("#dropMenu_employ").css("display");
	if(disstate == "block"){
		$("#clientPerType").css("visibility","visible");
	}else{
		$("#clientPerType").css("visibility","hidden");
	}
	$("#dropMenu_employ").focus();
	$("#dropMenu_employ").toggle();
	var depids = $("#domDepId_employ").val();
	setting_employ.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpDepCheck&checkedIds="+depids;
	zTree_employ = $("#dropdownMenu_employ").zTree(setting_employ,
			zNodes_employ);
}
function showUdepMenu() {
	$("#dropMenu_udep").focus();
	$("#dropMenu_udep").toggle();
	zTree5 = $("#dropdownMenu_udep").zTree(setting5, zNodes5);
	var disstate = $("#dropMenu_udep").css("display");
	if(disstate === 'block'){
		var menuIds = ["dropMenu","dropMenu_employ","dropMenu_client"];
		hideMenuById(menuIds);
	}else{
		$("#userPerType").css("visibility","visible");
		$("#employPerType").css("visibility","visible");
		$("#clientPerType").css("visibility","visible");
	}
}
function showUserMenu() {
	// var menu =
	// ["dropMenu_udep","dropMenu_edep","dropMenu_employ","dropMenu_client"];
	// hideMenuById(menu);
	var disstate = $("#dropMenu_user").css("display");
	if(disstate == "block"){
		$("#employPerType").css("visibility","visible");
		$("#clientPerType").css("visibility","visible");
	}else{
		$("#employPerType").css("visibility","hidden");
		$("#clientPerType").css("visibility","hidden");
	}
	$("#dropMenu_user").focus();
	/*
	 * var permissionType = $("#userPerType"); var sortOffset =
	 * permissionType.offset(); $("#selectDepBtn_user").css("visibility","hidden");
	 * $("#userState").css("visibility","hidden");
	 * $("#userState").css("visibility","hidden");
	 * $("#dropMenu_user").css({'top':sortOffset.top+permissionType[0].offsetHeight,'left':sortOffset.left});
	 */
	$("#dropMenu_user").toggle();
	zTree_user = $("#dropdownMenu_user").zTree(setting_user, zNodes_user);
}
function initzTreeuser()
{
	zTree_user = $("#dropdownMenu_user").zTree(setting_user, zNodes_user);
}

function zTreeOnClick_employ(event, treeId, treeNode) {
	if (treeNode) {
		var zTreeNodes2 = zTree_employ.getChangeCheckedNodes();
		var pops = "";
		var params = [];
		for ( var i = 0; i < zTreeNodes2.length; i++) {
			pops += zTreeNodes2[i].name + ";";
		}
		$("#domDepName_employ").attr("value", pops);
	}
}
function showClientMenu() {
	var menu = [ "dropMenu", "dropMenu_udep", "dropMenu_employ", "dropMenu2" ];
	hideMenuById(menu);
	$("#dropMenu_client").focus();
	$("#dropMenu_client").toggle();
	var depids = $("#domDepId_client").val();
	setting_client.asyncUrl=pathUrl+ "/opt_sysuser.htm?method=getClientDepCheck&checkedIds="+depids;
	zTree_client = $("#dropdownMenu_client").zTree(setting_client,
			zNodes_client);
}
function zTreeOnClick_client(event, treeId, treeNode) {
	if (treeNode) {
		var zTreeNodes2 = zTree_client.getChangeCheckedNodes();
		var pops = "";
		var params = [];
		for ( var i = 0; i < zTreeNodes2.length; i++) {
			pops += zTreeNodes2[i].name + ";";
		}
		$("#domDepName_client").attr("value", pops);
	}
}

function zTreeOnClickOK_employ() {
	$("#dropMenu_employ").hide();
	var zTreeNodes2 = zTree_employ.getCheckedNodes();
	var pops = "";
	var userids = "";
	for ( var i = 0; i < zTreeNodes2.length; i++) {
		pops += zTreeNodes2[i].name + ";";
		userids += zTreeNodes2[i].id + ",";
	}
	$("#domDepName_employ").attr("value", pops);
	$("#domDepId_employ").attr("value", userids);
	$("#depOption_employ").attr("text", getJsLocaleMessage('employee','employee_alert_79') + pops);
	if (zTreeNodes2.length == 0) {
		$("#depOption_employ").attr("text", getJsLocaleMessage('employee','employee_alert_81'));
	}
	$("#clientPerType").css("visibility","visible");
}
function cleanSelect_employ() {
	var checkNodes = zTree_employ.getCheckedNodes();
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	zTree_employ.refresh();
	$("#domDepId_employ").attr("value", "");
	$("#domDepName_employ").attr("value", getJsLocaleMessage('employee','employee_alert_82'));
}
function zTreeOnClickOK_client() {
	$("#dropMenu_client").hide();
	var zTreeNodes2 = zTree_client.getCheckedNodes();
	var pops = "";
	var userids = "";
	for ( var i = 0; i < zTreeNodes2.length; i++) {
		pops += zTreeNodes2[i].name + ";";
		userids += zTreeNodes2[i].id + ",";
	}
	$("#domDepName_client").attr("value", pops);
	$("#domDepId_client").attr("value", userids);
	$("#depOption_client").attr("text", getJsLocaleMessage('employee','employee_alert_79') + pops);
	if (zTreeNodes2.length == 0) {
		$("#depOption_client").attr("text", getJsLocaleMessage('employee','employee_alert_81'));
	}

}
function cleanSelect_client() {
	var checkNodes = zTree_client.getCheckedNodes();
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	zTree_client.refresh();
	$("#domDepId_client").attr("value", "");
	$("#domDepName_client").attr("value", "");
}
function cleanSelect_employ() {
	var checkNodes = zTree_employ.getCheckedNodes();
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	zTree_employ.refresh();
	$("#domDepId_employ").attr("value", "");
	$("#domDepName_employ").attr("value", getJsLocaleMessage('employee','employee_alert_82'));
}
function cleanSelect_client() {
	var checkNodes = zTree_client.getCheckedNodes();
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	zTree_client.refresh();
	$("#domDepId_client").attr("value", "");
	$("#domDepName_client").attr("value", "");
}

function changeUserPermType() {
	var permissionType = $("#userPerType").attr("value");
	if (permissionType == 1) {
		$("#selectDepBtn_user").hide();
		$("#dropMenu_user").hide();
		$("#employPerType").css("visibility","visible");
		$("#clientPerType").css("visibility","visible");
	} else if (permissionType == 2) {

		$("#selectDepBtn_user").parent().show();
		$("#selectDepBtn_user").show();
		$("#selectDepBtn_user").css("visibility","");
		showUserMenu();
	}
}
function changeEmployPermType() {
	var permissionType = $("#employPerType").attr("value");
	if (permissionType == 1) {
		$("#selectDepBtn_employ").hide();
		$("#dropMenu_employ").hide();
		$("#clientPerType").css("visibility","visible");
	} else if (permissionType == 2) {
		$("#selectDepBtn_employ").show();
		showEmployMenu();
	}
}
function changeClientPermType() {
	var permissionType = $("#clientPerType").attr("value");
	if (permissionType == 1) {
		$("#selectDepBtn_client").hide();
		$("#dropMenu_client").hide();
	} else if (permissionType == 2) {
		$("#selectDepBtn_client").show();
		showClientMenu();
	}
}

function zTreeOnClick_udep(event, treeId, treeNode) {
	if (treeNode) {
		$("#userDepName").attr("value", treeNode.name); // 所属机构名称
		$("#userDepName").removeClass("fontColor"); // 所属机构名称
		$("#userDepId").attr("value", treeNode.id); // 所属机构ID
		$("#selectDepBtn_user").css("visibility", "hidden");

		$("#userPerType").empty();
		$("#userPerType")
				.append(
						"<option value=\'1\'>"+getJsLocaleMessage('employee','employee_alert_83')+"</option><option id='depOption' value='2'>"+getJsLocaleMessage('employee','employee_alert_81')+"</option>");
		$("#userPerType").attr("disabled", false);
		$("#dropMenu_udep").hide();
		$("#userPerType").css("visibility","visible");
		$("#employPerType").css("visibility","visible");
		$("#clientPerType").css("visibility","visible");
	}
}

// 判断是否 需要绑定固定操作员尾号
function clickSubno(guid) {
	var isneedSubno = "2";
	if (document.getElementsByName('isNeedSubno')[0].checked == true) {
		isneedSubno = "1";
	}
	if (isneedSubno == "1") {
		// 这里是点击新分配一个子号
		$.post("opt_sysuser.htm?method=getSingleSubno", {
			guid : guid,
			lgcorpcode : $("#lgcorpcode").val()
		}, function(msg) {
			if ("" == msg) {
				alert(getJsLocaleMessage('employee','employee_alert_84'));
				document.getElementsByName('isNeedSubno')[0].checked = false;
				return;
			} else if ("notsubno" == msg) {
				alert(getJsLocaleMessage('employee','employee_alert_84'));
				document.getElementsByName('isNeedSubno')[0].checked = false;
				return;
			} else if ("enough" == msg) {
				alert(getJsLocaleMessage('employee','employee_alert_85'));
				document.getElementsByName('isNeedSubno')[0].checked = false;
				return;
			} else {
				$("#addSubno").val(msg);
				$("#subno2").val(msg);
				$("#addSubno").show();
			}
		});
	} else if (isneedSubno == "2") {
		// 这里是删除 分配的
		$.post("opt_sysuser.htm?method=delSingleSubno", {
			guid : guid,
			lgcorpcode : $("#lgcorpcode").val()
		}, function(data) {
			if (data == "1") {
				$("#addSubno").val("");
				$("#subno2").val("");
				$("#addSubno").hide();
			} else {
				$("#zhu").html(getJsLocaleMessage('employee','employee_alert_86'));
				return;
			}
		});
	}
	$("#haveSubno").val(isneedSubno);
}
function swapEle(e1, e2) {
	if (navigator.userAgent.indexOf('MSIE') > 0) {
		e1.swapNode(e2);
	} else {
		var parent = e1.parentNode;// 父节点
		var t1 = e1.nextSibling;// 两节点的相对位置
		var t2 = e2.nextSibling;
		if (t1)
			parent.insertBefore(e2, t1);
		else
			parent.appendChild(e2);
		if (t2)
			parent.insertBefore(e1, t2);
		else
			parent.appendChild(e1);

	}
}
