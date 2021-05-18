<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.notice.vo.LfNoticeVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute("emp_lang");
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath=iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath=inheritPath.substring(0,inheritPath.lastIndexOf("/"));
PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
List<LfNoticeVo> lnList = (List<LfNoticeVo>)request.getAttribute("allList");
//LfSysuser user = (LfSysuser)session.getAttribute("loginSysuser");
String username = (String)request.getAttribute("lgusername");
String type = (String)request.getAttribute("type");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("notice");
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/common/common.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
    <link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=iPath%>/css/notice.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/not_notice.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/not_notice.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	<script>
	var iPath="<%=iPath%>";
	</script>
	</head>

		<body id="not_notice">
		
		<div id="addNotice" title="<emp:message key='xtgl_gg_gglb_fbgg' defVal='发布公告' fileName='xtgl'/>" class="addNotice">
			<div>
			<center>
			<input type="hidden" id="nid" value=""/>
			<table>
			<tr><td><emp:message key="xtgl_gg_gglb_bt_mh" defVal="标题：" fileName="xtgl"/></td><td><input type="text" id="title" value=""  class="input_bd title"/ maxlength="20"></td></tr>
			<tr><td height="5px;"></td></tr>
			<tr><td><emp:message key="xtgl_gg_gglb_nr_mh" defVal="内容：" fileName="xtgl"/></td><td><textarea id="cont"  class="input_bd cont"></textarea></td></tr>
			<tr><td height="5px;"></td></tr>
			</table>
			</center>
			</div>
			<div class="gglb_fb_div">
			    <center>
			<input type="button" value="<emp:message key='xtgl_gg_gglb_fb' defVal='发布' fileName='xtgl'/>" onclick="doOk()" class="btnClass5 mr23"/>
			<input type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>"  class="btnClass6" onclick="doCancel()"/>
			<br/>
             </center>
	    	</div>
			</div>
		<div id="Notices" title="<emp:message key='xtgl_gg_gglb_ggnr' defVal='公告内容' fileName='xtgl'/>" class="Notices">
			      <table>
			        <tr><td><emp:message key="xtgl_gg_gglb_fbr_gs" defVal="发&nbsp;&nbsp;布&nbsp;&nbsp;人：" fileName="xtgl"/></td><td><input type="text" id="user" value=""  readonly="readonly" onfocus="this.blur()"  class="input_bd user"/></td></tr>
					<tr><td height="5px;"></td></tr>
					<tr><td><emp:message key="xtgl_gg_gglb_fbsj_mh" defVal="发布时间：" fileName="xtgl"/></td><td><input type="text" id="ttime" value=""  readonly="readonly" onfocus="this.blur()"  class="input_bd ttime"/></td></tr>
					<tr><td height="5px;"></td></tr>
					<tr><td><emp:message key="xtgl_gg_gglb_bt_gs" defVal="标　　题：" fileName="xtgl"/></td><td><input type="text" id="ttitle" value=""   readonly="readonly" onfocus="this.blur()"  class="input_bd ttitle"/></td></tr>
					<tr><td height="5px;"></td></tr>
					<tr><td valign="top"><emp:message key="xtgl_gg_gglb_nr_gs" defVal="内　　容：" fileName="xtgl"/></td><td><textarea readonly="readonly" id="tcont"  class="input_bd tcont"></textarea></td></tr>
					<tr><td height="5px;"></td></tr>
					</table>
			</div>
		<div id="container" class="container">
			<%
			if("1".equals(type))
			{
				out.print(com.montnets.emp.common.constant.ViewParams.getPositionWhitIn(MessageUtils.extractMessage("xtgl","xtgl_gg_gglb_sy",request),MessageUtils.extractMessage("xtgl","xtgl_gg_gglb_ggck",request)));
			}
			else
			{
				out.print(com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode));
			} %>
				<div id="rContent" class="rContent">
						<div class="buttons rContent_div"  >
							<a class="addNoti" onclick="javascript:doAdd();"><emp:message key="xtgl_gg_gglb_fbgg" defVal="发布公告" fileName="xtgl"/></a>
						</div>
				<form name="pageForm" action="not_notice.htm?method=find" method="post"
					id="pageForm">
					<%-- 表示是在首页还是系统管理下的公告 --%>
					<input type="hidden" name="type" id="type" value="<%=type!=null?type:"" %>"/>
					<%-- contextPath--%>
					<input type="hidden" name="contextPath" id="contextPath" value="<%=request.getContextPath()%>"/>
					<div id="loginparams" class="hidden"></div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="xtgl_gg_gglb_bt" defVal="标题" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_gg_gglb_nr" defVal="内容" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_gg_gglb_fbr" defVal="发布人" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_gg_gglb_fbsj" defVal="发布时间" fileName="xtgl"/>
								</th>
								<%if("admin".equals(username) || "sysadmin".equals(username)){ %>
								<th colspan="2">
									<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
								</th>
								<%} %>
							</tr>
						</thead>
						<tbody>
								<%
								if (lnList != null && lnList.size() > 0)
								{
									for (LfNoticeVo ln : lnList)
									{
								%>	
								<tr>
								<td>
								 <a onclick="showDetail('<%=ln.getNoticeID() %>')">
								    <xmp><%=ln.getTitle().length()>20?ln.getTitle().toString().substring(0,20)+"...":ln.getTitle() %></xmp>
								</a>
								</td>
								<td>
								<xmp><%=ln.getContext()!=null?ln.getContext().replaceAll("\\\\n"," ").length()>20?ln.getContext().replaceAll("\\\\n"," ").substring(0,20)+"...":ln.getContext().replaceAll("\\\\n"," "):"-" %></xmp>
								</td>
								<td> 
								<xmp><%=ln.getName()!=null?ln.getName():"" %></xmp>
								</td>
								<td><%=df.format(ln.getPublishTime()) %></td>
								<%if("admin".equals(username) || "sysadmin".equals(username)){ %>
								<td>
								<a onclick="javascript:doEdit('<%=ln.getNoticeID() %>')"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
								</td>
								<td>
								<a onclick="javascript:doDel('<%=ln.getNoticeID() %>')"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
								</td>
								<%} %>
								</tr>
								<%}
								}
								else
								{
								%>
								<tr><td colspan="6" align="center"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
								<%} %>
								</tbody>
								<tfoot>
							<tr>
								<td colspan="6">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</form>
					</div>
				</div>
			<%-- 内容结束 --%>
			<div class="clear"></div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=iPath %>/js/not_notice.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        getLoginInfo("#loginparams");
        $("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
    	initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
    });
    </script>
	</body>
</html>
