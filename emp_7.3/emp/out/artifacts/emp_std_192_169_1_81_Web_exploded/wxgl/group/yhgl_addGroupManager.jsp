<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiUserInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiGroup"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
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
    
	List<LfWeiAccount> otWeiAccList=(List<LfWeiAccount>)request.getAttribute("otWeiAccList");
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
		<link href="<%=commonPath%>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<body>
		<div id="container" style="width:290px; height:100px;">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<%-- 当前位置 --%>
			<%--<div id="rContent" class="rContent" style="overflow-y:hidden;overflow-x:hidden">--%>
			<form name="groupDescForm" action="" method="post" id="groupDescForm">
				<div style="display:none" id="hiddenValueDiv">
				</div>
				<table id="addGroupTable" style="width: 300px; height:120px;">
				<tbody>
					<tr>
                        <td style="width: <%=StaticValue.ZH_HK.equals(langName)?"100":"70" %>px;">
                            <span><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></span>
                        </td>
						<td>
							<label>
								<select id="aid" name="aid" class="input_bd">
									<%
									if(null != otWeiAccList && otWeiAccList.size()>0){
		                                for(LfWeiAccount acct : otWeiAccList){
									%>
									<option value="<%=acct.getAId() %>"> <%=acct.getName() %></option>
									<%
										}
									}
									%>
								</select>
							</label>
						</td>
					</tr>
					<tr>
                         <td style="width: <%=StaticValue.ZH_HK.equals(langName)?"100":"70" %>px;">
                             <span><emp:message key="wxgl_gzhgl_title_80" defVal="群组名称：" fileName="wxgl"/></span>
                         </td>
                         <td>
                             <div style="float: left;">
                                 <label>
							<input name="gname" id="gname" class="input_bd" type="text"  maxlength="20" />
							</label><font color="red" style="display:none;"><emp:message key="wxgl_gzhgl_title_15" defVal="长度不能大于20" fileName="wxgl"/></font>
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
		<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(function(){
			  getLoginInfo("#hiddenValueDiv");
			  //类型默认选中公众帐号类型
			<%-- $('#aid').isSearchSelect({'width':'150','height':'60','isInput':false,'zindex':0},function(){
				  judgeAccIsSynched();
				});--%>
		   	})
		</script>
		<script type="text/javascript" src="<%=iPath%>/js/yhgl_groupManager.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>