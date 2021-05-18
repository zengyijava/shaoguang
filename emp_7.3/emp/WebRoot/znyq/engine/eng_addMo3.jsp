<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@page import="com.montnets.emp.entity.datasource.LfDBConnect"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	List<LfProcess> proList=(List<LfProcess>)request.getAttribute("proList");
	String serId = request.getParameter("serId");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
	@ SuppressWarnings("unchecked")
	List<LfDBConnect> dbList =(List<LfDBConnect>)request.getAttribute("dbList");
	
	LfProcess process = (LfProcess)request.getAttribute("process");
	@ SuppressWarnings("unchecked")
	List<LfTemplate> tmpList=(List<LfTemplate>)request.getAttribute("tmpList");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		
		
	</head>

	<body id="eng_addMo3" class="eng_addMo3">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"步骤管理") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_bzgl",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="addManualConfig" action="eng_moService.htm?method=upSql" method="post" id="addManualConfig">
			<input type="hidden" id="workType" name="workType" value="waterLine"/>
			<input type="hidden" id="prId" name="prId" value='<%=process.getPrId()==null?"":process.getPrId() %>' />
			<input type="hidden" id="serId" name="serId" value='<%=serId%>' />
				<div id="hiddenValueDiv"></div>
				<div class="firstDiv">
					<table id="location" class="linebgimg3">
						<tr>
						<td><b>1.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjsxyw" defVal="新建上行业务" fileName="znyq"></emp:message></td>
						<td><b>2.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjbz" defVal="新建步骤" fileName="znyq"></emp:message></td>
						<td><b>3.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_sdpzbz" defVal="手动配置步骤" fileName="znyq"></emp:message></td>
						<td><b>4.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message></td>
						</tr>
					</table>
				</div>
				<div class="itemDiv" id="item1">
					<span class="righttitle"><emp:message key="znyq_ywgl_sxywgl_sjklj_mh" defVal="数据库连接：" fileName="znyq"></emp:message></span>
				<table>
				<tr>
				<td>
				<select name="dbId" id="dbId" class="input_bd">
				<option class="firstOp" value=""><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
				<%
				for(int i=0;i<dbList.size();i++)
				{
				LfDBConnect dbConn=dbList.get(i);
				%>
				<option value="<%=dbConn.getDbId() %>"
				<%
				if(process.getDbId()-dbConn.getDbId()==0)
				{
					out.print("selected=\"selected\"");
				}
				%>>
				<%=dbConn==null?"":(dbConn.getDbconName()==null?"":dbConn.getDbconName().replace("<","&lt;").replace(">","&gt;")) %>
				</option>
				<%
				}
				%>
				</select>
				</td>
				<% if(btnMap.get(titleMap.get("datasourceConf")+"-1")!=null){%>
				<td><a onclick="toAddDB()" id="toAddDB"><emp:message key="znyq_ywgl_sxywgl_xz" defVal="新增" fileName="znyq"></emp:message></a></td>
				<%} %>
				</tr></table>
				</div>
				
				
				<div class="itemDiv">
					<span class="righttitle"><emp:message key="znyq_ywgl_sxywgl_znzqmb" defVal="智能抓取模板：" fileName="znyq"></emp:message></span>
				<table class="firstTable"><tr><td>
				<select name="tempSel" id="tempSel" onchange="getSql2(this.value)" class="input_bd">
				<option class="firstOp" value=""><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
				<%
					if(tmpList != null && tmpList.size()>0)
					{
						for(LfTemplate temp : tmpList)
						{
				%>
						<option value="<%=temp.getTmid() %>"><%=temp.getTmName().replace("<","&lt;").replace(">","&gt;") %></option>
				<%
						}
					}
				 %>
				</select>
				</td>
				<%if(btnMap.get(titleMap.get("smsTemplate")+"-1")!=null) { %>
				<td><a onclick="showAddSmsTmp(2)" id="showAddSmsTmp"><emp:message key="znyq_ywgl_sxywgl_xz" defVal="新增" fileName="znyq"></emp:message></a></td>
				<%} %>
				</tr></table>
				</div>
				
				<div class="itemDiv">
				<span class="righttitle" id="databaseTip"><emp:message key="znyq_ywgl_sxywgl_sqlyj_mh" defVal="SQL语句：" fileName="znyq"></emp:message></span>
				<table class="secondTable"><tr><td>
				<textarea id="sql" name="sql" class="input_bd textarea_limit"><%
				if(process.getSql()!=null)
				{
					out.print(process.getSql());
				}
				%></textarea>
				</td></tr></table>
				</div>
				
				<div class="itemDiv">
				<table class="thirdTable">
				<tr>
				<td class="fengexian">
					<div class="leftDiv"></div>
					<div class="conCent"> <emp:message key="znyq_ywgl_sxywgl_bbzywc" defVal="本步骤已完成" fileName="znyq"></emp:message> </div>
					<div class="rightDiv"></div>
				</td>
				</tr>
				</table>
				</div>

				<div class="itemDiv div_bg">
				<table id="moreItem">
				<tr>
				<td class="firstTd"><emp:message key="znyq_ywgl_sxywgl_jxcjbz_mh" defVal="继续创建步骤：" fileName="znyq"></emp:message></td>
				<td >
				<input name="morePro" onclick="javascript:$('#subConfigSqlBtn').val('<emp:message key="znyq_ywgl_common_btn_17" defVal="下一步" fileName="znyq"></emp:message>').addClass('indent_none').css({'padding-left':'14px'})" type="radio" value="1"/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_s" defVal="是" fileName="znyq"></emp:message>
				<input class="firstIn" checked onclick="javascript:$('#subConfigSqlBtn').val('<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message>').css({'padding-left':0}).removeClass('indent_none')" name="morePro" id="moreNo" type="radio" value="0"/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_f" defVal="否" fileName="znyq"></emp:message>
				</td>
				</tr>
				</table>
				</div>
				
				<div class="secondDiv">
				<input type="button"  value="<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message>" class="btnClass5 mr23" id="subConfigSqlBtn"/>
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_23" defVal="上一步" fileName="znyq"></emp:message>" class="btnClass6 indent_none" id="subConfigSqlBackBtn"  onclick="goLast()"  />
				<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
				<br/>
				</div>
			</form>
			</div>
			<div id="id2" class="remindMessage"></div>
			<div id="addFrame" title="<emp:message key="znyq_ywgl_sxywgl_xjsjy" defVal="新建数据源" fileName="znyq"></emp:message>">
			<center>
				<iframe id="addDataSource" name="addDataSource" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</center>
			</div>
			<div id="addSmsTmpDiv" title="<emp:message key="znyq_ywgl_sxywgl_mb" defVal="模板" fileName="znyq"></emp:message>">
			<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
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
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_addMo3.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		
		function goLast()
		{
			var prId = $('#prId').val();
			location.href="<%=path%>/eng_moService.htm?method=doProEdit&workType=waterLine&prId="+prId+"&serId=<%=serId %>";
		}
		
		</script>
		
	</body>
</html>