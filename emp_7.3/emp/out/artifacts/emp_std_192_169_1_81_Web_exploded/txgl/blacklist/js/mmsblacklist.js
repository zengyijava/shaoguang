//全选
var empLangName = getJsLocaleMessage("common","common_empLangName");
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
	a[i].checked =e.checked;    
}

//单个删除
function del(id,phone,keyId){
	//if(confirm("你确定要删除此条记录吗？")==true)
	if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_11"))==true)
	{
		var type ="del";
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$('#lgusername').val();
		$.post("bla_mmsblacklistSvt.htm?method=delete",{ids:keyId,phone: phone,type:type,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
			if(result>0){
				//alert("删除成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_21"));
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
		//alert("请选择您要删除的彩信黑名单信息！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_55"));
	}else{
		//if(confirm("您确定要删除"+i+"条信息?")==true){
		if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_13")+i+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_14"))==true){
			        var lgcorpcode =$("#lgcorpcode").val();
			        var lgusername =$('#lgusername').val();
					$.post("bla_mmsblacklistSvt.htm?method=delete",{ids:ids,type:type,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
						if(result>0)
						{
							//alert("删除成功,共删除"+result+"条信息！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_15")+result+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_16"));
							black();
						}else{
							//alert("删除失败！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_17"));
						}
					});
					
		}
	}
}
//弹出可供导出的界面
function exportAll()
{
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

//数据导出Excel格式
function exportExcel(){
	var pnoticeSize = $("#pnoticeSize").val();
	var totalSize=$("#totalSize").val();
	if(totalSize>500000){
		//alert("超过50万以上数据，建议您选择TXT格式！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_56"));
		return;
	}
	if(pnoticeSize>0){
		//if(confirm("确定要导出数据到excel?")){
		if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_34"))){
			$("#outfont").css("display","");
			$('#excelBut').attr('disabled', true);
			$('#txtBut').attr('disabled', true);
			$.ajax({
				type: "POST",
				url: "bla_mmsblacklistSvt.htm?method=exportExcel",
				data: {phone:$('#phone').val(),lgcorpcode:$('#lgcorpcode').val()},
                complete:function () {
					$("#outfont").css("display","none");
					$('#excelBut').attr('disabled', false);
					$('#txtBut').attr('disabled', false);
                },
				success: function(result){
                    if(result=='true'){
						download_href("bla_mmsblacklistSvt.htm?method=downloadFile");
                    }else{
                        //alert('导出失败！');
                    	alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_38"));
                    }
	   			}
			});
	    	//location.href="bla_mmsblacklistSvt.htm?method=exportExcel&phone="+$('#phone').val()+"&lgcorpcode="+$('#lgcorpcode').val();
			//return;
		}
	}else{
    	//alert("无数据可导出！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_39"));
	    }
   }

//数据导出TXT格式
function exportTxt(){
	var pnoticeSize = $("#pnoticeSize").val();
	if(pnoticeSize>0){
		//if(confirm("确定要导出数据到txt?")){
		if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_37"))){
			$("#outfont").css("display","");
			$('#excelBut').attr('disabled', true);
			$('#txtBut').attr('disabled', true);
			$.ajax({
				type: "POST",
				url: "bla_mmsblacklistSvt.htm?method=exportTxt",
				data: {phone:$('#phone').val(),lgcorpcode:$('#lgcorpcode').val()},
				complete:function () {
					$("#outfont").css("display","none");
					$('#excelBut').attr('disabled', false);
					$('#txtBut').attr('disabled', false);
				},
				success: function(result){
                    if(result=='true'){
						download_href("bla_mmsblacklistSvt.htm?method=downloadTxtFile");
                    }else{
                        //alert('导出失败！');
                    	alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_38"));
                    }
	   			}
			});
			
			//location.href="bla_mmsblacklistSvt.htm?method=exportTxt&phone="+$('#phone').val()+"&lgcorpcode="+$('#lgcorpcode').val();
			//return;
		}
	}else{
    	//alert("无数据可导出！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_39"));
	    }
   }

//切换状态
function changeState(i){
        var statu=$.trim($("#blState"+i).attr("value"));
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername =$('#lgusername').val();
		
		$.post("bla_mmsblacklistSvt.htm?method=changeState",{id:i,statu:statu,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
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

//点击新增
function doadd()
{
	$("#addmmsBl").css("display","block");
	$('#addmmsBl').dialog({
		autoOpen: false,
		height: 200,
		width: 400,
		resizable:false,
		modal:true
	});
	$('#addmmsBl').dialog('open');	
}

//导入彩信黑名单
function doupload()
{
	$("#uploadmmsBl").css("display","block");
	$('#uploadmmsBl').dialog({
		autoOpen: false,
		height: 270,
		width: 450,
		resizable:false,
		modal:true
	});
	$('#uploadmmsBl').dialog('open');
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

//保存彩信黑名单前的判断
function checkPhone(type){
		
    if(type == "add")
	{
		var mobile=$('#mobile').val();
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername = $("#lgusername").val();
		if(mobile.length<11)
		{
			//alert('请输入11位的手机号码！');
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_57"));
			$('#mobile').focus();
		}else
		{
			$.post('bla_mmsblacklistSvt.htm?method=checkMobile', {
				mobile:mobile,
				lgcorpcode:lgcorpcode,
				lgusername:lgusername
				},function(result)
			{
					if(result.indexOf("checkBl:")<0)
					{
	 					//alert("系统繁忙，请刷新页面或稍后再试！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_42"));
	 					return;
					}	
					result = result.replace('checkBl:','');
				if(result=='errorPhone')
				{
					//alert('无效的手机号码！');
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_43"));
					$('#mobile').focus();
				}else if(result=='phoneRepeat')
				{
					//alert('手机号码已存在，请重新输入！');
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_44"));
					$('#mobile').focus();
				}else if(result=='error')
				{
					//alert('验证手机号码格式失败，请确认网络是否畅通！');
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_45"));
				}else
				{
					$.post("bla_mmsblacklistSvt.htm?method=update",{mobile:mobile,opType:"add",lgcorpcode:lgcorpcode,lgusername:lgusername},function(result1){
		              show(result1);						
					});
				}
			});
		}
	}
	else if(type == "upload")
	{
		var phone1 = $("#phone1").attr("value");
		if(phone1.length<1)
		{
			//alert("请选择上传的手机号码文件！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_47"));
		}else
		{
			var index = phone1.lastIndexOf(".");
			upFile = phone1.substring(index + 1).toLowerCase();
            if (upFile != "txt" && upFile != "xls" && upFile != "xlsx" && upFile != "et") {
				//alert("上传文件格式错误，请选择txt、xls、xlsx或et格式的文件。");
            	alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_48"));
            }else
			{
            	$("#impfont").css("display","");
				butg("#bloks","#blcs","");
				//$("#uploadForm").submit();
				document.updateInfo.submit();
			}
		}
	}
}

//显示结果
function show(result){
	if(result.indexOf("true") == 0)
	{
		//alert("新建成功,有效彩信黑名单号码个数为："+result.substr(4)+"！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_8")+result.substr(4)+"！");
		black();
	}
	else if( result =="noPhone")
	{
		//alert("没有有效的彩信黑名单记录可以添加！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_9"));
	}else if( result =="overCount")
	{
		//alert("上传号码个数超过300000，请分批次重新上传！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_51"));
	}else if(result == "false"){
		//alert("新建失败！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_52"));
	}
	butk("#blok","#blc","");
}


function black()
{
    	var url = 'bla_mmsblacklistSvt.htm';
		var conditionUrl = "";
		if(url.indexOf("?")>-1)
		{
			conditionUrl="&";
		}else
		{
			conditionUrl="?";
		}
		$("#blloginparams").find(" input").each(function(){
			conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
		});
		window.location=url+conditionUrl;
}

$(".file").change(function(){
	$("#filename").remove();
	var fliePath = $('#phone1').val();
	var flieName = fliePath.substring(fliePath.lastIndexOf("\\")+1,fliePath.length);
	$(this).after("<span id='filename'>"+flieName+"</span>")
})