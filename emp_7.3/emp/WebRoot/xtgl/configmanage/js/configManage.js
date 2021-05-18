function saveConfig(paramType) {
	var confirmStr;
	var paramId;
	var saveData = {};
	switch (paramType) {
		case 0:
			paramId = "corpConfigParam";
			confirmStr = "企业配置参数";
			break;
		case 1:
			paramId = "sysConfigParam";
			confirmStr = "系统配置参数";
		    break;
		case 2:
			paramId = "globalConfigParam";
			confirmStr = "全局配置参数";
			break;
	}
    if (!confirm("确定保存”" + confirmStr + "“吗？")){
    	return;
	}
	var inputObj = $("#" +paramId+ " .valueinput");
	var key;
	var value;
	var dataType;
	for (var i=0; i<inputObj.length; i++){
		dataType = $(inputObj[i]).attr("datatype");
		key = $(inputObj[i]).attr("valuekey");

		if (dataType == 1){
			// 下拉框
			value = $("#"+key + " option:selected").val().trim();

		}else {
			value = $(inputObj[i]).val().trim();
		}
		
		if (value == null || value == ""){
			alert($(inputObj[i]).attr("paramname") + "，参数值为空，不能保存！");
			inputObj[i].focus();
			return;
		}
		if (dataType == 0) {
			value = parseInt(value);
		}

		if (paramType == 0) {
			if (!checkOldValue(value, $(inputObj[i]))){
				return;
			}
		}
		if (key == "HTTP_REQUEST_TIMEOUT" || key == "HTTP_RESPONSE_TIMEOUT") {
			value = value * 1000;
			// 请求/响应时间
			if (value < 60000 || value > 240000) {
				alert($(inputObj[i]).attr("paramname") + "，参数值不在取值范围内！");
				inputObj[i].focus();
				return;
			}
		}
		if (key == "LOG_PRINT_INTERVAL") {
			value = value * 1000;
			// 日志打印间隔
			if (value < 10000 || value > 60000) {
				alert($(inputObj[i]).attr("paramname") + "，参数值不在取值范围内！");
				inputObj[i].focus();
				return;
			}
		}

		if (key == "BALANCE_REQ_INTERVAL") {
			value = value * 1000;
			// 运营商余额请求间隔
			if (value < 120000 || value > 600000) {
				alert($(inputObj[i]).attr("paramname") + "，参数值不在取值范围内！");
				inputObj[i].focus();
				return;
			}

		}
		if (key == "BLACK_MAXCOUNT") {
			value = value * 10000;
			// 黑名单支持最大数
			if (value < 10000000 || value > 50000000) {
				alert($(inputObj[i]).attr("paramname") + "，参数值不在取值范围内！");
				inputObj[i].focus();
				return;
			}
			if (!checkOldValue(value, $(inputObj[i]))){
				return;
			}
		}
		
		if (key == "cxtjMtExportLimit") {
			value = value * 10000;
			// 下行记录导出最大条数
			if (value < 1 || value >15000000){
				alert($(inputObj[i]).attr("paramname") + "，参数值不在取值范围内！");
				inputObj[i].focus();
				return;
			}
		} 

		
		if (key == "ENDHOUR"){
			if (!checkENDHOUR($(inputObj[i]))) {
				return;
			}
		}
		saveData[key] = value;
	}

	$.post(
		"webParamConfig.htm?method=saveConfig",
		{
		paramType: paramType,
		updateList: JSON.stringify(saveData)
		},
		function (result) {
			if (result == "true") {
				alert("保存成功！");
			} else if (result == "paramError") {
				alert("存在参数值为空，保存失败！");
			} else {
				alert("保存失败！");
			}
		}
	);

}

function checkOldValue(value, obj){
	// 设置的值不能小于当前值
	var oldData = parseInt(obj.attr("olddata").trim());
	if (parseInt(value) < oldData) {
		alert(obj.attr("paramname") + "，设置的值不能小于当前值：" + oldData + "！");
		obj.focus();
		return false;
	}
	return true;
}

function checkParamNum(obj) {
	var dataType = obj.attr("datatype");
	if (dataType == 0){
		phoneInputCtrl(obj);
	}

}
function checkENDHOUR(obj) {
	// 时间点检查
	var value = obj.val();
	var regTime = /^[6-8]:\d{2}$/;
	if (regTime.test(value)){
		var time = value.split(":");
		if (parseInt(time[1]) > 59 || parseInt(time[1])<0){
			// 时间格式不正确
			alert(obj.attr("paramname") + "，参数值不在取值范围内！");
			obj.focus();
			return false;
		}
		if (parseInt(time[0]) == 8 && parseInt(time[1])>0){
			// 时间格式不正确
			alert(obj.attr("paramname") + "，参数值不在取值范围内！");
			obj.focus();
			return false;
		}
	} else {
		// 时间格式不正确
		alert(obj.attr("paramname") + "，参数错误！");
		obj.focus();
		return false;
	}

	return true;
}