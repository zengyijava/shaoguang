var contentLen = 990;
var baseContentLen = 990;


function closeChangeTaskdiv() {
    parent.cancelChangeTaskdiv();
}


function contentLimi(ob) {
    var $this = ob,
        _val = $this.val(),
        count = "";
    if (_val.length > 100) {
        $this.val(_val.substring(0, 100));
    }
    count = 100 - $this.val().length;
    $("#text-count").text(count);
}



function confirmSub() {
    //询问框
    var validContent = $("#textArea").val() + $("#showtailcontent").text();
    var tipMsg = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_313');
    var confitm = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_314');
    var cancel = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_315');
    var tip = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_316');
    var msgType = $("#msgType").val();
    if (msgType ==1 && (-1 == validContent.indexOf("退订"))) {
        layer.confirm(tipMsg, {
            btn: [confitm, cancel], //按钮
            title: tip
        }, function () {
            changeTask();
        });
    } else {
        changeTask();
    }

}

function changeTask() {
    var timerTime;
    var checked = $("input[name='timerStatus']:checked").val();
    if (checked == 0) {//立即发送
        timerTime = "now";
    } else if (checked == 1) {//修改定时
        timerTime = $("#timerTime").val();
    }
    var mtId = $("#mtId").val();
    var msgType = $("#msgType").val();
    if(msgType != "1"){
        //不是相同内容群发，从xmp标签获取内容
        mtMsg = $("#msgDefault").text();
    }else {
        mtMsg = $("#textArea").val();
    }
    var showtailcontent = $("#showtailcontent").text();
    var subUrl = "smt_smsSendedBox.htm?method=changeTiming";
    if(timerTime == null || timerTime.trim().length == 0){
        alert("发送时间为空！");
        return;
    }
    //msgType  1-相同内容短信 2－动态模板短信；3－文件内容短信
    //msgType是相同内容，则需要判断发送内容是否为空
    if(msgType=="1") {
        if (mtMsg == null || mtMsg.trim().length == 0) {
            alert("发送内容为空！");
            return;
        }
    }
    $.post(subUrl, {
        mtId: mtId,
        timerTime: timerTime,
        mtMsg: mtMsg,
        showtailcontent: showtailcontent,
    }, function (message) {
        var result = message;
        if (result != "-1") {
            if (result.indexOf("empex") == 0) {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_317') + result.substr(5);
            } else if (result == "timerSuccess") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_2');
            } else if (result == "timerFail") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_3');
            } else if (result == "uploadFileFail") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_4');
            } else if (result == "createSuccess") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_5');
            } else if (result == "timeError") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_6');
            } else if (result == "000") {
                //alert("创建短信任务及发送到网关成功！");
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_312');
            } else if (result == "saveSuccess") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_7');
            } else if (result == "error") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_8');
            } else if (result == "nospnumber") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_9') + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_10');
            } else if (result == "depfee:-2") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_11');
            } else if (result == "depfee:-1") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_12');
            } else if (result == "spuserfee:-2") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_13');
            } else if (result == "spuserfee:-1" || result == "spuserfee:-3") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_14');
            } else if (result == "subnoFailed") {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_15');
            } else if ("nogwfee" == result || "feefail" == result) {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_16');
            } else if (result.indexOf("lessgwfee") == 0) {
                message = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_17');
            }
        }
        /*layer.confirm(message, {
            title: false,
            btn: ['确认'] //按钮
        }, function () {
            window.parent.location.reload(true);
        });*/
        alert(message);
        window.parent.location.reload(true);
    });
}

function textInput() {
    //贴尾内容
    var showtailcontent = $("#showtailcontent").val() || '';
    //短信内容
    var smsContent = $("#textArea").val();
    //发送内容
    var textArea = smsContent + showtailcontent;
    //回车长度
    var huiche = textArea.length - textArea.replaceAll("\n", "").length;

    //短信内容+回车大于最大长度
    if (textArea.length + huiche > contentLen) {
        $("#textArea").val(textArea.substring(showtailcontent.length, contentLen - huiche));
        //短信内容
        smsContent = $("#textArea").val();
        //贴尾内容+短信内容
        content = showtailcontent + smsContent;
        huiche = textArea.length - textArea.replaceAll("\n", "").length;
        $('#maxLen').html("/" + contentLen);
    }
    var len = contentLen;
    if (baseContentLen == 700) {
        setSmsContentMaxLen(showtailcontent, textArea);
        content = showtailcontent + $("#textArea").val();
        huiche = textArea.length - textArea.replaceAll("\n", "").length;
    }
    if (len != contentLen) {
        content = showtailcontent + $("#textArea").val();
        if (textArea.length > contentLen) {
            $("#textArea").val(textArea.substring(showtailcontent.length, contentLen));
            smsContent = $("#textArea").val();
            huiche = smsContent.length - smsContent.replaceAll("\n", "").length;
        }
        $('#maxLen').html("/" + contentLen);
    }
    $("#strlen").text($("#textArea").val().length+huiche+showtailcontent.length);
}

// 短信内容键盘事件监听统计字数
function synlen() {
    $("#textArea").keyup(function () {
        textInput(this);
    });
}

//设置贴尾
function setTailInfo() {
    //业务类型
    var busCode = $('#busCode').val();
    //sp账号
    var spUser = $("#spUser").val();
    //返回结果
    var result = true;
    //先根据其他贴尾类型查找贴尾 再根据全局贴尾 找到就终止
    $.ajax({
        url: 'common.htm',
        data: {method: 'setTailInfo', busCode: busCode, spUser: spUser, lgcorpcode: GlobalVars.lgcorpcode},
        type: 'post',
        dataType: 'text',
        async: false,
        beforeSend: function () {
            $('#tail-area').hide();
            $('#showtailcontent').text('');
            $('#tailcontents').val('');
        },
        success: function (data) {
            data = eval('(' + data + ')');
            if (data.status == 1) {//找到对应贴尾
                $('#showtailcontent').text(data.contents);
                $('#tailcontents').val(data.contents);
                $('#tail-area').show();
            }
        },
        error: function () {
            result = false;
        },
        complete: function () {

        }
    })
    return result;
}

//根据SP账号设置短信最长位数
var spUser = $("#spUser").val();
function setContentMaxLen(spUser) {
    $.post("ssm_comm.htm?method=getSpGateConfig&spUser=" + spUser, {},
        function (infoStr) {
            if (infoStr != "error" && infoStr.startsWith("infos:")) {
                var infos = infoStr.replace("infos:", "").split("&");
                var interGW = infos[3];
                var interGWs = interGW.split(",");
                entotalLen = interGWs[5];
                /*//签名前置
                if(interGWs[10] ==1)
                {
                    entotalLen = entotalLen - interGWs[7];
                }*/
                contentLen = infos[4];
                //贴尾内容
                var showtailcontent = $("#showtailcontent").val() || '';
                //短信内容
                var smsContent = $("#textArea").val();
                //发送内容
                var textArea = smsContent + showtailcontent;
                setSmsContentMaxLen(showtailcontent,textArea);
                baseContentLen = contentLen;
                $('#maxLen').html("/" + contentLen);
            }
        }
    );
}

//通过短信内容设置短信最长位数
function setSmsContentMaxLen(tailcontents, content) {
    var huiche = content.length - content.replaceAll("\n", "").length;
    var len = huiche + content.length
    var isChinese = false;
    var enLen = 0;
    var charCode;
    var pattern = /(9[1-4]|12[3-6])/;
    var enMsgShortLen = 0;
    for (var j = 0; j < len; j++) {
        enMsgShortLen += 1;
        enLen += 1;
        charCode = content.charAt(j).charCodeAt();
        if (charCode > 127) {
            isChinese = true;
            break;
        }
        if (pattern.test(charCode)) {
            //长短信边界值
            if (enLen % entotalLen == 0) {
                //条数加2
                enLen += 2;
            }
            else {
                enLen += 1;
            }
            enMsgShortLen += 1;
        }
        //英文短信长度超过最大短信长度
        if (enLen > contentLen) {
            $("#textArea").val(content.substring(tailcontents.length, j - huiche));
            break;
        }
    }

    if (isChinese && contentLen == 700) {
        contentLen = 350;
    }
    else if (!isChinese && contentLen == 350 && baseContentLen == 700) {
        contentLen = 700;
    }
}
//定义startWith方法
String.prototype.startWith=function(str){
    if(str==null||str==""||this.length==0||str.length>this.length)
        return false;
    if(this.substr(0,str.length)==str)
        return true;
    else
        return false;
    return true;
}