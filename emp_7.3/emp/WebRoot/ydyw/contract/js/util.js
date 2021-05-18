//数组去重扩展
Array.prototype.unique = function()
{
	this.sort();
	var re=[this[0]];
	for(var i = 1; i < this.length; i++)
	{
		if( this[i] != re[re.length-1])
		{
			re.push(this[i]);
		}
	}
	return re;
}
//数组元素移出扩展
Array.prototype.remove = function(obj)
{
	var re = [];
	for(var i = 0; i < this.length; i++)
	{
		if( this[i] != obj)
		{
			re.push(this[i]);
		}
	}
	return re;
}
/**
 * 对输入框非法字符限制输入
 * ids 元素ids 
 * handle 文本值发生变化时的处理 
 */
function filterChar(ids,handle){
	var idsArr = ids.split(',');
	var isFun = (typeof handle == 'function');
	if(!isFun){
		var reg = handle;
		handle = function(){
			var evt = window.event || arguments.callee.caller.arguments[0];
			var src = evt.target || evt.srcElement;
    		var val = src.value;
    		if(reg.test(val)){
    			src.value = val.replace(reg,'');
    		}
		}
	}
	for(var i=0;i<idsArr.length;i++){
		var obj = document.getElementById(idsArr[i]);
		if(!obj){continue;}
	   if(obj.addEventListener){
	    	obj.addEventListener('input',handle,false);
	    }else if(obj.attachEvent){
	    	obj.attachEvent('onpropertychange',handle);
	    }
	}
 
}

function isOutOfLogin(result){
	if(result == "outOfLogin"||/\<html\>/i.test(result))
	{
		$("#logoutalert").val(1);
		location.href="common/logoutEmp.jsp";
		return true;
	}
	return false;
}