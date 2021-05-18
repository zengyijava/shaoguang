<%@page import="com.montnets.emp.shorturl.surlmanage.entity.LfDomain"%>
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
<%@page import="com.montnets.emp.shorturl.comm.constant.DxzsStaticValue"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.entity.LfDfadvanced"%>

<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.indexOf("/",1));

	
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
	Map<String, String> titleMap = (Map<String, String>) session
			.getAttribute("titleMap");
	String menuCode = titleMap.get(request.getAttribute("reTitle"));
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String templateMenuCode = titleMap.get("smsTemplate");
	//String isFlow = (String) request.getAttribute("isFlow");
	String result = session.getAttribute("surl_batchResult")==null?"-1":
		(String)session.getAttribute("surl_batchResult");
	boolean isLoadTree = false;
    String findResult= (String)request.getAttribute("findresult");
    LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
    String userName = ((LfSysuser)session.getAttribute("loginSysuser")).getName() ;
    String depSign = (String)request.getAttribute("depSign");
    String taskId = (String) request.getAttribute("taskId");
	String lgcorpcode = request.getParameter("lgcorpcode");
    String oldTaskId = request.getParameter("oldTaskId");
    //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
    List<LfDomain> domainList = (List<LfDomain>)request.getAttribute("domainlist");
    //是否集成了短信模板模块
    boolean isHaveTemp=false;
    if(StaticValue.getInniMenuMap().containsKey(ViewParams.TEMP_MENU_HTM))
    {
    	isHaveTemp=true;
    }
    
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
        
//    String depSignLeft = DxzsStaticValue.SAMESMS_DEP_SIGN_LEFT;
//	String depSignRight = DxzsStaticValue.SAMESMS_DEP_SIGN_RIGHT;
// 	String nameSignLeft = DxzsStaticValue.SAMESMS_NAME_SIGN_LEFT;
//	String nameSignRight = DxzsStaticValue.SAMESMS_NAME_SIGN_RIGHT;
	String depSignLeft = DxzsStaticValue.getSamesmsDepSignLeft();
	String depSignRight = DxzsStaticValue.getSamesmsDepSignRight();
 	String nameSignLeft = DxzsStaticValue.getSamesmsNameSignLeft();
	String nameSignRight = DxzsStaticValue.getSamesmsNameSignRight();
	String specialChar = StaticValue.getSmscontentSpecialcharStr();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>相同内容群发</title>
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
	<script>
		var base_manualWriter = '<%=btnMap.get(menuCode+"-5") == null%>';
		var base_menu = '<%=btnMap.get(menuCode+"-8")%>';
		var base_depSign = "<%=depSign%>";
		var base_nameSign = "<%=userName%>";
		var base_commonPath = "<%=commonPath%>";
		var base_result = "<%=result%>";
		var base_reTitle = "<%=request.getAttribute("reTitle")%>";
		var base_menuCode = "<%=menuCode%>";
		var base_path = "<%=path%>";
		var base_findresult = "<%=findResult%>";
		var base_findresult = "<%=findResult%>";	
		var depSignLeft = "<%=depSignLeft%>";
		var depSignRight = "<%=depSignRight%>";
		var nameSignLeft = "<%=nameSignLeft%>";
		var nameSignRight = "<%=nameSignRight%>";
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
			<%=ViewParams.getPosition(menuCode) %>
			<div id="rContent" class="rContent"  style="position:relative">
                <input type="hidden" id="skin" value="<%=skin%>" />
				<input type="hidden" id="fuzhi" value=""/>
				<input type="hidden" id="formName" value="form1"/>
				<input type="hidden" id="cpath" value="<%=path%>"/>
				<input type="hidden" id="gt1" name="gt1" value=""/>
				<input type="hidden" id="gt2" name="gt2" value=""/>
				<input type="hidden" id="gt3" name="gt3" value=""/>
				<input type="hidden" id="gt4" name="gt4" value=""/>
				<input type="hidden" id="skin" value="<%=skin %>"/>
				<%--标示出现异常--%>
				<input type="hidden" id="error" name="error" value="" />
				<input id="path" value="<%=inheritPath%>" type="hidden" />
				
				<div id="batchFileSend" class="block" style="width:685px;">
					<form id="form2" name="form2" action="urls_previewSMS.htm?method=preview" method="post" enctype="multipart/form-data" target="hidden_iframe">
						<input name="hidemsg" type="hidden" value="" id="reinfo">
						<input type="hidden" name="phoneStr" id="phoneStr" value=","/>
						<input type=hidden name="flag" value="<%=str_flag%>" />
						<input type="hidden" id="tss" name="tss" value="e" />
						<input type="hidden" value="2" id="bmtType" name="bmtType" /><%-- 相同内容短链接发送本质是不同内容发送  caizg--%>
						<input type="hidden" name="groupStr" id="groupStr" value=","/><%--员工群组信息 --%>
						<input type="hidden" name="groupStrTemp" id="groupStrTemp" value=","/><%--临时保存员工群组信息 --%>
						<input type="hidden" name="depIdStr" id="depIdStr" value=","/>
						<input type="hidden" name="depIdStrTemp" id="depIdStrTemp" value=","/><%-- 临时保存depid字符串，在选择员工取消时用到 --%>
						<input type="hidden" value="0" id="isOk" name="isOk" />
						<input type="hidden" value="3" id="sendType" name="sendType" /><%-- 相同内容短链接发送本质是不同内容发送  caizg--%>
						<input type="hidden" value ="" id="circleSubNo" name="circleSubNo"/> <%-- 尾号 --%>
						<input type="hidden" value="" id="preStr" name="preStr" />
						<input type="hidden" value="0" id="SendReq" name="SendReq">
						<input type="hidden" value="" id="hidSubCount" name="hidSubCount" />
						<input type="hidden" value="" id="hidEffCount" name="hidEffCount" />
						<input type="hidden" value="" id="hidMobileUrl" name="hidMobileUrl" />
						<input type="hidden" value="" id="hidDzUrl" name="hidDzUrl" /><%-- 短链接地址 --%>
						<input type="hidden" value="" id="hidPreSendCount" name="hidPreSendCount" />
						
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
						<span class="righttitle" >发送主题：</span>
							<input id="taskname" name="taskname" type="text" class="graytext input_bd div_bd" style="padding-left:5px;width:577px;float:left;" maxlength="20" value="不作为短信内容发送"/>
						</div>
						<div class="clear2"></div>
						
						<div id="eq_sendDiv">
							<span class="righttitle">发送号码：</span>
							<div id="dds" class="div_bd">
								<table id="vss">
									<tr id="first" class="title_bd">
										<td width="100" class="div_bd title_bg" style="border-left:0px">类型</td>
										<td class="div_bd title_bg">号码</td>
										<td width="82" class="div_bd title_bg" style="border-right: 0px">操作</td>
									</tr>
								</table>
							</div>
							<div class="selectFileTable" style="width:255px;">
							<table width="100%" style="height: 122px ! important;height: 124px;">
							<%
							boolean isEmpty = false;
							if("5500-1000".equals(menuCode)){
								isEmpty = btnMap.get(menuCode+"-8")==null&&btnMap.get(menuCode+"-7")==null 
								&& btnMap.get(menuCode+"-6")==null;
							}else{
								isEmpty = btnMap.get(menuCode+"-7")==null 
								&& btnMap.get(menuCode+"-6")==null;
							}
							if(isEmpty){%>
									<tr style="height:122px" id="picTab"><td class="div_bd div_bg"></td></tr>
							<%}if(btnMap.get(menuCode+"-8")!=null||btnMap.get(menuCode+"-7")!=null){ %>
									<tr style="height:72px" id="picTab">
									<%if(btnMap.get(menuCode+"-8")!=null){%>
									<td align="center" style="cursor:pointer;" onclick="javascript:showInfo();"  
									<%if("5500-1000".equals(menuCode) && btnMap.get(menuCode+"-8")==null){%> colspan="2" <%} %> class="div_bd div_bg">
										<div style="width: 81px;" >
										<a class="selectEmp"></a><div class="mt10">选择人员</div>
										</div>
										</td>
									<%} %>	
									<%if(btnMap.get(menuCode+"-7")!=null){%>
									<td  class="div_bd div_bg" align="center" style='cursor:pointer;' 
									<%if(("5500-1000".equals(menuCode) && btnMap.get(menuCode+"-7")==null) ){%> colspan="2" <%} %>>
                                         <div style="position:relative;width:64px;">
										 <a class="importFile"/></a><div class="mt10">导入文件</div>
										 							
                                         <div id="filesdiv" style='margin-bottom: 0px;position: absolute;left:0;top:-7px;'>
										 <input id='numFile1' name='numFile1'  type=file onchange="addFiles()" class="numfileclass" style='display:block;height: 64px;width:64px;'/>										
										 </div></div>
										</td>
									<%} %>
									<%--新增批量导入start--%>
									
									<td  class="div_bd div_bg" align="center" style='cursor:pointer;width:85px;' onclick="bulkImport();"> 
									
                                         
										 <a class="bulkImport"/></a><div class="mt10">批量输入</div>
										 							
                                         
										</td>
									
									<%--新增批量导入end--%>
									</tr>
								<%} %>
								<%if(btnMap.get(menuCode+"-6")!=null){%>
									<tr>
										<td colspan="3" align="center" class="div_bd div_bg">
										
											<table width="235px" class="div_bd">
											<tr>
											<td width="190px">
											<input value=" 请输入手机号" class="graytext" type="text" style="line-height:23px;width: 190px; height: 26px;border:0"
											onkeyup="phoneInputCtrl($(this))" onpaste="phoneInputCtrl($(this))" maxlength="21" name="tph" id="tph" onpropertychange="addph()"/>
											</td>
											<td align="center" onclick="addph2()" style="cursor:pointer"  class="div_bd">添加</td>
											</tr>
											</table>
										</td>
										
									</tr>
									<%}else{%>
									<tr> <td colspan="2" align="center" class="div_bd div_bg"></td></tr>
									<%} %>
								</table>
							</div> 
						</div>
						<div style="clear:both;"></div>
						<div style="padding:5px 15px 15px 0;" align="right">
						
						
							<input id="netUrl" name="netUrl" type="hidden" value=""/>
							<input id="netUrlId" name="netUrlId" type="hidden" value=""/>
							<input id="vaildays" name="vaildays" type="hidden" value=""/>
							<input id="domainId" name="domainId" type="hidden" value=""/>
							<a id="downlinks" style="padding-bottom:10px;position: relative;">格式提示</a>
						    </div>
						    
						<div id="eq_sendDiv" style="margin-top:-5px">
						
						<span class="righttitle">发送内容：</span>
						<table style="width:584px;float:left;"  class="div_bd">
						<tr style="height:25px">
						<td style="height:25px;line-height: 25px;width:270px;" class="div_bd">
						<%if(btnMap.get(menuCode+"-4") != null){%>
						 <a  style="width:140px;border-top:0;border-left:0;float:left;text-align:center;border-bottom:0;height:25px;line-height: 25px;display: block"
							  id = "ctem" class="div_bg div_bd" href="javascript:chooseTemp()">点击选择模板</a>			
							<a  style="width:140px;border-top:0;border-left:0;float:left;text-align:center;border-bottom:0;height:25px;line-height: 25px;display: none"
							  id = "qtem" class="div_bg div_bd" href="javascript:tempNo()">点击取消模板</a>	
							<%} %>&nbsp;
							<%if(btnMap.get(templateMenuCode+"-1" )!= null && isHaveTemp){%> 			    
							<a href="javascript:showAddSmsTmp(0)" >[立即新建]</a>
							
							<%} %> 
							
							<a href="javascript:showDraft()" style="margin-left: 10px">草稿箱</a>
							<a href="javascript:insertUrl()" style="margin-left: 20px">插入短链接</a>
							<br/>
						</td>
						<td class="div_bd"  style="width:176px;padding-left:5px;">
						前缀签名：
						<input type="checkbox" value="" name="depSign" id="depSign"  onclick="setDepSign(this)" style="vertical-align: text-bottom;"/>&nbsp;机构&nbsp;&nbsp;
						<input type="checkbox" value="" name="nameSign" id="nameSign"  onclick="setNameSign(this)" style="vertical-align: text-bottom;"/>&nbsp;姓名
						</td>
						</tr>
						<tr style="height:124px">
						<td colspan="2">
							<textarea style="width:570px;height:124px;border:0px;overflow:auto;" class=".msg2" name="msg" rows="5" id="contents" onblur="eblur($(this))"></textarea>
						</td>
						</tr>
						<tr id="tail-area" class="div_bd">
                                <td id="tail-text" colspan="2">
                                   	 贴尾内容：<label id="showtailcontent"></label>
                                </td>
                         </tr>
						</table>
								<font style="line-height:12px;float:left;padding-top:5px;margin-left:75px;color: #656565;"><b id="strlen"> 0 </b><b id="maxLen">/990</b></font> 
								<font style="line-height:12px;float:right;padding-top:5px;margin-right:5px;color: #656565;">
								移动(<b id="ft1">0</b>)&nbsp;&nbsp;
								联通(<b id="ft2">0</b>)&nbsp;&nbsp;
								电信(<b id="ft3">0</b>)&nbsp;&nbsp;
								国外(<b id="ft4">0</b>)
								</font>
						</div>

						<div class="clear2"></div>
						
						<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span>发送时间：</span>
						  <div style="padding:10px 0 0; width: 498px;height:13px;">
						    <input type="radio" name="timerStatus" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;立即发送
							<input type="radio" name="timerStatus" value="1" style="margin-left: 10px;"
								onclick="javascript:$('#time2').show()"/>&nbsp;定时发送
							<label id="time2" style="display: none;">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false,enableInputMask:false})"
									id="timerTime" name="timerTime" value="">
							</label>
						  </div>
						</div>
						
						<div id="eq_sendDiv2" style="float:left;margin-top:0px">
						<span id="u_o_c_explain" class="div_bg"><b>高级设置</b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" style="text-decoration:none" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
						<div id="moreSelect" style="width:680px;"class="div_bg">
						<div style="width:630px" align="right">
							<a id="setdefualt" href="javascript:setDefault()"  class="alink" >选项存为默认</a>
						</div>
						<%-- 业务类型 --%>
						<table style="width:100%;height:100%;border:0px">
						<tr style="height:20%;"><td style="width:108px;"><font style="padding-left:35px">业务类型：</font></td>
						<td>
						<select id="busCode"  class="input_bd"  name="busCode" style="width:225px">
									<%
										if (busList != null && busList.size() > 0) {
											String busCode = lfDfadvanced != null?lfDfadvanced.getBuscode():"";
											for (LfBusManager busManager : busList) {
									%>
									<option value="<%=busManager.getBusCode()%>" 
									<%=busCode != null && !"".equals(busCode) && busCode.equals(busManager.getBusCode())?"selected":"" %>>
										<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") + "("+ busManager.getBusCode() +")"%>
									</option>
									<%
										}
										}
									%>
						</select>
						</td>
						</tr>
						<tr style="height:20%">
						<td><font style="padding-left:35px">发送级别：</font></td>
						<td>
						<select id="priority" class="input_bd" name="priority" style="width:225px">
							<option value="0">系统智能控制</option>
							<option value="1">1(优先级最高)</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9(优先级最低)</option>
							</select>
						</td>
						</tr>
						<tr  style="height:20%">
						<td><font style="padding-left:35px"><%=StaticValue.SMSACCOUNT%>：</font></td>
						<td>
						<select id="spUser"  class="input_bd" name="spUser" style="width:225px">
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
						<tr  style="height:20%">
						<td><font style="padding-left:35px">回复设置：</font></td>
						<td>
						<input type="radio"  name="isReply" value="0" onclick="getGateNumber()" 
								<%if(lfSysuser.getIsExistSubNo() != 1){ %> checked="checked" <%} %> style="margin-left: 4px;" />&nbsp;不用回复
								<input type="radio"  name="isReply" value="1" onclick="getGateNumber()" style="margin-left: 10px;" />&nbsp;本次任务
								<input type="radio"  name="isReply" value="2" onclick="getGateNumber()" 
								<%if(lfSysuser.getIsExistSubNo() != 1){ %> disabled="disabled" <%}else{ %>
								checked="checked"<%} %>
								style="margin-left: 10px;"/><label <%if(lfSysuser.getIsExistSubNo() != 1){ %> style="color:gray;" <%}else{%>
								style="color:#000000;"
								 <%}%>>&nbsp;我的尾号</label><label style="margin-left: 20px;color:#000000;display: none" id="curSubNo">当前尾号：</label>
								 <label id="subno" ></label>
								 <br/>
						</td>
						</tr>
						
						</table>
						</div>
						</div>
						<div class="b_F_btn" style="float:left;width:680px;">
							<input type="hidden" value="1" id="subState" name="subState" />
							<input id="toDraft" type="button" value="暂存草稿" class="btnClass5 mr23"/>
							<input id="subSend" class="btnClass5 mr23" type="button" value="提交"/>
							<input id="qingkong" onclick="javascript:reloadPage();" class="btnClass6" type="button" value="重置" />
						</div>
						</form>
						
						<div class="clear2"></div>
						<div id="infoDiv" title="选择发送对象">
							<input id="flowNames" type="hidden" name="flowNames" value="" />
							<iframe id="flowFrame" name="flowFrame" src="<%=context %>/ssm_chooseSendInfo.jsp?lguserid=<%=lguserid %>" style="width:525px;height:460px;border: 0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>
							<table style="border:0">
							<tr><td style="width:530px;border:0px" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button"  value="确定" class="btnClass5 mr23" onclick="javascript:doOk()" />
							<input type="button"  value="取消" class="btnClass6" onclick="javascript:doSelectEClose()" />
							</td>
							</tr>
							</table> 
						</div>
						
						<div id="detail_Info" style="width: 100%; padding: 5px; display: none;">							  
							<table id="infos" width="98%" style="border:0">
							  <tr >
							    <td class="infostd">发送条数：<span id="yct"></span></td>								    
							    <td class="infostd" id="showyct">机构余额：<span id="ct"></span></td>
							    <td class="infostd" id="showyspbalance">SP账号余额：<span id="spbalance"></span></td>							    
							  </tr>
							   <tr>
								  <td colspan="2">
								    <label id="messages2"><font style="color: red;"></font></label>
								  </td>
							  </tr>
							  <tr>
								  <td colspan="2">
								    <label id="messages1"><font style="color: red;">机构余额不足，不允许进行发送!</font></label>
								  </td>
							  </tr>
							  <tr>
								  <td>有效号码数：<span id="effs"></span></td>
								  <td>提交号码数：<span id="counts"></span></td>								  
								  <td align="center">
								   <div id="showeffinfo" class="div_bg">
								       <p style="font-size: 12px;">无效号码(<span id="alleff" style="color: red;"></span>)&nbsp;&nbsp;
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
								      <td align="left">黑名单号码：<span id="blacks"></span></td>
								      <td id="preinfonum">
								      <%
										if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) 
										{
									  %>
								      	<a href="javascript:uploadbadFiles()">详情下载</a>
								      <%
								      	}
								      %>
								      <input type="hidden" id="badurl" name="badurl"  value=""></input>
								      </td>
							      </tr>
							      <tr>
								      <td align="left">重复号码：<span id="sames"></span></td>
								      <td></td>
							      </tr>
							      <tr>
							          <td align="left">格式非法：<span id="legers"></span></td>
							          <td></td>
							      </tr>
							    </table>
							    </center>
						    </div>
						    <div style="height: 15px;"></div>	
							<p style="font-size: 12px;font-weight: bold;margin-left: 1%">
								部分预览：
							</p>
							<div id="maindiv" style="overflow: auto;height: 280px;">
							<center>
							<table id="content" style="width: 472px;"></table>
							</center>
							</div>
					    	<div id="footer" style="margin-top: 20px;">
					    	  <center>
					    		<input id="btsend" class="btnClass5 mr23" type="button" value="发送" onclick="javascript:btsend()"/>
							    <input id="btcancel" onclick="javascript:previewCancel();" class="btnClass6" type="button" value="取消" />
								<br/>
							  </center> 
					    	</div>	
						</div>
						
						<div class="clear2"></div>
					
						<div id="infomodel" style="display: none;position: absolute;z-index: 5000;border: 1px solid #dae1cf;background-color: #f7fff0;width: 300px;height: 350px;padding-left: 20px;">
	                       <table style="line-height: 30px;">
	                         <tr>
                            <td colspan="2" style="font-weight: bold;font-size: 14px;" align="left">文件格式如图:</td>
                          	</tr>
	                         <tr>
	                          <td style="text-align:left;vertical-align: top">
	                        txt格式
	                          </td>
	                          <td rowspan="2"><img id="foldIcon" src="<%=commonPath %>/common/img/mmsphone.png"/></td>
	                         </tr>
	                         <tr>
	                         <td style="text-align:left;vertical-align: top">excel格式</td>
	                         </tr>
	                         <tr>
	                         <td align="left" colspan="2">
	                         <span style="font-weight: bold;font-size: 14px;">注意：</span>
	                         </td>
	                         </tr>
	                         <tr>
	                         <td align="left" colspan="2" style="line-height:20px">
	                         1.手机号码格式不正确，在上传时将由EMP平台过滤；<br />
	                         2.号码包含于黑名单内，在上传时将由EMP平台过滤；<br />
	                         3.文件需小于<%=100%>M，有效号码需少于<%=100%>万；<br />
	                         4.文件仅支持txt、zip、rar、xls、xlsx、csv、et格式，请优先使用zip文件格式。
	                         </td>
	                         </tr>
	                       </table>
	                   </div>
	                   
						<div id="probar" style="display: none">
							<p style="padding-top: 8px;">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;处理中,
								请稍等.....
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
			<div id="tempDiv" title="短信模板选择" style="background-color:#FFFFFF;padding:5px;display:none">
			<iframe id="tempFrame" name="tempFrame" <%=skin.contains("frame4.0")?"style=\"width:100%;min-height:460px;border: 0;\"":"style=\"width:610px;height:370px;border: 0;\""%> marginwidth="0" frameborder="no" src ="" scrolling="auto"></iframe>
				<div style="text-align: center">
					<input class="btnClass5 mr23" onclick="tempSure()" value="选择" type="button"/>
					<input class="btnClass6" onclick="tempCancel()" value="取消" type="button"/>
					<br/>
				</div>
			</div>
			<div id="shortUrlDiv" title="选择短链接" style="background-color:#FFFFFF;padding:5px;display:none;height: 520px;width: 1000px;">
			<iframe id="shortUrlFrame" name="shortUrlFrame" <%=skin.contains("frame4.0")?"style=\"width:950px;height:500px;border: 0;\"":"style=\"width:800px;height:500px;border: 0;\""%> marginwidth="0" scrolling="auto" frameborder="no" src ="" ></iframe>
				<%-- <div style="text-align: center">
					<input class="btnClass5 mr23" onclick="urlSure()" value="选择" type="button"/>
					<input class="btnClass6" onclick="urlCancel()" value="取消" type="button"/>
					<br/>
				</div> --%>
			</div>
			<div id="addSmsTmpDiv" title="新建模板" style="padding:5px;display:none">
				<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" style="display:none;width:610px;height:480px;border: 0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
			</div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			
			<div id="draftDiv" title="草稿箱选择" style="padding:5px;display:none">
                <iframe id="draftFrame" name="draftFrame" style="width:880px;height:400px;border: 0;" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
                <div style="text-align: center">
                    <input class="btnClass5 mr23" onclick="draftSure()" value="选择" type="button"/>
                    <input class="btnClass6" onclick="draftCancel()" value="取消" type="button"/>
                    <br/>
                </div>
            </div>
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
		<div id="bulkImport_box" title="批量号码输入" style="display:none;">
			   <div class="imporInner div_bd">
			   		<textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onblur="formatTelNum('#bNum')"></textarea>
			   		<textarea name="importAreaTemp" id="importAreaTemp" style="display:none"></textarea>
			   </div>
			   <div class="bultMark">
			   		<span id="bultNum">当前共<font color='blue'><b id="bNum"></b></font>/20000个号码</span>
			   		<div style="height:25px"></div>
			   		<div class="numSplit">
			   			多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开
			   		</div>
			   </div>
			   <div class="bultBtn">
			   	<input onclick="previewTel()" class="btnClass5 mr23" type="button" value="确认">
			   	<input onclick="bultCancel()"  class="btnClass6" type="button" value="返回">
			   	<br/>
			   </div>
		</div>
		<div id="message" title="提示" style="padding:5px;display:none;font-size: 12px;">
				<div style="height:25px"></div>
		 		<center>
				<label >短链发送提交成功！</label>
				<a id="sendRecord" href="javascript:sendRecord('5600-1000',
				<%=oldTaskId%>,<%=lguserid%>,<%=lgcorpcode%>)"  class="alink" >查看发送记录</a>
				</center>
				<br>
				<div style="height:10px"></div>
				 <center>
				    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="关闭" />
				</center> 
		</div>
		<div id="urllist" style="display:none;">
				<%if(domainList!=null &&!domainList.isEmpty()){
				for (LfDomain lfDomain : domainList) {
				out.print("<input type=\"hidden\" value=\""+lfDomain.getDomain()+"\" />");
				}
				} %>
				</div>
				
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/ssm_batchFileSend.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=iPath%>/js/ssm_sendBatchSMS.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
			<script src="<%=commonPath %>/common/js/jquery.selection.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script>
		var userAgent = navigator.userAgent;
		var isChrome = 0;
		if (userAgent.indexOf("Chrome") > -1)
		{
   			isChrome = 1;
  		}
		 $(document).ready(
		 function(){
		   var result = base_result;
			if(result!="-1")
			{
				if(result.indexOf("empex") == 0)
				{
					alert("任务创建失败："+result.substr(5));
				}else
				if(result=="timerSuccess")
				{
					alert("创建短信任务及定时任务添加成功！");
					getCt();
				}else if(result=="timerFail")
				{
					alert("创建定时任务失败，取消任务创建！");
				}else if(result=="uploadFileFail")
				{
					alert("上传号码文件失败，取消任务创建！");
				}else if(result=="createSuccess")
				{ 
					alert("创建短信任务及提交到审批流成功！");
					getCt();
				}else if(result=="timeError"){
					alert("发送时间已经超过定时时间，不能发送！");
					getCt();
				}else if(result=="000")
				{
					//alert("创建短信任务及发送到网关成功！");
					getCt();
					<%session.removeAttribute("surl_batchResult");%>
					$("#message").dialog({width:300,height:180});
					$("#message").dialog("open");	
				}else if(result=="saveSuccess")
				{
					alert("存草稿成功！");
				}else if(result=="error")
				{
					alert("请求响应超时，创建短链任务失败！");
				}else if(result=="nospnumber")
				{
					alert("发送失败，" + '<%=StaticValue.SMSACCOUNT%>' + "未设置尾号！");
				}else if(result=="depfee:-2")
				{
				    alert("机构余额不足,创建短信任务失败！");
				}else if(result=="depfee:-1")
				{
				    alert("创建短信任务时,修改计费信息失败！");
				}else if(result=="spuserfee:-2")
				{
				    alert("SP账号余额不足,创建短信任务失败！");
				}else if(result=="spuserfee:-1"||result=="spuserfee:-3")
				{
				    alert("创建短信任务时,检查SP账号费用失败！");
				}else if(result=="nospnumber")
				{
					alert("发送失败，" + '<%=StaticValue.SMSACCOUNT%>' + "未设置尾号！");
				}else if(result == "subnoFailed"){
						alert("拓展尾号处理失败！");
				}else if("nogwfee"==result || "feefail"==result){
					alert("获取运营商余额失败，取消任务创建！");
				}else if(result.indexOf("lessgwfee")==0){
					alert("运营商余额不足，取消任务创建！");
				}else if(result == "waitingResult") {
				    alert("任务已创建，请等待处理结果");
                } else {
					alert("向网关发送请求失败:"+result);
				}
				<%session.removeAttribute("surl_batchResult");%>
			}
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
			getGateNumber();
			$("#spUser,#busCode").bind("change", function(){getGateNumber();});

			$("#showUrl").dialog({
				autoOpen: false,
				width:314,
			    height:380,
			    title:"链接预览",
				modal: true
			});
		 })
		 
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
		 

		</script>
	</body>
</html>
