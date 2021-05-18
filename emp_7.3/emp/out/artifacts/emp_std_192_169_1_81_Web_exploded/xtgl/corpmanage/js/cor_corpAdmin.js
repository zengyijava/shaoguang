var chPwdStatus = false;
function checkData(){
   var username=$("#username").val();
   var name=$("#name").val();
   var newPwd=$("#newPwd").val();
   var newPwd2=$("#newPwd2").val();
   var corpcode=$("#corpCode").val();
   var mobile=$.trim($("#mobile").val());
    if(name==null||name==""){
      //alert("请输入管理员名称");
      alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrglymc"));
      return;
   }
   if(chPwdStatus) {
 	   if(newPwd==null||newPwd==""){
	      //alert("请输入登录密码");
	      alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrdlmm"));
	      return;
	   }
	   if(newPwd2==null||newPwd2==""){
	      //alert("请确认登录密码");
	      alert(getJsLocaleMessage("xtgl","xtgl_qygl_qqrdlmm"));
	      return;
	   }
	   if(newPwd!=newPwd2){
	      //alert("两次输入的密码不一致");
	      alert(getJsLocaleMessage("xtgl","xtgl_qygl_lcsrdmmbyz"));
	      return;
	   }
   }
   if (mobile == "")
   {
	   //alert("手机不能为空！");
	   alert(getJsLocaleMessage("xtgl","xtgl_qygl_sjbnwk"));
	   $("#mobile").focus();
		  return;
   }
   else if (mobile!="" && mobile.length != 11)
   {
	   //alert("手机号码不正确！");
	   alert(getJsLocaleMessage("xtgl","xtgl_qygl_sjhmbzq"));
	   $("#mobile").focus();
		  return;
   }
   $("#button").attr("disabled","true");
      	$("#pageForm").submit();
}
function checkPwd(t) {
	var newPwd = $("#newPwd").attr("value");
	var newPwd2 = $("#newPwd2").attr("value");
	if(chPwdStatus){
		if(t == 1) {
			if(newPwd==null||newPwd==""){
			      //alert("请输入登录密码");
			      alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrdlmm"));
			      return;
			}
		} else if(t == 2) {
			if(newPwd2==null||newPwd2==""){
			    //alert("请确认登录密码");
			    alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrdlmm"));
			    return;
			}
		    if(newPwd!=newPwd2){
		      //alert("两次输入的密码不一致");
		      alert(getJsLocaleMessage("xtgl","xtgl_qygl_lcsrdmmbyz"));
		      return;
		    }
		}
	}
}
function resetPwd(v)
{
	//if("重置密码" == v)
	if(getJsLocaleMessage("xtgl","xtgl_qygl_czmm") == v)
	{
		chPwdStatus = true;
		//$("#rePwd").attr("value","取消重置");
		$("#rePwd").attr("value",getJsLocaleMessage("xtgl","xtgl_qygl_qxcz"));
		$('#newPwd').css("background","#FFFFFF");
		$("#newPwd").attr("disabled",false);+
		$('#newPwd').focus();
		$('#newPwd2').css("background","#FFFFFF");
		$("#newPwd2").attr("disabled",false);
	}else
	{
		chPwdStatus = false;
		//$("#rePwd").attr("value","重置密码");
		$("#rePwd").attr("value",getJsLocaleMessage("xtgl","xtgl_qygl_czmm"));
		$('#newPwd').css("background","#C0C0C0");
		$("#newPwd").attr("disabled",true);
		$('#newPwd').attr("value","");
		$('#newPwd2').css("background","#C0C0C0");
		$("#newPwd2").attr("disabled",true);
		$('#newPwd2').attr("value","");
	}
}