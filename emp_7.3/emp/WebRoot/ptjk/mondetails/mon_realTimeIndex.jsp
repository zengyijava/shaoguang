<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("hisMon");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> moniList = (List<DynaBean>) request
		.getAttribute("monitorList");

String msg = request.getParameter("msg");
String evttype = request.getParameter("evttype");
String recvtime = request.getParameter("recvtime");
String sendtime = request.getParameter("sendtime");
String montype = request.getParameter("montype");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_hostMoConfig.jsp' starting page</title>
    
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
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
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
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="mon_hisMon.htm" method="post" id="pageForm">
			<div id="loginInfo"></div>
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
								监控类型
							</td>
							<td >	
								<select id="montype" id="montype" isInput="false">
									<option value="0">全部</option>
									<option value="1" <%="1".equals(montype)?"selected":"" %>>主机</option>
									<option value="2" <%="2".equals(montype)?"selected":"" %>>程序</option>
								</select>
							</td>
							<td>
								告警级别
							</td>
							<td >
								<select id="evttype" name="evttype" isInput="false">
									<option value="">全部</option>
									<option value="10" <%="10".equals(evttype)?"selected":"" %>>严重</option>
									<option value="20" <%="20".equals(evttype)?"selected":"" %>>警告</option>
								</select>
							</td>
							<td>事件内容</td>
							<td>
								<input type="text" name="msg" id="msg" value="<%=msg!=null?msg:"" %>"/>
							</td>
							
							<td class="tdSer">
							     <center><a id="search"></a></center>
						    </td>		
						</tr>
						<tr>
							<td>
								告警时间
							</td>
							<td >
								<input type="text"
											style="cursor: pointer; width: 150px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												>
							</td>
							<td>至：</td>
							<td>
								<input type="text"
											style="cursor: pointer; width: 150px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" >
							</td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th>
							监控类型
						</th>
						<th>
							名称
						</th>
						<th>
							级别
						</th>
						<th>
							事件描述
						</th>
						<th>
							告警时间
						</th>
						<th>
							抛出者
						</th>
						<th>
							处理状态
						</th>
						<th>
							处理者
						</th>
						<th>
							处理时间
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
						<%
						if(host.get("hostid")!=null )
						{
							out.print("主机信息");
						}
						else if(host.get("proceid")!=null)
						{
							out.print("程序信息");
						}
						else
						{
							out.print("未知信息");
						}
						%>
					</td>
					<td>
					<xmp>
						<%
						if(host.get("hostid")!=null )
						{
							out.print(host.get("hostname"));
						}
						else if(host.get("proceid")!=null)
						{
							out.print(host.get("procename"));
						}
						else
						{
							out.print("未知信息");
						}
						%>
					</xmp>
					</td>
					<%
						String evttypeStr = host.get("evttype").toString();
						if("10".equals(evttypeStr))
						{
							out.print("<td class='warn'>警告</td>");
						}
						else if("20".equals(evttypeStr))
						{
							out.print("<td class='bad'>严重</td>");
						}
						else
						{
							out.print("<td class='natural'>正常</td>");
						}
					%>
					<td>
						<xmp>
						<%=host.get("msg") %>
						</xmp>
					</td>
					<td>
						<%=host.get("evttime")!=null?df.format(host.get("evttime")):"" %>
					</td>
					<td>
						<%=host.get("who")!=null?host.get("who"):"没有事件抛出者"%>
					</td>
					<td>
						<%
						String dealflag = host.get("dealflag").toString();
						if("0".equals(dealflag))
						{
							out.print("自动恢复");
						}
						else if("1".equals(dealflag))
						{
							out.print("新事件");
						}
						else if("2".equals(dealflag))
						{
							out.print("处理中");
						}
						else if("0".equals(dealflag))
						{
							out.print("人工处理");
						}
						%>
					</td>
					<td>
						<%=host.get("dealman")!=null?host.get("dealman"):"暂无处理者" %>
					</td>
					<td>
						<%=host.get("adapter1")!=null?host.get("adapter1"):"" %>
					</td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="13">无记录</td></tr>
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
		
		<div id="modify" title="信息内容"  style="padding:5px;width:300px;height:160px;display:none">
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
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/hisMon.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
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
