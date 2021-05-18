

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
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_tzwhhqsb"));
						$("#subno").html("");
						return ;
					}
					else if(result == "noUsedSubNo")
					{
						//alert("系统没有可用的拓展尾号！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xtmykydtzwh"));
						$("#subno").html("");
						return ;
					}
					else if(result == "error"){
						//alert("通道号获取失败！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_tdhhqsb"));
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

		
		function idfModeclick(obj){
		    if(obj.value == '2'){
		    	$("#spSubno").css("display","none");
		    }else{
		    	$("#spSubno").css("display","inline");
		    }
		}
		
		
		
		