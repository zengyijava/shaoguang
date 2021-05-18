
$(document).ready(function(){
	var charReg=/[\|\&;\$%@'"\<\>\(\)\+\”]/g;
	$("#aipadress").live('keyup blur',function(){
		var value=$(this).val();
		if(charReg.test(value)){
			value=value.replace(charReg,'');
			$(this).val(value);
		}
		
	});
	
	
	$("#gatename,#portnum,#agatename,#aportnum,#acommon,input[name='country'],#corpsign").live('keyup blur',function(){
		var value=$(this).val();
		if(value!=filterString(value)){
			$(this).val(filterString(value));
		}
	});
	
	$("#ipadress").live('keyup blur',function(){
		var reg=/[\|\&;\$%@'"\<\>\(\)\+\”]/g;
		var value=$(this).val();
		if(reg.test(value)){
			value=value.replace(charReg,'');
			$(this).val(value);
		}
	});
	
	
});


	//验证业务指令代码	
	function isNumberOrLetter(s) {//判断是否是数字或字母 
		var regu = "^[a-zA-Z]+[a-zA-Z0-9]+[#]{1,6}$";
	    var re = new RegExp(regu);
		var index = s.indexOf("#");
		var split = "";
		var code = "";
		if(index!= -1){
			split = s.substring(s.length-1,s.length);
			code = s.substring(0,s.length-1);
			if(split == "#"){
				if(code != ""){
					if (re.test(s)) {
				        return true;
				    } else {
				        return false;
				    }
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	    
	}
//删除sim卡排序
function sortHandle(){
	var oSim=$('.channel_manage .sim_detail');
	oSim.each(function(i){
		$(this).attr('data',(i+1));
		$(this).find('label.mod_t1').html('SIM卡'+(i+1)+'：');
	})
}
//sim卡卡号去重
function testUnique(){
	var oSimNumber=$('.channel_manage input[name="sim_number"]'),
		arr=[];
	oSimNumber.each(function(){
		var oSimNumberVal=$.trim($(this).val()),
			oData=$(this).parent().attr('data'),
			temp={};
		if(oSimNumberVal){
			temp['serial']=oData;
			temp['sim_number']=oSimNumberVal;
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
//检测sim卡号是否小于7位数
function testSixNum(){
	var oSimNumber=$('.channel_manage input[name="sim_number"]');
	var flag=false;
	oSimNumber.each(function(){
		var oSimNumberVal=$(this).val();
		if(oSimNumberVal.length<7){
			flag=true;
		}
	})
	return flag;
}
//数组去重
function uniqueSerial(arr){
var hash={},aSerial=[];
	for(var i=0;i<arr.length;i++){
		for(var j in arr[i]){
			if(j=='sim_number'){
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
//sim卡详情获取
function getSimDetailVal(){
	var len=$('.channel_item').size();
	var arr=[];
	if(len){
		$('.channel_item').each(function(i){
			var obj={},_this=$(this),item=[];;
			var serial=_this.attr('data'),
				sim_number=_this.find('input[name="sim_number"]').val(),
				country=_this.find('input[name="country"]').val(),
				sim_server=_this.find('select[name="sim_server"]').val();
			if(parseInt(serial)>0){
				if(sim_number==''){
					item.push('');
				}else{
					item.push(serial,sim_number,country,sim_server);
				}
				arr.push(item);
			}
		})
	}
	return arr.join('@');
}

//验证端口格式是否正解
function isPort(str) {
 return (isNumber(str) && str < 65536);
}


function isNumber(s) {
    var regu = "^[0-9]+$";
    var re = new RegExp(regu);
    if (s.search(re) != -1) {
        return true;
    } else {
        return false;
    }
}



function checkIP2(sIPAddress)
{
     var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
     var reg = sIPAddress.match(exp);
     if(reg==null)
     {
         return true;
     }else{
    	 return false;
     }

}


function addGateManage(e)
{
	
	if(e&&e.stopPropagation){  
            //因此它支持W3C的stopPropagation()方法  
            e.stopPropagation();  
        }else{  
            //否则我们使用ie的方法来取消事件冒泡  
            window.event.cancelBubble = true;  
        }  
	//通道名称
	var agatename = $('#agatename').val();
	//EMP网关IP
	var ainneripadress  = $.trim($("#ip1").val())+'.'+$.trim($("#ip2").val())+'.'+
	$.trim($("#ip3").val())+'.'+$.trim($("#ip4").val());
	//EMP网关端口
	var ainnerportnum = $('#ainnerportnum').val();
	//运营商网关ip
	var aipadress= $.trim($("#ip5").val())+'.'+$.trim($("#ip6").val())+'.'+
	$.trim($("#ip7").val())+'.'+$.trim($("#ip8").val());
	//运营商网关端口
	var aportnum = $('#aportnum').val();

	//企业签名
	var corpsign = $('#corpsign').val();
	//sim卡详情
	var arrSimDetailVal=getSimDetailVal();
	//if(agatename == ""){alert("通道名称不能为空！");$('#agatename').select();return;}
	if(agatename == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_23"));$('#agatename').select();return;}
	//if(ainneripadress == ""){alert("EMP网关IP不能为空！");return;}
	if(ainneripadress == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_24"));return;}
	//if(ainnerportnum == ""){alert("EMP网关端口不能为空！");$('#ainnerportnum').select();return;}
	if(ainnerportnum == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_25"));$('#ainnerportnum').select();return;}
	//if(aipadress == ""){alert("运营商网关IP不能为空！");return;}
	if(aipadress == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_26"));return;}
	//if(aportnum == ""){alert("运营商网关端口不能为空！");$('#aportnum').select();return;}
	if(aportnum == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_27"));$('#aportnum').select();return;}
	
	//验证端口号是否合法
	if(checkIP2(ainneripadress)){
		 //alert("EMP网关IP格式不正确！");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_28"));
		 return;	
	}
	
	if(!isPort(ainnerportnum)){
		//alert("EMP网关端口格式不正确！");$('#ainnerportnum').select();return;
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_29"));$('#ainnerportnum').select();return;
	}

	
	//验证端口号是否合法
	if(checkIP2(aipadress)){
		 //alert("运营商网关IP格式不正确！");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_30"));
		 return;	
	}
	
	if(!isPort(aportnum)){
		//alert("运营商网关端口格式不正确！");$('#aportnum').select();return;
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_31"));$('#aportnum').select();return;
	}

	if(corpsign!=""){
		var pat=/^\[.*\]$/;
		if(!pat.test(corpsign)){
			//alert("签名输入格式不对！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_32"));
			return;
		}
	}
	
	if(arrSimDetailVal==""){
		//alert("SIM卡卡号有未填项或未添加SIM卡信息！");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_33"));
		return;
	}
	
	if(testUnique()){
		//alert('SIM卡卡号不能重复！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_34"));
		return false;
	}
	if(testSixNum()){
		//alert('SIM卡卡号不能为空且不能小于7位数！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_35"));
		return false;
	}
	
	if($('#acommon').val().length>100)
	{
	   // alert("备注不能超过100个字！");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_36"));
	    return;
	}
	
	$.post("wy_gateManage.htm",
	{
		method:"add",
		agatename : agatename,
		aipadress : aipadress,
		aportnum : aportnum,
		ainneripadress : ainneripadress,
		ainnerportnum : ainnerportnum,
		arrSimDetailVal: arrSimDetailVal,
		corpsign : corpsign,
		acommon:$('#acommon').val()
	},function(result){
		if(result == "ipcomExists")
		{
			//alert("网优通道名称IP端口有重复或者与通道名称重复！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_37"));
			$('#agatename').select();
			return;
		}else if(result == "simExists")
		{
			//alert("SIM卡号未填或者SIM卡号格式不正确！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_38"));
			return;
		}else if(result.split(":").length==2){
			//alert("SIM卡号："+result.split(":")[1]+"已绑定其他IPCOMM！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_39")+result.split(":")[1]+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_40"));
			return;
		}else if(result.split("|")[0]=="true")
		{
			//var isrefresh=true;
			$('#show').dialog({
				autoOpen: false,
				width:370,
			    height:300,
			    close:function(){
					//alert(isrefresh);
					///if(isrefresh){
						black();
					//}
				}
			    	
			});
			//alert(result.split("|")[1]);
			if(result.split("|").length>1){
				//alert(result.split("|")[1].split(",").length);
				if(result.split("|")[1].split(",").length>3){
					//alert(result.split("|")[1].split(",")[0]);
					$("#showgatename").html(result.split("|")[1].split(",")[0]);
					$("#showgatenum").html(result.split("|")[1].split(",")[1]);
					$("#showgateusername").html(result.split("|")[1].split(",")[2]);
					$("#showcornsign").html(result.split("|")[1].split(",")[3]);
				}
			}
			//isrefresh=false;
			$('#addDiv').dialog('close');
			//isrefresh=true;
			$('#show').dialog('open');
			
					//black();
		}else {
					//alert("新建失败！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_41"));
					return;
		}
	}
);
}
function goHome(element){
	$('#show').dialog('close');
	black();
}

function doCancelEdit(obj){
	parent.doCancel(obj);
}

function black()
{
	submitForm();
    	
}

function updateGateManage(e)
{
	if(e&&e.stopPropagation){  
            //因此它支持W3C的stopPropagation()方法  
            e.stopPropagation();  
        }else{  
            //否则我们使用ie的方法来取消事件冒泡  
            window.event.cancelBubble = true;  
        }  
	//网优通道账户iPcomid
	var	ipcommid =$('#ipcommid').val();
	//通道名称
	var agatename = $('#agatename').val();
	//ip地址
	var  ainneripadress = $.trim($("#ip1").val())+'.'+$.trim($("#ip2").val())+'.'+
	$.trim($("#ip3").val())+'.'+$.trim($("#ip4").val());
	//端口号
	var aportnum = $('#aportnum').val();
	//ip地址
	var aipadress  = $.trim($("#ip5").val())+'.'+$.trim($("#ip6").val())+'.'+
	$.trim($("#ip7").val())+'.'+$.trim($("#ip8").val());
	//端口号
	var ainnerportnum = $('#ainnerportnum').val();
	//短信签名
	var corpsign = $('#corpsign').val();
	//sim卡详情
	var arrSimDetailVal=getSimDetailVal();
	//if(agatename == ""){alert("通道名称不能为空！");$('#agatename').select();return;}
	if(agatename == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_42"));$('#agatename').select();return;}
	//if(ainneripadress == ""){alert("EMP网关IP不能为空！");return;}
	if(ainneripadress == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_43"));return;}
	//if(ainnerportnum == ""){alert("EMP网关端口不能为空！");$('#ainnerportnum').select();return;}
	if(ainnerportnum == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_44"));$('#ainnerportnum').select();return;}
	//if(aipadress == ""){alert("运营商网关IP不能为空！");return;}
	if(aipadress == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_45"));return;}
	//if(aportnum == ""){alert("运营商网关端口不能为空！");$('#aportnum').select();return;}
	if(aportnum == ""){alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_46"));$('#aportnum').select();return;}
	
	//if(corpsign == ""){alert("短信签名不能为空！");$('#corpsign').select();return;}
	
	//验证端口及ip号是否合法
	if(checkIP2(ainneripadress)){
		// alert("EMP网关IP格式不正确！");
		 alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_47"));
		 return;	
	}
	
	if(!isPort(ainnerportnum)){
		//alert("EMP网关端口格式不正确！");$('#ainnerportnum').select();return;
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_48"));$('#ainnerportnum').select();return;
	}
	
	if(checkIP2(aipadress)){
		 //alert("运营商网关IP格式不正确！");
		 alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_49"));
		 return;	
	}
	
	if(!isPort(aportnum)){
		//alert("运营商网关端口格式不正确！");$('#aportnum').select();return;
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_50"));$('#aportnum').select();return;
	}
	
	
	if(corpsign!=""){
		var pat=/^\[.*\]$/;
		if(!pat.test(corpsign)){
			//alert("签名输入格式不对！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_51"));
			return;
		}
	}
	
	if(arrSimDetailVal==""){
		//alert("SIM卡卡号有未填项或未添加SIM卡信息！");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_52"));
		return;
	}
	
	if(testUnique()){
		//alert('SIM卡卡号不能重复！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_53"));
		return false;
	}
	if(testSixNum()){
		//alert('SIM卡卡号不能为空且不能小于7位数！');
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_54"));
		return false;
	}
	
	if($('#acommon').val().length>100)
	{
	    //alert("备注不能超过100个字！");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_55"));
	    return;
	}
	
	$.post("wy_gateManage.htm",
	{
		method:"update",
		ipcomid : ipcommid,
		agatename : agatename,
		aipadress : aipadress,
		aportnum : aportnum,
		ainneripadress : ainneripadress,
		ainnerportnum : ainnerportnum,
		arrSimDetailVal: arrSimDetailVal,
		corpsign : corpsign,
		acommon:$('#acommon').val()
	},function(result){
		if(result == "ipcomExists")
		{
			//alert("网优通道名称IP端口有重复或者与通道名称重复！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_56"));
			$('#agatename').select();
			return;
		}else if(result == "simExists")
		{
			//alert("SIM卡号未填或者SIM卡号格式不正确！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_57"));
			return;
		}else if(result.split(":").length==2){
			//alert("SIM卡号："+result.split(":")[1]+"已绑定其他IPCOMM！");
			alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_58")+result.split(":")[1]+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_59"));
			return;
		}else if(result == "true")
				{
					//alert("修改成功！");
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_60"));
					var url = 'wy_gateManage.htm';
					//var conditionUrl = "";
					
					//var pageIndex=$('#txtPage').val();
					//var pageSize=$('#pageSize').val();
					//var busName = $('#busName').val();
					//conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&busName="+busName;
					window.parent.submitForm();
				}else {
					//alert("修改失败！");
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_61"));
					return;
				}
			}
		);
}







function closeAddServiceBinddiv()
{
	$("#addMoServiceBindDiv").dialog("close");
	$("#addMoServiceBindFrame").attr("src","");
	
}


function closeEditServiceBinddiv()
{
	$("#editMoServiceBindDiv").dialog("close");
	$("#editMoServiceFrame").attr("src","");
	
}

function checkAlls(e,str)    
	{  
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0; i<n; i=i+1)    
			a[i].checked =e.checked;    
	}



function validatePort(obj){
	if(obj.value>65535){
		//alert("端口范围0-65535");
		alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_62"));
		obj.value="";
		window.setTimeout("document.getElementById('"+obj.id+"').focus()",50);
	}
}




function switchState(obj) {
	if(obj.value == 1) {
		$("#bizCode").attr("disabled", false);
	} else {
		$("#bizCode").attr("disabled", true);
	}
}
function isNumber(s) {
	var regu = "^[0-9]+$";
	var re = new RegExp(regu);
	if (s.search(re) != -1) {
		return true;
	} else {
		return false;
	}
}

