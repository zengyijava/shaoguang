(function($){
	$.fn.sel = function(opt,callback){

		var self = this;
		opt = $.extend({
			width:500,
			height:600
		},opt||{});
		$(self).addClass('zs-wrap');
		$(self).css('overflow','hidden');
		$(self).width(opt.width);
		$(self).height(opt.height);
		var selects = $(self).children('select');
		var l_sel = selects.first();
		var r_sel = selects.last();
		var size = r_sel.children().size();
		var buttons = $(self).children(':button');
		function moveToRight(){
			var sels = l_sel.children('option:selected');
			if(sels.size()==0){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_27"));}
			$.each(sels,function(){
				r_sel.append($(this).clone());
				$(this).remove();
				size++;
			})
			callback(size);
		}
		function moveToLeft(){
			var sels = r_sel.children('option:selected');
			if(sels.size()==0){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_27"));}
			$.each(sels,function(){
				l_sel.append($(this).clone());
				$(this).remove();
				size--;
			})
			callback(size);
		}
		buttons.first().click(moveToRight);
		l_sel.dblclick(moveToRight);
		buttons.last().click(moveToLeft);
		r_sel.dblclick(moveToLeft);
		var width = opt.width/2-68;
		var height = opt.height - 40;
		l_sel.width(width);l_sel.height(height);
		r_sel.width(width);r_sel.height(height);
		l_sel.wrap('<div class="zs-left">');
		r_sel.wrap('<div class="zs-right">');
		buttons.wrapAll('<div class="zs-button">');
		$('.zs-button').css('paddingTop',(height-120)/2+'px');
		callback(size);
	}
})(window.jQuery);
