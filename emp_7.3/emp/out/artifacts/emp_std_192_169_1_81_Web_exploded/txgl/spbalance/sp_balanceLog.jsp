<%@page import="java.sql.Timestamp"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";//   ->
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo =(PageInfo)request.getAttribute("pageInfo");
/* 	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)session.getAttribute("conditionMap"); */
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("spBalanceMgr");
	List<DynaBean> beanList = (List<DynaBean>)request.getAttribute("beanList");
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap)request.getAttribute("conditionMap");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	
	//SP账号
	String spzhh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_spzhh", request);
    if(spzhh!=null&&spzhh.length()>1){
    	spzhh = spzhh.substring(0,spzhh.length()-1);
    }
	//账户名称
	String zhmc = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_zhmc", request);
	//应用类型
	String yylx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_yylx", request);
    if(yylx!=null&&yylx.length()>1){
    	yylx = yylx.substring(0,yylx.length()-1);
    }
	//信息类型
	String xxlx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_xxlx", request);

	//充值/回收数量（条）
	String czhsslt = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_czhsslt", request);
	//执行结果
	String zxjg = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_zxjg", request);
    if(zxjg!=null&&zxjg.length()>1){
    	zxjg = zxjg.substring(0,zxjg.length()-1);
    }
	//备注
	String bz = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_bz", request);
	//充值操作员
	String czjgy = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_czjgy", request);
	//机构
	String jg = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_jg", request);
	//操作时间
	String czsj = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_czsj", request);
	//备注详情
	String bzxq = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_bzxq", request);
	
%>
<html>
<head>
<%@include file="/common/common.jsp" %>
<title><emp:message key="txgl_wgqdpz_spzhczhs_czhsrzz" defVal="充值/回收日志" fileName="txgl"></emp:message></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
      type="text/css" />
<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet"
      type="text/css" />
<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css">
<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/sp_balanceLog.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
</head>
<body id="sp_balanceLog">
	<div id="container" class="container">
		<%--  当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_czhsrz", request))%>
	
		<%-- 内容开始 --%>
		<form name="pageForm" action="sp_balanceLog.htm" method="post"
			id="pageForm">
			<div id="loginUser" class="hidden"></div>

			<div id="rContent" class="rContent">

				<div class="buttons">
					<div id="toggleDiv"></div>

					<span id="backgo" class="right mr5"
						onclick="javascript:location.href='<%=path%>/spb_spBalanceMgr.htm?method=find&operatePageReturn=true&lgguid='+$('#lgguid').val()">&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_fh" defVal="返回" fileName="txgl"></emp:message></span>
				</div>
				<div id="condition">
					<table>
						<tr>
						<%-- 新添加,value待修改 --%>
							<td><emp:message key="txgl_wgqdpz_spzhczhs_spzhh" defVal="SP账号：" fileName="txgl"></emp:message></td>
							<td><input id="spUser" name="spUser"
								class="spUser"
								value="<%if(conditionMap != null && conditionMap.get("spUser") != null){out.print(conditionMap.get("spUser"));}%>" />
							</td>
							<td><emp:message key="txgl_mwgateway_text_2" defVal="信息类型：" fileName="txgl"></emp:message></td>
							<td><select name="msgType" id="msgType"
								class="msgType">
									<option value=""><emp:message key="txgl_wgqdpz_spzhczhs_qb" defVal="全部" fileName="txgl"></emp:message></option>
									<option value="1"
										<%if(conditionMap != null && conditionMap.get("msgType") !=null && Integer.parseInt(conditionMap.get("msgType").toString().trim()) == 1 ){out.print("selected");}%>><emp:message key="txgl_wgqdpz_zhtdpz_dx" defVal="短信" fileName="txgl"></emp:message></option>
									<option value="2"
										<%if(conditionMap != null && conditionMap.get("msgType") !=null && Integer.parseInt(conditionMap.get("msgType").toString().trim()) == 2 ){out.print("selected");}%>><emp:message key="txgl_wgqdpz_zhtdpz_cx" defVal="彩信" fileName="txgl"></emp:message></option>
							</select></td>
							<td><emp:message key="txgl_wgqdpz_spzhczhs_czlx" defVal="操作类型：" fileName="txgl"></emp:message></td>
							<td><select name="opType" id="opType"
								class="opType">
									<option value=""><emp:message key="txgl_wgqdpz_spzhczhs_qb" defVal="全部" fileName="txgl"></emp:message></option>
									<option value="1"
										<%if(conditionMap != null && conditionMap.get("opType") !=null && Integer.parseInt(conditionMap.get("opType").toString().trim()) == 1 ){out.print("selected");}%>><emp:message key="txgl_wgqdpz_spzhczhs_cz" defVal="充值" fileName="txgl"></emp:message></option>
									<option value="-1"
										<%if(conditionMap != null && conditionMap.get("opType") !=null && Integer.parseInt(conditionMap.get("opType").toString().trim()) == -1  ){out.print("selected");}%>><emp:message key="txgl_wgqdpz_spzhczhs_hs" defVal="回收" fileName="txgl"></emp:message></option>
							</select></td>
							<td class="tdSer">
								<center>
									<a id="search"></a>
								</center></td>
						</tr>
						<tr>
						<td><emp:message key="txgl_wgqdpz_spzhczhs_zxjg" defVal="执行结果：" fileName="txgl"></emp:message></td>
							<td><select name="result" id="result" class="result">
									<option value=""><emp:message key="txgl_wgqdpz_spzhczhs_qb" defVal="全部" fileName="txgl"></emp:message></option>
									<option value="0"
										<%if(conditionMap != null && conditionMap.get("result") !=null && Integer.parseInt(conditionMap.get("result").toString().trim()) == 0){out.print("selected");}%>><emp:message key="txgl_wgqdpz_spzhczhs_cg" defVal="成功" fileName="txgl"></emp:message></option>
									<option value="1"
										<%if(conditionMap != null && conditionMap.get("result") !=null && Integer.parseInt(conditionMap.get("result").toString().trim()) == 1 ){out.print("selected");}%>><emp:message key="txgl_wgqdpz_spzhczhs_sb" defVal="失败" fileName="txgl"></emp:message></option>
							</select></td>
							<td><emp:message key="txgl_wgqdpz_spzhczhs_czy" defVal="操作员：" fileName="txgl"></emp:message></td>
							<td><input id="opUser" name="opUser" class="opUser"
								value="<%if(conditionMap != null && conditionMap.get("opUser") != null){out.print(conditionMap.get("opUser"));}%>" />
							</td>
						</tr>
						<tr>
						<td><emp:message key="txgl_wgqdpz_spzhczhs_czsj" defVal="操作时间：" fileName="txgl"></emp:message></td>
							<td><input type="text"
								class="Wdate beginTime" readonly="readonly" onclick="stime()"
								value="<%if(conditionMap != null && conditionMap.get("beginTime") !=null ){out.print(conditionMap.get("beginTime"));}%>"
								id="beginTime" name="beginTime" /></td>
							<td><emp:message key="txgl_wgqdpz_dcspzh_z" defVal="至" fileName="txgl"></emp:message></td>
							<td><input type="text"
								class="Wdate endTime" readonly="readonly" onclick="rtime()"
								value="<%if(conditionMap != null && conditionMap.get("endTime") !=null ){out.print(conditionMap.get("endTime"));}%>"
								id="endTime" name="endTime"></td>
						</tr>
						
						
					</table>
				</div>
				<table id="content">
					<thead>
						<tr>
							<th><%=spzhh %></th>
							<th><%=zhmc %></th>
							<th><%=yylx %></th>
							<th><%=xxlx %></th>
							<th class="czhsslt_th"><%=czhsslt %></th>
							<th><%=zxjg %></th>
							<th class="bz_th"><%=bz %></th>
							<th class="czjgy_th"><%=czjgy %></th>
							<th><%=jg %></th>
							<th class="czsj_th"><%=czsj %></th>
						</tr>
					</thead>
					<tbody>
						<%
							if(beanList == null || beanList.size() == 0){
										            out.print("<tr><td align=\"center\" colspan=\"10\">"+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_qyhdgl_wjl", request)+"</td></tr>");
										        }else{
						%>
						<%
							DynaBean dynaBean = null;
							String spUser;							
							String userName;							
							int spType;							
							int msgType;							
							long count;							
							int result;							
							String memo;							
							String opUser;							
							String depName;							
							String opTime;		
							int	userState;				
										    	for(int i=0 ; i < beanList.size() ; i++){
										    	   dynaBean = beanList.get(i);
										    	   spUser = dynaBean.get("spuser")!=null?dynaBean.get("spuser").toString().trim():"";
										    	   userName = dynaBean.get("staffname") != null ? dynaBean.get("staffname").toString().trim() : "";
										    	   spType = dynaBean.get("sptype") != null ? Integer.parseInt(dynaBean.get("sptype").toString().trim()) : 0;
										    	   msgType = dynaBean.get("accounttype") != null ? Integer.parseInt(dynaBean.get("accounttype").toString().trim()) : -1;
										    	   count = dynaBean.get("icount") != null ? Long.parseLong(dynaBean.get("icount").toString().trim()) : 0;
										    	   result = dynaBean.get("result") != null ? Integer.parseInt(dynaBean.get("result").toString().trim()) : -1;
										    	   memo = dynaBean.get("memo") != null ? dynaBean.get("memo").toString().trim() : "";
										    	   opUser = dynaBean.get("user_name") != null ? dynaBean.get("user_name").toString().trim() : "";
										    	   depName = dynaBean.get("dep_name") != null ? dynaBean.get("dep_name").toString().trim() : "";
										    	   opTime = dynaBean.get("opr_time") != null ? dynaBean.get("opr_time").toString().trim() : "";
										    	   opTime = opTime.substring(0, opTime.lastIndexOf(".") - 1);
										    	   userState = dynaBean.get("user_state") != null ? Integer.parseInt(dynaBean.get("user_state").toString().trim()) : -1;
						%>
						<tr>
							<%-- 新添加 --%>
							<td><%=spUser%></td>
							<td><%=userName%></td>
							<td>
								<%
									if(spType == 1){out.print(MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_empyyzh", request));}
									else if(spType == 2){out.print(MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_empjrzh", request));}
									else if(spType == 3){out.print(MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_zlzh", request));}
									else{}
								 %>
							</td>
							<td><%=msgType == 1?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request):"<font color='green'>"+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request)+"</font>"%></td>
							<td><%=count >= 0 ?  count : "<font color='red'>"+ count +"</font>"%></td>
							<td><%=result == 0 ? MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_cg", request):"<font color='red'>"+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_sb", request)+"</font>"%></td>
							<td><a onclick=javascript:modify(this)> <label
									class="memo_label"><xmp><%=memo%></xmp>
								</label> <xmp><%=memo.length()>15?memo.substring(0,15)+"...":memo%></xmp>
							</a></td>

							<td><%=opUser%>
								<%
									if(userState==2){out.print("<font color='red'>("+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_yzx", request)+")</font>");}
								%>
							</td>
							<td><%=depName%></td>
							<td>
								<%
									java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								%>
								<%
									if(opTime==null||"".equals(opTime)){
								%>
								- <%
									}else{
								%> <%=df.format(df.parse(opTime))%>
								<%
									}
								%>
							</td>
							</tr>
						<%
							}}
						%>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="10">
								<div id="pageInfo"></div></td>
						</tr>
					</tfoot>
				</table>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"></div>
				</div>
			</div>
		</form>
		<%-- foot结束 --%>
	</div>
	
	<div id="memo-div" title="<%=bzxq %>"
		class="memo-div">
		<div class="msg_div">
		<label id="msg" class="msg"><xmp></xmp></label>
		</div>
	</div>
	
	<div class="clear"></div>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/sp_balanceLog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	<script type="text/javascript">
		$(document).ready(
				function() {
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
					initPage(
	<%=pageInfo.getTotalPage()%>
		,
	<%=pageInfo.getPageIndex()%>
		,
	<%=pageInfo.getPageSize()%>
		,
	<%=pageInfo.getTotalRec()%>
		);
					$('#search').click(function() {
						submitForm();
					});
					$('.memo-label').bind(
							'click',
							function() {
								$('#memo-div').dialog({
									autoOpen : false,
									resizable : false,
									width : 250,
									height : 200
								});
								$("#memo-div").find("xmp").text(
										$(this).children("label").children(
												"xmp").text());
								$('#memo-div').dialog('open');
							});
				});
	</script>
</body>
</html>
