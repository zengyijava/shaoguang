<%@ page language="java" import="com.montnets.emp.common.constant.ViewParams" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.netnews.vo.WxDataVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
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
Map<String,String> phoneMap= (Map<String,String>)request.getAttribute("phoneMap");
String rTitle = (String)request.getAttribute("rTitle");
String menuCode = titleMap.get(rTitle);
String recvtime = request.getParameter("recvtime");
String sendtime = request.getParameter("sendtime");
String busId = request.getParameter("busId");
String did = request.getParameter("did");
String taskid = request.getParameter("taskid");
String wxquest=(String)request.getAttribute("wxquest");
@SuppressWarnings("unchecked")
List<WxDataVo> beans = (List<WxDataVo>)request.getAttribute("replyPageList");
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
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/replyPageCount.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
</head>

<body id="ydwx_replyPageCount">
<div id="container" class="container">
<%=ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("ydwx","ydwx_common_btn_xingqing",request))%>
<%
	if(CstlyeSkin.contains("frame4.0")){
%>
	<input id='hasBeenBind' value='1' type='hidden'/>
<%
	}
 %>
<div style="display:none" id="hiddenValueDiv"></div>
        <div id="rContent" class="rContent">
         <form name="pageForm" action="<%=path%>/wx_count.htm?method=getReplyQueList&pageno=1&busId=<%=busId %>&did=<%=did %>&taskid=<%=taskid%>" method="post" id="pageForm">
		<div class="buttons">
			<div id="toggleDiv">
			</div>
			<a id="exportCondition" onclick="javascript:importExcel();"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'></emp:message></a>
			<span id="backgo" class="right ydwx_backgo"  onclick="javascript:back()"><emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message></span>
		</div>
       
        <div id="condition">
			<table>
				<tr>
					<td>
						<span><emp:message key='ydwx_wxcxtj_hftj_shoujihaomas' defVal='手机号码：' fileName='ydwx'></emp:message></span>
					</td>
					<td>
						<input type="text" id="phone" name="phone" value="<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>" class="ydwx_phone">
					</td>
					<td>
						<span><emp:message key='ydwx_wxcxtj_hftj_xingmings' defVal='姓名：' fileName='ydwx'></emp:message></span>
					</td>
					<td>
						<input id="name"  name = "name" type="text" value="<%=request.getParameter("name")==null?"":request.getParameter("name") %>" class="ydwx_name">
					</td>
					<td>
						<span><emp:message key='ydwx_wxcxtj_hftj_shuifuneirongs' defVal='回复内容：' fileName='ydwx'></emp:message></span>
					</td>
					<td>
						<input id="colname"  name = "colname" type="text" value="<%=request.getParameter("colname")==null?"":request.getParameter("colname") %>" class="ydwx_colname">
					</td>
					<td  class="tdSer">
						<center><a id="search"></a></center>
					</td>
				</tr>
				<tr>
					<td>
						<span><emp:message key='ydwx_common_time_huifushijians' defVal='回复时间：' fileName='ydwx'></emp:message></span>
					</td>
					<td>
						<input type="text" readonly="readonly" class="Wdate ydwx_sendtime" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												 <%--onchange="onblourSendTime('end')" --%>>
					</td>
					<td>
						<span><emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message></span>
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
					<td></td>
				</tr>
				
			</table>
		
		</div>
		<br>
		<div class="mb10"><span><emp:message key="ydwx_wxcxtj_hftj_hdxnrs" defVal="互动项内容：" fileName="ydwx"></emp:message><font class="ydwx_font"><%=wxquest %></font></span></div>
         <table id="content">
                      <thead>
				      <tr>
                      <th ><emp:message key='ydwx_wxcxtj_hftj_shoujihaoma' defVal='手机号码' fileName='ydwx'></emp:message></th>
                      <th ><emp:message key='ydwx_wxcxtj_hftj_xingming' defVal='姓名' fileName='ydwx'></emp:message></th>
                      <th ><emp:message key='ydwx_wxcxtj_hftj_shuifuneirong' defVal='回复内容' fileName='ydwx'></emp:message></th>
                      <th ><emp:message key='ydwx_common_time_huifushijian' defVal='回复时间' fileName='ydwx'></emp:message></th>
                    </tr>
                    </thead>
                    <tbody id="tbody" align="center">
                    <%
                   
                    if(beans!=null && beans.size()>0)
                    {
                    	for(WxDataVo bean:beans)
                    	{
                    %>
                    <tr>
                    <td><%=bean.getPhone() %></td>
                    <td>
                    	<%=phoneMap.get(bean.getPhone())!=null?phoneMap.get(bean.getPhone()):"" %>
                    </td>
                    <td><%=bean.getHfcontent() %></td>
                    <%
                    //java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     %>
                    <td><%=bean.getHfdate()==null?"-":bean.getHfdate().substring(0,bean.getHfdate().length()-2) %></td>
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
		</form>
</div>
</div>
   	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/ydwx/ueditor/dialogs/jquery.form.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
  
  $(document).ready(function() {
  $('#search').click(function(){submitForm();});
	getLoginInfo("#hiddenValueDiv");
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
	$("#searchIcon").click(function() {
	$("#condition").toggle();
	if($(this).attr("checked")){
		$("#toggle").html("查询条件");
	}
	else{
		$("#toggle").html("查询条件");
			}
	});
 });

 function back()
 {
	   var lguserid='<%=request.getAttribute("lguserid")%>';
		var lgcorpcode='<%=request.getAttribute("lgcorpcode")%>';
   	location.href="<%=path%>/wx_count.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+'&skip=true';
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
			   var phone= '<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>';
			   var begintime='<%=sendtime==null?"":sendtime %>';
			   var endtime='<%=recvtime==null?"":recvtime %>';

				$.ajax({
					type: "POST",
					url: "wx_count.htm?method=replyPagesExportOut",
					data: {
					lguserid:lguserid,
					busId:'<%=busId%>',
					taskid:'<%=taskid%>',
					did:'<%=did%>',
					phone:phone,
					begintime:begintime,
					endtime:endtime
					},
					beforeSend: function(){
						page_loading();
					},
	                complete:function () {
						page_complete();
	                },
					success: function(result){
	                	if(result=='true'){
	                		download_href("wx_count.htm?method=downloadFile&down_session=replyPagesExportOut");
	                    }else{
	                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
	                    }
		   			}
				});
			   
			//window.location.href="<%=path %>/wx_count.htm?method=replyPagesExportOut&lguserid="+lguserid+"&busId=<%=busId %>&taskid=<%=taskid %>&did=<%=did %>&phone="+phone+"&begintime="+begintime+"&endtime="+endtime+"";
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
<script type="text/javascript" src="<%=iPath %>/js/replyPageCount.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
</body>
</html>