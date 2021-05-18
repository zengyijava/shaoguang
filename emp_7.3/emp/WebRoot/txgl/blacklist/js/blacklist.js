var empLangName = getJsLocaleMessage("common","common_empLangName");
//单个删除
function del(id,phone,busCode,keyId){
	//if(confirm("你确定要删除此条记录吗？")==true)
	if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_11"))==true)
	{
		var type ="del";
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$('#lgusername').val();
		$.post("bla_blacklistSvt.htm?method=delete",{ids:keyId,phone: phone,busCode: busCode,type:type,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
			if(result>0){
				//alert("删除成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_21"));
				//window.location = location;
				black();
			//}else alert("操作失败！");
			}else alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_18"));
		});
	}
}

//批量删除
function delAll()
{
	var type="delall";
	var ids="";
	var i=0;	
	$('input[name="checklist"]:checked').each(function(){	
		ids=ids+$(this).val()+",";
		i=i+1;
	});
	if(i==0){
		ids="";
	}else
	{
		ids=ids.substring(0,ids.lastIndexOf(','));
	}
	if(ids==""){
		//alert("请选择您要删除的黑名单信息！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_12"));
	}else{
		//if(confirm("您确定要删除"+i+"条信息?")==true){
		if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_13")+i+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_14"))==true){
			        var lgcorpcode =$("#lgcorpcode").val();
			        var lgusername =$('#lgusername').val();
					$.post("bla_blacklistSvt.htm?method=delete",{ids:ids,type:type,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
						if(result>0)
						{
							//alert("删除成功,共删除"+result+"条信息！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_15")+result+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_16"));
							//window.location = location;
							black();
						}else{
							//alert("删除失败！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_17"));
						}
					});
					
		}
	}
}
//切换状态
function changeState(i){
	
	    var statu=$.trim($("#blState"+i).attr("value"));
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$('#lgusername').val();
		
		$.post("bla_blacklistSvt.htm?method=changeState",{id:i,statu:statu,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
			if(result=="true"){
				//alert("更改成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_18"));
				$("#blState"+i).empty();
				if(statu == 1)
				{
				    $("#blState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_19")+"</option>");
				    $("#blState"+i).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_20")+"</option>");
				}
				else
				{
					$("#blState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_21")+"</option>");
					$("#blState"+i).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_22")+"</option>");
				}
				changeEmpSelect($("#blState"+i),80);

			//}else alert("操作失败！");
			}else alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_23"));
		});
}

function doEdit(id,spisuncm,spgate,blBusCode){
	var qd = getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_24");
	var qx = getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_25");
	$("#modify").css("display","block");
	$('#modify').dialog({
		autoOpen: false,
		resizable:false,
		height: 180,
		width: 400,
		modal:true,
		buttons:{
			//"确定":function(){
			qd:function(){
			update();
			},
			//"取消":function(){
			qx:function(){
				$('#modify').dialog('close');
			}
		}
	});
	$("#mobile").attr("value",$("#la"+id).text());
	$("#blId").val(id);
	$('#spisuncm').val(spisuncm);
	$('#blBusCode').val(blBusCode);
	$('#blSpgate').val(spgate);
	$('#modify').dialog('open');
}
function update() {   
    var phone =$("#mobile").val();
    var id =$("#blId").attr("value");
  	if(phone.length==0){
		//alert("手机号码不能为空！");
  		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_26"));
		$("#mobile").focus();
	}else if(phone.length<11){
		//alert("手机号码不能小于11位！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_27"));
		$("#mobile").focus();
	}else
 	if(phone!= $("#la"+id).text())
	{    
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$('#lgusername').val();
		$.ajax({
			type: "POST",
			url: "bla_blacklistSvt.htm?method=changePhone",
			data: {phone:phone,id:id,spisuncm:$("#spisuncm").val(),
			busCode:$("#blBusCode").val(),spgate:$("#blSpgate").val(),lgcorpcode:lgcorpcode,lgusername:lgusername},
			    success: function(result){
				if(result=="mid"){
					//alert("无效的手机号码，请检查是否输入了对应通道运营商的有效格式的手机号码！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_28"));
					$('#mobile').select();
				}else
				if(result=="exists"){
			 		//alert("该黑名单号码已存在，请重新输入！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_29"));
			 		$('#mobile').select();
				}else
				if(result=="true"){
			 		//alert("修改成功！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_30"));
			 		$('#modify').dialog('close');
			 		$("#la"+id).text(phone);
				}else{
					//alert("修改失败！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_31"));
				}
   			}
		});
	}else 
	{
		//alert("所录入黑名单号码未改变！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_32"));
	}
 }


//弹出新增界面
function doadd()
{
	$("#addmmsBl").css("display","block");
	$('#addmmsBl').dialog({
		autoOpen: false,
		height: 550,
		width: 500,
		resizable:false,
		modal:true
	});
	$('#addmmsBl').dialog('open');	
}

//弹出导入界面
function doupload()
{
	$("#uploadmmsBl").css("display","block");
	$('#uploadmmsBl').dialog({
		autoOpen: false,
		height: 550,
		width: "zh_HK" === empLangName? 500:450,
		resizable:false,
		modal:true
	});
	$('#uploadmmsBl').dialog('open');
}

//弹出导出界面
function exportAll()
{
    var empLangName = getJsLocaleMessage("common","common_empLangName");
	$("#exportDiv").css("display","block");
	$('#exportDiv').dialog({
		autoOpen: false,
		height: 160,
		width: "zh_HK" === empLangName? 450:340,
		resizable:false,
		modal:true
	});
	$('#exportDiv').dialog('open');
}

//导出Excel格式文件
function exportExcel(){
	var pnoticeSize = $("#pnoticeSize").val();
	var totalSize=$("#totalSize").val();
	if(totalSize>500000){
		//alert("超过50万以上数据，请您选择TXT格式！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_33"));
		return;
	}
	if(pnoticeSize>0){
		//if(confirm("确定要导出数据到excel?")){
		if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_34"))){
			$.ajax({
				type: "POST",
				url: "bla_blacklistSvt.htm?method=exportExcel",
				data: {busCode:cond_buscode,phone:cond_phone,lgcorpcode:$('#lgcorpcode').val()},
              beforeSend:function () {
                  $("#outfont").css("display", "");
                  $('#excelBut').attr('disabled', true);
                  $('#txtBut').attr('disabled', true);
              },
              complete:function () {
                  $("#outfont").css("display", "none");
                  $('#excelBut').attr('disabled', false);
                  $('#txtBut').attr('disabled', false);
              },
				success: function(result){
                      if(result=='true'){
							download_href("bla_blacklistSvt.htm?method=downloadFile");
                      }else{
                          //alert('导出失败！');
                      	alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_35"));
                      }
	   			}
			});
		}
	}else{
  	//alert("无数据可导出！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_36"));
	    }
 }




//导出文本文件
function exportTxt(){
	var pnoticeSize = $("#pnoticeSize").val();
	if(pnoticeSize>0){
		//if(confirm("确定要导出数据到txt?")){
		if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_37"))){
			$.ajax({
				type: "POST",
				url: "bla_blacklistSvt.htm?method=exportTxt",
				data: {busCode:cond_buscode,phone:cond_phone,lgcorpcode:$('#lgcorpcode').val()},
              beforeSend:function () {
                  $("#outfont").css("display", "");
                  $('#excelBut').attr('disabled', true);
                  $('#txtBut').attr('disabled', true);
              },
              complete:function () {
                  $("#outfont").css("display", "none");
                  $('#excelBut').attr('disabled', false);
                  $('#txtBut').attr('disabled', false);
              },
              success:function (result) {
                  if (result == 'true') {
						download_href("bla_blacklistSvt.htm?method=downloadFile");
                  } else {
                      //alert('导出失败！');
                  	alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_38")); 
                  }
              }
			});
			//window.location.href="bla_blacklistSvt.htm?method=exportTxt&busCode="+$('#busCode').val()+"&phone="+$('#phone').val()+"&lgcorpcode="+$('#lgcorpcode').val();
			//return;
	    }
	}else{
  	//alert("无数据可导出！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_39"));
	    }
 }

//保存黑名单的方法
function checkVal(type){
	
	if(type == "add")
	{
		var mobile=$('#mobile').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var lgusername = $('#lgusername').val();
		//var addbusCodeValue=$('#addbusCode').selectivity('value');
		var buscode=$('#addbusCode').val();
		if(buscode == "")
		{
			//alert('请选择业务名称！');
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_40"));
			return;
		}
		//var tempValue=addbusCodeValue.split("(")[1];
		//var buscode =tempValue.substring(0,tempValue.length-1);
		if(!checkPhone(mobile))
		{
			//alert('请输入有效的手机号码！');
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_41"));
			$('#mobile').focus();
		}else
		{
			$('#blok').attr('disabled', true);
			$('#blc').attr('disabled', true);
			$.post('bla_blacklistSvt.htm?method=checkMobile', {
				mobile:mobile,
				busCode: buscode,
				lgcorpcode:lgcorpcode,
				lgusername:lgusername
				},function(result)
			{
					if(result.indexOf("checkBl:")<0)
					{
	 					//alert("系统繁忙，请刷新页面或稍后再试！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_42"));
						$('#blok').attr('disabled', false);
						$('#blc').attr('disabled', false);
	 					return;
					}
					result = result.replace('checkBl:','');
				if(result=='errorPhone')
				{
					//alert('无效的手机号码！');
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_43"));
					$('#mobile').focus();
					$('#blok').attr('disabled', false);
					$('#blc').attr('disabled', false);
				}else
				if(result=='phoneRepeat')
				{
					//alert('手机号码已存在，请重新输入！');
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_44"));
					$('#blok').attr('disabled', false);
					$('#blc').attr('disabled', false);
					$('#mobile').focus();
				}else if(result=='error')
				{
					//alert('验证手机号码格式失败，请确认网络是否畅通！');
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_45"));
					$('#blok').attr('disabled', false);
					$('#blc').attr('disabled', false);
				}else
				{ 
					//备注信息
					var msg=$("#msgNote").val();
					if(msg!=""&&msg.length>120)
					{
						//alert('请输入有效的手机号码！');
						//alert("备注信息不能超过120个字符!");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_58"));
						$('#blok').attr('disabled', false);
						$('#blc').attr('disabled', false);
						$('#msgNote').focus();
					}else{
						$.post("bla_blacklistSvt.htm?method=update",{mobile:mobile,busCode:buscode,opType:"add",lgcorpcode:lgcorpcode,lgusername:lgusername,msgNote:msg},function(result1){
				              show(result1);
						});
					}
				}
			});
		}
	}
	else if(type == "upload")
	{
		if($('#uploadbusCode').val() == '')
		{
			//alert('请选择业务名称！');
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_46"));
			return;
		}
		var phone=$('#phone1').val();
		if(phone.length<1)
		{
			//alert("请选择上传的手机号码文件！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_47"));
		}else
		{
			var index = phone.lastIndexOf(".");
			upFile = phone.substring(index + 1).toLowerCase();
          if (upFile != "txt" && upFile != "xls" && upFile != "xlsx" && upFile != "et") {
					//alert("上传文件格式错误，请选择txt、xls、xlsx或et格式的文件。");	
          	alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_48"));
			}else
			{
				$("#impfont").css("display","");
				butg("#bloks","#blcs","");
				$('#blok').attr('disabled', true);
				$('#blc').attr('disabled', true);
				document.updateInfo.submit();
			}
		}
	}
}

function show(result){
	if(result.indexOf("true") == 0)
	{
		//alert("新建成功,有效黑名单号码个数为："+result.substr(4)+"！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_49")+result.substr(4)+"！");
		black();
	}
	else if( result =="noPhone")
	{
		//alert("没有有效的黑名单记录可以添加！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_50"));
	}else if( result =="overCount")
	{
		//alert("上传号码个数超过300000，请分批次重新上传！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_51"));
	}else if(result == "false"){
		//alert("新建失败！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_52"));
	}else if(result == "outCount")
	{
		//alert("企业黑名单总数超过"+maxCount+"个，不允许再添加！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_53")+maxCount+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_54"));
	}
	$('#blok').attr('disabled', false);
	$('#blc').attr('disabled', false);
}
//取消
function btcancel()
{
	if(!$('#addmmsBl').is(":hidden"))
	{
	    $('#addmmsBl').dialog('close');	
	}
	if(!$('#uploadmmsBl').is(":hidden"))
	{
	    $('#uploadmmsBl').dialog('close');	
	}
}

function black()
{
  	var url = 'bla_blacklistSvt.htm';
		var conditionUrl = "";
		if(url.indexOf("?")>-1)
		{
			conditionUrl="&";
		}else
		{
			conditionUrl="?";
		}
		//默认跳转到第一页，保存好当前的显示条数
		var pageIndex=$('#txtPage').val();
		var pageSize=$('#pageSize').val();
		conditionUrl=conditionUrl+"pageIndex=1&pageSize="+pageSize+"&";
		$("#blloginparams").find(" input").each(function(){
			conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
		});
		window.location=url+conditionUrl;
}

function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}
function opened(id,spisuncm,spgate){
	$('#spisuncm').val(spisuncm);
	$('#blSpgate').val(spgate);
	var hold=document.getElementById("la"+id).innerHTML;
	document.getElementById("modify").style.display="block";
	document.getElementById("mobile").value=hold;
	document.getElementById("blId").value=id;
	}
	function closed(){
	document.getElementById("modify").style.display="none";
}
	
$(".file").change(function(){
	$("#filename").remove();
	var fliePath = $('#phone1').val();
	var flieName = fliePath.substring(fliePath.lastIndexOf("\\")+1,fliePath.length);
	$(this).after("<span id='filename'>"+flieName+"</span>")
})
