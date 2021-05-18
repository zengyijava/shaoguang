<%@ page language="java" import="com.montnets.emp.common.constant.ViewParams" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.netnews.vo.WxDataBlVo"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path=request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String title = (String)request.getAttribute("title");
String busId = request.getParameter("busId");
String did = request.getParameter("did");
String mtid=request.getParameter("mtid");
String jspcount=(String)request.getAttribute("jspcount");
String busname=(String)request.getAttribute("busname");
String senddate = (String)request.getAttribute("senddate");
String hdcontent=(String)request.getAttribute("hdcontent");
String hdname = (String)request.getAttribute("hdname");
String rTitle = (String)request.getAttribute("rTitle");
String menuCode = titleMap.get(rTitle);
String langName = (String)session.getAttribute("emp_lang");



@SuppressWarnings("unchecked")
List<WxDataBlVo> beans  = (List<WxDataBlVo>)request.getAttribute("replyPageList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<style>
		a {
			text-decoration: none;
		}
		.table-flow{font-size:12px;line-height: 20px; margin-left: 10px;}
		table.table-flow  tr > td:nth-child(1){
			width: 80px;
		}
	</style>
<%if(StaticValue.ZH_HK.equals(empLangName)){%>
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<style type="text/css">
	table.table-flow  tr > td:nth-child(1){
		width: 170px;
		padding-right: 10px;
	}
</style>
<%}%>
	<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/replyPageCount.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>
<body id="ydwx_netReplyDetailCount">
	<div id="container" class="container">
	<%=ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_hftj_tjjg",request)) %>
	<div style="display:none" id="hiddenValueDiv"></div>
			<div class="rContent ydwx_rContent">
	        <form name="pageForm" action="<%=path%>/wx_count.htm?method=getTjDetailQueList&pageno=1&busId=<%=busId %>&did=<%=did %>&mtid=<%=mtid %>&senddate=<%=senddate %>&jspcount=<%=jspcount %>" method="post" id="pageForm">
	        <div class="div_bg ydwx_table_flow_dv">
					<table class="table-flow">
						<tr>
							<td align="left" colspan="2" >
								<b><emp:message key='ydwx_wxcxtj_hftj_jbxxs' defVal='基本信息：' fileName='ydwx'></emp:message></b>
							</td>
						</tr>
						<tr>
							<td>
								<b><emp:message key="ydwx_wxcxtj_hftj_wxzhts" defVal="网讯主题：" fileName="ydwx"></emp:message></b>
							</td>
							<td><%=title==null?"":title %></td>
						</tr>
						<tr>
							<td>
								<b><emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称：" fileName="ydwx"></emp:message></b>
							</td>
							<td><%=busname %></td>
						</tr>
						<tr>
							<td>
								<b><emp:message key="ydwx_wxcxtj_hftj_hdmchs" defVal="互动名称：" fileName="ydwx"></emp:message></b>
							</td>
							<td><%=hdname %></td>
						</tr>
						<tr>
							<td>
								<b><emp:message key="ydwx_wxcxtj_hftj_hdnrs" defVal="互动内容：" fileName="ydwx"></emp:message></b>
							</td>
							<td><%=hdcontent %></td>
							<td></td>
						</tr>
						<tr>
							<td>
								<b><emp:message key="ydwx_common_time_fasongshijians" defVal="发送时间：" fileName="ydwx"></emp:message></b>
							</td>
							<td><%=senddate==null?"":senddate.substring(0,senddate.lastIndexOf(".")) %></td>
							<td></td>
						</tr>
						<tr>
							<td>
								<b><emp:message key="ydwx_wxcxtj_hftj_jshrshs" defVal="接收人数：" fileName="ydwx"></emp:message></b>
							</td>
							<td><%=jspcount %></td>
							<td></td>
						</tr>
					</table>
				</div>
			<br>
			<div class="buttons ydwx_buttons">
				<a id="exportCondition" onclick="javascript:importExcel();"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'></emp:message></a>
			</div>
			<span id="backgo" class="right"  onclick="javascript:back()"><emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message></span>
	        <table id="content" class="ydwx_content">
	                      <thead>
					      <tr>
	                      <th ><emp:message key="ydwx_wxcxtj_hftj_xuanxiangs" defVal="选项" fileName="ydwx"></emp:message></th>
	                      <th ><emp:message key="ydwx_wxcxtj_hftj_hfrshbl" defVal="回复人数(比例)" fileName="ydwx"></emp:message></th>
	                      <th ><emp:message key="ydwx_wxcxtj_hftj_hfcshbl" defVal="回复次数(比例)" fileName="ydwx"></emp:message></th>
	                    </tr>
	                    </thead>
	                    <tbody id="tbody" align="center">
	                    <%
	                   
	                    if(beans!=null && beans.size()>0)
	                    {
	                    	for(WxDataBlVo bean:beans)
	                    	{
	                    %>
	                    <tr>
	                    <td><%=bean.getSname()!=null?bean.getSname():"0" %></td>
	                    <td><%=bean.getHpcount()!=null?bean.getHpcount():"0" %>(<%=bean.getHpbl() %>)</td>
	                    <td><%=bean.getHcount() %>(<%=bean.getHbl() %>)</td>
	                    </tr>
	                    <%		
	                    	}
	                    }else{
	                    %>
	                    <tr><td  colspan="4"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
	                    <%
	                    }
	                    %>
	                    </tbody>
				<tfoot>
				<tr>
				<td colspan="4">
				<div id="pageInfo"></div>
				</td>
				</tfoot>
			</table>
			<input id="path" name="path" type="hidden" value="<%=path %>" /> 
			</form>
	</div>
	   	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/ydwx/ueditor/dialogs/jquery.form.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<script type="text/javascript">
	  
	  
	  $(document).ready(function() {
	  $('#search').click(function(){submitForm();});
		getLoginInfo("#hiddenValueDiv");
	 });
	
	var lguserid='<%=request.getAttribute("lguserid")%>';
	var lgcorpcode='<%=request.getAttribute("lgcorpcode")%>';
	
	 function importExcel()
	 {
		if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd")))
		{
			   <%
			   if(beans!=null && beans.size()>0)
			   {
			   %>
				$.ajax({
					type: "POST",
					url: "wx_count.htm?method=getTjDQueExportOut",
					data: {
					lguserid:lguserid,
					busId:'<%=busId%>',
					mtid:'<%=mtid%>',
					did:'<%=did%>'
					},
					beforeSend: function(){
						page_loading();
					},
	                complete:function () {
						page_complete();
	                },
					success: function(result){
	                	if(result=='true'){
	                		download_href("wx_count.htm?method=downloadFile&down_session=getTjDQueExportOut");
	                    }else{
	                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
	                    }
		   			}
				});
				  // window.location.href="<%=path %>/wx_count.htm?method=getTjDQueExportOut&lguserid="+lguserid+"&busId=<%=busId %>&did=<%=did %>&mtid=<%=mtid %>";
			   <%  
			   }else
			   {
				%>
				alert(getJsLocaleMessage("ydwx","ydwx_common_wshjdch"));
				<%	   
			   }
			   %>
		  } 
	
	   }
	</script>
	<script type="text/javascript" src="<%=iPath %>/js/netReplyDetailCount.js"></script>
	</div>
	</body>
</html>
		
		