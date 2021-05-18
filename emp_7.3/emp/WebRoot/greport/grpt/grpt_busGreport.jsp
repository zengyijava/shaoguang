<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.system.LfPageField"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="org.apache.log4j.lf5.LF5Appender"%>
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
	String menuCode = titleMap.get("busGreport");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String mstype =(String)request.getAttribute("mstype");
	String year =(String)request.getAttribute("year");
	@SuppressWarnings("unchecked")
	List<String> ys=(List<String>)session.getAttribute("ys");
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList=(List<LfBusManager>)session.getAttribute("buslist");
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
<style type="text/css">
	ul{position: absolute;z-index: 5555;height: 300px;overflow: auto;width: 200px;line-height: 20px;}
	.liShow{display:block;background-color: white;}
	.liHide{display:none;}
</style>
<script>
var path="<%=path%>";
var year ='<%=year==null?"":year %>';
var imonth ='<%=request.getParameter("imonth")==null?"":request.getParameter("imonth") %>';
var mstype ='<%=mstype==null?"":mstype %>';
var buscode ='<%=request.getParameter("buscode")==null?"":request.getParameter("buscode") %>';
var rcorpcode ='<%=request.getParameter("lgcorpcode")==null?"":request.getParameter("lgcorpcode") %>';
</script>
</head>
<body>
<div id="container" class="container">
	<input type="hidden" value="<%=skin%>" id="skin" name="skin">
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<div id="rContent" class="rContent">
		<div id="grpt">
			<div class="hd_head">
			<dl>
			    <dd><emp:message key="cxtj_sjfx_ztfsqs_xxlx" defVal="信息类型：" fileName="cxtj"></emp:message></dd>
				<dd>
					<select name="mstype" id="mstype" style="width: 100px;">
										<%
										if(pagefileds!=null&&pagefileds.size()>1){
											for(int i=1;i<pagefileds.size();i++){
											LfPageField pagefid=pagefileds.get(i);
										%>
										     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(mstype==null?"":mstype)?"selected":"" %>>
										     <%--<%=pagefid.getSubFieldName() %>--%>
							     			 <%="短信".equals(pagefid.getSubFieldName())?MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_dx",request):("彩信".equals(pagefid.getSubFieldName())?MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_cx",request):pagefid.getSubFieldName()) %>
							    
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
									<%
									}
									}
									 %>

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
		<dd><emp:message key="cxtj_sjfx_ztfsqs_ywlx" defVal="业务类型：" fileName="cxtj"></emp:message></dd>
		<dd class="busType">
		<div onclick="change(this);" class="c_selectBox div_bd" style="width: 198px;height: 20px;z-index: 0;cursor: pointer;">&nbsp;<emp:message key="cxtj_sjfx_ztfsqs_qxz" defVal="请选择" fileName="cxtj"></emp:message><div class="c_selectimg"></div></div>
		<ul id="myUl">
		<%
			if(busList!=null){
				for(int i=0;i<busList.size();i++){
					LfBusManager busmg=busList.get(i);
					//if((i+1)%4==1){
		 %>
			<li value="1" class="liHide">
		 <%
					//}
		 %>
				<input type="checkbox" name="buscode" class="buscode" value="<%=busmg.getBusCode() %>" id="bus<%=i %>"><label for="bus<%=i %>"><%=(busmg.getBusName()!=null?busmg.getBusName().replace("\"","&quot;").replace("<","&lt;").replace(">","&gt;"):"") + "("+busmg.getBusCode()+")" %></label>
				<%
				//if((i+1)%4==0){
				 %>
			</li>
	     <%
	     		//}
			}
		}
		 %>
		 </ul>
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
<script src="<%=iPath%>/js/highcharts.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/grpt_busGreport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>

<script type="text/javascript">
var nextState=1;
function change(obj){
	var liArray=document.getElementsByTagName("LI");
	var i=0;
	var length=liArray.length;
	switch(nextState){
		case 1:
			//$("#myUl").addClass("div_bd");
			for(;i<length;i++){
				liArray[i].className="liShow";
			}
			nextState=0;
			break;
		case 0:
			for(;i<length;i++){
				liArray[i].className="liHide";
			}
			//$("#myUl").removeClass("div_bd");
			nextState=1;
	}
}

</script>
</body>
</html>

