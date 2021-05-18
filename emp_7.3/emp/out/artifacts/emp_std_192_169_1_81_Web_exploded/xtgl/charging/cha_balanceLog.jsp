<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.charging.vo.LfDepRechargeLogVo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";//   ->
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo =(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)session.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("balanceMgr");
	List<LfDepRechargeLogVo> lfDepRechargeLogVos = (ArrayList<LfDepRechargeLogVo>)request.getAttribute("lfDepRechargeLogVos");
	LfDepRechargeLogVo logCond = (LfDepRechargeLogVo)request.getAttribute("lfDepRechargeLogVo");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
 %>
<html>
	<head>
		<title>充值/回收日志</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cha_balanceLog.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cha_balanceLog.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="cha_balanceLog">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode,"充值回收日志") %>
			
			<%-- 内容开始 --%>
			<form name="pageForm" action="cha_balanceLog.htm" method="post" id="pageForm">
			<div id="loginUser" class="hidden"></div>
			
			<div id="rContent" class="rContent">
			
			<div class="titletop">
					<table class="titletop_table czhsrz_table" >
						<tr>
							<td class="titletop_td">
								充值回收日志
							</td>
							<td align="right">
								<font class="titletop_font"  onclick="javascript:location.href='<%=path%>/cha_balanceMgr.htm?method=find&lgguid='+$('#lgguid').val()">&larr;&nbsp;返回上一级</font>
							</td>
						</tr>
					</table>
				</div>
					
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					
					<span id="backgo" class="right mr5" onclick="javascript:location.href='<%=path%>/cha_balanceMgr.htm?method=find&operatePageReturn=true&lgguid='+$('#lgguid').val()">&nbsp;返回</span>
					</div>
					<div id="condition">
						<table>
							<tr>
								<td>
									操作时间：
								</td>
								<td>
								<input type="text" class="Wdate beginTime" readonly="readonly" onclick="stime()"
											 value="<%if(logCond != null && logCond.getBeginTime() !=null ){out.print(logCond.getBeginTime());}%>" id="beginTime" name="beginTime"/>
								</td>
								<td>
									至：
								</td>
								<td>
								<input type="text" class="Wdate endTime" readonly="readonly" onclick="rtime()"
											 value="<%if(logCond != null && logCond.getEndTime() !=null ){out.print(logCond.getEndTime());}%>" id="endTime" name="endTime">
								</td>
								<td>
									操作类型：
								</td>
								<td>
									 <select name="opAction" id="opAction" class="opAction" >
					                    	<option value="">全部</option>
					                     	<option value="1"  <%if(logCond != null && logCond.getCount() !=null && logCond.getCount() == 1 ){out.print("selected");}%>>充值</option>
					                      	<option value="-1" <%if(logCond != null && logCond.getCount() !=null && logCond.getCount() == -1 ){out.print("selected");}%>>回收</option>
									</select>
								</td>
								<td class="tdSer">
										<center><a id="search"></a></center>
								</td>
							</tr>
							<tr>
							<td>充入机构：</td>
							<td>
							<input id="dstName" name="dstName"  class="dstName" value="<%if(logCond != null && logCond.getDstName() !=null ){out.print(logCond.getDstName());}%>"/>
							</td>
								
								<td>
									充值员：
								</td>
								<td>
									<input id="opUser" name="opUser"  class="opUser" value="<%if(logCond != null && logCond.getUserName() !=null ){out.print(logCond.getUserName());}%>"/>
								</td>
								<td>
									执行结果：
								</td>
								<td>
									<select name="result"  id="result" class="result">
										<option value="">全部</option>
										<option value="0" <%if(logCond != null && logCond.getResult() !=null && logCond.getResult() == 0 ){out.print("selected");}%>>成功</option>
										<option value="1" <%if(logCond != null && logCond.getResult() !=null && logCond.getResult() == 1 ){out.print("selected");}%>>失败</option>
									</select>
								</td>
								<td></td>
							</tr>
							<tr>
							<td>
									信息类型：
								</td>
								<td >
									<select name="opInfoType" id="opInfoType" class="opInfoType">
										<option value="">全部</option>
										<option value="1" <%if(logCond != null && logCond.getMsgType() !=null && logCond.getMsgType() == 1 ){out.print("selected");}%>>短信</option>
										<option value="2" <%if(logCond != null && logCond.getMsgType() !=null && logCond.getMsgType() == 2 ){out.print("selected");}%>>彩信</option>
									</select>
								</td>
							<td colspan="4">
							&nbsp;
							</td>
							<td></td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									充值机构
								</th>
								<th>
									充入机构
								</th>
								<th>
									充值操作员ID
								</th>
								<th>
									信息类型
								</th><%--
								<th>
									操作类型
								</th>--%>
								<th>
									充值/回收数量(条)
								</th>
								<th class="th_bz">
									备注
								</th>
								<th>
									操作时间
								</th>
								<th>
									执行结果
								</th>
								<%--
								<th>
									描述
								</th>--%>
							</tr>
						</thead>
						<tbody>
						    <%
						        if(lfDepRechargeLogVos == null || lfDepRechargeLogVos.size() == 0){
						            out.print("<tr><td align=\"center\" colspan=\"8\">无记录</td></tr>");
						        }else{
						    %>
						    <%
						        LfDepRechargeLogVo lfDepRechargeLogVo = null;
						    	for(int i=0 ; i < lfDepRechargeLogVos.size() ; i++){
						    	   lfDepRechargeLogVo = lfDepRechargeLogVos.get(i);
						     %>
						    <tr>
								<td class="textalign"><%=lfDepRechargeLogVo.getSrcName() %></td>
								<td class="textalign"><%=lfDepRechargeLogVo.getDstName() %></td>
								<td class="textalign">
									<%=lfDepRechargeLogVo.getUserName() %>
									<%if(lfDepRechargeLogVo.getUserState()==2){out.print("<font color='red'>(已注销)</font>");} %>
								</td>
								<td><%=lfDepRechargeLogVo.getMsgType() == 1?"短信":"<font color='green'>彩信</font>" %></td>
								<%--
								<td>
								<%
								    if(lfDepRechargeLogVo.getCount() > 0){
								        if(lfDepRechargeLogVo.getOptType() == 100){
								        	out.print("总公司为机构充值");
								        }else if(lfDepRechargeLogVo.getOptType() == 101){
								        	out.print("机构为机构充值");
								        }else if(lfDepRechargeLogVo.getOptType() == 102){
								        	out.print("机构为操作员充值");
								        } 
								    }else{
								        if(lfDepRechargeLogVo.getOptType() == 100){
								        	out.print("总公司为机构回收");
								        }else if(lfDepRechargeLogVo.getOptType() == 101){
								        	out.print("机构为机构回收");
								        }else if(lfDepRechargeLogVo.getOptType() == 102){
								        	out.print("机构为操作员回收");
								        } 
								    }
								%></td>--%>
								<td><%=lfDepRechargeLogVo.getCount() >= 0 ?  lfDepRechargeLogVo.getCount() : "<font color='red'>"+lfDepRechargeLogVo.getCount()+"</font>"%></td>
								<td>
								<%
									String memo = lfDepRechargeLogVo.getMemo()==null?"":lfDepRechargeLogVo.getMemo();
									if(memo.length()<10){
										%>
										<xmp><%=memo %></xmp>
										<%
									}else{
										%>
										<a class="memo-label">
								    		<label class="memo_label_2"><xmp><%=memo %></xmp></label>
								    		<xmp><%=StringUtils.abbreviate(memo,10) %></xmp>
							      		</a>
										<%
									}
								%>
								</td>
								<td><%java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  %>
										  <%if(lfDepRechargeLogVo.getOptDate()==null||"".equals(lfDepRechargeLogVo.getOptDate())){ %>
										 - <%}else{ %>
										 <%=df.format(lfDepRechargeLogVo.getOptDate()) %><%} %></td>
								<td><%=lfDepRechargeLogVo.getResult() == 0 ? "成功":"<font color='red'>失败</font>"%></td>
								<%--  <td><%=lfDepRechargeLogVo.getResult() == 0 ? lfDepRechargeLogVo.getOptInfo():"<font color='red'>"+lfDepRechargeLogVo.getOptInfo()+"</font>"%></td>--%>
							</tr>
						    	    <%
						    	}}
						     %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="8">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"> 
					</div>
				</div>
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
		<div id="memo-div" title="备注详情"  class="memo-div">
				<div class="memo-div-div"><xmp></xmp></div>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/cha_balanceLog.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#loginUser");
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
			$('#search').click(function(){submitForm();});
			$('.memo-label').bind('click',function(){
				$('#memo-div').dialog({
					autoOpen: false,
					resizable: false,
					width:250,
				    height:200
				});
				$("#memo-div").find("xmp").text($(this).children("label").children("xmp").text());
				$('#memo-div').dialog('open');
			});
		});
		</script>
	</body>
</html>
