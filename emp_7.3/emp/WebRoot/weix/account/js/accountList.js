/*公众帐号管理-列表页面*/
//搜索
function doSearch(){
	 var form = $("form[name='pageForm']");
	 var url = $("#pathUrl").val()+"/cwc_acctmanager.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
   $("#pageForm").attr("action",url);
   document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
   document.forms['pageForm'].submit();
}

//--添加公众帐号弹出框-start--
function showAddAccountTmp(showType)
{
	$("#addAcctTmpDiv").css("display","block");
	$("#addAcctTmpFrame").css("display","block");

	$("#addAcctTmpDiv").dialog({
		autoOpen: false,
		height:480,
		width: 660,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			$("#addAcctTmpDiv").css("display","none");
			$("#addAcctTmpFrame").css("display","none");
		}
	});
	
    $("#addAcctTmpFrame").attr("src",$("#pathUrl").val()+"/cwc_acctmanager.htm?method=doAdd&lguserid="
    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType="+showType+"&time="+new Date().getTime());
          
	$("#addAcctTmpDiv").dialog("open");
}

function closeaddAcctTmpDiv()
{
	$("#addAcctTmpDiv").dialog("close");
	$("#addAcctTmpFrame").attr("src","");
}
//--添加公众帐号弹出框-end--

//打开微信公告提示

function openWeiTips(txt){
	window.parent.parent.wei_tips_dialog(txt);
}

//--编辑公众帐号弹出框-start--
function showEditAcctTmp(tmId)
{
	$("#editAcctTmpDiv").css("display","block");
	$("#editAcctTmpFrame").css("display","block");
	//修改
	$("#editAcctTmpDiv").dialog({
		autoOpen: false,
		height: 520,
		width: 660,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			$("#editAcctTmpDiv").css("display","none");
			$("#editAcctTmpFrame").css("display","none");
		}
	});
    $("#editAcctTmpFrame").attr("src",$("#pathUrl").val()+"/cwc_acctmanager.htm?method=doEdit&tmId="+tmId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
          
	$("#editAcctTmpDiv").dialog("open");
	}

   function closeEditAcctTmpDiv()
	{
		$("#editAcctTmpDiv").dialog("close");
	$("#editAcctTmpFrame").attr("src","");
	
}
//--编辑公众帐号弹出框-end--
	   
$(function(){
	//列表页面需要执行的
	
	getLoginInfo("#hiddenValueDiv");
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
		}, function() {
		$(this).removeClass("hoverColor");
	});
	
	//分页
	initPage($("#currentTotalPage").val(),$("#currentPageIndex").val(),$("#currentPageSize").val(),$("#currentTotalRec").val());
	
	//复制效果
	$('.sd_popcard').zclip({
	path: $("#iPathUrl").val()+'/js/ZeroClipboard.swf',
	copy:function(){
		var _this=this;
		return $(_this).parent().find('.zcopy').text();
	}
	});

	//复制按钮的显示与隐藏
	$('.getUrl,.getToken').hover(function(){
		$(this).find('.zcopy').addClass('blueColor');
		$(this).find('.sd_popcard').removeClass('visible_hidden');
	},function(){
		$(this).find('.zcopy').removeClass('blueColor');
		$(this).find('.sd_popcard').addClass('visible_hidden');
	});
	
	var txt = getJsLocaleMessage("weix","weix_qywx_protocol");
	var approve = $("#approve").val();
	if("1"!=approve){
		//开启协议弹出框
		openWeiTips(txt);
	}
});


function getApprove(){
	$.post($("#pathUrl").val()+"/cwc_acctmanager.htm",{
	    method: "doRelief",
	    approve: "1",
		corpCode: $("#lgcorpcode").val()
		},function(message){
        	if(message.indexOf("@")==-1){
        		return false;
			}else if (message.substr(message.indexOf("@")+1) == "success") {
				$("#approve").val("1");
				window.parent.parent.wei_tips_close();
				return false;
			}
	});
	return false;
}
