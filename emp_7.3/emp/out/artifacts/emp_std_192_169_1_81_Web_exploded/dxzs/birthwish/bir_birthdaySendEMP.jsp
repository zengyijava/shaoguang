<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.birthwish.LfBirthdaySetup"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue"%>
<%@page import="com.montnets.emp.entity.dxzs.LfDfadvanced"%>
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
    String lguserid = String.valueOf(lfSysuser.getUserId());
    String lgcorpcode = lfSysuser.getCorpCode();
    
    LfBirthdaySetup setup = (LfBirthdaySetup)request.getAttribute("birthdaySetup");
    
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    LfSysuser taskOwner = (LfSysuser)request.getAttribute("taskOwner");
    String signName = taskOwner.getName();
    boolean updateRightFlag = ((String)request.getAttribute("lguserid")).equals(taskOwner.getUserId().toString());
    //高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
    
   String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
   String nameSignLeft = DxzsStaticValue.getBIRTHWISH_NAME_SIGN_LEFT();
   String nameSignRight = DxzsStaticValue.getBIRTHWISH_NAME_SIGN_RIGHT();
   //语言方面相关
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String xgzf = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_144", request);
	String ckzf = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_145", request);
	String xjzf = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_146", request);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="dxzs_xtnrqf_title_40" defVal="生日祝福设置页面" fileName="dxzs"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/birthdaySend.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/bir_birthdaySendEMP.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YuanGongShengRiZhuFu.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="birthdaySendEMP">
		<%
		String posiStr = "";
		if(setup!=null && setup.getMsg() !=null && setup.getMsg().length()>0)
		{
			if(updateRightFlag){
				posiStr=xgzf;
			}else{
				posiStr=ckzf;
			}
		}
		else
		{
			posiStr=xjzf;
		}									
		%>
		<div id="container">
		<%-- 当前位置 --%>
			<%-- 不需要操作员位置,所以posiStr不需要作为参数传过去,修改人moll--%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) /*,posiStr*/%>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<form id="form2" name="form2" action="bir_birthdaySendEMP.htm?method=update" method="post">					
					<input id="cpath" value="<%=path%>" type="hidden" />
					<input id="path" value="<%=inheritPath%>" type="hidden" />
					<input id="isOk" name="isOk" value="" type="hidden"/>
					<input id="setupid" name="setupid" value="" type="hidden"/>
					<input id="haveOne" name="haveOne" value="<%=setup == null ? "0":"1"%>" type="hidden"/>
					<%--0代表不是点确定按钮，1代表是点击确定按钮  --%>
					<input type="hidden" value="0" name="isSubmit" id="isSubmit">
					<input type="hidden" id="skin" name="skin" value="<%=skin%>"/>
					<input type="hidden" value="" id="empDepIds" name="empDepIds"/><%-- 员工机构Ids --%>
					<input type="hidden" value="" id="empDepName" name="empDepName"/><%-- 员工机构名字 --%>
					<input type="hidden" value="" id="empIds" name="empIds"/><%-- 员工Ids --%>
					<input type="hidden" value="" id="empName" name="empName"/><%-- 员工名字 --%>
                    <input type="hidden" value="" id="rightSelectedUserOption"/><%--用于数据回显--%>
					<input id="taskOwnerId" name="taskOwnerId" type="hidden" value=""/><%-- 任务所有者id --%>
					<div id="hiddenValueDiv"></div>
					
					<div class="titletop">
						<table class="titletop_table">
							<tr>
								<td class="titletop_td">
									<%=posiStr%>
								</td>
								<td align="right">
									<span class="titletop_font" onclick="goback();">&larr;&nbsp;<emp:message key="dxzs_xtnrqf_title_89" defVal="返回上一级" fileName="dxzs"/></span>
								</td>
							</tr>
						</table>
					</div>
					
					<div id="detail_Info" class="ydbg_detail_Info">
					<table id="itemTab">
						<tr>
							<td width="<%="zh_HK".equals(empLangName)?15:10%>%">
								<span  class="righttitle"><emp:message key="dxzs_xtnrqf_title_90" defVal="任务主题" fileName="dxzs"/></span>：
							</td>
							<td>
								<input type="text" class="input_bd div_bd" id="title" name="title" maxlength="20"  value="<%=(setup==null || setup.getTitle()==null)?"":setup.getTitle() %>" <%=updateRightFlag?"":"onfocus='this.blur()'" %>/>
								<xmp id="titleXmp" name="titleXmp" style="display:none"><%=(setup==null || setup.getTitle()==null)?"":setup.getTitle()%></xmp>
							</td>
						</tr>
						
						<tr>
						<td >
							<span  class="righttitle"><emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/></span>：
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
								<span  class="righttitle"><emp:message key="dxzs_xtnrqf_title_91" defVal="发送对象" fileName="dxzs"/></span>：
							</td>
							<td>
								<input onclick="showInfo();" type="button" value="<emp:message key='dxzs_xtnrqf_title_7' defVal='选择人员' fileName='dxzs'/>" class="btnClass2" id="selectWorkerButton">
							</td>
						</tr>
						
						<tr>
							<td class="dxzs_td1">
								<span  class="righttitle"><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/></span>：
							</td>
							<td class="dxzs_td2">
							<table id="mesContent" class="div_bd">
							<tr>
							<td colspan="2"  class="dxzs_td3">
							<div class="dxzs_div1">
								<span class='<%="zh_HK".equals(empLangName)?"dxzs_span1":"dxzs_span2"%>'>&nbsp;&nbsp;<emp:message key="dxzs_xtnrqf_title_92" defVal="附带尊称" fileName="dxzs"/>：</span>
								<input <%=updateRightFlag?"":"disabled" %> type="checkbox" value="1" name="isAddName" id="isAddName" onclick="setSign(this)" class="dxzs_input"/>&nbsp;&nbsp;&nbsp;
								<input <%=updateRightFlag?"":"disabled" %> class='<%=setup!=null && setup.getIsAddName()==1?"input_bd1":"input_bd2" %>' type="text"  onfocus="this.blur()" readonly="readonly" value="" name="signStr" id="signStr" />
							</div>
							<div class="dxzs_div2">
								<span class='<%="zh_HK".equals(empLangName)?"dxzs_span3":"dxzs_span2"%>'>&nbsp;&nbsp;<emp:message key="dxzs_xtnrqf_title_93" defVal="附带签名" fileName="dxzs"/>：</span>
								<input <%=updateRightFlag?"":"disabled" %> type="checkbox" value="1" name="isSignName" id="isSignName" onclick="setSign2(this)" class="dxzs_input"/>&nbsp;&nbsp;&nbsp;
								<input <%=updateRightFlag?"":"disabled" %>  onmouseover="this.title=this.value" class='<%=setup!=null && setup.getIsAddName()==1?"input_bd1":"input_bd2" %>' type="text"  onfocus="this.blur()" readonly="readonly" value="" name="signName" id="signName" />
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
								<b id="strlen">0</b><b id="maxLen">/980</b> <font class="dxzs_font"><emp:message key="dxzs_xtnrqf_title_94" defVal="实际发送长度以姓名字数为准" fileName="dxzs"/></font>
							</label>
							</td>
						</tr>
					</table>
					</div>										
					<div class="moreItem div_bg">
					<table class="table">
						 <tr>
							<td >
								<font id="spuserTd"><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/></font>：
							</td>
							<td>
								<select id="spUser" name="spUser" class="input_bd"  <%=updateRightFlag?"":"disabled" %>>
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
								<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="dxzs_xtnrqf_title_29" defVal="选项存为默认" fileName="dxzs"/></a>
							</td>
							<%} %>
						</tr>						
						<tr>
							<td>
								<font id="isUseTd"><emp:message key="dxzs_xtnrqf_title_95" defVal="是否启用" fileName="dxzs"/></font>：
							</td>
							<td>
							<p>
							<input <%=updateRightFlag?"":"disabled" %> type="radio" id="isUse" name="isUse" value="1"/>&nbsp;<emp:message key="dxzs_xtnrqf_title_96" defVal="启用" fileName="dxzs"/>
							<input <%=updateRightFlag?"":"disabled" %> type="radio" id="isUse2"  name="isUse" value="2" />&nbsp;<emp:message key="dxzs_xtnrqf_title_97" defVal="停用" fileName="dxzs"/>
							</p>
							</td>
						</tr>
						</table>
					</div>
					<div class="clear2"></div>
					<div class="dxzs_div3">
							<%
								if(updateRightFlag){
									
							%>
								<input id="update" class="btnClass5 mr23" type="button" onclick="javascript:commitForm()" value="<emp:message key='dxzs_xtnrqf_button_2' defVal='提交' fileName='dxzs'/>" />
							<%
								}
							%>
								<input id="qingkong" class="btnClass6"  type="button"  onclick="javascript:goback();" value="<emp:message key='dxzs_xtnrqf_button_9' defVal='返回' fileName='dxzs'/>"/>
					</div>
					
				</form>
			</div>
			<iframe id="ifr" class="ifr"></iframe>
			<div id="id2" class="remindMessage"></div>
			<div id="infoDiv" title="<emp:message key='dxzs_xtnrqf_title_40' defVal='选择发送对象' fileName='dxzs'/>">
				<iframe id="flowFrame" name="flowFrame" src="" marginwidth="0" marginheight="-20px" scrolling="no" frameborder="no"></iframe>
				<table><tr><td align="center" valign="bottom">
				<input id="lguserid" type="hidden" name="lguserid" value="<%=lguserid %>"/>
				<input id="lgcorpcode" type="hidden" name="lgcorpcode" value="<%=lgcorpcode %>" />
				<input id="commonPath" type="hidden" name="commonPath" value="<%=commonPath %>" />
				<div id="choiceNum" class="dxzs_display_none">0</div><%-- 数字 --%>
				<input type="button" id="dook" value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass5 mr23" onclick="doOk()" />
				<input type="button"  value="<emp:message key='dxzs_xtnrqf_button_5' defVal='取消' fileName='dxzs'/>" class="btnClass6" onclick="doNo()" />
				<br/>
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
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/birSendE.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $("#lgcorpcode").val();
			var chooseType="1";
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
                $("#flowFrame").attr("src",commonPath+"/dxzs/birthwish/bir_birthSendInfoEMP.jsp?lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
			}
			$("#infoDiv").dialog("open");
		}

		$(document).ready(
			function(){
				floatingRemind("isUseTd",getJsLocaleMessage('dxzs','dxzs_ssend_alert_147'));
				getLoginInfo("#hiddenValueDiv");
				$("#taskOwnerId").val("<%=taskOwner.getUserId()%>");
				//防止不点击修改人员
                var bsid = "<%=setup == null ? -1:setup.getId()%>";
                var lguserid = $("#lguserid").val();
                var lgcorpcode = $("#lgcorpcode").val();
                var skin = $("#skin").val();
                getBirthMembers(bsid, lguserid, lgcorpcode, skin);
				if("<%=updateRightFlag%>"!="true") {
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
					$("#signStr").val(getJsLocaleMessage('dxzs','dxzs_ssend_alert_141'));
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
				
				setSpUserContentMaxLen();
				$("#spUser").bind("change", function(){setSpUserContentMaxLen();});
				var skin=$("#skin").val();
				var _width = 535;
				if(skin.indexOf("frame4.0") != -1) {
					_width = 700;
				}
				$("#infoDiv").dialog({
					autoOpen: false,
					height:560,
					width: _width,
					resizable:false,
					modal: true,
					open:function(){
						$("#isSubmit").val("0");
					}
				});
			});
			
	
       	function reloadPage()
       	{
   			window.location.href = "<%=path%>/bir_birthdaySendEMP.htm?method=toUpdate&setupid="+<%=setup==null?"-1":setup.getId()%>+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
       	}

       	function getBirthMembers(bsid, ownerId, lgcorpcode, skin) {
			$.ajax({
				type : "post",
				url : "bir_birthdaySendEMP.htm",
				data : {
					method:"getBirthMembers",
					setupid:bsid,
					ownerId:ownerId,
					lgcorpcode:lgcorpcode
				},
				async : false,
				success: function(data) {
					var reg = /\[(\d+)人]$/;
					var empIds = "";
					var empDepIds = "";
					var empName = "";
					var empDepName = "";
					var manCount = 0;
					var optionStr = "";
					var array = eval("(" + data + ")");
					//遍历
					$(array).each(function () {
						var membertype = this['membertype'];
						if(membertype === 1){
							//员工
							empIds += this['memberId'] + ",";
							empName += this['addName'] + ",";
							manCount++;
							if(skin.indexOf("frame4.0") > -1){
								optionStr += "<option value=\""+ this['memberId'] +"\" isdep=\"4\" et=\"\" mobile=\""+ this['phone'] +"\">"+ this['addName'] +"</option>";
							}else {
								optionStr += "<option membertype=\"1\" value=\""+ this['memberId'] +"\">"+ this['addName'] +"</option>";
							}
						}else if(membertype === 2){
							//单机构
							empDepIds += this['memberId'] + ",";
							empDepName += this['addName'] + ",";
							this['addName'].match(reg);
							manCount += parseInt(RegExp.$1);
							if(skin.indexOf("frame4.0") > -1){
								optionStr += "<option value=\""+ this['memberId'] +"\" isdep=\"1\" et=\"1\" mobile=\"\" mcount=\""+parseInt(RegExp.$1)+"\">"+ this['addName'] +"</option>";
							}else {
								optionStr += "<option membertype=\"2\" value=\""+ this['memberId'] +"\" mcount=\""+ parseInt(RegExp.$1) +"\">"+ this['addName'] +"</option>";
							}
						}else if(membertype === 3){
							//包含子机构
							empDepIds += "e" + this['memberId'] + ",";
							empDepName += this['addName'] + ",";
							this['addName'].match(reg);
							manCount += parseInt(RegExp.$1);
							if(skin.indexOf("frame4.0") > -1){
								optionStr += "<option value=\""+ this['memberId'] +"\" isdep=\"1\" et=\"2\" mobile=\"\" mcount=\""+ parseInt(RegExp.$1) +"\">"+ this['addName'] +"</option>";
							}else {
								optionStr += "<option membertype=\"3\" value=\""+ this['memberId'] +"\" mcount=\""+ parseInt(RegExp.$1) +"\">"+ this['addName'] +"</option>";
							}
						}
					});
					$("#empDepIds").val(empDepIds);
					$("#empIds").val(empIds);
					$("#empDepName").val(empDepName);
					$("#empName").val(empName);
					$("#choiceNum").html(manCount);
					$("#rightSelectedUserOption").val(optionStr);
				}
			});
        }

    	function goback()
       	{
           	window.location.href = "<%=path%>/bir_birthdaySendEMP.htm?method=find&isback=1&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
       	}
       	function commitForm() {
       		if($("#isAddName").attr("checked") && $.trim($("#signStr").val())=="")
           	{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_148'));
				return;
            }
            if($.trim($("#contents").val())=="")
            {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_149'));
				return;
            }
            if(parseInt($('#strlen').html())>990)
            {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_150'));
				return;
            }

            if ($("#haveOne").val() === "0")
			{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_75'));
				return;
			}
            if($("#sendTime").val()=="")
            {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_151'));
				return;
            }
            if($("#spUser").val()=="" || $("#spUser").val() ==null)
            {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_152'));
				return;
            }
            $('#setupid').val('<%=setup==null?"-1":setup.getId()%>');
            var commitFlag = true;
       		$("#update").attr("disabled",true);
         	$("#qingkong").attr("disabled",true);
         	$("#close").attr("disabled",true);
         	var msg = $("#contents").val();
         	$.ajax({
         		url: "bir_birthdaySendEMP.htm", 
         		type : "POST",
         		data: {method : "checkBadWord1",tmMsg : msg,corpCode:$("#lgcorpcode").val()},
         		success: function(message) {
         			if(message.indexOf("@")==-1 || message.substr(message.indexOf("@")+1) == "error")
             		{
         				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_40'));
         				$("#update").attr("disabled","");
         				$("#qingkong").attr("disabled","");
         				$("#close").attr("disabled","");
         				return;
         			}
         			else if (message.substr(message.indexOf("@")+1)!="" && message.substr(message.indexOf("@")+1) !="error") 
             		{
         				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+"\n     " + message.substr(message.indexOf("@")+1) + "\n"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
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
         			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_40'));
         			$("#update").attr("disabled","");
         			$("#qingkong").attr("disabled","");
         			$("#close").attr("disabled","");
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
			if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_136')))
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
					$.post("bir_birthdaySendEMP.htm",{method:"deleteSetup",lgcorpcode:lgcorpcode,setupId:setupId,type:type},function(data)
					{
						if(data=="true")
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_137'));
							reloadPage();
						}else
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_138'));
							$("#deleteSetup").attr("disabled","");
						}
					});
			}
		
        }
    </script>
	</body>
</html>
