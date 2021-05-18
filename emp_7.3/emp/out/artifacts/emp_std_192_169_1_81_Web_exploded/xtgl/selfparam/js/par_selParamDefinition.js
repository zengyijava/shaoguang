var dotype = "";	
function doAdd()
{		
	$("#definitionDiv").attr("title",getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_14"));
	var title = getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_14");
	$("select").each(function(){ $("#param").attr('disabled',''); });	
	changeparam();
	$("#definitionDiv").dialog({
		autoOpen: false,
		title : title,
		height: 350,
		width: 455,
		resizable: false,
		modal:true
	});
	$("#ParamSubName").val("");
	$("#memo").val("");
	$("#definitionDiv").css("display","block");
	$('#definitionDiv').dialog("open");
	dotype ="add";
}
var curParamSubName = "";
var curpid = "";
function doEdit(pid,paramIndex)
{
	$("#definitionDiv").attr("title",getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_15"));
	var title = getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_15");
	$("#param").val(paramIndex);
	var curParam = $.trim($("#param"+pid).text());
	var curParamSubNum =$.trim($("#ParamSubNum"+pid).text());
	var curParamSubSign =$.trim($("#ParamSubSign"+pid).text());
	curParamSubName =$.trim($("#ParamSubName"+pid).text());
	curpid = pid;
	var curMemo = $.trim($("#memo"+pid).text());
    $("#param option[text='"+curParam+"']").attr("selected", true); 
	$("#ParamSubNum").val(curParamSubNum);
	$("#ParamSubSign").val(curParamSubSign);
	$("#ParamSubName").val(curParamSubName);
	$("#memo").val(curMemo);
	$("select").each(function(){ $("#param").attr('disabled','disabled'); });			
	$("#definitionDiv").dialog({
		autoOpen: false,
		title : title,
		height: 350,
		width: 400,
		resizable: false,
		modal:true
	});
	$("#definitionDiv").css("display","block");
	$("#definitionDiv").dialog("open");
	dotype = "edit";
}
//确定
function dook()
{
   if(dotype == "edit")
   {
		 var param = $.trim($("#param").val());
		 var ParamSubNum = $.trim($("#ParamSubNum").val());
         var ParamSubSign =$.trim($("#ParamSubSign").val());
         var ParamSubName=$.trim($("#ParamSubName").val());
         var memo=$.trim($("#memo").val());
         if(ParamSubNum == "")
         {
         	alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_16")+"Param"+param+getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_17"));
            return;
         }
		if(ParamSubName == "")
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_18"));
			$("#ParamSubName").focus();
			return;
		}
		if(ParamSubName.indexOf("'")!=-1 || ParamSubName.indexOf('"')!=-1)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_19"));
			$("#ParamSubName").focus();
			return;
		}
		if(memo.length>65)
		{
		   alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_5"));
		   $("#memo").select();
		   return;
		}
		var reg=/'/g;
		if(reg.test(memo)){
			alert(getJsLocaleMessage("xtgl","xtgl_zdycs_yinhao"));
			return;
		}
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$("#lgusername").val();
        $.post("par_selParam.htm",{method:"editdefinition",param:param,ParamSubNum:ParamSubNum,ParamSubSign:ParamSubSign,ParamSubName:ParamSubName,curParamSubName:curParamSubName,memo:memo,pid:curpid,lgusername:lgusername,lgcorpcode:lgcorpcode}
        	,function(result){
			if("nameExsist" == result)
			{
			    alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_20"));
				$("#ParamSubName").select();
			}else if("true" == result)
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_40"));
				location.href = location.href;
				$("#definitionDiv").dialog('close');
			}else
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
			}
		});		
	}
	else if(dotype == "add")
	{
		var param = $.trim($("#param").val());
		var ParamSubNum = $.trim($("#ParamSubNum").val());
        var ParamSubSign =$.trim($("#ParamSubSign").val());
        var ParamSubName=$.trim($("#ParamSubName").val());
        var memo=$.trim($("#memo").val());
        if(ParamSubNum == "")
        {
        	alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_16")+"Param"+param+getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_17"));
           return;
        }
		if(ParamSubName == "")
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_18"));
			$("#ParamSubName").focus();
			return;
		}
		if(ParamSubName.indexOf("'")!=-1 || ParamSubName.indexOf('"')!=-1)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_19"));
			$("#ParamSubName").focus();
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
        $.post("par_selParam.htm",{method:"adddefinition",param:param,ParamSubNum:ParamSubNum,ParamSubSign:ParamSubSign,ParamSubName:ParamSubName,memo:memo,lgusername:lgusername,lgcorpcode:lgcorpcode}
        	,function(result){
                     if("nameExsist" == result)
			{
			    alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_20"));
				$("#ParamSubName").select();
			}
			else if("true" == result)
			{
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_zdcsgl_12"));
				location.href = location.href;
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
    $("#ParamSubName").attr('value','');
    $("#memo").attr('value','');
  }
  $("#definitionDiv").dialog('close');
}

function changeparam()
{
    $("#ParamSubNum").attr("value","");
    var param = $.trim($("#param").val());
    var lgcorpcode =$("#lgcorpcode").val();
	var lgusername =$("#lgusername").val();
    $.post("par_selParam.htm",{method:"changeparamsign",param:param,lgusername:lgusername,lgcorpcode:lgcorpcode},function(result){
	    /*if(result=="noadd")
        {
            alert("同一个参数类型只允许添加两个分段值!");
        }*/	
        if(result=="1")
        {
            $("#ParamSubNum").attr("value",'1');
        }
        else if(result=="2")
        {
           $("#ParamSubNum").attr("value",'2');
        }
        	    
    });
}
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