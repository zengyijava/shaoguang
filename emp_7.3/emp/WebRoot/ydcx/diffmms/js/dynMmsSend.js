var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var formName = "dynMmsSend";

//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
$(document).ready(function() {
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ jsp中的初始化
	var isIE = (document.all) ? true : false;
    var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0" ? true : false);
	var isIE8 = isIE && (navigator.appVersion.match(/MSIE 8.0/i)=="MSIE 8.0" ? true : false);
	var isIE9 = isIE && (navigator.appVersion.match(/MSIE 9.0/i)=="MSIE 9.0" ? true : false);
	var userAgent = navigator.userAgent.toLowerCase(); 
	var ismozi360 = userAgent.indexOf('mozilla/4.0')>-1?true:false;
	var isie360 = userAgent.indexOf('360se')>-1?true:false;
	var isSG =(userAgent.indexOf('se 2.x') != -1);
	var tophei = $('.top:visible').height();
	var topMargin = $('.top:visible').css('margin-top'); 
	if(tophei==null)
	{
		tophei = 0;
	}else{

	}
	if(topMargin != null)
	{
		tophei = tophei + 5;
	}
	
	
	//加载格式提示处的悬浮框
	floating("downlinks","infomodel1");
	getLoginInfo("#loginparam");
	//处理将输入框中的内容置空 
	inputTipText();
	$('#moreSelect').hide();
	$('#u_o_c_explain').toggle(function(){
			$("#foldIcon").removeClass("unfold");
			$("#foldIcon").addClass("fold");
			$('#moreSelect').show();
		},function(){
			$("#foldIcon").removeClass("fold");
			$("#foldIcon").addClass("unfold");
			$('#moreSelect').hide();
	});
	//彩信模板中CSS效果 
	$("#modelTable tbody tr").hover(function() {
		$(this).addClass("div_hover_bg");
	}, function() {
		$(this).removeClass("div_hover_bg");
	});
	$("#filesdiv input").hover(function(e){
       $("#upfile").css("text-decoration","underline");
	},function(e){
        $("#upfile").css("text-decoration","none");
	});
	var h = 500;
	if (navigator.appName == "Netscape") {
    	h = 500;
    }
	$("#tempView").dialog({
			autoOpen: false,
			height:h,
			width:515,
			modal: true,
			resizable:false,
			close:function(){
			    cplaytime = 0;
				nplaytime = -1;
				$("#screen").empty();
				clearInterval(ttimer); 
		}
	});



	
	$("#showeffinfo").click(function() {
	    if($("#effinfo").is(":hidden"))
	    {
			$("#effinfo").show();
			$("#arrowhead").removeClass("unfold");
			$("#arrowhead").addClass("fold");
            if($("#messages1").is(":hidden")){
                $("#effinfotable").css("top","62px");
            }
            else
            {
               $("#effinfotable").css("top","90px");
            }
	    }else
	    {
			$("#effinfo").hide();
			$("#arrowhead").removeClass("fold");
			$("#arrowhead").addClass("unfold");
		}
	});
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//短信预览窗口
	$("#detail_Info").dialog({
		autoOpen: false,
		modal:true,
		//title:'预览效果', 
		title:getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_ylxg"), 
		height:500,
		width:500,
		closeOnEscape: false,
		open:function(){
		//hideSelect();
		//$(".ui-dialog-titlebar-close").hide();												 		 
		var ct = $.trim($("#ct").text());
		var yct =$.trim($("#yct").text());
		
		var isCharg = $("#isCharg").val();
		
		var spFeeResult =  $("#spFeeResult").val();
		if(spFeeResult.indexOf("lessgwfee") == 0)
		{
			//altstr = "运营商余额不足，当前余额："+yeresult.split("=")[1];
			altstr = getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_yysyebzdqye")+yeresult.split("=")[1];
			$("#messages1").empty();
 			$("#messages1").html(altstr);
 		     $("#messages1").show(); 
 		     $("#btsend").attr("disabled","disabled");
 		    return;
		}else if(spFeeResult=="feefail" || spFeeResult=="nogwfee")
		{
			//altstr = "获取运营商余额失败，不允许发送";
			altstr = getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_hqyysyesbbyxfs");
			$("#messages1").empty();
 			$("#messages1").html(altstr);
 		     $("#messages1").show(); 
 		     $("#btsend").attr("disabled","disabled");
 		     return;
		}
		
		 if(isCharg == "true") {
	 		 if(eval(ct)<eval(yct))  {
	 			$("#messages1").empty();
	 			//$("#messages1").html("<font color='red'>余额不足不允许进行发送!</font>");
	 			$("#messages1").html("<font color='red'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_yebzbyxjxfs")+"</font>");
	 		     $("#messages1").show(); 
	 		     $("#btsend").attr("disabled","disabled");
	 		 } else {
	 			$("#messages1").empty();
	 		    $("#messages1").hide(); 
	 		 }
	 		 $("#showyct").show();
		 } else{
		     $("#showyct").hide();
		     $("#messages1").hide(); 
		 } 	 
		 //如果预发送条数为0，则也不允许发送，因为有可能此号码未绑定通道。				 		 
		 if(eval(yct) == 0) {
		    $("#btsend").attr("disabled","disabled");
		 }	
	}
	,close:function(){
	   // showSelect();
		$("#effinfo").hide();
		$("#btsend").attr("disabled","");
		$("#showyct").show();
	}
});	

// 点击创建或存草稿按钮时
$("input[id='preSend']").click(function() {
			$("form[name='" + formName + "']").find("input[name='subState']").val(2);
			//主题
			$("#taskName").trigger("blur");
			var taskName =$("#taskName").val();
			//主题颜色   
			var taskDeColor = $("#taskName").css("color");
			var tmName =$("#tmName").val();
			//var tmUrl = $("input[name='tmMsg']:checked").val();
			var tmUrl =$("#mmstemplateid").val();
			
			 if (taskName == "" || "#ccc" == taskDeColor){
		    	//alert("请添加彩信主题！");
		    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_qtjcxzt"));
		    	return;
			}
			if (tmName == "") {
				//alert("请添加彩信标题！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_qtjcxbt"));
				return;
			}
			if(!tmUrl || tmUrl == "" ){
				//alert("请添加彩信内容！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_qtjcxnr"));
		    	return;
			}
			//$("#tmUrl").val($("input[name='tmMsg']:checked").attr("tmMsg"));
			$("#tmUrl").val($("#mmstemplateurl").val());
			if (checkFile()) {
				checkWords();
			}
	});
});

function send(subcount){
	if($("#effs").text()=="0"){
		//alert("无有效发送号码，无法提交。");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_wyyfshm"));
		$("#btsend").attr("disabled","");
		$("#btcancel").attr("disabled","");
	}else{
		subcount = subcount+1;
		if(subcount > 1){
			//alert("提交信息中，请勿重复提交！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_tjxxz"));
			$("form[name='" + formName + "']").attr("action","");
			return;
		}
		//$(".ui-dialog-buttonpane button").attr("disabled",true);
		$("form[name='" + formName + "']").attr("target","_self");
		$("form[name='" + formName + "']").attr('action', "dmm_sendDynMMS.htm?method=send");
		if($("form[name='" + formName + "']").attr("encoding")) {
			$("form[name='" + formName + "']").attr("encoding","application/x-www-form-urlencoded");
		}else {
			$("form[name='" + formName + "']").attr("enctype","application/x-www-form-urlencoded");
		}
		$("form[name='" + formName + "']").submit();
	}
}

function btsend(){
	var sendType = $("input[name='sendType']:checked").val();
	$("#btsend").attr("disabled",true);
	$("#btcancel").attr("disabled",true);
	var subcount = 0;
	if (sendType == 1) {
		var serverTime ;
		var sendTime = $('#sendtime').val();
		$.post("common.htm", {
			method : "getServerTime"
		}, function(msg) {
			serverTime = msg;
			var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
			var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
            date1.setMinutes(date1.getMinutes() - 1, 0, 0);
			if (date1 <= date2) {
				//alert("预发送时间小于服务器当前时间！请合理预定发送时间[EXFV011]");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_yfssjxyfwqdqsj")+"[EXFV011]");
				$("#sendtime").val("");
				//showSelect();
				$("#btsend").attr("disabled","");
				$("#btcancel").attr("disabled","");
				$("#detail_Info").dialog("close");
				return;
			}else{
				send(subcount);
			}
		});
	}else {
		send(subcount);
	}
	
	
}

// 验证上传文件格式
function checkFile() {
	//alert("看看看进来没有？");
	var fileName = $("#numFile").val();
	if (fileName != "") {
		var index = fileName.lastIndexOf(".");
		var fileType = fileName.substring(index + 1).toLowerCase();
		if (fileType != "txt" && fileType !="zip"  && fileType !="xls"  && fileType !="xlsx"  && fileType !="et") {   
			//alert("上传文件格式错误，请选择txt、xls、xlsx、et或zip格式的文件。");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_scwjgscw"));
			return false;
		} else {
			return true;
		}		
	} else {
		//alert("请选择上传的文件！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_qxzscdwj"));
		return false;
	}
}

// 验证短信内容是否包含关键字
function checkWords() {
	$("#preSend").attr("disabled",true);
    //$("#qingkong").attr("disabled",true);
    var pathUrl = $("#pathUrl").attr("value");
	var lgcorpcode = $("#lgcorpcode").val();
	var theme = $("#tmName").val();
	//var tmMsg = $('input[name="tmMsg"]:checked').attr("tmMsg");
	var tmMsg = $("#mmstemplateurl").val();
	//var templateId = $('input[name="tmMsg"]:checked').val();
	var templateId = $("#mmstemplateid").val();
	$.post(pathUrl+"/dmm_sendDynMMS.htm?method=checkBadWords",{langName:langName,tmMsg:tmMsg,text:theme,lgcorpcode:lgcorpcode,templateId:templateId},function(result){
		if(result.indexOf("html") > 0){
			window.location.href=location;
		    return;
		}else if(result!="") {
			alert(result);
			$("#preSend").attr("disabled","");
			//$("#qingkong").attr("disabled","");
    		return;
		} else {
			checkTimer();
		}
	});
}

// 验证是否定时
function checkTimer() {
	var sendType = $("input[name='sendType']:checked").val();
	var timerTime = $("#sendtime").val();
	if (sendType == 1 && timerTime == "") {
		//alert("请填写定时时间！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_qtxdssj"));
		$("#preSend").attr("disabled","");
		//$("#qingkong").attr("disabled","");
		return ;
	} else if(sendType == 1){
		$.post("common.htm", {
			method : "getServerTime"
		}, function(msg) {
			serverTime = msg;
			var date1 = new Date(Date.parse(timerTime.replaceAll("-","/")));
			var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
            date1.setMinutes(date1.getMinutes() - 1, 0, 0);
			if (date1 <= date2) {
				//alert("预发送时间小于服务器当前时间！请合理预定发送时间！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_yfssjxyfwqdqsj"));
				$("#sendtime").val("");
				$("#preSend").attr("disabled","");
				//$("#qingkong").attr("disabled","");
				return;
			}else{
				getCt();
				$("#probar").dialog({
    				modal:true,
    				//title:'加载进度..', 
    				title:getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_jzjd"), 
    				height:100,
    				resizable :false,
    				closeOnEscape: false,
    				width:200,
    				open: function(){
    			  		errorNum=0;
    					$(".ui-dialog-titlebar").hide();
    					dd = window.setTimeout("fresh()",3000);
    		  		}
    		});
				//防止内存溢出，初始化为0。预览成功为1，为0则 提示系统繁忙
				$("#error").val("0");	
				//$("form[name='" + formName + "']").submit();
				$("form[name='" + formName + "']").attr("action",$("form[name='" + formName + "']").attr("action")+"&lguserid="+$("#lguserid").val());
	    		$("form[name='" + formName + "']").submit();
			}
		});
	}else{
		getCt();
		$("#probar").dialog({
			modal:true,
			//title:'加载进度..', 
			title:getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_jzjd"), 
			height:100,
			resizable :false,
			closeOnEscape: false,
			width:200,
			open: function(){
		  		errorNum=0;
				$(".ui-dialog-titlebar").hide();
				dd = window.setTimeout("fresh()",3000);
	  		}
	});
		//防止内存溢出，初始化为0。预览成功为1，为0则 提示系统繁忙
		$("#error").val("0");	
		//$("form[name='" + formName + "']").submit();
		$("form[name='" + formName + "']").attr("action",$("form[name='" + formName + "']").attr("action")+"&lguserid="+$("#lguserid").val());
		$("form[name='" + formName + "']").submit();
	}
	
}

function preSend(data){
	$("#preSend").attr("disabled",false);
    //$("#qingkong").attr("disabled",false);
    $("#error").val("1");	
	window.clearInterval(dd);
	//关闭旋转界面
	$("#probar").dialog("close");
	$(".ui-dialog-titlebar").show();
	if(data=="error"){
		//alert("预览异常，操作失败！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_ylycczsb"));
		//qdzz();
		return;
	}
	if(data=="noTmsMsg"){
		//alert("彩信模板文件不存在，预览失败！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_cxmbwjbcz"));
		//qdzz();
		return;
	}
	if(data=="overstep"){
		//alert("文件内有效号码大于500万，系统不支持，请重新选择发送文件！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_wjnyyhmdy500w"));
		$("#preSend").attr("disabled",false);
		//$("#qingkong").attr("disabled",false);
		return;
	}
	if(data=="overSize"){
		//alert("上传文件过大，单次上传TXT文件或EXCEL文件最大支持100兆，ZIP文件最大支持10兆，请重新选择发送文件！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_scwjgd"));
		$("#preSend").attr("disabled",false);
		//$("#qingkong").attr("disabled",false);
		return;
	}
	if(data=="noPhone"){
		//alert("无有效号码！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_wyxhm"));
		$("#preSend").attr("disabled",false);
		//$("#qingkong").attr("disabled",false);
		return ;
	}
	$("#newtable").empty();
	$("#preStr").val(data);
	var array=data.split('&');
	if(array[3] == 0){
		//alert("无有效号码！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_wyxhm"));
		$("#preSend").attr("disabled",false);
		//$("#qingkong").attr("disabled",false);
		return ;
	}else{
		//hideSelect();
		var tmUrl = $("#tmUrl").val();
		$("#mobileUrl").val(array[1]);
		$("#subCount").val(array[2]);
		$("#effCount").val(array[3]);
		$("#counts").text(array[2]);
	    $("#effs").text(array[3]);
	    $("#blacks").text(array[4]);
	    $("#legers").text(array[5]);
	    $("#sames").text(array[6]);
	    $("#keyW").text(array[7]);
	    $("#ct").text(array[8]);
		$("#yct").text(array[9]);
		$("#isCharg").attr("value",array[10]);
		 $("#spFeeResult").attr("value",array[11]);
		//无效号码
		$("#alleff").text(array[2]-array[3]);
		if(array[2]-array[3] == 0){
			$("#baddownload").attr("style","display:none");
		}else{
			$("#baddownload").attr("style","display:block");
		}
		
		//无效号码路径
		$("#badurl").attr("value",array[1]);
		$("#newtable").load(
		    	"dmm_sendDynMMS.htm?method=readMmsContent",
		    	{url:array[0],tmUrl:tmUrl},
		    	function(){
		    		$("#detail_Info").css("display","block");
		    		$("#detail_Info").dialog("open");
		    		deleteleftline1();
		    });
		/*$("#newtable").append("<tr align ='center'><td style='border: 1px  solid; height: 25px;'>"+1
				+"</td><td style='border: 1px  solid; height: 25px;'>"+array[9]+"</td>" +
				"<td style='border: 1px  solid; height: 25px;'>"
				+"<a style='cursor:pointer;color:blue;' onclick='doPreview(\""+tmUrl+"\",+"+2+",\""+array[10]+"\")'>【预览】</a>"+"</td></tr>");
			$("#detail_Info").css("display","block");
	$("#detail_Info").dialog("open");*/
	//$(".ui-dialog-titlebar-close").hide();
	}
}	
function checkError()
{
	if($("#error").val()=="0")
	{
		window.clearInterval(dd);
		$("#probar").dialog("close");
		//alert("系统繁忙，请稍后重试！[EBFV0284]");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_xtfmqshcs")+"[EBFV0284]");
		$("#preSend").attr("disabled",false);
        //$("#qingkong").attr("disabled",false);
	}
}
//防止数据库网络断开
function fresh()
{
	$.ajax({
		url:"LoadingServlet.htm",
		type:"post",
		dataType:"script",
		success:function(result){
			if( result !="true")
			{
				if(errorNum==0)
				{
					errorNum==1;
					//alert("网络或服务器无法连接，请稍后重试！[EXFV009]");
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_wlhfwqwflj")+"[EXFV009]");
					window.clearInterval(dd);
					window.location.href='dmm_sendDynMMS.htm?method=find&lgguid='+$('#lgguid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			if(errorNum==0)
			{
				errorNum=1;
				//alert("网络或服务器无法连接，请稍后重试！[EXFV010]");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_wlhfwqwflj")+"[EXFV010]");
			}
			window.clearInterval(dd);
			window.location.href='dmm_sendDynMMS.htm?method=find&lgguid='+$('#lgguid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
}



//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~jsp中的fuction


//预览
function doPreview(msg,type,tdId)
{
    var paramContent = $("#"+tdId).html();
    var pathUrl = $("#pathUrl").val();
    //var	url = "/tem_mmsTemplate.htm?method=getTmMsg";
    var	url = "/dmm_sendDynMMS.htm?method=getTmMsg";
	inits();
	$.post(pathUrl+url,{tmUrl:msg,paramContent:paramContent},function(result){
		if (result != null && result != "null" && result != "")
		{
			arr = result.split(">");
			if(arr[0] != null && arr[0] != "")
			{
				var da = $.parseJSON(arr[0]);
				ttime = (da.totaltime/1000);
			}
			index = 1;
			$("#screen").empty();
			$("#pointer").empty();
			$("#nextpage").empty();
			$("#currentpage").empty();
			parmCount =null;
			$("#inputParamCnt1").empty();
			$("select[name='groupSelect']").css("visibility","hidden");
			$("select[name='mmsUser3']").css("visibility","hidden");
			$("#tempView").dialog("open");
			play(1);
		}
		else
		{
             //alert("内容文件不存在，无法预览！");
             alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_nrwjbcz"));
		}
	});
}


//处理点查看模板时访问的页面
function openTemp(r)
{
	var path = $("#pathUrl").val();
	window.open( path + "/fileUpload/fileDownload/" + r,"_blank");	
}

	function inputTipText(){
	//所有样式名中含有graytext的input
	$("input[class*=graytext]").each(function(){
		var oldVal=$(this).val(); //默认的提示性文本
		$(this)
		.css({'color':'#ccc'}) //灰色
		.focus(function(){
		if($(this).val()!=oldVal)
			{$(this).css({'color':'#000'})}
		else
			{$(this).val('').css({'color':'#ccc'})}
		})
		.blur(function(){
		if($(this).val()=="")
			{$(this).val(oldVal).css({'color':'#ccc'})}
		})
		.keydown(function(){
			$(this).css({'color':'#000'})
		})
	})
}
//浏览按纽事件
function upFile(){
	if(checkFile("numFile")){ 
		var pathValue = $("#numFile").val();
		var index = pathValue.lastIndexOf("\\");
		var name =fileName= pathValue.substring(index + 1);
		if(name.length >12){
		   name = name.substring(0,12)+"....";
		}
		var fileType=getFileType(fileName),icon;
	 	 if(fileType=='txt'){
	 	 	icon='txt.gif';
	 	 }else if(fileType=='xls' || fileType=='xlsx' || fileType=='et'){
	 	 	icon='xls.gif';
	 	 }else{
	 	 	icon='fileimg.png';
	 	 }
		 var iPath = $("#iPath").val();
	      //$("#upTipDiv").html("<div id='tr' style='margin-bottom:0px;height:35px;line-height:35px;'><a style='background : url("+iPath+"/img/"+icon+") no-repeat left center;padding: 2px 0;margin-left:5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;<label>"+name+"</label>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delfile()' style='text-decoration: none;color: #2970c0;'>删除</a></div>");  
		 //$("#upTipDiv").html("<div id='tr' style='margin-bottom:0px;height:35px;line-height:35px;'><a style='background : url("+commonPath+"/common/img/"+icon+") no-repeat left center;padding:2px 0;margin-left:5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;<label stype='width:160px;'>"+name+"</label>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delfile()' style='text-decoration: none;color: #2970c0;'>删除</a></div>");  		     
		 $("#upTipDiv").html("<div id='tr' style='margin-bottom:0px;height:35px;line-height:35px;'><a style='background : url("+commonPath+"/common/img/"+icon+") no-repeat left center;padding:2px 0;margin-left:5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;<label stype='width:160px;'>"+name+"</label>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delfile()' style='text-decoration: none;color: #2970c0;'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_sc")+"</a></div>");  		     
		  		         
		 $("#upTipDiv").show();        			 
	}
}
//删除附件
function delfile(filename){	 
   var fileobj = document.getElementById("numFile");
	fileobj.outerHTML = fileobj.outerHTML; 
   if($("#upfilediv").html() == "")
   {
      $("#upfilediv").hide();
   }
   $("#upTipDiv").html("");
   $("#upTipDiv").hide();
}

//详情下载
function uploadbadFiles()
{
    var badurl = $("#badurl").val();
    var pathUrl = $("#pathUrl").val();
    badurl = badurl.replace(".txt","_bad.txt");
   	$.post("dmm_sendDynMMS.htm?method=goToFile", {
		url : badurl
	},
		function(result) {
			if (result == "true") {
				download_href( pathUrl+"/doExport.hts?u="+badurl);		
			} else if (result == "false")
				//alert("文件不存在");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_wjbcz"));
			else
				//alert("出现异常,无法跳转");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_cxyc"));
	});
}
function toAddTemplate(){
    var pathUrl = $("#pathUrl").val();
	var lgguid = $("#lgguid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	window.location.href = pathUrl+'/dmm_sendDynMMS.htm?method=doAdd&templateType=1&lgguid='+lgguid+'&lgcorpcode='+lgcorpcode;

}

//选择彩信模板
function chooseMmsTpl()
{
	$(".ui-dialog-titlebar-close").show();
	var pathUrl = $("#pathUrl").val();
	var frameSrc = $("#tempFrame").attr("src");
	if(frameSrc.indexOf("blank.jsp") > 0)
	{
		var lguserid = $("#lguserid").val();
		frameSrc = pathUrl+"/smm_sameMms.htm?method=getLfTemplateByMms&tmpltype=1&lguserid="+lguserid;
		$("#tempFrame").attr("src",frameSrc);
	}
	var hei=$(window).height();
	if(hei>970){
		hei=660;
	}else{
		hei=$(window).height()*0.65;
	}
	//初始化彩信模板DIV
	$("#tmplDiv").dialog({
		autoOpen: false,
		height:hei,
		width: 645,
		resizable:false,
		modal: true,
		open:function(){
		$(this).height(660);
		},
		close:function(){
		}
	});
	$("#tmplDiv").dialog("open");
}

// 取消彩信模板
function tempNo()
{
	$("#mmstemplateid").val("");
	$("#mmstemplateurl").val("");
	$("#templatepre").empty();
	$("#templatepre").css("display","none");
}
// 关闭彩信模板
function closeDialog(){
	$("#tmplDiv").dialog("close");
}

//开启新的标签
function openTemDiv(menuCode) {
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	window.parent.openNewTab(menuCode,pathUrl+"/tem_mmsTemplate.htm?method=doAdd&type=2&lgcorpcode="+lgcorpcode);
}
//发送成功跳转发送信息查看界面
 function sendRecord(menuCode)
 {
	var guid = $("#lgguid").val();
	var taskid = $("#oldTaskId").val();
	closemessage();
	window.parent.openNewTab(menuCode,base_path+"/mmt_mmsTask.htm?method=find&taskid="+taskid+"&lgguid="+guid+"&pageIndex="+1+"&pageSize="+15);
 }
 function closemessage()
 {
	 var lgguid = $("#lgguid").val();
	 $("#message").dialog("close");
 	 getCt();
	 window.location.href = base_path + "/dmm_sendDynMMS.htm?lgguid="+lgguid;
 }
function setDefault()
{
	//if(confirm("确认是否将当前选项设置为默认？ ")) {
	if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_qrsfjdqxxszwmr"))) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var mmsUser = $("#mmsUser").val();
		$.post("dmm_sendDynMMS.htm?method=setDefault", {
			lguserid: lguserid,
			lgcorpcode: lgcorpcode,
			spUser: mmsUser,
			flag: "10"			
			}, function(result) {
			if (result == "seccuss") {
				//alert("当前选项设置为默认成功！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_dqxxszwmrcg"));
				return;
			} 
			else if(result == "fail"){
				//alert("当前选项设置为默认失败！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_dqxxszwmrsb"));
				return;
			}
		});
	}
}








