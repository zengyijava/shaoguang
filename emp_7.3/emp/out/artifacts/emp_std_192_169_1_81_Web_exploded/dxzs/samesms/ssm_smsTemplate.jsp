<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
String dsflag = request.getParameter("dsflag");
String tmMsg = request.getParameter("tmMsg");
String tmName = request.getParameter("tmName");
@ SuppressWarnings("unchecked")
List<LfTemplate> temList = (List<LfTemplate>) request.getAttribute("temList");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

String lguserid = request.getParameter("lguserid");
//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<style type="text/css">
			.smsTemplate .rContent.dxzs_div{
				padding:5px;
			}
			.smsTemplate .dxzs_tmName{
				width:157px;
			}
			.smsTemplate .dxzs_modify{
				padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;
			}
			.smsTemplate .dxzs_msg{
				width:100%;height:100%;overflow-y:auto;
			}
		</style>
	</head>
	<body class="smsTemplate">
		<div id="container" class="container">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm" action="common.htm?method=getLfTemplateBySms&lguserid=<%=request.getParameter("lguserid") %>" method="post"
					id="pageForm">
					<input type="hidden" name="dsflag" value="<%=request.getParameter("dsflag") %>"/>
					<input type="hidden" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
					
					<div class="dxzs_display_none" id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent dxzs_div">
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="dxzs_xtnrqf_title_83" defVal="模板名称" fileName="dxzs"/>：</span>
								</td>
								<td>
									<input type="text" name="tmName" class="dxzs_tmName"
										id="tmName" value="<%=null!=tmName?tmName:"" %>"  />
								</td>
								<td>
									<span><emp:message key="dxzs_xtnrqf_title_84" defVal="模板内容" fileName="dxzs"/>：</span>
								</td>
								<td>
									<input type="text" name="tmMsg" id="tmMsg"  value="<%=null!=tmMsg?tmMsg:"" %>" class="dxzs_tmName"/>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
							<th>
								<emp:message key="dxzs_xtnrqf_button_7" defVal="选择" fileName="dxzs"/>
							</th>
							<th>
								<emp:message key="dxzs_xtnrqf_title_85" defVal="模板ID" fileName="dxzs"/>
							</th>
							<th>
								<emp:message key="dxzs_xtnrqf_title_83" defVal="模板名称" fileName="dxzs"/>
							</th>
							<th>
								<emp:message key="dxzs_xtnrqf_title_84" defVal="模板内容" fileName="dxzs"/>
							</th>
							<th>
							           <emp:message key="dxzs_xtnrqf_title_86" defVal=" 创建日期" fileName="dxzs"/>
							</th>
										
									</tr>
								</thead>
								<tbody>
								<%
								if (temList != null && temList.size() > 0)
								{
									for (LfTemplate tem : temList)
									{
								%>
									<tr>
										<td>
											<input type="radio" name="checklist" value="<%=tem.getTmid() %>" />
											<xmp class="dxzs_display_none"><%=tem.getTmMsg()%></xmp>
										</td>
										<td>
											<%=tem.getTmid() %>
										</td>
										<td class="textalign" >
											<%
										 if(!"".equals(tem.getTmName())&&tem.getTmName()!=null){
										String st1 = "";
											if(tem.getTmName().length()>8)
											{
												st1 = tem.getTmName().substring(0,8)+"...";
											}else
											{
												st1 = tem.getTmName();
											}
									
										%>
											<a onclick=javascript:modify(this,1)>
								  <label class="dxzs_display_none"><xmp><%=tem.getTmName()%></xmp></label>
								  <xmp><%=st1 %></xmp>
								  </a> 					<%}else{ %>		<%} %>
										</td>
										<td class="textalign" >
										<%
										 if(!"".equals(tem.getTmMsg())&&tem.getTmMsg()!=null){
										String st = "";
											if(tem.getTmMsg().length()>15)
											{
												st = tem.getTmMsg().substring(0,15)+"...";
											}else
											{
												st = tem.getTmMsg();
											}
									
										%>
										<a onclick=javascript:modify(this,2)>
								  <label class="dxzs_display_none"><xmp><%=tem.getTmMsg()%></xmp></label>
								  <xmp><%=st %></xmp>
								  </a> 					<%}else{ %>		<%} %>
										</td>
										<td>
											<%
												if(tem.getAddtime()!=null)
												{
													out.print(df.format(tem.getAddtime()));
												}
											%>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="5"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="5">
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
		<div id="modify" title="<emp:message key='dxzs_xtnrqf_title_84' defVal='模板内容' fileName='dxzs'/>" class="dxzs_modify">
				<div id="msg" class="dxzs_msg"><xmp></xmp></div>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
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
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
		});
		function modify(t,i)
		{
			$('#modify').dialog({
				autoOpen: false,
				resizable: false,
				width:250,
			    height:200
			});
			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			if(i==1)
			{
				$('#modify').dialog('option','title',getJsLocaleMessage('dxzs','dxzs_ssend_alert_134'));
			}
			else
			{
				$('#modify').dialog('option','title',getJsLocaleMessage('dxzs','dxzs_ssend_alert_135'));
			}
			$('#modify').dialog('open');
		}
		</script>
	</body>
</html>
