
$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			//floatingRemind("serNameTd","只对应一个业务逻辑");
			floatingRemind("serNameTd",getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_zdyygywlj"));
			//floatingRemind("orderCodeTd","系统识别的字符，只对应一个业务名称");
			floatingRemind("orderCodeTd",getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_xtsbdzf"));
			//floatingRemind("orderCodeTypeTd","指令和参数：上行内容格式为“指令+分隔符号+参数”<br/>仅指令：上行内容只有指令");
			floatingRemind("orderCodeTypeTd",getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_zlhcssxnrgsw"));
			//floatingRemind("splStr","分隔指令和参数的符号");
			floatingRemind("splStr",getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_fgzlhcsdfh"));
			
			if(orderTypeStr==0){
				//var obj=$('#idfModeClose').val();
				var obj=$('#orderTypeIn');
		        obj['value'] = 0; 
		        orderTypeclick(obj);
			}else{
		        orderTypeclick(1);
			}
			
			if(identifyModeStr==2){
				//var obj=$('#idfModeClose').val();
				var obj=$('#idfModeClose');
		        obj['value'] = 2; 
				idfModeclick(obj);
			}else{
				var obj=$('#idfModeOpen');
		        obj['value'] = 1; 
				idfModeclick(obj);
			}
		});

function getGateNumber(){
    var spUser = $("#spUser").val();
    if(spUser == null || spUser == ""){
    	return ;
    }
	$.post("eng_moService.htm", 
		{
			method : "getSubNo",
			lgcorpcode : $("#lgcorpcode").val(),
			spUser: spUser
		}, function(result) {
			if(result == "noSubNo"){
				//alert("拓展尾号获取失败！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tzwhhqsb"));
				$("#subno").html("");
				return ;
			}
			else if(result == "noUsedSubNo")
			{
				//alert("系统没有可用的拓展尾号！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_xtmykydtzwh"));
				$("#subno").html("");
				return ;
			}
			else if(result == "error"){
				//alert("通道号获取失败！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tdhhqsb"));
				$("#subno").html("");
				return ;
			}
			var data = eval("("+result+")");
		
			
			$("#subno").html(data.subNo);
			var sendFlag=(data.sendFlag).split("&");
			if(sendFlag[0]=="false" || sendFlag[1]=="false" || sendFlag[2]=="false"){
				$("#subno").css("color","red");
			}else{
				$("#subno").css("color","blue");
			}
			$("#sendFlag").val(data.sendFlag);
		});
}

function show(){
	getGateNumber();
}

function serSub()
{
	var userID = $("#spUser").val();
	$.post($("#pathUrl").val()+"/eng_moService.htm?method=checkMoUrl",{userID:userID},function(result){
		if ("nouserid" == result)
		{
			//alert("SP账号不存在！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_spzhbcz"));
			return;
		}else if("nomourl" == result)
		{
			//alert("SP账号未绑定上行URL！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_spzhwbdsxurl"));
			return;
		}else if("moconnfail"==result)
		{
			//alert("SP账号绑定的上行URL不可用，请检查配置的上行URL是否正确！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_spzhbddsxurlbky"));
			return;
		}
		else if("checksuccess"==result)
		{
			serOk();
		}
	});
}

function idfModeclick(obj){
    if(obj.value == '2'){
    	$("#spSubno").css("display","none");
    }else{
    	$("#spSubno").css("display","");
    }
}

function orderTypeclick(obj){
    if(obj.value == '0'){
    	$("#splDiv").css("display","none");
    }else{
    	$("#splDiv").css("display","");
    }
}




