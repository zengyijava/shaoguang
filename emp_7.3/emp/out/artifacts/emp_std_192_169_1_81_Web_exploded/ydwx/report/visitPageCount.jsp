<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.netnews.vo.PageNameVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	
	@SuppressWarnings("unchecked")
	List<PageNameVo> beans = (List<PageNameVo>)request.getAttribute("visitPageName");
	String busId = (String)session.getAttribute("busId");
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
	<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/visitPagePhone.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="ydwx_visitPageCount">
	<div id="container" class="container">
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_ymxq",request)) %>
	<div style="display:none" id="hiddenValueDiv"></div>
	      <div class="ydwx_hiddenValueDiv_sub">
			<div class="buttons ydwx_buttons">
				<a id="exportCondition" onclick="javascript:importExcel();"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'></emp:message></a>
			</div>
			<span id="backgo" class="right mr10" "display:inline;" onclick="javascript:back()"><emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message></span>
		  </div>
		  <form name="pageForm" action="<%=path%>/wx_visitreport.htm?method=getVisitQueList&type=1&busId=<%=busId%>" method="post" id="pageForm">	
	        <div class="rContent ydwx_rContent">
	         <table id="content">
	                      <thead>
					      <tr>
	                      <th ><emp:message key='ydwx_wxcxtj_fwtj_ymmch' defVal='页面名称' fileName='ydwx'></emp:message></th>
	                      <th ><emp:message key='ydwx_wxcxtj_fwtj_fwrsh' defVal='访问人数' fileName='ydwx'></emp:message></th>
	                      <th ><emp:message key='ydwx_wxcxtj_fwtj_fwcsh' defVal='访问次数' fileName='ydwx'></emp:message></th><%--
	                      <th >详情</th>
	                    --%></tr>
	                    </thead>
	                    <tbody id="tbody" align="center">
	                    <%
	                   
	                    if(beans!=null && beans.size()>0)
	                    {
	                    	for(PageNameVo bean:beans)
	                    	{
	                    %>
	                    <tr>
	                    <td><%=bean.getPageName()%></td>
	                    <td><%=bean.getVisitpep() %></td>
	                    <td><%=bean.getVisitcount()%></td><%--
	                    <td>
	                    	<%if(bean.getVisitcount()>0){ %>
	                    		<a href="###" onclick="seeVisitInfo('<%=bean.getId()%>')">查看</a>
	                    	<%}%>
	                    	
	                    </td>
	                    --%></tr>
	                    <%		
	                    	}
	                    }else{
	                    %>
	                    <tr><td  colspan="6"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
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
	</div>
	</form>
	</body>	
      	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/ydwx/ueditor/dialogs/jquery.form.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
	<script type="text/javascript">
		 function getpagesize(){
		      var number=$("#pagesize").val();
		      return number;
		 }
  
	    function getpageno(){
	      var pages=$("#pages").val();
	      return pages;
	  	}
  
     	$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
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
   		location.href="<%=path%>/wx_visitreport.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&type=<%=request.getParameter("type") %>";
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

			   //异步导出文件
				$.ajax({
					type: "POST",
					url: "wx_visitreport.htm?method=visitPagesExportOut",
					data: {
					lguserid:lguserid,tableName:'<%=request.getParameter("tableName")%>',
					busId:'<%=busId%>'
					},
					beforeSend: function(){
						page_loading();
					},
	                complete:function () {
						page_complete();
	                },
					success: function(result){
						if(result=='true'){
							download_href("wx_visitreport.htm?method=downloadFile&down_session=visitPagesExportOut");
	                    }else{
	                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
	                    }
		   			}
				});
			   
			  // window.location.href="<%=path %>/wx_visitreport.htm?method=visitPagesExportOut&lguserid="+lguserid+"&tableName="+'<%=request.getParameter("tableName")%>'+"&busId=<%=busId%>";
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
<script type="text/javascript" src="<%=iPath %>/js/visitPageCount.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
</div>
</html>
		
		
		