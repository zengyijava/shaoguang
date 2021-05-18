<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="com.montnets.emp.entity.engine.LfReply" %>
<%@page import="com.montnets.emp.entity.datasource.LfDBConnect"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	
	LfProcess process=(LfProcess)request.getAttribute("process");
	
	String strPrId = request.getParameter("prId");
	Long prId = null;
	if(strPrId != null && !"".equals(strPrId))
	{
		prId=Long.valueOf(strPrId);
	}
	else if(process != null && process.getPrId() != null && process.getPrId() > 0)
	{
		prId = process.getPrId();
	}
	
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	
	String serId = request.getParameter("serId");
	LfService service=(LfService)request.getAttribute("service");

	LfSysuser curSysuser=(LfSysuser)request.getAttribute("curSysuser");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	
	List<LfDBConnect> dbList = null;
	if(request.getAttribute("dbList") != null)
	{
		
		dbList=(List<LfDBConnect>)request.getAttribute("dbList");
	}
	else
	{
		dbList = new ArrayList<LfDBConnect>();
	}

	List<LfTemplate> tmpList = null;
	if(request.getAttribute("tmpList") != null)
	{
		tmpList=(List<LfTemplate>)request.getAttribute("tmpList");
	}
	else
	{
		tmpList = new ArrayList<LfTemplate>();
	}
	
	String tailcontents = (String)request.getAttribute("tailcontents");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_bjclbzxx" defVal="编辑处理步骤信息" fileName="znyq"></emp:message></title>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		
	</head>
	<body id="eng_editMtProcess" class="eng_editMtProcess">
				<div id="container">
					<%--<div class="top">
						<div id="top_right">
							<div id="top_left"></div>
								<b>当前位置:</b>[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]
								-[<%=titleMap.get(menuCode) %>]-[步骤管理]-[编辑处理步骤信息]
						</div>--%>
					</div>
					<div id="rContent" class="rContent">
						<center>
							
							<div >
								<form method="post" id="proform" action="eng_mtProcess.htm" >
								<input id="lguserid" name="lguserid" type="hidden" value="<%=curSysuser.getUserId() %>" />
								<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=lgcorpcode %>" />
								<input id="submitcheck" type="hidden" onclick="checkSubBefore()" />
								<input type="hidden" name="serId" value="<%=request.getParameter("serId") %>"/>
								<input type="hidden" name="prId" value="<%=prId==null?"":prId %>"/>
								<input type="hidden" id="tailcontents" name="tailcontents" value="<%=tailcontents==null?"":tailcontents %>"/>
								
									<table  id="proinfotable">
									<thead>
										<tr>
											<td class="spanPrNameTd">
												<span id="spanPrName"><emp:message key="znyq_ywgl_xhywgl_bzmc_mh" defVal="步骤名称：" fileName="znyq"></emp:message></span>
											</td>
											<td class="prNameTd">
											<div id="prNameDiv" class="input_bd">
												<input type="text" name="prName" id="prName" value="<%=process.getPrName()==null?"":process.getPrName() %>" maxlength="32"  />
											</div>
											<font class="tipColor">&nbsp;*</font>
												
											<input type="hidden" name="hidOpType" value="edit"/>
											</td>
										</tr>
										<tr>
											<td >
												<span><emp:message key="znyq_ywgl_xhywgl_bzms_mh" defVal="步骤描述：" fileName="znyq"></emp:message></span>
											</td>
											<td>
											<div id="commentsDiv" class="input_bd">
												<input type="text" name="comments" id="comments" value="<%=process.getComments()==null?"":process.getComments() %>" maxlength="32" />
											</div>
											</td>
										</tr>
										<tr>
											<td >
												<span id="spanPrType"><emp:message key="znyq_ywgl_xhywgl_bzlx_mh" defVal="步骤类型：" fileName="znyq"></emp:message></span>
											</td>
											<td>
												<%--<select name="prType" id="select" style="width:355px;">
													<option value="">
														请选择
													</option>
													<option value="4" <%if(process.getPrType()-4==0){out.print("selected=\"selected\"");}%> >
														Select
													</option>
													<option value="5" <%if(process.getPrType()-5==0){out.print("selected=\"selected\"");}%>>
														Reply
													</option>
												</select>--%>
												<input name="prType" id="prType" type="hidden" value="<%=process.getPrType() %>" />
												<% if(process.getPrType()-4==0){%>
												Select
												<%} %>
												<% if(process.getPrType()-5==0){%>
												Reply
												<%} %>
											</td>
										</tr>
										<tr class="finalStepTr">
											<td >
												<span><emp:message key="znyq_ywgl_xhywgl_bzsfzhclbz_mh" defVal="步骤是否最后处理步骤：" fileName="znyq"></emp:message></span>
											</td>
											<td>
												<input name="finalState" type="radio" value="1" id="yes" <%=process.getPrType()==5?"checked":"" %>/>
												<em id="y"><emp:message key="znyq_ywgl_xhywgl_s" defVal="是" fileName="znyq"></emp:message></em>
												<input name="finalState" type="radio" value="0" id="no"
													<%=process.getPrType()==4?"checked":"" %>/>
												<em id="n"><emp:message key="znyq_ywgl_xhywgl_f" defVal="否" fileName="znyq"></emp:message></em>
											</td>
										</tr>
										<%-- <tr>
											<td colspan="2" id="btn">
											<%
												if(service.getUserId()-curSysuser.getUserId()==0 || service.getOwnerId()-curSysuser.getUserId()==0)
												{
											%>
												<input id="addBasicInfoBtn" type="button" value="确 定"  class="btnClass1"/>
											<%
												}
											%>
												<input type="button" name="previous" id="previous" value="返回"
													onclick="javascript:location.href='ser_mtProcess.htm?serId=<%=serId %>&lguserid='+$('#lguserid').val()" class="btnClass1" />
											</td>
										</tr> --%>
										</thead>
									</table>
								
									<br/>
									<%
										if(process.getPrType() == 4)
										{
									%>
									<table id="sqltable" class="div_bg">
									<thead>
									<tr>
										<td class="spanDBLTd">
											<span id="spanDBL"><emp:message key="znyq_ywgl_xhywgl_sjklj_mh" defVal="数据库连接：" fileName="znyq"></emp:message></span>
											<input type="hidden" name="hidOpType" value="editSql"/>
										</td>
										<td class="dbIdTd">
										<div id="dbIdDiv" class="input_bd">
											<label>
											<select name="dbId" id="dbId">
											<option value=""><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
											<%
												for(int i=0;i<dbList.size();i++)
												{
													LfDBConnect dbConn=dbList.get(i);
											%>
												<option value="<%=dbConn.getDbId() %>"
													<%
														if(process.getDbId()!=null && process.getDbId()-dbConn.getDbId()==0)
														{
															out.print("selected=\"selected\"");
														}
													%>>
													<%=dbConn==null?"":(dbConn.getDbconName()==null?"":dbConn.getDbconName().replace("<","&lt;").replace(">","&gt;")) %>
												</option>
											<%
												}
											%>
											</select>
											</label>
											</div>
											<font class="tipColor">&nbsp;*</font>
										</td>
									</tr>
									<tr>
										<td class="spanSqlTempTd"><span id="spanSqlTemp"><emp:message key="znyq_ywgl_xhywgl_znzqmb" defVal="智能抓取模板：" fileName="znyq"></emp:message></span></td>
										<td>
										<div class="spanSqlTempDiv" class="input_bd">
										<label><select name="tempSel" id="tempSel" onchange="getSql(this.value)">
											<option value="">===<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>===</option>
										<%
											if(tmpList != null && tmpList.size()>0)
											{
												for(LfTemplate temp : tmpList)
												{
										%>
												<option value="<%=temp.getTmid() %>" <% if(temp.getTmid().equals(process.getTemplateId())){ %>selected="selected"<%} %>><%=temp.getTmName().replace("<","&lt;").replace(">","&gt;") %>(ID:<%=temp.getTmid() %>)</option>
										<%
												}
											}
										 %>
										</select></label><%-- &nbsp;<a href="javascript:location.href=location.href">还原</a> --%>
										</div>
										</td>
									</tr>
									<tr>
										<td class="spanSqlTd">
											<span id="spanSql"><emp:message key="znyq_ywgl_xhywgl_sqlyj_mh" defVal="SQL语句：" fileName="znyq"></emp:message></span>
										</td>
										<td class="spanSql1Td">
										<div id="spanSqlDiv" class="input_bd">
											<textarea class="textarea_limit" name="sql" id="sql" ><%
												if(process.getSql()!=null)
												{
													out.print(process.getSql());
												}
											%></textarea>
											<xmp id="sqlXmp" name="sqlXmp" style="display:none"><%=(process.getSql()==null?"":process.getSql())%></xmp>
										</div>
											<font class="tipColor">&nbsp;*</font>
										</td>
									</tr>
									<%-- <tr>
										<td colspan="4" id="btn">
										<%
											if(service.getUserId()-curSysuser.getUserId()==0 || service.getOwnerId()-curSysuser.getUserId()==0)
											{
										%>
											<input type="button" value="确定" id="subConfigSqlBtn" class="btnClass1"/>
										<%
											}
										%>
											<input id="subConfigSqlBackBtn" type="button" value="返回" 
												onclick="javascript:location.href='ser_mtProcess.htm?serId=<%=serId %>&lguserid='+$('#lguserid').val()" class="btnClass1"/>
										</td>
									</tr> --%>
									</thead>
								</table>
								<%
								}
								else if(process.getPrType()==5)
								{
								
								%>
								<table id="sqltable" class="div_bg">
									<thead>
									<tr>
										<td class="spanDataSourceTd">
											<span id="spanDataSource"><emp:message key="znyq_ywgl_xhywgl_sjy_mh" defVal="数据源：" fileName="znyq"></emp:message></span>
										</td>
										<td>
											<label>
											<%
												if(request.getAttribute("selPro") != null)
												{
													LfProcess selPro = (LfProcess)request.getAttribute("selPro");
												
											%>
											<input name="msgLoopId" id="msgLoopId" type="hidden" value="<%=selPro.getPrId() %>" />
											<%=selPro.getPrName() %>
											
											<%
												}
											%>
											</label>
										</td>
									</tr>
									<tr>
										<td><span><emp:message key="znyq_ywgl_xhywgl_dxmb_mh" defVal="短信模板：" fileName="znyq"></emp:message></span></td>
										<td>
										<div id="smsModelTd" class="input_bd">
										<label><select name="tempSel" id="tempSel" onchange="getSmsTempl(this.value)">
											<option value="">===<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>===</option>
										<%
											if(tmpList != null && tmpList.size()>0)
											{
												for(LfTemplate temp : tmpList)
												{
										%>
												<option value="<%=temp.getTmid() %>" <% if(temp.getTmid().equals(process.getTemplateId())){ %>selected="selected"<%} %>><%=temp.getTmName().replace("<","&lt;").replace(">","&gt;") %>(ID:<%=temp.getTmid() %>)</option>
										<%
												}
											}
										 %>
										</select></label><%-- &nbsp;<a href="javascript:location.href=location.href">还原</a> --%>
										</div>
										</td>
									</tr>
									<tr>
										<td>
											<span id="spanMsgCon"><emp:message key="znyq_ywgl_xhywgl_fsnr_mh" defVal="发送内容：" fileName="znyq"></emp:message></span>
										</td>
										<td >
										<div id="spanMsgConDiv" class="input_bd">
											<textarea class="textarea_limit" id="msgMain" name="msgMain"><%
												LfReply reply =null;
												if(request.getAttribute("reply") !=null)
												{
													reply = (LfReply)request.getAttribute("reply");
													
													out.print(reply.getMsgMain());
												}
											%></textarea>
											<xmp id="msgXmp" name="msgXmp" style="display:none"><%=(reply==null?"":reply.getMsgMain())%></xmp>
										</div>
										<div id="tailConDiv" class="<%=tailcontents==null?"hiddenValueDiv;":"emptycss" %>">
											<div class="tailContCss input_bd"><emp:message key="znyq_ywgl_xhywgl_twnr_mh" defVal="贴尾内容：" fileName="znyq"></emp:message><label id="showtailcontent"><%=tailcontents==null?"":StringEscapeUtils.escapeHtml(tailcontents) %></label></div>
										</div>
											
										</td>
										<td class="tailConDivTd"><font class="tipColor">&nbsp;*</font></td>
									</tr>
									<tr >
										<td><span id="spanPreview"><emp:message key="znyq_ywgl_xhywgl_dxyl_mh" defVal="短信预览：" fileName="znyq"></emp:message></span></td>
										<td>
											<input type="hidden" id="pathUrl" value="<%=path %>" />
											<input id="btnPreview" type="button" value="<emp:message key="znyq_ywgl_common_text_6" defVal="预览" fileName="znyq"></emp:message>" class="btnClass3" onclick="getResult()" />
										</td>
									</tr>
									<%-- <tr>
										<td colspan="4" id="btn">
										<%
											if(service.getUserId()-curSysuser.getUserId()==0 || service.getOwnerId()-curSysuser.getUserId()==0)
											{
										%>
											<input type="button" value="确定" id="subConfigSqlBtn" class="btnClass1"/>
										<%
											}
										%>
											<input id="subConfigSqlBackBtn" type="button" value="返回" 
												onclick="javascript:location.href='ser_mtProcess.htm?serId=<%=serId %>&lguserid='+$('#lguserid').val()" class="btnClass1"/>
										</td>
									</tr> --%>
									</thead>
								</table>
								<%} %>
								<div id="errInfoMsg"><span></span></div>
								</form>
								
							</div></center>
							
							<div id="modify" title="<emp:message key="znyq_ywgl_xhywgl_dxyl" defVal="短信预览" fileName="znyq"></emp:message>">
								<div id="msg"><xmp></xmp></div>
							</div>
							
					</div>
					<%-- 这是每个界面相应的DIV --%>
					<div class="clear"></div>
					<div class="bottom">
						<div id="bottom_right">
							<div id="bottom_left"></div>
						</div>
					</div>
				
				<%--end round_content--%>

				<div class="clear"></div>
			<%-- end container --%>
			<%--底部--%>
			
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/replyAdd.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_editMtProcess.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_smsContentLength.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		function show(){
			<% Object result=request.getAttribute("editProcessResult");
				if(result!=null && result.toString().equals("1")){
			%>
					$(window.parent.document).find("#prNameSpan_<%= process.getPrType() %>").html("<%=process.getPrName() %>");
					//alert("修改步骤成功！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgbzcg"));
					//关闭弹窗
					$(window.parent.document).find("#editCancel").click();
			<%
				}
				else if(result!=null && result.toString().equals("2"))
				{
					String ctext = "";
					if(process.getPrType() == 4)
					{
						//ctext = "修改数据库查询";
						ctext = MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_xgsjkcx",request);
			%>
						$(window.parent.document).find("#prEditLink_5").removeAttr('style');
						$(window.parent.document).find("#prEditLink_5").removeAttr('onclick');
						$(window.parent.document).find("#prEditLink_5").attr("href","javascript:showInfo(5,-1);");
			<%
					}
					else if(process.getPrType() == 5)
					{
						//ctext = "修改短信模板";
						ctext = MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_xgdxmb",request);
					}
			%>
					//$(window.parent.document).find("#proTitleSpan_<%= process.getPrType() %>").html("步骤名称：");
					$(window.parent.document).find("#proTitleSpan_<%= process.getPrType() %>").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmc_mh"));
					$(window.parent.document).find("#prNameSpan_<%= process.getPrType() %>").html("<%=process.getPrName() %>");
					$(window.parent.document).find("#prEditLink_<%= process.getPrType() %>").attr("href","javascript:showInfo(<%= process.getPrType() %>,<%= process.getPrId() %>);");
					$(window.parent.document).find("#prEditLink_<%= process.getPrType() %>").text("<%=ctext %>");
					//alert("新增步骤成功！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xzbzcg"));
			        $(window.parent.document).find("#editCancel").click();
			<%
				}
				else if(result!=null && result.toString().equals("0"))
				{
			%>
					//alert("操作失败！");	
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
			<%
				}
				request.removeAttribute("editProcessResult");
			%>
		}
        $(document).ready(function(){
        	getSmsContentMaxLen('<%=serId %>');
			getLoginInfo("#hiddenValueDiv");
			noquot("#prName");
			noquot("#comments");

            //用textarea显示短信内容
            $("#sql").empty();
            $("#sql").text($("#sqlXmp").text());

            //用textarea显示短信内容
            $("#msgMain").empty();
            $("#msgMain").text($("#msgXmp").text());

			<%if(process.getPrType()-4==0){%> 
				$("#yes").hide();
				$("#y").hide();
				$("#no").show();
			   	$("#n").show();
			<%} else if(process.getPrType()-5==0){%>
			  	$("#yes").show();
				$("#y").show();
				$("#no").hide();
			  	$("#n").hide();
			<%}%>
			$('#u_o_c_explain').find('> p').next().hide();
			$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
			show();
			
			<%
			if (!curSysuser.getUserId().equals(service.getUserId()) && !curSysuser.getUserId().equals(service.getOwnerId()))
			         {
			%>
			$(window.parent.document).find("#editok").attr("disabled","disabled");
			<%	
			}
			else
			{
			%>
			$(window.parent.document).find("#editok").attr("disabled","");
			<%
			}
			%>
			
        });
        
    </script>
		
	</body>
</html>