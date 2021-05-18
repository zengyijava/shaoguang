var i = 0;
var isnorpt = "";
//标示点击登录是否请求后台后返回
var isAjax = false;

// 加载语言资源文件
var iHead = document.getElementsByTagName('HEAD').item(0);
var iScript= document.createElement("script");
iScript.setAttribute("type","text/javascript");
iScript.setAttribute("src",commonPath+"/common/i18n/"+langName+"/common_"+langName+".js");
iHead.appendChild(iScript);

var tipInfo = getJsLocaleMessage("common","common_frame2_login_44");

$(document).ready(function() {
	/**
	 * 解决登录页文本框 内容与提示重叠的问题
	 */
	$('.loginFormTdIpt').bind('propertychange input focus',function(){
		if($(this).val()){
			$(this).next(".placeholder").hide();
		}
	})
	//界面新增2013-11-16
	$('.loginFormTdIpt').focus(function(){
		$(this).parent().addClass('inp_focus');
	}).blur(function(){
		$(this).parent().removeClass('inp_focus');
	}).keypress(function(){
		$(this).next(".placeholder").hide();
	}).blur(function(){
		if(!$(this).val()){
			$(this).next(".placeholder").show();
		}
	}).click(function(){
		$('#errorAlert').removeClass('errorAlert-show').hide();
		$('#errorMask').remove();
	})
	$('.loginFormInp label').click(function(){
		$(this).prev('.loginFormTdIpt').focus();
	})
	//界面新增2013-11-16
	$('#pwdArea').focus(function(){
		$(this).addClass('inp_focus');
	}).blur(function(){
		$(this).removeClass('inp_focus');
	})
	$('#codeArea').focus(function(){
		$(this).addClass('inp_focus');
	}).blur(function(){
		$(this).removeClass('inp_focus');
	})
	$('#uinArea').focus(function(){
		$(this).addClass('inp_focus');
	}).blur(function(){
		$(this).removeClass('inp_focus');
	})
	$('#verifyArea').focus(function(){
		$(this).addClass('inp_focus');
	}).blur(function(){
		$(this).removeClass('inp_focus');
	})
	$('#errorMask').live('click',function(){
			$('#errorAlert').removeClass('errorAlert-show').hide();
			$(this).remove();
		
	if($("#corptype").val() !="true"){
		tipInfo = getJsLocaleMessage("common","common_frame2_login_45");
	}else{
		tipInfo = getJsLocaleMessage("common","common_frame2_login_44");
	}
		
	})
	//获取上次选中的banner
	var bIndex=parseInt(cookieUtil.getCookie("banner"));
	$('#banner_list').empSlide({"_index":bIndex});
	
    var v = null;
    if (document.documentMode == 10) v = 'IE 10';
    var isIE10 = isIE && (v == "IE 10") && isIE10;
    if (isbrowser && !(isIE6 || (isIE7 && !SG) || isIE8 || isIE9 || isIE10 || isSG)) {
        showMsg(getJsLocaleMessage("common","common_frame2_login_31"));
        return;
    }

    var isShowEnpCode = $("#isMulti").attr("value");
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        $("#enpCode").focus();
    } else {
        $("#login_name").focus();
    }
    $("#login_btn").hover(function(e) {
        $("#login_btn").addClass('login_btn_hover');
    }, function(e) {
        $("#login_btn").removeClass('login_btn_hover');
    });
    //浏览器保存密码时，清除文本框提示
    setTimeout(function() {
        var v1 = $('#login_pwd').val();

        var v2 = $('#login_name').val();
        if (v1 != '') {
            $('#namePlaceholder').hide();
        }
        if (v2 != '') {
            $('#pwdPlaceholder').hide();
        }
    }, 30);
    //点击文本框  框内提示消失
    $(".inputOuter input").bind('focus keypress',function(){
    	var tips_id=$("#"+$(this).attr("id")+"_tips");
    	tips_id.hide();
    	
    }).blur(function(){
    	var tips_id=$("#"+$(this).attr("id")+"_tips");
    	if($(this).val()!=tips_id.text()&&$(this).val()==''){
    		tips_id.show();
    	}
    }).click(function(){
    	$('#tips').css({'visibility':'hidden'});
    });
    
    $('#loginSub').bind('click', function() { doLogin($('#login_btn')); });
});
function showAbout(){
    var empLangName = $("#langName").val();
    $("#aboutDiv").dialog({
        title: getJsLocaleMessage("common","common_aboutPlatform"),
        autoOpen: false,
        width: empLangName === "zh_HK"?650:526,
        height: 320,
        resizable: false,
        closeOnEscape: false,
        modal: true,
        close: function() {}
    });
    $("#aboutFrame").attr("src", getContextPath+"/emp_tz.hts?method=showAbout&empLangName="+empLangName+"&isLogin=1");
    $("#aboutDiv").dialog("open");
};
//验证码
function change() {
    if("true" == $("#isShowYanzheng").val()){
        $("#verifyImg").attr("src", getContextPath + "/changeyzm?timer=" + new Date().getTime());
    }
    return false;
};
//按回车登录 支持ie 火狐 键盘按下事件
function keydown(e){
    var currKey = 0;
    var isShow = $("#isShowYanzheng").attr("value");
        e = e || event;
    if (e.keyCode == 13) {
        doLogin();
    }
    
    if (e.keyCode == 9) {
    	if (isShow != null && "true" == isShow) {
    		if(document.activeElement.id == "code"){
    		return false;
        }
    	}else{
    	  if(document.activeElement.id == "login_pwd"){
    	            return false;
    	  }
    	}
    }
};
function doClickStyle(obj, objclassname) {
    document.getElementById(obj).className = objclassname;
};
function doLogin() {
    if(isLogining()){
        return;
    }
	logining();
	var errorAlert=$('#errorAlert');
    $("#errorCount").val("0");
    var v = null;
    if (document.documentMode == 10) v = 'IE 10';
    var isIE10 = isIE && (v == "IE 10") && isIE10;
    if (isbrowser && !(isIE6 || (isIE7 && !SG) || isIE8 || isIE9 || isIE10 || isSG)) {
        showMsg(getJsLocaleMessage("common","common_frame2_login_36"));
        return;
    }
    var account = $.trim($("#login_name").attr("value"));
    var pass = $("#login_pwd").attr("value");
    var inyzm = "";
    var code = "";
    var isShow = $("#isShowYanzheng").attr("value");
    if (isShow != null && "true" == isShow) {
        inyzm = $.trim($("#code").attr("value"));
    }
    var isShowEnpCode = $("#isMulti").attr("value");
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        code = $.trim($("#enpCode").attr("value"));
    }
    var tstName = /^[A-Za-z0-9]{3,64}$/;
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        if (code == null || code == "") {
            //showMsg("请输入企业编码！");
            getPos.apply(this,[errorAlert,$("#enpCode"),getJsLocaleMessage("common","common_frame2_login_37")]);
            $("#enpCode").focus();
            return;
        } else if (code.length != 6) {
            //showMsg("企业编码为6位数字！");
            getPos.apply(this,[errorAlert,$("#enpCode"),getJsLocaleMessage("common","common_frame2_login_38")]);
            $("#enpCode").focus();
            return;
        } else {
            var pattern = /^[0-9]+$/;
            if (!pattern.test(code)) {
                //showMsg("企业编码为6位数字！");
                getPos.apply(this,[errorAlert,$("#enpCode"),getJsLocaleMessage("common","common_frame2_login_38")]);
                $("#enpCode").focus();
                return;
            }
        }
    }
    var patrnUserNo = /^(\w){1,20}$/; //字母数字下划线
    if (account == null || account == "") {
       // showLoginTips('请输入登录账号！');
        getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_39")]);
        $("#login_name").focus();
        return;
    } else if (!patrnUserNo.exec(account)) {
        //showLoginTips('登录账号输入格式不正确，是由1~20位字母数字下划线组成！');
         getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_40")]);
        $("#login_name").focus();
        return;
    } else if (pass == null || pass == "") {
       // showLoginTips('请输入密码！');
        getPos.apply(this,[errorAlert,$("#login_pwd"),getJsLocaleMessage("common","common_frame2_login_41")]);
        $("#login_pwd").focus();
        return;
    } else if (isShow != null && "true" == isShow&&(inyzm== null || inyzm.length == 0 || inyzm.length<4)){
            if(inyzm== null || inyzm.length == 0){
                getPos.apply(this,[errorAlert,$("#code"),getJsLocaleMessage("common","common_frame2_login_42")]);
                $("#code").focus();
                return;
            }else if(inyzm.length<4){
                getPos.apply(this,[errorAlert,$("#code"),getJsLocaleMessage("common","common_frame2_login_54")]);
                $("#code").focus();
                return;
            }
    } else {
        var passvalue="";
        var tmppass= _getRandomString(pass.length*2);
        for (var i = 0; i < pass.length; i++) {
        	passvalue=passvalue+tmppass.charAt(i*2)+pass.charAt(i);

        }
        //赋值密码输入框为混淆的密码，避免暴露明文
        // $('#login_pwd').val(passvalue);
       var tmpname= _getRandomString(account.length*2);
       var accountvalue="";
       for (var i = 0; i < account.length; i++) {
    	   accountvalue=accountvalue+tmpname.charAt(i*2)+account.charAt(i);
       }
        var phoneword = '';
        if($("#dynphoneword").is(":visible")){
            phoneword = $('#phonewords').val();
            if(phoneword == null || phoneword == ''){
                complete();
                $("#sendsucess").html("<font color='red'>*"+getJsLocaleMessage("common","common_frame2_login_35")+"</font>").show();
                $("#phonewords").focus();
                return false;
            }
        }
        if($("#dynphoneword").is(":visible")){
        	art.dialog({id:'art-dynphoneword'}).close();
        	checkDynamicWord(accountvalue,passvalue,code,inyzm,phoneword);
        }else{
        	toLogin(accountvalue,passvalue,code,inyzm,phoneword);
        }
   //     toLogin(accountvalue,passvalue,code,inyzm,phoneword);
    }
};

//给用户名加密
function _getRandomString(len) {
    len = len || 32;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}

//取消
function btcancel1()
{
    $('#dynphoneword').dialog('close');
}

//登录方法
function toLogin(account, pass, code, inyzm, phoneword) {
	
	pass = Base64.encode(pass);
	
    var errorAlert=$('#errorAlert');
    $.post(getContextPath + "/login", {
        username: account,
        pass: pass,
        enpCode: code,
        inyzm: inyzm,
        phoneword: phoneword,
        tkn: tkn,
        ipAddr: ipAddr,
        macAddr: macAddr
    }, function(result) {
        isAjax = true;
        if (result == "yzfalse") {
            //showMsg("验证码输入错误！");
            getPos.apply(this,[errorAlert,$("#code"),getJsLocaleMessage("common","common_frame2_login_54")]);
            $("#login_pwd").focus();
            return;
        } else if (result == "false") {
            var isShowEnpCode = $("#isMulti").attr("value");
            var showMsgStr = getJsLocaleMessage("common","common_frame2_login_33");
            if (isShowEnpCode != null && "true" == isShowEnpCode) {
                showMsgStr = getJsLocaleMessage("common","common_frame2_login_34");
            }
            showMsg(showMsgStr);
            $("#login_name").select();
            return;
        } else if (result == "mid") {
           // showMsg("该账号已禁用，无法登录！请联系管理员启用此账号！");
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_forbidLogin_1")]);
            return;
        } else if (result == "deleteuser") {
          //  showMsg("该账号已注销，无法登录！");
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_deleteUser_1")]);
            return;
        } else if (result == "error") {
            //showMsg("登录验证失败，请确认网络是否正常！");
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_53")]);
            return;
        } else if (result == "qybmerror") {
            //showMsg("企业编码在系统中不存在，请重新输入企业编码！  ");
            getPos.apply(this,[errorAlert,$("#enpCode"),getJsLocaleMessage("common","common_frame2_login_55")]);
            $("#enpCode").select();
            return;
        } else if (result == "overtimes") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_56"));
            return;
        } else if (result == "phoneworderror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_57"));
            return;
        } else if (result == "nogetMacAddr") {
            showMsg(getJsLocaleMessage("common","common_frame3_login_1"));
            return;
        } else if (result == "nogetIP") {
            showMsg(getJsLocaleMessage("common","common_frame3_login_2"));
            return;
        } else if (result == "lockCorp") {
        	showMsg(getJsLocaleMessage("common","common_frame3_login_lockCorp"));
            return;
        } else if (result == "MacAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_52"));
            return;
        }else if (result == "IPAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_50"));
            return;
        }else if (result.indexOf("true") > -1) {
            var rArr = result.split("&");
        	$('#loginSub').unbind();
            $('#tkn').val(tkn);
            $("#form1").attr("action", path + "/frame/main.jsp");
            setTimeout(function(){
            	document.form1.submit();
            },300);
        }else if(result == 'illegal'){
            showMsg(getJsLocaleMessage("common","common_frame3_login_3"));
            setTimeout(function(){
                window.location.reload();
            },1500);
            return;
        }else if(result == 'expire'){
            window.location.reload();
        }else if(result == 'nophoneword'){
            isAjax = false;
            complete();
            $('#phonewords').val('');
            $("#sendsucess").html("<font color='red'>*"+getJsLocaleMessage("common","common_frame2_login_26")+"</font>").show();
            art.dialog({
                id:"art-dynphoneword",
                content:$('#dynphoneword')[0],
                title:getJsLocaleMessage("common","common_message"),
                okVal:getJsLocaleMessage("common","common_confirm"),
                cancelVal:getJsLocaleMessage("common","common_cancel"),
                ok:function(){
                    return doLogin();
                },
                cancel: true
            });
        }else if(result == "mananger") {
            window.location.href=getContextPath+"/systemManage.htm?method=toSkip";
            return;
        }else if (result.indexOf("[change];")>-1) {
            var re= result.split(";");
            if(re.length>=2){
                getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame3_login_4")+re[1]+getJsLocaleMessage("common","common_frame3_login_5")]);
                return;
            }
        }else if (result == "lockuser") {
            getPos.apply(this,[errorAlert,$("#login_name"),tipInfo]);
            return;
        }else if (result == "errpwd") {
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_43")]);
            return;
        }else
    	{
    	    showMsg(getJsLocaleMessage("common","common_frame2_login_65")+"["+result+"]");
            return;
    	}
    });
};

//动态口令验证
function checkDynamicWord(account, pass, code, inyzm, phoneword) {
	pass = Base64.encode(pass);
    var errorAlert=$('#errorAlert');
    $.post(getContextPath + "/login", {
        method:"checkDynamicWord",
    	username: account,
        pass: pass,
        enpCode: code,
        inyzm: inyzm,
        phoneword: phoneword,
        tkn: tkn,
        ipAddr: ipAddr,
        macAddr: macAddr
    }, function(result) {
        isAjax = true;
        if (result == "yzfalse") {
            //showMsg("验证码输入错误！");
            getPos.apply(this,[errorAlert,$("#code"),getJsLocaleMessage("common","common_frame2_login_54")]);
            $("#login_pwd").focus();
            return;
        } else if (result == "false") {
            var isShowEnpCode = $("#isMulti").attr("value");
            var showMsgStr = getJsLocaleMessage("common","common_frame2_login_33");
            if (isShowEnpCode != null && "true" == isShowEnpCode) {
                showMsgStr = getJsLocaleMessage("common","common_frame2_login_34");
            }
            showMsg(showMsgStr);
            $("#login_name").select();
            return;
        } else if (result == "mid") {
           // showMsg("该账号已禁用，无法登录！请联系管理员启用此账号！");
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_47")]);
            return;
        } else if (result == "deleteuser") {
          //  showMsg("该账号已注销，无法登录！");
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_46")]);
            return;
        } else if (result == "error") {
            //showMsg("登录验证失败，请确认网络是否正常！");
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_53")]);
            return;
        } else if (result == "qybmerror") {
            //showMsg("企业编码在系统中不存在，请重新输入企业编码！  ");
            getPos.apply(this,[errorAlert,$("#enpCode"),getJsLocaleMessage("common","common_frame2_login_55")]);
            $("#enpCode").select();
            return;
        } else if (result == "overtimes") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_56"));
            return;
        } else if (result == "phoneworderror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_57"));
            return;
        } else if (result == "nogetMacAddr") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_51"));
            return;
        } else if (result == "nogetIP") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_49"));
            return;
        } else if (result == "MacAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_52"));
            return;
        }else if (result == "IPAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_50"));
            return;
        }else if (result.indexOf("true") > -1) {
            var rArr = result.split("&");
        	$('#loginSub').unbind();
            $('#tkn').val(tkn);
            $("#form1").attr("action", path + "/frame/main.jsp");
            setTimeout(function(){
            	document.form1.submit();
            },300);
        }else if(result == 'illegal'){
            showMsg(getJsLocaleMessage("common","common_frame3_login_3"));
            setTimeout(function(){
                window.location.reload();
            },1500);
            return;
        }else if(result == 'expire'){
            window.location.reload();
        }else if(result == 'nophoneword'){
            isAjax = false;
            complete();
            $('#phonewords').val('');
            $("#sendsucess").html("<font color='red'>*"+getJsLocaleMessage("common","common_frame2_login_26")+"</font>").show();
            art.dialog({
                id:"art-dynphoneword",
                content:$('#dynphoneword')[0],
                ok:function(){
                    return doLogin();
                },
                cancel: true
            });
        }else if(result == "mananger") {
            window.location.href=getContextPath+"/systemManage.htm";
            return;
        }else if (result.indexOf("[change];")>-1) {
            var re= result.split(";");
            if(re.length>=2){
                getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame3_login_4")+re[1]+getJsLocaleMessage("common","common_frame3_login_5")]);
                return;
            }
        }else if (result == "lockuser") {
            getPos.apply(this,[errorAlert,$("#login_name"),tipInfo]);
            return;
        }else if (result == "errpwd") {
            getPos.apply(this,[errorAlert,$("#login_name"),getJsLocaleMessage("common","common_frame2_login_43")]);
            return;
        }else
    	{
    	    showMsg(getJsLocaleMessage("common","common_frame2_login_65")+"["+result+"]");
            return;
    	}
    });
};

//当启动获取手机动态口令时的方法
var phoneCount=0;
var phoneStatus=true;
function GetPhoneWord() {
	//添加短信验证码发送十分钟只能发送三次
	if(phoneStatus){
		phoneStatus=false;
		window.setTimeout(function(){
			phoneStatus=true;
			phoneCount=0
		},10 * 60 * 1000);
		
	}
	if(phoneCount>=3){
		alert(getJsLocaleMessage("common","Number_of_verification_codes"));
		return;
	}
	phoneCount=phoneCount+1;
    var account = $.trim($("#login_name").attr("value"));
    var code = "";
    var isShowEnpCode = $("#isMulti").attr("value");
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        code = $.trim($("#enpCode").attr("value"));
        if (code == null || code == "") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_37"));
            $("#enpCode").focus();
            return;
        } else if (code.length != 6) {
            showMsg(getJsLocaleMessage("common","common_frame2_login_38"));
            $("#enpCode").focus();
            return;
        } else {
            var pattern = /^[0-9]+$/;
            if (!pattern.test(code)) {
                showMsg(getJsLocaleMessage("common","common_frame2_login_38"));
                $("#enpCode").focus();
                return;
            }
        }
    }
    if (account == null || account == "") {
        showMsg(getJsLocaleMessage("common","common_frame2_login_39"));
        $("#login_name").focus();
        return;
    }
    $("#getphoneword").attr("disabled", "disabled");
    //获取动态口令的方法
    $.post(getContextPath + "/getphoneword", {
        method: "getPhoneWord",
        username: account,
        enpCode: code
    }, function(result) {
        if (result == "Nosubno") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_66"));
            return;
        } else if (result == "sendfalse") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_67"));
            return;
        } else if (result == "NoMoney") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_68"));
            return;
        } else if (result == "Moneyerror") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_69"));
            return;
        } else if (result == "errorphone") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_70"));
            return;
        } else if (result == "Nospid") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_71"));
            return;
        } else if (result == "NouserPhone") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_72"));
            return;
        } else if (result == "Nosysuser") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_73"));
            return;
        } else if (result == "error") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_74"));
            return;
        } else if (result == "senderror") {
			showCommandErr(getJsLocaleMessage("common","common_frame2_login_75"));
            return;
        } else if (result == "true") {
            i = 0;
            $("#sendsucess").html("<font color='red'>"+getJsLocaleMessage("common","common_frame2_login_76")+"</font>");
            getState();

        }
    });
};
function getState() {
    var account = $.trim($("#login_name").attr("value"));
    var code = "";
    var isShowEnpCode = $("#isMulti").attr("value");
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        code = $.trim($("#enpCode").attr("value"));
    }
    i = i + 1;
    if (i > 6) {
        if (isnorpt == "norpt") {
            $("#sendsucess").html("<font color='red'>"+getJsLocaleMessage("common","common_frame2_login_77")+"</font>");
            $("#sendsucess,#sendtime").show();
            $("#phonewords").focus();

            window.setTimeout('setButton()', 1000 * 30); //30秒后重新获取
        } else {
            $("#sendsucess").html("<font color='red'>"+getJsLocaleMessage("common","common_frame2_login_78")+"</font>");
            $("#phonewords").focus();
            $("#sendsucess,#sendtime").show();
            $("#getphoneword").attr("disabled", "");
        }

    } else {
        isStateReturn(account, code);
    }
};


function promptMac() {
    $("#promptMsg").html(getJsLocaleMessage("common","common_frame2_login_79"));
}








document.onkeydown = keydown;


//判断状态报告是否返回

function isStateReturn(account, code) {
    $.post(getContextPath + "/getphoneword", {
        method: "getSendState",
        username: account,
        enpCode: code
    }, function(result) {
        if (result == "true") {
            $("#sendsucess").html("<font color='red'>"+getJsLocaleMessage("common","common_frame2_login_80")+"</font>");
            $("#sendsucess").css("display", "block");
            $("#sendtime").css("display", "block");
            $("#phonewords").focus();

            window.setTimeout('setButton()', 1000 * 30); //30秒后重新获取
        } else {
            isnorpt = result;
            window.setTimeout('getState()', 1000 * 10); //10秒后重新获取
        }
    });
}

function setButton() {
    $("#getphoneword").attr("value", getJsLocaleMessage("common","common_frame2_login_81"));
    $("#getphoneword").attr("disabled", "");
    $("#sendtime").css("display", "none");
}
//所有样式名中含有graytext的input

function checkText(ep) {
    var eqva = $.trim(ep.val());
    if (ep.val().length > eqva.length) {
        ep.val(eqva);
    }
    if (ep.val() == "") {
        ep.next("label").css("display", "inline");
    } else {
        ep.next("label").css("display", "none");
    }
}
//收藏本站

function AddFavorite(title, url) {
    try {
        window.external.addFavorite(url, title);
    } catch (e) {
        try {
            window.sidebar.addPanel(title, url, "");
        } catch (e) {
        	var timer;
        	var msgConfig={
        		id: 'AddFavorite',
				height:50,
				fixed: true,
				left: '100%',
                top: '100%',
                init: function () {
		    	var that = this, i = 5;
		        var fn = function () {
		            that.title(i + getJsLocaleMessage("common","common_frame2_login_82"));
		            !i && that.close();
		            i --;
		        };
		        timer = setInterval(fn, 1000);
		        fn();
			    },
			    close: function () {
			    	clearInterval(timer);
			    }
			  };
            showMsg(getJsLocaleMessage("common","common_frame2_login_83")+"<br /><br />"+getJsLocaleMessage("common","common_frame2_login_84"),msgConfig);
        }
    }
}

function focusShow(ep) {
    ep.next("label").css("display", "none");
}

function blurHide(ep) {
    ep.val($.trim(ep.val()));
    if (ep.val() == "") {
        ep.next("label").css("display", "inline");
    } else {
        ep.next("label").css("display", "none");
    }
    if (ep.val().length > 200) {
        ep.val(ep.val().substring(0, 200));
    }
}
function showMsg(msg,config){
    complete();
    var	errorAlert=$('#errorAlert');
    errorAlert.css({'top':'210px'});
    $('#errorTitle').html(msg);
    errorAlert.show();
}
function showLoginTips(msg){
    complete();
	var	errorAlert=$('#errorAlert');
		errorAlert.css({'top':'75px'});
		$('#errorTitle').html(msg);
		errorAlert.show();
}
function showMsgTips(msg,config){
    complete();
	if(typeof config=="undefined"){
	  var msgConfig={
		height:50,
		fixed: true,
		lock: true,
		background: "#000",
		opacity: 0.5
	  };
	}else{
		var msgConfig=config;
	}
	
	msgConfig.content=msg;
	art.dialog(msgConfig).time(3);
}
function showCommandErr(msg){
    complete();
    var sendSucess="<font color='red'>"+msg+"</font>";
	$("#sendsucess").html(sendSucess);
	$("#phonewords").focus();
	$("#sendsucess").css("display", "block");
	$("#sendtime").css("display", "none");
	$("#getphoneword").attr("disabled", "");
}

//<%-- 忘记密码--%>
function autoPass(){

	    art.dialog({
	        content: document.getElementById('autoPasswordDiv'),
	        id: 'autoPasswordDiv',
	        title:getJsLocaleMessage("common","common_message"),
            okVal:getJsLocaleMessage("common","common_confirm"),
            cancelVal:getJsLocaleMessage("common","common_cancel"),
	        ok: function () {
	        	return doAutoUserPro();
	        },
	        okVal:getJsLocaleMessage("common","common_commit"),
	        cancelVal: getJsLocaleMessage("common","common_close"),
	        cancel: true //为true等价于function(){}
	    });
	
	
	
	
 	//clearAuto();
	$("#autoPassUsers").empty();
	if(autoPassUsers0){
		$("#autoPassUsers").html("<option value=''>"+autoPassUsers0+"</option>")
	}else{
		$("#autoPassUsers").html("<option value=''>"+getJsLocaleMessage("common","common_pleaseSelect")+"</option>")
	}
 	$('#autoPasswordDiv').show();
	
}
//<%-- 忘记密码界面的验证码--%>
function changeAutoPass(){
	$("#autoYzm").val("");
	$("#imgPass").attr("src",getContextPath + "/autoyanzhengma?timer="+new Date().getTime());
	return false;
};



//<%--忘记密码点提交--%>
function doAutoUserPro(){
	var autoUserName = $("#autoUserName").val();
	var autoName = $("#autoName").val();
	var autoYzm = $("#autoYzm").val();
	var autoReason = $("#autoReason").val();
	var corpCode = $("#corpCode").val();
	if(autoUserName == "" || autoUserName.length == 0){
		alert(getJsLocaleMessage("common","common_frame3_login_6"));
		return false;
	}
	if(autoName == "" || autoName.length == 0){
		alert(getJsLocaleMessage("common","common_frame3_login_7"));
		return false;
	}
	var autoUserId = "";
	var moblie = "";
	var reName = "";
	var hiddenMoblie = "";
	if($("#autoPassUsers option:selected").size() == 1 ){
		$("#autoPassUsers option:selected").each(function(){
			autoUserId = $(this).val();
			moblie = $(this).attr("moblie");
			reName = $(this).attr("name");
			hiddenMoblie = $(this).attr("hiddenMoblie");
		});
 	}else{
		alert(getJsLocaleMessage("common","common_frame3_login_8"));
		return false;
	}
	if(moblie == "" || moblie.length == 0){
		alert(getJsLocaleMessage("common","common_frame3_login_9"));
		return false;
	}
	
	$("#autoTijiao").attr("disabled","disabled");
		var str =getJsLocaleMessage("common","common_frame3_login_10");
			if(confirm(str)){
				//$("#probar").show();
				
				$.post(getContextPath + "/getphoneword",{method:"sendAutoPass",autoUserName:autoUserName,corpCode:corpCode,autoName:autoName,moblie:moblie,autoUserId:autoUserId,autoReason:autoReason},function(msg){
					//$("#probar").dialog("close");
					 
					if(msg == "fail"){
						alert(getJsLocaleMessage("common","common_frame3_login_11"));
						$("#autoTijiao").attr("disabled",false);
						return false;
					 }else if(msg == "success"){
						alert(getJsLocaleMessage("common","common_frame3_login_12"));
						$("#autoTijiao").attr("disabled",false);
						changeAutoPass();
						autoReset();
					    return false;
					 }else if(msg == "noUserdSpuser"){//noUserdSpuser
							alert(getJsLocaleMessage("common","common_frame3_login_13"));
							$("#autoTijiao").attr("disabled",false);
							return false;
					}else if(msg == "allow3times"){//noUserdSpuser
						alert(getJsLocaleMessage("common","common_frame3_login_14"));
						$("#autoTijiao").attr("disabled",false);
						return false;
					 }else if(msg == "noUser"){//noUserdSpuser
							alert(getJsLocaleMessage("common","common_frame2_login_29"));
							$("#autoTijiao").attr("disabled",false);
							return false;
					}else if(msg == "cheating"){//noUserdSpuser
						alert(getJsLocaleMessage("common","common_frame3_login_15"));
						$("#autoTijiao").attr("disabled",false);
						return false;
				} else{
						alert(getJsLocaleMessage("common","common_operateFailed"));
						$("#autoTijiao").attr("disabled",false);
						return false;
					 }
				});
			}else{
				$("#autoTijiao").attr("disabled",false);
				return false;
			}
		

}

//<%--忘记密码点取消 刷新yzm--%>
function autoReset(){
	clearAuto();
 	$('#autoPasswordDiv').dialog('close');
}
//<%--忘记密码清空数据--%>
function clearAuto(){
	$("#autoUserName").val("");
	$("#autoName").val("");
	$("#autoYzm").val("");
	$("#autoGuId").val("");
}
//获取出错信息的位置--使用apply方法延长作用域
function getPos(obj,source,context){
    complete();
	$('#errorAlert').removeClass('errorAlert-show').hide();
	var enpTop=source.offset().top,
	    enpLeft=$('.login_cont').offset().left,
	    eLeft=enpLeft-obj.outerWidth()-20,
	    eTop=enpTop-15;
	obj.find('#errorTitle').html(context);
	obj.show().stop(true,false).css({
				'top':eTop+'px'
			},500).addClass('errorAlert-show');
	if($('#errorMask').size()==0){
		$('#banner_list').append('<div id="errorMask"></div>');
	}
}

function isLogining(){
    return $('#login-loading').is(':visible');
}

function logining(){
    $('#login-loading').show();
    $('#loginSub').html('');
}

function complete(){
    $('#login-loading').hide();
    $('#loginSub').html(getJsLocaleMessage("common","common_login")).css({'letter-spacing':''});
    if(isAjax){
        //清除密码 以及 验证码
        $('#login_pwd').val('');
        $('#code').val('');
        change();
    }
    isAjax = false;
}

