function revtime(){
			    var max = "2099-12-31 23:59:59";
			    var v = $("#startTime").attr("value");
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
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max,enableInputMask:false});
	
			};
	
			function sedtime(){
			       var max = "2099-12-31 23:59:59";
			    var v = $("#recvtime").attr("value");
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
			
function checkform(){
	var domain = $("#adddomain").attr("value");
	var lenExten = $("#addlenExten").attr("value");
	var lenExten1 = parseInt(lenExten);
	var validDays = $("#addvalidDays").attr("value");
	var validDays1 = parseInt(validDays);
	var dtype = $("#adddtype").attr("value");
	var dtype1 = parseInt(dtype);
	if(document.forms["form1"].adddomain.value==null||trim(document.forms["form1"].adddomain.value).length==0){
		window.alert("????????????????????????");
		document.forms["form1"].adddomain.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form1"].addlenExten.value==null||document.forms["form1"].addlenExten.value.length==0){
		window.alert("?????????????????????????????????");
		document.forms["form1"].addlenExten.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(isNaN(lenExten) || lenExten.indexOf(".")>0){
		window.alert("???????????????????????????????????????");
		document.forms["form1"].addlenExten.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form1"].addlenExten.value > 15 ){
		window.alert("??????????????????????????????15??????");
		document.forms["form1"].addlenExten.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form1"].addvalidDays.value==null||document.forms["form1"].addvalidDays.value.length==0){
		window.alert("?????????????????????????????????");
		document.forms["form1"].addvalidDays.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}
    else if(document.forms["form1"].addvalidDays.value>999){
        window.alert("??????????????????????????????????????????3?????????");
        document.forms["form1"].addvalidDays.focus();
        butk("#btnSsu","#btnSca");
        return false;
    }else if(isNaN(validDays) || validDays.indexOf(".")>0){
		window.alert("???????????????????????????????????????");
		document.forms["form1"].addvalidDays.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form1"].addvalidDays.value <= 0){
		window.alert("??????????????????????????????1??????");
		document.forms["form1"].addvalidDays.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form1"].adddtype.value==null||document.forms["form1"].adddtype.value.length==0){
		window.alert("???????????????????????????");
		document.forms["form1"].adddtype.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}
	var flag=false;
	$.ajaxSettings.async = false;
	$.post("surl_manage.htm",{domain:domain,lenExten:lenExten,validDays:validDays,dtype:dtype,method:"add"},function(result){
		if(result=="true"){
			//window.location.href="<%=path %>/surl_manage.htm?method=findMain";
//			$.post("surl_manage.htm",{method:"findMain"});
			document.forms["form1"].submit();
			flag=true;
		}else{
			alert("?????????????????????");
			return false;
			
		}
	});	
	$.ajaxSettings.async = true;
	return flag;
}

function trim(str) {
	return str.replace(/(^\s+)|(\s+$)/g, "");
}

function update(){ 
	var id = $("#id").attr("value");
	var domain = $("#adddomain").attr("value");
	var lenExten = $("#addlenExten").attr("value");
	var lenExten1 = parseInt(lenExten);
	var validDays = $("#addvalidDays").attr("value");
	var validDays1 = parseInt(validDays);
	var dtype = $("#adddtype").attr("value");
	var dtype1 = parseInt(dtype);
	
	if(document.forms["form2"].adddomain.value==null||trim(document.forms["form2"].adddomain.value).length==0){
		window.alert("????????????????????????");
		document.forms["form2"].adddomain.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form2"].addlenExten.value==null||document.forms["form2"].addlenExten.value.length==0){
		window.alert("?????????????????????????????????");
		document.forms["form2"].addlenExten.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(isNaN(lenExten) || lenExten.indexOf(".")>0){
		window.alert("???????????????????????????????????????");
		document.forms["form2"].addlenExten.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form2"].addlenExten.value > 15 ){
		window.alert("??????????????????????????????15??????");
		document.forms["form2"].addlenExten.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form2"].addvalidDays.value==null||document.forms["form2"].addvalidDays.value.length==0){
		window.alert("?????????????????????????????????");
		document.forms["form2"].addvalidDays.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(isNaN(validDays) || validDays.indexOf(".")>0){
		window.alert("???????????????????????????????????????");
		document.forms["form2"].addvalidDays.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form2"].addvalidDays.value <= 0){
		window.alert("??????????????????????????????1??????");
		document.forms["form2"].addvalidDays.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form2"].adddtype.value==null||document.forms["form2"].adddtype.value.length==0){
		window.alert("???????????????????????????");
		document.forms["form2"].dtype.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}
	
	$.ajaxSettings.async = false;
	$.post("surl_manage.htm",{id:id,domain:domain,lenExten:lenExten,validDays:validDays,dtype:dtype,method:"update"},function(result){
		if(result=="false"){
			alert("?????????????????????");
			return false;
		}else{
			//window.location.href=path+"/surl_manage.htm?method=findMain";
			document.forms["form2"].submit();
			//$.post("surl_manage.htm",{method:"findMain"});
		} 
			
	});	
	$.ajaxSettings.async = true;
}
			
function butk(id1,id2,id3)
{
	if(id1 != null && id1 != "")
	{
		$(id1).attr("disabled",false);
	}
	if(id2 != null && id2 != "")
	{
		$(id2).attr("disabled",false);
	}
	if(id3 != null && id3 != "")
	{
		$(id3).attr("disabled",false);
	}
}

function changeStatu(id){
	var ks=$.trim($("#flag"+id).attr("value"));
	
	$.ajaxSettings.async = false;
	$.post("surl_manage.htm",{id:id,method:"changeState"},function(result){
		if(result)
		{
			alert("?????????????????????");
			$("#flag"+id).empty();
			
			if(ks == 0)
			{
			    $("#flag"+id).append("<option value='0' selected='selected'>?????????</option>");
			    $("#flag"+id).append("<option value='1' >?????????</option>");
			}
			else
			{
				$("#flag"+id).append("<option value='1' selected='selected'>?????????</option>");
				$("#flag"+id).append("<option value='0' >?????????</option>");
			}
			
			$("#flag"+id).next(".c_selectBox").remove();
			
			
			
			
			
		}else
		{
			alert("?????????????????????");
		}
	});
	$.ajaxSettings.async = true;
}	







			