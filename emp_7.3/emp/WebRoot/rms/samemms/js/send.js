/**
 * 全局变量，判断是否点击快捷进入;为1时是，为0时是点击富信发送;
 */
var isShortCut = 0;
$(document).ready(function () {

    //解决IE下内容时效性默认值不显示的问题，placeholder失效造成的 ---> lianghuageng
    if (!isPlaceholer()) {
        if ($('#validHourNum').val() == "") {
            $('#validHourNum').css({
                "color": "#ccc"
            })
            $('#validHourNum').val("48");
        }
        $('#validHourNum').focus(function () {
            $('#validHourNum').css({
                "color": "#000000"
            })
            if ($(this).val() == "48") {
                $(this).val("");
            }
        })

        $('#validHourNum').blur(function () {
            if ($(this).val() == "") {
                $(this).val("48");
                $('#validHourNum').css({
                    "color": "#ccc"
                })
            } else {
                $('#validHourNum').css({
                    "color": "#000000"
                })
            }
        })
    }

    havOne = '2';
    var tempId = $("#tmIdSend").val();
    if (tempId && tempId != 'null') {
        getTmpId(tempId);
    }
    $('#busCode,#mmsUser').isSearchSelect({'width': '600','select_height' : '33', 'isInput': false, 'zindex': 0});
    var commonPath = $("#commonPath").val();
    $(window.parent.document).find("#load-bg").hide();
    getLoginInfo("#hiddenValueDiv");
    //高级设置
    $("#advancedSetting").toggle(function () {
        $("#advancedSettingContent").show();
        $("#foldIcon").addClass("send_unfold");
        $("#foldIcon").removeClass("send_fold");
        $(".table-hd").addClass("active");
    }, function () {
        $("#advancedSettingContent").hide();
        $("#foldIcon").addClass("send_fold");
        $("#foldIcon").removeClass("send_unfold");
        $(".table-hd").removeClass("active");
    });
    //错误号码
    $(".invalidPhone").toggle(function () {
        $(".errorDiv").show();
        $("#errorDiv_diff").show();
        $(".arrowUp").show();
        $(".foldIcon").attr("src", commonPath + "/rms/samemms/img/up_icon.png");
        $("#arrow").attr("src", commonPath + "/rms/samemms/img/arrow_up.png");
    }, function () {
        $(".errorDiv").hide();
        $("#errorDiv_diff").hide();
        $(".arrowUp").hide();
        $(".foldIcon").attr("src", commonPath + "/rms/samemms/img/down_icon.png");
        $("#arrow").attr("src", commonPath + "/rms/samemms/img/arrow_down.png");
    });
    //富信不同内容发送预览窗口
    $("#detail_Info_diff").dialog({
        autoOpen: false,
        modal: true,
        height: 580,
        width: 760,
        closeOnEscape: false,
        resizable: false,
        open: function () {
            //默认不打开错误号码详情
            $(".errorDiv").css("display", "none");
            //这里是处理打开预览界面的时候， 处理界面的显示信息
            $(".ui-dialog-titlebar-close").hide();
            //预发送条数就是有效号码数
            var effs = $.trim($("#effs_diff").text());
            if (eval(effs) === 0) {
                // 没有可发送的号码!不允许发送
                $("#nosendReason_diff").html(getJsLocaleMessage("rms","rms_mbgl_nophonenumber"));
                //没有预发号码的时候 ，隐藏信息，并且把按钮置灰
                $("#btsend").attr("disabled", true);
            }
        },
        close: function () {
            //设置预览提交时的错误标志
            $("#error").val("");

            $(".ui-dialog-titlebar-close").show();
            $("#subSend1").attr("disabled", "");
        }
    });
    //如果是通过快捷方式点进来的需要改变样式
    var _tmId = $("#tmId").val();
    var _tempId = $("#tempId").val();
    var _tempName = $("#tempName").val();
    var _tempUrl = $("#tempUrl").val();
    var _isShortCut = $("#isShortCut").val();
    var _tempType = $("#tempType").val();
    var _tempVer = $("#tempVer").val();
    if ("true" === _isShortCut) {
        isShortCut = 1;
        if (_tempId === "" || _tempId == null || _tempId === "undefined") {
            // 模板有误，无法正确加载页面。
            alert(getJsLocaleMessage("rms","rms_mbgl_badtemplate"));
            return
        }
        $("#choose-list").css("display", "");
        //清除掉第一次进入的提示语
        $("#firstInInfo").css("display", "none");
        $("#firstInInfo").text("");
        //加载格式提示悬浮框
        showReminder("downlinks", "reminder",1);
        if (_tempType === "1") {
            //--------不同内容-------
            //发送方式
            $("#choosePerson").css("display", "none");
            $("#bulkInput").css("display", "none");
            $("#phoneInput").css("display", "none");
            //格式提示
            $("#sameInfo").css("display", "none");
            if (_tempVer === "V1.0") {
                $("#withArrow").css("display", "");
                //下载格式号码文件
                $("#downExcel").css("display", "none");
            } else if (_tempVer === "V2.0") {
                $("#withArrow").css("display", "none");
                $("#diffInfo").css("display", "");
                //下载格式号码文件
                $("#downExcel").css("display", "");
            }
            $("#diffInfo").css("display", "block");
            //高级设置
            $("#checkRepeat").css("display", "");
        } else if (_tempType === "0") {
            //--------相同内容--------
            //发送方式
            $("#choosePerson").css("display", "");
            $("#bulkInput").css("display", "");
            $("#phoneInput").css("display", "");
            //格式提示
            $("#withArrow").css("display", "");
            $("#diffInfo").css("display", "none");
            $("#sameInfo").css("display", "block");
            //高级设置
            $("#checkRepeat").css("display", "none");
            //下载格式号码文件
            $("#downExcel").css("display", "none");
        }
        $("#tempview").css("display", "");
        tempPreview(_tempUrl, _tempName, _tmId);
    }
    //预览页面的滑动条样式
    $("#cust_preview_outer").mouseenter(function () {
        $(this).css("overflow-y", "auto");
    });
    $("#cust_preview_outer").mouseleave(function () {
        $(this).css("overflow-y", "hidden");
    });
});

function isPlaceholer() {
    var input = document.createElement('input');
    return "placeholder" in input;
}


//显示有效时间提示语
function showInfoMsg() {
    $("span#InfoMsg").show();
}

function hideInfoMsg() {
    $("span#InfoMsg").hide();
}

//选择模板
function chooseMeditorTemplate() {
    var chooseTem = layer.open({
        type: 2,
        area: ['1060px', '700px'],//默认自适应
        offset: 'auto',
        title: '',
        maxmin: false,
        resize: true,
        shadeClose: true, //点击遮罩关闭层
        content: 'toTempChoose.meditorPage'
    });
    //layer.full(chooseTem);
}

//立即发送
function rmsSend(tmid,usecount){
    var path = $("#pathUrl").val();
    usecount = parseInt(usecount) + 1; //点击立即使用就增加1次
    $.post(path+"/rms_commTpl.htm?method=updateUseCount",{tmid:tmid,usecount:usecount},function(result){
        if(result != null && result == "success")
        {
            window.parent.openNewTab("5100-1000",path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid);
            //window.location.href = path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid;
        }
        else
        {
            alert(getJsLocaleMessage("ydcx","rms_myscene_runexception"));
        }
    });

}
//立即使用
function getTmpId(id) {
    //获取发送菜单
    /*var sendMenuId=  $("#topLink #m5100-1000").attr("id");
    if (!sendMenuId){
        $("#topLink #m5100-1000").remove()
    }*/

    if (!id) {
        // 模板有误，无法正确加载页面。
        alert(getJsLocaleMessage("rms","rms_mbgl_badtemplate"));
        return
    }
    var getUserUrl = "meditor/user/getUserInfo";
    $.ajax({
        type: 'POST',
        url: getUserUrl,
        data: JSON.stringify({"tmId": id}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            var lgcorpcode = data.data.loginCorp.corpCode;
            var lguserid = data.data.loginCorp.userId;
            //企业编码
            $("#lgcorpcode").val(lgcorpcode);
            $("#lguserid").val(lguserid);
        }
    });

    var url = "meditor/getTempDetail";
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify({"tmId": id}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            var spid = data.data.sptemplid;
            var tempName = data.data.tmName;
            var templateType = data.data.tmpType;
            var dsflag = data.data.dsflag;

            if (spid === "" || spid == null) {
                // 模板ID为空，无法正确加载页面。
                alert(getJsLocaleMessage("rms","rms_mbgl_notemplateid"));
                return;
            }
            if (tempName === "" || spid == null) {
                // 模板名称为空，请重新选择。
                alert(getJsLocaleMessage("rms","rms_mbgl_notemplatename"));
                return
            }

            $("#tempId").val(spid);

            $("#tmId").val(id);
            //模板名称tempInputName
            $("#tempName").val(tempName);
            //静动态类型
            $("#tempType").val(dsflag);
            //模板类型 11富媒体、12卡片和13富文本 14短信
            $("#templateType").val(templateType);
            //任务主题
            $("#mmstaskname").val(tempName);
            //发送主题
            $("#tempInputName").val(tempName);
            //富信发送账号
            var _tempType = $("#tempType").val();
            //模板类型 tempType 0相同 1不同
            $("#tempVer").val('V2.0');

            //重新选择富信后清除号码文件
            $("#infomaTable").find("li").remove();
            $('#allDiffFileName').html("");
            $('#allSameFileName').html("");
            $('#diffContent').html("");
            $("#sameSendInfo input").val("");
            $("#importArea").val('');
            //给上传文件按钮重新赋值
            $("#fileInput").empty();
            $("#fileInput").append("<input type='file' name='uploadFile1' id ='uploadFile1' value='quose' maxlength='11' onchange='addFilesNoModel();'>导入文件");
            noFileModelCount = 1;

            $("#choose-list").css("display", "");
            //清除掉第一次进入的提示语
            $("#firstInInfo").css("display", "none");
            $("#firstInInfo").text("");
            //加载格式提示悬浮框
            if (isShortCut != 1) {
                showReminder("downlinks", "reminder");
            }

            if (_tempType === "1") {
                //--------不同内容-------
                //发送方式
                $("#choosePerson").css("display", "none");
                $("#bulkInput").css("display", "none");
                $("#phoneInput").css("display", "none");
                //格式提示
                $("#sameInfo").css("display", "none");
                $("#withArrow").css("display", "none");
                $("#diffInfo").css("display", "block");
                //下载格式号码文件
                $("#downExcel").css("display", "");
                //高级设置
                $("#checkRepeat").css("display", "");
            } else if (_tempType === "0") {
                //--------相同内容--------
                //发送方式
                $("#choosePerson").css("display", "");
                $("#bulkInput").css("display", "");
                $("#phoneInput").css("display", "");
                //格式提示
                $("#withArrow").css("display", "none");
                $("#diffInfo").css("display", "none");
                $("#sameInfo").css("display", "block");
                //高级设置
                $("#checkRepeat").css("display", "none");
                //下载格式号码文件
                $("#downExcel").css("display", "none");
            }
            $("#tempview").css("display", "");

            $("#tempInputName").text();

            //0只给一个主数据  1给所有数据
            var frameSrc = 'toPreviewIndex.meditorPage';
            var previewType = 0;
            //需要预览的模板id
            //1显示标题，0不显示
            var title = 0;
            //1显示提示信息，0不显示
            var hint = 0;
            //预览方法
            showTempView(previewType, id, title, hint, frameSrc);
        }
    });
}

function showTempView(previewType, id, title, hint, frameSrc) {
    var lang = $("#langName").val();
    var param = 'previewType=' + previewType + '&id=' + id + '&title=' + title + '&hint=' + hint + '&lang=' + lang;
    $("#tempview").show();
    $("#tempview").height(755);
    $("#tempview").width(400);
    $("#tempview-iframe").addClass("J-vue-cont");
    $("#tempview-iframe").attr('data-param', param);
    $("#tempview-iframe").attr("src", frameSrc);
}

function showTempViewPre(previewType, id, title, hint, frameSrc) {
    var lang = $("#langName").val();
    var param = 'previewType=' + previewType + '&id=' + id + '&title=' + title + '&hint=' + hint + '&lang=' + lang;
    $("#tempview1").show();
    //$("#tempview1").height(500);
    //$("#tempview1").width(400);
    $("#tempview1-iframe").addClass("J-vue-cont");
    $("#tempview1-iframe").attr('data-param', param);
    $("#tempview1-iframe").attr("src", frameSrc);
}

function showDiffTempViewPre(previewType, id, title, hint, jsonArr, frameSrc) {
    var jsonStr = encodeURIComponent(jsonArr);
    var lang = $("#langName").val();
    var param = 'previewType=' + previewType + '&id=' + id + '&title=' + title + '&hint=' + hint + '&params=' + jsonStr + '&lang=' + lang;
    $("#myView").show();
    $("#tempview2-iframe").addClass("J-vue-cont");
    //解决iframe调用同一个js方法冲突问题,向第一个iframe插入ID
    // var oneParam = 'previewType=' + previewType + '&id=' + id + '&title=' + title + '&hint=' + hint+ '&fid=tempview2-iframe';
    // $("#tempview-iframe").attr('data-param', oneParam);
    $("#tempview2-iframe").attr('data-param', param);
    $("#tempview2-iframe").attr("src", frameSrc);
    $('#myView').dialog({
        autoOpen: false,
        width: 440,
        height: 700,
        position: {
            using: function (pos) {
                var topOffset = $(this).css(pos).offset().top;
                if (topOffset = 0 || topOffset > 0) {
                    $(this).css('top', 40);
                }
            }
        },
    });
    $("#myView").dialog("open");
    changeDialogTitleBar();
    //显示右上角的X按钮
    $("#ui-dialog-title-myView").parent().find(".ui-dialog-titlebar-close").show();
}

// 选择富信模板/
function chooseRmsTemplate() {

    var pathUrl = $("#pathUrl").val();
    var lguserid = $("#lguserid").val();

    /*跳转到选择模板页面*/
    var frameSrc = pathUrl + "/rms_rmsSameMms.htm?method=getLfTemplateByMms&choosePublic=1&pageSource=0&lguserid=" + lguserid;

    $("#tempFrame").attr("src", frameSrc);
    //初始选择模板DIV
    $("#tmplDiv").dialog({
        autoOpen: false,
        height: 740,
        width: 1036,
        resizable: false,
        //开启遮罩层
        modal: true,
        //改变起始位置
        position: {
            using: function (pos) {
                var topOffset = $(this).css(pos).offset().top;
                if (topOffset === 0 || topOffset > 0) {
                    $(this).css('top', 40);
                }
            }
        }
    });

    $(".ui-dialog-titlebar-close").hide();
    $(".ui-dialog-titlebar").hide();

    $("#tmplDiv").dialog("open");

}

// 关闭富信模板页面
function closeDialog() {
    $("#tmplDiv").dialog("close");
    $(".ui-dialog-titlebar-close").show();
    $(".ui-dialog-titlebar").show();
}
//选项存为默认
function setDefault() {
    //确认是否将当前选项设置为默认？
    if (confirm(getJsLocaleMessage("rms","rms_mbgl_confirmsetdefault"))) {
        var lguserid = $('#lguserid').val();
        var lgcorpcode = $('#lgcorpcode').val();
        var mmsUser = $("#mmsUser").val();
        var busCode = $("#busCode").val();
        var checkRepeat = $('input[name="checkRepeat"]:checked').val();
        $.post("rms_rmsSameMms.htm?method=setDefault", {
            lguserid: lguserid,
            lgcorpcode: lgcorpcode,
            spUser: mmsUser,
            busCode: busCode,
            checkrepeat: checkRepeat,
            flag: 13
        }, function (result) {
            if (result === "success") {
                //当前选项设置为默认成功！
                alert(getJsLocaleMessage("rms","rms_mbgl_setcurrdefaultok"));
            }
            else if (result === "fail") {
                //当前选项设置为默认失败！
                alert(getJsLocaleMessage("rms","rms_report_operatfail"));
            } else {
                alert(getJsLocaleMessage("rms","rms_report_operatfail"));
            }
        });
    }
    //解决IE10兼容问题-----------------lianghuageng
    var version = userAgent();
    if (version == 'ie10' || version == 'ie9' || version == 'ie8' || version == 'ie6') {
        page_complete();
    }
}

//-------------------------批量输入
function bulkImport() {
    $('#bulkImport_box').dialog({
        autoOpen: false,
        height: 500,
        width: 690,
        resizable: false,
        modal: true
    });
    $('#bulkImport_box').dialog('open');
    changeDialogTitleBar();
    $('#importArea').blur();
    $('#importArea').html($('#importAreaTemp').html());
    var num = $('#importAreaTemp').attr('data-num');
    num = typeof num == 'undefined' ? 0 : num;
    $('#bNum').html(num);
}

function bulkInputConfirm() {
    if ($('#importArea').val() === '') {
        //请输入号码
        alert(getJsLocaleMessage("rms","rms_mbgl_inputnumber"));
        return false;
    }

    var phone = $('#bNum').html();
    //批量输入最大支持20000个号码
    if (20000 - phone < 0) {
        //批量输入号码个数超出范围，最大只支持20000个号码
        alert(getJsLocaleMessage("rms","rms_mbgl_batchexceedrange"));
        return false;
    }
    $('.bultPhone').remove();

    $("#infomaTable").append(
        "<li class=\"list-li fxsend-clear bultPhone\">" +
        "<p><span>"+getJsLocaleMessage('rms','rms_mbgl_batchinput')+"</span></p>" +
        "<p><span class='addressListSpan' onclick= 'bulkImport()'>"+getJsLocaleMessage('rms','rms_mbgl_detail')+"</span></p>" +
        "<p><a href='javaScript:delInputPhone()' class=\"red-text\">"+getJsLocaleMessage('rms','rms_myscene_del')+"</a></p></li>");
    var inputphone = $('#importArea').val();
    $('#importAreaTemp').html(inputphone).attr('data-num', phone);
    var reg = /[\s,、\n，]+/g;
    var result = inputphone.replace(reg, ",");
    if (result.substr(0, 1) === ",") {
        result = result.substr(1);
    }
    $("#inputphone").val(result);
    bulkInputCancel();
}

function bulkInputCancel() {
    $('#bulkImport_box').dialog('close');
    $('#importArea').html("");
}

function delInputPhone() {
    // 确认要删除吗?
    if (confirm(getJsLocaleMessage("rms","rms_mbgl_confirmdel2"))) {
        $(".bultPhone").remove();
        $("#inputphone").val("");
        $("#importArea").val("");
        $('#importAreaTemp').val("").attr('data-num', 0);
    }
}

//输入计数
function formatTelNum(element) {
    var len;
    var str = $('#importArea').val();
    //过滤掉中文以及英文字母
    var reg = /[a-zA-Z]+/g;
    var result = str.replace(reg, "");
    reg = /[\u4e00-\u9fa5]+/g;
    result = result.replace(reg, "");
    $('#importArea').val(result);

    result = $.trim(result);
    reg = /[\s,、\n，]+/g;
    result = result.replace(reg, " ");
    var arr = result.split(" ");
    if (arr[arr.length - 1] === "") {
        len = arr.length - 1;
    } else {
        len = arr.length;
    }
    $(element).html(len);
}

//--------------------------选择人员
function showSelectPerson() {
    var lguserid = $("#lguserid").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var commonPath = $("#commonPath").val();
    //引用公共的选择人员页面
    //选择类型查询  1.员工通讯录   2.客户通讯录  3.群组(员工+客户) 4.员工群组 5.客户群组 6.客户属性 7.签约用户
    var chooseType = "1,2,3";
    $("#flowFrame").attr("src", commonPath + "/common/selectUserInfo.jsp?lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid + "&chooseType=" + chooseType);
    //选择发送对象
    $("#SelectPerson").dialog({
        autoOpen: false,
        height: 600,
        width: 690,
        resizable: false,
        modal: true
    });
    $("#SelectPerson").dialog("open");
    changeDialogTitleBar();
}

//点击选择对象中的  确定按钮
function doOk() {
    // 这里处理是否 右边选择的记录$("#right option:selected").size()
    var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
    //代表的是员工机构IDS
    $("#empDepIds").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
    //代表的是客户机构IDS
    $("#cliDepIds").val($(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val());
    //代表的是群组IDS
    $("#groupIds").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
    //代表的是员工IDS
    $("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
    //代表的是客户IDS
    $("#cliIds").val($(window.frames['flowFrame'].document).find("#clientIds").val());
    //代表的是外部人员IDS
    $("#malIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
    //选择用户对象号码串
    $("#userMoblieStr").val($(window.frames['flowFrame'].document).find("#moblieStrs").val());

    $("#rightSelectedUserOption").val($(window.frames['flowFrame'].document).find("#right").html());

    var manCount = $(window.frames['flowFrame'].document).find("#manCount").html() + "";

    //处理选择对象的操作
    //havOne = $("#havOne").val();
    if (havOne === "2"&& optionSize > 0) {
        $("#infomaTable").append(
            "<li id='addressList' class=\"list-li fxsend-clear selectPerson\">" +
            "<p><span>"+getJsLocaleMessage('rms','rms_mbgl_contacts')+"</span></p>" +
            "<p><span class='addressListSpan' onclick='showSelectPerson()'>"+getJsLocaleMessage('rms','rms_mbgl_detail')+"(<span id='choiceNum'>" + manCount + "</span>)</span></p>" +
            // 删除
            "<p><a href='javaScript:doNo("+manCount+")' class=\"red-text\">"+getJsLocaleMessage('rms','rms_myscene_del')+"</a></p></li>");
        //$("#havOne").val("1");
        havOne = '1';
    }
    else {
        //如果未选择对象,则清空一栏
        if (optionSize === 0) {
            clearUser();
        } else {
            $("#choiceNum").empty();
            $("#choiceNum").append(manCount);
        }
    }
    $("#SelectPerson").dialog("close");
}

//点击选择对象中的  清空按钮
function doNo() {
    var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
    if (optionSize === 0) {
        clearUser();
        return;
    }
    //该操作将清空所选择对象,是否执行？
    if (getJsLocaleMessage("rms","rms_report_clearchooseobj")) {
        //代表的是员工机构IDS
        $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
        //代表的是客户机构IDS
        $(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val("");
        //代表的是群组IDS
        $(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
        //代表的是分页索引第一页
        $(window.frames['flowFrame'].document).find("#pageIndex").val("1");
        //代表的是员工IDS
        $(window.frames['flowFrame'].document).find("#employeeIds").val("");
        //代表的是客户IDS
        $(window.frames['flowFrame'].document).find("#clientIds").val("");
        //代表的是外部人员IDS
        $(window.frames['flowFrame'].document).find("#malistIds").val("");
        $(window.frames['flowFrame'].document).find("#right").empty();
        $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
        //选择用户对象号码串
        $(window.frames['flowFrame'].document).find("#moblieStrs").val("");
        $(window.frames['flowFrame'].document).find("#manCount").html(0);

        clearUser();

        //$("#SelectPerson").dialog("close");
    }
}

// 清空主界面的选择对象（div#sameSendInfo）的数据
function clearUser() {
    //点取消选择对象的时候，把TABLE里的选择对象一TR去掉
    //$(".selectPerson").remove();
    $("#addressList").remove();

    //$("#havOne").val("2");
    havOne = '2';
    // 把主页面的数据也清空
    $("#rightSelectedUserOption").val("");
    $("#empDepIds").val("");
    $("#cliDepIds").val("");
    $("#groupIds").val("");
    $("#empIds").val("");
    $("#cliIds").val("");
    $("#malIds").val("");
    $("#userMoblieStr").val("");
}

//----------------------------导入文件
var noFileModelCount = 1;
//处理是导入文件
var fileCount = 0;

function addFilesNoModel() {
    var tempType = $("#tempType").val();
    var className = "";
    if ("1" === tempType) {
        className = "diffSendFile";
    } else if ("0" === tempType) {
        className = "sameSendFile";
    }
    var $obj = $("#uploadFile" + noFileModelCount);
    var pathValue = $obj.val();
    var index = pathValue.lastIndexOf("\\");
    var name = pathValue.substring(index + 1);
    if (name.length > 20) {
        name = name.substring(0, 20) + "....";
    }
    if (checkFile("uploadFile" + noFileModelCount, tempType)) {
        if ($("#tr" + noFileModelCount).length === 0) {
            $("#infomaTable").append(
                "<li id='tr" + noFileModelCount + "' class='list-li fxsend-clear " + className + "'>" +
                "<p><span>"+getJsLocaleMessage('rms','rms_report_phonenumberfile')+"</span></p>" +
                "<p><a style='text-decoration: none;'>" + name + "</></p>" +
                "<p><a href='javaScript:delPhone(" + "\"tr" + noFileModelCount + "\",\"" + tempType + "\")' class=\"red-text\">删除</a></p></li>");
            $("#uploadFile" + noFileModelCount).css("display", "none");
            noFileModelCount++;
            var ss = "<input type='file' id='uploadFile" + noFileModelCount + "' name='uploadFile" + noFileModelCount + "' value='' onchange='addFilesNoModel();' class='numfileclass'>";
            $("#fileInput").prepend(ss);
            if ("1" === tempType) {
                $("#allDiffFileName").append(pathValue + ";");
            } else if ("0" === tempType) {
                $("#allSameFileName").append(pathValue + ";");
            }
        }
    }
}

//对输入框中的手工添加的手机号码/文件删除做删除操作
function delPhone(phoneNum, tempType) {
    var allFileName;
    if ("1" === tempType) {
        allFileName = "allDiffFileName";
    } else if ("0" === tempType) {
        allFileName = "allSameFileName";
    }
    if (phoneNum.indexOf("tr") === 0) {
        //确定要删除吗？
        if (confirm(getJsLocaleMessage("rms","rms_mbgl_confirmdel2"))) {
            $("#" + phoneNum + "").remove();
            var numFile = phoneNum.replace("tr", "uploadFile");
            var trs = $("#" + allFileName).html();
            trs = trs.replace($("#" + numFile).val() + ";", "");
            $("#" + numFile).remove();
            $("#" + allFileName).html(trs);
        }
    } else {
        //确定要删除吗？
        if (confirm(getJsLocaleMessage("rms","rms_mbgl_confirmdel2"))) {
            $("#" + phoneNum + "").remove();
            $("#phoneStr").val($("#phoneStr").val().replace(phoneNum + ",", ""));
        }
    }

    //解决IE10兼容问题-----------------lianghuageng
    var version = userAgent();
    if (version == 'ie10' || version == 'ie9' || version == 'ie8' || version == 'ie6') {
        page_complete();
    }
}

// 验证上传文件格式
function checkFile(name, tempType) {
    var tempVer = $("#tempVer").val();
    var allFileName;
    if ("1" === tempType) {
        allFileName = "allDiffFileName";
    } else if ("0" === tempType) {
        allFileName = "allSameFileName";
    }
    var $fileObj = $("form[name='form2']").find("input[name='" + name + "']");
    if ($fileObj.val() !== "") {
        var fileName = $fileObj.val();
        var index = fileName.split(".");
        var fileType = index[index.length - 1].toLowerCase();
        var trs = $("#" + allFileName).html();
        if (trs.indexOf(fileName + ";") > -1) {
            //该文件已经存在！
            alert(getJsLocaleMessage("rms","rms_mbgl_fileexists"));
            $fileObj.after($fileObj.clone().val(""));
            $fileObj.remove();
            return false;
        }
        var fileTypeFlag = fileType !== "txt" && fileType !== "xls" && fileType !== "xlsx" &&
            fileType !== "et" && fileType !== "zip" && fileType !== "csv" && fileType !== "rar";
        var condition1 = fileTypeFlag && tempType === "0";
        var condition2 = fileType !== "xls" && fileType !== "xlsx" && tempType === "1" && tempVer === "V2.0";
        var condition3 = fileTypeFlag && tempType === "1" && tempVer === "V1.0";
        if (condition1 || condition2 || condition3) {
            if (condition2) {
                //上传文件格式错误！请上传指定模板Excel文件。
                alert(getJsLocaleMessage("rms","rms_mbgl_badfilefmt"));
            } else {
                //
                // 上传文件格式错误！请选择txt、zip、rar、xls、xlsx、csv或et格式的文件。
                alert(getJsLocaleMessage("rms","rms_mbgl_badfilefmt2"));
            }
            $fileObj.after($fileObj.clone().val(""));
            $fileObj.remove();
            return false;
        } else {
            return true;
        }
    } else {
        //请选择上传的文件！
        alert(getJsLocaleMessage("rms","rms_mbgl_chooseuploadfile"));
        return false;
    }
}

//----------------------------输入手机号
function addphone() {
    var phone = $.trim($("#tph").val());

    if (phone.length === 0 || "" === phone) {
        //请输入手机号码！
        alert(getJsLocaleMessage("rms","rms_mbgl_inputtelphonenumber"));
        return;
    }

    if (phone.length > 21 || phone.length < 7 || !/\\+?\d{6,20}/.test(phone)) {
        $.post("common.htm",
            {
                method: "filterPh",
                tmp: phone
            },
            function (returnMsg) {
                if (returnMsg === "false") {
                    // 手机号码输入不合法，请重新输入！
                    alert(getJsLocaleMessage("rms","rms_report_invalidno"));
                    $("#tph").focus();
                } else {
                    var phoneStr = $("#phoneStr").val();
                    var flag = "1";
                    if (phoneStr.length > 0 && phoneStr !== "") {
                        var phoneArr = phoneStr.split(",");
                        for (var i = 0; i < phoneArr.length; i++) {
                            if (phone === phoneArr[i]) {
                                flag = "2";
                                break;
                            }
                        }
                    }
                    if (flag === "1") {
                        if (/^\+/.test(phone)) {
                            phone = phone.substring(1);
                        }
                        $("#infomaTable").append(
                            "<li id='" + phone + "' class=\"list-li fxsend-clear inputPhone\">" +
                            "<p><span>"+getJsLocaleMessage('rms','rms_report_addmanually')+"</span></p>" +
                            "<p><span id='showPhone'>" + phone + "</span></p>" +
                            "<p><a href='javaScript:delPhone(\"" + phone +"\")' class=\"red-text\">"+getJsLocaleMessage('rms','rms_myscene_del')+"</a></p></li>");
                        $("#phoneStr").val(phoneStr+phone+",");
                        $("#tph").val("");
                    } else {
                        //手机号码已存在
                        alert(getJsLocaleMessage("rms","rms_report_phonenoexist"));
                        $("#tph").val("");
                        return;
                    }
                }
            });
    } else {
        //手机号码输入不合法，请重新输入！
        alert(getJsLocaleMessage("rms","rms_report_invalidno"));
        $("#tph").val("");
    }
}

function controlPhoneInput(obj) {
    //第一个字符允许为+号,其余的只能为数字
    var $obj = $(obj);
    if (!/^\+.*/.test($obj.val())) {
        $obj.val($obj.val().replace(/\D/g, ''));
    } else {
        $obj.val("+" + $obj.val().substring(1).replace(/\D/g, ''));
    }
}

//重置按钮
//重置
function reloadPage() {
    var pathUrl = $("#pathUrl").val();
    window.location.href = pathUrl + "/rms_rmsSameMms.htm";
}

//------------------------------提交预览
var layer1;
function preview() {
    //如果是首次进入提示
    var infoTxt = $("#firstInInfo").text();
    if (infoTxt !== "" && infoTxt != null && infoTxt !== "undefined") {
        alert(infoTxt);
        return
    }
    $("#allinputphone").val($("#phoneStr").val() + $("#inputphone").val() + ",");
    //发送主题
    var title = $("#tempInputName").val();
    //富信发送账号
    var spUser = $("#mmsUser").val();
    //业务类型编码
    var busCode = $("#busCode").val();
    //发送类型 即时0 /定时1
    var timerStatus = $("input[name='sendType']:checked").val();
    //发送时间
    var timerTime = $("input#sendtime").val();
    //内容时效
    var validHourNum = $("input#validHourNum").val();
    //企业编码
    var corpCode = $("#lgcorpcode").val();
    //重号过滤
    var checkRepeat = $("input[name='checkRepeat']").val();
    //选择的富信模板ID
    var tempId = $("#tempId").val();
    //模板类型 tempType 0相同 1不同
    var tempType = $("#tempType").val();
    //模板类型
    var templateType = $("#templateType").val();
    //发送对象
    var trcount = $("#infomaTable").find("li[id!='firstInInfo']");
    //模板版本
    var tempVer = $("#tempVer").val();

    if (trcount.size() === 0) {
        if (tempType === "1") {
            // alert(tempVer === "V1.0" ? "请上传指定格式号码文件。" : "请上传指定模板Excel文件。");
            alert(tempVer === "V1.0" ? getJsLocaleMessage("rms","rms_report_plzuploadfile") : getJsLocaleMessage("rms","rms_report_plzuploadxls"));
        } else if (tempType === "0") {
            // 请选择发送对象。
            alert(getJsLocaleMessage("rms","rms_report_choosesendobj"));
        }

        return;
    } else if (tempId === "" || tempId == null) {
        // 请选择富信发送模板!
        alert(getJsLocaleMessage("rms","rms_report_choosefuxintpl"));

        return;
    } else if (spUser === "" || spUser == null) {
        // 请选择SP账号
        alert(getJsLocaleMessage("rms","rms_senddtl_alert2"));

        return;
    } else if (busCode === "" || busCode == null) {
        // 请选择业务类型
        alert(getJsLocaleMessage("rms","rms_report_choosebiztype"));

        return;
    } else if (timerTime === "" && timerStatus === "1") {
        // 请输入定时时间
        alert(getJsLocaleMessage("rms","rms_report_inputschedtime"));

        return;
    } else if (tempType === "1" && (checkRepeat !== "0" && checkRepeat !== "1") || checkRepeat == null || checkRepeat === "") {
        // 高级设置中重号过滤设置错误
        alert(getJsLocaleMessage("rms","rms_report_badsettinggrep"));

        return;
    }
    if (validHourNum === "" || validHourNum == null || validHourNum === undefined) {
        //内容时效不输入默认为48h
        validHourNum = $("#validHourNum").attr("defaultVal");
    }
    //校验 + 模板使用次数加1
    $.post("rms_rmsSameMms.htm?method=checkBeforeSend",
        {
            title: title,
            corpCode: corpCode,
            timerStatus: timerStatus,
            tempId: tempId,
            timerTime: timerTime,
            validHourNum: validHourNum
        }, function (result) {
            if (result.indexOf("html") > 0) {
                // 响应超时,请重新登录！
                alert(getJsLocaleMessage("rms","rms_report_responsetimeout"));
                window.location.href = window.location.href;
                return;
            } else if (result === "") {
                alert(getJsLocaleMessage("rms","rms_report_operatfail"));
                return;
            }
            if (result === "success") {
                $("#validtm").val(validHourNum);
            } else {
                var preStr = result.substring(0, result.indexOf("&"));
                var backStr = result.substring(result.indexOf("&") + 1);
                if (preStr === "stage1") {
                    // alert("发送主题包含关键字：" + backStr);
                    alert(getJsLocaleMessage("rms","rms_report_topiccontainkeyword") + backStr);

                    return;
                } else if (preStr === "stage2") {
                    alert(backStr);
                    $("#sendtime").val("");

                    return;
                } else if (preStr === "stage3") {
                    alert(backStr);
                    $("#sendtime").val("");

                    return;
                } else if (preStr === "stage4") {
                    alert(backStr);
                    $("#sendtime").val("");

                    return;
                }
            }

            //加载小菊花
            layer1 = layer.msg(getJsLocaleMessage("common","common_frame2_checkFee_20"), {
                //图标
                icon: 16,
                //遮罩阴影透明度 1为全黑
                shade: 0.5,
                //是否自动关闭
                time:false
            });
            //防止内存溢出，初始化为0。预览成功为1，为0则 提示系统繁忙
            $("#error").val("0");
            var action = "";
            var $form2 = $("form[name='form2']");
            var taskId = $("#taskId").val();
            if (tempType === "0") {
                $("#allFileNames").val($("#allDiffFileName").html());
                action = "rms_rmsSameMms.htm?method=preview&taskId=" + taskId;
            } else if (tempType === "1") {
                $("#allFileNames").val($("#allSameFileName").html());
                action = "rms_diffMms.htm?method=preview&taskId=" + taskId + "&tempVer=" + tempVer;
            }
            $form2.attr("action", action);
            $form2.submit();
        });
}

//发送界面预览窗口
function preSend(data) {
    $("#error").val("1");
    //关闭小菊花
    layer.close(layer1);
    $(".ui-dialog-titlebar").show();
    if(data === "error"){
        alert(getJsLocaleMessage('rms','rms_report_exceptionview'));

        return;
    }else if(data === "overstep"){
        alert(getJsLocaleMessage('rms','rms_report_exceed500w'));

        return;
    }else if(data === "noPhone"){
        alert(getJsLocaleMessage('rms','rms_report_nosendno'));

        return;
    } else if (data.indexOf("empex") === 0) {
        alert(data.substring(5));
        return;
    }else if(data === "emptyTemplate"){
        alert("获取模板内容异常！");

        return;
    } else if (data === "corpcodeError") {
        alert(getJsLocaleMessage('rms','rms_report_getecidexception'));

        return;
    } else if (data.indexOf("keywords") === 0) {
        alert(getJsLocaleMessage('rms','rms_report_tmpcontainbannedword') + data.substring(data.indexOf("&") + 1));

        return;
    }
    var fxBalance;
    var spFeeBalance;
    var errerCount;
    var array = eval("(" + data + ")");
    var sendType = array.sendType;
    if (sendType === "1") {
        //先清空内容
        $("#diffContent").html("");
        $("#counts_diff").text(array.subCount);
        $("#effs_diff").text(array.effCount);
        $("#legers_diff").text(array.badModeCount);
        $("#sames_diff").text(array.repeatCount);
        $("#blacks_diff").text(array.blackCount);
        $("#invalidFile").text(array.invalidFile);
        $("#badUrl_diff").val(array.viewFilePath);
        $("#keyW_diff").text(array.kwCount);
        $("#tempFeeDegree_diff").text(array.tempDegree);
        $("#feeFlag").val(array.feeFlag);
        $("#resultUrl").val(array.resultUrl);
        $("#tempUrl").val(array.viewFilePath);

        errerCount = parseInt(array.subCount) - parseInt(array.effCount);
        if (errerCount !== 0) {
            $("#errerCount_diff").text(errerCount);
        } else {
            $("#errerCount_diff").text(0);
        }

        //获取富信余额
        fxBalance = array.fxBalance;
        //获取SP账号余额
        spFeeBalance = array.spFeeBalance;
        //检查余额
        checkspye(fxBalance, spFeeBalance, 'diff');
        //过滤完的号码文件
        $("#upNumberPhoneUrl").val(array.validFilePath);

        if (array.effCount - 0 === 0) {
            /*$("#content").append("<thead><tr align='center'><th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_63')+"</th>" +
                "<th><center><div style='width:89px'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_64')+"</div></center></th>" +
                "<th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_65')+"</th></tr></thead>" );
            $("#content").append("<tbody><tr><td colspan='3' align='center'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_289')+"</td></tr></tbody>");*/
            $("#diffContent").append("<tr><td align='center' style='font-size: 12px;font-weight: bold;'>" + getJsLocaleMessage("rms","rms_presend_novalidphone") + "</td></tr>");
            $("#detail_Info_diff").css("display", "block");
            $("#detail_Info_diff").dialog("open");
            changeDialogTitleBar();
            deleteleftline1();
        } else {
            var tempName = $("#tempName").val();
            var tempUrl = $("#tempUrl").val();
            $.post(
                "rms_diffMms.htm?method=readRmsContent",
                {
                    url: array.viewFilePath
                },
                function (result) {
                    if (result != null && result !== "" && result !== undefined) {
                        var data = eval("(" + result + ")");
                        for (var i = 0; i < data.length; i++) {
                            var snum = data[i];
                            if (snum[0].length > 38) {
                                snum[0] = snum[0].substr(0, 38) + "...";
                            }
                            $("#diffContent").append("<tr><td>" + snum[2] + "</td><td><span class='paramSpan'>"
                                + snum[0] + "</span></td><td>" + snum[1] + "</td>" +
                                "<td><a style='cursor:pointer;color:blue;word-break:break-all;white-space:normal;' " +
                                "onclick='diffSendTempPreview(\"" + tempUrl + "\",\"" + tempName + "\",\"" + snum[3] + "\")'>"+ getJsLocaleMessage("common","common_preview") +"</a></td></tr>");
                        }
                        $("#detail_Info_diff").dialog("open");
                        changeDialogTitleBar();
                        deleteleftline1();
                        $(".ui-dialog-titlebar-close").hide();
                    }
                }
            );
        }
    } else if (sendType === "0") {
        $("#counts").text(array.subCount);
        $("#effs").text(array.effCount);
        $("#legers").text(array.badModeCount);
        $("#sames").text(array.repeatCount);
        $("#blacks").text(array.blackCount);
        $("#badUrl_same").val(array.viewFilePath);
        $("#tempFeeDegree").text(array.tempDegree);
        $("#feeFlag").val(array.feeFlag);

        errerCount = parseInt(array.subCount) - parseInt(array.effCount);
        if (errerCount !== 0) {
            $("#errerCount").text(errerCount);
        } else {
            $("#errerCount").text("0");
        }

        //获取富信余额
        fxBalance = array.fxBalance;
        //获取SP账号余额
        spFeeBalance = array.spFeeBalance;
        //检查富信商余额
        checkspye(fxBalance, spFeeBalance, 'same');
        //过滤完的号码文件
        $("#upNumberPhoneUrl").val(array.validFilePath);
        //富信模板路径
        var tempUrl = $("#tempUrl").val();
        //富信名字
        var tempName = $("#tempName").val();
        var skinType = $("#skinType").val();
        var tmId = $("#tmId").val();
        // $.post("mbgl_mytemplate.htm?method=getTmMsg",
        //     {
        //         tmUrl:tempUrl,
        //         tmName:tempName
        //     },
        //     function(result){
        //         if (result != null && result !== "null" && result !== "") {
        //
        //         }
        //         else {
        //             alert("内容文件不存在，无法预览！");
        //         }
        //     });

        // document.getElementById("cust_preview_same").innerHTML = result;
        //在ajax回调中设置参数值
        //0只给一个主数据  1给所有数据
        var frameSrc = 'toPreviewIndex.meditorPage';
        var previewType = 0;
        //需要预览的模板id
        //1显示标题，0不显示
        var title = 0;
        //1显示提示信息，0不显示
        var hint = 0;
        //预览方法
        showTempViewPre(previewType, tmId, title, hint, frameSrc);
        $("#detail_Info_same").css("display", "block");
        //富信预览窗口_相同内容
        $("#detail_Info_same").dialog({
            autoOpen: false,
            modal: true,
            height: 700,
            width: 680,
            closeOnEscape: false,
            resizable: false,
            open: function () {
                //默认不打开错误号码详情
                $(".errorDiv").css("display", "none");
                $(".foldIcon_error").removeClass("unfold");
                $(".foldIcon_error").addClass("fold");
                //这里是处理打开预览界面的时候， 处理界面的显示信息
                $(".ui-dialog-titlebar-close").hide();
                //预发送条数就是有效号码数
                var effs = $.trim($("#effs").text());
                if (eval(effs) === 0) {
                    //没有可发送的号码!不允许发送
                    $("#nosendReason_same").html( getJsLocaleMessage("rms","rms_mbgl_nophonenumber"));
                    //按钮置灰
                    $("#sendConfirm").attr("disabled", true);
                }
            },
            close: function () {
                //设置预览提交时的错误标志
                $("#error").val("");

                $("#sendConfirm").attr("disabled", false);
            }
        });
        $("#detail_Info_same").dialog("open");
        changeDialogTitleBar();
    }
}

//检查余额
function checkspye(fxBalance, spFeeBalance, sendType) {
    var $nosendReason = $("#nosendReason_" + sendType);
    var altStr = "";
    $nosendReason.empty();
    //先校验SP账号余额，再校验富信余额
    if (spFeeBalance.indexOf("lessSpFee") === 0) {
        altStr = getJsLocaleMessage('rms','rms_report_spinsufficientbalance') + spFeeBalance.split("=")[1];
        $("#sendConfirm").attr("disabled", true);
        $("#btsend").attr("disabled", true);
    } else if (spFeeBalance === "null" || spFeeBalance === "" || spFeeBalance === undefined || spFeeBalance === "feeFail") {
        altStr = getJsLocaleMessage('rms','rms_report_getbalancefailed');
        $("#sendConfirm").attr("disabled", true);
        $("#btsend").attr("disabled", true);
    }
    if (altStr !== "") {
        $nosendReason.html(altStr);
        return
    }
    if (fxBalance.indexOf("lessgwfee") === 0) {
        altStr = getJsLocaleMessage('rms','rms_report_fuxininsufficientbalance')  + fxBalance.split("=")[1];
        //altStr = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_40")+yeresult.split("=")[1];
        $("#sendConfirm").attr("disabled", true);
        $("#btsend").attr("disabled", true);
    } else if (fxBalance == null || fxBalance === "" || fxBalance === "feefail" || fxBalance === "nogwfee") {
        //altStr = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_41");
        altStr = getJsLocaleMessage('rms','rms_report_getfuxinbalancefailed');
        $("#sendConfirm").attr("disabled", true);
        $("#btsend").attr("disabled", true);
    }
    $nosendReason.html(altStr);
}

//------------------------------内容时效性
function validHour(hour) {
    var hourNum = hour;
    if (hourNum !== "" && hourNum != null) {
        var patt = /^\d+$/;
        if (!patt.test(hourNum)) {
            alert(getJsLocaleMessage('rms','rms_report_errvalidtime'));
            $("#validHourNum").val("");
        } else {
            //最大为72小时
            if (hourNum > 72) {
                alert(getJsLocaleMessage('rms','rms_report_maxvalidtime72hours'));
                $("#validHourNum").val("");
            }
            if (hourNum < 1) {
                alert(getJsLocaleMessage('rms','rms_report_validtime1hour'));
                $("#validHourNum").val("");
            }
        }
    }
}

//防止数据库网络断开
function fresh() {
    var pathUrl = $("#pathUrl").val();
    $.ajax({
        url: pathUrl + "/LoadingServlet.htm",
        type: "post",
        dataType: "script",
        success: function (result) {
            if (result !== "true") {
                if (errorNum === 0) {
                    errorNum = 1;
                    alert(getJsLocaleMessage('rms','rms_report_connectsvrfail09'));
                    window.clearInterval(dd);
                    window.location.href = 'rms_rmsSameMms.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
                }
            }
        },
        error: function (xrq, textStatus, errorThrown) {
            if (errorNum === 0) {
                errorNum = 1;
                // 网络或服务器无法连接，请稍后重试！[EXFV010]
                alert(getJsLocaleMessage('rms','rms_report_connectsvrfail10'));
                //alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_43"));
            }
            window.clearInterval(dd);
            window.location.href = 'rms_rmsSameMms.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val();
        }
    });
}

//rms_sameMmsPre.jsp 页面调用
//防止内存溢出，初始化为0。预览成功为1，为0则 提示系统繁忙
function checkError() {
    if ($("#error").val() === "0") {
        window.clearInterval(dd);
        $("#probar").dialog("close");
        //服務器繁忙！[EXFV017]
        alert(getJsLocaleMessage('rms','rms_report_serverbusy'));

    }
}

//错误号码详情下载
function uploadbadFiles(flag) {
    var badUrl = $("#badUrl" + flag).val();
    badUrl = badUrl.replace("_view.txt", "_bad.txt");
    $.post("rms_rmsSameMms.htm?method=goToFile", {
        url: badUrl
    }, function (result) {
        if (result === "true") {
            download_href("doExport.hts?u=" + badUrl);
        } else if (result === "false") {
            //文件不存在！
            alert(getJsLocaleMessage('rms','rms_myscene_nofile'));
        }else{
            //操作失败！
            alert(getJsLocaleMessage('rms','rms_mbgl_optfail'));
        }
    });
}

// 预览后取消
function previewReset(flag) {


    //设置预览提交时的错误标志
    $("#error").val("");
    $("#detail_Info_" + flag).dialog("close");
    var pathUrl = $("#pathUrl").val();
    var tempUrl = $("#tempUrl").val();
    if ("diff" === flag) {
        //删除预览生成的资源文件以及html
        $.post(pathUrl + "/rms_diffMms.htm?method=deleteDiffSendPreviewFile",
            {
                tmUrl: tempUrl
            },
            function (result) {
                if (result != null && result !== "") {

                }
            });
        $("#myView").dialog("close");
    }
}

//-----------------预览提交后发送
function previewSubSame() {
// 预览后提交发送_相同内容
    var pathUrl = $("#pathUrl").val();
    $("#sendConfirm").attr("disabled", "disabled");
    //定时发送还是即时发送
    var timerStatus = $("form[name='form2']").find("input:checked[name='sendType']").val();
    //定时发送的时间
    var timerTime = $("form[name='form2']").find("input[name='sendtime']").val();
    //富信任务ID
    var taskId = $("#taskId").val();
    //模板类型 11富媒体、12卡片和13富文本 14短信
    var templateType = $("#templateType").val();
    //号码文件地址
    var phoneFileUrl = $("#upNumberPhoneUrl").val();
    //用户ID
    var lguserid = $("#lguserid").val();
    //企业编码
    var lgcorpcode = $("#lgcorpcode").val();
    //彩信发送账号
    var spUser = $("#mmsUser").val();
    //富信选择模板ID
    var tempId = $("#tempId").val();
    //富信选择模板路径
    var tempUrl = $("#tempUrl").val();
    //总数
    var subCount = $.trim($("#counts").text());
    //有效数
    var effCount = $.trim($("#effs").text());
    //内容时效
    var validtm = $("#validHourNum").val();
    if (validtm == null || validtm === "" || validtm === undefined) {
        validtm = $("#validHourNum").attr("defaultVal");
    }
    //发送主题
    var taskName = $("#tempInputName").val();
    //业务类型编码
    var busCode = $("#busCode").val();

    layer1 = layer.msg(getJsLocaleMessage("common","common_frame2_checkFee_20"), {
        //图标
        icon: 16,
        //遮罩阴影透明度 1为全黑
        shade: 0.5,
        //是否自动关闭
        time:false
    });
    //处理提交富信发送
    $.post(pathUrl + "/rms_rmsSameMms.htm?method=sendSameRMS",
        {
            sendType: 0,
            templateType: templateType,
            taskName: taskName,
            timerTime: timerTime,
            timerStatus: timerStatus,
            taskId: taskId,
            phoneFileUrl: phoneFileUrl,
            lguserid: lguserid,
            lgcorpcode: lgcorpcode,
            spUser: spUser,
            tempId: tempId,
            tempUrl: tempUrl,
            subCount: subCount,
            effCount: effCount,
            validtm: validtm,
            busCode: busCode
        }, function (msg) {
            //关闭小菊花
            layer.close(layer1);

            if(msg.indexOf("html") > 0){
                //alert(getJsLocaleMessage('rms','rms_report_responsetimeout'));
                var msgTip = getJsLocaleMessage('rms','rms_report_responsetimeout');
                alertTips(msgTip,true);
                return;
            }else if(msg === "" || msg === "sendError" || msg === "error" || msg == null){
                //alert(getJsLocaleMessage('rms','rms_report_czsbqcs'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_czsbqcs');
                alertTips(msgTip,true);
            }else if("timerFail" === msg){
                //alert(getJsLocaleMessage('rms','rms_report_qyfxfsdsrwszsb'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_qyfxfsdsrwszsb');
                alertTips(msgTip,true);
            }else if("timerSuccess" === msg){
                //alert(getJsLocaleMessage('rms','rms_report_cjdsrwtjcg'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_cjdsrwtjcg');
                alertTips(msgTip,true);
            }else if(msg.indexOf("sendFail") === 0){
                var msgCode = msg.substr(msg.indexOf("&")+1);
                if(msgCode !== ""){
                    //alert(getJsLocaleMessage('rms','rms_report_fuxinsendfailure')+"[" + msgCode + "]");
                    var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendfailure')+"[" + msgCode + "]";
                    alertTips(msgTip,true);
                }else {
                    //alert(getJsLocaleMessage('rms','rms_report_fuxinsendfailure'));
                    var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendfailure');
                    alertTips(msgTip,true);
                }
                //reloadPage();
            }else if(msg === "handled"){
                //alert(getJsLocaleMessage('rms','rms_report_fuxincreateok'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_fuxincreateok');
                alertTips(msgTip,true);
            } else if(msg.indexOf("sendSuccess") === 0) {
                $("#oldTaskId").val(msg.substr(msg.indexOf("&")+1));
                //alert(getJsLocaleMessage('rms','rms_report_fuxinsendsuccess'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendsuccess');
                alertTips(msgTip,true);
            } else {
                if (msg.indexOf("empex") === 0) {
                    var errorCode = msg.substr(msg.indexOf("["));
                    //alert(getJsLocaleMessage('rms','rms_report_fuxinsendfailure') + errorCode + "。");
                    var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendfailure') + errorCode + "。";
                    alertTips(msgTip,true);
                }
                $("#detail_Info").dialog("close");
                $("#subSend1").attr("disabled", "");
                //reloadPage();
            }
        });
}

// 预览后提交发送_不同内容
function previewSubDiff() {
    //富信选择模板路径
    var tempUrl = $("#tempUrl").val();
    var pathUrl = $("#pathUrl").val();
    //删除预览生成的资源文件以及html
    $.post(pathUrl + "/rms_diffMms.htm?method=deleteDiffSendPreviewFile",
        {
            tmUrl: tempUrl
        },
        function (result) {
            if (result != null && result !== "") {

            }
        });
    $("#btsend").attr("disabled", "disabled");
    //定时发送还是即时发送
    var timerStatus = $("form[name='form2']").find("input:checked[name='sendType']").val();
    //定时发送的时间
    var timerTime = $("form[name='form2']").find("input[name='sendtime']").val();
    //富信任务ID
    var taskId = $("#taskId").val();
    //模板类型 11富媒体、12卡片和13富文本 14短信
    var templateType = $("#templateType").val();
    //号码文件地址
    var phoneFileUrl = $("#upNumberPhoneUrl").val();
    //用户ID
    var lguserid = $("#lguserid").val();
    //企业编码
    var lgcorpcode = $("#lgcorpcode").val();
    //彩信发送账号
    var spUser = $("#mmsUser").val();
    //富信选择模板ID
    var tempId = $("#tempId").val();
    //总数
    var subCount = $.trim($("#counts_diff").text());
    //有效数
    var effCount = $.trim($("#effs_diff").text());
    //内容时效
    var validtm = $("#validHourNum").val();
    if (validtm == null || validtm === "" || validtm === undefined) {
        validtm = $("#validHourNum").attr("defaultVal");
    }
    //发送主题
    var taskName = $("#tempInputName").val();
    //业务类型编码
    var busCode = $("#busCode").val();
    //模板参数
    var paramCount = $("#paramCount").val();
    //处理提交富信发送

    //提交发送时出现旋转等待页面
    layer1 = layer.msg(getJsLocaleMessage("common","common_frame2_checkFee_20"), {
        //图标
        icon: 16,
        //遮罩阴影透明度 1为全黑
        shade: 0.5,
        //是否自动关闭
        time:false
    });

    $.post(pathUrl + "/rms_diffMms.htm?method=send",
        {
            sendType: 1,
            templateType: templateType,
            timerTime: timerTime,
            timerStatus: timerStatus,
            taskId: taskId,
            phoneFileUrl: phoneFileUrl,
            lguserid: lguserid,
            lgcorpcode: lgcorpcode,
            spUser: spUser,
            tempId: tempId,
            tempUrl: tempUrl,
            subCount: subCount,
            effCount: effCount,
            validtm: validtm,
            taskName: taskName,
            busCode: busCode,
            paramCount: paramCount
        }, function (msg) {

            //关闭小菊花
            layer.close(layer1);

            if(msg.indexOf("html") > 0){
                //alert(getJsLocaleMessage('rms','rms_report_responsetimeout'));
                //window.location.href = window.location.href;
                var msgTip = getJsLocaleMessage('rms','rms_report_responsetimeout');
                alertTips(msgTip,true);
                return;
            }else if(msg === "" || msg === "sendError" || msg === "error" || msg == null){
                //alert(getJsLocaleMessage('rms','rms_report_czsbqcs'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_czsbqcs');
                alertTips(msgTip,true);
            }else if("timerFail" === msg){
                //alert(getJsLocaleMessage('rms','rms_report_qyfxfsdsrwszsb'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_qyfxfsdsrwszsb');
                alertTips(msgTip,true);
            }else if(msg === "handled"){
                //alert(getJsLocaleMessage('rms','rms_report_fuxincreateok'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_fuxincreateok');
                alertTips(msgTip,true);
            }else if("timerSuccess" === msg){
                //alert(getJsLocaleMessage('rms','rms_report_cjdsrwtjcg'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_cjdsrwtjcg');
                alertTips(msgTip,true);
            }else if(msg.indexOf("sendFail") === 0){
                var megCode = msg.substr(msg.indexOf("&")+1);
                if(megCode !== ""){
                    //alert(getJsLocaleMessage('rms','rms_report_fuxinsendfailure')+"[" + megCode + "]。");
                    var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendfailure')+"[" + megCode + "]。";
                    alertTips(msgTip,true);
                } else {
                    //alert(getJsLocaleMessage('rms','rms_report_fuxinsendfailure'));
                    var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendfailure');
                    alertTips(msgTip,true);
                }
            }else if(msg.indexOf("sendSuccess") === 0) {
                $("#oldTaskId").val(msg.substr(msg.indexOf("&")+1));
                //alert(getJsLocaleMessage('rms','rms_report_fuxinsendsuccess'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendsuccess');
                alertTips(msgTip,true);
            }else if(msg === "timerSuccess") {
                //alert(getJsLocaleMessage('rms','rms_report_cjdsrwtjcg'));
                //reloadPage();
                var msgTip = getJsLocaleMessage('rms','rms_report_cjdsrwtjcg');
                alertTips(msgTip,true);
            } else {
                if (msg.indexOf("empex") === 0) {
                    var errorCode = msg.substr(msg.indexOf("["));
                    //alert(getJsLocaleMessage('rms','rms_report_fuxinsendfailure') + errorCode + "。");
                    var msgTip = getJsLocaleMessage('rms','rms_report_fuxinsendfailure') + errorCode + "。";
                    alertTips(msgTip,true);
                }
                $("#detail_Info").dialog("close");
                $("#btsend").attr("disabled", "");
                reloadPage();
            }
        });
}

//-------------模板预览---------------
function tempPreview(tempUrl, tempName, tmId) {
    var tmId = tmId;
    //0只给一个主数据  1给所有数据
    var frameSrc = 'toPreviewIndex.meditorPage';
    var previewType = 0;
    //需要预览的模板id
    //1显示标题，0不显示
    var title = 0;
    //1显示提示信息，0不显示
    var hint = 0;
    //预览方法
    showTempView(previewType, tmId, title, hint, frameSrc);
    /*var pathUrl = $("#pathUrl").val();
    $.post(pathUrl+"/mbgl_mytemplate.htm?method=getTmMsg",
        {
            tmUrl:tempUrl,
            tmName:tempName,
            tmid:tmId
        },
        function(result){
            if (result != null && result !== "null" && result !== "") {
                $("#cust_preview").html(result);
            } else {
               // alert("内容文件不存在，无法预览！");
                $("#cust_preview").html("");
            }
        });*/
}

function inputTipText() {
    //所有样式名中含有graytext的input
    $("input[class*=graytext]").each(function() {
        var oldVal = $(this).val(); //默认的提示性文本
        $(this).css({
            'color': '#999'
        }) //灰色
            .focus(function() {
                if ($(this).val() != oldVal) {
                    $(this).css({
                        'color': '#000'
                    })
                } else {
                    $(this).val('').css({
                        'color': '#999'
                    })
                }
            }).blur(function() {
            if ($(this).val() == "") {
                $(this).val(oldVal).css({
                    'color': '#999'
                })
            }
        }).keydown(function() {
            $(this).css({
                'color': '#000'
            })
        })
    })
}

$(function() {
    inputTipText(); //直接调用就OK了
})

//不同内容发送预览
function diffSendTempPreview(tempUrl, tempName, excelJson) {
    var json = decodeURIComponent(excelJson);
    json = json.replace(/%20/g, " ");
    var jsonObject = JSON.parse(json);
    // var jsonArr = jsonObject[0].content.param;
    var paramStr = jsonObject[0].content;
    var jsonArr = JSON.parse(paramStr).param;
    var tmId = $("#tmId").val();
    //0只给一个主数据  1给所有数据
    var frameSrc = 'toPreviewIndex.meditorPage';
    var previewType = 0;
    //需要预览的模板id
    //1显示标题，0不显示
    var title = 0;
    //1显示提示信息，0不显示
    var hint = 0;
    //预览方法
    var jsonStr = JSON.stringify(jsonArr);
    showDiffTempViewPre(previewType, tmId, title, hint, jsonStr, frameSrc);
    // $("#cust_diff_preview").show();
    /*var pathUrl = $("#pathUrl").val();
    $.post(pathUrl+"/rms_diffMms.htm?method=tempPreview",
        {
            tmUrl:tempUrl,
            tmName:tempName,
            excelJson:excelJson,
            //画饼图时需要的标记
            val:true
        },
        function(result){
            if (result != null && result !== "") {
                $("#cust_diff_preview").html(result);
                $('#myView').dialog({
                    autoOpen: false,
                    width:358,
                    height:670
                });
                $("#myView").dialog("open");
                changeDialogTitleBar();
                //显示右上角的X按钮
                $("#ui-dialog-title-myView").parent().find(".ui-dialog-titlebar-close").show();
            } else if(result === "false"){
                //alert("内容文件不存在，无法预览！");
                $("#cust_diff_preview").html("");
            }
        });*/
}

//下载号码格式文件
function downExcel() {
    var tmUrl = $("#tempUrl").val();
    var tmId = $("#tmId").val();
    var tempType = $("#templateType").val();
    var pathUrl = $("#pathUrl").val();
    download_href(pathUrl + "/rms_commTpl.htm?method=downPhoneFile&tmMsg=" + tmUrl + "&tmId=" + tmId + "&tempType=" + tempType);
}

function changeDialogTitleBar() {
    $(".ui-dialog-titlebar").css("height", "50px");
    $(".ui-dialog-titlebar").css("background-color", "#f7fafc");
    $(".ui-dialog-titlebar").css("border-bottom", "1px solid #ecf1f2");
}

function changeInputCss() {
    $("#tph").removeClass("lostFocus");
    $("#tph").addClass("focus");
}

function lostInputCss() {
    $("#tph").removeClass("focus");
    $("#tph").addClass("lostFocus");
}

function showReminder(id, id2, flag) {
    if(flag === 1){
        $("#reminder").css("top","323px");
    }else {
        $("#reminder").css("top","350px");
    }
    $("#" + id).hover(function () {
        $("#" + id2).show();
    }, function () {
        $("#" + id2).hide();
    });
}
function alertTips(msg,reload){
    layer.confirm(
        msg,{
        btn: ['确认'],
        closeBtn: 0
        },
        function(){
        if (reload){
            window.location.href = window.location.href;
        }
    });
}