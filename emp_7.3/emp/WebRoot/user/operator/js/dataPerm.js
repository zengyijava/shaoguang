var zTree_udep;
var setting_udep;
var zNodes_udep =[];
var setting_edep ;
var zTree_edep;
var zNodes_edep =[];

var zTree_user;
var setting_user;
var zNodes_user = [];
var setting_employ;

//新增
var setting_dep;
var zTree_dep;
var zNodes_dep = [];
//end

var zTree_employ;
var zNodes_employ = [];
var setting_client;
var zTree_client;
var zNodes_client = [];
var pathUrl ;
$(document).ready(function() {
	getLoginInfo("#loginUser");
	pathUrl = $("#pathUrl").val();
	$('#roDiv').dialog( {
		autoOpen : false,
		height : 300,
		title : getJsLocaleMessage("user","user_xtgl_czygl_text_1"),
		width : 320,
		close : function() {
			$("select[name='sex']").show();
			$("select[name='userState']").show();
			$("select[name='userPerType']").show();
			fillRoleName();
		},
		modal : true
	});

	$('#flowDiv').dialog( {
		autoOpen : false,
		height : 523,
		title : getJsLocaleMessage("user","user_xtgl_czygl_text_2"),
		width : 668,
		close : function() {
			$("select[name='sex']").show();
			$("select[name='userState']").show();
			$("select[name='userPerType']").show();
		},
		open : function() {
			$("#flow_choose").show();
			$("select[name='sex']").hide();
			$("select[name='userState']").hide();
			$("select[name='userPerType']").hide();
		},
		modal : true
	});

	setting_udep = {
			async : true,
			asyncUrl :pathUrl+"/opt_department.htm?method=createTree2&lguserid="+GlobalVars.lguserid, //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			asyncParam: ["depId"],
			callback: {
				click: zTreeOnClick_udep,
			  	asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){
					   var rootNode = zTree_udep.getNodeByParam("level", 0);
					   zTree_udep.expandNode(rootNode, true, false);
					}
				}
			}
	};
	setting_edep = {
			async : true,
			asyncUrl:"",  //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			callback: {
				click: zTreeOnClick_edep,
				asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+				
						var rootNode2 = zTree_edep.getNodeByParam("level", 0);
				        zTree_edep.expandNode(rootNode2, true, false);
			      }
				},
				beforeAsync:function(treeId,treeNode){				
					zTree_edep.setting.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
				}	
			}
		};
	
	setting_user = {
		async : true,
		asyncUrl : pathUrl
				+ "/opt_department.htm?method=createTree2&lguserid="
				+ GlobalVars.lguserid, // 获取节点数据的URL地址
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
		asyncUrl :"", // 获取节点数据的URL地址
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
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
	setting_dep = {
			
			checkable : true,
			checkStyle : "checkbox",
			checkType : {
				"Y" : "s",
				"N" : "s"
			},
			async : true,
			asyncUrl :"", // 获取节点数据的URL地址
			isSimpleData : true,
			rootPID : -1,
			treeNodeKey : "id",
			treeNodeParentKey : "pId",
			asyncParam : [ "depId" ],
			callback : {
				change : zTreeOnClick_dep,
				asyncSuccess : function(event, treeId, treeNode, msg) {
					if (!treeNode) { // 判断是 顶级机构就展开,其余的收缩+
						var rootNode2 = zTree_dep.getNodeByParam(
								"level", 0);
						zTree_dep.expandNode(rootNode2, true, false);
						
						
						
					}
					

				}/*,
				beforeChange: function(treeId, treeNode){
					
					if(treeNode.canEdit != undefined && !treeNode.canEdit){
						return false;
					}
					
				}
				*/
				
			}
		};
		// end
});
function openRoleChoose() {
	$("select[name='sex']").hide();
	$("select[name='userState']").hide();
	$("select[name='userPerType']").hide();
	// $('#roDiv').show();
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
		$("#roleName").val(getJsLocaleMessage("user","user_xtgl_czygl_text_3"));
		$("#roleName").addClass("fontColor");
		$("#roleName").attr("allRoleName", "");
	}
}

function shFlow() {
	var departId = $("#depId").attr("value");
	if ($("#userDepName").attr("value") == "") {
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_4"));
		return;
	}
	$("#flowDiv").dialog("open");
}

function flowOk() {
	var sysOpeName = "";
	if ($("#right option").size() < 1) {
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_5"));
		return;
	}
	$("#right option").each(function() {
		sysOpeName = sysOpeName + $(this).val() + ",";
	})
	sysOpeName = sysOpeName.substring(0, sysOpeName.length - 1);
	$("#bindOpeNum").attr("value", sysOpeName);
	$("#flowDiv").dialog("close");
	$("#zhu").html("");
}

function showFlow() {
	var pathUrl = $("#pathUrl").attr("value");
	var operId = $("#right").attr("value");
	$.post(pathUrl + '/opt_sysuser.htm?method=getFlowInfo', {
		operId : operId
	}, function(info) {
		if (info != null) {
			$("#showFlow").val("");
			$("#showFlow").val(info);
		}
	})
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
		var depId = $("#depId").attr("value");
		if (depId != treeNode.id) {
			var depNode = zTree_user.getNodeByParam("id", depId);//获取机构ID对应的节点
			if(!depNode){alert(getJsLocaleMessage("user","user_xtgl_czygl_text_6"));return;}//当前节点还没有展开 说明所选节点不在其内
			if (treeNode.dpath.indexOf(depNode.dpath) == -1) {
				$("#domDepId").val("");
				//$("#depOption").attr("text", "机构");
				$("#userPerType").children("option").eq(1).text(getJsLocaleMessage("user","user_xtgl_czygl_text_7"));
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_6"));
				return;
				// $("#permissionType").empty();
				// $("#permissionType").append("<option
				// value=\'1\'>个人</option><option id='depOption'
				// value='2'>机构</option>");
			} else {
				$("#dropMenu_user").hide();
			}
		} else {
			$("#dropMenu_user").hide();
		}
        var sortObj = $("#domdepName");
        sortObj.attr("value", treeNode.name);
        $("#userPerType").children("option").eq(1).text("机构-" + treeNode.name);
        //alert($("#userPerType :selected").text());
        //$("#depOption").attr("text", "机构-" + treeNode.name);
        $("#domDepId").attr("value", treeNode.id);
	}
	//$("#postId").css("visibility", "visible");
	$("#selectDepBtn").css("visibility", "visible");
	$("#userState").css("visibility", "visible");
	$("#employPerType").css("visibility","visible");
	$("#clientPerType").css("visibility","visible");
}

function showEmployMenu() {
	// var menu = ["dropMenu","dropMenu_udep","dropMenu2","dropMenu_client"];
	// hideMenuById(menu);
	$("#dropMenu_client").hide();
	$("#dropMenu_dep").hide();
	var disstate = $("#dropMenu_employ").css("display");
	

	$("#dropMenu_employ").focus();
	$("#dropMenu_employ").toggle();
	//$("#depPerType").toggle();
	var depids = $("#domDepId_employ").val();
	setting_employ.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpDepCheck&checkedIds="+depids;
	zTree_employ = $("#dropdownMenu_employ").zTree(setting_employ,
			zNodes_employ);
}

// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
function showDepMenu() {
	//获取所属机构的depid值
	var depIdByOperator = $("#depId").val();
	//alert(depIdByOperator);
	//---------------------------------------
	
	var menu = ["dropMenu","dropMenu_udep","dropMenu2","dropMenu_client","dropMenu_employ"];
	hideMenuById(menu);
	$("#dropMenu_dep").focus();
	$("#dropMenu_dep").toggle();
	var depids = $("#domDepId_dep").val();
	//setting_dep.asyncUrl=pathUrl+ "/opt_sysuser.htm?method=getDepCheck&checkedIds="+depids;
	var lguserid = GlobalVars.lguserid;
	setting_dep.asyncUrl=pathUrl+ "/opt_sysuser.htm?method=getBalanceDepCheck&lguserid="+lguserid+"&checkedIds="+depids+"&depIdByOperator="+depIdByOperator;
	zTree_dep = $("#dropdownMenu_dep").zTree(setting_dep,
			zNodes_dep);
	
}
// end



function showUdepMenu() {
	$("#dropMenu_udep").focus();
	$("#dropMenu_udep").toggle();
	
		$('#isReviewer,#clientPerType').show();
	zTree5 = $("#dropdownMenu_udep").zTree(setting5, zNodes5);
	var disstate = $("#dropMenu_udep").css("display");
	if(disstate == 'block'){	
		var menuIds = ["dropMenu","dropMenu_employ","dropMenu_client"];
		hideMenuById(menuIds);
	}else{
		$("#userPerType").css("visibility","visible");
		$("#employPerType").css("visibility","visible");
		//$("#clientPerType").css("visibility","visible");
	}
}
function showUserMenu() {
	// var menu =
	// ["dropMenu_udep","dropMenu_edep","dropMenu_employ","dropMenu_client"];
	// hideMenuById(menu);
	var disstate = $("#dropMenu_user").css("display");
	if(disstate == "block"){
		$("#employPerType").css("visibility","visible");
		//$("#clientPerType").css("visibility","visible");
		
	}else{
		$("#employPerType").css("visibility","hidden");
		//$("#clientPerType").css("visibility","hidden");
		$('#dropMenu_employ').hide();
	}
	$("#dropMenu_user").focus();
	/*
	 * var permissionType = $("#userPerType"); var sortOffset =
	 * permissionType.offset(); $("#selectDepBtn").css("visibility","hidden");
	 * $("#userState").css("visibility","hidden");
	 * $("#userState").css("visibility","hidden");
	 * $("#dropMenu_user").css({'top':sortOffset.top+permissionType[0].offsetHeight,'left':sortOffset.left});
	 */
	$("#dropMenu_user").toggle();
	zTree_user = $("#dropdownMenu_user").zTree(setting_user, zNodes_user);
}
function zTreeOnClick_employ(event, treeId, treeNode) {
	if (treeNode) {
		var nodes = zTree_employ.getChangeCheckedNodes();
		var pops = "";
		var params = [];
		for ( var i = 0; i < nodes.length; i++) {
			pops += nodes[i].name + ";";
		}
		$("#domDepName_employ").attr("value", pops);
	}
}

//EMP5.7新需求：增加对操作员充值和回收权限   by pengj
function zTreeOnClick_dep(event, treeId, treeNode) {
	if (treeNode) {
		var nodes = zTree_dep.getChangeCheckedNodes();
		var pops = "";
		var params = [];
		for ( var i = 0; i < nodes.length; i++) {
			pops += nodes[i].name + ";";
		}
		
		
		$("#domDepName_dep").attr("value", pops);
		/*
		//获取所属机构的depid值
		var depIdByOperator = $("#depId").val();
		//alert(depIdByOperator);
		//---------------------------------------
		//获取机构树中选中节点的depId
		var id = treeNode.depId;
		//alert(id);
		//如果在机构树中选中的节点depid等于所属机构的depid
		if(treeNode.depId == depIdByOperator){
			//var depNode = zTree_dep.getNodeByParam("depId",depIdByOperator);
			//if(depNode.checked == false){
				//获取所属机构的所有子机构节点
				var firstChildrenNodes = zTree_dep.getNodesByParam("pId",depIdByOperator);
				//遍历所属机构的所有子机构节点
				for(i=0;i<firstChildrenNodes.length;i++){
					alert(firstChildrenNodes[i].depId);
					//alert(firstChildrenNodes.length);
					//获取子机构节点depid
					//var firstChildrenNodeId = firstChildrenNodes[i].depId;
					//alert(firstChildrenNodeId);
					//通过子机构节点的depid获取子机构节点
					//var firstChildrenNode = zTree_dep.getNodeByParam("depId",firstChildrenNodeId);
					//alert(firstChildrenNode.length);
					//勾选子机构节点
					var bool = firstChildrenNodes[i].checked;
					alert(bool);
					firstChildrenNodes[i].checked = "true";
					alert(bool);
					//zTree_dep.checkAllNodes(true);
				//}
				//alert(firstChildrenNodes.length)
				//zTree_dep.selectNode(firstChildrenNodes);
			}
		}
		*/
	}
}
//end


function showClientMenu() {
	var menu = [ "dropMenu", "dropMenu_udep", "dropMenu_employ", "dropMenu2","dropMenu_dep" ];
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
	$("#depOption_employ").attr("text", getJsLocaleMessage("user","user_xtgl_czygl_text_9") + pops);
	if (zTreeNodes2.length == 0) {
		$("#depOption_employ").attr("text", getJsLocaleMessage("user","user_xtgl_czygl_text_7"));
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
	$("#domDepName_employ").attr("value", getJsLocaleMessage("user","user_xtgl_czygl_text_1"));
}

//EMP5.7新需求：增加对操作员充值和回收权限   by pengj
function zTreeOnClickOK_dep() {
	$("#dropMenu_dep").hide();
	var zTreeNodes2 = zTree_dep.getCheckedNodes();
	var pops = "";
	var userids = "";
	for ( var i = 0; i < zTreeNodes2.length; i++) {
		pops += zTreeNodes2[i].name + ";";
		userids += zTreeNodes2[i].id + ",";
	}
	$("#domDepName_dep").attr("value", pops);
	$("#domDepId_dep").attr("value", userids);
	$("#depOption_dep").attr("text", getJsLocaleMessage("user","user_xtgl_czygl_text_9") + pops);
	if (zTreeNodes2.length == 0) {
		$("#depOption_dep").attr("text", getJsLocaleMessage("user","user_xtgl_czygl_text_7"));
	}
	$("#clientPerType").css("visibility","visible");
	//$("#employPerType").css("visibility","visible");
}
function cleanSelect_dep() {
	var checkNodes = zTree_dep.getCheckedNodes();
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	zTree_dep.refresh();
	$("#domDepId_dep").attr("value", "");
	$("#domDepName_dep").attr("value", getJsLocaleMessage("user","user_xtgl_czygl_text_10"));
}
//end



function zTreeOnClickOK_client() {
	$("#dropMenu_client").hide();
	var zTreeNodes2 = zTree_client.getChangeCheckedNodes();
	var pops = "";
	var userids = "";
	for ( var i = 0; i < zTreeNodes2.length; i++) {
		pops += zTreeNodes2[i].name + ";";
		userids += zTreeNodes2[i].id + ",";
	}
	$("#domDepName_client").attr("value", pops);
	$("#domDepId_client").attr("value", userids);
	$("#depOption_client").attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_9")  + pops);
	if (zTreeNodes2.length == 0) {
		$("#depOption_client").attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_7"));
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
	$("#domDepName_employ").attr("value", getJsLocaleMessage("user","user_xtgl_czygl_text_10"));
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
	$("#depOption_client").attr("text", getJsLocaleMessage("user","user_xtgl_czygl_text_9") + pops);
	if (zTreeNodes2.length == 0) {
		$("#depOption_client").attr("text", getJsLocaleMessage("user","user_xtgl_czygl_text_7"));
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
        $("#selectDepBtn_client").parent().show();
		$("#selectDepBtn_client").show();
		showClientMenu();
	}
}

// EMP6.0新需求：增加对操作员充值和回收权限   by pengj */
function changeDepPermType() {
	var permissionType = $("#depPerType").attr("value");
	if (permissionType == 1) {
		$("#selectDepBtn_dep").hide();
		$("#dropMenu_dep").hide();
		//$("#clientPerType").css("visibility","visible");
	} else if (permissionType == 2) {
		$("#selectDepBtn_dep").show();
		showDepMenu();
	}
}
// end

function zTreeOnClick_udep(event, treeId, treeNode) {
	
	if (treeNode) {
		$("#userDepName").attr("value", treeNode.name); // 所属机构名称
		$("#userDepName").removeClass("fontColor"); // 所属机构名称
		$("#depId").attr("value", treeNode.id); // 所属机构ID
		$("#selectDepBtn").css("visibility", "hidden");

		$("#userPerType").empty();
		$("#userPerType")
				.append(
						"<option value=\'1\'>" + getJsLocaleMessage("user","user_xtgl_czygl_text_139")+ "</option><option id='depOption' value='2'>" + getJsLocaleMessage("user","user_xtgl_czygl_text_7")+ "</option>");
		$("#userPerType").attr("disabled", false);
		$("#selectDepBtn_user").hide();
		$("#dropMenu_udep").hide();
		$('#isReviewer,#clientPerType').show();
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
			lgcorpcode : GlobalVars.lgcorpcode
		}, function(msg) {
			if ("" == msg) {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_11"));
				document.getElementsByName('isNeedSubno')[0].checked = false;
				return;
			} else if ("notsubno" == msg) {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_11"));
				document.getElementsByName('isNeedSubno')[0].checked = false;
				return;
			} else if ("enough" == msg) {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_12"));
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
			lgcorpcode : GlobalVars.lgcorpcode
		}, function(data) {
			if (data == "1") {
				$("#addSubno").val("");
				$("#subno2").val("");
				$("#addSubno").hide();
			} else {
				$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_13"));
				return;
			}
		});
	}
	$("#haveSubno").val(isneedSubno);
}
function showMenu() {
	var menu = ["dropMenu_user","dropMenu_edep","dropMenu_employ","dropMenu_client","dropMenu_dep"];
	hideMenuById(menu);
	var disstate = $("#dropMenu_udep").css("display");
	if(disstate=='block'){
	    $("#userState").css("visibility","visible");
		$("#job").css("visibility","visible");
		$("#dropMenu_udep").toggle();
		$('#isReviewer,#clientPerType').show();

	}else if(disstate == 'none'){
	    $("#userState").css("visibility","hidden");
		$("#job").css("visibility","hidden");
		$("#dropMenu_udep").toggle();
		$('#isReviewer,#clientPerType').hide();

	}
}

function zTreeOnClick_udep(event, treeId, treeNode) {
	
    $("#job").css("visibility","visible");
    $("#userState").css("visibility","visible");
	if (treeNode) {
		$("#depNam").attr("value", treeNode.name);			//所属机构名称
		$("#depNam").removeClass("fontColor");			//所属机构名称
		$("#depId").attr("value",treeNode.id);				//所属机构ID
		$("#userPerType").empty();
		$("#userPerType").append("<option value=\'1\'>个人</option><option id='depOption' value='2'>机构</option>");
		$("#selectDepBtn_user").hide();
		$("#userPerType").attr("disabled",false);
		
		//新增  pengj  
		//$("#depPerType").empty();
		//$("#depPerType").append("<option value=\'1\'>操作员所属机构</option><option id='depOption' value='2'>机构</option>");
		//$("#selectDepBtn_dep").hide();
		$("#depPerType").attr("disabled", false);
		//------------------------------------------
		
		
		hideMenu();
	}
}

function zTreeOnClick_edep(event, treeId, treeNode) {
	$("#employDepNam").attr("value", treeNode.name);
	$("#employDepId").attr("value",treeNode.id);
	$("#dropMenu_edep").hide();
}
function hideMenu() {
	$("#dropMenu_udep").hide();
	$('#isReviewer,#clientPerType').show();

}
function hideMenu2() {
	$("#dropMenu_user").hide();
	$('#isReviewer,#clientPerType').show();
}
function showMenuEdep() {
	var menu = ["dropMenu_udep","dropMenu_user","dropMenu_employ","dropMenu_client"];
	hideMenuById(menu);
	$("#dropMenu_edep").focus();
	$("#dropMenu_edep").toggle();

		setting_edep.asyncUrl="epl_employeeBook.htm?method=getEmpSecondDepJson";
		zTree_edep = $("#dropdownMenu_edep").zTree(setting_edep, zNodes_edep);
}
function reloadTree() {
	hideMenu();
	hideMenu2();
	
	zTree_udep = $("#dropdownMenu_udep").zTree(setting_udep, zNodes_udep);
	zTree_user = $("#dropdownMenu_user").zTree(setting_user, zNodes_user);
}
$('html,body').bind('click',function(){
	if($('#dropMenu_udep').is(":visible")){
		$('#isReviewer,#clientPerType').hide();
	}else{
		$('#isReviewer,#clientPerType').show();
		
		
		
	}
	
})
