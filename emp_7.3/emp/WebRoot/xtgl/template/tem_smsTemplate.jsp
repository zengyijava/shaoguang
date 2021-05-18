<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.template.vo.LfTemplateVo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute("emp_lang");
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("smsTemplate");
menuCode = menuCode==null?"0-0-0":menuCode;
PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
LfTemplateVo temVo = (LfTemplateVo)request.getAttribute("lfTemplateVo");
Long dsflag = temVo.getDsflag()==null?-1l:temVo.getDsflag();
Long tmState = temVo.getIsPass()==null?3l:temVo.getIsPass();
long state = temVo.getTmState()==null?-2:temVo.getTmState();
@ SuppressWarnings("unchecked")
List<LfTemplateVo> temList = (List<LfTemplateVo>) request.getAttribute("temList");
@ SuppressWarnings("unchecked")
List<LfSysuser> sysList = (List<LfSysuser>)session.getAttribute("sysList");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

String optm = request.getParameter("opentm");

String flowId = request.getParameter("flowId");
%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tem_smsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="tem_smsTemplate">
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<input type="hidden" id="iPath" value="<%=iPath %>" />
		<input type="hidden" id="templType" value="1">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<form name="pageForm" action="tem_smsTemplate.htm?method=find" method="post"
					id="pageForm">
					<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" href="javascript:showAddSmsTmp(0)"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
						<a id="delete" onclick="javascript:delAll('<%=path %>')"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
						<%} %>
						
					</div>
					<div id="condition">
						<table>
							<tr>
							<td>
									<span><emp:message key="xtgl_cswh_dxmbgl_mbbh_mh" defVal="模板编号：" fileName="xtgl"/></span>
								</td>
								<td>
									<input type="text" name="tmCode" 
										id="tmCode" value="<%=null!=temVo.getTmCode()?temVo.getTmCode():"" %>" class="tmCode"  />
								</td>
								
								<td>
									<span><emp:message key="xtgl_spgl_mbsp_mbmc_mh" defVal="模板名称：" fileName="xtgl"/></span>
								</td>
								<td>
									<input type="text" name="tmName" 
										id="tmName" value="<%=null!=temVo.getTmName()?temVo.getTmName():"" %>" class="tmName"  />
								</td>
								<td>
									<span><emp:message key="xtgl_spgl_mbsp_mbnr_mh" defVal="模板内容：" fileName="xtgl"/></span>
								</td>
								<td>
									<input type="text" name="tmMsg" id="tmMsg"  value="<%=null!=temVo.getTmMsg()?temVo.getTmMsg():"" %>" class="tmMsg" />
								</td>

								<td class="tdSer">
											<center><a id="search"></a></center>
										</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="xtgl_spgl_mbsp_mblx_mh" defVal="模板类型：" fileName="xtgl"/></span>
								</td>
								<td>
									<select name="dsflag" id="dsflag" class="dsflag" >
										<option value="">
											<emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/>
										</option>
										<option value="0" <%=dsflag-0==0?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_tyjtmk" defVal="通用静态模块" fileName="xtgl"/>
										</option>
										<option value="1" <%=dsflag-1==0?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_tydtmk" defVal="通用动态模块" fileName="xtgl"/>
										</option>
										<option value="2" <%=dsflag-2==0?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_znzqmk" defVal="智能抓取模块" fileName="xtgl"/>
										</option>
										<option value="3" <%=dsflag-3==0?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_ydcwmk" defVal="移动财务模块" fileName="xtgl"/>
										</option>
										<option value="4" <%=dsflag-4==0?"selected":"" %>>
											<emp:message key="xtgl_cswh_dxmbgl_xtjrmb" defVal="系统接入模板" fileName="xtgl"/>
										</option>
									</select>
								</td>
								<td>
									<span> <emp:message key="xtgl_cswh_dxmbgl_cjrmc_mh" defVal="创建人名称：" fileName="xtgl"/></span>
								</td>
								<td>
									<%--
									<select name="userId" id="userId" style="width:180px" >
										<option value="">全部</option>
										<%
											Long userId2=temVo.getUserId()==null?-1:temVo.getUserId();
											if (sysList != null && sysList.size() > 0)
											{
											for(LfSysuser sys : sysList)
											{
										%>
											<option value="<%=sys.getUserId()%>"
												<%if(sys.getUserId()-userId2==0)
												{out.print("selected=\"selected\"");} %>>
												<%=sys.getUserName()%>
											</option>
										<%
											}
											}
										%>	
									</select>
									 --%>
									 <input type="text" id="username" name="username" class="username" value="<%=null!=temVo.getName()?temVo.getName():"" %>"/> 
								</td>
								 <td><emp:message key="xtgl_cswh_dxmbgl_mbzt" defVal="模板状态：" fileName="xtgl"/></td>
									    <td><select id="state" name="state" class="state">
									    <option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
											<option value="1" <%=state==1?"selected":"" %>><emp:message key="xtgl_spgl_shlcgl_qy" defVal="启用" fileName="xtgl"/></option>
											<option value="0" <%=state==0?"selected":"" %>><emp:message key="xtgl_spgl_shlcgl_ty" defVal="停用" fileName="xtgl"/></option>
										</select>
										</td>
										<td class="tdSer">
										</td>
							</tr>
							<tr>
							<td>
									<span><emp:message key="xtgl_spgl_xxsp_spzt_mh" defVal="审批状态：" fileName="xtgl"/></span>
								</td>
								<td>
									<select name="tmState" id="tmState" class="tmState" >
										<option value="">
											<emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/>
										</option>
										<option value="-1" <%=tmState+1==0?"selected":"" %>>
											<emp:message key="xtgl_spgl_xxsp_wsp" defVal="未审批" fileName="xtgl"/>
										</option>
										<option value="0" <%=tmState+0==0?"selected":"" %>>
											<emp:message key="xtgl_spgl_xxsp_wxsp" defVal="无需审批" fileName="xtgl"/>
										</option>
										<option value="1" <%=tmState-1==0?"selected":"" %>>
											<emp:message key="xtgl_spgl_xxsp_sptg" defVal="审批通过" fileName="xtgl"/>
										</option>
										<option value="2" <%=tmState-2==0?"selected":"" %>>
											<emp:message key="xtgl_cswh_dxmbgl_spwtg" defVal="审批未通过" fileName="xtgl"/>
										</option>
									</select>
								</td>
                                <td><%if(flowId != null){out.print("审核流程ID：");}%></td>
                                <td>
                                    <%if(flowId != null){
                                    %>
                                    <input type="text" name="flowId" class="flowId_input" value="<%=flowId%>" onkeyup="javascript:numberControl($(this))" maxlength="19"/>
                                    <% }%>
                                </td>
							<td></td>
							<td></td>
							<td></td>
							</tr>
							
							
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
										<th>
											<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')" />
										</th>
										<th>
											<emp:message key="xtgl_cswh_dxmbgl_mbbh" defVal="模板编号" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_mbsp_mbmc" defVal="模板名称" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_mbsp_mblx" defVal="模板类型" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_mbsp_mbnr" defVal="模板内容" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_cswh_dxmbgl_cjrmc" defVal="创建人名称" fileName="xtgl"/>
										</th>
										<th>
										    <emp:message key="xtgl_spgl_mbsp_cjrq" defVal="创建日期" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_xxsp_spzt" defVal="审批状态" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_cswh_dxmbgl_mbzt" defVal="模板状态" fileName="xtgl"/>
										</th>
										<th colspan="3">
											<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
										</th>
										
									</tr>
								</thead>
								<tbody>
								<%
								if (temList != null && temList.size() > 0)
								{
									for (LfTemplateVo tem : temList)
									{
										boolean isEdit=false;
										if(tem.getShareType()==0){
											isEdit=true;
										}
								%>
									<tr>
										<td>
										<% if(btnMap.get(menuCode+"-2")!=null&&isEdit){%>
											<input type="checkbox" name="checklist" value="<%=tem.getTmid() %>"/>
										<% }else{%>
											<input type="checkbox" disabled="disabled"/>
										<%} %>
										</td>
										<td>
											<%=tem.getTmCode()==null?"":tem.getTmCode() %>
										</td>
										<td class="textalign" >
											<xmp><%=tem.getTmName() %></xmp>
										</td>
										<td class="ztalign" >
											<%
												if(tem.getDsflag()-1==0)
												{
													out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_tydtmk",request));
												}else if(tem.getDsflag()-0==0)
												{
													out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_tyjtmk",request));
												}else if(tem.getDsflag()-2==0)
												{
													out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_znzqmk",request));
												}else if(tem.getDsflag()-3==0)
												{
													out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_ydcwmk",request));
												}else if(tem.getDsflag()-4==0)
												{
													out.print(MessageUtils.extractMessage("xtgl","xtgl_cswh_dxmbgl_xtjrmb",request));
												}
											%>
										</td>
										<td class="textalign" >
										<%
										 if(!"".equals(tem.getTmMsg())&&tem.getTmMsg()!=null){
										String st = "";
										String temp = tem.getTmMsg().replaceAll("#[pP]_([1-9][0-9]*)#","{#"+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_cs",request)+"$1#}");
											if(temp.length()>20)
											{
												st = temp.substring(0,20)+"...";
											}else
											{
												st = temp;
											}
										%>
										<a onclick="javascript:modify(this,1)">
								  <label class="temp_label"><xmp class="temp_xmp"><%=temp%></xmp></label>
								  <xmp  class="st_xmp"><%=st %></xmp>
								  </a> 					<%}else{ %>		<%} %>
										</td>
										<td class="textalign" >
											<%
											    if(tem.getUserState() !=null && tem.getUserState() ==2)
											    {
											      out.print(tem.getName()+"<font color='red'>("+MessageUtils.extractMessage("xtgl","xtgl_cswh_dxmbgl_yzx",request)+")</font>");
											    }
											    else
											    {
											       out.print(tem.getName());
											    }
											%>
										</td>
										<td>
											<%
												if(tem.getAddtime()!=null)
												{
													out.print(df.format(tem.getAddtime()));
												}
											%>
										</td>
										<td class="ztalign" >
											<%
										String s = "";
											//boolean flag = false;
										 if (tem.getIsPass()== -1)
										 {
										     s = MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_wsp",request);
										     //flag = true;
									     }else if(tem.getIsPass()== 0)
										 {
										     s = MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_wxsp",request);
									     }else if(tem.getIsPass()== 1)
										 {
										     s = MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_sptg",request);
										     //flag = true;
									     }else if(tem.getIsPass()== 2)
									     {
									    	 s = MessageUtils.extractMessage("xtgl","xtgl_cswh_dxmbgl_spwtg",request);
									    	 //flag = true;
									     }
										%>
											<%
											if(tem.getIsPass()== -1){
												%>
													<a href="javascript:openReviewFlow('<%=tem.getTmid() %>','<%=tem.getUserId()%>','3')"><%=s%></a>
												<%
											}else if(tem.getIsPass()== 1||tem.getIsPass()== 2){
											    %>
											    	<a href="javascript:opentmpAudmsg('<%=tem.getTmid() %>')"><emp:message key="xtgl_spgl_xxsp_ck" defVal="查看" fileName="xtgl"/></a>
											    <%
											}else{
												out.print(s);
											}
											%>
										</td>
										<td class="ztalign" >
										<center>
										<%if(tem.getTmState()-1 == 0){
										%>
											
											<%if(isEdit){ %>
											 <select  name="tempState<%=tem.getTmid() %>" id="tempState<%=tem.getTmid() %>" class="input_bd" onchange="javascript:changestate('<%=tem.getTmid() %>')">
												 <option value="1" selected="selected"><emp:message key="xtgl_spgl_shlcgl_yqy" defVal="已启用" fileName="xtgl"/></option>
										         <option value="0" ><emp:message key="xtgl_spgl_shlcgl_ty" defVal="停用" fileName="xtgl"/></option>
									          </select>
											 <%}else{
												 %>
												<emp:message key="xtgl_spgl_shlcgl_yqy" defVal="已启用" fileName="xtgl"/>
											 <%} %>
										<%
										}else{
										%>
											
											<%if(isEdit){ %>
											<select  name="tempState<%=tem.getTmid() %>" id="tempState<%=tem.getTmid() %>" class="input_bd"  onchange="javascript:changestate('<%=tem.getTmid() %>')">
												 <option value="0" selected="selected"><emp:message key="xtgl_spgl_shlcgl_yty" defVal="已停用" fileName="xtgl"/></option>
										         <option value="1" ><emp:message key="xtgl_spgl_shlcgl_qy" defVal="启用" fileName="xtgl"/></option>									          
									        </select>
											 <%}else{
												 %>
												 <emp:message key="xtgl_spgl_shlcgl_yty" defVal="已停用" fileName="xtgl"/>
											 <%} %>
										<%	
										}
										%>
										</center>
										</td>
										
										<%if(btnMap.get(menuCode+"-3")!=null) { %>
                                            <%if(tem.getIsPass() == 0&&isEdit){ %>
                                                <td><a href="javascript:showEditSmsTmp(<%=tem.getTmid() %>)"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a></td>
                                             <%}else{%>
                                                <td><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></td>
                                             <%} %>
                                            <%if(isEdit){%>
                                            <td><a href="javascript:showShareTmp(<%=tem.getTmid() %>,'<%=tem.getTmCode() %>','<%=tem.getTmName() %>',<%=tem.getUserId() %>)"><emp:message key="xtgl_cswh_dxmbgl_gx" defVal="共享" fileName="xtgl"/></a></td>
                                            <%}else{%>
                                            <td><emp:message key="xtgl_cswh_dxmbgl_gx" defVal="共享" fileName="xtgl"/></td>
                                            <%}%>
                                         <%}%>
										
										<td>
										<%if(btnMap.get(menuCode+"-2")!=null&&isEdit) { %>
											<a href="javascript:del('<%=path%>',<%=tem.getTmid() %>)"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
										 <%} else{%>
										 	<emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/>
										 	<%	} %>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="13"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="13">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
			</div>
			<%} %>
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
		<div id="modify" title="<emp:message key='xtgl_spgl_mbsp_mbnr' defVal='模板内容' fileName='xtgl'/>"  class="modify">
				<div id="msg" class="msg"><xmp class="msg_xmp"></xmp></div>
		</div>
		
		<div id="editSmsTmpDiv" title="<emp:message key='xtgl_spgl_shlcgl_dxmb' defVal='短信模板' fileName='xtgl'/>" class="editSmsTmpDiv">
			<iframe id="editSmsTmpFrame" name="editSmsTmpFrame" class="editSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
							
		</div>
		<div id="addSmsTmpDiv" title="<emp:message key='xtgl_spgl_shlcgl_dxmb' defVal='短信模板' fileName='xtgl'/>" class="addSmsTmpDiv">
			<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" class="addSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
		</div>
		<%-- 共享模板 DIV --%>
		<div id="shareTmpDiv" title="<emp:message key='xtgl_cswh_dxmbgl_mbgx' defVal='模板共享' fileName='xtgl'/>" class="shareTmpDiv">
			<center>
			<iframe id="flowFrame" name="flowFrame" src=""  attrid="" tmpname="" class="flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</center>
			<table>
				<tr>
					<td class="shlcgl_qd_td" align="center">
						<input type="button"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" id="updateShareTemp" class="btnClass5 mr23" onclick="javascript:updateShareTemp();" />
						<input type="button"  value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/> " class="btnClass6" onclick="javascript:closeShare();" />
						<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
						<br/>
					</td>
				</tr>
			</table>
		</div>
		
		<div id="smsdetailinfo" title="<emp:message key='xtgl_cswh_dxmbgl_xxxx' defVal='详细信息' fileName='xtgl'/>" class="smsdetailinfo">
				<div class="recordTableDiv" id="recordTableDiv" align="center">
				<table  id="recordTable"  class='recordTable'>
				</table>
				</div>
				<div class="nextrecordmgs" id="nextrecordmgs" align="left">
				</div>
		</div>	
		<div id="reviewflowinfo" title="<emp:message key='xtgl_cswh_dxmbgl_dsp' defVal='待审批' fileName='xtgl'/>" class="reviewflowinfo">
				<div class="reviewTableDiv" id="reviewTableDiv" align="center">
				<table  id="reviewTable"  class="reviewTable">
				</table>
				</div>
				<div class="nextreviewmgs" id="nextreviewmgs" align="left">
				</div>
		</div>	
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/template.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			$("#content select").empSelect({width:80});
			$('#content select').each(function(){
				$(this).next().hide();
				$(this).before('<div class="selectBefore">'+$(this).find('option:selected').text()+'</div>');
		  	});
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
				$(this).find('select').next().show().siblings().hide();
			}, function() {
				$(this).removeClass("hoverColor");
				var $select = $(this).find('select');
				$select.next().hide();
				$select.prev().show();
			});
			
			$("#smsdetailinfo").dialog({
				autoOpen: false,
				modal:true,
				title:getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_69"), 
				width:680,
				height: 'auto',
				minHeight:180,
				maxHeight:650,
				closeOnEscape: false,
				resizable:false,
				open:function(){
				},
				close:function(){
				}
			});
			$("#reviewflowinfo").dialog({
				autoOpen: false,
				modal:true,
				title:getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_2"), 
				width:880,
				height: 'auto',
				minHeight:170,
				maxHeight:750,
				closeOnEscape: false,
				resizable:false,
				open:function(){
				},
				close:function(){
				}
			});		
			//新增共享模板DIV
			$("#shareTmpDiv").dialog({
				autoOpen: false,
				height:500,
				width: 530,
				resizable:false,
				modal: true
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			<%if(optm != null){%>
			showAddSmsTmp(<%=optm%>);
			<%}%>
		});
		
		</script>
	</body>
</html>
