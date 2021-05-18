	$(document).ready(function() {
		$(".mune_show > ul > li > a").click(function() {
			$(".higehLisght").removeClass("higehLisght");
			$(this).addClass("higehLisght");
		});
		var lastShowMn = null;
		var span_bg = null;
		$(".mune_show").each(

		function(index) {
			$(this).click(

			function() {
				var ele = $(this);
				if (ele.children(".mune_hidden").is(':visible')) return false;
				if (lastShowMn != null) {
					lastShowMn.css("display", "none");
				}
				lastShowMn = ele.children(".mune_hidden");
				span_bg = ele;
				lastShowMn.show();
				$(".open").removeClass("open");
				$(this).removeClass("li_a_hover");
				$(this).addClass("open");
			});
		});
		$(".mune_show > ul > li > a").hover(function() {
			$(this).addClass("li_a_hover");
		}, function() {
			$(this).removeClass("li_a_hover");
		});
		$(".mune_show").hover(function() {
			if ($(this).children(".mune_hidden").is(":hidden")) {
				$(this).addClass("li_a_hover");
			}
		}, function() {
			$(this).removeClass("li_a_hover");
		});
		getLoginInfo(".logininfo");
			//如果未打开标签页
			if($(window.parent.document).find("#topLink .cursel").length<1)
			{
				if("null"!=openMenuCode){
					var openMenu =openMenuCode;
					showTabMenClk(openMenu);
					$("a[id='ak"+openMenu+"']").trigger("click");
				}else{
					$("#mod"+selPriMenus).addClass("block");
					$(".mune_show:visible > ul > li > a").eq(0).trigger("click");
				}
				
			}else
			{
				//将当前的菜单显示为对应的标签
				showTabMenClk($(window.parent.document).find("#topLink .cursel").attr("menuCode"));
			}
	})

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

	function showTabMenClk(menucode) {
		$(".block").removeClass("block");
		$(".higehLisght").removeClass("higehLisght");
		$("#ak" + menucode).addClass("higehLisght");
		$("#ak" + menucode).parents(".mune_show").trigger("click");
		$("#ak" + menucode).parents(".mune_show").trigger("click");
		$("#ak" + menucode).parents("table").addClass("block");
	}

	function doOpen(url, menucode, menuname) {
		try {
			
			$(this).css("background-color", "#FFFFFF");
			if (url.indexOf("http://") > 0) {
				url = url.substr(1);
			} else if (url.indexOf(".jsp") > 0) {
				var menuStr = empRoot;
				var menuCode = "," + url.substring(url.indexOf("/") + 1, url.indexOf("_"));
				var modeUrl = menuStr.substr(menuStr.indexOf(menuCode) + menuCode.length);
				modeUrl = modeUrl.substring(1, modeUrl.indexOf(","));
				url = path + modeUrl + url;
			} else {
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
			window.parent.addTabMenu(url + conditionUrl, menucode, menuname);

		} catch (error) {}
	}