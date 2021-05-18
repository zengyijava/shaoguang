<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.system.LfPageField"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@SuppressWarnings("unchecked")	
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefiledusers");
	//boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String menuCode = titleMap.get("totalGreport");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String mstype =(String)request.getAttribute("mstype");
	String ismuti =(String)request.getAttribute("ismuti");
	String year =(String)request.getAttribute("year");
	@SuppressWarnings("unchecked")
	List<String> ys=(List<String>)session.getAttribute("ys");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	MessageUtils messageUtils = new MessageUtils();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//确定
	String qd = messageUtils.extractMessage("cxtj", "cxtj_sjfx_ztfsqs_qd", request);
	//取消
	String qx = messageUtils.extractMessage("cxtj", "cxtj_sjfx_ztfsqs_qx", request);
	
%>
<!doctype html>
<html lang="en">
<head>
<%@include file="/common/common.jsp" %>
<title><%=titleMap.get(menuCode) %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/grpt.css?V=<%=StaticValue.getJspImpVersion() %>" />
<style type="text/css">

<%--[if lt IE 7]>
		ul,ol{*list-style:none;}
		.addOption>li{*float:left;*display: inline;}
		.todo{
			*display: block;*height:20px;
		}	
		.addOption:after{*content:".";*display:block;*visibility:hidden;*height:0;*clear:both;}
		.addOption{*zoom:1}
<![endif]--%>


</style>
<script>
var path="<%=path%>";
var ryear ='<%=year==null?"":year %>';
var rimonth ='<%=request.getParameter("imonth")==null?"":request.getParameter("imonth") %>';
var rmstype ='<%=request.getParameter("mstype")==null?"":request.getParameter("mstype") %>';
var stempval='<%=request.getParameter("stempval")==null?"":request.getParameter("stempval") %>';
var rcorpcode='<%=request.getParameter("lgcorpcode")==null?"":request.getParameter("lgcorpcode") %>';
</script>
</head>
<body>
<div id="container" class="container">
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<input type="hidden" value="<%=skin%>" id="skin" name="skin">
	<div id="rContent" class="rContent">
		<div id="grpt">
			<div class="hd_head">
				<dl>
			      <dd><emp:message key="cxtj_sjfx_ztfsqs_xxlx" defVal="信息类型：" fileName="cxtj"></emp:message></dd>
			      <dd><select name="mstype" id="mstype" style="width: 100px;">
							<%
							if(pagefileds!=null&&pagefileds.size()>1){
								for(int i=1;i<pagefileds.size();i++){
								LfPageField pagefid=pagefileds.get(i);
								
							%>
							     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(mstype==null?"":mstype)?"selected":"" %>>
							     <%--<%=pagefid.getSubFieldName() %>
							     --%><%="短信".equals(pagefid.getSubFieldName())?MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_dx",request):("彩信".equals(pagefid.getSubFieldName())?MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_cx",request):pagefid.getSubFieldName()) %>
							     </option>
							<% 
								}
							}
							%>
						</select>
					</dd>
					<dd><emp:message key="cxtj_sjfx_ztfsqs_sj" defVal="时间：" fileName="cxtj"></emp:message></dd>
					<dd>
						<select name="year" id="year"  style="width: 100px;">
							<%
							if(ys!=null){
							for(String years:ys){
							 %>
							<option value="<%=years %>" <%if(years.equals(year)) {%> selected="selected" <%} %>><%=years %>
								<%if(!StaticValue.ZH_HK.equals(langName)){%>
								<emp:message key="cxtj_sjfx_ztfsqs_n" defVal="年" fileName="cxtj"></emp:message>
								<%}%>
							</option>
							<%}}%>

						</select>
					</dd>
					<dd>
					<select name="month" id="month" style="width: 100px;">
						<option value=""><emp:message key="cxtj_sjfx_ztfsqs_qxzyf" defVal="请选择月份" fileName="cxtj"></emp:message></option>
						
						<%if(StaticValue.ZH_HK.equals(langName)){%>
							<option value="1" <%if("1".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>January</option>
							<option value="2"  <%if("2".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>February</option>
							<option value="3" <%if("3".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>March</option>
							<option value="4" <%if("4".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>April</option>
							<option value="5" <%if("5".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>May</option>
							<option value="6" <%if("6".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>June</option>
							<option value="7" <%if("7".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>July</option>
							<option value="8" <%if("8".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>Aguest</option>
							<option value="9" <%if("9".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>September</option>
							<option value="10" <%if("10".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>October</option>
							<option value="11" <%if("11".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>November</option>
							<option value="12" <%if("12".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>December</option>
						<%}else{%>
							<option value="1" <%if("1".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>1<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="2"  <%if("2".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>2<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="3" <%if("3".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>3<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="4" <%if("4".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>4<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="5" <%if("5".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>5<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="6" <%if("6".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>6<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="7" <%if("7".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>7<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="8" <%if("8".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>8<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="9" <%if("9".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>9<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="10" <%if("10".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>10<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="11" <%if("11".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>11<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
							<option value="12" <%if("12".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>12<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
						<%}%>
					</select>
					</dd>
					<dd>
						<div class="timePk">
							<a href="javascript:void(0)" class="compare"><emp:message key="cxtj_sjfx_ztfsqs_yqtsjdb" defVal="与其他时间对比" fileName="cxtj"></emp:message></a>
							<div id="other_time">
								<div class="inner">
									<div class="btnDiv">
										<input type="button" class="btnClass2" id="confirm"  value="<emp:message key="cxtj_sjfx_ztfsqs_qd" defVal="确定" fileName="cxtj"></emp:message>">
					    	    		<input type="button" class="btnClass2" id="cancel" value="<emp:message key="cxtj_sjfx_ztfsqs_qx" defVal="取消" fileName="cxtj"></emp:message>">
					    	    		<br/>
									</div>
									<div id="all_option">
										<ul class="addOption">
											<li>
												<select name="year1" class="year1">
												<option value=""><emp:message key="cxtj_sjfx_ztfsqs_qxznf" defVal="请选择年份" fileName="cxtj"></emp:message></option>
													<%
													if(ys!=null){
													for(String years:ys){
													 %>
													<option value="<%=years %>" ><%=years %>
													<%if(!StaticValue.ZH_HK.equals(langName)){%>
													<emp:message key="cxtj_sjfx_ztfsqs_n" defVal="年" fileName="cxtj"></emp:message>
													<%}%>
													</option>
													<%}}%>

												</select>
											</li>
											<li>	
												<select name="month1" class="month1">
													<option value=""><emp:message key="cxtj_sjfx_ztfsqs_qxzyf" defVal="请选择月份" fileName="cxtj"></emp:message></option>
													
													<%if(StaticValue.ZH_HK.equals(langName)){%>
														<option value="1">January</option>
														<option value="2">February</option>
														<option value="3">March</option>
														<option value="4">April</option>
														<option value="5">May</option>
														<option value="6">June</option>
														<option value="7">July</option>
														<option value="8">Aguest</option>
														<option value="9">September</option>
														<option value="10">October</option>
														<option value="11">November</option>
														<option value="12">December</option>
													<%}else{%>
														<option value="1">1<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="2">2<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="3">3<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="4">4<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="5">5<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="6">6<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="7">7<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="8">8<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="9">9<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="10">10<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="11">11<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
														<option value="12">12<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="月" fileName="cxtj"></emp:message></option>
													<%}%>
												</select>
											</li>
											<li>	
												<i class="addPara todo">+</i>
											</li>
										</ul>
									</div>	
									<span class="bd1"></span>
									<span class="bd2"></span>
									<span class="close"><b>x</b></span>
								</div>
							</div>
						</div>
					</dd>
					<%-- <dd><a href="javascript:void(0)" onclick="printPage()" id="printPage">打印</a></dd> --%>
				</dl>
				
			</div>
			<div class="showCharts">
				<div id="charts" style="min-width:800px;_width:800px;height:400px"></div>
			</div>	
		</div>
	</div>
</div>

<div id="corpCode" class="hidden"></div>
<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/grpt_totalGreport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=iPath%>/js/highcharts.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>

</body>
</html>

