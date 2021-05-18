	$(document).ready(function() {
		$("#replycont").manhuaInputLetter({					       
			len : 500,//限制输入的字符个数				       
			showId : "sid"//显示剩余字符文本标签的ID
		});
		getLoginInfo("#getloginUser");
		 everept("replycont");
		 $("#replycont").val(textare);
		 $("#replycont").focus(function(){
			if($.trim($(this).val())==textare){
				 $("#replycont").val('');
				}
			 }).blur(function(){
				if($.trim($(this).val())==''){
					 $("#replycont").val(textare);
					}
				 });
	  });
	   var textare = getJsLocaleMessage("weix","weix_qywx_hfgl_text_36");
	   var path=$("#path").val();
	  function back()
	  {
		  history.go(-1);
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
							  var datarid = (parseInt(i)+1);
							  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_32"));
					    		continue ;
						  }
						  if($("#infomaTable tr").length>=6){
							  alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_33"));
								return;
							  }else{
						  if($("#getObject").get(0).style.display=='none'){
				                $("#getObject").show();
				            }	  
						  $("#infomaTable").append("<tr id='"+keyword+"'><td align='center'  class='div_bd' style='border-left: 0;border-right:0;' name='ConR'  valign='middle'>"+keyword+"</td>"
				       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'><input type='radio'name='rt"+keyword+"' value='1'/></td>"
				       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'><input type='radio'name='rt"+keyword+"' value='0' checked/></td>"
				       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"
				       				+"<a onclick=delKeyword('"+keyword+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");			
						  }
					  }  
			      }
		       }
		  }
		  
		  $("#keyword").val("");

     }
	  function delKeyword(id){
		  $("#"+id).remove(); 
		  var et = reKeyArray();
		  if(et.length<=0){
			  $("#getObject").hide();
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
	     
	 function save(o){
		 //公众帐号
		 $(this).attr('disabled',false);
		 var accuntnum = $("#accuntnum").val();
		 //回复内容
		 var replyname = $("#replyname").val();
		 var replycont = $("#replycont").val();
		 var lgcorpcode = $("#lgcorpcode").val();
		if(isnullstr(replyname)){
			alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_4"));
			 return ;
		 }else{
			 if(replyname.length>32){
				 alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_5"));
				return ;
			 }
		}
		 if(isnullstr(replycont)||replycont==textare){
				alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_35"));
				 return ;
		 }else{
			 if(replycont.length>500){
				 alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_36"));
				return ;
			 } 
		}
		
		 var words = "";
            $("#infomaTable tr").each(function(){
                var id = this.id;
               if(id){
            	   var str = getviBytr(id);
            	   words+=(id+":"+str+",");
	            }
            });
            words = words.substring(0,words.length-1)
            if(isnullstr(words)){
				alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_6"));
				 return ;
		 }
            $.post(path+"/cwc_replymanger.htm?method=saveTextReply",
					{
            			words:words,
            			replyname:replyname,
            			accuntnum:accuntnum,
            			replycont:replycont,
            			lgcorpcode:lgcorpcode
					},
					function(r){
		            		 if(r!=null&&r=="success")
		                     {
		                     	alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_37"));
		                     	location.href = path+'/cwc_replymanger.htm?lgcorpcode='+$("#lgcorpcode").val()+'&pageSize='+$("#pageSize").val();
		                     }
		                     else if(r!=null && r=="fail")
		                     {
		                       alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_38"));
		                     }
		                     else
		                     {
		                         alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_39"))
		                     }
		               });
			 $(this).attr('disabled',true);
	} 
	 function isnullstr(a){
		if(a!=""&&a.length>=0){
			return false;
		}
	   return true;
    }
	function resetkey(){
		$("#accuntnum").val("");
		$("#replycont").val("");
		$("#replyname").val("");
		$("#keyword").val("");
		 $("#infomaTable tr").each(function(){
                var id = this.id;
               if(id){
            	   $(this).remove();
	            }
            });
		 $("#getObject").hide();    
		 $("#sid").html("0"); 
   }	
	function everept(id){
        $("#"+id).bind('click',function(e){
            $(this).val("");
            $(this).unbind("click");
            e.stopPropagation();
          });
    }