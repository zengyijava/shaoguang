$(document).ready(function() {
	if (aproinfo != null && aproinfo != "" && aproinfo == "over") 
	{
		alert(getJsLocaleMessage("common","common_frame2_index_1")+validday+getJsLocaleMessage("common","common_frame2_index_2"));
	}
	showImg();
	//var contents = $("#contents").html();
	//alert(contents);
	//window.parent.$("#uChildren").html(str);

});

function doOpen(priMenus) {
	//记录操作记录
	$('#menu', window.parent.document).html(priMenus);
	$('#menuInfo', window.parent.document).html('');
	
	var $par = $(window.parent.frames["topFrame"].document);
	$par.find("#onSys").attr("value", "2");
	location.href = iPath + "/middel.jsp?priMenus=" + priMenus;
}

function showImg(){
	var userid=urlRouter.userid,tkn=urlRouter.tkn;
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
			//templateArr.push('</div></div>');
			/*if(pageCount>1){
				mouseTouch=true;
				templateArr.push('<div class="touchslider-nav">');
				templateArr.push('<a class="touchslider-prev png"></a>');
				templateArr.push('<a class="touchslider-next png"></a>');
				for(var i=0;i<pageCount;i++){
					if(i==0){
						indexCurrent='touchslider-nav-item-current';
					}else{
						indexCurrent='';
					}
					templateArr.push('<a class="touchslider-nav-item '+indexCurrent+'">'+(i+1)+'</a>');
				}
			}*/
			templateArr.push('</div>');
		//}else{
				/*templateArr=[];
				menuList=[];
				str="";
				templateArr.push("<center>");
				templateArr.push('<div style="width:'+thirdMenuList.length*198+'px;">');
				for(var i=0;i<thirdMenuList.length;i++){
					var menuNum=thirdMenuList[i].menuNum,
					title=thirdMenuList[i].title;
					templateArr.push("<div class=\"menuDiv\">");
					if(urlRouter['flag'] && urlRouter['modId']==menuNum){
						
						templateArr.push('<a href="javascript:void(0)" onclick="openServerUrl(\''+userid+'\',\''+tkn+'\',\''+path+'\')" id="mod_style_"'+menuNum+'" class="act_btn">');
					}else{
						templateArr.push("<a href=\"javascript:doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">");
					}
					templateArr.push("<span>"+title+"</span>");
					templateArr.push("</a>");
					templateArr.push("</div>");
					if(urlRouter['flag'] && urlRouter['modId']==menuNum){
						menuList.push('<li><a href="javascript:void(0)" onclick="openServerUrl(\''+userid+'\',\''+tkn+'\',\''+path+'\')" id="mod_style_"'+menuNum+'" class="act_btn">'+title+'</a></li>');
					
					}else{
						menuList.push("<li><a href=\"javascript:void(0)\" onclick=\"doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
					}
				}
				templateArr.push("</div>");
				templateArr.push("</center>");*/
		//}
		$('#perCount').html(templateArr.join(''));
		//$(".touchslider-demo").touchSlider({mouseTouch: mouseTouch});
		window.parent.$("#uChildren").html(menuList.join(''));
			
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

function openServerUrl(userid,tkn,path){
	//在线坐席 对不兼容浏览器的处理
	if(!isBrowserOk()){
		return;
	}
	if(tkn){
		ajaxOnline(userid,tkn,path);
	}else{
		tkn=getField('#appTkn');
		ajaxOnline(userid,tkn,path);
	}
	
}

function ajaxOnline(userid,tkn,path){
	$.ajax({
			type:'GET',
			url:path+'/customChat.htm?method=checkUser',
			data:'userid='+userid+'&tkn='+tkn+'&isAsync=yes',
			success:function(data){
				if(data == "outOfLogin")
				{
					location.href=path+"/common/logoutEmp.jsp";
					return;
				}
	
				var data=eval('('+data+')');
				if(data['aid']==0){
					alert(data['msg']);
					return false;
				}else{
					window.open(path+'/'+data['url']);
				}
			}
		});
}
function getField(obj){
	return $(window.parent.frames['topFrame'].document).find(obj).val();
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
