<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiUserInfo"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiGroup"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>)session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>)session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String)session.getAttribute("stlyeSkin");
    String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
    String weixBasePath = request.getScheme() + "://" + request.getServerName() + path + "/";
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //使用集群，文件服务器的地址
    String filePath = GlobalMethods.getWeixFilePath();
    //MP企业微信服务协议
    String approve = (String)session.getAttribute("approve");
    
	//用户信息
    LfWeiGroup otWeiGroup =(LfWeiGroup)request.getAttribute("groupinfo");
	//公共账号信息
	LfWeiAccount otWeiAccount=(LfWeiAccount)request.getAttribute("otWeiAccount");
	//用来表示用户是查看详情，还是修改信息(1：查看详情，2:修改)
	String type = String.valueOf(request.getParameter("type"));
	if(null == type || "".equals(type) || "null".equals(type))
	{
	    type="";
	}
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container" style="width: 360px; height:100px;">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<%-- 当前位置 --%>
			<%--<div id="rContent" class="rContent">--%>
			<form name="groupForm" action="" method="post" id="groupForm">
				<div style="display:none" id="hiddenValueDiv">
				</div>			
				<table id="editGroupTable" style="height: 150px;text-align: left;">
				<tbody>
                     <tr>
                         <td style="width:90px;">
                             <span><emp:message key="wxgl_gzhgl_title_81" defVal="所属公众帐号：" fileName="wxgl"/></span>
                         </td>
                         <td>
                             <div>
								<label><%=otWeiAccount.getName()==null?"-": otWeiAccount.getName()%></label>
                            </div>
                        </td>
                    </tr>
					<tr>
						<td>
							<span><emp:message key="wxgl_gzhgl_title_80" defVal="群组名称：" fileName="wxgl"/></span>
						</td>
						<td>
							<%if("2".equals(type)){ %>
							<div style="float: left;">
                                         <label>
								<input name="gname" id="gname" maxlength="20" class="bd_none" type="text" value="<%=otWeiGroup.getName()==null?"-": otWeiGroup.getName()%>"/>
								</label><font color="red" style="display:none;"><emp:message key="wxgl_gzhgl_title_15" defVal="长度不能大于20" fileName="wxgl"/></font>
							</div>
							<%}else if("1".equals(type)){ %>
							<div style="float: left;">
								<label><%=otWeiGroup.getName()==null?"-": otWeiGroup.getName()%></label>
							</div>
							<%} %>
						</td>
					</tr>
					<tr>
                         <td>
                             <span><emp:message key="wxgl_gzhgl_title_82" defVal="用户数量：" fileName="wxgl"/></span>
                         </td>
                         <td>
                             <div style="float: left;">
								<label><%=otWeiGroup.getCount()==null?"0": otWeiGroup.getCount()%></label>
                             </div>
                         </td>
                     </tr>
                    <tr>
                         <td>
                             <span><emp:message key="wxgl_gzhgl_title_83" defVal="群组创建时间：" fileName="wxgl"/></span>
                         </td>
                         <td>
                             <div style="float: left;">
								<label><%=formatter.format(otWeiGroup.getCreatetime()) %></label>
                            </div>
                        </td>
                    </tr>

					</tbody>
				</table>
            </form>
			<%--</div>--%>
			<%-- 内容结束 --%>
			<div id="pageStuff" style="display:none;">
				<input type="hidden" id="pathUrl" value="<%=path%>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
				<input type="hidden" id="gid" value="<%=otWeiGroup.getGId()%>" />
				<input type="hidden" id="aid" value="<%=otWeiGroup.getAId()%>" />
				<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode%>" />
				<input type="hidden" id="lguserid" value="<%=lguserid%>" />
			</div>
		</div>
		
   		<div class="clear"></div>
   		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path %>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/yhgl_groupManager.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<link href="<%=path %>/wxcommon/css/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	<link href="wxcommon/<%=wxskin %>/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
		
	</body>
</html>