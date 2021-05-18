function deleteSetup1(setupId,type)
{
           	var setupId;
           	var type;
			if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_136')))
			{
				$("#deleteSetup").attr("disabled",true);
					var lgcorpcode=$("#lgcorpcode").val();
					$.post("bir_birthdaySendEMP.htm",{method:"deleteSetup",lgcorpcode:lgcorpcode,setupId:setupId,type:type},function(data)
					{
						if(data=="true")
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_137'));
							reloadPage();
						}else
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_138'));
							$("#deleteSetup").attr("disabled","");
						}
					});
			}
		
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

function changestate(id)
{
			var lgcorpcode = $('#lgcorpcode').val();
			var runState=$.trim($('#runState'+id).attr("value"));
			var pathUrl = $("#pathUrl").val();
			$.post(pathUrl+"/bir_birthdaySendEMP.htm?method=changeState",{lgcorpcode:lgcorpcode,setupid:id,isUse:runState},function(result)
			{
				if (result== "true") 
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_139'));
					$("#pageForm").submit();
				}
				else 
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_140'));
				}
			});
}


function modifyNew(t)
{
    $("#msgcont").empty();
    //用label显示短信内容
    $("#msgcont").text($(t).children("xmp[name='msgXmp']").text());
    //修改成用textarea显示短信内容
    $('#modify').dialog('open');
}

