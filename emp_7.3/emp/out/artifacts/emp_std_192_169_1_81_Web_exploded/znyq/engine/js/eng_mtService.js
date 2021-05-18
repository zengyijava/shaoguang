
function deleteSer(serId,serName,runState) {
	var msg;
	var lgcorpcode = $('#lgcorpcode').val();
	if(runState==1)
	{
		//msg="该业务处于运行状态，请确定是否删除？";
		msg=getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_gywcyyxztqqdsfsc");
	}else
	{
		//msg="确定删除选中的下行短信业务吗？";
		msg=getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdscxzdxxdxywm");
	}
	if (serId == "") {
		//alert("缺少下行业务ID参数！")
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qsxxywidcs"))
	} else {
		if (confirm(msg)) {
			$.post("eng_mtService.htm",
						 {
							serId : serId,
							serName:serName,
							method:"delete",
							lgcorpcode:lgcorpcode
						},
						function(result) {
							if (result== "true") {
								//alert("删除成功！");
								alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sccg"));
								$("#pageForm").submit();
							} else {
								//alert("删除失败！");
								alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_scsb"));
							}
					});
		}

	}
}

function changestate(serId,serName)
{
	var lgcorpcode = $('#lgcorpcode').val();
	var runState=$("#runState"+serId).val();
	$.post("eng_mtService.htm",
		 {
		 	method : "updateStatus",
			serId : serId,
			runState:runState,
			serName:serName,
			lgcorpcode:lgcorpcode
		},
		function(result) {
			if (result == "true") {
				//alert("修改成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgcg"));
				$("#pageForm").submit();
			}else{
				//alert("修改失败！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgsb"));
			}
	});
//}
}

function doOk() 
{
	$(window.frames['serInfoflowFrame'].document).find("#submitcheck").click();
}

function doTimeOk() 
{
	$(window.frames['exctimeInfoFrame'].document).find("#submitcheck").click();
}

function closeTimeInfoDiv()
{
	$("#exctimeInfoDiv").dialog("close");
	$("#exctimeInfoFrame").attr("src","");
	
}

function closeSerInfodiv()
{
	$("#serInfoDiv").dialog("close");
	$("#serInfoflowFrame").attr("src","");
	
}

		