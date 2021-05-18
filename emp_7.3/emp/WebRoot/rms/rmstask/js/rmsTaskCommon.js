var zTree3;
var zTree2;
var setting3;
var setting2;
var zNodes2 = [];
var zNodes3 = [];
var htmName = "rmsTaskUserAndDep.htm";

$(document).ready(function() {
    //加载头文件内容
    getLoginInfo("#hiddenValueDiv");
    var lgcorpcode = GlobalVars.lgcorpcode;
    var lguserid = GlobalVars.lguserid;
    //操作员树
    setting2.asyncUrl = htmName+"?method=createUserTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
    //机构树
    setting3.asyncUrl = htmName+"?method=createDeptTree&lguserid="+lguserid;
    setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);
    zTree2.expandAll(true);
    reloadTree(zNodes3);
    //参数是要隐藏的下拉框的div的id数组，
    closeTreeFun(["dropMenu2","dropMenu"]);
});

// 加载人员/机构树形控件
function reloadTree(zNodes3) {
    $("#dropMenu2").hide();
    $("#dropMenu").hide();
    setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
    zTree3.expandAll(true);
}

//获取机构
setting3 = {
    async : true,
    asyncUrl : htmName+"?method=createDeptTree", //获取节点数据的URL地址
    isSimpleData : true,
    rootPID : 0,
    treeNodeKey : "id",
    treeNodeParentKey : "pId",
    asyncParam: ["depId"],
    callback: {
        click: showDepName,
        asyncSuccess:function(event, treeId, treeNode, msg){
            if(!treeNode){
                var rootNode = zTree3.getNodeByParam("level", 0);
                zTree3.expandNode(rootNode, true, false);
            }
        }
    }
};

//获取操作员
setting2 = {
    checkable : true,
    checkStyle : "checkbox",
    checkType : { "Y": "s", "N": "s" },
    async : true,
    asyncUrl : htmName+"?method=createUserTree2", //获取节点数据的URL地址
    isSimpleData: true,
    rootPID : 0,
    treeNodeKey: "id",
    treeNodeParentKey: "pId",
    asyncParam: ["depId"],
    callback: {
        change: showUserName,
        asyncSuccess:function(event, treeId, treeNode, msg){
            if(!treeNode){
                var rootNode = zTree2.getNodeByParam("level", 0);
                zTree2.expandNode(rootNode, true, false);
            }
        }
    }
};

//人员树形控件
function showUserMenu() {
    $("#dropMenu").hide();
    $("#dropMenu2").toggle();
}

//机构树形控件
function showDepMenu() {
    $("#dropMenu").toggle();
    $("#dropMenu2").hide();
}

//机构->点击确定
function zTreeDepOnClickOK() {
    $("#dropMenu").hide();
}

//机构->点击清空
function zTreeDepOnClickClean() {
    var checkNodes = zTree3.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
        checkNodes[i].checked=false;
    }
    zTree3.refresh();
    $("#depNam").attr("value", getJsLocaleMessage('common','common_pleaseSelect'));
    $("#deptid").attr("value","");
}

//操作员->点击确定
function zTreeUserOnClickOK() {
    $("#dropMenu2").hide();
    var zTreeNodes2=zTree2.getChangeCheckedNodes();
    var pops="";
    var userids ="";
    for(var i=0; i<zTreeNodes2.length; i++){
        pops+=zTreeNodes2[i].name+";";
        userids+=zTreeNodes2[i].id.replace("u","")+",";
    }
    $("#userName").attr("value", pops);
    $("#userid").attr("value",userids);
    if(zTreeNodes2.length === 0){
        $("#userid").attr("value","");
        $("#userName").attr("value", getJsLocaleMessage('dxzs','dxzs_ssend_alert_159'));
    }
}

//操作员->点击清空
function zTreeUserOnClickClean() {
    var checkNodes = zTree2.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
        checkNodes[i].checked=false;
    }
    zTree2.refresh();
    $("#userid").val("");
    var text = getJsLocaleMessage('common', 'common_pleaseSelect');
    $("#userName").val(text);
}

//选中的机构显示文本框
function showDepName(event, treeId, treeNode) {
    if (treeNode) {
        $("#depNam").attr("value", treeNode.name);
        $("#deptid").attr("value",treeNode.id);
    }
}
//选中的人员显示文本框
function showUserName(event, treeId, treeNode) {
    if (treeNode) {
        var zTreeNodes2=zTree2.getChangeCheckedNodes();
        var pops="";
        var userids = "";
        for(var i=0; i<zTreeNodes2.length; i++){
            pops+=zTreeNodes2[i].name+";";
            userids+=zTreeNodes2[i].id.replace("u","")+",";
        }
        $("#userName").attr("value", pops);
        $("#userid").attr("value",userids);
        if(zTreeNodes2.length===0){
            $("#userid").attr("value","");
            $("#userName").attr("value", "");
        }
    }
}
function zTreeBeforeAsync(treeId, treeNode) {
    return treeNode.id !== 1;
}

//关闭树的下拉框方法（点击别处时关闭）
function closeTreeFun(ids) {
    for(var i=0;i<ids.length;i++) {
        $("#"+ids[i]).click(function(e){
            e.stopPropagation();
        });
    }
    $('html,body').click(function(e){
        var $obj=$(e.target);
        /*if($obj.attr("class").indexOf("treeInput")===-1){*/
        if($obj.prop("class").indexOf("treeInput")===-1){
            for(var i=0;i<ids.length;i++) {
                $("#"+ids[i]).css("display","none");
            }
        }
    });
}

//结束时间设置
function rtime() {
    var min = "1900-01-01 00:00:00";
    var max = "2099-12-31 23:59:59";
    var sendtime = $("#sendtime").val();
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:sendtime||min,maxDate:max,enableInputMask:false});
}

//起始时间设置
function stime(){
    var nowDate = new Date();
    var max = nowDate.Format("yyyy-MM-dd HH:mm:ss");
    var recvtime = $("#recvtime").val();
    var min = "1900-01-01 00:00:00";
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:recvtime||max,enableInputMask:false});
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
Date.prototype.Format = function (fmt) {
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
function showTitle(obj) {
    var $title = $(obj);
    var _title = getJsLocaleMessage("rms","rms_taskrecord_sendtopic");
    var html = '<div id="showTitle" title="'+ _title + '">';
    html += "<span style='font-size: 18px;'>"+ $title.attr("title") +"</span>";
    html += '</div>';
    console.log(html);
    //$('#showTitle').html();
    layer.open({
        type: 1,
        title:getJsLocaleMessage("rms","rms_sendtopic"),
        maxmin: true,
        shadeClose: true, //点击遮罩关闭层
        area : ['300px' , '300px'],
        content: html
    });
}
function downRptExcel(countSize,totalRec,menuName) {
    if(confirm(getJsLocaleMessage("rms","rms_visireport_confirmexp"))) {
        if(parseInt(totalRec) > 500000){
            alert(getJsLocaleMessage("rms","rms_rltcreport_exp50w"));
            return;
        }
        if(parseInt(countSize) > 0){
            $.ajax({
                type: "POST",
                url: "rms_rmsTaskHistory.htm?method=getRptExcel",
                data: {
                    menuName:menuName
                },
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
//-------------模板预览---------------
function tempPreview(tmId) {
    $("#tempView").dialog({
        modal:true,
        title:"预览",
        closeOnEscape: true,
        height:660,
        width:360
    });
    $("#ui-dialog-title-tempView").parent().parent().css("border-radius","10px");
    $.post("rms_rmsTaskHistory.htm?method=tempPreview",
        {
            tmId:tmId
        },
        function(result){
            if (result != null && result !== "null" && result !== "") {
                $("#cust_preview").html(result);
            } else {
                alert("内容文件不存在，无法预览！");
                $("#cust_preview").html("");
            }
        });
    $("#tempView").dialog("open");
}
function reSend(taskId)
{
    if(confirm(getJsLocaleMessage('rms','rms_mbgl_sureToResubmit')))
    {
        $("a[name='rsend']").attr("disabled","disabled");
        $.ajax({
            url:"rms_rmsSameMms.htm?method=rmsReSend",
            method:"POST",
            data:{
                taskId : taskId
            },
            beforeSend:function(){
                page_loading();
            },
            success:function(result) {
                page_complete();
                if(result.indexOf("sendSuccess") === 0 || result === "timerSuccess") {
                    //alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_5'));
                    alert(getJsLocaleMessage('rms','rms_mbgl_reissuesuccess'));
                }else if(result==="sendFail" || result === "reSendFail" || result === "timerFail") {
                    //alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_260'));
                    alert(getJsLocaleMessage('rms','rms_mbgl_reissuefail'));
                }else if(result.indexOf("empex") === 0) {
                    var errorCode = result.substr(result.indexOf("["));
                    alert(getJsLocaleMessage('rms','rms_mbgl_reissuefail') + errorCode + "。");
                }else if(result==="hasResend") {
                    //alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_8'));
                    alert(getJsLocaleMessage('rms','rms_mbgl_noreissueagain'));
                }else if(result.indexOf("sendFail&") === 0) {
                    alert(getJsLocaleMessage('rms','rms_mbgl_reissuefail') + "[" + result.substr(result.indexOf("&") + 1) + "]");
                }else {
                    alert(getJsLocaleMessage('rms','rms_mbgl_reissuefail'));
                }
                $("a[name='rsend']").attr("disabled","");
                document.pageForm.submit();
            }
        });
    }
}
//撤销任务
function cancelTimer(taskId) {
    var lgcorpcode =$("#lgcorpcode").val();
    var lguserid =$("#lguserid").val();
    // if(confirm("是否撤销该任务？")){
    if(confirm(getJsLocaleMessage('rms','rms_mbgl_canceltask'))){
        $.ajax({
            url:"rms_rmsSameMms.htm?method=changeTimerState",
            method:"POST",
            data:{
                taskId:taskId,
                lgcorpcode:lgcorpcode,
                lguserid:lguserid
            },
            beforeSend:function(){
                $('#load-bg').show();
            },
            success:function(result) {
                $('#load-bg').hide();
                if(result === "cancelSuccess") {
                    //alert("撤销短信任务成功！");
                    alert(getJsLocaleMessage('rms','rms_mbgl_cancelmessage'));
                    location.reload();
                }else if(result === "timeError"){
                    //alert("不允许撤销距发送时间只有一分钟的定时任务。");
                    alert(getJsLocaleMessage('rms','rms_mbgl_nocancel1mintask'));
                }else if(result === "cancelFail"){
                    //alert("定时任务撤销失败,请重试。");
                    alert(getJsLocaleMessage('rms','rms_mbgl_canceltaskfail'));
                }else if(result === "hasCanceled"){
                    //alert("该定时任务已经撤销，不允许重复操作。");
                    alert(getJsLocaleMessage('rms','rms_mbgl_nocanceltaskagain'));
                }else{
                    //alert("定时任务撤销失败,请重试。");
                    alert(getJsLocaleMessage('rms','rms_mbgl_canceltaskfail'));
                }
            }
        });
    }
}