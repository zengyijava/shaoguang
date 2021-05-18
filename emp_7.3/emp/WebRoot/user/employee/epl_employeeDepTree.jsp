<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	String treemethod = request.getParameter("treemethod");
	String getType = request.getParameter("getType");
	boolean addcode = new Boolean(request.getParameter("ac"));
	boolean delcode = new Boolean(request.getParameter("dc"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="/common/common.jsp" %>
	<title><emp:message key="employee_dxzs_title_69" defVal="员工通讯录机构树" fileName="employee"/></title>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<%--<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.JSP_IMP_VERSION %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" type="text/css" >--%>
	<%--<link href="<%=skin %>/table.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>
	<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/epl_employeeDepTree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<%--	<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.JSP_IMP_VERSION %>"/>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.JSP_IMP_VERSION%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.JSP_IMP_VERSION %>"/>
	<%}%>--%>
  </head>
  <body style="margin:0;padding:0;" id="epl_employeeDepTree">
  			<input type="text" class="display_none" value="<%=skin %>" id="skin">
  			<%-- 员工机构树列表中点选新增、修改机构时弹出的对话框界面 --%>
  			<%-- 员工机构树列表新增 --%>
  			<div id="addDep">
 			 		<input type="hidden" id="sdepcode" value="" />
				  <table align="center" class="user_table">
				  <tr><td class="user_td_1"></td></tr>
				  <tr><td class='<%="zh_HK".equals(empLangName)?"user_td_2":"user_td_3"%>' align="right"><emp:message key="employee_dxzs_title_56" defVal="机构名称" fileName="employee"/>：</td><td><input type="text" id="depName" maxlength="20" value="" /></td></tr>
				  <tr><td class="user_td_1"></td></tr>
				  <tr><td class='<%="zh_HK".equals(empLangName)?"user_td_2":"user_td_3"%>' align="right"><emp:message key="employee_dxzs_title_57" defVal="机构编码" fileName="employee"/>：</td><td><input type="text" id="depcodethird" maxlength="32" value="" /></td></tr>
				  <tr><td class="user_td_4">&nbsp;</td></tr>
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" id="depsub" onfocus="this.blur();" class="btnClass5 mr23"  onclick="javascript:doOk();"/>
				  <input type="button" value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>"  class="btnClass6" id="depcancel" onclick="javascript:doNo();"/><br/></td></tr>
				  </table>
			 </div>
			 
			 	<%-- 员工机构树列表修改 --%>
			 	<div id="updateDep">
 			 	
				  <table class="user_table">
				  <tr><td class="user_td_1"></td></tr>
				  <tr>	<td class='<%="zh_HK".equals(empLangName)?"user_td_2":"user_td_3"%>' align="right"><emp:message key="employee_dxzs_title_56" defVal="机构名称" fileName="employee"/>：</td>
				  		<td>
				  			<input type="hidden" id="updateCode" value="" />
				  			<input type="hidden" id="depOldName" value="" />
					  		<input type="text" id="depNewName" maxlength="20" value="" />
				  		</td>
				  </tr>
				  <tr><td class="user_td_1"></td></tr>
				  <tr>
				  	<td class='<%="zh_HK".equals(empLangName)?"user_td_2":"user_td_3"%>' align="right"><emp:message key="employee_dxzs_title_57" defVal="机构编码" fileName="employee"/>：</td>
				  	<td>
				  		<label id="depcodethirdUpdate"></label>
				  	</td>
				  </tr>
				  <tr><td class="user_td_4">&nbsp;</td></tr>
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>"  class="btnClass5 mr23" id="updateSubmit"  onclick="javascript:updateDepName();"/>
				  <input type="button" value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>"  class="btnClass6" id="updateCancle"  onclick="javascript:noUpdate();"/><br/></td></tr>
				  </table>
			 </div>
	 <ul id="tree" class="tree"></ul>
	 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
	 <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	  <script type="text/javascript">
  		var skin = $("#skin").val();
	  	var _height=190;
		if(skin.indexOf("frame4.0") != -1) {
			_height = 220;
		}
		var zTree;
		var demoIframe;
		
		var setting;
		var isHand = "true";

		setting = {
			async: true,
			isSimpleData: true,
			asyncUrl:"<%=request.getContextPath()%>/epl_employeeBook.htm?method=<%=treemethod%>",  //获取节点数据的URL地址
			rootPID : -1,
			treeNodeKey: "id",
			//addDiyDom : addDom,
			treeNodeParentKey: "pId",
			callback:{
				beforeAsync: function(treeId, treeNode) {
					zTree.setting.asyncUrl="<%=request.getContextPath()%>/epl_employeeBook.htm?method=<%=treemethod%>&depId="+treeNode.id;
				},
				asyncSuccess:function(event, treeId, treeNode, msg){
					//zTree.expandAll(true);
					if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+
					   var rootNode = zTree.getNodeByParam("level", 0);
					   zTree.expandNode(rootNode, true, false);
					}
				},
				click: zTreeOnClick
				
				//asyncSuccess: zTreeOnAsyncSuccess,
				//asyncError: zTreeOnAsyncError
			}
		};
	
		function addDom(treeId, treeNode) {
			if (isHand == "true")
			{
				var add = "";
				var del = "";
				
				var edit = "";
				
				
				if (<%=addcode%>)
				{
		            add= "<button type='button' class='diyBtn1' id='add_" +treeNode.id+ "' title='"+getJsLocaleMessage('employee','employee_alert_157')+"' onfocus='this.blur();'></button>&nbsp;";
				}
				if (<%=delcode%>&&treeNode.pId!=-1)
				{
		            del="<button type='button' class='diyBtn2' id='del_" +treeNode.id+ "' title='"+getJsLocaleMessage('employee','employee_alert_122')+"' onfocus='this.blur();'></button>";
				
					
				}
				
				edit="<button type='button' class='diyBtn3' id='edit_" +treeNode.id+ "' title='"+getJsLocaleMessage('employee','employee_alert_158')+"' onfocus='this.blur();'></button>&nbsp;";
				
				
				var editStr = "<span id='hrefDiv'>&nbsp;&nbsp;&nbsp;"+add+edit+del+"</span>";
				$("#"+treeNode.tId+"_a").append(editStr);
				var btn = $("#add_"+treeNode.id);
				if (btn) 
					btn.bind("click", function(){
						$("#sdepcode").val(treeNode.id);
						$("#addDep").dialog("open");
					});
				var btn2 = $("#del_"+treeNode.id);
				if (btn2) btn2.bind("click", function(){doDel(treeNode.id,treeNode.name);});
				
				var btn3 = $("#edit_"+treeNode.id);
				if (btn3) btn3.bind("click", function(){ 
						$("#updateCode").val(treeNode.id);
						$("#depOldName").val(treeNode.name);
						$("#depNewName").val(treeNode.name);
						$("#depcodethirdUpdate").html(treeNode.depcodethird=="null"?"":treeNode.depcodethird);
						$("#updateDep").dialog("open");
				});
			} 
		}
	
		function addDepFun()
		{
			var oldName =  $("#depOldName").val();
			if(oldName==""||oldName==null)
			{
				alert(getJsLocaleMessage('employee','employee_alert_88'));
				return;
			}
			//$("#addDep").dialog("open");
			$("#addDep").dialog({
				autoOpen: true,
				height:_height,
				title:getJsLocaleMessage('employee','employee_alert_159'),
				width: 360,
				modal: true,
				resizable :false,
				close:function(){
					var name = $("#depName").val("");
		            //var scode = $("#sdepcode").val("");
		            var scode = $("#depcodethird").val("");
				}
			});
		}
	
		function updateDepFun()
		{
			var oldName =  $("#depOldName").val();
			if(oldName==""||oldName==null)
			{
				alert(getJsLocaleMessage('employee','employee_alert_160'));
				return;
			}
			$("#updateDep").dialog({
				autoOpen: true,
				height:_height-20,
				title:getJsLocaleMessage('employee','employee_alert_161'),
				width: 360,
				modal: true,
				resizable :false,
				close:function(){
				}
			});
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
	
			//首先判断 机构是否被删除 新增 p-------------------------------------------------------------
			$.get("<%=request.getContextPath()%>/epl_permissions.htm?method=checkdepDel", {
				selDepId:treeNode.id
			}, function(result) {
				if(result!=null && result=="-1"){
					alert(getJsLocaleMessage('employee','employee_alert_114'));
					window.location.reload();
				}else{
			//----------------------------------------------------------------------------------------
	
					//-----add------
					$("#sdepcode").val(treeNode.id);
					//-----update------
					$("#updateCode").val(treeNode.id);
					$("#depOldName").val(treeNode.name);
					$("#depNewName").val(treeNode.name);
					$("#depcodethirdUpdate").html(treeNode.depcodethird=="null"?"":treeNode.depcodethird);
					//---------delete----------
					
			 		//$('#depId').val(treeNode.id);
			 		if (treeNode.isParent) {
						setting.asyncUrl =  "<%=request.getContextPath()%>/epl_employeeBook.htm?method=<%=treemethod%>&depId="+treeNode.id;;
					}
					 $("#depId").val(treeNode.id);
					 $("#depName2").val(treeNode.name);
					 
					 //新增 pengj  点击机构树中的机构后，将页数置为第一页，这样可以重新查询该机构第一页时的总记录数、总页数等分页信息
					 //这样是为了避免点击机构时，仍然保持前一个机构的分页信息的bug
					 $('#txtPage').val(1);
					 //--------------------------
					 
					 submitForm();
				}
			});
		}
		
		var zNodes =[];
	
		$(document).ready(function(){
			setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			$.post("<%=path%>/epl_permissions.htm?method=isHandAdd&m=<%=treemethod%>",{},function(result){
	             if(result != null && result == "false")
	             {
	            	isHand = "false";
	             }
	             zTree = $("#tree").zTree(setting, zNodes);
			});
			
			//var nodes = zTree.getNodes();
		//	zTree.selectNode(nodes[2].nodes[0].nodes[0]);
			demoIframe = $("#testIframe");
			$("#addDep").dialog({
				autoOpen: false,
				height:_height,
				title:getJsLocaleMessage('employee','employee_alert_159'),
				width: 280,
				modal: true,
				resizable :false,
				close:function(){
					var name = $("#depName").val("");
		            //var scode = $("#sdepcode").val("");
		            var scode = $("#depcodethird").val("");
				}
			});
			
			
			$("#updateDep").dialog({
				autoOpen: false,
				height:_height-20,
				title:getJsLocaleMessage('employee','employee_alert_161'),
				width: 280,
				modal: true,
				resizable :false,
				close:function(){
				}
			});
			
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
				$('#depName').keypress(function(e) {
					var iKeyCode = window.event ? e.keyCode
							: e.which;
					if (iKeyCode == 60 || iKeyCode == 62) {
						if (isIE) {
							event.returnValue = false;
						} else {
							e.preventDefault();
						}
					}
				}
				);
				$('#depName').blur(function(e) {
					
					$(this).val($(this).val().replaceAll("<","").replaceAll(">",""));
				}
				);
					
			
			
		});
		function loadReady() {
			var h = demoIframe.contents().find("body").height();
			if (h < 600) h = 600;
			demoIframe.height(h);
		}
		
		
		
		function updateDepName()
			{
	             var pathUrl = $("#pathUrl").val();
	             var lgcorpcode = $("#lgcorpcode").val();
	             var code =  $("#updateCode").val();
	             var oldName =  $("#depOldName").val();
	             var newName =  $("#depNewName").val();
	            // newName = newName.replace(/(^\s*)|(\s*$)/g,"");
	             newName = newName.replace(/(\\|\s)+/g,""); 
	                if (newName == "")
	             {
	                 alert(getJsLocaleMessage('employee','employee_alert_92'));
	                 $("#depNewName").focus();
	                 return;
	             }
	             if(newName.length>20||oldName.length>20){
	             	 alert(getJsLocaleMessage('employee','employee_alert_162'));
	                 $("#depNewName").focus();
	                 return;
	             }
	             if(oldName == newName){
	             	alert(getJsLocaleMessage('employee','employee_alert_163'));
	             	$("#depNewName").select();
	             	return;
	             }
	             var type = "1";
	              var node = zTree.getSelectedNode().parentNode;  
	              if(node != null){
	                  var deleteZtree2Node = zTree.getNodesByParam("pId",node.id ,node);
					  if(deleteZtree2Node!=null){
						for(var i=0;i<deleteZtree2Node.length;i++){
							if(deleteZtree2Node[i].name == newName){
								alert(getJsLocaleMessage('employee','employee_alert_164'));
								$("#depNewName").select();
								return;
							}
						}
					 }
	              }else{
	              	type = "2";
	              }
				$("#updateCancle").attr("disabled",true);
				$("#updateSubmit").attr("disabled",true);
			     $.post(pathUrl+"/epl_employeeBook.htm?method=updateDepName",
			    	 {
			    	 	depName:newName,
			    	 	depCode:code,
			    	 	type:type,
			    	 	lgcorpcode:lgcorpcode
			    	 	},
				     function(resultMsg){
		                  if(resultMsg != null && resultMsg == "1")
		                  {
		                  		alert(getJsLocaleMessage('employee','employee_alert_74'));
		                  		//修改后将旧名称标识更新
		                  		$("#depOldName").val(newName);
		                  		var curTreenodes=zTree.getSelectedNode();
		                  		curTreenodes.name = newName;
		                  		zTree.updateNode(curTreenodes, true);
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        //window.location.href=location;
		                  }else if(resultMsg != null && resultMsg == "2"){
		                  		alert(getJsLocaleMessage('employee','employee_alert_76'));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  }else if(resultMsg != null && resultMsg == "3"){
		                	  	$("#depNewName").select();
		                  		alert(getJsLocaleMessage('employee','employee_alert_165'));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  }else if(resultMsg != null && resultMsg == "0"){
		                        noUpdate();
		                  		alert(getJsLocaleMessage('employee','employee_alert_166'));
		                	    $("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        return;
		                  /*备份p
		                  }else if(resultMsg != null && resultMsg == "4"){
		                        noUpdate();
		                  		alert("数据查询失败！");
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  */
		                  //新增 p 机构为null，可能已被其它用户删除---------------------------
		                  }else if(resultMsg != null && resultMsg == "4"){
		                        noUpdate();
		                  		alert(getJsLocaleMessage('employee','employee_alert_167'));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		            			/*刷新机构树
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
		                        */
		                        noUpdate();
		                        window.location.reload();
		                        
		                        return;
		                  //-------------------------------------------------------------------      
		                  }else{
		                  		alert(getJsLocaleMessage('employee','employee_alert_168'));
		                        window.location.href=location;
		                  }
		              
		             }
	             )
	             
		           //  $.post(pathUrl+"/epl_employeeBook.htm?method=addDep",{name:name,scode:scode},function(r){
		           //        if(r!=null&&r=="true")
		           //        {	location.href=location;
		             //      }else if(r.indexOf("机构不能超过9层")!=-1){
		            //       	alert(r);
		             //      }
		          //   })
			}
			
			
			function noUpdate(){
				  //$("#updateCode").attr("value","");
	              //$("#depOldName").attr("value","");
	              //$("#depNewName").attr("value","");
	               $("#depNewName").val($("#depOldName").val());
	              //$("#depcodethirdUpdate").html("");
	              $("#updateDep").dialog("close");
			}

  		</script>
  </body>
</html>
