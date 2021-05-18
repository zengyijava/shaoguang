function plchongZhi(){
	var $buttons = $("#addBalanceAll input:button");
	$buttons.attr("disabled",true);
	
	var supDepId = $("#supDepId").val();
	var isDefault="";
	if($("#isDefault").attr('checked')==true)
	{
		isDefault = 1;
	}
	var lguserId = GlobalVars.lguserid;
	var balanceDepIds = "";
	var smsCounts = "";
	var mmsCounts = "";
	var sumSms = $("#smsSum").html().replace(/[^\d]/g,'');
	var sumMms = $("#mmsSum").html().replace(/[^\d]/g,'');
	if(sumSms == null || sumSms =="")
	{
		alert("短信充值数目不能为空！");
		$buttons.attr("disabled",false);
		return false;
	}
	if(sumMms == null || sumMms =="")
	{
		alert("彩信充值数目不能为空！");
		$buttons.attr("disabled",false);
		return false;
	}
	if(sumSms == 0 && sumMms == 0)
	{
		alert("请输入充值数目！");
		$buttons.attr("disabled",false);
		return false;
	}
	$('input[name="balanceDepId"]').each(function(){
		if($(this).val() == "" || $(this).val() == null){
			$(this).val(0);
		}
		balanceDepIds += $(this).val()+",";
	});
	$('input[name="smsCount"]').each(function(){
		if($(this).val() == "" || $(this).val() == null){
			$(this).val(0);
		}
		smsCounts += $(this).val()+",";
	});
	$('input[name="mmsCount"]').each(function(){
		if($(this).val() == "" || $(this).val() == null){
			$(this).val(0);
		}
		mmsCounts += $(this).val()+",";
	});
	
	if (balanceDepIds != "")
	{
		balanceDepIds = balanceDepIds.toString().substring(0, balanceDepIds.lastIndexOf(','));
	}
	if (smsCounts != "")
	{
		smsCounts = smsCounts.toString().substring(0, smsCounts.lastIndexOf(','));
	}
	if (mmsCounts != "")
	{
		mmsCounts = mmsCounts.toString().substring(0, mmsCounts.lastIndexOf(','));
	}
	
	   var path = 'cha_balanceMgr.htm';
	   $.post(path,
		   {method:"addBalanceAll",
		   lguserId:lguserId,
		   supDepId:supDepId,
		   balanceDepIds:balanceDepIds,
		   smsCounts:smsCounts,
		   mmsCounts:mmsCounts,
		   lgcorpcode:GlobalVars.lgcorpcode,
		   isDefault:isDefault
		   	},
		   	function(result){
		       if(result == "0"){
		           alert("充值成功！");
		     	   window.parent.location.href = "cha_balanceMgr.htm?method=find&lgguid="+GlobalVars.lgguid+"&operatePageReturn=true&operatorBalancePri=notDefaultBalancePri";
		      
		       }
		       else if(result == "-1")
		       {
		    	   alert("充值失败！");
		       }
		       else if(result == "-2")
		       {
		    	   alert("充值成功，设置默认值失败!");
		     	   window.parent.location.href = "cha_balanceMgr.htm?method=find&lgguid="+GlobalVars.lgguid+"&operatePageReturn=true&operatorBalancePri=notDefaultBalancePri";
		    	   
		       }
		       else if(result == "-3")
		       {
		    	   alert("充值失败，机构下没有可用余额！");		    	  
		       }
		       else if(result == "-4")
		       {
		    	   alert("充值失败，充值数目大于可分配余额！");
		       }
		       else if(result == "-5")
		       {
		    	   alert("充值失败，获取机构余额记录失败！");
		       }
		       else{
		    	   alert("充值失败！");
		       }
		       $buttons.attr("disabled",false);
	   });
}


function plquXiao(){
	window.parent.$('#addBalanceAllDiv').dialog('close');
	window.parent.$("#addBalanceAllFrame").attr("src","");
}

//设置所有子机构短信充值条数
//短信
function checkSmsCount(){
	var smsCount = $("#addsmsCountAll").val();
	var reg = /^[1-9]\d*|0$/;
	if(!reg.test(smsCount))
	{
		$('input[name="smsCount"]').val(0);
		$("#smsSum").html(0+"&nbsp;条");
		if(smsCount.replace(/[^\d]/g,'') != "")
		{
			$("#addsmsCountAll").val(parseInt(smsCount.replace(/[^\d]/g,''),10));
			$('input[name="smsCount"]').val(parseInt(smsCount.replace(/[^\d]/g,'')));
			sumSmsCount();
		}
		else
		{
			$("#addsmsCountAll").val('');
		}
		return false;
	}
	else
	{
		$('input[name="smsCount"]').val(parseInt(smsCount,10));
		$("#addsmsCountAll").val(parseInt(smsCount,10));
		sumSmsCount();
	}
}
//彩信
function checkMmsCount(){
	var mmsCount = $("#addmmsCountAll").val();
	var reg = /^[1-9]\d*|0$/;
	if(!reg.test(mmsCount))
	{
		$('input[name="mmsCount"]').val(0);
		$("#mmsSum").html(0+"&nbsp;条");
		if(mmsCount.replace(/[^\d]/g,'') != "")
		{
			$("#addmmsCountAll").val(parseInt(mmsCount.replace(/[^\d]/g,''),10));
			$('input[name="mmsCount"]').val(parseInt(mmsCount.replace(/[^\d]/g,'')));
			mumSmsCount();
		}
		else
		{
			$("#addmmsCountAll").val('');
		}
		return false;
	}
	else
	{
		$('input[name="mmsCount"]').val(parseInt(mmsCount,10));
		$("#addmmsCountAll").val(parseInt(mmsCount,10));
		mumSmsCount();
	}
}

//求和
//短信
function sumSmsCount(){
	var smsCounts = 0;
	var reg = /^[1-9]\d*|0$/;	
	$('input[name="smsCount"]').each(function(){
		if(!reg.test($(this).val()))
		{
			if($(this).val().replace(/[^\d]/g,'') != "")
			{
				$(this).val(parseInt($(this).val().replace(/[^\d]/g,''),10));
			}
			else
			{
				$(this).val('');
			}
		}
		else
		{
			$(this).val(parseInt($(this).val(),10));
		}
		smsCounts += Number($(this).val());
	});
	$("#smsSum").html(smsCounts+"&nbsp;条");
}

//彩信
function mumSmsCount(){
	var mmsCounts = 0;
	var reg = /^[1-9]\d*|0$/;
	$('input[name="mmsCount"]').each(function(){
		if(!reg.test($(this).val()))
		{
			if($(this).val().replace(/[^\d]/g,'') != "")
			{
				$(this).val(parseInt($(this).val().replace(/[^\d]/g,''),10));
			}
			else
			{
				$(this).val('');
			}
		}
		else
		{
			$(this).val(parseInt($(this).val(),10));
		}
		mmsCounts += Number($(this).val());
	});
	$("#mmsSum").html(mmsCounts+"&nbsp;条");
}
