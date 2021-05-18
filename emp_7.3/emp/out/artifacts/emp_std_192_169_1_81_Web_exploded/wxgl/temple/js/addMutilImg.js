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
	  if(s.length<3){
	      alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_8"));
		  return false;
	  }
	  for(var i in s){
		  if(!isnullstr(s[i])){
			  if(isnullstr($("#item-title"+s[i]).val())){
			      alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_9")+s[i]+getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_10"));
				  return false;
			  }else{
				 if($("#item-title"+s[i]).val().length>32){
				     alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_11")+s[i]+getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_12"));
					return false;
				 }
			 }
			  if(isnullstr($("#item-url"+s[i]).val())){
			      alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_13")+s[i]+getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_14"));
				  return false;
			  }
			  $('#item-content'+s[i]).val(UM.getEditor('myEditor'+s[i]).getContent());
			  if(!isnullstr($("#item-content"+s[i]).val())){
				  if($("#item-content"+s[i]).val().length>20000){
				      alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_16")+s[i]+getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_15"));
					  return false;
				  }	  
			  }
			  if(!isnullstr($("#item-link"+s[i]).val())){
				  if($("#item-link"+s[i]).val().length>120){
				      alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_16")+s[i]+getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_17"));
					  return false;
				  }	else{
					  var pattern=/http(s)?:\/\/([\w-]+\.)+[\w-]+([\w-]*)?/;
					  if(!pattern.test($("#item-link"+s[i]).val())){
					      alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_16")+s[i]+getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_18"));
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
		  
    	   if(s[i] == 1){

			    $("#appmsgItem1").find(".default-tip").show();
			    $("#appmsgItem1").find(".i-title").html(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_19"));
			    $("#appmsgItem1").find("img.default-tip").attr({"src":""}).hide();
			}else{
				 $("#appmsgItem"+s[i]).find(".default-tip").show();
				  $("#appmsgItem"+s[i]).find(".i-title").html(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_19"));
				  $("#appmsgItem"+s[i]).find("img.default-tip").attr("src","").hide();
			}
		  
	  }
  }
  function uploadImage(obj) {
    	var currentareaId = $(obj).parents(".msg-edit-area").attr('id');
    	var datarid = currentareaId.replace("msg-edit-area","");
    	var imgtem = $("#item-img"+datarid).val();
    	var filePath = $("#filePath").val();
    	if(imgtem.length<1){
    	    alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_20"));
	    	return ;	
	    }else{
	    	var c = ".jpg|.jpeg|.gif|.bmp|.png|";
	        var b = imgtem.substring(imgtem.lastIndexOf("."),imgtem.length);
            if(c.indexOf(b)<0){
                alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_21"));
	            return;
	        }
            
		}
        $.ajaxFileUpload({
            url: path+"/weix_keywordReply.htm?method=uploadImg&datarid="+datarid, //需要链接到服务器地址
            secureuri: false,
            dataType: "html",
            fileElementId: "item-img"+datarid,                            //文件选择框的id属性
            //服务器返回的格式，可以是json
            success: function (data, textStatus) {               //相当于java中try语句块的用法
                if('success'==textStatus){
                	var message = "";
                	try{
                		message =$(data).text();
                	}catch(e){
                		message = data;
                	}
	                message=message.substr(message.indexOf("@")+1);
	                var imgurl = message.split(",")[0].split(':')[1];
	                var fieldName = message.split(",")[1].split(':')[1];
	                var datarid = fieldName.replace('item-img','');
				    if(datarid == "1"){
				    	$("#appmsgItem1").find(".default-tip").hide();
					    $("#appmsgItem1").find("img.default-tip").attr({"src":imgurl,height:160,width:315}).show();
					    $("#item-url1").val(imgurl);
					}else{
						$("#appmsgItem"+datarid).find(".default-tip").hide();
					    $("#appmsgItem"+datarid).find("img.default-tip").attr({"src":imgurl,height:72,width:72}).show();
					    $("#item-url"+datarid).val(imgurl);
					}
				    alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_22"));
	            }else{
	            	alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_23"));
		        }
            },
            error: function (data, status, e) {           //相当于java中catch语句块的用法
            	alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_23"));
               // $('#imgPath').val('');
            }
        });
        $(obj).parent().siblings("#txt").val('');
    }
  function back()
  {
	  history.go(-1);
  }
