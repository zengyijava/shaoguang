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
	
	@ SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>)request.getAttribute("spUserList");
	LfService ls = (LfService)request.getAttribute("ls");
	if (ls == null)
	{
		ls = new LfService();
	}
	
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	
	String prId=(String)request.getAttribute("prId");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	int orderTypeI = ls.getOrderType()==null?1:ls.getOrderType();
	String orderTypeStr=String.valueOf(orderTypeI);
	
	int  identifyModeI = ls.getIdentifyMode()==null?1:ls.getIdentifyMode();
	String identifyModeStr=String.valueOf(identifyModeI);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
	
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<script>
	var orderTypeStr=<%=orderTypeStr%>;
	var identifyModeStr=<%=identifyModeStr%>;
	</script>
	</head>
	
	
	<body onload="show()" id="eng_addMo1" class="eng_addMo1">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"步骤管理") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_bzgl",request)) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="eng_moService.htm?method=update" method="post" id="serForm">
				<input type="hidden" name="sendFlag" id="sendFlag" value="" /><%-- 通道号合法标记 --%>
				<input type="hidden" name="workType" id="workType" value="waterLine"/><%-- 表明是流水线新建流程 --%>
				<input type="hidden" name="serId" id="serId" value="<%=serId==null?"":serId %>"/><%-- 表明是新建还是更新 --%>
				<input type="hidden" name="prId" id="prId" value="<%=prId %>"/>
				
				<div id="hiddenValueDiv"></div>
				<div class="div1">
					<table id="location" class="linebgimg1">
						<tr><td><b>1.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjsxyw" defVal="新建上行业务" fileName="znyq"></emp:message></td><td>
						<b>2.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjbz" defVal="新建步骤" fileName="znyq"></emp:message></td><td>
						<b>3.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_sdpzbz" defVal="手动配置步骤" fileName="znyq"></emp:message></td>
						<td><b>4.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message></td></tr>
					</table>
				</div>
				<div class="itemDiv" id="item1">
				<span class="righttitle" id="serNameTd"><emp:message key="znyq_ywgl_sxywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></span>
				<table class="table1"><tr>
				
				<td class="td1">
				<input type="hidden" id="hserName" value='<%=ls.getSerName()==null?"":ls.getSerName() %>' />
				<input class="graytext input_bd" type="text" name="serName" id="serName" value='<%=ls.getSerName()==null?"":ls.getSerName() %>' maxlength="20"/>
				</td>
				<td class="td2">
				<font class="tipColor">&nbsp;*</font>
				</td>
				</tr></table>
				</div>
				
				<div class="itemDiv">
					<span class="righttitle"><emp:message key="znyq_ywgl_sxywgl_ywms_mh" defVal="业务描述：" fileName="znyq"></emp:message></span>
				<table><tr><td>
				<input type="text" class="input_bd" name="comments" id="comments" value='<%=ls.getCommnets()==null?"":ls.getCommnets() %>' maxlength="32"/>
				</td></tr></table>
				</div>
				
				<div class="itemDiv">
				<span class="righttitle" id="orderCodeTd"><emp:message key="znyq_ywgl_sxywgl_zldm_mh" defVal="指令代码：" fileName="znyq"></emp:message></span>
				<table class="table2"><tr><td>
				<input type="hidden" value='<%=ls.getOrderCode()==null?"":ls.getOrderCode() %>' id="curCode"/>
				<input class="graytext input_bd" type="text" name="orderCode" id="orderCode" value='<%=ls.getOrderCode()==null?"":ls.getOrderCode() %>' maxlength="10"/>
				</td><td id="tipColorTd"><font class="tipColor">&nbsp;*</font></td></tr></table>
				</div>
				
				<div class="itemDiv">
				<span class="righttitle" id="orderCodeTypeTd"><emp:message key="znyq_ywgl_sxywgl_pbfs_mh" defVal="匹配方式：" fileName="znyq"></emp:message></span>
				<%if(StaticValue.ZH_HK.equals(langName)){%>
				<table class="tableZH_HK">
				<%}else{%>
				<table class="tableZH">
				<%}%>
				<tr><td>
					<%int orderType = ls.getOrderType()==null?1:ls.getOrderType(); %>
					<input type="radio" name="orderType" value="1" <%=orderType==1?"checked":"" %> onclick="orderTypeclick(this)" />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_zlhcs" defVal="指令和参数" fileName="znyq"></emp:message>
					<input type="radio" name="orderType" id="orderTypeIn" value="0" <%=orderType==0?"checked":"" %> onclick="orderTypeclick(this)" />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_jzl" defVal="仅指令" fileName="znyq"></emp:message>
				</td></tr></table>
				</div>
				
				<div class="itemDiv" id="splDiv" class="<%=orderType==0?"hiddenValueDiv":"displayisempty" %>" >
				<span class="righttitle" id="splStr"><emp:message key="znyq_ywgl_sxywgl_fgfh_mh" defVal="分隔符号：" fileName="znyq"></emp:message></span>
				<table class="table3"><tr><td>
				<select name="msgSeparated" id="msgSeparated"  class="input_bd">
					<%String separate=ls.getMsgSeparated()==null?"":ls.getMsgSeparated(); %>
					<option value="#">#</option>
					<option value="*" <%="*".equals(separate)?"selected":"" %>>*</option>
					<option value="+" <%="+".equals(separate)?"selected":"" %>>+</option>
				</select>
				</td></tr></table>
				</div>
				
				<div class="itemDiv div_bg">
				<table id="moreItem" class="moreItem"><tr>
				<td class="firsttd"><emp:message key="znyq_ywgl_sxywgl_ywlx_mh" defVal="业务类型：" fileName="znyq"></emp:message></td>
				<td>
				<select class="input_bd" id="busCode" name="busCode">
				<%
				if (busList != null && busList.size() > 0)
				{
					for(LfBusManager busManager : busList)
					{
				%>
						<option value="<%=busManager.getBusCode() %>"  
						<%=busManager.getBusCode().equals(ls.getBusCode())?"selected":""%>>
						<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") + "("+busManager.getBusCode()+")" %>
						</option>
				<%  }
				}													
				%>
			    </select>
				</td>
				</tr>
				<tr>
				<td>
				<%--<%=StaticValue.SMSACCOUNT %>：--%>
				<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message>
				</td>
				<td>
				<%
					if(request.getAttribute("staffName")==null ||request.getAttribute("staffName").equals(""))
					{
					%>
					<select class="input_bd" id="spUser" name="spUser" onchange="getGateNumber()">
			        <%
						if (spUserList != null && spUserList.size() > 0)
						{
							for(Userdata userdata : spUserList)
							{
					%>
								<option value="<%=userdata.getUserId()%>" <%=userdata.getUserId().equals(ls.getSpUser())?"selected":""%>>
									<%=userdata.getUserId()+"("+userdata.getStaffName()+")"%>
								</option>
					<%}
							}
					%>
			        </select>
			        <%
					}else
					{
					%>
						<select class="input_bd" id="spUser" name="spUser"  disabled="disabled">
								<option value="<%=ls.getSpUser()%>">
									<%=ls.getSpUser()+"("+(String)request.getAttribute("staffName")+")"%>
								</option>
			            </select>
					<%
					}
					%>
				</td>
				</tr>
				<tr>
				<td class="secondtd">
				<emp:message key="znyq_ywgl_sxywgl_whzt_mh" defVal="尾号状态：" fileName="znyq"></emp:message>
				</td>
				<td>
					<%int identifyMode = ls.getIdentifyMode()==null?1:ls.getIdentifyMode(); %>
					<input type="radio" name="identifyMode" id="idfModeOpen" value="1" onclick="idfModeclick(this)" <%=identifyMode==1?"checked":"" %> />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_qy" defVal="启用" fileName="znyq"></emp:message>
					<input type="radio" name="identifyMode" id="idfModeClose" value="2" onclick="idfModeclick(this)" <%=identifyMode==2?"checked":"" %>/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_ty" defVal="停用" fileName="znyq"></emp:message>
				</td>
				<td class="thirdtd">
					<span id="spSubno" class="<%=identifyMode==2?"hiddenValueDiv":"displayisempty" %>"><emp:message key="znyq_ywgl_sxywgl_wh_mh" defVal="尾号：" fileName="znyq"></emp:message> <label id="subno" ></label></span>
				</td>
				</tr>
				<tr>
				<td><emp:message key="znyq_ywgl_sxywgl_yxzt_mh" defVal="运行状态：" fileName="znyq"></emp:message></td>
				<td>
				<%int rs = ls.getRunState()==null?2:ls.getRunState(); %>
				<input name="runState" type="radio" value="1" <%=rs-0!=0?"checked":"" %>/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_yx" defVal="运行" fileName="znyq"></emp:message>
				<input class="runState" name="runState" type="radio" value="0" <%=rs-0==0?"checked":"" %>  />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_jy" defVal="禁用" fileName="znyq"></emp:message>
				</td>
				</tr>
				</table>
				</div>
				
				<div class="div2">
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_17" defVal="下一步" fileName="znyq"></emp:message>" class="btnClass5 mr23 indent_none" onclick="javascript:serSub();" />
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" class="btnClass6" onclick="javascript:location.href='<%=path %>/eng_moService.htm?method=find&lguserid='+$('#lguserid').val()" />
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_addMo1.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	</body>
</html>