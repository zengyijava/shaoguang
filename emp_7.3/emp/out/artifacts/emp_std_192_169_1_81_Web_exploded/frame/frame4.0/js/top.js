//**********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var iIE = (document.all) ? true : false;
var iIE6 = iIE && (navigator.appVersion.match(/MSIE 6.0/i) == "MSIE 6.0" ? true : false);
//菜单悬浮显示计算top时兼容ie6用到
var topNum = 0;
if (iIE6) {
	topNum = 5;
}

var appn = navigator.appName;
var iWidth = 656;
var iHeight = 447;
var iWidth2 = 526;
var iHeight2 = 247;

if (iIE6) {
	iWidth = 630;
	iHeight = 470;
}
if (appn == "Netscape") {
	iWidth = 646;
	iHeight = 447;
}

//***************
$(document).ready(function() {
	//新增隐藏操作:关于平台、帮助手册、反馈(start)   默认全部显示
	var dispContent="11111000";
	var path=$("#path").val();
	//多企业
	if(corpType!=0){
		/*关于平台*/
	}else{
		$.post(path+'/loginImg.login',{method:'getCfgInfo'},function(data,textStatus){
			if(textStatus=='success'){
				data = eval("("+data+")");
				dispContent=data.dispContent;
			    if(dispContent.length>=8){
			    	//关于平台
					 if(dispContent.charAt(0)=='0')
					{
						// 隐藏掉
						 $("a[id=aboutPlatform]").hide();
						 $("#aboutPlatform-span").hide();
					}else if(dispContent.charAt(0)=='1') {
						//显示
						$("a[id=aboutPlatform]").show();
						$("#aboutPlatform-span").show();
					}
					//反馈 id=feedback
					 if(dispContent.charAt(3)=='0')
					{
						 //隐藏掉
						$("a[id=feedback]").hide();
						$("#feedback-span").hide();
					}else if(dispContent.charAt(3)=='1') {
						//显示
						$("a[id=feedback]").show();
						$("#feedback-span").show();
					}
					
					//帮助手册
					 if(dispContent.charAt(4)=='0')
					{
						 //隐藏掉
						 $("a[id=helpManual]").hide();
						 $("#helpManual-span").hide();
					}else if(dispContent.charAt(4)=='1') {
						//显示
						 $("a[id=helpManual]").show();
						 $("#helpManual-span").show();
					}
					
					
				} 
				
			}
		});
		
	}
	//新增隐藏操作:关于平台、帮助手册、反馈（end）
	
	
	//获取左箭头切换的位置
	
	/*if($('body').width()<=1024){
		$('#topLink').addClass('w1024');
	}*/
	$('#leftDiv').css({'left':$('#topLink').offset().left-20+'px'});
	$('#rightDiv').css({'left':($('#topLink').offset().left+$('#topLink').outerWidth()+20)+'px'});
	getLoginInfo1("#topparams");

	$("#u li").each(function(i) {
		if (i > 0) {
			$(this).hover(function() {
				if (this.id != "firstPage") {
					$(this).addClass("on");
				}
			}, function() {
				$(this).removeClass("on");
			});
		}
	});

	//短信助手
	$("#11").hover(function() {
		//alert('111');
		$(this).css("background-position-y", "-328px");
	}, function() {
		$(this).css("background-position-y", "-200px");
	});
	//管理员下拉菜单
	/*$("#loginMes").hover(function() {
		var top = $("#loginMes").offset().top + $(this).height() - topNum;
		var left = $("#loginMes").offset().left+$("#loginMes").outerWidth()-parent.$("#loginMesFloating").outerWidth();				
		parent.$("#ifr").css("top", top + "px");
		parent.$("#ifr").css("left", left + "px");
		parent.$("#loginMesFloating").css("top", top + "px");
		parent.$("#loginMesFloating").css("left", left + "px");
		parent.$("#ifr").css("height", parent.$("#loginMesFloating").height() + "px");
		$(this).addClass("onSys");
		$(this).css("cursor", "pointer");
		parent.$("#ifr").css({'width':'98px'}).show();
		parent.$("#loginMesFloating").show();
	}, function() {
		parent.$("#loginMesFloating").hide();
		parent.$("#ifr").hide();
		var onSys = $("#onSys").val();
		$("#loginMes").removeClass("onSys");
	});*/
	
	
	
	$("#sysmenu").hover(

	function() {
		var top = $("#sysmenu").offset().top + $(this).height() - topNum;
		var left = $("#sysmenu").offset().left + $(this).width() - 98;

		parent.$("#ifr").css("top", top + "px");
		parent.$("#ifr").css("left", left + "px");
		//parent.$("#uChildren").slideDown("slow");;
		parent.$("#dd").css("top", top + "px");
		parent.$("#dd").css("left", left + "px");
		//parent.$("#uChildren").slideDown("slow");;
		// var onSys=$("#onSys").val();
		parent.$("#ifr").css("height", parent.$("#dd").height() + "px");
		// if(onSys!="1")
		// {
		//$("#firstPage").removeClass("selected");
		$(this).addClass("onSys");
		$(this).css("cursor", "pointer");
		parent.$("#ifr").show();
		parent.$("#dd").show();
		// }
		//zhezhao();
	}, function() {
		parent.$("#dd").hide();
		parent.$("#ifr").hide();
		var onSys = $("#onSys").val();
		if (onSys == "1") {
			$("#sysmenu").removeClass("onSys");
			//$(".selected").removeClass("selected");
			//$("#firstPage").addClass("selected");
		} else {
			$("#sysmenu").removeClass("onSys");
		}
	});
	$(".topMenu").hover(function() {
		if (!$(this).hasClass("cursel")) {
			$(this).addClass("curhover");
		}
	}, function() {
		$(this).removeClass("curhover");
	});
	//触发一下
	reOpen();

});

function reOpen(){
	var idstr=$('#menu', window.parent.document).html();
	if(idstr!=null&&idstr!=''){
		$("#"+idstr).trigger('click');
	}
}



//获取网关认证信息里的发送速度 
var sendSpeed;
function findinfo() {
	$.post(path + "/findAproInfo", {
		method: "find"
	}, function(result) {
		if (result != null && result != "error") {
			sendSpeed = result;
		}
	});
	var bodywidth = $("body").width();
	var topMenu_width=$('.topMenu').outerWidth();
	var newTabCount = Math.floor((bodywidth - $('#topLink').offset().left) / topMenu_width);
	changeTopMenu(newTabCount);
}
//退出

function logout() {
	/*是否退出登录？*/
	if (confirm(getJsLocaleMessage("common","common_frame2_top_5"))) {

		parent.window.document.getElementById("logoutalert").value = 1;
		parent.window.location.replace(path + '/logout')
	}
}
//关于平台

function about() {
	parent.showAbout();
}

function checkFee() {
	var tkn = $("#tkn").val();
	$.post(path + "/emp_tz.htm", {
		method: "checkTkn",
		tkn:tkn,
		isAsync: "yes"
	},function(result) {
		if (result == "true") {
			time = time + 1;
			window.showModalDialog(iPath + "/checkFeeBefor.jsp?lguserid="+$("#userid").val()+"&isshowww=0&time=" + time + "&ye=" + $("#hiddenYe").val() + "&mmsBanalce=" + $("#mmsBalance").val(), "", "dialogWidth=" + iWidth + "px;dialogHeight=" + iHeight + "px;help:no;status:no");
		} else {
			window.location.href = path  + "/common/logoutEmp.jsp";
		}
	});
	
}

//帮助手册

function upLoad() {
	/*是否下载帮助手册？*/
	if (confirm(getJsLocaleMessage("common","common_frame2_left_7"))) {
		uploadFiles('common/file/help.zip', getContextPath);
	}
}

//系统首页

function doIndex() {
	try {
		$("#position").empty();
		parent.$("#dd").hide();
		parent.$("#ifr").hide();
		$(".selected").removeClass("selected");
		$("#firstPage").addClass("selected");
		$("#onSys").attr("value", "1");
		parent.window.frames['mainFrame'].location.href = path + "/thirdMenu.htm?method=thirdMenuPage";
	} catch (error) {
		parent.window.frames['mainFrame'].location.href = 'login.jsp';
	}
}

//通知公告

function doNotice() {
	try {
		$("#onSys").val("2");
		$("#position").empty();
		$(".selected").removeClass("selected");
		$("#secondPage").addClass("selected");
		var lgcorpcode = $("#lgcorpcode").val();
		parent.window.frames['mainFrame'].location.href = path + "/i_firstPage.htm?lgcorpcode=" + lgcorpcode;
	} catch (error) {
		parent.window.frames['mainFrame'].location.href = 'login.jsp';
	}
}

//通知公告

function doNotice2() {
	window.parent.showGg();
	$("#onSys").attr("value", "2");
}

//获取当前发送余额

function getCt() {}

function getLoginInfo1(ids) {
	var $pa = $(window.parent.document);
	var pahtm = $pa.find("#loginparams").html();
	$(ids).html(pahtm);
}

function clickSkin(skin) {
	var tkn = $("#tkn").val();
	$.post(path + "/emp_tz.htm", {
		method: "checkTkn",
		tkn:tkn,
		isAsync: "yes"
	},function(result) {
		if (result == "true") {
			parent.window.openSkinDiv();
		} else {
			window.location.href = path  + "/common/logoutEmp.jsp";
		}
	});
}

function clickLanguage() {
	parent.window.openLanguageDiv();
}

function complain() {
	var tkn = $("#tkn").val();
	$.post(path + "/complain.htm", {
		method: "checkTkn",
		tkn:tkn,
		isAsync: "yes"
	},function(result) {
		if (result == "true") {
			parent.window.openComplainDiv();
		} else {
			window.location.href = path  + "/common/logoutEmp.jsp";
		}
	});
}
//头部菜单点击事件

function showLeftMenu(idstr) {
	$('#menu', window.parent.document).html(idstr);
	$('#menuInfo', window.parent.document).html('');
	if (!$("#" + idstr).hasClass("cursel")) {
		$(".cursel").removeClass("cursel");
		$("#" + idstr).addClass("cursel");
		urlRouter.tkn=0;
		if(urlRouter.tkn){
			ajaxOnline(urlRouter,idstr);
		}else{
			urlRouter.tkn=$('#appTkn').val();
			ajaxOnline(urlRouter,idstr);
		}
		
	}
}

function ajaxOnline(urlRouter,idstr){
	if(urlRouter['flag'] && ('mod'+urlRouter['modId'])==idstr){
	//在线坐席 对不兼容浏览器的处理
	if(!isBrowserOk()){
		return;
	}
	$.ajax({
		type:'GET',
		url:path+'/customChat.htm?method=checkUser',
		data:'userid='+urlRouter.userid+'&tkn='+urlRouter.tkn+'&isAsync=yes',
		success:function(data){
		    if(data){
		    	if(data == "outOfLogin")
				{
					location.href=path+"/common/logoutEmp.jsp";
					return;
				}
		    	data=eval('('+data+')');
				if(data['aid']==0){
					alert(data['msg']);
					return false;
				}else{
					window.open(path+'/'+data['url']);
				}
		    }
			
		}
	});
	}else{
		$(window.parent.document).find("#mainFrame").contents().find("#leftIframe")[0].contentWindow.showMenuTable(idstr);
	}
}
//选中头部菜单，不触发跳转

function showTopMenu(idstr) {
	if (!$("#" + idstr).hasClass("cursel")) {
		$(".cursel").removeClass("cursel");
		$("#" + idstr).addClass("cursel");
	}
}

function showTabLeft() {
	$("#top_menu_right").show();
	var hidmm = perMenuSize + hidMenuIndex - 1;
	//$(".topMenu:eq("+hidmm+")").hide();
	var showTabIndex = hidMenuIndex - 1;
	$(".topMenu:eq(" + showTabIndex + ")").show();
	hidMenuIndex = hidMenuIndex - 1;
	if (hidMenuIndex == 0) {
		$("#top_menu_left").hide();
	}
}

function showTabRight() {
	$("#top_menu_left").show();
	$(".topMenu:eq(" + hidMenuIndex + ")").hide();
	var showTabIndex = perMenuSize + hidMenuIndex;
	$(".topMenu:eq(" + showTabIndex + ")").show();
	hidMenuIndex = hidMenuIndex + 1;
	if (hidMenuIndex == hidMenuSize) {
		$("#top_menu_right").hide();
	}
}
var aaaaa = 0;
var singleNav=$('.topMenu').outerWidth();
window.onresize = function() {
	if($('body').width()>=1024){
		$('#topLink').removeClass('w1024');
	}
	aaaaa = 0
	if (aaaaa < 1) {
		aaaaa = aaaaa + 1;
	} else {
		aaaaa = 0;
		return;
	}
	var bodywidth = $("body").width();
	
	var newTabCount = Math.floor((bodywidth - 252) / singleNav);
	changeTopMenu(newTabCount);
	setTimeout("aaaaa=0;", 400);
}

function changeTopMenu(newTabCount) {
	var dwidth=newTabCount * singleNav;
	/*$("#topLink").css("width", dwidth + "px");*/
	$("#rightDiv").css("left", dwidth+$("#topLink").offset().left+10 + "px");
	perMenuSize = newTabCount;
	hidMenuSize = menuSize - perMenuSize;
	hidMenuSize = hidMenuSize < 0 ? 0 : hidMenuSize;
	while (hidMenuIndex > hidMenuSize) {
		showTabLeft();
	}
	if (hidMenuIndex == hidMenuSize) {
		$("#top_menu_right").hide();
	} else {
		$("#top_menu_right").show();
	}
	var aindex = $(".cursel").index("#topLink .topMenu");
	while ((aindex + 1 - hidMenuIndex) > perMenuSize) {
		showTabRight();
	}
	while (aindex < hidMenuIndex) {
		showTabLeft();
	}
}

function doUpdatePass() {
	var tkn = $("#tkn").val();
	$.post(path + "/emp_tz.htm", {
		method: "checkTkn",
		tkn:tkn,
		isAsync: "yes"
	},function(result) {
		if (result == "true") {
			parent.doUpdatePass();
		} else {
			window.location.href = path  + "/common/logoutEmp.jsp";
		}
	});
}

//判断浏览器版本是否可以运行在线客服
function isBrowserOk(){
	var isIE = (document.all) ? true : false;
    var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0");
	var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0");
	var isIE8 = isIE && (navigator.appVersion.match(/MSIE 8.0/i)=="MSIE 8.0")&&(navigator.appVersion.search(/trident\/4\.0/i)>-1);
	//var isIE9 = isIE && (navigator.appVersion.match(/MSIE 9.0/i)=="MSIE 9.0");		
	var is360 = isIE && ((navigator.userAgent).indexOf('360SE')>0);// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) 360SE
	//搜狗
	var userAgent = navigator.userAgent.toLowerCase(); 
	var isSG =  (userAgent.indexOf('se 2.x') != -1);// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)SE 2.X
	if(isIE6||isIE7||isIE8||is360||isSG){
		alert(getJsLocaleMessage("common","common_frame2_top_6"));
		return false;
	}
	return true;
}