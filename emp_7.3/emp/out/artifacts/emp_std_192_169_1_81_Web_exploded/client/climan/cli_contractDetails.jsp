<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("bookType");
	menuCode = menuCode==null?"0-0-0":menuCode;
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String lguserid = (String)request.getAttribute("lguserid");
	String guid = (String)request.getAttribute("guid");
	
	List<DynaBean> contractList = (List<DynaBean>)request.getAttribute("contractList");
	Map taocanMap = (Map)request.getAttribute("taocanMap");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style type="text/css">
			#cli_contractDetails .client_display_none,.buttons{
				display: none;
			}
		</style>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="cli_contractDetails">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm" action="cli_addrBook.htm?method=getContractDetail" method="post" id="pageForm">
			<div class="client_display_none" id="hiddenValueDiv"></div>
			<input id="lguserid" name="lguserid" type="hidden" value="<%=lguserid %>" />
			<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=lgcorpcode %>" />
			<input id="guid" name="guid" type="hidden" value="<%=guid %>" />
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						
					</div>
					<div id="condition">
					</div>
					<table id="content">
					<thead>
					<tr>
						<th><emp:message key="client_khtxlgl_temp_text_signpackage" defVal="签约套餐" fileName="client"></emp:message></th>
						<th><emp:message key="client_khtxlgl_temp_text_signaccount" defVal="签约账号" fileName="client"></emp:message></th>
						<th><emp:message key="client_khtxlgl_temp_text_signtime" defVal="签约时间" fileName="client"></emp:message></th>
						<th><emp:message key="client_khtxlgl_temp_text_freezetime" defVal="取消签约/冻结时间" fileName="client"></emp:message></th>
						<th><emp:message key="client_khtxlgl_temp_text_signstatus" defVal="签约状态" fileName="client"></emp:message></th>
					</tr>
					</thead>
					<tbody>
					<%
					if(contractList != null && contractList.size()>0)
					{
						for (int i = 0; i < contractList.size(); i++)
						{
							DynaBean bean=contractList.get(i);
							String cont_id="";
							Object id=bean.get("contract_id");
							if(id!=null){
								Object conObject=taocanMap.get(bean.get("contract_id").toString());
								if(conObject!=null){
									cont_id=conObject.toString();
								}
							}
					%>
					<tr >
						<td><%=cont_id%></td>
						<td>
							<%
								String acctNoStr = "";
								if(null!=bean.get("acct_no") && !"".equals(bean.get("acct_no").toString())){
									String acctno = bean.get("acct_no").toString();
									if(acctno.length()>4){
										acctNoStr = "***"+acctno.substring(acctno.length()-4,acctno.length());
									}else{
										acctNoStr = "***"+acctno;
									}
								}
							%>
							<%=acctNoStr %>
						</td>
						<td>
							<%
								String dateStr = "";
								if(null!=bean.get("contract_date") && !"".equals(bean.get("contract_date").toString())){
									dateStr = df.format(bean.get("contract_date"));
								}
							%>
							<%=dateStr %>							
						</td>
						<td>
							<%
								String contimeStr = "";
								if(null!=bean.get("cancel_contime") && !"".equals(bean.get("cancel_contime").toString())){
									contimeStr = df.format(bean.get("cancel_contime"));
								}
							%>
							<%=contimeStr %>
						</td>
						<td>
							<%
								String conractState = "";
								if(null!=bean.get("contract_state") && "0".equals(bean.get("contract_state").toString())){
									conractState = MessageUtils.extractMessage("client","client_khtxlgl_temp_text_sign",request);  //"已签约";
								}else if(null!=bean.get("contract_state") && "1".equals(bean.get("contract_state").toString())){
									conractState = MessageUtils.extractMessage("client","client_khtxlgl_temp_text_cancelsign",request);  //"已取消签约";
								}else if(null!=bean.get("contract_state") && "2".equals(bean.get("contract_state").toString())){
									conractState = MessageUtils.extractMessage("client","client_khtxlgl_temp_text_freezen",request);  //"已冻结";
								}
							%>
							<%=conractState %></td>
					</tr>
			 		<%
			 			} 
					}else{
			  		%>
					<tr><td colspan="5"><emp:message key="client_common_text_norecord" defVal="无记录" fileName="client"></emp:message></td></tr>
					<%
						}
					%>
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
		
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		});
		</script>
	</body>
</html>
