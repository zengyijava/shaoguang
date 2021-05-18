<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@page import="com.montnets.emp.security.context.ErrorLoger"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath
			.lastIndexOf("/"));
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	


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
	@ SuppressWarnings("unchecked")
	Map<Long, String> keyIdUidMap = (Map<Long, String>)request
			.getAttribute("keyIdUidMap");

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title>接入SP账号参数设置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/frame/frame3.0/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/frame/frame3.0/skin/default/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=commonPath %>/frame/frame3.0/skin/default/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mwp_userData.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mwp_userData.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="mwp_userData">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="mwp_userData.htm" method="post"
					id="pageForm">
					<div id="corpCode" class="hidden"></div>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
					<div id="condition">
						<table>
							<tr>
							<td>
								<span><emp:message key='txgl_mwpasgroup_text_63' defVal='SP账号：' fileName='mwadmin'/></span>
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
								<span><emp:message key='txgl_mwpasgroup_text_38' defVal='账户名称：' fileName='mwadmin'/></span>
							</td>
							<td>
								<label>
								<%
									String staffName = request.getParameter("staffName");
								%>
									<input type="text" name="staffName" id="staffName" value="<%=staffName == null ? "" : staffName.replace("&","&amp;").replace("\"", "&quot;")%>" />
								</label>
							</td>
							<td>
								<span><emp:message key='txgl_mwpasgroup_text_39' defVal='账户状态：' fileName='mwadmin'/></span>
							</td>
							<td>
								<label>
									<%
										String status = request.getParameter("status");
									%>
									<select name="status" id="status" isInput="false" >
										<option value="">
											<emp:message key='txgl_mwpasgroup_text_40' defVal='全部' fileName='mwadmin'/>
										</option>
										<option value="0" <%="0".equals(status) ? "selected" : ""%>>
											<emp:message key='txgl_mwpasgroup_text_41' defVal='已激活' fileName='mwadmin'/>
										</option>
										<option value="1" <%="1".equals(status) ? "selected" : ""%>>
											<emp:message key='txgl_mwpasgroup_text_42' defVal='已失效' fileName='mwadmin'/>
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
									<emp:message key='txgl_mwpasgroup_text_62' defVal='SP账号' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwpasgroup_text_43' defVal='账户名称' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwpasgroup_text_44' defVal='应用类型' fileName='mwadmin'/>
								</th>								
								<th>
									<emp:message key='txgl_mwpasgroup_text_45' defVal='信息类型' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwpasgroup_text_46' defVal='绑定IP' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwpasgroup_text_47' defVal='账户状态' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwpasgroup_text_48' defVal='修改时间' fileName='mwadmin'/>
								</th>
								<th class="cz_th">
									<emp:message key='txgl_mwpasgroup_text_49' defVal='操作' fileName='mwadmin'/>
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
								<td class="ztalign">
									<%
										if (1==user.getSptype()) {
									%>
										<emp:message key='txgl_mwpasgroup_text_50' defVal='EMP应用账户' fileName='mwadmin'/>
									<%
										} else if (2==user.getSptype()) {
									%>
										<emp:message key='txgl_mwpasgroup_text_51' defVal='EMP接入账户' fileName='mwadmin'/>
									<%
										} else if (3==user.getSptype()) {
											out.print(MessageUtils.extractMessage("mwadmin","txgl_mwpasgroup_text_52",request));
										}
									%>
								</td>
								<td class="ztalign">
									<%
										if ("1".equals(user.getAccouttype().toString())) {
									%>
										<font color="#159800"><emp:message key='txgl_mwpasgroup_text_53' defVal='短信SP账户' fileName='mwadmin'/></font>
									<%
										} else {
									%>
										<font color="#f1913c"><emp:message key='txgl_mwpasgroup_text_54' defVal='彩信SP账户' fileName='mwadmin'/></font>
									<%
										}
									%>
								</td>
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
									<td class="ztalign">	
									<center>
									<div class="setControl"></div>
										<%
											if (user.getStatus() == 0) {
										%>
										<emp:message key='txgl_mwpasgroup_text_55' defVal='已激活' fileName='mwadmin'/>
										<%
											} else {
										%>
										<emp:message key='txgl_mwpasgroup_text_56' defVal='已失效' fileName='mwadmin'/>
										<%
											}
										%>									
									</center>								
									</td>
									<td>
									<%
										try {
												out.print(sd.format(user.getOrderTime()));
											} catch (Exception ex) {
												EmpExecutionContext.error(new ErrorLoger().getErrorLog(ex, "用户开户时间异常!"));
												out.print(user.getOrderTime());
											}
									%>
									</td>
									<td>
										<a href="javascript:location.href='mwp_userData.htm?method=toEdit&userid=<%=user.getUserId()%>&accouttype=<%=user.getAccouttype()%>&keyId=<%=keyId %>'"><emp:message key='txgl_mwpasgroup_text_58' defVal='参数设置' fileName='mwadmin'/></a>
										&nbsp;&nbsp;
										<a href="javascript:showEditHttps('<%=user.getUserId()%>', '<%=keyId %>');"><emp:message key='txgl_mwpasgroup_text_59' defVal='HTTPS设置' fileName='mwadmin'/></a>
									</td>
							</tr>
						<%} } else {%>
						<tr><td colspan="8"><emp:message key='txgl_mwpasgroup_text_60' defVal='无记录' fileName='mwadmin'/>
							</td></tr>
						<%}%>
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
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div id="ips" title="<emp:message key='txgl_mwpasgroup_text_61' defVal='绑定IP详情' fileName='mwadmin'/>"></div>
		<div id="editHttpsDiv" title="<emp:message key='txgl_mwpasgroup_text_59' defVal='HTTPS设置' fileName='mwadmin'/>" class="editHttpsDiv">
			<iframe id="editHttpsFrame" name="editHttpsFrame" class="editHttpsFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mwp_userdata.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#corpCode");
			var findresult="<%=(String) request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
                //alert("加载页面失败,请检查网络是否正常!");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_1"));
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
		    $('#status').isSearchSelect({'width':'156','isInput':false,'zindex':0});
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
			
			$("#editHttpsDiv").dialog({
				autoOpen: false,
				height:290,
				width: 590,
				resizable:false,
				modal: true,
				open:function(){
					
				},
				close:function(){
					$("#editHttpsDiv").attr("src","");
				}
			});
			
			});
			
		function closeEditHttpsdiv(){
			$("#editHttpsDiv").dialog("close");
			$("#editHttpsFrame").attr("src","");
		}
		
			
			//打开修改上行业务指令绑定页面
		function showEditHttps(spuser, keyId)
		{
		  	$("#editHttpsFrame").attr("src","<%=path %>/mwp_userData.htm?method=toEditHttps&userid="+spuser+"&keyId="+keyId);
			$("#editHttpsDiv").dialog("open");
		}
		</script>
	</body>
</html>
