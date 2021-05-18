<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.selfparam.LfWgParmDefinition"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.report.bean.RptConfInfo"%>
<%@page import="com.montnets.emp.report.bean.RptStaticValue"%>
<%@page import="com.montnets.emp.report.biz.ReportBiz"%>
<%@ page
        import="com.montnets.emp.report.biz.RptConfBiz" %>
<%@ page
        import="com.montnets.emp.report.vo.DynParmReportVo" %>
<%@ page
        import="com.montnets.emp.util.PageInfo" %>
<%@ page
        import="java.text.SimpleDateFormat" %>
<%@ page
        import="java.util.Date" %>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="java.util.HashMap"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@include file="/common/common.jsp" %>

<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");

    boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");

    long[] sumArray = (long[]) session.getAttribute("dyn_sumArray");

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Date date = new Date();

	String beginTime = df.format(date).substring(0, 8) + "01";
	//String  endTime =df.format(date).substring(0, 8)+"07" ;

	String endTime = df.format(date).substring(0, 11); //change by dj

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session
			.getAttribute("titleMap");

	@SuppressWarnings("unchecked")
	List<DynParmReportVo> mtreportList = (List<DynParmReportVo>) request
			.getAttribute("mtreportList");

	@SuppressWarnings("unchecked")
	List<LfWgParmDefinition> paraList = (List<LfWgParmDefinition>) request
			.getAttribute("paraList");

	String menuCode = titleMap.get("dynParamReport");

	String paramName = null == request.getParameter("paramName") ? null
			: (String) request.getParameter("paramName");

	String paramTitle = null == session.getAttribute("paramTitle") ? ""
			: (String) session.getAttribute("paramTitle");

	menuCode = menuCode == null ? "0-0-0" : menuCode;
	
	String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    
    MessageUtils messageUtils = new MessageUtils();
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
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
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%--<script type="text/javascript"	src="<%=inheritPath%>/scripts/expandContent.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<style type="text/css">
			.c_selectBox{
				width: 194px!important;
			}
			.c_selectBox ul {
				width: 194px!important;
			}
			.c_selectBox ul li{
				width: 194px!important;
			}
			#condition table tr td > div > input{
				width: 193px!important;
			}
		</style>
	</head>
	<body class="rep_dynParamReport">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<%
				if (btnMap.get(menuCode + "-0") != null) {
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath%>" id="inheritPath" />

				<form name="pageForm" action="rep_dynParamReport.htm" method="post"
					id="pageForm">

					<input type="hidden" id="deptString" name="deptString"
						value="<%=request.getAttribute("deptString") == null ? ""
						: request.getAttribute("deptString")%>" />
					<input type="hidden" id="userString" name="userString"
						value="<%=request.getAttribute("userString") == null ? ""
						: request.getAttribute("userString")%>" />
					<input type="hidden" id="sp" name="sp"
						value="<%=request.getAttribute("sp") == null ? "" : request
						.getAttribute("sp")%>" />
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
							if (btnMap.get(menuCode + "-5") != null) {
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition"></a>

						<input type="hidden" name="menucode" id="menucode" value="17" />
						<%
							}
						%>

						<a id="exportCondition"><emp:message key="cxtj_sjcx_yystjbb_dc" defVal="导出" fileName="cxtj"></emp:message></a>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>

									<td>
										<emp:message key="cxtj_sjcx_zhtjbb_bblx" defVal="报表类型：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<select name="paramName" id="paramName" isInput="false">
											<option value="">
												<emp:message key="cxtj_sjcx_zhtjbb_qxz" defVal="请选择" fileName="cxtj"></emp:message>
											</option>
											<%
												String tempStr = null;
											%>

											<%
											if(paraList!=null && paraList.size()>0){
												for (LfWgParmDefinition paramDef : paraList) {
														tempStr = paramDef.getParam() + "&"
																+ paramDef.getParamSubNum() + "&"
																+ paramDef.getParamSubName();
											%>
											<option value="<%=tempStr%>"
												<%=tempStr.equals(paramName) ? "selected" : ""%>>

												<%=paramDef.getParamSubName().replace("<","&lt;").replace(">","&gt;")%>
												
											</option>
											<%
												}
												}
											%>

										</select>



									</td>
									<td>

									</td>
									<td>

									</td>
									<td class="tdSer"><center><a id="search"></a></center></td>
								</tr>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_zdcstjbb_kssj" defVal="开始时间：" fileName="cxtj"></emp:message>
									</td>
									<td class="tableTime">

										<input type="text"
											class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=request.getParameter("begintime") != null ? request
						.getParameter("begintime") : beginTime%>"
											id="sendtime" name="begintime">
									</td>
									<td>
										<emp:message key="cxtj_sjcx_zdcstjbb_jssj" defVal="结束时间：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text"
											class="Wdate" readonly="readonly"
											onclick="retime()"
											value="<%=request.getParameter("endtime") != null ? request
						.getParameter("endtime") : endTime%>"
											id="recvtime" name="endtime">
									</td>
									<td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">

						<thead>
							<tr>
								<%
									if (null == paramTitle || "".equals(paramTitle.trim())) {
											//paramTitle = "未知";
											paramTitle = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
										}
								%>
								<th>
									<emp:message key="cxtj_sjcx_yystjbb_sj" defVal="时间" fileName="cxtj"></emp:message>
								</th>
								<th>
									<xmp><%=paramTitle%></xmp>
								</th>
								<th>
									<emp:message key="cxtj_sjcx_zdcstjbb_csz" defVal="参数值" fileName="cxtj"></emp:message>
								</th>
                                <%
                                    List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DYN_RPT_CONF_MENU_ID);
                                    //总列数
                                    int cols = 3+rptConList.size();
                                    String temp = null;
                                    for(RptConfInfo rptConf : rptConList)
                                    {
                                    	temp = rptConf.getName();
                                %>
                                <th><%=MessageUtils.extractMessage("cxtj",temp,request) %></th>
                                <%  } %>
							</tr>
						</thead>
						<%
							//时间格式显示处理  eg：2012年12月12日 
								String btime = request.getParameter("begintime") != null ? request
										.getParameter("begintime")
										: beginTime;
								String etime = request.getParameter("endtime") != null ? request
										.getParameter("endtime")
										: endTime;
								String showTime = "";
								if (!"".equals(btime) && null != btime && 0 != btime.length()) {
									String btemp[] = btime.split("-");

									//btime = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
									
									//btime = btemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request) + btemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + btemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);

								}

								if (!"".equals(etime) && null != etime && 0 != etime.length()) {
									String etemp[] = etime.split("-");

									//etime = etemp[0] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_year", request)  + etemp[1] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_month", request) + etemp[2] + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_day", request);
								}

								showTime = btime + "  "+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  " + etime;
								session.setAttribute("dyn_showTime", showTime);
								//String showTime = "-";
								/*if( null != mdreportVo.getIymd() && 0 != mdreportVo.getIymd().length()
										&& mdreportVo.getIymd().contains("-"))
								{
									String[] showTimeArray = mdreportVo.getIymd().split("-");
									Integer dTime = Integer.parseInt(showTimeArray[2].substring(0,2).toString());//显示天数
									Integer monthtime = Integer.parseInt(showTimeArray[1].toString());//显示月份
									 
									showTime  = showTimeArray[0]+"年"+monthtime+"月"+dTime+"日";
								 
								}*/
						%>
						<tbody>
						
							<%if (isFirstEnter) {
								%>
								<tr>
									<td colspan="<%=cols%>" align="center">
										<emp:message key="cxtj_sjcx_yystjbb_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"></emp:message>
									</td>
								</tr>
								<%
                            } else {
								if (mtreportList != null&& mtreportList.size() > 0 ) {
                                    Map<String,String> sumCountMap = new HashMap<String, String>();
							%>

							<%
								for (int mindex = 0; mindex < mtreportList.size(); mindex++) {
											DynParmReportVo mdreportVo = mtreportList.get(mindex);
							%>
							<tr>
								<td><%=showTime%></td>
								<td class="textalign"><xmp><%=null == mdreportVo.getParamName() ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request)
								: mdreportVo.getParamName().replace("<","&lt;").replace(">","&gt;")%></xmp></td>
								<td class="textalign">
									<xmp><%=null == mdreportVo.getPa() ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request)
								: mdreportVo.getPa().replace("<","&lt;").replace(">","&gt;")%></xmp>
								</td>
                                <%
                                    sumCountMap = ReportBiz.getRptNums(mdreportVo.getIcount(),mdreportVo.getRsucc(),mdreportVo.getRfail1(),mdreportVo.getRfail2(),mdreportVo.getRnret(),RptStaticValue.DYN_RPT_CONF_MENU_ID);
                                    for(RptConfInfo rptConf : rptConList)
                                    {
                                %>
                                <td><%=sumCountMap.get(rptConf.getColId())%></td>
                                <%
                                    }
                                %>
							</tr>

							<%
								}
							%>
							<tr>
								<td colspan="3">
									<b><emp:message key="cxtj_sjcx_yystjbb_hj" defVal="合计：" fileName="cxtj"></emp:message></b>
								</td>
                                <%
                                    if(sumArray != null && sumArray.length >=5)
                                    {
                                        sumCountMap = ReportBiz.getRptNums(sumArray[0],sumArray[1],sumArray[2],sumArray[3],sumArray[4],RptStaticValue.DYN_RPT_CONF_MENU_ID);
                                    }
                                    for(RptConfInfo rptConf : rptConList)
                                    {
                                %>
                                <td><%=sumCountMap.get(rptConf.getColId())%></td>
                                <%
                                    }
                                %>
							</tr>
							<%
                            }else {
							%>
							<tr>
								<td align="center" colspan="<%=cols%>"><emp:message key="cxtj_sjcx_yystjbb_wjj" defVal="无记录" fileName="cxtj"></emp:message></td>
							</tr>
							
							<%
								}}
							%>
						</tbody>

						<tfoot>
							<tr>
								<td colspan="<%=cols%>">
									<input type="hidden" name="pageTotalRec" id="pageTotalRec"
										value="<%=pageInfo.getTotalRec()%>" />
									<input type="hidden" name="queryTime" id="queryTime"
										value="<%=request.getParameter("begintime") == null ? ""
						: request.getParameter("begintime")%><%=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)%><%=request.getParameter("endtime") == null ? ""
						: request.getParameter("endtime")%>" />
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
					</div>
				</div>
			</div>
		</div>
		<%-- foot结束 --%>
		<div class="clear"></div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"	src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">		
	
		$(document).ready(function(){
		getLoginInfo("#corpCode");
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

			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
			
			var judgeSendtime = $("#sendtime").val();//开始时间
			
			var judgeendtime = $("#recvtime").val();//结束时间
			
			var paramName = $("#paramName").val();
			
			if("" == $.trim(judgeSendtime) || "" == $.trim(judgeendtime) )
			{
				//alert("请选择开始时间和结束时间！");
				alert(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_4"));			
			}else
			{

				if( "" == $.trim(paramName) )
				{
					//alert("请选择报表类型！");
					alert(getJsLocaleMessage("cxtj","cxtj_sjcx_zdcstjbb_text_1"));
				}else
				{
			      
			   		 submitForm();//选择好时间段，才允许查询 
				}
			    
			}
			
			});
			
			
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {
				
				  //if(confirm("确定要导出数据到excel?"))
				  if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_2")))
				   {

				   		<%if (null != mtreportList && mtreportList.size() > 0) {%>		
				   		var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
				   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
				   	    var paramName = '<%=paramName %>';
				   		var lgguid = $("#lgguid").val();
				   		var lguserid = $("#lguserid").val();
				   	    var lgcorpcode = $("#lgcorpcode").val();
				   	    var parmtemp = paramName.split("&");
				   		var paramType = null;
				   		var paramSubNum = null;
				   		var paramTitle = null;
				   		if( null != parmtemp )
				   		{
				   		   paramType = parmtemp[0];
				   		   paramSubNum = parmtemp[1] ;
				   		   paramTitle = parmtemp[2] ;
				   		}	
				   		$.ajax({
							type: "POST",
							url: "<%=path%>/rep_dynParamReport.htm?method=r_dpRptExportExcel",
							data: {
								lguserid:lguserid,
								lgcorpcode:lgcorpcode,
								begintime:sendtime,
								endtime:recvtime,
								paramType:paramType,
								paramSubNum:paramSubNum
							},
						    beforeSend:function () {
						        page_loading();
						    },
						    complete:function () {
						        page_complete()
						    },
						    success:function (result) {
						        if (result == 'true') {
						            download_href("<%=path%>/rep_dynParamReport.htm?method=downloadFile");
						        } else {
						            //alert('导出失败！');
						            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
						        }
	           				}
				 		});			
				   		//location.href="<%=path%>/rep_dynParamReport.htm?method=r_dpRptExportExcel&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&begintime="+sendtime+"&endtime="+recvtime+"&paramType="+paramType+"&paramSubNum="+paramSubNum;
				   		<%} else {%>
				   		//alert("无数据可导出！");
				   		alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));
				   		<%}%>
				   }				 
			  });
		
		   
			
		});
		
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/rep_dynParamReport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
