

function butg(id1,id2,id3)
{
	if(id1 != null && id1 != "")
	{
		$(id1).attr("disabled",true);
	}
	if(id2 != null && id2 != "")
	{
		$(id2).attr("disabled",true);
	}
	if(id3 != null && id3 != "")
	{
		$(id3).attr("disabled",true);
	}
}

function butk(id1,id2,id3)
{
	if(id1 != null && id1 != "")
	{
		$(id1).attr("disabled",false);
	}
	if(id2 != null && id2 != "")
	{
		$(id2).attr("disabled",false);
	}
	if(id3 != null && id3 != "")
	{
		$(id3).attr("disabled",false);
	}
}


function PhoneCheck(s)//检测是否为固定电话号码或手机号
{
var str=s;
var reg=/^(13[0-9]{9}$)|(15[0-35-9][0-9]{8}$)|(((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$)|(18[05-9][0-9]{8}$)/;
return reg.test(str);
} 

function checkform(){ 
	butg("#btnSsu","");
//	var strRegex = "^((https|http|ftp|rtsp|mms)?://)" 
//	+ "?(([0-9a-z_!~*'().&=+$%-]+:)?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@ 
//	+ "(([0-9]{1,3}.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184 
//	+ "|" // 允许IP和DOMAIN（域名）
//	+ "([0-9a-z_!~*'()-]+.)*" // 域名- www. 
//	+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]." // 二级域名 
//	+ "[a-z]{2,6})" // first level domain- .com or .museum 
//	+ "(:[0-9]{1,4})?" // 端口- :80 
//	+ "((/?)|" // a slash isn't required if there is no file name 
//	+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
	//var strRegex = "^(http://)?(*)";
	var strRegex = "^http://(.)*";
	var v =null;
	var rptUrl =null;
	var morptUrl = null;
	if(document.getElementById("accouttype").value==1){
		v=$.trim(document.getElementById("sx").value);
		rptUrl=$.trim(document.getElementById("rptUrl").value);
		morptUrl=$.trim(document.getElementById("spbindurl").value);
	}
	var re=new RegExp(strRegex); 
			if(document.forms["form1"].userid.value.length==0 || document.forms["form1"].userid.value.length!=6){
                /*SP账号不能为空且长度只能为6位字母或数字！*/
                window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_1"));
				document.forms["form1"].userid.focus();
				butk("#btnSsu","#btnSca");
				return false;
			}else if(document.forms["form1"].userpassword.value==null){
                /*账户密码不能为空！*/
                window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_2"));
				document.forms["form1"].userpassword.focus();
				butk("#btnSsu","#btnSca");
				return false;
			}
			else
			if(document.forms["form1"].userpassword.value.length<6 || document.forms["form1"].userpassword.value.length>18)
					{
				/*密码长度必须在6-18位数之间！*/
				window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_3"));
				document.forms["form1"].userpassword.focus();
				butk("#btnSsu","#btnSca");
				return false;
			}else
			if(document.forms["form1"].staffname.value==null || document.forms["form1"].staffname.value.length<1){
                /*账户密码不能为空！*/
                window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_2"));
				document.forms["form1"].staffname.focus();
				butk("#btnSsu","#btnSca");
				return false;
			/*}else if(document.forms["form1"].lxrph.value!=""&&!PhoneCheck(document.forms["form1"].lxrph.value)){				
				alert("请输入有效的联系号码，若固定电话有区号或分机号则用\"-\"符号隔开！");
				butk("#btnSsu","#btnSca");
				return false;	*/		
			}else if(morptUrl!=null&&(!re.test(morptUrl)) && morptUrl.length > 0){
                /*URL格式输入不正确！请检查后重新输入！\n格式以(http|https)://开头 \n例如:http://www.baidu.com */
                alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_7"));
	        	$('#spbindurl').select();
	        	butk("#btnSsu","#btnSca");
	        	return false;
			}else if(v!=null&&(!re.test(v)) && v.length > 0){
				/*URL格式输入不正确！请检查后重新输入！\n格式以http://开头 \n例如:http://www.baidu.com */
	        	alert(getJsLocaleMessage("txgl","txgl_js_userdata_5"));
	        	$('#sx').select();
	        	butk("#btnSsu","#btnSca");
	        	return false;
			}else if(rptUrl!=null&&(!re.test(rptUrl)) && rptUrl.length > 0){
                /*URL格式输入不正确！请检查后重新输入！\n格式以http://开头 \n例如:http://www.baidu.com */
                alert(getJsLocaleMessage("txgl","txgl_js_userdata_5"));
	        	$('#rptUrl').select();
	        	butk("#btnSsu","#btnSca");
	        	return false;
			}else if(!checkIps($('#td-ips')).result){//检验ip地址
                /*存在重复或不合法的IP地址！*/
                alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_8"))
				butk("#btnSsu","#btnSca");
				return false;
			}
			else if(document.forms["form1"].hidOpType.value=="add"){
				userid=document.getElementById("userid2").value;
				accouttype=document.getElementById("accouttype").value;
				$.post("pag_userData.htm",{userid:userid,accouttype:accouttype,method:"checkName"},function(result){
					if(result=="false"){
                        /*该账户已存在，请重新输入！*/;
                        alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_9"));
						document.getElementById("userid2").focus();
						butk("#btnSsu","#btnSca");
						return false;
					}else document.forms["form1"].submit();
				});
			}
			else return true;

			return false;
		}


function checkstr(){
	if (!((event.keyCode>47 && event.keyCode<58) ||(event.keyCode>64 && event.keyCode<91)||(event.keyCode>96 && event.keyCode<123))) event.returnValue = false;
}
function checknum(){
	if (!((event.keyCode>47 && event.keyCode<58) || event.keyCode==45)) event.returnValue = false;
}


function changeSel(i){
	if(i==1){
		document.getElementById("daili").style.display="none";
		document.getElementById("daili2").style.display="none";
		$('#selectFlag').find("> option").each(function(){
			if(this.value==2)
			{
				$(this).remove();
			}
		});
	}else{
		document.getElementById("daili").style.display="block";
		document.getElementById("daili2").style.display="block";
		/*后付费*/
		$('#selectFlag').append('<option value=2>'+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_10")+'</option>');
	}
}
//sp帐号验证====chen add====
function spCard(obj){
	var reg=/[^\w]/g,
	val=$(obj).val();
	if(reg.test(val)){
		$(obj).val(val.replace(reg,'').toUpperCase());
	}else{
		$(obj).val(val.toUpperCase());
	}
}

function del(userId,userType)
{
    /*确定要删除该账户吗？*/
    if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_11")))
    {
		var lgusername = $("#lgusername").val();
		$.post("pag_userData.htm",{userId:userId,method:"delete",usertype:userType,lgusername:lgusername},function(result){
			if(result=="true")
			{
                /*删除成功！*/
                alert(getJsLocaleMessage("common","common_deleteSucceed"));
				location.href="pag_userData.htm";
				//document.getElementById("pageForm").submit();
			}else if(result=="mid")
			{
                /*该账户存在绑定的路由，无法删除！*/
                alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_13"));
			}else 
			{
                /*操作失败！*/
                alert(getJsLocaleMessage("common","common_operateFailed"));
			}
		});
	}
}



//修改SP账号状态
function changestate(i, keyid)
{
	var ks=$.trim($("#userState"+i).attr("value"));
	//提示有个问题没解决，先不提示了直接该状态
	//if(true){
	/*确定要修改该账户的状态吗？*/
	if(confirm(getJsLocaleMessage("txgl","txgl_js_userdata_4"))){
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername = $("#lgusername").val();
		$.post("pag_userData.htm?method=ChangeSate",{uid:i,status:ks,lgcorpcode:lgcorpcode,lgusername:lgusername,keyId:keyid},function(result){
			
	        if (result == "true") {
                /*alert("更改状态成功！");*/
                alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_1"));
				$("#userState"+i).empty();
				if(ks == 0)
				{
					/*已激活   失效*/
				    $("#userState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_48")+"</option>");
				    $("#userState"+i).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49")+"</option>");
				}
				else
				{
                    /*已失效   激活*/
					$("#userState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_51")+"</option>");
					$("#userState"+i).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50")+"</option>");
				}
				//changeEmpSelect($("#userState"+i),80,function(){
				  //changestate(i);
				//});
				$("#userState"+i).next(".c_selectBox").remove();
				$("#userState"+i).isSearchSelect({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
					changestate(i, keyid);
					var parents=$(data.box.self).parent().parent().parent();
			 		parents.siblings().removeClass('c_selectedBg');
				    parents.addClass('c_selectedBg');
				},function(data){
					 var self=$(data.box.self);
	  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
				});
				//black();
			}else{
                /*修改失败！*/
                alert(getJsLocaleMessage("common","common_modifyFailed"));
			}		
		});
	}else{
		/*$("#userState"+i).empty();*/
		if(ks != 0)
		{
            /*已激活   失效*/
		    $("#userState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_48")+"</option>");
		    $("#userState"+i).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49")+"</option>");
		}
		else
		{
            /*已失效   激活*/
			$("#userState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_51")+"</option>");
			$("#userState"+i).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50")+"</option>");
		}
		//changeEmpSelect($("#userState"+i),80,function(){
		  //changestate(i);
		//});
		$("#userState"+i).next(".c_selectBox").remove();
		$("#userState"+i).isSearchSelect({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(){
			changestate(i, keyid);
		},function(data){
			 var self=$(data.box.self);
				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
		});
	}
}
function checkIps($obj){
	var msg = {};
	var data = [];
	var hash= {};
	msg.result = true;
	$ul = $obj.find('ul');
	$ul.children('li').each(function(index){
		var val = getIp($(this));
		if(val){
			if(!hash[val]){
				hash[val] = true;
				data.push(val);
			}else{
				msg.result = false;
				$(this).css('borderColor','red');
				return false;
			}
		}else if(val === undefined){
			msg.result = false;
			$(this).css('borderColor','red');
			return false;
		}else{
			if($(this).siblings().size()>0){
				$(this).remove();
			}
		}
	})
	if(msg.result){
		msg.data = data.join(',');
		if(msg.data.length==0){msg.data = ' ';}
		$('#ips').val(msg.data);
	}
	return msg;
}
function getIp($li){
	var temp = [];
	var isBlank = true;
	$li.children(':text').each(function(){
		if($(this).val()){
			isBlank = false;
			temp.push($(this).val());
		}
	})
	if(temp.length==4){
		return temp.join('.');
	}else if(!isBlank){
		return undefined;
	}else{
		return null;
	}
	
}

function save(){
	
	var pushMoTime=$("#pushMoTime").val();
	var pushFailcnt=$("#pushFailcnt").val();
	if(pushMoTime=='1'&&pushFailcnt=='0'){
		/*固定次数不能为0！*/
		alert(getJsLocaleMessage("txgl","txgl_js_userdata_3"));
		return;
	}
    /*保存成功！*/
    alert(getJsLocaleMessage("txgl","txgl_js_install_28"));
	document.forms["updateInfo"].submit();
}
function changepwd(){
	var pwdencode=$("#pwdencode").val();
	if(pwdencode=="0"){
		$("#pwdEncodetr").css('display','none'); 
		$("#pwdEncodeStr").val("00000000");
	}else{
		$("#pwdEncodeStr").val("00000000");
		$("#pwdEncodetr").css('display',''); 
	}
}



function changepush(){
	var pushPwdEncode=$("#pushPwdEncode").val();
	if(pushPwdEncode=="0"){
		$("#pushPwdEncodeStr").val("00000000");
		$("#pushtr").css('display','none'); 
	}else{
		$("#pushPwdEncodeStr").val("00000000");
		$("#pushtr").css('display',''); 
	}
}
function back(){
	var pathUrl = $("#pathUrl").val();
	 window.location.href=pathUrl+"/mwp_userData.htm?";	
}
//不限次数的方法
function changetimes(){
	var pushMoTime=$("#pushMoTime").val();
		if(pushMoTime=="1"){
		$("#pushFailcnt").val("3"); 
		$("#pushFailcnt").attr("disabled","");
		$("#numbers").css('display','none');
	}else{
		$("#pushFailcnt").val("0");
		$("#pushFailcnt").attr("disabled","disabled"); 
		
		$("#numbers").css('display',''); 
	}
}

