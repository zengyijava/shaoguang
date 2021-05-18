<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.common.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.shorturl.report.vo.VisitReportVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
	if(null == pageInfo){
		pageInfo = new PageInfo();
	}

	@ SuppressWarnings("unchecked")
	List<VisitReportVo> visitReportVoList = (List<VisitReportVo>)request.getAttribute("visitReportVoList");
    boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
//	String titlePath = (String)request.getAttribute("titlePath");
    String titlePath = "/surlVisitDetail";
	String actionPath = (String)request.getAttribute("actionPath");
	String menuCode = titleMap.get(titlePath);
	menuCode = menuCode==null?"0-0-0":menuCode;

	String phone = request.getParameter("phone");
	String subject = request.getParameter("subject");
	String taskId = request.getParameter("taskId");
	String visitStatus = request.getParameter("visitStatus");
	String visitCount = request.getParameter("visitCount");
	String unicom = request.getParameter("unicom");
	String areaZone = request.getParameter("areaZone");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");

	String recvtime = request.getParameter("recvtime");
	String sendtime = request.getParameter("sendtime");
	String deptid = request.getParameter("depid");
	String busCode = request.getParameter("busCode");
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");
	String depNam = request.getParameter("depNam");
	String spUser = request.getParameter("spUser");
	String taskState= request.getParameter("taskState");
	taskState = "4";
	if(taskState==null || "".equals(taskState))
	{
		taskState="0";
	}

	String taskType= request.getParameter("taskType");
	if(taskType==null || "".equals(taskType))
	{
		taskType="0";
	}

	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}

	//任务批次
	String taskID= request.getParameter("taskID");
	if(taskID==null||"".equals(taskID.trim())){
		taskID="";
	}else{
		try{
			Long.parseLong(taskID.trim());
			taskID=taskID.trim();
		}catch(Exception e){
			EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + taskID);
			taskID="";
		}
	}

	//信息内容
	String msg=request.getParameter("msg");
	//对msg的处理
	if(msg==null||"".equals(msg.trim()))
	{
		msg="";
	}

	int corpType = StaticValue.getCORPTYPE();
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");

	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?
			"default":(String)session.getAttribute("stlyeSkin");

	String httpUrl = StaticValue.getFileServerViewurl();
	String taskid = (String)request.getParameter("taskid");
    String flowId = request.getParameter("flowId");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		</style>
	</head>

	<body id="surl_VisitReport">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>

			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<input type="hidden" name="htmName" id="htmName" value="<%=actionPath %>">
            <input type="hidden" name="tips" id="tips" value="<%=null==request.getAttribute("tips")?0:request.getAttribute("tips")%>"/>
			<form name="pageForm" action="<%=actionPath%>" method="post" id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
					<div id="condition">
						<table>

							<tbody>
								<tr>
									<td>
										手机号码：
									</td>
									<td>
								      <input type="text" id="phone" name ="phone" style="width: 181px" onkeyup="javascript:numberControl($(this))" value="<%= (phone != null && !"".equals(phone))?phone:""%>" maxlength="19">
								    </td>
									<td>
										发送主题：
									</td>
									<td>
								      <input type="text" id="subject" name ="subject" style="width: 181px" value="<%= (subject != null && !"".equals(subject))?subject:""%>" maxlength="19">
								    </td>
									<td>任务批次：</td>
									  <td>
								      <input type="text" id="taskId" name ="taskId" style="width: 181px" onkeyup="javascript:numberControl($(this))" value="<%= (taskId != null && !"".equals(taskId))?taskId:""%>" maxlength="19">
								    </td>
									<td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>
								</tr>
								<tr>
									 <%--<td>访问状态：</td>--%>
								    <%--<td>--%>
								    	<%--<select name="visitStatus" id="visitStatus" style="width: 181px" isInput="false">--%>
								    		<%--<option value="">全部</option>--%>
								    		<%--<option value="1" <%="1".equals(visitStatus)?"selected":"" %>>已访问</option>--%>
								    		<%--<option value="0" <%="0".equals(visitStatus)?"selected":"" %>>未访问</option>--%>
								    	<%--</select>--%>
								    <%--</td>--%>
								    <td>
										访问次数：
									</td>
									<td>
								      <input type="text" id="visitCount" name ="visitCount" style="width: 180px" onkeyup="javascript:numberControl($(this))" value="<%= (visitCount != null && !"".equals(visitCount))?visitCount:""%>" maxlength="19">
								    </td>
									 <td>运营商：</td>
								    <td>
								    	<select name="unicom" id="unicom" style="width: 181px" isInput="false">
								    		<option value="">全部</option>
								    		<option value="0" <%="0".equals(unicom)?"selected":"" %>>移动</option>
								    		<option value="1" <%="1".equals(unicom)?"selected":"" %>>联通</option>
								    		<option value="21" <%="21".equals(unicom)?"selected":"" %>>电信</option>
								    		<option value="5" <%="5".equals(unicom)?"selected":"" %>>国外</option>
								    	</select>
								    </td>
									 <td>区域：</td>
									 <td>
										 <select name="areaZone" id="areaZone" style="width: 181px">
											 <option value="">全部</option>
											 <%-- <option value="3" <%="3".equals(areaZone)?"selected":"" %>>北京</option>
											 <option value="4" <%="4".equals(areaZone)?"selected":"" %>>上海</option>
											 <option value="5" <%="5".equals(areaZone)?"selected":"" %>>广东</option> --%>
										 </select>
									 </td>
								</tr>
								<tr>
								     <td>
								      发送时间：
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()"
											value="<%= (startTime!= null&&!"".equals(startTime))?startTime:"" %>"  id="startTime" name="startTime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>：</td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=(endTime!=null&&!"".equals(endTime))?endTime:"" %>"
											 id="endTime" name="endTime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>

								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									发送主题
								</th>
								<th>
									任务批次
								</th>
								<th>
									发送内容
								</th>
								<th>
									运营商
								</th>
								<th>
									手机号码
								</th>
								<th>
									区域
								</th>
								<th>
									接收状态
								</th>
								<%--<th>--%>
									<%--访问状态--%>
								<%--</th>--%>
								<th>
									访问次数
								</th>
								<th>
									末次访问IP
								</th>
								<th>
									末次访问时间
								</th>
								<th>
									发送时间
								</th>
								<th>
									详情
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="13" align="center"><emp:message key="dxzs_xtnrqf_title_164" defVal="请点击查询获取数据" fileName="dxzs"/></td></tr>
							<%
							} else if(visitReportVoList != null && visitReportVoList.size()>0)
							{
								for(VisitReportVo visitReportVo : visitReportVoList)
								{
								%>
								<tr>
									<td class="textalign"><%=visitReportVo.getTitle()!=null?visitReportVo.getTitle():"" %></td>
									<td><%=visitReportVo.getTaskId()!=null?visitReportVo.getTaskId():"" %></td>
									<td class="textalign">
										<%
											if(visitReportVo.getMessage() == null || "".equals(visitReportVo.getMessage())) {
														out.print("-");
													} else {
										%> <a onclick="modify(this)"><xmp>
											<%
												String st = "";
															String temp = visitReportVo.getMessage().replaceAll("#[pP]_(\\d+)#", "{#" + cs + "$1#}");
															if (temp.length() > 5) {
																st = temp.substring(0, 5) + "...";
															} else {
																st = temp;
															}
															out.print(st);
											%>
											</xmp> <%--用label显示短信内容<label style="display:none"><xmp><%//temp%></xmp></label>--%>
											<textarea style="display:none"><%=temp%></textarea>
									</a> <% } %>
									</td>
									<td>
									<%
										Long u = visitReportVo.getUnicom();
												if (0 == u) {
													out.print("移动");
												} else if (1 == u) {
													out.print("联通");
												} else if (5 == u) {
													out.print("国外");
												} else if (21 == u) {
													out.print("电信");
												} else {
													out.print("-");
												}
									%>
									</td>
									<td><%=visitReportVo.getPhone()!=null?visitReportVo.getPhone():"" %></td>
									<td><%=visitReportVo.getAreaZone()!= null? visitReportVo.getAreaZone():"****" %></td>
									<td><%String errcode = visitReportVo.getErrorCode()==null?"":visitReportVo.getErrorCode().trim();
									if(errcode.length() == 0)
									{
										out.print("-");
									}
									else if("DELIVRD".equals(errcode) || "0".equals(errcode))
									{
									    out.print("成功");
									}else{
									    out.print("失败["+errcode+"]");
									}%></td>
									<%--<td><%=(visitReportVo.getVisitCount()!=null&&visitReportVo.getVisitCount()>0)?"已访问":"未访问" %></td>--%>
									<td><%=visitReportVo.getVisitCount()==null?0:visitReportVo.getVisitCount() %></td>
									<td><%=visitReportVo.getLastIP()==null?"-":visitReportVo.getLastIP() %></td>
									<td><%=visitReportVo.getLastVisitTime()==null?"-":sdf.format(visitReportVo.getLastVisitTime().getTime()) %></td>
									<td><%=visitReportVo.getSendTime()==null?"-":sdf.format(visitReportVo.getSendTime().getTime()) %></td>
									<td><a onclick="javascript:goDetail('<%=visitReportVo.getPhone()%>','<%=visitReportVo.getTaskId() %>','<%=URLEncoder.encode(visitReportVo.getTitle(), "UTF-8") %>','<%=sdf.format(visitReportVo.getSendTime()) %>')">查看</a></td>
								</tr>
								<%
								}
							}else{
						%>
						<tr><td align="center" colspan="13"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="13">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</form>
			</div>

			<%-- 内容结束 --%>
			<div id="modify" title="信息内容"  style="padding:5px;width:300px;height:160px;display:none">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td style='word-break: break-all;'>
								<span><textarea id="msgcont" style="width:100%;height:100%;border:0px;" rows="15" readonly="readonly"></textarea></span>
							</td>
						</tr>
					</thead>
				</table>
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
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
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

		Date.prototype.format = function(fmt) {
		    var o = {
		        "M+": this.getMonth() + 1,
		        //月份
		        "d+": this.getDate(),
		        //日
		        "h+": this.getHours(),
		        //小时
		        "m+": this.getMinutes(),
		        //分
		        "s+": this.getSeconds(),
		        //秒
		        "q+": Math.floor((this.getMonth() + 3) / 3),
		        //季度
		        "S": this.getMilliseconds()
		        //毫秒
		    };
		    if (/(y+)/.test(fmt)) {
		        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		    }
		    for (var k in o) {
		        if (new RegExp("(" + k + ")").test(fmt)) {
		            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		        }
		    }
		    return fmt;
		};
			<%
			if(!(endTime!= null && !"".equals(endTime))){
				out.print("$(\"#startTime\").val(new Date().format(\"yyyy-MM\"+\"-01 00:00:00\"));");
			}

			if(!(endTime!= null && !"".equals(endTime))){
				out.print("$(\"#endTime\").val(new Date().format(\"yyyy-MM-dd hh:mm:ss\"));");
			}
			%>

			$('#search').click(function(){

			    //新增查询次数输入长度控制在10位
                if($('#visitCount').val() != null && $('#visitCount').val() != "") {
                    var str = $('#visitCount').val()+'';
                    if(str.length > 10) {
                        alert("输入查询次数的数值已超过最大值！");
                        return false;
                    }
                }

                if(($.trim($('#phone').val()) == "" || $.trim($('#phone').val()) == null)
                    && ($.trim($('#taskId').val()) == '' || $.trim($('#taskId').val()) == null)) {
                    alert("由于数据量过大，查询时手机号和批次必须至少输入一个条件！");
                    return false;
                }

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


			$('#modify').dialog({
				autoOpen: false,
				width:300,
			    height:300
			});

			if($('#tips').val() == 1) {
			    alert("由于数据量过大，查询时手机号和批次必须至少输入一个条件！");
            }

		});

		function stime() {
				var max = "2099-12-31 23:59:59";
				var v = $("#endTime").attr("value");
				var min = "1900-01-01 00:00:00";
				if (v.length != 0) {
					max = v;
					var year = v.substring(0, 4);
					var mon = v.substring(5, 7);
					if (mon != "01") {
						mon = String(parseInt(mon, 10) - 1);
						if (mon.length == 1) {
							mon = "0" + mon;
						}
					} else {
						year = String((parseInt(year, 10) - 1));
						mon = "12";
					}
					min = year + "-" + mon + "-01 00:00:00";
				}
				WdatePicker({
					dateFmt: 'yyyy-MM-dd HH:mm:ss',
					minDate: min,
					maxDate: max,
					enableInputMask:false
				});

			};

			function rtime() {
				var max = "2099-12-31 23:59:59";
				var v = $("#startTime").attr("value");
				if (v.length != 0) {
					var year = v.substring(0, 4);
					var mon = v.substring(5, 7);
					var day = 31;
					if (mon != "12") {
						mon = String(parseInt(mon, 10) + 1);
						if (mon.length == 1) {
							mon = "0" + mon;
						}
						switch (mon) {
						case "01":
							day = 31;
							break;
						case "02":
							day = 28;
							break;
						case "03":
							day = 31;
							break;
						case "04":
							day = 30;
							break;
						case "05":
							day = 31;
							break;
						case "06":
							day = 30;
							break;
						case "07":
							day = 31;
							break;
						case "08":
							day = 31;
							break;
						case "09":
							day = 30;
							break;
						case "10":
							day = 31;
							break;
						case "11":
							day = 30;
							break;
						}
					} else {
						year = String((parseInt(year, 10) + 1));
						mon = "01";
					}
					max = year + "-" + mon + "-" + day + " 23:59:59";
				}
				WdatePicker({
					dateFmt: 'yyyy-MM-dd HH:mm:ss',
					minDate: v,
					maxDate: max,
                    enableInputMask:false
				});
			}

			function modify(t) {
				$("#msgcont").empty();
				$("#msgcont").val($(t).children("textarea").val());
				$('#modify').dialog('open');
			}

			 function goDetail(phone,taskId,title,sendTime){
			 	window.location.href = "<%=commonPath %>/visitDetailSvt.htm?phone="+phone+"&taskId="+taskId+"&title="+encodeURIComponent(title)+"&sendTime="+sendTime;
			 }

		</script>
	</body>
</html>
