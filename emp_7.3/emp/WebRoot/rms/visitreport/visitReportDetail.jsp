<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@page import="com.montnets.emp.shortUrl.vo.LfVisitDetailVo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String titlePath = (String)request.getAttribute("titlePath");
	String menuCode = titleMap.get(titlePath);
	menuCode = menuCode==null?"0-0-0":menuCode;
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String taskid = (String)request.getParameter("taskid");
	LfVisitDetailVo taskVo = (LfVisitDetailVo)request.getAttribute("taskVo");
    @ SuppressWarnings("unchecked")
    List<LfVisitDetailVo> taskList = (List<LfVisitDetailVo>)request.getAttribute("taskList");   
    String actionPath = (String)request.getAttribute("actionPath");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>sendSMS.html</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%-- 表示请求的名称 --%>
			<form name="pageForm" action="fx_urlvisit.htm?method=getDetail" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						 <span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="rms_fxapp_fxsend_back" defVal="返回" fileName="rms"/></span>
                            <a id="exportCondition"><emp:message key="rms_fxapp_degreerep_export" defVal="导出" fileName="rms"/></a>
					</div>
					<input type="hidden" value="<%=taskVo.getSrcUrl()==null?"":taskVo.getSrcUrl()%>" name="srcurl" id="srcurl"/>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									
									<td>
										<emp:message key="rms_fxapp_fsmx_sjhm" defVal="手机号码：" fileName="rms"/>
									</td>
									<td >	
											
										<input type="text" style="width: 180px"  id="phone" name="phone" value="<%=taskVo.getPhone()==null?"":taskVo.getPhone()%>"/>
									</td>
									<td><emp:message key="rms_taskrecord_fwzt" defVal="访问状态：" fileName="rms"/></td>
									  
									<td>	
  		                                  <input type="text" id="title" name ="title" style="width: 180px"  value="<%=taskVo.getStatus()==null?"":taskVo.getStatus()%>" maxlength="19">	
									</td>
									<td><emp:message key="rms_taskrecord_fwip" defVal="访问IP：" fileName="rms"/></td>
									  
									<td>	
  		                                  <input type="text" id="taskid" name ="taskid" style="width: 180px" onkeyup="javascript:numberControl($(this))" value="<%=taskVo.getTaskid()==null?"":taskVo.getTaskid()%>" maxlength="19">	
									</td>	
									<td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>		
								</tr>
								<tr>
								
								    <td>
								       <emp:message key="rms_fxapp_fxsend_sendtime" defVal="发送时间：" fileName="rms"/>
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="sedtime()" 
											value="<%= taskVo.getStartTime()== null?"":taskVo.getStartTime() %>"  id="sendtime" name="sendTime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td>至：</td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="revtime()"
											value="<%=taskVo.getEndTime()==null?"":taskVo.getEndTime() %>" 
											 id="recvtime" name="endTime" <%--onchange="onblourSendTime('end')" --%>>
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
									<emp:message key="rms_fxapp_fxsend_telphone" defVal="手机号码" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_district" defVal="地区" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_fwzt2" defVal="访问状态" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_fwsj" defVal="访问时间" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_fwip2" defVal="访问IP" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_rwpc2" defVal="任务批次" fileName="rms"/>
								</th>	
								<th>
									<emp:message key="rms_taskrecord_sendtopic" defVal="发送主题" fileName="rms"/>
								</th>	
								<th>
									<emp:message key="rms_fxapp_fsmx_fssj" defVal="发送时间" fileName="rms"/>
								</th>																
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="13" align="center"><emp:message key="rms_fxapp_fsmx_clickquery" defVal="请点击查询获取数据" fileName="rms"/></td></tr>
							<%
							} else if(taskList != null && taskList.size()>0)
							{
								for(LfVisitDetailVo task : taskList)
								{
									
									%>
										<tr>
											<td> 
												<%=task.getPhone()==null?"-":task.getPhone() %>
											</td>
											<td class="textalign">
												<xmp><%=task.getArea()==null?"-":task.getArea() %></xmp>
											</td>
											<td>
											<%=task.getStatus()==null?"-":task.getStatus() %>
											</td>
											<td >
											<%=task.getLastvisittm()==null?"-":task.getLastvisittm() %>
											</td>
											<td >
											<%=task.getIp()==null?"-":task.getIp() %>
											</td>
											
											<td   class="textalign">
												<%=task.getTaskid()==null?"-":task.getTaskid() %>
											</td>
																					
											<td>
													<%=task.getTitle()==null?"-":task.getTitle() %>
											</td>
											<td>
													<%=task.getSubmittime()==null?"-":task.getSubmittime() %>
											</td>
											
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="8"><emp:message key="rms_fxapp_fsmx_norecord" defVal="无记录" fileName="rms"/></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="8">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
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
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript"
			src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?v=6.0"></script>
 		<script type="text/javascript" src="<%=iPath %>/js/exportexcel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		var path="<%=path%>";
			$(document).ready(function() {
			    //getLoginInfo("#smssendparams");
			    var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       alert(getJsLocaleMessage("rms","rms_repdegree_alert1"));	
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
				$('#search').click(function(){submitForm();});
				//导出全部数据到excel
				$("#exportCondition").click(
				    function()
				    {
					
						if(confirm(getJsLocaleMessage("rms","rms_visireport_confirmexp")))
						{
							var userIds = '';
							var taskid = '<%=taskVo.getTaskid()!=null?taskVo.getTaskid():""%>';
					   		var title = '<%=taskVo.getTitle()!=null?taskVo.getTitle():""%>';
					   	    var sendTime = '<%=taskVo.getStartTime()!=null?taskVo.getStartTime():""%>';	
					   	    var endTime = '<%=taskVo.getEndTime()!=null?taskVo.getEndTime():""%>';	
					   	    var userNames = '';
					   	    var lgcorpcode =$("#lgcorpcode").val();
					        var lguserid =$("#lguserid").val();	
					        var srcurl =$("#srcurl").val();		   	    
					   	    
					   		<%
					   		if(taskList!=null && taskList.size()>0 && pageInfo.getTotalRec()<=500000){
					   		%>	
					   				$.ajax({
									type: "POST",
									url: "<%=path%>/fx_urlvisit.htm?method=r_VisitDetailExportExcel",
									data: {userid:userIds,
											taskid:taskid,
											title:title,
											sendTime:sendTime,
											lguserid:lguserid,
											endTime:endTime,
											srcurl:srcurl
											},
					                beforeSend:function () {
										page_loading();
					                },
					                complete:function () {
								    	page_complete();
					                },
									success: function(result){
					                        if(result=='true'){
					                           download_href("<%=path%>/fx_urlvisit.htm?method=downloadDetailFile");
					                        }else{
					                            alert(getJsLocaleMessage("rms","rms_visireport_failexp"));
					                        }
						   			}
								});		
					   		<%	
					   		}
					   		else if(taskList!=null && pageInfo.getTotalRec()>500000)
					   		{
					   		%>
					   		   alert(getJsLocaleMessage("rms","rms_visireport_limit50w"));
					   		<%}	else{
					   		%>
					   		    alert(getJsLocaleMessage("rms","rms_visireport_nodataexp"));
					   		<%
					   		}%>
					   }				 
				});
			});
			function showback()
			{
			   var lgcorpcode =$("#lgcorpcode").val();
			   var lguserid =$("#lguserid").val();
			   location.href='<%=actionPath%>';
			}
		</script>
	</body>
</html>
