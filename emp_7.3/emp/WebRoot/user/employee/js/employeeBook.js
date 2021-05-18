$(document).ready(function() {
			getLoginInfo("#getloginUser");
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
                $(this).removeClass("collapse");
            }, function() {
				$("#condition").show();
                $(this).addClass("collapse");
            });
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
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
    		$("#search").click(function(){submitForm();});
			reloadTree();
			setLeftHeight();
		});

var zTree2;
var zNodes2 =[];
var pathUrl = $("#pathUrl").val();
var setting2 = {
	async: true,
	isSimpleData: true,
	asyncUrl:pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson",  //获取节点数据的URL地址
	rootPID : -1,
	treeNodeKey: "id",
	treeNodeParentKey: "pId",
	callback:{
		beforeAsync: function(treeId, treeNode) {
			zTree2.setting.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
		},
		asyncSuccess:function(event, treeId, treeNode, msg){
			if(!treeNode){
			   var rootNode = zTree2.getNodeByParam("level",0); 
			   zTree2.expandNode(rootNode, true, false);
			}
			
		},
		click: zTreeOnClick2
	}
};
function reloadTree() {
	setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
	zTree2 = $("#dropdownMenu").zTree(setting2, zNodes2);
}
function showDepTree(){
	var selected=document.getElementsByName("delBook");
	var n=0;		//统计勾选中的个数
	var id="";
	for(var i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			id=id+selected[i].value;
			id=id+","
			n=n+1;
		}
	}
	id=id.substring(0,id.lastIndexOf(','));
	if(n<1){alert(getJsLocaleMessage('employee','employee_alert_6'));return;}
	$("#changeDep").dialog({
		autoOpen: false,
		height:490,
		title:getJsLocaleMessage('employee','employee_alert_87'),
		width: 370,
		resizable:false,
		modal: true,
		close:function(){
			 doCancel();
		}
	});
	reloadTree();
	$("#changeDep").dialog("open");
}

function depTreeCancel()
{
	doCancel();
	$("#changeDep").dialog("close");
}

function zTreeOnClick2(event, treeId, treeNode) {
	$("#changeDepId").val(treeNode.id);
}
function doSubmit(){
    var selected=document.getElementsByName("delBook");
    var changeDepId = $("#changeDepId").val();
    if(changeDepId == null || changeDepId == ""){
    	alert(getJsLocaleMessage('employee','employee_alert_88'));
    	return ;
    }
	var id="";
	for(var i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			id=id+selected[i].value;
			id=id+","
		}
	}
	id=id.substring(0,id.lastIndexOf(','));
	$.ajax({
    url: pathUrl+"/epl_employeeBook.htm",
    data:{
        method:'changeDep',
		bookIds:id,
		depId:changeDepId
    },
    type: 'post',
    error: function(){
       alert(getJsLocaleMessage('employee','employee_alert_89'));		
    },
    success: function(result){
		if (result == "true") {
			alert(getJsLocaleMessage('employee','employee_alert_90'));
			submitForm();
		}else {
			alert(getJsLocaleMessage('employee','employee_alert_89'));	
		}
    }
});
doCancel();
$("#changeDep").dialog("close");
}

function doCancel(){
	$("#changeDepId").val("");
	zTree2.cancelSelectedNode();
}
/*使用post方式提交 备份P*/
function doOk()
{
	 var zenze=/^[A-Za-z0-9]+$/;
     var pathUrl = $("#pathUrl").val();
     //名称
     var name = $("#depName").val();
     //部门的id
     var scode = $("#sdepcode").val();
     //自定义编码
     var depcodethird=$("#depcodethird").val();
     
      var lgcorpcode=$("#lgcorpcode").val();
      var lguserid=$("#lguserid").val();
     
     
      name = name.replace(/(^\s*)|(\s*$)|\\/g,"");
     //过滤重复部门名
     var node = zTree.getSelectedNode();  
     var deleteZtree2Node = zTree.getNodesByParam("pId",node.id ,node);
	 if(deleteZtree2Node!=null){
		for(var i=0;i<deleteZtree2Node.length;i++){
			if(deleteZtree2Node[i].name == name){
					alert(getJsLocaleMessage('employee','employee_alert_91'));
					$("#depName").select();
					return;
			}
		}
	}
     if (name == "")
     {
         alert(getJsLocaleMessage('employee','employee_alert_92'));
         $("#depName").focus();
         return;
     }
     
     if(name.indexOf("'")!=-1  || outSpecialChar(name)  ){
     	alert(getJsLocaleMessage('employee','employee_alert_93'));
        $("#depName").select();
     	return;
     }
     
	if(depcodethird=="")
	{
		alert(getJsLocaleMessage('employee','employee_alert_94'));
		$("#depcodethird").focus();
		return;
	}
	if(!zenze.test(depcodethird))
	{
		alert(getJsLocaleMessage('employee','employee_alert_95'));
		$("#depcodethird").select();
		return;
	}
	else
     {
		$("#depcancel").attr("disabled",true);
		$("#depsub").attr("disabled",true);
        $.post(pathUrl+"/epl_employeeBook.htm?method=addDep",{name:name,scode:scode,depcodethird:depcodethird,lgcorpcode:lgcorpcode,lguserid:lguserid},function(r){
              if(r!=null&&r=="true")
              {
            	 alert(getJsLocaleMessage('employee','employee_alert_96'));
            	 zTree.setting.asyncUrl =  pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+node.id;
            	 zTree.reAsyncChildNodes(zTree.getSelectedNode(), "refresh");
              	$("#depcancel").attr("disabled",false);
   			 	$("#depsub").attr("disabled",false);
   			 	doNo();
              }
              else if(r.indexOf("existdepcode")!=-1){
                $("#depcodethird").select();
              	$("#depcancel").attr("disabled",false);
   			 	$("#depsub").attr("disabled",false);
              	alert(getJsLocaleMessage('employee','employee_alert_97'));
              }
              else if(r.indexOf("existdepname")!=-1){
                $("#depName").select();
              	$("#depcancel").attr("disabled",false);
   			 	$("#depsub").attr("disabled",false);
              	alert(getJsLocaleMessage('employee','employee_alert_91'));
              }
              else if(r.indexOf("DepAbove,")!=-1)
              {
                  var rs=r.split(",");
                  $("#depcancel").attr("disabled",false);
     			  $("#depsub").attr("disabled",false);
                  alert(getJsLocaleMessage('employee','employee_alert_98')+rs[1]+getJsLocaleMessage('employee','employee_alert_99'));
              }
              else if(r.indexOf("levAbove,")!=-1)
              {
                  var rs=r.split(",");
                  alert(getJsLocaleMessage('employee','employee_alert_100')+rs[1]+getJsLocaleMessage('employee','employee_alert_101'));
                  $("#depcancel").attr("disabled",false);
     			  $("#depsub").attr("disabled",false);
              }
              else if(r.indexOf("childAbove,")!=-1)
              {
                  var rs=r.split(",");
                  alert(getJsLocaleMessage('employee','employee_alert_102')+rs[1]+getJsLocaleMessage('employee','employee_alert_99'));
                  $("#depcancel").attr("disabled",false);
     			  $("#depsub").attr("disabled",false);
              }
              else if(r.indexOf("notfounddep")!=-1){
            	  alert(getJsLocaleMessage('employee','employee_alert_114'));
                  $("#depcancel").attr("disabled",false);
     			  $("#depsub").attr("disabled",false);
     			  window.location.reload();
                  
              }
              else{
              	  alert(getJsLocaleMessage('employee','employee_alert_103'));
                  $("#depcancel").attr("disabled",false);
       			  $("#depsub").attr("disabled",false);
       			  //location.href=location;
       			var name = $("#depName").val("");
               // var scode = $("#sdepcode").val("");
                var scode = $("#depcodethird").val("");
                $("#addDep").dialog("close");
              }
        })
      }
}





function doNo()
{
	 var name = $("#depName").val("");
     //var scode = $("#sdepcode").val("");
     var scode = $("#depcodethird").val("");
     $("#addDep").dialog("close");
}
function doDel(id,name)
{
	var oldName =  $("#depOldName").val();
	if(oldName==""||oldName==null)
	{
		alert(getJsLocaleMessage('employee','employee_alert_104'));
		return;
	}
	var pathUrl = $("#pathUrl").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var lguserid = $("#lguserid").val();
	
	
	//------换界面后改的地方-------
	id= $("#depId").val();
	name=$("#depName2").val();
	//---------------------------
	if(id==""||id==null)
	{
    
		alert(getJsLocaleMessage('employee','employee_alert_104'));
		return;
	}
	if(id==1)
	{
    	alert(getJsLocaleMessage('employee','employee_alert_108'));
    	return;
	}
	
     if(confirm(getJsLocaleMessage('employee','employee_alert_105')))
     {
    	 
    	 //新增p 添加前台日志---------------------------------------
    		EmpExecutionContext.log(pathUrl+"/epl_employeeBook.htm?method=delDep",{id:id,name:name,lguserid:lguserid,lgcorpcode:lgcorpcode});
    	 //--------------------------------------------------------
    		
    		
    	 $.post(pathUrl+"/epl_employeeBook.htm?method=delDep",{id:id,name:name,lguserid:lguserid,lgcorpcode:lgcorpcode},function(r){
    		 if(r!=null&&r=="1")
             {
             	alert(getJsLocaleMessage('employee','employee_alert_106'));
             	var node = zTree.getSelectedNode().parentNode;  
             	zTree.setting.asyncUrl =  pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+node.id;
            	zTree.reAsyncChildNodes(node, "refresh");
            	$('#'+node.tId).children('button').click();
            	submitForm();
                //location.href=location;
                //---------删除成功清空隐藏域的值--------
            	$("#depId").val("");
    			$("#depName2").val("");
    			$("#updateCode").attr("value","");
	            $("#depOldName").attr("value","");
	            $("#depNewName").attr("value","");
	            $("#depcodethirdUpdate").html("");
                //-------------------------------------
             }
             else if(r!=null && r=="0")
             {
               alert(getJsLocaleMessage('employee','employee_alert_107'));
             }
             else if(r!=null && r=="2")
             {
             	alert(getJsLocaleMessage('employee','employee_alert_108'));
             }
    		 //新增p 对机构树中已删除的机构进行删除操作的处理 如果为机构为null，可能该机构已被其它用户删除
             else if(r!=null && r=="-2")
             {
             	alert(getJsLocaleMessage('employee','employee_alert_114'));
             	//新增p-------------------------------------------------
             	/*刷新机构树*/
             	var node = zTree.getSelectedNode().parentNode;  
             	zTree.setting.asyncUrl =  pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+node.id;
            	zTree.reAsyncChildNodes(node, "refresh");
            	$('#'+node.tId).children('button').click();
            	submitForm();
            	//location.href=location;
                //---------删除成功清空隐藏域的值--------
            	$("#depId").val("");
    			$("#depName2").val("");
    			$("#updateCode").attr("value","");
	            $("#depOldName").attr("value","");
	            $("#depNewName").attr("value","");
	            $("#depcodethirdUpdate").html("");
                //-------------------------------------
	            //------------------------------------------------------------------------------------
             }
			 else if (r!=null && r=="haveBirthdayMembers")
			 {
				 alert("该机构在员工生日祝福中被使用，不能删除！")
			 }
             else
             {
                 alert(getJsLocaleMessage('employee','employee_alert_109'));
             }
       })
     }
}

function toAddEmployee(path){
	var lgcorpcode =  $("#lgcorpcode").val();
	var lgguid =  $("#lgguid").val();
	var lguserid =  $("#lguserid").val();
	//新增：当获取不到值时，调用common.js中的公共方法获取
	if(!lgcorpcode){
		lgcorpcode = GlobalVars.lgcorpcode;
	}
	if(!lgguid){
		lgguid = GlobalVars.lgguid;
	}
	if(!lguserid){
		lguserid = GlobalVars.lguserid;
	}
	//--------------------------------------------------
	window.location.href=path+"/epl_employeeBook.htm?method=toAddEmployeePage&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&lgguid="+lgguid;
}



function rtime(){
    var max = "2099-12-31";
    var min = "1900-01-01";
    var v = $("#submitSartTime").attr("value");
	if(v.length != 0)
	{
		min = v;
	}
	WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:max});

};

function stime(){
   var max = "2099-12-31";
    var v = $("#submitEndTime").attr("value");
   var min = "1900-01-01";
	if(v.length != 0)
	{
		max = v;
	}
	WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:max});

};