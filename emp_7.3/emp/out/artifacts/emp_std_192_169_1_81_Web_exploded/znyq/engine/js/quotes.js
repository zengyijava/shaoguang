$(document).ready(function(){
	$("input:text").bind("blur keyup",function(){
		var value=$(this).val();
		if(/['"]/g.test(value)){
			value=value.replace(new RegExp("\"","gm"), "")
			.replace(new RegExp("\'","gm"), "");
			$(this).val(value);
		}
	});   
});
