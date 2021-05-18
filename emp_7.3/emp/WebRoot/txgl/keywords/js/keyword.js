var empLangName = getJsLocaleMessage("common","common_empLangName");
//检查输入关键字是否符合条件
function doClick(){
	var keywords = $.trim($("#keywored").val()) ;
	//var reg=/[&\'\",，]/;
	var reg=/[#=$%@&*<>\/\\^\?'_",，]/gi;
	if(keywords.length == 0){
			//alert("关键字不能为空") ;
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_8")) ;
			$("#keywored").focus();
			return;
	}else if(keywords.length>10)
	{
		//alert("关键字长度不能大于10") ;
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_9")) ;
		$("#keywored").select();
		return;
	}else if(reg.test(keywords))
	{
		//alert("关键字不能包含括号中(#=$%@*<>\\/^?'_&,\")这些字符！") ;
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_10")+"(#=$%@*<>\\/^?'_&,\")"+getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_11")) ;
		$("#keywored").select();
		return;
	}else{
		CheckExist(keywords);
	}
}

//检查上传的文件是否符合条件
function checkUpload(){
	var uploadFile = $("#numfile").attr("value") ;
	$("#kwsok").attr("disabled",true);
	if(uploadFile.length == 0){//非空检查
		//alert("没有选择导入文件，请选择包含过滤关键字的.txt文件") ;
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_12")) ;
		$("#kwsok").attr("disabled",false);
		return false ;
	}else if(uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="txt"){//检查文件格式是否是.txt格式
		//alert("不支持此种文件类型，请选择包含过滤关键字的.txt文件") ;
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_13")) ;
		$("#kwsok").attr("disabled",false);
		return false ;
	}else{
		$("#uploadForm").submit();
	}
}

function CheckExist(keyword)
{   
	butg("#kwok","#kwc","");
	var lgcorpcode =$("#lgcorpcode").val();
	var keywored =$("#keywored").val();
	var kwState =$("#kwState").val();
	var lguserid =$("#lguserid").val();
	$.post("key_keywordSvt.htm?method=checkExist",{keyword:keyword,lgcorpcode:lgcorpcode},function(result){
		if (result == "true") {
			//alert("关键字已存在！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_14"));
			butk("#kwok","#kwc","");
		}else{
			$.post("key_keywordSvt.htm?method=update",{keywoed:keywored,opType:"add",lgcorpcode:lgcorpcode,kwState:kwState,lguserid:lguserid},function(result1){
				if(result1 == "true")
				{
					//alert("共添加1条记录！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_15"));
					butk("#kwok","#kwc","");
					$('#addKey').dialog('close');
					black();
				}
				else if(result1 == "cap")
				{
					var corpType = $("#corpType").val();
					//alert((corpType==1?"本企业":"")+"关键字记录已满5000条，无法继续添加！");
					alert((corpType==1?getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_16"):"")+getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_17"));
					butk("#kwok","#kwc","");
				}
				else
				{
					//alert("操作失败！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_18"));
					butk("#kwok","#kwc","");
				}
				
			});
		}		
	});
}


//根据id删除关键字
function doCmd(kwId, keyId)
{
	var keyName = $("#keyName").val();
	//if(confirm("确定删除该关键字？")){
	if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_19"))){	
		var lguserid =$("#lguserid").val();
		var lgcorpcode =$("#lgcorpcode").val();
	    $.ajax({
	        url: 'key_keywordSvt.htm?method=delete',
	        data:{
	        	kwIds:keyId,
	        	keyName:keyName,
	        	opType:"s",
	        	lguserid:lguserid,
	        	lgcorpcode:lgcorpcode
	        },
	        type: 'post',
	        error: function(){
	           //alert("删除失败！");	
	        	alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_20"));	
	        },
	        success: function(result){
				if (result == 1) {
					//alert("删除成功！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_21"));	
					//window.location=location;
					black();
				}else{
					//alert("删除失败！");	
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_20"));	
				}		   
	        }
	    });
	}
}

//删除全部关键字
function delAllKw()
{ 
	//if(confirm("确定删除全部关键字？")){
	if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_22"))){	
		var lguserid =$("#lguserid").val();
		var lgcorpcode =$("#lgcorpcode").val();
	    $.ajax({
	        url: 'key_keywordSvt.htm?method=delete',
	        data:{
	        	opType:"deleteAll",
	        	lguserid:lguserid,
	        	lgcorpcode:lgcorpcode
	        },
	        type: 'post',
	       // timeout: 3000,
	        error: function(){
	          // alert("删除全部关键字失败！");	
	        	 alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_23"));	 
	        },
	        success: function(result){
				if (result == "true") {
					//alert("删除全部关键字成功！");
					 alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_24"));	 
					//window.location.href=location;
					black();
				}else if(result == "NoExists"){
					//alert("当前关键字为空,无需删除！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_25"));	
				}else{
					//alert("删除全部关键字失败！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_26"));	
				}		   
	        }
	    });
	}
}
function doadd()
{
	$('#keywored').val('');
	$("#addKey").css("display","block");
	$('#addKey').dialog({
		autoOpen: false,
		height: 200,
		width: "zh_HK" === empLangName? 450:350,
		resizable:false,
		modal:true
	});
	$('#addKey').dialog('open');	
}
function doupload()
{
	$("#uploadKey").css("display","block");
	$('#uploadKey').dialog({
		autoOpen: false,
		height: 250,
		width: "zh_HK" === empLangName? 450:350,
		resizable:false,
		modal:true
	});
	$('#uploadKey').dialog('open');
}

//i:关键字id，v：关键字名称s，加密ID
function doEdit(i,v,keyId)
{
	$("#editKey").css("display","block");
	$('#editKey').dialog({
		autoOpen: false,
		height: 210,
		width: "zh_HK" === empLangName? 450:350,
		resizable:false,
		modal:true
	});
	$("#bkw").attr("value",v);
	$("#fkw").attr("value",v);
	$("#fkwid").attr("value",i);
	$("#keyId").attr("value",keyId);
	$('#editKey').dialog('open');
}
//确定
function btok()
{
	checkediKw();
}
//取消
function btcancel()
{
	if(!$('#editKey').is(":hidden"))
	{
	    $('#editKey').dialog('close');	
	}
	if(!$('#addKey').is(":hidden"))
	{
	    $('#addKey').dialog('close');	
	}
	if(!$('#uploadKey').is(":hidden"))
	{
	    $('#uploadKey').dialog('close');	
	}
}

//修改关键字条件判断
function checkediKw(){
	var keywoed=$("#fkw").attr("value");
	var kwid=$.trim($("#fkwid").attr("value"));
	var keyId=$.trim($("#keyId").attr("value"));
	var kwName=$.trim($("#bkw").attr("value"));
	var lgcorpcode =$("#lgcorpcode").val();
	var lguserid =$("#lguserid").val();
	var reg=/[#=$%@&*<>\/\\^\?'_",，]/gi;
	if(kwName.length == 0){
		//alert("关键字不能为空") ;
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_27")) ;
		$("#bkw").select();
		return false;
	}else if(kwName.length>10)
	{
		//alert("关键字长度不能大于10") ;
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_28")) ;
		$("#bkw").select();
		return false ;
	}else if(reg.test(kwName))
	{
		//alert("关键字不能包含括号中(#=$%@*<>\\/^?'_&,\")这些字符！") ;
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_29")+"(#=$%@*<>\\/^?'_&,\")"+getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_30")) ;
		$("#bkw").select();
		return;
	}else if(keywoed == kwName)
	{
		//alert("未作任何修改！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_31")) ;
	}
	else
	{
		$.post("key_keywordSvt.htm?method=update",{keywoed:kwName,curKey:keywoed,kwId:kwid,opType:"edit",lgcorpcode:lgcorpcode,lguserid:lguserid,keyId:keyId},function(result){
			
			if (result == "wordExists") {
				//alert("关键字已存在！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_32")) ;
			}else if (result == "true2") {
				//alert("修改成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_33")) ;
				$('#editKey').dialog('close');
				black();
			}else{
				//alert("修改失败！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_34")) ;
			}		
		});
	}
}
//修改关键字状态
function changestate(i, keyId)
{
	var ks=$.trim($("#bkwState"+i).attr("value"));
	var lgcorpcode =$("#lgcorpcode").val();
	var lguserid =$("#lguserid").val();
	$.post("key_keywordSvt.htm?method=changeState",{kwId:i,kwState:ks,lgcorpcode:lgcorpcode,lguserid:lguserid,keyId:keyId},function(result){
		
        if (result == "true") {
			//alert("修改成功！");
        	alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_33")) ;
			$("#pageForm").submit();
		}else{
			//alert("修改失败！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_34")) ;
		}		
	});
}
//checkbox全选
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}

function delKw()
{
	var selected=document.getElementsByName("delKwId");
	var n=0;		//统计勾选中的个数
	var id="";
	for(i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			id=id+selected[i].value;
			id=id+","
			n=n+1;
		}
	}
	id=id.substring(0,id.lastIndexOf(','));
	//if(n<1){alert("请选择一个或多个关键字进行操作");return;}
	if(n<1){alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_35"));return;}
	//if(confirm("确认删除该关键字?")){
	if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_36"))){	
		var lguserid =$("#lguserid").val();
		var lgcorpcode =$("#lgcorpcode").val();
	    $.ajax({
	        url: 'key_keywordSvt.htm?method=delete',
	        data:{
	    		kwIds:id,
	    		opType:"s",
	        	lguserid:lguserid,
	        	lgcorpcode:lgcorpcode
	        },
	        type: 'post',
	        error: function(){
	           //alert("删除选中关键字失败！");	
	        	alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_37"));
	        },
	        success: function(result){
				if (result>0) {
					//alert("删除选中关键字成功,共删除"+result+"条记录！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_38")+result+getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_39"));
					//window.location=location;
					black();
				}
				else if(result == -1)
				{
					//alert("删除选中的关键字包含系统级的记录，不允许删除！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_40"));
				}
				else{
					//alert("删除选中关键字失败！");	
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_41"));
				}		   
	        }
	    });
	}
}

function black()
{
	submitForm();
    	/*var url = 'key_keywordSvt.htm';
		var conditionUrl = "";
		if(url.indexOf("?")>-1)
		{
			conditionUrl="&";
		}else
		{
			conditionUrl="?";
		}
		$("#kwparams").find(" input").each(function(){
			conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
		});
		window.location=url+conditionUrl;*/
}
$('#keywored,#bkw').blur(function(){
	$(this).val($.trim($(this).val()));
})

$(".file").change(function(){
	$("#filename").remove();
	var fliePath = $('#numfile').val();
	var flieName = fliePath.substring(fliePath.lastIndexOf("\\")+1,fliePath.length);
	$(this).after("<span id='filename'>"+flieName+"</span>")
})
