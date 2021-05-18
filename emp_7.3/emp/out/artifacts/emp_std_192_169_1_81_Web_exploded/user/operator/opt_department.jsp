<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>

<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("department");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	boolean addcode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addcode = true;
	}
	boolean delcode = false;
	if(btnMap.get(menuCode+"-2")!=null)
	{
		delcode = true;
	}
	
	LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	LinkedHashMap<String, String> corpConf = SystemGlobals.getSysParamLfcorpConf(lfSysuser.getCorpCode());
	
	String lguserid = (String)request.getAttribute("lguserid");
	request.setAttribute("lguserid",lguserid);
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
%>
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath %>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/department.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_department.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="opt_department" onload="showDepTable()">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<div id="loginUser" class="hidden"></div>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
				<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" style="">
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					<input type="hidden" id="corptype" name="corptype" value="<%=StaticValue.getCORPTYPE() %>" />
					<h3 class="div_bd title_bg">
						<emp:message key="user_xtgl_czygl_text_51" 
										defVal="操作员机构" fileName="user" />
					</h3>
					<div id="depOperate" class="depOperate">
						<%if (delcode){%>
							<span id="delDepNew" class="depOperateButton3" onclick="delDeps('','','')"></span>
						<%} %>
						<span id="updateDepNew" class="depOperateButton2" onclick="updateDepFun()"></span>
						<%if (addcode){%>
							<span id="addDepNew" class="depOperateButton1" onclick="addDepFun()"></span>
						<%} %>
					</div>
					<div class="list depTree_up_div"  >
						<div id="depTree"><jsp:include page="opt_depTree.jsp"></jsp:include>  </div>	
					</div>
				</div>
				<div class="right_info">
				<form name="pageForm" action="" method="post">
						<input type="hidden" id="depId" value=""/>
						<input type="hidden" id="upDepName" value=""/>
						<input type="hidden" id="pareDepId" value=""/>
						<input type="hidden" id="servletUrl" value="opt_department.htm"/>
						<input type="hidden" id="depmaxlevel" value="<%=corpConf.get(StaticValue.DEP_MAXLEVEL) %>"/>
						<input type="hidden" id="depmaxchild" value="<%=corpConf.get(StaticValue.DEP_MAXCHILD) %>"/>
						<input type="hidden" id="depmaxdep" value="<%=corpConf.get(StaticValue.DEP_MAXDEP) %>"/>
						<input type="hidden" id="departmentUrl" value="opt_department.htm?tttt=<%=System.currentTimeMillis()%>"/>
						
						
					<div class="tableInfo_up_div"></div>
					<div id="tableInfo">
					</div>
					</form>
				</div>
			<%--按添加按钮的HTML--%>
			<div id="addDiv" title="<emp:message key="user_xtgl_czygl_text_52" 
										defVal="新建/修改机构" fileName="user" />" class="hidden addDiv"  >
						<input type="hidden" id="dId" value="" />
						<input type="hidden" id="opType" />
						<input type="hidden" name="oldCode" id="oldCode" value="" />
						<input type="hidden" name="level" id="level" value="" />
						<input type="hidden" name="superiorId" id="superDepId" value=""/>
						<input type="hidden" name="oldDepName" id="oldDepName" value=""/>
						<table class="jgmc_table">
							<tr><td class="jgmc_up_tr_td"></td></tr>
							<tr>
								<td class="jgmc_td">
									<emp:message key="user_xtgl_czygl_text_53" 
										defVal="机构名称：" fileName="user" />
<%--									<input type="hidden" name="depCodeThird" value="00000000000000" id="depCodeThird"  maxlength="32" width="270px"/>--%>
								</td>
								<td class="addDepName_td"><input type="text" name="addDepName" id="addDepName" maxlength="20" class="input_bd addDepName"  /><font class="font_red">&nbsp;*</font></td>
							</tr>
							<tr><td class="jgbm_up_tr_td"></td></tr>
							<tr>
								<td>
									<emp:message key="user_xtgl_czygl_text_54" 
										defVal="机构编码：" fileName="user" />
								</td>
								<td class="depCodeThird_td"><input type="text" name="depCodeThird" id="depCodeThird" 
								onkeyup= "if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')"
								 maxlength="32" class="input_bd depCodeThird"  /><font class="font_red">&nbsp;*</font></td>
							</tr>
							<%--
							<tr><td height="5px"></td></tr>
							<tr>
								<td>
									上级机构：
								</td>
								<td><input type="text" name="superDepName" id="superDepName" readonly="readonly" width="270px" value="" disabled="disabled"/></td>
							</tr>
							 --%>
							<tr><td class="jgzz_up_tr_td"></td></tr>
							<tr>
								<td class="jgzz_td">
									<emp:message key="user_xtgl_czygl_text_55" 
										defVal="机构职责：" fileName="user" />
								</td>
								<td><textarea name="addDepResp" id="addDepResp" class="addDepResp" maxlength="250"></textarea></td>
							</tr>
							<tr><td class="qd_up_tr_td"></td></tr>
							<tr><td colspan="2" align="center">
								<input type="button" onclick="sub()" class="btnClass5 mr23" id="subbtn" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />"/>
								<input type="button" onclick="doCancel();$('#addDiv').dialog('close');" class="btnClass6" value="<emp:message key="common_btn_16" 
										defVal="取消" fileName="common" />"/><br/>
							</td></tr>
						</table>
						
				</div>
			</div>
			<%} %>
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
				<%-- 国际化js加载 --%>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=inheritPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=inheritPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=inheritPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript" src="<%=iPath%>/js/optDepartment.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	</body>
</html>