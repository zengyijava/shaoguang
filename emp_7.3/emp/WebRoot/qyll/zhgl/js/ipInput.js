(function($)  
{  
    $.fn.ipinput = function()  
    {  
        // 输入框绑定键盘按下弹起事件  
        $('#ipOne').keyup(function(event)  
        {  
        	// 当前输入的键盘值  
            var code = event.keyCode; 
            var one = $('#ipOne').val(); 
            if((code < 48 && 8 != code && 37 != code && 39 != code && 32 != code )   
                    || (code > 57 && code < 96)   
                    || (code > 105 && 110 != code && 190 != code)){
            	$('#ipOne').val("");
            	return;
            } 
            if(one.length == 3|| code == 39){
            	$('#ipTwo').focus();
            }
        });  
        $('#ipTwo').keyup(function(event)  
            {  
            	// 当前输入的键盘值  
                var code = event.keyCode; 
                var ipTwo = document.getElementById("ipTwo").value;
                if((code < 48 && 8 != code && 37 != code && 39 != code && 32 != code )   
                        || (code > 57 && code < 96)   
                        || (code > 105 && 110 != code && 190 != code)){
                	$('#ipTwo').val("");
                	return;
                } 
                if(ipTwo.length == 3 || code == 39){
                	$('#ipThree').focus();
                }
                if(code==37||(ipTwo.length==0&&code==8)){
                	$('#ipOne').focus();
                }
            }); 
        $('#ipThree').keyup(function(event)  
        	{  
        	// 当前输入的键盘值  
        	var code = event.keyCode; 
        	var ipThree = document.getElementById("ipThree").value;
        	 if((code < 48 && 8 != code && 37 != code && 39 != code && 32 != code )   
                     || (code > 57 && code < 96)   
                     || (code > 105 && 110 != code && 190 != code)){
        		$('#ipThree').val("");
        		return;
        	} 
        	if(ipThree.length == 3|| code == 39){
        		$('#ipFour').focus();
        	}
        	if(code==37||(ipThree.length==0&&code==8)){
        		$('#ipTwo').focus();
        	}
		}); 
        $('#ipFour').keyup(function(event)  
            {  
        	// 当前输入的键盘值  
        	var code = event.keyCode; 
        	if((code < 48 && 8 != code && 37 != code && 39 != code && 32 != code )   
                     || (code > 57 && code < 96)   
                     || (code > 105 && 110 != code && 190 != code)){
        		$('#ipFour').val("");
        		return;
        	} 
        	if(code==37||(ipFour.length==0&&code==8)){
        		$('#ipThree').focus();
        	}
    	}); 
        // 输入框失去焦点事件  
        $('.ipinput_input').blur(function()  
        {  
    	  var one = $.trim($('#ipOne').val()); 
          var two = $.trim($('#ipTwo').val());  
          var three = $.trim($('#ipThree').val());  
          var four = $.trim($('#ipFour').val()); 
          // 如果四个框都有值则赋值给隐藏框  
          if(!isEmpty(one) && !isEmpty(two) && !isEmpty(three) && !isEmpty(four))  
          {  
              var ip = one + "." + two + "." + three + "." + four;
              var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
              if(!reg.test(ip)){
            	  $('#ipFour').focus();
            	  $("#ipError").css('color','red');
            	  return;
              }else{
            	  $("#ipError").css('color','#cccccc');
              }
          }
              
        });
        // 判断参数是否为空  
        var isEmpty = function(obj)  
        {  
            if(null == obj)  
            {  
                return true;  
            }  
            else if(undefined == obj)  
            {  
                return true;  
            }  
            else if("" == obj)  
            {  
                return true;  
            }  
            else  
            {  
                return false;  
            }  
        }; 
        $('#ipOne').focus(function(){
        	$("#ipError").css('color','#cccccc');
        });
    } 
})(jQuery);  
function clearDiv(){
	$(".ipinput_input").val("");
	$(".ipinput_input:last").val("x");
	$('#ipOne').focus();
}
