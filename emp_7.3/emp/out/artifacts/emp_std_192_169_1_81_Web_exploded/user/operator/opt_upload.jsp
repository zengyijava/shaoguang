<%@page import="com.montnets.emp.sysuser.bean.OptUpload"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.montnets.emp.biztype.vo.LfBusManagerVo"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 
           uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String langName = (String) session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,
			inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String skin = session.getAttribute("stlyeSkin") == null
			? "default"
			: (String) session.getAttribute("stlyeSkin");
    OptUpload opt=(OptUpload)request.getAttribute("logData");
   Map<Integer,String> erroMap=(Map<Integer,String>)request.getAttribute("errMap");
   String result = (String)request.getAttribute("jsresult");
   @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("department");
   String bitchImport = MessageUtils.extractMessage("user","user_xtgl_czygl_text_200",request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<title></title>
<%@include file="/common/common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet"
	href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>"
	type="text/css">
<link
	href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		
<%
	if (StaticValue.ZH_HK.equals(langName)) {
%>
<link rel="stylesheet" type="text/css"
	href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet" type="text/css"
	href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
<%
	}
%>
<link rel="stylesheet" type="text/css"
	href="<%=iPath%>/css/optUpload.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet" type="text/css"
	href="<%=skin%>/xtgl.css?V=<%=StaticValue.getJspImpVersion()%>" />
	
	<%if(result!=null&&!"".equals(result)){ %>
	<script type="text/javascript">
	alert("<%=result%>");
</script>
<%} %>
</head>

<body id="bit_busType">
	<div id="container" class="container">
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,bitchImport)%>
	 
		<!-- 
			<form name="pageForm" action="bit_busType.htm" method="post" id="pageForm">
			<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<input type="hidden" value="" id="busCodeTemp" />
			<input type="hidden" value="" id="menuCodeTemp" /> -->

		<div id="rContent" class="rContent">
			<div class="buttons">
				<div id="excelDiv">
					<span class="daoru_text"><p>导入机构（用excel表格快速导入机构数据）</p> </span>
				</div>

				<a class="addNoti" id="downLodeTemple"
					href="javascript:download_href('<%=path%>/down.htm?filepath=user/operator/file/temp/organization_<%=langName%>.xlsx')">
					<emp:message key="user_xtgl_czygl_text_152" defVal="下载模板"
						fileName="user" /> 
						</a> 
						<a id="upload">
					<emp:message key="user_xtgl_czygl_text_137" defVal="导入"
						fileName="user" /> </a>
			</div>

			<div class="shuoming title_bg">
				<div class="shuoming_content">
					<p>
						<span><emp:message key="user_xtgl_czygl_text_154" defVal="说明：" fileName="user"></emp:message></span>
					</p>

					<p>
						<span><br> </span>
					</p>

					<p>
						<span><emp:message key="user_xtgl_czygl_text_177" defVal="1.下载机构导入模板" fileName="user"></emp:message></span>
					</p>

					<p>
						<span><br> </span>
					</p>
					<p>
						<span><emp:message key="user_xtgl_czygl_text_178" defVal="2.在【机构导入模板】工作表按规则添加机构数据" fileName="user"></emp:message></span>
					</p>

					<p>
						<span><br> </span>
					</p>
					<p>
						<span><emp:message key="user_xtgl_czygl_text_179" defVal="3.导入数据，会在导入记录中显示" fileName="user"></emp:message></span>
					</p>

					<p>
						<span><br> </span>
					</p>
					<p>
						<span><emp:message key="user_xtgl_czygl_text_180" defVal="注：一次导入最多上传个机构" fileName="user"></emp:message></span>
					</p>
				</div>
			</div>
			<div class="dr_jl">
				<div class="dr_jilu_con">
					<span class="daoru_text"><p><emp:message key="user_xtgl_czygl_text_160" defVal="导入记录" fileName="user"></emp:message></p> </span> <br>
					<table id="content">
						<thead>
							<tr>
								<th class="th1"><emp:message key="user_xtgl_czygl_text_139"
										defVal="序号" fileName="xtgl" /></th>
								<th class="th2"><emp:message key="user_xtgl_czygl_text_140"
										defVal="总数据数" fileName="xtgl" /></th>
								<th class="th3"><emp:message key="user_xtgl_czygl_text_141"
										defVal="成功数" fileName="xtgl" /></th>
								<th class="th4"><emp:message key="user_xtgl_czygl_text_142"
										defVal="失败数" fileName="xtgl" /></th>
								<th class="th5"><emp:message key="user_xtgl_czygl_text_143"
										defVal="导入时间" fileName="xtgl" /></th>
								<%-- <%if(btnMap.get(menuCode+"-3") != null || btnMap.get(menuCode+"-2") != null)  {  %>
							<th <%if(btnMap.get(menuCode+"-3") != null && btnMap.get(menuCode+"-2") != null)  {  out.print(" colspan=2");}%> class="cz_th">
								<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
							</th>
							<%} %> --%>
							</tr>
							
						</thead>
						<tbody>
						<%if(opt!=null&&opt.getTotal()>0){ %>
						<tr>
							<td >1</td>
							<td ><%=opt.getTotal() %></td>
							<td ><%=opt.getSuccess() %></td>
							<td ><%if(opt.getFail()==0){ %>
							 <%=opt.getFail() %>
							 <%}else{ %>
								<a id="erroData" ><%=opt.getFail() %></a>
								<%} %>
							</td> 
							<td ><%=opt.getTime() %></td> 
						</tr>
						<%}else{ %>
						<tr>
							<td  colspan="5"><emp:message key="employee_dxzs_title_59" defVal="无记录" fileName="employee"/></td>
						</tr>
						<%} %> 
						
						</tbody>
					</table>
					   <br />
					   <div class="btnDiv">
					     <input  class="btnClass6" type="button" value="<emp:message key='user_xtgl_czygl_text_167' defVal='返回' fileName='user'/>"onclick="javascript:goback('<%=path %>');" /> 
				</div>
				</div>
			</div>

		</div>
	</div>




	<div id="fileUploadDiv" class="bindDiv" style="display: none;">
		<form id="uploadForm" method="post" action="<%=path%>/opt_department.htm?method=organizUplode" enctype="multipart/form-data">
			<table class="addDiv_table">
				<tr class="ywmc_mh_tr">
					<td align="right" class="ywmc_mh_td"><emp:message
							key="xtgl_czygl_gjaqsz_xzwj_mh" defVal="上传文件" fileName="xtgl" />
					</td>
					<td class="busNameAdd_td">
					 <input type="file" name="file" id="uploadFile" maxlength="32"
						 />
					</td>
				</tr>
				
				<tr >
					<td colspan="2" class="addsubmit_td">
					<input type="hidden"> 
					<input id="kwsok" class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>"onclick="javascript:checkUpload()" />
					 <input id="kwsc" class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>"onclick="javascript:back()" /> <br /></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="erroDataDiv" style="display: none;">
		
			<table id="erroDataTab" border="1px">
			<thead>
					<tr class="tr1">
								<th class="ths1"><emp:message key="user_xtgl_czygl_text_162" defVal="行数" fileName="user"/></th>
								<th class="ths2"><emp:message key="user_xtgl_czygl_text_163" defVal="失败原因" fileName="user"/></th>
					</tr>
			</thead>
			<tbody>
			<c:forEach items="${errMap}" var="item">
				   <tr class="ywmc_mh_tr"> 
					<td class="ywmc_mh_td">
					
					${item.key} 
					</td>
					  <td>
					${item.value}	
					</td>   
					
				   <%-- <c:choose>
				    <c:when test="${fn:contains(item.value, ',')}">
				        <td>
					<span class="erroDisplay" title="${item.value}">${fn:substringBefore(item.value, ',')}
					....
					</span>
					</td>
				    </c:when>
				   
				    <c:otherwise>
				       <td>
				       <span class="erroDisplay" >${item.value}</span>
					</td>
				    </c:otherwise>
				</c:choose>  --%>
					
				</tr>
            </c:forEach>
            
            
	
				
				</tbody>
			</table>
		
	</div> 
	<div class="clear"></div>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
	<script language="javascript"
		src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<%-- 为了解决ie8空白双击导致浏览器崩溃，增加日期控件的引用，可解决这问题 --%>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=iPath%>/js/optUpload.js?V=<%=StaticValue.getJspImpVersion()%>"></script>

</body>
</html>
