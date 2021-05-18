<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");


@SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("busPkgMgr");
menuCode = menuCode==null?"0-0-0":menuCode;

List<DynaBean> busList = (List<DynaBean>)request.getAttribute("busList");

%>
<html>
	<head><%@include file="/common/common.jsp"%>
		<title><emp:message key="ydyw_ywgl_ywbgl_text_8" fileName="ydyw" defVal="新建业务包"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/params.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/addBusPkg.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydyw/ywgl/css/pkgMgr.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style type='text/css'>
		</style>
	</head>

	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<%=ViewParams.getPosition(empLangName,menuCode) %>
			<div id="rContent" class="rContent">
				<center>
					<div style="text-align: left;padding-left: 30px;padding-top: 15px;">
						<form action="<%=path %>/ydyw_busPkgMgr.htm?method=update" method="post" name="busPkgform" id="busPkgform">
							<div id="corpCode" class="hidden"></div>
							<table cellpadding="0" cellspacing="0" border="0" class="contTable">
								<tr>
									<td><emp:message key="ydyw_ywgl_ywbgl_text_1_p" fileName="ydyw" defVal="业务包名称："></emp:message></td>
									<td>
										<input type="text" class="input_bd div_bd wth1" id="busPkgName" name="busPkgName" maxlength="25"/>
										<font style="color: red;">*</font>
									</td>
								</tr>
								<tr><td colspan="2" style="height: 15px;"></td></tr>
								<tr>
									<td><emp:message key="ydyw_ywgl_ywbgl_text_3_p" fileName="ydyw" defVal="业务包编号："></emp:message>	</td>
									<td>
										<input type="text" class="input_bd div_bd wth1" id="busPkgCode" name="busPkgCode" maxlength="25"/>
										<font style="color: red;">*</font>
									</td>
								</tr>
								<tr><td colspan="2" style="height: 15px;"></td></tr>
								<tr>
									<td  valign="top"><emp:message key="ydyw_ywgl_ywbgl_text_6" fileName="ydyw" defVal="添加业务："></emp:message></td>
									<td style="background-color: #f0f2f9;padding: 10px 20px 20px 20px;border:solid 1px #c8daf3;">
										<input type="text" class="input_bd div_bd wth2" id="busSearStr" name="busSearStr" style="float:left;height: 21px;line-height:21px; margin-top: 2px;"/>
										<a onclick="searchName();" class="searIcon"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
										<span style="margin-left:170px;color: #333433;"><emp:message key="ydyw_ywgl_ywbgl_text_7" fileName="ydyw" defVal="已选择业务数："></emp:message><label id="manCount">0</label></span>
									    <table cellpadding="0" cellspacing="0" border="0" style="margin: 0px;clear: both;">
											<tr>
												<td>
													<div class="div_bd" style="height: 258px; width: 205px;overflow:hidden;padding-left:5px;">
														<select  multiple name="left" id="left" size="15" class="left_select_choose">
														<% 
														if(busList!=null&&busList.size()>0){
														for(int i=0;i<busList.size();i++){
														%>
															<option value="<%=busList.get(i).get("bus_code")%>" title="<%=busList.get(i).get("bus_name")%>(<%=busList.get(i).get("bus_code")%>)"><%=busList.get(i).get("bus_name")%>(<%=busList.get(i).get("bus_code")%>)</option>
														<%}
														}
														%>											
														</select>
													</div>
												</td>
												<td width="60" align="center">
												<br>
													<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="common_option" defVal="选择" fileName="common"></emp:message>"  onclick="javascript:router();">
													<br/>
													<br/>
													<input class="btnClass1" type="button" id="toRight" value="<emp:message key="common_delete" defVal="刪除" fileName="common"></emp:message>" onclick="javascript:moveRight();">
												</td>
												<td>
													<div id="rightDiv"   class="div_bd" >
														<select multiple name="right" id="right" size="20"  style='width:104px;height: 318px;display:none; border:0;float:left;color: black;font-size: 12px;padding:4px;vertical-align:middle;margin:-6px -10px;' >
														</select>
														
														<ul id="getChooseMan">
														</ul>
													</div>
													<span style="display:block;width:5px;height:5px;color: red;margin-top:130px; float: left;">*</span>
												</td>
											</tr>
										</table>										
									</td>
								</tr>
								<tr>
									<td></td>
									<td id="btn" style="text-align: center;padding:15px;">
										<input name="subBut" type="button" id="subBut" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="save()" class="btnClass5 mr23"/>
										<input name="cancelwid" type="button" onclick="back()" value="<emp:message key="common_btn_10" defVal="返回" fileName="common"></emp:message>" class="btnClass6"/>
									</td>
							  	</tr>								
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
	    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath %>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=116"></script>
		<script type="text/javascript" src="<%=iPath%>/js/chooseInfo_new.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=iPath%>/js/addBusPkg.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
		<script type="text/javascript">
	
		$(document).ready(function() {
			getLoginInfo("#corpCode");
		});
	
		</script>
	</body>
</html>
