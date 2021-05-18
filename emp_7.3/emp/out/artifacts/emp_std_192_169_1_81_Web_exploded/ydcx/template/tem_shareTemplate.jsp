<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="java.net.URLDecoder"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	String lgcorpcode = (String)request.getParameter("lgcorpcode");
	String tempId=request.getParameter("tempId");
	String tempName=request.getParameter("tempName");
	String userId=request.getParameter("userId");
	if(tempId==null||"".equals(tempId.trim())){
		tempId="0";
	}
	if(tempName==null||"".equals(tempName.trim())){
		tempName="-";
	}else{
		tempName=URLDecoder.decode(tempName,"UTF-8");
	}
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="ydcx_cxyy_mbbj_xzgxmb" defVal="选择共享模板" fileName="ydcx"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=iPath%>/css/installsel.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/shareTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	</head>
	<body id="ydcx_shareTemplate">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center>
			<div class="ydcx_container_sub">
			<form method="post" id="selectForm">
				<input type="hidden" id="pathUrl" value="<%=path %>">
				<input type="hidden" id="lguserid" value="<%=lguserid %>">
				<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode %>">
				<input type="hidden" id="tempId" value="<%=tempId %>">
				<input type="hidden" id="userid" value="<%=userId %>">
				<table class="ydcx_selectForm_tabl">
					<tr class="ydcx_tr1">
						<td class="ydcx_td1" align="left" colspan="2" height="18px;">
							<emp:message key="ydcx_cxyy_mbbj_mbid_mh" defVal="模板ID:" fileName="ydcx"></emp:message><%=tempId %>
						</td>
						<td class="ydcx_td2" align="left" height="18px;">
							&nbsp;&nbsp;<emp:message key="ydcx_cxyy_mbbj_mbmc_mh" defVal="模板名称：" fileName="ydcx"></emp:message><span id="tmpname"></span>
						</td>
					</tr>
					<tr class="ydcx_tr2">
						<td class="ydcx_td1"  align="left" colspan="2">
								<div class="div_bd ydcx_div_bd">
									<table border="0">
										<tr>
											<td width="165px" height="22px">
												<input id="searchname" name="searchname" type="text" maxlength="16" class="graytext ydcx_searchname" value=""  
												onkeypress="if(event.keyCode==13) {searchbyname();event.returnValue=false;}"/>
											</td>
											<td width="28px">
										   		 <a onclick="searchbyname()" id="searchUser" name="searchUser"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
											</td>
										</tr>
									</table>
							    </div>
						</td>
					</tr>
					<tr>
						<td style ="vertical-align:top">
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame"  name="sonFrame" class="ydcx_sonFrame"  frameborder="0" src="<%=iPath %>/tem_seltree.jsp?lguserid=<%=lguserid %>&tempId=<%=tempId %>"></iframe>
										</div>
										<div class="shownameDiv div_bd ydcx_shownameDiv">
											<span id="showUserName" class="title_bg ydcx_showUserName" ><emp:message key="ydcx_cxyy_mbbj_cylb_mh" defVal="成员列表：" fileName="ydcx"></emp:message>
											</span>
										</div>
										<div class="dept div_bd ydcx_dept">
											<select  multiple name="left" id="left" size="16" onfocus="treeLoseFocus()" class="ydcx_left" ondblclick="">
											</select>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td  align="center"  height="190px;" width="60px;">
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="ydcx_cxyy_common_text_10" defVal="选择" fileName="ydcx"></emp:message>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message>" onclick="javascript:moveOut();">
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
							<br/>
						</td>
						<td  style ="vertical-align:top">
							<div class="showchoiceDiv div_bd">
								<select multiple name="right" id="right" class="ydcx_right">
								</select>
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
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/shareTemp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$('#tmpname').text($(window.parent.document).find('#flowFrame').attr('tmpname'));
		$.post("<%=path%>/tem_mmsTemplate.htm",{
			method:"getSel",
			tempid:<%=tempId%>,
			infoType:"2"
			},function(returnmsg){
   			if(returnmsg != ""){
   				$("#right").empty();
   				$("#right").html(returnmsg);
	   		}
   		});
	});
		</script>
	</body>
</html>