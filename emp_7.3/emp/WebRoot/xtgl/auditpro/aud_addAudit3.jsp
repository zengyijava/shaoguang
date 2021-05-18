<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfFlow" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfFlowBindType" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfReviewer2level" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("auditpro");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String lguserid = (String)request.getAttribute("lguserid");
	String flowid = (String)request.getAttribute("flowid");
	
	LfFlow lfflow = (LfFlow)request.getAttribute("lfflow");
	@ SuppressWarnings("unchecked")
	List<LfFlowBindType> bindtypeList = (List<LfFlowBindType>)request.getAttribute("bindtypeList");
	@ SuppressWarnings("unchecked")
	List<LfReviewer2level> reviewerList = (List<LfReviewer2level>)request.getAttribute("reviewerList");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> LevelAmountMap = (LinkedHashMap<String, String>)request.getAttribute("LevelAmountMap");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> auditMap = (LinkedHashMap<String, String>)request.getAttribute("auditMap");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" />
	    <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/aud_addAudit3.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/aud_addAudit3.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="aud_addAudit3">

		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_cjshlc",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="aud_auditpro.htm?method=tofinishAudit" method="post" id="serForm">
				<input type="hidden" id="pathUrl" value="<%=path %>"/>
				<input type="hidden" id="ipath" value="<%=iPath %>"/>
				<input type="hidden" id="lguserid" value="<%=lguserid %>"/>
				<input type="hidden" id="flowid" value="<%=flowid %>"/>
				<div id="getloginUser" class="getloginUser">
				</div>
				<%-- 显示信息--%>
				<div id="singledetail" class="singledetail">
					<div id="msg" class="msg"></div>
				</div>
					
				<div class="serForm_div1">
					<table  class="audLinebgimg3 serForm_div1_table">
						<tr>
							<td class="serForm_div1_table_td1"></td>
							<td class="serForm_div1_table_td2"><b>1.</b><emp:message key="xtgl_spgl_shlcgl_cjshlc" defVal="创建审核流程" fileName="xtgl"/></td>
							<td class="serForm_div1_table_td3"><b>2.</b><emp:message key="xtgl_spgl_shlcgl_szbshdx" defVal="设置被审核对象" fileName="xtgl"/></td>
							<td class="serForm_div1_table_td4"><b>3.</b><emp:message key="xtgl_spgl_shlcgl_wc" defVal="完成" fileName="xtgl"/></td>
						</tr>
					</table>
				</div>
				
				<div class="serForm_div2" align="left">
					&nbsp;<b><emp:message key="xtgl_spgl_shlcgl_xjlcwc" defVal="新建流程及设置被审核对象完成！" fileName="xtgl"/></b>
				</div>
				<div class="itemDiv div_bd serForm_div3" >
					<table class="serForm_div3_table">
					   		<tr>
								<td  align="center"   class="div_bd div_bg serForm_div3_table_td1">
									<emp:message key="xtgl_spgl_shlcgl_lcmc" defVal="流程名称" fileName="xtgl"/>
								</td>
								<td align="left"  class="div_bd serForm_div3_table_td2" colspan="2">
									<%
										if(lfflow.getFTask() != null && !"".equals(lfflow.getFTask())){
											out.print(lfflow.getFTask());
										}else{
											out.print("-");
										}
									%>
								</td>
							</tr>
							<tr>
								<td  align="center"   class="div_bd div_bg serForm_div3_table_tr2_td1">
									<emp:message key="xtgl_spgl_shlcgl_shfw" defVal="审核范围" fileName="xtgl"/>
								</td>
								<td  align="left"  class="div_bd serForm_div3_table_tr2_td2"  colspan="2"> 
									<%
										if(bindtypeList != null && bindtypeList.size()>0){
											String typestr = "";
											for(int i=0;i<bindtypeList.size();i++){
												LfFlowBindType bindtype = bindtypeList.get(i);
												if(bindtype.getInfoType() == 1){
													typestr =  typestr + MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_dxfs",request)+"&nbsp;&nbsp;";
												}else if(bindtype.getInfoType() == 2){
													typestr =  typestr + MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_cxfs",request)+"&nbsp;&nbsp;";
												}else if(bindtype.getInfoType() == 3){
													typestr =  typestr + MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_dxmb",request)+"&nbsp;&nbsp;";
												}else if(bindtype.getInfoType() == 4){
													typestr =  typestr + MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_cxmb",request)+"&nbsp;&nbsp; ";
												}else if(bindtype.getInfoType() == 6){
													typestr =  typestr + MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_wxmb",request)+"&nbsp;&nbsp; ";
												}
											}
											out.print(typestr);
										}else{
											out.print("-");
										}
									%>
								</td>
							</tr>
							<tr>
								<td  align="center"  class="div_bd div_bg serForm_div3_table_tr3_td1">
									<emp:message key="xtgl_spgl_shlcgl_shjb" defVal="审核级别" fileName="xtgl"/>
								</td>
								<td  align="left"  class="div_bd serForm_div3_table_tr3_td2"  colspan="2"> 
									
										<%
											if(lfflow.getRLevelAmount() != null && !"".equals(lfflow.getRLevelAmount())){
												out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_g",request)+lfflow.getRLevelAmount()+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_jsp",request));
											}else{
												out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_wz",request));
											}
										%>
								</td>
							</tr>
							
							<% 
								//审批等级
								Integer rlevelamount = lfflow.getRLevelAmount();
								if(rlevelamount != null && !"".equals(rlevelamount)){
									for(int i=1;i<=rlevelamount;i++){
										String name = LevelAmountMap.get("LevelAmountID"+i);
										String showname = "";
										String showallname = "";
										if(name != null && !"".equals(name)){
											String[] arr = name.split("#");
											for(int m=0;m<arr.length;m++){
												if(m <= 2){
													showname = showname +  arr[m] + "&nbsp;&nbsp;&nbsp;";
												}
												if(m == 3){
													showname = showname + "...";
												}
												showallname = showallname + "&nbsp;&nbsp;" + arr[m];
											}
											
										}
										String auditinfo = auditMap.get("audit"+i);
										String[] typecondition = auditinfo.split("_");
										 //审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
										  //1全部通过生效    2第一人审核生效
										%>
											<tr>
												<td  align="center"  style="<%if(i==rlevelamount){out.print("border-bottom: 0");} %>" class="div_bd div_bg serForm_div3_table_tr4_td1" >
													<emp:message key="xtgl_spgl_shlcgl_d" defVal="第" fileName="xtgl"/><%=i %> <emp:message key="xtgl_spgl_shlcgl_jsp" defVal="级审批" fileName="xtgl"/>
												</td>
												<td  align="left"  title="<%=showallname %>"  style="<%if(i==rlevelamount){out.print("border-bottom: 0");} %>" class="div_bd serForm_div3_table_tr4_td2"> 
													 <%  
													 		if("5".equals(typecondition[0])){
													 			
													 			
													 			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_zjsp",request));
													 		}else if("1".equals(typecondition[0]) || "4".equals(typecondition[0])){
													 			out.print("<a onclick='javascript:shownamedialog(this)' class='serForm_div3_table_tr4_td2_a'><label class='serForm_div3_table_tr4_td2_label'><xmp>"+showallname+"</xmp></label>"+showname+"</a>");
													 		}else{
													 			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_wz",request));
													 		}
													 %>
													 
												</td>
												<td  align="left" style="<%if(i==rlevelamount){out.print("border-bottom: 0");} %>" class="div_bd serForm_div3_table_tr4_td3"> 
													 <%  
													 		if("1".equals(typecondition[1])){
													 			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_qbtgyx",request));
													 		}else if("2".equals(typecondition[1])){
													 			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_qzyrspsx",request));
													 		}else{
													 			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_wz",request));
													 		}
													 %>
												</td>
											</tr>
										<%
									}
								}
							%>
							
					</table>
				</div>
				
				<div class="itemDiv serForm_div4"  >
										<input type="button" id="upbtn" value="<emp:message key='xtgl_spgl_shlcgl_syb' defVal='上一步' fileName='xtgl'/>"  class="btnClass5 mr23 btnLetter3 indent_none" onclick="javascript:backUpdateInstallobj();" />&nbsp;&nbsp;
										<input type="button" id="savebtn"  value="<emp:message key='xtgl_spgl_shlcgl_bc' defVal='保存' fileName='xtgl'/>" class="btnClass6" onclick="javascript:backmgr();" />&nbsp;&nbsp;
										<br/>
				</div>
				
				
			</form>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addAudit3.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>