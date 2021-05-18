function doCancelEdit(obj){
	parent.doCancel(obj);
}


//新建
function setHostMonNet()
{
	var pathUrl=$("#pathUrl").val();
	if(validate()){
		$("body").find("input[type='button']").attr("disabled","disabled");
		var monphone=$("#monphone").val();
		var monemail = $("#monemail").val();
		if(monemail==" "){monemail="";}
		$.ajax({
			url:"mon_webHostMonNet.htm?method=checkPhone",
			method:"POST",
			async:true,
			data:{
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
					alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_1"));
					$("#btn").find("input[type='button']").attr("disabled","");
				}
				else if(!monemail.length==0 &&!checkEmail(monemail)){
					alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_2"));
					$("#btn").find("input[type='button']").attr("disabled","");
				}
				else
				{
					$.post("mon_webHostMonNet.htm",
							{
								method:"setNetWarn",
								monphone:monphone,
								monemail:monemail,
								isAsync:"yes"},
							function(result){
								if(result == "outOfLogin")
								{
									$("#logoutalert").val(1);
									location.href=pathUrl+"/common/logoutEmp.html";
									return;
								}
								else if(result=="false")
								{
									alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_3"));
									$("#btn").find("input[type='button']").attr("disabled","");
									doCancelEdit('#setDiv');
								}
								else if(result=="true")
								{
									alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_4"));
									$("#btn").find("input[type='button']").attr("disabled","");
									doCancelEdit('#setDiv');
								}
								
							});
				}
			}
			
		});
		
		
	}
}



function changestate(i)
{
	var ks=$.trim($("#gateState"+i).attr("value"));
	$.post("mon_webHostMonNet.htm?method=changeState",{monid:i,monstatus:ks},function(result){
        if (result == "true") {
			$("#gateState"+i).empty();
			if(ks == 1)
			{
			    $("#gateState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("ptjk","ptjk_wljk_web_5")+"</option>");
			    $("#gateState"+i).append("<option value='0' >"+getJsLocaleMessage("ptjk","ptjk_wljk_web_6")+"</option>");
			}
			else
			{
				$("#gateState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("ptjk","ptjk_wljk_web_6")+"</option>");
				$("#gateState"+i).append("<option value='1' >"+getJsLocaleMessage("ptjk","ptjk_wljk_web_5")+"</option>");
			}
			var $select = $("#gateState"+i);
			changeEmpSelect($select,80);
			$select.next().hide();
			$select.prev().html($select.find('option:selected').text());
			alert(getJsLocaleMessage("ptjk","ptjk_common_xgcg"));
		}else{
			alert(getJsLocaleMessage("ptjk","ptjk_common_xgsb"));
		}		
	});
}




function validate(){
	//判断手机号码格式
	var phone=$("#monphone").val();
	
	//告警手机号为空 则 进行确认
	if(phone==0){
		alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_7"));
		return false;
	}

	if(!validatePhones($("#monphone"))){
		return false;
	}
	
	
	//告警邮箱为空 则 进行确认
	var email=$("#monemail").val();
	if(email==" "){email="";}
	if(email.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_wljk_web_8"))){
		return false;
	}
	return true;
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

