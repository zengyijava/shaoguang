<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.segnumber.PbServicetype"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);



PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("phoneNo");
@ SuppressWarnings("unchecked")
List<PbServicetype> psList = (List<PbServicetype>)request.getAttribute("psList");
@ SuppressWarnings("unchecked")
Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String txglFrame = skin.replace(commonPath, inheritPath);

//修改号段
String xghd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_xghd", request);
//运营商：
String yys = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yys", request);
    if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
//号段：
String hd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_hd", request);
    if(hd!=null&&hd.length()>1){
    	hd = hd.substring(0,hd.length()-1);
    }
//确定
String qd = MessageUtils.extractMessage("common", "common_confirm", request);
//取消
String qx = MessageUtils.extractMessage("common", "common_cancel", request);
//移动
String Mobile = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yd", request);
//联通
String Unicom = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_lt", request);
//电信
String Telecom = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dx", request);
%>
<html>
	<head>
		<%@include file="/common/common.jsp"%>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link
			href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link rel="stylesheet"
			href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet"
			href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/css">
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/seg_phoneNo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/seg_phoneNo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="seg_phoneNo">
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>

			<div id="editNo" title="<%=xghd %>"
				class="editNo">
				<center>
					<table>
						<tr>
							<td>
								<emp:message key="txgl_wgqdpz_yyshdgl_yys" defVal="运营商："
									fileName="txgl"></emp:message>
							</td>
							<td>
								<input class="input_bd spisuncm" type="text" name="spisuncm"
									id="spisuncm" value="" onfocus="this.blur()"
									readonly="readonly"   />
								<input type="hidden" id="spid" value="" />
								<input type="hidden" id="keyId" value="" />
							</td>
						</tr>
						<tr>
							<td>
								<label class="hd_label">
									<emp:message key="txgl_wgqdpz_yyshdgl_hd" defVal="号　段："
										fileName="txgl"></emp:message>
								</label>
							</td>
							<td>
								<textarea name="phoneNo" id="phoneNo" class="input_bd phoneNo" ></textarea>
							</td>
						</tr>
						<tr>
							<td class="bt1_td" colspan="2">
								<input id="bt1" class="btnClass5 mr23" type="button"
									value="<%=qd %>" onclick="javascript:editNo();" />
								<input id="bt2" onclick="javascript:btcancel();"
									class="btnClass6" type="button" value="<%=qx %>" />
								<br />
							</td>
						</tr>
					</table>
				</center>

			</div>

			<%-- 内容开始 --%>
			<form name="pageForm" action="w_phoneNo.htm?method=find"
				method="post" id="pageForm">
				<div id="rContent" class="rContent">
					<div class="rContent_down_div">
						<table id="content">
							<thead>
								<tr>
									<th>
										<emp:message key="txgl_wygl_wytdgl_1" defVal="运营商"
											fileName="txgl"></emp:message>
									</th>
									<th>
										<emp:message key="txgl_wygl_wytdgl_2" defVal="号段"
											fileName="txgl"></emp:message>
									</th>
									<%if(btnMap.get(menuCode+"-3")!=null){ %>
									<th>
										<emp:message key="txgl_wgqdpz_yyshdgl_cz" defVal="操作"
											fileName="txgl"></emp:message>
									</th>
									<%} %>
								</tr>
							</thead>
							<tbody>
								<%
						if (psList != null && psList.size() > 0)
						{
							String keyId;
						    for (PbServicetype ps : psList)
						    {
						    	keyId = keyIdMap.get((long)(ps.getSpisuncm()));
						    	String operatorName = ps.getServiceinfo();
								operatorName = "移动".equals(operatorName)?Mobile:"联通".equals(operatorName)?Unicom:Telecom;
						%>
								<tr>
									<td class="ztalign"><%= operatorName %></td>
									<td>
										<%=ps.getServiceno() %>
										<input type="hidden" id="<%=ps.getSpisuncm()%>"
											name="<%=ps.getSpisuncm()%>" value="<%=ps.getServiceno() %>" />
									</td>
									<%if(btnMap.get(menuCode+"-3")!=null){ %>
									<td>
										<a
											onclick="javascript:edit('<%=ps.getSpisuncm() %>','<%=operatorName%>','<%=ps.getServiceno() %>','<%=keyId %>')"><emp:message
												key="txgl_wgqdpz_yyshdgl_xg" defVal="修改" fileName="txgl"></emp:message>
										</a>
									</td>
									<%} %>
								</tr>
								<%}} %>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="3">
										&nbsp;
									</td>
								</tr>
							</tfoot>
						</table>
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
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=iPath %>/js/seg_phoneNo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript">
		var isIE = false;
		var isFF = false;
		var isSa = false;
		if ((navigator.userAgent.indexOf("MSIE") > 0)
				&& (parseInt(navigator.appVersion) >= 4))
			{isIE = true;}
		if (navigator.userAgent.indexOf("Firefox") > 0)
			{isFF = true;}
		if (navigator.userAgent.indexOf("Safari") > 0)
			{isSa = true;}
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_1"));	
		       return;			       
		    }
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
		
			$('#search').click(function(){submitForm();});
		
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
			/*$('#phoneNo').keypress(function(e) {
				var iKeyCode = window.event ? e.keyCode
						: e.which;
				var vv = !(((iKeyCode >= 48) && (iKeyCode <= 57))
						|| (iKeyCode == 44)
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
			});*/
			//控制不能由输入法输入其他字符
			$('#phoneNo').keyup(function() {
				var str = $('#phoneNo').val();
				//只能输入0-9或者英文标点","符号、回车换行
				var reg = /[^0-9,\r\n]+/g;
				str = str.replace(reg, "");
				$('#phoneNo').val(str);
			});
			
		});
		
		</script>
	</body>
</html>
