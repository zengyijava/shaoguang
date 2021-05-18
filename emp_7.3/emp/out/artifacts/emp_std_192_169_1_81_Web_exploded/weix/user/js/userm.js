$(document).ready(function() {
	getLoginInfo("#getloginUser");
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
		
	initPage($("#_totalPage").val(),$("#_pageIndex").val(),$("#_pageSize").val(),$("#_totalRec").val());
	$("#search").click(function(){submitForm(); });
	//微信编号文本框限制输入非数字
	$("input[name=wcid]").bind('keyup blur',function(){
		var val=$(this).val();
		if(/[^0-9]/g.test(val)){
			$(this).val(val.replace(new RegExp("[^0-9]","gm"), ""));
		}
	});
});
function rtime(){
    //var max = "2099-12-31 23:59:59";
    var max = "2099-12-31";
    var v = $("#submitSartTime").attr("value");
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
		//max = year+"-"+mon+"-"+day+" 23:59:59"
		max = year+"-"+mon+"-"+day+""
	}
	//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

};

function stime(){
   // var max = "2099-12-31 23:59:59";
   var max = "2099-12-31";
    var v = $("#submitEndTime").attr("value");
  //  var min = "1900-01-01 00:00:00";
   var min = "1900-01-01";
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
	//	min = year+"-"+mon+"-01 00:00:00"
		min = year+"-"+mon+"-01"
	}
	//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

};