$(document).ready(function(){
	$(this).bind('click',function(){
		$("td .save").each(function(){
			var $tr = $(this).parents("tr");
			if(!$tr.hasClass('hoverColor')){
				var phoneTd = $tr.children("td:eq(1)");
				var phoneTypeTd = $tr.children("td:eq(2)");
				phoneTd.empty().append(phoneTd.attr('old'));
				phoneTypeTd.empty().append(phoneTypeTd.attr('old'));
				//$tr.find('.save').removeClass('save').addClass('edit').text('修改');
				$tr.find('.save').removeClass('save').addClass('edit').text(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_73"));
			}
		})
	})
	$(".int").live('keyup blur',function(){
		var value=$(this).val();
		if(/[^\d]/.test(value)){
			value=value.replace(/[^\d]/g,'');
			$(this).val(value);
		}
		if(/phone/.test($(this).attr('name'))&&value.length>11){
			$(this).val(value.substring(0,11));
		}
	});
	//单条删除
	$(".delete").click(function(){
		//if(!confirm("您确定要删除该记录吗？")){
		if(!confirm(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_74"))){
			return;
		}
		var id = $(this).parents('td').siblings().first().children().val();
		$.ajax({
			type:"POST",
			url: "wy_mnpManage.htm",
			data: {method: "delete",id:id,time:new Date(),isAsync:"yes"},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				if(result=='success'){
					//alert('删除成功！');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_80"));
					submitForm();
				}else{
					//alert('删除失败！');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_81"));
				}
			},
			//error: function(){alert('请求异常！');}
			error: function(){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_82"));}
		})
	});

	//批量删除
	$("#delete").click(function(){
		var ids = '';
		$('input[name="checklist"]:checked').each(function(){	
			ids += $(this).val()+",";
		});
		if(ids.length<=0){
			//alert('未选中任何记录！');
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_78"));
			return;
		}
		//if(!confirm("您确定要删除选中的记录吗？")){
		if(!confirm(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_79"))){
			return;
		}
		ids = ids.toString().substring(0, ids.lastIndexOf(','));
		$.ajax({
			type:"POST",
			url: "wy_mnpManage.htm",
			data: {method: "delete",id:ids,time:new Date(),isAsync:"yes"},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				if(result=='success'){
					//alert('删除成功！');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_80"));
					submitForm();
				}else{
					//alert('删除失败！');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_81"));
				}
			},
			//error: function(){alert('请求异常！');}
			error: function(){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_82"));}
		})
	});
	//修改
	$(".edit").live('click',function(){
		//$(this).removeClass('edit').addClass('save').text('保存');
		$(this).removeClass('edit').addClass('save').text(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_67"));
		var phoneTd = $(this).parents('td').siblings().eq(1);
		var phoneTypeTd = $(this).parents('td').siblings().eq(2);
		var phone = phoneTd.text();
		var phoneType = phoneTypeTd.text();
		phoneTd.attr('old',phone);
		phoneTypeTd.attr('old',phoneType);
		phoneTd.empty().append(getPhoneText(phone));
		phoneTypeTd.empty().append(getSelect(phoneType));
		phoneTypeTd.children().isSearchSelect({'width':'158','isInput':false,'zindex':0});
	});
	$("#content").delegate('.data_tr','dblclick',function(){
		var $edit = $(this).find('.edit');
		if($edit.length>0){
			$edit.click();
		}
	})
	//保存
	$(".save").live('click',function(){
		var $click = $(this);
		$click.removeClass('save');
		var phoneTd = $click.parents('td').siblings().eq(1);
		var oldPhone = phoneTd.attr('old');
		var phoneTypeTd = $click.parents('td').siblings().eq(2);
		var phone = phoneTd.children(":text").val();
		var phoneTypeStr = phoneTypeTd.find(":selected").text();
		var phoneType = phoneTypeTd.find("select").val();
		if(phoneTd.attr('old')!=phone||phoneTypeTd.attr('old')!=phoneTypeStr){
			if(!asyncCheckPhone(phone)){
				//alert('手机号码格式不正确！');
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_89"));
				$click.addClass('save');
				return;
			}
			var id = $click.parents('td').siblings().eq(0).children('input').val();
			$.ajax({
				type:"POST",
				url: "wy_mnpManage.htm",
				data: {method:"update",phoneType:phoneType,phone:phone,oldPhone:oldPhone,id:id,time:new Date(),isAsync:"yes"},
				//beforeSend: function(){$click.text('...');},
				complete: function(){
					if(!$click.hasClass('edit')){
						//$click.addClass('save').text('保存');
						$click.addClass('save').text(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_67"));
					}
				},
				success: function(result){
					if(isOutOfLogin(result)){return;}
					if(result=='success'){
						//alert('修改成功！');
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_68"));
						var pageIndex = $("#txtPage").val()||1;
						document.forms['pageForm'].elements["pageIndex"].value = pageIndex;
						document.forms['pageForm'].submit();
					}else if(result=='phoneRepeat'){
						//alert('重复的手机号码！');
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_90"));
					}else if(result=='phoneError'){
						//alert('手机号码格式不正确！');
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_89"));
					}else{
						//alert('修改失败！');
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_71"));
					}
				},
				//error: function(){alert('请求异常！');}
				error: function(){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_72"));}
			})
		}else{
			//$(this).addClass('edit').text('修改');
			$(this).addClass('edit').text(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_73"));
			phoneTd.empty().append(phone);
			phoneTypeTd.empty().append(phoneTypeStr);
		}
	})

	var matchLength = 0;
	//手工录入输入框
	$("#importArea").bind('keyup blur',function(){
		var value=$(this).val();
		if(/[^\d，, 、\n]/.test(value)){
			value=value.replace(/[^\d，, 、\n]/g,'');
			$(this).val(value);
		}
		var matchs = value.match(/\d+/g);
		if(matchs!=null){
			matchLength = matchs.length;
		}else{
			matchLength = 0;
		}
		if(matchLength>1000){
			var reg = /^\D*(\d+\D+){999}\d+/g;
			reg.exec(value);
			$(this).val(value.substring(0,reg.lastIndex));
			matchLength = 1000;
		}
		$("#count").html(matchLength);
	})
});

function getPhoneText(phone){
	var $text = $("#phone").clone();
	$text.removeAttr('id').attr('name','phoneStr');
	$text.val(phone);
	return $text;
}
function getSelect(type){
	var $type = $("#phoneType").clone();
	$type.removeAttr('id').removeAttr('name');
	$type.find("option[value='0']").remove();
	$type.find("option[value='-1']").remove();
	$type.children().each(function(){
		if($(this).text()==type){
			$(this).attr('selected',true);
			return false;
		}
	})
	return $type;
}

//手动添加
function add(){
	$('#toAdd').dialog({
		autoOpen: false,
		width: 500,
		height:390,
		resizable:false,
		modal:true,
		open: function(){
		},
		close:function(){
			var $select  = $('#toAdd').find("select");
			$('#toAdd').find("form")[0].reset();
			$select.children("option:eq(0)").attr('selected',true);
			$select.next().remove();
			$select.isSearchSelect({'width':'158','isInput':false,'zindex':0});
			$('#count').html('0');
		}
	});
	$('#toAdd').dialog('open');
}
function cancel_add(){
	$('#toAdd').dialog('close');
}

function sure_add(obj){
	var $obj = $(obj);
	var phoneType = $('#toAdd').find("select").val();
	if($('#toAdd').find("select").val()==''){
		//alert('请选择号码类型！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_91"));
		return;
	}
	if($('#count').html()<=0){
		//alert('请输入转网号码！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_92"));
		return;
	}
	var phoneStr = $('#importArea').val();
	$.ajax({
		type:"POST",
		url: "wy_mnpManage.htm",
		data: {method: "add",phoneType:phoneType,phoneStr:phoneStr,time:new Date(),isAsync:"yes"},
		beforeSend: function(){$obj.attr("disabled",true);},
		complete: function(){$obj.attr("disabled",false);},
		success: function(result){
			if(isOutOfLogin(result)){return;}
			var reg =/success\[(\d+)\]/;
			var group = result.match(reg);
			if(group!=null&&group.length>0){ 
				//alert('['+group[1]+'条记录]添加成功！');
				alert('['+group[1]+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_85"));
				$('#toAdd').dialog('close');
				submitForm();
			}else if(result=='nophone'){
				//alert('没有有效的手机号码！');
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_93"));
			}else{
				//alert('添加失败！');
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_87"));
			}
		},
		//error: function(){alert('请求异常！');}
		error: function(){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_88"));}
	})
}

//文件导入
function importData(){
	$('#toImport').dialog({
		autoOpen: false,
		width: 550,
		height:310,
		resizable:false,
		modal:true,
		open: function(){
		},
		close:function(){
			$('#toImport').find("form")[0].reset();
		}
	});
	$('#toImport').dialog('open');
};

function cancel_import(){
	$('#toImport').dialog('close');
}

function sure_import(obj){
	var filepath = $('#toImport').find("input[name='file']").val().toLowerCase();
	if(filepath==''){
		//alert('未选择任何文件！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_94"));
		return;
	}
	//文件格式不支持
	if(!/\.(txt|xls|xlsx|zip|et)$/.test(filepath)){
		//alert('不支持的文件格式！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_95"));
		return;
	}
	var $form=$(obj).parents("form");
	$form.ajaxSubmit({
    dataType:'html',
    type:'POST',
    success:function(result){
    	if(isOutOfLogin(result)){return;}
		var reg =/success\[(\d+)\]/;
		var group = result.match(reg);
		if(group!=null&&group.length>0){ 
			//alert('['+group[1]+'条记录]导入成功！');
			alert('['+group[1]+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_96"));
			$('#toImport').dialog('close');
			submitForm();
		}else if(result=='nophone'){
			//alert('没有有效的手机号码！');
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_93"));
		}else if(result=='outMaxSize'){
			//alert('上传文件大小不能超过5M！');
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_97"));
		}else if(result=='overMax'){
			//alert('上传号码总数超过最大上限！');
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_98"));
		}else{
			//alert('导入失败！');
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_99"));
		}
	},
	//error:function(){alert('请求异常');	 },
	error:function(){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_72"));	 },
	complete:function(){
    	$(this).attr('disabled', false);
	},
	beforeSubmit:function(){
		$(this).attr('disabled', true);
	}
	});
}

function isOutOfLogin(result){
	if(result == "outOfLogin"||/\<html\>/i.test(result))
	{
		$("#logoutalert").val(1);
		location.href=path+"/common/logoutEmp.html";
		return true;
	}
	return false;
}

function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}

