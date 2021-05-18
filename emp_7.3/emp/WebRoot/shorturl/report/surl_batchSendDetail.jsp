<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.montnets.emp.shorturl.report.vo.SendDetailMttaskVo" %>
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
	
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));

	PageInfo prePageInfo = (PageInfo)session.getAttribute("prePageInfo");

	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
			
	@ SuppressWarnings("unchecked")
	List<SendDetailMttaskVo> mtList =(List<SendDetailMttaskVo>) request.getAttribute("mttaskVoList");
	//从session中取相关信息
	HashMap<String,String> map = (HashMap<String, String>) request.getSession(false).getAttribute("batchSendDetailMap");
	String title = "";
	String message = "";
	String sendtime = "";
	String sendinfo = "";
	String taskId = "";
	if(map != null){
		title = map.get("title") == null ? "" : map.get("title").replaceAll("<","&lt;").replaceAll(">","&gt;") ;
	 	message = map.get("message") == null ? "":map.get("message").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("#[pP]_(\\d+)#","{#参数$1#}");
	 	sendtime = map.get("sendtime") == null ? "":map.get("sendtime");
	 	sendinfo = map.get("sendInfo") == null ? "":map.get("sendInfo");
		taskId = map.get("taskId") == null ? "":map.get("taskId");
	}
	/*数据回显*/
	String type=(String)request.getAttribute("type");
	String phone = (String)request.getAttribute("phone");
	String spisuncm = (String)request.getAttribute("spisuncm");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String actionPath = (String)request.getAttribute("actionPath");
	String menuCode = titleMap.get("smsTaskRecord");
	menuCode=menuCode==null?"0-0-0":menuCode;
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	String fsxqck =  com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_170", request);

	String sixHour = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_176", request);
	String mday = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_177", request);
	
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
						<div id="u_o_c_explain" class="div_bg">
							<h2>
								<emp:message key="dxzs_xtnrqf_title_171" defVal="基本信息" fileName="dxzs"/>
							</h2>
							<table id="baseInfo">
                              <tr>
								<td><b>发送主题：</b></td>
							  	<td><%=title%></td>
							  </tr>
                              <tr>
							 	<td><b>发送内容：</b></td>
						  		<td style="word-wrap: break-word;word-break:break-all;"><%=message%></td>
							  </tr>
                              <tr>
							 	<td><b><emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>：</b></td>
							  	<td><%=sendtime %></td>
							  </tr>
                              <tr>
                                 <td><b><emp:message key="dxzs_xtnrqf_title_173" defVal="发送情况" fileName="dxzs"/>：</b></td>
								  <td><%=sendinfo %>
								  </td>
							  </tr>
								<tr>
									<td colspan="2">
										<font class="zhu"><emp:message key="dxzs_xtnrqf_title_174" defVal="注：以上数据" fileName="dxzs"/><%=StaticValue.getCORPTYPE() ==0?sixHour:mday%><emp:message key="dxzs_xtnrqf_title_175" defVal="汇总一次,可能与下面的实时数据有一定误差。" fileName="dxzs"/></font>
									</td>
								</tr>
							</table>
						</div>
					<form name="pageForm" action="surlBatchRecord.htm?method=findSendInfoByCondition" method="post">
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
						      <td><emp:message key="dxzs_xtnrqf_title_178" defVal="运营商" fileName="dxzs"/>：</td>
						      <td>
						           <select  id="spisuncm" name="spisuncm">
										<option value=""><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
										<option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_21" defVal="移动" fileName="dxzs"/></option>
										<option value="1" <%="1".equals(spisuncm)?"selected":"" %> ><emp:message key="dxzs_xtnrqf_title_22" defVal="联通" fileName="dxzs"/></option>
										<option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_23" defVal="电信" fileName="dxzs"/></option>
										<%="5".equals(spisuncm)?"selected":"" %>>国外</option>
									</select>
						      </td>
						      <td><emp:message key="dxzs_xtnrqf_title_119" defVal="手机号码" fileName="dxzs"/>：</td>
						      <td>
						         <input type="text" style="width: 181px;" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="phoneInputCtrl($(this))"/>
						      </td>
						      <td><emp:message key="dxzs_xtnrqf_title_103" defVal="状态" fileName="dxzs"/>：</td>
						      <td>
						          <select  id="type" name="type">
										<option value="1" <%="1".equals(type)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_179" defVal="全部号码" fileName="dxzs"/></option>
										<option value="5" <%="5".equals(type)?"selected":"" %> >发送成功</option>
										<option value="6" <%="6".equals(type)?"selected":"" %> >状态未返</option>
										<option value="2" <%="2".equals(type)?"selected":"" %> >接收失败</option>
										<option value="3" <%="3".equals(type)?"selected":"" %>>提交失败</option>
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
										<emp:message key="dxzs_xtnrqf_title_182" defVal="序号" fileName="dxzs"/>
									</th>
									<th>
									          <emp:message key="dxzs_xtnrqf_title_178" defVal="运营商" fileName="dxzs"/>
									</th>
									<th>
										<emp:message key="dxzs_xtnrqf_title_119" defVal="手机号码" fileName="dxzs"/>
									</th>
									<th>
										<emp:message key="dxzs_xtnrqf_title_172" defVal="内容" fileName="dxzs"/>
									</th>
									<th>
										<emp:message key="dxzs_xtnrqf_title_183" defVal="分条" fileName="dxzs"/>
									</th>
									<th>
										<emp:message key="dxzs_xtnrqf_title_103" defVal="状态" fileName="dxzs"/>
									</th>
								</tr>
							</thead>
							<%
							if("0".equals(type))
							{
							%>
							<tr><td colspan="6" align="center"><emp:message key="dxzs_xtnrqf_title_164" defVal="请点击查询获取数据" fileName="dxzs"/></td></tr>
							<%
							}else if(mtList!= null && mtList.size() > 0)
								{
								for(int i=0;i<mtList.size();i++)
								{
									SendDetailMttaskVo vo = mtList.get(i);
							%>
							<tr>
								<td>
									<%=i+1 %>
								</td> 
								<td>
				  					<%
									if(vo.getUnicom()-0==0){%>
									<emp:message key="dxzs_xtnrqf_title_21" defVal="移动" fileName="dxzs"/>
									<%}else if(vo.getUnicom()-1==0){%>
									<emp:message key="dxzs_xtnrqf_title_22" defVal="联通" fileName="dxzs"/>
									<%}else if(vo.getUnicom()-21==0){ %>
									<emp:message key="dxzs_xtnrqf_title_23" defVal="电信" fileName="dxzs"/>
									<%} else if(vo.getUnicom()-5==0){%>
									<emp:message key="dxzs_xtnrqf_title_24" defVal="国外" fileName="dxzs"/>
									<%}%>
								</td>
								<td>
									<%
									   String phones = CV.replacePhoneNumber(btnMap,vo.getPhone());
									   out.print(phones);									   
									 %>
								</td>
								<td class="textalign">									
									<pre><%=(vo.getMessage()==null||"".equals(vo.getMessage()))?"":vo.getMessage()%></pre>
								</td>
								<td>
									<%=vo.getPknumber() %>/<%=vo.getPktotal() %>
								</td>
								<td class="textalign">
								<%if(vo.getErrorcode() != null){ %>
								    <%if(vo.getErrorcode().equals("DELIVRD")||vo.getErrorcode().trim().equals("0")){  %>
								         <label><emp:message key="dxzs_xtnrqf_title_184" defVal="发送成功" fileName="dxzs"/></label>  
								    <%}else if(vo.getErrorcode().trim().length()>0){%>
								         <label><emp:message key="dxzs_xtnrqf_title_185" defVal="失败" fileName="dxzs"/>[<%=vo.getErrorcode() %>]</label>
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
		   			var type=$.trim($("#type").val());
		   			var lgcorpcode =$("#lgcorpcode").val();
					var lguserid =$("#lguserid").val();
					var phone = $("#phone").val();
					var spisuncm = $("#spisuncm").val();
				 	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210'))) {
				   		<%
				   		if(mtList!=null && mtList.size()>0 ){
				   		%>  
				   		    var IsexportAll = true;
				   		    if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_301')))
				   		    {
				   		        IsexportAll = false;
				   		    }  
	   	
				   		<%		     
				   		  	if(pageInfo.getTotalRec()<=15000000){
				   		%>	
					   		   $.ajax({
									type: "GET",
									url: "<%=path%>/surlBatchRecord.htm?method=sendDetailReportExcel",
									data: {
                                        	taskId:taskId,
											type:type,
											lgcorpcode:lgcorpcode,
											lguserid:lguserid,
											spisuncm:spisuncm,
											phone:phone,
											IsexportAll:IsexportAll
										  },
					                beforeSend:function () {
										page_loading();
					                },
					                complete:function () {
								    	page_complete();
					                },
									success: function(result){
					                        if(result=='true'){
					                           download_href("<%=path%>/surlBatchRecord.htm?method=downloadFile&type=BatchSendDetail");
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
