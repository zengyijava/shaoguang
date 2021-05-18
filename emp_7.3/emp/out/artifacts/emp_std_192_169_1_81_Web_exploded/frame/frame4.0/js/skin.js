$(document).ready(function() {
	$(".perSkin").hover(function() {
		$(this).toggleClass("div_bg");
	}, function() {
		$(this).toggleClass("div_bg");
	});
});

function changeSkin2(new_skin, themecode) {
	if (themecode == old_frame) {
		$(".themeSpan").each(function() {
			//	var baim = $(this).css("background-image");
			//	$(this).css("background-image",baim.replace(old_skin,new_skin));
		});
		parent.parent.changeSkin2(new_skin, themecode);
		parent.parent.skinSure();
		old_skin = new_skin;
	} else {
		parent.parent.changeSkin2(new_skin, themecode);
	}
	chooseSkin(new_skin);

}
//选中皮肤

function chooseSkin(skinstr) {
	if ($(".curSkin2:visible").length > 0) {
		$(".curSkin2:visible").removeClass("div_bd");
		$(".curSkin2:visible").removeClass("title_bg");
		$(".curSkin2:visible").removeClass("curSkin2");
	}
	$(".skin_" + skinstr + ":visible").addClass("curSkin2");
	$(".skin_" + skinstr + ":visible").addClass("div_bd");
	$(".skin_" + skinstr + ":visible").addClass("title_bg");
}

function skinSure() {
	parent.parent.skinSure();
}

function skinNoGood() {
	selTheme(old_frame);
	changeSkin2(old_skin, old_frame);
}

function selTheme(themeCode) {
	if (sel_frame == themeCode) {
		return;
	}
	$(".themeSpan").each(function() {
		var thcode = $(this).attr("themeCode");
		var baim = $(this).css("background-image");
		if (themeCode == thcode && sel_frame != thcode) {
			$(this).addClass("fontsel");
			$(".skinDiv[framecode='" + thcode + "']").show();
			$(this).css("background-image", baim.replace(".png", "_sel.png"));
		} else {
			$(this).removeClass("fontsel");
			$(".skinDiv[framecode='" + thcode + "']").hide();
			$(this).css("background-image", baim.replace("_sel.png", ".png"));
		}
	});
	if ($(".curSkin2:visible").length == 0) {
		chooseSkin('default');
		parent.parent.changeSkin2('default', themeCode);
	}
	sel_frame = themeCode;
	parent.parent.getThemeSkin2(themeCode);
}