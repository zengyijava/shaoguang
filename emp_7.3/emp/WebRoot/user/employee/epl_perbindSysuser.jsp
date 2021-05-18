<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	String bookType=(String)request.getAttribute("bookType");
	String depName = (String)request.getAttribute("depName");
	String sysName = (String)request.getAttribute("sysName");
	String depid = (String)request.getAttribute("depid");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String lguserid = (String)request.getAttribute("lguserid");
	
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <%@include file="/common/common.jsp" %>
        <title></title>
        <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css">
        <link href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css">
        <link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/epl_perbindSysuser.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    </head>
	<body id="epl_perbindSysuser">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form action="epl_permissions.htm?method=getSysuserList" method="post" name="pageForm" id="pageForm">
			<div class="user_display_none" id="hiddenValueDiv"></div>
			<input type="hidden" name="depid" id="depid" value="<%=depid==null?"":depid %>"/> 						
			<input id="lguserid" name="lguserid" type="hidden" value="<%=lguserid %>" />
			<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=lgcorpcode %>" />
			<input id="selDepId" name="selDepId" type="hidden" value="<%=request.getAttribute("selDepId") %>" />
			<input id="selDepName" name="selDepName" type="hidden" value="<%=request.getAttribute("selDepName") %>" />
			<input id="pathUrl" name="pathUrl" type="hidden" value="<%=path %>" />
			
			<div id="rContent" class="rContent">
				<div id="condition">
		  		<table>
		  			<tr>
						<td><emp:message key="employee_dxzs_title_86" defVal="当前机构：" fileName="employee"/></td>
						<td class="user_td1" colspan="3"><label id="depName"><%=request.getAttribute("selDepName") %></label></td>
						<td class="tdSer">
									<center><a id="search"></a></center>
						</td>
					</tr>
		 			<tr>
						<td><span><emp:message key="employee_dxzs_title_87" defVal="机构：" fileName="employee"/></span></td>
						<td>
						<input readonly onclick="javascript:dropSysMenu()" class="input_bd treeInput" type="text" name="depname" id="depname" value="<%=null==depName?"":depName %>"/>
								<div id="dropMenu">
									<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div><%--
									<div style="margin-top: 3px;margin-right:10px;text-align:right">
											<input type="button" value="确定" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
											<input type="button" value="清空" class="btnClass1" onclick="cleanSelect_dep3();" style=""/>
									</div>	
									--%><ul id="ztree" class="tree"></ul>
								</div>
						</td>
					 
						<td><span><emp:message key="employee_dxzs_title_88" defVal="操作员姓名：" fileName="employee"/></span></td>
						<td ><input type="text" name="sysName" id="sysName" value="<%=null==sysName?"":sysName %>" maxlength="11"/></td>
						<td></td>
		 			</tr>
		 		</table>
		 		</div>
		   		<table id="content">
					<thead>
						<tr>
						 
							<th>
								<input type="checkbox" name=checkall2 id="checkall2"/>
							</th>
						 
							<th>
								<emp:message key="employee_dxzs_title_77" defVal="登录账号" fileName="employee"/>
							</th>
							<th>
								<emp:message key="employee_dxzs_title_89" defVal="操作员姓名" fileName="employee"/>
							</th>
							
							
						</tr>
					</thead>
					<tbody>
					 
					<%
						 
						@ SuppressWarnings("unchecked")
						List<LfSysuser> sysuserList=(List<LfSysuser>)request.getAttribute("sysuserList");
						for(int g=0;g<sysuserList.size();g++)
						{
							LfSysuser lfSysuser=sysuserList.get(g);
					%>
						<tr>
							<td class="user_td2">
								<input type="checkbox" name="sysUserName" value="<%=lfSysuser.getUserId() %>"/>
							</td>
							<td class="user_td2"><%=lfSysuser.getUserName()==null?"":lfSysuser.getUserName().replace("<","&lt;").replace(">","&gt;") %></td>
							<td class="user_td2"><%=lfSysuser.getName()==null?"":lfSysuser.getName() %></td>
						</tr>
					 
					 <%} 
					  if(sysuserList.size()==0){%>
							<tr><td colspan="3" class="user_td2"  ><emp:message key="employee_dxzs_title_59" defVal="无记录" fileName="employee"/></td></tr>
					 <%
					 }
					 %>
		
					</tbody>
				 	<tfoot>
						<tr  >
							<td colspan="3">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
	<div class="clear"></div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>	
	 <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=iPath%>/js/perbindSysuser.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script>
		$(document).ready(function() {
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[10]);
			$('#search').click(function(){submitForm();});
		});
		</script>
	</body>
</html>