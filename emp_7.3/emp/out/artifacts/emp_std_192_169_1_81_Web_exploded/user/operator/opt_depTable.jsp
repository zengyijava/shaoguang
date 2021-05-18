<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("department");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<LfDep> depList = (List<LfDep>)request.getAttribute("depList");
	
	LfSysuser curSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	String lguserid = curSysuser.getUserId().toString();
	HashMap<String,String> encryptmap=   (HashMap)request.getAttribute("encryptmap");
	//String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<html>
	<head>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_depTable.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		<script>

			$(document).ready(function(){
			   //  var findresult="findResult";
			   // if(findresult=="-1")
			//    {
			  //     alert("加载页面失败,请检查网络是否正常!");	
			 //      return;			       
			 //   }
						$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$("#singledetail").dialog({
					autoOpen: false,
					maxWidth: 350,
					maxHeight: 170,
					modal: true
				});
				
				deleteleftline();
			});
			function checkAlls(e,str)    
			{  
				var a = document.getElementsByName(str);    
				var n = a.length;    
				for (var i=0; i<n; i=i+1)    
					a[i].checked =e.checked;    
			}
				function modify(t)
			{
	      			$("#msg").empty();
					$("#msg").text($(t).children("label").children("xmp").text());
					//$("#singledetail").dialog("option","title", getJsLocaleMessage("user","user_xtgl_czygl_text_149"));
	      			$("#singledetail").dialog({
						autoOpen: false,
					    width:350,
					    height:200,
					    title:getJsLocaleMessage("user","user_xtgl_czygl_text_149"),
					    modal:true,
					    resizable:false
					 });
	      			$("#singledetail").dialog("open");
			}
		</script>
	</head>
	<body id="opt_depTable">
			<div id="singledetail" class="singledetail">
				<div id="msg" class="msg"></div>
			</div>
			
			<div class="buttons">
					
				
					<a id="upload"  onclick="javascript:upload('<%=path %>')">
					<emp:message key="user_xtgl_czygl_text_137" defVal="导入" fileName="user"/></a>
					<a id="exportCondition"  onclick="javascript:importExcel()"><emp:message key="user_xtgl_czygl_text_77" defVal="导出" fileName="user"/></a> 
					
				
				</div>
			
					<table id="content">
					
						<thead>
							<tr>
							
							<th>
									<emp:message key="user_xtgl_czygl_text_138" 
										defVal="机构编码" fileName="user" />
								</th>
								<th>
									<emp:message key="user_xtgl_czygl_text_126" 
										defVal="机构名称" fileName="user" />
								</th>
								<%--<th>
									机构编码
								</th>
								 <th>
									上级编码
								</th>--%>
								<th>
									<emp:message key="user_xtgl_czygl_text_127" 
										defVal="机构职责" fileName="user" />
								</th>
								<th>
									<emp:message key="user_xtgl_czygl_text_100" 
										defVal="操作" fileName="user" />
								</th>
							</tr>
						</thead>
						<tbody>
						<%
							if(depList != null && depList.size()>0)
							{
								for(LfDep dep : depList)
								{
								String depidstr=encryptmap.get(dep.getDepId()+"");
						%>
									
							<tr>
							<td class="textalign">
									<%=dep.getDepCodeThird() %>
								</td>
								<td class="textalign">
									<%=dep.getDepName() %>
								</td>
								<%--<td>
									<%=dep.getDepCodeThird() %>
								</td> --%>
								<td class="textalign">
								<%--	<%=dep.getDepResp()==null?"":dep.getDepResp() %> --%>
										<%
										 if(!"".equals(dep.getDepResp())&&dep.getDepResp()!=null){
											String st = "";
											if(dep.getDepResp().length()>35)
											{
												st = dep.getDepResp().substring(0,35)+"...";
											}else
											{
												st = dep.getDepResp();
											}
										%>
										<a onclick="javascript:modify(this)">
										  <label class="getDepResp_label"><xmp><%=dep.getDepResp()%></xmp></label>
										  <xmp><%=st%></xmp>
										  </a> <%}else{ %>		<%} %>
								</td>
								<td>
								<%if(btnMap.get(menuCode+"-3")!=null) { 
								//if(!"0000000000000000".equals(dep.getDepCode()) ||  curSysuser.getUserId()-2==0){ 
								if(dep.getSuperiorId()!=0 ||  curSysuser.getUserId()-2==0){ 
								%>
									<a href="javascript:editDep('<%=depidstr %>')"><emp:message key="user_xtgl_czygl_text_135" 
										defVal="修改" fileName="user" /></a>
									
								<%} }%>
								</td>
							</tr>
						<%			
								}
							}else{
						%>
							<tr>
								<td colspan="4"><emp:message key="user_xtgl_czygl_text_61" 
										defVal="该机构不存在，可能已被删除！" fileName="user" /></td>
							</tr>
						<%} %>
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="4">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
<script type="text/javascript"
		src="<%=iPath%>/js/optUpload.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>