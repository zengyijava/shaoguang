(EMP_Cookie = {
 			add:function(key,value){document.cookie = key + "="+ escape (value) + ";expires=Tue, 19 Jan 2038 00:01:11 GMT";},
  			del:function(key){document.cookie=key+"= ; expires=Sat, 01 Sep 2001 00:00:00 GMT";},
  			get:function(key){var arr,reg=new RegExp("(^| )"+key+"=([^;]*)(;|$)");if(arr=document.cookie.match(reg))return unescape(arr[2]);else return null;}
   	});
var autoPassUsers0;
var tingyong = "对不起！该登录账号已注销（已禁用），请联系管理员。";
var copyright= "梦网科技 All rights reserved 建议使用IE9及以上版本、360、谷歌浏览器";
var langName = EMP_Cookie.get(langKeyJs)||"zh_CN";
var deny= "不能登录，系统未配置适合运行的浏览器版本，请联系管理员！";
    	var zh_CN_Languages={"title":"欢迎访问企业移动信息平台","enPlaceholder":"企业编码","namePlaceholder":"用户名","pwdPlaceholder":"密码","codePlaceholder":"验证码","verifyImg":"看不清楚?点击换一张!","getPwd":"找回密码","loginSub":"登录",
    			"dynphoneword":"短信动态口令","dynCommand":"动态口令","getphoneword":"获取","sendsucess":"*点击【获取】按钮获取动态口令","sendtime":"*30秒后能重新获取动态口令","autoPasswordDiv":"自助重置密码","tdAutoUserName":"用户名：","tdAutoName":"姓名：",
    			"resetReason":"重置密码的原因：","autoReason0":"密码遗忘","autoReason1":"输错次数过多","autoReason2":"系统原因",
    			"pwdReceiver":"密码接收人：","autoPassUsers0":"请选择","probarContent":"处理中,请稍等.....","copyright":"梦网科技 All rights reserved 建议使用IE9及以上版本、360、谷歌浏览器","tingyong":"对不起！该登录账号已注销（已禁用），请联系管理员。","deny":"不能登录，系统未配置适合运行的浏览器版本，请联系管理员！",
    			"supportBrowser1":"浏览器再登录.","supportBrowser0":"您使用的浏览器不适合本版本系统运行，请使用","favourite":"收藏此页","about":"关于平台","dialogueTitle":"消息","okVal":"确定","cancelVal":"取消","loginValue":"登录"};
    	var zh_TW_Languages={"title":"歡迎訪問企業移動信息平台","enPlaceholder":"企業編碼","namePlaceholder":"用戶名","pwdPlaceholder":"密碼","codePlaceholder":"驗證碼","verifyImg":"看不清楚?點擊換一張!","getPwd":"找回密碼","loginSub":"登錄",
    			"dynphoneword":"短信動態口令","dynCommand":"動態口令","getphoneword":"獲取","sendsucess":"*點擊【獲取】按鈕獲取動態口令","sendtime":"*30秒後能重新獲取動態口令","autoPasswordDiv":"自助重置密碼","tdAutoUserName":"用戶名：","tdAutoName":"姓名：",
    			"resetReason":"重置密碼的原因：","autoReason0":"密碼遺忘","autoReason1":"輸錯次數太多","autoReason2":"系統原因",
    			"pwdReceiver":"密碼接收人：","autoPassUsers0":"請選擇","probarContent":"處理中,請稍等.....","copyright":"夢網科技 All rights reserved 建議使用IE9及以上版本、360、谷歌瀏覽器","tingyong":"對不起！該登錄帳號已註銷（已禁用），請聯繫管理員。","deny":"不能登录，系统未配置适合运行的浏览器版本，请联系管理员！",
    			"supportBrowser0":"您使用的瀏覽器不適合本版本系統運行，請使用","supportBrowser1":"瀏覽器再登錄.","favourite":"收藏此頁","about":"關於平臺","dialogueTitle":"消息","okVal":"確定","cancelVal":"取消","loginValue":"登錄"};
    	var zh_HK_Languages={"title": "welcome to the enterprise mobile information platform","enPlaceholder": "enterprise encoding","namePlaceholder": "username","pwdPlaceholder":"password",
    			"codePlaceholder": "code", "verifyImg": "cant see? Click another one!","getPwd": "find password","loginSub": "login","dynphoneword": "SMS dynamic password","dynCommand": "dynamic password",
    					"getphoneword": "access","sendsucess": "* click [acquisition] button for dynamic password","sendtime": "*30 seconds can retrieve dynamic password","autoPasswordDiv": "self reset the password",
    					"tdAutoUserName": "user name","tdAutoName":"full name","resetReason": "reset password","autoReason0": "password is forgotten","autoReason1":"input the wrong number over","autoReason2":"other",
    					"pwdReceiver": "receiver","autoPassUsers0": "please select","probarContent": "processing, please wait....","copyright":"Montnets All rights reserved over IE9、360、google browser are supported",
    					"tingyong":"sorry！your account has been disabled。","deny":"login denied，suitable browser has not been configed！","supportBrowser0":" browser are supported，please use",
    					"supportBrowser1":"browser to login","favourite":"Bookmark This Page","about":"About the platform","dialogueTitle":"Message","okVal":"Ok","cancelVal":"Cancel","loginValue":"Login"};
    	function changeLang(obj){
    		try {
    			//初始化弹出提示
				tipInfo = getJsLocaleMessage("common", "common_frame2_login_44");
			} catch (e) {
			}
				var  myselect=document.getElementById("langName");
    		if(!obj){
        		var index=myselect.selectedIndex;
        		langName = myselect.options[index].value;
        		EMP_Cookie.add(langKeyJs,langName);
    		}else{
    			langName=obj;
    			 for (var i = 0; i < myselect.options.length; i++){  
         	        if (myselect.options[i].value == langName){  
         	        	myselect.options[i].selected = true;  
         	            break;  
         	        }  
         	    }  
    		}
    		var source = langName+"_Languages";
    		
    		try {
				//加载语言资源文件
				var iHead = document.getElementsByTagName('HEAD').item(0);
				var iScript = document.createElement("script");
				iScript.setAttribute("type", "text/javascript");
				iScript.setAttribute("src", commonPath + "/common/i18n/" + langName + "/common_" + langName + ".js");
				iHead.appendChild(iScript);
			} catch (e) {
				
			}
			document.getElementById("title").textContent=eval(source)["title"];
    		if(document.getElementById("enPlaceholder"))document.getElementById("enPlaceholder").textContent=eval(source)["enPlaceholder"];
/*         		document.getElementById("login_name").nextElementSibling.textContent=eval(source)["login_name"]; */
    		document.getElementById("namePlaceholder").textContent=eval(source)["namePlaceholder"];
    		document.getElementById("pwdPlaceholder").textContent=eval(source)["pwdPlaceholder"];
    		if(document.getElementById("loginValue"))document.getElementById("loginValue").textContent=eval(source)["loginValue"];
    		if(document.getElementById("codePlaceholder"))document.getElementById("codePlaceholder").textContent=eval(source)["codePlaceholder"];
    		if(document.getElementById("verifyImg"))document.getElementById("verifyImg").setAttribute("title",eval(source)["verifyImg"]);
    		if(document.getElementById("getPwd"))document.getElementById("getPwd").textContent=eval(source)["getPwd"];
    		if(document.getElementById("loginSub"))document.getElementById("loginSub").textContent=eval(source)["loginSub"];
    		if(document.getElementById("dynphoneword"))document.getElementById("dynphoneword").setAttribute("title",eval(source)["dynphoneword"]);
    		if(document.getElementById("dynCommand"))document.getElementById("dynCommand").textContent=eval(source)["dynCommand"];
    		if(document.getElementById("getphoneword"))document.getElementById("getphoneword").setAttribute("value",eval(source)["getphoneword"]);
    		if(document.getElementById("sendsucess"))document.getElementById("sendsucess").textContent=eval(source)["sendsucess"];
    		if(document.getElementById("sendtime"))document.getElementById("sendtime").textContent=eval(source)["sendtime"];
    		if(document.getElementById("autoPasswordDiv"))document.getElementById("autoPasswordDiv").setAttribute("title",eval(source)["autoPasswordDiv"]);
    		if(document.getElementById("tdAutoUserName"))document.getElementById("tdAutoUserName").textContent=eval(source)["tdAutoUserName"];
    		if(document.getElementById("tdAutoName"))document.getElementById("tdAutoName").textContent=eval(source)["tdAutoName"];
    		if(document.getElementById("resetReason"))document.getElementById("resetReason").textContent=eval(source)["resetReason"];
    		if(document.getElementById("autoReason")){
    			document.getElementById("autoReason")[0].textContent=eval(source)["autoReason0"];
    			document.getElementById("autoReason")[1].textContent=eval(source)["autoReason1"];
    			document.getElementById("autoReason")[2].textContent=eval(source)["autoReason2"];
    		}
    		if(document.getElementById("pwdReceiver"))document.getElementById("pwdReceiver").textContent=eval(source)["pwdReceiver"];
    		autoPassUsers0 = eval(source)["autoPassUsers0"];
    		if(document.getElementById("autoPassUsers"))
    		document.getElementById("autoPassUsers")[0].textContent=autoPassUsers0;
    		if(document.getElementById("probarContent"))document.getElementById("probarContent").textContent=eval(source)["probarContent"];
    		var dialogueTitle = document.getElementsByClassName("aui_title");
    		if(dialogueTitle&&dialogueTitle.length>0){
    			dialogueTitle[0].textContent=eval(source)["dialogueTitle"];
    		}
    		
    		var buttons = document.getElementsByClassName("aui_buttons");
    		if(buttons&&buttons.length>01&&buttons[0].children.length==2){
    			buttons[0].children[0].textContent=eval(source)["okVal"];
    			buttons[0].children[1].textContent=eval(source)["cancelVal"];
    		}
    		
    		copyright=eval(source)["copyright"];
    		if(copyright){
//    			document.getElementById("cright").textContent=document.getElementById("cright").textContent.substring(0,16)+copyright;
    			//document.getElementById("cright").textContent=copyright;
    		}
    		tingyong=eval(source)["tingyong"];
    		deny=eval(source)["deny"];
    		if(document.getElementById("sccy")){
    			document.getElementById("sccy").textContent=eval(source)["favourite"];
    		} else {
    			var langSeprator = document.getElementById("langSeprator");
    			if(langSeprator)
    			langSeprator.parentNode.removeChild(langSeprator);
    		}
    		if(document.getElementById("gypt"))document.getElementById("gypt").textContent=eval(source)["about"];
    		// document.getElementById("bgPng").style.backgroundImage='url('+commonPath+'/common/img/login_bg_'+langName+'.png)';
    		
    		 var alertStr=eval(source)["supportBrowser0"];
    		//根据配置的浏览器版本动态拼接提示
    		if (browser != null) {
    			browser = browser.trim();
    			if (browser.indexOf("IE6") != -1) {
    				alertStr = alertStr + "IE6,";
    			}
    			if (browser.indexOf("IE7") != -1) {
    				alertStr = alertStr + "IE7,";
    			}
    			if (browser.indexOf("IE8") != -1) {
    				alertStr = alertStr + "IE8,";
    			}
    			if (browser.indexOf("IE9") != -1) {
    				alertStr = alertStr + "IE9,";
    			}
    			if (browser.indexOf("IE10") != -1) {
    				alertStr = alertStr + "IE10,";
    			}
    			if (browser.indexOf("sogou") != -1) {
    				alertStr = alertStr + "sogou,";
    			}
    			//判断配置是否正确，不正确或未配置给出对应提示
    			if (alertStr.indexOf(",") != -1) {
    				alertStr = alertStr.substring(0, alertStr.lastIndexOf(","));
    				alertStr = alertStr + eval(source)["supportBrowser1"];;
    			} else {
    				alertStr = deny;
    			}
    		
    		} else {
    			alertStr = deny;
    		}
    		
    	}
		//判断数组中是否存在某个值
    	function inArray(langName,langArr) {
    		var flag = false;
			for(var i =0;i < langArr.length;i++){
				if(langName === langArr[i]){
					flag = true;
					break;
				}
			}
			return flag;
        }

		window.onload = function() {
            var multiLanguageEnable = document.getElementById("multiLanguageEnable").value;
            var langs = document.getElementById("langs").value;
            if("Yes"===multiLanguageEnable) {
                var langArr = langs.split(",");
                //如果只选择一门语言
                if (langArr.length === 1 && (langArr[0] === "zh_CN" || langArr[0] === "zh_TW" || langArr[0] === "zh_HK")) {
                    changeLang(langArr[0]);
                    EMP_Cookie.add(langKeyJs,langArr[0]);
				//选择三门语言
                }else if(langArr.length === 3 && inArray("zh_CN",langArr) && inArray("zh_HK",langArr) && inArray("zh_TW",langArr)){
                    initIndex();
				//选择两门语言
                }else if(langArr.length === 2 && (inArray("zh_CN",langArr) || inArray("zh_HK",langArr) || inArray("zh_TW",langArr))){
                	//如果其中有zh_CN优先显示简体
					if(inArray("zh_CN",langArr)){
                        initIndex();
					}else {//没有zh_CN优先显示繁体
						if(!EMP_Cookie.get(langKeyJs)){
                            changeLang("zh_TW");
                            EMP_Cookie.add(langKeyJs,"zh_TW");
						}else {
                            initIndex();
                        }
					}
				}else {
                    initIndex();
				}

            }else{
            	//不开启多语言开关则设置为默认语言
            	var langName = document.getElementById("langName").value;
            	if(!langName){
            		langName = "zh_CN";
            	}
                changeLang(langName);
            }
		};
    	function initIndex(){
    		changeLang(langName);
    	}