<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.pasgroup.entity.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	//清除页面缓存
	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 
	
	String path=request.getContextPath();
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String result = request.getAttribute("w_userdataResult")==null?"0"
			:(String)request.getAttribute("w_userdataResult");
	request.removeAttribute("w_userdataResult");
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = "1900-1480";
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	//确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
	//取消
	String qx = MessageUtils.extractMessage("common", "common_cancel", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_dlzhgl_dlzhxz" defVal="代理账号新增" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pag_addProxyMage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pag_addProxyMage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="pag_addProxyMage">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dlzhgl_xjdlzh", request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<% if(btnMap.get(menuCode+"-1")!=null) {  %>
			<div class="titletop">
					<table class="titletop_table xjdlzh_table"  >
						<tr>
							<td class="titletop_td">
								<emp:message key="txgl_wgqdpz_dlzhgl_xjdlzh" defVal="新建代理账号" fileName="txgl"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_fhsyj" defVal="返回上一级" fileName="txgl"></emp:message></font>
							</td>
						</tr>
					</table>
			</div>
				<div id="detail_Info" class="<%="zh_HK".equals(empLangName)?"detail_Info1":"detail_Info2"%>"  >
            <form action="<%=path %>/pag_proxyMage.htm" name="form1" onsubmit="return checkform()" method="post">
            		<div id="loginUser" class="hidden"></div>
				<table  class="dlhhh_table">
       			 	<thead>

               	  		 <tr align="left">
                         	<td class="dlhhh_td"><span><emp:message key="txgl_wgqdpz_dcspzh_dlhhh" defVal="代理账号：" fileName="txgl"></emp:message></span></td>
                            <td>
								<label>
									<input type="text" id="userid2" name="userid" maxlength="6"  onkeyup= "spCard(this)" class="input_bd userid2" />
								</label>
									<font class="font_red">&nbsp;*</font>
									<font class="font_color">
										&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dlzhgl_zfzmszzc" defVal="6个字符，由大写字母、数字组成" fileName="txgl"></emp:message>
									</font>
								<input type="hidden" name="hidOpType" value="add"/>
								<input type="hidden" name="method" value="update"/>
								<input type="hidden" id="ips" name ="ips" value=""/>
                            </td>
                          </tr>
                         <tr align="left">
                         	<td><span><emp:message key="txgl_wgqdpz_dcspzh_zhmc" defVal="账户名称：" fileName="txgl"></emp:message></span></td>
                            <td><label><input maxlength="20" type="text" id="xiugai"  name="staffname" value=""  onkeyup= "if(value != value.replace(/[\n\s*\r]/g,'')) value=value.replace(/[\n\s*\r]/g,'')"  class="input_bd xiugai" /></label>
                            	<font class="font_red">&nbsp;*</font><font class="font_color">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_pysb" defVal="自定义，便于识别" fileName="txgl"></emp:message></font>
                            </td>
                           </tr>
                           <tr align="left">
                            <td ><span><emp:message key="txgl_wgqdpz_dcspzh_zhmm" defVal="账户密码：" fileName="txgl"></emp:message></span></td>
                            <td><label><input maxlength="18" type="password" id="xiugai" name="userpassword"  value=""  onkeypress= "if(event.keyCode==32) return false;"  class="input_bd xiugai" /></label>
                            	<font class="font_red">&nbsp;*</font><font class="font_color">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dlzhgl_zfqfdxx" defVal="6－18个字符，区分大小写" fileName="txgl"></emp:message></font>
                            </td>
                         </tr>
                  

                         <tr align="left" id="trip">
                         <td valign="top"><span><emp:message key="txgl_mwgateway_text_4" defVal="绑定IP：" fileName="txgl"></emp:message></span></td>
                         <td valign="top" id="td-ips">
	                         <div class="clearfix">
		                         <ul>
									<li class="input_bd">
										<input type="text" maxlength="3">.<input type="text" maxlength="3">.
										<input type="text" maxlength="3">.<input type="text" maxlength="3">
									</li>
								</ul><a id="add-ip" class="ip-btn"><emp:message key="txgl_wgqdpz_dlzhgl_jxtj" defVal="继续添加" fileName="txgl"></emp:message></a> <a id="remove-ip" class="ip-btn"><emp:message key="txgl_wgqdpz_qyhdgl_sc" defVal="删除" fileName="txgl"></emp:message></a>
	                         </div>
	                         <span class="zdyd_span"><emp:message key="txgl_wgqdpz_dlzhgl_zspzipdzdyd" defVal="在所配置IP地址对应的服务器上使用代理账号发送信息，为空则不受限制" fileName="txgl"></emp:message></span>
                         </td>
                         </tr>
                         <tr>
                         <td colspan="2"  id="btn"  >
                           <div  class="mt10 btnSsu_div">
                         	<input  type="submit" id="btnSsu" value="<%=qd %>" class="btnClass5 mr23"/>
                         	<input type="button" id="btnSca"  value="<%=qx %>" class="btnClass6" onclick="javascript:back()"/></td>
                         	</div>
                         </tr> 
                  		</thead>    
                		</table>
                		<div id="corpCode" class="hidden"></div>
                	</form>
                </div>
                		
			<%} %>
			</div>
			<%-- 内容结束 --%>
			<div class="clear"></div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
    <div class="clear"></div>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=iPath%>/js/userdata.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/ip.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	
	<script>
	$(document).ready(function(){
			getLoginInfo("#corpCode");
			getLoginInfo("#loginUser");
		});
			function back(){
			var codecorp  = $("#lgcorpcode").val();
			location.href='<%=path %>/pag_proxyMage.htm?method=find&lgcorpcode='+codecorp;
		}
			
			$(document).ready(function(){
				show(<%=result %>);
			} );
					function show(i)
		{
			if(i == 1)
			{
				//alert("新建账户成功！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dlzhgl_text_1"));
				location.href="<%=path%>/pag_proxyMage.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i == 2)
			{
				//alert("修改账户成功！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dlzhgl_text_2"));
				location.href="<%=path%>/pag_proxyMage.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i != 0)
			{
				//alert("操作失败！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dlzhgl_text_3"));
			}	
		}
	</script>
	</body>
</html>
