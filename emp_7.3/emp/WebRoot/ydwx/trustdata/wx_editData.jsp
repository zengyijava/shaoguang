<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.netnews.entity.LfWXData"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXDataType"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
   String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map	
	@ SuppressWarnings("unchecked")
	List<LfWXDataType> dataTypes=(List<LfWXDataType>)request.getAttribute("dataTypes");//互动项类别
	LfWXData wxData=(LfWXData)request.getAttribute("wxData");
	
	String dataChos=(String)request.getAttribute("dataChos");
	Integer colNum = (Integer)request.getAttribute("colNum");
	String optype=(String)request.getAttribute("optype");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute("emp_lang");
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=path%>/ydwx/trustdata/css/addInteraction.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=iPath%>/css/trustData.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" href="<%=skin%>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			initCols();
		});
		function initCols(){
			var quesType = "<%=wxData.getQuesType()%>";
			var dataChos = "<%=dataChos %>";
			if(quesType != 1 && dataChos != null && dataChos.length > 0){
				$("#judgementNum").val("<%=colNum%>");
				$("#colNum").val("<%=colNum%>");
				 $("#addCol").css("display","");
				 var cols = $("#initCols");
				 cols.html(dataChos);
			}
		}
		function resetForm(){
			location.href = 'wx_trustdata.htm?method=toEdit&optype=<%=optype%>&dId=<%=wxData.getDId() %>&lgcorpcode=<%=wxData.getCorpCode()%>';
		}
		$(function(){
		  $('#dataTypeId,#quesType').isSearchSelect({'width':'302','isInput':false,'zindex':0});
	    });
		</script>
</head>
<%-- header结束 --%>
<body id="ydwx_editData">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("ydwx","ydwx_jsp_out_xghdx",request)) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			<div class="titletop">
				<table class="titletop_table">
					<tr>
						<td class="titletop_td">
						<%  if("copy".equalsIgnoreCase(optype)){
							   // out.write("新增互动项");
							   	out.write(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_xzhdx",request));
							}else{
								//out.write("修改互动项");
								out.write(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_xghdx",request));
							} 
						%>
						</td>
						<td align="right">
						</td>
					</tr>
				</table>
			</div>
			<style>
			#sysTable td{padding:3px 6px;}
			</style>
			<input type="hidden" name="pathUrl" id="pathUrl" value="<%=path %>">
			<form action="<%=path%>/wx_trustdata.htm?method=update" method="post" id="trustForm">
				<div id="loginUser" class="hidden"></div>
				<input id="colNum" name="colNum" type="hidden" value="2"/>
				<%-- 为了限制增加的个数 --%>
				<input id="path" name="path" type="hidden" value="<%=path%>"/>
				<input id="judgementNum" name="judgementNum" type="hidden" value="2"/>
				<input id="dId" name="dId" type="hidden" value="<%=wxData.getDId() %>"/>	
				<input id="optype" name="optype" type="hidden" value="<%=optype %>"/>	
				<input id="oldCode" name="oldCode" type="hidden" value="<%=wxData.getCode() %>"/>
				<input name="replySetType" type="hidden" value="1" checked="checked">
				<div id="selfSelect">
				<table border="0" cellspacing="0" id="sysTable" class="ydwx_sysTable">
					<tbody>
						<tr >
							<td align="right">
								<emp:message key="ydwx_wxgl_hdxgl_biaohaos" defVal="编&nbsp;&nbsp;号：" fileName="ydwx"></emp:message>
							</td>
							<td align="left" id="left">
								<input id="code" name="code" class="input_bd ydwx_code"
									type="text" maxlength="20" value="<%=wxData.getCode() %>"
									onkeyup= "numberControl($(this))"/>
							<font class="ydwx_asterisk">&nbsp;&nbsp;*</font>
							</td>
						</tr>
						<tr >
							<td align="right">
								<emp:message key="ydwx_wxgl_hdxgl_mingchens" defVal="名&nbsp;&nbsp;称：" fileName="ydwx"></emp:message>
							</td>
							<td align="left" id="left">
								<input id="name" name="name" class="input_bd ydwx_name"
									type="text" maxlength="20" value="<%=wxData.getName() %>"/>
									<font  class="ydwx_asterisk">&nbsp;&nbsp;*</font>
							</td>
						</tr>
						<tr >
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_leibeis" defVal="类&nbsp;&nbsp;别：" fileName="ydwx"></emp:message></td>
							<td align="left">
							
								<select name="dataTypeId" id="dataTypeId" class="ydwx_dataTypeId">
								<%for(LfWXDataType dataType:dataTypes){%>
								<option value="<%=dataType.getId() %>" 
								<%if(wxData.getDataTypeId().intValue() == dataType.getId().intValue()){%>
								selected="selected"
								<%} %>><xmp><%=dataType.getName() %></xmp></option>
								<% } %>
								</select>
								<a class="ydwx_manageType_a" onclick="javascript:manageType()" ><emp:message key="ydwx_wxgl_hdxgl_gllb" defVal="管理类别" fileName="ydwx"></emp:message></a>
							</td>
						</tr>
						<%--
						<tr >
							<td align="right">回复设置：</td>
							<td align="left" >
							<input name="replySetType" type="radio" value="1" <%if(wxData.getReplySetType() == 1){%>
								checked="checked"<%} %>>
								多次回复有效
								<input type="radio" value="2"  name="replySetType" <%if(wxData.getReplySetType() == 2){%>
								checked="checked"<%} %> style="margin-left:10px;" >
								首次回复有效
								<input type="radio" value="3" name="replySetType" <%if(wxData.getReplySetType() == 3){%>
								checked="checked"<%} %> style="margin-left:10px;">
								末次回复有效
							</td>
						</tr>
						--%>
						<tr >
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_leixing" defVal="类&nbsp;&nbsp;型：" fileName="ydwx"></emp:message></td>
							<td >
								<select name="quesType" id="quesType" class="ydwx_quesType" onchange="changeQuesType();">
								<option value="1" <%if(wxData.getQuesType() == 1){%>
								selected="selected"<%} %>><emp:message key="ydwx_wxgl_hdxgl_huida" defVal="回答类" fileName="ydwx"></emp:message></option>
								<option value="2" <%if(wxData.getQuesType() == 2){%>
								selected="selected"<%} %>><emp:message key="ydwx_wxgl_hdxgl_danxuan" defVal="单选类" fileName="ydwx"></emp:message></option>
								<option value="3" <%if(wxData.getQuesType() == 3){%>
								selected="selected"<%} %>><emp:message key="ydwx_wxgl_hdxgl_duoxuan" defVal="多选类" fileName="ydwx"></emp:message></option>
								<option value="4" <%if(wxData.getQuesType() == 4){%>
								selected="selected"<%} %>><emp:message key="ydwx_wxgl_hdxgl_xialakuang" defVal="下拉框类" fileName="ydwx"></emp:message></option>
								</select>
							</td>
						</tr>
						<tr >
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_neirongss" defVal="内&nbsp;&nbsp;容：" fileName="ydwx"></emp:message></td>
							<td align="left" id="left">
								<textarea rows="8" cols="10" class="ydwx_quesContent"  name="quesContent" id="quesContent" onblur="eblur($(this))"
								><%=wxData.getQuesContent() %></textarea>
								<font  class="ydwx_asterisk">&nbsp;&nbsp;*</font><br />
							<font class="ydwx_quesstrlen_font"><b id="quesstrlen"> <%=wxData.getQuesContent().trim().length()%></b><b>/512</b></font> 
							</td>
						</tr>
						<tr class="ydwx_addCol" id="addCol"><td></td><td><a onclick="javascript:addCols()" class="ydwx_addCol_a"><emp:message key="ydwx_wxgl_hdxgl_tjsx" defVal="添加选项" fileName="ydwx"></emp:message></a></td></tr>
						<tr id="initCols_show"><td></td><td><table id="initCols">
						</table></td></tr>
					</tbody>
				</table>
				</div>
			<div class="ydwx_btns">
			<input type="button" id="save" value="<emp:message key='ydwx_common_btn_baocun' defVal='保  存' fileName='ydwx'></emp:message>" class="btnClass5 mr10"  onclick="javascript:saveTrustData();"/>
			<input type="button" class="btnClass6 mr10"  value="<emp:message key='ydwx_common_btn_chongzhi' defVal='重  置' fileName='ydwx'></emp:message>" onclick="resetForm();">
			<input type="button" class="btnClass6" value="<emp:message key='ydwx_common_btn_fanhui' defVal='返  回' fileName='ydwx'></emp:message>" onclick="javascript:doreturn();" ><br/>
			</div>
			</form>
		</div>
		<div id="addTypeDiv" title="<emp:message key="ydwx_wxgl_hdxgl_hdlbgl" defVal="互动类别管理" fileName="ydwx"></emp:message>" class="ydwx_addTypeDiv">
				<iframe id="tempFrame" name="tempFrame" class="ydwx_tempFrame" marginwidth="0" scrolling="no" frameborder="no" src ="<%=iPath%>/blank.jsp " ></iframe>
			
			</div>
			<%-- 内容结束 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
<%-- 加载JS文件 --%>
<script type="text/javascript" src="<%=iPath%>/js/wxData.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
</body>
</html>