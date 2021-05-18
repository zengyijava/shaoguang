function doAdd() {
	$('#areaname').val('');
	$('#areacode').val('');
	$("#addAreaCode").css("display", "block");
	$('#addAreaCode').dialog( {
		autoOpen : false,
		height : 220,
		width : 430,
		resizable : false,
		modal : true
	});
	$('#addAreaCode').dialog('open');
}

//取消
function btcancel() {
	if (!$('#addAreaCode').is(":hidden")) {
		$('#addAreaCode').dialog('close');
	}
}

function toAdd() {
	var areaname = $.trim($('#areaname').val());
	var areacode = $.trim($('#areacode').val());
	if(areaname == "" || areaname.length == 0)
	{
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_8"));
		$("#areaname").focus();
		return;
	}
	if(!specialString(areaname))
	{
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_9"));
		$("#areaname").focus();
		return;
	}
	if(areacode == "" || areacode.length == 0)
	{
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_10"));
		$("#areacode").focus();
		return;
	}
	if((areacode.substring(0,1) == "+" && areacode.length > 1) || (areacode.length > 2 && areacode.substring(0,2) =="00"))
	{
		$.post("wy_areaCodeManage.htm?method=toAdd", {
			areaname : areaname,
			areacode : areacode
		}, function(result) {
			if (result == "success") {
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_11"));
				window.location.href = "wy_areaCodeManage.htm?method=find";
			} else if(result == "isExist"){
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_12"));
			}
			else {
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_13"));
				$('#addAreaCode').dialog('close');
			}
		});
		}
	else
	{
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_14"));
		$("#areacode").focus();
		return;
	}
}

function toDel(id) {
	if (!confirm(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_15"))) {
		return;
	}
	$.post("wy_areaCodeManage.htm?method=toDel", {
		id : id
	}, function(result) {
		if (result == "faild") {
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_16"));
		} else {
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_17"));
			window.location.href = "wy_areaCodeManage.htm?method=find";
		}
	});
}

//checkbox全选
function checkAlls(e, str) {
	var a = document.getElementsByName(str);
	var n = a.length;
	for ( var i = 0; i < n; i = i + 1)
		a[i].checked = e.checked;
}

function delCheckId() {
	var selected = document.getElementsByName("delareaCodeId");
	var n = 0; //统计勾选中的个数
	var id = "";
	for (i = 0; i < selected.length; i = i + 1) {
		if (selected[i].checked == true) {
			id = id + selected[i].value;
			id = id + ","
			n = n + 1;
		}
	}
	id = id.substring(0, id.lastIndexOf(','));
	if (n < 1) {
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_18"));
		return;
	}
	if (confirm(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_19"))) {
		$.post("wy_areaCodeManage.htm?method=toDel", {
			id : id
		}, function(result) {
			if (result == "faild") {
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_20"));
			} else {
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_20") + result + getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_22"));
				window.location.href = "wy_areaCodeManage.htm?method=find";
			}
		});
	}
}