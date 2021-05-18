<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXDataType"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
   String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map	
	@ SuppressWarnings("unchecked")
	List<LfWXDataType> dataTypes=(List<LfWXDataType>)request.getAttribute("dataTypes");//按钮权限Map	
	
	String code = (String)request.getAttribute("code");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("sysuser"); 
	String menuOther= titleMap.get("trustdata"); 
	
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
		<style type="text/css">
			 body {overflow-x :hidden; overflow-y:auto;}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=path%>/ydwx/trustdata/css/addInteraction.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script language="javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
</head>
<%-- header结束 --%>
<body style="margin-left: -20px;">
		<div id="container" class="container" >
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			<input type="hidden" name="pathUrl" id="pathUrl" value="<%=path %>">
			<form action="<%=path%>/wx_trustdata.htm?method=add" method="post" id="trustForm">
				<div id="loginUser" class="hidden"></div>
				<input id="path" name="path" type="hidden" value="<%=path%>"/>
				<input id="colNum" name="colNum" type="hidden" value="2"/>
								<%-- 为了限制增加的个数 --%>
				<input id="judgementNum" name="judgementNum" type="hidden" value="2"/>
				<input id="optype" name="optype" type="hidden" value="add"/>
				<input id="refpage" name="refpage" type="hidden" value="addhdx"/>
				<input id="lguserid" name="lguserid" type="hidden" value="<%=request.getParameter("lguserid") %>"/>
				<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=request.getParameter("lgcorpcode") %>"/>

				<table border="0" cellspacing="0" id="sysTable" >
					<tbody>
						<tr >
							<td align="right">
								<label><emp:message key="ydwx_wxgl_hdxgl_biaohaos" defVal="编&nbsp;&nbsp;号：" fileName="ydwx"></emp:message></label>
							</td>
							<td align="left" id="left">
								<input id="code" name="code" class="input_bd" style="width:300px;"
									type="text" maxlength="20" value="<%=code %>" 
							onkeyup= "if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')"/>
							<font style="color: red;">&nbsp;&nbsp;*</font>
							</td>
						</tr>
						<tr >
							<td align="right">
								<label><emp:message key="ydwx_wxgl_hdxgl_mingchens" defVal="名&nbsp;&nbsp;称：" fileName="ydwx"></emp:message></label>
							</td>
							<td align="left" id="left">
								<input id="name" name="name" class="input_bd" style="width:300px;"
									type="text" maxlength="20" value=""/>
									<font style="color: red;">&nbsp;&nbsp;*</font>
							</td>
						</tr>
						<tr >
							<td align="right"><label><emp:message key="ydwx_wxgl_hdxgl_leibeis" defVal="类&nbsp;&nbsp;别：" fileName="ydwx"></emp:message></label></td>
							<td align="left">
								<select id="dataTypeId" name="dataTypeId" style="width:220px;">
								<%for(int i=0;i<dataTypes.size();i++){%>
								<option value="<%=dataTypes.get(i).getId() %>" <%if(i==0){ %>selected="selected"<%} %>><xmp><%=dataTypes.get(i).getName() %></xmp></option>
								<% } %>
								</select>
								<%-- 增加了类别管理权限控制    may add --%>
								<%if(btnMap.get(menuOther+"-4")!=null){%>
								<a style="margin-left: 30px;cursor:pointer;" onclick="javascript:manageType()" ><emp:message key="ydwx_wxgl_hdxgl_gllb" defVal="管理类别" fileName="ydwx"></emp:message></a>
								<%} %>
								
							</td>
						</tr>
						<%--  根据设计要求去掉回复设置
						<tr >
							<td align="right"><label >回复设置：</label></td>
							<td align="left" >
							<input name="replySetType" type="radio" value="1" checked="checked">多次回复有效
								<input type="radio" value="2"  name="replySetType" style="margin-left:10px;" >首次回复有效
								<input type="radio" value="3" name="replySetType" style="margin-left:10px;">末次回复有效
							</td>
						</tr>
						--%>
						<tr >
							<td align="right"><label ><emp:message key="ydwx_wxgl_hdxgl_leixing" defVal="类&nbsp;&nbsp;型：" fileName="ydwx"></emp:message></label></td>
							<td >
								<select name="quesType" id="quesType" style="width:220px;" onchange="changeQuesType();">
									<option value="1"><emp:message key="ydwx_wxgl_hdxgl_huida" defVal="回答类" fileName="ydwx"></emp:message></option>
									<option value="2"><emp:message key="ydwx_wxgl_hdxgl_danxuan" defVal="单选类" fileName="ydwx"></emp:message></option>
									<option value="3"><emp:message key="ydwx_wxgl_hdxgl_duoxuan" defVal="多选类" fileName="ydwx"></emp:message></option>
									<option value="4"><emp:message key="ydwx_wxgl_hdxgl_xialakuang" defVal="下拉框类" fileName="ydwx"></emp:message></option>
								</select>
							</td>
						</tr>
						<tr >
							<td align="right"><label style="color: black;"><emp:message key="ydwx_wxgl_hdxgl_neirongss" defVal="内&nbsp;&nbsp;容：" fileName="ydwx"></emp:message></label></td>
							<td align="left" id="left">
								<textarea rows="4" cols="10" style="width:300px;" name="quesContent" id="quesContent"></textarea>
							<font style="color: red;">&nbsp;&nbsp;*</font>
							</td>
						</tr>
						<tr style="display: none;cursor: hand;" id="addCol"><td></td><td><a onclick="javascript:addCols()" style="margin-left: 250px;cursor: pointer;"><emp:message key="ydwx_wxgl_hdxgl_tjsx" defVal="添加选项" fileName="ydwx"></emp:message></a></td></tr>
						<tr ><td></td><td><table id="initCols">
							
						</table></td></tr>
						
						
					</tbody>
				</table>
				<div style="margin-top: 10px;margin-left: 130px;">
				<input type="button" id="save" value="<emp:message key='ydwx_common_btn_baocun' defVal='保  存' fileName='ydwx'></emp:message>" class="btnClass5 mr10"  onclick="javascript:saveTrustData('addhdx');"/>
				<input type="button" id="reset" class="btnClass6 mr10"  value="<emp:message key='ydwx_common_btn_chongzhi' defVal='重  置' fileName='ydwx'></emp:message>"  >
				<input type="button" class="btnClass6 mr10"  value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="window.parent.closeDialog()" >
			</div>
			</form>

		</div>
		<div id="addTypeDiv" title="<emp:message key="ydwx_wxgl_hdxgl_hdlbgl" defVal="互动类别管理" fileName="ydwx"></emp:message>" style="padding:5px;display:none">
				<iframe id="tempFrame" name="tempFrame" style="width:600px;height:440px;border: 0;" marginwidth="0" scrolling="no" frameborder="no" src ="<%=iPath%>/blank.jsp " ></iframe>
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
	<script type="text/javascript" src="<%=iPath%>/js/wxData.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
		// 返回互动项管理页面
		function goback(){	
			
			window.location.href="<%=path%>/wx_trustdata.htm";
		}
	</script>		
</body>
</html>