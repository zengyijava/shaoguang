<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.employee.LfEmployeeDep"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("empDep");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<LfEmployeeDep> empDepList = (List<LfEmployeeDep>)request.getAttribute("empDepList");
	
	LfSysuser curSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	boolean sortdep = false;
	//String findResult= (String)request.getAttribute("findresult");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<style type="text/css">
			#epl_depTable #singledetail{
				padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;	
			}			
			#epl_depTable #msg{
				width:100%;height:100%;overflow-y:auto;
			}				
			#epl_depTable .user_th1{
				width:40%;
			}				
			#epl_depTable .user_th2{
				width:20%;
			}				
			#epl_depTable #sortdep{
				cursor: pointer;
			}					
			#epl_depTable .sortvalue{
				width:50px;background-color: white;
			}
		</style>
	</head>
	<body id="epl_depTable">
			<div id="singledetail">
				<div id="msg"><xmp></xmp></div>
			</div>
			<div class="dxkf_display_none" id="hiddenValueDiv"></div>
					<table id="content">
					
						<thead>
							<tr>
								<th width="40%">
									<emp:message key="employee_dxzs_title_56" defVal="机构名称" fileName="employee"/>
								</th>
							
								<th width="40%"> 
									<emp:message key="employee_dxzs_title_57" defVal="机构编码" fileName="employee"/>
								</th>
								<th width="20%">
									 <% if(btnMap.get(menuCode+"-10")!=null && empDepList != null && empDepList.size()>0) {  %>
					  				<a id="sortdep"  style="cursor: pointer;" onclick="sortdepclick()"><emp:message key="employee_dxzs_title_58" defVal="排序" fileName="employee"/></a>
					    			<%} %>
								</th>
								
							</tr>
						</thead>
						<tbody>
						<%
							if(empDepList != null && empDepList.size()>0)
							{
								for(LfEmployeeDep dep : empDepList)
								{
						%>
									
							<tr>
								<td class="">
									<%=dep.getDepName() %>
								</td>
								<td class="">
								<%=dep.getDepcodethird()==null?"-":dep.getDepcodethird() %>
								</td>
								<td>
									<input type="text" class="sortvalue" id="<%=dep.getDepId() %>" 
										value="<%=dep.getAddType()==null || dep.getAddType()==0?"":dep.getAddType()%>" 
										disabled="true" style="width:50px;background-color: white;"
										onkeyup="value=value.replace(/[^\d]/g,'')" onblur="value=value.replace(/[^\d]/g,'')" maxlength="5"
									/>
								</td>
							</tr>
						<%			
								}
							}else{ 
						%>
							<tr><td  colspan="3"><emp:message key="employee_dxzs_title_59" defVal="无记录" fileName="employee"/></td></tr>
						<%} %>
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="3">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
		<div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/user/employee/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
			$(document).ready(function(){
				getLoginInfo("#hiddenValueDiv");
				$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});
				
				$("#singledetail").dialog({
					autoOpen: false,
					maxWidth: 350,
					maxHeight: 170,
					modal: true
				});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				deleteleftline();
			});
			function modify(t)
			{
	      			$("#msg").children("xmp").empty();
					$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
					$("#singledetail").dialog("option","title", getJsLocaleMessage('employee','employee_alert_138'));
	      			$("#singledetail").dialog("open");
			}
		        
			var click = false;
			function sortdepclick(){
				if(click){
			        var aa =  $(".sortvalue");
			        var ids = "";
			        var sortids = "";
					if(confirm(getJsLocaleMessage('employee','employee_alert_139'))){
						 for(var i=0;i<aa.length;i++){
							ids += aa[i].id+","; 
							var val = aa[i].value;
							if(val == null || val == ""){
								val = "0";
							}
							sortids += val+",";
						}
						var lgcorpcode = $("#lgcorpcode").val();
						$.post("<%=path%>/epl_empDep.htm?method=doEditSort",
							{ids:ids,sortids:sortids,corpCode:lgcorpcode},function(r){
				            		 if(r != null && r == "true")
				                     {
				                     	alert(getJsLocaleMessage('employee','employee_alert_140'));
				                     	var node = zTree.getSelectedNode();  
				                     	if(node!=null){
				                     		zTree.setting.asyncUrl =  "<%=path%>/epl_empDep.htm?method=getEmpSecondDepJson&depId="+node.id;
				                     	} 	
				                    	zTree.reAsyncChildNodes(zTree.getSelectedNode(), "refresh");	
				                     	submitForm();
				                     }
				                     else
				                     {
				                         alert(getJsLocaleMessage('employee','employee_alert_141'))
				                     }
				               });
					}else{
						$("#sortdep").html(getJsLocaleMessage('employee','employee_alert_142'));
						$("input[class=sortvalue]").attr("disabled",true);
					}
					click = false;
					return;
				}
				click = true;
				$("#sortdep").empty();
				$("#sortdep").html(getJsLocaleMessage('employee','employee_alert_143'));
				$("input[class=sortvalue]").attr("disabled",false);
			}	
		</script>
	</body>
</html>