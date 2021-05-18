<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	Integer noticeCount = StaticValue.MAX_PERNOTIC_COUNT;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String depNoBody = ViewParams.DEPNOBODY;
	String depIsExist = ViewParams.DEPISEXIST;
	
	String lgcorpcode = request.getParameter("lgcorpcode");
	String lguserid = request.getParameter("lguserid");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_40" defVal="选择发送对象" fileName="dxzs"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/stasendmmsUserInfo.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<style>form{margin-top: 10px;}</style>
		<%}%>
		<style type="text/css">
			.dxzs_display_none{
				display: none;
			}
			.per_sendNotSelUser .dxzs_div{
				height:450px;
			}
			.per_sendNotSelUser #selectForm{
				margin-top:10px;
			}
			.per_sendNotSelUser .dxzs_td{
				font-size:12px;height:22px;vertical-align:top;
			}
			.per_sendNotSelUser .dxzs_div1.div_bd{
				width:100px;height:22px;background-color:#ffffff;float:left;vertical-align: middle;
			}
			.per_sendNotSelUser #choose_Type{
				line-height:22px;width:100px;border:0;padding:0;margin:0;font-size: 12px;height: 20px;
			}
			.per_sendNotSelUser .div_bd.search_tx_choose.dxzs_div2{
				width:133px;
			}
			.per_sendNotSelUser .graytext.dxzs_searchname{
				font-size:12px;border:0px;width:105px;background-color:#F3FAEE
			}
			.per_sendNotSelUser .dxzs_td1{
				font-size:12px;color:#666666;text-align:left;width: 200px;height:26px;
			}
			.per_sendNotSelUser .dxzs_td2{
				width:235px;
			}
			.per_sendNotSelUser .dxzs_sonFrame{
				height:240px;width:240px;
			}
			.per_sendNotSelUser .shownameDiv.div_bd.dxzs_div3{
				border-bottom: 0;margin-top: 13px;
			}
			.per_sendNotSelUser .title_bg.dxzs_span{
				margin-left: -5px;
			}
			.per_sendNotSelUser .dept.div_bd.dxzs_div4{
				height: 125px; width: 237px;overflow:hidden;display:block;padding-left:5px;
			}
			.per_sendNotSelUser .showchoiceDiv.div_bd.dxzs_div6{
				height:403px;
			}
			.per_sendNotSelUser .dxzs_tr{
				height: 20px;margin-top: 10px;
			}
			.per_sendNotSelUser .dxzs_td3{
				padding-bottom: 5px;font-size: 12px;
			}
			.per_sendNotSelUser .dxzs_label{
				color: blue;font-size: 12px;
			}
			.per_sendNotSelUser .dxzs_td4{
				color:#CCCCCC;
				font-size: 12px;
			}
		</style>
	</head>
	<body class="per_sendNotSelUser">

		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center>
			<div class="dxzs_div">
			<form method="post" id="selectForm">
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<input type="hidden" id="pathUrl" value="<%=path %>">
				<input type="hidden" id="iPathUrl" value="<%=iPath %>">
				<input type="hidden" id="inheritPath" value="<%=inheritPath %>">
				<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode %>">
				<input type="hidden" id="lguserid" value="<%=lguserid %>">
				
				<select class="dxzs_display_none" id='rightSelectTempAll'></select>
				
				<%-- 这里是选择的是  1是员工 2客户3群组 --%>
				<input type="hidden" id="chooseType" value="1"/>	
				<%-- 这里是点击的  1是员工 2客户 3群组  的ID --%>
				<input type="hidden" id="choiceId" value=""/>
				<%-- 这里是点击的  1是员工 2客户  3群组  的名称 --%>
				<input type="hidden" id="choiceName" value=""/>
				<%--操作员的GUID--%>
				<input type="hidden" id="userGuid" value=""/>
				<%-- 员工机构IDS --%>
				<input type="hidden" id="empDepIdsStrs" value=""/>
				<%-- 客户机构IDS --%>
				<input type="hidden" id="cliDepIdsStrs" value=""/>
				<%-- 群组IDS --%>
				<input type="hidden" id="groupIdsStrs" value=""/>
				<%-- 默认是获取分页信息 中第一页 --%>
				<input type="hidden" id="pageIndex" value="1"/>
				
				<%--选择的是 1员工群组还是2客户群组 --%>
				<input type="hidden" id="qztype" value="1"/>
				<%--代表的是员工IDS --%>
				<input type="hidden" id="employeeIds" value=""/>
				<%--代表的是客户IDS--%>
				<input type="hidden" id="clientIds" value=""/>
				<%--代表的是外部人员IDS --%>
				<input type="hidden" id="malistIds" value=""/>
				
				<%--号码字符串 --%>
				<input type="hidden" id="moblieStrs" value=""/>
				
					<%--界面提示 --%>
				<input type="hidden" id="viewDepNoBody" value="<%=depNoBody%>"/>
				<input type="hidden" id="viewDepIsExist" value="<%=depIsExist%>"/>
				<%--完美通知的群组   1   代表 只需要员工 群组       彩信发送中的群组是2 代表的是需要员工客户群组  --%>
				<input type="hidden" id="modulegrouptype" value="1"/>
				<%--完美通知发送最大人数--%>
				<input type="hidden" id="noticeCount" value="<%=noticeCount %>"/>
				
				<table class="chooseBox">
					<tr>
						<td class="dxzs_td" align="left" colspan="2">
								<div class="dxzs_div1 div_bd">
									<select name="choose_Type" id="choose_Type" onchange="choiceTree()">
										<option value="1" id="deptTree"><emp:message key="dxzs_xtnrqf_title_75" defVal="员工通讯录" fileName="dxzs"/></option>
										<%--<option value="2" id="clientTree">客户通讯录</option>
										--%><option value="3" id="groupTree"><emp:message key="dxzs_xtnrqf_title_76" defVal="群组" fileName="dxzs"/></option>
									</select>
								</div>
								
								
								<div class="div_bd search_tx_choose dxzs_div2">
									<table border="0">
										<tr>
											<td width="105px" height="22px">
												<input id="searchname" name="searchname" type="text" maxlength="16" class="graytext dxzs_searchname" value="" 
												onkeypress="if(event.keyCode==13) {searchbyname();event.returnValue=false;}"/>
											</td>
											<td width="28px">
										   		 <a onclick="searchbyname()"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
											</td>
										</tr>
									</table>
							    </div>
						</td>
						
						<td class="dxzs_td1"><emp:message key="dxzs_xtnrqf_title_77" defVal="所选总人数" fileName="dxzs"/>：<label id="manCount">0</label></td>
					</tr>
					</table>
					<table  class="chooseBox">
					<tr>
						<td class="dxzs_td2">
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame" class="dxzs_sonFrame" frameborder="0" src="<%=iPath %>/per_sendNotEmpTree.jsp?lguserid=<%=lguserid %>"></iframe>
										</div>
										<div id="egroup" class="div_bd">
										</div>
										<div class="shownameDiv div_bd dxzs_div3" >
											<span id="showUserName" class="title_bg dxzs_span"><emp:message key="dxzs_xtnrqf_title_78" defVal="成员列表" fileName="dxzs"/>：
											</span>
										</div>
										<div class="dept div_bd dxzs_div4">
											<select  multiple name="left" id="left" size="15" onfocus="treeLoseFocus()" class="left_select_choose"
												ondblclick="">
											</select>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td  align="center"  height="390px;" width="60px;">
						<br>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key='dxzs_xtnrqf_button_11' defVal='删除' fileName='dxzs'/>" onclick="javascript:newmoveOut();">
						</td>
						<td>
							<div class="showchoiceDiv div_bd dxzs_div6">
								<select multiple name="right" id="right" size="27" class="dxzs_display_none">
								</select>
								<ul id="getChooseMan">
								</ul>
							</div>
						</td>
					</tr>
					<tr class="dxzs_tr">
						<td class="dxzs_td3"  align="center">
								<input class="btnClass1" type="button" id="prepage" value="<emp:message key='dxzs_xtnrqf_title_80' defVal='上一页' fileName='dxzs'/>"  onclick="javascript:prePage();">
								<input class="btnClass1" type="button" id="nextpage" value="<emp:message key='dxzs_xtnrqf_title_81' defVal='下一页' fileName='dxzs'/>"  onclick="javascript:nextPage();">
								&nbsp;&nbsp;&nbsp;&nbsp;<label class="dxzs_label"  id="pagecode">
								</label>
								<br/>
						</td>
						<td colspan="2" class="dxzs_td4"  align="right">
									 <emp:message key="dxzs_xtnrqf_title_141" defVal="注：最大选择接收人数" fileName="dxzs"/>1000 &nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</form>
		</div>
		</center>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/pernoticeUserInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>