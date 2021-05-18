<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.util.StringUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.site.LfSitInfo"%>
<%@page import="com.montnets.emp.entity.site.LfSitType"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
    
    @SuppressWarnings("unchecked")
    List<LfSitInfo> infoList = (List<LfSitInfo>)request.getAttribute("infoList");
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> typeHashMap = (LinkedHashMap<String, String>)request.getAttribute("typeHashMap");
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
    //微信类型
    String typeId = conditionMap.get("typeId");
    if(typeId == null || "".equals(typeId))
    {
        typeId = "-1";
    }
    //微信名称
    String name = conditionMap.get("name&like");
    //微站风格
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<div id="container" class="container">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="wzgl_siteManager.htm?method=find" method="post" id="pageForm">
					<div style="display: none" id="hiddenValueDiv">
					</div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<a id="addeployee" onclick="javascript:doAdd()"><emp:message key="wzgl_qywx_site_text_28" defVal="创建微站"
												fileName="wzgl" /></a>	
					</div>
					<div id="condition" style="display: block;">
					<table>
							<tbody>
								<tr>
									<td><emp:message key="wzgl_qywx_site_text_19" defVal="风格类型："
												fileName="wzgl" /></td>
									<td>
										<label>
										<select id="typeId" name="typeId" class="input_bd"
											style="width:225px">
												<option value=""><emp:message key="wzgl_qywx_site_text_20" defVal="全部"
												fileName="wzgl" /></option>
												<%
													if (typeHashMap!=null&&typeHashMap.keySet().size()>0) {
													    for (Iterator it =  typeHashMap.keySet().iterator();it.hasNext();){
													        String key = (String)it.next();
													     	String value =  typeHashMap.get(key); 
												%>
												<option value="<%=key%>"
													<%=(typeId.equals(key)) ? "selected" : ""%>>
													<%= value%></option>
												<%
													}
													}
												%>
										</select>
										</label>
									</td>
									<td><emp:message key="wzgl_qywx_site_text_21" defVal="微站名称："
												fileName="wzgl" /></td>
									<td>
									<input type="text" id="name" name="name" value="<%=(name==null) ? "" : name%>">
									</td>
									<td class="tdSer">
										<a id="search" onclick="javascript:doSearch();"></a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="bd">
						<table id="content">
							<thead>
								<tr>
									<th>
										<emp:message key="wzgl_qywx_site_text_29" defVal="编号"
												fileName="wzgl" />
									</th>
									<th>
										<emp:message key="wzgl_qywx_site_text_22" defVal="微站名称"
												fileName="wzgl" />
									</th>
									<th>
										<emp:message key="wzgl_qywx_site_text_23" defVal="微信风格"
												fileName="wzgl" />
									</th>
									<th>
										<emp:message key="wzgl_qywx_form_text_6" defVal="创建时间"
												fileName="wzgl" />
									</th>
									<th colspan="4">
										<emp:message key="common_text_14" defVal="操作"
											fileName="common" />
									</th>
								</tr>
							</thead>
							<tbody>
								<%
								    if(infoList != null && infoList.size() > 0)
								    {
								        for (LfSitInfo info : infoList)
								        {
								%>
								<tr>
									<td valign="middle">
										<%= info.getSId()%>
									</td>
									<td>
										<%= info.getName()%>
									</td>
									<td>
										<%= typeHashMap.get(String.valueOf(info.getTypeId()))%>
									</td>
									<td>
										<%=formatter.format(info.getCreatetime())%>
									</td>
									<td>
										<a href="javascript:doPreview('<%= info.getUrl() %>')"><emp:message key="common_text_6" defVal="预览"
												fileName="common" /></a>
										<a href="javascript:doEdit(<%= info.getSId() %>)"><emp:message key="common_text_7" defVal="编辑"
												fileName="common" /></a>
										<a href="javascript:doDelete(<%= info.getSId() %>)"><emp:message key="common_text_8" defVal="删除"
												fileName="common" /></a>
									</td>
								</tr>
								<%
								    }
								    }
								    else
								    {
								%>
								<tr>
									<td align="center" colspan="11">
									<emp:message key="common_text_1" defVal="无记录"
												fileName="common" />
									</td>
								</tr>
								<%
								    }
								%>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="8">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
					
				</form>
			</div>
			<%-- 内容结束 --%>
			<div id="divPreBox" style="display:none" title="<emp:message key="common_text_6" defVal="预览"
												fileName="common" />">
				<div style="width:240px;height:460px;margin-top:-3px; background:url(<%=commonPath%>/common/img/iphone5.jpg);">
					<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame" src=""></iframe>	
				</div>
			</div> 
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<div id="pageStuff" style="display: none;">
				<input type="hidden" id="pathUrl" value="<%=path%>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
				<input type="hidden" id="currentTotalPage" value="<%=pageInfo.getTotalPage()%>"/>
				<input type="hidden" id="currentPageIndex" value="<%=pageInfo.getPageIndex()%>"/>
				<input type="hidden" id="currentPageSize" value="<%=pageInfo.getPageSize()%>"/>
				<input type="hidden" id="currentTotalRec" value="<%=pageInfo.getTotalRec()%>"/>
				<input type="hidden" id="lguserid" value="<%=lguserid%>" />
				<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode%>" />
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/siteList.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
    		$('#typeId').isSearchSelect({'width':'165','isInput':false,'zindex':0});
    		//分页
    		initPage($("#currentTotalPage").val(),$("#currentPageIndex").val(),$("#currentPageSize").val(),$("#currentTotalRec").val());
		</script>
	</body>
</html>