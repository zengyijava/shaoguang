<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.LoginInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.entity.gateway.AProInfo"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.system.LfSpeUICfg"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
	List<String> langs = (List)session.getAttribute("multiLanguage");
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String path = request.getContextPath();
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	String tkn = request.getParameter("tkn");
	Map<String, LoginInfo> loginMap = StaticValue.getLoginInfoMap();
	String corpcode = request.getParameter("corpcode");
	String userid =  request.getParameter("userid");
	Long lguserid =  (userid==null||"".equals(userid))?0L:Long.valueOf(userid);
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
	boolean isCharging = SystemGlobals.isDepBilling((corpcode == null || corpcode == "") ? "100001" : corpcode);//是否启用计费机制
	AProInfo proInfo = (AProInfo) session.getAttribute("AProInfo");
	proInfo = proInfo == null ? new AProInfo() : proInfo;
	String logoUrl = request.getParameter("logoUrl");
	LfCorp corp = (LfCorp) session.getAttribute("loginCorp");
	String depName = (String) session.getAttribute("depName");
	@SuppressWarnings("unchecked")
	Map<Integer, String> thirdMenuMap = (Map<Integer, String>) session
			.getAttribute("thirdMenuMap");

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");
	
	//是否显示公告
	boolean isShowGg = btnMap.get(ViewParams.GGCODE+"-0") == null ? false : true;
	//是否显示运营商余额
	boolean isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? false : true;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	//模块总数（不含首页）
	int menuSize = thirdMenuMap.size();
	//显示的模块个数（包含首页）
	int perTopMenu = 9;
	LfSpeUICfg cfg = (LfSpeUICfg)request.getAttribute("cfg");
	String companyLogo=skin+"/images/logo.png";
	if(cfg!=null&&cfg.getCompanyLogo()!=null&&!"".equals(cfg.getCompanyLogo().trim())){
		companyLogo=cfg.getCompanyLogo();
	}
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String multiLanguageEnable = SystemGlobals.getValue("multiLanguageEnable");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head><%@include file="/common/common.jsp"%>
	<title><emp:message key="common_frame2_contacts_1" defVal="企业移动信息平台" fileName="common"></emp:message></title>
	<link href="<%=iPath %>/css/top.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/top.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
    <style type="text/css">
.top_logo {
	float: left;
	height: 44px;
	margin: 8px 10px 0 19px;
	background-repeat: no-repeat;
	background-image: url(<%=companyLogo %>);
	*-background-image: none;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=<%=companyLogo%>);
}

<%if(StaticValue.ZH_HK.equals(langName)){%>
.topMenu {
	width: 100px;
	height: 32px;
	overflow: hidden;
    white-space: normal;
    /* padding-top: 1px;
	padding-left: 9px; */
	font-size: 9px;
    font-weight: bold;
    text-align: center;
    padding:0px;
}
#topLink .cursel,#topLink .curhover {
    background-size: cover;
}

#topLink {
    top: 65px;
}
#topLink table{
	width:100%;
	height:100%;
}
#topLink td{
	vertical-align: middle;
}
/*.topMenu {
	width: 80px;
	height: 32px;
	overflow: hidden;
    border: 1px solid #ddd;
    white-space: normal;
    padding-top: 1px;
	padding-left: 9px;
	font-size: 9px;
    font-weight: bold;
    color: #252525;
    text-align: center;
    text-align: left;
    display: inline-block;
}
#topLink .cursel,#topLink .curhover {
	background: #5190c9;
}

#topLink {
    top: 65px;
}*/
<%}%>
</style>
</head>
<body onload="findinfo()">
     <input type="hidden" id="hiddenYe" value="0">
		<input type="hidden" id="userid" value="<%=lguserid%>">
		<input type="hidden" id="appTkn" value="<%=tkn%>">
		<input type="hidden" id="path" value="<%=path%>">
		<input type="hidden" id="path1" value="<%=request.getContextPath()%>">
		<input type="hidden" id="mmsBalance" value="0">
     <%--標示是否在系統首頁頁面的隐藏域 --%>
	 <input type="hidden" name="onSys" id="onSys" value="1"/>
	 <%--標示是否查询余额结束 1:是  2：否（若是1则表示查询结束那鼠标放上再次去后台查询，若是2表示查询还未返回值那鼠标放上则不去后台查询）--%>
	 <input type="hidden" name="isEnd" id="isEnd" value="1"/>
     <input type="hidden" id="isCharging" value="<%=isCharging%>">
      <%-- 用来判断鼠标悬停在余额显示的位置是否有指定的时间  --%>
	 <input type="hidden" name="onTime" id="onTime" value="2"/>
     <div class="top_bg_div" style="position: absolute;width:100%;height:100px;z-index: -1;overflow: hidden">
     	<div  class="top_bg_left"></div>
     	<div class="top_bg_right"></div>
     </div>
            <div class="top"  style="_width:800px;min-width:800px;width:auto !important; ">
                <div class="top_left">                    
                    <div class="top_logo">
                        <div style="" id="companyName">
                         <%
                         	int corpType = StaticValue.getCORPTYPE();
                         	if (depName != null && depName != "") {
                         		out.print(depName);
                         	} else if (corp != null && !"".equals(corp.getCorpCode())) {
                         		out.print(corp.getCorpName());
                         	} else {
                         		out.print(MessageUtils.extractMessage("common","common_frame2_top_1",request));
                         	}
                         %>
                        </div>
                    </div>
                    <div class="top_info">
                        <a id="quit" onclick="javascript:logout();" href="#"><emp:message key="common_logout" defVal="退出" fileName="common"></emp:message></a>
					 	<a href="javascript:doUpdatePass()"><xmp style="font-weight:800"><%=((LfSysuser) session.getAttribute("loginSysuser")).getName()%></xmp></a>
                 <span></span>
                    <%
                     if (corp != null && !"100000".equals(corp.getCorpCode())) 
                     {
                    	
                     %>
	                        	<a href="javascript:checkFee()"><emp:message key="common_balance" defVal="余额" fileName="common"></emp:message></a>
	                        	<span></span>
                	<%  }  %> 
                	<a href="javascript:about()" id="aboutPlatform"><emp:message key="common_about" defVal="关于" fileName="common"></emp:message></a>
                	<span id="aboutPlatform-span"></span>
                       <a href="javascript:complain()" id="feedback"><emp:message key="common_feedback" defVal="反馈" fileName="common"></emp:message></a>
                       <span id="feedback-span"></span>
                        
                    <a href="javascript:upLoad()" id="helpManual" >帮助手册</a>
                    <span id="helpManual-span"></span> 
                    <a  href="javascript:clickSkin()" ><emp:message key="common_skinChange" defVal="换肤" fileName="common"></emp:message></a>
                       <span></span>
						<%if("Yes".equals(multiLanguageEnable) && langs != null && !langs.isEmpty() && langs.size() > 1){%>
                    <a  href="javascript:clickLanguage()"><emp:message key="common_zh_language" defVal="语言" fileName="common"></emp:message></a>
						<%}%>
                    	</div>           
                    <input type="hidden" id="currentTypeId">
                </div>
            </div>
    <div id="topparams" class="hidden" style="display: none;"></div>
    
    <%if(menuSize > perTopMenu-1)
    {%>
    <div id="leftDiv" style="width:9px;position: absolute;top:79px;left:100px;height:15px">
    <span style="display: none" id="top_menu_left" onclick="showTabLeft()"></span></div>
    <div id="rightDiv" style="width:9px;position: absolute;top:79px;left:<%=140 + perTopMenu*84+2 %>px;height:15px;z-index:1;">
    <span id="top_menu_right"  onclick="showTabRight()"></span></div>
    <%}%>
    <div id="topLink" style="width:<%=perTopMenu*84+2 %>px;">
      <div class="topLink_inner">
       <div class="topLink_left_bg">
       <div class="topLink_right_bg">
    	<div class="topMenu cursel" onclick="showLeftMenu('modIndex')" id="modIndex"><table><tr><td><div><emp:message key="common_index" defVal="首页" fileName="common"></emp:message></div></td></tr></table></div>
    	<%Iterator<Integer> ite = thirdMenuMap.keySet().iterator();
    	int index = 0;
    	while(ite.hasNext())
    	{
    		index = 0;
    		Integer key = ite.next();
    	%>
    	<div class="topMenu" id="mod<%=key %>" title="<%=thirdMenuMap.get(key) %>" onclick="showLeftMenu(this.id)"><table><tr><td><div><%=thirdMenuMap.get(key) %></div></td></tr></table></div>
    	<%} %>
    	</div>
    	</div>
    	</div>
    </div>
	 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	 <script src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	 <script type="text/javascript" src="<%=path%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	 <script src="<%=path%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	 <script type="text/javascript">
	 var corpType="<%=corpType%>";
	 var isCharging=<%=isCharging%>;
     var path="<%=path%>";
     var iPath="<%=iPath%>";
     var getContextPath='<%=request.getContextPath()%>';
     var time = <%=System.currentTimeMillis()%>;
     var menuSize = <%=menuSize+1%>;
	 var perMenuSize  = <%=perTopMenu%>;
	 var hidMenuSize = menuSize - perMenuSize;
	 var hidMenuIndex = 0;
	 var urlRouter={};
       $(function(){
       	 urlRouter={
			  modId:20,//在线客服模块ID
			  flag:1,//1表示开启跳转
			  userid:getField('#userid')||getLoginparams('#lguserid'),
			  tkn:getField('#appTkn')
		};
        function getField(obj){
        	return $(obj).val();
        }
        function getLoginparams(obj){
        	var $pa = $(window.parent.document);
        	var pahtm = $pa.find("#loginparams").children(obj).val();
        	return pahtm;
        }
       })
    </script>
    <script type="text/javascript" src="<%=iPath %>/js/top.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>