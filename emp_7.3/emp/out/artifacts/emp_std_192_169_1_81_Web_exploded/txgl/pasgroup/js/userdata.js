

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
	var strRegex = "^(https|http)://(.)*";
	var v =null;
	var rptUrl =null;
	var moUrl = null;
	var morptUrl = null;
	var morptmode = null;
	var pushmourl = null;
	var pushrpurl = null;
    if(!$(".accability[name='cxval']").prop('checked')){
        v=$.trim(document.getElementById("sx").value);
        rptUrl=$.trim(document.getElementById("rptUrl").value);
        moUrl = $.trim(document.getElementById("sx").value);
        morptUrl=$.trim(document.getElementById("spbindurl").value);
        morptmode=$("#morptmode").val();
        pushmourl=$("#pushmourl");
        pushrpurl=$("#pushrpurl");
    }
	var re=new RegExp(strRegex);
    if(!$(".accability:checked").val()){
        window.alert("信息类型不能为空！");
        butk("#btnSsu","#btnSca");
        return false;
    }else if(document.forms["form1"].userid.value.length==0 || document.forms["form1"].userid.value.length!=6){
				//window.alert("SP账号不能为空且长度只能为6位字母或数字！");
				window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_1"));
				document.forms["form1"].userid.focus();
				butk("#btnSsu","#btnSca");
				return false;
	}else if(document.forms["form1"].userpassword.value==null){
		//window.alert("账户密码不能为空！");
		window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_2"));
		document.forms["form1"].userpassword.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}
	else if(document.forms["form1"].userpassword.value.length<6 || document.forms["form1"].userpassword.value.length>18) {
		//window.alert("密码长度必须在6-18位数之间！");
		window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_3"));
		document.forms["form1"].userpassword.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(document.forms["form1"].staffname.value==null || document.forms["form1"].staffname.value.length<1){
		//window.alert("账户名称不能为空！");
		window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_4"));
		document.forms["form1"].staffname.focus();
		butk("#btnSsu","#btnSca");
		return false;
	/*}else if(document.forms["form1"].lxrph.value!=""&&!PhoneCheck(document.forms["form1"].lxrph.value)){
		alert("请输入有效的联系号码，若固定电话有区号或分机号则用\"-\"符号隔开！");
		butk("#btnSsu","#btnSca");
		return false;	*/
	//}else if(document.forms["form1"].loginid.value=="请选择")
	}else if(document.forms["form1"].loginid.value==getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_5")) {
		//window.alert("请选择代理账号，如没有代理账号，请先在[代理账号管理]中新建代理账号。");
		window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_6"));
		document.forms["form1"].agentUser.focus();
		butk("#btnSsu","#btnSca");
		return false;
	} else if(pushmourl!=null&&pushmourl.is(":checked") && morptmode == 2 &&  moUrl.length <= 0) {
		alert(getJsLocaleMessage("txgl","txgl_js_userdata_19"));
		document.forms["form1"].moUrl.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}  else if(pushrpurl!=null&&pushrpurl.is(":checked") && morptmode == 2 && rptUrl.length <= 0) {
		alert(getJsLocaleMessage("txgl","txgl_js_userdata_20"));
		document.forms["form1"].rptUrl.focus();
		butk("#btnSsu","#btnSca");
		return false;
	}  else if(morptmode == 1 && morptUrl.length <= 0) {
		alert(getJsLocaleMessage("txgl","txgl_js_userdata_21"));
		document.forms["form1"].spbindurl.focus();
		butk("#btnSsu","#btnSca");
		return false;
	} else if(morptUrl!=null&&(!re.test(morptUrl)) && morptUrl.length > 0){
		//alert("URL格式输入不正确！请检查后重新输入！\n格式以(http|https)://开头 \n例如:http://www.baidu.com ");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_7"));
		$('#spbindurl').select();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(v!=null&&(!re.test(v)) && v.length > 0){
		//alert("URL格式输入不正确！请检查后重新输入！\n格式以(http|https)://开头 \n例如:http://www.baidu.com ");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_7"));
		$('#sx').select();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(rptUrl!=null&&(!re.test(rptUrl)) && rptUrl.length > 0){
		//alert("URL格式输入不正确！请检查后重新输入！\n格式以(http|https)://开头 \n例如:http://www.baidu.com ");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_7"));
		$('#rptUrl').select();
		butk("#btnSsu","#btnSca");
		return false;
	}else if(!checkIps($('#td-ips')).result){//检验ip地址
		//alert('存在重复或不合法的IP地址！')
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_8"))
		butk("#btnSsu","#btnSca");
		return false;
	}
	else if(document.forms["form1"].hidOpType.value=="add"){
		userid=document.getElementById("userid2").value;
		accouttype=document.getElementById("accouttype").value;
		$.post("pag_userData.htm",{userid:userid,accouttype:accouttype,method:"checkName"},function(result){
			if(result=="false"){
				//alert("该账户已存在，请重新输入！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_9"))
				document.getElementById("userid2").focus();
				butk("#btnSsu","#btnSca");
				return false;
			}else {
				document.forms["form1"].submit();
			}
		});
	}
	else {
        $('.accability').attr("disabled",false);
    		return true;
    }

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
	//if(confirm("确定要删除该账户吗？"))
	if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_11")))	
	{
		var lgusername = $("#lgusername").val();
		$.post("pag_userData.htm",{userId:userId,method:"delete",usertype:userType,lgusername:lgusername},function(result){
			if(result=="true")
			{
				//alert("删除成功！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_12"));
				location.href="pag_userData.htm";
				//document.getElementById("pageForm").submit();
			}else if(result=="mid")
			{
				//alert("该账户存在绑定的路由，无法删除！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_13"));
			}else 
			{
				//alert("操作失败！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_14"));
			}
		});
	}
}



//修改SP账号状态
function changestate(i, keyid)
{
	var ks=$.trim($("#userState"+i).attr("value"));
	//提示有个问题没解决，先不提示了直接该状态
	if(true){
	//if(confirm("确定要修改该账户的状态吗？")){
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername = $("#lgusername").val();
		$.post("pag_userData.htm?method=ChangeSate",{uid:i,status:ks,lgcorpcode:lgcorpcode,lgusername:lgusername,keyId:keyid},function(result){
			
	        if (result == "true") {
				//alert("更改状态成功！");
	        	alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_15"));
				$("#userState"+i).empty();
				if(ks == 0)
				{
				    $("#userState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_16")+"</option>");
				    $("#userState"+i).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_17")+"</option>");
				}
				else
				{
					$("#userState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_18")+"</option>");
					$("#userState"+i).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_19")+"</option>");
				}
				//changeEmpSelect($("#userState"+i),80,function(){
				  //changestate(i);
				//});
				$("#userState"+i).next(".c_selectBox").remove();
				$("#userState"+i).isSearchSelectNew({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
					changestate(i, keyid);
					var parents=$(data.box.self).parent().parent().parent();
			 		parents.siblings().removeClass('c_selectedBg');
				    parents.addClass('c_selectedBg');
				},function(data){
					 var self=$(data.box.self);
	  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px','display':'block'});
				});
				//black();
			}else{
				//alert("修改失败！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_20"));
			}		
		});
	}else{
		/*$("#userState"+i).empty();*/
		if(ks != 0)
		{
		    $("#userState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_16")+"</option>");
		    $("#userState"+i).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_17")+"</option>");
		}
		else
		{
			$("#userState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_18")+"</option>");
			$("#userState"+i).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_19")+"</option>");
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

