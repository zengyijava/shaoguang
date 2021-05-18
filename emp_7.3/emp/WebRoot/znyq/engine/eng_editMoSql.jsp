<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.datasource.LfDBConnect"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
		
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
	@ SuppressWarnings("unchecked")
	List<LfTemplate> tmpList=(List<LfTemplate>)request.getAttribute("tmpList");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	LfProcess process = (LfProcess)request.getAttribute("process");
	@ SuppressWarnings("unchecked")
	List<LfDBConnect> dbList =(List<LfDBConnect>)request.getAttribute("dbList");
	String prId = (String)request.getAttribute("prId");
	String serId = (String)request.getAttribute("serId");
		
	String lguserid = (String)request.getAttribute("lguserid");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	</head>

	<body onload="show()" id="eng_editMoSql" class="eng_editMoSql">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" >
		<%--
		<div id="u_o_c_explain">
			<p>
			说明：手动配置步骤
		</p>
		<ul>
			<li>
				在本步骤中，配置人员可手工修改SQL语句
			</li>
			<li>
				数据库连接：选择要操作的数据库
			</li>
			<li>
				SQL语句：输入用于执行的sql语句，sql语句可以带执行条件，如select * from xxx where name = '#P_0#'，其中#P_0#为上行信息中的手机号，#P_1#，#P_2#，..,#P_n#为短信中的用户参数
			</li>
		</ul>
		</div>
		 --%>
			<div id="detail">
			<form id="addManualConfig" name="addManualConfig" action="<%=path %>/eng_moService.htm?method=upSql" method="post">
				<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
				<table class="dbConnectTable">
				<thead>
					<tr class="dbConnectTr">
						<td>
							<span><emp:message key="znyq_ywgl_sxywgl_sjklj_mh" defVal="数据库连接：" fileName="znyq"></emp:message></span>
							<input type="hidden" name="prId" value="<%=prId%>"/>
							<input type="hidden" name="serId" value="<%=serId %>"/>
							<input type="hidden" name="lguserid" id="lguserid" value="<%=lguserid %>"/>
							<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=lgcorpcode %>"/>
						</td>
						<td>
							<label>
							<select class="input_bd" name="dbId" id="dbId">
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
							</label><%-- <font color="red">选择外部系统管理配置的数据库连接</font> --%>
						</td>
					</tr>
					<tr class="tempSelTr">
						<td><span><emp:message key="znyq_ywgl_sxywgl_znzqmb" defVal="智能抓取模板：" fileName="znyq"></emp:message></span></td>
						<td><label><select class="input_bd" name="tempSel" id="tempSel" onchange="getSql2(this.value)">
						<option value="">===<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>===</option>
						<%
							if(tmpList != null && tmpList.size()>0)
							{
								for(LfTemplate temp : tmpList)
								{
						%>
								<option value="<%=temp.getTmid() %>" <% if(temp.getTmid().equals(process.getTemplateId())){ %>selected="selected"<%} %>><%=temp.getTmName().replace("<","&lt;").replace(">","&gt;") %></option>
						<%
								}
							}
						 %>
						</select></label>&nbsp;<a href="javascript:location.href=location.href"><emp:message key="znyq_ywgl_sxywgl_hy" defVal="还原" fileName="znyq"></emp:message></a></td>
					</tr>
					<tr>
						<td>
							<span><emp:message key="znyq_ywgl_sxywgl_sqlyj_mh" defVal="SQL语句：" fileName="znyq"></emp:message></span>
						</td>
						<td>
							<textarea class="textarea_limit" name="sql" id="sql"><%
								if(process.getSql()!=null)
								{
									out.print(process.getSql());
								}
							%></textarea>
							<xmp id="sqlXmp" name="sqlXmp" style="display:none"><%=(process.getSql()==null?"":process.getSql())%></xmp>
						</td>
					</tr>
					<tr>
						<td colspan="2" id="btn">
							<input type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="subConfigSqlBtn" class="btnClass5 mr23"/>
							<input id="subConfigSqlBackBtn" type="button" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" onclick="javascript:window.parent.closeDatabaseDiv()" class="btnClass6"/>
							<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
							<br/>
						</td>
					</tr>
					</thead>
				</table>
			</form>
	</div>
				<div class="clear"></div>
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
    <div class="clear"></div>
    
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    	<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_editMoSql.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		
		function show(){
		<% String result=(String)request.getAttribute("result");
				if(result!=null && "true" == result){%>
				//alert("数据库配置成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_sjkpzcg"));
				$("#subConfigSqlBackBtn").trigger("click");
			<%}else if(result!=null && "false" == result){%>
				//alert("操作失败！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czsb"));	
		    <%  }%>
		}

        $(document).ready(function(){
            //用textarea显示短信内容
            $("#sql").empty();
            $("#sql").text($("#sqlXmp").text());
        });
    </script>
		
	</body>
</html>
