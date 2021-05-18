<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.entity.approveflow.LfFlowRecord"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.entity.system.LfPageField"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfFlowRecord> recordList = (List<LfFlowRecord>)request.getAttribute("recordList");
	@ SuppressWarnings("unchecked")
	List<LfSysuser> userList = (List<LfSysuser>)request.getAttribute("userList");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> usernameMap = (LinkedHashMap<Long,String>)request.getAttribute("usernameMap");
	LfSysuser lfsysuser = (LfSysuser) request.getAttribute("lfsysuser");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap<String,String>) request.getAttribute("conditionMap");
	//HashSet<Long> mtidSet = (HashSet<Long>) request.getAttribute("mtidSet");
	String conreviewtype = (String) request.getAttribute("conreviewtype");
	String conrstate = (String) request.getAttribute("conrstate");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)request.getAttribute("pagefileds");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=iPath %>/css/detail.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/msg_examineMsg.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		<script type="text/javascript">
			
		</script>
	</head>

	<body id="msg_examineMsg">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(langName,ViewParams.MSRECODE) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="<%=path %>/msg_smsInfoReview.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
					<input type="hidden" id="pathUrl" value="<%=path %>"/>
					<div id="hiddenValueDiv" class="hidden"></div>
					<div id="condition">
						<table>
								<tbody>
								<tr>
								<td>
										<emp:message key="xtgl_spgl_xxsp_zt_mh" defVal="主题： " fileName="xtgl"/>
									</td>
									<td>
										<input type="text" value="<%=null==conditionMap.get("RContent&like")?"":conditionMap.get("RContent&like") %>" id="conrcontent" name="conrcontent" class="conrcontent">
									</td>
									<td>
										<emp:message key="xtgl_spgl_xxsp_fsr_mh" defVal="发送人： " fileName="xtgl"/>
									</td>
									<td>						
										<select id="conproname" name="conproname" class="conproname">
											<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部 " fileName="xtgl"/></option>
											<%
												if(userList != null){
												   for(int i=0;i<userList.size();i++){
													 LfSysuser user = userList.get(i);   
											 %>
													<option value="<%=user.getUserId()%>" <%=conditionMap.get("ProUserCode")!=null && String.valueOf(user.getUserId()).equals(conditionMap.get("ProUserCode"))?"selected":"" %>>
													<%=user.getName().replaceAll("<","&lt;").replaceAll(">","&gt;")%>(<%=user.getUserName()%>)
													</option>
											<%
													} 
												}
											%>
										</select>
									</td>
									<td>
										<emp:message key="xtgl_spgl_xxsp_spzt_mh" defVal="审批状态： " fileName="xtgl"/>
									</td>
									<td>
									<%--	//审批状态（-1代表未审核；0代表无需审核；1代表审核通过；2代表审核不通过） --%>
										<select id="conrstate" name="conrstate" class="conrstate">
											<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部 " fileName="xtgl"/></option>
											<option value="-1"  <%="-1".equals(conrstate)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_wsp" defVal="未审批 " fileName="xtgl"/></option>
											<option value="1"  <%="1".equals(conrstate)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_sptg" defVal="审批通过 " fileName="xtgl"/></option>
											<option value="2"  <%="2".equals(conrstate)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_spbtg" defVal="审批不通过 " fileName="xtgl"/></option>
											<option value="3"  <%="3".equals(conrstate)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_spwc" defVal="审批完成 " fileName="xtgl"/></option>
											<option value="4"  <%="4".equals(conrstate)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_ycx" defVal="已撤销 " fileName="xtgl"/></option>
										</select>
									</td>
									<td class="tdSer">
									<center><a id="search"></a></center>
									</td>
								</tr>
								<tr>								
									<td>
										<emp:message key="xtgl_spgl_xxsp_tjsj_mh" defVal="提交时间：" fileName="xtgl"/>
									</td>
									<td>
										<input type="text" value="<%=null==conditionMap.get("submitTime&>")?"":conditionMap.get("submitTime&>") %>" id="startSubmitTime" name="startSubmitTime"  class="Wdate startSubmitTime" readonly="readonly" onclick="stime()">
									</td>
									<td align="left">
										<emp:message key="xtgl_spgl_xxsp_z_mh" defVal="至：" fileName="xtgl"/>
									</td>
									<td>
										<input type="text" value="<%=null==conditionMap.get("submitTime&<")?"":conditionMap.get("submitTime&<") %>" id="endSubmitTime" name="endSubmitTime"  class="Wdate endSubmitTime" readonly="readonly" onclick="rtime()">
									</td>
									<td><emp:message key="xtgl_spgl_xxsp_xxlx" defVal="信息类型" fileName="xtgl"/></td>
									<td>
										<select id="conreviewtype" name="conreviewtype" class="conreviewtype">
											<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部 " fileName="xtgl"/></option>
											<option value="1" <%="1".equals(conreviewtype)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_dx" defVal="短信" fileName="xtgl"/></option>
											<option value="2" <%="2".equals(conreviewtype)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_cx" defVal="彩信" fileName="xtgl"/></option>
											 
											
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
									<emp:message key="xtgl_spgl_xxsp_zt" defVal="主题" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_spgl_xxsp_lx" defVal="类型" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_spgl_xxsp_fsr" defVal="发送人" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_spgl_xxsp_tjsj" defVal="提交时间" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_spgl_xxsp_spjb" defVal="审批级别" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_spgl_xxsp_spr" defVal="审批人" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_spgl_xxsp_spzt" defVal="审批状态" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
								</th>
							</tr>
						</thead>
						<tbody>
						<%
								//审核流程记录list
								if (recordList != null && recordList.size() > 0)
								{
									for (LfFlowRecord record : recordList)
									{
								%>	
								<tr>
								
								<td class="textalign">
									<%=record.getRContent()==null?"":(record.getRContent().replaceAll("<","&lt;").replaceAll(">","&gt;")) %>
								</td>
								<td>
									<%
										if(record.getInfoType()-2==0){
											out.print("<font color='#f1913c'>"+MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_cx",request)+"</font>");
										}
										else if(record.getInfoType()-1==0||record.getInfoType()-5==0){
											out.print("<font color='#159800'>"+MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_dx",request)+"</font>");
										}
									%>
								</td>
								<td class="textalign">
									<%if(usernameMap !=null && usernameMap.size()>0 && usernameMap.get(record.getProUserCode()) != null){
										 String str = usernameMap.get(record.getProUserCode());
										%>
											<%=str %>
										 <%
								    	}else {
								    		out.print("-");
								    	}
								 	 %>
								</td>
								<td>
									<%=record.getSubmitTime()==null?"":df.format(record.getSubmitTime()) %>
								</td>
								<td>
									<%--//RLevel 当前审批级别 --%>
									<%=record.getRLevel() %>/<%=record.getRLevelAmount() %>
								</td>
								<td class="textalign">
									<xmp>
										<%=lfsysuser.getName() %>
									</xmp>
								</td>
								<td class="ztalign">
								<%
										//审批状态（-1代表未审核；0代表无需审核；1代表审核通过；2代表审核不通过）
								    		int state=record.getRState();
									    	if(state == -1){
									    		if(record.getIsComplete() == 1){
									    			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_spwc",request));
									    		}else{
									    			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_wsp",request));	
									    		}
									    	}else if(state==1)
									    	{
									    		out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_sptg",request));	
									    	}else if(state==2)
									    	{
												out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_spbtg",request));				    		
									    	}else if(state==0){
									    		out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_wxsp",request));		
									    	}else if(state==4){
									    		out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_ycx",request));		
									    	}
							    %> 
								</td>
								<td>
								    <% 
									    if(record.getRState() == 4){
											out.print("-");
										}else{
								    %>
										<a onclick="javascript:examineMsg('<%=record.getMtId()%>','<%=record.getRLevel()%>','<%=record.getInfoType()%>','<%=record.getFId()%>','<%=record.getFrId()%>')">
											
												<%=record.getIsComplete() == 2?MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_sp",request):MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_ck",request) %>
										</a>
								   <% 
									}
							    	%>
								</td>
								</tr>
								<%}
								}
								else
								{
									%>
									<tr><td colspan="8"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
								<%
								}
								%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="8">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
				</form>
			</div>
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
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=iPath%>/js/examine.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script>
		$(document).ready(function() {
	   		 getLoginInfo("#hiddenValueDiv");
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
			$('#search').click(function(){submitForm();});
		});
	</script>
	</body>
</html>
