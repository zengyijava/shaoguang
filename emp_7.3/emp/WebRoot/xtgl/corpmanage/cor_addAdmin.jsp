<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

 <%
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("manager");
	LfSysuser user= (LfSysuser)request.getAttribute("user");
	String action = (String)request.getAttribute("action");
	String corpCode=(String)request.getAttribute("corpCode");
	LfDep dep = (LfDep)request.getAttribute("dep");
	if(corpCode==null){
	    corpCode=request.getParameter("corpCode");
	}
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String condcode=request.getParameter("condcode");
	String condname=request.getParameter("condname");
	String condaddr=request.getParameter("condaddr");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01  Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_addAdmin.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cor_addAdmin.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	</head>
	<body id="cor_addAdmin" onload="show()">
	<input type="hidden" id="showZhezhao" value="false"/>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"新建管理员") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("xtgl","xtgl_qygl_xjgly",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2" >
					<%--<div class="titletop">
						<table class="titletop_table">
							<tr>
								<td class="titletop_td">
									新建管理员
								</td>
								<td align="right">
									<span class="titletop_font" style="margin-right: 35px;" onclick="quxiao()">&larr;&nbsp;返回上一级</span>
								</td>
							</tr>
						</table>
					</div>
				
					--%><div id="table_input" class="table_input">
						<form action="<%=path %>/cor_manager.htm?method=updateAdmin" method="post"  name="pageForm" id="pageForm">
						<div id="corp_addAdminparams" class="hidden"></div>
						<input type="hidden" id="id" name="id" value="<%=user.getUserId() %>"/>
						<input type="hidden" id="corpCode" name="corpCode" value="<%=corpCode %>">
						<input type="hidden" id="action" name="action" value="<%=action %>"/>
							<input type="hidden" id="inheritPath" value="<%=inheritPath %>"/>
						<input type="hidden" id="code" name="code" value="<%=null==condcode?"":condcode %>"/>
						<input type="hidden" id="condcorpname" name="condcorpname" value="<%=null==condname?"":condname %>"/>
						<input type="hidden" id="addr" name="addr" value="<%=condaddr==null?"":condaddr%>"/>
							<table id="sysTable" class="sysTable" >
								<tr>
									<td class="widthTD"><emp:message key="xtgl_qygl_glyid_mh" defVal="管理员ID：" fileName="xtgl"></emp:message></td>
									<td>
										<label>
											<input type="text" name="username" id="username" class="input_bd username" value="admin" title="" maxlength="20" readonly="readonly" />
										    <font class="font_red">&nbsp;*</font>
										</label>
									</td>
								</tr>
								<tr>
									<td><emp:message key="xtgl_qygl_glymc_mh" defVal="管理员名称：" fileName="xtgl"></emp:message></td>
									<td>
										<label>
											<input type="text" name="name" id="name" class="input_bd" value="" maxlength="12" />
											<font class="font_red">&nbsp;*</font>
										</label>
									</td>
								</tr>
								<tr>
									<td><emp:message key="xtgl_qygl_dlmm_mh" defVal="登录密码：" fileName="xtgl"></emp:message></td>
									<td>
										<label>
											<input type="password" name="nPwd" id="nPwd" onblur="" class="input_bd" value="" maxlength="20"/>
											<font class="font_red">&nbsp;*</font>
										</label>
									</td>
								</tr>
								<tr>
									<td><emp:message key="xtgl_qygl_qdmm_mh" defVal="确定密码：" fileName="xtgl"></emp:message></td>
									<td>
										<label>
											<input type="password" id="nPwd2" onblur="" class="input_bd" value="" maxlength="20"/>
											<font class="font_red">&nbsp;*</font>
										</label>
									</td>
								</tr>
								<tr>
									<td><emp:message key="xtgl_qygl_zt_mh" defVal="状态：" fileName="xtgl"></emp:message></td>
									<td>
										<select name="state" id="state" class="input_bd">
											<option value="1"><emp:message key="xtgl_qygl_qy" defVal="启用" fileName="xtgl"></emp:message></option>
											<option value="0"><emp:message key="xtgl_qygl_jy" defVal="禁用" fileName="xtgl"></emp:message></option>
										</select>
									</td>
								</tr>
								<tr>
									<td><emp:message key="xtgl_qygl_xb_mh" defVal="性别：" fileName="xtgl"></emp:message></td>
									<td>
										<select name="sex" class="input_bd">
											<option value="1"><emp:message key="xtgl_qygl_nan" defVal="男" fileName="xtgl"></emp:message></option>
											<option value="0"><emp:message key="xtgl_qygl_nv" defVal="女" fileName="xtgl"></emp:message></option>
										</select>
									</td>
								</tr>
								<tr>
									<td><emp:message key="xtgl_qygl_ssjg" defVal="所属机构：" fileName="xtgl"></emp:message></td>
									<td>
										<label>
											<input type="text" name="dep" id="dep" class="input_bd dep" value="<%=null==dep.getDepName()?"":dep.getDepName() %>" readonly="readonly" />
											<input type="hidden" name="depId" id="depId" value='<%=dep.getDepId() %>'/>		 
										</label>
									</td>
								</tr>
								<tr>
									<td><emp:message key="xtgl_qygl_sj_mh" defVal="手机：" fileName="xtgl"></emp:message></td>
									<td>
										<label>	
											<input type="text" name="mobile" id="mobile" class="input_bd" value="" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="11"/>
											<font class="font_red">&nbsp;*</font>
										</label>
									</td>
								</tr>
								<tr>
									<td colspan="4" align="right" >
										<div class="buttonDiv">
											<input type="button" value="<emp:message key="xtgl_qygl_qd" defVal="确定" fileName="xtgl"></emp:message>" id="button" onclick="checkData()" class="btnClass5 mr23"/>
											<input name="" type="button"  onclick="quxiao()" value="<emp:message key="xtgl_qygl_fh" defVal="返回" fileName="xtgl"></emp:message>" class="btnClass6"/>
											<br />
										</div>
									</td>
								</tr>
							</table>
							<%-- <table style="background-color:#e8f0f8; border:1px solid #82baf1;" id="sysTable" width="100%" height="100%">
						<tr>
						<td width="15%" height="30px">管理员ID：</td><td width="40%"><label>
											<input type="text" name="username" id="username" value="admin"  title="" maxlength="20" readonly="readonly" style="background-color:#EFEFEF"/>
										    <font style="color: red;">&nbsp;*</font>
 											
										</label></td>
						<td width="15%">管理员名称：</td><td width="30%"><label>
											<input type="text" name="name" id="name" value="" maxlength="12"/>
											<font style="color: red;">&nbsp;*</font>
										</label></td>
						</tr>
						<tr>
						<td height="30px">登录密码：</td>
						<td><label><input type="password" name="nPwd" id="nPwd"  onblur="" value="" maxlength="20"/>
											<font style="color: red;">&nbsp;*</font>
											</label></td>
						<td>状态：</td><td><select name="state" id="state">
						                        <option value="1">启用</option>
						                        <option value="0">禁用</option>
						                   </select></td>
						</tr>
						<tr>
						<td height="30px">确定密码：</td><td><label><input type="password" id="nPwd2" onblur="" value="" maxlength="20"/>
						<font style="color: red;">&nbsp;*</font></label></td>
						<td>性别：</td><td>
													<select name="sex">
														<option value="1">男</option>
														<option value="0">女</option>
													</select>
												</td>
						</tr>
						<tr>
						<td>所属机构：</td><td><label>
													<input type="text" name="dep" id="dep"
														 value="<%=null==dep.getDepName()?"":dep.getDepName() %>" readonly="readonly" style="background-color:#EFEFEF"/>
												<input type="hidden" name="depId" id="depId" value='<%=dep.getDepId() %>'/>		 
												</label></td>
						<td>手机：</td><td><label>	
													<input type="text" name="mobile" id="mobile" value="" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="11"/>
												<font style="color: red;">&nbsp;*</font></label></td>
						</tr>
						<tr>
						
						</tr>
						
						
						<tr><td colspan="4" align="right"><p id="zhu"></p></td></tr>
						<tr>
						<td colspan="4" align="center" height="30px">
									
										<input type="button" value="确定" id="button" class="btnClass1" onclick="checkData()" class="btnClass1"/>
									
										<input type="button" value="返回" class="btnClass1" onclick="javascript:window.location.href='<%=path %>/cor_corpManager.htm?method=find'" id="button2" class="btnClass1"/>
									</td>
						</tr>
						
						</table>--%>
								</form>
							</div>
						
							
							<div class="clear"></div>
						

			</div>

			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
        $(document).ready(function() {
		    getLoginInfo("#corp_addAdminparams");
		});
		function show()
		{
			<% String result=(String)request.getAttribute("result");
			if(result!=null && result.equals("true")){%>
			//alert("操作成功！");
			alert(getJsLocaleMessage("xtgl","xtgl_qygl_czcg"));
			<%}else if(result!=null && result.equals("false")){%>
				//alert("操作失败！");
				alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
			<%}
			if(result !=null)
			{
				%>
				location.href="<%=path%>/cor_manager.htm";
				<%
			}
			%>
		}
		//内容检查
		function checkData(){
		   var username=$("#username").val();
		   var name=$("#name").val();
		   var nPwd=$("#nPwd").val();
		   var nPwd2=$("#nPwd2").val();
		   var corpcode=$("#corpCode").val();
		   var mobile=$.trim($("#mobile").val());
		   if(name==null||name==""){
		      //alert("请输入管理员名称");
		      alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrglymc"));
		      return;
		   }
		    if(nPwd==null||nPwd==""){
		      //alert("请输入登录密码");
		      alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrdlmm"));
		      return;
		   }
		   if(nPwd2==null||nPwd2==""){
		      //alert("请确认登录密码");
		      alert(getJsLocaleMessage("xtgl","xtgl_qygl_qqrdlmm"));
		      return;
		   }
		   if(nPwd!=nPwd2){
		      //alert("两次输入的密码不一致");
		      alert(getJsLocaleMessage("xtgl","xtgl_qygl_lcsrdmmbyz"));
		      return;
		   }
		   if (mobile == "")
		   {
			   //alert("手机不能为空！");
			   alert(getJsLocaleMessage("xtgl","xtgl_qygl_sjbnwk"));
			   $("#mobile").focus();
				  return;
		   }
		   else if (mobile!="" && mobile.length != 11)
		   {
			   //alert("手机号码不正确！");
			   alert(getJsLocaleMessage("xtgl","xtgl_qygl_sjhmbzq"));
			   $("#mobile").focus();
				  return;
		   }
		   $("#button").attr("disabled","true");
		   $.post("<%=path%>/cor_manager.htm?method=checkId",{userId:username,corpCode:corpcode},function(result){
                 if(result != null && result !="")
                 {
                       //if(confirm("该企业已存在此ID的管理员,是否覆盖原管理员的信息提交？"))
                       if(confirm(getJsLocaleMessage("xtgl","xtgl_qygl_gqyyczciddgly")))
                       {
                           $("#action").val("update");
                           $("#id").val(result);
                           $("#pageForm").submit();
                       }
                       else
                       {
						   $("#button").attr("disabled","");
                       }
                 }
                 else
                 {
                	 $("#pageForm").submit();
                 }
		   });
		}
		
		function quxiao()
		{
			 var url = encodeURI("<%=path%>/cor_manager.htm?method=find"+
							"&code=<%=null==condcode?"":condcode %>&condcorpname=<%=null==condname?"":condname %>&addr=<%=condaddr==null?"":condaddr%>");
		 		window.location.href = url;
		}
	
		</script>
	</body>
</html>
