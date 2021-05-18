
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
            $('#name_label').hide();
        }
        if (v2 != '') {
            $('#pw_label').hide();
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
    art.dialog.open(path + "/frame.hts?method=showAbout", aboutConfig);
};
//验证码
function change() {
    $("#verifyImg").attr("src", getContextPath + "/changeyzm?timer=" + new Date().getTime());
    return false;
};
//按回车登录 支持ie 火狐 键盘按下事件
function keydown(e){
    var currKey = 0,
        e = e || event;
    if (e.keyCode == 13) {
        //如果弹出的输入验证码的div不是隐藏的则需要判断验证码信息
        if ($("#dynphoneword").is(":visible")) {
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
};
function doClickStyle(obj, objclassname) {
    document.getElementById(obj).className = objclassname;
};
function doLogin(obj) {
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
    var patrnUserNo = /^(\w){1,20}$/; //字母数字下划线
    if (account == null || account == "") {
        showLoginTips('请输入登录账号！');
        $("#login_name").focus();
        return;
    } else if (!patrnUserNo.exec(account)) {
        showLoginTips('登录账号输入格式不正确，是由1~20位字母数字下划线组成！');
        $("#login_name").focus();
        return;
    } else if (pass == null || pass == "") {
        showLoginTips('请输入密码！');
        $("#login_pwd").focus();
        return;
    } else if ((isShow != null && "true" == isShow) && (inyzm == null || inyzm == "")) {
        showLoginTips('请输入验证码！');
        $("#code").focus();
        return;
    } else {
    	toLogin(obj, account, pass, code, inyzm)
    }
};
//登录方法
function toLogin(obj, account, pass, code, inyzm) {
    $.post(getContextPath + "/login", {
        username: account,
        pass: pass,
        enpCode: code,
        inyzm: inyzm
    }, function(result) {
        if (result == "yzfalse") {
            showMsg("验证码输入错误！");
            change();
            $("#code").val("");
            $("#code").focus();
            return;
        } else if (result == "false") {
            change();
            $("#login_name").select();
            $("#login_pwd").val("");
            return;
        } else if (result == "mid") {
            showMsg("该账号已禁用，无法登录！请联系管理员启用此账号！");
            return;
        } else if (result == "deleteuser") {
            showMsg("该账号已注销，无法登录！");
            return;
        } else if (result == "error") {
            showMsg("登录验证失败，请确认网络是否正常！");
            return;
        } else if (result == "qybmerror") {
            showMsg("企业编码在系统中不存在，请重新输入企业编码！  ");
            $("#enpCode").select();
            return;
        }  else if (result == "oneLogin") {
            showMsg("当前浏览器已登录EMP（其他网页选项卡已登录），实现多个登录可打开新的浏览器窗口登录！");
            return;
        } else if (result == "userExists") {
            showMsg("已登录EMP，请勿重复登录！");
            document.form1.submit();
        } else if (result.indexOf("true") > -1) {
            var rArr = result.split("&");
            //$("#tkn").val(rArr[1]);
            //$("#loginparams").val(rArr[1]);
            var permissiontype = rArr[2];
            if(permissiontype == 3)
            {
                $("#form1").attr("action", path + "/customChat.htm");
            }else
            {
                $("#form1").attr("action", path + "/frame/"+rArr[1]+"/main.jsp");
            }
            showLoading(true);//loading
            setTimeout(function(){
            	document.form1.submit();
            },1000);
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
	
	$('#tips .notice_text').html(msg);
	$('#tips').css({'visibility':'visible'});
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
function showLoginTips(msg){
	$('#tips .notice_text').html(msg);
	$('#tips').css({'visibility':'visible'});
}
function showLoading(flag){
	if(flag){
		$('#login_btn').html('<span><img src="common/img/loading.gif">登录中</span>').addClass('login_btn_active');
	}else{
		$('#login_btn').html(' <span>登&nbsp;录</span>').removeClass('login_btn_active');
	}
}