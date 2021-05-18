var empLangName = $("#empLangName").val();
$(document).ready(function () {
    $("#uChildren").hover(
        function () {
            var $par = $(window.frames["topFrame"].document);
            $par.find("#sysmenu").removeClass("selected");
            $par.find("#sysmenu").addClass("onSys");
            $("#ifr").show();
            $("#dd").show();
        }, function () {
            var $par = $(window.frames["topFrame"].document);
            var onSys = $par.find("#onSys").val();
            if (onSys == "1") {
                $par.find("#sysmenu").removeClass("onSys");
            } else {
                $par.find("#sysmenu").removeClass("onSys");
            }

            $("#dd").hide();
            $("#ifr").hide();
        });
    sizeset();
    //设置发送内容参数值
    $.post(emppatch + "/common.htm?method=setSendInfoMap");
    //如果用户名与密码相同则要求用户修改密码
    var a = isOneLogin;
    if (a == "NameAndPWordSame") {
        doUpdatePass();
        changeinfo(2);
    }

    if (noticeTxt != null && noticeTxt.length > 0) {
        notice_tips_dialog(noticeTxt);
    }

    $("#skinDiv").dialog({
        title: getJsLocaleMessage("common", "common_changeSkin"),
        autoOpen: false,
        height: 520,
        width: 755,
        resizable: false,
        closeOnEscape: false,
        modal: true,
        close: function () {
            //old_skin = old_skin2;
            changeSkin(old_skin2);
        }
    });
    $("#languageDiv").dialog({
        title: getJsLocaleMessage("common", "common_changeLanguage"),
        autoOpen: false,
        height: 220,
        width: 555,
        resizable: false,
        closeOnEscape: false,
        modal: true,
        close: function () {
            //old_skin = old_skin2;
            changeSkin(old_skin2);
        }
    });
    $("#complainDiv").dialog({
        title: getJsLocaleMessage("common", "common_wantToFeedback"),
        autoOpen: false,
        width: "zh_HK" === empLangName ? 650 : 540,
        resizable: false,
        closeOnEscape: false,
        modal: true,
        close: function () {

        }
    });
})


function changeSkin2(new_skin, themecode) {
    if (frameCode != themecode) {
        chooseSkin(new_skin);
        otherSkin = new_skin;
        return;
    } else {
        changeSkin(new_skin);
    }
}

function changeSkin(new_skin) {
    chooseSkin(new_skin);
    if (old_skin == new_skin) {
        return;
    }
    var $hcss = $("head").find("link");
    var old_skin_css = "/skin/" + old_skin + "/";
    var new_skin_css = "/skin/" + new_skin + "/";
    for (var i = 0; i < $hcss.length; i = i + 1) {
        var csshref = $hcss.eq(i).attr("href");

        if (csshref.indexOf(old_skin_css) > -1) {
            csshref = csshref.replace(old_skin_css, new_skin_css);
            //alert(csshref);
            $hcss.eq(i).attr("href", csshref);
        }
    }
    //replaceSkin(1,new_skin);
    changeSkin_loop(1, new_skin);
    var $topob = $("iframe[name='topFrame']").contents().find(".top_logo");
    var logosrc = $topob.css("background-image");
    var filtersrc = $topob.css("filter");
    logosrc = logosrc.replace(old_skin_css, new_skin_css);
    filtersrc = filtersrc.replace(old_skin_css, new_skin_css);
    $topob.css("filter", filtersrc);
    $topob.css("background-image", logosrc);
    old_skin = new_skin;
    $('#topFrame')[0].contentWindow.findinfo();
}

function changeSkin_loop($paob, new_skin) {
    try {
        if ($paob != null) {
            if ($paob == 1) {
                $paob = $("body").find("iframe");
            } else {
                $paob = $paob.contents().find("iframe");
            }
            for (var b = 0; b < $paob.length; b = b + 1) {
                replaceSkin($paob.eq(b), new_skin);
                changeSkin_loop($paob, new_skin);
            }
        }
    } catch (err) {

    }

}

function replaceSkin($docu, new_skin) {

    var $hcss = $docu.contents().find("link");
    for (var i = 0; i < $hcss.length; i = i + 1) {
        var csshref = $hcss.eq(i).attr("href");
        var old_skin_css = "/skin/" + old_skin + "/";
        var new_skin_css = "/skin/" + new_skin + "/";
        if (csshref.indexOf(old_skin_css) > -1) {
            csshref = csshref.replace(old_skin_css, new_skin_css);
            //alert(csshref);
            $hcss.eq(i).attr("href", csshref);
        } else if (csshref.indexOf("index-v2")) {   // 兼容新的查询统计模块, 并不影响原来的逻辑
            var tmp_old_skin_css = "index-v2-" + old_skin;
            var tmp_new_skin_css = "index-v2-" + new_skin;
            if (csshref.indexOf(tmp_old_skin_css) > -1) {
                csshref = csshref.replace(tmp_old_skin_css, tmp_new_skin_css);
                $hcss.eq(i).attr("href", csshref);
            }
        }
    }
}

function checkMenuSkin(skincode) {
    if (skincode != old_skin) {
        old_skin = skincode;
        var $mainFrame = $("iframe[name='mainFrame']");
        replaceSkin($mainFrame.contents().find("#menuFrame iframe:visible"), old_skin2);
        changeSkin_loop($mainFrame.contents().find("#menuFrame iframe:visible"), old_skin2);
        old_skin = old_skin2;
    }
}

function skinSure() {
    if (old_skin2 != old_skin || frameCode != selTheme) {
        old_skin2 = old_skin;
        if (frameCode != selTheme) {
            old_skin = otherSkin;
        }
        $.post(emppatch + "/emp_tz.htm", {
            method: "setSkin",
            skin: old_skin,
            themeCode: selTheme,
            userid: $("#lguserid").val(),
            isAsync: "yes"
        }, function (result) {
            if (result == "outOfLogin") {
                $("#logoutalert").val(1);
                location.href = getContextPath + "/common/logoutEmp.jsp";
                return;
            }
            if (frameCode != selTheme) {
                $("#themeForm").attr("action", emppatch + "/frame/" + selTheme + "/main.jsp");
                $("#themeForm").submit();
            }
        });
    }
    $("#skinDiv").dialog("close");
}

function complainSure() {
    $("#tipSpan").remove();
    var ctitle = $("#ctitle").val();
    var ctype = $("#ctype").val();
    var ccontent = $("#ccontent").val();
    var cemail = $("#cemail").val();
    var cphone = $("#cphone").val();
    var lguserid = $("#lguserid").val();
    if (cemail != "") {
        //对电子邮箱的验证
        var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if (!myreg.test(cemail)) {
            alert(getJsLocaleMessage("common", "common_frame2_contacts_23"));
            $("#cemail").focus();
            return;
        }
    }
    if (ctype == 2 || ctype == 3) {
        if (cphone == "" && cemail == "") {
            alert(getJsLocaleMessage("common", "common_frame2_contacts_24"));
            return;
        }
    }
    if ($.trim(ccontent).length < 5) {
        alert(getJsLocaleMessage("common", "common_frame2_contacts_25"));
        return;
    }
    $("#com_but_ok").attr("disabled", "disabled");
    $("#com_but_no").attr("disabled", "disabled");
    $.post(emppatch + "/complain.htm", {
        method: "find",
        ctitle: ctitle,
        ctype: ctype,
        ccontent: ccontent,
        cemail: cemail,
        cphone: cphone,
        lguserid: lguserid,
        isAsync: "yes"
    }, function (result) {
        if (result == "true") {
            if (ctype == 1) {
                alert(getJsLocaleMessage("common", "common_frame2_contacts_26"));
            } else {
                alert(getJsLocaleMessage("common", "common_frame2_contacts_27"));
            }
            $("#complainDiv").dialog("close");
        } else if (result == "false") {
            $("#complain").append("<span id='tipSpan' style='color:red'>" + getJsLocaleMessage("common", "common_frame2_contacts_28") + "<br/>" + getJsLocaleMessage("common", "common_frame2_contacts_29") + "<a onclick='javascript:copyToClipboard()' id='source'>http://61.145.229.29:8015/mboss/sug.aspx?version=" + version + "</a></span>");
        }
        $("#com_but_ok").attr("disabled", "");
        $("#com_but_no").attr("disabled", "");
    });

}

//取消选择皮肤

function skinNoGood() {
    $("#skinDiv").dialog("close");
    if (old_skin2 != old_skin) {
        //old_skin = old_skin2;
        changeSkin(old_skin2);
    }
}

//取消投诉

function complainNoGood() {
    $("#complainDiv").dialog("close");

}

//选中皮肤

function chooseSkin(skinstr) {
    if ($(".curSkin:visible").length > 0) {
        $(".curSkin:visible").removeClass("div_bd");
        $(".curSkin:visible").removeClass("title_bg");
        $(".curSkin:visible").removeClass("curSkin");
    }
    $(".skin_" + skinstr + ":visible").addClass("curSkin");
    $(".skin_" + skinstr + ":visible").addClass("div_bd");
    $(".skin_" + skinstr + ":visible").addClass("title_bg");
}

//打开皮肤选择框

function openSkinDiv() {
    $("#skinDiv").dialog("open");
    if ($("#skinlist").html() == "") {
        $.post(emppatch + "/emp_tz.htm", {
            method: "getTheme",
            patch: '"' + iPath + '"',
            isAsync: "yes"
        }, function (resutlHtml) {
            if (resutlHtml == "outOfLogin") {

                $("#logoutalert").val(1);
                location.href = getContextPath + "/common/logoutEmp.jsp";
            }
            //$("#themeDiv").html(resutlHtml);
        });
        $.post(emppatch + "/emp_tz.htm", {
            method: "getSkinList",
            patch: emppatch + "/common",
            isAsync: "yes"
        }, function (resutlHtml) {
            if (resutlHtml == "outOfLogin") {
                $("#logoutalert").val(1);
                location.href = getContextPath + "/common/logoutEmp.jsp";
            }
            var str = resutlHtml;
            str = str.replace(/经典蓝（默认）/g, getJsLocaleMessage("common", "common_skinname_8"));
            str = str.replace(/Wood风/g, getJsLocaleMessage("common", "common_skinname_9"));
            str = str.replace(/经典绿/g, getJsLocaleMessage("common", "common_skinname_10"));
            str = str.replace(/经典红/g, getJsLocaleMessage("common", "common_skinname_11"));
            $("#skinlist").html(str);
            chooseSkin(old_skin);
            $(".perSkin").hover(function () {
                $(this).toggleClass("div_bg");
            }, function () {
                $(this).toggleClass("div_bg");
            });
        });
    } else {
        chooseSkin(old_skin);
    }
}

//
function openLanguageDiv() {
    $("#languageDiv").dialog("open");
}


/**
 * 修改语言
 * @param type
 * @return
 */
function changeLanguage() {
    var lang = $('input:radio[name="setLanguage"]:checked').val();
    if (lang != null && lang != "") {
        if (confirm(getJsLocaleMessage("common", "common_frame2_main_62"))) {
            if (lang == 'zh_TW' || lang == 'zh_HK' || lang == 'zh_CN') {
                var pathUrl = $("#pathUrl").val();
                $.post(pathUrl + "/emp_tz.htm", {
                    method: "setLangeuage",
                    lang: lang,
                    userid: $("#lguserid").val(),
                    isAsync: "yes"
                }, function (result) {
                    if (result == "true") {
                        var menu = $("#menu").html();
                        var menuInfo = $("#menuInfo").html();
                        if (menu != null && menu != '') {
                            var arr = menuInfo.split(",");
                            $("#idstr").html(arr[1]);
                            $('#topFrame').attr('src', $('#topFrame').attr('src'));
                            $(window.parent.document).contents().find("#mainFrame")[0].contentWindow.showLeftMenu1(menu);
                        } else {
                            $("#themeForm").attr("action", pathUrl + "/frame/" + "frame2.5" + "/main.jsp");
                            $("#themeForm").submit();
                        }
                    }
                    $("#languageDiv").dialog('close');
                });
            }
        }
    }

}

var selTheme = frameCode;

//获取指定主题的皮肤

function getThemeSkin(theme) {

    if (selTheme == theme) {
        return;
    } else {
        $(".themeSpan").each(function () {
            var thcode = $(this).attr("themeCode");
            var baim = $(this).css("background-image");
            if (theme == thcode && selTheme != thcode) {
                $(this).addClass("fontsel").parent().addClass('selected');
                //$(this).css("background-image",baim.replace(".png","_sel.png"));
            } else {
                $(this).removeClass("fontsel").parent().removeClass('selected');
                //$(this).css("background-image",baim.replace("_sel.png",".png"));
            }
        });
        selTheme = theme;
    }
    if ($(".themeSkin[framecode='" + theme + "']").html() != null) {
        $(".themeSkin:visible").hide();
        $(".themeSkin[framecode='" + theme + "']").show();
        return;
    }
    $(".themeSkin:visible").hide();
    $.post(emppatch + "/emp_tz.htm", {
        method: "getSkinList",
        themecode: theme,
        isAsync: "yes"
    }, function (resutlHtml) {
        if (resutlHtml == "outOfLogin") {
            $("#logoutalert").val(1);
            location.href = getContextPath + "/common/logoutEmp.jsp";
        }
        var str = resutlHtml;
        str = str.replace(/极简蓝（默认）/g, getJsLocaleMessage("common", "common_skinname_1"));
        str = str.replace(/新春贺岁/g, getJsLocaleMessage("common", "common_skinname_2"));
        str = str.replace(/午后时光/g, getJsLocaleMessage("common", "common_skinname_3"));
        str = str.replace(/化猫物语/g, getJsLocaleMessage("common", "common_skinname_4"));
        str = str.replace(/炫动心情/g, getJsLocaleMessage("common", "common_skinname_5"));
        str = str.replace(/清晨拂晓/g, getJsLocaleMessage("common", "common_skinname_6"));
        str = str.replace(/清爽夏日/g, getJsLocaleMessage("common", "common_skinname_7"));
        $("#skinlist").append(str);
        $(".perSkin:visible").eq(0).trigger("click");
        $(".themeSkin[framecode='" + theme + "']").find(".perSkin").hover(function () {
            $(this).toggleClass("div_bg");
        }, function () {
            $(this).toggleClass("div_bg");
        });
    });
}

function getThemeSkin2(theme) {

    if (selTheme == theme) {
        return;
    } else {
        selTheme = theme;
    }
}

//打开投诉建议框
//打开投诉建议框

function openComplainDiv() {
    $("#tipSpan").remove();
    var frameSrc = $("#tempFrame").attr("src");
    frameSrc = "contacts.jsp";
    $("#tempFrame").attr("src", frameSrc);
    $("#complainDiv").dialog("open");
}

function doOpen(priMenus) {
    //var $par = $(window.frames["topFrame"].document);
    var ismiddel = $("iframe[name='mainFrame']").contents().find("#isMiddel").val();
    if (ismiddel == 0) {
        window.frames["mainFrame"].location.href = iPath + "/middel.jsp?priMenus=" + priMenus;
    } else {
        //alert($par.find("#tle").html());
        $(window.frames["mainFrame"].document).find("#leftIframe")[0].contentWindow.showMod(priMenus);

        //$par.find("#onSys").attr("value","2");
    }
    $("#dd").hide();
}

//按回车登录
document.onkeydown = keydown;

function keydown(e) //支持ie 火狐 键盘按下事件      
{
    var currKey = 0,
        e = e || event;
    if (e.keyCode == 13) {
        //如果这个div不是隐藏的
        if (!$("#UpdatePassDiv").is(":hidden")) {
            checkForm();
        }
    }
}

//取消按钮事件

function doReset() {
    var a = isOneLogin;
    if (a == "NameAndPWordSame" && !isuppw || isPassOverdue == "true") {
        //如果在第一次登录时修改密码点了取消,则直接到登录页面
        $("#logoutalert").val(1);
        window.location.replace(getContextPath + '/logout');
    }
    $("#UpdatePassDiv").dialog("close");
}

function closeDiv() {
    var a = isOneLogin;
    if (a == "NameAndPWordSame" && !isuppw || isPassOverdue == "true") {
        //如果在第一次登录时修改密码点了取消,则直接到登录页面
        $("#logoutalert").val(1);
        window.location.replace(getContextPath + '/logout');
    }
    $("#UpdatePassDiv").find("input[type='text']").val("");
    $("#UpdatePassDiv").find("input[type='password']").val("");
    $("#zhu").html("");
    $('#zhuPass').html("<font style='color : #FF0000'>" + getJsLocaleMessage("common", "common_frame2_main_40") + "</font>");
}

//显示公告

function showGg() {
    var ggCode = GGCODE;
    var ismiddel = $("iframe[name='mainFrame']").contents().find("#isMiddel").val();
    if (ismiddel == 0) {
        $("iframe[name='mainFrame']").attr("src", iPath + "/middel.jsp?openMenuCode=" + ggCode);
    } else if (ismiddel == 1) {
        var $lf = $("iframe[name='mainFrame']").contents().find("iframe[name='I1']");
        $lf[0].contentWindow.showTabMenClk(ggCode);
        $lf.contents().find("a[id='ak" + ggCode + "']").trigger("click");
    }
}

function numberControl(va) {
    var pat = /^\d*$/;
    if (!pat.test(va.val())) {
        va.val(va.val().replace(/[^\d]/g, ''));
    }
}

function focusShow(ep) {
    ep.next("label").css("display", "none");
}

function countRestrict(ep) {
    if (ep.val().length > 200) {
        ep.val(ep.val().substring(0, 200));
    }
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

function restrictCount() {
    var ccontent = $("#ccontent").val();
    if (ccontent.length > 200) {
        $("#ccontent").val($("#ccontent").val().substring(0, 200));
    }
}

function copyToClipboard() {
    var d = $("#source").html();
    window.clipboardData.setData('text', d);
    alert(getJsLocaleMessage("common", "common_frame2_main_63"));
}

function showAbout() {
    $("#aboutDiv").dialog({
        title: getJsLocaleMessage("common", "common_aboutPlatform"),
        autoOpen: false,
        width: empLangName === "zh_HK" ? 650 : 526,
        height: 320,
        resizable: false,
        closeOnEscape: false,
        modal: true,
        close: function () {
        }
    });
    $("#aboutFrame").attr("src", emppatch + "/emp_tz.hts?method=showAbout");
    $("#aboutDiv").dialog("open");
}

//-----------监控js处理-----------
//获取监控错误数据
function getErrMon() {
    var pathUrl = $("#pathUrl").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var lguserid = $("#lguserid").val();
    var time = new Date();
    $.post(pathUrl + "/mon_mainMon.htm",
        {
            method: "getErrMon",
            lguserid: lguserid,
            lgcorpcode: lgcorpcode,
            isAsync: "yes",
            time: time
        },
        function (result) {
            if (result == "outOfLogin") {
                location.href = pathUrl + "/common/logoutEmp.jsp";
                return;
            }
            else if (result != "error") {
                var MonMsgHtml = "";
                if (result != "[]") {
                    var monMsgJson = eval("(" + result + ")");
                    for (var i = 0; i < monMsgJson.length; i++) {
                        MonMsgHtml = MonMsgHtml + "<dl onclick='toErrMon(" + monMsgJson[i].id + ")' style='cursor: pointer;'><dt>" + monMsgJson[i].evttime + "</dt><dd>" + monMsgJson[i].monname + "：" + monMsgJson[i].msg + "</dd></dl>";
                    }
                    $("#warning_info").html(MonMsgHtml);
                }
                //无数据
                else {
                    MonMsgHtml = getJsLocaleMessage("common", "common_frame2_main_64");
                    $("#warning_info").html(MonMsgHtml);
                }
            }
            tiemrErrMon = window.setTimeout(getErrMon, 30 * 1000);
        });
}

//打开监控有下脚弹出框
function openMonDialog() {
    if (tiemrErrMon != null) {
        clearTimeout(tiemrErrMon);
    }
    $("#jkBox").show();
    $('#jkBox #warning_info').show();
    var pathUrl = $("#pathUrl").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var lguserid = $("#lguserid").val();
    var close_mon_State = $("#close_mon_State").val();
    getErrMon();
    if (close_mon_State == 2) {
        openNewTabMon("2900-1000", pathUrl + "/mon_realTimeAlarm.htm?method=find&herfType=1&lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid);
    }
    $("#close_mon_State").val("2");
}

//关闭右下角弹出框
function clioseMonDialog() {

    if (tiemrErrMon != null) {
        clearTimeout(tiemrErrMon);
    }
    $("#jkBox").hide();
}

//打开监控主界面
function openMonMain() {
    if (tiemrErrMon != null) {
        clearTimeout(tiemrErrMon);
    }
    $("#jkBox").hide();
    var lgcorpcode = $("#lgcorpcode").val();
    var lguserid = $("#lguserid").val();
    var pathUrl = $("#pathUrl").val();
    openNewTabMon("2800-1300", pathUrl + "/mon_mainMon.htm?method=find&lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid);
}

//最小化
function tomini() {
    $('#jkBox #warning_info').hide();
}

function toErrMon(id) {
    var pathUrl = $("#pathUrl").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var lguserid = $("#lguserid").val();
    $("#close_mon_State").val("1");
    openNewTabMon("2900-1000", pathUrl + "/mon_realTimeAlarm.htm?method=find&herfType=1&lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid + "&id=" + id);
}

function openMon() {
    $("#mon_showVoice").val("2");
    $('#mon').dialog({
        autoOpen: false,
        resizable: false,
        height: 768,
        width: 1024,
        open: function () {

        },
        close: function () {
            //warning_dialog();
            $("#mon_showVoice").val("1");
            //window.frames['monFrame'].closeVoice();
        }
    });

    //$('#mon').dialog("open");


    //submit();
}

//已经打开过，再次打开
function openedMon() {
    $('#mon').dialog("open");
}

function closeMon() {
    $('#mon').dialog("close");
    $("#mon_showVoice").val("1");
    if ($("#mon_showVoice").is(":visible")) {
    }
}

//监控主界面专用跳转模块方法
function openNewTabMon(menuCode, url) {
    //var $lf = $("iframe[name='I1']");
    var $lf = $(window.parent.frames['mainFrame'].document).find("iframe[name='I1']");
    if ($lf.contents().find("a[id='ak" + menuCode + "']").length == 0) {
        alert(getJsLocaleMessage("common", "common_frame2_middle_2"));
        return;
    }
    closeMon();
    $lf[0].contentWindow.showTabMenClk(menuCode);

    $lf.contents().find("a[id='ak" + menuCode + "']").trigger("click");
    if (url != "") {
        $(window.parent.frames['mainFrame'].document).find("#cont").attr("src", url);
    }
}

function toggleFullScreen() {

    if (!document.fullscreenElement &&    // alternative standard method
        !document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement) {  // current working methods
        if (document.documentElement.requestFullscreen) {
            document.documentElement.requestFullscreen();
        } else if (document.documentElement.msRequestFullscreen) {
            document.documentElement.msRequestFullscreen();
        } else if (document.documentElement.mozRequestFullScreen) {
            document.documentElement.mozRequestFullScreen();
        } else if (document.documentElement.webkitRequestFullscreen) {
            document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
        }
    } else {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        }
    }
}