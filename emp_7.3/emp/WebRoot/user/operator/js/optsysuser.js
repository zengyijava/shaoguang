function InitPass(guId,userName,name, keyId){
			if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_55"))==true){
		$.post("opt_sysuser.htm?method=InitPassWord",{guId:guId,userName:userName,name:name,lgcorpcode:GlobalVars.lgcorpcode,keyId:keyId},function(data){
			if(data == "true"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_25"));
				$("#pageForm").attr("action","opt_sysuser.htm?method=find&pageIndex="+pageIndex+"&pageSize="+pageSize+"&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode);
				submitForm();
			}else if(data = "false"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
				return;
			}else if(data = "error"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
				return;
			}else{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_56"));
				return;
			}
		});
	}
}

function updateRoleNo(t)
{
	var str=$(t).children("label").text();
	str=str.replaceAll("<","&lt;").replaceAll(">","&gt;")
	if(str.indexOf("MWHS]#")!=-1)
	{
		var strArray=str.split("MWHS]#");
		str="";
		for(var i=0;i<strArray.length;i++)
		{
			str+=strArray[i]+"<br>";
		}
	}
	$("#msgRole").html();
	$("#msgRole").empty();
	$("#msgRole").append(str);
	$("#rolesDialog2").dialog("open");
}

function updateRole(ids,userId,name,keyId)
{
	$("#roleIds").val(ids);
	$("#userNameRole").val(name);
	ids=","+ids;
	$("#userIdRole").val(userId);
	$("#keyId").val(keyId);
	$("#rolesDialog").find("input[type=checkbox]").each(function(){
			var value=","+$(this).val()+",";
			if(ids.indexOf(value)!=-1)
			{
				$(this).attr("checked","true");
			}
	});
	$("#rolesDialog").dialog("open");
}

function updateRo()
{
	var userId=$("#userIdRole").val();
	var keyId=$("#keyId").val();
	var userName = $("#userNameRole").val();
	var corpCode = GlobalVars.lgcorpcode;
	var rolesIds=$("#roleIds").val();;
	var ids="";
	$("#rolesDialog").find("input[type=checkbox]:checked").each(function(){
		ids+=$(this).val()+",";
	});

	if(ids==null||ids=="")
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_57"));
		return;
	}
	if(rolesIds==ids)
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_58"));
		$("#rolesDialog").dialog("close");
		return;
	}

	$("#rolesDialog").find("input[type=button]").attr("disabled","disabled");
	$.post('opt_sysuser.htm',{method:'updateRole',rolesId:ids,userId:userId,userName:userName,lgcorpcode:corpCode,keyId:keyId},
		function(result)
		{
		
			$("#pageForm").attr("action","opt_sysuser.htm?method=find&pageIndex="+pageIndex+"&pageSize="+pageSize+"&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode);
			if(result-0==-2)
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_59"));
				$("#rolesDialog").find("input[type=button]").attr("disabled","");
				$("#userIdRole").dialog("close");
			}else if(result-0>-1)
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_60"));
				$("#rolesDialog").find("input[type=button]").attr("disabled","");
				submitForm();
			}else //if(result == "error")
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
				$("#rolesDialog").find("input[type=button]").attr("disabled","");
				$("#userIdRole").dialog("close");
			}
		}
	);
}

function allRoleName(t)
{
	//$("select[name='sendType']").hide();
	$("#msg").empty();
	$("#msg").append($(t).children("label").text().replaceAll("<","&lt;").replaceAll(">","&gt;"));
	$("#roleNamesdialogtemp").dialog("option","title",getJsLocaleMessage("user","user_xtgl_czygl_text_61"));
	$("#roleNamesdialogtemp").dialog("open");
}	


//注销操作员
function deleteUser(userId, keyId)
{
	//if(confirm("注销该用户后，该用户涉及的所有业务将被撤销，是否继续？")){
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/opt_sysuser.htm",{method:"delete",userId:userId,keyId:keyId},
		function(result)
		{
			if(result=="false")
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_62"));
				return;
			}
			if(result=="true")
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_63"));
				submitForm();
				return;
			}
			if(result=="flowExist")
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_64"));
				return;
			}
		});
	//}
}

//重新启用已注销的用户
function useUser(userId, keyId)
{
     location.href="opt_sysuser.htm?method=toEdit&userId="+userId+"&lguserid="+GlobalVars.lguserid+
     "&lgcorpcode="+GlobalVars.lgcorpcode+"&optype=use"+"&keyId="+keyId;
}
function toAdd(){
	location.href="opt_sysuser.htm?method=toAdd&lgguid="+GlobalVars.lgguid;
}

function toImport(){
	location.href="opt_sysuser.htm?method=toImport&lgguid="+GlobalVars.lgguid;
}

function toEdit(userId, keyId){
	location.href="opt_sysuser.htm?method=toEdit&userId="+userId+"&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode+
	"&keyId="+keyId+"&optype=update";
}


function rtime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#submitSartTime").attr("value");
	if(v.length != 0)
	{
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		var day = 31;
		if (mon != "12")
		{
		    mon = String(parseInt(mon,10)+1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		    switch(mon){
		    case "01":day = 31;break;
		    case "02":day = 28;break;
		    case "03":day = 31;break;
		    case "04":day = 30;break;
		    case "05":day = 31;break;
		    case "06":day = 30;break;
		    case "07":day = 31;break;
		    case "08":day = 31;break;
		    case "09":day = 30;break;
		    case "10":day = 31;break;
		    case "11":day = 30;break;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)+1));
		    mon = "01";
		}
		max = year+"-"+mon+"-"+day+" 23:59:59"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

};

function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#submitEndTime").attr("value");
    var min = "1900-01-01 00:00:00";
	if(v.length != 0)
	{
	    max = v;
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		if (mon != "01")
		{
		    mon = String(parseInt(mon,10)-1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)-1));
		    mon = "12";
		}
		min = year+"-"+mon+"-01 00:00:00"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

};
function detail(t,i){
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	if(i==1){
		$('#detail').dialog('option','title',getJsLocaleMessage("user","user_xtgl_czygl_text_65"));
	}else if(i == 2){
		$('#detail').dialog('option','title',getJsLocaleMessage("user","user_xtgl_czygl_text_66"));
	}else if(i == 3){
		//角色
		$('#detail').dialog('option','title',getJsLocaleMessage("user","user_xtgl_czygl_text_138"));
	}
	
	$('#detail').dialog('open');
}



$(function(){
  $("#dropMenu").after($("<iframe id=\"hidFr\" style=\"display:none\"><\/iframe>"));
  $('#roleName,#subno,#isEmployee,#userState').isSearchSelect({'width':'182','isInput':false,'zindex':30});
});

function showMenu() {
	//$("select[name='subno']").css("visibility","hidden");
	var sortSel = $("#sortSel");
	var sortOffset = $("#sortSel").offset();
	
	$("#dropMenu,#hidFr").toggle();
	//$("select[name='subno']").css("visibility","visible");
}
function hideMenu() {
	$("#dropMenu,#hidFr").hide();
//	$("select[name='subno']").css("visibility","visible");
}

function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode) {
		var sortObj = $("#sortSel");
		sortObj.attr("value", treeNode.name);
		$("#depName").attr("value",treeNode.id);
		hideMenu();
	}
}

function reloadTree() {
	hideMenu();
	zTree1 = $("#dropdownMenu").zTree(setting, zNodes);
}	
function cAll()
{
	var sortObj = $("#sortSel");
	sortObj.attr("value", getJsLocaleMessage("user","user_xtgl_czygl_text_67"));
	$("#depName").attr("value","");
	hideMenu();
}
$(document).ready(function(){
   if(findresult=="-1")
    {
       alert(getJsLocaleMessage("user","user_xtgl_czygl_text_68"));	
       return;			       
    }
	getLoginInfo("#loginUser");
	closeTreeFun(["dropMenu","hidFr"]);
	var pathUrl = $("#pathUrl").val();
	setting = {
			async : true,
			asyncUrl :pathUrl+"/opt_department.htm?method=createTree2&lguserid="+GlobalVars.lguserid, //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			asyncParam: ["depId"],
			callback: {
				click: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){
					   var rootNode = zTree1.getNodeByParam("level", 0);
					   zTree1.expandNode(rootNode, true, false);
					}
				}
			}
	};
	$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
			$(this).find('select').next().show().siblings().hide();

		}, function() {
			$(this).removeClass("hoverColor");
			var $select = $(this).find('select');
			$select.next().hide();
			$select.prev().show();

		});
	reloadTree();
	$("#call").hover(function(){$("#call").css("background-color","#c1ebff");},
			function(){$("#call").css("background-color","");});
			
	$("#roleNamesdialogtemp").dialog({
		autoOpen: false,
		height:200,
		width: 250,
		modal: true,
		open:function(){
			$("select[name='subno']").css("visibility","hidden");
			$("select[name='roleName']").css("visibility","hidden");
		},
		close:function(){
			$("select[name='subno']").css("visibility","visible");
			$("select[name='roleName']").css("visibility","visible");
		}
	});	
		
	$("#rolesDialog").dialog({
		autoOpen: false,
		height:350,
		width: 254,
		modal: true,
		open:function(){
		},
		close:function(){
			$("#userIdRole").val("");
			$("#roleIds").val("");
			$("#userNameRole").val("");
			$("#rolesDialog").find("input[type=checkbox]").attr("checked","");
		}
	});

	$("#rolesDialog2").dialog({
		autoOpen: false,
		height:350,
		width: 254,
		modal: true,
		open:function(){
		},
		close:function(){
		}
	});				
	$("#content select").empSelect({
			width:80
	});
	$('#content select').each(function(){
		    $(this).next().hide();
			$(this).before('<div class="setControl" style="width: 80px;">'+$(this).find('option:selected').text()+'</div>');
	  });
	$('#detail').dialog({
		autoOpen: false,
		width:250,
	    height:200
	});		
	initPage(totalPage,pageIndex,pageSize,totalRec);
	$('#search').click(function(){submitForm();});
	//导出全部数据到excel
	$("#exportCondition").click(function(){
		  if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_69"))){
		      var lgcorpcode =GlobalVars.lgcorpcode;
			  var lguserid =GlobalVars.lguserid;
		      var lgguid =GlobalVars.lgguid;
		      if(sysvoFlag == "haverecord"){
		    	  
					$.ajax({
						type: "POST",
						url: "opt_sysuser.htm?method=exportToExcel",
						data: {
						lgcorpcode:lgcorpcode,lgguid:lgguid,
						lguserid:lguserid
						},
						beforeSend: function(){
							page_loading();
						},
		                complete:function () {
							page_complete();
		                },
						success: function(result){
							if(result=='true'){	
								download_href("opt_sysuser.htm?method=downloadFile&down_session=exportToExcel");
		                    }else{
		                        alert(getJsLocaleMessage("user","user_xtgl_czygl_text_70"));
		                    }
			   			}
					});
		   		   //window.location.href=pathUrl+"/opt_sysuser.htm?method=exportToExcel&lgcorpcode="+lgcorpcode+"&lgguid="+lgguid+"&lguserid="+lguserid;
	  	 	  }else{
	  	 		  alert(getJsLocaleMessage("user","user_xtgl_czygl_text_71"));
	  	 	  }	
		  }
	  });
	
	
});


