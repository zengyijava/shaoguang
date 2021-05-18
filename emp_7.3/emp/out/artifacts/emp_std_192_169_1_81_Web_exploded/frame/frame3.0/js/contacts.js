function blurHide(ep) {
	ep.val($.trim(ep.val()));
	if (ep.val() == "") {
		ep.next("div").css("display", "block");
	} else {
		ep.next("div").css("display", "none");
	}
	if (ep.val().length > 200) {
		ep.val(ep.val().substring(0, 200));
	}
}

function numberControl(va) {
	var pat = /^\d*$/;
	if (!pat.test(va.val())) {
		va.val(va.val().replace(/[^\d]/g, ''));
	}
}

function focusShow(ep) {
	ep.next("div").css("display", "none");
}

function countRestrict(ep) {
	if (ep.val().length > 200) {
		ep.val(ep.val().substring(0, 200));
	}
}

function focusShow_label(ep) {
	ep.hide().prev().focus();
}

function complainSure() {
	$("#tipSpan").remove();
	var ctitle = $("#ctitle").val();
	var ctype = $("#ctype").val();
	var ccontent = $("#ccontent").val();
	var cemail = $("#cemail").val();
	var cphone = $("#cphone").val();
	var lguserid = window.parent.$("#lguserid").val();
	if (cemail != "") {
		//对电子邮箱的验证
		var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if (!myreg.test(cemail)) {
			/*'邮箱格式不正确'*/
			alert(getJsLocaleMessage("common","common_frame2_contacts_23"));
			$("#cemail").focus();
			return;
		}
	}
	if (ctype == 2 || ctype == 3) {
		if (cphone == "" && cemail == "") {
			/*"请输入邮箱或电话，方便我们联系您！"*/
			alert(getJsLocaleMessage("common","common_frame2_contacts_24"));
			return;
		}
	}
	if ($.trim(ccontent).length < 5) {
		/*描述至少输入5个字符*/
		alert(getJsLocaleMessage("common","common_frame2_contacts_25"));
		return;
	}
	$("#com_but_ok").attr("disabled", "disabled");
	$("#com_but_no").attr("disabled", "disabled");

	$.post(emppatch + "/complain.htm", {
		method: "find",
		ctitle: ctitle,
		ctype: ctype,
		ccontent: ccontent,
		cemail: cemail,
		cphone: cphone,
		lguserid: lguserid,
		isAsync: "yes"
	}, function(result) {
		if (result == "true") {
			if (ctype == 1) {
				/*提交成功！感谢您的建议，我们会根据您的建议优化产品和服务！*/
				alert(getJsLocaleMessage("common","common_frame2_contacts_26"));
			} else {
				/*提交成功！我们将尽快给您回复，谢谢！*/
				alert(getJsLocaleMessage("common","common_frame2_contacts_27"));
			}
			window.parent.$("#complainDiv").dialog("close");
		} else if (result == "false") {
			$("#complain").append("<span id='tipSpan' style='color:red'>"+getJsLocaleMessage("common","common_frame2_contacts_28")+"<br/>"+getJsLocaleMessage("common","common_frame2_contacts_29")+"<a onclick='javascript:copyToClipboard()' id='source'>http://61.145.229.29:8015/mboss/sug.aspx?version="+version+"</a></span>");
		}
		$("#com_but_ok").attr("disabled", "");
		$("#com_but_no").attr("disabled", "");
	});

}
//取消投诉

function complainNoGood() {
	window.parent.$("#complainDiv").dialog("close");

}