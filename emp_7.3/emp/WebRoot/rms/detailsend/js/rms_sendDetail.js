$(function () {
    closeTreeFun([ "dropMenu" ]);
    getLoginInfo("#hiddenValueDiv");
    $("#toggleDiv").toggle(function() {
        $("#condition").hide();
        $(this).addClass("collapse");
    }, function() {
        $("#condition").show();
        $(this).removeClass("collapse");
    });
    $("#content tbody tr").hover(function() {
        $(this).addClass("hoverColor");
    }, function() {
        $(this).removeClass("hoverColor");
    });
    $('#search').click(function() {
        submitForm();
    });

    var recordType = $("select[name='recordType']").val();
    var nowTime=new Date();
    var sendtimeVal = $("#sendTime").val();
    var recvtimeVal = $("#recvTime").val();
    if("realTime" === recordType && (sendtimeVal === "" || sendtimeVal == null) && (recvtimeVal === "" || recvtimeVal == null)){
        //实时记录只能查询三天之内
        nowTime.setDate(nowTime.getDate() - 3);
        $("#sendTime").val(nowTime.Format("yyyy-MM-dd HH:mm:ss"));
        $("#recvTime").val(new Date().Format("yyyy-MM-dd HH:mm:ss"));
    }

    var $selectedLi = $("#recordType").next(".c_selectBox").find("ul.c_result li");
    $selectedLi.each(function () {
        var value = $(this).html();
       $(this).click(function () {
           setTimeWhenHistory(value);
       });
    });
});

//发送起止时间控制
function revtime(){
    var nowDate = new Date();
    var max = nowDate.Format("yyyy-MM-dd HH:mm:ss");
    var changeDateStr;
    var v = $("#sendTime").attr("value");
    var recordType = $("#recordType").val();
    if(recordType === "realTime"){
        //实时记录只允许发送时间延后三天
        WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max,isShowClear:false,enableInputMask:false});
    }else {
        if (v.length !== 0) {
            var year = v.substring(0, 4);
            var mon = v.substring(5, 7);
            var day = 31;
            if (mon !== "12") {
                mon = String(parseInt(mon, 10) + 1);
                if (mon.length === 1) {
                    mon = "0" + mon;
                }
                switch (mon) {
                    case "01":
                        day = 31;
                        break;
                    case "02":
                        day = 28;
                        break;
                    case "03":
                        day = 31;
                        break;
                    case "04":
                        day = 30;
                        break;
                    case "05":
                        day = 31;
                        break;
                    case "06":
                        day = 30;
                        break;
                    case "07":
                        day = 31;
                        break;
                    case "08":
                        day = 31;
                        break;
                    case "09":
                        day = 30;
                        break;
                    case "10":
                        day = 31;
                        break;
                    case "11":
                        day = 30;
                        break;
                }
            } else {
                year = String((parseInt(year, 10) + 1));
                mon = "01";
            }
            changeDateStr = year + "-" + mon + "-" + day + " 23:59:59"
        }
        //最大时间超过当前时间则认为max为当前时间
        var changeDate = new Date(changeDateStr.replace("-", "/").replace("-", "/"));
        max = changeDate > nowDate ? max : changeDateStr;
        WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max,isShowClear:false,enableInputMask:false});
    }
}

function sedtime(){
    var v = $("#recvTime").attr("value");
    var recordType = $("#recordType").val();
    if(recordType === "realTime"){
        //实时查询，开始时间只能设置为结束时间到减3天之内的时间
        var nowTime = new Date();
        nowTime.setDate(nowTime.getDate() - 3);
        nowTime.setSeconds(nowTime.getSeconds() - 30);
        var minTime = nowTime.Format("yyyy-MM-dd HH:mm:ss");
        WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:minTime,maxDate:v,isShowClear:false,enableInputMask:false});
    }else {
        var max = "2099-12-31 23:59:59";
        var min = "1900-01-01 00:00:00";
        if (v.length !== 0) {
            max = v;
            var year = v.substring(0, 4);
            var mon = v.substring(5, 7);
            if (mon !== "01") {
                mon = String(parseInt(mon, 10) - 1);
                if (mon.length === 1) {
                    mon = "0" + mon;
                }
            } else {
                year = String((parseInt(year, 10) - 1));
                mon = "12";
            }
            min = year + "-" + mon + "-01 00:00:00"
        }
        WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max,isShowClear:false,enableInputMask:false});
    }
}

/*当选择查询历史记录时，默认设置时间为当月1号到当前系统时间*/
function setTimeWhenHistory(value) {
    var nowTime=new Date();
    if("历史记录" === value){
        $("#sendTime").val(nowTime.Format("yyyy-MM-01 00:00:00"));
        $("#recvTime").val(nowTime.Format("yyyy-MM-"+getDateInMonth()+" 23:59:59"));
    }else if("实时记录" === value){
            //实时记录只能查询四天之内
        $("#sendTime").val(new Date(nowTime - 4 * 24 * 60 * 60 * 1000).Format("yyyy-MM-dd 00:00:00"));
        $("#recvTime").val(new Date().Format("yyyy-MM-dd HH:mm:ss"));
    }


}

/**
 * 获取本月天数
 * @returns {number}
 */
function getDateInMonth(){
    var curDate = new Date();
    /* 获取当前月份 */
    var curMonth = curDate.getMonth();
    /*  生成实际的月份: 由于curMonth会比实际月份小1, 故需加1 */
    curDate.setMonth(curMonth + 1);
    curDate.setDate(0);
    /* 返回当月的天数 */
    return curDate.getDate();
}
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
/*
报表下载
 */
function downDetailRpt(countSize,totalRec) {
    if(confirm(getJsLocaleMessage("rms","rms_visireport_confirmexp"))) {
        if(totalRec !== "isDes" && totalRec > 500000){
            alert(getJsLocaleMessage("rms","rms_rltcreport_exp50w"));
            return;
        }
        if(countSize > 0){
            $.ajax({
                type: "POST",
                url: "rms_sendDetail.htm?method=getSendDetailExcel",
                data: {},
                beforeSend:function () {
                    page_loading();
                },
                complete:function () {
                    page_complete()
                },
                success:function (result) {
                    if (result === 'true') {
                        download_href("rms_sendDetail.htm?method=downloadFile");
                    } else {
                        alert(getJsLocaleMessage("rms","rms_visireport_failexp"));
                    }
                }
            });
        }else {
            alert(getJsLocaleMessage("rms","rms_visireport_nodataexp"));
        }
    }
}
function showTempView(id) {
    var param = '?&id=' + id;
    layer.open({
        type: 2,
        title:'预览效果',
        maxmin: true,
        shadeClose: true, //点击遮罩关闭层
        area : ['500px' , '700px'],
        content: 'toShowTemp.meditorPage'+param
    });
    //解决IE10兼容性问题
    setTimeout(function() {
        var version = userAgent();
        if (version == 'ie10' || version == 'ie9' || version == 'ie8' || version == 'ie6') {
            page_complete();
        }
    }, 1500);
}