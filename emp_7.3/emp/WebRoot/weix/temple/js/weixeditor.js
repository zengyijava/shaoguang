﻿$(document).ready(function(){
  $('#appmsgItem1').find('.sub-msg-opa').css({'height':$('.cover').outerHeight()+'px'});
  $('#appmsgItem1').find('.sub-msg-opa').find('.icostyle').css({'margin-top':'80px'});
  $('.sub-btn-add').bind('click',function(){
	cloneprevappmsgItem();
  })
});

function iconeditClick(obj){
	var height=parseInt($('#msg-preview').css('height'));
	$('.msg-editer-wrapper').css({'height':height-2+'px'});
	//console.log($('#msg-preview').css('height'));
    var content=$('.content').offset().top;
	var dest = $(obj).offset().top;
	//var target=parseInt($('.msg-arrow').css('top'));
	//var mt=(dest==target) ? 0 : (dest-content+84);
	var mt=dest-content;
	//console.log(dest);
	//console.log(mt);
	//if(mt==48){mt=0;}
	//console.log(content);
	//console.log(dest);
	//console.log(target);
	//console.log(mt);
	$(".msg-edit").find(".msg-edit-area").hide();
	var datarid = $(obj).parents(".appmsgItem").attr('data-rid');
	//$(".msg-edit").find(".msg-edit-area").hide();
	if(!$('#msg-edit-area'+datarid).length){
		currentEditDiv = clonemsgeditarea(datarid);
	}
	$('#msg-edit-area'+datarid).show();
	$('.msg-arrow').css("top",mt+'px');
	//$('.msg-edit-area').animate({'marginTop':mt+'px'});
}

function appmsgItemOver(obj){
   $(obj).addClass('sub-msg-opa-show').siblings().removeClass('sub-msg-opa-show');
}
function appmsgItemOut(obj){
   $(obj).removeClass('sub-msg-opa-show');
}
function appmsgItemRemove(obj){
  if($('.msg-item-wrapper').children().length<=4){
    alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_51"));
    return false;
  }
  $(obj).parent().parent().remove();
  window.IE_UNLOAD = true;
  var datarid = $(obj).parents(".appmsgItem").attr('data-rid');
  var currentEditDiv = $(".msg-edit").find("#msg-edit-area"+datarid);
  $(currentEditDiv).remove();
  hideditother(datarid);
  $("#msg-edit-area1").show();
  $('.msg-arrow').css("top",'129px');
}
function hideditother(datarid){
	  var itemIds="";
	  $('.msg-edit').find('.msg-edit-area').each(function(){
		  var idStr = $(this).attr('id');
		  itemIds +=(idStr.replace("msg-edit-area",'')+",");
	  });
	  var s = itemIds.split(",");
	  for(var i in s){
		 if(s[i]==datarid){
			 continue;
		 } 
		 if(s[i]==1){
			 continue;
		 }else{
			 $("#msg-edit-area"+s[i]).hide(); 
		 } 
		  
	  }
}
function renameIdandName(currentEditDiv,datarid){
	$(currentEditDiv).find("#item-title").attr("id","item-title"+datarid).attr("name","item-title"+datarid);
	$(currentEditDiv).find("#item-url").attr("id","item-url"+datarid).attr("name","item-url"+datarid);
	$(currentEditDiv).find("#item-content").attr("id","item-content"+datarid).attr("name","item-content"+datarid);
	$(currentEditDiv).find("#item-link").attr("id","item-link"+datarid).attr("name","item-link"+datarid);
	$(currentEditDiv).find("#item-img").attr("id","item-img"+datarid).attr("name","item-img"+datarid);
	return currentEditDiv;
}

function cloneprevappmsgItem(){
	if($('.msg-item-wrapper').children().length>=10){
	    alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_52"));
	    return false;
	  }
    div=$('.msg-item-wrapper').children(':last').prev('div');
	var newdiv=div.clone();
	newdiv.attr('data-rid',parseInt(div.attr('data-rid'))+1);
	newdiv.find("span.default-tip").css({height:72,width:72,display:'block'}).show();
	newdiv.find("img.default-tip").attr({src:''}).hide();
	newdiv.find(".i-title").html(getJsLocaleMessage("weix","weix_qywx_hfgl_text_19"));
	div.after(newdiv);
	newdiv.attr('id','appmsgItem'+newdiv.attr('data-rid'));
}

function clonemsgeditarea(datarid){
	div = $("#msg-edit-area-hidden").children(':last');
	var newarea = div.clone();
	//console.log(newarea);
	newarea.attr("id","msg-edit-area"+datarid);
	if($(".msg-edit").find("#msg-edit-area"+datarid).size()<1){
		$(".msg-edit").append(newarea);
	}
	
	var currentEditDiv = $(".msg-edit").find("#msg-edit-area"+datarid);
	currentEditDiv = renameIdandName(currentEditDiv,datarid);
	return currentEditDiv;
}
function showTitle(obj){
    var currentareaId = $(obj).parents(".msg-edit-area").attr('id');
    var datarid = currentareaId.replace("msg-edit-area","");
    if(obj.value.length>0){
    	var a = trun(obj.value);
        $("#appmsgItem"+datarid).find(".i-title").html(a);
    }else{
        $("#appmsgItem"+datarid).find(".i-title").html(getJsLocaleMessage("weix","weix_qywx_hfgl_text_19"));
    }

}
function setIputname(obj){
    $(obj).prev().prev().val(obj.value);
 }
function trun(str){
    if(str==null) return "";
      var html = str;
      html = html.replace(new RegExp("&","gm"), "&amp;");
      html = html.replace(new RegExp(" ","gm"), "&nbsp;");
      html = html.replace(new RegExp("<","gm"), "&lt;");
      html = html.replace(new RegExp(">","gm"), "&gt;");

 return html;
 }