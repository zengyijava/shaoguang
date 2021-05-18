
//**********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var app=navigator.appName;
var ieWidth = 350;
var ieHeight = 440;

if (isIE6)
{
	ieWidth = 360;
	ieHeight = 470;
}
if (app == "Netscape")
{
	ieWidth = 355;
	ieHeight = 447;
}


function delAll(){
	var items = "";
	var addrs = "";
	$('input[name="checklist"]:checked').each(function(){	
		var temp  = $(this).val().toString().split("&");
		items += temp[0]+",";
		addrs += temp[1]+",";
	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
		addrs = addrs.toString().substring(0, addrs.lastIndexOf(','));
	}
 	if(items==""){
		//alert("请选择您要删除的彩信素材！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qxznyscdcxsc"));
		return;
	}else{
	//if(confirm("您确定要删除选择的彩信素材？")==true){
	if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_nqdyscxzdcxsc"))==true){
				$.post("mat_mmsMaterial.htm?method=deleteMaterial",{ids:items},function(result){
					if(result>=1)
					{
						$.post("mat_mmsMaterial.htm?method=delMoreSource",{addrs:addrs},function(result){
							  
						});
						//alert("删除成功,共删除"+result+"条信息！");
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sccggsc")+result+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_txx"));
						window.location.reload();
					}else{
						//alert("删除失败！");
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scsb"));
						window.location.reload();
					}
				});
				
	}
	}
}

function del(i,addrTemp2){
 	//if(confirm("您确定要删除选择的彩信素材？"))
 	if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_nqdyscxzdcxsc")))
	{
 		
		$.post("mat_mmsMaterial.htm?method=deleteMaterial",{ids:i},function(result){
			if(result>0)
			{
				$.post("mat_mmsMaterial.htm?method=delSource",{addrTemp2:addrTemp2},function(result){
					 
				});
				//alert("删除成功！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sccg"));
				location.href="mat_mmsMaterial.htm?method=find&skip=true";
			}else{
				//alert("删除失败！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scsb"));
				location.href="mat_mmsMaterial.htm?method=find&skip=true";
			}
		});
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
	
	if(CstlyeSkin.indexOf("frame4.0") != -1) {
		PageInfo.initPage(total,pageIndex,pageSize,totalRec);	// 新分页功能
	} else {
		showPageInfo();  //旧分页功能
	}
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
		//alert("每页显示数量必须为一个大于0的正整数！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_myxsslbxwygdy0dzzs"));
		return;
	}
	if(size>500)
	{
		//alert("每页显示数量不能大于500！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_myxsslbndy500"));
		return;
	}
	if(page==null || page==""){
		document.forms['pageForm'].elements["pageIndex"].value =1;
		submitForm();
		return;
	}
	if(page-0>pagetotal){
		//alert("输入页数大于最大页数！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_srysdyzdys"));
		document.forms['pageForm'].elements["pageIndex"].value="";
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		//alert("跳转页必须为一个大于0的整数！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_tzybxwygdy0dzs"));
		return;
	}
	document.forms['pageForm'].elements["pageIndex"].value=page;
	submitForm();
}

function submitForm(){
	var time = new Date();
	 var childCode = $("#childCode").val();
	 var parentCodetemp = $("#parentCodetemp").val();
	 var lgcorpcode = $("#lgcorpcode").val();
	 var userId= $("#lguserid").val();
 	$('#tableInfo').load($('#servletUrl').val(),
		{
			method:'getTable',
			time:time,
			childCode:childCode,
			parentCodetemp:parentCodetemp,
			lgcorpcode:lgcorpcode,
			userId:userId,
			pageIndex:$('#txtPage').val(),
			pageSize:$('#pageSize').val()
		},function(response,status,xhr){
			var resultHtml = xhr.responseText ;
			
			if(resultHtml.indexOf("showwww") > 0){
    			window.location.href=location;
    		    return;
    		}
		}
	);
}

function showPageInfo1(){
	var $pa = $('#pageInfo');
	var baseRoot = "website";
	$pa.empty();
	//$pa.append('共').append(totalRec).append('条，第').append(pageIndex).append('/').append(total).append('页');
	$pa.append(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_g")).append(totalRec).append(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_td")).append(pageIndex).append('/').append(total).append(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_y"));
	$pa.append('&nbsp;');
	if(pageIndex<=1){
		$pa.append('<img src="'+baseRoot+'/images/first.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="'+baseRoot+'/images/back.gif" width="26" height="15" />');
	}else{
		$pa.append('<a href="javascript:goPage(1)"><img border="0" src="'+baseRoot+'/images/first.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage('+(pageIndex-1)+')"><img border="0" src="'+baseRoot+'/images/back.gif" width="26" height="15" /></a>');
	}
	$pa.append('&nbsp;');
	if(pageIndex<total){
		$pa.append('<a href="javascript:goPage('+(pageIndex-0+1)+')"><img border="0" src="'+baseRoot+'/images/next.gif" width="26" height="15" /></a>');
		$pa.append('&nbsp;');
		$pa.append('<a href="javascript:goPage('+total+')"><img border="0" src="'+baseRoot+'/images/last.gif" width="26" height="15" /></a>');
	}else{
		$pa.append('<img src="'+baseRoot+'/images/next.gif" width="26" height="15" />');
		$pa.append('&nbsp;');
		$pa.append('<img src="'+baseRoot+'/images/last.gif" width="26" height="15" />');
	}
	$pa.append('&nbsp;');
	//$pa.append('每页').append('<input name="pageSize" id="pageSize" type="text" value="'+pageSize+'" size="4" />').append('条，');
	$pa.append(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_my")).append('<input name="pageSize" id="pageSize" type="text" value="'+pageSize+'" size="4" />').append(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_t"));
	//$pa.append('转到').append('<input name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" size="4" />');
	$pa.append(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_zd")).append('<input name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" size="4" />');
	$pa.append('&nbsp;');
	$pa.append('<a href="javascript:goPage(-1)"><img src="'+baseRoot+'/images/go.gif" id="goPage" width="26" height="15" /></a>');
}
 





function showPageInfo(){
	var $pa = $('#pageInfo');
	$pa.empty();

	$pa.append('<span id="p_goto" onclick="jumpMenu()"><center style="margin-top:5px;margin-right:8px;">'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_tz")+'</center></span>');
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
	
	//$pa.append('<span id="p_info">共'+totalRec+'条，第'+pageIndex+'/'+total+'页，'+pageSize+'条/页</span>');
	$pa.append('<span id="p_info">'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_g")+totalRec+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_td")+pageIndex+'/'+total+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_y")+'，'+pageSize+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_ty")+'</span>');
	$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
	$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
	
	var newDiv = document.createElement("div");
	newDiv.id = "p_jump_menu";
	newDiv.style.position = "absolute";
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.zIndex = "100";
	newDiv.style.display = "none";
	if(langName==langEng){
		newDiv.style.width = "200px";
	}
	newDiv.className="div_bd div_bg";
	//newDiv.innerHTML = "<center><div>第<input name='page_input' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />页," +
	//	"<input name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />条/页</div>" +
	//	"<div><a id='p_jump_sure' href='javascript:jumpSure()' ></a></div></center><input type='hidden' id='isHd'/>";
	newDiv.innerHTML = "<center><div>"+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_d")+"<input name='page_input' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />"+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_y")+"," +
	"<input name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />"+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_ty")+"</div>" +
	"<div><a id='p_jump_sure' href='javascript:jumpSure()' style='font-size:13px;color:black;' >"+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qd")+"</a></div></center><input type='hidden' id='isHd'/>";
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
		//alert("输入页数大于最大页数！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_srysdyzdys"));
		//document.forms['pageForm'].elements["pageIndex"].value="";
		$("#page_input").focus();
		return;
	}
	if(page<1 ||  !checkPage.test(page) ){
		//alert("跳转页必须为一个大于0的整数！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_tzybxwygdy0dzs"));
		$("#page_input").focus();
		return;
	}
	if(size<1 ||  !checkPage.test(size) ){
		//alert("每页显示数量必须为一个大于0的整数！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_myxsslbxwygdy0dzs"));
		$("#size_input").focus();
		return;
	}
	if(size>500)
	{
		//alert("每页显示数量不能大于500！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_myxsslbndy500"));
		$("#size_input").focus();
		return;
	}
	$("#pageSize").val($("#size_input").val());
	$("#txtPage").val($("#page_input").val());
	submitForm();
}











function doEdit(mtalId,mtalName,mtalAddress,sortId,methodType)
{
	doCancel();
	if(methodType=='add' && $("#childCode").val()=="")
	{
	   //alert("请选择彩信素材分类！");
	   alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qxzcxscfl"));
	   return;
	}
	//var dialogtitle = "修改彩信素材";
	var dialogtitle = getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_xgcxsc");
	if(methodType=='add'){
		//dialogtitle = "新建彩信素材";
		dialogtitle = getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cxsc");
	}
 	$("#com_add_part").css("display","block");
	$('#com_add_part').dialog({
		autoOpen: false,
		height: 410,
		title:dialogtitle,
		width: 340,
		modal:true,
		resizable:false,
		close:function(){
			doCancel();
		}
	});

     $("#methodType").val(methodType);
     $("#sortId").val(sortId);
     $("#mtalName_old").val(mtalName);
     $("#mtalId").val(mtalId);
	 //$("#mmsMaterialUpload").val(mtalAddress);
	 $("#mtalName").val(mtalName);
	 $("#addrTemp2").val(mtalAddress);
	 if (methodType!='add')
	 {
       	$("#doUp").empty();
    	//$("#doUp").html("重新上传");
    	$("#doUp").html(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scfg"));
	    var type = fileType();
	    if(type == "pic") {
			showPic();
	    } else {
	    	playSound();
	    }
	 }
	 else
	 {
		 	$("#doUp").empty();
	    	//$("#doUp").html("上传附件");
	    	$("#doUp").html(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scfj"));
	 }
	 $('#com_add_part').dialog('open');
}

//修改时判断文件类型
function fileType() {
    var type = $("#addrTemp2").val();
	type = type.toString().substring( type.toString().lastIndexOf(".")+1,type.toString().length);
	type = type.toUpperCase();
	var fileType = "";
	if(type == 'JPG' ||type == 'GIF' ||  type == 'JPEG')
	{
		fileType = "pic";
		 
	}else if(type == 'MID' || type == 'MIDI' || type == 'AMR')
	{
		fileType = "music";
	}
	return fileType;
}

function doCancel()
{
	$("#mtalName").val("");
//	$("#comments").val("");
	$("#mmsMaterialUpload").val("");
	$("#soundpic").empty();
}

function addOrUpdateMaterial(){
	$("#queren").attr("disabled","disabled");
	var addrTemp = $("#addrTemp").val();
	var addrTemp2 = $("#addrTemp2").val();
 	var name=$("#mtalName").attr("value");
 	var childCode = $("#childCode").val();
	var parentCode=$("#parentCode").attr("value");
	var methodType = $("#methodType").val();
	//var comments = $("#comments").val();
	
	
	var lguserid = $("#lguserid").val();
	
	
	
	
	if($.trim(name)=="" || $.trim(name)==null ){// 非空判断
		//alert("素材名称不能为空！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scmcbnwk"));
		$("#mtalName").focus();
		$("#queren").attr("disabled",false);
 		return;
	}else if(name.length>32){
		//alert("素材名称过长，请输入长度为32个字以内的素材名称！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scmcgc"));
		$("#mtalName").focus();
		$("#queren").attr("disabled",false);
 		return;
	}
	else if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})|(\\{1,})/.test(name)){
		//alert("素材名称中包含不允许的特殊字符(\",\',\<,\>,\\)！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scmczbgbyxdtszf"));
		$("#queren").attr("disabled",false);
	    return;
	}
	if(addrTemp2 =="" && methodType =="add")
	{
		//alert("请上传彩信素材！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qsccxsc"));
		$("#queren").attr("disabled",false);
		return;
	}
	/**
	 * 不做重命名检查
	$.post("mat_mmsMaterial.htm",{method:"checkMtalName",mtalName:$("#mtalName").val()},function(result){
	
		if(result == "true" &&  $("#mtalName_old").val() != $("#mtalName").val() )
		{
			alert("该素材名称已存在，请重新输入！");
			$("#mtalName").focus();
 			return;
		}
	
		if(comments.length>256){
			alert("备注信息过长，请输入长度在256个字以内的机构职责！");
			$(".ui-dialog-buttonpane button").attr("disabled",false);
 			return;
		}else if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})|(\\{1,})/.test(comments)){
			alert("素材备注中包含不允许的特殊字符(\",\',\<,\>,\\)！");
			$(".ui-dialog-buttonpane button").attr("disabled",false);
		    return;
		}*/
	  //var level=document.getElementById("level"+superId).value;
	   var childCode = $("#childCode").val();
	   var mtalAddress = addrTemp2;
	   var method = "addMaterial";
	   var sortId = $("#sortId").val();
	   var mtalId = $("#mtalId").val();
	   var methodType = $("#methodType").val();
	   var corpCode = $("#lgcorpcode").val();
	   var temp ;
	   if(methodType == "update")
	   {
	   		method = "updateMaterial";
	   		//temp = "修改";
	   		temp = getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_xg");
	   }else
	   {
	   		method = "addMaterial";
	   		//temp = "新建";
	   		temp = getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_xj");
	   }
			$.ajax({
			url:"mat_mmsMaterial.htm",
			type:"post",
			data:{mtalName:name,
			    childCode:childCode,
			    sortId:sortId,
			    mtalId:mtalId,
			 //   comments:comments,
				parentCode:parentCode,
				mtalAddress:mtalAddress,
				addrTemp2:addrTemp2,
				lguserid:lguserid,
				corpCode:corpCode,
 				method:method
			},
			success:function(data){
				if(data=="true")
				{
					//alert(temp+"彩信素材成功！");
					alert(temp+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cxsccg"));
					if(methodType=="add"){
 						location.href="mat_mmsMaterial.htm?method=find&skip=true";
 					}else{
 						location.href="mat_mmsMaterial.htm?method=find&skip=false";
 					}
					return;
				}else{
					//alert(temp+"彩信素材失败！");
					alert(temp+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cxscsb"));
					$("#queren").attr("disabled",false);
					return;
				}
				

 			}
		});
	/**
	});
	*/
 
}




function doEditSort(methodType)
{
	
	var childCode = $.trim($("#childCode").val());
	if(childCode == "" || childCode.length==0 || childCode == null){
		//alert("请先选择彩信素材分类！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qxxzcxscfl"));
		return;
	}
	 hiddenMenu();
	 //var dialogtitle = "重命名";
	 var dialogtitle = getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cmm");
	 if(methodType =='add')
	 {
		 $("#sortName2").val("");
		// $("#sortNametemp").val("");
		 $("#addOrRename").val("add");
		 //dialogtitle = "新建";
		 dialogtitle = getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_xj");
	 }else
	 {
			
		 $("#sortName2").val($("#sortNametemp").val());
		 $("#addOrRename").val("update");
		 
	 }
		
	$("#com_add_part2").css("display","block");
	$('#com_add_part2').dialog({
		autoOpen: false,
		height: 165,
		width:380,
		title:dialogtitle,
		modal:true
		/**
		});
		buttons:{
			"确定":function(){
				 if(methodType =='add')
				 {
					 menuAdd();
				 }else
				 {
					 menuRename();
				 }
			},
			"取消":function(){
				$('#com_add_part2').dialog('close');
			}
		}*/
	});

     $("#methodType").val(methodType);
     
	 $('#com_add_part2').dialog('open');
}


function menuAdd(){
	
    var sortName =  $.trim($("#sortName2").val());
   	var childCode = $("#childCode2").val(); 
 	var corpCode = $("#lgcorpcode").val(); 
   	if(sortName == "")
   	{
   		//alert("分类名称不能为空！");
   		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_flmcbnwk"));
   		$("#sortName2").focus();
     	return;
   	}else if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})|(\\{1,})/.test(sortName)){
		//alert("分类名称中包含不允许的特殊字符(\",\',\<,\>,\\)！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_flmczbhbyxdtszf"));
		$("#sortName2").focus();
	    return;
	}
   		$.post("mat_mmsMaterial.htm",{method:"checkSortName",sortName:sortName,corpCode:corpCode},function(result){
				if(result == "true")
				{
					//alert("该素材分类名称已存在，请重新输入！");
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_gscflmcycz"));
					return;
				}else{
					$.ajax({
					url:"mat_mmsMaterial.htm",
					type:"post",
					data:{sortName:sortName,
							childCode:childCode,
							corpCode:corpCode,
							method:"addMaterialSort" 
					},success:function(result){
							 if(result == "true")
							 {
								//alert("新建彩信素材分类成功！");
								alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_xjcxscflcg"));
								$("#childCode").val("");
							     $("#sortName2").val("");
							   	 $("#childCode2").val(""); 
							   	 $('#com_add_part2').dialog('close');
								 refreshTree();
							 }else{
								 //alert("新建彩信素材分类失败！");
								 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_xjcxscflsb"));
								 return;
							 }
						}
					});
			 }
		});
  
   }
  
	
   
   function menuRename()
   {
	        var sortNametemp =  $.trim($("#sortNametemp").val());
			var sortName2 =  $.trim($("#sortName2").val());
			var childCode2 = $("#childCode2").val();
			var parentCode2 = $("#parentCode2").val();
			
			var corpCode = $("#lgcorpcode").val();
			
			//$("#addOrRename").val("");
		if (sortName2 == "")
		{
			//alert("分类名称不能为空！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_flmcbnwk"));
	   		$("#sortName2").focus();
			return;
		}else if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})|(\\{1,})/.test(sortName2)){
			//alert("分类名称中包含不允许的特殊字符(\",\',\<,\>,\\)！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_flmczbhbyxdtszf"));
			$("#sortName2").focus();
		    return;
		}else if(sortNametemp == sortName2){
			 //alert("彩信素材名称未做修改！");
			 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cxscmcwzxg"));
	         $('#com_add_part2').dialog('close');
	         return;
		}
	    //if(confirm('"'+ sortNametemp +'" 确认更名为"'+sortName2+'"？'))
		if(confirm('"'+ sortNametemp +'" '+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qrgmw")+'"'+sortName2+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_wh")))
	    {
	    	$.post("mat_mmsMaterial.htm",{method:"checkSortName",sortName:sortName2,corpCode:corpCode},function(result){
			if(result == "true")
			{
				//alert("该素材分类名称已存在，请重新输入！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_gscflmcycz"));
				return;
			}else{
		       $.post("mat_mmsMaterial.htm",
		       {
		       method:"updateSortName",
		       sortName:sortName2,
		       childCode:childCode2
		       },
		       function(result){
		         if(result=='1')
		         {
		         	 //alert("重命名成功！");
		         	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cmmcg"));
		         	$("#childCode").val("");
		         	  $('#com_add_part2').dialog('close');
		         	 refreshTree();
			     }else
		         {
		         	//alert("重命名失败！");
		         	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cmmsb"));
		         }
		       
		       });
			}
			});
	    }/*else
	    {
	    	return true;
	    }
		$("#sortName2").val("");
   	    $("#childCode2").val(""); 
   	    $("#parentCode2").val("");
   	    $("#sortNametemp").val("");
   	   */
	   }
   function menuDelete()
   {
	   var pathUrl = $("#pathUrl").val();
	  // var childCode = $("#childCode").val();
	   var sortName = $("#sortName").val();
		var childCode = $.trim($("#childCode").val());
		var  parentCode = $("#parentCodetemp").val();
	
		
		if(childCode == "" || childCode.length==0 || childCode == null){
			//alert("请选择需要删除的彩信素材分类！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qxzxyscdcxscfl"));
			return;
		}
		if(parentCode == "0" || parentCode == 0){
			//alert("彩信素材顶级类别不能删除！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cxscdjlbbnsc"));
			return;
		}
	   
	   
	   //if(confirm('是否删除"'+sortName+'"分类？'))
	   if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sfsc")+'"'+sortName+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_fl")))
		{
		   $.post("mat_mmsMaterial.htm",
	       {
	       method:"delMaterialSort",
	       childCode:childCode
	       },
	       function(result){
	         if(result =='1')
	         {
	            //alert('删除"'+sortName+'"成功！');
	            alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sc")+'"'+sortName+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cg"));
	            refreshTree();
	            location.reload();// 刷新页面;
	            
	         
		     }else if(result =='0'){
					
		    	 //alert("当前素材分类下有子分类不可删除！");
		    	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_dqscflxyzflbksc"));
		     }else if(result =='2')
		     {
		    	 //alert("当前素材分类（"+sortName+"）存在素材，请先删除素材!")
		    	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_dqscfl")+sortName+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_czsc"))
		     }
			else
	         {
	         	 //alert('删除"'+sortName+'"失败！');
	         	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sc")+'"'+sortName+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sb"));
	         
	         }
	       });
		}else
		{
			return false;
		}
	   	 hiddenMenu();		  
   }
 
			
   function getSortTree()
   {
	   var items = "";
		var childCode = $("#childCode").val();
	
	 	$('input[name="checklist"]:checked').each(function(){	
			items += $(this).val()+",";
	 	});
	 	if(items==""){
			//alert("请选择您要移动的彩信素材！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qxznyyddcxsc"));
			return;
		} 
		if (items != "")
		{
			items = items.toString().substring(0, items.lastIndexOf(','));
		}
	   var childCode;
	   var sortName;
	   var time = new Date();
	   var inheritPath=$('#inheritPath').val();
 	  // $('#transferTree').load(inheritPath+'/t_mmsMaterialsTree.jsp',{time:time});
 	   material = window.showModalDialog( inheritPath+"/t_mmsMaterialsTree2.jsp?timer="+time+"&isReturnDepId=false&childCode="+childCode,"","dialogWidth="+ieWidth+"px;dialogHeight="+ieHeight+"px;help:no;status:no;center:yes");   
 	   if (material == null)
 		{
 		    material="&";
 			//sortName = "全部";
 			sortName = getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qb");
 			childCode = "";
 		}
 	    else
 	    {
 	    	sortName = material.split("&")[0];
 	    	childCode = material.split("&")[1];
 	    }
 	   if(childCode != "")
 	   {
 		  //if(confirm("您确定要移动所选择的彩信素材？")==true  ){
 	 	  if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_ndqyydsxzdcxsc"))==true  ){
 				$.post("mat_mmsMaterial.htm?method=updateSortId",{ids:items,childCode:childCode},function(result){
 					if(result>=1)
 					{
 						//alert("移动成功,共移动"+result+"条信息！");
 						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_ydcggyd")+result+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_txx"));
 						window.location.reload();
 					}else{
 						//alert("移动失败！");
 						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_ydsb"));
 						window.location.reload();
 					}
 				});
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
   
   
   
   
  //~~~~~~~~~~~~~~~~~~~~jsp中fuction 
   
 String.prototype.replaceAll  = function(s1,s2){    
       return this.replace(new RegExp(s1,"gm"),s2); 
    }
$(document).ready(function(){
	getLoginInfo("#getloginUser");
	var time = new Date();
	//$('#materialTree').load('iPath/t_mmsMaterialsTree.jsp',{time:time});
//	$('#tableInfo').load('mat_mmsMaterial.htm',{method:'getTable',time:time});
    $("#addrTemp2").val("");


    $("#addDepNew").hover(function() {
		$(this).removeClass("depOperateButton1");
		$(this).addClass("depOperateButton1On");
	}, function() {
		$(this).addClass("depOperateButton1");
		$(this).removeClass("depOperateButton1On");
	});

	$("#updateDepNew").hover(function() {
		$(this).removeClass("depOperateButton2");
		$(this).addClass("depOperateButton2On");
	}, function() {
		$(this).addClass("depOperateButton2");
		$(this).removeClass("depOperateButton2On");
	});

	$("#delDepNew").hover(function() {
		$(this).removeClass("depOperateButton3");
		$(this).addClass("depOperateButton3On");
	}, function() {
		$(this).addClass("depOperateButton3");
		$(this).removeClass("depOperateButton3On");
	});
	var lgcorpcode = $("#lgcorpcode").val();
	var iPath = $("#iPath").val();
	$("#materialTree").load(iPath+"/mat_mmsMaterialTree.jsp?lgcorpcode="+lgcorpcode);
	showMmsTable();
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
	$('#comments').keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		if (iKeyCode == 39) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	}
	);
    
	$('#comments').blur(function(e) {
		$(this).val($(this).val().replaceAll("'",""));
	}
	);
	
	filedivHover();
});
function filedivHover()
{
   	$("#mmsMaterialUpload").hover(function(e){
       $("#doUp").css("text-decoration","underline");
	},function(e){
          $("#doUp").css("text-decoration","none");
	});
}



function upfile()
{
	var fileName = "mmsMaterialUpload";
	var type = $("#mmsMaterialUpload").val();
	if (type == "")
	{
		//alert("请选择要上传的图片文件！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qxzyscdtpwj"));
		return;
	}
	type = type.toString().substring( type.toString().lastIndexOf(".")+1,type.toString().length);
	type = type.toUpperCase();
	var fileType;
		if(type == 'JPG' ||type == 'GIF' ||type == 'JPEG')
	{
		fileType = "pic";
		 
	}else if(type == 'MIDI' || type == 'MID' || type == 'AMR')
	{
		fileType = "music";
	}else
	{
	  //alert("不支持的图片或音频格式！");
	  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_bzcdtphypgs"));
	  return ;
	}
	var mms = $("#addrTemp2").val();
	
	var pathUrl = $("#pathUrl").val();
	$.ajaxFileUpload({ 
		    url:pathUrl+'/mat_mmsMaterial.htm?method=upload&fileType='+fileType+'&mms='+mms+'&lgcorpcode='+$("#lgcorpcode").val()+'&lguserid='+$("#lguserid").val(), 
		    secureuri:false, //是否启用安全提交，默认为false
		    fileElementId:fileName, //与页面处理代码中file相对应的ID值
		    dataType: 'json', //返回数据类型:text，xml，json，html,scritp,jsonp五种
		    success: function (data) {
			    if (data != null && data.url != "false"&&data.url!="uploadfail")
			    {
			    	$("#addrTemp").val($("#mmsMaterialUpload").val());
			    	$("#addrTemp2").val(data.url);
			    	if(fileType == "pic")
			    	{
			    		showPic();
			    	}else
			    	{
			    		playSound();
			    	}
			    	//alert("上传文件成功！");
			    	//$("#doUp").hide();
			    	//$("#reUp").show();
			       	$("#doUp").empty();
			    	//$("#doUp").html("重新上传");
			    	$("#doUp").html(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scfg"));
				    }
			    else if(data != null && data.url == "false")
			    {
                    //alert("上传的文件大小不能超过80KB！");
                    alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scdwjdxbncg80"));
                    return;
			    }  
				    else
			    {
			    	$("#addrTemp").val("");
			    	$("#addrTemp2").val("");
			    	//alert("上传文件失败！");
			    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_scwjsb"));
			    }
		    } 
		 });
}

function sizecheck(name)
{
var MAXSIZE = 50 * 1024;
var obj = document.getElementById(name);
var img = new Image();
img.dynsrc= obj.value;         
var filesize=img.fileSize; 

var flag = false;
if ($.browser.msie) {//查看是否是IE
	if(obj.readyState == "complete") {
		alert(filesize)
		if (filesize <= MAXSIZE) {
			flag = true;
		}
	}
} else {
	var file = $("input:file[name='"+name+"']")[0];

	if (file.files[0].fileSize <= MAXSIZE) {
		flag = true;
	}
}
return true;
}		
function showPic()
{
	var isjq = $("#isCluster").val();
	var fileHttpUrl= $("#httpUrl").val();
	var isjqUrl = "";
	var addrTemp = $("#addrTemp2").val();
	if(1==isjq){
		//isjqUrl = fileHttpUrl;
			var pathUrl = $("#pathUrl").val();
			$.post(pathUrl+"/mat_mmsMaterial.htm?method=checkFile", {
				url : addrTemp
			},
			function(result) {
				if (result == "true") {
					
				}
			});
	}
	//var addrTemp = isjqUrl+$("#addrTemp2").val();
	
	$("#soundpic").empty();
	$("#soundpic").append("<img id='preWatch' src='"+addrTemp+"'/>");
} 
function playSound(){
	var isjq = $("#isCluster").val();
	var fileHttpUrl= $("#httpUrl").val();
	var isjqUrl = "";
	var addrTemp = $("#addrTemp2").val();
	if(1==isjq){
		//isjqUrl = fileHttpUrl;
		var pathUrl = $("#pathUrl").val();
			$.post(pathUrl+"/mat_mmsMaterial.htm?method=checkFile", {
				url : addrTemp
			},
			function(result) {
				if (result == "true") {
					
				}
		});
	}
	//var addrTemp = isjqUrl+$("#addrTemp2").val();
	$("#soundpic").empty();
	//$("#soundpic").append("<embed src='"+addrTemp+"' autostart='false' loop='false'/>");
	// $("#soundpic").html("<embed id='swfEmbed' src='"+addrTemp+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='70' height='40'></embed>");
	if(addrTemp.length>0){
		 var soundtype = $.trim(addrTemp.substring(addrTemp.lastIndexOf(".")+1));
//		 if(soundtype == "amr" || soundtype == "AMR"){
//		     //$("#soundpic").html("<object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95'  width='70' height='40'><param name='FileName' value='"+addrTemp+"'/></object>");
//		 	$("#soundpic").html("<embed src='"+addrTemp+"' style='HEIGHT: 45px; WIDTH: 250px' type='audio/mpeg' AUTOSTART='1' loop='0'/>");
//		 }else{
//			//$("#soundpic").html("<embed id='swfEmbed' src='"+addrTemp+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='70' height='40'></embed>");
//		  	$("#soundpic").html("<object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95'  width='230' height='45' type=application/x-oleobject><param name='FileName' value='"+addrTemp+"'/><param name='AutoStart' value='0'/></object>");
//		 }

         var explorer = window.navigator.userAgent.toLowerCase();
         var ver=null;
         //如果是ie 
         if (explorer.indexOf("msie") >= 0) {
         	//版本号
             ver = explorer.match(/msie ([\d.]+)/)[1];
         }
         //IE浏览器，就IE9不显示预览音频
         //ie 
         if (explorer.indexOf("msie") >= 0&&ver!=9) {
     	 	if(soundtype == "mid" || soundtype == "MID" || soundtype == "midi" || soundtype == "MIDI")
     	  	{
     		  	//如果是websphere
     	  	    if(serverName == "WebSphere")
     	  	    {
     		  	   //QuickTime Player
     	  	    	$("#soundpic").html("<embed src='"+addrTemp+"' style='HEIGHT: 45px; WIDTH: 250px' type='audio/mpeg' AUTOSTART='1' loop='0'/>");
     	  	    }
     		  	else
     	  	    {
     		  	    //media play
     				$("#soundpic").html("<object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95'  width='230' height='45' type=application/x-oleobject><param name='FileName' value='"+addrTemp+"'/><param name='AutoStart' value='0'/></object>");
     	  	    }
     		}
     		else
     		{
     			//QuickTime Player
     	  	    $("#soundpic").html("<embed src='"+addrTemp+"' style='HEIGHT: 45px; WIDTH: 250px' type='audio/mpeg' AUTOSTART='1' loop='0'/>");
     	  	}
         }else{
        	 $("#soundpic").html("<span style='width:250px;font-size:12px;padding:10px;' >文件上传成功！如需播放试听，请使用IE8及IE10以上浏览器。</span>");
         }
	}
//$("#soundpic").append("<span>音频文件不支持预览</span>");
//$("embed").attr("src",addrTemp);
}
function showMmsTable()
{
	setLeftHeight();
	var userId = $('#lguserid').val();
	var lgcorpcode = $('#lgcorpcode').val();
	var mmsMaterialUrl = $('#mmsMaterialtableUrl').val();
	$('#tableInfo').load(mmsMaterialUrl,{method:'getTable',userId:userId,lgcorpcode:lgcorpcode});
}

function addRename(){
 var operation = $("#addOrRename").val();
 if(operation == "add"){
	 menuAdd();
 }else if(operation == "update"){
	 //$("#sortName2").val($("#sortNametemp").val());
	 menuRename();
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
	function init(initSelect){  //初始化分页
		var $pageWrapper = $("#pageInfo");

		if(initSelect != undefined) {
	        page.initSelect = initSelect;
		}
		
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
		$(".page_index").live("click",function(){  
			var newPage = $(this).attr("pageIndex");
			jumpTo(newPage);
		});
		
		$(".priv_page").click(function(){
			privPage();
		});
		$(".next_page").click(function(){
			nextPage();
		});
		if(page.initSelect.length != 1) {
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
	    }
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
		initPage : function(total,pageIndex,pageSize,totalRec,initSelect) { //指定参数进行分页
			page.total = total;
			page.pageIndex = pageIndex;
			page.pageSize = pageSize;
			page.totalRec = totalRec;
			init(initSelect);
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
				   
   
   
   
   
   
 