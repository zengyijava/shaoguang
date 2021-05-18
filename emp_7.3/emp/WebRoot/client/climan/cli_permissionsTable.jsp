<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.client.vo.AddrBookPermissionsVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	LfSysuser loginSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
<script>
$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
$("input[name='checkall']").each(function(index){
					$(this).click(
						function(){
							$("input[name='checklist']").attr("checked",$(this).attr("checked")); 
						}
					);
				});
initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
function checkAlls(e,str)    
{  
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
				<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')"/>
			</th>
			<%--
			<th>
				操作员ID
			</th>
			 --%>
			<th>
				<emp:message key="client_khtxlgl_txlqxgl_text_optname" defVal="操作员名称" fileName="client"></emp:message>
			</th>
			<%--<th>
				机构编码
			</th> --%>
			<th>
				<emp:message key="client_khtxlgl_txlqxgl_text_jurisdiction" defVal="管辖机构" fileName="client"></emp:message>
			</th>
		</tr>
	</thead>
	<tbody>
	<%
		@ SuppressWarnings("unchecked")
		List<AddrBookPermissionsVo>	bookList=(List<AddrBookPermissionsVo>)request.getAttribute("bookList");
		if(bookList != null && bookList.size() > 0 )
		{
			for(int g=0;g<bookList.size();g++)
			{
				AddrBookPermissionsVo bookInfo=bookList.get(g);
				 
	%>
		<tr>
			<td>
			 
			 	<%if(!"admin".equals(bookInfo.getUserName())){ %>
					<input type="checkbox" name="checklist" value="<%=bookInfo.getConnId() %>" />
			 	<%}else{ %>
					<input type="checkbox" name="checklistDisabled" value="<%=bookInfo.getConnId() %>" disabled="disabled"/>
			 	<%} %>
		 
			</td>
			<%--<td><%=bookInfo.getUserName() %></td> --%>
			<td class="" ><%=bookInfo.getName() %></td>
			<%--<td><%=bookInfo.getDepCode() %></td> --%>
			<td class="" ><%=bookInfo.getDepName() %></td>
		</tr>
	<%
				}
				
		 }else
			{%>
				<tr><td colspan="3"><emp:message key="client_common_text_norecord" defVal="无记录" fileName="client"></emp:message></td></tr>
		 <%
			}
	%>

	</tbody>
	<tfoot>
		<tr>
		
			<td colspan="3">
				<div id="pageInfo"></div>
			</td>
		</tr>
	</tfoot>
</table>
