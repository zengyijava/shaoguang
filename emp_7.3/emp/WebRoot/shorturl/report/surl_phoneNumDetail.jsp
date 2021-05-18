<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sms.LfMttask" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.shorturl.report.entity.VstDetail" %>
<%@ page import="com.montnets.emp.shorturl.report.vo.VstDetailVo" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String actionPath = (String)request.getAttribute("actionPath");
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	PageInfo prePageInfo = (PageInfo)session.getAttribute("prePageInfo");

	PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");

	String corpCode = (String) request.getAttribute("corpCode");

	@ SuppressWarnings("unchecked")
	List<VstDetailVo> vstDetailVoList =(List<VstDetailVo>) request.getAttribute("vstDetailVoList");

	Integer visitorCount =(Integer) request.getAttribute("visitorCount");
	visitorCount = visitorCount == null ? 0 : visitorCount;

	Integer visitCount =(Integer) request.getAttribute("visitCount");
	visitCount = visitCount == null ? 0 : visitCount;

	LfMttask lfMttask =(LfMttask) request.getAttribute("lfMttask");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

    String titlePath = "/surlBatchVisit";
	String menuCode = titleMap.get(titlePath);
	menuCode=menuCode==null?"0-0-0":menuCode;

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	/*数据回显*/
	String phone = request.getAttribute("phone") == null ? "":(String)request.getAttribute("phone");
	String visitStatus =  request.getAttribute("visitStatus")== null ? "":(String)request.getAttribute("visitStatus");
	String visitIP = request.getAttribute("visitIP")==null ? "":(String) request.getAttribute("visitIP");
	String visitArea = request.getAttribute("visitArea")==null?"":(String) request.getAttribute("visitArea");
	String sendtime = request.getAttribute("sendtime")==null?"":(String) request.getAttribute("sendtime");
	String recvtime = request.getAttribute("recvtime")==null?"":(String) request.getAttribute("recvtime");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
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
            <%=ViewParams.getPosition(langName, menuCode) %>
            <%--<div class="top">
                <div id="top_right">
                    <div id="top_left"></div>
                    <div id="top_main">
                        <emp:message key="ydwx_add_jsp_1" defVal="当前位置：" fileName="ydwx"/>[<emp:message key="ydwx_common_menuTitle_1" defVal="网讯查询统计" fileName="ydwx"/>]-[<emp:message key="ydwx_common_menuTitle_2" defVal="网讯发送统计" fileName="ydwx"/>]
                    </div>
                </div>

			</div>--%>

            <%--<div class="abs_right">
					<span class="titletop_font"  onclick="showback(<%=path%>)">&larr;&nbsp;
					<emp:message key="ydwx_add_jsp_3" defVal="返回上一级" fileName="ydwx"/></span>
            </div>--%>

			<div class="rContent">
						<%              		
							if(btnMap.get(menuCode+"-0")!=null)                       		
							{                        	
						%>
						<%--<div class="titletop" style="padding-left: 0;padding-top: 0">
						<table class="titletop_table" style="width: 100%">
								<tr>
								    <td class="titletop_td">
										号码详情查看
						        	</td>
									<td align="right">
										<span class="titletop_font"   onclick="showback()">&larr;&nbsp;
										<emp:message key="ydwx_add_jsp_3" defVal="返回上一级" fileName="ydwx"/></span>
									</td>
								</tr>
							</table>
					   </div>--%>
						<div id="u_o_c_explain" class="div_bg">
							<h2>
								基本信息：
							</h2>
                            <table id="baseInfo">
								<tr>
									<td><b>发送主题：</b></td>
									<td><%=lfMttask.getTitle() == null ? "":(lfMttask.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;")) %></td>
								</tr>

								<tr>
									<td><b>发送内容：</b></td>
									<td style="word-wrap: break-word;word-break:break-all;"><%=lfMttask.getMsg() == null ? "":lfMttask.getMsg().replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("#[pP]_(\\d+)#",MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request)) %></td>
								</tr>

								<tr>
									<td><b>任务批次：</b></td>
									<td><%=lfMttask.getTaskId() == null ? "":lfMttask.getTaskId()%></td>
								</tr>

								<tr>
									<td><b>发送时间：</b></td>
									<td><%=lfMttask.getTimerTime() == null ? "":sdf.format(lfMttask.getTimerTime())%></td>
								</tr>

								<tr>
									<td><b>访问情况：</b></td>
									<td>
										发送号码数为<%=lfMttask.getEffCount() == null ? 0 : lfMttask.getEffCount()%>条，其中访问人数为<%=visitorCount%>人,访问次数为<%=visitCount%>次
									</td>
								</tr>

                            </table>
						</div>
						<form name="pageForm" action="<%=actionPath%>?method=findPhoneNumDetailByCondition" method="post">
							<input type="hidden" id="taskId" name="taskId" value="<%=lfMttask.getTaskId()%>"/>
							<input type="hidden" id="corpCode" name="corpCode" value="<%=corpCode%>"/>
							<div class="buttons" style="padding-top: 10px;">
							<div id="toggleDiv">
							</div>
							<a id="exportCondition"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'/></a>

							<%--<span id="backgo" class="right mr5" onclick="showback('<%=path%>')"><emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'/></span>--%>
                            <a id="changedep" onclick="showback('<%=path%>')"><span>返回</span></a>
							</div>
							<div id="condition">
							 <table>
								<tr>
									<td>手机号码：</td>
									<td>
										<input type="text" style="width: 181px;" value="<%=phone%>" id="phone" name="phone" maxlength="11" onkeyup="numberControl($(this))"/>
									</td>
									<td>访问状态：</td>
									<td>
										<select id="visitStatus" name="visitStatus" isInput="false">
											<option value="" selected>全部</option>
											<option value="1" <%="1".equals(visitStatus)?"selected":""%>>已访问</option>
											<option value="0" <%="0".equals(visitStatus)?"selected":""%>>未访问</option>
										</select>
									</td>
									<td>区域：</td>
									<td>
										<select  id="visitArea" name="visitArea" isInput="false">
											<option value="" selected>全部</option>
											<%--<option value="0" <%="0".equals(visitArea)?"selected":""%>>北京</option>--%>
											<%--<option value="1" <%="1".equals(visitArea)?"selected":""%>>上海</option>--%>
											<%--<option value="21" <%="21".equals(visitArea)?"selected":""%>>广州</option>--%>
											<%--<option value="5" <%="15".equals(visitArea)?"selected":""%>>深圳</option>--%>
										</select>
									</td>
								  <td class="tdSer">
									<center><a id="search"></a></center>
								  </td>
								</tr>
								 <tr>
									 <td>访问IP：</td>
									 <td>
										 <input type="text" style="width: 181px;" value="<%=visitIP%>" id="visitIP" name="visitIP"/>
									 </td>
									 <td>访问时间：</td>
									 <td>
										 <input type="text" style="cursor: pointer; width: 181px; background-color: white;" readonly="readonly" class="Wdate" onclick="stime()" value="<%=sendtime%>"  id="sendtime" name="sendtime"
										 <%--onchange="onblourSendTime('end')" --%>>
									 </td>
									 <td>至：</td>
									 <td>
										 <input type="text" style="cursor: pointer; width: 181px; background-color: white;" readonly="readonly" class="Wdate"  onclick="rtime()" value="<%=recvtime%>" id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
									 </td>
									 <td>&nbsp;</td>
									 <td>&nbsp;</td>
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
											区域
										</th>
										<th>
											访问状态
										</th>
										<th>
											访问时间
										</th>
										<th>
											访问IP
										</th>
									</tr>
								</thead>
								<%if(vstDetailVoList!= null && vstDetailVoList.size() > 0)
									{
									for(VstDetailVo detail:vstDetailVoList)
									{
								%>
								<tr>
									<td>
										<%=detail.getPhone()%><%--手机号码--%>
									</td>
									<td>
										<%=detail.getAreaName()== null||"".equals(detail.getAreaName())?"***":detail.getAreaName()%><%--区域--%>
									</td>
									<td>
										<%="1".equals(detail.getVisitStatus()) ? "已访问":"未访问"%><%--访问状态--%>
									</td>
									<td>
										<%=detail.getVsttm() == null || "".equals(detail.getVsttm()) ?"-":detail.getVsttm().substring(0,detail.getVsttm().lastIndexOf("."))%><%--访问时间--%>
									</td>
									<td>
										<%=detail.getVisitIP() == null || "".equals(detail.getVisitIP())? "-":detail.getVisitIP()%><%--访问IP--%>
									</td>
								</tr>
								<%
									}
									}
									else
									{
								%>
								<tr><td colspan="6" align="center"><emp:message key="common_norecord" defVal="无记录" fileName="common"/></td></tr>
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
								<emp:message key="ydwx_add_jsp_22" defVal="请耐心等待....." fileName="ydwx"/>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/report/js/surl_phoneNumDetail.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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
            initPage('<%=pageInfo.getTotalPage()%>','<%=pageInfo.getPageIndex()%>','<%=pageInfo.getPageSize()%>','<%=pageInfo.getTotalRec()%>');
		    $("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});	

			//导出全部数据到excel
			$("#exportCondition").click(
			function() {
	   			var taskId = $.trim($("#taskId").val());
	   			var visitStatus = $.trim($("#visitStatus").val());
                var visitArea = $.trim($("#visitArea").val());
                var visitIP = $.trim($("#visitIP").val());
                var sendtime = $.trim($("#sendtime").val());
                var recvtime = $.trim($("#recvtime").val());
                var timerTime = '<%=lfMttask.getTimerTime()%>';
				var phone = $.trim($("#phone").val());
			 	if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd"))) {
			   		<%
			   		if(vstDetailVoList!=null && vstDetailVoList.size()>0 ){
			   		%>
			   		    var isExportAll = true;
			   		    if(confirm(getJsLocaleMessage("ydwx","ydwx_wxfstj_31"))) {
                            isExportAll = false;
			   		    }
			   		<%
			   		  	if(pageInfo.getTotalRec()<=500000){
			   		%>
							$.ajax({
								type: "POST",
								url: "surlBatchVisit.htm?method=batchVisitDetailReportExcel",
								data: {
								taskId:taskId,
								phone:phone,
								visitStatus:visitStatus,
								visitArea:visitArea,
								visitIP:visitIP,
								sendtime:sendtime,
								recvtime:recvtime,
								timerTime:timerTime,
								isExportAll:isExportAll
								},
								beforeSend: function(){
									page_loading();
								},
				                complete:function () {
									page_complete();
				                },
								success: function(result){
									if(result==='true'){
                                        //复用批次发送统计的下载方法
                                        download_href("<%=path%>/surlBatchRecord.htm?method=downloadFile&type=BatchVisitDetail");
				                    }else{
				                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
				                    }
					   			}
							});


				   		//	location.href="<%=path%>/wx_wmsTaskRecord.htm?method=smsReportAllExcel&mtid="+mtid+"&type="+type+"&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&spisuncm="+spisuncm+"&phone="+phone+"&IsexportAll="+IsexportAll;

				   		<%}else{
				   		%>
				   		     alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_3"));
				   		<%}
			   		}else{
			   		%>
			   			alert(getJsLocaleMessage("ydwx","ydwx_common_wshjdch"));
			   		<%
			   		}
			   		%>
			   	}
			});

		});

		//关闭加载窗口的方法		
		function closelog(){
		    var lguserid =$("#lguserid").val();
            $.post("<%=path%>/wx_wmsTaskRecord.htm?method=checkResult",{lguserid:lguserid},function(result){
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
