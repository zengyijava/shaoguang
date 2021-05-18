//winPage中文件选择框
$('.chooseFile').delegate('.file-hidden','change',function(e){
	e.stopPropagation();
	$(this).siblings('.file-txt').val($(this).val());
})

