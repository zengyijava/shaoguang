<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="appmage_xxfb_appxxfs_opt_selectobject" defVal="选择发送对象" fileName="appmage"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			center {
				font-size: 14px;
			}
			select {
				font-size: 14px;
			}
			.app_search{
				position:absolute;
				left:100px;
				top:3px;
				z-index:1;
				color:#999;
				width: 120px;
				font-size: 12px;
			}
		</style>
		<script>
		var base_ipath = "<%=path%>";
		</script>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center class="centerBack">
			<div style="height:465px">
			<form method="post" id="selectForm">
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<select style="display:none" id='tempOptions'></select>
				<select style="display:none" id='rightSelectTemp'></select>
				<select style="display:none" id='rightSelectTempAll'></select>
				<input type="hidden" id="pageIndex1" name="pageIndex1" value="1"/>
				<input type="hidden" id="totalPage1" name="totalPage1" value=""/>		
				
				<table border="0"  class="chooseBox">
					<tr>
						<td style="font-size:12px;height:24px;vertical-align:top" align="left" colspan="2">
						<div class="div_bd" style="width:80px;height:22px;background-color:#ffffff;float:left;vertical-align: middle">
						<select name="chooseType" id="chooseType" style="line-height:22px;width:80px;border:0;padding:0;margin:0;font-size:12px" onchange="changeChooseType()">
							<option value="1" id="chooseType1"><emp:message key="appmage_xxfb_appxxfs_text_group" defVal="群组" fileName="appmage"></emp:message></option>
							<option value="2" id="chooseType2"><emp:message key="appmage_xxfb_appxxfs_text_appeaccount" defVal="APP企业账号" fileName="appmage"></emp:message></option>
						</select>
						</div>
						<div class="div_bd search_tx_choose">
						<table border="0">
						<tr>
						<td width="125" valign="top">
						<input id="epname" name="epname" type="text" maxlength="16" class="graytext" value="<emp:message key="appmage_xxfb_appxxfs_text_inputaccount" defVal="请输入用户账号" fileName="appmage"></emp:message>" style="font-size:12px;border:0px;width:125px;height:22px;background-color:#F3FAEE" onkeypress="if(event.keyCode==13) {searchAppCode();event.returnValue=false;}"/>
						</td>
						<td width="28px">
					    <a href="javascript:void(0)" onclick="searchAppCode()"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
						</td>
						</tr>
						</table>
					    </div>
						</td>
						<td style="font-size:12px;color:#666666;text-align:left;"><emp:message key="appmage_xxfb_appxxfs_text_selectpeploenum" defVal="所选总人数：" fileName="appmage"></emp:message><label id="manCount">0</label></td>
					</tr>
					</table>
					<table border="0"  class="chooseBox">
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<div id="showList"  class="dept hidden div_bd" style="background-color:#ffffff;height:240px;width:240px;"></div>
										<div class="div_bd div_bg" style="border-bottom:0;margin-top:10px;height:22px;width:242px;float:left;overflow: hidden">
											<span style="float:left;margin-top:3px;font-size:12px">&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_memberlist" defVal="成员列表：" fileName="appmage"></emp:message></span><span id="showUserName" style="display:none;height:22px;width:240px;padding-top: 5px; padding-left:10px; text-align: left;font-size: 12px;">当前群组：</span>
										</div>
										<div class="dept div_bd" style="height: 125px; width: 237px;overflow:hidden;padding-left:5px;">
											<select  multiple name="left" id="left" size="15" onfocus="treeLoseFocus()" ondblclick='router()' class="left_select_choose">
											</select>
										 
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td width="60" align="center">
						<br>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="appmage_common_opt_xuanze" defVal="选择" fileName="appmage"></emp:message>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key="appmage_common_opt_shanchu" defVal="删除" fileName="appmage"></emp:message>" onclick="javascript:moveRight();">
						</td>
						<td>
							<div id="rightDiv"   class="dept div_bd" style="">
								<select multiple name="right" id="right" size="20" 
									style='width:204px;height: 418px;display:none; border:0;float:left;color: black;font-size: 12px;padding:4px;vertical-align:middle;margin:-6px -10px;' >
								</select>
								<ul id="getChooseMan">
								</ul>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="pageDiv" style="margin-top: 3px;">
										<input type="button" class="btnClass1" value="<emp:message key="appmage_common_opt_prepage" defVal="上一页" fileName="appmage"></emp:message>" id="lastPage1" onclick="javascript:goLastPage1()"/>
										<input type="button" class="btnClass1" value="<emp:message key="appmage_common_opt_nextpage" defVal="下一页" fileName="appmage"></emp:message>" id="nextPage1" onclick="javascript:goNextPage1()"/>
										<label id="showPage1"></label>
										<br/>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		</center>
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
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=116"></script>
   		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send_cx.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath%>/js/app_chooseSendInfo.js?V=116.1"></script>
	</body>
</html>