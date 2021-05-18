<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<style>
	.cont p{width:100%;height:30px;background:#2288cc;}
	*{margin:0;padding:0;}
	.cont li{padding-left:20px;font-size:13px;}
	.cont{background-color:#f1f1f9;height:100%;}
</style>
<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
    $(document).ready(function(){
        $("li").each(function () {
            $(this).mouseenter(function(){
                $(this).css("background-color","#a8a8a3");
            });
            $(this).mouseleave(function(){
                $(this).css("background-color","");
            });
        });
    });
</script>
<div class="cont">
		<div class="contleft">
			<ul>
				<p></p>
				<li url="systemManage.htm" class="on"><emp:message key='txgl_datasource_text_61' defVal='数据库及WEB配置' fileName='mwadmin'/></li>
				<% 
				String connflag = SystemGlobals.getValue("ConnectDataBaseFlag");
				if("1".equals(connflag)){
				 %>
				<li url="mwg_userdata.htm"><emp:message key='txgl_datasource_text_62' defVal='通道账号参数设置' fileName='mwadmin'/></li>
				<li url="wg_apiPara.htm"><emp:message key='txgl_datasource_text_63' defVal='API参数管理' fileName='mwadmin'/></li>
				<li url="wg_apiBaseMage.htm"><emp:message key='txgl_datasource_text_64' defVal='API个性化配置' fileName='mwadmin'/></li>
				<li url="wg_pushRsProtocol.htm"><emp:message key='txgl_datasource_text_65' defVal='API推送回应管理' fileName='mwadmin'/></li>
				<li url="mwp_userData.htm"><emp:message key='txgl_datasource_text_66' defVal='接入SP账号参数设置' fileName='mwadmin'/></li>
				<li url="tx_degreeManager.htm"><emp:message key='txgl_datasource_text_73' defVal='计费档位管理' fileName='mwadmin'/></li>
				<li url="tx_corManager.htm?method=rmsAuthManage"><emp:message key='txgl_datasource_text_74' defVal='企业富信权限设置' fileName='mwadmin'/></li>
				<%} %>
			</ul>
		</div>
		<div class="contright"></div>
	</div>
</html>
