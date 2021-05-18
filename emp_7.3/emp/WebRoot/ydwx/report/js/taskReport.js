function modify(t, state) {
	if (state == "1") {
		$('#modify').dialog("option", "title", getJsLocaleMessage("ydwx","ydwx_wxfschx_34"));
	} else if (state == "2") {
		$('#modify').dialog("option", "title", getJsLocaleMessage("ydwx","ydwx_wxfschx_35"));
	}
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
}


