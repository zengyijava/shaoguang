<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiSendlog"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressWarnings("unchecked")
    List<LfWeiAccount> otWeiAcctList = (List<LfWeiAccount>)request.getAttribute("otWeiAcctList");
    
    String tcount = String.valueOf(request.getAttribute("tcount"));
    String mcount = String.valueOf(request.getAttribute("mcount"));
    
    LfWeiSendlog sendlog = (LfWeiSendlog)request.getAttribute("sendlog");
    
  	//公众帐号过滤
    String aid = String.valueOf(sendlog.getAId());
    if(aid == null || "".equals(aid))
    {
    	aid = "-1";
    }
    
    //发送 对象
    String tp = sendlog.getTp();
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style>
		 dd,dt{float: left;zoom: 1;}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="/yxgl_fzqfManager.htm" method="post" id="pageForm">
					<div style="display: none" id="hiddenValueDiv"></div>
					<input type="hidden" id="pathUrl" value="<%=path%>" />
					<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode%>" />
					<input type="hidden" id="msgtype" value="<%=sendlog.getMsgType() %>" />
					<input type="hidden" name="sendlogId" id="sendlogId" value="<%= sendlog.getSendId() %>" />
					<div id="condition">
					<table id="searchCondition" class="div_bd div_bg">
							<tbody>
								<tr>
									<td><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/>
									<label>
										<select id="aid" name="a_id" class="input_bd">
											<option value=""><emp:message key="wxgl_gzhgl_title_135" defVal="请选择公众帐号" fileName="wxgl"/></option>
											<%
												if (otWeiAcctList != null && otWeiAcctList.size() > 0) {
													for (LfWeiAccount acct : otWeiAcctList) {
														String aId = String.valueOf(acct.getAId());
											%>
											<option value="<%=acct.getAId()%>"
												<%=(aId.equals(aid)) ? "selected" : ""%>>
												<%=acct.getName()%></option>
											<%
												}
												}
											%>
									</select>
									</label>
									
									<emp:message key="wxgl_gzhgl_title_154" defVal="发送对象：" fileName="wxgl"/>
									<label>
										<select id="tp" name="tp" class="input_bd">
											<option value="1" <%= ("1".equals(tp)) ?  "selected" : ""%>><emp:message key="wxgl_gzhgl_title_156" defVal="按地区选择" fileName="wxgl"/></option>
											<option value="0" <%= (!"1".equals(tp)) ?  "selected" : ""%>><emp:message key="wxgl_gzhgl_title_155" defVal="按分组选择" fileName="wxgl"/></option>
										</select>
									</label>
									</td>
									</tr>
									<tr>
									<td>
									<div>
										<dl class="dl-list" id="groupDiv" style="<%= (!"1".equals(tp)) ?  "" : "display:none;"%>"> 
											<dt>
												<emp:message key="wxgl_gzhgl_title_80" defVal="群组名称：" fileName="wxgl"/>
											</dt>
											<dd>
												<select id="groupid" name="groupid" class="input_bd" title="<emp:message key='wxgl_gzhgl_title_340' defVal='如果群组信息为空，请先做同步操作！' fileName='wxgl'/>">
												</select>
											</dd>
										</dl>
										<dl class="dl-list" id="areaDiv" style="<%= ("1".equals(tp)) ?  "" : "display:none;"%>">
											<dt>
												<emp:message key="wxgl_gzhgl_title_158" defVal="群发地区：" fileName="wxgl"/>
											</dt>
											<dd>
												<select id="areaid" name="areaid" class="input_bd">
													<option value='0'><emp:message key="wxgl_gzhgl_title_63" defVal="全部" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_159" defVal="中国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_159" defVal="中国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_160" defVal="不丹" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_160" defVal="不丹" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_161" defVal="中国台湾" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_161" defVal="中国台湾" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_162" defVal="中国澳门" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_162" defVal="中国澳门" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_163" defVal="中国香港" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_163" defVal="中国香港" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_164" defVal="中非共和国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_164" defVal="中非共和国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_165" defVal="丹麦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_165" defVal="丹麦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_166" defVal="乌克兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_166" defVal="乌克兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_167" defVal="乌兹别克斯坦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_167" defVal="乌兹别克斯坦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_168" defVal="乌干达" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_168" defVal="乌干达" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_169" defVal="乌拉圭" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_169" defVal="乌拉圭" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_170" defVal="乔治亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_170" defVal="乔治亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_171" defVal="也门" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_171" defVal="也门" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_172" defVal="亚美尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_172" defVal="亚美尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_173" defVal="以色列" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_173" defVal="以色列" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_174" defVal="伊拉克" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_174" defVal="伊拉克" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_175" defVal="伊朗" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_175" defVal="伊朗" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_176" defVal="俄罗斯" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_176" defVal="俄罗斯" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_177" defVal="保加利亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_177" defVal="保加利亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_178" defVal="克罗地亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_178" defVal="克罗地亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_179" defVal="关岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_179" defVal="关岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_180" defVal="冈比亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_180" defVal="冈比亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_181" defVal="冰岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_181" defVal="冰岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_182" defVal="列支敦士登" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_182" defVal="列支敦士登" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_183" defVal="刚果民主共和国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_183" defVal="刚果民主共和国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_184" defVal="利比亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_184" defVal="利比亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_185" defVal="利比里亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_185" defVal="利比里亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_186" defVal="加拿大" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_186" defVal="加拿大" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_187" defVal="加纳" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_187" defVal="加纳" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_188" defVal="匈牙利" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_188" defVal="匈牙利" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_189" defVal="北马里亚纳群岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_189" defVal="北马里亚纳群岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_190" defVal="南非" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_190" defVal="南非" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_191" defVal="博茨瓦纳" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_191" defVal="博茨瓦纳" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_192" defVal="卡塔尔" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_192" defVal="卡塔尔" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_193" defVal="卢旺达" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_193" defVal="卢旺达" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_194" defVal="卢森堡" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_194" defVal="卢森堡" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_195" defVal="印度" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_195" defVal="印度" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_196" defVal="印度尼西亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_196" defVal="印度尼西亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_197" defVal="危地马拉" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_197" defVal="危地马拉" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_198" defVal="厄瓜多尔" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_198" defVal="厄瓜多尔" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_199" defVal="厄立特里亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_199" defVal="厄立特里亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_200" defVal="古巴" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_200" defVal="古巴" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_201" defVal="吉尔吉斯斯坦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_201" defVal="吉尔吉斯斯坦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_202" defVal="吉布提" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_202" defVal="吉布提" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_203" defVal="哈萨克斯坦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_203" defVal="哈萨克斯坦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_204" defVal="哥伦比亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_204" defVal="哥伦比亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_205" defVal="哥斯达黎加" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_205" defVal="哥斯达黎加" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_206" defVal="喀麦隆" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_206" defVal="喀麦隆" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_207" defVal="土耳其" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_207" defVal="土耳其" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_208" defVal="圣基茨和尼维斯" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_208" defVal="圣基茨和尼维斯" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_209" defVal="圣马力诺" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_209" defVal="圣马力诺" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_210" defVal="坦桑尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_210" defVal="坦桑尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_211" defVal="埃及" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_211" defVal="埃及" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_212" defVal="埃塞俄比亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_212" defVal="埃塞俄比亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_213" defVal="基里巴斯" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_213" defVal="基里巴斯" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_214" defVal="塔吉克斯坦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_214" defVal="塔吉克斯坦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_215" defVal="塞内加尔" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_215" defVal="塞内加尔" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_216" defVal="塞尔维亚,黑山" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_216" defVal="塞尔维亚,黑山" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_217" defVal="塞拉利昂" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_217" defVal="塞拉利昂" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_218" defVal="塞舌尔" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_218" defVal="塞舌尔" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_219" defVal="墨西哥" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_219" defVal="墨西哥" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_220" defVal="多米尼加共和国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_220" defVal="多米尼加共和国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_221" defVal="奥地利" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_221" defVal="奥地利" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_222" defVal="委内瑞拉" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_222" defVal="委内瑞拉" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_223" defVal="孟加拉" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_223" defVal="孟加拉" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_224" defVal="安哥拉" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_224" defVal="安哥拉" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_225" defVal="安提瓜岛和巴布达" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_225" defVal="安提瓜岛和巴布达" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_226" defVal="安道尔" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_226" defVal="安道尔" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_227" defVal="尼加拉瓜" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_227" defVal="尼加拉瓜" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_228" defVal="尼日利亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_228" defVal="尼日利亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_229" defVal="尼泊尔" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_229" defVal="尼泊尔" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_230" defVal="巴哈马" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_230" defVal="巴哈马" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_231" defVal="巴基斯坦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_231" defVal="巴基斯坦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_232" defVal="巴巴多斯岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_232" defVal="巴巴多斯岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_233" defVal="巴布亚新几内亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_233" defVal="巴布亚新几内亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_234" defVal="巴拿马" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_234" defVal="巴拿马" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_235" defVal="巴林" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_235" defVal="巴林" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_236" defVal="巴西" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_236" defVal="巴西" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_237" defVal="布隆迪" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_237" defVal="布隆迪" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_238" defVal="希腊" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_238" defVal="希腊" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_239" defVal="帕劳群岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_239" defVal="帕劳群岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_240" defVal="开曼群岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_240" defVal="开曼群岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_241" defVal="德国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_241" defVal="德国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_242" defVal="意大利" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_242" defVal="意大利" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_243" defVal="所罗门群岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_243" defVal="所罗门群岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_244" defVal="拉脱维亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_244" defVal="拉脱维亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_245" defVal="挪威" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_245" defVal="挪威" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_246" defVal="捷克共和国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_246" defVal="捷克共和国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_247" defVal="摩尔多瓦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_247" defVal="摩尔多瓦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_248" defVal="摩洛哥" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_248" defVal="摩洛哥" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_249" defVal="摩纳哥" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_249" defVal="摩纳哥" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_250" defVal="文莱" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_250" defVal="文莱" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_251" defVal="斐济" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_251" defVal="斐济" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_252" defVal="斯威士兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_252" defVal="斯威士兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_253" defVal="斯洛伐克" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_253" defVal="斯洛伐克" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_254" defVal="斯洛文尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_254" defVal="斯洛文尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_255" defVal="斯里兰卡" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_255" defVal="斯里兰卡" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_256" defVal="新加坡" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_256" defVal="新加坡" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_257" defVal="新喀里多尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_257" defVal="新喀里多尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_258" defVal="新西兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_258" defVal="新西兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_259" defVal="日本" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_259" defVal="日本" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_260" defVal="智利" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_260" defVal="智利" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_261" defVal="朝鲜" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_261" defVal="朝鲜" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_262" defVal="柬埔寨" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_262" defVal="柬埔寨" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_263" defVal="格恩西岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_263" defVal="格恩西岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_264" defVal="格林纳达" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_264" defVal="格林纳达" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_265" defVal="格陵兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_265" defVal="格陵兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_266" defVal="比利时" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_266" defVal="比利时" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_267" defVal="毛里塔尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_267" defVal="毛里塔尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_268" defVal="毛里求斯" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_268" defVal="毛里求斯" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_269" defVal="汤加" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_269" defVal="汤加" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_270" defVal="沙特阿拉伯" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_270" defVal="沙特阿拉伯" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_271" defVal="法国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_271" defVal="法国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_272" defVal="波兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_272" defVal="波兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_273" defVal="波多黎各" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_273" defVal="波多黎各" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_274" defVal="泰国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_274" defVal="泰国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_275" defVal="泽西岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_275" defVal="泽西岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_276" defVal="洪都拉斯" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_276" defVal="洪都拉斯" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_277" defVal="海地" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_277" defVal="海地" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_278" defVal="澳大利亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_278" defVal="澳大利亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_279" defVal="爱尔兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_279" defVal="爱尔兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_280" defVal="牙买加" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_280" defVal="牙买加" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_281" defVal="特立尼达和多巴哥" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_281" defVal="特立尼达和多巴哥" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_282" defVal="玻利维亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_282" defVal="玻利维亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_283" defVal="瑞典" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_283" defVal="瑞典" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_284" defVal="瑞士" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_284" defVal="瑞士" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_285" defVal="瓦努阿图" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_285" defVal="瓦努阿图" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_286" defVal="留尼旺岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_286" defVal="留尼旺岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_287" defVal="百慕大" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_287" defVal="百慕大" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_288" defVal="直布罗陀" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_288" defVal="直布罗陀" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_289" defVal="科威特" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_289" defVal="科威特" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_290" defVal="秘鲁" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_290" defVal="秘鲁" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_291" defVal="约旦" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_291" defVal="约旦" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_292" defVal="纳米比亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_292" defVal="纳米比亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_293" defVal="缅甸" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_293" defVal="缅甸" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_294" defVal="罗马尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_294" defVal="罗马尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_295" defVal="美国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_295" defVal="美国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_296" defVal="老挝" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_296" defVal="老挝" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_297" defVal="肯尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_297" defVal="肯尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_298" defVal="芬兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_298" defVal="芬兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_299" defVal="苏丹" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_299" defVal="苏丹" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_300" defVal="苏里南" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_300" defVal="苏里南" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_301" defVal="英国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_301" defVal="英国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_302" defVal="荷兰" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_302" defVal="荷兰" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_303" defVal="莫桑比克" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_303" defVal="莫桑比克" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_304" defVal="莱索托" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_304" defVal="莱索托" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_305" defVal="菲律宾" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_305" defVal="菲律宾" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_306" defVal="萨摩亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_306" defVal="萨摩亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_307" defVal="葡萄牙" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_307" defVal="葡萄牙" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_308" defVal="蒙古" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_308" defVal="蒙古" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_309" defVal="西班牙" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_309" defVal="西班牙" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_310" defVal="贝宁" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_310" defVal="贝宁" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_311" defVal="赞比亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_311" defVal="赞比亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_312" defVal="越南" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_312" defVal="越南" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_313" defVal="阿塞拜疆" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_313" defVal="阿塞拜疆" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_314" defVal="阿尔及利亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_314" defVal="阿尔及利亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_315" defVal="阿尔巴尼亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_315" defVal="阿尔巴尼亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_316" defVal="阿拉伯联合酋长国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_316" defVal="阿拉伯联合酋长国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_317" defVal="阿曼" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_317" defVal="阿曼" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_318" defVal="阿根廷" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_318" defVal="阿根廷" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_319" defVal="阿鲁巴" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_319" defVal="阿鲁巴" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_320" defVal="韩国" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_320" defVal="韩国" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_321" defVal="马其顿" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_321" defVal="马其顿" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_322" defVal="马尔代夫" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_322" defVal="马尔代夫" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_323" defVal="马拉维" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_323" defVal="马拉维" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_324" defVal="马来西亚" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_324" defVal="马来西亚" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_325" defVal="马绍尔群岛" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_325" defVal="马绍尔群岛" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_326" defVal="马达加斯加" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_326" defVal="马达加斯加" fileName="wxgl"/></option>
													<option value='<emp:message key="wxgl_gzhgl_title_327" defVal="黎巴嫩" fileName="wxgl"/>'><emp:message key="wxgl_gzhgl_title_327" defVal="黎巴嫩" fileName="wxgl"/></option>
												</select>
											</dd>
										</dl>
										<dl class="dl-list" id="provinceDiv">
											<dd>
												<select id="province" name="province" class="input_bd" style="display:none;">
												</select>
											</dd>
										</dl>
										<dl class="dl-list" id="cityDiv">
											<dd>
												<select id="city" name="city" class="input_bd" style="display:none;">
												</select>
											</dd>
										</dl>
									</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="container" class="container">
					<div class="hd">
						<%if("text".equals(sendlog.getMsgType())) {%>
		                <a id="sendText" href="#1"><emp:message key="wxgl_gzhgl_title_328" defVal="文本消息" fileName="wxgl"/></a>
		                <% } else if("mpnews".equals(sendlog.getMsgType())) {%>
		                <a id="sendImgText" href="#1"><emp:message key="wxgl_gzhgl_title_329" defVal="图文消息" fileName="wxgl"/></a>
		                <%} %>
		            </div>
            		<div class="bd" style="padding:20px 0;">
            			<%if("text".equals(sendlog.getMsgType())) {%>
						<div id="sendTextDiv">
							<table>
								<tbody>
									<tr>
									<td style="vertical-align: top;text-align: right;"><emp:message key="wxgl_gzhgl_title_330" defVal="文本内容：" fileName="wxgl"/></td>
									<td style="position: relative;">
									<textarea style="width:400px;height:200px;" class="input_bd div_bd" name="msgText" rows="5" id="msgText" disabled="disabled"><%= sendlog.getSendContent() == null ? "" : sendlog.getSendContent()%></textarea>
										<b style="bottom:-15px;left:0;color:#656565;display: block;"><span id="sid">0</span>/500</b>
									</td>
								</tr>
								</tbody>
							</table>	
						</div>
						<%} %>
						<%if("mpnews".equals(sendlog.getMsgType())) {%>
						<div id="sendImgTextDiv">
							<div id="radioDiv2">
							<p style="vertical-align:top;text-align: left;padding:20px 0;"><emp:message key="wxgl_gzhgl_title_341" defVal="已选择标题为：" fileName="wxgl"/>"<%= sendlog.getSendContent() == null ? "" : sendlog.getSendContent()%>"  <emp:message key="wxgl_gzhgl_title_342" defVal="的模板作为回复的内容，点击" fileName="wxgl"/> <a onclick="javascript:toPreview('<%=sendlog.getTId()%>')" style="cursor:pointer" title="<emp:message key='wxgl_gzhgl_title_343' defVal='点击预览模板效果' fileName='wxgl'/>"><emp:message key="wxgl_button_11" defVal="预览" fileName="wxgl"/></a></p>
							</div>
						</div>
						<%} %>
					</div>
				</div>
				<div id="condition">
					<div class="hd cc">
						<dl class="dl-list">
						</dl>
							<p style='display: block;margin: 10px 25px;'>(<emp:message key="wxgl_gzhgl_title_334" defVal="日发送量：" fileName="wxgl"/><span id="tcount" style="color:#F00"><%=tcount%></span><emp:message key="wxgl_gzhgl_title_335" defVal=",本月发送量：" fileName="wxgl"/><span id="mcount" style="color:#F00"><%=mcount%></span>)</p>
							<ul style="margin-left:30px;margin-top:10px;">
								<li><emp:message key="wxgl_gzhgl_title_336" defVal="温馨提示：" fileName="wxgl"/></li>
								<li><emp:message key="wxgl_gzhgl_title_337" defVal="1.该功能暂时仅提供给已认证的微信服务号。" fileName="wxgl"/></li>
								<li><emp:message key="wxgl_gzhgl_title_338" defVal="2.每个公众帐号每天可提交群发100次（不包括在公众平台网站的提交）。" fileName="wxgl"/></li>
								<li><emp:message key="wxgl_gzhgl_title_339" defVal="3.用户每月只能接收4条群发消息（包括在公众平台网站和本平台发送），多于4条的群发将对该用户发送失败。" fileName="wxgl"/></li>
							</ul>
						</div>
				</div>
			</form>	
		</div>
		
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	    <script src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/yxgl_qunfa.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path %>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
			$(document).ready(function() {
		    	//$("#aid").isSearchSelect({'width':'145','isInput':false,'zindex':0});
		    	//选择公众帐号
				$("#aid").change(function(){
					recoverInitStatus();
					var corpCode = $("#lgcorpcode").val();
					var group = $("#groupid");
					var aid = $(this).val();
					$.get($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
						method: "getGroups",aid: aid,corpCode:corpCode,isAsync:"yes"	
					},function(data){
						if(data == "outOfLogin"){
				 			window.location.href = pathUrl + "/common/logoutEmp.html";
				 		}
						if(data!=undefined&&"error"!= data){
							var html = "";
							var options = $.parseJSON(data);
							$.each(options,function(i,n){
								n = n=='全部'?getJsLocaleMessage("common","common_whole"):n;
								html = html + "<option value='"+ i +"'>" + n + "</option>";
							});
							$("option",group).remove();
							$(group).append(html);
							$(group).find("option[value='000']").attr("selected","selected");
						}else{
							$("option",group).remove();
						}
					});

					$.get($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
						method: "getCount",aid: aid,corpCode: corpCode,isAsync:"yes",timee: new Date().getTime()	
					},function(data){
						if(data == "outOfLogin"){
				 			window.location.href = pathUrl + "/common/logoutEmp.html";
				 		}
						if(data!=undefined&&"error"!= data){
							var options = $.parseJSON(data);
							var tcount = options.tcount == undefined ? "0" : options.tcount;
							var mcount = options.mcount == undefined ? "0" : options.mcount;
							$("#tcount").text(tcount);
							$("#mcount").text(mcount);
							return false;
						}
					});
				});
				
				$("#tp").find("option[value='']").remove();
				//选择发送对象
		    	$("#tp").change(function(){
		    		var tpvalue = $("#tp").val();
		    		if(tpvalue=="0"){
		    			//选择组
		    			$("#groupDiv").show();
		    			$("#areaDiv").hide();
		    			recoverInitStatus();
		    		}else if("1" == tpvalue){
						$("#groupDiv").hide();
		    			$("#areaDiv").show();
		    			recoverInitStatus();
		    		}
		    	});
				
		    	//选择国家
		    	$("#areaid").change(function(){
		    		var areaValue = $("#areaid").val();
		    		var aid = $("#aid").val();
		    		var corpCode = $("#lgcorpcode").val();
		    		
		    		if("" == aid||null==aid){
						alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_50"));
						return false;
					}
		    		
		    		if(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_47") == areaValue){
		    			$.post($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
							method: "getAreas",corpCode:corpCode, aid: aid, tp:"province",tpvalue:areaValue 	
						},function(data){
							if(data == "outOfLogin"){
						 		window.location.href = pathUrl + "/common/logoutEmp.html";
						 	}
							if(data!=undefined&&"error"!= data){
								var html = "";
								var options = $.parseJSON(data);
								
								$.each(options,function(i,n){
									html = html + "<option value='"+ i +"'>" + n + "</option>";
								});
								
								$("#province").show().find("option").remove().andSelf().append(html);
								$("#province").find("option[value='']").attr("selected","selected");
								
							}else{
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_59"));
								$("#province").hide().find("option").remove();
							}
						});
		    		}else{
		    			$("#province").hide().find("option").remove();
		    			$("#city").hide().find("option").remove();
		    		}
		    	});
		    	
		    	//选择省份
		    	$("#province").change(function(){
		    		var province = $("#province").val();
		    		var aid = $("#aid").val();
		    		var corpCode = $("#lgcorpcode").val();
		    		
		    		if("" == aid||null==aid){
						alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_50"));
						return false;
					}

		    		if("" != province&&null!=province){
		    			$.post($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
							method: "getAreas",corpCode:corpCode, aid: aid, tp:"city",tpvalue: province	
						},function(data){
							if(data!=undefined&&"error"!= data){
								var html = "";
								var options = $.parseJSON(data);
								
								$.each(options,function(i,n){
									html = html + "<option value='"+ i +"'>" + n + "</option>";
								});
								$("#city").show().find("option").remove().andSelf().append(html);
								$("#city").find("option[value='']").attr("selected","selected");
								
							}else{
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_60"));
								$("#city").hide().find("option").remove();
							}
						});
		    		}else{
		    			$("#city").hide().find("option").remove();
		    		}
		    	});
				
				//初始化按地区选择和初始化按组选择
				function recoverInitStatus(){
					$("#groupid").find("option[value='000']").attr("selected","selected");
					$("#areaid").find("option[value='0']").attr("selected","selected");
					$("#province").hide().find("option").remove();
					$("#city").hide().find("option").remove();
				}
				
				//限制输入的字符个数
				$("#msgText").manhuaInputLetter({					       
					len : 500,//限制输入的字符个数				       
					showId : "sid", //显示剩余字符文本标签的ID
					showNum: ($("#msgText").val() == null || $("#msgText").val() ==undefined) ? 0 : $("#msgText").val().length
				});
				
				getLoginInfo("#hiddenValueDiv");
				
				function pageLoad(){
					var corpCode = $("#lgcorpcode").val();
					var group = $("#groupid");
					var aid = "<%=aid%>";
					var tp = "<%=sendlog.getTp()%>";
					//选择地区，省，城市
					var areaValue = "<%=sendlog.getAreaValue()%>";
					//初始值
					recoverInitStatus();
					//加载分组
					$.get($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
						method: "getGroups",aid: aid,corpCode:corpCode	
					},function(data){
						if(data == "outOfLogin"){
				 			window.location.href = pathUrl + "/common/logoutEmp.html";
				 		}
						if(data!=undefined&&"error"!= data){
							var html = "";
							var options = $.parseJSON(data);
							$.each(options,function(i,n){
								n = n=='全部'?getJsLocaleMessage("common","common_whole"):n;
								html = html + "<option value='"+ i +"'>" + n + "</option>";
							});
							$("option",group).remove();
							$(group).append(html);
							$(group).find("option[value='000']").attr("selected","selected");
						}else{
							$("option",group).remove();
						}
					});

					//发送次数
					$.get($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
						method: "getCount",aid: aid,corpCode: corpCode,isAsync:"yes"	
					},function(data){
						if(data == "outOfLogin"){
				 			window.location.href = pathUrl + "/common/logoutEmp.html";
				 		}
						if(data!=undefined&&"error"!= data){
							var options = $.parseJSON(data);
							var tcount = options.tcount == undefined ? "0" : options.tcount;
							var mcount = options.mcount == undefined ? "0" : options.mcount;
							$("#tcount").text(tcount);
							$("#mcount").text(mcount);
							return false;
						}
					});
					if("1" == tp&&areaValue!=null){
						var areaArr = areaValue.split(",");
						var area =  areaArr[0];
						var province = areaArr[1];
						var city = areaArr[2];
						//选择地区
						if(area){
			    			$.post($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
								method: "getAreas",corpCode:corpCode, aid: aid, tp:"province",tpvalue:area
							},function(data){
								if(data == "outOfLogin"){
							 		window.location.href = pathUrl + "/common/logoutEmp.html";
							 	}
								if(data!=undefined&&"error"!= data){
									var html = "";
									var options = $.parseJSON(data);
									
									$.each(options,function(i,n){
										html = html + "<option value='"+ i +"'>" + n + "</option>";
									});
									$('#areaid').find('option[value="'+area+'"]').attr('selected',true);
									if(area == getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_47")){
										$("#province").show().find("option").remove().andSelf().append(html);
										$("#province").find("option[value='"+ province +"']").attr("selected","selected");
									}
								}else{
									$("#province").hide().find("option").remove();
								}
							});
			    		}else{
			    			$("#province").hide().find("option").remove();
			    			$("#city").hide().find("option").remove();
			    		}
						
						//选择省
						if(province!=null&&province!=""&&province!="0"){
							$.post($("#pathUrl").val() + "/yxgl_fzqfManager.htm",{
								method: "getAreas",corpCode:corpCode, aid: aid, tp:"city",tpvalue: province	
							},function(data){
								if(data == "outOfLogin"){
							 		window.location.href = pathUrl + "/common/logoutEmp.html";
							 	}
								if(data!=undefined&&"error"!= data){
									var html = "";
									var options = $.parseJSON(data);
									
									$.each(options,function(i,n){
										html = html + "<option value='"+ i +"'>" + n + "</option>";
									});
									if(province){
										$("#city").show().find("option").remove().andSelf().append(html);
										$("#city").find("option[value='"+city+"']").attr("selected","selected");
									}
								}else{
									$("#city").hide().find("option").remove();
								}
							});
						}
					}
				}
				
				pageLoad();
				
			});
	    </script>
	</body>
</html>