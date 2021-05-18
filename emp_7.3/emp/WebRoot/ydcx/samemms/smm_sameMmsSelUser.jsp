<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	Integer noticeCount = StaticValue.MAX_PERNOTIC_COUNT;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	//String depNoBody = ViewParams.DEPNOBODY;
	//String depIsExist = ViewParams.DEPISEXIST;
	
	String depNoBody = MessageUtils.extractMessage("ydcx","ydcx_cxyy_jtcxfs_text_74",request);
	String depIsExist = MessageUtils.extractMessage("ydcx","ydcx_cxyy_jtcxfs_text_75",request);
	
	
	String lguserid = request.getParameter("lguserid");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title>静态彩信选择发送对象</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=iPath%>/css/stasendmmsUserInfo.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body>

		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center>
			<div  style="height:450px;">
			<form method="post" id="selectForm">
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<input type="hidden" id="pathUrl" value="<%=path %>">
				<input type="hidden" id="iPathUrl" value="<%=iPath %>">
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
				<input type="hidden" id="modulegrouptype" value="2"/>
				
				<table  class="chooseBox">
					<tr style="height:22px">
						<td style="font-size:12px;height:26px;vertical-align:top" align="left" colspan="2">
								<%--
								<input type="radio" id="deptTree" name="dept" checked="checked" onclick="choiceTree('1')" value="1"/> 员工通讯录
								<input type="radio" id="clientTree" name="dept"  onclick="choiceTree('2')" value="2"/>客户通讯录
								<input type="radio" id="groupTree" name="dept"  onclick="choiceTree('3')" value="3"/>群组 --%>
								<div style="width:100px;height:22px;background-color:#ffffff;float:left;vertical-align: middle" class="div_bd">
									<select name="choose_Type" id="choose_Type" style="line-height:22px;width:100px;border:0;padding:0;margin:0;font-size: 12px;" onchange="choiceTree()">
										<option value="1" id="deptTree"><emp:message key="ydcx_cxyy_jtcxfs_text_57" 
										defVal="员工通讯录" fileName="ydcx"></emp:message></option>
										<option value="2" id="clientTree"><emp:message key="ydcx_cxyy_jtcxfs_text_58" 
										defVal="客户通讯录" fileName="ydcx"></emp:message></option>
										<option value="3" id="groupTree"><emp:message key="ydcx_cxyy_jtcxfs_text_59" 
										defVal="群组" fileName="ydcx"></emp:message></option>
									</select>
								</div>
						</td>
						<td style="font-size:12px;color:#666666;text-align:left;width: 200px;height:26px;"><emp:message key="ydcx_cxyy_jtcxfs_text_60" 
										defVal="所选总人数：" fileName="ydcx"></emp:message><label id="manCount">0</label></td>
					</tr>
					</table>
					<table  class="chooseBox">
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame"  style="height:240px;width:240px;" frameborder="0" src="<%=iPath %>/smm_sameMmsEmpTree.jsp?lguserid=<%=lguserid%>"></iframe>
										</div>
										<div id="egroup" class="div_bd">
										</div>
										<div class="shownameDiv div_bd" style="border-bottom: 0;">
											<span id="showUserName" class="title_bg" style="margin-left: -10px;">&nbsp;&nbsp;<emp:message key="ydcx_cxyy_jtcxfs_text_61" 
										defVal="成员列表：" fileName="ydcx"></emp:message>
												<%-- <label id="addrName">成员列表：</label><label id="userTotal" style="color:blue"></label>
												  --%>
											</span>
										</div>
										<div class="dept div_bd" style="height: 120px; width: 237px;overflow:hidden;display:block;padding-left:5px;">
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
						<%--
							<input class="btnClass2" type="button" id="selectDep" value="选择"  onclick="javascript:choiceBtn();">
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<br>
							<input class="btnClass2" type="button" id="toLeft" value="选择人员" onclick="javascript:moveIn();">
							<input class="btnClass2" type="button" id="toRight" value="移出" onclick="javascript:moveOut();">
						--%>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="ydcx_cxyy_common_text_10" 
										defVal="选择" fileName="ydcx"></emp:message>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key="ydcx_cxyy_common_text_8" 
										defVal="删除" fileName="ydcx"></emp:message>" onclick="javascript:moveOut();">
							
						</td>
						<td>
							<div class="showchoiceDiv div_bd">
								<select multiple name="right" id="right" size="27" style="display:none;">
								</select>
								<ul id="getChooseMan">
								</ul>
							</div>
						</td>
					</tr>
					<tr   height="38px;">
						<td style="padding-bottom: 5px;font-size: 12px;"  align="center">
								<input class="btnClass1" type="button" id="prepage" value="<emp:message key="ydcx_cxyy_jtcxfs_text_62" 
										defVal="上一页" fileName="ydcx"></emp:message>"  onclick="javascript:prePage();">
								<input class="btnClass1" type="button" id="nextpage" value="<emp:message key="ydcx_cxyy_jtcxfs_text_63" 
										defVal="下一页" fileName="ydcx"></emp:message>"  onclick="javascript:nextPage();">
								&nbsp;&nbsp;&nbsp;&nbsp;<label style="color: blue;font-size: 12px;"  id="pagecode">
								</label>
								<br/>
						</td>
						<td colspan="2">
							&nbsp;
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
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/staMmsUserInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send_cx.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
	</body>
</html>