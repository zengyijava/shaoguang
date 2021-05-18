//时间控件 结束时间
function rtime()
{
    var max = "2099-12-31 23:59:59";
    var min = "1900-01-01 00:00:00";
    var v = $("#starttime").val();
	if(v.length != 0)
	{
		min = v;
    }
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
}

//时间控件 开始时间
function stime(){
    var max = "2099-12-31 23:59:59";
    var min = "1900-01-01 00:00:00";
    var v = $("#endtime").val();
	if(v.length != 0)
	{
	    max = v;
	}

    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
}


function del(sid,draftstype,e){
    var usedDraftId = window.parent.$('#draftId').val();
    var fromShortUrl = $('#fromShortUrl').val();
    if(usedDraftId && sid == usedDraftId){
        alert(getJsLocaleMessage("common","common_js_draft_1"));
        return;
    }
    if(!confirm(getJsLocaleMessage("common","common_js_draft_2"))){
        return;
    }
    if(sid==""){
        alert(getJsLocaleMessage("common","common_js_draft_3"));
        return;
    }
    var lgcorpcode = $('#lgcorpcode').val();
    var domainId = $(e).parent('td').parent('tr').find('input[name="domainId"]').val()//$('#domainId').val();
    var netUrlId = $(e).parent('td').parent('tr').find('input[name="netUrlId"]').val()//$('#netUrlId').val();
    $.post("common.htm?method=deleteDrafts&sid="+sid+"&domainId="+domainId+"&netUrlId="+netUrlId,
				{},
				function(result)
				{
					if(result=="true")
				    {
					     alert(getJsLocaleMessage("common","common_js_draft_4"));
					     window.location.href = "common.htm?method=getDrafts&draftstype="+draftstype+"&shorturl=" + fromShortUrl + "&timee="+new Date().getTime();
				    }else if(result=="false")
				    {
				    	 alert(getJsLocaleMessage("common","common_js_draft_5"));
				    }
				}
			);
				
    //$.ajax({
    //    type:"POST",
    //    url: "common.htm",
    //    data: {method: "delete",sid:sid,lgcorpcode:lgcorpcode,time:new Date(),isAsync:"yes"},
    //    success: function(result){
    //        if(result == 'true'){
    //            alert("删除成功！");
    //            submitForm();
    //        }else{
    //            alert("删除失败！");
    //        }
    //    },
    //    error: function(){alert('请求异常！');}
    //});
}

$(function(){
    $('#des-info').dialog({
        autoOpen: false,
        resizable: false,
        width:300,
        height:210
    });
    $('.ellipsis').click(function(){
        var text = $(this).attr('msg') || $(this).text();
        $('#des-info').html('<xmp>'+text+'</xmp>');
        var index = $(this).parents('tr').find('.ellipsis').index($(this));
        $('#des-info').dialog({title:index==0?getJsLocaleMessage("common","common_js_draft_6"):getJsLocaleMessage("common","common_js_draft_7")});
        $('#des-info').dialog('open');
    });
    $("#content tbody tr").hover(function() {
        $(this).addClass("hoverColor");
    }, function() {
        $(this).removeClass("hoverColor");
    });
})