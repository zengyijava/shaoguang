function checkAlls(e,str)    
	{  
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0; i<n; i=i+1)    
			a[i].checked =e.checked;    
	}
	
function openDiv(fid,id,type,dsFlag){
	  if(type==3){
		$("#smsExamine").load("tef_smsTemInfoReview.htm?method=getReviewInfo",{tmId:id,fId:fid},
		function(){
			$("#smsExamine").dialog("open");
		});
	  }else{
		  $("#fmmsId").attr("value",fid);
		  inits();
		  $.post("tef_mmsTemInfoReview.htm?method=getTmMsg",{tmUrl:id},function(result){
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
				$("#shenpi").show();
				$("#inputParamCnt1").remove();
				$("#showParam").html('<div id="inputParamCnt1" ></div>');
				
				$('#mmsExamine').dialog('option', 'title', getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_102"));
				$('#mmsExamine').dialog('option', 'width', '500px');
				$('#mobileDiv').css("float","right");
				$("#mmsExamine").dialog("open");
				
				play(dsFlag);
			}
			else
			{
	             alert(getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_103"));
				}
			});
	}
}
function review(r,t)
{
	if(t==3){
	   
	}else{
	        $("#oks").attr("disabled","disabled");
        $("#rjs").attr("disabled","disabled");
        var pathUrl = $("#pathUrl").val();
	    var cont = $("#cont").val();
	    
	    if(cont.length>400)
		{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_101"));
				$("#oks").attr("disabled","");
                $("#rjs").attr("disabled","");
				return;
		}
	    var id = $("#fmmsId").attr("value");
	    $.post(pathUrl+"/tef_mmsTemInfoReview.htm?method=review",{tmId:id,rState:r,cont:cont},function(result){
	           if(result != null && result == "true")
	           {
	              alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_81"));
	              $("#oks").attr("disabled","");
	              $("#rjs").attr("disabled","");
	              $("#mmsExamine").dialog("close");
	               var url = pathUrl+"/tef_smsTemInfoReview.htm?method=find";
				var conditionUrl = "";
				if(url.indexOf("?")>-1)
				{
					conditionUrl="&";
				}else
				{
					conditionUrl="?";
				}
				conditionUrl = conditionUrl +backfind("#ir_smsTRparams");
                location.href=url+conditionUrl;
	           }
	           else
	           {
	              alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_84"));
	              $("#oks").attr("disabled","");
	              $("#rjs").attr("disabled","");
		        }
		    });
	}
}
	function modify(t,r,dsFlag){
		if(r == 1){
			$('#modify').dialog({
			autoOpen: false,
			width:250,
		    height:200
		});
		$("#msg").empty();
		//$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
		$("#msg").text($(t).children("label").children("xmp").text());
		$('#modify').dialog('open');
	}else{
		doPreview(t,dsFlag);
	}
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
			$('#mmsExamine').dialog('option', 'title', getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_102"));
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
             alert(getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_103"));
		}
		});
}
function examineMsg(frid){
	
	var pathUrl = $("#ipathUrl").val();
	var lguserid = $("#lguserid").val();
	//短信审批
	window.location.href = pathUrl+"/tef_smsTemInfoReview.htm?method=getExamineInfo&lguserid="+lguserid+"&frid="+frid;
}