var ddd = null;
var contentLen = 955;
var skin = $("#skin").val();
//判断界面输入手机号码
function addphone(type){
	var phone=$.trim($("#tph").val());
	var tempname=$.trim($("#tempname").val());
	var ipath = $("#ipathUrl").val();
	var commonPath = $("#commonPath").val();

	if(type == "2"){
		if(phone.length == 0 || "" == phone){
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_39'));
			return;
		}
	}
	if(tempname.length == 0 || "" == tempname){
		tempname = "-";
	}
	if((phone.length == 11 && phone.substring(0,1) != "+" && phone.substring(0,2) !="00")||(type == "2"&&(phone.length>6&&phone.length<22)&&(phone.substring(0,1) == "+" || phone.substring(0,2) =="00"))){
 		$.post("per_sendNot.htm",
 	     {method:"filterPhone",tmp:phone},
 	     function(returnMsg){
 	    	 if(returnMsg == "1"){
 	    		 var phoneStr = $("#phoneStr").val();
 	    		 var flag = "1";
 	    		 if(phoneStr.length > 0 && phoneStr != ""){
 	    			var phoneArr = phoneStr.split(",");
 	    			for(var i=0;i<phoneArr.length;i++){
 	    				if(phone == phoneArr[i]){
 	    					flag = "2";
 	    					break;
 	    				}
 	    			}
 	    		 }
				if(flag == "1")
				{
					if(phone.substring(0,1) == "+")
					{
					$("#infomaTable").append("<tr id='GJ"+phone.substring(1)+"'><td align='center'  class='div_bd' style='border-left: 0;border-right:0;' name='ConR'  valign='middle'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_154')+"</td>"
       	       		+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"+tempname+"</td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"+phone+"</td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"
       				+"<a onclick=delPhone('GJ"+phone.substring(1)+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
					}else{
       				$("#infomaTable").append("<tr id='"+phone+"'><td align='center'  class='div_bd' style='border-left: 0;border-right:0;' name='ConR'  valign='middle'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_154')+"</td>"
       	       		+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"+tempname+"</td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"+phone+"</td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"
       				+"<a onclick=delPhone('"+phone+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
       				}
       				$("#phoneStr").val(phoneStr+phone+",");
       				
       				clearbyhand();
				}else{
					//手机号码已存在
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_155'));
					clearbyhand();
					return;
       			}  
      		}else{
      			//手机不合法
      			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_156'));
      			clearbyhand();
      			return;
      		}
  		});
	}else{
		if(type == "2"){
			alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_156'));
			clearbyhand();
  			return;
		}
	}
}

//清空输入框数据，并还原
function clearbyhand(){
	$("#tph").val("");
	$("#tempname").val("");
	checkText($("#tph"),"1");
	checkText($("#tempname"),"2");
}


//对 输入框中的手工 添加的手机号码/
function delPhone(phoneNum){
	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76')))
	{
		$("#"+phoneNum+"").remove();
		if(phoneNum.substring(0,2) == "GJ")
		{
			phoneNum = "+" + phoneNum.substring(2);
		}
		$("#phoneStr").val($("#phoneStr").val().replace(phoneNum+",",""));
	}
}


function previewNotice(){
		$("#subSend").attr("disabled","disabled");
		var fsName = $("#fsName").val();
		var jiFei = $("#jiFei").val();
		if("1" == jiFei){
			var smsCount = $("#smsCount").val();
			if(smsCount < 1){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_157'));
				$("#subSend").attr("disabled",false);
				return;
			}
		}
		var content = $.trim($("#contents").val());		
		var sdInterval = $("#sdInterval").val();	
		var sdCount = $("#sdCount").val();
		var busCode =$("#busCode").val();
		var spUser  = $("#spUser").val();
		var usedSubno  = $("#usedSubno").val();
		var depIds = $("#depIds").val();
		var groupIds = $("#groupIds").val();
		var empUserIds = $("#empUserIds").val();
		var deUserIds = $("#deUserIds").val();
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		
		$("form[name='form3']").find("textarea[name='msg']").trigger("blur");	
		var isOk = $("#isOk").val();
		var trcount = $("#infomaTable tr");
		if(trcount.size() == 1){
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
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_161')+contentLen+getJsLocaleMessage('dxzs','dxzs_ssend_alert_162'));
			}
			$("#subSend").attr("disabled",false);
			return ;
		}else{
			var addphonename = "";
			$("#infomaTable tr").each(function(){
				var idmsg = $(this).attr("id");
				if(idmsg != "addressList" && idmsg != "first"){
					var name = $(this).find("td:eq(1)").html();
					var phone = $(this).find("td:eq(2)").html();
					addphonename = addphonename + phone + "_" + name + "#";
				}
			});
			if(addphonename != "" && addphonename.length>0){
				$("#handphonename").val(addphonename);
			}
			$.post("per_sendNot.htm",{method:"checkPerfectMsg",
						lguserid:lguserid,
						content:content,
						usedSubno:usedSubno,
						spUser:spUser,
						lgcorpcode:lgcorpcode
			 	     },function(returnMsg){
			 	    	 	if(returnMsg == "" || returnMsg == "errer"){
			 	    	 		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_163'));
			 	    	 		$("#subSend").attr("disabled",false);
			 	    	 		return;
			 	    	 	}else{
			 	    	 		var array = returnMsg.split("&");
			 	    	 		var returnType = array[0];
			 	    	 		var returnMsg = array[1];
			 	    	 		if(returnType == "success"){
				 	    	 			if(usedSubno == null || "" == usedSubno){
			 	    						$("#usedSubno").val(returnMsg);
			 	    					}
			 	    	 				$("#probar").dialog({
						 	   				modal:true,
						 	   				title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_37'), 
						 	   				height:100,
						 	   				resizable :false,
						 	   				closeOnEscape: false,
						 	   				width:200,
						 	   				open: function(){
					 	   			  		errorNum=0;
					 	   					$(".ui-dialog-titlebar").hide();
					 	   					dd = window.setTimeout("fresh()",3000);
					 	   		  		}
					 	    	 		});
					 	    	 		//防止内存溢出，初始化为0。预览成功为1，为0则 提示系统繁忙
						 	   			$("#error").val("0");	
						 	       		$("form[name='form3']").attr("action",$("form[name='form3']").attr("action")+"&lguserid="+$("#lguserid").val()+"&perfectTaskId="+$("#perfectTaskId").val());
						 	       		$("form[name='form3']").submit();
			 	    	 		}else if(returnType == "stage1"){
			 	    				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_165'));
			 	    			 	$("#subSend").attr("disabled",false);
			 	    			 	return;		
			 	    			}else if(returnType == "stage2"){
			 	    			 	alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_41')+"\n     "+returnMsg+"\n "+getJsLocaleMessage('dxzs','dxzs_ssend_alert_42'));
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
			 	    				var fsName = $("#fsName").val();
			 	    				var showsptext = fsName+$("#spUser").val()+""+spErrText+"！";
			 	    				alert(showsptext);
			 	    				$("#subSend").attr("disabled",false);
			 	    			 	return;
			 	    			}else if(returnType == "stage3"){
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
			 	    					}else if("errpre" == errerCode){
			 	    							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_177'));
			 	    							$("#subSend").attr("disabled",false);
			 	    							return;
			 	    					}else{
			 	    					 		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_178'));
			 	    							$("#subSend").attr("disabled",false);
			 	    							window.location.href = window.location.href;
			 	    					 }
			 	    				 	
			 	    				 }
			 	    			}else{
			 	    				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_163'));
 	    							$("#subSend").attr("disabled",false);
			 	    			}
			 	    	 	}
			 	     }
			);
			
		
    	
		}
  }



function presend(data)
{
	$("#error").val("1");	
	window.clearInterval(dd);
	//关闭旋转界面
	$("#probar").dialog("close");
	$(".ui-dialog-titlebar").show();
	if(data=="error")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_177'));
		$("#subSend").attr("disabled",false);
		return;
	}else if(data=="noPhone")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_179'));
		$("#subSend").attr("disabled",false);
		return;
	}else if(data=="maxcounterrer")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_180'));
		$("#subSend").attr("disabled",false);
		return;
	}else if(data=="presendcounterrer")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_181'));
		$("#subSend").attr("disabled",false);
		return;
	}else if(data=="spUserFeeFlagErr")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_182'));
		$("#subSend").attr("disabled",false);
		return;
	}else if(data=="spUserIdFeeErr")
	{
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_183'));
		$("#subSend").attr("disabled",false);
		return;
	}else
	{
		var array=eval("("+data+")");
		var returnType = array.stage;
		var returnMsg = array.errormsg;
		 if(returnType == "stage4"){
			$("#content").empty();
			$("#content").append("<thead><tr align='center'><th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_63')+"</th>" +
					"<th><center><div style='width:89px'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_64')+"</div></center></th>" +
					"<th>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_65')+"</th></tr></thead>" );
			var name = $("#name").val();
			var msgContent= perfectSignName.replace("%s",name).replace("完美通知",getJsLocaleMessage("dxzs","dxzs_common_perfectNotice")) + $("form[name='form3']").find("textarea[name='msg']").val();
			//var array=data.split('&');
			//预览手机号码
			//var nums=array[1].split(",");
			var nums=array.previewPhone.split(';');
			//总提交数
			//$("#counts").text(array[2]);
			$("#counts").text(array.subCount);
			//有效号数
		    //$("#effs").text(array[3]);
		    $("#effs").text(array.effCount);
		    //黑名单
		    //$("#blacks").text(array[6]);
		    $("#blacks").text(array.blackCount);
		    //不合法数
		   //$("#legers").text(array[4]);
		    $("#legers").text(array.badModeCount);
		    //重复数
		    //$("#sames").text(array[5]);
		    $("#sames").text(array.repeatCount);
		    //总发送条数
		    //$("#yct").text(array[8]);
		    $("#yct").text(array.presendcount);
		    //最大条数
		    //$("#ct").text(array[13]);
		    $("#ct").text(array.maxcount);
		    //错误号码文件url
		    //$("#badurl").attr("value",array[9]);
		    $("#badurl").attr("value",array.viewFilePath);
		    //有效员工guid
		    //$("#employeeguids").attr("value",array[11]);
		    //有效自定义人员guid
		    //$("#malistguids").attr("value",array[12]);
		    //有效号码文件url
		    //$("#phoneurl").attr("value",array[7]);
		    $("#phoneurl").attr("value",array.validFilePath);
		    //尾号
			//$("#usedSubno").val(array[14]);
		    $("#usedSubno").val(array.usedSubno);
			//var yeresult = array[15];
			var spye = array.spFeeResult;
			checkspye(spye);
			
			var spUserFeeFlag = array.spUserFeeFlag;
			$("#spUserFeeFlag").val(spUserFeeFlag);
			var spUserIdFee = array.spUserIdFee
			$("#spanSpFee").text(spUserIdFee);
			
		    //var wuxiaonum = parseInt(array[4])+parseInt(array[6])+parseInt(array[5]);
			var wuxiaonum = parseInt(array.subCount)-parseInt(array.effCount);
			$("#alleff").text(wuxiaonum);
		    if(wuxiaonum == 0)
		    {
		    	$("#preinfonum").attr("style","display:none");
		    }else{
		    	$("#preinfonum").attr("style","display:block");
		    }
		   // $("#isCharg").attr("value",array[10]);
		    $("#isCharg").attr("value",array.isCharge);
		    if(array[2] == 0)
		    {
		    	$("#content").append("<tbody><tr><td colspan='3'  align='center'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_73')+"</td></tr></tbody>");
		    }else
		    {
				for (var x = 0; x < nums.length; x=x+1) {
					if(nums[x] != null && nums[x] != ""){
						$("#content").append("<tr align ='center'><td>"+(x+1)+"</td><td>"+nums[x]+"</td>" +"<td  style='text-align:left'><xmp style='word-break: break-all;white-space:normal; '>"
								+msgContent+"</xmp></td></tr>");
					}
				}
		    }
		    $("#preMin").empty();
		    $("#preMin").html($("#sdInterval").val());
		    $("#preCount").empty();
		    $("#preCount").html($("#sdCount").val());
		    $("#detail_Info").css("display","block");
		    $("#detail_Info").dialog("open");
		    deleteleftline();
		    $(".ui-dialog-titlebar-close").hide();
		}else if(returnType == "stage1"){
			if(returnMsg == "PERF100"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_165'));
				$("#subSend").attr("disabled",false);
				return;
			}else{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_177'));
				$("#subSend").attr("disabled",false);
				return;
			}
		}else if(returnType == "stage7"){
			 if(returnMsg == "PERF101"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_185'));
				$("#subSend").attr("disabled",false);
				return;
			 }else{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_177'));
					$("#subSend").attr("disabled",false);
					return;
				}
		}else if(returnType == "stage8"){
			if(returnMsg == "PERF102"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_177'));
				$("#subSend").attr("disabled",false);
				return;
			}
		}
	}
}

function checkspye(yeresult)
{
	var altstr = "";
	 $("#nosendReason").empty();
	if(yeresult.indexOf("lessgwfee") == 0)
	{
		altstr = getJsLocaleMessage('dxzs','dxzs_ssend_alert_67')+yeresult.split("=")[1];
		$("#btsend").attr("disabled","disabled");
	}else if(yeresult=="feefail" || yeresult=="nogwfee")
	{
		altstr = getJsLocaleMessage('dxzs','dxzs_ssend_alert_68');
		$("#btsend").attr("disabled","disabled");
	}
	$("#nosendReason").html(altstr);
	$("#notsend").css("visibility","visible");
}





function checkerror()
{
	if($("#error").val()=="0")
	{
		window.clearInterval(ddd);
		$("#probar").dialog("close");
		alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_184'));
		$("#subSend").attr("disabled",false);
	}
}



//防止数据库网络断开
function fresh()
{
	$.ajax({
		url:"LoadingServlet.htm",
		type:"post",
		dataType:"script",
		success:function(result){
			if( result !="true")
			{
				if(errorNum==0)
				{
					errorNum==1;
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_48'));
					window.clearInterval(ddd);
					window.location.href='per_sendNot.htm?method=find&lgguid='+$('#lgguid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			if(errorNum==0)
			{
				errorNum=1;
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_48'));
			}
			window.clearInterval(ddd);
			window.location.href='per_sendNot.htm?method=find&lgguid='+$('#lgguid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
}


//错误上传内容下载
function uploadbadFiles()
{
    var phoneurl = $("#phoneurl").val();
	var pathUrl = $("#cpath").val();
    var badurl = phoneurl.replace(".txt","_bad.txt");
   	$.post(pathUrl+"/per_sendNot.htm?method=goToFile", {
		url : badurl
	},function(result) {
			if (result == "true") {
				download_href(pathUrl+"/doExport.hts?u="+badurl);				
			} else if (result == "false"){
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_286'));
				return;
			}else{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_194'));
				return;
			}
	});
}
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
		} else if (ob.val().length > contentLen) {
			var errorMsg = "<br/><br/>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_161')+contentLen+getJsLocaleMessage('dxzs','dxzs_ssend_alert_162')+"<br/>";
			document.forms[formName].isOk.value = 1;
		} else {
			
			document.forms[formName].isOk.value = 2;
		}
	}
}


// 统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	if(content.length>contentLen)
	{
		$("#contents").val(content.substring(0,contentLen));
		content = $("#contents").val();
	}
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
		if (content.length + huiche > contentLen) {
			$(this).val(content.substring(0, contentLen - huiche));
		}
		len($(this));
	});
}

$(document).ready(function(){
	getLoginInfo("#getloginUser");
	synlen();
	var _width = 535;
	if(skin.indexOf("frame4.0") != -1) {
		_width = 700;
	}
	$("#infoDiv").dialog({
		autoOpen: false,
		height:550,
		width: _width,
		resizable:false,
		modal: true,
		open:function(){
			$(".ui-dialog-titlebar-close").show();
		},
		 close:function(){
			closeDialog();
			$(".ui-dialog-titlebar-close").hide();
		}
	});


	
	//短信预览窗口
	$("#detail_Info").dialog({
		autoOpen: false,
		modal:true,
		title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_78'), 
		height:550,
		width:520,
		closeOnEscape: false,
		resizable:false,
		open:function(){
				//默认打开错误号码详情	
				$("#foldIcon1").attr("src",$("#skin").val()+"/images/fold.png");
				$('#moreSelect1').show();
				//这里是处理打开预览界面的时候， 处理界面的显示信息 
				$(".ui-dialog-titlebar-close").hide();		
				//短信余额 								 		 
		 		 var ct = $.trim($("#ct").text());
		 		//预发送条数	
		 		 var yct =$.trim($("#yct").text());
			
				  //是否计费 1是  2不是	
				var jiFei = $("#jiFei").val();
				if(jiFei == "1"){
					 $("#nosendReason").empty();
					 $("#showyct").css("visibility","visible");
					 if(eval(yct) == 0 ){
						 $("#nosendReason").css("visibility","visible");
						 $("#nosendReason").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_66'));
						 $("#notsend").css("visibility","visible");
				 		//没有预发号码的时候 ，隐藏信息，并且把按钮置灰	
			 		    $("#btsend").attr("disabled","disabled");
			 		    return;
					 }else if(eval(ct) == 0){
						 $("#nosendReason").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_157'));
						 $("#notsend").css("visibility","visible");
				 		//没有预发号码的时候 ，隐藏信息，并且把按钮置灰	
			 		    $("#btsend").attr("disabled","disabled");
			 		    return;
					}else if(eval(ct) < eval(yct)){
						 $("#nosendReason").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_157'));
						 $("#notsend").css("visibility","visible");
				 		//没有预发号码的时候 ，隐藏信息，并且把按钮置灰	
			 		    $("#btsend").attr("disabled","disabled");
			 		    return;
					}
				}
				else
				{
					//判断不是计费的情况下	
				 	$("#showyct").css("visibility","hidden");
				}
				
				var spUserFeeFlag = $("#spUserFeeFlag").val();
				//预付费，则要计费
				if(spUserFeeFlag == 1)
				{
					$("#showSpFee").css("visibility","visible");
					//sp账号余额
					var spUserIdFee = eval($.trim($("#spanSpFee").text()));
					var presendcount = eval(yct);
					if(spUserIdFee < presendcount)
					{
						$("#nosendReason").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_79'));
						$("#notsend").css("visibility","visible");
						//按钮置灰	
						$("#btsend").attr("disabled","disabled");
						return;
					}
				}
				else
				{
					//判断不是计费的情况下	
				 	$("#showSpFee").css("visibility","hidden");
				}
				
				 if(eval(yct) == 0){
					 $("#nosendReason").empty();
					 $("#nosendReason").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_66'));
			 		//没有预发号码的时候 ，隐藏信息，并且把按钮置灰	
		 		    $("#btsend").attr("disabled","disabled");
		 		    return;
				 }
				 
			},
			 close:function(){
				//设置预览提交时的错误标志 
				$("#error").val("");
				$("#btsend").attr("disabled","");
				$(".ui-dialog-titlebar-close").show();
			}
	});
	
	inputTipText();
	
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


	$("#showeffinfo").toggle(function() {
		$("#effinfo").show();
		$("#arrowhead").removeClass("unfold");
		$("#arrowhead").addClass("fold");
	}, function() {
		$("#effinfo").hide();
		$("#arrowhead").removeClass("fold");
		$("#arrowhead").addClass("unfold");
	});

	
	setContentMaxLen();
	$("#spUser").bind("change", function(){setContentMaxLen();});
	
	var guid = $("#lgguid").val();
	var ofrm1 = document.getElementById("flowFrame").document;

	// if (ofrm1 == undefined){
	// 	ofrm1 = document.getElementById("flowFrame").contentWindow.document;
	// 	setTimeout(function(){
	// 		ofrm1.getElementById("userGuid").value = guid;
	// 	},1000);
	// 	//ofrm1.getElementById("userGuid").value = guid;
	// }else{
	// 	setTimeout(function(){
	// 		document.frames["flowFrame"].document.getElementById("userGuid").value = guid;
	// 	},1000);
	// //	document.frames["flowFrame"].document.getElementById("userGuid").value = guid;
	// }
	//如果完美通知发送成功，则弹出查看任务的框。
	if(isAlert == "1"){
		$("#message").dialog({width:300,height:180});
		$("#message").dialog("open");	
	}
});
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
			noticEblur($("#contents"));
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

	function cancelInfo(){
		$("#usedSubno").attr("value","");
		$("#depIds").val("");
		$("#groupIds").val("");
		$("#empUserIds").val("");
		$("#deUserIds").val("");
		var pathUrl = $("#cpath").val();
		window.location.href = pathUrl+"/per_sendNot.htm?method=find&lgguid="+$("#lgguid").val();
	}

	//##########替换选择人员###########
	function changeDialogTitleCss(id) {
	    var $titleBar = $("#ui-dialog-title-" + id);
	    $titleBar.parent().addClass("titleBar");
	    $titleBar.addClass("titleBarTxt");
	}

function showInfo() {
		var commonPath = $("#commonPath").val();
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		var chooseType="1,4";
		if(skin.indexOf("frame4.0") != -1) {
			$("#flowFrame").attr("src",commonPath+"/common/selectUserInfo.jsp?lguserid="+lguserid+"&chooseType="+chooseType+"&lgcorpcode="+lgcorpcode);
			changeDialogTitleCss('infoDiv');
		}
		$(".ui-dialog-titlebar-close").show();
		$("#infoDiv").dialog("open");

		/*
		详见bug平台72031，如果你找到了dialog关闭manCount值消失的原因，有了更好的方法，可将下面这段注释或删除
		 */
        var nums = $('#nums').val();
        if(nums.length!==0){
            $(window.frames['flowFrame'].document).find("#manCount").html($('#nums').val());
        }
	}

	// 选择对象界面的确定操作
	function doOk(){
			//右边选择的option 用于数据回显
		    $("#rightSelectedUserOption").val($(window.frames['flowFrame'].document).find("#right").html());
			var commonPath = $("#commonPath").val();
			// 这里处理是否 右边选择的记录$("#right option:selected").size()
			var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
			if(optionSize === 0){
				$("#addressList").remove();
				$(window.frames['flowFrame'].document).find("#manCount").html(0);
				$(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
				$(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
				$("#depIds").val("");
				$("#depIdsTemp").val("");
				 $("#havOne").val("2");
				$("#infoDiv").dialog("close");
				return;
			}
			$("#depIds").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
        	$("#depIdsTemp").val($("#depIds").val());
        	$("#empDepIds").val($("#depIds").val());
        	$("#malIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
			$("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
			$("#groupIds").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
			$("#userMoblieStr").val($(window.frames['flowFrame'].document).find("#moblieStrs").val());

			var numcount = parseInt($(window.frames['flowFrame'].document).find("#manCount").html());
			var skinStr = $("#skin").val();
			if(skinStr.indexOf("frame4.0") > -1){
				//员工
                $("#empUserIds").val($("#empIds").val());
                $("#empUserIdsTemp").val($("#empIds").val());
                //群组
                $("#groupIds").val($("#groupIds").val());
                $("#groupIdsTemp").val($("#groupIds").val());
                //自建
				$("#deUserIds").val($("#malIds").val());
				$("#deUserIdsTemp").val($("#malIds").val());
			}else {
                $(window.frames['flowFrame'].document).find("#right option").each(function() {
                    //isDep表示是1 员工机构 2表示客户 3 表示群组   4代表员工 5代表客户 6代表外部人员   et 1表示不包含    2表示包含
                    var  id =  $(this).val();
                    var isDep = $(this).attr("isDep");
                    if(isDep === "1"){
                        var et = $(this).attr("et");
                        if(et === "1"){
                            //$("#depIds").val($("#depIds").val()+id+",");
                        }else if(et === "2"){
                            //$("#depIds").val($("#depIds").val()+"e"+id+",");
                        }
                        //代表的是员工机构IDS
                        //	$("#depIdsTemp").val($("#depIdsTemp").val()+id+",");
                    }else if(isDep === "3"){
                        //代表的是群组IDS
                        $("#groupIds").val($("#groupIds").val()+id+",");
                        $("#groupIdsTemp").val($("#groupIdsTemp").val()+id+",");
                    }else if(isDep === "4"){
                        //代表的是员工IDS
                        $("#empUserIds").val($("#empUserIds").val()+id+",");
                        $("#empUserIdsTemp").val($("#empUserIdsTemp").val()+id+",");
                    }else if(isDep === "6"){
                        //代表的是外部人员IDS
                        $("#deUserIds").val($("#deUserIds").val()+id+",");
                        $("#deUserIdsTemp").val($("#deUserIdsTemp").val()+id+",");
                    }
                });
            }
			//处理选择对象的操作
    		var havOne = $("#havOne").val();
		    if(numcount > 0 && havOne === "2"){
		    	$("#infomaTable").append("<tr id='addressList'><td align='center' class='div_bd' height='16px' style='border-left: 0;border-right: 0;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_186')+"</td>"
       			+"<td align='center'  class='div_bd'   colspan='2' style='color:blue;border-right: 0;border-left: 0;cursor:pointer;'><a onclick='showInfo();'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_125')+"(<label id='choiceNum'>"+numcount+"</label>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_114')+")</a></td>"
       			+"<td align='center'  class='div_bd' style='border-right: 0;border-left: 0;'>"
       			+"<a onclick='doNo();'><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'/></a></td></tr>");
		    	$("#havOne").val("1");
		    }else{
		    	//如果未选择对象,则清空一栏	
				if(numcount === 0){
					clearUser();
				}else{
					$("#choiceNum").empty();
					$("#choiceNum").html(numcount);
				}
		    }
		    $(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
			$(window.frames['flowFrame'].document).find("#rightSelectTempAll").html($(window.frames['flowFrame'].document).find("#right").html());
            $("#nums").val(numcount);
			$("#infoDiv").dialog("close");
	}


	//点击选择对象中的  人员取消	
	function doNo(){
		var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
		if(optionSize === 0){
			if($("#choiceNum").length>0 && $("#choiceNum").html()>0)
			{
				if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_187'))){
					$(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
					$(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
					$(window.frames['flowFrame'].document).find("#employeeIds").val("");
					$(window.frames['flowFrame'].document).find("#malistIds").val("");
					$(window.frames['flowFrame'].document).find("#moblieStrs").val("");
					//代表的是分页索引第一页
					$(window.frames['flowFrame'].document).find("#pageIndex").val("1");
					$(window.frames['flowFrame'].document).find("#right").empty();
					$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
					$(window.frames['flowFrame'].document).find("#manCount").html(0);
					  
					  clearUser();
					  $("#addressList").remove();
					  $(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
					  $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
					  return;
				 }
			}
			clearUser();
			return;
		}
		if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_187'))){
			$(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
			$(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
			$(window.frames['flowFrame'].document).find("#employeeIds").val("");
			$(window.frames['flowFrame'].document).find("#malistIds").val("");
			$(window.frames['flowFrame'].document).find("#moblieStrs").val("");
			//代表的是分页索引第一页
			$(window.frames['flowFrame'].document).find("#pageIndex").val("1");
			$(window.frames['flowFrame'].document).find("#right").empty();
			$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
			$(window.frames['flowFrame'].document).find("#manCount").html(0);
			  
			  clearUser();
			  $("#addressList").remove();
			  $(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
			  $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
		 }
	}
	// 清空主界面的选择对象的数据
	function clearUser(){
		// 把主页面的数据也清空
		$("#depIds").val("");
		$("#groupIds").val("");
		$("#empUserIds").val("");
		$("#deUserIds").val("");
		$("#depIdsTemp").val("");
		$("#groupIdsTemp").val("");
		$("#empUserIdsTemp").val("");
		$("#deUserIdsTemp").val("");
        $('#nums').val(0);
        $("#empIds").val("");
        $("#malIds").val("");
        $("#userMoblieStr").val("");
        $("#empDepIds").val("");
        $("#rightSelectedUserOption").val("");
        $(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
        $("#havOne").val("2");
		$("#addressList").remove();
	}
	
	// 关闭dialog
	function closeDialog(){
		$("#depIds").val($("#depIdsTemp").val());
		$("#groupIds").val($("#groupIdsTemp").val());
		$("#empUserIds").val($("#empUserIdsTemp").val());
		$("#deUserIds").val($("#deUserIdsTemp").val());
		$(window.frames['flowFrame'].document).find("#empDepIdsStrs").val($("#depIdsTemp").val());
		$(window.frames['flowFrame'].document).find("#right").empty();
		$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
		$(window.frames['flowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());

		//修改点击关闭图表 上方菜单栏消失的bug
		if(null != $(window.frames['flowFrame'].document).find("#rightSelectTempAll").html()){
            $(window.frames['flowFrame'].document).find("#getChooseMan").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html().replaceAll("option", "li").replaceAll("value", "dataval"));
        }

		var flagtr = $("#havOne").val();
		if(flagtr == "1"){
			  var size = $(window.frames['flowFrame'].document).find("#right option").size();
			  if(size > 0){
				  $(window.frames['flowFrame'].document).find("#manCount").html($("#totalcount").html());
			  }else{
				  $(window.frames['flowFrame'].document).find("#manCount").html(0);
			  }
		}else{
			  $(window.frames['flowFrame'].document).find("#manCount").html(0);
		}
	}

	function inputTipText(){
		//所有样式名中含有graytext的input
		$("input[class*=graytext]").each(function(){
			var oldVal=$(this).val(); //默认的提示性文本
			$(this)
			.css({'color':'#ccc'}) //灰色
			.focus(function(){
			if($(this).val()!=oldVal)
				{$(this).css({'color':'#000'})}
			else
				{$(this).val('').css({'color':'#ccc'})}
			})
			.blur(function(){
			if($(this).val()=="")
				{$(this).val(oldVal).css({'color':'#ccc'})}
			})
			.keydown(function(){
				$(this).css({'color':'#000'})
			})
		})
	}
	// 处理输入  框的
	function checkText(ep,type)
	{
		var phone=$.trim($("#tph").val());
		var tempname=$.trim($("#tempname").val());
		
		if(ep.val()=="")
		{
			ep.next("label").css("display","inline");
		}else
		{
			ep.next("label").css("display","none");
		}

		if(type == "1"){
			var reg=/^(\+?)(\d*)$/g;	
			if(!reg.test(phone)){
				var str=phone.replace(/(\+?)(\d*)([^\d]*)/g,'$1$2');
				ep.val(str);
				$("#tph").val("");
				$("#idPlaceholder1").empty();
				$("#idPlaceholder1").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_188'));
			}
			else{
				var str=phone;
				if(str.length>21){
					str=str.substr(0,21);
					ep.val(str);
				}
			}
		}else if(type == "2"){
		 	var namereg = /[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g;
		 	if(namereg.test(tempname)){
		 		$("#tempname").val("");
		 		$("#idPlaceholder2").empty();
				$("#idPlaceholder2").html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_64'));
			}
		}
	}


	// 发送
	function send(){
		$("#btsend").attr("disabled","disabled");
		//发送内容	
		var content = $.trim($("#contents").val());		
		//发送间隔	
		var sdInterval = $("#sdInterval").val();	
		//发送短信次条数		
		var sdCount = $("#sdCount").val();
		//业务类型					
		var busCode =$("#busCode").val();
		//发送账号	
		var spUser  = $("#spUser").val();
		//发送时获取的尾号		
		var usedSubno  = $("#usedSubno").val();
		//员工所需要发送的guid		
		var employeeguids  = $("#employeeguids").val();
		//自定义所需要发送的guid		
		var malistguids  = $("#malistguids").val();
		//文件号码路径		
		var phoneurl  = $("#phoneurl").val();
		var lguserid = $("#lguserid").val();
		//预发送总条数
		var yct =$.trim($("#yct").text());
		//有效果号码数
		var effs =$.trim($("#effs").text());
		//任务ID
		var perfectTaskId = $("#perfectTaskId").val();
		var pathUrl = $("#cpath").val();
		var addphonename = "";
		$("#infomaTable tr").each(function(){
			var idmsg = $(this).attr("id");
			if(idmsg != "addressList" && idmsg != "first"){
				var name = $(this).find("td:eq(1)").html();
				var phone = $(this).find("td:eq(2)").html();
				addphonename = addphonename + name + "_" +phone + "#";
			}
		});
		$.post(pathUrl+"/per_sendNoticeSMS.htm",{
			method:"sendmsg" ,
			content : content.replaceAll("\n","\r\n") ,
			sdInterval : sdInterval,
			sdCount : sdCount,
			spUser : spUser,
			busCode:busCode,
			usedSubno:usedSubno,
			employeeguids:employeeguids,
			malistguids:malistguids,
			phoneurl:phoneurl,
			addphonename:addphonename,
			lguserid:lguserid,
			yct:yct,
			effs:effs,
			perfectTaskId:perfectTaskId
   			},function(result){
   				if(result == "unenoughfee"){
   					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_189'));
   					$("#subSend").attr("disabled","disabled");
					$("#btsend").attr("disabled","disabled");
					return;
	   			}else if(result == "sysusererrer"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_190'));
					$("#subSend").attr("disabled","");
					$("#btsend").attr("disabled","");
					$("#detail_Info").dialog("close");
					window.location.href = "per_sendNot.htm?method=find&lgguid="+$("#lgguid").val();
					return;
				}else if(result == "parametererrer"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_165'));
					$("#subSend").attr("disabled","");
					$("#btsend").attr("disabled","");
					$("#detail_Info").dialog("close");
					return;
				}else if(result == "adderrer"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_191'));
					$("#subSend").attr("disabled","");
					$("#btsend").attr("disabled","");
					$("#detail_Info").dialog("close");
					return;
				}else if(result == "success"){
					//alert("完美通知提交成功！");
					$("#btsend").attr("disabled","");
					$("#subSend").attr("disabled","");
					$("#detail_Info").dialog("close");
					window.location.href = "per_sendNot.htm?method=find&lgguid="+$("#lgguid").val()+"&isAlert=1";
					//$("#message").dialog({width:300,height:180});
					//$("#message").dialog("open");	
					//window.location.href = "per_sendNot.htm?method=find&lgguid="+$("#lgguid").val();
				}else if(result == "fail"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_192'));
					$("#subSend").attr("disabled","");
					$("#btsend").attr("disabled","");
					$("#detail_Info").dialog("close");
					return;
				}else if("nogwfee"==result || "feefail"==result || "feeerror"==result){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_16'));
					$("#subSend").attr("disabled","");
					$("#btsend").attr("disabled","");
					$("#detail_Info").dialog("close");
					return;
				}else if(result.indexOf("lessgwfee")==0){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_17'));
					$("#subSend").attr("disabled","");
					$("#btsend").attr("disabled","");
					$("#detail_Info").dialog("close");
					return;
				}else{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_194'));
					window.location.href = "per_sendNot.htm?method=find&lgguid="+$("#lgguid").val();
					return;
				}
	   		});
	}
	// 取消
	function precel(){
		$("#employeeguids").val("");
		$("#malistguids").val("");
		$("#phoneurl").val("");
		$("#btsend").attr("disabled","");
		$("#subSend").attr("disabled","");
		$("#detail_Info").dialog("close");
	}
	
	//发送成功跳转完美通知历史界面
	 function sendRecord(taskId)
	 {
		var guId = $("#lgguid").val();
		closemessage();
		window.parent.openNewTab('1050-1400',base_path+"/per_queryNotHis.htm?method=find&taskId=" +taskId+ "&lgguid="+guId);
	 }
	 function closemessage()
	 {
		 $("#message").dialog("close");
		 //window.location.href = "per_sendNot.htm?method=find&lgguid="+$("#lgguid").val();
	 }
	 
	function setDefault()
	{
		if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_98'))) {
			var lguserid = $('#lguserid').val();
			var lgcorpcode = $('#lgcorpcode').val();
			var busCode = $("#busCode").val();
			var spUser = $("#spUser").val();
			$.post("per_sendNot.htm?method=setDefault", {
				lguserid: lguserid,
				lgcorpcode: lgcorpcode,
				busCode: busCode,
				spUser: spUser,
				flag: "3"			
				}, function(result) {
				if (result == "seccuss") {
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_99'));
					return;
				} 
				else if(result == "fail"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_100'));
					return;
				}
			});
		}
	}