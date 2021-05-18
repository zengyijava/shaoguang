$(document).ready(function(){
	 //输入时过滤开头结尾空格
	 $("input:not(:file)").keyup(function(e){
		 var val=$(this).val();
		 var reg=/^\s+/g;
		 if(reg.test(val)){
			$(this).val($(this).val().replace(reg,''));
		 }
		 }).blur(function(){
			$(this).val($.trim($(this).val()));
		}); 
	 /*$(".reply_name").live('keyup blur',function(){
		 secapp(this,/[<'">]/g);
	 })*/
	 $(document).delegate('.reply_name', 'keyup blur', function() { secapp(this,/[<'">]/g); });
});

function secapp(obj,reg){
	var val=$(obj).val();
	if(reg.test(val)){
		$(obj).val($(obj).val().replace(reg,''));
		
	}
}