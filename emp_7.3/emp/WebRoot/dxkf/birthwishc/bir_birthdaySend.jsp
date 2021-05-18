<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.birthwish.LfBirthdaySetup"%>
<%@ page import="com.montnets.emp.entity.clientsms.LfDfadvanced"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.servmodule.dxkf.constant.DxkfStaticValue"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
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
	
	String isTrue = (String) request.getAttribute("isTrue");
	String context = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	
	@SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>)request.getAttribute("spUserList");


	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get(request.getAttribute("titlePath"));
	menuCode = menuCode == null ? "0-0-0" : menuCode;
    LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
    String userName = lfSysuser.getName();
    String lguserid=(String)request.getAttribute("lguserid");
    String lgcorpcode=lfSysuser.getCorpCode();
    
    LfBirthdaySetup setup = (LfBirthdaySetup)request.getAttribute("birthdaySetup");
    
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    LfSysuser taskOwner = (LfSysuser)request.getAttribute("taskOwner");
    String signName = taskOwner.getName();
    boolean updateRightFlag = ((String)request.getAttribute("lguserid")).equals(taskOwner.getUserId().toString());
    
   String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
   String nameSignLeft = DxkfStaticValue.getBirthwishNameSignLeft();
   String nameSignRight = DxkfStaticValue.getBirthwishNameSignRight();
   //高级默认设置
   LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
   
  
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxkf_ydkf_srzf_text_birthdayset" defVal="生日祝福设置页面" fileName="dxkf"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/bir_birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_KeHuShengRiZhuFu.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="birthdaySend">
		<div id="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<form id="form2" name="form2" action="bir_birthdaySendClient.htm?method=update" method="post">
					<input id="commonPath" type="hidden" name="commonPath" value="<%=commonPath %>" />
					<input id="skin" type="hidden" name="skin" value="<%=skin%>" />
					<input id="cpath" value="<%=path%>" type="hidden" />
					<input id="path" value="<%=inheritPath%>" type="hidden" />
					<input id="isOk" name="isOk" value="" type="hidden"/>
					<input id="setupid" name="setupid" value="" type="hidden"/>
					<%--0代表不是点确定按钮，1代表是点击确定按钮  --%>
					<input type="hidden" value="0" name="isSubmit" id="isSubmit">
					<input id="taskOwnerId" name="taskOwnerId" type="hidden" value=""/><%-- 任务所有者id --%>
					<%--客户机构IDS  --%>
					<input type="hidden" id="cliDepIds"  name="cliDepIds" value=""/>
					<%--客户IDS  --%>
					<input type="hidden" id="cliIds"  name="cliIds" value=""/>
					<%-- 员工机构名字 --%>
					<input type="hidden" value="" id="clientDepName" name="clientDepName"/>
					<%-- 员工名字 --%>
					<input type="hidden" value="" id="clientName" name="clientName"/>
					<%--用于数据回显--%>
					<input type="hidden" value="" id="rightSelectedUserOption"/>
					<%--选择发送对象为1--%>
					<input id="haveOne" name="haveOne" value="<%=setup == null ? "0":"1"%>" type="hidden"/>
					<%--常用数据--%>
					<div class="dxkf_display_none" id="hiddenValueDiv"></div>
					
					<div class="titletop">
						<table class="titletop_table">
							<tr>
								<td class="titletop_td">
									<% if(setup!=null && setup.getMsg() !=null && setup.getMsg().length()>0)
									{
										if(updateRightFlag){
										   // out.print("修改生日祝福");
										   out.print(MessageUtils.extractMessage("dxkf","dxkf_ydkf_srzf_text_updatesrzf",request)) ;
										}else{
										   // out.print("查看生日祝福");
										    out.print(MessageUtils.extractMessage("dxkf","dxkf_ydkf_srzf_text_selectsrzf",request)) ;
										}
									}
									else
									{
									   // out.print("新建生日祝福");
									    out.print(MessageUtils.extractMessage("dxkf","dxkf_ydkf_srzf_text_addsrzf",request)) ;
									}									
									%>
								</td>
								<td align="right">
									<span class="titletop_font" onclick="goback();">&larr;&nbsp;<emp:message key="dxkf_common_opt_gobackpri" defVal="返回上一级" fileName="dxkf"/></span>
								</td>
							</tr>
						</table>
					</div>
					
					<div id="detail_Info">
					<table id="itemTab">
						<tr>
							<td width="<%="zh_HK".equals(empLangName)?15:10%>%">
								<span  class="righttitle"><emp:message key="dxkf_ydkf_srzf_text_taskname" defVal="任务主题" fileName="dxkf"/></span>：
							</td>
							<td>
								<input type="text" class="input_bd div_bd" id="title" name="title" maxlength="20" value="<%=(setup==null || setup.getTitle()==null)?"":setup.getTitle() %>" <%=updateRightFlag?"":"onfocus='this.blur()'" %>/>
								<xmp id="titleXmp" name="titleXmp" style="display:none"><%=(setup==null || setup.getTitle()==null)?"":setup.getTitle()%></xmp>
							</td>
						</tr>
						
						<tr>
						<td >
							<span  class="righttitle"><emp:message key="dxkf_ydkf_srzf_text_sendtime" defVal="发送时间" fileName="dxkf"/></span>：
						</td>
						<td>
								<input type="text" <%=updateRightFlag?"":"disabled" %> 
									class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'HH:mm',isShowToday:false})"
									id="sendTime" name="sendTime" value='<%=setup==null?"":sdf.format(setup.getSendTime()) %>'>
						</td>
						</tr>
						<tr>
							<td>
								<span  class="righttitle"><emp:message key="dxkf_ydkf_srzf_text_sendobject" defVal="发送对象" fileName="dxkf"/></span>：
							</td>
							<td>
								<input onclick="showInfo();" type="button" value="<emp:message key="dxkf_ydkf_srzf_opt_choosepeploe" defVal="选择人员" fileName="dxkf"/>" class="btnClass2" id="selectWorkerButton">
							</td>
						</tr>
						
						<tr>
							<td class="dxkf_td1">
								<span  class="righttitle"><emp:message key="dxkf_ydkf_srzf_text_sendcontent" defVal="发送内容" fileName="dxkf"/></span>：
							</td>
							<td class="dxkf_td2">
							<table id="mesContent" class="div_bd">
							<tr>
							<td colspan="2"  class="div_bd dxkf_td3">
							<div class="dxkf_div1">
								<span class="dxkf_span">&nbsp;&nbsp;<emp:message key="dxkf_ydkf_srzf_text_fdzc" defVal="附带尊称：" fileName="dxkf"/></span>
								<input <%=updateRightFlag?"":"disabled" %> type="checkbox" value="1" name="isAddName" id="isAddName" onclick="setSign(this)" class="dxkf_isAddName"/>&nbsp;&nbsp;&nbsp;
								<input <%=updateRightFlag?"":"disabled" %> type="text"  onfocus="this.blur()" readonly="readonly" value="" name="signStr" id="signStr" class='<%=setup!=null && setup.getIsAddName()==1?"input_bd dxkf_signStr1":"input_bd dxkf_signStr2" %>'/>
							</div>
							<div class="div_bd dxkf_div2" >
								<span class="dxkf_span1">&nbsp;&nbsp;<emp:message key="dxkf_ydkf_srzf_text_fdqm" defVal="附带签名：" fileName="dxkf"/></span>
								<input <%=updateRightFlag?"":"disabled" %> type="checkbox" value="1" name="isSignName" id="isSignName" onclick="setSign2(this)"/>&nbsp;&nbsp;&nbsp;
								<input <%=updateRightFlag?"":"disabled" %>  onmouseover="this.title=this.value" type="text"  onfocus="this.blur()" readonly="readonly" value="" name="signName" id="signName" class='<%=setup!=null && setup.getIsSignName()==1?"input_bd dxkf_signStr1":"input_bd dxkf_signStr2" %>'/>
							</div>
							</td>
							</tr>
							<tr>
							<td colspan="2" class="div_bd">
							<textarea <%=updateRightFlag?"":"onfocus='this.blur()'" %> class=".msg2" name="msg" rows="5" id="contents" onblur="eblur($(this))"><%=setup==null?"":setup.getMsg()%></textarea><br/>
								<xmp id="msgXmp" name="msgXmp" style="display:none"><%=setup==null?"":setup.getMsg()%></xmp>
							</td>
							</tr>
							</table>
							<label>
								<b id="strlen">0</b><b id="maxLen">/980</b> <font class="dxkf_font2"><emp:message key="dxkf_ydkf_srzf_text_smslength" defVal="实际发送长度以姓名字数为准" fileName="dxkf"/></font>
							</label>
							</td>
						</tr>
					</table>
					</div>										
					<div class="moreItem div_bg">
					<table class="dxkf_table">
						 <tr>
							<td >
								<%--<font id="spuserTd"><%=StaticValue.SMSACCOUNT %></font>：
								--%><font id="spuserTd"><emp:message key="dxkf_ydkf_srzf_text_spaccount_mh" defVal="SP账号：" fileName="dxkf"/></font>
							</td>
							<td>
								<select id="spUser" name="spUser" class="input_bd" <%=updateRightFlag?"":"disabled" %>>
								<%
								if (spUserList != null && spUserList.size() > 0) {
									String spUserId = lfDfadvanced != null?lfDfadvanced.getSpuserid():"";
									for (Userdata spUser : spUserList) {
								%>
								<option value="<%=spUser.getUserId()%>"  
									<%=setup == null && spUserId != null && !"".equals(spUserId) && spUserId.equals(spUser.getUserId())?"selected":"" %>>
									<%=spUser.getUserId()+"("+spUser.getStaffName()+")"%>
								</option>
								<%}}%>
								</select>
							</td>
							<%if(setup == null){ %>
							<td align="center">
								<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="dxkf_ydkf_srzf_opt_xxcwmr" defVal="选项存为默认" fileName="dxkf"/></a>
							</td>
							<%} %>
						</tr>						
						<tr>
							<td>
								<font id="isUseTd"><emp:message key="dxkf_ydkf_srzf_opt_sfqy" defVal="是否启用" fileName="dxkf"/></font>：
							</td>
							<td>
							<p>
							<input <%=updateRightFlag?"":"disabled" %> type="radio" id="isUse" name="isUse" value="1" />&nbsp;<emp:message key="dxkf_common_opt_qiyong" defVal="启用" fileName="dxkf"/>
							<input <%=updateRightFlag?"":"disabled" %> type="radio" id="isUse2"  name="isUse" value="2" />&nbsp;<emp:message key="dxkf_common_opt_tingyong" defVal="停用" fileName="dxkf"/>
							</p>
							</td>
						</tr>
						</table>
					</div>
					<div class="clear2"></div>
					<div class="dxkf_div3">
							<%
								if(updateRightFlag){
									
							%>
								<input id="update" class="btnClass5 mr23" type="button" onclick="commitForm()" value="<emp:message key="dxkf_common_opt_tijiao" defVal="提交" fileName="dxkf"/>" class="dxkf_font_size"/>
							<%
								}
							%>
								<input id="qingkong" class="btnClass6"  type="button"  onclick="goback();" value="<emp:message key="dxkf_common_opt_fanhui" defVal="返回" fileName="dxkf"/>" class="dxkf_font_size"/>
					</div>
					
				</form>
			</div>
			<iframe id="ifr" class="ifr"></iframe>
			<div id="id2" class="remindMessage"></div>
			<div id="infoDiv" title="<emp:message key="dxkf_srzf_opt_xzfsdx" defVal="选择发送对象" fileName="dxkf"/>">
				<iframe id="flowFrame" name="flowFrame" src="" marginwidth="0" marginheight="-20px" scrolling="no" frameborder="no"></iframe>
				<div id="choiceNum" class="dxzs_display_none">0</div><%-- 数字 --%>
				<table><tr><td class="dxfk_td1" align="center" valign="bottom">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" id="dook" value="<emp:message key="dxkf_common_opt_queding" defVal="确定" fileName="dxkf"/>" class="btnClass5 mr23" onclick="doOk()" />
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  value="<emp:message key="dxkf_common_opt_quxiao" defVal="取消" fileName="dxkf"/>" class="btnClass6" onclick="doNo()"/>
				</td></tr></table> 
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

		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/dxkf_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/birSend.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
		<script>
		
		function setSign2(obj)
		{
			if(obj.checked)
			{
				
				$("#signName").css("display","inline");
				$("#signName").css("background-color","#E8E8E8");
				$("#signName").val("<%=nameSignLeft%>"+"<%=signName%>"+"<%=nameSignRight%>");
			}else
			{
				$("#signName").css("display","none");
				$("#signName").val("");
			}
			eblur($("#contents"));
		}
		function changeDialogTitleCss(id) {
		    var $titleBar = $("#ui-dialog-title-" + id);
		    $titleBar.parent().addClass("titleBar");
		    $titleBar.addClass("titleBarTxt");
		}
		function showInfo() {
            var skin = $("#skin").val();
            var commonPath = $("#commonPath").val();
			var ownerId = $("#taskOwnerId").val();
            var lgcorpcode = $("#lgcorpcode").val();
            var lguserid = $("#lguserid").val();
            var chooseType="2";
            var bsid = "<%=setup == null ? -1:setup.getId()%>";
            if(bsid > 0){
                //点击修改bsid有值进入
                //为了防止修改值被重置应先判断是不是已经修改，即是否点了确定
                if($("#isSubmit").val() === "0"){
                    getBirthMembers(bsid, ownerId, lgcorpcode, skin);
                }
            }
            if(skin.indexOf("frame4.0") > -1){
                $("#flowFrame").attr("src",commonPath+"/common/selectUserInfo.jsp?lguserid="+lguserid+"&chooseType="+chooseType+"&lgcorpcode="+lgcorpcode);
                changeDialogTitleCss('infoDiv');
            }else{
                $("#flowFrame").attr("src",commonPath+"/dxkf/birthwishc/bir_birthSendInfo.jsp?lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
            }
            resizeDialog($("#infoDiv"),"ydkfDialogJson","kfdx_kfqzqf_test2");
            $("#infoDiv").dialog("open");
		}

        function getBirthMembers(bsid, ownerId, lgcorpcode, skin) {
		    $.ajax({
                type : "post",
                url : "bir_birthdaySendClient.htm",
                data : {
                    method:"getBirthMembers",
                    setupid:bsid,
                    ownerId:ownerId,
                    lgcorpcode:lgcorpcode
                },
                async : false,
                success: function (data) {
                    var reg = /\[(\d+)人]$/;
                    var clientIds = "";
                    var clientDepIds = "";
                    var clientName = "";
                    var clientDepName = "";
                    var manCount = 0;
                    var optionStr = "";
                    var array = eval("(" + data + ")");
                    //遍历
                    $(array).each(function () {
                        var membertype = this['membertype'];
                        if(membertype === 1){
                            //客户
                            clientIds += this['memberId'] + ",";
                            clientName += this['addName'] + ",";
                            manCount++;
                            if(skin.indexOf("frame4.0") > -1){
                                optionStr += "<option value=\""+ this['memberId'] +"\" isdep=\"5\" et=\"\" mobile=\""+ this['phone'] +"\">"+ this['addName'] +"</option>";
                            }else {
                                optionStr += "<option membertype=\"1\" value=\""+ this['memberId'] +"\">"+ this['addName'] +"</option>";
                            }
                        }else if(membertype === 2){
                            //单机构
                            clientDepIds += this['memberId'] + ",";
                            clientDepName += this['addName'] + ",";
                            this['addName'].match(reg);
                            manCount += parseInt(RegExp.$1);
                            if(skin.indexOf("frame4.0") > -1){
                                optionStr += "<option value=\""+ this['memberId'] +"\" isdep=\"2\" et=\"1\" mcount=\""+parseInt(RegExp.$1)+"\">"+ this['addName'] +"</option>";
                            }else {
                                optionStr += "<option membertype=\"2\" value=\""+ this['memberId'] +"\" mcount=\""+ parseInt(RegExp.$1) +"\">"+ this['addName'] +"</option>";
                            }
                        }else if(membertype === 3){
                            //包含子机构
                            clientDepIds += "e" + this['memberId'] + ",";
                            clientDepName += this['addName'] + ",";
                            this['addName'].match(reg);
                            manCount += parseInt(RegExp.$1);
                            if(skin.indexOf("frame4.0") > -1){
                                optionStr += "<option value=\""+ this['memberId'] +"\" isdep=\"2\" et=\"2\" mcount=\""+ parseInt(RegExp.$1) +"\">"+ this['addName'] +"</option>";
                            }else {
                                optionStr += "<option membertype=\"3\" value=\""+ this['memberId'] +"\" mcount=\""+ parseInt(RegExp.$1) +"\">"+ this['addName'] +"</option>";
                            }
                        }
                    });
                    $("#cliDepIds").val(clientDepIds);
                    $("#cliIds").val(clientIds);
                    $("#clientDepName").val(clientDepName);
                    $("#clientName").val(clientName);
                    $("#choiceNum").html(manCount);
                    $("#rightSelectedUserOption").val(optionStr);
                }
            });
        }

		$(document).ready(
			function(){
				// floatingRemind("isUseTd","启用必须在每天23:00之前，第二天正式生效");
				floatingRemind("isUseTd",getJsLocaleMessage('dxkf','dxkf_srzf_page5_isUseTd'));
				getLoginInfo("#hiddenValueDiv");
				$("#taskOwnerId").val("<%=taskOwner.getUserId()%>");

                //防止不点击修改人员
                var bsid = "<%=setup == null ? -1:setup.getId()%>";
                var lguserid = $("#lguserid").val();
                var lgcorpcode = $("#lgcorpcode").val();
                var skin = $("#skin").val();
                getBirthMembers(bsid, lguserid, lgcorpcode, skin);

				if("<%=updateRightFlag%>"!="true")
				{
					$(window.frames['flowFrame'].document).find("#selectDep").attr("disabled","disabled");
					$(window.frames['flowFrame'].document).find("#toLeft").attr("disabled","disabled");
					$(window.frames['flowFrame'].document).find("#toRight").attr("disabled","disabled");
					
					$("#dook").attr("disabled","disabled");
				}else
				{
					$(window.frames['flowFrame'].document).find("#right").dblclick(function(){
						window.frames['flowFrame'].moveRight1();
			    	});
					//$(window.frames['flowFrame'].document).find("#left").dblclick(function(){
						//window.frames['flowFrame'].moveLeft1();
			    	//});
			    }
				$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});

                //用textarea显示短信内容
                $("#contents").empty();
                $("#contents").text($("#msgXmp").text());
                //标题修改
                $("#title").empty();
                $("#title").val($("#titleXmp").text());
				
				synlen();
				$("#spUser").trigger("change");	
			   
				<%
				if(setup!=null && setup.getIsAddName()==1)
				{
				%>
					$("#isAddName").attr("checked","checked");
					$("#signStr").attr("display","inline");
					$("#signStr").css("background-color","#E8E8E8");
					$("#signStr").val(getJsLocaleMessage('dxkf','dxkf_srzf_birSend_zjd') + "XXX");
					$('#strlen').html("<%=24+setup.getMsg().length()%>");
					$("#spUser").attr("value","<%=setup.getSpUser()%>");
				<%
				}
				if(setup!=null && setup.getIsAddName()==2)
				{
				%>
					$('#strlen').html("<%=setup.getMsg().length()%>");
					$("#spUser").attr("value","<%=setup.getSpUser()%>");
				<%
				}
				if(setup!=null && setup.getIsSignName()==1)
				{
				%>
					$("#isSignName").attr("checked","checked");
					$("#signName").attr("display","inline");
					$("#signName").css("background-color","#E8E8E8");
					$("#signName").val("<%=nameSignLeft%>"+"<%=signName%>"+"<%=nameSignRight%>");
					$('#strlen').html(parseInt($('#strlen').html())+<%=signName.length()%>+2);//+2表示两个[]
				<%	
				}
				if(setup!=null &&setup.getIsUse()==2)
				{
				%>
					$("#isUse2").attr("checked","checked");
				<%
					
				}else
				{
				%>
					$("#isUse").attr("checked","checked");
				<%
				}
				%>
				
				setContentMaxLen();
				$("#spUser").bind("change", function(){setContentMaxLen();});
				
				$("#infoDiv").dialog({
					autoOpen: false,
					height:535,
					width: 535,
					resizable:false,
					modal: true,
					open:function(){
						$("#isSubmit").val("0");
					}
				});
			});
			
	
       	function reloadPage()
       	{
           	window.location.href = "<%=path%>/bir_birthdaySendClient.htm?method=toUpdate&setupid="+<%=setup==null?"-1":setup.getId()%>+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
       	}

       	function goback()
       	{
           	window.location.href = "<%=path%>/bir_birthdaySendClient.htm?method=find&isback=1&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
       	}
       	function commitForm()
       	{
       		if($("#isAddName").attr("checked") && $.trim($("#signStr").val())=="")
           	{
				// alert("请填写附带尊称！");
				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_txfdzc'));
				return;
            }
            if($.trim($("#contents").val())=="")
            {
				// alert("祝福语内容不能为空！");
				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_wishnotnull'));
				return;
            }
            if ($("#haveOne").val() === "0") {
                //alert(发送对象不能为空)
                alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_receiversnotnull'));
                return;
            }
            if(parseInt($('#strlen').html())>990) {
				// alert("字数超过限制！请重新输入");
				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_zscc'));
				return;
            }
            if($("#sendTime").val()=="")
            {
				// alert("发送时间不能为空！");
				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_sendtimenotnull'));
				return;
            }
            if($("#spUser").val()=="" || $("#spUser").val() ==null)
            {
				// alert("发送账号不能为空！");
				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_accountnotnull'));
				return;
            }
            $('#setupid').val('<%=setup==null?"-1":setup.getId()%>');
            var commitFlag = true;
       		$("#update").attr("disabled",true);
         	$("#qingkong").attr("disabled",true);
         	$("#close").attr("disabled",true);
         	var msg = $("#contents").val();
         	$.ajax({
         		url: "bir_birthdaySendClient.htm", 
         		type : "POST",
         		data: {method : "checkBadWord1",tmMsg : msg,corpCode:$("#lgcorpcode").val()},
         		success: function(message) {
         			if(message.indexOf("@")==-1)
             		{
         				// alert("过滤关键字失败！");
         				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_glkeyfalse'));
         				$("#update").attr("disabled","");
         				$("#qingkong").attr("disabled","");
         				$("#close").attr("disabled","");
         				return;
         			}
         			else if (message.substr(message.indexOf("@")+1)!="" && message.substr(message.indexOf("@")+1) !="error") 
             		{
         				// alert("发送内容包含如下违禁词组：\n     " + message.substr(message.indexOf("@")+1) + "\n请检查后重新输入");
         				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_client_cannotword') + message.substr(message.indexOf("@")+1) + getJsLocaleMessage('dxkf','dxkf_dxqf_client_reinput'));
         				$("#update").attr("disabled","");
             			$("#qingkong").attr("disabled","");
             			$("#close").attr("disabled","");
         				return;
         			}else if (message.substr(message.indexOf("@")+1) == "error") 
             		{
         				// alert("操作失败");
         				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_optionfalse'));
         				$("#update").attr("disabled","");
             			$("#qingkong").attr("disabled","");
             			$("#close").attr("disabled","");
         				return;
         			}
         			if(commitFlag)
         			{
         				document.forms["form2"].submit();
         				commitFlag=false;
             		}
         		},
         		error:function(xrq,textStatus,errorThrown){
         			// alert("过滤关键字失败！");
         			alert(getJsLocaleMessage('dxkf','dxkf_srzf_page5_glkeyfalse'));
         			$("#update").attr("disabled","");
         			$("#qingkong").attr("disabled","");
         			$("#close").attr("disabled","");
         			return;
         		}
         	});
        }
    
        //定义replaceAll方法
       	String.prototype.replaceAll = function(s1, s2) {
       		return this.replace(new RegExp(s1, "gm"), s2);
       	}
       	
       	function deleteSetup1()
       	{
           	var setupId;
           	var type;
			if(confirm(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_confiretodeletset')))
			{
				$("#deleteSetup").attr("disabled",true);
				if("<%=setup==null%>"=="true")
				{
					reloadPage();
					return;
				}else
				{
					<% 
					if(setup!=null)
					{
					%>
					setupId = "<%=setup.getId()%>";
					type = "<%=setup.getType()%>";
					<%
					}
					%>
				}
					var lgcorpcode=$("#lgcorpcode").val();
					$.post("bir_birthdaySendClient.htm",{method:"deleteSetup",lgcorpcode:lgcorpcode,setupId:setupId,type:type},function(data)
					{
						if(data=="true")
						{
							// alert("删除成功！");
							alert(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_deletesuccess'));
							reloadPage();
						}else
						{
							//alert("删除失败！");
							alert(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_deletefalse'));
							$("#deleteSetup").attr("disabled","");
						}
					});
			}
		
        }
    </script>
	</body>
</html>
