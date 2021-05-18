
//检查定时监控的任务是否已经执行
function keydown(e) // 支持ie 火狐 键盘按下事件
{        
	var currKey=0,e=e||event;
	if(e.keyCode==13) return false;  
}

document.onkeydown=keydown;

function form(){
	var time=$("#sendtime").val();
	if(time==""){
		//alert("请选择开始时间！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qxzkssj"));
	}else
	if($('#exit').val()==1)
	{
		//alert("已在该业务定时,不能重复设置！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yzgywds"));
	}else
	{
		$.post("eng_mtService.htm",{method:"getRunState",serId:$("#serId").val()},
				function(result)
				{
					if(result=="0")
					{
						//alert("该业务已禁用，无法定时！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_gywyjy"));
					}else if(result=="error")
					{
						//alert("验证业务状态是否运行失败，请确认网络是否正常！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yzywztsfyxsb"));
					//}else if(confirm('请检查所填时间格式是否正确，正确请点击“确定”，否则点击“取消”进行修改！')==true)
					}else if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qjcstsjgesfzq"))==true)
					{
						$(window.parent.document).find("#serTimeEditok").attr("disabled","disabled");
						$('#form1').attr("action",$('#form1').attr('action')+'&lgcorpcode='+$('#lgcorpcode').val());
						$('#form1').submit();
					}
				}
		);
	}
}
		//检查定时任务是否存在
		
//当前时间与服务器时间对比
function checkServerTime(){
	
	//------------------------------------guodw
	var sendTime = $('#sendtime').val();
	var serverTime ;
	$.post("eng_mtService.htm", {
		method : "getServerTime"
	}, function(msg) {
		if(msg == null || msg.length==0){
			//alert("获取服务器时间失败！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_hqfwqsjsb"));
			return;
		}
		serverTime = msg;
		var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
		var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
        date1.setMinutes(date1.getMinutes() - 1, 0, 0);
		if (date1 <= date2) {
			//alert("预发送时间小于服务器当前时间！请合理预定发送时间");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yfssjxyfwqdqsj"));
			$("#sendtime").val("");
			return;
		}
		form();
	});
	
	//----------------------------------------
}

function showAlert(obj){
	if(obj.value == '2'){
		$("#fontZhu").css("display","");
	}else{
		$("#fontZhu").css("display","none");
	}
}


