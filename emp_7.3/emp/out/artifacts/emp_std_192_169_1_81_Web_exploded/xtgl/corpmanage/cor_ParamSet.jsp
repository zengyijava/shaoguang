<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
    LfCorp corp =(LfCorp) request.getAttribute("corp");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("manager");
	String depName=(String)request.getAttribute("depName");
	String IsChargingValues = "0";
	if(corp != null && corp.getIsBalance() !=null)
	{
	  IsChargingValues = corp.getIsBalance().toString();
	}
	//企业状态  1启用  0禁用
	String corpState="1";
	if(corp != null&&corp.getCorpState()!=null)
	{
	   corpState=String.valueOf(corp.getCorpState());
	}
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String condcode=request.getParameter("condcode");
	String condname=request.getParameter("condname");
	String condaddr=request.getParameter("condaddr");
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
			
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	
	</head>
	<body id="cor_ParamSet">
	<input type="hidden" id="showZhezhao" value="false"/>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode,"新建/编辑企业") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode,"参数设置") %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2" >
			
					<%--
					<div class="titletop">
						<table class="titletop_table">
							<tr>
								<td class="titletop_td">
									新建/编辑企业
								</td>
								<td align="right">
									<span class="titletop_font" style="margin-right: 35px;" onclick="javascript:quxiao()">&larr;&nbsp;返回上一级</span>
								</td>
							</tr>
						</table>
					</div>
					--%>
					<div id="table_input" class="table_input">
					 <form action="cor_manager.htm?method=update" method="post" name="pageForm" id="pageForm"> 
							<input type="hidden" id="corpId" name="corpId" value="<%=null==corp.getCorpID()?"":corp.getCorpID() %>"/>
							<input type="hidden" id="ccode" name="ccode" value="<%=null==corp.getCorpCode()?"":corp.getCorpCode() %>"/>
							<input type="hidden" id="action" name="action" value="setParam">
							<input type="hidden" id="depName" name="depName" value='<%=null==depName?"":depName %>' maxlength="25"/>
							<input type="hidden" id="path" name="path" value="<%=path%>"/>
							
							<div>
							<div style="height: 30px; line-height: 30px;"><strong>富信状态报告设置</strong></div>
<%-- 							<div style="height: 30px; line-height: 30px;"><input type="checkbox" id="rpt" name="rpt" value="1" <%=3==corp.getRptflag()||1==corp.getRptflag()?"checked":"" %>><label for="rpt">需要短信通知状态报告及报表汇总数据</label></div> --%>
							<div style="height: 30px; line-height: 30px;"><input type="checkbox" id="rpt" name="rpt" value="1" style="cursor: not-allowed;" onclick="javascript:return false;" checked="checked"><label for="rpt">需要短信通知状态报告及报表汇总数据</label></div>
							<div style="height: 30px; line-height: 30px;"><input type="checkbox" id="down" name="down" value="1" <%=3==corp.getRptflag()||2==corp.getRptflag()?"checked":"" %>><label for="down">需要手机终端下载状态报告及报表汇总数据</label></div>
							</div>
							<div class="buttonDiv" style="margin-top: 20px;">
									<input type="button" value="<emp:message key="xtgl_qygl_qd" defVal="确定" fileName="xtgl"></emp:message>" id="button" onclick="sub()" class="btnClass5"/>
									<input type="button" name="button" id="button" style="margin-left: 10px;" value="<emp:message key="xtgl_qygl_fh" defVal="返回" fileName="xtgl"></emp:message>" onclick="javascript:quxiao()" 
								class="btnClass6"/>
								<br />
							</div>
							  </form>
							</div>
							<div class="clear"></div>
						

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
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript">
	
		function sub()
		{			   
		    $("#button").attr("disabled","true");
	        $("#pageForm").submit();
		}
		
		
		 function quxiao()
		 {
			 var url = encodeURI("<%=path%>/cor_manager.htm?method=find"+
							"&code=<%=null==condcode?"":condcode %>&condcorpname=<%=null==condname?"":condname %>&addr=<%=condaddr==null?"":condaddr%>");
		 		window.location.href = url;
		 }
		</script>
	</body>
</html>
