<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.corpmanage.vo.LfSpCorpBindVo"%>
<%@page import="com.montnets.emp.entity.pasroute.GtPortUsed"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%

	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 

	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfSpCorpBindVo> busList = (List<LfSpCorpBindVo>) request.getAttribute("corpList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("corpSp");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<GtPortUsed> gpList = (List<GtPortUsed>)request.getAttribute("gpList");
	@ SuppressWarnings("unchecked")
	List<GtPortUsed> dbList = (List<GtPortUsed>)request.getAttribute("dbList");
	@ SuppressWarnings("unchecked")
	List<LfCorp> lbList = (List<LfCorp>)request.getAttribute("lbList");
	
	String SMSACCOUNT = StaticValue.SMSACCOUNT;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_addBindSp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cor_addBindSp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		
	</head>
	<body id="cor_addBindSp">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"新建绑定关系") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("xtgl","xtgl_qygl_xjbdgx",request)) %>
			<form name="pageForm" action="p_busBindSp.htm" method="post" id="pageForm">
			<input type="hidden" id="smsaccount" name="smsaccount" value=<%=SMSACCOUNT!=null?SMSACCOUNT:"" %>>
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent rContent2">
			<div class="titletop">
						<table class="titletop_table">
							<tr>
								<td class="titletop_td">
									<emp:message key="xtgl_qygl_xjbdgx" defVal="新建绑定关系" fileName="xtgl"></emp:message>
								</td>
								<td align="right">
									<span class="titletop_font fhsyj_span"  onclick="javascript:doCancel();">&larr;&nbsp;<emp:message key="xtgl_qygl_fhsyj" defVal="返回上一级" fileName="xtgl"></emp:message></span>
								</td>
							</tr>
						</table>
			</div>
			<div id="table_input" class="table_input">
				<table id="sysTable" class="sysTable" >
					<tr>
						<td class="widthTD"><emp:message key="xtgl_qygl_qymc_mh" defVal="企业名称：" fileName="xtgl"></emp:message></td>
						<td>
							<select name="cbusname" id="cbusname" class="input_bd">
								<%
								if (lbList != null && lbList.size() > 0)
								{
									for (LfCorp lbm : lbList)
									{
								%>
								<option value='<%=lbm.getCorpCode() %>'>
								[<%=lbm.getCorpCode() %>]<%=lbm.getCorpName() %>
								</option>
								<%		
									}
								}
								%>
							</select>
						</td>
					</tr>
					<tr>
						<td><emp:message key="xtgl_qygl_zhlx_mh" defVal="账户类型：" fileName="xtgl"></emp:message></td>
						<td>
							<select name="ctypename" id="ctypename" class="input_bd" onchange="javascript:changeType($(this).val())">
								<option value="1"><emp:message key="xtgl_qygl_empyyzh" defVal="EMP应用账户" fileName="xtgl"></emp:message></option>
							<% 
								int corpType= StaticValue.getCORPTYPE();
								int spytpeflag = StaticValue.getSPTYPEFLAG();
							%>
							<%if(corpType-0==0 || spytpeflag-0==0) {%>
								<option value="2"><emp:message key="xtgl_qygl_empjrzh" defVal="EMP接入账户" fileName="xtgl"></emp:message>
								</option>
							<%} %>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<hr class="spzh_up_tr_td_hr"/>
						</td>
					</tr>
					<tr>
						<td>
						<%--<%=SMSACCOUNT %>：
						--%><emp:message key="xtgl_qygl_spzh_mh" defVal="SP账号：" fileName="xtgl"></emp:message>
						</td>
						<td>
							<div class="spzh_down_div">
								<table id="t1" class="t1">
									<% if(gpList != null && gpList.size() > 0)
									{
										for (GtPortUsed gpu: gpList)
										{
											%>
											<tr><td class="GtPortUsed_td">
											<input name="gpus" type="checkbox" value='<%=gpu.getUserId() %>' />
											<%=gpu.getUserId() %>
											</td></tr>
											<%
										}
									}
									%>
									</table>
									<table id="t2" class="t2">
									<% if(dbList != null && dbList.size() > 0)
									{
										for (GtPortUsed gpu: dbList)
										{
											%>
											<tr><td class="GtPortUsed_td">
											<input name="gpus2" type="checkbox" value='<%=gpu.getUserId() %>' />
											<%=gpu.getUserId() %>
											</td></tr>
											<%
										}
									}
									%>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2" >
							<hr class="btnOK_up_tr_td_hr"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="right" class="btnOK_td">
								<input type="button" id="btnOK" value="<emp:message key="xtgl_qygl_qd" defVal="确定" fileName="xtgl"></emp:message>" onclick="addBusType()" class="btnClass5"/>
								<input name="" type="button"  onclick="goback()" value="<emp:message key="xtgl_qygl_fh" defVal="返回" fileName="xtgl"></emp:message>" class="btnClass6"/>
								<br />
						</td>
					</tr>
				</table>
			</div>
			
			<%-- <div id="detail_Info" style="padding:5px">
				<table id="totable">
				<thead>
					<tr>
						<td width="30%" height="29px" align="right">
							企业名称：
						</td>
						<td width="70%">
						<select name="cbusname" id="cbusname" style="width:250px;">
						<%
						if (lbList != null && lbList.size() > 0)
						{
							for (LfCorp lbm : lbList)
							{
						%>
						<option value='<%=lbm.getCorpCode() %>'>
						[<%=lbm.getCorpCode() %>]<%=lbm.getCorpName() %>
						</option>
						<%		
							}
						}
						%>
						</select>
						</td>
					</tr>
					<tr>
						<td height="29px" align="right">
							账户类型：
						</td>
						<td width="70%">
						<select name="ctypename" id="ctypename" style="width:250px;" onchange="javascript:changeType($(this).val())">
						
						<option value="1">EMP应用账户</option>
						<%  corpType=StaticValue.CORPTYPE; %>
						<%if(corpType-0==0) {%>
							<option value="2">EMP接入账户</option>
						<%} %>
						</select>
						</td>
					</tr>
					<tr>
						<td style="vertical-align:top;" align="right">
							<%=SMSACCOUNT %>：
						</td>
						<td>
						<div style="display:block;width:250px;height:150px;overflow: auto;border-right: 1px #82baf1 solid;">
						<table id="t1" style="margin-left:3px;width:227px;border:none;">
							<% if(gpList != null && gpList.size() > 0)
							{
								for (GtPortUsed gpu: gpList)
								{
									%>
									<tr><td height="20px">
									<input name="gpus" type="checkbox" value='<%=gpu.getUserId() %>' />
									<%=gpu.getUserId() %>
									</td></tr>
									<%
								}
							}
							%>
							</table>
							<table id="t2" style=" margin-left:3px;width:227px;display: none;border:none;">
							<% if(dbList != null && dbList.size() > 0)
							{
								for (GtPortUsed gpu: dbList)
								{
									%>
									<tr><td height="20px">
									<input name="gpus2" type="checkbox" value='<%=gpu.getUserId() %>' />
									<%=gpu.getUserId() %>
									</td></tr>
									<%
								}
							}
							%>
							</table>
						</div>
						</td>
					</tr>
					<tr id="btn">
						<td colspan='2' align="center" height="30px;">
							<input type="button" id="btnOK" value="确定" onclick="addBusType()" class="btnClass1"/>
							<input type="button" value="返回" onclick="javascript:doCancel();" class="btnClass1"/>
						</td>
					</tr>
					</thead>
				</table>
			</div>--%>
			
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
			</form>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/bindsp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
