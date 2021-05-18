<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
String langName = (String)session.getAttribute("emp_lang");
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" href="<%=commonPath%>/ydwx/sendWX/css/wx_contentTemplate.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
		<script type="text/javascript">
		//页面加载，初始化相关数据
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
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[5]);
			$('#search').click(function(){submitForm();});
		});
		
		//显示模板名称详细信息
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
				$('#modify').dialog('option','title',getJsLocaleMessage("ydwx","ydwx_dtwxfs_93"));
			}
			else
			{
				$('#modify').dialog('option','title',getJsLocaleMessage("ydwx","ydwx_dtwxfs_94"));
			}
			$('#modify').dialog('open');
		}
		</script>
	</head>
	<body class="wx_contentTemplate" id="wx_contentTemplate">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%--<div class="top">
				<div id="top_right">
					<div id="top_left"></div>
					<div id="top_main">
					</div>
				</div>
			</div>
			--%><%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm" action="wx_send.htm?method=getLfTemplateBySms&lguserid=<%=request.getParameter("lguserid") %>" method="post"
					id="pageForm">
					<input type="hidden" name="dsflag" value="<%=request.getParameter("dsflag") %>"/>
					<input type="hidden" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
					
					<div id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent">
					<div id="condition">
						<table>
							<tr>
								<td class="condition_td1">
									<span><emp:message key="ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td2" defVal="模板名称：" fileName="ydwx"></emp:message></span>
								</td>
								<td class="condition_td2">
									<input type="text" name="tmName" id="tmName" value="<%=null!=tmName?tmName:"" %>"/>
								</td>
								<td class="condition_td3">
									<span><emp:message key="ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td3" defVal="模板内容：" fileName="ydwx"></emp:message></span>
								</td>
								<td class="condition_td4">
									<input type="text" name="tmMsg" id="tmMsg"  value="<%=null!=tmMsg?tmMsg:"" %>"/>
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
								<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td4" defVal="模板编码" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td5" defVal="模板名称" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td6" defVal="模板内容" fileName="ydwx"></emp:message>
							</th>
							<th>
							    <emp:message key="ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td7" defVal="创建日期" fileName="ydwx"></emp:message>
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
											<xmp class="pre-wrap"><%=tem.getTmMsg()%></xmp>
										</td>
										<td>									
											<%=tem.getTmCode()==null?"":tem.getTmCode() %>
										</td>
										<td>
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
													<label style="display:none">
														<xmp><%=tem.getTmName()%></xmp>
													</label>
										  			<xmp><%=st1 %></xmp>
										  		</a>
											<%}%>
										</td>
										<td>
										<%
										 if(!"".equals(tem.getTmMsg())&&tem.getTmMsg()!=null){
										String st = "";
										//当使用String中的replaceAll方法时，如果替换的值中包含有$符号时，在进行替换操作时会出现如下错误。
										//替换操作前后分别对替换值中的$符号进行encode和decode操作
										String replacement = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request);
									 	//replacement = replacement.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;
										String temp = tem.getTmMsg().replaceAll("#[pP]_(\\d+)#",replacement);
										//temp = temp.replaceAll("RDS_CHAR_DOLLAR", "\\$");// decode replacement;
											if(temp.length()>15)
											{
												st = temp.substring(0,15)+"...";
											}else
											{
												st = temp;
											}
									
										%>
										<a onclick='modify(this,2)'>
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
		<div id="modify" title="<emp:message key="ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td6" defVal="模板内容" fileName="ydwx"></emp:message>">
			<div id="msg"><xmp></xmp></div>
		</div>
		<div class="clear"></div>
	</body>
</html>
