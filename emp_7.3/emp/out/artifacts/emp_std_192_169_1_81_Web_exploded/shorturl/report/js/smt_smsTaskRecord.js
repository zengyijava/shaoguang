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
	$("#detailDialog").dialog({
		autoOpen: false,
		height:550,
		width: "zh_HK"===getJsLocaleMessage("common","common_empLangName")?670:650,
		modal: true,
		resizable:false,
		close:function(){
		}
	});
});

function modify(t,title) {
    $('#modify').dialog({
        title:title,
        autoOpen: false,
        width:300,
        height:300
    });
	$("#msgcont").empty();

	$("#msgcont").html($(t).children("textarea").val());

	$('#modify').dialog('open');
}

function reSend(mtid,msType)
{
    if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_254')))
    {
    	var smsAccount=$("#smsAccount").val();
	    $("a[name='rsend']").attr("disabled","disabled");
	    var lgcorpcode =$("#lgcorpcode").val();
		var lguserid =$("#lguserid").val();
		var lgusername =$("#lgusername").val();
		var lgguid =$("#lgguid").val();
		if(msType == 5){// 移动财务短信
			$.post("ycw_electronicPayroll.htm?method=retry", {
					mtid : mtid,
					lgcorpcode : lgcorpcode,
					lguserid : lguserid,
					lgusername:lgusername,
					lgguid:lgguid
				},
					function(result) {
	                    if(result=="success")
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_255'));
						}else if(result=="error")
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_8'));
						}else if(result=="noMoney")
						{
						    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_256'));
						}else if(result=="payFailure")
						{
						    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_12'));
						}
						else if(result=="nofindfile")
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_257'));
						}
						else if(result=="isretry")
					    {
					       alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_258'));
					    }
						else if(result=="nospnumber")
						{
					        alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_9')+smsAccount+getJsLocaleMessage('dxzs','dxzs_ssend_alert_10'));
				        }else if(result == "noSubNo"){
				        	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_91'));
				        }else if(result == "noUsedSubNo"){
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_92'));
						}else
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_18')+result);
						}
						 $("a[name='rsend']").attr("disabled","");
						document.pageForm.submit();
				});
		}
		else
		{
		    $.post("smt_smsTaskRecord.htm?method=reSendSMS", {
				mtid : mtid,
				lgcorpcode : lgcorpcode,
				lguserid : lguserid,
				lgusername:lgusername,
				lgguid:lgguid
			},
				function(result) {
	                if(result=="createSuccess")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_5'));
					}else if(result=="000")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_260'));
					}else if(result=="saveSuccess")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_7'));
					}else if(result=="error")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_8'));
					}else if(result=="nomoney")
					{
					    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_256'));
					}else if(result=="false")
					{
					    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_12'));
					}
					else if(result=="nofindfile")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_261'));
					}
					else if(result=="isretry")
					{
					    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_258'));
					}
					else if(result=="uploadFileFail")
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_4'));
					}
					else if(result=="nospnumber")
				    {
					    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_9')+smsAccount+getJsLocaleMessage('dxzs','dxzs_ssend_alert_10'));
				    }else if(result == "noSubNo"){
			        	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_91'));
			        }else if(result == "noUsedSubNo"){
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_92'));
					}
					else
					{
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_18')+result);
					}
					//window.location.href = window.location.href;
					 $("a[name='rsend']").attr("disabled","");
					document.pageForm.submit();
			});
		}
	}
}

function replysearch(pageIndex, mtId, corpCode)
{
	var replyName = $("#replyName").val();
	var replyMoblie = $("#replyMoblie").val();
	var replyContent = $("#replyContent").val();
   	$.post(
   	"smt_smsTaskRecord.htm",
   	{method:"getReplyDetail",
	mtId:mtId,
   	pageIndex:pageIndex,
   	lgcorpcode:corpCode,
   	replyName:replyName,
   	replyMoblie:replyMoblie,
   	replyContent:replyContent},
   	function(data){replyshow(pageIndex,data, corpCode);});
}

function exportReplyInfo(mtId, corpCode, recordCount, count)
{
	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210')))
	{
		if(count > 0)
		{
			if(recordCount <= 500000)
			{
				var replyName = $("#replyName").val();
				var replyMoblie = $("#replyMoblie").val();
				var replyContent = $("#replyContent").val();
				var lguserid = $("#lguserid").val();
				$.ajax({
					type: "POST",
					url: "smt_smsTaskRecord.htm?method=ReportReplyExcel",
					data: {mtId:mtId,
							lgcorpcode:corpCode,
							lguserid:lguserid,
							replyName:replyName,
							replyMoblie:replyMoblie,
							replyContent:replyContent
						  },
	                beforeSend:function () {
						page_loading();
	                },
	                complete:function () {
				    	page_complete();
	                },
					success: function(result){
	                        if(result=='true'){
	                           download_href("smt_smsTaskRecord.htm?method=downloadFile&exporttype=smstaskreply_export");
	                        }else{
	                            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
	                        }
		   			}
				});	
			}
			else
			{
				 alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_272'));
			}
		}
		else
		{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_212'));
		}
	}
}

function deleteleftline2()
{
	$('#ccontent1 table tbody tr:last').addClass('tbody_last');
	$('#ccontent1 > table > tbody > tr').each(function(){$(this).find('td:last').addClass('no_r_b');}); 
	$('#ccontent1 > table > tbody > tr').each(function(){$(this).find('td:first').addClass('no_l_b');});
	$('#ccontent1 table th:first').addClass('th_l_b');
	$('#ccontent1 table th:last').addClass('th_r_b');	
}

function stopTask(mtid)
{
	var htm = $("#htmName").val();
    if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_273')))
    {
		$.post(htm+"?method=stopTask", {
			mtId : mtid
		},
		function(result) {
               if(result=="true")
               {
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_274'));
            }else if(result=="false"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_275'));
            }else if(result=="AllSended")
            {
            	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_276'));
	        }
			document.pageForm.submit();
		});
    }	
}

function changeHisInfo(taskId){
    page_loading();
    var path = $("#path").val();
    var lgguid = $("#lgguid").val();
    //记录当前页的分页信息
    var pageSize = $("#pageSize").val();
    var pageIndex = $("#txtPage").val();
    var totalPage = $("#totalPage").val();
    var totalRec = $("#totalRec").val();
    window.location.href = path+"/surlBatchRecord.htm?method=findAllSendDetail" + "&taskId="+taskId+ "&lgguid="+lgguid+ "&pageIndex="+pageIndex+ "&totalPage="+totalPage+ "&totalRec="+totalRec+ "&pageSize="+pageSize;
}

//回复详情
function getReplyDetail(taskId){
    page_loading();
    var path = $("#path").val();
    var lgguid = $("#lgguid").val();
    //记录当前页的分页信息
    var pageSize = $("#pageSize").val();
    var pageIndex = $("#txtPage").val();
    var totalPage = $("#totalPage").val();
    var totalRec = $("#totalRec").val();
    window.location.href = path+"/surlBatchRecord.htm?method=getReplyDetail" + "&taskId="+taskId+ "&lgguid="+lgguid+ "&pageIndex="+pageIndex+ "&totalPage="+totalPage+ "&totalRec="+totalRec+ "&pageSize="+pageSize;
}