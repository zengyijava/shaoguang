$(document).ready(function(){
	//智能引擎模块上行下行中 sql语句及短信内容均限制在990字以内
	$(".textarea_limit").bind("blur",function(){
		var maxlimit=990;
		var field=$.trim($(this).val());
		if(field.length>maxlimit){
		      field= field.substring(0, maxlimit);
				$(this).val(field);
		}
	}).bind("keyup",function(){
		var maxlimit=990;
		var field=$(this).val();
		if(field.length>maxlimit){
			field= field.substring(0, maxlimit);
			$(this).val(field);
		}
	});
				    
});
