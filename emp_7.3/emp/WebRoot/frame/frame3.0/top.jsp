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
	@SuppressWarnings("unchecked")
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
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");
	
	//是否显示公告
	boolean isShowGg = btnMap.get(ViewParams.GGCODE+"-0") == null ? false : true;
	//是否显示运营商余额
	boolean isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? false : true;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
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
	<link href="<%=iPath %>/css/top.css" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/top.css" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
    <style type="text/css">
		.top_logo 
		{
			float: left;
			height: 44px;
			margin: 12px 10px 0 30px;
			background-repeat:no-repeat;
			background-image:url(<%=companyLogo %>);
			*-background-image:none;
			filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=<%=companyLogo%>);
		}
		a#lg{
			text-decoration:none;
		}
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		#secondPageDiv{
			width: 65px;
			margin-right: 5px;
			padding: 0 6px 0 6px;
		}
		a#lg,a#feedback,a#notice,a#balance,div#remainingNum{
			width: 65px;
		}
		<%}%>
		</style>
</head>
<body onload="findinfo()">
	<input type="hidden" id="empLangName" value="<%=empLangName%>"/>
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
     
    <table  style="border-spacing: 0px;" id="tbBody">
        <tbody><tr>
            <td>
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
                    <a id="firstPage" class="" href="javascript:doIndex();" title='<emp:message key="common_backToIndex" defVal="返回主页" fileName="common"></emp:message>'>
                    <span id="top_home"  style="background-image: url('<%=iPath %>/img/home.gif');"></span></a>
                    <a id="firstPage" href="javascript:clickSkin()" >
                    <span id="skin" style="background-image: url('<%=iPath %>/img/skin.gif');width:30px"></span></a>
                     <span class="halving_line"></span>
                   <div id="secondPageDiv" style="width: 40px; padding: 0px 3px 0 3px;">
                       <a id="sysmenu" style="text-decoration:none;width: 40px;" ><emp:message key="common_menu" defVal="菜单" fileName="common"></emp:message></a>
                   </div>
					<%if("Yes".equals(multiLanguageEnable) && langs != null && !langs.isEmpty() && langs.size() > 1){%>
                    <div id="secondPageDiv">
                       <a id="lg"> <emp:message key="common_zh_language" defVal="语言" fileName="common"></emp:message> </a>
                   </div>
					<%}%>
                 
                   <div id="secondPageDiv">
                       <a id="feedback" href="javascript:complain()"><emp:message key="common_feedback" defVal="反馈" fileName="common"></emp:message></a>
                   </div> 
                    <%if(isShowGg && StaticValue.getInniMenuMap().get("/not_notice.htm")!=null){ %>
				  	<div id="secondPageDiv">
						<a id="notice" href="javascript:doNotice2()" ><emp:message key="common_notice" defVal="公告" fileName="common"></emp:message></a>
				   	</div>
                     <%}
                     if (corp != null && !"100000".equals(corp.getCorpCode())){
                     %>
					<div id="remainingNum" style="padding: 0px 3px;">
						<a id="balance" href="javascript:void(0)"><emp:message key="common_balance" defVal="余额" fileName="common"></emp:message></a>
					</div>
					<%}%>
                    	<div id="loginMes">
                    	    <a href="javascript:void(0)"><%=((LfSysuser) session.getAttribute("loginSysuser")).getName()%></a>
                    	</div>
                    	  <div id="secondPageDiv">
                       <a id="quit" onclick="javascript:logout();" href="#"><emp:message key="common_logout" defVal="退出" fileName="common"></emp:message></a>
                   </div>
                    	</div>           
                  
                    <input type="hidden" id="currentTypeId">
                </div>
               
            </div>
            </td>
        </tr>
    </tbody></table>
    <div id="topparams" class="hidden" style="display: none;"></div>
	 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=path%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=path%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
     <script language="javascript" type="text/javascript">
      var isCharging = <%=isCharging%>;
      var time = <%=System.currentTimeMillis()%>;
      var iPath="<%=iPath%>";
      var getContextPath='<%=request.getContextPath()%>';
      var path="<%=path%>";
      var corpType="<%=corpType%>";
    </script>
    <script type="text/javascript" src="<%=iPath %>/js/top.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>