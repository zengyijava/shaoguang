<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
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
	
    LfCorp corp =(LfCorp) request.getAttribute("corp");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("manager");
	String depName=(String)request.getAttribute("depName");
	String type =(String)request.getAttribute("type");
	
	String IsChargingValues = "0";
	if(corp != null && corp.getIsBalance() !=null)
	{
	  IsChargingValues = corp.getIsBalance().toString();
	}
	//企业状态  1启用  0禁用
	String corpState="1";
	if(corp != null&&corp.getCorpState()!=null)
	{
	   corpState=String.valueOf(corp.getCorpState());
	}
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String condcode=request.getParameter("condcode");
	String condname=request.getParameter("condname");
	String condaddr=request.getParameter("condaddr");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
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
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_editCorp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cor_editCorp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	</head>
	<body id="cor_editCorp" onload="setLogo()">
	<input type="hidden" id="showZhezhao" value="false"/>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode,"新建/编辑企业") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode,MessageUtils.extractMessage("xtgl","xtgl_qygl_jxbjqy",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2" >
			
					<%--
					<div class="titletop">
						<table class="titletop_table">
							<tr>
								<td class="titletop_td">
									新建/编辑企业
								</td>
								<td align="right">
									<span class="titletop_font" style="margin-right: 35px;" onclick="javascript:quxiao()">&larr;&nbsp;返回上一级</span>
								</td>
							</tr>
						</table>
					</div>
					--%>
					<div id="table_input" class="table_input">
					 <form action="cor_manager.htm?method=update" method="post" name="pageForm" id="pageForm"> 
					         <input type="hidden" id="corpId" name="corpId" value="<%=null==corp.getCorpID()?"":corp.getCorpID() %>"/>
					         <input type="hidden" id="action" name="action" value="<%=null==corp.getCorpID()?"add":"update" %>">
							<input type="hidden" id="action" value="<%=null==corp.getCorpCode()?"add":"update" %> "/>
							<input type="hidden" id="code" name="code" value="<%=null==condcode?"":condcode %>"/>
							<input type="hidden" id="condcorpname" name="condcorpname" value="<%=null==condname?"":condname %>"/>
							<input type="hidden" id="addr" name="addr" value="<%=condaddr==null?"":condaddr%>"/>
							<input type="hidden" id="path" name="path" value="<%=path%>"/>
							
						<table id="sysTable" class="sysTable" >
							<tr>
								<td class="widthTD"><emp:message key="xtgl_qygl_qybm_mh" defVal="企业编码：" fileName="xtgl"></emp:message></td>
								<td>
									<label>
										<input type="hidden" id="oldCorpCode" name="oldCorpCode"  value="<%=corp.getCorpCode() %>" />
										<input type="hidden" value="logo_1.png" name="lgoUrl" id="fileName"/><%--去除上传logo功能保留的隐藏域 --%>
										<input type="hidden" name="ccode" id="ccode" value='<%=null==corp.getCorpCode()?"":corp.getCorpCode() %>'>
										<input type="text" name="corpCode" id="corpCode" maxlength="6" class="input_bd" value='<%=null==corp.getCorpCode()?"":corp.getCorpCode() %>' maxlength="12" <%if (corp.getCorpCode()!=null){ %> disabled="disabled"<%} %> onkeyup="this.value=this.value.replace(/[\W]/g,'')" />
										<font class="font_red">&nbsp;*</font>
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="xtgl_qygl_qymc_mh" defVal="企业名称：" fileName="xtgl"></emp:message></td>
								<td>
									<label>
										<input type="text" name="corpName" id="corpName" class="input_bd" value="<%=null==corp.getCorpName()?"":corp.getCorpName() %>" maxlength="25"/>
										<font class="font_red">&nbsp;*</font>
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="xtgl_qygl_qyjc_mh" defVal="企业简称：" fileName="xtgl"></emp:message></td>
								<td>
									<label>
										<input id="depName" name="depName" type="text" class="input_bd" value='<%=null==depName?"":depName %>' maxlength="25"/>
										<font class="font_red">&nbsp;*</font>
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="xtgl_qygl_qydz_mh" defVal="企业地址：" fileName="xtgl"></emp:message></td>
								<td>
									<label>
										<input type="text" name="address" id="address" class="input_bd" value="<%=null==corp.getAddress()?"":corp.getAddress() %>" maxlength="25"/>
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="xtgl_qygl_qylxdh_mh" defVal="企业联系电话：" fileName="xtgl"></emp:message></td>
								<td>
									<label>
										<input type="text" name="phone" id="phone" class="input_bd" value="<%=null==corp.getPhone()?"":corp.getPhone() %>" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/>
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="xtgl_qygl_lxr_mh" defVal="联系人：" fileName="xtgl"></emp:message></td>
								<td>
									<label>
										<input type="text" name="linkman" id="linkman" class="input_bd" value="<%=null==corp.getLinkman()?"":corp.getLinkman() %>" maxlength="12"/>
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="xtgl_qygl_lxrdh_mh" defVal="联系人电话：" fileName="xtgl"></emp:message></td>
								<td>
									<label>
										<input name="mobile" type="text" class="input_bd" value="<%=null==corp.getMobile()?"":corp.getMobile() %>" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/>
									</label>
								</td>
							</tr>
							<tr>
								<td><emp:message key="xtgl_qygl_lxremal_mh" defVal="联系人E-mail：" fileName="xtgl"></emp:message></td>
								<td>
									<label>	
										<input type="text" name="email" id="email" class="input_bd" value="<%=null==corp.getEmails()?"":corp.getEmails() %>" maxlength="40"/>
									</label>
								</td>
							</tr>
							<%--<tr style="height: 60px;">
								<td>上传logo图片：</td>
								<td>
									<label>
										<input type="file" name="fileInput" id="fileInput" class="input_bd" /><br/>
										<a href="javascript:doUplaod()" >上传新LOGO</a>&nbsp;<a href="javascript:reLogo()" >重置LOGO</a><br/>
										支持格式：jpg,png,jpeg,gif.文件不能大于50KB，建议上传300×45分辨率的文件
										<input type="hidden" value="<%=corp.getLgoUrl()==null?"logo_1.png":corp.getLgoUrl() %>" name="lgoUrl" id="fileName"/>
									</label>
								</td>
							</tr> 
							<tr style="height: 50px;">
								<td>
									企业LOGO:
								</td>
								<td>
									<div id="logo" style="width: 300px;float: left;height: 44px;display:block;background-repeat:no-repeat;">
									</div>
								</td>
							</tr>--%>
							<tr>
								<td><emp:message key="xtgl_qygl_sfqyjf_mh" defVal="是否启用计费：" fileName="xtgl"></emp:message></td>
								<td>
									<select name="IsCharging" id="IsCharging" class="input_bd">
										<option value="1" <%="1".equals(IsChargingValues)?"selected":"" %>><emp:message key="xtgl_qygl_s" defVal="是" fileName="xtgl"></emp:message></option>
										<option value="0" <%="0".equals(IsChargingValues)?"selected":"" %>><emp:message key="xtgl_qygl_f" defVal="否" fileName="xtgl"></emp:message></option>
									</select>
								</td>
							</tr>
							<%if(corp==null||!"100000".equals(corp.getCorpCode())){ %>
							<tr>
								<td><emp:message key="xtgl_qygl_qyzt_mh" defVal="企业状态：" fileName="xtgl"></emp:message></td>
								<td>
									<select name="corpState" id="corpState" class="input_bd">
										<option value="1" <%="1".equals(corpState)?"selected":"" %>><emp:message key="xtgl_qygl_qy" defVal="启用" fileName="xtgl"></emp:message></option>
										<option value="0" <%="0".equals(corpState)?"selected":"" %>><emp:message key="xtgl_qygl_jy" defVal="禁用" fileName="xtgl"></emp:message></option>
									</select>
								</td>
							</tr>
							<%} %>
							<tr>
								<td colspan="4" align="right" style="">
								<div class="buttonDiv">
									<input type="button" value="<emp:message key="xtgl_qygl_qd" defVal="确定" fileName="xtgl"></emp:message>" id="button" onclick="sub()" class="btnClass5"/>
									<input type="button" name="button" id="button" value="<emp:message key="xtgl_qygl_fh" defVal="返回" fileName="xtgl"></emp:message>" onclick="javascript:quxiao()" 
								class="btnClass6"/>
								<br />
								</div>
								</td>
							</tr>
						</table>
						<%-- <table style="background-color:#e8f0f8; border:1px solid #82baf1;" id="sysTable" width="100%" height="100%">
						<tr>
						
						<td width="15%" align="right">企业编码：</td><td width="35%"><label>
						<input type="hidden" id="oldCorpCode" name="oldCorpCode"  value="<%=corp.getCorpCode() %>" />
											<input type="hidden" name="ccode" id="ccode" value='<%=null==corp.getCorpCode()?"":corp.getCorpCode() %>'>
											<input type="text" name="corpCode" id="corpCode" maxlength="6"  value='<%=null==corp.getCorpCode()?"":corp.getCorpCode() %>' maxlength="12" <%if (corp.getCorpCode()!=null){ %> disabled="disabled"<%} %> onkeyup="this.value=this.value.replace(/[\W]/g,'')" />
											<font style="color: red;">&nbsp;*</font>
										</label></td>
						
						<td height="30px" align="right">企业名称：</td><td><label><input type="text" name="corpName" id="corpName" value="<%=null==corp.getCorpName()?"":corp.getCorpName() %>" maxlength="25"/>
											<font style="color: red;">&nbsp;*</font>
											</label></td></tr><tr>
											<td height="30px" align="right">企业简称：</td><td>
											       <div  style="width:220px;">
										<input id="depName" name="depName" type="text"  value='<%=null==depName?"":depName %>' style="width:160px;height:20px;border: 1px solid #7F9DB9;"  maxlength="25"/>
										<font style="color: red;">&nbsp;*</font>
										</div>
									</td>
						<td align="right">企业地址：</td><td width="30%">
												<label>	<input type="text" name="address" id="address" value="<%=null==corp.getAddress()?"":corp.getAddress() %>" maxlength="25"/></label>
												</td>
												</tr>
						<tr>
						<td height="30px" align="right">企业联系电话：</td><td><label><input type="text" name="phone" id="phone" value="<%=null==corp.getPhone()?"":corp.getPhone() %>" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></label></td>
						<td align="right">联系人：</td><td>
													<label>	<input type="text" name="linkman" id="linkman" value="<%=null==corp.getLinkman()?"":corp.getLinkman() %>" maxlength="12"/></label>
												</td>
						
						</tr>
						<tr>
						<td height="30px" align="right">联系人电话：</td><td><label>
													<input name="mobile" type="text" value="<%=null==corp.getMobile()?"":corp.getMobile() %>" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/>
												</label>
												
												</td>
						<td align="right">联系人E-mail：</td><td><label>	
													<input type="text" name="email" id="email" value="<%=null==corp.getEmails()?"":corp.getEmails() %>" maxlength="40"/>
												</label></td>
						</tr>
						<tr>
						<td align="right">上传logo图片：</td>
						<td><label><input type="file" name="fileInput" id="fileInput"/></label>
						<br/><a href="javascript:doUplaod()" >上传新LOGO</a>&nbsp;<a href="javascript:reLogo()" >重置LOGO</a><br/>支持图片格式：jpg,png,jpeg,gif.文件大小不能大于50KB
						<input type="hidden" value="<%=corp.getLgoUrl()==null?"logo_1.png":corp.getLgoUrl() %>" name="lgoUrl" id="fileName"/></td>
						<td colspan="2"> 企业LOGO（建议上传300×45分辨率的文件）:
							<div id="logo" style="width: 300px;float: left;height: 44px;margin: 10px 10px 0 30px;display:block;background-repeat:no-repeat;">
							</div>
						</td>
						</tr>
						<tr>
						<td align="right">是否启用计费：</td>
						<td>
						<select name="IsCharging" id="IsCharging">
									<option value="1" <%="1".equals(IsChargingValues)?"selected":"" %>>是</option>
									<option value="0" <%="0".equals(IsChargingValues)?"selected":"" %>>否</option>
						</select>
						</td>
						<td colspan="2"></td>
						</tr>
						<tr>
						<td colspan="4" align="center" height="30px">
										<input type="button" value="确定" id="button" onclick="sub()" class="btnClass1"/>
										<input type="button" value="返回" onclick="javascript:quxiao();" id="button2" class="btnClass1"/>
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
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/cor_editCorp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript">
		 function reLogo()
		 {
			 var data="logo_1.png";
			 $("#fileName").val(data);
		   	 $("#logo").css("background-image","url(<%=path%>/website/sglcorpFrame/images/logo/"+data+")");
		 }
	
		 function setLogo()
		 {
			 $.post("<%=path%>/emp_tz.htm",{method:"picTz",imgUrl:"<%=corp.getLgoUrl()%>"},
	              	function(result)
	              	{
	          				$("#logo").css("background-image","url("+result+")");	
	              	}
	       	 )
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
