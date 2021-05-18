$(document).ready(function() {
	getLoginInfo("#getloginUser");
	//$("#spdate").html(currtime());
	var lgcorpcode = $("#lgcorpcode").val();
	
	$("#addAccessibleUrl").click(function(){
		var $this = $(this);
		dlog = art.dialog.through({
			title: getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_24"),
		    content: document.getElementById('divBox'),
		    id: 'divBox',
		    width: "720px",
			height: "450px",
		    padding:0,
		    lock: true,
		    ok:function(){
				var iframe = $("#iframe1")
		        var form = iframe.find('#pageForm');
				//window.console.log(form)
		        var checked = $(form).find("input[name='checklist']:checked");
		        if(checked&&checked.size()>0){
		        	var sid = checked.val();
		        	var accessibleUrl = "{basePath}yxgl_lotteryMgr.hts?method=tolotteryActivity&marId=" + sid + "&openid={openid}" + "&aid={aid}";
		        	$this.prev('input').val(accessibleUrl);
					return true;
		        }else{
		        	alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_25"));
		        	return false;
		        }
			},
			cancel: true
		});
	});
	$('#item-summary1').keyup(function(){
		var content = $(this).val();
		if(content != null && content.length > 100){
			content = content.substring(0,100);
		}
	    $('#summaryDiv').text(content);
	});
	
  });
  
var path=$("#path").val();
var basePath=$("#basePath").val();

function judeitemtitle(){
	  /*var itemIds="";
	  $('.msg-edit').find('.msg-edit-area').each(function(){
		  var idStr = $(this).attr('id');
		  itemIds +=(idStr.replace("msg-edit-area",'')+",");
	  });
	  var s = itemIds.split(",");
	  for(var i in s){
	  if(!isnullstr(s[i])){*/
	  if(isnullstr($("#item-title1").val())){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_26"));
		  return false;
	  }else{
			 if($("#item-title1").val().length>32){
				 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_27"));
				return false;
			 }
	 }
	  if(isnullstr($("#item-url1").val())){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_28"));
		  return false;
	  }
	  $('#item-content1').val(UM.getEditor('myEditor1').getContent());
	  if(!isnullstr($("#item-content1").val())){
		  if($("#item-content1").val().length>20000){
			  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_29"));
			  return false;
		  }	  
	  }
	  if(!isnullstr($("#item-link1").val())){
		  if($("#item-link1").val().length>120){
			  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_30"));
			  return false;
		  }else{
			  var pattern=/http(s)?:\/\/([\w-]+\.)+[\w-]+([\w-]*)?/;
			  if(!pattern.test($("#item-link1").val())){
				  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_31"));
				  return false;
			  }
			  
		  }	  
	  }
	  //}
	  //}
	  
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
		  $("#appmsgItem"+s[i]).find(".i-title").html(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_19"));
		  $("#appmsgItem"+s[i]).find("img.default-tip").attr("src","").hide();
		  
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
	                if(message == "oversize"){
	                	alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_32"));
	                	return false;
	                }
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
  