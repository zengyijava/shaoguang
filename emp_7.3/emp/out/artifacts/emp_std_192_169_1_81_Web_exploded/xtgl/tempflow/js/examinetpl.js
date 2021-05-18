function compartDate() { 
		var endDate=$("#timerTime").val();
	if(endDate==undefined)
	{
		return false
	}
	else{
		return true;
	}
}  

function buttonDisabled(){
	$('#butt').attr("disabled","disabled");
	$('#butt1').attr("disabled","disabled");
}

function buttongEffect(){
	$('#butt').attr("disabled","");
	$('#butt1').attr("disabled","");
}

//点击“查看”的方法    
function doPreview(msg,dsFlag){
	inits();
	$.post("tef_mmsTemInfoReview.htm?method=getTmMsgXtgl",{tmUrl:msg},function(result){
		if (result != null && result != "null" && result != "")
		{   
	        arr = result.split(">");
	        if(arr[0] != null && arr[0] != "")
			{
				var da = $.parseJSON(arr[0]);
				ttime = (da.totaltime/1000);
			}
	        index = 1;
			clearInterval(timer);  
			timer = null;
			$("#screen").empty();
			$("#pointer").empty();
			$("#nextpage").empty();
			$("#cont").val("");
			$("#currentpage").empty();
			parmCount =null;
			$("#inputParamCnt1").empty();
			$("#shenpi").hide();
			$("#showParam").html("");
			$("#mmsExamine").append('<div id="inputParamCnt1" style="margin-top: 10px;margin-left: 280px; "></div>');
			$('#mmsExamine').dialog('option', 'title', getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_99"));
			if(dsFlag == 0){
				$('#mmsExamine').dialog('option', 'width', '290px');
			}else{
				$('#mmsExamine').dialog('option', 'width', '500px');
			}
			$('#mobileDiv').css("float","left");
			$("#mmsExamine").dialog("open");
			play(dsFlag);
		}
		else
		{
             alert(getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_100"));
		}
		});
}

//显示隐藏信息
function changflod(num){
	if($("#img"+num).hasClass("unfold")){
		 $("#msgdiv"+num).show();
		 $("#img"+num).removeClass("unfold");
		 $("#img"+num).addClass("fold");
	}else if($("#img"+num).hasClass("fold")){
		 $("#msgdiv"+num).hide();
	     $("#img"+num).removeClass("fold");
		 $("#img"+num).addClass("unfold");
	}
}

// 刷新页面
function updatePage(){
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	var mtId = $("#mtId").val();
	var flowid = $("#flowid").val();
	window.location.href = pathUrl+"/msg_smsInfoReview.htm?method=find&lguserid="+lguserid+"&mtId="+mtId+"&flowid="+flowid;
}
function review(frId,state)
{
	$("#oks").attr("disabled","disabled");
    $("#rjs").attr("disabled","disabled");
    var cont = $("#content").val();
    var pathUrl = $("#pathUrl").val();
    if(cont.length>400)
	{
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_101"));
			$("#oks").attr("disabled","");
            $("#rjs").attr("disabled","");
			return;
	}
    $.post(pathUrl+"/tef_smsTemInfoReview.htm?method=review",{frId: frId,rState:state,cont:cont},function(result){
           if(result != null && result == "true")
           {
              alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_81"));
              $("#oks").attr("disabled","");
              $("#rjs").attr("disabled","");
              back();
           }
           else
           {
              alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_90"));
              $("#oks").attr("disabled","");
              $("#rjs").attr("disabled","");
           }
    });
}
function back()
{
	var pathUrl = $("#pathUrl").val();
	 var url = pathUrl+"/tef_smsTemInfoReview.htm?method=find&isOperateReturn=true";
		var conditionUrl = "";
		if(url.indexOf("?")>-1)
		{
			conditionUrl="&";
		}else
		{
			conditionUrl="?";
		}
		conditionUrl = conditionUrl +backfind("#loginparams");
     location.href=url+conditionUrl;
}
$(document).ready(function() {
	var he = 500;
	if (navigator.appName == "Netscape")
    {
    	he = 520;
    }
		$("#mmsExamine").dialog({
			autoOpen: false,
			height:he,
			width: 480,
			modal: true,
			resizable:false,
			open:function(){
			}
		});
	getLoginInfo('#loginparams');
});