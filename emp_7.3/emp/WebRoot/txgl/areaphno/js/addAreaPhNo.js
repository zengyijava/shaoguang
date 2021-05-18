/**
 * 非空判断
 * return 非空：false 空：true
 */
function isBrank(obj){
	if(obj == "" ||  obj == undefined || obj.toString().trim() == ""){
		return true;
	}
	return false;
}
    $(document).ready(function() {
    	var pathUrl = $("#pathUrl").val();
    	var index=1;
    	var url=pathUrl+"/seg_areaPhoneNo.htm";
    	var opType = $("#opType").val();
    	var numberCheck=/^[0-9]*[1-9][0-9]*$/;//正整数 ;
    	$('#subBut').click(function() {
    		   var mobile = $("#mobile").val();
    		   // var areaCode = $("#areaCode").val();
    		   var province = $("#province").val();
    		   // var servePro = $("#servePro").val();
    		   var city = $("#city").val();
    		   if(isBrank(mobile)){
    		     // alert("号段不能为空！");
    			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_5"));
    		      $("#mobile").focus();
    		   }else if(!numberCheck.exec(mobile)){
    			  // alert("号段只能是正整数！");
    			   alert("号段只能是正整数");
     		      $("#mobile").focus();
    		   }else if(mobile.length>7){
    		      //alert("号段长度不能大于7！");
    			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_14"));
                   $("#mobile").focus();
    		   }else if(isBrank(province)){
    		      //alert("省份不能为空！");
    			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_8"));
    		      $("#province").focus();
    		   // }else if(isBrank(servePro)){
    		   //    //alert("运营商能为空！");
    			//    alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_9"));
    		   //    $("#servePro").focus();
    		   }else if(isBrank(city)){
    			  // alert("城市不能为空！");
    			   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_10"));
    			   $("#city").focus();
    		   }else{
    		    	  $.post(url,{
    		    		  method:opType,
    		    		  mobile:mobile,
    		    		  // areaCode:areaCode,
    				      province:province,
    				      // servePro:servePro,
    				      city:city
//    				      ,tructtype:tructtype
    				     }, function(result) {
    				
    						if (result=="true") 
    						{
								//alert("添加成功！");
    							 alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_11"));
    							window.parent.location.href=pathUrl+"/seg_areaPhoneNo.htm";

    						}
    						else if(result=="false"){
								//alert("添加失败！");
    							alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_12"));
    						} 
    						else if(result=="isExit"){
    							//alert("添加失败，号段已存在！");
    							alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_13"));
    						}
    			});
    		}
    	});
    });
    