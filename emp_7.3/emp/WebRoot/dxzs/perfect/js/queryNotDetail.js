String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
var contentLen = 965;
function noticEblur(ob) {
	if (ob.is("#sendContent")) {
		ob.val($.trim(ob.val()));
		len(ob);
		if ($.trim(ob.val()) == "") {
			//alert("请输入完美通知发送内容！");
			$("#reStartSendDia").find('#isOk').attr("value","0");
		} else if (ob.val().length > contentLen) {
			//alert("完美通知发送内容过长，应少于965个字符！");
			$("#reStartSendDia").find('#isOk').attr("value","1");
		} else {
			$("#reStartSendDia").find('#isOk').attr("value","2");
		}
	}
}

// 统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	var huiche = content.length - content.replaceAll("\n", "").length;
	$("#reStartSendDia").find('#strlen').html((content.length + huiche));
};
// 短信内容键盘事件监听统计字数
function synlen() {
	$("#reStartSendDia").find("textarea[name='sendContent']").keyup(function() {
		var content = $(this).val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > contentLen) {
			$(this).val(content.substring(0, contentLen - huiche));
		}
		len($(this));
	});
}
	
  //选中
function checkAlls(e)    
	{  
		var a = document.getElementsByName("checklist");    
	var n = a.length;    
	for (var i=0; i<n; i=i+1) {
		a[i].checked =e.checked;   
	}   
}

//设置短信最长位数
function setContentMaxLen()
{
	var spUser = $("#spUser").val();
  //$.post("ssm_comm.htm",{method : "getSpGateConfig",spUser : spUser},
	$.post("ssm_comm.htm?method=getSpGateConfig&spUser="+spUser,{},
		function(infoStr){
			if(infoStr !="error" && infoStr.startWith("infos:"))
			{
				var infos = infoStr.replace("infos:","").split("&");
				//完美通知为965
				contentLen = infos[4] - 25;
				//完美通知暂不支持英文短信发送
				if(contentLen == 675)
				{
					contentLen = 325;
				}
				$('#maxLen').html("/"+contentLen);
			}
		}
	);
}

//定义startWith方法
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	return false;
	if(this.substr(0,str.length)==str)
	return true;
	else
	return false;
	return true;
}

//重新发送
var userCount = 0;
function reStartSend(taskid){
	setContentMaxLen();
	userCount = 0;
		var items = "";
	$('input[name="checklist"]:checked').each(function(){	
		items += $(this).val()+",";
		userCount = userCount + 1;
	});
	if (items != "")
	{
		$("#itemUser").val("");
		items = items.toString().substring(0, items.lastIndexOf(','));
		$("#itemUser").val(items);
	}else{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_204'));
		return;
	}
	$("#noticecount").val(userCount);
	$("#userCount").empty();
	$("#userCount").html("[ "+userCount+" ]");
 	$("#reStartSendDia").css("display","block");
	$('#reStartSendDia').dialog({
		autoOpen: false,
		height: 330,
		width: 380,
		modal:true,
		close:function(){
			doNotReStart();
		}
	});
 	$(".ui-dialog-titlebar-close").hide();
 	 $('#reStartSendDia').dialog('open');
 	checkFee();
}
			
//重新发送完美通知
function RestartSendPre(taskid){
 	$("#queren").attr("disabled","disabled");
 	//发送的用户
	var items = $("#itemUser").val();
	//发送内容
	var reStartContent = $.trim($('#sendContent').val());
	//是否完美通知内容符合要求
	var isOk = $("#isOk").val();
	//登录者GUID
	var lgguid = $("#lgguid").val();
	//发送次数
	var sendCount = $("#sendCount").val();
	//时间间隔
	var sendInterval = $("#sendInterval").val();
	//获取子号
	var userSubno = $("#userSubno").val();
	//发送账号
	var spUser = $("#spUser").val();
	
	var noticecount = $("#noticecount").val();
	
	if(isOk == 0 || isOk == 1){
		if(isOk == 0){
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_160'));
			$("#queren").attr("disabled",false);
		}else{
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_161')+contentLen+getJsLocaleMessage('dxzs','dxzs_ssend_alert_162'));
			$("#queren").attr("disabled",false);
		}
		return ;
	}
	$.post("per_sendNoticeSMS.htm",{
			method:"reStartSendPreNotice",
			reStartContent : reStartContent.replaceAll("\n","\r\n") ,
			sendInterval : sendInterval,
			memcount:userCount,
			sendCount : sendCount,
			lgguid:lgguid,
			taskid:taskid,
			items:items,
			userSubno:userSubno,
			noticecount:noticecount,
			spUser:spUser
 			},function(result){
						if("" == result){
		   					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_205'));
		   					$("#queren").attr("disabled",false);
		   					return;
		   				}else if(result.indexOf("html") > 0){
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_201'));
			    			window.location.href=window.location.href;
			    		    return;
			    		}else if(result == "errer"){
			    			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_205'));
		   					$("#queren").attr("disabled",false);
		   					return;
			    		}
		   				var array = result.split("&");
		   				var returnType = array[0];
		   				var returnMsg =  array[1];
		   				if(returnType == "stage1"){
		   					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_165'));
		   					$("#queren").attr("disabled",false);
	 	    			 	return;	
		   				}
		   				else if(returnType == "stage2"){
		   				 	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+":\n    "+returnMsg+"\n "+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
		   				 	$("#queren").attr("disabled",false);
			   			 	return;
   						}else if(returnType == "stage5")
   						{
   							var spArr = returnMsg.split("_");
				   			var errerCode = spArr[0];
				   			var errerCodeDetial =  spArr[1];
				   			var spErrText = "";
				   			if("nomourl" == errerCodeDetial){
				   				spErrText = getJsLocaleMessage('dxzs','dxzs_ssend_alert_166');
				   			}else if("norpturl" == errerCodeDetial){
				   				spErrText = getJsLocaleMessage('dxzs','dxzs_ssend_alert_167');
				   			}else if("moconnfail" == errerCodeDetial){
				   				spErrText = getJsLocaleMessage('dxzs','dxzs_ssend_alert_168');
				   			}else if("rptconnfail" == errerCodeDetial){
				   				spErrText = getJsLocaleMessage('dxzs','dxzs_ssend_alert_169');
				   			}else
				   			{
				   				spErrText = getJsLocaleMessage('dxzs','dxzs_ssend_alert_170');
				   			}
				   			var showsptext = getJsLocaleMessage('dxzs','dxzs_ssend_alert_206')+$("#spUser").val()+""+spErrText+"！";
   							alert(showsptext);
   							$("#queren").attr("disabled",false);
			   			 	return;
   						}else{
	   						if("enoughSubno" == returnMsg){
								alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_92'));
								$("#queren").attr("disabled",false);
								return;
							 }else if("notSubno" == returnMsg){
								alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_91'));
								$("#queren").attr("disabled",false);
								return;
							 }else{
								var arr = returnMsg.split("_");
				   				var errerCode = arr[0];
				   				var subno =  arr[1];
				   				if(userSubno == null || "" == userSubno){
				   					$("#userSubno").val(subno);
				   				}
							 	if(returnType == "stage3"){
						 			 if("2" == errerCode){
											alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_172'));
											$("#queren").attr("disabled",false);
											return;
									 }else if("3" == errerCode){
											alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_207'));
											$("#queren").attr("disabled",false);
											return;
									 }else if("4" == errerCode){
											alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_174'));
											$("#queren").attr("disabled",false);
											return;
									 }else if("errer" == errerCode){
											alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_208'));
											$("#queren").attr("disabled",false);
											return;
									 }else{
									 		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_178'));
									 		$("#queren").attr("disabled",false);
											window.location.href = window.location.href;
									 }
							 	}else if(returnType == "stage4"){
			   						if("success" == errerCode){
											alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_209'));
											doNotReStart();
											$('#userSubno').val("");
											//$('#spUser').val("");
											$("#queren").attr("disabled",false);
											$('#reStartSendDia').dialog('close');
											return;
									 }else{
									 		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_192'));
									 		doNotReStart();
											$('#userSubno').val("");
											//$('#spUser').val("");
											$("#queren").attr("disabled",false);
									 		$('#reStartSendDia').dialog('close');
											return;
									 }
			   					}
							 }
   					} 
 			});
}

function exportExcel(){
	var pnoticeSize = $("#pnoticeSize").val();
	if(pnoticeSize>0){
		if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210'))){
				$.ajax({
				type: "POST",
				url: "per_queryNotHis.htm?method=exportPerExcel",
				data: {pre_username:$('#username').val(),
						pre_phone:$('#phone').val(),
						pre_noticeId:$('#noticeId').val(),
						remsg:$('#remsg').val(),
						isReAttr:$('#isReAttr').val(),
						isGeAttr:$('#isGeAttr').val(),
						sendCount:$('#reCount').val(),
						taskid:$('#taskid').val()
						},
                beforeSend:function () {

					page_loading();
                },
                complete:function () {
			    	page_complete();
                },
				success: function(result){
                        if(result=='true'){
                            download_href("per_queryNotHis.htm?method=downloadFile");
                        }else{
                            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
                        }
	   			}
			});
	    	//location.href="per_queryNotHis.htm?method=exportPerExcel&pre_username="+$('#username').val()+"&pre_phone="+$('#phone').val()+"&pre_noticeId="+$('#noticeId').val()+"&remsg="+$('#remsg').val()
	    	//+"&isReAttr="+$('#isReAttr').val()+"&isGeAttr="+$('#isGeAttr').val()+"&sendCount="+$('#reCount').val()+"&taskid="+$('#taskid').val();
	    }
	}else{
    	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_212'));
	    }
   }	
		
		
	//取消重发关闭界面
function doNotReStart(){
	$("#isOk").val("");
	$("#sendContent").val("");
	$("#userCount").empty();
	$("#itemUser").val("");
}		

//发送之前判断余额
function checkFee(){
	//次数
	var sdCount1 = parseInt($("#sendCount").val());
	$.post("per_queryNotHis.htm",{method:"getCt",lguserid:$("#lguserid").val(),isAsync:"yes",time: new Date().getTime()},
			function(result)
			{
				if(result == "outOfLogin")
				{
					//登录超时，返回登录界面
					parent.document.getElementById("logoutalert").value = 1
					var commonPath = $("#commonPath").val();
		    		window.location.href= commonPath+"/common/logoutEmp.html";
					return;
				}else if(result =="nojifei" )
				{
					$("#queren").attr("disabled","");
					return;
				}
				else{
					result=result.substr(result.indexOf("ye")+2);
					if(result.indexOf("ye")!=-1)
					{
						result=result.substr(result.indexOf("ye")+2);
					}
					var count=result.split(",");
					var regNum=/^[0-9]*$/; 
					//如果是数字返回true，其它返回false
					if(regNum.test(count[0])&&regNum.test(count[1]))
					{
						var allFee = parseInt(count[0]);
						if(sdCount1*userCount>0)
						{
							if(allFee<sdCount1*userCount)
							{
								feeFlag = false;
								alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_189'));
								$("#queren").attr("disabled",true);
							}else
							{
								feeFlag = true;
								$("#queren").attr("disabled",false);
							}
						}
					}
				}
			
		});	
}