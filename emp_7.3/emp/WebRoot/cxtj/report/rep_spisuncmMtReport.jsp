<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.passage.XtGateQueue"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@ page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@ page import="com.montnets.emp.report.biz.ReportBiz" %>
<%@page import="com.montnets.emp.report.biz.RptConfBiz"%>
<%@page import="com.montnets.emp.report.vo.OperatorsMtDataReportVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
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
    PageInfo pageInfo = null;
    if(request.getAttribute("pageInfo")!=null){
        pageInfo = (PageInfo) request.getAttribute("pageInfo");
    }else{
        pageInfo=new PageInfo();
    }
    @ SuppressWarnings("unchecked")
    Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

    @ SuppressWarnings("unchecked")
    Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
    String menuCode = titleMap.get("spisuncmMtReport");
    menuCode = menuCode==null?"0-0-0":menuCode;
    OperatorsMtDataReportVo spisuncmMtDataReportVo = (OperatorsMtDataReportVo)request.getAttribute("spisuncmMtDataReportVo");
    @ SuppressWarnings("unchecked")
    List<OperatorsMtDataReportVo> reportList=(List<OperatorsMtDataReportVo>)request.getAttribute("reportList");
    @ SuppressWarnings("unchecked")
    List<OperatorsMtDataReportVo> smsuserList = (List<OperatorsMtDataReportVo>)session.getAttribute("smsspUserList");
    List<OperatorsMtDataReportVo> mmsuserList = (List<OperatorsMtDataReportVo>)session.getAttribute("mmsspUserList");
    List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pagefileds");
    @ SuppressWarnings("unchecked")
    List<XtGateQueue> xtList = (List<XtGateQueue>)session.getAttribute("mrXtList");

    long[] sumCount=(long[])request.getAttribute("sumCount");
    String spisuncm="";
    if(spisuncmMtDataReportVo.getSpisuncm() !=null)
    {
        spisuncm= spisuncmMtDataReportVo.getSpisuncm().toString();
    }
    else
    {
        spisuncm = "";
    }
    String staffId = "";
    if(spisuncmMtDataReportVo.getSpID() != null){
        staffId = spisuncmMtDataReportVo.getSpID();
    }else{
        staffId = "";
    }
    String counTime = request.getAttribute("countTime")==null?"":request.getAttribute("countTime").toString();
    Boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

    MessageUtils messageUtils = new MessageUtils();
//时间
    String sj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request);
//运营商账户ID
    String yyszhid = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_yyszhid", request);
    if(yyszhid!=null&&yyszhid.length()>1){
        yyszhid = yyszhid.substring(0,yyszhid.length()-1);
    }
//运营商类型
    String yyslx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_yyslx", request);
//发送成功数
    String fscgs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_fscgs", request);
//接收失败数
    String jssbs = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_jssbs", request);
//详情
    String xq = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_xq", request);

//excel自动识别用
    Map<String,String> excelConditionMap = new java.util.LinkedHashMap<String,String>();
    //excelConditionMap.put("年份","y");
    //excelConditionMap.put("时间","imonth");
    //excelConditionMap.put("运营商账户ID","spID");
    //excelConditionMap.put("运营商类型","spisuncm");
    //excelConditionMap.put("发送总数","icount");
    excelConditionMap.put(sj,"imonth");
    excelConditionMap.put(yyszhid,"spID");
    excelConditionMap.put(yyslx,"spisuncm");



    session.setAttribute("EXCEL_MAP",excelConditionMap);
    java.util.Iterator<Map.Entry<String, String>> iter = null;
    Map.Entry<String, String> e = null;
    String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

    //报表类型
    int reportType = 2;//默认日报表
    String startTime = request.getAttribute("startTime")==null?"":request.getAttribute("startTime").toString();
    String endTime = request.getAttribute("endTime")==null?"":request.getAttribute("endTime").toString();
    int msType = spisuncmMtDataReportVo!=null&&spisuncmMtDataReportVo.getMstype()!=null?spisuncmMtDataReportVo.getMstype():0;

    if(spisuncmMtDataReportVo!=null&&spisuncmMtDataReportVo.getReporttype()!=null){
        reportType = spisuncmMtDataReportVo.getReporttype();
    }
    boolean isDes = "1".equals(request.getParameter("isDes"));//是否详情页面

    //全部
    String qb = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_qb", request);
    //未知
    String wz = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
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
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
</head>

<body class="rep_spisuncmMtReport">
<div id="container" class="container">
    <%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>

    <%-- 内容开始 --%>
    <%if(btnMap.get(menuCode+"-0")!=null) { %>
    <form name="pageForm" action="rep_spisuncmMtReport.htm?method=find" method="post" id="pageForm">
        <div id="rContent" class="rContent">
            <div class="buttons">
                <%if(!isDes){ %>
                <div id="toggleDiv" >
                    <%-- <img id="searchIcon" src="<%=inheritPath %>/images/toggle_collapse.png" title="展开查询条件"/> --%>
                </div>
                <%} %>
                <%if(isDes){%>
                <span id="backgo" class="right mr5" onclick="javascript:back()">&nbsp;<emp:message key="cxtj_sjcx_yystjbb_fh" defVal="返回" fileName="cxtj"></emp:message></span>
                <%}%>
                <a id="exportCondition"><emp:message key="cxtj_sjcx_yystjbb_dc" defVal="导出" fileName="cxtj"></emp:message></a>
                <input type="hidden" id="menucode" value="24" name="menucode" />
                <input type="hidden" id="pageTotalRec" name="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
            </div>
            <div id="condition" class="<%=isDes?"yinc":"" %>">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <emp:message key="cxtj_sjcx_yystjbb_yyszhid" defVal="运营商账户ID：" fileName="cxtj"></emp:message>
                        </td>
                        <td>
                            <label>
                                <select name="staffId" id="staffId">
                                    <option value=""><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"></emp:message></option>
                                    <option value=" " <%= (" ").equals(staffId)?"selected":"" %>><emp:message key="cxtj_sjcx_zhtjbb_wz" defVal="未知" fileName="cxtj"></emp:message></option>
                                    <%
                                        if(spisuncmMtDataReportVo.getMstype()!=null&&spisuncmMtDataReportVo.getMstype()==1){
                                            if (mmsuserList != null && mmsuserList.size() > 0)
                                            {
                                                for(OperatorsMtDataReportVo userdata : mmsuserList)
                                                {
                                                    if(!("").equals(userdata.getSpID().trim())&&userdata.getSpID()!= null){
                                    %>
                                    <option value="<%=userdata.getSpID() %>" <%= userdata.getSpID().equals(staffId)?"selected":"" %>><%=userdata.getSpID() %></option>
                                    <%
                                                }
                                            }
                                        }
                                    }else{
                                        if (smsuserList != null && smsuserList.size() > 0)
                                        {
                                            for(OperatorsMtDataReportVo userdata : smsuserList)
                                            {

                                                if(!("").equals(userdata.getSpID().trim())&&userdata.getSpID()!= null){
                                    %>
                                    <option value="<%=userdata.getSpID() %>" <%= userdata.getSpID().equals(staffId)?"selected":"" %>><%=userdata.getSpID() %></option>
                                    <%
                                                    }
                                                }
                                            }
                                        }
                                    %>

                                </select>
                            </label>
                        </td>
                        <td><emp:message key="cxtj_sjcx_yystjbb_yys" defVal="运营商：" fileName="cxtj"></emp:message></td>
                        <td>
                            <select id="spisuncm" name="spisuncm" isInput="false">
                                <option value=""><emp:message key="cxtj_sjcx_yystjbb_qb" defVal="全部" fileName="cxtj"></emp:message></option>
                                <option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_dxzljl_yd" defVal="移动" fileName="cxtj"></emp:message></option>
                                <option value="1" <%="1".equals(spisuncm)?"selected":"" %> ><emp:message key="cxtj_sjcx_dxzljl_lt" defVal="联通" fileName="cxtj"></emp:message></option>
                                <option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_dxzljl_dx" defVal="电信" fileName="cxtj"></emp:message></option>
                                <option value="5" <%="5".equals(spisuncm)?"selected":"" %>><emp:message key="cxtj_sjcx_report_gw" defVal="国外" fileName="cxtj"></emp:message></option>
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
                            <%-- <%=typename %>--%>
                            <emp:message key="cxtj_sjcx_yystjbb_xxlx" defVal="信息类型" fileName="cxtj"/>
                        </td>

                        <td>
                            <%--
											<select name="msType" id="msType" style="width:180px">
											<%
											if(pagefileds!=null&&pagefileds.size()>1){
												for(int i=1;i<pagefileds.size();i++){
												LfPageField pagefid=pagefileds.get(i);
											%>
											     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(spisuncmMtDataReportVo.getMstype()==null?"":spisuncmMtDataReportVo.getMstype().toString())?"selected":"" %>><%=pagefid.getSubFieldName() %></option>
											<% 
												}
											}
											
											%>
											</select>--%>
                            <select name="msType" id="msType" isInput="false">
                                <option value="0" <%="0".equals(spisuncmMtDataReportVo.getMstype()==null?"":spisuncmMtDataReportVo.getMstype().toString())?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_dx" defVal="短信" fileName="cxtj"/></option>
                                <option value="1" <%="1".equals(spisuncmMtDataReportVo.getMstype()==null?"":spisuncmMtDataReportVo.getMstype().toString())?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_cx" defVal="彩信" fileName="cxtj"/></option>
                            </select>
                            <input type="hidden" id="msTypes" name ="msTypes" value="<%=msType%>" />
                        </td>

                        <td class="tdSer">
                            <center><a id="search"></a></center>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <emp:message key="cxtj_sjcx_yystjbb_bblx" defVal="报表类型 ：" fileName="cxtj"></emp:message>
                        </td>
                        <td id="timeSelect">
                            <select name="reportType" id="reportType" isInput="false">
                                <option value="2" <%=reportType==2?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_rbb" defVal="日报表" fileName="cxtj"></emp:message></option>
                                <option value="0" <%=reportType==0?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_ybb" defVal="月报表" fileName="cxtj"></emp:message></option>
                                <option value="1" <%=reportType==1?"selected":"" %>><emp:message key="cxtj_sjcx_yystjbb_nbb" defVal="年报表" fileName="cxtj"></emp:message></option>
                            </select>
                        </td>
                        <td>
                            <input type="hidden" value="month" name="yearOrMonth" />
                            <input type="hidden" value="2" name="count" />
                            <%--<span id="countSpan">统计年月</span>： --%>
                            <span id="countSpan"><emp:message key="cxtj_sjcx_yystjbb_tjsj" defVal="统计年月:" fileName="cxtj"></emp:message></span>：
                        </td>
                        <td>
                            <input type="text" name="countTime" id="countTime" value="" readonly="readonly" class="Wdate startdate"/>
                            <input type="hidden" id="tTime" value="<%=counTime %>"/>
                            <input type="hidden" id="startTime" value="<%=startTime %>"/>
                        </td>
                        <td><span class="novisible"><emp:message key="cxtj_sjcx_yystjbb_z" defVal="至：" fileName="cxtj"></emp:message></span></td>
                        <td>
                            <input type="text" name="endTime" value="" readonly="readonly" class="Wdate novisible enddate"/>
                            <input type="hidden" id="endTime"  value="<%=endTime%>"/>
                        </td>
                        <td>&nbsp;
                            <input type="hidden" name="isDes" id="isDes" value="<%=isDes?1:0 %>"/>
                            <input type="hidden" id="reportTypes" name ="reportTypes" value="<%=reportType %>" />
                        </td>
                    </tr>
                    </tbody>
                </table>
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
                    <%-- 读取可显示表头 --%>
                    <%
                        List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
                        String temp = null;
                        for(RptConfInfo rptConf : rptConList)
                        {
                            temp = rptConf.getName();
                    %>
                    <%--<th><%=rptConf.getName() %></th> --%>
                    <th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
                    <%} %>

                    <%if(!isDes){%><th><emp:message key="cxtj_sjcx_yystjbb_xq" defVal="详情" fileName="cxtj"></emp:message></th><%} %>
                </tr>
                </thead>
                <tbody>
                <%
                    if (reportList != null && reportList.size() > 0)
                    {
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+"M"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request)+"d"+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request));
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
                        //DateFormat df = DateFormat.getDateInstance();
                        String timestr = "";
                        if(reportType == 2){
                            //String startDate = sdf.format(df.parse(startTime));
                            //String endDate = sdf.format(df.parse(endTime));
                            //timestr = startDate+"至"+endDate;
                            String startDate="";
                            String endDate="";
                            if(!"".equals(startTime) && null != startTime && 0 != startTime.length())
                            {
                                //String btemp[] = startTime.split("-");
                                //startDate = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
                                //startDate = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + btemp[2] +MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
                                startDate = startTime;
                            }

                            if(!"".equals(endTime) && null != endTime && 0 != endTime.length())
                            {
                                //String etemp[] = endTime.split("-");
                                //endDate = etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
                                endDate =endTime;
                            }
                            timestr = startDate + "  " + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+ "  " + endDate;
                        }
                        for(OperatorsMtDataReportVo report : reportList)
                        {
                            // if(!report.getSpID().equals(""))
                %>
                <tr>
                    <td>
                        <%
                            if(!isDes){
                                if(reportType == 2){
                                    out.print(timestr);
                                }else if(reportType == 1){
                                    //out.print(report.getY()+"年");
                                    //out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request));
                                    out.print(report.getY());
                                }else{
                                    //out.print(report.getY()+"年"+report.getImonth()+"月");
                                    //out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+report.getImonth()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request));
                                    out.print(report.getY()+"-"+report.getImonth());
                                }
                            }else{
                                if(reportType == 2){
                                    out.print(sdf.format(sdf1.parse(report.getIymd())));
                                }else if(reportType == 1){
                                    out.print(report.getY()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)+report.getImonth()+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request));
                                }else{
                                    out.print(sdf.format(sdf1.parse(report.getIymd())));
                                }
                            }
                        %>
                    </td>
                    <td>
                        <%=report.getSpID().trim().equals("")?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request):report.getSpID()%>

                    </td>
                    <td>
                        <%
                            if(report.getSpisuncm()-0==0){%>
                        <emp:message key="cxtj_sjcx_dxzljl_yd" defVal="移动" fileName="cxtj"></emp:message>
                        <%}else if(report.getSpisuncm()-1==0){%>
                        <emp:message key="cxtj_sjcx_dxzljl_lt" defVal="联通" fileName="cxtj"></emp:message>
                        <%}else if(report.getSpisuncm()-21==0){ %>
                        <emp:message key="cxtj_sjcx_dxzljl_dx" defVal="电信" fileName="cxtj"></emp:message>
                        <%}else if(report.getSpisuncm()-5==0){ %>
                        <emp:message key="cxtj_sjcx_report_gw" defVal="国外" fileName="cxtj"></emp:message>
                        <%}else{ %>
                        -
                        <%} %>


                    </td>
                    <%--//从公用方法中获取 计算结果map  --%>
                    <%
                        Map<String, String> map = ReportBiz.getRptNums(
                                report.getIcount(), report.getRsucc(),
                                report.getRfail1(), report.getRfail2(),
                                report.getRnret(),
                                RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);

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

                    <%if(!isDes){%>
                    <td>
                        <a class="des"><emp:message key="cxtj_sjcx_yystjbb_ck" defVal="查看" fileName="cxtj"></emp:message></a>
                        <%if(report.getSpisuncm()-5 == 0){ %>
                        <a class="country"><emp:message key="cxtj_sjcx_yystjbb_ggfs" defVal="各国发送" fileName="cxtj"></emp:message></a>
                        <%} %>
                    </td>
                    <%}%>
                </tr>
                <%

                    }%>
                <tr>
                    <td colspan="3"><b><emp:message key="cxtj_sjcx_yystjbb_hj" defVal="合计：" fileName="cxtj"></emp:message></b></td>
                    <%
                        //从公用方法中获取 计算结果map
                        long icount = sumCount[0];
                        long rfail1 = sumCount[1];
                        long rfail2 = sumCount[2];
                        long rnret = sumCount[3];
                        long rsucc = sumCount[4];
                        Map<String,String> map=ReportBiz.getRptNums(Long.valueOf(icount),Long.valueOf(rsucc),Long.valueOf(rfail1),Long.valueOf(rfail2),Long.valueOf(rnret),RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
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
                    <%if(!isDes){%><td>-</td><%} %>
                </tr>
                <%}else{
                %>
                <tr>
                    <td colspan="<%=isDes?3+rptConList.size():4+rptConList.size() %>">
                        <%if(isFirstEnter!=null&&isFirstEnter){%>
                        <emp:message key="cxtj_sjcx_yystjbb_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"></emp:message>
                        <%}else{%>
                        <emp:message key="cxtj_sjcx_yystjbb_wjj" defVal="无记录" fileName="cxtj"></emp:message>
                        <%}%>
                    </td>
                </tr>
                <%} %>

                </tbody>
                <tfoot>
                <tr>
                    <td colspan="<%=isDes?3+rptConList.size():4+rptConList.size() %>">
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
        <div class="bottom">
            <div id="bottom_right">
                <div id="bottom_left"></div>
                <div id="bottom_main">
                    <div id="pageInfo"></div>
                </div>
            </div>
        </div>
        <div id="corpCode" class="hidden"></div>
    </form>
    <%-- foot结束 --%>
</div>
<div class="clear"></div>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath %>/js/rep_spMtReport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
<script type="text/javascript">

    var _startTime = '<%=startTime != null?startTime:""%>';
    var _endTime = '<%=endTime != null?endTime:""%>';
    var _staffId = '<%=staffId!=null?staffId:""%>';

    $(document).ready(function(){
        getLoginInfo("#corpCode");
        var findresult="<%=findResult%>";
        if(findresult=="-1")
        {
            //alert("加载页面失败,请检查网络是否正常!");
            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_1"));
            return;
        }

        //$("#toggleDiv").toggle(function() {
        //	$("#condition").hide();
        //	$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_expand.png');
        //	$('#searchIcon').attr('title', '展开查询条件');
        //}, function() {
        //	$("#condition").show();
        //	$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_collapse.png');
        //	$('#searchIcon').attr('title', '收缩查询条件');
        //});

        $("#toggleDiv").toggle(function() {
            $("#condition").hide();
            $(this).addClass("collapse");
        }, function() {
            $("#condition").show();
            $(this).removeClass("collapse");
        });

        //$('#content tbody tr').hover(function() {
        //	$(this).css('background-color', '#c1ebff');
        //}, function() {
        //	$(this).css('background-color', '#FFFFFF');
        //});
        $("#content tbody tr").hover(function() {
            $(this).addClass("hoverColor");
        }, function() {
            $(this).removeClass("hoverColor");
        });


        $("#msType").change(function(){
            if ($("#msType").attr("value") == "0")
            {
                $("#staffId").empty();
                $("#staffId").append("<option value=\"\">"+getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_5") +"</option>");
                $("#staffId").append("<option value=\" \" <%= (" ").equals(staffId)?"selected":"" %>>"+getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_6")+"</option>");
                <%
                    if (smsuserList != null && smsuserList.size() > 0)
                    {
                        for(OperatorsMtDataReportVo userdata : smsuserList)
                        {
                            if(!("").equals(userdata.getSpID().trim())&&userdata.getSpID()!= null){
                %>
                $("#staffId").append("<option value=\"<%=userdata.getSpID() %>\" <%= userdata.getSpID().equals(staffId)?"selected":"" %>><%=userdata.getSpID() %></option>");
                <%			}
                        }
                    }
                %>
            }
            else
            {
                $("#staffId").empty();
                $("#staffId").append("<option value=\"\">"+getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_5") +"</option>");
                $("#staffId").append("<option value=\" \" <%= (" ").equals(staffId)?"selected":"" %>>"+getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_6") +"</option>");
                <%
                    if (mmsuserList != null && mmsuserList.size() > 0)
                    {
                        for(OperatorsMtDataReportVo userdata : mmsuserList)
                        {
                            if(!("").equals(userdata.getSpID().trim())&&userdata.getSpID()!= null){
                %>
                $("#staffId").append("<option value=\"<%=userdata.getSpID() %>\" <%= userdata.getSpID().equals(staffId)?"selected":"" %>><%=userdata.getSpID() %></option>");
                <%			}
                        }
                    }
                %>
            }
        });

        //导出全部数据到excel
        $("#exportCondition").click(
            function(){
                //if(confirm("确定要导出数据到excel?"))
                if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_2")))
                {
                    <%if (reportList != null && reportList.size() > 0) {%>
                    var countTime='<%=startTime%>';
                    var staffId = '<%=staffId!=null?staffId:""%>';
                    var reportType = '<%=reportType%>';
                    var spisuncm = '<%=spisuncm!=null?spisuncm:""%>';
                    var endTime = '<%=endTime != null?endTime:""%>';
                    var isDes = $('#isDes').val();
                    var totalIcount=<%=sumCount[0]%>;
                    var totalRfail1=<%=sumCount[1]%>;
                    var totalRfail2=<%=sumCount[2]%>;
                    var totalRnret=<%=sumCount[3]%>;
                    var totalRsucc=<%=sumCount[4]%>;
                    var mstype=<%=spisuncmMtDataReportVo.getMstype()==null?"":spisuncmMtDataReportVo.getMstype().toString()%>;
                    $.ajax({
                        type: "POST",
                        url: "<%=path%>/rep_spisuncmMtReport.htm?method=r_stRptExportExcel",
                        data: {
                            countTime:countTime,
                            endTime:endTime,
                            spisuncm:spisuncm,
                            staffId:staffId,
                            reportType:reportType,
                            totalIcount:totalIcount,
                            totalRfail1:totalRfail1,
                            totalRfail2:totalRfail2,
                            totalRnret:totalRnret,
                            totalRsucc:totalRsucc,
                            isDes:isDes,
                            mstype:mstype
                        },
                        beforeSend:function () {
                            page_loading();
                        },
                        complete:function () {
                            page_complete()
                        },
                        success:function (result) {
                            if (result == 'true') {
                                download_href("<%=path%>/rep_spisuncmMtReport.htm?method=downloadFile");
                            } else {
                                //alert('导出失败！');
                                alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
                            }
                        }
                    });
                    //location.href="<%=path%>/rep_spisuncmMtReport.htm?method=r_stRptExportExcel&countTime="+countTime+"&endTime="+endTime+"&spisuncm="+spisuncm+"&staffId="+staffId+"&reportType="+reportType+"&totalIcount="+totalIcount+"&totalRfail1="+totalRfail1+"&totalRfail2="+totalRfail2+"&totalRnret="+totalRnret+"&totalRsucc="+totalRsucc+"&isDes="+isDes+"&mstype="+mstype;

                    <%} else {%>

                    //alert("无数据可导出！");
                    alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));

                    <%}%>
                }
            });

        initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
        $('#spisuncm').isSearchSelect({'width':'180','zindex':1,'isInput':false});
        $('#staffId').isSearchSelect({'width':'180','zindex':1,'isInput':false});
        $('#msType').isSearchSelect({'width':'180','zindex':1,'isInput':false});
        $('#search').click(function(){submitForm();});
        $('#reportType').isSearchSelectNew(
            {'width':'180','zindex':1,'isInput':false},
            function(){$("#reportType").change();}
        );
        $("#reportType").change();
        $("#timeSelect div:nth-child(3)").hide();
    });
</script>
</body>
</html>
