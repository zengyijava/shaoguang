<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page  import="com.montnets.emp.common.constant.ViewParams"  %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@page import="com.montnets.emp.entity.clientsms.LfDfadvanced"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	//发送账号集合
	@SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>) request
			.getAttribute("spUserList");

	//业务类型集合
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>) request
			.getAttribute("busList");
	String taskId = (String)request.getAttribute("taskId");
	@SuppressWarnings("unchecked")
	//按钮权限Map
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");
	//当前登录操作员
	LfSysuser lfSysuser = (LfSysuser)request.getAttribute("lfSysuser");
	String company=(String)request.getAttribute("company");
	String lgcorpcode=(String)request.getAttribute("company");
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session
			.getAttribute("titleMap");
	String menuCode = titleMap.get("sendClientSMS");
	menuCode = menuCode == null ? "0-0-0" : menuCode;

	String templateMenuCode = titleMap.get("smsTemplate");
	
	String isFlow = (String) request.getAttribute("isFlow");
	String result = session.getAttribute("mcs_clientResult") == null ? "-1"
			: (String) session.getAttribute("mcs_clientResult");

	String findResult = (String) request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String lguserid = request.getParameter("lguserid");
	String oldTaskId = request.getParameter("oldTaskId");
			
	int ishidephome=0;
		if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
			ishidephome=1;
	}
	
	//是否集成了短信模板模块
    boolean isHaveTemp=false;
    if(StaticValue.getInniMenuMap().containsKey(ViewParams.TEMP_MENU_HTM))
    {
    	isHaveTemp=true;
    }
    //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxkf_ydkf_khqzqf_text_title" defVal="短信客服客户群组群发" fileName="dxkf"/></title>
		<%@include file="/common/common.jsp" %>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
        <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
        <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT"> 
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="<%=iPath %>/css/kfs_sendClientSMS.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_KeFuDuanXin.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<style type="text/css">
			#effinfotable > tbody > tr > td:nth-child(1){width: 120px;}
			div#effinfo table{width: 95%;}
			div#effinfo{width: 280px;}
		</style>
		<%}%>

	</head>
	<body onload="getGateNumber();" id="kfs_sendClientSMS">
	<input type="hidden" id="pathUrl" value="<%=request.getContextPath() %>"/>
	<input type="hidden" id="ishidephome" value="<%=ishidephome %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">    
				<input id="formName" value="form3" type="hidden"/>
				<input id="cpath" value="<%=path %>" type="hidden"/>
				<input id="path" value="<%=commonPath %>" type="hidden"/>
                <div id="batchFileSend" class="block">
	                <form name="form3" action = "kfs_sendClientSMS.htm?method=add" method="post" id="form3" target="hidden_iframe">
	                 <iframe name="hidden_iframe" id="hidden_iframe"
								class="dxkf_display_none"></iframe>
								
					<div id="loginUser" class="hidden"></div>		
	                <input type="hidden" value ="4" id="bmtType" name="bmtType"/>
	                <input type="hidden" value ="0" id="isOk" name="isOk"/>
	                <input type="hidden" value ="4" id="sendType" name="sendType"/>
	                <input type="hidden" value="" id="inputContent" name="inputContent" /><%--临时存储手工输入内容 --%>
	                <input type="hidden" value ="" id="sendFlag" name="sendFlag"/><%--  发送帐号主通道号+循环尾号的位数超过２０位个数 --%>
	                <input type="hidden" value ="" id="subNo" name="subNo"/> <%-- 尾号 --%>
	                <input type="hidden" value ="" id="circleSubNo" name="circleSubNo"/> <%-- 尾号 --%>
	                <input type="hidden" value ="<%=taskId %>" id="taskId" name="taskId"/>
	                <input type="hidden" value="" id="isCharg" name="isCharg"/>
	                <input type="hidden" value="" id="spChargetype" name="spChargetype"/>
	                <div class="dxkf_display_none" id="allfilename" ></div>
	                <input type="hidden" name="groupStr" id="groupStr" value=","/><%--员工群组信息 --%>
	                <input type="hidden" name="ydywGroupStr" id="ydywGroupStr" value=","/><%--移动业务群组信息 --%>
	                <input type="hidden" name="ydywGroupStrTemp" id="ydywGroupStrTemp" value=","/><%--临时保存移动业务群组信息 --%>
	                <input type="hidden" value="" id="ydywPhoneStr" name="ydywPhoneStr"/><%--移动业务手机号码信息 --%>
					<input type="hidden" name="groupStrTemp" id="groupStrTemp" value=","/><%--临时保存员工群组信息 --%>
					<input type="hidden" name="depIdStr" id="depIdStr" value=","/>
					<input type="hidden" name="depIdStrTemp" id="depIdStrTemp" value=","/><%-- 临时保存depid字符串，在选择员工取消时用到 --%>
					<input type="hidden" name="proIdStr" id="proIdStr" value=","/>
					<input type="hidden" name="proIdStrTemp" id="proIdStrTemp" value=","/><%-- 临时保存depid字符串，在选择员工取消时用到 --%>
					<input type="hidden" name="proValueIdStr" id="proValueIdStr" value=","/>
					<input type="hidden" value="" id="phoneStr1" name="phoneStr1"/><%-- 保存由通讯录选择的员工电话号码 --%>
					<input type="hidden" value="" id="phoneStr12" name="phoneStr12"/><%-- 没有全选的情况下，保存由通讯录高级搜索的员工电话号码 --%>
					<input type="hidden" value="" id="selectAllStatus" name="selectAllStatus"/><%-- 高级搜索页面全选的状态  --%>
					<input type="hidden" value="" id="selectAllStatusTemp" name="selectAllStatusTemp"/><%-- 高级搜索页面全选的状态  --%>
					<input type="hidden" value="" id="unChioceUserIds" name="unChioceUserIds"/><%-- 全选状态下没有选中的客户id  --%>
					<input type="hidden" value="" id="conditionsqlTemp" name="conditionsqlTemp"/><%-- 全选条件下的查询条件  --%>
					
					<input type="hidden" value="" id="phoneStr12Temp" name="phoneStr12Temp"/><%-- 没有全选的情况下，保存由通讯录高级搜索的员工电话号码 --%>
					<input type="hidden" value="" id="unChioceUserIdsTemp" name="unChioceUserIdsTemp"/><%-- 全选状态下没有选中的客户id  --%>
					
					<%-- 微信用户号码  --%>
					<input type="hidden" value="" id="wxphoneStr" name="wxphoneStr"/>
						
					<input type="hidden" value="" id="hidSubCount" name="hidSubCount" />
					<input type="hidden" value="" id="hidEffCount" name="hidEffCount" />
					<input type="hidden" value="" id="hidMobileUrl" name="hidMobileUrl" />
					<input type="hidden" value="" id="hidPreSendCount" name="hidPreSendCount" />

                    <%--草稿箱--%>
                    <input type='hidden' id="draftId" name='draftId' value=''/>
						<div id="eq_sendDiv">
							<span><emp:message key="dxkf_ydkf_khqzqf_text_fszt" defVal="发送主题：" fileName="dxkf"/></span>
							<input id="taskname" name="taskname" type="text" class="graytext input_bd div_bd" maxlength="20" value="<emp:message key="dxkf_ydkf_khqzqf_text_bzwdxnrfs" defVal="不作为短信内容发送" fileName="dxkf"/>"/>
						</div>
						<div class="clear2"></div>
						
						<div id="eq_sendDiv">
							<span><emp:message key="dxkf_ydkf_khqzqf_text_fshm" defVal="发送号码" fileName="dxkf"/></span>
							<div id="dds" class="div_bd">
								<table id="vss">
									<tr id="first" class="title_bd">
										<td class="div_bd title_bg dxkf_td1"><emp:message key="dxkf_ydkf_khqzqf_text_lx" defVal="类型" fileName="dxkf"/></td>
										<td class="div_bd title_bg dxkf_td2"><emp:message key="dxkf_ydkf_khqzqf_text_hm" defVal="号码" fileName="dxkf"/></td>
										<td class="div_bd title_bg dxkf_td3"><emp:message key="dxkf_common_opt_caozuo" defVal="操作" fileName="dxkf"/></td>
									</tr>
								</table>
							</div>
							<div class="selectFileTable">
							<table class="dxkf_table">
									<tr class="dxkf_tr" id="picTab">
										<td align="center" onclick="javascript:showInfo();"  class="div_bd div_bg dxkf_cursor">
										<div class="dxkf_div1">
										<a class="selectEmp"></a><br/><emp:message key="dxkf_ydkf_khqzqf_opt_xzry" defVal="选择人员" fileName="dxkf"/>
										</div>
										</td>
										<td class="div_bd div_bg dxkf_cursor" align="center" onclick="javascript:showSearchBox();">
                                         <div class="dxkf_div1">
										 <a class="superSearch"/></a><br/><emp:message key="dxkf_ydkf_khqzqf_opt_gjss" defVal="高级搜索" fileName="dxkf"/>
										 </div>										
										</td>
										
									</tr>
									<%--
									<tr>
										<td colspan="2" align="center" class="div_bd div_bg">
											<table width="162px" class="div_bd">
											<tr>
											<td width="125px">
											<input value=" 请输入手机号" class="graytext" type="text" style="line-height:23px;width: 125px; height: 26px;border:0"
											onkeyup="if(this.value!=this.value.replace(/\D/g,'')) this.value=this.value.replace(/\D/g,'')"
											onpaste="return !clipboardData.getData('text').match(/\D/)" maxlength="11" name="tph" id="tph" onpropertychange="addph()"/>
											</td>
											<td align="center" onclick="addph2()" style="cursor:pointer"  class="div_bd">添加</td>
											</tr>
											</table>
										</td>
									</tr>
									 --%>
								</table>
							</div> 
						</div>
	                    <div class="clear2"></div>
							<div id="eq_sendDiv" class="dxkf_div2">
							<span><emp:message key="dxkf_ydkf_khqzqf_text_fsnr" defVal="发送内容" fileName="dxkf"/></span>
							<table class="div_bd dxkf_table2">
							<tr class="dxkf_tr1">
							<td class="div_bd dxkf_td5">
	                       	<a id = "ctem" class="div_bg div_bd" href="javascript:chooseTemp()"><emp:message key="dxkf_ydkf_khqzqf_opt_djxzmb" defVal="点击选择模板" fileName="dxkf"/></a>			
							<a id = "qtem" class="div_bg div_bd" href="javascript:tempNo()"><emp:message key="dxkf_ydkf_khqzqf_opt_djqxmb" defVal="点击取消模板" fileName="dxkf"/></a>&nbsp;	
							<%if(btnMap.get(templateMenuCode+"-1" )!= null && isHaveTemp){%>				    
							<a href="javascript:showAddSmsTmp(0)" ><emp:message key="dxkf_ydkf_khqzqf_opt_ljxj" defVal="[立即新建]" fileName="dxkf"/></a>
							<%} %>
                            <a href="javascript:showDraft()" class="dxkf_a"><emp:message key="dxkf_ydkf_khqzqf_opt_cgx" defVal="草稿箱" fileName="dxkf"/></a>
                            </td>
                            </tr>
							<tr class="dxkf_tr2">
							<td>
								<textarea  class=".msg2" name="msg" rows="5" id="contents" onblur="eblur($(this))"></textarea>
							</td>
							</tr>
                            <tr id="tail-area" class="div_bd">
                                <td id="tail-text">
                                    <emp:message key="dxkf_ydkf_khqzqf_text_twnr" defVal="贴尾内容" fileName="dxkf"/><label id="tailcontents"></label>
                                    <input type="hidden" name="tailcontents" />
                                </td>
                            </tr>
							</table>
									<font class="dxfk_font"><b id="strlen"> 0 </b><b id="maxLen">/980</b></font>
							</div>
							<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="dxkf_ydkf_khqzqf_text_fssj" defVal="发送时间" fileName="dxkf"/></span>
						  <div class="dxkf_div3">
						    <input type="radio" name="timerStatus" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="dxkf_ydkf_khqzqf_opt_ljfs" defVal="立即发送" fileName="dxkf"/>
							<input type="radio" name="timerStatus" value="1" class="dxkf_timerStatus" 
								onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="dxkf_ydkf_khqzqf_opt_dsfs" defVal="定时发送" fileName="dxkf"/>
							<label id="time2" class="dxkf_display_none">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
									id="timerTime" name="timerTime" value="">
							</label>
						  </div>
						</div>
						<div id="eq_sendDiv2">
							<span id="u_o_c_explain" class="div_bg"><b><emp:message key="dxkf_ydkf_khqzqf_opt_gjsz" defVal="高级设置" fileName="dxkf"/></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
							<div id="moreSelect" class="div_bg">
							<div class="dxkf_div4" align="right">
								<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="dxkf_ydkf_khqzqf_opt_xxcwmr" defVal="选项存为默认" fileName="dxkf"/></a>
							</div>
							<%-- 业务类型 --%>
							<table class="dxlf_table2">
							<tr class="dxkf_tr3"><td class="dxkf_td6"><font class="dxkf_font"><emp:message key="dxkf_ydkf_khqzqf_opt_ywlx" defVal="业务类型" fileName="dxkf"/></font></td>
							<td>
							<select id="busCode"  name="busCode">
										<%
											if (busList != null && busList.size() > 0) {
												String busCode = lfDfadvanced != null?lfDfadvanced.getBuscode():"";
												for (LfBusManager busManager : busList) {
										%>
										<option value="<%=busManager.getBusCode()%>" 
											<%=busCode != null && !"".equals(busCode) && busCode.equals(busManager.getBusCode())?"selected":"" %>>
											<%String busName = busManager.getBusName().replace("默认业务", MessageUtils.extractMessage("ydwx","ydwx_defaultBussiness",request));%>
											<%=(busName+"("+busManager.getBusCode()+")").replaceAll("<", "&lt;").replaceAll(">","&gt;")%>
										</option>
										<%
											}
											}
										%>
							</select>
							</td>
							</tr>
							<tr  class="dxkf_tr3">
							<td><font class="dxkf_font"><emp:message key="dxkf_ydkf_khqzqf_text_fsjb" defVal="发送级别" fileName="dxkf"/></font></td>
							<td>
							<select id="priority" name="priority">
									<option value="0"><emp:message key="dxkf_ydkf_khqzqf_text_xtznkz" defVal="系统智能控制" fileName="dxkf"/></option>
									<option value="1">1<emp:message key="dxkf_ydkf_khqzqf_text_yxjzg" defVal="(优先级最高)" fileName="dxkf"/></option>
									<option value="2">2</option>
									<option value="3">3</option>
									<option value="4">4</option>
									<option value="5">5</option>
									<option value="6">6</option>
									<option value="7">7</option>
									<option value="8">8</option>
									<option value="9">9<emp:message key="dxkf_ydkf_khqzqf_text_yxjzd" defVal="(优先级最低)" fileName="dxkf"/></option>
									</select>
							</td>
							</tr>
							<tr class="dxkf_tr3">
							<%--<td><font style="padding-left:35px"><%=StaticValue.SMSACCOUNT%>：</font></td>
							--%><td><font class="dxkf_font"><emp:message key="dxkf_ydkf_srzf_text_spaccount_mh" defVal="SP账号：" fileName="dxkf"/></font></td>
							<td>
							<select id="spUser" name="spUser">
										<%
										if (spUserList != null && spUserList.size() > 0) {
											String spUserId = lfDfadvanced != null?lfDfadvanced.getSpuserid():"";
											for (Userdata spUser : spUserList) {
									%>
									<option value="<%=spUser.getUserId()%>"  
										<%=spUserId != null && !"".equals(spUserId) && spUserId.equals(spUser.getUserId())?"selected":"" %>>
										<%=spUser.getUserId()+"("+spUser.getStaffName()+")"%>
									</option>
									<%}}%>
									</select>
							</td>
							</tr>
							<tr class="dxkf_tr3">
							<td><font class="dxkf_font"><emp:message key="dxkf_ydkf_khqzqf_text_hfsz" defVal="回复设置" fileName="dxkf"/></font></td>
							<td>
							<input type="radio"  name="isReply" value="0" onclick="getGateNumber()" 
									<%if(lfSysuser.getIsExistSubNo() != 1){ %> checked="checked" <%} %> style="margin-left: 4px;" />&nbsp;<emp:message key="dxkf_ydkf_khqzqf_opt_byhf" defVal="不用回复" fileName="dxkf"/>
									<input type="radio"  name="isReply" value="1" onclick="getGateNumber()" style="margin-left: 10px;" />&nbsp;<emp:message key="dxkf_ydkf_khqzqf_opt_bcrw" defVal="本次任务" fileName="dxkf"/>
									<input type="radio"  name="isReply" value="2" onclick="getGateNumber()" 
									<%if(lfSysuser.getIsExistSubNo() != 1){ %> disabled="disabled" <%}else{ %>
									checked="checked"<%} %>
									style="margin-left: 10px;"/><label <%if(lfSysuser.getIsExistSubNo() != 1){ %> style="color:gray;" <%}else{%>
									style="color:#000000;"
									 <%}%>>&nbsp;<emp:message key="dxkf_ydkf_khqzqf_text_wdwh" defVal="我的尾号" fileName="dxkf"/></label><label style="margin-left: 20px;color:#000000;display: none" id="curSubNo"><emp:message key="dxkf_ydkf_khqzqf_text_dqwh" defVal="当前尾号" fileName="dxkf"/></label>
									 <label id="subno" ></label>
							</td>
							</tr>
							
							</table>
							</div>
							</div>
						
						<div class="clear2"></div>
						<div class="b_F_btn">
							<input type="hidden" value ="1" id="subState" name="subState"/>
                            <input id="toDraft" type="button" value="<emp:message key="dxkf_ydkf_khqzqf_opt_zccg" defVal="暂存草稿" fileName="dxkf"/>" class="btnClass5 mr23"/>
							<input id="preSend" type="button" value="<emp:message key="dxkf_common_opt_tijiao" defVal="提交" fileName="dxkf"/>" class="btnClass5 mr23"/>
							<input id="qingkong" type="button" value="<emp:message key="dxkf_common_opt_chognzhi" defVal="重置" fileName="dxkf"/>" class="btnClass6" onclick="javascript:reloadPage()"/>
							<input type = "hidden" name="preStr" id="preStr" value="" />
						</div>
						</form>
					</div>
				
								<div id="detail_Info">							  
								<table id="infos" >
								  <tr >
								    <td class="infostd"><emp:message key="dxkf_ydkf_khqzqf_text_fsts" defVal="发送条数" fileName="dxkf"/><span id="yct"></span></td>								    
								    <td class="infostd" id="showyct"><emp:message key="dxkf_ydkf_khqzqf_text_jgye" defVal="机构余额" fileName="dxkf"/><span id="ct"></span></td>				
								    <td class="infostd" id="showspf"><emp:message key="dxkf_ydkf_khqzqf_text_spzhye" defVal="sp账号余额" fileName="dxkf"/><span id="spf"></span></td>							    
								  </tr>
								  <tr>
									  <td colspan="2">
									    <label id="messages2"><font class="dxkf_red"><emp:message key="dxkf_ydkf_khqzqf_text_yysyebz" defVal="运营商余额不足不允许进行发送!" fileName="dxkf"/></font></label>
									  </td>
								  </tr>
								  <tr>
									  <td colspan="2">
									    <label id="messages1"><font class="dxkf_red"><emp:message key="dxkf_ydkf_khqzqf_text_yebz" defVal="余额不足不允许进行发送!" fileName="dxkf"/></font></label>
									  </td>
								  </tr>
								  <tr>
									  <td><emp:message key="dxkf_ydkf_khqzqf_text_yxhms" defVal="有效号码数：" fileName="dxkf"/><span id="effs"></span></td>
									  <td><emp:message key="dxkf_ydkf_khqzqf_text_tjhms" defVal="提交号码数：" fileName="dxkf"/><span id="counts"></span></td>
									  <td align="center">
									   <div id="showeffinfo" class="div_bg">
									       <p class="dxkf_font_size"><emp:message key="dxkf_ydkf_khqzqf_text_wxhm" defVal="无效号码" fileName="dxkf"/>(<span id="alleff" class="dxkf_red"></span>)&nbsp;&nbsp;
									   <a id="arrowhead" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></p> 
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
									      <td align="left"><emp:message key="dxkf_ydkf_khqzqf_text_hmdhm" defVal="黑名单号码" fileName="dxkf"/></td>
										  <td><span id="blacks"></span></td>
									      <td id="preinfodown">
									       <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>	
									      <a href="javascript:uploadbadFiles()"><emp:message key="dxkf_ydkf_khqzqf_opt_xqxz" defVal="详情下载" fileName="dxkf"/></a><input type="hidden" id="badurl" name="badurl"  value=""></input>
									      <%} %>
									      </td>
								      </tr>
								      <tr>
									      <td align="left"><emp:message key="dxkf_ydkf_khqzqf_text_cfhm" defVal="重复号码" fileName="dxkf"/></td>
										  <td><span id="sames"></span></td>
									      <td></td>
								      </tr>
								      <tr>
								          <td align="left"><emp:message key="dxkf_ydkf_khqzqf_text_gsff" defVal="格式非法" fileName="dxkf"/></td>
										  <td><span id="legers"></span></td>
								          <td></td>
								      </tr>
								    </table>
								    </center>
							    </div>
							    <div class="dxkf_div5"></div>	
								<p class="dxkf_p">
									<emp:message key="dxkf_ydkf_khqzqf_opt_bfyl" defVal="部分预览" fileName="dxkf"/>
								</p>
								<div id="maindiv">
								<center>
								<table id="content"></table>
								</center>
								</div>
						    	<div id="footer">
						    		<div id="tdSign"></div>
						    	  <center>
						    		<input id="btsend" class="btnClass5 mr23" type="button" value="<emp:message key="dxkf_common_opt_fasong" defVal="发送" fileName="dxkf"/>" onclick="javascript:btsend()"/>
								    <input id="btcancel" onclick="javascript:btcancel1();" class="btnClass6" type="button" value="<emp:message key="dxkf_common_opt_quxiao" defVal="取消" fileName="dxkf"/>" />
								   <br/>
								  </center> 
						    	</div>	
							</div>
			</div>
			<div id="infoDiv" title="<emp:message key="dxkf_ydkf_khqzqf_opt_xzfsdx" defVal="选择发送对象" fileName="dxkf"/>" class="dxkf_display_none">
				<input id="flowNames" type="hidden" name="flowNames" value="" />
				<iframe id="flowFrame" name="flowFrame" data-src="<%=iPath %>/kfs_chooseSendInfo.jsp?lguserid=<%=lfSysuser.getUserId() %>" marginwidth="0" scrolling="no" frameborder="no"></iframe>
				<input id="skin" type="hidden" name="skin" value="<%=skin %>" />
				<input id="lguserid" type="hidden" name="lguserid" value="<%=lguserid %>" />
				<input id="lgcorpcode" type="hidden" name="lgcorpcode" value="<%=lgcorpcode %>" />
				<input id="commonPath" type="hidden" name="commonPath" value="<%=commonPath %>" />
				<%--用于数据回显--%>
             	<div title="" class="dxzs_display_none" id="rightSelectedUserOption"></div>
				<%--员工机构IDS --%>
				<input type="hidden" id="empDepIds"  name="empDepIds" value=""/>
				<%--客户机构IDS  --%>
				<input type="hidden" id="cliDepIds"  name="cliDepIds" value=""/>
				<%--群组机构IDS  --%>
				<input type="hidden" id="groupIds"  name="groupIds" value=""/>
				<%--员工IDS  --%>
				<input type="hidden" id="empIds"  name="empIds" value=""/>
				<%--客户IDS  --%>
				<input type="hidden" id="cliIds"  name="cliIds" value=""/>
				<%--外部人员IDS --%>
				<input type="hidden" id="malIds"  name="malIds" value=""/>
				<%--签约用户Ids--%>
				<input type="hidden" name="signClientIds" id="signClientIds" value=""/>
				<table>
					<tr>
						<td class="dxkf_td7" align="center">
						<input type="button"  value="<emp:message key="dxkf_common_opt_queding" defVal="确定" fileName="dxkf"/>" class="btnClass5 mr23" onclick="javascript:doOk()" />
						<input type="button" value="<emp:message key="dxkf_common_opt_quxiao" defVal="取消" fileName="dxkf"/>" class="btnClass6 clearBtn" onclick="javascript:doNo()" />
						<br/>
						</td>
					</tr>
				</table> 
			</div>
			<div id="advSearch"  title="<emp:message key="dxkf_ydkf_khqzqf_opt_gjssssry" defVal="高级搜索-搜索人员" fileName="dxkf"/>" class="dxkf_display_none">
				<iframe id="advSearchFrame" style="height: <%=skin.contains("frame4.0")? 615:535%>px" name="advSearchFrame" data-src="<%=path%>/kfs_sendClientSMS.htm?method=advancedSearch&lgcorpcode=<%=lgcorpcode %>&lguserid=<%=lfSysuser.getUserId() %>" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div id="tempDiv" title="<emp:message key="dxkf_ydkf_khqzqf_opt_dxmbxz" defVal="短信模板选择" fileName="dxkf"/>">
			<iframe id="tempFrame" name="tempFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
				<div  class="dxzs_div6">
					<input class="btnClass5 mr23" onclick="tempSure()" value="<emp:message key="dxkf_common_opt_xuanze" defVal="选择" fileName="dxkf"/>" type="button"/>
					<input class="btnClass6" onclick="tempCancel()" value="<emp:message key="dxkf_common_opt_quxiao" defVal="取消" fileName="dxkf"/>" type="button"/>
					<br/>
				</div>
			</div>
			<div id="addSmsTmpDiv" title="<emp:message key="dxkf_ydkf_khqzqf_opt_xjmb" defVal="新建模板" fileName="dxkf"/>">
				<iframe id="addSmsTmpFrame" name="addSmsTmpFrame"  marginwidth="0" scrolling="no" frameborder="no"></iframe>				
			</div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>

            <div id="draftDiv" title="<emp:message key="dxkf_ydkf_khqzqf_opt_cgxxz" defVal="草稿箱选择" fileName="dxkf"/>">
                <iframe id="draftFrame" name="draftFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
                <div class="dxzs_div6">
                    <input class="btnClass5 mr23" onclick="draftSure()" value="<emp:message key="dxkf_common_opt_xuanze" defVal="选择" fileName="dxkf"/>" type="button"/>
                    <input class="btnClass6" onclick="draftCancel()" value="<emp:message key="dxkf_common_opt_quxiao" defVal="取消" fileName="dxkf"/>" type="button"/>
                    <br/>
                </div>
            </div>
			<%-- foot结束 --%>
		</div>
		<div class="clear2"></div>
				<div id="message" title="<emp:message key="dxkf_common_opt_tishi" defVal="提示" fileName="dxkf"/>">
				<div class="dxkf_tr1"></div>
		 		<center>
				<label ><emp:message key="dxkf_ydkf_khqzqf_text_fszwgcg" defVal="发送至网关成功！" fileName="dxkf"/></label>
				<a href="javascript:sendRecord('2300-1400',<%=oldTaskId%>,<%=lguserid%>,<%=lgcorpcode%>)"  class="alink" ><emp:message key="dxkf_ydkf_khqzqf_opt_ckfsjl" defVal="查看发送记录" fileName="dxkf"/></a>
				</center>
				<br>
				<div class="dxkf_div7"></div>
				 <center>
				    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="<emp:message key="dxkf_common_opt_guanbi" defVal="关闭" fileName="dxkf"/>" />
				</center> 
		</div>
		
		<div id="confirmMsgText" style="display:none;padding: 30px;font-size: 16px;">
			当前发送内容中未添加退订指令，信息可能会被运营商退回，您确定继续发送吗？
			<div style="margin-top:50px;">
	    	  <center>
	    		<input onclick="javascript:confirmBtn();" class="btnClass5 mr23" type="button" value="<emp:message key='dxkf_common_opt_queding' defVal='确定' fileName='dxkf'/>"/>
			    <input onclick="javascript:cancelBtn();" class="btnClass6" type="button" value="<emp:message key="dxkf_common_opt_quxiao" defVal="取消" fileName="dxkf"/>" />
				<br/>
			  </center> 
	    	</div>	
		</div>
						
        <script>
            var tmpEditorFlag = <%=StaticValue.getTMPEDITORFLAG()%>;
        </script>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=116"	type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/dxkf_<%=empLangName%>.js"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=116" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=116" type="text/javascript" ></script>
		<script src="<%=iPath %>/js/clientSend.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
		<script>
		var base_path = "<%=path%>";
		function add() {
			var Privilege = "";
			//var checks = document.getElementById('dropMenu').contentWindow.document.getElementsByName("check");
			//var radios = document.getElementById('dropMenu').contentWindow.document.getElementsByName("check");
			var checks = $("#dropMenu").contents().find("input:checkbox");
			var radios = $("#dropMenu").contents().find("input:radio");
			for ( var i = 0; i < checks.length; i=i+1) {
				if (checks[i].checked == true) {
					Privilege += checks[i].value + ",";
				}
			}
			for ( var i = 0; i < radios.length; i=i+1) {
				if (radios[i].checked == true) {
					Privilege += radios[i].value + ",";
				}
			}
			if (Privilege != "" && Privilege.lastIndexOf(',')==Privilege.length-1) 
			{
				var pp = Privilege.substring(0,Privilege.lastIndexOf(','));
				$("#proIds").val(pp);
				$.post("<%=path%>/kfs_sendClientSMS.htm?method=getNums",{pro:pp,lgcorpcode:$("#lgcorpcode").val()},function(result){
                     if(result != null && result != "")
                     {
                          // $("#groupResouce").val("共发送给"+result+"个客户");
                           $("#groupResouce").val(getJsLocaleMessage('dxkf','dxkf_dxqf_page_ggsg')+result+getJsLocaleMessage('dxkf','dxkf_dxqf_page_gkh'));
                     }
                     else
                     {
                    	 // alert("没有发送号码，该短信将不发送！");
                    	  alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nophonenosend'));
                    	 $("#groupResouce").val("");
                    	 $("#proIds").val("");
                     }
                     $('#sengTypes').dialog('close');
				});
			}
			else
			{
                 // alert("请至少选择一个客户属性！");
                 alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_chosepropties'));
			}
		}
		
		function setSign(isSign){
			if(isSign.checked){
				$("#signStr").css("display","inline");
				$("#signStr").val("[<%=lfSysuser.getName()%>]");
			}else{
				$("#signStr").css("display","none");
				$("#signStr").val("");
			}
			eblur($("#contents"));
		}

			$(document).ready(
				function(){
				$('#moreSelect').hide();
				$('#u_o_c_explain').toggle(function(){
						$("#foldIcon").removeClass("unfold");
						$("#foldIcon").addClass("fold");
						$('#moreSelect').show();
					},function(){
						$("#foldIcon").removeClass("fold");
						$("#foldIcon").addClass("unfold");
						$('#moreSelect').hide();
				});
				$("#showeffinfo").click(function() {
				    if($("#effinfo").is(":hidden"))
				    {
						$("#effinfo").show();
						$("#arrowhead").removeClass("unfold");
						$("#arrowhead").addClass("fold");
	                    if($("#messages1").is(":hidden")){
	                        $("#effinfotable").css("top","62px");
	                    }
	                    else
	                    {
	                       $("#effinfotable").css("top","90px");
	                    }
				    }else
				    {
						$("#effinfo").hide();
						$("#arrowhead").removeClass("fold");
						$("#arrowhead").addClass("unfold");
					}
				}); 
				$("#infoDiv").dialog({
					autoOpen: false,
					height:550,
					width: 535,
					resizable:false,
					modal: true,
					open:function(){

						hideSelect();
					},
					close:function(){
						showSelect();
					}
				});
				
				$("#advSearch").dialog({
					autoOpen: false,
					height:<%=skin.contains("frame4.0")? 550:600%>,
					width:function(){
					  if(screen.width<=1024){
						  return screen.width-310;
					  }else{
						  return 1024;
					  }

					},
					resizable:false,
					modal: true,
					open:function(){
						hideSelect();
						//$(".ui-dialog-titlebar-close").show();
					},
					close:function(){
						showSelect();
						//$(".ui-dialog-titlebar-close").hide();
					}
				});
				resizeDialog($("#advSearch"),"ydkfDialogJson","kfdx_kfqzqf_test3");
				
				$("#chooseshows").hover(function(e){
	                $(this).css("text-decoration","underline");
				},function(e){
                    $(this).css("text-decoration","none");
				});
				$("#picTab td").hover(function(){
					$(this).removeClass("div_bg");
					$(this).addClass("div_hover_bg");
					$(this).addClass("img_hover");
				},function(){
					$(this).removeClass("div_hover_bg");
					$(this).addClass("div_bg");
					$(this).removeClass("img_hover");
				});
				getLoginInfo("#loginUser");
				var date = new Date().valueOf();
				setFormName("form3");
					var result = "<%=result%>";
					var findresult="<%=findResult%>";

				    if(findresult=="-1")
				    {
				       alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_neterror'));	
				       // alert("加载页面失败,请检查网络是否正常!");	
				       return;			       
				    }
					if(result!="-1")
					{
						if(result.indexOf("empex") == 0)
						{
							// alert("任务创建失败："+result.substr(5));
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_addtaskfalse')+result.substr(5));
						}else
						if(result=="timerSuccess")
						{
							// alert("创建短信任务及定时任务添加成功！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_addtasksuccess'));
							getCt();
						}else if(result=="uploadFileFail")
						{
							// alert("上传号码文件失败，取消任务创建！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_cancelfileupload'));
						}else if(result=="timerFail")
						{
							// alert("创建定时任务失败，取消任务创建！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_createtaskfalse'));
						}else if(result=="createSuccess")
						{ 
							// alert("创建短信任务及提交到审批流成功！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_createsmssuccess'));
							getCt();
						}else if(result=="000")
						{
							//alert("创建短信任务及发送到网关成功！");
							getCt();
							<%session.removeAttribute("eq_diffResult");%>
							$("#message").dialog({width:300,height:180});
							$("#message").dialog("open");
						}else if(result=="saveSuccess")
						{
							// alert("存草稿成功！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_savesuccess'));
						}else if(result=="noPhone")
						{
							// alert("通讯录中无可发送号码！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nophoneinbook'));
						}else if(result=="error")
						{
							// alert("创建短信任务失败！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_createsmsfalse'));
						}else if(result=="depfee:-2")
						{
						    // alert("机构余额不足,创建短信任务失败！");
						    alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_yuebuzu'));
						}else if(result=="depfee:-1")
						{
						    // alert("创建短信任务时,修改计费信息失败！");
						    alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_updatecostfalse'));
						}else if(result == "timeError"){
							// alert("发送时间已经超过定时时间，不能发送！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_timeout'));
						}else if(result=="nospnumber")
						{
							// alert("发送失败，"+<%=StaticValue.SMSACCOUNT %>+"未设置尾号！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_sendfalse')+<%=StaticValue.SMSACCOUNT %>+getJsLocaleMessage('dxkf','dxkf_dxqf_page_noweihao'));
						}else if(result == "subnoFailed"){
							// alert("拓展尾号处理失败！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_addweihaofalse'));
						}else if("nogwfee"==result || "feefail"==result){
							// alert("获取运营商余额失败，取消任务创建！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_hqyysyesb'));
						}else if(result.indexOf("lessgwfee")==0){
							// alert("运营商余额不足，取消任务创建！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_yysyebz'));
						}else if(result=="spuserfee:-2")
						{
						    // alert("sp账号余额不足,不允许发送,请联系管理员充值");
						    alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_spyebz'));
						}else if(result=="spuserfee:-1"||result=="spuserfee:-3")
						{
						    // alert("查询SP账号余额失败！");
						    alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_spyesb'));
						}
						else
						{
							// alert("向网关发送请求失败:"+result);
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_sendfalse')+result);
						}
					}
					<%session.removeAttribute("mcs_clientResult");%>
					
					<% 
					String priority = lfDfadvanced != null?lfDfadvanced.getPriority():"0";
					int isReply = lfDfadvanced != null?lfDfadvanced.getReplyset():-99;
					%>
					$("#priority").val(<%=priority%>);
					var isReply = <%=isReply%>;
					var isExistSubNo = <%=lfSysuser.getIsExistSubNo()%>;
					if(isReply != -99){
						if(isReply == 2 && isExistSubNo != 1){
							isReply = 0;
						}
						$("input:radio[name='isReply'][value='"+isReply+"']").attr("checked",'checked');
					}
					$("#spUser,#busCode").bind("change", function(){getGateNumber();});
					}
			);

		//详情下载
		function uploadbadFiles()
		{
		    var badurl = $("#badurl").val();
		    badurl = badurl.replace(".txt","_bad.txt");
		   	$.post("kfs_sendClientSMS.htm?method=goToFile", {
				url : badurl
			},
				function(result) {
					if (result == "true") {
						download_href("<%=path%>/doExport.hts?u="+badurl);
					} else if (result == "false")
						// alert("文件不存在");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nofile'));
					else
						// alert("出现异常,无法跳转");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_donotjumperror'));
			});
		}
		
		//关闭对话框
		function doOk() {

			$("#signClientIds").val();
			$("#rightSelectedUserOption").val($(window.frames['flowFrame'].document).find("#right").html());
			$("#empDepIds").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
			$("#cliDepIds").val($(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val());
			$("#groupIds").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
			$("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
			$("#cliIds").val($(window.frames['flowFrame'].document).find("#clientIds").val());
			$("#malIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
			/*if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
				// alert("您没有选择任何人员！");
				//alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nopepolechoose'));
				//return;
			}*/
			$("#depIdStr").val(",");
			$("#groupStr").val(",");
			$("#proIdStr").val(",");
			$("#proValueIdStr").val(",");
			$("#ydywGroupStr").val(",");
			$("#ydywGroupStrTemp").val(",");
			$("#depIdStrTemp").val(",");
			$("#groupStrTemp").val(",");
			$("#proIdStrTemp").val(",");
			$("#proValueIdStrTemp").val(",");
			$("#phoneStr1").val("");
			$("#wxphoneStr").val("");
			$("#ydywPhoneStr").val("");

            if("<%=skin%>".indexOf("frame4.0") > -1){
                $(window.frames['flowFrame'].document).find("#right option").each(function(){
                    var isDep = $(this).attr("isdep");
                    var phone = $(this).attr("mobile");
                    var et = $(this).attr("et");
                    var id = $(this).val();
                    //客户 自建 共享
                    if(isDep === "5" || isDep === "6"){
                        $("#phoneStr1").val($("#phoneStr1").val() + phone + ",");
					}
					//机构
                    if(isDep === "2"){
                        if(et === "2"){
                            //包含子机构
                            $("#depIdStr").val($("#depIdStr").val()+"e"+id+",");
                        }else if(et === "1"){
                            //不包含子机构
                            $("#depIdStr").val($("#depIdStr").val()+id+",");
                        }
                    }
                    //群组
                    if(isDep === "3"){
                        $("#groupStr").val($("#groupStr").val()+id+",");
                    }
                    //客户属性
                    if(isDep === "10"){
                        $("#proIdStr").val($("#proIdStr").val()+id+",");
                    }
					//属性值用户
                    if(isDep === "9"){
                        $("#proValueIdStr").val($("#proValueIdStr").val()+id+",");
                    }
                    //签约套餐
                    if(isDep === "8"){
                        $("#ydywGroupStr").val($("#ydywGroupStr").val()+id+",");
                    }
                    //签约用户
                    if(isDep === "7"){
                        $("#ydywPhoneStr").val($("#ydywPhoneStr").val()+ phone +",");
                    }
				});
            }else {
                $(window.frames['flowFrame'].document).find("#right option").each(function() {
                    //1客户成员2是机构3是机构（包含子机构）4群组5客户属性 6属性值
                    var  id =  $(this).val();
                    var et = $(this).attr("et");
                    $("#signClientIds").val($("#signClientIds").val()+$(this).val()+",");
                    if(et == "1"){
                        var mobile = $(this).attr("mobile");
                        $("#phoneStr1").val($("#phoneStr1").val()+mobile+",");
                    }else if(et == "2"){
                        $("#depIdStr").val($("#depIdStr").val()+id+",");
                        $("#depIdStrTemp").val($("#depIdStrTemp").val()+id+",");
                    }else if(et == "3"){
                        $("#depIdStr").val($("#depIdStr").val()+"e"+id+",");
                        $("#depIdStrTemp").val($("#depIdStrTemp").val()+"e"+id+",");
                    }else if(et == "4"){
                        $("#groupStr").val($("#groupStr").val()+id+",");
                        $("#groupStrTemp").val($("#groupStrTemp").val()+id+",");
                    }else if(et == "5"){
                        $("#proIdStr").val($("#proIdStr").val()+id+",");
                        $("#proIdStrTemp").val($("#proIdStrTemp").val()+id+",");
                    }else if(et == "6"){
                        $("#proValueIdStr").val($("#proValueIdStr").val()+id+",");
                        $("#proValueIdStrTemp").val($("#proValueIdStrTemp").val()+id+",");
                    }else if(et == "7"){
                        var mobile = $(this).attr("mobile");
                        $("#wxphoneStr").val($("#wxphoneStr").val()+mobile+",");
                    }else if(et=="9"){
                        $("#ydywGroupStr").val($("#ydywGroupStr").val()+id+",");
                        $("#ydywGroupStrTemp").val($("#ydywGroupStrTemp").val()+id+",");
                    }else if(et=="8"){
                        var mobile = $(this).attr("mobile");
                        $("#ydywPhoneStr").val($("#ydywPhoneStr").val()+mobile+",");
                    }
                });
            }


			$("#ygtxl").remove();
			/*客户通讯录 详情 人*/
			if($(window.frames['flowFrame'].document).find("#manCount").html()!=0){
                $("#vss").append("<tr  class='div_bd' id='ygtxl' style='background-color:#ffffff'><td style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("dxkf","dxkf_srzf_page6_khtxl")+"</td><td style='border-left:0;border-right:0'  align='center' valign='middle' ><a onclick='javascript:showInfo();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage("common","common_details")+"(<label style='color:#0e5ad1' id='choiceNum'></label>"+getJsLocaleMessage("common","common_person")+")</font> </a></td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px'><a  onclick='javascript:delAddr()'><img border='0' src='<%=commonPath%>/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
            }
			$(window.frames['flowFrame'].document).find("#rightSelectTempAll").html($(window.frames['flowFrame'].document).find("#right").html());
			$("#infoDiv").dialog("close");
			$("#choiceNum").html($(window.frames['flowFrame'].document).find("#manCount").html());
			showSelect();
		}

		function setDefault()
		{
			if(confirm(getJsLocaleMessage('dxkf','dxkf_dxqf_page_setdefault'))) {
				var lguserid = $('#lguserid').val();
				var lgcorpcode = $('#lgcorpcode').val();
				var busCode = $("#busCode").val();
				var priority = $("#priority").val();
				var spUser = $("#spUser").val();
				var isReply = $("input:radio[name='isReply']:checked").attr("value");
				$.post("kfs_sendClientSMS.htm?method=setDefault", {
					lguserid: lguserid,
					lgcorpcode: lgcorpcode,
					busCode: busCode,
					priority: priority,
					spUser: spUser,
					isReply: isReply,
					flag: "11"			
					}, function(result) {
					if (result == "seccuss") {
						// alert("当前选项设置为默认成功！");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_setdefaultsuccess'));
						return;
					} 
					else if(result == "fail"){
						// alert("当前选项设置为默认失败！");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_setdefaultfalse'));
						return;
					}
				});
			}
		}
		</script>
	</body>
</html>
