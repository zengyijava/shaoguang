$(document).ready(function(){
	getLoginInfo("#loginUser");
	setLeftHeight();

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
	
	$("#addDiv").dialog({
		autoOpen: false,
		height:270,
		width: 370,
		modal: true,
		close:function(){
			doCancel();
		}
	});

	var inheritPath=$('#inheritPath').val();
	noquot("#addDepName");
	noquot("#depCodeThird");
	noquot("#depCode");
	noquot("#depName");
	noyinhao("#depResp");
	noyinhao("#addDepResp");
});

function disabledbtn(){
	$("#subbtn").attr("disabled","disabled");
}
function effectbtn(){
	$("#subbtn").attr("disabled","");
}


function sub(){
	disabledbtn();
	butg("#btnOK","#btnCancel","");
	var dId = $('#dId').val();
	
	var name=$("#addDepName").attr("value");				//新机构名称
	var depCodeThird = $("#depCodeThird").attr("value");	
	var oldDepName = $("#oldDepName").val();				//老机构名称
	//var codeName=$.trim($("#depCodeThird").attr("value"));	//第3方机构编码
	var resp=$("#addDepResp").attr("value");				//机构描述
	var superId=$("#superDepId").attr("value");				//父级机构ID
	var level=$("#level").attr("value");					//级别
	var oldcode = $("#oldCode").val();				
	name = name.replace(/\s+/g,""); 
	var opType = $("#opType").val();//1为添加，2为树节点上修改，其他为最右侧的修改链接
	var pathUrl = $("#pathUrl").val();
	var type = "1";
	if(opType != "1"){
		type = "2";
	}
	if($.trim(name)=="" || $.trim(name)==null ){// 非空判断
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_31"));
		$("#depName").focus();
        butk("#btnOK","#btnCancel","");
        effectbtn();
		return;
	}else if(hasSpecialChar(name)){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_32"));
		$("#addDepName").focus();
		effectbtn();
		return;
	}else if(name.length>20){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_33"));
		$("#addDepName").focus();
		butk("#btnOK","#btnCancel","");
		effectbtn();
		return;
	}
	else if(yinhao(getJsLocaleMessage("user","user_xtgl_czygl_text_34"),name))
	{
		butk("#btnOK","#btnCancel","");
		effectbtn();
		return;
	}
	else if(xiegang(getJsLocaleMessage("user","user_xtgl_czygl_text_34"),name))
	{
		butk("#btnOK","#btnCancel","");
		effectbtn();
		return;
	}else{
		if(type == "1"){		//新增  验证同等机构下机构名称重复的
			var node = zTree.getSelectedNode();
			if(node != null){
                var deleteZtree2Node = zTree.getNodesByParam("pId",node.id ,node);
			    if(deleteZtree2Node!=null){
				    for(var i=0;i<deleteZtree2Node.length;i++){
					    if(deleteZtree2Node[i].name == name){
						    alert(getJsLocaleMessage("user","user_xtgl_czygl_text_35"));
						    effectbtn();
							return;
						}
					}
			 	}
            }
			if($.trim(depCodeThird)=="" || $.trim(depCodeThird)==null){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_36"));
				$("#depCodeThird").focus();
				effectbtn();
				return;
			}
		}
   }
	if(resp.length>250){
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_37"));
			butk("#btnOK","#btnCancel","");
			effectbtn();
			return;
	}
	$.ajax({
		url:pathUrl+"/opt_department.htm",
		type:"post",
		data:{
		    id:dId,
		    depName:name,
		    depCodeThird:depCodeThird,
			depResp:resp,
			superiorId:superId,
			method:"add",
			depLevel:level,
			oldcode:oldcode,
			oldDepName:oldDepName,
			type:type,
			lgcorpcode:GlobalVars.lgcorpcode
		},
		success:function(data){
			if(data=="codethirdRepeat")
			{
               alert(getJsLocaleMessage("user","user_xtgl_czygl_text_38"));
               effectbtn();
               $("#depCodeThird").focus();
			}else if(data == "error"){
			 	alert(getJsLocaleMessage("user","user_xtgl_czygl_text_39"));
			 	effectbtn();
			 }else if(data == "maxLevel"){
				 var depmaxlevel = $("#depmaxlevel").val();
				 effectbtn();
			 	alert(getJsLocaleMessage("user","user_xtgl_czygl_text_40")+depmaxlevel +getJsLocaleMessage("user","user_xtgl_czygl_text_41"));
			 }else if(data == "maxChild"){
				 var depmaxchild = $("#depmaxchild").val();
                 alert(getJsLocaleMessage("user","user_xtgl_czygl_text_42")+depmaxchild+getJsLocaleMessage("user","user_xtgl_czygl_text_43"));
                 effectbtn();
             }else if(data == "maxDep"){
            	 var depmaxdep = $("#depmaxdep").val();
            	 alert(getJsLocaleMessage("user","user_xtgl_czygl_text_44")+depmaxdep+getJsLocaleMessage("user","user_xtgl_czygl_text_43"));
            	 effectbtn();
             }else if(data != null && data=="1"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_25"));
				butk("#btnOK","#btnCancel","");
				effectbtn();
				var oType = $("#oType").val();
				$('#addDiv').dialog('close');
				if(superId == 0){
					if($('#corptype').val()=='0'){
						window.parent.parent.frames["topFrame"].document.getElementById('companyName').innerHTML = name;
					}
				}
				if(opType == 1 ){//添加时只刷新局部树
					var node = zTree.getSelectedNode(); 
					zTree.setting.asyncUrl =  pathUrl+"/opt_department.htm?method=createTreejm&depId="+node.id+"&lguserid="+GlobalVars.lguserid;
       	 		 	zTree.reAsyncChildNodes(zTree.getSelectedNode(), "refresh");
       	 		 	submitForm();
				}else if(opType==2){
					var curTreenodes=zTree.getSelectedNode();
      				curTreenodes.name = name;
      				curTreenodes.depResp = resp;
      				zTree.updateNode(curTreenodes, true);
      				submitForm();
				}else{
					location.href = location;// 刷新页面;
				}
				return;
			}else if(data != null && data=="2"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_45"));
				effectbtn();
				return;
			}else if(data != null && data=="3"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_35"));
				effectbtn();
				return;
			}else if(data != null && data=="4"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_46"));
				location.href = location; //刷新页面
			}else if(data != null && data=="5"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_47"));
				effectbtn();
				return;
			}
		}
	});
};

function doCancel()
{
	$("#addDepName").attr("value","");
	$("#depCodeThird").attr("value","");
	$("#addDepResp").val("");
	$("#oldDepName").attr("value","");
	$("#dId").attr("value","");
	$("#depCodeThird").attr("disabled","");
	$("#level").attr("value","");
	$("#superiorId").attr("value","");
	$("#opType").attr("value","");
}

function submitForm(){
	var time = new Date().valueOf();
	$('#tableInfo').load($('#servletUrl').val(),
		{
			method:'getTable',
			time:time,
			depId:$('#depId').val(),
			lguserid:GlobalVars.lguserid,
			lgcorpcode:GlobalVars.lgcorpcode,
			pageIndex:$('#txtPage').val(),
			pageSize:$('#pageSize').val()
		}
	);
}
function delDeps(id,name,pId)
{
	//-----------emp3.0----------------------
	var id=$("#depId").val();
	if(id==""||id==null)
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_54"));
		return;
	}
	if($('#depId').val()!=""){
		var pathUrl = $("#pathUrl").attr("value");
		var depId= $('#depId').attr("value");
		var depName =$("#upDepName").attr("value");
		$.post("opt_department.htm?method=checkMsg",{depId:depId,lgcorpcode:GlobalVars.lgcorpcode},function(data){
			if(data=="true"){
				if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_48"))){// 提示框
					delDepsDetail(id,name,pId);
				}
			} else{
				if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_49"))){// 提示框
					delDepsDetail(id,name,pId);
				}
			}
		});
	}
	else
	{
        alert(getJsLocaleMessage("user","user_xtgl_czygl_text_50"));
	}	
}

function delDepsDetail(id,name,pId)
{
	//-----------emp3.0----------------------
//	var id=$("#depId").val();
//	if(id==""||id==null)
//	{
//		alert("请选择机构！");
//		return;
//	}
//	name=$("#upDepName").attr("value",name);
//	pId=$("#pareDepId").attr("value",pId);
	//---------------------------------------

		var pathUrl = $("#pathUrl").attr("value");
		var depId= $('#depId').attr("value");
		var depName =$("#upDepName").attr("value");
		var pareDepId = $('#pareDepId').attr("value");
		//首先判断下有没有余额
			$.post("opt_department.htm?method=delete",{depId:depId,depName:depName,pareDepId:pareDepId,lgcorpcode:GlobalVars.lgcorpcode},function(data){
					if(data == "0"){
						alert(getJsLocaleMessage("user","user_xtgl_czygl_text_51"));
						return;
					}else if(data == "1"){
						 getCt();
						 alert(getJsLocaleMessage("user","user_xtgl_czygl_text_77"));
						 location.href = location; //刷新页面
						 //window.location.href = pathUrl+"/opt_department.htm";
						 var node = zTree.getSelectedNode().parentNode;
						 zTree.setting.asyncUrl =  pathUrl+"/opt_department.htm?method=createTreejm&depId="+node.id+"&lguserid="+GlobalVars.lguserid;
         	 		 	 zTree.reAsyncChildNodes(node, "refresh");
         	 		 	 if(pId != null && pId != ""){
         	 		 	 	$("#depId").val(pId);
         	 		 	 	submitForm();
         	 		 	 }
         	 		 	$("#depId").val("");
					}else if(data=="2")
					{
						alert(getJsLocaleMessage("user","user_xtgl_czygl_text_52"));
						return;
					}else if(data == "3")
					{
						alert(getJsLocaleMessage("user","user_xtgl_czygl_text_46"));
						location.href = location; //刷新页面
					}else if(data == "errer")
					{
						alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
						return;
					}
				});
}
function showDepTable()
{
	var departmentUrl = $("#departmentUrl").val();
	$('#tableInfo').load(departmentUrl,
	{method:'getTable',lguserid:GlobalVars.lguserid,lgcorpcode:GlobalVars.lgcorpcode});
}
function editDep(id)
{
	//-------------emp3.0-----------------
	if(id!=null&&id!="")
	{
	}
	else
	{
		//机构id
		id=$("#depId").val();
	}					
	//-------------emp3.0---------------------
	
	if(id == null || id ==""){
	//修改时偶尔会出现id为空
		return ;
	}
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/opt_department.htm?method=getDep",{depId:id},function(result){
		  if(result == "error"){
		  	  alert(getJsLocaleMessage("user","user_xtgl_czygl_text_140"));
		  }else if(result == ""){
		  	  alert(getJsLocaleMessage("user","user_xtgl_czygl_text_53"));
		  }else{
          	  var data = eval("("+result+")");
        	  $("#addDepName").attr("value",data.depName);		//新机构名称
			  $("#oldDepName").attr("value",data.depName);		//老机构名称
			  $("#depCodeThird").attr("value",data.depCodeThird);	//第三方机构
			  $('#dId').val(id);						//机构ID
			  $("#depCodeThird").attr("disabled","disabled");
			  $("#oldCode").val(data.depCodeThird);					//不解	
			  $("#superDepId").attr("value",data.superiorId);		//父级机构ID
			  $("#addDepResp").val(data.depResp);				//描述
			  $("#addDiv").dialog("open");
          }
	});
}

//添加机构
function addDepFun()
{
	var idStr=$("#depId").val();
	if(idStr==""||idStr==null)
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_54"));
		return;
	}
	//值随便传，没有用传入的值
	doAdd(1,1,1);
}

//修改机构
function updateDepFun()
{
	var idStr=$("#depId").val();
	if(idStr==""||idStr==null)
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_54"));
		return;
	}
	
	$("#opType").val("2");
	//值随便传，没有用传入的值
	editDep(null);
}
