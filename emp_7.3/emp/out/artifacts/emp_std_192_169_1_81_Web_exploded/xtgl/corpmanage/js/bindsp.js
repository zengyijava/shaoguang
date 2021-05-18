function del(busCode,SpUser,op)
{
	var pathUrl = $("#pathUrl").val();
	$("a[name='querySP']").attr("disabled","disabled");
	//var opr = (op=="0")?"确定要使该绑定关系失效吗？":"确定要激活该绑定关系吗？";
	var opr = (op=="0")?getJsLocaleMessage("xtgl","xtgl_qygl_qdysgbdgxsxm"):getJsLocaleMessage("xtgl","xtgl_qygl_qdyjhgbdgxm");
	if(confirm(opr))
	{
		$.post(pathUrl+"/cor_corpSp.htm",{method:"delete",busCode : busCode,SpUser:SpUser,op:op},function(result){
			if(result=="true")
			{
				//alert("操作成功！");
				alert(getJsLocaleMessage("xtgl","xtgl_qygl_czcg"));
				submitForm();
			}else 
			{
				//alert("操作失败！");
				alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
				$("a[name='querySP']").attr("disabled","");
			}
		});
	}
	else
	{
		
		$("a[name='querySP']").attr("disabled","");
	}
}
function changeIdt(t)
{
    var $show;
    $show = $("#aid"+t).next('.c_selectBox');
    if($show.length > 0)
    $show.show().siblings().hide();
}
function changeType(t)
{
    if("2" == t)
    {
         $("#t1").hide();
         $("#t2").show();
    }
    else if("1" == t)
    {
    	$("#t2").hide();
        $("#t1").show();
    }
    else if("3" == t)
    {
    	$("#t4").hide();
        $("#t3").show();
    }
    else if("4" == t)
    {
    	$("#t3").hide();
        $("#t4").show();
    }
}

function doCancel()
{
   location.href = "cor_corpSp.htm?method=find";
}
function addBusType()
{
	var smsaccount = $("#smsaccount").val();
	var busName = $('#cbusname').val();
	var bustype = $('#ctypename').val();
	var pathUrl = $("#pathUrl").val();
	var id1 = "";
	var id2 = "";
		$('input[name="gpus"]:checked').each(function(){
		   id1 += ","+$(this).val();
    });
		$('input[name="gpus2"]:checked').each(function(){
		   id2 += ","+$(this).val();
    });
    if (id1 == "" && bustype == "1")
    {
         //alert("请至少选择一个"+smsaccount+"！");
         alert(getJsLocaleMessage("xtgl","xtgl_qygl_qzsxzyg")+smsaccount+getJsLocaleMessage("xtgl","xtgl_qygl_gth"));
         return;
	}else if (id2 == "" && bustype == "2")
    {
        //alert("请至少选择一个"+smsaccount+"！");
        alert(getJsLocaleMessage("xtgl","xtgl_qygl_qzsxzyg")+smsaccount+getJsLocaleMessage("xtgl","xtgl_qygl_gth"));
        return;
	}
    id1 = id1.substring(1);
    id2 = id2.substring(1);
    var ids = bustype == "1"?id1:id2;
    $("#btnOK").attr("disabled","true");
    $.post(pathUrl+"/cor_corpSp.htm",
	{
		method:"add",
		busCode : busName,
		bustype : bustype,
		ids : ids
	},function(result){
		if(result != null && result == "true")
		{
			//alert("新建成功！");
			alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjcg"));
		    $("#btnOK").attr("disabled","");
			location.href = pathUrl+"/cor_corpSp.htm?method=find";
		}else if(result != null && result == "error"){
			 $("#btnOK").attr("disabled","");
			 //alert("新建失败！");
			 alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjsb"));
		}else if(result != null&&result != "true"&&result != "error"){
			 $("#btnOK").attr("disabled","");
			 //alert("新建失败！短信SP账号("+result+")未配置或者未正确配置状态报告URL！");
			 alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjsbdxspzh")+result+getJsLocaleMessage("xtgl","xtgl_qygl_wpzhzwzqpzztbgurl"));
		}else
		{
		    $("#btnOK").attr("disabled","");
			//alert("新建失败！");
			alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjsb"));
		}
	});
}
function goback()
{
 		window.location.href = "cor_corpSp.htm";
}