$(document).ready(function() {
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

$('#search').click(function(){

		var val = $("#pcid").val();
		if(isNaN(val)&&val!=""){
			alert("文件批次："+val +"不是数字");
			return;
		}
		submitForm();});

	$('#modify').dialog({
		autoOpen: false,
		width:300,
	    height:300
	});

	//短信任务详细信息弹出框
	$("#tempDiv").dialog({
		autoOpen: false,
		modal:true,
		title:'上传文件',
		width:680,
		height: 'auto',
		minHeight:170,
		maxHeight:650,
		closeOnEscape: false,
		resizable:false,
		open:function(){
		},
		close:function(){
		}
	});


	//短信任务详细信息弹出框
	$("#smsdetailinfo").dialog({
		autoOpen: false,
		modal:true,
		title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_215'),
		width:680,
		height: 'auto',
		minHeight:170,
		maxHeight:650,
		closeOnEscape: false,
		resizable:false,
		open:function(){
		},
		close:function(){
		}
	});
	//短信任务详细信息弹出框
	$("#reviewflowinfo").dialog({
		autoOpen: false,
		modal:true,
		title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_216'),
		width:880,
		height: 'auto',
		minHeight:170,
		maxHeight:750,
		closeOnEscape: false,
		resizable:false,
		open:function(){
		},
		close:function(){
		}
	});
});


function rtime()
{
    var max = "2099-12-31 23:59:59";
    var v = $("#sendtime").attr("value");
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
    var v = $("#recvtime").attr("value");
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

function changeFileStatus(id,fileStatus){
	var path = $("#path").val();

	debugger;
	var fileStatus1 = "fileStatus"+id;
	var fileStatusVal =  $("#" + fileStatus1 + "").html();
	if(fileStatusVal=="已启用"){
		fileStatus = 1;
	}else{
		fileStatus = 2;
	}
	$.ajax({
		type: "POST",
		url: path+"/ssm_dxzsBigFileExport.htm?method=changeFileStatus",
		data: {
			id:id,
			fileStatus:fileStatus
				},
		success: function(result){
	           $("#" + fileStatus1 + "").html(result);
			}
	});
}

function tempCancel()
{
	page_complete();
	$("#tempDiv").dialog("close");
}

function tempSure() {

    var $fo = $("#tempFrame").contents();
    var creFileName = $fo.find("#creFileName").val();
    var busId = $fo.find("#busId").val();

    var numFile1 ="";
    $fo.find("#filesdiv input[type='file']").each(function(){
    	numFile1+=$(this).val();
    });
    var path = $("#path").val();

    if(creFileName == undefined||creFileName=="") {
        alert('请输入文件名称');
        return;
    }

    if(numFile1 == undefined||numFile1=="") {
        alert('请上传文件');
        return;
    }

    if(busId == undefined||busId=="") {
        alert('请输入业务编码');
        return;
    }

    $("#subSend").attr("disabled","disabled");

    var form1 = $fo.find("#pageForm");
    var maxsize = 1000*1024*1024;
    //var minsize = 50*1024*1024;
    var filesize = $fo.find("#upResult").val();
//    if(filesize < minsize){
//   	 alert("上传文件小于50M");
//   	$("#subSend").attr("disabled","");
//   	 return ;
//   }
    if(filesize>maxsize ){
    	 alert("上传文件大于1000M");
    	 $("#subSend").attr("disabled","");
    	 return ;
    }
    page_loading();
    $(form1).submit();


    //"已提交处理，请耐心等待!";
   // setTimeout(function() {
    	//$("#tempDiv").dialog("close");
    //},3000);
}

/*
*将字节进行转换
*/
function bytesToSize(bytes) {
    if (bytes === 0) return '0 B';
    var k = 1024,
        sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));

   return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}

//删除该行文件
function ddll(idd) {
	if (confirm("是否要删除该文件")) {
			$("#" + idd + "").remove();
			var filename = idd.replace("tr", "numFile");
			var curSize = $("#" + filename)[0].files[0].size;
			sumSize = sumSize - curSize;
			$("#upResult").val(sumSize);
			$("#sumsize").html(bytesToSize(sumSize));
			$("#" + filename).remove();

		}
}

function deleteSum(idd) {
	if (confirm("是否要删除所有文件")) {
			var str1 = "<input id='numFile1' name='numFile1'  type='file' onchange='addFiles()' class='numfileclass' style='display:none;'/>	";
			$("#filesdiv").html(str1);
			$("#vss").html("");
			$("#sumsize").html("0");
			fileCount = 1;
			sumSize = 0;
			$("#fileBind").attr("for","numFile"+fileCount);
			$("#upResult").val(sumSize);
		}
}

function modify(t)
{
    $('#modify').dialog({
        autoOpen: false,
        width:250,
        height:200
    });
    $("#msgss").empty();
    $("#msgss").text($(t).children("label").children("xmp").text());
    $('#modify').dialog('open');
}

function modifyClose(t)
{
    $('#modify').dialog({
        autoOpen: false,
        width:250,
        height:200
    });
    $("#msgss").empty();
    //$("#msgss").text($(t).children("label").children("xmp").text());
    $('#modify').dialog('close');
}