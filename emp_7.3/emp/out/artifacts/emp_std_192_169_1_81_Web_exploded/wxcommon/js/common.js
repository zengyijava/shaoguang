function init_set(path){
}

function numberControl(va)
{
	var pat=/^\d*$/;
	if(!pat.test(va.val()))
	{
		va.val(va.val().replace(/[^\d]/g,''));
	}
}
String.prototype.replaceAll  = function(s1,s2){    
       return this.replace(new RegExp(s1,"gm"),s2); 
    }
//no ''
function noyinhao(obj)
{  
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$(obj).keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = ((iKeyCode == 39)
				|| (iKeyCode == 34));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
}
//不可输入',",<,>
function noquot(obj)
{  
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$(obj).keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = ((iKeyCode == 39)
				|| (iKeyCode == 62)
				|| (iKeyCode == 34)
				||(iKeyCode == 60));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
}

function yinhao(name,str)
{
	if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})/.test(str)){
		alert(name+"所输入的内容中包含不允许的特殊字符(\",\',\<,\>)！");
	    return true;
	}
    return false;
}
 function replaceAll(src,newStr,oldStr){  
	     return src.replace(new RegExp(oldStr, "g"), newStr);  
	 }  
  function HtmlEncode(str){  
//	      str=replaceAll(str,"&amp;","&");  
	      str=replaceAll(str,"","<");  
	      str=replaceAll(str,"",">");  
	      str=replaceAll(str,"","'");  
	      str=replaceAll(str,"","\"");  
//	      str=replaceAll(str,"&#034;","\"");  
	      return str;  
	  }  
  
  function searchCheck(str)
{
	if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})/.test(str)){
	    return true;
	}
    return false;
}
  /* 查找时  把特殊字符(',",>,<)过滤
   * var paramJson={
   * params:["param1","param2","param2"..],//需要验证得text的Id集
   * form:"formId"//form表单Id
   * }
   * 
   */
 function submitCheck(paramJson){
	var paramArray=paramJson.params;	
	for(var i=0,len=paramArray.length;i<len;i++){
		var value=document.getElementById(paramArray[i]).value;
		if(searchCheck(value)){
			//document.getElementById(paramArray[i]).focus();
			document.getElementById(paramArray[i]).value=HtmlEncode(value);
			//return;		
		}
	}
	var form=$("#"+paramJson.form);
	form.submit();
}

function xiegang(name,str)
{
	if(/\\$/.test(str))
	{
		alert(name+"所输入的内容不能以\"\\\"结尾！");
		return true;
	}
	return false;
	}

function butg(id1,id2,id3)
{
	if(id1 != null && id1 != "")
	{
		$(id1).attr("disabled",true);
	}
	if(id2 != null && id2 != "")
	{
		$(id2).attr("disabled",true);
	}
	if(id3 != null && id3 != "")
	{
		$(id3).attr("disabled",true);
	}
}

function butk(id1,id2,id3)
{
	if(id1 != null && id1 != "")
	{
		$(id1).attr("disabled",false);
	}
	if(id2 != null && id2 != "")
	{
		$(id2).attr("disabled",false);
	}
	if(id3 != null && id3 != "")
	{
		$(id3).attr("disabled",false);
	}
}

//实时更新top页面的账号余额
function getCt()
{
}

$(document).ready(function() {
    if($(window.parent.document).find("#isMain").val()==1)
    {
        setTimeout("window.parent.reinitHeight()",200);
        $(window.parent.document).find("#loading").hide();
    }
    $("#condition #search").text("查询");
	var isIE = false;
	var isFF = false;
	var isSa = false;
	
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	
	$('#condition input[type="text"]').bind('keyup blur',function() {
		var val=$(this).val();
		var reg=/[#=$%@&*<>\/\^\?']/gi;                      
		if(reg.test(val)){
			$(this).val($(this).val().replace(reg,''));
		}
		
	}
	);
	$('#infoDiv').css({'background-color':'#fff'});
	$(".titletop .titletop_font").text("返回上一级");
	$('.abs_right .titletop_font,.goback').text("返回");
	
	//表格选中变色
	$('#content table tbody tr,#content tbody tr').click(function(){
		$(this).toggleClass('c_selectedBg').siblings().removeClass('c_selectedBg');
	})
	
	
});
function setPosition(ysfh,$a)
{
	var posy;
	var posx;
	var ahei =$a.css("height");
	ahei = parseInt(ahei.substr(0,ahei.indexOf("px")));
	if($a.css("background-position")==undefined)
	{
		posy=$a.css("background-position-y");
		posy = parseInt(posy.substr(0,posy.indexOf("px")));
		posx=$a.css("background-position-x");//TM的IE9要加上这行代码
		if(ysfh=="+")
		{
			$a.css("background-position-x",posx);//TM的IE9要加上这行代码
			$a.css("background-position-y",(posy-0+ahei)+"px");
		}else
		{
			$a.css("background-position-x",posx);//TM的IE9要加上这行代码
			$a.css("background-position-y",(posy-0-ahei)+"px");
		}
	}else
	{
		var poss = $a.css("background-position");
		var pi = poss.indexOf(" ");
		posx = poss.substr(0, pi);
		posy = poss.substr( pi+1);
		if(posy == "0%")
		{
			posy = "0px";
		}
		posy = parseInt(posy.substr(0,posy.indexOf("px")));
		if(ysfh=="+")
		{
			poss = posx + " "+(posy-0+ahei)+"px";
		}else
		{
			poss = posx + " "+(posy-0-ahei)+"px";
		}
		$a.css("background-position",poss);
	}
}
function setPositionY(hei,$a)
{
	var posy;
	var posx;
	if($a.css("background-position")==undefined)
	{
		posy=$a.css("background-position-y");
		posy = hei;
		posx=$a.css("background-position-x");//TM的IE9要加上这行代码
		$a.css("background-position-x",posx);//TM的IE9要加上这行代码
		$a.css("background-position-y",posy+"px");
	}else
	{
		var poss = $a.css("background-position");
		var pi = poss.indexOf(" ");
		posx = poss.substr(0, pi);
		posy = hei;
		poss = posx + " "+posy+"px";
		$a.css("background-position",poss);
	}
}
function deleteleftline()
{
	if(document.all)
	{
		var isIE6 = (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
		var isIE7 = (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0" ? true : false);

		if(isIE6 || isIE7)
		{
			$('#content thead tr').addClass('relat');
		}
	}
	//兼容搜狗浏览器的显示问题
	if($('#content tbody tr td').length == 1){
		$('#content tbody tr:last').before("<tr></tr>");
	}
	$('#content tbody tr:last').addClass('tbody_last');
	$('#content > tbody > tr').each(function(){$(this).find('td:last').addClass('no_r_b');}); 
	$('#content > tbody > tr').each(function(){$(this).find('td:first').addClass('no_l_b');});
	$('#content th:first').addClass('th_l_b');
	$('#content th:last').addClass('th_r_b');	
}

function deleteleftline1()
{
	//兼容搜狗浏览器的显示问题
	if($('#content tbody tr td').length == 1){
		$('#content tbody tr:last').before("<tr></tr>");
	}
	$('#content tbody tr:last').addClass('tbody_last');
	$('#content > tbody > tr').each(function(){$(this).find('td:last').addClass('no_r_b');}); 
	$('#content > tbody > tr').each(function(){$(this).find('td:first').addClass('no_l_b');});
	$('#content th:first').addClass('th_l_b');
	$('#content th:last').addClass('th_r_b');	
}

function controlInput(content,tip,isAlert){//content 需要限制输入的输入框内容;tip自定义提示信息(可以为null);isAlert是否需要alert弹出框(true需要)
	if(!content.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]+$/) )
	{
		if(isAlert){
		    if( tip == null)
		    {
				alert("步骤名称只能由汉字、英文字母、数字、下划线组成！");
		    }else{
			   alert(tip);
		    }
		    return false;
		}else{
			return false;
		}
	}else{
		return true;
	}
}

//web im 要求控制 
function hasSpecialChar(str){
	var reg = /[\*\:\?\<\>\|\&]+/;
	if(reg.test(str)){
		return true;
	}else{
		return false;
	}
	
}



function outSpecialChar(str){
	var reg = /[\*\_\#\:\?\<\>\|\&]+/;
	if(reg.test(str)){
		return true;
	}else{
		return false;
	}
	
}

function getLoginInfo(ids)
{
	var $pa = $(window.parent.document);
	var pahtm = $pa.find(".logininfo").html();
	$(ids).html(pahtm);
}

function backfind(ids)
{    	
		var conditionUrl = "";
		$(ids).find(" input").each(function(){
			conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
		});
		return conditionUrl;
}

//给树的弹出框加一个关闭按钮和关闭事件
function CloseTreePlugIN()
{
	//var htmlCloseTree="<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>"
	//$("table tr td .tree").before(htmlCloseTree);
	$(".closeDepTreeClassHover").hover(
		function(){
			$(this).addClass("closeDepTreeClass");
		},
		function(){
			$(this).removeClass("closeDepTreeClass");
		}
	);
	$(".closeDepTreeClassHover").click(
		function(){
			$(this).parent().css("display","none");
			$("select").css("visibility","");
	});
}

(function ($) {
	$.fn.closePlu = function(options) { //定义插件的名称，这里为userCp
		var dft = {
			CloseClick: function(){
				$(".closeDepTreeClassHover").parent().css("display","none");
				$("select").css("visibility","");
			}//显示之前执行的方法
		};
		var ops = $.extend(dft,options);
		$(".closeDepTreeClassHover").hover(
				function(){
					$(this).addClass("closeDepTreeClass");
				},
				function(){
					$(this).removeClass("closeDepTreeClass");
				}
			);
			$(".closeDepTreeClassHover").click(
				function(){
					dft.beforeShow.apply();
			});
	}
})(jQuery);

//关闭树的下拉框方法（点击别处时关闭）
function closeTreeFun(ids)
{
	for(var i=0;i<ids.length;i++)
	{
		$("#"+ids[i]).click(function(e){
			e.stopPropagation();
		});
	}
	
	$('html,body').click(function(e){
		var $obj=$(e.target);
        if($obj.attr("class").indexOf("treeInput")==-1){
        	for(var i=0;i<ids.length;i++)
        	{
        		$("#"+ids[i]).css("display","none");
        	}
      }
   });
}

//关闭树的下拉框方法（点击别处时关闭）+关闭时显示下拉框
function closeTreeFunSelCom(ids)
{
	for(var i=0;i<ids.length;i++)
	{
		$("#"+ids[i]).click(function(e){
			e.stopPropagation();
		});
	}
	
	$('html,body').click(function(e){
		var $obj=$(e.target);
        if($obj.attr("class").indexOf("treeInput")==-1){
        	for(var i=0;i<ids.length;i++)
        	{
        		$("#"+ids[i]).css("display","none");
        	}
        	$("select").css("visibility","");
      }
   });
}
//标签转义公共方法
function escapeString(str)
{
	str = str.replaceAll("&","&amp;");
	str = str.replaceAll("<","&lt;");
	str = str.replaceAll(">","&gt;");
	
	return str;
}
var aaa=0;
$(window).resize(function() {
    if($(window.parent.document).find("#isMain").val()==1)
    {
        setTimeout("window.parent.reinitHeight()",100);
    }
  });
/**
 * 刷新内容框架页面
 * @param url
 * @return
 */
function contReponse(url)
{
    $(window.parent.document).find('#mainFrame')[0].contentWindow.location.href = url;
}

/**
 * 共用的关闭框架页面
 * @return
 */
function closeDialog(){
    $(window.parent.document).find('#mainFrame')[0].contentWindow.dlog.close();
}

function doGo(url)
{
    $.post('frame.htm?method=checkLogin',{lguserid:$('#lguserid').val(),isAsync:'yes'},function(result){
        if(result == "outOfLogin" || result == "false")
        {
            window.parent.showLoginUrl(url);
        }else
        {
            location.href = url;
        }
    });
}

function doFrameGo(url,frameId)
{
    $.post('frame.htm?method=checkLogin',{lguserid:$('#lguserid').val(),isAsync:'yes'},function(result){
        if(result == "outOfLogin" || result == "false")
        {
            window.parent.showFrameUrl(url,frameId);
        }else
        {
            $(frameId).attr("src",url);
        }
    });
}

//列表页面表格列加上title
$("#content td").hover(function(){
	if($(this).find(".titleTip").size()>0){
		$(this).attr("title",$.trim($(this).find(".titleTip").text()));
	}
	},function(){
		$(this).removeAttr("title");
	}
);

var paddd=window.parent;
var docUrl = window.location.href;
var indexp=0;
while(paddd.location.href!=docUrl && indexp<6)
{
    docUrl=paddd.location.href;
    paddd=paddd.parent;
    indexp=indexp+1;
}
// 统一调用的alert框
//window.alert=function(mess)
//{
//    paddd.nalert(mess);
//}