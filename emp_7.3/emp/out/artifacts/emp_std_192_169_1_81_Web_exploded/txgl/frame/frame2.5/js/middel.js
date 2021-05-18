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
   		alert("没有集成相应模块，无法打开新的标签");
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
