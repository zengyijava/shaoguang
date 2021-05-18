$(document).ready(function() {
    $("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
	}, function() {
		$(this).removeClass("hoverColor");
	});		
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$('#search').click(function(){submitForm();});
});

//回复详情
function searchNoticeDetail(pageIndex,mtId,corpCode){
	 $.post("smt_smsTaskRecord.htm?method=getReplyDetail&mtId="+mtId+"&pageIndex="+pageIndex+"&lgcorpcode="+corpCode,null,function call(data){ 
	 	replyshow(pageIndex, data, corpCode);
   });
}

function rtime()
{
    var max = "2099-12-31 23:59:59";
    var v = $("#addtimeEnd").attr("value");
	//if(v.length != 0)
	//{
	//    var year = v.substring(0,4);
	//	var mon = v.substring(5,7);
	//	var day = 31;
	//	if (mon != "12")
	//	{
	//	    mon = String(parseInt(mon,10)+1);
	//	    if (mon.length == 1)
	//	    {
	//	        mon = "0"+mon;
	//	    }
	//	    switch(mon){
	//	    case "01":day = 31;break;
	//	    case "02":day = 28;break;
	//	    case "03":day = 31;break;
	//	    case "04":day = 30;break;
	//	    case "05":day = 31;break;
	//	    case "06":day = 30;break;
	//	    case "07":day = 31;break;
	//	    case "08":day = 31;break;
	//	    case "09":day = 30;break;
	//	    case "10":day = 31;break;
	//	    case "11":day = 30;break;
	//	    }
	//	}
	//	else
	//	{
	//	    year = String((parseInt(year,10)+1));
	//	    mon = "01";
	//	}
	//	max = year+"-"+mon+"-"+day+" 23:59:59"
    //}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
}

function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#addtimeStart").attr("value");
    var min = "1900-01-01 00:00:00";
	//if(v.length != 0)
	//{
	//    max = v;
	//    var year = v.substring(0,4);
	//	var mon = v.substring(5,7);
	//	if (mon != "01")
	//	{
	//	    mon = String(parseInt(mon,10)-1);
	//	    if (mon.length == 1)
	//	    {
	//	        mon = "0"+mon;
	//	    }
	//	}
	//	else
	//	{
	//	    year = String((parseInt(year,10)-1));
	//	    mon = "12";
	//	}
	//	min = year+"-"+mon+"-01 00:00:00"
	//}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
}

/**
 * 跳转发送
 */
function toSendMarathon(taskId, corpCode) {
    layer.confirm('确认立即发送？', {
        btn: ['确认', '取消'], //按钮
        offset: ['35%', '40%']
    }, function () {
        var index;
        //调用ajax
        $.ajax({
            //提交数据的类型 POST GET
            type: "POST",
            //提交的网址
            url: "importTemplateDetails.htm?method=smsSend",
            //提交的数据
            data: {
                batch: taskId,
                corp: corpCode
            }, beforeSend: function () {
                    index = layer.load(1, {
                    shade: [0.1, '#fff'], //0.1透明度的白色背景
                    offset: ['50%', '50%']
                });
            }, success: function (msg) {
                layer.close(index);
                if (msg.indexOf("html") > 0) {
                    alertTips(getJsLocaleMessage('rms', 'rms_report_connectsvrfail09'), 2);
                }
                if (msg.indexOf("empex") === 0) {
                    var errorCode = msg.substr(msg.indexOf("["));
                    alertTips(getJsLocaleMessage('rms', 'rms_report_fuxinsendfailure')+ errorCode + "。", 2);
                }
                if (msg.indexOf("sendSuccess") === 0) {
                    alertTips(getJsLocaleMessage('rms', 'rms_report_fuxincreateok'), 1);
                }
                if (msg.indexOf("sendFail") === 0) {
                    if (msg.indexOf("&") > -1) {
                        alertTips(getJsLocaleMessage('rms', 'rms_report_fuxinsendfailure') + "[" + msg.substr(msg.indexOf("&") + 1) + "]。", 2);
                    } else {
                        alertTips(getJsLocaleMessage('rms', 'rms_report_fuxinsendfailure'), 2);
                    }
                }
                if(msg === "noAuditTemplate"){
                    alertTips(getJsLocaleMessage('rms', 'rms_report_noAuditTemplate'), 2);
                }
                if(msg === "" || msg === "error"){
                    alertTips(getJsLocaleMessage('rms', 'rms_report_fuxinsendfailure'), 2);
                }
            }
        });
    });
}

function alertTips(msg, flag) {
    layer.msg(msg, {icon: flag,time:1500,offset: ['35%', '40%']},function() {
        window.location.reload();
    });
}
var plExport;
function plExportCondition() {
     plExport = layer.open({
        type: 1,
        title: "导入模板",
        closeBtn: 0, //不显示关闭按钮
        shade: [0],
        area: ['450px', '450px'],
        offset: ['25%', '35%'], //右下角弹出
        anim: 2,
        content: $('#tempDiv') //iframe的url，no代表不显示滚动条
    });
}

function xzExportCondition() {
    layer.confirm('确认下载模板？', {
        btn: ['确认', '取消'], //按钮
        offset: ['35%', '40%']
    }, function () {
        $.ajax({
            type: "GET",
            url: "importTemplate.htm?method=smsReportAllExcel",
            data: {
                yangli:1
            },
            beforeSend:function() {
                page_loading();
            },
            complete:function () {
                layer.closeAll();
                page_complete();
            },
            success:function(result){
                var jsonObject = JSON.parse(result);
                var status = jsonObject.status;
                var data = jsonObject.data;
                var src = data.src;
                if(status==true){
                    download_href("importTemplate.htm?method=downloadFile&data="+src);
                }else{
                    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
                }
            }
        });
    });
}


function tempCancel() {
    $("#zhuti").val("");
    $("#numFile").val("");
    layer.closeAll();
}

function tempSure() {
    var $fo = $("#tempFrame").contents();
    var corp = $fo.find("#corp").val();
    var zhuti = $fo.find("#zhuti").val();
    var numFile = $fo.find("#numFile");
    var fileName = numFile.val();
    console.log(numFile+"  111");
    var path = $("#path").val();

    /*if(corp == undefined||corp =='请选择')
    {
        alert('请选择企业');
        return;
    }*/
    if(zhuti == undefined||zhuti=="") {
        alert('请输入富信主题');
        return;
    }
    if(fileName == undefined||fileName=="") {
        alert('请选择文件');
        return;
    }
    var form1 = $fo.find("#pageForm");
    $(form1).submit();
    //"已提交处理，请耐心等待!";
    setTimeout(function() {
//        var tempHtml=$("#tempFrame").contents().find("body").text();
//        //alert(tempHtml);
//        if(tempHtml === "已提交处理，请耐心等待!"){
//            //alert("模板数据导入成功!请查看数据");
//        }else{
//            //alert("请按照规定重新上传模板");
//        }
        layer.close(plExport);
    },3000);
}



