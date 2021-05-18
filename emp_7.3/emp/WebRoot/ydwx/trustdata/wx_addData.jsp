<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.netnews.entity.LfWXDataType" %>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
    List<LfWXDataType> dataTypes = (List<LfWXDataType>) request.getAttribute("dataTypes");//按钮权限Map

	String code = (String)request.getAttribute("code");

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
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=path%>/ydwx/trustdata/css/addInteraction.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
        <link rel="stylesheet" type="text/css"
              href="<%=commonPath%>/common/css/newSelect.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=iPath%>/css/trustData.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" href="<%=skin%>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
        <script type="text/javascript"
                src="<%=inheritPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
        <script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
	</head>
<%-- header结束 --%>
<body id="ydwx_addData">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("ydwx","ydwx_wxgl_hdxgl_xzhdx",request)) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			<div class="titletop">
				<table class="titletop_table ydwx_titletop_table">
					<tr>
						<td class="titletop_td">
							<emp:message key="ydwx_wxgl_hdxgl_xzhdx" defVal="新增互动项" fileName="ydwx"></emp:message>
						</td>
						<td align="right">
						 <font class="titletop_font ydwx_titletop_font" onclick="goback();"><emp:message key="ydwx_wxgl_hdxgl_fhsyj" defVal="返回上一级" fileName="ydwx"></emp:message></font>
						</td>
					</tr>
				</table>
			</div>
			<style>
			#sysTable td{padding:3px 6px;}
			</style>
			<input type="hidden" name="pathUrl" id="pathUrl" value="<%=path %>">
			<form action="<%=path%>/wx_trustdata.htm?method=add" method="post" id="trustForm">
				<div id="loginUser" class="hidden"></div>
				<input id="path" name="path" type="hidden" value="<%=path%>"/>
				<input id="colNum" name="colNum" type="hidden" value="2"/>
				<%-- 为了限制增加的个数 --%>
				<input id="judgementNum" name="judgementNum" type="hidden" value="2"/>
				<input id="optype" name="optype" type="hidden" value="add"/>
				<input name="replySetType" type="hidden" value="1" checked="checked">
				<div id="selfSelect">
				<table border="0" cellspacing="0" id="sysTable" class="ydwx_sysTable">
					<tbody>
						<tr >
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_biaohaos" defVal="编&nbsp;&nbsp;号：" fileName="ydwx"></emp:message></td>
							<td align="left" id="left">
								<input id="code" name="code" class="input_bd ydwx_code"
                                       type="text" maxlength="20" value="<%=code %>"
                                       onkeyup="numberControl($(this))"/>
							<font class="ydwx_asterisk">&nbsp;&nbsp;*</font>
							</td>
						</tr>
						<tr >
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_mingchens" defVal="名&nbsp;&nbsp;称：" fileName="ydwx"></emp:message></td>
							<td align="left" id="left">
								<input id="name" name="name" class="input_bd ydwx_name"
									type="text" maxlength="20" value=""/>
									<font class="ydwx_asterisk">&nbsp;&nbsp;*</font>
							</td>
						</tr>
						<tr >
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_leibeis" defVal="类&nbsp;&nbsp;别：" fileName="ydwx"></emp:message></td>
							<td align="left">
								<select id="dataTypeId" name="dataTypeId" class="ydwx_dataTypeId" isInput="false">
								<%for(LfWXDataType dataType:dataTypes){%>
								<option value="<%=dataType.getId() %>"><xmp><%=dataType.getName() %></xmp></option>
								<% } %>
								</select>
								<%if(btnMap.get(menuCode+"-4")!=null){%>
								<a class="ydwx_manageType_a" onclick="javascript:manageType()" ><emp:message key="ydwx_wxgl_hdxgl_gllb" defVal="管理类别" fileName="ydwx"></emp:message></a>
								<%} %>
							</td>
						</tr>

                        <%--
                        <tr >
                            <td align="right"><label >回复设置：</label></td>
                            <td align="left" >
                            <input name="replySetType" type="radio" value="1" checked="checked">
                            多次回复有效
                            <input type="radio" value="2"  name="replySetType" style="margin-left:10px;" >
                            首次回复有效
                            <input type="radio" value="3" name="replySetType" "margin-left:10px;">
                            末次回复有效
                            </td>
                            </tr>
                            --%>

                        <tr>
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_leixing" defVal="类&nbsp;&nbsp;型：" fileName="ydwx"></emp:message></td>
							<td >
								<select name="quesType" id="quesType" class="ydwx_quesType" onchange="changeQuesType();" isInput="false">
								<option value="1"><emp:message key="ydwx_wxgl_hdxgl_huida" defVal="回答类" fileName="ydwx"></emp:message></option>
								<option value="2"><emp:message key="ydwx_wxgl_hdxgl_danxuan" defVal="单选类" fileName="ydwx"></emp:message></option>
								<option value="3"><emp:message key="ydwx_wxgl_hdxgl_duoxuan" defVal="多选类" fileName="ydwx"></emp:message></option>
								<option value="4"><emp:message key="ydwx_wxgl_hdxgl_xialakuang" defVal="下拉框类" fileName="ydwx"></emp:message></option>
								</select>
							</td>
						</tr>
						<tr >
							<td align="right"><emp:message key="ydwx_wxgl_hdxgl_neirongss" defVal="内&nbsp;&nbsp;容：" fileName="ydwx"></emp:message></td>
							<td align="left" id="left">
								<textarea rows="4" cols="10" class="ydwx_quesContent" name="quesContent" id="quesContent" onblur="eblur($(this))"></textarea>
							<font class="ydwx_asterisk">&nbsp;&nbsp;*</font><br />
                                <font class="ydwx_quesstrlen_font"><b id="quesstrlen"> 0 </b><b>/512</b></font>
							</td>
						</tr>
						<tr<%-- class="ydwx_addCol"--%> id="addCol"><td></td><td><a onclick="javascript:addCols()" class="ydwx_addCol_a" ><emp:message key="ydwx_wxgl_hdxgl_tjsx" defVal="添加选项" fileName="ydwx"></emp:message></a></td></tr>
						<tr id="initCols_show" style="display:none;"><td></td><td><table id="initCols">
						</table></td></tr>

                    </tbody>
				</table>
				</div>
				<div class="ydwx_btns">
					<input type="button" id="save" value="<emp:message key='ydwx_common_btn_baocun' defVal='保  存' fileName='ydwx'></emp:message>" class="btnClass5 mr10"  onclick="javascript:saveTrustData();"/>
					<input type="reset" class="btnClass6 mr10"  value="<emp:message key='ydwx_common_btn_chongzhi' defVal='重  置' fileName='ydwx'></emp:message>" onclick="javascript:location.href='wx_trustdata.htm?method=toEdit&lgcorpcode=<%=request.getParameter("lgcorpcode")%>';" >
					<input type="button" class="btnClass6"  value="<emp:message key='ydwx_common_btn_fanhui' defVal='返  回' fileName='ydwx'></emp:message>" onclick="javascript:doreturn();" ><br/>
				</div>
			</form>

		</div>
		<div id="addTypeDiv" title="<emp:message key="ydwx_wxgl_hdxgl_hdlbgl" defVal="互动类别管理" fileName="ydwx"></emp:message>" class="ydwx_addTypeDiv">
				<iframe id="tempFrame" name="tempFrame" class="ydwx_tempFrame"  marginwidth="0" scrolling="no" frameborder="no" src ="<%=iPath%>/blank.jsp " ></iframe>
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
 <%-- 加载JS 文件 --%>
 <script type="text/javascript" src="<%=iPath%>/js/wxData.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript"
                src="<%=commonPath%>/common/js/newSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 <script type="text/javascript">
	 //设置样式
	 $("#addCol").css("display","none");
	// 返回互动项管理页面
    function goback() {
		window.location.href="<%=path%>/wx_trustdata.htm";
	}

     $('#dataTypeId').isSearchSelectNew({'id' : 'newSelect_01', 'width': '302', 'enableInput': false, 'zIndex': 0});
     $('#quesType').isSearchSelectNew({'id' : 'newSelect_02', 'width': '302', 'enableInput': false, 'zIndex': 0}, function () {
         changeQuesType()
     });

</script>
</body>
</html>