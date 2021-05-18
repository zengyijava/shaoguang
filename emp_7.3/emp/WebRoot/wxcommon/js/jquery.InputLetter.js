/***
 * ����jQuery�����ַ�������
 * ��дʱ�䣺2012��7��18��
 * version:manhuaInputLetter.1.0.js
***/
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
		$input.on({
		    keydown:function(){
		        doCountNum(this,options,$show);
		    },
		    blur:function(){
                doCountNum(this,options,$show);
            },
		    keyup:function(){
                doCountNum(this,options,$show);
            }
		});
	}	
});
function doCountNum(obj,options,$show)
{
    var content =$.trim($(obj).val());
    var length = content.length;
    var result = options.len - length;
    if (result >= 0){
        $show.html(length);
    }else{
        $(this).val(content.substring(0,options.len))
    }
}