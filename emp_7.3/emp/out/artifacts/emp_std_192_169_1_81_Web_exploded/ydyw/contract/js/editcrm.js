$(document).ready(function(){
	var guid = '';
	getLoginInfo('#loginUser');
	var contractId = $('#contractId').val();
	if(contractId){
		$('#cliname,#cliCode,#cli-dep').attr('disabled',true);
		$('#cli-dep').removeClass('treeInput');
		guid = $('#cliGuid').val();//原始guid
		//查找签约客户机构
		$.ajax({
			type:"POST",
			url: "ydyw_crmSignQuery.htm?method=findClientDepsByGuid",
			data: {guid:guid,lgcorpcode:$('#lgcorpcode').val(),isAsync:"yes"},
			beforeSend: function(){},
			complete: function(){},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				if(result){
					result = eval('('+result+')');
					var depnames = [];
					var depids = [];
					for(var i=0;i<result.length;i++){
						depids.push(result[i].dep_id);
						depnames.push(result[i].dep_name);
					}
					$('#cli-dep').val(depnames.join(","));
					$('#depId').val(depids.join(','));
				}else{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_28"));
				}
			},
			error:function(){
				alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_28"));
			}
			});
		
		var $button = $('.btn-submit').children('input');
		var $r_sel = $('#select-tc');
        var empLangName = $("#empLangName").val();
		$.ajax({
			type:"POST",
			url: "ydyw_crmSignQuery.htm?method=findSelTaocans",
			data: {contractId:contractId,lgcorpcode:$('#lgcorpcode').val(),isAsync:"yes",empLangName:empLangName},
			beforeSend: function(){$button.attr("disabled",true);},
			complete: function(){$button.attr("disabled",false);sel();},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				$r_sel.empty();
				result = eval('('+result+')');
				var len = result.length;
				for(var i=0;i<len;i++){
					var item = result[i];
					var state = '';
					if(item.state != 0){state = 'state';}
					var $option = $('<option class="'+state+'" id="'+item.id+'" type="'+item.type+'" money="'+item.money+'">'+item.name+'&nbsp;&nbsp;('+item.code+'&nbsp;&nbsp;'+item.zf+')</option>');
					$option.attr('_name',item.name);
					$option.attr('code',item.code);
					$option.attr('title',$option.text());
					$r_sel.append($option);
				}
			},
			error:function(){
			}
			});
	}else{
		sel();
	}
});

function sel(flag){
    var empLangName = $("#empLangName").val();
	$('#taocan').sel({width:"zh_HK" == empLangName?620:625,height:250},function(size){
		$('.tip').hide();
		$('#total').html(size);
		if(flag)ischange = true;
	});
	flag = 1;
	$('#search').trigger('click');
}