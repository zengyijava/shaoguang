
$(function() {		
	$.fn.manhuaInputLetter = function(options) {
		var defaults = {			
			len : 200,
			showId : "show",
			isShow:true,
			showNum:0
		};
		var options = $.extend(defaults,options);	
		var $input = $(this);		
		var $show = $("#"+options.showId);
		if(options.isShow){
			$show.html(options.showNum);
		}
		$input.live("keydown keyup",function(e){						
		  	var content =$.trim($(this).val());
			var length = content.length;
			var result = options.len - length;
			if (result >= 0){
				$show.html(length);
			}else{
				$(this).val(content.substring(0,options.len))
			}
			contentlen($(this), 1);
		});	
		$input.live("blur",function(e){						
		  	var content =$.trim($(this).val());
			var length = content.length;
			var result = options.len - length;
			if (result >= 0){
				$show.html(length);
			}else{
				$(this).val(content.substring(0,options.len))
			}
			contentlen($(this), 2);
		});	
	}	
});