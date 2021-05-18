<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.wyquery.vo.SystemMtTaskVo"%>
<%@page import="com.montnets.emp.wyquery.vo.SystemMtTask01_12Vo"%>
<%@page import="com.montnets.emp.wyquery.vo.SysMoMtSpgateVo"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,
		iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	boolean isFirstEnter = (Boolean)request.getAttribute("isFirstEnter");
	 CommonVariables  CV = new CommonVariables();
	String recordType = request.getParameter("recordType");
	String reportStatus=(String)request.getAttribute("reportStatus");
	String numberStyle=(String)request.getAttribute("numberStyle");
	String gateName=(String)request.getAttribute("gateName");
	@ SuppressWarnings("unchecked")
	List<DynaBean> mtTaskList=(List<DynaBean>)request.getAttribute("sysMtRealTimeTaskList");
	List<DynaBean> sysMtHisTaskList=(List<DynaBean>)request.getAttribute("sysMtHisTaskList");
	

	if(recordType == null)
	{
		recordType = "realTime";
	}
	String userid,phone,spgate,startTime,endTime,errorcode,message,buscode,spisuncm,usercode,staskid;
		if("history".equals(recordType))
	{
		SystemMtTask01_12Vo mtTaskVo = (SystemMtTask01_12Vo)request.getAttribute("sysMtTask");
		if(mtTaskVo==null)
		{
			mtTaskVo = new SystemMtTask01_12Vo();
		}
		userid=mtTaskVo.getUserid();
		phone=mtTaskVo.getPhone();
		spgate=mtTaskVo.getSpgate();
		startTime=mtTaskVo.getStartTime();
		endTime=mtTaskVo.getEndTime();
		errorcode=mtTaskVo.getErrorcode();
		staskid=mtTaskVo.getTaskid()!=null?mtTaskVo.getTaskid().toString():"";
		//message = mtTaskVo.getMessage();
		buscode = mtTaskVo.getSvrtype();
		if(mtTaskVo.getUnicom() !=null)
		{
		    spisuncm= mtTaskVo.getUnicom().toString();
		}
		else
		{
		   spisuncm = "";
		}
	}else
	{
		SystemMtTaskVo mtTaskVo = (SystemMtTaskVo)request.getAttribute("sysMtTask");
		
		if(mtTaskVo==null)
		{
			mtTaskVo = new SystemMtTaskVo();
		}
		userid=mtTaskVo.getUserid();
		phone=mtTaskVo.getPhone();
		spgate=mtTaskVo.getSpgate();
		startTime=mtTaskVo.getStartTime();
		endTime=mtTaskVo.getEndTime();
		errorcode=mtTaskVo.getErrorcode();
		staskid=mtTaskVo.getTaskid()!=null?mtTaskVo.getTaskid().toString():"";
		//message = mtTaskVo.getMessage();
		buscode = mtTaskVo.getSvrtype();
		if(mtTaskVo.getUnicom() !=null)
		{
		    spisuncm= mtTaskVo.getUnicom().toString();
		}
		else
		{
		   spisuncm = "";
		}
	}
	usercode = (String)request.getAttribute("usercode");
	@ SuppressWarnings("unchecked")
	List<String> userList = (List<String>)request.getAttribute("mrUserList");
	@ SuppressWarnings("unchecked")
	List<SysMoMtSpgateVo> spList = (List<SysMoMtSpgateVo>)request.getAttribute("spList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> busMap = (Map<String,String>)request.getAttribute("busMap");
	if(busMap==null) busMap = new HashMap<String,String>();
	String menuCode = titleMap.get("mtRecord");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String findResult= (String)request.getAttribute("findresult");
	//excel自动识别用
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>系统上行记录</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<style type="text/css">
			#condition table tr td select
			{
				width: 182px;
			}
		</style>
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container" class="container">
					<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
		<div id="rContent" class="rContent">
		<form name="pageForm" action="wyq_mtRecord.htm" method="post" id="pageForm">
		<div id="r_sysMoRparams" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key="common_export" defVal="导出" fileName="common"></emp:message></a>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
								 <td>
									<emp:message key="wygl_common_text38" defVal="记录类型 ：" fileName="wygl"></emp:message>
									</td>
									<td>
										<select name="recordType" id="recordType" style="">
										     <option value="realTime"><emp:message key="wygl_common_text39" defVal="实时记录" fileName="wygl"></emp:message></option>
										     <option value="history" 
										     	<%="history".equals(recordType)?"selected":"" %>><emp:message key="wygl_common_text40" defVal="历史记录" fileName="wygl"></emp:message></option>
										</select>
									</td>
									<td>
										<emp:message key="wygl_common_text41" defVal="接收号码 ：" fileName="wygl"></emp:message>
									</td>

									<td>
										<input type="text" style="width: 177px;" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="javascript:numberControl($(this))"/>
									</td>
									<td>
										<emp:message key="wygl_common_text42" defVal="状态报告：" fileName="wygl"></emp:message>
									</td>
									<td>
										<label>
											<select name="reportStatus" id="reportStatus" style="">
												<option value="">
													<emp:message key="common_whole" defVal="全部" fileName="common"></emp:message>
												</option>
												<option value="success" <%=("success").equals(reportStatus)?"selected":"" %>>
												
													<emp:message key="common_success" defVal="成功" fileName="common"></emp:message>
												</option>
												<option value="fail" <%=("fail").equals(reportStatus)?"selected":"" %>>
													<emp:message key="common_fail" defVal="失败" fileName="common"></emp:message>
												</option>
											</select>
										</label>
									</td>

									<td class="tdSer">
									     <center><a id="search"></a></center>
								    </td>
								</tr>

								<tr>
								<td>
										<emp:message key="wygl_common_text9" defVal="任务批次：" fileName="wygl"></emp:message>
									</td>
									<td><input type="text" style="width: 177px;" maxlength="18"  value='<%=staskid==null?"":staskid %>' id="taskid" name="taskid" onkeyup="javascript:numberControl($(this))"  onblur="javascript:numberControl($(this))"/>
									</td>
								<td>
										<emp:message key="wygl_common_text5" defVal="源SP账号：" fileName="wygl"></emp:message>
									</td>
									<td>
										<label>
											<select name="userid" id="userid" >
												<option value="">
													<emp:message key="common_whole" defVal="全部" fileName="common"></emp:message>
												</option>
											<%
											if (userList != null && userList.size() > 0)
											{
												for(String userdata : userList)
												{
													if(userdata!=null){
														String spuser=userdata==null?"":userdata;
											%>
													<option value="<%=spuser %>" 
														<%=spuser.equals(userid)?"selected":""%>>
														<%=spuser %>
													</option>
											<%
													}
												}
											}													
											%>
											</select>
										</label>
									</td>
									<td>
										<emp:message key="wygl_common_text43" defVal="通道号码：" fileName="wygl"></emp:message>
									</td>
									<td>
										<label>
											<select name="spgate" id="spgate">
												<option value="">
													<emp:message key="common_whole" defVal="全部" fileName="common"></emp:message>
												</option>
													<%
												if (spList != null && spList.size() > 0)
												{
													for(int i=0;i<spList.size();i++)
													{
														SysMoMtSpgateVo spgatevo=spList.get(i);
														if(spgatevo!=null){
															String ospgate=spgatevo.getSpgate()==null?"":spgatevo.getSpgate();
											%>
												<option value="<%=ospgate %>"
													<%=ospgate.equals(spgate)?"selected":""%>>
													<%=ospgate %>
												</option>
											<%		
														}
													}
												}
											%>
											</select>
										</label>
									</td>


									<td></td>
								</tr>
								<tr>
									<td>
										<emp:message key="wygl_common_text11" defVal="发送时间：" fileName="wygl"></emp:message>
									</td>
									<td class="tableTime">
										<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="sedtime()"
											value="<%=startTime == null ? "" : startTime%>"  id="sendtime"
											name="sendtime">
									</td>
									<td>
										<emp:message key="common_to" defVal="至：" fileName="common"></emp:message>
									</td>
									<td>
										<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="revtime()"
											value="<%=endTime == null ? "" : endTime%>" id="recvtime"
											name="recvtime">
											<input type="hidden" style="width: 177px;" value='<%=usercode==null?"":usercode %>' id="usercode" name="usercode" />
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
						            <th><emp:message key="wygl_common_text15" defVal="任务批次" fileName="wygl"></emp:message> </th>
						            <th><emp:message key="wygl_common_text21" defVal="信息内容 " fileName="wygl"></emp:message></th>
						            <th><emp:message key="wygl_common_text22" defVal="发送时间" fileName="wygl"></emp:message></th>
						            <th><emp:message key="wygl_common_text44" defVal="接收时间" fileName="wygl"></emp:message></th>
						            <th><emp:message key="wygl_common_text45" defVal="接收号码 " fileName="wygl"></emp:message></th>
						            <th><emp:message key="wygl_common_text46" defVal="状态报告" fileName="wygl"></emp:message></th>
						            <th><emp:message key="wygl_common_text18" defVal="源SP账号" fileName="wygl"></emp:message></th>
						            <th><emp:message key="wygl_common_text47" defVal="通道号码" fileName="wygl"></emp:message></th>
						            <th><emp:message key="wygl_common_text48" defVal="通道名称" fileName="wygl"></emp:message></th>
						        </tr>
							</thead>
							<tbody>
							<%if(isFirstEnter){ %>
							 	<tr><td colspan="12" align="center"><emp:message key="wygl_common_text29" defVal="请点击查询获取数据" fileName="wygl"></emp:message></td></tr>
							<%}else if("history".equals(recordType))
							{

								if(sysMtHisTaskList!=null && sysMtHisTaskList.size()>0)
								{
									for(int i=0;i<sysMtHisTaskList.size();i++)
									{
							%>
								<tr>
									<td>
									<%
									String taskid=String.valueOf(sysMtHisTaskList.get(i).get("taskid"));

									if(!"0".equals(taskid)){
										out.print(taskid);
									}else{
										out.print("--");
									}
									 %>
									</td>
									<td style="text-align:left;">
									<a onclick="javascript:modify(this)">
									<%
									String rmessage=String.valueOf(sysMtHisTaskList.get(i).get("message"));
									 %>
								  <label style="display:none"><xmp><%=rmessage %></xmp></label>
											<xmp><%=rmessage.length()>5?rmessage.substring(0,5)+"...":rmessage %></xmp>
								  </a> 
									</td>
									<td>
									<%
										String format="--";
										String sendtime=sysMtHisTaskList.get(i).get("sendtime")+"";
										if(sendtime.lastIndexOf(".")>0){
										sendtime=sendtime.substring(0,sendtime.lastIndexOf("."));
										}
										String recvTime=sysMtHisTaskList.get(i).get("recvtime")+"";
										if(recvTime.lastIndexOf(".")>0){
										recvTime=recvTime.substring(0,recvTime.lastIndexOf("."));
										}
									 %>
										<%=sendtime%>
									</td>
									<td><%=recvTime %></td>
									
																		<td>
									<%
									String phone1=String.valueOf(sysMtHisTaskList.get(i).get("phone"));
									   String phones = CV.replacePhoneNumber(btnMap,phone1);
									   out.print(phones);								   
									 %>
									</td>
									
									<td class="ztalign">
									<% 
									String rerrorcode=String.valueOf(sysMtHisTaskList.get(i).get("errorcode"));
									String userid1=String.valueOf(sysMtHisTaskList.get(i).get("userid"));
									String spgate1=String.valueOf(sysMtHisTaskList.get(i).get("spgate"));
									String cpno=String.valueOf(sysMtHisTaskList.get(i).get("cpno"));
									String gateName1=String.valueOf(sysMtHisTaskList.get(i).get("gatename"));
									if("".equals(rerrorcode.trim())){
										out.print("-");
									}else if("DELIVRD".equals(rerrorcode.trim())||"0".equals(rerrorcode.trim())){
									    out.print(MessageUtils.extractMessage("common", "common_success", request));
									}else {out.print(MessageUtils.extractMessage("common", "common_fail", request)+"["+rerrorcode.trim()+"]");}%>
									</td>
									<td>
											<%="null".equals(userid1)?"":userid1 %>
									</td>
									<td>
										<%="null".equals(spgate1)?"":spgate1%><%=cpno==null?"":cpno%>
									</td>
									<td><%="null".equals(gateName1)?"--":gateName1 %></td>
									  
								</tr>
								<%		}
									}else{%>
								<tr><td colspan="12" align="center"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%}}
							
							else if("realTime".equals(recordType))
							{

								if(mtTaskList!=null && mtTaskList.size()>0)
								{
									for(int i=0;i<mtTaskList.size();i++)
									{
										if(mtTaskList.get(i)==null){
										}
							%>
								<tr>
									<td>
									<%
									String taskid=String.valueOf(mtTaskList.get(i).get("taskid"));

									if(!"0".equals(taskid)){
										out.print(taskid);
									}else{
										out.print("--");
									}
									 %>
									</td>
									<td style="text-align:left;">
									<a onclick="javascript:modify(this)">
									<%
									String rmessage=String.valueOf(mtTaskList.get(i).get("message"));
									 %>
								  <label style="display:none"><xmp><%=rmessage %></xmp></label>
											<xmp><%=rmessage.length()>5?rmessage.substring(0,5)+"...":rmessage %></xmp>
								  </a> 
									</td>
									<td>
									<%
										String format="--";
										String sendtime=mtTaskList.get(i).get("sendtime")+"";
										if(sendtime.lastIndexOf(".")>0){
										sendtime=sendtime.substring(0,sendtime.lastIndexOf("."));
										}
										String recvTime=mtTaskList.get(i).get("recvtime")+"";
										if(recvTime.lastIndexOf(".")>0){
										recvTime=recvTime.substring(0,recvTime.lastIndexOf("."));
										}
									 %>
										<%=sendtime%>
									</td>
									<td><%=recvTime %></td>
									
																		<td>
									<%
									String phone1=String.valueOf(mtTaskList.get(i).get("phone"));
									   String phones = CV.replacePhoneNumber(btnMap,phone1);
									   out.print(phones);								   
									 %>
									</td>
									
									<td class="ztalign">
									<% 
									String rerrorcode=String.valueOf(mtTaskList.get(i).get("errorcode"));
									String userid1=String.valueOf(mtTaskList.get(i).get("userid"));
									String spgate1=String.valueOf(mtTaskList.get(i).get("spgate"));
									String cpno=String.valueOf(mtTaskList.get(i).get("cpno"));
									String gateName1=String.valueOf(mtTaskList.get(i).get("gatename"));
									if("".equals(rerrorcode.trim())){
										out.print("-");
									}else if("DELIVRD".equals(rerrorcode.trim())||"0".equals(rerrorcode.trim())){
									    out.print(MessageUtils.extractMessage("common", "common_success", request));
									}else {out.print(MessageUtils.extractMessage("common", "common_fail", request)+"["+rerrorcode.trim()+"]");}%>
									</td>
									<td>
											<%="null".equals(userid1)?"":userid1 %>
									</td>
									<td>
										<%="null".equals(spgate1)?"":spgate1%><%=cpno==null?"":cpno%>
									</td>
									<td><%="null".equals(gateName1)?"--":gateName1 %></td>
									  
								</tr>
								<%		}
									}else{%>
								<tr><td colspan="12" align="center"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%}} %>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="12">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>	
						<div id="r_sysMrparams" class="hidden"></div>			
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
		<%-- foot结束 --%>
			 
		<div id="modify" title="<emp:message key="wygl_common_text21" defVal="信息内容" fileName="wygl"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<label id="msg" style="width:100%;height:100%"></label>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/wygl_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/wyq_exportexcel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript">
			$(document).ready(function(){
			    getLoginInfo("#r_sysMrparams");
				var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       alert(getJsLocaleMessage('wygl', 'wygl_common_text1'));	
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
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				//导出全部数据到excel
				$("#exportCondition").click(
				 function(){
					  if(confirm(getJsLocaleMessage('wygl', 'wygl_common_text2')))
					   {
					   		var langName ='<%=langName %>';	
					   		var recordType = $("#recordType").val();
					   		var recvNumber = $("#recvNumber").val();
					   		var spgate= $("#spgate").val();
					   		var phone = $("#phone").val();
					   		var sendtime = $("#sendtime").val();
					   		var recvtime = $("#recvtime").val();
					   		var userid = $("#userid").val();
					   		var lgcorpcode =<%=request.getParameter("lgcorpcode")%>;
				            var lguserid =<%=request.getParameter("lguserid")%>;
				            var reportStatus = $("#reportStatus").val();
				            var taskid = $("#taskid").val();
				            var gateName=$("#gateName").val();
				            var numberStyle=$("#numberStyle").val();
					   		<%if (mtTaskList!=null&&mtTaskList.size()>0||sysMtHisTaskList!=null&&sysMtHisTaskList.size()>0)  {
					   		if(pageInfo.getTotalRec()<=500000){
					   		%>			
					   			$.ajax({
									type: "POST",
									url: "<%=path%>/wyq_mtRecord.htm?method=exportExcel",
									data: {
										langName:langName,
										recvNumber:recvNumber,
										recordType:recordType,
										gateName:gateName,
										numberStyle:numberStyle,
										spgate:spgate,
										phone:phone,
										sendtime:sendtime,
										recvtime:recvtime,
										userid:userid,
										lgcorpcode:lgcorpcode,
										lguserid:lguserid,
										reportStatus:reportStatus,
										taskid:taskid
									},
						            beforeSend:function () {
						                page_loading();
						            },
						            complete:function () {
						           	  	page_complete();
						            },
						            success:function (result) {
						                    if (result == 'true') {
						                        download_href("<%=path%>/wyq_mtRecord.htm?method=downloadFile");
						                    } else {
						                        alert(getJsLocaleMessage('wygl', 'wygl_common_text5'));
						                    }
			           				}
				 				});
					   			//location.href="<%=path%>/wyq_mtRecord.htm?method=exportExcel&recvNumber="+recvNumber+"&recordType="+recordType+"&gateName="+gateName+"&numberStyle="+numberStyle+"&spgate="+spgate+"&phone="+phone+"&sendtime="+sendtime+"&recvtime="+recvtime+"&userid="+userid+"&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&reportStatus="+reportStatus+"&taskid="+taskid;
					   		<%}else{%>
					   		alert(getJsLocaleMessage('wygl', 'wygl_common_text3'));
			   				<%}
					   		} else {%>
					   		alert(getJsLocaleMessage('wygl', 'wygl_common_text4'));
					   		<%}
					   		%>
					   	}
				  });
			$('#search').click(function(){submitForm();});
			$("#userid,#spgate").isSearchSelect({'width':'180','isInput':true,'zindex':0});
			$("#reportStatus,#recordType").isSearchSelect({'width':'180','isInput':false,'zindex':0});
		});
		
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/wyq_mtRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
