$(document).ready(function(){
	$("#addCount").bind('keyup blur',function(){
		var reg=/[^0-9]/g;
		var val=$(this).val();
		if(reg.test(val)){
			$(this).val($(this).val().replace(reg,''));
		}
 	});
});


function plchongZhi(e){
	if(e&&e.stopPropagation){  
	    //因此它支持W3C的stopPropagation()方法  
	    e.stopPropagation();  
    }else{  
        //否则我们使用ie的方法来取消事件冒泡  
        window.event.cancelBubble = true;  
    }  
	var $buttons = $("#addBalanceAll input:button");
	$buttons.attr("disabled",true);
	var useridstrs = $("#useridstrs").val();
	var lguserId = GlobalVars.lguserid;
	var bltype=$("#bltype").val();
	var addCount = $("#addCount").val();
	
	if(bltype == null || bltype =="")
	{
		//alert("请选择类型！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_31"));
		$buttons.attr("disabled",false);
		return false;
	}
	var btypestr="";
	if(bltype =="1"){
		//btypestr="充值";	
		btypestr=getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_32");
	}else if(bltype =="2"){
		//btypestr="回收";	
		btypestr=getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_33");	
	}
	if(addCount == null || addCount =="")
	{
		//alert("SP账号"+btypestr+"数目不能为空！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_34")+btypestr+getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_35"));
		 $('#addCount').select();
		$buttons.attr("disabled",false);
		return false;
	}
	if(addCount == 0)
	{
		//alert("请输入"+btypestr+"数目！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_36")+btypestr+getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_37"));
		 $('#addCount').select();
		$buttons.attr("disabled",false);
		return false;
	}
	
	 var re = /^(\+?)(\d*)$/g;    
     if (!re.test(addCount))
    {
       // alert(""+btypestr+"条数必须为数值!");
    	 alert(""+btypestr+getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_38"));
        $('#addCount').select();
        $buttons.attr("disabled",false);
        return ;
     }
	
	//if(!window.confirm('你确定要批量'+btypestr+'吗？')){
     if(!window.confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_39")+btypestr+getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_40"))){	
			$buttons.attr("disabled",false);
		   return ;
	 }
	
	var path = 'spb_spBalanceMgr.htm';
	$.post(path,
	   {method:"addBalanceAll",
		   lguserId:lguserId,
		   useridstrs:useridstrs,
		   bltype:bltype,
		   addCount:addCount,
		   lgcorpcode:GlobalVars.lgcorpcode,
		   lgguid:GlobalVars.lgguid
	   },function(result){
		   if(result.indexOf("html") > 0){
			   window.parent.submitForm();
   		    return;
		   }else{
		       if(result == "0"){
		           //alert("SP账号批量"+btypestr+"成功！ ");
		    	   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_41")+btypestr+getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_42"));
		           $buttons.attr("disabled",false);
		           window.parent.submitForm();
		       }else{
		    	   var str='';
		    	   if(bltype =="1"){
		    		   str = getBalanceMsg(result,1);
		    		}else if(bltype =="2"){
		    			 str = getBalanceMsg(result,2);
		    		}
		    	  // alert("SP账号批量"+btypestr+"失败 ："+str);
		    	   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_41")+btypestr+getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_44")+str);
		           $buttons.attr("disabled",false);
		           $('#addCount').select();
		       }
		       
		    }
	   });
}

//num返回值 ,baltype充值1 回首2
function getBalanceMsg(num,baltype){
	 //         0:回收成功
	 //         -1:回收失败
	 //         -2 SP账号充值/回收数不能为空或者为0
	 //         -5  回收余额数大于SP账号可分配数
	 //		   -6 获取SP账号余额记录失败
	 //		   -7 SP账号没有进行充值过
	 //         -9999:短信回收接口调用异常
	//var msg = "SP账号";
	var msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_34");
	if(baltype == 1){
		if(num == "-1"){
			//msg = msg + "充值失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_45");
		}else if(num == "-2"){
			//msg = msg + "充值数不能为空或者为0！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_46");
		}else if(num == "-9999"){
			//msg = msg + "充值接口调用异常！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_47");
		}else{
			//msg = msg + "充值失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_45");
		}
	}else if(baltype == 2){
		if(num == "-1"){
			//msg = msg + "回收失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_48");
		}else if(num == "-2"){
			//msg = msg + "回收数不能为空或者为0！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_49");
		}else if(num == "-5"){
			//msg = "回收余额数大于" + msg +"可分配数！";
			msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_50") + msg +getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_51");
		}else if(num == "-9999"){
			//msg = msg + "回收接口调用异常！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_52");
		}else if(num == "-6"){
			//msg = " 获取SP账号余额记录失败！";
			msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_53");
		}else if(num == "-7"){
			//msg = "SP账号没有进行充值过！";
			msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_54");
		}else{
			//msg = msg + "充值失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_45");
		}
	}
	return msg;
}


function doCancelEdit(obj){
	parent.doCancel(obj);
}
