<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.wymanage.vo.ASiminfoVo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" + request.getServerPort()	+ path + "/";
String iPath = request.getRequestURI().substring(0,	request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

MessageUtils messageUtils = new MessageUtils();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("gateManage");
menuCode = menuCode==null?"0-0-0":menuCode;
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
//通道名称
String gatename=request.getParameter("gatename");
//ip地址
String ipadress=request.getParameter("ipadress");
//端口号
String portnum=request.getParameter("portnum");
//SIM卡号
String phoneno=request.getParameter("phoneno");
//运营商
String simunicom=request.getParameter("simunicom");

@ SuppressWarnings("unchecked")
List<DynaBean> ipcomList=(List<DynaBean>)request.getAttribute("ipcomList");
@ SuppressWarnings("unchecked")
Map<Integer,ASiminfoVo> icomsimmap=(Map<Integer,ASiminfoVo>)request.getAttribute("icomsimmap");
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//请输入SIM卡号
String qsrsimkh = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_qsrsimkh", request);
//请输入国家
String qsrgj = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_qsrgj", request);
//确定
String qd = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_qd", request);
//取消
String qx = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_qx", request);
//修改网优通道
String xgwytd = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_xgwytd", request);
//SIM卡详情
String simkxq = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_simkxq", request);
//备注
String bz = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_bz", request);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wygl_wytdgl_wytdgl" defVal="网优通道管理" fileName="txgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=iPath %>/css/gateManage.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<style type="text/css">
		#busNameAdd,#busCode,#busNameEdit,#busCodeEdit  {
			width: 170px;
			height: 20px;
			line-height: 20px;
			padding-left: 2px;
			padding-right: 2px;
		}
		
		#busDescriptionEdit {
			width: 170px;
			font-size: 13px;
			padding-left: 2px;
			padding-right: 2px;
		}
		
		#btn {
			margin-top: 15px;
			width: 240px !important;
			text-align: right;
		}
		
		#btn input {
			padding: 2px 5px;
			font-size: 14px;
			display: inline;
			cursor: pointer;
			width: 55px !important;
			height: 23px !important;
		}
		
		.input_textarea
		{
			border: 1px solid #b3caee;
		}
		
		.a3 {
			width: 35px;
			height:21px;
			 border: 0;
			text-align: center;
			background: white;
		}
		
		.operators{
			width: 190px;
			margin-top:-22px;
			margin-right:50px;
		}
		.channel_item label.mod_t1{
			width:170px;
		}
		.channel_item {
   		 	width: 680px;
		}
		.channel_item .handle {
		    right: 1px;
		    width: 80px;
			text-align:right;
		}
		</style>
		
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<form name="pageForm" action="wy_gateManage.htm" method="post" id="pageForm">
			<div style="display:none" id="hiddenValueDiv"></div>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" >
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<% if(btnMap.get(menuCode+"-1")!=null) {  %>
					<a id="add"><emp:message key="txgl_wygl_wytdgl_xj" defVal="新建" fileName="txgl"/></a>
					<% } %>
				</div>
				<div id="condition">
					<table>
						<tr>
							<td width="100px">
								<span><emp:message key="txgl_wygl_wytdgl_tdmc" defVal="通道名称" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</td>
							<td>
								<label>
									<input type="text" name="gatename" id="gatename" value="<%=gatename==null?"":gatename %>" />
								</label>
							</td>
							<td width="100px">
								<span><emp:message key="txgl_wygl_wytdgl_yyswgip" defVal="运营商网关IP" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</td>
							<td>
								<label>
									<input type="text" name="ipadress" id="ipadress" value="<%=ipadress==null?"":ipadress %>" />
								</label>
							</td>
							<td width="100px">
								<span><emp:message key="txgl_wygl_wytdgl_yyswgdk" defVal="运营商网关端口" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</td>
							<td>
								<label>
									<input type="text" name="portnum" id="portnum" value="<%=portnum==null?"":portnum %>" maxlength="5" />
								</label>
							</td>
							<td class="tdSer">
												<center><a id="search"></a></center>
							</td>
						</tr>
						<tr>
						<td width="100px">
								<span><emp:message key="txgl_wygl_wytdgl_simkh" defVal="SIM卡号" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</td>
							<td>
								<label>
									<input type="text" name="phoneno" id="phoneno" value="<%=phoneno==null?"":phoneno %>"  onkeyup="javascript:phoneInputCtrl($(this));" onblur="javascript:phoneInputCtrl($(this));" maxlength="21"  />
								</label>
							</td>
							<td width="100px">
								<span><emp:message key="txgl_wygl_wytdgl_yys" defVal="运营商" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</td>
							<td>
								<label>
								<select name="simunicom" id="simunicom" style="width: 155px;">
									<option value="" ><emp:message key="txgl_wygl_wytdgl_qb" defVal="全部" fileName="txgl"/></option>
									<option value="0" <%if("0".equals(simunicom)){ %> selected="selected" <%} %>><emp:message key="txgl_wygl_wytdgl_yd" defVal="移动" fileName="txgl"/></option>
									<option value="1" <%if("1".equals(simunicom)){ %> selected="selected" <%} %>><emp:message key="txgl_wygl_wytdgl_lt" defVal="联通" fileName="txgl"/></option>
									<option value="21" <%if("21".equals(simunicom)){ %> selected="selected" <%} %>><emp:message key="txgl_wygl_wytdgl_dx" defVal="电信" fileName="txgl"/></option>
									<option value="5" <%if("5".equals(simunicom)){ %> selected="selected" <%} %>><emp:message key="txgl_wygl_wytdgl_gj" defVal="国际" fileName="txgl"/></option>
								</select>
								</label>
							</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

					</table>
				</div>
				<table id="content">
					<thead>
						<tr >
							<th style="width: 150px;">
								<emp:message key="txgl_wygl_wytdgl_tdmc" defVal="通道名称" fileName="txgl"/>
							</th>
							<th style="width: 150px;">
								<emp:message key="txgl_wygl_wytdgl_empwgipjdk" defVal="EMP网关IP及端口" fileName="txgl"/>
							</th>
							<%--<th style="width: 100px;">
								EMP网关端口
							</th>
							--%>
							<th style="width: 150px;">
								<emp:message key="txgl_wygl_wytdgl_yyswgipjdk" defVal="运营商网关IP及端口" fileName="txgl"/>
							</th>
							<%--<th style="width: 100px;">
								运营商网关端口
							</th>
							--%>
							<th style="width: 290px;">
								<emp:message key="txgl_wygl_wytdgl_simkxq" defVal="SIM卡详情" fileName="txgl"/>
							</th>
							<th  style="width: 120px;">
								<emp:message key="txgl_wygl_wytdgl_dxqm" defVal="短信签名" fileName="txgl"/>
							</th>
							<th>
								<emp:message key="txgl_wygl_wytdgl_bz" defVal="备注" fileName="txgl"/>
							</th>
							<th  style="width: 140px;">
								<emp:message key="txgl_wygl_wytdgl_cj" defVal="创建" fileName="txgl"/>/<emp:message key="txgl_wygl_wytdgl_xgsj" defVal="修改时间" fileName="txgl"/>
							</th>
							<%if(btnMap.get(menuCode+"-2") != null)  {  %>
							<th style="width: 40px;">
								<emp:message key="txgl_wygl_wytdgl_cz" defVal="操作" fileName="txgl"/>
							</th>
							<%} %>
						</tr>
					</thead>
					<tbody>
						<%
						if(ipcomList != null&&ipcomList.size()>0)
						{
							for(int i=0;i<ipcomList.size();i++)
							{
								DynaBean dybipcom = ipcomList.get(i); 
						%>
						<tr>
							<td><xmp><%=dybipcom.get("gatename")!=null?dybipcom.get("gatename").toString():""%></xmp></td>
							<td class="textalign"><xmp><%=dybipcom.get("ptip")!=null?dybipcom.get("ptip").toString():""%>:<%=dybipcom.get("ptport")!=null?dybipcom.get("ptport").toString():""%></xmp></td>
							<td class="textalign"><xmp><%=dybipcom.get("ip")!=null?dybipcom.get("ip").toString():""%>:<%=dybipcom.get("port")!=null?dybipcom.get("port").toString():""%></xmp></td>
							<td class="textalign" >
							<%
								Integer gateid =dybipcom.get("gateid")!=null?Integer.parseInt(dybipcom.get("gateid").toString()):0;
								List<ASiminfoVo> asims=(List<ASiminfoVo>)icomsimmap.get(gateid);
								if(asims!=null&&asims.size()>0){
									String threerow="";
									String allrow="";
									int row=0;
									for(ASiminfoVo simvo:asims){
										row++;
										if(row<4){
											//threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											
											if(StaticValue.ZH_HK.equals(langName)){
												if("移动".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"mobile"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("联通".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"unicom"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("电信".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"telecommunications"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("国际".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"International"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else{
													threerow=threerow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}	
											
											}else if(StaticValue.ZH_TW.equals(langName)){
												if("移动".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"移動"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("联通".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"聯通"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("电信".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"電信"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("国际".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"國際"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else{
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}	
											}else{
												if("移动".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("联通".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("电信".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else if("国际".equals(simvo.getUnicomname())){
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}else{
													threerow=threerow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
												}	
											}
										}
										//allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
										
										if(StaticValue.ZH_HK.equals(langName)){
											if("移动".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"mobile"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("联通".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"unicom"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("电信".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"telecommunications"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("国际".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"International"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else{
												allrow=allrow+"&nbsp;&nbsp;card"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}	
										}else if(StaticValue.ZH_TW.equals(langName)){
											if("移动".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"移動"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("联通".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"聯通"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("电信".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"電信"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("国际".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+"國際"+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else{
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}	
										}else{
											if("移动".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("联通".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("电信".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else if("国际".equals(simvo.getUnicomname())){
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}else{
												allrow=allrow+"&nbsp;&nbsp;卡"+simvo.getSimno()+":&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getPhoneno()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getUnicomname()+"&nbsp;&nbsp;&nbsp;&nbsp;"+simvo.getAreaname()+"</br>";
											}	
										}
										
									}
									if(!"".equals(threerow)&&threerow!=null&&!"".equals(allrow)&&allrow!=null){
										%>
											<%=threerow%>
										<%
										if(allrow.length()>threerow.length()){
											%>
											<a style="text-indent: 8px;" onclick="javascript:modify(this)">
											<label  style="display:none"><xmp><%=allrow %></xmp></label>
											<xmp>------</xmp> 
											</a> 
											<%
										}	
									}
								}
										 
							%>
										
							</td>
							<td class="textalign"><xmp><%=dybipcom.get("corpsign")!=null?dybipcom.get("corpsign").toString():""%></xmp></td>
							<td class="textalign">
							<a onclick="javascript:opencommon(this)">
										<%
											String xmessage=dybipcom.get("common")!=null?dybipcom.get("common").toString():"";
										%>
								  <label style="display:none"><xmp><%=xmessage %></xmp></label>
											<xmp><%=xmessage.length()>12?xmessage.substring(0,12)+"...":xmessage %></xmp>
								  </a> 
							</td>
							<td><xmp><%=dybipcom.get("createtime")!=null?df.format(Timestamp.valueOf(dybipcom.get("createtime").toString())):"--"%> </xmp></td>
							<%  if(btnMap.get(menuCode+"-2")!=null)  {  %>
							<td>
								<a href="javascript:showEditGateManage(<%=dybipcom.get("id")!=null?dybipcom.get("id").toString():""%>)"><emp:message key="txgl_wygl_wytdgl_xg" defVal="修改" fileName="txgl"/></a>
							</td>
							<%} %>
						</tr>
						<%
						 	}
						 }else{
						%>
									<tr><td colspan="10"><emp:message key="txgl_wygl_wytdgl_wjl" defVal="无记录" fileName="txgl"/></td></tr>
						<%} %>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="10">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
			<div id="addDiv" style="display:none">
				<div class="channel_manage nano">
					
					<div class="nano-content">
					
						<div class="channel_item clearfix">
							<label class="mod_t1"><emp:message key="txgl_wygl_wytdgl_tdmc" defVal="通道名称" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></label>
							<input type="text" class="input_bd div_bd" name="agatename" id="agatename" maxlength="16"/>
							<span class="channelTip"><em>*&nbsp;</em></span>
						</div>
						<div class="channel_item clearfix" >
							<div style="width:170px;float:left;margin-right: 5px;">
								<span class="mod_t1"><emp:message key="txgl_wygl_wytdgl_empwgipjdk" defVal="EMP网关IP及端口" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</div>
								<%--<input type="hidden"  class="input_bd div_bd" name="aipadress" id="aipadress"  maxlength="16"/>
								--%>
							<div id="ainneripadress"	class="div_bd" style="width:172px; background: white;font-size: 8pt;float: left;_margin-left:2px;">
								<input type=text name=ip1 id="ip1" maxlength=3 class=a3
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip2 id="ip2"  maxlength=3 class=a3
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip3  id="ip3" maxlength=3 class=a3 
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip4 id="ip4" maxlength=3 class=a3
								onkeyup="mask(this,event)" onbeforepaste=mask_c()> 
							</div>
							<div style="float:left;">
								<span style="padding-left: 2px;padding-right:2px;"><strong>:</strong></span>
							</div>
							<div style="float:left;">
								<input type="text"  class="input_bd div_bd" style="width:70px;height: 21px;" name="ainnerportnum" id="ainnerportnum" onkeyup= "if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')" onblur="validatePort(this)" placeholder="<emp:message key="txgl_wygl_wytdgl_dkh" defVal="端口号" fileName="txgl"/>" maxlength="5"/>
							</div>
							<div style="width:200px;float:left;padding-left:4px;">
								<span class="channelTip zhu"><em>*&nbsp;</em><emp:message key="txgl_wygl_wytdgl_empwgszfwqipjtxdk" defVal="EMP网关所在服务器IP及通讯端口" fileName="txgl"/></span>
							</div>
						</div>
						<%--
						<div class="channel_item clearfix">
							<label class="mod_t1">EMP网关端口：</label>
							<input type="text"  class="input_bd div_bd" name="ainnerportnum" id="ainnerportnum"  maxlength="16"/>
							<span class="channelTip"><em>*</em>EMP网关通讯端口</span>
						</div>
						--%>
						<div class="channel_item clearfix" >
							<div style="width:170px;float:left;margin-right: 5px;">
								<span class="mod_t1"><emp:message key="txgl_wygl_wytdgl_yyswgipjdk" defVal="运营商网关IP及端口" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</div>
								<%--<input type="hidden"  class="input_bd div_bd" name="aipadress" id="aipadress"  maxlength="16"/>
								--%>
							<div id="aipadress" class="div_bd"	style="width:172px; background: white;font-size: 8pt;float: left;_margin-left:2px;">
								<input type=text name=ip5 id="ip5" maxlength=3 class=a3
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip6 id="ip6"  maxlength=3 class=a3
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip7  id="ip7" maxlength=3 class=a3 
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip8 id="ip8" maxlength=3 class=a3
								onkeyup="mask(this,event)" onbeforepaste=mask_c()> 
							</div>
							<div style="float:left;padding-right:2px;">
								<span style="padding-left: 2px;"><strong>:</strong></span>
							</div>
							<div style="float:left;">
								<input type="text"  class="input_bd div_bd"  style="width:70px;height: 21px;" name="aportnum" id="aportnum" onkeyup= "if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')" onblur="validatePort(this)" placeholder="<emp:message key="txgl_wygl_wytdgl_dkh" defVal="端口号" fileName="txgl"/>" maxlength="5"/>
							</div>	
							<div style="width:200px;float:left;padding-left:4px;">
								<span class="channelTip zhu"><em>*</em><emp:message key="txgl_wygl_wytdgl_ipcomsbdipdzjtxdk" defVal="IPCOM设备的IP地址及通讯端口" fileName="txgl"/></span>
							</div>
						</div>
						<%--
						<div class="channel_item clearfix">
							<label class="mod_t1">运营商网关端口：</label>
							<input type="text"  class="input_bd div_bd" name="aportnum" id="aportnum"  maxlength="16"/>
							<span class="channelTip"><em>*</em>IPCOM设备的监听端口号</span>
						</div>
						--%>
						<div class="channel_item clearfix">
							<div style="width:170px;float:left;margin-right: 5px;">
								<span class="mod_t1"><emp:message key="txgl_wygl_wytdgl_dxqm" defVal="短信签名" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></span>
							</div>
							<div style="float:left;">
								<input type="text"  class="input_bd div_bd" name="corpsign" id="corpsign"  maxlength="10" />
							</div>
							<div style="width:260px;float:left;padding-left:4px;">
								<span class="channelTip zhu">   <emp:message key="txgl_wygl_wytdgl_gsbxbhbjzkhrmwkj" defVal="格式必须包含半角中括号，如：[梦网科技]" fileName="txgl"/></span>
							</div>
						</div>
						
						<div class="channel_item sim_detail clearfix" data="1">
							<label class="mod_t1"><emp:message key="txgl_wygl_wytdgl_simk" defVal="SIM卡" fileName="txgl"/>1<emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></label>
							<input type="text" name="sim_number" class="input_bd div_bd" onblur="phoneInputCtrl($(this));" onkeyup="phoneInputCtrl($(this));" maxlength="21" placeholder="<%=qsrsimkh %>"/><em style="color: red;">*</em>
							<input type="text"  class="input_bd div_bd" style="width:95px" name="country"  maxlength="16" placeholder="<%=qsrgj %>"/>
							<div class="operators" >
							<div class="choose_operators">
								<select name="sim_server">
									<option value="0"><emp:message key="txgl_wygl_wytdgl_yd" defVal="移动" fileName="txgl"/></option>
									<option value="1"><emp:message key="txgl_wygl_wytdgl_lt" defVal="联通" fileName="txgl"/></option>
									<option value="21"><emp:message key="txgl_wygl_wytdgl_dx" defVal="电信" fileName="txgl"/></option>
									<option value="5"><emp:message key="txgl_wygl_wytdgl_gj" defVal="国际" fileName="txgl"/></option>
								</select>
							</div>
							<span class="handle">
								<a class="add_handle" href="#"><emp:message key="txgl_wygl_wytdgl_tj" defVal="添加" fileName="txgl"/></a>
						  	  	|
						  	  	<a class="remove_handle" href="#"><emp:message key="txgl_wygl_wytdgl_sc" defVal="删除" fileName="txgl"/></a>
						  	  	<br/>
							</span>
							
							</div>
							
							
						</div>
						
						<div class="channel_item mb20 bz" style="position:static;">
							<label class="mod_t1"><emp:message key="txgl_wygl_wytdgl_bz" defVal="备注" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></label>
							<textarea class="input_bd div_bd"  name="acommon" id="acommon" onblur="if(this.value.length > 100)this.value=this.value.substring(0,100)" onkeyup="if(this.value.length > 100)this.value=this.value.substring(0,100)"></textarea>
						</div>
						<div class="fr" align="center">
							<input class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript: addGateManage(arguments[0]);"/>
							<input class="btnClass6" type="button" value="<%=qx %>" onclick="javascript:doCancel('#addDiv');"/>
							<br/>
						</div>
					</div>
					</div>
				
				
			</div>
			
			
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>

			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div id="templates" style="display:none;">
			<div class="channel_item sim_detail clearfix" data="1">
							<label class="mod_t1"><emp:message key="txgl_wygl_wytdgl_simk1" defVal="SIM卡1" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></label>
							<input type="text" name="sim_number" class="input_bd div_bd" value=""  onblur="phoneInputCtrl($(this));" onkeyup="phoneInputCtrl($(this));"  maxlength="21" placeholder="<%=qsrsimkh %>"/><em style="color: red;">*</em>
							<input type="text"  class="input_bd div_bd" style="width:95px" name="country"  maxlength="16" placeholder="<%=qsrgj %>"/>
							<div class="operators">
							<div class="choose_operators">
								<select name="sim_server">
									<option value="0"><emp:message key="txgl_wygl_wytdgl_yd" defVal="移动" fileName="txgl"/></option>
									<option value="1"><emp:message key="txgl_wygl_wytdgl_lt" defVal="联通" fileName="txgl"/></option>
									<option value="21"><emp:message key="txgl_wygl_wytdgl_dx" defVal="电信" fileName="txgl"/></option>
									<option value="5"><emp:message key="txgl_wygl_wytdgl_gj" defVal="国际" fileName="txgl"/></option>
								</select>
							</div>
							<span class="handle">
								<a class="add_handle" href="#"><emp:message key="txgl_wygl_wytdgl_tj" defVal="添加" fileName="txgl"/></a>
						  	  	|
						  	  	<a class="remove_handle" href="#"><emp:message key="txgl_wygl_wytdgl_sc" defVal="删除" fileName="txgl"/></a>
						  	  	<br/>
							</span>
							
							</div>
							
							
						</div>
		
		</div>
		<div id="editWyGateDiv" title="<%=xgwytd %>" style="display:none;">
			<iframe id="editWyGateFrame" name="editWyGateFrame" style="width:630px;height:380px;border: 0;" marginwidth="0"  frameborder="no" style="overflow:scroll;overflow-y:hidden"></iframe>
							
		</div>
		<div id="modify" title="<%=simkxq %>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<label id="msg" style="width:100%;height:100%"></label>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			
			
			 <div id="opencommon" title="<%=bz %>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="240px">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<span style="display:block;width:240px;"><label id="commonstr" style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			
			<div id="show"  style="padding:15px;width:500px;height:200px;display:none;word-wrap: break-word;word-break:break-all;">
				<h3 style="text-align:center;margin-bottom:10px;"><emp:message key="txgl_wygl_wytdgl_wytdcjcg" defVal="网优通道创建成功!" fileName="txgl"/></h3>
				<table width="100%">
					<thead>
						<tr>
							<th width="40%" style="text-align: right;"><emp:message key="txgl_wygl_wytdgl_tdmc" defVal="通道名称" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></th>
							<td>
								<label id="showgatename" style="font-weight: bold;text-align: left;text-indent: 1em;"></label>
							</td>
						</tr>
						<tr>
							<th width="40%" style="text-align: right;"><emp:message key="txgl_wygl_wytdgl_tdhm" defVal="通道号码" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></th>
							<td>
								<label id="showgatenum" style="font-weight: bold;text-align: left;text-indent: 1em;"></label>
							</td>
						</tr>
						<tr>
							<th width="40%" style="text-align: right;"><emp:message key="txgl_wygl_wytdgl_tdzh" defVal="通道账号" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></th>
							<td>
								<label id="showgateusername" style="font-weight: bold;text-align: left;text-indent: 1em;"></label>
							</td>
						</tr>
						<tr>
							<th width="40%" style="text-align: right;"><emp:message key="txgl_wygl_wytdgl_dxqm" defVal="短信签名" fileName="txgl"/><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"/></th>
							<td>
								<label id="showcornsign" style="font-weight: bold;text-align: left;text-indent: 1em;"></label>
							</td>
						</tr>
						<tr>
							<td  colspan="2" style="text-indent: 2em;"><emp:message key="txgl_wygl_wytdgl_gtdxgxxjtbztxgl" defVal="该通道相关信息将同步至[通信管理]-[通道管理]及[通信管理]-[通道账户管理]。" fileName="txgl"/></td>
						</tr>
						<tr>
							<td colspan="2" style="text-align:center; ">
								<input class="btnClass5" type="button" value="<%=qd %>" onclick="goHome(this)" style="background-position: 0% 0px;"><br/>
							</td>
						</tr>
					</thead>
				</table>
			</div>
			

		<div class="clear"></div>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
			type="text/javascript"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=iPath %>/js/gateManage.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=iPath %>/js/ip.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=iPath %>/js/json2.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>

		<script type="text/javascript">
		$(document).ready(function() {
			$('.channel_manage input[placeholder]').each(function(){
				$(this).placeholder({'labelMode':true});	
			})
			$('.channel_manage').click(function(){
				$('.channel_manage input[name="sim_number"]').removeClass('no-unique');
			})
			$("#simunicom").isSearchSelect({'width':'152','isInput':false,'zindex':0});
			var add_handle=$('.add_handle'),
				remove_handle=$('.remove_handle'),
				sim_template=$('.sim_template:first'),
				sim_detail=$('.sim_detail');
			createOpera();
			add_handle.live('click',function(e){
				e.preventDefault();
				var clone=$('#templates').children().clone();
				var index=Number($('.channel_manage  .sim_detail:last').attr('data'));
				var newIndex=++index;
				var zIndex=$('.channel_manage .operators:last').css('zIndex');
				clone.attr('data',newIndex);
				clone.find('.operators').css({'z-index':--zIndex});
				//clone.find('label').html('SIM卡'+newIndex+'：');
				<%if(StaticValue.ZH_HK.equals(langName)){%>
					clone.find('label').html('SIM Card'+newIndex+'：');
				<%}else{%>
					clone.find('label').html('SIM卡'+newIndex+'：');
				<%}%>
				clone.insertAfter('.sim_detail:last');
				
				clone.find('.c_selectBox').remove();
				clone.find('select').isSearchSelect({'width':'90','isInput':false,'zindex':1});
				clone.find('input[placeholder]').placeholder({'labelMode':true});	
				
			});
			
			remove_handle.live('click',function(e){
				e.preventDefault();
				var oSim=$('.channel_manage .sim_detail');
				var len=oSim.size();
				var fa=$(this).parent().parent().parent();
				if(len>1){
					fa.remove();
					sortHandle();
				}else{
					//alert('至少保留一组SIM卡');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_1"));
				}
				
			})
			
			function createOpera(){
				$('.channel_item select').isSearchSelect({'width':'90','isInput':false,'zindex':1});
			}
			
			function reconstruct(o){
				if(o.value==1 && $(o.box.self).parent().parent().find('select[name="sim_server"]').size()==0){
					 var operaClone=operators_template.children().clone();
					 operaClone.insertAfter($(o.box.self).parent());
					$(o.box.self).parent().parent().find('select[name="sim_server"]').isSearchSelect({'width':'60','isInput':false,'zindex':0});
				 }else if(o.value!=1){
					 $(o.box.self).parent().parent().find('.choose_operators').remove();
				 }
			}
			 
			getLoginInfo("#hiddenValueDiv");
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

			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);

			$('#search').click(function(){submitForm();});
			
			var text_2 = getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_2")
			$("#addDiv").dialog({
				autoOpen: false,
			    width:720,
			    height:400,
			    //title:'添加网优通道',
			    title:text_2,
			    modal:true,
			    resizable:false,
			    open:function(){
					//$(".nano").nanoScroller();
			    },
			    close:function(){
			    	//black();
			    	$('#agatename').val("");
					//$('#ip1').val("");
					//$('#ip2').val("");
					//$('#ip3').val("");
					//$('#ip4').val("");
					//$('#ip5').val("");
					//$('#ip6').val("");
					//$('#ip7').val("");
					//$('#ip8').val("");
					$('.a3').val("");
					$('#aportnum').val("");
					$('#ainnerportnum').val("");
					$('#corpsign').val("");
					$("input[name='country']").val("");
					$("input[name='sim_number']").val("");
					$('#acommon').val("");
					$('#addDiv .sim_detail').not(':first').remove();
			    }
			 });
			 $('#add').click(function(){
				$("#addDiv").dialog("open");
			});
			$("#editWyGateDiv").dialog({
				autoOpen: false,
				 width:630,
				resizable:false,
				modal: true,
				open:function(){
					
				}
			});
			
			
		});


		//打开修改通道页面
		function showEditGateManage(ipcomid)
		{
			$("#editWyGateFrame").attr("src","<%=path %>/wy_gateManage.htm?method=doEdit&ipcomid="+ipcomid);
			$("#editWyGateDiv").dialog("open");
		}
		
		
		function modify(t)
			{
			$('#modify').dialog({
				autoOpen: false,
				width:480,
			    height:200
			});
			$("#msg").empty();
			$("#msg").html($(t).children("label").children("xmp").html());
			$('#modify').dialog('open');
			}
			
		function opencommon(t)
			{
			$('#opencommon').dialog({
				autoOpen: false,
				width:308,
			    height:200
			});
			$("#commonstr").empty();
			$("#commonstr").html($(t).children("label").children("xmp").html());
			$('#opencommon').dialog('open');
			}
		function doCancel(obj){
			$(obj).dialog('close');
		}
					
		
		</script>
	</body>
</html>
