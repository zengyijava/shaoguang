<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.engine.vo.LfMoServiceVo"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

@ SuppressWarnings("unchecked")
List<LfMoServiceVo> serMoTaskList = (List<LfMoServiceVo>)request.getAttribute("serMoTaskList");
@ SuppressWarnings("unchecked")
Map<String,String> conditionMap = (Map<String,String>)request.getAttribute("conditionMap");
@ SuppressWarnings("unchecked")
List<Userdata> sendUserList = (List<Userdata>)request.getAttribute("sendUserList");

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("serMoTask");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
		
String httpUrl = StaticValue.getFileServerViewurl();

boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	</head>
	<body class="eng_serMotask" id="eng_serMotask">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		
		
		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
		--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<form name="pageForm" action="<%=path %>/eng_serMoTask.htm?method=find" method="post"
					id="pageForm">
					<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<a id="exportCondition"><emp:message key="znyq_ywgl_common_btn_18" defVal="导出" fileName="znyq"></emp:message></a>
					</div>
					<div id="condition">
						<table >
							<tr>
							   <td ><emp:message key="znyq_ywgl_sxywjl_sjhm_mh" defVal="手机号码：" fileName="znyq"></emp:message></td>
							   <td>
							   		<input type="text" value="<%=conditionMap.get("phone")==null?"":conditionMap.get("phone") %>" class="aaaaa" id="phone" name="phone" maxlength="21" onkeyup="phoneInputCtrl($(this))" onblur="phoneInputCtrl($(this))"/>
							   </td>
							   <td ><emp:message key="znyq_ywgl_sxywjl_xm_mh" defVal="姓名：" fileName="znyq"></emp:message></td>
							   <td>
									<input type="text" value="<%=conditionMap.get("name")==null?"":conditionMap.get("name") %>" id="name" name="name" />
								</td>
								<td ><emp:message key="znyq_ywgl_sxywjl_sxnr_mh" defVal="上行内容：" fileName="znyq"></emp:message></td>
								<td>
									<input type="text" name="msgContent" id="msgContent" value="<%=conditionMap.get("msgContent")==null?"":conditionMap.get("msgContent") %>"/>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
							<tr> 
								<td><emp:message key="znyq_ywgl_sxywjl_hfzt_mh" defVal="回复状态：" fileName="znyq"></emp:message></td>
								<td>
									<select id="replyState" name="replyState" isInput="false">
										<option value=""><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
										<option value="1" <%=(conditionMap.get("replyState")!=null && "1".equals(conditionMap.get("replyState")))?"selected":"" %> ><emp:message key="znyq_ywgl_sxywjl_cg" defVal="成功" fileName="znyq"></emp:message></option>
										<option value="2" <%=(conditionMap.get("replyState")!=null && "2".equals(conditionMap.get("replyState")))?"selected":"" %> ><emp:message key="znyq_ywgl_sxywjl_whf" defVal="未回复" fileName="znyq"></emp:message></option>
									</select>
								</td>
								<td align="left"> <emp:message key="znyq_ywgl_sxywjl_ywbh_mh" defVal="业务编号：" fileName="znyq"></emp:message></td>
								<td> 
									<input type="text"  name="serId" id="serId" value="<%=conditionMap.get("serId")==null?"":conditionMap.get("serId") %>" onkeyup="numberControl($(this))" />
								</td>
								<td><emp:message key="znyq_ywgl_sxywjl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></td>
								<td>
									<input type="text"  name="serName" id="serName" class="commonType" value="<%=conditionMap.get("serName")==null?"":conditionMap.get("serName") %>"/>
						  		</td>
						  		<td></td>
							</tr>
							<tr> 
								<td><emp:message key="znyq_ywgl_sxywjl_sxzl_mh" defVal="上行指令：" fileName="znyq"></emp:message></td>
								<td>
									<input type="text"  name="orderCode" id="orderCode" class="commonType" value="<%=conditionMap.get("orderCode")==null?"":conditionMap.get("orderCode") %>" />
								</td>
								<td align="left"> <emp:message key="znyq_ywgl_sxywjl_cjr_mh" defVal="创建人：" fileName="znyq"></emp:message></td>
								<td> 
									<input type="text"  name="createrName" id="createrName" class="commonType" value="<%=conditionMap.get("createrName")==null?"":conditionMap.get("createrName") %>" />
								</td>
								<td>
								<%--<%=StaticValue.SMSACCOUNT %>：--%>
								<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message>
								</td>
								<td>
									<select id="spUser"  class="input_bd" name="spUser">
									<option value="" ><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
									<%
									if (sendUserList != null && sendUserList.size() > 0) 
									{
										for (Userdata spUser : sendUserList) {
									%>
									<option value="<%=spUser.getUserId()%>" <%=(conditionMap.get("spUser")!=null && spUser.getUserId().equals(conditionMap.get("spUser")))?"selected":"" %> >
										<%=spUser.getUserId() %>
									</option>
									<%}}%>
									</select>
						  		</td>
						  		<td></td>
							</tr>
							<tr>
								<td><emp:message key="znyq_ywgl_sxywjl_sxsj_mh" defVal="上行时间：" fileName="znyq"></emp:message></td>
								<td>
								<input type="text" onclick="stime()"
									value="<%=conditionMap.get("moRecBeginTime")==null?"":conditionMap.get("moRecBeginTime") %>"
									readonly="readonly" class="Wdate"   id="moRecBeginTime"  name="moRecBeginTime"/>
								</td>
								<td align="left"> <emp:message key="znyq_ywgl_common_text_15" defVal="至：" fileName="znyq"></emp:message></td>
								<td> 
								<input type="text"  name="moRecEndTime" id="moRecEndTime" readonly="readonly"
									value="<%=conditionMap.get("moRecEndTime")==null?"":conditionMap.get("moRecEndTime") %>"
									onclick="rtime()"  class="Wdate" />
								</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
								<tr> 
								 <th><emp:message key="znyq_ywgl_sxywjl_sxsj" defVal="上行时间" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_sjhm" defVal="手机号码" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_xm" defVal="姓名" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_sxnr" defVal="上行内容" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_hfnr" defVal="回复内容" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_hfzt" defVal="回复状态" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_ywbh" defVal="业务编号" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_ywmc" defVal="业务名称" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_sxzl" defVal="上行指令" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_cjr" defVal="创建人" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_sxywjl_spzh" defVal="SP账号" fileName="znyq"></emp:message></th>
								</tr>	
							</thead>
							<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="18" align="center"><emp:message key="znyq_ywgl_common_qdjcxhqsj" defVal="请点击查询获取数据" fileName="znyq"></emp:message></td></tr>
							<%}else if (serMoTaskList != null && serMoTaskList.size() > 0)
							{
											for(LfMoServiceVo serMoTask : serMoTaskList)
											{
										%>
											<tr>
												<td>
													<%=df.format(serMoTask.getDeliverTime()) %>
												</td>
												<td>
													<%=serMoTask.getPhone() %>
												</td>
												<td>
													<%=serMoTask.getClientName()==null?"-":serMoTask.getClientName() %>
												</td>
												<td>
													<%=serMoTask.getMsgContent() %>
												</td>
												<td><input type="hidden" name="hidUrl<%=serMoTask.getMsId() %>" value="<%=serMoTask.getReplyUrl() %>"/>
												    <%
												    	if(serMoTask.getReplyUrl()==null || "".equals(serMoTask.getReplyUrl().trim()))
												    	{
												    		//out.print("无法查看");
												    		out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_sxywjl_wfck",request));
												    	}else
												    	{
															if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) {
															%>
													    	<a href="javascript:checkFile('<%=serMoTask.getReplyUrl() %>','<%=path %>')"><emp:message key="znyq_ywgl_sxywjl_djck" defVal="点击查看" fileName="znyq"></emp:message></a>
												    	<%
												    		}else{
												    			out.print("-");
												    		}
												    	}
												    %>
												</td>
												<td>
													<%
													//String repStateStr = "未回复";
													String repStateStr = MessageUtils.extractMessage("znyq","znyq_ywgl_sxywjl_whf",request);
													if(serMoTask.getReplyState()==null){
														
													}else if("1".equals(serMoTask.getReplyState().toString())){
														//repStateStr = "成功";
														repStateStr = MessageUtils.extractMessage("znyq","znyq_ywgl_common_text_11",request);
													}else if("3".equals(serMoTask.getReplyState().toString())){
														//repStateStr = "失败";
														repStateStr = MessageUtils.extractMessage("znyq","znyq_ywgl_common_text_12",request);
													}
														%>
													<%=repStateStr %>
												</td>
												<td>
													<%=serMoTask.getSerId() %>
												</td>
												<td>
													<xmp id="firstXmp">
														<%=serMoTask.getSerName() %>
													</xmp>
												</td>
												<td>
													<%=serMoTask.getOrderCode()==null?"-":serMoTask.getOrderCode() %>
												</td>
												<td>
													<%=serMoTask.getCreaterName() %>
												</td>
												<td>
													<%=serMoTask.getSpUser() %>
												</td>
											
											</tr>
										<%
											}
							}
							            else{
										%>			
										<tr><td colspan="11" align="center"><emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message></td></tr>
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
					</form>
			</div>
			<%} %>
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
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_serMotask.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
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
			//导出全部数据到excel
			$("#exportCondition").click(function(){
				//if(confirm("确定要导出数据到excel?"))
				if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_sxywjl_qdydcsjde")))
				 {
			   		<%
			   		if(serMoTaskList!=null && serMoTaskList.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>	
			   		var langName ='<%=langName %>';				   	   
			   		var lgcorpcode =$("#lgcorpcode").val();
					var lguserid =$("#lguserid").val(); 
					var lgusername =$("#lgusername").val();
					
					$.ajax({
						type: "POST",
						url: "eng_serMoTask.htm?method=ReportMoSerExcel",
						data: {
							langName:langName,lgcorpcode:lgcorpcode,lguserid:lguserid,lgusername:lgusername
						},
						beforeSend: function(){
							page_loading();
						},
		                complete:function () {
							page_complete();
		                },
						success: function(result){
							if(result=='true'){		
								download_href("eng_serMoTask.htm?method=downloadFile&down_session=ReportMoSerExcel");	                    	
		                    }else{
		                        //alert('导出失败！');{
		                        alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywjl_dcsb"));
		                    }
			   			}
					});
					
			   		<%	
			   		}else{%>
			   		    //alert("数据量超过导出的范围50万，请从数据库中导出！");
			   		    alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywjl_sjlcgdcdfw50w"));
			   		<%}
			   		}else{
			   		%>
			   		    //alert("无数据可导出！");
			   		    alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywjl_wsjkdc"));
			   		<%
			   		}%>
				}
			});
		});
		
		
		</script>
		
	</body>
</html>