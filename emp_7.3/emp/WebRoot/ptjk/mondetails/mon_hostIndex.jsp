<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.monitor.constant.MonitorStaticValue"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("hostMon");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> moniList = (List<DynaBean>) request
		.getAttribute("monitorList");

String hostname = request.getParameter("hostname");
String hostid = request.getParameter("hostid");
String evttype = request.getParameter("evttype");
String hoststatus = request.getParameter("hoststatus");
String txtPage = request.getParameter("pageIndex");
String pageSize = request.getParameter("pageSize");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_index.jsp' starting page</title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
  	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style type="text/css">
		.c_selectBox{
			width: 208px!important;
		}
		.c_selectBox ul {
			width: 208px!important;
		}
		.c_selectBox ul li{
			width: 208px!important;
		}
	</style>
	
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="mon_hostMon.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<%--<a id="exportCondition">导出</a>--%>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
								<emp:message key="ptjk_common_zjbh_mh" defVal="主机编号：" fileName="ptjk"/>
							</td>
							<td >	
								<input type="text" name="hostid" id="hostid" value="<%=hostid!=null?hostid:"" %>"/>
							</td>
							
							<td><emp:message key="ptjk_common_zjmc_mh" defVal="主机名称：" fileName="ptjk"/></td>
							<td>
								<input type="text" name="hostname" id="hostname" value="<%=hostname!=null?hostname:"" %>"/>
							</td>
							<td>
								<emp:message key="ptjk_common_gjjb_mh" defVal="告警级别：" fileName="ptjk"/>
							</td>
							<td >
								<select id="evttype" name="evttype" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0" <%="0".equals(evttype)?"selected":"" %>><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<option value="1" <%="1".equals(evttype)?"selected":"" %>><emp:message key="ptjk_common_gj" defVal="警告" fileName="ptjk"/></option>
									<option value="2" <%="2".equals(evttype)?"selected":"" %>><emp:message key="ptjk_common_yz" defVal="严重" fileName="ptjk"/></option>
								</select>
							</td>
							<td class="tdSer">
							     <center><a id="search"></a></center>
						    </td>		
						</tr>
						<tr>
							<td>
								<emp:message key="ptjk_jkxq_zj_zjzt_mh" defVal="主机状态：" fileName="ptjk"/>
							</td>
							<td>
								<select name="hoststatus" id="hoststatus" style="width: 155px;" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="1" <%="1".equals(hoststatus)?"selected":"" %>><emp:message key="ptjk_jkxq_zj_zw" defVal="在网" fileName="ptjk"/></option>
									<option value="2" <%="2".equals(hoststatus)?"selected":"" %>><emp:message key="ptjk_jkxq_zj_tj" defVal="脱机" fileName="ptjk"/></option>
									<option value="0" <%="0".equals(hoststatus)?"selected":"" %>><emp:message key="ptjk_common_wz" defVal="未知" fileName="ptjk"/></option>
								</select>
							</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="info"></div>
		</form>
		</div>
			
		<%-- 内容结束 --%>
		
		<div id="modify" title="<emp:message key='ptjk_common_xxnr' defVal='信息内容' fileName='ptjk'/>"  style="padding:5px;width:300px;height:160px;display:none">
			<table width="100%">
				<thead>
					<tr style="padding-top:2px;margin-bottom: 2px;">
						<td style='word-break: break-all;'>
							<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
							 
						</td>
						 
					</tr>
				   <tr style="padding-top:2px;">
						<td>
						</td>
						</tr>
					 
				</thead>
			</table>
		</div>
		
			
		<%-- foot开始 --%>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
				<div id="bottom_main">
				</div>
			</div>
		</div>
	</div>
    <div class="clear"></div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/monPageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		getLoginInfo("#loginInfo");

		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
	    $("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
	    submitForm();
		$('#search').click(function(){
			//点击查询显示第一页
			$("#txtPage").val('1');
			submitForm();
		});
		//定时刷新时间
		var refreshTime = <%=MonitorStaticValue.getRefreshTime()%>;
		//window.clearInterval();
		//定时刷新
		reTimer=window.setInterval("submitForm()",refreshTime);

		$("#hoststatus,#evttype").isSearchSelect({'width':'152','isInput':false,'zindex':0});
		setInterval("refresh()",15*60*1000);//15分钟刷新一次页面
	});
	function submitForm()
	{
		var search = document.getElementById('search');
		if(search)
		{
			search.isClick = true;
		}
		window.parent.loading();
		
		var time=new Date();
		var evttype = $("#evttype").val();
		var procename = $("#procename").val();
		var hostid = $("#hostid").val();
		var hostname = $("#hostname").val();
		var hoststatus = $("#hoststatus").val();
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		$('#info').load("mon_hostMon.htm",{
			method:'getInfo',
			lgcorpcode:lgcorpcode,
			lguserid:lguserid,
			hostid:hostid,
			hostname:hostname,
			evttype:evttype,
			hoststatus:hoststatus,
			pageIndex : $("#txtPage").val()||<%=txtPage%>,
			pageSize : $("#pageSize").val()||<%=pageSize%>,
			time:time
		},function(){
			
		});
	}

	function refresh(){
		$("#pageForm").submit();
	}
	</script>
  </body>
</html>
