//兼容Ie9窗口高度显示不正常
var isChange = 0;
function setTableHeight()
{
	var liheight =  $("#fixedDiv").height();
	if($("#mainTable").height()<liheight-86)
	{
		isChange = 1;
	}
	setTableHeight2();
}
var reSizeCount=0;
function setTableHeight2()
{
	if(isChange == 1)
	{
		var liheight =  $("#fixedDiv").height();
		$("#mainTable").css("height",(liheight-84)+"px");
		$("#leftIframe").css("height",(liheight-84)+"px");
		$("#menuFrame").find("> iframe").css("height",(liheight-117)+"px");
	}
	reSizeCount=0;
	if(reSizeCount<1)
	{
		reSizeCount=reSizeCount+1;
	}else
	{
		reSizeCount=0;
		return;
	}
	var bodywidth = $("body").width();
	var newTabCount = Math.floor((bodywidth - 210)/112);
	changeTopMenu(newTabCount);
	setTimeout("reSizeCount=0;",400);
}