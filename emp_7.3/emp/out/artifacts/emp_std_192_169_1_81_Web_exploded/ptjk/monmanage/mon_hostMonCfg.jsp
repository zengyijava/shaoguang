<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
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
String menuCode = titleMap.get("hostMonCfg");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> moniList = (List<DynaBean>) request
		.getAttribute("monitorList");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String hostid = request.getParameter("hostid");
String hostname = request.getParameter("hostname");
String adapter1 = request.getParameter("adapter1");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_hostMoConfig.jsp' starting page</title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="mon_hostMonCfg.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<a id="add" onclick="javascript:toAdd()"><emp:message key="ptjk_common_xj" defVal="新建" fileName="ptjk"/></a>
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
								<input type="text" value="<%=hostid!=null?hostid:"" %>" name="hostid" id="hostid" />
							</td>
							<td>
								<emp:message key="ptjk_common_zjmc_mh" defVal="主机名称：" fileName="ptjk"/>
							</td>
							<td >	
								<input type="text" name="hostname" id="hostname" value="<%=hostname!=null?hostname:"" %>"/>
							</td>
							<td><emp:message key="ptjk_jkxq_zj_nwip_mh" defVal="内网IP：" fileName="ptjk"/></td>
							<td>
								<input type="text" name="adapter1" id="adapter1" value="<%=adapter1!=null?adapter1:"" %>"/>
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
						<th rowspan="2">
							<emp:message key="ptjk_common_zjbh" defVal="主机编号" fileName="ptjk"/>
						</th>
						<th rowspan="2">
							<emp:message key="ptjk_common_zjmc" defVal="主机名称" fileName="ptjk"/>
						</th>
						<th colspan="5">
							<emp:message key="ptjk_jkgl_zj_jbxx" defVal="基本信息" fileName="ptjk"/>
						</th>
						<th colspan="5">
							<emp:message key="ptjk_jkgl_zj_1" defVal="告警阀值信息" fileName="ptjk"/>
						</th>
						<th rowspan="2">
							<emp:message key="ptjk_common_jkzt" defVal="监控状态" fileName="ptjk"/>
						</th>
						<th rowspan="2">
							<emp:message key="ptjk_common_xgsj" defVal="修改时间" fileName="ptjk"/>
						</th>
						<th rowspan="2">
							<emp:message key="ptjk_common_cz" defVal="操作" fileName="ptjk"/>
						</th>
					</tr>
					<tr>
						<th>
							<emp:message key="ptjk_jkxq_zj_nwip" defVal="内网IP" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_zj_nc" defVal="内存(M)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_zj_cp" defVal="磁盘(M)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_zj_2" defVal="CPU信息" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_zj_czxt" defVal="操作系统" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_cpuzy" defVal="CPU占用" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_wlncsyl" defVal="物理内存使用量(M)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_xnncsyl" defVal="虚拟内存使用量(M)" fileName="ptjk"/>
						</th>
						 <th>
							<emp:message key="ptjk_common_cpsy" defVal="磁盘剩余(M)" fileName="ptjk"/>
						 </th>
						 <th>
							<emp:message key="ptjk_jkxq_zj_jcsg" defVal="进程数(个)" fileName="ptjk"/>
						 </th>
					</tr>
				</thead>
				<tbody>
				<%
					if(moniList!=null && moniList.size()>0)
					{
						for(DynaBean host : moniList)
						{
				%>
				<tr>
					<td >
						<%=host.get("hostid") %>
					</td>
					<td>
						<xmp>
						<%=host.get("hostname")==null?"":host.get("hostname")%>
						</xmp>
					</td>
					<td>
						<%=host.get("adapter1")!=null&&host.get("adapter1").toString().trim().length()>0?host.get("adapter1"):"-"%>
					</td>
					<td>
						<%=host.get("hostmem")==null?"":host.get("hostmem")%>
					</td>
					<td>
						<%=host.get("hosthd")==null?"":host.get("hosthd")%>
					</td>
					<td>
						<%=host.get("hostcpu")!=null&&host.get("hostcpu").toString().length()>0?host.get("hostcpu"):"-"%>
					</td>
					<td>
						<%=host.get("opersys")!=null&&host.get("opersys").toString().length()>0?host.get("opersys"):"-"%>
					</td>

					
					<td>
						<%=host.get("cpuusage")%>%
					</td>
					<td>
						<%=host.get("memusage")!=null?host.get("memusage"):"0" %>
					</td>
					<td>
						<%=host.get("vmemusage")!=null?host.get("vmemusage"):"0" %>
					</td>
					<td>
						<%=host.get("diskfreespace")!=null?host.get("diskfreespace"):"0" %>
					</td>
					<td>
						<%=host.get("processcnt")!=null?host.get("processcnt"):"0" %>
					</td>
					<td>
						<%
						String status = "0";
						if(host.get("monstatus")!=null){
							status = String.valueOf(host.get("monstatus"));
						}
					%>
						<%="1".equals(status)?MessageUtils.extractMessage("ptjk","ptjk_common_jk",request):MessageUtils.extractMessage("ptjk","ptjk_common_wjk",request) %>
					</td>
					<td>
						<%=host.get("modifytime")!=null?sdf.format(host.get("modifytime")):"" %>
					</td>
					<td>
						<%if(btnMap.get(menuCode+"-2")==null&&btnMap.get(menuCode+"-3")==null) {out.println("-");} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
							<a href="javascript:toEdit('<%=host.get("hostid")%>')" hidefocus="true"><emp:message key="ptjk_common_sz" defVal="设置" fileName="ptjk"/></a>
						<%}else{out.print("");} %>
						&nbsp;&nbsp;
						<%if(btnMap.get(menuCode+"-3")!=null) { %>
							<a href="javascript:del('<%=host.get("hostid") %>')" hidefocus="true"><emp:message key="ptjk_common_sc" defVal="删除" fileName="ptjk"/></a><br/>
						<%}else{out.print("");} %>
					 </td>  
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="15"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="15">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
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
		
		<div id="gateAccountDetails" style="display: none;">
			<iframe id="detailsFrame" name="detailsFrame" scrolling="no" src="" ></iframe>
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
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/hostMonCfg.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});
		});
	</script>
  </body>
</html>
