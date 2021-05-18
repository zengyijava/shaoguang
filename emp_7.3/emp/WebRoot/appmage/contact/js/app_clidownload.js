	function upload(id){
			var imgtem=$("#"+id).val();
			if(imgtem.length<1){
				return ;	
			}else{
				var c = ".jpg|.jpeg|.gif|.bmp|.png|";
			    var b = imgtem.substring(imgtem.lastIndexOf("."),imgtem.length).toLowerCase();
			    if(c.indexOf(b)<0){
			        //alert("请上传.jpg、.jpeg、.gif、.bmp、.png类型的图片！");
					alert(getJsLocaleMessage('appmage','appmage_js_clidownload_uploadformat'));
			        resetfileinput('#'+id);
			        return;
			    }
			}
			var size=1024;
			var index=0;
			   $.ajaxFileUpload({
			       url: "app_clidownload.htm?method=uploadImg", //需要链接到服务器地址
			       secureuri: false,
			       fileElementId: id,
			       data:{'size':size},
			       success: function (data, textStatus) {               //相当于java中try语句块的用法
			           if('success'==textStatus){
			        	   	var url=$(data).text();
			                var $upload=$("#"+id);
			                var $pre=$upload.parents("form").find(".img");
			                $upload.siblings().first().val(url);
			                $upload.next().val(url);
			                $pre.eq(index).children("img").removeAttr('src').removeAttr('width').removeAttr('height');
			                $pre.eq(index).children("img").attr('src',url).show().siblings().hide();
			                $upload.parents("form").find("#company_logo").val(url);
			           	}
			       },
			       error: function (data, status, e) {           //相当于java中catch语句块的用法
			       	//alert("上传图片失败！");
					alert(getJsLocaleMessage('appmage','appmage_js_home_uploadimagefalse'));
			       },
			       complete: function(){
			    	   resetfileinput('#'+id);
			       }
			   });
			   $("#"+id).val("");
			}

	function resetfileinput(obj){
		 $(obj).wrap("<form></form>");
	    var $form = $(obj).parent();
	    $form[0].reset();
	    $(obj).unwrap();
	}
		
		
		