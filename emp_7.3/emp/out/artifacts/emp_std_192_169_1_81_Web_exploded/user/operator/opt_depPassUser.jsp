<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %> 
<%@ page import="com.montnets.emp.entity.system.LfdepPassUser"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	 String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	@ SuppressWarnings("unchecked")
	List<LfdepPassUser> userList =(List<LfdepPassUser>) request.getAttribute("passUserList");
	
	String depId=(String)request.getAttribute("depId");
	String depName=(String)request.getAttribute("depName");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("department");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>密码接收者</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=path%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=empBasePath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=path%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=path%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_depPassUser.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="opt_depPassUser">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" value="<%=path %>" id="pathUrl" name="pathUrl">
			<input type="hidden" value="<%=depId %>" id="depId" name="depId">
			<input type="hidden" value="<%=depName %>" id="depName" name="depName">
			<form name="pageForm" action="<%=path %>/opt_department.htm?method=getPassUser" method="post" id="pageForm">
					<div id="getloginUser" class="getloginUser">
					</div>
					<div class="buttons add_div"  >
							<a href='javascript:addPassUser()' id='add' ><emp:message key="user_xtgl_czygl_text_125" 
										defVal="新增" fileName="user" /></a>
							<a href='javascript:deletePassUser()' id='delete' ><emp:message key="common_btn_7" 
										defVal="删除" fileName="common" /></a>
						<span id="backgo" class="right mr5" onclick="javascript:location.href='<%=path%>/opt_department.htm?method=find&lgguid='+$('#lgguid').val()">&nbsp;返回</span>
					</div>
					
					<table id="content" class="content_table">
						<thead>
							<tr>
								<th class="content_table_th1">
									<input type='checkbox' name='checkall' id='checkall' onclick='checkAlls(this)' />
								</th>
								<th class="content_table_th2">
								   <emp:message key="user_xtgl_czygl_text_56" 
										defVal="姓名" fileName="user" />	 
								</th>
								<th class="content_table_th3">
								   <emp:message key="user_xtgl_czygl_text_57" 
										defVal="工号" fileName="user" />	 
								</th>
								<th class="content_table_th4">
									<emp:message key="user_xtgl_czygl_text_58" 
										defVal="手机号码" fileName="user" />
								</th>
							</tr>
						</thead>
						<tbody>
								<%
									if(userList != null && userList.size()>0)
									{
										for(LfdepPassUser user : userList)
										{
								%>
									<tr>
										<td class="content_table_th1">
											<input type='checkbox' name='checklist' value='<%=user.getUserId() %>'/> 
										</td>
										<td class="content_table_th2">
											<%=user.getName().replaceAll("<","&lt;").replaceAll(">","&gt;")%>
											(<%=user.getDepName().replaceAll("<","&lt;").replaceAll(">","&gt;")%>)
										</td>
										<td class="content_table_th3">
										   	 <%
										   	 	if(user.getWorknumber()!=null && !"".equals(user.getWorknumber())){
										   	 		out.print(user.getWorknumber());
										   	 	}else{
										   	 		out.print("-");
										   	 	}
										   	 %>
										</td>
										<td class="content_table_th4">
												<%
													String moblie = user.getMobile();
												    Integer len = moblie.length();
													if(len == 11){
														moblie = moblie.substring(0,3)+ "****" + moblie.substring(len-4,len);
													}
												%>
											<%=moblie%>
										</td>
									</tr>
								<%
										}
									}else{
									%>
										<tr><td align="center" colspan="4"><emp:message key="user_xtgl_czygl_text_59" 
										defVal="无记录" fileName="user" /></td></tr>
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
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- 选择人员的弹出框 --%>
			<div id="infoDiv" title="<emp:message key="user_xtgl_czygl_text_60" 
										defVal="新建密码接收人" fileName="user" />" class="infoDiv">
					<iframe id="flowFrame" name="flowFrame" src="<%=inheritPath %>/operator/opt_depPassUserInfo.jsp" class="flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
					<table>
						<tr>
							<td class="sureBtn_td" >
								<input type="button"   id="sureBtn"  value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" class="btnClass5" onclick="doAddPassUser();" />
									&nbsp;<input type="button"  value="<emp:message key="common_btn_16" 
										defVal="取消" fileName="common" />" class="btnClass6" onclick="doCloseDiv();" />
							</td>
						</tr>
					</table>
			</div>
			
		</div>
    <div class="clear"></div>
	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path%>/user/operator/js/depPassUser.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    	<script>
			$(document).ready(function(){
				getLoginInfo("#getloginUser");
				 var findresult="<%=request.getAttribute("findresult")%>";
				    if(findresult != null && findresult !="" && findresult=="-1")
				    {
				       alert(getJsLocaleMessage("user","user_xtgl_czygl_text_148"));	
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
					
					$("#infoDiv").dialog({
						autoOpen: false,
						height:450,
						width: 360,
						resizable:false,
						modal: true
					});
					
					
					initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
					deleteleftline1();
					//$('#search').click(function(){submitForm();});
			});	
	</script>
	</body>
</html>
