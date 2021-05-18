function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#recvtime").attr("value");
    var min = "1900-01-01 00:00:00";
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',enableInputMask:false});
}

function rtime() {
    var max = "2099-12-31 23:59:59";
    var v = $("#sendtime").attr("value");
    //if(v.length != 0)
    //{
    //    var year = v.substring(0,4);
    //	var mon = v.substring(5,7);
    //	var day = 31;
    //	if (mon != "12")
    //	{
    //	    mon = String(parseInt(mon,10)+1);
    //	    if (mon.length == 1)
    //	    {
    //	        mon = "0"+mon;
    //	    }
    //	    switch(mon){
    //	    case "01":day = 31;break;
    //	    case "02":day = 28;break;
    //	    case "03":day = 31;break;
    //	    case "04":day = 30;break;
    //	    case "05":day = 31;break;
    //	    case "06":day = 30;break;
    //	    case "07":day = 31;break;
    //	    case "08":day = 31;break;
    //	    case "09":day = 30;break;
    //	    case "10":day = 31;break;
    //	    case "11":day = 30;break;
    //	    }
    //	}
    //	else
    //	{
    //	    year = String((parseInt(year,10)+1));
    //	    mon = "01";
    //	}
    //	max = year+"-"+mon+"-"+day+" 23:59:59"
    //}
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',enableInputMask:false});
}

$('#search').click(
    function(){
        submitForm();
        // alert($("form[name='pageForm']").attr("action"));
        // $("form[name='pageForm']").submit();
    });

function showback(path) {
    var prePageSize = $("#prePageSize").val();
    var prePageIndex = $("#prePageIndex").val();
    var preTotalPage = $("#preTotalPage").val();
    var preTotalRec = $("#preTotalRec").val();
    page_loading();
    window.location.href = path + "/surlBatchVisit.htm?method=find" +
        "&pageSize=" + prePageSize +
        "&pageIndex=" + prePageIndex +
        "&totalPage=" + preTotalPage +
        "&totalRec=" + preTotalRec;
}