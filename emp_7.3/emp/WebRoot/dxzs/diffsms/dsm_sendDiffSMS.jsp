<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.dxzs.LfDfadvanced"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@ page import="com.montnets.emp.entity.pasgrpbind.LfAccountBind"%>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@SuppressWarnings("unchecked")
	List<LfAccountBind> accountBindsList = (List<LfAccountBind>) request
			.getAttribute("accountBindsList");
	@ SuppressWarnings("unchecked")
	List<LfSpDepBind> userList = (List<LfSpDepBind>)request.getAttribute("sendUserList");
	
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String reTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(reTitle);

	menuCode = menuCode==null?"0-0-0":menuCode;
	String templateMenuCode = titleMap.get("smsTemplate");
	String result = session.getAttribute("eq_diffResult")==null?"-1":
		(String)session.getAttribute("eq_diffResult");
	
	String isTrue = (String)request.getAttribute("isTrue");
	String sendType=(String)request.getAttribute("sendType");
	
	String findResult= (String)request.getAttribute("findresult");
	
	//上传返回信息
    String message=(String)request.getAttribute("message");
    
    String taskId=(String)request.getAttribute("taskId");
    
	@SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>) request.getAttribute("spUserList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

    String lguserid = request.getParameter("lguserid");
	String lgcorpcode = request.getParameter("lgcorpcode");
    String oldTaskId = request.getParameter("oldTaskId");
    
    //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
	LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	//是否集成了短信模板模块
    boolean isHaveTemp=false;
    if(StaticValue.getInniMenuMap().containsKey(ViewParams.TEMP_MENU_HTM))
    {
    	isHaveTemp=true;
    }
	
	Integer isExistSubNo = 0;
	if(request.getAttribute("isExistSubNo") != null)
	{
		isExistSubNo = (Integer)request.getAttribute("isExistSubNo");
	}
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	// 短信是否分批
	String confSmsSplit = (String) request.getAttribute("confSmsSplit");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxzs_xtnrqf_title_206" defVal="短信客服不同内容群发" fileName="dxzs"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" >
		<link href="<%=commonPath %>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/params.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<script>
		var base_manualWriter = '<%=btnMap.get(menuCode+"-5") == null%>';
		var base_result = "<%=result%>";
		var base_sendType="<%=sendType%>";
		var base_message="<%=message%>";
		var base_findresult="<%=findResult%>";
		var base_reTitle = "<%=reTitle%>";
		var base_path = "<%=path%>";
		var base_commonPath = "<%=commonPath%>";
		var base_skin = "<%=skin%>";
		
		var base_menuCode = "<%=menuCode%>";
		//模板可编辑标识
		var tmpEditorFlag = "<%=StaticValue.getTMPEDITORFLAG()%>";
		var ZIP_SIZE = "<%=StaticValue.ZIP_SIZE/1024/1024%>";
   		var MAX_SIZE = "<%=StaticValue.MAX_SIZE/1024/1024%>";
    	var MAX_PHONE_NUM = "<%=StaticValue.MAX_PHONE_NUM/10000%>";
    	var isSend = 0;
		</script>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<style type="text/css">
			.showParams li { width: 70px; }
			.paraContent,.x-fileUpload,.tailContdiv{width: 640px; }
			#taskname{width:635px;}
			#showmessage,#showdtsend{width: 800px;}
			#batchFileSend{width: 740px;}
			#dtsend,#wjsend{width: 370px;}
			div#effinfo{width: 280px;}
			#showeffinfo p{width: 120px;background-position: 110px 8px;}
			div#effinfo table tr td:nth-child(1){width: 120px;}
			td#predowninfo{padding-left: 10px;}
		</style>
		<%}%>
		<style type="text/css">
			#sendDiffSMS #txtstyle{
				float:left;
				width: 155px;
				height: 69px;
				background : url('<%=commonPath %>/common/img/dtsend_<%=langName %>.png') no-repeat; 
			}
			#sendDiffSMS #xlsstyle{
				float:left;
				width: 165px;
				height: 85px;
				background: url('<%=commonPath %>/common/img/dtsend_<%=langName %>.png') no-repeat 0px -80px;
			}
			#sendDiffSMS #moreSelect{
				height: auto;
			}
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
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/dsm_sendDiffSMS.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydzs_DuanXinZhuShou.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="sendDiffSMS">
	    <input type="hidden" name="checkCount" id="checkCount" value="0"/>
		<input type="hidden" id="pathUrl" value="<%=request.getContextPath() %>" />
		<div id="container" class="container" >
			<%-- 当前位置 --%>
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<div class="clear"></div>
				<input type="hidden" name="htmName" id="htmName" value="dsm_<%=reTitle %>.htm" />
				<div id="batchFileSend"  >
					<form  name="form2" id="form2"
						action="dsm_previewSMS.htm?method=preview" method="post"
						enctype="multipart/form-data" target="hidden_iframe">
						<iframe name="hidden_iframe" id="hidden_iframe"></iframe>
						<div class="hidden" id="loginparam"></div>	
							<input type="hidden" value="2" id="bmtType" name="bmtType" />
							<input type="hidden" value="0" id="isOk" name="isOk" />
							<input type="hidden" value="" id="dtMsg" name="dtMsg" />
							
							<input type="hidden" value="" id="preStr" name="preStr" />
							
							<input type="hidden" value="" id="hidSubCount" name="hidSubCount" />
							<input type="hidden" value="" id="hidEffCount" name="hidEffCount" />
							<input type="hidden" value="" id="hidMobileUrl" name="hidMobileUrl" />
							<input type="hidden" value="" id="hidPreSendCount" name="hidPreSendCount" />
							
							<input type="hidden" value="" id="error" name="error">							
							<input type="hidden" value="dsm_<%=reTitle %>.htm" name="reTitle" id="reTitle"/>
							<input type="hidden" value="0" id="SendReq" name="SendReq">							
							<input type="hidden" value="" id="inputContent" name="inputContent" /><%--临时存储手工输入内容 --%>
							<input type="hidden" value="" id="busCodeHidden" name="busCodeHidden" /><%--业务类型的值 --%>
							<input type="hidden" value="" id="spuserHidden" name="spuserHidden" /><%--sp账号的值 --%>
							<input type="hidden" value="" id="sendtypeHidden" name="sendtypeHidden" /><%--发送类型的值 --%>
	           			     <input type="hidden" value ="" id="sendFlag" name="sendFlag"/><%--  发送帐号主通道号+循环尾号的位数超过２０位个数 --%>
	               			 <input type="hidden" value ="" id="subNo" name="subNo"/> <%-- 尾号 --%>
	              			 <input type="hidden" value ="" id="circleSubNo" name="circleSubNo"/> <%-- 尾号 --%>
	               			 <input type="hidden" value ="<%=taskId %>" id="taskId" name="taskId"/>
							 <input id="formName" value="form2" type="hidden" />
							<input id="cpath" value="<%=path %>" type="hidden" />
							<input id="path" value="<%=inheritPath %>" type="hidden" />
	               			 <input type="hidden" value="2" id="sendType" name="sendType">
	               			 <input type="hidden" value="" id="isCharg" name="isCharg"/>
	               			 <input type="hidden" value="" id="feeFlag" name="feeFlag"/><%-- SP账号计费类型 --%>
	               			 <input type="hidden" value="" id="gwFee" name="gwFee"/><%-- 运营商扣费状态 --%>
	               			 <input type="hidden"  value="" id="tailcontents" name="tailcontents" /><%-- 短信贴尾内容 --%>
	               			 <input type="hidden"  value="" id="tailstate" name="tailstate" /><%-- 短信贴尾状态 --%>
	               			 <%--草稿箱--%>
	                    	<input type='hidden' id="draftFile" name='draftFile' value=''/>
	                    	<input type='hidden' id="draftFileTemp" name='draftFileTemp' value=''/>
	                    	<input type='hidden' id="draftId" name='draftId' value=''/>
	               			 
	               			 <div id="allfilename" ></div>
							 <div id="dtsend" onclick="javascript:bycheck(1)" class="dtsendclass div_bg">
							     <div>
							     <font class="send_ac1"><emp:message key="dxzs_xtnrqf_title_207" defVal="动态模板发送" fileName="dxzs"/></font>
							     </div>
							 </div>
                            <div id="wjsend" onclick="javascript:bycheck(2)" class="wjsendclss div_bg">
                                 <div>
                                 <font class="send_ac2"><emp:message key="dxzs_xtnrqf_title_208" defVal="文件内容发送" fileName="dxzs"/></font>
                                 </div>
                            </div> 
                            
                            <%--动态模板中间的div --%> 
                            <div id="showdtsend" class="dxzs_showdtsend"> 	
                          <span  class="righttitle"><emp:message key="dxzs_xtnrqf_title_2" defVal="发送主题" fileName="dxzs"/>：</span>
							<input id="taskname" name="taskname" type="text" class="graytext input_bd div_bd" maxlength="20" value="<emp:message key='dxzs_xtnrqf_title_209' defVal='不作为短信内容发送' fileName='dxzs'/>"/>
							</div>
						<div class="clear2"></div>
						<div id="showdtsend"  > 
						 <span  class="righttitle"><emp:message key="dxzs_xtnrqf_title_8" defVal="导入文件" fileName="dxzs"/>：  
						 	<input id='sss' name='sss'  type="hidden"  class="numfileclass2" value=""/>
						 </span>
						 <%-- <div id="filesdiv" style="position:relative;width:510px;overflow:hidden;">
						 							<a id="downlinks" style="cursor:pointer;position:absolute;right:0;top:5px;">格式提示</a>
						 							 <a id="afile" name="afile" class="afileclass1" >上传文件</a>	
						 	                          <input id='numFile1' name='numFile1'  type=file onchange="ch();" 
						 	                          class="numfileclass2" value="" />						  
						 							  <font class="zhu">&nbsp;&nbsp;(最大100M)</font>
						 							  
						  </div>
						  <div style="padding-left:78px;float:left;">
						 							  <div id="upfilediv" style="display: none;width:500px;margin-bottom:10px;" class="div_bg">	
						 							  </div>
						 							  </div> --%>
						 		<input type="hidden"  value="<%=skin %>" id="skin">
								<div id="dsm_descriptInfo" style="left: <%=langName.equals(StaticValue.ZH_HK)?"750px":"650px"%>;top:10px;position: absolute;z-index: 5;display: none;">
									<img src="<%=iPath %>/image/<%=langName%>_diffdynamic_desc.png"/>
								</div>					  
						 		<div class="x-fileUpload div_bd">
									<div class="div_bd div_bg x-uploadButton"  id="downlinks">
										<a href="javascript:void(0)" class="x-fileBtn">
											<emp:message key="dxzs_xtnrqf_title_210" defVal="上传文件" fileName="dxzs"/>
										</a>
										<input type="file" name="numFile1" id="numFile1" onchange="ch()" class="x-numfileclass" >
									</div>
									<div class="x-fileList">
										<ul id="upfilediv">
											
										</ul>
									</div>
									
								</div>
						
								<div id="wtailcontents" class="tailContdiv div_bd">
									<div class="draftButton" >
										<a href="javascript:showDraft(2)"><emp:message key="dxzs_xtnrqf_title_16" defVal="草稿箱" fileName="dxzs"/></a>
									</div>
									<div id="tailContendDiv">
										<div id="tailShowDiv">
										<emp:message key="dxzs_xtnrqf_title_20" defVal="贴尾内容" fileName="dxzs"/>：<label class="tailcontents"></label>
										</div>
									</div>
									
								</div>
                            
                              </div>
							<div id="showmessage"> 
							<span class="righttitle"><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>：</span>
						  <div class="paraContent div_bd">
							<div class="tit_panel div_bd">
								<a href="javascript:void(0)" class="para_cg"><emp:message key="dxzs_xtnrqf_title_211" defVal="关闭参数" fileName="dxzs"/></a>
								<%if(btnMap.get(menuCode+"-4") != null){%>
								<a id="ctem" class="mr10" href="javascript:chooseTemp()"><emp:message key="dxzs_xtnrqf_title_13" defVal="点击选择模板" fileName="dxzs"/></a>
								<a id="qtem" class="mr10" href="javascript:tempNo()"><emp:message key="dxzs_xtnrqf_title_14" defVal="点击取消模板" fileName="dxzs"/></a>
								<%} %>
								<%if(btnMap.get(templateMenuCode+"-1" )!= null && isHaveTemp){%>
								<a href="javascript:showAddSmsTmp(0)"><emp:message key="dxzs_xtnrqf_title_65" defVal="新建模板" fileName="dxzs"/></a>
								<%} %>
								<a class="dxzs_a" href="javascript:showDraft(1)"><emp:message key="dxzs_xtnrqf_title_16" defVal="草稿箱" fileName="dxzs"/></a>
								<br/>
							</div>
							<textarea name="msg" onblur="eblur($(this))" class="contents_textarea msg2" id="contents" ></textarea>
                          <div id="dtailcontents" class="tail-area div_bd">
                              <div class="tail-text">
                                	  <emp:message key="dxzs_xtnrqf_title_20" defVal="贴尾内容" fileName="dxzs"/>：<label class="tailcontents"></label>
                              </div>
                          </div>
						</div>

							<span>&nbsp;</span><font class="dxzs_font"><b id="strlen"> 0 </b><b id="maxLen">/980</b>&nbsp;<emp:message key="dxzs_xtnrqf_title_212" defVal="参数格式为：“{#参数1#}”(如：我和{#参数1#}去{#参数2#})" fileName="dxzs"/></font>
							</div>
							<div class="clear2"></div>
							<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>：</span>
						  <div class="dxzs_div">
						    <input type="radio" name="timerStatus" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="dxzs_xtnrqf_title_26" defVal="立即发送" fileName="dxzs"/>
							<input type="radio" name="timerStatus" value="1" class="dxzs_timerStatus"
								onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="dxzs_xtnrqf_title_27" defVal="定时发送" fileName="dxzs"/>
							<label id="time2">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
									id="timerTime" name="timerTime" value="">
							</label>
						  </div>
						</div>
							<div id="eq_sendDiv2">
							<span id="u_o_c_explain" class="div_bg dxzs_span">
							<a id="foldIcon" class="unfold"><emp:message key="dxzs_xtnrqf_title_28" defVal="高级设置" fileName="dxzs"/></a></span>
							<div id="moreSelect" class="div_bg">
							<div  align="right" class="dxzs_div2">
								<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="dxzs_xtnrqf_title_29" defVal="选项存为默认" fileName="dxzs"/></a>
							</div>
							<%-- 业务类型 --%>
							<table class="dxzs_table">
							<tr class="dxzs_tr">
								<td class="dxzs_td1">
									<emp:message key="dxzs_xtnrqf_title_30" defVal="业务类型" fileName="dxzs"/>：
								</td>
							<td>
							<select id="busCode"  name="busCode" >
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
							<tr class="dxzs_tr">
							<td class="dxzs_td2"><emp:message key="dxzs_xtnrqf_title_31" defVal="发送级别" fileName="dxzs"/>：</td>
							<td>
							<select id="priority" name="priority">
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
							<tr class="dxzs_tr">
							<td class="dxzs_td2"><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>：</td>
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
							<tr class="dxzs_tr">
							<td class="dxzs_td2"><emp:message key="dxzs_xtnrqf_title_35" defVal="回复设置" fileName="dxzs"/>：</td>
							<td>
							<input type="radio"  name="isReply" value="0" onclick="getGateNumber()" 
									<%if(isExistSubNo != 1){ %> checked="checked" <%} %> style="margin-left: 4px;" />&nbsp;<emp:message key="dxzs_xtnrqf_title_36" defVal="不用回复" fileName="dxzs"/>
									<input type="radio"  name="isReply" value="1" onclick="getGateNumber()" style="margin-left: 10px;" />&nbsp;<emp:message key="dxzs_xtnrqf_title_37" defVal="本次任务" fileName="dxzs"/>
									<input type="radio"  name="isReply" value="2" onclick="getGateNumber()" 
									<%if(isExistSubNo != 1){ %> disabled="disabled" <%}else{ %>
									checked="checked"<%} %>
									style="margin-left: 10px;"/><label <%if(isExistSubNo != 1){ %> style="color:gray;" <%}else{%>
									style="color:#000000;"
									 <%}%>>&nbsp;<emp:message key="dxzs_xtnrqf_title_38" defVal="我的尾号" fileName="dxzs"/></label><label style="margin-left: 20px;color:#000000;display: none" id="curSubNo"><emp:message key="dxzs_xtnrqf_title_39" defVal="当前尾号" fileName="dxzs"/>：</label>
									 <label id="subno" ></label>
									 <br/>
							</td>
							</tr>
							
							<tr class="dxzs_tr">
							<td class="dxzs_td2"><emp:message key="dxzs_xtnrqf_title_213" defVal="重号过滤" fileName="dxzs"/>：</td>
							<td>
							<input type="radio" name="checkrepeat" value="2"  checked="checked"  style="margin-left: 4px;" />&nbsp;<emp:message key="dxzs_xtnrqf_title_214" defVal="是" fileName="dxzs"/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="checkrepeat" value="1"   style="margin-left: 22px;" />&nbsp;<emp:message key="dxzs_xtnrqf_title_215" defVal="否" fileName="dxzs"/>
							<br/>
							</td>
							</tr>
							<% if("1".equals(confSmsSplit)){ %>
							<tr  class="dxzs_tr">
								<td class="dxzs_td2">分批设置：</td>
								<td>
									<input type="radio" id="splitFlagN" name="splitFlag" checked="checked" value="0" />
									<label for="splitFlagN">不分批</label>
									<input type="radio" id="splitFlagY" name="splitFlag" value="1" />
									<label for="splitFlagY">分批</label>
								</td>
							</tr>
							<%} %>
							</table>
							</div>
							</div>
							<div class="clear2"></div>
							<div class="b_F_btn">
								<input type="hidden" value="1" id="subState" name="subState" />
								<input id="toDraft" type="button" value="<emp:message key='dxzs_xtnrqf_button_1' defVal='暂存草稿' fileName='dxzs'/>" class="btnClass5 mr23"/>
								<input id="subSend" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_2' defVal='提交' fileName='dxzs'/>" />
								<input id="qingkong" onclick="javascript:reloadPage();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_3' defVal='重置' fileName='dxzs'/>" />
							</div>
							
							<div id="detail_Info">							  
								<table id="infos">
								  <tr >
								    <td class="infostd"><emp:message key="dxzs_xtnrqf_title_41" defVal="发送条数" fileName="dxzs"/>：<span id="yct"></span></td>								    
								    <td class="infostd" id="showyct"><emp:message key="dxzs_xtnrqf_title_42" defVal="机构余额" fileName="dxzs"/>：<span id="ct"></span></td>
								    <td class="infostd" id="showyspbalance"><emp:message key="dxzs_xtnrqf_title_43" defVal="SP账号余额" fileName="dxzs"/>：<span id="spbalance"></span></td>							    
								  </tr>
								  <tr>
									  <td colspan="2">
									    <label id="messages2"><font><emp:message key="dxzs_xtnrqf_title_216" defVal="余额不足不允许进行发送!" fileName="dxzs"/></font></label>
									  </td>
								  </tr>
								  <tr>
									  <td colspan="2">
									    <label id="messages1"><font><emp:message key="dxzs_xtnrqf_title_44" defVal="机构余额不足，不允许进行发送!" fileName="dxzs"/></font></label>
									  </td>
								  </tr>
								  <tr>
									  <td><emp:message key="dxzs_xtnrqf_title_45" defVal="有效号码数" fileName="dxzs"/>：<span id="effs"></span></td>
									  <td><emp:message key="dxzs_xtnrqf_title_46" defVal="提交号码数" fileName="dxzs"/>：<span id="counts"></span></td>								  
									  <td align="center">
									   <div id="showeffinfo" class="div_bg">
									       <p class="unfold"><emp:message key="dxzs_xtnrqf_title_47" defVal="无效号码" fileName="dxzs"/>(<span id="alleff" style="color: red;"></span>)&nbsp;&nbsp;
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
									      <td align="left"><emp:message key="dxzs_xtnrqf_title_48" defVal="黑名单号码：" fileName="dxzs"/>：</td>
										  <td><span id="blacks"></span></td>
									      <td id="predowninfo">
									      <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>	
									        <a href="javascript:uploadbadFiles()"><emp:message key="dxzs_xtnrqf_title_49" defVal="详情下载" fileName="dxzs"/></a><input type="hidden" id="badurl" name="badurl"  value=""></input>
									      <%} %>
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
								      <tr>
								          <td align="left"><emp:message key="dxzs_xtnrqf_title_217" defVal="含关键字号码" fileName="dxzs"/></td>
										  <td><span id="keyW"></span></td>
								          <td></td>
								      </tr>
								    </table>
								    </center>
							    </div>
							    <div class="dxzs_div3"></div>	
								<p class="dxzs_p">
									<emp:message key="dxzs_xtnrqf_title_52" defVal="部分预览" fileName="dxzs"/>：
								</p>
								<div id="maindiv">
								<center>
								<table id="content"></table>
								</center>
								</div>
						    	<div id="footer">
						    		<div id="tdSign"></div>
					    			<div id="configBatch"></div>
						    	  <center>
						    		<input id="btsend" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_6' defVal='发送' fileName='dxzs'/>" onclick="javascript:btsend();"/>
								    <input id="btcancel" onclick="javascript:btcancel1();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" />
								 	<br/>
								  </center> 
						    	</div>	
							</div>
						</form>
					</div>
					
					<div class="clear2"></div>		
					<div id="infomodel" class="infomodelclass">
                        <center>
                        <table class="dxzs_table1">
                          <tr>
                            <td class="dxzs_td3" colspan="2" align="left"><emp:message key="dxzs_xtnrqf_title_53" defVal="文件格式如图" fileName="dxzs"/>:</td>
                          </tr>
                          <tr>
                           <td valign="top" align="left"><emp:message key="dxzs_xtnrqf_title_54" defVal="txt格式" fileName="dxzs"/>：</td>
                           <td valign="top"><div id="txtstyle"></div></td>
                          </tr>
                          <tr height="12px;">
                          <td colspan="2"></td>
                          </tr>
                          <tr>
                           <td valign="top" align="left" width="55px;"><emp:message key="dxzs_xtnrqf_title_218" defVal="excel 格式" fileName="dxzs"/>：</td>
                           <td valign="top"><div id="xlsstyle"></div></td>
                          </tr>
                          <tr>
                          <td colspan="2" align="left" style="line-height:20px">
                           <span class="dxzs_span"><emp:message key="dxzs_xtnrqf_title_56" defVal="注意" fileName="dxzs"/>：</span><br/>
                           1.<emp:message key="dxzs_xtnrqf_title_219" defVal="txt格式分隔符号是英文半角“,”；" fileName="dxzs"/><br />
                           2.<emp:message key="dxzs_xtnrqf_title_220" defVal="文件需小于" fileName="dxzs"/><%=StaticValue.MAX_SIZE/1024/1024%><emp:message key="dxzs_xtnrqf_title_221" defVal="M，有效号码需少于" fileName="dxzs"/><%=StaticValue.MAX_PHONE_NUM%><br />
	                       3.<emp:message key="dxzs_xtnrqf_title_62" defVal="文件仅支持txt、zip、rar、xls、xlsx、csv、et格式，请优先使用zip格式文件。" fileName="dxzs"/>
                          </td>
                          </tr>
                        </table>
                       </center>
                    </div>

                     <div id="probar">
							<p >
								<emp:message key="dxzs_xtnrqf_title_63" defVal="处理中,请稍等" fileName="dxzs"/>.....
							</p>
							<div id="shows">
								<img src="<%=commonPath%>/common/img/loader.gif" />
							</div>
							<%-- <div style="margin-left: 20px;" id="processbar"></div> --%>
						</div>
			</div>
			<div id="tempDiv" title="<emp:message key='dxzs_xtnrqf_title_64' defVal='短信模板选择' fileName='dxzs'/>">
			<iframe id="tempFrame" name="tempFrame"  marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
				<div style="text-align: center">
					<input class="btnClass5 mr23" onclick="tempSure()" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>" type="button"/>
					<input class="btnClass6" onclick="tempCancel()" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" type="button"/>
					<br/>
				</div>
			</div>
			<div id="addSmsTmpDiv" title="<emp:message key='dxzs_xtnrqf_title_65' defVal='新建模板' fileName='dxzs'/>" style="padding:5px;display:none">
				<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
			</div>
			<div id="batchDetial" style="display:none">
				<div class="dxzs_div7">
					<input onclick="closeBatchDetail()" class="btnClass5 mr23" type="button" value="<emp:message key='dxzs_xtnrqf_button_8' defVal='确认' fileName='dxzs'/>">
				</div>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			
			<div id="draftDiv" title="<emp:message key='dxzs_xtnrqf_title_66' defVal='草稿箱选择' fileName='dxzs'/>">
                <iframe id="draftFrame" name="draftFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
                <div style="text-align: center">
                    <input class="btnClass5 mr23" onclick="draftSure()" value="<emp:message key='dxzs_xtnrqf_button_7' defVal='选择' fileName='dxzs'/>" type="button"/>
                    <input class="btnClass6" onclick="draftCancel()" value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" type="button"/>
                    <br/>
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
			<%-- foot结束 --%>
		</div>
		<div id="message" title="<emp:message key='dxzs_xtnrqf_title_73' defVal='提示' fileName='dxzs'/>">
				<div style="height:25px"></div>
		 		<center>
				<label ><emp:message key="dxzs_xtnrqf_title_71" defVal="发送至网关成功！" fileName="dxzs"/></label>
				<a id="sendRecord" href="javascript:sendRecord('<%=menuCode%>',<%=oldTaskId%>,<%=lguserid%>,<%=lgcorpcode%>)"  class="alink" ><emp:message key="dxzs_xtnrqf_title_74" defVal="查看发送记录" fileName="dxzs"/></a>
				</center>
				<br>
				<div style="height:10px"></div>
				 <center>
				    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="<emp:message key='dxzs_xtnrqf_button_10' defVal='关闭' fileName='dxzs'/>" />
				</center> 
		</div>
		<div class="clear2"></div>
		
		<%--<iframe id="ifr" class="ifr"></iframe>--%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script src="<%=iPath %>/js/dsm_batchFileSend.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=iPath %>/js/dsm_sendDiffSMS.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/jquery.selection.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
		<script>
			var userAgent = navigator.userAgent;
			var isChrome = 0;
			if (userAgent.indexOf("Chrome") > -1)
			{
	   			isChrome = 1;
	  		}
			function setSpUser()
			{
			    var isReply = $("input:radio[name='isReply']:checked").attr("value");
				$("#spUser").empty();
				var date = new Date();
				 var time = date.valueOf(); 
				if(isReply==1){
					$("#spUser").load("<%=path%>/mcs_sendClientSMS.htm?method=getSpUser&date="+time  );
				}else{
				<%
				if (accountBindsList != null && accountBindsList.size() > 0) {
					for (LfAccountBind accBind : accountBindsList) {%>
					$("#spUser").append('<option value="<%=accBind.getSpuserId()%>" > <%=accBind.getSpuserId()%></option>');
									<%}
				} else if (userList != null && userList.size() > 0) {
					for (LfSpDepBind userdata : userList) {%>
					$("#spUser").append('<option value="<%=userdata.getSpUser()%>"> <%=userdata.getSpUser()%></option>');
					<%}
				}%>
				}
			}
			
			$(document).ready(function(){
                $("#message").dialog("open");
				$('.para_cg').gotoParam({'width':498,'textarea':'#contents'});
				getLoginInfo("#loginparam");
				initContents();
				setFormName('form2');
				var result = base_result;

				if(result!="-1")
				{
					if(result.indexOf("empex") == 0)
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_1')+result.substr(5));
					}
					else if(result=="timerSuccess")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_2'));
						getCt();
					}
					else if(result=="timerFail")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_3'));
					}
					else if(result=="createSuccess")
					{ 
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_5'));
						getCt();
					}
					else if(result=="uploadFileFail")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_4'));
					}
					else if(result=="000")
					{
						//alert("创建短信任务及发送到网关成功！");
						getCt();
						<%session.removeAttribute("eq_diffResult");%>
                        $("#message").dialog(
                            {
                                width:300,
                                height:180,
                                //改变起始位置
                                position: { using:function(pos){
                                        var topOffset = $(this).css(pos).offset().top;
                                        if (topOffset === 0||topOffset>0) {
                                            $(this).css('top', 310);
                                        }
                                    }}
                            });
						$("#message").dialog("open");
					}
					else if(result=="saveSuccess")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_7'));
					}
					else if(result=="error")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_8'));
					}
					else if(result=="depfee:-2")
					{
					    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_11'));
					}
					else if(result=="depfee:-1")
					{
					    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_12'));
					}
					else if(result=="spuserfee:-2")
					{
				    	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_13'));
					}
					else if(result=="spuserfee:-1"||result=="spuserfee:-3")
					{
				    	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_14'));
					}
					else if(result=="timeError")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_6'));
					}
					else if(result=="nospnumber")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_9')+<%=StaticValue.SMSACCOUNT%>+getJsLocaleMessage('dxzs','dxzs_ssend_alert_10'));
					}
					else if(result == "subnoFailed")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_15'));
					}
					else if("nogwfee"==result || "feefail"==result || "feeerror"==result)
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_16'));
					}else if(result.indexOf("lessgwfee")==0)
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_17'));
					}
					else
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_18')+result);
					}
					<%session.removeAttribute("eq_diffResult");%>
				}
				<% 
				String priority = lfDfadvanced != null?lfDfadvanced.getPriority():"0";
				int isReply = lfDfadvanced != null?lfDfadvanced.getReplyset():-99;
				int checkrepeat = lfDfadvanced != null?lfDfadvanced.getRepeatfilter():2;
				%>
				$("#priority").val(<%=priority%>);
				$("input:radio[name='checkrepeat'][value="+<%=checkrepeat%>+"]").attr("checked",'checked');
				var isReply = <%=isReply%>;
				var isExistSubNo = <%=lfSysuser.getIsExistSubNo()%>;
				if(isReply != -99){
					if(isReply == 2 && isExistSubNo != 1){
						isReply = 0;
					}
					$("input:radio[name='isReply'][value="+<%=isReply%>+"]").attr("checked",'checked');
				}
				getGateNumber();
				$("#spUser,#busCode").bind("change", function(){getGateNumber();});
				$("#wtailcontents").hide();
			})
			
			//谷歌浏览器 
		if(isChrome == 1)
		{
			function onbeforeCloseIt()
		    {
				 if(isSend == "0")
				 {
					 	 var logInfo = getJsLocaleMessage('dxzs','dxzs_ssend_alert_303')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
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
					 	 var logInfo = getJsLocaleMessage('dxzs','dxzs_ssend_alert_303')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
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
