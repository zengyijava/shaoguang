$(document).ready(function() {
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
    $('#search').click(function(){submitForm();});

    $('#spisuncm').isSearchSelect({'width':'180','zindex':1,'isInput':false});
    $('#sendType').isSearchSelect({'width':'180','zindex':1,'isInput':false});
    $('#downType').isSearchSelect({'width':'180','zindex':1,'isInput':false});
});

function showback(){
    var rmsMtTaskVo = $.cookie('rmsMtTaskVo');
    var pageInfo = $.cookie('rmsMtTask_pageInfo');
    if(rmsMtTaskVo === "" || rmsMtTaskVo === "null"){
        alert(getJsLocaleMessage("rms","rms_rltcreport_loadingerr"));
        return;
    }
    if(pageInfo === "" || pageInfo === "null"){
        alert(getJsLocaleMessage("rms","rms_rltcreport_loadingerr"));
        return;
    }
    window.location.href = "rms_rmsTaskHistory.htm?method=find&isBack=true";
}

function downRptExcel(countSize,totalRec) {
    if(confirm(getJsLocaleMessage("rms","rms_visireport_confirmexp"))) {
        if(parseInt(totalRec) > 500000){
            alert(getJsLocaleMessage("rms","rms_rltcreport_exp50w"));
            return;
        }
        if(parseInt(countSize) > 0){
            $.ajax({
                type: "POST",
                url: "rms_rmsTaskHistory.htm?method=getDetailRptExcel",
                data: {},
                beforeSend:function () {
                    page_loading();
                },
                complete:function () {
                    page_complete()
                },
                success:function (result) {
                    if (result === 'true') {
                        download_href("rms_rmsTaskHistory.htm?method=downloadFile");
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