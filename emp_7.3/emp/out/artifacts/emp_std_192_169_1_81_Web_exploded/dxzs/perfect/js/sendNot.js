
var formName = "form3";

//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
function noticEblur(ob) {
	if (ob.is("#contents")) {
	ob.val($.trim(ob.val()));
	len(ob);
	if ($.trim(ob.val()) == "") {
		var errorMsg = getJsLocaleMessage('dxzs','dxzs_ssend_alert_160');
		document.forms[formName].isOk.value = 0;
	} else if (ob.val().length > 965) {
		var errorMsg = "<br/><br/>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_213')+"<br/>";
			document.forms[formName].isOk.value = 1;
		} else {
			
			document.forms[formName].isOk.value = 2;
		}
	}
}


// 统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	var huiche = content.length - content.replaceAll("\n", "").length;
$('form[name="' + formName + '"]').find(' #strlen').html((content.length + huiche));
if(content.length > 0){
	$('form[name="form3"]').find(".add_error").remove();
	}
};

// 短信内容键盘事件监听统计字数
function synlen() {
	$("form").find("textarea[name='msg']").keyup(function() {
	var content = $(this).val();
	var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > 965) {
			$(this).val(content.substring(0, 965 - huiche));
		}
		len($(this));
	});
}

$(document).ready(function(){
	getLoginInfo("#getloginUser");
$("#subSend").attr("disabled","disabled");
synlen();
$("#infoDiv").dialog({
	autoOpen: false,
	height:570,
	width: 560,
	resizable:false,
	modal: true
});


$('#moreSelect').hide();
$('#u_o_c_explain').toggle(function(){
		$("#foldIcon").removeClass("unfold");
		$("#foldIcon").addClass("fold");
		$('#moreSelect').show();
	},function(){
		$("#foldIcon").removeClass("fold");
		$("#foldIcon").addClass("unfold");
		$('#moreSelect').hide();
});

var guid = $("#lgguid").val();
var ofrm1 = document.getElementById("flowFrame").document;  
if (ofrm1 == undefined){
	ofrm1 = document.getElementById("flowFrame").contentWindow.document;
	ofrm1.getElementById("userGuid").value = guid;
}else{
	document.frames["flowFrame"].document.getElementById("userGuid").value = guid;
	}

});


function cancelInfo(){
	$("#usedSubno").attr("value","");
	$("#depIds").val("");
	$("#groupIds").val("");
	$("#empUserIds").val("");
	$("#deUserIds").val("");
	window.location.href = window.location.href;
}

function showInfo()
{  
	$(".ui-dialog-titlebar-close").hide();
	$("#infoDiv").dialog("open");
}
//发送之前判断余额
var feeFlag= false;
var userCount = 0;
function checkFee(){
	//次数
	var sdCount1 = parseInt($("#sdCount").val());
	if(userCount == 0){
		$("#subSend").attr("disabled","disabled");
		return;
	}
	$.post("per_queryNotHis.htm",{method:"getCt",lguserid:$("#lguserid").val(),isAsync:"yes",time: new Date().getTime()},
			function(result)
			{
				if(result == "outOfLogin")
				{
					//登录超时，返回登录界面
					parent.document.getElementById("logoutalert").value = 1;
					var commonPath = $("#commonPath").val();
		    		window.location.href= commonPath+"/common/logoutEmp.html";
					return;
				}else if(result =="nojifei" )
				{
					$("#subSend").attr("disabled","");
					feeFlag = true;
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
								$("#subSend").attr("disabled","disabled");
							}else
							{
								feeFlag = true;
								$("#subSend").attr("disabled","");
							}
						}
					}
				}
			
		});	
}
	

//发送完美通知	
function sendPerNotice(){
	if(feeFlag==false)
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_189'));
		return;
	}
		$("#subSend").attr("disabled","disabled");
		//获取的操作员的GUID静态变量	
		var lgguid = $("#lgguid").val();
		if(lgguid == ""){
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_190'));
			$("#subSend").attr("disabled",false);
			window.location.href = window.location.href;
			return;
		}
		//SP账号的静态变量	
		var fsName = $("#fsName").val();
		//是否是计费模式	
		var jiFei = $("#jiFei").val();
		if("1" == jiFei){
			var smsCount = $("#smsCount").val();
			if(smsCount < 1){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_157'));
				$("#subSend").attr("disabled",false);
				return;
			}
		}
		//发送内容	
		var content = $.trim($("#contents").val());		
		//发送间隔	
		var sdInterval = $("#sdInterval").val();	
		//发送短信条数		
		var sdCount = $("#sdCount").val();
		//业务类型					
		var busCode =$("#busCode").val();
		//发送账号	
		var spUser  = $("#spUser").val();
		//发送时获取的尾号		
		var usedSubno  = $("#usedSubno").val();
		//选择的部门ID
		var depIds = $("#depIds").val();
		//选择的群组ID
		var groupIds = $("#groupIds").val();
		//选择的员工ID
		var empUserIds = $("#empUserIds").val();
		//选择的自定义ID
		var deUserIds = $("#deUserIds").val();
		$("form[name='form3']").find("textarea[name='msg']").trigger("blur");	
		var isOk = $("#isOk").val();
		
		if(depIds == "" && depIds.length == 0 && groupIds == "" && groupIds.length == 0 && empUserIds == "" && empUserIds.length == 0 && deUserIds == "" && deUserIds.length == 0 ){
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_158'));
			$("#subSend").attr("disabled",false);
			return;
		}else if(spUser == ""){
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_159')+fsName+"！");
			$("#subSend").attr("disabled",false);
			return;
		}else if(isOk == 0 || isOk == 1){
			if(isOk == 0){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_160'));
			}else{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_213'));
			}
			$("#subSend").attr("disabled",false);
			return ;
		}else{
			$.post("per_sendNot.htm",{
			method:"sendPerMsg" ,
			content : content.replaceAll("\n","\r\n") ,
			sdInterval : sdInterval,
			memcount:userCount,
			sdCount : sdCount,
			spUser : spUser,
			busCode:busCode,
			usedSubno:usedSubno,
			depIds :depIds,
			groupIds :groupIds,
			empUserIds :empUserIds,
			deUserIds:deUserIds,
			lgguid:lgguid
   			},function(result){
   					if("" == result){
	   					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_194'));
	   					$("#subSend").attr("disabled",false);
	   					return;
	   				}else if(result.indexOf("html") > 0){
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_201'));
		    			window.location.href=location;
		    		    return;
		    		}
	   				var array = result.split("&");
	   				var returnType = array[0];
	   				var returnMsg =  array[1];
	   				if(returnType == "stage1"){
		   				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_214'));
		   			 	$("#subSend").attr("disabled",false);
		   			 	return;		
		   			}else if(returnType == "stage2"){
	   				 	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+"\n       "+returnMsg+"\n "+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
		   			 	$("#subSend").attr("disabled",false);
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
			   			var showsptext = $("#fsName").val()+$("#spUser").val()+""+spErrText+"！";
						alert(showsptext);
						$("#subSend").attr("disabled",false);
		   			 	return;
					}else{
   						if("enoughSubno" == returnMsg){
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_92'));
							$("#subSend").attr("disabled",false);
							return;
						 }else if("notSubno" == returnMsg){
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_171'));
							$("#subSend").attr("disabled",false);
							return;
						 }else{
							var arr = returnMsg.split("_");
			   				var errerCode = arr[0];
			   				var subno =  arr[1];
			   				if(usedSubno == null || "" == usedSubno){
			   					$("#usedSubno").val(subno);
			   				}
						 	if(returnType == "stage3"){
					 			 if("2" == errerCode){
										alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_172'));
										$("#subSend").attr("disabled",false);
										return;
								 }else if("3" == errerCode){
										alert(fsName+getJsLocaleMessage('dxzs','dxzs_ssend_alert_173'));
										$("#subSend").attr("disabled",false);
										return;
								 }else if("4" == errerCode){
										alert(fsName+getJsLocaleMessage('dxzs','dxzs_ssend_alert_174'));
										$("#subSend").attr("disabled",false);
										return;
								 }else if("errer" == errerCode){
										alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_175')+fsName+getJsLocaleMessage('dxzs','dxzs_ssend_alert_176'));
										$("#subSend").attr("disabled",false);
										return;
								 }else{
								 		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_178'));
										$("#subSend").attr("disabled",false);
										window.location.href = window.location.href;
								 }
						 	}else if(returnType == "stage4"){
		   						if("success" == errerCode){
										alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_209'));
										window.location.href = window.location.href;
										return;
								 }else{
								 		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_192'));
								 		$("#subSend").attr("disabled",false);
											window.location.href = window.location.href;
											return;
									 }
			   				
			   					}
							 }
   					} 
		
			});
	
	}

}



// 选择对象界面的确定操作
function doOk(){
	// 这里处理是否 右边选择的记录$("#right option:selected").size()
	var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
	  //代表的是员工机构IDS 
	  $("#depIds").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
	  //代表的是群组IDS 
	  $("#groupIds").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
	  //代表的是员工IDS 
	  $("#empUserIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
	  //代表的是外部人员IDS 
	  $("#deUserIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
	  userCount =  parseInt($(window.frames['flowFrame'].document).find("#manCount").html());
	  $("#pernoticCount").empty();
	    if(userCount > 0){
   			$("#pernoticCount").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_305')+userCount+getJsLocaleMessage('dxzs','dxzs_ssend_alert_114'));
   			$("#showflowname").val(getJsLocaleMessage('dxzs','dxzs_ssend_alert_306'));
	    }else{
	    	$("#showflowname").val(getJsLocaleMessage('dxzs','dxzs_ssend_alert_307'));
	    	userCount = 0;
	    }
		$("#infoDiv").dialog("close");
	 	checkFee();  
}


//点击选择对象中的  人员取消	
function doNo(){
	var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
	$("#pernoticCount").empty();
 	$("#showflowname").val(getJsLocaleMessage('dxzs','dxzs_ssend_alert_307'));
	if(optionSize == 0){
		clearUser();
		$("#infoDiv").dialog("close");
		return;
	}
	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_187'))){
		  //代表的是员工机构IDS 
		  $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
		  //代表的是群组IDS 
		  $(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
		  //代表的是分页索引第一页 
		  $(window.frames['flowFrame'].document).find("#pageIndex").val("1");
		  //代表的是员工IDS 
		  $(window.frames['flowFrame'].document).find("#employeeIds").val("");
		  //代表的是外部人员IDS 
		  $(window.frames['flowFrame'].document).find("#malistIds").val("");
		  $(window.frames['flowFrame'].document).find("#right").empty();
		  $(window.frames['flowFrame'].document).find("#manCount").html(0);
		  clearUser();
    	  $("#infoDiv").dialog("close");
	 }
}
// 清空主界面的选择对象的数据
function clearUser(){
	//把主页面的数据也清空
	$("#depIds").val("");
	$("#groupIds").val("");
	$("#empUserIds").val("");
	$("#deUserIds").val("");
	userCount = 0;
	$("#subSend").attr("disabled","disabled");
}