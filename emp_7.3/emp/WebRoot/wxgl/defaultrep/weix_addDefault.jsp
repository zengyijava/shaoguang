<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>

<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String shortInheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get(request.getAttribute("rTitle"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String lguserid = (String)request.getParameter("lguserid");
 	String lgcorpcode = (String)request.getParameter("lgcorpcode");
 	
 	@ SuppressWarnings("unchecked")
 	List<LfWeiAccount> otWeiAccList=(List<LfWeiAccount>)request.getAttribute("otWeiAccList");
    PageInfo pageInfo = new PageInfo();
    pageInfo=(PageInfo)request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
    String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="wxgl_gzhgl_title_53" defVal="默认回复" fileName="wxgl"/></title>
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
              type="text/css" />

		<style type="text/css">
			#moreSelect{
				width: 610px;
				padding-top:20px;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<style type="text/css">
				.btnClass3 {
				    margin-left: 90px;
				}
			</style>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	<div id="container" class="container" >
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				
				<form  action="<%=path%>/weix_defaultReply.htm?method=update" method="post" name="acctform" id="acctform">
				<input type="hidden" id="OpType" name="OpType" value="add" />
				<%-- 存放选择的模板的tid --%>
				<input type="hidden" name="tid" id="tid" />
				<input id="cpath" value="<%=path%>" type="hidden" />
				<div id="hiddenValueDiv" style="display:none;"></div>
				<div id="moreSelect">
					<table width="" style="">
							<tr style="">
								<td style="width:108px;text-align: right;"><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></td>
								<td>
								<label id="busname" class="hidden"></label>
									<select class="input_bd div_bd" id=AId name="AId" class="input_bd" style="width:120px">
										<%
										    if(otWeiAccList != null && otWeiAccList.size()>0){
													                               for(LfWeiAccount lwa : otWeiAccList){
										%>
										<option value="<%=lwa.getAId() %>"><%=lwa.getName().replaceAll("<","&lt").replaceAll(">","&gt")%></option>
										<%
										}
										}
										%>
									</select>
									<font color="red">&nbsp;&nbsp;&nbsp;*</font><span style="color:gray"><emp:message key="wxgl_gzhgl_title_55" defVal="选择公众平台微信号" fileName="wxgl"/></span>
								</td>
							</tr>
							<tr><td height="20px;"></td></tr>
							<tr>
								<td style="text-align: right;"><emp:message key="wxgl_gzhgl_title_56" defVal="回复标题：" fileName="wxgl"/></td>
								<td>
									<input type="text" value="" class="input_bd div_bd" id="title" name="title" style="width:400px;" maxlength="32">
									<font color="red" title="<emp:message key='wxgl_gzhgl_title_57' defVal='回复标题不能为空，且不能超过32个字符' fileName='wxgl'/>" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr><td height="20px;"></td></tr>
							<tr>
								<td style="text-align: right;"><emp:message key="wxgl_gzhgl_title_58" defVal="回复类型：" fileName="wxgl"/></td>
								<td>
								  <div style="position:relative;height:24px;padding:5px 0 0;width:404px;">
									<span><input type="radio" value="0" id="radio1" checked name="radioTemp" onclick="selectTemp(0)"><emp:message key="wxgl_gzhgl_title_59" defVal="手工编辑" fileName="wxgl"/></span>
									<span><input type="radio" value="1" id="radio2" name="radioTemp" onclick="selectTemp(1)"><emp:message key="wxgl_gzhgl_title_60" defVal="引用模板" fileName="wxgl"/></span>
									<div id="chooseTemp" style="display:none;position:absolute;left:<%=StaticValue.ZH_HK.equals(langName)?30:130 %>px;top:2px;"><input type="button" value="<emp:message key='wxgl_gzhgl_title_61' defVal='选择模板' fileName='wxgl'/>" class="btnClass3" onclick="chooseTemp()"></div>
								  </div>
								</td>
							</tr>
							<tr><td height="20px;"></td></tr>
							<tr id="radioDiv1">
								<td style="vertical-align: top;text-align: right;">
									<emp:message key="wxgl_gzhgl_title_62" defVal="回复内容：" fileName="wxgl"/>
								</td>
								<td style="position: relative;">
									<textarea style="width: 400px;height: 200px;" class="input_bd div_bd" name="msgText" rows="5" id="msgText"></textarea>
									<%-- <div id="text_title" style="position: absolute;color: #BABDC0;left:5px;top:5px;">在这里输入文本信息：(最多可以输入500个字符!)</div> --%>
									<b style="bottom:-15px;left:0;color:#656565;display: block;"><span id="sid"></span>/500</b>
								</td>
							</tr>
							<tr id="radioDiv2" style="display:none;">
								
							</tr>
						  <tr><td height="20px;"></td></tr>
						  <tr align="right">
                         	<td></td>
                         	<td id="btn">
                         		<input id="btsend" class="btnClass5 mr10" type="button" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" onclick="save()"/>
						    	<input id="btcancel" class="btnClass6 mr10" type="reset" value="<emp:message key='wxgl_button_7' defVal='重置' fileName='wxgl'/>" onclick="javascript:resetTemp()"/>
						    	<input id="btcancel" class="btnClass6" type="button" onclick="javascript:doreturn()" value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>" />
                         	</td>
                          </tr>
					</table>
			        </div>
				</form>
			</div>
			<div id="pageStuff" style="display:none;">
				<input type="hidden" id="pathUrl" value="<%=path %>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addDefault.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<link href="<%=path %>/wxcommon/css/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	<link href="wxcommon/<%=wxskin %>/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	 <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>	
		<script type="text/javascript" >
		function doGo(url)
		{
			location.href = url;
		}
		</script>
	</body>
</html>