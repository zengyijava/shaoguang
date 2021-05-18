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
		/*关于平台*/
		$(".hd_link").prepend('<a id="gypt" onclick="showAbout()">'+getJsLocaleMessage("common","common_aboutPlatform")+'</a>');
		$(".hd_link").prepend('<span id="separator">|</span>');
		/*收藏此页*/
		$(".hd_link").prepend('<a id="sccy" onclick="AddFavorite(document.title,document.URL);">'+getJsLocaleMessage("common","common_frame2_login_7")+'</a>');
		$("#cright").html(cright);
		$("#logo").append('<img id="login_logo" src='+logoPath+' alt="EMP">');
	}else{
		$.post(path+'/loginImg.login',{
			method:'getCfgInfo'
		},function(data,textStatus){
			if(textStatus=='success'){
				data = eval("("+data+")");
				dispContent=data.dispContent;
				bgImg=data.bgImg;
				loginLogo=data.loginLogo;
				if(dispContent&&dispContent.length>=8){
					//if(dispContent.charAt(0)=='0'){
						//$("#gypt").hide();
						//$("#gypt").next().hide();
					//}
					//关于平台
					if(dispContent.charAt(0)!='0')
					{
						$(".hd_link").prepend('<a id="gypt" onclick="showAbout()">'+getJsLocaleMessage("common","common_aboutPlatform")+'</a>');
					}
					//if(dispContent.charAt(1)=='0'){
						//$("#sccy").hide();
						//$("#sccy").prev().hide();
 					//}
					//收藏此页
					if(dispContent.charAt(1)!='0')
					{
						if(dispContent.charAt(0)!='0')
						{
							$(".hd_link").prepend('<span>|</span>');
						}
						$(".hd_link").prepend('<a id="sccy" onclick="AddFavorite(document.title,document.URL);">'+getJsLocaleMessage("common","common_frame2_login_7")+'</a>');
					}
					//if(dispContent.charAt(2)=='0'){
					//	$("#cright").hide();
					//}
					if(dispContent.charAt(2)!='0')
					{
						$("#cright").html(cright);
					}
				}
				if(bgImg&&bgImg.length>0){
					$(".user_defined").find("img").attr('src',bgImg).attr('width',$(".banner_area")[0].offsetWidth);
					$(".user_defined").show().nextAll().hide();	
				}
				if(loginLogo&&loginLogo.length>0){
					$(".logo").append('<img id="login_logo" src="'+loginLogo+'" alt="EMP">');
					//$("#login_logo").attr('src',loginLogo);
				}
				else
				{
					$(".logo").append('<img id="login_logo" src="'+logoPath+'" alt="EMP">');
				}
				
			}
		});
		
	}
})