<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

	@ SuppressWarnings("unchecked")
	List<DynaBean> infos = (List<DynaBean>)request.getAttribute("enterpProList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	String webgate=SystemGlobals.getValue("montnets.webgate");
	if(webgate!=null){
		webgate=webgate.replaceAll("/sms/mt","/sms/v2/");
	}else{
		webgate="http://ip:port/sms/v2/";
	}
	String ecid  ="";
	if(request.getAttribute("ecid")!=null){
		ecid=request.getAttribute("ecid").toString();
	}
%>
<html>
	<head><%@include file="/common/common.jsp"%>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/frame/frame3.0/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/frame/frame3.0/skin/default/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			#updatemethod > div:nth-child(1) > center > table > tbody > tr > td:nth-child(1){
				text-align: left;
			}
		</style>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/wg_funList.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/wg_funList.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="wg_funList">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="" method="post" id="pageForm">
				<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
				<input type="hidden" id="lguserid" name="lguserid"
					value="<%=request.getParameter("lguserid") %>" />
				<input type="hidden" id="ipathUrl" value="<%=iPath %>" />
				<input type="hidden" id="commonPath" value="<%=commonPath %>" />
				<input id="pathUrl" value="<%=path%>" type="hidden" />
				<input id="ecid" value="<%=ecid%>" type="hidden" />
				<input id="funtype" value="<%=request.getAttribute("funtype")%>"
					type="hidden" />
				<div id="rContent" class="rContent rContent2"  >
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key='txgl_apimanage_text_75' defVal='梦网接口名' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_76' defVal='客户接口名' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_77' defVal='请求地址' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_78' defVal='请求格式' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_61' defVal='全成功回应' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_62' defVal='全失败回应' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_63' defVal='部分成功回应' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_114' defVal='详细信息回应' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_79' defVal='方法状态' fileName='mwadmin' />
								</th>
								<th>
									<emp:message key='txgl_apimanage_text_80' defVal='操作' fileName='mwadmin' />
								</th>
							</tr>
						</thead>
						<tbody>
							<%
								if (infos != null && infos.size() > 0)
								{
									for (DynaBean info : infos)
									{
								%>
							<tr>
								<td>
									<%=info.get("funname") %>
								</td>
								<td>
									<% 
										String cust_intfname = info.get("cust_intfname").toString();
										String cfunname = info.get("cfunname").toString();
										String clientall=cust_intfname+"("+cfunname+")";
										String client="";
										if(clientall.length()>8)
										{
											client = clientall.substring(0,8)+"...";
										}else
										{
											client = clientall;
										}
										%>
									<a onclick=javascript:showclient(this)> <label
											class="clientall_label">
											<xmp><%=clientall%></xmp>
										</label> <xmp><%=client%></xmp> </a>
								</td>
								<%String url=webgate+info.get("funtype")+"/"+ info.get("cfunname");%>
								<td>
									<% 
										String st1 = "";
										if(url.length()>8)
										{
											st1 = url.substring(0,8)+"...";
										}else
										{
											st1 = url;
										}
										%>
									<a onclick=javascript:modify(this)> <label
											class="url_label">
											<xmp><%=url%></xmp>
										</label> <xmp><%=st1 %></xmp> </a>
								</td>
								<td>
									<%
										String type="";
										String show="";
										if(info.get("reqtype")!=null){
											type=info.get("reqtype").toString();
										}
										if("1".equals(type)){
											show="XML";
										}else if("2".equals(type)){
											show="JSON";
										}else if("4".equals(type)){
											show="URLENCODE";
										}else{
											show = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_19",request);
										} 
										out.print(show);
									%>
								</td>
								<td>
									<%
									String resptype="";
									if(info.get("resptype")!=null){
										resptype=info.get("resptype").toString();
									}
									if("1".equals(resptype)){
										show="XML";
									}else if("2".equals(resptype)){
										show="JSON";
									}else if("4".equals(resptype)){
										show="URLENCODE";
									}else{
										show = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_19",request);
									} 
									out.print(show);
									
									%>
								</td>
								<td><%=show %></td>
								<td><%=show %></td>
								<td><%=show %></td>
								<td>

									<%
										String status="";
										if(info.get("status")!=null){
											status=info.get("status").toString();
										}
										String st="";
										if("0".equals(status)){
											st = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_48",request);
											out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_49",request));
										}else if("1".equals(status)){
											st = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_49",request);
											out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_48",request));
										}else{
											out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_116",request));
										} %>
								</td>
								<td>
									<a id="mapping" onclick="javascript:mapping('<%=info.get("funname")%>','<%=type%>','<%=resptype%>','<%=info.get("funtype")%>','<%=status%>')"><emp:message key='txgl_apimanage_text_81' defVal='映射' fileName='mwadmin'/></a>
									<a id="edit" onclick="javascript:edit('<%=ecid%>','<%=info.get("funtype") %>','<%=url%>','<%=info.get("funname") %>','<%=info.get("cust_intfname") %>','<%=info.get("cfunname") %>','<%=type%>','<%=resptype%>','<%=status%>')"><emp:message key='txgl_apimanage_text_82' defVal='编辑' fileName='mwadmin'/></a>
									<a id="del" onclick="javascript:del('<%=ecid%>','<%=info.get("funname") %>')"><emp:message key='txgl_apimanage_text_83' defVal='删除' fileName='mwadmin'/></a>
									<a id="changeStaute" onclick="javascript:changeStaute('<%=ecid%>','<%=info.get("funname") %>','<%=status%>')"><%=st%></a>
								</td>
							</tr>
							<%
									}
								}else{
									%>
							<tr>
								<td colspan="11">
									<emp:message key='txgl_apimanage_text_52' defVal='无记录' fileName='mwadmin'/>
								</td>
							</tr>
							<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="11">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</div>
				<div id="updatemethod" title="<emp:message key='txgl_apimanage_text_84' defVal='编辑方法' fileName='mwadmin'/>" class="updatemethod">
					<div class="updatemethod_down_div">
						<center>
							<table>
								<tr>
									<td>
										<span><emp:message key='txgl_apimanage_text_85' defVal='梦网接口名：' fileName='mwadmin'/></span>
									</td>
									<td align="left">
										<input type="text"  name="mwInter" id="mwInter" disabled="true" maxlength="21" class="input_bd updatemethod_table_input" />
									</td>
								</tr>
								<tr class="updatemethod_table_tr">
									<td colspan="2"></td>
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_apimanage_text_86' defVal='方法类型：' fileName='mwadmin'/>
									</td>
									<td align="left">
										<input type="text"  name="funType" id="funType" disabled="true" maxlength="21" class="input_bd updatemethod_table_input" />
									</td>
								</tr>
								<tr class="updatemethod_table_tr">
									<td colspan="2"></td>
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_apimanage_text_87' defVal='基本路径：' fileName='mwadmin'/>
									</td>
									<td align="left">
										<input type="text"  name="basePath" id="basePath" disabled="true" maxlength="21" class="input_bd updatemethod_table_input" />
									</td>
								</tr>
								<tr class="updatemethod_table_tr">
									<td colspan="2"></td>
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_apimanage_text_88' defVal='客户接口：' fileName='mwadmin'/>
									</td>
									<td align="left">
										<input type="text"  name="clientInterface" id="clientInterface" maxlength="21" class="input_bd updatemethod_table_input" />
									</td>
								</tr>
								<tr class="updatemethod_table_tr">
									<td colspan="2"></td>
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_apimanage_text_89' defVal='客户接口名称：' fileName='mwadmin'/>
									</td>
									<td align="left">
										<input type="text"  name="interfaceName" id="interfaceName" maxlength="21" class="input_bd updatemethod_table_input" />
									</td>
								</tr>

								<tr class="updatemethod_table_tr">
									<td colspan="2"></td>
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_apimanage_text_34' defVal='请求格式：' fileName='mwadmin'/>
									</td>
									<td align="left">
										<select class="req_type" id="req_type" name="req_type">
											<option value="1">
												XML
											</option>
											<option value="2">
												JSON
											</option>
											<option value="4">
												URLENCODE
											</option>
										</select>
									</td>
								</tr>
								<tr class="updatemethod_table_tr">
									<td colspan="2"></td>
								</tr>
								<tr>
									<td>
										<emp:message key='txgl_apimanage_text_36' defVal='回应格式：' fileName='mwadmin'/>
									</td>
									<td align="left">
										<select class="resp_type" id="resp_type" name="resp_type">
											<option value="0">
												<emp:message key='txgl_apimanage_text_19' defVal='无' fileName='mwadmin'/>
											</option>
											<option value="1">
												XML
											</option>
											<option value="2">
												JSON
											</option>
											<option value="4">
												URLENCODE
											</option>

										</select>
									</td>
								</tr>
							</table>
						</center>
					</div>
					<div class="tj_div">
						<center>
							<input id="blok" class="btnClass5 mr23" type="button" value="<emp:message key='txgl_apimanage_text_20' defVal='提  交' fileName='mwadmin'/>" onclick="javascript:save()" />
							<input id="blc" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<emp:message key='txgl_apimanage_text_21' defVal='取  消' fileName='mwadmin'/>" />
							<br />
						</center>
					</div>
				</div>
				<div id="modify" title="<emp:message key='txgl_apimanage_text_77' defVal='请求地址' fileName='mwadmin'/>"
					class="modify">
					<div id="msg" class="msg">
						<xmp></xmp>
					</div>
				</div>

				<div id="modifyshow" title="<emp:message key='txgl_apimanage_text_76' defVal='客户接口名' fileName='mwadmin'/>"
					class="modifyshow">
					<div id="msgshow"
						class="msgshow">
						<xmp></xmp>
					</div>
				</div>
				<%-- 内容结束 --%>

				<%-- 内容结束 --%>
				<div id="mappingDiv" title="<emp:message key='txgl_apimanage_text_115' defVal='API接口方法映射' fileName='mwadmin'/>" class="mappingDiv">
					<iframe id="mappingFrame" name="tempFrame" class="mappingFrame" marginwidth="0" scrolling="no" frameborder="no" src="<%=commonPath%>/common/blank.jsp "></iframe>
				</div>
				<%-- foot开始 --%>
				<div class="bottom">
					<div id="bottom_right">
						<div id="bottom_left"></div>
						<div id="bottom_main">
						</div>
					</div>
				</div>
			</form>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=iPath %>/js/apicommon.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/wg_funList.js"></script>
		<script type="text/javascript">
		//页面加载，初始化相关数据
		$(document).ready(function() {
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
		});
		//显示请求地址的详细信息
		function modify(t)
		{
			$('#modify').dialog({
				autoOpen: false,
				resizable: false,
				width:250,
			    height:200
			});
			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			$('#modify').dialog('open');
		}
		
				//显示请求地址的详细信息
		function showclient(t)
		{
			$('#modifyshow').dialog({
				autoOpen: false,
				resizable: false,
				width:250,
			    height:200
			});
			$("#msgshow").children("xmp").empty();
			$("#msgshow").children("xmp").text($(t).children("label").children("xmp").text());
			$('#modifyshow').dialog('open');
		}
		</script>
	</body>
</html>
