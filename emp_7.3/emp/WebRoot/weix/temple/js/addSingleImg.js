$(document).ready(function() {
	getLoginInfo("#getloginUser");
	//$("#spdate").html(currtime());
  });
var path=$("#path").val();
var basePath=$("#basePath").val();

function judeitemtitle(){
	  var itemIds="";
	  $('.msg-edit').find('.msg-edit-area').each(function(){
		  var idStr = $(this).attr('id');
		  itemIds +=(idStr.replace("msg-edit-area",'')+",");
	  });
	  var s = itemIds.split(",");
	  for(var i in s){
		  if(!isnullstr(s[i])){
			  if(isnullstr($("#item-title"+s[i]).val())){
				  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_25"));
				  return false;
			  }else{
					 if($("#item-title"+s[i]).val().length>32){
						 alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_26"));
						return false;
					 }
			 }
			  if(isnullstr($("#item-url"+s[i]).val())){
				  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_27"));
				  return false;
			  }
			  if(!isnullstr($("#item-content"+s[i]).val())){
				  if($("#item-content"+s[i]).val().length>1000){
					  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_28"));
					  return false;
				  }	  
			  }
			  if(!isnullstr($("#item-link"+s[i]).val())){
				  if($("#item-link"+s[i]).val().length>50){
					  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_29"));
					  return false;
				  }else{
					  var pattern=/http(s)?:\/\/([\w-]+\.)+[\w-]+([\w-]*)?/;
					  if(!pattern.test($("#item-link"+s[i]).val())){
						  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_30"));
						  return false;
					  }
					  
				  }	  
			  }
		  }
	  }
	  return true;
  }
  function clearitemtitle(){
	  var itemIds="";
	  $('.msg-edit').find('.msg-edit-area').each(function(){
		  var idStr = $(this).attr('id');
		  itemIds +=(idStr.replace("msg-edit-area",'')+",");
	  });
	  var s = itemIds.split(",");
	  for(var i in s){
		  $("#appmsgItem"+s[i]).find(".default-tip").show();
		  $("#appmsgItem"+s[i]).find(".i-title").html(getJsLocaleMessage("weix","weix_qywx_hfgl_text_19"));
		  $("#appmsgItem"+s[i]).find("img.default-tip").attr("src","").hide();
		  
	  }
  }
  function uploadImage(obj) {
    	var currentareaId = $(obj).parents(".msg-edit-area").attr('id');
    	var datarid = currentareaId.replace("msg-edit-area","");
    	var imgtem = $("#item-img"+datarid).val();
    	var filePath = $("#filePath").val();
    	if(imgtem.length<1){
	    	alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_27"));
	    	return ;	
	    }else{ 
            if(imgtem.split(".").length!=2){
            	alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_21"));
	            return;	
            }
	    	var c = ".jpg|.jpeg|.gif|.bmp|.png|";
	        var b = imgtem.substring(imgtem.lastIndexOf("."),imgtem.length).toLowerCase();
            if(c.indexOf(b)<0){
	            alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_22"));
	            return;
	        }
		}
	    
        if(true) {
            $.ajaxFileUpload({
                url: path+"/cwc_replymanger.htm?method=uploadImg&datarid="+datarid, //需要链接到服务器地址
                secureuri: false,
                fileElementId: "item-img"+datarid,                            //文件选择框的id属性
                //服务器返回的格式，可以是json
                success: function (data, textStatus) {               //相当于java中try语句块的用法
	                if('success'==textStatus){
		                var message = $(data).text();
		                message=message.substr(message.indexOf("@")+1);
		                
		                var imgurl = message.split(",")[0].split(':')[1];
		                var fieldName = message.split(",")[1].split(':')[1];
		                var datarid = fieldName.replace('item-img','');
					    if(datarid == "1"){
					    	$("#appmsgItem1").find(".default-tip").hide();
						    $("#appmsgItem1").find("img.default-tip").attr({"src":filePath+imgurl,height:160,width:315}).show();
						    $("#item-url1").val(imgurl);
						}else{
							$("#appmsgItem"+datarid).find(".default-tip").hide();
						    $("#appmsgItem"+datarid).find("img.default-tip").attr({"src":filePath+imgurl,height:72,width:72}).show();
						    $("#item-url"+datarid).val(imgurl);
						}
	                	alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_23"));
		            }else{
		            	alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_24"));
			        }
                },
                error: function (data, status, e) {           //相当于java中catch语句块的用法
                	alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_24"));
                   // $('#imgPath').val('');
                }
            });
        }
        $(obj).parent().siblings("#txt").val('');
    }
  function back()
  {
	  history.go(-1);
  }
