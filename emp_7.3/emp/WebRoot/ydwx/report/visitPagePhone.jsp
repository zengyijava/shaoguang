<%@ page language="java" import="com.montnets.emp.common.constant.ViewParams" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.netnews.vo.VisitTrustVo"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> phoneMap=(Map<String,String>)request.getAttribute("phoneMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	
	@SuppressWarnings("unchecked")
	List<VisitTrustVo> beans = (List<VisitTrustVo>)request.getAttribute("visittrustView");
	String busId = (String)request.getAttribute("busId");
	String recvtime = request.getParameter("recvtime");
	String sendtime = request.getParameter("sendtime");
	String taskid = request.getParameter("taskid");
	String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	    <link rel="stylesheet" type="text/css" href="<%=iPath %>/css/visitPagePhone.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	    <link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>
<body id="ydwx_visitPagePhone">
		<div id="container" class="container">
		<%
			if(CstlyeSkin.contains("frame4.0")){
		%>
			<input id='hasBeenBind' value='1' type='hidden'/>
		<%
			}
		 %>
		<%=ViewParams.getPosition(empLangName,menuCode, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_hmxq",request)) %>
		<div style="display:none" id="hiddenValueDiv"></div>
		        <div id="rContent" class="rContent">
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<a id="exportCondition" onclick="javascript:importExcel();"><emp:message key='ydwx_common_btn_daochu' defVal='??????' fileName='ydwx'></emp:message></a>
					<span id="backgo" class="right ydwx_backgo" onclick="javascript:back()"><emp:message key='ydwx_common_btn_fanhui' defVal='??????' fileName='ydwx'></emp:message></span>
				</div>
		
		        <form name="pageForm" action="<%=path%>/wx_visitreport.htm?method=visittrustView&type=<%=request.getParameter("type") %>&busId=<%=busId %>&taskid=<%=taskid!=null?taskid:"" %>" method="post" id="pageForm">
		        
		        		<div id="condition">
					<table>
						<tr>
							<td>
								<span><emp:message key='ydwx_wxcxtj_hftj_shoujihaomas' defVal='???????????????' fileName='ydwx'></emp:message></span>
							</td>
							<td>
								<input type="text" id="phone" name="phone" value="<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>" class="ydwx_phone">
							</td>
							<td>
								<span><emp:message key='ydwx_wxcxtj_hftj_xingmings' defVal='?????????' fileName='ydwx'></emp:message></span>
							</td>
							<td>
								<input id="name"  name = "name" type="text" value="<%=request.getParameter("name")==null?"":request.getParameter("name") %>" class="ydwx_name">
							</td>
							<td>
								<span><emp:message key='ydwx_wxcxtj_fwtj_ymmchs' defVal='???????????????' fileName='ydwx'></emp:message></span>
							</td>
							<td>
								<input id="pagename"  name = "pagename" type="text" value="<%=request.getParameter("pagename")==null?"":request.getParameter("pagename") %>" class="ydwx_pagename">
							</td>
							<td  class="tdSer">
								<center><a id="search"></a></center>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="ydwx_common_time_youxiaoshijians" defVal="???????????????" fileName="ydwx"></emp:message></span>
							</td>
							<td>
								<input type="text" readonly="readonly" class="Wdate ydwx_sendtime" onclick="stime()" 
													value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
														 <%--onchange="onblourSendTime('end')" --%>>
							</td>
							<td>
								<span><emp:message key="ydwx_common_time_zhi" defVal="??????" fileName="ydwx"></emp:message></span>
							</td>
							<td>
								<input type="text" readonly="readonly" class="Wdate ydwx_recvtime"  onclick="rtime()"
													value="<%=recvtime==null?"":recvtime %>" 
													 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
							
						</tr>
						
					</table>
				
				</div>
		         <table id="content" >
		                      <thead>
						      <tr>
								<th>
									<emp:message key="ydwx_common_time_fangwenshijian" defVal="????????????" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key='ydwx_wxcxtj_hftj_shoujihaoma' defVal='????????????' fileName='ydwx'></emp:message>
								</th>
								<th>
									<emp:message key='ydwx_wxcxtj_hftj_xingming' defVal='??????' fileName='ydwx'></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxgl_wxbhs" defVal="????????????" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxgl_wxmcs" defVal="????????????" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key='ydwx_wxcxtj_fwtj_ymmch' defVal='????????????' fileName='ydwx'></emp:message>
								</th>
		
							</tr>
		                    </thead>
		                    <tbody id="tbody" align="center">
		                    <%
		                   
		                    if(beans!=null && beans.size()>0)
		                    {
		                    	for(VisitTrustVo bean:beans)
		                    	{
		                    %>
		                    <tr>
			                    <td><%=bean.getHistorytime()!=null?bean.getHistorytime().toString().substring(0,bean.getHistorytime().toString().length()-2):"" %></td>
			                    <td><%=bean.getPhone()%></td>
			                    <td><%=phoneMap.get(bean.getPhone())!=null?phoneMap.get(bean.getPhone()):"" %></td>
			                    <td><%=bean.getNetid() %></td>
			                    <td><%=bean.getNetName() %></td>
			                    <td><%=bean.getPagename() %></td>
		                    </tr>
		                    <%		
		                    	}
		                    }else{
		                    %>
		                    <tr><td  colspan="6"><emp:message key="common_norecord" defVal="?????????" fileName="common"></emp:message></td></tr>
		                    <%
		                    }
		                    %>
		                    </tbody>
					<tfoot>
						<tr>
							<td colspan="6">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
				</form>
		</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/ydwx/ueditor/dialogs/jquery.form.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
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
			$('#search').click(function(){submitForm();});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$("#searchIcon").click(function() {
			$("#condition").toggle();
			if($(this).attr("checked")){
			     $("#toggle").html(getJsLocaleMessage("ydwx","ydwx_common_chxtj"));
			}
			else{
			    $("#toggle").html(getJsLocaleMessage("ydwx","ydwx_common_chxtj"));
			}
		});
	});


   function back()
   {
	   var lguserid='<%=request.getAttribute("lguserid")%>';
		var lgcorpcode='<%=request.getAttribute("lgcorpcode")%>';
   	location.href="wx_visitreport.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&type=<%=request.getParameter("type") %>";
   }

   function importExcel()
   {
	   if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd")))
	   {
		   <%
		   if(beans!=null && beans.size()>0)
		   {
		   %>
			   var lguserid = $("#lguserid").val();
			   var phone ='<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>';
			   var begintime ='<%=sendtime==null?"":sendtime %>';
			   var endtime ='<%=recvtime==null?"":recvtime %>';
			   var taskid='<%=taskid==null?"":taskid %>';
			   
			   //??????????????????
				$.ajax({
					type: "POST",
					url: "wx_visitreport.htm?method=replyPagesExportOut",
					data: {
					taskid:taskid,phone:phone,
					begintime:begintime,endtime:endtime,
					lguserid:lguserid,tableName:'<%=request.getParameter("tableName")%>',
					busId:${busId}
					},
					beforeSend: function(){
						page_loading();
					},
	                complete:function () {
						page_complete();
	                },
					success: function(result){
						if(result=='true'){
							download_href("wx_visitreport.htm?method=downloadFile&down_session=replyPagesExportOut");
	                    }else{
	                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
	                    }
		   			}
				});
			   
			   //window.location.href="<%=path %>/wx_visitreport.htm?method=replyPagesExportOut&taskid="+taskid+"&phone="+phone+"&begintime="+begintime+"&endtime="+endtime+"&lguserid="+lguserid+"&tableName="+'<%=request.getParameter("tableName")%>'+"&busId=${busId}";
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
<script type="text/javascript" src="<%=iPath %>/js/visitPagePhone.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
</body>
</html>
		
		
		