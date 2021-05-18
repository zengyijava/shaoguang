$(document).ready(function(){
	$('.enddate').live('click',function(){
		if(!$(this).hasClass('Wdate')){
			return;
		}
	    var max = "2099-12-31 23:59:59";
	    var v = $(this).parents("tr").find('.startdate').val();
		if(v.length != 0)
		{
		   
			min = v;
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
	});
	
	$('.startdate').live('click',function(){
			if(!$(this).hasClass('Wdate')){
				return;
			}
	        var max = "2099-12-31 23:59:59";
		    var v = $(this).parents("tr").find('.enddate').val();
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
				max = v;
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
	});
	
	$('.validity').live('click',function(){
		if(!$(this).hasClass('Wdate')){
			return;
		}
	    var max = "2099-12-31 23:59:59";
	    var v = new Date().Format("yyyy-MM-dd hh:mm:ss");
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
	});
	
});
