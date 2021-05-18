<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ page import="com.montnets.emp.rms.vo.LfTempImportBatchVo" %>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	///emp_sta/rms/meditor
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map


		
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	
	String titlePath = (String)request.getAttribute("titlePath");
	String menuCode = titleMap.get("/importTemplateDetails");
	menuCode=menuCode==null?"0-0-0":menuCode;
	
	String actionPath = (String)request.getAttribute("actionPath");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

	String findResult= (String)request.getAttribute("findresult");
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String httpUrl = StaticValue.getFileServerViewurl();
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");

	@ SuppressWarnings("unchecked")
	List<LfTempImportBatchVo> tempBatchs =(List<LfTempImportBatchVo>) request.getAttribute("tempBatchs");
	
	//任务批次
	String batch= request.getParameter("batch");
	//企业编码
	String corpCode= request.getParameter("corpCode");
	//企业名称
	String corpName= request.getParameter("corpName");
	//状态
	//String processStatus=request.getParameter("processStatus");
	//开始时间
	String addtimeStart = request.getParameter("addtimeStart");
	//结束时间
	String addtimeEnd = request.getParameter("addtimeEnd");
	
	//处理查询条件的值
	if(batch==null||"".equals(batch.trim())){
		batch="";
	}else{
		try{
			Long.parseLong(batch.trim());
			batch=batch.trim();
		}catch(Exception e){
			EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + batch);
			batch="0";
		}
	}
	
	if(corpCode==null||"".equals(corpCode.trim()))
	{
		corpCode="";
	}
	
	if(corpName==null || "".equals(corpName))
	{
		corpName="";
	}
	//
	//if(processStatus==null || "".equals(processStatus))
	//{
		//processStatus="";
	//}
	
	if(addtimeStart==null || "".equals(addtimeStart))
	{
		addtimeStart="";
	}
	
	if(addtimeEnd==null || "".equals(addtimeEnd))
	{
		addtimeEnd="";
	}	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>importTemplateDetails.html</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<style type="text/css">
			.smt_smsTaskRecord div#tooltip { 
				position:absolute;
				z-index:1000;
				max-width:435px; 
				_width:expression(this.scrollWidth > 435 ? "435px" : "auto");  
				width:auto; background:#A8CFF6; 
				border:#FEFFD4 solid 1px; 
				text-align:left; padding:6px;
			}
		</style>
	</head>

	<body id="smt_smsTaskRecord">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="path" id="path" value="<%=path %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="htmName" id="htmName" value="<%=actionPath %>">
			<%-- 短信账号--%>
			<input type="hidden" name="smsAccount" id="smsAccount" value="<%=StaticValue.SMSACCOUNT %>">
			<form name="pageForm" action="importTemplateDetails.htm" method="post" id="pageForm">
				<div class="buttons">
					<div id="toggleDiv"></div>
					<a id="xzExportCondition" onclick="xzExportCondition()"><emp:message key="rms_btn_downloadTemplate" defVal="下载模板" fileName="rms"/></a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a id="plExportCondition" onclick="plExportCondition()"><emp:message key="rms_btn_importTemplate" defVal="导入模板" fileName="rms"/></a>
				</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
								  <td><emp:message key="dxzs_xtnrqf_title_12bak" defVal="任务批次" fileName="dxzs"/>：</td>
								   <td><input type="text" id="batch" name ="batch"  value="<%=batch== null?"":batch %>"></td>
								   <% if (StaticValue.getCORPTYPE() == 1) {%>
								   <td><emp:message key="dxzs_xtnrqf_title_12bak" defVal="企业编码" fileName="dxzs"/>：</td>
								   <td><input type="text" id="corpCode" name ="corpCode"  value="<%=corpCode== null?"":corpCode %>"></td>

								   
								   <td><emp:message key="dxzs_xtnrqf_title_12bak" defVal="企业名称" fileName="dxzs"/>：</td>
								   <td><input type="text" id="corpName" name ="corpName"  value="<%=corpName== null?"":corpName %>"></td>
									<% }else{%>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<%}%>
								   <td class="tdSer">
										<center><a id="search"></a></center>
									</td>	
								    
								</tr>
								
								<tr>
									<%-- 
									<td><emp:message key="dxzs_xtnrqf_title_187" defVal="发送状态" fileName="dxzs"/>：</td>
								    <td>
								    	<select name="processStatus" id="processStatus" isInput="false">
								    		<option value="-1"><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
								    		<option value="1" <%="1".equals(processStatus)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_189" defVal="处理完成" fileName="dxzs"/></option>
								    		<option value="0" <%="0".equals(processStatus)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_185" defVal="处理中" fileName="dxzs"/></option>
								    	</select>
								   </td>
								    --%>
									<td>
								        <emp:message key="dxzs_xtnrqf_title_25" defVal="导入时间" fileName="dxzs"/>：
								   </td>
								   <td>
								    	<input type="text"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= addtimeStart== null?"":addtimeStart %>"  id="addtimeStart" name="addtimeStart"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>：</td>
								   <td>
								      	<input type="text"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=addtimeEnd==null?"":addtimeEnd %>" 
											 id="addtimeEnd" name="addtimeEnd" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
								     <td>&nbsp;</td>
								     <td>&nbsp;</td>
								     <td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="dxzs_xtnrqf_title_146bak" defVal="序号" fileName="dxzs"/>
								</th>
								<% if (StaticValue.getCORPTYPE() == 1) {%>
								<th>
									<emp:message key="dxzs_xtnrqf_title_143bak" defVal="企业编码" fileName="dxzs"/>
								</th>
								<th class="dxzs_th">
									<emp:message key="dxzs_xtnrqf_title_99bak" defVal="企业名称" fileName="dxzs"/>
								</th>
								<% }%>
								<th class="dxzs_th">
									<emp:message key="dxzs_xtnrqf_title_158bak" defVal="任务批次" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_2bak" defVal="总数据数" fileName="dxzs"/>
								</th>
								<th class="dxzs_th1">
									<emp:message key="dxzs_xtnrqf_title_25bak" defVal="成功数" fileName="dxzs"/>
								</th>
								<th class="dxzs_th2">
								    <emp:message key="dxzs_xtnrqf_title_187bak" defVal="失败数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_191bak" defVal="处理状态" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_192bak" defVal="导入时间" fileName="dxzs"/>
								</th>
								<th>
								  	<emp:message key="dxzs_xtnrqf_title_6bak" defVal="操作" fileName="dxzs"/>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(tempBatchs != null && tempBatchs.size()>0) {
								int i = 0;
								for(LfTempImportBatchVo tempBatch : tempBatchs) {
						%>
						<tr align="center">
							<td>
								<%=++i%>
							</td>
							<% if (StaticValue.getCORPTYPE() == 1) {%>
							<td >
								<%="".equals(tempBatch.getCorpCode()) ?"-":tempBatch.getCorpCode()%>
							</td>
							<td >
								<%="".equals(tempBatch.getCorpName()) ?"-":tempBatch.getCorpName()%>
							</td>
							<% }%>
							<td>
								<%=tempBatch.getBatch()==null? 0L :tempBatch.getBatch()%>
							</td>
							<td>
								<%=tempBatch.getSuccessAmount()==null?0L:tempBatch.getAmount()%>
							</td>
							<td>
								<%=tempBatch.getSuccessAmount()==null?0L:tempBatch.getSuccessAmount()%>
							</td>
							<td>
								<%=tempBatch.getFailAmount()==null?0L:tempBatch.getFailAmount()%>
							</td>
							<td>
								<%
									if(tempBatch.getProcessStatus()!=null&&tempBatch.getProcessStatus()==1){
								%>
									处理完成
								<%
									}else{
								%>
									处理中
								<%
									}
								 %>
							</td>
							<td>
								<%=tempBatch.getAddtime()==null?"-":df.format(tempBatch.getAddtime()) %><%--导入时间 --%>
							</td>
							<td>
							   <a onclick="location.href='<%=path%>/importTemplateDetails.htm?method=findAllSendInfo&batch=<%=tempBatch.getBatch()%>&corp=<%=tempBatch.getCorpCode()%>'"><emp:message key="dxzs_xtnrqf_title_106bak" defVal="详情查看" fileName="dxzs"/></a>
							  &nbsp;&nbsp;
                                <%
                                    if(tempBatch.getSendState() == 0 && tempBatch.getSuccessAmount() > 0){
                                %>
                                <a onclick="toSendMarathon('<%=tempBatch.getBatch()%>','<%=tempBatch.getCorpCode()%>')"><emp:message key="dxzs_xtnrqf_title_106bak" defVal="立即发送" fileName="dxzs"/></a>
                                <%}else if(tempBatch.getSendState() != null && tempBatch.getSendState() != 0){%>
									<emp:message key="dxzs_xtnrqf_title_153" defVal="已发送" fileName="dxzs"/>
								<%}%>
							</td>
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="10"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td>

						</tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="19">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
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
		</div>
    <div class="clear"></div>
	<%--导入模板--%>
	<div id="tempDiv" class="dxzs_tempDiv" title="<emp:message key='dxzs_xtnrqf_title_64bak' defVal='模板导入' fileName='dxzs'/>" style="display: none;">
		<iframe id="tempFrame" name="tempFrame" width="100%" height="360px" class="dxzs_tempFrame" marginwidth="0" scrolling="no" frameborder="no" src ="importTemplate.htm?method=importTemp"></iframe>
		<div class="dxzs_div7"  align="center" id="tempButton">
			<input id="subSend" class="btnClass5 mr23" type="button" onclick="tempSure()" value="<emp:message key='common_confirm' defVal='提交' fileName='common'/>">
			&nbsp;&nbsp;&nbsp;
			<input class="btnClass6" type="button" onclick="tempCancel();" value="<emp:message key='common_cancel' defVal='取消' fileName='common'/>">
			<br/>
		</div>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/layer/layer.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/rms/meditor/js/templateDetails.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function(){
		    // getLoginInfo("#smssendparams");
		    //参数是要隐藏的下拉框的div的id数组，
		    
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_77'));	
		       return;			       
		    }
			
			$("#title").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
			});
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
	
		});
		
	</script>
	</body>
</html>
