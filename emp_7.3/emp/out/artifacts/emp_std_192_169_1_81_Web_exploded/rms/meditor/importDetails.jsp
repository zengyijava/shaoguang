<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@ page import="com.montnets.emp.common.context.EmpExecutionContext" %>
<%@ page import="com.montnets.emp.rms.meditor.vo.LfTempImportDetailsVo" %>
<%@page import="com.montnets.emp.rms.vo.LfTempImportBatchVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("/importTemplateDetails");
	menuCode=menuCode==null?"0-0-0":menuCode;
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
			
	@ SuppressWarnings("unchecked")
	List<LfTempImportDetailsVo> details =(List<LfTempImportDetailsVo>) request.getAttribute("details");
	
	@ SuppressWarnings("unchecked")
	LfTempImportBatchVo tempBatch =(LfTempImportBatchVo) request.getAttribute("tempBatch");

	//手机号码
	String phoneNum= request.getParameter("phoneNum");
	//导入状态
	String importStatus= request.getParameter("importStatus");
	//审核状态
	String auditstatus= request.getParameter("auditstatus");
	
	
	
	if(phoneNum==null||"".equals(phoneNum.trim()))
	{
		phoneNum="";
	}
	
	if(importStatus==null||"".equals(importStatus.trim())){
		importStatus="";
	}else{
		try{
			Long.parseLong(importStatus.trim());
			importStatus=importStatus.trim();
		}catch(Exception e){
			EmpExecutionContext.error(e, "导入状态转换异常!importStatus:" + importStatus);
			importStatus="";
		}
	}
	
	if(auditstatus==null||"".equals(auditstatus.trim())){
		auditstatus="";
	}else{
		try{
			Long.parseLong(auditstatus.trim());
			auditstatus=auditstatus.trim();
		}catch(Exception e){
			EmpExecutionContext.error(e, "审核状态转换异常!auditstatus:" + auditstatus);
			auditstatus="";
		}
	}

	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<style type="text/css">
			#u_o_c_explain table td{
				height: 25px;
			}
		</style>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
	</head>
	<body>
		<div id="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
				<div class="rContent">
					<%              		
						if(btnMap.get(menuCode+"-0")!=null)                       		
						{                        	
					%>
						<div id="u_o_c_explain" class="div_bg">
							<h3>
								<emp:message key="dxzs_xtnrqf_title_171bak" defVal="基本信息" fileName="dxzs"/>:
							</h3>
                            <table>
							  <% if (StaticValue.getCORPTYPE() == 1) {%>
                              <tr>
                                 <td><b><emp:message key="dxzs_xtnrqf_title_90bak" defVal="企业编号" fileName="dxzs"/>：</b></td>
								 <td><%=tempBatch.getCorpCode() %></td>
                              </tr>
							  <tr>
                                 <td><b><emp:message key="dxzs_xtnrqf_title_90bak" defVal="企业名称" fileName="dxzs"/>：</b></td>
								 <td><%=tempBatch.getCorpName() %></td>
                              </tr>
							  <% }%>
							  <tr>
                                 <td><b><emp:message key="dxzs_xtnrqf_title_90bak" defVal="任务批次" fileName="dxzs"/>：</b></td>
								 <td><%=tempBatch.getBatch() %></td>
                              </tr>
							  <tr>
                                 <td><b><emp:message key="dxzs_xtnrqf_title_90bak" defVal="富信主题" fileName="dxzs"/>：</b></td>
								 <td><%=tempBatch.getTmName()%></td>
                              </tr>
							  <tr>
                                 <td><b><emp:message key="dxzs_xtnrqf_title_90bak" defVal="导入时间" fileName="dxzs"/>：</b></td>
								 <td><%=df.format(tempBatch.getAddtime()) %></td>
                              </tr>
                              
                            </table>
						</div>

						<form name="pageForm" action="importTemplateDetails.htm?method=findAllSendInfo" method="post">
						<input type="hidden" name="batch" id="batch" value="<%=tempBatch.getBatch() %>">
						<div class="buttons" style="padding-top: 10px;">
						  <div id="toggleDiv">								
						  </div> 
						  <span id="backgo" class="right mr5" onclick="javascript:showback();">&nbsp;<emp:message key="dxzs_xtnrqf_button_9bak" defVal="返回" fileName="dxzs"/></span>
						  <a id="exportCondition" ><emp:message key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs"/></a>				  					 			          
						</div>
						<div id="condition">
						 <input type="hidden" id="path" value="<%=path %>" name="path">
						 <table>
						    <tr>
						      <td><emp:message key="dxzs_xtnrqf_title_119bak" defVal="手机号码" fileName="dxzs"/>：</td>
						      <td>
						         <input type="text" style="width: 177px;" value='<%=phoneNum==null?"":phoneNum %>' id="phoneNum" name="phoneNum" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
						      </td>
						      <td><emp:message key="dxzs_xtnrqf_title_103bak" defVal="导入状态" fileName="dxzs"/>：</td>
						      <td>
						          <select style="width:180px;"  id="importStatus" name="importStatus">
										<option value="" <%="".equals(importStatus)?"selected":"" %>>全部</option>
										
										<option value="1" <%="1".equals(importStatus)?"selected":"" %> >成功</option>
										<option value="0" <%="0".equals(importStatus)?"selected":"" %> >失败</option>										
										
								  </select>
						       </td>
						       <td><emp:message key="dxzs_xtnrqf_title_103bak" defVal="审核状态" fileName="dxzs"/>：</td>
							   <td>
						          <select style="width:180px;"  id="auditstatus" name="auditstatus">
										<option value="" <%="".equals(auditstatus)?"selected":"" %>>全部</option>
										<option value="-1" <%="-1".equals(auditstatus)?"selected":"" %> >未审批</option>
										<option value="0" <%="0".equals(auditstatus)?"selected":"" %> >无需审批</option>
										<option value="1" <%="1".equals(auditstatus)?"selected":"" %> >审批通过</option>
										<option value="2" <%="2".equals(auditstatus)?"selected":"" %>>审批未通过</option>
										<option value="3" <%="3".equals(auditstatus)?"selected":"" %>>审核中</option>
										<option value="4" <%="4".equals(auditstatus)?"selected":"" %>>禁用</option>
								  </select>
						       </td>
						      <td class="tdSer">
								<center><a id="search"></a></center>
							  </td>
						    </tr> 
						 </table>
						</div>
						<table id="content">
							<thead>
								<tr>
									<th>
										<emp:message key="dxzs_xtnrqf_title_182bak" defVal="序号" fileName="dxzs"/>
									</th>
									<%--<th>
									    <emp:message key="dxzs_xtnrqf_title_178bak" defVal="姓名" fileName="dxzs"/>
									</th>--%>
									<th>
										<emp:message key="dxzs_xtnrqf_title_119bak" defVal="手机号码" fileName="dxzs"/>
									</th>

									<th>
										<emp:message key="dxzs_xtnrqf_title_172bak" defVal="导入状态" fileName="dxzs"/>
									</th>

									<th>
										<emp:message key="dxzs_xtnrqf_title_183bak" defVal="详情" fileName="dxzs"/>
									</th>

									<th>
										<emp:message key="dxzs_xtnrqf_title_183bak" defVal="审核状态" fileName="dxzs"/>
									</th>
									<%--<th>--%>
										<%--<emp:message key="dxzs_xtnrqf_title_103bak" defVal="操作" fileName="dxzs"/>--%>
									<%--</th>--%>
								</tr>
							</thead>
							</tr>
							<%
							if(details!= null && details.size() > 0)
								{
								int num = 0;
								for(int i=0;i<details.size();i++)
								{
								num = i+1;
									LfTempImportDetailsVo detailsDto = details.get(i);
							%>
							<tr>
								<td>
									<%=num%>
								</td>
								<%--<td>
									<%=detailsDto.getName()==""?"-":detailsDto.getName()%>	
								</td>--%>
								<td>
									<%
									   String phones = CV.replacePhoneNumber(btnMap,detailsDto.getPhoneNum());
									   out.print(phones);									   
									 %>
								</td>
								<td>
									<%
									if(detailsDto.getImportStatus()-1==0){%>
									<emp:message key="dxzs_xtnrqf_title_21bak" defVal="成功" fileName="dxzs"/>
									<%}else if(detailsDto.getImportStatus()==0){%>
									<emp:message key="dxzs_xtnrqf_title_22bak" defVal="失败" fileName="dxzs"/>
									<%} else{%>
									<emp:message key="dxzs_xtnrqf_title_24bak" defVal="--" fileName="dxzs"/>
									<%}%>
								</td>
								<td>
								<%
									if(detailsDto.getImportStatus()==0){%>
										<%=detailsDto.getCause()%>
									<%}else{%>
										- -
									<%}%>
								</td>
								<td>
									<%
										if(detailsDto.getAuditstatus()+1==0){%>
									<emp:message key="dxzs_xtnrqf_title_21bak" defVal="未审批" fileName="dxzs"/>
									<%}else if(detailsDto.getAuditstatus()==0){%>
									<emp:message key="dxzs_xtnrqf_title_22bak" defVal="无需审批" fileName="dxzs"/>
									<%}else if(detailsDto.getAuditstatus()-1==0){ %>
									<emp:message key="dxzs_xtnrqf_title_23bak" defVal="审批通过" fileName="dxzs"/>
									<%}else if(detailsDto.getAuditstatus()-2==0){ %>
									<emp:message key="dxzs_xtnrqf_title_23bak" defVal="审批未通过" fileName="dxzs"/>
									<%}else if(detailsDto.getAuditstatus()-3==0){ %>
									<emp:message key="dxzs_xtnrqf_title_23bak" defVal="审核中" fileName="dxzs"/>
									<%} else if(detailsDto.getAuditstatus()-4==0){%>
									<emp:message key="dxzs_xtnrqf_title_24bak" defVal="禁用" fileName="dxzs"/>
									<%}else{%>
									--
									<%}%>
								</td>
								<%----%>
								<%--<td>--%>
									 <%--<a onclick="javascript:location.href='<%=path%>/importTemplateDetails.htm?method=smsSend&batch=<%=tempBatch.getBatch()%>'"><emp:message key="dxzs_xtnrqf_title_106bak" defVal="立即发送" fileName="dxzs"/></a>--%>
								<%--</td>--%>
							</tr>
							<%
								}
								}
								else
								{
							%>
							<tr><td colspan="5" align="center"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
							<%} %>
							<tfoot>
							<tr >
								<td colspan="5">
								<div id="pageInfo"></div>
								</td>
							</tr>
							</tfoot>
						</table>
						<div id="smssendparams" class="hidden"></div>
						</form>
						<div class="clear"></div>
						<%
								}
						%>
										
					</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script type="text/javascript">
		$(document).ready(function() {
		    getLoginInfo("#smssendparams");
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(
				function()
				{
					//点击查询按钮时，需要新数据
					$("#isclicksearch").attr("value","1");
					submitForm();
				}
			);
		    $("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});	
			
			
			
			
			
			//导出全部数据到excel
			<%--
			$('#backgo').click(
				function()
				{
					//点击查询按钮时，需要新数据
					location.href="<%=path%>/importTemplateDetails.htm?method=find";
				}
			);
			--%>
			var details = "<%=details%>";
			$("#exportCondition").click(
				function()
				{
		   			var phoneNum=$.trim($("#phoneNum").val());
		   			var importStatus=$.trim($("#importStatus").val());
		   			var auditstatus =$("#auditstatus").val();
		   			var batch ="<%=tempBatch.getBatch()%>";
		   		
				 	if(confirm("是否继续导出"))
				 	{
				   		<%
				   		if(details!=null && details.size()>0 ){
				   		%>  
				   		    var IsexportAll = true;
				   		   // if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_301')))
				   		    //{
				   		       // IsexportAll = false;
				   		    //}  
	   	
				   		<%		     
				   		  	if(pageInfo.getTotalRec()<=15000000){
				   		%>	
					   		   $.ajax({
									type: "GET",
									url: "<%=path%>/importTemplateDetails.htm?method=smsReportAllExcel",
									data: {
											auditstatus:auditstatus,
											importStatus:importStatus,
											phoneNum:phoneNum,
											batch:batch,
											IsexportAll:IsexportAll
										  },
					                beforeSend:function () {
										page_loading();
					                },
					               complete:function () {
								    	page_complete();
					                },
									success:function(result){
										var jsonObject = JSON.parse(result);  
										var status = jsonObject.status;
										var data = jsonObject.data;
										var src = data.src;
										
					                        if(status==true){
					                           download_href("<%=path%>/importTemplateDetails.htm?method=downloadFile&data="+src);
					                        }else{
					                            alert("下载失败");
					                        }
					                    page_complete();
						   			}
								});	
					   		    
					   		<%}else{
					   		%>				   		    
					   		     alert("下载失败");
					   		<%}
				   		}else{
				   		%>
				   			alert("下载失败");
				   		<%
				   		}
				   		%>
				   	}				 
				});
			
			});
			
			
			function showback()
			{
			   location.href="<%=path%>/importTemplateDetails.htm?method=find";
			}
			
		
		</script>
	</body>
</html>
