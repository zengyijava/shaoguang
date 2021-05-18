
$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			noquot("#serName");
			noquot("#comments");
			noquot("#orderCode");
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

		function closeSerInfodiv()
		{
			window.parent.closeDiv();
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
		
		
		
		