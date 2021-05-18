<%@page import="com.montnets.emp.entity.sysuser.LfPrivilege"%>
<%@page import="com.montnets.emp.entity.system.LfThiMenuControl"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.LoginInfo"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.entity.notice.LfNotice"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
@SuppressWarnings("unchecked")
List<String> langs = (List)session.getAttribute("multiLanguage");
String multiLanguageEnable = SystemGlobals.getValue("multiLanguageEnable")==null?"No":SystemGlobals.getValue("multiLanguageEnable");
String path = request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String tkn = request.getParameter("tkn");
String cursessionid = session.getId();
String passWordLimt=(String)session.getAttribute("passWordLimt");
if(passWordLimt != null && "密码6位以上且含字母、数字".equals(passWordLimt)){
    passWordLimt = MessageUtils.extractMessage("common","common_frame2_main_68",request);
}else{
    passWordLimt = "";
}
String passOverdue = (String)session.getAttribute("passOverdue");
String loginparams= (String) session.getAttribute("loginparams");
String[] loginparamArr = loginparams.split("&");
String remd= loginparamArr[4];
//用于提示多少天
String params=loginparamArr[0];
Map<String,LoginInfo> loginMap = StaticValue.getLoginInfoMap();
String isOneLogin = (String)session.getAttribute("NameAndPWord");
String setNoTip = (String)session.getAttribute("setNoTip");
Object sysNoticeObj = session.getAttribute("sysNotice");
String depName = (String) session.getAttribute("depName");
String noticeTxt = null;
if(sysNoticeObj != null){
        LfNotice sysNotice = (LfNotice)sysNoticeObj;
        long validTime = sysNotice.getNoteValid();
        validTime = validTime*24*60*60*1000;
        long valid = sysNotice.getPublishTime().getTime() + validTime;
        boolean isValid = valid > System.currentTimeMillis();
        if(sysNotice.getNoteState() == 1 && isValid && setNoTip==null){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sysNotice.getPublishTime().getTime());
            noticeTxt="<h2 style='font-weight:bold;font-size:22px;font-family:微软雅黑;text-align:center;'>"+sysNotice.getTitle()+"</h2>"+
                    "<p style='text-indent:2em;margin-top:40px;'>"+sysNotice.getContext()+"</p>"+
                    "<p class='p3'  style='margin-top:60px;text-align:right;font-family:微软雅黑;font-size:14px;'>"+sysNotice.getNoteTail()+"</p>"+
                    "<p class='p3' style='text-align:right;font-family:微软雅黑;font-size:14px;'>"+cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月"+cal.get(Calendar.DAY_OF_MONTH)+"日</p>";
            noticeTxt = noticeTxt.replace("\"","&quot;");
        }
    }

    String Logindays="";
    if(session.getAttribute("Logindays")!=null){
        Logindays=(String)session.getAttribute("Logindays");
    }
//= request.getParameter("isOneLogin");
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");
    @SuppressWarnings("unchecked")
	Map<Integer, String> thirdMenuMap = (Map<Integer, String>) session.getAttribute("thirdMenuMap");
    @SuppressWarnings("unchecked")
    List<LfThiMenuControl> thirdMenuList = (List<LfThiMenuControl>)session.getAttribute("thirdMenuList");
    @SuppressWarnings("unchecked")
    Map<String,List<LfPrivilege>> menuMap = (Map<String,List<LfPrivilege>>)session.getAttribute("priMap");
//是否显示运营商余额
boolean isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? false : true;
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String version = StaticValue.getEmpVersion();
//是否集群
    int ISCLUSTER = StaticValue.getISCLUSTER();
    String frameCode = (String)session.getAttribute(StaticValue.EMP_WEB_FRAME);
    if(tkn == null || loginMap.get(tkn) == null ||
            !loginMap.get(tkn).checkSessionId(cursessionid)){
        //使用EMP服务器URL
        if(StaticValue.getUseServerUrlFlag() - 1 == 0)
        {
            //请求HOST
            String host = ","+request.getHeader("Host")+",";
            //EMP服务器URL
            String empServerUrl = StaticValue.getServerUrl();
            //未配置EMP服务器URL
            if(empServerUrl == null || empServerUrl.trim().length() < 1)
            {
                EmpExecutionContext.error(MessageUtils.extractMessage("common","common_frame2_main_1",request)+host+"，empServerUrl:"+empServerUrl);
                return;
            }
            //请求HOST在配置的EMP服务器URL中不存在，直接返回
            if(empServerUrl.indexOf(host) < 0)
            {
                EmpExecutionContext.error(MessageUtils.extractMessage("common","common_frame2_main_2",request)+host+"，empServerUrl:"+empServerUrl);
                return;
            }
        }
        response.sendRedirect(path+"/common/logoutEmp.jsp");
        return;
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
    <head>
    <%@include file="/common/common.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><emp:message key="common_frame2_contacts_1" defVal="企业移动信息平台" fileName="common"></emp:message></title>
    <jsp:include page="/common/commonfile.jsp"></jsp:include>
    <link rel="icon" href="<%=path%>/common/img/favicon.ico" mce_href="<%=path%>/common/img/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="<%=path%>/common/img/favicon.ico" type="image/x-icon" />
    <link rel="bookmark" href="<%=path%>/common/img/favicon.ico" type="image/x-icon"/>
    <link href="<%=iPath %>/css/main.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=path %>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/main.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" type="text/css" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/showbox.css?V=<%=StaticValue.getJspImpVersion() %>">
    <style type="text/css">
        .ui-widget-header {
            border-bottom:none;
            border-bottom-left-radius:0;
            border-bottom-right-radius:0;
        }
        .fontsel {
            background:url(img/sel.gif) no-repeat 0 0;
        }
        .classsic_change .themeSpan {
            background:url(img/icon_tradition.png) no-repeat 15px 50%;
        }
        .tradition_change .themeSpan {
            background:url(img/icon_classic.png) no-repeat 15px 50%;
        }
        .classsic_change .fontsel {
            background:url(img/icon_tradition_sel.png) no-repeat 15px 50%;
        }
        .tradition_change .fontsel {
            background:url(img/icon_classic_sel.png) no-repeat 15px 50%;
        }
        .simple_change  .themeSpan{background:url(img/icon_classic.png) no-repeat 15px 50%;}
   	    .simple_change  .fontsel{background:url(img/icon_tradition_sel.png) no-repeat 15px 50%;}
        #themeDiv .selected {
            background:url(img/sel.gif) no-repeat 0 0;
        }
        .p1,.p2,.p3{line-height:22px;font-family:"微软雅黑";}
        .p1{font-weight:bold;font-size:16px;}
        .p2{font-size:14px;}
        .p3{text-align:right;font-size:14px;}
        #jkBox
        {
            border:1px solid #ccc;width: 200px;position: fixed;*position:absolute;right: 5px;font-size: 14px;
            bottom: 8px;color: #f07171;
            z-index: 1000;
            background-color: #fff;
            box-shadow:0 0 10px #ccc;
        }
        #jkBox .toolbar{
            height: 29px;
            line-height: 29px;
            color: #000;
            font-weight: 700;
            border-bottom: 1px solid #ccc;
            background:url(img/monImg/mon_warning_bg.gif) repeat-x 0 0;
            padding-left: 5px;

        }
        #jkBox .toolbar .tool_warn{
            float: right;
            height:29px;
        }
        #jkBox .toolbar .tool_warn span{
            width: 13px;
            height: 29px;
            display: inline-block;
            *display:inline;
            *zoom:1;
            margin-right: 8px;
            cursor: pointer;
        }
        #jkBox .toolbar span.tomini{
            background: url(img/monImg/tomini.png) no-repeat 0 50%;
        }
        #jkBox .toolbar span.winMax{
            background: url(img/monImg/mon_max.png) no-repeat 0 50%;
        }
        #jkBox .toolbar span.winClose{
            background: url(img/monImg/mon_close.png) no-repeat 0 50%;

        }
        #jkBox  .bd{
            padding: 10px;
            height:180px;
            overflow-y:auto;
            font-size:12px;
            width: 180px;
        }
        #jkBox .bd dd{
            line-height: 20px;
        }
        #jkBox .bd dl{
            margin-bottom:5px;
        }
        #jkBox .bd dt{
            background:url(img/monImg/dian.gif) no-repeat 0 50%;
            padding-left:10px;
            color:#555;
        }
        input#new_pass1,input#pass2，input#new_pass2{
            width:215px;
        }
        
        
        
        
        
        
        
        
        
        
        .top-nav-div{
	        width:100%;
	        height:60px;
	        position: relative;
	        box-shadow: 5px 0px 5px rgba(0,0,0,0.1);
        }
        
		
		
		
    </style>
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <style type="text/css">
        #updateinfoDiv table tr td{
            padding-left: 0;
        }
        input#button,input#button1,input.btnClass5.mr23,input.btnClass6{
            letter-spacing: 0;
            text-indent: 0px;
            text-align: center;
        }
        #updatePwDiv input#new_pass1,#updatePwDiv input#pass2,div#passWordLimt,#updatePwDiv input#new_pass2{
            width:275px;
        }
        .classsic_change .themeSpan {
            background:url(img/icon_tradition.png) no-repeat 5px 50%;
        }
        .tradition_change .themeSpan {
            background:url(img/icon_classic.png) no-repeat 3px 50%;
        }
        .simple_change .themeSpan {
            background:url(img/icon_classic.png) no-repeat 3px 50%;
        }

        .classsic_change .fontsel {
            background:url(img/icon_tradition_sel.png) no-repeat 5px 50%;
        }
        .tradition_change .fontsel {
            background:url(img/icon_classic_sel.png) no-repeat 3px 50%;
        }
        .simple_change .fontsel {
            background:url(img/icon_classic_sel.png) no-repeat 3px 50%;
        }
        #themeDiv > div.tradition_change > span {
            font-size: 12px;
        }
    </style>
    <%}%>
    <script type="text/javascript">
        <% String skinstyle = skin.substring(skin.lastIndexOf("/")+1);%>
        var remd="<%=remd%>";
        /*change of the emp skin*/
        var old_skin2 = "<%=skinstyle%>";
        var old_skin = "<%=skinstyle%>";
        var emppatch = "<%=path%>";
        var frameCode = "<%=frameCode%>";
        var otherSkin = "";
        var isPassOverdue = '<%=passOverdue %>';
        /*End-change of the emp skin*/
        $(document).ready(function(){
            <%
            if(tkn == null || loginMap.get(tkn) == null ||
                    !loginMap.get(tkn).checkSessionId(cursessionid))
            {
            %>
            $("#logoutalert").val(1);
            location.href="<%=path%>/common/logoutEmp.jsp";
            <%
            }else if(params == null || params.length() == 0)
            {
                params = StaticValue.getLoginInfoMap().get(tkn).toString();
            }
            %>

            var logindays = '<%=Logindays %>';
            if(isPassOverdue == "true")
            {
                alert(getJsLocaleMessage("common","common_frame2_main_4"));
                doUpdatePass();
                changeinfo(2);
            }else{
                if (remd != "0"&&remd != ""&&remd != "null") {
                    alert(getJsLocaleMessage("common","common_frame2_main_5")+logindays+getJsLocaleMessage("common","common_frame2_main_6"));
                }

            }
        });
    </script>
</head>

<body style="height:100%; overflow-y:hidden;" onresize="sizeset()" onunload="befoclose()">
<div id="bdbg_left"></div>
<div id="loginparams" class="hidden">
    <%
        String corpcode = "";
        String userid = "";
        if(params != null && params.length() > 0)
        {
            String[] paramArr = params.split(",");
            for(int i = 0;i < paramArr.length; i ++ )
            {
                String[] vaa = paramArr[i].split(":");
                out.print("<input type='hidden' id='lg"+vaa[0]+"' name='lg"+vaa[0]+"' value='"+vaa[1]+"' />");
                if(vaa[0].equals("userid"))
                {
                    userid = vaa[1];
                }
                if(vaa[0].equals("corpcode"))
                {
                    corpcode = vaa[1];
                }
            }
        }
        boolean isCharging = SystemGlobals
                .isDepBilling((corpcode == null || corpcode == "") ? "100001"
                        : corpcode);//是否启用计费机制
    %>
    <input type="hidden" name="tkn" id="tkn" value="<%=tkn %>"/>
</div>
<input type="hidden" id="logoutalert" value="0"/>
<input type="hidden" id="pathUrl" name="pathUrl" value="<%=path %>"/>
<div id="wrap" class="wrap" style="min-width: 1580px;">
    <div class="top-nav-div">
        <iframe style="border: 0;" frameborder="0" border=0 height="100%" src="<%=path %>/thirdMenu.htm?method=toTopPage&time=<%=System.currentTimeMillis() %>&tkn=<%=tkn %>&corpcode=<%=corpcode %>&userid=<%=userid %>" name="topFrame" scrolling="no" id="topFrame" allowtransparency="true" ></iframe>
    </div>
    <div id="top_frame_div">
        <iframe style="border: 0;" frameborder="0" border=0 height="100%" src="<%=iPath %>/middel.jsp"  scrolling="no"  name="mainFrame" id="mainFrame" allowtransparency="true" ></iframe>
    </div>
</div>
<div id="msg1" class="msg" style="z-index: 100;">
    <table bordercolor="#cfdef4" id="table1" width="320px;" height="160px;" cellpadding="0" cellspacing="0">
    </table>
</div>
<%--
	<div style="display:none; z-index: 3000;" id="detail" ></div>
	 --%>
<div style="display:none; z-index: 3000; overflow: inherit;" id="detail" >
    <iframe width="100%" height="100%" frameborder="0" id="dialog_if" name="dialog_if"></iframe>
</div>
<%-- 系统菜单 --%>
<div id="dd" class="floating" style="">
    <ul id="uChildren" class="">
    </ul>
</div>
<iframe id="ifr" class="ifr"></iframe>
<%-- 信息 --%>
<div id="loginMesFloating" class="floating" style="">
    <ul class="uChildren" id="">
        <li><a href="#" onclick="doUpdatePass()"><emp:message key="common_personalSetting" defVal="个人设置" fileName="common"></emp:message></a></li>
        <li><a href="#" onclick="parent.frames['topFrame'].window.upLoad()"><emp:message key="common_helpManual" defVal="帮助手册" fileName="common"></emp:message></a></li>
        <li><a href="#" onclick="parent.frames['topFrame'].window.about()"><emp:message key="common_aboutPlatform" defVal="关于平台" fileName="common"></emp:message></a></li>
        <%--
                   <li><a href="#" onclick="parent.frames['topFrame'].window.logout()">退&nbsp;&nbsp;出</a></li>
                    --%>
    </ul>
</div>
<%-- 余额 --%>
<div id="rNumFloating" class="floating1" style="">
    <ul class="uChildren" id="">
        <%if (isCharging)
        {%>
        <li><a href="#" ><emp:message key="common_frame2_main_8" defVal="机构短信余额：" fileName="common"></emp:message><span id="ctSms" style="color:red;">0</span></a></li>
        <li><a href="#" ><emp:message key="common_frame2_main_9" defVal="机构彩信余额：" fileName="common"></emp:message><span id="ctMms" style="color:red">0</span></a></li>
        <%} %>
        <%if(isShowYe){ %>
        <li><a href="#" id="checkfee" style="padding-left: 0;padding-right: 0;text-align: center;_padding-left: 10px;_padding-right: 10px;" onclick="parent.frames['topFrame'].window.checkFee()"><emp:message key="common_frame2_main_10" defVal="查看运营商余额" fileName="common"></emp:message></a></li>
        <%} %>
    </ul>
</div>
<div id="UpdatePassDiv" style="display: none;">
    <div style="height: 5px;"></div>
    <center>
        <table height="25px;" align="center">
            <tr align="center">
                <td id="updateinfo"  class="infotd1" onclick="javascript:changeinfo(1)"><emp:message key="common_personalInfo" defVal="个人信息" fileName="common"></emp:message></td>
                <td id="updatepw" class="infotd2" onclick="javascript:changeinfo(2)"><emp:message key="common_passwdChange" defVal="修改密码" fileName="common"></emp:message></td>
            </tr>
        </table>
        <div id="updateinfoDiv" style="height: 315px;">
            <form name="modifyMobile" method="post" action="">
                <table>
                    <tr>
                        <td colspan="2"><emp:message key="common_account" defVal="账号：" fileName="common"></emp:message></td>
                        <td><label id="username"></label></td>
                    </tr>
                    <tr>
                        <td colspan="2"><emp:message key="common_designation" defVal="名称：" fileName="common"></emp:message></td>
                        <td><label id="name"></label></td>
                    </tr>
                    <tr>
                        <td colspan="2"><emp:message key="common_gender" defVal="性别：" fileName="common"></emp:message></td>
                        <td><label id="sex"></label></td>
                    </tr>
                    <tr>
                        <td colspan="2"><emp:message key="common_cellPhone" defVal="手机：" fileName="common"></emp:message></td>
                        <td><input class="input_bd" type="text" name="mobile" id="mobile" onblur="checkM()" onkeyup="phoneInputCtrl($(this))" value="" maxlength="21" /><font style="color: red;">*</font></td>
                    </tr>
                    <tr>
                        <td colspan="2"><emp:message key="common_phone" defVal="座机：" fileName="common"></emp:message></td>
                        <td><input class="input_bd" type="text" name="oph" id="oph" onblur="checkM()" onkeyup="checkM()" value=""/></td>
                    </tr>
                    <tr>
                        <td colspan="2"><emp:message key="common_email" defVal="邮箱：" fileName="common"></emp:message></td>
                        <td><input class="input_bd" type="text" name="EMail" id="EMail" onblur="checkM()"  value=""/></td>
                    </tr>
                    <tr>
                        <td colspan="2">Q&nbsp;Q：&nbsp;</td>
                        <td><input class="input_bd" type="text" maxlength="15" name="qq" id="qq" onblur="checkM()" onkeyup="checkM()" value=""/></td>
                    </tr>
                </table>
            </form>
            <div style="width: 80%;">
                <p id="zhu" style="color: red;"></p>
            </div>
        </div>
        <div id="updatePwDiv" style="display: none;height: 315px;"><br/>
            <form name="modifyPass" method="post"  action="">
                <table>
                    <tr>
                        <td width="70"><span style="display: block;white-space: nowrap;">&nbsp;<emp:message key="common_frame2_main_19" defVal="原 密 码：" fileName="common"></emp:message></span></td>
                        <td><input class="input_bd" type="password"  name="pass2" id="pass2" onblur="checkPass(this.value)"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="70"><span style="display: block;white-space: nowrap;">&nbsp;<emp:message key="common_frame2_main_67" defVal="新 密 码：" fileName="common"></emp:message></span></td>
                        <td>
                            <div style="position: relative;height:28px;">
                                <input class="input_bd" type="password" name="new_pass1" id="new_pass1" onblur="blurPass()" onfocus ="focusPass()"/>
                                <div class="input_pass" id="passWordLimt" onclick ="focusShowPass()"><%=passWordLimt %></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td width="70"><span style="display: block;white-space: nowrap;"><emp:message key="common_frame2_main_20" defVal="再次输入：" fileName="common"></emp:message></span></td>
                        <td><input class="input_bd" type="password" name="new_pass2" id="new_pass2"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <label id="zhuPass" class="zhu" style="padding-left: 0px; "></label>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </center>
    <div >
        <center>
            <input type="button" value="<emp:message key="common_frame2_main_23" defVal="确  认" fileName="common"></emp:message>" id="button" onclick="checkForm()" class="btnClass5 mr23"/>
            <input type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" id="button1" class="btnClass6" onclick="doReset()"/>
            <br/>
        </center>
    </div>
</div>
<div id="skinDiv" style="display:none;">
    <div id="themeDiv" class="ui-widget-header" style="width:100%;overflow:hidden;padding:12px 0 0;">
        <div class="simple_change ml23 selected" ><span class="themeSpan" themecode="frame4.0" onclick="getThemeSkin('frame4.0')"><emp:message key="common_simple" defVal="简约" fileName="common"/></span></div>
        <div class="classsic_change ml23 " ><span class="themeSpan" themecode="frame3.0" onclick="getThemeSkin('frame3.0')"><emp:message key="common_classic" defVal="经典" fileName="common"/></span></div>
        <div class="tradition_change" ><span class="themeSpan" themecode="frame2.5" onclick="getThemeSkin('frame2.5')"><emp:message key="common_tradition" defVal="传统" fileName="common"/></span></div>
    </div>
    <center>
        <div style="width:722px;height:375px;overflow:hidden;padding-top:5px" id="skinlist"></div>
        <div style="text-align: center;">
            <input type="button" class="btnClass5 mr23" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="skinSure()"/>
            <input type="button" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="skinNoGood()"/>
            <br/>
        </div>
    </center>
</div>
<%-- 语言 --%>
<%if(("Yes".equals(multiLanguageEnable) && langs != null && !langs.isEmpty()) && langs.size() > 1){%>
<div id="languageDiv" style="display:none;">
    <center>
        <div style="width:522px;height:90px;overflow:hidden;padding-top:25px" id="languagelist">
            <emp:message key="common_frame2_main_24" defVal="语言设置：" fileName="common"></emp:message>&nbsp;&nbsp;&nbsp;&nbsp;
            <%
                if(langs != null && !langs.isEmpty()){
                for(String str : langs){
                    String langName = "zh_HK".equals(str)?"English":"zh_TW".equals(str)?"繁體":"简体";
                    String checked="";
					String StaticlangName = (String)session.getAttribute(StaticValue.LANG_KEY);
                    if(StaticlangName.equals(str)){
                    	checked="1";
                    }
            %>
            <input type="radio" <%if("1".equals(checked)){%>checked <%}%> name="setLanguage" value="<%=str%>" >&nbsp;<%=langName%>&nbsp;&nbsp;&nbsp;&nbsp;
            <%}}%>
        </div>
        <div style="text-align: center;">
            <input type="button" class="btnClass5 mr23" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="changeLanguage()"/>
            <input type="button" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="$('#languageDiv').dialog('close')"/>
            <br/>
        </div>
    </center>
</div>
<%}%>
<%--   所有反馈的内容都在当前这个页面里面 --%>
<div id="complainDiv" title="<emp:message key="common_feedback" defVal="反馈" fileName="common"></emp:message>" style="display:none">
    <iframe id="tempFrame" name="tempFrame" <%="zh_HK".equals(empLangName)?"style=\"width:650px;height:620px;border: 0;\"":"style=\"width:520px;height:580px;border: 0;\""%> marginwidth="0" scrolling="no" frameborder="no" src ="" ></iframe>
</div>

<div class="hidden">
    <form id = "themeForm" action="" method="post">
        <input type="hidden" value="<%=tkn %>" name="tkn"/>
        <input type="hidden" value="<%=params %>" name="loginparams"/>
    </form>
</div>
<div style="display: hidden;overflow: hidden" id="aboutDiv">
    <input type="hidden" value="<%=empLangName%>" id="empLangName"/>
    <iframe src="" id="aboutFrame" border="0" frameborder="0"width="100%" height="100%"></iframe>
</div>
<div id="weixArgu" style="display:none;">
    <div id="weixArgu_inner" style="width:672px;height:350px;overflow-y:auto;border:1px solid #ccc;margin:25px auto;padding:10px;"></div>
    <div  style="margin:0 auto;width:86px;"><input type="button" value="<emp:message key="common_accept" defVal="接受" fileName="common"></emp:message>" class="btnClass5" id="argeeArg" onclick="$('#mainFrame').contents().find('#cont')[0].contentWindow.getApprove();"/></div>
</div>
<div id="noticeArgu" style="display:none;">
    <div id="noticeArgu_inner" style="width:672px;height:350px;overflow-y:auto;border:1px solid #ccc;margin:25px auto;padding:10px;"></div>
    <div  style="margin:0 auto;width:86px;"><input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass5" id="notArgeeArg" onclick="notice_tips_close();"/></div>
</div>
<%--  标示1.点击监控主界面内的链接  2.点击关闭按钮--%>
<input type="hidden" value="2" id="close_mon_State" name="close_mon_State"/>
<%-- 监控告警信息右下角弹出框 --%>
<div id="jkBox" style="display: none;">
    <div class="inner">
        <div class="toolbar">

            <div class="tool_warn">
                <span class="tomini" onclick="tomini()" title="<emp:message key="common_minimize" defVal="最小化" fileName="common"></emp:message>"></span>
                <span class="winMax" onclick="openMonMain()" title="<emp:message key="common_maximize" defVal="最大化" fileName="common"></emp:message>"></span>
                <span class="winClose" onclick="clioseMonDialog()" title="<emp:message key="common_close" defVal="关闭" fileName="common"></emp:message>"></span>
            </div>
            <emp:message key="common_errorInfo" defVal="告警信息" fileName="common"></emp:message>
        </div>
        <div class="bd" id="warning_info">
        </div>
    </div>
</div>
<div style="display: none" id="menuReOpen">
    <div id="menu"></div>
    <div id="menuInfo"></div>
    <div id="idstr"></div>
</div>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=iPath %>/js/json2.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath %>/js/main.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
    /*change of the emp skin*/
    var iPath="<%=iPath%>";
    var getContextPath="<%=request.getContextPath()%>";
    var isOneLogin="<%=isOneLogin%>";
    var noticeTxt = "<%=noticeTxt==null?"":noticeTxt %>";
    var GGCODE= "<%=ViewParams.GGCODE%>";
    var tiemrErrMon = null;

    /*End-change of the emp skin*/
    //免责申明
    function wei_tips_dialog(txt){
        $('#weixArgu').dialog({
            autoOpen: true,
            height: 507,
            width: 768,
            title:getJsLocaleMessage("common","common_frame2_main_31"),
            resizable:false,
            modal: true,
            open:function(){

            },
            close:function(){
                var aprove = window.frames['mainFrame'].frames['cont'].document.getElementById("approve").value;
                if(aprove!="1"){
                    $(window.parent.document).find("#topFrame").contents().find("#modIndex").trigger('click');
                }
            }
        });

        $('#weixArgu_inner').html(txt);
    }
    function wei_tips_close(){
        $('#weixArgu').dialog('close');
    }

    //系统公告
    function notice_tips_dialog(txt){
        $('#noticeArgu').dialog({
            autoOpen: true,
            height: 507,
            width: 768,
            title:getJsLocaleMessage("common","common_SysNotice"),
            resizable:false,
            modal: true,
            closeOnEscape: false,
            open:function(){
                $(".ui-dialog-titlebar-close").hide();
            },
            close:function(){
                setRefrshNoTip();
            }
        });

        $('#noticeArgu_inner').html(txt);
    }
    function notice_tips_close(){
        $('#noticeArgu').dialog('close');
    }
    function setRefrshNoTip(){
        $.post("<%=path %>/not_sysNotice.htm?method=setNoTip",{},function(result){


        });
    }
</script>
<script type="text/javascript" src="<%=iPath %>/js/main_func.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=path%>/common/widget/artDialog/artDialog.js?skin=jk"></script>
<script src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script>
    $(document).ready(function(){
        //warning_dialog();
        //设置语言
        $(".fin_char").click(function(){
            $(this).addClass("selected");
            $(this).siblings().removeClass("selected");
            $(this).find("input").attr("checked","checked");
        });
    })
    //报警信息弹出框
    function warning_dialog(){
        var throughBox = art.dialog.through;
        throughBox({
            content: document.getElementById('hidePanel').innerHTML,
            id: 'msg',
            title: getJsLocaleMessage("common","common_warnings"),
            left: '100%',
            top: '100%',
            fixed: true,
            drag: true,
            resize: false
        });
    }
</script>
</body>
</html>
