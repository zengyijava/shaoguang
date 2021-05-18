
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)   
	{
		a[i].checked =e.checked; 
	} 
}	

var total=0;// 总页数
var pageIndex=0;// 当前页数
var pageSize=0;// 每页记录数
var totalRec=0;// 总记录数
function initPage(total,pageIndex,pageSize,totalRec){// 初始化页面
	this.total=total;
	this.pageIndex=pageIndex;
	this.pageSize=pageSize;
	this.totalRec=totalRec;
	
	showPageInfo();
}

//跳到第几页


function showPageInfo(){
	var $pa = $('#pageInfo');
	$pa.empty();

    var tz=getJsLocaleMessage("common","common_skip");
    $pa.append('<span id="p_goto" onclick="jumpMenu()" style="text-align: left;" ><font style="padding-left:5px;width:100%;color:#000;line-height:24px;">'+tz+'</font></span>');
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

	var newDiv = document.createElement("div");
	newDiv.id = "p_jump_menu";
	newDiv.style.position = "absolute";
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.zIndex = "100";
	newDiv.style.display = "none";
	newDiv.className="div_bd div_bg";
    /*如果为英文增加宽度*/
    var empLangName = getJsLocaleMessage("common","common_empLangName");
    if("zh_HK" === empLangName){
        newDiv.style.width="200px";
    }
    /*第 页，条/页 */
    newDiv.innerHTML = "<center><div>"+getJsLocaleMessage("common","common_js_pageInfo_11")+"<input name='page_input' class='input_bd' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />"+getJsLocaleMessage("common","common_page_p") +
        "<input  class='input_bd' name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />"+getJsLocaleMessage("common","common_js_pageInfo_8")+"</div>" +
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
    var confirm=getJsLocaleMessage("common","common_confirm");
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
        alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
		//document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input").focus();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
        /*跳转页必须为一个大于0的整数！*/
        alert(getJsLocaleMessage("common","common_js_pageInfo_4"));
		$("#page_input").focus();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
        /*每页显示数量必须为一个大于0的整数！*/
        alert(getJsLocaleMessage("common","common_js_pageInfo_1"));
		$("#size_input").focus();
		return;
	}
	if(size>500)
	{
        /*每页显示数量不能大于500！*/
        alert(getJsLocaleMessage("common","common_js_pageInfo_2"));
		$("#size_input").focus();
		return;
	}
	$("#pageSize").val($("#size_input").val());
	$("#txtPage").val($("#page_input").val());
	submitForm();
}

function afterSubmitForm()
{
	aaaaa = 3;
	setLeftHeight2();
	aaaaa = 0;
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
	if(page==null || page==""){
		document.forms['pageForm'].elements["pageIndex"].value =1;
		submitForm();
		return;
	}
	if(page-0>pagetotal){
        /*输入页数大于最大页数！*/
        alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
		document.forms['pageForm'].elements["pageIndex"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
        /*跳转页必须为一个大于0的整数！*/
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

$(function(){
	$('#addToGroup').click(function (){
		var i=0;	
		$('input[name="checklist"]:checked').each(function(index){	
			i=i+1;
		});
		if(i==0)
		{
			/*未勾选通讯录信息！*/
            alert(getJsLocaleMessage("common","common_js_monPageinfo_1"));
		}else
		{
			$('#modify').css('display','block');
		}
	});
})
function addL2G()
{
	var udgId=$('#groupSelect').val();
	var l2gType=$('#l2gType').val();
	var ids=new Array();
	var i=0;	
	$('input[name="checklist"]:checked').each(function(index){	
		ids[i]=$(this).val();
		i=i+1;
	});
	if(i==0)
	{
		/*未勾选通讯录信息！*/
		alert(getJsLocaleMessage("common","common_js_monPageinfo_1"));
	}else if(udgId==0)
	{
		/*未选择分组！*/
		alert(getJsLocaleMessage("common","common_js_monPageinfo_2"));
	}else
	{
		$.post("updateGroup.htm",{
			opType:"addToGroup",
			udgId:udgId,
			ids:ids,
			l2gType:l2gType
		},function(count){
			if(count>=0)
			{
				/*共添加  条记录到组：*/
				alert(getJsLocaleMessage("common","common_js_monPageinfo_3")+count+getJsLocaleMessage("common","条记录到组：")
						+$('#groupSelect').find('> option[selected]').text()+"！");
				$('input:checkbox').attr("checked",false);
				$('#modify').css('display','none');
			}else
			{
				/*添加失败！*/
				alert(getJsLocaleMessage("common","common_text_7"));
			}
		});
	}
}
function showAll()
{
	/*$('#phone').val("");
	$('#name').val("");
	$('#depId').val("");
	$('li span[link] a').each(function(e){
		$(this).removeClass("selected");
	});
	submitForm();*/
	location.href= location.href;
}
function showAll2()
{
	/*$('#phone').val("");
	$('#name').val("");
	$('#depId').val("");
	$('#userId').val("");
	$('li span[link] a').each(function(e){
		$(this).removeClass("selected");
	});
	submitForm();*/
	location.href= location.href;
}
function keydown(e) //支持ie 火狐 键盘按下事件      
{        
	var currKey=0,e=e||event;
        if(e.keyCode==13) goPage();      
}

function toSysuser()
{
	var pathUrl = $("#pathUrl").val();
	var count = $("input[name='delBook']:checked").length;
	/*请选择一个员工进行操作！*/
	if(count != "1"){alert(getJsLocaleMessage("common","common_js_monPageinfo_5"));return;}
	else
	{
		var isopr = $("input[name='delBook']:checked").attr("id");
		if (isopr == "1")
		{
			/*该员工已经是操作员！*/
			alert(getJsLocaleMessage("common","common_js_monPageinfo_6"));
			return;
		}
		var id = $("input[name='delBook']:checked").val();
	    if (id != null || id != "")
	    {
	    	location.href=pathUrl+"/opt_sysuser.htm?method=toAdd&eid="+id+"&lgguid="+$("#lgguid").val();
	    	//$.post(pathUrl+"/opt_sysuser.htm?method=toAdd",{eid:id},function(r){});
	    }
	}
     
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

//var  resizeTimer = null;  
