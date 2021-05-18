/*     
	  将Date类型,解析为String类型.
	  不正确的Date,返回 ''
	  如果时间部分为0,则忽略,只返回日期部分.     
	*/       
	function formatDate(v){        
	  if(v instanceof Date){
	    var y = v.getFullYear();        
	    var m = leftZreo(v.getMonth() + 1);        
	    var d = leftZreo(v.getDate());        
	    var h = leftZreo(v.getHours());        
	    var i = leftZreo(v.getMinutes());        
	    var s = leftZreo(v.getSeconds());        
	   // var ms = v.getMilliseconds();           
	   // if(ms>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s + '.' + ms;   
	    if(h>0 || i>0 || s>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;        
	    return y + '-' + m + '-' + d;        
	  }        
	  return '';        
	}
	function leftZreo(o){
		if(o<10){
			return '0'+o;
		}
		return ''+o;
	}