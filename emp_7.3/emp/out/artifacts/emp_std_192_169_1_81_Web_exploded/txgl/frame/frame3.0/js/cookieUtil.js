var cookieUtil={};
cookieUtil.setCookie=function(key,value,iDay){
	var oDate=new Date();
	oDate.setDate(oDate.getDate()+iDay);
	document.cookie=key+"="+value+";expires="+oDate;
}
cookieUtil.getCookie=function(name){
	var cookieArray=document.cookie.split("; "); //得到分割的cookie名值对   
   var cookie=new Object();   
   for (var i=0;i<cookieArray.length;i++){   
      var arr=cookieArray[i].split("=");       //将名和值分开   
      if(arr[0]==name)return unescape(arr[1]); //如果是指定的cookie，则返回它的值   
   }
   return "0";

}
