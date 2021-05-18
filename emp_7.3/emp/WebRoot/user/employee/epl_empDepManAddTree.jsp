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
	String srcid = request.getParameter("srcid");
	
	String action = request.getParameter("action");
	String lgcorpcode = request.getParameter("lgcorpcode");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  	<%@include file="/common/common.jsp" %>
	<title><emp:message key="employee_dxzs_title_69" defVal="员工通讯录机构树" fileName="employee"/></title>
	<link href="<%=commonPath%>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style>
		#epl_empDepManAddTree{
			margin:0;padding:0;height: 500px;
		}
		#epl_empDepManAddTree #treeAdd{
			width:100%;margin-bottom: auto;
		}
		#epl_empDepManAddTree .user_div1{
			margin-bottom:30px;margin-top:15px; width: 360px;
		}
		#epl_empDepManAddTree .user_div1 input{
			margin-left: 60px;
		}
	</style>
  </head>
  <body id="epl_empDepManAddTree">
  			
	 <ul id="treeAdd" class="tree"></ul>
	 <div class="user_div1">
		<input type="button"  value="<emp:message key='employee_dxzs_button_1' defVal='确定' fileName='employee'/>" class="btnClass5" onclick="javascript:doadddepOk()" />
		<input type="button"  value="<emp:message key='employee_dxzs_button_5' defVal='取消' fileName='employee'/>" class="btnClass6" onclick="javascript:window.parent.dooperaterNo()" />
		<br/>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	 <script type="text/javascript">
	var zTreeAdd;
	var demoIframe;
	var setting;
	var isHand = "true";
	setting = {
		async: true,
		isSimpleData: true,
		asyncUrl:"<%=request.getContextPath()%>/epl_empDep.htm?method=<%=treemethod%>",  //获取节点数据的URL地址
		rootPID : -1,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		callback:{
			beforeAsync: function(treeId, treeNode) {
			zTreeAdd.setting.asyncUrl="<%=request.getContextPath()%>/epl_empDep.htm?method=<%=treemethod%>&depId="+treeNode.id;
			},
			asyncSuccess:function(event, treeId, treeNode, msg){
				if(!treeNode){	
					//判断是 顶级机构就展开,其余的收缩+
				   var rootNode = zTreeAdd.getNodeByParam("level", 0);
				   zTreeAdd.expandNode(rootNode, true, false);
				}
			},
			click: zTreeOnClick
		}
	};
	
	function zTreeOnClick(event, treeId, treeNode) {
		//-----add------
		$("#sdepcode").val(treeNode.id);
		//-----update------
		$("#updateCode").val(treeNode.id);
		$("#depOldName").val(treeNode.name);
		$("#depNewName").val(treeNode.name);
		$("#depcodethirdUpdate").html(treeNode.depcodethird=="null"?"":treeNode.depcodethird);
 		if (treeNode.isParent) {
			setting.asyncUrl =  "<%=request.getContextPath()%>/epl_empDep.htm?method=<%=treemethod%>&depId="+treeNode.id;;
		}
		 $("#depId").val(treeNode.id);
		 $("#depName2").val(treeNode.name);
	}
	
	var zNodes =[];
	$(document).ready(function(){
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		$.post("<%=path%>/epl_permissions.htm?method=isHandAdd&m=<%=treemethod%>",{},function(result){
             if(result != null && result == "false")
             {
            	isHand = "false";
             }
             zTreeAdd = $("#treeAdd").zTree(setting, zNodes);
		});
		demoIframe = $("#testIframe");
	});

		
		function doadddepOk(){
			var action = "<%=action%>";
			var srcid = "<%=srcid%>"+"";
			var lgcorpcode = "<%=lgcorpcode%>";
			var selectNode = zTreeAdd.getSelectedNode();
			if(selectNode == null || selectNode == ""){
				if(action=="add"){
					alert(getJsLocaleMessage('employee','employee_alert_145'));
				}else if(action=="edit"){
					alert(getJsLocaleMessage('employee','employee_alert_146'));
				}
				return;
			}
			var nodetagid = zTreeAdd.getSelectedNode().id+"";
			if(action=="add"){
				if(srcid==nodetagid){
					alert(getJsLocaleMessage('employee','employee_alert_147'));
					return;
				}
				if(confirm(getJsLocaleMessage('employee','employee_alert_148'))){
					$.post("<%=path%>/epl_empDep.htm?method=doAdddep",
						{srcid:srcid,tagid:nodetagid,lgcorpcode:lgcorpcode},
						function(r){
								if(r!=null){
									 if(r=="true")
				                     {
				                     	alert(getJsLocaleMessage('employee','employee_alert_149'));
				                     	parent.location.reload();
				                     }else if(r=="exits"){
					                     alert(getJsLocaleMessage('employee','employee_alert_150'));
					                 }
				                     else
				                     {
				                         alert(getJsLocaleMessage('employee','employee_alert_151'))
				                     }
								}else{
									alert(getJsLocaleMessage('employee','employee_alert_151'))
								}
			              
			               });
				}
			}else if(action=="edit"){
				if(srcid==nodetagid){
					alert(getJsLocaleMessage('employee','employee_alert_147'));
					return;
				}
				if(confirm(getJsLocaleMessage('employee','employee_alert_152'))){
					$.post("<%=path%>/epl_empDep.htm?method=doEditdep",
						{srcid:srcid,tagid:nodetagid,lgcorpcode:lgcorpcode},
						function(r){
			            		 if(r!=null){
				            			 if(r!=null&&r=="true")
					                     {
					                     	alert(getJsLocaleMessage('employee','employee_alert_153'));
					                     	parent.location.reload();
					                     }else if(r=="exits"){
					                    	 alert(getJsLocaleMessage('employee','employee_alert_154'));
						                 }else if(r=="isparent"){
							                 alert(getJsLocaleMessage('employee','employee_alert_155'));
							             }
					                     else
					                     {
					                         alert(getJsLocaleMessage('employee','employee_alert_156'))
					                     }
				             		}else
					             	{
				             			 alert(getJsLocaleMessage('employee','employee_alert_156'))
					            	}
			              
			               });
				}
			}
		}
		
  </script>
  </body>
</html>
