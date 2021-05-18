//**********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var iIE = (document.all) ? true : false;
var iIE6 = iIE && (navigator.appVersion.match(/MSIE 6.0/i) == "MSIE 6.0" ? true : false);
//菜单悬浮显示计算top时兼容ie6用到
var topNum = 0;
if (iIE6) {
	topNum = 5;
}

var appn = navigator.appName;
var empLangName = $("#empLangName").val();

var iWidth = 526;
var iHeight = 247;

if (iIE6) {
	iWidth = 530;
	iHeight = 270;
}
if (appn == "Netscape") {
	iWidth = 526;
	iHeight = 247;
}
//***************
$(document).ready(function() {
	getLoginInfo1("#topparams");
	if (isCharging) {
		//getCt();
		//errorNum=0;
		//dd==window.setInterval("getCt()",60*1000);
	}
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

	//新增隐藏操作:关于平台、帮助手册、反馈(start)
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
						 //隐藏掉
						 parent.$("#loginMesFloating-3").hide();
					}else if(dispContent.charAt(0)=='1') {
						//显示
						parent.$("#loginMesFloating-3").show();
					}
					//反馈id=feedback
					 if(dispContent.charAt(3)=='0')
					{
						 //隐藏掉
						$("#secondPageDiv a[id=feedback]").parent().hide();
					}else if(dispContent.charAt(3)=='1') {
						//显示
						$("#secondPageDiv a[id=feedback]").parent().show();
					}
					
					//帮助手册
					 if(dispContent.charAt(4)=='0')
					{
						 //隐藏掉
						 parent.$("#loginMesFloating-2").hide();
					}else if(dispContent.charAt(4)=='1') {
						//显示
						parent.$("#loginMesFloating-2").show();
					}
					
					
				} 
				
			}
		});
		
	}
	//新增隐藏操作:关于平台、帮助手册、反馈（end）

	$("#sysmenu").hover(function() {
		var top = $("#sysmenu").offset().top + $(this).height() - topNum;
		var left = $("#sysmenu").offset().left + $(this).width() - 98;
		parent.$("#ifr").css("top", top + "px");
		parent.$("#ifr").css("left", left + "px");
		parent.$("#dd").css("top", top + "px");
		parent.$("#dd").css("left", left + "px");
		parent.$("#ifr").css("height", parent.$("#dd").height() + "px");
		$(this).addClass("onSys");
		$(this).css("cursor", "pointer");
		parent.$("#ifr").css({'width':'98px'}).show();
		parent.$("#dd").show();
	}, function() {
		parent.$("#dd").hide();
		parent.$("#ifr").hide();
		var onSys = $("#onSys").val();
		$("#sysmenu").removeClass("onSys");
	});

	//切换语言
	$("#lg").hover(function() {
		var top = $("#lg").offset().top + $(this).height() - topNum;
		var left = $("#lg").offset().left+$("#lg").outerWidth()-98;	
		parent.$("#ifr").css("top", top + "px");
		parent.$("#ifr").css("left", left + "px");
		parent.$("#lang").css("top", top + "px");
		parent.$("#lang").css("left", left + "px");
		parent.$("#ifr").css("height", parent.$("#lang").height() + "px");
		$(this).addClass("onSys");
		$(this).css("cursor", "pointer");
		parent.$("#ifr").css({'width':'98px'}).show();
		parent.$("#lang").show();
	}, function() {
		parent.$("#lang").hide();
		parent.$("#ifr").hide();
		var onSys = $("#onSys").val();
		$("#lg").removeClass("onSys");
	});
	//查询余额
	$('#remainingNum').hover(function(){
		$(this).addClass("onSys");
		$(this).css("cursor", "pointer");
		//是否正在查询中
		var isQuery = !!$('#remainingNum').attr("title");
		if(!isQuery) {
			getBalance();//ajax查询余额
		}
	}, function() {
		parent.$("#rNumFloating").hide();
		parent.$("#ifr").hide();
		$("#remainingNum").removeClass("onSys");
	})
	//管理员下拉菜单
	$("#loginMes").hover(function() {
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
	});
	
	//floatingOnParent("loginMes", "loginMesFloating");
	//floatingOnParentGetCt("remainingNum", "rNumFloating");

});
function getBalance(){
	var path=$("#path").val();
	var userid = $("#userid").val();
	$.ajax({
		type:'GET',
		url:path+"/emp_tz.htm",
		data:"method=getCt&lguserid="+userid+"&time="+new Date().getTime(),
		beforeSend:function(){
			$('#remainingNum').attr("title",getJsLocaleMessage("common","common_frame3_uploadLogo_1"));
		},
		success:function(result){
			if(result == "outOfLogin"){
				parent.document.getElementById("logoutalert").value = 1
	    		window.location.href=path+"/common/logoutEmp.jsp";
				return;
			}else{
				$('#remainingNum').attr("title","");


                //如果鼠标已经移出 则不显示
                if(!$('#remainingNum').hasClass('onSys'))
                {
                    return;
                }

				result=result.substr(result.indexOf("ye")+2);
				if(result.indexOf("ye")!=-1)
				{
					result=result.substr(result.indexOf("ye")+2);
				}
				var count=result.split(",");
				var regNum=/^[0-9]*$/; 
				//如果是数字返回true，其它返回false
				if(regNum.test(count[0])&&regNum.test(count[1]))
				{
					parent.$("#ctSms").html(count[0]);
					parent.$("#ctMms").html(count[1]);
					$("#hiddenYe").val(count[0]);
					$("#mmsBalance").val(count[1]);
				}
				var rNumFloating=parent.$("#rNumFloating"),
					remainingNum=$('#remainingNum'),
				    rNumFloating_H=rNumFloating.outerHeight(),
					rNumFloating_W=rNumFloating.outerWidth();
					
					
				var left = remainingNum.offset().left+remainingNum.outerWidth()-rNumFloating_W;		
				var top = 63;
				parent.$("#ifr").css({
					"top":top + "px",
					"left": left + "px",
					"height":rNumFloating_H+"px",
					"width":rNumFloating_W+"px"
				});
				parent.$("#rNumFloating").css({"top":top + "px","left":left + "px"});
				parent.$("#ifr").show();
				parent.$("#rNumFloating").show();
			}
		}
	})
	
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
}
//退出

function logout() {
	if (confirm(getJsLocaleMessage("common","common_frame2_top_5"))) {
		parent.window.document.getElementById("logoutalert").value = 1;
		parent.window.location.replace(path + '/logout')
		location.replace(this.href);
		event.returnValue=false;
	}
}
//关于平台

function about() {
	parent.showAbout();
}

function checkFee() {
    if("zh_HK" === empLangName){
        iWidth = 650;
    }
	time = time + 1;
	window.showModalDialog(iPath + "/checkFeeBefor.jsp?lguserid="+$("#userid").val()+"&time=" + time + "&ye=" + $("#hiddenYe").val() + "&mmsBanalce=" + $("#mmsBalance").val(), "", "dialogWidth=" + iWidth + "px;dialogHeight=" + iHeight + "px;help:no;status:no");
}

//帮助手册

function upLoad() {
	var tkn = $("#tkn").val();
	$.post(path + "/common.htm", {
		method: "checkTkn",
		tkn:tkn,
		isAsync: "yes"
	},function(result) {
		if (result == "true") {
			if (confirm(getJsLocaleMessage("common","common_frame2_left_7"))) {
				uploadFiles('common/file/help.zip', getContextPath);
			}
		} else {
			window.location.href = path  + "/common/logoutEmp.jsp";
		}
	});
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
		$("#menu", window.parent.document).html("");
		var tkn = $("#tkn").val();
		parent.window.frames['mainFrame'].location.href = path + "/thirdMenu.htm?method=getMonInfo&tkn="+tkn;
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
	parent.window.openSkinDiv();
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