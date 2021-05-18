document.onkeydown=keydown;
function chongZhi(){
	var addCount = $('#addCount').val();
			   if(addCount == '' ){
			     // alert("充值条数不能为空！");
				   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_2"));
			       $('#addCount').focus();
			      return;
			   }
			   if(addCount == 0 ){
			     // alert("充值条数不能为0！");
				   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_3"));
			       $('#addCount').select();
			      return;
			   }
			    var re = /^(\+?)(\d*)$/g;    
			     if (!re.test(addCount))
			    {
			        //alert("充值条数必须为数值!");
			    	 alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_4"));
			        $('#addCount').select();
			        return ;
			     }
			   	var addMark = $('#addMark').val();
			   	if(addMark.length > 256 ){
			     // alert("备注长度不能超过256个字");
			   		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_5"));
			      return;
			   }
			   
			   //if(!window.confirm('你确定要充值吗？')){
			   if(!window.confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_6"))){
				   return ;
			   }
			   	
			   var useridstr = $('#useridstr').val();
			   var addMark = $('#addMark').val();
			   var path = 'spb_spBalanceMgr.htm';
			   var $buttons = $("#addBalance input:button");
			   $buttons.attr("disabled",true);
			   $.post(path,{method:"addBalance",count:addCount,useridstr:useridstr,addMark:addMark,lgguid:$("#lgguid").val()},function(result){
				   if(result.indexOf("html") > 0){
		    			window.location.href=location;
		    		    return;
		    	   }else{
				       if(result == "0"){
				           //alert("SP账号充值成功！ ");
				    	   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_7"));
				           $('#addBalance').dialog('close');  
				           submitForm();
				           // end
				       }else{
				    	   var str = getBalanceMsg(result,1);
				    	   $('#addCount').select();
				    	   //alert("充值失败 ："+str);
				    	   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_8")+str); 
				       }
				       $buttons.attr("disabled",false);
				    }
			   });
}

function quXiao(flag){
	if(flag==1){
		$('#addCount').val("");
	   $('#addMark').val("");
	   $('#addBalance').dialog('close'); 
	 }else{
	      $('#delCount').val("");
			   $('#recMark').val("");
			   $('#delBalance').dialog('close');
	 }
}




function huiShou(){
	var recCount = $('#recCount').val();
			   if(recCount == '' ){
			      //alert("回收条数不能为空！");
			      alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_9"));
			      $('#recCount').focus();
			      return;
			   }
			   if(recCount == 0 ){
			     // alert("回收条数不能为0！");
				   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_10"));
			      $('#recCount').select();
			      return;
			   }
			   	var re = /^(\+?)(\d*)$/g;    
			     if (!re.test(recCount))
			    {
			       // alert("回收条数必须为数值!");
			    	 alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_11"));
			        $('#recCount').select();
			        return ;
			     }
			    var recMark = $('#recMark').val();
			   	if(recMark.length > 256 ){
			      //alert("备注长度不能超过256个字");
			   		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_12"));
			      return;
			   }
			   	
			   	//if(!window.confirm('你确定要回收吗？')){
			   	if(!window.confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_13"))){	
					   return ;
				 }
			   	
			   var useridstr = $('#useridstr').val();
			   var recMark = $('#recMark').val();
			   var path = 'spb_spBalanceMgr.htm';
			   var $buttons = $("#delBalance input:button");
			   $buttons.attr("disabled",true);
			   $.post(path,{method:"recBalance",count:recCount,useridstr:useridstr,recMark:recMark,lgguid:$("#lgguid").val()},function(result){
			       if(result.indexOf("html") > 0){
		    			window.location.href=location;
		    		    return;
		    	   }else{
				       if(result == "0"){
				           //alert("SP账号回收成功！ ");
				           alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_14"));
				           $('#delBalance').dialog('close');  
				           submitForm();
				       }else{
					       var str = getBalanceMsg(result,2);
					       $('#recCount').select();
					       //alert("回收失败 ："+str);
					       alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_15")+str);
				       }
				       $buttons.attr("disabled",false);
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
	var msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_16");
	if(baltype == 1){
		if(num == "-1"){
			//msg = msg + "充值失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_17");
		}else if(num == "-2"){
			//msg = msg + "充值数不能为空或者为0！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_18");
		}else if(num == "-9999"){
			//msg = msg + "充值接口调用异常！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_19");
		}else{
			//msg = msg + "充值失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_20");
		}
	}else if(baltype == 2){
		if(num == "-1"){
			//msg = msg + "回收失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_21");
		}else if(num == "-2"){
			//msg = msg + "回收数不能为空或者为0！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_22");
		}else if(num == "-5"){
			//msg = "回收余额数大于" + msg +"可分配数！";
			msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_23")+ msg +getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_24");
		}else if(num == "-9999"){
			//msg = msg + "回收接口调用异常！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_25");
		}else if(num == "-6"){
			//msg = " 获取SP账号余额记录失败！";
			msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_26");
		}else if(num == "-7"){
			//msg = "SP账号没有进行充值过！";
			msg = getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_27");
		}else{
			//msg = msg + "充值失败！";
			msg = msg + getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_28");
		}
	}
	return msg;
}


function addBalance(spuser,jmspuser,staffname){
   $('#useridstr').val(jmspuser);
   $('#addspuser').text(spuser+"("+staffname+")");
   //$('#addBalance').dialog("option","title","充值");
   $('#addBalance').dialog("option","title",getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_29"));
   $('#addBalance').dialog('option', 'height', '240');
   $('#addBalance').dialog('option', 'width', '350');
   $('#addBalance').dialog('open');
}
function delBalance(spuser,jmspuser,staffname){
   $('#useridstr').val(jmspuser);
   $('#delspuser').text(spuser+"("+staffname+")");
   //$('#delBalance').dialog("option","title","回收");
   $('#delBalance').dialog("option","title",getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_30"));
    $('#delBalance').dialog('option', 'height', '240');
   $('#delBalance').dialog('option', 'width', '350');
   $('#delBalance').dialog('open');
}



function noyinhao(obj)
{  
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$(obj).keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = (!(iKeyCode >=48 && iKeyCode<=57));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
}

