//设置阀值
function toEdit(gateaccount)
{
	if(gateaccount==null || gateaccount=="null" || gateaccount=="")
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_1"));
	}
	$("#editGateAcct").find("input[name='gateaccount']").val(gateaccount);
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	$.post("mon_gateAcctMonCfg.htm",
	{method:"toEdit",lgcorpcode:lgcorpcode,gateaccount:gateaccount,lguserid: lguserid,isAsync:"yes"},
	function(result){
		if(result == "outOfLogin")
		{
			$("#logoutalert").val(1);
			location.href=path+"/common/logoutEmp.html";
			return;
		}
		else if(result!="error")
		{
			var array = result.split("|");
			$("#editGateAcct").find("#rptsndratio").val(array[0]);
			$("#editGateAcct").find("#mosndratio").val(array[1]);
			$("#editGateAcct").find("#mtremained").val(array[2]);
			$("#editGateAcct").find("#moremained").val(array[3]);
			$("#editGateAcct").find("#rptremained").val(array[4]);
			$("#editGateAcct").find("#linknum").val(array[5]);
			$("#editGateAcct").find("#userfee").val(array[6]);
			$("#editGateAcct").find("#monphone").val(array[7]);
			$("#editGateAcct").find("#monstatus option[value='"+array[8]+"']").attr('selected','selected');
			$("#editGateAcct").find("#isarrearage option[value='"+array[9]+"']").attr('selected','selected');
			$("#editGateAcct").find("#monemail").val(array[10]);
			$("#editGateAcct").dialog("open");
		}
		else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_2"));
		}
	});
}

function edit()
{
	var gateaccount = $("#editGateAcct").find("input[name='gateaccount']").val();
	if(gateaccount==null || gateaccount=="null" || gateaccount=="")
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_1"));
	}
	if(!validate($("#editGateAcct").children(".edit_spacct"))){
		return false;
	}
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var monphone=$("#editGateAcct").find("#monphone").val();
	var userfee=$("#editGateAcct").find("#userfee").val();
	var mtremained=$("#editGateAcct").find("#mtremained").val();
	var moremained=$("#editGateAcct").find("#moremained").val();
	var rptremained=$("#editGateAcct").find("#rptremained").val();
	var monstatus=$("#editGateAcct").find("#monstatus").val();
	var linknum=$("#editGateAcct").find("#linknum").val();
	var mosndratio=$("#editGateAcct").find("#mosndratio").val();
	var rptsndratio=$("#editGateAcct").find("#rptsndratio").val();
	var isarrearage=$("#editGateAcct").find("#isarrearage").val();
	var monemail=$("#editGateAcct").find("#monemail").val();
	if(monemail==" "){monemail="";}
	//告警邮箱为空 则 进行确认
	if((monemail==null || monemail.length==0) && !confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_3"))){
		return false;
	}
	$("#editGateAcct").find("input type='button'").attr("disabled","disabled");
	
	
	$.post("mon_gateAcctMonCfg.htm",
	{method:"edit",
	lgcorpcode:lgcorpcode,
	lguserid: lguserid,
	gateaccount:gateaccount,
	monphone:monphone,
	userfee:userfee,
	mtremained:mtremained,
	moremained:moremained,
	rptremained:rptremained,
	monstatus:monstatus,
	linknum:linknum,
	mosndratio:mosndratio,
	rptsndratio:rptsndratio,
	isarrearage:isarrearage,
	monemail:monemail,
	isAsync:"yes"},
	function(result){
		$("#editGateAcct").find("input type='button'").attr("disabled","");
		if(result == "outOfLogin")
		{
			$("#logoutalert").val(1);
			location.href=path+"/common/logoutEmp.html";
			return;
		}
		else if(result!="error")
		{
			if(result=="phoneError")
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_4"));
			}else if(result=="emailError"){
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_5"));
			}
			else
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_6"));
				$("#editGateAcct").dialog("close");
				$("#pageForm").find("#search").click();
			}
		}
		else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_8"));
		}
	});
}

function doreturn()
{
	$("#editGateAcct").dialog("close");
}
function validate($table){
	var isContinue=true;
	//文本框各值验证
	$table.find(":text").each(function(){
		var value=$(this).val();
		if(value.length>0&&$(this).hasClass("percent")&&value>100){
			isContinue = false;
			alert($(this).parent().prev().text().replace(/[:： ]/g,'')+getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_8"));
			return false;
		}
	});
	if(!isContinue){
		return false;
	}
	//判断手机号码格式
	var phone=$("#monphone").val();	
	if(!validatePhones($("#monphone"))){
		return false;
	}
	//告警手机号为空 则 进行确认
	if(phone.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_9"))){
		return false;
	}
	
	return true;
}