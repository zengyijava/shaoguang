<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.translog.entity.LfTransLog" %>
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
	List<LfTransLog> logList =(List<LfTransLog>)request.getAttribute("logList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("/transLog");
	
	String beforeTime=request.getParameter("sendtime");
	String afterTime=request.getParameter("recvtime");
	// 汇总类型
	String usetype=conditionMap.get("usetype");
	String tstatus=conditionMap.get("tstatus");
	String transname=conditionMap.get("transname");
	String runflag =conditionMap.get("runflag");

	String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
 %>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title>汇总转移日志</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/transLog.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="translog">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
				<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="transLog.htm" method="post"
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
									执行时间：
								</td>
								<td>
								<input type="text"  class="Wdate sendtime" readonly="readonly" onclick="stime()"
											 value="<%=beforeTime==null?"":beforeTime %>" id="sendtime" name="sendtime"/>
									
								</td>
								<td>
									<emp:message key="xtgl_spgl_xxsp_z_mh" defVal="至：" fileName="xtgl"/>
								</td>
								<td>
								<input type="text"  class="Wdate recvtime" readonly="readonly" onclick="rtime()"
											 value="<%=afterTime==null?"":afterTime %>" id="recvtime" name="recvtime">
								</td>
								<td>
									汇总类型：
								</td>
								<td>
									 <select name="usetype" id="usetype" class="usetype">
					                    <option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
										<option value="SMS" <%="SMS".equals(usetype)?"selected":"" %>>短信</option>
										<option value="MMS" <%="MMS".equals(usetype)?"selected":"" %>>彩信</option>
									</select>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
							<tr>

								<td>
									汇总名称：
								</td>
								<td >
									<input  name="transname" id="transname"
										class="tsContent" value="<%=transname==null?"":transname %>" maxlength="20" />
								</td>
								<td>
									日志内容：
								</td>
								<td>
									<input  name="tstatus" id="tstatus"
										   class="tsContent" value="<%=tstatus==null?"":tstatus %>"  maxlength="20" />
								</td>
								<td>
								    执行标志：
								</td>
								<td >
									<select name="runflag" id="runflag" class="usetype">
										<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
										<option value="0" <%="0".equals(runflag)?"selected":"" %>>开始</option>
										<option value="1" <%="1".equals(runflag)?"selected":"" %>>结束</option>
									</select>
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>

								<th class="content_th1">
									执行时间
								</th>
								<th class="content_th2">
									汇总类型
								</th>
								<th class="content_th3" style="width: 200px">
									汇总名称
								</th>

								<th>
									日志内容
								</th>
								<th class="content_th4">
									执行标志
								</th>
							</tr>
						</thead>
						<tbody>
							<%
										
								if (logList != null && logList.size() != 0 )
								{
									for (int i = 0; i < logList.size(); i++)
									{
										LfTransLog log = logList.get(i);
							%>
							<tr>
								<td> <%java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  %>
										  <%if(log.getCreatetime()==null||"".equals(log.getCreatetime())){ %>
										 - <%}else{ %>
										 <%=df.format(log.getCreatetime()) %><%} %></td>
								<td class="textalign"><%
									String type = log.getUsetype();
									if ("SMS".equals(type)){
										type = "短信";
									}else {
										type = "彩信";
									}
									%>
									<%=type%>
								</td>
								<td >
									<%=log.getTransname()%>
								</td>
								<td class="textalign"><%=log.getTstatus()%></td>
								<td><%=log.getRunflag()!=null&&log.getRunflag()==1?"结束":"开始"%></td>
								
								</tr>
							<%
									}
								}else{
							%>
							 <tr><td colspan="11"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr><%} %>
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
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"> 
					</div>
				</div>
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/transLog.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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
