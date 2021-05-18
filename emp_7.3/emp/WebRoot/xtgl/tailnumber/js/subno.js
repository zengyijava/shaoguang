 

function del(suId,allotType)
{
	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_1")))
	{
		var lgcorpcode = $('#lgcorpcode').val();
		var lgusername = $('#lgusername').val();
		$.post("tai_subno.htm",{suId : suId,allotType:allotType,method:"delete",lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){
			if(result=="true")
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
				location.href=location;
				//document.getElementById("pageForm").subit();
			}else 
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
			}
		});
	}
}

var SUID = 0;
var SUBNO = "";
function update(suId,bts,codes,subnoOld,accId,extendSubnoBegin,extendSubnoEnd,allotType,spName)
{
	$("#eallotType").empty();
	$("#eallotType").append("<option value=\"0\">"+getJsLocaleMessage("xtgl","xtgl_cswh_whgl_2")+"</option>"
  			+"<option value=\"1\">"+getJsLocaleMessage("xtgl","xtgl_cswh_whgl_3")+"</option>");
	$("#tixin1").empty();
	$("#tixin1").append(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_4"));
	$($("#etClass").attr("value")).hide();
	$("#etClass").attr("value",".etClass"+bts);
	$("#esuId").attr("value",suId);
	$(".etClass"+bts).show();
	$("#ebTypes").val(bts);
	$("#emenuCode").val(codes);
	$("#spName").val(spName);
	switch(bts)
	{
	case "0" :
		    codes = $("#emenuCode").val(codes);
	        break;
	case "1" :
		codes = $("#ebusCode").val(codes);
        break;
	case "2" :
		codes = $("#eproId").val(codes);
        break;
	case "3" :
		codes = $("#edepCode").val(codes);
        break;
	case "4" :
		codes = $("#esysCode").val(codes);
        break;
	}
	$("#eallotType").val(allotType);
	
	if("完美通知发送" == spName || "企业快快单发"==spName || "手工群发"==spName || "企业快快群发"==spName ||
			getJsLocaleMessage("xtgl","xtgl_cswh_whgl_5")==spname || 
			getJsLocaleMessage("xtgl","xtgl_cswh_whgl_6")==spname ||
			getJsLocaleMessage("xtgl","xtgl_cswh_whgl_7")==spname ||
			getJsLocaleMessage("xtgl","xtgl_cswh_whgl_8")==spname){
		$("#eallotType").empty();
		$("#eallotType").append("<option value=\"1\">"+getJsLocaleMessage("xtgl","xtgl_cswh_whgl_3")+"</option>");
		$("#tixin1").empty();
		//$("#tixin1").append("注：模块绑定时，已绑定账户的模块不能进行重复绑定，选择完美通知发送和上行业务管理时，发送账号下拉框只显示绑了上/下行路由的账号");
		$("#tixin1").append(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_4"));
	}else if("审批提醒" == spName ||"上行业务管理"==spName ||
			getJsLocaleMessage("xtgl","xtgl_cswh_whgl_9")==spname ||
			getJsLocaleMessage("xtgl","xtgl_cswh_whgl_10")==spname)
	{
		$("#eallotType").empty();
		$("#eallotType").append("<option value=\"0\">"+getJsLocaleMessage("xtgl","xtgl_cswh_whgl_2")+"</option>");
	}else if("上行业务管理"==spName || getJsLocaleMessage("xtgl","xtgl_cswh_whgl_10")==spname){
		$("#tixin1").empty();
		//$("#tixin1").append("注：模块绑定时，已绑定账户的模块不能进行重复绑定，选择完美通知发送和上行业务管理时，发送账号下拉框只显示绑了上/下行路由的账号");
		$("#tixin1").append(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_4")==spname);
	}

	
	
	if(allotType==0)
	{
		$("#eextendSubnoBegin").attr("readonly","readonly");
		$("#eextendSubnoEnd").attr("readonly","readonly");
		$("#trBegsubno2").css("display","none");
		$("#trSubno2").css("display","block");
	}else
	{
		$("#eextendSubnoBegin").attr("readonly","");
		$("#eextendSubnoEnd").attr("readonly","");
		$("#trBegsubno2").css("display","block");
		$("#trSubno2").css("display","none");
	}
	$("#eextendSubnoBegin").val(extendSubnoBegin=="null"?"":extendSubnoBegin);
	$("#eextendSubnoEnd").val(extendSubnoEnd=="null"?"":extendSubnoEnd);
	$("#esubnoAdd").val(subnoOld=="null"?"":subnoOld);
	var sure = getJsLocaleMessage("xtgl","xtgl_cswh_whgl_11");
	var cancle = getJsLocaleMessage("xtgl","xtgl_cswh_whgl_12");
	$('#editDiv').dialog({
		autoOpen: false,
		height: 270,
		width: 300,
		modal:true,
		buttons:{
			sure:function(){
					$(".ui-dialog-buttonpane button").attr("disabled",true);
		        	 editSubno();
			},
			cancle:function(){
				$('#editDiv').dialog('close');
			}
		}
	});
	$('#editDiv').dialog('open');
}

function editSubno()
{
	var pathUrl = $("#pathUrl").val();
	var suId = $("#esuId").attr("value");
	var bTypes = $("#ebTypes").val();
	var codes = "";
	var subno = $('#esubnoAdd').val();

	var userId = $.trim($("#euserId").val());

	var allotType = $("#eallotType").val();
	var extendSubnoBegin = $("#eextendSubnoBegin").val();
	var extendSubnoEnd = $("#eextendSubnoEnd").val();
	
	var begintemp ;
	var endtemp;
	var count = 0;
 	var b1 ;
 	var e1;
 	var b2 = extendSubnoBegin.toString().substring(0,extendSubnoBegin.length);
 	switch(bTypes)
	{
	case "0" :
		    codes = $("#emenuCode").val();
	        break;
	case "1" :
		codes = $("#ebusCode").val();
        break;
	case "2" :
		codes = $("#eproId").val();
        break;
	case "3" :
		codes = $("#edepCode").val();
        break;
	case "4" :
		codes = $("#esysCode").val();
        break;
	}
 	if (codes == "")
 	{
 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_13"));
 		$(".ui-dialog-buttonpane button").attr("disabled",false);
 		return;
 	}
 	if( userId == "")
 	{
 		//alert("编码类型为机构编码时，请选择账户ID！");
 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_14"));
 		$(".ui-dialog-buttonpane button").attr("disabled",false);
 		return;
 	}
 	if(allotType==1)
 	{
 		
 		
	 	if(extendSubnoBegin == "" || extendSubnoBegin.length>6)
	 	{
	 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_15"));
	 		$("#eextendSubnoBegin").select();
	 		$(".ui-dialog-buttonpane button").attr("disabled",false);
	 		return;
	 	}else{
	 		subno = extendSubnoBegin;
	 		$('#esubnoAdd').attr("value",extendSubnoBegin);
	 	}
	 	
	 		if( extendSubnoEnd == "" || extendSubnoEnd.length>6)
	 	{
	 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_16"));
	 		$("#eextendSubnoEnd").select();
	 		$(".ui-dialog-buttonpane button").attr("disabled",false);
	 		return ;
	 	}else if(subno == "" || subno.length>6){
	 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_17"));
	 		$('#subnoAdd').select();
	 		$(".ui-dialog-buttonpane button").attr("disabled",false);
	 		return;
	 	}
	 	
	
		
		var isContinue=true;
		if(extendSubnoBegin.length >extendSubnoEnd.length)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_18"));
	 		$("#eextendSubnoBegin").select();
	 		$(".ui-dialog-buttonpane button").attr("disabled",false);
			return;
		}
		if(subno.length > extendSubnoEnd.length || subno.length < extendSubnoBegin.length)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_19")+"\n"+
					getJsLocaleMessage("xtgl","xtgl_cswh_whgl_20"));
			$('#subnoAdd').select();
			$(".ui-dialog-buttonpane button").attr("disabled",false);
			return;
		}
		if(extendSubnoBegin.length == extendSubnoEnd.length)
		{
			for(var k= 0;k< extendSubnoBegin.length && isContinue;k+=1)
			{
			
			    begintemp = extendSubnoBegin.toString().substring(k,k+1);
	 			endtemp = extendSubnoEnd.toString().substring(k,k+1);
	 			e1 = parseInt(endtemp);
	 			b1 = parseInt(begintemp);
	 			 
	 			if( e1<b1)
				{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_21"));
					$("#eextendSubnoBegin").select();
					$(".ui-dialog-buttonpane button").attr("disabled",false);
					return;
				} 
	 			if( e1>b1)
				{
	 				isContinue=false;
				} 
	 		 } 
		}
		if(!(parseIntVa(extendSubnoBegin)<=parseIntVa(subno) && parseIntVa(extendSubnoEnd)>=parseIntVa(subno)))
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_22"));
			$('#subnoAdd').select();
			$(".ui-dialog-buttonpane button").attr("disabled",false);
		return;
		}
 	}
	 if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_23")))
     {
		 var subNumber ;
		 if(allotType == 0){
			 subNumber = subno.length;
		 }else{
			 subNumber = extendSubnoEnd.length;
		 }
		 var lgcorpcode = $('#lgcorpcode').val();
			$.post(pathUrl+"/tai_subno.htm?method=checkSubnoIsUsed",
					{
						subNumber:subNumber,
						userId:userId						
					},function(result){
						if(result == "true")
						{
							$.post(pathUrl+"/tai_subno.htm?method=checkSubnoScope",
									{
										subno : subno,
										lgcorpcode:lgcorpcode,
										userId:userId,
										allotType:allotType,
										extendSubnoBegin:extendSubnoBegin,
										extendSubnoEnd:extendSubnoEnd,
										type:1,
										suId:suId
									},function(result){
										if(result == "true")
										{
											$.post(pathUrl+"/tai_subno.htm?method=update",
													{
													    suId:suId,
													    bts:bTypes,
													    lgcorpcode:lgcorpcode,
													    codes:codes,
														subno : subno,
														userId:userId,
														allotType:allotType,
														extendSubnoBegin:extendSubnoBegin,
														extendSubnoEnd:extendSubnoEnd
														
													},function(result){
														if(result == "subnoExists")
														{
															alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_24"));
															$('#subnoAdd').select();
															$(".ui-dialog-buttonpane button").attr("disabled",false);
														}else if(result == "true")
														{
															alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_25"));
															location.href = pathUrl+"/tai_subno.htm?method=find";
														}else {
															alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_26"));
															location.href = pathUrl+"/tai_subno.htm?method=find";
														}
													}
												);
										}else if(result == "false"){
												alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_27"));
												$(".ui-dialog-buttonpane button").attr("disabled",false);
												return;
										}else if(result == "error"){
											alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_28"));
											$(".ui-dialog-buttonpane button").attr("disabled",false);
											return;
										}
										else {
											alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_29"));
											$(".ui-dialog-buttonpane button").attr("disabled",false);
										}
									}
								);
							
						}else {
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_30"));
							$(".ui-dialog-buttonpane button").attr("disabled",false);
							return;
						}
					}
				);
		 
		 
					
     }
}

function addSubno()
{
	var pathUrl = $("#pathUrl").val();
	var bTypes = $("#bTypes").val();
	var codes = "";
	var subno = $('#subnoAdd').val();

	var userId = $.trim($("#spUser1").text());

	var allotType = $("#allotType1").val();
	var extendSubnoBegin = $("#extendSubnoBegin1").val();
	var extendSubnoEnd = $("#extendSubnoEnd1").val();
	
	var begintemp ;
	var endtemp;
	var count = 0;
 	var b1 ;
 	var e1;
 	switch(bTypes)
	{
	case "0" :
		    codes = $("#menuCode").val();
	        break;
	case "1" :
		codes = $("#busCode").val();
        break;
	case "2" :
		codes = $("#aproId").val();
        break;
	case "3" :
		codes = $("#adepCode").val();
        break;
	case "4" :
		codes = $("#asysCode").val();
        break;
	}
 	if (codes == "")
 	{
 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_13"));
 		$(".ui-dialog-buttonpane button").attr("disabled",false);
 		return;
 	}

 	if( userId == "")
 	{
 		//alert("编码类型为机构编码时，请选择账户ID！");
 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_31"));
 		$(".ui-dialog-buttonpane button").attr("disabled",false);
 		return;
 	}
 	if(allotType==1)
 	{
	 	if(extendSubnoBegin == "" || extendSubnoBegin.length>6)
	 	{
	 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_15"));
	 		$("#extendSubnoBegin1").select();
	 		$(".ui-dialog-buttonpane button").attr("disabled",false);
	 		return;
	 	}else{
	 		subno = extendSubnoBegin;
	 		$('#subnoAdd').attr("value",extendSubnoBegin);
	 	}
	 	
	 	if( extendSubnoEnd == "" || extendSubnoEnd.length>6)
	 	{
	 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_16"));
	 		$(".ui-dialog-buttonpane button").attr("disabled",false);
	 		$("#extendSubnoEnd1").select();
	 		return ;
	 	}else if(subno == "" || subno.length>6){
	 		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_17"));
	 		$('#subnoAdd').select();
	 		$(".ui-dialog-buttonpane button").attr("disabled",false);
	 		return;
	 	}
	
		var isContinue=true;
		if(extendSubnoBegin.length >extendSubnoEnd.length)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_18"));
			$("#extendSubnoBegin1").select();
			$(".ui-dialog-buttonpane button").attr("disabled",false);
			return;
		}
		if(subno.length > extendSubnoEnd.length || subno.length < extendSubnoBegin.length)
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_19")+"\n"+
					getJsLocaleMessage("xtgl","xtgl_cswh_whgl_20"));
			$('#subnoAdd').select();
			$(".ui-dialog-buttonpane button").attr("disabled",false);
			return;
		}
		if(extendSubnoBegin.length == extendSubnoEnd.length)
		{
			for(var k= 0;k< extendSubnoBegin.length && isContinue;k+=1)
			{
			
			    begintemp = extendSubnoBegin.toString().substring(k,k+1);
			    endtemp = extendSubnoEnd.toString().substring(k,k+1);
			    var subnotemp = subno.toString().substring(k,k+1);
	 			e1 = parseInt(endtemp);
	 			b1 = parseInt(begintemp);
	 			var s1 = parseInt(subnotemp);
	 			 
	 			if( e1<b1 )
				{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_21"));
					$("#extendSubnoBegin1").select();
					$(".ui-dialog-buttonpane button").attr("disabled",false);
					return;
				}
	 			if( e1>b1)
				{
	 				isContinue=false;
				} 
	 		 } 
		}
		if(!(parseIntVa(extendSubnoBegin)<=parseIntVa(subno) && parseIntVa(extendSubnoEnd)>=parseIntVa(subno)))
			{
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_22"));
				$('#subnoAdd').select();
				$(".ui-dialog-buttonpane button").attr("disabled",false);
			return;
			}
		
 	}
 	
		 var subNumber ;
		 if(allotType == 0){
			 subNumber = subno.length;
		 }else{
			 subNumber = extendSubnoEnd.length;
		 }
		 var lgcorpcode=$("#lgcorpcode").val();
			$.post(pathUrl+"/tai_subno.htm?method=checkSubnoIsUsed",
					{
						subNumber:subNumber,
						userId:userId						
					},function(result){
						if(result == "true")
						{
							
							$.post(pathUrl+"/tai_subno.htm?method=checkSubnoScope",
									{
										subno : subno,
										lgcorpcode:lgcorpcode,
										userId:userId,
										allotType:allotType,
										extendSubnoBegin:extendSubnoBegin,
										extendSubnoEnd:extendSubnoEnd
									},function(result){
										if(result == "true")
										{
											$.post(pathUrl+"/tai_subno.htm?method=add",
													{
													    bts:bTypes,
													    lgcorpcode:lgcorpcode,
													    codes:codes,
														subno : subno,
														userId:userId,
														allotType:allotType,
														extendSubnoBegin:extendSubnoBegin,
														extendSubnoEnd:extendSubnoEnd
														
													},function(result){
														if(result == "subnoExists")
														{
															alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_24"));
															$('#subnoAdd').select();
															$(".ui-dialog-buttonpane button").attr("disabled",false);
														}else if(result == "true")
														{
															alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_44"));
															location.href = pathUrl+"/tai_subno.htm?method=find";
														}else {
															alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_45"));
															$(".ui-dialog-buttonpane button").attr("disabled",false);
														}
													}
												);
										}else if(result == "false"){
											alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_27"));
											$(".ui-dialog-buttonpane button").attr("disabled",false);
											return;
										}else if(result == "error"){
											alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_28"));
											$(".ui-dialog-buttonpane button").attr("disabled",false);
											return;
										}
										else {
											alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_29"));
											$(".ui-dialog-buttonpane button").attr("disabled",false);
										}
									}
								);
							
						
						}else {
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_30"));
							$(".ui-dialog-buttonpane button").attr("disabled",false);
							return;
						}
						
					}
				);
	
}

function parseIntVa(s)
{
	s=s.toString();
	var k=0;
	var isContinue = true;
	for(;k<s.length && isContinue;)
	{
		if(s.substring(k,k+1)!=0)
		{
			isContinue=false;
		}else
		{
			k+=1;
		}
	}
	if (k==s.length)
		return 0;
	else return parseInt(s.substring(k));
}

function getUserId(userId)
{
		 $("#userId_temp").val(userId);
}

function DisableSelect()
{
	var menuCode = $("#menuCode").val();
	
	if(menuCode =='')
	{
		
	}else
	{
		
	}
}

function changeType(id)
{
	var tf = $("#typeFlag").val();
	$(tf).hide();
	switch(id)
	{
	case "0" :
		    $("#typeName").empty();
	        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_32"));
	        break;
	case "1" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_33"));
        break;
	case "2" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_34"));
        break;
	case "3" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_35"));
        break;
	case "4" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_36"));
        break;
	}
     $(".typeClass"+id).show();
     $("#typeFlag").attr("value",".typeClass"+id);
     $("#pageForm").submit();
}

function cType(id)
{
    var tc = $("#tClass").val();
    $(".tClass0").hide();
    $(tc).hide();
    $(".tClass"+id).show();
    $("#tClass").attr("value",".tClass"+id);
}

function ecType(id)
{
    var tc = $("#etClass").val();
    $(tc).hide();
    $(".etClass"+id).show();
    $("#etClass").attr("value",".etClass"+id);
}

function typeInit(type)
{
    $("#typeFlag").attr("value",type);
    $(type).show();
    switch(type)
	{
	case ".typeClass0" :
		    $("#typeName").empty();
	        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_32"));
	        $("#bindTypes").attr("value","0");
	        break;
	case ".typeClass1" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_33"));
        $("#bindTypes").attr("value","1");
        break;
	case ".typeClass2" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_34"));
        $("#bindTypes").attr("value","2");
        break;
	case ".typeClass3" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_35"));
        $("#bindTypes").attr("value","3");
        break;
	case ".typeClass4" :
	    $("#typeName").empty();
        $("#typeName").html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_36"));
        $("#bindTypes").attr("value","4");
        break;
	}
}

function closeDiv(){
	$('#subnoName').html("");
	$('#subnoId').val("");
	$('#oldId').val("");
	$('#guid').val("");
	$('#type').val("");
	$('#editDiv').dialog('close');
}

//删除尾号
function delSubno(guid){
	var lgcorpcode = $('#lgcorpcode').val();
	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_37"))==true){
			$.post("tai_subno.htm?method=delSubno",{subnoId:guid,lgcorpcode:lgcorpcode},function(msg){
				$("#pageForm").attr("action","tai_subno.htm??method=find&&pageIndex="+pageIndex+"&&pageSize="+pageSize );
				if(msg == "1"){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_38"));
					submitForm();
				}else if(msg == "errer"){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
					return;
				}else if(msg == ""){
					alert("操作失败！");
					return;
				}else{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_29"));
					return;
				}
			});
	}
}

//更新尾号	
function updateSubno(subnoName,subnoId,guid,type){
	//$('#subnoName').val(subnoName);
	$('#subnoName').html("");
	$('#subnoName').html(subnoName);
	if(subnoId==null)
	{
		$('#subnoId').val("");
		$('#oldId').val("");
	}else
	{
		$('#subnoId').val(subnoId);
		$('#oldId').val(subnoId);
	}
	
	$('#guid').val(guid);
	$('#type').val(type);
	$('#cmName').html("");
	if("1" == type){
		$('#cmName').html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_36"));
	}else if("2" == type){
		$('#cmName').html(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_39"));
	}
	$('#editDiv').dialog('open');
}
function update(){
	var lgcorpcode = $('#lgcorpcode').val();
	$(".ui-dialog-buttonpane button").attr("disabled",true);	
	var updateId = $.trim($('#subnoId').val());
	var oldId = $.trim($('#oldId').val());
	var guid = $('#guid').val();
	var type = $('#type').val();
	if(updateId == null || "" == updateId || updateId.length == 0){
		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_40"));
		$(".ui-dialog-buttonpane button").attr("disabled",false);
		return;
	}
	else if(updateId!=updateId.replace(/[^\d]/g,''))
	{
		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_41"));
		$("#subnoId").focus();
		$(".ui-dialog-buttonpane button").attr("disabled",false);
		return;
	}
	else if(oldId == updateId){
		alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_42"));
		$(".ui-dialog-buttonpane button").attr("disabled",false);
		$('#editDiv').dialog('close');
	}else{
		$.post("tai_subno.htm?method=updateSubno",{guid:guid,updateId:updateId,type:type,lgcorpcode:lgcorpcode},function(msg){
				$("#pageForm").attr("action","tai_subno.htm??method=find&&pageIndex="+pageIndex+"&&pageSize="+pageSize );
				if(msg == "1"){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_38"));
					submitForm();
				}else if(msg == "errer"){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
					$(".ui-dialog-buttonpane button").attr("disabled",false);
					return;
				}else if(msg == "2"){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_43"));
					$('#subnoId').val(oldId);
					$(".ui-dialog-buttonpane button").attr("disabled",false);
					return;
				}else if(msg == ""){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
					$(".ui-dialog-buttonpane button").attr("disabled",false);
					return;
				}else{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_29"));
					$(".ui-dialog-buttonpane button").attr("disabled",false);
					return;
				}
		
		});
	}
}

//发送起止时间控制
function rtime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#sendtime").attr("value");
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
		    case "01":day = 31;break;
		    case "02":day = 28;break;
		    case "03":day = 31;break;
		    case "04":day = 30;break;
		    case "05":day = 31;break;
		    case "06":day = 30;break;
		    case "07":day = 31;break;
		    case "08":day = 31;break;
		    case "09":day = 30;break;
		    case "10":day = 31;break;
		    case "11":day = 30;break;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)+1));
		    mon = "01";
		}
		max = year+"-"+mon+"-"+day+" 23:59:59"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

};

function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#recvtime").attr("value");
    var min = "1900-01-01 00:00:00";
	if(v.length != 0)
	{
	    max = v;
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
		min = year+"-"+mon+"-01 00:00:00"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
}

function qeuryUser()
{
	var state=$("#state").val();
	if(state=="2")
	{
		$("#xx").show();
		$("#yy").show();
		$("#ww").hide();
	}
	else
	{
		$("#xx").hide();
		$("#yy").hide();
		$("#ww").show();
	}
}