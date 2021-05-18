<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.common.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.shorturl.report.vo.LinkDetailVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="com.montnets.emp.shorturl.report.biz.Surl_VisitDetailReportBiz"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
	if(null == pageInfo){
		pageInfo = new PageInfo();
	}
	
	@ SuppressWarnings("unchecked")
	List<LinkDetailVo> linkVisitStatics = (List<LinkDetailVo>)request.getAttribute("linkVisitStatics");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String titlePath = (String)request.getAttribute("titlePath");
	titlePath = "/surlLinkVisit";
	String actionPath = (String)request.getAttribute("actionPath");
	String menuCode = titleMap.get(titlePath);
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String url = request.getParameter("url");
	
	String phone = request.getParameter("phone");
	String visitStatus = request.getParameter("visitStatus");
	String visitIP = request.getParameter("visitIP");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = false;
	
	String skin = session.getAttribute("stlyeSkin")==null?
	"default":(String)session.getAttribute("stlyeSkin");
	
	String httpUrl = StaticValue.getFileServerViewurl();
    String flowId = request.getParameter("flowId");
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
    String pserch = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_144", request);
    String empSd = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_156", request);
    String httpSd = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_157", request);
    String intime = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_166", request);
    String lgoff = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
    
    String dsp = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_149", request);
    String spbtg = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_150", request);
    String ydj = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_154", request);
    String ycx = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_152", request);
    String cswfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_155", request);
    String yfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_153", request);
    String dfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_151", request);
    String xjwj = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_205", request);
    String cs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_223", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<title>sendSMS.html</title>
<%@include file="/common/common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link
	href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet"
	href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>"
	type="text/css">
<link
	href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>"
	rel="stylesheet" type="text/css" />
<%
	if(StaticValue.ZH_HK.equals(langName)){
%>
<link rel="stylesheet" type="text/css"
	href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet" type="text/css"
	href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
<%
	}
%>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
<%--<link href="<%=inheritPath %>/styles/easyui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>


<style type="text/css">
body div#tooltip {
	position: absolute;
	z-index: 1000;
	max-width: 435px;
	_width: expression(this.scrollWidth > 435 ? "435px" : "auto");
	width: auto;
	background: #A8CFF6;
	border: #FEFFD4 solid 1px;
	text-align: left;
	padding: 6px;
}

body div#tooltip p {
	margin: 0;
	padding: 6;
	color: #FFFFFE;
	font: 12px verdana, arial, sans-serif;
}

body div#tooltip p em {
	display: block;
	margin-top: 3px;
	color: #f60;
	font-style: normal;
	font-weight: bold;
}

#content tr.peachpuff,#content tr.peachpuff td {
	background: #FFDAB9;
}
</style>
</head>

<body>
	<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode)%>

		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl%>">
			<input type="hidden" name="ISCLUSTER" id="isCluster"
				value="<%=StaticValue.getISCLUSTER()%>"> <input type="hidden"
                                                                name="htmName" id="htmName" value="<%=actionPath%>">
			<form name="pageForm" action="<%=actionPath%>" method="post" id="pageForm">
			<input type="hidden" name="url" id="url" value="<%=url %>">
				<div class="buttons">
					<div id="toggleDiv"></div>
					<%
						if(flowId == null){
					%>
					<a id="exportCondition"><emp:message
							key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs" /></a>
					<%
						}
					%>
					<a id="changedep" onclick="javascript:goback();"><span>返回</span></a>
				</div>
				<div id="condition">
					<table>

						<tbody>
							<tr>
								<td>手机号码：</td>
								<td><input type="text" id="phone" name="phone"
									style="width: 180px"
									onkeyup="javascript:numberControl($(this))"
									value="<%=(phone != null && !"".equals(phone))?phone:""%>"
									maxlength="19"></td>
								<td>访问状态：</td>
								<td><select name="visitStatus" id="visitStatus"
									style="width: 180px;">
										<option value="">全部</option>
										<option value="1" <%="1".equals(visitStatus)?"selected":""%>>已访问</option>
										<option value="0" <%="0".equals(visitStatus)?"selected":""%>>未访问</option>
								</select></td>
								<td>访问IP：</td>
								<td>								
								<input type="text" id="visitIP" name="visitIP"
									style="width: 180px"
									value="<%=(visitIP != null && !"".equals(visitIP))?visitIP:""%>"
									maxlength="25"></td>
								<td class="tdSer">
									<center>
										<a id="search"></a>
									</center>
								</td>
							</tr>
							<tr>

								<td>发送时间：</td>
								<td><input type="text"
									style="cursor: pointer; width: 181px; background-color: white;"
									readonly="readonly" class="Wdate" onclick="stime()"
									value="<%=(startTime!= null&&!"".equals(startTime))?startTime:""%>"
									id="startTime" name="startTime"<%--onchange="onblourSendTime('end')" --%>>
								</td>
								<td><emp:message key="dxzs_xtnrqf_title_135" defVal="至"
										fileName="dxzs" />：</td>
								<td><input type="text"
									style="cursor: pointer; width: 181px; background-color: white;"
									readonly="readonly" class="Wdate" onclick="rtime()"
									value="<%=(endTime!=null&&!"".equals(endTime))?endTime:""%>"
									id="endTime" name="endTime"<%--onchange="onblourSendTime('end')" --%>>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<table id="content">
					<thead>
						<tr>
							<th>手机号码</th>
							<th>区域</th>
							<th>访问状态</th>
							<th>访问次数</th>
							<th>末次访问时间</th>
							<th>末次访问IP</th>
							<th>任务批次</th>
							<th>发送主题</th>
							<th>发送时间</th>
						</tr>
					</thead>
					<tbody>
						<%
							if(isFirstEnter){
						%>
						<tr>
							<td colspan="9" align="center"><emp:message
									key="dxzs_xtnrqf_title_164" defVal="请点击查询获取数据" fileName="dxzs" /></td>
						</tr>
						<%
							} else if(linkVisitStatics != null && linkVisitStatics.size()>0)
											{
												for(LinkDetailVo visitStatics : linkVisitStatics)
												{
						%>
						<tr>
							<td><%=visitStatics.getPhone()%></td>
							<td>****</td>
							<td><%=(visitStatics.getVisitCount()!=null&&visitStatics.getVisitCount()>0)?"已访问":"未访问"%></td>
							<td><%=visitStatics.getVisitCount()!=null?visitStatics.getVisitCount():0%></td>
							<td><%=visitStatics.getLastVisitTime()!=null?sdf.format(visitStatics.getLastVisitTime()):"-"%></td>
							<td><%=visitStatics.getLastIP()!=null?visitStatics.getLastIP():"-"%></td>
							<td><%=visitStatics.getTaskId()!=null?visitStatics.getTaskId():"-"%></td>
							<td><%=visitStatics.getTitle()!=null?visitStatics.getTitle():"-"%></td>
							<td><%=visitStatics.getSendTime()!=null?sdf.format(visitStatics.getSendTime()):"-"%></td>
						</tr>
						<%
							}
											}else{
						%>
						<tr>
							<td align="center" colspan="9"><emp:message
									key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs" /></td>
						</tr>
						<%
							}
						%>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="9">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</form>
		</div>

		<%-- 内容结束 --%>

		<%-- foot开始 --%>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
				<div id="bottom_main"></div>
			</div>
		</div>
	</div>
	<div class="clear"></div>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script
		src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
		$(document).ready(function(){
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		Date.prototype.format = function(fmt) { 
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
     for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
     }
    return fmt; 
};
			<%
			if(!(endTime!= null && !"".equals(endTime))){
			out.print("$(\"#startTime\").val( new Date().format(\"yyyy-MM\"+\"-01 00:00:00\"));");
			}
			
			if(!(endTime!= null && !"".equals(endTime))){
			out.print("$(\"#endTime\").val( new Date().format(\"yyyy-MM-dd hh:mm:ss\"));");
			}
			%>
			
		$('#search').click(function(){
			
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if("" == $.trim(startTime) || "" == $.trim(endTime) )
			{
				alert("请选择开始时间和结束时间！");
			}else
			{
				submitForm();//选择好时间段，才允许查询 
			}
			
			});
			
		});
		
	function stime(){
	       var max = "2099-12-31 23:59:59";
		    var v = $("#endTime").attr("value");
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
				min = year+"-"+mon+"-01 00:00:00";
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max,enableInputMask:false});
		
		};
	
	function rtime()
	{
	    var max = "2099-12-31 23:59:59";
	    var v = $("#startTime").attr("value");
		if(v.length != 0)
		{
		    var year = v.substring(0,4);
			var mon = v.substring(5,7);
			var day = 31;
			if (mon != "12")
			{
			    mon = String(parseInt(mon,10)+1);
			    if (mon.length == 1)
			    {
			        mon = "0"+mon;
			    }
		    switch(mon){
			    case "01":day = 31;break;
			    case "02":day = 28;break;
			    case "03":day = 31;break;
			    case "04":day = 30;break;
			    case "05":day = 31;break;
			    case "06":day = 30;break;
			    case "07":day = 31;break;
			    case "08":day = 31;break;
			    case "09":day = 30;break;
			    case "10":day = 31;break;
			    case "11":day = 30;break;
			    }
			}
			else
			{
			    year = String((parseInt(year,10)+1));
			    mon = "01";
			}
			max = year+"-"+mon+"-"+day+" 23:59:59";
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max,enableInputMask:false});
	
	}

    function goback(){
	    //设置参数
		var surl_taskId = getCookie("surl_taskId");
        var surl_lurl = getCookie("surl_lurl");
        var surl_subject= getCookie("surl_subject");
        var surl_startTime = getCookie("surl_startTime");
        var surl_endTime = getCookie("surl_endTime");
        var surl_areaZone = getCookie("surl_areaZone");
        delCookie("surl_taskId");
        delCookie("surl_lurl");
        delCookie("surl_subject");
        delCookie("surl_startTime");
        delCookie("surl_endTime");
        delCookie("surl_areaZone");
        surl_taskId==null?"":surl_taskId;
        surl_lurl==null?"":surl_lurl;
        surl_subject==null?"":surl_subject;
        surl_startTime==null?"":surl_startTime;
        surl_endTime==null?"":surl_endTime;
        surl_areaZone==null?"":surl_areaZone;
	    //增加所有的回显条件,修改人moll
		window.location.href = "<%=commonPath%>/surlLinkVisit.htm?taskId="+surl_taskId+"&lurl="+surl_lurl+"&subject="+surl_subject+
        "&startTime="+surl_startTime+"&endTime="+surl_endTime+"&areaZone="+surl_areaZone;
		}


        function getCookie(c_name)
        {
            if (document.cookie.length>0)
            {
                c_start=document.cookie.indexOf(c_name + "=")
                if (c_start!=-1)
                {
                    c_start=c_start + c_name.length+1
                    c_end=document.cookie.indexOf(";",c_start)
                    if (c_end==-1) c_end=document.cookie.length
                    return unescape(document.cookie.substring(c_start,c_end))
                }
            }
            return ""
        }

        //删除cookies
        function delCookie(name)
        {
            var exp = new Date();
            exp.setTime(exp.getTime() - 1);
            var cval=getCookie(name);
            if(cval!=null)
                document.cookie= name + "="+cval+";expires="+exp.toGMTString();
        }
		
		
		
		
		
		
		
		
		
		
		
		
		
		//导出全部数据到excel
			 $("#exportCondition").click(
			function() {
				if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210'))) {
			   	    var phone = '<%=(phone != null && !"".equals(phone))?phone:""%>';
			   	    var visitStatus = '<%=(visitStatus != null && !"".equals(visitStatus))?visitStatus:""%>';
					 var visitIP='';	
					 var startTime='<%=(startTime!= null&&!"".equals(startTime))?startTime:""%>';
					 var endTime='<%=(endTime!=null&&!"".equals(endTime))?endTime:""%>';
					 var url = '<%=url %>';
			   		<%
			   		if(linkVisitStatics!=null && linkVisitStatics.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>	
		   				$.ajax({
								type: "POST",
								url: "<%=path%>/<%=actionPath%>?method=exportAllReport",
								data: {
								    	phone:phone,
										visitStatus:visitStatus,
										visitIP:visitIP,
										startTime:startTime,
										endTime:endTime,
										url:url
									  },
				                beforeSend:function () {
									page_loading();
				                },
				                complete:function () {
							    	page_complete();
				                },
								success: function(result){
				                        if(result==='true'){
				                           download_href("<%=path%>/<%=actionPath%>?method=downloadFile");
				                        }else{
				                            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
				                        }
					   			}
							});	
			   		<%	
			   		}else{%>
			   		    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_272'));
			   		<%}
			   		}else{
			   		%>
			   		    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_212'));
			   		<%
			   		}%>
				}				 
			}); 
	</script>
</body>
</html>
