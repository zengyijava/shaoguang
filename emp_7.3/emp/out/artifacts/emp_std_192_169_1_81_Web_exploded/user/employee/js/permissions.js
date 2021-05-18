var curDepName = ""
var time = new Date();
var bookType = "employee";//标明操作模块。

//**********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var isfIE = (document.all) ? true : false;
var isfIE6 = isfIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var app=navigator.appName;
var ipWidth = 13;

if (isfIE6)
{
	ipWidth = 0;
}
if (app == "Netscape")
{
	ipWidth = 10;
}
//***************

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

function showPageInfo(){
	var $pa = $('#pageInfo');
	$pa.empty();

	/*$pa.append('<span id="p_goto" onclick="jumpMenu()"></span>');*/
	/*跳轉*/
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

    /*共  条，第   页， 条/页  */
    $pa.append('<span id="p_info">'+getJsLocaleMessage("common","common_all")+totalRec+getJsLocaleMessage("common","common_js_pageInfo_10")+pageIndex+'/'+total+getJsLocaleMessage("common","common_page_p")+pageSize+getJsLocaleMessage("common","common_js_pageInfo_8")+'</span>');
	$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
	//新增
	$pa.append('<input type="hidden" name="totalRec" id="totalRec" type="text" value="'+totalRec+'" />');
	$pa.append('<input type="hidden" name="totalPage" id="totalPage" type="text" value="'+total+'" />');
	//-------------
	
	
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
function showAll()
{
	/*$("#ifra").contents().find("li span[link] a").each(function(e){
		$(this).removeClass("selected");
	});*/
	/*$("#depId").val($("#curDepId").val());
	$("#depName").text("");
	/*$("#userName").find("> option").eq(0).attr("selected","selected");
	$("#username").val("");
	var booktype = $("#bookType").val();
	if(booktype == "client")
	{
		 $('#servletUrl').val("epl_permissions.htm?method=toClientPm");
		 
		 //路径修改增加 “addrbook” by郭凯
		 $('#ifra').attr('src',$('#inheritPath').val()+'/addrbook/a_addrbookDepTree.jsp?treemethod=getClientDepTreeJson&getType=privi');
		
	}else
	{
		$('#servletUrl').val("epl_permissions.htm?method=toEmployeePm");
		
		//路径修改增加 “addrbook” by郭凯
		 $('#ifra').attr('src',$('#inheritPath').val()+'/addrbook/a_addrbookDepTree.jsp?treemethod=getEntDepTreeJson&getType=privi');
	}
	submitForm();*/
	location.href = location.href;
}
function doDel()
{
 var bookType2 = $('#bookType').val();
	var i=0;		//统计勾选中的个数
	var dsgIds="";
	$('input[name="checklist"]:checked').each(function(index){	
		dsgIds=dsgIds+$(this).val()+",";
		i=i+1;
	});
	dsgIds=dsgIds.substring(0,dsgIds.lastIndexOf(','));
	if(i==0)
	{
		alert(getJsLocaleMessage('employee','employee_alert_118'));
		return;
	}else
	{
	
		if(confirm(getJsLocaleMessage('employee','employee_alert_119'))){
				
		    $.ajax({
		        url: 'epl_permissions.htm?method=getInfo',
		        data:{
		    		ids:dsgIds,
		    		opType:"delete",
		    		bookType:bookType2
		        },
		        type: 'post',
		       // timeout: 3000,
		        error: function(){
		           alert(getJsLocaleMessage('employee','employee_alert_69'));		
		           return;
		        },
		        success: function(result){
					if (result>=0) {
						alert(getJsLocaleMessage('employee','employee_alert_120')+result+getJsLocaleMessage('employee','employee_alert_121'));
						submitForm();
					}else{
						alert(getJsLocaleMessage('employee','employee_alert_69'));	
					}		   
		        }
		    });
		}
	}
}

function doDelCliDom()
{
	var i=0;		//统计勾选中的个数
	var dsgIds="";
	$('input[name="checklist"]:checked').each(function(index){	
		dsgIds=dsgIds+$(this).val()+",";
		i=i+1;
	});
	dsgIds=dsgIds.substring(0,dsgIds.lastIndexOf(','));
	if(i==0)
	{
		alert(getJsLocaleMessage('employee','employee_alert_118'));
		return;
	}else
	{
		var lgcorpcode = $('#lgcorpcode').val();
		if(confirm(getJsLocaleMessage('employee','employee_alert_119'))){
				
		    $.ajax({
		        url: 'mcsa_mcsPermissions.htm?method=getInfo',
		        data:{
		    		ids:dsgIds,
		    		opType:"delete",
		    		lgcorpcode:lgcorpcode
		        },
		        type: 'post',
		       // timeout: 3000,
		        error: function(){
		           alert(getJsLocaleMessage('employee','employee_alert_69'));	
		           return;
		        },
		        success: function(result){
					if (result.indexOf(getJsLocaleMessage('employee','employee_alert_122')) == -1 && result>0) {
						alert(getJsLocaleMessage('employee','employee_alert_120')+result+getJsLocaleMessage('employee','employee_alert_121'));
						submitForm();
					}else if (result.indexOf(getJsLocaleMessage('employee','employee_alert_122')) != -1 && result.indexOf("0") != -1) {
						alert(getJsLocaleMessage('employee','employee_alert_123'));	
						return;
					}
					else if(result.indexOf(getJsLocaleMessage('employee','employee_alert_122')) != -1 && result.indexOf("0") == -1){
						alert(getJsLocaleMessage('employee','employee_alert_124')+result.substring(0,1)+getJsLocaleMessage('employee','employee_alert_125'));	
						submitForm();
					}	   
		        }
		    });
		}
	}
}

function doDelQuanXian()
{
 var bookType2 = $('#bookType').val();
	var i=0;		//统计勾选中的个数
	var dsgIds="";
	$('input[name="checklist"]:checked').each(function(index){	
		dsgIds=dsgIds+$(this).val()+",";
		i=i+1;
	});
	dsgIds=dsgIds.substring(0,dsgIds.lastIndexOf(','));
	if(i==0)
	{
		alert(getJsLocaleMessage('employee','employee_alert_118'));
		return;
	}else
	{
		var lgcorpcode = $('#lgcorpcode').val();
		if(confirm(getJsLocaleMessage('employee','employee_alert_119'))){
				
		    $.ajax({
		        url: 'epl_permissions.htm?method=getInfo',
		        data:{
		    		ids:dsgIds,
		    		opType:"delete",
		    		bookType:bookType2,
		    		lgcorpcode:lgcorpcode
		        },
		        type: 'post',
		       // timeout: 3000,
		        error: function(){
		           alert(getJsLocaleMessage('employee','employee_alert_69'));	
		           return;
		        },
		        success: function(result){
					if (result.indexOf(getJsLocaleMessage('employee','employee_alert_122')) == -1 && result>0) {
						alert(getJsLocaleMessage('employee','employee_alert_120')+result+getJsLocaleMessage('employee','employee_alert_121'));
						submitForm();
					}else if (result.indexOf(getJsLocaleMessage('employee','employee_alert_122')) != -1 && result.indexOf("0") != -1) {
						alert(getJsLocaleMessage('employee','employee_alert_123'));	
						return;
					}
					else if(result.indexOf(getJsLocaleMessage('employee','employee_alert_122')) != -1 && result.indexOf("0") == -1){
						alert(getJsLocaleMessage('employee','employee_alert_124')+result.substring(0,1)+getJsLocaleMessage('employee','employee_alert_125'));	
						submitForm();
					}	
						   
		        }
		    });
		}
	}
}


function selectUserName(va)
{
	//$("#curSysUser").text(va);
}

function bangding()
{
	var dCodeThird=$('#depId').val();
	if(dCodeThird=="")
	{
		alert(getJsLocaleMessage('employee','employee_alert_126'));
	}else
	{ 
        $("#com_add_Dom2 curDep").html($('#depName').text());
        var time=new Date();
        $('#showSysuser').load(path+'/epl_permissions.htm?method=getSysuserList&opType='+"getSysuserList"+'&bookType='+bookType,{time:time});
        /*
		$.post("epl_permissions.htm?method=getInfo",{opType:"getSysuserList",bookType:bookType},function(data){
			$("#showSysuser").html(data);
			
			//$("#curSysUser").text("");
			//$("#sysDiv").css("display","block");
			$('#showSysuser table tr').hover(function(){
				$(this).css('background-color','#EDEDED');
			},function(){
				if($(this).find('> td input:checkbox').attr("checked")==true)
				{
					return;
				}
				$(this).css('background-color','#FFFFFF');
			});
			$('#showSysuser table tr').click(function(){
				if($(this).find('> td input:checkbox').attr('checked')==true)
				{
					$(this).find('> td input:checkbox').attr('checked',false);
				}else
				{
					$(this).find('> td input:checkbox').attr('checked',true);
				}
				
				checkRadion();
			});
		});
		*/
	}
}

$(function(){
 
//	$("#bindOk").click(function(){
	
	//});
//	$("#bindNo").click(function(){
	//    $("#sysDiv").css("display","none");
	//    qdzz();
	//});
	
	//选项卡切换
	$("#s_ud_s_list li").each(function(index){
		$(this).click(function(){
			$("div.s_ud_contentin").removeClass("s_ud_contentin");
			$("li.s_ud_tabin").removeClass("s_ud_tabin");
			$(this).addClass("s_ud_tabin");
			var booktype = $(this).find("> input").val();
			$("#bookType").val(booktype);
			if(booktype!=bookType)
			{
				bookType = booktype;
				$('#servletUrl').val("epl_permissions.htm?method=toEmployeePm");
				$('#ifra').attr('src',$('#iPath').val()+'/epl_permissionsTree.jsp?treemethod=getEmpSecondDepJson');
					 
				var servletUrl = $('#servletUrl').val();
				var time=new Date();
				 
				 
 				var servletUrl=$('#servletUrl').val();
				// $('#permissionsInfo').load(servletUrl,
					//		{time:time});
				//showAll();
 				$("#depId").val("");
 				$("#depName").text("");
 				$("#userName").find("> option").eq(0).attr("selected","selected");
 				submitForm();
			}
		});
	});
});
function checkRadion()
{
	$('#showSysuser table tr').each(function(){
		if($(this).find('> td input:checkbox').attr("checked")==true)
		{
			$(this).css('background-color','#EDEDED');
		}else
		{
			$(this).css('background-color','#FFFFFF');
		}
	});
}
function submitForm()
{
	page_loading();
	var time=new Date();
	var pageIndex=$('#txtPage').val();
	var pageSize=$('#pageSize').val();
	//新增
	var totalRec=$('#totalRec').val();
	var totalPage=$('#totalPage').val();
	//---------------
 	var depCode=$('#depId').val();
	var userName =$('#username').val();
	var servletUrl=$('#servletUrl').val();
	var lguserid=$('#lguserid').val();
	var lgcorpcode=$('#lgcorpcode').val();
	$('#permissionsInfo').load(servletUrl,{time:time,depCode:depCode,pageIndex:pageIndex,pageSize:pageSize,totalRec:totalRec,totalPage:totalPage,userName:userName,lguserid:lguserid,lgcorpcode:lgcorpcode});
	afterSubmitForm();
}
//跳到第几页
//表单提交
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
	document.forms['pageForm'].elements["pageIndex"].value=page;
	submitForm();
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
		alert(getJsLocaleMessage('employee','employee_alert_20'));
		return;
	}
	if(size>500)
	{
		alert(getJsLocaleMessage('employee','employee_alert_19'));
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm3'].elements["pageIndex3"].value =1;
		submitForm3();
		return;
	}
	if(page-0>pagetotal){
		alert(getJsLocaleMessage('employee','employee_alert_16'));
		document.forms['pageForm3'].elements["pageIndex3"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		alert(getJsLocaleMessage('employee','employee_alert_17'));
		return;
	}
	document.forms['pageForm3'].elements["pageIndex3"].value=page;
	submitForm3();
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
//员工机构树
function getEmpDepTree()
{
	$("#bookType").val('client');
	 DepTree("getEmpSecondDepJson");
}

function getClinetDepTree()
{
	 $("#bookType").val('employee');
	 DepTree("getClientDepTreeJson");
}
//获取机构树
function DepTree(method)
{
	var inheritPath = $("#inheritPath").val();
	var time=new Date();
	$('#DepTree').load(inheritPath+'/a_addrbookDepTree.jsp?treemethod='+method,{time:time});
}

function doClientBind()
{
	if($("#permissionType").val()=="1")
	{
		alert(getJsLocaleMessage('employee','employee_alert_127'));
		return;
	}
	var bookType = $("#bookType").val();
	 
	if(bookType =='')
	{
		bookType = 'employee';
	}
 	var dCodeThird=$('#depId').val();
	if(dCodeThird=="")
	{
		alert(getJsLocaleMessage('employee','employee_alert_126'));
		return;
	} 
	$("#com_add_Dom2").css("display","block");
	var selDepName = $('#depName2').val();
	var selDepId = $('#depId').val();
    //$("#curDep").html($('#depName2').val());
    var lguserid = $("#lguserid").val();
    var lgcorpcode = $("#lgcorpcode").val();
    $("#bindUserFrame").attr("src",'mcsa_mcsPermissions.htm?method=getSysuserList&pageSize=10&opType='+"getSysuserList"+'&selDepName='+selDepName+'&selDepId='+selDepId+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode,{time:time});
	
	$('#com_add_Dom2').dialog('open');
}

function doBind()
{
	if($("#permissionType").val()=="1")
	{
		alert(getJsLocaleMessage('employee','employee_alert_127'));
		return;
	}
	var bookType = $("#bookType").val();
	 
	if(bookType =='')
	{
		bookType = 'employee';
	}
 	var dCodeThird=$('#depId').val();
	if(dCodeThird=="")
	{
		alert(getJsLocaleMessage('employee','employee_alert_126'));
		return;
	} 
	$("#com_add_Dom2").css("display","block");
    $("#curDep").html($('#depName2').val());
    var lguserid = $("#lguserid").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var selDepName = $('#depName2').val();
	var selDepId = $('#depId').val();

	var src = 'epl_permissions.htm?method=getSysuserList&pageSize=10&opType='
		+"getSysuserList"+'&selDepName='+selDepName+'&selDepId='+selDepId+'&bookType='
		+bookType+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode;
    src = encodeURI(src);
	
$.get("epl_permissions.htm?method=checkdepDel&selDepId="+selDepId,function(result) {
		if(result!=null && result=="-1"){
			alert(getJsLocaleMessage('employee','employee_alert_114'));
			window.location.reload();
		}else{
			$("#bindUserFrame").attr("src", src, {time:time.getTime()});
			$('#com_add_Dom2').dialog('open');
		}
       
	});
	
	//---------------------------
	
	/*备份p
	 $("#bindUserFrame").attr("src",'epl_permissions.htm?method=getSysuserList&pageSize=10&opType='+"getSysuserList"+'&selDepName='+selDepName+'&selDepId='+selDepId+'&bookType='+bookType+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode,{time:time});
	 $('#com_add_Dom2').dialog('open');
	 */
}
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
function showPageInfo3(){
	var $pa = $('#pageInfo3');
	$pa.empty();
	$pa.append(getJsLocaleMessage('employee','employee_alert_12')).append(totalRec3).append(getJsLocaleMessage('employee','employee_alert_13')).append(pageIndex3).append('/').append(total3).append(getJsLocaleMessage('employee','employee_alert_128'));
	$pa.append('&nbsp;');
	if(pageIndex3<=1){
		$pa.append('<img src="website/images/first.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="website/images/back.gif" width="26" height="15" />');
	}else{
		$pa.append('<a href="javascript:goPage3(1)"><img border="0" src="website/images/first.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage3('+(pageIndex3-1)+')"><img border="0" src="website/images/back.gif" width="26" height="15" /></a>');
	}
	$pa.append('&nbsp;');
	if(pageIndex3<total3){
		$pa.append('<a href="javascript:goPage3('+(pageIndex3-0+1)+')"><img border="0" src="website/images/next.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage3('+total3+')"><img border="0" src="website/images/last.gif" width="26" height="15" /></a>');
	}else{
		$pa.append('<img src="website/images/next.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="website/images/last.gif" width="26" height="15" />');
	}
	$pa.append('&nbsp;');
	$pa.append(getJsLocaleMessage('employee','employee_alert_129')).append('<input name="pageSize3" id="pageSize3" type="text" value="'+pageSize3+'" size="4" />').append(getJsLocaleMessage('employee','employee_alert_130'));
	$pa.append('<input name="pageIndex3" id="txtPage3" type="text" value="'+pageIndex3+'" size="4" />');
	$pa.append('&nbsp;');
	$pa.append('<a href="javascript:goPage3(-1)"><img border="0" src="website/images/go.gif" id="goPage3" width="26" height="15" /></a>');
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
		$('#ifra').css('height',hei-73-tophei);
	}else
	{
		$('.left_dep').css('height',hei-20-tophei);
		$('.left_dep .list ').css('height',hei-70-tophei);
		$('#ifra').css('height',hei-73-tophei);
	}
}

function afterSubmitForm()
{
	aaaaa = 3;
	setLeftHeight2();
	aaaaa = 0;
}



//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
	$('#search').click(function(){submitForm();});
    $("#toggleDiv").toggle(function() {
        $("#condition").hide();
        $(this).removeClass("collapse");
    }, function() {
        $("#condition").show();
        $(this).addClass("collapse");
    });
	$("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
		}, function() {
		$(this).removeClass("hoverColor");
	});
	
	$("input[name='checkall']").each(function(index){
		$(this).click(
			function(){
				$("input[name='checklist']").attr("checked",$(this).attr("checked")); 
			}
		);
	});
	var skin = $("#skin").val();
	var _height=500;
	var _width=710;
	if(skin.indexOf("frame4.0") != -1) {
		_height=800;
		_width=900;
	}
	$('#com_add_Dom2').dialog({
		autoOpen: false,
		height: _height,
		width: _width,
		modal:true,
		close:function(){
		$("#bindUserFrame").attr("src","");
	}
	});
	setLeftHeight();
});

function closeBinddiv()
{
	$("#com_add_Dom2").dialog("close");
	$("#bindUserFrame").attr("src","");
}

function bindok()
{
	window.frames['bindUserFrame'].ok();
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
				page_loading();
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
				




