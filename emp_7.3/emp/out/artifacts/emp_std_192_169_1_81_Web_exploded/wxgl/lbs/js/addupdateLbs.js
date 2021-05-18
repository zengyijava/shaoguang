		

		$(function() {
			getLoginInfo("#getloginUser");
		});

		//地理位置采集
		function addupdateLbs(skiptype){
			$("#addupdatebtn").attr("disabled","disabled");
			 var account = $("#account").val();
			 if(account == null || account.length == 0){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_1"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }
			 var title = $.trim($("#title").val());
			 if(title == null || title.length == 0){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_2"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }
			 var keyword = $.trim($("#keyword").val());
			 if(keyword == null || keyword.length == 0){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_3"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }
			 var note = $("#note").val();
			 if(note.length > 216){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_4"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }
			 
			 var telephone = $("#telephone").val();
			 if(telephone == null || telephone.length == 0){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_5"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }else if(telephone.length<7||telephone.length>21){
	    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_6"));
			 	$("#addupdatebtn").attr("disabled",false);
			 	return;
			}else if (telephone != "" && !checkPhone(telephone)) {
	    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_6"));
			 	$("#addupdatebtn").attr("disabled",false);
			 	return;
			} 
			 
			 var address = $("#address").val();
			 if(address == null || address.length == 0){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_7"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }
			
			 var lng = $("#lng").val();
			 if(lng == null || lng.length == 0){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_8"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }
			 var lat = $("#lat").val();
			 if(lat == null || lat.length == 0){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_8"));
				 $("#addupdatebtn").attr("disabled",false);
				 return;
		     }
			 var prompt;
			 var pid;
			 if(skiptype == "update"){
				 prompt = getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_9");
				 pid = $("#pid").val();
			 }else if(skiptype == "add"){
				 prompt =getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_10");
			 }else{
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_11"));
				 return;
			 }
			 var lgcorpcode = $("#lgcorpcode").val();
			 
			 var pathUrl = $("#pathUrl").val();
				$.post(pathUrl+"/weix_lbsManager.htm",{
				    method: "addupdateLbs",
				    account:account,
				    title : title,
				    keyword:keyword,
				    note:note,
				    address:address,
				    telephone:telephone,
				    lat:lat,
				    lng:lng,
				    lgcorpcode :lgcorpcode,
				    skiptype:skiptype,
				    pid:pid,
				    isAsync: "yes"
					},function(returnmsg){
						 $("#addupdatebtn").attr("disabled",false);
						if (returnmsg == "outOfLogin") {
							window.location.href = pathUrl + "/common/logoutEmp.html";
							return;
						}else if(returnmsg == "success"){
							$("#addupdatebtn").attr("disabled","disabled");
							$("#searchbtn").attr("disabled","disabled");
							alert(prompt+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_12"),2);
							setTimeout('back()',1500); 
							return;
						}else if(returnmsg == "fail"){
							alert(prompt+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_13"));
							return;
						}else if(returnmsg == "error"){
							alert(prompt+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_14"));
							return;
						}else if(returnmsg == "paramsIsNull"){
							alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_15"));
							return;
						}else{
							alert(prompt+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_13"));
							return;
						}
					});
		}
		//返回
		function back(){
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			//doGo(pathUrl + "/weix_lbsManager.htm?method=find&lgcorpcode="+lgcorpcode);
			location.href = pathUrl + "/weix_lbsManager.htm?method=find&lgcorpcode="+lgcorpcode;
			return;
		}
		
		function checkPhone(phone)
		{
			var phoneReg = /^1[0-9]{10}$/;
			if(phone==null||phone.length<7||phone.length>21){
				return false;
			}
			var reg = /^(\+|00)?(\d+)$/;
			var group = phone.match(reg);
			if(group!=null&&group.length>0){
				if(!group[1]){
					return phoneReg.test(group[2]);
				}else{
					if(/^86\d+/.test(group[2])){
						return phoneReg.test(group[2].substring(2));	
					}
				}
			}else{
				return false;
			}
			return true;
		}