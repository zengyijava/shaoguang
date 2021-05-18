<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@page import="com.montnets.emp.shortUrl.vo.LfVisitVo"%>
<%@page import="com.montnets.emp.entity.report.AprovinceCity"%>
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
    LfVisitVo taskVo = (LfVisitVo)request.getAttribute("taskVo");
    String actionPath = (String)request.getAttribute("actionPath");
    @ SuppressWarnings("unchecked")
    List<LfVisitVo> taskList = (List<LfVisitVo>)request.getAttribute("taskList");   
    @ SuppressWarnings("unchecked")
    List<AprovinceCity> acitys=(List<AprovinceCity>)request.getAttribute("acitys");
    String province = taskVo!=null&&taskVo.getProvince()!=null?taskVo.getProvince() : "";
    long[] sumArray = (long[]) session.getAttribute("visit_sumArray");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>visitReport.html</title>
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
			<form name="pageForm" action="fx_urlvisit.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
                            <a id="exportCondition"><emp:message key="rms_fxapp_degreerep_export" defVal="导出" fileName="rms"/></a>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									
									<td>
										<emp:message key="rms_taskrecord_ydz2" defVal="源地址：" fileName="rms"/>
									</td>
									<td >	
											
										<input type="text" id="srcurl" name="srcurl" style="width: 180px"  value="<%=taskVo.getSrcurl()==null?"":taskVo.getSrcurl()%>"/>
									</td>
									<td><emp:message key="rms_taskrecord_sendtopic" defVal="发送主题：" fileName="rms"/></td>
									  
									<td>	
  		                                  <input type="text" id="title" name ="title" style="width: 180px"  value="<%=taskVo.getTitle()==null?"":taskVo.getTitle()%>" maxlength="19">	
									</td>
									<td><emp:message key="rms_taskrecord_rwpc" defVal="任务批次：" fileName="rms"/></td>
									  
									<td>	
  		                                  <input type="text" id="taskid" name ="taskid" style="width: 180px" onkeyup="javascript:numberControl($(this))" value="<%=taskVo.getTaskid()==null?"":taskVo.getTaskid()%>" maxlength="19">	
									</td>	
									
									<td class="tdSer">						 
									       <center><a id="search"></a></center>
								    </td>		
								</tr>
								<tr>
								 <td><emp:message key="rms_taskrecord_area" defVal="区域 ：" fileName="rms"/></td>
									<td>
										
											<select name="provinces" id="provinces" style="width:180px">
												<option value=""><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
													<% for(AprovinceCity a: acitys){ %>
													<option <%if(taskVo!=null&&taskVo.getProvince()!=null&&taskVo.getProvince().equals(a.getProvince())){ %> selected="selected" <%} %> value="<%=a.getProvince() %>"><%=a.getProvince() %></option>
													<%
														}
													 %>
												</select>
												<input type="hidden" id="province" name="province" value='<%=province %>' />
										
									</td>
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
								</tr>								
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="rms_fxapp_fsmx_fssj" defVal="发送时间" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_ydz" defVal="源地址" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_hmgs" defVal="号码个数" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_fwrs" defVal="访问人数" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_fwcs" defVal="访问次数" fileName="rms"/>
								</th>
								<th>
									<emp:message key="rms_taskrecord_hmxq" defVal="号码详情" fileName="rms"/>
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
								for(LfVisitVo task : taskList)
								{
									
									%>
										<tr>
											<td> 
												<%=taskVo.getStartTime()==null?"-":taskVo.getStartTime() %>至<%=taskVo.getEndTime()==null?"-":taskVo.getEndTime() %>
											</td>
											<td class="textalign">
												<xmp><%=task.getSrcurl()==null?"-":task.getSrcurl() %></xmp>
											</td>
											<td>
												<%=task.getTotal()%>
											</td>
											<td >
											<%=task.getVisitp()%>
											</td>
											
											<td>
												<%=task.getVisnum()%>
											</td>
																					
											<td>
													<a onclick="javascript:location.href='<%=actionPath%>?method=getDetail&srcurl=<%=task.getSrcurl()%>&sendTime=<%=taskVo.getStartTime()%>&endTime=<%=taskVo.getEndTime()%>'">号码详情</a>
											</td>
											
						</tr>
						<%} %>
						<tr>
						    <td colspan="2"><b><emp:message key="rms_fxapp_degreerep_total" defVal="合计：" fileName="rms"/></b></td>
                            <%                             
                                if(sumArray != null && sumArray.length >=3)
                                {
                                %>
                                <td><%=sumArray[2]%></td>
                                <td><%=sumArray[1]%></td>
                                <td><%=sumArray[0]%></td>
						     <td>-</td>
						    </tr>	
						    <%} %>
						<%
								
							}else{
						%>
						<tr><td align="center" colspan="7"><emp:message key="rms_fxapp_fsmx_norecord" defVal="无记录" fileName="rms"/></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="7">
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
				$('#provinces').isSearchSelect({'width':'180','zindex':1},function(data){
					//keyup click触发事件
						$("#province").val(data.value);
				},function(data){
					//初始化加载
					var val=$("#province").val();
					if(val){
						data.box.input.val(val);
					}
				});
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
					   	    
					   		<%
					   		if(taskList!=null && taskList.size()>0 && pageInfo.getTotalRec()<=500000){
					   		%>	
					   				$.ajax({
									type: "POST",
									url: "<%=path%>/fx_urlvisit.htm?method=r_VisitExportExcel",
									data: {userid:userIds,
											taskid:taskid,
											title:title,
											sendTime:sendTime,
											lguserid:lguserid,
											endTime:endTime,
											},
					                beforeSend:function () {
										page_loading();
					                },
					                complete:function () {
								    	page_complete();
					                },
									success: function(result){
					                        if(result=='true'){
					                           download_href("<%=path%>/fx_urlvisit.htm?method=downloadFile");
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
		</script>
	</body>
</html>
