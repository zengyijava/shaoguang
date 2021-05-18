function changestate(id)
		{
			var lgcorpcode = $('#lgcorpcode').val();
			var runState=$.trim($('#runState'+id).attr("value"));
			var pathUrl = $("#pathUrl").val();
			$.post(pathUrl+"/bir_birthdaySendClient.htm?method=changeState",{lgcorpcode:lgcorpcode,setupid:id,isUse:runState},function(result)
			{
				if (result== "true") 
				{
					// alert("更新状态成功！");
					alert(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_updatesuccess'));
					$("#pageForm").submit();
				}
				else 
				{
					// alert("更新状态失败！");
					alert(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_updatefalse'));
				}
			});
}

function modify(t)
{
		$("#msgcont").empty();
		//用label显示短信内容
		//$("#msgcont").text($(t).children("label").children("xmp").text());
		//修改成用textarea显示短信内容
		$("#msgcont").val($(t).children("textarea").val());
		$('#modify').dialog('open');
}

function submitForm1(){
            var lguserid = $("#lguserid").val();
            var lgcorpcode = $("#lgcorpcode").val();
            $("#pageForm").attr("action",'bir_birthdaySendClient.htm?method=find&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode);
        	document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
        	document.forms['pageForm'].submit();
}

function deleteSetup1(setupId,type)
{
           	var setupId;
           	var type;
			if(confirm(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_confiretodeletset')))
			{
				$("#deleteSetup").attr("disabled",true);
					var lgcorpcode=$("#lgcorpcode").val();
					$.post("bir_birthdaySendClient.htm",{method:"deleteSetup",lgcorpcode:lgcorpcode,setupId:setupId,type:type},function(data)
					{
						if(data=="true")
						{
							// alert("删除成功！");
							alert(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_deletesuccess'));
							reloadPage();
						}else
						{
							// alert("删除失败！");
							alert(getJsLocaleMessage('dxkf','dxkf_srzf_birManage_deletefalse'));
							$("#deleteSetup").attr("disabled","");
						}
					});
			}
		
}

function modifyNew(t)
{
    $("#msgcont").empty();
    //用label显示短信内容
    $("#msgcont").text($(t).children("xmp[name='msgXmp']").text());
    //修改成用textarea显示短信内容
    $('#modify').dialog('open');
}

