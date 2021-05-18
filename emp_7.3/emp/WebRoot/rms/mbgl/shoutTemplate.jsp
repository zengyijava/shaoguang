<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.common.vo.LfMaterialVo"%>
<%@ page import="com.montnets.emp.rms.servmodule.constant.ServerInof"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("template");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	String findResult = (String)request.getAttribute("findresult");
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    	<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
  </head>
  
  <body>
  	<div id="container" class="container">
		<%-- header开始 --%>
		<%=ViewParams.getPosition(langName,menuCode) %>
		<%-- header结束 --%>
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<form name="pageForm" action="/rms_templateMana.htm?method=find" method="post" id="pageForm">
				<table id="content">
					<thead>
						<tr>
							<th>
								ID
							</th>
							<th>
								<emp:message key="rms_fxapp_fsmx_tempid2" defVal="模板ID" fileName="rms"/>
							</th>
							<th>
								<emp:message key="rms_fxapp_mbgl_mbmc" defVal="模板名称" fileName="rms"/>
							</th>
							<th>
								<emp:message key="rms_fxapp_dwtjbb_operation" defVal="操作" fileName="rms"/>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(shortTemplateList) == 0}">
								<tr>
									<td colspan="4" align="center"><emp:message key="" defVal="无记录" fileName="rms"/></td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${shortTemplateList}" var="short" varStatus="i">
									<tr>
										<td>${short.id}</td>
										<td>${short.tempId}</td>
										<td>${short.tempName}</td>
										<td>
											<input type="button" value="删除" onclick = "deleteShortTemp('${short.id}')" style="border: 0px;color:#0e5ad1;background: #ffffff;">
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="4">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
				<%-- foot开始 --%>
				
				<div class="bottom">
					<div id="bottom_right">
						<div id="bottom_left"></div>
						<div id="bottom_main">
							<div id="pageInfo"></div>
						</div>
					</div>
				</div>
				<div id="orderCode" class="hidden"></div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			var findresult="<%=findResult%>";
			if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage("rms","rms_repdegree_alert1"));	
		       return;			       
		    }
		});
		function deleteShortTemp(id){
			if(confirm(getJsLocaleMessage("rms","rms_mbgl_confirmdel"))){
				$.ajax({
					type:"POST",
					url :"rms_templateMana.htm?method=deleteShortTemp&id="+id,
					beforeSend:function () {
	                    page_loading();
	                },
	                complete:function () {
	               	  	page_complete()
	                }, 
	                success:function (result) {
		                if (result == 'true') {
	                        alert(getJsLocaleMessage("rms","rms_myscene_successdel"));
	                        window.location.reload();
	                        window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
	                    } else {
	                        alert(getJsLocaleMessage("rms","rms_myscene_faildel"));
	                    }
	                }
				});
			}
		}
	</script>
  </body>
</html>
