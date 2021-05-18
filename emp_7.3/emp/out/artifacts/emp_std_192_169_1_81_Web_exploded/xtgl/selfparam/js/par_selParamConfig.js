function modify(t)
	{
	$('#modify').dialog({
		autoOpen: false,
		resizable: false,
		width:250,
	    height:200
	});
	$("#msg").children("xmp").empty();
	$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
		}
		
var dotype = "";		
function doAddConfig()
{
		$("#configDiv").attr("title",getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_1"));
	var title =getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_1");
	$("input").each(function(){ $("#paramValues").attr('disabled',''); });	
	$("#configDiv").dialog({
		autoOpen: false,
		title : title,
		height: 300,
		width: 350,
		resizable: false,
		modal:true
	
	});
	$("#configDiv").css("display","block");
	$('#configDiv').dialog("open");
	dotype = "add";
}	

var curpid = "";
function doEdit(pid)
{
	$("#configDiv").attr("title",getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_2"));
	var title =getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_2");
	var curparamValue =$.trim($("#paramValue"+pid).text());
	var curparamName =$.trim($("#paramName"+pid).text());
	var curMemo = $.trim($("#memo"+pid).text());
	curpid = pid; 
	$("#paramValues").val(curparamValue);
	$("#paramNames").val(curparamName);
	$("#memo").val(curMemo);
	$("input").each(function(){ $("#paramValues").attr('disabled','disabled'); });		
	$("#configDiv").dialog({
		autoOpen: false,
		title : title,
		height: 300,
		width: 350,
		resizable: false,
		modal:true
	
	});
	$("#configDiv").css("display","block");
	$("#configDiv").dialog("open");
	dotype = "edit";
}

//确定
function dook()
{
   if(dotype == "edit")
   {
	    var paramName =$.trim($("#paramNames").val());
	    var memo=$.trim($("#memo").val());
	    var dpid=$.trim($("#dpidh").val());
		if(paramName == "")
		{
		    alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_3"));
			$("#paramNames").focus();
			return;
		}
		if(paramName.indexOf("'")!=-1 || paramName.indexOf('"')!=-1)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_4"));
			$("#paramNames").focus();
			return;
		}
		if(memo.length>65)
		{
		   alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_5"));
		   $("#memo").select();
		   return;
		}
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$("#lgusername").val();
		$.post("par_selParam.htm",{method:"editConfig",paramName:paramName,memo:memo,pid:curpid,lgusername:lgusername,lgcorpcode:lgcorpcode}
			,function(result){
			if("true" == result)
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_40"));
				location.href =  "par_selParam.htm?method=findConfig&dpid="+dpid;
				$("#configDiv").dialog('close');
			}
			else if("nameExsist" == result)
			{
			    alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_6"));
				$("#paramNames").select();
			}
			else
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
			}
		});
	}else if(dotype == "add")
	{
		 var paramValue = $.trim($("#paramValues").val());
	     var paramName =$.trim($("#paramNames").val());
	     var memo=$.trim($("#memo").val());
	     var dpid=$.trim($("#dpidh").val());
		if(paramValue == "")
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_7"));
			$("#paramValues").focus();
			return;
		}
		if(paramName == "")
		{
		    alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_3"));
			$("#paramNames").focus();
			return;
		}
		if(paramValue.indexOf("'")!=-1 || paramValue.indexOf('"')!=-1)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_8"));
			$("#paramValues").focus();
			return;
		}
		if(paramName.indexOf("'")!=-1 || paramName.indexOf('"')!=-1)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_9"));
			$("#paramNames").focus();
			return;
		}
		if(memo.length>65)
		{
		   alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_5"));
		   $("#memo").select();
		   return;
		}
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$("#lgusername").val();
	    $.post("par_selParam.htm",{method:"add",dpid:dpid,paramValue:paramValue,paramName:paramName,memo:memo,lgcorpcode:lgcorpcode,lgusername:lgusername}
	    	,function(result){
			if("valueExsist" == result)
			{
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_11"));
				$("#paramValues").select();
			}
			else if("nameExsist" == result)
			{
			    alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_6"));
				$("#paramNames").select();
			}
			else if("true" == result)
			{
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_12"));
				location.href =  "par_selParam.htm?method=findConfig&dpid="+dpid;
			}else
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
				}
			});
	}
}

//取消
function docancel()
{
    if(dotype == "edit")
    {
		$("#paramValues").attr('value','');
		$("#paramNames").attr('value','');
		$("#memo").attr('value','');
    }
    $("#configDiv").dialog('close');
}

function doDel(pid)
{
	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_13")))
{
    var dpid=$.trim($("#dpidh").val());
    var lgcorpcode =$("#lgcorpcode").val();
	var lgusername =$("#lgusername").val();
	$.post("par_selParam.htm",{method:"delConfig",pid:pid,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
		if(result=="true")
		{
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
			location.href =  "par_selParam.htm?method=findConfig&dpid="+dpid;
		}else 
		{
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
			}
		});
	}
}