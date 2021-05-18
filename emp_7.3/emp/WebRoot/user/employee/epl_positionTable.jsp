<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.employee.vo.LfEmployeeVo" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo" %>
<%@ page import="java.util.Map" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String bookType=(String)request.getAttribute("bookType");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("position");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
	<%@include file="/common/common.jsp" %>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
		<script>
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		})
		
		deleteleftline();
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		function checkAllTypes(e,str){  
			var a = document.getElementsByName(str);    
			var n = a.length;    
			for (var i=0; i<n; i=i+1)    
				a[i].checked =e.checked;    
		}
		
		</script>
		<table id="content">
			<thead>
				<tr>
				 <th>
						<input type="checkbox" name="dels" value=""
							onclick="checkAllTypes(this,'delTypeBook')" />
					</th>
					<th>
						<emp:message key="employee_dxzs_title_81" defVal="职位" fileName="employee"/>
					</th>
					<%if(!"custorm".equals(bookType)&&btnMap.get(menuCode+"-3")!=null) {%><th><emp:message key="employee_dxzs_title_83" defVal="操作" fileName="employee"/></th><%} %>
				</tr>
			</thead>
			<tbody>
			<%
			if("employee".equals(bookType))
			{
				@ SuppressWarnings("unchecked")
				List<LfEmployeeTypeVo> employeeList=(List<LfEmployeeTypeVo>)session.getAttribute("bookInfoList");
				for(int g=0;g<employeeList.size();g++)
				{
					LfEmployeeTypeVo  employeeInfo=employeeList.get(g);
			%>
				<tr>
					<td>
						<input type="checkbox" name="delTypeBook" id="<%=employeeInfo.getId() %>" value="<%=employeeInfo.getId() %>"/>
					</td> 
					<td><%=employeeInfo.getName().replace("<","&lt;").replace(">","&gt;") %></td>
					<%if(btnMap.get(menuCode+"-3")!=null) {%>
					<td><a onclick="javascript:updType(<%=employeeInfo.getId()%>,'<%=StringEscapeUtils.escapeJavaScript(employeeInfo.getName()) %>')"><emp:message key="employee_dxzs_title_84" defVal="修改" fileName="employee"/></a></td>
					<%} %>
				</tr>
			<%
				}
				
				 if(employeeList.size()==0){%>
					<tr><td colspan="3"><emp:message key="employee_dxzs_title_59" defVal="无记录" fileName="employee"/></td></tr>
			 <%
			 }
			 
			}
			%>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="3">
						<div id="pageInfo"></div>
						<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
						<div>&nbsp;</div>
					</td>
				</tr>
			</tfoot>
		</table>
		