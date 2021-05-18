<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.common.constant.SystemGlobals" %>
<%@page import="java.util.Calendar" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%
    String langs = SystemGlobals.getValue("selectedLanguage") == null ? "" : SystemGlobals.getValue("selectedLanguage");
    String multiLanguageEnable = SystemGlobals.getValue("multiLanguageEnable");
    String defaultLanguage = SystemGlobals.getValue("defaultLanguage");
    String path = (String) request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    String yanzhengma = (String) request.getAttribute("yanzhengma");
    String tkn = (String) request.getAttribute("tkn");
    String isMulti = (String) request.getAttribute("isMulti");
    String frameUrl = SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME);
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String islogout = request.getParameter("islogout") == null ? "0" : (String) request.getParameter("islogout");
    if ("1".equals(islogout)) {
        //session.removeAttribute("loginCorp");
        //session.removeAttribute("loginSysuser");
    }

    String logoPath = (String) request.getAttribute("logoPath");
    if (logoPath == null || "".equals(logoPath)) {
        logoPath = iPath + "/img/logo.png";
    } else {
        logoPath = basePath + logoPath;
    }
//获取配置允许登录的浏览器
    String browser = SystemGlobals.getValue("browser");
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title id="title"><emp:message key="common_frame2_login_5" defVal="欢迎访问企业移动信息平台"
                                   fileName="common"></emp:message></title>
    <link rel="shortcut icon" href="<%=path %>/common/img/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="<%=iPath %>/css/emplogin20131116.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet"
          href="<%=path %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <style>
        #login-loading {
            display: none;
            position: absolute;
            bottom: 7px;
            left: 26px;
            width: 243px;
            height: 39px;
            z-index: 999;
            -moz-opacity: 0.4;
            opacity: .40;
            filter: alpha(opacity=40);
            background: url('<%=commonPath %>/common/img/login-loading.gif') no-repeat center
        }
    </style>
    <!--[if IE 6]>
    <script type="text/javascript" src="<%=iPath %>/js/DD_belatedPNG.js"></script>
    <script language="javascript" type="text/javascript">
        DD_belatedPNG.fix(".png,.png img,.login_btn_bg");
    </script>
    <![endif]-->

    <script type="text/javascript">
        var langKeyJs = "<%=StaticValue.LANG_KEY%>";
        var browser = "<%=browser%>";
        var commonPath = "<%=commonPath %>";
    </script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript"
            src="<%=iPath %>/js/loginDependence.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript">
        //js提示
        var getContextPath = "<%=request.getContextPath() %>";
        var isIE10 =<%=browser!=null && browser.indexOf("IE10")!=-1%>;
        var isbrowser =<%=!"0".equals(browser)%>;
        var path = "<%=path%>";
        var iPath = "<%=iPath%>";
        var tkn = "<%=tkn%>";
        var logoPath = "<%=logoPath %>";
        var cright = "Copyright©" + " <%= Calendar.getInstance().get(Calendar.YEAR)%> " + copyright;
    </script>
</head>
<body>
<div id="object-div" style="display:none;">
</div>
<script language="JScript">
    function isIE() {
        if (!!window.ActiveXObject || "ActiveXObject" in window)
            return true;
        else
            return false;
    }

    var ipAddr = "";
    var macAddr = "";
    if (document.all && !!window.ActiveXObject) {
        try {
            var locator1 = new ActiveXObject("WbemScripting.SWbemLocator");
            var obj;
            if (locator1) {
                obj = GetAdapterInfo(locator1);
            }
            if (obj) {
                ipAddr = obj.ip.join(',');
                macAddr = obj.mac;
            }
        } catch (e) {

        }
    } else if (isIE()) {
        document.getElementById("object-div").innerHTML = "<object id=locator classid=CLSID:76A64158-CB41-11D1-8B02-00600806D9B6 VIEWASTEXT></object>" +
            "<object id=foo classid=CLSID:75718C9A-F029-11d1-A1AC-00C04FB6C223></object>";
        var service = locator.ConnectServer();
        service.Security_.ImpersonationLevel = 3;
        service.InstancesOfAsync(foo, 'Win32_NetworkAdapterConfiguration');
    }

    function GetAdapterInfo(locator) {
        var service = locator.ConnectServer("."); //连接本机服务器
        var properties = service.ExecQuery("SELECT * FROM Win32_NetworkAdapterConfiguration");
        //查询使用SQL标准
        var e = new Enumerator(properties);
        var ip, mac, tmp;
        ip = [];
        for (; !e.atEnd(); e.moveNext()) {
            var p = e.item();
            if (p.MACAddress && p.IPAddress != null) {
                tmp = p.IPAddress.toArray();
                mac = p.MACAddress;
            }
        }
        for (var i = 0; i < tmp.length; i++) {
            var t = tmp[i];
            if (t.indexOf(':') == -1) {
                ip.push(t);
            }
        }
        return {'ip': ip, 'mac': mac};
    }

</script>
<script language="JScript" event="OnObjectReady(objObject,objAsyncContext)" for="foo">
    if (objObject.IPEnabled != null && objObject.IPEnabled != "undefined" && objObject.IPEnabled == true) {
        if (objObject.MACAddress != null && objObject.MACAddress != "undefined") {
            macAddr = objObject.MACAddress;
        }
        if (objObject.IPEnabled && objObject.IPAddress(0) != null && objObject.IPAddress(0) != "undefined") {
            var ipAddrArray = objObject.IPAddress.toArray();
            var tmp = [];
            for (var i = 0; i < ipAddrArray.length; i++) {
                if (ipAddrArray[i].indexOf(":") == -1) {
                    tmp.push(ipAddrArray[i]);
                }
            }
            ipAddr = tmp.join(',');
        }
    }

</script>
<div class="header">
    <div class="area">
        <div class="logo"></div>
        <div class="hd_link">
            <input type="hidden" id="langs" value="<%=langs%>">
            <input type="hidden" id="multiLanguageEnable" value="<%=multiLanguageEnable%>">
            <input type="hidden" id="defaultLanguage" value="<%=defaultLanguage%>">
            <%
                String[] langArr = langs.split(",");
                if ("Yes".equals(multiLanguageEnable) && langArr.length != 0) {
                    System.out.println();
                    if (langArr.length == 1) {//只有一个元素则页面不显示
            %>
            <select name="langName" id="langName" style="display: none">
                <option value="<%=langArr[0]%>" selected="selected"></option>
            </select>
            <%} else {%>
            <span id="langSeprator">|</span>
            <select name="langName" id="langName" onchange="javascript:changeLang();">
                <%
                    for (String str : langArr) {
                        String langName = "zh_HK".equals(str) ? "English" : "zh_TW".equals(str) ? "繁體" : "简体";
                %>
                <option value="<%=str%>"><%=langName%>
                </option>
                <%
                        }
                    }
                %>
            </select>
            <%} else {%>
            <%--加入这个select是为了不让loginDependence.js的changeLang方法报错--%>
            <select name="langName" id="langName" style="display: none">
                <option value="<%=defaultLanguage%>" selected="selected"></option>
            </select>
            <%}%>
        </div>
    </div>
</div>
<div class="banner_area" id="banner_list">
    <div class="login_box">
        <div class="login_cont png" id="bgPng">
            <div style="width: 272px;height: 26px;font-size:26px;line-height: 26px;">
                <div style="width:5px;height:26px;background:#04AD72;float:left;"></div>
                <div style="color:#585C66;margin-left:14px;float:left;" id="loginValue">登录</div>
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
                    <input type="hidden" id="path" value="<%=path %>"/>
                    <input type="hidden" name="CORPTYPE" id="CORPTYPE" value="<%=StaticValue.getCORPTYPE() %>"/>
                    <%
                        String userClass = "userClass";
                        String pwdClass = "pwdClass";
                        String doLogin = "doLogin";
                        if ("true".equals(isMulti)) {
                    %>
                    <div class="loginFormInp login_btn_bg" id="codeArea" tabindex="-1">
                        <b class="ico ico-code png"></b>
                        <input type="text" name="enpCode" id="enpCode" class="loginFormTdIpt" maxlength="6"
                               autocomplete="off">
                        <label for="enpCode" class="placeholder" id="enPlaceholder"><emp:message
                                key="common_companyCode" defVal="企业编码" fileName="common"></emp:message></label>
                    </div>
                    <%
                        } else {

                        }
                    %>
                    <div class="loginFormInp login_btn_bg <%=userClass%>" id="uinArea" tabindex="-1">
                        <b class="ico ico-uid png"></b>
                        <input type="text" name="login_name" id="login_name" class="loginFormTdIpt" autocomplete="off">
                        <label for="login_name" class="placeholder" id="namePlaceholder"><emp:message
                                key="common_username" defVal="用户名" fileName="common"></emp:message></label>
                    </div>
                    <div class="loginFormInp login_btn_bg <%=pwdClass%>" id="pwdArea" tabindex="-1">
                        <b class="ico ico-pwd png"></b>
                        <input type="password" name="login_pwd" id="login_pwd" class="loginFormTdIpt"
                               autocomplete="off">
                        <label for="login_pwd" class="placeholder" id="pwdPlaceholder"><emp:message
                                key="common_password" defVal="密码" fileName="common"></emp:message></label>
                    </div>
                    <%if ("true".equals(yanzhengma)) {%>
                    <div class="verify login_btn_bg" id="verifyArea" tabindex="-1">
                        <div class="sInp">
                            <input type="text" name="code" id="code" class="loginFormTdIpt" maxlength="4"
                                   autocomplete="off">
                            <label for="code" class="placeholder" id="codePlaceholder"><emp:message
                                    key="common_verificationCode" defVal="验证码" fileName="common"></emp:message></label>
                            <img id="verifyImg" onclick="javascript:change()"
                                 title="<emp:message key="common_frame2_login_11" defVal="看不清楚？点击换一张！" fileName="common"></emp:message>"
                                 src="<%=request.getContextPath() %>/changeyzm" onclick="javascripte:change();"
                                 width="106" height="42">
                        </div>
                    </div>
                    <%}%>
                    <%
                        if (!"true".equals(isMulti)) {%>
                    <p class="forget_pwd"><a onclick="javascript:autoPass();" href="javascript:void(0)"
                                             id="getPwd"><emp:message key="common_frame2_login_12" defVal="忘记密码了？"
                                                                      fileName="common"></emp:message></a></p>
                    <%
                        }
                    %>
                    <input type="hidden" name="corpCode" id="corpCode" value="100001"/><%-- 托管版上页面要增加公司编码 --%>
                    <a id="loginSub" class="loginSub login_btn_bg <%=doLogin%>"><emp:message
                            key="common_frame2_login_13" defVal="登 录" fileName="common"></emp:message></a>
                    <div id="login-loading"></div>
                </form>
            </div>
        </div>
        <input type="hidden" id="corptype" value="<%=isMulti%>">
    </div>
    <div class="user_defined" style="display:none;">
        <img width="100%" src="">
    </div>
    <div class="banner_box banner_ui">
        <div class="ban1 anim png">
            <div class="banner_cont">
                <div class="area ">
                    <%-- <div class="ban1_anim_chart png an0"></div>
                    <div class="ban1_anim_bg png an0"></div>
                    <div class="ban1_anim_bg2 png an0"></div> --%>
                </div>
            </div>
        </div>
    </div>
    <div class="banner_box banner_red">
        <div class="ban2">
            <div class="banner_cont">
                <div class="area ">
                    <div id="mod_blo">
                        <ul>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a1.jpg" alt=""></li>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a2.jpg" alt=""></li>
                            <li></li>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a4.jpg" alt=""></li>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a5.jpg" alt=""></li>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a6.jpg" alt=""></li>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a7.jpg" alt=""></li>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a8.jpg" alt=""></li>
                            <li class="an1"><img src="<%=iPath %>/img/20131116/ban2/a9.jpg" alt=""></li>
                        </ul>
                    </div>
                    <div class="mod_text_1 png an1"></div>
                    <div class="mod_text_2 png an1"></div>
                    <div id="mode_ico" class="png an1">
                        <ul>
                            <li class="m1 an1"><img src="<%=iPath %>/img/20131116/ban2/01.png" alt=""></li>
                            <li class="m2 an1"><img src="<%=iPath %>/img/20131116/ban2/02.png" alt=""></li>
                            <li class="m3 an1"><img src="<%=iPath %>/img/20131116/ban2/03.png" alt=""></li>
                            <li class="m4 an1"><img src="<%=iPath %>/img/20131116/ban2/04.png" alt=""></li>
                            <li class="m5 an1"><img src="<%=iPath %>/img/20131116/ban2/05.png" alt=""></li>
                            <li class="m6 an1"><img src="<%=iPath %>/img/20131116/ban2/06.png" alt=""></li>
                            <li class="m7 an1"><img src="<%=iPath %>/img/20131116/ban2/07.png" alt=""></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="banner_box banner_blue">
        <div class="ban3 ">
            <div class="banner_cont png">
                <div class="area ">
                    <div class="showPic pc1 an2">
                        <img src="<%=iPath %>/img/20131116/ban3/pc1.png" alt="">
                    </div>
                    <div class="showPic pc2 an2">
                        <img src="<%=iPath %>/img/20131116/ban3/pc2.png" alt="">
                    </div>
                    <div class="showPic pc3 an2">
                        <img src="<%=iPath %>/img/20131116/ban3/pc2.png" alt="">
                    </div>
                    <div class="showPic pc4 an2">
                        <img src="<%=iPath %>/img/20131116/ban3/pc.png" alt="">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="switch">
        <div class="switch_cont">
            <a id="prev" class="png"></a>
            <a id="next" class="png"></a>
        </div>
    </div>

</div>

<div id="cright" class="footer"></div>

<div id="dynphoneword"
     title="<emp:message key="common_frame2_login_23" defVal="短信动态口令" fileName="common"></emp:message>"
     style="padding:5px;display:none;font-size: 12px;">
    <center>
        <table width="100%" height="165px">
            <tr>
                <td colspan="3" height="15px;"></td>
            </tr>
            <tr>
                <td colspan="3" align="center"><span id="dynCommand"><emp:message key="common_frame2_login_24"
                                                                                  defVal="动态口令 ："
                                                                                  fileName="common"></emp:message></span>
                    ：<input type="text" id="phonewords" name="phonewrods" value="" class="input_bd"
                            style="width: 110px;"/>
                    <input type="button"
                           value="  <emp:message key="common_acquire" defVal="获取" fileName="common"></emp:message>  "
                           id="getphoneword" name="getphoneword" onclick="GetPhoneWord()" class="btnClass5"/></td>
            </tr>
            <tr>
                <td colspan="3" height="5px;"></td>
            </tr>
            <tr>
                <td colspan="3" align="center"><label id="sendsucess" style="display: block;"><font
                        color="red">*<emp:message key="common_frame2_login_26" defVal="点击获取按钮获取动态口令"
                                                  fileName="common"></emp:message></font></label></td>
            </tr>
            <tr>
                <td colspan="3" align="center"><label id="sendtime" style="display: none;"><font
                        color="red">*<emp:message key="common_frame2_login_27" defVal="30秒后能重新获取动态口令"
                                                  fileName="common"></emp:message></font></label></td>
            </tr>
            <tr>
            </tr>
        </table>
    </center>
</div>

<div id="autoPasswordDiv"
     title="<emp:message key="common_frame2_login_15" defVal="自助重置密码" fileName="common"></emp:message>"
     style="padding:5px;display:none;">
    <input type="hidden" value="" id="autoGuId"/>
    <center>
        <table>
            <tr>
                <td id="tdAutoUserName" style="padding-top: 5px;width:35%;font-size: 12px;text-align: left;">
                    <emp:message key="common_username" defVal="用户名：" fileName="common"></emp:message>
                </td>
                <td style="padding-top: 5px;text-align: left;">
                    <input type="text" style="height: 16px;font-size:12px;width: 205px;" id="autoUserName" size="20"
                           maxlength="16" onblur="checkAutoUserPro();" value="" onkeyup="checkText($(this))"/>
                </td>
            </tr>
            <tr>
                <td id="tdAutoName" style="padding-top: 5px;font-size: 12px;text-align: left;">
                    <emp:message key="common_name" defVal="姓名：" fileName="common"></emp:message>
                </td>
                <td style="padding-top: 5px;text-align: left;">
                    <input type="text" style="height: 16px;font-size:12px;width: 205px;" id="autoName" size="20"
                           maxlength="16" onblur="checkAutoUserPro();" value="" onkeyup="checkText($(this))"/>
                </td>
            </tr>
            <tr>
                <td id="resetReason" style="padding-top: 5px;font-size: 12px;text-align: left;">
                    <emp:message key="common_frame2_login_18" defVal="重置密码的原因：" fileName="common"></emp:message>
                </td>
                <td style="padding-top: 5px;text-align: left;">
                    <select id="autoReason" style="width: 210px;font-size: 12px;height: 22px;">
                        <option value="1"><emp:message key="common_frame2_login_19" defVal="密码遗忘"
                                                       fileName="common"></emp:message></option>
                        <option value="2"><emp:message key="common_frame2_login_20" defVal="输错次数过多"
                                                       fileName="common"></emp:message></option>
                        <option value="3"><emp:message key="common_frame2_login_21" defVal="系统原因"
                                                       fileName="common"></emp:message></option>
                    </select>
                </td>
            </tr>


            <tr>
                <td id="pwdReceiver" style="padding-top: 5px;font-size: 12px;text-align: left;">
                    <emp:message key="common_frame2_login_22" defVal="密码接收人：" fileName="common"></emp:message>
                </td>
                <td style="padding-top: 5px;text-align: left;">
                    <select id="autoPassUsers" style="width: 210px;font-size: 12px;height: 22px;">
                        <option value=""><emp:message key="common_pleaseSelect" defVal="请选择"
                                                      fileName="common"></emp:message></option>
                    </select>
                </td>
            </tr>

        </table>
    </center>
</div>
<%--等待旋转--%>
<div id="probar" style="display: none">
    <center>
        <p id="probarContent" style="padding-top: 8px;font-size: 12px;">
            <emp:message key="common_frame2_login_28" defVal="处理中,请稍等....." fileName="common"></emp:message>
        </p>
    </center>
</div>
<div id="errorAlert">
    <div class="error-hd"></div>
    <div class="error-mid">
        <div class="error-tt">
            <p id="errorTitle"></p>
        </div>

    </div>
    <div class="error-ft"></div>
    <div id="errorArr" class="error-arrow" style="top: 25px;"></div>
</div>
<div style="display:none;overflow: hidden" id="aboutDiv">
    <iframe src="" id="aboutFrame" border="0" frameborder="0" width="100%" height="100%"></iframe>
</div>
<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=path%>/common/widget/artDialog/artDialog.js?skin=default"></script>
<script type="text/javascript"
        src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=iPath%>/js/slide.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=iPath%>/js/cookieUtil.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=path%>/common/js/specicfg.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=path %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
    //判断一下是否是用ie6,ie7,ie8,360,世界之窗  浏览器登录系统
    var isIE = (document.all) ? true : false;
    var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i) == "MSIE 6.0" ? true : false) && <%=browser!=null && browser.indexOf("IE6")!=-1%>;
    var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i) == "MSIE 7.0" ? true : false) && <%=browser!=null && browser.indexOf("IE7")!=-1%>;
    var isIE8 = isIE && (navigator.appVersion.match(/MSIE 8.0/i) == "MSIE 8.0" ? true : false) && <%=browser!=null && browser.indexOf("IE8")!=-1%>;
    var isIE9 = isIE && (navigator.appVersion.match(/MSIE 9.0/i) == "MSIE 9.0" ? true : false) && <%=browser!=null && browser.indexOf("IE9")!=-1%>;
    var is360 = isIE && ((navigator.userAgent).indexOf('360SE') > 0) && <%=browser!=null && browser.indexOf("360")!=-1%>;
    //搜狗
    var userAgent = navigator.userAgent.toLowerCase();
    var SG = (userAgent.indexOf('se 2.x') != -1);
    var isSG = (userAgent.indexOf('se 2.x') != -1) && <%=browser!=null && browser.indexOf("sogou")!=-1%>;// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)SE 2.X
    var isTheWorld = isIE && ((navigator.userAgent).toLowerCase().indexOf('theworld') > 0) && <%=browser!=null && browser.indexOf("theworld")!=-1%>;//世界之窗

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
    //<%--检测用户名 /名字  的属性验证--%>

    function checkAutoUserPro() {
        var autoUserName = $("#autoUserName").val();
        var autoName = $("#autoName").val();
        if (autoUserName != null && autoUserName != "" && autoName != null && autoName != "") {
            $.post("<%=request.getContextPath() %>/getphoneword", {
                method: "getAutoUser",
                autoUserName: autoUserName,
                autoName: autoName
            }, function (returnMsg) {
                if (returnMsg == "tingyong") {
                    alert(tingyong);
                    return;
                } else {
                    $("#autoPassUsers").empty();
                    if (returnMsg != null && returnMsg != "") {
                        $("#autoPassUsers").html(returnMsg);
                    }
                }
            });
        } else {
            $("#autoPassUsers").empty();
            $("#autoPassUsers").html("<option value=''>" + autoPassUsers0 + "</option>");
        }
    };
</script>
<script type="text/javascript" src="<%=iPath %>/js/login.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath %>/js/encode.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>	