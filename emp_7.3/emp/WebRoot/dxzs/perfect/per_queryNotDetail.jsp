<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.perfect.vo.PerfectNoticUpVo" %>
<%@ page import="com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	
	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<PerfectNoticUpVo> pnoticeVos =(List<PerfectNoticUpVo>) request.getAttribute("noticUpVos");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("queryNotHis");
	
	//不需要隐藏号码
	String isHideNumber = "1";
	if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) { 
		isHideNumber = "2";
	}
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	String conUserName = (String)request.getAttribute("username");	
	String conPhone = (String)request.getAttribute("phone");	
	String conRemsg = (String)request.getAttribute("remsg");	
	String conisReAttr = (String)request.getAttribute("isReAttr");	
	String conisGeAttr = (String)request.getAttribute("isGeAttr");	
	String conreCount = (String)request.getAttribute("reCount");	
	
	String conNoticeId = (String)request.getAttribute("preNoticeId");
	String conMaxSendCount = (String)request.getAttribute("maxSendCount");
	String conSendInterval = (String)request.getAttribute("sendInterval");
	String conSendContent = (String)request.getAttribute("sendContent");
	String conArySendCount = (String)request.getAttribute("arySendCount");
	String conSenderName = (String)request.getAttribute("senderName");
	String conSpUser = (String)request.getAttribute("spUser");
	String conlgguid = (String)request.getAttribute("lgguid");
	String conlgcorpcode = (String)request.getAttribute("lgcorpcode");
	float skinVersion = Float.parseFloat(skin.substring(skin.lastIndexOf("frame")+5, skin.indexOf("skin")-1));
	String taskid = (String)request.getAttribute("taskid");
	//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String menucop = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_110", request);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_109" defVal="完美通知历史记录详情" fileName="dxzs"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<style type="text/css">
			.c_selectBox{
				width: 208px!important;
			}
			.c_selectBox ul {
				width: 208px!important;
			}
			.c_selectBox ul li{
				width: 208px!important;
			}
			.buttons a#addcporv {
				<% if(skinVersion>=4.0f){%>
					width: 104px;
				<% }else {%>
					width: 94px;
				<%}%>
			}
		</style>
	</head>
	<body id="queryNotDetail">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,menucop) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<div id="getloginUser">
					</div>
					<div id="u_o_c_explain" class="div_bg">
						<div id="tableDiv">
							<table class="dxzs_table">
								<tr>
									<td width="<%="zh_HK".equals(empLangName)?130:75%>" align="left">
										<b><emp:message key="dxzs_xtnrqf_title_109" defVal="信息内容" fileName="dxzs"/>：</b>
									</td>
									<td align='left' class="dxzs_td">
										<%
											String str="";
											String sign="";
											if("1".equals(conArySendCount)){
												str = conSenderName;
												sign = String.format(DxzsStaticValue.getPERFECT_SIGN_NAME(), str).replace("完美通知", MessageUtils.extractMessage("dxzs","dxzs_PERFECT_SIGN",request));
											}else{
												str = conArySendCount;
												sign = String.format(DxzsStaticValue.getPERFECT_SIGN_TIMER(), str).replace("完美通知", MessageUtils.extractMessage("dxzs","dxzs_PERFECT_SIGN",request)).replace("次", MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_115",request));
											}	
										%>
										<%--<%=sign %><%=conSendContent.replaceAll("<","&lt;").replaceAll(">","&gt;") %>.--%>
										<xmp><%=(sign+""+conSendContent) %></xmp>
									</td>
								</tr>
								<tr>
									<td  align="left">
										<b><emp:message key="dxzs_xtnrqf_title_112" defVal="发送规则" fileName="dxzs"/>：</b>
									</td>
									<td align="left">
										<emp:message key="dxzs_xtnrqf_title_113" defVal="若未回复,每隔" fileName="dxzs"/><%=conSendInterval %><emp:message key="dxzs_xtnrqf_title_114" defVal="分钟再发一次,最多发" fileName="dxzs"/><%=conMaxSendCount %><emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/>.
									</td>
								</tr>
							</table>
						</div>
						<span id="noticeInfoDiv">
							<span id="hide_content"> </span>
							<a id="foldIcon" class="fold">&nbsp;&nbsp;&nbsp;&nbsp;</a>
						</span>
					</div>
					<form name="pageForm" action="<%=path %>/per_queryNotHis.htm?method=getPreNoticeHisInfo" method="post" id="pageForm">
						<div class="buttons">
							<a href="javascript:reStartSend('<%=taskid %>')" id='addcporv' ><emp:message key="dxzs_xtnrqf_title_116" defVal="发送完美通知" fileName="dxzs"/></a>
							<a href='javascript:exportExcel()' id='exportCondition' ><emp:message key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs"/></a>
						<div id="toggleDiv" >
							</div>	
					<span id="backgo" class="right mr5" onclick="javascript:location.href='<%=path%>/per_queryNotHis.htm?method=find&isback=1&lgguid='+$('#lgguid').val()">&nbsp;<emp:message key="dxzs_xtnrqf_button_9" defVal="返回" fileName="dxzs"/></span>
				  
							
						</div>
						<%--设置需要不需要隐藏手机号码   1是需要隐藏   2是不需要隐藏 --%>
						<input type="hidden" id="isHideNumber" name="isHideNumber" value="<%=isHideNumber %>"/>
					     <input type="hidden" id="noticeId" name="noticeId" value="<%=conNoticeId%>"/>
					      <input type="hidden" id="type" name="type" value="2"/>
					       <input type="hidden" id="lgguid" name="lgguid" value="<%=conlgguid%>"/>
					       <input type="hidden" id="pnoticeSize" name="pnoticeSize" value="<%=pnoticeVos != null ? pnoticeVos.size():0%>"/>
					       <input type="hidden" id="conlgcorpcode" name="conlgcorpcode" value="<%=conlgcorpcode%>"/>
					       	<%--完美通知内容 --%>
					        <input type="hidden" id="pernoticeContent" name="pernoticeContent" value="<%=conSendContent.replaceAll("<","&lt;").replaceAll(">","&gt;")%>"/>
					         	<%--完美通知发送次数 --%>
					        <input type="hidden" id="pernoticesendCount" name="pernoticesendCount" value="<%=conArySendCount%>"/>
					         	<%--完美通知发送名称 --%>
					        <input type="hidden" id="pernoticeName" name="pernoticeName" value="<%=conSenderName%>"/>
					        <input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					       <input type="hidden" id="taskid" name="taskid" value="<%=taskid%>"/>
					       
					
					        
						<div id="condition" >
						 <table>
								<tbody>
								<tr>
								    <td>
									<emp:message key="dxzs_xtnrqf_title_117" defVal="接收对象" fileName="dxzs"/>：
									</td>
									<td>
										<label>
											<input type='text' value='<%=null==conUserName?"":conUserName%>' id ='username' name ='username' maxlength='16' maxlength="25">
										</label>
									</td>
									
									<td>
									<emp:message key="dxzs_xtnrqf_title_119" defVal="手机号码" fileName="dxzs"/>：
									</td>
									<td>
										<label>
											<input type='text' value='<%=null==conPhone?"":conPhone%>' id ='phone'  name ='phone' maxlength='21'  onkeyup='javascript:phoneInputCtrl($(this))'>
										</label>
									</td>
									
									<td>
									<emp:message key="dxzs_xtnrqf_title_120" defVal="回复内容" fileName="dxzs"/>：
									</td>
									<td>
										<label>
											<input type='text' value='<%=null==conRemsg?"":conRemsg%>' id ='remsg'  name ='remsg' maxlength="25">
										</label>
									</td>
									
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
								
								</tr>
								<tr>
									<td>
										<emp:message key="dxzs_xtnrqf_title_121" defVal="回复状态" fileName="dxzs"/>：
									</td>
									<td>
										<select id='isReAttr' name='isReAttr'>
											<option value=''><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
											<option value='1' <%="1".equals(conisReAttr)?"selected=selected":"" %>><emp:message key="dxzs_xtnrqf_title_122" defVal="已回复" fileName="dxzs"/></option>
											<option value='2' <%="2".equals(conisReAttr)?"selected=selected":"" %>><emp:message key="dxzs_xtnrqf_title_123" defVal="未回复" fileName="dxzs"/></option> 
										</select>
									</td>
									<td>
										<emp:message key="dxzs_xtnrqf_title_124" defVal="回执状态" fileName="dxzs"/>：
									</td>
									<td>
											<select id='isGeAttr' name='isGeAttr'>
												<option value=''><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
												<option value='1' <%="1".equals(conisGeAttr)?"selected=selected":"" %>><emp:message key="dxzs_xtnrqf_title_125" defVal="已接收" fileName="dxzs"/></option>
												<option value='2' <%="2".equals(conisGeAttr)?"selected=selected":"" %>><emp:message key="dxzs_xtnrqf_title_126" defVal="未接收" fileName="dxzs"/></option>
												<option value='3' <%="3".equals(conisGeAttr)?"selected=selected":"" %>><emp:message key="dxzs_xtnrqf_title_127" defVal="未发送成功" fileName="dxzs"/></option>  
											</select>
									</td>
									
									<td>
										<emp:message key="dxzs_xtnrqf_title_128" defVal="已发次数" fileName="dxzs"/>：
									</td>
									<td>
										<select id="reCount" name="reCount">
												 <option value=""><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
											     <option value="1" <%="1".equals(conreCount)?"selected=selected":"" %>>1<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
											     <option value="2" <%="2".equals(conreCount)?"selected=selected":"" %>>2<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
											     <option value="3" <%="3".equals(conreCount)?"selected=selected":"" %>>3<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
											     <option value="4" <%="4".equals(conreCount)?"selected=selected":"" %>>4<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
											     <option value="5" <%="5".equals(conreCount)?"selected=selected":"" %>>5<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
										</select>
									</td>
									<td >
									</td>
								</tr>
								
							</tbody>
							</table>
						</div>
			
					<table id="content" width="100%">
						<thead>
							<tr>
								<th align='center'  width='5%'>
										<input type='checkbox' name='checkall' id='checkall' onclick='checkAlls(this)' />
								</th>
								<th width="15%">
									<emp:message key="dxzs_xtnrqf_title_117" defVal="接收对象" fileName="dxzs"/>
								</th>
								<th width="15%">
									<emp:message key="dxzs_xtnrqf_title_119" defVal="手机号码" fileName="dxzs"/>
								</th>
								<th width="10%">
									<emp:message key="dxzs_xtnrqf_title_128" defVal="已发次数" fileName="dxzs"/>
								</th>
								<th width="10%">
									<emp:message key="dxzs_xtnrqf_title_124" defVal="回执状态" fileName="dxzs"/>
								</th>
								<th width="10%">
									<emp:message key="dxzs_xtnrqf_title_121" defVal="回复状态" fileName="dxzs"/>
								</th>
								<th width="15%">
									<emp:message key="dxzs_xtnrqf_title_118" defVal="回复时间" fileName="dxzs"/>
								</th>
								<th width="20%">
									<emp:message key="dxzs_xtnrqf_title_120" defVal="回复内容" fileName="dxzs"/>
								</th>
							</tr>
						</thead>
						<tbody>
									<% 
									   if(pnoticeVos != null && pnoticeVos.size()>0){
									   		PerfectNoticUpVo upVo = null;
									       for(int i=0;i<pnoticeVos.size();i++ ){
									       upVo = pnoticeVos.get(i);
									       
									%>
									<tr>
										<td  align='center'>
												<% 
													if("1".equals(upVo.getIsValid())){
												%>
												 	<input type='checkbox' name='checklist' value='<%=upVo.getPnupId() %>'/> 
												<% 
													}else{
												%>
													 -
												<% 
													}
												%>
												
										</td>
										<td class="textalign">
											<%=upVo.getName() %>
										</td>
										<td class="textalign">
											<%
												String moblie = upVo.getMobile();
												if("1".equals(isHideNumber)){
												    Integer len = moblie.length();
													if(len == 11){
														moblie = moblie.substring(0,3)+ "*****" + moblie.substring(len-3,len);
													}
												}
											%>
											<%=moblie%>
										</td>
										<td>
											<%=upVo.getReceiveCountMsg()%>
										</td>
										<td>
											<%=upVo.getIsReceiveMsg() %>
										</td>
										<td>
											<%=upVo.getIsReplyMsg() %>
										</td>
								
									
										<td>
											<%=upVo.getSendTime()%>
										</td>
										<td class="textalign">
											<%=upVo.getContent()%>
										</td>
									</tr>
									
									<% 
											 } 
										}else{
									%>
										<tr>
											<td colspan="8" align="center">
												<emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/>
											</td>
										</tr>
									<% 
										} 
									%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="8">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</form>
			</div>
			
			<div id="reStartSendDia"   title="<emp:message key='dxzs_xtnrqf_title_116' defVal='发送完美通知' fileName='dxzs'/>">
				<center>
					<%--处理是否输入了完美通知发送内容 --%>
					<input type="hidden" id="isOk" value=""/>
					<%--获取的USERSUBNO --%>
					<input type="hidden" id="userSubno" value=""/>
					<input type="hidden" id="spUser" value="<%=conSpUser %>"/>
					<%--发送的人员--%>
					<input type="hidden" id="itemUser" value=""/>
					<%-- 通知人数 --%>
					 <input type="hidden" id="noticecount" name="noticecount" value=""/>
					<table>
						<tr>
							<td class="dxzs_td1">
								<emp:message key="dxzs_xtnrqf_title_129" defVal="发送人数" fileName="dxzs"/>：
							</td>
							<td class="dxzs_td1" align="left">
								<span id="userCount"></span>
							</td>
						</tr>
						<tr>
		                		<td colspan="2" height="8">
		                		</td>
						</tr>
						<tr>
							<td >
								<emp:message key="dxzs_xtnrqf_title_130" defVal="时间间隔" fileName="dxzs"/>：
							</td>
							<td >
									<select id="sendInterval" name="sendInterval" >
																     <option value="3">3<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
																     <option value="4">4<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
																     <option value="5" selected="selected">5<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
																     <option value="6">6<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
																     <option value="7">7<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
																     <option value="8">8<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
																     <option value="9">9<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
																     <option value="10">10<emp:message key="dxzs_xtnrqf_title_131" defVal="分钟" fileName="dxzs"/></option>
									</select>
							</td>
						</tr>
						<tr>
		                		<td colspan="2" height="8">
		                		</td>
						</tr>
						<tr>
							<td >
								<emp:message key="dxzs_xtnrqf_title_132" defVal="发送次数" fileName="dxzs"/>：
							</td>
							<td >
									<select id="sendCount" name="sendCount"  onchange="checkFee()">
																	     <option value="1">1<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
																	     <option value="2">2<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
																	     <option value="3" selected="selected"> 3<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
																	     <option value="4">4<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
																	     <option value="5">5<emp:message key="dxzs_xtnrqf_title_115" defVal="次" fileName="dxzs"/></option>
									</select>
							</td>
						</tr>
						<tr>
		                		<td colspan="2" height="8">
		                		</td>
						</tr>
						<tr>
							<td >
								<emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>：
							</td>
							<td>
							 	<textarea class="msg3 div_bd" name="sendContent" rows="7"
												id="sendContent"  onblur="noticEblur($(this))"></textarea>
											<p align="left">	
												<label id="strlen"> 0</label>
												<label id="maxLen">/965</label>
											</p>
							</td>
						</tr>
						
						<tr>
							<td colspan="2" align="center" class="dxzs_td2">	
								<input id="queren" class="btnClass5 mr23" type="button" onclick="javascript:RestartSendPre('<%=taskid %>');" value="<emp:message key='dxzs_xtnrqf_button_8' defVal='确认' fileName='dxzs'/>"/>
								<input id="quxiao" onclick="javascript:doNotReStart();$('#reStartSendDia').dialog('close');" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" />
								<br/>
							</td>
						</tr>
				</table>
				</center>
			</div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
				</div>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=iPath %>/js/queryNotDetail.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script>
			$(document).ready(function(){
				getLoginInfo("#getloginUser");
				synlen();
			
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
				$('#tableDiv').show();
				//pernoticeContent
				var contentDiv = $("#pernoticeContent").val();
				var sendCountDiv = $("#pernoticesendCount").val();
				var nameDiv = $("#pernoticeName").val();
				var str = "";		
				var sign = "";
				if(sendCountDiv = "1"){
					sign = "<%=DxzsStaticValue.getPERFECT_SIGN_NAME()%>";
					str = sign.replace("%s",nameDiv);
				}else{
					sign = "<%=DxzsStaticValue.getPERFECT_SIGN_TIMER()%>";
					str = sign.replace("%s",sendCountDiv);
				}
				if(contentDiv != "" && contentDiv.length > 20){
					contentDiv  = contentDiv.substring(0,30) + "...";
				} 
				$('#noticeInfoDiv').toggle(function(){
						$("#foldIcon").removeClass("fold");
						$("#foldIcon").addClass("unfold");
						$('#tableDiv').hide();
						$('#hide_content').attr("style","display:block;padding-left: 20px;float:left;");
						$('#hide_content').empty();
						$('#hide_content').append(str+contentDiv);
					},function(){
						$("#foldIcon").removeClass("unfold");
						$("#foldIcon").addClass("fold");
						$('#tableDiv').show();
						$('#hide_content').attr("style","display:none;padding-left: 20px;float:left;");
						$('#hide_content').empty();
				});
				
				//手机号码的绑定事件	
				//$('#phone').bind({
				  //keyup: function() {
				    	//$(this).val($(this).val().replace(/[^\d]/g,''));
				  //}
				//});
				
				var isIE = false;
				var isFF = false;
				var isSa = false;
				if ((navigator.userAgent.indexOf("MSIE") > 0)
						&& (parseInt(navigator.appVersion) >= 4))
					isIE = true;
				if (navigator.userAgent.indexOf("Firefox") > 0)
					isFF = true;
				if (navigator.userAgent.indexOf("Safari") > 0)
					isSa = true;
				$('#username,#remsg').keypress(function(e) {
					var iKeyCode = window.event ? e.keyCode
							: e.which;
					if (iKeyCode == 39) {
						if (isIE) {
							event.returnValue = false;
						} else {
							e.preventDefault();
						}
					}
				});
				$('#username,#remsg').blur(function(e) {
					$(this).val($(this).val().replaceAll("'",""));
				});
				
				
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$('#search').click(function(){submitForm();});
				
			});
	</script>
	
	</body>
</html>
