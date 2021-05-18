<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.system.LfThiMenuControl"%>
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
	List<LfThiMenuControl> thirdMenuList = (List<LfThiMenuControl>) request
			.getAttribute("thirdMenuList");

	String aproinfo = (String) request.getAttribute("proInfo");
	Integer validday = (Integer) session.getAttribute("ValidDay");
	validday = validday == null ? 0 : validday;

	int menuCount = (Integer) request.getAttribute("menuCount");//模块菜单总数
	int perCount = 4;//每排菜单按钮数量
	perCount = perCount > menuCount ? menuCount : perCount;
	boolean isBreak = true;//最后一排菜单个数小于perCount时是否居中

	String skin = session.getAttribute("stlyeSkin") == null ? "default"
			: (String) session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@ include file="/common/common.jsp"%>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		
		<link href="<%=iPath %>/css/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script type="text/javascript">
			var iPath = <%=iPath%>;
			var aproinfo = <%=aproinfo%>;
			var validday = <%=validday%>;
    	</script>
	</head>
	<body >
	<div id="bg_top_line">&nbsp;</div>
		<input type="hidden" id="isMiddel" value="0" />
		<center>
			<div
				style="width:<%=perCount * 178 + perCount * 20 - 20%>px;padding-top:53px;">
				<%
					if (thirdMenuList != null && thirdMenuList.size() > 0) {
						int size = thirdMenuList.size();
						//String clorIndex = "";
						String priMenus = String.valueOf(thirdMenuList.get(0)
								.getPriMenu());
						String title = thirdMenuList.get(0).getTitle();
						Integer menuNum = thirdMenuList.get(0).getMenuNum();
						int index = 1;
						StringBuffer dropDownList = new StringBuffer();
						String topDropDownList = "";
						for (int i = 1; i < size; i++) {
							LfThiMenuControl menu = thirdMenuList.get(i);
							//clorIndex = String.valueOf(index);
							//clorIndex = clorIndex.equals("10")?"A":clorIndex.equals("11")?"B":clorIndex.equals("12")?"C":clorIndex;
							if (menuNum - menu.getMenuNum() == 0) {
								//priMenus+=","+String.valueOf(menu.getPriMenu());
							} else {

								dropDownList
										.append(
												"<li><a href=\"#\" onclick=\"javascript:doOpen('")
										.append(menuNum).append("','").append(title)
										.append("')\">").append(title).append(
												"</a></li>");
				%>
				<div class="menuDiv">
					<a href="javascript:doOpen('<%=menuNum%>')"
						style="background-image:url('<%=skin %>/images/index_pic/index_menu_<%=menuNum%>.jpg') ;)"><%=title%></a>
				</div>

				<%
					menuNum = menu.getMenuNum();
								priMenus = String.valueOf(menu.getPriMenu());
								title = menu.getTitle();
								if (index % perCount != 0 && index < menuCount) {
									out.print("<div class='midDiv'></div>");
								} else if ((index / perCount + 1) * perCount > menuCount
										&& isBreak) {
									isBreak = false;
									out
											.print("<div class='midDiv' style='width:"
													+ String
															.valueOf((perCount + index - menuCount) * 81.5)
													+ "px;margin-top:0px;'></div>");
								}
								index++;
							}
						}
						dropDownList.append(
								"<li><a href=\"#\" onclick=\"javascript:doOpen('")
								.append(menuNum).append("')\">").append(title).append(
										"</a></li>");
						topDropDownList = dropDownList.toString();
				%>
				<div class="menuDiv">
					<a href="javascript:doOpen('<%=menuNum%>')"
						style="background-image:url('<%=skin %>/images/index_pic/index_menu_<%=menuNum%>.jpg') ;)"><%=title%></a>
				</div>
				<div id="contents" style="display: none">
					<%=topDropDownList%>
				</div>
				<%
					}
				%>
			</div>
		</center>
		<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/index.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	</body>
</html>
