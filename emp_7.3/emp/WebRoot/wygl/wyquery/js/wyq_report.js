function showDate()
{
	var r = $("#reportType").attr("value");
	if (r == 0)
	{
		//WdatePicker({skin:'simple',dateFmt:'yyyy-MM',isShowClear:true});
		WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});
	}
	else
	{
		//WdatePicker({skin:'simple',dateFmt:'yyyy',isShowClear:true});
		WdatePicker({dateFmt:'yyyy',isShowClear:false});
	}
}