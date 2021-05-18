function sizeset()
{
	var widd = $("body").width();
	if(widd<=1000)
	{
		
		$("#wrap").css("width","1000px");
		//$("#topFrame").contents().find(".top_bg_table").css("width","1000px");
	}else
	{
		//var widdd = 1000+Math.ceil((widd-1000)/3);
		//$("#topFrame").contents().find(".top_bg_table").css("width",widdd+"px");
		$("#wrap").css("width","100%");
	}
}

function befoclose()
{
//	$.post("<%=request.getContextPath()%>/logout");
}
function goout(){
    var logoutalert = $("#logoutalert").val();
    if(logoutalert == 0)
    {
    	$("#logoutalert").val(1);
    	setTimeout("$('#logoutalert').val(0);", 1500 );
       return "";
    }
}

function focusShow_label(ep) {
	ep.hide().prev().focus();
}

function focusShowPass() {
	var ep = $("#new_pass1");
	ep.next("div").css("display", "block");
	ep.focus();
}

function blurPass(){
	var ccontent = $("#new_pass1");
	if (ccontent.val() == "") {
		ccontent.next("div").css("display", "block");
	} else {
		ccontent.next("div").css("display", "none");
	}
}
function focusPass() {
	var ep = $("#new_pass1");
	ep.next("div").css("display", "none");
}

//修改密码
function doUpdatePass()
{
    var lguserid =$("#lguserid").val();
    var pathUrl = $("#pathUrl").val();
    $.post(pathUrl+"/emp_tz.htm",{method:"getSysUserInfo",lguserid:lguserid}
	 ,function(result){
			if(result !=null && result == "error")	
			{
			    alert("获取用户个人信息错误！");
			    return ;
			}
			else if(result !=null && result !="")
			{
			    var array=result.split('&');
			    $("#username").text(array[0]);
			    $("#name").text(array[1]);
			    $("#sex").text(eval(array[2])-0==0?"女":"男");
			    $("#mobile").attr("value",array[3]!="null"?array[3]:"");
			    $("#oph").attr("value",array[4]!="null"?array[4]:"");
			    $("#EMail").attr("value",array[5]!="null"?array[5]:"");
			    $("#qq").attr("value",array[6]!="null"?array[6]:"");
			    $("#zhu").html("");
			    
			    $("#UpdatePassDiv").dialog({
			        title :"个人设置",
					autoOpen: false,
					height: 430,
					width: 340,
					resizable:false,
					closeOnEscape: false,
					modal:true,
					close:function(){
			    	     closeDiv();
				    }
			   });
			   $('#UpdatePassDiv').dialog('open');
			}
	});		  

}

function changeinfo(type)
{
    $("#button").attr("disabled","");
    if(type=='1')
    {
        $("#updateinfo").removeClass();
        $("#updatepw").removeClass();
        $("#updateinfo").addClass("infotd1");
        $("#updatepw").addClass("infotd2");
        
        $("#updateinfoDiv").show();
        $("#updatePwDiv").hide();
    }
    else
    {
        $("#updateinfo").removeClass();
        $("#updatepw").removeClass();
        $("#updatepw").addClass("infotd1");
        $("#updateinfo").addClass("infotd2");
        
        $("#updatePwDiv").show();
        $("#updateinfoDiv").hide();
    }
}

function checkM(){
	$("#button").attr("disabled",false);
	var patrnOph=/^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/; //可以“+”开头，除数字外，可含有“-”
	var patrnEMail=/^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
	var patrnMobile=/^[0-9]{11}$/;///^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8,16}$/; //必须以数字开头，除数字外，可含有“-”
	var patrnQQ=/^[0-9]*[1-9][0-9]*$/;
	var mobile=$("#mobile").val();
	var EMail=$("#EMail").val();
	var oph=$("#oph").val();
	var qq=$("#qq").val();
	$("#zhu").html("");

	if(!patrnEMail.exec(EMail)&&EMail.length!=0)
	{	
		$("#zhu").html("注：E-mail不合法！");
		$("#button").attr("disabled","disabled");
		return false;
	}else if(!patrnOph.exec(oph)&&oph.length!=0)
	{
		$("#zhu").html("注：办公电话号码只能7-8位直拨号码，可以加3-4位区号和1－4位分机号！");
		$("#button").attr("disabled","disabled");
		return false;
	}else if(qq.length!=0 && !patrnQQ.exec(qq)){
		$("#zhu").html("注：QQ只能是数字！");
		$("#button").attr("disabled","disabled");
		return false;
	}
}

function checkPass(agoPass)
{
	var pathUrl = $("#pathUrl").val();
	if(agoPass!="")
	{
		$.post(pathUrl+"/emp_tz.htm",{method:"checkPass",agoPass:agoPass},function(result)
			{
				if(result=="true")
				{
					$('#zhuPass').html("<font style='color:green'>原密码输入正确</font>");
				}else if(result=="false")
				{
					$('#zhuPass').html("<font style='color : #FF0000'>原密码输入错误</font>");
				}
			}
		);
	}else
	{
		$('#zhuPass').html("<font style='color : #FF0000'>请输入原密码</font>");
	}
}


var isuppw = false;

function checkForm()
{
   if($("#updateinfoDiv").is(":hidden"))
   {
       checkForm2();
   }
   else
   {
       checkForm1();
   }
}

function checkForm1(){
	var mobile=$("#mobile").val();
	var EMail=$("#EMail").val();
	var oph=$("#oph").val();
	var qq=$("#qq").val();
	var pathUrl = $("#pathUrl").val();
	if(mobile.length==0){
         alert("手机号码不能为空！")
	}
	else if(!checkPhone(mobile))
	{
		alert("手机号码输入非法！");
	}
	else{
		$("#button").attr("disabled","disabled");
		$.post(pathUrl+"/emp_tz.htm", {
			mobile : mobile,
			method:"checkMobileByInfo",
			separator:";"
			
		},function(result)
		{
			if(result==0)
			{
				alert("无效的手机号码，请确认后重新输入！");
				document.modifyMobile.mobile.focus();
				$("#button").attr("disabled",false);
			}else if(result==2)
			{
				alert("验证手机号码格式失败，请确认网络是否畅通！");
				$("#button").attr("disabled",false);
			}else
			{
				var username=$("#username").text();
				$.post(pathUrl+"/emp_tz.htm",{username:username,type:"phone",mobile:mobile,EMail:EMail,
				lgguid:$("#lgguid").val(),oph:oph,qq:qq,method:"update"},function(result){
					if(result=="true"){
						alert("修改成功！");
						$("#button").attr("disabled",false);
						//doReset();
					}else{ 
						alert("修改失败！");
						$("#button").attr("disabled",false);
						}
				});
			}
		});
		}
  }

function checkForm2(){
	var username=$("#username").text();
	var pass2=document.modifyPass.pass2.value;
	var newpass1=document.modifyPass.new_pass1.value;
	var newpass2=document.modifyPass.new_pass2.value;
	var pathUrl = $("#pathUrl").val();
	var zhu=$("#zhuPass").find("font").text();
	var reg =/[\d]/ ;//数字
	var reg1 =/[a-zA-Z]/ ;
	var reg2 =/\W/ ;
	if(zhu=="原密码输入错误")
	{
		return;
	}
	if(pass2==""){ 
		$('#zhuPass').html("<font style='color : #FF0000'>请输入原密码</font>");
	    return;
	}else if(newpass1==""){ 
	   $('#zhuPass').html("<font style='color : #FF0000'>新密码不能为空</font>");
	   return;
	}else if(newpass2==""){ 
		 $('#zhuPass').html("<font style='color : #FF0000'>重新输入新密码不能为空</font>");
		 return;
	}else if(newpass1!=newpass2){ 
		$('#zhuPass').html("<font style='color : #FF0000'>新密码两次输入不一致</font>");
		document.modifyPass.new_pass2.focus();
		return;
	}else if(username == newpass1){ 
		$('#zhuPass').html("<font style='color : #FF0000'>新密码不能与用户名相同</font>");
		document.modifyPass.new_pass2.focus();
		return;
	}else{
		var LgGuid = $("#lgguid").val();
		var lgguid="";
		var flag="";
		$.post(pathUrl+"/emp_tz.htm",{lgguid:lgguid,method:"valid"},function(result){
			flag="true";
			var array=result.split('@');
			if(array[0] !="" && array[0]!=="0"){	
				var intpas=parseInt(array[0]);
				if(newpass1.length<intpas){
					//alert("密码设置不得少于"+intpas+"位  ");
					$('#zhuPass').html("<font style='color : #FF0000'>密码设置不得少于"+intpas+"位 </font>");
					flag="false";
					return;
				}
			} 
			
			if(array[1]!=''&&!reg.test(newpass1)&&array[2]!=''&&!reg1.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>密码还需要包括数字,字母 </font>");
				flag="false";
				return;
				
			} 
			
			if(array[1]!=''&&!reg.test(newpass1)&&array[3]!=''&&!reg2.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>密码还需要包括数字,符号 </font>");
				flag="false";
				return;
				
			}
			
			if(array[2]!=''&&!reg1.test(newpass1)&&array[3]!=''&&!reg2.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>密码还需要包括字母,符号 </font>");
				flag="false";
				return;
				
			}
			if(array[1]!=''&&!reg.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>密码还需要包括数字</font>");
				flag="false";
				return;
				
			} 
			
			if(array[2]!=''&&!reg1.test(newpass1)){//
				$('#zhuPass').html("<font style='color : #FF0000'>密码还需要包括字母 </font>");
				flag="false";
				return;
				
			} 
			if(array[3]!=''&&!reg2.test(newpass1)){
				$('#zhuPass').html("<font style='color : #FF0000'>密码还需要包括符号 </font>");
				flag="false";
				return;
				
			}  
			$('#zhuPass').html("");
			if($("#button").attr("disabled")==true){
				return;
			}
			$("#button").attr("disabled","disabled");
			if(flag=="true"){
			$.post(pathUrl+"/emp_tz.htm",{username:username,type:"pass",pass:newpass1,agoPass:pass2,method:"update",lgguid:LgGuid},function(result){
				if(result=="true"){
					alert("修改成功！");
					document.modifyPass.pass2.value="";
					document.modifyPass.new_pass1.value="";
					document.modifyPass.new_pass2.value="";
					$('#zhuPass').html("");
					isuppw = true;
					$("#button").attr("disabled","");
					doReset();
				}else if(result=="false")
				{
					$("#button").attr("disabled","");
					alert("修改失败！");
				}else if(result=="mid")
				{
					$("#button").attr("disabled","");
					alert("原密码输入不正确，请重新输入！");
					document.modifyPass.pass2.focus();
				}
			});
			}
		});

	}
}
//通过模块编码打开新的选项卡菜单
function showMenu(menucode)
{
	var $lf = $("iframe[name='mainFrame']").contents().find("iframe[name='I1']");
   	$lf[0].contentWindow.showTabMenClk(menucode);
   	$lf.contents().find("a[id='ak"+menucode+"']").trigger("click");
}

