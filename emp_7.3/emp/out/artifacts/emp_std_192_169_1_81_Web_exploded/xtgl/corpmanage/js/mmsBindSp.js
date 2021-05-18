
function doJhSx(type,id){
	var msg = "";
	var resultMsg = "";
	if(type == 1){
		//msg = "确定要失效该绑定关系吗？";
		msg = getJsLocaleMessage("xtgl","xtgl_qygl_qdysxgbdgxm");
	}else if(type == 2){
		//msg = "确定要激活该绑定关系吗？";
		msg = getJsLocaleMessage("xtgl","xtgl_qygl_qdyjhgbdgxm");
	}
	$("a[name='querySP']").attr("disabled","disabled");
    if(confirm(msg))
 	{
 	  var pathUrl = $("#pathUrl").val();
	  $.post(pathUrl+"/cor_mmsBindSp.htm",
		{
			method:"update",
			type:type,
			mmsId:id
		},function(result){
			if(result.indexOf("html") > 0){
    			window.location.href=location;
    			$("a[name='querySP']").attr("disabled","");
    		    return;
    		}else if(result != null && result != "" && result == "true"){
    			//alert("操作成功！");
    			alert(getJsLocaleMessage("xtgl","xtgl_qygl_czcg"));
    			$("a[name='querySP']").attr("disabled","");
    			location.href = pathUrl+"/cor_mmsBindSp.htm?method=find";
    		}else if(result == "false"){
    			//alert("操作失败！");
    			alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
    			$("a[name='querySP']").attr("disabled","");
    			return;
    		}else{
    			//alert("操作失败！");
    			alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
    			$("a[name='querySP']").attr("disabled","");
    			return;
    		}
		});
	}
    else
    {
		$("a[name='querySP']").attr("disabled","");
    }
}

function doCancel()
{
   location.href = "cor_mmsBindSp.htm?method=find";
}
function addMmsAccBind(){
	var smsaccount =$("#smsaccount").val();
	var ids = "";
	var pathUrl = $("#pathUrl").val();
	var corpCode = $("#corpCode").val();
	 if (corpCode == "")
    {
        //alert("请选择需要绑定"+smsaccount+"的企业！");
        alert(getJsLocaleMessage("xtgl","xtgl_qygl_qxzxybd")+smsaccount+getJsLocaleMessage("xtgl","xtgl_qygl_dqy"));
        return;
	}
		$('input[name="mmsUsers"]:checked').each(function(){
		 ids += $(this).val()+",";
    });
	 if (ids == "")
    {
        //alert("请至少选择一个"+smsaccount+"！");
        alert(getJsLocaleMessage("xtgl","xtgl_qygl_qzsxzyg")+smsaccount+getJsLocaleMessage("xtgl","xtgl_qygl_gth"));
        return;
	}

	$("#btnOK").attr("disabled","true");
    $.post(pathUrl+"/cor_mmsBindSp.htm",
	{
		method:"add",
		corpCode:corpCode,
		mmsUseres:ids
	},function(result){
		if(result.indexOf("html") > 0){
			$("#btnOK").attr("disabled","");
			window.location.href=location;
		    return;
		}else if(result != null && result != "" && result > 0){
			//alert("成功新建"+result+"条记录！");
			alert(getJsLocaleMessage("xtgl","xtgl_qygl_cgxj")+result+getJsLocaleMessage("xtgl","xtgl_qygl_tjl"));
			$("#btnOK").attr("disabled","");
			location.href = pathUrl+"/cor_mmsBindSp.htm?method=find";
		}else if(result == 0){
			//alert("新建失败,请检查 数据库连接是否正常！");
			alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjsbqjcsjkljsfzc"));
			$("#btnOK").attr("disabled","");
			return;
		}else{
			//alert("新建失败！");
			alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjsb"));
			$("#btnOK").attr("disabled","");
			return;
		}
	});
}