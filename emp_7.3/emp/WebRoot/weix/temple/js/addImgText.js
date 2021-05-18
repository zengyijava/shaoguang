
		function currtime(){
			var  today= new Date();
             var the;
             the = (today.getFullYear())+"-" + (morth(today.getMonth()+1))+"-" +today.getDate();
             return the;
		}
		function morth(obj){
			return (obj<10)?("0"+obj):obj;
		}
		
		
		  
		  function getviBytr(id){
	            var as = "";
	              $("#"+id+" input:radio").each(function () {
	                     if(this.checked){
	                       as= this.value;
	                     }
	                 });
	            return as;
	        }
		  function addKeyword(){
			  var commonPath = $("#commonPath").val();
			  var keywords = $("#keyword").val();
			  if(isChinese(keywords)){
			  var arr = reKeyArray();	  
			  if(keywords!=null&&keywords.length>=1){
				  
				  var words = outRepeat(keywords.split(" "));
				  for(var i in words){
					  var keyword = words[i];
					  if(keyword.length>0){
						  if(isContain(arr,keyword)){
							  continue;
						  }
						  if(keyword.length>16){
							  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_1"));
					    		continue ;
						  }
						  if($("#infomaTable tr").length>6){
							  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_2"));
								return;
							  }else
								  $('.noKewords').hide();
						  $("#infomaTable").append("<tr  id='"+keyword+"'><td class='div_bd'>"+keyword+"</td>"
				       				+"<td class='div_bd'><input type='radio'name='rt"+keyword+"' value='1'/></td>"
				       				+"<td class='div_bd'><input type='radio'name='rt"+keyword+"' value='0' checked/></td>"
				       				+"<td class='div_bd'>"
				       				+"<a onclick=delKeyword('"+keyword+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");			
						  }
			      }
		       }
			  }
			  
			  $("#keyword").val("");

	     }
		  function delKeyword(id){
			  $("#"+id).remove(); 
			  if($("#infomaTable tr").length==2){
								$('.noKewords').show();
							  }
			  
		  }
		  function isChinese(str)
	        {
	            var pattern = /^[0-9a-zA-Z_ \u4e00-\u9fa5]+$/i;
	            if (pattern.test(str))
	            {

	              return true;
	            }
	            else
	            {
	              alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_3"));
	              return false;

	            }
	       } 
		//去掉重复元素
	        function outRepeat(arr){
	            var newArr = [];
	            arr.sort();
	            var temp= "";
	            for(var i=0;i<arr.length;i++){
	                if(arr[i] != temp){
	                   newArr.push(arr[i]);
	                    temp = arr[i];
	                }
	            }
	            return newArr;
	        } 
		 function reKeyArray(){
			 var a = [];
			 $("#infomaTable tr").each(function(){
	                var id = this.id;
	               if(id){
	            	   a.push(id);
		            }
	            });
			 return a;
		 } 
		 function isContain(arr,key){
	            for(var i=0;i<arr.length;i++){
	                if(arr[i] == key){
	                    return true;
	                }
	            }
	            return false;
	        }
		 function saveKeywords(){
			 var words = "";
	            $("#infomaTable tr").each(function(){
	                var id = this.id;
	               if(id){
	            	   var str = getviBytr(id);
	            	   words+=(id+":"+str+",");
		            }
	            });
	            words = words.substring(0,words.length-1)
	            $("#words").val(words);

		} 
	
		function checkimgupload(){
			if(isnullstr($("#item-name").val())){
				alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_4"));
				return false;
		    }else{
		    	if($("#item-name").val().length>32){
					 alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_5"));
					return false;
				 }
		    }
			saveKeywords();
			if(isnullstr($("#words").val())){
				alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_6"));
				return false;
		    }
			if(!judeitemtitle()){
				return false;
			}//判断个体图文是否添加了标题
			setitemids();
			return true;
	   }
		function isnullstr(a){
			if(a!=""&&a.length>=0){
				return false;
			}
		   return true;
	    }	   

	  function resetimg(){
		  $("#infomaTable tr").each(function(){
                var id = this.id;
               if(id){
            	   $(this).remove();
	            }
            });
		  $('.noKewords').show();
		  clearitemtitle();
            
		  
	  }  


	  function setitemids(){
		  var ids=[];
		  $('.msg-edit').find('.msg-edit-area').each(function(){
			  var idStr = $(this).attr('id');
				  ids.push(idStr.replace("msg-edit-area",''));
		  });
		  var itemIds = ids.join(","); 
		  $("#item-ids").attr("value",itemIds);
		 
	  }
	  
	  function nulls(obj){
			 if(obj=='null'){
	               return "";
	         }else{
		         return obj;
	         }    
		}  

	  function strlen(str){
          var len = 0;
        for (var i=0; i<str.length; i++) {
           var c = str.charCodeAt(i);
          //单字节加1
            if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
             len++;
            }
           else {
            len+=2;
             }
         }
           return len;
       }
	//ajaxsubmit提交  
		$(document).ready(function(){
			$("#subSend").click(function(){
				var flag=checkimgupload();
				var pageSize=$(this).next().val();
				if(flag){
					var $form=$(this).parents("form");
					$form.ajaxSubmit({
		            dataType:'html',
		            type:'POST',
		            success:function(data){
						if(data.indexOf('@')>-1){
							data=data.replace('@','');
							alert(data);
			   				$form.attr('action', 'cwc_replymanger.htm?lgcorpcode='+$("#lgcorpcode").val());
			   				$form.submit();
						}else{
							alert(data);
						}
					},
					error:function(){alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_7"));	 },
					complete:function(){
		            	$(this).attr('disabled', false);
					},
					beforeSubmit:function(){
						$(this).attr('disabled', true);
					}
					});
				}
				})
			});
		