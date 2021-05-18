function add()
{
	$('#bulkImport_box').dialog('open');
	$('#importArea').blur();
	$('#importArea').val("");
	$('#inputphone').val("");
	//$('#importArea').val($('#importAreaTemp').val());
	//var num=$('#importAreaTemp').attr('data-num');
	//num=typeof num=='undefined' ? 0 : num;
	$('#bNum').html("0");
	$("#bultMark_top").show();
}

//确定
function previewTel(){
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var lgusername = $("#lgusername").val();
	if($('#importArea').val()==''){
		//alert('请输入号码');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_100"));
		return false;
	}
	var phone=$('#bNum').html();
	//批量输入最大支持1000个号码
	if(1000 - phone < 0)
	{
		//alert('批量输入号码个数超出范围，最大只支持1000个号码');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_101"));
		return false;
	}
	var inputphone=$('#importArea').val();
	$('#importAreaTemp').html(inputphone).attr('data-num',phone);
	var reg=/[\s,、\n，]+/g;
	var result=inputphone.replace(reg,",");
	if(result.substr(0,1)==",")
	{
		result = result.substr(1);
	}
	$("#inputphone").val(result);
	$(".bultBtn").find("input[type='button']").attr("disabled","disabled");
	$.post("wy_spePhoneManage.htm",
			{method:"add",lgcorpcode:lgcorpcode,lguserid: lguserid,result:result,lgusername:lgusername,time:new Date(),isAsync:"yes"},
			function(result){
				$(".bultBtn").find("input[type='button']").attr("disabled","");
				if(result == "outOfLogin")
				{
					$("#logoutalert").val(1);
					location.href=path+"/common/logoutEmp.html";
					return;
				}
				else if(result!="error")
				{
					var resArr = result.split(",");
					if(resArr[1]=="-1")
					{
						//alert("上传文件内无号码！");
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_102"));
					}
					else
					{
						$.post("common.htm?method=checkFile", {url : resArr[1],upload:0 },function(returnmsg) {
							$("#button3").attr("disabled","");
							if(returnmsg=="true") {
								if(resArr[0]=="0")
								{
									//alert("无有效的号码！");
									alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_103"));
									$('#bulkImport_box').dialog('close');
									location.href=path+"/wy_spePhoneManage.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
									//window.showModalDialog(path + "/" + resArr[1] +"?Rnd="+ Math.random());
									window.showModalDialog(srcpath + resArr[1] +"?Rnd="+ Math.random());
									return;
								}
								else
								{
									//alert("成功添加"+resArr[0]+"个号码！");
									alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_104")+resArr[0]+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_105"));
									$('#bulkImport_box').dialog('close');
									location.href=path+"/wy_spePhoneManage.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
									//window.showModalDialog(path + "/" + resArr[1] +"?Rnd="+ Math.random());
									window.showModalDialog(srcpath + resArr[1] +"?Rnd="+ Math.random());
									return;
								}
								
							}else if (returnmsg == "false"){
								//alert("文件不存在");
								alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_106"));
							}else{
								//alert("出现异常,无法跳转");
								alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_107"));
							}
							location.href=path+"/wy_spePhoneManage.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
						});
					}
				}
				else
				{
					//alert("添加失败！");
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_87"));
				}
			});
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

function bultCancel()
{
	$('#bulkImport_box').dialog('close');
	$('#importArea').html("");
}

function openUpload()
{
	$('#uploadPhone').dialog('open');
}

function back()
{
	$('#uploadPhone').dialog('close');
}

//上传确定
function upload()
{
	var fileName = $("#uplfile").val();
	if(fileName=="")
	{
		//alert("请选择上传文件");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_108"));
		return false;
	}
	var fileType = fileName.substr(fileName.lastIndexOf(".")+1);
	if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et") {
		//alert("上传文件格式错误，请选择txt、zip、xls、xlsx或et格式的文件。");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_109"));
		//$("#uplfile").after($("#uplfile").clone().val(""));
		//fileObj.remove();
		return false;
	}
	$(".uplBtn").find("input[type='button']").attr("disabled","disabled");
	$("#updateInfo").submit();
}

//上传后返回结果处理
function upd(result)
{
	$(".uplBtn").find("input[type='button']").attr("disabled","");
	if(result=="error")
	{
		//alert("上传号码文件失败！")
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_110"))
	}
	else if(result=="countOut")
	{
		//alert("文件内号码个数超过5万个！")
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_111"))
	}
	else if(result=="isOverSize")
	{
		//alert("上传文件大小不能超过2M！")
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_112"))
	}
	else
	{
		var resArr = result.split(",");
		if(resArr[1]=="-1")
		{
			//alert("上传文件内无号码！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_102"))
		}
		else
		{
			$.post("common.htm?method=checkFile", {url : resArr[1],upload:0 },function(returnmsg) {
				if(returnmsg=="true") {
					if(resArr[0]=="0")
					{
						//alert("文件内无有效号码！");
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_113"))
						$('#uploadPhone').dialog('close');
						//window.showModalDialog(path + "/" + resArr[1] +"?Rnd="+ Math.random());
						window.showModalDialog(srcpath + resArr[1] +"?Rnd="+ Math.random());
						location.href=path+"/wy_spePhoneManage.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
					}
					else
					{
						//alert("成功添加"+resArr[0]+"个号码！");
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_104")+resArr[0]+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_105"));
						location.href=path+"/wy_spePhoneManage.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
						$('#uploadPhone').dialog('close');
						//window.showModalDialog(path + "/" + resArr[1] +"?Rnd="+ Math.random());
						window.showModalDialog(srcpath + resArr[1] +"?Rnd="+ Math.random());
					}
				}else if (returnmsg == "false"){
					//alert("文件不存在");
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_106"));
				}else{
					//alert("出现异常,无法跳转");
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_107"));
				}
			});
		}
	}
}

//修改
function toEdit(id)
{
	var ediStr=$.trim($("#edit"+id).html());
	
	//if(ediStr=="保存")
	var bc = getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_67");
	if(ediStr==bc)
	{
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		var lgusername = $("#lgusername").val();
		var phone=$("#phone"+id).val();
		var pat=/^\d*$/;
		if(phone=="")
		{
			//alert("请输入手机号码！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_114"));
			return false;
		}
		if(!pat.test(phone))
		{
			//alert("手机号码输入不合法！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_115"));
			return false;
		}
		//手机号码没变
		if(phone==$.trim($("#span"+id).html()))
		{
			//alert("修改成功！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_68"));
			//$("#edit"+id).html("修改");
			$("#edit"+id).html(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_73"));
			$("#span"+id).html(phone);
			$("#phone"+id).hide();
			$("#span"+id).show();
			return;
		}
		$.post("wy_spePhoneManage.htm",
				{method:"eidt",lgcorpcode:lgcorpcode,lguserid: lguserid,phone:phone,id:id,lgusername:lgusername,time:new Date(),isAsync:"yes"},
				function(result){
					if(result == "outOfLogin")
					{
						$("#logoutalert").val(1);
						location.href=path+"/common/logoutEmp.html";
						return;
					}
					else if(result.indexOf("success")!=-1)
					{
						//alert("修改成功！");
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_68"));
						/*$("#edit"+id).html("修改");
						$("#span"+id).html(phone);
						$("#phone"+id).hide();
						$("#span"+id).show();
						var res=result.split(",");
						var unicom=res[1];
						var str="移动"
						if(unicom=="1")
						{
							str="联通";
						}
						else if(unicom=="21")
						{
							str="电信";
						}
						$("#unicom"+id).html(str)*/
						location.href=path+"/wy_spePhoneManage.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
					}
					else if(result=="exist")
					{
						//alert("手机号码已存在！");
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_116"));
					}
					else if(result=="haoduan_error")
					{
						//alert("手机号码输入不合法！");
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_115"));
					}
					else
					{
						//alert("修改失败！");
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_71"));
					}
				});
	}
	else
	{
		edit_js(id)
	}
}

function edit_js(id)
{
	$("#hid_id").val(id);
	$("#hid_state").val("2");
	$("#phone"+id).val($.trim($("#span"+id).html()));
	$("#phone"+id).show();
	$("#span"+id).hide();
	//$("#edit"+id).html("保存");
	$("#edit"+id).html(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_67"));
}

//修改
function clearEdit(id)
{
	if(id=="")
	{
		return;
	}
	$("#phone"+id).hide();
	$("#span"+id).show();
	//$("#edit"+id).html("修改");
	$("#edit"+id).html(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_73"));
}

//批量删除
function delAll()
{
	var id="";
	$("input:checkbox[name='checklist']").each(function(){
		if($(this).attr("checked")==true)
		{
			id=id+$(this).val()+",";
		}
	});
	if(id=="")
	{
		//alert("请选择要删除的记录！");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_117"));
		return false;
	}
	del(id);
}

//删除
function del(id)
{
	//if(!confirm("确定删除吗？")){
	if(!confirm(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_15"))){
		return;
	}
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var lgusername = $("#lgusername").val();
	$.post("wy_spePhoneManage.htm",
			{method:"del",lgcorpcode:lgcorpcode,lguserid: lguserid,id:id,lgusername:lgusername,time:new Date(),isAsync:"yes"},
			function(result){
				if(result == "outOfLogin")
				{
					$("#logoutalert").val(1);
					location.href=path+"/common/logoutEmp.html";
					return;
				}
				else if(result!="error")
				{
					//alert("成功删除"+result+"个号码！");
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_118")+result+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_105"));
					submitForm();
					bultCancel();
				}
				else
				{
					//alert("删除失败！");
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_81"));
				}
			});
}

function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)
	{
		a[i].checked =e.checked;
	}
}

function numberControl(va)
{
	var pat=/^\d*$/;
	if(!pat.test(va.val()))
	{
		va.val(va.val().replace(/[^\d]/g,''));
	}
}
