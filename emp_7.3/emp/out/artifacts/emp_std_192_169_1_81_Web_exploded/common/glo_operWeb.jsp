<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=path %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
	<link rel="stylesheet" href="<%=path %>/common/skin/default/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
  	<%}%>
  </head>
  	
  
  <body>
   	 	<form action="<%=path %>/glo_operWebSvt.hts?method=getweb" method="post" name="form2" id="form2">
	   	 	<div align="left" style="margin-left: 80px;display: none;">
				<emp:message key="common_operWeb_1" defVal="所要修改的web路径：" fileName="common"></emp:message><input type="file" value="" name="webfileurl" id="webfileurl">
	   	 	</div>
	   	 	<table align="center" style="width: 90%" border="1">
		   	 	<tr>
		   	 			<th>
							<emp:message key="common_operWeb_2" defVal="模块ID" fileName="common"></emp:message>
		   	 			</th>
		   	 			<th>
							<emp:message key="common_operWeb_4" defVal="模块名称" fileName="common"></emp:message>
		   	 			</th>
		   	 			<th>
							<emp:message key="common_operWeb_6" defVal="模块状态" fileName="common"></emp:message>
		   	 			</th>
		   	 	</tr>
		   	 	<tbody id="bodymsg">
		   	 	</tbody>
	   	 	</table>
	   	 	<div align="center" style="margin-top:40px;">
	   	 		<input type="button" value="<emp:message key="common_newlyIncrease" defVal="新增" fileName="common"></emp:message>" onclick="javascript:addnewMenu();" style="width: 55px;">
	   	 			&nbsp;&nbsp;&nbsp;
	   	 		<%--
	   	 		<input type="button" value="初始化" onclick="" style="width: 55px;">
	   	 		&nbsp;&nbsp;&nbsp;
	   	 		--%><input type="button" value="<emp:message key="common_commit" defVal="提交" fileName="common"></emp:message>" onclick="javascript:doSubmit();" style="width: 55px;">
	   	 		&nbsp;&nbsp;&nbsp;
	   	 		<input type="button" value="<emp:message key="common_refresh" defVal="刷新" fileName="common"></emp:message>" onclick="javascript:doRefresh();" style="width: 55px;">
	   	 	</div>
   	 	</form>
   	 	
	 		<div id="addMenu"   title="<emp:message key="common_operWeb_7" defVal="新增模块" fileName="common"></emp:message>" style="padding:5px;display:none;">
            	<center>
            		<table >
						<tr>
							<td style="font-size: 14px;">
								<emp:message key="common_operWeb_3" defVal="模块ID：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="menuid" name="menuid" type="text"  maxlength="8"  onkeyup="value=value.replace(/[^A-Za-z]+$/,'')"  style="width: 150px;height:16px;font-size: 14px;"/> 
							</td>
						</tr>
						<tr>
							<td style="font-size: 14px;">
								<emp:message key="common_operWeb_5" defVal="模块名称：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="menuname" name="menuname" type="text"  onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" maxlength="12" style="width: 150px;height: 16px;font-size: 14px;"/> 
								
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						 <tr>
						 	<td colspan="2" align="center" >
						 		<input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"  style="height: 24px;font-size: 12px;" onclick="javascript:add();"/>
						 		<input type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>"  style="height: 24px;font-size: 12px;" onclick="javascript:cancel();"/>
						 	</td>
						 </tr>
			</table>
		</center>
		</div>
		<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script  type="text/javascript" src="<%=path%>/common/commonJs/glo_operWeb.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
  </body>
</html>














