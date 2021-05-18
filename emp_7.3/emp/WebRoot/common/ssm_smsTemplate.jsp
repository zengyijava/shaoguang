<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
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
%>
<html>
	<head><%@ include file="/common/common.jsp"%>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=path%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=path%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
	</head>
	<body id="comm_ssm_smsTemplate">
		<div id="container" class="container">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm" action="common.htm?method=getLfTemplateBySms&lguserid=<%=request.getParameter("lguserid") %>" method="post"
                        id="pageForm" <%=skin.contains("frame4.0")?"style=\"height: 450px\"":"style=\"max-height: 360px;overflow: scroll\""%>>
					<input type="hidden" name="dsflag" value="<%=request.getParameter("dsflag") %>"/>
					<input type="hidden" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
					
					<div style="display:none" id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent">
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="common_smsTemplate_5" defVal="模板名称：" fileName="common"></emp:message></span>
								</td>
								<td>
									<input type="text" name="tmName" 
										id="tmName" value="<%=null!=tmName?tmName:"" %>" style="width:157px !important"  />
								</td>
								<td>
									<span><emp:message key="common_smsTemplate_2" defVal="模板内容：" fileName="common"></emp:message></span>
								</td>
								<td>
									<input type="text" name="tmMsg" id="tmMsg"  value="<%=null!=tmMsg?tmMsg:"" %>" style="width:157px !important" />
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
								<emp:message key="common_option" defVal="选择" fileName="common"></emp:message>
							</th>
							<th>
								<emp:message key="common_smsTemplate_3" defVal="模板编号" fileName="common"></emp:message>
							</th>
							<th>
								<emp:message key="common_operWeb_4" defVal="模板名称" fileName="common"></emp:message>
							</th>
							<th>
								<emp:message key="common_smsTemplate_1" defVal="模板内容" fileName="common"></emp:message>
							</th>
							<th>
								<emp:message key="common_smsTemplate_6" defVal="创建日期" fileName="common"></emp:message>
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
											<xmp style="display:none" class="pre-wrap"><%=tem.getTmMsg().replaceAll("#[pP]_(\\d+)#","{#"+MessageUtils.extractMessage("common","common_parameter",request)+"$1#}")%></xmp>
										</td>
										<td>
											<%=tem.getTmCode() %>
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
											<a onclick="javascript:modify(this,1)">
								  <label style="display:none"><xmp><%=tem.getTmName()%></xmp></label>
								  <xmp><%=st1 %></xmp>
								  </a> 					<%}else{ %>		<%} %>
										</td>
										<td class="textalign" >
										<%
										 if(!"".equals(tem.getTmMsg())&&tem.getTmMsg()!=null){
											String st = "";
											String temp = tem.getTmMsg().replaceAll("#[pP]_(\\d+)#","{#"+ MessageUtils.extractMessage("common","common_parameter",request)+"$1#}");
											if(temp.length()>15)
											{
												st = temp.substring(0,15)+"...";
											}else
											{
												st = temp;
											}
										%>
										<a onclick="javascript:modify(this,2)">
								  <label style="display:none"><xmp class="pre-wrap"><%=temp%></xmp></label>
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
									<tr><td colspan="5"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
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
		<div id="modify" title="<emp:message key="common_smsTemplate_1" defVal="模板内容" fileName="common"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		var total=<%=pageInfo.getTotalPage()%>;
		var pageIndex=<%=pageInfo.getPageIndex()%>;
		var pageSize=<%=pageInfo.getPageSize()%>;
		var totalRec=<%=pageInfo.getTotalRec()%>;
		
		//隐藏页数选择
		window.onload=function(){
			$(".page_select").hide();
		};
		</script>
		<script type="text/javascript" src="<%=path%>/common/commonJs/ssm_smsTemplate.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>
