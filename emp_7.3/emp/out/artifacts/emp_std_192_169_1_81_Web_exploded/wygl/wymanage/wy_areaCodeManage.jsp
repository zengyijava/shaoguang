<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("areaCodeManage");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String findResult= (String)request.getAttribute("findresult");
	
	MessageUtils messageUtils = new MessageUtils();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//分页对象	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	@ SuppressWarnings("unchecked")
	List<DynaBean> areaCodeList = (List<DynaBean>) request.getAttribute("areaCodeList");
	//国家地区
	String queryareaname=request.getParameter("queryareaname");
	//国际区号
	String queryareacode=request.getParameter("queryareacode");
	
//增加国家代码
String zjgjdm = messageUtils.extractMessage("txgl", "txgl_wygl_gjdmgl_zjgjdm", request);
//国际区号必须以
String gjqhbxy = messageUtils.extractMessage("txgl", "txgl_wygl_gjdmgl_gjqhbxy", request);
//或
String h = messageUtils.extractMessage("txgl", "txgl_wygl_gjdmgl_h", request);
//开头。
String kt = messageUtils.extractMessage("txgl", "txgl_wygl_gjdmgl_kt", request);
//确  定
String qd = messageUtils.extractMessage("txgl", "txgl_wygl_gjdmgl_qd", request);
//取  消
String qx = messageUtils.extractMessage("txgl", "txgl_wygl_gjdmgl_qx", request);
	
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">

		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode)%>
			<div id="addAreaCode" title="<%=zjgjdm %>" style="padding: 5px; display: none; font-size: 12px;">
				<div style="height:80px;margin-top: 10px;">
					<center>
						<table>
							<tr>
								<td colspan="2"><emp:message key="txgl_wygl_gjdmgl_qagjbzlrgj" defVal="请按国际标准录入国家" fileName="txgl"></emp:message>/<emp:message key="txgl_wygl_gjdmgl_dqjgjqhrzgh" defVal="地区及国际区号，如：中国 0086或+86。" fileName="txgl"></emp:message></td>
							</tr>
							</tr>
							 <tr height="15px"></tr>
							<tr>
							<tr>
								<td align="right" width="130"><emp:message key="txgl_wygl_gjdmgl_gj" defVal="国家" fileName="txgl"></emp:message>/<emp:message key="txgl_wygl_gjdmgl_dq" defVal="地区" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
								<td align="left" width="240">	
									<input type="text" name="areaname" id="areaname" style="width:200px" class="input_bd" maxlength="50"/>
								</td>
							</tr>
							 <tr height="5px"><td colspan="2"></td></tr>
							<tr>
								<td align="right"><emp:message key="txgl_wygl_gjdmgl_gjqh" defVal="国际区号" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
								<td align="left">
									<input type="text" style="width:200px" name="areacode" title='<%=gjqhbxy %>"+"<%=h %>"00"<%=kt %>'
										id="areacode" maxlength="21" class="input_bd"  onblur="phoneInputCtrl($(this))" onkeyup="phoneInputCtrl($(this))"/>
								</td>
							</tr>
						</table>
					</center>
				</div>
				<div style="height: 50px; line-height: 50px; margin-top: 35px;">
					<center>
						<input id="kwok" class="btnClass5 mr23" type="button" value="<%=qd %>"
							onclick="javascript:toAdd()" />
						<input id="kwc" onclick="javascript:btcancel();" class="btnClass6"
							type="button" value="<%=qx %>" /><br/>
					</center>
				</div>
			</div>
			<%-- 内容开始 --%>
			<%
				if(btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="wy_areaCodeManage.htm?method=find" method="post" id="pageForm">
					<div id="loginInfo" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
						if(btnMap.get(menuCode + "-1") != null)
						{
						%>
						<a id="add" onclick="javascript:doAdd()"><emp:message key="txgl_wygl_gjdmgl_tj" defVal="添加" fileName="txgl"></emp:message></a>
						<%
						}
						%>
						<%
						if(btnMap.get(menuCode + "-2") != null)
						{
						%>
						<a id="delete" onclick="javascript:delCheckId()"><emp:message key="txgl_wygl_gjdmgl_sc" defVal="删除" fileName="txgl"></emp:message></a>
						<%
						}
						%>
					</div>
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="txgl_wygl_gjdmgl_gj" defVal="国家" fileName="txgl"></emp:message>/<emp:message key="txgl_wygl_gjdmgl_dq" defVal="地区" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></span>
								</td>
								<td>
									<label>
										<input type="text" name="queryareaname" id="queryareaname"
											style="width: 180px;" value="<%=queryareaname==null?"":queryareaname %>" maxlength="50"/>
									</label>
								</td>

								<td>
									<span><emp:message key="txgl_wygl_gjdmgl_gjqh" defVal="国际区号" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></span>
								</td>
								<td>
									<label>
										<input type="text" name="queryareacode" id="queryareacode" style="width: 180px;" value="<%=queryareacode==null?"":queryareacode %>" 
										maxlength="21" onblur="phoneInputCtrl($(this))" onkeyup="phoneInputCtrl($(this))"/>
									</label>
								</td>
								<td class="tdSer">
									<center>
										<a id="search"></a>
									</center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<input type="checkbox" name="delAll" value="" onclick="checkAlls(this,'delareaCodeId')" />
								</th>
								<th>
									<emp:message key="txgl_wygl_gjdmgl_gj" defVal="国家" fileName="txgl"></emp:message>/<emp:message key="txgl_wygl_gjdmgl_dq" defVal="地区" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wygl_gjdmgl_gjqh" defVal="国际区号" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wygl_gjdmgl_cz" defVal="操作" fileName="txgl"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
						<%
							if(areaCodeList!=null && areaCodeList.size()>0)
							{
								for(DynaBean areaCodeInfo : areaCodeList)
								{
						 %>
								 <tr>
									<td>
										<input type="checkbox" name="delareaCodeId" value="<%=areaCodeInfo.get("id")%>" />
									</td>
									<td class="ztalign">
										<xmp><%=areaCodeInfo.get("areaname")!=null?areaCodeInfo.get("areaname").toString().trim():"-"%></xmp>
									</td>
									<td class="ztalign">
										<xmp><%=areaCodeInfo.get("areacode")!=null?areaCodeInfo.get("areacode").toString().trim():"-"%></xmp>
									</td>
									<td>
									<%
									if(btnMap.get(menuCode + "-2") != null)
									{
									%>
									<a onclick="toDel(<%=areaCodeInfo.get("id")%>)"><emp:message key="txgl_wygl_gjdmgl_sc" defVal="删除" fileName="txgl"></emp:message></a>
									<%
									}
									else
									{
									%>
										-
									<%
									}
								}
									%>
									</td>
								</tr>
							<%
							}
							else
							{
							%>
								<tr>
									<td colspan="4" align="center"><emp:message key="txgl_wygl_gjdmgl_wjl" defVal="无记录" fileName="txgl"></emp:message></td>
								</tr>
							<%
							}
							%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="4">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
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
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/areaCodeManage.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>

		<script type="text/javascript">
			$(document).ready(function() {
				getLoginInfo("#loginInfo");
				
				$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			});
	</script>
	</body>
</html>
