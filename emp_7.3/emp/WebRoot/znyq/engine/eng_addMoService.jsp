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
	LfSysuser curSysuser = (LfSysuser) session.getAttribute("loginSysuser");
			
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
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
	
	String subno = (String)request.getAttribute("subno");
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
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			noquot("#serName");
			noquot("#comments");
			noquot("#orderCode");
		});
		function show(){
		<% String result=(String)request.getAttribute("serviceResult");
				if(result!=null && "true" == result){%>
				//alert("操作成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czcg"));
			<%}else if(result!=null && "false" == result){%>
				//alert("操作失败！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czsb"));
		    <%  }%>
		    <%if(result!=null){%>
			location.href="<%=path%>/eng_moService.htm?method=find&lguserid="+$("#lguserid").val();
		    <%}%>
		    getGateNumber();
		}

		function getGateNumber2(){
		    var spUser = $("#spUser").val();
		    if(spUser == null || spUser == ""){
		    	return ;
		    }
	    	$.post("eng_moService.htm", 
	    		{
					method : "getGateNumber",
					lgcorpcode : $("#lgcorpcode").val(),
					spUser: spUser
				}, function(result) {
					if(result == "noSubNo"){
						//alert("拓展尾号获取失败！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tzwhhqsb"));
						$("#gateNumber").html("");
						return ;
					}
					else if(result == "noUsedSubNo")
					{
						//alert("系统没有可用的拓展尾号！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_xtmykydtzwh"));
						$("#gateNumber").html("");
						return ;
					}
					else if(result == "error"){
						//alert("通道号获取失败！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tdhhqsb"));
						$("#gateNumber").html("");
						return ;
					}
					var data = eval("("+result+")");
					$("#gateNumber").html(data.dataStr);
					$("#gateTLCount").val(data.gateTLCount);
					//$("#subNo").val(data.subNo);
				});
		}

		function getGateNumber(){
		    var spUser = $("#spUser").val();
		    if(spUser == null || spUser == ""){
		    	return ;
		    }
	    	$.post("ser_moService.htm", 
	    		{
					method : "getSubNo",
					lgcorpcode : $("#lgcorpcode").val(),
					spUser: spUser
				}, function(result) {
					if(result == "noSubNo"){
						//alert("拓展尾号获取失败！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tzwhhqsb"));
						$("#subno").html("");
						return ;
					}
					else if(result == "noUsedSubNo")
					{
						//alert("系统没有可用的拓展尾号！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_xtmykydtzwh"));
						$("#subno").html("");
						return ;
					}
					else if(result == "error"){
						//alert("通道号获取失败！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tdhhqsb"));
						$("#subno").html("");
						return ;
					}
					var data = eval("("+result+")");
				
					
					$("#subno").html(data.subNo);
					var sendFlag=(data.sendFlag).split("&");
					if(sendFlag[0]=="false" || sendFlag[1]=="false" || sendFlag[2]=="false"){
						$("#subno").css("color","red");
					}else{
						$("#subno").css("color","blue");
					}
					$("#sendFlag").val(data.sendFlag);
				});
		}
    </script>
	</head>

	<body onload="show()" id="eng_addMoService" class="eng_addMoService">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
		<%
		String title = "";
		if(ls == null || ls.getSerId() == null)
		{ 
			//title = "新建上行业务";
			title = MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_xjsxyw",request);
		}else{ 
			//title="编辑上行业务"; 
			title=MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_bjsxyw",request); 
		}%>
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,title) %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,title) %>
			
			<%-- 内容开始 --%>
						<center>
							<div id="u_o_c_explain">
							<p>
								<emp:message key="znyq_ywgl_sxywgl_smsxyw" defVal="说明：上行业务" fileName="znyq"></emp:message>
							</p>
							<ul>
								<li>
									<emp:message key="znyq_ywgl_sxywgl_ywmcygywmc" defVal="业务名称：一个业务名称对应一个业务逻辑" fileName="znyq"></emp:message>
								</li>
								<li>
									<emp:message key="znyq_ywgl_sxywgl_ywmsygywdms" defVal="业务描述：一个业务的描述供业务配置人员说明业务" fileName="znyq"></emp:message>
								</li>
								<li>
									<emp:message key="znyq_ywgl_sxywgl_zldmsjzdfsddm" defVal="指令代码：手机终端发送的代码，用于平台查找手机终端所要请求的业务，一个指令代码对应一个业务/业务名称。上行短信中第一个短信分隔符前面的内容平台均会识别为指令代码" fileName="znyq"></emp:message>
								</li>
								<li>
									<emp:message key="znyq_ywgl_sxywgl_dxfgfyydxn" defVal="短信分隔符：用于短信内容中分隔指令代码和用户参数。" fileName="znyq"></emp:message>
								</li>
								<li>
									<emp:message key="znyq_ywgl_sxywgl_yxztkykzyw" defVal="运行状态：可以控制业务是运行还是禁用，如果是禁用，平台将不处理手机终端对该业务的请求" fileName="znyq"></emp:message>
								</li>
							</ul>
						</div>
						<div id="detail_Info">
						<form action="<%=path %>/eng_moService.htm?method=update" method="post" id="serForm" name="serForm">
						<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
						<input type="hidden" name="serId" id="serId" value="<%=ls.getSerId()==null?"":ls.getSerId() %>" />
						<input type="hidden" name="gateTLCount" id="gateTLCount" value="" />
						<input type="hidden" name="sendFlag" id="sendFlag" value="" />
						<table>
						<thead>
							<tr>
								<td class="thirtyTd">
									<span><emp:message key="znyq_ywgl_sxywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></span>
								</td>
								<td>
								<input type="hidden" id="hserName" value='<%=ls.getSerName()==null?"":ls.getSerName() %>' />
									<input type="text" name="serName" 
										id="serName" class="twohunIn" value='<%=ls.getSerName()==null?"":ls.getSerName() %>' maxlength="20"/><font class="tipColor">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td >
									<span><emp:message key="znyq_ywgl_sxywgl_ywms_mh" defVal="业务描述：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input type="text" name="comments"
										id="comments" class="twohunIn" value='<%=ls.getCommnets()==null?"":ls.getCommnets() %>' maxlength="32"/>
								</td>
							</tr>
							<tr>
								<td >
									<span><emp:message key="znyq_ywgl_sxywgl_zldm_mh" defVal="指令代码：" fileName="znyq"></emp:message></span>
								</td>
								<td>
								<input type="hidden" value='<%=ls.getOrderCode()==null?"":ls.getOrderCode() %>' id="curCode"/>
									<input  type="text" name="orderCode"
										id="orderCode" class="twohunIn" value='<%=ls.getOrderCode()==null?"":ls.getOrderCode() %>' maxlength="20"/><font class="tipColor">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td >
									<span><emp:message key="znyq_ywgl_sxywgl_dxfgf_mh" defVal="短信分隔符：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<%String separate=ls.getMsgSeparated()==null?"":ls.getMsgSeparated(); %>
									<select name="msgSeparated" class="tafWidth">
										<option value="#">#</option>
										<option value="$" <%="$".equals(separate)?"selected":"" %>>$</option>
										<option value="*" <%="*".equals(separate)?"selected":"" %>>*</option>
										<option value="&" <%="&".equals(separate)?"selected":"" %>>&amp;</option>
										<option value="+" <%="+".equals(separate)?"selected":"" %>>+</option>
										<option value="@" <%="@".equals(separate)?"selected":"" %>>@</option>
										<option value=" " <%=" ".equals(separate)?"selected":"" %>> (<emp:message key="znyq_ywgl_sxywgl_dgkg" defVal="单个空格" fileName="znyq"></emp:message>)</option>
									</select>
								</td>
							</tr>
							<tr>
							<td >
								<span><emp:message key="znyq_ywgl_sxywgl_ywlx_mh" defVal="业务类型：" fileName="znyq"></emp:message></span>
							</td>
							<td>
								<select id="busCode" name="busCode" class="tafWidth">
									<%
									if (busList != null && busList.size() > 0)
									{
										for(LfBusManager busManager : busList)
										{
									%>
									<option value="<%=busManager.getBusCode() %>"  
									<%=busManager.getBusCode().equals(ls.getBusCode())?
											"selected":""%>>
										<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") %>
									</option>
									<%}
									}													
									%>
			            		</select>
			            		<font class="tipColor">*</font>
							</td>
						</tr>
					 	<tr>
							<td >
								<span><%--<%=StaticValue.SMSACCOUNT %>：--%>
								<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message></span>
							</td>
							<td>
							<%
							if(request.getAttribute("staffName")==null ||request.getAttribute("staffName").equals(""))
							{
							%>
								<select id="spUser" name="spUser" class="tafWidth" onchange="getGateNumber()">
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
			            		<font class="tipColor">*</font>
							<%
							}else
							{
							%>
								<select id="spUser" name="spUser" class="tafWidth" onchange="getGateNumber()" disabled="disabled">
										<option value="<%=ls.getSpUser()%>">
											<%=ls.getSpUser()+"("+(String)request.getAttribute("staffName")+")"%>
										</option>
			            		</select>
							<%
							}
							%>
								<input type="hidden" name="staffName" id="staffName" value=""/>
							</td>
						</tr>
						
						<tr>
							<td >
								<span><emp:message key="znyq_ywgl_sxywgl_kzwh_mh" defVal="扩展尾号：" fileName="znyq"></emp:message></span>
							</td>
							<td >
								 <label id="subno" ></label>
							</td>
						</tr>
						
							<tr>
								<td >
									<span><emp:message key="znyq_ywgl_sxywgl_yxzt_mh" defVal="运行状态：" fileName="znyq"></emp:message></span>
								</td>
								<td>
								<%int rs = ls.getRunState()==null?2:ls.getRunState(); %>
									<input name="runState" type="radio" value="1" <%=rs-0!=0?"checked":"" %>/>
									<emp:message key="znyq_ywgl_sxywgl_yx" defVal="运行" fileName="znyq"></emp:message>
									<input name="runState" type="radio" value="0" <%=rs-0==0?"checked":"" %>  />
									<emp:message key="znyq_ywgl_sxywgl_jy" defVal="禁用" fileName="znyq"></emp:message>
								</td>

							</tr>
							<tr>
								<td colspan="2" id="btn">
									<input name="" type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="formSubmit" onclick="javascript:serOk();" class="btnClass1 mr23"/>
									<input  type="button" value="<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message>" onclick="javascript:location.href='<%=path %>/eng_moService.htm?method=find&lguserid='+$('#lguserid').val()" class="btnClass1"/>
								</td>
							</tr>
							</thead>
						</table>
						</form>
				</div>
						</center>
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
	</body>
</html>
