
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
    var isIE10 = isIE && (v == "IE 10") && isIE10x;
    if (isbrowser && !(isIE6 || (isIE7 && !SG) || isIE8 || isIE9 || isIE10 || isSG)) {
        /*当前浏览器不可用*/
        showMsg(getJsLocaleMessage("common","common_frame2_login_31"));
        return;
    }
	$("#probar").dialog({
		modal:true,
		title:getJsLocaleMessage("common","common_frame2_login_86"),
		height:120,
		resizable :false,
		closeOnEscape: false,
		width:250
	});
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
    /*您开启了IP地址验证，需加载ActiveX控件*/
    $("#promptMsg").html(getJsLocaleMessage("common","common_frame2_login_32"));
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
        title: getJsLocaleMessage("common","common_aboutPlatform"),
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
            /*登录验证失败，请检查登录账号、密码是否输入正确！*/
            var showMsgStr = getJsLocaleMessage("common","common_frame2_login_33"); //提示信息

            /*登录验证失败，请检查企业编码、登录账号和密码是否输入正确！*/
            if (isShowEnpCode != null && "true" == isShowEnpCode) {
                showMsgStr = getJsLocaleMessage("common","common_frame2_login_34");
            }
            var phoneword = $("#phonewords").val();
            if (phoneword == null || phoneword == "") {
                //showMsg("请输入手机动态口令！");
                $("#sendsucess").html("<font color='red'>*"+getJsLocaleMessage("common","common_frame2_login_35")+"</font>");
                $("#sendsucess").css("display", "block");
                $("#phonewords").focus();
                return;
            }
            $('#dynphoneword').dialog('close');
            toLogin(document.getElementById('login_btn'), account, pass, code, inyzm, phoneword, showMsgStr);
        } else {
        	//增加TAB事件的控制
            if (e.keyCode == 9) {               
                if(document.activeElement.id == "code"){
                    doLogin($('#login_btn'));
                }
            }
            //否则直接登录
            doLogin($('#login_btn'));
        }
    }
    //去掉页面上找回密码的焦点
    if (e.keyCode == 9) {               
        if(document.activeElement.id == "code"){
           return false;
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
    var isIE10 = isIE && (v == "IE 10") && isIE10x;
    if (isbrowser && !(isIE6 || (isIE7 && !SG) || isIE8 || isIE9 || isIE10 || isSG)) {
        /*登录验证失败*/
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
    /*登录验证失败，请检查登录账号、密码是否输入正确！*/
    var showMsgStr = getJsLocaleMessage("common","common_frame2_login_33"); //提示信息

    var tstName = /^[A-Za-z0-9]{3,64}$/;
    if (isShowEnpCode != null && "true" == isShowEnpCode) {
        /*登录验证失败，请检查企业编码、登录账号和密码是否输入正确！*/
        showMsgStr = getJsLocaleMessage("common","common_frame2_login_34");
        if (code == null || code == "") {
            /*请输入企业编码！*/
            showMsg(getJsLocaleMessage("common","common_frame2_login_37"));
            $("#enpCode").focus();
            return;
        } else if (code.length != 6) {
            /*企业编码为6位数字！*/
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
    var patrnUserNo = /^(\w){1,20}$/; //字母数字下划线
    if (account == null || account == "") {
        /*请输入登录账号！*/
        showLoginTips(getJsLocaleMessage("common","common_frame2_login_39"));
        $("#login_name").focus();
        return;
    } else if (!patrnUserNo.exec(account)) {
        /*登录账号输入格式不正确，是由1~20位字母数字下划线组成！*/
        showLoginTips(getJsLocaleMessage("common","common_frame2_login_40"));
        $("#login_name").focus();
        return;
    } else if (pass == null || pass == "") {
        /*请输入密码！*/
        showLoginTips(getJsLocaleMessage("common","common_frame2_login_41"));
        $("#login_pwd").focus();
        return;
    } else if ((isShow != null && "true" == isShow) && (inyzm == null || inyzm == "")) {
        /*请输入验证码！*/
        showLoginTips(getJsLocaleMessage("common","common_frame2_login_42"));
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
        //赋值密码输入框为混淆的密码，避免暴露明文
        $('#login_pwd').val(passvalue);
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
        	     /*"登录账号或密码错误！"*/
                showMsg(getJsLocaleMessage("common","common_frame2_login_43"));
                
                return;
            }else if (result == "lockuser") {
        	     /*"密码已经失效，请联系管理员初始化密码！"*/
            	var tipInfo = getJsLocaleMessage("common","common_frame2_login_44");
            	if($("#corptype").val() !="true"){
            	    /*"密码已经失效，请点击[找回密码]或联系管理员初始化密码！";*/
            		tipInfo = getJsLocaleMessage("common","common_frame2_login_45");
            	}else{
            		tipInfo = getJsLocaleMessage("common","common_frame2_login_44");
            	}
                getPos.apply(this,[errorAlert,$("#login_name"),tipInfo]);
                return;
            } else if (result == "deleteuser") {
                showMsg( getJsLocaleMessage("common","common_frame2_login_46"));
                
                return;
            } else if (result == "mid") {
                showMsg( getJsLocaleMessage("common","common_frame2_login_47"));
               
                return;
            } else if (result == "3") {
                isMacLogin = true;
                //绑定IP和MAC地址
                ipAddr = getIpAddr();
                if (ipAddr == "") {
                    showMsg(getJsLocaleMessage("common","common_frame2_login_48"));
                   
                    return;
                }
                getMacAddr();
                // showMsg($("#macAddr").val());
            } else if (result == "1") {
                //绑定ip地址
                ipAddr = getIpAddr();
                if (ipAddr == "") {
                    showMsg(getJsLocaleMessage("common","common_frame2_login_48"));
                    
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
        showMsg(getJsLocaleMessage("common","common_frame2_login_48"));
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
            showMsg(getJsLocaleMessage("common","common_frame2_login_43"));
        } else if (result == "deleteuser") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_46"));
            return;
        } else if (result == "mid") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_47"));
            return;
        } else if (result == "nogetIP") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_49"))
        } else if (result == "IPAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_50"));
        } else if (result == "nogetMacAddr") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_51"));
        } else if (result == "MacAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_52"));
        } else if (result != null && result == "4") {
        	art.dialog({
			    content: document.getElementById('dynphoneword'),
			    id: 'dynphoneword',
			    ok: function () {
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
            showMsg(getJsLocaleMessage("common","common_frame2_login_53"));
            $("#errorCount").val("2");
        }
        
    });
};
//登录方法
function toLogin(obj, account, pass, code, inyzm, phoneword, showMsgStr) {
	pass = Base64.encode(pass);
    $.post(getContextPath + "/login", {
        username: account,
        pass: pass,
        enpCode: code,
        inyzm: inyzm,
        phoneword: phoneword,
        tkn: tkn
    }, function(result) {
        if (result == "yzfalse") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_54"));
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
            showMsg(getJsLocaleMessage("common","common_frame2_login_47"));
            return;
        } else if (result == "deleteuser") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_46"));
            return;
        } else if (result == "error") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_53"));
            return;
        } else if (result == "qybmerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_55"));
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
        } else if (result == "lockCorp") {
        	showMsg(getJsLocaleMessage("common","common_frame3_login_lockCorp"));
            return;
        } else if (result == "noMacAddr") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_51"));
            return;
        } else if (result == "MacAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_52"));
            return;
        } else if (result == "noIPAddr") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_58"));
            return;
        } else if (result == "IPAddrerror") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_50"));
            return;
        } else if (result == "oneLogin") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_59"));
            return;
        }//remd
        else if(result != null && result != "" && result.substring(0,result.length-1) == "errorPass"){
		    var times = result.substring(result.length-1,result.length);
		    if(times <6 ){
		    	alert(getJsLocaleMessage("common","common_frame2_login_60")+times+getJsLocaleMessage("common","common_frame2_login_61"));
		    }else{
		    	alert(getJsLocaleMessage("common","common_frame2_login_62"));
		    }
		} 
        else if (result == "userExists") {
            showMsg(getJsLocaleMessage("common","common_frame2_login_63"));
            document.form1.submit();
        } else if (result.indexOf("true") > -1) {
            var rArr = result.split("&");
            $("#tkn").val(rArr[2]);
            $("#loginparams").val(rArr[1]);
            $("#isOneLogin").val(rArr[3]);
            if(rArr.length>4){
            	$("#remd").val(rArr[5]);
            }
            //登录之后取消绑定
            $('#loginSub').unbind();
            $("#form1").attr("action", path + "/frame/" + rArr[4] + "/main.jsp");
            showLoading(true);//loading
            document.form1.submit();
        }
        else if(result.indexOf("false") > -1)
    	{
    		showMsg(getJsLocaleMessage("common","common_frame2_login_64"));
            return;
    	}
        else
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
    $("#promptMsg").html(getJsLocaleMessage("common","common_frame2_login_79"));
}








document.onkeydown = keydown;


//确定

function btok() {
    var phoneword = $("#phonewords").val();
    if (phoneword == null || phoneword == "") {
        //showMsg("请输入手机动态口令！");
        $("#sendsucess").html("<font color='red'>*"+getJsLocaleMessage("common","common_frame2_login_35")+"</font>");
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
				height:50,
				fixed: true,
				lock: true,
				background: "#000",
				opacity: 0,
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
	art.dialog(msgConfig);
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
		$('#login_btn').html("<span><img src=' + iPath + '/img/loading.gif>'"+getJsLocaleMessage("common","common_frame2_login_85")+"</span>").addClass('login_btn_active');
	}else{
		$('#login_btn').html(" <span>"+getJsLocaleMessage("common","common_frame2_login_13")+"</span>").removeClass('login_btn_active');
	}
}