$(document).ready(function(){
	$(".dis").qtip(
			{
				   content: getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_1"),
				   show: 'mouseover',
				   hide: 'mouseout',
				   position: {  
		                corner: {  
		                    target: 'topLeft',  
		                    tooltip: 'bottomLeft'  
		                } } 
				});
	$(".dis").bind('keyup blur',function(){
		var value=$(this).val();
		if(/[^\d^-]/.test(value)){
			value=value.replace(/[^\d^-]/g,'');
			$(this).val(value);
		}
		
	});
})