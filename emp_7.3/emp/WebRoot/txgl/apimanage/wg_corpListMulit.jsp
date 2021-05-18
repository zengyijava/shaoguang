<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

	@ SuppressWarnings("unchecked")
	List<LfCorp> corpList = (List<LfCorp>)request.getAttribute("corpList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	conditionMap=(LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	String code=conditionMap.get("corpCode&like");
	String name=conditionMap.get("corpName&like");
	String txglFrame = skin.replace(commonPath, inheritPath);
%>
<html>
	<head><%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/wg_corpListMulit.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="wg_corpListMulit">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="wg_apiBaseMage.htm?method=customersList" method="post"
					id="pageForm">
					<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
					<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
					<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					<input type="hidden" id="skinType" value="<%=skin %>"/>
					<input id="pathUrl" value="<%=path%>" type="hidden" />
					
			<div id="rContent" class="rContent rContent2"  >
					<div id="condition">
						<table>
							<tr>
								<td>
									<span> <emp:message key='txgl_apimanage_text_31' defVal='企业编号：' fileName='mwadmin'/> </span>
								</td>
								<td>
									<input type="text" name="code" 
										id="code" class="code"  maxlength="50" value="<%=null==code?"":code %>" onkeyup="value=value.replace(/\D/g,'')"/>
								</td>
								<td>
									<span> <emp:message key='txgl_apimanage_text_32' defVal='企业名称：' fileName='mwadmin'/> </span>
								</td>
								<td>
									<input type="text" name="condcorpname" 
										id="condcorpname" class="condcorpname"  maxlength="50" value="<%=null==name?"":name%>"/>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
							<th>
								 <emp:message key='txgl_apimanage_text_71' defVal='选择' fileName='mwadmin'/> 
							</th>
							<th>
								 <emp:message key='txgl_apimanage_text_72' defVal='企业编号' fileName='mwadmin'/> 
							</th>
							<th>
								 <emp:message key='txgl_apimanage_text_39' defVal='企业名称' fileName='mwadmin'/> 
							</th>
							<th>
								 <emp:message key='txgl_apimanage_text_73' defVal='状态' fileName='mwadmin'/> 
							</th>
										
									</tr>
								</thead>
								<tbody>
								<%
								if (corpList != null && corpList.size() > 0)
								{
									for (LfCorp corp : corpList)
									{
								%>
									<tr>
										<td>
											<input type="radio" name="checklist" id="checklist" value="<%=corp.getCorpCode() %>" corpcode="<%=corp.getCorpCode() %>" corpname="<%=corp.getCorpName().replace("<","&lt;").replace(">","&gt;") %>"/>
										</td>
										<td>
										<%=corp.getCorpCode()%>
										</td>
										<td>
										<%=corp.getCorpName().replace("<","&lt;").replace(">","&gt;")%>		
										</td>
										<td>
										<%=corp.getCorpState()==1?MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_48",request):
											(corp.getCorpState()==0?MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_49",request):"")%>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="5"> <emp:message key='txgl_apimanage_text_52' defVal='无记录' fileName='mwadmin'/></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="5">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
					<div class="xz_div">
						<input class="btnClass5 mr23" onclick="tempSure()" value=" <emp:message key='txgl_apimanage_text_71' defVal='选择' fileName='mwadmin'/>" type="button"/>
						<input class="btnClass6" onclick="tempCancel()" value=" <emp:message key='txgl_apimanage_text_74' defVal='取消' fileName='mwadmin'/>" type="button"/>
						<br>
					</div>
					
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
			</form>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		//页面加载，初始化相关数据
		$(document).ready(function() {
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				}, function() {
				$(this).removeClass("hoverColor");
			});
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
		});

		<%--彩信模板确认--%>
		function tempSure()
		{
			var tem = $("input[type='radio']:checked").val();
			var $ro = $("input[type='radio']:checked");
			if(tem == undefined || tem == "" || tem == null)
			{
				alert(getJsLocaleMessage("txgl","txgl_js_userdata_17"));
				return;
			}else
			{
				var corpcode = $ro.attr("corpcode");
				var corpname  =$ro.attr("corpname");
				<%--选择的模板ID  模板URL--%>
				$(window.parent.document).find("#corp_code").val(corpcode);
				$(window.parent.document).find("#corp_name").val(corpname);
			}
			<%--调用父窗口的函数--%>
			parent.closeDialog();
		}

		//关闭窗口
		function tempCancel(){
			<%--调用父窗口的函数--%>
			parent.closeDialog();
		}
		</script>
	</body>
</html>
