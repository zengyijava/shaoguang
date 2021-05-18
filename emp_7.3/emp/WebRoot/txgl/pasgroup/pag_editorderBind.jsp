<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfTructType"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.AcmdRoute"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

@SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("moServiceBind");
menuCode = menuCode==null?"0-0-0":menuCode;

@ SuppressWarnings("unchecked")
AcmdRoute lfMoTructVo = (AcmdRoute)request.getAttribute("lfMoTructVo");
@SuppressWarnings("unchecked")
List<Userdata>  userList = (List<Userdata>)request.getAttribute("spUserList");
@SuppressWarnings("unchecked")
List<LfTructType>  tructlist = (List<LfTructType>)request.getAttribute("tructList");

String msType = (String)request.getAttribute("msType");
String keyId = (String)request.getAttribute("keyId");
String spId = (String)request.getAttribute("spId");
String txglFrame = skin.replace(commonPath, inheritPath);


String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

//确定
String qd = MessageUtils.extractMessage("common", "common_confirm", request);
//取消
String qx = MessageUtils.extractMessage("common", "common_cancel", request);

String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));


%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_zllypz_bjsxywzlbd" defVal="编辑上行业务指令绑定" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />

		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
			<style type="text/css">
				input#Structcode,input#name,input#Bussysname,select#Spid{  width: 200px;  }
				table#editServiceBindTable{width: 620px;text-align: left;}
				table#editServiceBindTable tr > td:nth-child(1){width: 150px;padding-left: 20px;}
				table#editServiceBindTable tr > td:nth-child(2){width: 220px;}
			</style>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pag_editorderBind.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pag_editorderBind.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		</head>

	<body id="pag_editorderBind" onload="show()">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<center>
					<div >
						<form action="<%=path %>/ser_amoServiceBind.htm?method=update" method="post" name="bindform" id="bindform">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						<input type="hidden" id="tructid" name="tructid" value="<%=lfMoTructVo.getId() %>"/>
						<input type="hidden" id="opType" name="opType" value="update"/>
						<input type="hidden" id="status" name="status" value="<%=lfMoTructVo.getStatus() %>"/>
						<input type="hidden" id="oldcode" name="oldcode" value="<%=lfMoTructVo.getStructcode() %>"/>
						<input type="hidden" id="keyId" name="keyId" value="<%=keyId %>"/>
						<table id="editServiceBindTable">
							<thead>	
								<tr>
									<td colspan="3">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td>
										<emp:message key="txgl_wgqdpz_zhtdpz_zldm" defVal="指令代码：" fileName="txgl"></emp:message>
									</td>
									<td>
									    <input type="text" id="Structcode" name="Structcode" class="input_bd" value="<%=lfMoTructVo.getStructcode() %>"/>
									</td>
									<td><span> <font color="red"><emp:message key="txgl_wgqdpz_zllypz_wzmktzmszjw" defVal="* 为字母开头,字母与数字组合,长度2-6位,#结尾" fileName="txgl"></emp:message></font></span></td>
								</tr>
								<tr>
									<td colspan="3">
										&nbsp;
									</td>							
								</tr>								
								<tr>
									<td>
										<emp:message key="txgl_wgqdpz_zllypz_zlmcc" defVal="指令名称：" fileName="txgl"></emp:message>
									</td>
									<td>
									    <input type="text" id="name" name="name" class="input_bd" value="<%=lfMoTructVo.getName() %>" maxlength="16"/>
									</td>
									<td><span> <font color="red">*</font></span></td>
								</tr>
								<tr>
									<td colspan="3">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td>
										<emp:message key="txgl_wgqdpz_zllypz_ywxtmc" defVal="业务系统名称：" fileName="txgl"></emp:message>
									</td>
									<td>
									    <input type="text" id="Bussysname" name="Bussysname" class="input_bd" value="<%=lfMoTructVo.getBussysname() %>" maxlength="85"/>
									</td>
									<td><span> <font color="red">*</font></span></td>
								</tr>
								<tr>
									<td colspan="3">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td>
										<emp:message key="txgl_wgqdpz_zllypz_spzhh" defVal="SP账号：" fileName="txgl"></emp:message>
									</td>
									<td>
										<select id="Spid" name="Spid" class="input_bd">
										   <option value=""><emp:message key="txgl_wgqdpz_qyhdgl_qxz" defVal="请选择" fileName="txgl"></emp:message></option>
										   <%
												if(msType==null||"".equals(msType)||"0".equals(msType)){
													if (userList != null && userList.size() > 0){
														for(Userdata vo :userList){
											%>
											  <option
														  <%if(vo.getUserId().equals(spId)){
											      			%>selected = "selected"
													  	  <%}
											  				%> value="<%=vo.getUid() %>"><%=vo.getUserId()%><%=vo.getAccouttype()==1 ? ":("+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_dxspzh", request)+")" : ":("+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_cxspzh", request)+")" %> </option>
												<%
												  }
												 }
												}%>

										</select>
									</td>
									<td><span> <font color="red">*</font></span></td>
								</tr>
								<tr>
									<td colspan="3">
										&nbsp;
									</td>
								</tr>

								<%-- <tr>
									<td>指令类型:</td>
									<td>
										<select id="tructtype" name="tructtype" disabled="disabled">
											<option value="">-请选择-</option>
											<%
												if(tructlist!=null && tructlist.size()>0){
													for(LfTructType truct : tructlist){
											 %>
											 	<option value="<%=truct.getType() %>" <%=truct.getType().equals(lfMoTructVo.getTructtype())?"selected":"" %> ><%=truct.getName() %></option>
											 <%
											 		}
											 	}
											  %>
										</select>
									</td>
								</tr>
								 --%>
								<tr>
									<td colspan="3">
										&nbsp;
									</td>							
								</tr>
								<tr>
									<td id="btn" colspan="3" class="btn">
										<input name="sure" type="button" id="subBut" value="<%=qd %>" class="btnClass5 mr23"/>
										<input name="caual" type="button" onclick="javascript:window.parent.closeEditServiceBinddiv();" value="<%=qx %>" class="btnClass6"/>
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
    <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=commonPath%>/txgl/pasgroup/js/orderbind.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=commonPath%>/txgl/gateway/js/ip.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	
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
			//alert("新建成功！");
			
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_17"));
	<%
		}
		else if(result!=null && result.equals("error"))
		{
	%>
			//alert("操作失败！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_18"));
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
