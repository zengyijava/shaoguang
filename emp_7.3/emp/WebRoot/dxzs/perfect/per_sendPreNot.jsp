<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page  import="com.montnets.emp.entity.biztype.LfBusManager"  %>
<%@ page import="com.montnets.emp.entity.dxzs.LfDfadvanced" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
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
	
	Integer depLevel = (Integer)request.getAttribute("depLevel");
	Long smsCount = (Long)request.getAttribute("smsCount");
	String jiFei = (String)request.getAttribute("jiFei");
	String name = (String)request.getAttribute("name");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String lguserid = (String)request.getAttribute("lguserid");
	
	String perfectTaskId = (String)request.getAttribute("perfectTaskId");
	
	
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>) request
			.getAttribute("busList");
	@SuppressWarnings("unchecked")
	List<Userdata> userData = (List<Userdata>) request
			.getAttribute("userData");
	
	//高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
    		
	String spName = StaticValue.SMSACCOUNT;
	spName = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_99",request);
	  String skin = session.getAttribute("stlyeSkin")==null?"default":
			(String)session.getAttribute("stlyeSkin");
	String perfectSignName = DxzsStaticValue.getPERFECT_SIGN_NAME();
	String isAlert=request.getParameter("isAlert");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="dxzs_xtnrqf_title_116" defVal="发送完美通知" fileName="dxzs"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=iPath%>/css/pernotice.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<style type="text/css">
				div#eq_sendDiv,div#eq_sendDiv{width: 620px;}
				#batchFileSend span{width: 90px;}
				div#effinfo{width: 280px;}
				#showeffinfo p{width: 120px;background-position: 110px 8px;}
				div#effinfo table tr td:nth-child(1){width: 120px;}
				td#predowninfo{padding-left: 10px;}
			</style>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/per_sendPreNot.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_WanMeiTongZhi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<script>
		var base_path = "<%=path%>";
		var perfectSignName = "<%=perfectSignName%>";
		var isAlert="<%=isAlert%>";
	</script>
	<body id="sendPreNot">
		<div id="container" class="container" >
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<div id="getloginUser" class="dxzs_common_1">
			</div>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">    
				<input id="formName" value="form3" type="hidden"/>
				<input id="cpath" value="<%=path %>" type="hidden"/>
				<input id="depLevel" value="<%=depLevel%>" type="hidden"/>
				<input id="smsCount" value="<%=smsCount%>" type="hidden"/>
				<input id="jiFei" value="<%=jiFei%>" type="hidden"/>
				<input id="spUserFeeFlag" value="" type="hidden"/>
				<input id="path" value="<%=inheritPath %>" type="hidden"/>
				<input id="fsName" value="<%=spName %>" type="hidden"/>
			    <input  type="hidden" id="gpOrDepOrPerName" name="gpOrDepOrPerName" value=""/>
				<input type="hidden" id="sendtype" value="" />
    			<input type="hidden" id="receiveId" value=""/>
    			<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
    			<input type="hidden" id="skin" value="<%=skin %>"/>
    			<input id="name" name="name" value="<%=name %>" type="hidden"/>
    		
    			<input type="hidden" id="error" name="error" value="" />
    			
				<%--表示是否有选择对象的那一行      1表示有  2表示没有 --%>
				<input type="hidden" id="havOne"  name="havOne" value="2"/>
	
			      <div id="batchFileSend"  class="block">
			            <form name="form3" action="per_previewSMS.htm?method=priviewNotice" method="post"
							enctype="multipart/form-data" target="hidden_iframe">
			                <input type="hidden" value ="0" id="isOk" name="isOk"/>
			                	<%-- 	所选择的对象    机构ID 群组ID  员工ID  自定义ID--%>
							<input id="depIds" name="depIds" value="" type="hidden"/>
							<input id="groupIds" name="groupIds" value="" type="hidden"/>
							<input id="empUserIds" name="empUserIds" value="" type="hidden"/>
							<input id="deUserIds" name="deUserIds" value="" type="hidden"/>
							
							<input id="depIdsTemp" name="depIdsTemp" value="" type="hidden"/>
							<input id="groupIdsTemp" name="groupIdsTemp" value="" type="hidden"/>
							<input id="empUserIdsTemp" name="empUserIdsTemp" value="" type="hidden"/>
							<input id="deUserIdsTemp" name="deUserIdsTemp" value="" type="hidden"/>
							<%--新增单个手机号码 的号码集合  --%>
							<input type="hidden" id="phoneStr" name="phoneStr" value=""/>
							<%-- 完美通知任务ID--%>
    						<input id="perfectTaskId" name="perfectTaskId" value="<%=perfectTaskId %>" type="hidden"/>
    						<%-- 保存手工输入的名字+手机号码 --%>
    						<input id="handphonename" name="handphonename" value="" type="hidden"/>
    						<input id="usedSubno" value="" type="hidden" name="usedSubno"/>
			               <div id="eq_sendDiv">
								<span><emp:message key="dxzs_xtnrqf_title_130" defVal="时间间隔" fileName="dxzs"/>：</span>
									<select id="sdInterval" name="sendInterval"  class="input_bd">
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
								<span><emp:message key="dxzs_xtnrqf_title_132" defVal="发送次数" fileName="dxzs"/>：</span>
										<select id="sdCount" name="sdCount"  class="input_bd">
													     <option value="1">1<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="2">2<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="3" selected="selected"> 3<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="4">4<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
													     <option value="5">5<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
										</select>
							</div>
							<%--
							<div id="eq_sendDiv" style="float: left;">
								<span><b>接收对象</b>：</span>
								<input id="showflowname" name="showflowname" type="button" value="选择人员" onclick="javascript:showInfo();" class="btnClass3"/>
								<font id="pernoticCount"></font>
							</div>--%>
							<div id="eq_sendDiv">
									<span><emp:message key="dxzs_xtnrqf_title_117" defVal="接收对象" fileName="dxzs"/>：</span>
									<div id="getObject" class="div_bd">
										<table id="infomaTable" class="dxzs_table">
											<tr id="first" class="title_bg">
												<td width="15%"  class="div_bd td1" align="center"><emp:message key="dxzs_xtnrqf_title_4" defVal="类型" fileName="dxzs"/></td>
												<td width="27%"  class="div_bd td2" align="center" ><emp:message key="dxzs_xtnrqf_title_19" defVal="姓名" fileName="dxzs"/></td>
												<td width="45%"  class="div_bd td2" align="center" ><emp:message key="dxzs_xtnrqf_title_5" defVal="号码" fileName="dxzs"/></td>
												<td width="13%"  class="div_bd td3" align="center"><emp:message key="dxzs_xtnrqf_title_6" defVal="操作" fileName="dxzs"/></td>
											</tr>
										</table>
									</div>
									<div class="emp_btn_table div_bg" id="emp_btn_table" >
									<table width="100%" class="div_bg td1">
											<tr id="picTab">
												<td align="center"  class="div_bd td4" onclick="javascript:showInfo();">
												<img src="<%=skin %>/images/selectEmp.png"/><div class="mt10"><emp:message key="dxzs_xtnrqf_title_7" defVal="选择人员" fileName="dxzs"/></div></td>
											</tr>
											<tr>
												<td align="center" class="div_bd">
													<table width="182px">
													<tr>
														<td width="50px" class="div_bd">
														<div class="dxzs_position_relative">
															<input value="" name="tempname" id="tempname" class="graytext input1" onkeyup="checkText($(this),'2')" type="text" maxlength="8"/>
															<label for="tempname" class="placeholder adName_la" id="idPlaceholder1"><emp:message key="dxzs_xtnrqf_title_19" defVal="姓名" fileName="dxzs"/></label>
														</div>
														</td>
														<td width="85px" class="div_bd">
														  <div class="dxzs_position_relative">
															<input value=""  class="graytext input2" type="text"
															onkeyup="checkText($(this),'1')"
															onpaste="phoneInputCtrl($(this))"
															maxlength="21" name="tph" id="tph" onpropertychange="addphone('1');"/>
															<label for="tph"  class="placeholder adPhone_la" id="idPlaceholder2"><emp:message key="dxzs_xtnrqf_title_119" defVal="手机号码" fileName="dxzs"/></label>
														  </div>	
														</td>
														<td align="center" onclick="addphone('2');"  class="div_bd td5"><emp:message key="dxzs_xtnrqf_title_10" defVal="添加" fileName="dxzs"/></td>
													</tr>
													</table>
												</td>
											</tr>
										</table>
									</div>
								</div>
							
							
			                 <div id="eq_sendDiv" class="mt10">
								<span><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>：</span>
										<textarea class="div_bd textarea" name="msg"  rows="6"
											id="contents"  onblur="noticEblur($(this))"></textarea>
									<p class='<%="zh_HK".equals(empLangName)?"dxzs_p1":"dxzs_p2"%>'>
										<label id="strlen"> 0</label>
										<label id="maxLen">
											/ 965
										</label>
									</p>
							</div> 
							
					 <div id="eq_sendDiv2" class="eq_sendDiv2">
						<span id="u_o_c_explain" class="div_bg"><b><emp:message key="dxzs_xtnrqf_title_28" defVal="高级设置" fileName="dxzs"/></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
							<div id="moreSelect"  class="div_bg moreSelect">
							<div  align="right" class="dxzs_div">
								<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="dxzs_xtnrqf_title_29" defVal="选项存为默认" fileName="dxzs"/></a>
							</div>
							<table class="dxzs_common_table">
								<tr>
									<td class="dxzs_td">
										<%=spName %>：
									</td>
									<td>
										<select id="spUser" name="spUser"  class="input_bd">
			                 					<% 
			                 						if(userData != null && userData.size()>0){
			                 							Userdata user= null;
			                 							String spUserId = lfDfadvanced != null?lfDfadvanced.getSpuserid():"";
			                 							for(int i=0;i<userData.size();i++){
			                 							user = userData.get(i);
			                 					%>
			                 							<option value="<%=user.getUserId()%>" 
			                 							<%=spUserId != null && !"".equals(spUserId) && spUserId.equals(user.getUserId())?"selected":"" %>>
			                 							<%=user.getUserId() %>(<%=user.getStaffName() %>)</option>
			                 					<% 
			                 							}
			                 						}
			                 					%>
			                 			</select>
									</td>
								</tr>
								<tr>
									<td class="dxzs_td">
										<emp:message key="dxzs_xtnrqf_title_30" defVal="业务类型" fileName="dxzs"/>：
									</td>
									<td>
										<select id="busCode"  name="busCode"  class="input_bd">
												<%
													if (busList != null && busList.size() > 0) {
														String busCode = lfDfadvanced != null?lfDfadvanced.getBuscode():"";
														for(LfBusManager busManager:busList){
												%>
													<option value="<%=busManager.getBusCode()%>" 
															<%=busCode != null && !"".equals(busCode) && busCode.equals(busManager.getBusCode())?"selected":"" %>>
															<%String busName = busManager.getBusName().replace("默认业务", MessageUtils.extractMessage("ydwx","ydwx_defaultBussiness",request));%>
															<%=busName.replace("<","&lt;").replace(">","&gt;") + "("+ busManager.getBusCode() +")"%>
													</option>
												<%
														}
													}
												%>
										</select>
									</td>
								</tr>
								<tr height="1px;">
									<td colspan="2">
									 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
							</table>
							</div>
						</div>
							
							<div class="clear2"></div>
							<div class="b_F_btn">
								<input id="subSend" class="btnClass5 mr23" type="button" onclick="previewNotice()" value="<emp:message key='dxzs_xtnrqf_button_2' defVal='提交' fileName='dxzs'/>"/>
								<%--
								<input id="subSend" class="btnClass5 mr23" type="button" onclick="sendPerNotice()" value="提交"/>
								 --%>
								<input id="qingkong" onclick="javascript:cancelInfo();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_3' defVal='重置' fileName='dxzs'/>" >
							</div>
			                
					</form>
				</div>
			</div>
			<%-- 内容结束 --%>
			<div id="infoDiv" title="<emp:message key='dxzs_xtnrqf_title_40' defVal='选择发送对象' fileName='dxzs'/>" class ="dxzs_display_none">
					<iframe id="flowFrame" name="flowFrame" src="<%=iPath%>/per_sendNotSelUser.jsp?lgcorpcode=<%=lgcorpcode %>&lguserid=<%=lguserid %>"  
						class="dxzsFlowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
					<input type="hidden" value="<%=skin %>" id="skin">
					<input type="hidden" value="" id="empIds">
					<input type="hidden" value="" id="groupIds">
					<input type="hidden" value="" id="empDepIds">
					<input type="hidden" value="" id="malIds">
                    <input type="hidden" value="" id="nums">
					<input type="hidden" value="" id="userMoblieStr">
					<input id="lguserid" type="hidden" name="lguserid" value="<%=lguserid %>" />
					<input id="lgcorpcode" type="hidden" name="lgcorpcode" value="<%=lgcorpcode %>" />
					<input id="commonPath" type="hidden" name="commonPath" value="<%=commonPath %>" />
					<%--用于数据回显--%>
                    <div title="" class="display_none" id="rightSelectedUserOption"></div>
					<table>
						<tr>
							<td class="dxzs_td1" align="center">
								<input type="button"  value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass5 mr23" onclick="javascript:doOk()" />
								<input type="button"  value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空 ' fileName='dxzs'/>" class="btnClass6" onclick="javascript:doNo()" />
								<br/>
							</td>
						</tr>
					</table>
			</div>
			
			<div id="detail_Info" class="detail_Info">
						<input type="hidden" value="" id="employeeguids" name="employeeguids"/>	
						<input type="hidden" value="" id="malistguids" name="malistguids"/>	
						<input type="hidden" value="" id="phoneurl" name="phoneurl"/>	
						<table id="infos" class="infos">
						  <tr >
						    <td><emp:message key="dxzs_xtnrqf_title_142" defVal="预发送条数" fileName="dxzs"/>：<span id="yct"></span></td>								    
						    <td id="showyct" ><emp:message key="dxzs_xtnrqf_title_42" defVal="机构余额" fileName="dxzs"/>：<span id="ct"></span></td>								    
						    <td id="showSpFee" ><%=spName %><emp:message key="dxzs_xtnrqf_title_42" defVal="机构余额" fileName="dxzs"/>：<span id="spanSpFee"></span></td>
						  </tr>
						  <tr>
								<td colspan="3">
									<emp:message key="dxzs_xtnrqf_title_112" defVal="发送规则" fileName="dxzs"/>：&nbsp;&nbsp;
									<emp:message key="dxzs_xtnrqf_title_113" defVal="若未回复,每隔" fileName="dxzs"/><span id="preMin"></span><emp:message key="dxzs_xtnrqf_title_114" defVal="分钟再发一次,最多发" fileName="dxzs"/><span id="preCount"></span><emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/>.
								</td>
						 </tr>
						  
					 	 <tr id="notsend">
							<td  align="left" colspan="3" id="nosendReason" class="nosendReason">
							</td>
						 </tr>
						  <tr>
							  <td><emp:message key="dxzs_xtnrqf_title_45" defVal="有效号码数" fileName="dxzs"/>：<span id="effs"></span></td>
							  <td><emp:message key="dxzs_xtnrqf_title_46" defVal="提交号码数" fileName="dxzs"/>：<span id="counts"></span></td>								  
							  <td align="center">
							   <div id="showeffinfo" class="div_bg">
							       <p class="dxzs_p3"><emp:message key="dxzs_xtnrqf_title_47" defVal="无效号码" fileName="dxzs"/>(<span id="alleff" class="alleff"></span>)&nbsp;&nbsp;
							  			 <a id="arrowhead"  class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a>
							   		</p> 
							   </div>
							  </td>
						  </tr>
						</table>
						<div id="effinfo" class="div_bg">
						   <center>
						    <table id="effinfotable">
                                    <tr height="10px;">
                                      <td colspan="3"></td>
                                    </tr>
							      <tr>
							      <td align="left"><emp:message key="dxzs_xtnrqf_title_48" defVal="黑名单号码：" fileName="dxzs"/></td>
								  <td class="showNum"><span id="blacks"></span></td>
							      <td id="preinfonum">
							      <%
									if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) 
									{
								  %>
							      	<a href="javascript:uploadbadFiles();"><emp:message key="dxzs_xtnrqf_title_49" defVal="详情下载" fileName="dxzs"/></a>
							      <%
							      	}
							      %>
							      <input type="hidden" id="badurl" name="badurl"  value=""></input>
							      </td>
						      </tr>
						      <tr>
							      <td align="left"><emp:message key="dxzs_xtnrqf_title_50" defVal="重复号码：" fileName="dxzs"/></td>
								  <td><span id="sames"></span></td>
							      <td></td>
						      </tr>
						      <tr>
						          <td align="left"><emp:message key="dxzs_xtnrqf_title_51" defVal="格式非法：" fileName="dxzs"/></td>
								  <td><span id="legers"></span></td>
						          <td></td>
						      </tr>
						    </table>
						    </center>
					    </div>
					    <div class="dxzs_div1"></div>	
						<p class="dxzs_p4">
							<emp:message key="dxzs_xtnrqf_title_52" defVal="部分预览" fileName="dxzs"/>：
						</p>
						<div id="maindiv" class="maindiv">
							<center>
								<table id="content" class="dxzs_content">
								</table>
							</center>
						</div>
				    	<div id="footer" class="dxzs_footer">
				    	  <center>
				    		<input id="btsend" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_6' defVal='发送' fileName='dxzs'/>" onclick="javascript:send()"/>
						    <input id="btcancel" onclick="javascript:precel()" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>"/>
						    <br/>
						  </center> 
				    	</div>	
			</div>
			<div id="probar" class="dxzs_display_none">
				<p class="dxzs_p5">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="dxzs_xtnrqf_title_63" defVal="处理中,请稍等" fileName="dxzs"/>.....
				</p>
				<div id="shows">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<img class="dxzs_img"
						src="<%=commonPath%>/common/img/loader.gif" />
				</div>
			</div>
			
				<iframe name="hidden_iframe" id="hidden_iframe"
					class="dxzs_display_none"></iframe>
		
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div id="message" title="<emp:message key='dxzs_xtnrqf_title_73' defVal='提示' fileName='dxzs'/>" class="dxzs_message">
			<div class="dxzs_div2"></div>
	 		<center>
			<label ><emp:message key="dxzs_xtnrqf_title_71" defVal="发送至网关成功！" fileName="dxzs"/></label>
			<a href="javascript:sendRecord('<%=perfectTaskId %>')"  class="alink" ><emp:message key="dxzs_xtnrqf_title_74" defVal="查看发送记录" fileName="dxzs"/></a>
			</center>
			<br>
			<div class="dxzs_div3"></div>
			 <center>
			    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_10' defVal='关闭' fileName='dxzs'/>" />
			</center> 
		</div>
		<div class="clear2"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=iPath %>/js/pernotice.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		
		
	</body>
</html>
