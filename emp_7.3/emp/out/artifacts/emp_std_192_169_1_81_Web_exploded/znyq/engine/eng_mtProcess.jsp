<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
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
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String context = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
		
	String serId=request.getParameter("serId");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	
	LfService service = (LfService) request.getAttribute("service");

	LfSysuser curSysuser=(LfSysuser)request.getAttribute("curSysuser");
	@ SuppressWarnings("unchecked")
	Map<Integer,LfProcess> proMaps=(Map<Integer,LfProcess>)request.getAttribute("proMaps");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_bzgl" defVal="步骤管理" fileName="znyq"></emp:message></title>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	</head>
	<body id="eng_mtProcess" classs="eng_mtProcess">
			<div id="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"步骤管理") %>--%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_bzgl",request)) %>
			
				<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="znyq_ywgl_xhywgl_bzgl" defVal="步骤管理" fileName="znyq"></emp:message>（<%=service.getSerName() %>）
							</td>
							<td align="right">
								<span class="titletop_font" onclick="javascript:location.href='<%=path%>/eng_mtService.htm?method=find&lguserid='+$('#lguserid').val()">&larr;&nbsp;<emp:message key="znyq_ywgl_xhywgl_fhsyj" defVal="返回上一级" fileName="znyq"></emp:message></span>
							</td>
						</tr>
					</table>
				</div>
					<div class="rContent">
						
						<%-- <div class="topcontentdiv">
							<div class="topcontenttitle">
								步骤修改（<%=service.getSerName() %>）
							</div>
							<div style="text-align: right;">
							<a id="back" onclick="javascript:location.href='<%=path%>/eng_mtService.htm?method=find&lguserid='+$('#lguserid').val()">&larr;&nbsp;返回上一级</a>
							</div>
						</div> --%>
						
						<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
						<input type="hidden" name="method" value="delete"/>
						<input type="hidden" name="prId" id="prId" value=""/>
						<input type="hidden" name="prName" id="prName" value=""/>
						<input type="hidden" name="serId" value="<%=serId %>"/>
						<input type="hidden" name="serType" value="2"/>
						
						<table id="processtable" class="div_bd">
							<thead>
								<tr>
									<th id="arrow" colspan="2" class="title_bg"><emp:message key="znyq_ywgl_xhywgl_selectlx" defVal="Select类型" fileName="znyq"></emp:message></th>
									
										
									
								</tr>
							</thead>
							<tbody>
							<tr id="ptcontent">
								<td  colspan="2">
								<div class="pronamediv">
								<%--<span id="proTitleSpan_4"><%= proMaps.get(4) == null?"":"步骤名称：" %></span><span id="prNameSpan_4"><%= proMaps.get(4) == null?"":proMaps.get(4).getPrName() %></span>--%>
								<span id="proTitleSpan_4"><%= proMaps.get(4) == null?"":MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_bzmc_mh",request) %></span><span id="prNameSpan_4"><%= proMaps.get(4) == null?"":proMaps.get(4).getPrName() %></span>
								</div>
								<div>
								<%--<a id="prEditLink_4" href="javascript:showInfo(4,<%= proMaps.get(4) == null?"-1":proMaps.get(4).getPrId() %>);"><%= proMaps.get(4) == null?"新增数据库查询":"修改数据库查询" %></a> --%>
								<a id="prEditLink_4" href="javascript:showInfo(4,<%= proMaps.get(4) == null?"-1":proMaps.get(4).getPrId() %>);"><%= proMaps.get(4) == null?MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_xzsjkcx",request):MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_xgsjkcx",request) %></a>
								</div>
								</td>
							</tr>
							</tbody>
						</table>
						<br/>
						<br/>
						<br/>
						<table id="processtable" class="div_bd">
							<thead >
								<tr>
									<th id="arrow" colspan="2" class="title_bg"><emp:message key="znyq_ywgl_xhywgl_replylx" defVal="Reply类型" fileName="znyq"></emp:message></th>
								</tr>
							</thead>
							<tbody>
							<tr id="ptcontent">
								<td  colspan="2">
								<div class="pronamediv">
								<%--<span id="proTitleSpan_5"><%= proMaps.get(5) == null?"":"步骤名称：" %></span><span id="prNameSpan_5"><%= proMaps.get(5) == null?"":proMaps.get(5).getPrName() %></span> --%>
								<span id="proTitleSpan_5"><%= proMaps.get(5) == null?"":MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_bzmc_mh",request) %></span><span id="prNameSpan_5"><%= proMaps.get(5) == null?"":proMaps.get(5).getPrName() %></span>
								</div>
								<div>
								<%
									String href="";
									String astyle="";
									if(proMaps.get(4) != null)
									{
										Long prId = proMaps.get(5) == null? -1 : proMaps.get(5).getPrId();
										href="javascript:showInfo(5,"+ prId +");";
									}
									else
									{
										//设置为灰色和提示函数
										astyle = "class='astyle' onclick='disableALink()'";
									}
								%>
								<a id="prEditLink_5" <%=href!=null&&!"".equals(href)?"href='"+href+"'":""%> <%out.print(astyle); %>>
									<%--<%= proMaps.get(5) == null?"新增短信模板":"修改短信模板" %>--%>
									<%= proMaps.get(5) == null?MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_xzdxmb",request):MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_xgdxmb",request) %>
								</a>
								</div>
								</td>
							</tr>
							</tbody>
						</table>
						<div class="firstDiv">
							<input type="button" value="<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message>" onclick="javascript:location.href='<%=path%>/eng_mtService.htm?method=find'" class="btnClass6">
						</div>
						
						
						<div id="infoDiv" title="<emp:message key="znyq_ywgl_xhywgl_bz" defVal="步骤" fileName="znyq"></emp:message>">
							<input id="flowNames" type="hidden" name="flowNames" value="" />
							<iframe id="flowFrame" name="flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
							<table>
								<tr><td>
								<input type="button" id="editok"  value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" class="btnClass5 mr23" onclick="javascript:doOk()" />
								<input type="button" id="editCancel" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" class="btnClass6" onclick="javascript:closediv();" />
								<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
								<br/>
								</td>
								</tr>
							</table>
						</div>
						
					
					</div>
					
					<%-- 这是每个界面相应的DIV --%>
					<div class="clear"></div>
					<div class="bottom">
						<div id="bottom_right">
							<div id="bottom_left"></div>
						</div>
					</div>
				</div>
				<%--end round_content--%>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_mtProcess.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		function show(){
			<% String result=(String)request.getAttribute("result");
				if(result!=null && result.equals("1")){%>
				//alert("新增步骤成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xzbzcg"));
			<%}else if(result!=null && result.equals("2")){%>
				//alert("修改步骤成功！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgbzcg"));
			<%}else if(result!=null && result.equals("3")){%>
				//alert("数据库配置成功！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkpzcg"));
			<%}else if(result!=null && result.equals("4")){%>
				//alert("短信模板配置成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxmbpzcg"));
			<%}else if(result!=null && result.equals("5")){%>
				//alert("删除步骤成功！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_scbzcg"));		
			<%}else if(result!=null && result.equals("0")){%>
				//alert("操作失败");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
			<%}request.removeAttribute("result");
			 if (result != null)
		    {%>
			     location.href="eng_mtProcess.htm?serId=<%=serId%>&lguserid="+$('#lguserid').val();
		    <%  }%>
		}
		
		
		function showInfo(prType,prId)
		{
			var title = "";
			if(prType == 4)
			{
				//select步骤
				//title = "数据库查询";
				title = getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkcx");
			}
			else if(prType == 5)
			{
				//reply步骤
				//title = "短信模板";
				title = getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxmb");
			}
			else
			{
				//alert("参数异常");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_csyc"));
			}
			$("#infoDiv").dialog("option","title",title);
			
			if(prId != -1)
			{
				//修改
				$("#flowFrame").attr("src","<%=path %>/eng_mtProcess.htm?method=toEdit&prType="+prType+"&serId=<%=serId %>&prId="+prId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
			}
			else
			{
				//新增
				$("#flowFrame").attr("src","<%=path %>/eng_mtProcess.htm?method=toEdit&prType="+prType+"&serId=<%=serId %>&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
			}
			
			$("#infoDiv").dialog("open");
		}
		
		

      </script>
	
	</body>
</html>