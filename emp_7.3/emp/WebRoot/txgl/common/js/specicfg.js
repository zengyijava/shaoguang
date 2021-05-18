/**
 * 2014-05-13 去掉托管版 登录首页的个性化
 */
$(document).ready(function(){
	//判断是否单企业用户
	var corpType=$("#CORPTYPE").val();
	var path=$("#path").val();
	var displayType,dispContent,loginLogo,bgImg;
	dispContent="111000000";
	//多企业
	if(corpType!=0){
		//去掉多企业的处理
	}else{
		
		$.post(path+'/loginImg.login',{
			method:'getCfgInfo'
		},function(data,textStatus){
			if(textStatus=='success'){
				data = eval("("+data+")");
				dispContent=data.dispContent;
				bgImg=data.bgImg;
				loginLogo=data.loginLogo;
				if(dispContent.length>=8){
					if(dispContent.charAt(0)=='0'){
						$("#gypt").hide();
						$("#gypt").next().hide();
					}
					if(dispContent.charAt(1)=='0'){
						$("#sccy").hide();
						$("#sccy").prev().hide();
					}
					if(dispContent.charAt(2)=='0'){
						$("#cright").hide();
					}
				}
				if(bgImg.length>0){
					$(".user_defined").find("img").attr('src',bgImg).attr('width',$(".banner_area")[0].offsetWidth);
					$(".user_defined").show().nextAll().hide();	
				}
				if(loginLogo.length>0){
					$("#login_logo").attr('src',loginLogo);
				}
			}
		});
		
	}
})