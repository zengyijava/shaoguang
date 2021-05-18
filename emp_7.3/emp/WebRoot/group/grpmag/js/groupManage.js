


var total=0;// 总页数
var pageIndex=0;// 当前页数
var pageSize=0;// 每页记录数
var totalRec=0;// 总记录数
function initPage(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total=total;
	this.pageIndex=pageIndex;
	this.pageSize=pageSize;
	this.totalRec=totalRec;
	
	showPageInfo();
}

//跳到第几页
//表单提交
function goPage(i){
	var page;
	if(i<0){
		page=$("#txtPage").attr("value");
	}else{
		page=i;
	}
	var size=$('#pageSize').attr("value");
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total;
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的整数！");
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm'].elements["pageIndex"].value =1;
		submitForm();
		return;
	}
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		document.forms['pageForm'].elements["pageIndex"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		return;
	}
	document.forms['pageForm'].elements["pageIndex"].value=page;
	submitForm();
}

function afterSubmitForm()
{
	aaaaa = 3;
	setLeftHeight2();
	aaaaa = 0;
}

function submitForm(){
	var time = new Date();
	 var name = $("#name").val();
	 var phone = $("#phone").val();
	 var udgType = $("#udgType").val();
	 var depId = $("#depId").val();
	 var udgTypeId = $("#udgTypeId").val();
	 var lguserid = $("#lguserid").val();
    	$('#bookInfo').load($('#servletUrl').val(),
		{
			method:'getTable',
			time:time,
			name:name,
			phone:phone,
			udgTypeId:udgTypeId,
			l2gType:udgType,
			pageIndex:$('#txtPage').val(),
			pageSize:$('#pageSize').val(),
			lguserid:lguserid
		},function(){
			afterSubmitForm();
		});
	
}

function showPageInfo(){
	var $pa = $('#pageInfo');
	$pa.empty();

	$pa.append('<span id="p_goto" onclick="jumpMenu()"></span>');
	if(pageIndex<total){
		$pa.append('<a class="p_a_h" id="p_last" href="javascript:goPage('+total+')"></a>');
		$pa.append('<a id="p_next" href="javascript:goPage('+(pageIndex-0+1)+')" class="p_a_h" ></a>');
	}else{
		$pa.append('<a id="p_last" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_next" href="javascript:void(0)"></a>');
	}
	if(pageIndex<=1){
		$pa.append('<a id="p_back" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_first" href="javascript:void(0)"></a>');
	}else{
		$pa.append('<a class="p_a_h" id="p_back" href="javascript:goPage('+(pageIndex-1)+')"></a>');
		$pa.append('<a class="p_a_h" id="p_first" href="javascript:goPage(1)"></a>');
	}
	
	$pa.append('<span id="p_info">共'+totalRec+'条，第'+pageIndex+'/'+total+'页，'+pageSize+'条/页</span>');
	$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
}
function jumpMenu()
{
	
	var bodyheight=$(window).height();
	var top = $("#p_goto").offset().top;
	var left = $("#p_goto").offset().left;
	if($("#p_jump_menu").html()==null)
	{
		var newDiv = document.createElement("div");
		newDiv.id = "p_jump_menu";
		newDiv.style.position = "absolute";
		newDiv.style.top = "0";
		newDiv.style.left = "0";
		newDiv.style.zIndex = "100";
		newDiv.className="div_bd div_bg";
		newDiv.innerHTML = "<center><div>第<input name='page_input' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />页," +
			"<input name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />条/页</div>" +
			"<div><a id='p_jump_sure' href='javascript:jumpSure()' ></a></div></center><input type='hidden' id='isHd'/>";
		document.body.appendChild(newDiv);
		
		$("#p_jump_menu").show();
		$("#page_input").focus();
	}
	left = left - $("#p_jump_menu").width()+$("#p_goto").width();
	if(top+84 > bodyheight)
	{
		top = top - $("#p_jump_menu").height()-4;
	}else
	{
		top = top + $("#p_goto").height()+4;
	}
	
	$("#p_jump_menu").css("left",left+"px")
	$("#p_jump_menu").css("top",top+"px")
	$("#p_jump_menu").show();
	$("#page_input").focus();
}
function jumpDisplay(ddd)
{
	$("#isHd").val(ddd);
	setTimeout("isHid()",200);
}
function isHid()
{
	if($("#isHd").val() == 0)
	{
		 $("#p_jump_menu").hide();
	}
}
function jumpSure()
{
	var page=$("#page_input").val();
	var size=$("#size_input").val();
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total;
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		//document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input").focus();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		$("#page_input").focus();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的整数！");
		$("#size_input").focus();
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		$("#size_input").focus();
		return;
	}
	$("#pageSize").val($("#size_input").val());
	$("#txtPage").val($("#page_input").val());
	submitForm();
}
 
var doEditL2gId;
var doEditUdgName;
var doEditGuid;
var doEditMoblie;
function doEdit(l2gId,udgName,guid,moblie)
{
	doEditL2gId=l2gId;
	doEditUdgName=udgName;
	doEditGuid=guid;
	doEditMoblie=moblie;
 	$("#own_group_update").css("display","block");
	$('#own_group_update').dialog({
		autoOpen: false,
		height: 134,
		width: 268,
		modal:true
	});
	//$("#updateOK").click(function(){updateGroupInfo(l2gId,udgName,guid,moblie);});
	$("#updateCancel").click(function(){$('#own_group_update').dialog('close');});
	
	 $("#udgId").attr("option","selected");
	 $('#own_group_update').dialog('open');
}

function updateGroupInfo(l2gId,udgName,guid,moblie)
{
	var udgId = $("#udgId").val().split("&");
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
  	if(confirm("您确定要修改当前群组记录？")==true){
 		if(udgId[1].toString()==udgName.toString())
 		{
 			alert("当前所属群组未做修改！");
 			return;
 		}else{
 	     $.post("grp_cliGroupManage.htm?method=checkGrhaveMember", {
			udgId : udgId[0],
			personId:guid,
			moblie:moblie,
			lguserid:lguserid
		}, function(returnMsg) {
				if(returnMsg == 1){
					alert("此群组已存在该成员！");
	                return;
				}else if(returnMsg == 3){
					alert("此群组已存在此手机号码！");
	                return;
				}else if(returnMsg == 2){
					 $.post("grp_cliGroupManage.htm?method=updateGroupInfo",{l2gId:l2gId,udgId:udgId[0],lgcorpcode:lgcorpcode},function(result){
							if(result=='true')
							{
								alert("修改成功！");
								window.location.reload();
							}else{
								alert("修改失败！");
								window.location.reload();
							}
					 });
				}else{
					alert("查询数据失败！");
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
		alert("请选择您要删除的群组记录！");
		return;
	}else{
	if(confirm("您确定要删除当前群组记录？")==true){
				$.post("grp_cliGroupManage.htm?method=delete",{ids:items,lgcorpcode:lgcorpcode},function(result){
					if(result>=1)
					{
 						alert("删除成功,共删除"+result+"条信息！");
 						window.location.reload();
					}else if(result == 0){
						alert("删除失败！");
						//window.location.reload();
					}else{
						alert("请检查网络/数据库连接是否正常！");
						return;
					}
				});
				
	}
	}
}

function del(i){
	
	var lgcorpcode = $("#lgcorpcode").val();
 	if(confirm("您确定要删除当前群组记录？"))
	{
 		
		$.post("grp_cliGroupManage.htm?method=delete",{ids:i,lgcorpcode:lgcorpcode},function(result){
			if(result>=1)
			{
 				alert("删除成功！");
				window.location.reload();
			}else{
				alert("删除失败！");
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
		$('#bookInfo').load($("#path").val()+'/grp_cliGroupManage.htm',{method:'getTable',lguserid:lguserid,udgTypeId : udgId,time:time},function(){
			afterSubmitForm();
		});
	});
	
	$(".webwidget_scroller_tab").webwidget_scroller_tab({
        scroller_time_interval: '-1',
        scroller_window_padding: '10',
        scroller_window_width: '550',
        scroller_window_height: '480',
        scroller_head_text_color: '#000000',
        scroller_head_current_text_color: '#666',
        directory: 'images'
    });
}

function delGroup()
{
	var udgIdtemp = $('#udgIdtemp').val();
	if(udgIdtemp == "")
	{
		alert("请选择要删除的群组！");
		return;
	}else
	{
	
		
		$.post("grp_cliGroupManage.htm?method=checkMember", {
			udgId : udgIdtemp
		}, function(result) {
				if(result =='true' && confirm("当前群组下存在成员，您确定要删除当前群组？"))
				{
					 
					// delGpMethod(udgIdtemp);
					delClientGrp(udgIdtemp);
				}else if(result=='false'){
				  delGpMethod(udgIdtemp);
				}
			});
	}
	
	 
}

function delGpMethod(udgId)
{
	if(confirm("您确定要删除该群组？"))
	{
		
			$.post("grp_cliGroupManage.htm?method=delGroup", {
			udgId : udgId
		}, function(result) {
			if(result >=1)
			{	
				alert("删除群组成功！");
				window.location.reload();
	
			}else
			{
				alert("删除群组失败！");
			}
		});
	}
	
}
//删除 客户群组
function delClientGrp(udgId)
{
		$.post("grp_cliGroupManage.htm?method=delGroup", {
			udgId : udgId
		}, function(result) {
			if(result >=1)
			{	
				alert("删除群组成功！");
				window.location.href = window.location.href;
			}else
			{
				alert("删除群组失败！");
			}
		});
}


function addGroup()
{
 	$("#group_add").css("display","block");
	$('#group_add').dialog({
		autoOpen: false,
		height: 130,
		width: 290,
		modal:true
	});
 
 	 $('#group_add').dialog('open');
 	 $('#group_add_ok').click(function(){groupAdd();});
 	 $('#group_add_cancel').click(function(){$("#udgName").val("");$('#group_add').dialog('close');});
 	 
}

function groupAdd()
{
	var udgName = $("#udgName").val().replace(/(^\s*)|(\s*$)/g,"");
	var groupType = $("#groupType").val();
	var udgId = "";
	
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	
	if (udgName.indexOf(" ")>-1) 
	{  
	udgName=udgName.replace(/(^\s*)|(\s*$)/g,"");
	} 
	if (udgName.indexOf("'")!=-1 || outSpecialChar(udgName) ) 
	{  
		alert("请输入合法字符！");
		return;
	} 
	if(udgName == "") 
	{
		alert("请输入要新建的群组名称！");
		return;
	}else{
		$.post("grp_cliGroupManage.htm?method=checkGpName", {
			udgName : udgName,
			groupType:groupType,
			udgId:udgId,
			lguserid:lguserid
		}, function(result) {
				if(result =='true' )
				{
	 				alert("当前群组名称已经存在，请重新输入！");
	  			}else if(result =='false' && confirm("您确定要新建该群组？"))
	 			{
	  				$.post("grp_cliGroupManage.htm?method=addGroup", {
	  					udgName : udgName,
	  					groupType : groupType,
	  					lguserid:lguserid,
	  					lgcorpcode:lgcorpcode
	  				}, function(result) {
	  					if(result =='true')
	  					{
	  						alert("新建群组成功！");
	  						window.location.reload();

	  					}else
	  					{
	  						alert("新建群组失败！");
	  					}
	  				});
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
		alert("请选择要重命名的群组！");
		return;
	}
	$("#udgName2").val(udgName);
	$("#group_update").css("display","block");
	$('#group_update').dialog({
		autoOpen: false,
		height: 134,
		width: 268,
		modal:true
	});
 
 	 $('#group_update').dialog('open');
 	 $('#group_update_ok').click(function(){updateGpName(udgId);});
 	 $('#group_update_cancel').click(function(){$('#group_update').dialog('close');});
}

function updateGpName(udgId)
{
	var udgName = $("#udgName2").val();
	var udgName_old = $("#udgName_old").val();
	var lguserid = $("#lguserid").val();
	if(udgName_old == udgName)
	{
		alert("当前群组名称未做改变！");
		return;
	}
	if (udgName.indexOf("'")!=-1 || hasSpecialChar(udgName) ) 
	{  
		alert("请输入合法字符！");
		return ;
	} 
	$.post("grp_cliGroupManage.htm?method=checkGpName", {
		udgName : udgName,
		udgId:udgId,
		lguserid:lguserid
	}, function(result) {
			if(result =='true' )
			{
 				alert("当前群组名称已经存在，请重新输入！");
  			}else if(result =='false' && confirm("您确定要重命名该群组？"))
 			{
 				$.post("grp_cliGroupManage.htm?method=updateGroupName",
 						{
 					udgId:udgId,
 					udgName:udgName
 						},
 						function(result){
 							if(result == 'true')
 							{
 								alert("重命名成功！");
 								window.location.reload();
 							}else
 							{
 								alert("重命名失败！");
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
		alert("请选择左边栏的群组！");
		return;
	}
	var name = $('#udgNametemp').attr('value');
	$('#gp_member_add').dialog({
		autoOpen: false,
		height: 606,
		width: 572,
		modal:true,
		buttons:{
			"确定":function(){
					addMemberToGroup();
 			},
			"取消":function(){
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
		window.location.href = path+'/grp_cliGroupManage.htm?method=find&lguserid='+lguserid;
	}else if(gpAttribute == 0){
		window.location.href = path+'/eqa_groupManage.htm?method=find&lguserid='+lguserid;
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
		alert("请选择您要添加的成员！");
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
								alert("网络超时，请重新登录！");
				    			window.location.href=location;
				    		    return;
				    		}
			   				var arr = returnMsg.split("&");
			   				var badmoblie = arr[0];
			   				var num =  arr[1];
			   				if(num >= 1 && badmoblie.length >0){
			   				  alert("成功添加"+num+"条数据 ，群组包含重复号码数据:"+badmoblie +"。");
							  window.location.reload();
			   				}else if(num >= 1 &&  badmoblie.length == 0){
			   				  alert("成功添加"+num+"条数据 ！");
							  window.location.reload();
			   				}else if(num == 0 &&  badmoblie.length > 0){
			   				  alert("群组包含重复号码数据:"+badmoblie +"。");
							  window.location.reload();
			   				}else if(num == 0 &&  badmoblie.length == 0){
			   				   alert("添加成员失败！");
			   				   return;
			   				}else{
			   				   alert("添加成员失败！");
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
		alert("请选择左边栏的群组！");
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
 	    $('#personalSearchTable').load(path+'/grp_cliGroupManage.htm?method=getSepInfoByType&udgId='+udgId+'&l2gType='+l2gType+"&depId="+depId,{lgcorpcode:lgcorpcode,lguserid:lguserid,time:time},function(){
			afterSubmitForm();
		});

}

function noyinhao(obj)
{  
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
	$(obj).keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = ((iKeyCode == 39)
				|| (iKeyCode == 34));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
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

var aaaaa = 0 ;
function setLeftHeight()
{
	setLeftHeight2();
	window.onresize = function(){  
		if(aaaaa<1)
		{
			aaaaa=aaaaa+1;
		}else
		{
			aaaaa=0;
			return;
		}
		setLeftHeight2();
		setTimeout('aaaaa=0;',300);
	}
}
function setLeftHeight2()
{
	//var isIE = (document.all) ? true : false;
	//var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	var hei=$(window).height();
	var bodyhei = $('.right_info').height();
	var tophei = $('.top:visible').height();
	var topMargin = $('.top:visible').css('margin-top'); 
	if(tophei==null)
	{
		tophei = 0;
	}
	if(topMargin != null)
	{
		tophei = tophei + 5;
	}
	if(bodyhei > hei)
	{
		hei = bodyhei;
		$('.left_dep').css('height',hei-tophei);
		$('.left_dep .list ').css('height',hei-50-tophei);
	}else
	{
		$('.left_dep').css('height',hei-20-tophei);
		$('.left_dep .list ').css('height',hei-70-tophei);
	}
}