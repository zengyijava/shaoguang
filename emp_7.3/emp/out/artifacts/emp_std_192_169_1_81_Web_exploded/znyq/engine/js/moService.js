// 检查业务名称
function serOk() {
	var pathUrl = $("#pathUrl").val();
	var serName = $("#serName").val(); // 获取业务名称
	if($.trim(serName) == "") {      //检查步骤名称
		   //alert("请填写业务名称！");
		   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qtxywmc"));
		   return;
	}
	//if(!controlInput(serName,"业务名称只能由汉字、英文字母、数字、下划线组成！",true)){
	if(!controlInput(serName,getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmczyhz"),true)){
		return;
	}
	serName = $.trim(serName);
	var comments = $("#comments").val(); // 获取业务描述
	comments = $.trim(comments); // 清除业务描述中所有空格
	var orderCode = $("#orderCode").val(); // 从表单中获取账户密码的值
	var hserName = $("#hserName").val();
	var curCode = $("#curCode").val();
	var curIdentifyMode = $("#curIdentifyMode").val();
	var spUser = $("#spUser").val();
	//尾号状态，1启用，2停用
	var identifyMode = $('input[name="identifyMode"]:checked').val();
	//启用尾号，则要验证通道长度
	if(identifyMode == "1"){
		//验证通道号长度是否超过了
		var sendFlag = $("#sendFlag").val().split("&");
		if(sendFlag[0] == "false" && sendFlag[1] == "false" && sendFlag[2] == "false"){
			//alert("移动、联通、电信主通道号+拓展尾号位数超过20位，不能发送！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ydltdxztdh"));
			return ;
		}
		if(sendFlag[0] == "false" && sendFlag[1] == "false" ){
			//alert("移动、联通主通道号+拓展尾号位数超过20位，不能发送！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ydltztdh"));
			return ;
		}
		if(sendFlag[0] == "false" && sendFlag[2] == "false"){
			//alert("移动、电信主通道号+拓展尾号位数超过20位，不能发送！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yddxztdh"));
			return ;
		}
		if(sendFlag[1] == "false" && sendFlag[2] == "false"){
			//alert("联通、电信主通道号+拓展尾号位数超过20位，不能发送！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ltdxztdh"));
			return ;
		}
		if(sendFlag[0] == "false" ){
			//alert("移动主通道号+拓展尾号位数超过20位，不能发送！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ydztdh"));
			return ;
		}
		if(sendFlag[1] == "false"){
			//alert("联通主通道号+拓展尾号位数超过20位，不能发送！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ltztdh"));
			return ;
		}
		if(sendFlag[2] == "false"){
			//alert("电信主通道号+拓展尾号位数超过20位，不能发送！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxztdh"));
			return ;
		}
	}
	
	
	if($("#orderCode").val() == "") {      //检查步骤名称
		   //alert("请填写指令代码！");
		   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qtxzldm"));
		   return;
	}
	if ($.trim(serName) == "") { // 业务名称是否为空
		//alert("业务名称不能为空！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmcbnwk_gth"));
		return;
	} else if (serName.length > 20) { // 判断业务名称的长度是否大于限制的长度
		//alert("业务名称长度不能超过20！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmccdbncg20"));
		return;
	} else if (comments.length > 32) { // 判断业务描述的长度
		//alert("业务名称描述长度不能超过32！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ywmcmscdbncg32"));
		return;
	} else if ($.trim(orderCode) == "") { // 判断业务描述的长度
		//alert("指令代码不能为空！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zddmbnwk"));
		return;
	} else if ($.trim(spUser) == "") { // 判断业务描述的长度
		//alert("SP账号不能为空！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_spzhbnwk"));
		return;
	} else if (serName == hserName) { // 判断业务描述的长度
		isCheckCode(orderCode, curCode, curIdentifyMode, identifyMode);
	}
	else { // 判断业务名称是否存在
		var lgcorpcode = $("#lgcorpcode").val();
		$.post(pathUrl+"/eng_moService.htm?method=checkName",{serName:serName,lgcorpcode:lgcorpcode},function(result){
			if (result != null && "true" == result)
			{
				//alert("该业务名称已经存在！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_gywmcyjcz"));
				return;
			}
			else 
			{
				isCheckCode(orderCode, curCode, curIdentifyMode, identifyMode);
			}
		});
	}
}

function isCheckCode(orderCode, curCode, curIdentifyMode, identifyMode){
    var fromModify = $('#fromModify').val();
	if(orderCode != curCode){
		//指令一改，必须验证
		checkCode(orderCode,"#serForm");
	}

	//指令没改，尾号状态改，只验证禁用尾号情况且原来是启用尾号的情况
	else if(orderCode == curCode && curIdentifyMode == 1 && identifyMode == 2 && fromModify == undefined){

		checkCode(orderCode,"#serForm");
	}
	else{
		if($("#workType").val()=="waterLine")
		{
			
			$("#serForm").attr("action",$("#serForm").attr("action")+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$("#lgcorpcode").val());
			$("#serForm").submit();
			
		}else
		{
			//if (confirm("确定提交吗？")) {
			if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
				//$("#serForm").attr("action",$("#serForm").attr("action")+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$("#lgcorpcode").val());
				$("#serForm").submit();
			}
		}
	}
}

function checkCode(orderCode,form)
{
	var pathUrl = $("#pathUrl").val();
	//var lgcorpcode = $("#lgcorpcode").val();
	var orderType = $("input[name='orderType']:checked").val();
	var msgSeparated = $("#msgSeparated").val();
	var identifyMode = $("input[name='identifyMode']:checked").val();
	//var curIdentifyMode = $("#curIdentifyMode").val();
	var curCode = $("#curCode").val();
	var curCodeUrl ='';
	if(curCode != null && curCode.length > 0)
	{
		curCodeUrl = "&curCode="+curCode;
	}
	var strUrl = "/eng_moService.htm?method=checkOrderCode&orderCode="+orderCode+"&orderType="+orderType+"&msgSeparated="+msgSeparated+"&identifyMode="+identifyMode+curCodeUrl;
	$.post(pathUrl+strUrl,function(result){
		if (result != null && result == "true")
		{
			//alert("该指令代码已经存在！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_gzldmyjcz"));
		}
		else
		{
			//$.post('common.htm?method=frontLog',{
			//		info:"上行业务，检查指令，提交参数。method=checkOrderCode&orderCode="+orderCode+"&orderType="+orderType+"&msgSeparated="+msgSeparated+"&identifyMode="+identifyMode+curCodeUrl
			//	});
			$.post('common.htm?method=frontLog',{
				info:getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sxyw")+"method=checkOrderCode&orderCode="+orderCode+"&orderType="+orderType+"&msgSeparated="+msgSeparated+"&identifyMode="+identifyMode+curCodeUrl
			});
			if($("#workType").val()=="waterLine")
			{
				$(form).submit();
			}else
			{
			//if (confirm("确定提交吗？")) {
			if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
				$(form).submit();
			}
			}
		}
	})	
}

function deleteSer(serId,serName,runState) {
	var pathUrl = $("#pathUrl").val();
	var msg;
	if(runState=="1")
	{
		//msg="该业务处于运行状态，请确定是否删除？";	
		msg=getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_gywcyyxztqqdsfsc");
	}else
	{
		//msg="确定删除选中的上行短信业务吗？";
		msg=getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdscxzdsxdxywm");
	}
	if (serId == "") {
		//alert("缺少上行业务ID参数！")
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qssxywidcs"))
	} 
	else if (confirm(msg)) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		$.post(pathUrl+"/eng_moService.htm?method=delete",{serId:serId,serName:serName},function(result){
			if (result== "true") 
			{
				//alert("删除成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sccg"));
				$("#pageForm").submit();
			}
			else 
			{
				//alert("删除失败！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_scsb"));
			}});
    }
}

function processOk()
{
	var pathUrl = $("#pathUrl").val();
	 var prName = $("#prName").val();         //步骤名称
	 var hprName = $("#hprName").val();
	   var orderCode=$('#orderCode').val();
	   prName = $.trim(prName);         // 清除所有空格
	   orderCode = $.trim(orderCode);
	   var curCode = $("#curCode").val();
	   if(prName == "") {      //检查步骤名称
		   //alert("步骤名称不能为空！");
		   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcbnwk_gth"));
		   return;
       }
	   else if(!prName.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]+$/))
		{
		   //alert("步骤名称只能由汉字、英文字母、数字、下划线组成！");
		   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcznyhz_gth"));
		   return;
		}
	   else if(orderCode != "" && /(\$|#|\*|&|\+|@| )+/.test(orderCode))
		{
			//alert("指令代码不能包含短信分隔符（$,#,*,+,@, ），请重新输入！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zldmbnbhdxfgf"));
			return;
		}
	   else if (prName == hprName) { // 判断业务描述的长度
			if (orderCode == curCode)
			{
				//if (confirm("确定提交吗？")) {
				if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
					$("#proform").attr("action",$("#proform").attr("action")+"&lgcorpcode="+$('#lgcorpcode').val());
					$("#proform").submit();
				}
			}
			else
			{
				$("#proform").submit();
			}
		}
	   else if (orderCode == curCode)
		{
			//if (confirm("确定提交吗？")) {
			if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
				$("#proform").submit();
			}
			return;
		}
		else
		{
			$("#proform").submit();
		}
}

//流水线调用这个方法
function processOk2()
{
	 var pathUrl = $("#pathUrl").val();
	 var prName = $("#prName").val();         //步骤名称
	 var hprName = $("#hprName").val();
	   var orderCode=$('#orderCode').val();
	   prName = $.trim(prName);         // 清除所有空格
	   orderCode = $.trim(orderCode);
	   var curCode = $("#curCode").val();
	   if(prName=="")
	   {
		   //alert("请填写步骤名称！");
		   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qtxbzmc"));
		   return;
	   }
	   else if(!prName.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]+$/))
		{
		   //alert("步骤名称只能由汉字、英文字母、数字、下划线组成！");
		   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcznyhz"));
		   return;
		}
	   else if(orderCode != "" && /(\$|#|\*|&|\+|@| )+/.test(orderCode))
		{
			//alert("指令代码不能包含短信分隔符（$,#,*,+,@, ），请重新输入！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zldmbnbhdxfgf"));
			return;
		}
	   else{ 
			//$("#proform").attr("action",$("#proform").attr("action")+"&lgcorpcode="+$('#lgcorpcode').val());
			$("#proform").submit();
	   }
	  
}
function getStaffname() {
	$("#spUser").load(
			"sms_sendSMS.htm?method=getBindUserId",
			{busCode:$("#busCode").val()}
	);
}

function delPro(id,name)
{
	var pathUrl = $("#pathUrl").val();
	var lgcorpcode = $('#lgcorpcode').val();
	var lguserid = $('#lgcorpcode').val();
	//if (confirm("确定删除该步骤？")) {
	if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdscgbz"))) {
		$.post(pathUrl+"/eng_moService.htm?method=delPro",{prId:id,prName:name,lgcorpcode:lgcorpcode,lguserid:lguserid},function(result){
			if (result== "true") 
			{
				//alert("删除成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sccg"));
				location.href = location;
			}
			else 
			{
				//alert("删除失败！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_scsb"));
			}});
    }
}
