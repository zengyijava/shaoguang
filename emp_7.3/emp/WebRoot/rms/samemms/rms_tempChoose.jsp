<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="com.montnets.emp.rms.servmodule.constant.ServerInof" %>
<%@ page import="com.montnets.emp.rms.vo.LfTemplateVo" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfTemplateVo> temList = (List<LfTemplateVo>) request.getAttribute("temList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

	LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
	String lguserid = String.valueOf(loginSysuser.getUserId());

	String choosePublic = request.getAttribute("choosePublic").toString();

	//数据回显
	String tempName = request.getAttribute("tempName")==null?"":request.getAttribute("tempName").toString();
	String tempId = request.getAttribute("tempId")==null?"":request.getAttribute("tempId").toString();
	String tempType = request.getAttribute("tempType")==null?"":request.getAttribute("tempType").toString();
	String pageSource = request.getAttribute("pageSource")==null?"":request.getAttribute("pageSource").toString();

	//服务器名称
	String serverName = ServerInof.getServerName();
	//语言
	//String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/rms/samemms/css/tempChoose.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rms_tempChoose" id="rms_tempChoose">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="rms_rmsSameMms.htm?method=getLfTemplateByMms&lguserid=<%=lguserid%>" method="post" id="pageForm">
				<div class="fx-module-pop">
			        <div class="module-pop-hd">
			            <ul class="hd-table">
			                <li id="myTemp" class="hd-table-li active" onclick="selectTemp('myTemp')">我的场景</li>
			                <li id="publicTemp" class="hd-table-li " onclick="selectTemp('publicTemp')" style="display: none">公共场景</li>
			            </ul>
			            <div class="module-pop-close">
			                <i class="close-icon "></i>
			            </div>
			        </div>
			    </div>
			</form>
		</div>
		<div id="modify" title="富信主题"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>

		<%-- 预览弹层  --%>
		<div id="myView" title="预览" style="display: none;">
		<div id="phone_background">
			<div id="cust_preview_outer" class="rollBak">
				<div id="cust_preview"></div>
			</div>
		</div>
		</div>

		<%--富信内容预览DIV --%>
		<div id="tempView" title="富信内容" style="display:none;overflow: auto;">
			<input type="hidden" id="tmmsId" value=""/>
			<div style="float: left;width:250px;margin-left: 30px;display: inline;">
				<div id="mobile" style="width:240px;height:470px;position: absolute;background: url('<%=commonPath %>/common/img/iphone5.jpg') no-repeat">
				<center>
				<div id="pers" style='position:relative;margin-top:90px;border:1px #09F solid;text-align:left;display:none;width:180px;height:13px;background-color:#FFFFFF'>

				<div id="showtime" style="position:absolute;width:140px;height:13px;text-align: center;"></div>

				<div id='chart' style='position:absolute;left:0; background-color:#00ffdd;width:0;height:13px;text-align:right;padding-top:0px;'>
				</div>
				</div>
				</center>

				<div id="screen" style="position:relative;width:189px;height:240px;background:#ffffff;margin-left:23px;margin-top:95px;overflow: auto;padding:2px;word-break: break-all;">
				</div>
				<center>
				<table>
				<tr>
				  <td>
				     <label id="pointer" style="vertical-align: bottom"></label>
				     <label id="nextpage" style="vertical-align: bottom"></label>
				  </td>
				</tr>
				<tr align="center">
					<td>
					   <label id="currentpage" style="vertical-align: bottom"></label>
					</td>
				</tr>
				</table>
				</center>
				</div>
			</div>
			<div id="inputParamCnt1" style="margin-top: 10px;margin-left: 280px; ">
			</div>
		</div>
		<div class="clear"></div>
		<%--手动加的遮罩层--%>
		<div id="mask" class="mask"></div>
		<script>
			var serverName = "<%=serverName%>";
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/rms/samemms/js/template.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/rms/samemms/js/tempChoose.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			var pageSource = <%=pageSource %>;
			if(pageSource==1){
				$(".close-icon").click(function(){
					$(window.parent.document).find("#popup_div").css({"display":"none"});
	 				$(window.parent.document).find("#tempFrames").css({"display":"none"});
				});
			}else{
				$(".close-icon").click(function(){
					parent.closeDialog();
				});
			}
		});
		</script>
	</body>
</html>
