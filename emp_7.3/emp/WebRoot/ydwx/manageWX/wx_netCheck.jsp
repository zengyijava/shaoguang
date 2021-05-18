<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.approveflow.LfFlowRecord"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="java.sql.Timestamp" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfFlowRecord> recordList = (List<LfFlowRecord>)request.getAttribute("recordList");
	String tmName = request.getParameter("tmName");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> usernameMap = (LinkedHashMap<Long,String>) request.getAttribute("usernameMap");
	String conrstate = (String) request.getAttribute("conrstate");
	String skip=(String)request.getAttribute("skip");
	if("true".equals(skip)){
		conrstate=(String)request.getAttribute("conrstate");
		tmName=(String)request.getAttribute("tmName");
	}
	
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String langName = (String)session.getAttribute("emp_lang");
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/netCheck.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="ydwx_netCheck">
	<input type="hidden" id="ipathUrl" value="<%=commonPath %>"/>
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="<%=path %>/wx_check.htm?method=find" method="post"
					id="pageForm">
					<div id="ir_smsTRparams" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
					</div>
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="ydwx_wxgl_wxshh_mingchens" defVal="网讯名称：" fileName="ydwx"></emp:message></span>
								</td>
								<td>
									<input type="text" name="tmName" 
										id="tmName" value="<%=null!=tmName?tmName:"" %>" class="ydwx_tmName" />
								</td>
								<td><emp:message key="ydwx_wxgl_wxshh_zhuangtais" defVal="审批状态：" fileName="ydwx"></emp:message></td>
								<td>
								<%--	//审批状态（-1代表未审核；0代表无需审核；1代表审核通过；2代表审核不通过） --%>
										<select id="conrstate" name="conrstate" class="ydwx_conrstate" isInput="false">
											<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
											<option value="-1"  <%="-1".equals(conrstate)?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_weishp" defVal="未审批" fileName="ydwx"></emp:message></option>
											<option value="1"  <%="1".equals(conrstate)?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_shptongguo" defVal="审批通过" fileName="ydwx"></emp:message></option>
											<option value="2"  <%="2".equals(conrstate)?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_shpbutongguo" defVal="审批不通过" fileName="ydwx"></emp:message></option>
											<option value="3"  <%="3".equals(conrstate)?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_shpwancheng" defVal="审批完成" fileName="ydwx"></emp:message></option>
										</select>
								</td>
								<td class="tdSer">  <center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
										
										<th>
											<emp:message key="ydwx_wxgl_wxshh_bianhao" defVal="网讯编号" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_wxgl_wxshh_mingchen" defVal="网讯名称" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_wxgl_cjr" defVal="创建人" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_common_time_chuangjianshijian" defVal="创建时间" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_wxgl_wxshh_jibei" defVal="审批级别" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_wxgl_wxshh_zhuangtai" defVal="审批状态" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_wxgl_wxshh_shenpi" defVal="审批" fileName="ydwx"></emp:message>
										</th>
									</tr>
								</thead>
								<tbody>
								<%
								if (recordList != null && recordList.size() > 0)
								{
									for (LfFlowRecord record : recordList)
									{
								%>
									<tr>
										
										<td>
											<%=record.getMtId() %>
										</td>
										<td class="textalign">
											<%=record.getRContent().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
										</td>
										<td>
											<%=usernameMap.get(record.getProUserCode()) %>
										</td>
										<td>
											<%=record.getSubmitTime()==null?"":df.format(record.getSubmitTime()) %>
										</td>
										<td><%=record.getRLevel() %>/<%=record.getRLevelAmount() %></td>
										<td>
										<%
										//审批状态（-1代表未审核；0代表无需审核；1代表审核通过；2代表审核不通过）
								    		int state=record.getRState();
									    	if(state == -1){
									    		if(record.getIsComplete() == 1){
									    			out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_15",request));
									    		}else{
									    			out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_16",request));	
									    		}
									    	}else if(state==1)
									    	{
									    		out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_17",request));	
									    	}else if(state==2)
									    	{
												out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_18",request));				    		
									    	}else if(state==0){
									    		out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_19",request));		
									    	}
								    %> 
										</td>
										<td>
											<a onclick="javascript:examineMsg('<%=record.getFrId()%>')">
												<img alt="<emp:message key="ydwx_wxgl_wxshh_shenpi" defVal="审批" fileName="ydwx"></emp:message>" src="<%=commonPath%>/common/img/find.png">
													<%=record.getRState()==-1 && record.getIsComplete() == 2?MessageUtils.extractMessage("ydwx","ydwx_jsp_out_20",request):MessageUtils.extractMessage("ydwx","ydwx_jsp_out_21",request) %>
											</a>
										</td>
									</tr>
									<%
									}
								}else{
									%>
									<tr><td colspan="9"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</form>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
	<div class="clear"></div>
<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
	$(document).ready(function() {
	    getLoginInfo("#ir_smsTRparams");
		var findresult="<%=findResult%>";
	    if(findresult=="-1")
	    {
	       alert(getJsLocaleMessage("ydwx","ydwx_common_jzymshbqjchwl"));	
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
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,
			<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		$('#search').click(function(){submitForm();});
			
		
	});
		function examineMsg(frid){
			
			var pathUrl = $("#ipathUrl").val();
			var lguserid = $("#lguserid").val();
			//短信审批
			window.location.href = pathUrl+"/wx_check.htm?method=getExamineInfo&&frid="+frid;
		}
	$(function(){
	  $('#conrstate').isSearchSelect({'width':'180','isInput':false,'zindex':0});
    });
</script>
</body>
</html>
