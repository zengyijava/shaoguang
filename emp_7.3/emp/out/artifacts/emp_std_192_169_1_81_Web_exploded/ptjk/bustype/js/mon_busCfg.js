//跳到添加主机页面
function toAdd()
{
	var lguserid = GlobalVars.lguserid;
	var lgcorpcode = GlobalVars.lgcorpcode;
	window.location.href= "mon_busCfg.htm?method=toAdd&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}

//显示列表状态下，模板名字详细信息
function showmsg(t,i)
{
	$('#arealist').dialog({
		autoOpen: false,
		resizable: false,
		width:250,
	    height:200
	});
	$("#msg").children("xmp").empty();
	$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
	if(i==1)
	{
		$('#arealist').dialog('option','title',getJsLocaleMessage("ptjk","ptjk_jkxq_yw_2"));
	}
	else if(i==2)
	{
		$('#arealist').dialog('option','title',getJsLocaleMessage("ptjk","ptjk_jkxq_yw_3"));
	}else if(i==3){
		$('#arealist').dialog('option','title',getJsLocaleMessage("ptjk","ptjk_jkxq_yw_4"));
	}else if(i==4){
		$('#arealist').dialog('option','title',getJsLocaleMessage("ptjk","ptjk_jkxq_yw_5"));
	}
	$('#arealist').dialog('open');
}

//显示告警条件
function show_notice(t)
{
	$('#notice').dialog({
		autoOpen: false,
		resizable: false,
		width:250,
	    height:200
	});
	$("#notice_msg").empty();
	$("#notice_msg").html($(t).children("label").html());

	$('#notice').dialog('option','title',getJsLocaleMessage("ptjk","ptjk_jkxq_yw_4"));

	$('#notice').dialog('open');
}




//启用
function ope(id){
	var lguserid = GlobalVars.lguserid;
	var lgcorpcode = GlobalVars.lgcorpcode;
	if(confirm(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_6"))){
		$.post("mon_busCfg.htm?method=changeStatus",{id : id,status:"1",lgcorpcode:lgcorpcode},
				function(result){
					if(result ="true" )
					{
						alert(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_7"));
						window.location.href="mon_busCfg.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
					}else{
						alert(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_8"));
					}
				});
	}
}
//关闭
function clo(id){
	var lguserid = GlobalVars.lguserid;
	var lgcorpcode = GlobalVars.lgcorpcode;
	if(confirm(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_9"))){
		$.post("mon_busCfg.htm?method=changeStatus",{id : id,status:"0",lgcorpcode:lgcorpcode},
				function(result){
					if(result ="true" )
					{
						alert(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_10"));
						window.location.href="mon_busCfg.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
					}else{
						alert(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_11"));
					}
				});
		}
	}
//修改
function modif(id){
	var lguserid = GlobalVars.lguserid;
	var lgcorpcode = GlobalVars.lgcorpcode;
	window.location.href="mon_busCfg.htm?method=toUpdate&id="+id+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}
//删除
function del(id){
	var lguserid = GlobalVars.lguserid;
	var lgcorpcode = GlobalVars.lgcorpcode;
	if(confirm(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_12"))){
		$.post("mon_busCfg.htm",{method : "delete",id : id,lgcorpcode:lgcorpcode},
				function(result){
					if(result ="true" )
					{
						alert(getJsLocaleMessage("ptjk","ptjk_common_sccg"));
					}else{
						alert(getJsLocaleMessage("ptjk","ptjk_common_scsb"));
					}
					window.location.href="mon_busCfg.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				});
		}
}

//弹出区域列表
function openAreaChoose() {
	//$('#areaDiv').attr('display','');
	$('#areaDiv').dialog('open');

}
//点击确定赋值处理
function doArea() {
	$('#areaDiv').dialog('close');
	fillAreaName();
}
//赋值处理
function fillAreaName(){
	var areaNameStr = "";
	var codeStr="";
	$('input[name="roleId"]:checked').each(function(){    
   		areaNameStr +=$(this).next().html()+",";
   		codeStr+=$(this).val()+",";
 	}); 
 	if(areaNameStr != ""){
 	    areaNameStr = areaNameStr.substring(0,areaNameStr.lastIndexOf(","));
 	    $("#areaName").attr("allAreaName",areaNameStr);
 	    $("#allAreaName").html(areaNameStr);
 	    var arr = areaNameStr.split(",");
 	    if(arr.length > 1){
 	    	areaNameStr = arr[0]+"...";
 	    }
 		$("#areaName").val(areaNameStr.replaceAll("&lt;","<").replaceAll("&gt;",">"));
 		$("#areaName").removeClass("fontColor");
 		$("#allAreas").val(codeStr);
 	}else{
 		$("#areaName").val(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_13"));
 		$("#areaName").attr("allRoleName","");
 		$("#allAreas").val("");
 	}
}

//时间控件 结束时间
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
	WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v});
}
//时间控件 开始时间
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
	WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:max});
}