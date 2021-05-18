//双字节字符[^x00-xff] 特殊符号（sql或html）
var charReg = /[<>'"&\[\]%_ ]/g;
var errTypeIndex = 1;
var errCodeIndex = 2;
$(document).ready(function(){
	$(this).bind('click',function(){
		$("td .save").each(function(){
			var $tr = $(this).parents("tr");
			if(!$tr.hasClass('hoverColor')){
				var td0 = $tr.children("td:eq("+errTypeIndex+")");
				var td1 = $tr.children("td:eq("+errCodeIndex+")");
				td0.empty().append(td0.attr('old'));
				td1.empty().append(td1.attr('old'));
				//$tr.find('.save').removeClass('save').addClass('edit').text('修改');
				$tr.find('.save').removeClass('save').addClass('edit').text(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_64"));
			}
		})
	})
	$("#errCode,#errCodeStr").live('keyup blur',function(){
		var value=$(this).val();
		if(charReg.test(value)){
			value=value.replace(charReg,'');
			$(this).val(value);
		}
		while(len(value)>7&&value.length>1){
			value=value.substring(0,value.length-1);
			$(this).val(value);
		}
	});
	//修改
	$(".edit").live('click',function(){
		//$(this).removeClass('edit').addClass('save').text('保存');
		$(this).removeClass('edit').addClass('save').text(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_65"));
		var td0 = $(this).parents('tr').children("td:eq("+errTypeIndex+")");
		var td1 = $(this).parents('tr').children("td:eq("+errCodeIndex+")");
		var errCode = td1.text();
		var errType = td0.text();
		td0.attr('old',errType);
		td1.attr('old',errCode);
		td1.empty().append(getErrText(errCode));
		td0.empty().append(getSelect(errType));
		td0.children().isSearchSelect({'width':'158','isInput':false,'zindex':0});
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
		var td0 = $click.parents('td').siblings().eq(errTypeIndex);
		var oldType = td0.attr('old');
		var newType = td0.find(":selected").text();
		var type = td0.find("select").val();
		var td1 = $click.parents('td').siblings().eq(errCodeIndex);
		var oldErrCode = td1.attr('old');
		var newErrCode = td1.children(":text").val();
		if(oldType!=newType||oldErrCode!=newErrCode){
			if(!newErrCode||newErrCode.length==0){
				//alert('错误代码不能为空！');
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_66"));
				$click.addClass('save');
				return;
			}
			var id = $click.parents('td').siblings().first().children().val();
			$.ajax({
				type:"POST",
				url: "wy_mnpErrCodeManage.htm",
				data: {method:"update",type:type,errCode:newErrCode,oldErrCode:oldErrCode,id:id,time:new Date(),isAsync:"yes"},
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
					}else if(result=='repeat'){
						//alert('重复的错误代码！');
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_69"));
					}else if(result=='codeErr'){
						//alert('错误代码有误！');
						alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_70"));
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
			td0.empty().append(oldType);
			td1.empty().append(oldErrCode);
		}
	})
	
	//单条删除
	$(".delete").click(function(){
		//if(!confirm("您确定要删除该记录吗？")){
		if(!confirm(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_74"))){
			return;
		}
		var id = $(this).parents('td').siblings().first().children().val();
		$.ajax({
			type:"POST",
			url: "wy_mnpErrCodeManage.htm",
			data: {method: "delete",id:id,time:new Date(),isAsync:"yes"},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				if(result=='success'){
					//alert('删除成功！');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_75"));
					submitForm();
				}else{
					//alert('删除失败！');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_76"));
				}
			},
			//error: function(){alert('请求异常！');}
			error: function(){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_77"));}
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
			url: "wy_mnpErrCodeManage.htm",
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
	
	var matchLength = 0;
	//手工录入输入框
	$("#importArea").bind('keyup blur',function(){
		var reg = /[<>'"&\[\]%_]/g;
		var value=$(this).val();
		if(reg.test(value)){
			value=value.replace(reg,'');
			$(this).val(value);
		}
		var matchs = value.match(/[^，, 、\n]+/g);
		if(matchs!=null){
			matchLength = matchs.length;
		}else{
			matchLength = 0;
		}
		if(matchLength>1000){
			var reg = /^[，, 、\n]*([^，, 、\n]+[，, 、\n]+){999}[^，, 、\n]+/g;
			reg.exec(value);
			$(this).val(value.substring(0,reg.lastIndex));
			matchLength = 1000;
		}
		$("#count").html(matchLength);
	})
})

//手动添加
function add(){
	$('#toAdd').dialog({
		autoOpen: false,
		width: 550,
		height:410,
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
	var errCodeType = $('#toAdd').find("select").val();
	if($('#toAdd').find("select").val()==''){
		//alert('请选择类型！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_83"));
		return;
	}
	if($('#count').html()<=0){
		//alert('请输入错误代码！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_84"));
		return;
	}
	var errCodeStr = $('#importArea').val();
	$.ajax({
		type:"POST",
		url: "wy_mnpErrCodeManage.htm",
		data: {method: "add",errCodeType:errCodeType,errCodeStr:errCodeStr,time:new Date(),isAsync:"yes"},
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
			}else if(result=='noErrCode'){
				//alert('没有有效的错误代码！');
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_86"));
			}else{
				//alert('添加失败！');
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_87"));
			}
		},
		//error: function(){alert('请求异常！');}
		error: function(){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_88"));}
	})
}

function getErrText(errCode){
	var $text = $("#errCode").clone();
	$text.attr('id','errCodeStr').attr('name','errCodeStr');
	$text.val(errCode);
	return $text;
}
function getSelect(type){
	var $type = $("#errCodeType").clone();
	$type.removeAttr('id').removeAttr('name');
	$type.find("option[value='0']").remove();
	$type.children().each(function(){
		if($(this).text()==type){
			$(this).attr('selected',true);
			return false;
		}
	})
	return $type;
}

function len(s){//获取字符串的字节长度
    s=String(s);
    return s.length+(s.match(/[^\x00-\xff]/g) ||"").length;//加上匹配到的全角字符长度
}

function isOutOfLogin(result){
	if(result == "outOfLogin")
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