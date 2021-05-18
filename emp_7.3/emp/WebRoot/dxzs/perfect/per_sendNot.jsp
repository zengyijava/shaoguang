<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page  import="java.util.Map"  %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.tailnumber.LfSubnoAllot"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("sendNot");
	
	//LfSubnoAllot subnoAllot = (LfSubnoAllot)request.getAttribute("subnoAllot");
	//String isValidate = (String)request.getAttribute("isValidate");
	Integer depLevel = (Integer)request.getAttribute("depLevel");
	Long smsCount = (Long)request.getAttribute("smsCount");
	String jiFei = (String)request.getAttribute("jiFei");
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>) request
			.getAttribute("busList");
	@SuppressWarnings("unchecked")
	List<Userdata> userData = (List<Userdata>) request
			.getAttribute("userData");
			
	String spName = StaticValue.SMSACCOUNT;
	  String skin = session.getAttribute("stlyeSkin")==null?"default":
			(String)session.getAttribute("stlyeSkin");
	//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_116" defVal="发送完美通知" fileName="dxzs"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/pernotice.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style type="text/css">
			#sendNot #sdInterval,#sdCount{
				height: 22px;
			}
			#sendNot .div_bd{
				width:490px;
				font-size: 12px;
				resize:none;
				height: 80px;
			}
			#sendNot .dxzs_p{
				padding-left:75px;
				padding-top: 5px;
			}
			#sendNot #eq_sendDiv2{
				float:left;
				margin-top:10px;
			}
			#sendNot #moreSelect{
				height: 80px
			}
			#sendNot .dxzs_td{
				width:108px;
			}
			#sendNot #spUser,#busCode{
				width:240px;vertical-align:middle;height: 22px;
			}
			#sendNot .dxzs_font{
				padding-left:35px;
			}
			#sendNot .b_F_btn{
				float:left;padding-right:300px
			}
			#sendNot #flowFrame{
				width:530px;height:465px;border: 0;
			}
			#sendNot .dxzs_td1{
				width:530px;color:#CCCCCC;
			}
			#sendNot .dxzs_td2{
				width:530px;
			}
			#sendNot .unfold{
				text-decoration: none
			}
		</style>
	</head>
	<body id="sendNot">
		<div id="container" class="container" >
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<div id="getloginUser" class="dxzs_common_1">
			</div>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">    
				<input id="formName" value="form3" type="hidden"/>
				<input id="cpath" value="<%=path %>" type="hidden"/>
				<input id="usedSubno" value="" type="hidden" name="usedSubno"/>
				<input id="depLevel" value="<%=depLevel%>" type="hidden"/>
				<input id="smsCount" value="<%=smsCount%>" type="hidden"/>
				<input id="jiFei" value="<%=jiFei%>" type="hidden"/>
				<input id="path" value="<%=inheritPath %>" type="hidden"/>
				<input id="fsName" value="<%=spName %>" type="hidden"/>
				<%-- 	所选择的对象    机构ID 群组ID  员工ID  自定义ID--%>
				<input id="depIds" value="" type="hidden"/>
				<input id="groupIds" value="" type="hidden"/>
				<input id="empUserIds" value="" type="hidden"/>
				<input id="deUserIds" value="" type="hidden"/>
			    <input  type="hidden" id="gpOrDepOrPerName" name="gpOrDepOrPerName" value=""/>
				<input type="hidden" id="sendtype" value="" />
    			<input type="hidden" id="receiveId" value=""/>
    			<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
    			
	
			      <div id="batchFileSend"  class="block">
			            <form name="form3" method="post" >
			                <input type="hidden" value ="0" id="isOk" name="isOk"/>
			                
			               <div id="eq_sendDiv">
								<span><b><emp:message key="dxzs_xtnrqf_title_130" defVal="时间间隔" fileName="dxzs"/></b>：</span>
								
									<select id="sdInterval" name="sendInterval">
												     <option value="3">3<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
												     <option value="4">4<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
												     <option value="5" selected="selected">5<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
												     <option value="6">6<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
												     <option value="7">7<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
												     <option value="8">8<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
												     <option value="9">9<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
												     <option value="10">10<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
									 </select>
							
							</div>
			                
			                
			                
			                <div id="eq_sendDiv">
								<span><b><emp:message key="dxzs_xtnrqf_title_132" defVal="发送次数" fileName="dxzs"/></b>：</span>
										<select id="sdCount" name="sendCount" onchange="javascript:checkFee()">
													     <option value="1">1<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="2">2<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="3" selected="selected"> 3<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="4">4<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="5">5<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
										</select>
							</div>
							
							<div id="eq_sendDiv" class="dxzs_float_left">
								<span><b><emp:message key="dxzs_xtnrqf_title_117" defVal="接收对象" fileName="dxzs"/></b>：</span>
								<input id="showflowname" name="showflowname" type="button" value="<emp:message key='dxzs_xtnrqf_title_7' defVal='选择人员' fileName='dxzs'/>" onclick="javascript:showInfo();" class="btnClass3"/>
								<font id="pernoticCount"></font>
							</div>
			                
			                 <div id="eq_sendDiv">
								<span><b><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/></b>：</span>
										<textarea class="div_bd" name="msg" rows="6"
											id="contents"  onblur="noticEblur($(this))"></textarea>
									<p class="dxzs_p">	
										<label id="strlen"> 0</label>
										<label id="maxLen">
											/ 955
										</label>
									</p>
							</div> 
							
					 <div id="eq_sendDiv2">
						<span id="u_o_c_explain" class="div_bg"><b><emp:message key="dxzs_xtnrqf_title_28" defVal="高级设置" fileName="dxzs"/></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
							<div id="moreSelect" class="div_bg">
							<table class="dxzs_common_table">
								<tr><td class="dxzs_td"><font class="dxzs_font"><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>：</font></td>
									<td>
										<select id="spUser" name="spUser">
			                 					<% 
			                 						if(userData != null && userData.size()>0){
			                 							Userdata user= null;
			                 							for(int i=0;i<userData.size();i++){
			                 							user = userData.get(i);
			                 					%>
			                 							<option value="<%=user.getUserId()%>"><%=user.getUserId() %>(<%=user.getStaffName() %>)</option>
			                 					<% 
			                 							}
			                 						}
			                 					%>
			                 			</select>
									</td>
								</tr>
								<tr><td class="dxzs_td"><font class="dxzs_font"><emp:message key="dxzs_xtnrqf_title_30" defVal="业务类型" fileName="dxzs"/>：</font></td>
									<td>
										<select id="busCode"  name="busCode">
												<%
													if (busList != null && busList.size() > 0) {
														for (LfBusManager busManager : busList) {
												%>
													<option value="<%=busManager.getBusCode()%>">
															<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;")%>
													</option>
												<%
													}
													}
												%>
										</select>
									</td>
								</tr>
							</table>
							</div>
						</div>
							
							<div class="clear2"></div>
							<div class="b_F_btn">
								<input id="subSend" class="btnClass5 mr23" type="button" onclick="sendPerNotice()" value="<emp:message key='dxzs_xtnrqf_button_2' defVal='提交' fileName='dxzs'/>"/>
								<input id="qingkong"  type="reset" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" onclick="cancelInfo()" class="btnClass6" value="<emp:message key='dxzs_xtnrqf_button_3' defVal='重  置' fileName='dxzs'/>" />
							</div>
			                
					</form>
				</div>
			</div>
			<%-- 内容结束 --%>
			<div id="infoDiv" title="选择发送对象<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>">
					<iframe id="flowFrame" name="flowFrame" src="<%=iPath%>/per_sendNotSelUser.jsp"  marginwidth="0" scrolling="no" frameborder="no"></iframe>
					<table>
						<tr>
							<td class="dxzs_td1" align="left">
									&nbsp;&nbsp;<emp:message key="dxzs_xtnrqf_title_141" defVal="内容 " fileName="dxzs"/>1000
								
							</td>
						</tr>
						<tr>
							<td class="dxzs_td2" align="center">
								<input type="button"  value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass5 mr23" onclick="javascript:doOk()" />
								<input type="button"  value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/> " class="btnClass6" onclick="javascript:doNo()" />
							</td>
						</tr>
					</table>
			</div>
			
		
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear2"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script src="<%=iPath %>/js/sendNot.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	</body>
</html>
