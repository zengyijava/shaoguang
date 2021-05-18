$(document).ready(function() {
	if (aproinfo != null && aproinfo != "" && aproinfo == "over") 
	{
		alert("认证暂可使用" + validday + "天，为了保证系统正常使用，请获取认证！");
	}
	showImg();
	//var contents = $("#contents").html();
	//alert(contents);
	//window.parent.$("#uChildren").html(str);

});

function doOpen(priMenus) {
	var $par = $(window.parent.frames["topFrame"].document);
	$par.find("#onSys").attr("value", "2");
	location.href = iPath + "/middel.jsp?priMenus=" + priMenus;
}

function showImg(){
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
		if(thirdMenuLength>=6){
			templateArr.push('<div class="touchslider touchslider-demo">');
			templateArr.push('<div class="touchslider-viewport"><div style="width:10000px">');
			for(var i=0;i<thirdMenuLength;i++){
				var menuNum=thirdMenuList[i].menuNum,
					title=thirdMenuList[i].title;
				if(i%perCount==0){
					templateArr.push('<div class="touchslider-item">');
				}
				templateArr.push("<div class=\"menuDiv\">");
				templateArr.push("<a href=\"javascript:doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">");
				templateArr.push("<span>"+title+"</span>");
				templateArr.push("</a>");
				templateArr.push("</div>");
				if(i!=0 && i%perCount==(perCount-1)){
					templateArr.push('</div>');
				}
				//menuList.push("<li><a href=\"javascript:doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
				menuList.push("<li><a href=\"javascript:void(0)\" onclick=\"doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
				
			}
			templateArr.push('</div>');
			templateArr.push('</div></div>');
			if(pageCount>1){
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
			}
			templateArr.push('</div>');
		}else{
				templateArr=[];
				menuList=[];
				str="";
				templateArr.push("<center>");
				templateArr.push('<div style="width:'+thirdMenuList.length*198+'px;">');
				for(var i=0;i<thirdMenuList.length;i++){
					var menuNum=thirdMenuList[i].menuNum,
					title=thirdMenuList[i].title;
					templateArr.push("<div class=\"menuDiv\">");
					templateArr.push("<a href=\"javascript:doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">");
					templateArr.push("<span>"+title+"</span>");
					templateArr.push("</a>");
					templateArr.push("</div>");
					menuList.push("<li><a href=\"javascript:void(0)\" onclick=\"doOpen("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
				
				}
				templateArr.push("</div>");
				templateArr.push("</center>");
		}
		$('#perCount').html(templateArr.join(''));
		$(".touchslider-demo").touchSlider({mouseTouch: mouseTouch});
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