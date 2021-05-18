var total=0;// 总页数
var pageIndex=0;// 当前页数
var pageSize=0;// 每页记录数
var totalRec=0;// 总记录数
function initPage(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total=total;
	this.pageIndex=pageIndex;
	this.pageSize=pageSize;
	this.totalRec=totalRec;
	
	if(CstlyeSkin.indexOf("frame4.0") != -1) {
		PageInfo.initPage(total,pageIndex,pageSize,totalRec);	// 新分页功能
	} else {
		showPageInfo();  //旧分页功能
		
	}
}

//跳到第几页
function showPageInfo(){
	var $pa = $('#pageInfo');
	$pa.empty();
	
	var tz=getJsLocaleMessage("common","common_skip");
	
	$pa.append('<span id="p_goto" onclick="jumpMenu()" style="text-align: left;" ><font style="padding-left:5px;width:100%;color:#000;line-height:24px;">'+tz+'</font></span>');
	//$pa.append('<span id="p_goto" onclick="jumpMenu()"></span>');
	if(pageIndex<total){
		$pa.append('<a class="p_a_h" id="p_last" href="javascript:goPage('+total+')"></a>');
		$pa.append('<a id="p_next" href="javascript:goPage('+(pageIndex-0+1)+')" class="p_a_h" ></a>');
	}else{
		$pa.append('<a id="p_last" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_next" href="javascript:void(0)"></a>');
	}
	if(pageIndex<=1){
		$pa.append('<a id="p_back" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_first" href="javascript:void(0)"></a>');
	}else{
		$pa.append('<a class="p_a_h" id="p_back" href="javascript:goPage('+(pageIndex-1)+')"></a>');
		$pa.append('<a class="p_a_h" id="p_first" href="javascript:goPage(1)"></a>');
	}
	
	$pa.append('<span id="p_info">'+getJsLocaleMessage("common","common_all")+totalRec+getJsLocaleMessage("common","common_js_pageInfo_10")+pageIndex+'/'+total+getJsLocaleMessage("common","common_page_p")+pageSize+getJsLocaleMessage("common","common_js_pageInfo_8")+'</span>');
	$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
	$pa.append('<input type="hidden" name="totalPage" id="totalPage" type="text" value="'+total+'" />');
	$pa.append('<input type="hidden" name="totalRec" id="totalRec" type="text" value="'+totalRec+'" />');
	
	var newDiv = document.createElement("div");
	newDiv.id = "p_jump_menu";
	newDiv.style.position = "absolute";
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.zIndex = "100";
	newDiv.style.display = "none";
	newDiv.className="div_bd div_bg";
	newDiv.innerHTML = "<center><div>"+getJsLocaleMessage("common","common_js_pageInfo_11")+"<input name='page_input' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />"+getJsLocaleMessage("common","common_page_p") +
		"<input name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />"+getJsLocaleMessage("common","common_js_pageInfo_8")+"</div>" +
		"<div><a id='p_jump_sure' href='javascript:jumpSure()' ></a></div></center><input type='hidden' id='isHd'/>";
	document.body.appendChild(newDiv);
	
}
function jumpMenu()
{
	
	var bodyheight=$(window).height();
	var top = $("#p_goto").offset().top;
	var left = $("#p_goto").offset().left;
	left = left - $("#p_jump_menu").width()+$("#p_goto").width();
	if(top+84 > bodyheight)
	{
		top = top - $("#p_jump_menu").height()-4;
	}else
	{
		top = top + $("#p_goto").height()+4;
	}
	
	$("#p_jump_menu").css("left",left+"px")
	$("#p_jump_menu").css("top",top+"px")
	$("#p_jump_menu").show();
	$("#page_input").focus();
	//修改确认
	var confirm=getJsLocaleMessage("common","common_confirm");;;
	$("#p_jump_sure").html('<font style="color:#000;">'+confirm+'</font>');
}
function jumpDisplay(ddd)
{
	$("#isHd").val(ddd);
	setTimeout("isHid()",200);
}
function isHid()
{
	if($("#isHd").val() == 0)
	{
		 $("#p_jump_menu").hide();
	}
}
function jumpSure()
{
	var page=$("#page_input").val();
	var size=$("#size_input").val();
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total;
	if(page-0>pagetotal){
		alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
		//document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input").focus();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert(getJsLocaleMessage("common","common_js_pageInfo_4"));
		$("#page_input").focus();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
		alert(getJsLocaleMessage("common","common_js_pageInfo_1"));
		$("#size_input").focus();
		return;
	}
	if(size>500)
	{
		alert(getJsLocaleMessage("common","common_js_pageInfo_2"));
		$("#size_input").focus();
		return;
	}
	$("#pageSize").val($("#size_input").val());
	$("#txtPage").val($("#page_input").val());
	submitForm();
}

function goPage(i){
	if(i<0){
		page=$("#txtPage").attr("value");
	}else{
		page=i;
	}
	//var page=$("#txtPage").attr("value");
	var size=$('#pageSize').attr("value");
	var checkPage=/^\d+$/;//正则表达示验证数字
	var pagetotal=total;
	if(size<1 ||  !checkPage.test(size) ){
		alert(getJsLocaleMessage("common","common_js_pageInfo_1"));
		return;
	}
	if(size>500)
	{
		alert(getJsLocaleMessage("common","common_js_pageInfo_2"));
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm'].elements["pageIndex"].value =1;
		submitForm();
		return;
	}
	if(page-0>pagetotal){
		alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
		document.forms['pageForm'].elements["pageIndex"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert(getJsLocaleMessage("common","common_js_pageInfo_4"));
		return;
	}
	// alert(document.forms['pageForm'].elements["pageIndex"].value+"DD");
	 document.getElementById("txtPage").value = page;
	//document.forms['pageForm'].elements["pageIndex"].value=page;
	submitForm();
}

//点击页数
//表单提交
function pageClick(i){
				document.forms['pageForm'].elements["pageIndex"].value =i;
				submitForm();
			}
			
//向前向后翻页
function page(method){
				var pageIndex=$("#pageIndex").attr("value");
				if(method=="back"){
					pageIndex=(Math.ceil(pageIndex/5)-1)*5;
				}
				else{
					pageIndex=(Math.ceil(pageIndex/5))*5+1;
				}
				document.forms['pageForm'].elements["pageIndex"].value =pageIndex;
				submitForm();
			}


var aaaaa = 0 ;
function setLeftHeight()
{
	setLeftHeight2();
	window.onresize = function(){  
		if(aaaaa<1)
		{
			aaaaa=aaaaa+1;
		}else
		{
			aaaaa=0;
			return;
		}
		setLeftHeight2();
		setTimeout('aaaaa=0;',300);
	}
}

function setLeftHeight2()
{
	//var isIE = (document.all) ? true : false;
	//var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	var hei=$(window).height();
	var bodyhei = $('.right_info').height();
	var tophei = $('.top:visible').height();
	var topMargin = $('.top:visible').css('margin-top'); 
	if(tophei==null)
	{
		tophei = 0;
	}
	if(topMargin != null)
	{
		tophei = tophei + 5;
	}
	if(bodyhei > hei)
	{
		hei = bodyhei;
		$('.left_dep').css('height',hei-tophei);
		$('.left_dep .list ').css('height',hei-50-tophei);
	}else
	{
		$('.left_dep').css('height',hei-20-tophei);
		$('.left_dep .list ').css('height',hei-70-tophei);
	}
}

var PageInfo = (function(){
//pageSize=15&pageIndex=1&totalPage=1&totalRec=7&needNewData=1
var page = {
		pageSize : 10, //每页条数
		pageIndex : 0, //当前页数
		totalRec : 0, //总记录数
		total : 0 ,//总页数
		initSelect : [5,10,15,20,50], //设置N条/页
		limit : 5
		};
function init(){  //初始化分页
	var $pageWrapper = $("#pageInfo");
	
	$pageWrapper.append('<div id="page_new"><ul class="page_ul">' +
			'<li class="priv_page page_li_hb"></li>' +
			'<li class="next_page page_li_hb"></li>' +
			'<li class="page_select page_li_select_hb">' +
			'</li>' +
			'<li class="normal_text_li">' +
				'<span class="normal_text">跳至</span>' +
			'</li>' +
			'<li class="page_index_input page_li_hb">' +
				'<input type="text" id="page_input_value">' +
			'</li>' +
			'<li class="normal_text_li">' +
				'<span class="normal_text">页</span>' +
			'</li></ul></div>');
	var initSelect =  page.initSelect;
	for(var i = 0; i < initSelect.length; i++) {
		var pageSelectDiv = "";
		if(i == 0) {
			pageSelectDiv = '<div class="page_size_unselect page_size_first" pageSize="'  + initSelect[i] + '">' +
			'<span class="page_select_text">' + initSelect[i] + '条/页</span></div>';
		} else if(i ==  initSelect.length - 1) {
			pageSelectDiv = '<div class="page_size_unselect page_size_last" pageSize="'  + initSelect[i] + '">' +
			'<span class="page_select_text">' + initSelect[i] + '条/页</span></div>';
		} else{
			pageSelectDiv = '<div class="page_size_unselect" pageSize="'  + initSelect[i] + '">' +
			'<span class="page_select_text">' + initSelect[i] + '条/页</span></div>';
		}
		$("#pageInfo #page_new .page_ul .page_select").append(pageSelectDiv);	
	}

	$pageWrapper.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pageWrapper.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
	$pageWrapper.append('<input type="hidden" name="totalPage" id="totalPage" type="text" value="'+total+'" />');
	$pageWrapper.append('<input type="hidden" name="totalRec" id="totalRec" type="text" value="'+totalRec+'" />');
	
	if(page.total > page.limit) { //超过页数限制，需要以...表示
		var halfLimit = Math.floor(page.limit/2);
		
		if(page.pageIndex - 2 <= halfLimit) {  //当前页位于左侧
			for(var i = 1; i <= page.limit; i++) {
				var pageCurrent = "";
				if(i==page.pageIndex) pageCurrent = " page_curr";
				var li = '<li class="page_index page_li_hb ' + pageCurrent +'" pageIndex="' + i +'">' + i + '</li>';
				$(li).insertBefore($("#page_new .page_ul .next_page"));
			}
			var li = '<li class="normal_text_li">...</li>';
			$(li).insertBefore($("#page_new .page_ul .next_page"));
			
			li = '<li class="page_index page_li_hb ' + page.total +'" pageIndex="' + page.total +'">' + page.total + '</li>';
			$(li).insertBefore($("#page_new .page_ul .next_page"));
		} else if(page.total - 1 - pageIndex <= halfLimit) { //当前页接近右侧
			var li = '<li class="page_index page_li_hb ' + 1 +'" pageIndex="' + 1 +'">' + 1 + '</li>';
			$(li).insertBefore($("#page_new .page_ul .next_page"));
			
			li = '<li class="normal_text_li">...</li>';
			$(li).insertBefore($("#page_new .page_ul .next_page"));
			
			
			for(var i = page.total - page.limit + 1; i <= page.total; i++) {  
				var pageCurrent = "";
				if(i==page.pageIndex) pageCurrent = " page_curr";
				var li = '<li class="page_index page_li_hb ' + pageCurrent +'" pageIndex="' + i +'">' + i + '</li>';
				$(li).insertBefore($("#page_new .page_ul .next_page"));
			}
			
		} else {
			for(var i = page.pageIndex - halfLimit; i <= page.pageIndex + halfLimit; i++) { //当前页位于中间
				var pageCurrent = "";
				if(i==page.pageIndex) pageCurrent = " page_curr";
				var li = '<li class="page_index page_li_hb ' + pageCurrent +'" pageIndex="' + i +'">' + i + '</li>';
				$(li).insertBefore($("#page_new .page_ul .next_page"));
			}
			var li = '<li class="normal_text_li">...</li>';
			$(li).insertBefore($("#page_new .page_ul .next_page"));
			
			li = '<li class="page_index page_li_hb ' + page.total +'" pageIndex="' + page.total +'">' + page.total + '</li>';
			$(li).insertBefore($("#page_new .page_ul .next_page"));
		}
	} else {
		for(var i = 1; i <= page.total; i++) {
			var pageCurrent = "";
			if(i==page.pageIndex) pageCurrent = " page_curr";
			var li = '<li class="page_index page_li_hb ' + pageCurrent +'" pageIndex="' + i +'">' + i + '</li>';
			$(li).insertBefore($("#page_new .page_ul .next_page"));
		}
	}
	var $pageCurr = $(".page_size_unselect[pageSize=" + page.pageSize +"]");
	
	if($pageCurr.length > 0) {
 		$pageCurr.addClass("page_size_selected");
		$pageCurr.removeClass("page_size_unselect");
	} else {
 		$(".page_size_first").addClass("page_size_selected");
		$(".page_size_first").removeClass("page_size_unselect");
	}
	$(".page_index").click(function(){  
		var newPage = $(this).attr("pageIndex");
		jumpTo(newPage);
	});
	
	$(".priv_page").click(function(){
		privPage();
	});
	$(".next_page").click(function(){
		nextPage();
	});
	
	$(".page_size_selected").live("click",function(){
		$(".page_size_selected").addClass("page_size_unselect");
		$(".page_size_selected").removeClass("page_size_selected");
		
		$(".page_select").removeClass("page_li_select_hb");
		
		$(".page_size_unselect").show(400,"swing");
	});
	
	$(".page_li_hb,.page_li_select_hb").live("mouseenter",function(){
		$(this).addClass("page_li_hb_border");
	});
	
	$(".page_li_select_hb").live("mouseleave",function(){
		$(this).removeClass("page_li_hb_border");
	});
	
	$(".page_li_hb").live("mouseleave",function(){
		$(this).removeClass("page_li_hb_border");
	});
	
	$(".page_size_unselect").live("click",function(){
		$(this).addClass("page_size_selected");
		$(this).removeClass("page_size_unselect");
		$(".page_size_unselect").hide(400,"linear");
		
		var size = $(this).attr("pageSize");													
		var currSize = $('#pageSize').val();
		if(size != currSize) {
			$("#txtPage").attr("value",1); //调整页面大小默认切换到第一页
			$("#pageSize").val(size);
			//page_loading();
			submitForm();
		} else {
			$(".page_select").addClass("page_li_select_hb");
			$(".page_size_first").removeClass("page_select_hover");
		}													
	});
	
	$(".page_size_unselect").live("mouseenter",function(){
		$(this).addClass("page_select_hover");
	});
	$(".page_size_unselect").live("mouseleave",function(){
		$(this).removeClass("page_select_hover");
	});
	
	$("#page_input_value").bind("keypress",function(event){
		if(event.keyCode == 13){
			var inputValue = $(this).val();
			var regex = new RegExp("^[0-9]*[1-9][0-9]*$");
			if(regex.test(inputValue)) {
				jumpTo(parseInt(inputValue));
			} else {
				window.alert("请输入正确的页码！");
			}
		}
	});
	
	//如果是最后一页或者第一页则禁用上一页下一页
	if(page.pageIndex == 1) {
		$(".priv_page").addClass("priv_page_forbidden");
		$(".priv_page").removeClass("page_li_hb");
	}
	if(page.pageIndex == page.total) {
		$(".next_page").addClass("next_page_forbidden");
		$(".next_page").removeClass("page_li_hb");
	}
}
function nextPage() { //查看下一页
	if(page.pageIndex < page.total) {
		page.pageIndex++;
		jumpTo(page.pageIndex);
	} 
}
function privPage() { //查看上一页
	if(page.pageIndex > 1) {
		page.pageIndex--;
		jumpTo(page.pageIndex);
	}
}
function jumpTo(newPage) { //跳转到指定页
	if((newPage <= page.total) && (newPage >= 1)) {
		page.pageIndex = newPage;
		$(".page_index").removeClass("page_curr");
		$(".page_index:eq(" + (newPage - 1) + ")").addClass("page_curr");
		
		goPage(newPage);
	}  else {
		window.alert("请输入介于1~"+ page.total + "的页码");
	}
}
function reload(newtotal) { //重置总记录数
	 reload(newtotal);
}
function alterPageSize(newSize) { //修改每页显示条数
	alterPageSize(newSize);
}

return {
	init : function(){//默认参数分页
		init();
	}, 
	initPage : function(total,pageIndex,pageSize,totalRec) { //指定参数进行分页
		page.total = total;
		page.pageIndex = pageIndex;
		page.pageSize = pageSize;
		page.totalRec = totalRec;
		init();
	},
	nextPage : function() { //查看下一页
		nextPage();
	},
	privPage : function() { //查看上一页
		privPage();
	},
	jumpTo : function(newPage) { //跳转到指定页
		jumpTo(newPage);
	},
	reload : function(newtotal) { //重置总记录数
		reload(newtotal);
	},
	alterPageSize : function(newSize) { //修改每页显示条数
			alterPageSize(newSize);
		}
	};
}());