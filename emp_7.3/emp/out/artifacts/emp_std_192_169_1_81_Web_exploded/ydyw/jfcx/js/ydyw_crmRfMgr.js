var path = $('#path').val();
//弹出DIV层
function showRefundDiv(id,type)
{
	var lguserid=$("#lguserid").val();
	var lgcorpcode=$("#lgcorpcode").val();
	var urlpath = path+"/ydyw_crmRfMgr.htm?method=getBackMoney&id="+id+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&type="+type;
	var height = 0;
	if(type=="1")
	{
		height = 300
	}
	else
	{
		height = 140
	}
	$("#backmoney").dialog
	({
		autoOpen: false,
		width: 480,
		height:height,
		resizable:false,
		modal: true,
		open:function()
		{
			$('#backmoneyIframe').attr("src",urlpath);
		},
		close:function()
		{
			$("#backmoney").css("display","none");
		}
	});
       
	$("#backmoney").dialog("open");
}

//关闭弹出层
function closeDiv()
{
	parent.$('#backmoneyIframe').attr("src","");
	parent.$("#backmoney").dialog("close");
}
//只能输入数字
function numberControl(va)
{
	var pat=/^\d*$/;
	if(!pat.test(va.val()))
	{
		va.val(va.val().replace(/[^\d]/g,''));
	}
}

function save()
{
	//退费金额 
	var money = $('#backmoeny').val();
	var type = $('#type').val();
	var method = "";
	var ids = $("#refundId").val();
	
	if(money==null||""==money)
	{
		alert(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_15"));
		return false;
	}
	if("1"==type)
	{
		//套餐资费
		var taocanmoney = $('#taocanmoney').val();
		//
		if(money-taocanmoney>0)
		{
			alert(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_16"));
			return false;
		}
		method = "singleRefund";
	}
	else if("2"==type)
	{
		method = "muliteRefund";
	}
	var lguserid=$("#lguserid").val();
	var lgcorpcode=$("#lgcorpcode").val();
	//退费
	if(confirm(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_17")))
	{
		$.post(	path+"/ydyw_crmRfMgr.htm",
				{
				method:method,
				ids:ids,
				money:money,
				lguserid:lguserid,
				lgcorpcode:lgcorpcode
				},	
				function(data)
				{
					if(data=="success")
					{
						/*退费申请成功*/
						alert(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_18"));
						parent.location.href = path+"/ydyw_crmRfMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
					}
					else if(data=="fail")
					{
						/*退费申请失败*/
						alert(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_19"));
					}
					else if(data=="more")
					{
						/*退费申请失败!退费金额不允许大于套餐资费*/
						alert(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_16"));
					}else
					{
						/*退费失败*/
						alert(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_7"));
					}
				});
		}
}

//全选
$('#checkAll').click(function() {
	if ($('#checkAll').is(':checked')) {
		$('[name=crmRfmgrCB]:checkbox').attr("checked", true);  
	} else {
		$('[name=crmRfmgrCB]:checkbox').attr("checked", false);  
	}
});	

//去掉全选勾
$('[name=crmRfmgrCB]:checkbox').click(function() {
	$('[name=crmRfmgrCB]:checkbox').each(function() {
		if(!this.checked) {
			$('#checkAll').attr("checked", false);  
		}				
	});
});	

function muliteRefund()
{
	var ids = "";
	var count = 0;
	$('[name=crmRfmgrCB]:checkbox').each(function() {
		if(this.checked) {
			count = count+1 ;
			ids += this.value;
			ids += ",";						
		}				
	});
	if(ids=="")
	{
		alert(getJsLocaleMessage("ydyw","ydyw_qyjfcx_khtfgl_text_20"));
		return ;
	}
	//打开退费页面
	if(count = 1){
        showRefundDiv(ids.substring(0,ids.lastIndexOf(",")),'1');
	}else{
        showRefundDiv(ids.substring(0,ids.lastIndexOf(",")),'2');
    }
}

