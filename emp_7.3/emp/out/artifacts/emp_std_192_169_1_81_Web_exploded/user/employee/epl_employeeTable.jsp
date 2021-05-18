<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.employee.vo.LfEmployeeVo" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser "%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@page import="java.util.HashMap"%>
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
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("employeeBook");
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

	
	List<LfEmployeeVo> employeeList= new ArrayList<LfEmployeeVo>();
	employeeList=(List<LfEmployeeVo>)session.getAttribute("bookInfoList");
	if(employeeList==null){
		employeeList=new ArrayList<LfEmployeeVo>();
	}
	CommonVariables commonVariables = new CommonVariables();
	HashMap<String,String> encryptmap=   (HashMap)request.getAttribute("encryptmap");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String wz = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_10", request);
	String nan = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_11", request);
	String nv = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_12", request);
	
%>
	<%@include file="/common/common.jsp" %>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/employee_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
		
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		
		//导出全部数据到excel
		//$("#exportCondition").click(
		function importExcel()
		 {
			  if(confirm(getJsLocaleMessage('employee','employee_alert_169')))
			   {
			   		var depId = $("#depId").val();
			   		
			   		var name = $("#name").val();
			   		
			   		var phone = $("#phone").val();
			   		<%
			   		if("employee".equals(bookType)){
				   		if(employeeList !=null && employeeList.size() != 0){%>
				   				$.ajax({
									type: "POST",
									url: "epl_employeeBook.htm?method=exportEmployeeExcel",
									data: {lguserid:$('#lguserid').val(),
											lgcorpcode:$('#lgcorpcode').val(),
											lgguid:$('#lgguid').val()
											},
					                beforeSend:function () {
										page_loading();
					                },
					                complete:function () {
								    	page_complete();
					                },
									success: function(result){
					                        if(result=='true'){
					                           download_href("epl_employeeBook.htm?method=downloadFile");
					                        }else{
					                            alert(getJsLocaleMessage('employee','employee_alert_170'));
					                        }
						   			}
								});
				   		<%}else{
				   		%>
				   		    alert(getJsLocaleMessage('employee','employee_alert_171'));
				   		<%
				   		}
			   		}%>
			   }
		  }//);

		function toEditEmployee(empLoyeeId)
		{
			window.location.href="<%=path %>/epl_employeeBook.htm?method=doEdit&bookId="+empLoyeeId+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		}
		</script>
		
		
		
					
		
		<table id="content">
			<thead>
				<tr>
					 <th>
						<input type="checkbox" name="dels" value=""
							onclick="checkAlls(this,'delBook')" />
					</th>
					<th>
						<emp:message key="employee_dxzs_title_75" defVal="工号" fileName="employee"/>
					</th>
					<th>
						<emp:message key="employee_dxzs_title_76" defVal="姓名" fileName="employee"/>
					</th>
					<th>
						<emp:message key="employee_dxzs_title_77" defVal="登录账号" fileName="employee"/>
					</th>
					<th>
						<emp:message key="employee_dxzs_title_78" defVal="生日" fileName="employee"/>
					</th>
					<th>
						<emp:message key="employee_dxzs_title_79" defVal="性别" fileName="employee"/>
					</th>
					<th>
						<emp:message key="employee_dxzs_title_80" defVal="所属机构" fileName="employee"/>
					</th>
					<th>
						<emp:message key="employee_dxzs_title_81" defVal="职位" fileName="employee"/>
					</th>
					<th>
						<emp:message key="employee_dxzs_title_82" defVal="手机" fileName="employee"/>
					</th>
					<%if("employee".equals(bookType)&&btnMap.get(menuCode+"-3")!=null){%>
					<th><emp:message key="employee_dxzs_title_83" defVal="操作" fileName="employee"/></th>
					<%} %>
				</tr>
			</thead>
			<tbody>
			<%
			if("employee".equals(bookType))
			{
				for(int g=0;g<employeeList.size();g++)
				{
					LfEmployeeVo employeeInfo=employeeList.get(g);
					
					String employeeid=encryptmap.get(employeeInfo.getEmployeeId()+"");
					
			%>
				<tr>
					<td>
						<input type="checkbox" name="delBook" id="<%=employeeInfo.getIsOperator() %>" value="<%=employeeid %>"/>
					</td> 
					 <td><%=employeeInfo.getEmployeeNo()==null?"-":employeeInfo.getEmployeeNo()%></td>
					 <td><%=employeeInfo.getName()==null?"-":employeeInfo.getName().replace("<","&lt;").replace(">","&gt;")  %></td>
					 <td><%=employeeInfo.getUserName()!=null && !"".equals(employeeInfo.getUserName())?employeeInfo.getUserName():"-"%></td>
					 <td>
					 	 <%
					 		if(!"".equals(employeeInfo.getBirthday()) && employeeInfo.getBirthday() != null){
					 			out.print(df.format(employeeInfo.getBirthday()));
					 		}else{
					 			out.print("-");
					 		}
						 %>
					 
					 </td>
					 <td><%
					 		if(employeeInfo.getSex() == 0){
					 			out.print(nv);
					 		}else if(employeeInfo.getSex() == 1){
					 			out.print(nan);
					 		}else if(employeeInfo.getSex() == 1){
					 			out.print(wz);
					 		}else{
					 			out.print("-");
					 		}
					 %>
					 </td>
					 <td><%=employeeInfo.getDepName() %></td>
					 <td>
						 <%
					 		if(!"".equals(employeeInfo.getDutyName()) && employeeInfo.getDutyName() != null){
					 			out.print(employeeInfo.getDutyName().replaceAll("<","&lt;").replaceAll(">","&gt;"));
					 		}else{
					 			out.print("-");
					 		}
						 
						 %>
					 </td>
					
					<%
					String empPhoneStr=employeeInfo.getMobile();
					if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) {
					%>
					<td><%=(empPhoneStr==null || "".equals(empPhoneStr))?"-":empPhoneStr %></td>
					<%}else{ %>
					<td><%=(empPhoneStr==null || "".equals(empPhoneStr))?"-": commonVariables.replacePhoneNumber(empPhoneStr)%></td>
					<%} %>
					<%if(btnMap.get(menuCode+"-3")!=null) {%>
					<td><a onclick="toEditEmployee('<%=employeeid %>')"><emp:message key="employee_dxzs_title_84" defVal="修改" fileName="employee"/></a></td>
					<%} %>
				</tr>
			<%
				}
				if(employeeList.size()==0){%>
					<tr><td colspan="10"><emp:message key="employee_dxzs_title_59" defVal="无记录" fileName="employee"/></td></tr>
			<%
				}
			}
			%>

			</tbody>
			<tfoot>
				<tr>
					<td colspan="10">
						<div id="pageInfo"></div>
					</td>
				</tr>
			</tfoot>
		</table>
		
