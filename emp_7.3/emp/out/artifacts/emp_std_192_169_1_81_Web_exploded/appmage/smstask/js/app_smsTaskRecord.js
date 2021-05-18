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
		height:450,
		width: 650,
		modal: true,
		resizable:false,
		close:function(){
		}
	});
	
	$('#modify').dialog({
		autoOpen: false,
		width:250,
	    height:200
	});
	
});

function modify(t)
{
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
}

function reSend(mtid,msType)
{
    if(confirm(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_resubmit')))
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
							// alert("短信任务发送成功！");
	                    	alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_sendsuc'));
						}else if(result=="error")
						{
							//alert("请求响应超时，创建短信任务失败！");
							alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_timeout'));
						}else if(result=="noMoney")
						{
						    //alert("余额不足,创建短信任务失败！");
							alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_yebzrwsb'));
						}else if(result=="payFailure")
						{
						    //alert("创建短信任务时,修改计费信息失败！");
							alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_jfsb'));
						}
						else if(result=="nofindfile")
						{
							//alert("文件不存在,不允许进行失败重发操作！");
							alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_fileisnull'));
						}
						else if(result=="isretry")
					    {
					       //alert("已进行了失败重发操作,不允许再进行此操作!");
							alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_notresend'));
					    }
						else if(result=="nospnumber")
						{
					        //alert("发送失败，"+smsAccount+"未设置尾号！");
							alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_sendfalse') + smsAccount + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_noweihao'));
				        }else if(result == "noSubNo"){
				        	//alert("拓展尾号获取失败！");
				        	alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_weihaofalse'));
				        }else if(result == "noUsedSubNo"){
							//alert("系统没有可用的拓展尾号！");
				        	alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_sysnoweihao'));
						}else
						{
							//alert("向网关发送请求失败:"+result);
							alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_requestwgfalse') + result);
						}
						 $("a[name='rsend']").attr("disabled","");
						document.pageForm.submit();
				});
		}
		else
		{
		    $.post("app_appsmsTaskRecord.htm?method=reSendSMS", {
				mtid : mtid,
				lgcorpcode : lgcorpcode,
				lguserid : lguserid,
				lgusername:lgusername,
				lgguid:lgguid
			},
				function(result) {
	                if(result=="createSuccess")
					{ 
						//alert("创建短信任务及提交到审批流成功！");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_createtasksuc'));
					}else if(result=="000")
					{
						//alert("创建短信任务及发送到网关成功！");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_createsendsuc'));
					}else if(result=="saveSuccess")
					{
						//alert("存草稿成功！");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_savesuc'));
					}else if(result=="error")
					{
						//alert("请求响应超时，创建短信任务失败！");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_timeout'));
					}else if(result=="nomoney")
					{
					    // alert("余额不足,创建短信任务失败！");
					    alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_yebzrwsb'));
					}else if(result=="false")
					{
					    //alert("创建短信任务时,修改计费信息失败！");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_jfsb'));
					}
					else if(result=="nofindfile")
					{
						//alert("文件不存在,不允许进行失败重发操作!");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_fileisnull'));
					}
					else if(result=="isretry")
					{
					    //alert("已进行了失败重发操作,不允许再进行此操作!");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_notresend'));
					}
					else if(result=="uploadFileFail")
					{
						//alert("上传号码文件失败，取消任务创建！");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_uploadfilefalse'));
					}
					else if(result=="nospnumber")
				    {
					    //alert("发送失败，"+smsAccount+"未设置尾号！");
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_sendfalse') + smsAccount + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_noweihao'));
				    }else if(result == "noSubNo"){
			        	//alert("拓展尾号获取失败！");
				    	alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_weihaofalse'));
			        }else if(result == "noUsedSubNo"){
						//alert("系统没有可用的拓展尾号！");
			        	alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_sysnoweihao'));
					}
					else
					{
						//alert("向网关发送请求失败:"+result);
						alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_requestwgfalse') + result);
					}
					//window.location.href = window.location.href;
					 $("a[name='rsend']").attr("disabled","");
					document.pageForm.submit();
			});
		}
	}
}

//回复详情
function searchNoticeDetail(pageIndex,mtId){
	 $.post("app_appsmsTaskRecord.htm?method=getSinglePerNoticDetail&mtId="+mtId+"&pageIndex="+pageIndex,null,function call(data){   
		if(data!="error"&&data!="noList")
		{
	  		var member = eval("("+data+")");
	   		var moblie;
	   		var time;
	   		var content;
	       	var pageMsg = "";
	       	$("#detailDialog").text("");
	       	var maxSendCount = member.maxSendCount;
	       	var sendInterval = member.sendInterval;
	       	var sendContent = member.sendContent;
	       	var arySendCount = member.arySendCount;
	       	var recordCount = member.count; 
	       	var pageSize = member.pageSize;
	       	var mtId = member.preNoticeId;
	       	var index = member.index;

			var title = "";
			var msg = "<br><center id='ccontent1'><table id='content' style='width:97%;' >"                 
					+ "<thead> <tr>  <th align='center'  width='20%'>" + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_phone') + "</th><th align='center' width='50%'>" + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_recvecontent') + "</th> <th align='center' width='30%'>" + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_recvetime') + "</th> </tr> </thead>";

			if(member.jobs.length>0)
			{
				for(var i= 0;i<member.jobs.length;i++)
				{
        			moblie =  member.jobs[i].moblie;
        			time =  member.jobs[i].time;
        			content =  member.jobs[i].content;
	        		msg = msg +" <tr> <td width='20%' align='center'>"+moblie+"</td> "
	        		+"  <td align='center' width='50%' style='word-break : break-all;'>"+content.replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"          
			        +"  <td align='center' width='30%'>"+time+"</td>";
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
        			pageMsg = pageMsg +"<a href='javascript:searchNoticeDetail("+(pageIndex-1)+","+mtId+")'>" + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_prepage') + "</a>&nbsp;&nbsp;";
        		}
        		if(end - 5 >= 1){
        			pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail(1,"+mtId+")'>"+1+"</a>&nbsp;&nbsp;";
        		}
        		if(start >= 3 ){
        			pageMsg = pageMsg +"<span>...</span>";
        		}
        		for(var i=start ; i <= end ; i++){
        			if(i == pageIndex){
        				pageMsg = pageMsg + "<span class='current'>"+i+"</span>&nbsp;&nbsp;";
        			}else{
        				pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail("+i+","+mtId+")'>"+i+"</a>&nbsp;&nbsp;";
        			}
        		}
        		if(pageCount - end >= 2 ){
        			pageMsg = pageMsg + "<span>...</span>";
        		}
        		if(end < pageCount ){
        			pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail("+pageCount+","+mtId+")'>"+pageCount+"</a>&nbsp;&nbsp;";
        		}
        		if(pageIndex != pageCount){
        			pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail("+(pageIndex+1)+","+mtId+")'>" + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_nextpage') + "</a>";
        		}
			}
			else
			{
				msg =msg + " <tr><td align='center' colspan='7'>" + getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_nodata') + "</td></tr>";
			}
			var foot = "<div>&nbsp;</div>";
			$("#detailDialog").append(title+msg+"</table></center><br><div style='text-align:center';>"+pageMsg+"</div>"+foot); 
			$(".btnClass1").hover(function() {
				var img=$(this).css('background-image');
				$(this).css('background-image',img.replace('but-bg1','but-bg2'))
			}, function() {
				var img=$(this).css('background-image');
				$(this).css('background-image',img.replace('but-bg2','but-bg1'))
			});
			$("#detailDialog").dialog("option","title", getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_recvedetail'));
 			$("#detailDialog").dialog("open");
 			deleteleftline2();
		}
		else if(data=="error"){
			// alert("数据错误，查询失败！");
			alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_selectfalse'));
		}
		else {
			//alert("不存在相应数据！");
			alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_nothisdata'));
		}
   });
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
    if(confirm(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_isstop')))
    {
		$.post(htm+"?method=stopTask", {
			mtId : mtid
		},
		function(result) {
               if(result=="true")
               {
				// alert("任务已终止！");
            	   alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_stoptask'));
            }else if(result=="false"){
				//alert("终止任务失败！");
            	alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_stopfalse'));
            }else if(result=="AllSended")
            {
            	//alert("已经发送完成，无法终止！");
            	alert(getJsLocaleMessage('appmage','appmage_js_smsTaskRecord_stopsuc'));
	        }
			document.pageForm.submit();
		});
    }	
}