var curDepName = ""
var time=new Date();
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
function showAll()
{
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
		//alert("未勾选信息！");
		alert(getJsLocaleMessage('client','client_js_permissions_noselect'));
		return;
	}else
	{
	
		if(confirm(getJsLocaleMessage('client','client_js_permissions_confiredelselect'))){
				
		    $.ajax({
		        url: 'a_permissions.htm?method=getInfo',
		        data:{
		    		ids:dsgIds,
		    		opType:"delete",
		    		bookType:bookType2
		        },
		        type: 'post',
		       // timeout: 3000,
		        error: function(){
		           //alert("删除失败！");
		           alert(getJsLocaleMessage('client','client_common_deletefalse'));
		           return;
		        },
		        success: function(result){
					if (result>=0) {
						//alert("删除成功,共移除"+result+"条信息！");
						alert(getJsLocaleMessage('client','client_js_permissions_delsuctotal')+result+getJsLocaleMessage('client','client_js_permissions_infos'));
						submitForm();
					}else{
						//alert("删除失败！");
						alert(getJsLocaleMessage('client','client_common_deletefalse'));
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
		//alert("未勾选信息！");
		alert(getJsLocaleMessage('client','client_js_permissions_noselect'));
		return;
	}else
	{
		var lgcorpcode = $('#lgcorpcode').val();
		if(confirm(getJsLocaleMessage('client','client_js_permissions_confiredelselect'))){
				
		    $.ajax({
		        url: 'cli_permissions.htm?method=getInfo',
		        data:{
		    		ids:dsgIds,
		    		opType:"delete",
		    		lgcorpcode:lgcorpcode
		        },
		        type: 'post',
		       // timeout: 3000,
		        error: function(){
		           //alert("删除失败！");
		        	alert(getJsLocaleMessage('client','client_common_deletefalse'));
		           return;
		        },
		        success: function(result){
					if (result.indexOf(getJsLocaleMessage('client','client_common_delete')) == -1 && result>0) {
						//alert("删除成功,共移除"+result+"条信息！");
						alert(getJsLocaleMessage('client','client_js_permissions_delsuctotal')+result+getJsLocaleMessage('client','client_js_permissions_infos'));
						submitForm();
					}else if (result.indexOf(getJsLocaleMessage('client','client_common_delete')) != -1 && result.indexOf("0") != -1) {
						//alert("企业管理员顶级部门权限不能被删除");
						alert(getJsLocaleMessage('client','client_js_permissions_cannotdel'));
						return;
					}
					else if(result.indexOf(getJsLocaleMessage('client','client_common_delete')) != -1 && result.indexOf("0") == -1){
						//alert("共移除"+result.substring(0,1)+"条,"+"其中企业管理员顶级部门权限不能被删除");
						alert(getJsLocaleMessage('client','client_js_permissions_totalremove')+result.substring(0,1)+getJsLocaleMessage('client','client_js_permissions_donotdel'));
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
		//alert("未勾选信息！");
		alert(getJsLocaleMessage('client','client_js_permissions_noselect'));
		return;
	}else
	{
		var lgcorpcode = $('#lgcorpcode').val();
		if(confirm(getJsLocaleMessage('client','client_js_permissions_confiredelselect'))){
				
		    $.ajax({
		        url: 'cli_permissions.htm?method=getInfo',
		        data:{
		    		ids:dsgIds,
		    		opType:"delete",
		    		bookType:bookType2,
		    		lgcorpcode:lgcorpcode
		        },
		        type: 'post',
		       // timeout: 3000,
		        error: function(){
		          // alert("删除失败！");
		        	alert(getJsLocaleMessage('client','client_common_deletefalse'));
		           return;
		        },
		        success: function(result){
					if (result.indexOf(getJsLocaleMessage('client','client_common_delete')) == -1 && result>0) {
						//alert("删除成功,共移除"+result+"条信息！");
						alert(getJsLocaleMessage('client','client_js_permissions_delsuctotal')+result+getJsLocaleMessage('client','client_js_permissions_infos'));
						submitForm();
					}else if (result.indexOf(getJsLocaleMessage('client','client_common_delete')) != -1 && result.indexOf("0") != -1) {
						//alert("企业管理员顶级部门权限不能被删除");
						alert(getJsLocaleMessage('client','client_js_permissions_cannotdel'));
						return;
					}
					else if(result.indexOf(getJsLocaleMessage('client','client_common_delete')) != -1 && result.indexOf("0") == -1){
						//alert("共移除"+result.substring(0,1)+"条,"+"其中企业管理员顶级部门权限不能被删除");
						alert(getJsLocaleMessage('client','client_js_permissions_totalremove')+result.substring(0,1)+getJsLocaleMessage('client','client_js_permissions_donotdel'));
						submitForm();
					}	
						   
		        }
		    });
		}
	}
}


function bangding()
{
	var dCodeThird=$('#depId').val();
	if(dCodeThird=="")
	{
		//alert("请先选择一个机构！");
		alert(getJsLocaleMessage('client','client_js_permissions_selectoneorg'));
	}else
	{ 
        $("#com_add_Dom2 curDep").html($('#depName').text());
        var time=new Date();
        $('#showSysuser').load(path+'/cli_permissions.htm?method=getSysuserList&opType='+"getSysuserList"+'&bookType='+bookType,{time:time});
	}
}

$(function(){
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
				if(booktype == "client")
				{
					 $('#servletUrl').val("cli_permissions.htm?method=toClientPm");
					 $('#ifra').attr('src',$('#inheritPath').val()+'/a_addrbookDepTree.jsp?treemethod=getClientDepTreeJson&getType=privi');
					
				}else
				{
					$('#servletUrl').val("cli_permissions.htm?method=toEmployeePm");
					 $('#ifra').attr('src',$('#inheritPath').val()+'/a_addrbookDepTree.jsp?treemethod=getEmpSecondDepJson&getType=privi');
					 
				}
				var servletUrl = $('#servletUrl').val();
				var time=new Date();
				 
				 
 				var servletUrl=$('#servletUrl').val();
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
	var time=new Date();
	var pageIndex=$('#txtPage').val();
	var pageSize=$('#pageSize').val();
 	var depCode=$('#depId').val();
	var userName =$('#username').val();
	var servletUrl=$('#servletUrl').val();
	var lguserid=$('#lguserid').val();
	var lgcorpcode=$('#lgcorpcode').val();
	$('#permissionsInfo').load(servletUrl,{time:time,depCode:depCode,pageIndex:pageIndex,pageSize:pageSize,userName:userName,lguserid:lguserid,lgcorpcode:lgcorpcode});
	afterSubmitForm();
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
		//alert("对不起，您是个人权限，不能进行绑定操作！");
		alert(getJsLocaleMessage('client','client_common_personpermission'));
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
		//alert("请先选择一个机构！");
		alert(getJsLocaleMessage('client','client_js_permissions_selectoneorg'));
		return;
	} 
	$("#com_add_Dom2").css("display","block");
	var selDepName = $('#depName2').val();
	var selDepId = $('#depId').val();
    //$("#curDep").html($('#depName2').val());
    var lguserid = $("#lguserid").val();
    var lgcorpcode = $("#lgcorpcode").val();

    var src = 'cli_permissions.htm?method=getSysuserList&pageSize=10&opType='
		+"getSysuserList"+'&selDepName='+selDepName+'&selDepId='
		+selDepId+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode;
    src = encodeURI(src);

    //检测一下该部门是否被删除
	$.post("cli_permissions.htm?method=checkdepDel", {
		
		selDepId:selDepId
	}, function(result) {
		if(result !== ""){
			//alert("该机构不存在，可能已被删除，不能进行绑定操作！");
			alert(getJsLocaleMessage('client','client_js_permissions_notbindnullorg'));
			window.location.reload();
		}else{
		    $("#bindUserFrame").attr("src", src,{time:time.getTime()});
			$('#com_add_Dom2').dialog('open');
		}
       
	});

}

function doBind()
{
	if($("#permissionType").val()=="1")
	{
		//alert("对不起，您是个人权限，不能进行绑定操作！");
		alert(getJsLocaleMessage('client','client_common_personpermission'));
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
		//alert("请先选择一个机构！");
		alert(getJsLocaleMessage('client','client_js_permissions_selectoneorg'));
		return;
	} 
	$("#com_add_Dom2").css("display","block");
	//zhezhao();
    $("#curDep").html($('#depName2').val());
    var lguserid = $("#lguserid").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var selDepName = $('#depName2').val();
	var selDepId = $('#depId').val();
	 $("#bindUserFrame").attr("src",'cli_permissions.htm?method=getSysuserList&pageSize=10&opType='+"getSysuserList"+'&selDepName='+selDepName+'&selDepId='+selDepId+'&bookType='+bookType+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode,{time:time});

	 $('#com_add_Dom2').dialog('open');
}

$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
    $("#toggleDiv").toggle(function() {
        $("#condition").hide();
        $(this).removeClass("collapse");
    }, function() {
        $("#condition").show();
        $(this).addClass("collapse");
    });
	
	getLoginInfo("#getloginUser");
		
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
	$('#com_add_Dom2').dialog({
		autoOpen: false,
		height: 475,
		width: 700,
		resizable:false,
		modal: true,
		open:function(){

		},
		close:function(){
			$("#bindUserFrame").attr("src","");
		}
	});
	resizeDialog($("#com_add_Dom2"),"ydkfDialogJson","kfdx_txlqxgl_test1");
	setLeftHeight();
	$('#search').click(function(){submitForm();});
});

function closeBinddiv()
{
	$("#com_add_Dom2").dialog("close");
	$("#bindUserFrame").attr("src","");
}

function bindok()
{
	window.frames['bindUserFrame'].ok();
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

function cancel()
{
	$(".ui-dialog-buttonpane button").attr("disabled",false);
	$('#com_add_Dom2').dialog('close');
	$("#dropMenu").hide();
	submitForm();
}
