<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.system.LfOpratelog"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	List<LfOpratelog> logsList =(List<LfOpratelog>)request.getAttribute("logsList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("OprateMonitor");
	
	String beforeTime=request.getParameter("sendtime");
	String afterTime=request.getParameter("recvtime");
	
	String opAction=conditionMap.get("opAction");
	String opModule=conditionMap.get("opModule");
	String opContent=conditionMap.get("opContent");
	String opResult =conditionMap.get("opResult");
	String opUser = conditionMap.get("opUser");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
 %>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="xtgl_czygl_czrz_czjk" defVal="????????????" fileName="xtgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opl_oprateMonitor.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="opl_oprateMonitor">
		<div id="container" class="container">
			<%-- ???????????? --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- ???????????? --%>
				<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="opl_OprateMonitor.htm" method="post"
					id="pageForm">
					<div id="loginUser" class="hidden"></div>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
					<div id="condition">
						<table>
							<tr>
								<td>
									<emp:message key="xtgl_czygl_czrz_czsj_mh" defVal="???????????????" fileName="xtgl"/>
								</td>
								<td>
								<input type="text"  class="Wdate sendtime" readonly="readonly" onclick="stime()"
											 value="<%=beforeTime==null?"":beforeTime %>" id="sendtime" name="sendtime"/>
									
								</td>
								<td>
									<emp:message key="xtgl_spgl_xxsp_z_mh" defVal="??????" fileName="xtgl"/>
								</td>
								<td>
								<input type="text"  class="Wdate recvtime" readonly="readonly" onclick="rtime()"
											 value="<%=afterTime==null?"":afterTime %>" id="recvtime" name="recvtime">
								</td>
								<td>
									<emp:message key="xtgl_czygl_czrz_czlx" defVal="???????????????" fileName="xtgl"/>
								</td>
								<td>
									 <select name="opAction" id="opAction" class="opAction">
					                    <option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="??????" fileName="xtgl"/></option>
										<option value="??????" <%="??????".equals(opAction)?"selected":"" %>><emp:message key="xtgl_spgl_shlcgl_xj" defVal="??????" fileName="xtgl"/></option>
										<option value="??????" <%="??????".equals(opAction)?"selected":"" %> ><emp:message key="xtgl_spgl_shlcgl_sc" defVal="??????" fileName="xtgl"/></option>
										<option value="??????" <%="??????".equals(opAction)?"selected":"" %> ><emp:message key="xtgl_spgl_shlcgl_xg" defVal="??????" fileName="xtgl"/></option>
										<option value="??????" <%="??????".equals(opAction)?"selected":"" %> ><emp:message key="xtgl_czygl_czrz_qt" defVal="??????" fileName="xtgl"/></option>
									</select>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
							<tr>
								<%--<td>
									???????????????
								</td>
								<td>
									<select name="opModule" id="opModule" style="width: 180px">
										<option value="">??????</option>
										<option value="????????????" <%="????????????".equals(opModule)?"selected":"" %> >????????????</option>
										<option value="????????????" <%="????????????".equals(opModule)?"selected":"" %> >????????????</option>
										<option value="????????????" <%="????????????".equals(opModule)?"selected":"" %> >????????????</option>
										<option value="????????????"  <%="????????????".equals(opModule)?"selected":"" %>>????????????</option>
										<option value="????????????"  <%="????????????".equals(opModule)?"selected":"" %>>????????????</option>
										<option value="????????????" <%="????????????".equals(opModule)?"selected":"" %> >????????????</option>
										<option value="????????????" <%="????????????".equals(opModule)?"selected":"" %> >????????????</option>
										<option value="???????????????" <%="???????????????".equals(opModule)?"selected":"" %> >???????????????</option>
									</select>
								</td> --%>
								<td>
									<emp:message key="xtgl_czygl_czrz_cznr_mh" defVal="???????????????" fileName="xtgl"/>
								</td>
								<td >
									<input type="text" name="opContent" id="opContent" 
										class="opContent" value="<%=opContent==null?"":opContent %>"  maxlength="20" />
								</td>
								<td>
									<emp:message key="xtgl_czygl_czrz_czjg_mh" defVal="???????????????" fileName="xtgl"/>
								</td>
								<td>
									<select name="opResult"  id="opResult" class="opResult">
										<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="??????" fileName="xtgl"/></option>
										<option value="1" <%="1".equals(opResult)?"selected":"" %>><emp:message key="xtgl_czygl_czrz_cg" defVal="??????" fileName="xtgl"/></option>
										<option value="0" <%="0".equals(opResult)?"selected":"" %>><emp:message key="xtgl_czygl_czrz_sb" defVal="??????" fileName="xtgl"/></option>
									</select>
								</td>
								<td>
								    <emp:message key="xtgl_czygl_czrz_czyhid_mh" defVal="????????????ID???" fileName="xtgl"/>
								</td>
								<td >
									<input type="text" name="opUser" id="opUser" 
										class="opUser" value="<%=opUser==null?"":opUser %>"  maxlength="20" />
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>

								<th class="content_th1">
									<emp:message key="xtgl_czygl_czrz_czsj" defVal="????????????" fileName="xtgl"/>
								</th>
								<th class="content_th2">
									<emp:message key="xtgl_czygl_czrz_czyhid" defVal="????????????ID" fileName="xtgl"/>
								</th>
								<th class="content_th3">
									<emp:message key="xtgl_czygl_czrz_czlx" defVal="????????????" fileName="xtgl"/>
								</th>
								<%--<th>
									????????????
								</th> --%>
								<th>
									<emp:message key="xtgl_czygl_czrz_cznr" defVal="????????????" fileName="xtgl"/>
								</th>
								<th class="content_th4">
									<emp:message key="xtgl_czygl_czrz_czjg" defVal="????????????" fileName="xtgl"/>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
										
								if (logsList != null && logsList.size() != 0 )
								{
									for (int i = 0; i < logsList.size(); i++)
									{
										LfOpratelog log = logsList.get(i);
							%>
							<tr>
								<td> <%java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  %>
										  <%if(log.getOpTime()==null||"".equals(log.getOpTime())){ %>
										 - <%}else{ %>
										 <%=df.format(log.getOpTime()) %><%} %></td>
								<td class="textalign"><%=log.getOpUser()%></td>
								<td>
									<%
										String action = "";
										if(log.getOpAction().equals("??????")){
											action = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_xj",request);
										}else if(log.getOpAction().equals("??????")){
											action = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_xg",request);
										}else if(log.getOpAction().equals("??????")){
											action = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_sc",request);
										}else if(log.getOpAction().equals("??????")){
											action = MessageUtils.extractMessage("xtgl","xtgl_czygl_czrz_qt",request);
										}
									 %>
									<%=action%>
								</td>
							<%--	<td><%=log.getOpModule()%></td> --%>
				<%
				String opContentResult=log.getOpContent();
					if (log.getOpResult() - 0 == 0
						&& log.getOpContent().contains(StaticValue.OPSPER)) {
					opContentResult = opContentResult.substring(0, opContentResult
					.lastIndexOf(StaticValue.OPSPER));
					}
				%>
								<td class="textalign"><xmp><%=opContentResult%></xmp></td>
								<td><%=log.getOpResult()!=null&&log.getOpResult()==1?MessageUtils.extractMessage("xtgl","xtgl_czygl_czrz_cg",request):MessageUtils.extractMessage("xtgl","xtgl_czygl_czrz_sb",request)%></td>
								
								</tr>
							<%
									}
								}else{
							%>
							 <tr><td colspan="11"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="?????????" fileName="xtgl"/></td></tr><%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="6">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				<%
				}
				 %>
			</div>
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"> 
					</div>
				</div>
			</div>
			</form>
			<%-- foot?????? --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/oprateMonitor.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#loginUser");
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
			
		});
		
		</script>
	</body>
</html>
