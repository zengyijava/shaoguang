function getLoginInfo(ids) {
	var $pa = $(window.parent.parent.document);
	var pahtm = $pa.find("#loginparams").html();
	$(ids).append(pahtm);
}

function showMod(selMod) {
	$(".block").removeClass("block");
	$("#mod" + selMod).addClass("block");
	$("#mod" + selMod).find(".mune_show").eq(0).trigger("click");
}
$(document).ready(

function() {
	getLoginInfo(".logininfo");
	//打开首页
	doOpen("/thirdMenu.htm?method=getWarm", "Index", "首页");
	$(".mune_show > ul > li > a").click(function() {
		$(".higehLisght").removeClass("higehLisght");
		$(".higehLisght_parent").removeClass("higehLisght_parent");
		$(this).addClass("higehLisght");

		$(this).parent().addClass('higehLisght_parent')
	});
	var lastShowMn = null;
	var span_bg = null;
	$(".mune_show").each(

	function(index) {
		$(this).click(

		function() {
			var ele = $(this);
			if (ele.attr("id") == "upsc") {
				return false;
			}
			if (ele.children(".mune_hidden").is(':visible')) {
				return false;
			}
			if (lastShowMn != null) {
				lastShowMn.css("display", "none");
			}
			lastShowMn = ele.children(".mune_hidden");
			span_bg = ele;
			lastShowMn.show();
			$(".open").removeClass("open");
			$(this).removeClass("li_a_hover");
			$(this).parent().removeClass("lihover");
			$(this).addClass("open");
		});
	});
	$(".mune_show > ul > li > a").hover(function() {
		$(this).addClass("li_a_hover");
		$(this).parent().addClass("lihover");
	}, function() {
		$(this).removeClass("li_a_hover");
		$(this).parent().removeClass("lihover");
	});
	$(".mune_show").hover(function() {
		if ($(this).find(".mune_hidden").length == 0) {
			$(this).addClass("li_a_hover");
			$(this).parent().addClass("lihover");
		} else if ($(this).children(".mune_hidden").is(":hidden")) {
			$(this).addClass("li_a_hover");
			$(this).parent().addClass("lihover");
		}
	}, function() {
		$(this).removeClass("li_a_hover");
		$(this).parent().removeClass("lihover");
	});
	$("#mod" + selPriMenus).addClass("block");
	$(".mune_show:visible > ul > li > a").eq(0).trigger("click");
})

function showTabMenClk(menucode) {
	$(".block").removeClass("block");
	$(".higehLisght").removeClass("higehLisght");
	$(".higehLisght_parent").removeClass("higehLisght_parent");
	$("#ak" + menucode).addClass("higehLisght");
	$("#ak" + menucode).parent().addClass("higehLisght_parent");
	$("#ak" + menucode).parents(".mune_show").trigger("click");
	$("#ak" + menucode).parents(".mune_show").trigger("click");
	$("#ak" + menucode).parents("table").addClass("block");
	var modid = $("#ak" + menucode).parents("table").attr("id");
	$(window.parent.parent.document).find("#topFrame")[0].contentWindow.showTopMenu(modid);
}

function doOpen(url, menucode, menuname) {
	try {
		$(this).css("background-color", "#FFFFFF");
		if (url.indexOf("http://") > 0) {
			url = url.substr(1);
		} else if (url.indexOf(path + "/") != 0) {
			url = path + url;
		}
		var conditionUrl = "";
		if (url.indexOf("?") > -1) {
			conditionUrl = "&";
		} else {
			conditionUrl = "?";
		}
		$(".logininfo").find(" input").each(function() {
			conditionUrl = conditionUrl + $(this).attr("id") + "=" + $(this).val() + "&";
		});
		conditionUrl = conditionUrl + "timee=" + new Date();
		parent.window.frames['cont'].location.href = url + conditionUrl;
		if(url.indexOf("/mon_mainMon.htm")>0)
		{
			window.parent.parent.openMon();
		}
		else
		{
			window.parent.parent.closeMon();
		}

	} catch (error) {
	}
}

function showMenuTable(idstr) {
	var $par = $(window.parent.document);
	$(".block").removeClass("block");
	$("#" + idstr).addClass("block");
	if (idstr != "modIndex") {
		if(idstr=="mod16")
		{
			//$(".block").find(" ul > li").eq(2).find("ul > li > a").eq(0).trigger("click");
			//$(".block").find(" ul > li > a").eq(0).trigger("click");
			window.parent.parent.openMon();
		}
		$(".block").find(" ul > li > a").eq(0).trigger("click");
		//window.parent.parent.closeMon();
		$par.find(".middle_toggle").show();
	} else {
		$par.find("#frmTitle").show();
		$par.find(".middle_toggle").removeClass("mid_toggle_X");
		$par.find(".middle_toggle").hide();
		doOpen("/thirdMenu.htm?method=getWarm", "Index", "首页");
	}
}

function showgg() {
	$(".open").removeClass("open");
	$("#ggLi").addClass("open");
}

function doUpdatePass() {
	parent.parent.doUpdatePass();
}

function upLoad() {
	if (confirm("是否下载帮助手册？")) {
		uploadFiles('common/file/help.zip', getContextPath);
	}
}