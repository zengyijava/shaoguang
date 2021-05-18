<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.shorturl.report.vo.ReplyDetailVo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	PageInfo prePageInfo = (PageInfo)session.getAttribute("prePageInfo");

	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");

	String taskId = (String)request.getAttribute("taskId");
			
	@ SuppressWarnings("unchecked")
	List<ReplyDetailVo> replyDetailVos =(List<ReplyDetailVo>) request.getAttribute("replyDetailVos");

	/*数据回显*/
	String replyPhone = request.getAttribute("replyPhone") == null ? "":(String)request.getAttribute("replyPhone");
	String replyName = request.getAttribute("replyName")== null ? "":(String)request.getAttribute("replyName");
	String replyContent = request.getAttribute("replyContent")== null ? "":(String)request.getAttribute("replyContent");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String actionPath = (String)request.getAttribute("actionPath");
	String menuCode = titleMap.get("smsTaskRecord");
	menuCode=menuCode==null?"0-0-0":menuCode;
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String fsxqck =  com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_170", request);
	
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
			#baseInfo tr td{
				padding-top: 10px;
				font-size: 15px;
			}
		</style>
	</head>
	<body>
		<%--存储上一个页面的分页信息--%>
		<input type="hidden" name="prePageSize" id="prePageSize" value="<%=prePageInfo.getPageSize()%>">
		<input type="hidden" name="prePageIndex" id="prePageIndex" value="<%=prePageInfo.getPageIndex()%>">
		<input type="hidden" name="preTotalPage" id="preTotalPage" value="<%=prePageInfo.getTotalPage()%>">
		<input type="hidden" name="preTotalRec" id="preTotalRec" value="<%=prePageInfo.getTotalRec()%>">
		<div id="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,fsxqck) %>
				<div class="rContent">
					<%              		
						if(btnMap.get(menuCode+"-0")!=null)                       		
						{                        	
					%>
					<div class="titletop" style="padding-left: 0;padding-top: 0">
						<table class="titletop_table" style="width: 100%">
							<tr>
								<td class="titletop_td">
								 <emp:message key="dxzs_xtnrqf_title_170" defVal="发送详情查看" fileName="dxzs"/>
								</td>
								<td align="right">
									<span class="titletop_font"    onclick="showback(<%=path%>)">&larr;&nbsp;<emp:message key="dxzs_xtnrqf_title_89" defVal="返回上一级" fileName="dxzs"/></span>
								</td>
							</tr>
						</table>
				    </div>
					<form name="pageForm" action="surlBatchRecord.htm?method=getReplyDetailByCondition" method="post">
						<input type="hidden" name="taskId" id="taskId" value="<%=taskId %>">
						<div class="buttons" style="padding-top: 10px;">
						  <div id="toggleDiv">
						  </div> 
						  <span id="backgo" class="right mr5" onclick="showback('<%=path%>')">&nbsp;<emp:message key="dxzs_xtnrqf_button_9" defVal="返回" fileName="dxzs"/></span>
						  <a id="exportCondition" ><emp:message key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs"/></a>				  					 			          
						</div>
						<div id="condition">
						 <table>
						    <tr>
						      <td>姓名：</td>
						      <td>
								  <input type="text" style="width: 181px;" value='<%=replyName%>' id="replyName" name="replyName"/>
							  </td>
						      <td>手机号码：</td>
						      <td>
						         <input type="text" style="width: 181px;" value='<%=replyPhone%>' id="replyPhone" name="replyPhone" maxlength="21" onkeyup="phoneInputCtrl($(this))"/>
						      </td>
						      <td>回复内容：</td>
						      <td>
								  <input type="text" style="width: 181px;" value='<%=replyContent%>' id="replyContent" name="replyContent"/>
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
										手机号码
									</th>
									<th>
										姓名
									</th>
									<th>
										回复内容
									</th>
									<th>
										回复时间
									</th>
								</tr>
							</thead>
							<%
							if(replyDetailVos!= null && replyDetailVos.size() > 0)
								{
								for(ReplyDetailVo vo : replyDetailVos)
								{
							%>
							<tr>
								<td>
									<%=vo.getPhone()==null||"".equals(vo.getPhone())?"":vo.getPhone()%><%--手机号--%>
								</td> 
								<td>
									<%=vo.getReplyName()==null||"".equals(vo.getReplyName())?"":vo.getReplyName()%><%--姓名--%>
								</td>
								<td>
									<%=vo.getMsgContent()==null||"".equals(vo.getMsgContent())?"":vo.getMsgContent()%><%--回复内容--%>
								</td>
								<td class="textalign">
									<%=vo.getDeliverTime()==null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getDeliverTime().getTime())%><%--回复时间--%>
								</td>
							</tr>
							<%
								}
								}
								else
								{
							%>
							<tr><td colspan="6" align="center"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
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
								<emp:message key="dxzs_xtnrqf_title_186" defVal="请耐心等待....." fileName="dxzs"/>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
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
				function() {
					//点击查询按钮时，需要新数据
					submitForm();
				}
			);
		    $("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});	

			//导出全部数据到excel
			$("#exportCondition").click(
				function() {
		   			var taskId=$.trim($("#taskId").val());
		   			var replyName=$.trim($("#replyName").val());
		   			var replyPhone =$("#replyPhone").val();
					var replyContent =$("#replyContent").val();
                    var lgcorpcode =$("#lgcorpcode").val();
                    var lguserid =$("#lguserid").val();
				 	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210'))) {
				   		<%
				   		if(replyDetailVos!=null && replyDetailVos.size()>0 ){
				   		  	if(pageInfo.getTotalRec()<=15000000){
				   		%>	
					   		   $.ajax({
									type: "POST",
									url: "<%=path%>/surlBatchRecord.htm?method=sendReplyDetailExcel",
									data: {
											taskId:taskId,
											replyName:replyName,
                                        	replyPhone:replyPhone,
											replyContent:replyContent,
											lgcorpcode:lgcorpcode,
											lguserid:lguserid
										  },
					                beforeSend:function () {
										page_loading();
					                },
					                complete:function () {
								    	page_complete();
					                },
									success: function(result){
					                        if(result=='true'){
					                           download_href("<%=path%>/surlBatchRecord.htm?method=downloadFile&type=SendReplyDetail");
					                        }else{
					                            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
					                        }
						   			}
								});	
					   		    
					   		<%}else{
					   		%>				   		    
					   		     alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_302'));
					   		<%}
				   		}else{
				   		%>
				   			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_212'));
				   		<%
				   		}
				   		%>
				   	}				 
				});
			
			});

			function showback(path) {
				var prePageSize = $("#prePageSize").val();
				var prePageIndex = $("#prePageIndex").val();
				var preTotalPage = $("#preTotalPage").val();
				var preTotalRec = $("#preTotalRec").val();
                var lguserid = $("#lguserid").val();
				page_loading();
				window.location.href = path + "/surlBatchRecord.htm?method=find" +
					"&pageSize=" + prePageSize +
					"&pageIndex=" + prePageIndex +
					"&totalPage=" + preTotalPage +
					"&totalRec=" + preTotalRec +
					"&lguserid=" + lguserid;
			}

			//关闭加载窗口的方法		
			function closelog(){
			    var lguserid =$("#lguserid").val();
	            $.post("<%=path%>/smt_smsTaskRecord.htm?method=checkResult",{lguserid:lguserid},function(result){
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
