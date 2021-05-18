var pathUrl = $("#pathUrl").val();
var skin = $("#skin").val();
function doEdit(l2gId,udgName,guid,moblie)
{
 	$("#own_group_update").css("display","block");
 	var confire = getJsLocaleMessage('group','group_common_confire') ;
 	var cancel = getJsLocaleMessage('group','group_common_cancel') ;
	$('#own_group_update').dialog({
		autoOpen: false,
		height: 134,
		width: 268,
		modal:true,
		buttons:{
			confire:function(){
		     updateGroupInfo(l2gId,udgName,guid,moblie);
 			},
 			cancel:function(){
				$('#own_group_update').dialog('close');
			}
		}
	});
 
	 $("#udgId").attr("option","selected");
	 $('#own_group_update').dialog('open');
}

function updateGroupInfo(l2gId,udgName,guid,moblie)
{
	var udgId = $("#udgId").val().split("&");
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
  	if(confirm(getJsLocaleMessage('group','group_common_deletegroup'))==true){
 		if(udgId[1].toString()==udgName.toString())
 		{
 			//alert("当前所属群组未做修改！");
 			alert(getJsLocaleMessage('group','group_js_groupManageEqa_curgourpnomodify'));
 			return;
 		}else{
 	     $.post("grp_cliGroupManage.htm?method=checkGrhaveMember", {
			udgId : udgId[0],
			personId:guid,
			moblie:moblie,
			lguserid:lguserid
		}, function(returnMsg) {
				if(returnMsg == 1){
					//alert("此群组已存在该成员！");
					alert(getJsLocaleMessage('group','group_js_groupManageEqa_grouphadmember'));
	                return;
				}else if(returnMsg == 3){
					//alert("此群组已存在此手机号码！");
					alert(getJsLocaleMessage('group','group_js_groupManageEqa_grouphadphone'));
	                return;
				}else if(returnMsg == 2){
					 $.post("grp_cliGroupManage.htm?method=updateGroupInfo",{l2gId:l2gId,udgId:udgId[0],lgcorpcode:lgcorpcode},function(result){
							if(result=='true')
							{
								//alert("修改成功！");
								alert(getJsLocaleMessage('group','group_common_modifysuc'));
								window.location.reload();
							}else{
								//alert("修改失败！");
								alert(getJsLocaleMessage('group','group_common_modifyfalse'));
								window.location.reload();
							}
					 });
				}else{
					//alert("查询数据失败！");
					alert(getJsLocaleMessage('group','group_common_selectfalse'));
	                return;
				}
			});
		}
		
 	}
}



function delAll(){
	var lgcorpcode = $("#lgcorpcode").val();
	
	var items = "";
 	$('input[name="checklist"]:checked').each(function(){	
 		items += $(this).val().toString()+",";
 	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
 	}
 	if(items==""){
		// alert("请选择您要删除的群组记录！");
		alert(getJsLocaleMessage('group','group_common_deletegroup'));
		return;
	}else{
	if(confirm(getJsLocaleMessage('group','group_js_groupManageEqa_confiredelgruop'))==true){
				$.post("grp_cliGroupManage.htm?method=delete",{ids:items,lgcorpcode:lgcorpcode},function(result){
					if(result>=1)
					{
 						//alert("删除成功,共删除"+result+"条信息！");
 						alert(getJsLocaleMessage('group','group_js_groupManageEqa_delsuc') + result + getJsLocaleMessage('group','group_js_groupManageEqa_infos'));
 						window.location.reload();
					}else if(result == 0){
						//alert("删除失败！");
						alert(getJsLocaleMessage('group','group_common_deletefalse'));
						//window.location.reload();
					}else{
						//alert("请检查网络/数据库连接是否正常！");
						alert(getJsLocaleMessage('group','group_common_deletefalse'));
						return;
					}
				});
				
	}
	}
}

function del(i){
	
	var lgcorpcode = $("#lgcorpcode").val();
 	if(confirm(getJsLocaleMessage('group','group_js_groupManageEqa_confiredelgruop')))
	{
 		
		$.post("grp_cliGroupManage.htm?method=delete",{ids:i,lgcorpcode:lgcorpcode},function(result){
			if(result>=1)
			{
 				//alert("删除成功！");
				alert(getJsLocaleMessage('group','group_common_deletesuc'));
				window.location.reload();
			}else{
				//alert("删除失败！");
				alert(getJsLocaleMessage('group','group_common_deletefalse'));
			}
		});
	}
}

function bindPClik()
{
	$('#siderList p').click(function() { 
		// 点击角色名将其显示到文本框
		$("#siderList p").removeClass("roleNameColor");
		$(this).addClass("roleNameColor");
		var udgId = $(this).find('#r').val();
		//$("#depId").val(udgId);
		$("#udgTypeId").val(udgId);
		var udgName= $(this).text();
		$('#udgIdtemp').attr('value', udgId);
		$('#udgNametemp').attr('value',udgName);
	    $('#udgName_old').attr('value',udgName);
	    $("#udgName2").val(udgName);
		var time = new Date();
		var lguserid = $("#lguserid").val();
		$('#bookInfo').load($("#path").val()+'/grp_cliGroupManage.htm',{method:'getTable',lguserid:lguserid,udgTypeId : udgId,time:time});
	});
}

function delGroup()
{
	var udgIdtemp = $('#udgIdtemp').val();
	if(udgIdtemp == "")
	{
		//alert("请选择要删除的群组！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_confiredelgruop'));
		return;
	}else
	{
	
		
		$.post("grp_cliGroupManage.htm?method=checkMember", {
			udgId : udgIdtemp
		}, function(result) {
				if(result =='true' && confirm(getJsLocaleMessage('group','group_js_groupManageEqa_delorgwithmember')))
				{
					 
					 delGpMethod(udgIdtemp);
				}else if(result=='false'){
				  delGpMethod(udgIdtemp);
				}
			});
	}
	
	 
}

function delGpMethod(udgId)
{
	if(confirm(getJsLocaleMessage('group','group_js_groupManageEqa_confiredelgruop')))
	{
		
			$.post("grp_cliGroupManage.htm?method=delGroup", {
			udgId : udgId
		}, function(result) {
			if(result >=1)
			{	
				//alert("删除群组成功！");
				alert(getJsLocaleMessage('group','group_js_groupManageEqa_delgroupsuc'));
				window.location.reload();
	
			}else
			{
				//alert("删除群组失败！");
				alert(getJsLocaleMessage('group','group_js_groupManageEqa_delgroupfalse'));
			}
		});
	}
	
}
function addGroup() {
	//清空值
	$("#zjStr").val(",");
	$("#qzStr").val(",");
	$("#gxStr").val(",");
	$("#ygStr").val(",");

	var skin = $("#skin").val();
	var commonPath = $("#commonPath").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var chooseType="1,4,9";
	if(skin.indexOf("frame4.0") != -1) {
		$("#flowFrame").attr("src",commonPath+"/common/selectUserInfo.jsp?lguserid="+lguserid+"&chooseType="+chooseType+"&lgcorpcode="+lgcorpcode);
		changeDialogTitleCss('infoDiv');
	}else{
		var url = "grp_groupManage.htm?method=doEditGroupInfo&udgId=0&lguserid="+$("#lguserid").val();
		$("#flowFrame").attr("src",url);
	}
/*	$(window.frames['editFrame'].document).find("#rightSelectedUserOption").val();
	$(window.frames['editFrame'].document).find("#getChooseMan").html("");
	$(window.frames['editFrame'].document).find("#getChooseMan").html("");*/
	$("#rightSelectedUserOption").val("");
	$("#getChooseMan").html("");
	$("#choiceNum").html("0");
	$('#infoDiv').dialog('open');
}

function groupAdd()
{
	var udgName = window.frames["flowFrame"].document.getElementById("addGpName").value;
	var groupType = 0;
	var udgId = "";
	
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	
	if (udgName.indexOf(" ")>-1) 
	{  
	udgName=udgName.replace(/(^\s*)|(\s*$)/g,"");
	} 
	if (udgName.indexOf("'")!=-1 || outSpecialChar(udgName) || udgName.indexOf('"')!=-1) 
	{  
		//alert("请输入合法字符！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_enterillchar'));
		return;
	} 
	if(udgName == "") 
	{
		//alert("请输入要新建的群组名称！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_entergroupname'));
		return;
	}
	//员工信息
	var ygStr = window.frames["flowFrame"].document.getElementById("ygStr").value;
	//共享信息
	var gxStr =window.frames["flowFrame"].document.getElementById("gxStr").value;
	//群组信息
	var qzStr =window.frames["flowFrame"].document.getElementById("qzStr").value;
	//自建信息
	var zjStr = window.frames["flowFrame"].document.getElementById("zjStr").value;
	$("#infoDiv input").attr("disabled","disabled");
	$.post("grp_groupManage.htm?method=addGroup", {
		udgName : udgName,
		groupType : groupType,
		lguserid:lguserid,
		lgcorpcode:lgcorpcode,
		ygStr : ygStr,
		qzStr : qzStr,
		zjStr : zjStr,
		gxStr : gxStr
	}, function(result) {
		if(result == "exists")
		{
			//alert("当前群组名称已经存在，请重新输入！");
			alert(getJsLocaleMessage('group','group_js_groupManageEqa_regroupname'));
			$("#infoDiv input").attr("disabled","");
			return;
		}
		if(result =='true')
		{
			//alert("新建群组成功！");
			alert(getJsLocaleMessage('group','group_js_groupManageEqa_addgroupsuc'));
			//window.location.reload();
			window.location.href = 'grp_groupManage.htm?method=find&lguserid='+lguserid;
		}else
		{
			$("#infoDiv input").attr("disabled","");
			//alert("新建群组失败！");
			alert(getJsLocaleMessage('group','group_js_groupManageEqa_addgroupfalse'));
		}
	});
}

function groupEdit() {
    var qzStr = "";
    var ygStr ="";
    var gxStr = "";
    var zjStr = window.frames["editFrame"].document.getElementById("zjStr").value;

    var lguserid = $("#lguserid").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var curName = window.frames["editFrame"].document.getElementById("curName").value;
    var udgName = window.frames["editFrame"].document.getElementById("addGpName").value;
	var udgId =  window.frames["editFrame"].document.getElementById("curId").value;
	if(skin.indexOf("frame4.0") !== -1) {
		$(window.frames["editFrame"].document).find("#getChooseMan .groupEdit").each(function(){
			if($(this).attr("isdep")==="4"){
                ygStr += $(this).attr("dataval")+",";
			}else if($(this).attr("isdep")==="6"){
                qzStr += $(this).attr("dataval")+",";
			}else if($(this).attr("isdep")==="13"){
                gxStr += $(this).attr("dataval")+",";
            }
		});
	}else{
        ygStr = window.frames["editFrame"].document.getElementById("ygStr").value;
        qzStr = window.frames["editFrame"].document.getElementById("qzStr").value;
        gxStr = window.frames["editFrame"].document.getElementById("gxStr").value;
	}
	
	var groupType = 0;
	if (udgName.indexOf(" ")>-1) {
		udgName=udgName.replace(/(^\s*)|(\s*$)/g,"");
	} 
	if (udgName.indexOf("'")!=-1 || outSpecialChar(udgName) || udgName.indexOf('"')!=-1 ) {
		//alert("请输入合法字符！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_enterillchar'));
		return;
	} 
	if(udgName === "") {
		//alert("请输入群组名称！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_enternewgroupname'));
	}else{
		if(!isExistGroup(udgId)){
			//alert("未获取到群组信息，可能已删除！");
			alert(getJsLocaleMessage('group','group_page_groupMembers_groupisnull'));
			window.location.reload();
			return;
		}
		$.post("grp_groupManage.htm?method=editGroup", {
			udgName : udgName,
			groupType : groupType,
			lguserid:lguserid,
			lgcorpcode:lgcorpcode,
			curName : curName,
			ygStr : ygStr,
			qzStr : qzStr,
			zjStr : zjStr,
			gxStr : gxStr,
			udgId : udgId
		}, function(result) {
			if(result === "exists") {
				//alert("当前群组名称已经存在，请重新输入！");
				alert(getJsLocaleMessage('group','group_js_groupManageEqa_regroupname'));
				return;
			}
			if(result ==='true') {
				//alert("修改群组成功！");
				alert(getJsLocaleMessage('group','group_js_groupManageEqa_modifygroupsuc'));
				window.location.href = 'grp_groupManage.htm?method=find&lguserid='+lguserid;
			}else {
				//alert("修改群组失败！");
				alert(getJsLocaleMessage('group','group_js_groupManageEqa_modifygroupfalse'));
			}
		});
	}
}
function editGpName()
{
	var udgId = $('#udgIdtemp').val();

	var udgName = $('#udgNametemp').val();
	 
 	if(udgId == "")
	{
		//alert("请选择要重命名的群组！");
 		alert(getJsLocaleMessage('group','group_js_groupManageEqa_selectgrouprename'));
		return;
	}
	$("#udgName2").val(udgName);
	$("#group_update").css("display","block");
	var confire = getJsLocaleMessage('group','group_common_confire') ;
 	var cancel = getJsLocaleMessage('group','group_common_cancel') ;
	$('#group_update').dialog({
		autoOpen: false,
		height: 134,
		width: 268,
		modal:true,
		buttons:{
			confire:function(){
			updateGpName(udgId);
 			},
 			cancel:function(){
				$('#group_update').dialog('close');
			}
		}
	});
 
 	 $('#group_update').dialog('open');
}

function updateGpName(udgId)
{
	var udgName = $("#udgName2").val();
	var udgName_old = $("#udgName_old").val();
	var lguserid = $("#lguserid").val();
	if(udgName_old == udgName)
	{
		//alert("当前群组名称未做改变！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_curgoupnamenomodify'));
		return;
	}
	if (udgName.indexOf("'")!=-1 || hasSpecialChar(udgName) ) 
	{  
		//alert("请输入合法字符！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_enterillchar'));
		return ;
	} 
	$.post("grp_cliGroupManage.htm?method=checkGpName", {
		udgName : udgName,
		udgId:udgId,
		lguserid:lguserid
	}, function(result) {
			if(result =='true' )
			{
 				//alert("当前群组名称已经存在，请重新输入！");
				alert(getJsLocaleMessage('group','group_js_groupManageEqa_regroupname'));
  			}else if(result =='false' && confirm(getJsLocaleMessage('group','group_js_groupManageEqa_confirerenamegroup')))
 			{
 				$.post("grp_cliGroupManage.htm?method=updateGroupName",
 						{
 					udgId:udgId,
 					udgName:udgName
 						},
 						function(result){
 							if(result == 'true')
 							{
 								//alert("重命名成功！");
 								alert(getJsLocaleMessage('group','group_js_groupManageEqa_renamesuc'));
 								window.location.reload();
 							}else
 							{
 								//alert("重命名失败！");
 								alert(getJsLocaleMessage('group','group_js_groupManageEqa_renamefalse'));
 							}
 					
 				});
 			}
		});


}

function addGpMember(type)
{
	if(type=="0")
	{
		getEmployeeT();
		$("#addrType").val(0);
	}
	else if(type=="1")
	{
		getClientT();
		$("#addrType").val(1);
	}
	$("#gp_Name_bind").text($(".roleNameColor").text());
	var udgIdtemp = $('#udgIdtemp').val();
	if(udgIdtemp == "")
	{
		//alert("请选择左边栏的群组！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_selectleftgroup'));
		return;
	}
	var name = $('#udgNametemp').attr('value');
	var confire = getJsLocaleMessage('group','group_common_confire') ;
 	var cancel = getJsLocaleMessage('group','group_common_cancel') ;
	$('#gp_member_add').dialog({
		autoOpen: false,
		height: 606,
		width: 572,
		modal:true,
		buttons:{
			confire:function(){
					addMemberToGroup();
 			},
 			cancel:function(){
 				//window.location.href=location;
				$('#gp_member_add').dialog('close');
			}
		}
	});
    $('#gp_Name_bind').html(name);
	$("#gp_member_add").css("display","block");
 	 $('#gp_member_add').dialog('open');
}


function showAll2(gpAttribute)
{
	$('#phone').val("");
	$('#name').val("");
	$('#udgType').val("");
 /*	$('li span[link] a').each(function(e){
		$(this).removeClass("selected");
	});
	submitForm();*/
	var path = $("#path").val();
	var lguserid = $("#lguserid").val();
	if(gpAttribute == 1){
		window.location.href = path+'/mcsa_groupManage.htm?method=find&lguserid='+lguserid;
	}else if(gpAttribute == 0){
		window.location.href = path+'/grp_groupManage.htm?method=find&lguserid='+lguserid;
	}
}
function addMemberToGroup()
{
	var items = "";
 	$('input[name="checklist2"]:checked').each(function(){	
 		items += $(this).val().toString()+",";
 	});
 	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
 	}
 	if(items=="" || items.length == 0){
		//alert("请选择您要添加的成员！");
 		alert(getJsLocaleMessage('group','group_js_groupManageEqa_selectuseradded'));
		return;
	}
	var addrType = $("#addrType").val();
    var udgId = $('#udgIdtemp').val();
    var lgcorpcode = $('#lgcorpcode').val();
    var lguserid = $('#lguserid').val();

    $.post("grp_cliGroupManage.htm?method=addMemberToGroup",{
 					addrType:addrType,
 					items:items,
 					udgId:udgId,
 					lguserid:lguserid,
 					lgcorpcode:lgcorpcode
 					},
 						function(returnMsg){
			   				if(returnMsg.indexOf("html") > 0){
								//alert("网络超时，请重新登录！");
			   					alert(getJsLocaleMessage('group','group_common_nettimeout'));
				    			window.location.href=location;
				    		    return;
				    		}
			   				var arr = returnMsg.split("&");
			   				var badmoblie = arr[0];
			   				var num =  arr[1];
			   				if(num >= 1 && badmoblie.length >0){
			   				  //alert("成功添加"+num+"条数据 ，群组包含重复号码数据:"+badmoblie +"。");
			   					alert(getJsLocaleMessage('group','group_js_groupManageEqa_addsuc')+num+getJsLocaleMessage('group','group_js_groupManageEqa_infos')+","+getJsLocaleMessage('group','group_js_groupManageEqa_redata') + badmoblie +"。");
							  window.location.reload();
			   				}else if(num >= 1 &&  badmoblie.length == 0){
			   				  //alert("成功添加"+num+"条数据 ！");
			   					alert(getJsLocaleMessage('group','group_js_groupManageEqa_addsuc')+num+getJsLocaleMessage('group','group_js_groupManageEqa_infos'));
							  window.location.reload();
			   				}else if(num == 0 &&  badmoblie.length > 0){
			   				  //alert("群组包含重复号码数据:"+badmoblie +"。");
			   					alert(getJsLocaleMessage('group','group_js_groupManageEqa_redata') + badmoblie +"。");
							  window.location.reload();
			   				}else if(num == 0 &&  badmoblie.length == 0){
			   				   //alert("添加成员失败！");
			   					alert(getJsLocaleMessage('group','group_js_groupManageEqa_addmemeberfalse'));
			   				   return;
			   				}else{
			   				   //alert("添加成员失败！");
			   					alert(getJsLocaleMessage('group','group_js_groupManageEqa_addmemeberfalse'));
							   return;
			   				}
 						});
	
}

function getSearch()
{
	var udgId = $('#udgIdtemp').val();
	var l2gType = parseInt($("#addrType").val());
  	if(udgId == '')
	{
		//alert("请选择左边栏的群组！");
  		alert(getJsLocaleMessage('group','group_js_groupManageEqa_selectleftgroup'));
		return;
	}
	 
	if(l2gType == 0)
	{
  		getEmployeeT();
 		
	}
	else if(l2gType == 1)
	{
 		getClientT();
	}else if(l2gType == 2)
	{
 		getCuT();
	}
	$(".tabHead li a ").eq(3).trigger("click");
}

//员工通讯录
function getEmployeeT()
{
    // $("#table2DivName").val("getEmpTable");
	getAddrTable(0);
}

//客户通讯录
function getClientT()
{
	 
   // $("#table2DivName").val("getClientTable");
	getAddrTable(1);
}
//自定义通讯录
function getCuT()
{
	 
   // $("#table2DivName").val("getCustomerTable");
	//getAddrTable('getCustomerTable',2);
	getAddrTable(2)
}

function getAddrTable(l2gType)
{
 		var udgId = $('#udgIdtemp').val();
 					var path = $("#path").val();
			var time=new Date();
 	    var depId = $("#depId").val();
 	    $("#depIdtemp").val(depId);
 	    var lguserid = $("#lguserid").val();
 	    var lgcorpcode = $("#lgcorpcode").val();
 	    $('#personalSearchTable').load(path+'/grp_cliGroupManage.htm?method=getSepInfoByType&udgId='+udgId+'&l2gType='+l2gType+"&depId="+depId,{lgcorpcode:lgcorpcode,lguserid:lguserid,time:time});

}


function limit(){
	//只允许输入数字和';'
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
	$('#receiver').keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = !(((iKeyCode >= 48) && (iKeyCode <= 57))
				|| (iKeyCode == 59)
				|| (iKeyCode == 13)
				|| (iKeyCode == 46)
				|| (iKeyCode == 45)
				|| (iKeyCode == 37)
				|| (iKeyCode == 39) || (iKeyCode == 8));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
	//控制不能由输入法输入其他字符
	$('#receiver').keyup(function() {
		var str = $('#receiver').val();
		//只能输入0-9或者英文标点","符号、回车换行
		var reg = /[^0-9,\r\n]+/g;
		str = str.replace(reg, "");
		$('#receiver').val(str);
	});
}

function doNo1()
{
	$("#infoDiv").dialog("close");
}
function doNo2()
{
	$("#editInfoDiv").dialog("close");
}


// 显示详情  dw
function showGroupDetails(groupName,groupId,shareType)
{
	if(!isExistGroup(groupId)){
		//alert("未获取到群组信息，可能已删除！");
		alert(getJsLocaleMessage('group','group_page_groupMembers_groupisnull'));
		window.location.reload();
		return;
	}
	var path = $("#path").val();
	$("#udgIdtemp").val(groupId);
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
    var time=new Date();
    $('#groupDetail').load(path+'/grp_groupManage.htm?method=getTable',{udgid:groupId,lguserid:lguserid,lgcorpcode:lgcorpcode,time:time,shareType:shareType});
    $('#com_add_Dom2').dialog('open');
}

// 显示全部  YJM
function showAllGroup()
{
	var path = $("#path").val();
	var lguserid = $("#lguserid").val();
	window.location.href = path+'/grp_groupManage.htm?method=find&lguserid='+lguserid;
}

function closeDom2dialog()
{
	$('#com_add_Dom2').dialog('close');
	//submitForm();
}

function doEdit(l2gId,udgName,guid,moblie)
{
 	$("#own_group_update").css("display","block");
 	var confire = getJsLocaleMessage('group','group_common_confire') ;
 	var cancel = getJsLocaleMessage('group','group_common_cancel') ;
	$('#own_group_update').dialog({
		autoOpen: false,
		height: 134,
		width: 268,
		modal:true,
		buttons:{
			confire:function(){
		     updateGroupInfo(l2gId,udgName,guid,moblie);
 			},
 			cancel:function(){
				$('#own_group_update').dialog('close');
			}
		}
	});
 
	 $("#udgId").attr("option","selected");
	 $('#own_group_update').dialog('open');
}
function editGourpInfo(guid,groupName)
{
	$("#editInfoDiv").dialog("open");
	var skin = $("#skin").val();
	var commonPath = $("#commonPath").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var chooseType="1,4,9";
	groupName = encodeURIComponent(groupName);
	$("#getChooseMan").html("");
	var resultHtml="";
	var qzStr =",";
	var ygStr =",";
	var gxStr =",";
	var numb = 0;
	//guid = encodeURIComponent(guid);
	//guid = encodeURIComponent(guid);
	if(skin.indexOf("frame4.0") != -1) {
		var groupName = encodeURI(encodeURI(groupName));
		$("#editFrame").attr("src",commonPath+"/common/selectUserInfo.jsp?lguserid="+lguserid+"&chooseType="+chooseType+"&lgcorpcode="+lgcorpcode+"&groupName=" + groupName);
		changeDialogTitleCss('editInfoDiv');
		$.ajax({
			type: "POST",
			url: "grp_groupManage.htm?method=doEditGroupInfo&udgId="+guid+"&lguserid="+lguserid+"&upFlag=1",
			async:false,
			success:function(result){
				if (result!=null && result !=""){
					var map = eval('(' + result + ')');
					for(var key in map){
						if(key =="employee"){
							if(map[key] !=""){
								var listVal = eval('(' + map[key] + ')');
								for(var i=0;i<listVal.length;i++){
									resultHtml = resultHtml +"<div onclick='changeChooseDiv(this)' class='groupEdit' dataval='"+listVal[i].guId+"' isdep='4' et='' mobile='"+listVal[i].mobile+"'>" +
											"<span class='selectedName' title='"+listVal[i].name+"'>"+listVal[i].name+"</span><span class='selectedMobile'>"+listVal[i].mobile+"</span><span class='selectedType'>员工</span></div>";
									ygStr = ygStr+listVal[i].guId+",";
									numb++;
								}
							}
						}else if(key =="self"){
							if(map[key] !=""){
								var listVal1 = eval('(' + map[key] + ')');
								for(var i=0;i<listVal1.length;i++){
									resultHtml = resultHtml +"<div onclick='changeChooseDiv(this)' class='groupEdit' dataval='"+listVal1[i].guId+"' isdep='6' et='' mobile='"+listVal1[i].mobile+"'>" +
											"<span class='selectedName' title='"+listVal1[i].name+"'>"+listVal1[i].name+"</span><span class='selectedMobile'>"+listVal1[i].mobile+"</span><span class='selectedType'>自建</span></div>";
									qzStr = qzStr+listVal1[i].guId+",";
									numb++;
								}
							}
						}else if(key =="share"){
							if(map[key] !=""){
								var listVal2 = eval('(' + map[key] + ')');
								for(var i=0;i<listVal2.length;i++){
									resultHtml = resultHtml +"<div onclick='changeChooseDiv(this)' class='groupEdit' dataval='"+listVal2[i].guId+"' isdep='13' et='' mobile='"+listVal2[i].mobile+"'>" +
											"<span class='selectedName' title='"+listVal2[i].name+"'>"+listVal2[i].name+"</span><span class='selectedMobile'>"+listVal2[i].mobile+"</span><span class='selectedType'>共享</span></div>";
                                    gxStr = gxStr+listVal2[i].guId+",";
									numb++;
								}
							}
						}
					}
					$("#rightSelectedUserOption").val();
					$("#choiceNum").html("");
					$("#rightSelectedUserOption").val(resultHtml);
					$("#choiceNum").html(numb);
					$("#qzStr").val(qzStr);
					$("#ygStr").val(ygStr);
					$("#gxStr").val(gxStr);
					$("#curId").val(guid);
				}
				
			}
		});
	}else{
		if(!isExistGroup(guid)){
			//alert("未获取到群组信息，可能已删除！");
			alert(getJsLocaleMessage('group','group_page_groupMembers_groupisnull'));
			window.location.reload();
			return;
		}
		var lguserid = $("#lguserid").val();
		window.frames["editFrame"].location.href="grp_groupManage.htm?method=doEditGroupInfo&udgId="+guid+"&lguserid="+lguserid;
	}
	
}

// 删除群组  YJM
function deleteGroup(){
	var lguserid = $("#lguserid").val();
	var items = "";
 	$('input[name="checklist"]:checked').each(function(){	
 		items += $(this).val().toString()+",";
 	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
 	}
 	if(items == ""){
		//alert("请选择您要删除的群组记录！");
 		alert(getJsLocaleMessage('group','group_js_groupManageEqa_selectgrouprecorddel'));
		return;
	}else{
		if(confirm(getJsLocaleMessage('group','group_common_deletegroup'))==true){
				$.post("grp_groupManage.htm?method=deleteGroup",{ids:items,lguserid:lguserid},function(returnMsg){
					if(returnMsg == "true")
					{
 						//alert("删除成功！");
						alert(getJsLocaleMessage('group','group_common_deletesuc'));
 						window.location.href = pathUrl+"/grp_groupManage.htm?method=find&lguserid="+lguserid;
					}else if(returnMsg == "false"){
						//alert("删除失败！");
						alert(getJsLocaleMessage('group','group_common_deletefalse'));
					}else if(returnMsg == "errer"){
						//alert("删除失败！");
						alert(getJsLocaleMessage('group','group_common_deletefalse'));
					}else{
						//alert("请检查网络/数据库连接是否正常！");
						alert(getJsLocaleMessage('group','group_common_checknet'));
						window.location.href = pathUrl+"/grp_groupManage.htm?method=find&lguserid="+lguserid;
					}
				});
		}
	}
}

function showShare_errer(groupId,udgName)
{
    $("#groupId").val(groupId);
    $("#groupName").val(udgName);
    $(window.frames['flowFrame'].document).find("#groupName").html(udgName);
	$(window.frames['flowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());
	$("#depIdStrTemp").val($("#depIdStr").val());
	//window.frames['flowFrame'].window.frames['sonFrame'].a();
	$(window.frames['flowFrame'].document).find('#sonFrame')[0].contentWindow.a();
	$("#shareDiv").dialog("open");
}
   function checkAlls(e)    
{  
		
	var a = document.getElementsByName("checklist");    
	var n = a.length;    
	for (var i=0; i<n; i=i+1) {
		a[i].checked =e.checked;   
	}   
}

function changeDialogTitleCss(id) {
	var $titleBar = $("#ui-dialog-title-" + id);
    $titleBar.parent().addClass("titleBar");
    $titleBar.addClass("titleBarTxt");
}
function showShare(groupId,udgName)
{
	$("#groupId").val(groupId);
    $("#_groupName").val(udgName);
    $(window.frames['shareflowFrame'].document).find("#groupName").html(udgName);
	var commonPath = $("#commonPath").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var iPath = $("#iPath").val();
	var chooseType="8";
	if(skin.indexOf("frame4.0") != -1) {
		$("#shareflowFrame").attr("src",commonPath+"/common/selectUserInfo.jsp?lguserid="+lguserid+"&chooseType="+chooseType+"&lgcorpcode="+lgcorpcode);
		//$("#sonFrame").attr("src",commonPath+"/common/selectUserDepTree.jsp");
		$("#choose_Type").html("<option value='8' id='operatorTree'>操作员机构</option>");
		changeDialogTitleCss('shareDiv');
		$("#ui-dialog-title-shareDiv").html("当前群组："+udgName);
	}else{
		if(!isExistGroup(groupId)){
			//alert("未获取到群组信息，可能已删除！");
			alert(getJsLocaleMessage('group','group_page_groupMembers_groupisnull'));
			window.location.reload();
			return;
		}
		$(window.frames['shareflowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());
		$("#depIdStrTemp").val($("#depIdStr").val());
	}
    $("#rightSelectedUserOption").val("");
    $("#choiceNum").html("0");
	$.ajax({
		type: "POST",
		url: "grp_groupManage.htm?method=getSharedUser&groupid="+groupId,
		async:false,
		success:function(result){
			if("illegal" == result || "noexist" == result){
				//alert("未获取到群组信息，可能已删除！");
				alert(getJsLocaleMessage('group','group_page_groupMembers_groupisnull'));
				window.location.reload();
			}else if("false" == result){
				//alert("操作异常！");
				alert(getJsLocaleMessage('group','group_common_opterror'));
				window.location.reload();
			}else{
				if(result !== ""){
                    var resultHtml ="";
                    var getChooseManHtml = "";
                    var obj = eval('(' + result + ')');
                    if(skin.indexOf("frame4.0") != -1){
                        for(var i=0;i<obj.length;i++){
                            resultHtml = resultHtml+"<option et='1' value='"+obj[i].userId+"' mobile='"+obj[i].mobile+"' isdep='11'>[操作员]"+obj[i].name+"</option>"
                        }
                        $("#rightSelectedUserOption").val(resultHtml);
                        $("#choiceNum").html(obj.length);
                    }else{
                        for(var i=0;i<obj.length;i++){
                            resultHtml = resultHtml+"<option value='"+obj[i].userId+"' mobile='"+obj[i].mobile+"'>[操作员]"+obj[i].name+"</option>"
                        }
                        $(window.frames['shareflowFrame'].document).find("#right").html(resultHtml);
                        if($(window.frames['shareflowFrame'].document).find("#right").children().size()>0){
                            $(window.frames['shareflowFrame'].document).find("#isAdd").val(0);
                        }
                    }
				}
			}
		}
	});
	$("#shareDiv").dialog("open");
	
}
function doOk() {
	var eIds = "";
	if ($(window.frames['shareflowFrame'].document).find("#isAdd").val()==1&&$(window.frames['shareflowFrame'].document).find("#right option").size() < 1) {
		//alert("您没有选择任何人员！");
		alert(getJsLocaleMessage('group','group_js_groupManageEqa_nouserselected'));
		return;
	}
	//先清空原有数据
	$("#userIdStr").val('');
	$(window.frames['shareflowFrame'].document).find("#right option").each(function() {
		eIds = $(this).val();
		if(skin.indexOf("frame4.0") != -1) {
			if($(this).attr("isdep")!=="12"){ 
				$("#userIdStr").val($("#userIdStr").val()+eIds+",");
			}
		}else{
			if(!$(this).attr("isdep")){ 
				$("#userIdStr").val($("#userIdStr").val()+eIds+",");
			}
		}
	});
	if(!isExistGroup($("#groupId").val())){
		//alert("未获取到群组信息，可能已删除！");
		alert(getJsLocaleMessage('group','group_page_groupMembers_groupisnull'));
		window.location.reload();
		return;
	}
	$("#shareSubmit").attr("disabled","disabled");
	$("#shareCancel").attr("disabled","disabled");
	$.post("grp_groupManage.htm?method=groupShare",
	{depIdStr:$("#depIdStr").val(),
	lgcorpcode:$("#lgcorpcode").val(),
	groupId:$("#groupId").val(),
	lguserid:$("#lguserid").val(),
	groupName:$("#_groupName").val(),
	userIdStr:$("#userIdStr").val()}
	,function(returnMsg){
	    if(returnMsg == "noShareSelf"){
	        //alert("不能将群组共享给自己！");
	    	alert(getJsLocaleMessage('group','group_js_groupManageEqa_notsharemyself'));
	        $("#shareSubmit").attr("disabled","");
			$("#shareCancel").attr("disabled","");
	    }else if(returnMsg == "havingShare"){
	    	//alert("该群组已共享过给所选操作员，无需再共享！");
	    	alert(getJsLocaleMessage('group','group_js_groupManageEqa_hasshread'));
	    	$("#shareSubmit").attr("disabled","");
			$("#shareCancel").attr("disabled","");
	    }else if(returnMsg == "true")
		{
			//alert("共享成功！");
	    	alert(getJsLocaleMessage('group','group_js_groupManageEqa_sharesuc'));
			$("#shareDiv").dialog("close");
			window.location.href = pathUrl+"/grp_groupManage.htm?method=find&lguserid="+$("#lguserid").val();
		}else if(returnMsg == "false"){
			//alert("共享失败！");
			alert(getJsLocaleMessage('group','group_js_groupManageEqa_sharefalse'));
			$("#shareSubmit").attr("disabled","");
			$("#shareCancel").attr("disabled","");
			$("#shareDiv").dialog("close");
		}
	});
}
function doNo(){
	$("#depIdStr").val($("#depIdStrTemp").val());//将机构id字符串还原
	$("#depIdStrTemp").val("");
	$("#userIsStr").val("");
	$("#groupId").val("");
	$("#_groupName").val("");
	$(window.frames['shareflowFrame'].document).find("#epname").val("");
	$(window.frames['shareflowFrame'].document).find("#left").empty();
	$(window.frames['shareflowFrame'].document).find("#right").empty();
	$(window.frames['shareflowFrame'].document).find("#rightSelectTemp").empty();
	$("#shareDiv").dialog("close");
}

/*
校验群组是否存在
 */
function isExistGroup(id){
	if(!id){
		return false;
	}
	var isExist = false;
	$.ajax({
		type: "GET",
		async: false,
		url: "grp_groupManage.htm",
		data: {method:"existGroup",groupId:id},
		success: function(result){
			if("true" == result){
				isExist = true;
			}
		}
	});
	return isExist;
}