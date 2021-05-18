<%--
  User: Chau
  Date: 2018-10-18
  Time: 20:33
--%>

<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	//页数
    String pageIndex=(String)request.getAttribute("pageIndex");
	
	//权限
    String multimedia=(String)request.getAttribute("multimedia");
	//选择项
    Map<String,String> mapConfig=(Map<String,String>)request.getAttribute("mapConfig");
    
    Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
   /* String menuCode = titleMap.get("manager");*/

    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title>企业富信权限模块</title>

    <style type="text/css">

    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
    <link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_authConfig.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	<script>
	var pageNum=<%=pageIndex%>;
	</script>
	
</head>
<body>
    <div class="container">

        <%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode,MessageUtils.extractMessage("xtgl","xtgl_qygl_jxbjqy",request)) %>--%>

        <div id="rContent" class="rContent rContent2" >
            <input type="hidden" id="path" value="<%=path%>" />
            <input type="hidden" id="corpCode" value="<%=request.getAttribute("corpCode") %>">
            <form method="post" action="" name="pageForm">
                <div class="switch-module" id="control">
                    <b class="title-label"><emp:message key="xtgl_rms_config_text1" defVal="企业富信权限模块" fileName="xtgl"></emp:message></b>
                    <%-- 
                    <input type="radio" name="rmsAuth" value="1" checked="checked" id="hasAuth" />&nbsp;<span><emp:message key="xtgl_rms_config_text2" defVal="有" fileName="xtgl"></emp:message></span>&nbsp;&nbsp;
                    <input type="radio" name="rmsAuth" value="0" id="noAuth" />&nbsp;<span><emp:message key="xtgl_rms_config_text3" defVal="无" fileName="xtgl"></emp:message></span>
               		 --%>
               		<input type="radio" name="rmsAuth" value="1" checked="checked" id="hasAuth" <%if(("1").equals(multimedia)){ %> checked  <%} else{%>  <%}%>/>&nbsp;<span><emp:message key="xtgl_rms_config_text2" defVal="有" fileName="xtgl"></emp:message></span>&nbsp;&nbsp;
                    <input type="radio" name="rmsAuth" value="0" id="noAuth" <%if(("0").equals(multimedia)){ %> checked <%} %>/>&nbsp;<span><emp:message key="xtgl_rms_config_text3" defVal="无" fileName="xtgl"></emp:message></span>
                
                 </div>

                <div class="auth-list-div">
                    <div>
                    	<%-- 
                        <input type="checkbox" name="rmsTextMessage" value="13" />&nbsp;<span><emp:message key="xtgl_rms_config_text4" defVal="富文本消息" fileName="xtgl"></emp:message></span>
                        --%>
                        <input type="checkbox" name="rmsTextMessage" value="13" <%if(mapConfig.containsKey("13")){ %> checked<%} %>/>&nbsp;<span><emp:message key="xtgl_rms_config_text4" defVal="富文本消息" fileName="xtgl"></emp:message></span>
                         
                         <div class="supply-method-groups" id="text">
                            <%-- 
                            <input type="checkbox" name="rmsSupplyTextMessage" value="14" id="textCheckBox" />&nbsp;<span><emp:message key="xtgl_rms_config_text5" defVal="补充方式(短信)" fileName="xtgl"></emp:message></span>
                        	--%>
                        	<input type="checkbox" name="rmsSupplyTextMessage" value="14" id="textCheckBox" <%if(mapConfig.get("13")!=null&&mapConfig.get("13").contains("14")){ %> checked<%} %> />&nbsp;<span><emp:message key="xtgl_rms_config_text5" defVal="补充方式(短信)" fileName="xtgl"></emp:message></span>
                         	
                         </div>
                    </div>

                    <div>
                        <%-- 
                        <input type="checkbox" name="rmsMediaMessage" value="11" />&nbsp;<span><emp:message key="xtgl_rms_config_text6" defVal="富媒体消息" fileName="xtgl"></emp:message></span>
                        --%>
                        <input type="checkbox" name="rmsMediaMessage" value="11" <%if(mapConfig.containsKey("11")){ %> checked<%} %>/>&nbsp;<span><emp:message key="xtgl_rms_config_text6" defVal="富媒体消息" fileName="xtgl"></emp:message></span>
                         
                         <div class="supply-method-groups" id="media">
                            <%-- 
                            <input type="checkbox" name="rmsSupplyMediaMessage" value="14" id="mediaTextCheckBox" />&nbsp;<span><emp:message key="xtgl_rms_config_text5" defVal="补充方式(短信)" fileName="xtgl"></emp:message></span>
                        	--%>
                        	<input type="checkbox" name="rmsSupplyMediaMessage" value="14" id="mediaTextCheckBox" <%if(mapConfig.get("11")!=null&&mapConfig.get("11").contains("14")){ %> checked<%} %>/>&nbsp;<span><emp:message key="xtgl_rms_config_text5" defVal="补充方式(短信)" fileName="xtgl"></emp:message></span>
                         	
                         </div>
                    </div>

                    <div>
                        <%-- 
                        <input type="checkbox" name="rmsSceneMessage" value="12" />&nbsp;<span><emp:message key="xtgl_rms_config_text7" defVal="场景消息" fileName="xtgl"></emp:message></span>
                        --%>
                        <input type="checkbox" name="rmsSceneMessage" value="12" <%if(mapConfig.containsKey("12")){ %> checked<%} %>/>&nbsp;<span><emp:message key="xtgl_rms_config_text7" defVal="场景消息" fileName="xtgl"></emp:message></span>
                        
                         <div class="supply-method-groups" id="scene">
                            <%-- 
                            <input type="checkbox" name="rmsSupplyMedia" value="11" id="sceneMediaCheckBox" />&nbsp;<span><emp:message key="xtgl_rms_config_text8" defVal="补充方式(富媒体)" fileName="xtgl"></emp:message></span>
                            <input type="checkbox" name="rmsSupplyText" value="14" id="sceneTextCheckBox" />&nbsp;<span><emp:message key="xtgl_rms_config_text5" defVal="补充方式(短信)" fileName="xtgl"></emp:message></span>
                        	--%>
                        	<input type="checkbox" name="rmsSupplyMedia" value="11" id="sceneMediaCheckBox" <%if(mapConfig.get("12")!=null&&mapConfig.get("12").contains("11")){ %> checked<%} %>/>&nbsp;<span><emp:message key="xtgl_rms_config_text8" defVal="补充方式(富媒体)" fileName="xtgl"></emp:message></span>
                            <input type="checkbox" name="rmsSupplyText" value="14" id="sceneTextCheckBox" <%if(mapConfig.get("12")!=null&&mapConfig.get("12").contains("14")){ %> checked<%} %>/>&nbsp;<span><emp:message key="xtgl_rms_config_text5" defVal="补充方式(短信)" fileName="xtgl"></emp:message></span>
                         	
                         </div>
                    </div>

                    <div>
                        <%-- 
                        <input type="checkbox" name="rmsH5Message" value="15" />&nbsp;<span>H5</span>
                         --%>
                        <input type="checkbox" name="rmsH5Message" value="15" <%if(mapConfig.containsKey("15")){ %> checked<%} %>/>&nbsp;<span>H5</span>
                        
                         <div class="" id="h5">

                        </div>
                    </div>

                </div>

                <div class="buttonDiv">
                    <input type="button" value="<emp:message key="xtgl_qygl_qd" defVal="确定" fileName="xtgl"></emp:message>" id="buttonOk" onclick="sub()" class="btnClass5"/>
                </div>

            </form>
        </div>
    </div>

    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=iPath%>/js/cor_authConfig.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

</body>
</html>
