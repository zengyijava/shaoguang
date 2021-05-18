<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.vo.SendedMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
			
	@ SuppressWarnings("unchecked")
	List<SendedMttaskVo> mtList =(List<SendedMttaskVo>) request.getAttribute("mtList");
	String title = (String)request.getAttribute("title");
	String message = (String)request.getAttribute("message");
	String sendtime = (String)request.getAttribute("sendtime");
	String sendinfo = (String)request.getAttribute("sendinfo");
	String mtid = (String)request.getAttribute("mtid");
	String type=(String)request.getAttribute("type");
	String phone = (String)request.getAttribute("phone");
	String spisuncm = (String)request.getAttribute("spisuncm");
	String taskId = (String)request.getAttribute("taskId");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String menuCode = titleMap.get("smsTaskRecord");
	menuCode=menuCode==null?"0-0-0":menuCode;
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
				<div class="rContent">
					<%              		
						if(btnMap.get(menuCode+"-0")!=null)                       		
						{                        	
					%>
					<div class="titletop" style="padding-left: 0;padding-top: 0">
						<table class="titletop_table" style="width: 100%">
								<tr>
								    <td class="titletop_td">
								        <emp:message key="appmage_smstask_text_1" defVal="发送详情查看" fileName="appmage" />   
						        	</td>
									<td align="right">
										<span class="titletop_font"    onclick="javascript:showback()">&larr;&nbsp;
										<emp:message key="appmage_smstask_text_2" defVal="返回上一级" fileName="appmage" /></span>
										
									</td>
								</tr>
							</table>
					   </div>
						<div id="u_o_c_explain" class="div_bg">
							<p>
								<emp:message key="appmage_smstask_text_3" defVal="基本信息 " fileName="appmage" />   
							</p>
                            <table>
                              <tr>
                                 <td><b><emp:message key="appmage_smstask_text_4" defVal="主题：" fileName="appmage" /></b></td>
                              </tr>
                              <tr>
                                 <td><%=title==null?"":(title.replaceAll("<","&lt;").replaceAll(">","&gt;")) %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="appmage_smstask_text_5" defVal="内容：" fileName="appmage" /></b></td>
                              </tr>
                              <tr>
                                 <td style="word-wrap: break-word;word-break:break-all;"><%=message==null||"".equals(message)?"":message.replaceAll("<","&lt;").replaceAll(">","&gt;")
                                		 .replaceAll("#[pP]_(\\d+)#",MessageUtils.extractMessage("appmage","appmage_smstask_text_35",request)) %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="appmage_smstask_text_6" defVal="发送时间： " fileName="appmage" /></b></td>
                              </tr>
                              <tr>
                                 <td><%=sendtime %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="appmage_smstask_text_7" defVal="发送情况： " fileName="appmage" /></b></td>
                              </tr>
                              <tr>
                                <td><%=sendinfo %>
                                <font class="zhu"><emp:message key="appmage_smstask_text_8" defVal="注：以上数据" fileName="appmage" />   
                                <%=StaticValue.getCORPTYPE() ==0?MessageUtils.extractMessage("appmage","appmage_smstask_text_9",request):MessageUtils.extractMessage("appmage","appmage_smstask_text_10",request)%><emp:message key="appmage_smstask_text_11" defVal=" 汇总一次,可能与下面的实时数据有一定误差。" fileName="appmage" /></font>
                                </td>
                              </tr>
                            </table>
						</div>
						<form name="pageForm" action="app_appsmsTaskRecord.htm?method=findAllSendInfo" method="post">
						<input type="hidden" name="taskId" id="taskId" value="<%=taskId %>">
						<div class="buttons" style="padding-top: 10px;">
						  <div id="toggleDiv">								
						  </div> 
						  <span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="appmage_smstask_text_12" defVal="返回" fileName="appmage" /></span>
						  <a id="exportCondition" ><emp:message key="appmage_smstask_text_13" defVal="导出 " fileName="appmage" /></a>				  					 			          
						</div>
						<div id="condition">
						 <input type="hidden" id="mtid" value="<%=mtid %>" name="mtid">
						 <table>
						    <tr>
						      <td><emp:message key="appmage_smstask_text_14" defVal="运营商： " fileName="appmage" /></td>
						      <td>
						           <select style="width:180px;"  id="spisuncm" name="spisuncm">
										<option value=""><emp:message key="appmage_smstask_text_15" defVal="全部 " fileName="appmage" /></option>
										<option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key="appmage_smstask_text_16" defVal="移动 " fileName="appmage" />   </option>
										<option value="1" <%="1".equals(spisuncm)?"selected":"" %> ><emp:message key="appmage_smstask_text_17" defVal="联通" fileName="appmage" /></option>
										<option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key="appmage_smstask_text_18" defVal="电信" fileName="appmage" /></option>
									</select>
						      </td>
						      <td><emp:message key="appmage_smstask_text_19" defVal="手机号码：" fileName="appmage" /></td>
						      <td>
						         <input type="text" style="width: 177px;" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
						      </td>
						      <td><emp:message key="appmage_smstask_text_20" defVal="状态：" fileName="appmage" /></td>
						      <td>
						          <select style="width:180px;"  id="type" name="type">
										<option value="1" <%="1".equals(type)?"selected":"" %>><emp:message key="appmage_smstask_text_21" defVal="全部号码" fileName="appmage" /></option>
										<%--
										<option value="5" <%="5".equals(type)?"selected":"" %> >发送成功</option>
										<option value="6" <%="6".equals(type)?"selected":"" %> >状态未返</option>										
										--%>
										<option value="2" <%="2".equals(type)?"selected":"" %>><emp:message key="appmage_smstask_text_22" defVal="接收失败" fileName="appmage" /></option>
										<option value="3" <%="3".equals(type)?"selected":"" %>><emp:message key="appmage_smstask_text_23" defVal="提交失败" fileName="appmage" /></option>
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
										<emp:message key="appmage_smstask_text_24" defVal="序号 " fileName="appmage" />   
									</th>
									<th>
									    <emp:message key="appmage_smstask_text_25" defVal="运营商 " fileName="appmage" />   
									</th>
									<th>
										<emp:message key="appmage_smstask_text_26" defVal="手机号 " fileName="appmage" /> 
									</th>
									<th>
										<emp:message key="appmage_smstask_text_27" defVal="内容 " fileName="appmage" /> 
									</th>
									<th>
										<emp:message key="appmage_smstask_text_28" defVal="分条" fileName="appmage" /> 
									</th>
									<th>
										<emp:message key="appmage_smstask_text_29" defVal="状态 " fileName="appmage" /> 
									</th>
								</tr>
							</thead>
							<%
							if(type.equals("0"))
							{
							%>
							<tr><td colspan="6" align="center"><emp:message key="appmage_smstask_text_30" defVal="请点击按钮获取数据" fileName="appmage" /> </td></tr>
							<%
							}else if(mtList!= null && mtList.size() > 0)
								{
								for(int i=0;i<mtList.size();i++)
								{
									SendedMttaskVo vo = mtList.get(i);
							%>
							<tr>
								<td>
									<%=i+1 %>
								</td> 
								<td>
				  					<%
									if(vo.getUnicom()-0==0){%>
									<emp:message key="appmage_smstask_text_16" defVal="移动" fileName="appmage" /> 
									<%}else if(vo.getUnicom()-1==0){%>
									<emp:message key="appmage_smstask_text_17" defVal="联通" fileName="appmage" /> 
									<%}else if(vo.getUnicom()-21==0){ %>
									<emp:message key="appmage_smstask_text_18" defVal="电信" fileName="appmage" /> 
									<%} %>
								</td>
								<td>
									<%
									   String phones = CV.replacePhoneNumber(btnMap,vo.getPhone());
									   out.print(phones);									   
									 %>
								</td>
								<td class="textalign">									
									<%=vo.getMessage()==null||"".equals(vo.getMessage())?"":vo.getMessage().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
								</td>
								<td>
									<%=vo.getPknumber() %>/<%=vo.getPktotal() %>
								</td>
								<td class="textalign">
								<%if(vo.getErrorcode() != null){ %>
								    <%if(vo.getErrorcode().equals("DELIVRD")||vo.getErrorcode().trim().equals("0")){  %>
								         <label><emp:message key="appmage_smstask_text_31" defVal="发送成功" fileName="appmage" /> </label>  
								    <%}else if(vo.getErrorcode().toString().trim().length()>0){%>
								         <label><emp:message key="appmage_smstask_text_32" defVal="失败" fileName="appmage" />[<%=vo.getErrorcode() %>]</label>
								    <%}else { %>
								         <label>-</label>
								    <% }%>
								<%}else { %>
								    <label>-</label>
								<%} %>
								</td>

							</tr>
							<%
								}
								}
								else
								{
							%>
							<tr><td colspan="6" align="center"><emp:message key="appmage_smstask_text_33" defVal="无记录 " fileName="appmage" /> </td></tr>
							<%} %>
							<tfoot>
							<tr >
								<td colspan="6">
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
						
					 <div id="probar" style="display: none;text-align: center;">
							<p >
								<emp:message key="appmage_smstask_text_34" defVal="请耐心等待....." fileName="appmage" /> 
							</p>
							<div id="shows">
								<img src="<%=commonPath%>/common/img/loader.gif" />
							</div>							
					</div>					
					</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
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
			$('#search').click(function(){submitForm();});
		    $("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});	

			//导出全部数据到excel
			$("#exportCondition").click(
				function()
				{
		   			var mtid=$.trim($("#mtid").val());
		   			var type=$.trim($("#type").val());
		   			var lgcorpcode =$("#lgcorpcode").val();
					var lguserid =$("#lguserid").val();
					var phone = $("#phone").val();
					var spisuncm = $("#spisuncm").val();
				 	if(confirm(getJsLocaleMessage('appmage','appmage_smstask_text_1')))
				 	{
				   		<%
				   		if(mtList!=null && mtList.size()>0 ){
				   		%>  
				   		    var IsexportAll = true;
				   		    if(confirm(getJsLocaleMessage('appmage','appmage_smstask_text_2')))
				   		    {
				   		        IsexportAll = false;
				   		    }  
	   	
				   		<%		     
				   		  	if(pageInfo.getTotalRec()<=500000){
				   		%>	
//					   		   $("#probar").dialog({
//								modal:true,
//								title:'加载进度..', 
//								height:80,
//								resizable :false,
//								closeOnEscape: false,
//								width:230,
//								open: function(){
//									$(".ui-dialog-titlebar").hide();
//									dd = window.setInterval("closelog()",2000);						
//						          }
//								});	
				                $.ajax({
									type:"POST",
									url: "<%=path%>/app_appsmsTaskRecord.htm",
									data: {method: "smsReportAllExcel",
										mtid:mtid,
										type:type,
										lgcorpcode:lgcorpcode,
										lguserid:lguserid,
										spisuncm:spisuncm,
										phone:phone,
										IsexportAll:IsexportAll
					   					},
					   				beforeSend: function(){
					   					page_loading();
									},
									complete:function(){
										page_complete();
									},
									success: function(result){
										if(result)
										{
											window.parent.complete();
											download_href("<%=path%>/app_appsmsTaskRecord.htm?method=downloadFile");
										}else
										{
											alert(getJsLocaleMessage('appmage','appmage_smstask_text_3'));
										}
									}
								});
					   		    
					   		<%}else{
					   		%>				   		    
					   		     alert(getJsLocaleMessage('appmage','appmage_smstask_text_4'));
					   		<%}
				   		}else{
				   		%>
				   			alert(getJsLocaleMessage('appmage','appmage_smstask_text_5'));
				   		<%
				   		}
				   		%>
				   	}				 
				});
			
			});
			
			function showback()
			{
			   var lgcorpcode =$("#lgcorpcode").val();
			   var lguserid =$("#lguserid").val();
			   location.href="app_appsmsTaskRecord.htm?method=findallLfMttask&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid;
			}
			//关闭加载窗口的方法		
			function closelog(){
			    var lguserid =$("#lguserid").val();
	            $.post("<%=path%>/app_appsmsTaskRecord.htm?method=checkResult",{lguserid:lguserid},function(result){
	        	    if(result != null && result =="over" )
	        	    {
	        	          $("#probar").dialog("close");
			              window.clearInterval(dd);
	        	    }
	        	});	
			}
		</script>
	</body>
</html>
