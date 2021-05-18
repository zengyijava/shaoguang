<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String actionPath = (String)request.getAttribute("actionPath");
	//按钮权限Map
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	if(btnMap == null)
	{
		btnMap = new HashMap<String,String>();
	}
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	if(titleMap == null)
	{
		titleMap = new HashMap<String,String>();
	}
	String menuCode = titleMap.get("/surlSendDetail");
	menuCode = menuCode==null?"0-0-0":menuCode;
    
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	if(pageInfo == null) {
		pageInfo = new PageInfo();
	}
	
	boolean isFirstEnter = (Boolean)request.getAttribute("isFirstEnter");
	
	//发送账号
	List<String> userList = (List<String>)request.getAttribute("mrUserList");
	//通道号码
	List<DynaBean> spList = (List<DynaBean>)request.getAttribute("spList");
	Map<String,String> busMap = (Map<String,String>)request.getAttribute("busMap");
	if(busMap == null) {
		busMap = new HashMap<String,String>();
	}
	
	//结果集合
	List<DynaBean> mtTaskList = (List<DynaBean>)request.getAttribute("sysMtTaskList");
	// 查询条件
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)session.getAttribute("sysMtTaskCon");
	if(conditionMap == null)
	{
		conditionMap = new LinkedHashMap<String, String>();
	}
	
	String recordType = conditionMap.get("recordType");
	if(recordType == null)
	{
		recordType = "realTime";
	}
	String userid = conditionMap.get("userid");
	String phone = conditionMap.get("phone");
	String spgate = conditionMap.get("spgate");
	String startTime = conditionMap.get("sendtime");
	String endTime = conditionMap.get("recvtime");
	String buscode = conditionMap.get("buscode");
	String spisuncm = conditionMap.get("spisuncm");
	String usercode = conditionMap.get("p1");
	String taskid = conditionMap.get("taskid");
	String mterrorcode = conditionMap.get("mterrorcode");
	String usermsgid = conditionMap.get("usermsgid");
	
	//查询分页信息url
	String loadPageUrl = path+"/que_sysMtRecord.htm?method=getMtPageInfo";
	
	String findResult = (String)request.getAttribute("findresult");
	
	//错误码说明map，key为错误码，value为说明
	Map<String,String> errCodeDesMap = (Map<String,String>)request.getAttribute("errCodeDesMap");
	if(errCodeDesMap == null)
	{
		errCodeDesMap = new HashMap<String,String>();
	}
	
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
	//任务批次
	String rwpc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_rwpc", request);
    if(rwpc!=null&&rwpc.length()>1){
    	rwpc = rwpc.substring(0,rwpc.length()-1);
    }
	//状态报告 
	String ztbg = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_ztbg", request);
	//发送时间
	String fssj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_fssj", request);
    if(fssj!=null&&fssj.length()>1){
    	fssj = fssj.substring(0,fssj.length()-1);
    }
	//接收时间
	String jssj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_jssj", request);
    if(jssj!=null&&jssj.length()>1){
    	jssj = jssj.substring(0,jssj.length()-1);
    }
	//业务类型
	String ywlx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_ywlx", request);
    if(ywlx!=null&&ywlx.length()>1){
    	ywlx = ywlx.substring(0,ywlx.length()-1);
    }
	//分条
	String ft = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_ft", request);
	//短信内容 
	String dxnr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dxnr", request);
    if(dxnr!=null&&dxnr.length()>1){
    	dxnr = dxnr.substring(0,dxnr.length()-1);
    }
	//自定义流水号
	String zdylsh = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_zdylsh", request);
    if(zdylsh!=null&&zdylsh.length()>1){
    	zdylsh = zdylsh.substring(0,zdylsh.length()-1);
    }
	//编码
	String bm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_bm", request);
    //移动
    String yd = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_yd", request);
    //联通
    String lt = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_lt", request);
    //电信
     String dx = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_dx", request);
    //国外
    String gw = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_gw", request);
    //成功
    String cg = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_cg", request);
    //失败
    String sb = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_sb", request);
    //错误说明
    String cwsm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxjl_cwsm", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<style type="text/css">
			#condition table tr td select
			{
				width: 182px;
			}
			#msg {
                *white-space: pre;
                white-space: pre-wrap;
                *word-wrap: break-word;
                word-break: break-all;
            }
		</style>
	</head>
	<body id="surl_sendDetailReport">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<div id="errCodeDesDiv" style="display:none;">
				<%
					int i_err = 0;
					for(String key : errCodeDesMap.keySet())
					{
				%>
				<input type="hidden" id="hdErrCode<%=i_err %>" value="<%=key + "---" + errCodeDesMap.get(key) %>" />
				<%
						i_err++;
					}
				%>
			</div>
				
				<form name="pageForm" action="<%=actionPath %>?method=find" method="post" id="pageForm">
                    <div id="hiddenValueDiv" style="display:none;"></div>
						<div class="buttons">
							<div id="toggleDiv" >								
							</div>
						</div>
						<div id="condition">
						 <table>
								<tbody>
								<tr>
								    <td>
									<emp:message key="cxtj_sjcx_xtsxjl_jllx" defVal="记录类型" fileName="cxtj"/>
									</td>
									<td>
										<select name="recordType" class="recordTypeSelect" style="" onclick="setTimeWhenHistory(this)">
										     <option value="realTime" selected><emp:message key="cxtj_sjcx_xtsxjl_ssjl" defVal="实时记录" fileName="cxtj"/></option>
										     <option value="history" <%="history".equals(recordType)?"selected":"" %>><emp:message key="cxtj_sjcx_xtsxjl_lsjl" defVal="历史记录" fileName="cxtj"/></option>
										</select>
									</td>
									
									<td>
										<emp:message key="cxtj_sjcx_xtxxjl_rwpc" defVal="任务批次：" fileName="cxtj"/>
									</td>
									<td>
										<input type="text" style="width: 181px;" value='<%=taskid==null?"":taskid %>' id="taskid" name="taskid" onkeyup="javascript:numberControl($(this))"  onblur="javascript:numberControl($(this))" maxlength="16"/>
									</td>
									
									<td>
										<emp:message key="cxtj_sjcx_xtsxjl_sjhm" defVal="手机号码：" fileName="cxtj"/>
									</td>
									<td>
										<input type="text" style="width: 181px;" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
									</td>
									
									<td class="tdSer">
									     <center><a id="search"></a></center>
								    </td>
								</tr>
								<tr>
								<td><emp:message key="cxtj_sjcx_xtxxjl_ywlx" defVal="业务类型" fileName="cxtj"/></td>
								<td>
								<select id="busCode" name="busCode" style="">
                         			<option value=""><emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部" fileName="cxtj"/></option>
                         			<option value="-1" <%if("-1".equals(buscode)){ %> selected="selected" <%} %>>-(<emp:message key="cxtj_sjcx_xtxxjl_wywlx" defVal="无业务类型" fileName="cxtj"/>)</option>                         		
                         		<%
                         		if(busMap != null && busMap.size() > 0)
                         		{
                         			String value;
                         			for(String key : busMap.keySet())
                         			{
                         				value = busMap.get(key);
                         				if(value  == null)
                         				{
                         					value = "";
                         				}
                         			%>
                           			  <option value="<%=key %>" <% if (key.equals(buscode)){out.print("selected=\"selected\"");} %>><%=value.replace("<","&lt;").replace(">","&gt;") %>(<%=key %>)</option>
                           			<%
                         			}
                         		}
                         		%>
                         		</select>
								</td>
								
								<td>
										<emp:message key="cxtj_sjcx_xtsxjl_spzh" defVal="sp账号" fileName="cxtj"/>
									</td>
									<td>
										<label>
											<select name="userid" id="userid" style="">
												<option value="">
													<emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部" fileName="cxtj"/>
												</option>
											<%
											if (userList != null && userList.size() > 0)
											{
												for(String userdata : userList)
												{
													if(userdata == null)
													{
														continue;
													}
											%>
												<option value="<%=userdata %>" <%=userdata.equals(userid)?"selected":""%>>
													<%=userdata %>
												</option>
											<%
												}
											}
											%>
											</select>
										</label>
									</td>
								
									<td>
										<emp:message key="cxtj_sjcx_xtxxjl_ztm" defVal="状态码：" fileName="cxtj"/>
									</td>
									<td>
										<input type="text" value='<%=mterrorcode==null?"":mterrorcode %>' id="mterrorcode" name="mterrorcode" style="width: 181px;" maxlength="7"/>
									</td>
									
								<td>&nbsp;</td>
								</tr>
								<tr>
									
									<td>
										<emp:message key="cxtj_sjcx_xtxxjl_tdhm" defVal="通道号码：" fileName="cxtj"/>
									</td>
									<td>
										<label>
											<select name="spgate" id="spgate" style="">
												<option value="">
													<emp:message key="cxtj_sjcx_xtxxjl_qb" defVal="全部" fileName="cxtj"/>
												</option>
											<%
												if (spList != null && spList.size() > 0)
												{
													String tmpSpgate;
													for(DynaBean spgatevo : spList)
													{
														if(spgatevo == null || spgatevo.get("spgate") == null)
														{
															continue;
														}
														tmpSpgate = spgatevo.get("spgate").toString().trim();
											%>
												<option value="<%=tmpSpgate %>" <%=tmpSpgate.equals(spgate)?"selected":""%>>
													<%=tmpSpgate %>
												</option>
											<%		
													}
												}
											%>
											</select>
										</label>
									</td>
									<td>
											<emp:message key="cxtj_sjcx_xtxxjl_zdylsh" defVal="自定义流水号：" fileName="cxtj"/>
									</td>
									<td>
										<input type="text" style="width: 181px;" value='<%=usermsgid==null?"":usermsgid %>' id="usermsgid" name="usermsgid" onkeyup="javascript:numberControl($(this))"  onblur="javascript:numberControl($(this))" maxlength="19"/>
									</td>
									<td></td>
									<td></td>
									<td>&nbsp;</td>
								</tr>
								<tr>
								<td>
									<emp:message key="cxtj_sjcx_xtxxjl_fssj" defVal="发送时间：" fileName="cxtj"/>
								</td>
								<td class="tableTime">
									<input type="text" style="cursor: pointer; width: 181px;background-color: white;" class="Wdate" readonly="readonly" onclick="sedtime()" value="<%=startTime==null?"":startTime %>" id="sendtime" name="sendtime">
								</td>
								<td>
									<emp:message key="cxtj_sjcx_xtxxjl_z" defVal="至：" fileName="cxtj"/>
								</td>
								<td>
									<input type="text" style="cursor: pointer; width: 181px;background-color: white;" class="Wdate" readonly="readonly" onclick="revtime()" value="<%=endTime==null?"":endTime %>" id="recvtime" name="recvtime">
									<input type="hidden" style="width: 181px;" value='<%=usercode==null?"":usercode %>' id="usercode" name="usercode" />
								</td>
								<td></td>
								<td></td>
								<td>&nbsp;</td>
								</tr>
							</tbody>
							</table>
						</div>
						<table id="content">
							<thead>
							    <tr>
						            <th><emp:message key="cxtj_sjcx_report_spzh" defVal="sp账号" fileName="cxtj"/> </th>
						            <th><%=tdhm %>  </th>
						            <th><%=yys %></th>
						            <th><%=sjhm %> </th>
						            <th><%=rwpc %></th>
						            <th> <%=ztbg %> </th>
						            <th><%=fssj %></th>
						            <th><%=jssj %></th>
						            <th><%=ywlx %></th>
						            <th><%=ft %></th>
						            <th> <%=dxnr %></th>
						            <th><%=zdylsh %></th>
						            <%
						            if("realTime".equals(recordType))
									{
									%>
						            <th><%=bm %></th>
						            <%
						            }
						            %>
						        </tr>
							</thead>
							<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							 	<tr><td colspan="13" align="center"><emp:message key="cxtj_sjcx_xtsxjl_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"/>
							 	</td></tr>
							<%
							}
							else if(mtTaskList == null || mtTaskList.size() < 1)
							{
							%>
								<tr><td colspan="13" align="center"><emp:message key="cxtj_sjcx_xtxxjl_wjl" defVal="无记录" fileName="cxtj"/></td></tr>	
							<%
							}
							else
							{
								CommonVariables CV = new CommonVariables();
                                for (DynaBean mtTask : mtTaskList) {
                                    if (mtTask == null) {
                                        continue;
                                    }
                            %>
                            <tr>
                                <td>
                                    <%=mtTask.get("userid") == null ? "-" : mtTask.get("userid").toString() %>
                                </td>
                                <td>
                                    <%=mtTask.get("spgate") == null ? "-" : mtTask.get("spgate").toString() %><%=mtTask.get("cpno") == null ? "" : mtTask.get("cpno").toString() %>
                                </td>
                                <td class="ztalign">
                                    <%
                                        String unicom = mtTask.get("unicom") == null ? "" : mtTask.get("unicom").toString();
                                        if ("0".equals(unicom)) {
                                            out.print(yd);
                                        } else if ("1".equals(unicom)) {
                                            out.print(lt);
                                        } else if ("21".equals(unicom)) {
                                            out.print(dx);
                                        } else if ("5".equals(unicom)) {
                                            out.print(gw);
                                        } else {
                                    %>
                                    -
                                    <%
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        if (mtTask.get("phone") != null && btnMap != null) {
                                            String phones = CV.replacePhoneNumber(btnMap, mtTask.get("phone").toString());
                                            out.print(phones);
                                        } else {
                                            out.print("-");
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        String strTaskid = mtTask.get("taskid") == null ? "-" : mtTask.get("taskid").toString();
                                        if ("0".equals(strTaskid)) {
                                            out.print("-");
                                        } else {
                                            out.print(strTaskid);
                                        }
                                    %>
                                </td>
                                <td class="ztalign">
                                    <%
                                        String erorcode = mtTask.get("errorcode") == null ? "" : mtTask.get("errorcode").toString().trim();
                                        if (erorcode.length() == 0) {
                                            out.print("-");
                                        } else if ("DELIVRD".equals(erorcode) || "0".equals(erorcode)) {
                                            out.print(cg);
                                        } else {
                                            if (errCodeDesMap.get(erorcode) != null) {
                                    %>
                                    <a onclick="showErrDis('<%=erorcode %>')"><%=sb %>[<%=erorcode %>]</a>
                                    <%
                                    } else {
                                    %>
                                    <%=sb %>[<%=erorcode %>]
                                    <%
                                            }
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        String sendtime;
                                        if (mtTask.get("sendtime") != null) {
                                            Date date = df.parse(mtTask.get("sendtime").toString());
                                            sendtime = df.format(date);
                                        } else {
                                            sendtime = "-";
                                        }
                                        out.print(sendtime);
                                    %>
                                </td>
                                <td>
                                    <%
                                        String recvtime;
                                        if (mtTask.get("recvtime") != null) {
                                            Date date = df.parse(mtTask.get("recvtime").toString());
                                            recvtime = df.format(date);
                                        } else {
                                            recvtime = "-";
                                        }
                                        out.print(recvtime);
                                    %>
                                </td>
                                <td class="textalign">
                                    <%
                                        String svrtype = mtTask.get("svrtype") == null ? "" : mtTask.get("svrtype").toString();
                                    %>
                                    <xmp><%=busMap.get(svrtype) == null ? "-" : busMap.get(svrtype) %>
                                    </xmp>
                                </td>
                                <td><%=mtTask.get("pknumber") == null ? "-" : mtTask.get("pknumber").toString() %>
                                    /<%=mtTask.get("pktotal") == null ? "-" : mtTask.get("pktotal").toString() %>
                                </td>
                                <%--信息内容--%>
                                <td style="text-align:left;">
                                    <a onclick=modify(this,'信息内容'); title="<%=mtTask.get("neturl")%>">
                                        <xmp>
                                            <%
                                                String st = "";
                                                String temp = mtTask.get("message") == null ? "" : mtTask.get("message").toString();
                                                if (temp.length() > 5) {
                                                    st = temp.substring(0, 5) + "...";
                                                } else {
                                                    st = temp;
                                                }
                                                out.print(st);
                                            %>
                                        </xmp>

                                        <%
                                            String netUrl = mtTask.get("neturl") == null ? "" : mtTask.get("neturl").toString();
                                            String domainUrl = mtTask.get("domain_url") == null ? "" : mtTask.get("domain_url").toString();
                                        %>

                                        <textarea
                                                style="display:none"><%=temp.replaceAll(domainUrl , "<a href='" + netUrl + "' target='_blank' title='" + netUrl + "' style='color: blue;text-decoration: none'>" + domainUrl + "</a>") %></textarea>
                                    </a>
                                </td>
                                <td>
                                    <%
                                        //String strUsermsgid = mtTask.get("usermsgid") == null?"-":mtTask.get("usermsgid").toString();
                                        //out.print(strUsermsgid);
                                        //修改优先显示新版的自定义流水号custid，其次再显示旧版的usermsgid
                                        String custid = mtTask.get("custid").toString();
                                        String usermsgidStr = mtTask.get("usermsgid").toString();
                                        if ((custid == null || "".equals(custid.trim())) && usermsgidStr != null && !"0".equals(usermsgidStr)) {
                                            out.print(usermsgidStr);
                                        } else {
                                            String custidStr = (mtTask.get("custid") == null || "".equals(custid.trim())) ? "-" : mtTask.get("custid").toString();
                                            out.print(custidStr);
                                        }

                                    %>
                                </td>
                                <%
                                    if ("realTime".equals(recordType)) {
                                %>
                                <td><%=mtTask.get("msgfmt") == null ? "-" : mtTask.get("msgfmt").toString() %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                    }
							}
							%>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="13">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
						<div id="r_sysMrparams" class="hidden"></div>
				 </form>
			</div>
			<%}%>
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
			<%--信息内容弹出框--%>
			<div id="modify" title=""  style="padding:5px;width:300px;height:160px;display:none">
				<span><div id="msgcont" style="width:100%;height:100%;"></div></span>
			</div>
			
			<div id="errCodeDis" title=<%=cwsm %>  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="240px">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<span style="display:block;width:240px;"><label id="errDis" style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
						 
    <div class="clear"></div>
    	<script type="text/javascript">
			function LoginInfo(idname){
				document.getElementById(idname).innerHTML=window.parent.parent.document.getElementById("loginparams").innerHTML;
			}
			LoginInfo("r_sysMrparams");
		</script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/report/js/smt_smsTaskRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/que_sysMtRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">

			$(document).ready(function(){
                getLoginInfo("#hiddenValueDiv");
				var findresult="<%=findResult%>";
			    if(findresult==="-1") {
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
            $('#spgate,#userid,#busCode').isSearchOldSelect({'width':'179','isInput':true,'zindex':0});
            $('.recordTypeSelect').isSearchOldSelect({'width':'179','isInput':true,'zindex':0});
			initPage('<%=pageInfo.getTotalPage()%>','<%=pageInfo.getPageIndex()%>','<%=pageInfo.getPageSize()%>','<%=pageInfo.getTotalRec()%>');

			$('#search').click(function(){submitForm();});

			$('#other').click(function(){location.href="<%=path%>/r_sysMtRecord.htm?method=mmsFind";});
		});

		</script>
	</body>
</html>
