<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <base href="<%=basePath%>">
    
    <title><emp:message key="common_operPageField_1" defVal="页面控件维护" fileName="common"></emp:message></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="<%=path %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=path %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=path %>/frame/frame2.5/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=path %>/frame/frame2.5/skin/default/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=path %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
  	<%}%>
	<script>
	 var total=<%=pageInfo.getTotalPage()%>;
	 var pageIndex=<%=pageInfo.getPageIndex()%>;
	 var pageSize=<%=pageInfo.getPageSize()%>;
	 var totalRec=<%=pageInfo.getTotalRec()%>;
	</script>
	<style type="text/css">
		
		#setPageField table tr td
		{
			height: 30px;
		}
		.left
		{
			text-align: right;
		}
	</style>
  </head>
  
  
  <body>
  	<div id="container" class="container">
		<%-- header开始 --%>
			<div class="top">
				<div id="top_right">
					<div id="top_left"></div>
					<div id="top_main">
<%--						当前位置是：--%>
					</div>
				</div>
			</div>
			<%-- header结束 --%>
		<div id="rContent" class="rContent">	
	  		<br/>
	  		<br/>
	  		
	  		<form id="addForm">
						<div align="center">
							<input type="button" value="新建" onclick="javascript:doAdd(0)" class="btnClass5"/>
						</div>
						
			</form>			
			<br>
			<br/>
			<div id="condition">
			</div>
		   	<form id="pageForm" action="glo_operPageFieldSvt.hts?method=find" method="post">
				<center>
				<table id="content" align="center" style="width: 90%">
					<thead>
						<tr>
			   	 			<th>
								<emp:message key="common_operPageField_2" defVal="模块编号" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_4" defVal="界面编号" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_6" defVal="控件编号" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_8" defVal="控件名称" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_10" defVal="控件词汇" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_12" defVal="控件类型" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_14" defVal="控件子项值" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_16" defVal="控件子项名称" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_18" defVal="控件是否显示" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_19" defVal="是否有子项" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_21" defVal="控件默认值" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_24" defVal="子项排序值" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th>
								<emp:message key="common_operPageField_26" defVal="是否子项" fileName="common"></emp:message>
			   	 			</th>
			   	 			<th colspan="2">
								<emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
			   	 			</th>
				   	 	</tr>
					</thead>
					<tbody>
						<%
						List<LfPageField> pageFieldList = (List<LfPageField>)request.getAttribute("pageFieldList");
						if(pageFieldList!=null && pageFieldList.size()>0)
						{
							for(LfPageField pf : pageFieldList)
							{								
						%>
								<tr>
									<td>
										<%=pf.getModleId()!=null?pf.getModleId():"-"%>
									</td>
									<td>
										<%=pf.getPageId()!=null?pf.getPageId():"-"%>
									</td>
									<td>
										<%=pf.getFieldId()!=null?pf.getFieldId():"-"%>
									</td>
									<td>
										<%=pf.getFieldName()!=null?pf.getFieldName():"-"%>
									</td>
									<td>
										<%=pf.getField()!=null?pf.getField():"-"%>
									</td>
									<td>
										<%=pf.getFieldType()!=null?pf.getFieldType():"-"%>
									</td>
									<td>
										<%=pf.getSubFieldValue()!=null?pf.getSubFieldValue():"-"%>
									</td>
									<td>
										<%=pf.getSubFieldName()!=null?pf.getSubFieldName():"-"%>
									</td>
									<td>
										<%=pf.getFiledShow()!=null?pf.getFiledShow():"-"%>
									</td>
									<td>
										<%=pf.getSubField()!=null?pf.getSubField():"-"%>
									</td>
									<td>
										<%=pf.getDefaultValue()!=null?pf.getDefaultValue():"-"%>
									</td>
									<td>
										<%=pf.getSortValue()!=null?pf.getSortValue():"-"%>
									</td>
									<td>
										<%=pf.getIsField()!=null?pf.getIsField():"-"%>
									</td>
									<td>
										<a onclick="javascript:doEdit('1','<%=pf.getModleId()%>','<%=pf.getPageId()%>','<%=pf.getFieldId()%>','<%=pf.getFieldName()%>','<%=pf.getField()%>','<%=pf.getFieldType()%>','<%=pf.getSubFieldValue()%>','<%=pf.getSubFieldName()%>','<%=pf.getFiledShow()%>','<%=pf.getSubField()%>','<%=pf.getDefaultValue()%>','<%=pf.getSortValue()%>','<%=pf.getIsField()%>')" ><emp:message key="common_modify" defVal="修改" fileName="common"></emp:message></a>
									</td>
									<td>
										<a onclick="doDel('<%=pf.getModleId()%>','<%=pf.getPageId()%>','<%=pf.getFieldId()%>','<%=pf.getFieldName()%>','<%=pf.getField()%>','<%=pf.getFieldType()%>','<%=pf.getSubFieldValue()%>','<%=pf.getSubFieldName()%>','<%=pf.getFiledShow()%>','<%=pf.getSubField()%>','<%=pf.getDefaultValue()%>','<%=pf.getSortValue()%>','<%=pf.getIsField()%>')"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a>
									</td>
								</tr>
						<%
							}
							
						}else
						{
						%>
							<tr><td align="center" colspan="15"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
						<%} %>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="15">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
				</center>
			</form>	
			
	
				<div id="setPageField" title="<emp:message key="common_operPageField_28" defVal="设置页面控件" fileName="common"></emp:message>" style="padding:5px;display:none;font-size: 12px;">
				<center>
				<br/>
		        <table align="center">
						<tr>
							<td class="left" style="font-size: 14px;width: 100px;">
								<emp:message key="common_operPageField_3" defVal="模块编号：" fileName="common"></emp:message>
							</td>
							<td style="width: 180px;">
								<input id="modleid" name="modleid" type="text"  maxlength="8" style="width: 150px;height:16px;font-size: 14px;"/> 
							</td>
							<td  class="left"  style="font-size: 14px;width: 100px;">
								<emp:message key="common_operPageField_5" defVal="界面编号：" fileName="common"></emp:message>
							</td>
							<td style="width: 180px;">
								<input id="pageid" name="pageid" type="text" maxlength="12" style="width: 150px;height: 16px;font-size: 14px;"/> 						
							</td>
						</tr>
						<tr>
							<td class="left"  style="font-size: 14px;width: 100px;">
								<emp:message key="common_operPageField_7" defVal="控件编号：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="fieldid" name="fieldid" type="text"  maxlength="8" style="width: 150px;height:16px;font-size: 14px;"/> 
							</td>
							<td  class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_9" defVal="控件名称：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="fieldname" name="fieldname" type="text" maxlength="12" style="width: 150px;height: 16px;font-size: 14px;"/> 
							</td>
						</tr>
						<tr>
							<td  class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_11" defVal="控件词汇：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="field" name="field" type="text"  maxlength="8" style="width: 150px;height:16px;font-size: 14px;"/> 
							</td>
							<td  class="left" style="font-size: 14px;">
								<emp:message key="common_operPageField_13" defVal="控件类型：" fileName="common"></emp:message>
							</td>
							<td>
								<select id="fieldtype" name="fieldtype" style="width: 154px;height: 22px;font-size: 14px;">
									<option value="1"><emp:message key="common_operPageField_29" defVal="下拉列表" fileName="common"></emp:message></option>
									<option value="2"><emp:message key="common_operPageField_30" defVal="单选控件" fileName="common"></emp:message></option>
									<option value="3"><emp:message key="common_operPageField_31" defVal="多选控件" fileName="common"></emp:message></option>
									<option value="4"><emp:message key="common_operPageField_32" defVal="文本控件" fileName="common"></emp:message></option>
									<option value="5"><emp:message key="common_operPageField_33" defVal="按钮控件" fileName="common"></emp:message></option>
									<option value="6"><emp:message key="common_hyperlink" defVal="超链接" fileName="common"></emp:message></option>
								</select>
							</td>
						</tr>
						<tr>						
							<td class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_35" defVal="控件是否显示：" fileName="common"></emp:message>
							</td>
							<td>
								<select id="filedshow" name="filedshow" style="width: 154px;height: 22px;font-size: 14px;">
									<option value="0"><emp:message key="common_yes" defVal="是" fileName="common"></emp:message></option>
									<option value="1"><emp:message key="common_no" defVal="否" fileName="common"></emp:message></option>
								</select>
							</td>
							<td class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_20" defVal="是否有子项：" fileName="common"></emp:message>
							</td>
							<td>
								<select id="subfield" name="subfield" style="width: 154px;height: 22px;font-size: 14px;">
									<option value="0"><emp:message key="common_yes" defVal="是" fileName="common"></emp:message></option>
									<option value="1"><emp:message key="common_no" defVal="否" fileName="common"></emp:message></option>
								</select>
							</td>
						</tr>
						
						<tr>
							<td class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_23" defVal="控件默认值：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="defaultvalue" name="defaultvalue" type="text"  maxlength="8" style="width: 150px;height:16px;font-size: 14px;"/> 
							</td>
							<td class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_27" defVal="是否子项：" fileName="common"></emp:message>
							</td>
							<td>
								<select id="isfield" name="isfield" onchange="changefieldstate()" style="width: 154px;height: 22px;font-size: 14px;">
									<option value="0"><emp:message key="common_yes" defVal="是" fileName="common"></emp:message></option>
									<option value="1"><emp:message key="common_no" defVal="否" fileName="common"></emp:message></option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_15" defVal="控件子项值：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="subfieldvalue" name="subfieldvalue" type="text"  maxlength="8" style="width: 150px;height:16px;font-size: 14px;"/> 
							</td>
							<td class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_17" defVal="控件子项名称：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="subfieldname" name="subfieldname" type="text" maxlength="12" style="width: 150px;height: 16px;font-size: 14px;"/> 		
							</td>
						</tr>
						<tr>
							<td class="left"  style="font-size: 14px;">
								<emp:message key="common_operPageField_25" defVal="子项排序值：" fileName="common"></emp:message>
							</td>
							<td>
								<input id="sortvalue" name="sortvalue" type="text" maxlength="12" style="width: 150px;height: 16px;font-size: 14px;"/> 
							</td>
					</tr>
					<tr>
						<td colspan="4">
							&nbsp;
						</td>
					</tr>
					 <tr>
					 	<td align="center" colspan="4">
					 		<input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass5 mr23" onclick="javascript:setPageField()" />
					 		<input type="reset" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>取消"  class="btnClass6" onclick="javascript:cancel()" />
					 	</td>
					 </tr>	
					</table>
				</center>
				</div>
			</div>
		</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=path%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=path %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="common/commonJs/glo_operPageField.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		});
	</script>
  </body>
</html>














