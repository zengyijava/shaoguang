/*公众帐号管理-新增/修改*/
$(function() {
	$('#subBut').click(function() {
		var pathUrl = $("#pathUrl").val();
		var url="";
		$('#subBut').attr("disabled",true);
		var acctType=$("#acctType  option:selected").val();
		var acctName = $.trim($('#acctName').val());
		var acctCode = $.trim($('#acctCode').val());
		var acctOpenId = $.trim($('#acctOpenId').val());
		var acctInfo = $.trim($('#acctInfo').val());
		var OpType = $.trim($('#OpType').val());
		var acctImg = $.trim($('#acctImg').val());
		
		
        //公众帐号类型
		if (acctType==null||acctType == "") {
			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_1"));
			$('#subBut').attr("disabled",false);
			return;
		}
        //帐号名称
		if (acctName==null||acctName == "") {
			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_2"));
			$('#subBut').attr("disabled",false);
			return;
		}
        if (acctName.length > 20) {
            alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_3"));
            $('#subBut').attr("disabled",false);
            return;
        }
		//微信帐号
        if (acctCode==null||acctCode == "") {
			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_4"));
			$('#subBut').attr("disabled",false);
			return;
		}
        if(!/^[a-zA-Z0-9]\w+$/.test(acctCode)){
			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_5"));
			$('#subBut').attr("disabled",false);
			return;
		}
        if (acctCode.length < 6) {
            alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_6"));
            $('#subBut').attr("disabled",false);
            return;
        }
        if (acctCode.length > 20) {
            alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_7"));
            $('#subBut').attr("disabled",false);
            return;
        }

        if(acctImg!=null&&acctImg!=''){
        	  var c = ".jpg|.jpeg|.gif|.bmp|.png|";
        	  var b = acctImg.substring(acctImg.lastIndexOf("."),acctImg.length).toLowerCase();
	          if(c.indexOf(b)<0){
	            alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_8"));
	            $("#subBut").attr("disabled",false);
	            return;
		      }
        }
        
        
        if("add"==OpType){
        	//原始帐号
            if (acctOpenId==null||acctOpenId == "") {
    			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_9"));
    			$('#subBut').attr("disabled",false);
    			return;
    		}
            if (acctOpenId.length != 15) {
 
    			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_10"));
    			$('#subBut').attr("disabled",false);
    			return;
    		}
            	
            if(!/^gh_([0-9]|[a-z]){12}$/.test(acctOpenId)){
    			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_11"));
    			$('#subBut').attr("disabled",false);
    			return;
    		}
            
         	//介绍文字
            if (acctInfo==null||acctInfo == "") {
    			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_12"));
    			$('#subBut').attr("disabled",false);
    			return;
    		}
            if (acctInfo.length > 215) {
                alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_13"));
                $('#subBut').attr("disabled",false);
                return;
            }
            
            $.post(pathUrl+"/cwc_acctmanager.htm",{
            	    method: "doCheck",
            		acctOpenId: acctOpenId,
            		type: $('#OpType').val(),
            		corpCode: $("#lgcorpcode").val()
            	},function(message){
	            	if(message.indexOf("@")==-1){
						alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_14"));
						$('#subBut').attr("disabled",false);
						return;
					}
					message=message.substr(message.indexOf("@")+1);
					if (message != "") {
						if (message == "repeat") {
							alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_15"));
							$('#subBut').attr("disabled",false);
						}else
						{
							alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_16"));
							$('#subBut').attr("disabled",false);
						}
					} else {
						$("#acctform").attr("action",$("#acctform").attr("action")+"&lgcorpcode="+$("#lgcorpcode").val());
						document.forms["acctform"].submit();
					}
            });
        }else if ("edit"==OpType){
         	//介绍文字
            if (acctInfo==null||acctInfo == "") {
    			alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_12"));
    			$('#subBut').attr("disabled",false);
    			return;
    		}
            if (acctInfo.length > 215) {
                alert(getJsLocaleMessage("weix","weix_qywx_gzzhgl_text_13"));
                $('#subBut').attr("disabled",false);
                return;
            }
            
        	$("#acctform").attr("action",$("#acctform").attr("action")+"&lgcorpcode="+$("#lgcorpcode").val());
			document.forms["acctform"].submit();
        }
	});
	
	$("#acctName").bind('keyup blur',function(){
		secapp(this,/[<'">]/g);
	})
});

//去掉匹配的字符
function secapp(obj,reg){
	var val=$(obj).val();
	if(reg.test(val)){
		$(obj).val($(obj).val().replace(reg,''));
		
	}
}