//跳到添加主机页面
function toAdd()
{
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	window.location.href= "mon_hostMonCfg.htm?method=toAdd&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}

//跳到主机监控设置页面
function toEdit(hostid)
{
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	$.post("mon_hostMonCfg.htm",
			{method:"checkHost",hostid:hostid,isAsync:"yes"},
			function(result){
				if(result=="no")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_1"));
					location.reload();
				}else{
					window.location.href= "mon_hostMonCfg.htm?method=toEdit&hostid="+hostid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				}
			
			});	

	
}


//--------mon_editHost.jsp调用的js--------
//返回
function doreturn()
{
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	window.location.href= "mon_hostMonCfg.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}
//修改
function editHost()
{
	if(validate()){
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		var monphone=$("#monphone").val();
		var monemail = $("#monemail").val();
		if(monemail==" "){monemail="";}
		$("#rContent").find("input[type='button']").attr("disabled","disabled");
		$.ajax({
			url:"mon_gateAcctMonCfg.htm?method=checkPhone",
			method:"POST",
			async:true,
			data:{
				lgcorpcode:lgcorpcode,
				lguserid: lguserid,
				monphone:monphone,
				isAsync:"yes"
			},
			success:function(result){
				if(result == "outOfLogin")
				{
					$("#logoutalert").val(1);
					location.href=pathUrl+"/common/logoutEmp.html";
					return;
				}
				else if(result=="error"||result=="phoneError")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_4"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}else if(!monemail.length==0 &&!checkEmail(monemail)){
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_5"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}
				else
				{
					$("#editForm").submit();
				}
			}
			
		});
	}
}

//--------mon_addHost.jsp调用的js--------
//新建
function addHost()
{
	if(validate())
	{
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		var monphone=$("#monphone").val();
		var monemail = $("#monemail").val();
		if(monemail==" "){monemail="";}
		$("#rContent").find("input[type='button']").attr("disabled","disabled");
		$.ajax({
			url:"mon_gateAcctMonCfg.htm?method=checkPhone",
			method:"POST",
			async:true,
			data:{
				lgcorpcode:lgcorpcode,
				lguserid: lguserid,
				monphone:monphone,
				isAsync:"yes"
			},
			success:function(result){
				if(result == "outOfLogin")
				{
					$("#logoutalert").val(1);
					location.href=pathUrl+"/common/logoutEmp.html";
					return;
				}
				else if(result=="error"||result=="phoneError")
				{
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_4"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}
				else if(!monemail.length==0 &&!checkEmail(monemail)){
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_5"));
					$("#rContent").find("input[type='button']").attr("disabled","");
				}
				else
				{
					$("#addForm").submit();
				}
			}
			
		});
	}
	
}
function validate(){
	var isContinue = true;
	//判断非空项是否为空
	$(".xinhao").each(function(){
		var $text=$(this).siblings(".input_bd");
		if(!$text.val()||$text.val().length<=0){
			isContinue = false;
			//alert($(this).parent().prev().text().replace(/[:： ]/g,'')+getJsLocaleMessage("ptjk","ptjk_jkgl_zj_10"));
			alert($(this).parent().prev().text().replace(/[:：]/g,'')+getJsLocaleMessage("ptjk","ptjk_jkgl_zj_10"));
			return false;
		}
	});
	//如果存在非空项为空则终止
	if(!isContinue){
		return false;
	}
	//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
	//判断ip是否符合
	var ip1=$("#ip1").val();
	var ip2=$("#ip2").val();
	var ip3=$("#ip3").val();
	var ip4=$("#ip4").val();
	var ip5=$("#ip5").val();
	var ip6=$("#ip6").val();
	var ip7=$("#ip7").val();
	var ip8=$("#ip8").val();
	var oupip=ip1+"."+ip2+"."+ip3+"."+ip4;
	var adapter1=ip5+"."+ip6+"."+ip7+"."+ip8;
	if(oupip!="..."&&!ipPatrn.test(oupip))
	{
		isContinue = false;
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_2"));
		return;
	}
	if(adapter1!="..."&&!ipPatrn.test(adapter1))
	{
		isContinue = false;
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_3"));
		return;
	}
	$("#oupip").val(oupip);
	$("#adapter1").val(adapter1);
	/*$(".ip").each(function(){
		var value=$(this).val();
		if(!ipPatrn.test(value)){
			isContinue = false;
			alert($(this).parent().prev().text().replace(/[:： ]/g,'')+"格式不正确！");
			return false;
		}
	})*/
	if(!isContinue){
		return false;
	}
	//判断手机号码格式
	var phone=$("#monphone").val();
	//如果手机号码有值 则必须满足格式验证
	if(!validatePhones($("#monphone"))){
		return false;
	}
	//判断CPU占用比率告警阀值
	var cpubl=$("#cpuusage").val();
	if(cpubl.length>0&&cpubl>100){
		isContinue = false;
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_4"));
		return false;
	}
	//物理内存告警判断
	var memusage = $("#memusage").val();
	if(memusage.length>0&&memusage-$("#hostmem").val()>0){
		isContinue = false;
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_5"));
		return false;
	}
	//磁盘告警判断
	var diskfreespace = $("#diskfreespace").val();
	if(diskfreespace.length>0&&diskfreespace-$("#hosthd").val()>0){
		isContinue = false;
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_6"));
		return false;
	}
	//告警手机号为空 则 进行确认
	if(phone.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_9"))){
		return false;
	}
	
	//告警邮箱为空 则 进行确认
	var email=$("#monemail").val();
	if(email==" "){email="";}
	if(email.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_3"))){
		return false;
	}
	return true;
}

//验证邮箱是否有效
function checkEmail(email_str)
{
	var arr = [ "ac", "com", "net", "org", "edu", "gov", "mil", "ac\.cn",
		"com\.cn", "net\.cn", "org\.cn", "edu\.cn" ];
	var temp_arr = arr.join("|");
	// reg
	var reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
	var reg = new RegExp(reg_str);
	if (reg.test(email_str)) {
		return true;
	}else{
		return false;
	}
}

function del(id){
	if(!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_7"))){
		return;
	}
	if(!id){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_8"));
		return;
	}
	$.post("mon_hostMonCfg.htm",
			{method:"delete",id:id,isAsync:"yes"},
			function(result){
				if(result=="error"){
                	alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zj_9"));
                }else{
                	location.reload();
                }
			})
}
