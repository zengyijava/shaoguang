var smsContentLen = 990;
var baseContentLen = 990;
$(function(){
	//预览窗口
	$("#detail_Info").dialog({
		autoOpen: false,
		modal: true,
		// title: '预览效果',
		title: getJsLocaleMessage('appmage','appmage_js_appPageEdit_ylxg'),
		height: 330,
		width: 500,
		closeOnEscape: false,
		open: function() {
			var ct = $.trim($("#ct").text());
			var yct = $.trim($("#yct").text());
			var isCharg = $("#isCharg").val();
			var gwFee = $("#gwFee").val();
			if(gwFee.substring(0, 9) != "lessgwfee" && gwFee != "feefail" && gwFee != "nogwfee")
			{
				if (isCharg == "true") {
					if (eval(ct) < eval(yct)) {
						$("#messages1").show();
					} else {
						$("#messages1").hide();
					}
					$("#showyct").show();
				} else {
					$("#showyct").hide();
					$("#messages1").hide();
				}
			}
			else
			{
					$("#showyct").hide();
					$("#messages1").hide();
			}
		},
		close: function() {
			resetButtons();
			$("#showyct").show();
		}
	});
	
	$("#showeffinfo").toggle(
		function() {
			$("#effinfo").hide();
			$("#arrowhead").removeClass("fold");
			$("#arrowhead").addClass("unfold");
		},
		function() {
		$("#effinfo").show();
		$("#arrowhead").removeClass("unfold");
		$("#arrowhead").addClass("fold");
		if ($("#messages1").is(":hidden")) {
			$("#effinfotable").css("top", "62px");
		} else {
			$("#effinfotable").css("top", "90px");
		}
	});
		
	//取消按钮
	$('#btcancel').click(function(){
		$("#detail_Info").dialog('close');
//		$("#preview").attr("disabled", false);
//		$("#sendReset").attr("disabled", false);
	})
	$('#sendTabs').tabs('.tabsContent');
	$('.uploadFile dd').tipsy({gravity: 'nw'});
	$('#sendContent').manhuaInputLetter({'showId':'countLimit','len':990});

	//预览
	$('#preview').click(function(e){
		e.stopPropagation();
		$("#preview").attr("disabled", true);
		$("#sendReset").attr("disabled", true);
		var count = $(window.frames['flowFrame'].document).find("#manCount").html();
		if(count == 0)
		{
				// alert('请选择发送对象！');
				alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_selectsendobject')); 
				resetButtons();
				return false;
		}
		sendTypeVal=$('#sendTabs').find('input[name="sendType"]:checked').val();
		var sendContent = "";
		var isStop = false;
		if(sendTypeVal==0)
		{
			sendContent=$.trim($('#sendContent').val());
			if(!sendContent)
			{
				// alert('请输入发送内容!');
				alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_inputcontent')); 
				resetButtons();
				return false;
			}
			else
			{
				$.ajax({
					url: "common.htm?method=checkBadWord1", 
					type : "POST",
					data: {tmMsg : sendContent,corpCode : $('#lgcorpcode').val()},
					async: false, 
					success: function(message) {
						if(message.indexOf("@")==-1){
							//alert("关键字检验请求响应超时，请刷新页面或稍后重试！[EXFV005]");
							alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_checkkey')); 
							resetButtons();
							isStop = true;
							return;
						}
						message=message.substr(message.indexOf("@")+1);
						if (message != "" && message !="error") {
							//alert("发送内容包含如下违禁词组：\n     " + message + "\n请检查后重新输入[EXFV006]");
							alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_wzcz') + message + getJsLocaleMessage('appmage','appmage_js_appPageEdit_reinput')); 
							resetButtons();
							isStop = true;
							return;
						} else if (message == "error") {
							//alert("检验关键字异常[EXFV007]");
							alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_keyerror')); 
							resetButtons();
							isStop = true;
							return false;
						} 
					},
					error:function(xrq,textStatus,errorThrown){
						// alert("关键字检验请求响应超时，请刷新页面或稍后重试！[EXFV008]");
						alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_keytimeout')); 
						resetButtons();
						isStop = true;
						return;
					}
				});
			}
		}else if(sendTypeVal==1){
			var fileUrl=$('#fileUrl').val();
			if(!fileUrl){
				// alert('请上传多媒体文件!');
				alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_uploaddmtfile')); 
				resetButtons();
				return false;
			}
		}
		if(!isStop)
		{
			// 当前登录操作员id
			var lgUserId = $('#lguserid').val();
			// 当前登录企业
			var lgCorpCode = $('#lgcorpcode').val();
			//当前APP企业
			var appaccount = $("#appAccount").val();
			// 发送客户账号
			var appCode = $('#appcode').val();
			// 发送客户群组
			var group = $('#group').val();
			// 发送APP企业账号
			var appCorpCode = $('#appcorpcode').val();
			//SP账号
			var spUser = $('#spUser').val();
			//业务类型
			var busCode = $('#busCode').val();
			//是否同时发送短信
			var isSmsSend = 0;
			if($("#isSmsSend").attr("checked"))
			{
				isSmsSend = 1;
				$('#hidisSmsSend').val(1);
			}
			$.post("app_msgsend.htm",{
				method:"preview" ,
				spUser:spUser,
				busCode:busCode,
				isSmsSend:isSmsSend,
				sendTypeVal:sendTypeVal,
				lguserid:lgUserId ,
				lgcorpcode:lgCorpCode,
				appaccount:appaccount,
				group:group,
				appcorpcode:appCorpCode,
				appcode:appCode,
				sendContent:sendContent
	   			},function(result){
	   				if(result.indexOf('@@') != -1)
	   				{
						var data = result.split("@@");
						var subCount = $(window.frames['flowFrame'].document).find("#manCount").html();
						$('#subCount').html(subCount);
						$('#effCount').html(data[0]);
						$('#repeat').html(subCount - data[0] - data[1]);
						$('#illegal').html(data[1]);
						if(data[2] != "noSendSms")
						{
							if(data[2] == 1)
							{
								// alert("APP消息短信发送预览失败！");
								alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_ylsb')); 
								resetButtons();
								return;
							}
							else if(data[2] == 2)
							{
								// alert("APP消息短信发送获取发送文件路径失败！");
								alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_hqwjljsb')); 
								resetButtons();
								return;
							}
							else if(data[2] == 4)
							{
								// alert("APP消息短信发送获取预发送条数失败！");
								alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_yfstssb')); 
								resetButtons();
								return;
							}
							else if(data[2] == 5)
							{
								// alert("APP消息短信发送机构扣费失败！");
								alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_kfsb')); 
								resetButtons();
								return;
							}
							else
							{
								var array=eval("("+data[2]+")");
								$("#hidSubCount").val(array.subCount);
								$("#hidEffCount").val(array.effCount);
								$("#hidMobileUrl").val(array.validFilePath);
								$("#hidPreSendCount").val(array.preSendCount);
								
								$('#smssubCount').html(array.subCount);
								$('#smseffCount').html(array.effCount);
								$('#smsrepeat').html(array.repeatCount);
								$('#smsillegal').html(array.badModeCount);
								$('#smsblack').html(array.blackCount);
								
								var nums=array.previewPhone.split(';');
								$("#counts").text(array.subCount);
							    $("#effs").text(array.effCount);
							    $("#ct").text(array.depFeeCount);
							    $("#yct").text(array.preSendCount);
							    $("#blacks").text(array.blackCount);
							    $("#legers").text(array.badModeCount);
							    $("#sames").text(array.repeatCount);
							    $("#badurl").attr("value",array.viewFilePath);
							     var spye = array.spFeeResult;
							    $("#gwFee").attr("value",spye);
							    checkspye(spye);
							    checkSPfee(array);
							    
							    var wuxiaonum = parseInt(array.subCount)-parseInt(array.effCount);
							    $("#alleff").text(wuxiaonum);
							    if(wuxiaonum == 0)
							    {
							    	$("#preinfonum").hide();
							    }else{
							    	$("#preinfonum").show();
							    }
							    $("#isCharg").attr("value",array.isCharge);
							    $("#infos").show();
							    $("#infos").prev('legend').show();
							    $("#fieldset_info").show();
								$("#effinfotable").show();
							}
						}
						else
						{
							$("#infos").hide();
							$("#infos").prev('legend').hide();
							$("#fieldset_info").hide();
							$("#effinfotable").hide();
						}
						$("#detail_Info").dialog("open");
						//$("#sendUserInfo").dialog("open");
	   				}	
	   				else
	   				{
	   					alert(result);
	   					resetButtons();
	   				}	
				}
			);
		}
	})

	//form表单提交
	$('#sendSubmit').click(function(e){
		e.stopPropagation();
		sendTypeVal=$('#sendTabs').find('input[name="sendType"]:checked').val();
		//$("#sendUserInfo").dialog('close');
		$("#sendSubmit").attr("disabled", true);
        $("#probar").dialog({
		modal:true,
		//title:'加载进度..',
		title:getJsLocaleMessage('appmage','appmage_js_appPageEdit_load'),
		height:80,
		resizable :false,
		closeOnEscape: false,
		width:200,
		open: function(){
		$(".ui-dialog-titlebar").hide();
      	}
		});
		$('#appForm').submit();
	})
	
	$('#sendSub').click(function(){
		$('.app_placeholder').hide();});
	
	$('#sendSub').keyup(function(){
		//var sendSubject=$.trim($(this).val());
		$('.app_placeholder').hide();
		//sendSubject=sendSubject ? sendSubject : '请输入发送主题';
		//$('#subTitle').html(sendSubject);
	}).blur(function(){
		if($.trim($(this).val())==''){
			$('.app_placeholder').show();
		}
	});
	$('.app_placeholder').click(function(){
		$(this).hide();
		$('#sendSub').focus();
	})
	$('#sendContent').keyup(function(){
		var sendContent=$.trim($(this).val());
		sendContent=sendContent ? sendContent : getJsLocaleMessage('appmage','appmage_js_appPageEdit_inputcontent') ; //'请输入发送内容';
		var temp = document.createElement ("div");
		(temp.textContent != null) ? (temp.textContent = sendContent) : (temp.innerText = sendContent);
		var output = temp.innerHTML;
		$('#bubbleContent').html(output);
	});
	//谷歌浏览器使用其他文件上传方式
	if(navigator.userAgent.toLowerCase().match(/chrome/) != null)
		{
			$('#uploadFile').fileupload({
		        url: 'app_msgsend.htm?method=uploadFile',
		        dataType: 'json',
		        add: function (e, data) {
		        	 var jqXHR = data.submit()
		             .complete(function (result) {
		            	 if(result){
		                	 handleSuccess(result.responseText);
		            	 }else{
		            		 handleError();
		            	 }
		            	 handleComplete();
		            });
		        }
		    });
		}
		//触发账户change事件
	$("#spUser").trigger("change");
})

function showInputInfo()
{
	var sendContent=$.trim($('#sendContent').val());
	sendContent=sendContent ? sendContent : getJsLocaleMessage('appmage','appmage_js_appPageEdit_inputcontent') ; //'请输入发送内容';
	var temp = document.createElement ("div");
	(temp.textContent != null) ? (temp.textContent = sendContent) : (temp.innerText = sendContent);
	var output = temp.innerHTML;
	$('#bubbleContent').html(output);
}

//上传文件类型限制
var fileTypeArray={
	img:['jpg','jpeg','png','bmp'],
	video:['3gp','mp4'],
	audio:['amr']
};

//获取文件名
function getFileDest(fileRoute){
	var fileArr=fileRoute.split('\\'),
	fileDest=fileRoute[fileArr.length-1];
	return fileDest;
}

//替换3gp格式为flv后缀名
function changeLastType(fileRoute){
	if(fileRoute.indexOf('.3gp')!=-1){
		return fileRoute.replace('.3gp','.flv');
	}else if(fileRoute.indexOf('.amr')!=-1){
		return fileRoute.replace('.amr','.mp3');
	}
	else
	{
		return fileRoute;
	}
	
}

//获取文件类型
function getFileType(fileRoute){
	var pos=fileRoute.lastIndexOf("."),
	fileType=fileRoute.substring(pos+1).toLowerCase();
	for(var key in fileTypeArray){
		var bExits=$.inArray(fileType,fileTypeArray[key]);
		if(bExits!=-1){
			return key;
		}
	}
}
//文件上传
function upload(obj){
	var fileRoute=$.trim($(obj).val());
	var fileType = getFileType(fileRoute);
	if(fileType != "img" && fileType != "video" && fileType!="audio")
	{
		// alert("上传文件格式错误，请重新选择！");
		alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_reupload')); 
		$('#txt_input').val('');
		$('#fileUrl').val('');
		resetfileinput('#uploadFile');
		//$('#bubbleMedia').html('请选择多媒体内容!');
		$('#bubbleMedia').html(getJsLocaleMessage('appmage','appmage_js_appPageEdit_xzdmtnr'));
		return;
	}
	$("#preview").attr("disabled", true);
	$("#sendReset").attr("disabled", true);
	$('#txt_input').val(fileRoute);
	$('#bubbleMedia').html("<img src='"+path+"/common/img/mobilePreview/load.gif'/>");
	if(navigator.userAgent.toLowerCase().match(/chrome/) == null){
		$.ajaxFileUpload({
	           url:'app_msgsend.htm?method=uploadFile',
	           secureuri: false,
	           fileElementId:'uploadFile',
	           success: function (data, Status) {
	               if('success'==Status){
	            	   	handleSuccess($(data).text());
	   	           }
	           },
	           error: function (data, status, e) {
	        	   handleError();
	           },
	           complete:function(){
	        	   handleComplete();
	           }
	       });
	}else{
	}
	
}

	function handleSuccess(fileRoute){
	   var fileType=getFileType(fileRoute);
	   var fileDest=getFileDest(fileRoute);
	   if(fileRoute.indexOf("other")!=-1)
	   {
		 $('#txt_input').val('');
		 $('#fileUrl').val('');
		 // s$('#bubbleMedia').html('请选择多媒体内容!');
		 $('#bubbleMedia').html(getJsLocaleMessage('appmage','appmage_js_appPageEdit_xzdmtnr'));
	  	 // alert("上传文件失败！");
		 alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_uploaderror'));
	   }
      else if(fileRoute.indexOf("overSize")!=-1){
    	  $('#txt_input').val('');
    	  $('#fileUrl').val('');
    	 // $('#bubbleMedia').html('请选择多媒体内容!');
    	  $('#bubbleMedia').html(getJsLocaleMessage('appmage','appmage_js_appPageEdit_xzdmtnr'));
          //alert("上传文件大小超过限制！");
    	  alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_filesizetoobig'));
      }else if(fileRoute.indexOf("loadServerError")!=-1){
    	  $('#txt_input').val('');
    	  $('#fileUrl').val('');
    	  //$('#bubbleMedia').html('请选择多媒体内容!');
    	  $('#bubbleMedia').html(getJsLocaleMessage('appmage','appmage_js_appPageEdit_xzdmtnr'));
          // alert("上传文件至文件服务器失败！");
          alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_uploadfilefalse'));
      }else if(fileRoute.indexOf("subError")!=-1){
    	  $('#txt_input').val('');
    	  $('#fileUrl').val('');
    	  //$('#bubbleMedia').html('请选择多媒体内容!');
    	  $('#bubbleMedia').html(getJsLocaleMessage('appmage','appmage_js_appPageEdit_xzdmtnr'));
          // alert("上传文件失败，文件大小超过限制或文件不存在！");
          alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_fileisnull'));
      }else{
		$('#fileUrl').val(absolutePath + fileRoute);
		var bubbleMedia=$('#bubbleMedia');
		switch(fileType){
			case 'img':
			bubbleMedia.html('<img src="'+fileRoute+'" />');
			break;
			case 'video':
			fileRoute=changeLastType(fileRoute);
			bubbleMedia.html('');
			var videoParams={
				'route':videoPath,
				'width':115,
				'height':80,
				'fileUrl':path +'/'+ fileRoute,
				'wrap':'bubbleMedia'
			};
			createVideoPreview(videoParams);	
			break;
			case 'audio':
			fileRoute=changeLastType(fileRoute);	
			var mp3Array=[],temp;
			var data = videoPath+'/mp3/dewplayer-mini.swf?mp3='+fileRoute;
			mp3Array.push('<object style="width:20px" type="application/x-shockwave-flash" data="'+data+'" width="160" height="20" id="dewplayer-mini">');
			mp3Array.push('<param name="wmode" value="transparent" />');
			mp3Array.push('<param name="movie" value="'+data+'" />');
			mp3Array.push('</object>');
			temp=mp3Array.join('');
			$('#bubbleMedia').html(temp);
			break;
			case 'file':
			bubbleMedia.html('<p class="showFile"><img src="'+iPath+'/img/file.png" />'+fileDest+'</p>');
			break;
		}
      }
}

function handleComplete(){
	resetButtons();
}

function handleError(){
	$('#txt_input').val('');
	$('#fileUrl').val('');
	resetButtons();
	//$('#bubbleMedia').html('请选择多媒体内容!');
	$('#bubbleMedia').html(getJsLocaleMessage('appmage','appmage_js_appPageEdit_xzdmtnr'));
   	//alert("多媒体文件上传失败！");
	alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_dmtfileuploadfalse'));
}

function uploadTest(obj){
	var bubbleMedia=$('#bubbleMedia');
	bubbleMedia.html('');
	var videoParams={
		'route':videoPath,
		'width':115,
		'height':80,
		'fileUrl':iPath+'/img/demo.flv',
		'wrap':'bubbleMedia'
	};
	createVideoPreview(videoParams);
}

function uploadAudio(){
	var mp3Array=[],temp;
	mp3Array.push('<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="25" height="20" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab">');
	mp3Array.push('<param name="movie" value="'+videoPath+'mp3/singlemp3player.swf?file='+iPath+'/img/demo.mp3&backColor=990000&frontColor=ddddff&repeatPlay=true&songVolume=30" />');
	mp3Array.push('<param name="wmode" value="transparent" />');
	mp3Array.push('<embed wmode="transparent" width="25" height="20" src="'+videoPath+'mp3/singlemp3player.swf?file='+iPath+'/img/demo.mp3&backColor=&frontColor=ffffff&repeatPlay=false&songVolume=30" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />');
	mp3Array.push('</object>');
	temp=mp3Array.join('');
	$('#bubbleMedia').html(temp);
}

function hideSelect()
{
	$("#busCode").css("display","none");
	$("#spUser").css("display","none");
}
//检查SP余额
function checkSPfee(array){
    var feeFlag=array.feeFlag;
    var isshow=true;
     //如果余额<发送条数
	if(eval(array.depFeeCount)<eval(array.preSendCount))
	{
		isshow=false;
    }
	//后付费情况,该值不显示
	 if(feeFlag=="2"){
		$("#shospfee").text("");
	}
	 //预付费处理
	else if(feeFlag=="1")
	{
		$("#shospfee").text("");
		//$("#shospfee").html("SP账号余额：<span id='spfee'></span>");
		$("#shospfee").html(getJsLocaleMessage('appmage','appmage_js_appPageEdit_spzhye') + "<span id='spfee'></span>");
		var spAccount=array.spUserAmount;
		var resultCount=eval(spAccount-array.preSendCount);
	 	checkSpAccount(isshow,resultCount,spAccount);
	 }else if(feeFlag=="-1"&&isshow){
		 $("#shospfee").text("");
		 // $("#messages2 font").text("SP账号不能为空");
		 $("#messages2 font").text(getJsLocaleMessage('appmage','appmage_js_appPageEdit_spisnull'));
		 $("#sendSubmit").attr("disabled","disabled");
	 }else if(feeFlag=="-2"&&isshow){
		 $("#shospfee").text("");
		 //$("#messages2 font").text("获取不到SP账号信息");
		 $("#messages2 font").text(getJsLocaleMessage('appmage','appmage_js_appPageEdit_spinfoisnull'));
		 $("#sendSubmit").attr("disabled","disabled");
	 }else if(feeFlag=="-3"&&isshow){
		 $("#shospfee").text("");
		 //$("#messages2 font").text("检查SP账号余额异常！");
		 $("#messages2 font").text(getJsLocaleMessage('appmage','appmage_js_appPageEdit_sperror'));
		 $("#sendSubmit").attr("disabled","disabled");
	 }
	
}

	// 提示余额相关信息
function checkSpAccount(isshow,resultCount,spAccount)
{
	var spUser=$("#spUser").val();
	var altstr = "";
	if(resultCount <0&&isshow)
	{
		altstr = spUser+ getJsLocaleMessage('appmage','appmage_js_appPageEdit_yebzbfs'); //"账号余额不足，不允许发送，请联系管理员充值.";
		$("#messages2 font").text(altstr);
		$("#sendSubmit").attr("disabled","disabled");	 
	}

	$("#spfee").text(spAccount);

}

function checkspye(yeresult)
{
	var altstr = "";
	if(yeresult.indexOf("lessgwfee") == 0)
	{
		// altstr = "运营商余额不足，当前余额："+yeresult.split("=")[1];
		altstr = getJsLocaleMessage('appmage','appmage_js_appPageEdit_yysyebz')+yeresult.split("=")[1];
		 $("#sendSubmit").attr("disabled","disabled");
	}else if(yeresult=="feefail" || yeresult=="nogwfee")
	{
		//altstr = "获取运营商余额失败，不允许发送";
		altstr = getJsLocaleMessage('appmage','appmage_js_appPageEdit_yysyebzbfs');
		 $("#sendSubmit").attr("disabled","disabled");
	}
	$("#messages2 font").text(altstr);
}

//详情下载
function uploadbadFiles() {
	var badurl = $("#badurl").val();
	badurl = badurl.replace("_view.txt", "_bad.txt");
	$.post("common.htm?method=goToFile&url="+badurl, {
		url: badurl
	}, function(result) {
		if (result == "true") {
            download_href(path + "/doExport.hts?u=" + badurl);
		} else if (result == "false") alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_fileisexite')); //alert("文件不存在");
		else alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_jumperror')); //alert("出现异常,无法跳转");
	});
}

//根据账户获取账户绑定的路由信息拆分规则
function setGtInfo()
{
	var spuser = $('#spUser').val();
	$.post("app_msgsend.htm",{method : "getSpGateConfig",spUser : spuser},
		function(infoStr){
			if(infoStr !="error" && infoStr.indexOf("infos:") == 0)
			{
				var infos = infoStr.replace("infos:","").split("&");
				$("#gt1").val(infos[0]);
				$("#gt2").val(infos[1]);
				$("#gt3").val(infos[2]);
				$("#gt4").val(infos[3]);
				if($("#isSmsSend").attr("checked"))
				{
					smsContentLen = infos[4];
					baseContentLen = smsContentLen;
					$('#maxLen').html("/"+smsContentLen);
				}
				else
				{
					smsContentLen = 990;
					baseContentLen = 990;
					$('#maxLen').html("/990");
				}
			}
			contentlen($("#sendContent"));
		}
	);
}

 //统计短信内容字数
function contentlen(ob, flag) {
	if(flag == 2)
	{
		ob.val($.trim(ob.val()));
	}
	var content = $.trim($("#sendContent").val());
	var huiche = content.length - content.replaceAll("\n", "").length;
	var contentLen = Number(content.length) + Number(huiche);
	if (contentLen > smsContentLen) {
		$("#sendContent").val(content.substring(0, smsContentLen - huiche));
		content = $("#sendContent").val();
		huiche = content.length - content.replaceAll("\n", "").length;
	}
	var len = smsContentLen;
	if(content.length>smsContentLen)
	{
		$("#sendContent").val(content.substring(0,smsContentLen));
	}
	var countLimit = $("#sendContent").val().length + huiche;
	countSMS(countLimit, content);
	content = $("#sendContent").val();
	if(len != smsContentLen)
	{
		if($("#sendContent").val().length>smsContentLen)
		{
			$("#sendContent").val(content.substring(0,smsContentLen));
		}
		$('#maxLen').html("/"+smsContentLen);
	}
	countLimit = $("#sendContent").val().length + huiche;
	$('#countLimit').html(countLimit);
}

// 统计条数-短信拆分规则
function countTS(s) {

	var len ;
	var maxLen;
	var totalLen;
	var lastLen;
	var signLen;
	var count;
	for(var i=1;i<4;i=i+1)
	{
		count = 0;
		if(s>0)
		{
			var gtinfo = $("#gt"+i).val();
			if(gtinfo != "")
			{
				var gtinfos = gtinfo.split(",");
				
				maxLen = gtinfos[0];
				totalLen = gtinfos[1];
				lastLen = gtinfos[2];
				signLen = gtinfos[3];
				len = s*2;
				if (len <= (totalLen - signLen + 3)*2)
					count = 1;
				else
					count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
			}
		}
		$("#ft"+i).text(count);
	}
}

// 统计条数-短信拆分规则js版(支持英文短信)
function countSMS(s, content) {
	var len ;
	var enLen;
	var reset;
	var maxLen;
	var totalLen;
	var lastLen;
	var signLen;
	var enmaxLen;
	var entotalLen;
	var enlastLen;
	var ensignLen;
	var gateprivilege;
	var ensinglelen;
	var count;
	var enMsgShortLen;
	var signLocation;
	var longSmsFirstLen;
	//短信内容是否为中文短信,false:英文;true:中文
	var isChinese = false;
	for(var i=1;i<5;i=i+1)
	{
		count = 0;
		if(s>0)
		{
			enLen = 0;
			enMsgShortLen = 0;
			reset = false;
			var gtinfo = $("#gt"+i).val();
			if(gtinfo != "")
			{
				var gtinfos = gtinfo.split(",");
				
				maxLen = gtinfos[0];
				totalLen = gtinfos[1];
				lastLen = gtinfos[2];
				signLen = gtinfos[3];
				enmaxLen = gtinfos[4];
				entotalLen = gtinfos[5];
				enlastLen = gtinfos[6];
				ensignLen = gtinfos[7];
				gateprivilege = gtinfos[8];
				ensinglelen = gtinfos[9];
				signLocation = gtinfos[10];
				//签名前置
				if(signLocation == 1)
				{
					longSmsFirstLen = entotalLen - ensignLen;
				}
				else
				{
					longSmsFirstLen = entotalLen;
				}
				//支持英文短信
				if(!isChinese && gateprivilege == 1){
					//字符ASCII码
					var charAscii;
					//是否中文短信
					for(var j=0;j<content.length;j++)
					{
						enLen += 1;
						enMsgShortLen += 1;
						charAscii = content.charAt(j).charCodeAt();
						if(charAscii > 127)
						{
							isChinese = true;
						}
						for(var k=0; k<special.length; k++)
						{
							if(special[k] == charAscii)
							{
								//长短信边界值
								if(enLen % longSmsFirstLen == 0)
								{
									//条数加2
									enLen += 2;
								}
								else
								{
									enLen += 1;
								}
								enMsgShortLen += 1;
								break;
							}
						}
						
						//英文短信长度超过最大短信长度
						if(enLen > smsContentLen)
						{
							$("#sendContent").val(content.substring(0,j));
							content = $("#sendContent").val();
							s = content.length;
							//重新计算条数
							reset = true;
							break;
						}
						if(isChinese)
						{
							break;
						}
					}
					//重新计算条数,重新遍历
					if(reset)
					{
						i = 0;
						continue;
					}
					//如果为短短信
					if(enMsgShortLen <= (ensinglelen - ensignLen))
					{
						enLen = enMsgShortLen;
					}
				}
				//短信内容为英文并且支持英文短信，使用英文拆分规则
				if(!isChinese && gateprivilege == 1){
					//国内通道英文短信,特殊字符按1个计算
					if(i != 4)
					{
						enLen = s;
					}
					//条数计算
					if (enLen <= (ensinglelen - ensignLen)){
						count = 1;
					}
					else{
						count = 1 + Math.floor((enLen - enlastLen +  parseInt(entotalLen) -1) / entotalLen);
					}
				}
				//中文短信
				else{
					//处理模板导入,短信内容条数计算
					if(isChinese && contentLen == 700)
					{
						contentLen = 350;
						if(s > contentLen)
						{
							$("#sendContent").val(content.substring(0,350));
							content = $("#sendContent").val();
							s = content.length;
							//重新计算条数
							i = 0;
							continue;
						}
					}
					len = s*2;
					if (len <= (totalLen - signLen + 3)*2)
						count = 1;
					else
						count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
				}
			}
		}
		$("#ft"+i).text(count);
	}
	if(isChinese && smsContentLen == 700)
	{
		smsContentLen = 350;
	}
	else if(!isChinese && smsContentLen == 350 && baseContentLen == 700)
	{
		smsContentLen = 700;
	}
}

//重置上传文件控件
function resetfileinput(obj){
	 $(obj).wrap("<form></form>");
     var $form = $(obj).parent();
     $form[0].reset();
     $(obj).unwrap();
}

function resetButtons(){
	var isIE = (document.all) ? true : false;
	var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	if(isIE6){
		resetfileinput('#preview');
		resetfileinput('#sendReset');
	 }
	$('#preview').attr('disabled',false);
	$('#sendReset').attr('disabled',false);
}

function setDefault()
{
	if(confirm(getJsLocaleMessage('appmage','appmage_js_appPageEdit_setdefault'))) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var busCode = $("#busCode").val();
		var spUser = $("#spUser").val();
		$.post("app_msgsend.htm?method=setDefault", {
			lguserid: lguserid,
			lgcorpcode: lgcorpcode,
			busCode: busCode,
			spUser: spUser,
			flag: "6"			
			}, function(result) {
			if (result == "seccuss") {
				//alert("当前选项设置为默认成功！");
				alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_setdefaultsuc'));
				return;
			} 
			else if(result == "fail"){
				// alert("当前选项设置为默认失败！");
				alert(getJsLocaleMessage('appmage','appmage_js_appPageEdit_setdefaultfalse'));
				return;
			}
		});
	}
}