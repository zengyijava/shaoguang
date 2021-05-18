<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.entity.pasroute.GtPortUsed"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%

	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 

	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("corpSp");
	menuCode = menuCode==null?"0-0-0":menuCode;

	@ SuppressWarnings("unchecked")
	List<LfCorp> lbList = (List<LfCorp>)request.getAttribute("lbList");
	@ SuppressWarnings("unchecked")
	List<GtPortUsed> gpList = (List<GtPortUsed>)request.getAttribute("gpList");
	
	String SMSACCOUNT = StaticValue.SMSACCOUNT;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_addMmsBindSp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cor_addMmsBindSp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="cor_addMmsBindSp">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"新建绑定关系") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("xtgl","xtgl_qygl_xjbdgx",request)) %>
			
			<form name="pageForm" action="cor_mmsBindSp.htm" method="post" id="pageForm">
			<input type="hidden" id="smsaccount" name="smsaccount" value="<%=SMSACCOUNT!=null?SMSACCOUNT:"" %>"/>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2" >
			<div class="titletop">
				<table class="titletop_table">
					<tr>
						<td class="titletop_td">
							<emp:message key="xtgl_qygl_xjbdgx" defVal="新建绑定关系" fileName="xtgl"></emp:message>
						</td>
						<td align="right">
							<span class="titletop_font fhsyj_span"  onclick="javascript:doCancel();">&larr;&nbsp;<emp:message key="xtgl_qygl_fhsyj" defVal="返回上一级" fileName="xtgl"></emp:message></span>
						</td>
					</tr>
				</table>
			</div>
			
			<div id="table_input" class="table_input">
				<table id="sysTable" class="sysTable" >
					<tr>
						<td class="widthTD"><emp:message key="xtgl_qygl_qymc_mh" defVal="企业名称：" fileName="xtgl"></emp:message></td>
						<td>
							<select name="corpCode" id="corpCode"  class="input_bd corpCode">
								<option value=''>
									<emp:message key="xtgl_qygl_qxz" defVal="请选择" fileName="xtgl"></emp:message>
								</option>
								<%
								if (lbList != null && lbList.size() > 0)
								{
									for (LfCorp lbm : lbList)
									{
								%>
								<option value='<%=lbm.getCorpCode() %>'>
									[<%=lbm.getCorpCode() %>]<%=lbm.getCorpName() %>
								</option>
								<%		
									}
								}
								%>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="fengexian">
							<hr class="fengebj"/>
						</td>
					</tr>
					<tr>
						<%--<td><%=SMSACCOUNT %>：</td>
						--%><td><emp:message key="xtgl_qygl_spzh_mh" defVal="SP账号：" fileName="xtgl"></emp:message></td>
						<td>
							<div class="t1_div">
								<table id="t1" class="t1">
								<% 
									if(gpList != null && gpList.size() > 0)
									{
										for (GtPortUsed gpu: gpList)
										{
											%>
											<tr>
												<td class="GtPortUsed_td">
													<input name="mmsUsers" type="checkbox" value='<%=gpu.getUserId() %>' /><%=gpu.getUserId() %>
													
												</td>
											</tr>
											<%
										}
									}
								%>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="fengexian">
							<hr class="fengebj"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="right" class="btnOK_td">
								<input type="button" id="btnOK" value="<emp:message key="xtgl_qygl_qd" defVal="确定" fileName="xtgl"></emp:message>" onclick="addMmsAccBind()" class="btnClass5"/>
								<input name="" type="button"  onclick="javascript:location.href='cor_mmsBindSp.htm'" value="<emp:message key="xtgl_qygl_fh" defVal="返回" fileName="xtgl"></emp:message>" class="btnClass6"/>
								<br />
						</td>
					</tr>
				</table>
			</div>
			
			<%-- <div id="detail_Info" style="padding:5px">
				<table id="totable">
				<thead>
					<tr>
						<td width="30%" height="29px" align="right">
							企业名称：
						</td>
						<td width="70%">
						<select name="corpCode" id="corpCode" style="width:250px;">
							<option value=''>
								请选择
							</option>
							<%
							if (lbList != null && lbList.size() > 0)
							{
								for (LfCorp lbm : lbList)
								{
							%>
						<option value='<%=lbm.getCorpCode() %>'>
								[<%=lbm.getCorpCode() %>]<%=lbm.getCorpName() %>
						</option>
							<%		
								}
							}
							%>
						</select>
						</td>
					</tr>
					
					<tr>
						<td style="vertical-align:top;" align="right">
							<%=SMSACCOUNT %>：
						</td>
						<td>
						<div style="display:block;width:250px;height:150px;overflow: auto;border-right: 1px #82baf1 solid;">
							<table id="t1" style="margin-left:3px;width:227px;border:none;">
								<% 
									if(gpList != null && gpList.size() > 0)
									{
										for (GtPortUsed gpu: gpList)
										{
											%>
											<tr>
												<td height="20px">
													<input name="mmsUsers" type="checkbox" value='<%=gpu.getUserId() %>' /><%=gpu.getUserId() %>
													
												</td>
											</tr>
											<%
										}
									}
								%>
									
							</table>
						</div>
						</td>
					</tr>
					
					<tr id="btn">
						<td colspan='2' align="center" height="30px;">
							<input type="button" id="btnOK" value="确定" onclick="addMmsAccBind()" class="btnClass1"/>
							<input type="button" value="返回" onclick="javascript:doCancel();" class="btnClass1"/>
						</td>
					</tr>
					</thead>
				</table>
			</div>--%>
			
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsBindSp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
