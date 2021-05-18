//处理静态彩信发送时 选择彩信文件div转换    是选择模板还是上传模板

function changeTemplate(t)
{
	if (t=="1")
	{
		$("#mmsFileDiv").hide();
		$("#templateDiv").show();
		$("#mmsFileType").val("1");
	}else if (t=="2"){
		//将选择 模板中的选择按钮以及其彩信文件信息清除
		//$("input:radio[name='model']:checked").attr("checked",false);
		//$("#mmsTemplateFile").val("");
		$("#templateDiv").hide();
		$("#mmsFileDiv").show();
		$("#mmsFileType").val("2");
	}
}

//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}

//处理 静态彩信发送上传彩信模板。
function upMmsFile()
{
	var pathUrl = $("#pathUrl").val();
	var ipathUrl = $("#ipathUrl").val();
	var lguserid = $("#lguserid").val();
	var skinUrl = $("#skinUrl").val();
	var skinType = $("#skinType").val();
	var commonPath = $("#commonPath").val();
	var lgcorpcode=$("#lgcorpcode").val();
	
	
		var fName = $("#smmFile").val();
		fName = fName.substring(fName.lastIndexOf("\\")+1);
		if (fName == ""){
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_6"));
			return;
		}else if (fName != "" && !(/^(tms)$/.test(fName.toString().substring((fName.toString().lastIndexOf("."))+1).toLowerCase()))){alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_7"));
	        return;
	    }else{
			var oldUrl = $("#mmsFileName").attr("value");
			$.ajaxFileUpload ({ 
			    url:pathUrl+'/smm_sameMms.htm?method=uploadMmsFile&old='+oldUrl+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode, //处理上传文件的服务端 
			    secureuri:false, //是否启用安全提交，默认为false
			    fileElementId:"smmFile", //与页面处理代码中file相对应的ID值
			    dataType: 'json', //返回数据类型:text，xml，json，html,scritp,jsonp五种
			    success: function (data) {
					if(data == "" || data.length == 0){
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_8"));
						window.location.href = window.location.href;
						return;
					}
					if(data.oversize=="oversize")
					{
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_9"));
						return;
					}
					if(data.typeNotMatch=="typeNotMatch")
					{
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_10"));
						return;
					}
					
					if(data.tmsNotMatch=="tmsNotMatch"){
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_11"));
						return;
					}
					var arrmms = data.mmsinfomation.replaceAll("}&gt;{","}>{").split(">");
				    var eg = /#[pP]_[1-9][0-9]*#/g;
				    var paramCnt =0;//参数个数
				    for (var i = 1; i <arrmms.length; i++)
			        {          
			           var textt1 = arrmms[i];
			           textt1=textt1.replace(/\r\n/g,"<BR/>")   
					   textt1=textt1.replace(/\n/g,"<BR/>"); 
			    	   var data1 = $.parseJSON(textt1);
			    	   if(data1.text != null){
			    			var arr1 = data1.text.match(eg);//返回数组 
			    			if(arr1 != null)
			    			{
				    			for(var j=0;j<arr1.length;j++)
				    			{
				    				var rstr = arr1[j].toUpperCase(); 
									var pc = rstr.substring(rstr.indexOf("#P_")+3,rstr.lastIndexOf("#"));
									var pci = parseInt(pc);
									if(pci > paramCnt)
									{
										paramCnt = pci;
									}
				    			}
			    			}
			    	   }
			        }
					if(paramCnt>0)
					{
						//动态
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_12"));
						return;
					}
					else
					{
						$("#mmsFileName").attr("value",data.mmsFileName);
						$("#mmstemplateid").val("");
						$("#mmstemplateurl").val("");
						$("#teplortms").attr("value","2");
						
			         var ss = "<div style='float:left;' class='div_bg'>"
					+"<label><img border='0' src='"+ipathUrl+"/img/fileimg.png'></img>&nbsp;"+fName+"</label>"
					+"&nbsp;&nbsp;&nbsp;&nbsp;<font onclick='doPreview(\""+data.mmsFileName+"\")' style='color:#095AD1;cursor:pointer;'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_13") + "</font>&nbsp;&nbsp;"
						+"<font onclick='delmmsfile(\""+data.mmsFileName+"\")' style='color:#095AD1;cursor:pointer;'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_14") + "</font></div>" ;
			         
				    	$("#templatepre").empty();
				    	$("#templatepre").html(ss);
				    	$("#templatepre").show();
				    	//$("#upfile").empty();
				    	//$("#upfile").html("重新上传");
					}
				
			    } 
			 });
		}
		$('#smmFile').replaceWith('<input id="smmFile" type="file" name="smmFile"  onchange="upMmsFile();" />');
	       
	
}
//删除彩信tms文件
function delmmsfile(mmsUrl)
{
	var path = $("#pathUrl").val();
	$.post(path+"/smm_sameMms.htm",
	 	     {method:"delMmsFile",mmsUrl:mmsUrl},
	 	     function(returnMsg){
	 	    	if(returnMsg.indexOf("html") > 0){
	    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_15"));
	    			window.location.href = window.location.href;
	    		    return;
	    		}else if(returnMsg == ""){
	    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
	    		    return;
	    		}else{
	    			//$("#upTip").empty();
			    	//$("#upTip").hide();
			    	//$("#upfile").empty();
			    	//$("#upfile").html("上传附件");
			    	//$("#mmsFileName").val("");
	    			$("#mmstemplateid").val("");
	    			$("#mmstemplateurl").val("");
	    			$("#templatepre").empty();
	    			
	    			$("#teplortms").val("");
	    			$("#mmsFileName").val("");
	    			$("#templatepre").css("display","none");
	    		}
	});
}
//处理点查看模板时访问的页面
function openTemp(r)
{
	var path = $("#pathUrl").val();
	window.open( path + "/fileUpload/fileDownload/" + r,"_blank");	
}

//处理单个新增手机号码 type 1自动  2手动
function addphone(type){
	var phone=$.trim($("#tph").val());
	var ipath = $("#ipathUrl").val();
	var commonPath = $("#commonPath").val();

	if(type == "2"){
		if(phone.length == 0 || "" == phone){
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_17"));
			return;
		}
	}
	if(phone.length == 11){
 		$.post("smm_sameMms.htm",
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
       				$("#infomaTable").append("<tr id='"+phone+"'><td align='center'  class='div_bd' style='border-left: 0;border-right:0;' name='ConR'  valign='middle'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_18")+ "</td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"+phone+"</td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"
       				+"<a onclick=delPhone('"+phone+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
       				$("#phoneStr").val(phoneStr+phone+",");
					$("#tph").val("");
				}else{
					//手机号码已存在
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_19"));
					$("#tph").val("");
					return;
       			}  
      		}else{
      			//手机不合法
      			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_20"));
      			$("#tph").val("");
      			return;
      		}
  		});
	}else{
		if(type == "2"){
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_20"));
			$("#tph").val("");
  			return;
		}
	}
}
//处理是导入文件
var fileCount=0;
//对 输入框中的手工 添加的手机号码/文件删除做删除操作
function delPhone(phoneNum){
	if(phoneNum.indexOf("tr")==0){
		if(confirm( getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_21")))
		{ 
			$("#"+phoneNum+"").remove();
			var numFile = phoneNum.replace("tr","numFile");
			var trs = $("#allfilename").html();
		    trs = trs.replace($("#"+numFile).val()+";","");
			$("#"+numFile).remove();
		    $("#allfilename").html(trs);
			//	fileCount = fileCount - 1;
			//	if(fileCount == 0){
			//		$("#chooseFiles").val("导入文件");
			//	}
		}	
	}else{
		if(confirm( getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_21")))
		{
			$("#"+phoneNum+"").remove();
			$("#phoneStr").val($("#phoneStr").val().replace(phoneNum+",",""));
		}
	}

}
function addFiles()
{
	var ipath = $("#ipathUrl").val();
	var commonPath = $("#commonPath").val();

	fileCount = fileCount + 1;
	$("#infomaTable").append("<tr id='tr"+fileCount+"'>" 
	+"<td align='center' style='border-left: 0;border-right:0;' valign='middle'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_22") + "</td>" 
	+"<td align='center' style='border-right: 0;border-right:0;' id = 'td"+fileCount+"'>"
	+"<input type='file' id='file"+fileCount+"' onchange=checkFile('"+fileCount+"') name='file"+fileCount+"' value=''/>" 
	+"</td><td align='center' style='border-right: 0;border-right:0;' valign='middle'>"
	+"<a onclick=delPhone('tr"+fileCount+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a>"
	+"</td></tr>");		
	$("#chooseFiles").val(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_74"));
}



//处理点预览操作
$(document).ready(function(){
			$("input[id='subSend']").click(function() {
						
						$("#allinputphone").val($("#phoneStr").val()+$("#inputphone").val()+",");
						$("#subSend").attr("disabled","disabled");
						//主题
						var taskname = $("#mmstaskname").val();
						//主题颜色   
						var taskDeColor = $("#mmstaskname").css("color");
						//彩信发送账号
						var mmsUser = $("#mmsUser").val();
						//彩信标题
						var tmName = $("#tmName").val();
						//发送类型 即时0 /定时1
						var sendtype = $("form[name='form2']").find("input:checked[name='sendType']").val();
						//发送时间
						var sendTime = $("form[name='form2']").find("input[name='sendtime']").val();
						var pathUrl = $("#pathUrl").val();
						var corpCode = $("#lgcorpcode").val();
						//是  1 选择彩信模板还是   2上传彩信文件
						//var mmsFileType = $("#mmsFileType").val();
						var mmsFileType = $("#teplortms").val();
						// 选择的彩信模板ID
						//var mmsfileId = $("form[name='form2']").find("input:checked[name='model']").val();
						var mmsfileId = $("#mmstemplateid").val();
						// 上传的彩信文件名
						var mmsFileName = $("#mmsFileName").val();
						
						var  trcount = $("#infomaTable tr");
						 if (taskname == "" || taskname.length == 0 || "#ccc" == taskDeColor){
					    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_23"));
					    	$("#subSend").attr("disabled","");
					    	return;
					    }else if (tmName == "" || tmName.length == 0){
					    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_24"));
					    	$("#subSend").attr("disabled","");
					    	return;
					    }else if (trcount.size() == 1){
					    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_25"));
					    	$("#subSend").attr("disabled","");
					    	return;
					    }else if(mmsFileType == "" || mmsFileType.length == 0){
							//彩信文件类型
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_26"));
							$("#subSend").attr("disabled","");
							return;
						 }else if(mmsFileType == "1" && (mmsfileId == null || mmsfileId == "")){
							//彩信文件类型
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_26"));
							$("#subSend").attr("disabled","");
							return;
						 }else if(mmsFileType == "2"  && mmsFileName == ""){
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_26"));
							$("#subSend").attr("disabled","");
							return;
						 }else if (mmsUser == "" || mmsUser == null){
						    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_27"));
						    	$("#subSend").attr("disabled","");
						    	return;
						}else if (sendTime == "" && sendtype == "1"){
					    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_28"));
					    	$("#subSend").attr("disabled","");
					    	return;
					    }
						 var taskname1=getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_96");
						 if(taskname==taskname1){
							 taskname="";
						 }
				    	$.post(pathUrl+"/smm_sameMms.htm?method=staMMSCheckTimeBlackFile",
				    			{tmName:tmName,
				    			 corpCode:corpCode,
				    			 sendtype:sendtype,
				    			 mmsFileType:mmsFileType,
				    			 mmsfileId:mmsfileId,
				    			 mmsFileName:mmsFileName
				    			},function(result){
						    		if(result.indexOf("html") > 0){
						    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_29"));
						    			window.location.href = window.location.href;
						    		    return;
						    		}else if(result == ""){
						    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
						    		    return;
						    		}
						    		if(result == "success"){
						    			
						    		}else{
						    			var preStr = result.substring(0,result.indexOf("&"));
						    			var backStr = result.substring(result.indexOf("&")+1);
						    			if(preStr == "stage1"){
						    				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_30") + backStr);
						    				$("#subSend").attr("disabled","");
						    				return;
						    			}else if(preStr == "stage2"){
						    				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_31"));
						    				$("#subSend").attr("disabled","");
						    				return;
						    			}else if(preStr == "stage3"){
						    				var serverTime = backStr;
											var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
											var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                                            date1.setMinutes(date1.getMinutes() - 1, 0, 0);
											if (date1 <= date2) {
												alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_32"));
												$("#sendtime").val("");
												$("#subSend").attr("disabled","");
												return;
											}
						    			}else if(preStr=="stage4"){
						    				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_33")+backStr);
						    				$("#subSend").attr("disabled","");
						    				return;
						    			}
						    		}
						    		$("#probar").dialog({
						    				modal:true,
						    				title:getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_34"), 
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
						    		$("form[name='form2']").attr("action",$("form[name='form2']").attr("action")+"&lguserid="+$("#lguserid").val());
						    		$("form[name='form2']").submit();
						    	
				 });
			});
		});




//静态彩信发送界面预览窗口
function preSend(data)
{
	var pathUrl = $("#pathUrl").val();
	var ipath = $("#ipathUrl").val();
	var commonPath = $("#commonPath").val();
	$("#error").val("1");	
	window.clearInterval(dd);
	//关闭旋转界面
	$("#probar").dialog("close");
	$(".ui-dialog-titlebar").show();
	if(data=="error"){
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_35"));
		$("#subSend").attr("disabled",false);
		return;
	}else if(data=="overstep"){
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_97"));
		$("#subSend").attr("disabled",false);
		return;
	}else if(data=="overSize"){
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_37"));
		$("#subSend").attr("disabled",false);
		return;
	}
	$("#newtable").empty();
	$("#preStr").val(data);
	
	if(data=="noPhone")
	{
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_38"));
		$("#subSend").attr("disabled",false);
		return;
	}else{
		var array=data.split('&');
		$("#counts").text(array[0]);
	    $("#effs").text(array[1]);
	    $("#legers").text(array[2]);
	    $("#sames").text(array[3]);
	    $("#blacks").text(array[4]);
	    
	    var errerCount = parseInt(array[0]) - parseInt(array[1]);
	    $("#downlinkinfo").css("visibility","visible");
	    if(errerCount != 0){
	    	$("#errerCount").text(errerCount);
	    }else{
	    	$("#errerCount").text("0");
	    	$("#downlinkinfo").css("visibility","hidden");
	    }
	    
	    if(array[2] != "0"){
	    	$("#legers").attr("style","color:red");
	    }else{
	    	$("#legers").attr("style","color:black");
	    }
	    if(array[3] != "0"){
	    	$("#sames").attr("style","color:red");
	    }else{
	    	$("#sames").attr("style","color:black");
	    }
	    if(array[4] != "0"){
	    	$("#blacks").attr("style","color:red");
	    }else{
	    	$("#blacks").attr("style","color:black");
	    }
	    $("#yct").text(array[6]);
	    $("#ct").text(array[7]);
	    $("#isChargings").val(array[8]);
	    var yeresult = array[9];
	    checkspye(yeresult);
	    
	    //过滤完的号码文件
	    $("#upNumberPhoneUrl").val(array[5]);
	    //判断有效号码数
	    if(array[1] == 0)
	    {
	    	$("#newtable").append("</tr><tr><td colspan='3' style='border: 1px  solid; height: 25px;' align='center'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_39") + "</td></tr>");
	    	//将确定按钮置灰
	    	//$(".ui-dialog-buttonpane button").first().attr("disabled","disabled");
	    	$("#subSend1").attr("disabled","disabled");
	    }
		//是  1 选择彩信模板还是   2上传彩信文件
		//var mmsFileType = $("#mmsFileType").val();
	    var mmsFileType = $("#teplortms").val();
	    
		// 选择的彩信文件信息
		//var mmsTemplateFile = $("#mmsTemplateFile").val();
		var mmsTemplateFile = $("#mmstemplateurl").val();
		// 上传的彩信文件信息
		var mmsFileName = $("#mmsFileName").val();
		var msg = "";
		if(mmsFileType == "1"){
			msg = mmsTemplateFile;
		}else if(mmsFileType == "2"){
			msg = mmsFileName;
		}
	    inits1();
		$.post(pathUrl+"/smm_sameMms.htm?method=getTmMsg",{tmUrl:msg},function(returnMsg){
			if (returnMsg != null && returnMsg != "null" && returnMsg != ""){
				arr2 = returnMsg.split(">");
				if(arr2[0] != null && arr2[0] != ""){
					var da = $.parseJSON(arr2[0]);
					ttime1 = (da.totaltime/1000);
				}
				index1 = 1;
				$("#screen1").empty();
				$("#pointer1").empty();
				$("#nextpage1").empty();
				$("#currentpage1").empty();
				$("#screen1").html("<center style='padding-top:120px'><img src='"+ipath+"/img/play.png' style='cursor:pointer;' title='" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_3")+ "' onclick='javascript:play1();'/></center>");
			}
		    $("#detail_Info").css("display","block");
		    $("#detail_Info").dialog("open");
		})
	} 
	
}



function checkspye(yeresult)
{
	var altstr = "";
	 $("#nosendReason").empty();
	if(yeresult.indexOf("lessgwfee") == 0)
	{
		altstr = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_40")+yeresult.split("=")[1];
		$("#subSend1").attr("disabled","disabled");
	}else if(yeresult=="feefail" || yeresult=="nogwfee")
	{
		altstr = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_41");
		$("#subSend1").attr("disabled","disabled");
	}
	$("#nosendReason").html(altstr);
	$("#notsend").css("visibility","visible");
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
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_42"));
					window.clearInterval(dd);
					window.location.href='smm_sameMms.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			if(errorNum==0)
			{
				errorNum=1;
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_43"));
			}
			window.clearInterval(dd);
			window.location.href='smm_sameMms.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
}



function checkError()
{
	if($("#error").val()=="0")
	{
		window.clearInterval(dd);
		$("#probar").dialog("close");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_44"));
		$("#subSend").attr("disabled",false);
	}
}


//验证上传文件的时候判断其文件是否合法。
function checkFile(id){
	var fn = $("#file"+id).val();
	 if (fn != "" && !(/^(txt|xls|xlsx|et)$/.test(fn.toString().substring((fn.toString().lastIndexOf("."))+1))))
    {
        alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_45"));
        $("#file"+id).remove();
        $("#td"+id).html("<input type='file' id='file"+id+"' onchange=checkFile('"+id+"') name='file"+id+"' value=''/>");
        return;
    }
}



//错误上传内容下载
function uploadbadFiles()
{
    var upNumberPhoneUrl = $("#upNumberPhoneUrl").val();
	var pathUrl = $("#pathUrl").val();
    var badurl = upNumberPhoneUrl.replace(".txt","_bad.txt");
   	$.post(pathUrl+"/smm_sameMms.htm?method=goToFile", {
		url : badurl
	},function(result) {
			if (result == "true") {
				download_href(pathUrl+"/doExport.hts?u="+badurl);				
			} else if (result == "false"){
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_46"));
			}else{
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
			}
				
	});
}



var noFileModelCount = 1;
function addFilesNoModel(){
	var ipath = $("#ipathUrl").val();
	var commonPath = $("#commonPath").val();
	var obj=$("#numFile"+noFileModelCount);
	var pathValue="";
	pathValue = $("#numFile"+noFileModelCount).val();
	var index = pathValue.lastIndexOf("\\");
	var name = pathValue.substring(index + 1);
	if(name.length >12)
	{
	   name = name.substring(0,12)+"....";
	}
	if(checkFile("numFile"+noFileModelCount)){  	
		if ($("#tr"+noFileModelCount).length == 0){ 	  
	     $("#infomaTable").append("<tr style='background-color:#ffffff' id='tr"+noFileModelCount+"'><td  style='border-left:0;border-right:0' align='center'  class='div_bd' valign='middle' >" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_75") + "</td><td  style='border-left:0;border-right:0' align='center'  class='div_bd' name='FName' valign='middle'>"+name+"</td><td  style='border-left:0;border-right:0' align='center'  class='div_bd' name='Kind' valign='middle'><a onclick=delPhone('tr"+noFileModelCount+"')><img border='0'  src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");       			 
		 $("#numFile"+noFileModelCount).css("display","none");
		 noFileModelCount++; 
		 var ss = $("#filesdiv").html();      			
		 var ss1 = "<input type='file' id='numFile"+noFileModelCount+"' name='numFile"+noFileModelCount+"' value='' onchange='addFilesNoModel();' class='numfileclass'  style='height:43px;width: 85px;'>";
		 $("#filesdiv").prepend(ss1);
		 $("#allfilename").append(pathValue+";");
		}
	}
}



// 验证上传文件格式
function checkFile(name) {
	var fileObj = $("form[name='form2']").find("input[name='"+name+"']");
	if(fileObj.val() != "") {
		var fileName = fileObj.val();
		var index =fileName.lastIndexOf(".");
		var fileType =fileName.substring(index + 1).toLowerCase(); 
		var trs = $("#allfilename").html();
        if(trs.indexOf(fileName+';')>-1)
        {
        	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_47"));
	    	fileObj.after(fileObj.clone().val(""));   
	    	fileObj.remove(); 
    		return false;
        }
		if (fileType != "txt"  && fileType != "xls" && fileType != "xlsx" && fileType != "et") {
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_48"));
    		fileObj.after(fileObj.clone().val(""));   
    		fileObj.remove(); 
    		return false;
		} else {
			return true;
		}
	}
	else {
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_49"));
		return false;
	}
}



//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


$(document).ready(
		function(){
			//将彩信模板设置为选择
			//changeTemplate("1")
			//处理将输入框中的内容置空
			inputTipText();
			//加载格式提示处的悬浮框
			floating("downlinks","infomodel1");
			getLoginInfo("#hiddenValueDiv");
			
			var Dwidth  = 535;//非frame4.0默认宽度
			var Dheight = 550;//非frame4.0默认高度
			if(CstlyeSkin.indexOf("frame4.0") != -1) {
				Dwidth = 690;
				Dheight = 565;
			}
			//选择发送对象 
			$("#infoDiv").dialog({
				autoOpen: false,
				height:Dheight,
				width: Dwidth,
				resizable:false,
				modal: true
			});
			//初始化彩信预览DIV
			
			var h = 500;
			if (navigator.appName == "Netscape")
		    {
		    	h = 500;
		    }
			$("#tempView").dialog({
				autoOpen: false,
				height:h,
				width: 290,
				modal: true,
				resizable:false,
				close:function(){
				    cplaytime = 0;
					nplaytime = -1;
					$("#screen").empty();
					clearInterval(ttimer); 
				}
			});



			
			//彩信模板中CSS效果 
			$("#modelTable tbody tr").hover(function() {
				$(this).addClass("div_hover_bg img_hover");
			}, function() {
				$(this).removeClass("div_hover_bg img_hover");
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

			$("#picTab td").hover(function(){
				$(this).removeClass("div_bg");
				$(this).addClass("div_hover_bg img_hover");
			},function(){
				$(this).removeClass("div_hover_bg img_hover");
				$(this).addClass("div_bg");
			});
			var skinType = $("#skinType").val();
			$("#foldIcon1").attr("src",skinType+"/images/fold.png");
		    $("#errerDiv").css("visibility","visible");
			$('#moreSelect1').show();
			$('#u_o_c_explain1').toggle(function(){
					$("#foldIcon1").removeClass("unfold");
					$("#foldIcon1").addClass("fold");
					$("#errerDiv").css("visibility","hidden");
					$('#moreSelect1').hide();
				},function(){
					$("#foldIcon1").removeClass("fold");
					$("#foldIcon1").addClass("unfold");
				    $("#errerDiv").css("visibility","visible");
					$('#moreSelect1').show();
			});
			
			//短信预览窗口 
			$("#detail_Info").dialog({
				autoOpen: false,
				modal:true,
				title:getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_50"), 
				height:530,
				width:520,
				closeOnEscape: false,
				resizable:false,
				open:function(){
					//默认打开错误号码详情-
					$("#foldIcon1").attr("src",skinType+"/images/fold.png");
				    $("#errerDiv").css("visibility","visible");
					$('#moreSelect1').show();
					//这里是处理打开预览界面的时候， 处理界面的显示信息
					$(".ui-dialog-titlebar-close").hide();		
					//彩信余额 							 		 
			 		 var ct = $.trim($("#ct").text());
			 		//预发送条数	
			 		 var yct =$.trim($("#yct").text());
				
					 //是否计费 1是  2不是
					 var isChargings = $("#isChargings").val();
					
					if(isChargings == "1"){
						 $("#lesscount").css("visibility","visible");
						 if(eval(yct) == 0 ){
							 $("#nosendReason").empty();
							 $("#nosendReason").css("visibility","visible");
							 $("#nosendReason").html(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_51"));
							 $("#notsend").css("visibility","visible");
					 		//没有预发号码的时候 ，隐藏信息，并且把按钮置灰
				 		    $("#subSend1").attr("disabled","disabled");
				 		    return;
						 }else if(eval(ct) == 0){
							 $("#nosendReason").empty();
							 $("#nosendReason").html(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_52"));
							 $("#notsend").css("visibility","visible");
					 		//没有预发号码的时候 ，隐藏信息，并且把按钮置灰
				 		    $("#subSend1").attr("disabled","disabled");
				 		    return;
						}else if(eval(ct) < eval(yct)){
							 $("#nosendReason").empty();
							 $("#nosendReason").html(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_53"));
							 $("#notsend").css("visibility","visible");
					 		//没有预发号码的时候 ，隐藏信息，并且把按钮置
				 		    $("#subSend1").attr("disabled","disabled");
				 		    return;
						}
					}else{
						//判断不是计费的情况下
						 $("#lesscount").css("visibility","hidden");
						 if(eval(yct) == 0){
							 $("#nosendReason").empty();
							 $("#nosendReason").html(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_54"));
							 $("#notsend").css("visibility","visible");
					 		//没有预发号码的时候 ，隐藏信息，并且把按钮置灰
				 		    $("#subSend1").attr("disabled","disabled");
				 		    return;
						 }
					}
					 
				},
				 close:function(){
					//设置预览提交时的错误标志 
					$("#error").val("");
					$("#subSend").attr("disabled","");
					$(".ui-dialog-titlebar-close").show();
					$("#subSend1").attr("disabled","");
				}
			});	
			
	});


	// 彩信预览
	function doPreview(msg,dsFlag)
	{
		$(".ui-dialog-titlebar-close").show();
		inits();
		var pathUrl = $("#pathUrl").val();
		$.post(pathUrl+"/smm_sameMms.htm?method=getTmMsg",{tmUrl:msg},function(result){
			if (result != null && result != "null" && result != "")
			{
				arr = result.split(">");
				if(arr[0] != null && arr[0] != "")
				{
					var da = $.parseJSON(arr[0]);
					ttime = (da.totaltime/1000);
				}
				index = 1;
				$("#screen").empty();
				$("#pointer").empty();
				$("#nextpage").empty();
				$("#currentpage").empty();
				parmCount =null;
				$("#inputParamCnt1").empty();
				$("#tempView").dialog("open");
				play(dsFlag);
			}else{
	             alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_55"));
			}
		});
	}
	//修改Dialog标题样式
	function changeDialogTitleCss(id) {
	    var $titleBar = $("#ui-dialog-title-" + id);
	    $titleBar.parent().addClass("titleBar");
	    $titleBar.addClass("titleBarTxt");
	}

	
	// 点击选择对象按钮，弹出选择对象界面
	function showInfo()
	{  
		var Slguserid= $("#lguserid").val();
		var ScommonPath= $("#commonPath").val();
		var $lgcorpcode = $("#lgcorpcode").val();
		var $ipathUrl = $("#ipathUrl").val();
		//选择类型查询  1.员工通讯录   2.客户通讯录  3.群组(员工+客户) 4.员工群组 5.客户群组 6.客户属性 7.签约用户
		var chooseType = "1,2,3";
		if(CstlyeSkin.indexOf("frame4.0") != -1) {
			$("#flowFrame").attr("src",ScommonPath+"/common/selectUserInfo.jsp?lguserid="+Slguserid+"&chooseType="+chooseType+"&lgcorpcode="+$lgcorpcode);
			changeDialogTitleCss("infoDiv");
		}else{//非4.0逻辑
			$("#flowFrame").attr("src",$ipathUrl+"/smm_sameMmsSelUser.jsp?lguserid="+Slguserid);
		}
		
		$("#infoDiv").dialog("open");
	}
	//选择对象界面的确定操作
	function doOk(){
			// 这里处理是否 右边选择的记录$("#right option:selected").size()
			var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();

			  //代表的是员工机构IDS 
			  $("#empDepIds").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
			  //代表的是客户机构IDS
			  $("#cliDepIds").val($(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val());
			  //代表的是群组IDS 
			  $("#groupIds").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
			  //代表的是员工IDS
			  $("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
			  //代表的是客户IDS
			  $("#cliIds").val($(window.frames['flowFrame'].document).find("#clientIds").val());
			  //代表的是外部人员IDS 
			  $("#malIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
				//选择用户对象号码串 
			  $("#userMoblieStr").val($(window.frames['flowFrame'].document).find("#moblieStrs").val());
			  
			  //4.0增加回显
			  $("#rightSelectedUserOption").val($(window.frames['flowFrame'].document).find("#right").html());
			  //员工机构ids
			  $("#depIdStr").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
			  //群组ids
			  $("#groupStr").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
			  
			  
    		//处理选择对象的操作
    		var havOne = $("#havOne").val();
			if(havOne == "2" && optionSize > 0){
				var commonPath = $("#commonPath").val();
				var manCount = $(window.frames['flowFrame'].document).find("#manCount").html()+"";
				$("#infomaTable").append("<tr id='addressList'><td align='center' class='div_bd' height='16px' width='85px' style='border-left: 0;border-right: 0;'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_56")+ "</td>"
		       			+"<td align='center'  class='div_bd' width='210px' style='color:blue;border-right: 0;border-left: 0;cursor:pointer;'><a onclick='javascript:showInfo();'>" +
		       			"<font style='color:#0e5ad1'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_57")+"<label style='color:#0e5ad1' id='choiceNum'>"+ manCount+"</label>人)</a></td>"
		       			+"<td align='center'  class='div_bd' style='border-right: 0;border-left: 0;'>"
		       			+"<a onclick='javascript:doNo();'><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
				$("#havOne").val("1");
			}else{
				//如果未选择对象,则清空一栏
				if(optionSize == 0){
					clearUser();
				}else{
					var manCount1 = $(window.frames['flowFrame'].document).find("#manCount").html()+"";
					$("#choiceNum").empty();
					$("#choiceNum").append(manCount1);
				}
			}
			$("#infoDiv").dialog("close");
	}

	//点击选择对象中的  人员取消--%>	
	function doNo(){
		//4.0清空选择的对象
		$("#rightSelectedUserOption").val("");
		var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
		if(optionSize == 0){
			clearUser();
			//$("#infoDiv").dialog("close");
			return;
		}
		if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_59"))){
			  //代表的是选择的ID--%>
			//  $(window.frames['flowFrame'].document).find("#choiceId").val("");
			  //代表的是选择的名称 --%>
			//  $(window.frames['flowFrame'].document).find("#choiceName").val("");
			  //代表的是员工机构IDS --%>
			  $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
			  //代表的是客户机构IDS --%>
			  $(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val("");
			  //代表的是群组IDS --%>
			  $(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
			  //代表的是分页索引第一页 --%>
			  $(window.frames['flowFrame'].document).find("#pageIndex").val("1");
			  //代表的是员工IDS --%>
			  $(window.frames['flowFrame'].document).find("#employeeIds").val("");
			  //代表的是客户IDS--%>
			  $(window.frames['flowFrame'].document).find("#clientIds").val("");
			  //代表的是外部人员IDS --%>
			  $(window.frames['flowFrame'].document).find("#malistIds").val("");
			  $(window.frames['flowFrame'].document).find("#right").empty();
			  $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
				 //选择用户对象号码串 --%>
			  $(window.frames['flowFrame'].document).find("#moblieStrs").val("");
			  $(window.frames['flowFrame'].document).find("#manCount").html(0);

			  clearUser();
		   
	    	// $("#infoDiv").dialog("close");
		 }
	}
	// 清空主界面的选择对象的数据--%>
	function clearUser(){
	 	//点取消选择对象的时候，把TABLE里的选择对象一TR去掉--%>
		$("#addressList").remove();
		$("#havOne").val("2");
		// 把主页面的数据也清空--%>
		$("#empDepIds").val("");
		$("#cliDepIds").val("");
		$("#groupIds").val("");
		$("#empIds").val("");
		$("#cliIds").val("");
		$("#malIds").val("");
		$("#userMoblieStr").val("");
	}

	
	// 选择彩信模板的设值--%>
	function setMmsTmgValue(tmg){
		$("#mmstemplateurl").val(tmg);
	}
	// 重置--%>
	function reloadPage(){
   		//window.location.href='smm_sameMms.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		window.location.href = window.location.href;
     }
	// 预览后提交发送--%>
	function previewSub(){
		$("#subSend1").attr("disabled","disabled");
		var sendtype = $("form[name='form2']").find("input:checked[name='sendType']").val();
		var sendtime = $("form[name='form2']").find("input[name='sendtime']").val();
		var pathUrl = $("#pathUrl").val();
		// 彩信任务ID--%>
		var taskId = $("#taskId").val();
		// 号码文件地址 --%>
		var phoneFileUrl = $("#upNumberPhoneUrl").val();
		//用户ID --%>
		var lguserid = $("#lguserid").val();
		// 企业编码--%>
		var lgcorpcode = $("#lgcorpcode").val();
		//彩信发送账号 --%>
		var mmsSpuser = $("#mmsUser").val();
		// 彩信主题--%>
		var mmsTaskName = $("#mmstaskname").val();
		//彩信标题 --%>
		var mmsTitle = $("#tmName").val();
		// 模板类型   1选择  2上传--%>
		//var mmsTemplateType = $("#mmsFileType").val();
		var mmsTemplateType = $("#teplortms").val();
		
		// 彩信文件url--%>
		var mmsFileUrl = "";
		var mmsTemplateId = "";
		if(mmsTemplateType == "1"){
			//选择的彩信TMS文件信息 --%>
			mmsFileUrl = $("#mmstemplateurl").val();
			//彩信选择模板ID --%>
			//mmsTemplateId = $("form[name='form2']").find("input:checked[name='model']").val();
			mmsTemplateId = $("#mmstemplateid").val();
		}else if(mmsTemplateType == "2"){
			//上传的彩信TMS文件名称 --%>
			mmsFileUrl = $("#mmsFileName").val();
		}
		//总数 --%>
		
		var counts = $.trim($("#counts").text());
		//有效数 --%>
		var effs = $.trim($("#effs").text());
		 
		 var taskname1=getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_96");
		 if(mmsTaskName==taskname1){
			 mmsTaskName="";
		 }
		//处理提交彩信--%>
		$.post(pathUrl+"/smm_sameMms.htm?method=add",
				{sendtime:sendtime,
				sendtype:sendtype,
				taskId:taskId,
				phoneFileUrl:phoneFileUrl,
				lguserid:lguserid,
				lgcorpcode:lgcorpcode,
				mmsSpuser:mmsSpuser,
				mmsTitle:mmsTitle,
				mmsTemplateId:mmsTemplateId,
				mmsTaskName:mmsTaskName,
				mmsTemplateType:mmsTemplateType,
				mmsFileUrl:mmsFileUrl,
				counts:counts,
				effs:effs
				},function(msg){
		    		if(msg.indexOf("html") > 0){
		    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_60"));
		    			window.location.href = window.location.href;
		    		    return;
		    		}else if(msg == "" || msg == "errer"){
		    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
		    			$("#subSend1").attr("disabled","");
		    		    return;
		    		}else if(msg == "timeout"){
		    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_61"));
		    			$("#subSend1").attr("disabled","");
		    		    return;
			    	}else if(msg == "uploadFileFail"){
			    		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_62"));
			    		$("#subSend1").attr("disabled","");
		    		    return;
			    	}else if(msg == "uploadTmsFail"){
			    		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_63"));
			    		$("#subSend1").attr("disabled","");
		    		    return;
			    	}else if("nogwfee"==msg || "feefail"==msg || "feeerror"==msg){
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_64"));
			    		$("#subSend1").attr("disabled","");
			    		return;
					}else if(msg.indexOf("lessgwfee")==0){
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_65"));
						$("#subSend1").attr("disabled","");
			    		return;
					}else if(msg.substr(0,3) == "000"){
						$("#oldTaskId").attr("value",msg.substr(4));
						$("#message").dialog({width:300,height:180,close:function(){
							reloadPage();
						}});
						$("#message").dialog("open");
					}else{
			    		if(msg.indexOf("empex") == 0){
			    			msg = msg.substr(5);
    					}
			    		alert(msg);
			    		$("#detail_Info").dialog("close");
			    		$("#subSend1").attr("disabled","");
			    		reloadPage();
			    	}
		    		//处理返回结果--%>

					
					});

	}

	// 预览后取消--%>
	function previewReset(){
		$("#subSend").attr("disabled","");
		//设置预览提交时的错误标志 --%>
		$("#error").val("");
		$("#detail_Info").dialog("close");
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
	function toAddTemplate(){
	    var path = $("#pathUrl").val();
		var lgguid = $("#lgguid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		window.location.href = path+'/dmm_sendDynMMS.htm?method=doAdd&templateType=0&lgguid='+lgguid+'&lgcorpcode='+lgcorpcode;
	
	}


	// 选择彩信模板/
	function chooseMmsTpl()
	{
		$(".ui-dialog-titlebar-close").show();
		var pathUrl = $("#pathUrl").val();
		var frameSrc = $("#tempFrame").attr("src");
		if(frameSrc.indexOf("blank.jsp") > 0)
		{
			var lguserid = $("#lguserid").val();
			frameSrc = pathUrl+"/smm_sameMms.htm?method=getLfTemplateByMms&tmpltype=0&lguserid="+lguserid;
			$("#tempFrame").attr("src",frameSrc);
		}
		var hei=$(window).height();
		if(hei>970){
			hei=660;
		}else{
			hei=$(window).height()*0.65;
		}
		//初始化彩信模板DIV
		$("#tmplDiv").dialog({
			autoOpen: false,
			height:hei,
			width: 650,
			resizable:false,
			modal: true,
			open:function(){
			$(this).height(660);
			},
			close:function(){
			}
		});
		$("#tmplDiv").dialog("open");
	}

	// 取消彩信模板
	function tempNo()
	{
		$("#mmstemplateid").val("");
		$("#mmstemplateurl").val("");
		$("#templatepre").empty();

		$("#mmsFileName").val("");

		// 选择彩信模板1还是上传彩信文件2 ""为未选择
		$("#teplortms").val("");
		
		$("#templatepre").css("display","none");
	}
	// 关闭彩信模板
	function closeDialog(){
		$("#tmplDiv").dialog("close");
	}
	
	// 开启新的标签
	function openTemDiv(menuCode) {
		var lgcorpcode = $("#lgcorpcode").val();
		var pathUrl = $("#pathUrl").val();
		window.parent.openNewTab(menuCode,pathUrl+"/tem_mmsTemplate.htm?method=doAdd&type=1&lgcorpcode="+lgcorpcode);
	}
 //发送成功跳转发送记录查看界面
 function sendRecord(menuCode, guid)
 {
	closemessage();
	var taskid = $("#oldTaskId").val();
	reloadPage();
	window.parent.openNewTab(menuCode,base_path+"/mmt_mmsTask.htm?method=find&taskid="+taskid+"&lgguid="+guid+"&pageIndex="+1+"&pageSize="+15);
 }
 function closemessage()
 {
	 $("#message").dialog("close");
	 reloadPage();
 }
 //批量输入
 function bulkImport(){
	 $('#bulkImport_box').dialog({
				autoOpen: false,
				height: 415,
				width: 542,
				resizable:false,
				modal:true
			});
	$('#bulkImport_box').dialog('open');
/**	$('#bulkImport_box').dialog('open');
	if($('.bultPhone').size()==0){
		$('#bNum').html('0');
	}*/
	$('#importArea').blur();
	$('#importArea').html($('#importAreaTemp').html());
	// var num=$('#importAreaTemp').attr('data-num');
	// num=typeof num=='undefined' ? 0 : num;
	// $('#bNum').html(num);
 }
 function bultCancel(){
	 $('#bulkImport_box').dialog('close');
	 $('#importArea').html("");
 }

function formatTelNum(element){
	var str=$.trim($('#importArea').val());
	
	var reg=/[\s,、\n，]+/g;
	var result=str.replace(reg," ");
	var arr=result.split(" ");
	if(arr[arr.length-1]==""){
		var len=arr.length-1;
	}else{
		var len=arr.length;
	}
	$(element).html(len);
}
function previewTel(){
	if($('#importArea').val()==''){
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_66"));
		return false;
	}
	
	var phone=$('#bNum').html();
	//批量输入最大支持20000个号码
	if(20000 - phone < 0)
	{
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_67"));
		return false;
	}
	$('.bultPhone').remove();
	
	$("#infomaTable").append("<tr id='"+phone+"' class='bultPhone'><td align='center'  class='div_bd' style='border-left: 0;border-right:0;' name='ConR'  valign='middle'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_68") + "</td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'><a onclick='javascript:bulkImport();' style='cursor:pointer'><font styl<font style='color:#0e5ad1'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_69")+ "(<label style='color:#0e5ad1' id='Batchnput'>" + phone + "</label>人)</font></a></td>"
       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"
       				+"<a onclick=delinputphone('"+phone+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	var inputphone=$('#importArea').val();
	$('#importAreaTemp').html(inputphone).attr('data-num',phone);
	var reg=/[\s,、\n，]+/g;
	var result=inputphone.replace(reg,",");
	if(result.substr(0,1)==",")
	{
		result = result.substr(1);
	}
	$("#inputphone").val(result);
	bultCancel();
}

//数组去重
function distinctArray(arr){
	var tempArr=[],obj={};
	for(var i=0;i<arr.length;i++){
		if(!obj[arr[i]] && arr[i]!==''){
			tempArr.push(arr[i]);
			obj[arr[i]]=true;
		}
	}
	return tempArr;
}

function delinputphone(phone) {
	if (confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_70"))) {
		$("#" + phone).remove();
		$("#inputphone").val("");
		$("#importArea").val("");
		$('#importAreaTemp').val("").attr('data-num',0);
	}
}



