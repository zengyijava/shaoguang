<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	///emp_sta/rms/meditor
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map


		
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String titlePath = (String)request.getAttribute("titlePath");
	// String menuCode = "1520-1700";
	String menuCode = titleMap.get(titlePath);
	
	String actionPath = (String)request.getAttribute("actionPath");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

	String findResult= (String)request.getAttribute("findresult");
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String httpUrl = StaticValue.getFileServerViewurl();
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");

	@ SuppressWarnings("unchecked")
	List<LfCorp> corps =(List<LfCorp>) request.getAttribute("corps");
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>importTemplateDetails.html</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
        <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	    <%--<link href="<%=inheritPath %>/styles/easyui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>
		
		<style type="text/css">
			.smt_smsTaskRecord div#tooltip { 
				position:absolute;
				z-index:1000;
				max-width:435px; 
				_width:expression(this.scrollWidth > 435 ? "435px" : "auto");  
				width:auto; background:#A8CFF6; 
				border:#FEFFD4 solid 1px; 
				text-align:left; padding:6px;
			}
		</style>
	</head>

	<body id="smt_smsTaskRecord">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="path" id="path" value="<%=path %>">
			<form id="pageForm" name="pageForm" action="importTemplate.htm?method=importTempDatas" method="post" enctype="multipart/form-data">
					<div id="condition">
						<table>
							<tbody>
							<% if (StaticValue.getCORPTYPE() == 1) {%>
								<tr>
								  <td width="35%" style="text-align:right; word-break:break-all;"><emp:message key="dxzs_xtnrqf_title_12bak" defVal="企业" fileName="dxzs"/>：</td>
								   	<td>
								   		<label>
										<select id="corp" name="corp" >
											<option>请选择</option>
										<%
											if(corps != null && corps.size()>0)
											{
												for(LfCorp corp : corps)
												{
										%>
													<option value="<%=corp.getCorpCode()%>">
														<%=corp.getCorpName() %> (<%=corp.getCorpCode()%>)
													</option>
													
										<%
												}
											}	
												
										 %>
										</select>
										</label>
									</td>     
								</tr>
							<% }%>
								<tr>
									<td width="35%" style="text-align:right; word-break:break-all;">富信主题：</td>
									<td>
										<input id="zhuti" name="zhuti" type="text" >
									</td>
								</tr>
								<tr>
									<td width="35%" style="text-align:right; word-break:break-all;">文件:</td>
									<td>
										<input id="numFile" name="numFile" type="file" >
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</form>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
		</div>
    <div class="clear"></div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/rms/meditor/js/templateDetails.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function(){
		    // getLoginInfo("#smssendparams");
		    //参数是要隐藏的下拉框的div的id数组，
		    closeTreeFun(["dropMenu2","dropMenu"]);
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_77'));	
		       return;			       
		    }

		    $(".top").hide();
			
			$("#title").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
			});
	
		});
		
	</script>
	</body>
</html>
