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
		/*所输入的内容中包含不允许的特殊字符(",',<,>)！*/
		alert(name+getJsLocaleMessage("common","common_js_common_1"));
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
		/*所输入的内容不能以"\"结尾！*/
		alert(name+getJsLocaleMessage("common","common_js_common_2"));
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
	$(".btnClass1,.btnClass2,.btnClass3,.btnClass4").hover(function() {
		var img=$(this).css("background-image");
		var isIE6 = (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
		if(isIE6)
		{
			$(this).css("background-image",img.replace("but-bg1","but-bg2"));
		}
		$(this).toggleClass("btnHover");
	}, function() {
		var isIE6 = (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
		if(isIE6)
		{
			$(this).attr("style","");
		}
		$(this).toggleClass("btnHover");
	});
	$(".btnClass5,.btnClass6").each(function(){
		$(this).val($(this).val().replaceAll(" ",""));
	});
	
	
	$(".btnClass5,.btnClass6").hover(function() {
		setPositionY(0,$(this));
		setPosition("-",$(this));
	}, function() {
		setPositionY(0,$(this));
	});
	
	if($("#hasBeenBind").val()!=1)
	{
		$("body").append("<input id='hasBeenBind' value='1' type='hidden'/>");
		$(".buttons a,.titletop_font").hover(function() {
			setPosition("-",$(this));
		}, function() {
			setPosition("+",$(this));
		});
		$(".buttons a,.titletop_font").mousedown(function() {
			setPosition("-",$(this));
			$(this).css("line-height","2.4em");
		});
		$(".buttons a,.titletop_font").mouseup(function() {
			setPosition("+",$(this));
			$(this).css("line-height","2.2em");
		});
	}
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
		var reg=/[#=$%@&*<>\^\?']/gi;                      
		if(reg.test(val)){
			$(this).val($(this).val().replace(reg,''));
		}
		
	}
	);
	deleteleftline();	
	
	$('#infoDiv').css({'background-color':'#fff'});
	/*返回上一级*/
	$(".titletop .titletop_font").text(getJsLocaleMessage("common","common_backToPreviousLevel"));
	/*返回*/
	$('.abs_right .titletop_font,.goback').text(getJsLocaleMessage("common","common_return"));
	
	//表格选中变色
	$('#content table tbody tr,#content tbody tr').click(function(){
		$(this).addClass('c_selectedBg').siblings().removeClass('c_selectedBg');
	})
	//
	$('.content_table tr').hover(function(){
		$(this).find('.setControl').hide();
		$(this).find('.c_selectBox').show();
	},function(){
		$(this).find('.setControl').show();
		$(this).find('.c_selectBox').hide();
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
		    	/*步骤名称只能由汉字、英文字母、数字、下划线组成！*/
				alert(getJsLocaleMessage("common","common_js_common_3"));
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
	var $pa = $(window.parent.parent.document);
	var pahtm = $pa.find("#loginparams").html();
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
//返回监控
function toMon()
{
	//alert($(window.parent.parent.document).find("iframe[name='mainFrame']").length);
	//window.parent.parent.openMon();
}

//手机号码输入控制
function phoneInputCtrl(value) {
	var val=value.val();
	var reg=/^(\+?)(\d*)$/g;	
	if(!reg.test(val)){
		var str=val.replace(/(\+?)(\d*)([^\d]*)/g,'$1$2');
		value.val(str);
	}
	else{
		var str=val;
		if(str.length>21){
			str=str.substr(0,21);
			value.val(str);
		}
	}
}
//号码校验
function checkPhone(phone)
{
	var phoneReg = /^1[0-9]{10}$/;
	if(phone==null||phone.length<7||phone.length>21){
		return false;
	}
	var reg = /^(\+|00)?(\d+)$/;
	var group = phone.match(reg);
	if(group!=null&&group.length>0){
		if(!group[1]){
			return phoneReg.test(group[2]);
		}else{
			if(/^86\d+/.test(group[2])){
				return phoneReg.test(group[2].substring(2));	
			}
		}
	}else{
		return false;
	}
	return true;
}

//异步校验手机号码合法性
function asyncCheckPhone(phone){
	var flag = false;
	$.ajax({
		type:"POST",
		async:false,
		url: "common.htm",
		data: {method:"filterPh",tmp:phone},
		success: function(result){
			if(result == 'true'){
				flag = true;
			}
		}
	});
	return flag;
}


//字符串过滤
//eg.  filterString("weqe%^*().")
function filterString(str){
	var reg=/[\|\&;\$%@'"\<\>\(\)\+\”\.]/g;
	if(reg.test(str)){
		str=str.replace(reg,'');
	}
	return str;
}

//特殊字符校验
function specialString(str){
	var reg=/[\|\&;\$%@'"\<\>\(\)\+\”\.]/g;
	if(reg.test(str)){
		return false;
	}
	return true;
}
//滑过显示下拉控件
function  openControlPanel(obj){
	$(obj).find('.setControl').hide();
	$(obj).find('.c_selectBox').show();
}
//滑出时隐藏下拉控件
function closeControlPanel(obj){
	$(obj).find('.setControl').show();
	$(obj).find('.c_selectBox').hide();
	
}
//参数格式替换
function replacer(match, p1, p2, p3, offset, string){
	return '#p_'+ p1+'#';
}
function replacerChange(match, p1, p2, p3, offset, string){
	/*参数*/
	return '{#'+getJsLocaleMessage("common","common_parameter")+ p1+'#}';
}
//获取文件类型
function getFileType(fileName){
	if(fileName){
		var arrFile=fileName.split('.');
		return arrFile[arrFile.length-1].toLowerCase();
	}
}
function getContentValX(obj){
	var msg = $(obj).val();
    var cs=getJsLocaleMessage("common","common_parameter");
    if(cs == '参数'){
        reg=/{#参数(.*?)#}/g;
    }else if(cs == '參數'){
        reg=/{#參數(.*?)#}/g;
    }else{
        reg=/{#Param(.*?)#}/g;
    }
	msg=msg.replace(reg,replacer);
	return msg;
}
function getChangeValX(obj){
	var msg = $(obj).val(),
		reg=/#p_(.*?)#/gi;
	msg=msg.replace(reg,replacerChange);
	return msg;
}
$(document).ready(function(){
	if(window.parent.complete){
		window.parent.complete();
	}
	window.isLoading = false;
})

//调用 loading层 设置页面 loading状态
function page_loading(){
    if(window.parent.loading){
        //设置当前页面为处理中 loading
        window.isLoading = true;
        window.parent.loading();
    }
}
//关闭loading层 设置页面 loading状态
function page_complete(){
    if(window.parent.complete){
        //设置当前页面为已完成
        window.isLoading = false;
        window.parent.complete();
    }
}

/**
 * 下载页面跳转调用
 * @param url
 */
function download_href(url){
	window.IE_UNLOAD = true;
	window.location.href = url;
}

//由于页面下载文件操作会触发跳转 而不加载具体页面 导致 出现加载层而无法关闭 因此 文件下载操作 不加载层
//监听页面跳转前的动作
window.onbeforeunload = function(){
	if(window.IE_UNLOAD){
		window.IE_UNLOAD = false;
	}else if(window.parent.loading){
		window.isLoading = true;
		var frame = window.frameElement;
		if(frame && $(frame).is(':visible')){
			window.parent.loading();
		}
	}
}
//阻止在IE下 A标签 触发onbeforeunload事件 导致页面出现加载层
if(document.all){
	$(document).ready(function(){
		if($('body').live) {//1.3~1.6
			$("a[href^='javascript:']").live("click", function () {
				window.IE_UNLOAD = true;
			});
		}else{//delegate 1.4.2新增
			$("body").delegate("a[href^='javascript:']","click",function(){
				window.IE_UNLOAD = true;
			});
		}
	})
}
//前端日志记录方法
window.EmpExecutionContext = {
	log:function(url,data,extinf){
		var info = url;
		if(data){
			var concatChar = url.indexOf('?')>-1?'&':'?';
			if(typeof data != 'string'){
				data = $.param(data);
			}
			info = url+concatChar+ data;
		}
		var topWin = topWindow();
		var path = topWin.getContextPath||'';
		if(path){
			path = path+'/';
		}
		$.post(path+'common.htm?method=log',{info:info,extinf:extinf});
	}
};
//返回最顶级window
function topWindow(){
	var maxdeep = 5;
	var parentWindow = window;
	while(parentWindow !== parentWindow.parent && maxdeep--){
		parentWindow = parentWindow.parent;
	}
	return parentWindow;
}