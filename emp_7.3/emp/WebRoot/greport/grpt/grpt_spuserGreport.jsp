<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.system.LfPageField"%>
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
String menuCode = titleMap.get("spuserGreport");
menuCode = menuCode == null ? "0-0-0" : menuCode;
String mstype =(String)request.getAttribute("mstype");
String year =(String)request.getAttribute("year");
@SuppressWarnings("unchecked")
List<String> ys=(List<String>)session.getAttribute("ys");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

MessageUtils messageUtils = new MessageUtils();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);


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
<script>
var path="<%=path%>";
var ryear ='<%=year==null?"":year %>';
var rimonth ='<%=request.getParameter("imonth")==null?"":request.getParameter("imonth") %>';
var rmstype ='<%=request.getParameter("mstype")==null?"":request.getParameter("mstype") %>';
var rorderby='<%=request.getParameter("orderby")==null?"":request.getParameter("orderby") %>';
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
			      <dd><emp:message key="cxtj_sjfx_ztfsqs_xxlx" defVal="???????????????" fileName="cxtj"></emp:message></dd>
			      <dd><select name="mstype" id="mstype" style="width: 100px;">
							<%
							if(pagefileds!=null&&pagefileds.size()>1){
								for(int i=1;i<pagefileds.size();i++){
								LfPageField pagefid=pagefileds.get(i);
							%>
							     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(mstype==null?"":mstype)?"selected":"" %>>
							     <%--<%=pagefid.getSubFieldName() %>
							     --%>
							     <%="??????".equals(pagefid.getSubFieldName())?MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_dx",request):("??????".equals(pagefid.getSubFieldName())?MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_cx",request):pagefid.getSubFieldName()) %>
							    
							     
							     </option>
							<% 
								}
							}
							%>
						</select>
					</dd>
					<dd><emp:message key="cxtj_sjfx_ztfsqs_sj" defVal="?????????" fileName="cxtj"></emp:message></dd>
					<dd>
						<select name="year" id="year"  style="width: 100px;">
							<%
							if(ys!=null){
							for(String years:ys){
							 %>
							<option value="<%=years %>" <%if(years.equals(year)) {%> selected="selected" <%} %>><%=years %>
								<%if(!StaticValue.ZH_HK.equals(langName)){%>
								<emp:message key="cxtj_sjfx_ztfsqs_n" defVal="???" fileName="cxtj"></emp:message>
								<%}%>
							</option>
							<%}}%>

						</select>
					</dd>
					<dd>
					<select name="month" id="month" style="width: 100px;">
						<option value=""><emp:message key="cxtj_sjfx_ztfsqs_qxzyf" defVal="???????????????" fileName="cxtj"></emp:message></option>
						
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
							<option value="1" <%if("1".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>1<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="2"  <%if("2".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>2<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="3" <%if("3".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>3<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="4" <%if("4".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>4<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="5" <%if("5".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>5<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="6" <%if("6".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>6<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="7" <%if("7".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>7<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="8" <%if("8".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>8<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="9" <%if("9".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>9<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="10" <%if("10".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>10<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="11" <%if("11".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>11<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
							<option value="12" <%if("12".equals(request.getParameter("imonth"))){ %> selected="selected" <%} %>>12<emp:message key="cxtj_sjfx_ztfsqs_m" defVal="???" fileName="cxtj"></emp:message></option>
						<%}%>
					</select>
					</dd>
					<dd>
					<select name="orderby" id="orderby"  style="width: 120px;">
						<option value="1" <% if("1".equals(request.getAttribute("orderby"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjfx_ztfsqs_fslcdds" defVal="?????????????????????" fileName="cxtj"></emp:message></option>
						<option value="2" <% if("2".equals(request.getAttribute("orderby"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjfx_ztfsqs_fslcsdd" defVal="?????????????????????" fileName="cxtj"></emp:message></option>
						<option value="3" <% if("3".equals(request.getAttribute("orderby"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjfx_ztfsqs_zhpyzmsx" defVal="????????????????????????" fileName="cxtj"></emp:message></option>
					</select>
					</dd>
					<%-- <dd><a href="javascript:void(0)" onclick="printPage()" id="printPage">??????</a></dd> --%>
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
<script src="<%=iPath%>/js/highcharts.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/grpt_spuserGreport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>

</body>
</html>

