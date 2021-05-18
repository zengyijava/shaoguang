<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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

@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
List<LfProcess> proList=(List<LfProcess>)request.getAttribute("proList");
String serId = (String)request.getAttribute("serId");
String serName = (String)request.getAttribute("serName");
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("moService");
menuCode = menuCode==null?"0-0-0":menuCode;
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	</head>
	<body id="eng_moProcess" class="eng_moProcess">
		<div id="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,"步骤管理") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_bzgl",request)) %>
				<div class="titletop" >
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="znyq_ywgl_sxywgl_bzgl" defVal="步骤管理" fileName="znyq"></emp:message>（<%=serName %>）
							</td>
							<td class="titletop_fontTd">
								<span class="titletop_font" onclick="javascript:location.href='<%=path%>/eng_moService.htm?method=find&lguserid='+$('#lguserid').val()">&larr;&nbsp;<emp:message key="znyq_ywgl_sxywgl_fhsyj" defVal="返回上一级" fileName="znyq"></emp:message></span>
							</td>
						</tr>
					</table>
				</div>
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="eng_moService.htm?method=find" method="post" id="pageForm">
			<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<%--  <div>
			<label style="font-size:14px;font-weight:bold;">步骤管理：<%=serName %></label>
			<font class="titletop_font" onclick="window.location.href='eng_moService.htm?method=find&lguserid='+$('#lguserid').val()">←返回上一级</font>
			</div>--%>
			<div class="buttons">
				<a id="add" onclick="javascript:getProInfos('<%=serId %>','')"><emp:message key="znyq_ywgl_sxywgl_xjbz" defVal="新建步骤" fileName="znyq"></emp:message></a>
				<span id="backgo" class="right mr5" onclick="javascript:location.href='<%=path%>/eng_moService.htm?method=find&lguserid='+$('#lguserid').val()">&nbsp;<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message></span>
			</div>
			<table id="content">
					<thead>
						<tr>
							<th>
								<emp:message key="znyq_ywgl_sxywgl_bzxh" defVal="步骤序号" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_sxywgl_bzmc" defVal="步骤名称" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_sxywgl_bzlx" defVal="步骤类型" fileName="znyq"></emp:message>
							</th>
							<th colspan="4">
								<emp:message key="znyq_ywgl_sxywgl_czgl" defVal="操作管理" fileName="znyq"></emp:message>
							</th>
						</tr>
					</thead>
					<tbody>
					<%
					if (proList != null && proList.size() > 0)
					{
						for(int i=0;i < proList.size();i++)
						{
							LfProcess process=proList.get(i);
					%>
					<tr>
						<td><%=process.getPrNo() %></td>
						<td>
						<xmp class="commonXmp">
							<%=process.getPrName() %>
						</xmp>
						</td>
						<td>
							<%
								if(process.getPrType()-4==0)
								{
									out.print("Select");
								}else if(process.getPrType()-5==0)
								{
									out.print("Reply");
								}else if(process.getPrType()-1==0)
								{
									out.print("Insert");
								}else if(process.getPrType()-2==0)
								{
									out.print("Delete");
								}else if(process.getPrType()-3==0)
								{
									out.print("Update");
								}
							%>
						</td>
						<td>
							<a onclick="getProInfos('<%=serId %>','<%=process.getPrId() %>')"><emp:message key="znyq_ywgl_sxywgl_bzxx" defVal="步骤信息" fileName="znyq"></emp:message></a>
						</td>
						<%
							if(process.getPrType()-4<=0)
							{
						%>
						<td colspan="2">
							<a onclick="getDatabaseInfos('<%=serId %>','<%=process.getPrId() %>')"><emp:message key="znyq_ywgl_sxywgl_sjkpz" defVal="数据库配置" fileName="znyq"></emp:message></a>
						</td>
						<td colspan="1">
							<a href="javascript:delPro(<%=process.getPrId() %>,'<%=process.getPrName() %>')"><emp:message key="znyq_ywgl_common_btn_5" defVal="删除" fileName="znyq"></emp:message></a>
						</td>
						<%
							}else if(process.getPrType()-5==0)
							{
						%>
						 <td>
							<a onclick="getConditionInfos('<%=serId %>','<%=process.getPrId() %>')"><emp:message key="znyq_ywgl_sxywgl_zxtj" defVal="执行条件" fileName="znyq"></emp:message></a>
						</td>
						<td class="TemplateInfosTd">
							<a  onclick="getTemplateInfos('<%=serId %>','<%=process.getPrId() %>')"><emp:message key="znyq_ywgl_sxywgl_dxmbpz" defVal="短信模板配置" fileName="znyq"></emp:message></a>
						</td>
						
						<td colspan="1">
							<a href="javascript:delPro('<%=process.getPrId() %>','<%=process.getPrName() %>')"><emp:message key="znyq_ywgl_common_btn_5" defVal="删除" fileName="znyq"></emp:message></a>
						</td>
						<%
							}
						%>
					</tr>
					<%
						}
					}
					else {
					%>
					<tr>
						<td colspan="9" align="center">
						<emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message>
						</td>
					</tr>
					<%} %>
					</tbody>
				</table>
			</form>
			</div>
			<div id="ProInfos" title="<emp:message key="znyq_ywgl_sxywgl_bzxx" defVal="步骤信息" fileName="znyq"></emp:message>">
				<iframe id="proInfoflowFrame" name="proInfoflowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</div>
			<div id="excutecCondition" title="<emp:message key="znyq_ywgl_sxywgl_zxtj" defVal="执行条件" fileName="znyq"></emp:message>">
				<iframe id="excutecConditionFrame" name="excutecConditionFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</div>
			<div id="databaseSet" title="<emp:message key="znyq_ywgl_sxywgl_sjkpz" defVal="数据库配置" fileName="znyq"></emp:message>">
				<iframe id="databaseSetFrame" name="databaseSetFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</div>
			<div id="templateSet" title="<emp:message key="znyq_ywgl_sxywgl_dxmbpz" defVal="短信模板配置" fileName="znyq"></emp:message>">
				<iframe id="templateSetFrame" name="templateSetFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</div>
			<div id="tmplDiv" title="<emp:message key="znyq_ywgl_sxywgl_jtwxnrxz" defVal="静态网讯内容选择" fileName="znyq"></emp:message>">
				<iframe id="tempFrame" name="tempFrame" marginwidth="0" scrolling="no" frameborder="no"  ></iframe>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
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
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				}, function() {
				$(this).removeClass("hoverColor");
			});
			$('#u_o_c_explain').find('> p').next().hide();
			$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
		
			$('#ProInfos').dialog({
				autoOpen: false,
				height: 250,
				width:350,
				modal:true
			});
			$('#excutecCondition').dialog({
				autoOpen: false,
				height:300,
				width:670,
				modal:true
			});
			$('#databaseSet').dialog({
				autoOpen: false,
				height: 280,
				width:520,
				modal:true
			});
			$('#templateSet').dialog({
				autoOpen: false,
				height: 450,
				width:550,
				modal:true
			});
			$("#tmplDiv").dialog({
				autoOpen: false,
				height:520,
				width: 690,
				resizable:false,
				modal: true
			});
		});
		//步骤信息
		function getProInfos(serId,prId)
		{
			$("#proInfoflowFrame").attr("src","<%=path %>/eng_moService.htm?method=doProEdit&prId="+prId+"&serId="+serId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
		    $('#ProInfos').dialog('open');
		}
		function closeDiv()
		{
			$("#ProInfos").dialog("close");
			$("#proInfoflowFrame").attr("src","");
		}
		//执行条件
		function getConditionInfos(serId,prId)
		{
			$("#excutecConditionFrame").attr("src","<%=path %>/eng_moService.htm?method=editCon&prId="+prId+"&serId="+serId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
		    $('#excutecCondition').dialog('open');
		}
		function closeConditionDiv()
		{
			$("#excutecCondition").dialog("close");
			$("#excutecConditionFrame").attr("src","");
		}
		//数据库设置
		function getDatabaseInfos(serId,prId)
		{
			$("#databaseSetFrame").attr("src","<%=path %>/eng_moService.htm?method=editSql&prId="+prId+"&serId="+serId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
		    $('#databaseSet').dialog('open');
		}
		function closeDatabaseDiv()
		{
			$("#databaseSet").dialog("close");
			$("#databaseSetFrame").attr("src","");
		}
		//短信模板设置
		function getTemplateInfos(serId,prId)
		{
			$("#templateSetFrame").attr("src","<%=path %>/eng_moService.htm?method=editTemp&prId="+prId+"&serId="+serId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
		    $('#templateSet').dialog('open');
		}
		function closeTemplateDiv()
		{
			$("#templateSet").dialog("close");
			$("#templateSetFrame").attr("src","");
		}
		function chooseNetTpl()
		{
			$(".ui-dialog-titlebar-close").show();
			var frameSrc = $("#tempFrame").attr("src");
			var lgcorpcode = $("#lgcorpcode").val();
			
				var lguserid = $("#lguserid").val();
				frameSrc = "eng_mtProcess.htm?method=toWxTmpl&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				$("#tempFrame").attr("src",frameSrc);
			
			$("#tmplDiv").dialog("open");
		}
		//隐藏层
		function closeDialog(){
			$("#tmplDiv").dialog("close");
			$("#tempFrame").attr("src","");
		}
	    function setWxInfo(wxId){
	    	var newStr = '#W_'+wxId+'#';
	    	//var oldCon = $("#templateSetFrame").contents().find("#msgMain").val();
	    	//$("#msgMain").val(oldCon+newStr);
	    	//$("#templateSetFrame").contents().find("#msgMain").val(oldCon+newStr);
	    	$("#templateSetFrame")[0].contentWindow.add(newStr);
	    	//$(window.parent.document).contents().find("#templateSetFrame")[0].add(newStr);
	    }
		</script>
		
	</body>
</html>