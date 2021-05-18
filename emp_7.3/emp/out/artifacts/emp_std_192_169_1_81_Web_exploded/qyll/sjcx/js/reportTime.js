$("#reportType").change(function(){
	//未做跨年处理
	var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth();
    if(month==0){
        month=1;
    }
    if (month < 10) {
        month = "0" + month;
    }
    //var firstDay = year + "-" + month + "-" + "01";
    //var myDate = new Date(year, month, 0);
    //var lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
    //var lastDay = year + "-" + month + "-" + myDate.getDate();
	if(d.getDate()=='1'&&d.getMonth() =="0"){
		min =  year + "-" + month + "-" + "01";
		max =  year + "-" + month + "-" + "01";
	}else if(d.getDate()=='1'&&d.getMonth() !="0"){
		min = year+ "-" + month + "-" + "01";
		max = year+ "-" + month + "-" + new Date(year, month, 0).getDate();
	}else{
		var dayV   = d.getDate()-1;
		var monthV = d.getMonth()+1;
		if (monthV < 10) {
			monthV = "0" + monthV;
	    }
		if(dayV<10){
			dayV = "0"+dayV;
		}
		min = year+ "-" + monthV + "-" + "01";
		max = year+ "-" + monthV + "-" + dayV;
	}
	var type = $("#reportType").find("option:selected").val();
	if(type=='3'){
		$('.yearTime').show();
		$('.dayTime').hide();
		$('#statisticsTime').val(max.substring(0,4));
	}
	if(type=='2'){
		$('.yearTime').show();
		$('.dayTime').hide();
		$('#statisticsTime').val(max.substring(0,7));
		
	}
	if(type=='1'){
		$('.yearTime').hide();
		$('.dayTime').show();
		$('#sendtime').val(min);
		$('#recvtime').val(max);
	}
});
function updateTime(){
	var d = new Date();
	var max = d.getFullYear()+"-"+((d.getMonth()+1)<10?"0":"")+(d.getMonth()+1)+"-"+(d.getDate()-1);
    //var v = $("#recvtime").attr("value");
    var min = "1900-01-01";
    var type=$("#reportType").val();
    if(type=='1'){
    	WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:max});
    }else if(type=='2'){
    	WdatePicker({dateFmt:'yyyy-MM',minDate:min.substring(0, 7),maxDate:max.substring(0, 7)});
    }else{
    	WdatePicker({dateFmt:'yyyy',minDate:min.substring(0, 4),maxDate:max.substring(0, 4)});
    }
}


