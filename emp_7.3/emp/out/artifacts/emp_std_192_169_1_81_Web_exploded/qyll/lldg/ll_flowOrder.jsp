<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Random"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfPrivilege"%>
<%@ page import="com.montnets.emp.entity.sms.LfMttask"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="com.montnets.emp.entity.pasgrpbind.LfAccountBind"%>
<%@ page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ page import="com.montnets.emp.qyll.entity.LlProduct"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String langName = (String)session.getAttribute("emp_lang");
	String lguserid = request.getParameter("lguserid");
	
	if(lguserid == null || lguserid.trim().length()==0){
		EmpExecutionContext.error("相同内容页面lguserid为null");
	}
	
	Random rand = new Random();
	Integer flag = new Integer(rand.nextInt());
	String str_flag = flag.toString();
	session.setAttribute("flag", str_flag);

	String context = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	
	@SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>)request.getAttribute("sendUserList");
	@SuppressWarnings("unchecked")
	List<LlProduct> productList = (List<LlProduct>)request.getAttribute("productList");
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>) request
			.getAttribute("busList");

	//上传返回总数
	Long AllNum = (Long) request.getAttribute("AllNum");
	//上传返回信息
	String message = (String) request.getAttribute("message");
	//实体
	String reinfo = (String) request.getAttribute("reinfo");
	String info = (String) request.getAttribute("info");
	String re = (String) request.getAttribute("re");

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("flowOrder");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String templateMenuCode = titleMap.get("smsTemplate");
	String orderResult = "-1";
	if(session.getAttribute("result") != null)	{
		orderResult =	(String) session.getAttribute("result");
		session.setAttribute("result", null);
	}	
		
	String result = session.getAttribute("mcs_batchResult")==null?"-1":
		(String)session.getAttribute("mcs_batchResult");
	boolean isLoadTree = false;
    String findResult= (String)request.getAttribute("findresult");
    LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
    String userName = ((LfSysuser)session.getAttribute("loginSysuser")).getName() ;
    String depSign = (String)request.getAttribute("depSign");
    String taskId = (String) request.getAttribute("taskId");
	String lgcorpcode = request.getParameter("lgcorpcode");
    String oldTaskId = request.getParameter("oldTaskId");
    //高级默认设置
    
    //是否集成了短信模板模块
    boolean isHaveTemp=false;
    if(StaticValue.getInniMenuMap().containsKey(ViewParams.TEMP_MENU_HTM))
    {
    	isHaveTemp=true;
    }
    
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
        
	String specialChar = StaticValue.getSmscontentSpecialcharStr();
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>企业流量订购</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<style>
			#dds {
				width: 328px;
				height: 120px !important;
				height: 122px;
				float: left;
				overflow-y: auto;
				overflow-x: hidden;
				border-right: 0px;
			}
            #tail-area{
                text-indent: 1em;
                display: none;
            }
            #tail-text{
                padding: 4px;
                line-height: 16px;
                background-color: #f8f8f8;
                word-break:break-all;
                color: #8a8a8a
            }
		#toDraft{
                text-align: center;
                letter-spacing: 2px;
                text-indent: 0;
		}
		</style>
	<style type="text/css">
			<%if(StaticValue.ZH_HK.equals(langName)){%>
				.btnClass5,.btnClass6{
				    letter-spacing: 0px;
				}
			<%}%>
		</style>
	<script>
		var base_manualWriter = '<%=btnMap.get(menuCode+"-5") == null%>';
		var base_menu = '<%=btnMap.get(menuCode+"-8")%>';
		var base_depSign = "<%=depSign%>";
		var base_nameSign = "<%=userName%>";
		var base_commonPath = "<%=commonPath%>";
		var base_result = "<%=result%>";
		var orderResult = "<%=orderResult%>";
		var base_menuCode = "<%=menuCode%>";
		var base_path = "<%=path%>";
		var base_findresult = "<%=findResult%>";
		var specialChar = "<%=specialChar%>";
		var special = specialChar.split(",");
		//模板可编辑标识
		var tmpEditorFlag = "<%=StaticValue.getTMPEDITORFLAG()%>";
		var ZIP_SIZE = "<%=StaticValue.ZIP_SIZE/1024/1024%>";
   		var MAX_SIZE = "<%=StaticValue.MAX_SIZE/1024/1024%>";
    	var MAX_PHONE_NUM = "<%=StaticValue.MAX_PHONE_NUM/10000%>";
    	var isSend = 0;
	</script>
	
	</head>
	<body>
		<div id="container">
			<%=ViewParams.getPosition(langName,menuCode) %>
			<div id="rContent" class="rContent"  style="position：relative">
				<input type="hidden" id="fuzhi" value=""/>
				<input type="hidden" id="formName" value="form1"/>
				<input type="hidden" id="cpath" value="<%=path%>"/>
				<input type="hidden" id="gt1" name="gt1" value=""/>
				<input type="hidden" id="gt2" name="gt2" value=""/>
				<input type="hidden" id="gt3" name="gt3" value=""/>
				<input type="hidden" id="gt4" name="gt4" value=""/>
				<%--标示出现异常--%>
				<input type="hidden" id="error" name="error" value="" />
				<input id="path" value="<%=inheritPath%>" type="hidden" />
				<div id="batchFileSend" class="block" style="width:685px;">
					<form id="form2" name="form2" action="ll_flowOrder.htm?method=preview" method="post" enctype="multipart/form-data" target="hidden_iframe">
						<input name="hidemsg" type="hidden" value="" id="hidemsg">
						<input type="hidden" name="phoneStr" id="phoneStr" value=","/>
						<input type=hidden name="flag" value="<%=str_flag%>" />
						<input type="hidden" id="tss" name="tss" value="e" />
						<input type="hidden" value="1" id="bmtType" name="bmtType" />
						<input type="hidden" name="groupStr" id="groupStr" value=","/><%--员工群组信息 --%>
						<input type="hidden" name="groupStrTemp" id="groupStrTemp" value=","/><%--临时保存员工群组信息 --%>
						<input type="hidden" name="depIdStr" id="depIdStr" value=","/>
						<input type="hidden" name="depIdStrTemp" id="depIdStrTemp" value=","/><%-- 临时保存depid字符串，在选择员工取消时用到 --%>
						<input type="hidden" value="0" id="isOk" name="isOk" />
						<input type="hidden" value="1" id="sendType" name="sendType" />
						<input type="hidden" value ="" id="circleSubNo" name="circleSubNo"/> <%-- 尾号 --%>
						<input type="hidden" value="" id="preStr" name="preStr" />
						
						<input type="hidden" value="" id="hidSubCount" name="hidSubCount" />
						<input type="hidden" value="" id="hidEffCount" name="hidEffCount" />
						<input type="hidden" value="" id="hidMobileUrl" name="hidMobileUrl" />
						<input type="hidden" value="" id="hidPreSendCount" name="hidPreSendCount" />
						<input type="hidden" value="" id="hideSumPrice" name="hideSumPrice" />
						<input type="hidden" value="" id="hidTimerTime" name="hidTimerTime" />
						<input type="hidden" value="" id="hideProIds" name="hideProIds" />
						
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
						<input type="hidden" value="" id="phoneStr1" name="phoneStr1"/><%-- 保存由通讯录选择的员工电话号码 --%>
						<input type="hidden" value="" id="phoneStr2" name="phoneStr2"/><%-- 保存由手动输入的员工电话号码 --%>
						<input type="hidden" value="" id="inputphone" name="inputphone"/><%-- 批量导入的电话号码 --%>
						<input type="hidden" value="" id="isCharg" name="isCharg"/><%-- 机构扣费标识 --%>
						<input type="hidden" value="" id="feeFlag" name="feeFlag"/><%-- SP账号计费类型 --%>
						<input type="hidden" value="" id="gwFee" name="gwFee"/><%-- 运营商扣费状态 --%>
						<input type="hidden" value="" id="tailcontents" name="tailcontents"/><%-- 短信贴尾内容 --%>
						
						<input type="hidden" value="0" id="hidIsDoOk" name="hidIsDoOk"/><%-- 控制选择人员弹出框关闭时清空。0为关闭操作；1为确定操作 --%>
						
						<%--
						
						 <input type="hidden" value="0" id ="manCountTemp" name ="manCountTemp"/>
						 --%>
						<div style="display: none;" id="allfilename" ></div>
						<div style="display:none" id="hiddenValueDiv"></div>
						
						<%--草稿箱--%>
                    	<input type='hidden' id="draftFile" name='draftFile' value=''/>
                    	<input type='hidden' id="draftFileTemp" name='draftFileTemp' value=''/>
                    	<input type='hidden' id="draftId" name='draftId' value=''/>
						
						<div id="eq_sendDiv">
						<span class="righttitle" ><emp:message key="qyll_common_6" defVal="订购主题" fileName="qyll"/>：</span>
							<input id="taskname" name="taskname" autocomplete="off" type="text" class="graytext input_bd div_bd" style="padding-left:5px;width:577px;float:left;" maxlength="20" value="<emp:message key="qyll_common_190" defVal="不作为短信内容发送" fileName="qyll"/>"/>
						</div>
						<div class="clear2"></div>
						
						<div id="eq_sendDiv">
							<span class="righttitle"><emp:message key="qyll_common_7" defVal="订购号码" fileName="qyll"/>：</span>
							<div id="dds" class="div_bd">
								<table id="vss">
									<tr id="first" class="title_bd">
										<td width="100" class="div_bd title_bg" style="border-left:0px"><emp:message key="qyll_common_8" defVal="类型" fileName="qyll"/></td>
										<td class="div_bd title_bg"><emp:message key="qyll_common_182" defVal="号码" fileName="qyll"/></td>
										<td width="82" class="div_bd title_bg" style="border-right: 0px"><emp:message key="qyll_common_9" defVal="操作" fileName="qyll"/></td>
									</tr>
								</table>
							</div>
							<div class="selectFileTable" style="width:255px;">
							<table width="100%" style="height: 122px ! important;height: 124px;">
									<tr style="height:72px" id="picTab">
									<td align="center" style="cursor:pointer;" onclick="javascript:showInfo();"  
									 class="div_bd div_bg">
										<div style="width: 81px;" >
										<a class="selectEmp"></a><div class="mt10"><emp:message key="qyll_common_10" defVal="选择人员" fileName="qyll"/></div>
										</div>
										</td>
									<td  class="div_bd div_bg" align="center" style='cursor:pointer;' 
									>
                                         <div style="position:relative;width:64px;">
										 <a class="importFile"/></a><div class="mt10"><emp:message key="qyll_common_11" defVal="导入文件" fileName="qyll"/></div>
										 							
                                         <div id="filesdiv" style='margin-bottom: 0px;position: absolute;left:0;top:-7px;'>
										 <input id='numFile1' name='numFile1'  type=file onchange="addFiles()" class="numfileclass" style='display:block;height: 64px;width:64px;'/>										
										 </div></div>
										</td>
									<%--新增批量导入start--%>
									
									<td  class="div_bd div_bg" align="center" style='cursor:pointer;width:85px;' onclick="bulkImport();"> 
									
                                         
										 <a class="bulkImport"/></a><div class="mt10"><emp:message key="qyll_common_12" defVal="批量输入" fileName="qyll"/></div>
										 							
                                         
										</td>
									
									<%--新增批量导入end--%>
									</tr>
									<tr>
										<td colspan="3" align="center" class="div_bd div_bg">
										
											<table width="235px" class="div_bd">
											<tr>
											<td width="190px">
											<input autocomplete="off" value="<emp:message key="qyll_common_184" defVal="请输入手机号"  fileName="qyll"/>" class="graytext" type="text" style="line-height:23px;width: 190px; height: 26px;border:0"
											onkeyup="phoneInputCtrl($(this))" onpaste="phoneInputCtrl($(this))" maxlength="21" name="tph" id="tph" onpropertychange="addph()"/>
											</td>
											<td align="center" onclick="addph2()" style="cursor:pointer"  class="div_bd"><emp:message key="qyll_common_183" defVal="添加" fileName="qyll"/></td>
											</tr>
											</table>
										</td>
										
									</tr>
								</table>
							</div> 
						</div>
						<div style="clear:both;"></div>
						<div style="padding:5px 15px 15px 0;" align="right">
							<a id="downlinks" style="padding-bottom:10px;position: relative;"><emp:message key="qyll_common_13" defVal="格式提示" fileName="qyll"/></a>
						    </div>
						    
						<div id="eq_sendDiv" class="eq_sendDiv_c">
							<span class = "righttitle"><emp:message key="qyll_common_14" defVal="套餐选择" fileName="qyll"/>：</span>
							<div id="tcxz">
								<table id="tttcxz">
									<tr id="first" class="title_bd">
										<td width="200" height="30" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_15" defVal="移动套餐" fileName="qyll"/></td>
										<td width="200" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_16" defVal="联通套餐" fileName="qyll"/></td>
										<td width="200" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_17" defVal="电信套餐" fileName="qyll"/></td>
									</tr>
									<tr>
										<td width="200" height="30"  style="text-align:center;">
										  <select style="width: 180px" id="yd_select" name="yd_select" class="input_bd div_bd">
								          	<option selected value="-1:-1"><emp:message key="qyll_common_18" defVal="请选择套餐" fileName="qyll"/></option>
								       		<%
												if (productList != null && productList.size() > 0) 
												{
													for (LlProduct product : productList) {
														if(!"1".equals(product.getIsp())  ){
															continue;
														}
												%>
												<option value="<%=product.getProductid()+":"+product.getId()%>">
													<%="("+product.getProductid()+")"+product.getProductname()%>
												</option>
												<%}}%>
								          </select>
										</td>
										<td width="200"  style="text-align:center;">
										<select style="width: 180px" id="lt_select" name="lt_select" class="input_bd div_bd">
								          	<option selected value="-1:-1"><emp:message key="qyll_common_18" defVal="请选择套餐" fileName="qyll"/></option>
								          		<%
												if (productList != null && productList.size() > 0) 
												{
													for (LlProduct product : productList) {
														if(!"3".equals(product.getIsp())  ){
															continue;
														}
												%>
												<option value="<%=product.getProductid()+":"+product.getId()%>">
													<%="("+product.getProductid()+")"+product.getProductname()%>
												</option>
												<%}}%>
								          </select>
										</td>
										<td width="200"  style="text-align:center;">
										<select style="width: 180px" id="dx_select" name="dx_select" class="input_bd div_bd">
								          	<option selected value="-1:-1"><emp:message key="qyll_common_18" defVal="请选择套餐" fileName="qyll"/></option>
								         	<%
												if (productList != null && productList.size() > 0) 
												{
													for (LlProduct product : productList) {
														if(!"2".equals(product.getIsp())  ){
															continue;
														}
												%>
												<option value="<%=product.getProductid()+":"+product.getId()%>">
													<%="("+product.getProductid()+")"+product.getProductname()%>
												</option>
												<%}}%>
								          </select>
										</td>
									</tr>
								</table>
							</div>
							
						</div>    
						    
						<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="qyll_common_19" defVal="订购时间" fileName="qyll"/>：</span>
						  <div style="padding:10px 0 0; width: 498px;height:13px;">
						    <input type="radio" name="timerStatus" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="qyll_common_20" defVal="立即发送" fileName="qyll"/>
							<input type="radio" name="timerStatus" value="1" style="margin-left: 10px;"
								onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="qyll_common_21" defVal="定时发送" fileName="qyll"/>
							<label id="time2" style="display: none;">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
									id="timerTime" name="timerTime" value="">
							</label>
						  </div>
						</div>    
						    
						<div id="eq_sendDiv" style="margin-top:-5px">
						<span class="righttitle"><emp:message key="qyll_common_22" defVal="短信提醒" fileName="qyll"/>：</span>
						<textarea id="smsContent" name="smsContent" cols="50" rows="10" class="graytext input_bd div_bd" onblur="eblur($(this))" maxlength="990" style="padding-left:5px;height:120px;width:577px;"><emp:message key="qyll_common_191" defVal="请输入流量赠送短信提醒内容，系统将与流量套餐订购同步发送给手机用户，为空则不发送短信提醒。" fileName="qyll"/>
						</textarea>
								<font style="line-height:12px;float:left;padding-top:5px;margin-left:75px;color: #656565;"><b id="strlen"> 0 </b><b id="maxLen">/990</b></font> 
								<font style="line-height:12px;float:right;padding-top:5px;margin-right:5px;color: #656565;">
								<emp:message key="qyll_common_185" defVal="移动" fileName="qyll"/>(<b id="ft1">0</b>)&nbsp;&nbsp;
								<emp:message key="qyll_common_186" defVal="联通" fileName="qyll"/>(<b id="ft2">0</b>)&nbsp;&nbsp;
								<emp:message key="qyll_common_187" defVal="电信" fileName="qyll"/>(<b id="ft3">0</b>)&nbsp;&nbsp;
								<emp:message key="qyll_common_23" defVal="国外" fileName="qyll"/>(<b id="ft4">0</b>)
								</font>
						</div>
						<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="qyll_common_24" defVal="发送账号" fileName="qyll"/>：</span>
						   <select style="width: 180px" id="sp_User" name="sp_User"  class="input_bd div_bd">
								  <%
									if (spUserList != null && spUserList.size() > 0) 
									{
										for (Userdata spUser : spUserList) {
									%>
									<option value="<%=spUser.getUserId()%>">
										<%=spUser.getUserId()+"("+spUser.getStaffName()+")"%>
									</option>
									<%}}%>
							</select>
						</div>
						

						<div class="clear2"></div>
						
						<div class="b_F_btn" style="float:left;width:680px;">
							<input type="hidden" value="1" id="subState" name="subState" />
							<input id="subSend" class="btnClass5 mr23"  type="button" value="<emp:message key="qyll_common_25" defVal="提交" fileName="qyll"/>"/>
							<input id="qingkong" onclick="javascript:reloadPage();" class="btnClass6"  type="button" value="<emp:message key="qyll_common_26" defVal="重置" fileName="qyll"/>" />
						</div>
						</form>
						
						<div class="clear2"></div>
						<div id="infoDiv" title="<emp:message key="qyll_common_27" defVal="选择发送对象" fileName="qyll"/>">
							<input id="flowNames" type="hidden" name="flowNames" value="" />
							<iframe id="flowFrame" name="flowFrame" src="<%=context %>/ll_chooseSendInfo.jsp?lguserid=<%=lguserid %>" style="width:525px;height:460px;border: 0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>
							<table style="border:0">
							<tr><td style="width:530px;border:0px" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button"  value="<emp:message key="qyll_common_28" defVal="确定" fileName="qyll"/>" class="btnClass5 mr23"  onclick="javascript:doOk()" />
							<input type="button"  value="<emp:message key="qyll_common_29" defVal="取消" fileName="qyll"/>" class="btnClass6"  onclick="javascript:doSelectEClose()" />
							</td>
							</tr>
							</table> 
						</div>
						
						<div id="detail_Info" style="width: 100%; padding: 5px; display: none;">							  
							<table id="infos" width="98%" style="border:0">
							   <tr>
								  <td colspan="2">
								    <label id="messages2"><font style="color: red;"></font></label>
								  </td>
							  </tr>
							  <tr>
								  <td colspan="2">
								    <label id="messages1"><font style="color: red;"><emp:message key="qyll_common_30" defVal="机构余额不足，不允许进行发送!" fileName="qyll"/></font></label>
								  </td>
							  </tr>
							  <tr>
								  <td><emp:message key="qyll_common_31" defVal="有效号码数" fileName="qyll"/>：<span id="effs"></span></td>
								  <td><emp:message key="qyll_common_32" defVal="提交号码数" fileName="qyll"/>：<span id="counts"></span></td>								  
								  <td align="center">
								   <div id="showeffinfo" class="div_bg">
								       <p style="font-size: 12px;"><emp:message key="qyll_common_188" defVal="无效号码" fileName="qyll"/>(<span id="alleff" style="color: red;"></span>)&nbsp;&nbsp;
								   <a id="arrowhead" style="text-decoration:none"  class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></p> 
								   </div>
								  </td>
							  </tr>
							</table>
							<div id="effinfo" class="div_bg">
							   <center>
							    <table id="effinfotable">
                                     <tr height="10px;">
                                       <td colspan="2"></td>
                                     </tr>
								      <tr>
								      <td align="left"><emp:message key="qyll_common_33" defVal="黑名单号码" fileName="qyll"/>：<span id="blacks"></span></td>
								      <td id="preinfonum">
								      <%
										if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) 
										{
									  %>
								      	<a href="javascript:uploadbadFiles()"><emp:message key="qyll_common_34" defVal="详情下载" fileName="qyll"/></a>
								      <%
								      	}
								      %>
								      <input type="hidden" id="badurl" name="badurl"  value=""></input>
								      </td>
							      </tr>
							      <tr>
								      <td align="left"><emp:message key="qyll_common_35" defVal="重复号码" fileName="qyll"/>：<span id="sames"></span></td>
								      <td></td>
							      </tr>
							      <tr>
							          <td align="left"><emp:message key="qyll_common_36" defVal="格式非法" fileName="qyll"/>：<span id="legers"></span></td>
							          <td></td>
							      </tr>
							      <tr>
							          <td align="left"><emp:message key="qyll_common_37" defVal="无套餐号码" fileName="qyll"/>：<span id="noFlowPhone"></span></td>
							          <td></td>
							      </tr>
							    </table>
							    </center>
						    </div>
						    <div style="height: 15px;"></div>	
							<p style="font-size: 12px;font-weight: bold;margin-left: 1%">
								<emp:message key="qyll_common_38" defVal="流量订购预览" fileName="qyll"/>：
							</p><br>
							<div id="dgyl" style="overflow: auto;height: 30px;">
							<center>
							<table id="ylxx" style="width: 472px;">
								<tr>
									<td align="left"><emp:message key="qyll_common_39" defVal="流量消费金额" fileName="qyll"/>：<span id="allprice"></span> </td>
									<td align="left"><emp:message key="qyll_common_40" defVal="流量运营商余额" fileName="qyll"/>：<span id="leftprice"></span> </td>
								</tr>
							</table>
							</center>
							</div>
							<div id="maindiv" style="overflow: auto;">
							<center>
							<table id="content" style="width: 472px;">
								<tr id="first" class="title_bd">
										<td width="35" height="30" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_41" defVal="编号" fileName="qyll"/></td>
										<td width="50" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_76" defVal="套餐编号" fileName="qyll"/></td>
										<td width="135" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_77" defVal="套餐名称" fileName="qyll"/></td>
										<td width="80" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_43" defVal="运营商" fileName="qyll"/></td>
										<td width="70" class="div_bd title_bg" style="border-left:0px;text-align:center;"><emp:message key="qyll_common_44" defVal="有效号码数" fileName="qyll"/></td>
								</tr>
							</table>
							</center>
							</div>
							<div style="height: 15px;"></div>	
							<p style="font-size: 12px;font-weight: bold;margin-left: 1%">
								<emp:message key="qyll_common_45" defVal="短信提醒预览" fileName="qyll"/>：
							</p><br>
							<div id="dgtxyl" style="overflow: auto;">
							<center>
							<table id="dgxx" style="width: 472px;">
								<tr>
									<td align="left"><emp:message key="qyll_common_46" defVal="短信发送条数" fileName="qyll"/>：<span id="smsCount"></span> </td>
									<td align="left"><emp:message key="qyll_common_189" defVal="短信机构余额" fileName="qyll"/>：<span id="depFeeCount"></span> </td>
									<td align="left"><emp:message key="qyll_common_47" defVal="短信sp账号余额" fileName="qyll"/>：<span id="spUserFeeCount"></span> </td>
								</tr>
							</table>
							<div style="height: 15px;"></div>
							<textarea id="smsContentyl"  name="smsContentyl" cols="65" rows="5" class="div_bd " style="background-color: white;width: 471px" disabled="disabled" >
							</textarea>
							</center>
							</div>
					    	<div id="footer" style="margin-top: 20px;">
					    	  <center>
					    		<input id="btsend"  class="btnClass5 mr23" type="button" value="<emp:message key="qyll_common_48" defVal="订购" fileName="qyll"/>" onclick="javascript:btsend()"/>
							    <input id="btcancel"  onclick="javascript:previewCancel();" class="btnClass6" type="button" value="<emp:message key="qyll_common_29" defVal="取消" fileName="qyll"/>" />
								<br/>
							  </center> 
					    	</div>	
						</div>
						
						<div class="clear2"></div>
					
						<div id="infomodel" style="display: none;position: absolute;z-index: 5000;border: 1px solid #dae1cf;background-color: #f7fff0;width: 300px;height: 400px;padding-left: 20px;">
	                       <table style="line-height: 30px;">
	                         <tr>
                            <td colspan="2" style="font-weight: bold;font-size: 14px;" align="left"><emp:message key="qyll_common_49" defVal="文件格式如图" fileName="qyll"/></td>
                          	</tr>
	                         <tr>
	                          <td style="text-align:left;vertical-align: top">
	                        	<emp:message key="qyll_common_50" defVal="txt格式" fileName="qyll"/>
	                          </td>
	                          <td rowspan="2"><img id="foldIcon" src="<%=commonPath %>/common/img/mmsphone.png"/></td>
	                         </tr>
	                         <tr>
	                         <td style="text-align:left;vertical-align: top"><emp:message key="qyll_common_51" defVal="excel格式" fileName="qyll"/></td>
	                         </tr>
	                         <tr>
	                         <td align="left" colspan="2">
	                         <span style="font-weight: bold;font-size: 14px;"><emp:message key="qyll_common_52" defVal="注意" fileName="qyll"/>：</span>
	                         </td>
	                         </tr>
	                         <tr>
	                         <td align="left" colspan="2" style="line-height:20px">
	                         <emp:message key="qyll_common_53" defVal="1.手机号码格式不正确，在上传时将由EMP平台过滤；" fileName="qyll"/><br />
	                         <emp:message key="qyll_common_54" defVal="2.号码包含于黑名单内，在上传时将由EMP平台过滤；" fileName="qyll"/><br />
	                         <emp:message key="qyll_common_55" defVal="3.文件需小于" fileName="qyll"/><%=StaticValue.MAX_SIZE/1024/1024%><emp:message key="qyll_common_56" defVal="M，有效号码需少于" fileName="qyll"/><%=StaticValue.MAX_PHONE_NUM/10000%><emp:message key="qyll_common_57" defVal="万；" fileName="qyll"/><br />
	                         <emp:message key="qyll_common_58" defVal="4.文件仅支持txt、zip、rar、xls、xlsx、csv、et格式，请优先使用zip文件格式。" fileName="qyll"/>
	                         </td>
	                         </tr>
	                       </table>
	                   </div>
	                   
						<div id="probar" style="display: none">
							<p style="padding-top: 8px;">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="qyll_common_59" defVal="处理中," fileName="qyll"/>
								<emp:message key="qyll_common_60" defVal="请稍等....." fileName="qyll"/>
							</p>
							<div id="shows">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<img style="padding-left: 25px;"
									src="<%=commonPath%>/common/img/loader.gif" />
							</div>
						</div>
						
					</div>
				</div>
			
				<iframe name="hidden_iframe" id="hidden_iframe" style="display: none"></iframe>
			
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<%-- foot结束 --%>
		</div>
		<div class="clear2"></div>
		<%--批量导入--%>
		<style>
			.imporInner{width: 485px;height: 240px;margin:20px auto 10px;}
			.bultMark{width: 485px;margin:0 auto 20px;position: relative;overflow: hidden;}
			.imporInner textarea{border: 0;width:477px;height:220px;}
			textarea:focus{outline: none;resize:none;}
			.bultBtn{text-align: center;}
			.numSplit{text-align: left;}
			#bultNum{position: absolute;left: 0;top: 0;}
		</style>
		<div id="bulkImport_box" title="<emp:message key="qyll_common_64" defVal="批量号码输入" fileName="qyll"/>" style="display:none;">
			   <div class="imporInner div_bd">
			   		<textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onblur="formatTelNum('#bNum')"></textarea>
			   		<textarea name="importAreaTemp" id="importAreaTemp" style="display:none"></textarea>
			   </div>
			   <div class="bultMark">
			   		<span id="bultNum"><emp:message key="qyll_common_65" defVal="当前共" fileName="qyll"/><font color='blue'><b id="bNum"></b></font><emp:message key="qyll_common_66" defVal="/20000个号码" fileName="qyll"/></span>
			   		<div style="height:25px"></div>
			   		<div class="numSplit">
			   			<emp:message key="qyll_common_67" defVal="多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="qyll"/>
			   		</div>
			   </div>
			   <div class="bultBtn">
			   	<input onclick="previewTel()" class="btnClass5 mr23"  type="button" value="<emp:message key="qyll_common_68" defVal="确认" fileName="qyll"/>">
			   	<input onclick="bultCancel()"  class="btnClass6"  type="button" value="<emp:message key="qyll_common_69" defVal="返回" fileName="qyll"/>">
			   	<br/>
			   </div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/qyll_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/ll_batchFileSend.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=iPath%>/js/ll_flowOrder.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script>
		var userAgent = navigator.userAgent;
		var isChrome = 0;
		if (userAgent.indexOf("Chrome") > -1)
		{
   			isChrome = 1;
  		}
		//谷歌浏览器 
		if(isChrome == 1)
		{
			function onbeforeCloseIt()
		    {
				 if(isSend == "0")
				 {
					 	 var logInfo = '相同内容群发，界面刷新或关闭浏览器。userId:'+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
						+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
						+'，spUser:'+$("#spUser").val();
						$.post('common.htm?method=frontLog',{
						info:logInfo
						});
				 }
		    }
			window.onbeforeunload = onbeforeCloseIt;
		}
	    else
    	{
   		    function onunloadCloseIt()
		    {
				 if(isSend == "0")
				 {
					 	 var logInfo = '相同内容群发，界面刷新或关闭浏览器。userId:'+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
						+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
						+'，spUser:'+$("#spUser").val();
						$.post('common.htm?method=frontLog',{
						info:logInfo
						});
				 }
		    }
		    window.onunload = onunloadCloseIt;
    	}
    	
    	$(document).ready(function(){
    		getGateNumber();
			$("#sp_User").bind("change", function(){getGateNumber();});
    	
    	});

    	$(function(){
    		if(orderResult != '-1'){
				alert(orderResult);    		
    		}
    	});
		</script>
	</body>
</html>
