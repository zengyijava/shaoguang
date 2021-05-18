//跳到告警历史页面
function toHisMon(menuCode,apptype,monname)
{
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	if(apptype==5700){apptype = 5000;}
	if(monname=="-")
	{
		monname="";
	}
	window.parent.openNewTab(menuCode,pathUrl+"/mon_realTimeAlarm.htm?method=toHisMon&type=1&lgcorpcode="+lgcorpcode+"&apptype="+apptype+"&monname="+escape(encodeURIComponent(monname)));
}
function openDealMon(id,flag,dealdesc)
{
	$("#id").val(id);
	$("#dealdesc").val(dealdesc);
	 $("input[name='dealflag']").each(function(){
		 if($(this).val()==flag)
		 {
			 $(this).attr("checked",true);
		 }
	 });
	$("#dealMon").dialog("open");
	len($("#dealdesc"));
}
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}
function openDealMons()
{
	$("input[name='dealflag']").each(function(){
		$(this).attr("checked",false);
	 });
	var id="";
	$("input:checkbox[name='checklist']").each(function(){
		if($(this).attr("checked")==true)
		{
			id=id+$(this).val()+",";
		}
	});
	if(id=="")
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkxq_ssgj_1"));
		return;
	}
	$("#id").val(id);
	$("#dealMon").dialog("open");
}
function doreturn()
{
	$("#dealMon").dialog("close");
}
function checkPut()
{
	$("#dealdesc").val($("#dealdesc").val().replaceAll("'",""));
	
}
//计算字数
function getFontNumber(ob)
{
	$("#dealdesc").val($("#dealdesc").val().replaceAll("'",""));
	len(ob);
	
}
//统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	//var content = ob.val();
	var huiche = content.length - content.replaceAll("\n", "").length;
	if(ob.is("#dealdesc")){
		if(content.length>150)
		{
			$("#dealdesc").val(content.substring(0,150));
		}
	}
	$("#strlen").html(($("#dealdesc").val().length + huiche));
}
//处理
function dealMon()
{
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var lgusername = $("#lgusername").val();
	var time=new Date();
	var ids = $("#id").val();
	var dealdesc = $("#dealdesc").val();
	var dealflag = $("input[name='dealflag']:checked").val();
	if(dealflag==null || dealflag=="")
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkxq_ssgj_2"));
		return;
	}
	if(dealdesc.length>150)
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkxq_ssgj_3"));
		return;
	}
	$("#dealMon").find("input[type='button']").attr("disabled","disabled");
	$.post("mon_realTimeAlarm.htm",
			{method:"dealMon",lgcorpcode:lgcorpcode,ids:ids,lguserid: lguserid,dealdesc:dealdesc,dealflag:dealflag,lgusername:lgusername,time:time,isAsync:"yes"},
			function(result){
				$("#dealMon").find("input[type='button']").attr("disabled","");
				$("#pageForm").attr("action","mon_realTimeAlarm.htm?method=find&&pageIndex="+pageIndex+"&&pageSize="+pageSize );
				if(result == "outOfLogin")
				{
					$("#logoutalert").val(1);
					location.href=pathUrl+"/common/logoutEmp.html";
					return;
				}
				else if(result!="error" && result!="fail")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_common_clcg"));
					submitForm();
				}
				else
				{
					alert(getJsLocaleMessage("ptjk","ptjk_common_clsb"));
				}
			});
}
function rtime()
{
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
}

function stime(){
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
}