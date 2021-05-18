
$(document).ready(function() {
	var pathUrl = $("#pathUrl").val();
	var index=1;
	var url=pathUrl+"/mwp_userData.htm";
	$('#subBut').click(function() {
			$("#subBut").attr("disabled", true);
		   var cacertname = $("#cacertname").val();
		   var userid = $("#userid").val();
		   var gstype = $("#gstype option:selected").val();
		   var verifypeer = $("#verifypeer option:selected").val();
		   var verifyhost = $("#verifyhost option:selected").val();
		   if(gstype==""||verifypeer==""||verifyhost==""||userid==""){
		   		/*标识ID,服务端证书校验,服务端证书类型,证书校验方式为必选项！*/
			   alert(getJsLocaleMessage("txgl","txgl_js_mwedithttps_1"));
			   $("#subBut").attr("disabled", false);
		   }else if(verifypeer=="2"&&gstype=="1"&&cacertname == ""){
		   		/*服务端证书为私有时，服务端证书必需输入具体证书信息！*/
		      alert(getJsLocaleMessage("txgl","txgl_js_mwedithttps_2"));
		      $("#cacertname").focus();
		      $("#subBut").attr("disabled", false);
		   }else{
		    	  $.post(url,{
		    		  method:"sethttps",
		    		  userid:userid,
		    		  verifypeer:verifypeer,
		    		  verifyhost:verifyhost,
		    		  cacertname:cacertname
				     }, function(result) {
						if (result=="true") 
						{
                            /*修改成功！*/
                            alert(getJsLocaleMessage("common","common_modifySucceed"));
							window.parent.location.href=pathUrl+"/mwp_userData.htm";
						}
						else if(result=="false"){
								/*修改失败！*/
								alert(getJsLocaleMessage("common","common_modifyFailed"));
								$("#subBut").attr("disabled", false);
						} 
						else if(result=="nouserid"){
							/*传入的sp账号不合法!*/
							alert(getJsLocaleMessage("txgl","txgl_js_mwedithttps_3"));
							$("#subBut").attr("disabled", false);
						}
						else if(result=="noverifypeer"){
							/*服务端证书校验必填*/
							alert(getJsLocaleMessage("txgl","txgl_js_mwedithttps_4"));
							$("#subBut").attr("disabled", false);
						}else if(result=="noverifyhost"){
							/*证书校验方式必填！*/
							alert(getJsLocaleMessage("txgl","txgl_js_mwedithttps_5"));
							$("#subBut").attr("disabled", false);
						}
			});
		}
	});
});




function switchverifypeer(obj) {
	if(obj == 1) {
		$("#gstype").val("0");
		$("#verifyhost").val("1");
		switchgstype(0);
		$("#gstype").attr("disabled", true);
		$("#verifyhost").attr("disabled", true);
	} else {
		var gstype = $("#gstype option:selected").val();
		switchgstype(gstype);
		$("#gstype").attr("disabled", false);
		$("#verifyhost").attr("disabled", false);
	}
}

function switchgstype(objs) {
	if(objs == 0) {
		$("#cacertname").val("");
		$("#cacertname").attr("disabled", true);
	} else {
		$("#cacertname").attr("disabled", false);
	}
}
