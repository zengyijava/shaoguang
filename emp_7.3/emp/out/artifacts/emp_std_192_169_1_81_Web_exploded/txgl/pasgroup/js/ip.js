	$(document).ready(function () {
		$('#add-ip').click(function () {
			var $ul = $(this).prev('ul'); 
			var $lis = $ul.children('li');
			if($lis.size()>=10){
				//alert('最多支持绑定10个IP地址！');
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_27"));
				return false;
			}else{
				var $clone = $lis.eq(0).clone();
				$clone.children('input').val('');
				$clone.css('borderColor','');
				$ul.append($clone);
			}
		});
		$('#remove-ip').click(function () {
			var $ul = $(this).siblings('ul'); 
			var $lis = $ul.children('li');
			if($lis.size() == 1){
				$lis.eq(0).children('input').val('');
			}else{
				$lis.last().remove();
			}
			
		});
		$('ul').on('keyup blur','input',mask);
	}) 
	
function removeIps(){
		var $ul = $('#remove-ip').siblings('ul'); 
		$ul.find('li:gt(0)').remove();
		$ul.find('input:text').val('');
}
	
function mask() {
  	var obj = this;
  	$(this).parent().css('borderColor','');
  	var e = window.event || arguments.callee.caller.arguments[0];
  	if(/[^\d]/.test(obj.value)){
  		obj.value = obj.value.replace(/[^\d]/g, '');
  	}
	var key1;
	var isie = (document.all) ? true : false;
	if (isie) {// IE
		key1 = window.event.keyCode;
	} else { // 
		key1 = e.which;
	}
	if(e.type == 'keyup'){
		var nextEle;
		var length = obj.value.length;
		if (key1 == 39 || (length>0&&key1 == 190) ||(key1 != 37&&length==3)) {
			nextEle = $(obj).next();
		}else if(key1 == 37){
			nextEle = $(obj).prev();
		}
		if(nextEle&&nextEle.size()>0){
			obj.blur();
//			nextEle.focus();
			nextEle = nextEle[0];
			setCaretPosition(nextEle,nextEle.value.length);
		}
	}
};

function setCaretPosition(tObj, sPos){
    if(tObj.setSelectionRange){
        setTimeout(function(){
            tObj.setSelectionRange(sPos, sPos);
            tObj.focus();
        }, 0);
    }else if(tObj.createTextRange){
        var rng = tObj.createTextRange();
        rng.move('character', sPos);
        rng.select();
    }
}
