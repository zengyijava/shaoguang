<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfFlowBindObj" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfFlow" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("auditpro");
	menuCode = menuCode==null?"0-0-0":menuCode;
	//当前用户ID
	String lguserid = (String)request.getAttribute("lguserid");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	//审核流程ID
	String flowid = (String)request.getAttribute("flowid");
	LfFlow lfflow = (LfFlow)request.getAttribute("lfflow");
	//判断是 1 创建流程界面操作下来的还是  2 完成界面中上一步操作
	String pathtype = (String)request.getAttribute("pathtype");
	String optionmsg = (String)request.getAttribute("optionmsg");
	
	//审核范围
	//String bindtypebuffer = (String)request.getAttribute("bindtypebuffer");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" />
	    <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/aud_addAudit2.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/aud_addAudit2.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="aud_addAudit2">

		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_cjshlc",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="aud_auditpro.htm?method=toInstallObj?pathtype=<%=pathtype %>" method="post" id="serForm">
					<input type="hidden" id="pathUrl" value="<%=path %>"/>
					<input type="hidden" id="ipath" value="<%=iPath %>"/>
					<input type="hidden" id="lguserid" value="<%=lguserid %>"/>
					<input type="hidden" id="flowid" value="<%=flowid %>"/>
					<%-- 判断是 1 创建流程界面操作下来的还是  2 完成界面中上一步操作--%>
					<input type="hidden" id="pathtype" value="<%=pathtype %>"/>
					
					<%-- 标认界面有几个div   默认有一个--%>
					<input type="hidden" id="divlevel" name="divlevel" value="1"/>
					<div id="getloginUser" class="getloginUser">
					</div>
					
					
					
					<div id="optionstr" class="optionstr">
						<xmp>
								<%
									if(pathtype != null && "2".equals(pathtype)){
										if(optionmsg != null && !"".equals(optionmsg)){
											out.print(optionmsg);
										}
									}
								%>
						</xmp>
					</div>
					
				<div class="serForm_div1">
					<table  class="audLinebgimg2 serForm_div1_table" >
						<tr>
							<td class="serForm_div1_table_td1"></td>
							<td class="serForm_div1_table_td2"><b>1.</b><emp:message key="xtgl_spgl_shlcgl_cjshlc" defVal="创建审核流程" fileName="xtgl"/></td>
							<td class="serForm_div1_table_td3"><b>2.</b><emp:message key="xtgl_spgl_shlcgl_szbshdx" defVal="设置被审核对象" fileName="xtgl"/></td>
							<td class="serForm_div1_table_td4"><b>3.</b><emp:message key="xtgl_spgl_shlcgl_wc" defVal="完成" fileName="xtgl"/></td>
						</tr>
					</table>
				</div>
				
				<div class="itemDiv div_bg div_bd serForm_div2" >
					<div class="div_bd title_bg serForm_div2_div"  align="left">
						&nbsp;&nbsp;&nbsp;<emp:message key="xtgl_spgl_shlcgl_shlcmc" defVal="审核流程名称" fileName="xtgl"/>
							<% 
								if(lfflow != null){
									out.print(lfflow.getFTask() + "  (" +lfflow.getFId()+")" );
								}else{
									out.print( "  (" +flowid+")" );
								}
							%>
					</div>
					<center>
					<iframe id="flowFrame" name="flowFrame" src="<%=iPath%>/aud_installSelObj.jsp?lguserid=<%=lguserid %>&pathtype=<%=pathtype %>&lgcorpcode=<%=lgcorpcode %>&flowid=<%=flowid %>"  
						class="flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
					</center>
				</div>
				
				<div class="itemDiv serForm_div3" >
					<table class="serForm_div3_table">
					   		<tr>
								<td align="right">
										<input type="button"  id="upbtn"  value="<emp:message key='xtgl_spgl_shlcgl_syb' defVal='上一步' fileName='xtgl'/>"  class="btnClass5 mr10 btnLetter3 indent_none" onclick="javascript:backUpdateAudit();" />&nbsp;&nbsp;
										<input type="button"  id="nextbtn"  value="<emp:message key='xtgl_spgl_shlcgl_xyb' defVal='下一步' fileName='xtgl'/>"  class="btnClass5 mr10 btnLetter3 indent_none" onclick="javascript:nextsaveobj(1);" />&nbsp;&nbsp;
										<input type="button"  id="savebtn"  value="<emp:message key='xtgl_spgl_shlcgl_bc' defVal='保存' fileName='xtgl'/>" class="btnClass6 mr10" onclick="javascript:nextsaveobj(2);" />&nbsp;&nbsp;
										<input type="button"  id="cancelbtn"  value="<emp:message key='xtgl_spgl_shlcgl_fh' defVal='返回' fileName='xtgl'/>" class="btnClass6" onclick="javascript:cancelAudit()" />
										<br/>
								</td>
							</tr>
					</table>
				</div>
			</form>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addAudit2.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>