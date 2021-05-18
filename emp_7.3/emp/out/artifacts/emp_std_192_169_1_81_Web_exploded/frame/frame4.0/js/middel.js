function switchSysBar($tdObj)
{ 
	$("#frmTitle").toggle();
	$tdObj.toggleClass("mid_toggle_X");
}
function menuhover($menu)
{
	if(!$menu.hasClass())
	{
		$menu.toggleClass("curhover");
	}
}

function openNewTab(menuCode,url)
{
   	var $lf = $("iframe[name='I1']");
	if($lf.contents().find("a[id='ak"+menuCode+"']").length==0)
   	{
   		alert(getJsLocaleMessage("common","common_frame2_middle_2"));
   		return;
   	}
   	$lf[0].contentWindow.showTabMenClk(menuCode);
   
   	$lf.contents().find("a[id='ak"+menuCode+"']").trigger("click");
   	if(url != "")
   	{
   		$("#cont").attr("src",url);
   	}
}

function doSkin(menucode)
{
	var $hcss = $("#cont"+menucode).contents().find("link");
	var skincode = "";
	var forboo = true;
	for(var i = 0;i<$hcss.length && forboo;i=i+1)
	{
		var csshref = $hcss.eq(i).attr("href");
		if(csshref.indexOf("/skin/")>0)
		{
			csshref = csshref.substring(csshref.indexOf("/skin/")+6);
			skincode = csshref.substring(0,csshref.indexOf("/"));
			forboo = false;
		}
	}
	if(skincode != "")
	{
		parent.window.checkMenuSkin(skincode);
	}
}
function loading(){
	$('#load-bg').show();
}
function complete(){
	$('#load-bg').hide();
}
//修改模板共享
function updateShareTemp() {
    var tempId = $("#flowFrame").attr("attrid");
    var optionSize = $(window.frames['flowFrame'].document).find(
        "#right option").size();
    // 设置的机构IDS
    var depidstr = "";
    // 设置的操作员IDS
    var useridstr = "";
    $(window.frames['flowFrame'].document).find("#right option").each(
        function() {
            var id = $(this).val();
            // 1是机构 2是操作员
            var type = $(this).attr("isdeporuser");
            if (type == "2") {
                useridstr = useridstr + id + ",";
            } else if (type == "1") {
                depidstr = depidstr + id + ",";
            }
        });
    var path = $("#path").val();
    //短信模板
    var infoType = $("#templType").val();
    $("#updateShareTemp").attr("disabled", true);
    $.post(path + "/meditor/updateShareTemp", {
        depidstr : depidstr,
        useridstr : useridstr,
        tempid : tempId,
        infotype : infoType
    }, function(returnmsg) {
        $("#updateShareTemp").attr("disabled", false);
        if (returnmsg.indexOf("html") > 0) {
            alert(getJsLocaleMessage("common","common_spgl_shlcgl_1"));
        } else if (returnmsg == "success") {
            alert(getJsLocaleMessage("common","common_cswh_dxmbgl_46"));
            $("#shareTmpDiv").dialog("close");
        } else if (returnmsg == "fail") {
            alert(getJsLocaleMessage("common","common_cswh_dxmbgl_47"));
        } else {
            alert(getJsLocaleMessage("common","common_spgl_shlcgl_3"));
        }
    });
}
// 关闭模板共享窗口
function closeShare() {
    $("#flowFrame").attr("src", "");
    $("#flowFrame").attr("attrid", "");
    $(window.frames['flowFrame'].document).find("#right").empty();
    $("#shareTmpDiv").dialog("close");
}