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
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//String serId = (String)request.getAttribute("serId");
	//业务id
	String serId = request.getParameter("serId");
	@ SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>)request.getAttribute("spUserList");
	LfService ls = (LfService)request.getAttribute("service");
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	@ SuppressWarnings("unchecked")
	List<LfSysuser> sysUserList = (List<LfSysuser>)request.getAttribute("sysuserList");
	LfSysuser curSysuser = (LfSysuser)request.getAttribute("curSysuser");
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
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	</head>

	<body id="eng_addMt1" class="eng_addMt1">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"步骤管理") %>--%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_bzgl",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="<%=path %>/eng_mtService.htm?method=add" method="post" id="serForm">
				<input type="hidden" name="sendFlag" id="sendFlag" value="" /><%-- 通道号合法标记 --%>
				<input type="hidden" name="workType" id="workType" value="waterLine"/><%-- 表明是流水线新建流程 --%>
				<input type="hidden" name="serId" id="serId" value="<%=serId==null?"":serId %>"/><%-- 表明是新建还是更新 --%>
				<input type="hidden" name="prId" id="prId" value="<%=request.getAttribute("prId")==null?"":request.getAttribute("prId") %>" />
				<input type="hidden" name="repPrId" id="repPrId" value="<%=request.getAttribute("repPrId")==null?"":request.getAttribute("repPrId") %>" />
				
				<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
				<div class="linediv">
					<table class="linebgimg1">
						<tr>
						<td class="linebgth1"><b>1.</b><emp:message key="znyq_ywgl_xhywgl_xjyw" defVal="新建业务" fileName="znyq"></emp:message></td>
						<td class="linebgth2"><b>2.</b><emp:message key="znyq_ywgl_xhywgl_xjsleletbz" defVal="新建select步骤" fileName="znyq"></emp:message></td>
						<td class="linebgth3"><b>3.</b><emp:message key="znyq_ywgl_xhywgl_xjreplybz" defVal="新建reply步骤" fileName="znyq"></emp:message></td>
						<td class="linebgth4"><b>4.</b><emp:message key="znyq_ywgl_xhywgl_szfssj" defVal="设置发送时间" fileName="znyq"></emp:message></td>
						<td class="linebgth5"><b>5.</b><emp:message key="znyq_ywgl_xhywgl_wc" defVal="完成" fileName="znyq"></emp:message></td>
						</tr>
					</table>
				</div>
				<div class="itemDiv" id="item1">
				<span id="spanSerName" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></span>
				<table><tr>
				
				<td class="input_bd">
				<input type="hidden" id="hserName" value='<%=ls.getSerName()==null?"":ls.getSerName() %>' />
				<input type="text" name="serName" id="serName" maxlength="20" value='<%=ls.getSerName()==null?"":ls.getSerName() %>'/>
				</td>
				
				<td class="zeroBorder">
				<font class="tipColor">&nbsp;*</font>
				</td>
				
				</tr></table>
				</div>
				
				<div class="itemDiv" id="item2">
					<span class="righttitle"><emp:message key="znyq_ywgl_xhywgl_ywms_mh" defVal="业务描述：" fileName="znyq"></emp:message></span>
				<table><tr><td class="input_bd">
				<input type="text" name="comments" id="comments" value='<%=ls.getCommnets()==null?"":ls.getCommnets() %>' maxlength="32"/>
				</td></tr></table>
				</div>
				
				<div class="itemDiv" id="item3">
				<span><b><emp:message key="znyq_ywgl_xhywgl_yyz_mh" defVal="拥有者：" fileName="znyq"></emp:message></b></span>
				<table class="ownerTable"><tr><td>
				<input type="hidden" name="ownerList" value="<%=ls.getOwnerId()==null?curSysuser.getUserId():ls.getOwnerId() %>"/>
				<%-- <select id="ownerList" name="ownerList" style="width: 203px" onchange="getUserDate()">
					<option value="<%=curSysuser.getUserId()%>"><%=curSysuser.getName()%>（<%=curSysuser.getUserName() %>）</option>
					<% if (sysUserList != null && sysUserList.size() > 0)
							   {
							       for (LfSysuser lfu : sysUserList)
							       {
							           if (!curSysuser.getUserName().equals(lfu.getUserName()))
							           {
							 %>
			            		 <option value="<%=lfu.getUserId() %>"><%=lfu.getName()%>（<%=lfu.getUserName() %>）</option>
			            	 <%
			            	         }
			            	     }
			            	 }
			            	  %>
			    </select> --%>
				</td><td class="zeroBorder"><font class="tipColor">&nbsp;*</font></td></tr></table>
				</div>
				
				<div class="itemDiv div_bg">
				<table id="moreItem"><tr class="fourHTr">
				<td><emp:message key="znyq_ywgl_xhywgl_ywlx_mh" defVal="业务类型：" fileName="znyq"></emp:message></td>
				<td>
				<div class="input_bd" id="busCodeDiv">
				<select id="busCode" name="busCode">
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
			    </div>
				</td>
				</tr>
				<tr class="fourHTr">
				<td>
				<%--<%=StaticValue.SMSACCOUNT %>：--%>
				<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message>
				
				
				</td>
				<td class="lhteTd">
				<div class="input_bd" id="spUserDiv">
				<%
					if(request.getAttribute("staffName")==null ||request.getAttribute("staffName").equals(""))
					{
					%>
					<select id="spUser" name="spUser" onchange="getGateNumber()">
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
						<select id="spUser" name="spUser" disabled="disabled">
								<option value="<%=ls.getSpUser()%>">
									<%=ls.getSpUser()+"("+(String)request.getAttribute("staffName")+")"%>
								</option>
			            </select>
					<%
					}
					%>
					</div>
					<font class="tipColor">&nbsp;*</font>
				</td>
				</tr>
				<tr class="fourHTr">
					<td>
					<emp:message key="znyq_ywgl_xhywgl_whzt_mh" defVal="尾号状态：" fileName="znyq"></emp:message>
					</td>
					<td>
						<%int identifyMode = ls.getIdentifyMode()==null || ls.getIdentifyMode()==0?2:ls.getIdentifyMode(); %>
						<input type="radio" name="identifyMode" id="idfModeClose" value="2" onclick="idfModeclick(this)" <%=identifyMode==2?"checked":"" %> />&nbsp;&nbsp;<emp:message key="znyq_ywgl_xhywgl_ty" defVal="停用" fileName="znyq"></emp:message>
						<input type="radio" name="identifyMode" id="idfModeOpen" value="1" onclick="idfModeclick(this)" <%=identifyMode==1?"checked":"" %> />&nbsp;&nbsp;<emp:message key="znyq_ywgl_xhywgl_qy" defVal="启用" fileName="znyq"></emp:message>
						
						<span id="spSubno" class="<%=identifyMode==2?"spSubnoNone":"spSubnoInline" %>"><emp:message key="znyq_ywgl_xhywgl_wh_mh" defVal="尾号：" fileName="znyq"></emp:message><label id="subno" ></label></span>
					</td>
				</tr>
				<tr class="fourHNBTr">
				<td><emp:message key="znyq_ywgl_xhywgl_yxzt_mh" defVal="运行状态：" fileName="znyq"></emp:message></td>
				<td>
				<%int rs = ls.getRunState()==null?2:ls.getRunState(); %>
				<input name="runState" type="radio" value="1" <%=rs-0!=0?"checked":"" %>/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_xhywgl_yx" defVal="运行" fileName="znyq"></emp:message>
				<input class="runState" name="runState" type="radio" value="0" <%=rs-0==0?"checked":"" %>  />&nbsp;&nbsp;<emp:message key="znyq_ywgl_xhywgl_jy" defVal="禁用" fileName="znyq"></emp:message>
				</td>
				</tr>
				</table>
				</div>
				
				<div class="firstDiv">
				<input type="button" id="nextBtn"  value="<emp:message key="znyq_ywgl_common_btn_17" defVal="下一步" fileName="znyq"></emp:message>" class="btnClass5 mr23 indent_none" onclick="javascript:formSubmitForAdd();" />
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" class="btnClass6" onclick="javascript:location.href='<%=path %>/eng_mtService.htm?method=find&lguserid='+$('#lguserid').val()" />
				<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
				<br/>
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
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/downserAdd.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_addMt1.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_sersubno.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	</body>
</html>