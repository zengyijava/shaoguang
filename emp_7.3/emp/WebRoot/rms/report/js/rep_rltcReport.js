$(document).ready(function(){

    getLoginInfo("#hiddenValueDiv");

    if($('#skin').val().indexOf('frame4.0') == -1) {
        $('#spUser').isSearchOldSelect({'width':'154','height':'22'});
        $('#degree,#operator,#reportType,#reportTypeSelect').isSearchOldSelect({'width':'154','height':'22','isInput':false});
    }

    $("#toggleDiv").toggle(function() {
        $("#condition").hide();
    }, function() {
        $("#condition").show();
    });

    $("#content tbody tr").hover(function() {
        $(this).addClass("hoverColor");
    }, function() {
        $(this).removeClass("hoverColor");
    });

    $('#search').click(function(){submitForm();});

    var reportType = $("#reportTypeSelect").val();

    reportType = reportType === "" || reportType === undefined || reportType === "null" ? 0:parseInt(reportType);

    //初始化为日报表
    setTimeWhenHistory(reportType);

    var $selectedLi = $("#reportTypeSelect").next(".c_selectBox").find("ul.c_result li");
    $selectedLi.each(function () {
        var value = $(this).html();
        var intVal;
        switch (value){
            case getJsLocaleMessage("rms","rms_repdegree_monthlyrep"):intVal = 1;break;
            case getJsLocaleMessage("rms","rms_repdegree_yearlyrep"):intVal = 2;break;
            default:intVal = 0;//日报表
        }
        $(this).click(function () {
            setTimeWhenHistory(intVal);
        });
    });

	$("#endTime").click(function(){
		if(!$(this).hasClass('Wdate')){
			return;
		}
		var max = "2099-12-31 00:00:00";
		var v = $(this).parents("tr").find("#startTime").val();
		if(v.length !== 0) {
			min = v;
		}
		WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max,isShowClear:false});
	});

	$("#startTime").click(function(){
		var r = $("#reportTypeSelect").val();
		if (r === "1") {
			WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});
		} else if(r === "2") {
			WdatePicker({dateFmt:'yyyy',isShowClear:false});
		} else {
			if(!$(this).hasClass('Wdate')){
				return;
			}
			var max = "2099-12-31";
			var v = $(this).parents("tr").find('#endTime').val();
			var min = "1900-01-01";
			if(v.length !== 0) {
				max = v;
			}
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:max,isShowClear:false});
		}

	});
});

	function setValue(spisuncm,degree){
		$("#hiddenDegree").val(degree);
		$("#hiddenSpisuncm").val(spisuncm);
		$('#isDes').val(1);
	}

	function setTimeWhenHistory(value) {
    var nowDate = new Date();
    var startTime = $("#startTimeTemp").val();
    var endTime = $("#endTimeTemp").val();
    var t = nowDate.Format("yyyy-MM-dd");
    if (value === 2) {
        $("#startTime").show();
        $("#endTime").val("");
        $("#endTime").hide();
        $(".toSpan").hide();
        $("#startTime").val(startTime||t.substring(0,4));
        $('#yearOrMonth').text(getJsLocaleMessage("rms","rms_rltcreport_statisticsy"));
        $(".reminder").hide();
    }
    else if(value === 1) {
        $("#startTime").show();
        $("#endTime").val("");
        $("#endTime").hide();
        $(".toSpan").hide();
        $("#startTime").val(startTime||t.substring(0,7));
        $('#yearOrMonth').text(getJsLocaleMessage("rms","rms_rltcreport_statisticsm"));
        $(".reminder").hide();
    } else if(value === 0){
        $("#startTime").show();
        $("#endTime").show();
        $(".toSpan").show();
        $('#yearOrMonth').text(getJsLocaleMessage("rms","rms_rltcreport_statisticst"));
        var ts = t.replace(/\d+$/,'01');
        $("#startTime").val(startTime||ts);
        $("#endTime").val(endTime||t);
        $(".reminder").show();
    }
    $("#startTimeTemp").val("");
    $("#endTimeTemp").val("");
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
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
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

/**
 * 点击详情
 */
function showDetailsInfo(detailInfo) {
    //将detailInfo信息存在cookie中，方便分页拿到
    $.cookie('degreeRpt_detailInfo', null);
    $.cookie('degreeRpt_detailInfo', detailInfo);
    window.location.href = "rep_degreeReport.htm?method=showDetailsInfo";
}
/**
 下载档位统计报表
 */
function downDegreeRpt(countSize,totalRec) {
    if(confirm(getJsLocaleMessage("rms","rms_visireport_confirmexp"))) {
        if((totalRec !== "isDes" && totalRec > 500000) || (totalRec === "isDes" && countSize > 500000)){
            alert(getJsLocaleMessage("rms","rms_rltcreport_exp50w"));
            return;
        }
        var isDes = totalRec === "isDes" ? 1 : 0;
        if(countSize > 0){
            $.ajax({
                type: "POST",
                url: "rep_degreeReport.htm?method=getRptExcel",
                data: {
                    isDes:isDes
                },
                beforeSend:function () {
                    page_loading();
                },
                complete:function () {
                    page_complete()
                },
                success:function (result) {
                    if (result === 'true') {
                        download_href("rep_degreeReport.htm?method=downloadFile");
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

/**
 * 点击返回
 */
function back() {
    var degreeRptVo = $.cookie('degreeRptVo');
    var pageInfo = $.cookie('degree_pageInfo');
    if(degreeRptVo === "" || degreeRptVo === "null"){
        alert(getJsLocaleMessage("rms","rms_rltcreport_loadingerr"));
        return
    }
    if(pageInfo === "" || pageInfo === "null"){
        alert(getJsLocaleMessage("rms","rms_rltcreport_loadingerr"));
        return
    }
    var lgcorpcode = $("#lgcorpcode").val();
    var vo = eval('('+degreeRptVo+')');
    var chgrade = vo.chgrade === undefined ? "": vo.chgrade;
    var cropName = vo.cropName === undefined ? "": vo.cropName;
    var corpCode = vo.corpCode === undefined ? "": vo.corpCode;
    var startTime = vo.startTime === undefined ? "": vo.startTime;
    var endTime = vo.endTime === undefined ? "": vo.endTime;
    var spisuncm = vo.spisuncm === undefined ? "": vo.spisuncm;
    var userId = vo.userId === undefined ? "": vo.userId;
    var reportType = vo.reportType === undefined ? "": vo.reportType;
    var url = "rep_degreeReport.htm?method=findDegreeRpt" +
        "&pageIndex=" + pageInfo.split("&")[1] + "&pageSize=" + pageInfo.split("&")[0] +
        "&degree=" + chgrade + "&operator=" + spisuncm + "&spUser=" + userId +
        "&reportType=" + reportType + "&startTime=" + startTime + "&endTime=" + endTime;
    if(lgcorpcode === "100000"){
        url += "&cropName=" + cropName + "&corpCode=" + corpCode;
    }
    window.location.href = url;
}