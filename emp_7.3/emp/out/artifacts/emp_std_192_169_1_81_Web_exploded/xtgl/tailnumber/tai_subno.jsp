<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.tailnumber.LfSubnoAllot"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.HashMap"%>
<%@ page import="com.montnets.emp.tailnumber.vo.dao.LfSnoAllotVo"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("subno");
	menuCode = menuCode==null?"0-0-0":menuCode;
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfSnoAllotVo> allotList = (List<LfSnoAllotVo>) request.getAttribute("allotList");
	Integer digit = (Integer) request.getAttribute("digit");
	
	String startTime=(String)request.getParameter("sendtime");
	String endTime=(String)request.getParameter("recvtime");
	
	String name = request.getParameter("name");
	String state = request.getParameter("state");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String subnoquery = request.getParameter("subnoquery");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tai_subno.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="tai_subno">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<input type="hidden" id="oldId" value="" />
		<input type="hidden" id="guid" value="" />
		<input type="hidden" id="type" value="" />
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<div id="editDiv" title="<emp:message key='xtgl_cswh_whgl_xgwh' defVal='修改尾号' fileName='xtgl'/>" class="editDiv">
					<table class="editDiv_table">
						<tr>
							<td class="cmName_td" align="right">
								<div  id="cmName"></div>
							</td>
							<td class="subnoName_td">
							 	 <span id="subnoName">
							       <%-- <input id="subnoName" name="subnoName"  value="" type="text" readonly style="width:154px;height:18px;border: 1px solid #7F9DB9;"/> --%>
							      </span>
							</td>
					   </tr>
					   <tr><td class="subnoName_down_tr_td"></td></tr>
					   <tr>
							<td align="right">
							<emp:message key="xtgl_cswh_whgl_dqwh_mh" defVal="当前尾号：" fileName="xtgl"/>
							</td>
							<td>
								<input type="text" name="subnoId"  id="subnoId"  value="22"    maxlength="<%=digit %>"  onkeyup="value=value.replace(/[^\d]/g,'')" class="subnoId"/>
							</td>
						</tr>
						<tr><td class="subnoId_down_tr_td"></td></tr>
					</table>
					
					<center>
					<input class="btnClass5 mr23" type="button"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="javascript:update()"/>
					<input class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" onclick="closeDiv()"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</center>
			</div>
			
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
				<div class="buttons">
					<div id="toggleDiv">
					</div>
				</div>
				<form name="pageForm" action="tai_subno.htm?method=find" method="post" id="pageForm">
				<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
				<div id="condition" >
						 <table>
								<tbody>
								<tr>
									<td>
										<emp:message key="xtgl_cswh_whgl_lx_mh" defVal="类型：" fileName="xtgl"/>
									</td>
									<td>
										<label>
											<select id="state" name="state" onchange="qeuryUser()" class="state">
											<option value="">
													<emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/>
												</option>
											<option value="2" <%="2".equals(state)?"selected":"" %>>
													<emp:message key="xtgl_spgl_shlcgl_czy" defVal="操作员" fileName="xtgl"/>
												</option>
											<option value="3" <%="3".equals(state)?"selected":"" %>>
													<emp:message key="xtgl_cswh_whgl_mk" defVal="模块" fileName="xtgl"/>
												</option>
											</select>
										</label>
									</td>
									<td> 
									<emp:message key="xtgl_cswh_whgl_wh_mh" defVal="尾号：" fileName="xtgl"/>
									</td>
									<td> 
									<input type="text" class="subnoquery"  id="subnoquery" value="<%=subnoquery==null?"":subnoquery %>" name="subnoquery" />
									</td>
									<td id="xx" <%if(!"2".equals(state)){ %>class="xx"<%} %>> 
									<emp:message key="xtgl_cswh_whgl_czymc_mh" defVal="操作员名称：" fileName="xtgl"/>
									</td>
									<td id="yy" class="<%="2".equals(state)?"":"hidden" %>">
										<input type="text" class="name"  id="name" value="<%=name==null?"":name %>" name="name" />
									</td>
									<td id="ww" class="<%=(!"2".equals(state))?"":"hidden" %> ww"  colspan="2">
									</td>
									<td class="tdSer">
									<center><a id="search"></a></center>
									</td>
								</tr>
							</tbody>
							</table>
						</div>
				<table id="content">
					<thead>
						<tr >
							<%--
							<th>
								ID
							</th>
							 --%>
							<th>
								<emp:message key="xtgl_cswh_whgl_lx" defVal="类型" fileName="xtgl"/>
							</th>
							<th>
								<emp:message key="xtgl_cswh_whgl_czymk" defVal="操作员/模块" fileName="xtgl"/>
							</th>
							<th>
								<emp:message key="xtgl_cswh_whgl_dqwh" defVal="当前尾号" fileName="xtgl"/>
							</th>
							<th>
								<emp:message key="xtgl_cswh_twgl_cjsj" defVal="创建时间" fileName="xtgl"/>
							</th>
							<th>
								<emp:message key="xtgl_cswh_whgl_gxsj" defVal="更新时间" fileName="xtgl"/>
							</th>
							<%if(btnMap.get(menuCode+"-3") != null || btnMap.get(menuCode+"-2") != null)  {  %>
								<th colspan="2">
									<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
								</th>
							<%} %>
						</tr>
					</thead>
					<tbody>
							<% 
								if(allotList != null && allotList.size()>0){
									LfSnoAllotVo allot = null;
									for(int i=0;i<allotList.size();i++){
										allot = allotList.get(i);
							%>
										<tr>
										<%--
											<td>
												<%=allot.getSuId() %>
											</td>
											
											 --%>
											<td class="ztalign" >
												<%
												if(allot.getMenuCode()==null)
												{
													out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_czy",request));
												}
												else
												{
													out.print(MessageUtils.extractMessage("xtgl","xtgl_cswh_whgl_mk",request));
												}
												%>
											</td>
											<td class="textalign" >
												<%
												if(allot.getMenuCode()==null)
												{
													out.print(allot.getUsername()+"("+allot.getName()+")");
												}
												else
												{
													out.print(allot.getName());
												}
												%>
											</td>
											<td>
												<%=allot.getUsedExtendSubno()==null?"-":allot.getUsedExtendSubno() %>
											</td>
											<td>
												<%=allot.getCreateTime()==null?"-":df.format(allot.getCreateTime()) %>	
											</td>
											<td>
												<%=allot.getUpdateTime()==null?"-":df.format(allot.getUpdateTime()) %>	
											</td>
												<%if(btnMap.get(menuCode+"-3") != null || btnMap.get(menuCode+"-2") != null)  {  
													if(allot.getLoginId() != null && !"".equals(allot.getLoginId())){
												%>
													
														<% 
															if(btnMap.get(menuCode+"-2") != null){
														%>
															<td>
																<a href="javascript:updateSubno('<%=allot.getName() %>','<%=allot.getUsedExtendSubno() %>','<%=allot.getLoginId() %>','1')"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
															</td>
															
														<% 
															}else{
																	%>
																	<td>-</td>
																	<% 
															}
														%>
														<% 
															if(btnMap.get(menuCode+"-3") != null){
														%>
															<td>
																<a href="javascript:delSubno('<%=allot.getLoginId() %>')"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
															</td>
														<% 
															}else{
																	%>
																	<td>-</td>
																	<% 
															}
														%>
												<%
														}else if(!"".equals(allot.getMenuCode()) && allot.getMenuCode()!=null && !StaticValue.IM_MENUCODE.equals(allot.getMenuCode())){
																%>
																	<td>
																		<a href="javascript:updateSubno('<%=allot.getName() %>','<%=allot.getUsedExtendSubno() %>','<%=allot.getMenuCode() %>','2')"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
																	</td>
																	<td>-
																	</td>
																<% 
														}else {
																%>
																	<td>-</td>
																	<td>-</td>
																<% 
														}
													}else{
													%>
														<td>-</td>
														<td>-</td>
													<% 
													}
												 %>
										</tr>
							<% 
									}
								}
								else
								{
							%>
							<tr>
							<td colspan="9">
							<emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/>
							</td>
							</tr>							
							<%
								}
							%>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="9">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
				</form>
			</div>
			<%} %>
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
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=iPath %>/js/subno.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		
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
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				}, function() {
				$(this).removeClass("hoverColor");
			});
		
		    initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);

			$('#search').click(function(){submitForm();});
			$('#editDiv').dialog({
			autoOpen: false,
			height: 180,
			width: <%=StaticValue.ZH_HK.equals(langName)?350:300%>,
			modal:true
			});
		
		});
		
		</script>
	</body>
</html>
