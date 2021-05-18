<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
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
String menuCode = titleMap.get("prcMonCfg");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> moniList = (List<DynaBean>) request
		.getAttribute("monitorList");

String hostname = request.getParameter("hostname");
String procename = request.getParameter("procename");
String proceid = request.getParameter("proceid");

%>

<!doctype html>
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
		<form name="pageForm" action="mon_prcMonCfg.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<a id="add" onclick="javascript:toAdd()"><emp:message key="ptjk_common_xj" defVal="新建" fileName="ptjk"/></a>
				<%if(btnMap.get(menuCode+"-1")!=null) { %>
				<%} %>
				<%--<a id="exportCondition">导出</a>--%>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td><emp:message key="ptjk_jkxq_cx_cxbh_mh" defVal="程序编号：" fileName="ptjk"/></td>
							<td>
								<input type="text" name="proceid" id="proceid" value="<%=proceid!=null?proceid:"" %>"/>
							</td>
							<td><emp:message key="ptjk_jkxq_cx_cxmc_mh" defVal="程序名称：" fileName="ptjk"/></td>
							<td>
								<input type="text" name="procename" id="procename" value="<%=procename!=null?procename:"" %>"/>
							</td>
							<td>
								<emp:message key="ptjk_jkgl_cx_1" defVal="所属主机名称：" fileName="ptjk"/>
							</td>
							<td >	
								<input type="text" name="hostname" id="hostname" value="<%=hostname!=null?hostname:"" %>"/>
							</td>
							<%--
							<td>
								程序类型：
							</td>
							<td >
								<select>
									<option>EMP</option>
									<option>数据库</option>
									<option>网关WBS</option>
									<option>网关SPGATe</option>
									<option>监控客户端</option>
									<option>监控服务端</option>
									<option>其他应用程序</option>
								</select>
							</td>
							--%>
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
						<th>
							<emp:message key="ptjk_jkxq_cx_cxbh" defVal="程序编号" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_cx_cxmc" defVal="程序名称" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_bbh" defVal="版本号" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_cxlx" defVal="程序类型" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_cx_2" defVal="所属主机编号" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_cx_3" defVal="所属主机名称" fileName="ptjk"/>
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
						<%--
						<th>
							连接数
						</th>
						--%>
						<th>
							<emp:message key="ptjk_common_jkzt" defVal="监控状态" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_xgsj" defVal="修改时间" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_cz" defVal="操作" fileName="ptjk"/>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
					if(moniList!=null && moniList.size()>0)
					{
						for(DynaBean sprc : moniList)
						{
				%>
				<tr>
					<td >
						<xmp>
						<%=sprc.get("proceid") %>
						</xmp>
					</td>
					<td >
						<xmp>
						<%=sprc.get("procename") %>
						</xmp>
					</td>
					<td>
						<xmp>
						<%=sprc.get("version")!=null?sprc.get("version"):"-" %>
						</xmp>
					</td>
					<td>
						<%
						String procetype = sprc.get("procetype").toString();
						if("5000".equals(procetype))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_webcx",request));
						}
						else if("5200".equals(procetype))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_empwg",request)+"(EMP_GW)");
						}
						else if("5300".equals(procetype))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_yysjk",request)+"(SPGATE)");
						}
						else if("5800".equals(procetype))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wjfwq",request));
						}
						else
						{
							out.print("-");
						}
						%>
					</td>
					<td>
						<xmp>
						<%=sprc.get("hostid")!=null?"-1".equals(sprc.get("hostid").toString())?"-":sprc.get("hostid"):"-" %>
						</xmp>
					</td>
					<td>
						<xmp>
						<%=sprc.get("hostname")!=null?sprc.get("hostname"):"-" %>
						</xmp>
					</td>
					<td>
						<%=sprc.get("cpubl")!=null?sprc.get("cpubl"):"0" %>%
					</td>
					<td>
						<%=sprc.get("memuse")!=null?sprc.get("memuse"):"0" %>
					</td>
					<td>
						<%=sprc.get("vmemuse")!=null?sprc.get("vmemuse"):"0" %>
					</td>
					<td>
						<%=sprc.get("harddiskspace")!=null?sprc.get("harddiskspace"):"0" %>
					</td>
					<%--
					<td>
						<%=sprc.get("dbconnum")!=null?sprc.get("dbconnum"):"" %>
					</td>
					--%>
					<td>
						<%
						String status = "0";
						if(sprc.get("monstatus")!=null){
							status = String.valueOf(sprc.get("monstatus"));
						}
					%>
						<%="1".equals(status)?MessageUtils.extractMessage("ptjk","ptjk_common_jk",request):MessageUtils.extractMessage("ptjk","ptjk_common_wjk",request) %>
					</td>
					<td>
						<%=sprc.get("modifytime")!=null?df.format(sprc.get("modifytime")):"" %>
					</td>
					<td>
						<%if(btnMap.get(menuCode+"-2")==null&&btnMap.get(menuCode+"-3")==null) {out.println("-");} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
						<a href="javascript:toEdit('<%=sprc.get("proceid") %>')" hidefocus="true"><emp:message key="ptjk_common_sz" defVal="设置" fileName="ptjk"/></a>
						<%}else{out.print("");} %>
						&nbsp;&nbsp;
						<%if(btnMap.get(menuCode+"-3")!=null) { %>
							<a href="javascript:del('<%=sprc.get("proceid") %>')" hidefocus="true"><emp:message key="ptjk_common_sc" defVal="删除" fileName="ptjk"/></a><br/>
						<%}else{out.print("");} %>
					 </td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="13"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="13">
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
	<script src="<%=iPath%>/js/prcMonCfg.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
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
