$(document).ready(function(){
	$(":text").bind('keyup',function(){
		var value=$(this).val();
		if(/[<>''""]/.test(value)){
			value=value.replace(/[<>''""]/g,'');
			$(this).val(value);
		}
		
	}).bind('blur',function(){
		var value=$(this).val();
		if(/[ <>''""]/.test(value)){
			//过滤其他特殊字符 去除字符两边的空格
			value=$.trim(value.replace(/[<>''""]/g,''));
			$(this).val(value);
		}
		
	});
	$(".int").bind('keyup blur',function(){
		var value=$(this).val();
		if(/[^\d]/.test(value)){
			value=value.replace(/[^\d]/g,'');
			$(this).val(value);
		}
	});
	$(".ip").bind('keyup blur',function(){
		var value=$(this).val();
		if(/[^\d^\.]/.test(value)){
			value=value.replace(/[^\d^\.]/g,'');
			$(this).val(value);
		}
	});
	$("#monphone").css({
		'height':'24px',
		'line-height': '16px',
		'vertical-align': 'middle',
		'margin-bottom': '2px',
		'margin-top': '2px',
		'resize': 'none',
		'overflow':'hidden'	
	});
	$("#monphone").bind('keydown keyup focus',function(){
		//处理内容
		subText($(this));
		//处理高度
		$(this).height('24px');
		if($(this)[0].scrollHeight>24){
			$(this).height($(this)[0].scrollHeight);
		}
		if($("#dialog_flag").length>0){
			var $dialog = $(this).parents("div");
			var c = $dialog.dialog('option','def_height'); 
			$dialog.dialog('option','height',c+$(this).height()-24);
		}
	});	
	//失焦事件只处理内容 不处理高度
	$("#monphone").bind('blur',function(){
		subText($(this));
	});	
	$("#monphone").keydown();
});
function subText($obj){
	var value=$obj.val();
	if(/[^\d\,\+]/.test(value)){
		value=value.replace(/[^\d\,\+]/g,'');
		$obj.val(value);
	}
	if(value.length>119){
		$obj.val(value.substr(0,219));
	}
}
function validatePhones(obj){
	var phone = obj.val();
	if(phone.length>0){
		var tip = obj.parent().prev().text().replace(/[:： ]/g,'');
		var reg=/^1[3458][0-9]{9}(\,1[3458][0-9]{9}){0,9}$/;
		if(phone.replaceAll(",","").length<=6||/[^\d\,\+]/.test(phone)){
			isContinue = false;
			alert(tip+getJsLocaleMessage("ptjk","ptjk_wljk_web_9"));
			return false;
		}else{
			//处理+号
			var phoneStr=phone.replaceAll("\\+","s");
			var p = phone.split(',');
			var msg = '';
			if(p.length>10)
			{
				alert(tip+getJsLocaleMessage("ptjk","ptjk_wljk_web_10"))
				return false
			}
			for(var i=0;i<p.length;i++){
				if(!checkPhone(p[i]))
				{
					alert(tip+getJsLocaleMessage("ptjk","ptjk_wljk_web_9"));
					return false;
				}
				var pp=p[i].replaceAll("\\+","s");
				//var matchs = phoneStr.match(new RegExp(pp+",|,"+pp+"$",'g'));
				if(msg.indexOf(p[i])==-1){
					var countRep=0;
					for(var j=0;j<p.length;j++)
					{
						if(pp==p[j].replaceAll("\\+","s"))
						{
							countRep++;
						}
						if(countRep>1)
						{
							if(msg.length>0){
								msg+="，";
							}
							msg+=p[i];
						}
					}
				}
			}
			if(msg.length>0){
				msg=msg.replaceAll("s","\+");
				alert(tip+msg+getJsLocaleMessage("ptjk","ptjk_wljk_web_11"));
				return false;
			}
		}
	}
	return true;
}

//验证邮箱是否有效
function checkEmail(email_str)
{
    email_str = email_str || '';
    if(email_str.length > 128)
    {
        return false;
    }
    var arr = [ "ac", "com", "net", "org", "edu", "gov", "mil", "ac\.cn",
        "com\.cn", "net\.cn", "org\.cn", "edu\.cn" ];
    var temp_arr = arr.join("|");
    // reg
    var reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
    var reg = new RegExp(reg_str);
    if (reg.test(email_str)) {
        return true;
    }else{
        return false;
    }
}