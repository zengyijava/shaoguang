<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
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

	LfSysuser curSysuser = (LfSysuser) session.getAttribute("loginSysuser");
			
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>)request.getAttribute("spUserList");
	LfService ls = (LfService)request.getAttribute("ls");
	if (ls == null)
	{
		ls = new LfService();
	}
	
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	
	String subno = (String)request.getAttribute("subno");
		
		String lguserid=request.getParameter("lguserid");
		String lgcorpcode=request.getParameter("lgcorpcode");
		
	String serId = request.getParameter("serId");
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

	<body onload="show()" id="eng_MoInfos" class="eng_MoInfos">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
    <input type="hidden" id="fromModify" value="1"/>
		<div id="container" class="container">
						<div id="detail">
						<form action="<%=path %>/eng_moService.htm?method=update" method="post" id="serForm" name="serForm">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" name="serId" id="serId" value="<%=serId==null?"":serId %>" />
						<input type="hidden" name="gateTLCount" id="gateTLCount" value="" />
						<input type="hidden" name="sendFlag" id="sendFlag" value="" />
						<table>
						<thead>
							<tr>
								<td class="ywNameTd">
									<emp:message key="znyq_ywgl_sxywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message>
								</td>
								<td>
								<input type="hidden" id="hserName" value='<%=ls.getSerName()==null?"":ls.getSerName() %>' />
									<input class="input_bd" type="text" name="serName" id="serName" value='<%=ls.getSerName()==null?"":ls.getSerName() %>' maxlength="20"/><font class="tipColor">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td class="ywDiscTd">
									<emp:message key="znyq_ywgl_sxywgl_ywms_mh" defVal="业务描述：" fileName="znyq"></emp:message>
								</td>
								<td>
									<input class="input_bd" type="text" name="comments" id="comments" value='<%=ls.getCommnets()==null?"":ls.getCommnets() %>' maxlength="32"/>
								</td>
							</tr>
							<tr>
								<td class="orderCodeTd">
									<emp:message key="znyq_ywgl_sxywgl_zldm_mh" defVal="指令代码：" fileName="znyq"></emp:message>
								</td>
								<td>
								<input type="hidden" value='<%=ls.getOrderCode()==null?"":ls.getOrderCode() %>' id="curCode"/>
									<input class="input_bd" type="text" name="orderCode" id="orderCode" value='<%=ls.getOrderCode()==null?"":ls.getOrderCode() %>' maxlength="10"/><font class="tipColor">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td class="matchMehodTd">
									<emp:message key="znyq_ywgl_sxywgl_pbfs_mh" defVal="匹配方式：" fileName="znyq"></emp:message>
								</td>
								<td>
									<%int orderType = ls.getOrderType()==null?1:ls.getOrderType(); %>
									<input type="radio" name="orderType" value="1" <%=orderType==1?"checked":"" %> onclick="orderTypeclick(this)" />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_zlhcs" defVal="指令和参数" fileName="znyq"></emp:message>
									<input type="radio" name="orderType" class="orderTypeIn" value="0" <%=orderType==0?"checked":"" %> onclick="orderTypeclick(this)" />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_jzl" defVal="仅指令" fileName="znyq"></emp:message>
								</td>
							</tr>
							<tr id="splDiv">
								<td class="divideTd">
									<span><emp:message key="znyq_ywgl_sxywgl_fgfh_mh" defVal="分隔符号：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<%String separate=ls.getMsgSeparated()==null?"":ls.getMsgSeparated(); %>
									<select class="input_bd"  name="msgSeparated" id="msgSeparated" >
										<option value="#">#</option>
										<option value="*" <%="*".equals(separate)?"selected":"" %>>*</option>
										<option value="+" <%="+".equals(separate)?"selected":"" %>>+</option>
									</select>
								</td>
							</tr>
							<tr>
							<td class="ywTypeTd">
								<span><emp:message key="znyq_ywgl_sxywgl_ywlx_mh" defVal="业务类型：" fileName="znyq"></emp:message></span>
							</td>
							<td>
								<select class="input_bd"  id="busCode" name="busCode">
									<%
									if (busList != null && busList.size() > 0)
									{
										for(LfBusManager busManager : busList)
										{
									%>
									<option value="<%=busManager.getBusCode() %>"  
									<%=busManager.getBusCode().equals(ls.getBusCode())?
											"selected":""%>>
										<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") + "("+busManager.getBusCode()+")" %>
									</option>
									<%}
									}													
									%>
			            		</select>
			            		<font class="tipColor">*</font>
							</td>
						</tr>
					 	<tr>
							<td class="spServiceTd">
								<span>
								<%--<%=StaticValue.SMSACCOUNT %>：--%>
								<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message>
								</span>
							</td>
							<td>
							<%
							if(request.getAttribute("staffName")==null ||request.getAttribute("staffName").equals(""))
							{
							%>
								<select class="input_bd"  id="spUser" name="spUser" onchange="getGateNumber()">
			            			<%
									if (spUserList != null && spUserList.size() > 0)
									{
										for(Userdata userdata : spUserList)
										{
									%>
										<option value="<%=userdata.getUserId()%>" <%=userdata.getUserId().equals(ls.getSpUser())?"selected":""%>>
											<%=userdata.getUserId()+"("+userdata.getStaffName()+")"%>
										</option>
									<%  }
									}
									%>
			            		</select>
			            		<font class="tipColor">*</font>
							<%
							}else
							{
							%>
								<select class="input_bd"  id="spUser" name="spUser" onchange="getGateNumber()" >
										<option value="<%=ls.getSpUser()%>" class="spUserOp">
											<%=ls.getSpUser()+"("+(String)request.getAttribute("staffName")+")"%>
										</option>
										<%
									if (spUserList != null && spUserList.size() > 0)
									{
										for(Userdata userdata : spUserList)
										{
									%>
										<option value="<%=userdata.getUserId()%>" >
											<%=userdata.getUserId()+"("+userdata.getStaffName()+")"%>
										</option>
									<%  }
									}
									%>
			            		</select>
							<%
							}
							%>
								<input type="hidden" name="staffName" id="staffName" value=""/>
							</td>
						</tr>
						
						<tr>
							<td class="tailNumTd">
							<emp:message key="znyq_ywgl_sxywgl_whzt_mh" defVal="尾号状态：" fileName="znyq"></emp:message>
							</td>
							<td>
								<%int identifyMode = ls.getIdentifyMode()==null?1:ls.getIdentifyMode(); %>
								<input type="hidden" value="<%=identifyMode %>" id="curIdentifyMode" />
								<input type="radio" name="identifyMode" id="idfModeOpen" value="1" onclick="idfModeclick(this)" <%=identifyMode==1?"checked":"" %> />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_qy" defVal="启用" fileName="znyq"></emp:message>
								<input type="radio" name="identifyMode" id="idfModeClose" value="2" onclick="idfModeclick(this)" <%=identifyMode==2?"checked":"" %> />&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_ty" defVal="停用" fileName="znyq"></emp:message>
								<span id="spSubno"><emp:message key="znyq_ywgl_sxywgl_wh_mh" defVal="尾号：" fileName="znyq"></emp:message> <label id="subno" ></label></span>
							</td>
						</tr>
						
							<tr>
								<td class="runtimeTd">
									<span><emp:message key="znyq_ywgl_sxywgl_yxzt_mh" defVal="运行状态：" fileName="znyq"></emp:message></span>
								</td>
								<td>
								<%int rs = ls.getRunState()==null?2:ls.getRunState(); %>
									<input name="runState" type="radio" value="1" <%=rs-0!=0?"checked":"" %>/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_qy" defVal="启用" fileName="znyq"></emp:message>
									<input name="runState" type="radio" class="runStateIn" value="0" <%=rs-0==0?"checked":"" %>/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_ty" defVal="停用" fileName="znyq"></emp:message>
								</td>

							</tr>
							<tr>
								<td colspan="2" id="btn">
									<input name="" type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="formSubmit" onclick="javascript:serSub();" class="btnClass5 mr23"/>
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    	<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_MoInfos.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		
		function show(){
		<% String result=(String)request.getAttribute("serviceResult");
				if(result!=null && "true" == result){%>
				window.parent.closeMoInfos();
				//alert("操作成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czcg"));
			<%}else if(result!=null && "false" == result){%>
				window.parent.closeMoInfos();
				//alert("操作失败！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czsb"));		
		    <%  }%>
		    <%if(result!=null){%>
			window.parent.location.href="<%=path%>/eng_moService.htm?method=find&lguserid="+$("#lguserid").val();
		    <%}%>
		    getGateNumber();
		}

		$(function(){
			var orderType=<%=orderType %>;
			if(orderType == 0){
		    	$("#splDiv").css("display","none");
		    }else{
		    	$("#splDiv").css("display","");
		    }
			var identifyMode=<%=identifyMode %>;
			if(identifyMode == 2){
		    	$("#spSubno").css("display","none");
		    }else{
		    	$("#spSubno").css("display","");
		    }
		});
		
    </script>
		
	</body>
</html>
