<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

	String userid2=request.getParameter("userid1")+"";
	String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp" %>
		<title><emp:message key='ydwx_wxfs_jtwxfs_fshm_xzry_td1' defVal='选择发送对象' fileName='ydwx'></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style type="text/css">
			center {
				font-size: 14px;
			}
			select {
				font-size: 14px;
			}
		</style>

		<script type="text/javascript">
		var zTree;
		var ipathh = "<%=path %>";
		var iPath = "<%=iPath %>";
		var userid2="<%=userid2%>";
		
			
		</script>
	</head>
	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center class="centerBack">
			<div style="height:465px">
			<form method="post" id="selectForm">
				<input type="hidden" id="strUser" name="strUser" value=""/>
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<select style="display:none" id='tempOptions'></select>
				<select style="display:none" id='rightSelectTemp'></select>
				<select style="display:none" id='rightSelectTempAll'></select>
				<input type="hidden" id="depId" name="depId" value=""/>
				<input type="hidden" id="depName" name="depName" value=""/>
				<input type="hidden" id="user" name="user" value=""/>
				<input type="hidden" id="addType" name="addType" value="3"/>
				<input type="hidden" id="lguser" name="lguser" value="" />		
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
				<input type="hidden" id="totalPage" name="totalPage" value=""/>
				<input type="hidden" id="groupClientStr" value=""/>
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
				
				
				<%--完美通知的群组   1   代表 只需要员工 群组       彩信发送中的群组是2 代表的是需要员工客户群组  --%>
				<input type="hidden" id="modulegrouptype" value="2"/>
				
				
				<table class="chooseBox">
					<tr>
						<td style="font-size:12px;height:26px;vertical-align:top" align="left" colspan="2">
						<div class="div_bd" style="width:80px;height:22px;background-color:#ffffff;float:left;vertical-align: middle">
						<select name="chooseType" id="chooseType" style="line-height:22px;width:80px;border:0;padding:0;margin:0;font-size:12px" onchange="changeChooseType()">
							<option value="1" id="chooseType1">
								<emp:message key="ydwx_wxfs_jtwxfs_fshm_xzry_options1" defVal="员工通讯录" fileName="ydwx"></emp:message>
							</option>
							<option value="3" id="clientTree">
								<emp:message key="ydwx_wxfs_jtwxfs_fshm_xzry_options2" defVal="客户通讯录" fileName="ydwx"></emp:message>
							</option>
							<option value="2" id="chooseType2">
								<emp:message key="ydwx_wxfs_jtwxfs_fshm_xzry_options3" defVal="群组" fileName="ydwx"></emp:message>
							</option>
						</select>
						</div>
						<div class="div_bd search_tx_choose">
						<table border="0">
						<tr>
						<td width="125px" height="22px">
						<input id="epname" name="epname" type="text" maxlength="16" class="graytext" value="" style="font-size:12px;border:0px;width:125px;background-color:#F3FAEE"  onkeypress="if(event.keyCode==13) {zhijieSearch();event.returnValue=false;}"/>
						</td>
						<td width="28px">
					    <a onclick="zhijieSearch()"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
						</td>
						</tr>
						</table>
					    </div>
						</td>
						<td style="font-size:12px;color:#666666;text-align:left;">
							<emp:message key="ydwx_wxfs_jtwxfs_fshm_xzry_td2" defVal="所选总人数：" fileName="ydwx"></emp:message>
						<label id="manCount">0</label></td>
					</tr>
					</table>
					<table class="chooseBox">
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd" style="width:242px;height:240px;">
											<iframe id="sonFrame" name="sonFrame" style="height:240px;width:240px;" frameborder="0" src="<%=iPath %>/wx_tree.jsp?userid2=<%=userid2%>"></iframe>
										</div>
										<div id="egroup"  class="dept hidden div_bd" style="background-color:#ffffff;height:240px;width:240px;overflow:hidden;">
										</div>
										<div class="div_bd div_bg" style="border-bottom:0;margin-top:10px;height:22px;width:242px;float:left;overflow: hidden">
											<span style="float:left;margin-top:3px;font-size:12px">&nbsp;&nbsp;
												<emp:message key="ydwx_wxfs_jtwxfs_fshm_xzry_td3" defVal="成员列表：" fileName="ydwx"></emp:message>
											</span><span id="showUserName" style="display:none;height:22px;width:240px;padding-top: 5px; padding-left:10px; text-align: left;font-size: 12px;"><emp:message key="common_cAddressBook" defVal="当前通讯录：" fileName="common"></emp:message></span>
										</div>
										<div class="dept div_bd" style="height: 120px; width: 237px;overflow:hidden;display:block;padding-left:5px;">
											<select  multiple name="left" id="left" size="15" onfocus="treeLoseFocus()" class="left_select_choose">
											</select>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td width="60" align="center">
						<br>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key='ydwx_common_btn_shanqu' defVal='删除' fileName='ydwx'></emp:message>" onclick="javascript:moveRight();">
						</td>
						<td>
							<div id="rightDiv"  class="dept div_bd" >
								<select multiple name="right" id="right" size="20" 
									style='width:204px;height: 418px; display:none;border:0;float:left;color: black;font-size: 12px;padding:4px;vertical-align:middle;margin:-6px -10px;' >
								</select>
								<ul id="getChooseMan">
								</ul>
							</div>
						</td>
						
					</tr>
					<tr   height="38px;">
						<td style="padding-bottom: 5px;font-size: 12px;"  align="center">
								<input class="btnClass1" type="button" id="prepage" value="<emp:message key='ydwx_common_btn_shangyiye' defVal='上一页' fileName='ydwx'></emp:message>"  onclick="javascript:prePage();" >
								<input class="btnClass1" type="button" id="nextpage" value="<emp:message key='ydwx_common_btn_xiayiye' defVal='下一页' fileName='ydwx'></emp:message>"  onclick="javascript:nextPage();">
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
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/chooseInfo_new_wx.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/wx_userInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
    <script type="text/javascript">
     	
    </script>
	</body>
</html>