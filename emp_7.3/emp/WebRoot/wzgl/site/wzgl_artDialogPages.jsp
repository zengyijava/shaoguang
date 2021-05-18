<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.entity.site.LfSitPage"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
	//获取路径
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
	String sId = request.getParameter("sId");
	String lgcorpcode = request.getParameter("lgcorpcode");
	List<LfSitPage> sitPageList = (List<LfSitPage>)request.getAttribute("sitPageList");
	String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>选择微站页面</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
				<form name="pageForm" action="wzgl_siteManager.htm?method=getSitePages" method="post" id="pageForm">
				<div id="hiddenValueDiv" class="hidden"></div>
					<input type="hidden" name="lgcorpcode" value="<%=lgcorpcode%>"/>
					<div class="buttons">
					</div>
					<input type="hidden" id="sId" name="sId" value="<%= sId %>">
					<div id="container" class="container">
						<div class="bd">
							<table id="content">
								<thead>
									<tr>
										<th class="th_l_b">
											<emp:message key="common_text_10" defVal="选择"
												fileName="common" />
										</th>
										<th>
											<emp:message key="wzgl_qywx_site_text_17" defVal="微站页面名称"
												fileName="wzgl" />
										</th>
										<th>
											<emp:message key="wzgl_qywx_form_text_6" defVal="创建时间"
												fileName="wzgl" />
										</th>
										<th class="th_r_b">
											<emp:message key="common_text_14" defVal="操作"
												fileName="common" />
										</th>
									</tr>
								</thead>
								<tbody>
								<%for(LfSitPage sitpage : sitPageList ) {%>
									<tr>
										<td class="no_l_b">
											<input type="radio" name="checklist" value="<%=sitpage.getPageId()%>">
											<input type="hidden" name="url" value="<%=sitpage.getUrl()%>">
										</td>
										<td>
											<a>
												<%=sitpage.getName()%>
											</a> 
										</td>
										<td>
											<%=sdf.format(sitpage.getCreatetime())%>
										</td>
										<td class="no_r_b">
											 <a onclick="doPreview('<%=sitpage.getUrl()%>')">	<emp:message key="wzgl_qywx_site_text_18" defVal="页面预览"
												fileName="wzgl" /></a>
										</td>
									</tr>
								<%}%>
								</tbody>
								<tfoot>
									<tr>
										<td colspan="4">
											<div id="pageInfo"></div>
										</td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>						
				</form>
			</div>
			<%-- 内容结束 --%>
			<div id="pageStuff" style="display:none;">
				<input type="hidden" id="pathUrl" value="<%=path%>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
				<input type="hidden" id="currentTotalPage" value="<%=pageInfo.getTotalPage()%>"/>
				<input type="hidden" id="currentPageIndex" value="<%=pageInfo.getPageIndex()%>"/>
				<input type="hidden" id="currentPageSize" value="<%=pageInfo.getPageSize()%>"/>
				<input type="hidden" id="currentTotalRec" value="<%=pageInfo.getTotalRec()%>"/>
			</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<link href="<%=commonPath %>/wxcommon/css/artDialogCss.css" rel="stylesheet" type="text/css" />
   	<link href="<%=commonPath %>/wxcommon/<%=wxskin %>/artDialogCss.css" rel="stylesheet" type="text/css" />
   	 <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
	<script>
		//分页
		showPageInfo2($("#currentTotalPage").val(),$("#currentPageIndex").val(),$("#currentPageSize").val(),$("#currentTotalRec").val());
		
		/**
		 * 微站预览
		 */
		function doPreview(urlToken){
			url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=getPagePreview&from=pc&urlToken="+urlToken;
			showbox({src:url,mode:1});
		}
	</script>
	</body>
</html>