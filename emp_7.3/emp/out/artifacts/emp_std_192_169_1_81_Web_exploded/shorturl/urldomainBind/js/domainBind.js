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
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max,enableInputMask:false});
	
			};
			

			
function check(){ 
	var addcorpCode = $("#addcorpCode").attr("value");
	var addcorpName = $("#addcorpName").attr("value");
	var ival = parseInt(addcorpCode);
	
	//获取所有checkbox的值，并放入数组中
	var strIds=new Array();
	$('input[name="domainbind"]:checked').each(function(){ 
		strIds.push($(this).val()); 
	}); 
	
	if(strIds.length < 1) {
		window.alert("请选择域名进行绑定！");
		return false;
	}
	
	var ids = "";
	for(var i=0;i<strIds.length;i++){
		ids = ids + strIds[i]+",";
	}
	ids = ids.substring(0,ids.length-1);

	if(document.forms["form4"].addcorpCode.value==null||document.forms["form4"].addcorpCode.value.length==0){
		window.alert("企业编码不能为空！");
		document.forms["form4"].addcorpCode.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(isNaN(ival)){
		window.alert("请输入数字");
		document.forms["form4"].addcorpCode.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form4"].addcorpName.value==null||document.forms["form4"].addcorpName.value.length==0){
		window.alert("企业名称不能为空！");
		document.forms["form4"].addcorpName.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}
	
	$.ajaxSettings.async = false;
	$.post("surl_bind.htm",{addcorpCode:addcorpCode,addcorpName:addcorpName,ids:ids,async:false,method:"check"},function(result){
		if(result.length === 0){
			alert("请选择域名进行绑定");
			return false;
		}
		
		if(result.indexOf("n") > 0){
			
			var bindedIds = result.substring(0, result.length-1).split(",");
			
			var bindedDomains = new Array();
			
			$('input[name="domainbind"]:checked').each(function(){
				
				for(var id in bindedIds) {
					if(bindedIds[id] == $(this).val()) {
						bindedDomains.push( $(this).attr("domain"));
					}
				}
			});
			
			alert("域名绑定失败，独享专用的域名为 "+ bindedDomains.join(",") +"已经被绑定");
			return false;
		}else if(result.indexOf("c") > 0){
			
			var bindedIds = result.substring(0, result.length-1).split(",");
			
			var bindedDomains = new Array();
			
			$('input[name="domainbind"]:checked').each(function(){				
				for(var id in bindedIds) {
					if(bindedIds[id] == $(this).val()) {
						bindedDomains.push( $(this).attr("domain"));
					}
				}
			});
			alert("域名绑定失败，域名为 "+ bindedDomains.join(",") +"已经被该企业绑定");
			return false;
		}else{
			if(result==""){
				return false;
			}
			$.post("surl_bind.htm",{result:result,addcorpCode:addcorpCode,method:"add"},function(result){
				if(result === false){
					alert("域名绑定失败");
					return false;
				}else{
					alert("域名绑定成功");
                    window.location.href="surl_bind.htm?method=find";
				}
			});	
			
		}
	});	
	$.ajaxSettings.async = true;
}


function update(){ 
	var addcorpCode = $("#addcorpCode").attr("value");
	var addcorpName = $("#addcorpName").attr("value");
	var bindedIdStr = $("#bindedIdStr").val();

	//获取所有checkbox的值，并放入数组中
	var strIds=new Array();
	$('input[name="domainbind"]:checked').each(function(){ 
		strIds.push($(this).val()); 
	}); 
	
	var ids = "";
	for(var i=0;i<strIds.length;i++){
		ids = ids + strIds[i]+",";
	}
	ids = ids.substring(0,ids.length-1);

	if(ids === bindedIdStr) {
	    alert("未做任何更改");
	    return false;
    }

	$.ajaxSettings.async = false;
	// $.post("surl_bind.htm",{addcorpCode:addcorpCode,addcorpName:addcorpName,ids:ids,bindedIdStr:bindedIdStr,method:"checkUpdate"},function(result){
    $.post("surl_bind.htm",{addcorpCode:addcorpCode,addcorpName:addcorpName,ids:ids,bindedIdStr:bindedIdStr,method:"update1"},function(result){
        if(result == 'success') {
            alert("修改域名绑定成功");
        } else {
            alert("修改域名绑定失败");
        }
    // if(result.indexOf("n") > 0){
		// 	alert("修改域名绑定失败，独享专用的域名id为 "+result.substring(0, result.length-1)+"已经被其他企业绑定");
		// 	return false;
		// }else{
		//
		// 	if(result==""){
		// 		return false;
		// 	}
		//
		// 	$.post("surl_bind.htm",{result:result,addcorpCode:addcorpCode,method:"update"},function(result){
		// 		if(result === false){
		// 			alert("修改域名绑定失败");
		// 			return false;
		// 		}else{
		// 			alert("修改域名绑定成功");
		// 			//window.location.href="surl_bind.htm?method=find";
		// 			document.forms["form5"].submit();
		// 		}
		// 	});
		//
		// }
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
		
function changeStatu(id,corpCode){
	var ks=$.trim($("#flag"+id).attr("value"));
	
	$.ajaxSettings.async = false;
	$.post("surl_bind.htm",{id:id,corpCode:corpCode,state:ks,method:"changeState"},function(result){
		if(result)
		{
			alert("更改状态成功！");
			
			$("#flag"+id).empty();
			
			if(ks == 0)
			{
			    $("#flag"+id).append("<option value='0' selected='selected'>已启用</option>");
			    $("#flag"+id).append("<option value='1' >已禁用</option>");
			}
			else
			{
				$("#flag"+id).append("<option value='1' selected='selected'>已禁用</option>");
				$("#flag"+id).append("<option value='0' >已启用</option>");
			}
			
			$("#flag"+id).next(".c_selectBox").remove();
			
			
		}else
		{
			alert("更改状态失败！");
		}
	});
	$.ajaxSettings.async = true;
}	

