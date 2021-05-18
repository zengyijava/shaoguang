//ul模拟select
			$('#getChooseMan').live('click',function(e){
			   if($(this).find('li').size()>0){
			     var target = e.target,$target = $(target);
			     if (target.nodeName === 'LI') {
				    if($target.hasClass('cur')){
				      $target.removeClass('cur');
				      $('#right').find('option').each(function(){
				        if($(this).val()==$target.attr('dataval')){
				          $(this).attr('selected',false);
				         
				        }
				      })
				    }else{
				      $target.addClass('cur');
				      $('#right').find('option').each(function(){
				        if($(this).val()==$target.attr('dataval')){
				          $(this).attr('selected','selected');
				        }
				      })
				    }
				  }
			     
			   }
			})