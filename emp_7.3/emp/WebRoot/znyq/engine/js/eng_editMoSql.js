
$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			$("#subConfigSqlBtn").click(subConfigSql);
});

function checkSubBefore() {
	var dbId = $("#dbId").val();
	var sql = $("#sql").val();
	if (dbId == "" || dbId == null) {
		//alert("数据库连接不能为空,请选择连接");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_sjkljbnwk"));
		return false;
	}
	if (sql == "" || sql == null) {
		//alert("SQL语句不能为空");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_sqlyjbnwk"));
		return false;
	}
	return true;
}

function subConfigSql() {

	if (checkSubBefore()) {
		//if (confirm("确定提交吗?")) {
		if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_qdtjm"))) {
			$("#subConfigSqlBtn").attr("disabled", "disabled");
			$("#subConfigSqlBackBtn").attr("disabled", "disabled");
			//var msg = $("__tag_40$56_").css("color", "green").text(
			//		"正在提交,请稍候.........");
			var msg = $("__tag_40$56_").css("color", "green").text(
					getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_zztjqsh"));
			$("#subConfigSqlBackBtn").parent().append(msg);
			$("#addManualConfig").attr("action",$("#addManualConfig").attr("action")+"&lgcorpcode="+$("#lgcorpcode").val())
			$("#addManualConfig").submit();
		}
	}
}

//获取短信模板内容
function getSql2(sqlId) {
	var $sqlContent = $("form[name='addManualConfig']").find("textarea[name='sql']");
	if(sqlId != "")
	{
		$.post("tem_smsTemplate.htm",{method:"getTmMsg",tmId:sqlId},function(msg)
			{
				$sqlContent.val(msg);
			}
		);
	}else
	{
		$sqlContent.val("");
		//location.href=location.href;
		//$sqlContent.val("");
	}
}