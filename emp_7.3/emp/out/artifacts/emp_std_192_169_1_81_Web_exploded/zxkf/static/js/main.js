requirejs.config({
	paths: {
        jquery: 'jquery-1.10.2',
        nanoscroller:'jquery.nanoscroller',
        artDialog:path+'/common/widget/artDialog/artDialog',
        iframeTools:path+'/common/widget/artDialog/iframeTools',
        fancybox:'lib/fancyBox/jquery.fancybox',
        uploadify:'lib/uploadify/jquery.uploadify',
        fileupload:'ajaxfileupload'
    },
    shim:{
    	nanoscroller: ["jquery"],
    }
});
requirejs(['jquery','require','im','EasyChat','nanoscroller'],function($,req,chat,easy){
	$(document).ready(function(){
		EasyChat.init(
			{
				view: { resources: iPath+'/static' },
				type:""
			}
		);
		req(['fancybox'],function(){
			$(".fancybox").fancybox();
		})	
		chat.im.init();
		chat.im.dropdown('.dropdown','.dropdown-menu');
		chat.im.preventDefault();
		
		req(['sendMsg','showOthers'],function(){});
		req(['group'],function(){});
		req(['util'],function(util){
            var u=new util.util();
            u.slideList();
			u.addUserJson(userJson,iPath);
			u.addUserOfAppJson(appUserJson,iPath);
			u.userInfoJson(userInfoJson,iPath);
			u.addGroupJson(groupJson,iPath);
			u.loadUnReadMsg(path);
			//状态监测
			u.checkStatus();
			//开启轮询
            //u.checkMess(0,path,0);
			
        });
		
		/*左菜单切换*/
		$("#im-tmenu").hover(function(){
			var cval = $("#chosedMenu").val();
			if(cval=="wx"){//选中不作效果处理
			}else if(cval=="app"){
				cssHandle("im-mm-bg","im-bg4");
				cssHandle("im-block","im-block-chose");
				cssHandle("im-tmenu","im-tmenu-chose");
			}else if(cval=="custom"){
				cssHandle("im-mm-bg","im-bg1");
				cssHandle("im-block","im-block-chose");
				cssHandle("im-tmenu","im-tmenu-chose");
			}
		},function(){
			var cval = $("#chosedMenu").val();
			if(cval=="wx"){//选中不作效果处理
			}else if(cval=="app"){
				cssHandle("im-mm-bg","im-bg2");
				cssHandle("im-block","im-block-default");
				cssHandle("im-tmenu","im-tmenu-default");
			}else if(cval=="custom"){
				cssHandle("im-mm-bg","im-bg3");
				cssHandle("im-block","im-block-default");
				cssHandle("im-tmenu","im-tmenu-default");
			}
		});
		$("#im-mmenu").hover(function(){
			var cval = $("#chosedMenu").val();
			if(cval=="wx"){
				cssHandle("im-mm-bg","im-bg4");
				cssHandle("im-bm-bg","im-bg1");
				cssHandle("im-mmenu","im-mmenu-chose");
			}else if(cval=="app"){//选中不作效果处理
			}else if(cval=="custom"){
				cssHandle("im-mm-bg","im-bg2");
				cssHandle("im-bm-bg","im-bg4");
				cssHandle("im-mmenu","im-mmenu-chose");
			}
		},function(){
			var cval = $("#chosedMenu").val();
			if(cval=="wx"){
				cssHandle("im-mm-bg","im-bg1");
				cssHandle("im-bm-bg","im-bg3");
				cssHandle("im-mmenu","im-mmenu-default");
			}else if(cval=="app"){//选中不作效果处理
			}else if(cval=="custom"){
				cssHandle("im-mm-bg","im-bg3");
				cssHandle("im-bm-bg","im-bg2");
				cssHandle("im-mmenu","im-mmenu-default");
			}			
		});
		$("#im-bmenu").hover(function(){
			var cval = $("#chosedMenu").val();
			if(cval=="wx"){
				cssHandle("im-bm-bg","im-bg2");
				cssHandle("im-bmenu","im-bmenu-chose");
			}else if(cval=="app"){
				cssHandle("im-bm-bg","im-bg4");
				cssHandle("im-bmenu","im-bmenu-chose");
			}else if(cval=="custom"){//选中不作效果处理
			}
		},function(){
			var cval = $("#chosedMenu").val();
			if(cval=="wx"){
				cssHandle("im-bm-bg","im-bg3");
				cssHandle("im-bmenu","im-bmenu-default");
			}else if(cval=="app"){
				cssHandle("im-bm-bg","im-bg1");
				cssHandle("im-bmenu","im-bmenu-default");
			}else if(cval=="custom"){//选中不作效果处理
			}
		});
		
		$("#im-tmenu .im-menu-icondiv").click(function(){
			$("#cus-group-wx").css("display","block");
			$("#cus-group-app,#cus-group-custom").css("display","none");
			
			$("#chosedMenu").val("wx");
			showCount("#im-mmenu .im-menu-icondiv");
			showCount("#im-bmenu .im-menu-icondiv");
			$(this).find(".count").hide();
			
			cssHandle("im-block","im-block-chose");
			cssHandle("im-mm-bg","im-bg1");
			cssHandle("im-bm-bg","im-bg3");
			cssHandle("im-tmenu","im-tmenu-chose");
			cssHandle("im-mmenu","im-mmenu-default");
			cssHandle("im-bmenu","im-bmenu-default");
			
			initScroller();
		});
		$("#im-mmenu .im-menu-icondiv").click(function(){
			$("#cus-group-wx,#cus-group-custom").css("display","none");
			$("#cus-group-app").css("display","block");
			
			$("#chosedMenu").val("app");
			showCount("#im-tmenu .im-menu-icondiv");
			showCount("#im-bmenu .im-menu-icondiv");
			$(this).find(".count").hide();
			
			cssHandle("im-mm-bg","im-bg2");
			cssHandle("im-bm-bg","im-bg1");
			cssHandle("im-mmenu","im-mmenu-chose");
			cssHandle("im-block","im-block-default");
			cssHandle("im-tmenu","im-tmenu-default");
			cssHandle("im-bmenu","im-bmenu-default");
			
			initScroller();
			
		});
		$("#im-bmenu .im-menu-icondiv").click(function(){
			$("#cus-group-wx,#cus-group-app").css("display","none");
			$("#cus-group-custom").css("display","block");
			
			$("#chosedMenu").val("custom");
			showCount("#im-tmenu .im-menu-icondiv");
			showCount("#im-mmenu .im-menu-icondiv");
			$(this).find(".count").hide();
			
			cssHandle("im-mm-bg","im-bg3");
			cssHandle("im-bm-bg","im-bg2");
			cssHandle("im-block","im-block-default");
			cssHandle("im-tmenu","im-tmenu-default");
			cssHandle("im-mmenu","im-mmenu-default");
			cssHandle("im-bmenu","im-bmenu-chose");
			
			initScroller();
		});
		
		//建议提示处理--开始,EMp未放开非IE的登录限制，故而显注释此判断
		/*var browserStr = userAgent();
		if(browserStr.indexOf("chrome")== -1 && browserStr.indexOf("Firefox")==-1){
			$(".tip-browser").show();
		}
		$(".tip-browser-x").click(function(){
			$(".tip-browser").hide();
		});
		//建议提示处理--结束*/
		
	});

	$(window).resize(function(){
		chat.im.init();
	})
});

//浏览器版本判断
function userAgent(){
    var ua = navigator.userAgent;
    ua = ua.toLowerCase();
    var match = /(webkit)[ \/]([\w.]+)/.exec(ua) ||
    /(opera)(?:.*version)?[ \/]([\w.]+)/.exec(ua) ||
    /(msie) ([\w.]+)/.exec(ua) ||
    !/compatible/.test(ua) && /(mozilla)(?:.*? rv:([\w.]+))?/.exec(ua) ||
    [];
    //match[2]判断版本号
    var browserStr = "";
    switch(match[1]){
    	case "msie":      //ie
    		if (parseInt(match[2]) == 6){    //ie6
    			browserStr = "ie6";
    		}else if (parseInt(match[2]) == 7){    //ie7
    			browserStr = "ie7";
    		}else if (parseInt(match[2]) == 8){    //ie8
    			browserStr = "ie8";
    		}else if (parseInt(match[2]) == 9){    //ie8
    			browserStr = "ie9";
			}else if (parseInt(match[2]) == 10){    //ie8
    			browserStr = "ie10";
			}
    		break;
    	case "webkit":     //safari or chrome
    		browserStr = "safari or chrome";
    		break;
    	case "opera":      //opera
    		browserStr = "opera";
    		break;
    	case "mozilla":    //Firefox
    		browserStr = "Firefox";
    		break;
    	default:    
    		break;
    }
    return browserStr;
}


//左菜单未读消息显示
function showCount(id){
	var countstr = $(id).find(".count");
	if(countstr.html()!="" && countstr.html()-0>0){
		countstr.show();
	}
}
//表情初始化
function initScroller(type){
	$("#cus-group-wx #chat-tab-box,#cus-group-app #chat-tab-box,#cus-group-custom #chat-tab-box").nanoScroller({ preventPageScrolling: true });
	setTimeout(function(){
	    $("#chat-tab-box").nanoScroller();
	}, 300);
}
function cssHandle(id,cssval){
	$("#"+id).removeClass();
	$("#"+id).addClass(cssval);
}

function send()
{
/*
toUser : 发给谁（客户，客服，群组）的Id,
msg :消息内容,
servernum: 服务号,
msgType: 消息类型（text:文本，image图片，目前不知发送语音，但支持接收语音）,
customId: 发送者的客服Id,
pushType: 推送类型（2：客服to客户，3客服to客服，4群组）,
name: 发送者名称,
openId: 发送者所在微信号的openId
*/
    $.post("customChat.htm?method=sendMsg", {
        toUser : toUser,
        msg : tx,
        servernum: servernum,
        msgType: msgType,
        customId: customId,
        pushType: pushType,
        name: customeName,
        openId: openId
    },function(result){
    	
        if(result.indexOf("chat:")==0 && result != "chat:")
        {
            
            result = result.substring(5);
            //result为发送时间
            //显示到对应的窗口中
        }
        
    });
}
function logout() {
    if (confirm("是否退出客服系统？")) {
    	$.post(path + '/customChat.hts?method=logout&aId='+aId+'&userId='+userCustomeId+'&time='+new Date().getTime(),
    			{},function(){
    				window.close();
    			});
    }
}
window.onbeforeunload =function(){
	
	$.post(path + '/customChat.hts?method=logout&aId='+aId+'&userId='+userCustomeId+'&time='+new Date().getTime());
}
var isLogoutAlert = 0;
function doOut()
{
	if(isLogoutAlert==0)
	{
		isLogoutAlert = 1;
		alert('EMP系统登录超时或已退出登录，即将关闭本客服页面！');
		$.post(path + '/customChat.hts?method=logout&aId='+aId+'&userId='+userCustomeId+'&time='+new Date().getTime());
		window.close();
	}
}
//标签转义公共方法
function escapeString(str)
{
	str = str.replaceAll("&","&amp;");
	str = str.replaceAll("<","&lt;");
	str = str.replaceAll(">","&gt;");
	str = str.replaceAll(" ","&nbsp;");
	
	return str;
}
//标签转义公共方法
function escapeString2(str)
{
	str = str.replaceAll("<","&lt;");
	str = str.replaceAll(">","&gt;");
	return str;
}
String.prototype.replaceAll  = function(s1,s2){    
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function removeHTMLTag(str) {
    str = str.replace(/<\/?[^>^W^@]*>/g,''); //去除HTML tag
    str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
    //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
    str=str.replace(/&nbsp;/ig,' ');//去掉&nbsp;
    return str;
}
function removeHTMLTag2(str) {
    str = str.replace(/<\/?[^>^W^@]*>/g,''); //去除HTML tag
    str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
    //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
    //str=str.replace(/&nbsp;/ig,' ');//去掉&nbsp;
    return str;
}
