<%@page import="com.montnets.emp.sysuser.bean.OptUpload"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.montnets.emp.biztype.vo.LfBusManagerVo"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
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
			
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("sysuser");
			
	OptUpload optUpload = (OptUpload)request.getAttribute("optUpload");
	Map<Integer,String> errorMap = (Map<Integer,String>)request.getAttribute("errorMap");
	
	String result = (String)request.getAttribute("result");
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
<style type="text/css">
	
</style>
<script type="text/javascript">
	var base_result = "<%=result%>";
</script>
</head>
<body id="bit_busType">
	<div id="container" class="container">
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode, "批量导入") %>
		<!-- 
			<form name="pageForm" action="bit_busType.htm" method="post" id="pageForm">
			<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<input type="hidden" value="" id="busCodeTemp" />
			<input type="hidden" value="" id="menuCodeTemp" /> -->

		<div id="rContent" class="rContent">
			<div class="buttons">
				<div id="excelDiv">
					<span class="daoru_text"><p><emp:message key="user_xtgl_czygl_text_153" defVal="导入操作员（用EXCEL表格快速导入操作员数据）" fileName="user" /></p> </span>
				</div>
				<a id="downLodeTemple" class="addNoti" 
					href="javascript:download_href('<%=path%>/down.htm?filepath=user/operator/file/temp/opt_import_<%=langName%>.xls')">
					<emp:message key="user_xtgl_czygl_text_164" defVal="下载模板" fileName="user" /> 
				</a> 
						<a id="upload">
					<emp:message key="user_xtgl_czygl_text_165" defVal="导入" fileName="user" /> </a>
			</div>

			<div class="shuoming title_bg">
				<div class="shuoming_content">
					<p>
						<span><emp:message key="user_xtgl_czygl_text_154" defVal="说明：" fileName="user" /></span>
					</p>

					<p>
						<span><br> </span>
					</p>

					<p>
						<span>1.<emp:message key="user_xtgl_czygl_text_155" defVal="下载操作员导入模板" fileName="user" /></span>
					</p>

					<p>
						<span><br> </span>
					</p>
					<p>
						<span>2.<emp:message key="user_xtgl_czygl_text_156" defVal="在【操作员导入模板】工作表按规则添加操作员数据" fileName="user" /></span>
					</p>

					<p>
						<span><br> </span>
					</p>
					<p>
						<span>3.<emp:message key="user_xtgl_czygl_text_157" defVal="导入数据，会在导入记录中显示" fileName="user" /></span>
					</p>

					<p>
						<span><br> </span>
					</p>
					<p>
						<span>
							<emp:message key="user_xtgl_czygl_text_158" defVal="注：一次导入最多上传" fileName="user" />1000
							<emp:message key="user_xtgl_czygl_text_159" defVal="个操作员" fileName="user" />
						</span>
					</p>
				</div>
			</div>
			<div class="dr_jl">
				<div class="dr_jilu_con">
					<span class="daoru_text"><p><emp:message key="user_xtgl_czygl_text_160" defVal="导入记录" fileName="user" /></p> </span> <br>
					<table id="content">
						<thead>
							<tr>
								<th class="th1"><emp:message key="user_xtgl_czygl_text_139"
										defVal="序号" fileName="user" /></th>
								<th class="th2"><emp:message key="user_xtgl_czygl_text_140"
										defVal="总数据数" fileName="user" /></th>
								<th class="th3"><emp:message key="user_xtgl_czygl_text_141"
										defVal="成功数" fileName="user" /></th>
								<th class="th4"><emp:message key="user_xtgl_czygl_text_142"
										defVal="失败数" fileName="user" /></th>
								<th class="th5"><emp:message key="user_xtgl_czygl_text_143"
										defVal="导入时间" fileName="user" /></th>
							</tr>
						</thead>
						<tbody>
						<% if(optUpload != null && optUpload.getTotal() > 0) {%>
							<tr>
								<td>1</td>
								<td><%=optUpload.getTotal() %></td>
								<td><%=optUpload.getSuccess() %></td>
								<td>
									<%if(optUpload.getFail()==0){ %>
						 				<%=optUpload.getFail() %>
						 			<%}else{ %>
										<a id="errorData"><%=optUpload.getFail() %></a>
									<%} %>
								</td>
								<td><%=optUpload.getTime() %></td>
							</tr>
						<%} else {%>
							<tr><td colspan="5" align="center"><emp:message key="user_xtgl_czygl_text_161" defVal="无数据导入" fileName="user" /></td></tr>
						<%} %>
					</tbody>
					</table>
					<br/>
					<div class="btnDiv">
					     <input  class="btnClass6" type="button" value="<emp:message key='user_xtgl_czygl_text_167' defVal='返回' fileName='user'/>" onclick="goopt();" /> 
					</div>
				</div>
			</div>

		</div>
	</div>




	<div id="fileUploadDiv" class="bindDiv" style="display: none;">
		<form id="uploadForm" method="post"
			action="<%=path %>/opt_upload.htm?method=uploadOpt"
			enctype="multipart/form-data">
			<table class="addDiv_table">
				<tr class="ywmc_mh_tr">
					<td align="right" class="ywmc_mh_td">
						<emp:message key="user_xtgl_czygl_text_168" defVal="选择文件" fileName="user" />：
					</td>
					<td class="busNameAdd_td"><!-- <input class="filepath" maxlength="16" type="text" id="filepath" readonly> --> 
						<input type="file" name="file" id="uploadFile" maxlength="32" />
					</td>
				</tr>
				
				<tr >
					<td colspan="2" class="addsubmit_td">
					<input type="hidden"> 
					<input id="kwsok" class="btnClass5 mr23" type="button" value="<emp:message key='user_xtgl_czygl_text_36' defVal='确定' fileName='user'/>"onclick="javascript:checkUpload()" />
					<input id="kwsc" class="btnClass6" type="button" value="<emp:message key='user_xtgl_czygl_text_166' defVal='取消' fileName='user'/>"onclick="javascript:back();" /> <br/></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="erroDataDiv" style="display: none;">
		<table id="erroDataTab" border="1px">
			<thead>
				<tr class="tr1">
					<th class="ths1"><emp:message key='user_xtgl_czygl_text_162' defVal='行数' fileName='user'/></th>
					<th class="ths2"><emp:message key='user_xtgl_czygl_text_163' defVal='失败原因' fileName='user'/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${errorMap}" var="item">
					<tr class="ywmc_mh_tr"> 
						<td class="ywmc_mh_td">
							${item.key} 
						</td>
						<td>
							${item.value}	
						</td>
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
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
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
	<script type="text/javascript" src="<%=inheritPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#fileUploadDiv").dialog({
				autoOpen: false,
			    width:450,
			    height:150,
			    title:getJsLocaleMessage("user","user_xtgl_czygl_text_160"),
			    modal:true,
			    resizable:false
		 	});
	 
			$('#upload').click(function(){
				$("#fileUploadDiv").dialog("open");
			});
			
			$("#erroDataDiv").dialog({
				autoOpen: false,
			    width:480,
			    height:300,
			    //title:"失败详情",
			    title:getJsLocaleMessage("user","user_xtgl_czygl_text_159"),
			    modal:true,
			    resizable:false
			 });
			 
			$('#errorData').click(function(){
				$("#erroDataDiv").dialog("open");
			});
			
			if(base_result=="false") {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_161"));
			} else if (base_result=="maxopt") {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_162"));
			} else if (base_result=="no") {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_166"));
			}
			
			$("#erroDataDiv tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				$(this).find('select').next().show().siblings().hide();
			}, function() {
				$(this).removeClass("hoverColor");
				var $select = $(this).find('select');
				$select.next().hide();
				$select.prev().show();
			});
			
		});
		
		function back(){
			$("#fileUploadDiv").dialog("close");
		}
		//检查上传的文件是否符合条件
		function checkUpload(){
			var uploadFile = $("#uploadFile").attr("value") ;
			$("#kwsok").attr("disabled",true);
			if(uploadFile.length == 0){//非空检查
				alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_25")) ;
				$("#kwsok").attr("disabled",false);
				return false ;
			}else if(uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xls" && uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xlsx"){//检查文件格式是否是.txt格式
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_165")) ;
				$("#kwsok").attr("disabled",false);
				return false ;
			}else{
				$('#import').attr("disabled","disabled");
				$("#uploadForm").submit();
			
			}
		}
		
		function goopt() {
			location.href="opt_sysuser.htm?method=find";
		}
	</script>
</body>
</html>
