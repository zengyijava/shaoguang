<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonHostnet"%>
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
String menuCode = titleMap.get("webHostMonNet");
menuCode = menuCode==null?"0-0-0":menuCode;
PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");
String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<LfMonHostnet> dbstateList = (List<LfMonHostnet>) request.getAttribute("dbstateList");
List<LfMonHostnet> pageList = (List<LfMonHostnet>) request.getAttribute("pageList");

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//web主机名称
String webname = request.getParameter("webname");
//监控主机名称
String hostname = request.getParameter("hostname");
//告警级别
String evttype = request.getParameter("evttype");
//web主机所属节点
String webnode = request.getParameter("webnode");
//监控状态
String monstatus = request.getParameter("monstatus");
//网络状态
String netstate = request.getParameter("netstate");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
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
	<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
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
		<form name="pageForm" action="mon_webHostMonNet.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<a id="add" onclick="javascript:void(0)"><emp:message key="ptjk_common_sz" defVal="设置" fileName="ptjk"/></a>
				<%--<a id="exportCondition">导出</a>--%>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
								<emp:message key="ptjk_wlgk_web_1" defVal="WEB主机名称：" fileName="ptjk"/>
							</td>
							<td >
								<input type="text" name="webname" id="webname" value="<%=webname!=null?webname:"" %>"/>
							</td>
							<td>
								<emp:message key="ptjk_wlgk_web_2" defVal="监控主机名称：" fileName="ptjk"/>
							</td>
							<td >	
								<input type="text" name="hostname" id="hostname" value="<%=hostname!=null?hostname:"" %>"/>
							</td>
							<td>
								<emp:message key="ptjk_common_gjjb_mh" defVal="告警级别：" fileName="ptjk"/>
							</td>
							<td >
								<select id="evttype" name="evttype" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0" <%="0".equals(evttype)?"selected":"" %>><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<%--<option value="1" <%="1".equals(evttype)?"selected":"" %>>警告</option>
									--%><option value="2" <%="2".equals(evttype)?"selected":"" %>><emp:message key="ptjk_common_yz" defVal="严重" fileName="ptjk"/></option>
								</select>
							</td>
							<td class="tdSer">
							       <center><a id="search"></a></center>
						    </td>		
						</tr>
						<tr>
							<td>
								<emp:message key="ptjk_wlgk_web_3" defVal="WEB主机所属节点：" fileName="ptjk"/>
							</td>
							<td >	
								<input type="text" name="webnode" id="webnode" value="<%=webnode!=null?webnode:"" %>"/>
							</td>
							<td>
								<emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/>
							</td>
							<td >	
								<select name="monstatus" id="monstatus" style="" class="input_bd" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="1" <%="1".equals(monstatus)?"selected":"" %>><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
									<option value="0" <%="0".equals(monstatus)?"selected":"" %>><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
								</select>
							</td>
							<td>
								<emp:message key="ptjk_wlgk_web_wlzt_mh" defVal="网络状态：" fileName="ptjk"/>
							</td>
							<td >	
								<select name="netstate" id="netstate" style="" class="input_bd" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0" <%="0".equals(netstate)?"selected":"" %>><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<option value="1" <%="1".equals(netstate)?"selected":"" %>><emp:message key="ptjk_common_dk" defVal="断开" fileName="ptjk"/></option>
								</select>
							</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th>
							<emp:message key="ptjk_wlgk_web_4" defVal="WEB主机名称" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_wlgk_web_5" defVal="WEB主机所属节点" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_wlgk_web_6" defVal="监控主机名称" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_wlgk_web_7" defVal="监控主机IP" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_wlgk_web_wlzt" defVal=" 网络状态" fileName="ptjk"/>
						</th>
						<%--<th>
							更新时间
						</th>
						--%><th>
							<emp:message key="ptjk_common_jkzt" defVal="监控状态" fileName="ptjk"/>
						</th>
					</tr>
					
				</thead>
				<tbody>
				<%
					if(pageList!=null && pageList.size()>0)
					{
						for(LfMonHostnet mondbstate : pageList)
						{
				%>
				<tr>
					<td >
						<xmp>
						<%=mondbstate.getWebname()!=null?mondbstate.getWebname():"" %>
						</xmp>
					</td>
					<td>
						<xmp>
						<%=mondbstate.getWebnode()!=null?mondbstate.getWebnode():"" %>
						</xmp>
					</td>
					<td>
						<xmp>
						<%=mondbstate.getHostname()!=null?mondbstate.getHostname():"" %>
						</xmp>
					</td>
					<td>
						<xmp>
						<%=mondbstate.getIpaddr()!=null?mondbstate.getIpaddr():"" %>
						</xmp>
					</td>
				<%
					String evttypeStr = mondbstate.getEvttype()!=null?mondbstate.getEvttype().toString():"30";
					if("1".equals(evttypeStr))
					{
						out.print("<td class='warn'>"+MessageUtils.extractMessage("ptjk","ptjk_common_jg",request)+"</td>");
					}
					else if("2".equals(evttypeStr))
					{
						out.print("<td class='bad'>"+MessageUtils.extractMessage("ptjk","ptjk_common_yz",request)+"</td>");
					}
					else
					{
						out.print("<td class='natural'>"+MessageUtils.extractMessage("ptjk","ptjk_common_zc",request)+"</td>");
					}
					%>
					<%
					String netstatestr = mondbstate.getNetstate()!=null?mondbstate.getNetstate().toString():"30";
					if("0".equals(netstatestr))
					{
						out.print("<td>"+MessageUtils.extractMessage("ptjk","ptjk_common_zc",request)+"</td>");
					}
					else if("1".equals(netstatestr))
					{
						out.print("<td>"+MessageUtils.extractMessage("ptjk","ptjk_common_dk",request)+"</td>");
					}
					else
					{
						out.print("<td>"+MessageUtils.extractMessage("ptjk","ptjk_common_zc",request)+"</td>");
					}
					%>
					<%--<td>
						<xmp>
						<%=mondbstate.getUpdatetime()!=null?sdf.format(mondbstate.getUpdatetime()):"" %>
						</xmp>
					</td>
					--%><td class="ztalign">
						<center>
							<%
							if (mondbstate.getMonstatus() == 1){
							%>
								<select  name="gateState<%=mondbstate.getId() %>" id="gateState<%=mondbstate.getId() %>" class="input_bd" onchange="javascript:changestate('<%=mondbstate.getId() %>')">
												 <option value="1" selected="selected"><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
										         <option value="0" ><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
								</select>
							<%
							}else{
							%>
							<select  name="gateState<%=mondbstate.getId() %>" id="gateState<%=mondbstate.getId() %>" class="input_bd"  onchange="javascript:changestate('<%=mondbstate.getId() %>')">
												<option value="0" selected="selected"><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
										         <option value="1" ><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>									          
							</select>
							<%	
							}
							%>
						</center>
						</td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="14"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="14">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
		</div>
		<div id="setDiv" title="<emp:message key='ptjk_wlgk_web_8' defVal='设置WEB主机网络监控信息' fileName='ptjk'/>" style="display:none;">
			<iframe id="setFrame" name="setFrame" style="width:540px;height:220px;border: 0;" marginwidth="0"  frameborder="no" style="overflow:scroll;overflow-y:hidden"></iframe>				
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
	<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/hostMonNet.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
		$(document).ready(function() {

            $('#condition input[id="hostname"]').unbind();

			$("#content select").empSelect({width:80});
			$('#content select').each(function(){
				$(this).next().hide();
				$(this).before('<div class="selectBefore">'+$(this).find('option:selected').text()+'</div>');
		  	});
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
				$(this).find('select').next().show().siblings().hide();
			}, function() {
				$(this).removeClass("hoverColor");
				var $select = $(this).find('select');
				$select.next().hide();
				$select.prev().show();
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});
			
			
			$('#add').click(function(){
				$("#setFrame").attr("src","<%=path %>/mon_webHostMonNet.htm?method=getNetWarn");
				$("#setDiv").dialog("open");
			});
			
			$("#setDiv").dialog({
				autoOpen: false,
				 width:552,
				resizable:false,
				modal: true,
				open:function(){
					
				}
			});
		});
		
		function doCancel(obj){
			$(obj).dialog('close');
		}
		
	</script>
  </body>
</html>
