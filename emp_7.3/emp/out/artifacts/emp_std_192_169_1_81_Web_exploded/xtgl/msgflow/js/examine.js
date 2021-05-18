function rtime(){
	var max = "2099-12-31 23:59:59";
	var v = $("#startSubmitTime").attr("value");
	if(v.length != 0)
	{
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		var day = 31;
		if (mon != "12")
		{
		    mon = String(parseInt(mon,10)+1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		    switch(mon){
			    case "01":day = 31;break;
			    case "02":day = 28;break;
			    case "03":day = 31;break;
			    case "04":day = 30;break;
			    case "05":day = 31;break;
			    case "06":day = 30;break;
			    case "07":day = 31;break;
			    case "08":day = 31;break;
			    case "09":day = 30;break;
			    case "10":day = 31;break;
			    case "11":day = 30;break;
		    }
			}
			else
			{
			    year = String((parseInt(year,10)+1));
			    mon = "01";
			}
			max = year+"-"+mon+"-"+day+" 23:59:59"
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
	
	};
	
	function stime(){
		    var max = "2099-12-31 23:59:59";
	    var v = $("#endSubmitTime").attr("value");
	    var min = "1900-01-01 00:00:00";
		if(v.length != 0)
		{
		    max = v;
		    var year = v.substring(0,4);
			var mon = v.substring(5,7);
			if (mon != "01")
			{
			    mon = String(parseInt(mon,10)-1);
			    if (mon.length == 1)
			    {
			        mon = "0"+mon;
			    }
			}
			else
			{
			    year = String((parseInt(year,10)-1));
			    mon = "12";
			}
			min = year+"-"+mon+"-01 00:00:00"
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
	
};

function openDiv(mtId,level,type){
	var pathUrl = $("#pathUrl").val();
    if(type == 1){
		$('#smsExamine').load(pathUrl+"/msg_smsInfoReview.htm?method=getExamineInfo",{mtId:mtId,RLevel:level,reviewType:reviewType});
		$("#smsExamine").prev("div").css("font-size","14px");
		$("#smsExamine").dialog( "option", "resizable", false );
		$("#smsExamine").dialog("open");
	}else{
		$('#mmsExamine').load(pathUrl+"/msg_mmsInfoReview.htm?method=getExamineInfo",{mtId:mtId,RLevel:level});
	   	$("#mmsExamine").prev("div").css("font-size","14px");
		$("#mmsExamine").dialog( "option", "resizable", false );
	    $("#mmsExamine").dialog("open");
	}
}

//信息审批  短信 彩信 跳转
function examineMsg(taskid,rlevel,type,flowid,frId){
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val();
	//短信审批
	window.location.href = pathUrl+"/msg_smsInfoReview.htm?method=getExamineMsg&lguserid="
	+lguserid+"&flowid="+flowid+"&mtId="+taskid+"&rlevel="+rlevel+"&type="+type+"&frId="+frId;
}













