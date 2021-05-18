<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="com.montnets.emp.entity.engine.LfReply" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
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
	
LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("mtService");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_dxhfmbbj" defVal="短信回复模板编辑" fileName="znyq"></emp:message></title>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
			
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/replyAdd.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script>
		function show(){
				<% Object result=request.getAttribute("editReplyResult");
						if(result!=null && result.toString().equals("4")){%>
						//alert("配置短信模板成功！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_pzdxmbcg"));
					<%}else if(result!=null && result.toString().equals("0")){%>
						//alert("操作失败！");	
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
					<%}request.removeAttribute("editReplyResult");
					 %>
				}
		</script>
	</head>
	<body onload="show()" id="eng_mtSmsTemplate" class="eng_mtSmsTemplate">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container">
			<div class="top">
				<div id="top_right">
					<div id="top_left"></div>
						<b><emp:message key="znyq_ywgl_xhywgl_dqwz_mh" defVal="当前位置:" fileName="znyq"></emp:message></b>
						[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]
						-[<%=titleMap.get(menuCode) %><emp:message key="znyq_ywgl_xhywgl_bzgldxhfmbbj" defVal="]-[步骤管理]-[短信回复模板编辑]" fileName="znyq"></emp:message>
				</div>
			</div>
			<div class="rContent">
				<center>
				<div id="u_o_c_explain">
						<p>
							<emp:message key="znyq_ywgl_xhywgl_smhfdxsz" defVal="说明：回复短信设置" fileName="znyq"></emp:message>
						</p>
						<ul>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_hfdxszyldyfsgyhddxdjtnr" defVal="回复短信设置用来定义发送给用户的短信的具体内容" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_djxzankxzxttgdhs" defVal="点击【选择】按钮可选择系统提供的函数或变量作为短信内容" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_djqdanbcsdydhfdxnr" defVal="点击【确定】按钮保存所定义的回复短信内容" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_djfhanfqbcbfhsjclbzgllb" defVal="点击【返回】按钮放弃保存并返回数据处理步骤管理列表" fileName="znyq"></emp:message>
							</li>
						</ul>
					</div>
					<div id="detail_Info">
						<form method="post" action="<%=path %>/eng_mtProcess.htm?method=editReply" id="reply">
						<%
							Long prId =Long.valueOf(request.getParameter("prId"));
							Long serId=Long.valueOf(request.getParameter("serId"));
							
							LfService service=(LfService)request.getAttribute("service");
							
							LfReply reply=(LfReply)request.getAttribute("reply");
							if(reply==null)
							{
								reply=new LfReply();
								reply.setMsgLoopId(0l);
							}
							@SuppressWarnings("unchecked")
							List<LfProcess> proList=(List<LfProcess>)request.getAttribute("proList");
						%>
						<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
							<input type="hidden" name="prId" value='<%=prId %>' id="prId" />
							<input type="hidden" name="serId" value='<%=serId %>' id="serId" />
							<input type="hidden" name="hidOpType" value="editReply"/>
							<table >
							<thead>
								<tr>
									<td class="twenTd">
										<span><emp:message key="znyq_ywgl_xhywgl_bzmc_mh" defVal="步骤名称:" fileName="znyq"></emp:message></span>
									</td>
									<td>
									<label>
										<select name="msgLoopId" id="msgLoopId" class="commonWidth">
											<option value="">
												<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>
											</option>
										<%
										if(proList != null && proList.size()>0)
										{
											for(int i=0;i<proList.size();i++)
											{
												LfProcess process=proList.get(i);
										%>
											<option value="<%=process.getPrId() %>"
											<%
												if(reply.getMsgLoopId()-process.getPrId()==0)
												{
													out.print("selected=\"selected\"");
												}
											%>>
												<%=process.getPrName().replace("<","&lt;").replace(">","&gt;") %>
											</option>
										<%
											}
										}
										%>
										</select></label>
										<font class="tipColor"><emp:message key="znyq_ywgl_xhywgl_xzbzlxwselectdbz" defVal="选择步骤类型为Select的步骤" fileName="znyq"></emp:message></font>
									</td>
								</tr>
								<tr>
									<td >
										<span><emp:message key="znyq_ywgl_xhywgl_xxt_mh" defVal="信息体:" fileName="znyq"></emp:message></span>
									</td><td><label>
										<select id="selectInfoBody" class="commonWidth">
											<option value="">
												---<emp:message key="znyq_ywgl_xhywgl_kxzmb" defVal="可选择模板" fileName="znyq"></emp:message>---
											</option>
											<%
												@SuppressWarnings("unchecked")
												List<LfTemplate> temList=(List<LfTemplate>)request.getAttribute("temList");
												for(int t=0;t<temList.size();t++)
												{
													LfTemplate tem=temList.get(t);
											%>
											<option value="<%=tem.getTmid() %>"><%=tem.getTmName().replace("<","&lt;").replace(">","&gt;") %>(ID:<%=tem.getTmid() %>)</option>
											<%} %>
										</select></label>
										<font class="tipColor"><emp:message key="znyq_ywgl_xhywgl_kxzdxmbsxksbj" defVal="可选择短信模板实现快速编辑" fileName="znyq"></emp:message></font>
									</td>
								</tr>
								<tr>
									<td></td>
									<td>
										<textarea class="textarea_limit" id="msgMain" name="msgMain" cols="60" 
											rows="5"><%=reply.getMsgMain()!=null? reply.getMsgMain():""%></textarea>
									</td>
								</tr>
								<tr>
									<td >
										<span><emp:message key="znyq_ywgl_xhywgl_dxnryl1t" defVal="短信内容预览(1条)：" fileName="znyq"></emp:message></span>
										</td>
										<td>
										<input type="button" id="seesee" value="<emp:message key="znyq_ywgl_common_text_6" defVal="预览" fileName="znyq"></emp:message>" class="btnClass1"/>
									</td>
	
								</tr>
								<tr>
								<td></td>
									<td>
										<textarea id="result" onblur="javascript:$(this).val($.trim(this.value));" readonly="readonly"></textarea>
									</td>
								</tr>
								<tr>
									<td colspan="2" id="btn">
									<%
										if(service.getUserId()-curSysuser.getUserId()==0 || service.getOwnerId()-curSysuser.getUserId()==0)
										{
									%>
										<input type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="subReply" class="btnClass1"/>
									<%
										}
									%>
										<input type="button" name="previous" id="previous" value="<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message>" 
											onclick="javascript:location.href='<%=path%>/eng_mtProcess.htm?serId=<%=serId %>&lguserid='+$('#lguserid').val()" class="btnClass1"/>
										<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
										<br/>
									</td>
								</tr>
								</thead>
							</table>
						</form>
					</div>
					</center>
				</div>
				<%-- 这是每个界面相应的DIV --%>
				<div class="clear"></div>
			<%--end round_content--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			</div>
	</body>
</html>