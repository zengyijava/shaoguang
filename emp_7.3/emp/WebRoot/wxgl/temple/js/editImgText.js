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
				  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_1"));
		    		continue ;
			  }
			  if($("#infomaTable tr").length>6){
				  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_2"));
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
  var et = reKeyArray();
  if(et.length<=0){
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
    	 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_3"));
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
		 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_4"));
		return false;
    }else{
    	if($("#item-name").val().length>32){
    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_5"));
			return false;
		 }
   }
	saveKeywords();
	if(isnullstr($("#words").val())){
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_6"));
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
		if(flag){
			var $form=$(this).parents("form");
			console.log()
			$.ajax({
				url:$form.attr("action"),
				data:$form.serialize(),
				dataType:'html',
	            type:'POST',
	            success:function(data){
					if(data.indexOf('@')>-1){
						data=data.replace('@','');
						alert(data);
						$form.attr('action', 'weix_keywordReply.htm?lgcorpcode='+$("#lgcorpcode").val());
						    $form.submit();
					}else{
						alert(data);
					}
				},
				error:function(){alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_7"));},
				complete:function(){
		        	$(this).attr('disabled', false);
				},
				beforeSubmit:function(){
					$(this).attr('disabled', true);
				}
			});
//			$form.ajaxSubmit({
//            dataType:'html',
//            type:'POST',
//            success:function(data){
//				if(data.indexOf('@')>-1){
//    				data=data.replace('@','');
//    				alert(data);
//    				$form.attr('action', 'weix_keywordReply.htm?lgcorpcode='+$("#lgcorpcode").val());
// 				    $form.submit();
//				}else{
//					alert(data);
//				}
//			},
//			error:function(){alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_7"));},
//			complete:function(){
//            	$(this).attr('disabled', false);
//			},
//			beforeSubmit:function(){
//				$(this).attr('disabled', true);
//			}
//			});
		}
});

	var commonPath = $("#commonPath").val();
	  var keywords = $("#keywords").val();
	  if(keywords!=null&&keywords.length>1){
		  var words = keywords.split(",");
		  for(var i in words){
			  var keyword = words[i].split(":");
			  var type1,type0;
			  if(keyword[1]=="1"){
				  type1 = "checked";
				  type0="";
			  }else if(keyword[1]=="0"){
				  type0 = "checked";
				  type1="";
			  }	
			  $("#infomaTable").append("<tr id='"+keyword[0]+"'><td align='center'  class='div_bd' style='border-left: 0;border-right:0;' name='ConR'  valign='middle'>"+keyword[0]+"</td>"
	       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'><input type='radio'name='rt"+keyword[0]+"' value='1' "+type1+" /></td>"
	       				+"<td align='center'  class='div_bd' style='border-left:0;border-right:0;' valign='middle'><input type='radio'name='rt"+keyword[0]+"' value='0' "+type0+"/></td>"
	       				+"<td align='center' class='div_bd' style='border-left:0;border-right:0;' valign='middle'>"
	       				+"<a onclick=delKeyword('"+keyword[0]+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
	      }
       }

		var rimgs=$("#rimgs").html();
		rimgs=eval("("+rimgs+")");
		if(rimgs!=null&&rimgs.length>0){
		for(var i=0;i<rimgs.length;i++){
			var datarid=parseInt(i)+1;
			var rimg=rimgs[i];
			   if(datarid>2){
				   cloneprevappmsgItem();
			   }
			   if(datarid>1){
			       clonemsgeditarea(datarid);
			   }
				$("#item-title"+datarid).attr("value",rimg.title);
			    $("#item-content"+datarid).val(rimg.description);
			    // 初始化编辑器
			    createEditor("myEditor"+datarid);
			    UM.getEditor("myEditor"+datarid).setContent(rimg.description);
			    
		    	$("#item-link"+datarid).val(rimg.link);
		    	if($("#summaryDiv").length==1)
		    	{
		    	    $("#item-summary"+datarid).val(rimg.summary);
		    	    $("#summaryDiv").text(rimg.summary);
		    	}
			    $("#item-url"+datarid).val(rimg.picurl);
			    if("1" == rimg.source_url){
			    	$("#item-source"+datarid).attr("checked","checked");
			    }
				
			    $(".msg-edit").find("#msg-edit-area"+datarid).hide();
			    $("#appmsgItem"+datarid).find(".default-tip").hide();
			    $("#appmsgItem"+datarid).find(".i-title").text(rimg.title);
			    $("#appmsgItem"+datarid).find("img.default-tip").attr({"src":rimg.picurl,height:72,width:72}).show();
			}
		}else{
			  createEditor("myEditor1");
			  UM.getEditor("myEditor1").setContent("");
		}
		
		$("#appmsgItem1").find("img.default-tip").attr({height:160,width:315});
		$(".msg-edit").find("#msg-edit-area1").show();
		
		
		//字符限制处理问题  
		$("textarea[maxlength]").blur(function() {
			checkAreaLen($(this))
		});
		$("textarea[maxlength]").keydown(function() {
			checkAreaLen($(this))
		});
		$("textarea[maxlength]").keyup(function() {
			checkAreaLen($(this))
		});
	
});
function checkAreaLen(obj){
	var area = obj;//$(this);
	var max = parseInt(area.attr("maxlength"), 10); // 获取maxlength的值
	if (max > 0) {
		if (area.val().length > max) { // textarea的文本长度大于maxlength
			area.val(area.val().substr(0, max)); // 截断textarea的文本重新赋值
		}
	}
}
		