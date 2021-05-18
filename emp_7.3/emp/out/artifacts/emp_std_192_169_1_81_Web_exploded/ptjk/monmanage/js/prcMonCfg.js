//跳到添加主机页面
function toAdd()
{
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	window.location.href= "mon_prcMonCfg.htm?method=toAdd&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}

//跳到主机监控设置页面
function toEdit(proceid)
{
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	$.post("mon_prcMonCfg.htm",
			{method:"checkDEL",proceid:proceid,isAsync:"yes"},
			function(result){
				if(result=="no")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_1"));
					location.reload();
				}else{
					window.location.href= "mon_prcMonCfg.htm?method=toEdit&proceid="+proceid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				}
			
			});	

}


//--------mon_editPrc.jsp调用的js--------
//返回
function doreturn()
{
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	window.location.href= "mon_prcMonCfg.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}
//修改
function editPrc()
{
	if(validate()){
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		var monphone=$("#monphone").val();
		var monemail=$("#monemail").val();
		if(monemail==" "){monemail="";}
		
		$("#rContent").find("input[type='button']").attr("disabled","disabled");
		$.ajax({
			url:"mon_gateAcctMonCfg.htm?method=checkPhone",
			method:"POST",
			async:true,
			data:{
				lgcorpcode:lgcorpcode,
				lguserid: lguserid,
				monphone:monphone,
				isAsync:"yes"
			},
			success:function(result){
				if(result == "outOfLogin")
				{
					$("#logoutalert").val(1);
					location.href=pathUrl+"/common/logoutEmp.html";
					return;
				}
				else if(result=="error"||result=="phoneError")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_4"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}else if(!monemail.length==0 &&!checkEmail(monemail)){
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_5"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}
				else
				{
					$("#editForm").submit();
				}
			}
			
		});
	}
}

//验证邮箱是否有效
function checkEmail(email_str)
{
	var arr = [ "ac", "com", "net", "org", "edu", "gov", "mil", "ac\.cn",
		"com\.cn", "net\.cn", "org\.cn", "edu\.cn" ];
	var temp_arr = arr.join("|");
	// reg
	var reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
	var reg = new RegExp(reg_str);
	if (reg.test(email_str)) {
		return true;
	}else{
		return false;
	}
}

//--------mon_addPrc.jsp调用的js--------
//新建
function addPrc()
{
	var pathUrl=$("#pathUrl").val();
	var procetype = $("#procetype").val();
	var procname = $("#procename").val();
	//var procname = $("#procetype option:selected").text();
	var lgcorpcode = $("#lgcorpcode").val();
	var lguserid = $("#lguserid").val();
	var gatewayid = $("#gatewayid").val();
	var servernum = $("#servernum").val()||'';
	if(validate()){
		$("#rContent").find("input[type='button']").attr("disabled","disabled");
		$.post("mon_prcMonCfg.htm",
			{method:"checkPrc",lgcorpcode:lgcorpcode,gatewayid:gatewayid,lguserid: lguserid,procetype:procetype,servernum:servernum,isAsync:"yes"},
			function(result){
				if(result == "outOfLogin")
				{
					$("#logoutalert").val(1);
					location.href=pathUrl+"/common/logoutEmp.html";
					return;
				}
				else if(result=="error")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_cx_2")+procname+getJsLocaleMessage("ptjk","ptjk_jkgl_cx_3"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}
				else if(result=="no")
				{
					var lguserid = $("#lguserid").val();
					var lgcorpcode = $("#lgcorpcode").val();
					var monphone=$("#monphone").val();
					var monemail = $("#monemail").val();
					if(monemail==" "){monemail="";}
					
					$.ajax({
						url:"mon_gateAcctMonCfg.htm?method=checkPhone",
						method:"POST",
						async:true,
						data:{
							lgcorpcode:lgcorpcode,
							lguserid: lguserid,
							monphone:monphone,
							isAsync:"yes"
						},
						success:function(result){
							if(result == "outOfLogin")
							{
								$("#logoutalert").val(1);
								location.href=pathUrl+"/common/logoutEmp.html";
								return;
							}
							else if(result=="error"||result=="phoneError")
							{
								alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_4"));
								$("#rContent").find("input[type='button']").attr("disabled","");
							}
							else if(!monemail.length==0 &&!checkEmail(monemail)){
								alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_5"));
								$("#rContent").find("input[type='button']").attr("disabled","");
							}
							else
							{
								$("#addForm").submit();
							}
						}
						
					});
				}
				else if(result=="have")
				{
					alert(procname+getJsLocaleMessage("ptjk","ptjk_jkgl_cx_4")+procname+getJsLocaleMessage("ptjk","ptjk_jkgl_cx_5"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}
				else if(result=="haveGtId")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_cx_6")+gatewayid+getJsLocaleMessage("ptjk","ptjk_jkgl_cx_7"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}
			});
	}
}
function del(proceid)
{
	if(!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_cx_8"))){
		return;
	}
	if(!proceid){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_8"));
		return;
	}
	$.post("mon_prcMonCfg.htm",
	{method:"delete",id:proceid,isAsync:"yes"},
	function(result){
		if(result=="error")
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_cx_9"));
		}
		else 
		{
			window.location.href= "mon_prcMonCfg.htm?method=find";
		}
	});
}
function validate(){
	var isContinue = true;
	//判断非空项是否为空
	$(".xinhao:visible").each(function(){
		var $text=$(this).siblings(".input_bd");
		if(!$text.val()||$text.val().length<=0){
			isContinue = false;
			//alert($(this).parent().prev().text().replace(/[:： ]/g,'')+getJsLocaleMessage("ptjk","ptjk_jkgl_zj_10"));
			alert($(this).parent().prev().text().replace(/[:：]/g,'')+getJsLocaleMessage("ptjk","ptjk_jkgl_zj_10"));
			return false;
		}
	});
	//如果存在非空项为空则终止
	if(!isContinue){
		return false;
	}
	if($("#mqurl").length>0)
	{
		var mqurl = $("#mqurl").val();
		var mqurlold = $("#mqurlold").val();
		if((mqurl!=mqurlold) &&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_cx_10")))
		{
			return false;
		}
		
	}
	
	//判断手机号码格式
	var phone=$("#monphone").val();
	if(!validatePhones($("#monphone"))){
		return false;
	}
	
	//判断CPU占用比率告警阀值
	var cpubl=$("#cpubl").val();
	if(cpubl.length>0&&cpubl>100){
		isContinue = false;
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_4"));
		return false;
	}
	//告警手机号为空 则 进行确认
	if(phone.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_9"))){
		return false;
	}
	
	//告警邮箱为空 则 进行确认
	var email=$("#monemail").val();
	if(email==" "){email="";}
	if((email==null || email.length==0) && !confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_3"))){
		return false;
	}
	
	return true;
}
//选择spgate程序则要配置网关编号
function getGateWayid()
{
	var procetype = $("#procetype").val();
    if(procetype == '5000' || procetype == '5800'){
        $('#server-tr').show();
        $('#gate_id_tr').hide();
    }else{
        $('#server-tr').hide();
        $('#gate_id_tr').show();
        var gw=$("#procetype option:selected").val();
        if(gw=="5200"){
			$.ajax({
				url:"mon_prcMonCfg.htm?method=getgw",
				method:"POST",
				async:true,
				data:{para:'1'
			},
			success:function(jsonObject){
				$("select[name=gatewayid] option").each(function(){
					$(this).remove();
				});	
				var paramArray = eval("("+jsonObject+")");
				for(var i=0,len=paramArray.length;i<len;i++){
					$("select[name=gatewayid]").append("<option value='"+paramArray[i].gwno+"'>"+paramArray[i].gwno+"</option>");
				}

			}
			
		});
				

        }else if(gw=="5300"){
			$.ajax({
				url:"mon_prcMonCfg.htm?method=getShost",
				method:"POST",
				async:true,
				data:{para:'1'
			},
			success:function(jsonObject){
				$("select[name=gatewayid] option").each(function(){
					$(this).remove();
				});	
				var paramArray = eval("("+jsonObject+")");
				for(var i=0,len=paramArray.length;i<len;i++){
					$("select[name=gatewayid]").append("<option value='"+paramArray[i].gwno+"'>"+paramArray[i].gwno+"</option>");
				}

			}
			
		});
        }
    }

}
//跳到告警历史页面
function toHostMonCfg(menuCode)
{
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	window.parent.openNewTab(menuCode,pathUrl+"/mon_hostMonCfg.htm?method=toAdd&hrefType=1&lgcorpcode="+lgcorpcode+"&prcId="+prcId);
}