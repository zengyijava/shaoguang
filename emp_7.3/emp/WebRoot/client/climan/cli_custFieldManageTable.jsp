<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.client.vo.CustFieldValueVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
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
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	
	
	@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("custFieldManger");

String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/cli_custFieldManageTable.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="cli_custFieldManageTable">
 		<input type="hidden" name="path" id="path" value="<%=path %>"/>
 		<input type="hidden" name="inheritPath" id="inheritPath" value="<%=inheritPath %>"/>
 		<input type="hidden" name="addrType" id="addrType"/>
 		<table id="content">
			<thead>
				<tr>
					<th>
						<input type="checkbox" name="checkall" id="checkall">
					</th>
					
					<th> 
						<emp:message key="client_khtxlgl_khssgl_text_attrval" defVal="属性值" fileName="client"></emp:message>
					</th>
				
					<th colspan="2">
						<emp:message key="client_common_text_opt" defVal="操作" fileName="client"></emp:message>
					</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach  items="${rsList}" var="rsList" varStatus="status" > 
				<tr>
					<td>
						<input type="checkbox" name="checklist" value="${rsList.id }"/>
	 				</td>
					<td class="textalign" ><xmp>${rsList.field_Value }</xmp></td>
					<td>
					<% if(btnMap.get(menuCode+"-2")!=null) {  %>
					<a href="javascript:doEdit(${rsList.id },${rsList.field_ID },'${rsList.field_Value }');"><emp:message key="client_common_opt_modify" defVal="修改" fileName="client"></emp:message></a>
					<%} %>
					</td>
					<td>
						<% if(btnMap.get(menuCode+"-5")!=null) {  %>
					<a href="javascript:del('${rsList.id }');"><emp:message key="client_common_opt_delete" defVal="删除" fileName="client"></emp:message></a>
					<%} %>
					</td>
				</tr>
			</c:forEach>
			
			 <% List<CustFieldValueVo> cfv =(List<CustFieldValueVo>)request.getAttribute("rsList"); %>
			 <%if(cfv.size()==0){%>
					<tr><td colspan="4"><emp:message key="client_common_text_norecord" defVal="无记录" fileName="client"></emp:message></td></tr>
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
 					<div id="group_add" style="display:none;"  title="<emp:message key="client_khtxlgl_khssgl_text_addattr" defVal="添加属性" fileName="client"></emp:message>">
				<center>
 					<table width="70%" height="100%">
							<tr height="40">
							<td  align="left">
							<emp:message key="client_khtxlgl_khssgl_text_attrname" defVal="属性名称" fileName="client"></emp:message>：
							</td>
							<td>
							<input type="text" class="input_bd" name="udgName" id="udgName" maxlength="8"/>
							</td>
							</tr>
							<tr height="40">
						    <td>
						          <emp:message key="client_khtxlgl_khssgl_text_attrtype" defVal="属性类型" fileName="client"></emp:message>：
						    </td>
							<td align="left" class="client_td1">
							<input type="radio" name="oneradio" value="0" checked="checked" id="radio1"><label for='radio1'><emp:message key="client_khtxlgl_khssgl_text_singleoption" defVal="单选项" fileName="client"></emp:message></label>   
							<input type="radio" name="oneradio" value="1" id="radio2"><label for='radio1'><emp:message key="client_khtxlgl_khssgl_text_multipleoptions" defVal="多选项" fileName="client"></emp:message></label>   
							</td>
 							</tr>
						<tr><td colspan="2" class="client_td2">
						<input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass5 mr23" id="group_add_ok" onclick="cFieldAdd()"/>
						<input type="button" value="<emp:message key="client_common_opt_cancel" defVal="取消" fileName="client"></emp:message>" class="btnClass6" id="group_add_cancel"  onclick="group_add_cancel()"/>
						<br/>
						</td></tr>
					</table>
					</center>
 					</div>
 					
 					
 					
 					<div id="group_update2"   title="<emp:message key="client_khtxlgl_khssgl_text_modifyattr" defVal="修改属性" fileName="client"></emp:message>">
				
 					<table width="100%" height="100%">
 							 
							<tr>
								<td >
									<emp:message key="client_khtxlgl_khssgl_text_attrname" defVal="属性名称" fileName="client"></emp:message>：
								</td>
								<td >
									 <input type="text" class="input_bd" name="fName" id="fName" maxlength="8"/>
								</td>
 							</tr>
 							<tr>
 							 <td>
							    <emp:message key="client_khtxlgl_khssgl_text_attrtype" defVal="属性类型" fileName="client"></emp:message>：
							    </td>
							 
								<td>
								  <input type="radio" name="vradio" value="0"  id="ljradio1"><label for='radio1'><emp:message key="client_khtxlgl_khssgl_text_singleoption" defVal="单选项" fileName="client"></emp:message></label>   
								   <input type="radio" name="vradio" value="1" id="ljradio2"><label for='radio1'><emp:message key="client_khtxlgl_khssgl_text_multipleoptions" defVal="多选项" fileName="client"></emp:message></label>   
								</td>
 							</tr>
							<tr><td colspan="2" class="client_td3">
							<div>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass5 mr23" id="group_update2_ok" onclick="editGroupProperty()"/>
							<input type="button" value="<emp:message key="client_common_opt_cancel" defVal="取消" fileName="client"></emp:message>" class="btnClass6" id="group_update2_cancel" onclick="closeEidtGroupBox()"/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
							</td></tr>
							</table>
 					</div>
 					
 					<div id="ct_values_add"   title="<emp:message key="client_khtxlgl_khssgl_opt_addattributes" defVal="添加属性值" fileName="client"></emp:message>">
				 	<center>
	              		<table>
							<tr height="40">
							<td><emp:message key="client_khtxlgl_khssgl_text_attrname" defVal="属性名称" fileName="client"></emp:message>：</td>
							<td align=left>
							<label id="fieldName"></label>
							<input type="hidden" name="fieldID" id="fieldID"/>
							</td>
							</tr>
	              			<tr height="40">
							<td align=left>
								<emp:message key="client_khtxlgl_khssgl_text_attrval" defVal="属性值" fileName="client"></emp:message>：
							</td>
							<td>
								 <input type="text" class="input_bd" id="newFieldValue" name="newFieldValue" maxlength="20"/>
							</td>
 							</tr>
 							<tr><td class="client_td2" colspan="2">
 							<input type="button" class="btnClass5 mr23" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" onclick="saveFieldValue();"/>
 							<input class="btnClass6" value="<emp:message key="client_common_opt_cancel" defVal="取消" fileName="client"></emp:message>" type="button" onclick='javascript:$("#newFieldValue").val("");$("#ct_values_add").dialog("close");'/>
 							<br/>
 							</td></tr>
				</table>
				</center>
				</div>
				
				<div id="ct_values_update" title="<emp:message key="client_khtxlgl_khssgl_opt_modifyattrval" defVal="修改属性值" fileName="client"></emp:message>">
				
 					<table width="100%">
						<tr>
						<td >
							<emp:message key="client_khtxlgl_khssgl_text_attrval" defVal="属性值" fileName="client"></emp:message>：
						</td>
						<td >
							<input class="input_bd" type="text" id="fieldValue" name="fieldValue" maxlength="20"/>
						</td>
						</tr>
						<tr><td class="client_td_4" colspan="2">
						<input type="button" id="updateOK" class="btnClass5 mr23" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" onclick="javascript:editCustFieldValue(doEditIdTemp,doEditFieldIDTemp,$('#fieldValue').val(),doEditFieldValueTemp)"/>
						<input id="updateCancel"  class="btnClass6" value="<emp:message key="client_common_opt_cancel" defVal="取消" fileName="client"></emp:message>" type="button" onclick="javascript:$('#ct_values_update').dialog('close')"/>
						<br/>
						</td></tr>
					</table>
				</div>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
 		<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
		<script>
			$(document).ready(function(){
				noquot("#udgName");
				noquot("#fName");
				noquot("#fieldValue");
				noquot("#newFieldValue");
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
			});
			function limit(){
				//只允许输入数字和';'
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
				$('#receiver').keypress(function(e) {
					var iKeyCode = window.event ? e.keyCode
							: e.which;
					var vv = !(((iKeyCode >= 48) && (iKeyCode <= 57))
							|| (iKeyCode == 59)
							|| (iKeyCode == 13)
							|| (iKeyCode == 46)
							|| (iKeyCode == 45)
							|| (iKeyCode == 37)
							|| (iKeyCode == 39) || (iKeyCode == 8));
					if (vv) {
						if (isIE) {
							event.returnValue = false;
						} else {
							e.preventDefault();
						}
					}
				});
				//控制不能由输入法输入其他字符
				$('#receiver').keyup(function() {
					var str = $('#receiver').val();
					//只能输入0-9或者英文标点","符号、回车换行
					var reg = /[^0-9,\r\n]+/g;
					str = str.replace(reg, "");
					$('#receiver').val(str);
				});
			}
		</script>
 	</body>
</html>