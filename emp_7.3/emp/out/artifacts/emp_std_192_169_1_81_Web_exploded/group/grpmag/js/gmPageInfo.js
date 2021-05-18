

//自定义通讯录用分页
var total2=0;// 总页数
var pageIndex2=0;// 当前页数
var pageSize2=0;// 每页记录数
var totalRec2=0;// 总记录数
function initPage2(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total2=total;
	this.pageIndex2=pageIndex;
	this.pageSize2=pageSize;
	this.totalRec2=totalRec;
	
	showPageInfo2();
}

//跳到第几页
//表单提交
function goPage2(i){
	var page;
	if(i<0){
		page=$("#txtPage2").attr("value");
	}else{
		page=i;
	}
	var size=$('#pageSize2').attr("value");
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total2;
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的正整数！");
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm2'].elements["pageIndex2"].value =1;
		submitForm2();
		return;
	}
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		document.forms['pageForm2'].elements["pageIndex2"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		return;
	}
	document.forms['pageForm2'].elements["pageIndex2"].value=page;
	submitForm2();
}

function submitForm2(){
	var time = new Date();
 	var l2gType = $("#addrType").val();
 	var depId = $("#depId").val();
 	var udgId = document.getElementById("udgroupId").value;//增加群组ID
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
      	$('#personalSearchTable').load($('#servletUrl').val(),
		{
			method:'getSepInfoByType',
			time:time,
			l2gType:l2gType,
			cname:$("#cname").val(),
			cphone:$("#cphone").val(),
			depId:depId,
			udgId:udgId,
			lguserid:lguserid,
			lgcorpcode:lgcorpcode,
			pageIndex2:$('#txtPage2').val(),
			pageSize2:$('#pageSize2').val()
		}
	);
}

//function showPageInfo2(){
//	var $pa = $('#pageInfo2');
//	$pa.empty();
//	$pa.append('共').append(totalRec2).append('条，第').append(pageIndex2).append('/').append(total2).append('页');
//	$pa.append('&nbsp;');
//	if(pageIndex2<=1){
//		$pa.append('<img src="website/images/first.gif" width="26" height="15" />');
//		$pa.append('&nbsp;');
//		$pa.append('<img src="website/images/back.gif" width="26" height="15" />');
//	}else{
//		$pa.append('<a href="javascript:goPage2(1)"><img border="0" src="website/images/first.gif" width="26" height="15" /></a>');
//		$pa.append('&nbsp;');
//		$pa.append('<a href="javascript:goPage2('+(pageIndex2-1)+')"><img border="0" src="website/images/back.gif" width="26" height="15" /></a>');
//	}
//	$pa.append('&nbsp;');
//	if(pageIndex2<total2){
//		$pa.append('<a href="javascript:goPage2('+(pageIndex2-0+1)+')"><img border="0" src="website/images/next.gif" width="26" height="15" /></a>');
//		$pa.append('&nbsp;');
//		$pa.append('<a href="javascript:goPage2('+total2+')"><img border="0" src="website/images/last.gif" width="26" height="15" /></a>');
//	}else{
//		$pa.append('<img src="website/images/next.gif" width="26" height="15" />');
//		$pa.append('&nbsp;');
//		$pa.append('<img src="website/images/last.gif" width="26" height="15" />');
//	}
//	$pa.append('&nbsp;');
//	$pa.append('每页').append('<input name="pageSize2" id="pageSize2" type="text" value="'+pageSize2+'" size="4" />').append('条，');
//	$pa.append('<input name="pageIndex2" id="txtPage2" type="text" value="'+pageIndex2+'" size="4" />');
//	$pa.append('&nbsp;');
//	$pa.append('<a href="javascript:goPage2(-1)"><img border="0" src="website/images/go.gif" id="goPage" width="26" height="15" /></a>');
//}

function showPageInfo2(){
	var $pa = $('#pageInfo2');
	$pa.empty();

	$pa.append('<span id="p_goto2" onclick="jumpMenu2()"></span>');
	if(pageIndex2<total2){
		$pa.append('<a class="p_a_h" id="p_last2" href="javascript:goPage2('+total2+')"></a>');
		$pa.append('<a id="p_next2" href="javascript:goPage2('+(pageIndex2-0+1)+')" class="p_a_h" ></a>');
	}else{
		
		$pa.append('<a id="p_last2" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_next2" href="javascript:void(0)"></a>');
	}
	if(pageIndex2<=1){
		$pa.append('<a id="p_back2" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_first2" href="javascript:void(0)"></a>');
	
	}else{
		$pa.append('<a class="p_a_h" id="p_back2" href="javascript:goPage2('+(pageIndex2-1)+')"></a>');
		$pa.append('<a class="p_a_h" id="p_first2" href="javascript:goPage2(1)"></a>');
		
	}
	
	$pa.append('<span id="p_info2">共'+totalRec2+'条，第'+pageIndex2+'/'+total2+'页，'+pageSize2+'条/页</span>');
	$pa.append('<input type="hidden" name="pageSize2" id="pageSize2" type="text" value="'+pageSize2+'"  />');
	$pa.append('<input type="hidden" name="pageIndex2" id="txtPage2" type="text" value="'+pageIndex2+'" />');
	
	
	var newDiv = document.createElement("div");
	newDiv.id = "p_jump_menu2";
	newDiv.style.position = "absolute";
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.display = "none";
	newDiv.style.zIndex = "10000";
	newDiv.innerHTML = "<center><div>第<input name='page_input2' id='page_input2' onfocus='jumpDisplay2(1)' onblur='jumpDisplay2(0)' style='height: 15px;' id='page_input2' type='text' value='"+pageIndex2+"' size='4' />页," +
		"<input name='size_input2' id='size_input2' onfocus='jumpDisplay2(1)' onblur='jumpDisplay2(0)' style='height: 15px;' id='size_input2' type='text' value='"+pageSize2+"' size='4' />条/页</div>" +
		"<div><a id='p_jump_sure2' href='javascript:jumpSure2()' ></a></div></center><input type='hidden' id='isHd2'/>";
	document.body.appendChild(newDiv);
}
function jumpMenu2()
{
	
	var bodyheight=$(window).height();
	var top = $("#p_goto2").offset().top;
	var left = $("#p_goto2").offset().left;
	left = left - $("#p_jump_menu2").width()+$("#p_goto2").width();
	if(top+84 > bodyheight)
	{
		top = top - $("#p_jump_menu2").height()-4;
	}else
	{
		top = top + $("#p_goto2").height()+4;
	}
	
	$("#p_jump_menu2").css("left",left+"px")
	$("#p_jump_menu2").css("top",top+"px")
	$("#p_jump_menu2").show();
	$("#page_input2").focus();
}
function jumpDisplay2(ddd)
{
	$("#isHd2").val(ddd);
	setTimeout("isHid2()",200);
}
function isHid2()
{
	if($("#isHd2").val() == 0)
	{
		 $("#p_jump_menu2").hide();
	}
}
function jumpSure2()
{
	var page=$("#page_input2").val();
	var size=$("#size_input2").val();
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total2;
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		//document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input2").focus();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		$("#page_input2").focus();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的整数！");
		$("#size_input2").focus();
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		$("#size_input2").focus();
		return;
	}
	$("#pageSize2").val($("#size_input2").val());
	$("#txtPage2").val($("#page_input2").val());
	//document.forms['pageForm2'].submit();
	submitForm2();
}
function showPageInfo3(){
	var $pa = $('#pageInfo3');
	$pa.empty();

	$pa.append('<span id="p_goto3" onclick="jumpMenu3()"></span>');
	if(pageIndex3<total3){
		$pa.append('<a class="p_a_h" id="p_last3" href="javascript:goPage3('+total3+')"></a>');
		$pa.append('<a id="p_next3" href="javascript:goPage3('+(pageIndex3-0+1)+')" class="p_a_h" ></a>');
	}else{
		
		$pa.append('<a id="p_last3" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_next3" href="javascript:void(0)"></a>');
	}
	if(pageIndex3<=1){
		$pa.append('<a id="p_back3" href="javascript:void(0)"></a>');
		$pa.append('<a id="p_first3" href="javascript:void(0)"></a>');
	
	}else{
		$pa.append('<a class="p_a_h" id="p_back3" href="javascript:goPage3('+(pageIndex3-1)+')"></a>');
		$pa.append('<a class="p_a_h" id="p_first3" href="javascript:goPage3(1)"></a>');
		
	}
	
	$pa.append('<span id="p_info3">共'+totalRec3+'条，第'+pageIndex3+'/'+total3+'页，'+pageSize3+'条/页</span>');
	$pa.append('<input type="hidden" name="pageSize3" id="pageSize3" type="text" value="'+pageSize3+'"  />');
	$pa.append('<input type="hidden" name="pageIndex3" id="txtPage3" type="text" value="'+pageIndex3+'" />');
	
	
	var newDiv = document.createElement("div");
	newDiv.id = "p_jump_menu3";
	newDiv.style.position = "absolute";
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.display = "none";
	newDiv.style.zIndex = "10000";
	newDiv.innerHTML = "<center><div>第<input name='page_input3' id='page_input3' onfocus='jumpDisplay3(1)' onblur='jumpDisplay3(0)' style='height: 15px;' id='page_input3' type='text' value='"+pageIndex3+"' size='4' />页," +
		"<input name='size_input3' id='size_input3' onfocus='jumpDisplay3(1)' onblur='jumpDisplay3(0)' style='height: 15px;' id='size_input3' type='text' value='"+pageSize3+"' size='4' />条/页</div>" +
		"<div><a id='p_jump_sure3' href='javascript:jumpSure3()' ></a></div></center><input type='hidden' id='isHd3'/>";
	document.body.appendChild(newDiv);
}
function jumpMenu3()
{
	
	var bodyheight=$(window).height();
	var top = $("#p_goto3").offset().top;
	var left = $("#p_goto3").offset().left;
	left = left - $("#p_jump_menu3").width()+$("#p_goto3").width();
	if(top+84 > bodyheight)
	{
		top = top - $("#p_jump_menu3").height()-4;
	}else
	{
		top = top + $("#p_goto3").height()+4;
	}
	
	$("#p_jump_menu3").css("left",left+"px")
	$("#p_jump_menu3").css("top",top+"px")
	$("#p_jump_menu3").show();
	$("#page_input3").focus();
}
function jumpDisplay3(ddd)
{
	$("#isHd3").val(ddd);
	setTimeout("isHid3()",200);
}
function isHid3()
{
	if($("#isHd3").val() == 0)
	{
		 $("#p_jump_menu3").hide();
	}
}
function jumpSure3()
{
	var page=$("#page_input3").val();
	var size=$("#size_input3").val();
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total3;
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		//document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input3").focus();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		$("#page_input3").focus();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的整数！");
		$("#size_input3").focus();
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		$("#size_input3").focus();
		return;
	}
	$("#pageSize3").val($("#size_input3").val());
	$("#txtPage3").val($("#page_input3").val());
	//document.forms['pageForm3'].submit();
	submitForm3();
}

//客户通讯录用分页
var total3=0;// 总页数
var pageIndex3=0;// 当前页数
var pageSize3=0;// 每页记录数
var totalRec3=0;// 总记录数
function initPage3(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total3=total;
	this.pageIndex3=pageIndex;
	this.pageSize3=pageSize;
	this.totalRec3=totalRec;
	
	showPageInfo3();
}

//跳到第几页
//表单提交
function goPage3(i){
	var page;
	if(i<0){
		page=$("#txtPage3").attr("value");
	}else{
		page=i;
	}
	var size=$('#pageSize3').attr("value");
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total3;
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的正整数！");
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm3'].elements["pageIndex3"].value =1;
		submitForm3();
		return;
	}
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		document.forms['pageForm3'].elements["pageIndex3"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		return;
	}
	document.forms['pageForm3'].elements["pageIndex3"].value=page;
	submitForm3();
}

function submitForm3(){
	var time = new Date();
 	 var l2gType = $("#addrType").val();
 	 var depId = $("#depId").val();
 	  var udgId = document.getElementById("udgroupId").value;//增加群组ID
 	 var lguserid = $("#lguserid").val();
 	var lgcorpcode = $("#lgcorpcode").val();
    	$('#personalSearchTable').load($('#servletUrl').val(),
		{
			method:'getSepInfoByType',
			time:time,
			l2gType:l2gType,
			ccname:$("#ccname").val(),
			ccmobile:$("#ccphone").val(),
			depId:depId,
			udgId:udgId,
			lguserid:lguserid,
			lgcorpcode:lgcorpcode,
			pageIndex3:$('#txtPage3').val(),
			pageSize3:$('#pageSize3').val()
		}
	);
}
//
//function showPageInfo3(){
//	var $pa = $('#pageInfo3');
//	$pa.empty();
//	$pa.append('共').append(totalRec3).append('条，第').append(pageIndex3).append('/').append(total3).append('页');
//	$pa.append('&nbsp;');
//	if(pageIndex3<=1){
//		$pa.append('<img src="website/images/first.gif" width="26" height="15" />');
//		$pa.append('&nbsp;');
//		$pa.append('<img src="website/images/back.gif" width="26" height="15" />');
//	}else{
//		$pa.append('<a href="javascript:goPage3(1)"><img border="0" src="website/images/first.gif" width="26" height="15" /></a>');
//		$pa.append('&nbsp;');
//		$pa.append('<a href="javascript:goPage3('+(pageIndex3-1)+')"><img border="0" src="website/images/back.gif" width="26" height="15" /></a>');
//	}
//	$pa.append('&nbsp;');
//	if(pageIndex3<total3){
//		$pa.append('<a href="javascript:goPage3('+(pageIndex3-0+1)+')"><img border="0" src="website/images/next.gif" width="26" height="15" /></a>');
//		$pa.append('&nbsp;');
//		$pa.append('<a href="javascript:goPage3('+total3+')"><img border="0" src="website/images/last.gif" width="26" height="15" /></a>');
//	}else{
//		$pa.append('<img src="website/images/next.gif" width="26" height="15" />');
//		$pa.append('&nbsp;');
//		$pa.append('<img src="website/images/last.gif" width="26" height="15" />');
//	}
//	$pa.append('&nbsp;');
//	$pa.append('每页').append('<input name="pageSize3" id="pageSize3" type="text" value="'+pageSize3+'" size="4" />').append('条，');
//	$pa.append('<input name="pageIndex3" id="txtPage3" type="text" value="'+pageIndex3+'" size="4" />');
//	$pa.append('&nbsp;');
//	$pa.append('<a href="javascript:goPage3(-1)"><img border="0" src="website/images/go.gif" id="goPage3" width="26" height="15" /></a>');
//}





//员工通讯录分页

 var total4=0;// 总页数
var pageIndex4=0;// 当前页数
var pageSize4=0;// 每页记录数
var totalRec4=0;// 总记录数
function initPage4(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total4=total;
	this.pageIndex4=pageIndex;
	this.pageSize4=pageSize;
	this.totalRec4=totalRec;
	
	showPageInfo4();
}

//跳到第几页
//表单提交
function goPage4(i){
	var page;
	if(i<0){
		page=$("#txtPage4").attr("value");	
	}else{
		page=i;
	}
	var size=$('#pageSize4').attr("value");
	var checkPage=/^\d+$/;// 正则表达示验证数字
	var pagetotal=total4;
	if(size<1 ||  !checkPage.test(size) ){
		alert("每页显示数量必须为一个大于0的正整数！");
		return;
	}
	if(size>500)
	{
		alert("每页显示数量不能大于500！");
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm4'].elements["pageIndex4"].value =1;
		submitForm4();
		return;
	}
	if(page-0>pagetotal){
		alert("输入页数大于最大页数！");
		document.forms['pageForm4'].elements["pageIndex4"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert("跳转页必须为一个大于0的整数！");
		return;
	}
	document.forms['pageForm4'].elements["pageIndex4"].value=page;
	submitForm4();
}

function submitForm4(){
	var time = new Date();
 	 var l2gType = $("#addrType").val();
 	 var depId = $("#depId").val();
 	  var udgId = document.getElementById("udgroupId").value;//增加群组ID
	// var divName = $("#table2DivName").val();
    	$('#personalSearchTable').load($('#servletUrl').val(),
		{
			method:'getSepInfoByType',
			time:time,
			l2gType:l2gType,
			ename:$("#ename").val(),
			emobile:$("#ephone").val(),
			depId:depId,
			udgId:udgId,
			pageIndex4:$('#txtPage4').val(),
			pageSize4:$('#pageSize4').val()
		}
	);
}

function showPageInfo4(){
	var $pa = $('#pageInfo4');
	$pa.empty();
	$pa.append('共').append(totalRec4).append('条，第').append(pageIndex4).append('/').append(total4).append('页');
	$pa.append('&nbsp;');
	if(pageIndex4<=1){
		$pa.append('<img src="website/images/first.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="website/images/back.gif" width="26" height="15" />');
	}else{
		$pa.append('<a href="javascript:goPage4(1)"><img border="0" src="website/images/first.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage4('+(pageIndex4-1)+')"><img border="0" src="website/images/back.gif" width="26" height="15" /></a>');
	}
	$pa.append('&nbsp;');
	if(pageIndex4<total4){
		$pa.append('<a href="javascript:goPage4('+(pageIndex4-0+1)+')"><img border="0" src="website/images/next.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage4('+total4+')"><img border="0" src="website/images/last.gif" width="26" height="15" /></a>');
	}else{
		$pa.append('<img src="website/images/next.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="website/images/last.gif" width="26" height="15" />');
	}
	$pa.append('&nbsp;');
	$pa.append('每页').append('<input name="pageSize4" id="pageSize4" type="text" value="'+pageSize4+'" size="4" />').append('条，');
	$pa.append('<input name="pageIndex4" id="txtPage4" type="text" value="'+pageIndex4+'" size="4" />');
	$pa.append('&nbsp;');
	$pa.append('<a href="javascript:goPage4(-1)"><img border="0" src="website/images/go.gif" id="goPage4" width="26" height="15" /></a>');
}