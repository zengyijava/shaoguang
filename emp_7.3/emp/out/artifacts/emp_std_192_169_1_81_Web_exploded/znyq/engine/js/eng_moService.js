
function changestate(id)
{
	var lgcorpcode = $('#lgcorpcode').val();
	var runState=$.trim($('#runState'+id).attr("value"));
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/eng_moService.htm?method=changeState",{runState:runState,serId:id},function(result)
	{
		if (result== "true") 
		{
			//alert("更新状态成功！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_gxztcg"));
			$("#pageForm").submit();
		}
		else 
		{
			//alert("更新状态失败！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_gxztsb"));
		}
	});
}

function getMoInfos(serId)
{
	$("#serInfoflowFrame").attr("src","eng_moService.htm?method=doEdit&serId="+serId);
    $('#MoInfos').dialog('open');
}
function closeMoInfos(){
	$('#MoInfos').dialog('close');
}
function closeDiv()
{
	$("#MoInfos").dialog("close");
	$("#serInfoflowFrame").attr("src","");
}

function closeMoConfDiv()
{
	$("#moConfDiv").dialog("close");
	$("#moConfFrame").attr("src","");
}

$(function(){
  $('#content select').isSearchSelectNew({'width':'60','isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
	  var idx=$(data.box.self).attr("data");
	  changestate(idx);
  },function(data){
	  var self=$(data.box.self);
	  self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
  });
  $('#serState').isSearchSelect({'width':'182','isInput':false,'zindex':0});
  $('#identifyMode').isSearchSelect({'width':'182','isInput':false,'zindex':0});
  
})

function toMoConf(){
	var lguserid = $('#lguserid').val();
	var lgcorpcode = $('#lgcorpcode').val();
	$("#moConfFrame").attr("src","eng_moService.htm?method=toMoConf");
	$('#moConfDiv').dialog('open');
}

function closeMoConf()
{
	$("#moConfDiv").dialog("close");
	$("#moConfFrame").attr("src","");
}