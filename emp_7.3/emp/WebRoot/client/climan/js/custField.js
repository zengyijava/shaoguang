//var fieldStr = "<客户号><姓名><手机><职务><行业><所属机构><性别><客户经理><QQ><所属区域><MSN><客户描述><E-mail><座机><生日>";
var fieldStr = getJsLocaleMessage('client','client_js_custField_fieldStr') ;
function submitForm(){
	var time = new Date();
	 var name = $("#name").val();
	 var phone = $("#phone").val();
	 var udgType = $("#udgType").val();
	 var depId = $("#depId").val();
    	$('#bookInfo').load($('#servletUrl').val(),
		{
			method:'getTable',
			time:time,
			name:name,
			lgcorpcode:$("#lgcorpcode").val(),
			phone:phone,
			udgType:depId,
			depId:depId,
			l2gType:udgType,
			pageIndex:$('#txtPage').val(),
			pageSize:$('#pageSize').val()
		},function(){
			afterSubmitForm();
		}
	);
}
function addFieldValue()
{
	var fieldID = $('#udgIdtemp').val();

	var fieldName = $('#udgNametemp').val();
	 
 	if(fieldID == "")
	{
		//alert("请选择客户属性！");
 		alert(getJsLocaleMessage('client','client_js_custField_selectattr'));
		return;
	}
	
	$("#ct_values_add").css("display","block");
	$('#ct_values_add').dialog({
		autoOpen: false,
		height: 200,
		width: 400,
		modal:true,
		close:function()
		{
			$("#ct_values_add").find("input[type='button']").attr("disabled","");
		}
	});
 
 	$('#ct_values_add').dialog('open');
 	$("#fieldID").val(fieldID);
	$("#fieldName").html(fieldName);
 	 
}

function saveFieldValue()
{
	var fieldID = $("#fieldID").val();
	var fieldValue = $("#newFieldValue").val();

	var fieldValueLength  =  document.getElementById('newFieldValue').value;
	fieldValue = $.trim(fieldValue)
	if( fieldValue== "")
	{
		//alert("属性值不能为空！");
		alert(getJsLocaleMessage('client','client_js_custField_attrisnotnull'));
		return;
	}else if(fieldValueLength.length>32){
	//alert("属性值长度过长！");
		alert(getJsLocaleMessage('client','client_js_custField_attrtoolong'));
		return;
	}
	$("#ct_values_add").find("input[type='button']").attr("disabled","disabled");
	$.post("cli_custFieldManger.htm?method=checkFieldValue", {
		fieldID : fieldID,
		fieldValue : fieldValue
	}, function(result) {
			if(result =='true' )
			{
 				//alert("属性值已经存在，请重新输入！");
 				alert(getJsLocaleMessage('client','client_js_custField_reattr'));
 				$("#ct_values_add").find("input[type='button']").attr("disabled","");
  			}else if(result =='false')
 			{
  				var lgcorpcode = $("#lgcorpcode").val();
 				$.post("cli_custFieldManger.htm?method=EditCustFieldValue",
 						{
 					fieldID : fieldID,
 					fieldValue : fieldValue,
 					lgcorpcode:lgcorpcode
 						},
 						function(result){
 							if(result == 'true')
 							{
 								//alert("操作成功！");
 								alert(getJsLocaleMessage('client','client_common_optsuc'));
 								//window.location.reload();
 								submitForm();
 								$('#ct_values_add').dialog('close');
 								$('#newFieldValue').val("");
 							}else
 							{
 								$("#ct_values_add").find("input[type='button']").attr("disabled","");
 								//alert("操作失败！");
 								alert(getJsLocaleMessage('client','client_common_optfalse'));
 							}
 					
 				});
 			}
		});


}
var doEditIdTemp;
var doEditFieldIDTemp;
var doEditFieldValueTemp;

function doEdit(id,fieldID,fieldValue)
{
	doEditIdTemp = id;
	doEditFieldIDTemp = fieldID;
	doEditFieldValueTemp = fieldValue;
 	$("#ct_values_update").css("display","block");
 	
	$('#ct_values_update').dialog({
		autoOpen: false,
		height: 134,
		width: 268,
		modal:true
	});
	resizeDialog($("#ct_values_update"),"ydkfDialogJson","kfdx_kftxlgl_test2");
	 $('#ct_values_update').dialog('open');
	 
	 $("#fieldValue").val(fieldValue);
}

function editCustFieldValue(id,fieldID,newValue,oldValue)
{
	newValue=$.trim(newValue)
	if(newValue == ""){
		//alert("属性值不能为空！");
		alert(getJsLocaleMessage('client','client_js_custField_attrisnotnull'));
		return;
	}
	else if(newValue == oldValue){
		//alert("操作成功！");
		alert(getJsLocaleMessage('client','client_common_optsuc'));
		window.location.reload();
	}
	else if(newValue.length>32){
		//alert("属性值长度过长！");
		alert(getJsLocaleMessage('client','client_js_custField_attrtoolong'));
		return;
	}
	else{
		$.post("cli_custFieldManger.htm?method=checkFieldValue", {
		ID : id,
		fieldID : fieldID,
		fieldValue : newValue
		}, function(result) {
			if(result =='true' )
			{
 				//alert("属性值已经存在，请重新输入！");
				alert(getJsLocaleMessage('client','client_js_custField_reattr'));
  			}else if(result =='false')
 			{
  				var lgcorpcode = $("#lgcorpcode").val();
 				$.post("cli_custFieldManger.htm?method=EditCustFieldValue",{lgcorpcode:lgcorpcode,ID:id,fieldValue:newValue},function(result){
					if(result=='true')
					{
						//alert("操作成功！");
						alert(getJsLocaleMessage('client','client_common_optsuc'));
						//window.location.reload();
						submitForm();
						$('#ct_values_update').dialog('close');
					}else{
						//alert("操作失败！");
						alert(getJsLocaleMessage('client','client_common_optfalse'));
						//window.location.reload();
						$('#ct_values_update').dialog('close');
					}
				});
 			}
		});	
	}	
		
}

function delAll(){
	var items = "";
 	$('input[name="checklist"]:checked').each(function(){	
 		items += $(this).val().toString()+",";
 	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
 	}
 	if(items==""){
		//alert("请选择您要删除的属性值！");
 		alert(getJsLocaleMessage('client','client_js_custField_selectattrdel'));
	}else{
	if(confirm(getJsLocaleMessage('client','client_js_custField_confiredelattr'))==true){
				var lgcorpcode = $("#lgcorpcode").val();
				$.post("cli_custFieldManger.htm?method=delete",{lgcorpcode:lgcorpcode,ID:items},function(result){
					if(result>=1)
					{
 						//alert("操作成功！");
						alert(getJsLocaleMessage('client','client_common_optsuc'));
 						var udgId = $(".roleNameColor").find('#r').val();
 			 			$("#depId").val(udgId);
 						var time = new Date();
 						var lgcorpcode = $("#lgcorpcode").val();
 						$('#bookInfo').load($("#path").val()+'/cli_custFieldManger.htm',{method:'getTable',lgcorpcode:lgcorpcode,depId : udgId,time:time});
						//window.location.reload();
					}else{
						//alert("操作失败！");
						alert(getJsLocaleMessage('client','client_common_optfalse'));
						//window.location.reload();
					}
				});
				
	}
	}
}

function del(ID){
	
 	if(confirm(getJsLocaleMessage('client','client_js_custField_confiredelattrrecord')))
	{
 		var lgcorpcode = $("#lgcorpcode").val();
		$.post("cli_custFieldManger.htm?method=delete",{lgcorpcode:lgcorpcode,ID:ID},function(result){
			if(result>=1)
			{
 				//alert("删除成功！");
 				alert(getJsLocaleMessage('client','client_common_optsuc'));
				//window.location.reload();
 				submitForm();
			}else{
				//alert("删除失败！");
				alert(getJsLocaleMessage('client','client_common_optfalse'));
			}
		});
	}
}

function bindCTClik(){
	$('#siderList p').click(function() { 
		// 点击角色名将其显示到文本框
		$("#siderList p").removeClass("roleNameColor");
		$(this).addClass("roleNameColor");
	 			var udgId = $(this).find('#r').val();
	 			$("#depId").val(udgId);
	 			var udgName= $(this).text();
	 			$('#udgIdtemp').attr('value', udgId);
				$('#udgNametemp').attr('value',udgName);
			    $('#udgName_old').attr('value',udgName);
			    $("#udgName2").val(udgName);
			    var lgcorpcode = $("#lgcorpcode").val();
					var time = new Date();
				
	 				$('#bookInfo').load($("#path").val()+'/cli_custFieldManger.htm',{method:'getTable',lgcorpcode:lgcorpcode,depId : udgId,time:time});
	 		
		});
}


 

function delGroup()
{
	var udgIdtemp = $('#udgIdtemp').val();
	if(udgIdtemp == "")
	{
		//alert("请选择要删除的属性！");
		alert(getJsLocaleMessage('client','client_js_custField_selectdelattrubites'));
		return;
	}else
	{
	
		
		$.post("cli_custFieldManger.htm?method=checkCustField", {
			udgId : udgIdtemp
		}, function(result) {
				if(result =='true' && confirm(getJsLocaleMessage('client','client_js_custField_confiretodelthisattr')))
				{
					 
					 delGpMethod(udgIdtemp);

					 
				}else if(result=='false' && confirm(getJsLocaleMessage('client','client_js_custField_confiredelcurattr')))
				{
					delGpMethod(udgIdtemp);
 				}
			});
	}
	
	 
}

function delGpMethod(udgId)
{
	var lgcorpcode = $("#lgcorpcode").val();
			$.post("cli_custFieldManger.htm?method=delGroup", {
			udgId : udgId,
			lgcorpcode:lgcorpcode
		}, function(result) {
			if(result >=1)
			{	
				//alert("删除属性成功！");
				alert(getJsLocaleMessage('client','client_js_custField_delattrsuc'));
				window.location.reload();
	
			}else if(result ==-1){
				//alert("该属性不存在，可能已被删除！");
				alert(getJsLocaleMessage('client','client_js_custField_attrisnull'));
				window.location.reload();
			}else{
				//alert("删除属性失败！");
				alert(getJsLocaleMessage('client','client_js_custField_delattrfalse'));
			}
		});
	
	
}

function addCField()
{
	
 	$("#group_add").css("display","block");
	$('#group_add').dialog({
		autoOpen: false,
		height: 200,
		width: "zh_HK"===getJsLocaleMessage("common","common_empLangName")?500:400,
		modal:true,
		close:function()
		{
			$("#group_add_ok").attr("disabled","");
			$("#udgName").val(""); 
			$("#group_add_cancel").attr("disabled","");
		}
	});
	$('#group_add').dialog('open');
 	$("#radio1").attr("checked",true);
}

//添加属性的弹出框点击取消按钮
function group_add_cancel()
{
	$('#group_add').dialog('close');
}
function cFieldAdd()
{

	var udgName = $("#udgName").val();
	udgName = $.trim(udgName);
	if(fieldStr.indexOf("<"+udgName.toUpperCase()+">")>=0){
		//alert("该属性在客戶基本信息中已存在！");
		alert(getJsLocaleMessage('client','client_js_custField_attrexcite'));
		return false;
	}
	var groupType = $("input[name='oneradio']:checked").val();
	
	
	var field_ref = $("#field_ref").val();
	var udgId = "";
	if(udgName == "")
	{
		//alert("请输入要新增的属性名称！");
		alert(getJsLocaleMessage('client','client_js_custField_enterattrname'));
		return;
	}else{
		var lgcorpcode = $("#lgcorpcode").val();
		$("#group_add_ok").attr("disabled","disabled");
		$("#group_add_cancel").attr("disabled","disabled");
		$.post("cli_custFieldManger.htm?method=checkGpName", {
			udgName : udgName,
			lgcorpcode:lgcorpcode,
			groupType:groupType,
			udgId:udgId
		}, function(result) {
				if(result =='true' )
				{
	 				//alert("当前属性名称已经存在，请重新输入！");
					alert(getJsLocaleMessage('client','client_js_custField_reattrname'));
	 				$("#group_add_ok").attr("disabled","");
	 				$("#group_add_cancel").attr("disabled","");
	  			}else if(result =='false')
	 			{
	  				var lgcorpcode = $("#lgcorpcode").val();
	  				var lguserid = $("#lguserid").val();
	  				$.post("cli_custFieldManger.htm?method=addCustField", {
	  					udgName : udgName,
	  					groupType : groupType,
	  					lguserid:lguserid,
	  					lgcorpcode:lgcorpcode,
	  					field_ref :field_ref
	  				}, function(result) {
	  					if(result =='true')
	  					{
	  						//alert("新建属性成功！");
	  						alert(getJsLocaleMessage('client','client_js_custField_addattrsuc'));
	  						$("#group_add_ok").attr("disabled","");
	  		 				$("#group_add_cancel").attr("disabled","");
	  						window.location.reload();

	  					}else
	  					{
	  						$("#group_add_ok").attr("disabled","");
	  		 				$("#group_add_cancel").attr("disabled","");
	  						//alert("新建属性失败！");
	  		 				alert(getJsLocaleMessage('client','client_js_custField_addattrfalse'));
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
		//alert("请选择客户属性！");
 		alert(getJsLocaleMessage('client','client_js_custField_selectattr'));
		return;
	}
	$.post("cli_custFieldManger.htm?method=getCustFileByid", {
	
		udgId:udgId
	}, function(result) {
		if(result != ""){
			 var custFields = result.split(";");
			
			 
			  var fName = $("#fName").val(custFields[0]);

			  if(custFields[1] == 0){
	
			  	$("#ljradio1").attr("checked","checked");
			  }else {
		
			  $("#ljradio2").attr("checked","checked");
			  }
		 	 
			 
		}
       
	});
	$("#udgName2").val(udgName);
	$("#group_update2").css("display","block");
	$('#group_update2').dialog({
		autoOpen: false,
		height: 150,
		width: "zh_HK"===getJsLocaleMessage("common","common_empLangName")?350:290,
		modal:true
	});
 
 	 $('#group_update2').dialog('open');
}

//修改客户属性时确定按钮onclick事件
function editGroupProperty()
{
	var udgId = $('#udgIdtemp').val();
	updateGpName(udgId);
}

//修改客户属性时取消按钮onclick事件
function closeEidtGroupBox()
{
	$('#group_update2').dialog('close');
}


function updateGpName(udgId)
{
	var fName = $("#fName").val();
	fName = $.trim(fName);
	if(fName=="")
	{
		//alert("请输入属性名称！");
		alert(getJsLocaleMessage('client','client_js_custField_enterattrname'));
		return;
	}
	if(fieldStr.indexOf("<"+fName.toUpperCase()+">")>=0){
		//alert("该属性在客戶基本信息中已存在！");
		alert(getJsLocaleMessage('client','client_js_custField_atteisexcite'));
		return false;
	}
	var vtype = $("input[name='vradio']:checked").val();
	var udgName_old = $("#udgName_old").val();
	
	
	$("#group_update2_ok").attr("disabled","disabled");
	if($.trim(udgName_old)==$.trim(fName))
	{
		var lgcorpcode = $("#lgcorpcode").val();
		$.post("cli_custFieldManger.htm?method=updateGroupName",
 						{
 					udgId:udgId,
 					udgName:fName,
 					lgcorpcode:lgcorpcode,
 					 vtype:vtype  
 						},
 						function(result){
 							$("#group_update2_ok").attr("disabled",false);
 							if(result == 'true')
 							{
 								//alert("修改成功！");
 								alert(getJsLocaleMessage('client','client_common_modifysuc'));
 								window.location.reload();
 							}else if(result == 'notexit')
 							{
 								//多用户操作时候，会出现该情况
 								//alert("该属性不存在，可能已被删除！");
 								alert(getJsLocaleMessage('client','client_js_custField_attrisnull'));
 								window.location.reload();
 							}else{
 								//alert("修改失败！");
 								alert(getJsLocaleMessage('client','client_common_modifyfalse'));
 							}
 					
 				});
	
	}else{
		var lgcorpcode = $("#lgcorpcode").val();
	$.post("cli_custFieldManger.htm?method=checkGpName", {
		udgName:fName,
		lgcorpcode:lgcorpcode,
	  	udgId:udgId
	}, function(result) {
			if(result =='true' )
			{
 				//alert("当前属性名称已经存在，请重新输入！");
				alert(getJsLocaleMessage('client','client_js_custField_reattrname'));
 				$("#group_update2_ok").attr("disabled",false);
 				return;
  			}else{
				$.post("cli_custFieldManger.htm?method=updateGroupName",
 						{
 					udgId:udgId,
 					udgName:fName,
 					lgcorpcode:lgcorpcode,
 					 vtype:vtype  
 						},
 						function(result){
 							$("#group_update2_ok").attr("disabled",false);
 							if(result == 'true')
 							{
 								//alert("修改成功！");
 								alert(getJsLocaleMessage('client','client_common_modifysuc'));
 								window.location.reload();
 							}else if(result == 'notexit')
 							{
 								//多用户操作时候，会出现该情况
 								//alert("该属性不存在，可能已被删除！");
 								alert(getJsLocaleMessage('client','client_js_custField_attrisnull'));
 								window.location.reload();
 							}else
 							{
 								//alert("修改失败！");
 								alert(getJsLocaleMessage('client','client_common_modifyfalse'));
 							}
 					
 				});  			
  			}
  			
  		});
 		
	}
 }

function showAll2()
{
	$('#phone').val("");
	$('#name').val("");
	$('#udgType').val("");
 	$('li span[link] a').each(function(e){
		$(this).removeClass("selected");
	});
	submitForm();
}
function addMemberToGroup()
{
/*
	var items = "";
 	$('input[name="checklist2"]:checked').each(function(){	
 		items += $(this).val().toString()+",";
 	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
 	}
 	*/
 	   var items = $('#cTypeValue').val();
 	 
 	if(items=="" || items.length == 0){
		//alert("请选择输入属性值！");
 		alert(getJsLocaleMessage('client','client_js_custField_enterattrvalue'));
		return;
	}
	var addrType = $("#addrType").val();
    var udgId = $('#udgIdtemp').val();
    var lgcorpcode = $("#lgcorpcode").val();
    $.post("cli_custFieldManger.htm?method=addMemberToGroup",
 						{
 					addrType:addrType,
 					items:items,
 					lgcorpcode:lgcorpcode,
 					udgId:udgId
 						},
 						function(result){
 							if(result>=1)
 							{
 							    //alert("添加属性值成功！");
 								alert(getJsLocaleMessage('client','client_js_custField_addattrvaluesuc'));
 								window.location.reload();
 							}else if(result==-1)
 							{
 							   // alert("添加属性值失败！属性里已经有这个属性值了！");
 								alert(getJsLocaleMessage('client','client_js_custField_attrvalexcite'));
 								window.location.reload();
 							}else
 							{
 									//alert("添加属性值失败！");
 								alert(getJsLocaleMessage('client','client_js_custField_addattrvalfalse'));
 							}
 					
 				});
	
}

function getSearch()
{
	var udgId = $('#udgIdtemp').val();
	var l2gType = parseInt($("#addrType").val());
  	if(udgId == '')
	{
		//alert("请选择左边栏的属性！");
  		alert(getJsLocaleMessage('client','client_js_custField_selectleftattr'));
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
 	    $('#personalSearchTable').load(path+'/cli_custFieldManger.htm?method=getSepInfoByType&udgId='+udgId+'&l2gType='+l2gType+"&depId="+depId,
 	    		{time:time},function(){
 	    			afterSubmitForm();
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
function afterSubmitForm()
{
	aaaaa = 3;
	setLeftHeight2();
	aaaaa = 0;
}
$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
	var time = new Date();
	var lgcorpcode = $("#lgcorpcode").val();
	$('#bookInfo').load('cli_custFieldManger.htm',{method:'getTable',time:time,lgcorpcode:lgcorpcode});
		
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$('#content tbody tr').hover(function() {
		//鼠标移到元素上要触发的函数
		$(this).css('background-color', '#c1ebff');
	}, function() {
		//鼠标移出元素要触发的函数
		$(this).css('background-color', '#FFFFFF');
	});

	$("#addDepNew").hover(function() {
		$(this).removeClass("depOperateButton1");
		$(this).addClass("depOperateButton1On");
	}, function() {
		$(this).addClass("depOperateButton1");
		$(this).removeClass("depOperateButton1On");
	});

	$("#updateDepNew").hover(function() {
		$(this).removeClass("depOperateButton2");
		$(this).addClass("depOperateButton2On");
	}, function() {
		$(this).addClass("depOperateButton2");
		$(this).removeClass("depOperateButton2On");
	});

	$("#delDepNew").hover(function() {
		$(this).removeClass("depOperateButton3");
		$(this).addClass("depOperateButton3On");
	}, function() {
		$(this).addClass("depOperateButton3");
		$(this).removeClass("depOperateButton3On");
	});
	
	bindCTClik();
	//机构树高度自适应
	setLeftHeight();
}); 