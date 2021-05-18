	function addPushRsProtocol()
	{
		var newRspCmd = $.trim($('#newRspCmd').val());
		var newCargName = $.trim($('#newCargName').val());
		var newRspStatus = $.trim($('#newRspStatus').val());
		var newCargValue = $('#newCargValue').val();
		var corpCode=$.trim($("#corp_code").val());
		$('#addcancel').attr("disabled",true);
		$('#addsubmit').attr("disabled",true);
		if(newCargName == ""){
			/*客户参数名不能为空！*/
			alert(getJsLocaleMessage("txgl","txgl_js_pushRsProtocol_1"));
			$('#newCargName').val("");
			$('#newCargName').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		
		if(newCargName.length>25)
		{
			/*客户参数名长度不能超过25位！*/
			alert(getJsLocaleMessage("txgl","txgl_js_pushRsProtocol_2"));
			$('#newCargName').val("");
			$('#newCargName').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		
		if(newCargValue == ""){
			/*客户字段值不能为空！*/
			alert(getJsLocaleMessage("txgl","txgl_js_pushRsProtocol_3"));
			$('#newCargValue').val("");
			$('#newCargValue').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
			$.post("wg_pushRsProtocol.htm",
				{
					method:"add",
					rspCmd : newRspCmd,
					cargName : newCargName,
					rspStatus : newRspStatus,
					cargValue : newCargValue,
					corpCode : corpCode
				},function(result){
					if(result == "true")
					{
						/*新建成功！*/
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_17"));
						doCancel();
						var url = 'wg_pushRsProtocol.htm';
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
						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&isContainsSun=1";
						location.href=url+conditionUrl;
					}else if(result == "noBaseInfo")
					{
						/*没有获取到相应的企业基本协议信息！*/
						alert(getJsLocaleMessage("txgl","txgl_js_pushRsProtocol_4"));
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else if(result=="noCargValue")
					{
						/*请填写客户字段值！*/
						alert(getJsLocaleMessage("txgl","txgl_js_pushRsProtocol_5"));
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else if(result == "repeat")
					{
						/*该推送用户回应协议已存在！*/
						alert(getJsLocaleMessage("txgl","txgl_js_pushRsProtocol_6"));
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else 
					{
						/*新建失败！*/
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_18"));
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}
				}
			);
	}
	
	function editPushRsProtocol()
	{
		var cargValue = $('#cargValueEdit').val();
		var rspCmdHid = $('#rspCmdHid').val();
		var cargNameHid = $('#cargNameHid').val();
		var rspStatusHid = $('#rspStatusHid').val();
		
		
		var idEdit=$('#idEdit').val();
		$('#editcancel').attr("disabled",true);
		$('#editsubmit').attr("disabled",true);

		if(cargValue == "")
		{
            /*客户字段值不能为空！*/
            alert(getJsLocaleMessage("txgl","txgl_js_pushRsProtocol_3"));
			$('#cargValueEdit').val("");
			$('#cargValueEdit').select();
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
			return;
		}
			$.post("wg_pushRsProtocol.htm",
				{
					method:"edit",
					cargValue : cargValue,
					rspCmd:rspCmdHid,
					cargName:cargNameHid,
					rspStatus:rspStatusHid,
					id : idEdit
				},function(result){
				    if(result == "true")
					{
                        /*修改成功！*/
                        alert(getJsLocaleMessage("common","common_modifySucceed"));
						$("#editDiv").dialog("close");
						
						var url = 'wg_pushRsProtocol.htm';
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
						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize;
						location.href=url+conditionUrl;							
						
					}else {
                        /*修改失败！*/
                        alert(getJsLocaleMessage("common","common_modifyFailed"));
						$('#editcancel').attr("disabled",false);
					    $('#editsubmit').attr("disabled",false);
					}
				}
			);

	}
	
	    function doCancel()
    	{
	         $("#bindDivBusName").val("");
	         $("#busDescriptionAdd").val("");
	         $("#addDiv").dialog("close");
    	}
	    function doCancelEdit()
    	{
	         $("#bindDivBusName").val("");
	         $("#busDescriptionAdd").val("");
	         $("#editDiv").dialog("close");
    	}
	
	var RspCmd = "";
	var CargName = "";
	var RspStatus = "";
	var CargValue = "";
	function update(busId)
	{
		RspCmd = $.trim($('#lbRspCmd'+busId).text());
		CargName = $.trim($('#lbCargName'+busId).text());
		RspStatus = $.trim($('#lbRspStatus'+busId).text());
		CargValue = $.trim($('#lbCargValue'+busId).text());
		var HidRspCmd=$.trim($('#HidRspCmd'+busId).val());
		var HidRspStatus=$.trim($('#HidRspStatus'+busId).val());
		
		$('#rspCmdEdit').val(RspCmd);
		$('#cargNameEdit').val(CargName);
		$("#rspStatusEdit").val(RspStatus);
		$('#rspCmdHid').val(HidRspCmd);
		$('#cargNameHid').val(CargName);
		$("#rspStatusHid").val(HidRspStatus);
		$("#cargValueEdit").val(CargValue);
		$("#idEdit").val(busId);
		$("#editDiv").dialog("open");
	}
	
	
	function del(id,rspCmd, cargName,rspStatus,ecid){
	/*您确定要删除该条信息吗？*/
	if(confirm(getJsLocaleMessage("common","common_js_draft_2")))
	{
		$.get("wg_pushRsProtocol.htm?method=delete&id="+id+"&rspCmd="+rspCmd+"&cargName="+cargName+"&rspStatus="+rspStatus+"&ecid="+ecid,{},function(result){
			if(result>0)
			{
                /*删除成功！*/
                alert(getJsLocaleMessage("common","common_deleteSucceed"));
				location.href = "wg_pushRsProtocol.htm";
			}else{
                /*删除失败！*/
                alert(getJsLocaleMessage("common","common_deleteFailed"));
				location.href = "wg_pushRsProtocol.htm";
			}
		});
	}
}