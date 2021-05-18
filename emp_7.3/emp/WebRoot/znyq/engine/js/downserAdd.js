var docUrl = document.URL;
var urls = docUrl.split("/");
docUrl = urls[0] + "//" + urls[2] + "/" + urls[3];
// 添加下行业务项目管理
$(document).ready(function() {
	//$("#serName").blur(existSerName); // 业务名称输入框失去焦点的时候触发
		// $("#comments").blur(checkComments); //业务描述输入框失去焦点的时候触发
		//$("#password").blur(checkPassword); // 账户密码输入框失去焦点的时候触发
		$("#formSubmit").click(formSubmit); // 提交表的时候触发
		getUserDate();// 下行业务默认获得账户Id列表
	});

// 检查业务名称
function existSerName() {
	var serName = $("#serName").val(); // 获取业务名称
	serName = serName.replace(/\s+/g, "");
	var serType = $("#serType").val(); // 获取业务类型
	if ($.trim(serName) == "") { // 业务名称是否为空
		$("#serName").parent().find("span").remove();
		//var msg = $("<span>").css("color", "red").text("不能为空，请输入业务名称！");
		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmcbnwk"));
		
		$("#serName").parent().append(msg);
	} else if (serName.length > 20) { // 判断业务名称的长度是否大于限制的长度
		$("#serName").parent().find("span").remove();
		//var msg = $("<span>").css("color", "red").text(
		//		"你当前业务名称长度为:【" + serName.length + "】超出限制为【20】的长度！");

		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ndqywmccdw")
				 + serName.length +getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ccxzw20dcd") );
		$("#serName").parent().append(msg);
	} else { // 判断业务名称是否存在
		/*
		 * $.ajax({ type : "POST", url : docUrl+"/eng_mtService.htm", data : {
		 * serName : serName , serType:$('#serType').val(),
		 * hidOpType:"checkName" }, success: function(data) {
		 * //判断业务名称的结果，并且显示判断结果 $("#serName").parent().find("span").remove(); if
		 * (data=="true") { var msg = $("<span>").css("color",
		 * "red").text("业务名称:【"+serName+"】已存在，无法新建！");
		 * $("#serName").parent().append(msg); } else if(data=="false"){ var msg =
		 * $("<span>").css("color", "green").text("业务名称:【"+serName+"】
		 * 不存在，可以新建！"); $("#serName").parent().append(msg); }else { var msg =
		 * $("<span>").css("color",
		 * "red").text("验证业务名称:【"+serName+"】失败，无法提交！");
		 * $("#serName").parent().append(msg); } } });
		 */
	}
}

// 提交表单之前在一次判断表单
function formSubmit() 
{
	var serName = $("#serName").val(); // 获取业务名称
	//if(!controlInput(serName,"业务名称只能由汉字、英文字母、数字、下划线组成！",true))
	if(!controlInput(serName,getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmczyhz"),true))
	{
		return;
	}
	serName = serName.replace(/\s+/g, ""); // 清除业务名称所有空格
	var comments = $("#comments").val(); // 获取业务描述
	comments = comments.replace(/\s+/g, ""); // 清除业务描述中所有空格
	var spPwd = $("#password").val(); // 获取账户密码
	var runState = $("input[name='runState']:checked").val(); // 获取运行状态
	var spUserid = $("#spUser option:selected").text(); // 获取SP的ID
	spUserid = spUserid.replace(/\s+/g, ""); // 清除空格
	//var staffName = $("#spUser option:selected").val(); // 获取SP名称
	//staffName = staffName.replace(/\s+/g, ""); // 清除空格
	var serType = $("#serType").val(); // 获取业务类型
	$("#comments").parent().find("span").remove();
	$("#password").parent().find("span").remove();
	$("#orderCode").parent().find("span").remove();
	if ($.trim(serName) == "") 
	{ 
		// 判断业务名称是否为空
		$("#errInfoMsg").find("span").remove();
		//var msg = $("<span>").css("color", "red").text("业务名称不能为空，请输入业务名称！");
		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmcbnwk"));
		
		$("#errInfoMsg").append(msg);
	} 
	else if (serName.length > 20) 
	{ 
		// 判断业务名称的长度
		$("#errInfoMsg").find("span").remove();
		//var msg = $("<span>").css("color", "red").text("你当前业务名称长度为:【" + serName.length + "】超出限制为【20】的长度！");
		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ndqywmccdw")+ serName.length +getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ccxzw20dcd"));
		$("#errInfoMsg").append(msg);
		
	} 
	else if (serType == "") 
	{ 
		// 判断业务类型是否为空
		serType = 2;
	} 
	else if (spUserid == "") 
	{ 
		// 判断SP的ID是否为空
		$("#errInfoMsg").find("span").remove();
		//var msg = $("<span>").css("color", "red").text("SP账号不能为空！");
		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_spzhbnwk"));
		$("#errInfoMsg").append(msg);

	}
	else if (comments.length > 32) 
	{ 
		// 判断业务描述的长度
		$("#errInfoMsg").find("span").remove();
		//var msg = $("<span>").css("color", "red").text("你当前业务描述长度为【" + comments.length + "】大于36,请重新输入业务描述！");
		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ndqywmscdw") + comments.length +getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dy36"));
		$("#errInfoMsg").append(msg);

	} 
	else 
	{
		//if (confirm("确定提交吗？")) 
		if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) 
		{
			$(window.parent.document).find("#serEditok").attr("disabled","disabled");
			$('#serForm').attr("action",$('#serForm').attr('action')+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val());
			
			document.forms["serForm"].submit();
		}
	}
}

// 提交表单之前在一次判断表单
function formSubmitForAdd() 
{
	var serName = $("#serName").val(); // 获取业务名称
	serName = serName.replace(/\s+/g, ""); // 清除业务名称所有空格
	var comments = $("#comments").val(); // 获取业务描述
	comments = comments.replace(/\s+/g, ""); // 清除业务描述中所有空格
	var spPwd = $("#password").val(); // 获取账户密码
	var runState = $("input[name='runState']:checked").val(); // 获取运行状态
	var spUserid = $("#spUser option:selected").text(); // 获取SP的ID
	spUserid = spUserid.replace(/\s+/g, ""); // 清除空格
	//var staffName = $("#spUser option:selected").val(); // 获取SP名称
	//staffName = staffName.replace(/\s+/g, ""); // 清除空格
	var serType = $("#serType").val(); // 获取业务类型
	if ($.trim(serName) == "") 
	{ 
		// 判断业务名称是否为空
		//alert("业务名称不能为空，请输入业务名称！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmcbnwk"));
		return false;
	}
	//else if(!controlInput(serName,"业务名称只能由汉字、英文字母、数字、下划线组成！",true))
	else if(!controlInput(serName,getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmczyhz"),true))
	{
		return;
	}
	else if (serName.length > 20) 
	{ 
		// 判断业务名称的长度
		//alert("你当前业务名称长度为:【" + serName.length + "】超出限制为【20】的长度！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ndqywmccdw") + serName.length +getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ccxzw20dcd"));
		return false;
	} 
	else if (serType == "") 
	{ 
		// 判断业务类型是否为空
		serType = 2;
	} 
	else if (spUserid == "") 
	{ 
		// 判断SP的ID是否为空
		//alert("SP账号不能为空！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_spzhbnwk"));
		return false;
	}
	else if (comments.length > 32) 
	{ 
		// 判断业务描述的长度
		//alert("你当前业务描述长度为【" + comments.length + "】大于36,请重新输入业务描述！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ndqywmscdw") + comments.length + getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dy36"));
		return false;
	} 
	else 
	{
		
			$('#serForm').attr("action",$('#serForm').attr('action')+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val());
			
			document.forms["serForm"].submit();
		
	}
}

// 检查描述的长度是否超过限制
function checkComments() {
	$("#comments").parent().find("span").remove();
	var comments = $("#comments").val();	
	comments = comments.replace(/\s+/g, ""); // 清除所有空格
	if (comments.length > 32) {
		//var msg = $("<span>").css("color", "red").text(
		//		"你当前业务描述长度为:【" + comments.length + "】超出限制为【36】的长度，请重新输入业务描述！");
		var msg = $("<span>").css("color", "red").text(
				getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ndqywmscdw") + comments.length + getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dy36"));
		$("#comments").parent().append(msg);
	}
}

function getStaffname() {
	/*$("#spUser").parent().find("span").remove();

	var spUser = $("#spUser").val();
	if ($('#serType').val() == 1) {
		$.ajax( {
			type : "POST",
			url : docUrl + "/eng_mtService.htm",
			data : {
				userid : spUser,
				hidOpType : "checkUserId"
			},
			success : function(data) {
				if (data == "false") { // 如果不正确就显示提示
					$("#spUser").parent().find("span").remove();
					var msg = $("<span>").css("color", "red").text(
							"该账户尚未配置上行URL地址，请重新选择！");
					$("#spUser").parent().append(msg);
				} else {
					$("#spUser").parent().find("span").remove();
					$('#staffName')
							.val(
									$('#spUser').find(" > option[selected]")
											.attr("id"));
				}
			}
		});
	} else {
		$('#staffName')
				.val($('#spUser').find(" > option[selected]").attr("id"));
	}*/
	$("#spUser").load(
			"sms_sendSMS.htm?method=getBindUserId",
			{busCode:$("#busCode").val()}
	);
}

// 检查输入账户密码
function checkPassword() {
	$("#password").parent().find("span").remove();
	var spPwd = $("#password").val(); // 从表单中获取账户密码的值
	if ($('#hidOpType').val() == "edit" && spPwd == "") {
		spPwd = $("#curPass").val();
		$("#password").val(spPwd);
	}
	if (spPwd == "") { // 判断是否为空
		//var msg = $("<span>").css("color", "red").text("密码不能为空，请输入密码！");
		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mmbnwk"));
		$("#password").parent().append(msg);
	} else if (!($('#hidOpType').val() == "edit" && spPwd == $('#curPass')
			.val())) {
		// 判断输入的发送账号和账号密码是否正确
		var spUserid = $("#spUser option:selected").text(); // 获取发送账号的值
		spUserid = spUserid.replace(/\s+/g, ""); // 清除空格
		$.ajax( {
			type : "POST",
			url : docUrl + "/account/checkUser.htm",
			data : {
				userid : spUserid,
				pass : spPwd,
				opType : "checkPass"
			},
			success : function(data) {
				if (data == "false") { // 如果不正确就显示提示
					$("#password").parent().find("span").remove();
					//var msg = $("<span>").css("color", "red").text(
					//		"输入的密码有错误，请重新输入密码！");
					var msg = $("<span>").css("color", "red").text(
							getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_srdmmycw"));
					$("#password").parent().append(msg);
				} else {
					//var msg = $("<span>").css("color", "green").text("密码正确！");
					var msg = $("<span>").css("color", "green").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mmzq"));
					$("#password").parent().append(msg);
				}
			}
		});
	}
}

// 检查指令代码是否被用
function checkOrderCode() {
	$("#orderCode").parent().find("span").remove();
	var hidOpType = $('#hidOpType').val();
	var orderCode = $("#orderCode").val(); // 从表单中获取账户密码的值

	if (orderCode == "") { // 判断是否为空
		//var msg = $("<span>").css("color", "red").text("指令代码不能为空，请输入指令代码！");
		var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zldmbnwk"));
		$("#orderCode").parent().append(msg);
	} else {
		if (hidOpType == "add" || hidOpType == "edit"
				&& orderCode != $('#curCode').val()) {
			// 判断输入的发送账号和账号密码是否正确
			$.ajax( {
				type : "POST",
				url : docUrl + "/eng_mtService.htm",
				data : {
					orderCode : orderCode,
					hidOpType : "checkOrderCode"
				},
				success : function(data) {
					if (data == "true") { // 如果不正确就显示提示
						$("#orderCode").parent().find("span").remove();
						//var msg = $("<span>").css("color", "red").text(
						//		"指令代码已存在，请重新输入！");
						var msg = $("<span>").css("color", "red").text(
								getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zddmycz"));
						$("#orderCode").parent().append(msg);
					} else if (data == "error") {
						//var msg = $("<span>").css("color", "green").text(
						//		"指令代码验证重复失败！");
						var msg = $("<span>").css("color", "green").text(
								getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zddmyzcfsb"));
						$("#orderCode").parent().append(msg);
					}
				}
			});
		}
	}
}

function getUserDate() {
	/*var ownerUser = $.trim($("#ownerList").attr("value"));
	var hidvalue = $("#hidOpType").attr("value");
	if ($('#serType').val() != 1) {
		var accId = $("#accId").attr("value");
		$.post(docUrl + "/eng_mtService.htm", {
			ownerUser : ownerUser,
			method : "getUserDataList",
			accId : accId
		}, function(data) {
			if (data != null && data != "error") {
				$("#spUser").empty();
				$("#spUser").html(data);
			}
		});
	}*/

}
