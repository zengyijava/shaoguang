<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.ydyw.LfBusTaoCan"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.mobilebus.constant.MobileBusStaticValue"%>
<%@ page import="com.montnets.emp.tczl.vo.LfTaocanCmdVo" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
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
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfTaocanCmdVo> taocanVoList = (List<LfTaocanCmdVo>) request.getAttribute("taocanVoList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	LfTaocanCmdVo taocanVo = (LfTaocanCmdVo)request.getAttribute("taocanVo");
	@ SuppressWarnings("unchecked")
	LfTaocanCmdVo taocanVotemp = (LfTaocanCmdVo)request.getAttribute("taocanVotemp");
	String allspidTemp = null;
	String allCmdTemp = null;
	if(taocanVotemp!=null){
		allCmdTemp = taocanVotemp.getStructcode();
		allspidTemp = taocanVotemp.getSpId().toString();
	}
	//String userId=request.getParameter("userId");
	String taocanName = taocanVo.getTaocanName()!=null?taocanVo.getTaocanName():null;
	String taocanCode = taocanVo.getTaocanCode()!=null?taocanVo.getTaocanCode():null;
	String structcode = taocanVo.getStructcode()!=null?taocanVo.getStructcode():null;
	String structType = taocanVo.getStructType()!=null?taocanVo.getStructType().toString():null;
	String spUser = taocanVo.getSpUser()!=null?taocanVo.getSpUser():null;
	String name = taocanVo.getName()!=null?taocanVo.getName():null;
	String taocanType = taocanVo.getTaocanType()!=null?taocanVo.getTaocanType().toString():null;
	String recvtime = taocanVo.getEndSubmitTime()!=null?taocanVo.getEndSubmitTime():null;
	String sendtime = taocanVo.getStartSubmitTime()!=null?taocanVo.getStartSubmitTime():null;
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("pkgInstructionSet");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("spUserList");
	@ SuppressWarnings("unchecked")
	List<LfBusTaoCan> busTaocanList = (List<LfBusTaoCan>)request.getAttribute("busTaocanList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp"%>
		<title>业务类型</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydyw/tczl/css/pkgInstructionSet_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}else{%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydyw/tczl/css/pkgInstructionSet.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style type="text/css">
		#busNameAdd,#busCode,#busNameEdit,#busCodeEdit  {
			width: 170px;
			height: 20px;
			line-height: 20px;
			padding-left: 2px;
			padding-right: 2px;
		}
		
		#busDescriptionAdd,#busDescriptionEdit {
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
		ul.c_result li{
			word-wrap: break-word;
		} 
		
		#condition .c_selectBox {
			width:208px!important;
		}
		#condition .c_selectBox ul {
			width:208px!important;
		}
		#condition .c_selectBox ul li{
			width:208px!important;
		}
		
		</style>
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>

			<%=ViewParams.getPosition(empLangName,menuCode) %>
			<form name="pageForm" action="ydyw_pkgInstructionSet.htm" method="post" id="pageForm">
			<div style="display:none" id="hiddenValueDiv"></div>
			<input type="hidden" value="" id="busCodeTemp"></input>
			<input type="hidden" value="" id="menuCodeTemp"></input>
			<input type="hidden" value="<%=allspidTemp==null?"":allspidTemp %>" id="allspidTemp"></input>
			<input type="hidden" value="<%=allCmdTemp==null?"":allCmdTemp %>" id="allcmdTemp"></input>
			<input type="hidden" value="<%=MobileBusStaticValue.orderDelimiter %>" id="splitStr"></input>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" >
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<% if(btnMap.get(menuCode+"-1")!=null) {  %>
					<a id="add"><emp:message key="common_establish" fileName="common" defVal="新建"></emp:message></a>
					<% } %>
					<% if(btnMap.get(menuCode+"-4")!=null) {  %>
					<a id="bind"><emp:message key="ydyw_ywgl_tczlsz_btn_1" fileName="ydyw" defVal="全局设置"></emp:message></a>
<%--<a id="setCmb" >全局设置</a>--%>
					<% } %>
				</div>
				<div id="condition">
					<table>
						<tr>
							<td>
								<emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称：" fileName="ydyw"></emp:message>
							</td>
							<td>
								<label>
									<input type="text" style="width: 178px;" name="taocanName" id="taocanName" value="<%=taocanName==null?"":taocanName %>" maxlength="16" />
								</label>
							</td>
							<td>
								<emp:message key="ydyw_ywgl_ywbgl_text_35" defVal="套餐编号：" fileName="ydyw"></emp:message>
							</td>
							<td>
								<label>
									<input type="text" style="width: 178px;" name="taocanCode" id="taocanCode" value="<%=taocanCode==null?"":taocanCode %>" maxlength="16" />
								</label>
							</td>
							<td>
								<emp:message key="ydyw_ywgl_tczlsz_text_zl_p" defVal="指令：" fileName="ydyw"></emp:message>
									</td>
									<td>
								<label>
									<input type="text" style="width: 178px;" name="structcode" id="structcode" value="<%=structcode==null?"":structcode %>" maxlength="16" />
								</label>
							</td>
							<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
						</tr>
						<tr>
							<td>
								<emp:message key="ydyw_ywgl_tczlsz_text_1_p" defVal="SP账号：" fileName="ydyw"></emp:message>
							</td>
							<td>
							<select id="Spid" name="Spid" style="width:182px;" class="input_bd">
									   <option value=""><emp:message key="common_whole" defVal="全部" fileName="common"></emp:message></option>
									   <%
												if (userList != null && userList.size() > 0){
													for(Userdata vo :userList){
										%>
										  <option value="<%=vo.getUid() %>" <%=vo.getUid().toString().equals(spUser)?"selected":"" %> ><%=vo.getUserId()%><%=vo.getAccouttype()==1 ? ":("+ MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_6", (HttpServletRequest) pageContext.getRequest())+")" : ":("+MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_7",(HttpServletRequest) pageContext.getRequest())+")" %> </option>
											<%
											  }
											 } 
											%>
									   
									</select>
							</td>
							<td>
								<emp:message key="ydyw_ywgl_tczlsz_text_2_p" defVal="指令类型：" fileName="ydyw"></emp:message>
							</td>
							<td>
								<select id="structType" name="structType" style="width: 182px;" isInput="false">
											<option value=""><emp:message key="common_whole" defVal="全部" fileName="common"></emp:message></option>
											<option value="0"  <%="0".equals(structType)?"selected":"" %>><emp:message key="ydyw_ywgl_tczlsz_text_3" defVal="订购指令" fileName="ydyw"></emp:message></option>
											<option value="1"  <%="1".equals(structType)?"selected":"" %>><emp:message key="ydyw_ywgl_tczlsz_text_4" defVal="退订指令" fileName="ydyw"></emp:message></option>
										</select>
							</td>
							<td>
								<emp:message key="ydyw_ywgl_ywbgl_text_24" defVal="操作员：" fileName="ydyw"></emp:message>
									</td>
									<td>
										<label>
										<input type="text" style="width: 178px;" name="name" id="name" value="<%=name==null?"":name %>" maxlength="16" />
								</label>
									</td>
									<td>
									</td>
						</tr>
						<tr>
							<td>
								<emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型：" fileName="ydyw"></emp:message>
							</td>
							<td class="condi_f_l">
									<select id="taocanType" name="taocanType" style="width: 182px;" isInput="false">
											<option value=""><emp:message key="common_whole" defVal="全部" fileName="common"></emp:message></option>
											<option value="1"  <%="1".equals(taocanType)?"selected":"" %>><emp:message key="ydyw_ywgl_tczlsz_text_5" defVal="VIP免费" fileName="ydyw"></emp:message></option>
											<option value="2"  <%="2".equals(taocanType)?"selected":"" %>><emp:message key="ydyw_ywgl_tczlsz_text_by" defVal="包月" fileName="ydyw"></emp:message></option>
											<option value="3"  <%="3".equals(taocanType)?"selected":"" %>><emp:message key="ydyw_ywgl_tczlsz_text_bj" defVal="包季" fileName="ydyw"></emp:message></option>
											<option value="4"  <%="4".equals(taocanType)?"selected":"" %>><emp:message key="ydyw_ywgl_tczlsz_text_bn" defVal="包年" fileName="ydyw"></emp:message></option>
										</select>												
											
							</td>
							<td>
								<emp:message key="ydyw_ywgl_ywbgl_text_28" defVal="创建时间：" fileName="ydyw"></emp:message>
									</td>
									<td>
										<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime">
									</td>
									<td align="left">
								<emp:message key="common_to" defVal="至：" fileName="common"></emp:message>
									</td>
									<td>
										<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime">
									</td>
											<td>
											</td>
						</tr>
						
					</table>
				</div>
				<table id="content">
					<thead>
						<tr >
							<th width="11%">
								<emp:message key="ydyw_ywgl_ywbgl_text_32" defVal="套餐名称" fileName="ydyw"></emp:message>
							</th>
							<th width="9%">
								<emp:message key="ydyw_ywgl_ywbgl_text_34" defVal="套餐编号" fileName="ydyw"></emp:message>
							</th>
							<th width="6%">
								<emp:message key="ydyw_ywgl_ywbgl_text_36" defVal="计费类型" fileName="ydyw"></emp:message>
							</th>
							<th width="8%">
								<emp:message key="ydyw_ywgl_tczlsz_text_2" defVal="指令类型" fileName="ydyw"></emp:message>
							</th>
							<th width="6%">
								<emp:message key="ydyw_ywgl_tczlsz_text_zl" defVal="指令" fileName="ydyw"></emp:message>
							</th>
							<th width="6%">
								<emp:message key="ydyw_ywgl_tczlsz_text_1" defVal="SP账号" fileName="ydyw"></emp:message>
							</th>
							<th width="12%">
								<emp:message key="ydyw_ywgl_ywbgl_text_27" defVal="创建时间" fileName="ydyw"></emp:message>
							</th>
							<th width="12%">
								<emp:message key="ydyw_ywgl_ywbgl_text_29" defVal="修改时间" fileName="ydyw"></emp:message>
							</th>
							<th width="10%">
								<emp:message key="ydyw_ywgl_ywbgl_text_25" defVal="机构" fileName="ydyw"></emp:message>
							</th>
							<th width="12%">
								<emp:message key="ydyw_ywgl_ywbgl_text_23" defVal="操作员" fileName="ydyw"></emp:message>
							</th>
							<%if(btnMap.get(menuCode+"-3") != null || btnMap.get(menuCode+"-2") != null)  {  %>
							<th <%if(btnMap.get(menuCode+"-3") != null && btnMap.get(menuCode+"-2") != null)  {  out.print(" colspan=2");}%> width="8%">
								<emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
							</th>
							<%} %>
						</tr>
					</thead>
					<tbody>
						<%
						if(taocanVoList != null && taocanVoList.size()>0)
						{
							for(int i=0;i<taocanVoList.size();i++)
							{
								LfTaocanCmdVo taocanCmd = taocanVoList.get(i); 
						%>
						<tr>
							<td class="textalign" >
							<label id="lbName<%=taocanCmd.getId() %>"><xmp><%=taocanCmd.getTaocanName()%></xmp></label>
							</td>
							<td>
							<input type="hidden" id="tcCode<%=taocanCmd.getId() %>" value="<%=taocanCmd.getTaocanName()+'('+taocanCmd.getTaocanCode()+')'%>" />
							<label id="lbCode<%=taocanCmd.getId() %>"><%=taocanCmd.getTaocanCode()%></label>
							</td>
							<td>
							<input type="hidden" id="tcType<%=taocanCmd.getId() %>" value="<%=taocanCmd.getTaocanType()%>" />
							<%
								String s = "";
								boolean flag = false;
								 if (taocanCmd.getTaocanType()== 1)
								 {
								     s = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_6", (HttpServletRequest) pageContext.getRequest());
								     flag = true;
							     }else if(taocanCmd.getTaocanType()== 2)
								 {
								     s = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_by", (HttpServletRequest) pageContext.getRequest());
								     flag = true;
							     }else if(taocanCmd.getTaocanType()== 3)
								 {
								     s = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_bj", (HttpServletRequest) pageContext.getRequest());
								     flag = true;
							     }else if(taocanCmd.getTaocanType()== 4)
								 {
								     s = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_bn", (HttpServletRequest) pageContext.getRequest());
								     flag = true;
							     }
							%>
							<%
								if(flag){
									out.print(s);
								}
							%>
							</td>
							<td>
							<input type="hidden" id="structType<%=taocanCmd.getId() %>" value="<%=taocanCmd.getStructType()%>" />
							<%
								String structTypeStr = "";
								boolean sign = false;
								 if (taocanCmd.getStructType()== 0)
								 {
									 structTypeStr = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_3", (HttpServletRequest) pageContext.getRequest());
								     flag = true;
							     }else if(taocanCmd.getStructType()== 1)
								 {
							    	 structTypeStr = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_4", (HttpServletRequest) pageContext.getRequest());
								     flag = true;
							     }
							%>
							<%
								if(flag){
									out.print(structTypeStr);
								}
							%>
							</td>
							<td>
								<input type="hidden" id="structCode<%=taocanCmd.getId() %>" value="<%=taocanCmd.getStructcode()%>" />
								<%=taocanCmd.getStructcode()!=null?taocanCmd.getStructcode():"" %>
							</td>
							<td>
								<input type="hidden" id="spid<%=taocanCmd.getId() %>" value="<%=taocanCmd.getSpId()%>" />
								<%=taocanCmd.getSpUser()!=null?taocanCmd.getSpUser():"" %>
							</td>
							<td>
								<%=taocanCmd.getCreateTime()!=null?df.format(taocanCmd.getCreateTime()):"" %>
							</td>
							<td><%=taocanCmd.getUpdateTime()!=null?df.format(taocanCmd.getUpdateTime()):"" %>
							</td>
							<td>
							<%=taocanCmd.getDepName()!=null?taocanCmd.getDepName():""%>
							</td>
							<td>
							<%
								if(taocanCmd.getName()!=null||taocanCmd.getUserName()!=null){
									if(taocanCmd.getName()!=null){
										out.print(taocanCmd.getName());
									}
									if(taocanCmd.getUserName()!=null){
										out.print("("+taocanCmd.getUserName()+")");
										
									}
								}
							%>
							</td>
							<%              		
										if(btnMap.get(menuCode+"-3")!=null)                       		
										{                        	
									%>
									<td>
										<a href="javascript:update('<%=taocanCmd.getId()%>','<%=taocanCmd.getAcId() %>')"><emp:message key="common_modify" defVal="修改" fileName="common"></emp:message></a>
<%--										<a href="javascript:update('<%=taocanCmd.getId()%>','1)">修改</a>--%>
									</td>
									<%
										}
										if(btnMap.get(menuCode+"-2")!=null)                       		
										{                        	
									%>
									<td>
										
										<a href="javascript:del('<%=taocanCmd.getId()%>','<%=taocanCmd.getAcId() %>')"><emp:message key="common_btn_5" defVal="删除" fileName="common"></emp:message></a>
									</td>
									<%
										}
									%>
						</tr>
						<%
						 	}
						 }else{
						%>
						<tr><td align="center" colspan="12"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
						<%} %>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="12">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
			<div id="addDiv" style="padding:5px;display:none">
				<table style="width:100%;height:100%; font-size: 12px; ">
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称：" fileName="ydyw"></emp:message>
						</td>
<%--						<td width="70%"><input type="text" style="height: 24px; line-height:24px;" class="input_bd" name="busNameAdd" id="busNameAdd" maxlength="16"/></td>--%>
						<td>
							<select id="taocanNameAdd" name="taocanNameAdd" style="width:270px;" class="input_bd" onchange="taocanChange()">
									   <option title="<emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message>" value=""><emp:message key="common_text_13" defVal="请选择" fileName="common"></emp:message></option>
									   <%
												if (busTaocanList != null && busTaocanList.size() > 0){
													for(LfBusTaoCan busTaocan :busTaocanList){
										%>
										  <option title="<%=busTaocan.getTaocan_name()%>(<%=busTaocan.getTaocan_code() %>)" value="<%=busTaocan.getTaocan_code() %>" tem="<%=busTaocan.getTaocan_type() %>" tcname="<%=busTaocan.getTaocan_name() %>"  tcmoney="<%=busTaocan.getTaocan_money() %>" ><%=busTaocan.getTaocan_name()%>(<%=busTaocan.getTaocan_code() %>)</option>
											<%
											  }
											 } 
											%>
									   
									</select>
<%--									&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>--%>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span></td>
					</tr>
<%--					<tr><td height="3px" ></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型：" fileName="ydyw"></emp:message>
						</td>
						<td><label id="taocanTypeAdd" style="color:#000;background-color:#ffffff;border: 0px solid #7F9DB9;"></label></td>
						<td></td>
					</tr>
<%--					<tr><td height="3px" ></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_1_p" defVal="SP账号：" fileName="ydyw"></emp:message>
						</td>
						<td>
							<select id="spidAdd" name="spidAdd" style="line-height:24px;width:270px;" class="input_bd">
									   <option value=""><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message></option>
									   <%
												if (userList != null && userList.size() > 0){
													for(Userdata vo :userList){
										%>
								<option value="<%=vo.getUid() %>" <%=vo.getUid().toString().equals(spUser)?"selected":"" %> ><%=vo.getUserId()%><%=vo.getAccouttype()==1 ? ":("+ MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_6", (HttpServletRequest) pageContext.getRequest())+")" : ":("+MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_7",(HttpServletRequest) pageContext.getRequest())+")" %> </option>
											<%
											  }
											 } 
											%>
									   
									</select>
<%--									&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>--%>
						</td>
						<td>
						&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>
						</td>
					</tr>
<%--					<tr><td height="3px" ></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_3_p" defVal="订购指令：" fileName="ydyw"></emp:message>
						</td>
						<td><input type="text"  onkeyup="limitzw($(this))" onpaste="limitzw($(this))" style="height: 24px; line-height:24px; width: 266px;" class="input_bd" name="orderCmdAdd" id="orderCmdAdd" maxlength="8"/></td>
						<td></td>
					</tr>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_4_p" defVal="退订指令：" fileName="ydyw"></emp:message>
						</td>
						<td><input type="text"  onkeyup="limitzw($(this))" onpaste="limitzw($(this))" style="height: 24px; line-height:24px; width: 266px;" class="input_bd" name="exitCmdAdd" id="exitCmdAdd" maxlength="8"/></td>
						<td></td>
					</tr>
					<tr>
					<td colspan="3" style="text-align:center">
					<input  name="addsubmit" id="addsubmit" class="btnClass5 mr23" type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="javascript: addBusType();"/>
					<input name="addCancel" id="addCancel" class="btnClass6" type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="javascript:doCancel();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
			<div id="bindDiv" style="padding:5px;display:none">
				<table style="width:100%;height:100%;font-size: 12px;">
					<tr style="height: 35px;">
						<td width="39%" align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_1_p" defVal="SP账号：" fileName="ydyw"></emp:message>
						</td>
						<td width="41%">
							<select id="spidAllEdit" name="spidAllEdit" style="width:270px;" class="input_bd">
									   <option value=""><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message></option>
									   <%
												if (userList != null && userList.size() > 0){
													for(Userdata vo :userList){
										%>
								<option value="<%=vo.getUid() %>" <%=vo.getUid().toString().equals(spUser)?"selected":"" %> ><%=vo.getUserId()%><%=vo.getAccouttype()==1 ? ":("+ MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_6", (HttpServletRequest) pageContext.getRequest())+")" : ":("+MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_7",(HttpServletRequest) pageContext.getRequest())+")" %> </option>
											<%
											  }
											 } 
											%>
									   
									</select>
<%--									&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>--%>
						</td>
						<td width="20%">
									&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>
						</td>
					</tr>
					
<%--					<tr><td height="3px" ></td></tr>--%>
					<tr style="height: 33px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_9" defVal="全局退订指令：" fileName="ydyw"></emp:message>
						</td>
						<td><input type="text"   onkeyup="limitzw($(this))" onpaste="limitzw($(this))" style="height: 24px; line-height:24px; width: 267px;" class="input_bd" name="exitAllCmdEdit" id="exitAllCmdEdit" maxlength="8"/>
<%--						&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>--%>
						</td>
						<td>
									&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>
						</td>
					</tr>
					
<%--					<tr><td height="3px" ></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" colspan="3">
							<p class="zhu" style="margin-top: 10px; padding-left:25px; line-height: 18px;"><emp:message key="ydyw_ywgl_tczlsz_text_8" defVal="手机用户发送全局退订指令后，将退订该手机号码签约的所有套餐业务。" fileName="ydyw"></emp:message></p>
						</td>
					</tr>
					<tr>
					<td colspan="3" style="text-align:center">
					<input name="allCancelsubmit" id="allCancelsubmit" class="btnClass5 mr23" type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="javascript:bindBusSP();"/>
					<input name="allCancel" id="allCancel"  class="btnClass6" type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="javascript:doAllCancel();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
<%--					<tr>--%>
<%--						<td style="vertical-align:top;" align="right">--%>
<%--							描述 ：--%>
<%--						</td>--%>
<%--						<td><textarea  rows="3" cols="20" name="busDescriptionBind" id="busDescriptionBind"></textarea></td>--%>
<%--					</tr>--%>
				</table>
			</div>
			<div id="editDiv" style="padding:5px;display:none">
				<table style="width:100%;height:100%;font-size: 12px;">
<%--				<tr><td height="3px"></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称：" fileName="ydyw"></emp:message>
						</td>
						<td>
						<label id="taocanNameEdit"  style="color:#000;background-color:#ffffff;border: 0px solid #7F9DB9;"></label>
							</td>
							<td></td>
					</tr>
<%--					<tr><td height="3px"></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型：" fileName="ydyw"></emp:message>
						</td>
						<td><label id="taocanTypeEdit" style="color:#000;background-color:#ffffff;border: 0px solid #7F9DB9;"></label></td>
						<td></td>
					</tr>
<%--					<tr><td height="3px"></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_1_p" defVal="SP账号：" fileName="ydyw"></emp:message>
						</td>
						<td>
							<select id="spidEdit" name="spidEdit" style="width:270px;" class="input_bd">
									   <option value=""><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message></option>
									   <%
												if (userList != null && userList.size() > 0){
													for(Userdata vo :userList){
										%>
										  <option value="<%=vo.getUid() %>"><%=vo.getUserId()%><%=vo.getAccouttype()==1 ? ":("+ MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_6", (HttpServletRequest) pageContext.getRequest())+")" : ":("+MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_7",(HttpServletRequest) pageContext.getRequest())+")" %> </option>
											<%
											  }
											 } 
											%>
									   
									</select>
<%--									&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>--%>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span></td>
					</tr>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_2_p" defVal="指令类型：" fileName="ydyw"></emp:message>
						</td>
						<td>
						<label id="structTypeEdit" style="color:#000;background-color:#ffffff;border: 0px solid #7F9DB9;"></label>
						<input type="hidden" name="hidstructTypeEdit"  id="hidstructTypeEdit"  value="" />
						</td>
						<td></td>
					</tr>
<%--					<tr><td height="3px"></td></tr>--%>
					<tr style="height: 35px;">
						<td align="left" style="padding-left: 25px;">
							<emp:message key="ydyw_ywgl_tczlsz_text_10" defVal="指令代码：" fileName="ydyw"></emp:message>
						</td>
						<td>
						<input type="text" onkeyup="limitzw($(this))" onpaste="limitzw($(this))" style="height: 24px; line-height:24px; width:267px;" class="input_bd" name="structCodeEdit" id="structCodeEdit" maxlength="8"/>
<%--						&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span>--%>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;<span> <font color="red">*</font></span></td>
					</tr>
<%--					<tr><td height="3px"></td></tr>--%>
					<tr><td colspan="3" style="text-align:center">
					<input name="editsubmit" id="editsubmit" class="btnClass5 mr23" type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="javascript:editBusType()"/>
					<input name="editcancel" id="editcancel" class="btnClass6" type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="javascript:$('#editDiv').dialog('close')"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td></tr>
				</table>
				
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div id="singledetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
			</div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div class="clear"></div>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
			type="text/javascript"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<%-- 为了解决ie8空白双击导致浏览器崩溃，增加日期控件的引用，可解决这问题 --%>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<%--		<script type="text/javascript" src="<%=iPath %>/js/jquery.select.add.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>

		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/ydyw_pkgInstructionSet.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
		   /* var test = getJsLocaleMessage("common","common_ydwx_text_9");
		    alert(test);*/
			//closeTreeFun(["dropMenu"]);
			getLoginInfo("#hiddenValueDiv");
			//noquot($("#busNameEdit"));
			//noquot($("#busNameAdd"));
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
			$("#addDiv").dialog({
				autoOpen: false,
			    width:<%="zh_HK".equals(empLangName)?500:420%>,
			    height:280,
			    title:getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_btn_2"),
			    modal:true,
			    resizable:false
			 });
			$("#bindDiv").dialog({
				autoOpen: false,
			    width:<%="zh_HK".equals(empLangName)?550:480%>,
			    height:200,
			    title:getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_btn_1"),
			    
//			    buttons:{
//					"确定":function(){
//				       bindBusSP();
//					},
//					"取消":function(){
//						 //$("#exitAllCmdAdd").val("");
//			             $("#bindDiv").dialog("close");
//					}
//				},
			    modal:true,
			    resizable:false
			    
			 });
			 $("#editDiv").dialog({
				autoOpen: false,
			    width:<%="zh_HK".equals(empLangName)?430:420%>,
			    height:280,
			    title:getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_11"),
			    modal:true,
			    resizable:false
			 });
			$('#add').click(function(){
				//$('#taocanNameAdd').css("border-color","#BEC3D1");
				//$('#spidAdd').css("border-color","#BEC3D1");
				$("#addDiv").dialog("open");
			});
			$("#bind").click(
					function(){
						//设置全局退订参数
						var allspid = $("#allspidTemp").val();
						var allcmd = $("#allcmdTemp").val();
						if(allspid==""){
						}
						if(allspid!=""){
							$("#spidAllEdit").val(allspid);
						}
						if(allcmd!=""){
							$("#exitAllCmdEdit").val(allcmd);
						}
						$("#bindDiv").dialog("open");
					}
				);
			$("#singledetail").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 170,
				modal: true
			});
            $('#Spid').isSearchSelect({'width':'180','isInput':true,'zindex':0});
		});
		</script>
	</body>
</html>
