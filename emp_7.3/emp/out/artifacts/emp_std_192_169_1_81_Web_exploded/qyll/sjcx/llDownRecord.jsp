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
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute("emp_lang");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
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
	String menuCode = titleMap.get("downRecord");
	menuCode = menuCode==null?"0-0-0":menuCode;
    
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	if(pageInfo == null)
	{
		pageInfo = new PageInfo();
	}
	
	boolean isFirstEnter = (Boolean)request.getAttribute("isFirstEnter");
	
	//发送账号
	List<String> userList = (List<String>)request.getAttribute("mrUserList");
	//通道号码
	List<DynaBean> spList = (List<DynaBean>)request.getAttribute("spList");
	Map<String,String> busMap = (Map<String,String>)request.getAttribute("busMap");
	if(busMap == null)
	{
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
	String loadPageUrl = path+"/ll_downRecord.htm?method=getMtPageInfo";
	
	String findResult = (String)request.getAttribute("findresult");
	
	//错误码说明map，key为错误码，value为说明
	Map<String,String> errCodeDesMap = (Map<String,String>)request.getAttribute("errCodeDesMap");
	if(errCodeDesMap == null)
	{
		errCodeDesMap = new HashMap<String,String>();
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<style type="text/css">
			#condition table tr td select
			{
				width: 182px;
			}
			#msg {
                white-space: pre-wrap;
                *white-space: pre;
                *word-wrap: break-word;
                word-break: break-all;
            }
		</style>
		<script type="text/javascript">
		function numberControl1(va){
			va.val(va.val().replace(/[^u4e00-u9fa5w]/g,''));
		}
		</script>
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			
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
				
				<form name="pageForm" action="ll_downRecord.htm" method="post" id="pageForm">
						<div class="buttons">
							<div id="toggleDiv" >								
							</div>
						</div>
						<div id="condition" >
						 <table>
								<tbody>
								<tr>
								    <td>
									<emp:message key="qyll_common_132" defVal="记录类型：" fileName="qyll"/>：
									</td>
									<td>
										<select name="recordType" style="">
										     <option value="realTime"><emp:message key="qyll_common_133" defVal="实时记录" fileName="qyll"/></option>
										     <option value="history" <%="history".equals(recordType)?"selected":"" %>><emp:message key="qyll_common_134" defVal="历史记录" fileName="qyll"/></option>
										</select>
									</td>
									
									<td>
										<emp:message key="qyll_common_135" defVal="任务批次：" fileName="qyll"/>：
									</td>
									<td>
										<input type="text" style="width: 177px;" value='<%=taskid==null?"":taskid %>' id="taskid" name="taskid" onkeyup="javascript:numberControl($(this))"  onblur="javascript:numberControl($(this))" maxlength="16"/>
									</td>
									
									<td>
										<emp:message key="qyll_common_93" defVal="手机号码：" fileName="qyll"/>：
									</td>
									<td>
										<input type="text" style="width: 177px;" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
									</td>
									
									<td class="tdSer">
									     <center><a id="search"></a></center>
								    </td>
								</tr>
								<tr>
								<td><emp:message key="qyll_common_136" defVal="业务类型：" fileName="qyll"/>：</td>
								<td>
								<select id="busCode" name="busCode" style="">
                         			<option value=""><emp:message key="qyll_common_78" defVal="全部" fileName="qyll"/></option>
                         			<option value="-1" <%if("-1".equals(buscode)){ %> selected="selected" <%} %>>-(<emp:message key="qyll_common_137" defVal="-(无业务类型)" fileName="qyll"/>)</option>                         		
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
                           			  <option value="<%=key %>" <% if (key.equals(buscode)){out.print("selected=\"selected\"");} %>><emp:message key="qyll_common_196" defVal="默认业务" fileName="qyll"/>(<%=key %>)</option>
                           			<%
                         			}
                         		}
                         		%>
                         		</select>
								</td>
								
								<td>
										<emp:message key="qyll_common_195" defVal="SP账号：" fileName="qyll"/>：
									</td>
									<td>
										<label>
											<select name="userid" id="userid" style="">
												<option value="">
													<emp:message key="qyll_common_78" defVal="全部" fileName="qyll"/>
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
										<emp:message key="qyll_common_138" defVal="状态码：" fileName="qyll"/>：
									</td>
									<td>
										<input type="text" value='<%=mterrorcode==null?"":mterrorcode %>' id="mterrorcode" name="mterrorcode" style="width: 177px;" maxlength="7"/>
									</td>
									
								<td>&nbsp;</td>
								</tr>
								<tr>
									
									<td>
										<emp:message key="qyll_common_139" defVal="通道号码：" fileName="qyll"/>：
									</td>
									<td>
										<label>
											<select name="spgate" id="spgate" style="">
												<option value="">
													<emp:message key="qyll_common_78" defVal="全部" fileName="qyll"/>
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
											<emp:message key="qyll_common_140" defVal="自定义流水号：" fileName="qyll"/>：
									</td>
									<td>
										<input type="text" style="width: 177px;" value='<%=usermsgid==null?"":usermsgid %>' id="usermsgid" name="usermsgid" onkeyup="javascript:numberControl1($(this))"  onblur="javascript:numberControl1($(this))" maxlength="30"/>
									</td>
									<td></td>
									<td></td>
									<td>&nbsp;</td>
								</tr>
								<tr>
								<td>
										<emp:message key="qyll_common_141" defVal="发送时间：" fileName="qyll"/>：
								</td>
								<td class="tableTime">
										<input type="text" style="cursor: pointer; width: 178px;background-color: white;" class="Wdate" readonly="readonly" onclick="sedtime()"
											 value="<%=startTime==null?"":startTime %>" id="sendtime" name="sendtime">
								</td>
								<td>
										<emp:message key="qyll_common_105" defVal="至：" fileName="qyll"/>：
								</td>
								<td>
										<input type="text" style="cursor: pointer; width: 178px;background-color: white;" class="Wdate" readonly="readonly" onclick="revtime()"
											 value="<%=endTime==null?"":endTime %>" id="recvtime" name="recvtime">
											  <input type="hidden" style="width: 177px;" value='<%=usercode==null?"":usercode %>' id="usercode" name="usercode" />
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
						            <th><emp:message key="qyll_common_195" defVal="SP账号：" fileName="qyll"/> </th>
						            <th><emp:message key="qyll_common_139" defVal="通道号码" fileName="qyll"/> </th>
						            <th><emp:message key="qyll_common_43" defVal="运营商" fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_93" defVal="手机号码" fileName="qyll"/> </th>
						            <th><emp:message key="qyll_common_135" defVal="任务批次" fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_144" defVal="状态报告" fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_141" defVal="发送时间" fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_142" defVal="接收时间" fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_136" defVal="业务类型" fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_143" defVal="分条" fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_112" defVal="短信内容 " fileName="qyll"/></th>
						            <th><emp:message key="qyll_common_140" defVal="自定义流水号" fileName="qyll"/></th>
						            <%
						            if("realTime".equals(recordType))
									{
									%>
						            <th><emp:message key="qyll_common_145" defVal="编码" fileName="qyll"/></th>
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
							 	<tr><td colspan="13" align="center"><emp:message key="qyll_common_81" defVal="请点击查询获取数据" fileName="qyll"/></td></tr>
							<%
							}
							else if(mtTaskList == null || mtTaskList.size() < 1)
							{
							%>
								<tr><td colspan="13" align="center"><emp:message key="qyll_common_82" defVal="无记录" fileName="qyll"/></td></tr>	
							<%
							}
							else
							{
								CommonVariables CV = new CommonVariables();
								for(int i=0;i<mtTaskList.size();i++)
								{
									DynaBean mtTask = mtTaskList.get(i);
									if(mtTask == null)
									{
										continue;
									}
							%>
								<tr>
									<td>
										<%=mtTask.get("userid") == null?"-" : mtTask.get("userid").toString() %>
									</td>
									<td>
										<%=mtTask.get("spgate") == null?"-" : mtTask.get("spgate").toString() %><%=mtTask.get("cpno") == null?"" : mtTask.get("cpno").toString() %>
									</td>
									<td class="ztalign">
									<%
									String unicom = mtTask.get("unicom") == null?"":mtTask.get("unicom").toString();
									if("0".equals(unicom))
									{
									%>
									<emp:message key="qyll_common_185" defVal="移动" fileName="qyll"/>
									<%
									}
									else if("1".equals(unicom))
									{
									%>
									<emp:message key="qyll_common_186" defVal="联通" fileName="qyll"/>
									<%
									}
									else if("21".equals(unicom))
									{
									%>
									<emp:message key="qyll_common_187" defVal="电信" fileName="qyll"/>
									<%
									}
									else if("5".equals(unicom))
									{
									%>
									<emp:message key="qyll_common_23" defVal="国外" fileName="qyll"/>
									<%
									}
									else
									{
									%>												
									-
									<%
									}
									%>
									</td>		
									<td>
									<%
									if(mtTask.get("phone") != null && btnMap != null)
									{
										String phones = CV.replacePhoneNumber(btnMap, mtTask.get("phone").toString());
										out.print(phones);								   
									}
									else
									{
										out.print("-");
									}
									%>
									</td>
									<td>
									<%
									String strTaskid = mtTask.get("taskid") == null?"-":mtTask.get("taskid").toString();
									if("0".equals(strTaskid))
									{
										out.print("-");
									}
									else
									{
										out.print(strTaskid);
									}
									%>
									</td>		
									<td class="ztalign">
									<%
									String erorcode = mtTask.get("errorcode") == null?"" : mtTask.get("errorcode").toString().trim();
									if(erorcode.length() == 0)
									{
										out.print("-");
									}
									else if("DELIVRD".equals(erorcode) || "0".equals(erorcode))
									{
									    out.print(MessageUtils.extractMessage("qyll","qyll_common_146",request));
									}
									else
									{
										if(errCodeDesMap.get(erorcode) != null)
										{
									%>
										<a onclick="showErrDis('<%=erorcode %>')" >[<%=erorcode %>]</a>
									<%
										}
										else
										{
									%>
										<emp:message key="qyll_common_147" defVal="失败" fileName="qyll"/>[<%=erorcode %>]
									<%
										}
									}
									%>
									
									</td>
									<td>
									<%
									String sendtime;
									if(mtTask.get("sendtime") != null)
									{
										Date date = df.parse(mtTask.get("sendtime").toString());
										sendtime = df.format(date);
									}
									else
									{
										sendtime = "-";
									}
									out.print(sendtime);
									%>
									</td>
									<td>
									<%
									String recvtime;
									if(mtTask.get("recvtime") != null)
									{
										Date date = df.parse(mtTask.get("recvtime").toString());
										recvtime = df.format(date);
									}
									else
									{
										recvtime = "-";
									}
									out.print(recvtime);
									%>
									</td>
									<td class="textalign">
									<%
									String svrtype = mtTask.get("svrtype") == null?"" : mtTask.get("svrtype").toString();
									%>
										<xmp><%=busMap.get(svrtype) == null?"-" : MessageUtils.extractMessage("qyll","qyll_common_196",request) %></xmp>
									</td>
									<td><%=mtTask.get("pknumber") == null?"-" : mtTask.get("pknumber").toString() %>/<%=mtTask.get("pktotal") == null?"-" : mtTask.get("pktotal").toString() %></td>
									<td style="text-align:left;">
										<a onclick=javascript:modify(this)>
									<%
									String xmessage = mtTask.get("message") == null?"" : mtTask.get("message").toString();
									%>
								  			<label style="display:none"><xmp><%=xmessage %></xmp></label>
											<xmp><%=xmessage.length()>5?xmessage.substring(0,5)+"...":xmessage %></xmp>
										</a> 
									</td>
									<td>
									<%
										//String strUsermsgid = mtTask.get("usermsgid") == null?"-":mtTask.get("usermsgid").toString();
										//out.print(strUsermsgid);
										//修改优先显示新版的自定义流水号custid，其次再显示旧版的usermsgid
										String custid=mtTask.get("custid").toString();
										String usermsgidStr=mtTask.get("usermsgid").toString();
										if((custid==null||"".equals(custid.trim()))&&usermsgidStr!=null&&!"0".equals(usermsgidStr)){
											out.print(usermsgidStr);
										}else{
											String custidStr=(mtTask.get("custid") == null||"".equals(custid.trim()))?"-":mtTask.get("custid").toString();
											out.print(custidStr);
										}
										
									%>
									</td>
									<%
									if("realTime".equals(recordType))
									{
									%>
									<td><%=mtTask.get("msgfmt") == null?"-" : mtTask.get("msgfmt").toString() %></td>
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
									<td colspan="12">
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
			 
			 
			 <div id="modify" title="<emp:message key="qyll_common_112" defVal="短信内容" fileName="qyll"/>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="240px">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<span style="display:block;width:240px;"><label id="msg" style="width:100%;height:100%;"></label></span>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			
			<div id="errCodeDis" title="<emp:message key="qyll_common_148" defVal="错误说明" fileName="qyll"/>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
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
		
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/qyll_<%=langName%>.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?v=6.0"></script>
 		<script type="text/javascript" src="<%=iPath %>/js/exportexcel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript">
			$(document).ready(function(){
				var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       alert("加载页面失败,请检查网络是否正常!");	
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
            $('#spgate,#userid').isSearchSelect({'width':'179','isInput':true,'zindex':0});
			initPageSyn(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,<%=pageInfo.getNeedNewData() %>, <%=isFirstEnter %>, "<%=loadPageUrl %>");

			$('#search').click(function(){submitForm();});
			
			$('#other').click(function(){location.href="<%=path%>/ll_downRecord.htm?method=mmsFind";});
		});
		
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/que_llOrderDetails.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
