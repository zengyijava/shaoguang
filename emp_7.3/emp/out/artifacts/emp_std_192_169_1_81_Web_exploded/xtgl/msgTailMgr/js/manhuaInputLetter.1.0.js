$(function() {      
    $.fn.manhuaInputLetter=function(options) {
      var defaults = {            
            len : 200,
            showId : "show"
      };
      var options = $.extend(defaults,options);    
      var $input = $(this);      
      var $show = $("#"+options.showId);
      $input.live("keydown keyup blur",function(e){                        
            var content =$(this).val();
            var length = content.length;
            if (length <= 500){
                $show.html(length);
            }else{
                $(this).val(content.substring(0,options.len))
            }
      });    
    }    
});