function mask(obj, e) {
	var reg=/\.+/g;
	var dd=/[\d]+/g;
	obj.value = obj.value.replace(/[^\d\.]/g, '');
	var key1;
	var isie = (document.all) ? true : false;
	if (isie) {// IE
		key1 = window.event.keyCode;
	} else { // 
		key1 = e.which;
	}
	if (key1 == 37 || key1 == 39) {
		obj.blur();
		nextip = parseInt(obj.name.substr(2, 1))
		nextip = key1 == 37 ? nextip - 1 : nextip + 1;
		if(1<=nextip && nextip<=8){
			document.getElementById("ip" + nextip).focus();
		}else if(nextip == 0){
			document.getElementById("ip1").focus();
		}else if(nextip == 9){
			document.getElementById("ip8").focus();
		}
	}

	if (obj.value.length >= 3 || (reg.test(obj.value) && dd.test(obj.value)))
		
		if (parseInt(obj.value) >= 256 || parseInt(obj.value) <= 0) {
			
			//alert(parseInt(obj.value) + "IP地址错误！");
			alert(parseInt(obj.value) + getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_63"));
			obj.value = "";
			obj.focus();
			return false;
		} else {
			obj.value = obj.value.replace(/[^\d]/g, '');
			obj.blur();
			nextip = parseInt(obj.name.substr(2, 1)) + 1;
			if(1<=nextip && nextip<=8){
			document.getElementById("ip" + nextip).focus();
			}else if(nextip == 0){
				document.getElementById("ip1").focus();
			}else if(nextip == 9){
				document.getElementById("ip8").focus();
			}
		}
};
function mask_c(obj) {
	clipboardData.setData('text', clipboardData.getData('text').replace(
			/[^\d]/g, ''))
};