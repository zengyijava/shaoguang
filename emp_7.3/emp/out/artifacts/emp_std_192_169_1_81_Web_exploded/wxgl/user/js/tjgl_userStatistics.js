//根据选择公共账号，加载用户属性统计信息信息
function findUserStatistics(chartdiv)
{
	//如何用户选择了群组未选择公共账号，这需要提示用户必须选择公共账号
    var acctid = $("#aid").val();
    var typeid = $("#typeid").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var pathUrl = $("#pathUrl").val();
    var imgurl = "";
    
    if (typeid=="1")
    {
        imgurl = pathUrl + "/wxcommon/swf/Column3D.swf";
	}
    else if (typeid=="2") 
    {
        imgurl = pathUrl + "/wxcommon/swf/Pie3D.swf";
	}
    else if (typeid=="3") 
    {
        imgurl = pathUrl + "/wxcommon/swf/Bar2D.swf";
	}
    
	var url = pathUrl+"/yhsx_userAttrReport.htm?typeid="+typeid+"&aid="+acctid+"&lgcorpcode="+lgcorpcode;	
	$.post("yhsx_userAttrReport.htm",
			{method:"findUserAttrDistribution",aid:acctid,typeid:typeid,lgcorpcode:lgcorpcode,isAsync:"yes"},
			function(result){
				if(result == "outOfLogin")
			    {
			        return;
			    }
				var message=result.substr(0,result.indexOf("@"));
		    	if(message=="success")
				{
		    		var myChart = new FusionCharts(imgurl, "userStatistics", "750", "400");
					myChart.setDataXML(result);
					myChart.render(chartdiv);
	    		    return false;
				}
				else
				{
					contReponse(url);
					return false;
				}
			}
	);
}

//根据选择公共账号，加载用户增长统计信息信息
function findUserGrowthInfo(chartdiv)
{
	//如何用户选择了群组未选择公共账号，这需要提示用户必须选择公共账号
    var acctid = $("#aid").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var pathUrl = $("#pathUrl").val();
    //var imgurl = pathUrl + "/common/swf/Line.swf";
    var imgurl = pathUrl + "/wxcommon/swf/FCF_MSLine.swf";
    var newGrowth = $("input[name='newGrowth']:checked").val();
    var startdate = $.trim($("input[name='startdate']").val());
    var enddate = $.trim($("input[name='enddate']").val());
    var tp = $("input[name='tp']").val();
    var caption = getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_1");
    if("follow"==tp)
    {
        caption = getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_2");
    }
    else if("unfollow"==tp)
    {
        caption = getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_3");
    }
    else if("income"==tp)
    {
        caption = getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_4");
    }
    else if("amount"==tp)
    {
        caption = getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_5");
    }
    
//    if(acctid==null || acctid=="")
//    {
//        art.dialog.alert("请选择公众帐号！");
//        return;
//    }
    if(acctid==null||acctid==""||acctid=="null"){
    	alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_6"));
    	return;
    }
    if(newGrowth=="1"&&(startdate==null||startdate==""||enddate==null||enddate=="")){
    	alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_7"));
        return;
    }
    
	var url = pathUrl+"/yhzz_growReport.htm?aid="+acctid+"&lgcorpcode="+lgcorpcode;	
	$.post("yhzz_growReport.htm",
			{method:"findUserGrowthInfo",aid:acctid,lgcorpcode:lgcorpcode,isAsync:"yes",tp:tp,newGrowth:newGrowth,startdate:startdate,enddate:enddate},
			function(result){
				if(result == "outOfLogin")
			    {
			        return;
			    }
				var message=result.substring(0,result.indexOf("@"));
		    	if(message=="success")
				{
		    		reportresult = result.substring(result.indexOf("@")+1,result.indexOf("$"));
					reportData = result.substring(result.indexOf("$")+1);
					reportData = eval('('+reportData+')');
					// 转换成json对象   
		            var data = $.parseJSON(reportresult);
		            
		            //window.console.log(reportData);
		            var htmlText = "";   
		               
		            // 循环组装下拉框选项   
		            $.each(data, function(k, v)   
		            {
		            	$("#"+k).empty();
		            	$("#"+k).text($("#"+k).text()+v);
		            }); 
					
//		    		var myChart = new FusionCharts(imgurl, "userGrowth", "700", "400");
//					myChart.setDataXML(reportData);
//					myChart.render(chartdiv); 		    	
		            
		            
		            var chart = new Jon.Chart({
			    		caption:caption,
			    		subcaption:"",
			    		xAxisName:"",
			    		yAxisName:getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_8"),
			    		rotateNames:"1",
			    		decimalPrecision:"0",
			    		base:{
			    			id:"UserCartLine",
			    			url:imgurl,
			    			width:'750',
			    			height:'400',
			    			renderTo:chartdiv
			    		}
			    	});
			        
			        //类别
			        var cates = new Array();
			        var returnV = new Array();
			        
			        for(var key in reportData)
			        {
			            var jso = reportData[key];
			            cates.push(jso.xname);
			            returnV.push(jso.yvalue);
			        }
			        chart.loadCategories(cates);
			    	chart.add({seriesName:getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_9"),color:'00cc00',anchorBorderColor:'00cc00', anchorBgColor:'00cc00' },returnV); 
			    	chart.show();
		            
	    		    return false;
				}
				else
				{
					contReponse(url);
					return false;
				}
			}
	);
}

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
		    mon = String(parseInt(mon,10));
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
	WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max});

}

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
	WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:max});

}
