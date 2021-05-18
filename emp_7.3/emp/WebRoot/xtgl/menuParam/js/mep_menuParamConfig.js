//改变机构是否计费时，动态改变top页面的机构余额的是否显示
function changeTopYe(isBalance)
{
	var url=window.parent;
	for(var i=0;i<6;i++)
	{
		if(typeof(url.frames["mainFrame"])=="undefined")
		{
			url=url.parent;
		}
		else
		{
			break;
		}
	}
	var $par = $(url.window.document);
	//0.不计费不显示机构余额   1.计费模式显示机构余额
	if(isBalance==0)
	{
		$par.find("#smsYe").css("display","none");
		$par.find("#mmsYe").css("display","none");
	}
	else if(isBalance==1)
	{
		$par.find("#smsYe").css("display","");
		$par.find("#mmsYe").css("display","");
	}
}

function changePrivilege(isBalance, skin) {
	var oneMenu;
	var flag = false;
	//0.不显示机构余额   1.显示机构余额
	if(skin.indexOf("frame4.0") > -1){
		oneMenu = $(window.parent.document).find("#leftIframe").contents().find("#mod12");
		var secondMenu = oneMenu.children('ul').eq(0).children('li').eq(2).children('ul').find("li");
		if(isBalance === "0") {
			flag = existModule(secondMenu, "充值/回收管理");
			if(flag){
				$(secondMenu.eq(3)).remove();
			}
		}
		if(isBalance === "1"){
			flag = existModule(secondMenu, "充值/回收管理");
			if(!flag){
				secondMenu.eq(2).after("<li onmouseover=\"mouseOver('1600-1320')\" onmouseout=\"mouseOut('1600-1320')\" title=\"充值/回收管理\" onclick=\"doOpen('/cha_balanceMgr.htm','1600-1320','充值/回收管理')\">" +
					"<a id=\"ak1600-1320\" style=\"color: rgb(189, 195, 211);\">充值/回收管理</a></li>");
			}
		}
	}else {
		oneMenu = $(window.parent.document).find("#leftIframe").contents().find("#mod12").find("#sider").children('ul').eq(0).children("li").eq(2).children('ul').find("li");
		if(isBalance === "0") {
			flag = existModuleOld(oneMenu, "充值/回收管理");
			if(flag){
				$(oneMenu.eq(3)).remove();
			}
			//如果是3.0还需要去除顶上的
			if(skin.indexOf("frame3.0") > -1){
				$(window.parent.document).find("#topLink").find("div[menucode='1600-1320']").remove();
			}
		}
		if(isBalance === "1"){
			flag = existModuleOld(oneMenu, "充值/回收管理");
			if(!flag){
				oneMenu.eq(2).after("<li class=\" \"><a id=\"ak1600-1320\" onclick=\"doOpen('/cha_balanceMgr.htm','1600-1320', '充值/回收管理')\" onmouseover=\"mouseOver('1600-1320')\" onmouseout=\"mouseOut('1600-1320')\" class=\" \">充值/回收管理</a></li>");
			}
		}
	}
}

function existModule(secondMenu, module) {
	var flag = false;
	for(var j = 0;j < secondMenu.length;j++){
		if(module === $.trim(secondMenu[j].getAttribute("title"))){
			flag = true;
		}
	}
	return flag;
}

function existModuleOld(secondMenu, module) {
	var flag = false;
	for(var j = 0;j < secondMenu.length;j++){
		if(module === $.trim($(secondMenu[j]).find("a").text())){
			flag = true;
		}
	}
	return flag;
}



function bycheck(style)
{
	  /*if(style == "0")
	  {
		 $("#tabBody1").hide();
	     $("#tabBody0").show();
	     $("#tabBody2").hide();
	     $("#tabBody3").hide();
	     $("#img1").removeClass("fold");
		 $("#img1").addClass("unfold");
	     $("#img0").removeClass("unfold");
		 $("#img0").addClass("fold");
		 $("#img2").removeClass("fold");
		 $("#img2").addClass("unfold");
		 $("#img3").removeClass("fold");
		 $("#img3").addClass("unfold");
	  } else */
	  if(style == "1")
	  {
		 //$("#tabBody0").hide();
	     $("#tabBody1").show();
	     $("#tabBody2").hide();
	     $("#tabBody3").hide();
	     $("#tabBody4").hide();
	     $("#tabBody5").hide();
	     //$("#img0").removeClass("fold");
		 //$("#img0").addClass("unfold");
	     $("#img1").removeClass("unfold");
		 $("#img1").addClass("fold");
		 $("#img2").removeClass("fold");
		 $("#img2").addClass("unfold");
		 $("#img3").removeClass("fold");
		 $("#img3").addClass("unfold");
		 $("#img4").removeClass("fold");
		 $("#img4").addClass("unfold");
		 $("#img5").removeClass("fold");
		 $("#img5").addClass("unfold");
	  }
	  else if(style == "2")
	  {
		// $("#tabBody0").hide();
	     $("#tabBody1").hide();
	     $("#tabBody2").show();
	     $("#tabBody3").hide();
	     $("#tabBody4").hide();
	     $("#tabBody5").hide();
	     //$("#img0").removeClass("fold");
		 //$("#img0").addClass("unfold");
	     $("#img2").removeClass("unfold");
		 $("#img2").addClass("fold");
		 $("#img1").removeClass("fold");
		 $("#img1").addClass("unfold");
		 $("#img3").removeClass("fold");
		 $("#img3").addClass("unfold");
		 $("#img4").removeClass("fold");
		 $("#img4").addClass("unfold");
		 $("#img5").removeClass("fold");
		 $("#img5").addClass("unfold");
	  }else if(style == "3")
	  {
		     //$("#tabBody0").hide();
		     $("#tabBody1").hide();
		     $("#tabBody2").hide();
		     $("#tabBody3").show();
		     $("#tabBody4").hide();
		     $("#tabBody5").hide();
		     //$("#img0").removeClass("fold");
			 //$("#img0").addClass("unfold");
		     $("#img2").removeClass("fold");
			 $("#img2").addClass("unfold");
			 $("#img1").removeClass("fold");
			 $("#img1").addClass("unfold");
		     $("#img3").removeClass("unfold");
			 $("#img3").addClass("fold");
			 $("#img4").removeClass("fold");
			 $("#img4").addClass("unfold");
			 $("#img5").removeClass("fold");
			 $("#img5").addClass("unfold");
	  }else if(style == "4")
	  {
		     //$("#tabBody0").hide();
		     $("#tabBody1").hide();
		     $("#tabBody2").hide();
		     $("#tabBody3").hide();
		     $("#tabBody4").show();
		     $("#tabBody5").hide();
		     //$("#img0").removeClass("fold");
			 //$("#img0").addClass("unfold");
		     $("#img2").removeClass("fold");
			 $("#img2").addClass("unfold");
			 $("#img1").removeClass("fold");
			 $("#img1").addClass("unfold");
		     $("#img3").removeClass("fold");
			 $("#img3").addClass("unfold");
			 $("#img4").removeClass("unfold");
			 $("#img4").addClass("fold");
			 $("#img5").removeClass("fold");
			 $("#img5").addClass("unfold");
		  }else if(style == "5")
		  {
			     //$("#tabBody0").hide();
			     $("#tabBody1").hide();
			     $("#tabBody2").hide();
			     $("#tabBody3").hide();
			     $("#tabBody4").hide();
			     $("#tabBody5").show();
			     //$("#img0").removeClass("fold");
				 //$("#img0").addClass("unfold");
			     $("#img2").removeClass("fold");
				 $("#img2").addClass("unfold");
				 $("#img1").removeClass("fold");
				 $("#img1").addClass("unfold");
			     $("#img3").removeClass("fold");
				 $("#img3").addClass("unfold");
				 $("#img4").removeClass("fold");
				 $("#img4").addClass("unfold");
				 $("#img5").removeClass("unfold");
				 $("#img5").addClass("fold");
			  }
}


  $(function(){
    var checkbox=$(".exam_range input[type='checkbox']");
	var inp_text=$(".exam_total input[type='text']");
    checkbox.click(function(){
	  $(this).parent().find('span').toggle();
	});
	
    inp_text.each(function(){
	  var placeholder=$(this).attr('data-placeholder');
	
	  $(this).focus(function(){
	    if($(this).val()=='' || $(this).val()==placeholder){
		  $(this).val('');
		}
	  }).blur(function(){
	    if($(this).val()==''){
		 $(this).val(placeholder);
		}
	  }).keyup(function(){
		  numberControl($(this));
		  
	  });
	});
  });
  function openAndClose(val){
	    if(val=='1'){
		  $(".checkbox").attr("checked",true);
		  $(".checkbox").attr("disabled","");
		  $(".exam_range").css("color","black");
		  $('.exam_total').show();
		}
		if(val=='2'){
		  $(".checkbox").attr("checked",false);
		  $(".checkbox").attr("disabled","disabled");
		  $(".exam_range").css("color","#808080");
		  $('.exam_total').hide();
		}  
	  }
  
  function isShow(element){
	  numberControl($(element));
	  var modiftime=$("#modiftime").val();
	  if(modiftime!=''&&parseInt(modiftime)!='0'){
		  $("#remind").attr("disabled",false);
	  }else{
		  $("#remind").attr("disabled",true);
		  $("#remind").val("");
	  }
  }
  
  function isBig(element){
	  if(element!=""){
		  numberControl($(element));
	  }
	  var modiftime=$("#modiftime").val();
	  var remind=$("#remind").val();
	  if(parseInt(modiftime)<=parseInt(remind)&&(parseInt(modiftime)!='0'&&parseInt(remind)!='0')){
		  alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_50"));
		  return true;
	  }
 
  }
  