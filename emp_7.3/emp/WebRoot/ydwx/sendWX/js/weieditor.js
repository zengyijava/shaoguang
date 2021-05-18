$(document).ready(function(){
  $('#appmsgItem1').find('.sub-msg-opa').css({'height':$('.cover').outerHeight()+'px'});
  $('#appmsgItem1').find('.sub-msg-opa').find('.icostyle').css({'margin-top':'80px'});
  $('.sub-btn-add').bind('click',function(){
    div=$('.msg-item-wrapper').children(':last').prev('div');
	var newdiv=div.clone();
	newdiv.attr('data-rid',parseInt(div.attr('data-rid'))+1);
	div.after(newdiv);
	newdiv.attr('id','appmsgItem'+newdiv.attr('data-rid'));
  })
});

function iconeditClick(obj){
    var content=$('.content').offset().top;
	var dest = $(obj).parent().parent().parent().offset().top;
	var target=parseInt($('#msg-edit-area').css('margin-top'));
	var mt=(dest==target) ? 0 : (dest-content);
	if(mt==59){mt=33;}
	mt && $('.msg-edit-area').animate({'marginTop':mt+'px'});
}

function appmsgItemOver(obj){
   $(obj).addClass('sub-msg-opa-show').siblings().removeClass('sub-msg-opa-show');
}
function appmsgItemOut(obj){
   $(obj).removeClass('sub-msg-opa-show');
}
function appmsgItemRemove(obj){
  if($('.msg-item-wrapper').children().length<4){
    alert("无法删除，多条图文至少需要2条消息。");
    return false;
  }
  $(obj).parent().parent().parent().remove();
}