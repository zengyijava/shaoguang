<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfTructType"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.AcmdRoute"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.pasgroup.vo.UserPropertyVo"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	


@SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

@ SuppressWarnings("unchecked")
UserPropertyVo upvo = (UserPropertyVo)request.getAttribute("upvo");
String keyId = (String)request.getAttribute("keyId");

String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));

String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title>https设置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/frame/frame3.0/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
              type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/frame/frame3.0/skin/default/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mwp_editHttps.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mwp_editHttps.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="mwp_editHttps" onload="show()">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<center>
					<div >
						<form action="" method="post" name="bindform" id="bindform">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						<input type="hidden" id="keyId" name="keyId" value="<%=keyId %>"/>
						<input type="hidden" id="userid" name="userid" value="<%=upvo.getUserid() %>"/>
						<table id="editHttpsTable" class="editHttpsTable">
							<thead>	
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>								
								<tr>
									<td>
										<emp:message key='txgl_mwpasgroup_text_26' defVal='服务端证书校验：' fileName='mwadmin'/>
									</td>
									<td>
									<%
									String verifypeer=upvo.getVerifypeer()==null?"1":upvo.getVerifypeer().trim();
									String cacertname= upvo.getCacertname()==null?"":upvo.getCacertname().trim();
									String verifyhost=upvo.getVerifyhost()==null?"1":upvo.getVerifyhost().trim();
									String gstype="0";
									if("1".equals(verifypeer)){ 
										gstype="0";
										cacertname="";
										verifyhost="1";
									}else{
										if("".equals(cacertname)){
											gstype="0";
										}else{
											gstype="1";
										}
									}
									 %>
									<select id="verifypeer" name="verifypeer"  class="input_bd verifypeer" onchange="switchverifypeer(this.value)">
									   <option value="1" <%if("1".equals(verifypeer)){ %> selected="selected" <%} %> ><emp:message key='txgl_mwpasgroup_text_27' defVal='不校验' fileName='mwadmin'/></option>
									   <option value="2" <%if("2".equals(verifypeer)){ %> selected="selected" <%} %> ><emp:message key='txgl_mwpasgroup_text_28' defVal='校验' fileName='mwadmin'/></option>
									</select>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>								
								<tr>
									<td>
										<emp:message key='txgl_mwpasgroup_text_29' defVal='服务端证书类型：' fileName='mwadmin'/>
									</td>
									<td>
									<select id="gstype" name="gstype"  class="input_bd gstype" <%if("1".equals(verifypeer)){ %> disabled="disabled" <%} %> onchange="switchgstype(this.value)">
									   <option value="0" <%if("0".equals(gstype)){ %> selected="selected" <%} %>><emp:message key='txgl_mwpasgroup_text_30' defVal='公共' fileName='mwadmin'/></option>
									   <option value="1" <%if("1".equals(gstype)){ %> selected="selected" <%} %>><emp:message key='txgl_mwpasgroup_text_31' defVal='私有' fileName='mwadmin'/></option>
									</select>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_mwpasgroup_text_32' defVal='服务端证书：' fileName='mwadmin'/>
									</td>
									<td>
									 <input type="text" <%if("0".equals(gstype)){ %> disabled="disabled" <%} %> id="cacertname" name="cacertname"  class="input_bd cacertname" value="<%=cacertname.replace("\"","&#34;").replace("<","&#60;").replace(">","&#62;") %>" maxlength="128"/>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_mwpasgroup_text_33' defVal='证书校验方式：' fileName='mwadmin'/>
									</td>
									<td>								
									<select id="verifyhost" name="verifyhost"  class="input_bd verifyhost" <%if("1".equals(verifypeer)){ %> disabled="disabled" <%} %>>
									   <option value="1" <%if("1".equals(verifyhost)){ %> selected="selected" <%} %>><emp:message key='txgl_mwpasgroup_text_34' defVal='请选择' fileName='mwadmin'/></option>
									   <option value="2" <%if("2".equals(verifyhost)){ %> selected="selected" <%} %>><emp:message key='txgl_mwpasgroup_text_35' defVal='检查证书中是否有CN(common name)字段' fileName='mwadmin'/></option>
									   <option value="3" <%if("3".equals(verifyhost)){ %> selected="selected" <%} %>><emp:message key='txgl_mwpasgroup_text_36' defVal='校验当前的域名是否与common name匹配' fileName='mwadmin'/></option>
									</select>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td id="btn" colspan="2" class="btn">
										<input name="sure" type="button" id="subBut" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass5 mr23"/>
										<input name="caual" type="button" onclick="javascript:window.parent.closeEditHttpsdiv();" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" class="btnClass6"/>
										<br/>
									</td>
								</tr>
								</thead>
							</table>
						</form>
					</div>
				</center>
			</div>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=commonPath%>/txgl/mwpasgroup/js/mwedithttps.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">

	$(document).ready(function() {
		getLoginInfo("#hiddenValueDiv");
		noquot("#tmName");
	});
	
	function show()
	{
	<% 
		String result=(String)request.getAttribute("tmresult");
		if(result!=null && result.equals("true"))
		{
	%>
        /*新建成功！*/
        alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_17"));
	<%
		}
		else if(result!=null && result.equals("error"))
		{
	%>
        /*操作失败！*/
        alert(getJsLocaleMessage("common","common_operateFailed"));
	<%
		}
		request.removeAttribute("tmresult");
		if (result != null)
		{
	%>
		    window.parent.location.href="<%=path %>/ser_amoServiceBind.htm?method=find&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
	<%
		}
	%>
		
	}

	
	</script>
	</body>
</html>
