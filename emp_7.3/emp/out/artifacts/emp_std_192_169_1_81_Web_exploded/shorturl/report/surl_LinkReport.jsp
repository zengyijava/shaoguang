<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.cxf.wsdl.http.UrlEncoded"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.shorturl.report.vo.LinkReportVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy年MM月dd日");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	List<LinkReportVo> linkVisitStatics = (List<LinkReportVo>)request.getAttribute("linkVisitStatics");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	
	String actionPath = (String)request.getAttribute("actionPath");
//	String titlePath = (String)request.getAttribute("titlePath");
    String titlePath = "/surlLinkVisit";
	String menuCode = titleMap.get(titlePath);
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	
	String lurl = request.getParameter("lurl");
	String subject = request.getParameter("subject");
	String taskId = request.getParameter("taskId");
	String areaZone = request.getParameter("areaZone");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	String recordType = request.getParameter("recordType");
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = false;
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?
			"default":(String)session.getAttribute("stlyeSkin");
			
	String httpUrl = StaticValue.getFileServerViewurl();
	String taskid = (String)request.getParameter("taskid");
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
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	    <%--<link href="<%=inheritPath %>/styles/easyui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>
				
		
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto"); width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
			#content tr.peachpuff,#content tr.peachpuff td{background:#FFDAB9;}
			.length-limit{
			max-width: 560px;
			}
		</style>
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<input type="hidden" name="htmName" id="htmName" value="<%=actionPath %>">
			<form name="pageForm" action="<%=actionPath%>" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
                        <%if(flowId == null){%>
                            <a id="exportCondition"><emp:message key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs"/></a>
                        <%}%>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									<td>
										长链接：
									</td>									
									<td>
								      <input type="text" id="lurl" name ="lurl" style="width: 180px" value="<%= (lurl != null && !"".equals(lurl))?lurl:""%>" maxlength="250">		
								    </td>
									<td>
										发送主题：
									</td>
									<td>
								      <input type="text" id="subject" name ="subject" style="width: 180px" value="<%= (subject != null && !"".equals(subject))?subject:""%>" maxlength="100">		
								    </td>
									<td>任务批次：</td>
									  <td>
								      <input type="text" id="taskId" name ="taskId" style="width: 180px" onkeyup="javascript:numberControl($(this))" value="<%= (taskId != null && !"".equals(taskId))?taskId:""%>" maxlength="19">		
								    </td>
									<td class="tdSer">
									       <center><a id="search" ></a></center>
								    </td>		
								</tr>
								<tr>
								
								    <%-- <td>记录类型：</td>
								    <td>
								    	<select name="recordType" id="recordType" style="width: 180px;">
								    		<option value="realTime" <%="realTime".equals(recordType)?"selected":"" %>>实时记录</option>
								    		<option value="history" <%="history".equals(recordType)?"selected":"" %>>历史记录</option>
								    	</select>
								    </td> --%>
								    <td>区域：</td>
								    <td>
								    	<select name="areaZone" id="areaZone" style="width: 180px">
								    		<option value="0">全部</option>
								    		<%-- <option value="3" <%="3".equals(areaZone)?"selected":"" %>>北京</option>
								    		<option value="4" <%="4".equals(areaZone)?"selected":"" %>>上海</option>
								    		<option value="5" <%="5".equals(areaZone)?"selected":"" %>>广东</option> --%>
								    	</select>
								    </td>
								     <td>
								      发送时间：
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= startTime== null?"":startTime %>"  id="startTime" name="startTime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>：</td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=endTime==null?"":endTime %>" 
											 id="endTime" name="endTime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
								    
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<%-- <th>
									访问时间
								</th> --%>
								<th>
									长链接
								</th>
								<%-- <th>
									提交号码数
								</th> --%>
								<th>
									号码个数
								</th>
								<th>
									访问人数
								</th>
								<th>
									访问次数
								</th>
								<th>
									号码详情
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="6" align="center"><emp:message key="dxzs_xtnrqf_title_164" defVal="请点击查询获取数据" fileName="dxzs"/></td></tr>
							<%
							} else if(linkVisitStatics != null && linkVisitStatics.size()>0)
							{
								for(LinkReportVo visitStatics : linkVisitStatics)
								{
								%>
								<tr>
									<%-- <td class="textalign">2013年9月3日至2013年10月5日</td> --%>
									<td class="textalign length-limit"><%=visitStatics.getUrl() %></td>
									<%-- <td><%=visitStatics.getTotalNum() %></td> --%>
									<td><%=visitStatics.getEffCount()==null?0:visitStatics.getEffCount() %></td>
									<td><%=visitStatics.getVisitCount()==null?0:visitStatics.getVisitCount() %></td>
									<td><%=visitStatics.getVisitNum()==null?0:visitStatics.getVisitNum() %></td>
									<td><a onclick="javascript:goDetail('<%=URLEncoder.encode(visitStatics.getUrl(), "UTF-8")%>')">查看</a></td>
									<%-- <td class="textalign"><a href="<%=iPath %>/surl_LinkDetail.jsp?url=<%=URLEncoder.encode(visitStatics.getUrl(), "UTF-8")%>">查看</a></td> --%>
									<%-- <td class="textalign"><button onclick="javascript:goVisitDetailReport('<%=commonPath%>/surlVisitDetail.htm','5600-1700','访问明细详情')" >查看</button></td> --%>
								</tr>
								<%
								}
							}else{
						%>
						<tr><td align="center" colspan="6"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="6">
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
					<div id="bottom_main">
					</div>
				</div>
				</div>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function(){

		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		
		$('#lurl').unbind('keyup blur');
	
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
		function goVisitDetailReport(url,menucode,menuname){
			window.parent.addTabMenu(url, menucode, menuname);
			$("#ak" + menucode).addClass("higehLisght");
		}
		
		function goDetail(url){
            //设置cookie
	        setCookie("surl_taskId",$("#taskId").val());
            setCookie("surl_startTime",$("#startTime").val());
            setCookie("surl_endTime",$("#endTime").val());
            setCookie("surl_subject",$("#subject").val());
            setCookie("surl_lurl",$("#lurl").val());
            setCookie("surl_areaZone",$("#areaZone").val());

	    	//增加任务批次,开始日期,结束日期条件,修改人moll
           	window.location.href = "<%=commonPath %>/surlLinkDetail.htm?url="+url+"&taskId="+$("#taskId").val()+"&startTime="+$("#startTime").val()+"&endTime="+$("#endTime").val()+
				"&subject="+$("#subject").val()+"&lurl="+$("#lurl").val();
       	}

        function setCookie(name,value)
        {
            var Days = 30;
            var exp = new Date();
            exp.setTime(exp.getTime() + Days*24*60*60*1000);
            document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
        }


		
		//导出全部数据到excel
			 $("#exportCondition").click(
			function() {
				if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210'))) {
			   	    var lurl = '<%= (lurl != null && !"".equals(lurl))?lurl:""%>';
			   	    var subject = '<%= (subject != null && !"".equals(subject))?subject:""%>';
					 var taskId='<%= (taskId != null && !"".equals(taskId))?taskId:""%>';	
					 var recordType='<%="history".equals(recordType)?"history":"realTime" %>';
					 var startTime='<%= startTime== null?"":startTime %>';
					 var endTime='<%=endTime==null?"":endTime %>';
			   		<%
			   		if(linkVisitStatics!=null && linkVisitStatics.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>	
		   				$.ajax({
								type: "POST",
								url: "<%=path%>/<%=actionPath%>?method=exportAllReport",
								data: {
								    	lurl:lurl,
										subject:subject,
										taskId:taskId,
										recordType:recordType,
										startTime:startTime,
										endTime:endTime
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
