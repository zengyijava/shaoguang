

// 处理输入  框的
function checkText(ep,type)
{
	// var namereg = /[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g;
 	// if(namereg.test(ep.val())){
 	// ep.val("");
	// }
	if(ep.val()=="" && type == 1)
	{
		ep.next("label").css("display","none");
	}else if(ep.val()=="" && type == 2){
		ep.next("label").css("display","inline");
	}
}

// 选中所有
   function checkAlls(e)    
	{  
		var a = document.getElementsByName("checklist");    
	var n = a.length;    
	for (var i=0; i<n; i=i+1) {
		a[i].checked =e.checked;   
	}   
}
// 判断是否全部选中
   function checkCh(){
	   var a = document.getElementsByName("checklist");    
   var n = a.length; 
   var num = 0;
	$('input[name="checklist"]:checked').each(function(){	
		num = parseInt(num + 1);
	});
	if(n == num){
		$("#checkall").attr("checked","checked");
	}else{
		$("#checkall").attr("checked","");
		}
	}


	
	
   //删除模板
   function deleteDiv(count){
	  // 标识审核流程级别
   var divlevel = parseInt($("#divlevel").val());
   if(divlevel == 1){
	   	alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_43"));
		return;
   }
   // 获取审核DIV的等级 如果删除的是第一级
   var divser = $("#addFlowDiv"+count).find("label[class='nameclass']").attr("audlev");
   if(divser == "1"){
	   var num = 0;
	   var sernum = "";
	   // 找到第2个DIV的等级序列
		$("div[divattr='adddiv']").each(function(){
			num = parseInt(num +1);
			if(num == 2){
				sernum = $(this).attr("attrnum");
			}
	    });
	    // 获取第一级选择的值
	   var index = $("#type"+sernum).val();
	   var msg = returnSelec(index);
	   $("#type"+sernum).empty();
	   $("#type"+sernum).html(msg);
   }
   $("#addFlowDiv"+count).remove();
   divlevel = divlevel - 1;
   $("#divlevel").val(divlevel);
	   updateDivName();
	}

   // 选中 索引，返回select内容
function returnSelec(index){
	   var msg = "<option value='1'";
	   if(index == "1"){
		   msg = msg + " selected='selected' ";
	   }
	   msg = msg + ">"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_24")+"</option><option value='2'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_26")+"</option><option value='3' ";
	   if(index == "3"){
		   msg = msg + " selected='selected' ";
	   }
	   msg = msg + " >"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_25")+"</option>";
	   return msg;
}




// 增加审核流程DIV
function addFlowDiv(){
	var selIndex = "";
	var sernum = "";
	var count = 0;
	$("div[divattr='adddiv']").each(function(){
		count = count + 1;
		// 标识的是该审核div的隐藏的序列号
		// sernum = $(this).attr("attrnum");
		// isFlag = "2"
		// 所选择的索引 1操作员2逐级 3机构
	});
	// selIndex = $("#type"+sernum).val();
	// if(selIndex == 2){
	// alert("第一级是逐级审批,不允许添加！");
	// return;
	// }
	if(count == 5){
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_44"));
		return;
	}

	
	var ipath = $("#ipath").val();
	var lguserid = $("#lguserid").val();
	var divlevel = parseInt($("#divlevel").val());
	var divcount = parseInt($("#divcount").val());
	divcount = divcount + 1;
	divlevel = divlevel + 1;
	$("#divlevel").val(divlevel);
	$("#divcount").val(divcount);
	// alert(divlevel);
	// alert(divcount);
	
	
	var addDivStr = "<div class='itemDiv' style='height:120px;padding-top:20px;' id='addFlowDiv"+divcount+"'  attrnum='"+divcount+"' divattr='adddiv'>"
	+ "<table class='div_bg div_bd' style='height:120px;width:566px;' border='1'>"
	+ "<tr><td rowspan='3' style='width:90px;' class='div_bd'>"
	+ "<label class='nameclass' audlev=''></label><br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteDiv("+divcount+");'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_45")+"</a></td>"
	+ "<td height='35px;' class='div_bd'><select id='type"+divcount+"' name='type"+divcount+"'  style='width: 120px;margin-left:15px;height: 22px;' onchange='changeObj("+divcount
	+")'><option value='1'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_24")+"</option><option value='3'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_25")+"</option>"
	+ "</select><input type='hidden'  id='selindex"+divcount+"' name='selindex"+divcount+"' value='1'/>&nbsp;&nbsp;&nbsp;<input type='button'  id='add"+divcount+"' name='add"+divcount+"' value='"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_47")+"' class='btnClass1' onclick='javascript:addobj("+divcount+");'/><label id='auditcount"+divcount+"'  style='padding-left: 20px;'></label>" +
        "<a class='des'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_46")+"</a></td></tr>"
	+ "<tr><td height='50px;'  class='div_bd'><span id='flowobj"+divcount+"'  style='width: 470px;padding-left:5px;'></span></td></tr>"
	+ "<tr><td  class='div_bd' style='padding-left:10px; height:35px;'>&nbsp;&nbsp;<input name='isCheckFlow"+divcount+"'  id='isAllFlowY"+divcount+"' type='radio' value='1' checked='checked'/> "+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48")+"&nbsp;&nbsp;&nbsp;"
	+ "<input name='isCheckFlow"+divcount+"' id='isAllFlow"+divcount+"' type='radio' value='2'/> "+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49")+"&nbsp;</td></tr></table>"
	+ "<div id='sysuserDiv"+divcount+"' title='"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_50")+"' style='display: none;'>"
	+ "	<iframe id='flowuserFrame"+divcount+"' name='flowuserFrame"+divcount+"' src='' style='width:320px;height:400px;border:0;' marginwidth='0'  frameborder='no'></iframe>"
	+ "<table><tr><td style='width:300px;' align='center'><input type='button' id='userok"+divcount+"' value='"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_52")+"' class='btnClass5 mr23' onclick='javascript:dook("+divcount+",1)' />"
	+ "<input type='button'  value='"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_53")+"' class='btnClass6' onclick='javascript:closeuserdialog("+divcount+")' />"
	+"</td></tr></table></div><input type='hidden' value='' id='addsysuserstr"+divcount+"' name='addsysuserstr"+divcount+"'/>"
	+ "<div id='sysdepDiv"+divcount+"' title='"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_51")+"' style='display: none;'>"
	+ "	<iframe id='flowdepFrame"+divcount+"' name='flowdepFrame"+divcount+"' src='' style='width:320px;height:400px;border:0;' marginwidth='0'  frameborder='no'></iframe>"
	+ "<table><tr><td style='width:300px;' align='center'><input type='button'  id='depok"+divcount+"' value='"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_52")+"' class='btnClass5 mr23' onclick='javascript:dook("+divcount+",2)' />"
	+ "<input type='button'  value='"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_53")+"' class='btnClass6' onclick='javascript:closedepdialog("+divcount+")' />"
	+"</td></tr></table></div><input type='hidden' value='' id='addsysdepstr"+divcount+"' name='addsysdepstr"+divcount+"'/>"
	+"</div>";
	$("#adddiv").before(addDivStr);
	updateDivName();
}
// 修改审核级别信息
function updateDivName(){
	var num = 1;
	$(".nameclass").each(function(){
		$(this).empty();
		$(this).html("&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_27")+num+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_68"));
		$(this).attr("audlev",num);
		num = num + 1;
	});
}
// 填加审核对象信息
function addobj(count){
	// 获取指定DIV 中 下拉框的值
	var selIndex = $("#type"+count).val();
	// 如果是逐级审核 ，直接跳出
	if(selIndex == 2){
		return;
	}
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var ipath = $("#ipath").val();
	var flowid = $("#flowid").val();
	var pathtype = $("#pathtype").val();
	var label_idstr = "";
	var opration = "add";
	if(selIndex == 1 || selIndex == 3){
		$("#flowobj"+count+" label[attr='1']").each(function(){
			// 获取所选择的ID，操作员u_id或者机构id
			var id = $(this).attr("id");
			// 需要把前面的u去掉
			if(id != ""){
				if(selIndex == 1){
					id = id.substring(1);
				}
				label_idstr=label_idstr+id+"_";
			}
		});
	}
	if(selIndex == 1){
		$("#sysuserDiv"+count).dialog({
			autoOpen: false,
			height:480,
			width: 320,
			resizable:false,
			modal: true,
			close:function(){
				$("#userok"+count).attr("disabled","");
			}
		});
		$("#flowuserFrame"+count).empty();
		var url = ipath+"/aud_sysusertree.jsp?divcount="+count+"&lguserid="+lguserid+"&pathtype="+opration+"&labelidstr="+label_idstr+"&time="+ new Date().getTime();
		$("#flowuserFrame"+count).attr("src",url);
		$('#sysuserDiv'+count).dialog('open');
	}else if(selIndex == 2){
		
	}else if(selIndex == 3){
		$("#sysdepDiv"+count).dialog({
			autoOpen: false,
			height:480,
			width: 320,
			resizable:false,
			modal: true,
			close:function(){
				$("#depok"+count).attr("disabled","");
			}
		});
		$("#flowdepFrame"+count).empty();
		// div 序列号， 操作员userid ，操作路径 1默认 2是上一步，3是填加 ，所选择的label中的ID
		var url = ipath+"/aud_sysdeptree.jsp?divcount="+count+"&lguserid="+lguserid+"&pathtype="+opration+"&labelidstr="+label_idstr+"&time=" + new Date().getTime();
		$("#flowdepFrame"+count).attr("src",url);
		$('#sysdepDiv'+count).dialog('open');
	}

}
// 清空选择的对象  转换select
function changeObj(count){
	var selIndex = $("#type"+count).val();
	 $("#add"+count).css("visibility","visible");
	if(selIndex == 2){
		// 如果只有一个审核等级
		var level =   $("#divlevel").val();
		// if(level != "1"){
		// if(confirm("选择逐级审批会将增加的审批删除,确定吗？")){
		// var test = "1";
		// $("#auditcount"+count).empty();
		// $("div[divattr='adddiv']").each(function(){
		// if(test == "1"){
		// test = "2";
		// return;
		// }else{
		// $(this).remove();
		// }
		// });
		// $("#add"+count).css("visibility","hidden");
		// }else{
		// var a = $("#selindex"+count).val();
		// var msg = returnSelec(a);
		// $("#type"+count).empty();
		// $("#type"+count).html(msg);
		// $("#add"+count).css("visibility","visible");
		// return;
		// }
		// }else{
			 $("#add"+count).css("visibility","hidden");
			 $("#auditcount"+count).empty();
		// }
	}
	$("#addFlowDiv"+count).attr("height","120px;");
	$("#flowobj"+count).parent().attr("height","50px;");

	
	$("#selindex"+count).val(selIndex);
	// 读取所选择的div的审核等级
	var selIndex = $("#type"+count).val();
	if(selIndex == 2){
		$('#flowobj'+count).parent().parent().addClass("hidden");
	}else {
		$('#flowobj'+count).parent().parent().removeClass("hidden");
	}

	// $("#auditcount"+count).css("style:display","none");
	$("#auditcount"+count).empty();
	$("#flowobj"+count).empty();
}

// 直接去读取机构/操作员弹出框的数据
function dook(count,type){
	var returnZTree;
	var msg = "";
	// type 1表示的是操作员弹出框的确定 2是机构的确定
	if(type == 1){
		$("#userok"+count).attr("disabled","disabled");
		returnZTree = window.frames['flowuserFrame'+count].returnZTree();
		msg = getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_54");
	}else if(type == 2){
		$("#depok"+count).attr("disabled","disabled");
		returnZTree = window.frames['flowdepFrame'+count].returnZTree();
		msg = getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_55");
	}
	var obj = "";
	var namestr = "";
	// 获取页面label中的操作员或者机构的id字符串
	$("#flowobj"+count+" label[attr='1']").each(function(){
		// 获取所选择的ID，操作员u_id或者机构id
		var id = $(this).attr("id");
		if(id != ""){
			if(type == 1){
				id = id.substring(1);
			}
			obj=obj+id+",";
		}
	});
	// 选中的操作员/机构
	var zTreeusedNodes = returnZTree.getCheckedNodes(true);
	// 未选中的操作员/机构
	var zTreeunUsedNodes = returnZTree.getCheckedNodes(false);
	// 默认是未选择 1 有选择记录则是 2
	var isFlag = "1";
	// 页面上取到的结点
	var addNodes = "";
	// 新增加的结点
	var newaddNodes = "";
	// 新增加的结点名称
	var newaddNames = "";
	// 要移除的结点
	var pushNodes = "";
	if(obj != null && obj.length>0){
		isFlag = "2";
		// 根据页面上获取的label 组成一个字符窗 进行 新增结点的判断
		var allobjNodes = ","+obj;
		if(zTreeusedNodes != null && zTreeusedNodes.length>0){
			for(var i=0; i<zTreeusedNodes.length; i++){
				var selectid = zTreeusedNodes[i].id;
				if(type == 1){
					selectid = selectid.substring(1);
				}
				selectid = ","+ selectid + ",";
				// 说明该结点存在 ,不处理
				if(allobjNodes.indexOf(selectid) >= 0){
				}else{
					// 新增的结点ID
					newaddNodes = newaddNodes + zTreeusedNodes[i].id + ",";
					// 新增的结点名称
					newaddNames = newaddNames + zTreeusedNodes[i].name + "#";
				}
			}
		}
		addNodes = ","+obj;
		// 未选中的结点
		var unselectNodes = "";
		// 获取弹出框中未被选中的结点，不包括未展开过的结点
		if(zTreeunUsedNodes != null && zTreeunUsedNodes.length>0){
			for(var j=0; j<zTreeunUsedNodes.length;j++){
				var unselectid = zTreeunUsedNodes[j].id;
				if(type == 1){
					unselectid = unselectid.substring(1);
				}
				unselectNodes = unselectNodes + unselectid  + ",";
			}
		}
		// 这里处理 那些页面已经存在但在弹出框中被取消选中的结点
		if(unselectNodes.length >0){
			unselectNodes = "," + unselectNodes;
			var objExistNodes = obj.split(",");
			if(objExistNodes != null && objExistNodes.length>0){
				for(var m=0; m<objExistNodes.length;m++){
					var existSelectNode = objExistNodes[m];
					if(existSelectNode != null && existSelectNode.length>0){
						existSelectNode = ","+ objExistNodes[m] + ","
						if(unselectNodes.indexOf(existSelectNode) >= 0){
							pushNodes = pushNodes + objExistNodes[m] + ",";
							if(addNodes.indexOf(existSelectNode) >= 0){
								addNodes = addNodes.replace(existSelectNode,",");
							}
						}
					}
				}
			}
		}
		obj = "";
		// 这里的obj是新增的结点 + 除去被取消后的结点的结点
		obj = newaddNodes + addNodes.substring(1,addNodes.length);
	}else{
		// 页面未选中一个结点
		if(zTreeusedNodes != null && zTreeusedNodes.length>0){
			for(var n=0; n<zTreeusedNodes.length; n++){
				obj = obj + zTreeusedNodes[n].id + ",";
			}
		}
	}
	// 判断页面选择的个数
	var countnum = 0;
	if(obj != null && obj.length>0){
		// 未选择记录
		if(isFlag == "1"){
			countnum = zTreeusedNodes.length;
		}else if(isFlag == "2"){
			var arr = obj.split(",");
			for(var a=0;a<arr.length;a++){
				if(arr[a] != null && arr[a] != ""){
					countnum = countnum + 1 ;
				}
			}
		}
	}
	if(countnum > 10){
		alert(msg);
		if(type == 1){
			$("#userok"+count).attr("disabled","");
		}else if(type == 2){
			$("#depok"+count).attr("disabled","");
		}
		return;
	}else{
		if(countnum > 7){
			$("#addFlowDiv"+count).attr("height","145px;");
			$("#addFlowDiv"+count).attr("border-bottom","10px;");
			$("#flowobj"+count).parent().attr("height","75px;");
		}else if(count > 5){
			$("#addFlowDiv"+count).attr("height","135px;");
			$("#addFlowDiv"+count).attr("border-bottom","10px;");
			$("#flowobj"+count).parent().attr("height","65px;");
		}
	}
	if(obj != null && obj.length>0){
			// 移除 取消的结点
			if(pushNodes != null && pushNodes.length > 0){
				var pusharr = pushNodes.split(",");
				for(var t=0;t<pusharr.length;t++){
					var pushid = pusharr[t];
					if(pushid != null && pushid.length > 0){
						$("#flowobj"+count+" label[attr='1']").each(function(){
							// 获取所选择的ID，操作员u_id或者机构id
							var id = $(this).attr("id");
							if(type == 1){
								id = id.substring(1);
							}
							if(pushid == id){
								$(this).remove();
							}
						});
					}
				}
			}
			// 处理新增加的结点
			if(newaddNodes != null && newaddNodes.length>0){
				var newaddNodesarr = newaddNodes.split(",");
				var newaddNamesarr = newaddNames.split("#");
				var str = "";
				for(var i=0; i<newaddNodesarr.length; i++){	
					var id = newaddNodesarr[i] ; 
					if(id != null && id.length >0){
						str = str + "<label id='"+ id
		    			+"' attr='1' class='div_hover_bg div_bd'  style='padding: 2px;margin-right: 3px;'  onclick='removelabel(this,"+count+")'>"
		    			+newaddNamesarr[i]+"<font size='+1'>×</font></label>\n";
					}
	    		}	
				$("#flowobj"+count).append(str);
			}
			// 当页面未选审批人
			if(isFlag == "1"){
				if(zTreeusedNodes != null && zTreeusedNodes.length>0){
					$("#flowobj"+count).empty();
					var str1 = "";
					for(var i=0; i<zTreeusedNodes.length; i++){		
		    			str1 = str1 + "<label id='"+zTreeusedNodes[i].id
		    			+"' attr='1' class='div_hover_bg div_bd'  style='padding: 2px;margin-right: 3px;'  onclick='removelabel(this,"+count+")'>"
		    			+zTreeusedNodes[i].name+"<font size='+1'>×</font></label>\n";
		    		}	
					$("#flowobj"+count).html(str1);
				}
			}
	}
	// 选择操作员统计人数
	var auditCount = 0;
	if(type == 1 && countnum > 0){
		$("#auditcount"+count).css("style:display","block");
		$("#auditcount"+count).empty();
		$("#auditcount"+count).html(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_56")+countnum+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_57"));
	}else if(type == 2){
		$("#auditcount"+count).css("style:display","none");
	}else if(countnum == 0){
		$("#auditcount"+count).css("style:display","none");
		$("#auditcount"+count).empty();
		$("#flowobj"+count).empty();
	}
	if(type == 1){
		closeuserdialog(count);
	}else if(type == 2){
		closedepdialog(count);
	}
}



//关闭选择操作员
function closeuserdialog(count){
	$("#userok"+count).attr("disabled","");
	$("#sysuserDiv"+count).dialog("close");
}
// 关闭 选择机构
function closedepdialog(count){
	$("#depok"+count).attr("disabled","");
	$("#sysdepDiv"+count).dialog("close");
}

// 下一步 1/保存
function nextsaveAudit(operation){
	operationBtn(1);
	var flowtask = $("#flowtask").val();
    //首尾空格处理
    if(/(^ +| +$)/.test(flowtask)){
        flowtask = $.trim(flowtask);
        $("#flowtask").val(flowtask);
    }
	if(flowtask == null || "" == flowtask){
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_58"));
		operationBtn(2);
		return;
	}
	
	var switchCount = $("#switchCount").val();
	if(switchCount == "0"){
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_59"));
		operationBtn(2);
		return;
	}
	var items = "";
	$('input[name="checklist"]:checked').each(function(){	
		items += $(this).val()+",";
	});
	if(items == "" || items.length == 0){
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_60"));
		operationBtn(2);
		return;
	}
	var audstr = "";
	// 判断是否有没值的跳出
	var isFlag = "1";
	$("div[divattr='adddiv']").each(function(){
		if(isFlag == "2"){
			return;
		}
		// 标识的是该审核div的隐藏的序列号
		var sernum = $(this).attr("attrnum");
		// 所选择的索引 1操作员2逐级 3机构
		var selIndex = $("#type"+sernum).val();
		var obj = "";
		if(selIndex == 1 || selIndex == 3){ 
			// 页面显示的第几个 审核
			var divser = $("#addFlowDiv"+sernum).find("label[class='nameclass']").attr("audlev");
			$("#flowobj"+sernum+" label[attr='1']").each(function(){
				// 获取所选择的ID，操作员u_id或者机构id
				var id = $(this).attr("id");
				// 需要把前面的u去掉
				if(id != ""){
					if(selIndex == 1){
						id = id.substring(1);
					}
					obj=obj+id+",";
				}
			});
			if(obj == ""){
				if(selIndex == 1){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_27")+divser+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_61"));
				}else if(selIndex == 3){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_27")+divser+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_62"));
				}
				isFlag = "2";
				return;
			}
			
		}else{		// 这里处理的是逐级审核
			obj = "zhuji";
		}
		var isFlow = $("input[name='isCheckFlow"+sernum+"']:checked").val();
		audstr = audstr + selIndex + "_" +obj +"_"+isFlow + "#";
	});
	if(isFlag == "2"){
		operationBtn(2);
		return;
	}
	var comment = $("#comment").val();
	var lguserid = $("#lguserid").val();
	// 审核等级数
	var divlevel = $("#divlevel").val();
	var pathUrl = $("#pathUrl").val();
	var flowid = $("#flowid").val();
	var pathtype = $("#pathtype").val();
	EmpExecutionContext.log(pathUrl+"/aud_auditpro.htm",{
		method:"addauditpro",
		flowtask:flowtask,
		audstr:audstr,
		items:items,
		comment:comment,
		lguserid:lguserid,
		divlevel:divlevel,
		pathtype:pathtype,
		flowid:flowid
		});
	$.post(pathUrl+"/aud_auditpro.htm",{
		method:"addauditpro",
		flowtask:flowtask,
		audstr:audstr,
		items:items,
		comment:comment,
		lguserid:lguserid,
		divlevel:divlevel,
		pathtype:pathtype,
		flowid:flowid
		},function(returnmsg){
   			if(returnmsg == ""){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
				operationBtn(2);
				return;
	   		}else if(returnmsg.indexOf("html") > 0){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
				operationBtn(2);
    		    return;
	    	}else if(returnmsg == "haverecord"){
	    		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_8"));
	    		window.location.href = pathUrl+"/aud_auditpro.htm?method=toAddAuditPro&pathtype=2&lguserid="+lguserid+"&flowid="+flowid;
    		    return;
		    }else if(returnmsg == "nobindtype"){
	    		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_63"));
	    		operationBtn(2);
    		    return;
		    }else if(returnmsg == "userexist"){
	    		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_64"));
	    		operationBtn(2);
    		    return;
		    }else if(returnmsg == "depexist"){
	    		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_65"));
	    		operationBtn(2);
    		    return;
		    }else{
				var str = returnmsg.split("#");
				if(str[0] == "success"){
					if(operation == 1){// 下一步
						// 审核流程ID
						var flowid = str[1];
						window.location.href = pathUrl+"/aud_auditpro.htm?method=toInstallObj&pathtype="+pathtype+"&lguserid="+lguserid+"&flowid="+flowid;
					}else if(operation == 2){// 保存
						if(pathtype != null && pathtype == "2"){
							alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_66"));
						}else{
							alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_67"));
						}
						operationBtn(2);
						if(pathtype != null && pathtype == "2")
						{
							window.location.href = pathUrl+"/aud_auditpro.htm?method=find&lguserid="+lguserid+"&isOperateReturn=true";
						}
						else
						{
							window.location.href = pathUrl+"/aud_auditpro.htm?method=find&lguserid="+lguserid;
						}
						return;
					}
				}else if(str[0] == "fail"){
					if(pathtype != null && pathtype == "2"){
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_66"));
					}else{
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_67"));
					}
					operationBtn(2);
					return;
				}else{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
					operationBtn(2);
					return;
				}
		    }
	  });
}
//处理按钮
function operationBtn(type){
	if(type == 1){
		$("#nextbtn").attr("disabled","disabled");
		$("#savebtn").attr("disabled","disabled");
		$("#cancelbtn").attr("disabled","disabled");
	}else if(type == 2){
		$("#nextbtn").attr("disabled","");
		$("#savebtn").attr("disabled","");
		$("#cancelbtn").attr("disabled","");
	}
}

// 移出
function removelabel(t,count){
	$(t).remove();
	var selIndex = $("#type"+count).val();
	if(selIndex == 1){
		var num = 0;
		$("#flowobj"+count+" label[attr='1']").each(function(){
			num = parseInt(num + 1);
		});
		$("#auditcount"+count).empty();
		if(num >0){
			$("#auditcount"+count).css("style:display","blank");
			$("#auditcount"+count).html(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_56")+num+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_57"));
		}else{
			$("#auditcount"+count).css("style:display","none");
		}
	}
}

// 点取消
function cancelAudit(){
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	window.location.href = pathUrl+"/aud_auditpro.htm?method=find&lguserid="+lguserid+"&isOperateReturn=true";
}

































// 选择机构
function dosysdepok(count){
		
		var sysdepstr = $("#addsysdepstr"+count).val();
if(sysdepstr != ""){
	var strarr = sysdepstr.split("#");
	var str = "";
	for(var i=0;i<strarr.length;i++){
		if(strarr[i] == "" || strarr[i].length == 0){
			continue;
		}
		// 1是 id 0是名称
		var name_id = strarr[i].split("_");
		str = str + "<label id='"+name_id[1]+"'  class='div_bg'>"+name_id[0]+"</label>&nbsp;";
	}
	 $("#flowobj"+count).html(str);
}
$('#sysdepDiv'+count).dialog('close');
}
// 选择操作员
function dosysuserok(count){
	var sysuserstr = $("#addsysuserstr"+count).val();
if(sysuserstr != ""){
var strarr = sysuserstr.split("#");
var str = "";
for(var i=0;i<strarr.length;i++){
	if(strarr[i] == "" || strarr[i].length == 0){
		continue;
	}
	// 1是 u_id 0是名称
	var name_id = strarr[i].split("_");
	str = str + "<label id='"+name_id[1]+"' class='div_bg'>"+name_id[0]+"</label>&nbsp;";
}
 $("#flowobj"+count).html(str);
}
$('#sysuserDiv'+count).dialog('close');
}


$(document).ready(function(){
    //说明 鼠标移入
    $('.des').live('mouseover',function(event){
        var pos = $(this).position();
        $('#des-info').css({'top':pos.top-40,'left':pos.left+40}).show();
    }).live('mouseout',function(){//鼠标移出
        $('#des-info').hide();
    });
    $('#des-info').hover(
        function(){
            $(this).show();
        },
        function(){
            $(this).hide();
        }
    )
})










