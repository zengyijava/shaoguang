<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.perfect.vo.PerfectNoticeVo" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %> 
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 

	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<PerfectNoticeVo> pnoticeVos =(List<PerfectNoticeVo>) request.getAttribute("perfectNotics");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("queryNotHis");
	
	
	String sendName = request.getParameter("senderName");
//	String sendType  = request.getParameter("sendType");

	String content = request.getParameter("content");
	String sendTime = request.getParameter("sendTime");
	String recvTime = request.getParameter("recvTime");
	boolean isBack = request.getParameter("isback")==null?false:true;//是否返回操作
	if(isBack){
		sendName = request.getAttribute("senderName")==null?"":String.valueOf(request.getAttribute("senderName"));
		content = request.getAttribute("content")==null?"":String.valueOf(request.getAttribute("content"));
		sendTime = request.getAttribute("sendTime")==null?"":String.valueOf(request.getAttribute("sendTime"));
		recvTime = request.getAttribute("recvTime")==null?"":String.valueOf(request.getAttribute("recvTime"));
	}
	//不需要隐藏号码
	String isHideNumber = "2";
	if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) { 
		isHideNumber = "1";
	}
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String sendTaskId = (String)request.getAttribute("sendTaskId");
//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_133" defVal="完美通知历史记录" fileName="dxzs"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/per_queryNotHis.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_WanMeiTongZhi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="queryNotHis">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" value="<%=path %>" id="pathUrl" name="pathUrl">
			<form name="pageForm" action="<%=path %>/per_queryNotHis.htm" method="post" id="pageForm">
						<div class="buttons">
							<div id="toggleDiv" >
							</div>
						</div>
						<div id="getloginUser">
						</div>
						<%--设置需要不需要隐藏手机号码   1是需要隐藏   2是不需要隐藏 --%>
						<input type="hidden" id="isHideNumber" name="isHideNumber" value="<%=isHideNumber %>"/>
					
						<div id="condition" >
						 <table>
								<tbody>
								<tr>
								    <td>
									<emp:message key="dxzs_xtnrqf_title_134" defVal="发送者名称" fileName="dxzs"/>：
									</td>
									<td>
										<label>
											<input type="text" value='<%=null==sendName?"":sendName%>' id="senderName" name="senderName" maxlength="25"/>
										</label>
									</td>
									
									<td>
										<emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>：
									</td>
									<td>
										<label>
											<input class="dxzs_input" type="text" value='<%=null==content?"":content%>'  name="content" maxlength="25"/>
										</label>
									</td>
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
								
								</tr>
								<tr>
									<td>
										<emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>：
									</td>
									<td class="tableTime">

										<input type="text"  class="Wdate" readonly="readonly" onclick="stime()"
											 value="<%=null==sendTime?"":sendTime%>" id="sendTime" name="sendTime">
									</td>
									<td align="left">
										<emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>
									</td>
									<td>
										<input type="text"  class="Wdate" readonly="readonly" onclick="rtime()"
											 value="<%=null==recvTime?"":recvTime%>" id="recvTime" name="recvTime">
									</td>
									<td >
									</td>
							
								</tr>
								
							</tbody>
							</table>
						</div>
			
					<table id="content" width="100%">
						<thead>
							<tr>
								<th width="15%">
								   	 <emp:message key="dxzs_xtnrqf_title_134" defVal="发送者名称" fileName="dxzs"/>
								</th>
								<th width="20%">
									<emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>
								</th>
								<th width="30%">
									<emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>
								</th>
								<th width="9%">
									<emp:message key="dxzs_xtnrqf_title_136" defVal="通知人数" fileName="dxzs"/>
								</th>
								<th width="9%">
									<emp:message key="dxzs_xtnrqf_title_137" defVal="送达人数" fileName="dxzs"/>
								</th>
								<th width="9%">
									<emp:message key="dxzs_xtnrqf_title_138" defVal="回复人数" fileName="dxzs"/>
								</th>
								<th width="8%">
									<emp:message key="dxzs_xtnrqf_button_13" defVal="详情" fileName="dxzs"/>
								</th>
							</tr>
						</thead>
						<tbody>
								<%
									if(pnoticeVos != null && pnoticeVos.size()>0)
									{
										for(PerfectNoticeVo pn : pnoticeVos)
										{
											if(sendTaskId != null && !"".equals(sendTaskId) && Long.valueOf(sendTaskId) - pn.getTaskId() == 0)
											{
											%>
												<tr class="peachpuff">
											<%
											}
											else
											{ %>
												<tr>
											<%} %>
										<td class="textalign">
											<% 
													if(pn.getRecevierType() == 1){
													%>
														 <%=pn.getSenderName().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
													<% 
													}else{
											%>
											    <%=pn.getSenderName().replaceAll("<","&lt;").replaceAll(">","&gt;") %><font color="red">(<emp:message key="dxzs_xtnrqf_title_139" defVal="已注销" fileName="dxzs"/>)</font>
											<% 
													}
											%>
										</td>
										<td>
											<%=df.format(pn.getSubmitTime()) %>
										</td>
										<td class="textalign">
										<%
										 if(!"".equals(pn.getContent())&&pn.getContent()!=null){
											String st = "";
											String sign = "";
											if(pn.getArySendCount()==1){
												sign = String.format(DxzsStaticValue.getPERFECT_SIGN_NAME(), pn.getSenderName()).replace("完美通知", MessageUtils.extractMessage("dxzs","dxzs_PERFECT_SIGN",request));
												if(pn.getContent().length()>20)
												{
													st = sign + pn.getContent().substring(0,20)+"...";
												}else
												{
													st = sign + pn.getContent();
												}
											}else{
												sign = String.format(DxzsStaticValue.getPERFECT_SIGN_TIMER(), pn.getArySendCount()).replace("完美通知", MessageUtils.extractMessage("dxzs","dxzs_PERFECT_SIGN",request)).replace("次", MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_115",request));
												if(pn.getContent().length()>20)
												{
													st = sign + pn.getContent().substring(0,20)+"...";
												}else
												{
													st = sign + pn.getContent();
												}
											}
										%>
										<a onclick=javascript:modifyNew(this)>
<%--										  <label style="display:none"><xmp><%//pn.getContent()%></xmp></label>--%>
										  <textarea class="dxzs_disNo"><%=pn.getContent()%></textarea>
	                                      <xmp name="msgXmp" style="display:none"><%=pn.getContent()%></xmp>
										  <xmp><%=st%></xmp>
										  </a> <%}else{ %>		<%} %>
										</td>
										<td>
											<%=pn.getNoticCount()%>
										</td>
										<td>
											<%=pn.getReceiveCount()%>
										</td>
										<td>
											<%=pn.getReplyCount() %>
										</td>

										<td>
											<a href="javascript:changeHisInfo('<%=pn.getTaskIdCipher()%>')"><emp:message key="dxzs_xtnrqf_button_13" defVal="详情" fileName="dxzs"/></a>
										</td>
									</tr>
						
								<%
										}
									}else{
									%>
										<tr><td align="center" colspan="7"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
									<%
									}	
								%>
								
						</tbody>
						<tfoot>
							<tr>
								<td colspan="7">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</form>
			</div>
			<%-- 内容结束 --%>
			<div id="detailDialog">
			</div>
			
			<div id="singledetail">
<%--				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"></div>--%>
					<span><textarea id="msgcont" rows="15" readonly="readonly"></textarea></span>
			</div>
			
			
			<div id="getUsers">
				<div id="users"></div>
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=iPath %>/js/queryNotHis.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script>
			$(document).ready(function(){
				synlen();
				getLoginInfo("#getloginUser");
				 var findresult="<%=request.getAttribute("findresult")%>";
				    if(findresult != null && findresult !="" && findresult=="-1")
				    {
				       alert(getJsLocaleMessage('zxkf','dxzs_ssend_alert_77'));	
				       return;			       
				    }
			
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
					
				<%--获取所有的发详情--%>
				$("#detailDialog").dialog({
					autoOpen: false,
					height:600,
					width: 850,
					modal: true,
					close:function(){
						//	$("select[name='sendType']").show();
					}
				});
					<%--获取所有的发送内容--%>
				$("#singledetail").dialog({
					autoOpen: false,
					height:300,
					width: 300,
					modal: true,
					close:function(){
						//	$("select[name='sendType']").show();
					}
				});
				
				<%--获取所有的发送对象信息--%>
				$("#getUsers").dialog({
					autoOpen: false,
					height:350,
					width: 450,
					modal: true
				});
				
				
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				deleteleftline1();
				$('#search').click(function(){submitForm();});
				
			});	
			
	</script>
	</body>
</html>
