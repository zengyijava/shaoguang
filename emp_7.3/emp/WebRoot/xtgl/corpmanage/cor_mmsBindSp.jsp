<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.pasroute.LfMmsAccbind"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>


<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfMmsAccbind> accbindList = (List<LfMmsAccbind>) request.getAttribute("accbindList");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request.getAttribute("conditionMap");
	
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsBindSp");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String SMSACCOUNT = StaticValue.SMSACCOUNT;
	
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_mmsBindSp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cor_mmsBindSp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	</head>
	<body id="cor_mmsBindSp" >
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>
			
			<form name="pageForm" action="<%=path %>/cor_mmsBindSp.htm" method="post" id="pageForm">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<div class="buttons">
					<div id="toggleDiv">
						         </div>
					<a id="add"><emp:message key="xtgl_qygl_xj" defVal="新建" fileName="xtgl"></emp:message></a>
				</div>
				<div id="condition">
					<table>
						<tr>
							<td>
								<span><emp:message key="xtgl_qygl_qybm_mh" defVal="企业编码：" fileName="xtgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="text" name="cropNum" onkeyup="value=value.replace(/[^\d]/g,'')" id="cropNum" value='<%=null==conditionMap.get("corpCode&like")?"":conditionMap.get("corpCode&like") %>' class="cropNum" maxlength="20"/>
								</label>
							</td>
							<td>
								<span>
								<%--<%=SMSACCOUNT %>：
								--%><emp:message key="xtgl_qygl_spzh_mh" defVal="SP账号：" fileName="xtgl"></emp:message>
								</span>
							</td>
							<td>
								<label>
									<input type="text" name="mmsUser" id="mmsUser" value='<%=null==conditionMap.get("mmsUser&like")?"":conditionMap.get("mmsUser&like") %>' class="mmsUser" maxlength="20"/>
								</label>
							</td>
							<td class="tdSer">
											<center><a id="search"></a></center>
									</td>
						</tr>
					</table>
				</div>
				<table id="content">
					<thead>
						<tr >
							<th>
								<emp:message key="xtgl_qygl_qybm" defVal="企业编码" fileName="xtgl"></emp:message>
							</th>
							<th>
							  <%--<%=SMSACCOUNT %>
							  --%><emp:message key="xtgl_qygl_spzh" defVal="SP账号" fileName="xtgl"></emp:message>
							</th>
							<th>
							   	<emp:message key="xtgl_qygl_zt" defVal="状态" fileName="xtgl"></emp:message>
							</th>
							<th>
							   	<emp:message key="xtgl_qygl_cz" defVal="操作" fileName="xtgl"></emp:message>
							</th>
						</tr>
					</thead>
					<tbody>
						<% 
							if(accbindList != null && accbindList.size()>0){
								LfMmsAccbind accBind = null;
								
								for(int i=0;i<accbindList.size();i++)
								{
						
									accBind = accbindList.get(i);
						%>
					
					
					
						<tr>
							<td><%=accBind.getCorpCode() %></td>
							<td><%=accBind.getMmsUser() %></td>
							<td>
								<%if(accBind.getIsValidate() == 1){
								 		//out.print("激活");
								 		out.print(MessageUtils.extractMessage("xtgl","xtgl_qygl_jh",request));
									} else if(accBind.getIsValidate() == 0){
										//out.print("失效");
										out.print(MessageUtils.extractMessage("xtgl","xtgl_qygl_sx",request));
									}
								%>
							</td>
							<td>
								<%if(accBind.getIsValidate() == 1){
								%>
								 	<a name="querySP" href="javascript:doJhSx('1','<%=accBind.getId()%>')"><emp:message key="xtgl_qygl_sx" defVal="失效" fileName="xtgl"></emp:message></a>
						 		<%
						 			}else{
						 		%>
								 	<a name="querySP" href="javascript:doJhSx('2','<%=accBind.getId()%>')"><emp:message key="xtgl_qygl_jh" defVal="激活" fileName="xtgl"></emp:message></a>
								 		
								 <%
						 			}
						 		%>		
							</td>
						</tr>
						<% 
								}
							}else{
						%>
							<tr>
								<td colspan="4" align="center">
									<emp:message key="xtgl_qygl_wjl" defVal="无记录" fileName="xtgl"></emp:message>
								</td>
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
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
            	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsBindSp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
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
			$('#add').click(function(){
				location.href = "<%=path%>/cor_mmsBindSp.htm?method=goAdd&time=<%=System.currentTimeMillis()%>";
			});
		});
		
		</script>
	</body>
</html>
