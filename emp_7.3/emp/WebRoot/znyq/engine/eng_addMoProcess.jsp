<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
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
	LfSysuser curSysuser = (LfSysuser) session.getAttribute("loginSysuser");
			
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	String serId = (String)request.getAttribute("serId");
	LfProcess lp = (LfProcess)request.getAttribute("process");
	if (lp == null)
	{
		lp = new LfProcess();
	}
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
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
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	</head>

	<body onload="show()" id="eng_addMoProcess" class="eng_addMoProcess">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container">
			<%--
				<div id="u_o_c_explain">
				<p>
						说明：新建处理步骤信息
					</p>
					<ul>
						<li>
							步骤名称：为数据库处理步骤的名称
						</li>
						<%--<li>
							指令代码：手机终端发送的代码，用于平台查找手机终端所要请求的业务的处理步骤，一个指令代码对应一个业务的处理步骤。上行短信中第一个短信分隔符前面的内容平台均会识别为指令代码
						</li>
						<li>
							步骤类型：指定步骤所属执行类型。共有5种类型，分别是：
						</li>
						<li>&nbsp;&nbsp;&nbsp;&nbsp;Insert&nbsp;&nbsp;----表示本步骤处理的是一个数据库Insert语句</li>
						<li>&nbsp;&nbsp;&nbsp;&nbsp;Delete&nbsp;&nbsp;----表示本步骤处理的是一个数据库Delete语句</li>
						<li>&nbsp;&nbsp;&nbsp;&nbsp;Update&nbsp;----表示本步骤处理的是一个数据库Update语句</li>
						<li>&nbsp;&nbsp;&nbsp;&nbsp;Select&nbsp;&nbsp;----表示本步骤处理的是一个数据库Select语句</li>
						<li>&nbsp;&nbsp;&nbsp;&nbsp;Reply&nbsp;&nbsp;----表示本步骤是用来设置回复短信的格式</li>
						<li>
							步骤是否最后处理步骤：如果设置本步骤为最后处理步骤，则当系统执行完本处理步骤后，将不执行后面定义的处理步骤
						</li>
					</ul>
			</div>
			 --%>
			<div id="detail">
					<form method="post" action="<%=path %>/eng_moService.htm?method=updatePro" id="proform">
					<input type="hidden" id="prId" name="prId" value='<%=lp.getPrId()==null?"":lp.getPrId() %>' />
					<input type="hidden" id="lguserid" name="lguserid" value="<%=lguserid %>"/>
					<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=lgcorpcode %>"/>
						<table>
						<thead>
							<tr>
								<td class="eleTd">
									<span><emp:message key="znyq_ywgl_sxywgl_bzmc_mh" defVal="步骤名称：" fileName="znyq"></emp:message></span>
								</td>
								<td>
								<input type="hidden" id="hprName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' />
									<input type="text"  class="input_bd" name="prName" id="prName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' maxlength="20" /><font class="tipColor">&nbsp;*</font>
									<input type="hidden" name="serId" value="<%=serId %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_sxywgl_bzms_mh" defVal="步骤描述：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input class="input_bd"  type="text" name="comments" id="comments" value='<%=lp.getComments()==null?"":lp.getComments() %>' maxlength="32"/>
								</td>
							</tr>
							<tr>
								<td >
									<span><emp:message key="znyq_ywgl_sxywgl_bzlx_mh" defVal="步骤类型：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<select  class="input_bd" name="prType" id="select">
		                                <%int pt = lp.getPrType()==null?0:lp.getPrType(); %>
		                                <option value="4" <%if(pt-4==0){out.print("selected=\"selected\"");}%> >
											Select
										</option>
										<option value="5" <%if(pt-5==0){out.print("selected=\"selected\"");}%>>
											Reply
										</option>
										<option value="1" <%if(pt-1==0){out.print("selected=\"selected\"");}%> >
											Insert
										</option>
										<option value="2" <%if(pt-2==0){out.print("selected=\"selected\"");}%> >
											Delete
										</option>
										<option value="3" <%if(pt-3==0){out.print("selected=\"selected\"");}%> >
											Update
										</option>
									</select><font class="tipColor">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td >
									<span><emp:message key="znyq_ywgl_sxywgl_sfwzhbz_mh" defVal="是否为最后步骤：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<% int ps = lp.getFinalState()==null?2:lp.getFinalState(); %>
									<input name="finalState" type="radio" value="1" <%=ps-1==0?"checked":"" %>/>
									<em id="y"><emp:message key="znyq_ywgl_sxywgl_s" defVal="是" fileName="znyq"></emp:message></em>
									<input class="finalState" name="finalState" type="radio" value="0" <%=ps-1!=0?"checked":"" %>/>
									<em id="n"><emp:message key="znyq_ywgl_sxywgl_f" defVal="否" fileName="znyq"></emp:message></em>
								</td>
							</tr>
							<tr>
								<td colspan="2" id="btn">
									<input id="addBasicInfoBtn" type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" onclick="javascript:processOk()" class="btnClass5 mr23"/>
									<input type="button" name="previous" id="previous" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" onclick="javascript:window.parent.closeDiv()" class="btnClass6"/>
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
	<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	
	<script type="text/javascript">

		$(document).ready(function(){
			noquot("#prName");
			noquot("#comments");
		});
		function show(){
		<% String result=(String)request.getAttribute("result");
				if(result!=null && result == "true"){%>
				//alert("操作成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czcg"));
			<%}else if(result != null && result != "true") {%>
				//alert("操作失败！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czsb"));	
		    <%  }%>
		    <%if(result!=null){%>
			window.parent.location.href="<%=path%>/eng_moService.htm?method=process&serId=<%=serId%>";
		    <%}%>
		}
    </script>
	
	</body>
</html>
