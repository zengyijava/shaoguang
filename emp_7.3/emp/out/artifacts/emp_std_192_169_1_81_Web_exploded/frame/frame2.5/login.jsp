<%@page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = (String)request.getContextPath();
String basePath =  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); 
String yanzhengma = (String)request.getAttribute("yanzhengma");
String tkn = (String)request.getAttribute("tkn");
String isMulti = (String)request.getAttribute("isMulti");
String frameUrl = SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME);
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String islogout = request.getParameter("islogout") == null ? "0":(String)request.getParameter("islogout");
if("1".equals(islogout))
{
	//session.removeAttribute("loginCorp");
	//session.removeAttribute("loginSysuser");
}

String logoPath = (String)request.getAttribute("logoPath");
if(logoPath == null || "".equals(logoPath)){
	logoPath = iPath+"/img/logo.png";
}else{
	logoPath = basePath + logoPath;
}
//获取配置允许登录的浏览器
String browser=SystemGlobals.getValue("browser");
//js提示
String alertStr= MessageUtils.extractMessage("common","common_frame2_login_1",request);
//根据配置的浏览器版本动态拼接提示
if(browser!=null)
{
	browser=browser.trim();
	if(browser.indexOf("IE6")!=-1)
	{
		alertStr=alertStr+"IE6,";
	}
	if(browser.indexOf("IE7")!=-1)
	{
		alertStr=alertStr+"IE7,";
	}
	if(browser.indexOf("IE8")!=-1)
	{
		alertStr=alertStr+"IE8,";
	}
	if(browser.indexOf("IE9")!=-1)
	{
		alertStr=alertStr+"IE9,";
	}
	if(browser.indexOf("IE10")!=-1)
	{
		alertStr=alertStr+"IE10,";
	}
	if(browser.indexOf("sogou")!=-1)
	{
		alertStr=alertStr+"sogou,";
	}
	//判断配置是否正确，不正确或未配置给出对应提示
	if(alertStr.indexOf(",")!=-1)
	{
		alertStr=alertStr.substring(0,alertStr.lastIndexOf(","));
		alertStr=alertStr+MessageUtils.extractMessage("common","common_frame2_login_2",request);
	}
	else
	{
		alertStr=MessageUtils.extractMessage("common","common_frame2_login_3",request);
	}
	
}
else
{
	alertStr=MessageUtils.extractMessage("common","common_frame2_login_3",request);
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
	<title>
		<emp:message key="common_frame2_login_5" defVal="欢迎访问企业移动信息平台" fileName="common"></emp:message>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="shortcut icon" href="<%=path %>/common/img/favicon.ico" type="image/x-icon" />
    <link href="<%=iPath %>/css/emp_login.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <script type="text/javascript">
	  var getContextPath="<%=request.getContextPath() %>";
	  var isIE10=<%=browser!=null && browser.indexOf("IE10")!=-1%>;
	  var isbrowser=<%=!"0".equals(browser)%>;
	  var alertStr='<%=alertStr%>';
	  var path="<%=path%>";
	  var iPath="<%=iPath%>";
	  var tkn="<%=tkn%>";
	</script>  
</head>
<body>
	<div class="header">
	  <h2 class="logo"><img src="<%=logoPath %>" alt="EMP"/></h2>
	  <ul class="nav">
	    <li><a href="javascript:void(0)" onclick="showAbout()"><emp:message key="common_aboutPlatform" defVal="关于平台" fileName="common"></emp:message></a></li>
		<li><span>|</span></li>
	    <li><a href="javascript:void(0)" onclick="AddFavorite(document.title,document.URL);"><emp:message key="common_frame2_login_7" defVal="收藏此页" fileName="common"></emp:message></a></li>
	  </ul>
	</div>
    <div class="container">
	  <div class="inner">
	    <div class="area">
		  <div id="emp_login">
			  <div id="web_login" class="web_login">
				<div id="tips" class="tips">
				<i class="notice_ico"></i>
				<span class="notice_text"></span>
				</div>
				<div class="login_form">
				  <form name="form1" method="post" action="<%=iPath%>/main.jsp" id="form1">
				    <input type="hidden" name="isShowYanzheng" id="isShowYanzheng" value="<%=yanzhengma %>"/>
			    	<input type="hidden" name="errorCount" id="errorCount" value="0"/>
			    	<input type="hidden" name="tkn" id="tkn" value=""/>
			    	<input type="hidden" name="loginparams" id="loginparams" value=""/>
			    	
			    	<input type="hidden" name="isMulti" id="isMulti" value="<%=isMulti %>"/>
			    	<input type="hidden" name="macAddr" id="macAddr">
			    	<input type="hidden" name="isOneLogin" id="isOneLogin" value=""/>
			    	<input type="hidden" name="remd" id="remd" value=""/>
	                <%
		             int inputHeight=51;
	                 String inpOuterHeight="style=height:"+inputHeight+"px";
	                 String verMarginTop="style=margin-top:10px"; 
	                 if ("true".equals(isMulti)){
		              %>
	                <div class="codeArea" id="codeArea" <%=inpOuterHeight %>>
					  <label for="enpCode" class="input_tips" id="enpCode_tips"><emp:message key="common_companyCode" defVal="企业编码" fileName="common"></emp:message></label>
					  <div class="inputOuter">
						<input id="enpCode" name="enpCode" class="inputstyle" type="text" maxlength="6" autocomplete='off'>
					  </div>
					</div>
					<%}else{
					   inputHeight=65;
					   inpOuterHeight="style=height:"+inputHeight+"px";
					   verMarginTop="";
					}
					%>
					<div class="uinArea" id="uinArea" <%=inpOuterHeight %>>
					  <label for="login_name" class="input_tips" id="login_name_tips"><emp:message key="common_username" defVal="用户名" fileName="common"></emp:message></label>
					  <div class="inputOuter">
						<input id="login_name" name="login_name" class="inputstyle" type="text" maxlength="15" autocomplete='off'>
					  </div>
					</div>
					<div class="pwdArea" id="pwdArea" <%=inpOuterHeight %>>
					  <label class="input_tips" id="login_pwd_tips" for="login_pwd"><emp:message key="common_password" defVal="密码" fileName="common"></emp:message></label>
					  <div class="inputOuter">
					   <input id="login_pwd" name="login_pwd" class="inputstyle password" type="password" value="">
					  </div> 
					</div>
					<%if ("true".equals(yanzhengma)) {%>
					<div class="verifyArea" id="verifyArea" <%=verMarginTop %>>
					  <label class="input_tips" id="code_tips" for="code"><emp:message key="common_verificationCode" defVal="验证码" fileName="common"></emp:message></label>
					  <div class="inputOuter">
					    <input name="code" type="text" id="code"  maxlength="4" autocomplete="off" class="onlook inputstyle">
					  </div>
					  <div class="verifyCode">
					    <img id="verifyImg" onclick="javascript:change()" title="<emp:message key="common_frame2_login_11" defVal="看不清楚？点击换一张！" fileName="common"></emp:message>" src="<%=request.getContextPath() %>/changeyzm" onclick="javascripte:change();">
					  </div>
					</div>
					<%}%>  
					<p class="forget_pwd"><a onclick="javascript:autoPass();" href="javascript:void(0)"><emp:message key="common_frame2_login_12" defVal="忘记密码了？" fileName="common"></emp:message></a></p>
					<div id="login_btn" class="login_btn" onclick="doLogin(this);">
					  <span><emp:message key="common_frame2_login_13" defVal="登 录" fileName="common"></emp:message></span>
					</div>
				  </form>
				</div>
			  </div>
			</div>  
		</div>
	  </div>
	  <div class="inner_left_bg">	
	  </div>
	</div>
	<div class="footer"><emp:message key="common_frame2_login_14" defVal="Copyright© 2013梦网科技 All rights reserved" fileName="common"></emp:message></div>
		
	
	<div id="autoPasswordDiv"   title="<emp:message key="common_frame2_login_15" defVal="自助重置密码" fileName="common"></emp:message>" style="padding:5px;display:none;">
			<input type="hidden" value="" id="autoGuId"/>
		<center>
			<table>
				<tr>
					<td style="padding-top: 5px;width:35%;font-size: 12px;text-align: left;">
						<emp:message key="common_username" defVal="用户名：" fileName="common"></emp:message>
					</td>
					<td style="padding-top: 5px;text-align: left;">
						<input type="text" style="height: 16px;font-size:12px;width: 205px;" id="autoUserName" size="20" maxlength="16" onblur="checkAutoUserPro();" value="" onkeyup = "checkText($(this))"/>
					</td>
				</tr>
				<tr>
						<td style="padding-top: 5px;font-size: 12px;text-align: left;">
							<emp:message key="common_name" defVal="姓名：" fileName="common"></emp:message>
						</td>
						<td  style="padding-top: 5px;text-align: left;">
							<input type="text"  style="height: 16px;font-size:12px;width: 205px;" id="autoName" size="20" maxlength="16" onblur="checkAutoUserPro();" value=""  onkeyup = "checkText($(this))"/>
						</td>
				</tr>
				<tr>
					<td style="padding-top: 5px;font-size: 12px;text-align: left;">
						<emp:message key="common_frame2_login_18" defVal="重置密码的原因：" fileName="common"></emp:message>
					</td>
					<td  style="padding-top: 5px;text-align: left;">
						<select id="autoReason" style="width: 210px;font-size: 12px;height: 22px;">
							<option value="1"><emp:message key="common_frame2_login_19" defVal="密码遗忘" fileName="common"></emp:message></option>
							<option value="2"><emp:message key="common_frame2_login_20" defVal="输错次数过多" fileName="common"></emp:message></option>
							<option value="3"><emp:message key="common_frame2_login_21" defVal="系统原因" fileName="common"></emp:message></option>
						</select>
					</td>
				</tr>
				
				
				<tr>
					<td style="padding-top: 5px;font-size: 12px;text-align: left;">
						<emp:message key="common_frame2_login_22" defVal="密码接收人：" fileName="common"></emp:message>
					</td>
					<td  style="padding-top: 5px;text-align: left;">
						<select id="autoPassUsers" style="width: 210px;font-size: 12px;height: 22px;">
								<option value=""><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message></option>
						</select>
					</td>
				</tr>
				
		</table>
		</center>
	</div>
	<input type="hidden" id = "corptype" value="<%=isMulti%>">
	<div id="dynphoneword" title="<emp:message key="common_frame2_login_23" defVal="短信动态口令" fileName="common"></emp:message>" style="padding:5px;display:none;font-size: 12px;">
	<center>
	<table width="100%" >	
	<tr>
	  <td colspan="3" height="15px;"></td>
	</tr>		
	<tr>
	 <td colspan="3" align="center"><emp:message key="common_frame2_login_24" defVal="动态口令 ：" fileName="common"></emp:message><input type="text" id="phonewords" name="phonewrods" value="" class="input_bd" style="width: 110px;"/>
	<input type="button" value="  <emp:message key="common_acquire" defVal="获取" fileName="common"></emp:message>  "  id="getphoneword" name="getphoneword" onclick="GetPhoneWord()" class="btnClass5" /></td>
	</tr>
	<tr>
	 <td colspan="3" height="5px;"></td>
	</tr> 
	<tr>
	   <td colspan="3" align="center"><label id="sendsucess" style="display: block;"><font color="red">*<emp:message key="common_frame2_login_26" defVal="点击获取按钮获取动态口令" fileName="common"></emp:message></font></label></td>
	</tr>
	<tr>
	   <td colspan="3" align="center"><label id="sendtime" style="display: none;"><font color="red">*<emp:message key="common_frame2_login_27" defVal="30秒后能重新获取动态口令" fileName="common"></emp:message></font></label></td>
	</tr>

	</table>
	</center>
</div>
	<%--等待旋转--%>
	<div id="probar" style="display: none">
		<center>
			<p style="padding-top: 8px;font-size: 12px;">
				<emp:message key="common_frame2_login_28" defVal="处理中,请稍等....." fileName="common"></emp:message>
			</p>
		</center>
	</div>
	<script type="text/javascript" src="<%=path %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript">
	    //判断一下是否是用ie6,ie7,ie8,360,世界之窗  浏览器登录系统
		var isIE = (document.all) ? true : false;
        var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false) && <%=browser!=null && browser.indexOf("IE6")!=-1%>;
		var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0" ? true : false) && <%=browser!=null && browser.indexOf("IE7")!=-1%>;
		var isIE8 = isIE && (navigator.appVersion.match(/MSIE 8.0/i)=="MSIE 8.0" ? true : false) && <%=browser!=null && browser.indexOf("IE8")!=-1%>;
		var isIE9 = isIE && (navigator.appVersion.match(/MSIE 9.0/i)=="MSIE 9.0" ? true : false) && <%=browser!=null && browser.indexOf("IE9")!=-1%>;		
		var is360 = isIE && ((navigator.userAgent).indexOf('360SE')>0) && <%=browser!=null && browser.indexOf("360")!=-1%>;// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) 360SE
		//var isSG = isIE && ((navigator.userAgent).indexOf('SE')>0);// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)SE 2.X
		//搜狗
		var userAgent = navigator.userAgent.toLowerCase(); 
		var SG =(userAgent.indexOf('se 2.x') != -1);
		var isSG =  (userAgent.indexOf('se 2.x') != -1) && <%=browser!=null && browser.indexOf("sogou")!=-1%>;// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)SE 2.X
		var isTheWorld =isIE && ((navigator.userAgent).toLowerCase().indexOf('theworld')>0) && <%=browser!=null && browser.indexOf("theworld")!=-1%>;//世界之窗
		//alert(navigator.appVersion+"6:"+isIE6+"7:"+isIE7+"8:"+isIE8+"9:"+isIE9);

		var appn = navigator.appName;
		var iWidth = 506;
		var iHeight = 247;
	
		if (isIE6) {
			iWidth = 510;
			iHeight = 270;
		}
		if (appn == "Netscape") {
			iWidth = 506;
			iHeight = 247;
		}
				//<!--检测用户名 /名字  的属性验证-->
		function checkAutoUserPro(){
			var autoUserName = $("#autoUserName").val();
			var autoName = $("#autoName").val();
			if(autoUserName != null && autoUserName != "" && autoName != null && autoName != ""){
				$.post("<%=request.getContextPath() %>/getphoneword",{method:"getAutoUser",autoUserName:autoUserName,autoName:autoName},function(returnMsg){
						if(returnMsg == "tingyong"){
							alert(getJsLocaleMessage("common","common_frame2_login_29"));
							return;
						}else{
							$("#autoPassUsers").empty();
							if(returnMsg != null && returnMsg != ""){
								$("#autoPassUsers").html(returnMsg);
							}
						}
		    	});
			}else{
				$("#autoPassUsers").empty();
				$("#autoPassUsers").html("<option value=''>"+getJsLocaleMessage("common","common_pleaseSelect")+"</option>")
			}
		};
	</script>
	<script type="text/javascript" event=OnObjectReady(objObject,objAsyncContext) for=foo>
	//异步调用
	if (objObject.IPEnabled != null && objObject.IPEnabled != "undefined"
			&& objObject.IPEnabled == true) {
		if (objObject.MACAddress != null && objObject.MACAddress != "undefined") {
			MACAddr = objObject.MACAddress;
		}
		mac_arr[mac_arr.length] = MACAddr;
	}
	</script>
	
	<script type="text/javascript" event="OnCompleted(hResult,pErrorObject, pAsyncContext)" for=foo>
		var mac_str = '';
		for ( var i = 0; i < mac_arr.length; i++) {
			mac_str += mac_arr[i] + ",";
		}
		if(mac_str !=''){
			mac_str = mac_str.substring(0,mac_str.lastIndexOf(","));
			$("#macAddr").val(mac_str);
		}
		//document.getElementById("mac").innerHTML=mac_str;
	</script>
	<script type="text/javascript" src="<%=iPath %>/js/login.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/encode.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>

</html>

