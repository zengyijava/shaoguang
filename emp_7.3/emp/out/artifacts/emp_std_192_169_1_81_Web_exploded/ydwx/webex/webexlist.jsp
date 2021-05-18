<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
	String langName = (String)session.getAttribute("emp_lang");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/common/common.jsp" %>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>网讯列表树</title>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css"/>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	 <style type="text/css">
		 input#depsub,input#depcancel,input#updateSubmit,input#updateCancle{
			 text-indent: 0px;
			 text-align: center;
		 }
	 </style>
  	<%}%>
  	<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/webexbook.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
  <script type="text/javascript">

	var zTree;
	var demoIframe;
	
	var setting;
	var isHand = "true";

	setting = {
		async: true,
		isSimpleData: true,
		asyncUrl:"<%=request.getContextPath()%>/wx_attached.htm?method=<%=treemethod%>",  //获取节点数据的URL地址
		rootPID : -1,
		treeNodeKey: "id",
		//addDiyDom : addDom,
		treeNodeParentKey: "pId",
		callback:{
			beforeAsync: function(treeId, treeNode) {
				zTree.setting.asyncUrl="<%=path%>/wx_attached.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
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
	//增加DOM结构节点
	function addDom(treeId, treeNode) {
		if (isHand == "true")
		{
			var add = "";
			var del = "";
			
			var edit = "";
			
			
			if (<%=addcode%>)
			{
	            add= "<button type='button' class='diyBtn1' id='add_" +treeNode.id+ "' title='"+getJsLocaleMessage("ydwx","ydwx_common_xinzeng")+"' onfocus='this.blur();'></button>&nbsp;";
			}
			if (<%=delcode%>&&treeNode.pId!=-1)
			{
	            del="<button type='button' class='diyBtn2' id='del_" +treeNode.id+ "' title='"+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+"' onfocus='this.blur();'></button>";
			
				
			}
			
			edit="<button type='button' class='diyBtn3' id='edit_" +treeNode.id+ "' title='"+getJsLocaleMessage("ydwx","ydwx_common_bianji")+"' onfocus='this.blur();'></button>&nbsp;";
			
			
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

	
	//点击树形事件
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
			setting.asyncUrl =  "<%=request.getContextPath()%>/wx_attached.htm?method=<%=treemethod%>&depId="+treeNode.id;;
		}
		 $("#depId").val(treeNode.id);
		 $("#depName2").val(treeNode.name);
		 submitForm();
	}
	
	var zNodes =[];
	//初始化数据
	$(document).ready(function(){
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";		
		zTree = $("#tree").zTree(setting, zNodes);
		demoIframe = $("#testIframe");
		$("#addDep").dialog({
			autoOpen: false,
			height:150,
			title:getJsLocaleMessage("ydwx","ydwx_wxsc_1"),
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
			height:150,
			title:getJsLocaleMessage("ydwx","ydwx_wxsc_2"),
			width: 280,
			modal: true,
			resizable :false,
			close:function(){
				//$("#updateCode").attr("value","");
	            //$("#depOldName").attr("value","");
	            //$("#depNewName").attr("value","");
	            //$("#depcodethirdUpdate").html("");
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
	

  </script>


  </head>
  <body id="ydwx_webexlist">
  			<div id="addDep" class="ydwx_addDep">
 			 		<input type="hidden" id="sdepcode" value="" />
				  <table  class="ydwx_addDep_table">
				  <tr><td align="right"><emp:message key='ydwx_wxsc_add_1' defVal='分类名称：' fileName='ydwx'></emp:message></td><td><input type="text" id="depName" maxlength="20" value="" /></td></tr>
				  <tr><td height="20px;"></td></tr>
				  
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>" id="depsub" onfocus="this.blur();" class="btnClass5 mr23"  onclick="javascript:doOk();"/>
				  <input type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>"  class="btnClass6" id="depcancel" onclick="javascript:doNo();"/></td></tr>
				  </table>
			 </div>
			 
			 
			 	<div id="updateDep" class="ydwx_updateDep">
 			 	
				  <table>
				  <tr><td height="5px;"></td></tr>
				  <tr>	<td width="100px" align="right"><emp:message key='ydwx_wxsc_add_1' defVal='分类名称：' fileName='ydwx'></emp:message></td>
				  		<td>
				  			<input type="hidden" id="updateCode" value="" />
				  			<input type="hidden" id="depOldName" value="" />
					  		<input type="text" id="depNewName" maxlength="20" value="" />
				  		</td>
				  </tr>
				  <tr><td height="10px;"></td></tr>
				  <tr><td height="10px;"></td></tr>
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>"  class="btnClass5 mr23" id="updateSubmit"  onclick="javascript:updateDepName();"/>
				  <input type="button" value="<emp:message key='ydwx_common_btn_quxiao' defVal='取消' fileName='ydwx'></emp:message>"  class="btnClass6" id="updateCancle"  onclick="javascript:noUpdate();"/></td></tr>
				  </table>
			 </div>
	 <ul id="tree" class="tree ydwx_tree"></ul>
  </body>
</html>
