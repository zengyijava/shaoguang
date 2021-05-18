;(function($){
  $.fn.isSearchSelect=function(opt,callback){
    opt=$.extend({
	  selectBox:"c_selectBox",//最外层模拟select的div
	  input:'c_input',//显示数据用的input
	  selectItem:'c_result',
	  selectImg:'c_selectimg',
	  selectedItem:'selected',
	  select_bd:" div_bd",
	  isInput:true,//是否允许手动输入
	  width:200,//模拟select的宽度
	  inputWidth:178,
	  height:300,//select的最大高度
	  zindex:20//层级顺序
	},opt || {});
	
	$(this).hide();//select默认隐藏
	return this.each(function(){
	  var self=this;
	  
	  var thisBox,thisInputVal,thisselectItem,thisselectItem_li,thisselect,selectObj={},display="";
	  opt.width=(opt.width==0) ? $(self).width() : opt.width;
	  opt.inputWidth=opt.width-18;
	  var html="<div class='"+opt.selectBox+opt.select_bd+"' style=width:"+opt.width+"px><input name='input' class="+opt.input+" style='width:"+opt.inputWidth+"px;height:16px;' value=''/><div class="+opt.selectImg+"></div><ul class="+opt.selectItem+" style='width:"+opt.width+"px'>";
	  //添加下拉框列表项
	  

	  $(self).find('option').each(function(){
		    /*if($(this).val()>0){
		    	display="";
		    }else{
		    	display=" hide";
		    }*/
		    if($(this).is(':selected')){
			  html+="<li class='"+opt.selectedItem+display+"'>"+$(this).text()+"</li>";
			  
			}else{
					html+="<li class='"+display+"'>"+$(this).text()+"</li>";
			}
	    
	  })
	  html+="</ul></div>";
	  $(self).after(html);
	  
	  thisBox=$(self).next('.'+opt.selectBox);
	  thisInputVal=thisBox.find('.'+opt.input);
	  thisselectItem=thisBox.find('.'+opt.selectItem);
	  thisselectItem_li=thisBox.find('.'+opt.selectItem).find('li');
	  thisselect=thisBox.find('.'+opt.selectImg);
	  thisInputVal.val($(self).find("option:selected").text());
	  if(opt.isInput){
	    thisInputVal.bind('keyup',function(event){
		 inputKeyup(this,event);
		 thisselect.click(function(event){
		  selectToggle(event);
		});
		})
	  }else{
	    thisInputVal.attr('disabled',true);
		thisBox.click(function(event){
		  selectToggle(event);
		})
		
		
	  }
	   
	  

	  thisselectItem.width(opt.width);
	  if(thisselectItem.height()>opt.height){
        thisselectItem.height(opt.height);
       }
	   
	  thisselectItem_li.click(function(e){
	    $(this).addClass(opt.selectedItem).siblings().removeClass(opt.selectedItem);
	    thisInputVal.val($(this).text());
		cIndex=thisselectItem.find('li').index(this);
		$(self).find('option').attr('selected',false);
		$(self).find('option').eq(cIndex).attr('selected','selected');
		thisselectItem.hide();
		thisBox.css({'z-index':opt.zindex});
		selectObj.value=$(self).find('option').eq(cIndex).val();
		selectObj.text=$(self).find('option').eq(cIndex).text();
		if(typeof callback=='function'){
		  callback(selectObj);
		}
		 e.stopPropagation();
	  }).hover(function(){
	    $(this).addClass(opt.selectedItem).siblings().removeClass(opt.selectedItem);
	  },function(){
	    $(this).removeClass(opt.selectedItem);
	  })
	  
	  $('html,body').click(function(){
        
        //thisselectItem.hide();
        
		thisBox.css({'z-index':opt.zindex});
      });
	  var timer;
	  thisselectItem.mouseover(function(){
		  clearTimeout(timer);
	  }).mouseout(function(){
		  var _this=this;
		  timer=setTimeout(function(){
			  $(_this).hide();
		  },200);
		  
	  })
	  
	  function inputKeyup(obj,event){
	    var _this=obj;
	    $('.'+opt.selectItem).hide();
		thisselectItem_li.show();
		var text=$(_this).val();
		var reg = new RegExp(text,"i");
		thisselectItem_li.each(function(i){
		  var ithat=this;
		  var flag=reg.test($(ithat).text());
		  if(flag && text!=''){
		    $(ithat).show();
		  }else{
		    $(ithat).hide();
		  }
		})
		thisselectItem.show();
		thisBox.css({'z-index':opt.zindex+1});
		event.stopPropagation();
	  }
	  
	  
	  function selectToggle(event){
		 
	    thisselectItem_li.not('.hide').show();
		thisselectItem.toggle();
	    
		if(thisselectItem.is(":visible")){
		  thisBox.css({'z-index':opt.zindex+1});
		}else{
		  thisBox.css({'z-index':opt.zindex});
		}
		  event.stopPropagation();
          
	  }
	
	
	})
  
  };
  /* $.fn.isSearchSelect.myDefined = function(callback) {    
        
		  return callback();
		 
      }; */
  
})(jQuery);