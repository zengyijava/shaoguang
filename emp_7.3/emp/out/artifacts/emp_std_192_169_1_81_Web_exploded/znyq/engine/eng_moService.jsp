<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.engine.vo.LfServiceVo"%>
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
		
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	List<LfServiceVo> serList=(List<LfServiceVo>)request.getAttribute("serList");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	menuCode = menuCode==null?"0-0-0":menuCode;

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
    LfServiceVo lsv = (LfServiceVo)request.getAttribute("LfSerVo");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />

		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	</head>
	<body onload="show()" id="eng_moService" class="eng_moService">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="<%=path %>/eng_moService.htm?method=find" method="post"
					id="pageForm">
					<div id="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null){%>
						<a id="add" onclick="window.location.href='eng_moService.htm?method=doEdit&workType=waterLine&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()"><emp:message key="znyq_ywgl_common_btn_4" defVal="新建" fileName="znyq"></emp:message></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-5")!=null){%>
						<a id="add" onclick="toMoConf()"><emp:message key="znyq_ywgl_common_btn_21" defVal="设置" fileName="znyq"></emp:message></a>
						<%} %>
					</div>
					<div id="condition">
						<table>
									<tr>
									<td><emp:message key="znyq_ywgl_sxywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></td>
										<td>
											<input type="text" name="serName" id="serName"  value="<%=lsv.getSerName()==null?"":lsv.getSerName() %>" />
										</td>
										<td><emp:message key="znyq_ywgl_sxywgl_cjzmc_mh" defVal="创建者名称：" fileName="znyq"></emp:message></td>
										<td>
											<input type="text" name="sOpName" id="sOpName"  value="<%=lsv.getName()==null?"":lsv.getName() %>" maxlength="20"/>
										</td>
										
										<td><emp:message key="znyq_ywgl_sxywgl_yxzt_mh" defVal="运行状态：" fileName="znyq"></emp:message></td>
										<td>
											<select id="serState" name="serState" isInput="false">
											   <%int ss = lsv.getRunState() == null?2:lsv.getRunState(); %>
											    <option value=""><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
											    <option value="1" <%=ss==1?"selected":"" %>><emp:message key="znyq_ywgl_sxywgl_qy" defVal="启用" fileName="znyq"></emp:message></option>
											    <option value="0" <%=ss==0?"selected":"" %>><emp:message key="znyq_ywgl_sxywgl_ty" defVal="停用" fileName="znyq"></emp:message></option>
											</select>
										</td>
										<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
									</tr>
									<tr>
										<td><emp:message key="znyq_ywgl_sxywgl_zldm_mh" defVal="指令代码：" fileName="znyq"></emp:message></td>
										<td>
											<input type="text"  name="orderCode" id="orderCode" value="<%=lsv.getOrderCode()==null?"":lsv.getOrderCode() %>"/>
										</td>
										<td><emp:message key="znyq_ywgl_sxywgl_whzt_mh" defVal="尾号状态：" fileName="znyq"></emp:message></td>
										<td>
											<select id="identifyMode" name="identifyMode" isInput="false">
											   <%int identifyMode = lsv.getIdentifyMode() == null?0:lsv.getIdentifyMode(); %>
											    <option value=""><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
											    <option value="1" <%=identifyMode==1?"selected":"" %>><emp:message key="znyq_ywgl_sxywgl_qy" defVal="启用" fileName="znyq"></emp:message></option>
											    <option value="2" <%=identifyMode==2?"selected":"" %>><emp:message key="znyq_ywgl_sxywgl_ty" defVal="停用" fileName="znyq"></emp:message></option>
											</select>
										</td>
										<td></td><td></td><td></td>
									</tr>
									<tr class="hidden"></tr>
								</table>
					</div>
					<table id="content" class="content_table">
						<thead>
								<tr>
								<th>
										<emp:message key="znyq_ywgl_sxywgl_xh" defVal="序号" fileName="znyq"></emp:message>
									</th> 
									<th>
										<emp:message key="znyq_ywgl_sxywgl_ywmc" defVal="业务名称" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_sxywgl_cjzmc" defVal="创建者名称" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_sxywgl_cjsj" defVal="创建时间" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_sxywgl_zldm" defVal="指令代码" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_sxywgl_whzt" defVal="尾号状态" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_sxywgl_yxzt" defVal="运行状态" fileName="znyq"></emp:message>
									</th>
									<%if(btnMap.get(menuCode+"-3")!=null
										|| btnMap.get(menuCode+"-4")!=null
										|| btnMap.get(menuCode+"-2")!=null)                       		
									{%>
									<th colspan="3">
										<emp:message key="znyq_ywgl_sxywgl_czgl" defVal="操作管理" fileName="znyq"></emp:message>
									</th>
									<%} %>
								</tr>
							</thead>
							<tbody>
							<%
							if (serList != null && serList.size() > 0)
							{
								for(LfServiceVo service : serList)
								{
							 %>
								<tr>
							<td>
									<%=service.getSerId() %>
								</td> 
								<td class="textalign" >
								<xmp id="firstXmp">
									<%=service.getSerName() %>
								</xmp>
								</td>
								<td class="textalign" >
									<%--<%=service.getName()==null?"":service.getName() %><%if(service.getUserState() !=null && service.getUserState()==2){out.print("<font color='red'>(已注销)</font>");} %>
									(<%=service.getUserName()==null?"-":service.getUserName()%><%if(service.getUserState() !=null && service.getUserState()==2){out.print("<font color='red'>已注销</font>");} %>) --%>
									<%=service.getName()==null?"":service.getName() %><%if(service.getUserState() !=null && service.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_yzx",request)+")</font>");} %>
									(<%=service.getUserName()==null?"-":service.getUserName()%><%if(service.getUserState() !=null && service.getUserState()==2){out.print("<font color='red'>"+MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_yzx",request)+"</font>");} %>)
								</td>
								<td>
									<%
								if (service.getCreateTime() != null && !"".equals(service.getCreateTime().toString()))
								{
							    %>
								<%=df.format(service.getCreateTime()) %>
								<%}%>
								</td>
								<td>
									<%=service.getOrderCode()==null?"":service.getOrderCode() %>
								</td>
								<td>
									<%--<%=service.getIdentifyMode()!=null && service.getIdentifyMode()== 2?"停用":"启用" %>
									--%>
									<%=service.getIdentifyMode()!=null && service.getIdentifyMode()== 2?MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_ty",request):MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_qy",request) %>
								</td>
								<% if(btnMap.get(menuCode+"-3")!=null){%>
								<td class="ztalign">
								<center>
								<div class="setControl"></div>
								<%if(service.getRunState()-1 == 0){
								%>
									<select  name="runState<%=service.getSerIdCipher() %>" id="runState<%=service.getSerIdCipher() %>" data="<%=service.getSerIdCipher() %>" class="input_bd" onchange="javascript:changestate('<%=service.getSerIdCipher()%>')">
							          <option value="1" selected="selected"><emp:message key="znyq_ywgl_sxywgl_yqy" defVal="已启用" fileName="znyq"></emp:message></option>
							          <option value="0" ><emp:message key="znyq_ywgl_sxywgl_ty" defVal="停用" fileName="znyq"></emp:message></option>
							        </select>
								<%
								}else{
								%>
									<select  name="runState<%=service.getSerIdCipher() %>" id="runState<%=service.getSerIdCipher() %>" data="<%=service.getSerIdCipher() %>" class="input_bd" onchange="javascript:changestate('<%=service.getSerIdCipher() %>')">
							          <option value="0" selected="selected"><emp:message key="znyq_ywgl_sxywgl_yty" defVal="已停用" fileName="znyq"></emp:message></option>
							          <option value="1" ><emp:message key="znyq_ywgl_sxywgl_qy" defVal="启用" fileName="znyq"></emp:message></option>									          
							        </select>
								<%	
								}
								%>
								</center>
								</td>
								<%}else{ %>
								<td class="ztalign" >
								<%
			                    if(service.getRunState()==1)
								{
									//out.print("已启用");
									out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_yqy",request));
								}
			                    else if(service.getRunState()==0)
								{
									//out.print("已禁用");
									out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_sxywgl_yjy",request));
								}
								%>
								</td>
								<%} %>
								<% if(btnMap.get(menuCode+"-3")!=null){%>
								<td>
									
									<a onclick="getMoInfos('<%=service.getSerIdCipher() %>')"><emp:message key="znyq_ywgl_sxywgl_ywxx" defVal="业务信息" fileName="znyq"></emp:message></a>
									
								</td>
								<%} %>
							<%-- 	<td>
									<a href="<%=path%>addTrigger.jsp?serId=<%=service.getSerId() %>&serName=<%=service.getSerName() %>">触发条件</a>
								</td> --%>
								<% if(btnMap.get(menuCode+"-4")!=null){%>
								<td>
									
									<a href="<%=path%>/eng_moService.htm?method=process&serId=<%=service.getSerIdCipher() %>"><emp:message key="znyq_ywgl_sxywgl_bzgl" defVal="步骤管理" fileName="znyq"></emp:message></a>
									
								</td>
								<%} %>
								<% if(btnMap.get(menuCode+"-2")!=null){%>
								<td>
									
									<a href="javascript:deleteSer('<%=service.getSerIdCipher() %>','<%=service.getSerName() %>','<%=service.getRunState() %>')"><emp:message key="znyq_ywgl_common_text_8" defVal="删除" fileName="znyq"></emp:message></a>
									
								</td>
								<%} %>
							</tr>
								<%}
									}
							else{
								 %>
								 <tr><td colspan="10" align="center"><emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message></td></tr>
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
					</form>
					
					<div id="MoInfos" title="<emp:message key="znyq_ywgl_sxywgl_ywxx" defVal="业务信息" fileName="znyq"></emp:message>">
						<iframe id="serInfoflowFrame" name="serInfoflowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
					</div>
			</div>
			<%} %>
			<div id="moConfDiv" title="<emp:message key="znyq_ywgl_common_btn_21" defVal="设置" fileName="znyq"></emp:message>">
			<center>
				<iframe id="moConfFrame" name="moConfFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</center>
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
		<script language="javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			//var test= getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_sxywpzcg");
			//alert(test);
			//$("#content select").empSelect({width:80});
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
		
			$('#MoInfos').dialog({
				autoOpen: false,
				height: 450,
				width:520,
				modal:true
			});
			
			$('#moConfDiv').dialog({
				autoOpen: false,
				height: 370,
				width:500,
				modal:true
			});
		});
		
		
		function show(){
			<% String result=(String)request.getAttribute("createResult");
			   if(result!=null && "success" == result){%>
			   //alert("上行业务配置成功！");
			   alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_sxywpzcg"));
			   window.location.href='eng_moService.htm?lgcorpcode='+$('#lgcorpcode').val()+'&lguserid='+$('#lguserid').val();
			<% }%>
		}
		
		
</script>
		
	</body>
</html>