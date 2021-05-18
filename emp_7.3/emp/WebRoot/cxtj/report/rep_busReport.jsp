<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page
        import="com.montnets.emp.report.biz.RptConfBiz" %>
<%@ page
        import="com.montnets.emp.report.vo.BusReportVo" %>
<%@ page
        import="com.montnets.emp.util.PageInfo" %>
<%@ page
        import="java.text.SimpleDateFormat" %>
<%@ page
        import="java.util.Calendar" %>
<%@page import="java.util.HashMap"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>   
<%@include file="/common/common.jsp" %>    
<%
	String path = request.getContextPath();
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
	PageInfo pageInfo = new PageInfo();
	if (request.getAttribute("pageInfo")!=null){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}

    long[] sumArray = (long[]) session.getAttribute("bus_sumArray");
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//Date date = new Date();
	Calendar c=Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH,-1);
	String  beginTime= df.format(c.getTime()).substring(0, 8)+"01";
	//String  endTime =df.format(date).substring(0, 8)+"07" ;
	
	String  endTime =df.format(c.getTime()).substring(0, 11) ;  //change by dj

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	
	@SuppressWarnings("unchecked")	
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	
	@SuppressWarnings("unchecked")	
	List<BusReportVo> resultList = (List<BusReportVo>) request.getAttribute("resultList");
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList=(List<LfBusManager>)request.getAttribute("busList");
	@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefiledusers");
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String menuCode = titleMap.get("busReport");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String msType =(String)request.getParameter("msType");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String counTime = "";
	if(request.getAttribute("countTime")!=null){
		counTime=request.getAttribute("countTime").toString();
	}
	 String reportType=""; 
	   if(request.getAttribute("reportType")!=null){
		   reportType=request.getAttribute("reportType").toString();
	   }
		String spisuncm="100";//默认是国内
		if(request.getParameter("spisuncm")!=null){
			spisuncm=request.getParameter("spisuncm");
		}

MessageUtils messageUtils = new MessageUtils();
//时间
    String sj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request);
	//业务类型
	String ywlx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_ywlxtjbb_ywlx", request);
    if(ywlx!=null&&ywlx.length()>1){
    	ywlx = ywlx.substring(0,ywlx.length()-1);
    }
	//发送类型
	String fslx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_fslx", request);
    if(fslx!=null&&fslx.length()>1){
    	fslx = fslx.substring(0,fslx.length()-1);
    }
	//运营商
	String yys = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_yys", request);
    if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%--业务类型统计报表--%>
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
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rep_busReport">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<%
				if (btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
				
				<form name="pageForm" action="rep_busReport.htm"  method="post" id="pageForm">
  					<input type="hidden"  id="sp" name="sp" value="<%=request.getAttribute("sp")==null ? "" :request.getAttribute("sp")%>"/>      					
					<div class="buttons">
						<div id="toggleDiv">
						</div>
							<a id="exportCondition" ><emp:message key="cxtj_sjcx_yystjbb_dc" defVal="导出" fileName="cxtj"></emp:message></a>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
								<td><emp:message key="cxtj_sjcx_ywlxtjbb_ywlx" defVal="业务类型：" fileName="cxtj"></emp:message></td>
								<td>
									<select id="bustype" name="bustype">
									<option value=""><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"></emp:message></option>
									<option value="-1" <%if("-1".equals(request.getParameter("bustype"))){ %> selected="selected" <%} %>>-(<emp:message key="cxtj_sjcx_ywlxtjbb_wywlx" defVal="无业务类型" fileName="cxtj"></emp:message>)</option> 
									<% 
									if(busList!=null&busList.size()>0){
										for(int i=0;i<busList.size();i++){
											LfBusManager bus=busList.get(i);
											if(bus==null){
												bus=new LfBusManager();
											}
											String buscode=bus.getBusCode()==null?"":bus.getBusCode();
									%>
										<option value="<%=buscode %>" <%if(buscode.equals(request.getParameter("bustype"))){ %> selected="selected" <%} %>>
											<%=bus.getBusName().replace("<","&lt;").replace(">","&gt;") + "("+bus.getBusCode()+")" %>
										</option>
									<%
										}
									}
									 %>
								</select>
								</td>
								<td>
									<emp:message key="cxtj_sjcx_zhtjbb_fslx" defVal="发送类型 ：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<select name="datasourcetype" id="datasourcetype">
											<%
											if(btnMap.get(menuCode + "-1") != null&&btnMap.get(menuCode + "-2") != null){
											 %>
												<option value="0" <%if("0".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"></emp:message></option>
											<%
											}
											if(btnMap.get(menuCode + "-1") != null){
											 %>
												<option value="1" <%if("1".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_fs" defVal="EMP发送" fileName="cxtj"></emp:message></option>
											<%
											}
											if(btnMap.get(menuCode + "-2") != null){
											 %>
												<option value="2" <%if("2".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_httpjr" defVal="HTTP接入" fileName="cxtj"></emp:message></option>
											<%
											}
											 if(btnMap.get(menuCode + "-3") != null){
											 %>
												<option value="3" <%if("3".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_dbjr" defVal="DB接入" fileName="cxtj"></emp:message></option>
											<%
											}
											 if(btnMap.get(menuCode + "-4") != null){
											 %>
												<option value="4" <%if("4".equals(request.getParameter("datasourcetype"))){ %> selected="selected" <%} %>><emp:message key="cxtj_sjcx_zhtjbb_zljr" defVal="直连接入" fileName="cxtj"></emp:message></option>
											<%
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
								<td>
								<emp:message key="cxtj_sjcx_yystjbb_xxlx" defVal="信息类型" fileName="cxtj"/>
								</td>
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
										<select name="msType" id="msType">
											<option value="0" <%="0".equals(msType == null ? "" : msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_dx" defVal="短信" fileName="cxtj"/></option>
											<option value="1" <%="1".equals(msType == null ? "" : msType)?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_cx" defVal="彩信" fileName="cxtj"/></option>
										</select>
								</td>   
									
									<td class="tdSer">
												<center><a id="search" name="research"></a></center>
									</td>
								</tr>
								<tr>
								  <td><emp:message key="cxtj_sjcx_yystjbb_yys" defVal="运营商：" fileName="cxtj"></emp:message></td>
										<td>
										   <select id="spisuncm" name="spisuncm">
												<option value=""><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"></emp:message></option>
												<option value="100" <%="100".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_zhtjbb_gn" defVal="国内" fileName="cxtj"></emp:message></option>
												<option value="5" <%="5".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_zhtjbb_gw" defVal="国外" fileName="cxtj"></emp:message></option>
											</select>
										</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>										
								</tr>
								<tr>
								<td><emp:message key="cxtj_sjcx_zhtjbb_bblx" defVal="报表类型：" fileName="cxtj"></emp:message></td>
									<td>
										<select name="reportType" id="reportType">
											<option value="2" <%if("2".equals(reportType)||"".equals(reportType)) {
                                                out.print("selected");
                                            }
                                            %>><emp:message key="cxtj_sjcx_yystjbb_rbb" defVal="日报表" fileName="cxtj"></emp:message></option>
										     <option value="0" <%if("0".equals(reportType)) {
                                                 out.print("selected");
                                             }
                                             %>><emp:message key="cxtj_sjcx_yystjbb_ybb" defVal="月报表" fileName="cxtj"></emp:message></option>
										     <option value="1" <%if("1".equals(reportType)) {
                                                 out.print("selected");
                                             }
                                             %>><emp:message key="cxtj_sjcx_yystjbb_nbb" defVal="年报表" fileName="cxtj"></emp:message></option>
										</select>
									</td>  
									<td id="titleTime1">
										<emp:message key="cxtj_sjcx_yystjbb_tjsj" defVal="统计时间：" fileName="cxtj"></emp:message>
									</td>
									<td class="tableTime" id="titleTime2">
										<input type="text"
											class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>" id="sendtime"
											name="begintime">
									</td>
									<td id="titleTime3">
										<emp:message key="cxtj_sjcx_yystjbb_z" defVal="至：" fileName="cxtj"></emp:message>
									</td>
									<td id="titleTime4">
										<input type="text"
											class="Wdate" readonly="readonly" onclick="retime()"
											value="<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>" id="recvtime"
											name="endtime">
									</td>
									<td id="titleYear1" style="display:none"><emp:message key="cxtj_sjcx_zhtjbb_tjny" defVal="统计年月：" fileName="cxtj"></emp:message></td>
									<td id="titleYear2" style="display:none">
										<input type="hidden" value="month" name="yearOrMonth" />
										<input type="text" name="countTime" id="countTime"
														value="<%=counTime %>"
														onclick="showDate()"
														readonly="readonly" class="Wdate"/>
														<input type="hidden" id="tTime" value="<%=counTime %>"/>
									</td>
									<td id="titleYear3" style="display:none">&nbsp;</td>
									<td id="titleYear4" style="display:none">&nbsp;</td>
									<td>&nbsp;</td>
								</tr>

							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
							   <th width="240px"><%=sj %></th>
							   <th><%=ywlx %></th>
							   <th><%=fslx %></th>
							  	<th><%=yys %></th>
                                <%
                                    List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                    //总列数
                                    int cols = 5+rptConList.size();
                                    String temp = null;
                                    for(RptConfInfo rptConf : rptConList)
                                    {
                                    	temp = rptConf.getName();
                                %>
                                <th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
                                <%  } %>
							   <th class="detailTh"><emp:message key="cxtj_sjcx_yystjbb_xq" defVal="详情" fileName="cxtj"></emp:message></th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							    <tr><td colspan="<%=cols%>" class="queryData"><emp:message key="cxtj_sjcx_yystjbb_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"></emp:message></td></tr>
											
							<%}else if(resultList!=null&& resultList.size()>0){
								Map<String,String> sumCountMap = new HashMap<String, String>();
                                for(int i=0;i<resultList.size();i++){
									BusReportVo busreportVo = resultList.get(i);
								
							%>							
							<tr>
							<%
							
							//时间格式显示处理  eg：2012年12月12日 
							String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
							String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
							String showTime = "";
							if("2".equals(reportType)){
							if( !"".equals(btime) && null != btime && 0 != btime.length())
							{
								String btemp[] = btime.split("-");
								
								//btime = btemp[0]+"年"+btemp[1]+"月"+btemp[2]+"日";
								btime = btemp[0]+"-"+btemp[1]+"-"+btemp[2];
								//btime = btemp[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+btemp[1]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request)+btemp[2]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
						 	 		 
							}
							
							if( !"".equals(etime) && null != etime && 0 != etime.length() )
							{
								String etemp[] = etime.split("-");
								
								//etime = etemp[0]+"年"+etemp[1]+"月"+etemp[2]+"日";
								etime = etemp[0]+"-"+etemp[1]+"-"+etemp[2];
								//etime = etemp[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+etemp[1]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request)+etemp[2]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
							}
							showTime = btime+"  "+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  "+etime;
							
							}else if("0".equals(reportType)){
								if(counTime!=null){
									String time[]=counTime.split("-");
									//showTime=time[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+time[1]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request);
									showTime=time[0]+"-"+time[1];
								}else{
									//showTime="未知";
									showTime=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
								}
							}else {
								if(counTime!=null){
									String time[]=counTime.split("-");
									//showTime=time[0]+"年";
									showTime=time[0];
								}else{
									//showTime="未知";
									showTime=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
								}
							}
							
							session.setAttribute("bus_showTime",showTime);
							%>
								<td width="240px"><%=showTime%></td>
								<td>
									<xmp><%=busreportVo.getBusName()==null?"-":busreportVo.getBusName().replace("<","&lt;").replace(">","&gt;")%></xmp>
								</td>
								<td>
								<%
								if("0".equals(request.getParameter("datasourcetype"))){ 
								%>
									<emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"></emp:message>
								<%
								}else if("1".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_zhtjbb_fs" defVal="EMP发送" fileName="cxtj"></emp:message>
								<%
								}else if("2".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_zhtjbb_httpjr" defVal="HTTP接入" fileName="cxtj"></emp:message>
								<%
								}else if("3".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_zhtjbb_dbjr" defVal="DB接入" fileName="cxtj"></emp:message>
								<%
								}else if("4".equals(request.getParameter("datasourcetype"))){
								%>
								<emp:message key="cxtj_sjcx_zhtjbb_zljr" defVal="直连接入" fileName="cxtj"></emp:message>
								<%
								}else{
								 %>
								 --
								 <%
								 } 
								 %>
								</td>
								<td>
								<%
								if("".equals(spisuncm)){
									out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qb", request));
								}else if(!"".equals(spisuncm)){
									if("5".equals(spisuncm)){
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gw", request));
									}else if("100".equals(spisuncm)){
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_gn", request));
									}else{
										out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request));
									}
								}else{
									out.print(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request));
								}%>
								</td>						
                                <%
                                    sumCountMap = ReportBiz.getRptNums(busreportVo.getIcount(),busreportVo.getRsucc(),busreportVo.getRfail1(),busreportVo.getRfail2(),busreportVo.getRnret(),RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                    for(RptConfInfo rptConf : rptConList)
                                    {
                                %>
                                <td><%=sumCountMap.get(rptConf.getColId())%></td>
                                <%
                                    }
                                %>
	 							<td>
	 							<%-- 当业务类型busreportVo.getBusName()为null时候需要传个值 --%>
	 							<a onclick="detail('<%=busreportVo.getBusName()==null?"-1":busreportVo.getBusCode()%>','<%=request.getParameter("datasourcetype")%>')"><emp:message key="cxtj_sjcx_yystjbb_ck" defVal="查看" fileName="cxtj"></emp:message></a>
	 							&nbsp;&nbsp;&nbsp;
	 							<% if("5".equals(busreportVo.getSpisuncm())){%>
	 							<a onclick="sendInfo('<%=busreportVo.getBusName()==null?"-1":busreportVo.getBusCode()%>','<%=request.getParameter("datasourcetype")%>')"><emp:message key="cxtj_sjcx_jgtjbb_ggxq" defVal="各国发送" fileName="cxtj"></emp:message></a>
	 							<%}%>
	 							</td>
							</tr>
						<%} %>
							<tr>
						    <td colspan="4"><b><emp:message key="cxtj_sjcx_yystjbb_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>
                            <%
                                if(sumArray != null && sumArray.length >=5)
                                {
                                    sumCountMap = ReportBiz.getRptNums(sumArray[0],sumArray[1],sumArray[2],sumArray[3],sumArray[4],RptStaticValue.BUS_RPT_CONF_MENU_ID);
                                }
                                for(RptConfInfo rptConf : rptConList)
                                {
                                %>
                                <td><%=sumCountMap.get(rptConf.getColId())%></td>
                                <%
                                }
                                %>
						     <td>-</td>
						    </tr>	
						<%}else{						%>
						<tr>
							<td colspan="<%=cols%>" class="queryData"><emp:message key="cxtj_sjcx_yystjbb_wjj" defVal="无记录" fileName="cxtj"></emp:message></td>
							
						</tr>
						<%} %>
												
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="<%=cols%>">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="corpCode" class="hidden"></div>
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
					<div id="pageInfo"></div>
					</div>
				</div>
			</div>
		</div>
		<%-- foot结束 --%>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"	src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript">
		
		function showDate()
		{
			var r = $("#reportType").attr("value");
			if (r == 0)
			{
				//WdatePicker({skin:'simple',dateFmt:'yyyy-MM',isShowClear:true});
				WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});
			}
			else if (r == 1)
			{
				//WdatePicker({skin:'simple',dateFmt:'yyyy',isShowClear:true});
				WdatePicker({dateFmt:'yyyy',isShowClear:false});
			}else{
				WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});
			}
		}
				
		//开始时间
		function setime(){
		    var max = "2099-12-31";
		    var v = $("#recvtime").attr("value");
		    var min = "1900-01-01";
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:v});

		}
		//发送起止时间控制
		function retime(){
		    var max = "2099-12-31";
		    var v = $("#sendtime").attr("value");
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max});

		}
		
		$(document).ready(function(){
		getLoginInfo("#corpCode");
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

			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			var checkSubmitFlag = true;
			$('#search').click(function(){
			
					var judgeSendtime = $("#sendtime").val();//开始时间
					
					var judgeendtime = $("#recvtime").val();//结束时间
					
					var datasourcetype = $("#datasourcetype").val();//数据源
				
					if(datasourcetype==null||""==datasourcetype)
					{
						//alert("您当前的角色未赋予查看[EMP应用、接入类]的数据权限");	
						alert(getJsLocaleMessage("cxtj","cxtj_sjcx_ywlxtjbb_text_1"));
						return;
					}
					
					
					if("" == $.trim(judgeSendtime) || "" == $.trim(judgeendtime) )
					{
						//alert("请选择开始时间和结束时间！");		
						alert(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_4"));
					}else
					{
					    //submitForm();//选择好时间段，才允许查询 
					    checkSubmit();
					}
			
			});
			
		    var reportType="<%=reportType%>";
			if (reportType == "1")
			{
				$("#countTime").attr("value",$("#tTime").val().substring(0,4));
				$("#titleYear1").css("display", "");
				$("#titleYear2").css("display", "");
				$("#titleYear3").css("display", "");
				$("#titleYear4").css("display", "");
				
				$("#titleTime1").css("display", "none");
				$("#titleTime2").css("display", "none");
				$("#titleTime3").css("display", "none");
				$("#titleTime4").css("display", "none");
			}
			else if(reportType == "0")
			{
				$("#countTime").attr("value",$("#tTime").val());
				$("#titleYear1").css("display", "");
				$("#titleYear2").css("display", "");
				$("#titleYear3").css("display", "");
				$("#titleYear4").css("display", "");
				
				$("#titleTime1").css("display", "none");
				$("#titleTime2").css("display", "none");
				$("#titleTime3").css("display", "none");
				$("#titleTime4").css("display", "none");
			}else{
				$("#titleYear1").css("display", "none");
				$("#titleYear2").css("display", "none");
				$("#titleYear3").css("display", "none");
				$("#titleYear4").css("display", "none");
				
				$("#titleTime1").css("display", "");
				$("#titleTime2").css("display", "");
				$("#titleTime3").css("display", "");
				$("#titleTime4").css("display", "");
			}
			
			$("#reportType").change(function(){
				var t = $("#countTime").attr("value");
				
				if ($("#reportType").attr("value") == "1")
				{
					$("#countTime").attr("value",$("#tTime").val().substring(0,4));
					$("#titleYear1").css("display", "");
					$("#titleYear2").css("display", "");
					$("#titleYear3").css("display", "");
					$("#titleYear4").css("display", "");
					
					$("#titleTime1").css("display", "none");
					$("#titleTime2").css("display", "none");
					$("#titleTime3").css("display", "none");
					$("#titleTime4").css("display", "none");
//					 $("#spisuncm").val("100");
				}
				else if($("#reportType").attr("value") == "0")
				{
					$("#countTime").attr("value",$("#tTime").val());
					$("#titleYear1").css("display", "");
					$("#titleYear2").css("display", "");
					$("#titleYear3").css("display", "");
					$("#titleYear4").css("display", "");
					
					$("#titleTime1").css("display", "none");
					$("#titleTime2").css("display", "none");
					$("#titleTime3").css("display", "none");
					$("#titleTime4").css("display", "none");
//					 $("#spisuncm").val("100");
					// $("#spisuncm2").find("option[text='100']").attr("selected",true);
				}else{
					$("#titleYear1").css("display", "none");
					$("#titleYear2").css("display", "none");
					$("#titleYear3").css("display", "none");
					$("#titleYear4").css("display", "none");
					
					$("#titleTime1").css("display", "");
					$("#titleTime2").css("display", "");
					$("#titleTime3").css("display", "");
					$("#titleTime4").css("display", "");
//					 $("#spisuncm").val("100");
				}
			});
			
			function checkSubmit(){
			    if(checkSubmitFlag ==true){
			         submitForm();//选择好时间段，才允许查询 	
			         //$("a[name='research']").attr("title","您已经点过了查询按钮，请稍等!");	    
			           $("a[name='research']").attr("title",getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_5"));  
			         checkSubmitFlag = false;
			    }
			    else{
			      // $("a[name='research']").attr("title","您已经点过了查询按钮，请稍等!");		      
			    }
			}

			
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {
				
				  //if(confirm("确定要导出数据到excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_2")))
				   {
				   		<%
				   		if(resultList!=null && resultList.size()>0){
				   		%>			
				   		//var queryTime=encodeURIComponent(encodeURIComponent($("#queryTime").val()));
				   		var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
				   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
				   		var datasourcetype = '<%=request.getParameter("datasourcetype")==null ? "" :request.getParameter("datasourcetype") %>';
				   	    var bustype = '<%=request.getParameter("bustype")==null ? "" :request.getParameter("bustype")%>';
				   	    var mstype = '<%=msType==null?"":msType %>';
				   		//alert(depNam);
				   		$.ajax({
							type: "POST",
							url: "<%=path%>/rep_busReport.htm?method=r_busRptExportExcel",
							data: {
										isdeail:"no",
										reportType:'<%=request.getParameter("reportType")%>',
										lgcorpcode:'<%=request.getParameter("lgcorpcode")%>',
										spisuncm:'<%=request.getParameter("spisuncm")%>',
										countTime:'<%=request.getParameter("countTime")%>',
										begintime:sendtime,
										endtime:recvtime,
										datasourcetype:datasourcetype,
										bustype:bustype,
										msType:mstype
							},
						    beforeSend:function () {
						    	page_loading();
						  	},
						    complete:function () {
						        page_complete()
						    },
						    success:function (result) {
						        if (result == 'true') {
						            download_href("<%=path%>/rep_busReport.htm?method=bus_downloadFile");
						        } else {
						           // alert('导出失败！');
						            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
						        }
			           		}
			           	});
				   			//location.href="<%=path%>/rep_busReport.htm?method=r_busRptExportExcel&isdeail=no&reportType=<%=request.getParameter("reportType")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&spisuncm=<%=request.getParameter("spisuncm")%>&countTime=<%=request.getParameter("countTime")%>&lguserid=<%=request.getParameter("lguserid")%>&begintime="+sendtime+"&endtime="+recvtime+"&datasourcetype="+datasourcetype+"&bustype="+bustype+"&msType="+mstype;
				   		
				   		<%	
				   		}else{
				   		%>
				   		//alert("无数据可导出！");
				   		  alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));
				   		<%
				   		}%>
				   }				 
			  });
		
		   
			
		});
		
		function detail(bustype,datasourcetype){
			location.href ="<%=path%>/rep_busReport.htm?method=detailInfo&bustype="+bustype+"&msType=<%=request.getParameter("msType")%>&datasourcetype="+datasourcetype+"&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&reportType=<%=request.getParameter("reportType")%>&countTime=<%=request.getParameter("countTime")%>&begintime=<%=request.getParameter("begintime")%>&endtime=<%=request.getParameter("endtime")%>&spisuncm=<%=request.getParameter("spisuncm")%>";
		}
		function sendInfo(bustype,datasourcetype){
			location.href ="<%=path%>/rep_busReport.htm?method=busNation&bustype="+bustype+"&msType=<%=request.getParameter("msType")%>&datasourcetype="+datasourcetype+"&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&reportType=<%=request.getParameter("reportType")%>&countTime=<%=request.getParameter("countTime")%>&begintime=<%=request.getParameter("begintime")%>&endtime=<%=request.getParameter("endtime")%>&spisuncm=<%=request.getParameter("spisuncm")%>";
		}
		
		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}

			</script>
	</body>
</html>
