<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.passage.XtGateQueue"%>
<%@ page import="com.montnets.emp.entity.report.AprovinceCity"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue" %>
<%@page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@page import="com.montnets.emp.report.vo.SpMtDataReportVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.HashMap"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
 String inheritPath = iPath.substring(0,
		iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
PageInfo pageInfo =  (PageInfo) request.getAttribute("pageInfo");
@ SuppressWarnings("unchecked")
 Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("spMtReport");
menuCode = menuCode==null?"0-0-0":menuCode;
SpMtDataReportVo spisuncmMtDataReportVo = (SpMtDataReportVo)session.getAttribute("spisuncmMtDataReportVo");
@ SuppressWarnings("unchecked")
List<SpMtDataReportVo> reportList=(List<SpMtDataReportVo>)session.getAttribute("reportList");
@ SuppressWarnings("unchecked")
List<String> userList = (List<String>)request.getAttribute("spUserList");
List<String> mmsUserList = (List<String>)request.getAttribute("mmsUserList");
List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefiledsps");

HashMap<String, String> svrTypeList = (HashMap<String, String>)request.getAttribute("svrTypeMap");
List<AprovinceCity> areaCodeList = (List<AprovinceCity>)request.getAttribute("areaCodeList");
String userName = (String)request.getAttribute("userName");
String depName = (String)request.getAttribute("depName");
String msType =(String)request.getAttribute("msType");
String depIdStr =(String)request.getAttribute("depIdStr");
String userIdStr =(String)request.getAttribute("userIdStr");
String domesticOperator =(String)request.getAttribute("domesticOperator");
String containSubDep =(String)request.getAttribute("containSubDep");
String araCode = StringUtils.defaultIfEmpty((String)request.getAttribute("araCode"),"0");
String svrType =(String)request.getAttribute("svrType");
@ SuppressWarnings("unchecked")
List<XtGateQueue> xtList = (List<XtGateQueue>)session.getAttribute("mrXtList");
long[] sumCount=(long[])session.getAttribute("sumCount");
String counTime = "";
if(request.getAttribute("countTime")!=null){
	counTime=request.getAttribute("countTime").toString();
}

MessageUtils messageUtils = new MessageUtils();
//时间
    String sj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request);

//SP账号

//账户名称
 String zhmc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_zhmc", request);
    if(zhmc!=null&&zhmc.length()>1){
    	zhmc = zhmc.substring(0,zhmc.length()-1);
    }
//账号类型
 String zhlx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_zhlx", request);
    if(zhlx!=null&&zhlx.length()>1){
    	zhlx = zhlx.substring(0,zhlx.length()-1);
    }
//发送类型
String fslx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_fslx", request);
    if(fslx!=null&&fslx.length()>1){
    	fslx = fslx.substring(0,fslx.length()-1);
    }
//运营商
String yys = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_yys", request);
    if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
//发送成功数
String fscgs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_fscgs", request);
//接收失败数
String jssbs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_jssbs", request);
//详情
String xq = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_xq", request);
//请点击查询获取数据
String qdjcxhqsj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qdjcxhqsj", request);
//无记录
String wjj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_wjj", request);


//excel自动识别用
	Map<String,String> excelConditionMap = new java.util.LinkedHashMap<String,String>();
	excelConditionMap.put(sj,"imonth");
	excelConditionMap.put(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request),"userid");
	excelConditionMap.put(zhmc,"spusername");
	excelConditionMap.put(zhlx,"spusertype");
	excelConditionMap.put(fslx,"sendtype");
	excelConditionMap.put(yys,"spisuncm");
	session.setAttribute("EXCEL_MAP",excelConditionMap);
	java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;
    
    String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    
    String corpcode = (String)request.getAttribute("corpcode");
    Boolean isFirstEnter=(Boolean)request.getAttribute("isFirstEnter");
    //String strshow="无记录";
    String strshow=wjj;
   if(isFirstEnter!=null&&isFirstEnter){
   		reportList=null;
   		//strshow="请点击查询获取数据 ";
   		strshow=qdjcxhqsj;
   }
 String reportType=""; 
   if(request.getAttribute("reportType")!=null){
	   reportType=request.getAttribute("reportType").toString();
   }
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	String beginTime = df.format(c.getTime()).substring(0, 8) + "01";
	String endTime = df.format(c.getTime()).substring(0, 11); //change by dj
	String spisuncm="100";//默认是国内
	if(request.getParameter("spisuncm")!=null){
		spisuncm=request.getParameter("spisuncm");
	}
	
//SP账号详细信息
String zhxxxx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_zhxxxx", request);
//各国发送详细信息
String ggfsxxxx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_ggfsxxxx", request);
%>
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
        <link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>

	<body class="rep_spMtReport" id="rep_spMtReport">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<input type="hidden" id="reportTypeStr" name="reportTypeStr" value="<%=reportType%>"/>
			<input type="hidden" id="skin" name="skin" value="<%=skin%>"/>
			<input type="hidden" id="findResult" name="findResult" value="<%=findResult%>"/>
			<input type="hidden" id="sendType" name="sendType" value="<%=request.getParameter("sendtype")%>"/>
			<div id="corpCode" class="hidden"></div>
			<form name="pageForm" action="rep_spMtReport.htm?method=find" method="post" id="pageForm">
				<div id="hiddenValueDiv" style="display: none"></div>
				<input type="hidden" id="containSubDep" name="containSubDep" value="0"/>
				<div id="rContent" class="rContent">
						<div class="buttons">
							<div id="toggleDiv" >
							</div>
							<a id="exportCondition" onclick="exportCondition(<%=reportList == null ? 0:reportList.size()%>)"><emp:message key="cxtj_sjcx_yystjbb_dc" defVal="导出" fileName="cxtj"/></a>
	                 	    <input type="hidden" id="menucode" value="24" name="menucode" />
	                 	    <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
						</div>
						<div id="condition">
							<table>
								<tbody>
									<tr>
									<td><emp:message key="cxtj_sjcx_zhtjbb_zhlx" defVal="账号类型：" fileName="cxtj"/></td>
									<td>
										<select name="sptype" id="sptype" isInput="false">
											<%
											if(btnMap.get(menuCode + "-1") != null&&btnMap.get(menuCode + "-2") != null){
											 %>
											 	<option value="0"  <%if("0".equals(request.getParameter("sptype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"/></option>
											 <%
											 }
											 if(btnMap.get(menuCode + "-1") != null){
											  %>
										     <option value="1" <%if("1".equals(request.getParameter("sptype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_empyy" defVal="EMP应用" fileName="cxtj"/></option>
										     <%
										     }
										     
										     if(btnMap.get(menuCode + "-2") != null){
										     %>
										     <option value="2" <%if("2".equals(request.getParameter("sptype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_empjr" defVal="EMP接入" fileName="cxtj"/></option>
										     <%
										     }
										      %>
										</select>
									</td> 
									<td><emp:message key="cxtj_sjcx_xtsxjl_spzh" defVal="sp账号" fileName="cxtj"/></td>
										<td>
										<label>
										<select name="userId" id="userId" isInput="false">
											<option value=""><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"/></option>
											<%
											if(msType==null||"".equals(msType)||"0".equals(msType)){
												if (userList != null && userList.size() > 0){
													for(int k=0;k<userList.size();k++){
											%>
														<option value="<%=userList.get(k) %>"
															<%= userList.get(k).equals(request.getParameter("userId"))?"selected":"" %>><%=userList.get(k) %></option>
											<%		
													}
												} 
											 }else{
												if (mmsUserList != null && mmsUserList.size() > 0){
													for(int k=0;k<mmsUserList.size();k++){
											%>
														<option value="<%=mmsUserList.get(k) %>"
														<%= mmsUserList.get(k).equals(request.getParameter("userId"))?"selected":"" %>><%=mmsUserList.get(k) %></option>
											<%
													}
												}
											}
											%>
											
											</select>
											 <input type="hidden" id="userId" name="userId" value="<%=request.getParameter("userId")!=null?request.getParameter("userId"):"" %>" />
											</label> 
											
										</td> 
										<td><emp:message key="cxtj_sjcx_zhtjbb_zhmc" defVal="账户名称：" fileName="cxtj"/></td>
										<td>
										<input type="text"  id="staffname" name="staffname" value='<%=request.getParameter("staffname")==null?"":request.getParameter("staffname") %>'maxlength="11" />
										</td>
									
										
										<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
									</tr>
									<tr>

									<td><emp:message key="cxtj_sjcx_zhtjbb_fslx" defVal="发送类型：" fileName="cxtj"/></td>
								<td>
								<select name="sendtype" id="sendtype" isInput="false">
									<option value="" ><emp:message key="cxtj_sjcx_zhtjbb_qxz" defVal="请选择" fileName="cxtj"/></option>
									<%
											if(btnMap.get(menuCode + "-1") != null&&btnMap.get(menuCode + "-2") != null&&"0".equals(request.getParameter("sptype")!=null?request.getParameter("sptype"):"0")){
									%>
											 <option value="1" <%if("1".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_fs" defVal="EMP发送" fileName="cxtj"/></option>
											 <option value="2" <%if("2".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_httpjr" defVal="HTTP接入" fileName="cxtj"/></option>
											 <option value="3" <%if("3".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_dbjr" defVal="DB接入" fileName="cxtj"/></option>
											 <option value="4" <%if("4".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_zljr" defVal="直连接入" fileName="cxtj"/></option>
									<%
											 }else{
											 if(btnMap.get(menuCode + "-1") != null&&"1".equals(request.getParameter("sptype")!=null?request.getParameter("sptype"):"1")){
									%>
										    <option value="1" <%if("1".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_fs" defVal="EMP发送" fileName="cxtj"/></option>
									<%
										     }
										     
										     if(btnMap.get(menuCode + "-2") != null&&"2".equals(request.getParameter("sptype")!=null?request.getParameter("sptype"):"2")){
										     %>
										      <option value="2" <%if("2".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_httpjr" defVal="HTTP接入" fileName="cxtj"/></option>
											 <option value="3" <%if("3".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_dbjr" defVal="DB接入" fileName="cxtj"/></option>
											 <option value="4" <%if("4".equals(request.getParameter("sendtype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_zljr" defVal="直连接入" fileName="cxtj"/></option>
										     <%
										     	}
										     }
										      %>
									
								</select>
								
								</td>
								<%
									String typename="";
									if(pagefileds!=null&&pagefileds.size()>0){
										LfPageField first=pagefileds.get(0);
										typename=first.getField()+"：";
									} 
								%>
								<td><emp:message key="cxtj_sjcx_yystjbb_xxlx" defVal="信息类型" fileName="cxtj"/></td>
								<td><%--
										<select name="msType" id="msType" style="width:180px">
										<%
										if(pagefileds!=null&&pagefileds.size()>1){
											for(int i=1;i<pagefileds.size();i++){
											LfPageField pagefid=pagefileds.get(i);
										%>
										     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(msType==null?"":msType)?"selected":"" %>><%=pagefid.getSubFieldName() %></option>
										<% 
											}
										}
										
										%>
										</select> --%>
										<select name="msType" id="msType" isInput="false">
												<option value="0" <%="0".equals(msType==null?"":msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_dx" defVal="短信" fileName="cxtj"/></option>
												<option value="1" <%="1".equals(msType==null?"":msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_cx" defVal="彩信" fileName="cxtj"/></option>
											</select>
								</td>
								<td><emp:message key="cxtj_sjcx_zhtjbb_yys" defVal="运营商：" fileName="cxtj"/></td>
										<td>
										   <select id="spisuncm" name="spisuncm" isInput="false">
												<option value=""><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"/></option>
												<option value="100" <%="100".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_zhtjbb_gn" defVal="国内" fileName="cxtj"/></option>
												<option value="5" <%="5".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_zhtjbb_gw" defVal="国外" fileName="cxtj"/></option>
											</select>
										</td>
									<td>&nbsp;</td>
								</tr>
								<tr >
								<td><emp:message key="cxtj_sjcx_zhtjbb_bblx" defVal="报表类型：" fileName="cxtj"/></td>
									<td id="timeSelect">
										<select name="reportType" id="reportType" isInput="false">
											<option value="2" <%if("2".equals(reportType)||"".equals(reportType)) {
                                                out.print("selected");
                                            }
                                            %>><emp:message key="cxtj_sjcx_yystjbb_rbb" defVal="日报表" fileName="cxtj"/></option>
										     <option value="0" <%if("0".equals(reportType)) {
                                                 out.print("selected");
                                             }
                                             %>><emp:message key="cxtj_sjcx_yystjbb_ybb" defVal="月报表" fileName="cxtj"/></option>
										     <option value="1" <%if("1".equals(reportType)) {
                                                 out.print("selected");
                                             }
                                             %>><emp:message key="cxtj_sjcx_yystjbb_nbb" defVal="年报表" fileName="cxtj"/></option>
										     
										</select>
									</td>
								
									<td id="titleTime1">
										<emp:message key="cxtj_sjcx_yystjbb_tjsj" defVal="统计时间：" fileName="cxtj"/>
									</td>
									<td class="tableTime" id="titleTime2">
										<input type="text" class="Wdate" readonly="readonly" onclick="showDate()"
											value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>" 
											id="sendtime" name="begintime">
											<input type="hidden" id="sTime" value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>"/>
									</td>
									<td id="titleTime3">
										<emp:message key="cxtj_sjcx_yystjbb_z" defVal="至" fileName="cxtj"/>
									</td>
									<td id="titleTime4">
										<input type="text" class="Wdate" readonly="readonly" onclick="showDate()"
											value="<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>"
											id="recvtime" name="endtime">
											<input type="hidden" id="eTime" value="<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>"/>
									</td>

									<td id="titleYear1" style="display:none"><emp:message key="cxtj_sjcx_zhtjbb_tjny" defVal="统计年月：" fileName="cxtj"/></td>
									<td id="titleYear2" style="display:none">
										<input type="hidden" value="month" name="yearOrMonth" />
											<input type="hidden" value="2" name="count" />
										<input type="text" name="countTime" id="countTime"
														value="<%=spisuncmMtDataReportVo!=null&&spisuncmMtDataReportVo.getImonth() != null?counTime:counTime.toString().substring(0,4)%>"
														onclick="showDate()"
														readonly="readonly" class="Wdate"/>
										<input type="hidden" id="tTime" value="<%=counTime %>"/>
									</td>
								<td id="titleYear3" style="display:none">&nbsp;</td>
								<td id="titleYear4" style="display:none">&nbsp;</td>
								<%--多维度查询--%>
								<td><a class="moreDimension" onclick="showMoreDimension()"><emp:message key="common_moreDimension" defVal="更多维度" fileName="common"/></a></td>
								</tr>
								</tbody>
								<%--更多维度弹出框--%>
							</table>
							<div id="moreDimensionDiv" class="moreDimensionDiv" style="display: none;">
								<table>
									<tr>
										<td><emp:message key="cxtj_rpt_qy" defVal="区域" fileName="cxtj"/>：</td>
										<td>
											<select title="" id="araCode" name="araCode">
												<option value=""><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"/></option>
												<% for(AprovinceCity areacode :areaCodeList){
													String selected = areacode.getAreaCode()== Integer.parseInt(araCode) ?"selected":"";
												%>
												<option value="<%=areacode.getAreaCode()%>" <%=selected%>><%=areacode.getCity()%></option>
												<%}%>
											</select>
										</td>
									</tr>
									<tr>
										<td><emp:message key="cxtj_sjcx_report_ywlx" defVal="业务类型" fileName="cxtj"/>：</td>
										<td>
											<select title="" id="svrType" name="svrType">
												<option value=""><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"/></option>
												<%if(svrTypeList != null){
													for(Map.Entry<String, String> entry : svrTypeList.entrySet()){
														String selected = entry.getKey().equals(svrType) ?"selected":"";
												%>
												<option value="<%=entry.getKey()%>" <%=selected%>><%=entry.getValue()%></option>
												<%}}%>
											</select>
										</td>
									</tr>
									<tr>
										<td><emp:message key="cxtj_sjcx_grsjx_czy" defVal="操作员" fileName="cxtj"/>：</td>
										<td>
											<div>
												<input type="hidden" id="userIdStr" name="userIdStr" value="<%=StringUtils.defaultIfEmpty(userIdStr,"")%>"/>
												<input type="text" title="" id="userName" placeholder="<emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"/>" class="treeInput" name="userName" value="<%=StringUtils.defaultIfEmpty(userName,"")%>" onclick="showUserMenu()" readonly/>&nbsp;
											</div>
										</td>
									</tr>
									<tr>
										<td><emp:message key="cxtj_sjcx_grsjx_jg" defVal="机构" fileName="cxtj"/>：</td>
										<td>
											<input type="hidden" id="depIdStr" name="depIdStr" value="<%=StringUtils.defaultIfEmpty(depIdStr,"")%>"/>
											<input type="text" title="" id="depName" placeholder="<emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"/>" class="treeInput" name="depName" value="<%=StringUtils.defaultIfEmpty(depName,"")%>" onclick="showDepMenu()" readonly/>&nbsp;
										</td>
									</tr>
									<tr>
										<td><emp:message key="cxtj_sjfx_tips22" defVal="国内运营商" fileName="cxtj"/>：</td>
										<td>
											<select title="" id="DomesticOperator" name="DomesticOperator">
												<option value=""><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"/></option>
												<option value="0" <%="0".equals(domesticOperator)? "selected":""%>><emp:message key="cxtj_sjcx_xtsxjl_yd" defVal="移动" fileName="cxtj"/></option>
												<option value="1" <%="1".equals(domesticOperator)? "selected":""%>><emp:message key="cxtj_sjcx_xtsxjl_lt" defVal="联通" fileName="cxtj"/></option>
												<option value="21" <%="21".equals(domesticOperator)? "selected":""%>><emp:message key="cxtj_sjcx_xtsxjl_dx" defVal="电信" fileName="cxtj"/></option>
											</select>
										</td>
									</tr>
								</table>
								<div class="buttonDiv">
									<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" class="btnClass5 mr23" onclick="confirmMoreDimension()"/>
									&nbsp;&nbsp;
									<input type="button" value="<emp:message key='common_cancel' defVal='取消' fileName='common'/>" class="btnClass6" onclick="closeMoreDimension()"/>
								</div>
							</div>
						</div>
						<table id="content">
							<thead>
							    <tr>
							         <%
							 iter = excelConditionMap.entrySet().iterator();
							 while (iter.hasNext())
							{
	 							e = iter.next();
							  %>
							  <th><%=e.getKey() %></th>
						    <%
						    
						    }
						    
						     %>
						      <%
								List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
								String temp = null;
								for(RptConfInfo rptConf : rptConList){
									temp = rptConf.getName();
								%>
								<th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
								<%} %>
						     <th><emp:message key="cxtj_sjcx_yystjbb_xq" defVal="详情" fileName="cxtj"/></th>
						        </tr>
							</thead>
							<tbody>
								<%
								if (reportList != null && reportList.size() > 0)
								{
									for(SpMtDataReportVo report : reportList)
									{
							%>
								<tr>								    
									<td>
									<%
									String year = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request);
									String month = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request);
									String day = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
									String wz = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
									String z = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request);
									
									String showTime = "";
									String begintime="";
									String endtime="";
									if("2".equals(reportType)){
									String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
									 begintime=btime;
									String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
									endtime=etime;
									//if(!"".equals(btime) && null != btime && 0 != btime.length())
									//{
										//String btemp[] = btime.split("-");
										//btime = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
										//btime = btemp[0] + year + btemp[1] + month + btemp[2] + day;
									//}

									//if(!"".equals(etime) && null != etime && 0 != etime.length())
									//{
										//String etemp[] = etime.split("-");
										//etime = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
										//etime = etemp[0] + year + etemp[1] + month + etemp[2] + day;
									//}
									//showTime = btime + " 至 " + etime;
									  showTime = btime + "  "+ z +"  " + etime;
									}else if("0".equals(reportType)){
										//showTime=report.getY()+"年"+report.getImonth()+"月"	;
										//showTime=report.getY() + year + report.getImonth()+month;
										showTime=report.getY()+"-"+report.getImonth();
									}else {
										//showTime=report.getY()+"年";
										//showTime=report.getY()+year;
										showTime=report.getY();
									}
									%>
											<%=showTime%>
									</td>	
																	
									<td>
										<%--<xmp><%=report.getUserid()==null?"未知":report.getUserid()%></xmp> --%>
										<xmp><%=report.getUserid()==null?wz:report.getUserid()%></xmp>
										
									</td>
									<td><xmp><%=report.getStaffname()!=null?report.getStaffname():"--" %></xmp></td>
									<%--<td><xmp><%=report.getSptype()!=null?(report.getSptype()==1?"EMP应用":"EMP接入"):"--" %></xmp></td> --%>
									<td><xmp><%=report.getSptype()!=null?(report.getSptype()==1?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_empyy", request):MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_empjr", request)):"--" %></xmp></td>
									<%
									String sendtypename="";
									if(report.getSptype()!=null){
										sendtypename=report.getSptype()==1?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_empyy", request):MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_empjr", request);
										if(report.getSendtype()!=null){
											if(report.getSendtype()==1){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_fs", request)+")";
											}else if(report.getSendtype()==2){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_httpjr", request)+")";
											}else if(report.getSendtype()==3){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_dbjr", request)+")";
											}else if(report.getSendtype()==4){
												sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_zljr", request)+")";
											}
										}
									}
									
									%>
									<td><xmp><%=sendtypename %></xmp></td>
									<td>
										<%
										if("".equals(request.getAttribute("spisuncm"))){
											//out.print("全部");
											out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qb", request));
										}else if(report.getSpisuncm()!=null){
											if("5".equals(report.getSpisuncm())){
												//out.print("国外");
												out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gw", request));
											}else if("0".equals(report.getSpisuncm())||"1".equals(report.getSpisuncm())||"21".equals(report.getSpisuncm())){
												//out.print("国内");
												out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gn", request));
											}else{
												//out.print("未知");
												out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request));
											}
											
										}else{
											//out.print("未知");
											out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request));
										}%>
									</td>
									<%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> map=ReportBiz.getRptNums(report.getIcount(),report.getRsucc(),report.getRfail1(),
	 									report.getRfail2(),report.getRnret(),RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
									if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//提交总数
								%>
								<td><%=map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功数
								%>
								<td><%=map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败数
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败数
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功数
								%>
								<td><%=map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//未返数
								%>
								<td><%=map.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功率
								%>
								<td><%=map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败率
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功率
								%>
								<td><%=map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败率
								%>
								<td><%=map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//未返率
								%>
								<td><%=map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
								<%}
								} %>
									
									<td>
										<a onclick="detail('<%=report.getUserid()==null?"--":report.getUserid().toUpperCase()%>','<%=report.getSptype()!=null?report.getSptype():"--" %>','<%=report.getUserid()!=null?report.getUserid():"--" %>','<%=report.getSendtype()%>','<%=begintime%>','<%=endtime %>','<%=reportType%>','<%=msType%>','<%=counTime%>')"><emp:message key="cxtj_sjcx_yystjbb_ck" defVal="查看" fileName="cxtj"/></a>
										<%if(!"0".equals(report.getSpisuncm())&&!"1".equals(report.getSpisuncm())&&!"21".equals(report.getSpisuncm())){ %>
										<a onclick="sendInfo('<%=report.getUserid()==null?"--":report.getUserid().toUpperCase()%>','<%=report.getSptype()!=null?report.getSptype():"--" %>','<%=report.getUserid()!=null?report.getUserid():"--" %>','<%=report.getSendtype()%>','<%=begintime%>','<%=endtime %>','<%=reportType%>','<%=msType%>','<%=counTime%>')"><emp:message key="cxtj_sjcx_yystjbb_ggfs" defVal="各国发送" fileName="cxtj"/></a>
									<%}%>
									</td>
								</tr>
								<%
								}
								%>
								<tr>
									<td colspan="6"><b><emp:message key="cxtj_sjcx_yystjbb_hj" defVal="合计：" fileName="cxtj"/></b></td>
								    <%
	 							//从公用方法中获取 计算结果map
	 							Map<String,String> mapsum=ReportBiz.getRptNums(sumCount[0],sumCount[1],sumCount[2],
	 									sumCount[3],sumCount[4],RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
								for(RptConfInfo rptConf : rptConList)
								{
									if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//提交总数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
									{//未返数
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RNRET_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送成功率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
									{//发送失败率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收成功率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//接收失败率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):0 %></td>
								<%
									}
									else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
									{//未返率
								%>
								<td><%=mapsum.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?mapsum.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):0 %></td>
								<%}
								} %>
									<td>-</td>
								</tr>
								<%}else{
							%> 
							<tr>
									<td colspan="<%=7+rptConList.size() %>" align="center"><%=strshow %></td>
									
								</tr>
							<%} %>
							
							</tbody>
							<tfoot>
								<tr>
									<td colspan="<%=7+rptConList.size() %>">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			 </form>
			<%-- foot结束 --%>
		</div>
    	<div class="clear"></div>
  		<div id="tempDiv" title="<%=zhxxxx %>">
			<iframe id="contentFrame" name="contentFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
		</div>
		<div id="sendInfoDiv" title="<%=ggfsxxxx %>">
			<iframe id="sendInfoFrame" name="sendInfoFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
		</div>
		<%--操作员选择框--%>
		<div id="selectUser" style="display:none;">
			<div class="dropMenu2_div2"><font class="zhu"><emp:message key="common_chooseOperator" defVal="注：请勾选操作员进行查询" fileName="common"/></font></div>
			<div class="showUserTree"><ul id="dropdownMenu2" class="tree"></ul></div>
			<div>
				<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" class="btnClass5 mr23 selectUserConfirm" onclick="zTreeUserOnClickOK()"/>
				&nbsp;&nbsp;
				<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'/>" class="btnClass6" onclick="zTreeUserOnClickClean()"/>
			</div>
		</div>
		<%--机构选择框--%>
		<div id="selectDep" style="display:none;">
			<div class="isContainsSun">
				<input type="checkbox" title="" id="isContainsSun" <%="1".equals(containSubDep) ? "checked":""%> name="isContainsSun" onchange="setContain()"/>
				<span><emp:message key="common_containsSun" defVal="包含子机构" fileName="common"/></span>
			</div>
			<div class="showDepTree"><ul  id="dropdownMenu" class="tree"></ul></div>
			<div>
				<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" class="btnClass5 mr23 selectUserConfirm" onclick="zTreeDepOnClickOK()"/>
				&nbsp;&nbsp;
				<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'/>" class="btnClass6" onclick="zTreeDepOnClickClean()"/>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/layer/layer.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript">
				$(document).ready(function(){
					$("#msType").change(function(){
						if ($("#msType").attr("value") == "0")
						{
							$("#userIds").empty();
							$("#userIds").append("<option value=\"\">"+getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_5")+"</option>");
							<%
								if (userList != null && userList.size() > 0){
									for(int k=0;k<userList.size();k++){
							%>
										$("#userIds").append("<option value=\"<%=userList.get(k) %>\" <%= userList.get(k).equals(request.getParameter("userId"))?"selected":"" %>><%=userList.get(k) %></option>");
							<%
									}
								}
							%>
							$('#userIds').next('.c_selectBox').remove();
							$('#userIds').isSearchSelect({'width':'180','zindex':0},function(data){
								//keyup click触发事件
									$("#userId").val(data.value);
							},function(data){
								//初始化加载
								var val=$("#userId").val();
								if(val){
									data.box.input.val(val);
								}
							});
						}
						else
						{
									$("#userIds").empty();
									$("#userIds").append("<option value=\"\">"+getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_5")+"</option>");
									<%
										if (mmsUserList != null && mmsUserList.size() > 0){
											for(int k=0;k<mmsUserList.size();k++){
									%>
												$("#userIds").append("<option value=\"<%=mmsUserList.get(k) %>\" <%=mmsUserList.get(k).equals(request.getParameter("userId"))?"selected":"" %>><%=mmsUserList.get(k) %></option>");
									<%
											}
										}
									%>
									$('#userIds').next('.c_selectBox').remove();
									$('#userIds').isSearchSelect({'width':'180','zindex':0},function(data){
										//keyup click触发事件
											$("#userId").val(data.value);
									},function(data){
										//初始化加载
										var val=$("#userId").val();
										if(val){
											data.box.input.val(val);
										}
									});
									//alert($("#userIds").html());

						}
					});
					initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				});

				function detail(userId,sptype,staffname,sendtype,btime,etime,reportType,msType,countTime){
					staffname=encodeURI(encodeURI(staffname));
					var back_staffname='<%=request.getParameter("staffname")%>';
					back_staffname=encodeURI(encodeURI(back_staffname));
					location.href ="<%=path%>/rep_spMtReport.htm?method=detailInfo&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&spisuncm=<%=request.getParameter("spisuncm")%>&lguserid=<%=request.getParameter("lguserid")%>&begintime="+btime+"&userId="+userId+"&staffname="+staffname+"&sptype="+sptype+"&sendtype="+sendtype+"&endtime="+etime+"&reportType="+reportType+"&msType="+msType+"&countTime="+countTime+"&timee="+new Date().getTime();
				}
				function sendInfo(userId,sptype,staffname,sendtype,btime,etime,reportType,msType,countTime){
					staffname=encodeURI(encodeURI(staffname));
					var back_staffname='<%=request.getParameter("staffname")%>';
					back_staffname=encodeURI(encodeURI(back_staffname));
					location.href ="<%=path%>/rep_spMtReport.htm?method=nationSendInfo&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&spisuncm=<%=request.getParameter("spisuncm")%>&lguserid=<%=request.getParameter("lguserid")%>&begintime="+btime+"&userId="+userId+"&staffname="+staffname+"&sptype="+sptype+"&sendtype="+sendtype+"&endtime="+etime+"&reportType="+reportType+"&msType="+msType+"&countTime="+countTime+"&timee="+new Date().getTime();
				}
		</script>
		<script type="text/javascript" src="<%=commonPath%>/cxtj/report/js/rep_spMtReportDedail.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
