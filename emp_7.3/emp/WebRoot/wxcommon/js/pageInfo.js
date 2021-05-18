var total=0;// 总页数
var pageIndex=0;// 当前页数
var pageSize=0;// 每页记录数
var totalRec=0;// 总记录数
function initPage(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total=parseInt(total);
	this.pageIndex=parseInt(pageIndex);
	this.pageSize=(pageSize);
	this.totalRec=parseInt(totalRec);
	
	showPageInfo3();
}
var persize = 9;//偶数会有问题
function showPageInfo3(){
    var firstV = "共  "+totalRec+" 条<span onclick='goPage(1)' "+(pageIndex==1?"class='current'":"")+" >1</span>";
    var lastV = "<span onclick='goPage("+total+")' "+(pageIndex==total?"class='current'":"")+">"+total+"</span>";
    var i;
    var size;
    if(total==1)
    {
        lastV = "";
        i= 0;
        size = 0;
    }else
    if(total <= persize+2)
    {
        i=2;
        size = total - 1;
    }else{
        var mid = Math.floor(persize/2);
        if(pageIndex < mid+2)
        {
            i = 2;
            size = 1+persize;
        }else if(pageIndex >= total-mid)
        {
            size = total - 1;
            i = total - persize;
        }else
        {
            i = pageIndex - mid;
            size = pageIndex + mid;
        }
    } 
    if(i > 2)
    {
        firstV += "...";
    }
    if(size < total - 1)
    {
        lastV =  "..."+ lastV;
    }
    for(;i<=size && size > 0;i=i+1)
    {
        firstV += "<span onclick='goPage("+i+")' "+(pageIndex==i?"class='current'":"")+" >"+i+"</span>";
    }
    lastV += "<span class='input'><input type='text' id='page_input' value='"+pageIndex+"'/></span><span onclick='jumpSure()'>跳转</span>"
    var hidInput = "<input type='hidden' name='pageIndex' id='pageIndex' value='"+pageIndex+"'/>"
        + "<input type='hidden' name='pageSize' id='pageSize' value='"+pageSize+"'/>";
    $('#pageInfo').append(firstV + lastV + hidInput);
}

// 跳到第几页
// 表单提交
function goPage(i){
	var page;
	if(i<0){
		page=$("#txtPage").attr("value");
	}else{
		page=i;
	}
	var size=$('#pageSize').attr("value");
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total;
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的整数！");
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm'].elements["pageIndex"].value =1;
		document.forms['pageForm'].submit();
		return;
	}
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		document.forms['pageForm'].elements["pageIndex"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		return;
	}
	submitForm(page);
}

function submitForm(){
    submitForm(1);
}
function submitForm(page)
{
    var action  = $('#pageForm').attr('action');
    if(action.indexOf("?")>-1)
    {
        action += "&"+new Date().getTime();
    }else
    {
        action += "?"+new Date().getTime();
    }
    $('#pageForm').attr('action',action);
    if(page != 0)
    {
        document.forms['pageForm'].elements["pageIndex"].value =page;  // 回到第一页
    }
    $.post('frame.htm?method=checkLogin',{lguserid:$('#lguserid').val(),isAsync:'yes'},function(result){
        if(result == "outOfLogin" || result == "false")
        {
            window.parent.showLogin(2);
        }else
        {
            document.forms['pageForm'].submit();
        }
    });
}
function showPageInfo(){
	var $pa = $('#pageInfo');
	$pa.empty();

	$pa.append('<span id="p_goto" onclick="jumpMenu()"></span>');
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
	
	$pa.append('<span id="p_info">共'+totalRec+'条，第'+pageIndex+'/'+total+'页，'+pageSize+'条/页</span>');
	$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
	
	
	var newDiv = document.createElement("div");
	newDiv.id = "p_jump_menu";
	newDiv.style.position = "absolute";
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.display = "none";
	newDiv.style.zIndex = "100";
	newDiv.className="div_bd div_bg";
	newDiv.innerHTML = "<center><div>第<input name='page_input' class='input_bd' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />页," +
		"<input  class='input_bd' name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />条/页</div>" +
		"<div><a id='p_jump_sure' href='javascript:jumpSure()' ></a></div></center><input type='hidden' id='isHd'/>";
	document.body.appendChild(newDiv);
	/*
     * $pa.append('&nbsp;'); $pa.append('每页').append('<input name="pageSize"
     * style="height: 15px;" id="pageSize" type="text" value="'+pageSize+'"
     * size="4" />').append('条，'); $pa.append('转到第').append('<input
     * name="pageIndex" style="height: 15px;" id="txtPage" type="text"
     * value="'+pageIndex+'" size="4" />').append('页'); $pa.append('&nbsp;');
     * $pa.append('<a href="javascript:goPage(-1)"><img
     * src="website/images/go.gif" id="goPage" width="26" height="15" /></a>');
     */
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
	var size=$("#pageSize").val();
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total;
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		// document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input").select();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		$("#page_input").select();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的整数！");
		$("#size_input").focus();
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		$("#size_input").select();
		return;
	}
	//$("#pageSize").val($("#size_input").val());
	//$("#pageIndex").val($("#page_input").val());
	submitForm($("#page_input").val());
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
		$("#Flexigrid_Bar").append("<span>&lt;&lt;上5页</span>");
	}
	else{
		$("#Flexigrid_Bar").append("<a href="+"\"javascript:page('back')\">&lt;&lt;上5页</a>");
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
		$("#Flexigrid_Bar").append("<a href="+"\"javascript:page('front')\">下5页&gt;&gt;</a>");
	}
	else{
		$("#Flexigrid_Bar").append("<span>下5页&gt;&gt;</span>");
	}
	$("#Flexigrid_Bar").append("&nbsp");
}

// 显示每页记录数按钮
// javascript:changeCount(),此函数为点击事件。
function showPageSize(){
	var num=10;
	$("#setupNum").html("");// 初始化
	$("#setupNum").append("<span>每页显示数量:</span>");
    $("#setupNum").append("<input  onkeyup=\"value=value.replace(/[^\\d]/g,\'\')\"  onkeypress='javascript:return checkPageIndex(event,this);'  maxlength=\"3\" type=\"text\" id='pageSize' name=\"pageSize\" value=\""+pageSize+"\"/>取值（1~500）");
    // 显示“共几页，几条”
    $("#AllPage").html("");
    $("#AllPage").append("共"+total+"页/共"+totalRec+"条");
    
    // 显示查看第几页
    $("#checkPage").html("");
    $("#checkPage").append("第<input type='text' onkeyup=\"value=value.replace(/[^\\d]/g,\'\')\"  maxlength=\"9\" id='txtPage' value=\""+pageIndex+"\" name='txtPage' /><span>页</span><a href='javascript:goPage();'>GO</a>");
   
}



// 点击页数
// 表单提交
function pageClick(i){
	document.forms['pageForm'].elements["pageIndex"].value =i;
	document.forms['pageForm'].submit();
}


// 点击每页记录数
// 表单提交
function changeCount(){
	document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
	document.forms['pageForm'].submit();
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
	document.forms['pageForm'].elements["pageIndex"].value =pageIndex;
	document.forms['pageForm'].submit();
	// showPages();
}

 function keydown(e) // 支持ie 火狐 键盘按下事件
 {        
 	var currKey=0,e=e||event;
    if(e.keyCode==13) return false;
 }
       document.onkeydown=keydown;
       
   function showPageInfo2(total2,pageIndex2,pageSize2,totalRec2){
	   this.total=parseInt(total2);
		this.pageIndex=parseInt(pageIndex2);
		this.pageSize=(pageSize2);
		this.totalRec=parseInt(totalRec2);
		var $pa = $('#pageInfo');
		$pa.empty();

		// $pa.append('<span id="p_goto" onclick="jumpMenu()"></span>');
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
		
		$pa.append('<span id="p_info">共'+totalRec2+'条，第'+pageIndex2+'/'+total2+'页，'+pageSize2+'条/页</span>');
		$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize2+'"  />');
		$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex2+'" />');
		$pa.append('<input name="page_input" class="input_bd" id="page_input" type="hidden" value='+pageIndex2+'/>');
		
	}       