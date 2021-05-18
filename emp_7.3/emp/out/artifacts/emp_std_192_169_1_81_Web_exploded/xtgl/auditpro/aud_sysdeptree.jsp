<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
    String path = request.getContextPath();
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	//登录id
	String lguserid = request.getParameter("lguserid");
	//判断是哪个审核DIV发出的请求
	String divcount = request.getParameter("divcount");
	//判断是不是回值
	String pathtype  = request.getParameter("pathtype");
	//审核流程的ID
	String flowid = request.getParameter("flowid");
	//第几个审核等级
	String auditlevel = request.getParameter("auditlevel");
	//该DIV审批所选择的ID串 
	String labelidstr = request.getParameter("labelidstr");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  		
<title></title>
	<%@include file="/common/common.jsp" %>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
<%if(StaticValue.ZH_HK.equals(langName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<%}%>

<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/aud_sysdeptree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<%--<link rel="stylesheet" type="text/css" href="<%=skin %>/aud_sysdeptree.css?V=<%=StaticValue.JSP_IMP_VERSION %>"/>--%>

<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>


  </head>
  <body id="aud_sysdeptree" class="aud_sysdeptree_body">
        <input type="hidden" id="path" value="<%=request.getContextPath()%>" name="path" />
        <input type="hidden" id="lguserid" value="<%=lguserid%>" name="lguserid" />
        <input type="hidden" id="pathtype" value="<%=pathtype%>" name="pathtype" />
        <input type="hidden" id="auditlevel" value="<%=auditlevel%>" name="auditlevel" />
        <input type="hidden" id="flowid" value="<%=flowid%>" name="flowid" />
        <input type="hidden" id="labelidstr" value="<%=labelidstr%>" name="labelidstr" />

  		<input type="hidden" name="divcount" id="divcount" value="<%=divcount %>">
  		<label id="test"></label>
	    <ul id="tree" class="tree tree2" ></ul>

        <script type="text/javascript" src="<%=iPath%>/js/aud_sysdeptree.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
  </body>
</html>
