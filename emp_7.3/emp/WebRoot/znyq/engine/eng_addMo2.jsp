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
	
	LfProcess lp = (LfProcess)request.getAttribute("process");
	if (lp == null)
	{
		lp = new LfProcess();
	}
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
	<body id="eng_addMo2" class="eng_addMo2">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"步骤管理") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_bzgl",request)) %>
		
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="<%=path%>/eng_moService.htm?method=updatePro" method="post" id="proform">
			
				<input type="hidden" id="prId" name="prId" value='<%=lp.getPrId()==null?"":lp.getPrId() %>' />
				<input type="hidden" id="serId" name="serId" value='<%=serId%>' />
				<input type="hidden" id="workType" name="workType" value="waterLine"/>
				<input type="hidden" id="finalState" name="finalState" value="0"/><%-- 默认设为不是最终步骤 --%>
				
				<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
				<div class="firstDiv">
					<table id="location" class="linebgimg2">
						<tr><td><b>1.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjsxyw" defVal="新建上行业务" fileName="znyq"></emp:message></td><td>
						<b>2.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjbz" defVal="新建步骤" fileName="znyq"></emp:message></td><td>
						<b>3.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_sdpzbz" defVal="手动配置步骤" fileName="znyq"></emp:message></td><td>
						<b>4.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message></td></tr>
					</table>
				</div>
				<div class="itemDiv" id="item1">
				<span class="righttitle" id="serNameTd"><emp:message key="znyq_ywgl_sxywgl_bzmc_mh" defVal="步骤名称：" fileName="znyq"></emp:message></span>
				<table class="firstTable"><tr><td class="firstTd">
				<input type="hidden" id="hprName" name="hprName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' />
				<input class="graytext input_bd" type="text" name="prName" id="prName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' maxlength="20"/>
				</td><td class="secondTd"><font class="tipColor">&nbsp;*</font></td></tr></table>
				</div>
				
				<div class="itemDiv">
				<span class="righttitle"><emp:message key="znyq_ywgl_sxywgl_ms_mh" defVal="描述：" fileName="znyq"></emp:message></span>
				<table class="secondTable"><tr><td>
				<input class="input_bd"  type="text" name="comments" id="comments" value='<%=lp.getComments()==null?"":lp.getComments() %>' maxlength="32"/>
				</td></tr></table>
				</div>
				
				<div class="itemDiv">
				<span class="righttitle"><emp:message key="znyq_ywgl_sxywgl_lx_mh" defVal="类型：" fileName="znyq"></emp:message></span>
				<table class="thirdTable"><tr><td>
				<select name="prType"  class="input_bd" >
					<%int pt = lp.getPrType()==null?0:lp.getPrType(); %>
					<option value="4" <%if(pt-4==0){out.print("selected=\"selected\"");}%> >Select</option>
					<option value="5" <%if(pt-5==0){out.print("selected=\"selected\"");}%>>Reply</option>
					<option value="1" <%if(pt-1==0){out.print("selected=\"selected\"");}%> >Insert</option>
					<option value="2" <%if(pt-2==0){out.print("selected=\"selected\"");}%> >Delete</option>
					<option value="3" <%if(pt-3==0){out.print("selected=\"selected\"");}%> >Update</option>
				</select>
				</td><td id="thirdTableTd"><font class="tipColor">&nbsp;*</font></td></tr></table>
				</div>
				
				<div class="secondDiv">
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_17" defVal="下一步" fileName="znyq"></emp:message>" class="btnClass5 mr23 indent_none"  onclick="javascript:processOk2()" />
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_23" defVal="上一步" fileName="znyq"></emp:message>" class="btnClass6 indent_none"  onclick="goLast()" />
				<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
				<br/>
				</div>
			</form>
			</div>
			<iframe id="ifr" class="ifr"></iframe>
			<div id="id2" class="remindMessage"></div>
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
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			//floatingRemind("serNameTd","只对应一个业务逻辑");
			floatingRemind("serNameTd",getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_zdyygywlj"));
		});
		function goLast()
		{
			var prId = $("#prId").val();
			var lguserid =$('#lguserid').val();
			var lgcorpcode=$('#lgcorpcode').val();
			location.href="<%=path%>/eng_moService.htm?method=doEdit&prId="+prId+"&workType=waterLine&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&serId=<%=serId %>";
		}
		</script>
		
	</body>
</html>