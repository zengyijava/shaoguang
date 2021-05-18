<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.entity.engine.LfAutoreply" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
	
	LfAutoreply reply = new LfAutoreply();
	if(request.getAttribute("reply") != null){
		reply = (LfAutoreply)request.getAttribute("reply");
	}
	
	String lguserid=request.getParameter("lguserid");
	String lgcorpcode=request.getParameter("lgcorpcode");
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
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	</head>

	<body onload="show()" id="eng_moConf" class="eng_moConf">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
						<div id="detail">
						<form action="<%=path %>/eng_moService.htm?method=updateMoConf" method="post" id="serForm" name="serForm">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type ="hidden" name ="repid" id="repid" value="<%=reply.getId()==null?"":reply.getId() %>"/>
						
						<table>
						<thead>
							<tr><td class="explain"><emp:message key="znyq_ywgl_sxywgl_sm_mh" defVal="说明：" fileName="znyq"></emp:message></td>
							<td><emp:message key="znyq_ywgl_sxywgl_dqszdhfnr" defVal="当前设置的回复内容，适用于启用尾号的所有上行业务，当手机用户回复错误指令，系统则自动下发本设置内容。" fileName="znyq"></emp:message></td>
							</tr>
							<tr>
								<td class="replyTd">
									<emp:message key="znyq_ywgl_sxywgl_hfnr_mh" defVal="回复内容：" fileName="znyq"></emp:message>
								</td>
								<td >
								<textarea id="repContent" name="repContent" class="input_bd textarea_limit"><%
									if(reply.getReplycontent()!=null)
									{
										out.print(reply.getReplycontent());
									}
								%></textarea><font class="tipColor">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td class="stateTd">
									<span><emp:message key="znyq_ywgl_sxywgl_zt_mh" defVal="状态：" fileName="znyq"></emp:message></span>
								</td>
								<td>
								<%int rs = reply.getState()==null?1:reply.getState(); %>
									<input name="state" type="radio" value="1" <%=rs-1==0?"checked":"" %>/>
									<emp:message key="znyq_ywgl_sxywgl_qy" defVal="启用" fileName="znyq"></emp:message>
									<input class="stateIn" name="state" type="radio" value="2" <%=rs-2==0?"checked":"" %>/>
									<emp:message key="znyq_ywgl_sxywgl_jy" defVal="禁用" fileName="znyq"></emp:message>
								</td>

							</tr>
							<tr>
								<td colspan="2" id="btn">
									<input name="" type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="formSubmit" onclick="checkInput();" class="btnClass5 mr23"/>
									<input  type="button" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" onclick="closeSerInfodiv()" class="btnClass6"/>
									<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
									<br/>
								</td>
							</tr>
							</thead>
						</table>
						</form>
				</div>
				<div class="clear"></div>
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
    <div class="clear"></div>
    
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    	<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		
		function show(){
		<% String result=(String)request.getAttribute("moConfResult");
				if(result!=null && "true" == result){%>
				//alert("操作成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czcg"));
			<%}else if(result!=null && "false" == result){%>
				//alert("操作失败！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czsb"));	
		    <%  }%>
		    <%if(result!=null){%>
			window.parent.location.href="<%=path%>/eng_moService.htm?method=find&lguserid="+$("#lguserid").val();
		    <%}%>
		}
		
		function closeSerInfodiv()
		{
			window.parent.closeMoConfDiv();
		}
		
		function checkInput(){
			var repContent = $("#repContent").val();
			if(repContent == null || $.trim(repContent).length == 0){
				//alert("回复内容不能为空。");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_hfnrbnwk"));
				return false;
			}else if(repContent.length > 512){
				//alert("回复内容超过512个字符。");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_hfnrcg512gzf"));
				return false;
			}
			
			//if (confirm("确定提交吗？")) {
			if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_qdtjm"))) {
					//$("#serForm").attr("action",$("#serForm").attr("action")+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$("#lgcorpcode").val());
					$("#serForm").submit();
			}
		}
		
    </script>
		
	</body>
</html>
