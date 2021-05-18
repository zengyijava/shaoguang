<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.util.MD5" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@ page import="com.montnets.emp.entity.pasgrpbind.LfAccountBind" %>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
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
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
				
	String serId=request.getParameter("serId");
	LfService service=(LfService) request.getAttribute("service");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	LfSysuser curSysuser=(LfSysuser)request.getAttribute("curSysuser");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	@ SuppressWarnings("unchecked")
	List<LfSysuser> sysUserList = (List<LfSysuser>)request.getAttribute("sysuserList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	
	@ SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>)request.getAttribute("sendUserList");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	int identifyModeI = service.getIdentifyMode()==null || service.getIdentifyMode()==0?2:service.getIdentifyMode();
	String identifyModeStr=String.valueOf(identifyModeI);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_bjxxyw" defVal="编辑下行业务" fileName="znyq"></emp:message></title>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />

		<script>
		var identifyModeStr=<%=identifyModeStr%>;
		</script>
		
	</head>
	
	<body id="eng_editMtProcess" class="eng_editMtService">
				<div id="container">
					<%-- <div class="top">
						<div id="top_right">
							<div id="top_left"></div>
								<b>当前位置:</b>[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]-[<%=titleMap.get(menuCode) %>]-[编辑下行业务]
						</div> --%>
					</div>
					<div class="rContent">
					<%              		
						if(btnMap.get(menuCode+"-24")!=null)                       		
						{                        	
					%>
						 <center>
						 <%-- <div id="u_o_c_explain">
							<p>
								说明：下行业务
							</p>
							<ul>
								<li>
									业务名称：一个业务名称对应一个业务逻辑；
								</li>
								<li>
									业务描述：一个业务的描述供业务配置人员说明业务；
								</li>
								<li>
									运行状态：表示该业务当前设置是运行还是禁用
								</li>
								<li>
									点击【确定】按钮保存业务基本信息
								</li>
								<li>
									点击【返回】按钮放弃保存并返回下行业务列表
								</li>

							</ul>
						</div>--%>
						<div >
						<form action="<%=path %>/eng_mtService.htm?method=edit" name="serForm" method="post" id="serForm">
						<input id="submitcheck" type="hidden" onclick="formSubmit()" />
						<input type="hidden" name="serId"  value="<%=serId %>" />
						<%  boolean isEdit = true;
						    boolean isOwner = false;
						    if (curSysuser.getUserId() - service.getUserId() != 0 && curSysuser.getUserId() - service.getOwnerId() != 0)
						    {
						        isEdit = false;
						    }
						    if (curSysuser.getUserId() - service.getUserId() != 0 && curSysuser.getUserId() - service.getOwnerId() == 0)
						    {
						        isOwner = true;
						    }
						 %>
						 <div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<table id="serinfotable">
						<thead>
							<tr>
								<td class="ywNameTd">
									<span><emp:message key="znyq_ywgl_xhywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<xmp class="commonXmp"><%=service.getSerName() %></xmp>
									<input type="hidden" name="serName" id="serName" value="<%=service.getSerName() %>" />
									<input type="hidden" name="createTime" id="createTime" value="<%=service.getCreateTime().getTime() %>" />
									<input id="lguserid" name="lguserid" type="hidden" value="<%=curSysuser.getUserId() %>" />
									<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=lgcorpcode %>" />
								</td>
							</tr>
							<tr>
								<td  class="ywDiscTd">
									<span><emp:message key="znyq_ywgl_xhywgl_ywms_mh" defVal="业务描述：" fileName="znyq"></emp:message></span>
								</td>
								<td>
								<div id="commentsDiv" class="input_bd">
									<input type="text" name="comments"
										id="comments" value="<%=service.getCommnets()==null?"":service.getCommnets()%>" maxlength="256" />
								</div>
								</td>
							</tr>
                            <tr class="ownerListTr">
								<td  class="ownerListTd">
									<span><emp:message key="znyq_ywgl_xhywgl_yyz_mh" defVal="拥有者：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<div class="input_bd" id="ownerListDiv">

									<select id="ownerList" name="ownerList" onchange="getUserDate()" <%=isOwner==true?"disabled='disabled'":"" %> >
									<option value="<%=curSysuser.getUserId()%>"><%=curSysuser.getName()%>（<%=curSysuser.getUserName() %>）</option>
									<% if (sysUserList != null && sysUserList.size() > 0)
									   {
										   for (LfSysuser lfu : sysUserList)
										   {
											   if (!curSysuser.getUserName().equals(lfu.getUserName()))
											   {
									 %>
										 <option value="<%=lfu.getUserId() %>" <%=service.getOwnerId() - lfu.getUserId() == 0?"selected=selected":"" %>><%=lfu.getName()%>（<%=lfu.getUserName() %>）</option>
									 <%
											 }
										 }
									 }
									  %>
										</select>
									</div>
								</td>
							</tr>
							<tr>
							<td class="ywTypeTd">
								<span><emp:message key="znyq_ywgl_xhywgl_ywlx_mh" defVal="业务类型：" fileName="znyq"></emp:message></span>
							</td>
							<td class="busCodeTd">
							<div id="busCodeDiv" class="input_bd ">
								<select id="busCode" name="busCode" >
									<%
									if (busList != null && busList.size() > 0)
									{
										for(LfBusManager busManager : busList)
										{
									%>
									<option value="<%=busManager.getBusCode() %>" 
										<%=service.getBusCode()!=null && 
											service.getBusCode().equals(busManager.getBusCode())?
													"selected":""%> >
										<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") + "("+busManager.getBusCode()+")" %>
									</option>
									<%}
									}													
									%>
			            		</select>
			            	</div>
			            		<font class="tipColor">&nbsp;*</font>
			            		
							</td>
						</tr>
							<tr>
								<td class="spServiceTd">
									<span>
									<%--<%=StaticValue.SMSACCOUNT %>：--%>
									<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message>
									</span>
								</td>
								<td class="spUserTd">
								<div id="spUserDiv" class="input_bd">
								<%
								if(request.getAttribute("staffName")==null ||request.getAttribute("staffName").equals(""))
								{
								%>
									<select id="spUser" name="spUser" >
									<%
									if (spUserList != null && spUserList.size() > 0)
									{
										for(Userdata userdata : spUserList)
										{
									%>
									<option value="<%=userdata.getUserId() %>" 
										<%=service.getSpUser().equals(userdata.getUserId())?"selected":"" %> >
										<%=userdata.getUserId() %>(<%=userdata.getStaffName() %>)
									</option>
									<%}
									}
									%>
				            		</select>
				            		
				            	<%
								}else{
								%>
									<select id="spUser" name="spUser" class="spUser" disabled="disabled">
									<option value="<%=service.getSpUser() %>">
										<%=service.getSpUser()+"("+(String)request.getAttribute("staffName")+")" %>
									</option>
				            		</select>
								<%
								}
								%>
									<input type="hidden" id="accId" value="<%=service.getSpUser() %>"/>
								</div>
								<font class="tipColor">&nbsp;*</font>
								</td>
							</tr>
							<tr>
								<td class="tailNumTd">
								<emp:message key="znyq_ywgl_xhywgl_whzt_mh" defVal="尾号状态：" fileName="znyq"></emp:message>
								</td>
								<td>
									<%int identifyMode = service.getIdentifyMode()==null || service.getIdentifyMode()==0?2:service.getIdentifyMode(); %>
									<input type="radio" name="identifyMode" id="idfModeClose" value="2" onclick="idfModeclick(this)" <%=identifyMode==2?"checked":"" %> />&nbsp;&nbsp;<emp:message key="znyq_ywgl_xhywgl_ty" defVal="停用" fileName="znyq"></emp:message>
									<input type="radio" name="identifyMode" id="idfModeOpen" value="1" onclick="idfModeclick(this)" <%=identifyMode==1?"checked":"" %> />&nbsp;&nbsp;<emp:message key="znyq_ywgl_xhywgl_qy" defVal="启用" fileName="znyq"></emp:message>
									
									<span id="spSubno" class="<%=identifyMode==2?"spSubno1":"spSubno2" %>;"><emp:message key="znyq_ywgl_xhywgl_wh_mh" defVal="尾号：" fileName="znyq"></emp:message> <label id="subno" ></label></span>
								</td>
							</tr>
							<%--<tr>
								<td >
									<span>账户密码：</span>
								</td>
								<td>
									<input style="width: 200px" type="password" name="spPwd"
										id="password" value="<%=MD5.getMD5Str(service.getSpPwd()) %>"
										onfocus="javascript:if(this.value==$('#curPass').val()) this.value='';"/>
									<font class="xingRed">*</font>
									<input type="hidden" value="<%=MD5.getMD5Str(service.getSpPwd()) %>" id="curPass" name="curPass"/>
								</td>
							</tr> --%>
							<tr class="runtimeTr">
								<td class="runtimeTd">
									<span><emp:message key="znyq_ywgl_xhywgl_yxzt_mh" defVal="运行状态：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input name="runState" type="radio" value="1" <%=service.getRunState()-1==0?"checked":"" %> />
									<emp:message key="znyq_ywgl_xhywgl_yx" defVal="运行" fileName="znyq"></emp:message>
									&nbsp;&nbsp;&nbsp;&nbsp;<input name="runState" type="radio" value="0" <%=service.getRunState()-0==0?"checked":"" %> />
									<emp:message key="znyq_ywgl_xhywgl_jy" defVal="禁用" fileName="znyq"></emp:message>
								</td>

							</tr>
							<%-- <tr>
								<td colspan="2" id="btn">
									<input type="hidden" name="serType" id="serType" value="2" />
									<input type="hidden" name="hidOpType" id="hidOpType" value="edit" />
									
									<%if (isEdit) 
									{
									%>
									<input name="" type="button" value="确定" id="formSubmit" class="btnClass1" />
									<%} %>
									<input  type="button" value="返回" onclick="javascript:window.location.href = '<%=path%>/ser_mtService.htm?method=find&lguserid='+$('#lguserid').val()" class="btnClass1" />
								</td>
							</tr>--%>
							</thead>
						</table>
						<div id="errInfoMsg"><span></span></div>
						</form>
				</div></center>
				<%
						}
				%>
			</div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/downserAdd.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_sersubno.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script>
	function show(){
		<% 
			Object result=request.getAttribute("mtSerResult");
			if(result!=null && result.toString().equals("1"))
			{
		%>
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgcg"));
                $(window.parent.document).find("#serEditCancel").click();
		<%
			}
			else if(result!=null && result.toString().equals("0"))
			{
		%>
				//alert("操作失败！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
		<%
			}
			request.removeAttribute("mtSerResult");
			if (result != null)
			{
		%>
		    	//location.href="<%=path%>/ser_mtService.htm?method=find&lguserid="+$('#lguserid').val();
		<%  
			}
		%>
	}
	
	$(document).ready(function() {
		getLoginInfo("#hiddenValueDiv");
		noquot("#comments");
		$('#u_o_c_explain').find('> p').next().hide();
		$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
		show();
		<%
		if (!curSysuser.getUserId().equals(service.getUserId()) && !curSysuser.getUserId().equals(service.getOwnerId()))
           {
		%>
			$(window.parent.document).find("#serEditok").attr("disabled","disabled");
		<%	
		}
		else
		{
		%>
      		$(window.parent.document).find("#serEditok").attr("disabled","");
      	<%
		}
      	%>
      	getGateNumber();
      	

		if(identifyModeStr==2){
			//var obj=$('#idfModeClose').val();
			var obj=$('#idfModeClose');
	        obj['value'] = 2; 
	        idfModeclick(obj);
		}else{
			var obj=$('#idfModeOpen');
	        obj['value'] = 1; 
	        idfModeclick(obj);
		}
		
      	
	});
		</script>
		
	</body>
</html>
