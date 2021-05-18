<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	LfSysuser lfSysuser = ((LfSysuser)session.getAttribute("loginSysuser")) ;
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("balanceMgr");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String operatePageReturn = (String)request.getAttribute("operatePageReturn");
	String operatorBalancePri = (String)request.getAttribute("operatorBalancePri");
%>
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cha_balanceMgr.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cha_balanceMgr.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="cha_balanceMgr" onload="showDepTable()">
	<div id="loginUser" class="hidden"></div>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			
			<%-- 内容开始 --%>
				<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<%if("admin".equals(lfSysuser.getUserName())){ %>
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					<h3 class="div_bd title_bg">
						操作员机构
					</h3>
					
					<div class="list depTree_div" >
					
						<div id="depTree"><jsp:include page="cha_depBalanceTree.jsp"></jsp:include>  </div>	
					</div>
				</div> 
				<div class="right_info">
				<%} else{%>
				
				<%-- EMP5.7新需求：增加对操作员充值和回收权限   by pengj --%>
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					<input type="hidden" id="operatorBalancePri" value="<%=operatorBalancePri != null?operatorBalancePri:"defaultBalancePri" %>"/>
					<h3 class="div_bd title_bg">
						操作员机构
					</h3>
					
					<div class="list depTree_div" >
					
						<div id="depTree"><jsp:include page="cha_depBalanceTreeByGeneralOperator.jsp"></jsp:include>  </div>	
					</div>
				</div> 
				<div class="right_info">
				<%-- end --%>
				<%} %>
				
				<form name="pageForm" action="" method="post">
						<input type="hidden" id="depId" value=""/>
						<input type="hidden" id="depTreeId" value=""/>
						<input type="hidden" id="upDepName" value=""/>
						<input type="hidden" id="servletUrl" value="cha_balanceMgr.htm"/>
					<div class="tableInfo_up_div"></div>
					<div id="tableInfo">
					</div>
					</form>
				<%if("admin".equals(lfSysuser.getUserName())){ %>
				</div>
				<%} %>
			<%--按添加按钮的HTML--%>
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"> 
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript">
			function LoginInfo(idname){
				document.getElementById(idname).innerHTML=window.parent.parent.document.getElementById("loginparams").innerHTML;
			}
			LoginInfo("loginUser");
		</script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/xtgl/charging/js/departmentBalance.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/xtgl/charging/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
			$(document).ready(function(){
				setLeftHeight();
				//getLoginInfo("#loginUser");
				$("#addDiv").dialog({
					autoOpen: false,
					height:300,
					width: 370,
					modal: true,
					buttons:{
						"确定":function(){
					       sub();
						},
						"取消":function(){
							doCancel();
						}
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

			function doCancel()
			{
				$("#addDepName").attr("value","");
				$("#depCodeThird").attr("value","");
				$("#addDepResp").val("");
				$('#addDiv').dialog('close');
			}
			
			function showDepTable()
			{
				$('#tableInfo').load('cha_balanceMgr.htm?tttt=<%=System.currentTimeMillis()%>',{method:'getTable',lgguid:$("#lgguid").val(),operatePageReturn:<%=operatePageReturn%>,operatorBalancePri:$("#operatorBalancePri").val()});
			}
		</script>
	</body>
</html>