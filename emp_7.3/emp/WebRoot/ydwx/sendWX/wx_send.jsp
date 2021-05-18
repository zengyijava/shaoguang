<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.netnews.entity.LfDfadvanced"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Random"%>
<%@ page import="java.util.regex.Pattern"%>
<%@ page import="java.util.regex.Matcher" %>
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

	Random rand = new Random();
	Integer flag = new Integer(rand.nextInt());
	String str_flag = flag.toString();
	session.setAttribute("flag", str_flag);

	String isTrue = (String) request.getAttribute("isTrue");
	String context = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	
	
	@SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>)request.getAttribute("sendUserList");
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");
	String lgus=request.getAttribute("lguserid")+"";
	//上传返回标志,
	String isPre = (String) request.getAttribute("isPre");
	//上传返回总数
	Long AllNum = (Long) request.getAttribute("AllNum");
	//上传返回信息
	String message = (String) request.getAttribute("message");
	//实体
	String reinfo = (String) request.getAttribute("reinfo");
	String info = (String) request.getAttribute("info");
	String re = (String) request.getAttribute("re");

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String isFlow = (String) request.getAttribute("isFlow");
	String result = session.getAttribute("mcs_batchResult")==null?"-1":(String)session.getAttribute("mcs_batchResult");
	boolean isLoadTree = false;
    String findResult= (String)request.getAttribute("findresult");
    LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
    String userName = ((LfSysuser)session.getAttribute("loginSysuser")).getName() ;
    String depSign = (String)request.getAttribute("depSign");
    String taskId = (String) request.getAttribute("taskId");
    @SuppressWarnings("unchecked")
    List<LfTemplate> tmpList = (List<LfTemplate>)request.getAttribute("tmpList");//获取短信模板
    //为了获得网讯新增的权限
    String menuOther= titleMap.get("manger"); 
    
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String skinType = "";
	/*/p_ydwx/frame/frame4.0/skin/default*/
	Pattern patt = Pattern.compile("frame(\\d\\.\\d)");
	Matcher matcher = patt.matcher(skin);
	if(matcher.find()){
		skinType = matcher.group(1);
	}
    String lgcorpcode = request.getParameter("lgcorpcode");
    String lguserid = request.getParameter("lguserid");
    String oldtaskId = request.getParameter("oldtaskId");
    //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
  	String specialChar = StaticValue.getSmscontentSpecialcharStr();
  	String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp" %>
		<title>网讯</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css" type="text/css?V=<%=StaticValue.getJspImpVersion()%>" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydwx/sendWX/css/wx_send.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydwx/sendWX/css/wx_send_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/wx_send.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<script>
			var specialChar = "<%=specialChar%>";
			var special = specialChar.split(",");
			//最大值限制
			var ZIP_SIZE = "<%=StaticValue.ZIP_SIZE/1024/1024%>";
			var MAX_SIZE = "<%=StaticValue.MAX_SIZE/1024/1024%>";
			var MAX_PHONE_NUM = "<%=StaticValue.MAX_PHONE_NUM/10000%>";
		</script>

</head>
<body class="wx_send" id="wx_send">
	<%-- header开始 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<%-- header结束 --%>
		<div id="container">
			<div id="rContent" class="rContent">
                <input type="hidden" id="skinType" value="<%=skinType%>"/>
				<input type="hidden" id="empLangName" value="<%=empLangName%>"/>
				<input type="hidden" id="fuzhi" value=""/>
				<input type="hidden" id="formName" value="form1"/>
				<input type="hidden" id="cpath" value="<%=path%>"/>
				<input type="hidden" id="gt1" name="gt1" value=""/>
				<input type="hidden" id="gt2" name="gt2" value=""/>
				<input type="hidden" id="gt3" name="gt3" value=""/>
				<input type="hidden" id="gt4" name="gt4" value=""/>
				<input type="hidden" id="gt4" name="gt4" value=""/>
				<%--标示出现异常--%>
				<input type="hidden" id="error" name="error" value="" />
				<input id="path" value="<%=inheritPath%>" type="hidden" />
				<div id="batchFileSend" class="block">
					<form id="form2" name="form2" action="wx_send.htm?method=upNumber" method="post" enctype="multipart/form-data" target="hidden_iframe">
						<input name="hidemsg" type="hidden" value="" id="reinfo">
						<input type="hidden" name="phoneStr" id="phoneStr" value=","/>
						<input type=hidden name="flag" value="<%=str_flag%>"/>
						<input type="hidden" id="tss" name="tss" value="e" />
						<input type="hidden" value="3" id="bmtType" name="bmtType" />
						<%--群组id集合--%>
                        <input type="hidden" id="groupIds"  name="groupIds" value=""/>
                        <%--客户机构id集合--%>
						<input type="hidden" id="cliDepIds"  name="cliDepIds" value=""/>
						<%--员工机构id集合--%>
                        <input type="hidden" id="empDepIds"  name="empDepIds" value=""/>
                        <%--员工id集合--%>
						<input type="hidden" name="empIds" id="empIds" value=""/>
						<%--客户id集合--%>
						<input type="hidden" name="cliIds" id="cliIds" value=""/>
						<%--自建id集合--%>
						<input type="hidden" name="malIds" id="malIds" value=""/>
						<%--选择用户对象号码串--%>
						<input type="hidden" value="" id="phoneStr1" name="phoneStr1"/>
						<%--用于数据回显--%>
						<input type="hidden" id="rightSelectedUserOption" value=""/>
						<%-- 群组中的客户个人信息 --%>
						<input type="hidden" id="groupClient" name="groupClient"  value=""/>
						<input type="hidden" value="0" id="isOk" name="isOk" />
						<input type="hidden" name="groupStrTemp" id="groupStrTemp" value=","/><%--临时保存员工群组信息--%>
						<input type="hidden" name="depIdStrTemp" id="depIdStrTemp" value=","/><%-- 临时保存depid字符串，在选择员工取消时用到 --%>
						<input type="hidden" value="0" id="hidIsDoOk" name="hidIsDoOk"/><%-- 控制选择人员弹出框关闭时清空。0为关闭操作；1为确定操作 --%>
						<input type="hidden" value="1" id="sendType" name="sendType" />
						<input type="hidden" value ="" id="circleSubNo" name="circleSubNo"/> <%-- 尾号 --%>
						<input type="hidden" value="" id="preStr" name="preStr" />
						<input type="hidden" value="" id="dtMsg" name="dtMsg" />
						<input type="hidden" value="<%=request.getAttribute("reinfo")%>" id="pram" name="pram" />
						<input type="hidden" value="" id="queryString" name="queryString" />
						<input type="hidden" value ="" id="gateCount" name="gateCount"/><%--  发送帐号绑定的通道个数 --%>
						<input type="hidden" value ="" id="gateTLCount" name="gateTLCount"/><%--  发送帐号主通道号+循环尾号的位数超过２０位个数 --%>
						<input type="hidden" value ="" id="subNo" name="subNo"/> <%-- 尾号 --%>
						<input type="hidden" value ="<%=taskId %>" name="taskId" id="taskId"/>
						<input type="hidden" value="" id="inputContent" name="inputContent" /><%--临时存储手工输入内容 --%>
						<input type="hidden" value="" id="busCode1" name ="busCode1"/><%--临时存储buscode --%>
						<input type="hidden" value="" id="spUser1" name="spUser1"/><%--临时存储spUser --%>
						<input type="hidden" value ="" id="sendFlag" name="sendFlag"/><%--  发送帐号主通道号+循环尾号的位数超过２０位个数 --%>
						<input type="hidden" value="" id="phoneStr2" name="phoneStr2"/><%-- 保存由手动输入的员工电话号码 --%>
						<input type="hidden" value="" id="inputphone" name="inputphone"/><%-- 批量输入的电话号码 --%>
						<input type="hidden" value="" id="isCharg" name="isCharg"/>
						<input type="hidden" value="0" id="SendReq" name="SendReq">
						<input type="hidden" value="" id="smsTail" name="smsTail">	<%-- 隐藏的贴尾内容，用于后台拼装--%>
						<input id="cpath" value="<%=path %>" type="hidden" />
						<input id="lguserid" value="<%=lguserid %>" type="hidden" />
						<input id="wxtempcode" value="<%=ViewParams.WXTEMPCODE %>" type="hidden" />
						<%--草稿箱--%>
	                    <input type="hidden" id="draftFile" name="draftFile" value=""/>
	                    <input type="hidden" id="draftId" name="draftId" value=""/>
						<div id="allfilename" ></div>
						<div id="hiddenValueDiv"></div>
						<div id="eq_sendDiv">
							<span class="righttitle">
								<emp:message key="ydwx_wxfs_jtwxfs_wxzt" defVal="网讯主题：" fileName="ydwx"></emp:message>
							</span>
							<input id="taskname" name="taskname" type="text" class="graytext input_bd div_bd" maxlength="20" value="<emp:message key="ydwx_wxfs_jtwxfs_wxzt_value_1" defVal="不作为短信内容发送" fileName="ydwx"></emp:message>"/>
						</div>
						<div class="clear2"></div>
						<div id="eq_sendDiv">
							<span class="righttitle">
							<emp:message key="ydwx_wxfs_jtwxfs_fshm" defVal="发送号码：" fileName="ydwx"></emp:message></span>
							<div id="dds" class="div_bd">
								<table id="vss">
									<tr id="first" class="title_bd">
										<td width="100" class="div_bd title_bg first_td"><emp:message key="ydwx_wxfs_jtwxfs_fshm_td_1" defVal="类型" fileName="ydwx"></emp:message></td>
										<td class="div_bd title_bg"><emp:message key="ydwx_wxfs_jtwxfs_fshm_td_2" defVal="号码" fileName="ydwx"></emp:message></td>
										<td width="82" class="div_bd title_bg first_td"><emp:message key="ydwx_wxfs_jtwxfs_fshm_td_3" defVal="操作" fileName="ydwx"></emp:message></td>
									</tr>
								</table>
							</div>
							<div class="selectFileTable">
							<table width="100%" class="chooseTable">
									<tr id="picTab">
										<td align="center" onclick="showInfo();" class="div_bd div_bg selectPerson">
											<div class="selectPersonDiv">
												<a class="selectEmp"></a>
												<div class="mt10"><emp:message key="ydwx_wxfs_jtwxfs_fshm_xzry" defVal="选择人员" fileName="ydwx"></emp:message></div>
											</div>
										</td>
										<td align="center" class="div_bd div_bg selectFileTd">
											 <div class="selectFileDiv">
												<a class="importFile"/></a>
												<div class="mt10"><emp:message key="ydwx_wxfs_jtwxfs_fshm_drwj" defVal="导入文件" fileName="ydwx"></emp:message></div>
												<div id="filesdiv">
													<input id='numFile1' name='numFile1'  type=file onchange="addFiles()" class="numfileclass"/>
												</div>
											 </div>
										</td>
										<td align="center" class="div_bd div_bg bulkImport" onclick="bulkImport();">
											 <a class="bulkImport"/></a><div class="mt10"><emp:message key="ydwx_wxfs_jtwxfs_fshm_plsr" defVal="批量输入" fileName="ydwx"></emp:message></div>
										</td>
									</tr>
									<tr>
										<td colspan="3" align="center" class="div_bd div_bg">
											<table width="235px" class="div_bd">
												<tr>
													<td width="192px">
														<input value="<emp:message key='ydwx_wxfs_jtwxfs_fshm_value' defVal=' 请输入手机号' fileName='ydwx'></emp:message>" class="graytext" type="text" onkeyup="addph(this)" onpaste="return !clipboardData.getData('text').match(/\D/)" maxlength="21" name="tph" id="tph"/>
													</td>
													<td onclick="addph2()" class="addPhone div_bd"><emp:message key="ydwx_wxfs_jtwxfs_fshm_btn_tj" defVal="添加" fileName="ydwx"></emp:message></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</div>
						<div class="clearDiv"></div>
						<div align="right" class="downlinksWrapper">
							<a id="downlinks"><emp:message key="ydwx_wxfs_jtwxfs_gstx" defVal="格式提示" fileName="ydwx"></emp:message></a>
						</div>
						<div id="eq_sendDiv" class="sendDiv">
							<span class="righttitle" >
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr" defVal="短信内容：" fileName="ydwx"></emp:message>
							</span>
							<table class="div_bd smsContent">
								<tr class="smsContent_tr1">
									<td class="div_bd smsContent_tr1_td1">
										<a id ="ctem" class="div_bg div_bd" href="javascript:chooseTemp('<%=skinType%>')">
											<emp:message key="ydwx_wxfs_jtwxfs_dxnr_td_1" defVal="点击选择短信内容" fileName="ydwx"></emp:message>
										</a>
										<a id ="qtem" class="div_bg div_bd" href="javascript:tempNoShow()">
											<emp:message key="ydwx_wxfs_jtwxfs_dxnr_td_2" defVal="点击取消短信内容" fileName="ydwx"></emp:message>
										</a>	&nbsp;
										<a onclick="showDraft('<%=skinType%>')" class="showDraft">
											<emp:message key="ydwx_wxfs_jtwxfs_dxnr_td_3" defVal="草稿箱" fileName="ydwx"></emp:message>
										</a>
									</td>
									<td class="div_bd signature">
										<emp:message key="ydwx_wxfs_jtwxfs_dxnr_td_4" defVal="前缀签名：" fileName="ydwx"></emp:message>
										<input type="checkbox" value="" name="depSign" id="depSign"  onclick="setDepSign(this)"/>
										&nbsp;<emp:message key="ydwx_wxfs_jtwxfs_dxnr_td_5" defVal="机构" fileName="ydwx"></emp:message>&nbsp;&nbsp;
										<input type="checkbox" value="" name="nameSign" id="nameSign"  onclick="setNameSign(this)"/>
										&nbsp;<emp:message key="ydwx_wxfs_jtwxfs_dxnr_td_6" defVal="姓名" fileName="ydwx"></emp:message>
									</td>
								</tr>
								<tr class="showSmsContent">
									<td colspan="2">
										<textarea class="msg2" name="msg" rows="5" id="contents" onblur="eblur($(this))"></textarea>
									</td>
								</tr>
								<tr id="tail-area">
									<td colspan="2" id="tail-text" class="div_bd">
										&nbsp;&nbsp;&nbsp;&nbsp;
										<font><emp:message key="ydwx_wxfs_twnr" defVal="贴尾内容：" fileName="ydwx"></emp:message></font>
										<font id="smsTailContens"></font>
									</td>
								</tr>
							</table>
							<font class="smsWordCount">
								<b id="strlen"> 0 </b><b id="maxLen">/900</b>
							</font>
							<font class="operator">
								<emp:message key="ydwx_common_yidong" defVal="移动" fileName="ydwx"></emp:message>(<b id="ft1">0</b>)&nbsp;&nbsp;
								<emp:message key="ydwx_common_liantong" defVal="联通" fileName="ydwx"></emp:message>(<b id="ft2">0</b>)&nbsp;&nbsp;
								<emp:message key="ydwx_common_dianxin" defVal="电信" fileName="ydwx"></emp:message>(<b id="ft3">0</b>)&nbsp;&nbsp;
								<emp:message key="ydwx_common_guowai" defVal="国外" fileName="ydwx"></emp:message>(<b id="ft4">0</b>)
							</font>
						</div>
						<div class="clear2"></div>
						<div id="eq_sendDiv">
							<span class="righttitle" >
								<emp:message key="ydwx_wxfs_jtwxfs_wxnr" defVal="网讯内容：" fileName="ydwx"></emp:message>
							</span>
							<%--
							<p style="margin-top:7px">
							请选择
							</p> --%>
							<div class="separator">
								<input type="hidden" id="netid" name="netid" value=""/>
								<input type="button"  class="btnClass4 chooseNetTpl" value="<emp:message key='ydwx_wxfs_jtwxfs_wxnr_value_1' defVal='选择静态网讯' fileName='ydwx'></emp:message>" onclick="chooseNetTpl('<%=skinType%>')"/>
								<%-- 增加了权限控制    may add --%>
								<%if(btnMap.get(menuOther+"-1")!=null){%>
									<a class="addNetTpl" onclick="toAdd()"><emp:message key="ydwx_wxfs_jtwxfs_wxnr_a" defVal="新增网讯" fileName="ydwx"></emp:message></a>
								<%} %>
							</div>
							<div id="templatepre"  class='div_bg'></div>
						</div>
						<div class="clear2"></div>
						<div id="eq_sendDiv" class="eq_sendDiv_c">
							<span class="sendTime"><emp:message key="ydwx_wxfs_jtwxfs_fssj" defVal="发送时间：" fileName="ydwx"></emp:message></span>
							<div class="sendTimeDiv">
								<input type="radio" name="timerStatus" value="0" id="sendNow" checked="checked" onclick="$('#time2').hide()"/>
								&nbsp;<emp:message key="ydwx_wxfs_jtwxfs_fssj_td_1" defVal="立即发送" fileName="ydwx"></emp:message>
								<input type="radio" name="timerStatus" value="1" class="timer" onclick="$('#time2').show()"/>
								&nbsp;<emp:message key="ydwx_wxfs_jtwxfs_fssj_td_2" defVal="定时发送" fileName="ydwx"></emp:message>
								<label id="time2">
									<input type="text" class="Wdate div_bd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})" id="timerTime" name="timerTime" value="">
								</label>
							</div>
						</div>
						<div class="advancedSettings" id="eq_sendDiv2">
							<span id="u_o_c_explain" class="div_bg">
								<b><emp:message key="ydwx_wxfs_jtwxfs_gjsz" defVal="高级设置" fileName="ydwx"></emp:message></b>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<a id="foldIcon" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a>
							</span>
							<div id="moreSelect" class="div_bg">
								<div class="defaultSettings">
									<a id="setdefualt" href="javascript:setDefault()" class="alink" >
										<emp:message key="ydwx_wxfs_jtwxfs_gjsz_td_1" defVal="选项存为默认" fileName="ydwx"></emp:message>
									</a>
								</div>
								<%-- 业务类型 --%>
								<div id="selfSelect">
								<table>
									<tr>
										<td class="busType">
											<font>
												<emp:message key="ydwx_wxfs_jtwxfs_gjsz_td_2" defVal="业务类型：" fileName="ydwx"></emp:message>
											</font>
										</td>
										<td>
											<select id="busCode"  class="input_bd"  name="busCode" isInput="false">
													<%
														if (busList != null && busList.size() > 0) {
															String busCode = lfDfadvanced != null?lfDfadvanced.getBuscode():"";
															for (LfBusManager busManager : busList) {
													%>
													<option value="<%=busManager.getBusCode()%>" <%=busCode != null && !"".equals(busCode) && busCode.equals(busManager.getBusCode())?"selected":"" %>>
														<%String name = busManager.getBusName().replace("默认业务",MessageUtils.extractMessage("ydwx","ydwx_defaultBussiness",request));%>
														<%=name.replace("<","&lt;").replace(">","&gt;")%>(<%=busManager.getBusCode()%>)
													</option>
													<%
														}
														}
													%>
										</select>
										</td>
									</tr>
									<tr>
										<td class="spUserTd">
											<font>
												<emp:message key="ydwx_common_SPzhanghaos" defVal="SP账号：" fileName="ydwx"></emp:message>
											</font>
										</td>
										<td>
											<select id="spUser"  class="input_bd" name="spUser" onchange="getGtInfo()" isInput="false">
												<%
												if (spUserList != null && spUserList.size() > 0)
												{
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
								</table>
								</div>
							</div>
						</div>
						<%-- 增加权限控制 --%>
						<div class="b_F_btn">
							<input type="hidden" value="1" id="subState" name="subState"/>
							<%if(btnMap.get(menuCode+"-1")!=null){%>
								<input id="toDraft" class="btnClass5 mr23" type="button" value="<emp:message key='ydwx_common_btn_zccg' defVal='暂存草稿' fileName='ydwx'></emp:message>"/>
								<input id="subSend" class="btnClass5 mr23" type="button" value="<emp:message key='ydwx_common_btn_tijiao' defVal='提交' fileName='ydwx'></emp:message>"/>
								<input id="qingkong" class="btnClass6" type="button" value="<emp:message key='ydwx_common_btn_chongzhi' defVal='重置' fileName='ydwx'></emp:message>" onclick="reloadPage();"/>
							<%}%>
						</div>
					</form>
					<div class="clear2"></div>
					<%--选择发送对象--%>
					<div id="infoDiv" title="<emp:message key='ydwx_wxfs_jtwxfs_fshm_xzry_td1' defVal='选择发送对象' fileName='ydwx'></emp:message>">
						<iframe id="flowFrame" name="flowFrame" src="<%=context %>/wx_userInfo.jsp?userid1=<%=lgus %>" marginwidth="0" scrolling="no" frameborder="no"></iframe>
						<table>
							<tr>
								<td>
									<input type="button" class="btnClass5 mr23" value="<emp:message key='common_confirm_p' defVal='确 定' fileName='common'></emp:message>" onclick="doOk()" />
									<input type="button" class="btnClass6 select_cancel"  value="<emp:message key="common_cancel_p" defVal="取 消" fileName="common"></emp:message>" onclick="doSelectEClose()" />
									<input type="button" class="btnClass6 select_clear" style="display: none;" value="<emp:message key='common_clean_p' defVal='清 空' fileName='common'></emp:message>" onclick="clearUserInfo()"/>
									<br/>
								</td>
							</tr>
						</table>
					</div>
					<%--发送预览--%>
					<div id="detail_Info">
						<div id="wxtempDiv">
							<div class="pageNavigation">
								<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
								<select id="selDh"></select>
							</div>
							<div class="phoneBackground">
								<iframe id="nm_preview_common" src=""></iframe>
							</div>
						</div>
						<div class="sendDetail">
							<table id="infos" width="98%">
							  <tr>
									<td class="infostd">
										<emp:message key='ydwx_wxfs_fsts' defVal='发送条数：' fileName='ydwx'></emp:message>
										<span id="yct"></span>
									</td>
									<td class="infostd" id="showyct">
										<emp:message key='ydwx_wxfs_jgyue' defVal='机构余额：' fileName='ydwx'></emp:message>
										<span id="ct"></span>
									</td>
							  </tr>
							  <tr>
								  <td colspan="2">
									  <label id="messages2">
										  <font></font>
									  </label>
								  </td>
							  </tr>
							  <tr>
								  <td colspan=2>
										<label id="messages1">
											<font>
												<emp:message key='ydwx_wxfs_yuexx' defVal='余额不足不允许进行发送!' fileName='ydwx'></emp:message>
											</font>
										</label>
								  </td>
							  </tr>
							  <tr>
								  <td class="infostd" colspan=2 id="shospfee">
									  <emp:message key='ydwx_wxfs_spjgyue' defVal='SP账号余额：' fileName='ydwx'></emp:message>
									  <span id="spfee"></span>
								  </td>
							  </tr>
							  <tr>
								  <td colspan=2>
									  <emp:message key='ydwx_wxfs_yxhms' defVal='有效号码数：' fileName='ydwx'></emp:message>
									  <span id="effs"></span>
								  </td>
							  </tr>
							  <tr>
								  <td colspan=2>
									  <emp:message key='ydwx_wxfs_tjhms' defVal='提交号码数：' fileName='ydwx'></emp:message>
									  <span id="counts"></span>
								  </td>
							  </tr>
							  <tr>
								  <td colspan=2 align="center">
								   <div id="showeffinfo" class="div_bg">
									   <p>
										   <emp:message key='ydwx_wxfs_wxhm' defVal='无效号码' fileName='ydwx'></emp:message>
										   (<span id="alleff"></span>)&nbsp;&nbsp;
								   			<a id="arrowhead" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a>
									   </p>
								   </div>
								  </td>
								</tr>
							</table>
						</div>
						<div id="effinfo" class="div_bg">
						   <center>
								<table id="effinfotable">
									<tr class="effinfotable_tr1">
										<td colspan="2"></td>
									</tr>
									<tr>
										<td align="left">
											<emp:message key='ydwx_wxfs_hmdhm' defVal='黑名单号码：' fileName='ydwx'></emp:message>
										</td>
										<td>
											<span id="blacks"></span>
										</td>
										<td></td>
									</tr>
									<tr>
										<td align="left">
											<emp:message key='ydwx_wxfs_cfhm' defVal='重复号码：' fileName='ydwx'></emp:message>
										<td>
											<span id="sames"></span></td>
										</td>
										<td></td>
									</tr>
									<tr>
										<td align="left">
											<emp:message key='ydwx_wxfs_gsff' defVal='格式非法：' fileName='ydwx'></emp:message>
										</td>
										<td>
											<span id="legers"></span>
										</td>
										<td id="preinfonum">
											<%if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) {%>
											<a href="javascript:uploadbadFiles()"><emp:message key='ydwx_wxfs_xqxz' defVal='详情下载' fileName='ydwx'></emp:message></a>
											<%}%>
											<input type="hidden" id="badurl" name="badurl"  value="">
										</td>
									</tr>
								</table>
							</center>
						</div>
						<div class="detail_Info_separator"></div>
						<div id="footer">
							 <input id="btsend" onclick="btsend()" class="btnClass5 mr10 left" type="button" value="<emp:message key='ydwx_common_btn_fasong' defVal='发送' fileName='ydwx'></emp:message>"/>
							 <input id="btcancel" onclick="btcancel1();" class="btnClass6 left" type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>"/>
						</div>
					</div>
					<div class="clear2"></div>
					<%--格式提示--%>
					<div id="infomodel">
					   <table>
							<tr>
								<td class="infomodel_td1" colspan="2" align="left">
									<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_1" defVal="文件格式如图：" fileName="ydwx"></emp:message>
								</td>
							</tr>
							<tr>
								<td class="infomodel_td2">
									<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_2" defVal="txt格式：" fileName="ydwx"></emp:message>
								</td>
								<td rowspan="2">
									<%--图片文件来源--%>
									<img id="foldIcon" src="<%=commonPath%>/common/img/mmsphone-<%=empLangName%>.png"/>
								</td>
							</tr>
							<tr>
								<td class="infomodel_td3">
									<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_3" defVal="xls,xlsx,et格式：" fileName="ydwx"></emp:message>
								</td>
							</tr>
							<tr>
								<td class="infomodel_td4" align="left" colspan="2">
									<span>
										<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_4" defVal="注意：" fileName="ydwx"></emp:message>
									</span>
								</td>
							</tr>
							<tr>
								<td align="left" colspan="2">
									1.<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_5" defVal="手机号码格式不正确，在上传时将由EMP平台过滤；" fileName="ydwx"></emp:message>
									<br/>
									2.<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_6" defVal="号码包含于黑名单内，在上传时将由EMP平台过滤；" fileName="ydwx"></emp:message>
									<br/>
									3.<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_7" defVal="文件需小于" fileName="ydwx"></emp:message>
									<%=StaticValue.MAX_SIZE/1024/1024%>
									<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_8" defVal="M，有效号码需少于" fileName="ydwx"></emp:message>
									<%=StaticValue.MAX_PHONE_NUM/10000%>
									<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_9" defVal="万；" fileName="ydwx"></emp:message><br/>
									4.<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_10" defVal="文件仅支持txt、zip、xls、xlsx、rar、et格式。" fileName="ydwx"></emp:message>
								</td>
							</tr>
					   </table>
				   </div>
					<%--等待旋转--%>
					<div id="probar">
						<p>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<emp:message key="ydwx_common_chlzqs" defVal="处理中,请稍等....." fileName="ydwx"></emp:message>
						</p>
						<div id="shows">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<img src="<%=commonPath%>/common/img/loader.gif"/>
						</div>
					</div>
					<%--模板预览--%>
					<div id="divBox" class="hideDlg" title="<emp:message key='ydwx_common_mubanyulan' defVal='模板预览' fileName='ydwx'></emp:message>">
						<div class="tempPreview" align="center">
							<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
							<select class="ym"></select>
						</div>
						<div class="tempPreviewPhone">
							<iframe id="nm_preview_common1" src=""></iframe>
						</div>
					</div>
				</div>
			</div>
			<iframe name="hidden_iframe" id="hidden_iframe"></iframe>
			<%--静态网讯内容选择--%>
			<div id="tmplDiv" title="<emp:message key='ydwx_wxfs_jtwxfs_dxnr_xzjtwx_td1' defVal='静态网讯内容选择' fileName='ydwx'></emp:message>">
				<iframe id="tempFrame" name="tempFrame" marginwidth="0" scrolling="no" frameborder="no" src ="<%=commonPath%>/common/blank.jsp"></iframe>
			</div>
			<%--草稿箱--%>
            <div id="draftDiv" title="<emp:message key='ydwx_wxfs_jtwxfs_dxnr_cgx_td1' defVal='草稿箱选择' fileName='ydwx'></emp:message>">
                <iframe id="draftFrame" name="draftFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
                <div>
					<input class="btnClass5 mr23 ydwx_borderradius" onclick="draftSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
					<input class="btnClass6 ydwx_borderradius" onclick="draftCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
					<%--<%
						if(MessageUtils.extractMessage("ydwx","ydwx_yy",request).equals("ZH_HK")){
					%>
						<input style="width: 85px;background-color: #67cd28;cursor: pointer;height: 30px;border-radius:5px;border: 1px solid #ccc;"  onclick="draftSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
                    	<input style="width: 85px;background-color: #f1f1f9;cursor: pointer;height: 30px;border-radius:5px;border: 1px solid #ccc;" onclick="draftCancel()" value="<emp:message key='ydwx_common_btn_quxiao' defVal='取消' fileName='ydwx'></emp:message>" type="button"/>
					<%
						}else{
					 %>
						<input class="btnClass5 mr23" onclick="draftSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
                    	<input class="btnClass6" onclick="draftCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
					<%
						}
					%>--%>
                    <br/>
                </div>
            </div>
			<%--短信内容选择--%>
			<div id="tempDiv" title="<emp:message key='ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td1' defVal='短信内容选择' fileName='ydwx'></emp:message>">
				<iframe id="contentFrame" name="contentFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
				<div>
					<input class="btnClass5 mr23 ydwx_borderradius" onclick="tempSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
					<input class="btnClass6 ydwx_borderradius" onclick="tempCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
				<%--<%
					if(MessageUtils.extractMessage("ydwx","ydwx_yy",request).equals("ZH_HK")){
				%>
					<input style="width: 85px;background-color: #67cd28;cursor: pointer;height: 30px;border-radius:5px;border: 1px solid #ccc;" onclick="tempSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
					<input style="width: 85px;background-color: #f1f1f9;cursor: pointer;height: 30px;border-radius:5px;border: 1px solid #ccc;" onclick="tempCancel()" value="<emp:message key='ydwx_common_btn_quxiao' defVal='取消' fileName='ydwx'></emp:message>" type="button"/>
				<%
					}else{
				 %>
					<input class="btnClass5 mr23" onclick="tempSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
					<input class="btnClass6" onclick="tempCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
				<%
					}
				%>--%>
				<br>
			</div>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear2"></div>
		<%--批量输入--%>
		<div id="bulkImport_box" title="<emp:message key='ydwx_wxfs_jtwxfs_fshm_plsr_td1' defVal='批量号码输入' fileName='ydwx'></emp:message>">
			   <div class="imporInner div_bd">
			   		<textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onblur="formatTelNum('#bNum')"></textarea>
			   		<textarea name="importAreaTemp" id="importAreaTemp"></textarea>
			   </div>
			   <div class="bultMark">
			   		<span id="bultNum">
						<emp:message key="ydwx_wxfs_jtwxfs_fshm_plsr_td2" defVal="当前共" fileName="ydwx"></emp:message>
						<font color='blue'><b id="bNum"></b></font>
			   			<emp:message key="ydwx_wxfs_jtwxfs_fshm_plsr_td3" defVal="/20000个号码" fileName="ydwx"></emp:message>
			   		</span>
			   		<div class="bultNum_separator"></div>
			   		<div class="numSplit">
			   			<emp:message key="ydwx_wxfs_jtwxfs_fshm_plsr_td4" defVal="多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="ydwx"></emp:message>
			   		</div>
			   </div>
			   <div class="bultBtn">
				   <input onclick="previewTel()" class="btnClass5 mr23 ydwx_borderradius" type="button" value="<emp:message key='ydwx_common_btn_queren' defVal='确认' fileName='ydwx'></emp:message>">
				   <input onclick="bultCancel()"  class="btnClass6 ydwx_borderradius" type="button" value="<emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message>">
				   <br/>
			   <%--<%
					if(MessageUtils.extractMessage("ydwx","ydwx_yy",request).equals("ZH_HK")){
				%>
					 	<input onclick="previewTel()" style="width: 85px;background-color: #67cd28;cursor: pointer;height: 30px;border-radius:5px;border: 1px solid #ccc;" type="button" value="<emp:message key='ydwx_common_btn_queren' defVal='确认' fileName='ydwx'></emp:message>">
			   			<input onclick="bultCancel()" style="width: 85px;background-color: #f1f1f9;cursor: pointer;height: 30px;border-radius:5px;border: 1px solid #ccc;text-align: center;" value="<emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message>"><br/>
				<%
					}else{
				 %>
				 	<input onclick="previewTel()" class="btnClass5 mr23" type="button" value="<emp:message key='ydwx_common_btn_queren' defVal='确认' fileName='ydwx'></emp:message>">
		   			<input onclick="bultCancel()"  class="btnClass6" type="button" value="<emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message>"><br/>
				<%
					}
				%>--%>
			   </div>
		</div>
		<%--发送成功后的提示--%>
		<div id="message" title="<emp:message key="ydwx_common_btn_tishi" defVal="提示" fileName="ydwx"></emp:message>">
			<div class="message_separator"></div>
			<center>
				<label><emp:message key="ydwx_wxfs_fszwgcg" defVal="发送至网关成功" fileName="ydwx"></emp:message></label>
				<a href="javascript:sendRecord(<%=oldtaskId %>,<%=lguserid%>,<%=lgcorpcode%>)"  class="alink">
					<emp:message key="ydwx_wxfs_ckfsjl" defVal="查看发送记录" fileName="ydwx"></emp:message>
				</a>
			</center>
			<br>
			<div class="close_separator"></div>
			 <center>
				<input id="close" onclick="closemessage();" class="btnClass6" type="button" value="<emp:message key="ydwx_common_btn_guanbi" defVal="关闭" fileName="ydwx"></emp:message>"/>
			</center>
		</div>
		<div class="clear2"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/wx_send.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(function(){
			  $('#busCode,#spUser').isSearchSelect({'width':'180','isInput':false,'zindex':0});
		   })
		</script>
		
		<script>
			var depSign = "<%=depSign%>";
			var nameSign = "<%=userName%>";
			var commonPath="<%=commonPath%>"
			var findresult="<%=findResult%>";
			var result = "<%=result%>";
			var smaacount=getJsLocaleMessage("ydwx","ydwx_common_SPzhanghao");
			var base_path="<%=path%>";
			//加载时候初始化相关数据
			var subcount = 0;
			$(document).ready(
				function(){
					//短信预览窗口
					$("#detail_Info").dialog({
						autoOpen: false,
						modal:true,
						title:getJsLocaleMessage("ydwx","ydwx_jtwxfs_97"),
						height:520,
						width:<%="zh_HK".equals(empLangName)?560:520%>,
						closeOnEscape: false,
						open:function(){
							hideSelect();
							$(".ui-dialog-titlebar-close").hide();
							 var ct = $.trim($("#ct").text());
							 var yct =$.trim($("#yct").text());
							 var isCharg = $("#isCharg").val();
							 if(isCharg == "true")
							 {
								 if(eval(ct)<eval(yct))
								 {
									 $("#messages1").show();
									 $("#btsend").attr("disabled","disabled");
								 }
								 else
								 {
									$("#messages1").hide();
								 }
								 $("#showyct").show();
							 }
							 else{
								 $("#showyct").hide();
								 $("#messages1").hide();
							 }

							  //如果预发送条数为0，则也不允许发送，因为有可能此号码未绑定通道。
							 if(eval(yct) == 0)
							 {
								$("#btsend").attr("disabled","disabled");
							 }
						},
						 close:function(){
							$("#subSend").attr("disabled","");
							$("#qingkong").attr("disabled","");
							$(".ui-dialog-titlebar-close").show();
							$("#SendReq").val("0");
							$("#btsend").attr("disabled","");
							$("#showyct").show();
						}
					});
					if(result!=="-1") {
				if(result=="timerSuccess") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_98"));
					getCt();
				}else if(result=="timerFail") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_99"));
				}else if(result=="uploadFileFail") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_100"));
				}else if(result=="createSuccess") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_101"));
					getCt();
				}else if(result=="timeError"){
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_102"));
					getCt();
				}else if(result == "000") {
					//alert("创建短信任务及发送到网关成功！");
					getCt();
					<%session.removeAttribute("mcs_batchResult");%>
					$("#message").dialog({width:300,height:180});
					$("#message").dialog("open");
				}else if(result.indexOf("empex") == 0) {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_103")+result.substr(5));
				}else if(result=="spuserfee:-2") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_104"));
				} else if(result=="spuserfee:-1"||result=="spuserfee:-3") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_105"));
				} else if(result=="saveSuccess") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_106"));
				}else if("nogwfee"==result || "feefail"==result){
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_107"));
				}else if(result.indexOf("lessgwfee")==0){
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_108"));
				}else if(result=="error") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_109"));
				}else if(result=="nospnumber") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_110")+smaacount+getJsLocaleMessage("ydwx","ydwx_jtwxfs_111"));
				}else if(result=="nomoney") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_112"));
				}else if(result=="false") {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_113"));
				}else if(result == "subnoFailed"){
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_114"));
				}else {
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_115")+result);
				}
				<%session.removeAttribute("mcs_batchResult");%>
			}
				});
			//初始化短信内容
			function initContents(){
				var manualWriter = <%=btnMap.get(menuCode+"-5") == null%>;
				var msgob = $("form[name='form2']").find("textarea[name='msg']");
				if(manualWriter){
					msgob.attr("readonly","");
					msgob.css("background-color","#E8E8E8");
					msgob.val("");
				}else{
					msgob.css("background-color","");
					msgob.val("");
					//msgob.attr("readonly","");
				}
			}
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>
