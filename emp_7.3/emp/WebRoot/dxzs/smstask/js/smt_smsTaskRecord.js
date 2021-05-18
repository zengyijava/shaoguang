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
		width: "zh_HK"===getJsLocaleMessage("common","common_empLangName")?750:750,
		modal: true,
		resizable:false,
		close:function(){
		}
	});
	
	$('#modify').dialog({
		autoOpen: false,
		width:300,
	    height:300
	});
	
});

/*
* 群发历史查询任务汇总
* */
function task(taskId,mtid) {
	{
        if(confirm("确定进行汇总吗？"))
        {
            $("a[name='task']").attr("disabled","disabled");
            var lgcorpcode =$("#lgcorpcode").val();
            var lguserid =$("#lguserid").val();
            var lgusername =$("#lgusername").val();
            var lgguid =$("#lgguid").val();
            $.post("smt_smsTaskRecord.htm?method=summaryByTaskid", {
                    mtid : mtid,
                    taskId : taskId,
                    lgcorpcode : lgcorpcode,
                    lguserid : lguserid,
                    lgusername:lgusername,
                    lgguid:lgguid
                },
                function(result) {
                    if(result != null && result != "" && result.indexOf("true") > -1)
                    {
                        alert("汇总成功！任务批次号为：" + result.substr(4,8));
                        $("a[name='task']").attr("disabled","");
                        document.pageForm.submit();
                    } else if (result != null && result != "" && result.indexOf("executing") > -1){
                        alert("汇总中，请不要点击汇总按钮！任务批次号为：" + result.substr(10,8));
                    }else {
                        alert("汇总失败！任务批次号为：" + result.substr(6,8));
                    }
                });
		}
	}
}


function modify(t)
{
	$("#msgcont").empty();
		//用label显示短信内容
	//$("#msgcont").text($(t).children("label").children("xmp").text());
	//修改成用textarea显示短信内容
	$("#msgcont").val($(t).children("textarea").val());
	$('#modify').dialog('open');
}

function modifyNew(t)
{
    $("#msgcont").empty();
    //用label显示短信内容
    //$("#msgcont").text($(t).children("label").children("xmp").text());
    //修改成用textarea显示短信内容
    // $("#msgcont").text($("#msgXmp").text());
	// 参考群发任务查看（修改依次查看多个发送内容，发送内容都是第一次查看的内容）
	$("#msgcont").text($(t).children("xmp[name='msgXmp']").text());
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

//回复详情
function searchNoticeDetail(pageIndex,mtId,corpCode){
	 $.post("smt_smsTaskRecord.htm?method=getReplyDetail&mtId="+mtId+"&pageIndex="+pageIndex+"&lgcorpcode="+corpCode,null,function call(data){ 
	 	replyshow(pageIndex, data, corpCode);
   });
}

function replyshow(pageIndex,data, corpCode)
{
	if(data!="error"&&data!="noList")
		{
	  		var member = eval("("+data+")");
	   		var moblie;
	   		var time;
	   		var content;
	   		var name;
	       	var pageMsg = "";
	       	$("#detailDialog").text("");
	       	$("#exportCondition").unbind("click");
 			$("#search1").unbind("click");
	       	var maxSendCount = member.maxSendCount;
	       	var sendInterval = member.sendInterval;
	       	var sendContent = member.sendContent;
	       	var arySendCount = member.arySendCount;
	       	var recordCount = member.count; 
	       	var pageSize = member.pageSize;
	       	var mtId = member.preNoticeId;
	       	var index = member.index;
			var replyName = member.replyName;
			var replyMoblie= member.replyMoblie;
			var replyContent = member.replyContent;
			if(replyName == null)
			{
				replyName = "";
			}
			if(replyMoblie == null)
			{
				replyMoblie = "";
			}
			if(replyContent == null)
			{
				replyContent = "";
			}
			var title = "";
			var condition = "";
			var condition = "<div class='buttons' style='height:40px;''><a id='exportCondition' class='replyExport' style='margin: 6px 5px 2px 10px;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_304')+"</a></div><div id='condition' style='width: 740px;'>" +
					"<table style='margin-left: 2px;margin-right: 2px;'><tbody>" +
					"<tr><td style='padding-right: 10px!important;width: 40px!important;padding-left: 24px;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_188')+"：</td><td style='padding-right: 20px!important;'><input type='text' id='replyName' name='replyName' value='"+replyName+"' style='width: 100px!important;'></td>" +
					"<td style='padding-right: 10px!important;width: 60px!important;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_64')+"：</td><td style='padding-right: 20px!important;'><input type='text' id='replyMoblie' name='replyMoblie' value='"+replyMoblie+"' style='width: 100px!important;'></td>" +
					"<td style='padding-right: 10px!important;width: 40px!important;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_262')+"：</td><td style='padding-right: 20px!important;'><input type='text' id='replyContent' name ='replyContent' value='"+replyContent+"' style='width: 100px!important;'></td>" +
					"<td class='tdSer'><center><a id='search' class='replySearch'><font style=\"text-align: center;margin-left: 21px;color:#000;line-height:24px;\">"+getJsLocaleMessage("common","common_query")+"</font></a></center></td></tr></tbody></table></div>";

			var msg = "<br><center id='ccontent1'><table id='content' style='width:99%;' >"                 
					+ "<thead> <tr>  <th align='center'  width='20%'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_64')+"</th><th align='center'  width='15%'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_188')+"</th><th align='center' width='40%'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_263')+"</th> <th align='center' width='25%'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_264')+"</th> </tr> </thead>";

			if(member.jobs.length>0)
			{
				for(var i= 0;i<member.jobs.length;i++)
				{
        			moblie =  member.jobs[i].moblie;
        			name = member.jobs[i].name;
        			content =  member.jobs[i].content;
        			time =  member.jobs[i].time;
	        		msg = msg +" <tr> <td width='20%' align='center'>"+moblie+"</td> <td width='15%' align='center'>"+name+"</td>"
	        		+"  <td align='center' width='40%' style='word-break : break-all;'>"+content.replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"          
			        +"  <td align='center' width='25%'>"+time+"</td>";
		         }
				
        		if(recordCount%pageSize == 0){
        			pageCount = recordCount/pageSize;
        		}else{
        			pageCount = parseInt(recordCount/pageSize) + 1;
        		}
        		if(pageCount == 0){
        			pageCount = 1;
        		}
        		var start = 1;
        		var end = pageCount; 
        		if(pageCount>5){
        			start = pageIndex - 2;
        			if(start <= 0){
        				start = 1;
        			}
        			end = parseInt(pageIndex) + 2;
        			if(end > pageCount){
        				end = pageCount;
        			}
        		}  
        		if(pageIndex != 1){
        			pageMsg = pageMsg +"<a href='javascript:replysearch("+(pageIndex-1)+","+mtId+","+corpCode+")' style='color:#2a6fbe;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_265')+" </a>&nbsp;&nbsp;";
        		}
        		if(end - 5 >= 1){
        			pageMsg = pageMsg + "<a href='javascript:replysearch(1,"+mtId+","+corpCode+")' style='color:#2a6fbe;'>"+1+"</a>&nbsp;&nbsp;";
        		}
        		if(start >= 3 ){
        			pageMsg = pageMsg +"<span>...</span>";
        		}
        		for(var i=start ; i <= end ; i++){
        			if(i == pageIndex){
        				pageMsg = pageMsg + "<span class='current'>"+i+"</span>&nbsp;&nbsp;";
        			}else{
        				pageMsg = pageMsg + "<a href='javascript:replysearch("+i+","+mtId+","+corpCode+")' style='color:#2a6fbe;'>"+i+"</a>&nbsp;&nbsp;";
        			}
        		}
        		if(pageCount - end >= 2 ){
        			pageMsg = pageMsg + "<span>...</span>";
        		}
        		if(end < pageCount ){
        			pageMsg = pageMsg + "<a href='javascript:replysearch("+pageCount+","+mtId+","+corpCode+")' style='color:#2a6fbe;'>"+pageCount+"</a>&nbsp;&nbsp;";
        		}
        		if(pageIndex != pageCount){
        			pageMsg = pageMsg + "<a href='javascript:replysearch("+(pageIndex+1)+","+mtId+","+corpCode+")' style='color:#2a6fbe;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_266')+"</a>";
        		}
        		pageMsg = pageMsg + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_267')+recordCount+getJsLocaleMessage('dxzs','dxzs_ssend_alert_268');
			}
			else
			{
				msg =msg + " <tr><td align='center' colspan='7'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_234')+"</td></tr>";
			}
			var foot = "<div>&nbsp;</div>";
			$("#detailDialog").append(title+condition+msg+"</table></center><br><div style='text-align:center';>"+pageMsg+"</div>"+foot); 
			$(".btnClass1").hover(function() {
				var img=$(this).css('background-image');
				$(this).css('background-image',img.replace('but-bg1','but-bg2'))
			}, function() {
				var img=$(this).css('background-image');
				$(this).css('background-image',img.replace('but-bg2','but-bg1'))
			});
			$("#detailDialog").dialog("option","title", getJsLocaleMessage('dxzs','dxzs_ssend_alert_269'));
 			$("#detailDialog").dialog("open");
 			$(".replyExport").bind("click", function(){exportReplyInfo(mtId, corpCode, recordCount, member.jobs.length);});
 			$(".replySearch").bind("click", function(){replysearch(1, mtId, corpCode);});
 			deleteleftline2();
		}
		else if(data=="error"){
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_270'));
		}
		else {
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_271'));
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