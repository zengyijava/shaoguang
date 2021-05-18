<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.util.Collection"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.common.constant.MonOnluserParams"%>
<%@page import="com.montnets.emp.entity.monitoronline.LfMonOnlcfg"%>
<%@page import="com.montnets.emp.monitoruser.servlet.mon_onlineUserNumSvt"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.monitoronline.LfMonOnluser"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	List<LfMonOnluser> onlUserDetail = (ArrayList<LfMonOnluser>)request.getAttribute("pageList");
	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("onlineUserNum");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String corpCode = request.getParameter("corpCode");
	String userName = request.getParameter("userName");
	String name = request.getParameter("name");
	String depName = request.getParameter("depName");
	LfMonOnlcfg molcfg = StaticValue.getMonOnlinecfg();
	String freq = "30000";//默认30s刷新一次
	if(molcfg.getMonfreq()!=null&&molcfg.getMonfreq()>0){
		freq = String.valueOf(molcfg.getMonfreq()*1000);
	}
	mon_onlineUserNumSvt mon_onlineUserNum = new mon_onlineUserNumSvt();
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> userMap=(LinkedHashMap<String,String>)request.getAttribute("userMap");//按钮权限Map
	if(userMap==null)
	{
		userMap = new LinkedHashMap<String, String>();
	}
	@ SuppressWarnings("unchecked")
	LinkedHashMap<Long, String> depMap=(LinkedHashMap<Long,String>)request.getAttribute("depMap");//按钮权限Map
	if(depMap==null)
	{
		depMap = new LinkedHashMap<Long, String>();
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
   	<head>
	<title><emp:message key="ptjk_jkxq_zxyhs_4" defVal="在线用户详情" fileName="ptjk"/></title>
	<%@include file="/common/common.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  
  <body>
   		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("ptjk","ptjk_jkxq_zxyhs_4",request)) %>
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<form name="pageForm" action="<%=path %>/mon_onlineUserNum.htm?method=getOnlineUserInfo" method="post" id="pageForm">
			<div class="buttons">
				<div id="toggleDiv" ></div>
				<span id="backgo" class="right mr5" onclick="javascript:location.href='<%=path%>/mon_onlineUserNum.htm?method=find'">&nbsp;<emp:message key="ptjk_common_fh" defVal="返回" fileName="ptjk"/></span>
			</div>
			<div id="getOnlineUserDetail" style="padding:5px;display:none;"></div>
				<div id="condition" >
					 <table>
						<tbody>
							<tr>
							    <%--
							     <td>企业编号：</td>
								<td>
									<label>
										<input type="text" style="width: 177px;" value='<%=null==corpCode?"":corpCode%>' id="corpCode" name="corpCode" maxlength="25"/>
									</label>
								</td> --%>
							    <td><emp:message key="ptjk_jkxq_zxyhs_dlzh_mh" defVal="登录账号：" fileName="ptjk"/></td>
								<td>
									<label>
										<input type="text" style="width: 177px;" value='<%=null==userName?"":userName%>' id="userName" name="userName" maxlength="25"/>
									</label>
								</td>
								<td><emp:message key="ptjk_jkxq_zxyhs_5" defVal="操作员名称：" fileName="ptjk"/></td>
								<td>
									<label>
										<input type="text" style="width: 177px;" value='<%=null==name?"":name%>' id="name" name="name" maxlength="25"/>
									</label>
								</td>
								<td><emp:message key="ptjk_jkxq_zxyhs_ssjg_mh" defVal="所属机构：" fileName="ptjk"/></td>
								<td>
									<label>
										<input type="text" style="width: 177px;" value='<%=null==depName?"":depName%>' id="depName" name="depName" maxlength="25"/>
									</label>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<table id="content">
						<thead>
							<tr>
								<th width="320px;">
									<emp:message key="ptjk_jkxq_zxyhs_hhbh" defVal="会话编号" fileName="ptjk"/>
								</th>
								<%--
								<th>
									企业编号
								</th>
								 --%>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_dlzh" defVal="登录账号" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_6" defVal="操作员名称" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_ssjg" defVal="所属机构" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_dlsj" defVal="登录时间" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_jkxq_zxyhs_zxsc" defVal="在线时长" fileName="ptjk"/>
								</th>
							</tr>
						</thead>
						<tbody>
						<%
						if (onlUserDetail != null && onlUserDetail.size() > 0)
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String logintime ="-";
							for (LfMonOnluser monOnluser : onlUserDetail) 
							{
								if(monOnluser.getLogintime()!=null){
									logintime = sdf.format(monOnluser.getLogintime());
								}
						%>
							<tr>
								<td>
									<%=monOnluser.getSesseionid()!=null&&!"".equals(monOnluser.getSesseionid())?monOnluser.getSesseionid():"-"%>
								</td>
								<%--
								<td>
									<%=monOnluser.getCorpcode()!=null&&!"".equals(monOnluser.getCorpcode())?monOnluser.getCorpcode():"-" %>
								</td>
								  --%>
								<td>
									<%=monOnluser.getUsername()!=null&&!"".equals(monOnluser.getUsername())?monOnluser.getUsername():"-" %>
								</td>
								<td>
									<%=userMap.get(monOnluser.getUsername())!=null&&!"".equals(userMap.get(monOnluser.getUsername()))?userMap.get(monOnluser.getUsername()):"-" %>
								</td>
								<td>
									<%=depMap.get(monOnluser.getDepid())!=null&&!"".equals(depMap.get(monOnluser.getDepid()))?depMap.get(monOnluser.getDepid()):"-" %>
								</td>
								<td>
									<%=logintime %>
								</td>
								<%
									String onlineTime=mon_onlineUserNum.getOnlineTime(logintime);
									onlineTime=onlineTime.replaceAll("天",MessageUtils.extractMessage("ptjk","ptjk_jkgl_zxyhs_t",request));
									onlineTime=onlineTime.replaceAll("小时",MessageUtils.extractMessage("ptjk","ptjk_jkgl_zxyhs_xs",request));
									onlineTime=onlineTime.replaceAll("分钟",MessageUtils.extractMessage("ptjk","ptjk_jkgl_zxyhs_fz",request));
									onlineTime=onlineTime.replaceAll("秒",MessageUtils.extractMessage("ptjk","ptjk_jkgl_zxyhs_m",request));
								 %>
								<td>
									<%=onlineTime%>
								</td>
							</tr>
						<%
							}
						}else{
						%>
						<tr><td align="center" colspan="6"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script>	
			$(document).ready(function() {
				$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$('#search').click(function(){submitForm();});
				});
				
	        function submitForm(){
	        var search = document.getElementById('search');
			if(search)
			{
				search.isClick = true;
			}
			window.parent.loading();

        	document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
        	document.forms['pageForm'].submit();
        }
	        //setTimeout('submitForm()',<%=freq%>); 
			</script>			
  </body>
</html>
