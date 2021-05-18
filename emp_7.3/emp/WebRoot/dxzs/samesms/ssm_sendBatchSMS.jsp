<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.dxzs.LfDfadvanced"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Random"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

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
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get(request.getAttribute("reTitle"));
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String templateMenuCode = titleMap.get("smsTemplate");
	//String isFlow = (String) request.getAttribute("isFlow");
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
 	// 短信是否分批
    String confSmsSplit = (String) request.getAttribute("confSmsSplit");
    //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
    
    //是否集成了短信模板模块
    boolean isHaveTemp=false;
    if(StaticValue.getInniMenuMap().containsKey(ViewParams.TEMP_MENU_HTM))
    {
    	isHaveTemp=true;
    }
    
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String depSignLeft = DxzsStaticValue.getSAMESMS_DEP_SIGN_LEFT();
	String depSignRight = DxzsStaticValue.getSAMESMS_DEP_SIGN_RIGHT();
 	String nameSignLeft = DxzsStaticValue.getSAMESMS_NAME_SIGN_LEFT();
	String nameSignRight = DxzsStaticValue.getSAMESMS_NAME_SIGN_RIGHT();
	String specialChar = StaticValue.getSmscontentSpecialcharStr();
    
    //语言方面相关
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_1" defVal="相同内容群发" fileName="dxzs"/></title>
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
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/ssm_sendBatchSMS.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydzs_DuanXinZhuShou.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style>
			#tdSign,#configBatch{
				margin: 10px;
			}
			.custBatchNode{
				float: left;
				width: 95%;
			    margin-bottom: 10px;
			    border-top: 1px solid gray;
			    border-bottom: 1px solid gray;
			    border-left: 0px;
			    border-right: 0px;
			}
			.custBatchNode .custBatchNode-left{
				float: left;
				width: 16%;
				margin-top: 15px;
				margin-bottom: 10px;
			}
			.custBatchNode .custBatchNode-left a{
				display: block;
			    color: #2893c7;
			    text-decoration: underline;
			}
			.nodeControl{
			    clear: both;
				text-align: center;
			}
			.nodeControl a{
				margin-left: 15px;
				color: #2893c7;
			    text-decoration: underline;
			}
			.custBatchNode .custBatchNode-middle{
				float: left;
				width: 27%;
				margin-left: 20px;
				margin-top: 20px;
			}
			.custBatchNode .custBatchNode-middle .batchNodeValue{
				width: 50px;
			}
			.custBatchNode .custBatchNode-right{
			    float: left;
			    width: 48%;
			    margin-left: 20px;
			    margin-top: 20px;
			}
			.custBatch span{
				margin:10px;
			}
			#splitFlagN{
			    margin-left: 4px;
			}
			#splitFlagY{
			    margin-left: 20px;
			}
			#batchDetial table{
				margin: 15px;
			}
			#batchDetial table th{
				background-color: #f5f8fa;
			}
			#batchDetial .td-batch-left, #batchDetial .td-batch-middle, #batchDetial .td-batch-right{
				border: 1px solid #eceff1;
				text-align: center;
			}
			#batchDetial .td-batch-left, #batchDetial .td-batch-middle{
				width: 35%;
				height: 10%;
				padding: 15px;
			}
			#batchDetial .td-batch-right{
				width: 150px;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<style type="text/css">
			div#effinfo{width: 280px;}
			td#preinfonum{padding-left: 10px;}
			div#effinfo table tr td:nth-child(1){width: 130px;}
		</style>
		<%}%>
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
	<body id="ssm_sendBatchSMS">
		<div id="container">
			<%=ViewParams.getPosition(langName,menuCode) %>
			<div id="rContent" class="rContent dxzs_div" >
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
				<div id="batchFileSend" class="block">
					<form id="form2" name="form2" action="ssm_previewSMS.htm?method=preview" method="post" enctype="multipart/form-data" target="hidden_iframe">
						<input name="hidemsg" type="hidden" value="" id="reinfo">
						<input type="hidden" name="phoneStr" id="phoneStr" value=","/>
						<input type=hidden name="flag" value="<%=str_flag%>" />
						<input type="hidden" id="tss" name="tss" value="e" />
						<input type="hidden" value="1" id="bmtType" name="bmtType" />
						<input type="hidden" name="groupIds" id="groupIds" value=","/><%--员工群组信息 --%>
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
						<input type="hidden" value="" id="gwFee" name="gwFemployeeIdsee"/><%-- 运营商扣费状态 --%>
						<input type="hidden" value="" id="tailcontents" name="tailcontents"/><%-- 短信贴尾内容 --%>
						<input type="hidden" value="" id="empIds" name="empIds"/><%-- 员工Ids --%>
						<input type="hidden" value="" id="malIds" name="malIds"/><%-- 自建Ids --%>
						<input type="hidden" value="0" id="hidIsDoOk" name="hidIsDoOk"/><%-- 控制选择人员弹出框关闭时清空。0为关闭操作；1为确定操作 --%>
						<input type="hidden" value=""  id="bigfileid" name="bigfileid"/><!-- 超大文件 -->
						<input type="hidden" value=""  id="bigtaiskids" name="bigtaiskids"/><!-- 超大文件 -->
						<%--
						
						 <input type="hidden" value="0" id ="manCountTemp" name ="manCountTemp"/>
						 --%>
						<div class="dxzs_display_none" id="allfilename" ></div>
						<div class="dxzs_display_none" id="hiddenValueDiv"></div>
						
						<%--草稿箱--%>
                    	<input type='hidden' id="draftFile" name='draftFile' value=''/>
                    	<input type='hidden' id="draftFileTemp" name='draftFileTemp' value=''/>
                    	<input type='hidden' id="draftId" name='draftId' value=''/>
						
						<div id="eq_sendDiv" class="dxzs_eq_sendDiv">
						<span class="righttitle" ><emp:message key="dxzs_xtnrqf_title_2" defVal="发送主题" fileName="dxzs"/>：</span>
							<input id="taskname" name="taskname" type="text" class="graytext input_bd div_bd dxzs_taskname" maxlength="20" value="<emp:message key='dxzs_xtnrqf_title_209' defVal='不作为短信内容发送' fileName='dxzs'/>"/>
						</div>
						<div class="clear2"></div>
						
						<div id="eq_sendDiv">
							<span class="righttitle"><emp:message key="dxzs_xtnrqf_title_3" defVal="发送号码" fileName="dxzs"/>：</span>
							<div id="dds" class="div_bd">
								<table id="vss">
									<tr id="first" class="title_bd">
										<td class="div_bd title_bg dxzs_td1" ><emp:message key="dxzs_xtnrqf_title_4" defVal="类型" fileName="dxzs"/></td>
										<td class="div_bd title_bg"><emp:message key="dxzs_xtnrqf_title_5" defVal="号码" fileName="dxzs"/></td>
										<td class="div_bd title_bg dxzs_td2" ><emp:message key="dxzs_xtnrqf_title_6" defVal="操作" fileName="dxzs"/></td>
									</tr>
								</table>
							</div>
							<div class="selectFileTable">
							<table class="dxzs_table" >
							<%
							boolean isEmpty = false;
							if("1050-1000".equals(menuCode)){
								isEmpty = btnMap.get(menuCode+"-8")==null&&btnMap.get(menuCode+"-7")==null 
								&& btnMap.get(menuCode+"-6")==null;
							}else{
								isEmpty = btnMap.get(menuCode+"-7")==null 
								&& btnMap.get(menuCode+"-6")==null;
							}
							if(isEmpty){%>
							<tr id="picTab" class="dxzs_picTab div_bg"><td class="div_bd div_bg"></td></tr>
					<%}if(btnMap.get(menuCode+"-8")!=null||btnMap.get(menuCode+"-7")!=null){ %>
							<tr id="picTab" class="dxzs_picTab1 div_bg">
							<%if(btnMap.get(menuCode+"-8")!=null){%>
							<td class="ssm-send-import-btn div_bd" align="center" style="cursor:pointer;" onclick="showInfo();"
							<%if("1050-1000".equals(menuCode) && btnMap.get(menuCode+"-7")==null){%> colspan="2" <%} %> class="div_bd div_bg">
									<div  class="dxzs_div1" >
										<a class="selectEmp"></a><div class="mt10"><emp:message key="dxzs_xtnrqf_title_7" defVal="选择人员" fileName="dxzs"/></div>
									</div>
								</td>
							<%} %>	
							<%if(btnMap.get(menuCode+"-7")!=null){%>
							<td  class="div_bd dxzs_cursor ssm-send-import-btn" align="center"
							<%if(("1050-1000".equals(menuCode) && btnMap.get(menuCode+"-8")==null) || "2300-1100".equals(menuCode)){%> colspan="2" <%} %>>
                                 <div class="dxzs_div2">
								 	<a class="importFile"/></a><div class="mt10"><emp:message key="dxzs_xtnrqf_title_8" defVal="导入文件" fileName="dxzs"/></div>
                                 	<div id="filesdiv" class="dxzs_filesdiv">
								 		<input id='numFile1' name='numFile1'  type=file onclick="checkbigfile()"; onchange="addFiles()" class="numfileclass"/>
								 	</div>
								 </div>
								</td>
							<%} %>
							<%if(btnMap.get(menuCode+"-3")!=null){%>
								<td  class="div_bd ssm-send-import-btn" align="center" style='cursor:pointer;' onclick="choosebigfile()">
									<div style="position:relative;width:48px;">
									 	<a class="importbigFile"/></a><div class="mt10">超大文件</div>
									 </div>
								</td>
							<%}%>
							<%if(btnMap.get(menuCode+"-2")!=null){%>
								<td  class="dxzs_td1 div_bd <%= skin.indexOf("4.0")>-1?"ssm-send-import-btn3":"ssm-send-import-btn2"%> " align="center" onclick="bulkImport();">
									<a class="bulkImport"/></a><div class="mt10"><emp:message key="dxzs_xtnrqf_title_9" defVal="批量输入" fileName="dxzs"/></div>
								</td>
							<%}%>
							</tr>
						<%} %>
						<%if(btnMap.get(menuCode+"-6")!=null){%>
							<tr>
								<td colspan="3" align="center" class="div_bd div_bg">
								
									<table width="275px" class="div_bd">
									<tr>
									<td width="230px">
									<input value="<emp:message key='dxzs_xtnrqf_title_72' defVal='请输入手机号' fileName='dxzs'/>" class="graytext dxzs_input" type="text"
									onkeyup="phoneInputCtrl($(this))" onpaste="phoneInputCtrl($(this))" maxlength="21" name="tph" id="tph" onpropertychange="addph()"/>
									</td>
									<td align="center" onclick="addph2()"  class="div_bd dxzs_cursor"><emp:message key='dxzs_xtnrqf_title_10' defVal='添加' fileName='dxzs'/></td>
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
						<div class="dxzs_both"></div>
						<div class="dxzs_div3" align="right">
							<a id="downlinks" class="dxzs_downlinks"><emp:message key="dxzs_xtnrqf_title_11" defVal="格式提示" fileName="dxzs"/></a>
							<input type="text" class="dxzs_display_none" value="<%=skin %>" id="skin">
							<div id="descriptInfo" style="display: none;">
								<img src="<%=iPath %>/image/<%=langName%>_samedesc.png"/>
							</div>
					    </div>
						<div id="eq_sendDiv" class="dxzs_eq_sendDiv">
						<span class="righttitle"><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>：</span>
						<table  class="dxzs_table div_bd">
						<tr class="dxzs_tr">
						<td class="dxzs_td div_bd">
						<%if(btnMap.get(menuCode+"-4") != null){%>
						 <a  id = "ctem" class="dxzs_ctem div_bg div_bd" href="javascript:chooseTemp()"><emp:message key="dxzs_xtnrqf_title_13" defVal="点击选择模板" fileName="dxzs"/></a>			
							<a  id = "qtem" class="dxzs_qtem div_bg div_bd" href="javascript:tempNo()"><emp:message key="dxzs_xtnrqf_title_14" defVal="点击取消模板" fileName="dxzs"/></a>	
							<%} %>&nbsp;
							<%if(btnMap.get(templateMenuCode+"-1" )!= null && isHaveTemp){%> 			    
							<a href="javascript:showAddSmsTmp(0)" >[<emp:message key="dxzs_xtnrqf_title_15" defVal="立即新建" fileName="dxzs"/>]</a>
							
							<%} %> 
							<a href="javascript:showDraft()" class="dxzs_a"><emp:message key="dxzs_xtnrqf_title_16" defVal="草稿箱" fileName="dxzs"/></a>
							<br/>
						</td>
						<td class='<%="zh_HK".equals(empLangName)?"div_bd dxzs_td1":"div_bd dxzs_td2"%>'>
						<emp:message key="dxzs_xtnrqf_title_17" defVal="前缀签名" fileName="dxzs"/>：
						<input type="checkbox" value="" name="depSign" id="depSign" class="dxzs_vertical_align"  onclick="setDepSign(this)" />&nbsp;<emp:message key="dxzs_xtnrqf_title_18" defVal="机构" fileName="dxzs"/>&nbsp;&nbsp;
						<input type="checkbox" value="" name="nameSign" id="nameSign" class="dxzs_vertical_align"  onclick="setNameSign(this)"/>&nbsp;<emp:message key="dxzs_xtnrqf_title_19" defVal="姓名" fileName="dxzs"/>
						</td>
						</tr>
						<tr class="dxzs_tr1">
						<td colspan="2">
							<textarea class="dxzs_msg" name="msg" rows="5" id="contents" onblur="eblur($(this))"></textarea>
<%-- 							<textarea style="width:570px;height:124px;border:0px;overflow:auto;" class=".msg2" name="msg" rows="5" id="contents" onblur="eblur($(this))"></textarea> --%>
						</td>
						</tr>
						<tr id="tail-area" class="div_bd">
                                <td id="tail-text" colspan="2">
                                   	 <emp:message key="dxzs_xtnrqf_title_20" defVal="贴尾内容" fileName="dxzs"/>：<label id="showtailcontent"></label>
                                </td>
                         </tr>
						</table>
								<font class="dxzs_font1"><b id="strlen"> 0 </b><b id="maxLen">/980</b></font>
								<font class="dxzs_font2">
								<emp:message key="dxzs_xtnrqf_title_21" defVal="移动" fileName="dxzs"/>(<b id="ft1">0</b>)&nbsp;&nbsp;
								<emp:message key="dxzs_xtnrqf_title_22" defVal="联通" fileName="dxzs"/>(<b id="ft2">0</b>)&nbsp;&nbsp;
								<emp:message key="dxzs_xtnrqf_title_23" defVal="电信" fileName="dxzs"/>(<b id="ft3">0</b>)&nbsp;&nbsp;
								<emp:message key="dxzs_xtnrqf_title_24" defVal="国外" fileName="dxzs"/>(<b id="ft4">0</b>)
								</font>
						</div>

						<div class="clear2"></div>
						
						<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>：</span>
						  <div class="dxzs_div4">
						    <input type="radio" name="timerStatus" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="dxzs_xtnrqf_title_26" defVal="立即发送" fileName="dxzs"/>
							<input type="radio" name="timerStatus" class="dxzs_timerStatus" value="1"
								onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="dxzs_xtnrqf_title_27" defVal="定时发送" fileName="dxzs"/>
							<label id="time2" class="dxzs_display_none">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
									id="timerTime" name="timerTime" value="">
							</label>
						  </div>
						</div>
						
						<div id="eq_sendDiv2" class="dxzs_eq_sendDiv2">
						<span id="u_o_c_explain" class="div_bg"><b><emp:message key="dxzs_xtnrqf_title_28" defVal="高级设置" fileName="dxzs"/></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="dxzs_text_decoration unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
						<div id="moreSelect" class="div_bg">
						<div class="dxzs_div5" align="right">
							<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="dxzs_xtnrqf_title_29" defVal="选项存为默认" fileName="dxzs"/></a>
						</div>
						<%-- 业务类型 --%>
						<table class="dxzs_table1">
						<tr class="dxzs_tr2">
							<td class="dxzs_td3">
								<emp:message key="dxzs_xtnrqf_title_30" defVal="业务类型" fileName="dxzs"/>：
							</td>
						<td>
						<select id="busCode"  class="input_bd dxzs_busCode"  name="busCode">
									<%
										if (busList != null && busList.size() > 0) {
											String busCode = lfDfadvanced != null?lfDfadvanced.getBuscode():"";
											for (LfBusManager busManager : busList) {
									%>
									<option value="<%=busManager.getBusCode()%>" 
									<%=busCode != null && !"".equals(busCode) && busCode.equals(busManager.getBusCode())?"selected":"" %>>
										<%String name = busManager.getBusName().replace("默认业务", MessageUtils.extractMessage("ydwx","ydwx_defaultBussiness",request));%>
										<%=name.replace("<","&lt;").replace(">","&gt;") + "("+ busManager.getBusCode() +")"%>
									</option>
									<%
										}
										}
									%>
						</select>
						</td>
						</tr>
						<tr class="dxzs_tr2">
						<td class="dxzs_td6"><emp:message key="dxzs_xtnrqf_title_31" defVal="发送级别" fileName="dxzs"/>：</td>
						<td>
						<select id="priority" class="input_bd dxzs_priority" name="priority">
							<option value="0"><emp:message key="dxzs_xtnrqf_title_32" defVal="系统智能控制" fileName="dxzs"/></option>
							<option value="1">1(<emp:message key="dxzs_xtnrqf_title_33" defVal="优先级最高" fileName="dxzs"/>)</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9(<emp:message key="dxzs_xtnrqf_title_34" defVal="优先级最低" fileName="dxzs"/>)</option>
							</select>
						</td>
						</tr>
						<tr class="dxzs_tr2">
						<td class="dxzs_td4"><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>：</td>
						<td>
						<select id="spUser"  class="input_bd dxzs_spUser" name="spUser">
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
						<tr  class="dxzs_tr2">
						<td class="dxzs_td4"><emp:message key="dxzs_xtnrqf_title_35" defVal="回复设置" fileName="dxzs"/>：</td>
						<td>
						<input type="radio"  name="isReply" value="0" onclick="getGateNumber()" 
								<%if(lfSysuser.getIsExistSubNo() != 1){ %> checked="checked" <%} %> style="margin-left: 4px;" />&nbsp;<emp:message key="dxzs_xtnrqf_title_36" defVal="不用回复" fileName="dxzs"/>
								<input type="radio"  name="isReply" value="1" onclick="getGateNumber()" style="margin-left: 10px;" />&nbsp;<emp:message key="dxzs_xtnrqf_title_37" defVal="本次任务" fileName="dxzs"/>
								<input type="radio"  name="isReply" value="2" onclick="getGateNumber()" 
								<%if(lfSysuser.getIsExistSubNo() != 1){ %> disabled="disabled" <%}else{ %>
								checked="checked"<%} %>
								style="margin-left: 10px;"/><label <%if(lfSysuser.getIsExistSubNo() != 1){ %> style="color:gray;" <%}else{%>
								style="color:#000000;"
								 <%}%>>&nbsp;<emp:message key="dxzs_xtnrqf_title_38" defVal="我的尾号" fileName="dxzs"/></label><label class="dxzs_curSubNo" id="curSubNo"><emp:message key="dxzs_xtnrqf_title_39" defVal="当前尾号" fileName="dxzs"/>：</label>
								 <label id="subno" ></label>
								 <br/>
						</td>
						</tr>
						<% if("1".equals(confSmsSplit)){ %>
							<tr  class="dxzs_tr2">
								<td class="dxzs_td4">分批设置：</td>
								<td>
									<input type="radio" id="splitFlagN" name="splitFlag" onchange="testBigFile()" checked="checked" value="0" />
									<label for="splitFlagN">不分批</label>
									<input type="radio" id="splitFlagY" name="splitFlag" onchange="testBigFile()" value="1"/>
									<label for="splitFlagY">分批</label>
								</td>
							</tr>
						<%} %>
						</table>
						</div>
						</div>
						<div class="b_F_btn">
							<input type="hidden" value="1" id="subState" name="subState" />
							<input id="toDraft" type="button" value="<emp:message key='dxzs_xtnrqf_button_1' defVal='暂存草稿' fileName='dxzs'/>" class="btnClass5 mr23"/>
							<input id="subSend" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_2' defVal='提交' fileName='dxzs'/>" />
							<input id="qingkong" onclick="javascript:reloadPage();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_3' defVal='重置' fileName='dxzs'/>" />
						</div>
						</form>
						
						<div class="clear2"></div>
						<div id="infoDiv" title="<emp:message key='dxzs_xtnrqf_title_40' defVal='选择发送对象' fileName='dxzs'/>">
							<input id="flowNames" type="hidden" name="flowNames" value="" />
							<iframe id="flowFrame" name="flowFrame" class="dxzs_flowFrame" src="<%=context %>/ssm_chooseSendInfo.jsp?lguserid=<%=lguserid %>" marginwidth="0" scrolling="no" frameborder="no"></iframe>
							<input id="lguserid" type="hidden" name="lguserid" value="<%=lguserid %>" />
							<input id="lgcorpcode" type="hidden" name="lgcorpcode" value="<%=lgcorpcode %>" />
							<input id="commonPath" type="hidden" name="commonPath" value="<%=commonPath %>" />
							<%--用于数据回显--%>
                    		<input type="hidden" class="dxzs_display_none" id="rightSelectedUserOption" value=""/>
							<table style="border:0">
							<tr><td class="dxzs_td8" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button"  value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass5 mr23" onclick="doOk()"/>
								<input type="button" id="closeBtn" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" class="btnClass6" onclick="doSelectEClose()" />
								<input type="button"  value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/>" class="btnClass6" onclick="doNo()"/>
							</td>
							</tr>
							</table> 
						</div>
						
						<div id="detail_Info" class="dxzs_detail_Info">							  
							<table id="infos" class="infos">
							  <tr >
							    <td class="infostd"><emp:message key="dxzs_xtnrqf_title_41" defVal="发送条数" fileName="dxzs"/>：<span id="yct"></span></td>								    
							    <td class="infostd" id="showyct"><emp:message key="dxzs_xtnrqf_title_42" defVal="机构余额" fileName="dxzs"/>：<span id="ct"></span></td>
							    <td class="infostd" id="showyspbalance"><emp:message key="dxzs_xtnrqf_title_43" defVal="SP账号余额" fileName="dxzs"/>：<span id="spbalance"></span></td>							    
							  </tr>
							   <tr>
								  <td colspan="2">
								    <label id="messages2"><font class="dxzs_font3"></font></label>
								  </td>
							  </tr>
							  <tr>
								  <td colspan="2">
								    <label id="messages1"><font class="dxzs_font3"><emp:message key="dxzs_xtnrqf_title_44" defVal="机构余额不足，不允许进行发送!" fileName="dxzs"/></font></label>
								  </td>
							  </tr>
							  <tr>
								  <td><emp:message key="dxzs_xtnrqf_title_45" defVal="有效号码数" fileName="dxzs"/>：<span id="effs"></span></td>
								  <td><emp:message key="dxzs_xtnrqf_title_46" defVal="提交号码数" fileName="dxzs"/>：<span id="counts"></span></td>								  
								  <td align="center">
								   <div id="showeffinfo" class="div_bg">
								       <p style="font-size: 12px;"><emp:message key="dxzs_xtnrqf_title_47" defVal="无效号码" fileName="dxzs"/>(<span id="alleff" class="dxzs_font3"></span>)&nbsp;&nbsp;
								   <a id="arrowhead"  class="dxzs_text_decoration unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></p> 
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
								      <td align="left"><emp:message key="dxzs_xtnrqf_title_48" defVal="黑名单号码" fileName="dxzs"/></td>
								      <td><span id="blacks"></span></td>
									  <td id="preinfonum">
								      <%
										if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) 
										{
									  %>
								      	<a href="javascript:uploadbadFiles()"><emp:message key="dxzs_xtnrqf_title_49" defVal="详情下载" fileName="dxzs"/></a>
								      <%
								      	}
								      %>
								      <input type="hidden" id="badurl" name="badurl"  value=""></input>
								      </td>
							      </tr>
							      <tr>
								      <td align="left"><emp:message key="dxzs_xtnrqf_title_50" defVal="重复号码" fileName="dxzs"/></td>
									  <td><span id="sames"></span></td>
									  <td></td>
							      </tr>
							      <tr>
							          <td align="left"><emp:message key="dxzs_xtnrqf_title_51" defVal="格式非法" fileName="dxzs"/></td>
									  <td><span id="legers"></span></td>
									  <td></td>
							      </tr>
							    </table>
							    </center>
						    </div>
						    <div class="dxzs_div6"></div>	
							<p class="dxzs_p2">
								<emp:message key="dxzs_xtnrqf_title_52" defVal="部分预览" fileName="dxzs"/>：
							</p>
							<div id="maindiv" class="dxzs_maindiv">
							<center>
							<table id="content" class="dxzs_content"></table>
							</center>
							</div>
					    	<div id="footer" class="dxzs_footer">
					    		<div id="tdSign"></div>
					    		<div id="configBatch"></div>
					    	  <center>
					    		<input id="btsend" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_6' defVal='发送' fileName='dxzs'/>" onclick="javascript:btsend()"/>
							    <input id="btcancel" onclick="javascript:previewCancel();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" />
								<br/>
							  </center> 
					    	</div>	
						</div>
						
						<div class="clear2"></div>
					
						<div id="infomodel" class="dxzs_infomodel" >
	                       <table class="dxzs_table3">
	                         <tr>
                            <td colspan="2" class="dxzs_td5" align="left"><emp:message key="dxzs_xtnrqf_title_53" defVal="文件格式如图" fileName="dxzs"/>:</td>
                          	</tr>
	                         <tr>
	                          <td class="dxzs_td6">
	                        <emp:message key="dxzs_xtnrqf_title_54" defVal="txt格式" fileName="dxzs"/>
	                          </td>
	                          <td rowspan="2"><img id="foldIcon" src="<%=commonPath %>/common/img/mmsphone-<%=langName %>.png"/></td>
	                         </tr>
	                         <tr>
	                         <td class="dxzs_td6"><emp:message key="dxzs_xtnrqf_title_55" defVal="excel格式" fileName="dxzs"/></td>
	                         </tr>
	                         <tr>
	                         <td align="left" colspan="2">
	                         <span class="dxzs_td5"><emp:message key="dxzs_xtnrqf_title_56" defVal="注意" fileName="dxzs"/>：</span>
	                         </td>
	                         </tr>
	                         <tr>
	                         <td align="left" colspan="2" style="line-height:20px">
	                         1.<emp:message key="dxzs_xtnrqf_title_57" defVal="手机号码格式不正确，在上传时将由EMP平台过滤；" fileName="dxzs"/><br />
	                         2.<emp:message key="dxzs_xtnrqf_title_58" defVal="号码包含于黑名单内，在上传时将由EMP平台过滤；" fileName="dxzs"/><br />
	                         3.<emp:message key="dxzs_xtnrqf_title_59" defVal="文件需小于" fileName="dxzs"/><%=StaticValue.MAX_SIZE/1024/1024%>M，<emp:message key="dxzs_xtnrqf_title_60" defVal="有效号码需少于" fileName="dxzs"/><%=StaticValue.MAX_PHONE_NUM%><br />
	                         4.上传超大文件总大小小于1000M，有效号码数小于5000万<br />
	                         5.<emp:message key="dxzs_xtnrqf_title_62" defVal="文件仅支持txt、zip、rar、xls、xlsx、csv、et格式，请优先使用zip文件格式" fileName="dxzs"/>
	                         </td>
	                         </tr>
	                       </table>
	                   </div>
	                   
						<div id="probar" class="dxzs_display_none">
							<p class="dxzs_p3">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="dxzs_xtnrqf_title_63" defVal="处理中,请稍等" fileName="dxzs"/>.....
							</p>
							<div id="shows">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<img class="dxzs_img"
									src="<%=commonPath%>/common/img/loader.gif" />
							</div>
						</div>
						
						<div id="confirmMsgText" style="display:none;padding: 30px;font-size: 16px;">
							当前发送内容中未添加退订指令，信息可能会被运营商退回，您确定继续发送吗？
							<div style="margin-top:50px;">
					    	  <center>
					    		<input onclick="javascript:confirmBtn();" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_8' defVal='确认' fileName='dxzs'/>"/>
							    <input onclick="javascript:cancelBtn();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" />
								<br/>
							  </center> 
					    	</div>	
						</div>
					</div>
				</div>
			
				<iframe name="hidden_iframe" id="hidden_iframe" class="dxzs_display_none"></iframe>
			
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div id="tempDiv" class="dxzs_tempDiv" title="<emp:message key='dxzs_xtnrqf_title_64' defVal='短信模板选择' fileName='dxzs'/>">
			<iframe id="tempFrame" name="tempFrame" class="dxzs_tempFrame" marginwidth="0" scrolling="no" frameborder="no" src =""></iframe>
				<div class="dxzs_div7">
					<input class="btnClass5 mr23" onclick="tempSure()" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>" type="button"/>
					<input class="btnClass6" onclick="tempCancel()" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" type="button"/>
					<br/>
				</div>
			</div>
			<div id="bigfileDiv" title="超大文件选择" style="background-color:#FFFFFF;padding:5px;display:none">
			<iframe id="bigfileFrame" name="bigfileFrame" style="width:1200px;height:652px;border: 0;" marginwidth="0" scrolling="yes" frameborder="no" src ="" ></iframe>
				<div style="text-align: center">
					<input class="btnClass5 mr23" onclick="bigfileSure()" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>" type="button"/>
					<input class="btnClass6" onclick="bigfileCancel()" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" type="button"/>
					<br/>
				</div>
			</div>
			<div id="batchDetial" style="display:none">
				<div class="dxzs_div7">
					<input onclick="closeBatchDetail()" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_8' defVal='确认' fileName='dxzs'/>">
				</div>
			</div>
			<div id="addSmsTmpDiv" class="dxzs_addSmsTmpDiv" title="<emp:message key='dxzs_xtnrqf_title_65' defVal='新建模板' fileName='dxzs'/>">
				<iframe id="addSmsTmpFrame" class="dxzs_addSmsTmpFrame" name="addSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
			</div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			
			<div id="draftDiv" title="<emp:message key='dxzs_xtnrqf_title_66' defVal='草稿箱选择' fileName='dxzs'/>" class="dxzs_addSmsTmpDiv">
                <iframe id="draftFrame" name="draftFrame" class="dxzs_draftFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
                <div class="dxzs_div7">
                    <input class="btnClass5 mr23" onclick="draftSure()" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>" type="button"/>
                    <input class="btnClass6" onclick="draftCancel()" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" type="button"/>
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
		<div id="bulkImport_box" title="<emp:message key='dxzs_xtnrqf_title_67' defVal='批量号码输入' fileName='dxzs'/>" class="dxzs_display_none">
			   <div class="imporInner div_bd">
			   		<textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onblur="formatTelNum('#bNum')"></textarea>
			   		<textarea name="importAreaTemp" id="importAreaTemp" class="dxzs_display_none"></textarea>
			   </div>
			   <div class="bultMark">
			   		<span id="bultNum"><font color='blue'><b id="bNum"></b></font>/20000</span>
			   		<div class="dxzs_tr"></div>
			   		<div class="numSplit">
			   			<emp:message key="dxzs_xtnrqf_title_70" defVal="多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="dxzs"/>
			   		</div>
			   </div>
			   <div class="bultBtn">
			   	<input onclick="previewTel()" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_8' defVal='确认' fileName='dxzs'/>">
			   	<input onclick="bultCancel()"  class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消 ' fileName='dxzs'/>">
			   	<br/>
			   </div>
		</div>
		<div id="message" title="<emp:message key='dxzs_xtnrqf_title_73' defVal='提示' fileName='dxzs'/>" class="dxzs_message">
				<div class="dxzs_tr"></div>
		 		<center>
				<label ><emp:message key="dxzs_xtnrqf_title_71" defVal="发送至网关成功！" fileName="dxzs"/></label>
				<a id="sendRecord" href="javascript:sendRecord('<%=menuCode%>',<%=oldTaskId%>,<%=lguserid%>,<%=lgcorpcode%>)"  class="alink" ><emp:message key="dxzs_xtnrqf_title_74" defVal="查看发送记录" fileName="dxzs"/></a>
				</center>
				<br>
				<div class="dxzs_div8"></div>
				 <center>
				    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_10' defVal='关闭' fileName='dxzs'/>" />
				</center> 
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
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
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
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
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_1')+result.substr(5));
				}else
				if(result=="timerSuccess")
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_2'));
					getCt();
				}else if(result=="timerFail")
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_3'));
				}else if(result=="uploadFileFail")
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_4'));
				}else if(result=="createSuccess")
				{ 
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_5'));
					getCt();
				}else if(result=="timeError"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_6'));
					getCt();
				}else if(result=="000")
				{
					//alert("创建短信任务及发送到网关成功！");
					getCt();
					<%session.removeAttribute("mcs_batchResult");%>
					$("#message").dialog({width:300,height:180});
					$("#message").dialog("open");	
				}else if(result=="saveSuccess")
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_7'));
				}else if(result=="error")
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_8'));
				}else if(result=="nospnumber")
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_9') + <%=StaticValue.SMSACCOUNT%> + getJsLocaleMessage('dxzs','dxzs_ssend_alert_10'));
				}else if(result=="depfee:-2")
				{
				    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_11'));
				}else if(result=="depfee:-1")
				{
				    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_12'));
				}else if(result=="spuserfee:-2")
				{
				    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_13'));
				}else if(result=="spuserfee:-1"||result=="spuserfee:-3")
				{
				    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_14'));
				}else if(result == "subnoFailed"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_15'));
				}else if("nogwfee"==result || "feefail"==result){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_16'));
				}else if(result.indexOf("lessgwfee")==0){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_17'));
				}else
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_18')+result);
				}
				<%session.removeAttribute("mcs_batchResult");%>
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
		 })
		 
		//谷歌浏览器 
		if(isChrome == 1)
		{
			function onbeforeCloseIt()
		    {
				 if(isSend == "0")
				 {
					 	 var logInfo = getJsLocaleMessage('dxzs','dxzs_ssend_alert_19')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
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
					 	 var logInfo = getJsLocaleMessage('dxzs','dxzs_ssend_alert_19')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
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
