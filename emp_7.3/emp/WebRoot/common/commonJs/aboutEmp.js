
function showVer()
	{
		var validDay = $('#validDay').val();
		var statusInfo = $('#statusInfo').val();
		var sendSpeed = $('#sendSpeed').val();
		var day =0;
		if(validDay != null && validDay != "null" && validDay !="")
		{
		   day = validDay;
		}
		/*天*/
		document.getElementById("youxiaoday").innerHTML=day+" "+getJsLocaleMessage("common","text_sky");

		if(statusInfo != null && statusInfo != "null" && statusInfo !="")
		{
		    if(statusInfo.indexOf("认证成功")==0) {
		    	/*认证成功，欢迎使用！*/
		       document.getElementById("proInfo").innerHTML=getJsLocaleMessage("common","common_frame2_about_5");
		    } else {
		    	/*软件认证失败，还可以使用d%天，d%天内若不能认证成功，届时将关闭软件。*/
		    	if("软件认证失败"===statusInfo.substr(0,6)){
		    		/*取得还剩几天，再替换*/
                    var reg = /(\d+)/;
                    statusInfo.match(reg);
                    statusInfo = getJsLocaleMessage("common","common_frame2_checkFee_21").replace(/d%/g,RegExp.$1);
                }
		    	/*为了保证系统正常使用，请获取认证！*/
		       document.getElementById("proInfo").innerHTML=statusInfo+getJsLocaleMessage("common","common_frame2_about_2");
		    }
		}
		else
		{
			/*无法获取！*/
		   document.getElementById("proInfo").innerHTML=getJsLocaleMessage("common","common_frame2_about_3");
		}
		
		if(sendSpeed == "9999")
		{
            /*不限速！*/
		   document.getElementById("sendspeed").innerHTML=getJsLocaleMessage("common","common_frame2_about_4");
		}
		else if(sendSpeed == null || sendSpeed == ""|| sendSpeed == "null")
		{
            /*无法获取！*/
		   document.getElementById("sendspeed").innerHTML=getJsLocaleMessage("common","common_frame2_about_3");
		}
		else
		{
			/*条/秒*/
		   document.getElementById("sendspeed").innerHTML=sendSpeed+" "+getJsLocaleMessage("common","common_frame2_about_6");
		}
	}	
window.onload=function(){
	showVer();
}