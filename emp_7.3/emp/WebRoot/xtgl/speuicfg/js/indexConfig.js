//		function getlengthB(str){
//		     return str.replace(/[^\x00-\xff]/g,"**").length;
//		     }
		var path=$("#path").val();
		function upload(id){
		var imgtem=$("#"+id).val();
       	if(imgtem.length<1){
	    	return ;	
	    }else{
	    	var c = ".jpg|.jpeg|.png|";
	        var b = imgtem.substring(imgtem.lastIndexOf("."),imgtem.length).toLowerCase();
            if(c.indexOf(b)<0){
	            alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_51"));
	            resetfileinput('#'+id);
	            return;
	        }
		}
		var size=280;
		var index=0;
		if(id=='back_pic'){
				size=2*1024;
				index=1;
			}
    	   $.ajaxFileUpload({
               url: "par_indexCfg.htm?method=uploadImg", //需要链接到服务器地址
               secureuri: false,
               fileElementId: id,
               data:{'size':size},
               success: function (data, textStatus) {               //相当于java中try语句块的用法
                   if('success'==textStatus){
                	   var url=$(data).text();
                      if(url.indexOf("overSize")!=-1){
                          alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_52"));
                      }else{
                          var $upload=$("#"+id);
                          var $pre=$upload.parents("form").find(".img");
                          $upload.siblings().first().val(url);
                          $upload.next().val(url);
                          $pre.eq(index).children("img").removeAttr('src').removeAttr('width').removeAttr('height');
                          $pre.eq(index).children("img").attr('src',url).show().siblings().hide();
                          $upload.parents("form").find(".icoBox").children().eq(index).click();
                      	}
   	            	}
               },
               error: function (data, status, e) {           //相当于java中catch语句块的用法
               	alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_53"));
               },
               complete: function(){
            	   resetfileinput('#'+id);
               }
           });
		}
		//重置上传文件控件
		function resetfileinput(obj){
			 $(obj).wrap("<form></form>");
		     var $form = $(obj).parent();
		     $form[0].reset();
		     $(obj).unwrap();
		}
		$(document).ready(function(){
			getLoginInfo("#getloginUser");
			$(".img img").load(function(){
					var $div=$(this).parents(".showImg");
					var w=$(this)[0].width;
					var h=$(this)[0].height;
					var W=$div[0].offsetWidth;
					var H=$div[0].offsetHeight;
					var r=W/w;
					if(W/w>H/h){
							r=H/h;
						}
					$(this)[0].width=w*r;
					$(this)[0].height=h*r;
					$(this).css('vertical-align','middle');
				})
			
			$("input:text").bind('keyup blur',function(){
				var reg=/['<>"]/g;
				var val=$(this).val();
				if(reg.test(val)){
					$(this).val($(this).val().replace(reg,''));
							}
		  })
			$(".submit").click(function(){
					var $form=$(this).parents("form");
					var param='';
					var name=$(this).attr('name');
					var companyLogo=$("input[name='company_logo']").val();
					if(name==1&&companyLogo==''){
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_54"));
						return;
					}
					$form.find("input[type='checkbox']").each(function(){
							if($(this).attr("checked")=='checked'){
									param=param.concat('1');
							}else{
									param=param.concat('0');
								}
						});
					while(param.length>0&&param.length<8){
						param=param.concat('0');
						}
					$form.attr('action',$form.attr('action')+'&lgcorpcode='+$("#lgcorpcode").val()+'&param='+param);
					$form.ajaxSubmit({
		            dataType:'html',
		            type:'POST',
		            success:function(data){
			            if(data==1){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_55"));
							window.location.href=location;
							//动态的修改topFrame的内容
							window.parent.parent.frames['topFrame'].location.reload();
							//如果为1 则表示更新了内页logo 需重新加载top框架页
							if(name==1){
								//window.parent.parent.frames['topFrame'].location.reload();
							}
				        }else{
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_56"));
					          if(data!=0)
					            {
					            	window.location.href=location;
					            }
					   }
					},
					error:function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_57"));	 },
					complete:function(){
		            	$(this).attr('disabled', false);
					},
					beforeSubmit:function(){
						$(this).attr('disabled', true);
					}
					});
				});

			$(".default").click(function(){
				if(!confirm(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_58"))){
					return;
				}
				var $form=$(this).parents("form");
				var name=$(this).attr('name');
				$form.attr('action',$form.attr('action')+'&lgcorpcode='+$("#lgcorpcode").val()+'&default='+name);
				$form.ajaxSubmit({
	            dataType:'html',
	            type:'POST',
	            success:function(data){
		            if(data==1){
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_59"));
						window.location.href=location;
						//如果为1 则表示更新了内页logo 需重新加载top框架页
						//if(name==1){
						window.parent.parent.frames['topFrame'].location.reload();
						//}
			         }else{
			            	alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_60"));
				            if(data!=0)
				            {
				            	window.location.href=location;
				            }
				     }
				},
				error:function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_gxhjmsz_57"));	 },
				complete:function(){
	            	$(this).attr('disabled', false);
				},
				beforeSubmit:function(){
					$(this).attr('disabled', true);
				}
				});
			})
				$('.files').live('change',function(){
					var id = $(this).attr('name');
					upload(id);
				})
			
			});
		