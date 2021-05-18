<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.pasgroup.entity.Userdata"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@page import="com.montnets.emp.security.context.ErrorLoger"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath
			.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request
			.getAttribute("conditionMap");
	@SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>) request
			.getAttribute("userdataList");
	@ SuppressWarnings("unchecked")
	Map<String, String> keyIdMap = (Map<String, String>)request
			.getAttribute("keyIdMap");
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<Long, String> keyIdUidMap = (Map<Long, String>)request
			.getAttribute("keyIdUidMap");
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session
			.getAttribute("titleMap");
	String menuCode = "1900-1480";
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String skin = session.getAttribute("stlyeSkin") == null ? "default"
			: (String) session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//代理账号
	String dlhhh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_dlhhh", request);
    if(dlhhh!=null&&dlhhh.length()>1){
    	dlhhh = dlhhh.substring(0,dlhhh.length()-1);
    }
	//账户名称
	String zhmc = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_zhmc", request);
    if(zhmc!=null&&zhmc.length()>1){
    	zhmc = zhmc.substring(0,zhmc.length()-1);
    }
	//账户状态
	String zhzt = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_zhzt", request);
    if(zhzt!=null&&zhzt.length()>1){
    	zhzt = zhzt.substring(0,zhzt.length()-1);
    }
    //绑定IP详情
    String bdipxq = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dlzhgl_bdipxq", request);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_dlzhgl_dlzhcx" defVal="代理账号查询" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pag_proxyMage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="pag_proxyMage">
		<div id="container" class="container">
			<%-- 当前位置 --%>
				<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<form name="pageForm" action="pag_proxyMage.htm" method="post"
					id="pageForm">
					<div id="corpCode" class="hidden"></div>
			<%
				if (btnMap.get(menuCode + "-0") != null) {
			%>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
							if (btnMap.get(menuCode + "-1") != null) {
						%>
						<a id="add" href="<%=path%>/pag_proxyMage.htm?method=toAdd"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
						<%
							}
						%>
						
					</div>
					<div id="condition">
						<table>
							<tr>
							<td>
								<span><emp:message key="txgl_wgqdpz_dcspzh_dlhhh" defVal="代理账号：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
								<%
									String userId = request.getParameter("userId");
								%>
									<input type="text" name="userId" class="userId" id="userId" value="<%=userId == null ? "" : userId.replace("&", "&amp;")
						.replace("\"", "&quot;")%>"
										onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')" />
								</label>
							</td>
							<td>
								<span><emp:message key="txgl_wgqdpz_dcspzh_zhmc" defVal="账户名称：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
								<%
									String staffName = request.getParameter("staffName");
								%>
									<input type="text" name="staffName" id="staffName" value="<%=staffName == null ? "" : staffName.replace("&",
						"&amp;").replace("\"", "&quot;")%>" />
								</label>
							</td>
							<td>
							<td>
								<span><emp:message key="txgl_wgqdpz_dcspzh_zhzt" defVal="账户状态：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<%
										String status = request.getParameter("status");
									%>
									<select name="status" id="status" isInput="false" >
										<option value="">
											<emp:message key="txgl_wgqdpz_qyhdgl_qb" defVal="全部" fileName="txgl"></emp:message>
										</option>
										<option value="0" <%="0".equals(status) ? "selected" : ""%>>
											<emp:message key="txgl_wgqdpz_dcspzh_yjh" defVal="已激活" fileName="txgl"></emp:message>
										</option>
										<option value="1" <%="1".equals(status) ? "selected" : ""%>>
											<emp:message key="txgl_wgqdpz_dcspzh_ysx" defVal="已失效" fileName="txgl"></emp:message>
										</option>
									</select>
								</label>
							</td>
							<td class="tdSer"><center><a id="search"></a></center></td>
						</tr>
						</table>
					</div>
					<table id="content" class="content_table">
						<thead>
							<tr >
								<th>
									<%=dlhhh %>
								</th>
								<th>
									<%=zhmc %>
								</th>
								<th>
									<%=zhzt %>
								</th>
								<th>
									<emp:message key="txgl_mwgateway_text_5" defVal="绑定IP" fileName="txgl"></emp:message>
								</th>
									<th>
									<emp:message key="txgl_wgqdpz_dlzhgl_cjsj" defVal="创建时间" fileName="txgl"></emp:message>
								</th>
									<th>
									<emp:message key="txgl_wgqdpz_dlzhgl_xgsj" defVal="修改时间" fileName="txgl"></emp:message>
								</th>								
								<th>
									<emp:message key="txgl_wgqdpz_yyshdgl_cz" defVal="操作" fileName="txgl"></emp:message>
								</th>

							</tr>
						</thead>
						<tbody>
						<%
							if (userList != null && userList.size() > 0) {
									String keyId;
									String keyIdUid;
									for (int i = 0; i < userList.size(); i++) {
										Userdata user = userList.get(i);
										keyId = keyIdMap.get(user.getUserId());
										keyIdUid = keyIdUidMap.get(user.getUid());
						%>
							<tr>
								<td><xmp><%=user.getUserId()%></xmp></td>
								<td class="textalign"><xmp><%=user.getStaffName()%></xmp></td>
								<%
									if (btnMap.get(menuCode + "-2") != null) {
								%>
									<td class="ztalign">	
									<center>
									<div class="setControl"></div>
										<%
											if (user.getStatus() == 0) {
										%>
											<select  name="userState<%=user.getUid()%>" id="userState<%=user.getUid()%>" idx="<%=user.getUid()%>" keyidx="<%=keyIdUid%>" class="input_bd" onchange="javascript:changestate('<%=user.getUid()%>', '<%=keyIdUid%>')">
									          <option value="0" selected="selected"><emp:message key="txgl_wgqdpz_dcspzh_yjh" defVal="已激活" fileName="txgl"></emp:message></option>
									          <option value="1" ><emp:message key="txgl_wgqdpz_dcspzh_sx" defVal="失效" fileName="txgl"></emp:message></option>
									        </select>
										<%
											} else {
										%>
										   <select  name="userState<%=user.getUid()%>" id="userState<%=user.getUid()%>" idx="<%=user.getUid()%>"  keyidx="<%=keyIdUid%>" class="input_bd" onchange="javascript:changestate('<%=user.getUid()%>', '<%=keyIdUid%>')">
									          <option value="1" selected="selected"><emp:message key="txgl_wgqdpz_dcspzh_ysx" defVal="已失效" fileName="txgl"></emp:message></option>
									          <option value="0" ><emp:message key="txgl_wgqdpz_dcspzh_jh" defVal="激活" fileName="txgl"></emp:message></option>									          
									        </select>
										<%
											}
										%>									

									</center>								
									</td>
									<%
										} else {
									%>
									<td class="ztalign">
										<%
											if (user.getStatus() == 0) {
										%>
										<emp:message key="txgl_wgqdpz_dcspzh_yjh" defVal="已激活" fileName="txgl"></emp:message>
										<%
											} else {
										%>
										<emp:message key="txgl_wgqdpz_dcspzh_ysx" defVal="已失效" fileName="txgl"></emp:message>
										<%
											}
										%>
									</td>
									<%
										}
									%>
									
									
							<td class="ips-td">
								<%
								String ips = user.getLoginIp();
								String[] ipArr = null;
								if(ips!=null&&!"".equals(ips.trim())){
									ipArr = ips.split(",");
									out.print("<a>"+ipArr[0]+"</a>");
								}else{
									out.print("-");
								}
								if(ipArr != null){
									%>
									<div>
									<%
									for(String ip:ipArr){
										out.print(ip+"<br/>");
									}
									%>
									</div>
									<%
								}
								%>
								</td>


								
								<td>
								<%
								out.print(sd.format(user.getOrderTime()));
								%>
								</td>
								<td>
								<%
								out.print(sd.format(user.getModitime()));
								%>
								</td>
								<%
									if (btnMap.get(menuCode + "-2") != null) {
								%>
								<td>
									<a href="pag_proxyMage.htm?method=toEdit&userid=<%=user.getUserId()%>&accouttype=<%=user.getAccouttype()%>&keyId=<%=keyId %>"><emp:message key="txgl_wygl_wytdgl_xg" defVal="修改" fileName="txgl"></emp:message></a>
								</td>
								<%
									}else{%>
										<td>
									-
								</td>
									<%}
												
								%>
							</tr>
						<%
							}
								} else {
						%>
						<tr><Td colspan="9"><emp:message key="txgl_wgqdpz_qyhdgl_wjl" defVal="无记录" fileName="txgl"></emp:message></Td></tr><%
							}
						%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
			</div>
			<%
				}
			%>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div id="ips" title="<%=bdipxq %>"></div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/userdata.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#corpCode");
			var findresult="<%=(String) request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_1"));
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
			$('#search').click(function(){submitForm();});
			$('#content select').isSearchSelectNew({'width':'60','isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
			  var idx=$(data.box.self).attr("idx");
			  var keyidx=$(data.box.self).attr("keyidx");
			  changestate(idx, keyidx);
			  var parents=$(data.box.self).parent().parent().parent();
			  parents.siblings().removeClass('c_selectedBg');
			  parents.addClass('c_selectedBg');
		    },function(data){
		    	 var self=$(data.box.self);
	  			self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
		    });
		    $('#loginId,#status').isSearchSelect({'width':'156','isInput':false,'zindex':0});
			$('#ips').dialog({
				autoOpen: false,
				resizable:false,
				height: 200,
				width: 160,
				modal:true
			});
			$('.ips-td>a').bind('click',function(){
				$('#ips').html($(this).next('div').html());
				$('#ips').dialog('open');
			});
			});
		</script>
	</body>
</html>
