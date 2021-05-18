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
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  <body style="margin:0;padding:0;">
  			<div id="addDep" style="padding:5px;display:none">
 			 		<input type="hidden" id="sdepcode" value="" />
				  <table align="center">
				  <tr><td height="5px;"></td></tr>
				  <tr><td width="90px" align="right"><emp:message key="employee_dxzs_title_56" defVal="机构名称" fileName="employee"/>2222：</td><td><input type="text" id="depName" maxlength="20" value="" /></td></tr>
				  <tr><td height="5px;"></td></tr>
				  <tr><td width="90px" align="right"><emp:message key="employee_dxzs_title_57" defVal="机构编码" fileName="employee"/>：</td><td><input type="text" id="depcodethird" maxlength="32" value="" /></td></tr>
				  <tr><td height="20px;">&nbsp;</td></tr>
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" id="depsub" onfocus="this.blur();" class="btnClass5 mr23"  onclick="javascript:doOk();"/>
				  <input type="button" value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>"  class="btnClass6" id="depcancel" onclick="javascript:doNo();"/><br/></td></tr>
				  </table>
			 </div>
			 
			 
			 	<div id="updateDep" style="padding:5px;display:none">
 			 	
				  <table>
				  <tr><td height="5px;"></td></tr>
				  <tr>	<td width="90px" align="right"><emp:message key="employee_dxzs_title_56" defVal="机构名称" fileName="employee"/>：</td>
				  		<td>
				  			<input type="hidden" id="updateCode" value="" />
				  			<input type="hidden" id="depOldName" value="" />
					  		<input type="text" id="depNewName" maxlength="20" value="" />
				  		</td>
				  </tr>
				  <tr><td height="5px;"></td></tr>
				  <tr>
				  	<td width="90px" align="right"><emp:message key="employee_dxzs_title_57" defVal="机构编码" fileName="employee"/>：</td>
				  	<td>
				  		<label id="depcodethirdUpdate"></label>
				  	</td>
				  </tr>
				  <tr><td height="20px;">&nbsp;</td></tr>
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>"  class="btnClass5 mr23" id="updateSubmit"  onclick="javascript:updateDepName();"/>
				  <input type="button" value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>"  class="btnClass6" id="updateCancle"  onclick="javascript:noUpdate();"/><br/></td></tr>
				  </table>
			 </div>
	 <ul id="tree" class="tree" style="width:auto;"></ul>
	 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
	 <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
	var zTree;
	var demoIframe;
	
	var setting;
	var isHand = "true";

	setting = {
		async: true,
		isSimpleData: true,
		asyncUrl:"<%=request.getContextPath()%>/epl_empDep.htm?method=<%=treemethod%>",  //获取节点数据的URL地址
		rootPID : -1,
		treeNodeKey: "id",
		//addDiyDom : addDom,
		treeNodeParentKey: "pId",
		callback:{
			beforeAsync: function(treeId, treeNode) {
				zTree.setting.asyncUrl="<%=request.getContextPath()%>/epl_empDep.htm?method=<%=treemethod%>&depId="+treeNode.id;
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
		$("#addDep").dialog({
			autoOpen: true,
			height:190,
			title:getJsLocaleMessage('employee','employee_alert_159'),
			width: 280,
			modal: true,
			resizable :false,
			close:function(){
				var name = $("#depName").val("");
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
			height:170,
			title:getJsLocaleMessage('employee','employee_alert_161'),
			width: 280,
			modal: true,
			resizable :false,
			close:function(){
			}
		});
	}
	
	function zTreeOnClick(event, treeId, treeNode) {

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
			setting.asyncUrl =  "<%=request.getContextPath()%>/epl_empDep.htm?method=<%=treemethod%>&depId="+treeNode.id;;
		}
		 $("#depId").val(treeNode.id);
		 $("#depName2").val(treeNode.name);
		 submitForm();
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
			height:190,
			title:getJsLocaleMessage('employee','employee_alert_159'),
			width: 280,
			modal: true,
			resizable :false,
			close:function(){
				var name = $("#depName").val("");
	            var scode = $("#depcodethird").val("");
			}
		});
		
		
		$("#updateDep").dialog({
			autoOpen: false,
			height:170,
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
             newName = newName.replace(/\s+/g,""); 
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
		     $.post(pathUrl+"/epl_empDep.htm?method=updateDepName",
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
	                  }else if(resultMsg != null && resultMsg == "4"){
	                        noUpdate();
	                  		alert(getJsLocaleMessage('employee','employee_alert_167'));
	                  		$("#updateCancle").attr("disabled",false);
	            			$("#updateSubmit").attr("disabled",false);
	                        noUpdate();
	                        return;
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
