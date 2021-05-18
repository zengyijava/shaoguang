//发送起止时间控制
	function revtime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#sendtime").attr("value");
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
	

//导出等待
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);

var docUrl = document.URL;
var urls = docUrl.split("/");
docUrl = urls[0] + "//" + urls[2] + "/" + urls[3];

function dengdai(){
  var width = "100%";
	var height = "100%";
	setdengdai("dengdai");
	if (null != document.getElementById("dengdai") && null != document.getElementById("mask2"))
	{
	document.body.removeChild(document.getElementById("dengdai"));
	document.body.removeChild(document.getElementById("mask2"));
	}
	var newDiv = document.createElement("div");
	newDiv.id = "dengdai";
	newDiv.style.position = "fixed";
	newDiv.style.zIndex = "100";
	newDiv.style.width = width;
	newDiv.style.height = height;
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.background = "#999";
	newDiv.style.filter = "alpha(opacity=60)";
	newDiv.style.opacity = 0.6;
	newDiv.innerHTML = '<iframe style="border:0;position:absolute;top:0;left:0;width:99%;height:99%;filter:alpha(opacity=0);"></iframe>'
	if(isIE6){
		newDiv.style.position = "absolute";
		newDiv.style.width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth) + "px";
		newDiv.style.height = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) + "px";
	}
	document.body.appendChild(newDiv);
	newDiv.style.display="block";
	// mask图层
	var newMask = document.createElement("div");
	newMask.id = "mask2";
	newMask.style.position = "absolute";
	newMask.style.zIndex = "200";
	newMask.style.background = "#FFFFFF";
	newMask.style.width = "330px";
	newMask.style.height = "100px";
	newMask.innerHTML = "<center><div id='percent' style='padding-top:15px;'><img src='"+$("#inheritPath").val()+"/images/loding.gif' style='width:300px;height:15px'/>"
	    +"<div style='padding-top:10px;' id='waiting'>"+getJsLocaleMessage('rms','rms_report_waithanding')+"</div></div></center>";
	newMask.style.top = "40%";
	newMask.style.left = "43%";
	newMask.style.opacity = 1;
	document.body.appendChild(newMask);
}

function qddd(isreload)
{
	if (null != document.getElementById("mask2"))
	{
		document.body.removeChild(document.getElementById("mask2"));
	}
	if (null != document.getElementById("dengdai"))
	{
	    document.body.removeChild(document.getElementById("dengdai"));
	    setdengdai("nodengdai");
	}
}

function qxdc(isreload)
{
	$("#quxiao").attr("disabled","true");
	if (isreload)
	{
		var menucode=$("#menucode").val();
 		$.post(urls[0]+"//"+urls[2]+"/"+urls[3]+"/"+"stopThread.htm",
				{menucode:menucode},
				function(result){
					  if(result=='stop')
					   {
						   qddd(false);
						   alert(getJsLocaleMessage('rms','rms_report_cancelexpsuccess'));
					   }
				  });
	}
}

function setdengdai(v)
{
	var szz = document.getElementById("showZhezhao");
	if (szz != null)
	{
		szz.value = v;
	}
}
