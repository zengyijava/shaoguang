<%@ page language="java" import="com.montnets.emp.common.constant.SystemGlobals" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.entity.system.LfSkin"%>
<%@page import="com.montnets.emp.entity.system.LfTheme"%>
<%@page import="com.montnets.emp.entity.system.LfUser2skin"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String pageUrl = request.getContextPath() + "/"
			+ SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME);
	@SuppressWarnings("unchecked")
	List<LfTheme> themeList = (List<LfTheme>) request
			.getAttribute("themeList");
	LfUser2skin u2S = (LfUser2skin) request
			.getAttribute("u2S");


	String skin = session.getAttribute("stlyeSkin") == null ? "default"
			: (String) session.getAttribute("stlyeSkin");
			
	@SuppressWarnings("unchecked")
	Map<String, List<LfSkin>> themeSkinMap = (Map<String,  List<LfSkin>>) request
			.getAttribute("themeSkinMap");
	
	String userFrame = "",userSkin = "";
	if(u2S != null)
	{
		userFrame = u2S.getThemecode();
		userSkin = u2S.getSkincode();
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@ include file="/common/common.jsp"%>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<jsp:include page="/common/commonfile.jsp"></jsp:include>
		<link href="<%=iPath %>/css/main.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/main.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	<style>
	.fontsel {
		font-weight: 800;
	}
	</style>
	
	<body >
	<div id="container" class="container" >
	<%-- 当前位置 --%>
	<%
		String location = ViewParams.getPositionWhitIn(MessageUtils.extractMessage("common","common_index",request), MessageUtils.extractMessage("common","common_frame2_left_3",request));
		location = location.replace("当前位置：",MessageUtils.extractMessage("common","common_location",request));
	%>
	<%=location%>
	<%-- 内容开始 --%>
	<div id="rContent" class="rContent">
		<div style="padding-left:20px;float:left;height:100%;width:722px;">
			<div style="display:block;height:14px">
			<span style="height:14px;float:left;width:10px;"></span>
			<%
			if(themeList != null && themeList.size() > 0) 
			{
				int index = 0;
				int size = themeList.size();
				for(LfTheme theme : themeList)
				{
					index ++;
					String themeName = theme.getThemename();
					if("经典".equals(themeName)){
						themeName = MessageUtils.extractMessage("common","common_classic",request);
					}else if("传统".equals(themeName)){
						themeName = MessageUtils.extractMessage("common","common_tradition",request);
					}
			%>
				<span style="height:16px;float:left;line-height:16px;background:url(<%=iPath %>/img/icon_<%=theme.getThemesrc() 
				%><%=userFrame.equals(theme.getThemecode())?"_sel":"" %>.png) left top no-repeat;text-indent:22px;cursor:pointer"
				 onclick="selTheme('<%=theme.getThemecode() %>')" class="themeSpan <%=userFrame.equals(theme.getThemecode())?"fontsel":"" %>" themeCode = "<%=theme.getThemecode() %>">
					<%=themeName %>
				</span>
			<%
					if(index<size)
					{
			%>
			<span style="height:14px;float:left;line-height:14px;border-left:0;border-top:0;border-bottom:0;width:10px;" class="div_bd"></span>
			<span style="height:14px;float:left;width:10px;"></span>
			<%			
					}
				} 
			}
			%>
				</div>
			<%
			int skinLine = 0;
			int skinCount = 0;
			if(themeSkinMap != null)
			{
				Iterator<String> ite = themeSkinMap.keySet().iterator();
				while(ite.hasNext())
				{
					skinLine = 0;
					skinCount = 0;
					String frame = ite.next();
					List<LfSkin> skinList = themeSkinMap.get(frame);
					StringBuffer sb = new StringBuffer("<div class='themeSkin' framecode='"+frame+"' style='width:100%' >");
					if(skinList != null && skinList.size() > 0)
					{
						String selclass= "";
						for(LfSkin sk : skinList)
						{
							skinCount ++;
							if(skinCount > (skinLine+1)*4)
							{
								skinLine ++;
							}
							selclass = "";
							String skcode = sk.getSkincode();
							if(userSkin.equals(skcode) && userFrame.equals(frame))
							{
								selclass = " curSkin2 div_bd title_bg";
							}

							sb.append("<div style='padding:5px;float:left'><div class='perSkin skin_").append(skcode).append(selclass).append("'  onclick='changeSkin2(\"").append(sk.getSkincode()).append("\",\"").append(frame).append("\")'>").append("<div class='skinPic' style='background:url(").append(request.getContextPath()).append("/frame/").append(frame).append("/skin/").append(skcode).append("/images/skinpreview.jpg) no-repeat;'></div>").append("<div class='uselb' style='text-align:center'>").append(sk.getSkinname()).append("</div></div></div>");
						}
					}
					sb.append("</div>");
			%>
			<div style="float:left;width:100%;height:<%=skinLine*180+220 %>px;display:<%=userFrame.equals(frame)?"block":"none" %>;padding-top:10px;" class="skinDiv" framecode="<%=frame %>">
			<%=sb.toString() %>
			<%if(!userFrame.equals(frame)){ %>
			<div style="float:left;width:100%;display:block;padding-top:20px;text-align: center;">
				<input type="button" class="btnClass5 mr23" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"  id="com_but_ok" onclick="skinSure()"/>
    			<input type="button" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" id="com_but_no"  onclick="skinNoGood()"/>
			</div>
			<%} %>
			</div>
			
			<%		
				}
			%>
			<%} %>
		</div>
	</div>
	</div>
	<script  type="text/javascript">
		var old_skin = "<%=userSkin%>";
		var sel_frame = "<%=userFrame%>";
		var old_frame = "<%=userFrame%>";
	</script>
	<script type="text/javascript" src="<%=iPath %>/js/skin.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
