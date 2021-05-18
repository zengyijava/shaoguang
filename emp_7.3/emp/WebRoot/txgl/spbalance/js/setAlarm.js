
$(document).ready(function(){
	$("#threshold").live('keyup blur',function(){
		var value=$(this).val();
		if(value!=filterString(value)){
			$(this).val(filterString(value));
		}
	});
	
	$("input[name='noticename'],input[name='alarmphone']").live('keyup blur',function(){
		var value=$(this).val();
		if(value!=filterString1(value)){
			$(this).val(filterString1(value));
		}
	});
});

//字符串过滤
function filterString1(str){
	var reg=/[\|\&;,\$%@'"\<\>\(\)\+\”\.]/g;
	if(reg.test(str)){
		str=str.replace(reg,'');
	}
	return str;
}


//删除通知人排序
function sortHandle(){
	var oSim=$('.channel_manage .sim_detail');
	oSim.each(function(i){
		$(this).attr('data',(i+1));
		//$(this).find('label.mod_t1').html('通知人'+(i+1)+'：');
		$(this).find('label.mod_t1').html(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_56")+(i+1)+'：');
	})
}
//通知人去重 复号码
function testUnique(){
	var oSimNumber=$('.channel_manage input[name="alarmphone"]'),
		arr=[];
	oSimNumber.each(function(){
		var oSimNumberVal=$.trim($(this).val()),
			oData=$(this).parent().attr('data'),
			temp={};
		if(oSimNumberVal){
			temp['serial']=oData;
			temp['alarmphone']=oSimNumberVal;
			arr.push(temp);
		}
		
	})
	var arrSerial=uniqueSerial(arr);
	if(arrSerial.length){
		oSimNumber.each(function(){
			if($(this).val()==arrSerial[0]){
				$(this).addClass('no-unique');
			}
		})
		return true;
	}
}
//检测手机号是否合法
function testSixNum(){
	var oSimNumber=$('.channel_manage input[name="alarmphone"]');
	var flag=false;
	oSimNumber.each(function(){
		var oSimNumberVal=$(this).val();
		if(!asyncCheckPhone(oSimNumberVal)){
			flag=true;
			$(this).focus();
		}
	})
	return flag;
}

//异步校验手机号码合法性
function asyncCheckPhone(phone){
	var flag = false;
	$.ajax({
		type:"POST",
		async:false,
		url: "common.htm",
		data: {method:"filterPh",tmp:phone},
		success: function(result){
			if(result == 'true'){
				flag = true;
			}
		}
	});
	return flag;
}


//数组去重
function uniqueSerial(arr){
var hash={},aSerial=[];
	for(var i=0;i<arr.length;i++){
		for(var j in arr[i]){
			if(j=='alarmphone'){
				var key='No'+arr[i][j];
				if(hash[key]!==1){
					hash[key]=1;
				}else{
					aSerial.push(arr[i][j]);
				}
			}

		}
		
	}
	return aSerial;
}
//通知人详情获取
function getNoticeDetailVal(){
	var len=$('.channel_item').size();
	var arr=[];
	if(len){
		$('.channel_item').each(function(i){
			var obj={},_this=$(this),item=[];;
			var  serial=_this.attr('data'),noticename=_this.find('input[name="noticename"]').val(),
			alarmphone=_this.find('input[name="alarmphone"]').val();
			if(parseInt(serial)>0){
				if(alarmphone==''){
					item.push('');
				}else{
					item.push(noticename,alarmphone);
				}
				arr.push(item);
			}
		})
	}
	return arr.join('@');
}



function doCancelEdit(obj){
	parent.doCancel(obj);
}

function setAlarm(e)
{
	if(e&&e.stopPropagation){  
	    //因此它支持W3C的stopPropagation()方法  
	    e.stopPropagation();  
    }else{  
        //否则我们使用ie的方法来取消事件冒泡  
        window.event.cancelBubble = true;  
    }  
	//sp账号
	var	useridstr =$('#useridstr').val();
	//判断获取到的sp账号必须的参数是否丢失
	if(useridstr==''){
	   return;
	}
	//告警阀值
	var threshold = $('#threshold').val();
	//判断如果为空则填0
	if(threshold==''){
		threshold=='0';
	}
	//通知人详情
	var arrNoticeDetailVal=getNoticeDetailVal();
	if(arrNoticeDetailVal==""){
		//alert("通知人信息有未填项或未添加通知人信息！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_58"));
		return;
	}
	if(testUnique()){
		//alert('通知人手机号不能重复！');
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_59"));
		return false;
	}
	if(testSixNum()){
		//alert('通知人手机号码格式不正确！');
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_60"));
		return false;
	}
	 var $buttons = $(".channel_manage input:button");
	 $buttons.attr("disabled",true);
	$.post("spb_spBalanceMgr.htm",
	{
		method:"setAlarm",
		useridstr : useridstr,
		threshold : threshold,
		arrNoticeDetailVal: arrNoticeDetailVal,
		lgcorpcode:$('#lgcorpcode').val(),
		lguserid:$('#lguserid').val()
	},function(result){
	   if(result.indexOf("html") > 0){
			window.location.href=location;
		    return;
	   }else{
		   if(result>0){
			   //alert("设置阀值成功！");
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_61"));
			   window.parent.submitForm();
		   }else{
			  // alert("设置阀值异常！");
			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_62")); 
		   }
		   $buttons.attr("disabled",false);
	   }
	}
	     
	);
}



function checkAlls(e,str)    
	{  
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0; i<n; i=i+1)    
			a[i].checked =e.checked;    
	}



