function isNumber(s) {
    var regu = "^[0-9]+$";
    var re = new RegExp(regu);
    return s.search(re) != -1;
}
	//验证业务指令代码	
	function isNumberOrLetter(s) {//判断是否是数字或字母 
		var regu = "^[a-zA-Z]+[a-zA-Z0-9]+[#]{1,6}$";
	    var re = new RegExp(regu);
		var index = s.indexOf("#");
		var split = "";
		var code = "";
		if(index!= -1){
			split = s.substring(s.length-1,s.length);
			code = s.substring(0,s.length-1);
			if(split == "#"){
				if(code != ""){
					if (re.test(s)) {
				        return true;
				    } else {
				        return false;
				    }
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	    
	}
	
	//验证端口格式是否正解
   function isPort(str) {
    return (isNumber(str) && str < 65536);
   }
$(document).ready(function() {
	var pathUrl = $("#pathUrl").val();
	var index=1;
	var url=pathUrl+"/pag_orderBind.htm";
	var opType = $("#opType").val();
	//$('#dsflag').trigger("change");
	$('#subBut').click(function() {
		   var name = $("#name").val();
		   var structcode = $("#Structcode").val();	
		   var bussysname = $("#Bussysname").val();
		   var spid = $("#Spid").val();
		   //var tructtype = $("#tructtype").val();
		   var tructid = $("#tructid").val();
		   var status = $("#status").val();
		   var keyId = $("#keyId").val();
		   if(name == ""){
		     // alert("指令名称不能为空！");
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_3"));
		      $("#name").focus();
		   }else if(structcode == ""){
		     // alert("指令代码不能为空！");	
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_16"));
		      $("#Structcode").focus();
		   }else if(structcode.length < 2 || structcode.length >7){
			   //alert("指令代码长度必须为2到6位！");
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_4"));
			   $("#Structcode").focus();
		   }else if(!isNumberOrLetter(structcode)){
		      //alert("指令代码格式不正确！");
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_5"));
		      $("#Structcode").focus();
//		   }else if(tructtype == ""){
//		      alert("指令类型不能为空！");
//		      $("#tructtype").focus();
//		      return;
		   }else if(bussysname == ""){
		      //alert("业务系统名称不能为空！");
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_6"));
		      $("#Bussysname").focus();
		   }else if(spid == ""){
		     // alert("sp账号不能为空！");
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_7"));
		      $("#Spid").focus();
		   }else{
			   	  if(opType == "update")
			   	  {
			   		  var type = checkTruct(structcode, pathUrl);
			   		  if(type == 0)
		   			  {
		   			   //alert("指令已存在！");
			   			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_8"));	
		   			   	$("#Structcode").focus();
						return;
		   			  }
			   		  else if(type == -1)
		   			  {
		   			  	//alert("查询指令是否存在异常！");
			   			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_9"));
		   			   	$("#Structcode").focus();
						return;
		   			  }
			   	  }
				  var lgcorpcode = $("#lgcorpcode").val();
		    	  var lguserid = $("#lguserid").val();
		    	  var lgusername=$("#lgusername").val();
		    	  $.post(url,{
		    		  method:opType,
		    		  tructid:tructid,
		    		  keyId:keyId,
				      lgcorpcode:lgcorpcode,
				      lguserid:lguserid,
				      lgusername:lgusername,
				      name:name,
				      structcode:structcode,
				      bussysname:bussysname,
				      spid:spid,
				      status:status
//				      ,tructtype:tructtype
				     }, function(result) {
				
						if (result=="true") 
						{
							if(opType == "add"){
								//alert("添加成功！");
								alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_10"));
							}else if(opType == "update"){
								//alert("修改成功！");
								alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_11"));
							}
							window.parent.location.href=pathUrl+"/pag_orderBind.htm?lgcorpcode="+lgcorpcode+"&lguserid="+lguserid;

						}
						else if(result=="false"){
							
							if(opType == "add"){
								//alert("添加失败！");
								alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_12"));
							}else if(opType == "update"){
								//alert("修改失败！");
								alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_13"));
							}

						} 
						else if(result=="isExits"){
							//alert("添加失败，指令已存在！");
							alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_14"));

						}
						else if(result=="error"){
							//alert("添加失败，查询指令是否存在异常！");
							alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_15"));
						}
			});
		}
	});
});

	function checkTruct(structcode, pathUrl)
	{
		var oldcode = $("#oldcode").val();
		var type = true;
		if(oldcode != structcode)
		{
			$.ajax({
					async : false,
					url: pathUrl+"/pag_orderBind.htm?method=checkTructIsExits", 
					type : "POST",
					data: {structcode:structcode},
					success: function(result)
					{
		   			  	if (result=="0") 
						{
	   			  		  type = 0;
						}
						else if(result=="-1")
						{
							type = -1;
						}
					}
				});
			}
		return type;
	}	


//打开新增上行业务指令绑定页面
function showAddServiceBind()
{
    $("#addMoServiceBindFrame").attr("src","pag_orderBind.htm?method=doAdd&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
          
	$("#addMoServiceBindDiv").dialog("open");
}

function closeAddServiceBinddiv()
{
	$("#addMoServiceBindDiv").dialog("close");
	$("#addMoServiceBindFrame").attr("src","");
	
}


function closeEditServiceBinddiv()
{
	$("#editMoServiceBindDiv").dialog("close");
	$("#editMoServiceFrame").attr("src","");
	
}

function checkAlls(e,str)    
	{  
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0; i<n; i=i+1)    
			a[i].checked =e.checked;    
	}



function switchState(obj) {
	if(obj.value == 1) {
		$("#bizCode").attr("disabled", false);
	} else {
		$("#bizCode").attr("disabled", true);
	}
}
function isNumber(s) {
	var regu = "^[0-9]+$";
	var re = new RegExp(regu);
	return s.search(re) != -1;
}
