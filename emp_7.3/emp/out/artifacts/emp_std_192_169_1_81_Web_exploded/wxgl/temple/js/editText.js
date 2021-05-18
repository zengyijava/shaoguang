$(document).ready(function() {
    var str=$("#msgleng").val();
	$("#replycont").manhuaInputLetter({					       
		len : 500,//限制输入的字符个数				       
		showId : "sid",//显示剩余字符文本标签的ID
		showNum:str
	});
	 $.ajaxSetup({
		    async : false
		});	      
	getLoginInfo("#getloginUser");
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
	  
	  
 });
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
						  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_34"));
				    		continue ;
					  }
					  if($("#infomaTable tr").length>=6){
						  alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_35"));
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
	       }}
	  
	  $("#keyword").val("");

}
 function delKeyword(id){
	  $("#"+id).remove(); 
	  var et = reKeyArray();
	  if(et.length<=0){
		  $("#getObject").hide();
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
    	   alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_36"));
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
function save(o){
	 $(o).attr('disabled',false);
	 //回复内容
	 var replycont = $("#replycont").val();
	 var accoutid = $("#accoutid").val();
	 var replyname = $("#replyname").val();
	 var lgcorpcode = $("#lgcorpcode").val();
	 var accuntnum = $("#accuntnum").val();
	 if(isnullstr(replyname)){
		 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_37"));
			 return ;
	 }else{
		 if(replyname.length>32){
			 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_38"));
			return ;
		 }
	}
	 if(isnullstr(replycont)){
		 	alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_39"));
			 return ;
	 }
	 else{
		 if(replycont.length>500){
			 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_40"));
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
			alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_41"));
			 return ;
	 }
       $.post(path+"/weix_keywordReply.htm?method=editTextReply",
				{
       			words:words,
       			accoutid:accoutid,
       			replyname:replyname,
       			replycont:replycont,
       			lgcorpcode:lgcorpcode,
       			accuntnum:accuntnum
				},
				function(r){
	            		 if(r!=null&&r=="success") {
	            			 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_58"));
		            		$("form[name='updateInfo']").submit();
	                     	}
	                     else{
	                    	 alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_59"));
	        				 $(o).attr('disabled',true);
	                     }
	               });
} 
function isnullstr(a){
		if(a!=""&&a.length>=0){
			return false;
		}
	   return true;
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