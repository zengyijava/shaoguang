var empLangName = $("#empLangName").val();
function sizeset()
{
	var widd = $("body").width();
	if(widd<1000)
	{
		$("#wrap").css("width","1000px");
	}else
	{
		$("#wrap").css("width","100%");
	}
}

function befoclose()
{
//	$.post("<%=request.getContextPath()%>/logout");
}
function goout(){
    var logoutalert = $("#logoutalert").val();
    if(logoutalert == 0)
    {
    	$("#logoutalert").val(1);
    	setTimeout("$('#logoutalert').val(0);", 1500 );
       return "";
    }
}
function focusShowPass() {
	var ep = $("#new_pass1");
	ep.next("div").css("display", "block");
	ep.focus();
}

function blurPass(){
	var ccontent = $("#new_pass1");
	if (ccontent.val() == "") {
		ccontent.next("div").css("display", "block");
	} else {
		ccontent.next("div").css("display", "none");
	}
}
function focusPass() {
	var ep = $("#new_pass1");
	ep.next("div").css("display", "none");
}

//个人设置
function doUpdatePass(){
    var tkn = $("#tkn").val();
    var pathUrl = $("#pathUrl").val();
    $.post(pathUrl + "/emp_tz.htm", {
		method: "checkTkn",
		tkn:tkn,
		isAsync: "yes"
	},function(result) {
		if (result == "true") {
			setInfoAndPassword();
		} else {
			window.location.href = pathUrl  + "/common/logoutEmp.jsp";
		}
	});
}
//修改信息或密码。
function setInfoAndPassword()
{
    var lguserid =$("#lguserid").val();
    var pathUrl = $("#pathUrl").val();
    $.get(pathUrl+"/emp_tz.htm",{method:"getSysUserInfo",lguserid:lguserid}
	 ,function(result){
			if(result !=null && result == "error")	
			{
			    alert(getJsLocaleMessage("common","common_frame2_main_34"));
			    return ;
			}
			else if(result !=null && result !="")
			{
			    var array=result.split('&');
			    $("#username").text(array[0]);
			    $("#name").text(array[1]);
			    $("#sex").text(eval(array[2])-0==0?getJsLocaleMessage("common","common_woman"):getJsLocaleMessage("common","common_man"));
			    $("#mobile").attr("value",array[3]!="null"?array[3]:"");
			    $("#oph").attr("value",array[4]!="null"?array[4]:"");
			    $("#EMail").attr("value",array[5]!="null"?array[5]:"");
			    $("#qq").attr("value",array[6]!="null"?array[6]:"");
			    $("#zhu").html("");
			    
			    $("#UpdatePassDiv").dialog({
			        title :getJsLocaleMessage("common","common_personalSetting"),
					autoOpen: false,
					height: 430,
                    width: empLangName === "zh_HK"?400:340,
					resizable:false,
					closeOnEscape: false,
					modal:true,
					beforeClose:function(){
			    		return closeDiv();
			    	}
			   });
			   $('#UpdatePassDiv').dialog('open');
			}
	});		  

}

function changeinfo(type)
{
    $("#button").attr("disabled","");
    if(type=='1')
    {
        $("#updateinfo").removeClass();
        $("#updatepw").removeClass();
        $("#updateinfo").addClass("infotd1");
        $("#updatepw").addClass("infotd2");
        
        $("#updateinfoDiv").show();
        $("#updatePwDiv").hide();
    }
    else
    {
        $("#updateinfo").removeClass();
        $("#updatepw").removeClass();
        $("#updatepw").addClass("infotd1");
        $("#updateinfo").addClass("infotd2");
        
        $("#updatePwDiv").show();
        $("#updateinfoDiv").hide();
    }
}

/**
 * 修改语言
 * @param lang
 * @return
 */
function changeLanguage(lang)
 {
	if(lang!=null&&lang!=""){
		if(confirm(getJsLocaleMessage("common","common_frame2_main_62"))){
		    // 在二级页面切换时语言时，切换皮肤弹框皮肤标签名称未对应语言
		    changeSkinLabel(lang);
			if(lang == 'zh_CN'){
				replacejsfile("common_zh_TW.js", "common_zh_CN.js", lang);
				replacejsfile("common_zh_HK.js", "common_zh_CN.js", lang);
				changeTitle("企业移动信息平台");
			}else if(lang == 'zh_TW'){
				replacejsfile("common_zh_CN.js", "common_zh_TW.js", lang);
				replacejsfile("common_zh_HK.js", "common_zh_TW.js", lang);
				changeTitle("企業移動信息平臺");
			}else if(lang == 'zh_HK'){
				replacejsfile("common_zh_CN.js", "common_zh_HK.js", lang);
				replacejsfile("common_zh_TW.js", "common_zh_HK.js", lang);
				changeTitle("EMP");
			}
			if (lang == 'zh_TW' || lang == 'zh_HK' || lang == 'zh_CN') {
				var pathUrl = $("#pathUrl").val();
				$.post(pathUrl + "/emp_tz.htm", {
					method : "setLangeuage",
					lang : lang,
					userid: $("#lguserid").val(),
					isAsync: "yes"
				}, function(result) {
					if (result == "true") {
						var menu=$("#menu").html();
						 if(menu!=null&&menu!=''){
							 //打开模块
							 $("#topFrame",window.parent.document).attr("src",$("#topFrame",window.parent.document).attr("src"));
							 refreshLanguage();
							 refreshPersonSet();
							 refreshBalance();
							 refreshSkin();
							 showImg();
							 $(window.parent.document).contents().find("#mainFrame")[0].contentWindow.doOpen1(menu); 
						 }else{
							$("#themeForm").attr("action", pathUrl + "/frame/" + "frame3.0" + "/main.jsp");
							$("#themeForm").submit();
						 }
					}
				});
			}
		}
	}
}

function changeTitle(title){
	 var titleList = document.getElementsByTagName("title");
	 if(titleList&&titleList.length>0){
		 titleList[0].textContent=title;
	 };
}

function changeSkinLabel(lang) {
    var nodeSimple = $("#themeDiv .simple_change span");
    var nodeClassic = $("#themeDiv .classsic_change span");
    var nodeTradition = $("#themeDiv .tradition_change span");
    var btnSure = $("#skinDiv .btnClass5");
    var btnCancel = $("#skinDiv .btnClass6");
    var dialogTitle = $($(".ui-dialog .ui-dialog-title").get(0));

    $(nodeSimple).css('background','url(img/icon_classic.png) no-repeat 15px 50%');
    $(nodeClassic).css('background','url(img/icon_classic.png) no-repeat 15px 50%');
    $(nodeTradition).css('background','url(img/icon_tradition.png) no-repeat 15px 50%');

    if(lang == 'zh_HK'){
        $(nodeSimple).text("Simple");
        $(nodeClassic).text("Classic");
        $(nodeTradition).text("Tradition");
        $(nodeSimple).css('background','url(img/icon_classic.png) no-repeat 3px 50%');
        $(nodeClassic).css('background','url(img/icon_classic.png) no-repeat 5px 50%');
        $(nodeTradition).css('background','url(img/icon_tradition.png) no-repeat 3px 50%');
        $(btnSure).val("Ok");
        $(btnCancel).val("Cancel");
        $(btnSure).css('letter-spacing','0');
        $(btnCancel).css('letter-spacing','0');
        $(dialogTitle).text("change skin");
    } else if(lang == 'zh_CN'){
        $(nodeSimple).text("简约");
        $(nodeClassic).text("经典");
        $(nodeTradition).text("传统");
        $(btnSure).val("确 定");
        $(btnCancel).val("取 消");
        $(dialogTitle).text("更换皮肤");
    } else if(lang == 'zh_TW'){
        $(nodeSimple).text("簡約");
        $(nodeClassic).text("經典");
        $(nodeTradition).text("傳統");
        $(btnSure).val("確 定");
        $(btnCancel).val("取 消");
        $(dialogTitle).text("更換皮膚");
    }

}

function createjsfile(filename) {
		var fileref = document.createElement('script');
		fileref.setAttribute("type", "text/javascript");
		fileref.setAttribute("src", filename);
	return fileref;
}

function replacejsfile(oldfilename, newfilename, lang) {
	var allsuspects = document.getElementsByTagName("script");
	for (var i = allsuspects.length; i >= 0; i--) {
		if (allsuspects[i] && allsuspects[i].getAttribute("src") != null
				&& allsuspects[i].getAttribute("src").indexOf(oldfilename) != -1) {
			var nsrc = allsuspects[i].getAttribute("src").substring(0,allsuspects[i].getAttribute("src").indexOf(oldfilename)) + newfilename;
			var tailsrc = nsrc.substring(nsrc.lastIndexOf("/"));
			nsrc = nsrc.substring(0, nsrc.lastIndexOf("/"));
			nsrc = nsrc.substring(0, nsrc.lastIndexOf("/"));
			nsrc = nsrc + "/" + lang + tailsrc;
			var newelement = createjsfile(nsrc);
			allsuspects[i].parentNode.replaceChild(newelement, allsuspects[i]);
		}
	}
} 

function refreshSkin(){
	$("#skinlist").html('');
}
function refreshLanguage(){
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl + "/emp_tz.htm", 
		{
			method : "refreshLanguage",
			isAsync: "yes"
		},function(result) {
			 $("#lang").html(result);
		});
}
function refreshPersonSet(){
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl + "/emp_tz.htm", 
		{
			method : "refreshPersonSet",
			isAsync: "yes"
		},function(result) {
			$("#loginMesFloating").html(result);
		});
}
function refreshBalance(){
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl + "/emp_tz.htm", 
		{
		method : "refreshBalance",
		isAsync: "yes"
		},function(result) {
			$("#rNumFloating").html(result);
		});
}


function showImg(){
	var userid=userid,tkn=tkn;
	var path = $("#pathUrl").val();
	$.ajax({ 
        type : "post", 
        url : path+'/thirdMenu.htm?method=getPageJson',
        async : false,
        success : function(data){ 
			if(data == "outOfLogin")
			{
				location.href=path+"/common/logoutEmp.jsp";
				return;
			}
			data=eval('('+data+')');
			var thirdMenuList=removeDuplicates(data['thirdMenuList']);
			var templateArr=[],menuList=[],str="";
			var perCount=10,thirdMenuLength=thirdMenuList.length;
			var pageCount=Math.ceil(thirdMenuLength/perCount),indexCurrent,mouseTouch=false;
		//if(thirdMenuLength>=6){
			templateArr.push('<div class="touchslider touchslider-demo">');
			//templateArr.push('<div class="touchslider-viewport"><div style="width:10000px">');
			for(var i=0;i<thirdMenuLength;i++){
				var menuNum=thirdMenuList[i].menuNum,
					title=thirdMenuList[i].title;
				if(i%perCount==0){
					templateArr.push('<div class="touchslider-item">');
				}
				templateArr.push("<div class=\"menuDiv\" style='margin-bottom:0;'>");
				//新增在线客服url跳转判断
				if(urlRouter['flag'] && urlRouter['modId']==menuNum){
					templateArr.push("<a href=\"javascript:void(0)\" onclick=\"openServerUrl(\'"+userid+"\',\'"+tkn+"\',\'"+path+"\')\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">");
				}else{
					templateArr.push("<a href=\"javascript:doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">");
				}
				templateArr.push("<span>"+title+"</span>");
				templateArr.push("</a>");
				templateArr.push("</div>");
				if(i!=0 && i%perCount==(perCount-1)){
					templateArr.push('</div>');
				}
				//menuList.push("<li><a href=\"javascript:doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
				if(urlRouter['flag'] && urlRouter['modId']==menuNum){
					menuList.push("<li><a href=\"javascript:void(0)\" onclick=\"openServerUrl(\'"+userid+"\',\'"+tkn+"\',\'"+path+"\')\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
				}else{
					menuList.push("<li><a href=\"javascript:void(0)\" onclick=\"doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
				}
				
				
			}
			templateArr.push('</div>');
			templateArr.push('</div>');
//		$('#perCount').html(templateArr.join(''));
		//$(".touchslider-demo").touchSlider({mouseTouch: mouseTouch});
		$("#dd #uChildren").html(menuList.join(''));
			
        } 
	})
	
}

function removeDuplicates(myArray){
	var length=myArray.length;
	var arr=[],temp={};
	for(var i=0;i<length;i++){
		var key=JSON.stringify(myArray[i].menuNum);
		var value=myArray[i];
		if(temp[key]=== undefined){
			arr.push(value);
			temp[key]=1;
		}else{
			temp[key]++;
		}
	}
	return arr;
}

function checkM(){
	$("#button").attr("disabled",false);
	var patrnOph=/^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/; //可以“+”开头，除数字外，可含有“-”
	var patrnEMail=/^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
	var patrnMobile=/^[0-9]{11}$/;///^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8,16}$/; //必须以数字开头，除数字外，可含有“-”
	var patrnQQ=/^[0-9]*[1-9][0-9]*$/;
	var mobile=$("#mobile").val();
	var EMail=$("#EMail").val();
	var oph=$("#oph").val();
	var qq=$("#qq").val();
	$("#zhu").html("");

	if(!patrnEMail.exec(EMail)&&EMail.length!=0)
	{	
		$("#zhu").html(getJsLocaleMessage("common","common_frame2_main_35"));
		$("#button").attr("disabled","disabled");
		return false;
	}else if(!patrnOph.exec(oph)&&oph.length!=0)
	{
		$("#zhu").html(getJsLocaleMessage("common","common_frame2_main_36"));
		$("#button").attr("disabled","disabled");
		return false;
	}else if(qq.length!=0 && !patrnQQ.exec(qq)){
		$("#zhu").html(getJsLocaleMessage("common","common_frame2_main_37"));
		$("#button").attr("disabled","disabled");
		return false;
	}
}

function checkPass(agoPass)
{
	var pathUrl = $("#pathUrl").val();
	if(agoPass!="")
	{
		$.post(pathUrl+"/emp_tz.htm",{method:"checkPass",agoPass:agoPass},function(result)
			{
				if(result=="true")
				{
					$('#zhuPass').html("<font style='color:green'>"+getJsLocaleMessage("common","common_frame2_main_38")+"</font>");
				}else if(result=="false")
				{
					$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_39")+"</font>");
				}
			}
		);
	}else
	{
		$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_40")+"</font>");
	}
}
var isuppw = false;

function checkForm()
{
   if($("#updateinfoDiv").is(":hidden"))
   {
       checkForm2();
   }
   else
   {
       checkForm1();
   }
}

function checkForm1(){
	var mobile=$("#mobile").val();
	var EMail=$("#EMail").val();
	var oph=$("#oph").val();
	var qq=$("#qq").val();
	var pathUrl = $("#pathUrl").val();
	if(mobile.length==0){
         alert(getJsLocaleMessage("common","common_frame2_main_41"))
	}
	else if(!checkPhone(mobile))
	{
		alert(getJsLocaleMessage("common","common_frame2_main_42"));
	}
	else{
		$("#button").attr("disabled","disabled");
		$.post(pathUrl+"/emp_tz.htm", {
			mobile : mobile,
			method:"checkMobileByInfo",
			separator:";"
			
		},function(result)
		{
			if(result==0)
			{
				alert(getJsLocaleMessage("common","common_frame2_main_43"));
				document.modifyMobile.mobile.focus();
				$("#button").attr("disabled",false);
			}else if(result==2)
			{
				alert(getJsLocaleMessage("common","common_frame2_main_44"));
				$("#button").attr("disabled",false);
			}else
			{
				var username=$("#username").text();
				$.post(pathUrl+"/emp_tz.htm",{username:username,type:"phone",mobile:mobile,EMail:EMail,
				lgguid:$("#lgguid").val(),oph:oph,qq:qq,method:"update"},function(result){
					if(result=="true"){
						alert(getJsLocaleMessage("common","common_modifySucceed"));
						$("#button").attr("disabled",false);
						//doReset();
					}else{ 
						alert(getJsLocaleMessage("common","common_modifyFailed"));
						$("#button").attr("disabled",false);
						}
				});
			}
		});
		}
  }

function checkForm2(){
	var username=$("#username").text();
	var pass2=document.modifyPass.pass2.value;
	var newpass1=document.modifyPass.new_pass1.value;
	var newpass2=document.modifyPass.new_pass2.value;
	var pathUrl = $("#pathUrl").val();
	var zhu=$("#zhuPass").find("font").text();
	var reg =/[\d]/ ;//数字
	var reg1 =/[a-zA-Z]/ ;
	var reg2 =/\W/ ;
	if(zhu==getJsLocaleMessage("common","common_frame2_main_39"))
	{
		return;
	}
	if(pass2==""){ 
		$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_40")+"</font>");
	    return;
	}else if(newpass1==""){
	   $('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_45")+"</font>");
	   return;
	}else if(pass2==newpass1){
        $('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_46")+"</font>");
        document.modifyPass.new_pass1.value = '';
        document.modifyPass.new_pass2.value = '';
        document.modifyPass.new_pass1.focus();
        return;
    }else if(newpass2==""){
		 $('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_47")+"</font>");
		 return;
	}else if(newpass1!=newpass2){ 
		$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_48")+"</font>");
		document.modifyPass.new_pass2.focus();
		return;
	}else if(username == newpass1){ 
		$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_49")+"</font>");
		document.modifyPass.new_pass2.focus();
		return;
	}else{
		var LgGuid = $("#lgguid").val();
		var lgguid="";
		var flag="";
		$.post(pathUrl+"/emp_tz.htm",{lgguid:lgguid,method:"valid"},function(result){
			flag="true";
			var array=result.split('@');
			if(array[0] !="" && array[0]!=="0"){	
				var intpas=parseInt(array[0]);
				if(newpass1.length<intpas){
					//alert("密码设置不得少于"+intpas+"位  ");
					$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_50")+intpas+getJsLocaleMessage("common","common_frame2_main_51")+" </font>");
					flag="false";
					return;
				}
			} 
			if(array[1]!=''&&!reg.test(newpass1)&&array[2]!=''&&!reg1.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_52")+" </font>");
				flag="false";
				return;
				
			} 
			
			if(array[1]!=''&&!reg.test(newpass1)&&array[3]!=''&&!reg2.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_53")+" </font>");
				flag="false";
				return;
				
			}
			
			if(array[2]!=''&&!reg1.test(newpass1)&&array[3]!=''&&!reg2.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_54")+" </font>");
				flag="false";
				return;
				
			}
			
			if(array[1]!=''&&!reg.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_55")+" </font>");
				flag="false";
				return;
				
			} 
			if(array[2]!=''&&!reg1.test(newpass1)){//
				$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_56")+" </font>");
				flag="false";
				return;
				
			} 
			if(array[3]!=''&&!reg2.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>"+getJsLocaleMessage("common","common_frame2_main_57")+" </font>");
				flag="false";
				return;
				
			}  
			$('#zhuPass').html("");
			if($("#button").attr("disabled")==true){
				return;
			}
			$("#button").attr("disabled","disabled");
			if(flag=="true"){
			$.post(pathUrl+"/emp_tz.htm",{username:username,type:"pass",pass:newpass1,agoPass:pass2,method:"update",lgguid:LgGuid},function(result){
				if(result=="true"){
					alert(getJsLocaleMessage("common","common_modifySucceed"));
					document.modifyPass.pass2.value="";
					document.modifyPass.new_pass1.value="";
					document.modifyPass.new_pass2.value="";
					$('#zhuPass').html("");
					$("#button").attr("disabled","");
					isuppw = true;
					doReset();
				}else if(result=="false")
				{
					$("#button").attr("disabled","");
					alert(getJsLocaleMessage("common","common_modifyFailed"));
				}else if(result=="mid")
				{
					$("#button").attr("disabled","");
					alert(getJsLocaleMessage("common","common_frame2_main_58"));
					document.modifyPass.pass2.focus();
				}
			});
		}
		});

	
	}
}
//通过模块编码打开新的选项卡菜单
function showMenu(menucode)
{
	var $lf = $("iframe[name='mainFrame']").contents().find("iframe[name='I1']");
   	$lf[0].contentWindow.showTabMenClk(menucode);
   	$lf.contents().find("a[id='ak"+menucode+"']").trigger("click");
}

