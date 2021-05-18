
var MACAddr;
var mac_arr = new Array();
var obj1 = null;
var account1 = null;
var pass1 = null;
var code1 = null;
var inyzm1 = null;
var showMsgStr1 = null;
var i = 0;
var isnorpt = "";

$(document).ready(function() {
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
	$('#errorMask').live('click',function(){
			$('#errorAlert').removeClass('errorAlert-show').hide();
			$(this).remove();
		
		
		
	})
	//获取上次选中的banner
	var bIndex=parseInt(cookieUtil.getCookie("banner"));
	$('#banner_list').empSlide({"_index":bIndex});
	
    var v = null;
    if (document.documentMode == 10) v = 'IE 10';
    var isIE10 = isIE && (v == "IE 10") && isIE10;
    if (isbrowser && !(isIE6 || (isIE7 && !SG) || isIE8 || isIE9 || isIE10 || isSG)) {
        showMsg('当前浏览器不可用');
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
function getMacAddr() {
    xe.load.loadObject("locator", "CLSID:76A64158-CB41-11D1-8B02-00600806D9B6", "foo", "CLSID:75718C9A-F029-11d1-A1AC-00C04FB6C223");
    try {
        var service = locator.ConnectServer();
        service.Security_.ImpersonationLevel = 3;
        //此处异步调用
        service.InstancesOfAsync(foo, 'Win32_NetworkAdapterConfiguration');
    } catch (e) {
        //showMsg("请允许ActiveX运行！");
    }
};
function getIpAddr() {
    $("#promptMsg").html("您开启了IP地址验证，需加载activeX控件");
    var oSetting = null;
    var ip = null;
    try {
        oSetting = new ActiveXObject("rcbdyctl.Setting");
        ip = oSetting.GetIPAddress;
        if (ip.length == 0) {
            //showMsg("没有连接到Internet");
            return "";
        }
        oSetting = null;
    } catch (e) {
        //showMsg("获取IP地址失败！");
        return "";
    }
    return ip;
};
function showAbout(){
    var aboutConfig={
        title: '关于平台',
        lock: true,
        background: "#CCCCCC",
        opacity: 0
    };
    art.dialog.open(path + "/emp_tz.hts?method=showAbout", aboutConfig);
};
//验证码
function change() {
    $("#verifyImg").attr("src", getContextPath + "/changeyzm?timer=" + new Date().getTime());
    return false;
};
//按回车登录 支持ie 火狐 键盘按下事件
function keydown(e){
    var currKey = 0;
    var isShow = $("#isShowYanzheng").attr("value");
        e = e || event;
    if (e.keyCode == 13) {
        //如果弹出的输入验证码的div不是隐藏的则需要判断验证码信息
        if ($("#dynphoneword").is(":visible")) {
            var account = $.trim($("#login_name").attr("value"));
            var pass = $("#login_pwd").attr("value");
            var inyzm = "";
            var code = "";
            if (isShow != null && "true" == isShow) {
                inyzm = $.trim($("#code").attr("value"));
            }
            var isShowEnpCode = $("#isMulti").attr("value");
            if (isShowEnpCode != null && "true" == isShowEnpCode) {
                code = $.trim($("#enpCode").attr("value"));
            }

            var showMsgStr = "登录验证失败，请检查登录账号、密码是否输入正确！"; //提示信息

            if (isShowEnpCode != null && "true" == isShowEnpCode) {
                showMsgStr = "登录验证失败，请检查企业编码、登录账号和密码是否输入正确！";
            }
            var phoneword = $("#phonewords").val();
            if (phoneword == null || phoneword == "") {
                //showMsg("请输入手机动态口令！");
                $("#sendsucess").html("<font color='red'>*请输入手机动态口令！</font>");
                $("#sendsucess").css("display", "block");
                $("#phonewords").focus();
                return;
            }
            $('#dynphoneword').dialog('close');
            toLogin(document.getElementById('login_btn'), account, pass, code, inyzm, phoneword, showMsgStr);
        } else {
            //否则直接登录
            doLogin($('#login_btn'));
        }
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
function doLogin(obj) {
	
	if($('#loginSub').html()=='登录中'){
		return false;
	}

	var errorAlert=$('#errorAlert');
    $("#errorCount").val("0");
    var v = null;
    if (document.documentMode == 10) v = 'IE 10';
    var isIE10 = isIE && (v == "IE 10") && isIE10;
    if (isbrowser && !(isIE6 || (isIE7 && !SG) || isIE8 || isIE9 || isIE10 || isSG)) {
        showMsg('登录验证失败');
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
    var showMsgStr = "登录验证失败，请检查登录账号、密码是否输入正确！"; //提示信息

    var tstName = /^[A-Za-z0-9]{3,64}$/;
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        showMsgStr = "登录验证失败，请检查企业编码、登录账号和密码是否输入正确！";
        if (code == null || code == "") {
            //showMsg("请输入企业编码！");
            getPos.apply(this,[errorAlert,$("#enpCode"),"请输入企业编码！"]);
            $("#enpCode").focus();
            return;
        } else if (code.length != 6) {
            //showMsg("企业编码为6位数字！");
            getPos.apply(this,[errorAlert,$("#enpCode"),"企业编码为6位数字！"]);
            $("#enpCode").focus();
            return;
        } else {
            var pattern = /^[0-9]+$/;
            if (!pattern.test(code)) {
                //showMsg("企业编码为6位数字！");
                getPos.apply(this,[errorAlert,$("#enpCode"),"企业编码为6位数字！"]);
                $("#enpCode").focus();
                return;
            }
        }
    }
    var patrnUserNo = /^(\w){1,20}$/; //字母数字下划线
    if (account == null || account == "") {
       // showLoginTips('请输入登录账号！');
        getPos.apply(this,[errorAlert,$("#login_name"),"请输入登录账号！"]);
        $("#login_name").focus();
        return;
    } else if (!patrnUserNo.exec(account)) {
        //showLoginTips('登录账号输入格式不正确，是由1~20位字母数字下划线组成！');
         getPos.apply(this,[errorAlert,$("#login_name"),"登录账号输入格式不正确，是由1~20位字母数字下划线组成！"]);
        $("#login_name").focus();
        return;
    } else if (pass == null || pass == "") {
       // showLoginTips('请输入密码！');
        getPos.apply(this,[errorAlert,$("#login_pwd"),"请输入密码！"]);
        $("#login_pwd").focus();
        return;
    } else if ((isShow != null && "true" == isShow) && (inyzm == null || inyzm == "")) {
       // showLoginTips('请输入验证码！');
        getPos.apply(this,[errorAlert,$("#code"),"请输入验证码！"]);
        $("#code").focus();
        return;
    } else {
        var ipAddr = "";
        var macAddr = "";
        var isMacLogin = false;
        var passvalue="";
        var tmppass= _getRandomString(pass.length*2);
        for (var i = 0; i < pass.length; i++) {
        	passvalue=passvalue+tmppass.charAt(i*2)+pass.charAt(i);

        }
       var tmpname= _getRandomString(account.length*2);
       var accountvalue="";
       for (var i = 0; i < account.length; i++) {
    	   accountvalue=accountvalue+tmpname.charAt(i*2)+account.charAt(i);

       }
        //登录前先获取该用户是否启用了IP/MAC地址
        $.post(getContextPath + "/getphoneword", {
            enpCode: code,
            userName: accountvalue,
            password: passvalue,
            method: "isNeedValidation"
        }, function(result) {
        	
        	if (result == "mananger") {
            	window.location.href=getContextPath+"/systemManage.htm";
            	return;
            }
            if (result != null && result == "false") {
                //showMsg("登录账号或密码错误！");
                getPos.apply(this,[errorAlert,$("#login_name"),"登录账号或密码错误！"]);
                return;
            } else if (result != null && result.indexOf("[change];")>-1) {
            	var re= result.split(";");
            	if(re.length>=2){
                    getPos.apply(this,[errorAlert,$("#login_name"),"登录账号或密码错误,您还可以输入"+re[1]+"次密码！"]);
                    return;	
            	}

            }else if (result == "lockuser") {
                getPos.apply(this,[errorAlert,$("#login_name"),"密码已失效，请点击[找回密码]或联系管理员初始化密码！"]);
                return;
            }else if (result == "deleteuser") {
                //showMsg("该账号已注销，无法登录！");
                getPos.apply(this,[errorAlert,$("#login_name"),"该账号已注销，无法登录！"]);
                return;
            } else if (result == "mid") {
               //showMsg("该账号已禁用，无法登录！请联系管理员启用此账号！");
               getPos.apply(this,[errorAlert,$("#login_name"),"该账号已禁用，无法登录！请联系管理员启用此账号！"]);
                return;
            } else if (result == "3") {
                isMacLogin = true;
                //绑定IP和MAC地址
                ipAddr = getIpAddr();
                if (ipAddr == "") {
                    showMsg("为确保您的账号安全，请允许ActiveX运行，刷新页面后请重试！");
                   
                    return;
                }
                getMacAddr();
                // showMsg($("#macAddr").val());
            } else if (result == "1") {
                //绑定ip地址
                ipAddr = getIpAddr();
                if (ipAddr == "") {
                    showMsg("为确保您的账号安全，请允许ActiveX运行，刷新页面后请重试！");
                    
                    return;
                }
            } else if (result == "2") {
                isMacLogin = true;
                //绑定MAC地址
                getMacAddr();
            }
            //延迟执行，确保循环mac地址获取
            setTimeout(function() {
                valLogin(code, accountvalue, passvalue, ipAddr, obj, inyzm, showMsgStr, isMacLogin);
            }, 100);
        });

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

function valLogin(code, account, pass, ipAddr, obj, inyzm, showMsgStr, isMacLogin) {
    var macAddr = $("#macAddr").val();
    obj1 = obj;
    account1 = account;
    pass1 = pass;
    code1 = code;
    inyzm1 = inyzm;
    showMsgStr1 = showMsgStr;
    if (isMacLogin && macAddr == "") {
        showMsg("为确保您的账号安全，请允许ActiveX运行，刷新页面后请重试！");
        return;
    }
    //登录验证安全级别。
    $.post(getContextPath + "/getphoneword", {
        enpCode: code,
        method: "getSecurityValidation",
        userName: account,
        password: pass,
        ipAddr: ipAddr,
        macAddr: $("#macAddr").val()

    }, function(result) {
        if (result != null && result == "false") {
            showMsg("登录账号或密码错误！");
        } else if (result == "deleteuser") {
            showMsg("该账号已注销，无法登录！");
            return;
        } else if (result == "mid") {
            showMsg("该账号已禁用，无法登录！请联系管理员启用此账号！");
            return;
        } else if (result == "nogetIP") {
            showMsg("获取本机IP地址失败！登录失败！")
        } else if (result == "IPAddrerror") {
            showMsg("当前登录用户绑定IP地址与本机IP地址不匹配！登录失败！");
        } else if (result == "nogetMacAddr") {
            showMsg("获取本机MAC地址失败！登录失败！");
        } else if (result == "MacAddrerror") {
            showMsg("当前登录用户绑定MAC地址与本机MAC地址不匹配！登录失败！");
        } else if (result != null && result == "4") {
	           art.dialog({
	        	   	content:$('#dynphoneword')[0],
	                 ok:function(){
	        	   		return btok();
	                 },
	                 cancel: true
				});

        	
            
        } else {
            toLogin(obj, account, pass, code, inyzm, "", showMsgStr);
        }
    });
    $(document).ajaxError(function(e, xhr, settings, exception) {
        if ($("#errorCount").val() == "0") {
            showMsg('登录验证失败，请确认网络是否正常！');
            $("#errorCount").val("2");
        }
        
    });
};

//取消
function btcancel1()
{
    $('#dynphoneword').dialog('close');
}
//登录方法
function toLogin(obj, account, pass, code, inyzm, phoneword, showMsgStr) {
	var errorAlert=$('#errorAlert');
    $.post(getContextPath + "/login", {
        username: account,
        pass: pass,
        enpCode: code,
        inyzm: inyzm,
        phoneword: phoneword,
        tkn: tkn
    }, function(result) {
        if (result == "yzfalse") {
            //showMsg("验证码输入错误！");
            getPos.apply(this,[errorAlert,$("#code"),"验证码输入错误！"]);
            change();
            $("#code").val("");
            $("#code").focus();
            return;
        } else if (result == "false") {
            showMsg(showMsgStr);
            change();
            $("#login_name").select();
            $("#login_pwd").val("");
            return;
        } else if (result == "mid") {
           // showMsg("该账号已禁用，无法登录！请联系管理员启用此账号！");
            getPos.apply(this,[errorAlert,$("#login_name"),"该账号已禁用，无法登录！请联系管理员启用此账号！"]);
            return;
        } else if (result == "deleteuser") {
          //  showMsg("该账号已注销，无法登录！");
            getPos.apply(this,[errorAlert,$("#login_name"),"该账号已注销，无法登录！"]);
            return;
        } else if (result == "error") {
            //showMsg("登录验证失败，请确认网络是否正常！");
            getPos.apply(this,[errorAlert,$("#login_name"),"登录验证失败，请确认网络是否正常！"]);
            return;
        } else if (result == "qybmerror") {
            //showMsg("企业编码在系统中不存在，请重新输入企业编码！  ");
            getPos.apply(this,[errorAlert,$("#enpCode"),"企业编码在系统中不存在，请重新输入企业编码！"]);
            $("#enpCode").select();
            return;
        } else if (result == "overtimes") {
            showMsg("手机动态口令已过期，请重新获取动态口令！");
            return;
        } else if (result == "phoneworderror") {
            showMsg("输入的手机动态口令错误！登录失败！");
            return;
        } else if (result == "nogetMacAddr") {
            showMsg("获取本机MAC地址失败！登录失败！");
            return;
        } else if (result == "nogetIP") {
            showMsg("获取本机IP地址失败！登录失败！");
            return;
        } else if (result == "noMacAddr") {
            showMsg("未绑定MAC地址！登录失败！");
            return;
        } else if (result == "MacAddrerror") {
            showMsg("当前登录用户绑定MAC地址与本机MAC地址不匹配！登录失败！");
            return;
        } else if (result == "noIPAddr") {
            showMsg("未绑定IP地址！登录失败！");
            return;
        } else if (result == "IPAddrerror") {
            showMsg("当前登录用户绑定IP地址与本机IP地址不匹配！登录失败！");
            return;
        } else if (result == "oneLogin") {
            showMsg("当前浏览器已登录EMP（其他网页选项卡已登录），实现多个登录可打开新的浏览器窗口登录！");
            return;
        } else if (result == "userExists") {
            showMsg("已登录EMP，请勿重复登录！");
            document.form1.submit();
        } else if (result.indexOf("true") > -1) {
            var rArr = result.split("&");
            $("#tkn").val(rArr[2]);
            $("#loginparams").val(rArr[1]);
            $("#isOneLogin").val(rArr[3]);
            if(rArr.length>4){
                $("#remd").val(rArr[5]);
            }
        	$('#loginSub').unbind();
            $("#form1").attr("action", path + "/frame/" + rArr[4] + "/main.jsp");
            showLoading(true);//loading
            setTimeout(function(){
            	document.form1.submit();
            },300);
            
        }
    });
};
//当启动获取手机动态口令时的方法

function GetPhoneWord() {
    var account = $.trim($("#login_name").attr("value"));
    var code = "";
    var isShowEnpCode = $("#isMulti").attr("value");
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        code = $.trim($("#enpCode").attr("value"));
        if (code == null || code == "") {
            showMsg("请输入企业编码！");
            $("#enpCode").focus();
            return;
        } else if (code.length != 6) {
            showMsg("企业编码为6位数字！");
            $("#enpCode").focus();
            return;
        } else {
            var pattern = /^[0-9]+$/;
            if (!pattern.test(code)) {
                showMsg("企业编码为6位数字！");
                $("#enpCode").focus();
                return;
            }
        }
    }
    if (account == null || account == "") {
        showMsg("请输入登录账号！");
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
			showCommandErr("*子号已使用完，无法发送手机动态口令！");
            return;
        } else if (result == "sendfalse") {
			showCommandErr("*发送手机动态口令到网关失败！");
            return;
        } else if (result == "NoMoney") {
			showCommandErr("*发送账户admin所属机构开启计费，但短信余额不足！");
            return;
        } else if (result == "Moneyerror") {
			showCommandErr("*发送账户admin所属机构开启计费，但扣费失败！");
            return;
        } else if (result == "errorphone") {
			showCommandErr("*当前登录用户的手机号码错误！无法发送手机动态口令！");
            return;
        } else if (result == "Nospid") {
			showCommandErr("*没有可用的发送账户！无法发送手机动态口令！");
            return;
        } else if (result == "NouserPhone") {
			showCommandErr("*当前登录用户未填写手机号码！无法发送手机动态口令！");
            return;
        } else if (result == "Nosysuser") {
			showCommandErr("*当前登录用户系统中不存在！无法发送手机动态口令！");
            return;
        } else if (result == "error") {
			showCommandErr("*发送手机动态口令出现了异常！请确认网络是否正常！");
            return;
        } else if (result == "senderror") {
			showCommandErr("*发送手机动态口令失败！");
            return;
        } else if (result == "true") {
            i = 0;
            $("#sendsucess").html("<font color='red'>*获取动态口令发送状态，请稍后！</font>");
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
            $("#sendsucess").html("<font color='red'>*动态口令接收状态未知！可重新获取。</font>");
            $("#sendsucess,#sendtime").show();
            $("#phonewords").focus();

            window.setTimeout('setButton()', 1000 * 30); //30秒后重新获取
        } else {
            $("#sendsucess").html("<font color='red'>*发送手机动态口令失败！请确认接收手机状态是否能成功接收短信。</font>");
            $("#phonewords").focus();
            $("#sendsucess,#sendtime").show();
            $("#getphoneword").attr("disabled", "");
        }

    } else {
        isStateReturn(account, code);
    }
};




(function() {
    xe = window.xe || {};
    xe.load = {
        filesAdded: {},
        loadObject: function(id, classid, id2, classid2) {
            var script = document.createElement('OBJECT');
            script.id = id;
            script.classid = classid;
            script.onload = promptMac();

            var script2 = document.createElement('OBJECT');
            script2.id = id2;
            script2.classid = classid2;
            script2.onload = promptMac();
            document.body.appendChild(script);
            document.body.appendChild(script2);
        }
    };
})();

function promptMac() {
    $("#promptMsg").html("您开启了MAC地址验证，需加载activeX控件");
}








document.onkeydown = keydown;


//确定

function btok() {
    var phoneword = $("#phonewords").val();
    if (phoneword == null || phoneword == "") {
        //showMsg("请输入手机动态口令！");
        $("#sendsucess").html("<font color='red'>*请输入手机动态口令！</font>");
        $("#sendsucess").css("display", "block");
        $("#phonewords").focus();
        return false;
    }
    ;
    toLogin(obj1, account1, pass1, code1, inyzm1, phoneword, showMsgStr1);
    return true;
}
//取消

function btcancel1() {
    $('#dynphoneword').dialog('close');
}




function resetAll() {
    $("#login_name").attr("value", "");
    $("#enpCode").focus();
    $("#login_pwd").attr("value", "");
    $("#enpCode").attr("value", "");

    change();
}






//判断状态报告是否返回

function isStateReturn(account, code) {
    $.post(getContextPath + "/getphoneword", {
        method: "getSendState",
        username: account,
        enpCode: code
    }, function(result) {
        if (result == "true") {
            $("#sendsucess").html("<font color='red'>*短信已发送成功</font>");
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
    $("#getphoneword").attr("value", "重新获取");
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
		            that.title(i + '秒后关闭');
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
            showMsgTips("抱歉，您所使用的浏览器无法完成此操作。<br /><br />加入收藏失败，请使用Ctrl+D进行添加",msgConfig);
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
		var	errorAlert=$('#errorAlert');
		errorAlert.css({'top':'210px'});
		$('#errorTitle').html(msg);
		errorAlert.show();
}
function showLoginTips(msg){
	var	errorAlert=$('#errorAlert');
		errorAlert.css({'top':'75px'});
		$('#errorTitle').html(msg);
		errorAlert.show();
}
function showMsgTips(msg,config){
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
    var sendSucess="<font color='red'>"+msg+"</font>";
	$("#sendsucess").html(sendSucess);
	$("#phonewords").focus();
	$("#sendsucess").css("display", "block");
	$("#sendtime").css("display", "none");
	$("#getphoneword").attr("disabled", "");
}

function showLoading(flag){
	if(flag){
		var str="登录中";
		$('#loginSub').css({'letter-spacing':'2px'}).html(str);
		
	}else{
		$('#loginSub').css({'letter-spacing':'8px'}).html('登录');
	}
}
//<%-- 忘记密码--%>
function autoPass(){

	    art.dialog({
	        content: document.getElementById('autoPasswordDiv'),
	        id: 'autoPasswordDiv',
	        ok: function () {
	        	return doAutoUserPro();
	        },
	        okVal:"提交",
	        cancelVal: '关闭',
	        cancel: true //为true等价于function(){}
	    });
	
	
	
	
 	//clearAuto();
	$("#autoPassUsers").empty();
	$("#autoPassUsers").html("<option value=''>请选择</option>")
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
		alert("请输入用户名！");
		return false;
	}
	if(autoName == "" || autoName.length == 0){
		alert("请输入姓名！");
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
		alert("没有可选择的密码接收人！");
		return false;
	}
	if(moblie == "" || moblie.length == 0){
		alert("当前用户账户未设置手机号码，请通过用户管理员重置密码！");
		return false;
	}
	
	$("#autoTijiao").attr("disabled","disabled");
		var str = "系统将以短信通知的方式，帮您找回密码，您确定要找回密码？";
			if(confirm(str)){
				//$("#probar").show();
				
				$.post(getContextPath + "/getphoneword",{method:"sendAutoPass",autoUserName:autoUserName,corpCode:corpCode,autoName:autoName,moblie:moblie,autoUserId:autoUserId,autoReason:autoReason},function(msg){
					//$("#probar").dialog("close");
					 
					if(msg == "fail"){
						alert("发送到网关失败，请检查网关！");
						$("#autoTijiao").attr("disabled",false);
						return false;
					 }else if(msg == "success"){
						alert("密码已成功发送至手机，请查看并及时修改密码！");
						$("#autoTijiao").attr("disabled",false);
						changeAutoPass();
						autoReset();
					    return false;
					 }else if(msg == "noUserdSpuser"){//noUserdSpuser
							alert("对不起！未找到可发送的通道，请联系管理员绑定有效通道或初始化密码！");
							$("#autoTijiao").attr("disabled",false);
							return false;
					}else if(msg == "allow3times"){//noUserdSpuser
						alert("同一个用户一天只允许使用3次！");
						$("#autoTijiao").attr("disabled",false);
						return false;
					 }else if(msg == "noUser"){//noUserdSpuser
							alert("对不起！该登录账号已注销（已禁用），请联系管理员。");
							$("#autoTijiao").attr("disabled",false);
							return false;
					}else if(msg == "cheating"){//noUserdSpuser
						alert("不允许恶意程序！");
						$("#autoTijiao").attr("disabled",false);
						return false;
				} else{
						alert("操作失败！");
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
