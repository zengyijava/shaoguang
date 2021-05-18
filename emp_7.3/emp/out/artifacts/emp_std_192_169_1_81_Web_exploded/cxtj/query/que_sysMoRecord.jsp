<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.CommonVariables" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.query.vo.MoTask01_12Vo" %>
<%@ page import="com.montnets.emp.query.vo.MoTaskVo" %>
<%@page import="com.montnets.emp.query.vo.SysMoMtSpgateVo"%>
<%@page import="com.montnets.emp.util.PageInfo" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
    String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0,
                                                     request.getRequestURI()
                                                            .lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    List<String> userList = (List<String>) request.getAttribute("mrUserList");

    String recordType = request.getParameter("recordType");//操作员上行记录：history，realTime
    session.setAttribute("recordType", recordType);

    if (recordType == null) {
        recordType = "realTime";
    }
    String phone, spgate, startTime, endTime, addbookname, msgContent;
    String spunicom = "";
    if ("history".equals(recordType)) {
        MoTask01_12Vo moTaskVo = (MoTask01_12Vo) request.getAttribute("moTaskHis");
        if (moTaskVo == null) {
            moTaskVo = new MoTask01_12Vo();
        }
        phone = moTaskVo.getPhone();
        spgate = moTaskVo.getSpnumber();
        startTime = moTaskVo.getStartSubmitTime();
        endTime = moTaskVo.getEndSubmitTime();
        addbookname = moTaskVo.getName();
        msgContent = moTaskVo.getMsgContent();
        if (moTaskVo.getUnicom() != null) {
            spunicom = String.valueOf(moTaskVo.getUnicom());
        }
    } else {
        MoTaskVo moTaskVo = (MoTaskVo) request.getAttribute("moTask");
        if (moTaskVo == null) {
            moTaskVo = new MoTaskVo();
        }
        phone = moTaskVo.getPhone();
        spgate = moTaskVo.getSpnumber();
        startTime = moTaskVo.getStartSubmitTime();
        endTime = moTaskVo.getEndSubmitTime();
        addbookname = moTaskVo.getName();
        msgContent = moTaskVo.getMsgContent();
        if (moTaskVo.getUnicom() != null) {
            spunicom = String.valueOf(moTaskVo.getUnicom());
        }
    }

    @SuppressWarnings("unchecked")
    List<SysMoMtSpgateVo> spList = (List<SysMoMtSpgateVo>) request.getAttribute("spList");

    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get("sysMoRecord");
    menuCode = menuCode == null ? "0-0-0" : menuCode;
    //excel自动识别用
    MessageUtils messageUtils = new MessageUtils();
    //通道号码
    String tdhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_tdhm", request);
    if(tdhm!=null&&tdhm.length()>1){
    	tdhm = tdhm.substring(0,tdhm.length()-1);
    }
  	 //运营商
  	 String yys = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_yys", request);
  	 if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
	 //手机号码
	 String sjhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_sjhm", request);
	 if(sjhm!=null&&sjhm.length()>1){
    	sjhm = sjhm.substring(0,sjhm.length()-1);
    }
	 //姓名
	 String xm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_xm", request);
	 if(xm!=null&&xm.length()>1){
    	xm = xm.substring(0,xm.length()-1);
    }
	 //接收时间
	 String jssj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_jssj", request);
	 if(jssj!=null&&jssj.length()>1){
    	jssj = jssj.substring(0,jssj.length()-1);
    }
	 //短信内容
	 String dxnr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dxnr", request);
	 if(dxnr!=null&&dxnr.length()>1){
    	dxnr = dxnr.substring(0,dxnr.length()-1);
    }
	 //编码
	 String bm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_bm", request);
	 if(bm!=null&&bm.length()>1){
    	bm = bm.substring(0,bm.length());
    }
    
     //移动
    String yd = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_yd", request);
    //联通
    String lt = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_lt", request);
    //电信
     String dx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dx", request);
    //国外
	 
    Map<String, String> excelConditionMap = new java.util.LinkedHashMap<String, String>();
    excelConditionMap.put(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request), "");
    excelConditionMap.put(tdhm, "spnumber");
    excelConditionMap.put(yys, "");
    excelConditionMap.put(sjhm, "phone");
    excelConditionMap.put(xm, "name");
    excelConditionMap.put(jssj, "deliverTime");
    if ("realTime".equals(recordType)) {
        //excelConditionMap.put("分条","");
    }
    excelConditionMap.put(dxnr, "msgContent");
    excelConditionMap.put(bm, "");
    session.setAttribute("EXCEL_MAP", excelConditionMap);
    java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;

    @SuppressWarnings("unchecked")
    List<MoTask01_12Vo> moTaskList_vo = (List<MoTask01_12Vo>) request.getAttribute("moHisTaskList");
    @SuppressWarnings("unchecked")
    List<MoTaskVo> moTaskList = (List<MoTaskVo>) request.getAttribute("moRealTimeTaskList");

    boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

    String findResult = (String) request.getAttribute("findresult");
    CommonVariables CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin") == null ? "default"
                                                           : (String) session.getAttribute("stlyeSkin");
    //获取语言
    String langName = (String) session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="cxtj_sjcx_xtsxjl_title" defVal="系统上行记录" fileName="cxtj"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="que_sysMoRecord">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode)%>
			
			<%-- 内容开始 --%>
			<%
			    if (btnMap.get(menuCode + "-0") != null) {
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath%>" id="inheritPath"/>
				<form name="pageForm" action="que_sysMoRecord.htm" method="post"
					id="pageForm">
					<div id="r_sysMoRparams" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<%
						    if (btnMap.get(menuCode + "-5") != null) {
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition" ></a>
						<input type="hidden" name="menucode" id="menucode" value="17"/>
						<%
						    }
						%>
						<a id="exportCondition"><emp:message key="cxtj_sjcx_xtsxjl_dc" defVal="导出" fileName="cxtj"></emp:message></a>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_jllx" defVal="记录类型" fileName="cxtj"></emp:message>
									</td>
									<td>
										<select name="recordType"  id="recordType" isInput="false">
											<option value="realTime">
												<emp:message key="cxtj_sjcx_xtsxjl_ssjl" defVal="实时记录" fileName="cxtj"></emp:message>
											</option>
											<option value="history"
												<%="history".equals(recordType) ? "selected" : ""%>>
												<emp:message key="cxtj_sjcx_xtsxjl_lsjl" defVal="历史记录" fileName="cxtj"></emp:message>
											</option>
										</select>
									</td>
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_tdhm" defVal="通道号码" fileName="cxtj"></emp:message>
									</td>
									<td>
										<label>
											<select name="spgate" id="spgate">
												<option value="">
													<emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部" fileName="cxtj"></emp:message>
												</option>
												<%
												    if (spList != null && spList.size() > 0) {
												            for (int i = 0; i < spList.size(); i++) {
												%>
												<option value="<%=spList.get(i).getSpgate()%>"
													<%=spList.get(i).getSpgate().equals(spgate) ? "selected" : ""%>>
													<%=spList.get(i).getSpgate()%>
												</option>
												<%
												    }
												        }
												%>
											</select>
										</label>
									</td>
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_spzh" defVal="sp账号" fileName="cxtj"/>
									</td>
									<td>
										<label>
											<select name="userid" id="userid">
												<option value="">
													<emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部" fileName="cxtj"></emp:message>
												</option>
												<%
												    String userid = request.getParameter("userid");
												        if (userList != null && userList.size() > 0) {
												            for (String userdata : userList) {
												%>
											<option value="<%=userdata%>" 
												<%=userdata.equals(userid) ? "selected" : ""%>>
												<%=userdata%>
											</option>
											<%
											    }
											        }
											%>
											</select>
										</label>
									</td>
									<td class="tdSer"><center><a id="search"></a></center></td>
								</tr>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_xm" defVal="姓名" fileName="cxtj"></emp:message>
									</td>
									<td class="tableTime">
										<input type="text" value="<%=addbookname == null ? "" : addbookname%>" id="addbookname" name="addbookname" />
									</td>
 									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_dxnr" defVal="短信内容" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" value="<%=msgContent == null ? "" : msgContent%>" id="msgContent" name="msgContent" />
									</td>
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_sjhm" defVal="手机号码" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" value="<%=phone == null ? "" : phone%>" id="phone" name="phone" maxlength="21" onkeyup="phoneInputCtrl($(this))"/>
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
								<td>
										<emp:message key="cxtj_sjcx_xtsxjl_jssj" defVal="接收时间" fileName="cxtj"></emp:message>
									</td>
									<td class="tableTime">
										<input type="text" class="Wdate" readonly="readonly" onclick="sedtime()" value="<%=startTime == null ? "" : startTime%>" id="sendtime" name="sendtime">
									</td>
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_z" defVal="至：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" class="Wdate" readonly="readonly" onclick="revtime()" value="<%=endTime == null ? "" : endTime%>" id="recvtime" name="recvtime">
									</td>
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_yys" defVal="运营商" fileName="cxtj"></emp:message>
									</td>
									<td>
										<select id="spunicom" name="spunicom" isInput="false">
											<option value=""><emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部" fileName="cxtj"></emp:message></option>
											<option value="0" <%=spunicom.equals("0") ? "selected" : ""%> ><emp:message key="cxtj_sjcx_xtsxjl_yd" defVal="移动" fileName="cxtj"></emp:message></option>
											<option value="1" <%=spunicom.equals("1") ? "selected" : ""%>><emp:message key="cxtj_sjcx_xtsxjl_lt" defVal="联通" fileName="cxtj"></emp:message></option>
											<option value="21" <%=spunicom.equals("21") ? "selected" : ""%>><emp:message key="cxtj_sjcx_xtsxjl_dx" defVal="电信" fileName="cxtj"></emp:message></option>
											<option value="5" <%=spunicom.equals("5") ? "selected" : ""%>><emp:message key="cxtj_sjcx_xtsxjl_gw" defVal="国外" fileName="cxtj"></emp:message></option>
										</select>
										<input type="hidden" name="unicoms" id="unicoms" value="<%=spunicom%>">
									</td>
									<td colspan=1>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
							     <%
							         iter = excelConditionMap.entrySet().iterator();
							             while (iter.hasNext()) {
							                 e = iter.next();
							     %>
							  <th><%=e.getKey()%></th>
						    <%
						        }
						    %>
							</tr>
						</thead>
						<tbody>
							<%
							    if (isFirstEnter) {
							%>
							<tr><td colspan="9" class="queryData"><emp:message key="cxtj_sjcx_xtsxjl_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"></emp:message>
							</td></tr>
							<%
							    } else if ("history".equals(recordType)) {

							            if (moTaskList_vo != null && moTaskList_vo.size() > 0) {

							                for (int i = 0; i < moTaskList_vo.size(); i++) {
							                    MoTask01_12Vo moTask = moTaskList_vo.get(i);
							%>
							<tr>
								<td><%=moTask.getUserId()%></td>
								<td>
									<%=moTask.getSpnumber()%>
								</td>
								<td>
								<%
									String yd1 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_yd", request);
									String lt1 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_lt", request);
									String dx1 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dx", request);
									String gw1 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_gw", request);
								    Integer unicom = moTask.getUnicom();
								                    if (unicom != null) {
								                        out.print((unicom - 0 == 0) ? yd1
								                                                   : (unicom - 1 == 0 ? lt1
								                                                                     : (unicom - 21 == 0 ? dx1
								                                                                                        : (unicom - 5 == 0 ? gw1
								                                                                                                          : "-"))));
								                    }
								%>
								</td> 
								<td>
								<%
								    if ("ALARM".equals(moTask.getPhone().toUpperCase())) {
								                        out.print("-");
								                    } else {
								                        String phones = CV.replacePhoneNumber(btnMap, moTask.getPhone());
								                        out.print(phones);
								                    }
								%>
								</td>
								<td>
								<%=moTask.getName() == null ? "-" : moTask.getName()%>
								</td>
								<td>
								 <%=df.format(moTask.getDeliverTime())%>
								</td>
								<td class="msgContentTd">
									<a onclick="modify(this)">
										<label style="display:none">
											<xmp><%=moTask.getMsgContent()%></xmp>
										</label>
										<xmp><%=moTask.getMsgContent().length() > 5 ? moTask.getMsgContent().substring(0, 5) + "..." : moTask.getMsgContent()%></xmp>
									</a>
								</td>
								<td><%=moTask.getMsgFmt()%></td>
							</tr>
							   <%
							       }
							   %>
								<%
								    } else {
								%>
								 <tr><td colspan="9" class="queryData"><emp:message key="cxtj_sjcx_xtxxjl_wjl" defVal="无记录" fileName="cxtj"></emp:message></td></tr>
								<%
								    }
								%>	
								<%
									    } else if ("realTime".equals(recordType)) {
									            if (moTaskList != null && moTaskList.size() > 0) {
									                for (int i = 0; i < moTaskList.size(); i++) {
									                    MoTaskVo moTask = moTaskList.get(i);
									%>
							<tr>
								<td><%=moTask.getUserId()%></td>
								<td>
									<%=moTask.getSpnumber()%>
								</td>
								<td>
								<%
									String yd2 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_yd", request);
									String lt2 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_lt", request);
									String dx2 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dx", request);
									String gw2 = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_gw", request);
								    Integer unicom = moTask.getUnicom();
								                    if (unicom != null) {
								                        out.print((unicom - 0 == 0) ? yd2
								                                                   : (unicom - 1 == 0 ? lt2
								                                                                     : (unicom - 21 == 0 ? dx2
								                                                                                        : (unicom - 5 == 0 ? gw2
								                                                                                                          : "-"))));
								                    }
								%>
								</td>
								<td>
								<%
								    if ("ALARM".equals(moTask.getPhone().toUpperCase())) {
								                        out.print("-");
								                    } else {
								                        String phones = CV.replacePhoneNumber(btnMap, moTask.getPhone());
								                        out.print(phones);
								                    }
								%>
								</td>
								<td>
								<%=moTask.getName() == null ? "-" : moTask.getName()%>
								</td>
								<td>
								 <%=df.format(moTask.getDeliverTime())%>
								</td>
								<%--<td><%=moTask.getPknumber() %>/<%=moTask.getPktotal() %></td> --%>
								<td class="msgContentTd">
								  <a onclick="modify(this)">
									<label style="display:none">
										<xmp><%=moTask.getMsgContent()%></xmp>
									</label>
										<xmp><%=moTask.getMsgContent().length() > 5 ? moTask.getMsgContent().substring(0, 5) + "..." : moTask.getMsgContent()%></xmp>
								  </a> 
 								</td>
								<td>
								  <%=moTask.getMsgFmt()%>
								</td>
							</tr>
							<%
							    }
							            } else {
							%>
								<tr><td colspan="9" class="queryData"><emp:message key="cxtj_sjcx_xtxxjl_wjl" defVal="无记录" fileName="cxtj"></emp:message></td></tr>
								  
								<%
								  								    }
								  								        }
								  								%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec()%>"/>
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>					
				</form>
			</div>
			<%
			    }
			%>
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
		<%-- foot结束 --%>
		<div id="modify" title="<%=dxnr%>">
			<table>
				<thead>
					<tr class="modify_tr1">
						<td>
							<label id="msgss"></label>
						</td>
					</tr>
				   <tr class="modify_tr2">
						<td></td>
					</tr>
				</thead>
			</table>
		</div>
		<div class="clear"></div>
		<script type="text/javascript">
			function LoginInfo(idname){
				document.getElementById(idname).innerHTML=window.parent.parent.document.getElementById("loginparams").innerHTML;
			}
			LoginInfo("r_sysMoRparams");
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/exportexcel.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				//测试
				//var test = getJsLocaleMessage("cxtj","cxtj_sjcx_xtxxjl_text_1");
				//alert(test);
	            //getLoginInfo("#r_sysMoRparams");
				var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       //alert("加载页面失败,请检查网络是否正常!");	
			       alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_1"));
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
				
				$("#addbookname,#msgContent").live('keyup blur',function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
				});
                $('#spgate,#userid').isSearchSelect({'width':'179','isInput':true,'zindex':0});
				//导出全部数据到excel
				$("#exportCondition").click(
				 function(){
					 // if(confirm("确定要导出数据到excel?"))
					  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_2")))
					   {
					   		<%if ((moTaskList != null && moTaskList.size() > 0)
                || (moTaskList_vo != null && moTaskList_vo.size() > 0)) {%>
					   			var recordType = '<%=recordType != null ? recordType : ""%>';
						   		var spgate= '<%=spgate != null ? spgate : ""%>';
						   		var phone = '<%=phone != null ? phone : ""%>';
						   		var sendtime = '<%=startTime != null ? startTime : ""%>';
						   		var recvtime = '<%=endTime != null ? endTime : ""%>';
						   		var userid = '<%=request.getParameter("userid") != null ? request.getParameter("userid")
                                                                : ""%>';
						   		var lgcorpcode =$("#lgcorpcode").val();
					            var lguserid =$("#lguserid").val();
					            var addbookname = '<%=addbookname != null ? addbookname : ""%>';
					            var msgContent = '<%=msgContent != null ? msgContent : ""%>';
					            var spunicom = '<%=spunicom != null ? spunicom : ""%>';
					            //addbookname=encodeURI(encodeURI(addbookname));
					            //msgContent=encodeURI(encodeURI(msgContent));		
					            $.ajax({
									type: "POST",
									url: "<%=path%>/que_sysMoRecord.htm?method=exportExcel",
									data: {
											recordType:recordType,
										   	spunicom:spunicom,
										   	spgate:spgate,
										   	phone:phone,
										   	sendtime:sendtime,
										   	recvtime:recvtime,
										   	userid:userid,
										   	lgcorpcode:lgcorpcode,
										   	lguserid:lguserid,
										   	addbookname:addbookname,
										   	msgContent:msgContent
										  },
					                beforeSend:function () {
					                    page_loading();
					                },
					                complete:function () {
					               	  	page_complete()
					                },
					                success:function (result) {
					                    if (result == 'true') {
					                        download_href("<%=path%>/que_sysMoRecord.htm?method=downloadFile");
					                    } else {
					                        //alert('导出失败！');
					                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
					                    }
	               					}
								  });
					   			//location.href="<%=path%>/que_sysMoRecord.htm?method=exportExcel&recordType="+recordType+"&spunicom="+spunicom+"&spgate="+spgate+"&phone="+phone+"&sendtime="+sendtime+"&recvtime="+recvtime+"&userid="+userid+"&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&addbookname="+addbookname+"&msgContent="+msgContent;
					   		<%} else {%>
					   			//alert("无数据可导出！");
					   			alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));
					   		<%}%>
					   	}
				  });
				
				
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$('#search').click(function(){submitForm();});
			});
	</script>
	<script type="text/javascript" src="<%=iPath%>/js/que_sysMoRecord.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>
