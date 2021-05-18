<%@page import="com.montnets.emp.shorturl.surlmanage.entity.LfDomain"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.Random"%>
<%@ page import="java.util.HashMap"%>
<%@page import="com.montnets.emp.entity.pasgrpbind.LfAccountBind"%>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.entity.LfDfadvanced"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.indexOf("/",1));

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
	String result = session.getAttribute("durl_diffResult")==null?"-1":
		(String)session.getAttribute("durl_diffResult");
	
	String isTrue = (String)request.getAttribute("isTrue");
	String sendType=(String)request.getAttribute("sendType");
	
	String findResult= (String)request.getAttribute("findresult");
	List<LfDomain> domainList = (List<LfDomain>)request.getAttribute("domainlist");
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
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>短信客服不同内容群发</title>
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
		<style>
		.bd1,.bd2{
		left:463px;}
		#batchFileSend span {
			width:75px;
		}

		#eq_sendDiv2 table tr td
		{
			font-size:12px;
		}
		#foldIcon
		{
			font-weight: 800;
			color: #000000;
			display:block;
			width:100px;
			background-position: 62px 5px;
			height:24px;
		}
		#u_o_c_explain a:hover
		{
			text-decoration: none;
		}
		#showeffinfo p
		{
			background-position: 90px 8px;
			width: 100px;
			height: 24px;
		}
        .x-fileUpload{
           /* margin-bottom: 0;*/
        }
        .tail-area{
            text-indent: 1em;
            display: block;
            display:block;
        }
        .tail-text{
            padding: 4px;
            line-height: 16px;
            background-color: #f8f8f8;
            word-break:break-all;
            color: #8a8a8a;
        }
        .tail-text-file{
            padding: 4px;
            line-height: 16px;
            background-color: #f8f8f8;
            word-break:break-all;
            color: #8a8a8a;
            width: 370px;
			display: inline-block;
			margin-left: -12px;
			border-right: 1px solid #c9d9f2;
        }
        .tail-text-draft{
            display: inline;
        }
        #toDraft{
                text-align: center;
                letter-spacing: 2px;
                text-indent: 0;
		}
		.tailContdiv {
		  width: 507px;
		  margin-bottom: 13px;
		  min-height: 25px;
		  _height: 128px;
		  overflow: hidden;
		  position: relative;
		   border-top-style: none;
		  margin-left: 80px; 
		    margin-top: -13px;
		}
		.draftButton {
		  width: 128px;
		  height: 100%;
		  min-height: 25px;
		  position: absolute;
		  right: 0;
		  top: 0;
		  background-color: #e9f0f9;
		    border-left: 1px solid #c9d9f2;
		}
		#tailContendDiv {
			min-height: 25px;
			text-indent: 1em;
			display:none;
		}
		#tailShowDiv {
			min-height: 25px;
			background-color: #f8f8f8;
			color: #8a8a8a;
			width:379px;
			line-height: 25px;
		}
        .tailcontents{
            word-break: break-all;
        }
		</style>
	</head>
	<body>
	    <input type="hidden" name="checkCount" id="checkCount" value="0"/>
		<input type="hidden" id="pathUrl" value="<%=request.getContextPath() %>" />
        <input type="hidden" id="skin" value="<%=skin %>" />
		<div id="container" class="container" >
			<%-- 当前位置 --%>
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(menuCode) %>
			
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" style="position：relative">
				<div class="clear"></div>
				<input type="hidden" name="htmName" id="htmName" value="dsm_<%=reTitle %>.htm" />
				<div id="batchFileSend"  >
					<form  name="form2" id="form2"
						action="urld_previewSMS.htm?method=preview" method="post"
						enctype="multipart/form-data" target="hidden_iframe">
						<iframe name="hidden_iframe" id="hidden_iframe"
							style="display: none"></iframe>
						<div class="hidden" id="loginparam"></div>	
							<input type="hidden" value="2" id="bmtType" name="bmtType" />
							<input type="hidden" value="0" id="isOk" name="isOk" />
							<input type="hidden" value="" id="dtMsg" name="dtMsg" />
							
							<input type="hidden" value="" id="preStr" name="preStr" />
							
							<input type="hidden" value="" id="hidSubCount" name="hidSubCount" />
							<input type="hidden" value="" id="hidEffCount" name="hidEffCount" />
							<input type="hidden" value="" id="hidMobileUrl" name="hidMobileUrl" />
							<input type="hidden" value="" id="hidDzUrl" name="hidDzUrl" /><%-- 短链接地址 --%>
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
	               			 
	               			 <div style="display: none;" id="allfilename" ></div>
							 <%-- <div id="dtsend" onclick="javascript:bycheck(1)" style="float: left;cursor: pointer;" class="dtsendclass div_bg">
							     <div style="height: 28px;text-align: center;line-height: 28px;padding-top:5px;">
							     <font class="send_ac1">动态模板发送</font>
							     </div>
							 </div>
                            <div id="wjsend" onclick="javascript:bycheck(2)" style="float: right;cursor: pointer;" class="wjsendclss div_bg">
                                 <div style="height: 28px;text-align: center;line-height: 28px;padding-top:5px;">
                                 <font class="send_ac2">文件内容发送</font>
                                 </div>
                            </div> 
                             --%>
                            <%--动态模板中间的div --%> 
                            <div id="showdtsend"> 	
                          <span  class="righttitle">发送主题：</span>
							<input id="taskname" name="taskname" type="text" class="graytext input_bd div_bd" style="padding-left:5px;width:503px;" maxlength="20" value="不作为短信内容发送"/>
							</div>
						<div class="clear2"></div>
						<div id="showdtsend"  > 
						 <span  class="righttitle">文件导入：  
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
						 							  
						 		<div class="x-fileUpload div_bd">
									<div class="div_bd div_bg x-uploadButton"  id="downlinks" style="cursor: default;">
										<a href="javascript:void(0)" class="x-fileBtn">
											上传文件
										</a>
										<input type="file" name="numFile1" id="numFile1" onchange="ch()" class="x-numfileclass" >
									</div>
									<div class="x-fileList">
										<ul id="upfilediv">
											
										</ul>
									</div>
									
								</div>
						
								<div id="wtailcontents" class="tailContdiv div_bd">
									<div class="draftButton" style="cursor: default;">
										<a href="javascript:insertUrl()" style="margin-left: 10px">插入短链接</a>
										<a href="javascript:showDraft(2)" style="margin-left: 10px;line-height:25px;">草稿箱</a>
										
									</div>
									<div id="tailContendDiv">
										<div id="tailShowDiv">
										贴尾内容：<label class="tailcontents"></label>
										</div>
									</div>
									<input id="netUrl" name="netUrl" type="hidden" value=""/>
							<input id="netUrlId" name="netUrlId" type="hidden"value=""/>
							<input id="vaildays" name="vaildays" type="hidden"value=""/>
							<input id="domainId" name="domainId" type="hidden"value=""/>
								</div>
                            
                              </div>
							<div id="showmessage" style="margin-bottom:0px"> 
							<span class="righttitle">发送内容：</span>
						  <div class="paraContent div_bd">
							<div class="tit_panel div_bd">
								<a href="javascript:void(0)" class="para_cg" >关闭参数</a>
								<a href="javascript:insertUrl()" style="margin-right: 10px;float: right">插入短链接</a>
								<%if(btnMap.get(menuCode+"-4") != null){%>
								<a id="ctem" class="mr10" href="javascript:chooseTemp()">选择模板</a>
								<a id="qtem" class="mr10" style="display:none" href="javascript:tempNo()">取消模板</a>
								<%} %>
								<%if(btnMap.get(templateMenuCode+"-1" )!= null && isHaveTemp){%>	
								
								<a href="javascript:showAddSmsTmp(0)">新建模板</a>
								<%} %>
								
								<a href="javascript:showDraft(1)" style="margin-left: 10px">草稿箱</a>
								<br/>
							</div>
							<textarea name="msg" onblur="eblur($(this))" class="contents_textarea msg2" id="contents" ></textarea>
                          <div id="dtailcontents" class="tail-area div_bd" style="border-left: none;border-right: none;border-bottom: none">
                              <div class="tail-text">
                                	  贴尾内容：<label class="tailcontents"></label>
                              </div>
                          </div>
						</div>

							<span>&nbsp;</span><font style="float:left;cursor:pointer;padding-top:5px;margin-left:5px;"><b id="strlen"> 0 </b><b id="maxLen">/990</b>&nbsp;参数格式为：“{#参数1#}”(如：我和{#参数1#}去{#参数2#})</font> 
							</div>
							<div class="clear2"></div>
							<input id="srcUrl" name="srcUrl" type="text" class="graytext input_bd div_bd" style="display: none;" value=""/>
							<input id="srcId" name="srcId" type="text" class="graytext input_bd div_bd" style="display: none;" value=""/>
						<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span>发送时间：</span>
						  <div style="padding:3px 5px 0; width: 493px;line-height: 26px;">
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
							<div id="eq_sendDiv2">
							<span id="u_o_c_explain" class="div_bg">
							<a id="foldIcon" class="unfold">高级设置</a></span>
							<div id="moreSelect" class="div_bg">
							<div style="width:580px" align="right">
								<a id="setdefualt" href="javascript:setDefault()"  class="alink" >选项存为默认</a>
							</div>
							<%-- 业务类型 --%>
							<table style="width:100%;height:100%;border:0px">
							<tr style="height:30px;;"><td style="width:108px;"><font style="padding-left:35px">业务类型：</font></td>
							<td>
							<select id="busCode"  name="busCode" style="width:225px">
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
							<tr  style="height:30px;">
							<td><font style="padding-left:35px">发送级别：</font></td>
							<td>
							<select id="priority" name="priority" style="width:225px">
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
							<tr  style="height:30px;">
							<td><font style="padding-left:35px"><%=StaticValue.SMSACCOUNT%>：</font></td>
							<td>
							<select id="spUser" name="spUser" style="width:225px">
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
							<tr  style="height:30px;">
							<td><font style="padding-left:35px">回复设置：</font></td>
							<td>
							<input type="radio"  name="isReply" value="0" onclick="getGateNumber()" 
									<%if(isExistSubNo != 1){ %> checked="checked" <%} %> style="margin-left: 4px;" />&nbsp;不用回复
									<input type="radio"  name="isReply" value="1" onclick="getGateNumber()" style="margin-left: 10px;" />&nbsp;本次任务
									<input type="radio"  name="isReply" value="2" onclick="getGateNumber()" 
									<%if(isExistSubNo != 1){ %> disabled="disabled" <%}else{ %>
									checked="checked"<%} %>
									style="margin-left: 10px;"/><label <%if(isExistSubNo != 1){ %> style="color:gray;" <%}else{%>
									style="color:#000000;"
									 <%}%>>&nbsp;我的尾号</label><label style="margin-left: 20px;color:#000000;display: none" id="curSubNo">当前尾号：</label>
									 <label id="subno" ></label>
									 <br/>
							</td>
							</tr>
							
							<tr  style="height:30px;">
							<td><font style="padding-left:35px">重号过滤：</font></td>
							<td>
							<input type="radio" name="checkrepeat" value="2"  checked="checked"  style="margin-left: 4px;" />&nbsp;是
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="checkrepeat" value="1"   style="margin-left: 22px;" />&nbsp;否
							<br/>
							</td>
							</tr>
							</table>
							</div>
							</div>
							<div class="clear2"></div>
							<div class="b_F_btn">
								<input type="hidden" value="1" id="subState" name="subState" />
								<input id="toDraft" type="button" value="暂存草稿" class="btnClass5 mr23"/>
								<input id="subSend" class="btnClass5 mr23" type="button" value="提交" />
								<input id="qingkong" onclick="javascript:reloadPage();" class="btnClass6" type="button" value="重置" />
							</div>
							
							<div id="detail_Info" style="width: 100%; padding: 5px; display: none;">							  
								<table id="infos" width="98%">
								  <tr >
								    <td class="infostd">发送条数：<span id="yct"></span></td>								    
								    <td class="infostd" id="showyct">机构余额：<span id="ct"></span></td>
								    <td class="infostd" id="showyspbalance">SP账号余额：<span id="spbalance"></span></td>							    
								  </tr>
								  <tr>
									  <td colspan="2">
									    <label id="messages2"><font style="color: red;">余额不足不允许进行发送!</font></label>
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
									       <p style="font-size: 12px;" class="unfold">无效号码(<span id="alleff" style="color: red;"></span>)&nbsp;&nbsp;
									   </p> 
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
									      <td id="predowninfo">
									      <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>	
									        <a href="javascript:uploadbadFiles()">详情下载</a><input type="hidden" id="badurl" name="badurl"  value=""></input>
									      <%} %>
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
								      <tr>
								          <td align="left">含关键字号码：<span id="keyW"></span></td>
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
						    		<input id="btsend" class="btnClass5 mr23" type="button" value="发送" onclick="javascript:btsend();"/>
								    <input id="btcancel" onclick="javascript:btcancel1();" class="btnClass6" type="button" value="取消" />
								 	<br/>
								  </center> 
						    	</div>	
							</div>
						</form>
					</div>
					
					<div class="clear2"></div>		
					<div id="infomodel" class="infomodelclass" style="padding-left:20px;padding-right:10px;display: none;position: absolute;z-index: 5000;border: 1px solid #dae1cf;background-color: #f7fff0;width: 250px;height: 290px;padding-bottom: 8px;">
                        <center>
                        <table style="line-height: 30px;">
                          <tr>
                            <td colspan="2" style="font-weight: bold;font-size: 14px;" align="left">文件格式如图:</td>
                          </tr>
                          <tr>
                           <td valign="top" align="left">txt格式：</td>
                           <td valign="top"><div id="txtstyle" style="float:left;width: 155px;height: 69px;background : url('<%=commonPath %>/common/img/dtsend.png') no-repeat; "></div></td>
                          </tr>
                          <tr height="12px;">
                          <td colspan="2"></td>
                          </tr>
                          <tr>
                           <td valign="top" align="left" width="55px;">xls,xlsx,et格式：</td>
                           <td valign="top"><div id="xlsstyle" style="float:left;width: 165px;height: 85px;background: url('<%=commonPath %>/common/img/dtsend.png') no-repeat 0px -80px;"></div></td>
                          </tr>
                          <tr>
                          <td colspan="2" align="left" style="line-height:20px">
                           <span style="font-weight: bold;font-size: 14px;">注意：</span><br/>
                           1.txt格式分隔符号是英文半角“,”；<br />
                           2.文件需小于<%=100%>M，有效号码需少于<%=100%>万；<br />
	                       3.文件仅支持txt、zip、rar、xls、xlsx、csv、et格式，请优先使用zip格式文件。
                          </td>
                          </tr>
                        </table>
                       </center>
                    </div>

                     <div id="probar" style="display: none;text-align: center;">
							<p >
								处理中,
								请稍等.....
							</p>
							<div id="shows">
								<img src="<%=commonPath%>/common/img/loader.gif" />
							</div>
							<%-- <div style="margin-left: 20px;" id="processbar"></div> --%>
						</div>
			</div>
			<div id="tempDiv" title="短信模板选择" style="padding:5px;display:none">
			<iframe id="tempFrame" name="tempFrame" <%=skin.contains("frame4.0")?"style=\"width:100%;min-height:460px;border: 0;\"":"style=\"width:610px;height:370px;border: 0;\""%> marginwidth="0" scrolling="auto" frameborder="no" src ="" ></iframe>
				<div style="text-align: center">
					<input class="btnClass5 mr23" onclick="tempSure()" value="选择" type="button"/>
					<input class="btnClass6" onclick="tempCancel()" value="取消" type="button"/>
					<br/>
				</div>
			</div>
			<div id="shortUrlDiv" title="选择短链接" style="background-color:#FFFFFF;padding:5px;display:none;height: 510px;">
			<iframe id="shortUrlFrame" <%=skin.contains("frame4.0")?"style=\"width:100%;min-height:490px;border: 0;\"":"style=\"width:800px;height:500px;border: 0;\""%> name="shortUrlFrame" marginwidth="0" scrolling="auto" frameborder="no" src ="" ></iframe>
				<%-- <div style="text-align: center">
					<input class="btnClass5 mr23" onclick="urlSure()" value="选择" type="button"/>
					<input class="btnClass6" onclick="urlCancel()" value="取消" type="button"/>
					<br/>
				</div> --%>
			</div>
			<div id="addSmsTmpDiv" title="新建模板" style="padding:5px;display:none">
				<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" style="display:none;width:630px;height:480px;border: 0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			
			<div id="draftDiv" title="草稿箱选择" style="padding:5px;display:none">
                <iframe id="draftFrame" name="draftFrame" <%=skin.contains("frame4.0")?"style=\"width:100%;min-height:400px;border: 0;\"":"style=\"width:880px;height:400px;border: 0;\""%> marginwidth="0" scrolling="auto" frameborder="no" src ="" ></iframe>
                <div style="text-align: center">
                    <input class="btnClass5 mr23" onclick="draftSure()" value="选择" type="button"/>
                    <input class="btnClass6" onclick="draftCancel()" value="取消" type="button"/>
                    <br/>
                </div>
            </div>
            <div id="urllist" style="display:none;">
				<%if(domainList!=null &&!domainList.isEmpty()){
				for (LfDomain lfDomain : domainList) {
				out.print("<input type=\"hidden\" value=\""+lfDomain.getDomain()+"\" />");
				}
				} %>
				</div>
			<%-- foot结束 --%>
		</div>
		<div id="message" title="提示" style="padding:5px;display:none;font-size: 12px;">
				<div style="height:25px"></div>
		 		<center>
				<label >短链发送提交成功！</label>
				<a id="sendRecord" href="javascript:sendRecord('5600-1000',<%=oldTaskId%>,<%=lguserid%>,<%=lgcorpcode%>)"  class="alink" >查看发送记录</a>
				</center>
				<br>
				<div style="height:10px"></div>
				 <center>
				    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="关闭" />
				</center> 
		</div>
		<div class="clear2"></div>
		<iframe id="ifr" class="ifr"></iframe>
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
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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
				$('.para_cg').gotoParam({'width':498,'textarea':'#contents'});
				getLoginInfo("#loginparam");
				initContents();
				setFormName('form2');
				var result = base_result;

				if(result!="-1")
				{
					if(result.indexOf("empex") == 0)
					{
						alert("任务创建失败："+result.substr(5));
					}
					else if(result=="timerSuccess")
					{
						alert("创建短信任务及定时任务添加成功！");
						getCt();
					}
					else if(result=="timerFail")
					{
						alert("创建定时任务失败，取消任务创建！");
					}
					else if(result=="createSuccess")
					{ 
						alert("创建短信任务及提交到审批流成功！");
						getCt();
					}
					else if(result=="uploadFileFail")
					{
						alert("上传号码文件失败，取消任务创建！");
					}
					else if(result=="000")
					{
						//alert("创建短信任务及发送到网关成功！");
						getCt();
						<%session.removeAttribute("drul_diffResult");%>
						$("#message").dialog({width:300,height:180});
						$("#message").dialog("open");
					}
					else if(result=="saveSuccess")
					{
						alert("存草稿成功！");
					}
					else if(result=="error")
					{
						alert("请求响应超时，创建短链任务失败！");
					}
					else if(result=="depfee:-2")
					{
					    alert("机构余额不足,创建短信任务失败！");
					}
					else if(result=="depfee:-1")
					{
					    alert("创建短信任务时,修改计费信息失败！");
					}
					else if(result=="spuserfee:-2")
					{
				    	alert("SP账号余额不足,创建短信任务失败！");
					}
					else if(result=="spuserfee:-1"||result=="spuserfee:-3")
					{
				    	alert("创建短信任务时,检查SP账号费用失败！");
					}
					else if(result=="timeError")
					{
						alert("发送时间已经超过定时时间，不能发送！");
					}
					else if(result=="nospnumber")
					{
						alert("发送失败，"+'<%=StaticValue.SMSACCOUNT%>'+"未设置尾号！");
					}
					else if(result == "subnoFailed")
					{
						alert("拓展尾号处理失败！");
					}
					else if("nogwfee"==result || "feefail"==result || "feeerror"==result)
					{
						alert("获取运营商余额失败，取消任务创建！");
					}else if(result.indexOf("lessgwfee")==0)
					{
						alert("运营商余额不足，取消任务创建！");
					} else if(result == "waitingResult") {
                        alert("任务已创建，请等待处理结果");
                    } else {
						alert("向网关发送请求失败:"+result);
					}
					<%session.removeAttribute("durl_diffResult");%>
				}
				<% 
				String priority = lfDfadvanced != null?lfDfadvanced.getPriority():"0";
				int isReply = lfDfadvanced != null?lfDfadvanced.getReplyset():-99;
				int checkrepeat = lfDfadvanced != null?lfDfadvanced.getRepeatfilter():2;
				%>
				$("#priority").val(<%=priority%>);
				$("input:radio[name='checkrepeat'][value="+<%=checkrepeat%>+"]").attr("checked",'checked');
				var isReply = <%=isReply%>;
				if(isReply != -99){
					if(isReply == 2 && isExistSubNo != 1){
						isReply = 0;
					}
					$("input:radio[name='isReply'][value="+<%=isReply%>+"]").attr("checked",'checked');
				}
				getGateNumber();
				$("#spUser,#busCode").bind("change", function(){getGateNumber();});
				$("#wtailcontents").hide();
				
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
					 	 var logInfo = '不同内容群发，界面刷新或关闭浏览器。userId:'+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
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
					 	 var logInfo = '不同内容群发，界面刷新或关闭浏览器。userId:'+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
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
