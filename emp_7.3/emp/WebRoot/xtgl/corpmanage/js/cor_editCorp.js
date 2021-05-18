function sub()
{			   
    var corpCode=$("#corpCode").val();   
    var corpName=$("#corpName").val();
    var depNam=$("#depName").val();
    if(corpCode==null||corpCode=="")
    {
	    //alert("请输入企业编码！");
	    alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrqybm"));
	    $("#corpCode").focus();
	    return;
    } 
	else if(corpCode.length != 6) 
	{
		//alert("企业编码为6位数字！");
		alert(getJsLocaleMessage("xtgl","xtgl_qygl_qybmw6wsz"));
		$("#corpCode").focus();
		return;
	}
	else 
	{
	    var pattern = /^[0-9]+$/;
	    if(!pattern.test(corpCode)) {
		    //alert("企业编码为6位数字！");
		    alert(getJsLocaleMessage("xtgl","xtgl_qygl_qybmw6wsz"));
		    $("#corpCode").focus();
		    return;
	    }
    }
    if(corpName==null||corpName=="")
    {
	    //alert("请输入企业名称！");
	    alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrqymc"));
	    $("#corpName").focus();
	    return;
    }

    if(depNam==""){
	    //alert("请输入企业简称！");
	    alert(getJsLocaleMessage("xtgl","xtgl_qygl_qsrqyjc"));
	    $("#depName").focus();
	    return;
    }
   
    var regu ="^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT)$"
    var re = new RegExp(regu);
    if ($("#email").val().search(re) == -1 && $("#email").val()!="")
    {
        //window.alert ("请输入有效合法的E-mail地址 ！")
        window.alert (getJsLocaleMessage("xtgl","xtgl_qygl_qsryxhfdemaildz"))
        return false;
    }   
    if($("#action").val()=="add"){
        $("#button").attr("disabled","true");
        $.post('cor_manager.htm?method=check',
        {  
        corpCode:corpCode,
        corpName:corpName  
        },function(result){
	        if(result=="corpCode")
	        {
	           //alert("已存在相同的企业编码！");
	           alert(getJsLocaleMessage("xtgl","xtgl_qygl_yczxtdqybm"));
	           $("#button").attr("disabled","");
	           return ;
	        }
	        else if(result=="corpName")
	        {
	           //alert("已存在相同的企业名称！");
	           alert(getJsLocaleMessage("xtgl","xtgl_qygl_yczxtdqymc"));
	           $("#button").attr("disabled","");
	           return;
	        }
	        else
	        {
	        	$("#pageForm").submit();
	        }
        }); 
    }else 
    {
    	$("#button").attr("disabled","true");
        $("#pageForm").submit();
    }
}
function doUplaod()
{
	var path=$("#path").val();
	var imgs = $.trim($("#fileInput").val());
	if (imgs == "")
	{
		//alert("请选择要上传的图片文件！");
		alert(getJsLocaleMessage("xtgl","xtgl_qygl_qxvyscdtpwj"));
		
	}
	else if (imgs != "" && !(/^(jpg|png|jpeg|gif)$/.test(imgs.toString().substring((imgs.toString().lastIndexOf("."))+1))))
    {
        //alert("不支持的图片格式！");
        alert(getJsLocaleMessage("xtgl","xtgl_qygl_bzcdtpgs"));
        
    }else
    {
     	$.ajaxFileUpload ({ 
		    url:'cor_manager.htm?method=upload', //处理上传文件的服务端 
		    secureuri:false, //是否启用安全提交，默认为false
		    fileElementId:"fileInput", //与页面处理代码中file相对应的ID值
		    dataType: 'text', //返回数据类型:text，xml，json，html,scritp,jsonp五种
		    success: function (data) { 
			 	if(data != null )
			   {
					   if(data == "overSize")
					   {
						   //alert("文件大小超过50K，请重新选择文件上传！");
						   alert(getJsLocaleMessage("xtgl","xtgl_qygl_wjdxcg50k"));
						   return;
					   }else if (data == "error")
					   {
						   //alert("上传失败！");
						   alert(getJsLocaleMessage("xtgl","xtgl_qygl_scsb"));
						   return;
					   }
							   
			    	 $("#fileName").val(data);
			   	 $("#logo").css("background-image","url("+path+"/website/sglcorpFrame/images/logo/"+data+")");
			    }
		  	} 
		});
    }
 }