<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.montnets.emp.znly.biz.CustomChatBiz"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.montnets.emp.znly.biz.CustomStatusBiz"%>
<%@page import="com.montnets.emp.znly.biz.GroupChatBiz"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String userJson = (String)request.getAttribute("userJson");
//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
// out print 内容参数变量
String queueMap =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_32", request);
String serverMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_33", request);
String msgMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_34", request);
String custWeixObjectMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_35", request);
String custAppObecjtMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_36", request);
String zjMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_37", request);
String serCountMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_38", request);
String weixCustMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_39", request);
String customStatusMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_40", request);
String changeStatusMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_41", request);
String onlineUserTimeInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_42", request);
String userReadedIdMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_43", request);
String groupMsgMapInfo =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_44", request);
String dbid =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_45", request);

%>
<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="/common/common.jsp" %>
	<title><emp:message key="zxkf_chat_title_31" defVal="在线客服内存消息" fileName="zxkf"/></title>
	<link rel="stylesheet" href="<%=iPath %>/static/css/base.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath %>/static/css/group_info.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath %>/static/css/nanoscroller2.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
</head>
<body style="font-size:14px;line-height:24px;">
	<%
	Map<String,String > quMap = new HashMap<String,String>();
	quMap.putAll(CustomChatBiz.getQueueMap());
	Iterator<String> it = quMap.keySet().iterator();
	out.print(queueMap+"queueMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+quMap.get(key));
		out.print("<br/>");
	}
	
	Map<String, JSONObject> serverMap = new HashMap<String, JSONObject>();
	serverMap.putAll(CustomChatBiz.getServerMap());
	it = serverMap.keySet().iterator();
	out.print(serverMapInfo+"serverMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+serverMap.get(key).toString());
		out.print("<br/>");
	}
	
	Map<String, JSONArray> msgMap = new HashMap<String, JSONArray>();
	msgMap.putAll(CustomChatBiz.getMsgMap());
	it = msgMap.keySet().iterator();
	out.print(msgMapInfo+"msgMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+msgMap.get(key).toString());
		out.print("<br/>");
	}
	
	Map<String,String > weixMap = new HashMap<String,String>();
	weixMap.putAll(CustomChatBiz.custWeixObjectMap);
	it = weixMap.keySet().iterator();
	out.print(custWeixObjectMapInfo+"custWeixObjectMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+weixMap.get(key));
		out.print("<br/>");
	}
	
	Map<String,String > appMap = new HashMap<String,String>();
	appMap.putAll(CustomChatBiz.custAppObecjtMap);
	it = appMap.keySet().iterator();
	out.print(custAppObecjtMapInfo+"custAppObecjtMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+appMap.get(key));
		out.print("<br/>");
	}
	
	Map<String,String > zjMap = new HashMap<String,String>();
	zjMap.putAll(CustomChatBiz.getZjMap());
	it = zjMap.keySet().iterator();
	out.print(zjMapInfo+"zjMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+zjMap.get(key));
		out.print("<br/>");
	}
	
	out.print(serCountMapInfo+"serCountMap--------<br/>"+CustomChatBiz.serCountMap.toString()+"<br/>");
	
	Map<String, JSONObject> weixCustMap = new HashMap<String, JSONObject>();
	weixCustMap.putAll(CustomChatBiz.weixCustMap);
	it = weixCustMap.keySet().iterator();
	out.print(weixCustMapInfo+"weixCustMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+weixCustMap.get(key).toString());
		out.print("<br/>");
	}
	
	Map<String, Integer> stateMap = new HashMap<String, Integer>();
	stateMap.putAll(CustomStatusBiz.customStatusMap);
	it = stateMap.keySet().iterator();
	out.print(customStatusMapInfo+"customStatusMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+stateMap.get(key).toString());
		out.print("<br/>");
	}
	
	out.print(changeStatusMapInfo+"changeStatusMap--------<br/>"+CustomStatusBiz.getChangeStatusMap().toString()+"<br/>");
	
	out.print(onlineUserTimeInfo+"onlineUserTime--------<br/>"+CustomStatusBiz.onlineUserTime.toString()+"<br/>");
	
	Map<String, Long> readIdMap = new HashMap<String, Long>();
	readIdMap.putAll(GroupChatBiz.getUserReadedIdMap());
	it = readIdMap.keySet().iterator();
	out.print(userReadedIdMapInfo+"userReadedIdMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+readIdMap.get(key).toString());
		out.print("<br/>");
	}
	
	Map<String, JSONArray> groupMsgMap = new HashMap<String, JSONArray>();
	groupMsgMap.putAll(GroupChatBiz.getGroupMspMap());
	it = groupMsgMap.keySet().iterator();
	out.print(groupMsgMapInfo+" groupMsgMap--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+groupMsgMap.get(key).toString());
		out.print("<br/>");
	}
	
	Map<String, Boolean> isUpdateDbMap = new HashMap<String, Boolean>();
	isUpdateDbMap.putAll(GroupChatBiz.getIsUpdateDbMap());
	it = isUpdateDbMap.keySet().iterator();
	out.print(dbid+"--------<br/>");
	while(it.hasNext())
	{
		String key = it.next();
		out.print(key+"="+isUpdateDbMap.get(key).toString());
		out.print("<br/>");
	}
	%>
</body>
</html>