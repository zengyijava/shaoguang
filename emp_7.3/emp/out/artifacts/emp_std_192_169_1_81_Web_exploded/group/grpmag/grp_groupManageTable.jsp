<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.vo.GroupInfoVo"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,
			iPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<GroupInfoVo> groupInfoVoList = (List<GroupInfoVo>)request.getAttribute("groupInfoVoList");
    @ SuppressWarnings("unchecked")
    LinkedHashMap<String,String> groupMap = (LinkedHashMap<String,String>)session.getAttribute("groupMap");
    
    @SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
	</head>
	<body>
 		<input type="hidden" name="path" id="path" value="<%=path %>"/>
 		<input type="hidden" name="inheritPath" id="inheritPath" value="<%=inheritPath %>"/>
 		<table id="content">
			<thead>
				<tr>
					<th>
						<input type="checkbox" name="checkall" id="checkall">
					</th>
					<th> 
						<emp:message key="group_ydbg_ygtxlgl_text_name" defVal="姓名" fileName="group"></emp:message>
					</th>
					<th> 
						<emp:message key="group_ydbg_xzqz_text_addbooktype" defVal="通讯录类型" fileName="group"></emp:message> 
					</th>
					<th> 
						<emp:message key="group_ydbg_xzqz_text_phone" defVal="手机" fileName="group"></emp:message>
					</th>
					<th> 
						<emp:message key="group_ydbg_xzqz_text_affiliatedgroup" defVal=">所属群组" fileName="group"></emp:message>
					</th>
	 
					<th colspan="2">
						<emp:message key="group_common_text_opt" defVal="操作" fileName="group"></emp:message>
					</th>
				</tr>
			</thead>
			<tbody>
				<%
					if(null != groupInfoVoList && 0 != groupInfoVoList.size())
					{
						String l2gTypeName = "";
						String udgName = "";
						String udgId = null;
						for(GroupInfoVo groupInfoVo:groupInfoVoList)
						{
						
							switch(groupInfoVo.getL2gType())
							{
								case 0:
									l2gTypeName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_employee",request);  //"员工";
									break;
								case 1:
									l2gTypeName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_client",request);  //"客户";
									break;
								case 2:
								    l2gTypeName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_customize",request);  //"自定义";
								    break;
								    default:break;
								
							}
							if(null != groupMap && !groupMap.entrySet().isEmpty())
							{
							
							    udgId = null == groupInfoVo.getUdgId()?"":groupInfoVo.getUdgId().toString();
								udgName = groupMap.get(udgId);
							}
				 %>
				 
			<tr>
				<td>
					<input type="checkbox" name="checklist" value="<%=groupInfoVo.getL2gId() %>"/>
 				</td>
				<td class="textalign"><%=null == groupInfoVo.getName()?" ": groupInfoVo.getName().replace("<","&lt;").replace(">","&gt;") %></td>
				<td  class="ztalign"><%=l2gTypeName%></td>
				<%
					String custPhoneStr=groupInfoVo.getMobile();
					if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) {
					%>
					<td><%=custPhoneStr==null?"-":custPhoneStr %></td>
					<%}else{ %>
					<td><%=custPhoneStr==null?"-":custPhoneStr.substring(0,3)+"*****"+custPhoneStr.substring(custPhoneStr.length()-3,custPhoneStr.length()) %></td>
				<%} %>
				<td  class="textalign"><%=udgName.replace("<","&lt;").replace(">","&gt;")%></td>
				<td><a href="javascript:doEdit('<%=groupInfoVo.getL2gId() %>','<%=groupInfoVo.getUdgName() %>','<%=groupInfoVo.getGuId() %>','<%=groupInfoVo.getMobile()%>');"><emp:message key="group_common_opt_modify" defVal="修改" fileName="group"></emp:message></a></td>
				<td><a href="javascript:del('<%=groupInfoVo.getL2gId() %>');"><emp:message key="group_common_opt_delete" defVal="删除" fileName="group"></emp:message></a></td>
			</tr>
			
			<%
				}
			}
			
				else if(groupInfoVoList.size()==0){%>
					<tr><td colspan="7"><emp:message key="group_common_text_norecord" defVal="无记录" fileName="group"></emp:message></td></tr>
			 <%
			 }
			 %>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="7">
						<div id="pageInfo"></div>
					</td>
				</tr>
			</tfoot>
		</table>
		<script src="<%=inheritPath %>/scripts/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script>
			$(document).ready(function(){
				 
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				
				limit();
 					
					//$('#').load(path+'/a_groupManage.htm?method=getSepInfoByType',{time:time});
				$('#modelTable1 tbody tr').hover(function() {
					$(this).css('background-color', '#c1ebff');
				}, function() {
					$(this).css('background-color', '#FFFFFF');
				});
				$('#modelTable2 tbody tr').hover(function() {
					$(this).css('background-color', '#c1ebff');
				}, function() {
					$(this).css('background-color', '#FFFFFF');
				});
				$('#modelTable3 tbody tr').hover(function() {
					$(this).css('background-color', '#c1ebff');
				}, function() {
					$(this).css('background-color', '#FFFFFF');
				});
			    
				$("input[name='checkall']").each(function(index){
					$(this).click(
						function(){
							$("input[name='checklist']").attr("checked",$(this).attr("checked")); 
						}
					);
				});
				$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
					}, function() {
					$(this).removeClass("hoverColor");
				});
				
				
					var isIE = false;
					var isFF = false;
					var isSa = false;
					if ((navigator.userAgent.indexOf("MSIE") > 0)
							&& (parseInt(navigator.appVersion) >= 4))
						isIE = true;
					if (navigator.userAgent.indexOf("Firefox") > 0)
						isFF = true;
					if (navigator.userAgent.indexOf("Safari") > 0)
						isSa = true;
					$("input.udgName,input.udgName2").keypress(function(e) {
						var iKeyCode = window.event ? e.keyCode
								: e.which;
						if (iKeyCode == 60 || iKeyCode == 62) {
							if (isIE) {
								event.returnValue = false;
							} else {
								e.preventDefault();
							}
						}
					}
					);
						$("input.udgName,input.udgName2").blur(function(e) {
							$(this).val($(this).val().replaceAll("<","").replaceAll(">",""));
						}
						);

				});
		</script>
 	</body>
</html>