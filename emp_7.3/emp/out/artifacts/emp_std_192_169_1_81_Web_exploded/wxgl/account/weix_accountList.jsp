<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="org.apache.tools.ant.taskdefs.Length"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    //-> /p_weix
    String path = request.getContextPath();
    //-> http://localhost:8088/p_weix/
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    //-> /p_weix/weix/account
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    //-> /p_weix/weix
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    //-> /p_weix
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    @SuppressWarnings("unchecked")
    List<DynaBean> beans = (List<DynaBean>) request.getAttribute("beans");
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String name = (String) request.getParameter("name");
    String ghname = (String) request.getParameter("ghname");
    String weixBasePath = GlobalMethods.getWeixBasePath();

    //使用集群，文件服务器的地址
    String filePath = GlobalMethods.getWeixFilePath();
    //MP企业微信服务协议
    String approve = (String) session.getAttribute("approve");
    
    String httpUrl = StaticValue.getFileServerViewurl();
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
   	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   	<%@include file="/common/common.jsp" %>
    <title><emp:message key="wxgl_gzhgl_title_1" defVal="微信公众帐号管理" fileName="wxgl"/></title>
    <link rel="stylesheet" href="<%=iPath%>/css/weix_accountList.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
    <script>
        var pathUrl="<%=path%>",
            iPathUrl="<%=iPath%>",
            currentTotalPage="<%=pageInfo.getTotalPage()%>",
            currentPageIndex="<%=pageInfo.getPageIndex()%>",
            currentPageSize="<%=pageInfo.getPageSize()%>",
            currentTotalRec="<%=pageInfo.getTotalRec()%>",
            approve="<%=approve%>";
    </script>
    <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
</head>
<body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<div id="container" class="container">
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<%if(btnMap.get(menuCode+"-0")!=null) { %>
		<div id="rContent" class="rContent">
		<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
		<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
		<form name="pageForm" action="<%=path %>/weix_acctManager.htm" method="post"
				id="pageForm">
				<div style="display:none" id="hiddenValueDiv"></div>
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<a href="javascript:showAddAccountTmp()" id="add"><emp:message key="wxgl_button_1" defVal="添加" fileName="wxgl"/><%-- 公众帐号 --%></a>
				</div>
				<div id="condition">
					<table >
						<tr>
						   <td ><emp:message key="wxgl_gzhgl_title_2" defVal="公众帐号名称：" fileName="wxgl"/></td>
						   <td><input id="title" name="name" value="<%=name != null ? name : ""%>">
						   </td>
							<td class="tdSer">
								<a id="search"></a></center>
							</td>
						</tr>
					</table>
				</div>
						
                <table id="content">
                    <thead>
                        <tr>
                            <th><emp:message key="wxgl_gzhgl_title_3" defVal="头像" fileName="wxgl"/></th>
                            <th><emp:message key="wxgl_gzhgl_title_4" defVal="公众帐号名称" fileName="wxgl"/></th>
                            <th width="210"><emp:message key="wxgl_gzhgl_title_5" defVal="帐号接入信息" fileName="wxgl"/></th>
                            <th><emp:message key="wxgl_gzhgl_title_6" defVal="创建时间" fileName="wxgl"/></th>
                            <th><emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if(beans != null && beans.size() > 0)
                            {
                                for (DynaBean bean : beans)
                                {
                        %>
                        <tr>
                            <td align="center">
                            <%
                                if(bean.get("img") != null && !"".equals(bean.get("img")))
                                {
                                	
                            %>
                               <img src='<%=bean.get("img")%>'  title="<emp:message key='wxgl_gzhgl_title_3' defVal='头像' fileName='wxgl'/>" class="avatar" onerror="this.src='<%= inheritPath + "/account/img/default_acct.png"%>'"/>
                            <%
                                }
                                else
                                {
                            %>
                                <img src='<%=inheritPath + "/account/img/default_acct.png" %>' title="<emp:message key='wxgl_gzhgl_title_3' defVal='头像' fileName='wxgl'/>" class="avatar">
                            <%
                                }
                            %>
                            </td>
                            <td align="center"><%=StringUtils.escapeString(bean.get("name").toString())%>
                            	<div class="titleTip" style="display:none"><%=StringUtils.escapeString(bean.get("name").toString())%></div>
                            </td>
                            <td><div class="copyinfo">
                            		<% String url = "";
                            		    if(bean.get("url") != null){
                            		        url = weixBasePath + "api/" + bean.get("url");
                            		    }
                            		%>
                                    <p title="<%=url%>"><emp:message key="wxgl_gzhgl_title_8" defVal="接口URL:" fileName="wxgl"/><span><%=url%></span></p>
                                    <a href="" class="copy">copy</a>
                                </div>
                                <div class="copyinfo">
                                	<%
                                		String token = "";
                                		if(bean.get("token")!=null){
                                		    token = (String)bean.get("token");
                                		}
                                	%>
                                    <p title="<%=token%>">Token:<span><%=token%></span></p>
                                    <a href="" class="copy">copy</a>
                                </div>
                            </td>
                            <td align="center"><%=formatter.format(bean.get("createtime"))%></td>
                            <td align="center">
                                <a href="javascript:showEditAcctTmp(<%=bean.get("a_id")%>)" class="mr10"><emp:message key="wxgl_button_2" defVal="修改" fileName="wxgl"/></a>
                                <%if("0".equals(String.valueOf(bean.get("sync_state")))) {%>
                                	<emp:message key="wxgl_gzhgl_title_9" defVal="同步中..." fileName="wxgl"/>
                                <%}else{%>
                                	<a class="doSyncBut" data-id="<%=bean.get("a_id")%>"><emp:message key="wxgl_gzhgl_title_10" defVal="同步" fileName="wxgl"/></a>
                                <%} %>
                            </td>
                        </tr>
                        <%
                            }
                            }
                            else
                            {
                        %>
                        <tr><td align="center" colspan="5"><emp:message key="wxgl_gzhgl_title_11" defVal="无记录" fileName="wxgl"/></td></tr>
                        <%
                            }
                        %>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="8">
                                <div id="pageInfo">
                                </div>
                            </td>
                        </tr>
                    </tfoot>
                </table>						
			</form>
		</div>
		<%} %>
		<%-- 内容结束 --%>
		<%-- foot开始 --%>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
				<div id="bottom_main">
				</div>
			</div>
		</div>
		<%-- foot结束 --%>
	</div>
  	<div id="pageStuff" style="display:none;">
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
		<input type="hidden" id="approve" value="<%=approve%>"/>
	</div>
	<div class="clear"></div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=commonPath%>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
    <script src="<%=commonPath%>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=iPath%>/js/jquery.zclip.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=iPath%>/js/accountList.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=iPath%>/js/doSync.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <link href="<%=commonPath %>/wxcommon/css/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=commonPath %>/wxcommon/<%=wxskin %>/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
	</body>
</html>