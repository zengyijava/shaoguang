<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.datasource.LfDBConnect"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
	
Long prId=Long.valueOf(request.getParameter("prId"));
Long serId=Long.valueOf(request.getParameter("serId"));

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("mtService");

LfService service=(LfService)request.getAttribute("service");
LfProcess process=(LfProcess)request.getAttribute("process");
@ SuppressWarnings("unchecked")
List<LfDBConnect> dbList=(List<LfDBConnect>)request.getAttribute("dbList");

@ SuppressWarnings("unchecked")
List<LfTemplate> tmpList=(List<LfTemplate>)request.getAttribute("tmpList");

LfSysuser curSysuser=(LfSysuser)request.getAttribute("curSysuser");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_sjkpz" defVal="数据库配置" fileName="znyq"></emp:message></title>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		function show(){
			<% Object result=request.getAttribute("sqlConfigResult");
					if(result!=null && result.toString().equals("1")){%>
					//alert("数据库配置成功！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkpzcg"));
					$("#subConfigSqlBackBtn").trigger("click");
				<%}else if(result!=null && result.toString().equals("0")){%>
					//alert("操作失败！");	
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
				<%}request.removeAttribute("sqlConfigResult");
				 %>
			}
           $(document).ready(function(){
        	   getLoginInfo("#hiddenValueDiv");
        	   $("#subConfigSqlBtn").click(subConfigSql);
        	    $('#u_o_c_explain').find('> p').next().hide();
      			$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
      			show();
            });
		function checkSubBefore() {
			var dbId = $("#dbId").val();
			var sql = $("#sql").val();
			if (dbId == "" || dbId == null) {
				//alert("数据库连接不能为空");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkljbnwk"));
				return false;
			}
			if (sql == "" || sql == null) {
				//alert("SQL语句不能为空");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sqlyjbnwk"));
				return false;
			}
			return true;
		}
		//获取短信模板内容
		function getSql(sqlId) {
			var $sqlContent = $("form[name='addManualConfig']").find("textarea[name='sql']");
			if(sqlId != "")
			{
				$.post("tem_smsTemplate.htm",{method:"getTmMsg",tmId:sqlId},function(msg)
					{
						$sqlContent.val(msg);
					}
				);
			}else
			{
				location.href=location.href;
				//$sqlContent.val("");
			}
		}
		function subConfigSql() {
	
			if (checkSubBefore()) {
				//if (confirm("确定提交吗 ?")) {
				if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
					$("#subConfigSqlBtn").attr("disabled", "disabled");
					$("#subConfigSqlBackBtn").attr("disabled", "disabled");
					//var msg = $("__tag_40$56_").css("color", "green").text(
					//		"正在提交,请稍后.........");
					var msg = $("__tag_40$56_").css("color", "green").text(
							getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zztjqsh"));
					$("#subConfigSqlBackBtn").parent().append(msg);
					$("#addManualConfig").attr("action",$("#addManualConfig").attr("action")+'&lgcorpcode='+$('#lgcorpcode').val());
					$("#addManualConfig").submit();
				}
			}
		}
		</script>
	</head>
	<body id="eng_mtSqlConfig" class="eng_mtSqlConfig">
		<div id="container">
			<div class="top">
				<div id="top_right">
					<div id="top_left"></div>
					<b><emp:message key="znyq_ywgl_xhywgl_dqwz_mh" defVal="当前位置:" fileName="znyq"></emp:message></b>
					[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]-
					[<%=titleMap.get(menuCode) %><emp:message key="znyq_ywgl_xhywgl_bzglsjkpz" defVal="]-[步骤管理]-[数据库配置]" fileName="znyq"></emp:message>
				</div>
			</div>
			<div class="rCcontent">
				<center>
					<div id="u_o_c_explain">
						<p>
							<emp:message key="znyq_ywgl_xhywgl_smsdpzbz" defVal="说明：手动配置步骤" fileName="znyq"></emp:message>
						</p>
						<ul>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_zbbzz" defVal="在本步骤中，配置人员可手工修改SQL语句" fileName="znyq"></emp:message>
							</li>
						</ul>
					</div>
					<div id="detail_Info">
						<form id="addManualConfig" name="addManualConfig" action="<%=path %>/eng_mtProcess.htm?method=sqlConfig" method="post">
						<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
							<table>
							<thead>
								<tr>
									<td class="twenTd">
										<span><emp:message key="znyq_ywgl_xhywgl_sjklj_mh" defVal="数据库连接：" fileName="znyq"></emp:message></span>
										<input type="hidden" name="prId" value="<%=prId %>"/>
										<input type="hidden" name="serId" value="<%=serId %>"/>
										<input type="hidden" name="hidOpType" value="editSql"/>
									</td>
									<td>
										<label>
										<select name="dbId" id="dbId">
										<option value=""><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
										<%
											for(int i=0;i<dbList.size();i++)
											{
												LfDBConnect dbConn=dbList.get(i);
										%>
											<option value="<%=dbConn.getDbId() %>"
												<%
													if(process.getDbId()-dbConn.getDbId()==0)
													{
														out.print("selected=\"selected\"");
													}
												%>>
												<%=dbConn==null?"":(dbConn.getDbconName()==null?"":dbConn.getDbconName().replace("<","&lt;").replace(">","&gt;")) %>
											</option>
										<%
											}
										%>
										</select>
										</label><font color="red"><emp:message key="znyq_ywgl_xhywgl_xzwbxtglpzdsjklj" defVal="选择外部系统管理配置的数据库连接" fileName="znyq"></emp:message></font>
									</td>
								</tr>
								<tr>
									<td><span><emp:message key="znyq_ywgl_xhywgl_znzqmb" defVal="智能抓取模板：" fileName="znyq"></emp:message></span></td>
									<td><label><select name="tempSel" id="tempSel" onchange="getSql(this.value)">
										<option value="">===<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>===</option>
									<%
										if(tmpList != null && tmpList.size()>0)
										{
											for(LfTemplate temp : tmpList)
											{
									%>
											<option value="<%=temp.getTmid() %>"><%=temp.getTmName().replace("<","&lt;").replace(">","&gt;") %></option>
									<%
											}
										}
									 %>
									</select></label>&nbsp;<a href="javascript:location.href=location.href"><emp:message key="znyq_ywgl_xhywgl_hy" defVal="还原" fileName="znyq"></emp:message></a></td>
								</tr>
								<tr>
									<td>
										<span><emp:message key="znyq_ywgl_xhywgl_sqlyj_mh" defVal="SQL语句：" fileName="znyq"></emp:message></span>
									</td>
									<td>
										<textarea class="textarea_limit" cols="70" rows="10" name="sql" id="sql" readonly="readonly"><%
											if(process.getSql()!=null)
											{
												out.print(process.getSql());
											}
										%></textarea>
									</td>
								</tr>
								<tr>
									<td colspan="4" id="btn">
									<%
										if(service.getUserId()-curSysuser.getUserId()==0 || service.getOwnerId()-curSysuser.getUserId()==0)
										{
									%>
										<input type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="subConfigSqlBtn" class="btnClass1"/>
									<%
										}
									%>
										<input id="subConfigSqlBackBtn" type="button" value="<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message>" 
											onclick="javascript:location.href='eng_mtProcess.htm?serId=<%=serId %>&lguserid='+$('#lguserid').val()" class="btnClass1"/>
										<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
										<br/>
									</td>
								</tr>
								</thead>
							</table>
						</form>
					</div>
				</center>
			</div>
			<%-- 这是每个界面相应的DIV --%>
			<div class="clear"></div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
	</body>
</html>
