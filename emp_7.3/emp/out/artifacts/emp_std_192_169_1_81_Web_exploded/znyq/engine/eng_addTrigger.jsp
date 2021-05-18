<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.system.LfTimer" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	request.setCharacterEncoding("utf-8");
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	LfSysuser curSysuser=(LfSysuser)request.getAttribute("curSysuser");
	String lgcorpcode=(String)request.getAttribute("lgcorpcode");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	
	LfService service=(LfService)request.getAttribute("service");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_sdcftj" defVal="设定触发条件" fileName="znyq"></emp:message></title>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	
	</head>
	<body id="eng_addTrigger" class="eng_addTrigger">
				<div id="rContent" id="container">
					<%-- <div class="top">
						<div id="top_right">
							<div id="top_left"></div>
							<b>当前位置:</b>
							[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]-
							[<%=titleMap.get(menuCode) %>]-[设定触发条件]
						</div> --%>
					</div>
					<div class="rContent">
					<center>
						<%-- <div id="u_o_c_explain">
							<p>
								说明：下行短信触发条件管理
							</p>
							<ul>
								<li>
									下行业务触发条件将在设定的时间点每天执行
								</li>
								<li>
									设定触发条件的时间如果在当前服务器时间之前，将于隔天开始执行
								</li>
								<li>
									设定触发条件的时间如果在当前服务器时间之后，将于当天开始执行
								</li>
							</ul>
						</div> --%>
						<div >
						<form id="form1" action="<%=path %>/eng_mtService.htm?method=addTrigger" name="form1" method="post">
						<input id="submitcheck" type="hidden" onclick="checkServerTime()" />
						<input id="lguserid" name="lguserid" type="hidden" value="<%=curSysuser.getUserId() %>" />
						<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=lgcorpcode %>" />
						<%
							String serId=request.getParameter("serId");
							
							LfTimer timer=(LfTimer)request.getAttribute("timer");
							
						%>
						<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
							<table id="sertimetable" >
							<thead>
								<tr>
									<td class="rwNameTd">
										<span><emp:message key="znyq_ywgl_xhywgl_rwm_mh" defVal="任务名：" fileName="znyq"></emp:message><input type="hidden" name="serName" value="<%=service.getSerName() %>"/></span>
									</td>
									<td>
                                        <div class="input_bd" style="width: 269px;">
                                            <b><xmp class="commonXmp"><%=service.getSerName() %></xmp></b>
                                            <input name="serId" id="serId" type="hidden" value="<%=request.getParameter("serId") %>" />
                                            <input name="state" type="hidden" value="<%="0".equals(request.getParameter("serState"))?2:1 %>" />
                                            <input id="exit" type="hidden" value="<%=timer!=null?1:0 %>" />
                                            <input name="hidOpType" type="hidden" value="timer" />
                                        </div>

									</td>
								</tr>
								<tr>
									<td  class="beginTimeTd">	
								    	<span id="spanBeginTime"><emp:message key="znyq_ywgl_xhywgl_kssj_mh" defVal="开始时间：" fileName="znyq"></emp:message></span>
								    </td>
									<td class="sendTimeTd">
									<div id="sendtimeDiv" class="input_bd">
										<input name="sendtime"  value="<%=timer!=null && timer.getNextTime()!=null?sdf.format(timer.getNextTime()):"" %>" 
											id="sendtime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m'})" readonly="readonly" class="Wdate" />
									</div>
									<font class="tipColor">&nbsp;*</font>
									</td>
								</tr>
								<tr>
									<td  class="SendCountTd">
										<span id="spanSendCount"><emp:message key="znyq_ywgl_xhywgl_fspl_mh" defVal="发送频率：" fileName="znyq"></emp:message></span>
									</td>
									<td>
										<div class="runwayDiv input_bd">
								    	<select name="runway" onchange="showAlert(this)" >
								    		<option value="0"><emp:message key="znyq_ywgl_xhywgl_ycx" defVal="一次性" fileName="znyq"></emp:message></option>
								    		<option value="1" <%=timer!=null && timer.getIntervalUnit()==1 && timer.getRunInterval()!=null && timer.getRunInterval()-1==0?"selected":"" %>><emp:message key="znyq_ywgl_xhywgl_mt" defVal="每天" fileName="znyq"></emp:message></option>
								    		<option value="4" <%=timer!=null && timer.getIntervalUnit()==1 && timer.getRunInterval()!=null && timer.getRunInterval()-7==0?"selected":"" %>><emp:message key="znyq_ywgl_xhywgl_mz" defVal="每周" fileName="znyq"></emp:message></option>
											<option value="2" <%=timer!=null && timer.getIntervalUnit()==2 && timer.getRunInterval()!=null && timer.getRunInterval()-1==0?"selected":"" %>><emp:message key="znyq_ywgl_xhywgl_my" defVal="每月" fileName="znyq"></emp:message></option>
								    	</select>
								    	</div>
									</td>
								</tr>
								<tr>
									<td class="ValiddateDiv">
										<span id="spanValiddate"><emp:message key="znyq_ywgl_xhywgl_yxq_mh" defVal="有效期：" fileName="znyq"></emp:message></span>
									</td>
									<td>
										<div id="hourDiv" class="input_bd">
								    	<input type="text" value="<%=request.getAttribute("hours")==null?"1":request.getAttribute("hours") %>" id="hours" name="hours" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="3"/>
								    	&nbsp;<emp:message key="znyq_ywgl_xhywgl_xs" defVal="小时" fileName="znyq"></emp:message>
								    	</div>
								    	<div id="minitesDiv" class="input_bd">
								    	<input type="text" name="minites" value="<%=request.getAttribute("minites")==null?"":request.getAttribute("minites") %>" id="minites" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="3"/>
								    	&nbsp;<emp:message key="znyq_ywgl_xhywgl_fz" defVal="分钟" fileName="znyq"></emp:message>
								    	</div>
									</td>
								</tr>
								<tr><td colspan="2" class="fontZhuTd"><font id="fontZhu" class="<%=timer!=null && timer.getIntervalUnit()==2 && timer.getRunInterval()!=null && timer.getRunInterval()-1==0?"fontZhu1":"fontZhu2" %>"><emp:message key="znyq_ywgl_xhywgl_z_mh_dyfm" defVal="注：当月份没29、30、31号时，本业务当月将不执行。" fileName="znyq"></emp:message></font></td></tr>
								<%-- <tr>
									<td id="btn" colspan="8">
									<%
										if(service.getUserId()-curSysuser.getUserId()==0 || service.getOwnerId()-curSysuser.getUserId()==0)
										{
									%>
										<input type="button" value="确定"  id="onSubBton" class="btnClass1"/>
									<%
										}
									%>
										<input type="button" value="返回" 
											onclick="javascript:window.location.href = '<%=path%>/ser_mtService.htm?method=find&lguserid='+$('#lguserid').val()" class="btnClass1"/>
									</td>
								</tr> --%>
							</thead>
							</table>
							
						</form>
						</div></center>
					</div>
					<%-- 这是每个界面相应的DIV --%>
					<div class="clear"></div>
					<div class="bottom">
						<div id="bottom_right">
							<div id="bottom_left"></div>
						</div>
					</div>
				
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_addTrigger.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				getLoginInfo("#hiddenValueDiv");
		        $("#onSubBton").click(checkServerTime);
		        $('#u_o_c_explain').find('> p').next().hide();
				$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
				show();
			<%
			if (!curSysuser.getUserId().equals(service.getUserId()) && !curSysuser.getUserId().equals(service.getOwnerId()))
	           {
			%>
				$(window.parent.document).find("#serTimeEditok").attr("disabled","disabled");
			<%	
			}
			else
			{
			%>
	      		$(window.parent.document).find("#serTimeEditok").attr("disabled","");
	      	<%
			}
	      	%>
				
			});
			
			function show()
			{
				<% 
					Object result=request.getAttribute("serviceResult");
					if(result!=null && result.toString().equals("3"))
					{
				%>
						//alert("新增触发条件成功！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xzcftjcg"));
                		$(window.parent.document).find("#serTimeEditCancel").click();
				<%
					}
					else if(result!=null && result.toString().equals("0"))
					{
				%>
						//alert("操作失败！");	
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
				<%
					}
					request.removeAttribute("serviceResult");
					if (result != null)
				    {
				%>
				    	//location.href="ser_mtService.htm?method=find&lguserid="+$('#lguserid').val();
				<%
					}
				%>
			}
		
		
</script>
		
	</body>
</html>
