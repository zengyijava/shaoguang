<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.datasource.LfDBConnect"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
 
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("datasourceConf");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/dat_datasourceConf.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dat_datasourceConf.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		

	</head>
	<body id="dat_datasourceConf" onload="show()">
		<div id="container" class="container">
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<div id="rContent" class="rContent">
			<%              		
						if(btnMap.get(menuCode+"-0")!=null)                       		
						{                        	
					%>
					<form name="pageForm" action="dat_datasourceConf.htm" method="post" id="pageForm">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" id="inheritPath" value="<%=inheritPath %>"/>
							<div class="buttons inheritPath_div"  >
							<%--	<div id="toggleDiv" >
									<img id="searchIcon" style="cursor: pointer;"  src="<%=inheritPath %>/images/toggle_collapse.png" title="展开查询条件"/>
								</div> --%>
								<%              		
								if(btnMap.get(menuCode+"-1")!=null)                       		
								{                        	
							%>
 								<a id="add" onclick="javascript:addFrame()"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>
 									<%  
								}            		
							%>
								<%-- <a id="search"></a> --%>
							</div>
							<%--  --%><div id="condition">
							
							</div>
								<table id="content">
									<thead>
									<tr>
										<%--<th>
											业务编号
										</th> --%>
										<th>
											<emp:message key="xtgl_cswh_sjypz_sjymc" defVal="数据源名称" fileName="xtgl"/>
										</th>
										<%-- <th>数据库类型</th>--%>
										<th>
											<emp:message key="xtgl_cswh_sjypz_sjkdz" defVal="数据库地址" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_cswh_sjypz_dkh" defVal="端口号" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_cswh_sjypz_yhm" defVal="用户名" fileName="xtgl"/>
										</th>
										<%--  <th>失败重连次数</th>--%>
										<%              		
											if(btnMap.get(menuCode+"-1")!=null || btnMap.get(menuCode+"-2")!=null)                       		
											{                        	
										%>
										 <th colspan="2">
											<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
										</th>
										<%
											}
										 %>
									</tr>
								</thead>
								<tbody>
								<%
									@ SuppressWarnings("unchecked")
									List<LfDBConnect> dbList=(List<LfDBConnect>)request.getAttribute("dbList");
								if(dbList!=null&&dbList.size()>0){
									for(int i=0;i<dbList.size();i++)
									{
										LfDBConnect dbConn=dbList.get(i);
								 %>
									<tr>
										<%--<td>
											<%=dbConn.getDbId() %>
										</td> --%>
										<td class="textalign" >
											<label><xmp><%=dbConn.getDbconName() %></xmp></label>
										</td>
										<%-- <td><s:property value="dbType"/></td> --%>
										<td>
											<%=dbConn.getDbconIP() %>
										</td>
										<td>
											<%=dbConn.getPort() %>
										</td>
										<td>
											<xmp><%=dbConn.getDbUser() %></xmp>
										</td>
										<%              		
											if(btnMap.get(menuCode+"-3")!=null)                       		
											{                        	
										%>
										<td>
											<a href="javascript:eidtFrame('<%=dbConn.getDbId() %>')" ><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
										</td>
										<%      
											}        		
											if(btnMap.get(menuCode+"-2")!=null )                       		
											{                        	
										%>
										<td>
										    <input type="hidden" name="dbconName" id="dbconName" value="<%=dbConn.getDbconName().replace(">","&gt;").replace("<","&lt;") %>"/>
											<a href="javascript:deleteSelDBConn(<%=dbConn.getDbId() %>)"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
										</td> 
										<%
											}
										 %>
									</tr>
									 <%} }else{%>
									 <tr><Td colspan="11"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></Td></tr><%} %>
									</tbody>
							<tfoot>
							<tr>
								<td colspan="10">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
							</table>
							</form>
							<%
						 
						}
					%>
						
			</div>
			<div id="addFrame" title="<emp:message key='xtgl_cswh_sjypz_xjsjy' defVal='新建数据源' fileName='xtgl'/>" <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='addFrame_1'");}else{out.print("class='addFrame_2'");}%> >
			<center>
				<iframe id="addDataSource" name="addDataSource"  <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='addDataSource_1'");}else{out.print("class='addDataSource_2'");}%> marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</center>
			</div>
			<div id="editFrame" title="<emp:message key='xtgl_cswh_sjypz_xgsjy' defVal='修改数据源' fileName='xtgl'/>"  <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='editFrame_1'");}else{out.print("class='editFrame_2'");}%>>
			<center>
				<iframe id="editDataSource" name="editDataSource" <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='editDataSource_1'");}else{out.print("class='editDataSource_2'");}%> marginwidth="0" scrolling="no" frameborder="no"></iframe>
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
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<%--
	<script type="text/javascript" src="<%=commonPath %>/scripts/sysuser.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>	
	 --%>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<%-- 为了解决ie8空白双击导致浏览器崩溃，增加日期控件的引用，可解决这问题 --%>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=iPath %>/js/outSystemManager.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		getLoginInfo("#hiddenValueDiv");
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
			}, function() {
			$(this).removeClass("hoverColor");
		});
		$('#search').click(function(){submitForm();});

		$('#addFrame').dialog({
			autoOpen: false,
			height: 450,
			width:400,
			modal:true
		});
		$('#editFrame').dialog({
			autoOpen: false,
			height: 450,
			width:400,
			modal:true
		});
	});
	
     function show(){
		<%String result = (String) session.getAttribute("result1");
			if (result != null && result.equals("1"))
			{%>
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_73"));
		<%} else if (result != null && result.equals("2"))
			{%>
			$(window.parent.document).find("#editFrame").dialog("close");
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_74"));
		<%} else if (result != null && result.equals("-1"))
			{%>		
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_75"));
		<%} else if (result != null && result.equals("0"))
			{%>
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_76"));	
		<%}
			session.removeAttribute("result1");
			 if (result != null)
		    {%>
			     window.parent.location.href="dat_datasourceConf.htm?method=find&lgcorpcode="+$(window.parent.document).find("#lgcorpcode").val();
		    <%  }%>
	};	
	</script>
	</body>
</html>
