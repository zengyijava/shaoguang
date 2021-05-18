var empLangName = getJsLocaleMessage("common","common_empLangName");
$(document).ready(function() {
			initPage(total,pageIndex,pageSize,totalRec);
			$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
			$('#search').click(function(){submitForm();});
				var pathUrl = $("#pathUrl").val();
				$("#tmplDiv").dialog({
				autoOpen: false,
				height:755,
				width: 1200,
				resizable:false,
				modal: true,
				close: function() {
				window.location.replace(pathUrl + '/wg_apiBaseMage.htm')
				return;
 		}
			});
				
			
		$("#custDiv").dialog({
		autoOpen: false,
		height:520,
		width: 750,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			}
		});	
				
				
		});

		   //结束时间
			function rtime(){
				
			    var max = "2099-12-31 23:59:59";
			    var v = $("#startdate").attr("value");
			    var min = "1900-01-01 00:00:00";
				if(v.length != 0)
				{
				    var year = v.substring(0,4);
					var mon = v.substring(5,7);
					var day = 31;
					if (mon != "12")
					{
					    mon = String(parseInt(mon,10)+1);
					    if (mon.length == 1)
					    {
					        mon = "0"+mon;
					    }
					    switch(mon){
                            case "02":day = 28;break;
                            case "04":day = 30;break;
                            case "06":day = 30;break;
                            case "09":day = 30;break;
                            case "11":day = 30;break;
                            default:day = 31;
					    }
					}
					else
					{
					    year = String((parseInt(year,10)+1));
					    mon = "01";
					}
				}
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
			
			};
			
		//开始时间	
		function stime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#enddate").attr("value");
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
//			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
//				min = year+"-"+mon+"-01 00:00:00"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

		};

//弹出新增界面
function doadd(ecid,funtype)
{
	$('#ecid').val(ecid);
	$('#funtype').val(funtype);
	$("#addmethod").css("display","block");
	$('#addmethod').dialog({
		autoOpen: false,
		height: 280,
		width:520,
		resizable:false,
		modal:true
	});
	$('#addmethod').dialog('open');	
}

//保存方法
function save(){
		var ecid=$('#ecid').val();
		var funtype=$('#funtype').val();
		var methodNames=$('#methodNames').val();
		var clientIntName=$('#clientIntName').val();
		if($.trim(methodNames) == '')
		{
			/*请选择方法名称！*/
			alert(getJsLocaleMessage("txgl","txgl_js_baseApi_1"));
			return;
		}
		if($.trim(clientIntName) == '')
		{
			/*请填入客户接口名称！*/
			alert(getJsLocaleMessage("txgl","txgl_js_baseApi_2"));
			return;
		}
		var InterfaceName=$('#InterfaceName').val();
		if($.trim(InterfaceName) == '')
		{
			/*请填入客户接口！*/
			alert(getJsLocaleMessage("txgl","txgl_js_baseApi_3"));
			return;
		}
		
		var req_type=$('#req_type').val();
		var resp_type=$('#resp_type').val();
			$('#blok').attr('disabled', true);
			$('#blc').attr('disabled', true);
			$.post('wg_apiBaseMage.htm?method=add', {
				methodNames:methodNames,
				clientIntName: clientIntName,
				InterfaceName:InterfaceName,
				req_type:req_type,
				ecid:ecid,
				funtype:funtype,
				resp_type:resp_type
				},function(result)
			{
				if(result=="isExist"){
					/*企业中，该方法已经存在！*/
					alert(getJsLocaleMessage("txgl","txgl_js_baseApi_4"));
				$('#blok').attr('disabled', false);
				$('#blc').attr('disabled', false);
					return;
				}else if(result=="true"){
                    /*alert("保存成功！");*/
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_10"));
					$('#addmethod').dialog('close');
					window.location.href = window.location.href;
				}else{
			$('#blok').attr('disabled', false);
			$('#blc').attr('disabled', false);
				}
			});
		

}
//保存方法
function setStaut(ecid,enStatus){
		if(enStatus=="1"){
			/*您确定要禁用该企业吗？*/
			if(!confirm(getJsLocaleMessage("txgl","txgl_js_baseApi_5"))){
					return;
				}
		}
		if(enStatus=="0"){
			/*您确定要启用该企业吗？*/
			if(!confirm(getJsLocaleMessage("txgl","txgl_js_baseApi_6"))){
					return;
				}
		}
			$.post('wg_apiBaseMage.htm?method=setStatus', {
				ecid:ecid,
				enStatus:enStatus
				},function(result)
			{
				window.location.href = window.location.href;

			});
		

}

//方法详情
function funList(ecid,funtype){
	var pathUrl = $("#pathUrl").val();
	$(".ui-dialog-titlebar-close").show();
	var frameSrc = $("#tempFrame").attr("src");
	if(frameSrc.indexOf("blank.jsp") > 0)
	{
		var lguserid = $("#lguserid").val();
		frameSrc = pathUrl+"/wg_apiBaseMage.htm?method=funList&lguserid="+lguserid+"&ecid="+ecid+"&funtype="+funtype;
		$("#tempFrame").attr("src",frameSrc);
	}
	$("#tmplDiv").dialog("open");
}

function btcancel(){
	$("#addmethod").dialog("close");
	
}
//弹出新增方法框
 function toAdd(){
	$("#addtype").css("display","block");
	$('#addtype').dialog({
		autoOpen: false,
		height: 380,
		width: 620,
		resizable:false,
		modal:true
	});
	$('#addtype').dialog('open');	
	 
 }
 
 function savetype(){
	var code=$.trim($("#corp_code").val());
 	if(code==''){
 		/*企业信息不能为空*/
 		alert(getJsLocaleMessage("txgl","txgl_js_baseApi_7"));
 		return false;
 	}
	var ch_reg = /^[\u4e00-\u9fa5]+$/;//筛选包含汉字的字符  
	var $user_name = $("input[name=functiontype]");  
	if($.trim($user_name.val()) == ''){
		/*方法类型不能为空*/
	    alert(getJsLocaleMessage("txgl","txgl_js_baseApi_8"));$user_name.focus();return false;
	}  
	   if(escape($user_name.val()).indexOf("%u")!=-1){
		/*方法类型不能汉字*/
		alert(getJsLocaleMessage("txgl","txgl_js_baseApi_9"));$user_name.focus();return false;
		}

	/*方法类型保存后，不允许修改您确定要保存吗？*/
 	if(!confirm(getJsLocaleMessage("txgl","txgl_js_baseApi_10"))){
		return;
	}
 		$("#typesave").attr("disabled","disabled");
 	var funtype =$.trim($("#functiontype").val());
 		$.post('wg_apiBaseMage.htm?method=addFuntype', {
		functiontype:funtype,
		code:code
		},function(result)
		{
		$("#typesave").attr("disabled","");
		if(result=="existed"){
			/*企业已存在方法类型，请修改！*/
			alert(getJsLocaleMessage("txgl","txgl_js_baseApi_11"));
		}else if(result=="true"){
            /*保存成功！*/
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_10"));
			$('#addtype').dialog('close');
			window.location.href = window.location.href;
		}else{
			/*保存失败，请联系管理员！*/
			alert(getJsLocaleMessage("txgl","txgl_js_baseApi_12"));
		}
	});
}
 
 function canceltype(){
	 $("#addtype").dialog("close");
 }
 function choice(){
	 var pathUrl = $("#pathUrl").val();
	$(".ui-dialog-titlebar-close").show();
	var frameSrc = $("#custFrame").attr("src");
	if(frameSrc.indexOf("blank.jsp") > 0)
	{
		frameSrc = pathUrl+"/wg_apiBaseMage.htm?method=customersList";
		$("#custFrame").attr("src",frameSrc);
	}
	$("#custDiv").dialog("open");
 }
 
 //隐藏层
function closeDialog(){
	$("#custDiv").dialog("close");
}
