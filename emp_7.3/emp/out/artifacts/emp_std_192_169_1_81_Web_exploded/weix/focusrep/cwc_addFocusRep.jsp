<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.weix.LfWcAccount"%>
<%@page import="com.montnets.emp.entity.weix.LfWcRevent"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

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
	String menuCode = titleMap.get("focusrep");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
 	@ SuppressWarnings("unchecked")
 	List<LfWcAccount> lfWcAccList=(List<LfWcAccount>)request.getAttribute("lfWcAccList");
    
 	LfWcRevent lfRevent =(LfWcRevent) request.getAttribute("lfWcRevent");
	 	
    PageInfo pageInfo = new PageInfo();
    pageInfo=(PageInfo)request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>关注时回复</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css" type="text/css" >	
		
		<style type="text/css">
			.divLy{
				padding-top: 20px;
			}
			#moreSelect{
				width: 610px;
				padding-top:20px;
			}
		</style>
	</head>
	<body>
		<div id="container" class="container" >
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"新建关注时回复") %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				
				<form name="pageForm" action="<%=path %>/cwc_focusrep.htm?method=update" method="post" id="pageForm">
				<div id="hiddenValueDiv" ></div>
				<input type="hidden" name="tid" id="tid" />
				<input id="cpath" value="<%=path %>" type="hidden" />
				<div id="moreSelect">
      			<table width="" style="">
							<tr style="">
								<td style="width:108px;text-align: right;"><emp:message key="wexi_qywx_mrhf_text_1" defVal="公众帐号："
											fileName="weix"></emp:message></td>
								<td>
								<label id="busname" class="hidden"></label>
									<select class="input_bd div_bd" name="AId" id="AId" style="width: 120px;">
									<%
		                           	if(lfWcAccList != null && lfWcAccList.size()>0){
		                               for(LfWcAccount lwa : lfWcAccList){
			                        %>
									<option value="<%=lwa.getAId() %>"><%=lwa.getName().replaceAll("<","&lt").replaceAll(">","&gt")%></option>
									<%
									}
									}
									%>
									</select>
									<font color="red">&nbsp;&nbsp;&nbsp;*</font><span style="color:gray"><emp:message key="wexi_qywx_mrhf_text_12" defVal="选择公众平台微信号"
											fileName="weix"></emp:message></span>
								</td>
							</tr>
							<tr><td height="20px;"></td></tr>
							<tr>
								<td style="text-align: right;"><emp:message key="wexi_qywx_mrhf_text_3" defVal="回复标题："
											fileName="weix"></emp:message></td>
								<td>
									<input type="text" value="<%=lfRevent!=null?lfRevent.getTitle()!=null?lfRevent.getTitle():"":"" %>" class="input_bd div_bd" id="title" name="title" style="width:400px;" maxlength="32">
									<font color="red" title="<emp:message key="wexi_qywx_mrhf_text_14" defVal="回复标题不能为空，且不能超过32个字符"
											fileName="weix"></emp:message>" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;*</font>
								</td>
							</tr>
							<tr><td height="20px;"></td></tr>
							<tr>
								<td style="text-align: right;"><emp:message key="wexi_qywx_mrhf_text_15" defVal="回复类型："
											fileName="weix"></emp:message></td>
								<td>
									<span><input type="radio" value="0" id="radio1" checked name="radioTemp" onclick="selectTemp(0)"><emp:message key="wexi_qywx_mrhf_text_16" defVal="手工编辑"
											fileName="weix"></emp:message></span>
									<span><input type="radio" value="1" id="radio2" name="radioTemp" onclick="selectTemp(1)"><emp:message key="wexi_qywx_mrhf_text_17" defVal="引用模板"
											fileName="weix"></emp:message></span>
									<div id="chooseTemp" style="display:none;"><input type="button" value="<emp:message key="wexi_qywx_mrhf_text_18" defVal="选择模板"
												fileName="weix"></emp:message>" class="btnClass3" onclick="chooseTemp()"></div>
								</td>
							</tr>
							<tr><td height="20px;"></td></tr>
							<tr id="radioDiv1">
								<td style="vertical-align: top;text-align: right;">
									<emp:message key="wexi_qywx_hfgl_text_11" defVal="回复内容："
											fileName="weix"></emp:message>
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
	                         		<input id="btsend" class="btnClass5 mr10" type="button" onclick="save()" value="<emp:message key="common_btn_8" defVal="保存"
											fileName="common"></emp:message>" />
	                         		<input id="btcancel" class="btnClass6 mr10" type="button" value="<emp:message key="common_btn_9" defVal="重置"
											fileName="common"></emp:message>" onclick="javascript:resetTemp()"/>
									<input id="btcancel"  class="btnClass6" type="button" onclick="javascript:doreturn()" value="<emp:message key="common_btn_10" defVal="返回"
											fileName="common"></emp:message>"/>
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
		<div title="<emp:message key="wexi_qywx_mrhf_text_19" defVal="回复模板选择"
												fileName="weix"></emp:message>" style="display: none;" id="tempDiv">
				    	<iframe id="tempFrame" name="tempFrame" style="width:720px;height:450px;border: 0;overflow:hidden;" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
							<div style="text-align: center">
								<input class="btnClass5 mr23" onclick="tempSure()" value="<emp:message key="common_text_10" defVal="选择"
											fileName="common"></emp:message>" type="button"/>
								<input class="btnClass6" onclick="tempCancel()" value="<emp:message key="common_btn_16" defVal="取消"
											fileName="common"></emp:message>" type="button"/>
							</div>
				    </div>
		<%-- 默认回复浏览 --%>
		<div id="divBox2" style="display:none" title="<emp:message key="common_text_6" defVal="预览"
											fileName="common"></emp:message>">
			<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=commonPath %>/common/img/iphone5.jpg);">
				<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame2" src=""></iframe>	
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.InputLetter.js"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addFocusRep.js"></script>
	</body>
</html>