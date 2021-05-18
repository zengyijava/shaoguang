
$(document).ready(function() {
	//floatingRemind("databaseTip","输入用于执行的sql语句，sql语句可以带执行条件，如select * from xxx where name = '#P_0#'，其中#P_0#为上行信息中的手机号，#P_1#，#P_2#，..,#P_n#为短信中的用户参数");
	floatingRemind("databaseTip",getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_sryyzxdsqlyj"));

	getLoginInfo("#hiddenValueDiv");
	$("#subConfigSqlBtn").click(subConfigSql);
});

function checkSubBefore() {
	var dbId = $("#dbId").val();
	var sql = $.trim($("#sql").val());
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
		$("#subConfigSqlBtn").attr("disabled", "disabled");
		$("#subConfigSqlBackBtn").attr("disabled", "disabled");
		//var msg = $("__tag_40$56_").css("color", "green").text("正在提交,请稍候.........");
		var msg = $("__tag_40$56_").css("color", "green").text(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_zztjqsh"));
		$("#subConfigSqlBackBtn").parent().append(msg);
		$("#addManualConfig").attr("action",$("#addManualConfig").attr("action")+"&lgcorpcode="+$("#lgcorpcode").val())
		$("#addManualConfig").submit();
		
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
		location.href=location.href;
	}
}

//获取新模板
function getTmplInfo() {
	
	var lguserid=$("#lguserid").val();
	$.post("eng_mtProcess.htm?method=getSmsTmpl&dsflag=2&lguserid="+lguserid,function(msg)
			{
				if(msg== "" || msg=="errtmpl"){
					//alert("获取模板信息失败。");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_hqmbxxsb"));
					return;
				}
				if(msg=="nodata"){
					return;
				}
				msg = eval("("+msg+")");
				//var selStr = '<option value="" style="color:#666666">请选择</option>';
				var selStr = '<option value="" style="color:#666666">'+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_qxz")+'</option>';
				$.each(msg, function(idx,item){
					selStr += '<option value="'+item.tmid+'" >'+item.tmname+'</option>';
				});
				$("#tempSel").find("option").remove();
				$("#tempSel").append(selStr);
			}
	);
}

