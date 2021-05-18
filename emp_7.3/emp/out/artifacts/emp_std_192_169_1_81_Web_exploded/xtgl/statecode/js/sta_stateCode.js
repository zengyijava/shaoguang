function del(stateId)
	{
		if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_47")))
		{
			$.post("sta_stateCode.htm",{method:"delete",stateId : stateId},function(result){
				if(result=="true")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
					var lgcorpcode = $('#lgcorpcode').val();
					var pageIndex=$('#txtPage').val();
					var pageSize=$('#pageSize').val();
					//删除默认跳转到第一页
					location.href="sta_stateCode.htm?method=find&lgcorpcode="+lgcorpcode+"&pageIndex=1&pageSize="+pageSize;
					//dom删除当前要删除的行
					//$("#tr-"+stateId).remove();
				}else 
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
					return;
				}
			});
		}
	}
	function showMenu() {
		//hideMenu2();
		var sortSel = $("#depNam");
		var sortOffset = $("#depNam").offset();
		$("#dropMenu").toggle();
	}
    function doCancel()
    {
         $("#stateDesAdd").val("");
         $("#addDiv").dialog("close");
    }
   
	var STATECODE = "";
	var MAPPINGCODE = "";
	var STATEDES = "";
	var STATEID = 0;
	
	function update(stateId)
	{
		STATECODE = $.trim($('#lbStateCode'+stateId).text());
		MAPPINGCODE = $.trim($('#lbMappingCode'+stateId).text());
		STATEDES = $.trim($('#lbDes'+stateId).text());
		$('#stateCodeEdit').text(STATECODE);
		$('#mappingCodeEdit').text(MAPPINGCODE);
		$('#stateDesEdit').val(STATEDES);
		STATEID = stateId;
		$("#editDiv").dialog("open");
	}
	
	function editStateCode()
	{
		var stateCode = $.trim($('#stateCodeEdit').text());
		var mappingCode = $.trim($('#mappingCodeEdit').text());//映射码
		var stateDes = $('#stateDesEdit').val();
		
		$('#editcancel').attr("disabled",true);
		$('#editsubmit').attr("disabled",true);
		
		//验证状态码说明
		if($('#stateDesEdit').val().length>64){
		  alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_48"));
		  $('#stateDesEdit').select();
		  $('#editcancel').attr("disabled",false);
		  $('#editsubmit').attr("disabled",false);
		  return ;
		}
		else if(STATEDES != stateDes)
		{

			var lguserid = $("#lguserid").val();
			var lgcorpcode = $('#lgcorpcode').val();
			$.post("sta_stateCode.htm",
				{
					method:"update",
					lgcorpcode:lgcorpcode,
					lguserid:lguserid,
					stateCode : stateCode,
					mappingCode : mappingCode,
					stateDes : stateDes,
					stateId : STATEID
				},function(result){
					var resultSplit=result.split("#")[0];
					var updateTimeTemp=result.split("#")[1];
					updateTimeTemp=updateTimeTemp.substring(0,updateTimeTemp.lastIndexOf("."));
					if(resultSplit == "nameExists")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_49"));
						$('#stateCodeEdit').select();
						$('#editcancel').attr("disabled",false);
					    $('#editsubmit').attr("disabled",false);
					}else if(resultSplit == "true")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_40"));
						$("#editDiv").dialog("close");
						//修改之后状态码
						$("#lbDes"+STATEID).text(stateDes);
						//修改之后的更新时间
						$("#lbUpdateTime-"+STATEID).text(updateTimeTemp);
						$('#editcancel').attr("disabled",false);
					    $('#editsubmit').attr("disabled",false);
						var url = 'sta_stateCode.htm';
						var conditionUrl = "";
						if(url.indexOf("?")>-1)
						{
							conditionUrl="&";
						}else
						{
							conditionUrl="?";
						}
						$("#hiddenValueDiv").find(" input").each(function(){
							conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
						});
						var pageIndex=$('#txtPage').val();
						var pageSize=$('#pageSize').val();
						var stateCode = $('#stateCode').val();
						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&isContainsSun=1";
						location.href=url+conditionUrl;						
						
					}else {
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_41"));
						$('#editcancel').attr("disabled",false);
					    $('#editsubmit').attr("disabled",false);
					}
				}
			);
		}
		else
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_50"));
			$('#editcancel').attr("disabled",false);
		    $('#editsubmit').attr("disabled",false);
		}
	}

	function addStateCode()
	{
		var stateCode = $.trim($('#stateCodeAdd').val());
		var mappingCode = $.trim($('#mappingCodeAdd').val());
		var stateDes = $.trim($('#stateDesAdd').val());
		$('#addcancel').attr("disabled",true);
		$('#addsubmit').attr("disabled",true);
		if(stateCode == ""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_51"));
			$('#stateCodeAdd').val("");
			$('#stateCodeAdd').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		
		var reg = /^[0-9a-zA-Z]*$/g;
		if(mappingCode!=""&&!reg.test(mappingCode)){
			//映射码只能是数字+字母！
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_58"));
			$('#mappingCodeAdd').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		
		if(stateCode.length>7){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_52"));
			$('#stateCodeAdd').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(mappingCode.length>7){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_57"));
			$('#mappingCodeAdd').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if($('#stateDesAdd').val().length>64)
		{
		    alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_53"));
			$('#stateDesAdd').select();
		    $('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
		    return;
		}
		else
		{
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $('#lgcorpcode').val();
			$.post("sta_stateCode.htm",
				{
					method:"add",
					lgcorpcode:lgcorpcode,
					lguserid:lguserid,
					stateCode : stateCode,
					mappingCode : mappingCode,
					stateDes : stateDes
				},function(result){
					if(result == "nameExists")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_49"));
						$('#stateCodeAdd').select();
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else if(result == "true")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_54"));
						doCancel();
						var url = 'sta_stateCode.htm';
						var conditionUrl = "";
						if(url.indexOf("?")>-1)
						{
							conditionUrl="&";
						}else
						{
							conditionUrl="?";
						}
						$("#hiddenValueDiv").find(" input").each(function(){
							conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
						});
						var pageIndex=$('#txtPage').val();
						var pageSize=$('#pageSize').val();
						var busName = $('#stateCode').val();
						conditionUrl = conditionUrl+"pageIndex=1&pageSize="+pageSize+"&isContainsSun=1";
						location.href=url+conditionUrl;
					}else {
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_55"));
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}
				}
			);
		}
	}
	function regs(va) {
		var pattern = /^[A-z0-9]+$/;
		if(pattern.test(va.val())) {
			return;
		} else {
			
		}
	}
	function rtime()
	{
	    var max = "2099-12-31 23:59:59";
	    var v = $("#sendtime").attr("value");
	    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
    }
	function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#recvtime").attr("value");
	    var min = "1900-01-01 00:00:00";
	    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:v});
	}
	
	function modify(t)
	{
  			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			$("#singledetail").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_56"));
  			$("#singledetail").dialog("open");
	}