<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
List<DynaBean> beans=(List<DynaBean>)request.getAttribute("custUserList");
@ SuppressWarnings("unchecked")
List<LfWeiAccount> otWeiAcctList=(List<LfWeiAccount>)request.getAttribute("otWeiAcctList");

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("custList");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
	
String httpUrl = StaticValue.getFileServerViewurl();

String aId = request.getParameter("aId");
String name = request.getParameter("name");
//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String methodText1 =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_customer_title_9", request);
String methodText2 =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_customer_title_10", request);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css">
		<link href="<%=commonPath%>/common/css/select.css" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style type="text/css">
			.c_selectBox, #condition .c_selectBox ul, #condition .c_selectBox ul li{
				width:208px!important;
			}
		</style>
	</head>
	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		
		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			
			<form name="pageForm" action="<%=path %>/zxkf_custList.htm?method=find" method="post"
					id="pageForm">
					<div  style="display:none" id="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
					<div id="condition">
					
						<table >
							<tr>
							   <td ><emp:message key="zxkf_customer_title_1" defVal="公众帐号" fileName="zxkf"/>：</td>
							   <td>
								<select id="aId" name="aId" style="width: 184px">
									<option value=""><emp:message key="zxkf_customer_title_8" defVal="全部" fileName="zxkf"/></option>
									<option value="0" <%="0".equals(aId)?"selected":"" %>><emp:message key="zxkf_customer_title_7" defVal="未绑定" fileName="zxkf"/></option>
									<%
											if (otWeiAcctList != null && otWeiAcctList.size() > 0) {
												for (LfWeiAccount acct : otWeiAcctList) {
													String aid = String.valueOf(acct.getAId());
										%>
										<option value="<%=acct.getAId()%>"
											<%=(aid.equals(aId)) ? "selected" : ""%>>
											<%=acct.getName()%></option>
										<%
											}
											}
										%>
								</select>
								</td>
							   <td ><emp:message key="zxkf_customer_title_2" defVal="客服名称" fileName="zxkf"/>：</td>
							   <td><input type="text" style="width: 180px" name="name" id="name" value="<%=name==null?"":name %>" />
							   </td>
							   <td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
								<tr> 
								 <th>
										<emp:message key="zxkf_customer_title_2" defVal="客服名称" fileName="zxkf"/>
									</th>
									<th>
										<emp:message key="zxkf_customer_title_3" defVal="客服帐号" fileName="zxkf"/>
									</th>
									<th>
										<emp:message key="zxkf_customer_title_4" defVal="状态" fileName="zxkf"/>
									</th>
									<th>
										<emp:message key="zxkf_customer_title_1" defVal="公众帐号" fileName="zxkf"/>
									</th>
									<th>
										<emp:message key="zxkf_customer_title_5" defVal="创建时间" fileName="zxkf"/>
									</th>
									<th>
										<emp:message key="zxkf_customer_title_6" defVal="操作" fileName="zxkf"/>
									</th>
								</tr>	
							</thead>
							<tbody>
							<%
							if(beans != null && beans.size() > 0)
						    {
						        for (DynaBean bean : beans)
						        {
							%>
								<tr>
									<td>
										<%=StringUtils.escapeString((bean.get("name")==null||"".equals(bean.get("name"))) ? "-" : String.valueOf(bean.get("name")))%>
									</td>
									<td>
										<%=StringUtils.escapeString((bean.get("user_name")==null||"".equals(bean.get("user_name"))) ? "-" : String.valueOf(bean.get("user_name")))%>
									</td>
									<td>
										<%
										   if(bean.get("user_state") != null && "1".equals(String.valueOf(bean.get("user_state"))))
										   {
										%>
										<emp:message key="zxkf_customer_content_10" defVal="启用" fileName="zxkf"/>
										<%
										   }else{
										%>
										<emp:message key="zxkf_customer_content_11" defVal="禁用" fileName="zxkf"/>
										<%
										   }
										%>
									</td>
									<td>
										<label id="acna<%=bean.get("user_id") %>"><%
										   if(bean.get("acctname") != null && !"".equals(bean.get("acctname")))
										   {
										%>
										<%=bean.get("acctname")%>
										<%
										   }else{
											   out.print("-");
										} %></label>
									</td>
									<td>
										<%=StaticValue.DBTYPE==StaticValue.ORACLE_DBTYPE?
												bean.get("reg_time"):df.format(bean.get("reg_time"))%>
									</td>
									<td>
										<%
										String jsMethod = "doBind";
										String methodText = methodText1; 
										if(bean.get("a_id") != null)
										{
											jsMethod = "doUnBind";
											methodText = methodText2; 
										}
										%>
										<label id="tlink<%=bean.get("user_id") %>">
											<a href="javascript:<%=jsMethod %>(<%=bean.get("user_id") %>)"><%=methodText %></a>
										</label>
										<input type="hidden" id="aid<%=bean.get("user_id") %>" value="<%=bean.get("a_id") %>"/>
									</td>
								</tr>
							<%
								}
							}
				            else{
							%>			
							<tr><td colspan="6" align="center"><emp:message key="zxkf_customer_content_9" defVal="无记录" fileName="zxkf"/> </td></tr>
							<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="6">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</form>
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<div id="editDiv" style="display:none;">
		<div style="margin:0 auto;padding-left:30px">
		<br/>
			<div style="text-align:left;margin-bottom:10px;">
			<input type="hidden" id="hduserid" value=""/>
			<emp:message key="zxkf_customer_title_11" defVal="请选择需要绑定的公众帐号" fileName="zxkf"/>：</div>
			<select id="bindId" name="bindId" style="width: 184px">
				<%
						if (otWeiAcctList != null && otWeiAcctList.size() > 0) {
							for (LfWeiAccount acct : otWeiAcctList) {
								String aid = String.valueOf(acct.getAId());
					%>
					<option value="<%=acct.getAId()%>"><%=acct.getName()%></option>
					<%
						}
						}
					%>
			</select>
			<div style="margin-bottom:10px;">
			</div>
			<input class="btnClass5 mr5" type="button"  value="<emp:message key='zxkf_button_1' defVal='aaa' fileName='zxkf'/>" onclick="javascript:update()"/>
			<input class="btnClass6" type="button" value="<emp:message key='zxkf_button_2' defVal='取消' fileName='zxkf'/>" onclick="javascript:$('#editDiv').dialog('close');"/>
		</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/zxkf_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
		
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
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
		});
		
		$("#editDiv").dialog({
			title:getJsLocaleMessage('zxkf','zxkf_customer_tille_9'),
			autoOpen: false,
			height: 150,
			width: 245,
			modal:true
		});
		function doUnBind(userid)
		{
			if(confirm(getJsLocaleMessage('zxkf','zxkf_customer_content_1')))
			{
				$.post("zxkf_custList.htm?method=doUnBind",{userid:userid,aid:$("#aid"+userid).val()},function(result){
					if(result=="success")
					{
						alert(getJsLocaleMessage('zxkf','zxkf_customer_content_2'));
						$("#acna"+userid).text("-");
						var xx = getJsLocaleMessage('zxkf','zxkf_customer_tille_9');
						var ahtm = "<a href='javascript:doBind("+userid+")'>"+xx+"</a>";
						$("#tlink"+userid).html(ahtm);
					}else if(result == "online")
					{
						alert(getJsLocaleMessage('zxkf','zxkf_customer_content_3'));
					}else
					if(result == "inserver")
					{
						alert(getJsLocaleMessage('zxkf','zxkf_customer_content_4'));
					}else
					{
						alert(getJsLocaleMessage('zxkf','zxkf_customer_content_5'));
					}
				});
			}
		}
		function update()
		{
			$("input").attr("disabled","disabled");
			var hduserid = $("#hduserid").val();
			var selectid = $("#bindId").val();
			var selecttext = $("#bindId > option:selected").text();
			if(confirm(getJsLocaleMessage('zxkf','zxkf_customer_content_6')))
			{
				$.post("zxkf_custList.htm?method=doBind",{userid:hduserid, aid: selectid,isAsync:"yes"},function(result){
					$("input").attr("disabled","");
					if(result=="success")
					{
						alert(getJsLocaleMessage('zxkf','zxkf_customer_content_7'));
						$("#acna"+hduserid).text(selecttext);
						var bb = getJsLocaleMessage('zxkf','zxkf_customer_tille_10');
						var ahtm = "<a href='javascript:doUnBind("+hduserid+")'>"+bb+"</a>";
						$("#tlink"+hduserid).html(ahtm);
						$("#aid"+hduserid).val(selectid);
						$("#editDiv").dialog('close');
					}else
					{
						alert(getJsLocaleMessage('zxkf','zxkf_customer_content_8'));
					}
				});
			}
		}
		function doBind(userid)
		{
			$("#hduserid").val(userid);
			$("#editDiv").dialog('open');
		}
		</script>
		
	</body>
</html>