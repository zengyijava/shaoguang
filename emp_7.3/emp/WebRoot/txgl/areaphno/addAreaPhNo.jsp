<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfTructType"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
//@SuppressWarnings("unchecked")
//List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	


@SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("areaPhoneNo");
menuCode = menuCode==null?"0-0-0":menuCode;
@ SuppressWarnings("unchecked")
List<LfTructType> tructlist = (List<LfTructType>)request.getAttribute("tructList");

@ SuppressWarnings("unchecked")
List<String> provinceList = (List<String>)request.getAttribute("provinceList");
@ SuppressWarnings("unchecked")
Map<String,List<String>> provinceAndCityMap=(Map<String,List<String>>)request.getAttribute("provinceAndCityMap");
String msType = (String)request.getAttribute("msType");
String txglFrame = skin.replace(commonPath, inheritPath);

//确定
String qd = MessageUtils.extractMessage("common", "common_confirm", request);
//取消
String qx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_qx", request);

%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_qyhdgl_xzsxtdbdml" defVal="新增上行业务指令绑定" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addAreaPhNo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="addAreaPhNo">
	<input id="skin" type="hidden" value=<%=skin %>>
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<center>
					<div >
						<form action="<%=path %>/seg_areaPhoneNo.htm?method=add" method="post" name="bindform" id="bindform">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						<input type="hidden" id="opType" name="opType" value="add"/>
						<input type="hidden" id="selectedType" name="selectedType" value="add"/>
							<table id="editServiceBindTable" class="editServiceBindTable">
							<thead>	
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>							
								<tr>
									<td class="editServiceBindTable_td1" align="right">
										<emp:message key="txgl_wgqdpz_qyhdgl_sf" defVal="省份：" fileName="txgl"></emp:message>
									</td>
									<td class="editServiceBindTable_td2">
									<select  id="province" name="province" class="input_bd province" maxlength="85" onchange="getcity($(this).val())">
													   <option value="">==<emp:message key="txgl_wgqdpz_qyhdgl_qxz" defVal="请选择" fileName="txgl"></emp:message>==</option>
													    <%if (provinceList != null&&provinceList.size()>0){
													   		for(int i=0;i<provinceList.size();i++){
													   		String proStr=provinceList.get(i);
													   %>
													   			<option value="<%=proStr %>"  ><%=proStr %></option>
													   		<% }%>
													  	
													   <%} %>
											    </select>
										<span> <font color="red">*</font></span>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td class="editServiceBindTable_td1" align="right">
										<emp:message key="txgl_wgqdpz_qyhdgl_cs" defVal="城市：" fileName="txgl"></emp:message>
									</td>
									<td class="editServiceBindTable_td2">
									 <select id="city" name="city"  class="input_bd city">
										<option id="cityOption" value="">==<emp:message key="txgl_wgqdpz_qyhdgl_qxz" defVal="请选择" fileName="txgl"></emp:message>==</option>
									 </select>
									<span> <font color="red">*</font></span>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>	
								<tr>
									<td class="editServiceBindTable_td1" align="right">
										<emp:message key="txgl_wgqdpz_qyhdgl_hd" defVal="号段：" fileName="txgl"></emp:message>
									</td>
									<td class="editServiceBindTable_td2">
									<input type="text" id="mobile" name="mobile"  class="input_bd mobile"/>
									<span> <font color="red">*号段只能是正整数</font></span>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td id="btn" colspan="2" class="btn">
										<input name="subBut" type="button" id="subBut" value="<%=qd %>" class="btnClass5 mr23"/>
										<input name="" type="button" onclick="javascript:window.parent.closeAddAreaPhNodiv();"
													value="<%=qx %>" class="btnClass6"/>
										<br/>
									</td>
								</tr>
								</thead>
							</table>
						</form>
					</div>
				</center>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>	 
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script language="javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript" src="<%=commonPath%>/txgl/areaphno/js/addAreaPhNo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/areaphno.js" ></script>
	</body>
</html>
