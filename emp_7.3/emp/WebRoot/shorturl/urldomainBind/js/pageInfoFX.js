var total=0;// 总页数
var pageIndex=0;// 当前页数
var pageSize=0;// 每页记录数
var totalRec=0;// 总记录数
var needNewData = 1;//是否需要最新分页
function initPage(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total=parseInt(total);
	this.pageIndex=parseInt(pageIndex);
	this.pageSize=(pageSize);
	this.totalRec=parseInt(totalRec);
	
	showPageInfo();
}

function initPageSyn(total,pageIndex, pageSize, totalRec, needNewData, isFirstEnter, url){// 初始化页面
	this.total=parseInt(total);
	this.pageIndex=parseInt(pageIndex);
	this.pageSize=parseInt(pageSize);
	this.totalRec=parseInt(totalRec);
	this.needNewData=parseInt(needNewData);
	synPageInfoShow(isFirstEnter,url);
}

//跳到第几页
//表单提交
function goPage(i){
	var page;
	var pageIndex = "";
	var pageSize = "";
	if(i<0){
		page=$("#txtPage").attr("value");
	}else{
		page=i;
	}
	var size=$('#pageSize').attr("value");
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total;
	if(size<1 ||  !checkPage.test(size) ){
		/*每页显示数量必须为一个大于0的整数！*/
		alert(getJsLocaleMessage("common","common_js_pageInfo_1"));
		return;
	}
	if(size>500)
	{
		/*每页显示数量不能大于500！*/
		alert(getJsLocaleMessage("common","common_js_pageInfo_2"));
		return;
	}
	//设置为不需要重新查分页信息
	document.forms['form4'].elements["needNewData"].value =2;
	if(page==null || page==""){
		page_loading();
		document.forms['form4'].elements["pageIndex"].value =1;
		//document.forms['form4'].submit();
		debugger;
		pageIndex = 1;
		pageSize = $('#pageSize').val();
		window.location.href="surl_bind.htm?method=toAdd&pageIndex="+pageIndex+"&pageSize="+pageSize;
		return;
	}
	if(page-0>pagetotal){
		/*输入页数大于最大页数！*/
		alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
		document.forms['form4'].elements["pageIndex"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		/*跳转页必须为一个大于0的整数！*/
		alert(getJsLocaleMessage("common","common_js_pageInfo_4"));
		return;
	}
	page_loading();
	document.forms['form4'].elements["pageIndex"].value=page;
	//document.forms['form4'].submit();
	debugger;
	pageIndex = page;
	pageSize = $('#pageSize').val();
	window.location.href="surl_bind.htm?method=toAdd&pageIndex="+pageIndex+"&pageSize="+pageSize;
}

function submitForm(){
	
	page_loading();
	document.forms['form4'].elements["pageIndex"].value =1;	// 回到第一页
	document.forms['form4'].elements["needNewData"].value =1;	// 需要查新总数
	
	var pageIndex = 1;
	//document.forms['form4'].submit();
	debugger;
	window.location.href="surl_bind.htm?method=toAdd&pageIndex="+pageIndex;
}

function showPageInfo(){
	var $pa = $('#pageInfo');
	$pa.empty();
	var tz='跳转';
	
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
	/*共  条，第   页， 条/页  */
	$pa.append('<span id="p_info">'+'共'+totalRec+'条，第'+pageIndex+'/'+total+'页，'+pageSize+'条/页'+'</span>');
	$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
	$pa.append('<input type="hidden" name="totalPage" id="totalPage" type="text" value="'+total+'" />');
	$pa.append('<input type="hidden" name="totalRec" id="totalRec" type="text" value="'+totalRec+'" />');
	$pa.append('<input type="hidden" name="needNewData" id="needNewData" type="text" value="'+needNewData+'" />');
	
	
	var newDiv = document.createElement("div");
	newDiv.id = "p_jump_menu";
	newDiv.style.position = "absolute";
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.display = "none";
	newDiv.style.zIndex = "100";
	newDiv.className="div_bd div_bg";
    /*如果为英文增加宽度*/
    var empLangName = getJsLocaleMessage("common","common_empLangName");
    if("zh_HK" === empLangName){
        newDiv.style.width="200px";
    }
	/*第 页，条/页 */
	newDiv.innerHTML = "<center><div>"+'第'+"<input name='page_input' class='input_bd' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />"+'页' +
		"<input  class='input_bd' name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />"+'条/页'+"</div>" +
		"<div><a id='p_jump_sure' href='javascript:jumpSure()' ></a></div></center><input type='hidden' id='isHd'/>";
	document.body.appendChild(newDiv);
	/*$pa.append('&nbsp;');
	$pa.append('每页').append('<input name="pageSize" style="height: 15px;" id="pageSize" type="text" value="'+pageSize+'" size="4" />').append('条，');
	$pa.append('转到第').append('<input name="pageIndex" style="height: 15px;" id="txtPage" type="text" value="'+pageIndex+'" size="4" />').append('页');
	$pa.append('&nbsp;');
	$pa.append('<a href="javascript:goPage(-1)"><img src="website/images/go.gif" id="goPage" width="26" height="15" /></a>');*/
}


function getJsLocaleMessage(fileName, key) {
	try {
		return eval(fileName)[key];
	} catch (e) {
		return undefined;
	}
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
	var confirm='确认';
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
        /*输入页数大于最大页数！*/
        alert('输入页数大于最大页数！');
		//document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input").focus();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
        /*跳转页必须为一个大于0的整数！*/
        alert('跳转页必须为一个大于0的整数！');
		$("#page_input").focus();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
        /*每页显示数量必须为一个大于0的整数！*/
        alert('每页显示数量必须为一个大于0的整数！');
		$("#size_input").focus();
		return;
	}
	if(size>500)
	{
        /*每页显示数量不能大于500！*/
        alert('每页显示数量不能大于500！');
		$("#size_input").focus();
		return;
	}
	
	var currSize=$('#pageSize').val();
	if(currSize != size)
	{
		//如果改变了每页显示数量，则要重新查分页信息
		document.forms['form4'].elements["needNewData"].value = 1;
	}
	else
	{
		//设置为不需要重新查分页信息
		document.forms['form4'].elements["needNewData"].value = 2;
	}
	
	page_loading();
	$("#pageSize").val($("#size_input").val());
	$("#txtPage").val($("#page_input").val());
	//document.forms['form4'].submit();
	debugger;
	var pageSize = $("#pageSize").val();
	window.location.href="surl_bind.htm?method=toAdd&pageSize="+pageSize;
}
var ie4;
var firefox;
if (document.all){
	ie4 = true;
}
else{
	ie4 = false; // 判断是否Ie4
}
// 设置键盘事件函数
function checkPageIndex(event,text) {
	var key;
	if (ie4){
		key = event.keyCode;
		// Ie4使用event.keyCode获取键盘码
	}else{
		// key = checkPageIndex.arguments[0].keyCode;
	// FireFox使用我们定义的键盘函数的arguments[0].keyCode来获取键盘码
		key = event.which;
	}
	// alert(key);
	if (!(key >= 48 && key <= 57)) {
		if (ie4){
			event.keyCode   =   0;
			event.returnValue   =   false; 
		}else{
			if(key == 8 ){
				return true;
			}
			return false;
		}
	}
}


function showPages(){// 显示页数
	// var i=((pageIndex-(pageIndex%5))/5)*5+1;
	var i=(Math.ceil(pageIndex/5)-1)*5+1;
	$("#Flexigrid_Bar").html("");	// 初始化标签
	if(i==1){	// 如果当前为第一页，则不能向前翻页
		/*上5页*/
		$("#Flexigrid_Bar").append("<span>&lt;&lt;"+getJsLocaleMessage("common","common_js_pageInfo_6")+"</span>");
	}
	else{
        /*上5页*/
		$("#Flexigrid_Bar").append("<a href="+"\"javascript:page('back')\">&lt;&lt;"+getJsLocaleMessage("common","common_js_pageInfo_6")+"</a>");
	}
	$("#Flexigrid_Bar").append("&nbsp");
	var n=5;
	while(n>0){
		if(i==pageIndex){// 如果为当前页
			$("#Flexigrid_Bar").append("<span>"+i+"</span>");
		}
		else{
			$("#Flexigrid_Bar").append("<a href='javascript:pageClick("+i+")'>"+i+"</a>");
		}
		$("#Flexigrid_Bar").append("&nbsp");
		if(i>=total)break;	// 如果达到最后一页了,刚跳出
		i=i+1;
		n=n-1;
	}

	if(((Math.ceil(pageIndex/5))*5)<total){// 如果
		/*下5页*/
		$("#Flexigrid_Bar").append("<a href="+"\"javascript:page('front')\">&gt;&gt;"+getJsLocaleMessage("common","common_js_pageInfo_7")+"</a>");
	}
	else{
		/*下5页*/
		$("#Flexigrid_Bar").append("<span>"+getJsLocaleMessage("common","common_js_pageInfo_6")+"&gt;&gt;</span>");
	}
	$("#Flexigrid_Bar").append("&nbsp");
}

// 显示每页记录数按钮
// javascript:changeCount(),此函数为点击事件。
function showPageSize(){
	var num=10;
	$("#setupNum").html("");// 初始化
	/*每页显示数量:*/
	$("#setupNum").append("<span>"+getJsLocaleMessage("common","common_js_pageInfo_5")+"</span>");
    $("#setupNum").append("<input  onkeyup=\"value=value.replace(/[^\\d]/g,\'\')\"  onkeypress='javascript:return checkPageIndex(event,this);'  maxlength=\"3\" type=\"text\" id='pageSize' name=\"pageSize\" value=\""+pageSize+"\"/>"+getJsLocaleMessage("common","common_js_pageInfo_9"));
    // 显示“共几页，几条”
    $("#AllPage").html("");
    /*共 页/共 条*/
    $("#AllPage").append(getJsLocaleMessage("common","common_all")+total+getJsLocaleMessage("common","common_page")+"/"+getJsLocaleMessage("common","common_all")+totalRec+getJsLocaleMessage("common","common_item"));
    
    // 显示查看第几页
    $("#checkPage").html("");
    $("#checkPage").append(getJsLocaleMessage("common","common_js_pageInfo_11")+"<input type='text' onkeyup=\"value=value.replace(/[^\\d]/g,\'\')\"  maxlength=\"9\" id='txtPage' value=\""+pageIndex+"\" name='txtPage' /><span>"+getJsLocaleMessage("common","common_page")+"</span><a href='javascript:goPage();'>GO</a>");
   
}



// 点击页数
// 表单提交
function pageClick(i){
	document.forms['form4'].elements["pageIndex"].value =i;
	//document.forms['form4'].submit();
	debugger;
	var pageIndex = i;
	window.location.href="surl_bind.htm?method=toAdd&pageIndex="+pageIndex;
}


// 点击每页记录数
// 表单提交
function changeCount(){
	document.forms['form4'].elements["pageIndex"].value =1;	// 回到第一页
	//document.forms['form4'].submit();
	debugger;
	var pageIndex = 1;
	window.location.href="surl_bind.htm?method=toAdd&pageIndex="+pageIndex;
}

// 向前向后翻页
// 此处为表单提交
function page(method){
	if(method=="back"){
		pageIndex=(Math.ceil(pageIndex/5)-1)*5;
	}
	else{
		pageIndex=(Math.ceil(pageIndex/5))*5+1;
	}
	document.forms['form4'].elements["pageIndex"].value =pageIndex;
	document.forms['form4'].submit();
	debugger;

	window.location.href="surl_bind.htm?method=toAdd&pageIndex="+pageIndex;
	// showPages();
}

 function keydown(e) // 支持ie 火狐 键盘按下事件
 {        
 	var currKey=0,e=e||event;
       // if(e.keyCode==13) goPage();
 }
       document.onkeydown=keydown;
       
   function showPageInfo2(total2,pageIndex2,pageSize2,totalRec2){
	   this.total=parseInt(total2);
		this.pageIndex=parseInt(pageIndex2);
		this.pageSize=(pageSize2);
		this.totalRec=parseInt(totalRec2);
		var $pa = $('#pageInfo');
		$pa.empty();

		//$pa.append('<span id="p_goto" onclick="jumpMenu()"></span>');
		if(pageIndex2<total2){
			$pa.append('<a class="p_a_h" id="p_last" href="javascript:goPage('+total2+')"></a>');
			$pa.append('<a id="p_next" href="javascript:goPage('+(pageIndex2-0+1)+')" class="p_a_h" ></a>');
		}else{
			
			$pa.append('<a id="p_last" href="javascript:void(0)"></a>');
			$pa.append('<a id="p_next" href="javascript:void(0)"></a>');
		}
		if(pageIndex2<=1){
			$pa.append('<a id="p_back" href="javascript:void(0)"></a>');
			$pa.append('<a id="p_first" href="javascript:void(0)"></a>');
		
		}else{
			$pa.append('<a class="p_a_h" id="p_back" href="javascript:goPage('+(pageIndex2-1)+')"></a>');
			$pa.append('<a class="p_a_h" id="p_first" href="javascript:goPage(1)"></a>');
			
		}

       $pa.append('<span id="p_info">'+getJsLocaleMessage("common","common_all")+totalRec+getJsLocaleMessage("common","common_js_pageInfo_10")+pageIndex+'/'+total+getJsLocaleMessage("common","common_page_p")+pageSize+getJsLocaleMessage("common","common_js_pageInfo_8")+'</span>');
		$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize2+'"  />');
		$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex2+'" />');
		$pa.append('<input name="page_input" class="input_bd" id="page_input" type="hidden" value='+pageIndex2+'/>');
		
		$pa.append('<input type="hidden" name="totalPage" id="totalPage" type="text" value="'+total2+'" />');
		$pa.append('<input type="hidden" name="totalRec" id="totalRec" type="text" value="'+totalRec2+'" />');
		$pa.append('<input type="hidden" name="needNewData" id="needNewData" type="text" value="1" />');
		
	}

function synPageInfoShow(isFirstEnter,url)
{
	//第一次进页面则不需要加载分页
	if(isFirstEnter)
	{
		showPageInfo();
		return;
	}
	//不需要获取新分页信息
	else if(needNewData != 1)
	{
		showPageInfo();
		return;
	}
	synLoadPageInfo(url);
}
   
//异步加载分页
function synLoadPageInfo(url)
{
	var $pa = $('#pageInfo');
	$pa.empty();

	$pa.append('<img src="common/img/login-loading.gif" />');
	$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
	$pa.append('<input type="hidden" name="totalPage" id="totalPage" type="text" value="'+total+'" />');
	$pa.append('<input type="hidden" name="totalRec" id="totalRec" type="text" value="'+totalRec+'" />');
	$pa.append('<input type="hidden" name="needNewData" id="needNewData" type="text" value="'+needNewData+'" />');
	
	//异步加载分页信息
	loadPageInfo(url);
}

//异步加载分页信息
function loadPageInfo(url)
{
	$.ajax({
		type: "POST",
		url: url,
		data: {
			pageIndex:pageIndex,
			pageSize:pageSize,
			totalPage:total,
			totalRec:totalRec
		},
		beforeSend:function () {},
		complete:function () {},
		success:function (result) {
			result = eval("("+result+")");
			totalRec = result.totalRec;
			total = result.totalPage;
			//document.forms['pageForm'].elements["totalRec"].value = result.totalRec;
			//document.forms['pageForm'].elements["totalPage"].value = result.totalPage;
			showPageInfo();
		}
	});
}

$(document).ready(function(){
	//阻止在IE下 翻页操作时 导致页面出现加载层
	if(document.all){
		if($('body').live) {//1.3~1.6
			$("a[href='javascript:void(0)']").live("click", function (e) {
				e.preventDefault();
			});
		}else{//delegate 1.4.2新增
			$("body").delegate("a[href='javascript:void(0)']","click",function(e){
				e.preventDefault();
			});
		}
	}
})
