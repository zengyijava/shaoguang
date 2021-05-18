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
<%@page import="com.montnets.emp.netnews.entity.LfWXBASEINFO"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.netnews.entity.LfDfadvanced"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
	String isFlow = (String)request.getAttribute("isFlow");
	String result = session.getAttribute("eq_diffResult")==null?"-1":
		(String)session.getAttribute("eq_diffResult");
	String isTrue = (String)request.getAttribute("isTrue");
	String sendType=(String)request.getAttribute("sendType");
	String biaoshi=(String)request.getAttribute("biaoShi");
	String moban=(String)request.getAttribute("template");
	String list = (String)request.getAttribute("sb");
	String findResult= (String)request.getAttribute("findresult");
	//上传返回信息
    String message=(String)request.getAttribute("message");
	//动态短信模板
	@SuppressWarnings("unchecked")
    List<LfTemplate> tmpList = (List<LfTemplate>)request.getAttribute("tmpList");//获取短信模板
    String taskId=(String)request.getAttribute("taskId");
	@SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>) request.getAttribute("spUserList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	Integer isExistSubNo = 0;
	if(request.getAttribute("isExistSubNo") != null)
	{
		isExistSubNo = (Integer)request.getAttribute("isExistSubNo");
	}
	    //为了获得网讯新增的权限
    String menuOther= titleMap.get("manger"); 
    String lgcorpcode = request.getParameter("lgcorpcode");
    String lguserid = request.getParameter("lguserid");
    
        //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
    String langName = (String)session.getAttribute("emp_lang");
  
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="ydwx_wxfs_wxdtfs" defVal="网讯动态发送" fileName="ydwx"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
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
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydwx/sendWXDSM/css/senddsm.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydwx/sendWXDSM/css/senddsmzh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/wx_send.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
		<script>
				var result = "<%=result%>";
				var flag = "<%=isTrue%>";
				var sendType="<%=sendType%>";
				var biaoshi="<%=biaoshi%>";
				var moban="<%=moban%>";
			    var message="<%=message%>";
			    var findresult="<%=findResult%>";
			    var path="<%=path%>";
			    var smsaccount=getJsLocaleMessage("ydwx","ydwx_common_SPzhanghao");
				var inheritPath="<%=inheritPath %>";
				var skin="<%=skin %>";
				var commonPath="<%=commonPath %>";
				<%
				int checkrepeat = lfDfadvanced != null?lfDfadvanced.getRepeatfilter():2;
				%>
				var checkrepeat = <%= checkrepeat%>;
				//最大值限制
				var ZIP_SIZE = "<%=StaticValue.ZIP_SIZE/1024/1024%>";
		   		var MAX_SIZE = "<%=StaticValue.MAX_SIZE/1024/1024%>";
		    	var MAX_PHONE_NUM = "<%=StaticValue.MAX_PHONE_NUM/10000%>";
		</script>
	</head>
	<body id="ydwx_senddsm">
	    <input type="hidden" name="checkCount" id="checkCount" value="0"/>
		<input type="hidden" id="pathUrl" value="<%=request.getContextPath() %>" />
		<div id="container" class="container" >
		<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<div class="clear"></div>
				<input type="hidden" id="empLangName" value="<%=empLangName%>" />
				<input type="hidden" name="htmName" id="htmName" value="wx_senddsm.htm" />
				<div id="batchFileSend"  >
					<form  name="form2" id="form2"
						action="wx_senddsm.htm?method=preview" method="post"
						enctype="multipart/form-data" target="hidden_iframe">
						<iframe name="hidden_iframe" id="hidden_iframe"
							style="display: none"></iframe>
						<div class="hidden" id="loginparam"></div>	
							<%--网讯发送成功的任务ID--%>
							<input type="hidden" value="" id="oldTaskId" name="oldTaskId" />
							<input type="hidden" value="2" id="bmtType" name="bmtType" />
							<input type="hidden" value="0" id="isOk" name="isOk" />
							<input type="hidden" value="" id="dtMsg" name="dtMsg" />
							<input type="hidden" value="" id="preStr" name="preStr" />
							<input type="hidden" value="" id="error" name="error">							
							<input type="hidden" value="wx_senddsm.htm" name="reTitle" id="reTitle"/>
							<input type="hidden" value="0" id="SendReq" name="SendReq">							
							<input type="hidden" value="" id="inputContent" name="inputContent" /><%--临时存储手工输入内容 --%>
							<input type="hidden" value="" id="busCodeHidden" name="busCodeHidden" /><%--业务类型的值 --%>
							<input type="hidden" value="" id="spuserHidden" name="spuserHidden" /><%--sp账号的值 --%>
							<input type="hidden" value="" id="sendtypeHidden" name="sendtypeHidden" /><%--发送类型的值 --%>
							<input type="hidden" value="" id="netId" name="netId" /><%--网讯内容 --%>
	               			 <input type="hidden" value ="<%=taskId %>" id="taskId" name="taskId"/>
							 <input id="formName" value="form2" type="hidden" />
							<input id="cpath" value="<%=path %>" type="hidden" />
							<input id="path" value="<%=inheritPath %>" type="hidden" />
	               			 <input type="hidden" value="2" id="sendType" name="sendType">
	               			 <input type="hidden" value="" id="isCharg" name="isCharg"/>
	               			 <div style="display: none;" id="allfilename" ></div>
                            <div style="display: none;" id="alltempname" ></div>
                            <input type="hidden" value="" id="smsTail" name="smsTail" value="">	<%-- 隐藏的贴尾内容，用于后台拼装--%>
                            <input id="cpath" value="<%=path %>" type="hidden" />
	                        <%--草稿箱--%>
		                    <input type="hidden" id="draftFile" name="draftFile" value=""/>
		                    <input type="hidden" id="draftId" name="draftId" value=""/>
                            <div></div>
                            <%--动态模板中间的div --%> 
                            <div id="showdtsend"> 	
								<span  class="righttitle"><emp:message key="ydwx_wxfs_dtwxfs_fszt" defVal="发送主题：" fileName="ydwx"></emp:message></span>
								<input id="taskname" name="taskname" type="text" class="graytext input_bd div_bd" class="ydwx_taskname" style="padding-left:5px;width:<%="zh_HK".equals(empLangName)?620:503%>px;float:left;" maxlength="20" value="<emp:message key="ydwx_wxfs_jtwxfs_wxzt_value_1" defVal="不作为短信内容发送" fileName="ydwx"></emp:message>"/>
							</div>
						<div class="clear2"></div>

							<div id="showmessage" class="ydwx_showmessage"> 
							<span class="righttitle"><emp:message key="ydwx_wxfs_jtwxfs_dxnr" defVal="短信内容：" fileName="ydwx"></emp:message></span>
							<div class="paraContent div_bd" >
								<div class="tit_panel div_bd">
									<a href="javascript:void(0)" class="para_cg"><emp:message key="ydwx_wxfs_dtwxfs_gbcs" defVal="关闭参数" fileName="ydwx"></emp:message></a>
									<a id="ctem" class="mr10" href="javascript:chooseTemp()"><emp:message key="ydwx_wxfs_dtwxfs_xzmb" defVal="选择模板" fileName="ydwx"></emp:message></a>
									<a id="qtem" class="mr10" style="display:none" href="javascript:tempNoShow()"><emp:message key="ydwx_wxfs_dtwxfs_qxmb" defVal="取消模板" fileName="ydwx"></emp:message></a>
									<a onclick="showDraft()" class="ydwx_drafts"><emp:message key="ydwx_wxfs_jtwxfs_dxnr_td_3" defVal="草稿箱" fileName="ydwx"></emp:message></a>
								</div>
								<textarea name="msg" onblur="eblur($(this))" class="contents_textarea msg2" id="contents" ></textarea>
							</div>
								<span class="righttitle" id="wx_lefttitle"></span>
								<div class="tit_panel div_bd" id="wx_righttitle">
								&nbsp;&nbsp;&nbsp;&nbsp;<font class="ydwx_righttitle_font"><emp:message key="ydwx_wxfs_twnr" defVal="贴尾内容：" fileName="ydwx"></emp:message></font><font id="smsTailContens" class="ydwx_smsTailContens" ></font>
								</div>
							<div class="clear"></div>		
							<span>&nbsp;</span>
							
							<font class="ydwx_areacount"><b id="strlen"> 0 </b><b id="maxLen">/900</b>&nbsp;&nbsp;
							<emp:message key="ydwx_wxfs_dtwxfs_csgs" defVal="参数格式为：“{#参数1#}”(如：我和{#参数1#}去{#参数2#})" fileName="ydwx"></emp:message></font> 
							</div>
							<div class="clear"></div>
						
							<div id="eq_sendDiv" class="ydwx_eq_sendDiv">
							<span class="righttitle"><emp:message key="ydwx_wxfs_jtwxfs_wxnr" defVal="网讯内容：" fileName="ydwx"></emp:message></span>
							<p class="ydwx_eq_sendDiv_p">
							<%--<input type="button" class="btnClass4" value="<emp:message key="ydwx_wxfs_jtwxfs_wxnr_value_1" defVal="选择静态网讯" fileName="ydwx"></emp:message>" onclick="staticTemp()"/>--%>
							<input type="button" class="btnClass4 ydwx_btnClass4" value="<emp:message key="ydwx_wxfs_dtwxfs_xzdtwx" defVal="选择动态网讯" fileName="ydwx"></emp:message>" onclick="dynamicTemp()"/>
							<%-- 增加了权限控制    may add --%>
							<%if(btnMap.get(menuOther+"-1")!=null){%>
							 <a class="ydwx_toAdd" onclick="javascript:toAdd()"><emp:message key="ydwx_wxfs_jtwxfs_wxnr_a" defVal="新增网讯" fileName="ydwx"></emp:message></a>
							 <%} %>
							</p>	
							<div class="clear2"></div>
							<div style="padding-left:<%="zh_HK".equals(empLangName)?105:78%>px;float:left">
							  <div id="tempupfilediv" class="div_bg ydwx_tempupfilediv"></div>
						  	</div>
							</div>	

							<div id="showdtsend"  > 
						 <span  class="righttitle"><emp:message key="ydwx_wxfs_dtwxfs_wjdr" defVal="文件导入：" fileName="ydwx"></emp:message>  
						 	<input id='sss' name='sss'  type="hidden"  class="numfileclass2" value=""/>		
						 </span>
						  <div id="filesdiv" class="ydwx_filesdiv">
						  <a id="downlinks" class="ydwx_downlinks"><emp:message key="ydwx_wxfs_jtwxfs_gstx" defVal="格式提示" fileName="ydwx"></emp:message></a>
						  <a id="afile" name="afile" class="afileclass1" ><emp:message key="ydwx_wxfs_dtwxfs_schwj" defVal="上传文件" fileName="ydwx"></emp:message></a>
						  <input id='numFile1' name='numFile1'  type=file onchange="ch();" class="numfileclass2" value=""/>
						  <font class="zhu">&nbsp;&nbsp;(<emp:message key="ydwx_wxfs_dtwxfs_zuida" defVal="最大" fileName="ydwx"></emp:message><%=StaticValue.MAX_SIZE/1024/1024 %></>M)</font>
						 							  
						  </div>
						  <div style="padding-left:<%="zh_HK".equals(empLangName)?0:78%>px;float:left">
						  	<div id="upfilediv" class="div_bg ydwx_upfilediv"></div>
						  </div>
						 	<%-- <div class="x-fileUpload div_bd">
								<div class="div_bd div_bg x-uploadButton"  id="downlinks">
									<a href="javascript:void(0)" class="x-fileBtn">
										上传文件
									</a>
									<input type="file" name="numFile1" id="numFile1" onchange="ch()" class="x-numfileclass" >
								</div>
								<div class="x-fileList">
									<ul id="upfilediv">
										
									</ul>
								</div>
								
							</div> --%>
                          </div>
                              <div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="ydwx_wxfs_jtwxfs_fssj" defVal="发送时间：" fileName="ydwx"></emp:message></span>
						  <div class="ydwx_senttm_dv">
						    <input type="radio" name="timerStatus" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="ydwx_wxfs_jtwxfs_fssj_td_1" defVal="立即发送" fileName="ydwx"></emp:message>
							<input type="radio"  name="timerStatus" value="1" class="ydwx_timerStatus"
								onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="ydwx_wxfs_jtwxfs_fssj_td_2" defVal="定时发送" fileName="ydwx"></emp:message>
							<label id="time2" style="display: none;">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
									id="timerTime" name="timerTime" value="">
							</label>
						  </div>
						</div>
							<div id="eq_sendDiv2">
							<span id="u_o_c_explain" class="div_bg">
								<b><emp:message key="ydwx_wxfs_jtwxfs_gjsz" defVal="高级设置" fileName="ydwx"></emp:message></b>&nbsp;&nbsp;&nbsp;&nbsp;
								<a id="foldIcon" style="text-decoration:none" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a>
							</span>
							<div id="moreSelect" class="div_bg ydwx_moreSelect">
							<div class="ydwx_moreSelect_sub_dv" align="right">
								<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="ydwx_wxfs_jtwxfs_gjsz_td_1" defVal="选项存为默认" fileName="ydwx"></emp:message></a>
							</div>
							<%-- 业务类型 --%>
							<div id="selfSelect">
							<table class="ydwx_tabl">
								<tr class="ydwx_tr1"><td style="width:<%="zh_HK".equals(empLangName)?180:108%>px;"><font class="ydwx_seviceType_font"><emp:message key="ydwx_wxfs_jtwxfs_gjsz_td_2" defVal="业务类型：" fileName="ydwx"></emp:message></font></td>
									<td>
									<select id="busCode"  class="input_bd ydwx_busCode"  name="busCode" isInput="false">
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
								<tr class="ydwx_tr2">
									<td><font class="ydwx_spuser_font"><emp:message key="ydwx_common_SPzhanghaos" defVal="SP账号：" fileName="ydwx"></emp:message></font></td>
									<td>
									<select id="spUser" name="spUser" class="ydwx_spUser" isInput="false">
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
	
								<tr class="ydwx_tr3">
								<td><font class="ydwx_filter_repeatnum"><emp:message key="ydwx_wxfs_dtwxfs_chgl" defVal="重号过滤：" fileName="ydwx"></emp:message></font></td>
								<td>
								<input type="radio" name="checkrepeat" value="1"  checked="checked" class="ydwx_filter_no" />&nbsp;<emp:message key="ydwx_common_fou" defVal="否" fileName="ydwx"></emp:message>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio" name="checkrepeat" value="2" class="ydwx_filter_yes"/>&nbsp;<emp:message key="ydwx_common_shi" defVal="是" fileName="ydwx"></emp:message>
								</td>
								</tr>
							</table>
							</div>
							</div>
							</div>
							<div class="clear2"></div>
							<div class="b_F_btn">
								<input type="hidden" value="1" id="subState" name="subState" />
							<% 
							if(btnMap.get(menuCode+"-1")!=null){
							%>
								<input id="toDraft" class="btnClass5 mr23 ydwx_borderradius"  type="button" value="<emp:message key='ydwx_common_btn_zccg' defVal='暂存草稿' fileName='ydwx'></emp:message>"/>
								<input id="subSend" class="btnClass5 mr23 ydwx_borderradius" type="button" value="<emp:message key='ydwx_common_btn_tijiao' defVal='提交' fileName='ydwx'></emp:message>" />
								<input id="qingkong" onclick="javascript:reloadPage();" class="btnClass6 ydwx_borderradius" type="button" value="<emp:message key='ydwx_common_btn_chongzhi' defVal='重置' fileName='ydwx'></emp:message>" />
							<% 
							}
							%>
							</div>
							
							<div id="detail_Info" class="ydwx_detail_dv">							  
								<table id="infos" width="98%">
								  <tr >
								    <td class="infostd"><emp:message key='ydwx_wxfs_fsts' defVal='发送条数：' fileName='ydwx'></emp:message><span id="yct"></span></td>								    
								    <td class="infostd" id="showyct"><emp:message key='ydwx_wxfs_jgyue' defVal='机构余额：' fileName='ydwx'></emp:message><span id="ct"></span></td>
								    <td class="infostd" id="shospfee"><emp:message key='ydwx_wxfs_spjgyue' defVal='SP账号余额：' fileName='ydwx'></emp:message><span id="spfee"></span></td>								    
								  </tr>
								  
								  	 <tr>
								  <td colspan="2">
								    <label id="messages2"><font class="ydwx_messages2_font"></font></label>
								  </td>
							 	 </tr>
							  		<tr>
									  <td colspan="2">
									    <label id="messages1"><font class="ydwx_messages2_font"><emp:message key='ydwx_wxfs_yuexx' defVal='余额不足不允许进行发送!' fileName='ydwx'></emp:message></font></label>
									  </td>
								  </tr>
								  <tr>
									  <td><emp:message key='ydwx_wxfs_yxhms' defVal='有效号码数：' fileName='ydwx'></emp:message><span id="effs"></span></td>
									  <td><emp:message key='ydwx_wxfs_tjhms' defVal='提交号码数：' fileName='ydwx'></emp:message><span id="counts"></span></td>								  
									  <td align="center">
									   <div id="showeffinfo" class="div_bg">
									       <p  class="unfold ydwx_unfold"><emp:message key='ydwx_wxfs_wxhm' defVal='无效号码' fileName='ydwx'></emp:message>(<span id="alleff" class="ydwx_alleff" ></span>)&nbsp;&nbsp;
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
									      <td align="left"><emp:message key='ydwx_wxfs_hmdhm' defVal='黑名单号码：' fileName='ydwx'></emp:message></td>
										  <td><span id="blacks"></span></td>
									      <td id="predowninfo">
									      <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>	
									        <a href="javascript:uploadbadFiles()"><emp:message key='ydwx_wxfs_xqxz' defVal='详情下载' fileName='ydwx'></emp:message></a><input type="hidden" id="badurl" name="badurl"  value="">
									      <%} %>
									      </td>
								      </tr>
								      <tr>
									      <td align="left"><emp:message key='ydwx_wxfs_cfhm' defVal='重复号码：' fileName='ydwx'></emp:message></td>
									      <td><span id="sames"></span></td>
										  <td></td>
								      </tr>
								      <tr>
								          <td align="left"><emp:message key='ydwx_wxfs_gsff' defVal='格式非法：' fileName='ydwx'></emp:message></td>
								          <td><span id="legers"></span></td>
										  <td></td>
								      </tr>
								      <tr>
								          <td align="left"><emp:message key='ydwx_wxfs_hgjzhm' defVal='含关键字号码：' fileName='ydwx'></emp:message></td>
								          <td><span id="keyW"></span></td>
										  <td></td>
								      </tr>
								    </table>
								    </center>
							    </div>
							    <div style="height: 15px;"></div>	
								<p class="ydwx_somepreview_p">
									<emp:message key='ydwx_wxfs_bfyl' defVal='部分预览：' fileName='ydwx'></emp:message>
								</p>
								<div id="maindiv" class="ydwx_maindiv">
								<center>
								<table id="content"></table>
								</center>
								</div>
						    	<div id="footer" class="ydwx_footer">
						    	  <center>
						    		<input id="btsend" class="btnClass5 mr23" type="button" value="<emp:message key='ydwx_common_btn_fasong' defVal='发送' fileName='ydwx'></emp:message>" onclick="javascript:btsend();"/>
								    <input id="btcancel" onclick="javascript:btcancel1();" class="btnClass6" type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" />
								    <br/>
								  </center> 
						    	</div>	
							</div>
						</form>
					</div>
					
					<div class="clear2"></div>		
					<div id="infomodel" class="infomodelclass ydwx_infomodel">
                        <center>
                        <table class="ydwx_infomodel_tabl">
                          <tr>
                            <td colspan="2" class="ydwx_td1" align="left"><emp:message key="ydwx_wxfs_jtwxfs_gstx_td_1" defVal="文件格式如图：" fileName="ydwx"></emp:message></td>
                          </tr>
                          <tr>
							    <td valign="top" align="left" id="txtFormat"><emp:message key="ydwx_wxfs_jtwxfs_gstx_td_2" defVal="txt格式：" fileName="ydwx"></emp:message></td>
						  		<td valign="top"><div id="txtstyle"  style="width: 155px;height: 69px;background : url('<%=commonPath%>/common/img/dtsend_<%=empLangName%>.png') no-repeat; "></div></td>
                          </tr>
                          <tr height="4px;">
                          		<td colspan="2"></td>
                          </tr>
                          <tr>
							   <td valign="top" align="left" id="xlsFormat"><emp:message key="ydwx_wxfs_jtwxfs_gstx_td_3" defVal="xls,xlsx,et格式：" fileName="ydwx"></emp:message></td>
							   <td valign="top"><div id="xlsstyle" style="width: 155px;height: 85px;background: url('<%=commonPath%>/common/img/dtsend_<%=empLangName%>.png') no-repeat 0px -80px;"></div></td>
                          </tr>
                          <tr>
                          <td colspan="2" align="left">
                           <span style="font-weight: bold;font-size: 14px;"><emp:message key="ydwx_wxfs_jtwxfs_gstx_td_4" defVal="注意：" fileName="ydwx"></emp:message></span><br/>
                           	 1.<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_11" defVal="1.txt格式分隔符号是英文半角“,”；" fileName="ydwx"></emp:message><br/>
	                         2.<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_7" defVal="文件需小于" fileName="ydwx"></emp:message>
	                         <%=StaticValue.MAX_SIZE/1024/1024%>
	                         <emp:message key="ydwx_wxfs_jtwxfs_gstx_td_8" defVal="M，有效号码需少于" fileName="ydwx"></emp:message>
	                         <%=StaticValue.MAX_PHONE_NUM/10000%>
	                         <emp:message key="ydwx_wxfs_jtwxfs_gstx_td_9" defVal="万；" fileName="ydwx"></emp:message><br/>
	                         3.<emp:message key="ydwx_wxfs_jtwxfs_gstx_td_10" defVal="文件仅支持txt、zip、xls、xlsx、rar、et格式。" fileName="ydwx"></emp:message>
                          </td>
                          </tr>
                        </table>
                       </center>
                    </div>

                     <div id="probar" style="display: none;text-align: center;">
							<p >
								<emp:message key="ydwx_common_chlzqs" defVal="处理中,请稍等....." fileName="ydwx"></emp:message>
							</p>
							<div id="shows">
								<img src="<%=commonPath%>/common/img/loader.gif" />
							</div>
							<%-- <div style="margin-left: 20px;" id="processbar"></div> --%>
						</div>
						
						<div id="divBox" class="hideDlg" style="display:none" title="<emp:message key='ydwx_common_mubanyulan' defVal='模板预览' fileName='ydwx'></emp:message>">
							<div  align="center" class="ydwx_divBox_sub">
				          	<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
				          	 <select class="ym ydwx_ym"></select>
				        	</div>
							<div class="ydwx_iphone_dv">
								<iframe class="ydwx_preview_common" id="nm_preview_common" src=""></iframe>	
							</div>
						</div>
						
			</div>
			<div id="contentDiv" title="<emp:message key='ydwx_wxfs_jtwxfs_dxnr_djxzdxnr_td1' defVal='短信内容选择' fileName='ydwx'></emp:message>" class="ydwx_contentDiv">
			<iframe id="contentFrame" name="contentFrame" class="ydwx_contentFrame" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
				<div style="text-align: center">
					<input class="btnClass5 mr23 ydwx_borderradius" onclick="tempSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
					<input class="btnClass6 ydwx_borderradius" onclick="tempCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
					<br/>
				</div>
			</div>
			
			 <div id="draftDiv" title="<emp:message key='ydwx_wxfs_jtwxfs_dxnr_cgx_td1' defVal='草稿箱选择' fileName='ydwx'></emp:message>" class="ydwx_draftDiv">
                <iframe id="draftFrame" name="draftFrame" style="width:<%="zh_HK".equals(empLangName)?910:880%>px;height:400px;border: 0;" marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
                <div style="text-align: center">
                    <input class="btnClass5 mr23" onclick="draftSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
                    <input class="btnClass6" onclick="draftCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
                    <br/>
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
		<iframe id="ifr" class="ifr"></iframe>
		<div id="tempDiv" title="<emp:message key="ydwx_wxfs_dtwxfs_wxnrxz" defVal="网讯内容选择" fileName="ydwx"></emp:message>" style="background-color:#FAFAFF;padding:5px;display:none;height:565px;">
			<iframe id="tempFrame" name="tempFrame" class="ydwx_tempFrame"  marginwidth="0" scrolling="no" frameborder="no" src="<%=commonPath%>/common/blank.jsp"></iframe>
			<div style="text-align: center">
				<%--<input class="btnClass5" onclick="tempSure()" value="选择" type="button"/>
				<input class="btnClass6" onclick="tempCancel()" value="取消" type="button"/>
				 --%>
			</div> 
		</div>
		<div id="message" title="<emp:message key="ydwx_common_btn_tishi" defVal="提示" fileName="ydwx"></emp:message>" class="dywx_message">
			<div class="ydwx_message_sub1"></div>
	 		<center>
			<label ><emp:message key="ydwx_wxfs_fszwgcg" defVal="发送至网关成功！" fileName="ydwx"></emp:message></label>
			<a href="javascript:sendRecord(<%=lguserid%>,<%=lgcorpcode%>)"  class="alink" ><emp:message key="ydwx_wxfs_ckfsjl" defVal="查看发送记录" fileName="ydwx"></emp:message></a>
			</center>
			<br>
			<div class="ydwx_message_sub2"></div>
			 <center>
			    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="<emp:message key="ydwx_common_btn_guanbi" defVal="关闭" fileName="ydwx"></emp:message>" />
			</center> 
		</div>
		<script src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/jquery.selection.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=iPath %>/js/wx_senddsm.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=empFramePath%>/js/dialogui.js"></script>
		<script type="text/javascript">
		//新增网讯链接
	function toAdd(){
	window.parent.openNewTab("<%=ViewParams.WXTEMPCODE %>","<%=path%>/wx_ueditor.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&type=2");
		//location.href="wx_ueditor.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&type=2";
	}
	
		//初始化内容
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
				msgob.attr("readonly","");
			}
		}
				//设置sp用户
		function setSpUser(){
		    var isReply = $("input:radio[name='isReply']:checked").attr("value");
			$("#spUser").empty();
			var date = new Date();
			 var time = date.valueOf(); 
			if(isReply==1){
				$("#spUser").load("<%=path%>/wx_senddsm.htm?method=getSpUser&date="+time  );
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
		//select 下拉初始化
		$(function(){
			  $('#busCode,#spUser').isSearchSelect({'width':'180','isInput':false,'zindex':0});
		   });
		</script>
	</body>
</html>
