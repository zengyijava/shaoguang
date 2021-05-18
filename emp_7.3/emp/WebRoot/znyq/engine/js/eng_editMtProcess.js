
function setSelect(){
	   var prType = $("#select option:selected").val(); //获取选中的值
	   initShow();          //  初始化显示单选按钮
	  if(prType == 4){      //判断如果选择prType的值为4,就隐藏ID为yes的radio表单和他的值
		     $("#select").parent().find("span").remove();
	    	 $("#no").attr("checked","checked");   //设置选中radio
	    	 $("#yes").hide();
	    	 $("#y").hide();
    	}
	 else if(prType == 5){   //判断如果选择prType的值为5,就隐藏ID为no的radio表单和他的值
		    $("#select").parent().find("span").remove();
	    	$("#yes").attr("checked","checked");   //设置选中radio
   	    	    $("#no").hide();
   	    	    $("#n").hide();
    	 }
}
    //表单验证
	function checkSubBefore(){  
		
		var prName = $("#prName").val();         //步骤名称
		var prType = $("#prType").val();
		prName = prName.replace(/\s+/g,"");         // 清除所有空格
		if(prName == "") {      //检查步骤名称
			$("#errInfoMsg").find("span").remove();
			//var msg = $("<span>").css("color", "red").text("步骤名称不能为空,请输入步骤名称");
			var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcbnwk"));
			$("#errInfoMsg").append(msg);
			return false;
		}
		if(!prName.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]+$/))
		{
			$("#errInfoMsg").find("span").remove();
			//var msg = $("<span>").css("color", "red").text("步骤名称只能由汉字、英文字母、数字、下划线组成");
			var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcznyhz"));
			$("#errInfoMsg").append(msg);
			return false;
		}
		if(prType == 4)
		{
			var dbId = $("#dbId").val();
			if (dbId == "" || dbId == null) {
				$("#errInfoMsg").find("span").remove();
				//var msg = $("<span>").css("color", "red").text("数据库连接不能为空");
				var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkljbnwk"));
				$("#errInfoMsg").append(msg);
				return false;
			}
			var sql = $("#sql").val();
			if (sql == "" || sql == null) {
				$("#errInfoMsg").find("span").remove();
				//var msg = $("<span>").css("color", "red").text("SQL语句不能为空");
				var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sqlyjbnwk"));
				$("#errInfoMsg").append(msg);
				return false;
			}
		}
		else if(prType == 5)
		{
			var msgMain = $("#msgMain").val();
			if (msgMain == "" || msgMain == null) {
				$("#errInfoMsg").find("span").remove();
				//var msg = $("<span>").css("color", "red").text("发送内容不能为空");
				var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrbnwk"));
				$("#errInfoMsg").append(msg);
				return false;
			}
			var len = msgMain.length;
	        if(len > smsContentMaxLen)
	        {
	        	//alert("发送内容长度大于短信最大长度限制，最大长度限制为："+smsContentMaxLen);
	        	alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrcddydxzdcdxz")+smsContentMaxLen);
				return false;
	        }
			if(smsContentMaxLen == 700)
			{
				if(!checkSmsContentLen(msgMain,smsContentMaxLen))
				{
					return false;
				}
			}
		}

       	//if(confirm("确定提交吗?")) {
        if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
        	
   	        $(window.parent.document).find("#editok").attr("disabled","disabled");
   	        //var msg = $("<span>").css("color", "green").text("正在提交中,请稍候.........");
			//$("#previous").parent().append(msg);
			
			if(prType == 5)
			{
				var repedMsg = getContentValX($("#msgMain"));
				$("#msgMain").val(repedMsg);
				$('#proform').attr("action",$('#proform').attr('action')+'?method=editReply'+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val());
				
			}
			else if(prType == 4)
			{
				$('#proform').attr("action",$('#proform').attr('action')+'?method=edit'+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val());
				
			}
			$("#proform").submit();
		}         	  
	}
	
    //添加验证提示信息
	function addprNameMark() {
		var prName = $("#prName").val();
		prName = prName.replace(/\s+/g,"");         // 清除所有空格
		if(prName == "") {
   	   		$("#errInfoMsg").find("span").remove();
			//var msg = $("<span>").css("color", "red").text("步骤名称不能为空,请输入步骤名称");
			var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcbnwk"));
			$("#errInfoMsg").append(msg);
			return false;
		}
		var dbId = $("#dbId").val();	
		if (dbId == "" || dbId == null) {
			$("#errInfoMsg").find("span").remove();
			//var msg = $("<span>").css("color", "red").text("数据库连接不能为空");
			var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkljbnwk"));
			$("#errInfoMsg").append(msg);
			return false;
		}
		$("#errInfoMsg").find("span").remove();
	}
               
     //  初始化显示单选按钮
     function initShow(){ 
		$("#no").show();
 		$("#n").show();
		$("#yes").show();
  		$("#y").show();
     }

     //删除步骤名称提示信息
	function removeprNameMark(){  
		$("#prName").parent().find("span").remove();
	}
           
    //获取sql模板内容
	function getSql(sqlId) {
		var $sqlContent = $("form[name='proform']").find("textarea[name='sql']");
		if(sqlId != "")
		{
			$.post("tem_smsTemplate.htm",{method:"getTmMsg",tmId:sqlId},function(msg)
				{
					$("#sql").val(msg);
				}
			);
		}else
		{
			$("#sql").val("");
			//location.href=location.href;
			//$sqlContent.val("");
		}
	}
    
    //获取短信模板内容
	function getSmsTempl(tmId) {
		var $sqlContent = $("form[name='proform']").find("textarea[name='msgMain']");
		if(tmId != "")
		{
			$.post("tem_smsTemplate.htm",{method:"getTmMsg",tmId:tmId},function(msg)
				{
					$("#msgMain").val(msg);
				}
			);
		}else
		{
			$("#msgMain").val("");
			//location.href=location.href;
			//$sqlContent.val("");
		}
	}